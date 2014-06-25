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

package yaphyre.core.cameras;

import java.util.stream.IntStream;

import javax.annotation.Nonnull;
import yaphyre.core.api.CameraSample;
import yaphyre.core.api.Film;
import yaphyre.core.api.Scene;
import yaphyre.core.math.Color;
import yaphyre.core.math.Point2D;
import yaphyre.core.math.Ray;

/**
 * Base class for all camera implementations using a {@link yaphyre.core.api.Film} for recording the sampled information.
 * This contains just the filed to hold a film instance with the corresponding accessor.
 *
 * @author Michael Bieri
 * @since 27.07.13
 */
public abstract class FilmBasedCamera extends AbstractCamera {

    /**
     * film field.
     */
    private final Film film;

    /**
     * Constructor creating a new instance. The {@link yaphyre.core.api.Film} instance must not be null.
     *
     * @param film The instance of {@link yaphyre.core.api.Film} to use. Not null
     */
    protected FilmBasedCamera(@Nonnull Film film) {
        this.film = film;
    }

    /**
     * Access the {@link yaphyre.core.api.Film} instance.
     *
     * @return The {@link yaphyre.core.api.Film} instance associated with this camera.
     */
    @Nonnull
    public Film getFilm() {
        return film;
    }

    @Override
    public void renderScene(@Nonnull Scene scene) {
        final int xResolution = getFilm().getNativeResolution().getFirst();
        final int yResolution = getFilm().getNativeResolution().getSecond();

        final double xStep = 1d / xResolution;
        final double yStep = 1d / yResolution;

        IntStream.range(0, xResolution)
            .forEach(x -> IntStream.range(0, yResolution)
                .mapToObj(y -> new Point2D(x, y))
                .forEach(p -> getSampler().getUnitSquareSamples()
                    .forEach(s -> renderPoint(scene, xStep, yStep, p, s))));

    }

    private void renderPoint(Scene scene, double xStep, double yStep, Point2D filmPoint, Point2D sample) {
        final Point2D sampledFilmPoint = filmPoint.add(sample);
        final Point2D filmSamplePoint = new Point2D(sampledFilmPoint.getU() * xStep, sampledFilmPoint.getV() * yStep);
        final Ray cameraRay = createCameraRay(filmSamplePoint);
        final Color sampledColor = getTracer().traceRay(cameraRay, scene);
        getFilm().addCameraSample(new CameraSample(filmPoint, sampledColor));
    }
}
