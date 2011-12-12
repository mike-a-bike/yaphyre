package test.yaphyre.general;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Assert;
import org.junit.Test;

import yaphyre.core.Shape;
import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point3D;
import yaphyre.shaders.Material;
import yaphyre.shaders.SimpleShader;
import yaphyre.shapes.Plane;
import yaphyre.shapes.Sphere;
import yaphyre.util.Color;

public class SerializationTest {

  @Test
  public void testShapeSerialization() {
    try {
      Shape sphere = new Sphere(new Point3D(10, 20, 30), 1, new SimpleShader(new Material(0.2, 0.25, 0.1, 0.9, 0), new Color(1, 0, 0)), true);
      Shape plane = new Plane(new Point3D(4, 5, 6), new Normal3D(1, 1, 2), new SimpleShader(new Material(0, 1, 0, 0, 0), new Color(1, 1, 1)), false);

      ByteArrayOutputStream buffer = new ByteArrayOutputStream();

      ObjectOutputStream out = new ObjectOutputStream(buffer);

      out.writeObject(sphere);
      out.writeObject(plane);
      out.close();

      ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));

      Shape shapeSphere = (Shape)in.readObject();
      Shape shapePlane = (Shape)in.readObject();

      Assert.assertEquals(sphere, shapeSphere);
      Assert.assertEquals(plane, shapePlane);

    } catch (IOException exception) {
      Assert.fail("Unexpected exceptino: " + exception.toString());
      exception.printStackTrace();
    } catch (ClassNotFoundException exception) {
      Assert.fail("Unexpected exceptino: " + exception.toString());
      exception.printStackTrace();
    }

  }

}
