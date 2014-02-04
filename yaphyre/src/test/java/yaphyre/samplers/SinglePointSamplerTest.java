package yaphyre.samplers;

import org.junit.Assert;
import org.junit.Test;
import yaphyre.core.Sampler;

/**
 * YaPhyRe
 *
 * @author axmbi03
 * @since 04.02.14
 */
public class SinglePointSamplerTest {
    @Test
    public void testGetNextSample() throws Exception {
        Sampler sampler = new SinglePointSampler();
        Assert.assertEquals(0.5d, sampler.getNextSample(), 0d);
        Assert.assertEquals(0.5d, sampler.getNextSample(), 0d);
    }

    @Test
    public void testIterator() throws Exception {
        Sampler sampler = new SinglePointSampler();
        for (double sample : sampler) {
            Assert.assertEquals(0.5d, sample, 0d);
        }
    }
}
