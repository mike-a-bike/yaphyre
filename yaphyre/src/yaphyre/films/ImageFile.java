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
package yaphyre.films;

import yaphyre.core.CameraSample;
import yaphyre.core.Films;
import yaphyre.util.Color;

/**
 * Film implementation which records the camera samples as colored pixels in an
 * image file.
 * 
 * @version $Revision: 42 $
 * 
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 */
public class ImageFile implements Films {

  private static final FileType DEFAULT_FILE_TYPE = FileType.PNG;

  private final int xResolution;

  private final int yResolution;

  private final FileType fileType;

  private final Color[] pixelColors;

  public ImageFile(int xResolution, int yResolution) {
    this(xResolution, yResolution, DEFAULT_FILE_TYPE);
  }

  public ImageFile(int xResolution, int yResolution, FileType fileType) {
    this.xResolution = xResolution;
    this.yResolution = yResolution;
    this.fileType = fileType;
    this.pixelColors = new Color[xResolution * yResolution];
  }

  @Override
  public int getXResolution() {
    return this.xResolution;
  }

  @Override
  public int getYResolution() {
    return this.yResolution;
  }

  @Override
  public void addCameraSample(CameraSample sample, Color color) {
    throw new RuntimeException("Not implemented yet");
  }

  @Override
  public void writeImageFile(int xSize, int ySize, String fileName) {
    if (xSize != this.xResolution || ySize != this.yResolution) {
      // SCALE IMAGE....
    }
  }

  public static enum FileType {
    GIF, JPEG, PNG, OpenEXR
  }

}
