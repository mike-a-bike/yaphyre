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

package yaphyre.core.films;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.math3.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yaphyre.core.api.CameraSample;
import yaphyre.core.api.Film;
import yaphyre.core.math.Color;
import yaphyre.core.math.Point2D;

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

    public void safeAsImage(String filename, ImageFormat format, double gamma) {
		BufferedImage bufferedImage = createImageFromSamples(gamma);

	    try (FileOutputStream imageFileStream = new FileOutputStream(filename)) {
		    ImageIO.write(bufferedImage, String.valueOf(format), imageFileStream);
		} catch (IOException ioe) {
			LOGGER.error("Could not write image file: '" + filename + "' with format: '" + format + "'", ioe);
		}

	}

	private BufferedImage createImageFromSamples(double gamma) {

		BufferedImage image = new BufferedImage(xResolution, yResolution, BufferedImage.TYPE_INT_RGB);

        samples.asMap().entrySet().stream().forEach(
            entry -> {
                final Collection<Color> colorSamples = entry.getValue();
                Color sampleColor = colorSamples.stream().reduce(Color.BLACK, (c1, c2) -> c1.add(c2.multiply(1d / colorSamples.size())));
                sampleColor = (gamma != 1d) ? sampleColor.pow(gamma).clip() : sampleColor;
                final Point2D samplePoint = entry.getKey();
                image.setRGB((int) (samplePoint.getU()), (int) (samplePoint.getV()), createARGBfromColor(sampleColor));
            }
        );

		return image;
	}

	private int createARGBfromColor(Color color) {
		int red = (int) (color.getRed() * 255);
		int green = (int) (color.getGreen() * 255);
		int blue = (int) (color.getBlue() * 255);
		int alpha = 0xff;
        return ((alpha << 24) | (red << 16) | (green << 8) | blue);
	}

	@Nonnull
    @Override
	public Pair<Integer, Integer> getNativeResolution() {
		return new Pair<>(xResolution, yResolution);
	}

	@Override
	public void addCameraSample(@Nonnull CameraSample sample) {
		samples.put(sample.getSamplePoint(), sample.getSampleColor());
	}

	@SuppressWarnings("UnusedDeclaration")
    public static enum ImageFormat {
		GIF("gif"),
		JPEG("jpg"),
		PNG("png"),;

		private final String defaultFileExtension;

		private ImageFormat(String defaultFileExtension) {
			this.defaultFileExtension = defaultFileExtension;
		}

		public String getDefaultFileExtension() {
			return defaultFileExtension;
		}

	}

}
