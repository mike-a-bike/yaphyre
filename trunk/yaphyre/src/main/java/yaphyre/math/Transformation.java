/*
 * Copyright 2013 Michael Bieri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yaphyre.math;

import com.google.common.base.Objects;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.tan;
import static java.lang.Math.toRadians;
import static yaphyre.math.MathUtils.div;

/**
 * This class encapsulates a series of transformations. This class also provides factory methods for the most used
 * transformations like translation, rotation around various axes, scaling and the rather special transformation for
 * 'look-at', orthographic mapping and perspective mappings.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 208 $
 */
public class Transformation implements Serializable {

	private static final long serialVersionUID = 2965313538211290303L;

	public static final Transformation IDENTITY = new Transformation(Matrix.IDENTITY);

	private final Matrix matrix;
	private final Matrix matrixInv;

	private Transformation inverse;
	private Transformation transposed;

	/**
	 * Create a new instance of an identity transformation. Which means all objects transformed by this will not change.
	 */
	public Transformation() {
		this(Matrix.IDENTITY, Matrix.IDENTITY);
	}

	/**
	 * Create a new transformation instance based on the given transformation matrix. The inverse of the matrix is
	 * pre-calculated on instantiation to optimize its later use.
	 *
	 * @param matrix The {@link yaphyre.math.Matrix} this transformation is based upon.
	 */
	public Transformation(Matrix matrix) {
		this(matrix, matrix.inverse());
	}

	/**
	 * Create a new transformation instance which uses the given matrix for transformation and the inverse matrix for the
	 * inverse transformation. Use this when calculating the inverse of a special matrix can be done more efficiently than
	 * solving the inverse equation. For example: The scaling and translation matrices are very simple to inverse.
	 *
	 * @param matrix  The {@link yaphyre.math.Matrix} this transformation uses.
	 * @param inverse The {@link yaphyre.math.Matrix} used for its inverse.
	 */
	public Transformation(Matrix matrix, Matrix inverse) {
		this.matrix = matrix;
		matrixInv = inverse;
		this.inverse = null;
		transposed = null;
	}

	/**
	 * Factory method for creating a {@link yaphyre.math.Transformation} for the given translation.
	 *
	 * @param x The x component of the translation
	 * @param y The y component of the translation
	 * @param z The z component of the translation
	 *
	 * @return A {@link yaphyre.math.Transformation} with the translation matrix and its inverse.
	 */
	public static Transformation translate(double x, double y, double z) {
		Matrix trans = new Matrix(new double[][]{{1, 0, 0, x}, {0, 1, 0, y}, {0, 0, 1, z}, {0, 0, 0, 1}});
		Matrix transInv = new Matrix(
				new double[][]{{1, 0, 0, -x}, {0, 1, 0, -y}, {0, 0, 1, -z}, {0, 0, 0, 1}});
		return new Transformation(trans, transInv);
	}

	/**
	 * Factory method for creating a scaling {@link yaphyre.math.Transformation}. The scaling factors for each direction can be
	 * different.
	 *
	 * @param sx The amount of scaling along the x axis.
	 * @param sy The amount of scaling along the y axis.
	 * @param sz The amount of scaling along the z axis.
	 *
	 * @return A {@link yaphyre.math.Transformation} with the scaling matrix and its inverse.
	 */
	public static Transformation scale(double sx, double sy, double sz) {
		checkArgument(!(sx == 0d && sy == 0d && sz == 0d));
		Matrix matrix = new Matrix(
				new double[][]{{sx, 0, 0, 0}, {0, sy, 0, 0}, {0, 0, sz, 0}, {0, 0, 0, 1}});
		Matrix inv = new Matrix(
				new double[][]{{1d / sx, 0, 0, 0}, {0, 1d / sy, 0, 0}, {0, 0, 1d / sz, 0}, {0, 0, 0, 1}});
		return new Transformation(matrix, inv);
	}

	/**
	 * Factory for the rotation around the x axis.
	 *
	 * @param angle The angle (in degree) to rotate around the x axis.
	 *
	 * @return A {@link yaphyre.math.Transformation} with the rotation matrix.
	 */
	public static Transformation rotateX(double angle) {
		if (MathUtils.equalsWithTolerance(0d, angle)) {
			return Transformation.IDENTITY;
		}
		double radAngle = toRadians(angle);
		Matrix matrix = new Matrix(new double[][]{{1, 0, 0, 0}, {0, cos(radAngle), -sin(radAngle), 0}, {0, sin(
				radAngle), cos(radAngle), 0}, {0, 0, 0, 1}});
		return new Transformation(matrix, matrix.transpose());
	}

