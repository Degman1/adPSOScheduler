package syslab.cloudcomputing.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public abstract class DataCenter {
  protected int dcVirtualMachineId = 1;
  // Maps the vm id to the vm object
  protected ArrayList<VirtualMachine> virtualMachines;

  protected HashMap<VirtualMachine, Double> virtualMachineReadyTime;
  protected int taskCount = 0;

  protected int totalMillionsOfInstructions = 0;

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
    this.totalMillionsOfInstructions = 0;
  }

  public void addExecutionTimeToVirtualMachine(Task task, VirtualMachine virtualMachine) {
    double currentExecutionTime = 0.0;
    if (this.virtualMachineReadyTime.containsKey(virtualMachine)) {
      currentExecutionTime = this.virtualMachineReadyTime.get(virtualMachine);
    }
    double newExecutionTime = currentExecutionTime + getLoadExecutionTime(task, virtualMachine);
    this.virtualMachineReadyTime.put(virtualMachine, newExecutionTime);
    taskCount++;
    this.totalMillionsOfInstructions += task.getMillionsOfInstructions();
  }

  abstract protected double getLoadExecutionTime(Task task, VirtualMachine virtualMachine);

  public double computeObjective() {
		double makespan = this.computeMakespan();
    double throughput = (this.taskCount / makespan);
    double KW = this._computeEnergyConsumptionKW(makespan);
    double hW = KW * 10;  // hectowatts
    double averageWattsPerTask = hW / this.taskCount;
    // Scale the energy consumption to an appropriate weight in the objective function
    return throughput + (2.0 / averageWattsPerTask);
	}

  // Private version that doesn't recompute the makespan to prioritize efficiency
  private double _computeEnergyConsumptionKW(double makespan) {
    double energyConsumption = 0;

    for (Map.Entry<VirtualMachine, Double> entry : this.virtualMachineReadyTime.entrySet()) {
      VirtualMachine vm = entry.getKey();
      double millionsOfInstructions = entry.getValue();
      double machineEnergyConsumption = millionsOfInstructions * vm.getActiveStateJoulesPerMillionInstructions();
      machineEnergyConsumption += (makespan - millionsOfInstructions) * vm.getIdleStateJoulesPerMillionInstructions();
      machineEnergyConsumption *= vm.getMillionsOfInstructionsPerSecond();
      energyConsumption += machineEnergyConsumption;
    }

    // Convert from Joules * MIPS to Joules / sec (Watts)
    energyConsumption /= this.totalMillionsOfInstructions;

    // Convert from Watts (W) to KiloWatts (KW)
    energyConsumption /= 1000.0;

    return energyConsumption;
  }

  public double computeEnergyConsumptionKW() {
    double makespan = this.computeMakespan();
    return this._computeEnergyConsumptionKW(makespan);
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

  public VirtualMachine getVmWithMinEET(Task t) {
    Double minEET = null;
    VirtualMachine vmMinEET = null;

    for (VirtualMachine vm : this.virtualMachines) {
      double eet = this.virtualMachineReadyTime.get(vm) + this.getLoadExecutionTime(t, vm);
      if (minEET == null || eet < minEET) {
        minEET = eet;
        vmMinEET = vm;
      }
    }

    return vmMinEET;
  }

  public ArrayList<VirtualMachine> getSortedVirtualMachines() {
    ArrayList<VirtualMachine> virtualMachines = new ArrayList<>(this.virtualMachines);
    Collections.sort(virtualMachines, Comparator.comparing(VirtualMachine::getMillionsOfInstructionsPerSecond));
    return virtualMachines;
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
