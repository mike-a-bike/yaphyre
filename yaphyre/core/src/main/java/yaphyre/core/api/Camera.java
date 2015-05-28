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

package yaphyre.core.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

import com.google.inject.BindingAnnotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Camera interface. This interface must be implemented by all classes functioning as a camera. This interface contains
 * a single method: {@link #renderScene(Scene)}. As the name implies, the function of this class is to create a
 * representation of the scene as seen from the camera. It is not mandatory, that a visual representation is created.
 *
 * @author Michael Bieri
 * @since 04.07.13
 */
public interface Camera {

    /**
     * Render the given scene. Please notice, the recording medium is part of the camera. An example are cameras
     * which contain a virtual film which records the image data.
     *
     * @param scene The {@link Scene} to render. Must not be null.
     */
    void renderScene(@Nonnull Scene scene);

    /**
     * Access the {@link Film} instance.
     *
     * @return The {@link Film} instance associated with this camera.
     */
    @Nonnull
    Film getFilm();

    /**
     * Marker interface used for wiring. Use this in an injection context to mark a sampler as camera specific.
     */
    @BindingAnnotation
    @Target({FIELD, METHOD, PARAMETER})
    @Retention(RUNTIME)
    @interface CameraSampler { }

}

