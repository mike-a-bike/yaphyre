package yaphyre.shapes;

import yaphyre.core.BoundingBox;
import yaphyre.core.CollisionInformation;
import yaphyre.geometry.Ray;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public class BezierPatch extends AbstractShape {

	private TriangleMesh tesselatedPatch = null;

	@Nullable
	@Override
	public CollisionInformation intersect(@NotNull final Ray ray) {
		return null;
	}

	@NotNull
	@Override
	public BoundingBox getBoundingBox() {
		return null;
	}

}
