package yaphyre.lights;

import java.io.Serializable;

/**
 * Representation and implementation of different falloff algorithms. With the
 * exception of {@link Falloff#None}, the result becomes smaller the bigger the
 * distance becomes.
 * <ul>
 * <li>None: The value does not vary in relation to the distance: v = i</li>
 * <li>Linear: The value decreases with a linear function: v = i / d</li>
 * <li>Quadric: The value decreases with a quadric function: v = i /
 * d<sup>2</sup></li>
 * <li>Cubic: The value decreases with a cubic function: v = i / d<sup>3</sup></li>
 * </ul>
 *
 * @author Michael Bieri
 *
 */
public enum Falloff implements Serializable {
  None {
    @Override
    public double getIntensity(double intensity, double distance) {
      return intensity;
    }
  },
  Linear {
    @Override
    public double getIntensity(double intensity, double distance) {
      if (distance == 0d) {
        return intensity;
      }
      return intensity / distance;
    }
  },
  Quadric {
    @Override
    public double getIntensity(double intensity, double distance) {
      if (distance == 0d) {
        return intensity;
      }
      return intensity / (distance * distance);
    }
  },
  Cubic {
    @Override
    public double getIntensity(double intensity, double distance) {
      if (distance == 0d) {
        return intensity;
      }
      return intensity / (distance * distance * distance);
    }
  };

  public abstract double getIntensity(double intensity, double distance);

  public static Falloff parse(String falloffString) throws IllegalArgumentException {
    for (Falloff falloff : Falloff.values()) {
      if (falloff.toString().equalsIgnoreCase(falloffString)) {
        return falloff;
      }
    }
    throw new IllegalArgumentException("Unknown falloff name: " + falloffString);
  }
}