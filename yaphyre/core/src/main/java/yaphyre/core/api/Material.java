/*
 * Copyright 2014 Michael Bieri
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

package yaphyre.core.api;

public class Material {

    private final double ambient;
    private final double diffuse;
    private final double specular;
    private final double reflection;
    private final double refraction;

    public Material(double ambient, double diffuse, double specular, double reflection, double refraction) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.reflection = reflection;
        this.refraction = refraction;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(ambient);
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(diffuse);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(reflection);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(refraction);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(specular);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Material other = (Material) obj;
        if (Double.doubleToLongBits(ambient) != Double.doubleToLongBits(other.ambient)) {
            return false;
        }
        if (Double.doubleToLongBits(diffuse) != Double.doubleToLongBits(other.diffuse)) {
            return false;
        }
        if (Double.doubleToLongBits(reflection) != Double.doubleToLongBits(other.reflection)) {
            return false;
        }
        if (Double.doubleToLongBits(refraction) != Double.doubleToLongBits(other.refraction)) {
            return false;
        }
        return Double.doubleToLongBits(specular) == Double.doubleToLongBits(other.specular);
    }

    public double getAmbient() {
        return ambient;
    }

    public double getDiffuse() {
        return diffuse;
    }

    public double getSpecular() {
        return specular;
    }

    public double getReflection() {
        return reflection;
    }

    public double getRefraction() {
        return refraction;
    }

}
