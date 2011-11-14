package test.yaphyre.cameras;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import yaphyre.cameras.OrthographicCamera;
import yaphyre.cameras.AbstractCamera.BaseCameraSettings;
import yaphyre.cameras.OrthographicCamera.OrthographicCameraSettings;
import yaphyre.core.Cameras;
import yaphyre.films.ImageFile;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;

public class OrthographicCameraTest {

  @Test
  public void testGetCameraRay() {
    Cameras<ImageFile> camera;

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

    camera = getTiltedCamera();

    System.out.println(camera.getCameraRay(new Point2D(0, 0)));
    System.out.println(camera.getCameraRay(new Point2D(1, 0)));
    System.out.println(camera.getCameraRay(new Point2D(0, 1)));
    System.out.println(camera.getCameraRay(new Point2D(1, 1)));
    System.out.println(camera.getCameraRay(new Point2D(0.5, 0.5)));
  }

  private Cameras<ImageFile> getZCamera() {
    BaseCameraSettings<ImageFile> baseSettings = BaseCameraSettings.create(new Point3D(0, 0, -1), Point3D.ORIGIN, new ImageFile(800, 600));
    OrthographicCameraSettings orthoSettings = OrthographicCameraSettings.create(4, 3);

    return new OrthographicCamera<ImageFile>(baseSettings, orthoSettings);
  }

  private Cameras<ImageFile> getOffsettedOriginCamera() {
    BaseCameraSettings<ImageFile> baseSettings = BaseCameraSettings.create(new Point3D(0, 1, 0), new Point3D(0, 1, 1), new ImageFile(800, 600));
    OrthographicCameraSettings orthoSettings = OrthographicCameraSettings.create(4, 3);

    return new OrthographicCamera<ImageFile>(baseSettings, orthoSettings);
  }

  private Cameras<ImageFile> getOriginCamera() {
    BaseCameraSettings<ImageFile> baseSettings = BaseCameraSettings.create(Point3D.ORIGIN, new Point3D(0, 0, 1), new ImageFile(800, 600));
    OrthographicCameraSettings orthoSettings = OrthographicCameraSettings.create(4, 3);

    return new OrthographicCamera<ImageFile>(baseSettings, orthoSettings);
  }

  private Cameras<ImageFile> getTiltedCamera() {
    BaseCameraSettings<ImageFile> baseSettings = BaseCameraSettings.create(new Point3D(0, 1, -2), Point3D.ORIGIN, new ImageFile(800, 600));
    OrthographicCameraSettings orthoSettings = OrthographicCameraSettings.create(4, 3);

    return new OrthographicCamera<ImageFile>(baseSettings, orthoSettings);
  }

  @Test
  public void testOrthographicCamera() {
    BaseCameraSettings<ImageFile> baseSettings = BaseCameraSettings.create(new Point3D(1, 1, 0), new Point3D(1, 1, 1), new ImageFile(800, 600));
    OrthographicCameraSettings orthographicSettings = OrthographicCameraSettings.create(4, 3);

    Cameras<ImageFile> camera = new OrthographicCamera<ImageFile>(baseSettings, orthographicSettings);

    assertNotNull(camera);
  }

}
