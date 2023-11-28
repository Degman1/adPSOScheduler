use criterion::{black_box, criterion_group, criterion_main, Criterion};
use psoscheduler;
use ndarray::Array;
use ndarray::Array2;
use ndarray_rand::RandomExt;
use ndarray_rand::rand_distr::Uniform;

pub fn criterion_benchmark2(c: &mut Criterion) {
  let arr1: Array2<f32> = black_box(Array::random((1000, 100), Uniform::new(0., 10.)));
  let arr2: Array2<f32> = black_box(Array::random((1000, 100), Uniform::new(0., 10.)));
  c.bench_function("Rust ndarray Ops Benchmark", |b| b.iter(|| psoscheduler::ndarray_ops(&arr1, &arr2)));
}

criterion_group!(benches, criterion_benchmark2);
criterion_main!(benches);