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
package yaphyre.geometry;

import static com.google.common.base.Preconditions.checkArgument;
import static yaphyre.geometry.MathUtils.equalsWithTolerance;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.math.linear.InvalidMatrixException;
import org.apache.commons.math.linear.LUDecomposition;
import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;

/**
 * Rudimentary implementation of some essential matrix operations.
 *
 * @author Michael Bieri
 * @author $LastChangedBy$
 * @version $Revision$
 */
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
		return Arrays.deepToString(this.m);
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
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Matrix other = (Matrix) obj;
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
		Matrix result = new Matrix();
		for (int row = 0; row < DIMENSION; row++) {
			for (int col = 0; col < DIMENSION; col++) {
				result.m[row][col] = this.m[row][col] + other.m[row][col];
			}
		}
		return result;
	}

	public Matrix mul(double s) {
		Matrix result = new Matrix();
		for (int row = 0; row < DIMENSION; row++) {
			for (int col = 0; col < DIMENSION; col++) {
				result.m[row][col] = this.m[row][col] * s;
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
				result.m[i][j] = this.m[i][0] * M.m[0][j] + this.m[i][1] * M.m[1][j] + this.m[i][2] * M.m[2][j]
						+ this.m[i][3] * M.m[3][j];
			}
		}

		return result;
	}

	double[] mul(double[] vect) {
		double[] result = new double[DIMENSION];

		result[0] = this.m[0][0] * vect[0] + this.m[0][1] * vect[1] + this.m[0][2] * vect[2] + this.m[0][3] * vect[3];
		result[1] = this.m[1][0] * vect[0] + this.m[1][1] * vect[1] + this.m[1][2] * vect[2] + this.m[1][3] * vect[3];
		result[2] = this.m[2][0] * vect[0] + this.m[2][1] * vect[1] + this.m[2][2] * vect[2] + this.m[2][3] * vect[3];
		result[3] = this.m[3][0] * vect[0] + this.m[3][1] * vect[1] + this.m[3][2] * vect[2] + this.m[3][3] * vect[3];

		return result;
	}

	public Matrix transpose() {
		if (this.transposed == null) {
			this.transposed = new Matrix();
			for (int row = 0; row < DIMENSION; row++) {
				for (int col = 0; col < DIMENSION; col++) {
					this.transposed.m[row][col] = this.m[col][row];
				}
			}
		}
		return this.transposed;
	}

	public double getDeterminat() {
		if (this.inverse == null && this.invertible) {
			this.calculateInternals();
		}
		return this.determinant;
	}

	public Matrix inverse() {
		if (this.inverse == null && this.invertible) {
			this.calculateInternals();
		}
		return this.inverse;
	}

	public boolean isInvertible() {
		if (this.inverse == null && this.invertible) {
			this.calculateInternals();
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