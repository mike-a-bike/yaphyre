package yaphyre.geometry;

/**
 * This class encapsulates a series of transformations.
 * 
 * @author Michael Bieri
 */
public class Transformation {

  private Matrix transformationMatrix;

  private Matrix transformationMatrixInverse;

  public Transformation() {
    this.transformationMatrix = Matrix.IDENTITY;
    this.transformationMatrixInverse = Matrix.IDENTITY;
  }

  public Transformation(Matrix matrix) {
    this.transformationMatrix = matrix;
    this.transformationMatrixInverse = matrix.inverse();
  }

  public void append(Matrix transformationMatrix) {
    this.transformationMatrix = this.transformationMatrix.mul(transformationMatrix);
    this.transformationMatrixInverse = this.transformationMatrix.inverse();
  }

  public Vector transformPoint(Vector pointVector) {
    throw new RuntimeException("Not implemented yet");
  }

  public Vector transformDirection(Vector direction) {
    throw new RuntimeException("Not implemented yet");
  }

  public Vector transformNormal(Vector normal) {
    throw new RuntimeException("Not implemented yet");
  }

  public Ray transform(Ray ray) {
    throw new RuntimeException("Not implemented yet");
  }

}
