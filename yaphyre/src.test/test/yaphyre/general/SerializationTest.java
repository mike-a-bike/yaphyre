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

package test.yaphyre.general;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import yaphyre.core.Shape;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Transformation;
import yaphyre.shaders.Material;
import yaphyre.shaders.SimpleShader;
import yaphyre.shapes.Plane;
import yaphyre.shapes.Sphere;
import yaphyre.util.Color;

import org.junit.Assert;
import org.junit.Test;

public class SerializationTest {

	@Test
	public void testShapeSerialization() {
		try {
			Shape sphere = Sphere.createSphere(new Point3D(10, 20, 30), 1, new SimpleShader(new Material(0.2, 0.25, 0.1, 0.9, 0), new Color(1, 0, 0)), true);
			Shape plane = new Plane(Transformation.IDENTITY, new SimpleShader(new Material(0, 1, 0, 0, 0), new Color(1, 1, 1)), false);

			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			ObjectOutputStream out = new ObjectOutputStream(buffer);

			out.writeObject(sphere);
			out.writeObject(plane);
			out.close();

			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));

			Shape shapeSphere = (Shape) in.readObject();
			Shape shapePlane = (Shape) in.readObject();

			Assert.assertEquals(sphere, shapeSphere);
			Assert.assertEquals(plane, shapePlane);

		} catch (IOException exception) {
			Assert.fail("Unexpected exception: " + exception.toString());
			exception.printStackTrace();
		} catch (ClassNotFoundException exception) {
			Assert.fail("Unexpected exception: " + exception.toString());
			exception.printStackTrace();
		}

	}

}
