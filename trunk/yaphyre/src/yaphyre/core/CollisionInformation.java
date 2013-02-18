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

import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
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

	private final Shape shape;

	private final double distance;

	private final Point3D point;

	private final Ray ray;

	private final Normal3D normal;

	private final Point2D uvCoordinate;

	/**
	 * Creates a new instance for the collision information.
	 *
	 * @param ray
	 * @param shape
	 * @param distance
	 * @param point
	 * @param normal
	 */
	public CollisionInformation(final Ray ray, Shape shape, double distance, Point3D point, final Normal3D normal, final Point2D uvCoordinate) {
		this.shape = shape;
		this.distance = distance;
		this.point = point;
		this.ray = ray;
		this.normal = normal;
		this.uvCoordinate = uvCoordinate;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(
				getClass())
				.add("ray", ray)
				.add("shape", shape)
				.add("distance", distance)
				.add("point", point)
				.add("normal", normal)
				.add("uvCoordinate", uvCoordinate).toString();
	}

	public Ray getRay() {
		return ray;
	}

	public Shape getShape() {
		return shape;
	}

	public double getDistance() {
		return distance;
	}

	public Point3D getPoint() {
		return point;
	}

	public Normal3D getNormal() {
		return normal;
	}

	public Point2D getUVCoordinate() {
		return uvCoordinate;
	}
}