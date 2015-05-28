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
import java.util.OptionalDouble;
import java.util.stream.DoubleStream;
import javax.annotation.Nonnull;

import yaphyre.core.api.CollisionInformation;
import yaphyre.core.api.Shader;
import yaphyre.core.math.BoundingBox;
import yaphyre.core.math.Normal3D;
import yaphyre.core.math.Point2D;
import yaphyre.core.math.Point3D;
import yaphyre.core.math.Ray;
import yaphyre.core.math.Solvers;
import yaphyre.core.math.Transformation;
import yaphyre.core.math.Vector3D;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Math.PI;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static yaphyre.core.math.MathUtils.EPSILON;
import static yaphyre.core.math.MathUtils.TWO_PI;
import static yaphyre.core.math.MathUtils.isInRangeWithTolerance;

/**
 * A sphere in the three dimensional space is defined as:<br/> (p - p<sub>0</sub>) &sdot; (p - p<sub>0</sub>) =
 * r<sup>2</sup><br/> with: <ul> <li>p<sub>0</sub>: center of the sphere</li> <li>p: point on the sphere</li> <li>r:
 * radius</li> </ul>
 *
 * @author Michael Bieri
 */
public class Sphere extends AbstractShape {

    /**
     * The radius of the unit sphere is 1.
     */
    private static final int RADIUS = 1;
    /**
     * The squared radius of the unit sphere is 1: 1^2 = 1
     */
    private static final int RADIUS_SQUARED = 1;
    /**
     * Range of the angle across the 'up'/'down' axis
     */
    private final double thetaMin, thetaMax;
    /**
     * Range of the angle rotating around the y axis.
     */
    private final double phiMin, phiMax;
    /**
     * Flag if the instance is a partial sphere or not
     */
    private final boolean isPartial;
    /**
     * Bounding box for the sphere
     */
    private final BoundingBox boundingBox;

    /**
     * Creates a unit sphere at the origin of the world coordinates. Changes in the position of the objects and the radius
     * are made by providing a transformation matrix. <ul> <li>Change the radius -> Use a scaling transformation.</li>
     * <li>Change the center -> Use a translation transformation.</li> <li>Make an ellipsoid -> Use a non uniform scaling
     * transformation</li> </ul>
     *
     * @param objectToWorld The {@link Transformation} to translate from object space into world space.
     * @param phiMin        The start angle for &phi; (&phi; &isin; [0, 360])
     * @param phiMax        The end angle for &phi; (&phi; &isin; [0, 360])
     * @param thetaMin      The start angle for &theta; (&theta; &isin; [0, 180])
     * @param thetaMax      The start angle for &theta; (&theta; &isin; [0, 180])
     */
    public Sphere(final Transformation objectToWorld,
                  final double phiMin, final double phiMax, final double thetaMin, final double thetaMax,
                  final Shader shader) {
        super(objectToWorld, shader);
        checkArgument(0d <= phiMin && phiMin <= 360d);
        checkArgument(0d <= phiMax && phiMax <= 360d);
        checkArgument(0d <= thetaMin && thetaMin <= 180d);
        checkArgument(0d <= thetaMax && thetaMax <= 180d);
        this.phiMin = min(phiMin, phiMax) / 360d * TWO_PI;
        this.phiMax = max(phiMin, phiMax) / 360d * TWO_PI;
        this.thetaMin = min(thetaMin, thetaMax) / 180d * PI;
        this.thetaMax = max(thetaMin, thetaMax) / 180d * PI;
        isPartial = phiMin == 0d && phiMax == 360d && thetaMin == 0d && thetaMax == 180d;
        boundingBox = objectToWorld.transform(new BoundingBox(new Point3D(1, 1, 1), new Point3D(-1, -1, -1)));
    }

    /**
     * Helper method for creating a sphere where the center and its radius are known. This creates a transformation which
     * scales the unit sphere by the size of the given radius and translates it to the given coordinates. So, the
     * resulting matrix looks like this: <br/> [[r 0 0 c<sub>x</sub>] [0 r 0 c<sub>y</sub>] [0 0 r c<sub>z</sub>] [0 0 0
     * 1]]
     *
     * @param center The center of the sphere (c<sub>x, y, z</sub>)
     * @param radius Its radius (r)
     * @param shader The {@link yaphyre.core.api.Shader} to use for rendering
     * @return A new instance of {@link Sphere}.
     * @throws NullPointerException     If either <code>center</code> or <code>shader</code> are <code>null</code>, a {@link NullPointerException} is
     *                                  thrown.
     * @throws IllegalArgumentException If <code>radius</code> is too small, an {@link IllegalArgumentException} is thrown.
     */
    public static Sphere createSphere(Point3D center, double radius, Shader shader) {
        return createSphere(center, radius, 0d, 360d, 0d, 180d, shader);
    }

