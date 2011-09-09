package yaphyre.raytracer;

import yaphyre.geometry.Vector;
import yaphyre.shapes.Shapes;

public class CollisionInformations {

  private final Shapes collisionShape;

  private final double collisionDistance;

  private final Vector collisionPoint;

  public CollisionInformations(Shapes collisionShape, double collisionDistance, Vector collisionPoint) {
    this.collisionShape = collisionShape;
    this.collisionDistance = collisionDistance;
    this.collisionPoint = collisionPoint;
  }

  public Shapes getCollisionShape() {
    return collisionShape;
  }

  public double getCollisionDistance() {
    return collisionDistance;
  }

  public Vector getCollisionPoint() {
    return collisionPoint;
  }

}