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
import javax.inject.Inject;
import yaphyre.core.api.Camera;
import yaphyre.core.api.CameraSample;
import yaphyre.core.api.Film;
import yaphyre.core.api.Sampler;
import yaphyre.core.api.Scene;
import yaphyre.core.api.Tracer;
import yaphyre.core.math.Color;
import yaphyre.core.math.Point2D;
import yaphyre.core.math.Ray;

/**
 * A generic camera base class. Most of the implemented cameras will use this base class. It contains a
 * {@link yaphyre.core.api.Sampler} for use in Montecarlo sampling as well as a {@link yaphyre.core.api.Tracer} for integrating
 * a camera ray.
 * Both instance cannot be null.
 *
 * @author Michael Bieri
 * @since 08.09.13
 */
public abstract class AbstractCamera implements Camera {

    /**
     * film field.
     */
    protected final Film film;
    /**
     * Tracer used for integrating a camera ray.
     */
    private Tracer tracer;
    /**
     * Camera sampler
     */
    private Sampler sampler;

    public AbstractCamera(@Nonnull Film film) {
        this.film = film;
    }

    @Nonnull
    public Sampler getSampler() {
        return sampler;
    }

    @Inject
    public void setSampler(@CameraSampler @Nonnull Sampler sampler) {
        this.sampler = sampler;
    }

    @Nonnull
    public Tracer getTracer() {
        return tracer;
    }

    @Inject
    public void setTracer(@Nonnull Tracer tracer) {
        this.tracer = tracer;
    }

    @Nonnull
    @Override
    public Film getFilm() {
        return film;
    }

    /**
     * Create a sample ray representing a 'looking' ray for the given sample point. The range of the samples is [0,1).
     * @param samplePoint The point to create the sample ray for. Must not be null
     * @return A corresponding ray starting at the camera.
     */
    @Nonnull
    protected abstract Ray createCameraRay(@Nonnull Point2D samplePoint);

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
        final Color sampledColor = getTracer().traceRay(cameraRay, scene).orElse(Color.BLACK);
        getFilm().addCameraSample(new CameraSample(filmPoint, sampledColor));
    }
}
