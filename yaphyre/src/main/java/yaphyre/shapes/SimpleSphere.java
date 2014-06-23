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

package yaphyre.shapes;

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalDouble;
import javax.annotation.Nonnull;
import yaphyre.core.CollisionInformation;
import yaphyre.core.Shader;
import yaphyre.core.Shape;
import yaphyre.math.BoundingBox;
import yaphyre.math.Normal3D;
import yaphyre.math.Point2D;
import yaphyre.math.Point3D;
import yaphyre.math.Ray;
import yaphyre.math.Solver;
import yaphyre.math.Transformation;
import yaphyre.math.Vector3D;

import static yaphyre.math.MathUtils.INV_PI;
import static yaphyre.math.MathUtils.INV_TWO_PI;

/**
 * Like the name suggests, this is a very basic implementation of a sphere. It represents the unit sphere. So its radius
 * is one and its center is at the origin. Using transformations, simple changes are possible.
 *
 * @author axmbi03
 * @since 03.06.2014
 */
public class SimpleSphere extends AbstractShape {

    public static final Vector3D RADIUS_VECTOR = new Vector3D(1, 1, 1);

    private static final BoundingBox LOCAL_INSTANCE_BOUNDING_BOX;

    static {
        LOCAL_INSTANCE_BOUNDING_BOX = new BoundingBox(new Point3D(-1, -1, -1), new Point3D(1, 1, 1));
    }

    private final BoundingBox transformedLocalBoundingBox;
    private final BoundingBox axisAlignedBoundingBox;

    /**
     * Initialize the common fields for all {@link Shape}s. Each {@link Shape} defines a
     * point of origin for its own, which is translated to the world coordinate space using the given transformation.
     * {@link Ray}s are translated by the inverse of the {@link Transformation} to calculate
     * an eventual intersection.</br>
     * Please remember, that the order of the {@link Transformation} matters. It is not the same if
     * the object is rotated an then translated or first translated and then rotated.
     *
     * @param objectToWorld The {@link Transformation} used to map world coordinates to object coordinates.
     * @param shader The {@link Shader} instance to use when rendering this {@link Shape}.
     */
    public SimpleSphere(Transformation objectToWorld, Shader shader) {
        super(objectToWorld, shader);

        // pre-calculate bounding boxes
        transformedLocalBoundingBox = getObjectToWorld().transform(LOCAL_INSTANCE_BOUNDING_BOX);
        Point3D center = getObjectToWorld().transform(Point3D.ORIGIN);
        Vector3D radiusVector = getObjectToWorld().transform(RADIUS_VECTOR);
        axisAlignedBoundingBox = new BoundingBox(center.sub(radiusVector), center.add(radiusVector));
    }

    @Override
    @Nonnull
    public Optional<CollisionInformation> intersect(@Nonnull Ray ray) {
        final Ray objectSpaceRay = transformToObjectSpace(ray);
        final Vector3D originPositionVector = objectSpaceRay.getOrigin().asVector();
        final Vector3D direction = objectSpaceRay.getDirection();

        final double a = direction.dot(direction);
        final double b = originPositionVector.dot(direction) * 2;
        final double c = originPositionVector.dot(originPositionVector) - 1; // 1 = radius^2

        final double[] solutions = Solver.Quadratic.solve(c, b, a);

        final OptionalDouble minSolution = Arrays.stream(solutions)
            .filter(d -> ray.getTRange().contains(d))
            .min();

        if (minSolution.isPresent() && !Double.isNaN(minSolution.getAsDouble())) {
            final double distance = minSolution.getAsDouble();
            final Point3D intersectionPoint = objectSpaceRay.getPoint(distance);

            return Optional.of(new CollisionInformation(
                ray,
                this,
                distance,
                getObjectToWorld().transform(intersectionPoint),
                getObjectToWorld().transform(calculateNormal(intersectionPoint, objectSpaceRay)),
                mapToLocalUV(intersectionPoint)));

        } else {

            return Optional.empty();

        }

    }

    @Nonnull
    @Override
    public BoundingBox getBoundingBox() {
        return transformedLocalBoundingBox;
    }

    @Nonnull
    @Override
    public BoundingBox getAxisAlignedBoundingBox() {
        return axisAlignedBoundingBox;
    }

    private Normal3D calculateNormal(Point3D intersectionPoint, Ray intersectionRay) {
        Normal3D result = intersectionPoint.sub(Point3D.ORIGIN).asNormal();
        if (intersectionRay.getDirection().isSameDirection(result)) {
            return result.neg();
        }
        return result;
    }

    /**
     * Simple orthographic to spherical coordinate mapping. This represents the 2D-coordinates on the surface of this
     * sphere. Please note: There is no sanity check. so if the point is not on the surface of the sphere it is
     * simply projected onto its surface by not caring at all about this fact...
     *
     * @param surfacePoint A point on the surface.
     * @return The U/V coordinates of <code>surfacePoint</code>.
     */
    @Nonnull
    private Point2D mapToLocalUV(@Nonnull Point3D surfacePoint) {
        return new Point2D(surfacePoint.getPhi() * INV_TWO_PI, surfacePoint.getTheta() * INV_PI);
    }

}
