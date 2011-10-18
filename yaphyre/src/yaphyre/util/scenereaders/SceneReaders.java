/*
 * Copyright 2011 Michael Bieri
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
package yaphyre.util.scenereaders;

import yaphyre.raytracer.Scene;

/**
 * Common interface for all scene readers. It does not matter where the
 * implementing reader gets its data from. The type of the datasource is defined
 * by a generic.
 * 
 * @version $Revision: 37 $
 * 
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 */
public interface SceneReaders<T> {

  public Scene readScene(T source);

}