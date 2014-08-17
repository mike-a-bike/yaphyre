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

package yaphyre.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yaphyre.app.dependencies.DefaultBindingModule;
import yaphyre.app.dependencies.SolverBindingModule;
import yaphyre.core.api.Camera;
import yaphyre.core.api.Sampler;
import yaphyre.core.api.Scene;
import yaphyre.core.cameras.PerspectiveCamera;
import yaphyre.core.films.ImageFile;
import yaphyre.core.lights.AmbientLight;
import yaphyre.core.lights.PointLight;
import yaphyre.core.math.Color;
import yaphyre.core.math.FovCalculator;
import yaphyre.core.math.Normal3D;
import yaphyre.core.math.Point3D;
import yaphyre.core.math.Transformation;
import yaphyre.core.samplers.HaltonSampler;
import yaphyre.core.samplers.RegularSampler;
import yaphyre.core.samplers.SingleValueSampler;
import yaphyre.core.samplers.StratifiedSampler;
import yaphyre.core.shaders.ColorShader;
import yaphyre.core.shapes.Plane;
import yaphyre.core.shapes.SimpleSphere;
import yaphyre.core.tracers.RayCaster;

import java.util.concurrent.atomic.AtomicInteger;

import static yaphyre.core.math.MathUtils.EPSILON;

/**
 * YaPhyRe: Renderer application class. This reads the commandline for the sampler to use and sets up a simple,
 * hardcoded scene. It then renders the scene and saves the result as image file. In the future,
 * this will be the entry point from where the rendering process is setup and executed.
 *
 * @author Michael Bieri
 * @since 08.09.13
 */
public class YaPhyRe {

    private static final Logger LOGGER = LoggerFactory.getLogger(YaPhyRe.class);

    private static final String COMMANDLINE_OPTION_CAMERA_SAMPLER = "cameraSampler";
    private static final String COMMANDLINE_OPTION_GAMMA = "gamma";
    private static final double DEFAULT_GAMMA = 1d;

    public static void main(String... arguments) {

        final double gamma;

        LOGGER.info("------------------------");
        LOGGER.info("-- Welcome to YaPhyRe --");
        LOGGER.info("------------------------");

        // Parsing the commandline
        LOGGER.info("Reading CommandLine");
        final CommandLine commandLine = parseCommandLine(arguments);
        gamma = evaluateGamma(commandLine);

        // Setup the injector
        LOGGER.info("Setting up Injector");
        final Injector injector = setupInjector(commandLine);

        // Preparing the scene
        LOGGER.info("Setting up Scene");
        final Scene scene = setupScene(injector);

        // Render the scene
        LOGGER.info("Render Scene");
        renderScene(scene);

        // Save the result
        LOGGER.info("Save Result");
        saveImages(scene, gamma);

        LOGGER.info("Finished");
    }

    private static double evaluateGamma(CommandLine commandLine) {
        double gamma;
        try {
            gamma = Double.parseDouble(commandLine.getOptionValue(COMMANDLINE_OPTION_GAMMA));
        } catch (NullPointerException | NumberFormatException exception) {
            if (exception instanceof NumberFormatException) {
                LOGGER.warn("Unable to parse gamma value.");
            }
            LOGGER.info("Using default gamma value: " + DEFAULT_GAMMA);
            gamma = DEFAULT_GAMMA;
        }
        return gamma;
    }

    private static Scene setupScene(Injector injector) {
        Scene scene = injector.getInstance(Scene.class);

        // add primitives
        scene.addShape(new SimpleSphere(Transformation.translate(0, 2, 0), new ColorShader(new Color(.95d, .95d, .95d))));
        scene.addShape(new Plane(Transformation.IDENTITY, new ColorShader(new Color(.95d, .95d, .95d))));

        // add lights
        scene.addLight(new AmbientLight(.25d));
        scene.addLight(new PointLight(25d, Color.WHITE, new Point3D(0, 5, 0)));

        // add cameras
        final double aspectRatio = FovCalculator.FullFrame35mm.getAspectRatio();

        final int yResolution = 120;
        final int xResolution = (int) (yResolution * aspectRatio);
        final Color skyColor = new Color(0d, 0d, .25d);

        // add perspective camera
        final double hFov = FovCalculator.FullFrame35mm.calculateHorizontalFov(50d);
        ImageFile film = new ImageFile(xResolution, yResolution);
        Camera camera = new PerspectiveCamera(
            film,
            skyColor,
            new Point3D(0, 2, -10),
            new Point3D(0, 0, 0),
            Normal3D.NORMAL_Y,
            hFov,
            aspectRatio,
            EPSILON,
            1d / EPSILON);
        scene.addCamera(camera);

        // add orthographic camera
//        film = new ImageFile(xResolution, yResolution);
//        double vDimension = 6d;
//        double uDimension = vDimension * aspectRatio;
//        camera = new OrthographicCamera(film, skyColor, uDimension, vDimension, 100d);
//        scene.addCamera(camera);

        return scene;
    }

