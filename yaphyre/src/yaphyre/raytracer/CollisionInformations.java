package yaphyre.raytracer;

import yaphyre.geometry.Point3D;
import yaphyre.shapes.Shapes;

public class CollisionInformations {

  private final Shapes collisionShape;

  private final double collisionDistance;

  private final Point3D collisionPoint;

  public CollisionInformations(Shapes collisionShape, double collisionDistance, Point3D collisionPoint) {
    this.collisionShape = collisionShape;
    this.collisionDistance = collisionDistance;
    this.collisionPoint = collisionPoint;
  }

  public Shapes getCollisionShape() {
    return this.collisionShape;
  }

  public double getCollisionDistance() {
    return this.collisionDistance;
  }

  public Point3D getCollisionPoint() {
    return this.collisionPoint;
  }

}