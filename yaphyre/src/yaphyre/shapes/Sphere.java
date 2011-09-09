package yaphyre.shapes;

import java.text.MessageFormat;

import yaphyre.geometry.Ray;
import yaphyre.geometry.Vector;
import yaphyre.shaders.Shaders;

/**
 * A sphere in the three dimensional space is defined as:<br/>
 * (p - p<sub>0</sub>) &sdot; (p - p<sub>0</sub>) = r<sup>2</sup><br/>
 * with:
 * <ul>
 * <li>p<sub>0</sub>: center of the sphere</li>
 * <li>p: point on the sphere</li>
 * <li>r: radius</li>
 * </ul>
 * 
 * @author A727595
 */
public class Sphere extends AbstractShape {

  private final Vector center;

  private final double radius;

  public Sphere(String id, Vector origin, double radius, Shaders shader, boolean throwsShadow) {
    super(id, shader, throwsShadow);
    this.center = origin;
    this.radius = radius;
  }

  @Override
  public String toString() {
    return MessageFormat.format("Sphere[{0}, {1}, {2}]", this.getId(), this.center, this.radius);
  }

  /**
   * Determine the distance on a half line where this line intersects with the
   * sphere. To do this, we use the parametric form of a line which is:<br/>
   * p(t) = p<sub>0</sub> + t * d<br/>
   * with
   * <ul>
   * <li>p(t): point on the line for the parameter value t</li>
   * <li>p<sub>0</sub>: line start point</li>
   * <li>d: direction</li>
   * <li>t: parameter value</li>
   * </ul>
   * We have to solve a quadratic equation: a*t<sup>2</sup> + b*t + c = 0<br/>
   * Solutions:<br/>
   * t<sub>0</sub> = (-b - SQRT( b<sup>2</sup> - 4ac)) / 2a<br/>
   * t<sub>1</sub> = (-b + SQRT( b<sup>2</sup> - 4ac)) / 2a<br/>
   * 
   * @param ray
   *          The {@link Ray} to intersect with this sphere.
   * 
   * @return The distance in which the ray intersects this sphere, or if they do
   *         not intersect {@link Shapes#NO_INTERSECTION}.
   */
  @Override
  public double getIntersectDistance(Ray ray) {
    // Transform the origin of the ray into the object space of the sphere.
    Vector oc = ray.getOrigin().sub(this.center);

    double a = ray.getDirection().dot(ray.getDirection());
    double b = 2 * oc.dot(ray.getDirection());
    double c = oc.dot(oc) - this.radius * this.radius;

    double det = b * b - 4 * a * c;

    if (det >= 0) {
      double dist = ((-1) * b - Math.sqrt(det)) / (2 * a);
      if (dist < 0) {
        dist = ((-1) * b + Math.sqrt(det)) / (2 * a);
      }
      // Second check: if the distance is still less than zero, both
      // intersection points lie behind the origin point.
      return (dist > 0) ? dist : Shapes.NO_INTERSECTION;
    }

    return Shapes.NO_INTERSECTION;
  }

  @Override
  public Vector getNormal(Vector surfacePoint) {
    return new Vector(this.center, surfacePoint).unitVector();
  }

  public Vector getCenter() {
    return this.center;
  }

  public double getRadius() {
    return this.radius;
  }

}
