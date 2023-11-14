package syslab.cloudcomputing.pso;

import java.util.ArrayList;
import java.util.HashMap;

import syslab.cloudcomputing.simulation.DataCenter;
import syslab.cloudcomputing.simulation.Task;
import syslab.cloudcomputing.simulation.VirtualMachine;
import syslab.cloudcomputing.simulation.Workload;
import syslab.cloudcomputing.utils.Matrix;
import syslab.cloudcomputing.utils.Utilities;

/*
 * Particle represents a complete solution to the optimization problem, meaning a complete 
 * mapping of tasks to virtual machines. Each particle's goal is to move towards the
 * optimized mapping of tasks to virtual machine by maximizing the objective function
 */
public class Particle {
  final static double c1 = 2.0;
  final static double c2 = 1.49455;

  final static double maxAbsoluteVelocity = 10.0;  // from https://www.sciencedirect.com/science/article/pii/S1319157820305279#e0045

  private static int idCounter = 1;
	private int id;

  // The velocity represents a 2D array where the rows are the tasks and the columns are the positions
  // Source: https://www.sciencedirect.com/science/article/pii/S1319157820305279
  private Matrix position;
  private Matrix velocity;
  private HashMap<Task, VirtualMachine> taskVmMapping = new HashMap<Task, VirtualMachine>();

  private Matrix personalBestPosition;
  private double personalBestObjectiveValue;

  private ArrayList<Double> objectiveHistory = new ArrayList<Double>();

  private Matrix globalBestPosition;

  private Workload workload;
  private DataCenter dataCenter;

  public static enum InitializationStrategy {
    RANDOM,
    MCT,
    HIGH_TASK_HIGH_VM
  }

  public Particle(DataCenter dataCenter, Workload workload) {
    this.id = Particle.idCounter++;
    this.workload = workload;
    this.dataCenter = dataCenter;
    this.randomInitialization();
  }

  public Particle(DataCenter dataCenter, Workload workload, InitializationStrategy initializationStrategy) {
    this.id = Particle.idCounter++;
    this.workload = workload;
    this.dataCenter = dataCenter;
    if (initializationStrategy == Particle.InitializationStrategy.MCT) {
      this.mctInitialization();
    } else if (initializationStrategy == Particle.InitializationStrategy.HIGH_TASK_HIGH_VM) {
      this.highTaskHighVmInitialization();
    } else {
      this.randomInitialization();
    }
  }

  // NOTE Software Engineering Approach to the Algorithm's development
  // Returns true is this iteration was a success (ie. it acheived a new personal best)
  public int runIteration(double w) {

    this.updateVelocity(w);
    this.updatePosition();

    this.updateDataCenter();
    double objective = this.dataCenter.computeObjective();

    this.objectiveHistory.add(this.personalBestObjectiveValue);

    if (objective > this.personalBestObjectiveValue) {
      this.personalBestPosition = this.position.copy();
      this.personalBestObjectiveValue = objective;
      return 1;
    }

    return 0;
  }

  private void randomInitialization() {
    this.position = new Matrix(this.workload.getTaskCount(), this.dataCenter.getVirtualMachineCount());

    this.velocity = new Matrix(this.workload.getTaskCount(), this.dataCenter.getVirtualMachineCount());
    this.velocity.randomVelocityInitialization(0, 1);

    this.updatePosition();

    updateDataCenter();

    // Must compute the local best after building the task to vm mapping in the line above for accurate
    // objective function computation
    this.personalBestPosition = this.position;
    this.personalBestObjectiveValue = this.dataCenter.computeObjective();
  }

