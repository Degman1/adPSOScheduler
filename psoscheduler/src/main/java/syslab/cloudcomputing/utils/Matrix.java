package syslab.cloudcomputing.utils;

public class Matrix {
  private double[][] mtx;
  private int rows;
  private int columns;

  public Matrix(int rows, int columns) {
    this.mtx = new double[rows][columns];
  }

  // This init is used to randomly populate the matrix values to act as particle positions
  public void randomPositionInitialization() {
    for (int i = 0; i < this.mtx.length; i++) {
      int j = Utilities.getRandomInteger(0, this.mtx[0].length);
      this.mtx[i][j] = Utilities.getRandomInteger(0, 1);
    }
  }

  // This init is used to randomly populate the matrix values to act as particle velocities
  // Init velocities to zero: https://ieeexplore.ieee.org/document/6256112
  // NOTE this could have a big impact on convergence speed: https://www.hindawi.com/journals/cin/2021/6628889/
  public void randomVelocityInitialization(double randomMin, double randomMax) {
    for (int i = 0; i < this.mtx.length; i++) {
      for (int j = 0; j < this.mtx[0].length; j++) {
        this.mtx[i][j] = Utilities.getRandomDouble(randomMin, randomMax);
      }
    }
  }

  public int getIndexOfFirstNonZeroColumnForRow(int row) {
    for (int col = 0; col < this.mtx[0].length; col++) {
      if (this.mtx[row][col] == 1) {
        return col;
      }
    }
    return -1;
  }

  private void operate(Matrix otherPosition, BinaryOperation binaryOperation) {
    if (otherPosition.mtx.length == this.mtx.length && 
        otherPosition.mtx.length > 0 && 
        otherPosition.mtx.length == this.mtx.length) {
      for (int i = 0; i < this.mtx.length; i++) {
        for (int j = 0; j < this.mtx[0].length; j++) {
          this.mtx[i][j] = binaryOperation.operate(this.mtx[i][j], otherPosition.mtx[i][j]);
        }
      }
    }
  }

  public void add(Matrix otherPosition) {
    BinaryOperation addition = (x, y) -> x + y;
    this.operate(otherPosition, addition);
  }

  public void subtract(Matrix otherPosition) {
    BinaryOperation subtraction = (x, y) -> x - y;
    this.operate(otherPosition, subtraction);
  }

  public void multiply(Matrix otherPosition) {
    BinaryOperation multilpication = (x, y) -> x * y;
    this.operate(otherPosition, multilpication);
  }

  public void division(Matrix otherPosition) {
    BinaryOperation division = (x, y) -> x / y;
    this.operate(otherPosition, division);
  }

  public int getRows() {
    return this.rows;
  }

  public int getColumns() {
    return this.columns;
  }
}
