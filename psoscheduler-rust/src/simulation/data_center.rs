use core::fmt;
use std::sync::atomic::{AtomicUsize, Ordering};
use std::collections::HashMap;

use super::virtual_machine::VirtualMachine;
use super::task::Task;

static DATA_CENTER_ID_COUNTER: AtomicUsize = AtomicUsize::new(1);

pub(crate) struct DataCenter {
  pub id: usize,
  pub virtual_machines: Vec<VirtualMachine>,
  pub task_count: u32,
  pub virtual_machine_ready_time: HashMap<usize, f32>,
  pub total_millions_of_instructions: u32,
}

impl DataCenter {
  pub fn new() -> DataCenter {
    DataCenter {
      id: DATA_CENTER_ID_COUNTER.fetch_add(1, Ordering::Relaxed),
      virtual_machines: Vec::new(),
      task_count: 0,
      virtual_machine_ready_time: HashMap::new(),
      total_millions_of_instructions: 0
    }
  }

  pub fn add_virtual_machine(&mut self, mut vm: VirtualMachine) {
    vm.data_center_id = self.id;
    self.virtual_machine_ready_time.insert(vm.id, 0.0);
    self.virtual_machines.push(vm);
  }

  pub fn reset_virtual_machine_ready_time(&mut self) {
    self.virtual_machine_ready_time.clear();
    for vm in self.virtual_machines.iter() {
      self.virtual_machine_ready_time.insert(vm.id, 0.0);
    }
    self.task_count = 0;
    self.total_millions_of_instructions = 0;
  }

  fn get_task_execution_time(&self, task: &Task, vm: &VirtualMachine) -> f32 {
    (task.millions_of_instructions as f32) / (vm.millions_of_instructions_per_second as f32)
  }

  pub fn add_execution_time_to_virtual_machine(&mut self, task: Task, vm: VirtualMachine) {
    let current_execution_time: f32;
    match self.virtual_machine_ready_time.get(&task.id) {
      Some(time) => current_execution_time = *time,
      None => current_execution_time = 0.0
    }

    let new_execution_time: f32 = current_execution_time + self.get_task_execution_time(&task, &vm);
    self.virtual_machine_ready_time.insert(vm.id, new_execution_time);
    self.task_count += 1;
    self.total_millions_of_instructions += task.millions_of_instructions;
  }

  
}

impl fmt::Display for DataCenter {
  fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
    let mut output: String = String::new();
    for (key, value) in self.virtual_machine_ready_time.iter() {
      output.push_str(&key.to_string());
      output.push_str("=>");
      output.push_str(&value.to_string());
      output.push(' ');
    }
    write!(f, "DataCenter{}: Task Load Count = {}; Ready Time = {}", self.id, self.task_count, output)
  }
}
