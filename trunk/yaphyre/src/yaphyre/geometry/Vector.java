package yaphyre.geometry;

import static java.lang.Math.abs;

import java.text.MessageFormat;

/**
 * Represent an arithmetical vector within a 3 dimensional Cartesian coordinate
 * space. This class also provides the rudimentary operations for calculating
 * with vectors.
 * 
 * @author Michael Bieri
 */
public class Vector {

  private static final String TO_STRING_FORMAT = "<{0,number}, {1,number}, {2, number}>";

  public static final Vector NULL = new Vector(0d, 0d, 0d);

  public static final Vector ORIGIN = NULL;

  public static final Vector NORMAL_X = new Vector(1d, 0d, 0d);

  public static final Vector NORMAL_Y = new Vector(0d, 1d, 0d);

  public static final Vector NORMAL_Z = new Vector(0d, 0d, 1d);

  private final double x;

  private final double y;

  private final double z;

  public Vector(Vector start, Vector end) {
    this.x = end.getX() - start.getX();
    this.y = end.getY() - start.getY();
    this.z = end.getZ() - start.getZ();
  }

  public Vector(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Vector add(Vector v) {
    return new Vector(this.x + v.getX(), this.y + v.getY(), this.z + v.getZ());
  }

  public Vector sub(Vector v) {
    return new Vector(this.x - v.getX(), this.y - v.getY(), this.z - v.getZ());
  }

  public double length() {
    return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
  }

  public Vector unitVector() throws ArithmeticException {
    if (this == NORMAL_X || this == NORMAL_Y || this == NORMAL_Z) {
      return this;
    }
    double length = length();
    if (length == 0d) {
      throw new ArithmeticException("Cannot create unit vector from zero length vector");
    } else if (length == 1d) {
      return this;
    }
    return scale(1 / length);
  }

  public Vector scale(double scalar) {
    return new Vector(this.x * scalar, this.y * scalar, this.z * scalar);
  }

  public double dot(Vector v) {
    return this.x * v.getX() + this.y * v.getY() + this.z * v.getZ();
  }

  public Vector cross(Vector v) {
    double cx = this.y * v.getZ() - this.z * v.getY();
    double cy = this.z * v.getX() - this.x * v.getZ();
    double cz = this.x * v.getY() - this.y * v.getX();
    return new Vector(cx, cy, cz);
  }

  public Vector transform(Matrix transformation) {
    return transformation.mul(this);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Vector)) {
      return false;
    }
    Vector other = (Vector)o;
    return (other.getX() == this.x) && (other.getY() == this.y) && (other.getZ() == this.z);
  }

  public boolean equals(Vector vector, double tolerance) {
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
    return MessageFormat.format(TO_STRING_FORMAT, this.x, this.y, this.z);
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
