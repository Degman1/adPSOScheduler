package syslab.cloudcomputing.simulation;

import java.util.ArrayList;

/*
 * The concept of a data center that is abstracted away to focus only on the
 * virtual machines and their processing powers that lie on the physical machines
 */
public class CustomDataCenter extends DataCenter {
  public CustomDataCenter() {
    super();
  }

  public CustomDataCenter(ArrayList<VirtualMachine> virtualMachines) {
    super(virtualMachines);
  }

  @Override
	protected double getLoadExecutionTime(Task task, VirtualMachine virtualMachine) {
		// The size of the task divided by the assigned VM's computation power
		return task.getMillionsOfInstructions() / (double) virtualMachine.getMillionsOfInstructionsPerSecond();
	}
}
