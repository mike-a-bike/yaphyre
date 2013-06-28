/*
 * Copyright 2013 Michael Bieri
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

package yaphyre.core;

import yaphyre.util.Color;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for all film instances. This is for recording rendered samples and process them in an appropriate way. The
 * most common form is an image file.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 42 $
 */
public interface Film {

	public int getXResolution();

	public int getYResolution();

	/**
	 * Adds a new sample for this film to record.
	 *
	 * @param sample
	 * 		The {@link CameraSample} instance which contains all the necessary meta information.
	 * @param color
	 * 		The {@link Color} to record.
	 */
	public void addCameraSample(@NotNull CameraSample sample, @NotNull Color color);

	public void writeImageFile(int xSize, int ySize, @NotNull String fileName);

}
