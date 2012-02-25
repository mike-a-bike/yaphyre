/*
 * Copyright 2011 Michael Bieri
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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yaphyre.SceneReader.SceneReaderResult;
import yaphyre.core.Film;
import yaphyre.core.Sampler;
import yaphyre.films.ImageFile;
import yaphyre.films.ImageFile.ImageFormat;
import yaphyre.raytracer.RayTracer;
import yaphyre.samplers.JitteredSampler;
import yaphyre.samplers.RandomSampler;
import yaphyre.samplers.RegularSampler;
import yaphyre.samplers.SinglePointSampler;

/**
 * The main class starting the application. This class parses the command line,
 * prepares the environment and calls the renderer.
 * 
 * @version $Revision$
 * 
 * @author Michael Bieri
 * @author $LastChangedBy$
 */
public class YaPhyRe {

  private static final Logger LOGGER = LoggerFactory.getLogger(YaPhyRe.class);

  private static final String DEFAULT_HEIGHT = "960";

  private static final String DEFAULT_WIDTH = "1280";

  private static final String DEFAULT_OUTPUT_FILE = "color.png";

  private static final String DEFAULT_FILE_FORMAT = "PNG";

  private static final int DEFAULT_SAMPLE_NUMBER = 4;

  private static Options commandLineOptions = null;

  public static void main(String... args) throws IOException {

    LOGGER.info("Starting renderer");

    LOGGER.info("Parsing command line");
    CommandLine commandLine = null;
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

    LOGGER.debug("Setup camera");

    int imageWidth = Integer.parseInt(commandLine.getOptionValue('w', DEFAULT_WIDTH));
    int imageHeight = Integer.parseInt(commandLine.getOptionValue('h', DEFAULT_HEIGHT));
    String imageFormat = commandLine.getOptionValue('f', DEFAULT_FILE_FORMAT);

    Film film = new ImageFile(imageWidth, imageHeight, ImageFormat.valueOf(imageFormat));

    LOGGER.debug("Read scene");
    SceneReaderResult readScene = readScene(commandLine);
    readScene.getCamera().setFilm(film);

    LOGGER.debug("Initialize sampler");
    Sampler sampler = parseSampler(commandLine);

    LOGGER.debug("Initializing RayTracer");
    RayTracer rayTracer = new RayTracer();
    rayTracer.setScene(readScene.getScene());
    rayTracer.setCamera(readScene.getCamera());
    rayTracer.setSampler(sampler);
    if (commandLine.hasOption("single")) {
      rayTracer.useSingleThreadedRenderer();
    }

    LOGGER.info("Render image");
    film = rayTracer.render();

    LOGGER.debug("Write image file");
    String fileName = commandLine.getOptionValue('o', DEFAULT_OUTPUT_FILE);
    File imageFile = new File(fileName);
    film.writeImageFile(imageWidth, imageHeight, imageFile.getAbsolutePath());

    LOGGER.info("Output written to: {}", imageFile.getAbsolutePath());

    if (commandLine.hasOption("show")) {
      if (Desktop.isDesktopSupported()) {
        LOGGER.info("Opening file: {}", imageFile.getAbsolutePath());
        Desktop.getDesktop().open(imageFile);
      } else {
        LOGGER.warn("Your desktop environment does not support the opening of the image");
      }
    }

    LOGGER.info("Renderer finished");

  }

  private static SceneReaderResult readScene(CommandLine commandLine) {
    SceneReaderResult result = null;
    String sceneName = commandLine.getOptionValue('s').trim().toLowerCase();
    LOGGER.info("Trying to load scene <{}>", sceneName);
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
    }
    else {
      LOGGER.warn("Scene not found! Using fallback scene (first)");
      result = SceneReader.createFirstLight();
    }
    return result;
  }

  private static Sampler parseSampler(CommandLine commandLine) {
    String[] samplerSettings = commandLine.getOptionValues('a');
    Sampler sampler = null;
    int sampleCount = DEFAULT_SAMPLE_NUMBER;
    if (samplerSettings.length == 2) {
      sampleCount = Integer.parseInt(samplerSettings[1]);
    }
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
    commandLineOptions.addOption(OptionBuilder.withArgName("<first|spheres|simple|dof|area>").hasArg().isRequired().withLongOpt("scene").withDescription("Scene to render").create('s'));
    commandLineOptions.addOption(OptionBuilder.withArgName("file").hasArg().withLongOpt("out").withDescription("Output file name").create('o'));
    commandLineOptions.addOption(OptionBuilder.withArgName("format").hasArg().withLongOpt("format").withDescription("Format of the output image file").create('f'));
    commandLineOptions.addOption(OptionBuilder.withArgName("pixel").hasArg().withLongOpt("width").withDescription("Width of the rendered image").create('w'));
    commandLineOptions.addOption(OptionBuilder.withArgName("pixel").hasArg().withLongOpt("height").withDescription("Height of the rendered image").create('h'));
    commandLineOptions.addOption(OptionBuilder.withArgName("strategy [samples]").hasArgs(2).withLongOpt("sampling").isRequired().withDescription("Type and number of samples for anti aliasing").create('a'));
    commandLineOptions.addOption(OptionBuilder.withLongOpt("single").withDescription("Perform rendering with in a single task").create());
    commandLineOptions.addOption(OptionBuilder.withLongOpt("show").withDescription("Shows the created image when finished").create());
    commandLineOptions.addOption(OptionBuilder.withLongOpt("help").withDescription("Shows this help").create());
    return parser.parse(commandLineOptions, args);
  }
}
