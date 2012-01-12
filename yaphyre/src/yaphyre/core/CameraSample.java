package yaphyre.core;

import com.google.common.base.Objects;

import yaphyre.geometry.Point2D;

public class CameraSample {

  private Point2D rasterPoint;

  private Point2D lensCoordinates;

  @Override
  public String toString() {
    return Objects.toStringHelper(getClass()).add("rasterPoint", rasterPoint).add("lensCoordinates", lensCoordinates).toString();
  }

  public Point2D getRasterPoint() {
    return this.rasterPoint;
  }

  public void setRasterPoint(Point2D rasterPoint) {
    this.rasterPoint = rasterPoint;
  }

  public Point2D getLensCoordinates() {
    return this.lensCoordinates;
  }

  public void setLensCoordinates(Point2D lensCoordinates) {
    this.lensCoordinates = lensCoordinates;
  }


}
