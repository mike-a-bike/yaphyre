package yaphyre.lights;

import java.text.MessageFormat;

import yaphyre.geometry.Vector;
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

  private final double intensity;

  public AreaLight(String id, Vector position, Vector orientation, double width, int numberOfSamples, double intensity, Color color) {
    super(id, position, color);
    this.orientation = orientation;
    this.width = width;
    this.numberOfSamples = numberOfSamples;
    this.intensity = intensity;
  }

  @Override
  public String toString() {
    return MessageFormat.format(TO_STRING_FORMAT, getId(), getPosition(), this.orientation, this.width, getColor(), this.numberOfSamples, this.intensity);
  }

  @Override
  public double getIntensity(Vector point, Scene scene) {
    // TODO Auto-generated method stub
    return 0;
  }

}
