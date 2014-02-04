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

package yaphyre.films;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.math3.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yaphyre.core.CameraSample;
import yaphyre.core.Film;
import yaphyre.math.Color;
import yaphyre.math.Point2D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 27.07.13
 */
public class ImageFile implements Film {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageFile.class);

	private final Multimap<Point2D, Color> samples;

	private final int xResolution;
	private final int yResolution;

	public ImageFile(int xResolution, int yResolution) {
		this.xResolution = xResolution;
		this.yResolution = yResolution;

		samples = ArrayListMultimap.create(xResolution * yResolution, 1);
	}

    public void safeAsImage(String filename, ImageFormat format) {
		BufferedImage bufferedImage = createImageFromSamples();

		try {
			FileOutputStream imageFileStream = new FileOutputStream(filename);
			ImageIO.write(bufferedImage, String.valueOf(format), imageFileStream);
			imageFileStream.close();
		} catch (IOException ioe) {
			LOGGER.error("Could not write image file: '" + filename + "' with format: '" + format + "'", ioe);
		}

	}

	private BufferedImage createImageFromSamples() {

		BufferedImage image = new BufferedImage(xResolution, yResolution, BufferedImage.TYPE_INT_RGB);

		for (Point2D sample : samples.keySet()) {
			Color sampleColor = Color.BLACK;
			Collection<Color> colorSamples = samples.get(sample);
            for (Color color : colorSamples) {
				sampleColor = sampleColor.add(color);
			}
			sampleColor = sampleColor.multiply(1d / colorSamples.size()).clip();
			int imageX = (int) (sample.getU() * xResolution);
			int imageY = (int) (sample.getV() * yResolution);
			image.setRGB(imageX, imageY, createARGBfromColor(sampleColor));
		}

		return image;
	}

	private int createARGBfromColor(Color color) {
		int red = (int) (color.getRed() * 255);
		int green = (int) (color.getGreen() * 255);
		int blue = (int) (color.getBlue() * 255);
		int alpha = 0xff;
		int argb = ((alpha << 24) | (red << 16) | (green << 8) | blue);
		return argb;
	}

	@Override
	public Pair<Integer, Integer> getNativeResolution() {
		return new Pair<Integer, Integer>(xResolution, yResolution);
	}

	@Override
	public void addCameraSample(CameraSample sample) {
		samples.put(sample.getSamplePoint(), sample.getSampleColor());
	}

	public static enum ImageFormat {
		GIF("gif"),
		JPEG("jpg"),
		PNG("png"),;

		private final String defaultFileExtention;

		private ImageFormat(String defaultFileExtention) {
			this.defaultFileExtention = defaultFileExtention;
		}

		public String getDefaultFileExtention() {
			return defaultFileExtention;
		}

	}

}
