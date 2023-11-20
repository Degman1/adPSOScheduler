use rand::Rng;
use std::fs::File;
use std::io::Write;

pub fn get_random_integer(min: u8, max: u8) -> u8 {
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
      output.push_str(&objective_value.to_string());
      if (i < history.len() - 1) {
        output.push_str("\t");
      }
      output.push("\n");
    }
  }

  let mut outfile = File::create(path)?;
  write!(outfile, output)?;
}
