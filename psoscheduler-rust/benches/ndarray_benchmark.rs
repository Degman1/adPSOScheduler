use criterion::{black_box, criterion_group, criterion_main, Criterion};
use ndarray::Array;
use ndarray::Array2;
use ndarray_rand::RandomExt;
use ndarray_rand::rand_distr::Uniform;

fn subtract(m1: &Array2<f32>, m2: &Array2<f32>) -> Array2<f32> {
  return m1 - m2;
}

pub fn criterion_benchmark2(c: &mut Criterion) {
  let mut group = c.benchmark_group("Rust ndarray");
  let arr1: Array2<f32> = black_box(Array::random((1000, 100), Uniform::new(0., 10.)));
  let arr2: Array2<f32> = black_box(Array::random((1000, 100), Uniform::new(0., 10.)));
  group.bench_function("Subtract Benchmark", |b| b.iter(|| subtract(&arr1, &arr2)));
  group.finish();
}

criterion_group!(benches, criterion_benchmark2);
criterion_main!(benches);

// NOTE To run regualar benchmarking use "cargo bench --bench ndarray_benchmark --verbose"
