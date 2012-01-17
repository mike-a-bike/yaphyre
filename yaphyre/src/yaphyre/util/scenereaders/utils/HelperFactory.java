package yaphyre.util.scenereaders.utils;


import yaphyre.geometry.Point3D;
import yaphyre.geometry.Transformation;
import yaphyre.geometry.Vector3D;
import yaphyre.util.Color;

public abstract class HelperFactory {

  private static final EntityHelper<Color> COLOR_HELPER = new ColorEntityHelper();

  public static final EntityHelper<Color> getColorHelper() {
    return COLOR_HELPER;
  }

  private static final EntityHelper<Transformation> TRANSFORMATION_HELPER = new TransformationEntityHelper();

  public static final EntityHelper<Transformation> getTransformationHelper() {
    return TRANSFORMATION_HELPER;
  }

  public static final EntityHelper<Vector3D> getVector3DHelper() {
    return Vector3DEntityHelper.INSTANCE;
  }

  public static final EntityHelper<Point3D> getPoint3DHelper() {
    return Point3DEntityHelper.INSTANCE;
  }

}
