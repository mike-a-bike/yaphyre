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

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static yaphyre.geometry.MathUtils.EPSILON;

import java.io.Serializable;

import yaphyre.geometry.MathUtils;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;

import com.google.common.base.Objects;

/**
 * Bounding boxes are used to speed up the rendering process. Complex {@link Shape}s are wrapped by such a {@link
 * BoundingBox} to which simplifies the task of determining if a {@link Ray} potentially intersects with a shape. If
 * the {@link Ray} intersects the bounding box, the more expensive check must be performed to check if the {@link Ray}
 * also intersects with the wrapped {@link Shape}.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 91 $
 */
public class BoundingBox implements Serializable {

	public static final BoundingBox INFINITE_BOUNDING_BOX = new BoundingBox(new Point3D(Double.NEGATIVE_INFINITY,
			Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY), new Point3D(Double.POSITIVE_INFINITY,
			Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)) {
		@Override
		public boolean isHitBy(Ray ray) {
			return true;
		}

		@Override
		public boolean isInside(Point3D p) {
			return true;
		}

		@Override
		public boolean overlaps(BoundingBox box) {
			return true;
		}

		@Override
		public boolean equals(Object obj) {
			return obj == this;
		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}
	};

	private Point3D pointMin;
	private Point3D pointMax;

	public static BoundingBox union(BoundingBox box, Point3D p) {
		BoundingBox result = new BoundingBox();
		result.pointMin = new Point3D(min(box.pointMin.getX(), p.getX()), min(box.pointMin.getY(), p.getY()), min( box.pointMin.getZ(), p.getZ()));
		result.pointMax = new Point3D(max(box.pointMax.getX(), p.getX()), max(box.pointMax.getY(), p.getY()), max( box.pointMax.getZ(), p.getZ()));
		return result;
	}

	public static BoundingBox union(BoundingBox box1, BoundingBox box2) {
		BoundingBox result = new BoundingBox();
		result.pointMin = new Point3D(min(box1.pointMin.getX(), box2.pointMin.getX()), min(box1.pointMin.getY(), box2.pointMin.getY()), min(box1.pointMin.getZ(), box2.pointMin.getZ()));
		result.pointMax = new Point3D(max(box1.pointMax.getX(), box2.pointMax.getX()), max(box1.pointMax.getY(), box2.pointMax.getY()), max(box1.pointMax.getZ(), box2.pointMax.getZ()));
		return result;
	}

	private BoundingBox() {
		pointMin = new Point3D(POSITIVE_INFINITY, POSITIVE_INFINITY, POSITIVE_INFINITY);
		pointMax = new Point3D(NEGATIVE_INFINITY, NEGATIVE_INFINITY, NEGATIVE_INFINITY);
	}

	public BoundingBox(Point3D point) {
		pointMin = point;
		pointMax = point;
	}

	public BoundingBox(Point3D p1, Point3D p2) {
		pointMin = new Point3D(min(p1.getX(), p2.getX()), min(p1.getY(), p2.getY()), min(p1.getZ(), p2.getZ()));
		pointMax = new Point3D(max(p1.getX(), p2.getX()), max(p1.getY(), p2.getY()), max(p1.getZ(), p2.getZ()));
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(getClass()).add("pMin", pointMin).add("pMax", pointMax).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getClass(), pointMin, pointMax);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass().isAssignableFrom(obj.getClass())) {
			final BoundingBox box = getClass().cast(obj);
			return Objects.equal(pointMin, box.pointMin) && Objects.equal(pointMax, box.pointMax);
		}
		return false;
	}

	public boolean isInside(Point3D p) {
		return pointMin.getX() <= p.getX() && pointMax.getX() >= p.getX()
				&& pointMin.getY() <= p.getY() && pointMax.getY() >= p.getY()
				&& pointMin.getZ() <= p.getZ() && pointMax.getZ() >= p.getZ();
	}

	public boolean overlaps(BoundingBox box) {
		return pointMin.getX() <= box.pointMin.getX() && pointMax.getX() >= box.pointMax.getX()
				&& pointMin.getY() <= box.pointMin.getY() && pointMax.getY() >= box.pointMax.getY()
				&& pointMin.getZ() <= box.pointMin.getZ() && pointMax.getZ() >= box.pointMax.getZ();
	}

	public boolean isHitBy(Ray ray) {

		final double ox = ray.getOrigin().getX(); final double oy = ray.getOrigin().getY(); final double oz = ray.getOrigin().getZ();
		final double dx = ray.getDirection().getX(); final double dy = ray.getDirection().getY(); final double dz = ray.getDirection().getZ();

		double tx_min;
		double tx_max;

		double a = 1d / dx;

		if (a >= 0) {
			tx_min = (pointMin.getX() - ox) * a;
			tx_max = (pointMax.getX() - ox) * a;
		} else {
			tx_min = (pointMax.getX() - ox) * a;
			tx_max = (pointMin.getX() - ox) * a;
		}

		double ty_min;
		double ty_max;

		double b = 1d / dy;

		if (b >= 0) {
			ty_min = (pointMin.getY() - oy) * b;
			ty_max = (pointMax.getY() - oy) * b;
		} else {
			ty_min = (pointMax.getY() - oy) * b;
			ty_max = (pointMin.getY() - oy) * b;
		}

		double tz_min;
		double tz_max;

		double c = 1d / dz;

		if (c >= 0) {
			tz_min = (pointMin.getZ() - oz) * c;
			tz_max = (pointMax.getZ() - oz) * c;
		} else {
			tz_min = (pointMax.getZ() - oz) * c;
			tz_max = (pointMin.getZ() - oz) * c;
		}

		double t0 = MathUtils.max(tx_min, ty_min, tz_min);
		double t1 = MathUtils.min(tx_max, ty_max, tz_max);

		return (t0 < t1 && t1 > EPSILON);
	}
}
