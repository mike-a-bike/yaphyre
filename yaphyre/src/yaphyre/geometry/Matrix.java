package yaphyre.geometry;

import java.text.MessageFormat;

/**
 * In the future to come this will be a simple implementation of an algebraic
 * matrix. Used to transform vectors in the 3d space. In order to do this, it
 * adds another dimension (4x4 matrices) and converts the used {@link Vector}s
 * where used into suitable representations with four elements.
 * 
 * @author Michael Bieri
 */
public class Matrix {

  private static final int DIMENSION = 4;

  private static final String TO_STRING_ROW = "[{0,number,0.###}, {1,number,0.###}, {2,number,0.###}, {3,number,0.###}]";

  private static final String TO_STRING = "[{0}, {1}, {2}, {3}]";

  private final double[][] matrixValues;

  public Matrix(double[][] values) {
    this.matrixValues = initMatrix(DIMENSION, DIMENSION);
    System.arraycopy(values[0], 0, this.matrixValues[0], 0, DIMENSION);
    System.arraycopy(values[1], 0, this.matrixValues[1], 0, DIMENSION);
    System.arraycopy(values[2], 0, this.matrixValues[2], 0, DIMENSION);
    System.arraycopy(values[3], 0, this.matrixValues[3], 0, DIMENSION);
  }

  public Matrix(double... values) {
    int size = DIMENSION * DIMENSION;
    if (values == null || values.length != size) {
      throw new IllegalArgumentException("the number of values must be " + size);
    }
    this.matrixValues = initMatrix(DIMENSION, DIMENSION);
    assignValues(DIMENSION, values);
  }

  private double[][] initMatrix(int rows, int columns) {
    double[][] result = new double[rows][];
    for (int i = 0; i < rows; i++) {
      result[i] = new double[columns];
    }
    return result;
  }

  private void assignValues(int dimension, double... values) {
    for (int row = 0; row < dimension; row++) {
      System.arraycopy(values, row * dimension, this.matrixValues[row], 0, dimension);
    }
  }

  public double get(int row, int column) {
    if (row < 0 || column < 0 || row >= DIMENSION || column >= DIMENSION) {
      throw new IllegalArgumentException(row + "/" + column + " is not an allowed row/column pair for this matrix");
    }
    return this.matrixValues[row][column];
  }

  public Matrix add(Matrix other) {
    return performMatrixOperation(other, Operations.Add);
  }

  public Matrix sub(Matrix other) {
    return performMatrixOperation(other, Operations.Sub);
  }

  public Matrix mul(double scalar) {
    return performScalarOperation(scalar, Operations.Mul);
  }

  public Matrix mul(Matrix other) {
    double[][] result = this.mul(this.matrixValues, other.matrixValues);
    return new Matrix(result);
  }

  public Vector mul(Vector vector) {
    double[][] vectorMatrix = matrixFromVector(vector);
    double[][] multipliedVector = this.mul(this.matrixValues, vectorMatrix);
    return vectorFromMatrix(multipliedVector);
  }

  private double[][] matrixFromVector(Vector vector) {
    double[][] result = initMatrix(4, 1);
    result[0][0] = vector.getX();
    result[1][0] = vector.getY();
    result[2][0] = vector.getZ();
    result[3][0] = 1d;
    return result;
  }

  private Vector vectorFromMatrix(double[][] multipliedVector) {
    Vector result;
    if (multipliedVector[0].length < multipliedVector.length) {
      result = new Vector(multipliedVector[0][0], multipliedVector[1][0], multipliedVector[2][0]).scale(multipliedVector[3][0]);
    } else {
      result = new Vector(multipliedVector[0][0], multipliedVector[0][1], multipliedVector[0][2]).scale(multipliedVector[0][3]);
    }
    return result;
  }

  private double[][] mul(double[][] m1, double[][] m2) {
    if (m1[0].length != m2.length) {
      throw new IllegalArgumentException("the two matrices are not comfortable with each other");
    }
    int resultRows = m1.length;
    int resultColumns = m2[0].length;
    double[][] result = initMatrix(resultRows, resultColumns);

    for (int m1Row = 0; m1Row < resultRows; m1Row++) {
      for (int m2Column = 0; m2Column < resultColumns; m2Column++) {
        double product = 0d;
        for (int m1Column = 0; m1Column < m1[0].length; m1Column++) {
          for (int m2Row = 0; m2Row < m2.length; m2Row++) {
            product += m1[m1Row][m1Column] * m2[m2Row][m2Column];
          }
        }
        result[m1Row][m2Column] = product;
      }
    }

    return result;
  }

  public Matrix div(double scalar) {
    return performScalarOperation(scalar, Operations.Div);
  }

  private Matrix performScalarOperation(double scalar, Operations operation) {
    double[] values = new double[DIMENSION * DIMENSION];
    for (int row = 0; row < DIMENSION; row++) {
      for (int column = 0; column < DIMENSION; column++) {
        values[row * DIMENSION + column] = operation.perform(get(row, column), scalar);
      }
    }
    return new Matrix(values);
  }

  private Matrix performMatrixOperation(Matrix other, Operations operation) {
    double[] values = new double[DIMENSION * DIMENSION];
    for (int row = 0; row < DIMENSION; row++) {
      for (int column = 0; column < DIMENSION; column++) {
        values[row * DIMENSION + column] = operation.perform(get(row, column), other.get(row, column));
      }
    }
    return new Matrix(values);
  }

  private static class Vector4D {

    private final double x, y, z, w;

    public Vector4D(double x, double y, double z, double w) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.w = w;
    }

    public Vector4D(Vector v) {
      this(v.getX(), v.getY(), v.getZ(), 1);
    }

    public Vector asVector() {
      return new Vector(this.x, this.y, this.z).scale(1 / this.w);
    }

    public double getX() {
      return this.x;
    }

    public double getY() {
      return this.y;
    }

    public double getZ() {
      return this.z;
    }

    public double getW() {
      return this.w;
    }
  }

  private enum Operations {
    Add {
      @Override
      public double perform(double v1, double v2) {
        return v1 + v2;
      }
    },
    Sub {
      @Override
      public double perform(double v1, double v2) {
        // TODO Auto-generated method stub
        return v1 - v2;
      }
    },
    Mul {
      @Override
      public double perform(double v1, double v2) {
        // TODO Auto-generated method stub
        return v1 * v2;
      }
    },
    Div {
      @Override
      public double perform(double v1, double v2) {
        // TODO Auto-generated method stub
        return v1 / v2;
      }
    };

    public abstract double perform(double v1, double v2);

  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof Matrix) {
      Matrix other = (Matrix)obj;
      for (int row = 0; row < DIMENSION; row++) {
        for (int column = 0; column < DIMENSION; column++) {
          if (get(row, column) != other.get(row, column)) {
            return false;
          }
        }
      }
      return true;
    }
    return false;
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = 7;
    for (int row = 0; row < DIMENSION; row++) {
      for (int column = 0; column < DIMENSION; column++) {
        result *= this.matrixValues[row][column];
      }
    }
    return result;
  }

  @Override
  public String toString() {
    String[] rows = new String[DIMENSION];
    for (int row = 0; row < DIMENSION; row++) {
      rows[row] = MessageFormat.format(TO_STRING_ROW, this.matrixValues[row][0], this.matrixValues[row][1], this.matrixValues[row][2], this.matrixValues[row][3]);
    }
    return MessageFormat.format(TO_STRING, (Object[])rows);
  }
}
