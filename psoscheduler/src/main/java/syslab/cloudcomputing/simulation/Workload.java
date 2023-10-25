package syslab.cloudcomputing.simulation;

import java.util.ArrayList;

public class Workload {
  int taskId = 1;
  // Maps the task id to the Task object
  private ArrayList<Task> tasks;

  public Workload() {
    this.tasks = new ArrayList<Task>();
  }

  public Workload(ArrayList<Task> tasks) {
    this.tasks = tasks;
  }

  public void addTask(Task task) {
    task.setWorkloadId(taskId++);
    this.tasks.add(task);
  }

  public Task getTaskById(int id) {
    if (id > 0 && id <= taskId) {
      return this.tasks.get(id);
    }
    
    return null;
  }

  public int getTaskCount() {
    return this.tasks.size();
  }
}
