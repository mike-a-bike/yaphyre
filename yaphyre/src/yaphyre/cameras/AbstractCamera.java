/*
 * Copyright 2011 Michael Bieri
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
package yaphyre.cameras;

import yaphyre.core.Cameras;
import yaphyre.core.Films;
import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Transformation;

/**
 * A common super class for all implemented {@link Cameras}. This handles some
 * common stuff like transformations and the film instances.
 * 
 * @version $Revision: 42 $
 * 
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 */
public abstract class AbstractCamera implements Cameras {

  private Transformation world2Camera;

  private Transformation camera2World;

  private final Films film;

  private final double nearClip;

  private final double farClip;

  private final Point3D position;

  private final Normal3D direction;

  public AbstractCamera(BaseCameraSettings baseSettings) {
    this.nearClip = baseSettings.getNearClip();
    this.farClip = baseSettings.getFarClip();
    this.position = baseSettings.getPosition();
    this.direction = baseSettings.getDirection();
    this.film = baseSettings.getFilm();
  }

  @Override
  public abstract Ray getCameraRay(Point2D viewPlanePoint);

  @Override
  public Films getFilm() {
    return this.film;
  }

  protected double getNearClip() {
    return this.nearClip;
  }

  protected double getFarClip() {
    return this.farClip;
  }

  protected Point3D getPosition() {
    return this.position;
  }

  protected Normal3D getDirection() {
    return this.direction;
  }

  protected Transformation getCamera2World() {
    return this.camera2World;
  }

  protected void setCamera2World(Transformation camera2World) {
    this.camera2World = camera2World;
  }

  protected Transformation getWorld2Camera() {
    return this.world2Camera;
  }

  protected void setWorld2Camera(Transformation world2Camera) {
    this.world2Camera = world2Camera;
  }

  /**
   * Base settings common for each camera like, near and far clipping pane and a
   * film instance.
   * 
   * @version $Revision: 42 $
   * 
   * @author Michael Bieri
   * @author $LastChangedBy: mike0041@gmail.com $
   */
  public static class BaseCameraSettings {
    private final double nearClip;

    private final double farClip;

    private final Films film;

    private final Point3D position;

    private final Normal3D direction;

    public static BaseCameraSettings create(Point3D position, Normal3D direction, Films film) {
      return create(0d, Double.MAX_VALUE, position, direction, film);
    }

    public static BaseCameraSettings create(double nearClip, double farClip, Point3D position, Normal3D direction, Films film) {
      return new BaseCameraSettings(nearClip, farClip, position, direction, film);
    }

    private BaseCameraSettings(double nearClip, double farClip, Point3D position, Normal3D direction, Films film) {
      this.nearClip = nearClip;
      this.farClip = farClip;
      this.position = position;
      this.direction = direction;
      this.film = film;
    }

    public double getNearClip() {
      return this.nearClip;
    }

    public double getFarClip() {
      return this.farClip;
    }

    public Point3D getPosition() {
      return this.position;
    }

    public Normal3D getDirection() {
      return this.direction;
    }

    public Films getFilm() {
      return this.film;
    }
  }

}