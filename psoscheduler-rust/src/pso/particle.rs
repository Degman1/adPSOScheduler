use ndarray::Array;
use ndarray::Array2;
use ndarray::Axis;
use ndarray_rand::RandomExt;
use ndarray_rand::rand_distr::Uniform;
use std::sync::atomic::{AtomicUsize, Ordering};
use std::collections::HashMap;
use ordered_float::OrderedFloat;

use crate::utils::utilities;
use crate::simulation::data_center::DataCenter;
use crate::simulation::workload::Workload;
use crate::simulation::task::Task;

static PARTICLE_ID_COUNTER: AtomicUsize = AtomicUsize::new(0);

struct Particle {
  pub id: usize,
  pub position: Array2<f32>,
  pub velocity: Array2<f32>,
  pub task_vm_mapping: HashMap<usize, usize>,
  pub personal_best_position: Array2<f32>,
  pub personal_best_objective: f32,
}

impl Particle {
  const c1: f32 = 2.;
  const c2: f32 = 1.49455;

  const max_absolute_velocity: f32 = 10.;

  pub fn new(workload: &mut Workload, data_center: &mut DataCenter) -> Particle {
    let mut position: Array2<f32> = Array::zeros((workload.tasks.len(), data_center.virtual_machines.len()));
    let mut task_vm_mapping: HashMap<usize, usize> = HashMap::new();

    data_center.reset_virtual_machine_ready_time();

    // TODO sort tasks once, not all 20 times
    for task in workload.get_sorted_tasks().iter() {
      let vm_id: usize;
      if utilities::get_random_float(0., 1.) < 0.25 {
        vm_id = utilities::get_random_integer(0, data_center.virtual_machines.len());
      } else {
        vm_id = data_center.get_min_eet_virtual_machine(task);
      }
      position[[task.id, vm_id]] = 1.;
      data_center.add_execution_time_to_virtual_machine(task, vm_id);
    }

    let velocity:Array2<f32>  = Array::random((workload.tasks.len(), data_center.virtual_machines.len()), Uniform::new(0., 1.));
    let personal_best_position: Array2<f32> = position.clone();
    let personal_best_objective: f32 = data_center.compute_objective();

    Particle {
      id: PARTICLE_ID_COUNTER.fetch_add(1, Ordering::Relaxed),
      position: position,
      velocity: velocity,
      task_vm_mapping: task_vm_mapping,
      personal_best_position: personal_best_position,
      personal_best_objective: personal_best_objective,
    }
  }

  fn update_data_center(&self, workload: Workload, data_center: &mut DataCenter) {
    data_center.reset_virtual_machine_ready_time();

    let mut i: usize = 0;
    for row in self.position.outer_iter() {
      let task: &Task = &workload.tasks[i];
      let mut vm_id: usize = 0;
      for (j, e) in row.indexed_iter() {
        if *e > 0. {
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
    let local_exploration: Array2<f32> = (&self.personal_best_position - &self.position)
                                          .mapv(|a| a * Particle::c1 * r1);
    let global_exploration: Array2<f32> = (global_best_position - &self.position)
                                          .mapv(|a| a * Particle::c2 * r2);
    self.velocity += &(local_exploration + &global_exploration);
    self.velocity.mapv_inplace(|a| -> f32 {
      if a > Particle::max_absolute_velocity {
        return utilities::get_random_float(0., Particle::max_absolute_velocity);
      }
      return a;
    });
  }

  fn update_position(&mut self) {
    self.position.mapv_inplace(|_a| 0.);
    // let maxes  = self.velocity.map_axis(Axis(1), 
    //   |view| *view.iter()
    //                                                      .enumerate()
    //                                                      .max_by_key(|(_, v)| OrderedFloat(*v)))
    //                                                      .map(|(idx, value)| idx);

    // }
    let mut i: usize = 0;
    for row in self.velocity.rows() {
      let mut max_col: usize = 0;
      let mut max_val: f32 = 0.;
      for (j, e) in row.indexed_iter() {
        if e.partial_cmp(&max_val).unwrap() == std::cmp::Ordering::Greater {
          max_col = j;
          max_val = *e;
        }
      }
      self.position[[i, max_col]] = 1.;
      i += 1
    }
  }

  fn position_max_copy<T: Ord + Copy>(slice: &[T]) -> Option<usize> {
    slice.iter().enumerate().max_by_key(|(_, &value)| value).map(|(idx, _)| idx)
}