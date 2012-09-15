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

import static yaphyre.geometry.MathUtils.calcLength;
import static yaphyre.geometry.MathUtils.calculateLengthSquared;


/**
 * Abstraction of a point in a 3d Cartesian coordinate system.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 37 $
 */
public class Point3D implements Serializable {

	private static final long serialVersionUID = -5727406094615817485L;

	public static final Point3D ORIGIN = new Point3D(0, 0, 0);

	final double x, y, z;

	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this.getClass()).add("x", x).add("y", y).add("z", z).toString();
	}

	public Vector3D asVector() {
		return new Vector3D(this.x, this.y, this.z);
	}

	public Normal3D asNormal() {
		return new Normal3D(this.x, this.y, this.z);
	}

	public Point3D add(Point3D p) {
		return new Point3D(this.x + p.x, this.y + p.y, this.z + p.z);
	}

	public Point3D add(Point2D p) {
		return new Point3D(this.x + p.u, this.y + p.v, this.z);
	}

	public Point3D add(Vector3D v) {
		return new Point3D(this.x + v.x, this.y + v.y, this.z + v.z);
	}

	public Point3D sub(Vector3D v) {
		return new Point3D(this.x - v.x, this.y - v.y, this.z - v.z);
	}

	public Vector3D sub(Point3D p) {
		return new Vector3D(this.x - p.x, this.y - p.y, this.z - p.z);
	}

	public double length() {
		return calcLength(this.x, this.y, this.z);
	}

	public double lengthSquared() {
		return calculateLengthSquared(this.x, this.y, this.z);
	}

	public Point3D scale(double s) {
		return new Point3D(this.x * s, this.y * s, this.z * s);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.getClass(), this.x, this.y, this.z);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Point3D other = (Point3D) obj;
		if (!MathUtils.equalsWithTolerance(x, other.x)) {
			return false;
		}
		if (!MathUtils.equalsWithTolerance(y, other.y)) {
			return false;
		}
		if (!MathUtils.equalsWithTolerance(z, other.z)) {
			return false;
		}
		return true;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getZ() {
		return this.z;
	}


}
