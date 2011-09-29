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

import java.text.MessageFormat;

import yaphyre.math.MathUtils;

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
public class Vector3D {

  private final String TO_STRING_FORMAT = "<{0,number,0.000}, {1,number,0.000}, {2,number,0.000}>";

  public static final Vector3D NULL = new Vector3D(0d, 0d, 0d);

  final double x, y, z;

  public Vector3D(Point3D start, Point3D end) {
    this(end.x - start.x, end.y - start.y, end.z - start.z);
  }

  public Vector3D(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Normal3D asNormal() {
    Vector3D unitVector = unitVector();
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

  public Vector3D unitVector() throws ArithmeticException {
    double length = length();
    if (length == 0d) {
      throw new ArithmeticException("Cannot create unit vector from zero length vector");
    } else if (length == 1d) {
      return this;
    }
    return scale(1 / length);
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

  public Vector3D transform(Matrix transformation) {
    return transformation.mul(this);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Vector3D)) {
      return false;
    }
    Vector3D other = (Vector3D)o;
    return (other.getX() == this.x) && (other.getY() == this.y) && (other.getZ() == this.z);
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
    return MessageFormat.format(this.TO_STRING_FORMAT, this.x, this.y, this.z);
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