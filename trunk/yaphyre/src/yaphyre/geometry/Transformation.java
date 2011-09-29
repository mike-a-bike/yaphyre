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

/**
 * This class encapsulates a series of transformations.
 * 
 * @version $Revision$
 * 
 * @author Michael Bieri
 * @author $LastChangedBy$
 */
public class Transformation {

  public static final Transformation IDENTITY = new Transformation(Matrix.IDENTITY);

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

  public Vector3D transformPoint(Vector3D pointVector) {
    throw new RuntimeException("Not implemented yet");
  }

  public Vector3D transformDirection(Vector3D direction) {
    throw new RuntimeException("Not implemented yet");
  }

  public Vector3D transformNormal(Vector3D normal) {
    throw new RuntimeException("Not implemented yet");
  }

  public Ray transform(Ray ray) {
    throw new RuntimeException("Not implemented yet");
  }

}
