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

import yaphyre.core.Camera;
import yaphyre.core.Film;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Transformation;
import yaphyre.geometry.Vector3D;

import com.google.common.base.Preconditions;

/**
 * A common super class for all implemented {@link Camera}. This handles some
 * common stuff like transformations and the film instances.
 * 
 * @param <F>
 *          The type of film used by the instance.
 * 
 * @version $Revision: 42 $
 * 
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 */
public abstract class AbstractCamera<F extends Film> implements Camera<F> {

  private final Transformation world2Camera;

  private final Transformation camera2World;

  private final BaseCameraSettings<F> cameraSettings;

  public AbstractCamera(BaseCameraSettings<F> baseSettings) {
    this.cameraSettings = baseSettings;
    this.world2Camera = Transformation.lookAt(baseSettings.getPosition(), baseSettings.getLookAt(), baseSettings.getUp());
    this.camera2World = this.world2Camera.inverse();
  }

  @Override
  public abstract Ray getCameraRay(Point2D viewPlanePoint);

  @Override
  public F getFilm() {
    return this.cameraSettings.getFilm();
  }

  protected double getNearClip() {
    return this.cameraSettings.getNearClip();
  }

  protected double getFarClip() {
    return this.cameraSettings.getFarClip();
  }

  protected Point3D getPosition() {
    return this.cameraSettings.getPosition();
  }

  protected Point3D getLookAt() {
    return this.cameraSettings.getLookAt();
  }

  protected Vector3D getUp() {
    return this.cameraSettings.getUp();
  }

  protected Transformation getCamera2World() {
    return this.camera2World;
  }

  protected Transformation getWorld2Camera() {
    return this.world2Camera;
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
  public static class BaseCameraSettings<F extends Film> {
    private final double nearClip;

    private final double farClip;

    private final F film;

    private final Point3D position;

    private final Point3D lookAt;

    private final Vector3D up;

    public static <T extends Film> BaseCameraSettings<T> create(Point3D position, Point3D lookAt, T film) {
      return create(0d, Double.MAX_VALUE, position, lookAt, Vector3D.Y, film);
    }

    public static <T extends Film> BaseCameraSettings<T> create(Point3D position, Point3D lookAt, Vector3D up, T film) {
      return create(0d, Double.MAX_VALUE, position, lookAt, up, film);
    }

    public static <T extends Film> BaseCameraSettings<T> create(double nearClip, double farClip, Point3D position, Point3D lookAt, Vector3D up, T film) {
      return new BaseCameraSettings<T>(nearClip, farClip, position, lookAt, up, film);
    }

    private BaseCameraSettings(double nearClip, double farClip, Point3D position, Point3D lookAt, Vector3D up, F film) {
      Preconditions.checkArgument(!position.equals(lookAt), "the position and look at point must not be equal");
      this.nearClip = nearClip;
      this.farClip = farClip;
      this.position = position;
      this.lookAt = lookAt;
      this.film = film;
      this.up = up;
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

    public Point3D getLookAt() {
      return this.lookAt;
    }

    public Vector3D getUp() {
      return this.up;
    }

    public F getFilm() {
      return this.film;
    }
  }

}