	/**
	 * Factory for the rotation around the y axis.
	 *
	 * @param angle The angle (in degree) to rotate around the y axis.
	 *
	 * @return A {@link yaphyre.math.Transformation} with the rotation matrix.
	 */
	public static Transformation rotateY(double angle) {
		if (MathUtils.equalsWithTolerance(0d, angle)) {
			return Transformation.IDENTITY;
		}
		double radAngle = toRadians(angle);
		Matrix matrix = new Matrix(new double[][]{{cos(radAngle), 0, sin(radAngle), 0}, {0, 1, 0, 0}, {-sin(
				radAngle), 0, cos(radAngle), 0}, {0, 0, 0, 1}});
		return new Transformation(matrix, matrix.transpose());
	}

	/**
	 * Factory for the rotation around the z axis.
	 *
	 * @param angle The angle (in degree) to rotate around the z axis.
	 *
	 * @return A {@link yaphyre.math.Transformation} with the rotation matrix.
	 */
	public static Transformation rotateZ(double angle) {
		if (MathUtils.equalsWithTolerance(0d, angle)) {
			return Transformation.IDENTITY;
		}
		double radAngle = toRadians(angle);
		Matrix matrix = new Matrix(new double[][]{{cos(radAngle), -sin(radAngle), 0, 0}, {sin(radAngle), cos(
				radAngle), 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}});
		return new Transformation(matrix, matrix.transpose());
	}

	/**
	 * {@link yaphyre.math.Transformation} which rotates a certain angle around an arbitrary axis defined by the given {@link
	 * yaphyre.math.Vector3D}.
	 *
	 * @param angle The angle (in degrees) to rotate.
	 * @param axis  The axis to rotate around.
	 *
	 * @return A {@link yaphyre.math.Transformation} with the rotation matrix.
	 */
	public static Transformation rotate(double angle, Vector3D axis) {
		if (MathUtils.equalsWithTolerance(0d, angle)) {
			return Transformation.IDENTITY;
		}
		Vector3D a = axis.normalize();
		double s = sin(toRadians(angle));
		double c = cos(toRadians(angle));
		double[][] m = new double[4][4];

		m[0][0] = a.x * a.x + (1d - a.x * a.x) * c;
		m[0][1] = a.x * a.y * (1d - c) - a.z * s;
		m[0][2] = a.x * a.z * (1d - c) + a.y * s;
		m[0][3] = 0;

		m[1][0] = a.y * a.x * (1d - c) + a.z * s;
		m[1][1] = a.y * a.y + (1d - a.y * a.y) * c;
		m[1][2] = a.y * a.z * (1d - c) - a.x * s;
		m[1][3] = 0;

		m[2][0] = a.z * a.x * (1d - c) - a.y * s;
		m[2][1] = a.z * a.y * (1d - c) + a.x * s;
		m[2][2] = a.z * a.z + (1d - a.z * a.z) * c;
		m[2][3] = 0;

		m[3][0] = 0;
		m[3][1] = 0;
		m[3][2] = 0;
		m[3][3] = 1;

		Matrix matrix = new Matrix(m);
		return new Transformation(matrix, matrix.transpose());
	}

	/**
	 * 'Look at' {@link yaphyre.math.Transformation}. This describes the {@link yaphyre.math.Transformation} necessary to map coordinates when a
	 * location is given and a point to 'look at'. In order to define the correct {@link yaphyre.math.Transformation}, an 'up' {@link
	 * yaphyre.math.Vector3D} is needed based on which the new coordinate system is built. The up vector is recalculated during the
	 * transformation.
	 *
	 * @param eye    The location of the eye ({@link yaphyre.math.Point3D})
	 * @param lookAt The point to look at ({@link yaphyre.math.Point3D})
	 * @param up     An imaginary up vector to make rotations of the coordinate system possible ({@link yaphyre.math.Vector3D})
	 *
	 * @return A {@link yaphyre.math.Transformation} which aligns the given coordinates with the aligned coordinate system.
	 *
	 * @see <a href="http://cs.fit.edu/~wds/classes/cse5255/thesis/viewTrans/viewTrans.html">View Transformation</a>
	 */
	public static Transformation lookAt(Point3D eye, Point3D lookAt, Vector3D up) {
		Vector3D dir = lookAt.sub(eye).normalize();
		Vector3D right = up.cross(dir).normalize();
		Vector3D newUp = dir.cross(right);

		Matrix camToWorld = new Matrix(
				new double[][]{{right.x, newUp.x, dir.x, eye.x}, {right.y, newUp.y, dir.y, eye.y},
						{right.z, newUp.z, dir.z, eye.z}, {0, 0, 0, 1}});

		return new Transformation(camToWorld.inverse(), camToWorld);
	}

