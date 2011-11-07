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
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Ray;

/**
 * Emulate an optimal pin hole camera. This means, that the hole is
 * infinitesimal small and has no physical size whatsoever. So no effect like
 * depth of field and/or aperture size, and forms are modeled here.
 * 
 * @version $Revision: 42 $
 * 
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 */
public class PerspectiveCamera implements Cameras {

  @Override
  public Ray getCameraRay(Point2D viewPlanePoint) {
    throw new RuntimeException("Not implemented yet");
  }

  @Override
  public Films getFilm() {
    throw new RuntimeException("Not implemented yet");
  }

}
