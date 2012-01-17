package yaphyre.util.scenereaders.utils;

import org.joox.Match;

import yaphyre.geometry.Vector3D;

class Vector3DEntityHelper implements EntityHelper<Vector3D> {

  public static final EntityHelper<Vector3D> INSTANCE = new Vector3DEntityHelper();

  @Override
  public Vector3D decodeEntity(Match entityMatch) {
    double x = entityMatch.attr("x", Double.class);
    double y = entityMatch.attr("y", Double.class);
    double z = entityMatch.attr("z", Double.class);
    return new Vector3D(x, y, z);
  }

}
