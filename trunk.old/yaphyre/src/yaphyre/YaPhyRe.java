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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

import yaphyre.core.Camera;
import yaphyre.core.Film;
import yaphyre.core.Sampler;
import yaphyre.films.ImageFile;
import yaphyre.films.ImageFile.ImageFormat;
import yaphyre.raytracer.RayTracer;
import yaphyre.raytracer.Scene;
import yaphyre.samplers.JitteredSampler;
import yaphyre.samplers.RandomSampler;
import yaphyre.samplers.RegularSampler;
import yaphyre.samplers.SinglePointSampler;
import yaphyre.scenereaders.SceneReaders;
import yaphyre.scenereaders.yaphyre.MultiStageXMLSceneReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main class starting the application. This class parses the command line, prepares the environment and calls the
 * renderer.
 *
 * @author Michael Bieri
 * @author $LastChangedBy$
 * @version $Revision$
 */
public class YaPhyRe {

	private static final Logger LOGGER = LoggerFactory.getLogger(YaPhyRe.class);

	private static final String DEFAULT_HEIGHT = "960";

	private static final String DEFAULT_WIDTH = "1280";

	private static final String DEFAULT_OUTPUT_FILE = "color";

	private static final String DEFAULT_FILE_FORMAT = "PNG";

	private static final int DEFAULT_SAMPLE_NUMBER = 4;

	private static Options commandLineOptions = null;

	private YaPhyRe() {
	}

	public static void main(String... args) throws IOException {

		LOGGER.info("Starting renderer");

		LOGGER.info("Parsing command line");
		CommandLine commandLine;
		try {
			commandLine = YaPhyRe.parseCommandLine(args);
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
			printHelp();
			return;
		}

		if (commandLine.hasOption("help")) {
			printHelp();
			return;
		}

		LOGGER.debug("Testing scene reader");
		String sceneFileName = commandLine.getOptionValue('i');
		if (sceneFileName != null && !sceneFileName.trim().isEmpty()) {
			try {
				InputStream sceneFile = new FileInputStream(sceneFileName);
				SceneReaders sceneReader = new MultiStageXMLSceneReader();
				Scene scene = sceneReader.readScene(sceneFile);
				System.out.println("Scene: " + scene.toString());
				sceneFile.close();
			} catch (Exception e) {
				LOGGER.debug("Error debugging scene reader", e);
			}
		}

		LOGGER.debug("Setup camera");
		int imageWidth = Integer.parseInt(commandLine.getOptionValue('w', DEFAULT_WIDTH));
		int imageHeight = Integer.parseInt(commandLine.getOptionValue('h', DEFAULT_HEIGHT));
		String imageFormat = commandLine.getOptionValue('f', DEFAULT_FILE_FORMAT);

		LOGGER.debug("Read scene");
		Scene readScene = readScene(commandLine);
		for (Camera camera : readScene.getCameras()) {
			Film film = new ImageFile(imageWidth, imageHeight, ImageFormat.valueOf(imageFormat));
			camera.setFilm(film);
		}

		LOGGER.debug("Initialize sampler");
		Sampler sampler = parseSampler(commandLine);

		LOGGER.debug("Initializing RayTracer");
		RayTracer rayTracer = new RayTracer();
		rayTracer.setScene(readScene);
		rayTracer.setSampler(sampler);
		if (commandLine.hasOption("single")) {
			LOGGER.info("Using single threaded renderer");
			rayTracer.useSingleThreadedRenderer();
		} else {
			LOGGER.info("Using multi threaded renderer");
		}

		LOGGER.info("Render image");
		rayTracer.render();

		LOGGER.debug("Write image file(s)");
		String fileName = commandLine.getOptionValue('o', DEFAULT_OUTPUT_FILE);
		int imageIndex = 0;
		for (Camera camera : readScene.getCameras()) {
			Film film = camera.getFilm();
			File imageFile = new File(MessageFormat.format("{0}_{1,number,000}.{2}", fileName, imageIndex,
					imageFormat));
			imageIndex++;
			film.writeImageFile(imageWidth, imageHeight, imageFile.getAbsolutePath());

			LOGGER.info("Output written to: {}", imageFile.getAbsolutePath());
		}

		LOGGER.info("Renderer finished");

	}

