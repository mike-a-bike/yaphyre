package yaphyre.shaders;

import yaphyre.util.Color;
import yaphyre.util.IdentifiableObjects;

public interface Shaders extends IdentifiableObjects {

  public Color getColor(double u, double v);

  public Material getMaterial();

}
