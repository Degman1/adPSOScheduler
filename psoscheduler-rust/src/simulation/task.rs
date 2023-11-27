use core::fmt;
use std::sync::atomic::{AtomicUsize, Ordering};

static TASK_ID_COUNTER: AtomicUsize = AtomicUsize::new(0);

#[derive(Hash, PartialEq, Eq)]
pub struct Task {
  pub id: usize,
  pub workload_id: usize,
  pub millions_of_instructions: u32
}

impl Task {
  pub fn new(millions_of_instructions: u32) -> Task {
    Task { 
      id: TASK_ID_COUNTER.fetch_add(1, Ordering::Relaxed),
      workload_id: 0,
      millions_of_instructions: millions_of_instructions
    }
  }
}

impl PartialOrd for Task {
    fn partial_cmp(&self, other: &Self) -> Option<std::cmp::Ordering> {
      other.millions_of_instructions.partial_cmp(&self.millions_of_instructions)
    }
}

impl Ord for Task {
    fn cmp(&self, other: &Self) -> std::cmp::Ordering {
      other.millions_of_instructions.cmp(&self.millions_of_instructions)
    }
}

impl fmt::Display for Task {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
      write!(f, "(Task{}: {} mi)", self.id, self.millions_of_instructions)
    }
}