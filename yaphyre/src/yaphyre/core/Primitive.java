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

import java.io.Serializable;

import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;

/**
 * Created with IntelliJ IDEA. User: michael Date: 16.02.13 Time: 12:41 To change this template use File | Settings | File
 * Templates.
 */
public interface Primitive extends Serializable {

	/** Constant for signaling that there is no intersection. */
	double NO_INTERSECTION = Double.POSITIVE_INFINITY;

	/**
	 * Calculate the distance in which the given {@link Ray} and this shape intersect. If no intersection occurs, {@link
	 * #NO_INTERSECTION} is returned.
	 *
	 * @param ray
	 * 		The {@link Ray} to calculate the intersection distance for.
	 *
	 * @return The distance in which the ray intersects this shape. {@link #NO_INTERSECTION} if there is no intersection.
	 */
	public double getIntersectDistance(Ray ray);

	/**
	 * Gets the {@link yaphyre.geometry.Normal3D} information at a given point on the surface of this shape.
	 *
	 *
	 * @param surfacePoint
	 * 		The {@link yaphyre.geometry.Point3D} on the surface of this shape.
	 *
	 * @return The {@link yaphyre.geometry.Normal3D} information at the location of the <code>surfacePoint</code>.
	 */
	public Normal3D getNormal(Point3D surfacePoint);

	/**
	 * Gets the mapping information of the given surface point. Each 3D object has a 2D surface with each point described
	 * by its u- and v- coordinates. The method calculates these coordinates for the given point on the surface.
	 *
	 * @param surfacePoint
	 * 		The point on the surface of this shape.
	 *
	 * @return A {@link Point2D} instance with the u- and v- coordinates.
	 */
	public Point2D getMappedSurfacePoint(Point3D surfacePoint);

}
