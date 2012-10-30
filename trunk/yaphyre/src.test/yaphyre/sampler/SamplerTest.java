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

package yaphyre.sampler;

import static com.google.common.base.Preconditions.checkState;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.samplers.AbstractSampler;

import org.junit.After;
import org.junit.Before;

/**
 * Base class for all test cases producing images for different sampling strategies.
 * This class prepares a BufferedImage instance to paint on and provides convenience methods to work with it (like
 * marking a certain point with a cross and saving the resulting image after the test has completed).
 */
public class SamplerTest {

	private static final int IMAGE_WIDTH = 640;
	private static final int IMAGE_HEIGHT = 640;
	private static final int MARK_SIZE = 3;
	private static final int MARK_COLOR = 0xffffffff; // ARGB: White

	private BufferedImage image;

	private String imageName;

	@Before
	public void prepareImage() {
		image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_BYTE_BINARY);
	}

	@After
	public void saveImage() {
		checkState(imageName != null && !imageName.isEmpty());

		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(imageName);
			ImageIO.write(image, "png", outputStream);
			outputStream.close();
		} catch (Exception e) {
			System.err.println("Cannot create file: " + imageName);
		}

	}

	protected void addMark(final BufferedImage image, final Point2D point) {
		int centerU = (int) (image.getWidth() * point.getU());
		int centerV = (int) (image.getHeight() * point.getV());

		int startU = centerU - MARK_SIZE / 2;
		int startV = centerV - MARK_SIZE / 2;

		final int maxHeight = image.getHeight() - 1;
		final int maxWidth = image.getWidth() - 1;

		for(int u = startU; u < startU + MARK_SIZE; u++) {
			image.setRGB(clipImageCoordinate(0, maxWidth, u), clipImageCoordinate(0, maxHeight, centerV), MARK_COLOR);
		}

		for(int v = startV; v < startV + MARK_SIZE; v++) {
			image.setRGB(clipImageCoordinate(0, maxWidth, centerU), clipImageCoordinate(0, maxHeight, v), MARK_COLOR);
		}

	}

	private int clipImageCoordinate(int min, int max, int coordinate) {
		return min(max(min, coordinate), max);
	}

	protected BufferedImage getImage() {
		return image;
	}

	protected void setImageName(final String imageName) {
		this.imageName = imageName;
	}

	protected void createUnitSquareImage(final BufferedImage image, final AbstractSampler sampler, final int numberOfSets) {
		for(int run = 0; run < numberOfSets; run++) {
			for(Point2D point : sampler.getUnitSquareSamples()) {
				addMark(image, point);
			}
		}
	}

	protected void createUnitCircleImage(final BufferedImage image, final AbstractSampler sampler, final int numberOfSets) {
		for(int run = 0; run < numberOfSets; run++) {
			for(Point2D sampledPoint : sampler.getUnitCircleSamples()) {
				Point2D point = sampledPoint.mul(.5d).add(.5d, .5d);
				addMark(image, point);
			}
		}
	}

	protected void createUnitSphereImageXY(final BufferedImage image, final AbstractSampler sampler, final int numberOfSets) {
		for(int run = 0; run < numberOfSets; run++) {
			for(Point3D sampledPoint : sampler.getSphereSamples()) {
				Point2D point = new Point2D(sampledPoint.getX(), sampledPoint.getY()).mul(.5d).add(.5d, .5d);
				addMark(image, point);
			}
		}
	}

}
