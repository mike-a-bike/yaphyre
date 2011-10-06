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

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import yaphyre.math.MathUtils;

/**
 * This class encapsulates a series of transformations. This class also provides
 * factory methods for the most used transformations like translation, rotation
 * around various axes, scaling and the rather special transformation for
 * 'look-at', orthographic mapping and perspective mappings.
 * 
 * @version $Revision$
 * 
 * @author Michael Bieri
 * @author $LastChangedBy$
 */
public class Transformation {

  public static final Transformation IDENTITY = new Transformation(Matrix.IDENTITY);

  private final Matrix matrix;

  private final Matrix matrixInv;

  /**
   * Factory method for creating a {@link Transformation} for the given
   * translation.
   * 
   * @param x
   *          The x component of the translation
   * @param y
   *          The y component of the translation
   * @param z
   *          The z component of the translation
   * 
   * @return A {@link Transformation} with the translation matrix and its
   *         inverse.
   */
  public static Transformation translate(double x, double y, double z) {
    Matrix trans = new Matrix(new double[][] { {1, 0, 0, x},
                                               {0, 1, 0, y},
                                               {0, 0, 1, z},
                                               {0, 0, 0, 1}});
    Matrix transInv = new Matrix(new double[][] { {1, 0, 0, -x},
                                                  {0, 1, 0, -y},
                                                  {0, 0, 1, -z},
                                                  {0, 0, 0, 1}});
    return new Transformation(trans, transInv);
  }

  /**
   * Factory method for creating a scaling {@link Transformation}. The scaling
   * factors for each direction can be different.
   * 
   * @param sx
   *          The amount of scaling along the x axis.
   * @param sy
   *          The amount of scaling along the y axis.
   * @param sz
   *          The amount of scaling along the z axis.
   * 
   * @return A {@link Transformation} with the scaling matrix and its inverse.
   */
  public static Transformation scale(double sx, double sy, double sz) {
    Matrix matrix = new Matrix(new double[][] { {sx, 0, 0, 0},
                                               {0, sy, 0, 0},
                                               {0, 0, sz, 0},
                                               {0, 0, 0, 1}});
    Matrix inv = new Matrix(new double[][] { {1d / sx, 0, 0, 0},
                                            {0, 1d / sy, 0, 0},
                                            {0, 0, 1d / sz, 0},
                                            {0, 0, 0, 1}});
    return new Transformation(matrix, inv);
  }

  /**
   * Factory for the rotation around the x axis.
   * 
   * @param angle
   *          The angle (in degree) to rotate around the x axis.
   * 
   * @return A {@link Transformation} with the rotation matrix.
   */
  public static Transformation rotateX(double angle) {
    double radAngle = MathUtils.toRad(angle);
    Matrix matrix = new Matrix(new double[][] { {1, 0, 0, 0},
                                                {0, cos(radAngle), -sin(radAngle), 0},
                                                {0, sin(radAngle), cos(radAngle), 0},
                                                {0, 0, 0, 1}});
    return new Transformation(matrix, matrix.transpose());
  }

  /**
   * Factory for the rotation around the y axis.
   * 
   * @param angle
   *          The angle (in degree) to rotate around the y axis.
   * 
   * @return A {@link Transformation} with the rotation matrix.
   */
  public static Transformation rotateY(double angle) {
    double radAngle = MathUtils.toRad(angle);
    Matrix matrix = new Matrix(new double[][] { {cos(radAngle), 0, sin(radAngle), 0},
                                                {0, 1, 0, 0},
                                                {-sin(radAngle), 0, cos(radAngle), 0},
                                                {0, 0, 0, 1}});
    return new Transformation(matrix, matrix.transpose());
  }

  /**
   * Factory for the rotation around the z axis.
   * 
   * @param angle
   *          The angle (in degree) to rotate around the z axis.
   * 
   * @return A {@link Transformation} with the rotation matrix.
   */
  public static Transformation rotateZ(double angle) {
    double radAngle = MathUtils.toRad(angle);
    Matrix matrix = new Matrix(new double[][] { {cos(radAngle), -sin(radAngle), 0, 0},
                                                {sin(radAngle), cos(radAngle), 0, 0},
                                                {0, 0, 1, 0},
                                                {0, 0, 0, 1}});
    return new Transformation(matrix, matrix.transpose());
  }

