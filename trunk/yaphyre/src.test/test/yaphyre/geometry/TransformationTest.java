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

package test.yaphyre.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import yaphyre.geometry.Matrix;
import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Transformation;
import yaphyre.geometry.Vector3D;

public class TransformationTest {

	@Test
	public void testLookAtTransformation() {
		Point3D eye;
		Point3D lookAt;
		Vector3D up;

		eye = Point3D.ORIGIN;
		lookAt = new Point3D(0, 0, 1);
		up = Vector3D.Y;
		Transformation result;

		result = Transformation.lookAt(eye, lookAt, up);

		assertEquals(Matrix.IDENTITY, result.getMatrix());
	}

	@Test
	public void testRasterTransformation() {
		Transformation rasterTransformation;
		Point2D p;
		Point2D pt;

		rasterTransformation = Transformation.rasterToUnitSquare(640, 480);
		assertNotNull(rasterTransformation);

		p = new Point2D(0d, 0d);
		pt = rasterTransformation.transform(p);

		assertEquals(new Point2D(0d, 0d), pt);

		p = new Point2D(640d, 480d);
		pt = rasterTransformation.transform(p);

		assertEquals(new Point2D(1d, 1d), pt);

		p = new Point2D(320d, 240d);
		pt = rasterTransformation.transform(p);

		assertEquals(new Point2D(0.5d, 0.5d), pt);

		p = new Point2D(0d, 0d);
		pt = rasterTransformation.inverse().transform(p);

		assertEquals(p, pt);

		p = new Point2D(1d, 1d);
		pt = rasterTransformation.inverse().transform(p);

		assertEquals(new Point2D(640d, 480d), pt);

		p = new Point2D(0.5d, 0.5d);
		pt = rasterTransformation.inverse().transform(p);

		assertEquals(new Point2D(320d, 240d), pt);

	}

	@Test
	public void testMul() {

		Transformation trans1 = Transformation.rasterToUnitSquare(640, 480);
		Transformation trans2 = Transformation.translate(-0.5d, -0.5d, 0);
		Transformation transCombined = trans2.mul(trans1);

		Point2D p;
		Point2D pt1;
		Point2D pt2;

		p = new Point2D(0d, 0d);
		pt1 = transCombined.transform(p);
		pt2 = trans2.transform(trans1.transform(p));

		assertEquals(pt1, pt2);

		p = new Point2D(640d, 480d);
		pt1 = transCombined.transform(p);
		pt2 = trans2.transform(trans1.transform(p));

		assertEquals(pt1, pt2);

		p = new Point2D(320d, 240d);
		pt1 = transCombined.transform(p);
		pt2 = trans2.transform(trans1.transform(p));

		assertEquals(pt1, pt2);
	}

	@Test
	public void testTransformVector() {
		Vector3D v;
		Vector3D r;
		Transformation t;

		v = Vector3D.X;
		t = Transformation.rotateZ(90);
		r = t.transform(v);
		assertEquals(Vector3D.Y, r);
		r = t.inverse().transform(r);
		assertEquals(v, r);

		t = Transformation.translate(10, 20, 30);
		r = t.transform(v);
		assertEquals(v, r);
		r = t.inverse().transform(r);
		assertEquals(v, r);

		t = Transformation.scale(100, 100, 100);
		r = t.transform(v);
		assertEquals(new Vector3D(100, 0, 0), r);
		r = t.inverse().transform(r);
		assertEquals(v, r);

	}

	@Test
	public void testTransfromPoint() {
		Point3D p1;
		Point3D p2;
		Transformation t;

		p1 = new Point3D(1, 0, 0);
		p2 = new Point3D(10, 1, 1);
		t = Transformation.translate(9, 1, 1);
		assertEquals(p2, t.transform(p1));

		p1 = new Point3D(1, 1, 0);
		p2 = new Point3D(10, 10, 0);
		t = Transformation.scale(10, 10, 10);
		assertEquals(p2, t.transform(p1));

	}

	@Test
	public void testTransformNormal() {
		Normal3D n1;
		Normal3D n2;
		Transformation t;

		n1 = new Normal3D(0.5, 0.5, 0);
		n2 = new Normal3D(0.25, 0.5, 0);
		t = Transformation.scale(2, 1, 1);
		assertEquals(n2, t.transform(n1));

		n1 = new Normal3D(1, 1, 0);
		n2 = new Normal3D(-1, 1, 0);
		t = Transformation.rotateZ(90);
		assertEquals(n2, t.transform(n1));

		n1 = new Normal3D(1, 0, 0);
		n2 = new Normal3D(1, 0, 0);
		t = Transformation.translate(10, 10, 10);
		assertEquals(n2, t.transform(n1));
	}

	@Test
	public void testRotate() {

		Vector3D v;
		Vector3D r;
		Transformation t;

		t = Transformation.rotate(120, new Vector3D(1, 1, 1));

		v = Vector3D.X;
		r = t.transform(v);
		assertEquals(Vector3D.Y, r);

		r = t.transform(r);
		assertEquals(Vector3D.Z, r);

		r = t.transform(r);
		assertEquals(Vector3D.X, r);

		v = new Vector3D(1, 1, 0);
		t = Transformation.rotateZ(90);
		r = t.transform(v);
		assertEquals(new Vector3D(-1, 1, 0), r);

	}
}
