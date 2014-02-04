package yaphyre.samplers;

import yaphyre.core.Sampler;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Simple sampler. It only returns one value (0.5) which represents the mean value of the range (0-1).
 *
 * @author axmbi03
 * @since 04.02.14
 */
public class SinglePointSampler implements Sampler {

    /**
     * Returns the same value over and over again. 0.5 which represents the mean value of (0,1)
     *
     * @return 0.5 representing the mean value.
     */
    @Override
    public double getNextSample() {
        return 0.5d;
    }

    /**
     * Create an {@link java.util.Iterator} to be used in for-each loops. Since this sampler contains only
     * one sample, so does the iterator.
     *
     * @return An {@link java.util.Iterator} for the single sample.
     */
    @Override
    public Iterator<Double> iterator() {
        return Arrays.asList(0.5d).iterator();
    }
}
