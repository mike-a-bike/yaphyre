package yaphyre.shaders;

import yaphyre.util.Color;
import yaphyre.util.IdentifiableObject;

public class SimpleShader extends IdentifiableObject implements Shaders {

  private final Color color;

  private final Material material;

  public SimpleShader(String id, java.awt.Color awtColor, Material material) {
    this(id, material, new Color(awtColor));
  }

  public SimpleShader(String id, Material material, double red, double green, double blue) {
    this(id, material, new Color(red, green, blue));
  }

  public SimpleShader(String id, Material material, Color color) {
    super(id);
    this.material = material;
    this.color = color;
  }

  @Override
  public Color getColor(double u, double v) {
    return this.color;
  }

  @Override
  public Material getMaterial() {
    return this.material;
  }

}
