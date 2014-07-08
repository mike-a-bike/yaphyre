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

package yaphyre.core.bsdfs;

import javax.inject.Inject;

import yaphyre.core.api.BSDF;
import yaphyre.core.api.Sampler;

/**
 * Common super class for all sampling functions. This contains a Sampler instance. This can be injected
 * as the setter is annotated with the @Inject annotation.
 *
 * @author axmbi03
 * @since 08.07.2014
 */
public abstract class AbstractBSDF implements BSDF {

    private Sampler sampler;

    @Inject
    public void setSampler(Sampler sampler) {
        this.sampler = sampler;
    }

    public Sampler getSampler() {
        return sampler;
    }
}
