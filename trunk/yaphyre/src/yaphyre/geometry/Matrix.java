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

  private static final String TO_STRING = "Matrix(4x4)[{0}, {1}, {2}, {3}]";

  private final double[][] matrixValues;

  public static final Matrix IDENTITY = new Matrix(new double[][] { {1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}});

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
    return Matrix.getColumnVector(0, multipliedVector).asVector();
  }

  private double[][] matrixFromVector(Vector vector) {
    double[][] result = initMatrix(4, 1);
    Vector4 columnVector = new Vector4(vector);
    Matrix.setColumnVector(0, result, columnVector);
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
        for (int index = 0; index < m2.length; index++) {
          product += m1[m1Row][index] * m2[index][m2Column];
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

  private static Vector4 getRowVector(int row, double[][] matrixValues) {
    return new Vector4(matrixValues[row][0], matrixValues[row][1], matrixValues[row][2], matrixValues[row][3]);
  }

  private static Vector4 getColumnVector(int column, double[][] matrixValues) {
    return new Vector4(matrixValues[0][column], matrixValues[1][column], matrixValues[2][column], matrixValues[3][column]);
  }

  private static void setRowVector(int row, double[][] matrixValues, Vector4 rowVector) {
    matrixValues[row][0] = rowVector.getX();
    matrixValues[row][1] = rowVector.getY();
    matrixValues[row][2] = rowVector.getZ();
    matrixValues[row][3] = rowVector.getW();
  }

  private static void setColumnVector(int column, double[][] matrixValues, Vector4 columnVector) {
    matrixValues[0][column] = columnVector.getX();
    matrixValues[1][column] = columnVector.getY();
    matrixValues[2][column] = columnVector.getZ();
    matrixValues[3][column] = columnVector.getW();
  }

  private static class Vector4 {

    private final double x, y, z, w;

    public Vector4(double x, double y, double z, double w) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.w = w;
    }

    public Vector4(Vector v) {
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
