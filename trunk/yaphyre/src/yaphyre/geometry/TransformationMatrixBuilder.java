package yaphyre.geometry;

import java.util.LinkedList;
import java.util.Queue;

public class TransformationMatrixBuilder {

  private final Queue<Matrix> transformationQueue;

  public static TransformationMatrixBuilder matrix() {
    return new TransformationMatrixBuilder();
  }

  private TransformationMatrixBuilder() {
    this.transformationQueue = new LinkedList<Matrix>();
  }

  public TransformationMatrixBuilder forTranslation(double dx, double dy, double dz) {
    this.transformationQueue.offer(new Matrix(new double[][] { {1, 0, 0, dx}, {0, 1, 0, dy}, {0, 0, 1, dz}, {0, 0, 0, 1}}));
    return this;
  }

  public TransformationMatrixBuilder forTranslation(Vector translationVector) {
    return this.forTranslation(translationVector.getX(), translationVector.getY(), translationVector.getZ());
  }

  public TransformationMatrixBuilder forRotation(double ax, double ay, double az) {
    return this;
  }

  public Matrix build() {
    Matrix result = Matrix.IDENTITY;
    while (!this.transformationQueue.isEmpty()) {
      result = result.mul(this.transformationQueue.poll());
    }
    return result;
  }

}
