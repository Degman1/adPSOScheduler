use ndarray::Array;
use ndarray::Array2;
use ndarray_rand::RandomExt;
use ndarray_rand::rand_distr::Uniform;
use std::sync::atomic::{AtomicUsize, Ordering};
use std::collections::HashMap;

use crate::utils::utilities;
use crate::simulation::data_center::DataCenter;
use crate::simulation::workload::Workload;

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

}