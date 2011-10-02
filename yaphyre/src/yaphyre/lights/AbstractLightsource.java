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
package yaphyre.lights;

import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Vector3D;
import yaphyre.math.MathUtils;
import yaphyre.raytracer.CollisionInformations;
import yaphyre.raytracer.Scene;
import yaphyre.util.Color;
import yaphyre.util.IdentifiableObject;
import yaphyre.util.RenderStatistics;

/**
 * Common interface for all light sources in the rendering system.
 * 
 * @version $Revision$
 * 
 * @author Michael Bieri
 * @author $LastChangedBy$
 */
public abstract class AbstractLightsource extends IdentifiableObject implements Lightsources {

  private final Point3D position;

  private final Color color;

  private final Falloff falloff;

  protected AbstractLightsource(String id, Point3D position, Color color, Falloff falloff) {
    super(id);
    this.position = position;
    this.color = color;
    this.falloff = falloff;
  }

  @Override
  public Point3D getPosition() {
    return this.position;
  }

  @Override
  public Color getColor() {
    return this.color;
  }

  protected Falloff getFalloff() {
    return this.falloff;
  }

  protected CollisionInformations calculateVisibility(Point3D lightPoint, Point3D surfacePoint, Scene scene) {
    RenderStatistics.incShadowRays();
    Vector3D lightVector = new Vector3D(lightPoint, surfacePoint);
    Vector3D lightDirection = lightVector.unitVector();
    Ray lightRay = new Ray(lightPoint, lightDirection);
    double maxCollisionDistance = lightVector.length();
    return scene.getCollidingShape(lightRay, maxCollisionDistance - MathUtils.EPSILON, true);
  }

}