/*
 * Copyright 2013 Michael Bieri
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

package yaphyre.core.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TransformationTest {

    @Test
    public void testLookAtTransformation() {

        Point3D eye = Point3D.ORIGIN;
        Point3D lookAt = new Point3D(0, 0, 1);
        Vector3D up = Vector3D.Y;

        Transformation result = Transformation.lookAt(eye, lookAt, up);

        assertEquals(Matrix.IDENTITY, result.getMatrix());
    }

    @Test
    public void testRasterTransformation() {

        Transformation rasterTransformation = Transformation.rasterToUnitSquare(640, 480);
        assertNotNull(rasterTransformation);

        Point2D p = new Point2D(0d, 0d);
        Point2D pt = rasterTransformation.transform(p);

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

        Point2D p = new Point2D(0d, 0d);
        Point2D pt1 = transCombined.transform(p);
        Point2D pt2 = trans2.transform(trans1.transform(p));

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

        Vector3D v = Vector3D.X;
        Transformation t = Transformation.rotateZ(90);
        Vector3D r = t.transform(v);
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
    public void testTransformPoint() {

        Point3D p1 = new Point3D(1, 0, 0);
        Point3D p2 = new Point3D(10, 1, 1);
        Transformation t = Transformation.translate(9, 1, 1);
        assertEquals(p2, t.transform(p1));

        p1 = new Point3D(1, 1, 0);
        p2 = new Point3D(10, 10, 0);
        t = Transformation.scale(10, 10, 10);
        assertEquals(p2, t.transform(p1));

    }

    @Test
    public void testTransformNormal() {

        Normal3D n1 = new Normal3D(0.5, 0.5, 0);
        Normal3D n2 = new Normal3D(0.25, 0.5, 0);
        Transformation t = Transformation.scale(2, 1, 1);
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

        Transformation t = Transformation.rotate(120, new Vector3D(1, 1, 1));

        Vector3D v = Vector3D.X;
        Vector3D r = t.transform(v);
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


    @Test
    public void testTransformBoundingBox() {
        BoundingBox box = new BoundingBox(Point3D.ORIGIN, new Point3D(1, 1, 1));
        Transformation t = Transformation.scale(2, 1, 1);
        box = t.transform(box);

        assertEquals(box.getPointMin(), Point3D.ORIGIN);
        assertEquals(box.getPointMax(), new Point3D(2, 1, 1));

        box = new BoundingBox(Point3D.ORIGIN, new Point3D(1, 1, 1));
        t = Transformation.scale(-1, -1, -1);
        box = t.transform(box);

        assertEquals(box.getPointMin(), new Point3D(-1, -1, -1));
        assertEquals(box.getPointMax(), Point3D.ORIGIN);
    }
}
