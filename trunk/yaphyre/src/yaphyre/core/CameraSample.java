package yaphyre.core;

import yaphyre.geometry.Point2D;

public class CameraSample {

  private Point2D rasterPoint;

  private Point2D lensCoordinates;

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
