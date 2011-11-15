package test.yaphyre.cameras;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import yaphyre.cameras.PerspectiveCamera;
import yaphyre.cameras.AbstractCamera.BaseCameraSettings;
import yaphyre.cameras.PerspectiveCamera.PerspectiveCameraSettings;
import yaphyre.films.ImageFile;
import yaphyre.geometry.Point3D;

public class PerspectiveCameraTest {

  @Test
  public void testGetCameraRay() {
    fail("Not yet implemented");
  }

  @Test
  public void testPerspectiveCamera() {
    BaseCameraSettings<ImageFile> baseSettings = BaseCameraSettings.create(new Point3D(0, 0, -1),
                                                                           Point3D.ORIGIN,
                                                                           new ImageFile(640, 480));
    PerspectiveCameraSettings perspectiveSettings = PerspectiveCameraSettings.create(4d / 3d, 50d);

    PerspectiveCamera<ImageFile> camera = new PerspectiveCamera<ImageFile>(baseSettings, perspectiveSettings);

    System.out.println("Testing camera: ".concat(camera.toString()));

    assertNotNull(camera);
    assertNotNull(camera.getFilm());

  }

}
