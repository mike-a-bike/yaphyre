package test.yaphyre.util.scenereaders;

import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Test;

import yaphyre.core.SceneReaders;
import yaphyre.raytracer.Scene;
import yaphyre.util.scenereaders.XMLSceneReader;

public class XMLSceneReaderTest {

  @Test
  public void testReadXMLFile() throws FileNotFoundException {
    Scene scene = null;
    SceneReaders<InputStream> sceneReader = new XMLSceneReader();

    scene = sceneReader.readScene(new FileInputStream("./scenes/first_light.scene.xml"));

    assertNotNull(scene);
  }

}
