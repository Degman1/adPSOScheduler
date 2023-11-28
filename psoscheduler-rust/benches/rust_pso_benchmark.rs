use criterion::{black_box, criterion_group, criterion_main, Criterion};
use psoscheduler;
// use std::env;

pub fn criterion_benchmark(c: &mut Criterion) {
  // env::set_var("RUST_BACKTRACE", "full");
  c.bench_function("Rust PSO Test 11", |b| b.iter(|| psoscheduler::run_pso_algorithm(black_box(psoscheduler::build_basic_workload()), black_box(psoscheduler::build_basic_data_center()))));
}

criterion_group!(benches, criterion_benchmark);
criterion_main!(benches);