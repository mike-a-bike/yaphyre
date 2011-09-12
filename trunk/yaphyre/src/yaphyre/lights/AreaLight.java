package yaphyre.lights;

import java.text.MessageFormat;

import yaphyre.geometry.Matrix;
import yaphyre.geometry.TransformationMatrixBuilder;
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

  private final double size;

  private final int samplesPerSide;

  private final int numberOfRays;

  private final double rayIntensity;

  private final double intensity;

  public AreaLight(String id, Vector position, Vector orientation, double size, int samplesPerSide, double intensity, Color color, Falloff falloff) {
    super(id, position, color, falloff);
    this.orientation = orientation;
    this.size = size;
    this.samplesPerSide = samplesPerSide;
    this.numberOfRays = this.samplesPerSide * this.samplesPerSide;
    this.intensity = intensity;
    this.rayIntensity = this.intensity / this.numberOfRays;
  }

  @Override
  public String toString() {
    return MessageFormat.format(TO_STRING_FORMAT, getId(), getPosition(), this.orientation, this.size, getColor(), this.samplesPerSide, this.intensity);
  }

  @Override
  public double getIntensity(Vector point, Scene scene) {

    double accumulatedIntensity = 0d;

    for (Vector origin : createOrigins()) {
      CollisionInformations shadowCollision = super.calculateVisibility(origin, point, scene);
      if (shadowCollision == null) {
        accumulatedIntensity += super.getFalloff().getIntensity(this.rayIntensity, origin.sub(point).length());
      }
    }

    return accumulatedIntensity;

  }

  /**
   * Calculates an array of origin points based on the distribution in a 1x1
   * unit rectangle which is the transformed into the scene coordinate space by
   * the lights position and direction.
   * 
   * @return An array of {@link Vector}, each representing a start point for a
   *         light ray.
   */
  private Vector[] createOrigins() {
    Vector[] originPoints = new Vector[this.numberOfRays];
    Matrix tranformationMatrix = TransformationMatrixBuilder.matrix().forTranslation(getPosition()).build();
    double dx = 1d / this.samplesPerSide;
    double dy = 1d / this.samplesPerSide;
    double startx = -0.5d;
    double starty = -0.5d;
    for (int yIndex = 0; yIndex < this.samplesPerSide; yIndex++) {
      for (int xIndex = 0; xIndex < this.samplesPerSide; xIndex++) {
        int index = yIndex * this.samplesPerSide + xIndex;
        Vector localStartPoint = new Vector(startx + xIndex * dx, starty + yIndex * dy, 0);
        originPoints[index] = localStartPoint;
      }
    }
    return originPoints;
  }

}
