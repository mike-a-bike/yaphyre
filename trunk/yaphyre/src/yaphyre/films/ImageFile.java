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
import yaphyre.core.Film;
import yaphyre.util.Color;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * Film implementation which records the camera samples as colored pixels in an
 * image file.
 *
 * @version $Revision: 42 $
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 */
public class ImageFile implements Film {

  private static final ImageFormat DEFAULT_IMAGE_FORMAT = ImageFormat.PNG;

  private final int xResolution;

  private final int yResolution;

  private final ImageFormat imageFormat;

  private final Color[] pixelColors;

  public ImageFile(int xResolution, int yResolution) {
    this(xResolution, yResolution, DEFAULT_IMAGE_FORMAT);
  }

  public ImageFile(int xResolution, int yResolution, ImageFormat imageFormat) {
    this.xResolution = xResolution;
    this.yResolution = yResolution;
    this.imageFormat = imageFormat;
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
    int uCoordinate = (int) sample.getRasterPoint().getU();
    int vCoordinate = (int) sample.getRasterPoint().getV();

    Preconditions.checkPositionIndex(uCoordinate, this.xResolution);
    Preconditions.checkPositionIndex(vCoordinate, this.yResolution);

    this.pixelColors[vCoordinate * this.xResolution + vCoordinate] = color;
  }

  @Override
  public void writeImageFile(int xSize, int ySize, String fileName) {
    if (xSize != this.xResolution || ySize != this.yResolution) {
      // SCALE IMAGE....
    }
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(getClass())
        .add("xRes", xResolution)
        .add("yRes", yResolution)
        .add("format", imageFormat).toString();
  }

  public static enum ImageFormat {
    GIF("gif"),
    JPEG("jpg"),
    PNG("png"),
    OpenEXR("exr");

    private final String defaultFileExtention;

    private ImageFormat(String defaultFileExtention) {
      this.defaultFileExtention = defaultFileExtention;
    }

    public String getDefaultFileExtention() {
      return this.defaultFileExtention;
    }

  }

}
