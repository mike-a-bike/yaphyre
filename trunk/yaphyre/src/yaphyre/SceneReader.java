package yaphyre;

import yaphyre.core.Lightsource;
import yaphyre.core.Sampler;
import yaphyre.core.Shader;
import yaphyre.core.Shape;
import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point3D;
import yaphyre.geometry.Transformation;
import yaphyre.lights.AreaLight;
import yaphyre.lights.Falloff;
import yaphyre.lights.Pointlight;
import yaphyre.raytracer.Scene;
import yaphyre.samplers.JitteredSampler;
import yaphyre.samplers.RegularSampler;
import yaphyre.shaders.CheckerShader;
import yaphyre.shaders.Material;
import yaphyre.shaders.MaterialBuilder;
import yaphyre.shaders.SimpleShader;
import yaphyre.shapes.Plane;
import yaphyre.shapes.Sphere;
import yaphyre.util.Color;

/**
 * Read a very simple file format in order to make the development and testing
 * simpler.<br/>
 * TODO: IMPLEMENT THIS...
 *
 * @author Michael Bieri
 *
 */
public class SceneReader {

  public static final Scene createSceneWithSpheres() {

    double ambientLight = 0.1;

    Point3D sphere1Center = new Point3D(0, 1.5, 0);
    double sphere1Radius = 1d;

    Point3D sphere2Center = new Point3D(2.5, 1.5, 1.5);
    double sphere2Radius = 0.5;

    Point3D pointlight1Position = new Point3D(-1, 5, 0);
    double pointlight1Intensity = 10d;
    Color pointlight1Color = new Color(java.awt.Color.RED);
    Falloff pointlight1falloff = Falloff.Quadric;

    Point3D pointlight2Position = new Point3D(1, 5, 2);
    double pointlight2Intensity = 10d;
    Color pointlight2Color = new Color(java.awt.Color.GREEN);
    Falloff pointlight2falloff = Falloff.Quadric;

    Point3D pointlight3Position = new Point3D(1, 5, -2);
    double pointlight3Intensity = 10d;
    Color pointlight3Color = new Color(java.awt.Color.BLUE);
    Falloff pointlight3falloff = Falloff.Quadric;

    Material diffuseMaterial = MaterialBuilder.start().ambient(ambientLight).diffuse(0.8).build();
    Material mirrorMaterial = MaterialBuilder.start().ambient(ambientLight / 2d).diffuse(0.1d).reflection(0.9).build();

    // SimpleShader redShader = new SimpleShader(diffuseMaterial, new Color(1d,
    // 0d, 0d));
    // SimpleShader greenShader = new SimpleShader(diffuseMaterial, new
    // Color(0d, 1d, 0d));
    // SimpleShader blueShader = new SimpleShader(diffuseMaterial, new Color(0d,
    // 0d, 1d));
    SimpleShader whiteShader = new SimpleShader(diffuseMaterial, new Color(1d, 1d, 1d));
    SimpleShader whiteMirror = new SimpleShader(mirrorMaterial, new Color(1d, 1d, 1d));

    Scene simpleScene = new Scene();

    simpleScene.addShape(Sphere.createSphere(sphere1Center, sphere1Radius, whiteShader, true));
    simpleScene.addShape(Sphere.createSphere(sphere2Center, sphere2Radius, whiteMirror, true));
    simpleScene.addShape(new Plane(Transformation.IDENTITY, whiteMirror, true));

    simpleScene.addLightsource(new Pointlight(pointlight1Position, pointlight1Color, pointlight1Intensity, pointlight1falloff));
    simpleScene.addLightsource(new Pointlight(pointlight2Position, pointlight2Color, pointlight2Intensity, pointlight2falloff));
    simpleScene.addLightsource(new Pointlight(pointlight3Position, pointlight3Color, pointlight3Intensity, pointlight3falloff));

    return simpleScene;
  }

