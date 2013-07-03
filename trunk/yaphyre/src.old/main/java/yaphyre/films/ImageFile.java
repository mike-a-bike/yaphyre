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
package yaphyre.films;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yaphyre.core.CameraSample;
import yaphyre.core.Film;
import yaphyre.util.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Film implementation which records the camera samples as colored pixels in an image file.
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 * @version $Revision: 42 $
 */
public class ImageFile implements Film {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageFile.class);
	private static final ImageFormat DEFAULT_IMAGE_FORMAT = ImageFormat.PNG;
	private final int xResolution;
	private final int yResolution;
	private final ImageFormat imageFormat;
	private final Color[] pixelColors;

	public ImageFile(int xResolution, int yResolution) {
		this(xResolution, yResolution, DEFAULT_IMAGE_FORMAT);
	}

	public ImageFile(int xResolution, int yResolution, ImageFormat imageFormat) {

		Preconditions.checkArgument(imageFormat == ImageFormat.JPEG || imageFormat == ImageFormat.PNG, "unsupoorted image format: %s", imageFormat);

		this.xResolution = xResolution;
		this.yResolution = yResolution;
		this.imageFormat = imageFormat;
		pixelColors = new Color[xResolution * yResolution];
	}

	@Override
	public int getXResolution() {
		return xResolution;
	}

	@Override
	public int getYResolution() {
		return yResolution;
	}

	@Override
	public void addCameraSample(CameraSample sample, Color color) {
		int uCoordinate = (int) sample.getRasterPoint().getU();
		int vCoordinate = (int) sample.getRasterPoint().getV();

		setColor(uCoordinate, vCoordinate, color);

	}

	private void setColor(int x, int y, Color color) {

		Preconditions.checkPositionIndex(x, xResolution);
		Preconditions.checkPositionIndex(y, yResolution);

		pixelColors[y * xResolution + x] = color;
	}

	private Color getColor(int x, int y) {

		Preconditions.checkPositionIndex(x, xResolution);
		Preconditions.checkPositionIndex(y, yResolution);

		return pixelColors[y * xResolution + x];
	}

	@Override
	public void writeImageFile(int xSize, int ySize, String fileName) {
		Preconditions.checkArgument(xSize == xResolution && ySize == yResolution, "scaling is not yet supported");

		BufferedImage image = createImageFromData();

		try {
			FileOutputStream imageFileStream = new FileOutputStream(fileName);
			ImageIO.write(image, imageFormat.toString(), imageFileStream);
			imageFileStream.close();
		} catch (IOException ioe) {
			LOGGER.error("Could not write image file: '" + fileName + "'", ioe);
		}

	}

	private BufferedImage createImageFromData() {
		BufferedImage result = new BufferedImage(xResolution, yResolution, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < yResolution; y++) {
			for (int x = 0; x < xResolution; x++) {
				Color pixelColor = getColor(x, (yResolution - 1) - y).clip();
				int red = (int) (pixelColor.getRed() * 255);
				int green = (int) (pixelColor.getGreen() * 255);
				int blue = (int) (pixelColor.getBlue() * 255);
				int alpha = 0xff;
				int argb = ((alpha << 24) | (red << 16) | (green << 8) | blue);
				result.setRGB(x, y, argb);
			}
		}

		return result;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(getClass()).add("xRes", xResolution).add("yRes", yResolution).add("format",
				imageFormat).toString();
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
			return defaultFileExtention;
		}

	}

}
