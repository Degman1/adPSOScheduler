use std::sync::atomic::{AtomicUsize, Ordering};

static TASK_ID_COUNTER: AtomicUsize = AtomicUsize::new(1);

pub struct Task {
  id: usize,

}

impl Task {
  pub fn new() -> Task {
    Task { TASK_ID_COUNTER.fetch_add(1, Ordering::Relaxed); }
  }

  fn get_id() -> usize {
    id;
  }
}