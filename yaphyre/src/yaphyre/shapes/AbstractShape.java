package yaphyre.shapes;

import yaphyre.geometry.Ray;
import yaphyre.geometry.Vector;
import yaphyre.shaders.Shaders;
import yaphyre.util.Color;
import yaphyre.util.IdentifiableObject;

public abstract class AbstractShape extends IdentifiableObject implements Shapes {

  private final Shaders shader;

  private final boolean throwsShadow;

  protected AbstractShape(String id, Shaders shader, boolean throwsShadow) {
    super(id);
    this.shader = shader;
    this.throwsShadow = throwsShadow;
  }

  @Override
  public Shaders getShader() {
    return this.shader;
  }

  @Override
  public boolean throwsShadow() {
    return this.throwsShadow;
  }

  @Override
  public Vector getIntersectionPoint(Ray ray) {
    double intersectionDistance = this.getIntersectDistance(ray);
    if (intersectionDistance == Shapes.NO_INTERSECTION) {
      return null;
    }

    return ray.getPoint(intersectionDistance);
  }

  // TODO implement by subclasses
  @Override
  public Color getColor(Vector surfacePoint) {
    return this.shader.getColor(0, 0);
  }

}
