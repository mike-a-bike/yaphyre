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
import yaphyre.core.Films;
import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Transformation;

/**
 * A very simple camera showing an orthographic view of the scene to render.
 * 
 * @version $Revision: 42 $
 * 
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 */
public class OrthographicCamera implements Cameras {

  private final double viewPlaneWidth;

  private final double viewPlaneHeight;

  private final Transformation world2Camera;

  public OrthographicCamera(Point3D position, Normal3D direction, double viewPlaneWidth, double viewPlaneHeight) {
    this.world2Camera = null;
    this.viewPlaneWidth = viewPlaneWidth;
    this.viewPlaneHeight = viewPlaneHeight;
  }

  @Override
  public Ray getCameraRay(Point2D viewPlanePoint) {
    throw new RuntimeException("Not implemented yet");
  }

  @Override
  public Films getFilm() {
    throw new RuntimeException("Not implemented yet");
  }

}
