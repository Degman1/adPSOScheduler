use rand::prelude::*;

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
