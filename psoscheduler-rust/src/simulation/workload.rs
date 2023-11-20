use core::fmt;
use std::{sync::atomic::{AtomicUsize, Ordering}, fmt::Display};

use super::task::Task;

static WORKLOAD_ID_COUNTER: AtomicUsize = AtomicUsize::new(1);

pub struct Workload {
  pub workload_id: usize,
  pub tasks: Vec<Task>
}

impl Workload {
  pub fn new() -> Workload {
    Workload {
      workload_id: WORKLOAD_ID_COUNTER.fetch_add(1, Ordering::Relaxed),
      tasks: Vec::new() }
  }

  pub fn add_task(&mut self, mut t: Task) {
    t.workload_id = self.workload_id;
    self.tasks.push(t);
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
    return write!(f, "Workload{}: {}", self.workload_id, output);
  }
}