package syslab.cloudcomputing.simulation;

/*
 * Task represents a single computational task that is required to be completed 
 * as quickly as possible by some assigned virtual machine
 */
public class Task {
  private static int idCounter = 1;
	private int id;
	private int workloadId;
	private int millionsOfInstructions;

	public Task(int millionsOfInstructions) {
		this.millionsOfInstructions = millionsOfInstructions;
		this.id = Task.idCounter++;
		this.workloadId = 0;
	}
	
	public int hashCode() {
		return id;
	}
	
	public boolean equals(Task otherTask) {
		return this.id == otherTask.id;
	}

  public String toString() {
    return String.format("(TSK%d: %d mi)", this.id, this.millionsOfInstructions);
  }

  public int getId() {
    return this.id;
  }

  public int getMillionsOfInstructions() {
    return this.millionsOfInstructions;
  }

	public int getWorkloadId() {
		return this.workloadId;
	}

	public void setWorkloadId(int id) {
		this.workloadId = id;
	}	
}
