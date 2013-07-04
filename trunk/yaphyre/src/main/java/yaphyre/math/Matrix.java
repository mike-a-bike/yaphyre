/*
 * Copyright 2012 Michael Bieri
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
package yaphyre.math;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;

import java.io.Serializable;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static yaphyre.math.MathUtils.equalsWithTolerance;

/**
 * Rudimentary implementation of some essential matrix operations.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 208 $
 */
@SuppressWarnings("ProtectedField")
public class Matrix implements Serializable {

	private static final long serialVersionUID = 1125712454842709925L;

	private static final int DIMENSION = 4;

	public static final Matrix IDENTITY = new Matrix(MatrixUtils.createRealIdentityMatrix(DIMENSION).getData());

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
		m = values;
	}

	public Matrix(double... values) {
		checkArgument(DIMENSION * DIMENSION == values.length);
		m = new double[DIMENSION][DIMENSION];
		for (int row = 0; row < DIMENSION; row++) {
			System.arraycopy(values, row * DIMENSION, m[row], 0, DIMENSION);
		}
	}

	private Matrix() {
		m = new double[DIMENSION][DIMENSION];
	}

	@Override
	public String toString() {
		return Arrays.deepToString(m);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(m);
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
		Matrix other = (Matrix) obj;
		if (m.length != other.m.length) {
			return false;
		}
		for (int row = 0; row < DIMENSION; row++) {
			if (m[row].length != other.m[row].length) {
				return false;
			}
			for (int col = 0; col < DIMENSION; col++) {
				// compare with tolerance of 1e-10...
				if (!equalsWithTolerance(m[row][col], other.m[row][col])) {
					return false;
				}
			}
		}
		return true;
	}

	public double get(int row, int col) {
		return m[row][col];
	}

	public Matrix add(Matrix other) {
		Matrix result = new Matrix();
		for (int row = 0; row < DIMENSION; row++) {
			for (int col = 0; col < DIMENSION; col++) {
				result.m[row][col] = m[row][col] + other.m[row][col];
			}
		}
		return result;
	}

	public Matrix mul(double s) {
		Matrix result = new Matrix();
		for (int row = 0; row < DIMENSION; row++) {
			for (int col = 0; col < DIMENSION; col++) {
				result.m[row][col] = m[row][col] * s;
			}
		}
		return result;
	}

	public Matrix mul(Matrix M) {
		if (M.equals(IDENTITY)) {
			return this;
		}

		Matrix result = new Matrix();

		for (int i = 0; i < DIMENSION; i++) {
			for (int j = 0; j < DIMENSION; j++) {
				result.m[i][j] = m[i][0] * M.m[0][j] + m[i][1] * M.m[1][j] + m[i][2] * M.m[2][j]
						+ m[i][3] * M.m[3][j];
			}
		}

		return result;
	}

	double[] mul(double[] vect) {
		double[] result = new double[DIMENSION];

		result[0] = m[0][0] * vect[0] + m[0][1] * vect[1] + m[0][2] * vect[2] + m[0][3] * vect[3];
		result[1] = m[1][0] * vect[0] + m[1][1] * vect[1] + m[1][2] * vect[2] + m[1][3] * vect[3];
		result[2] = m[2][0] * vect[0] + m[2][1] * vect[1] + m[2][2] * vect[2] + m[2][3] * vect[3];
		result[3] = m[3][0] * vect[0] + m[3][1] * vect[1] + m[3][2] * vect[2] + m[3][3] * vect[3];

		return result;
	}

	public Matrix transpose() {
		if (transposed == null) {
			transposed = new Matrix();
			for (int row = 0; row < DIMENSION; row++) {
				for (int col = 0; col < DIMENSION; col++) {
					transposed.m[row][col] = m[col][row];
				}
			}
		}
		return transposed;
	}

	public double getDeterminat() {
		if (inverse == null && invertible) {
			calculateInternals();
		}
		return determinant;
	}

	public Matrix inverse() {
		if (inverse == null && invertible) {
			calculateInternals();
		}
		return inverse;
	}

	public boolean isInvertible() {
		if (inverse == null && invertible) {
			calculateInternals();
		}
		return invertible;
	}

	private void calculateInternals() {
		try {
			RealMatrix rm = MatrixUtils.createRealMatrix(m);
			LUDecomposition decomp = new LUDecomposition(rm);
			determinant = decomp.getDeterminant();
			inverse = new Matrix(decomp.getSolver().getInverse().getData());
			invertible = true;
		} catch (SingularMatrixException e) {
			inverse = null;
			invertible = false;
		}
	}

}