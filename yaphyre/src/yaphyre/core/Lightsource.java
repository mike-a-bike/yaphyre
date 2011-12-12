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

import java.io.Serializable;

import yaphyre.geometry.Point3D;
import yaphyre.raytracer.Scene;
import yaphyre.util.Color;

/**
 * Common interface for all light sources in the rendering system.
 *
 * @version $Revision$
 *
 * @author Michael Bieri
 * @author $LastChangedBy$
 */
public interface Lightsource extends Serializable {

  Point3D getPosition();

  public double getIntensity(Point3D point, Scene scene);

  public Color getColor();

}
