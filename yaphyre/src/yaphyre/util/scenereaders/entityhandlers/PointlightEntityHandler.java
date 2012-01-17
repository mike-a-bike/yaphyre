package yaphyre.util.scenereaders.entityhandlers;

import java.util.Collections;
import java.util.List;

import org.joox.Match;

import yaphyre.core.Lightsource;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Transformation;
import yaphyre.lights.Falloff;
import yaphyre.lights.Pointlight;
import yaphyre.util.Color;
import yaphyre.util.scenereaders.utils.HelperFactory;

import com.google.common.base.Preconditions;

public class PointlightEntityHandler extends EntityHandler<IdentifiableObject<Lightsource>> {

  @Override
  public IdentifiableObject<Lightsource> decodeEntity(Match entityMatch) {

    Preconditions.checkArgument(entityMatch.tag().equals("light"));
    Preconditions.checkArgument(entityMatch.attr("type").equals("point"));

    String id = entityMatch.id();
    Color lightColor = HelperFactory.getColorHelper().decodeEntity(entityMatch.child("color"));
    Point3D position = Point3D.ORIGIN;
    Transformation transformation = null;
    Match transformMatch = entityMatch.child("transform");
    if (!transformMatch.isEmpty()) {
      transformation = HelperFactory.getTransformationHelper().decodeEntity(transformMatch);
    }

    return new IdentifiableObject<Lightsource>(id, new Pointlight(position, lightColor, 0, Falloff.Quadric));
  }

  @Override
  public String getXPath() {
    return "//light[@type = \"point\"]";
  }

  @Override
  public List<String> getValidParents() {
    return Collections.emptyList();
  }

}