	private static Scene readScene(CommandLine commandLine) {
		String sceneName = commandLine.getOptionValue('s').trim().toLowerCase();
		LOGGER.info("Trying to load scene <{}>", sceneName);
		Scene result;
		if (sceneName.equals("first")) {
			result = SceneReader.createFirstLight();
		} else if (sceneName.equals("spheres")) {
			result = SceneReader.createSceneWithSpheres();
		} else if (sceneName.equals("simple")) {
			result = SceneReader.createSimpleScene();
		} else if (sceneName.equals("dof")) {
			result = SceneReader.createDOFScene();
		} else if (sceneName.equals("area")) {
			LOGGER.warn("This scene cannot be created any more.");
			result = SceneReader.createFirstLight();
		} else {
			LOGGER.warn("Scene not found! Using fallback scene (first)");
			result = SceneReader.createFirstLight();
		}
		return result;
	}

	private static Sampler parseSampler(CommandLine commandLine) {
		String[] samplerSettings = commandLine.getOptionValues('a');
		int sampleCount = DEFAULT_SAMPLE_NUMBER;
		if (samplerSettings.length == 2) {
			sampleCount = Integer.parseInt(samplerSettings[1]);
		}
		Sampler sampler;
		if (samplerSettings[0].equalsIgnoreCase("single")) {
			sampler = new SinglePointSampler();
		} else if (samplerSettings[0].equalsIgnoreCase("regular")) {
			sampler = new RegularSampler(sampleCount);
		} else if (samplerSettings[0].equalsIgnoreCase("random")) {
			sampler = new RandomSampler(sampleCount);
		} else if (samplerSettings[0].equalsIgnoreCase("jittered")) {
			sampler = new JitteredSampler(sampleCount);
		} else {
			// Fallback to make sure that a sampler is set.
			sampler = new SinglePointSampler();
		}
		LOGGER.debug("Sampler: {}", sampler.toString());
		return sampler;
	}

	private static void printHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.setWidth(999);
		formatter.printHelp("java YaPhyRe", commandLineOptions, true);
	}

	@SuppressWarnings("static-access")
	private static CommandLine parseCommandLine(String... args) throws ParseException {
		CommandLineParser parser = new PosixParser();
		commandLineOptions = new Options();
		commandLineOptions.addOption(OptionBuilder.withArgName("first|spheres|simple|dof|area")
				.hasArg()
				.isRequired()
				.withLongOpt("scene")
				.withDescription("Scene to render")
				.create('s'));
		commandLineOptions.addOption(OptionBuilder.withArgName("file").hasArg().withLongOpt("out").withDescription(
				"Output file name").create('o'));
		commandLineOptions.addOption(OptionBuilder.withArgName("file").hasArg().withLongOpt("in").withDescription(
				"Scene file name").create('i'));
		commandLineOptions.addOption(OptionBuilder.withArgName("format")
				.hasArg()
				.withLongOpt("format")
				.withDescription("Format of the output image file")
				.create('f'));
		commandLineOptions.addOption(OptionBuilder.withArgName("pixel").hasArg().withLongOpt("width").withDescription(
				"Width of the rendered image").create('w'));
		commandLineOptions.addOption(OptionBuilder.withArgName("pixel").hasArg().withLongOpt("height").withDescription(
				"Height of the rendered image").create('h'));
		commandLineOptions.addOption(OptionBuilder.withArgName("single|regular|random|jittered [samples]")
				.hasArgs(2)
				.withLongOpt("sampling")
				.isRequired()
				.withDescription("Type and number of samples for anti aliasing")
				.create('a'));
		commandLineOptions.addOption(OptionBuilder.withLongOpt("single").withDescription(
				"Perform rendering with in a single task").create());
		commandLineOptions.addOption(OptionBuilder.withLongOpt("help").withDescription("Shows this help").create());
		return parser.parse(commandLineOptions, args);
	}
}
