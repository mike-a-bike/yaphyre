package yaphyre.geometry;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import yaphyre.math.MathUtils;

public class TransformationBuilder {

  public static Transformation translate(double x, double y, double z) {
    Matrix trans = new Matrix(new double[][] { {1, 0, 0, x},
                                               {0, 1, 0, y},
                                               {0, 0, 1, z},
                                               {0, 0, 0, 1}});
    Matrix transInv = new Matrix(new double[][] { {1, 0, 0, -x},
                                                  {0, 1, 0, -y},
                                                  {0, 0, 1, -z},
                                                  {0, 0, 0, 1}});
    return new Transformation(trans, transInv);
  }

  public static Transformation scale(double sx, double sy, double sz) {
    Matrix matrix = new Matrix(new double[][] { {sx, 0, 0, 0},
                                               {0, sy, 0, 0},
                                               {0, 0, sz, 0},
                                               {0, 0, 0, 1}});
    Matrix inv = new Matrix(new double[][] { {1d / sx, 0, 0, 0},
                                            {0, 1d / sy, 0, 0},
                                            {0, 0, 1d / sz, 0},
                                            {0, 0, 0, 1}});
    return new Transformation(matrix, inv);
  }

  public static Transformation rotateX(double angle) {
    double radAngle = MathUtils.toRad(angle);
    Matrix matrix = new Matrix(new double[][] { {1, 0, 0, 0},
                                                {0, cos(radAngle), -sin(radAngle), 0},
                                                {0, sin(radAngle), cos(radAngle), 0},
                                                {0, 0, 0, 1}});
    return new Transformation(matrix, matrix.transpose());
  }

  public static Transformation rotateY(double angle) {
    double radAngle = MathUtils.toRad(angle);
    Matrix matrix = new Matrix(new double[][] { {cos(radAngle), 0, sin(radAngle), 0},
                                                {0, 1, 0, 0},
                                                {-sin(radAngle), 0, cos(radAngle), 0},
                                                {0, 0, 0, 1}});
    return new Transformation(matrix, matrix.transpose());
  }

  public static Transformation rotateZ(double angle) {
    double radAngle = MathUtils.toRad(angle);
    Matrix matrix = new Matrix(new double[][] { {cos(radAngle), -sin(radAngle), 0, 0},
                                                {sin(radAngle), cos(radAngle), 0, 0},
                                                {0, 0, 1, 0},
                                                {0, 0, 0, 1}});
    return new Transformation(matrix, matrix.transpose());
  }

  public static Transformation lookAt(Point3D eye, Point3D lookAt, Vector3D up) {
    Vector3D z = lookAt.sub(eye).normalize();
    Vector3D x = up.cross(z).normalize();
    Vector3D y = z.cross(x);

    throw new RuntimeException("Not implemented yet.");
  }

}
