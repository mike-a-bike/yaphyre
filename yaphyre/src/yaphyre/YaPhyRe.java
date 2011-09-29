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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yaphyre.geometry.Point3D;
import yaphyre.geometry.Vector3D;
import yaphyre.raytracer.RayTracer;
import yaphyre.raytracer.Scene;

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
    Point3D cameraPosition = new Point3D(0, 25, -100);
    Point3D lookAt = new Point3D(0, 1, 0);
    Vector3D cameraDirection = new Vector3D(cameraPosition, lookAt);

    double frameWidth = 12d;
    double frameHeight = 9d;

    int imageWidth = Integer.parseInt(commandLine.getOptionValue('w', DEFAULT_WIDTH));
    int imageHeight = Integer.parseInt(commandLine.getOptionValue('h', DEFAULT_HEIGHT));

    LOGGER.debug("Read scene");
    Scene scene = SceneReader.createSimpleScene();
    // SceneReaders<File> xmlFileReader = new XMLFileSceneReader();
    // File sceneFile = new File(commandLine.getOptionValue('s'));
    // Scene scene = xmlFileReader.readScene(sceneFile);
    // if (scene == null) {
    // LOGGER.error("Could not read scene from: {}", sceneFile.getPath());
    // return;
    // }

    RayTracer rayTracer = new RayTracer();
    rayTracer.setScene(scene);

    LOGGER.debug("Render image");
    BufferedImage renderedImage = rayTracer.render(imageWidth, imageHeight, frameWidth, frameHeight, cameraPosition, cameraDirection);

    LOGGER.debug("Write image file");
    String imageFormat = commandLine.getOptionValue('f', DEFAULT_FILE_FORMAT);
    File imageFile = new File(commandLine.getOptionValue('o', DEFAULT_OUTPUT_FILE));
    FileOutputStream imageFileStream = new FileOutputStream(imageFile);
    ImageIO.write(renderedImage, imageFormat, imageFileStream);
    imageFileStream.close();

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

  private static void printHelp() {
    HelpFormatter formatter = new HelpFormatter();
    formatter.setWidth(999);
    formatter.printHelp("java YaPhyRe", commandLineOptions, true);
  }

  @SuppressWarnings("static-access")
  private static CommandLine parseCommandLine(String... args) throws ParseException {
    CommandLineParser parser = new PosixParser();
    commandLineOptions = new Options();
    commandLineOptions.addOption(OptionBuilder.withArgName("file").hasArg().isRequired().withLongOpt("scene").withDescription("Scene file name").create('s'));
    commandLineOptions.addOption(OptionBuilder.withArgName("file").hasArg().withLongOpt("out").withDescription("Output file name").create('o'));
    commandLineOptions.addOption(OptionBuilder.withArgName("format").hasArg().withLongOpt("format").withDescription("Format of the output image file").create('f'));
    commandLineOptions.addOption(OptionBuilder.withArgName("pixel").hasArg().withLongOpt("width").withDescription("Width of the rendered image").create('w'));
    commandLineOptions.addOption(OptionBuilder.withArgName("pixel").hasArg().withLongOpt("height").withDescription("Height of the rendered image").create('h'));
    commandLineOptions.addOption(OptionBuilder.withLongOpt("show").withDescription("Shows the created image when finished").create());
    commandLineOptions.addOption(OptionBuilder.withLongOpt("help").withDescription("Shows this help").create());
    return parser.parse(commandLineOptions, args);
  }
}
