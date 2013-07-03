/*
 * Copyright 2012 Michael Bieri
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

/**
 * This package contains all known shapes. The most used, public structure is
 * the {@link Shape} interface. All shapes must implement this interface. The
 * rendering process only uses this interface. So all concrete implementations
 * and implementation details are considered private.<br/>
 *
 * The currently available shapes are:
 * <ul>
 * <li>{@link Plane}</li>
 * <li>{@link Sphere}</li>
 * <li>{@link Instance}</li>
 * </ul>
 *
 * The {@link Sphere} and {@link Plane} are actual shapes with a defined
 * geometry. The {@link Instance} is not really a shape with a geometry of its
 * own, but a wrapper which transforms a given shape without crating multiple
 * instances of it. This may be used to safe memory if shapes get complex.
 */
package yaphyre.shapes;

