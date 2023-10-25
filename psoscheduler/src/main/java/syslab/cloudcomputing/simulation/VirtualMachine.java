package syslab.cloudcomputing.simulation;

/*
 * VirtualMachine represents a single virtual machine that sections off a portion of a
 * single machine in a data center
 */
public class VirtualMachine {
  private static int idCounter = 1;
	private int id;
	private int dataCenterId;
	private int millionsOfInstructionsPerSecond;

	public VirtualMachine(int millionsOfInstructionsPerSecond) {
		this.id = VirtualMachine.idCounter++;
		this.millionsOfInstructionsPerSecond = millionsOfInstructionsPerSecond;
		this.dataCenterId = 0;
	}
	
	public int hashCode() {
		return id;
	}
	
	public boolean equals(VirtualMachine otherVM) {
		return this.id == otherVM.id;
	}

  public String toString() {
    return String.format("(VM%i: %imips)", this.id, this.millionsOfInstructionsPerSecond);
  }

  public int getId() {
    return this.id;
  }

  public int getMillionsOfInstructionsPerSecond() {
    return this.millionsOfInstructionsPerSecond;
  }

	public int getDataCenterId() {
		return this.dataCenterId;
	}

	public void setDataCenterId(int dataCenterId) {
		this.dataCenterId = dataCenterId;
	}
}
