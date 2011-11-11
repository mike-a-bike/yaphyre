package test.yaphyre.cameras;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import yaphyre.cameras.PerspectiveCamera;

public class PerspectiveCameraTest {

  ThreadLocal<PerspectiveCamera> testCamera;

  @BeforeClass
  public void setUpTest() throws Exception {
    this.testCamera = new ThreadLocal<PerspectiveCamera>();
  }

  @Before
  public void setUp() {

  }

  @Test
  public void testGetCameraRay() {
    fail("Not yet implemented");
  }

  @Test
  public void testPerspectiveCamera() {
    fail("Not yet implemented");
  }

}
