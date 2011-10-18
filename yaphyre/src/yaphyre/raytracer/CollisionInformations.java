/*
 * Copyright 2011 Michael Bieri
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package yaphyre.raytracer;

import yaphyre.geometry.Point3D;
import yaphyre.shapes.Shapes;

/**
 * A record which contains all the informations relevant for a collision.
 * 
 * @version $Revision$
 * 
 * @author Michael Bieri
 * @author $LastChangedBy$
 */
public class CollisionInformations {

  private final Shapes collisionShape;

  private final double collisionDistance;

  private final Point3D collisionPoint;

  /**
   * Creates a new instance for the collision informations.
   * 
   * @param collisionShape
   *          The {@link Shapes} instance which is hit.
   * @param collisionDistance
   *          The value of the <code>t</code> parameter in which the collision
   *          happens.
   * @param collisionPoint
   *          The {@link Point3D} of the collision.
   */
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