package yaphyre.shapes;

import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point2D;
import yaphyre.geometry.Point3D;

/**
 *
 */
public class SmoothMeshTriangle extends MeshTriangle {

	private final Normal3D n0, n1, n2;

	public SmoothMeshTriangle(final Point3D v0, final Point3D v1, final Point3D v2,
	                          final Normal3D n0, final Normal3D n1, final Normal3D n2,
	                          final Point2D uv0, final Point2D uv1, final Point2D uv2) {
		super(v0, v1, v2, uv0, uv1, uv2);
		this.n0 = n0;
		this.n1 = n1;
		this.n2 = n2;
	}

	public static SmoothMeshTriangle create(final Point3D v0, final Point3D v1, final Point3D v2,
	                                        final Normal3D n0, final Normal3D n1, final Normal3D n2) {
		return new SmoothMeshTriangle(v0, v1, v2, n0, n1, n2, new Point2D(0, 0), new Point2D(1, 0), new Point2D(0, 1));
	}

	public static SmoothMeshTriangle create(final Point3D v0, final Point3D v1, final Point3D v2,
	                                        final Normal3D n0, final Normal3D n1, final Normal3D n2,
	                                        final Point2D uv0, final Point2D uv1, final Point2D uv2) {
		return new SmoothMeshTriangle(v0, v1, v2, n0, n1, n2, uv0, uv1, uv2);
	}

	@Override
	protected Normal3D calculateNormal(final double alpha, final double beta, final double gamma) {
		return n0.scale(alpha).add(n1.scale(beta)).add(n2.scale(gamma));
	}

}
