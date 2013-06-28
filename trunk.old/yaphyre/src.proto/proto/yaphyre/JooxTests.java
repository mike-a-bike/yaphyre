package proto.yaphyre;

import static org.joox.JOOX.$;
import static org.joox.JOOX.even;
import static org.joox.JOOX.ids;
import static org.joox.JOOX.odd;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Deque;
import java.util.List;

import org.joox.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import yaphyre.geometry.Transformation;

import com.google.common.collect.Lists;

public class JooxTests {

  private static final Logger LOGGER = LoggerFactory.getLogger("JooxTests");

  public static void main(String... args) throws Exception {

    testSimpleXmlFile();

    testSceneFile();

  }

  private static void testSimpleXmlFile() throws SAXException, IOException {
    InputStream xmlInputStream = JooxTests.class.getClassLoader().getResourceAsStream("proto/yaphyre/JooxTests.xml");
    try {

      LOGGER.debug("Reading inputFile");
      Document document = $(xmlInputStream).document();
      LOGGER.debug("Document read: {}", document);

      Match x1 = $(document);
      LOGGER.debug("Document: {}", x1);

      Match x2 = $(document).find("book");
      LOGGER.debug("x2: {}", x2);

      Match x3 = $(document).find("book").filter(even());
      Match x4 = $(document).find("book").filter(odd());
      LOGGER.debug("x3: {}", x3);
      LOGGER.debug("x4: {}", x4);

      List<String> ids = $(document).find("book").ids();
      LOGGER.debug("ids: {}", ids);

      Match x5 = $(document).find("book").filter(ids("1", "2"));
      LOGGER.debug("x5: {}", x5);

      x5 = $(document).xpath("//book[@id = 1 or @id = 2]");
      LOGGER.debug("x5: {}", x5);

    } finally {
      xmlInputStream.close();
    }
  }

  private static void testSceneFile() throws SAXException, IOException {
    InputStream xmlInputStream = new FileInputStream("scenes/first_light.scene.xml");
    try {

      Document document = $(xmlInputStream).document();
      LOGGER.debug("Scene file: {}", document);

      Match lights = $(document).find("light");
      LOGGER.debug("number of lights found: {}", lights.size());
      for (Match light : lights.each()) {
        LOGGER.debug("light.id = \"{}\"", light.id());
        LOGGER.debug("light.type = \"{}\"", light.attr("type"));
        LOGGER.debug("light.falloff = \"{}\"", light.child("falloff").attr("value"));
        LOGGER.debug("light.transform = \"{}\"", light.child("transform"));
        List<Match> transformations = light.child("transform").children().each();
        Deque<Transformation> transformationStack = Lists.newLinkedList();
        for (Match transformation : transformations) {
          if (transformation.tag().equals("translate")) {
            Transformation translation = Transformation.translate(transformation.attr("dx", Double.class), transformation.attr("dy", Double.class), transformation.attr("dz", Double.class));
            LOGGER.debug("transformation: {}", translation);
            transformationStack.push(translation);
          } else if (transformation.tag().equals("scale")) {
            Transformation scale = Transformation.scale(transformation.attr("sx", Double.class), transformation.attr("sy", Double.class), transformation.attr("sz", Double.class));
            LOGGER.debug("scale: {}", scale);
            transformationStack.push(scale);
          }
        }
        Transformation transformationResult = Transformation.IDENTITY;
        while (!transformationStack.isEmpty()) {
          transformationResult = transformationResult.mul(transformationStack.pop());
        }
        LOGGER.debug("resulting transformation: {}", transformationResult);
      }

    } finally {
      xmlInputStream.close();
    }

  }

}
