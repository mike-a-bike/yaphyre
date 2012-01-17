package yaphyre.util.scenereaders.utils;

import org.joox.Match;

import yaphyre.geometry.Point2D;

public class Point2DEntityHelper implements EntityHelper<Point2D> {

  public static final EntityHelper<Point2D> INSTANCE = new Point2DEntityHelper();

  @Override
  public Point2D decodeEntity(Match entityMatch) {
    double u = entityMatch.attr("u", Double.class);
    double v = entityMatch.attr("v", Double.class);
    return new Point2D(u, v);
  }

}
