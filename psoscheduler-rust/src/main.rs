use crate::simulation::task;

mod schedule {
    mod scheduler;
}

mod simulation {
    pub(crate) mod task;
}

fn main() {
    println!("Hello, world!");
    let t = task::Task::new(500);
    let t2 = task::Task::new(600);
    println!("{} {}", t, t2);
}
