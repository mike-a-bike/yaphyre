package yaphyre.util.scenereaders.nodehandlers;

import org.w3c.dom.Node;

public interface NodeHandler<T> {

  T handleNode(Node node);

}
