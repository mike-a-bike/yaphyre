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

package yaphyre.core.math;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A Solver is an instance which is used to find the roots to a binomial equation like:<br/>
 * ax<sup>2</sup> + bx + c = 0<br/>
 *
 * @author axmbi03
 * @since 29.07.2014
 */
public interface Solver {
    /**
     * Solve an equation for the given equation type.
     *
     * @param c A list of coefficients. c[0] -> a, c[1] -> b, c[2] -> c, c[3] -> d, ...
     * @return A list of solutions. This list may be empty if there are no real solutions.
     * @throws IllegalArgumentException If the number of the coefficients in <code>c</code> does not match the necessary number of values, an {@link
     *                                  IllegalArgumentException} is thrown. Please notice, even if coefficients may be zero, they must be provided.
     */
    double[] solve(double... c) throws IllegalArgumentException;

    @BindingAnnotation
    @Target({FIELD, METHOD, PARAMETER})
    @Retention(RUNTIME)
    public @interface Linear {
    }

    @BindingAnnotation
    @Target({FIELD, METHOD, PARAMETER})
    @Retention(RUNTIME)
    public @interface Quadratic {
    }

    @BindingAnnotation
    @Target({FIELD, METHOD, PARAMETER})
    @Retention(RUNTIME)
    public @interface Cubic {
    }

    @BindingAnnotation
    @Target({FIELD, METHOD, PARAMETER})
    @Retention(RUNTIME)
    public @interface Quartic {
    }
}