  // Minimum completion time algorithm used for initialization as per https://www.sciencedirect.com/science/article/pii/S1319157820305279#e0045
  // This paper supports MCT usage: https://pdf.sciencedirectassets.com/280416/1-s2.0-S1319157820X00024/1-s2.0-S1319157817303361/main.pdf?X-Amz-Security-Token=IQoJb3JpZ2luX2VjEGEaCXVzLWVhc3QtMSJHMEUCIAnFYurY1Gus9viOlcuoWEPxbszjPz4HQywtBp4gRh2GAiEAvCwsSvysGBgk38ovbjyhKrNg3VQ7falBDsL%2Ffm1nqVUqvAUImv%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FARAFGgwwNTkwMDM1NDY4NjUiDGcVxoQOmcec9UawiCqQBW3bNWEVUrCl35LbcAN1dLjqbZn01QwmpbbU2bjRA8J9EpXK1akvweU%2FwpxmEETHuQX8xXJeoWZk7QTSshMpT8cvEBmLG4HP0Knm4eyZAnB2WMRwuM3gUOubgBo1Bk5sWCpHQD2mpFJCe9NdnxonfqgebtDHRk68R%2BEJlSA14HkYQxvO3lVlg0hYrIUWV%2FP22SOw%2Bu3jSgQXkFh8jNHg884mxOwPSm%2F%2Fi60qXfDqGT9JnHjZ45FEQlg%2FD2T70Fq7Y74S6idhaq26CZ%2FzmVPJE%2BHgTsM3xbZTk9pJ6ewwSUHOxBKdmWHAwTbYBD4V2fBTPHYl96%2BaQNXtfnIOlkUnrXIBK8cfq4n3ak6xhwJ4p7fq8J7nYHIG1W5iA5MRgqigydWMa2kQQlrJPQwZy%2BzEp4n3fo7tc0E5pGqn70qGYUJVXKVXScfxont8PO812GbgXFuyJ3tyvIVdmb%2BYWQ1PSWb9cBDilrwT8DU5oqVTYGK7smM635ymN2LI%2BOsJ0tNFEJkACKTL8VrMo1qvlFi%2BtRzCeFdIestbSe6dVT1FeBtlPkm71xnj1rwp20oh94UjBClbruYulMteGIGa7jL9QUN33Miy6YcRzZxM6xepF36hXYz9zfVndFe6%2BaU9MdUn6eVYe8LaP%2BhGD5ReyVlR6ojss2R692fxnVxDdjUmKjjufpS2FkZEth7sUe2CdH3j6lyk7EldzztaDfu2EyeSgCj8YpxNDqvFYhO4l5HRutYG7EcqWb0qCoTr1S79SUOG0B%2Br2b2dBUYQDezsfFmA0ZT65DjdHaXScAZePIVgj%2FwBWwjGhtcZY5u1BoZ5wH4hUPqeqBvW73CtxL6Jsi7BwRiUjk44GSeBd6mKTSZRq%2BQmMJiHpqoGOrEBQGG%2FjXIrv2ZN2RY2l4NKeKTUcOLpR4EzE8sV23YtG51mF6kGX35v9aivZEwWqPZawpXx568%2F6vAcdM41WD1%2BcoLDzWV2DkmAcwO30NqnOR6CKVsnyhvXOfnbVKV24Ay7IKgxvERi%2BHL8x4%2F83GBCB3Wt%2BJXBGBFstvrpJjU6AVy4XxIVfHXp8nP%2BD7N3yU5y5JRkLZwgAHePh2PPlcVwwx%2F1CfYRuAu2pBlKWtTR8cq2&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20231107T003741Z&X-Amz-SignedHeaders=host&X-Amz-Expires=300&X-Amz-Credential=ASIAQ3PHCVTYQLJ5R7PC%2F20231107%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=990bc81ef32fb65c2e80efca27d3ec0328bcd903c6e9e5512ec0a4746453ce9c&hash=47ee638ef3e8fbf8d3fe647506afbc3712143b9210ac8e0b20c33ae2b38f120f&host=68042c943591013ac2b2430a89b270f6af2c76d8dfd086a07176afe7c76c2c61&pii=S1319157817303361&tid=spdf-216d5cd9-c2e1-44b6-a4fc-059dcfd86f5e&sid=2927ae6c23c719410f2a51a784616f99fc1agxrqa&type=client&tsoh=d3d3LnNjaWVuY2VkaXJlY3QuY29t&ua=131c585d53555b5556&rr=8221825779b64cd4&cc=us
  //    for both makespan and energy consumption
  private void mctInitialization() {
    // Attempt to overcome this issue with getting stuck in local optimums: https://www.mdpi.com/1999-4893/11/2/23
    // Potential remedy: https://www.intechopen.com/journals/1/articles/117
    this.position = new Matrix(this.workload.getTaskCount(), this.dataCenter.getVirtualMachineCount());

    this.dataCenter.resetVirtualMachineReadyTimes();

    ArrayList<Task> tasks = this.workload.getSortedTasks(true);

    for (Task t : tasks) {
      VirtualMachine vm = this.dataCenter.getVmWithMinEET(t);
      if (Utilities.getRandomDouble(0, 1) < 0.25) {
        int vmId = Utilities.getRandomInteger(0, this.dataCenter.getVirtualMachineCount() - 1);
        vm = this.dataCenter.getVirtualMachineById(vmId);
      }
      this.position.setComponent(t.getId() - 1, vm.getId() - 1, 1);
      this.dataCenter.addExecutionTimeToVirtualMachine(t, vm);
    }
    
    this.velocity = new Matrix(this.workload.getTaskCount(), this.dataCenter.getVirtualMachineCount());
    this.velocity.randomVelocityInitialization(0, 1);

    updateDataCenter();
    this.personalBestPosition = this.position;
    this.personalBestObjectiveValue = this.dataCenter.computeObjective();
  }

