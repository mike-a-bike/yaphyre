package yaphyre.util.scenereaders.entityhandlers;

import java.util.Map;

import org.joox.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yaphyre.core.Shader;
import yaphyre.core.Shape;
import yaphyre.shaders.Material;

import com.google.common.base.Preconditions;

public class MaterialEntityHandler extends EntityHandler<IdentifiableObject<Material>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(MaterialEntityHandler.class);

  @Override
  public IdentifiableObject<Material> decodeEntity(Match entityMatch, Map<String, IdentifiableObject<Material>> knownMaterials, Map<String, IdentifiableObject<Shader>> knownShaders, Map<String, IdentifiableObject<Shape>> knownShapes) {
    LOGGER.trace("enter decodeEntity: {}", entityMatch);

    Preconditions.checkArgument(entityMatch.tag().equals("material"));

    String id = entityMatch.id();
    double ambient = this.readNumericAttribute(entityMatch, "ambient", 0d, Double.class);
    double diffuse = this.readNumericAttribute(entityMatch, "diffuse", 0d, Double.class);
    double specular = this.readNumericAttribute(entityMatch, "specular", 0d, Double.class);
    double reflection = this.readNumericAttribute(entityMatch, "reflection", 0d, Double.class);
    double refraction = this.readNumericAttribute(entityMatch, "refraction", 0d, Double.class);

    IdentifiableObject<Material> result = new IdentifiableObject<Material>(id, new Material(ambient, diffuse, specular, reflection, refraction));

    LOGGER.trace("exit decodeEntity: {}", result);

    return result;
  }

  @Override
  public String getXPath() {
    return "/scene/material";
  }

}
