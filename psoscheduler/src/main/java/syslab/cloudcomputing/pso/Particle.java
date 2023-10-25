package syslab.cloudcomputing.pso;

import java.util.HashMap;

import syslab.cloudcomputing.simulation.DataCenter;
import syslab.cloudcomputing.simulation.Task;
import syslab.cloudcomputing.simulation.VirtualMachine;
import syslab.cloudcomputing.simulation.Workload;
import syslab.cloudcomputing.utils.Matrix;

/*
 * Particle represents a complete solution to the optimization problem, meaning a complete 
 * mapping of tasks to virtual machines. Each particle's goal is to move towards the
 * optimized mapping of tasks to virtual machine using the objective function as a measure
 * for cost.
 */
public class Particle {
  final static double c1 = 2.0;
  final static double c2 = 1.49455;

  private static double w;

  private static int idCounter = 1;
	private int id;

  // The velocity represents a 2D array where the rows are the tasks and the columns are the positions
  // Source: https://www.sciencedirect.com/science/article/pii/S1319157820305279
  private Matrix position;
  private Matrix velocity;
  private HashMap<Task, VirtualMachine> taskVmMapping = new HashMap<Task, VirtualMachine>();

  private Matrix personalBestPosition;
  private double personalBestObjectiveValue;

  private Matrix globalBestPosition;
  private double globalBestObjectiveValue;

  private Workload workload;
  private DataCenter dataCenter;

  public Particle(DataCenter dataCenter, Workload workload) {
    this.id = Particle.idCounter++;
    this.workload = workload;
    this.dataCenter = dataCenter;
    this.randomInitialization();
  }

  // Returns true is this iteration was a success (ie. it acheived a new personal best)
  public Boolean runIteration() {
    
    // Randomize constants r1 and r2
    double r1 = Math.random();
    double r2 = Math.random();

    this.updateVelocity(r1, r2);
    this.updatePosition();

    this.updateDataCenter();

    if (this.dataCenter.computeObjective() > this.personalBestObjectiveValue) {
      this.personalBestPosition = this.position;
      this.personalBestObjectiveValue = dataCenter.computeObjective();
      return true;
    }

    return false;
  }

  private void randomInitialization() {
    this.position = new Matrix(this.workload.getTaskCount(), this.dataCenter.getVirtualMachineCount());
    this.position.randomPositionInitialization();

    this.velocity = new Matrix(this.workload.getTaskCount(), this.dataCenter.getVirtualMachineCount());

    // Build the Task to VirtualMachine mapping based on a randomly generated position
    updateDataCenter();

    // Must compute the local best after building the task to vm mapping in the line above for accurate
    // objective function computation
    this.personalBestPosition = this.position;
    this.personalBestObjectiveValue = this.dataCenter.computeObjective();
  }

  private void updateDataCenter() {
    this.dataCenter.resetVirtualMachineReadyTimes();

    for (int i = 0; i < this.position.getRows(); i++) {
      Task task = workload.getTaskById(i);
      VirtualMachine virtualMachine = dataCenter.getVirtualMachineById((int) this.position.getIndexOfFirstNonZeroColumnForRow(i));
      this.dataCenter.addExecutionTimeToVirtualMachine(task.getMillionsOfInstructions(), virtualMachine);
    }
  }

  private void updateVelocity(double r1, double r2) {
    Matrix previousVelocityFactor = this.velocity.multiply(Particle.w);
    Matrix localExploration = this.personalBestPosition.subtract(this.position).multiply(Particle.c1).multiply(r1);
    Matrix globalExploration = this.globalBestPosition.subtract(this.position).multiply(Particle.c2).multiply(r2);
    this.velocity = previousVelocityFactor.add(localExploration).add(globalExploration);
  }

  private void updatePosition() {
    // Instead of the standard PSO update equation, use the one defined in https://www.sciencedirect.com/science/article/pii/S1319157820305279#e0045
    // Because this version of PSO is discrete in nature
    this.position.zeroOut();

    for (int i = 0; i < this.velocity.getRows(); i++) {
      int j = this.velocity.getIndexOfMaximumColumnForRow(i);
      this.position.setComponent(i, j, 1);
    }
  }

  public int getId() {
    return this.id;
  }

  public Matrix getPosition() {
    return this.position;
  }

  public Matrix getVelocityVector() {
    return this.velocity;
  }

  public HashMap<Task,VirtualMachine> getTaskVmMapping() {
    this.taskVmMapping.clear();

    for (int i = 0; i < this.position.getRows(); i++) {
      Task task = workload.getTaskById(i);
      VirtualMachine virtualMachine = dataCenter.getVirtualMachineById((int) this.position.getIndexOfFirstNonZeroColumnForRow(i));
      this.taskVmMapping.put(task, virtualMachine);
    }

    return this.taskVmMapping;
  }

  public void setGlobalBestPosition(Matrix globalBestPosition) {
    this.globalBestPosition = globalBestPosition;
  }

  public void setGlobalBestObjectiveValue(double globalBestObjectiveValue) {
    this.globalBestObjectiveValue = globalBestObjectiveValue;
  }
 
  public static void setW(double w) {
    Particle.w = w;
  }

  public static double getW() {
    return Particle.w;
  }
}
