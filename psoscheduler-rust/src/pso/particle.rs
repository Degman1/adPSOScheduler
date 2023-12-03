use ndarray::Array;
use ndarray::Array2;
use ndarray_rand::RandomExt;
use ndarray_rand::rand_distr::Uniform;
use std::sync::atomic::{AtomicUsize, Ordering};
use std::collections::HashMap;

use crate::simulation::task::Task;
use crate::utils::utilities;
use crate::simulation::data_center::DataCenter;
use crate::simulation::workload::Workload;

static PARTICLE_ID_COUNTER: AtomicUsize = AtomicUsize::new(0);

pub struct Particle {
  pub id: usize,
  pub position: Array2<f32>,
  pub velocity: Array2<f32>,
  pub task_vm_mapping: HashMap<usize, usize>,
  pub personal_best_position: Array2<f32>,
  pub personal_best_objective: f32,
  pub personal_best_throughput: f32,
  pub objective_history: Vec<f32>,
}

impl Particle {
  const C1: f32 = 2.;
  const C2: f32 = 1.49455;

  const MAX_ABS_VELOCITY: f32 = 10.;

  pub fn new(workload: &mut Workload, data_center: &mut DataCenter) -> Particle {
    let mut position: Array2<f32> = Array::zeros((workload.tasks.len(), data_center.virtual_machines.len()));
    let task_vm_mapping: HashMap<usize, usize> = HashMap::new();

    data_center.reset_virtual_machine_ready_time();

    for task in workload.get_sorted_tasks().iter() {
      let vm_id: usize;
      if utilities::get_random_float(0., 1.) < 0.25 {
        vm_id = utilities::get_random_integer(0, data_center.virtual_machines.len());
      } else {
        vm_id = data_center.get_min_eet_virtual_machine(task);
      }
      position[[task.id, vm_id]] = 1.;
      
      // task_vm_mapping.insert(task.id, vm_id);
      data_center.add_execution_time_to_virtual_machine(task, vm_id);
    }

    let velocity:Array2<f32>  = Array::random((workload.tasks.len(), data_center.virtual_machines.len()), Uniform::new(0., 1.));
    let personal_best_position: Array2<f32> = position.clone();
    let personal_best_objective: f32 = data_center.compute_objective();
    let personal_best_throughput: f32 = data_center.compute_throughput();
    let objective_history: Vec<f32> = Vec::new();

    Particle {
      id: PARTICLE_ID_COUNTER.fetch_add(1, Ordering::Relaxed),
      position: position,
      velocity: velocity,
      task_vm_mapping: task_vm_mapping,
      personal_best_position: personal_best_position,
      personal_best_objective: personal_best_objective,
      personal_best_throughput: personal_best_throughput,
      objective_history: objective_history,
    }
  }

  pub fn run_iteration(&mut self, workload: &Workload, data_center: &mut DataCenter, w: f32, global_best_position: &Array2<f32>) -> u8 {
    self.update_velocity(w, global_best_position);
    self.update_position_and_data_center(workload, data_center);

    // self.update_data_center(workload, data_center);
    let objective: f32 = data_center.compute_objective();

    if objective > self.personal_best_objective {
      self.personal_best_objective = objective;
      self.personal_best_throughput = data_center.compute_throughput();
      self.personal_best_position = self.position.clone();
      self.objective_history.push(objective);
      return 1;
    }

    self.objective_history.push(self.personal_best_objective);

    return 0;
  }
  
  fn update_data_center(&self, workload: &Workload, data_center: &mut DataCenter) {
    data_center.reset_virtual_machine_ready_time();

    let mut i: usize = 0;
    for row in self.position.rows() {
      let task: &Task = &workload.tasks[i];
      let mut vm_id: usize = 0;
      for (j, e) in row.indexed_iter() {
        if *e > 0.9 {
          vm_id = j;
          break;
        }
      }
      data_center.add_execution_time_to_virtual_machine(task, vm_id);
      i += 1;
    }
  }

  fn update_velocity(&mut self, w: f32, global_best_position: &Array2<f32>) {
    let r1: f32 = utilities::get_random_float(0., 1.);
    let r2: f32 = utilities::get_random_float(0., 1.);

    self.velocity.mapv_inplace(|a| a * w);
    let mut local_exploration: Array2<f32> = &self.personal_best_position - &self.position;
    local_exploration.mapv_inplace(|a| a * Particle::C1 * r1);
    let mut global_exploration: Array2<f32> = global_best_position - &self.position;
    global_exploration.mapv_inplace(|a| a * Particle::C2 * r2);
    self.velocity += &(local_exploration + &global_exploration);
    self.velocity.mapv_inplace(|a| -> f32 {
      if a.abs() > Particle::MAX_ABS_VELOCITY || a < 0. {
        return utilities::get_random_float(0., Particle::MAX_ABS_VELOCITY);
      }
      return a;
    });
  }

  fn update_position_and_data_center(&mut self, workload: &Workload, data_center: &mut DataCenter) {
    data_center.reset_virtual_machine_ready_time();

    let mut i: usize = 0;
    for row in self.velocity.rows() {
      let mut max_col: usize = 0;
      let mut max_val: f32 = -Particle::MAX_ABS_VELOCITY - 1.;
      for (j, e) in row.indexed_iter() {
        self.position[[i, j]] = 0.;
        if *e > max_val {
          max_col = j;
          max_val = *e;
        }
      }
      self.position[[i, max_col]] = 1.;
      data_center.add_execution_time_to_virtual_machine(&workload.tasks.get(i).unwrap(), max_col);
      i += 1
    }
  }

  pub fn update_task_vm_mapping(&mut self) {
    self.task_vm_mapping.clear();
    let mut i: usize = 0;
    for row in self.position.rows() {
      for (j, e) in row.indexed_iter() {
        if *e > 0.9 {
          self.task_vm_mapping.insert(i, j);
        }
      }
      i += 1;
    }
  }
}
