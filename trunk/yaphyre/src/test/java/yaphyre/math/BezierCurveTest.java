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

package yaphyre.math;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

import static com.google.common.base.Preconditions.checkState;

/**
 *
 */
public class BezierCurveTest {

	private static final int IMAGE_WIDTH = 640;
	private static final int IMAGE_HEIGHT = 640;
	private static final int CURVE_COLOR = 0xffffff; // white
	private static final int POINT_COLOR = 0x00ff00; // green
	private static final int ENVELOPE_COLOR = 0x0000ff; // blue
	private BufferedImage image;
	private String imageName;

	@Before
	public void prepareImage() {
		image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
	}

	@After
	public void saveImage() {
		checkState(imageName != null && !imageName.isEmpty());

		try {
			FileOutputStream outputStream = new FileOutputStream(imageName);
			ImageIO.write(image, "png", outputStream);
			outputStream.close();
		} catch (Exception e) {
			System.err.println("Cannot create file: " + imageName);
		}

	}

	@Test
	public void testLinearBezier() {
		imageName = "BezierTest_Linear.png";

		final Point3D p0 = new Point3D(0, 320, 0);
		final Point3D p1 = new Point3D(640, 320, 0);

		for (double t = 0d; t <= 1d; t += 0.0025d) {
			final Point3D result = BezierCurve.LINEAR.calculatePoint(t, p0, p1);
			image.setRGB((int) result.getX(), (int) result.getY(), CURVE_COLOR);
		}

		paintControlPoints(p0, p1);
	}

	@Test
	public void testQuadraticBezier() {
		imageName = "BezierTest_Quadratic.png";

		final Point3D p0 = new Point3D(0, 320, 0);
		final Point3D p1 = new Point3D(320, 640, 0);
		final Point3D p2 = new Point3D(640, 320, 0);

		for (double t = 0d; t <= 1d; t += 0.0025d) {
			final Point3D result = BezierCurve.QUADRATIC.calculatePoint(t, p0, p1, p2);
			image.setRGB((int) result.getX(), (int) result.getY(), CURVE_COLOR);
		}

		paintControlPoints(p0, p1, p2);
	}

	@Test
	public void testCubicBezier() {
		imageName = "BezierTest_Cubic.png";

		final Point3D p0 = new Point3D(0, 320, 0);
		final Point3D p1 = new Point3D(160, 640, 0);
		final Point3D p2 = new Point3D(480, 0, 0);
		final Point3D p3 = new Point3D(640, 320, 0);

		for (double t = 0d; t <= 1d; t += 0.0025d) {
			final Point3D result = BezierCurve.CUBIC.calculatePoint(t, p0, p1, p2, p3);
			image.setRGB((int) result.getX(), (int) result.getY(), CURVE_COLOR);
		}

		paintControlPoints(p0, p1, p2, p3);
	}

	@Test
	public void testQuarticBezier() {
		imageName = "BezierTest_Quartic.png";

		final Point3D p0 = new Point3D(0, 320, 0);
		final Point3D p1 = new Point3D(160, 640, 0);
		final Point3D p2 = new Point3D(320, 0, 0);
		final Point3D p3 = new Point3D(480, 640, 0);
		final Point3D p4 = new Point3D(640, 320, 0);

		for (double t = 0d; t <= 1d; t += 0.0025d) {
			final Point3D result = BezierCurve.QUARTIC.calculatePoint(t, p0, p1, p2, p3, p4);
			image.setRGB((int) result.getX(), (int) result.getY(), CURVE_COLOR);
		}

		paintControlPoints(p0, p1, p2, p3, p4);
	}

	@Test
	public void testGenericBezier() {
		imageName = "BezierTest_Generic.png";

		final Point3D p0 = new Point3D(0, 320, 0);
		final Point3D p1 = new Point3D(128, 640, 0);
		final Point3D p2 = new Point3D(256, 0, 0);
		final Point3D p3 = new Point3D(384, 640, 0);
		final Point3D p4 = new Point3D(512, 0, 0);
		final Point3D p5 = new Point3D(640, 320, 0);

		for (double t = 0d; t <= 1d; t += 0.0025d) {
			final Point3D result = BezierCurve.GENERIC.calculatePoint(t, p0, p1, p2, p3, p4, p5);
			image.setRGB((int) result.getX(), (int) result.getY(), CURVE_COLOR);
		}

		paintControlPoints(p0, p1, p2, p3, p4, p5);

	}

	private void paintControlPoints(Point3D... controlPoints) {
		Graphics g = image.getGraphics();
		Color pointColor = new Color(POINT_COLOR);
		Color lineColor = new Color(ENVELOPE_COLOR);
		Point3D lastPoint = null;

		for (Point3D point : controlPoints) {
			if (lastPoint != null) {
				g.setColor(lineColor);
				g.drawLine((int) lastPoint.getX(), (int) lastPoint.getY(), (int) point.getX(), (int) point.getY());
			}
			g.setColor(pointColor);
			g.fillOval((int) point.getX(), (int) point.getY(), 2, 2);
			lastPoint = point;
		}
	}

}
