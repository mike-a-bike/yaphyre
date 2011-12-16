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
import yaphyre.core.Sampler;
import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Transformation;
import yaphyre.geometry.Vector3D;
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

  private static final String TO_STRING_FORMAT = "AreaLight[{0}, {1}, {2}, {3}, {4}, {5}]";

  private final Normal3D normal;

  private final double size;

  private final double intensity;

  private final Sampler sampler;

  private final Shape shape;

  private final Transformation lightToWorld;

  private final Transformation samplerToLight;

  public AreaLight(Point3D position, Color color, Falloff falloff, double intensity, Normal3D normal, double size, Sampler sampler, Shape shape) {
    super(position, color, falloff);
    this.normal = normal;
    this.size = size;
    this.intensity = intensity;
    this.sampler = sampler;
    this.shape = shape;
    this.lightToWorld = Transformation.lookAt(position, position.add(normal.asVector()), Vector3D.Y).inverse();
    this.samplerToLight = Transformation.scale(size, size, size).mul(Transformation.translate(-0.5, -0.5, 0));
  }

  @Override
  public String toString() {
    return MessageFormat.format(TO_STRING_FORMAT, getPosition(), this.normal, this.size, getColor(), this.intensity, this.sampler);
  }

  @Override
  public double getIntensity(Point3D point, Scene scene) {
    int sampleCount = 0;
    int rayCount = 0;
    double distance = this.getPosition().sub(point).length();
    for (Point2D lightSample : this.getLightSamples()) {
      sampleCount++;
      lightSample = this.samplerToLight.transform(lightSample);
      Point3D lightPoint = this.lightToWorld.transform(new Point3D(lightSample.getU(), lightSample.getV(), 0));
      CollisionInformation shadowCollision = super.calculateVisibility(lightPoint, point, scene);
      if (shadowCollision == null) {
        rayCount++;
      }
    }
    assert sampleCount > 0 : "Empty sample count!";
    return super.getFalloff().getIntensity(this.intensity * ((double)rayCount) / ((double)sampleCount), distance);
  }

  public Iterable<Point2D> getLightSamples() {
    switch (this.shape) {
      case Square:
        return this.sampler.getUnitSquareSamples();
      case Disc:
        return this.sampler.getUnitCircleSamples();
    }
    throw new RuntimeException("Unknown light shape");
  }

  public static enum Shape {
    Square, Disc;
  }

}
