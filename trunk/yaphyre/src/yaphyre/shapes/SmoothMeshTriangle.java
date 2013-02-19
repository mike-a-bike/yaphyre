package yaphyre.shapes;

import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Ray;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class SmoothMeshTriangle extends MeshTriangle {
	@NotNull
	public static SmoothMeshTriangle create(final Point3D v0, final Point3D v1, final Point3D v2, final Normal3D n0,
			final Normal3D n1, final Normal3D n2) {
		return null;
	}

	@Override
	public double getIntersectDistance(@NotNull final Ray ray) {
		return 0;
	}

	@NotNull
	@Override
	public Normal3D getNormal(@NotNull final Point3D surfacePoint) {
		return null;
	}

	@NotNull
	@Override
	public Point2D getMappedSurfacePoint(@NotNull final Point3D surfacePoint) {
		return null;
	}

	@Override
	protected TriangleIntersectionInformation calculateTriangleIntersection(@NotNull final Ray ray) {
		return null;
	}

}
