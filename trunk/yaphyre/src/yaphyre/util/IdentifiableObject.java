package yaphyre.util;

public abstract class IdentifiableObject {
  private final String id;

  public IdentifiableObject(String id) {
    this.id = id;
  }

  public String getId() {
    return this.id;
  }

}
