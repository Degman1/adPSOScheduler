use core::fmt;
use std::sync::atomic::{AtomicUsize, Ordering};
use std::collections::HashMap;

use super::virtual_machine::VirtualMachine;

static DATA_CENTER_ID_COUNTER: AtomicUsize = AtomicUsize::new(1);

struct DataCenter {
  id: usize,
  virtual_machines: Vec<VirtualMachine>,
  task_count: u32,
  virtual_machine_ready_time: HashMap<VirtualMachine, f32>
}

impl DataCenter {

}

impl fmt::Display for DataCenter {
  fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
    let mut output: String = String::new();
    for i in 0..self.virtual_machines.len() {
      output.push_str(&self.virtual_machines[i].to_string());
      if i < self.virtual_machines.len() - 1 {
        output.push_str(", ");
      }
    }
    write!(f, "DataCenter{}: {}", self.id, output)
  }
}