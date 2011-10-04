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
package yaphyre.shapes;

import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.shaders.Shaders;
import yaphyre.util.IdentifiableObjects;

/**
 * Interface implemented by all {@link Shapes} of the rendering system.
 * 
 * @version $Revision$
 * 
 * @author Michael Bieri
 * @author $LastChangedBy$
 */
public interface Shapes extends IdentifiableObjects {

  public static final double NO_INTERSECTION = Double.POSITIVE_INFINITY;

  public Shaders getShader();

  public double getIntersectDistance(Ray ray);

  public Point3D getIntersectionPoint(Ray ray);

  public Normal3D getNormal(Point3D surfacePoint);

  public Point2D getMappedSurfacePoint(Point3D surfacePoint);

  public boolean throwsShadow();

}
