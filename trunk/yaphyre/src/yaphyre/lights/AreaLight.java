package yaphyre.lights;

import java.text.MessageFormat;

import yaphyre.geometry.Vector;
import yaphyre.raytracer.CollisionInformations;
import yaphyre.raytracer.Scene;
import yaphyre.util.Color;

/**
 * Simple implementation of an area light. This type of light is not just a
 * single point in space which is either seen or not, but has actual physical
 * dimensions.<br/>
 * TODO: implement monte carlo light distribution
 * 
 * @author Michael Bieri
 */
public class AreaLight extends AbstractLightsource {

  private static final String TO_STRING_FORMAT = "AreaLight[{0}, {1}, {2}, {3}x{3}, {5}x{5}, {6}, {4}]";

  private final Vector orientation;

  private final double width;

  private final int numberOfSamples;

  private final int numberOfRays;

  private final double rayIntensity;

  private final double intensity;

  private Vector[] originPoints;

  public AreaLight(String id, Vector position, Vector orientation, double width, int numberOfSamples, double intensity, Color color, Falloff falloff) {
    super(id, position, color, falloff);
    this.orientation = orientation;
    this.width = width;
    this.numberOfSamples = numberOfSamples;
    this.numberOfRays = this.numberOfSamples * this.numberOfSamples;
    this.intensity = intensity;
    this.rayIntensity = this.intensity / this.numberOfRays;
    this.originPoints = null;
  }

  @Override
  public String toString() {
    return MessageFormat.format(TO_STRING_FORMAT, getId(), getPosition(), this.orientation, this.width, getColor(), this.numberOfSamples, this.intensity);
  }

  @Override
  public double getIntensity(Vector point, Scene scene) {

    if (originPoints == null) {
      this.initializeOrigins();
    }

    double accumulatedIntensity = 0d;

    for (Vector origin : originPoints) {
      CollisionInformations shadowCollision = super.calculateVisibility(origin, point, scene);
      if (shadowCollision == null) {
        accumulatedIntensity += super.getFalloff().getIntensity(this.rayIntensity, origin.sub(point).length());
      }
    }

    return accumulatedIntensity;

  }

  private void initializeOrigins() {
    this.originPoints = new Vector[this.numberOfRays];
    double dx = this.width / this.numberOfSamples;
    double dy = this.width / this.numberOfSamples;
    for (int yIndex = 0; yIndex < this.numberOfSamples; yIndex++) {
      for (int xIndex = 0; xIndex < this.numberOfSamples; xIndex++) {
        int index = yIndex * this.numberOfSamples + xIndex;
      }
    }
  }

}
