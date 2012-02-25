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
package yaphyre.core;

import yaphyre.geometry.Point2D;
import yaphyre.geometry.Ray;

/**
 * This is the common interface all cameras must implement.
 * 
 * @version $Revision: 42 $
 * 
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 */
public interface Camera {

  /**
   * Create a ray for the given u/v coordinates. Where <em>u</em> &isin; [0, 1]
   * and <em>v</em> &isin; [0, 1]. It is possible, that a camera returns
   * different samples for a given view plane point. This is used to simulate
   * depth of field effects by sampling the same view plane point using a non
   * zero lens size.
   * 
   * @param viewPlanePoint
   *          The {@link Point2D} to create the camera ray for.
   * 
   * @return An instance of {@link Ray} which corresponds with the given u/v
   *         position in the world space.
   */
  public Ray getCameraRay(Point2D viewPlanePoint);

  /**
   * Get the {@link Film} instance that is associated with the camera. This is
   * usually an image file, but can be something else as well.
   * 
   * @return The {@link Film} instance for the camera.
   * 
   * @see yaphyre.films.ImageFile
   */
  public Film getFilm();

  /**
   * Sets the {@link Film} instance for this camera.
   * 
   * @param film
   *          An instance of {@link Film}.
   */
  public void setFilm(Film film);

}
