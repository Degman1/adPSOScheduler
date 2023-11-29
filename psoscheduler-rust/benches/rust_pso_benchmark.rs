use std::time::Duration;

use criterion::{black_box, criterion_group, criterion_main, Criterion};

mod perf;
use psoscheduler;

pub fn criterion_benchmark(c: &mut Criterion) {
  let mut group = c.benchmark_group("Rust PSO");
  group.warm_up_time(Duration::from_secs(15));
  group.measurement_time(Duration::from_secs(350));
  group.bench_function("Rust PSO Test 11 Benchmark", |b| b.iter(|| psoscheduler::run_pso_algorithm(black_box(psoscheduler::build_test11_workload()), black_box(psoscheduler::build_test11_data_center()))));
  group.finish();
}

criterion_group!{
  name=benches;
  config=Criterion::default().with_profiler(perf::FlamegraphProfiler::new(100));
  targets=criterion_benchmark
}
criterion_main!(benches);

// NOTE To run the flamegraph profiler use "cargo bench --bench rust_pso_benchmark -- --profile-time=5"