  /**
   * {@link Transformation} which rotates a certain angle around an arbitrary
   * axis defined by the given {@link Vector3D}.
   * 
   * @param angle
   *          The angle (in degrees) to rotate.
   * @param axis
   *          The axis to rotate around.
   * 
   * @return A {@link Transformation} with the rotation matrix.
   */
  public static Transformation rotate(double angle, Vector3D axis) {
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
   * 'Look at' {@link Transformation}. This describes the {@link Transformation}
   * necessary to map coordinates when a location is given and a point to 'look
   * at'. In order to define the correct {@link Transformation}, an 'up'
   * {@link Vector3D} is needed based on which the new coordinate system is
   * built.
   * 
   * @param eye
   *          The location of the eye ({@link Point3D})
   * @param lookAt
   *          The point to look at ({@link Point3D})
   * @param up
   *          An imaginary up vector to make rotations of the coordinate system
   *          possible ({@link Vector3D})
   * 
   * @return A {@link Transformation} which aligns the given coordinates with
   *         the aligned coordinate system.
   * 
   * @see http://cs.fit.edu/~wds/classes/cse5255/thesis/viewTrans/viewTrans.html
   */
  public static Transformation lookAt(Point3D eye, Point3D lookAt, Vector3D up) {
    Vector3D dir = lookAt.sub(eye).normalize();
    Vector3D right = up.cross(dir).normalize();
    Vector3D newUp = dir.cross(right);

    Matrix camToWorld = new Matrix(new double[][] { {right.x, newUp.x, dir.x, eye.x},
                                                    {right.y, newUp.y, dir.y, eye.y},
                                                    {right.z, newUp.z, dir.z, eye.z},
                                                    {0, 0, 0, 1}});

    return new Transformation(camToWorld.inverse(), camToWorld);
  }

  /**
   * An orthographic projection matrix.
   * 
   * @param znear
   *          The near clipping distance.
   * @param zfar
   *          The far clipping distance.
   * 
   * @return A {@link Transformation} containing the view transformation.
   */
  public static Transformation orthographic(double znear, double zfar) {
    Transformation scale = scale(1d, 1d, 1d / (zfar - znear));
    Transformation translation = translate(0d, 0d, -znear);
    return scale.mul(translation);
  }

  /**
   * The transformation matrix for perspective projection onto the view plane.
   * 
   * @param fov
   *          The field of view. The angle between the to and bottom plane of
   *          the view frustum.
   * @param near
   *          The new clipping distance.
   * @param far
   *          The far clipping distance.
   * 
   * @return A transformation matrix to transform between camera and world
   *         coordinates.
   */
  public static Transformation perspective(double fov, double near, double far) {
    Matrix persp = new Matrix(new double[][] { {1, 0, 0, 0},
                                              {0, 1, 0, 0},
                                              {0, 0, far / (far - near), -far * near / (far - near)},
                                              {0, 0, 1, 0}});
    double invTanAng = 1d / Math.tan(toRadians(fov) / 2d);
    Transformation scale = scale(invTanAng, invTanAng, 1);
    return scale.mul(new Transformation(persp));
  }

  public Transformation() {
    this.matrix = Matrix.IDENTITY;
    this.matrixInv = Matrix.IDENTITY;
  }

  public Transformation(Matrix matrix) {
    this.matrix = matrix;
    this.matrixInv = matrix.inverse();
  }

  public Transformation(Matrix matrix, Matrix inverse) {
    this.matrix = matrix;
    this.matrixInv = inverse;
  }

  public Matrix getMatrix() {
    return this.matrix;
  }

  public Matrix getInverseMatrix() {
    return this.matrixInv;
  }

  public Transformation mul(Transformation trans) {
    Matrix matrix = this.matrix.mul(trans.matrix);
    Matrix matrixInv = this.matrixInv.mul(trans.matrixInv);
    return new Transformation(matrix, matrixInv);
  }

  public Transformation inverse() {
    return new Transformation(this.matrixInv, this.matrix);
  }

  public Transformation transpose() {
    return new Transformation(this.matrix.transpose(), this.matrixInv.transpose());
  }
}
