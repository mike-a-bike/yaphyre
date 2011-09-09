package yaphyre.geometry;

import java.text.MessageFormat;

/**
 * Simple implementation of a ray. These are used as seeing rays and shadow
 * rays.<br/>
 * 
 * It is defined by an origin ({@link Vector} interpreted as the sum of the
 * Cartesian origin plus the origin vector) and a direction ( {@link Vector}). A
 * point on the ray are represented in a parametric way so that:
 * 
 * <pre>
 * p(distance) = origin + distance * direction
 * </pre>
 * 
 * @author Michael Bieri
 */
public class Ray {

  private static final String TO_STRING_FORMAT = "Ray[{0}, {1}]";

  private final Vector origin;

  private final Vector direction;

  public Ray(Vector origin, Vector direction) throws ArithmeticException {
    this.origin = origin;
    this.direction = direction;
  }

  @Override
  public String toString() {
    return MessageFormat.format(TO_STRING_FORMAT, this.origin, this.direction);
  }

  public Vector getPoint(double distance) {
    return this.origin.add(this.direction.scale(distance));
  }

  public Vector getOrigin() {
    return this.origin;
  }

  public Vector getDirection() {
    return this.direction;
  }

}
