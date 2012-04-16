/*
 * Copyright 2011 Michael Bieri
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
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;

import com.google.common.base.Objects;

/**
 * Bounding boxes are used to speed up the rendering process. Complex
 * {@link Shape}s are wrapped by such a {@link BoundingBox} to which simplifies
 * the task of determining if a {@link Ray} potentially intersects with a shape.
 * If the {@link Ray} intersects the bounding box, the more expensive check must
 * be performed to check if the {@link Ray} also intersects with the wrapped
 * {@link Shape}.
 * 
 * @version $Revision: 91 $
 * 
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 */
public class BoundingBox {

  public static final BoundingBox INFINITE_BOUNDING_BOX = new BoundingBox(new Point3D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY), new Point3D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)) {
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
  };

  protected Point3D pointMin;
  protected Point3D pointMax;

  public static BoundingBox union(BoundingBox box, Point3D p) {
    BoundingBox result = new BoundingBox();
    result.pointMin = new Point3D(min(box.pointMin.getX(), p.getX()),
                                  min(box.pointMin.getY(), p.getY()),
                                  min(box.pointMin.getZ(), p.getZ()));
    result.pointMax = new Point3D(max(box.pointMax.getX(), p.getX()),
                                  max(box.pointMax.getY(), p.getY()),
                                  max(box.pointMax.getZ(), p.getZ()));
    return result;
  }

  public static BoundingBox union(BoundingBox box1, BoundingBox box2) {
    BoundingBox result = new BoundingBox();
    result.pointMin = new Point3D(min(box1.pointMin.getX(), box2.pointMin.getX()),
                                  min(box1.pointMin.getY(), box2.pointMin.getY()),
                                  min(box1.pointMin.getZ(), box2.pointMin.getZ()));
    result.pointMax = new Point3D(max(box1.pointMax.getX(), box2.pointMax.getX()),
                                  max(box1.pointMax.getY(), box2.pointMax.getY()),
                                  max(box1.pointMax.getZ(), box2.pointMax.getZ()));
    return result;
  }

  protected BoundingBox() {
    this.pointMin = new Point3D(POSITIVE_INFINITY, POSITIVE_INFINITY, POSITIVE_INFINITY);
    this.pointMax = new Point3D(NEGATIVE_INFINITY, NEGATIVE_INFINITY, NEGATIVE_INFINITY);
  }

  public BoundingBox(Point3D point) {
    this.pointMin = point;
    this.pointMax = point;
  }

  public BoundingBox(Point3D p1, Point3D p2) {
    this.pointMin = new Point3D(min(p1.getX(), p2.getX()),
                                min(p1.getY(), p2.getY()),
                                min(p1.getZ(), p2.getZ()));
    this.pointMax = new Point3D(max(p1.getX(), p2.getX()),
                                max(p1.getY(), p2.getY()),
                                max(p1.getZ(), p2.getZ()));
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this.getClass()).add("pMin", this.pointMin).add("pMax", this.pointMax).toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.getClass(), this.pointMin, this.pointMax);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass().isAssignableFrom(obj.getClass())) {
      final BoundingBox box = this.getClass().cast(obj);
      return Objects.equal(this.pointMin, box.pointMin) && Objects.equal(this.pointMax, box.pointMax);
    }
    return false;
  }

  public boolean isInside(Point3D p) {
    return this.pointMin.getX() <= p.getX() && this.pointMax.getX() >= p.getX()
        && this.pointMin.getY() <= p.getY() && this.pointMax.getY() >= p.getY()
        && this.pointMin.getZ() <= p.getZ() && this.pointMax.getZ() >= p.getZ();
  }

  public boolean overlaps(BoundingBox box) {
    return this.pointMin.getX() <= box.pointMin.getX() && this.pointMax.getX() >= box.pointMax.getX()
        && this.pointMin.getY() <= box.pointMin.getY() && this.pointMax.getY() >= box.pointMax.getY()
        && this.pointMin.getZ() <= box.pointMin.getZ() && this.pointMax.getZ() >= box.pointMax.getZ();
  }

  public boolean isHitBy(Ray ray) {
    throw new RuntimeException("Not implemented yet");
  }
}
