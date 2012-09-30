/*
 * Copyright 2012 Michael Bieri
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

package yaphyre.core;

import java.io.Serializable;

import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;

import com.google.common.base.Objects;

/**
 * A record which contains all the information relevant for a collision.
 *
 * @author Michael Bieri
 * @author $LastChangedBy$
 * @version $Revision$
 */
public class CollisionInformation implements Serializable {

	private static final long serialVersionUID = 9132420627811920135L;

	private final Shape collisionShape;

	private final double collisionDistance;

	private final Point3D collisionPoint;

	private final Ray collisionRay;

	/**
	 * Creates a new instance for the collision information.
	 *
	 * @param collisionRay
	 *  The {@link Ray} which is represented by this collision information instance.
	 * @param collisionShape
	 * 		The {@link yaphyre.core.Shape} instance which is hit.
	 * @param collisionDistance
 * 		The value of the <code>t</code> parameter in which the collision happens.
	 * @param collisionPoint
* 		The {@link yaphyre.geometry.Point3D} of the collision.
	 */
	public CollisionInformation(final Ray collisionRay, Shape collisionShape, double collisionDistance, Point3D collisionPoint) {
		this.collisionShape = collisionShape;
		this.collisionDistance = collisionDistance;
		this.collisionPoint = collisionPoint;
		this.collisionRay = collisionRay;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(getClass()).add("ray", collisionRay).add("shape", collisionShape).add("distance", collisionDistance).add("point", collisionPoint).toString();
	}

	public Ray getCollisionRay() {
		return collisionRay;
	}

	public Shape getCollisionShape() {
		return collisionShape;
	}

	public double getCollisionDistance() {
		return collisionDistance;
	}

	public Point3D getCollisionPoint() {
		return collisionPoint;
	}

}