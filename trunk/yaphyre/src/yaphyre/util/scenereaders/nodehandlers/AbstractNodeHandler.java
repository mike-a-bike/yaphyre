package yaphyre.util.scenereaders.nodehandlers;

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public abstract class AbstractNodeHandler<T> implements NodeHandler<T> {

  @Override
  public abstract T handleNode(Node node);

  protected String getAttributeValue(Node node, String attributeName) {
    return this.getAttributeValue(node, attributeName, null);
  }

  protected String getAttributeValue(Node node, String attributeName, String nullSubstitue) {
    NamedNodeMap attributes = node.getAttributes();
    Node attribute = attributes.getNamedItem(attributeName);
    if (attribute == null) {
      return nullSubstitue;
    } else {
      try {
        String attributeValue = attribute.getNodeValue();
        if (attributeValue == null) {
          return nullSubstitue;
        } else {
          return attributeValue;
        }
      } catch (DOMException e) {
        return nullSubstitue;
      }
    }
  }

}