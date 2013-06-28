/*
 * Copyright 2012 Michael Bieri
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package yaphyre.scenereaders.yaphyre.utils;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import org.joox.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Transformation;
import yaphyre.geometry.Vector3D;

import java.util.Deque;

class TransformationEntityHelper implements EntityHelper<Transformation> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransformationEntityHelper.class);

	public static final TransformationEntityHelper INSTANCE = new TransformationEntityHelper();

	@Override
	public Transformation decodeEntity(Match entityMatch) {
		Deque<Transformation> transformationStack = Lists.newLinkedList();
		for (Match transformationMatch : entityMatch.children().each()) {
			transformationStack.push(decodeSingleTransformation(transformationMatch));
		}
		Transformation result = Transformation.IDENTITY;
		while (!transformationStack.isEmpty()) {
			result = result.mul(transformationStack.pop());
		}
		return result;
	}

	private Transformation decodeSingleTransformation(Match transformationMatch) {
		try {
			String tag = transformationMatch.tag();
			if (tag.equals("translate")) {
				return decodeTranslation(transformationMatch);
			} else if (tag.equals("scale")) {
				return decodeScaling(transformationMatch);
			} else if (tag.equals("rotateX")) {
				return decodeRotationX(transformationMatch);
			} else if (tag.equals("rotateY")) {
				return decodeRotationY(transformationMatch);
			} else if (tag.equals("rotateZ")) {
				return decodeRotationZ(transformationMatch);
			} else if (tag.equals("rotateAxis")) {
				return decodeRotationAxis(transformationMatch);
			} else if (tag.equals("lookAt")) {
				return decodeLookAt(transformationMatch);
			}
			LOGGER.warn("Unknown transformation found: {}", tag);
		} catch (Throwable throwable) {
			LOGGER.error("Error during transformation decoding: {}", throwable.getMessage(), throwable);
			Throwables.propagate(throwable);
		}
		return null;
	}

	private Transformation decodeTranslation(Match translationMatch) {
		double dx = translationMatch.attr("dx", Double.class);
		double dy = translationMatch.attr("dy", Double.class);
		double dz = translationMatch.attr("dz", Double.class);
		return Transformation.translate(dx, dy, dz);
	}

	private Transformation decodeScaling(Match scalingMatch) {
		double sx = scalingMatch.attr("sx", Double.class);
		double sy = scalingMatch.attr("sy", Double.class);
		double sz = scalingMatch.attr("sz", Double.class);
		return Transformation.scale(sx, sy, sz);
	}

	private Transformation decodeRotationX(Match rotationMatch) {
		return Transformation.rotateX(rotationMatch.attr("rx", Double.class));
	}

	private Transformation decodeRotationY(Match rotationMatch) {
		return Transformation.rotateY(rotationMatch.attr("ry", Double.class));
	}

	private Transformation decodeRotationZ(Match rotationMatch) {
		return Transformation.rotateZ(rotationMatch.attr("rz", Double.class));
	}

	private Transformation decodeRotationAxis(Match rotationMatch) {
		Vector3D axis = Vector3DEntityHelper.INSTANCE.decodeEntity(rotationMatch.child("axis"));
		double angle = rotationMatch.attr("angle", Double.class);
		return Transformation.rotate(angle, axis);
	}

	private Transformation decodeLookAt(Match transformationMatch) {
		Point3D eye = Point3DEntityHelper.INSTANCE.decodeEntity(transformationMatch.child("eye"));
		Point3D lookAt = Point3DEntityHelper.INSTANCE.decodeEntity(transformationMatch.child("lookAt"));
		Vector3D up = decodeUp(transformationMatch);
		return Transformation.lookAt(eye, lookAt, up);
	}

	private Vector3D decodeUp(Match transformationMatch) {
		Match upMatch = transformationMatch.child("up");
		Vector3D up = Vector3D.Y;
		if (!upMatch.isEmpty()) {
			up = Vector3DEntityHelper.INSTANCE.decodeEntity(upMatch);
		}
		return up;
	}
}
