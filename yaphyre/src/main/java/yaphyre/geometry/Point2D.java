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

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Abstraction of a point in a 2 dimensional space. This uses u and v as coordinates since its major usage will be the
 * mapping of shader and texture informations. <p/>
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 47 $
 */
@SuppressWarnings("ProtectedField")
public class Point2D implements Serializable {

	private static final long serialVersionUID = 3894290074952334962L;

	private static final int INT_SIZE = 32;

	protected final double u, v;

	public static final Point2D ZERO = new Point2D(0d, 0d);

	public Point2D(double u, double v) {
		this.u = u;
		this.v = v;
	}

	@Override
	public int hashCode() {
		long temp = Double.doubleToLongBits(u);
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (temp ^ (temp >>> INT_SIZE));
		temp = Double.doubleToLongBits(v);
		result = prime * result + (int) (temp ^ (temp >>> INT_SIZE));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Point2D)) {
			return false;
		}
		Point2D other = (Point2D) obj;
		if (!MathUtils.equalsWithTolerance(u, other.u)) {
			return false;
		}
		if (!MathUtils.equalsWithTolerance(v, other.v)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(getClass()).add("u", u).add("v", v).toString();
	}

	public Point2D add(Point2D p) {
		return new Point2D(u + p.u, v + p.v);
	}

	public Point2D add(double dU, double dV) {
		return new Point2D(u + dU, v + dV);
	}

	public Point3D add(Point3D p) {
		return new Point3D(u + p.x, v + p.y, p.z);
	}

	public Point2D mul(double s) {
		return new Point2D(u * s, v * s);
	}

	public Point2D mul(double su, double sv) {
		return new Point2D(u * su, v * sv);
	}

	public double dist(Point2D p) {
		return MathUtils.calcLength(p.u - u, p.v - v);
	}

	public double getU() {
		return u;
	}

	public double getV() {
		return v;
	}
}
