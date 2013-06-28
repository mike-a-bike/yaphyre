/*
 * Copyright 2013 Michael Bieri
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

import com.google.common.base.Objects;
import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;

import java.io.Serializable;

/**
 * A record which contains all the information relevant for a ray-object collision.
 *
 * @author Michael Bieri
 * @author $LastChangedBy$
 * @version $Revision$
 */
public class CollisionInformation implements Serializable {

	private static final long serialVersionUID = 9132420627811920135L;

	private final Ray ray;

	private final Shape shape;

	private final double distance;

	private final Point3D point;

	private final Normal3D normal;

	private final Point2D uvCoordinate;

	/**
	 * Creates a new instance for the collision information.
	 */
	public CollisionInformation(final Ray ray, final Shape shape, final double distance,
	                            final Point3D point, final Normal3D normal,
	                            final Point2D uvCoordinate) {
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

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final CollisionInformation that = (CollisionInformation) o;

		if (Double.compare(that.distance, distance) != 0) {
			return false;
		}
		if (!normal.equals(that.normal)) {
			return false;
		}
		if (!point.equals(that.point)) {
			return false;
		}
		if (!ray.equals(that.ray)) {
			return false;
		}
		if (!shape.equals(that.shape)) {
			return false;
		}
		if (!uvCoordinate.equals(that.uvCoordinate)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = shape.hashCode();
		long temp = (distance != +0.0d) ? Double.doubleToLongBits(distance) : 0L;
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + point.hashCode();
		result = 31 * result + ray.hashCode();
		result = 31 * result + normal.hashCode();
		result = 31 * result + uvCoordinate.hashCode();
		return result;
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