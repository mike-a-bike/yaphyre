package yaphyre.geometry;


public class TransformationMatrixBuilder {

  public static TransformationMatrixBuilder matrix() {
    return new TransformationMatrixBuilder();
  }

  public TransformationMatrixBuilder forTranslation(double dx, double dy, double dz) {
    return this;
  }

  public TransformationMatrixBuilder forTranslation(Vector translationVector) {
    return this.forTranslation(translationVector.getX(), translationVector.getY(), translationVector.getZ());
  }

  public TransformationMatrixBuilder forRotation(double ax, double ay, double az) {
    return this;
  }

  public Matrix build() {
    return null;
  }

}
