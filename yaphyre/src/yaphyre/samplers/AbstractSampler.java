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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;

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

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSampler.class);

  /** random generator for choosing a set of samples. */
  private static final Random random = new Random(System.nanoTime());

  /** nice prime number... a very arbitrary chosen number */
  private static final int NUMBER_OF_SAMPLE_SETS = 97;

  private final List<List<Point2D>> sampleSets;

  public AbstractSampler(int numberOfSamples) {
    LOGGER.debug("Start creation of samples for {}", this.getClass().getSimpleName());
    this.sampleSets = new ArrayList<List<Point2D>>(NUMBER_OF_SAMPLE_SETS);
    for (int set = 0; set < NUMBER_OF_SAMPLE_SETS; set++) {
      this.sampleSets.add(this.createSamples(numberOfSamples));
    }
    LOGGER.debug("Sample creation finished");
  }

  @Override
  public void shuffle() {
    Collections.shuffle(this.sampleSets, random);
  }

  @Override
  public Iterable<Point2D> getUnitSquareSamples() {
    int setIndex = (int) random.nextFloat() * NUMBER_OF_SAMPLE_SETS;
    return this.sampleSets.get(setIndex);
  }

  @Override
  public Iterable<Point2D> getUnitCircleSamples() {
    throw new RuntimeException("Not implemented yet");
  }

  @Override
  public Iterable<Point3D> getHemisphereSamples() {
    throw new RuntimeException("Not implemented yet");
  }

  @Override
  public Iterable<Point3D> getSphereSamples() {
    throw new RuntimeException("Not implemented yet");
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
