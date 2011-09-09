package yaphyre.shapes;

import yaphyre.geometry.Ray;
import yaphyre.geometry.Vector;
import yaphyre.shaders.Shaders;
import yaphyre.util.Color;
import yaphyre.util.IdentifiableObjects;

public interface Shapes extends IdentifiableObjects {

  public static final double NO_INTERSECTION = Double.POSITIVE_INFINITY;

  public Shaders getShader();

  public double getIntersectDistance(Ray ray);

  public Vector getIntersectionPoint(Ray ray);

  public Vector getNormal(Vector surfacePoint);

  public Color getColor(Vector surfacePoint);

  public boolean throwsShadow();

}
