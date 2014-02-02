/*
 * Copyright 2014 Michael Bieri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yaphyre.math;

import com.google.common.base.Objects;
import org.apache.commons.lang3.Range;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

import static yaphyre.math.MathUtils.EPSILON;

/**
 * Simple implementation of a ray. These are used as seeing rays and shadow rays.<br/> <p/> It is defined by an origin
 * ({@link Point3D}) and a direction ( {@link Vector3D}). A point on the ray are represented in a parametric way so
 * that: <p/>
 * <pre>
 * p(distance) = origin + distance * direction
 * </pre>
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 208 $
 */
@Immutable
public class Ray implements Serializable {

	private static final long serialVersionUID = 6349693380913182303L;

	private final Point3D origin;

	private final Vector3D direction;

	private final Range<Double> tRange;

	public Ray(Point3D origin, Vector3D direction) {
		this(origin, direction, EPSILON, Double.MAX_VALUE);
	}

	public Ray(Point3D origin, Vector3D direction, double mint, double maxt) {
		this.origin = origin;
		this.direction = direction;
		this.tRange = Range.between(mint, maxt);
	}

	public Ray(Point3D origin, Vector3D direction, Range<Double> tRange) {
		this.origin = origin;
		this.direction = direction;
		this.tRange = tRange;
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
		result = prime * result + tRange.hashCode();
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
		if (!tRange.equals(other.tRange)) {
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

	public Range<Double> getTRange() {
		return tRange;
	}

}
