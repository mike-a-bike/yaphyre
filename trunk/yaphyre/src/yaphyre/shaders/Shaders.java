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
package yaphyre.shaders;

import yaphyre.geometry.Point2D;
import yaphyre.util.Color;
import yaphyre.util.IdentifiableObjects;

/**
 * The common interface for all shading activities. It provides two method. One
 * for accessing the material at the given u- v- coordinates. And another for
 * accessing the material properties at the given coordinates.
 * 
 * @version $Revision$
 * 
 * @author Michael Bieri
 * @author $LastChangedBy$
 */
public interface Shaders extends IdentifiableObjects {

  public Color getColor(Point2D uvCoordinate);

  public Material getMaterial(Point2D uvCoordinate);

}