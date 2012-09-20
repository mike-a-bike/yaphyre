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

import java.io.Serializable;

import com.google.common.base.Objects;

/**
 * Abstraction of a point in a 2 dimensional space. This uses u and v as coordinates since its major usage will be the
 * mapping of shader and texture informations. <p/> TODO implement the camera, so that it uses the 2d coordinates in
 * order to create the seeing rays.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 47 $
 */
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
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(this.u);
		result = prime * result + (int) (temp ^ (temp >>> INT_SIZE));
		temp = Double.doubleToLongBits(this.v);
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
		if (!MathUtils.equalsWithTolerance(this.u, other.u)) {
			return false;
		}
		if (!MathUtils.equalsWithTolerance(this.v, other.v)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this.getClass()).add("u", u).add("v", v).toString();
	}

	public Point2D add(Point2D p) {
		return new Point2D(this.u + p.u, this.v + p.v);
	}

	public Point3D add(Point3D p) {
		return new Point3D(this.u + p.x, this.v + p.y, p.z);
	}

	public Point2D mul(double s) {
		return new Point2D(this.u * s, this.v * s);
	}

	public Point2D mul(double su, double sv) {
		return new Point2D(this.u * su, this.v * sv);
	}

	public double dist(Point2D p) {
		return MathUtils.calcLength(p.u - this.u, p.v - this.v);
	}

	public double getU() {
		return this.u;
	}

	public double getV() {
		return this.v;
	}
}
