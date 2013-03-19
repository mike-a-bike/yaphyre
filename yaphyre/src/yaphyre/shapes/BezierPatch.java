package yaphyre.shapes;

import static com.google.common.base.Preconditions.checkArgument;

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

	private static final double U_RESOLUTION = 0.001;
	private static final double V_RESOLUTION = U_RESOLUTION;

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

	private void tessellateMesh() {
		state = TessellationState.Tessellated;
	}

	private void tessellateIfNecessary() {
		switch (state) {
		case NotTessellated:
			tessellateMesh();
			break;
		case Failed:
			throw new IllegalStateException("Failed to tessellate bezier patch: " + toString());
		}
	}

	private static enum TessellationState {
		NotTessellated, Tessellated, Failed
	}

}
