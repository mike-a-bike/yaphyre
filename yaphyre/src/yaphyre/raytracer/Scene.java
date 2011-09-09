package yaphyre.raytracer;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import yaphyre.geometry.Ray;
import yaphyre.lights.Lightsources;
import yaphyre.shaders.Shaders;
import yaphyre.shapes.Shapes;

public class Scene {

  private final List<Shapes> shapes;

  private final List<Lightsources> lightsources;

  private final List<Shaders> shaders;

  public Scene() {
    this.shapes = new LinkedList<Shapes>();
    this.lightsources = new LinkedList<Lightsources>();
    this.shaders = new LinkedList<Shaders>();
  }

  public void addShape(Shapes shape) {
    this.shapes.add(shape);
  }

  public List<Shapes> getShapes() {
    return Collections.unmodifiableList(this.shapes);
  }

  public void addLightsource(Lightsources lightsource) {
    this.lightsources.add(lightsource);
  }

  public List<Shaders> getShaders() {
    return Collections.unmodifiableList(this.shaders);
  }

  public void addShader(Shaders shader) {
    this.shaders.add(shader);
  }

  public List<Lightsources> getLightsources() {
    return Collections.unmodifiableList(this.lightsources);
  }

  @Override
  public String toString() {
    return MessageFormat.format("Scene[shapes:{0,number},lights:{1,number},shaders:{2,number}]", this.shapes.size(), this.lightsources.size(), this.shaders.size());
  }

  public CollisionInformations getCollidingShape(Ray ray, double maxDistance, boolean onlyShadowShapes) {

    CollisionInformations result = null;

    double nearestCollitionDistance = maxDistance;
    Shapes nearestCollitionShape = null;

    for (Shapes shape : this.getShapes()) {
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
      result = new CollisionInformations(nearestCollitionShape, nearestCollitionDistance, ray.getPoint(nearestCollitionDistance));
    }

    return result;
  }

}