  // Initialization strategy used in this paper: https://www.mdpi.com/2079-9292/12/12/2580
  // Top 25% of tasks by MI assigned to Virtual Machines with higher processing capacity
  // The rest follow a random allocation strategy
  private void highTaskHighVmInitialization() {
    this.position = new Matrix(this.workload.getTaskCount(), this.dataCenter.getVirtualMachineCount());
    
    ArrayList<VirtualMachine> sortedVms = this.dataCenter.getSortedVirtualMachines();
    ArrayList<Task> sortedTasks = this.workload.getSortedTasks(false);

    int vmQ3 = sortedVms.size() * 1 / 2;
    int taskQ3 = sortedTasks.size() * 1 / 2;
    
    for (int i = 0; i < sortedTasks.size(); i++) {
      VirtualMachine vm;
      Task t = sortedTasks.get(i);
      if (i < taskQ3) {
        // Assign to low capacity machine
        vm = sortedVms.get(Utilities.getRandomInteger(0, vmQ3));
      } else {
        // Assign to high capacity machine
        vm = sortedVms.get(Utilities.getRandomInteger(vmQ3, sortedVms.size()));
      }
      this.position.setComponent(t.getId() - 1, vm.getId() - 1, 1);
    }

    // this.velocity = this.position.copy().multiply(0.2).addJ(0.1);
    this.velocity = new Matrix(this.workload.getTaskCount(), this.dataCenter.getVirtualMachineCount());
    this.velocity.randomVelocityInitialization(0, 1);

    updateDataCenter();
    this.personalBestPosition = this.position;
    this.personalBestObjectiveValue = this.dataCenter.computeObjective();
  }

  private void updateDataCenter() {
    this.dataCenter.resetVirtualMachineReadyTimes();

    for (int i = 0; i < this.position.getRowsCount(); i++) {
      Task task = workload.getTaskById(i);
      VirtualMachine virtualMachine = dataCenter.getVirtualMachineById((int) this.position.getIndexOfFirstNonZeroColumnForRow(i));
      this.dataCenter.addExecutionTimeToVirtualMachine(task, virtualMachine);
    }
  }

  private void updateVelocity(double w) {
    // Randomize constants r1 and r2
    double r1 = Utilities.getRandomDouble(0, 1);
    double r2 = Utilities.getRandomDouble(0, 1);

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

  public ArrayList<Double> getObjectiveHistory() {
    return this.objectiveHistory;
  }

  @Override
  public String toString() {
    return "(Particle" + this.id + ")";
  }
}