    /**
     * Helper method for creating a sphere where the center and its radius are known. This creates a transformation which
     * scales the unit sphere by the size of the given radius and translates it to the given coordinates. So, the
     * resulting matrix looks like this: <br/> [[r 0 0 c<sub>x</sub>] [0 r 0 c<sub>y</sub>] [0 0 r c<sub>z</sub>] [0 0 0
     * 1]]
     * In addition, the angular ranges for &theta; and &phi; can be provided. Both ranges take values between 0 and 360
     * degree.
     *
     * @param center   The center of the sphere (c<sub>x</sub>, c<sub>y</sub>, c<sub>z</sub>)
     * @param radius   Its radius (r)
     * @param phiMin   The start angle for &phi; (&phi; &isin; [0, 360])
     * @param phiMax   The end angle for &phi; (&phi; &isin; [0, 360])
     * @param thetaMin The start angle for &theta; (&theta; &isin; [0, 180])
     * @param thetaMax The start angle for &theta; (&theta; &isin; [0, 180])
     * @param shader   The {@link yaphyre.core.api.Shader} to use for rendering
     * @return A new instance of {@link Sphere}.
     * @throws NullPointerException     If either <code>center</code> or <code>shader</code> are <code>null</code>, a {@link NullPointerException} is
     *                                  thrown.
     * @throws IllegalArgumentException If <code>radius</code> is too small, an {@link IllegalArgumentException} is thrown. Or if the specified
     *                                  ranges for &theta; and &phi; are out of bounds.
     */
    public static Sphere createSphere(Point3D center, double radius,
                                      double phiMin, double phiMax, double thetaMin, double thetaMax,
                                      Shader shader) {
        checkArgument(radius > EPSILON);
        Transformation scaling = Transformation.scale(radius, radius, radius);
        Transformation translation = Transformation.translate(center.getX(), center.getY(), center.getZ());
        Transformation objectToWorld = translation.mul(scaling);
        return new Sphere(objectToWorld, phiMin, phiMax, thetaMin, thetaMax, shader);
    }

