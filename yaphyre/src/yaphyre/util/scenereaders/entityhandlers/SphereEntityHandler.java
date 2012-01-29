package yaphyre.util.scenereaders.entityhandlers;

import java.util.Map;

import org.joox.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yaphyre.core.Shader;
import yaphyre.core.Shape;
import yaphyre.geometry.Transformation;
import yaphyre.shaders.Material;
import yaphyre.shapes.Sphere;

import com.google.common.base.Preconditions;

public class SphereEntityHandler extends EntityHandler<IdentifiableObject<Shape>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(SphereEntityHandler.class);

  @Override
  public IdentifiableObject<Shape> decodeEntity(Match entityMatch, Map<String, IdentifiableObject<Material>> knownMaterials, Map<String, IdentifiableObject<Shader>> knownShaders, Map<String, IdentifiableObject<yaphyre.core.Shape>> knownShapes) {
    LOGGER.trace("enter decodeEntity: {}", entityMatch);

    Preconditions.checkArgument(entityMatch.tag().equals("sphere"));

    String id = entityMatch.id();
    Transformation object2World = super.decodeTransform(entityMatch);
    String shaderRef = entityMatch.child("shader").attr("ref");
    Shader shader = knownShaders.get(shaderRef).getObject();
    Shape sphere = new Sphere(object2World, shader, true);

    IdentifiableObject<Shape> result = new IdentifiableObject<Shape>(id, sphere);

    LOGGER.trace("exiting decodeEntity: {}", result);

    return result;
  }

  @Override
  public String getXPath() {
    return "//sphere";
  }

}
