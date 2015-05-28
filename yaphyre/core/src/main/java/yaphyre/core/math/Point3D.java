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

package yaphyre.core.math;

import java.io.Serializable;
import javax.annotation.concurrent.Immutable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import static org.apache.commons.math3.util.FastMath.PI;
import static org.apache.commons.math3.util.FastMath.acos;
import static org.apache.commons.math3.util.FastMath.atan2;
import static yaphyre.core.math.MathUtils.isZero;

/**
 * Abstraction of a point in a 3d Cartesian coordinate system.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 37 $
 */
@Immutable
@SuppressWarnings("PackageVisibleField")
public class Point3D implements Serializable {

    private static final long serialVersionUID = -5727406094615817485L;

    public static final Point3D ORIGIN = new Point3D(0, 0, 0);

    final double x;
    final double y;
    final double z;

    double[] polarCoordinates;

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        polarCoordinates = null;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("x", x).add("y", y).add("z", z).toString();
    }

    public Vector3D asVector() {
        return new Vector3D(x, y, z);
    }

    public Normal3D asNormal() {
        return new Normal3D(x, y, z);
    }

    public Point3D add(Point3D p) {
        return new Point3D(x + p.x, y + p.y, z + p.z);
    }

    public Point3D add(Point2D p) {
        return new Point3D(x + p.u, y + p.v, z);
    }

    public Point3D add(Vector3D v) {
        return new Point3D(x + v.x, y + v.y, z + v.z);
    }

    public Point3D add(Normal3D n) {
        return new Point3D(x + n.x, y + n.y, z + n.z);
    }

    public Point3D sub(Vector3D v) {
        return new Point3D(x - v.x, y - v.y, z - v.z);
    }

    public Vector3D sub(Point3D p) {
        return new Vector3D(x - p.x, y - p.y, z - p.z);
    }

    public double length() {
        return MathUtils.calcLength(x, y, z);
    }

    public double lengthSquared() {
        return MathUtils.calculateLengthSquared(x, y, z);
    }

    public Point3D scale(double s) {
        return new Point3D(x * s, y * s, z * s);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getClass(), x, y, z);
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
        return MathUtils.equalsWithTolerance(x, other.x) &&
                MathUtils.equalsWithTolerance(y, other.y) &&
                MathUtils.equalsWithTolerance(z, other.z);
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

    /**
     * Length part of the polar coordinates. Range: (0, oo)
     */
    public double getR() {
        return toPolar()[0];
    }

    /**
     * Phi value of the polar coordinates. Range: [0, 2PI)
     */
    public double getPhi() {
        return toPolar()[1];
    }

    /**
     * Theta value of the polar coordinates. Range: [0, PI)
     */
    public double getTheta() {
        return toPolar()[2];
    }

    /**
     * Polar coordinate representation with the following value ranges: r (0, oo), phi [0, 2PI), theta [0, PI)
     *
     * @return An array {r, phi, theta}
     */
    private double[] toPolar() {
        if (polarCoordinates == null) {
            if (isZero(lengthSquared())) {
                polarCoordinates = new double[]{0d, 0d, 0d};
            } else {
                double r = length();
                double phi = (atan2(y, x) + PI);
                double theta = acos(z / r);
                polarCoordinates = new double[]{r, phi, theta};
            }
        }
        return polarCoordinates;
    }
}
