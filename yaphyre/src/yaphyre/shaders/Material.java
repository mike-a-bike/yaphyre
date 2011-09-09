package yaphyre.shaders;

public class Material {

  private final double ambient;

  private final double diffuse;

  private final double specular;

  private final double reflection;

  private final double refraction;

  public Material(double ambient, double diffuse, double specular, double reflection, double refraction) {
    this.ambient = ambient;
    this.diffuse = diffuse;
    this.specular = specular;
    this.reflection = reflection;
    this.refraction = refraction;
  }

  public double getAmbient() {
    return this.ambient;
  }

  public double getDiffuse() {
    return this.diffuse;
  }

  public double getSpecular() {
    return this.specular;
  }

  public double getReflection() {
    return this.reflection;
  }

  public double getRefraction() {
    return this.refraction;
  }

}
