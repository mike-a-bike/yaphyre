package yaphyre.lights;

import java.text.MessageFormat;

import yaphyre.geometry.Vector;
import yaphyre.raytracer.CollisionInformations;
import yaphyre.raytracer.Scene;
import yaphyre.util.Color;

public class Pointlight extends AbstractLightsource {

  private final static String TO_STRING_FORMAT = "Pointlight[{0}, {1}, {2}, {3}]";

  private static final double EPSILON = 1e-5;

  private final double intensity;

  private final Falloff falloff;

  public Pointlight(String id, Vector position, Color color, double intensity, Falloff falloff) {
    super(id, position, color);
    this.intensity = intensity;
    this.falloff = falloff;
  }

  @Override
  public String toString() {
    return MessageFormat.format(TO_STRING_FORMAT, this.getPosition(), super.getColor(), this.intensity, this.falloff);
  }

  @Override
  public double getIntensity(Vector point, Scene scene) {
    double intensity = 0d;
    CollisionInformations shadowCollision = calculateVisibility(getPosition(), point, scene);
    if (shadowCollision == null) {
      intensity = this.falloff.getIntensity(this.intensity, this.getPosition().sub(point).length());
    }
    return intensity;
  }
}
