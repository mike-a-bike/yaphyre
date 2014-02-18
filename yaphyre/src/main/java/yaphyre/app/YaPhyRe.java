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
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yaphyre.cameras.FilmBasedCamera;
import yaphyre.cameras.PerspectiveCamera;
import yaphyre.core.Camera;
import yaphyre.core.Sampler;
import yaphyre.core.Scene;
import yaphyre.core.Tracer;
import yaphyre.films.ImageFile;
import yaphyre.math.*;
import yaphyre.samplers.SinglePointSampler;
import yaphyre.shapes.Plane;
import yaphyre.shapes.Sphere;
import yaphyre.tracers.SimpleRayCaster;

/**
 * YaPhyRe
 *
 * @author Michael Bieri
 * @since 08.09.13
 */
public class YaPhyRe {

    private static final Logger LOGGER = LoggerFactory.getLogger(YaPhyRe.class);

    private static Injector injector;

    public static void main(String... arguments) {

        LOGGER.info("------------------------");
        LOGGER.info("-- Welcome to YaPhyRe --");
        LOGGER.info("------------------------");

        // Parsing the commandline
        LOGGER.info("Reading CommandLine");
        CommandLine commandLine = parseCommandLine(arguments);

        // Setup the injector
        LOGGER.info("Setting up Injector");
        setupInjector(commandLine);

        // Preparing the scene
        LOGGER.info("Setting up Scene");
        Scene scene = setupScene(injector);

        // Render the scene
        LOGGER.info("Render Scene");
        renderScene(scene);

        // Save the result
        LOGGER.info("Save Result");
        saveImages(scene);

        LOGGER.info("Finished");
    }

    private static void saveImages(Scene scene) {
        int cameraIndex = 0;
        for (Camera camera : scene.getCameras()) {
            if (camera instanceof FilmBasedCamera) {
                FilmBasedCamera<?> filmBasedCamera = (FilmBasedCamera) camera;
                if (filmBasedCamera.getFilm() instanceof ImageFile) {
                    ImageFile imageFileFilm = (ImageFile) filmBasedCamera.getFilm();
                    String fileName = String.format("color_%d.%s",
                            cameraIndex++,
                            ImageFile.ImageFormat.PNG.getDefaultFileExtension());
                    imageFileFilm.safeAsImage(fileName, ImageFile.ImageFormat.PNG);
                }
            }
        }
    }

    private static void renderScene(Scene scene) {
        for (Camera camera : scene.getCameras()) {
            camera.renderScene(scene);
        }
    }

    private static Scene setupScene(Injector injector) {
        Scene scene = injector.getInstance(Scene.class);

        scene.addShape(new Sphere(Transformation.IDENTITY, 0, 360d, 0, 180d, null));
        scene.addShape(new Plane(Transformation.IDENTITY, null));

        ImageFile film = new ImageFile(640, 480);

        final double hFov = FovCalculator.FullFrame35mm.calculateHorizontalFov(50d);
        final double aspectRatio = ((double) film.getNativeResolution().getFirst()) / ((double) film.getNativeResolution().getSecond());

        final Camera camera = new PerspectiveCamera<>(
                film,
                new Point3D(2, 2, -2),
                Point3D.ORIGIN,
                Normal3D.NORMAL_Y,
                hFov,
                aspectRatio,
                MathUtils.EPSILON,
                1d / MathUtils.EPSILON);
        scene.addCamera(camera);

        return scene;
    }

    private static void setupInjector(CommandLine commandLine) {
        String cameraSamplerName = commandLine.getOptionValue("cameraSampler");
        Sampler cameraSampler = new SinglePointSampler();
        Sampler lightSampler = new SinglePointSampler();
        Sampler defaultSampler = new SinglePointSampler();
        Tracer tracer = new SimpleRayCaster();
        injector = Guice.createInjector(new DefaultBindingModule(cameraSampler, lightSampler, defaultSampler, tracer));
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

    private static Options createCommandLineOptions() {
        Options options = new Options();

        options.addOption(
                OptionBuilder.withArgName("sampler name")
                        .withDescription("The Sampler to use for the camera (single, jittered, regular, random)")
                        .hasArg()
                        .isRequired()
                        .create("cameraSampler"));

        return options;
    }

    private static void printHelp(Options commandLineOptions) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(999, "java -jar YaPhyRe.jar", null, commandLineOptions, null, true);
    }

}
