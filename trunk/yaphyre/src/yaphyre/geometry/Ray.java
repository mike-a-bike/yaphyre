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
package yaphyre.geometry;

import static yaphyre.math.MathUtils.EPSILON;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * Simple implementation of a ray. These are used as seeing rays and shadow
 * rays.<br/>
 *
 * It is defined by an origin ({@link Point3D}) and a direction (
 * {@link Vector3D}). A point on the ray are represented in a parametric way so
 * that:
 *
 * <pre>
 * p(distance) = origin + distance * direction
 * </pre>
 *
 * @version $Revision$
 *
 * @author Michael Bieri
 * @author $LastChangedBy$
 */
public class Ray implements Serializable {

  private static final long serialVersionUID = 6349693380913182303L;

  private static final String TO_STRING_FORMAT = "Ray[o:{0}, d:{1}, t:[{2}, {3}]]";

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
    return MessageFormat.format(TO_STRING_FORMAT, this.origin, this.direction, this.mint, this.maxt);
  }

  public Point3D getPoint(double distance) {
    return this.origin.add(this.direction.scale(distance));
  }

  public Point3D getOrigin() {
    return this.origin;
  }

  public Vector3D getDirection() {
    return this.direction;
  }

  public double getMint() {
    return this.mint;
  }

  public double getMaxt() {
    return this.maxt;
  }

}
