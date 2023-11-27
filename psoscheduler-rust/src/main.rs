use std::env;
use psoscheduler;

fn main() {
  env::set_var("RUST_BACKTRACE", "1");

  psoscheduler::pso_basic_test();
}