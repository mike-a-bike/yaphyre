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
package yaphyre.math;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * Represents a normal in the 3d Cartesian coordinate system.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 37 $
 */
@SuppressWarnings("PackageVisibleField")
public class Normal3D implements Serializable {

	private static final long serialVersionUID = 5210137820413107110L;

	public static final Normal3D NORMAL_X = new Normal3D(1d, 0d, 0d);

	public static final Normal3D NORMAL_Y = new Normal3D(0d, 1d, 0d);

	public static final Normal3D NORMAL_Z = new Normal3D(0d, 0d, 1d);

	final double x, y, z;

	public Normal3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public String toString() {
		return MessageFormat.format("|{0,number,0.000}, {1,number,0.000}, {2,number,0.000}|", x, y, z);
	}

	@Override
	public int hashCode() {
		long temp = Double.doubleToLongBits(x);
		int result = 1;
		final int prime = 31;
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		Normal3D other = (Normal3D) obj;
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

	public Vector3D asVector() {
		return new Vector3D(x, y, z);
	}

	public Point3D asPoint() {
		return new Point3D(x, y, z);
	}

	public Normal3D neg() {
		return new Normal3D(-x, -y, -z);
	}

	public Normal3D add(Normal3D n) {
		return new Normal3D(x + n.x, y + n.y, z + n.z);
	}

	public double dot(Vector3D v) {
		return x * v.x + y * v.y + z * v.z;
	}

	public Normal3D scale(double s) {
		return new Normal3D(x * s, y * s, z * s);
	}

	public Vector3D add(Vector3D v) {
		return new Vector3D(x + v.x, y + v.y, z + v.z);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public Normal3D faceForward(final Ray collisionRay) {
		return (asVector().dot(collisionRay.getDirection()) >= 0) ? this : neg();
	}
}
