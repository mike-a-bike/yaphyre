/*
 * Copyright 2013 Michael Bieri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yaphyre.math;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 04.07.13
 */
public abstract class VectorMath {

	/**
	 * Calculate the reflected ray for the given incident ray and normal.
	 *
	 * @param incident The incoming incident ray
	 * @param normal   The surface normal
	 *
	 * @return The reflected, outgoing ray.
	 */
	public static Vector3D reflect(final Vector3D incident, final Normal3D normal) {
		checkNotNull(incident, "incident vetcor is null");
		checkNotNull(normal, "normal is null");

		return incident.sub(normal.scale(2 * incident.dot(normal)));
	}

	/**
	 * Calculate the refracted ray for the given incident ray, surface normal, n1 and n2 the refracting factors of the
	 * two materials. There are two possible scenarios: 1) The angle of the incident ray is smaller than the critical
	 * angle, so an outgoing ray is calculated. 2) The angle is bigger or equals tho the critical angle and total
	 * internal reflection occurs, in this case, null is returned since there is no refracted ray. Instead, a reflected
	 * ray has to be calculated.
	 *
	 * @param incident The incoming incident ray
	 * @param normal   The surface normal
	 * @param n1       The refractive index of the 1st material (where the ray is coming from)
	 * @param n2       The refractive index of the 2nd material (where the ray is going into)
	 *
	 * @return The refracted ray or null if the angle is so bigger than the critical angle and total internal reflection
	 *         occurs.
	 */
	public static Vector3D refract(final Vector3D incident, final Normal3D normal, final double n1, final double n2) {

		checkNotNull(incident, "incident vector is null");
		checkNotNull(normal, "normal is null");

		checkArgument(n1 != 0d);
		checkArgument(n2 != 0d);

		final double n = n1 / n2;

		if (Double.compare(n, 1d) == 0) {
			return incident;
		}

		final double cosPhi = -normal.dot(incident);
		final double sinT2 = normal.dot(normal) * (1d - cosPhi * cosPhi);

		if (sinT2 > 1d) return null; // Total Internal Reflection

		final double cosT = Math.sqrt(1d - sinT2);

		return incident.scale(n).add(normal.scale(n * cosPhi - cosT));
	}

	/**
	 * Calculate the amount of light which is reflected on a surface. If the angle is smaller than the critical angle,
	 * there is only a certain amount which is reflected. The rest is refracted. If the angle is bigger or equals to the
	 * critical angle, all light is reflected. The critical angle is determined by the refractive indices of both
	 * involved materials.
	 *
	 * @param incident The incoming incident ray
	 * @param normal   The surface normal
	 * @param n1       The refractive index of the 1st material (where the ray is coming from)
	 * @param n2       The refractive index of the 2nd material (where the ray is going into)
	 *
	 * @return The fraction of light which is reflected. 1 in all cases which are bigger than the critical angle.
	 */
	public static double reflectance(final Vector3D incident, final Normal3D normal, final double n1, final double n2) {

		checkNotNull(incident, "incident vector is null");
		checkNotNull(normal, "normal is null");

		checkArgument(n1 != 0d);
		checkArgument(n2 != 0d);

		final double n = n1 / n2;
		final double cosPhi = -normal.dot(incident);
		final double sinT2 = normal.dot(normal) * (1d - cosPhi * cosPhi);

		if (sinT2 > 1d) return 1d; // Total Internal Reflection

		final double cosT = Math.sqrt(1d - sinT2);
		final double r0rth = (n1 * cosPhi - n2 * cosT) / (n1 * cosPhi + n2 * cosT);
		final double rPar = (n2 * cosPhi - n1 * cosT) / (n2 * cosPhi + n1 * cosT);

		return (r0rth * r0rth + rPar * rPar) / 2d;
	}

}
