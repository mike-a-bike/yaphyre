package yaphyre.shaders;

import yaphyre.core.Shaders;
import yaphyre.geometry.Point2D;
import yaphyre.util.Color;

public class SimpleShader implements Shaders {

  private final Color color;

  private final Material material;

  public SimpleShader(java.awt.Color awtColor, Material material) {
    this(material, new Color(awtColor));
  }

  public SimpleShader(Material material, double red, double green, double blue) {
    this(material, new Color(red, green, blue));
  }

  public SimpleShader(Material material, Color color) {
    this.material = material;
    this.color = color;
  }

  @Override
  public Color getColor(Point2D uvCoordinate) {
    return this.color;
  }

  @Override
  public Material getMaterial(Point2D uvCoordinate) {
    return this.material;
  }

}
