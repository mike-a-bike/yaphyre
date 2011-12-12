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

import java.text.MessageFormat;

import yaphyre.core.CollisionInformation;
import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point3D;
import yaphyre.raytracer.Scene;
import yaphyre.util.Color;

/**
 * Simple implementation of an area light. This type of light is not just a
 * single point in space which is either seen or not, but has actual physical
 * dimensions.<br/>
 * TODO: implement monte carlo light distribution
 *
 * @author Michael Bieri
 */
public class AreaLight extends AbstractLightsource {

  private static final long serialVersionUID = -3600533548259119486L;

  private static final String TO_STRING_FORMAT = "AreaLight[{0}, {1}, {2}x{2}, {4}x{4}, {5}, {3}]";

  private final Normal3D normal;

  private final double size;

  private final int samplesPerSide;

  private final int numberOfRays;

  private final double rayIntensity;

  private final double intensity;

  public AreaLight(Point3D position, Normal3D normal, double size, int samplesPerSide, double intensity, Color color, Falloff falloff) {
    super(position, color, falloff);
    this.normal = normal;
    this.size = size;
    this.samplesPerSide = samplesPerSide;
    this.numberOfRays = this.samplesPerSide * this.samplesPerSide;
    this.intensity = intensity;
    this.rayIntensity = this.intensity / this.numberOfRays;
  }

  @Override
  public String toString() {
    return MessageFormat.format(TO_STRING_FORMAT, getPosition(), this.normal, this.size, getColor(), this.samplesPerSide, this.intensity);
  }

  @Override
  public double getIntensity(Point3D point, Scene scene) {

    double accumulatedIntensity = 0d;

    for (Point3D origin : createOrigins()) {
      CollisionInformation shadowCollision = super.calculateVisibility(origin, point, scene);
      if (shadowCollision == null) {
        accumulatedIntensity += super.getFalloff().getIntensity(this.rayIntensity, origin.sub(point).length());
      }
    }

    return accumulatedIntensity;

  }

  /**
   * Calculates an array of origin points based on the distribution in a 1x1
   * unit rectangle which is the transformed into the scene coordinate space by
   * the lights position and direction.
   *
   * @return An array of {@link Point3D}, each representing a start point for a
   *         light ray.
   */
  private Point3D[] createOrigins() {
    throw new RuntimeException("Not implemented yet");
  }

}
