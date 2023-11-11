package syslab.cloudcomputing.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    if (id >= 0 && id < this.tasks.size()) {
      return this.tasks.get(id);
    }
    
    return null;
  }

  public ArrayList<Task> getSortedTasks(Boolean reversed) {
    ArrayList<Task> tasks = new ArrayList<>(this.tasks);
    if (reversed) {
      Collections.sort(tasks, Comparator.comparing(Task::getMillionsOfInstructions).reversed());
    } else {
      Collections.sort(tasks, Comparator.comparing(Task::getMillionsOfInstructions));
    }
    return tasks;
  }

  public int getTaskCount() {
    return this.tasks.size();
  }

  public static Workload generateNullWorkload(int nTasks) {
    Workload wk = new Workload();
    for (int i = 0; i < nTasks; i++) {
      wk.addTask(new Task(0));
    }
    return wk;
  }

  public ArrayList<Task> getTasks() {
    return this.tasks;
  }

  @Override
  public String toString() {
    return "{ Workload: " + tasks + " }";
  }

}
