package syslab.cloudcomputing.pso;

import java.util.HashMap;

import syslab.cloudcomputing.simulation.Task;
import syslab.cloudcomputing.simulation.VirtualMachine;

public class Result {
  private double objective;
  private HashMap<Task, VirtualMachine> mapping;

  public Result(double objective, HashMap<Task,VirtualMachine> mapping) {
    this.objective = objective;
    this.mapping = mapping;
  }

  public double getObjective() {
    return this.objective;
  }

  public void setObjective(double objective) {
    this.objective = objective;
  }

  public HashMap<Task,VirtualMachine> getMapping() {
    return this.mapping;
  }

  public void setMapping(HashMap<Task,VirtualMachine> mapping) {
    this.mapping = mapping;
  }

}