    @Override
    public String toString() {
        return MessageFormat.format("Sphere[{0}]", super.getObjectToWorld().toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Sphere)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Nonnull
    @Override
    public Optional<CollisionInformation> intersect(@Nonnull final Ray ray) {
        OptionalDouble intersectDistance = this.getIntersectDistance(ray);
        if (!intersectDistance.isPresent()) {
            return Optional.empty();
        }

        Point3D intersectionPoint = ray.getPoint(intersectDistance.getAsDouble());

        return Optional.of(new CollisionInformation(ray, this,
                intersectDistance.getAsDouble(), intersectionPoint,
                getNormal(intersectionPoint), getMappedSurfacePoint(intersectionPoint)));
    }

    @Nonnull
    @Override
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    /**
     * Determine the distance on a half line where this line intersects with the sphere. To do this, we use the parametric
     * form of a line which is:<br/>
     * p(<em>t</em>) = p<sub>0</sub> + <em>t</em> * d<br/>
     * with
     * <ul>
     * <li>p(<em>t</em>): point on the line for the parameter value <em>t</em></li>
     * <li>p<sub>0</sub>: line start point</li>
     * <li>d: direction</li>
     * <li><em>t</em>: parameter value</li>
     * </ul>
     * We have to solve a quadratic equation:
     * c<sub>2</sub>*<em>t</em><sup>2</sup> + c<sub>1</sub>* <em>t</em> + c<sub>0</sub> = 0<br/>
     * Solutions:<br/>
     * <em>t</em><sub>0</sub> = (-c<sub>1</sub> - SQRT( c<sub>1</sub><sup>2</sup> - 4c<sub>2</sub>c<sub>0</sub>)) / 2c<sub>2</sub><br/>
     * <em>t</em><sub>1</sub> = (-c<sub>1</sub> + SQRT( c<sub>1</sub><sup>2</sup> - 4c<sub>2</sub>c<sub>0</sub>)) / 2c<sub>2</sub><br/>
     *
     * @param ray The {@link Ray} to intersect with this sphere.
     * @return The distance in which the ray intersects this sphere, or if they do not intersect {@link
     * java.util.OptionalDouble#EMPTY}.
     */
    OptionalDouble getIntersectDistance(Ray ray) {

        // Transform the incoming ray from the world space into the object space.
        final Ray objectRay = super.getWorldToObject().transform(ray);

        // Transform the origin of the ray into the object space of the sphere.
        Vector3D oc = objectRay.getOrigin().asVector();

        final double c2 = objectRay.getDirection().dot(objectRay.getDirection());
        final double c1 = 2 * oc.dot(objectRay.getDirection());
        final double c0 = oc.dot(oc) - RADIUS_SQUARED;

        final double[] solutions = Solvers.Quadratic.solve(c0, c1, c2);

        return DoubleStream.of(solutions)
                .filter(d -> objectRay.getTRange().contains(d) && (!isPartial || isInAngularRange(objectRay.getPoint(d))))
                .min();
    }

    /**
     * Calculate the normal for the given point. For a {@link Sphere} this is pretty simple: <ol> <li>Transform the
     * surface point into the object space</li> <li>Construct the vector connecting the origin of the sphere and the
     * surface point</li> <li>Transform the resulting normal back to world space</li> </ol>
     */
    Normal3D getNormal(Point3D surfacePoint) {
        surfacePoint = super.getWorldToObject().transform(surfacePoint);
        return super.getObjectToWorld().transform(surfacePoint.asNormal());
    }

    /**
     * Map the given point onto the <em>u</em>/<em>v</em> coordinates of the sphere. This is done by converting the
     * standard Cartesian coordinates into the polar representation and mapping the angles &theta; and &phi; to the
     * standard <em>u</em>/<em>v</em> range {u, v &isin; [0, 1]}<br/>
     * The definition is:
     * <ul>
     * <li><em>cos</em>(&theta;) = <em>z</em> / <em>r</em></li>
     * <li><em>tan</em>(&phi;) = <em>y</em> / <em>x</em></li>
     * </ul>
     * With &theta; &isin; [0, &pi;) and &phi; &isin; [0, 2&pi;)
     *
     * @throws NullPointerException     If <code>surfacePoint</code> is <code>null</code> a {@link NullPointerException} is thrown.
     * @throws IllegalArgumentException If <code>surfacePoint</code> does not lie on the surface of the sphere an {@link IllegalArgumentException} is
     *                                  thrown.
     */
    Point2D getMappedSurfacePoint(Point3D surfacePoint) throws NullPointerException, IllegalArgumentException {
        surfacePoint = super.getWorldToObject().transform(surfacePoint);
        // Make sure, that the point lies on the surface.
        checkNotNull(surfacePoint, "surfacePoint must not be null");
        checkArgument(Math.abs(surfacePoint.asVector().length()) - RADIUS <= EPSILON, "the point % does not lie on the surface of %", surfacePoint, this);

        Point2D mappedSurfacePoint = calculateAngularCoordinates(surfacePoint);

        // Map u and v to [0, 1]
        double u = (mappedSurfacePoint.getU() - phiMin) / (phiMax - phiMin);
        double v = (mappedSurfacePoint.getV() - thetaMin) / (thetaMax - thetaMin);

        return new Point2D(u, v);

    }

    private Point2D calculateAngularCoordinates(final Point3D surfacePoint) {

        // Calculate the two angles of the spherical coordinates
        double phi = atan2(surfacePoint.getZ(), surfacePoint.getX());
        phi = phi >= 0 ? phi : phi + TWO_PI;
        double theta = acos(surfacePoint.getY());

        return new Point2D(phi, theta);
    }

    private boolean isInAngularRange(Point3D p) {
        Point2D uvCoordinates = calculateAngularCoordinates(p);

        return isInRangeWithTolerance(phiMin, phiMax, uvCoordinates.getU())
                && isInRangeWithTolerance(thetaMin, thetaMax, uvCoordinates.getV());
    }

}
