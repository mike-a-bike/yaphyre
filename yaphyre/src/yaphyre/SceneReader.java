package yaphyre;

import yaphyre.geometry.Normal3D;
import yaphyre.geometry.Point3D;
import yaphyre.lights.Falloff;
import yaphyre.lights.Lightsources;
import yaphyre.lights.Pointlight;
import yaphyre.raytracer.Scene;
import yaphyre.shaders.CheckerShader;
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

  public static final Scene createConellBox() {

    // colors
    Color white = new Color(1, 1, 1);
    Color red = new Color(1, 0, 0);
    Color blue = new Color(0, 0, 1);

    // materials
    Material diffuseMaterial = MaterialBuilder.init().ambient(1).diffuse(0.8d).build();
    Material mirrorMaterial = MaterialBuilder.init().ambient(1).diffuse(0.1d).reflection(0.9d).build();

    // shaders
    Shaders whiteDiffuse = new SimpleShader("white-diff", diffuseMaterial, white);
    Shaders whiteMirror = new SimpleShader("white-mirr", mirrorMaterial, white);
    Shaders redDiffuse = new SimpleShader("red-diff", diffuseMaterial, red);
    Shaders blueDiffuse = new SimpleShader("blue-diff", diffuseMaterial, blue);

    // walls
    Plane back = new Plane("back", new Point3D(0, 0, 6), Normal3D.NORMAL_Z.neg(), whiteDiffuse, true);
    Plane top = new Plane("top", new Point3D(0, 3, 0), Normal3D.NORMAL_Y.neg(), whiteDiffuse, true);
    Plane bottom = new Plane("bottom", new Point3D(0, -3, 0), Normal3D.NORMAL_Y, whiteDiffuse, true);
    Plane left = new Plane("left", new Point3D(-3, 0, 0), Normal3D.NORMAL_X, redDiffuse, true);
    Plane right = new Plane("right", new Point3D(3, 0, 0), Normal3D.NORMAL_X.neg(), blueDiffuse, true);

    // spheres
    Sphere sphereLeft = new Sphere("sp-left", new Point3D(-2, -2, 3), 1, whiteMirror, true);
    Sphere sphereRight = new Sphere("sph-right", new Point3D(1, 0, 2), 1.5, whiteDiffuse, true);

    // light
    Pointlight light = new Pointlight("light", new Point3D(0, 3 - 1e-5, 3), white, 5, Falloff.Quadric);

    // put it all together
    Scene conellBox = new Scene();

    conellBox.addLightsource(light);

    conellBox.addShape(back);
    conellBox.addShape(top);
    conellBox.addShape(bottom);
    conellBox.addShape(left);
    conellBox.addShape(right);
    conellBox.addShape(sphereLeft);
    conellBox.addShape(sphereRight);

    return conellBox;
  }

  public static final Scene createSceneWithSpheres() {

    double ambientLight = 0.1;

    Point3D sphere1Center = new Point3D(0, 1.5, 0);
    double sphere1Radius = 1d;

    Point3D sphere2Center = new Point3D(2.5, 1.5, 1.5);
    double sphere2Radius = 0.5;

    Point3D planeOrigin = Point3D.ORIGIN;
    Normal3D planeNormal = Normal3D.NORMAL_Y;

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

    Material diffuseMaterial = MaterialBuilder.init().ambient(ambientLight).diffuse(0.8).build();
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
    // simpleScene.addShape(new Sphere("light1-sphere", pointlight1Position,
    // 0.05, redShader, false));
    simpleScene.addLightsource(new Pointlight("pointlight2", pointlight2Position, pointlight2Color, pointlight2Intensity, pointlight2falloff));
    // simpleScene.addShape(new Sphere("light2-sphere", pointlight2Position,
    // 0.05, greenShader, false));
    simpleScene.addLightsource(new Pointlight("pointlight3", pointlight3Position, pointlight3Color, pointlight3Intensity, pointlight3falloff));
    // simpleScene.addShape(new Sphere("light3-sphere", pointlight3Position,
    // 0.05, blueShader, false));

    return simpleScene;
  }

  public static final Scene createSimpleScene() {

    double ambientLight = 0.075d;

    Material diffuseMaterial = MaterialBuilder.init().ambient(ambientLight).diffuse(0.8).build();
    Material mirrorMaterial = MaterialBuilder.init().ambient(ambientLight).diffuse(0.1).reflection(0.9).build();

    Shaders blackDiffuse = new SimpleShader("black-diffuse", diffuseMaterial, Color.BLACK);
    Shaders whiteDiffuse = new SimpleShader("white-diffuse", diffuseMaterial, 1d, 1d, 1d);
    Shaders redDiffuse = new SimpleShader("red-diffuse", diffuseMaterial, 1d, 0d, 0d);
    Shaders whiteMirror = new SimpleShader("white-mirror", mirrorMaterial, 1d, 1d, 1d);
    Shaders redMirror = new SimpleShader("red-diffuse", mirrorMaterial, 1d, 0d, 0d);

    Shaders checkBoardShader = new CheckerShader("checker", blackDiffuse, whiteMirror);

    Lightsources pointLight = new Pointlight("light", new Point3D(-2, 5, -2), new Color(1, 1, 1), 15, Falloff.Quadric);

    Shapes plane = new Plane("plane", Point3D.ORIGIN, Normal3D.NORMAL_Y, checkBoardShader, true);
    Shapes sphere = new Sphere("sphere", new Point3D(0, 1.5, 0), 1, whiteMirror, true);
    Shapes distantSphere = new Sphere("distant-sphere", new Point3D(-2, 10, -5), 2, redDiffuse, true);

    Scene scene = new Scene();

    scene.addLightsource(pointLight);

    scene.addShape(plane);
    scene.addShape(sphere);
    scene.addShape(distantSphere);

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

    Material diffuseMaterial = MaterialBuilder.init().ambient(ambientLight).diffuse(0.8d).build();

    Shaders diffuseWhite = new SimpleShader("shader", diffuseMaterial, 1, 1, 1);

    Lightsources light = new Pointlight("light", new Point3D(-2, 5, -2), new Color(1, 1, 1), 10, Falloff.Quadric);

    Shapes plane = new Plane("plane", Point3D.ORIGIN, Normal3D.NORMAL_Y, diffuseWhite, true);
    Shapes sphere = new Sphere("sphere", new Point3D(0, 1, 0), 1, diffuseWhite, true);

    Scene scene = new Scene();

    scene.addLightsource(light);
    scene.addShape(plane);
    scene.addShape(sphere);

    return scene;
  }
}
