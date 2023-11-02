package syslab.cloudcomputing.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class DataCenter {
  protected int dcVirtualMachineId = 1;
  // Maps the vm id to the vm object
  protected ArrayList<VirtualMachine> virtualMachines;

  protected HashMap<VirtualMachine, Double> virtualMachineReadyTime;
  protected int taskCount = 0;

  public DataCenter() {
    this.virtualMachines = new ArrayList<VirtualMachine>();
    this.virtualMachineReadyTime = new HashMap<VirtualMachine, Double>();
  }

  public DataCenter(ArrayList<VirtualMachine> virtualMachines) {
    this.virtualMachines = virtualMachines;
    this.virtualMachineReadyTime = new HashMap<VirtualMachine, Double>();
    for (VirtualMachine vm : virtualMachines) {
      this.virtualMachineReadyTime.put(vm, 0.0);
    }
  }

  public void addVirtualMachine(VirtualMachine virtualMachine) {
    virtualMachine.setDataCenterId(dcVirtualMachineId);
    this.virtualMachines.add(virtualMachine);
    this.virtualMachineReadyTime.put(virtualMachine, 0.0);
  }

  public VirtualMachine getVirtualMachineById(int id) {
    return this.virtualMachines.get(id);
  }

  public void resetVirtualMachineReadyTimes() {
    this.virtualMachineReadyTime.clear();
    for (VirtualMachine virtualMachine : this.virtualMachines) {
      this.virtualMachineReadyTime.put(virtualMachine, 0.0);
    }
    this.taskCount = 0;
  }

  public void addExecutionTimeToVirtualMachine(Task task, VirtualMachine virtualMachine) {
    double currentExecutionTime = 0.0;
    if (this.virtualMachineReadyTime.containsKey(virtualMachine)) {
      currentExecutionTime = this.virtualMachineReadyTime.get(virtualMachine);
    }
    double newExecutionTime = currentExecutionTime + getLoadExecutionTime(task, virtualMachine);
    this.virtualMachineReadyTime.put(virtualMachine, newExecutionTime);
    taskCount++;
  }

  abstract protected double getLoadExecutionTime(Task task, VirtualMachine virtualMachine);

  public double computeObjective() {
		double makespan = this.computeMakespan();
    // Compute throughput manually as to not recompute makespan
		double throughput = this.taskCount / makespan;
		
		return throughput + (1 / makespan);
	}

  public double computeMakespan() {
    double maxMakespan = 0.0;
		
		// Makespan is defined as the maximum completion time over all VMs
		for (Map.Entry<VirtualMachine, Double> entry : this.virtualMachineReadyTime.entrySet()) {
      double completionTime = entry.getValue();
      if (completionTime > maxMakespan) {
        maxMakespan = completionTime;
			}
    }

    return maxMakespan;
  }

  public double computeThroughput() {
    return this.taskCount / this.computeMakespan();
  }

  public int getVirtualMachineCount() {
    return this.virtualMachines.size();
  }

  public int getTaskCount() {
    return this.taskCount;
  }

  @Override
  public String toString() {
    String desc = "{ DataCenter: Machine Count = " + this.virtualMachines.size();
    desc += "; Task Load Count = " + this.taskCount;
    desc += "; Ready Time = " + this.virtualMachineReadyTime + " }";
    return desc;
  }
}
