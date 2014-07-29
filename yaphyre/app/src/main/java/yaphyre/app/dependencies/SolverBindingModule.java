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

package yaphyre.app.dependencies;

import com.google.inject.PrivateModule;

import yaphyre.core.math.Solver;
import yaphyre.core.math.Solvers;

/**
 * Binding module for the solver instances. Used to mock different behaviours in tests.
 *
 * @author axmbi03
 * @since 29.07.2014
 */
public class SolverBindingModule extends PrivateModule {

    @Override
    protected void configure() {
        bind(Solvers.class).annotatedWith(Solver.Linear.class).toInstance(Solvers.Linear);
        bind(Solvers.class).annotatedWith(Solver.Quadratic.class).toInstance(Solvers.Quadratic);
        bind(Solvers.class).annotatedWith(Solver.Cubic.class).toInstance(Solvers.Cubic);
        bind(Solvers.class).annotatedWith(Solver.Quartic.class).toInstance(Solvers.Quartic);

        expose(Solvers.class).annotatedWith(Solver.Linear.class);
        expose(Solvers.class).annotatedWith(Solver.Quadratic.class);
        expose(Solvers.class).annotatedWith(Solver.Cubic.class);
        expose(Solvers.class).annotatedWith(Solver.Quartic.class);
    }
}
