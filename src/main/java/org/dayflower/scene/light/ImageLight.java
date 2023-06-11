/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
 * 
 * This file is part of Dayflower.
 * 
 * Dayflower is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Dayflower is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Dayflower. If not, see <http://www.gnu.org/licenses/>.
 */
package org.dayflower.scene.light;

import static org.dayflower.utility.Floats.PI;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.isZero;
import static org.dayflower.utility.Floats.sin;

import java.io.File;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Quaternion4F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.FloatImageF;
import org.dayflower.image.ImageF;
import org.dayflower.image.PixelOperation;
import org.dayflower.sampler.Distribution2F;
import org.dayflower.sampler.Sample2F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.LightSample;
import org.dayflower.scene.Transform;

import org.macroing.art4j.color.Color3F;
import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;

/**
 * An {@code ImageLight} is a {@link Light} implementation backed by an {@link ImageF} instance.
 * <p>
 * This class is mutable and not thread-safe.
 * <p>
 * This {@code Light} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ImageLight extends Light {
	/**
	 * The name of this {@code ImageLight} class.
	 */
	public static final String NAME = "Image Light";
	
	/**
	 * The ID of this {@code ImageLight} class.
	 */
	public static final int ID = 3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AngleF angle;
	private final Distribution2F distribution;
	private final ImageF image;
	private final Vector2F scale;
	private final float radius;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ImageLight} instance.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ImageLight(image, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param image an {@link ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	public ImageLight(final ImageF image) {
		this(image, AngleF.degrees(0.0F));
	}
	
	/**
	 * Constructs a new {@code ImageLight} instance.
	 * <p>
	 * If either {@code image} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ImageLight(image, angle, new Vector2F(1.0F, 1.0F));
	 * }
	 * </pre>
	 * 
	 * @param image an {@link ImageF} instance
	 * @param angle the {@link AngleF} instance to use
	 * @throws NullPointerException thrown if, and only if, either {@code image} or {@code angle} are {@code null}
	 */
	public ImageLight(final ImageF image, final AngleF angle) {
		this(image, angle, new Vector2F(1.0F, 1.0F));
	}
	
	/**
	 * Constructs a new {@code ImageLight} instance.
	 * <p>
	 * If either {@code image}, {@code angle} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param image an {@link ImageF} instance
	 * @param angle the {@link AngleF} instance to use
	 * @param scale the {@link Vector2F} instance to use as the scale factor
	 * @throws NullPointerException thrown if, and only if, either {@code image}, {@code angle} or {@code scale} are {@code null}
	 */
	public ImageLight(final ImageF image, final AngleF angle, final Vector2F scale) {
		super(new Transform(new Point3F(), Quaternion4F.from(Matrix44F.rotate(new OrthonormalBasis33F(Vector3F.y(), Vector3F.x())))), 1, false);
		
		this.image = Objects.requireNonNull(image, "image == null").copy();
		this.angle = Objects.requireNonNull(angle, "angle == null");
		this.scale = Objects.requireNonNull(scale, "scale == null");
		this.distribution = doCreateDistribution();
		this.radius = 10.0F;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link AngleF} instance to use.
	 * 
	 * @return the {@code AngleF} instance to use
	 */
	public AngleF getAngle() {
		return this.angle;
	}
	
	/**
	 * Returns a {@link Color3F} instance with the radiance emitted along {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @return a {@code Color3F} instance with the radiance emitted along {@code ray}
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Color3F evaluateRadianceEmitted(final Ray3F ray) {
		Objects.requireNonNull(ray, "ray == null");
		
		final Vector3F incomingWorldSpace = ray.getDirection();
		final Vector3F incomingObjectSpace = doTransformToObjectSpace(incomingWorldSpace);
		
		final Color3F result = doRadianceSky(incomingObjectSpace);
		
		return result;
	}
	
	/**
	 * Returns the {@link Distribution2F} instance associated with this {@code ImageLight} instance.
	 * 
	 * @return the {@code Distribution2F} instance associated with this {@code ImageLight} instance
	 */
	public Distribution2F getDistribution() {
		return this.distribution;
	}
	
	/**
	 * Returns a copy of the {@link ImageF} instance used by this {@code ImageLight} instance.
	 * 
	 * @return a copy of the {@code ImageF} instance used by this {@code ImageLight} instance
	 */
	public ImageF getImage() {
		return this.image.copy();
	}
	
	/**
	 * Samples the incoming radiance.
	 * <p>
	 * Returns an optional {@link LightSample} with the result of the sampling.
	 * <p>
	 * If either {@code intersection} or {@code sample} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param sample a {@link Point2F} instance
	 * @return an optional {@code LightSample} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code sample} are {@code null}
	 */
	@Override
	public Optional<LightSample> sampleRadianceIncoming(final Intersection intersection, final Point2F sample) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(sample, "sample == null");
		
		final Sample2F sampleRemapped = this.distribution.continuousRemap(new Sample2F(sample.x, sample.y));
		
		final float probabilityDensityFunctionValueRemapped = this.distribution.continuousProbabilityDensityFunction(sampleRemapped, true);
		
		if(isZero(probabilityDensityFunctionValueRemapped)) {
			return Optional.empty();
		}
		
		final Vector3F incomingObjectSpace = Vector3F.directionSpherical(sampleRemapped.getU(), sampleRemapped.getV());
		final Vector3F incomingWorldSpace = doTransformToWorldSpace(incomingObjectSpace);
		
		final float sinTheta = incomingObjectSpace.sinTheta();
		
		if(isZero(sinTheta)) {
			return Optional.empty();
		}
		
		final Color3F result = doRadianceSky(incomingObjectSpace);
		
		final Point3F point = Point3F.add(intersection.getSurfaceIntersectionPoint(), incomingWorldSpace, 2.0F * this.radius);
		
		final float probabilityDensityFunctionValue = probabilityDensityFunctionValueRemapped / (2.0F * PI * PI * sinTheta);
		
		return Optional.of(new LightSample(result, point, incomingWorldSpace, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code ImageLight} instance.
	 * 
	 * @return a {@code String} with the name of this {@code ImageLight} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ImageLight} instance.
	 * 
	 * @return a {@code String} representation of this {@code ImageLight} instance
	 */
	@Override
	public String toString() {
		return String.format("new ImageLight(%s, %s, %s)", this.image, this.angle, this.scale);
	}
	
	/**
	 * Returns the {@link Vector2F} instance to use as the scale factor.
	 * 
	 * @return the {@code Vector2F} instance to use as the scale factor
	 */
	public Vector2F getScale() {
		return this.scale;
	}
	
	/**
	 * Accepts a {@link NodeHierarchicalVisitor}.
	 * <p>
	 * Returns the result of {@code nodeHierarchicalVisitor.visitLeave(this)}.
	 * <p>
	 * If {@code nodeHierarchicalVisitor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}, a {@code NodeTraversalException} will be thrown with the {@code RuntimeException} wrapped.
	 * <p>
	 * This implementation will:
	 * <ul>
	 * <li>throw a {@code NullPointerException} if {@code nodeHierarchicalVisitor} is {@code null}.</li>
	 * <li>throw a {@code NodeTraversalException} if {@code nodeHierarchicalVisitor} throws a {@code RuntimeException}.</li>
	 * <li>traverse its child {@code Node} instances.</li>
	 * </ul>
	 * 
	 * @param nodeHierarchicalVisitor the {@code NodeHierarchicalVisitor} to accept
	 * @return the result of {@code nodeHierarchicalVisitor.visitLeave(this)}
	 * @throws NodeTraversalException thrown if, and only if, a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}
	 * @throws NullPointerException thrown if, and only if, {@code nodeHierarchicalVisitor} is {@code null}
	 */
	@Override
	public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
		Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
		
		try {
			if(nodeHierarchicalVisitor.visitEnter(this)) {
				if(!this.scale.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code ImageLight} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ImageLight}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ImageLight} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ImageLight}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ImageLight)) {
			return false;
		} else if(!Objects.equals(getTransform(), ImageLight.class.cast(object).getTransform())) {
			return false;
		} else if(!Objects.equals(this.angle, ImageLight.class.cast(object).angle)) {
			return false;
		} else if(!Objects.equals(this.distribution, ImageLight.class.cast(object).distribution)) {
			return false;
		} else if(!Objects.equals(this.image, ImageLight.class.cast(object).image)) {
			return false;
		} else if(!Objects.equals(this.scale, ImageLight.class.cast(object).scale)) {
			return false;
		} else if(!equal(this.radius, ImageLight.class.cast(object).radius)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Evaluates the probability density function (PDF) for the incoming radiance.
	 * <p>
	 * Returns a {@code float} with the probability density function (PDF) value.
	 * <p>
	 * If either {@code intersection} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param incoming the incoming direction
	 * @return a {@code float} with the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code incoming} are {@code null}
	 */
	@Override
	public float evaluateProbabilityDensityFunctionRadianceIncoming(final Intersection intersection, final Vector3F incoming) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		final Vector3F incomingObjectSpace = doTransformToObjectSpace(incoming);
		
		final float sinTheta = sin(incomingObjectSpace.sphericalTheta());
		
		if(isZero(sinTheta)) {
			return 0.0F;
		}
		
		final Point2F sphericalCoordinates = Point2F.sphericalCoordinates(incomingObjectSpace);
		
		final Sample2F sample = new Sample2F(sphericalCoordinates.x, sphericalCoordinates.y);
		
		final float probabilityDensityFunctionValue = this.distribution.continuousProbabilityDensityFunction(sample, true) / (2.0F * PI * PI * sinTheta);
		
		return probabilityDensityFunctionValue;
	}
	
	/**
	 * Returns the radius associated with this {@code ImageLight} instance.
	 * 
	 * @return the radius associated with this {@code ImageLight} instance
	 */
	public float getRadius() {
		return this.radius;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code ImageLight} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code ImageLight} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code ImageLight} instance.
	 * 
	 * @return a hash code for this {@code ImageLight} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getTransform(), this.angle, this.distribution, this.image, this.scale, Float.valueOf(this.radius));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Loads an {@code ImageLight} from the file represented by {@code file}.
	 * <p>
	 * Returns a new {@code ImageLight} instance.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * ImageLight.load(file, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} that represents the file to load from
	 * @return a new {@code ImageLight} instance
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static ImageLight load(final File file) {
		return load(file, AngleF.degrees(0.0F));
	}
	
	/**
	 * Loads an {@code ImageLight} from the file represented by {@code file}.
	 * <p>
	 * Returns a new {@code ImageLight} instance.
	 * <p>
	 * If either {@code file} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * ImageLight.load(file, angle, new Vector2F(1.0F, 1.0F));
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} that represents the file to load from
	 * @param angle the {@link AngleF} instance to use
	 * @return a new {@code ImageLight} instance
	 * @throws NullPointerException thrown if, and only if, either {@code file} or {@code angle} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static ImageLight load(final File file, final AngleF angle) {
		return load(file, angle, new Vector2F(1.0F, 1.0F));
	}
	
	/**
	 * Loads an {@code ImageLight} from the file represented by {@code file}.
	 * <p>
	 * Returns a new {@code ImageLight} instance.
	 * <p>
	 * If either {@code file}, {@code angle} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} that represents the file to load from
	 * @param angle the {@link AngleF} instance to use
	 * @param scale the {@link Vector2F} instance to use as the scale factor
	 * @return a new {@code ImageLight} instance
	 * @throws NullPointerException thrown if, and only if, either {@code file}, {@code angle} or {@code scale} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static ImageLight load(final File file, final AngleF angle, final Vector2F scale) {
		return new ImageLight(FloatImageF.load(Objects.requireNonNull(file, "file == null")), angle, scale);
	}
	
	/**
	 * Loads an {@code ImageLight} from the file represented by {@code pathname}.
	 * <p>
	 * Returns a new {@code ImageLight} instance.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * ImageLight.load(pathname, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} that represents the pathname to the file to load from
	 * @return a new {@code ImageLight} instance
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static ImageLight load(final String pathname) {
		return load(pathname, AngleF.degrees(0.0F));
	}
	
	/**
	 * Loads an {@code ImageLight} from the file represented by {@code pathname}.
	 * <p>
	 * Returns a new {@code ImageLight} instance.
	 * <p>
	 * If either {@code pathname} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * ImageLight.load(pathname, angle, new Vector2F(1.0F, 1.0F));
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} that represents the pathname to the file to load from
	 * @param angle the {@link AngleF} instance to use
	 * @return a new {@code ImageLight} instance
	 * @throws NullPointerException thrown if, and only if, either {@code pathname} or {@code angle} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static ImageLight load(final String pathname, final AngleF angle) {
		return load(pathname, angle, new Vector2F(1.0F, 1.0F));
	}
	
	/**
	 * Loads an {@code ImageLight} from the file represented by {@code pathname}.
	 * <p>
	 * Returns a new {@code ImageLight} instance.
	 * <p>
	 * If either {@code pathname}, {@code angle} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param pathname a {@code String} that represents the pathname to the file to load from
	 * @param angle the {@link AngleF} instance to use
	 * @param scale the {@link Vector2F} instance to use as the scale factor
	 * @return a new {@code ImageLight} instance
	 * @throws NullPointerException thrown if, and only if, either {@code pathname}, {@code angle} or {@code scale} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static ImageLight load(final String pathname, final AngleF angle, final Vector2F scale) {
		return load(new File(Objects.requireNonNull(pathname, "pathname == null")), angle, scale);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Color3F doRadianceSky(final Vector3F direction) {
		final Point2F textureCoordinates = Point2F.sphericalCoordinates(direction);
		final Point2F textureCoordinatesRotated = Point2F.rotateCounterclockwise(textureCoordinates, this.angle);
		final Point2F textureCoordinatesScaled = Point2F.scale(textureCoordinatesRotated, this.scale);
		final Point2F textureCoordinatesImage = Point2F.toImage(textureCoordinatesScaled, this.image.getResolutionX(), this.image.getResolutionY());
		
		return this.image.getColorRGB(textureCoordinatesImage.x, textureCoordinatesImage.y, PixelOperation.WRAP_AROUND);
	}
	
	private Distribution2F doCreateDistribution() {
		final int resolutionU = 32;
		final int resolutionV = 32;
		
		final float resolutionUReciprocal = 1.0F / resolutionU;
		final float resolutionVReciprocal = 1.0F / resolutionV;
		
		final float[][] functions = new float[resolutionU][resolutionV];
		
		for(int u = 0; u < resolutionU; u++) {
			final float sphericalU = (u + 0.5F) * resolutionUReciprocal;
			
			for(int v = 0; v < resolutionV; v++) {
				final float sphericalV = (v + 0.5F) * resolutionVReciprocal;
				final float sinTheta = sin(PI * sphericalV);
				
				final Color3F colorRGB = doRadianceSky(Vector3F.directionSpherical(sphericalU, sphericalV));
				
				functions[u][v] = colorRGB.relativeLuminance() * sinTheta;
			}
		}
		
		return new Distribution2F(functions, true);
	}
	
	private Vector3F doTransformToObjectSpace(final Vector3F vector) {
		return Vector3F.normalize(Vector3F.transform(getTransform().getWorldToObject(), vector));
	}
	
	private Vector3F doTransformToWorldSpace(final Vector3F vector) {
		return Vector3F.normalize(Vector3F.transform(getTransform().getObjectToWorld(), vector));
	}
}