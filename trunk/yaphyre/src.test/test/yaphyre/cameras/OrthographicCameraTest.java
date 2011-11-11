package test.yaphyre.cameras;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import yaphyre.cameras.OrthographicCamera;
import yaphyre.cameras.AbstractCamera.BaseCameraSettings;
import yaphyre.cameras.OrthographicCamera.OrthographicCameraSettings;
import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;

public class OrthographicCameraTest {

  @Test
  public void testGetCameraRay() {
    OrthographicCamera camera;

    camera = getOriginCamera();

    System.out.println(camera.getCameraRay(new Point2D(0, 0)));
    System.out.println(camera.getCameraRay(new Point2D(1, 0)));
    System.out.println(camera.getCameraRay(new Point2D(0, 1)));
    System.out.println(camera.getCameraRay(new Point2D(1, 1)));
    System.out.println(camera.getCameraRay(new Point2D(0.5, 0.5)));

    camera = getZCamera();

    System.out.println(camera.getCameraRay(new Point2D(0, 0)));
    System.out.println(camera.getCameraRay(new Point2D(1, 0)));
    System.out.println(camera.getCameraRay(new Point2D(0, 1)));
    System.out.println(camera.getCameraRay(new Point2D(1, 1)));
    System.out.println(camera.getCameraRay(new Point2D(0.5, 0.5)));

    camera = getOffsettedOriginCamera();

    System.out.println(camera.getCameraRay(new Point2D(0, 0)));
    System.out.println(camera.getCameraRay(new Point2D(1, 0)));
    System.out.println(camera.getCameraRay(new Point2D(0, 1)));
    System.out.println(camera.getCameraRay(new Point2D(1, 1)));
    System.out.println(camera.getCameraRay(new Point2D(0.5, 0.5)));

    camera = getTilltedCamera();

    System.out.println(camera.getCameraRay(new Point2D(0, 0)));
    System.out.println(camera.getCameraRay(new Point2D(1, 0)));
    System.out.println(camera.getCameraRay(new Point2D(0, 1)));
    System.out.println(camera.getCameraRay(new Point2D(1, 1)));
    System.out.println(camera.getCameraRay(new Point2D(0.5, 0.5)));
  }

  private OrthographicCamera getZCamera() {
    BaseCameraSettings baseSettings = BaseCameraSettings.create(new Point3D(0, 0, -1), Normal3D.NORMAL_Z, null);
    OrthographicCameraSettings orthoSettings = OrthographicCameraSettings.create(4, 3);

    return new OrthographicCamera(baseSettings, orthoSettings);
  }

  private OrthographicCamera getOffsettedOriginCamera() {
    BaseCameraSettings baseSettings = BaseCameraSettings.create(new Point3D(0, 1, 0), Normal3D.NORMAL_Z, null);
    OrthographicCameraSettings orthoSettings = OrthographicCameraSettings.create(4, 3);

    return new OrthographicCamera(baseSettings, orthoSettings);
  }

  private OrthographicCamera getOriginCamera() {
    BaseCameraSettings baseSettings = BaseCameraSettings.create(Point3D.ORIGIN, Normal3D.NORMAL_Z, null);
    OrthographicCameraSettings orthoSettings = OrthographicCameraSettings.create(4, 3);

    return new OrthographicCamera(baseSettings, orthoSettings);
  }

  private OrthographicCamera getTilltedCamera() {
    BaseCameraSettings baseSettings = BaseCameraSettings.create(new Point3D(0, 1, -2), new Point3D(0, 1, -2).sub(Point3D.ORIGIN).asNormal(), null);
    OrthographicCameraSettings orthoSettings = OrthographicCameraSettings.create(4, 3);

    return new OrthographicCamera(baseSettings, orthoSettings);
  }

  @Test
  public void testOrthographicCamera() {
    BaseCameraSettings baseSettings = BaseCameraSettings.create(new Point3D(1, 1, 0), new Normal3D(0, 0, 1), null);
    OrthographicCameraSettings orthographicSettings = OrthographicCameraSettings.create(4, 3);

    OrthographicCamera camera = new OrthographicCamera(baseSettings, orthographicSettings);

    assertNotNull(camera);
  }

}
