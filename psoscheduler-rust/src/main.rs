use crate::simulation::task;

mod schedule {
    mod scheduler;
}

mod simulation {
    pub(crate) mod task;
    pub(crate) mod workload;
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
}
