package yaphyre.util.scenereaders.entityhandlers;

import java.util.Map;

import org.joox.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yaphyre.core.Shader;
import yaphyre.core.Shape;
import yaphyre.geometry.Transformation;
import yaphyre.shaders.Material;
import yaphyre.shapes.Plane;

import com.google.common.base.Preconditions;

public class PlaneEntityHandler extends EntityHandler<IdentifiableObject<Shape>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(PlaneEntityHandler.class);

  @Override
  public IdentifiableObject<Shape> decodeEntity(Match entityMatch, Map<String, IdentifiableObject<Material>> knownMaterials, Map<String, IdentifiableObject<Shader>> knownShaders, Map<String, IdentifiableObject<Shape>> knownShapes) {
    LOGGER.trace("enter decodeEntity: {}", entityMatch);

    Preconditions.checkArgument(entityMatch.tag().equals("plane"));

    String id = entityMatch.id();
    Transformation object2World = super.decodeTransform(entityMatch);
    String shaderRef = entityMatch.child("shader").attr("ref");
    Shader shader = knownShaders.get(shaderRef).getObject();
    Plane plane = new Plane(object2World, shader, true);

    IdentifiableObject<Shape> result = new IdentifiableObject<Shape>(id, plane);

    LOGGER.trace("exit decodeEntity: {}", result);

    return result;
  }

  @Override
  public String getXPath() {
    return "/scene/plane";
  }

}
