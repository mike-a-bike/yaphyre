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

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 27.07.13
 */
public abstract class FilmBasedCamera<T extends Film> extends AbstractCamera {

	private final T film;

	protected FilmBasedCamera(T film) {
		this.film = film;
	}

	public T getFilm() {
		return film;
	}
}
