package proto.yaphyre;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import yaphyre.core.SceneReaders;
import yaphyre.util.scenereaders.MultiStageXMLSceneReader;

public class SceneReaderPrototyping {

  /**
   * @param args
   * @throws FileNotFoundException
   */
  public static void main(String[] args) throws FileNotFoundException {
    SceneReaders<InputStream> streamReader = new MultiStageXMLSceneReader();

    streamReader.readScene(new FileInputStream("scenes/first_light.scene.xml"));

  }

}
