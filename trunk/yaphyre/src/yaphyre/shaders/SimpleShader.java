package yaphyre.shaders;

import yaphyre.core.Shader;
import yaphyre.geometry.Point2D;
import yaphyre.util.Color;

public class SimpleShader implements Shader {

  private static final long serialVersionUID = 5072616518896339854L;

  private final Color color;

  private final Material material;

  public SimpleShader(Material material, java.awt.Color awtColor) {
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
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((color == null) ? 0 : color.hashCode());
    result = prime * result + ((material == null) ? 0 : material.hashCode());
    return result;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SimpleShader other = (SimpleShader)obj;
    if (color == null) {
      if (other.color != null)
        return false;
    } else if (!color.equals(other.color))
      return false;
    if (material == null) {
      if (other.material != null)
        return false;
    } else if (!material.equals(other.material))
      return false;
    return true;
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
