package yaphyre.lights;

import yaphyre.geometry.Vector;
import yaphyre.raytracer.Scene;
import yaphyre.util.Color;
import yaphyre.util.IdentifiableObjects;

public interface Lightsources extends IdentifiableObjects {

  public Vector getPosition();

  public double getIntensity(Vector point, Scene scene);

  public Color getColor();

}
