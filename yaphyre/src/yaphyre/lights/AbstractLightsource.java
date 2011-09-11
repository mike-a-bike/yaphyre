package yaphyre.lights;

import yaphyre.geometry.Ray;
import yaphyre.geometry.Vector;
import yaphyre.raytracer.CollisionInformations;
import yaphyre.raytracer.Scene;
import yaphyre.util.Color;
import yaphyre.util.IdentifiableObject;
import yaphyre.util.RenderStatistics;

public abstract class AbstractLightsource extends IdentifiableObject implements Lightsources {

  private static final double EPSILON = 1e-5;

  private final Vector position;

  private final Color color;

  private final Falloff falloff;

  protected AbstractLightsource(String id, Vector position, Color color, Falloff falloff) {
    super(id);
    this.position = position;
    this.color = color;
    this.falloff = falloff;
  }

  @Override
  public Vector getPosition() {
    return this.position;
  }

  @Override
  public Color getColor() {
    return this.color;
  }
  
  protected Falloff getFalloff() {
    return this.falloff;
  }

  protected CollisionInformations calculateVisibility(Vector lightPoint, Vector surfacePoint, Scene scene) {
    RenderStatistics.incShadowRays();
    Vector lightVector = new Vector(lightPoint, surfacePoint);
    Vector lightDirection = lightVector.unitVector();
    Ray lightRay = new Ray(lightPoint, lightDirection);
    double maxCollisionDistance = lightVector.length();
    return scene.getCollidingShape(lightRay, maxCollisionDistance - EPSILON, true);
  }

}
