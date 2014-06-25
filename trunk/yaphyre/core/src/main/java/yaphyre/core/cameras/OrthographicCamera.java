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

package yaphyre.core.cameras;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import yaphyre.core.api.Film;
import yaphyre.core.math.Point2D;
import yaphyre.core.math.Point3D;
import yaphyre.core.math.Ray;
import yaphyre.core.math.Vector3D;

/**
 * A simple orthographic camera located at the origin looking along the negative z axis. No transformation handling
 * is implemented yet. The camera uses some basic parameters to render the scene like: dimension and resolution
 *
 * @author Michael Bieri
 * @since 18.02.14
 */
public class OrthographicCamera extends AbstractCamera {

    /** The cameras size in the u dimension. */
    private final double uDimension;

    /** The cameras siz ein the v dimension. */
    private final double vDimension;

    /** The location of the camera on the z axis */
    private final double zCoordinate;

    /**
     * Creates a new orthographic camera with the given size. The resolution is taken from the used film.
     *
     * @param film The Film instance to use to record the image data.
     * @param uDimension The size of the sampling rectangle in the u direction.
     * @param vDimension The size of the sampling rectangle in the v direction.
     * @param zCoordinate The location of the camera on the z axis.
     */
    public OrthographicCamera(@Nonnull Film film,
                              @Nonnegative double uDimension, @Nonnegative double vDimension,
                              double zCoordinate) {
        super(film);
        this.uDimension = uDimension;
        this.vDimension = vDimension;
        this.zCoordinate = zCoordinate;
    }

    @Nonnull
    @Override
    protected Ray createCameraRay(@Nonnull Point2D samplePoint) {
        double u = transformSamplingPoint(samplePoint.getU(), uDimension);
        double v = transformSamplingPoint(samplePoint.getV(), vDimension);
        return new Ray(new Point3D(u, v, zCoordinate), Vector3D.Z.neg());
    }

    private double transformSamplingPoint(double samplePoint, double dimension) {
        return -(dimension / 2d) + (dimension * samplePoint);
    }

}
