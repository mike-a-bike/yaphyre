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

package yaphyre.core.tracers;

import java.util.Optional;
import yaphyre.core.api.Scene;
import yaphyre.core.api.Tracer;
import yaphyre.core.math.Color;
import yaphyre.core.math.Ray;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 28.06.14
 */
public class RayCaster implements Tracer {

    @Override
    public Optional<Color> traceRay(Ray ray, Scene scene) {
        return scene.hitObject(ray).map(
            collision -> {
                final double cosPhi = collision.getIncidentRay().getDirection().normalize().neg().dot(collision.getNormal());
                return collision.getShape().getShader().getColor(collision.getUVCoordinate()).multiply(cosPhi);
            }
        );
    }

}
