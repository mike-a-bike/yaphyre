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

import java.util.stream.Stream;

import yaphyre.core.math.Normal3D;
import yaphyre.core.math.Ray;
import yaphyre.core.math.Vector3D;

/**
 * Perfectly diffuse material. No reflected and not refracted light samples.
 *
 * @author axmbi03
 * @since 08.07.2014
 */
public class LambertBSDF extends AbstractBSDF {

    @Override
    public Stream<Ray> sampleReflectance(Vector3D incidentRay, Normal3D normal) {
        return Stream.empty();
    }

    @Override
    public Stream<Ray> sampleTransmittance(Vector3D incidentRay, Normal3D normal3D) {
        return Stream.empty();
    }
}
