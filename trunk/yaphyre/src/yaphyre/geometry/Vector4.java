package yaphyre.geometry;

/**
 * Package specific helper class for a 4D vector.
 * 
 * @author Michael Bieri
 */
class Vector4 {

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

  public Vector4 mul(Matrix transformation) {
    double x1 = this.x * transformation.get(0, 0) + this.y * transformation.get(1, 0) + this.z * transformation.get(2, 0) + this.w * transformation.get(3, 0);
    double y1 = this.x * transformation.get(0, 1) + this.y * transformation.get(1, 1) + this.z * transformation.get(2, 1) + this.w * transformation.get(3, 1);
    double z1 = this.x * transformation.get(0, 2) + this.y * transformation.get(1, 2) + this.z * transformation.get(2, 2) + this.w * transformation.get(3, 2);
    double w1 = this.x * transformation.get(0, 3) + this.y * transformation.get(1, 3) + this.z * transformation.get(2, 3) + this.w * transformation.get(3, 3);
    return new Vector4(x1, y1, z1, w1);
  }
}