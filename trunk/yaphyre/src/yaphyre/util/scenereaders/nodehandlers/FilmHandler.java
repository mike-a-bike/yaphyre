package yaphyre.util.scenereaders.nodehandlers;

import org.w3c.dom.Node;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import yaphyre.core.Film;
import yaphyre.films.ImageFile;
import yaphyre.films.ImageFile.ImageFormat;

public class FilmHandler extends AbstractNodeHandler<Film> {

  @Override
  public Film handleNode(Node node) {
    String filmType = Preconditions.checkNotNull(super.getAttributeValue(node, "type"));
    Film result = null;
    if (filmType.equalsIgnoreCase("image")) {
      int xRes = Integer.parseInt(Preconditions.checkNotNull(super.getAttributeValue(node, "xRes")));
      int yRes = Integer.parseInt(Preconditions.checkNotNull(super.getAttributeValue(node, "yRes")));
      String fileType = super.getAttributeValue(node, "fileType");
      if (Strings.isNullOrEmpty(fileType)) {
        result = new ImageFile(xRes, yRes);
      } else {
        result = new ImageFile(xRes, yRes, Enum.valueOf(ImageFormat.class, fileType.trim()));
      }
    }
    return result;
  }

}
