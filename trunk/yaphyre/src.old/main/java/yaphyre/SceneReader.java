/*
 * Copyright 2013 Michael Bieri
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

package yaphyre;

import yaphyre.cameras.AbstractCamera.BaseCameraSettings;
import yaphyre.cameras.PerspectiveCamera;
import yaphyre.cameras.PerspectiveCamera.PerspectiveCameraSettings;
import yaphyre.core.Camera;
import yaphyre.core.Lightsource;
import yaphyre.core.Shader;
import yaphyre.core.Shape;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Transformation;
import yaphyre.lights.Pointlight;
import yaphyre.raytracer.Scene;
import yaphyre.shaders.CheckerShader;
import yaphyre.shaders.GradientShader;
import yaphyre.shaders.Material;
import yaphyre.shaders.MaterialBuilder;
import yaphyre.shaders.SimpleShader;
import yaphyre.shapes.Plane;
import yaphyre.shapes.Sphere;
import yaphyre.util.Color;

import static yaphyre.geometry.Transformation.IDENTITY;
import static yaphyre.geometry.Transformation.rotateX;
import static yaphyre.geometry.Transformation.rotateY;
import static yaphyre.geometry.Transformation.scale;
import static yaphyre.geometry.Transformation.translate;
import static yaphyre.shapes.Sphere.createSphere;

/**
 * Read a very simple file format in order to make the development and testing simpler.<br/>
 * TODO: IMPLEMENT THIS...
 *
 * @author Michael Bieri
 */
public class SceneReader {

	private SceneReader() {
	}

	public static Scene createSceneWithSpheres() {

		double ambientLight = 0.1;

		Point3D sphere1Center = new Point3D(0, 1.5, 0);
		double sphere1Radius = 1d;

		Point3D sphere2Center = new Point3D(2.5, 1.5, 1.5);

		Transformation pointlight1Transformation = translate(-1, 5, 0);
		Color pointlight1Color = new Color(java.awt.Color.RED);

		Transformation pointlight2Transformation = translate(1, 5, 2);
		Color pointlight2Color = new Color(java.awt.Color.GREEN);

		Transformation pointlight3Transformation = translate(1, 5, -2);
		Color pointlight3Color = new Color(java.awt.Color.BLUE);

		Material diffuseMaterial = MaterialBuilder.start().ambient(ambientLight).diffuse(0.8).build();
		Material mirrorMaterial = MaterialBuilder.start()
				.ambient(ambientLight / 2d)
				.diffuse(0.1d)
				.reflection(0.9)
				.build();

		// SimpleShader redShader = new SimpleShader(diffuseMaterial, new Color(1d,
		// 0d, 0d));
		// SimpleShader greenShader = new SimpleShader(diffuseMaterial, new
		// Color(0d, 1d, 0d));
		// SimpleShader blueShader = new SimpleShader(diffuseMaterial, new Color(0d,
		// 0d, 1d));
		SimpleShader whiteShader = new SimpleShader(diffuseMaterial, new Color(1d, 1d, 1d));
		SimpleShader whiteMirror = new SimpleShader(mirrorMaterial, new Color(1d, 1d, 1d));

		Scene simpleScene = new Scene();

		simpleScene.addShape(createSphere(sphere1Center, sphere1Radius, whiteShader));
		double sphere2Radius = 0.5;
		simpleScene.addShape(createSphere(sphere2Center, sphere2Radius, whiteMirror));
		simpleScene.addShape(new Plane(IDENTITY, whiteMirror));

		double pointlight1Intensity = 10d;
		simpleScene.addLightsource(new Pointlight(pointlight1Transformation, pointlight1Color, pointlight1Intensity));
		double pointlight2Intensity = 10d;
		simpleScene.addLightsource(new Pointlight(pointlight2Transformation, pointlight2Color, pointlight2Intensity));
		double pointlight3Intensity = 10d;
		simpleScene.addLightsource(new Pointlight(pointlight3Transformation, pointlight3Color, pointlight3Intensity));

		simpleScene.addCamera(createDefaultCamera(Double.MAX_VALUE, 0d));

		return simpleScene;
	}

