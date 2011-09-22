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
package yaphyre.util;

import java.io.File;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import yaphyre.lights.Lightsources;
import yaphyre.raytracer.Scene;
import yaphyre.shaders.Material;
import yaphyre.shaders.Shaders;
import yaphyre.shapes.Shapes;

import com.google.common.base.Strings;

public class XMLFileSceneReader implements SceneReaders<File> {

  private static final Logger LOGGER = LoggerFactory.getLogger(XMLFileSceneReader.class);

  @Override
  public Scene readScene(File source) {

    Scene scene = null;

    try {
      scene = readSceneImpl(source);
    } catch (Exception e) {
      scene = null;
      LOGGER.error("Cannot parse input file: {}", e.getMessage());
    }

    return scene;

  }

  private Scene readSceneImpl(File source) throws Exception {
    SAXParser parser = createParser();
    SaxSceneHandler handler = new SaxSceneHandler();
    parser.parse(source, handler);
    return handler.getScene();
  }

  private SAXParser createParser() throws ParserConfigurationException, SAXException {
    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    return saxParserFactory.newSAXParser();
  }

  private class SaxSceneHandler extends DefaultHandler {

    private final Logger logger = LoggerFactory.getLogger(SaxSceneHandler.class);

    private static final int X_INDEX = 0, Y_INDEX = 1, Z_INDEX = 2;

    private static final int R_INDEX = 0, G_INDEX = 1, B_INDEX = 2, A_INDEX = 3;

    private static final int AMB_INDEX = 0, DIFF_INDEX = 1, SPEC_INDEX = 2, POW_INDEX = 3, REFL_INDEX = 4, REFR_INDEX = 5, IOR_INDEX = 6;

    private static final String ID_ATTR = "id";

    private static final String REF_ATTR = "ref";
    
    private static final String OBJECT_TYPE = "objectType";

    private final Map<String, Shapes> shapes = new HashMap<String, Shapes>();

    private final Map<String, Lightsources> lights = new HashMap<String, Lightsources>();

    private final Map<String, Material> materials = new HashMap<String, Material>();

    private final Map<String, Shaders> shaders = new HashMap<String, Shaders>();

    private final Deque<Map<String, String>> objectStack = new LinkedList<Map<String, String>>();

    private Scene scene;

    public Scene getScene() {
      return this.scene;
    }

    @Override
    public void startDocument() throws SAXException {
      this.logger.debug("startDocument");
      this.scene = new Scene();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      this.logger.debug("startElement: {} [{}]", qName, attributeList(attributes));
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
      String tmp = new String(ch, start, length).replace("\n\r\t", " ").trim();
      this.logger.debug("characters: '{}'", tmp);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
      this.logger.debug("endElement: {}", qName);
    }

    @Override
    public void endDocument() throws SAXException {
      this.logger.debug("endDocument");
    }

    private double[] readXYZ(Attributes attributes) {
      double[] result = new double[3];
      String rawX = attributes.getValue("x");
      String rawY = attributes.getValue("y");
      String rawZ = attributes.getValue("z");

      result[X_INDEX] = stringToDouble(rawX);
      result[Y_INDEX] = stringToDouble(rawY);
      result[Z_INDEX] = stringToDouble(rawZ);

      return result;
    }

    private double[] readRGBA(Attributes attributes) {
      double[] result = new double[4];
      String rawR = attributes.getValue("r");
      String rawG = attributes.getValue("g");
      String rawB = attributes.getValue("b");
      String rawA = attributes.getValue("a");

      result[R_INDEX] = stringToDouble(rawR);
      result[G_INDEX] = stringToDouble(rawG);
      result[B_INDEX] = stringToDouble(rawB);
      result[A_INDEX] = stringToDouble(rawA);

      return result;
    }

    private double[] readMaterialSettings(Attributes attributes) {
      double[] result = new double[7];
      String rawAmbient = attributes.getValue("ambient");
      String rawDiffuse = attributes.getValue("diffuse");
      String rawSpecular = attributes.getValue("specular");
      String rawPower = attributes.getValue("power");
      String rawReflection = attributes.getValue("reflection");
      String rawRefraction = attributes.getValue("refraction");
      String rawIor = attributes.getValue("ior");

      result[AMB_INDEX] = stringToDouble(rawAmbient);
      result[DIFF_INDEX] = stringToDouble(rawDiffuse);
      result[SPEC_INDEX] = stringToDouble(rawSpecular);
      result[POW_INDEX] = stringToDouble(rawPower);
      result[REFL_INDEX] = stringToDouble(rawReflection);
      result[REFR_INDEX] = stringToDouble(rawRefraction);
      result[IOR_INDEX] = stringToDouble(rawIor);

      return result;
    }

    private double stringToDouble(String stringValue) {
      return (Strings.isNullOrEmpty(stringValue)) ? 0d : Double.parseDouble(stringValue);
    }

    private String attributeList(Attributes attributes) {
      StringBuilder result = new StringBuilder();
      boolean isFirst = true;
      for (int i = 0; i < attributes.getLength(); i++) {
        if (!isFirst) {
          result.append(", ");
        }
        isFirst = false;
        result.append(attributes.getQName(i));
        result.append(" = ");
        result.append(attributes.getValue(i));
      }
      return result.toString();
    }
  }

}