	/**
	 * An orthographic projection matrix.
	 *
	 * @param znear The near clipping distance.
	 * @param zfar  The far clipping distance.
	 *
	 * @return A {@link yaphyre.math.Transformation} containing the view transformation.
	 */
	public static Transformation orthographic(double znear, double zfar) {
		Transformation scale = scale(1d, 1d, 1d / (zfar - znear));
		Transformation translation = translate(0d, 0d, -znear);
		return scale.mul(translation);
	}

	/**
	 * The transformation matrix for perspective projection onto the view plane.
	 *
	 * @param fov  The field of view. The angle between the top and bottom plane of the view frustum.
	 * @param near The new clipping distance.
	 * @param far  The far clipping distance.
	 *
	 * @return A transformation matrix to transform between camera and world coordinates.
	 */
	public static Transformation perspective(double fov, double near, double far) {
		Matrix persp = new Matrix(new double[][]{{1, 0, 0, 0}, {0, 1, 0, 0},
				{0, 0, far / (far - near), -far * near / (far - near)}, {0, 0, 1, 0}});
		double invTanAng = 1d / tan(toRadians(fov) / 2d);
		Transformation scale = scale(invTanAng, invTanAng, 1);
		return scale.mul(new Transformation(persp));
	}

	/**
	 * Transform the screen (raster-) coordinates with x &isin; [0, <code>xResolution</code> ] and y &isin; [0,
	 * <code>yResolution</code>] onto the unit square where u &isin; [0, 1] and v &isin; [0, 1].
	 *
	 * @param xResolution The max x coordinate.
	 * @param yResolution The max y coordinate.
	 *
	 * @return A {@link yaphyre.math.Transformation} which maps the raster coordinates onto a unit square.
	 */
	public static Transformation rasterToUnitSquare(int xResolution, int yResolution) {
		return scale(xResolution, yResolution, 1d).inverse();
	}

	/**
	 * Simple implementation representing the main transformation matrix.
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(getClass()).add("matrix", matrix).toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((matrix == null) ? 0 : matrix.hashCode());
		result = prime * result + ((matrixInv == null) ? 0 : matrixInv.hashCode());
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
		if (!(obj instanceof Transformation)) {
			return false;
		}
		Transformation other = (Transformation) obj;
		if (!matrix.equals(other.matrix)) {
			return false;
		}
		if (!matrixInv.equals(other.matrixInv)) {
			return false;
		}
		return true;
	}

	/**
	 * Calculate the inverse {@link yaphyre.math.Transformation} for this {@link yaphyre.math.Transformation}.
	 *
	 * @return A new instance representing the inverse operation.
	 */
	public Transformation inverse() {
		if (inverse == null) {
			inverse = new Transformation(matrixInv, matrix);
		}
		return inverse;
	}

	/**
	 * Calculate the transposed {@link yaphyre.math.Transformation} for this instance.
	 *
	 * @return A new instance representing the transposed operation.
	 */
	public Transformation transpose() {
		if (transposed == null) {
			transposed = new Transformation(matrix.transpose(), matrixInv.transpose());
		}
		return transposed;
	}

	/**
	 * Multiply two Transformations. Use this to 'chain' multiple {@link yaphyre.math.Transformation}s into one single operation.
	 *
	 * @param trans The instance to multiply this instance with.
	 *
	 * @return A new {@link yaphyre.math.Transformation} containing both operations within one instance.
	 */
	public Transformation mul(Transformation trans) {
		Matrix matrix = this.matrix.mul(trans.matrix);
		return new Transformation(matrix);
	}

