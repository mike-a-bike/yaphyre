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
import yaphyre.geometry.Point3D;
import yaphyre.raytracer.Scene;
import yaphyre.util.Color;

public class Pointlight extends AbstractLightsource {

  private static final long serialVersionUID = -1976888619913693137L;

  private final static String TO_STRING_FORMAT = "Pointlight[{0}, {1}, {2}, {3}]";

  private final double intensity;

  public Pointlight(Point3D position, Color color, double intensity, Falloff falloff) {
    super(position, color, falloff);
    this.intensity = intensity;
  }

  @Override
  public String toString() {
    return MessageFormat.format(TO_STRING_FORMAT, getPosition(), super.getColor(), this.intensity, getFalloff());
  }

  @Override
  public double getIntensity(Point3D point, Scene scene) {
    double intensity = 0d;
    CollisionInformation shadowCollision = calculateVisibility(getPosition(), point, scene);
    if (shadowCollision == null) {
      intensity = getFalloff().getIntensity(this.intensity, getPosition().sub(point).length());
    }
    return intensity;
  }
}
