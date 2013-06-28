/*
 * Copyright 2013 Michael Bieri
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

package yaphyre.shapes;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.math.util.FastMath.min;

import yaphyre.core.BoundingBox;
import yaphyre.core.CollisionInformation;
import yaphyre.core.Shader;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Transformation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Shape representing a BezierPatch geometry. Internally a TriangleMesh is used to approximate the bezier patch.
 *
 * @author Michael Bieri
 * @since 17.03.2013
 */
public class BezierPatch extends AbstractShape {

	private static final int U_RESOLUTION = 10;
	private static final int V_RESOLUTION = U_RESOLUTION;

	private final int uDimension;
	private final int vDimension;
	private final Point3D[] controlPoints;
	private final int startIndex;
	private final int lastControlPointIndex;
	private final int controlPointCount;

	private TessellationState state;
	private TriangleMesh tessellatedPatch = null;
	private BoundingBox boundingBox = null;

	/**
	 * {@inheritDoc}
	 */
	protected BezierPatch(@NotNull final Transformation objectToWorld, @NotNull final Shader shader, int uDimension,
			int vDimension, int startIndex, @NotNull Point3D... controlPoints) {
		super(objectToWorld, shader);
		checkArgument(startIndex >= 0);
		controlPointCount = uDimension * vDimension;
		lastControlPointIndex = startIndex + controlPointCount;
		checkArgument((controlPoints.length - startIndex) >= controlPointCount);
		this.uDimension = uDimension;
		this.vDimension = vDimension;
		this.startIndex = startIndex;
		this.controlPoints = controlPoints;
		state = TessellationState.NotTessellated;
		initializeBoundingBox();
	}

	private void initializeBoundingBox() {
		BoundingBox boundingBox = new BoundingBox(controlPoints[startIndex]);
		for(int pointIndex = startIndex + 1; pointIndex < lastControlPointIndex; pointIndex++) {
			boundingBox = BoundingBox.union(boundingBox, controlPoints[pointIndex]);
		}
	}

	@NotNull
	@Override
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	@Nullable
	@Override
	public CollisionInformation intersect(@NotNull final Ray ray) {
		tessellateIfNecessary();
		return null;
	}

	private void tessellateMesh(final int uResolution, final int vResolution) {
		Point3D[] patchPoints = calculateMeshPoints(uResolution, vResolution);
		int[][] triangles = createTriangleIndices(uResolution, vResolution);
		tessellatedPatch = new TriangleMesh(Transformation.IDENTITY, getShader(), patchPoints, triangles);
		state = TessellationState.Tessellated;
	}

	Point3D[] calculateMeshPoints(final int uResolution, final int vResolution) {
		final int arraySize = (uResolution + 1) * (vResolution + 1);
		final double uStepSize = 1d / uResolution;
		final double vStepSize = 1d / vResolution;
		Point3D[] patchPoints = new Point3D[arraySize];
		for(int uStep = 0; uStep <= uResolution; uStep++) {
			for(int vStep = 0; vStep <= vResolution; vStep++) {
				final double u = min(1d, uStep * uStepSize);
				final double v = min(1d, vStep * vStepSize);
				patchPoints[(vStep + uStep * vDimension)] = yaphyre.geometry.BezierPatch.GENERIC.calculateMeshPoint(u, v, uDimension, vDimension, 0, controlPoints);
			}
		}
		return patchPoints;
	}

	int[][] createTriangleIndices(final int uResolution, final int vResolution) {
		int[][] triangles = new int[uResolution * vResolution * 2][3];
		final int vStripSize = vResolution + 1;
		for(int uStep = 0, triangleIndex = 0; uStep < uResolution; uStep++) {
			for(int vStep = 0; vStep < vResolution; vStep++) {
				triangles[triangleIndex] = new int[3];
				triangles[triangleIndex][0] = vStep + uStep * vStripSize;
				triangles[triangleIndex][1] = vStep + (uStep + 1) * vStripSize;
				triangles[triangleIndex][2] = vStep + 1 + (uStep + 1) * vStripSize;
				triangleIndex++;
				triangles[triangleIndex] = new int[3];
				triangles[triangleIndex][0] = vStep + uStep * vStripSize;
				triangles[triangleIndex][1] = vStep + 1 + (uStep + 1) * vStripSize;
				triangles[triangleIndex][2] = vStep + 1 + uStep * vStripSize;
				triangleIndex++;
			}
		}
		return triangles;
	}

	private void tessellateIfNecessary() {
		switch (state) {
		case NotTessellated:
			tessellateMesh(U_RESOLUTION, V_RESOLUTION);
			break;
		case Failed:
			throw new IllegalStateException("Failed to tessellate bezier patch: " + toString());
		}
	}

	private static enum TessellationState {
		NotTessellated, Tessellated, Failed
	}

}
