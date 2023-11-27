use std::env;

mod schedule {
  pub(crate) mod scheduler;
}

mod simulation {
  pub(crate) mod task;
  pub(crate) mod workload;
  pub(crate) mod virtual_machine;
  pub(crate) mod data_center;
}

mod pso {
  pub(crate) mod particle;
  pub(crate) mod pso_swarm;
}

mod utils {
  pub(crate) mod utilities;
}

fn main() {
  env::set_var("RUST_BACKTRACE", "1");

  pso_test();
}

fn pso_test() {
  let t = simulation::task::Task::new(500);
  let t2 = simulation::task::Task::new(600);
  let mut wk = simulation::workload::Workload::new();
  wk.add_task(t);
  wk.add_task(t2);

  let vm = simulation::virtual_machine::VirtualMachine::new(200, 300.0);
  let vm2 = simulation::virtual_machine::VirtualMachine::new(400, 500.0);
  let mut dc = simulation::data_center::DataCenter::new();
  dc.add_virtual_machine(vm);
  dc.add_virtual_machine(vm2);

  let mut swarm = pso::pso_swarm::PSOSwarm::new(wk, dc);
}

fn basic_tests() {
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
