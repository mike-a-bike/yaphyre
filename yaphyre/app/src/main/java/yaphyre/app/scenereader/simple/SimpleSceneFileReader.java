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

package yaphyre.app.scenereader.simple;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import yaphyre.app.dependencies.DefaultBindingModule;
import yaphyre.app.dependencies.SolverBindingModule;
import yaphyre.app.scenereader.SceneReader;
import yaphyre.app.scenereader.simple.jaxb.Camera;
import yaphyre.app.scenereader.simple.jaxb.GeometryBase;
import yaphyre.app.scenereader.simple.jaxb.GlobalSettings;
import yaphyre.app.scenereader.simple.jaxb.LightBase;
import yaphyre.app.scenereader.simple.jaxb.NamedType;
import yaphyre.app.scenereader.simple.jaxb.OrthographicCamera;
import yaphyre.app.scenereader.simple.jaxb.PerspectiveCamera;
import yaphyre.app.scenereader.simple.jaxb.Rotate;
import yaphyre.app.scenereader.simple.jaxb.Sampler;
import yaphyre.app.scenereader.simple.jaxb.Scale;
import yaphyre.app.scenereader.simple.jaxb.SimpleScene;
import yaphyre.app.scenereader.simple.jaxb.TransformationBase;
import yaphyre.app.scenereader.simple.jaxb.Translate;
import yaphyre.core.api.Film;
import yaphyre.core.api.Light;
import yaphyre.core.api.Scene;
import yaphyre.core.api.Shader;
import yaphyre.core.api.Shape;
import yaphyre.core.api.Tracer;
import yaphyre.core.films.ImageFile;
import yaphyre.core.lights.AmbientLight;
import yaphyre.core.lights.PointLight;
import yaphyre.core.math.Color;
import yaphyre.core.math.MathUtils;
import yaphyre.core.math.Normal3D;
import yaphyre.core.math.Point3D;
import yaphyre.core.math.Transformation;
import yaphyre.core.math.Vector3D;
import yaphyre.core.samplers.HaltonSampler;
import yaphyre.core.samplers.RegularSampler;
import yaphyre.core.samplers.SingleValueSampler;
import yaphyre.core.samplers.StratifiedSampler;
import yaphyre.core.shaders.ColorShader;
import yaphyre.core.shapes.Plane;
import yaphyre.core.shapes.SimpleSphere;
import yaphyre.core.tracers.DebuggingRayCaster;
import yaphyre.core.tracers.RayCaster;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Scene reader, parsing a simple scene description file to create the scene from.
 *
 * @author axmbi03
 * @since 01.07.2014
 */
