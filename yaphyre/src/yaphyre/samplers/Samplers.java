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
package yaphyre.samplers;

import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;

/**
 * This is the common interface for different sampling methods.
 * 
 * @version $Revision: 46 $
 * 
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 */
public interface Samplers {

  /**
   * Shuffle the sets. Call this to avoid aliasing.
   */
  public void shuffle();

  /**
   * Gets a list of {@link Point2D} within a unit square. u = (0, 1) and v = (0,
   * 1).
   * 
   * @return An {@link Iterable} usable in loop to access all the samples.
   */
  public Iterable<Point2D> getUnitSquareSamples();

  /**
   * Gets a list of samples which all lie within a unit circle. A circle with
   * radius 1 and origin at [0, 0].
   * 
   * @return An {@link Iterable} usable in loop to access all the samples.
   */
  public Iterable<Point2D> getUnitCircleSamples();

  /**
   * Creates a collection of samples which lie on a hemisphere. These samples
   * are distributed according to a cosine function with the given exponent.
   * <code>exp</code> = 1 means that the samples lie on the hemisphere.
   * 
   * @param exp
   *          The exponent used for the cosine function.
   * 
   * @return An {@link Iterable} usable in loop to access all the samples.
   */
  public Iterable<Point3D> getHemisphereSamples(double exp);

  /**
   * Create a list of samples each lies on a unit sphere, which is a sphere with
   * the radius 1 and the origin = [0, 0]. This method is used mostly for the
   * creation of samples used to render spherical light sources.
   * 
   * @return An {@link Iterable} usable in loop to access all the samples.
   */
  public Iterable<Point3D> getSphereSamples();

}
