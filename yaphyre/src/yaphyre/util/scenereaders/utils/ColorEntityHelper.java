package yaphyre.util.scenereaders.utils;

import org.joox.Match;

import yaphyre.util.Color;

final class ColorEntityHelper implements EntityHelper<Color> {

  public static final EntityHelper<Color> INSTANCE = new ColorEntityHelper();

  @Override
  public Color decodeEntity(Match entityMatch) {
    double r = entityMatch.attr("r", Double.class);
    double g = entityMatch.attr("g", Double.class);
    double b = entityMatch.attr("b", Double.class);
    return new Color(r, g, b);
  }
}