use rand::Rng;
use std::fs::File;
use std::io::Write;

pub fn get_random_integer(min: usize, max: usize) -> usize {
  if min >= max {
    return 0;
  }
  let mut rng = rand::thread_rng();
  return rng.gen_range(min..max);
}

pub fn get_random_float(min: f32, max: f32) -> f32 {
  if min >= max {
    return 0.0;
  }
  let mut rng = rand::thread_rng();
  return rng.gen::<f32>() * (max - min) + min;
}

pub fn write_objective_history_to_csv(objective_histories: &[[f32; 200]; 20], path: &str) {
  let mut output = String::new();

  for history in objective_histories.iter() {
    for i in 0..history.len() {
      output.push_str(&history[i].to_string());
      if i < history.len() - 1 {
        output.push('\t');
      }
    }
    output.push('\n');
  }

  let mut data_file = File::create(path).expect("File creation or opening failed");
  data_file.write(output.as_bytes()).expect("File write failed");
}
