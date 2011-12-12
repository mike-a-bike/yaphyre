package yaphyre.raytracer;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import yaphyre.core.CollisionInformation;
import yaphyre.core.Lightsource;
import yaphyre.core.Shader;
import yaphyre.core.Shape;
import yaphyre.geometry.Ray;

public class Scene implements Serializable {

  private static final long serialVersionUID = 7351378461914059224L;

  private final List<Shape> shapes;

  private final List<Lightsource> lightsources;

  private final List<Shader> shaders;

  public Scene() {
    this.shapes = new LinkedList<Shape>();
    this.lightsources = new LinkedList<Lightsource>();
    this.shaders = new LinkedList<Shader>();
  }

  public void addShape(Shape shape) {
    this.shapes.add(shape);
  }

  public List<Shape> getShapes() {
    return Collections.unmodifiableList(this.shapes);
  }

  public void addLightsource(Lightsource lightsource) {
    this.lightsources.add(lightsource);
  }

  public List<Shader> getShaders() {
    return Collections.unmodifiableList(this.shaders);
  }

  public void addShader(Shader shader) {
    this.shaders.add(shader);
  }

  public List<Lightsource> getLightsources() {
    return Collections.unmodifiableList(this.lightsources);
  }

  @Override
  public String toString() {
    return MessageFormat.format("Scene[shapes:{0,number},lights:{1,number},shaders:{2,number}]", this.shapes.size(), this.lightsources.size(), this.shaders.size());
  }

  public CollisionInformation getCollidingShape(Ray ray, double maxDistance, boolean onlyShadowShapes) {

    CollisionInformation result = null;

    double nearestCollitionDistance = maxDistance;
    Shape nearestCollitionShape = null;

    for (Shape shape : getShapes()) {
      if (onlyShadowShapes && shape.throwsShadow() == false) {
        continue;
      }
      double distance = shape.getIntersectDistance(ray);
      if (distance < nearestCollitionDistance) {
        nearestCollitionDistance = distance;
        nearestCollitionShape = shape;
      }
    }

    if (nearestCollitionDistance < maxDistance) {
      result = new CollisionInformation(nearestCollitionShape, nearestCollitionDistance, ray.getPoint(nearestCollitionDistance));
    }

    return result;
  }

}
