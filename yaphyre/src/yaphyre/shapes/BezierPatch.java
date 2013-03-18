package yaphyre.shapes;

import yaphyre.core.BoundingBox;
import yaphyre.core.CollisionInformation;
import yaphyre.core.Shader;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;
import yaphyre.geometry.Transformation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public class BezierPatch extends AbstractShape {

	private final int uDimension;
	private final int vDimension;
	private final int startIndex;
	private final Point3D[] controlPoints;

	private State state;

	private TriangleMesh tesselatedPatch = null;

	/**
	 * {@inheritDoc}
	 */
	protected BezierPatch(@NotNull final Transformation objectToWorld, @NotNull final Shader shader, int uDimension, int vDimension, int startIndex, @NotNull Point3D... controlPoints) {
		super(objectToWorld, shader);
		this.uDimension = uDimension;
		this.vDimension = vDimension;
		this.startIndex = startIndex;
		this.controlPoints = controlPoints;
		state = State.NotInitialized;
	}

	@NotNull
	@Override
	public BoundingBox getBoundingBox() {
		handleState();
		return null;
	}

	@Nullable
	@Override
	public CollisionInformation intersect(@NotNull final Ray ray) {
		handleState();
		return null;
	}

	private void handleState() {
		if (state == State.NotInitialized) {
			initialize();
		}
		if (state == State.Failed) {
			throw new IllegalStateException("Failed to tesselate bezier patch: " + toString());
		}
	}

	private void initialize() {
		state = State.Initialized;
	}

	private static enum State {
		NotInitialized, Initialized, Failed
	}

}
