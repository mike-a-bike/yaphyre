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

import java.text.MessageFormat;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import yaphyre.geometry.Point3D;
import yaphyre.geometry.Transformation;
import yaphyre.lights.Lightsources;
import yaphyre.raytracer.Scene;
import yaphyre.shaders.Material;
import yaphyre.shaders.Shaders;
import yaphyre.shaders.SimpleShader;
import yaphyre.shapes.Shapes;
import yaphyre.util.Color;

import com.google.common.base.Strings;

class SaxSceneHandler extends DefaultHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(SaxSceneHandler.class);

  private static final String GENERIC_VALUES_NAME = "values";

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

  private final Deque<Object> elementStack = new LinkedList<Object>();

  private Scene scene;

  public Scene getScene() {
    return this.scene;
  }

  @Override
  public void startDocument() throws SAXException {
    LOGGER.debug("startDocument");
    this.scene = new Scene();
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    LOGGER.debug("startElement: {} [{}]", qName, attributeList(attributes));

    String elementType = qName.toLowerCase();
    String elementId = getId(attributes);
    String elementRef = getRef(attributes);

    SceneElement element = new SceneElement(elementType, elementId, elementRef);
    boolean pushPlaceholderToStack = true;

    // TODO: replace with string-switch in Java 7
    // transformations
    if (elementType.equals("translate") || elementType.equals("scale") || elementType.equals("rotate")) {
      element.addAttribute(GENERIC_VALUES_NAME, readXYZ(attributes));
    }
    // camera data
    else if (elementType.equals("location") || elementType.equals("look-at")) {
      double[] pointValues = readXYZ(attributes);
      element.addAttribute(GENERIC_VALUES_NAME, new Point3D(pointValues[X_INDEX], pointValues[Y_INDEX], pointValues[Z_INDEX]));
    }
    // color
    else if (elementType.equals("color")) {
      double[] colorValues = readRGBA(attributes);
      Color color = new Color(colorValues[R_INDEX], colorValues[G_INDEX], colorValues[B_INDEX]);
      this.elementStack.push(color);
      pushPlaceholderToStack = false;
    }
    // material
    else if (elementType.equals("material")) {
      Material material = null;
      if (Strings.isNullOrEmpty(elementId)) {
        if (Strings.isNullOrEmpty(elementRef)) {
          throw new SAXException("either 'id' or 'ref' must be specified for materials");
        }
        material = this.materials.get(elementRef);
        if (material == null) {
          throw new SAXException("unknown material '" + elementRef + "'");
        }
        this.elementStack.push(material);
      } else {
        double[] materialSettings = readMaterialSettings(attributes);
        material = new Material(materialSettings[AMB_INDEX], materialSettings[DIFF_INDEX], materialSettings[SPEC_INDEX], materialSettings[REFL_INDEX], materialSettings[REFR_INDEX]);
        this.materials.put(elementId, material);
      }
      pushPlaceholderToStack = false;
    }
    // shader (handle references here)
    else if (elementType.equals("shader")) {
      if (Strings.isNullOrEmpty(elementId)) {
        if (Strings.isNullOrEmpty(elementRef)) {
          throw new SAXException("either 'id' or 'ref' must be specified for shaders");
        }
        Shaders shader = this.shaders.get(elementRef);
        if (shader == null) {
          throw new SAXException("unknown shader '" + elementRef + "'");
        }
        pushPlaceholderToStack = false;
        this.elementStack.push(shader);
      }
    }
    // handle spheres
    else if (elementType.equals("sphere")) {
      double[] centerCoordinates = readXYZ(attributes);
      Point3D center = new Point3D(centerCoordinates[X_INDEX], centerCoordinates[Y_INDEX], centerCoordinates[Z_INDEX]);
      double radius = Double.valueOf(attributes.getValue("r"));
      element.addAttribute("CENTER", center);
      element.addAttribute("RADIUS", radius);
    }
    // everything else
    else {
      element.setAttributes(createAttributeMap(attributes));
    }

    if (pushPlaceholderToStack) {
      this.elementStack.push(element);
    }

  }

  private Map<String, Object> createAttributeMap(Attributes attributes) {
    Map<String, Object> result = new HashMap<String, Object>();
    for (int i = 0; i < attributes.getLength(); i++) {
      String name = attributes.getQName(i);
      if (name.equalsIgnoreCase(ID_ATTR) || name.equalsIgnoreCase(REF_ATTR)) {
        continue;
      }
      result.put(name, attributes.getValue(i));
    }
    return result;
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    LOGGER.debug("endElement: {}", qName);

    String elementType = qName.toLowerCase();

    if (elementType.equals("transform")) {
      // rethink this section...

      // Deque<SceneElement> transformationStack = new
      // LinkedList<SceneElement>();
      //
      // while (true) {
      // SceneElement element = (SceneElement)this.elementStack.pop();
      // if (element.getType().equals("transform")) {
      // break;
      // }
      // transformationStack.push(element);
      // }
      //
      // TransformationMatrixBuilder builder =
      // TransformationMatrixBuilder.matrix();
      // while (!transformationStack.isEmpty()) {
      // SceneElement element = transformationStack.pop();
      // double[] values = (double[])element.getAttribute(GENERIC_VALUES_NAME);
      // if (element.getType().equals("translate")) {
      // builder.forTranslation(values[X_INDEX], values[Y_INDEX],
      // values[Z_INDEX]);
      // } else if (element.getType().equals("scale")) {
      // builder.forScale(values[X_INDEX], values[Y_INDEX], values[Z_INDEX]);
      // } else if (element.getType().equals("rotate")) {
      // builder.forRotation(values[X_INDEX], values[Y_INDEX], values[Z_INDEX]);
      // }
      // }
      //
      // this.elementStack.push(new Transformation(builder.build()));
    }

    else if (elementType.equals("shader")) {
      Material material = null;
      Color color = null;

      for (int i = 0; i < 2; i++) {
        Object obj = this.elementStack.pop();
        if (Color.class.isAssignableFrom(obj.getClass())) {
          color = Color.class.cast(obj);
        } else if (Material.class.isAssignableFrom(obj.getClass())) {
          material = Material.class.cast(obj);
        } else {
          throw new SAXException("unknown object instance found for shader initialization: " + obj.getClass().getSimpleName());
        }
      }
      if (material == null || color == null) {
        throw new SAXException("shaders must define a color and a material");
      }
      SceneElement shaderElement = (SceneElement)this.elementStack.pop();

      Shaders shader = new SimpleShader(shaderElement.getId(), material, color);
      this.shaders.put(shaderElement.getId(), shader);
    }

    else if (elementType.equals("pointlight")) {
      Transformation transformation = Transformation.IDENTITY;
      Color color = null;

    }

    else if (elementType.equals("sphere")) {
      ;
    }

    else if (elementType.equals("plane")) {
      ;
    }

    else if (elementType.equals("location")) {
      ;
    }

    else if (elementType.equals("look-at")) {
      ;
    }

  }

  @Override
  public void endDocument() throws SAXException {
    LOGGER.debug("endDocument");
    assert (this.elementStack.isEmpty()) : "Object stack is not empty -> inconsisten data";
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

  private String getId(Attributes attributes) {
    int idPos = attributes.getIndex(ID_ATTR);
    if (idPos != -1) {
      return attributes.getValue(idPos);
    }
    return null;
  }

  private String getRef(Attributes attributes) {
    int refPos = attributes.getIndex(REF_ATTR);
    if (refPos != -1) {
      return attributes.getValue(refPos);
    }
    return null;
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

  private static class SceneElement {
    private final Map<String, Object> attributes = new HashMap<String, Object>();

    private final String type;

    private final String id;

    private final String ref;

    public SceneElement(String type, String id, String ref) {
      this.type = type;
      this.id = id;
      this.ref = ref;
    }

    @Override
    public String toString() {
      return MessageFormat.format("SceneElement [{0}, {1}, {2}]", this.type, this.id, this.ref);
    }

    public String getType() {
      return this.type;
    }

    public String getId() {
      return this.id;
    }

    public String getRef() {
      return this.ref;
    }

    public Map<String, Object> getAttributes() {
      return this.attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
      this.attributes.clear();
      this.attributes.putAll(attributes);
    }

    public void addAttribute(String name, Object value) {
      this.attributes.put(name, value);
    }

    public Object getAttribute(String name) {
      return this.attributes.get(name);
    }

  }

}