	public static Scene createSimpleScene() {

		double ambientLight = 0.075d;

		Material diffuseMaterial = MaterialBuilder.start().ambient(ambientLight).diffuse(0.8).build();
		Material mirrorMaterial = MaterialBuilder.start()
				.ambient(ambientLight / 2d)
				.diffuse(0.1d)
				.reflection(0.9)
				.build();

		Shader whiteDiffuse = new SimpleShader(diffuseMaterial, 1d, 1d, 1d);
		Shader redDiffuse = new SimpleShader(diffuseMaterial, 1d, 0d, 0d);
		Shader greenDiffuse = new SimpleShader(diffuseMaterial, 0d, 1d, 0d);
		Shader blueDiffuse = new SimpleShader(diffuseMaterial, 0d, 0d, 1d);
		Shader whiteMirror = new SimpleShader(mirrorMaterial, 1d, 1d, 1d);
		// Shader redMirror = new SimpleShader(mirrorMaterial, 1d, 0d, 0d);

		Shader sphereCheckerShader = new CheckerShader(IDENTITY, whiteMirror, blueDiffuse, 4d, 4d);
		Shader checkBoardShader = new CheckerShader(IDENTITY, redDiffuse, whiteDiffuse, 16d, 16d);
		Shader planeCeckerShader = new CheckerShader(IDENTITY, whiteDiffuse, greenDiffuse, 0.5d, 0.5d);

		Lightsource pointLight = new Pointlight(translate(-2, 5, -2), new Color(1, 1, 1), 15);

		Transformation sphereTransformation = translate(0, 1.5, 0).mul(rotateY(30).mul(rotateX(60)));
		Transformation distantTransformation = translate(-2, 10, -5).mul(scale(2, 2, 2).mul(rotateX(90)));
		Transformation planeTransformation = rotateX(-10).mul(translate(0, -1, 0).mul(rotateY(30)));

		Shape plane = new Plane(planeTransformation, planeCeckerShader);
		Shape sphere = new Sphere(sphereTransformation, 0d, 360d, 0d, 180d, sphereCheckerShader);
		Shape distantSphere = new Sphere(distantTransformation, 0d, 360d, 0d, 180d, checkBoardShader);

		Scene scene = new Scene();

		scene.addLightsource(pointLight);

		scene.addShape(plane);
		scene.addShape(sphere);
		scene.addShape(distantSphere);

		scene.addCamera(createDefaultCamera(Double.MAX_VALUE, 0d));

		return scene;
	}

	public static Scene createDOFScene() {

		final double ambientLight = 0.25d;

		final Material diffuseMaterial = MaterialBuilder.start().ambient(ambientLight).diffuse(0.8).build();

		final Shader whiteDiffuse = new SimpleShader(diffuseMaterial, 1d, 1d, 1d);
		final Shader redDiffuse = new SimpleShader(diffuseMaterial, 1d, 0d, 0d);
		final Shader greenDiffuse = new SimpleShader(diffuseMaterial, 0d, 1d, 0d);
		final Shader blueDiffuse = new SimpleShader(diffuseMaterial, 0d, 0d, 1d);

		final Shape floor = new Plane(IDENTITY, whiteDiffuse);
		final Shape redBall = new Sphere(translate(-2, 1.5, -2), 0d, 360d, 0d, 180d, redDiffuse);
		final Shape blueBall = new Sphere(translate(0, 1.5, 0), 0d, 360d, 0d, 180d, blueDiffuse);
		final Shape greenBall = new Sphere(translate(2, 1.5, 2), 0d, 360d, 0d, 180d, greenDiffuse);

		final Lightsource pointLight = new Pointlight(translate(2.5, 5, 5), new Color(1, 1, 1), 30);

		final Scene scene = new Scene();

		scene.addShape(floor);
		scene.addShape(redBall);
		scene.addShape(blueBall);
		scene.addShape(greenBall);

		scene.addLightsource(pointLight);

		// create a camera with an aperture size other than zero
		scene.addCamera(createDefaultCamera(100d, 0.0002d));

		return scene;
	}

	/**
	 * 'Historic' scene: The first scene ever rendered with <em>yaphyre</em>.
	 *
	 * @return A very simple {@link Scene} containing one light, one plane and one sphere.
	 */
	public static Scene createFirstLight() {
		double ambientLight = 0.05d;

		Material diffuseMaterial = MaterialBuilder.start().ambient(ambientLight).diffuse(0.8d).build();
		Material mirrorMaterial = MaterialBuilder.start().ambient(ambientLight).diffuse(0.1d).reflection(0.9d).build();

		Shader diffuseGradient = new GradientShader(diffuseMaterial, new Color(1d, 0d, 0d), new Color(0d, 0d, 1d), GradientShader.BlendDirection.uAxis);
		Shader diffuseWhite = new SimpleShader(diffuseMaterial, 1, 1, 1);
		Shader mirrorShader = new SimpleShader(mirrorMaterial, 0, 1, 0);
		Shader checker = new CheckerShader(IDENTITY, diffuseGradient, mirrorShader, 8d);

		Lightsource light = new Pointlight(translate(-2, 5, 2), new Color(1, 1, 1), 20);

		Shape plane = new Plane(IDENTITY, diffuseWhite);
		Shape sphere = new Sphere(translate(0d, 1d, 0d), 225d, 315d, 45d, 135d, checker);

		Scene scene = new Scene();

		scene.addLightsource(light);
		scene.addShape(plane);
		scene.addShape(sphere);

		scene.addCamera(createDefaultCamera(Double.MAX_VALUE, 0d));

		return scene;
	}

	private static Camera createDefaultCamera(final double focalDistance, final double lensRadius) {
		Point3D cameraPosition = new Point3D(0, 25, 100);
		Point3D lookAt = new Point3D(0, 1, 0);
		double aspectRatio = 4d / 3d;
		double focalLength = 25d;
		return createCamera(cameraPosition, lookAt, aspectRatio, focalLength, focalDistance, lensRadius);
	}

	private static Camera createCamera(Point3D position, Point3D lookAt, double aspectRatio, double focalLength,
	                                   double focalDistance, double lensRadius) {
		BaseCameraSettings baseSettings = BaseCameraSettings.create(position, lookAt);
		PerspectiveCameraSettings perspectiveSettings = PerspectiveCameraSettings.create(aspectRatio, focalLength,
				focalDistance, lensRadius);
		return new PerspectiveCamera(baseSettings, perspectiveSettings, null);
	}

}
