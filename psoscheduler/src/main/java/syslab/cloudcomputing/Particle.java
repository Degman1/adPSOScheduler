package syslab.cloudcomputing;

import java.util.HashMap;

/*
 * Particle represents a complete solution to the optimization problem, meaning a complete 
 * mapping of tasks to virtual machines. Each particle's goal is to move towards the
 * optimized mapping of tasks to virtual machine using the objective function as a measure
 * for cost.
 */
public class Particle {
  private static int idCounter = 1;
	private int id;

  private double[] positionVector;
  private double[] velocityVector;
  private HashMap<Task, VirtualMachine> taskVmMapping = new HashMap<Task, VirtualMachine>();

  private double[] personalBestPosition;
  private Double personalBestObjectiveValue;

  private int minPosition;
  private int maxPosition;
  private int maxAbsoluteVelocity;

  private Workload workload;
  private DataCenter dataCenter;

  public Particle(DataCenter dataCenter, Workload workload, int minPosition, int maxPosition, int maxAbsoluteVelocity) {
    this.id = Particle.idCounter++;
    this.minPosition = minPosition;
    this.maxPosition = maxPosition;
    this.maxAbsoluteVelocity = maxAbsoluteVelocity;
    this.workload = workload;
    this.dataCenter = dataCenter;
    randomInitialization();
  }

  private void randomInitialization() {
    this.positionVector = new double[this.workload.getTaskCount()];
    this.velocityVector = new double[this.workload.getTaskCount()];

    // Randomly generate the particle's position vector in d-dimensional space where d=# Tasks
    for (int i = 0; i < this.workload.getTaskCount(); i++) {
      this.positionVector[i] = Utilities.getRandomDouble(this.minPosition, this.maxPosition);
    }

    // Randomly generate velocities for each component of the position vector
    for (int i = 0; i < this.workload.getTaskCount(); i++){
      this.velocityVector[i] = Utilities.getRandomDouble(-this.maxAbsoluteVelocity, this.maxAbsoluteVelocity);
    }

    // Build the Task to VirtualMachine mapping based on the randomly generated position vector
    buildTaskVmMapping();

    // Must compute the local best after building the task to vm mapping in the line above for accurate
    // objective function computation
    this.personalBestPosition = this.positionVector;
    this.personalBestObjectiveValue = dataCenter.computeObjective();
  }

  public void buildTaskVmMapping() {
    this.taskVmMapping.clear();
    this.dataCenter.resetVirtualMachineReadyTime();

    for (int i = 0; i < this.positionVector.length; i++) {
      Task task = workload.getTaskById(i);
      VirtualMachine virtualMachine = dataCenter.getVirtualMachineById((int) this.positionVector[i]);
      this.taskVmMapping.put(task, virtualMachine);
      this.dataCenter.addExecutionTimeToVirtualMachine(task.getMillionsOfInstructions(), virtualMachine);
    }
  }

  private void updateVelocity() {
    // TODO
  }

  private void updatePosition() {
    // TODO
  }

  public int getId() {
    return this.id;
  }

  public double[] getPositionVector() {
    return this.positionVector;
  }

  public double[] getVelocityVector() {
    return this.velocityVector;
  }

  public HashMap<Task,VirtualMachine> getTaskVmMapping() {
    return this.taskVmMapping;
  }
  
}
