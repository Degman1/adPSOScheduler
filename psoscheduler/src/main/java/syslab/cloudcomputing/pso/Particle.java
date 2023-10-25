package syslab.cloudcomputing;

import java.util.HashMap;

import syslab.cloudcomputing.simulation.DataCenter;
import syslab.cloudcomputing.simulation.Task;
import syslab.cloudcomputing.simulation.VirtualMachine;
import syslab.cloudcomputing.simulation.Workload;
import syslab.cloudcomputing.utils.Matrix;

// TODO use SJF as a heuristic for initialization instead of random initialization?

/*
 * Particle represents a complete solution to the optimization problem, meaning a complete 
 * mapping of tasks to virtual machines. Each particle's goal is to move towards the
 * optimized mapping of tasks to virtual machine using the objective function as a measure
 * for cost.
 */
public class Particle {
  private static int idCounter = 1;
	private int id;

  // The velocity represents a 2D array where the rows are the tasks and the columns are the positions
  // Source: https://www.sciencedirect.com/science/article/pii/S1319157820305279
  private Matrix position;
  private Matrix velocity;
  private HashMap<Task, VirtualMachine> taskVmMapping = new HashMap<Task, VirtualMachine>();

  private Matrix personalBestPosition;
  private Double personalBestObjectiveValue;

  private Workload workload;
  private DataCenter dataCenter;

  public Particle(DataCenter dataCenter, Workload workload) {
    this.id = Particle.idCounter++;
    this.workload = workload;
    this.dataCenter = dataCenter;
    randomInitialization();
  }

  private void randomInitialization() {
    this.position = new Matrix(this.workload.getTaskCount(), this.dataCenter.getVirtualMachineCount());
    this.position.randomPositionInitialization();

    this.velocity = new Matrix(this.workload.getTaskCount(), this.dataCenter.getVirtualMachineCount());

    // Build the Task to VirtualMachine mapping based on a randomly generated position
    buildTaskVmMapping();

    // Must compute the local best after building the task to vm mapping in the line above for accurate
    // objective function computation
    this.personalBestPosition = this.position;
    this.personalBestObjectiveValue = dataCenter.computeObjective();
  }

  public void buildTaskVmMapping() {
    this.taskVmMapping.clear();
    this.dataCenter.resetVirtualMachineReadyTimes();

    for (int i = 0; i < this.position.length; i++) {
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
