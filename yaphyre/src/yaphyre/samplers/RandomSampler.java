package yaphyre.samplers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import yaphyre.geometry.Point2D;

public class RandomSampler extends AbstractSampler {

  private static final Random random = new Random(new Date().getTime());

  public RandomSampler(int numberOfSamples) {
    super(numberOfSamples);
  }

  @Override
  protected List<Point2D> createSamples(int numberOfSamples) {
    List<Point2D> samples = new ArrayList<Point2D>(numberOfSamples);

    while (samples.size() < numberOfSamples) {
      samples.add(new Point2D(random.nextDouble(), random.nextDouble()));
    }

    return samples;
  }

}
