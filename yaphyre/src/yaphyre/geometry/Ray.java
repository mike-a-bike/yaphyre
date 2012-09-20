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
package yaphyre.geometry;

import static yaphyre.geometry.MathUtils.EPSILON;

import java.io.Serializable;

import com.google.common.base.Objects;

/**
 * Simple implementation of a ray. These are used as seeing rays and shadow rays.<br/> <p/> It is defined by an origin
 * ({@link Point3D}) and a direction ( {@link Vector3D}). A point on the ray are represented in a parametric way so
 * that: <p/>
 * <pre>
 * p(distance) = origin + distance * direction
 * </pre>
 *
 * @author Michael Bieri
 * @author $LastChangedBy$
 * @version $Revision$
 */
public class Ray implements Serializable {

	private static final long serialVersionUID = 6349693380913182303L;

	private final Point3D origin;

	private final Vector3D direction;

	private final double mint;

	private final double maxt;

	public Ray(Point3D origin, Vector3D direction) {
		this(origin, direction, EPSILON, Double.MAX_VALUE);
	}

	public Ray(Point3D origin, Vector3D direction, double mint, double maxt) {
		this.origin = origin;
		this.direction = direction;
		this.mint = mint;
		this.maxt = maxt;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(getClass()).add("origin", origin).add("direction", direction).toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + direction.hashCode();
		result = prime * result + origin.hashCode();
		long temp = Double.doubleToLongBits(maxt);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(mint);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Ray)) {
			return false;
		}
		Ray other = (Ray) obj;
		if (!direction.equals(other.direction)) {
			return false;
		}
		if (!origin.equals(other.origin)) {
			return false;
		}
		if (!MathUtils.equalsWithTolerance(maxt, other.maxt)) {
			return false;
		}
		if (!MathUtils.equalsWithTolerance(mint, other.mint)) {
			return false;
		}
		return true;
	}

	public Point3D getPoint(double distance) {
		return origin.add(direction.scale(distance));
	}

	public Point3D getOrigin() {
		return origin;
	}

	public Vector3D getDirection() {
		return direction;
	}

	public double getMint() {
		return mint;
	}

	public double getMaxt() {
		return maxt;
	}

}
