use core::fmt;
use std::sync::atomic::{AtomicUsize, Ordering};

use super::task::Task;

static WORKLOAD_ID_COUNTER: AtomicUsize = AtomicUsize::new(0);

pub struct Workload {
  pub id: usize,
  pub tasks: Vec<Task>
}

impl Workload {
  pub fn new() -> Workload {
    Workload {
      id: WORKLOAD_ID_COUNTER.fetch_add(1, Ordering::Relaxed),
      tasks: Vec::new() }
  }

  pub fn add_task(&mut self, mut t: Task) {
    t.workload_id = self.id;
    self.tasks.push(t);
  }

  pub fn get_sorted_tasks(&self) -> Vec<&Task> {
    let mut clone = Vec::new();
    for task in self.tasks.iter() {
      clone.push(task);
    }
    clone.sort();
    clone
  }
}

impl fmt::Display for Workload {
  fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
    let mut output: String = String::new();
    for i in 0..self.tasks.len() {
      output.push_str(&self.tasks[i].to_string());
      if i < self.tasks.len() - 1 {
        output.push_str(", ");
      }
    }
    write!(f, "Workload{}: {}", self.id, output)
  }
}