use std::sync::atomic::AtomicUsize;

static TASK_ID_COUNTER: AtomicUsize = AtomicUsize::new(1);

pub struct Task {

}

impl Task {
  fn get_id() -> usize {
    TASK_ID_COUNTER.fetch_add(1, Ordering::Relaxed)
  }
}