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

import java.util.Iterator;
import java.util.List;

import yaphyre.geometry.Point2D;

import com.google.common.base.Preconditions;

/**
 * An abstract implementation of the interface {@link Samplers}. This is used to
 * ensure that all samplers provide a similar constructor.
 * 
 * @version $Revision: 46 $
 * 
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 */
public abstract class AbstractSampler implements Samplers {

  private final List<Point2D> samplePoints;

  public AbstractSampler(int numberOfSamples) {
    this.samplePoints = createSamples(numberOfSamples);
  }

  @Override
  public int getSampleCount() {
    return this.samplePoints.size();
  }

  @Override
  public Point2D getSample(int sampleIndex) {
    Preconditions.checkElementIndex(sampleIndex, this.samplePoints.size());
    return this.samplePoints.get(sampleIndex);
  }

  @Override
  public Iterator<Point2D> iterator() {
    return this.samplePoints.iterator();
  }

  /**
   * This is where each algorithm creates its samples. This method must be
   * implemented for each sampler strategy.
   * 
   * @param numberOfSamples
   *          The number of samples which are requested.
   * 
   * @return The list of created samples. Each {@link Point2D} represents a
   *         sample on a unit square.
   */
  protected abstract List<Point2D> createSamples(int numberOfSamples);
}
