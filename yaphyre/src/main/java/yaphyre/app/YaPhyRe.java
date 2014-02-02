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
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yaphyre.cameras.PerspectiveCamera;
import yaphyre.core.Scene;
import yaphyre.films.ImageFile;
import yaphyre.math.FovCalculator;
import yaphyre.math.MathUtils;
import yaphyre.math.Normal3D;
import yaphyre.math.Point3D;
import yaphyre.math.Transformation;
import yaphyre.shapes.Plane;
import yaphyre.shapes.Sphere;

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
		Options commandLineOptions = createCommandLineOptions();

		// Setup the injector
		LOGGER.info("Setting up Injector");
		setupInjector();

		// Preparing the scene
		LOGGER.info("Setting up Scene");
		Scene scene = new Scene();

		scene.addShape(new Sphere(Transformation.IDENTITY, 0, 360d, 0, 180d, null));
		scene.addShape(new Plane(Transformation.IDENTITY, null));

		ImageFile film = new ImageFile(640, 480);

		final double hFov = FovCalculator.FullFrame35mm.calculateHorizontalFov(50d);
		final double aspectRatio =
				((double) film.getNativeResolution().getFirst()) / ((double) film.getNativeResolution().getSecond());

		final PerspectiveCamera camera = new PerspectiveCamera<ImageFile>(film, new Point3D(2, 2, -2), Point3D.ORIGIN,
				Normal3D.NORMAL_Y, hFov, aspectRatio, MathUtils.EPSILON, 1d / MathUtils.EPSILON);

		// Inject values
		injector.injectMembers(scene);
		injector.injectMembers(camera);

		// Render the scene
		LOGGER.info("Render Scene");
		camera.renderScene(scene);

		// Save the result
		LOGGER.info("Save Result");
		film.safeAsImage(String.format("color.%s", ImageFile.ImageFormat.PNG.getDefaultFileExtention()), ImageFile.ImageFormat.PNG);

		LOGGER.info("Finished");
	}

	private static Options createCommandLineOptions() {
		Options options = new Options();

		options.addOption(
				OptionBuilder.withArgName("sampler name")
						.withDescription("The Sampler to use for the camera")
						.hasArg()
						.create("cameraSampler"));

		return options;
	}

	private static void setupInjector() {
		injector = Guice.createInjector(new DefaultBindingModule());
	}

}