  public static final Scene createSimpleScene() {

    double ambientLight = 0.075d;

    Material diffuseMaterial = MaterialBuilder.start().ambient(ambientLight).diffuse(0.8).build();
    Material mirrorMaterial = MaterialBuilder.start().ambient(ambientLight / 2d).diffuse(0.1d).reflection(0.9).build();

    Shader whiteDiffuse = new SimpleShader(diffuseMaterial, 1d, 1d, 1d);
    Shader redDiffuse = new SimpleShader(diffuseMaterial, 1d, 0d, 0d);
    Shader greenDiffuse = new SimpleShader(diffuseMaterial, 0d, 1d, 0d);
    Shader blueDiffuse = new SimpleShader(diffuseMaterial, 0d, 0d, 1d);
    Shader whiteMirror = new SimpleShader(mirrorMaterial, 1d, 1d, 1d);
    // Shader redMirror = new SimpleShader(mirrorMaterial, 1d, 0d, 0d);

    Shader sphereCheckerShader = new CheckerShader(Transformation.IDENTITY, whiteMirror, blueDiffuse, 4d, 4d);
    Shader checkBoardShader = new CheckerShader(Transformation.IDENTITY, redDiffuse, whiteDiffuse, 16d, 16d);
    Shader planeCeckerShader = new CheckerShader(Transformation.IDENTITY, whiteDiffuse, greenDiffuse, 0.5d, 0.5d);

    Lightsource pointLight = new Pointlight(new Point3D(-2, 5, -2), new Color(1, 1, 1), 15, Falloff.Quadric);

    Transformation sphereTransformation = Transformation.translate(0, 1.5, 0).mul(Transformation.rotateY(30).mul(Transformation.rotateX(60)));
    Transformation distantTransformation = Transformation.translate(-2, 10, -5).mul(Transformation.scale(2, 2, 2).mul(Transformation.rotateX(90)));
    Transformation planeTransformation = Transformation.rotateX(-10).mul(Transformation.translate(0, -1, 0).mul(Transformation.rotateY(30)));

    Shape plane = new Plane(planeTransformation, planeCeckerShader, true);
    Shape sphere = new Sphere(sphereTransformation, sphereCheckerShader, true);
    Shape distantSphere = new Sphere(distantTransformation, checkBoardShader, true);

    Scene scene = new Scene();

    scene.addLightsource(pointLight);

    scene.addShape(plane);
    scene.addShape(sphere);
    scene.addShape(distantSphere);

    return scene;
  }

  public static final Scene createDOFScene() {

    final double ambientLight = 0.075d;

    final Material diffuseMaterial = MaterialBuilder.start().ambient(ambientLight).diffuse(0.8).build();
    // final Material mirrorMaterial =
    // MaterialBuilder.start().ambient(ambientLight /
    // 2d).diffuse(0.1d).reflection(0.9).build();

    final Shader whiteDiffuse = new SimpleShader(diffuseMaterial, 1d, 1d, 1d);
    final Shader redDiffuse = new SimpleShader(diffuseMaterial, 1d, 0d, 0d);
    final Shader greenDiffuse = new SimpleShader(diffuseMaterial, 0d, 1d, 0d);
    final Shader blueDiffuse = new SimpleShader(diffuseMaterial, 0d, 0d, 1d);
    // final Shader whiteMirror = new SimpleShader(mirrorMaterial, 1d, 1d, 1d);

    final Shape floor = new Plane(Transformation.IDENTITY, whiteDiffuse, false);
    final Shape redBall = new Sphere(Transformation.translate(-2, 1.5, -2), redDiffuse, true);
    final Shape blueBall = new Sphere(Transformation.translate(0, 1.5, 0), blueDiffuse, true);
    final Shape greenBall = new Sphere(Transformation.translate(2, 1.5, 2), greenDiffuse, true);

    final Sampler lightSampler = new RegularSampler(4);

    final Point3D lightPosition = new Point3D(2.5, 5, -5);
    final Normal3D lightDirection = Point3D.ORIGIN.sub(lightPosition).asNormal();
    final Lightsource areaLight = new AreaLight(lightPosition, new Color(1, 1, 1), Falloff.Quadric, 30, lightDirection, 1, lightSampler, yaphyre.lights.AreaLight.Shape.Disc);

    final Scene scene = new Scene();

    scene.addShape(floor);
    scene.addShape(redBall);
    scene.addShape(blueBall);
    scene.addShape(greenBall);

    scene.addLightsource(areaLight);

    return scene;

  }

  /**
   * 'Historic' scene: The first scene ever rendered with <em>yaphyre</em>.
   *
   * @return A very simple {@link Scene} containing one light, one plane and one
   *         sphere.
   */
  public static final Scene createFirstLight() {
    double ambientLight = 0.075d;

    Material diffuseMaterial = MaterialBuilder.start().ambient(ambientLight).diffuse(0.8d).build();

    Shader diffuseWhite = new SimpleShader(diffuseMaterial, 1, 1, 1);

    Lightsource light = new Pointlight(new Point3D(-2, 5, -2), new Color(1, 1, 1), 10, Falloff.Quadric);

    Shape plane = new Plane(Transformation.IDENTITY, diffuseWhite, true);
    Shape sphere = Sphere.createSphere(new Point3D(0, 1, 0), 1, diffuseWhite, true);

    Scene scene = new Scene();

    scene.addLightsource(light);
    scene.addShape(plane);
    scene.addShape(sphere);

    return scene;
  }
}
