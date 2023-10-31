package syslab.cloudcomputing.pso;

import java.util.HashMap;

import syslab.cloudcomputing.simulation.Task;
import syslab.cloudcomputing.simulation.VirtualMachine;

public class Result {
  private PSOSwarm[] swarm;
  private double objectiveHistory;
  private HashMap<Task, VirtualMachine> mapping;

  public Result(PSOSwarm[] swarm, double objectiveHistory, HashMap<Task,VirtualMachine> mapping) {
    this.swarm = swarm;
    this.objectiveHistory = objectiveHistory;
    this.mapping = mapping;
  }

  public PSOSwarm[] getSwarm() {
    return this.swarm;
  }

  public double getFinalObjective() {
    return this.objectiveHistory;
  }

  public HashMap<Task,VirtualMachine> getFinalMapping() {
    return this.mapping;
  }
}
