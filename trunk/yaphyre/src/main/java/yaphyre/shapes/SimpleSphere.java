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
import yaphyre.math.BoundingBox;
import yaphyre.math.Normal3D;
import yaphyre.math.Point2D;
import yaphyre.math.Point3D;
import yaphyre.math.Ray;
import yaphyre.math.Solver;
import yaphyre.math.Transformation;
import yaphyre.math.Vector3D;

/**
 * YaPhyRe
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
     * Initialize the common fields for all {@link yaphyre.core.Shape}s. Each {@link yaphyre.core.Shape} defines a
     * point of origin for its own, which is translated to the world coordinate space using the given transformation.
     * {@link yaphyre.math.Ray}s are translated by the inverse of the {@link yaphyre.math.Transformation} to calculate
     * an eventual intersection.</br>
     * Please remember, that the order of the {@link yaphyre.math.Transformation} matters. It is not the same if
     * the object is rotated an then translated or first translated and then rotated.
     *
     * @param objectToWorld The {@link yaphyre.math.Transformation} used to map world coordinates to object coordinates.
     * @param shader The {@link yaphyre.core.Shader} instance to use when rendering this {@link yaphyre.core.Shape}.
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
    public Optional<CollisionInformation> intersect(@Nonnull Ray ray) {
        final Ray objectSpaceRay = transformToObjectSpace(ray);
        final Vector3D originPositionVector = objectSpaceRay.getOrigin().asVector();
        final Vector3D direction = objectSpaceRay.getDirection();

        final double c2 = direction.dot(direction);
        final double c1 = originPositionVector.dot(direction) * 2;
        final double c0 = originPositionVector.dot(originPositionVector) - 1; // 1 = radius^2

        final double[] solutions = Solver.Quadratic.solve(c0, c1, c2);

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

    private Point2D mapToLocalUV(Point3D surfacePoint) {
        return null;
    }

}
