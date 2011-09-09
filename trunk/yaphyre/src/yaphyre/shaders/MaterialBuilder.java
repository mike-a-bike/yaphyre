package yaphyre.shaders;

/**
 * Helper class for building materials. Use it like this:<br/>
 * 
 * <pre>
 * Material mirror = MaterialBuilder.init().ambient(0.05).diffuse(0.2).reflection(0.8).build();
 * </pre>
 * 
 * @author Michael Bieri
 */
public class MaterialBuilder {

  private double ambient;

  private double diffuse;

  private double specular;

  private double reflection;

  private double refraction;

  /**
   * Needed because static and non static methods cannot be overloaded.
   * 
   * @return A new instance of {@link MaterialBuilder} ready to use.
   */
  public static MaterialBuilder init() {
    return new MaterialBuilder();
  }

  private MaterialBuilder() {
  }

  public MaterialBuilder ambient(double ambient) {
    this.ambient = ambient;
    return this;
  }

  public MaterialBuilder diffuse(double diffuse) {
    this.diffuse = diffuse;
    return this;
  }

  public MaterialBuilder specular(double specular) {
    this.specular = specular;
    return this;
  }

  public MaterialBuilder reflection(double reflection) {
    this.reflection = reflection;
    return this;
  }

  public MaterialBuilder refraction(double refraction) {
    this.refraction = refraction;
    return this;
  }

  /**
   * Factory method. This builds the actual {@link Material} instance. Please
   * notice, this method can be called multiple times.
   * 
   * @return A new {@link Material} instance with all the values from the
   *         factory set.
   */
  public Material build() {
    return new Material(this.ambient, this.diffuse, this.specular, this.reflection, this.refraction);
  }

}