    private static Injector setupInjector(CommandLine commandLine) {
        Sampler cameraSampler = createSampler(commandLine.getOptionValues(COMMANDLINE_OPTION_CAMERA_SAMPLER));
        Sampler lightSampler = new SingleValueSampler();
        Sampler defaultSampler = new SingleValueSampler();
        return Guice.createInjector(new DefaultBindingModule(
                () -> cameraSampler,
                () -> lightSampler,
                () -> defaultSampler,
                //            new DebuggingRayCaster(false)
                new RayCaster()
            ),
            new SolverBindingModule());
    }

    private static void renderScene(Scene scene) {
        scene.getCameras().forEach(cam -> cam.renderScene(scene));
    }

    private static void saveImages(Scene scene, double gamma) {
        final AtomicInteger cameraIndex = new AtomicInteger(0);

        scene.getCameras().stream()
            .map(Camera::getFilm)
            .filter(film -> ImageFile.class.isAssignableFrom(film.getClass()))
            .map(ImageFile.class::cast)
            .forEach(imageFileFilm -> saveFilmToFile(gamma, cameraIndex.getAndIncrement(), imageFileFilm, ImageFile.ImageFormat.PNG));
    }

    private static void saveFilmToFile(double gamma, int cameraNumber, ImageFile imageFileFilm, ImageFile.ImageFormat imageFormat) {
        final String imageFileExtension = imageFormat.getDefaultFileExtension();
        final String fileName = String.format("color_%d.%s", cameraNumber, imageFileExtension);
        imageFileFilm.safeAsImage(fileName, imageFormat, gamma);
    }

    private static CommandLine parseCommandLine(String[] arguments) {
        CommandLine commandLine = null;
        Options commandLineOptions = createCommandLineOptions();
        try {
            commandLine = new PosixParser().parse(commandLineOptions, arguments);
        } catch (ParseException e) {
            LOGGER.error("Invalid program call. See usage below.");
            printHelp(commandLineOptions);
            System.exit(1);
        }
        return commandLine;
    }

    private static Sampler createSampler(String[] commandlineArguments) {
        LOGGER.debug("Creating sampler for: {}", (Object) commandlineArguments);
        String samplerName = commandlineArguments[0];
        Sampler sampler;
        if (samplerName.equalsIgnoreCase("single")) {
            sampler = new SingleValueSampler();
        } else {
            int sampleCount = Integer.parseInt(commandlineArguments[1]);
            switch (samplerName.toLowerCase()) {
                case "regular":
                    sampler = new RegularSampler(sampleCount);
                    break;
                case "stratified":
                    sampler = new StratifiedSampler(sampleCount);
                    break;
                case "halton":
                    sampler = new HaltonSampler(sampleCount);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown type for  sampler: " + samplerName);
            }
        }
        return sampler;
    }

    private static Options createCommandLineOptions() {
        Options options = new Options();

        OptionBuilder.withArgName("<sampler name> [number of samples]");
        OptionBuilder.withDescription("The Sampler to use for the camera (single, regular, stratified, halton)");
        OptionBuilder.hasArgs(2);
        OptionBuilder.isRequired();
        options.addOption(OptionBuilder.create(COMMANDLINE_OPTION_CAMERA_SAMPLER));

        OptionBuilder.withArgName("gamma value");
        OptionBuilder.withDescription("Optional gamma correction");
        OptionBuilder.hasArg();
        OptionBuilder.isRequired(false);
        options.addOption(OptionBuilder.create(COMMANDLINE_OPTION_GAMMA));

        return options;
    }

    private static void printHelp(Options commandLineOptions) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(999, "java -jar YaPhyRe.jar", null, commandLineOptions, null, true);
    }

}
