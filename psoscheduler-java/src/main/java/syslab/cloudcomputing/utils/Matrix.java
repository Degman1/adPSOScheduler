package syslab.cloudcomputing.utils;

import java.util.Arrays;

public class Matrix {
  private static BinaryOperation addition = (x, y) -> x + y;
  private static BinaryOperation subtraction = (x, y) -> x - y;
  private static BinaryOperation multilpication = (x, y) -> x * y;

  private double[][] mtx;
  private int rows;
  private int columns;

  public Matrix(int rows, int columns) {
    this.rows = rows;
    this.columns = columns;
    this.mtx = new double[rows][columns];
  }

  // This init is used to randomly populate the matrix values to act as particle positions
  public void randomPositionInitialization() {
    for (int i = 0; i < this.mtx.length; i++) {
      int j = Utilities.getRandomInteger(0, this.getColumnCount());
      this.mtx[i][j] = 1;
    }
  }

  public void setComponent(int row, int column, double value) {
    this.mtx[row][column] = value;
  }

  // This init is used to randomly populate the matrix values to act as particle velocities
  // Init velocities to zero: https://ieeexplore.ieee.org/document/6256112
  // NOTE this could have a big impact on convergence speed: https://www.hindawi.com/journals/cin/2021/6628889/
  public void randomVelocityInitialization(double randomMin, double randomMax) {
    for (int i = 0; i < this.getRowsCount(); i++) {
      for (int j = 0; j < this.getColumnCount(); j++) {
        this.mtx[i][j] = Utilities.getRandomDouble(randomMin, randomMax);
      }
    }
  }

  public int getIndexOfFirstNonZeroColumnForRow(int row) {
    int col;
    for (col = 0; col < this.getColumnCount(); col++) {
      if (this.mtx[row][col] == 1) {
        return col;
      }
    }
    System.out.println("ERROR: No value of 1 was found in column " + col + " of matrix -> task not assigned to any VM in previous iteration");
    return -1;
  }

  public int getIndexOfMaximumColumnForRow(int row) {
    int maxIndex = 0;
    double maxValue = this.mtx[row][0];

    for (int col = 1; col < this.getColumnCount(); col++) {
      if (this.mtx[row][col] > maxValue) {
        maxIndex = col;
        maxValue = this.mtx[row][col];
      }
    }

    return maxIndex;
  }

  public void zeroOut() {
    for (int i = 0; i < this.getRowsCount(); i++) {
      Arrays.fill(this.mtx[i], 0.0);
    }
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
      for (int i = 0; i < this.getRowsCount(); i++) {
        for (int j = 0; j < this.getColumnCount(); j++) {
          this.setComponent(i, j, binaryOperation.operate(this.mtx[i][j], otherPosition.mtx[i][j]));
        }
      }
      return this;
    }

    return null;
  }

  public Matrix enforceElementwiseBound(double maxAbsoluteValue) {
    for (int i = 0; i < this.getRowsCount(); i++) {
      for (int j = 0; j < this.getColumnCount(); j++) {
        if (Math.abs(this.mtx[i][j]) > maxAbsoluteValue || Math.abs(this.mtx[i][j]) < 0) {
          this.mtx[i][j] = Utilities.getRandomDouble(0, maxAbsoluteValue);
        }
      }
    }

    return this;
  }

  public Matrix add(Matrix otherPosition) {
    return this.operate(otherPosition, Matrix.addition);
  }

  public Matrix subtract(Matrix otherPosition) {
    return this.operate(otherPosition, Matrix.subtraction);
  }

  private Matrix operate(double constant, BinaryOperation binaryOperation) {
    for (int i = 0; i < this.getRowsCount(); i++) {
      for (int j = 0; j < this.getColumnCount(); j++) {
        this.setComponent(i, j, binaryOperation.operate(this.mtx[i][j], constant));
      }
    }
    return this;
  }

  public Matrix multiply(double constant) {
    return this.operate(constant, Matrix.multilpication);
  }

  public Matrix addJ(double constant) {
    return this.operate(constant, Matrix.addition);
  }

  public int getRowsCount() {
    return this.rows;
  }

  public int getColumnCount() {
    return this.columns;
  }

  @Override
  public String toString() {
    String desc = "[";
    for (int i = 0; i < this.mtx.length; i++) {
      if (i > 0) {
        desc += " ";
      }
      desc += Arrays.toString(this.mtx[i]);
      if (i < this.mtx.length - 1) {
        desc += "\n";
      }
    }
    return desc + "]";
  }
}
