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

import static com.google.common.base.Preconditions.checkNotNull;
import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Transformation;
import yaphyre.shaders.Shaders;

/**
 * This shape is special since it does not represent a shape itself. It is a
 * wrapper around another shape which is transformed by a defined
 * transformation. This is used when complicated objects which use a lot of
 * memory should be used multiple times in a scene.<br/>
 * All methods just transform the {@link Point3D}, {@link Ray} or
 * {@link Normal3D} instances using the given transformation in order to
 * calculate the required informations.
 * 
 * @version $Revision: 66 $
 * 
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 */
public class Instance extends AbstractShape implements Shapes {

  private final Shapes baseShape;

  private final Transformation instanceTransformation;

  public Instance(String id, Shapes baseShape, Transformation instanceTransformation, Shaders shader, boolean throwsShadow) {
    super(id, shader, throwsShadow);
    checkNotNull(baseShape);
    checkNotNull(instanceTransformation);
    this.baseShape = baseShape;
    this.instanceTransformation = instanceTransformation;
  }

  @Override
  public double getIntersectDistance(Ray ray) {
    Ray transformedRay = this.instanceTransformation.transformInverse(ray);
    return this.baseShape.getIntersectDistance(transformedRay);
  }

  @Override
  public Point2D getMappedSurfacePoint(Point3D surfacePoint) {
    Point3D transformedSurfacePoint = this.instanceTransformation.transformInverse(surfacePoint);
    return this.baseShape.getMappedSurfacePoint(transformedSurfacePoint);
  }

  @Override
  public Normal3D getNormal(Point3D surfacePoint) {
    Point3D transformedSurfacePoint = this.instanceTransformation.transformInverse(surfacePoint);
    Normal3D transformedNormal = this.baseShape.getNormal(transformedSurfacePoint);
    return this.instanceTransformation.transform(transformedNormal);
  }

}
