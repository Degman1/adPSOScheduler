package syslab.cloudcomputing.pso;

import java.util.HashMap;

import syslab.cloudcomputing.simulation.DataCenter;
import syslab.cloudcomputing.simulation.Task;
import syslab.cloudcomputing.simulation.VirtualMachine;

public class Schedule {
  public static void main(String[] args) {

    // Create VMS and put inside the data center
    DataCenter dataCenter = new DataCenter();
    PSOSwarm swarm = new PSOSwarm(null, null, 0, 0, 0, 0);
    HashMap<Task, VirtualMachine> taskVmMapping = swarm.runPSOAlgorithm();

  }
}
