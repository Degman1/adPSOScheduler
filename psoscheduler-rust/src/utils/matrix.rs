use std::ops::Index;
use std::ops::IndexMut;
use core::fmt;

use super::utilities::get_random_float;
use super::utilities::get_random_integer;

#[derive(Clone)]
pub struct Matrix {
  data: [[f32; COLS]; ROWS],
}

impl<const ROWS: usize, const COLS: usize> Matrix<ROWS, COLS> {
  pub fn new(data: [[f32; COLS]; ROWS]) -> Self {
    Self { data }
  }

  pub fn zeros() -> Self {
    Self { data: [[0.; COLS]; ROWS] }
  }

  pub fn random_velocity_init(min: f32, max: f32) -> Self {
    let mut m: Matrix<ROWS, COLS> = Self::zeros();
    for i in 0..ROWS {
      for j in 0..COLS {
        m[(i, j)] = get_random_float(min, max);
      }
    }
    m
  }

  pub fn random_position_init() -> Self {
    let mut m: Matrix<ROWS, COLS> = Self::zeros();
    for i in 0..ROWS {
      m[(i, get_random_integer(0, COLS))] = 1.;
    }
    m
  }

  pub fn mul(&mut self, rhs: f32) {
    for i in 0..ROWS {
      for j in 0..COLS {
        self.data[i][j] *= rhs;
      }
    }
  }

  pub fn add(&mut self, rhs: Matrix<ROWS, COLS>) {
    for i in 0..ROWS {
      for j in 0..COLS {
        self.data[i][j] += rhs[(i, j)];
      }
    }
  }

  pub fn sub(&mut self, rhs: Matrix<ROWS, COLS>) {
    for i in 0..ROWS {
      for j in 0..COLS {
        self.data[i][j] -= rhs[(i, j)];
      }
    }
  }

  pub fn enforce_elementwise_bound(&mut self, max_bound: f32) {
    for i in 0..ROWS {
      for j in 0..COLS {
        if self.data[i][j].abs() >= max_bound {
          self.data[i][j] = get_random_float(0., max_bound);
        }
      }
    }
  }

  pub fn zero_out(&mut self) {
    for i in 0..ROWS {
      for j in 0..COLS {
        self.data[i][j] = 0.;
      }
    }
  }

  pub fn get_index_of_maximum_col_for_row(&self, row: usize) -> usize {
    let mut max_index: usize = 0;
    let mut max_value: f32 = self.data[0][0];

    for col in 0..COLS {
      if self.data[row][col] > max_value {
        max_index = col;
        max_value = self.data[row][col];
      }
    }

    max_index
  }

  pub fn get_index_of_first_non_zero_column_for_row(&self, row: usize) -> usize {
    for col in 0..COLS {
      if self.data[row][col] > 0.0001 || self.data[row][col] < -0.0001 {
        return col;
      }
    }

    panic!("No value of 1 found in the row {} of the matrix", row);
  }
}

impl<const ROWS: usize, const COLS: usize> Index<(usize, usize)> for Matrix<ROWS, COLS> {
  type Output = f32;

  fn index(&self, index: (usize, usize)) -> &Self::Output {
      &self.data[index.0][index.1]
  }
}

impl<const ROWS: usize, const COLS: usize> IndexMut<(usize, usize)> for Matrix<ROWS, COLS> {
    fn index_mut(&mut self, index: (usize, usize)) -> &mut Self::Output {
      &mut self.data[index.0][index.1]
    }
}

impl<const ROWS: usize, const COLS: usize> fmt::Display for Matrix<ROWS, COLS> {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
      write!(f, "{:?}", self.data)
    }
}