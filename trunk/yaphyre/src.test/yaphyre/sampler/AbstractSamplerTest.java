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

import org.junit.After;
import org.junit.Before;

/**
 * Created with IntelliJ IDEA. User: michael Date: 28.10.12 Time: 21:05 To change this template use File | Settings |
 * File Templates.
 */
public class AbstractSamplerTest {

	private static final int IMAGE_WIDTH = 640;
	private static final int IMAGE_HEIGHT = 640;
	private static final int MARK_SIZE = 3;
	private static final int MARK_COLOR = 0xffff0000; // ARGB: Red

	private BufferedImage image;

	private String imageName;

	@Before
	public void prepareImage() {
		image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
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

	protected void addMark(final double u, final double v) {
		int centerU = (int) (IMAGE_WIDTH * u);
		int centerV = (int) (IMAGE_HEIGHT * v);

		int startU = centerU - MARK_SIZE / 2;
		int startV = centerV - MARK_SIZE / 2;

		for(int x = startU; x < startU + MARK_SIZE; x++) {
			image.setRGB(min(max(0, x), IMAGE_WIDTH), centerV, MARK_COLOR);
		}

		for(int y = startV; y < startV + MARK_SIZE; y++) {
			image.setRGB(centerU, min(max(0, y), IMAGE_HEIGHT), MARK_COLOR);
		}

	}

	public BufferedImage getImage() {
		return image;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(final String imageName) {
		this.imageName = imageName;
	}
}
