package yaphyre.util.scenereaders.nodehandlers;

import org.w3c.dom.Node;

import yaphyre.core.Camera;
import yaphyre.core.Film;

public class CameraHandler implements NodeHandler<Camera<? extends Film>> {

  @Override
  public Camera<? extends Film> handleNode(Node node) {
    return null;
  }

}