	/**
	 * Transform a {@link yaphyre.math.Point3D} using the operation stored within this instance. Points are scaled, rotated and
	 * translated.
	 *
	 * @param p The {@link yaphyre.math.Point3D} to transform.
	 *
	 * @return The transformed instance.
	 */
	public Point3D transform(Point3D p) {
		double[] homogenousPoint = new double[]{p.x, p.y, p.z, 1};
		double[] result = matrix.mul(homogenousPoint);
		return new Point3D(div(result[0], result[3]), div(result[1], result[3]), div(result[2], result[3]));
	}

	/**
	 * Transform a {@link yaphyre.math.Point2D} using the operation within this transformation instance. Points are scaled, rotated and
	 * translated.
	 *
	 * @param p The {@link yaphyre.math.Point2D} to transform.
	 *
	 * @return A transformed {@link yaphyre.math.Point2D} instance.
	 */
	public Point2D transform(Point2D p) {
		double[] homogeneousPoint = new double[]{p.u, p.v, 0, 1};
		double[] result = matrix.mul(homogeneousPoint);
		return new Point2D(div(result[0], result[3]), div(result[1], result[3]));
	}

	/**
	 * Transform a {@link yaphyre.math.Vector3D} using the operation of this transformation instance. Vectors are scaled and rotated.
	 * Since a mathematical vector has no defined origin, a vector is not translated.
	 *
	 * @param v The {@link yaphyre.math.Vector3D} to transform.
	 *
	 * @return A new, transformed {@link yaphyre.math.Vector3D} instance.
	 */
	public Vector3D transform(Vector3D v) {
		double[] homogeneousVector = new double[]{v.x, v.y, v.z, 0};
		double[] result = matrix.mul(homogeneousVector);
		return new Vector3D(result[0], result[1], result[2]);
	}

	/**
	 * Transforms a {@link yaphyre.math.Normal3D}. Normals are very special. In order to maintain their properties (like being
	 * perpendicular to a surface at a given point) their transformation is somewhat complicated.<br/> Like vectors, they
	 * are not translated, but we use the transposed matrix of the inverse of the transformation matrix.<br/> See the
	 * first article below. It actually makes sense ;-)
	 *
	 * @param n The {@link yaphyre.math.Normal3D} to transform.
	 *
	 * @return The transformed {@link yaphyre.math.Normal3D} instance.
	 *
	 * @see <a href="http://www.unknownroad.com/rtfm/graphics/rt_normals.html">Transforming Normals</a>
	 * @see <a href="http://tog.acm.org/resources/RTNews/html/rtnews1a.html#art4">Abnormal Normals</a>
	 */
	public Normal3D transform(Normal3D n) {
		double[] homogeneousNormal = new double[]{n.x, n.y, n.z, 0};
		double[] result = matrixInv.transpose().mul(homogeneousNormal);
		return new Normal3D(result[0], result[1], result[2]);
	}

	/**
	 * This transforms a {@link yaphyre.math.Ray} instance. To do this, the point of origin for the ray ({@link yaphyre.math.Ray#getOrigin()}) is
	 * transformed as a {@link yaphyre.math.Point3D} and its direction ({@link yaphyre.math.Ray#getDirection()}) is transformed as a vector. Please
	 * notice, the length of the direction vector is not necessarily one after the transformation.
	 *
	 * @param r The {@link yaphyre.math.Ray} to transform.
	 *
	 * @return The transformed ray.
	 */
	public Ray transform(Ray r) {
		Point3D newOrigin = transform(r.getOrigin());
		Vector3D newDirection = transform(r.getDirection());
		return new Ray(newOrigin, newDirection, r.getMint(), r.getMaxt());
	}

	/**
	 * Transformation of a {@link BoundingBox}. The minimum and maximum points are taken, transformed and a
	 * new instance is created from these information.
	 * <p/>
	 * TODO: Cover with test to make sure that this works
	 *
	 * @param boundingBox The {@link BoundingBox} to transform
	 *
	 * @return A new {@link BoundingBox} instance from the transformed points.
	 */
	public BoundingBox transform(BoundingBox boundingBox) {
		Point3D p1 = transform(boundingBox.getPointMin());
		Point3D p2 = transform(boundingBox.getPointMax());
		return new BoundingBox(p1, p2);
	}

	public Matrix getMatrix() {
		return matrix;
	}

	public Matrix getInverseMatrix() {
		return matrixInv;
	}
}
