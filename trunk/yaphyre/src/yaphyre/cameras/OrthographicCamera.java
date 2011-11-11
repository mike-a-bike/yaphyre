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

import yaphyre.core.Cameras;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Transformation;
import yaphyre.geometry.Vector3D;

import com.google.common.base.Preconditions;

/**
 * A very simple camera showing an orthographic view of the scene to render.
 * 
 * @version $Revision: 42 $
 * 
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 */
public class OrthographicCamera extends AbstractCamera implements Cameras {

  private static final Vector3D UP = Vector3D.Y;

  private static final Vector3D RIGHT = Vector3D.X;

  private final double viewPlaneWidth;

  private final double viewPlaneWidthStart;

  private final double viewPlaneHeight;

  private final double viewPlaneHeightStart;

  public OrthographicCamera(BaseCameraSettings baseSettings, OrthographicCameraSettings orthographicSettings) {

    super(baseSettings);

    this.viewPlaneWidth = orthographicSettings.getViewPlaneWidth();
    this.viewPlaneWidthStart = -(this.viewPlaneWidth / 2d);
    this.viewPlaneHeight = orthographicSettings.getViewPlaneHeight();
    this.viewPlaneHeightStart = -(this.viewPlaneHeight / 2d);

    initTransformations();
  }

  @Override
  public Ray getCameraRay(Point2D viewPlanePoint) {
    Preconditions.checkArgument(viewPlanePoint.getU() >= 0d && viewPlanePoint.getU() <= 1d);
    Preconditions.checkArgument(viewPlanePoint.getV() >= 0d && viewPlanePoint.getV() <= 1d);

    Point2D mappedPoint = mapViewPlanePoint(viewPlanePoint);
    Ray result = new Ray(new Point3D(mappedPoint.getU(), mappedPoint.getV(), 0), Vector3D.Z);
    result = super.getCamera2World().transform(result);

    return result;
  }

  private void initTransformations() {
    Transformation lookAt = Transformation.lookAt(super.getPosition(), super.getDirection().asVector(), UP, RIGHT);
    setWorld2Camera(lookAt);
    setCamera2World(lookAt.inverse());
  }

  /**
   * Map the view plane point onto a concrete coordinate on this cameras width
   * and height rectangle.
   * 
   * @param viewPlanePoint
   *          The point to map (u, v &isin; [0, 1])
   * 
   * @return A point which lies on the view plane rectangle (u &isin; [-width/2,
   *         +width/2] and v &isin; [-height/2, height/2])
   */
  private Point2D mapViewPlanePoint(Point2D viewPlanePoint) {
    double mappedU = this.viewPlaneWidthStart + this.viewPlaneWidth * viewPlanePoint.getU();
    double mappedV = this.viewPlaneHeightStart + this.viewPlaneHeight * viewPlanePoint.getV();
    return new Point2D(mappedU, mappedV);
  }

  /**
   * Parameter class for the initialization of the {@link OrthographicCamera}.
   * 
   * @version $Revision: 42 $
   * 
   * @author Michael Bieri
   * @author $LastChangedBy: mike0041@gmail.com $
   */
  public static class OrthographicCameraSettings {

    private final double viewPlaneWidth;

    private final double viewPlaneHeight;

    public static OrthographicCameraSettings create(double viewPlaneWidth, double viewPlaneHeight) {
      return new OrthographicCameraSettings(viewPlaneWidth, viewPlaneHeight);
    }

    private OrthographicCameraSettings(double viewPlaneWidth, double viewPlaneHeight) {
      this.viewPlaneWidth = viewPlaneWidth;
      this.viewPlaneHeight = viewPlaneHeight;
    }

    public double getViewPlaneWidth() {
      return this.viewPlaneWidth;
    }

    public double getViewPlaneHeight() {
      return this.viewPlaneHeight;
    }
  }
}
