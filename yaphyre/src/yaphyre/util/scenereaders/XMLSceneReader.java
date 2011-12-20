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

import java.io.IOException;
import java.io.InputStream;

import javax.swing.TransferHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import yaphyre.core.SceneReaders;
import yaphyre.raytracer.Scene;
import yaphyre.util.scenereaders.nodehandlers.NodeHandler;
import yaphyre.util.scenereaders.nodehandlers.TransformationHandler;

import com.google.common.annotations.Beta;

/**
 * This is the implementation which read an xml file and creates the scene
 * definition from it. The actual parsing of the xml is done by a DOM reader.
 *
 * @version $Revision: 37 $
 *
 * @author Michael Bieri
 * @author $LastChangedBy: mike0041@gmail.com $
 */
public class XMLSceneReader implements SceneReaders<InputStream> {

  private static final Logger LOGGER = LoggerFactory.getLogger(XMLSceneReader.class);

  @Override
  public Scene readScene(InputStream source) {

    Scene scene = null;

    try {
      scene = readSceneImpl(source);
    } catch (Exception e) {
      scene = null;
      LOGGER.error("Cannot parse input file: {}", e.getMessage());
    }

    return scene;

  }

  private Scene readSceneImpl(InputStream source) throws ParserConfigurationException, SAXException, IOException, HandlerNotFoundException {
    Document sceneDocument = this.readDocument(source);
    Element rootElement = sceneDocument.getDocumentElement();
    rootElement.normalize();

    this.printNode(rootElement, "");

    NodeList sceneElements = rootElement.getChildNodes();

    for(int sceneElementIndex = 0; sceneElementIndex < sceneElements.getLength(); sceneElementIndex++) {
      Node sceneElement = sceneElements.item(sceneElementIndex);
      if (sceneElement.getNodeType() != Node.ELEMENT_NODE) {
        continue;
      }
      NodeHandler<?> handler = createHandler(sceneElement.getNodeName());
    }

    return null;
  }

  private NodeHandler<?> createHandler(String nodeName) throws HandlerNotFoundException {
    if (nodeName.equalsIgnoreCase("transformation")) {
      return new TransformationHandler();
    }
    throw new HandlerNotFoundException(nodeName);
  }

  private Document readDocument(InputStream source) throws ParserConfigurationException, SAXException, IOException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document document = builder.parse(source);
    return document;
  }

  @Beta
  private void printNode(Node baseNode, String spacer) {
    LOGGER.debug(spacer + baseNode.getNodeName());
    NodeList children = baseNode.getChildNodes();
    for(int index = 0; index < children.getLength(); index++) {
      this.printNode(children.item(index), spacer + "..");
    }
  }

}
