mod schedule {
    pub(crate) mod scheduler;
}

mod simulation {
    pub(crate) mod task;
    pub(crate) mod workload;
    pub(crate) mod virtual_machine;
    pub(crate) mod data_center;
}

mod pso {
    pub(crate) mod particle;
    pub(crate) mod pso_swarm;
}

mod utils {
    pub(crate) mod utilities;
}

fn main() {
    println!("Hello, world!");
    let t = simulation::task::Task::new(500);
    let t2 = simulation::task::Task::new(600);
    println!("{} {}", t, t2);
    let mut wk = simulation::workload::Workload::new();
    wk.add_task(t);
    wk.add_task(t2);
    println!("{}", wk);
    let vm = simulation::virtual_machine::VirtualMachine::new(200, 300.0);
    let vm2 = simulation::virtual_machine::VirtualMachine::new(400, 500.0);
    println!("{} {}", vm, vm2);
    let mut dc = simulation::data_center::DataCenter::new();
    
}
