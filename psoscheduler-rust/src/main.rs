use std::env;
use psoscheduler;

fn main() {
  env::set_var("RUST_BACKTRACE", "1");
  log_basic_pso();
}

fn log_basic_pso() {
  let workload: psoscheduler::simulation::workload::Workload = psoscheduler::build_test1_workload();
  let data_center: psoscheduler::simulation::data_center::DataCenter = psoscheduler::build_test1_data_center();
  let mut swarm = psoscheduler::pso::pso_swarm::PSOSwarm::new(workload, data_center);
  swarm.run_pso_algorithm();
  println!("Global Best Objective: {:?}", swarm.global_best_objective);
  println!("Final Mapping: {:?}", swarm.global_best_task_vm_mapping);
  // Save the cost history to local file
  psoscheduler::utils::utilities::write_objective_history_to_csv(&swarm.get_particle_objective_history(), "./output_data/objective_history.csv");

}