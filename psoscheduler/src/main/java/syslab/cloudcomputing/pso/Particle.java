package syslab.cloudcomputing.pso;

import java.util.ArrayList;
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

  final static double maxAbsoluteVelocity = 10.0; // TODO what value to put in here

  private static int idCounter = 1;
	private int id;

  // The velocity represents a 2D array where the rows are the tasks and the columns are the positions
  // Source: https://www.sciencedirect.com/science/article/pii/S1319157820305279
  private Matrix position;
  private Matrix velocity;
  private HashMap<Task, VirtualMachine> taskVmMapping = new HashMap<Task, VirtualMachine>();

  private Matrix personalBestPosition;
  private double personalBestObjectiveValue;

  private ArrayList<Double> costHistory = new ArrayList<Double>();

  private Matrix globalBestPosition;

  private Workload workload;
  private DataCenter dataCenter;

  public Particle(DataCenter dataCenter, Workload workload) {
    this.id = Particle.idCounter++;
    this.workload = workload;
    this.dataCenter = dataCenter;
    this.randomInitialization();
  }

  // Returns true is this iteration was a success (ie. it acheived a new personal best)
  public int runIteration(double w) {

    this.updateVelocity(w);
    this.updatePosition();

    this.updateDataCenter();
    double objective = this.dataCenter.computeObjective();
    // System.out.println(this.dataCenter);
    // System.out.println(objective);

    this.costHistory.add(this.personalBestObjectiveValue);

    if (objective > this.personalBestObjectiveValue) {
      // System.out.println("BETTER POSITION");
      this.personalBestPosition = this.position.copy();
      this.personalBestObjectiveValue = objective;
      return 1;
    }
    // } else if (objective < this.personalBestObjectiveValue){
    //   System.out.println("WORSE POSITION");
    // } else {
    //   System.out.println("SAME POSITION");
    // }

    return 0;
  }

  private void randomInitialization() {
    this.position = new Matrix(this.workload.getTaskCount(), this.dataCenter.getVirtualMachineCount());
    this.position.randomPositionInitialization();

    this.velocity = new Matrix(this.workload.getTaskCount(), this.dataCenter.getVirtualMachineCount());
    this.velocity.randomVelocityInitialization(0, 1);

    updateDataCenter();

    // Must compute the local best after building the task to vm mapping in the line above for accurate
    // objective function computation
    this.personalBestPosition = this.position;
    this.personalBestObjectiveValue = this.dataCenter.computeObjective();
    // System.out.println(this.position);
  }

  private void updateDataCenter() {
    this.dataCenter.resetVirtualMachineReadyTimes();

    for (int i = 0; i < this.position.getRowsCount(); i++) {
      Task task = workload.getTaskById(i);
      VirtualMachine virtualMachine = dataCenter.getVirtualMachineById((int) this.position.getIndexOfFirstNonZeroColumnForRow(i));
      this.dataCenter.addExecutionTimeToVirtualMachine(task.getMillionsOfInstructions(), virtualMachine);
    }
  }

  private void updateVelocity(double w) {
    // Randomize constants r1 and r2
    double r1 = Math.random();
    double r2 = Math.random();

    // System.out.println("\nParticle " + this.id + " Velocity (before): " + this.velocity);
    Matrix previousVelocityFactor = this.velocity.copy().multiply(w);
    Matrix localExploration = this.personalBestPosition.copy().subtract(this.position).multiply(Particle.c1).multiply(r1);
    Matrix globalExploration = this.globalBestPosition.copy().subtract(this.position).multiply(Particle.c2).multiply(r2);
    this.velocity = previousVelocityFactor.add(localExploration).add(globalExploration)
                                          .enforceElementwiseBound(Particle.maxAbsoluteVelocity);
    // System.out.println("Particle " + this.id + " Velocity (after): " + this.velocity);
  }

  private void updatePosition() {
    // Instead of the standard PSO update equation, use the one defined in https://www.sciencedirect.com/science/article/pii/S1319157820305279#e0045
    // Because this version of PSO is discrete in nature
    // System.out.println("\nParticle " + this.id + " Position (before): " + this.position);
    this.position.zeroOut();  // TODO make this O(# tasks) instead of O(# tasks * # vms). Would this actually make a noticable difference? Because velocity is  O(# tasks * # vms)

    for (int i = 0; i < this.velocity.getRowsCount(); i++) {
      int j = this.velocity.getIndexOfMaximumColumnForRow(i);
      this.position.setComponent(i, j, 1);
    }
    // System.out.println("Particle " + this.id + " Position (after): " + this.position);
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

  private void updateTaskVmMapping() {
    this.taskVmMapping.clear();

    for (int i = 0; i < this.position.getRowsCount(); i++) {
      Task task = workload.getTaskById(i);
      VirtualMachine virtualMachine = dataCenter.getVirtualMachineById((int) this.position.getIndexOfFirstNonZeroColumnForRow(i));
      this.taskVmMapping.put(task, virtualMachine);
    }
  }

  public HashMap<Task,VirtualMachine> getTaskVmMapping() {
    this.updateTaskVmMapping();
    return this.taskVmMapping;
  }

  public void setGlobalBestPosition(Matrix globalBestPosition) {
    this.globalBestPosition = globalBestPosition;
  }

  public Matrix getPersonalBestPosition() {
    return this.personalBestPosition;
  }

  public double getPersonalBestObjectiveValue() {
    return this.personalBestObjectiveValue;
  }

  public ArrayList<Double> getCostHistory() {
    return this.costHistory;
  }

  @Override
  public String toString() {
    return "(Particle" + this.id + ")";
  }
}
