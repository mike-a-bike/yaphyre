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

package yaphyre.scenereaders;

import yaphyre.raytracer.Scene;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA. User: michael Date: 15.10.12 Time: 22:51 To change this template use File | Settings |
 * File Templates.
 */
public interface SceneReaders extends PartialSceneReaders {

	public Scene readScene(InputStream input);

}
