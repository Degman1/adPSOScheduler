package syslab.cloudcomputing.benchmarks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import syslab.cloudcomputing.pso.PSOSwarm;
import syslab.cloudcomputing.schedule.Scheduler;
import syslab.cloudcomputing.simulation.DataCenter;
import syslab.cloudcomputing.simulation.Task;
import syslab.cloudcomputing.simulation.VirtualMachine;
import syslab.cloudcomputing.simulation.Workload;

import java.util.concurrent.TimeUnit;

public class PSOBenchmark {
  public static void main(String[] args) throws Exception {
    Options opt = new OptionsBuilder()
      .include(PSOBenchmark.class.getSimpleName())
      .forks(5)
      .warmupIterations(2)
      .measurementIterations(10)
      .build();

    new Runner(opt).run();
  }

  @State(Scope.Thread)
  public static class MyState {
    public DataCenter dataCenter;
    public Workload workload;

    @Setup(Level.Trial)
    public void setupTest11Data() {
      Task.resetIdCounter();
      VirtualMachine.resetIdCounter();
      this.dataCenter = Scheduler.test11_DataCenter();
      this.workload = Scheduler.test11_Workload();
    }
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.SECONDS)
  public void runSinglePSO(MyState state, Blackhole blackhole) {
    PSOSwarm swarm = new PSOSwarm(state.dataCenter, state.workload);
    blackhole.consume(swarm.runPSOAlgorithm());
  }
}
