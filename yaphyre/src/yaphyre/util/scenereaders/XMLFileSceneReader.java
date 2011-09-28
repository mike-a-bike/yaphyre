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
package yaphyre.util.scenereaders;

import java.io.File;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import yaphyre.raytracer.Scene;

/**
 * This is the implementation which read an xml file and creates the scene
 * definition from it. The actual parsing of the xml is done by a SAX parser
 * using a custom handler. The {@link SaxSceneHandler} creates the actual scene.
 * 
 * @version $Revision: 37 $
 * 
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 */
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

}
