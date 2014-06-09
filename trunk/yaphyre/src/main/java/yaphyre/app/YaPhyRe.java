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
import yaphyre.cameras.FilmBasedCamera;
import yaphyre.cameras.PerspectiveCamera;
import yaphyre.core.Camera;
import yaphyre.core.Film;
import yaphyre.core.Sampler;
import yaphyre.core.Scene;
import yaphyre.films.ImageFile;
import yaphyre.math.FovCalculator;
import yaphyre.math.Normal3D;
import yaphyre.math.Point3D;
import yaphyre.math.Transformation;
import yaphyre.samplers.RegularSampler;
import yaphyre.samplers.SingleValueSampler;
import yaphyre.samplers.StratifiedSampler;
import yaphyre.shapes.SimpleSphere;
import yaphyre.tracers.DebuggingRayCaster;

import static yaphyre.math.MathUtils.EPSILON;

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

    public static final String COMMANDLINE_OPTION_CAMERA_SAMPLER = "cameraSampler";
    public static final String COMMANDLINE_OPTION_GAMMA = "gamma";

    public static final double DEFAULT_GAMMA = 1d;

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

        scene.addShape(new SimpleSphere(Transformation.IDENTITY, null));
//		scene.addShape(Sphere.createSphere(Point3D.ORIGIN, 1d, null));
//        scene.addShape(new Plane(Transformation.IDENTITY, null));

		ImageFile film = new ImageFile(640, 480);

        final double hFov = FovCalculator.FullFrame35mm.calculateHorizontalFov(50d);
        final double aspectRatio = ((double) film.getNativeResolution().getFirst()) / ((double) film.getNativeResolution().getSecond());

        final Camera camera = new PerspectiveCamera(
            film,
            new Point3D(2, 2, -2),
            Point3D.ORIGIN,
            Normal3D.NORMAL_Y,
            hFov,
            aspectRatio,
            EPSILON,
            1d / EPSILON);

//		Camera camera = new OrthographicCamera(film, 8d, 6d, 100d);
        scene.addCamera(camera);

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
            new DebuggingRayCaster()
        ));
    }

    private static void renderScene(Scene scene) {
		for (Camera camera : scene.getCameras()) {
			camera.renderScene(scene);
		}
	}

	private static void saveImages(Scene scene, double gamma) {
		int cameraIndex = 0;
		for (Camera camera : scene.getCameras()) {
			if (camera instanceof FilmBasedCamera) {
				FilmBasedCamera filmBasedCamera = (FilmBasedCamera) camera;
				final Film film = filmBasedCamera.getFilm();
				if (film instanceof ImageFile) {
					ImageFile imageFileFilm = (ImageFile) film;
					String fileName = String.format("color_%d.%s",
							cameraIndex++,
							ImageFile.ImageFormat.PNG.getDefaultFileExtension());
                    imageFileFilm.safeAsImage(fileName, ImageFile.ImageFormat.PNG, gamma);
				}
			}
		}
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
				default:
					throw new IllegalArgumentException("Unknown type for  sampler: " + samplerName);
			}
		}
		return sampler;
	}

	private static Options createCommandLineOptions() {
		Options options = new Options();

        OptionBuilder.withArgName("<sampler name> [number of samples]");
        OptionBuilder.withDescription("The Sampler to use for the camera (single, regular, stratified, random)");
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
