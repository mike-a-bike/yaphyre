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

package yaphyre.cameras;

import yaphyre.core.Film;

import javax.annotation.Nonnull;

/**
 * Base class for all camera implementations using a {@link yaphyre.core.Film} for recording the sampled information.
 * This contains just the filed to hold a film instance with the corresponding accessor.
 *
 * @author Michael Bieri
 * @since 27.07.13
 */
public abstract class FilmBasedCamera<T extends Film> extends AbstractCamera {

    /**
     * film field.
     */
    private final T film;

    /**
     * Constructor creating a new instance. The {@link yaphyre.core.Film} instance must not be null.
     *
     * @param film The instance of {@link yaphyre.core.Film} to use. Not null
     */
    protected FilmBasedCamera(@Nonnull T film) {
        this.film = film;
    }

    /**
     * Access the {@link yaphyre.core.Film} instance.
     *
     * @return The {@link yaphyre.core.Film} instance associated with this camera.
     */
    @Nonnull
    public T getFilm() {
        return film;
    }
}
