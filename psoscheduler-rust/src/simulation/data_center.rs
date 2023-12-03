use core::fmt;
use std::sync::atomic::{AtomicUsize, Ordering};
use std::collections::HashMap;

use super::virtual_machine::VirtualMachine;
use super::task::Task;

static DATA_CENTER_ID_COUNTER: AtomicUsize = AtomicUsize::new(0);

pub struct DataCenter {
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
    // println!("{} / {} = {}", task.millions_of_instructions as f32, vm.millions_of_instructions_per_second as f32, (task.millions_of_instructions as f32) / (vm.millions_of_instructions_per_second as f32));
    (task.millions_of_instructions as f32) / (vm.millions_of_instructions_per_second as f32)
  }

  pub fn add_execution_time_to_virtual_machine(&mut self, task: &Task, vm_id: usize) {
    let vm = &self.virtual_machines[vm_id];
    let current_execution_time: f32;
    match self.virtual_machine_ready_time.get(&vm_id) {
      Some(time) => current_execution_time = *time,
      None => current_execution_time = 0.0
    }

    let new_execution_time: f32 = current_execution_time + self.get_task_execution_time(&task, &vm);
    // println!("setting to {} sec", new_execution_time);
    self.virtual_machine_ready_time.insert(vm.id, new_execution_time);
    self.task_count += 1;
    self.total_millions_of_instructions += task.millions_of_instructions;
  }

  pub fn compute_makespan(&self) -> f32 {
    match self.virtual_machine_ready_time
      .iter()
      .max_by(|a, b| a.1.partial_cmp(&b.1).unwrap())
      .map(|(_k, v)| v) {
        Some(makespan) => *makespan,
        None => 0.0
      }
  }

  pub fn compute_throughput(&self) -> f32 {
    (self.task_count as f32) / self.compute_makespan()
  }

  pub fn _compute_energy_consumption_kwh(&self, makespan: f32) -> f32 {
    let mut energy_consumption: f32 = 0.0;

    for (vm_id, vm_expected_execution_time) in self.virtual_machine_ready_time.iter() {
      let vm = &self.virtual_machines[*vm_id];
      let mut machine_energy_consumption = vm_expected_execution_time * vm.active_state_joules_per_million_instructions;
      machine_energy_consumption += (makespan - vm_expected_execution_time) * vm.get_idle_state_joules_per_million_instructions();
      machine_energy_consumption *= vm.millions_of_instructions_per_second as f32;
      energy_consumption += machine_energy_consumption;
    }

    // Now we have joules, so divide it by the total number of seconds to get Watts=J/s
    // energy_consumption /= makespan as f32;

    // Now, convert to kWh
    energy_consumption /= 3600000.;

    return energy_consumption;
  }

  pub fn compute_energy_consumption_kwh(&self) -> f32 {
    let makespan = self.compute_makespan();
    self._compute_energy_consumption_kwh(makespan)
  }

  pub fn compute_objective(&self) -> f32 {
    let makespan = self.compute_makespan();
    let throughput = (self.task_count as f32) / makespan;
    let wh = self._compute_energy_consumption_kwh(makespan) * 1000.;
    let kwh_per_task = wh / self.task_count as f32;
    // Scale the energy consumption to an appropriate weight in the objective function
    return throughput + (0.1 / kwh_per_task);
  }

  pub fn get_min_eet_virtual_machine(&self, task: &Task) -> usize {
    let cmp = |a: &(&usize, &f32), b: &(&usize, &f32)| -> std::cmp::Ordering {
      let a_vm = &self.virtual_machines[*a.0];
      let b_vm = &self.virtual_machines[*b.0];
      let a_eet = self.virtual_machine_ready_time.get(a.0).unwrap() + self.get_task_execution_time(task, a_vm);
      let b_eet = self.virtual_machine_ready_time.get(b.0).unwrap() + self.get_task_execution_time(task, b_vm);
      return a_eet.partial_cmp(&b_eet).unwrap();
    };

    match self.virtual_machine_ready_time
      .iter()
      .min_by(cmp)
      .map(|(k, _v)| k) {
        Some(vm) => *vm,
        None => panic!("No vm with a minimum expected execution time was found")
      }
  }
}

impl fmt::Display for DataCenter {
  fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
    let mut output: String = String::new();
    for (key, value) in self.virtual_machine_ready_time.iter() {
      output.push_str("VM");
      output.push_str(&key.to_string());
      output.push_str("=>");
      output.push_str(&value.to_string());
      output.push(' ');
    }
    write!(f, "DataCenter{}: Task Load Count = {}; Ready Time = {}", self.id, self.task_count, output)
  }
}
