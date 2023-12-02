use ndarray::Array2;
use std::collections::HashMap;

use crate::pso::particle::Particle;
use crate::simulation::data_center::DataCenter;
use crate::simulation::workload::Workload;

pub struct PSOSwarm {
  pub max_iterations: u32,
  pub n_particles: u32,
  pub p_s: f32,
  pub w: f32,
  pub global_best_objective: f32,

  pub global_best_th: f32,
  pub global_best_tec: f32,

  pub global_best_position: Array2<f32>,
  pub global_best_task_vm_mapping: HashMap<usize, usize>,
  pub particles: Vec<Particle>,
  pub workload: Workload,
  pub data_center: DataCenter,
}

impl PSOSwarm {
  const W1: f32 = 0.9;
  const W2: f32 = 0.4;

  pub fn new(mut workload: Workload, mut data_center: DataCenter) -> PSOSwarm {
    let max_iterations: u32 = 200;
    let n_particles: u32 = 20;
    let p_s: f32 = 1.;
    let w: f32 = 0.;

    let mut particles = Vec::new();
    for _ in 0..n_particles {
      particles.push(Particle::new(&mut workload, &mut data_center));
    }

    let global_best_task_vm_mapping: HashMap<usize, usize> = HashMap::new();
    let global_best_position = particles.get(0).unwrap().position.clone();

    let mut swarm = PSOSwarm {
      max_iterations: max_iterations,
      n_particles: n_particles,
      p_s: p_s,
      w: w,
      global_best_objective: 0.,
      global_best_position: global_best_position,
      global_best_th: 0.0,
      global_best_tec: 0.0,
      global_best_task_vm_mapping: global_best_task_vm_mapping,
      particles: particles,
      workload: workload,
      data_center: data_center,
    };

    swarm.find_global_best();

    return swarm;
  }

  pub fn run_pso_algorithm(&mut self) {
    for iteration in 0..self.max_iterations {
      self.run_iteration(iteration);
    }

    self.update_task_vm_mapping();
  }

  fn run_iteration(&mut self, iteration: u32) {
    self.w = ((PSOSwarm::W1 - PSOSwarm::W2) / self.p_s) + 
             (((self.max_iterations - iteration) as f32) / (self.max_iterations as f32)) * 
             ((PSOSwarm::W1 - (PSOSwarm::W1 - PSOSwarm::W2)) / self.p_s);

    let mut ss: f32 = 0.;

    for particle in self.particles.iter_mut() {
      ss += particle.run_iteration(&self.workload, &mut self.data_center, self.w, &self.global_best_position) as f32;
    }

    self.find_global_best();

    self.p_s = ss / (self.n_particles as f32);

    if self.p_s <= 0. {
      self.p_s = 1.;
    }
  }

  pub fn find_global_best(&mut self) {
    for particle in self.particles.iter() {
      if particle.personal_best_objective > self.global_best_objective {
        self.global_best_objective = particle.personal_best_objective;
        self.global_best_th = particle.personal_best_th;
        self.global_best_tec = particle.personal_best_tec;
        self.global_best_position = particle.personal_best_position.clone();
      }
    }
  }

  pub fn update_task_vm_mapping(&mut self) {
    self.global_best_task_vm_mapping.clear();
    let mut i: usize = 0;
    for row in self.global_best_position.rows() {
      for (j, e) in row.indexed_iter() {
        if *e > 0.9 {
          self.global_best_task_vm_mapping.insert(i, j);
        }
      }
      i += 1;
    }
  }

  pub fn get_particle_objective_history(&self) -> [[f32; 200]; 20] {
    let mut histories: [[f32; 200]; 20] = [[0.; 200]; 20];
    let mut i: usize = 0;
    for p in self.particles.iter() {
      let mut j: usize = 0;
      for cost in p.objective_history.iter() {
        histories[i][j] = *cost;
        j += 1;
      }
      i += 1;
    }
    histories
  }
}
