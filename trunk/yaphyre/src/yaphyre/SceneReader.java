package yaphyre;

import yaphyre.geometry.Vector;
import yaphyre.lights.Falloff;
import yaphyre.lights.Lightsources;
import yaphyre.lights.Pointlight;
import yaphyre.raytracer.Scene;
import yaphyre.shaders.Material;
import yaphyre.shaders.MaterialBuilder;
import yaphyre.shaders.Shaders;
import yaphyre.shaders.SimpleShader;
import yaphyre.shapes.Plane;
import yaphyre.shapes.Shapes;
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

    Vector sphere1Center = new Vector(0, 1.5, 0);
    double sphere1Radius = 1d;

    Vector sphere2Center = new Vector(2.5, 1.5, 1.5);
    double sphere2Radius = 0.5;

    Vector planeOrigin = Vector.ORIGIN;
    Vector planeNormal = Vector.NORMAL_Y;

    Vector pointlight1Position = new Vector(-1, 5, 0);
    double pointlight1Intensity = 10d;
    Color pointlight1Color = new Color(java.awt.Color.RED);
    Falloff pointlight1falloff = Falloff.Quadric;

    Vector pointlight2Position = new Vector(1, 5, 2);
    double pointlight2Intensity = 10d;
    Color pointlight2Color = new Color(java.awt.Color.GREEN);
    Falloff pointlight2falloff = Falloff.Quadric;

    Vector pointlight3Position = new Vector(1, 5, -2);
    double pointlight3Intensity = 10d;
    Color pointlight3Color = new Color(java.awt.Color.BLUE);
    Falloff pointlight3falloff = Falloff.Quadric;

    Material diffuseMaterial = MaterialBuilder.init().ambient(ambientLight).diffuse(0.8).build();
    // Material phongMaterial =
    // MaterialBuilder.init().ambient(ambientLight).diffuse(0.5).specular(0.2).build();
    Material mirrorMaterial = MaterialBuilder.init().ambient(ambientLight).diffuse(0.2).specular(0.2).reflection(0.6).build();

    SimpleShader redShader = new SimpleShader("shader-red", diffuseMaterial, new Color(1d, 0d, 0d));
    SimpleShader greenShader = new SimpleShader("shader-green", diffuseMaterial, new Color(0d, 1d, 0d));
    SimpleShader blueShader = new SimpleShader("shader-blue", diffuseMaterial, new Color(0d, 0d, 1d));
    SimpleShader whiteShader = new SimpleShader("shader-white", diffuseMaterial, new Color(1d, 1d, 1d));
    SimpleShader whiteMirror = new SimpleShader("shader-white-mirror", mirrorMaterial, new Color(1d, 1d, 1d));

    Scene simpleScene = new Scene();

    simpleScene.addShape(new Sphere("sphere1", sphere1Center, sphere1Radius, whiteShader, true));
    simpleScene.addShape(new Sphere("shpere2", sphere2Center, sphere2Radius, whiteMirror, true));
    simpleScene.addShape(new Plane("plane1", planeOrigin, planeNormal, whiteMirror, true));

    simpleScene.addLightsource(new Pointlight("pointlight1", pointlight1Position, pointlight1Color, pointlight1Intensity, pointlight1falloff));
    simpleScene.addShape(new Sphere("light1-sphere", pointlight1Position, 0.05, redShader, false));
    simpleScene.addLightsource(new Pointlight("pointlight2", pointlight2Position, pointlight2Color, pointlight2Intensity, pointlight2falloff));
    simpleScene.addShape(new Sphere("light2-sphere", pointlight2Position, 0.05, greenShader, false));
    simpleScene.addLightsource(new Pointlight("pointlight3", pointlight3Position, pointlight3Color, pointlight3Intensity, pointlight3falloff));
    simpleScene.addShape(new Sphere("light2-sphere", pointlight3Position, 0.05, blueShader, false));

    return simpleScene;
  }

  public static final Scene createSimpleScene() {

    double ambientLight = 0.075d;

    Material diffuseMaterial = MaterialBuilder.init().ambient(ambientLight).diffuse(0.8).build();
    Material mirrorMaterial = MaterialBuilder.init().ambient(ambientLight).diffuse(0.1).reflection(0.9).build();

    Shaders whiteDiffuse = new SimpleShader("white-diffuse", diffuseMaterial, 1d, 1d, 1d);
    Shaders redDiffuse = new SimpleShader("red-diffuse", diffuseMaterial, 1d, 0d, 0d);
    Shaders whiteMirror = new SimpleShader("white-mirror", mirrorMaterial, 1d, 1d, 1d);

    Lightsources pointLight = new Pointlight("light", new Vector(-2, 5, -2), new Color(1, 1, 1), 15, Falloff.Quadric);
    // Shapes lightSphere = new Sphere("light-sphere", pointLight.getPosition(),
    // 0.05, whiteDiffuse, false);

    Shapes plane = new Plane("plane", Vector.ORIGIN, Vector.NORMAL_Y, whiteDiffuse, true);
    Shapes sphere = new Sphere("sphere", new Vector(0, 1.5, 0), 1, whiteMirror, true);
    Shapes distantSphere = new Sphere("distant-sphere", new Vector(-2, 10, -5), 2, redDiffuse, true);

    Scene scene = new Scene();

    scene.addLightsource(pointLight);
    // scene.addShape(lightSphere);

    scene.addShape(plane);
    scene.addShape(sphere);
    scene.addShape(distantSphere);

    return scene;
  }
}
