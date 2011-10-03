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
import java.util.List;

import yaphyre.geometry.Point2D;

/**
 * This is the common interface for different sampling methods.
 * 
 * @version $Revision: 46 $
 * 
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 */
public class RegularSampler extends AbstractSampler {

  public RegularSampler(int numberOfSamples) {
    super(numberOfSamples);
  }

  /**
   * This implementation creates a regular grid with a sample in the middle of
   * each square.
   */
  @Override
  protected List<Point2D> createSamples(int numberOfSamples) {
    int samplesPerSide = (int)Math.sqrt(numberOfSamples);
    List<Point2D> samples = new ArrayList<Point2D>(samplesPerSide * samplesPerSide);
    double step = 1d / samplesPerSide;
    double halfStep = step / 2d;
    for (int y = 0; y < samplesPerSide; y++) {
      for (int x = 0; x < samplesPerSide; x++) {
        samples.add(new Point2D(halfStep + x * step, halfStep + y * step));
      }
    }
    return samples;
  }

}
