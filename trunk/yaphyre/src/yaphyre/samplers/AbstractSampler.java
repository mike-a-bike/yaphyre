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

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static yaphyre.math.MathUtils.TWO_PI;

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

  private List<List<Point2D>> sampleSets;

  /**
   * Protected empty constructor. This can only be called from subclasses, since
   * there is no initialization whatsoever. Derived classes calling this
   * constructor must override the {@link #getUnitSquareSamples()} method or
   * call the {@link #createSamples(int)} method.
   */
  protected AbstractSampler() {

  }

  /**
   * Default constructor used by most of the derived classes. It takes a number
   * of samples to create. The derived {@link #createSamples(int)} method
   * handles the actual creation of the samples.
   * 
   * @param numberOfSamples
   *          The number of samples requested. Please notice, the number of
   *          effective samples may vary according to the implemented algorithm.
   */
  public AbstractSampler(int numberOfSamples) {
    createSampleSets(numberOfSamples);
  }

  /**
   * Initializes the internal list of different sample sets.
   * 
   * @param numberOfSamples
   *          The number of sets to create.
   */
  protected void createSampleSets(int numberOfSamples) {
    LOGGER.debug("Start creation of samples for {}", this.getClass().getSimpleName());
    this.sampleSets = new ArrayList<List<Point2D>>(NUMBER_OF_SAMPLE_SETS);
    for (int set = 0; set < NUMBER_OF_SAMPLE_SETS; set++) {
      this.sampleSets.add(createSamples(numberOfSamples));
    }
    LOGGER.debug("Sample creation finished");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void shuffle() {
    Collections.shuffle(this.sampleSets, random);
  }

  /**
   * Gets a set of samples within a unit square. This implementation chooses a
   * random set from the available sets in order to avoid aliasing artifacts
   * created by returning the same set over and over again.<br/> {@inheritDoc}
   */
  @Override
  public Iterable<Point2D> getUnitSquareSamples() {
    int setIndex = (int)random.nextFloat() * NUMBER_OF_SAMPLE_SETS;
    return this.sampleSets.get(setIndex);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<Point2D> getUnitCircleSamples() {

    double r, phi;
    Point2D sp;

    List<Point2D> result = new ArrayList<Point2D>();

    for (Point2D p : getUnitSquareSamples()) {
      sp = new Point2D(p.getU() * 2d - 1d, p.getV() * 2d - 2d);

      if (sp.getU() > -sp.getV()) {
        if (sp.getU() > sp.getV()) {
          r = sp.getU();
          phi = sp.getV() / sp.getU();
        } else {
          r = sp.getV();
          phi = 2d - sp.getU() / sp.getV();
        }
      } else {
        if (sp.getU() < sp.getV()) {
          r = -sp.getU();
          phi = 4 + sp.getV() / sp.getU();
        } else {
          r = -sp.getV();
          phi = (sp.getV() != 0d) ? 6d - sp.getU() / sp.getV() : 0d;
        }
      }

      phi *= PI / 4d;

      result.add(new Point2D(r * cos(phi), r * sin(phi)));

    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<Point3D> getHemisphereSamples(double exp) {
    List<Point3D> result = new ArrayList<Point3D>();
    for (Point2D p : getUnitSquareSamples()) {
      double cos_phi = cos(2d * PI * p.getU());
      double sin_phi = sin(2d * PI * p.getU());
      double cos_theta = pow((1d - p.getV()), 1d / (exp + 1d));
      double sin_theta = sqrt(1d - cos_theta * cos_theta);
      double pu = sin_theta * cos_phi;
      double pv = sin_theta * sin_phi;
      double pw = cos_theta;
      result.add(new Point3D(pu, pv, pw));
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<Point3D> getSphereSamples() {
    double r1, r2;
    double x, y, z;
    double r, phi;

    List<Point3D> result = new ArrayList<Point3D>();

    for (Point2D p : getUnitSquareSamples()) {
      r1 = p.getU();
      r2 = p.getV();
      z = 1d - 2d - r1;
      r = sqrt(1d - z * z);
      phi = TWO_PI * r2;
      x = r * cos(phi);
      y = r * sin(phi);
      result.add(new Point3D(x, y, z));
    }

    return result;
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