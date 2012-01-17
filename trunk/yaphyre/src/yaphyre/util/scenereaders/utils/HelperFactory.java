package yaphyre.util.scenereaders.utils;

import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Transformation;
import yaphyre.geometry.Vector3D;
import yaphyre.util.Color;

public abstract class HelperFactory {

  public static final EntityHelper<Color> getColorHelper() {
    return ColorEntityHelper.INSTANCE;
  }

  public static final EntityHelper<Vector3D> getVector3DHelper() {
    return Vector3DEntityHelper.INSTANCE;
  }

  public static final EntityHelper<Normal3D> getNormal3DHelper() {
    return Normal3DEntityHelper.INSTANCE;
  }

  public static final EntityHelper<Point3D> getPoint3DHelper() {
    return Point3DEntityHelper.INSTANCE;
  }

  public static final EntityHelper<Point2D> getPoint2DHelper() {
    return Point2DEntityHelper.INSTANCE;
  }

  public static final EntityHelper<Transformation> getTransformationHelper() {
    return TransformationEntityHelper.INSTANCE;
  }

}
