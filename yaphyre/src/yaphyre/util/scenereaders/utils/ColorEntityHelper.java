package yaphyre.util.scenereaders.utils;

import org.joox.Match;

import yaphyre.util.Color;

final class ColorEntityHelper implements EntityHelper<Color> {
  @Override
  public Color decodeEnity(Match entityMatch) {
    double r = entityMatch.attr("r", Double.class);
    double g = entityMatch.attr("g", Double.class);
    double b = entityMatch.attr("b", Double.class);
    return new Color(r, g, b);
  }
}