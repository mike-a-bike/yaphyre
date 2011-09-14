package yaphyre.math;

import static java.lang.Math.PI;
import static java.lang.Math.acos;
import static java.lang.Math.cbrt;
import static java.lang.Math.cos;
import static java.lang.Math.sqrt;
import static yaphyre.math.MathUtils.div;

import java.util.HashSet;
import java.util.Set;

public enum Solver {

  Linear {
    /**
     * Solve a linear equation for:<br/>
     * c<sub>1</sub>*x + c<sub>0</sub> = 0
     */
    @Override
    public double[] solve(double... c) throws IllegalArgumentException {
      assertOrder(1, c);
      if (c[1] == 0) {
        return EMPTY_RESULT;
      }
      return new double[] { div(-c[0], c[1]) };
    }
  },
  Quadratic {
    /**
     * Solve a quadratic equation for:<br/>
     * c<sub>2</sub>*x<sup>2</sup> + c<sub>1</sub>*x + c<sub>0</sub> = 0
     */
    @Override
    public double[] solve(double... c) throws IllegalArgumentException {
      assertOrder(2, c);

      if (c[2] == 0) {
        return Solver.Linear.solve(c[0], c[1]);
      }

      double det = c[1] * c[1] - 4 * c[2] * c[0];
      if (det == 0) {
        return new double[] { div(-c[1], 2 * c[2]) };
      }

      if (det > 0) {
        double sqrtDet = sqrt(det);
        double[] result = new double[2];
        result[0] = div(-c[1] + sqrtDet, 2 * c[2]);
        result[1] = div(-c[1] - sqrtDet, 2 * c[2]);
        return result;
      } else {
        return EMPTY_RESULT;
      }
    }
  },
  Cubic {
    /**
     * Solve a cubic equation for:<br/>
     * c<sub>3</sub>*x<sup>3</sup> + c<sub>2</sub>*x<sup>2</sup> +
     * c<sub>1</sub>*x + c<sub>0</sub> = 0 <br/>
     * http://en.wikipedia.org/wiki/Cubic_function under Trigonometric (and
     * hyperbolic) method
     */
    @Override
    public double[] solve(double... c) throws IllegalArgumentException {
      assertOrder(3, c);
      if (c[3] == 0) {
        return Solver.Quadratic.solve(c[0], c[1], c[2]);
      }

      if (c[2] == 0 && c[1] == 0) {
        return new double[] { cbrt(div(-c[0], c[3])) };
      }

      if (c[0] == 0) {
        double[] quadResults = Solver.Quadratic.solve(c[1], c[2], c[3]);
        Set<Double> results = new HashSet<Double>();
        results.add(0d);
        for (double value : quadResults) {
          results.add(value);
        }
        double[] result = new double[results.size()];
        int i = 0;
        for (Double value : results) {
          result[i++] = value;
        }
        return result;
      }

      double k, p, q;

      if (c[2] != 0) {
        k = div(-c[2], 3 * c[3]);
        p = div(3 * c[3] * c[1] - c[2] * c[2], -3 * c[3] * c[3]);
        q = div(2 * c[2] * c[2] * c[2] - 9 * c[3] * c[2] * c[1] + 27 * c[3] * c[3] * c[0], 27 * c[3] * c[3] * c[3]);
      } else {
        k = 0;
        p = div(-c[1], c[3]);
        q = div(-c[0], c[3]);
      }

      double p_d3_e3 = p * p / 27 * p;
      double w = q / 4 * q - p_d3_e3;

      if (w < 0) {
        double cos3a = div(q, 2 * sqrt(p_d3_e3));
        double a = acos(cos3a) / 3;
        double t = sqrt(p * (4 / 3));
        double[] results = new double[3];
        results[0] = t * cos(a) + k;
        results[1] = t * cos(a + 2 * PI / 3) + k;
        results[2] = t * cos(a + 2 * (2 * PI / 3)) + k;
        return results;
      }

      if (w == 0) {
        return new double[] { 2 * cbrt(q / 2) + k };
      }

      return new double[] { cbrt(q / 2 + sqrt(w)) + cbrt(q / 2 - sqrt(w)) + k };

    }
  },
  Quartic {
    @Override
    public double[] solve(double... c) throws IllegalArgumentException {
      throw new RuntimeException("Not implemented yet!");
    }
  };

  protected static final double[] EMPTY_RESULT = new double[0];

  /**
   * Solve an equation for the given equation type.
   * 
   * @param c
   *          A list of coefficients.
   * 
   * @return A list of solutions. This list may be empty if there are no real
   *         solutions.
   * 
   * @throws IllegalArgumentException
   *           If the number of the coefficients in <code>c</code> does not
   *           match the necessary number of values, an
   *           {@link IllegalArgumentException} is thrown.
   */
  public abstract double[] solve(double... c) throws IllegalArgumentException;

  protected void assertOrder(int order, double... c) throws IllegalArgumentException {
    if (c.length != order + 1) {
      throw new IllegalArgumentException("Number of coefficients do not match the order of the equation to solve");
    }
  }

}
