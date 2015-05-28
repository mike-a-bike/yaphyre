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

package yaphyre.app.scenereader;

import java.util.Optional;
import javax.annotation.Nonnull;

import yaphyre.core.api.Scene;

/**
 * Common API for classes creating Scene descriptions. This may be a built-in test scene or an implementation which
 * reads file and creates the Scene structures from it.
 *
 * @author axmbi03
 * @since 01.07.2014
 */
public interface SceneReader {

    @Nonnull
    Optional<Scene> readScene();

}
