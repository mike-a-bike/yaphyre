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
package yaphyre.cameras;

import java.text.MessageFormat;

import yaphyre.core.Camera;
import yaphyre.core.Film;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Vector3D;

import com.google.common.base.Preconditions;

/**
 * Emulate an optimal pin hole camera. This means, that the hole is
 * infinitesimal small and has no physical size whatsoever. So no effect like
 * depth of field and/or aperture size, and forms are modeled here.
 * 
 * @param <F>
 *          The type of {@link Film} which is used by this instance.
 * 
 * @version $Revision: 42 $
 * 
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 */
public class PerspectiveCamera<F extends Film> extends AbstractCamera<F> implements Camera<F> {

  private final PerspectiveCameraSettings cameraSettings;

  private final Point3D focalPoint;

  public PerspectiveCamera(BaseCameraSettings<F> baseSettings, PerspectiveCameraSettings perspectiveSettings) {
    super(baseSettings);
    this.cameraSettings = perspectiveSettings;
    this.focalPoint = new Point3D(0, 0, -this.cameraSettings.getFocalLength());
  }

  @Override
  public Ray getCameraRay(Point2D viewPlanePoint) {
    Preconditions.checkArgument(viewPlanePoint.getU() >= 0d && viewPlanePoint.getU() <= 1d);
    Preconditions.checkArgument(viewPlanePoint.getV() >= 0d && viewPlanePoint.getV() <= 1d);

    Point3D mappedPoint = mapViewPlanePoint(viewPlanePoint);
    Vector3D direction = new Vector3D(this.focalPoint, mappedPoint).normalize();
    Ray result = new Ray(mappedPoint, direction);

    result = super.getCamera2World().transform(result);

    return result;
  }

  private Point3D mapViewPlanePoint(Point2D viewPlanePoint) {
    double u = viewPlanePoint.getU() - 0.5d;
    double v = (viewPlanePoint.getV() - 0.5d) * this.cameraSettings.getAspectRatio();
    return new Point3D(viewPlanePoint.getU() - 0.5d, viewPlanePoint.getV() - 0.5d, 0d);
  }

  @Override
  public String toString() {
    return MessageFormat.format("{0} [pos:{1}, lookAt:{2}, apect:{3}, focal:{4}, film:{5}]",
                                this.getClass().getSimpleName(),
                                super.getPosition(),
                                super.getLookAt(),
                                String.valueOf(this.cameraSettings.getAspectRatio()),
                                String.valueOf(this.cameraSettings.getFocalLength()),
                                super.getFilm());
  }

  /**
   * Parameter class for the initialization of the {@link PerspectiveCamera}.
   * 
   * @version $Revision: 42 $
   * 
   * @author Michael Bieri
   * @author $LastChangedBy: mike0041@gmail.com $
   */
  public static class PerspectiveCameraSettings {
    private final double aspectRatio;

    private final double focalLength;

    public static PerspectiveCameraSettings create(double aspectRatio, double focalLength) {
      return new PerspectiveCameraSettings(aspectRatio, focalLength);
    }

    private PerspectiveCameraSettings(double aspectRatio, double focalLength) {
      this.aspectRatio = aspectRatio;
      this.focalLength = focalLength;
    }

    public double getAspectRatio() {
      return this.aspectRatio;
    }

    public double getFocalLength() {
      return this.focalLength;
    }
  }

}
