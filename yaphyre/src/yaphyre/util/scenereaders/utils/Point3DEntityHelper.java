package yaphyre.util.scenereaders.utils;

import org.joox.Match;

import yaphyre.geometry.Point3D;

class Point3DEntityHelper implements EntityHelper<Point3D> {

  public static final EntityHelper<Point3D> INSTANCE = new Point3DEntityHelper();

  @Override
  public Point3D decodeEntity(Match entityMatch) {
    double x = entityMatch.attr("x", Double.class);
    double y = entityMatch.attr("y", Double.class);
    double z = entityMatch.attr("z", Double.class);
    return new Point3D(x, y, z);
  }

}
