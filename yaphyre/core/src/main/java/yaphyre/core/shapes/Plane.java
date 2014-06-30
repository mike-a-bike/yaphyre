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

package yaphyre.core.shapes;

import java.text.MessageFormat;
import java.util.Optional;
import javax.annotation.Nonnull;
import yaphyre.core.api.CollisionInformation;
import yaphyre.core.api.Shader;
import yaphyre.core.math.BoundingBox;
import yaphyre.core.math.Normal3D;
import yaphyre.core.math.Point2D;
import yaphyre.core.math.Point3D;
import yaphyre.core.math.Ray;
import yaphyre.core.math.Transformation;

/**
 * Plane represented by a point on the plane and the normal. Since on a plane the normal does not change, it does not
 * matter where on the plane the origin lies.<br/> The mathematical representation of a point on the plane is:<br/>
 * (p - p<sub>0</sub>) &sdot; n = 0<br/> with
 * <ul>
 * <li>p: the point on the plane</li>
 * <li>p<sub>0</sub>: the origin</li>
 * <li>n: the normal of the plane</li>
 * </ul>
 *
 * @author Michael Bieri
 * @author $LastChangedBy$
 * @version $Revision$
 */
public class Plane extends AbstractShape {

	private static final long serialVersionUID = 6001965721771220664L;
	private final Point3D origin;
	private final Normal3D normal;
	private final BoundingBox boundingBox;

	public Plane(Transformation planeToWorld, Shader shader) {
		super(planeToWorld, shader);
		origin = Point3D.ORIGIN;
		normal = Normal3D.NORMAL_Y;
		boundingBox = BoundingBox.INFINITE_BOUNDING_BOX;
	}

	@Override
	public String toString() {
		return MessageFormat.format("Plane[{0}]", super.getObjectToWorld());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Plane)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		return true;
	}

	@Nonnull
    @Override
	public Optional<CollisionInformation> intersect(@Nonnull final Ray ray) {
		final Optional<CollisionInformation> result;
		final double intersectionDistance = calculateIntersectDistance(ray);

		if (intersectionDistance == NO_INTERSECTION) {
			result = Optional.empty();
		} else {
			final Point3D intersectionPoint = ray.getPoint(intersectionDistance);
            result = Optional.of(new CollisionInformation(
                ray,
                this,
                intersectionDistance,
                intersectionPoint,
                getNormal(),
                getMappedSurfacePoint(intersectionPoint)));
        }

		return result;
	}

	@Nonnull
    @Override
	public BoundingBox getBoundingBox() {
		return BoundingBox.INFINITE_BOUNDING_BOX;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Intersect the plane with a ray. We use the parametric form of the line equation to determine the distance in which
	 * the line intersects this plane.<br/>
	 * Using the two equations:<br/>
	 * Plane: (p - p<sub>0</sub>) &sdot; n = 0<br/>
	 * Line: p(t) = l<sub>0</sub> + (t * d)<br/>
	 * we get:<br/>
	 * t = ((p<sub>0</sub> - l<sub>0</sub>) &sdot; n) / (d &sdot; n)<br/>
	 * If the line starts outside the plane and is parallel to it there is no intersection, then the denominator
	 * is zero and the numerator is non-zero.<br/>
	 * If the line starts on the plane and is parallel to it so every point on the line intersects with the plane, the
	 * denominator and the numerator are zero<br/>
	 * If the result is negative, the line intersects with the plane behind the origin of the ray, so there is no
	 * visible intersection.
	 *
	 * @param ray The {@link Ray} to intersect with this plane.
	 *
	 * @return The distance in which the ray intersects this plane or {@link AbstractShape#NO_INTERSECTION} if there is no
	 * intersection.
	 */
	private double calculateIntersectDistance(Ray ray) {
		ray = super.transformToObjectSpace(ray);
		double numerator = origin.sub(ray.getOrigin()).dot(normal);
		double denominator = ray.getDirection().dot(normal);

		if (numerator == 0 && denominator == 0) {
            // The ray starts on the plane and is parallel to the plane, so it
            // intersects everywhere.
            return ray.getTRange().lowerEndpoint();
        } else if (numerator != 0 && denominator == 0) {
			// The ray starts outside the plane and is parallel to the plane, so no
			// intersection, ever...
			return NO_INTERSECTION;
		}

		double distance = numerator / denominator;

		return ray.getTRange().contains(distance) ? distance : NO_INTERSECTION;

	}

	/**
	 * The normal of a plane is independent from the position on the plane, so always the defining normal is returned.
	 *
	 * @return The normal of the plane (position independent)
	 */
	private Normal3D getNormal() {
		return super.getObjectToWorld().transform(normal);
	}

	/**
	 * Maps the given point to the planes u/v coordinates. Since each surface point lies on the x/z plane, the y component
	 * can be ignored. So [u, v] = [x, z].
	 */
	private Point2D getMappedSurfacePoint(Point3D surfacePoint) {
		surfacePoint = super.getWorldToObject().transform(surfacePoint);
		return new Point2D(surfacePoint.getX(), surfacePoint.getZ());
	}

}
