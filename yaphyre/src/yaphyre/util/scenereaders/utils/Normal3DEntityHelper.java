package yaphyre.util.scenereaders.utils;

import org.joox.Match;

import yaphyre.geometry.Normal3D;

public class Normal3DEntityHelper implements EntityHelper<Normal3D> {

  public static final EntityHelper<Normal3D> INSTANCE = new Normal3DEntityHelper();

  @Override
  public Normal3D decodeEntity(Match entityMatch) {
    double x = entityMatch.attr("x", Double.class);
    double y = entityMatch.attr("y", Double.class);
    double z = entityMatch.attr("z", Double.class);
    return new Normal3D(x, y, z);
  }

}
