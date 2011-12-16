/*
 * Copyright 2011 Michael Bieri
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package yaphyre.geometry;

import static com.google.common.base.Preconditions.checkArgument;
import static yaphyre.geometry.MathUtils.equalsWithTolerance;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;

import org.apache.commons.math.linear.InvalidMatrixException;
import org.apache.commons.math.linear.LUDecomposition;
import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;

/**
 * Rudimentary implementation of some essential matrix operations.
 *
 * @version $Revision$
 *
 * @author Michael Bieri
 * @author $LastChangedBy$
 */
public class Matrix implements Serializable {

  private static final long serialVersionUID = 1125712454842709925L;

  private static final int DIMENSION = 4;

  public static final Matrix IDENTITY = new Matrix(MatrixUtils.createRealIdentityMatrix(DIMENSION).getData());

  private static final String TO_STRING_ROW = "[{0,number,0.###}, {1,number,0.###}, {2,number,0.###}, {3,number,0.###}]";

  private static final String TO_STRING = "[{0}, {1}, {2}, {3}]";

  private double determinant = Double.NaN;

  private boolean invertible = true;

  private Matrix inverse = null;

  private Matrix transposed = null;

  protected final double[][] m;

  public Matrix(double[][] values) {
    checkArgument(DIMENSION == values.length);
    for (double[] row : values) {
      checkArgument(DIMENSION == row.length);
    }
    this.m = values;
  }

  public Matrix(double... values) {
    checkArgument(DIMENSION * DIMENSION == values.length);
    this.m = new double[DIMENSION][DIMENSION];
    for (int row = 0; row < DIMENSION; row++) {
      System.arraycopy(values, row * DIMENSION, this.m[row], 0, DIMENSION);
    }
  }

  @Override
  public String toString() {
    Object[] rows = new String[4];
    for (int row = 0; row < DIMENSION; row++) {
      rows[row] = MessageFormat.format(TO_STRING_ROW, this.m[row][0], this.m[row][1], this.m[row][2], this.m[row][3]);
    }
    return MessageFormat.format(TO_STRING, rows);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(this.m);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Matrix other = (Matrix)obj;
    if (this.m.length != other.m.length) {
      return false;
    }
    for (int row = 0; row < DIMENSION; row++) {
      if (this.m[row].length != other.m[row].length) {
        return false;
      }
      for (int col = 0; col < DIMENSION; col++) {
        // compare with tolerance of 1e-10...
        if (!equalsWithTolerance(this.m[row][col], other.m[row][col])) {
          return false;
        }
      }
    }
    return true;
  }

  public double get(int row, int col) {
    return this.m[row][col];
  }

  public Matrix add(Matrix other) {
    return new Matrix(this.m[0][0] + other.m[0][0], this.m[0][1] + other.m[0][1], this.m[0][2] + other.m[0][2], this.m[0][3] + other.m[0][3],
                      this.m[1][0] + other.m[1][0], this.m[1][1] + other.m[1][1], this.m[1][2] + other.m[1][2], this.m[1][3] + other.m[1][3],
                      this.m[2][0] + other.m[2][0], this.m[2][1] + other.m[2][1], this.m[2][2] + other.m[2][2], this.m[2][3] + other.m[2][3],
                      this.m[3][0] + other.m[3][0], this.m[3][1] + other.m[3][1], this.m[3][2] + other.m[3][2], this.m[3][3] + other.m[3][3]);
  }

  public Matrix mul(double s) {
    return new Matrix(this.m[0][0] * s, this.m[0][1] * s, this.m[0][2] * s, this.m[0][3] * s,
                      this.m[1][0] * s, this.m[1][1] * s, this.m[1][2] * s, this.m[1][3] * s,
                      this.m[2][0] * s, this.m[2][1] * s, this.m[2][2] * s, this.m[2][3] * s,
                      this.m[3][0] * s, this.m[3][1] * s, this.m[3][2] * s, this.m[3][3] * s);
  }

  public Matrix mul(Matrix M) {
    if (M.equals(IDENTITY)) {
      return this;
    }
    RealMatrix rmThis = MatrixUtils.createRealMatrix(this.m);
    RealMatrix rmOther = MatrixUtils.createRealMatrix(M.m);
    RealMatrix rmResult = rmThis.multiply(rmOther);
    return new Matrix(rmResult.getData());
  }

  double[] mul(double[] vect) {
    RealMatrix columnMatrix = MatrixUtils.createColumnRealMatrix(vect);
    RealMatrix rm = MatrixUtils.createRealMatrix(this.m);
    return rm.multiply(columnMatrix).getColumn(0);
  }

  public Matrix transpose() {
    if (this.transposed == null) {
      this.transposed = new Matrix(this.m[0][0], this.m[1][0], this.m[2][0], this.m[3][0],
                                   this.m[0][1], this.m[1][1], this.m[2][1], this.m[3][1],
                                   this.m[0][2], this.m[1][2], this.m[2][2], this.m[3][2],
                                   this.m[0][3], this.m[1][3], this.m[2][3], this.m[3][3]);
    }
    return this.transposed;
  }

  public double getDeterminat() {
    if (this.inverse == null && this.invertible) {
      calculateInternals();
    }
    return this.determinant;
  }

  public Matrix inverse() {
    if (this.inverse == null && this.invertible) {
      calculateInternals();
    }
    return this.inverse;
  }

  public boolean isInvertible() {
    if (this.inverse == null && this.invertible) {
      calculateInternals();
    }
    return this.invertible;
  }

  private void calculateInternals() {
    try {
      RealMatrix rm = MatrixUtils.createRealMatrix(this.m);
      LUDecomposition decomp = new LUDecompositionImpl(rm);
      this.determinant = decomp.getDeterminant();
      this.inverse = new Matrix(decomp.getSolver().getInverse().getData());
      this.invertible = true;
    } catch (InvalidMatrixException e) {
      this.inverse = null;
      this.invertible = false;
    }
  }

}