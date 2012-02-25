package test.yaphyre.cameras;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import yaphyre.cameras.AbstractCamera.BaseCameraSettings;
import yaphyre.cameras.OrthographicCamera;
import yaphyre.cameras.OrthographicCamera.OrthographicCameraSettings;
import yaphyre.core.Camera;
import yaphyre.films.ImageFile;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;

public class OrthographicCameraTest {

  @Test
  public void testGetCameraRay() {
    Camera camera;

    camera = this.getOriginCamera();

    System.out.println(camera.getCameraRay(new Point2D(0, 0)));
    System.out.println(camera.getCameraRay(new Point2D(1, 0)));
    System.out.println(camera.getCameraRay(new Point2D(0, 1)));
    System.out.println(camera.getCameraRay(new Point2D(1, 1)));
    System.out.println(camera.getCameraRay(new Point2D(0.5, 0.5)));

    camera = this.getZCamera();

    System.out.println(camera.getCameraRay(new Point2D(0, 0)));
    System.out.println(camera.getCameraRay(new Point2D(1, 0)));
    System.out.println(camera.getCameraRay(new Point2D(0, 1)));
    System.out.println(camera.getCameraRay(new Point2D(1, 1)));
    System.out.println(camera.getCameraRay(new Point2D(0.5, 0.5)));

    camera = this.getOffsettedOriginCamera();

    System.out.println(camera.getCameraRay(new Point2D(0, 0)));
    System.out.println(camera.getCameraRay(new Point2D(1, 0)));
    System.out.println(camera.getCameraRay(new Point2D(0, 1)));
    System.out.println(camera.getCameraRay(new Point2D(1, 1)));
    System.out.println(camera.getCameraRay(new Point2D(0.5, 0.5)));

    camera = this.getTiltedCamera();

    System.out.println(camera.getCameraRay(new Point2D(0, 0)));
    System.out.println(camera.getCameraRay(new Point2D(1, 0)));
    System.out.println(camera.getCameraRay(new Point2D(0, 1)));
    System.out.println(camera.getCameraRay(new Point2D(1, 1)));
    System.out.println(camera.getCameraRay(new Point2D(0.5, 0.5)));
  }

  private Camera getZCamera() {
    BaseCameraSettings baseSettings = BaseCameraSettings.create(new Point3D(0, 0, -1), Point3D.ORIGIN);
    OrthographicCameraSettings orthoSettings = OrthographicCameraSettings.create(4, 3);

    return new OrthographicCamera(baseSettings, orthoSettings, new ImageFile(800, 600));
  }

  private Camera getOffsettedOriginCamera() {
    BaseCameraSettings baseSettings = BaseCameraSettings.create(new Point3D(0, 1, 0), new Point3D(0, 1, 1));
    OrthographicCameraSettings orthoSettings = OrthographicCameraSettings.create(4, 3);

    return new OrthographicCamera(baseSettings, orthoSettings, new ImageFile(800, 600));
  }

  private Camera getOriginCamera() {
    BaseCameraSettings baseSettings = BaseCameraSettings.create(Point3D.ORIGIN, new Point3D(0, 0, 1));
    OrthographicCameraSettings orthoSettings = OrthographicCameraSettings.create(4, 3);

    return new OrthographicCamera(baseSettings, orthoSettings, new ImageFile(800, 600));
  }

  private Camera getTiltedCamera() {
    BaseCameraSettings baseSettings = BaseCameraSettings.create(new Point3D(0, 1, -2), Point3D.ORIGIN);
    OrthographicCameraSettings orthoSettings = OrthographicCameraSettings.create(4, 3);

    return new OrthographicCamera(baseSettings, orthoSettings, new ImageFile(800, 600));
  }

  @Test
  public void testOrthographicCamera() {
    BaseCameraSettings baseSettings = BaseCameraSettings.create(new Point3D(1, 1, 0), new Point3D(1, 1, 1));
    OrthographicCameraSettings orthographicSettings = OrthographicCameraSettings.create(4, 3);

    Camera camera = new OrthographicCamera(baseSettings, orthographicSettings, new ImageFile(800, 600));

    assertNotNull(camera);
  }

}