public class SimpleSceneFileReader implements SceneReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSceneFileReader.class);

    private static final Normal3D CAMERA_DEFAULT_UP = Normal3D.NORMAL_Y;
    private static final Color CAMERA_DEFAULT_SKY_COLOR = Color.BLACK;
    private static final double CAMERA_DEFAULT_NEAR = MathUtils.EPSILON;
    private static final double CAMERA_DEFAULT_FAR = 1d / MathUtils.EPSILON;

    private final File sceneFile;

    public SimpleSceneFileReader(@Nonnull File sceneFile) {
        checkArgument(sceneFile.exists());
        checkArgument(sceneFile.canRead());
        this.sceneFile = sceneFile;
    }

    @Override
    @Nonnull
    public Optional<Scene> readScene() {
        return parseFile(sceneFile).map(this::createSceneFromSimpleSceneModel);
    }

    private Optional<SimpleScene> parseFile(@Nonnull File sceneFile) {

        try {
            final JAXBContext context = JAXBContext.newInstance(SimpleScene.class);
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            final Schema schema = schemaFactory.newSchema(
                ClassLoader.getSystemResource("yaphyre/app/scenereader/simple/SimepleFileReaderSceneDescription.xsd")
            );

            unmarshaller.setSchema(schema);

            return Optional.ofNullable(((SimpleScene) unmarshaller.unmarshal(sceneFile)));

        } catch (JAXBException | SAXException e) {
            LOGGER.error("Error reading simple scene file");
            LOGGER.debug("SimpleSceneFileReader error: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    private Scene createSceneFromSimpleSceneModel(@Nonnull SimpleScene simpleScene) {

        Scene result = setupScene(simpleScene.getGlobalSettings());

        simpleScene.getCameras().getOrthographicCameraOrPerspectiveCamera().stream()
            .map(this::mapCamera)
            .forEach(result::addCamera);

        simpleScene.getGeometry().getSimpleSphereOrPlane().stream()
            .map(this::mapGeometry)
            .forEach(result::addShape);

        simpleScene.getLights().getAmbientLightOrPointLight().stream()
            .map(this::mapLight)
            .forEach(result::addLight);

        return result;
    }

    @Nonnull
    private Light mapLight(@Nonnull LightBase lightBase) {
        final double power = lightBase.getPower();
        final String lightTypeName = lightBase.getClass().getSimpleName();

        switch (lightTypeName) {
            case "PointLight":
                final Color color = createColor(lightBase.getColor());
                final Point3D position = createPoint3D(yaphyre.app.scenereader.simple.jaxb.PointLight.class.cast(lightBase).getPosition());
                return new PointLight(power, color, position);

            case "AmbientLight":
                return new AmbientLight(power);

        }

        final String errorMessage = "Unknown light type: '" + lightTypeName + "'";
        LOGGER.error(errorMessage);
        throw new RuntimeException(errorMessage);
    }

    @Nonnull
    private Shape mapGeometry(@Nonnull GeometryBase geometryBase) {

        final String geometryTypeName = geometryBase.getClass().getSimpleName();

        Shader shader = mapShader(
            Optional.ofNullable(geometryBase.getShader())
                .<NamedType>map(GeometryBase.Shader::getColorShader) // Cast no longer needed when more than one shader type is known
                .orElseGet(() -> ((NamedType) geometryBase.getShaderRef()))
        );

        Transformation transformation = Optional
            .ofNullable(geometryBase.getTransformationRef())
            .map(ref -> mapTransformation((TransformationBase) ref))
            .orElseGet(
                () -> Lists.reverse(geometryBase.getTransformation().getIdentityOrScaleOrTranslate())
                    .stream()
                    .map(this::mapTransformation)
                    .reduce(Transformation.IDENTITY, (t1, t2) -> t1.mul(t2))
            );

        switch (geometryTypeName) {
            case "SimpleSphere":
                return new SimpleSphere(transformation, shader);

            case "Plane":
                return new Plane(transformation, shader);
        }

        final String errorMessage = "Unknown geometry type: '" + geometryTypeName + "'";
        LOGGER.error(errorMessage);
        throw new RuntimeException(errorMessage);
    }

    @Nonnull
    private Shader mapShader(@Nonnull NamedType shader) {
        final String shaderName = shader.getClass().getSimpleName();

        switch (shaderName) {
            case "ColorShader":
                Color color = createColor(((yaphyre.app.scenereader.simple.jaxb.ColorShader) shader).getColor());
                return new ColorShader(color);
        }

        final String errorMessage = "Unknown shader type: '" + shaderName + "'";
        LOGGER.error(errorMessage);
        throw new RuntimeException(errorMessage);
    }

    @Nonnull
    private Transformation mapTransformation(@Nonnull TransformationBase transformation) {
        final String transformationName = transformation.getClass().getSimpleName();

        switch (transformationName) {
            case "Identity":
                return Transformation.IDENTITY;

            case "Translate":
                final List<Double> offsets = ((Translate) transformation).getOffsets();
                return Transformation.translate(offsets.get(0), offsets.get(1), offsets.get(2));

            case "Scale":
                final List<Double> factors = ((Scale) transformation).getFactors();
                return Transformation.scale(factors.get(0), factors.get(1), factors.get(2));

            case "Rotate":
                final Rotate rotate = (Rotate) transformation;
                if (rotate.getAxis() != null) {
                    final Rotate.Axis rotateAxis = rotate.getAxis();
                    switch (rotateAxis.getAxis()) {
                        case X:
                            return Transformation.rotateX(rotateAxis.getAmount());
                        case Y:
                            return Transformation.rotateY(rotateAxis.getAmount());
                        case Z:
                            return Transformation.rotateZ(rotateAxis.getAmount());
                    }
                } else {
                    final Rotate.Free rotateFree = rotate.getFree();
                    return Transformation.rotate(rotateFree.getAmount(), createVector3D(rotateFree.getAxis()));
                }

        }

        final String errorMessage = "Unknown transformation type: '" + transformationName + "'";
        LOGGER.error(errorMessage);
        throw new RuntimeException(errorMessage);
    }

    @Nonnull
    private Scene setupScene(GlobalSettings globalSettings) {
        final Injector injector = Guice.createInjector(
            new DefaultBindingModule(
                createSamplerSupplier(globalSettings.getCameraSampler()),
                createSamplerSupplier(globalSettings.getLightSampler()),
                createSamplerSupplier(globalSettings.getDefaultSampler()),
                createTracer(globalSettings)
            ),
            new SolverBindingModule());
        return injector.getInstance(Scene.class);
    }

    @Nonnull
    private Supplier<yaphyre.core.api.Sampler> createSamplerSupplier(Sampler sampler) {

        final int numberOfSamples = Optional.ofNullable(sampler.getSamples())
            .map(BigInteger::intValue)
            .orElse(Integer.MIN_VALUE);

        switch (sampler.getMethod()) {
            case SINGLE:
                SingleValueSampler singleValueSampler = new SingleValueSampler();
                return () -> singleValueSampler;

            case REGULAR:
                if (sampler.isSingleInstance()) {
                    RegularSampler regularSampler = new RegularSampler(numberOfSamples);
                    return () -> regularSampler;
                }
                return () -> new RegularSampler(numberOfSamples);

            case STRATIFIED:
                if (sampler.isSingleInstance()) {
                    StratifiedSampler stratifiedSampler = new StratifiedSampler(numberOfSamples);
                    return () -> stratifiedSampler;
                }
                return () -> new StratifiedSampler(numberOfSamples);

            case HALTON:
                if (sampler.isSingleInstance()) {
                    HaltonSampler haltonSampler = new HaltonSampler(numberOfSamples);
                    return () -> haltonSampler;
                }
                return () -> new HaltonSampler(numberOfSamples);
        }
        final String errorMessage = "Unknown surface integrator found: " + sampler.getMethod();
        LOGGER.error(errorMessage);
        throw new RuntimeException(errorMessage);
    }

    private Tracer createTracer(GlobalSettings globalSettings) {

        switch (globalSettings.getSurfaceIntegrator()) {
            case DEBUGGING_RAY_CASTER:
                return new DebuggingRayCaster(false);

            case RAY_CASTER:
                return new RayCaster();
        }

        final String errorMessage = "Unknown surface integrator found: " + globalSettings.getSurfaceIntegrator();
        LOGGER.error(errorMessage);
        throw new RuntimeException(errorMessage);
    }

    @Nonnull
    private yaphyre.core.api.Camera mapCamera(@Nonnull Camera camera) {
//        final String cameraName = camera.getName();
//        final ImageFile.ImageFormat imageFormat = ImageFile.ImageFormat.valueOf(camera.getFiletype().value());
        final Film film = new ImageFile(camera.getResolution().get(0), camera.getResolution().get(1));
        final Point3D position = createPoint3D(camera.getPosition());
        final Point3D lookAt = createPoint3D(camera.getLookAt());
        final Normal3D up = camera.getUp().size() == 3 ? createNormal3D(camera.getUp()) : CAMERA_DEFAULT_UP;
        final Color skyColor = camera.getSkycolor().size() == 3 ? createColor(camera.getSkycolor()) : CAMERA_DEFAULT_SKY_COLOR;
        switch (camera.getClass().getSimpleName()) {
            case "OrthographicCamera":
                final OrthographicCamera orthographicCamera = (OrthographicCamera) camera;
                final double uDimension = orthographicCamera.getUDimension();
                final double vDimension = orthographicCamera.getVDimension();
                final double zPosition = orthographicCamera.getZPosition();
                return new yaphyre.core.cameras.OrthographicCamera(film, skyColor, uDimension, vDimension, zPosition);

            case "PerspectiveCamera":
                final PerspectiveCamera perspectiveCamera = ((PerspectiveCamera) camera);
                final double aspectRatio = perspectiveCamera.getAspectRatio();
                final double fov = perspectiveCamera.getFov();
                final double near = Optional.ofNullable(perspectiveCamera.getNear()).orElse(CAMERA_DEFAULT_NEAR);
                final double far = Optional.ofNullable(perspectiveCamera.getFar()).orElse(CAMERA_DEFAULT_FAR);
                return new yaphyre.core.cameras.PerspectiveCamera(film, skyColor, position, lookAt, up, fov, aspectRatio, near, far);

        }

        final String errorMessage = "unknown camera type '" + camera.getClass().getSimpleName() + "' found";
        LOGGER.error(errorMessage);
        throw new RuntimeException(errorMessage);
    }

    @Nonnull
    private Vector3D createVector3D(@Nonnull List<Double> components) {
        return createFromNumberTriplet(Vector3D.class, components);
    }

    @Nonnull
    private Point3D createPoint3D(@Nonnull List<Double> components) {
        return createFromNumberTriplet(Point3D.class, components);
    }

    @Nonnull
    private Normal3D createNormal3D(@Nonnull List<Double> components) {
        return createFromNumberTriplet(Normal3D.class, components);
    }

    @Nonnull
    private Color createColor(@Nonnull List<Double> components) {
        return createFromNumberTriplet(Color.class, components);
    }

    @Nonnull
    private <T> T createFromNumberTriplet(@Nonnull Class<T> targetClass, @Nonnull List<Double> components) {
        checkNotNull(components);
        checkArgument(components.size() == 3);

        try {
            final Constructor<T> constructor = targetClass.getDeclaredConstructor(double.class, double.class, double.class);
            return constructor.newInstance(components.get(0), components.get(1), components.get(2));
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            final String errorMessage = "Unable to create instance of '" + targetClass.getSimpleName() + "'";
            LOGGER.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }

    }

}
