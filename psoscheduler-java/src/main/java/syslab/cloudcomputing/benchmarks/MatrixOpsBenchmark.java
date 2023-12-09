package syslab.cloudcomputing.benchmarks;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import syslab.cloudcomputing.utils.Matrix;

public class MatrixOpsBenchmark {
  public static void main(String[] args) throws Exception {
    Options opt = new OptionsBuilder()
      .include(MatrixOpsBenchmark.class.getSimpleName())
      .build();

    new Runner(opt).run();
  }

  @State(Scope.Thread)
  public static class MyState {
    public Matrix m1;
    public Matrix m2;

    @Setup(Level.Trial)
    public void setupRandomMatrices() {
      m1 = new Matrix(800, 100);
      m1.randomVelocityInitialization(0.0, 10.0);
      m2 = new Matrix(800, 100);
      m2.randomVelocityInitialization(0.0, 10.0);
    }
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  public void runSingleSubtract(MyState state, Blackhole blackhole) {
    blackhole.consume(state.m1.subtract(state.m2));
  }
}
