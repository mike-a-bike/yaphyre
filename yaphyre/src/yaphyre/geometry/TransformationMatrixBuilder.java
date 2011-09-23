/*
 * Copyright 2011 Michael Bieri
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package yaphyre.geometry;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static yaphyre.math.MathUtils.toRad;

import java.util.LinkedList;
import java.util.Queue;

/**
 * see {@link www.cosc.brocku.ca/Offerings/3P98/course/lectures/2d_3d_xforms/}
 * 
 * @version $Revision$
 * 
 * @author Michael Bieri
 * @author $LastChangedBy$
 */
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
    if (ax != 0) {
      double axRad = toRad(ax);
      this.transformationQueue.offer(new Matrix(new double[][] { {1, 0, 0, 0}, {0, cos(axRad), -sin(axRad), 0}, {0, sin(axRad), cos(axRad), 0}, {0, 0, 0, 1}}));
    }
    if (ay != 0) {
      double ayRad = toRad(ay);
      this.transformationQueue.offer(new Matrix(new double[][] { {cos(ayRad), 0, sin(ayRad), 0}, {0, 1, 0, 0}, {-sin(ayRad), 0, cos(ayRad), 0}, {0, 0, 0, 1}}));
    }
    if (az != 0) {
      double azRad = toRad(az);
      this.transformationQueue.offer(new Matrix(new double[][] { {cos(azRad), -sin(azRad), 0, 0}, {sin(azRad), cos(azRad), 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}}));
    }
    // Interpret each as rotation angle in degree
    return this;
  }

  public TransformationMatrixBuilder forScale(double sx, double sy, double sz) {
    this.transformationQueue.offer(new Matrix(new double[][] { {sx, 0, 0, 0}, {0, sy, 0, 0}, {0, 0, sz, 0}, {0, 0, 0, 1}}));
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
