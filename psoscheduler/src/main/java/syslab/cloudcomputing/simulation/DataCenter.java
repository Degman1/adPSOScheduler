package syslab.cloudcomputing.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * The concept of a data center that is abstracted away to focus only on the
 * virtual machines and their processing powers that lie on the physical machines
 */
public class DataCenter {
  private int dcVirtualMachineId = 1;
  // Maps the vm id to the vm object
  private ArrayList<VirtualMachine> virtualMachines;

  private HashMap<VirtualMachine, Double> virtualMachineReadyTime;
  private int taskCount = 0;

  public DataCenter() {
    this.virtualMachines = new ArrayList<VirtualMachine>();
  }

  public DataCenter(ArrayList<VirtualMachine> virtualMachines) {
    this.virtualMachines = virtualMachines;
    this.virtualMachineReadyTime = new HashMap<VirtualMachine, Double>();
  }

  public void addVirtualMachine(VirtualMachine virtualMachine) {
    virtualMachine.setDataCenterId(dcVirtualMachineId);
    this.virtualMachines.add(virtualMachine);
  }

  public VirtualMachine getVirtualMachineById(int id) {
    return this.virtualMachines.get(id);
  }

  public void resetVirtualMachineReadyTimes() {
    this.virtualMachineReadyTime.clear();
    for (VirtualMachine virtualMachine : virtualMachines) {
      this.virtualMachineReadyTime.put(virtualMachine, 0.0);
    }
    this.taskCount = 0;
  }

  public void addExecutionTimeToVirtualMachine(int millionsOfInstructions, VirtualMachine virtualMachine) {
    double currentExecutionTime = 0.0;
    if (this.virtualMachineReadyTime.containsKey(virtualMachine)) {
      currentExecutionTime = this.virtualMachineReadyTime.get(virtualMachine);
    }
    double newExecutionTime = currentExecutionTime + getLoadExecutionTime(millionsOfInstructions, virtualMachine);
    this.virtualMachineReadyTime.put(virtualMachine, newExecutionTime);
    taskCount++;
  }

	private double getLoadExecutionTime(int millionsOfInstructions, VirtualMachine virtualMachine) {
		// The size of the task divided by the assigned VM's computation power
		return millionsOfInstructions / (double) virtualMachine.getMillionsOfInstructionsPerSecond();
	}

	public double computeObjective() {
		double makespan = this.computeMakespan();
    // Compute throughput manually as to not recompute makespan
		double throughput = this.taskCount / makespan;
		
		return throughput + (1 / makespan);
	}

  public double computeMakespan() {
    double makespan = 0.0;
		
		// Makespan is defined as the maximum completion time over all VMs
		for (Map.Entry<VirtualMachine, Double> entry : this.virtualMachineReadyTime.entrySet()) {
      double completionTime = entry.getValue();
      if (completionTime > makespan) {
				makespan = completionTime;
			}
    } 

    return makespan;
  }

  public double computeThroughput() {
    return this.taskCount / this.computeMakespan();
  }

  public int getVirtualMachineCount() {
    return this.virtualMachines.size();
  }
}
