use std::env;
use psoscheduler;

fn main() {
  // env::set_var("RUST_BACKTRACE", "1");

  let args: Vec<String> = env::args().collect();

  let testID = args[1].clone();

  let workload = match testID.as_str() {
    "1" => psoscheduler::build_test1_workload(),
    "2" => psoscheduler::build_test2_workload(),
    _ => panic!("Test {} is not implemented", testID),
  };

  let data_center = match testID.as_str() {
    "1" => psoscheduler::build_test1_data_center(),
    "2" => psoscheduler::build_test2_data_center(),
    _ => panic!("Test {} is not implemented", testID),
  };

  let mut swarm = psoscheduler::pso::pso_swarm::PSOSwarm::new(workload, data_center);
  swarm.run_pso_algorithm();
  println!("Global Best Objective: {:?}", swarm.global_best_objective);
  println!("Final Mapping: {:?}", swarm.global_best_task_vm_mapping);
  // Save the cost history to local file
  psoscheduler::utils::utilities::write_objective_history_to_csv(&swarm.get_particle_objective_history(), "./output_data/objective_history.csv");
}