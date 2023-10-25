package syslab.cloudcomputing.utils;

public class Matrix {
  private static BinaryOperation addition = (x, y) -> x + y;
  private static BinaryOperation subtraction = (x, y) -> x - y;
  private static BinaryOperation multilpication = (x, y) -> x * y;
  private static BinaryOperation division = (x, y) -> x / y;

  private double[][] mtx;
  private int rows;
  private int columns;

  public Matrix(int rows, int columns) {
    this.mtx = new double[rows][columns];
  }

  // This init is used to randomly populate the matrix values to act as particle positions
  public void randomPositionInitialization() {
    for (int i = 0; i < this.mtx.length; i++) {
      int j = Utilities.getRandomInteger(0, this.getColumns());
      this.mtx[i][j] = Utilities.getRandomInteger(0, 1);
    }
  }

  private void setComponent(int row, int column, double value) {
    this.mtx[row][column] = value;
  }

  // This init is used to randomly populate the matrix values to act as particle velocities
  // Init velocities to zero: https://ieeexplore.ieee.org/document/6256112
  // NOTE this could have a big impact on convergence speed: https://www.hindawi.com/journals/cin/2021/6628889/
  public void randomVelocityInitialization(double randomMin, double randomMax) {
    for (int i = 0; i < this.getRows(); i++) {
      for (int j = 0; j < this.getColumns(); j++) {
        this.mtx[i][j] = Utilities.getRandomDouble(randomMin, randomMax);
      }
    }
  }

  public int getIndexOfFirstNonZeroColumnForRow(int row) {
    for (int col = 0; col < this.getColumns(); col++) {
      if (this.mtx[row][col] == 1) {
        return col;
      }
    }
    return -1;
  }

  public Matrix copy() {
    Matrix mtxCopy = new Matrix(this.rows, this.columns);
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.columns; j++) {
        mtxCopy.setComponent(i, j, this.mtx[i][j]);
      }
    }

    return mtxCopy;
  }

  private Matrix operate(Matrix otherPosition, BinaryOperation binaryOperation) {
    if (otherPosition.mtx.length == this.mtx.length && 
        otherPosition.mtx.length > 0 && 
        otherPosition.mtx.length == this.mtx.length) {

      Matrix mtxCopy = this.copy();

      for (int i = 0; i < this.getRows(); i++) {
        for (int j = 0; j < this.getColumns(); j++) {
          mtxCopy.setComponent(i, j, binaryOperation.operate(this.mtx[i][j], otherPosition.mtx[i][j]));
        }
      }

      return mtxCopy;
    }

    return null;
  }

  public Matrix add(Matrix otherPosition) {
    return this.operate(otherPosition, Matrix.addition);
  }

  public Matrix subtract(Matrix otherPosition) {
    return this.operate(otherPosition, Matrix.subtraction);
  }

  public Matrix multiply(Matrix otherPosition) {
    return this.operate(otherPosition, Matrix.multilpication);
  }

  public Matrix division(Matrix otherPosition) {
    return this.operate(otherPosition, Matrix.division);
  }

  private Matrix operate(double constant, BinaryOperation binaryOperation) {
    Matrix mtxCopy = this.copy();

    for (int i = 0; i < this.getRows(); i++) {
      for (int j = 0; j < this.getColumns(); j++) {
        mtxCopy.setComponent(i, j, binaryOperation.operate(this.mtx[i][j], constant));
      }
    }

    return mtxCopy;
  }

  public Matrix add(double constant) {
    return this.operate(constant, Matrix.addition);
  }

  public Matrix subtract(double constant) {
    return this.operate(constant, Matrix.subtraction);
  }

  public Matrix multiply(double constant) {
    return this.operate(constant, Matrix.multilpication);
  }

  public Matrix division(double constant) {
    return this.operate(constant, Matrix.division);
  }

  public int getRows() {
    return this.rows;
  }

  public int getColumns() {
    return this.columns;
  }
}
