/*
 * Copyright 2014 Michael Bieri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yaphyre.core;

/**
 * Common interface for all samplers. The idea is that it can deliver continuously values within the range (0,1). If
 * all samples would be taken together, a perfect distribution would be achieved. This is of course not possible,
 * so most implementation have a limited number of samples (which may be pre-calculated) which represent an
 * approximation of a continuous stream of values.<br/>
 * The interface extends Iterable&lt;Double&gt; so that each sampler can be used within a for-each loop. Example:
 * <pre>
 *     for(double sample : sampler) {
 *         ...
 *     }
 * </pre>
 *
 * @author Michael Bieri
 * @since 27.07.13
 */
public interface Sampler extends Iterable<Double> {

    /**
     * Fetch the next sample from the stream of random values.
     *
     * @return The next sample within the range (0,1)
     */
	public double getNextSample();

}
