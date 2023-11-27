use std::sync::atomic::{AtomicUsize, Ordering};

pub mod simulation {
  pub mod task;
  pub mod workload;
  pub mod virtual_machine;
  pub mod data_center;
}

pub mod pso {
  pub mod particle;
  pub mod pso_swarm;
}

pub mod utils {
  pub mod utilities;
}

pub fn run_pso_algorithm(workload: simulation::workload::Workload, data_center: simulation::data_center::DataCenter) {
  let mut swarm = pso::pso_swarm::PSOSwarm::new(workload, data_center);
  swarm.run_pso_algorithm();
}

pub fn build_test11_data_center() -> simulation::data_center::DataCenter {
  let n_vms: usize = 100;
  let vm_mips_low: usize = 1000;
  let vm_mips_high: usize = 5000;
  let active_state_joules_per_mi_low: f32 = 200.;
  let active_state_joules_per_mi_high: f32 = 1000.;

  let mut data_center = simulation::data_center::DataCenter::new();

  for i in 0..n_vms {
    let mi = utils::utilities::get_random_integer(vm_mips_low, vm_mips_high) as u32;
    let asj = utils::utilities::get_random_float(active_state_joules_per_mi_low, active_state_joules_per_mi_high);
    let vm = simulation::virtual_machine::VirtualMachine::new(mi, asj);
    data_center.add_virtual_machine(vm)
  }

  simulation::virtual_machine::VIRTUAL_MACHINE_ID_COUNTER.store(0, Ordering::Relaxed);

  data_center
}

pub fn build_test11_workload() -> simulation::workload::Workload {
  let n_tasks: usize = 800;
  let task_mi_low: usize = 1000;
  let task_mi_high: usize = 4000;

  let mut workload = simulation::workload::Workload::new();

  for i in 0..n_tasks {
    let mi = utils::utilities::get_random_integer(task_mi_low, task_mi_high) as u32;
    let task = simulation::task::Task::new(mi);
    workload.add_task(task);
  }

  simulation::task::TASK_ID_COUNTER.store(0, Ordering::Relaxed);

  workload
}

pub fn build_basic_workload() -> simulation::workload::Workload {
  let t = simulation::task::Task::new(500);
  let t2 = simulation::task::Task::new(600);
  let mut wk = simulation::workload::Workload::new();
  wk.add_task(t);
  wk.add_task(t2);

  simulation::task::TASK_ID_COUNTER.store(0, Ordering::Relaxed);

  wk
}

pub fn build_basic_data_center() -> simulation::data_center::DataCenter {
  let vm = simulation::virtual_machine::VirtualMachine::new(200, 300.0);
  let vm2 = simulation::virtual_machine::VirtualMachine::new(400, 500.0);
  let mut dc = simulation::data_center::DataCenter::new();
  dc.add_virtual_machine(vm);
  dc.add_virtual_machine(vm2);

  simulation::virtual_machine::VIRTUAL_MACHINE_ID_COUNTER.store(0, Ordering::Relaxed);

  dc
}

pub fn basic_tests() {
  println!("Hello, world!");
  let t = simulation::task::Task::new(500);
  let t2 = simulation::task::Task::new(600);
  println!("{} {}", t, t2);
  let mut wk = simulation::workload::Workload::new();
  wk.add_task(t);
  wk.add_task(t2);
  println!("{}", wk);
  let vm = simulation::virtual_machine::VirtualMachine::new(200, 300.0);
  let vm2 = simulation::virtual_machine::VirtualMachine::new(400, 500.0);
  println!("{} {}", vm, vm2);
  let mut dc = simulation::data_center::DataCenter::new();
  dc.add_virtual_machine(vm);
  dc.add_virtual_machine(vm2);
  println!("{}", dc);
  let assignee = dc.get_min_eet_virtual_machine(&wk.tasks[0]);
  println!("{}", assignee);
  dc.add_execution_time_to_virtual_machine(&wk.tasks[0], 0);
  dc.add_execution_time_to_virtual_machine(&wk.tasks[1], 1);
  let obj = dc.compute_objective();
  println!("{}", obj);
  let t3 = simulation::task::Task::new(100);
  let t4 = simulation::task::Task::new(50);
  wk.add_task(t3);
  wk.add_task(t4);
  for t in wk.get_sorted_tasks().iter() {
    println!("{}", t);
  }
}
