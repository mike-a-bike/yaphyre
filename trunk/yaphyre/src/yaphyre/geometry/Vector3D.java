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

import static java.lang.Math.abs;

import java.io.Serializable;

import com.google.common.base.Objects;

/**
 * Represent an arithmetical vector within a 3 dimensional Cartesian coordinate
 * space. This class also provides the rudimentary operations for calculating
 * with vectors.
 * 
 * @version $Revision$
 * 
 * @author Michael Bieri
 * @author $LastChangedBy$
 */
public class Vector3D implements Serializable {

  private static final long serialVersionUID = 6313172979195055223L;

  public static final Vector3D NULL = new Vector3D(0d, 0d, 0d);

  public static final Vector3D X = new Vector3D(1d, 0d, 0d);

  public static final Vector3D Y = new Vector3D(0d, 1d, 0d);

  public static final Vector3D Z = new Vector3D(0d, 0d, 1d);

  final double x, y, z;

  /**
   * Creates a new instance of {@link Vector3D} so that P<sub>start</sub> + V =
   * P<sub>end</sub>
   * 
   * @param start
   *          The start point for the calculation (P<sub>start</sub>)
   * @param end
   *          The end point for tha calculation (P<sub>end</sub>)
   */
  public Vector3D(Point3D start, Point3D end) {
    this(end.x - start.x, end.y - start.y, end.z - start.z);
  }

  /**
   * Create a new {@link Vector3D} with the given components.
   * 
   * @param x
   *          The X axis component.
   * @param y
   *          The Y axis component.
   * @param z
   *          The Z axis component.
   */
  public Vector3D(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Normal3D asNormal() {
    Vector3D unitVector = this.normalize();
    return new Normal3D(unitVector.x, unitVector.y, unitVector.z);
  }

  public Point3D asPoint() {
    return new Point3D(this.x, this.y, this.z);
  }

  public Vector3D neg() {
    return new Vector3D(-this.x, -this.y, -this.z);
  }

  public Vector3D add(Vector3D v) {
    return new Vector3D(this.x + v.x, this.y + v.y, this.z + v.z);
  }

  public Vector3D add(Normal3D n) {
    return new Vector3D(this.x + n.x, this.y + n.y, this.z + n.z);
  }

  public Vector3D sub(Vector3D v) {
    return new Vector3D(this.x - v.x, this.y - v.y, this.z - v.z);
  }

  public Vector3D sub(Normal3D n) {
    return new Vector3D(this.x - n.x, this.y - n.y, this.z - n.z);
  }

  public double length() {
    return MathUtils.calcLength(this.x, this.y, this.z);
  }

  public double lengthSquared() {
    return MathUtils.calculateLengthSquared(this.x, this.y, this.z);
  }

  public Vector3D normalize() throws ArithmeticException {
    double length = this.length();
    if (length == 0d) {
      throw new ArithmeticException("Cannot create unit vector from zero length vector");
    } else if (length == 1d) {
      return this;
    }
    return this.scale(1 / length);
  }

  public Vector3D scale(double s) {
    return new Vector3D(this.x * s, this.y * s, this.z * s);
  }

  public double dot(Vector3D v) {
    return this.x * v.x + this.y * v.y + this.z * v.z;
  }

  public double dot(Normal3D n) {
    return this.x * n.x + this.y * n.y + this.z * n.z;
  }

  public Vector3D cross(Vector3D v) {
    double cx = this.y * v.z - this.z * v.y;
    double cy = this.z * v.x - this.x * v.z;
    double cz = this.x * v.y - this.y * v.x;
    return new Vector3D(cx, cy, cz);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Vector3D)) {
      return false;
    }
    Vector3D other = (Vector3D) o;
    return this.equals(other, MathUtils.EPSILON);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(this.x);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(this.y);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(this.z);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  public boolean equals(Vector3D vector, double tolerance) {
    if (this == vector) {
      return true;
    }
    if (vector == null) {
      return false;
    }
    double difference = abs(this.x - vector.getX()) + abs(this.y - vector.getY()) + abs(this.z - vector.getZ());
    return difference <= tolerance;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this.getClass()).add("x", this.x).add("y", this.y).add("z", this.z).toString();
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

}
