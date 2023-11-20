use core::fmt;
use std::sync::atomic::{AtomicUsize, Ordering};

static TASK_ID_COUNTER: AtomicUsize = AtomicUsize::new(1);

pub struct Task {
  id: usize,
  workload_id: usize,
  millions_of_instructions: i32
}

impl Task {
  pub fn new(millions_of_instructions: i32) -> Task {
    Task { 
      id: TASK_ID_COUNTER.fetch_add(1, Ordering::Relaxed),
      workload_id: 0,
      millions_of_instructions: millions_of_instructions
    }
  }

  fn get_id(&self) -> usize {
    return self.id;
  }

  fn get_millions_of_instructions(&self) -> i32 {
    return self.millions_of_instructions;
  }

  fn get_workload_id(&self) -> usize {
    return self.workload_id;
  }

  fn set_workload_id(&mut self, workload_id: usize) {
    self.workload_id = workload_id;
  }
}

impl fmt::Display for Task {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
      let mut output: String = String::new();
      output.push_str("(TSK");
      output.push_str(&self.get_id().to_string());
      output.push_str(": ");
      output.push_str(&self.millions_of_instructions.to_string());
      output.push_str(" mi)");
      return write!(f, "{}", output);
    }
}