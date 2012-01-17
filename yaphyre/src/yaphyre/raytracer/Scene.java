package yaphyre.raytracer;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import yaphyre.core.Camera;
import yaphyre.core.CollisionInformation;
import yaphyre.core.Film;
import yaphyre.core.Lightsource;
import yaphyre.core.Shape;
import yaphyre.geometry.Ray;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public class Scene implements Serializable {

  private static final long serialVersionUID = 7351378461914059224L;

  private final List<Shape> shapes;

  private final List<Lightsource> lightsources;

  private final List<Camera<? extends Film>> cameras;

  public Scene() {
    this.shapes = Lists.newArrayList();
    this.lightsources = Lists.newArrayList();
    this.cameras = Lists.newArrayList();
  }

  public void addCamera(Camera<? extends Film> camera) {
    this.cameras.add(camera);
  }

  public List<Camera<? extends Film>> getCameras() {
    return Collections.unmodifiableList(this.cameras);
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

  public List<Lightsource> getLightsources() {
    return Collections.unmodifiableList(this.lightsources);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this.getClass()).add("cameras", this.cameras.size()).add("shapes", this.shapes.size()).add("lightsources", this.lightsources.size()).toString();
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
