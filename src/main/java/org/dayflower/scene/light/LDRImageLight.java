/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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

import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.utility.Floats.asinpi;
import static org.dayflower.utility.Floats.atan2;
import static org.dayflower.utility.Floats.ceil;
import static org.dayflower.utility.Floats.cos;
import static org.dayflower.utility.Floats.floor;
import static org.dayflower.utility.Floats.positiveModulo;
import static org.dayflower.utility.Floats.sin;
import static org.dayflower.utility.Ints.padding;
import static org.dayflower.utility.Ints.positiveModulo;
import static org.dayflower.utility.Ints.toInt;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.ImageF;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.LightRadianceEmittedResult;
import org.dayflower.scene.LightRadianceIncomingResult;
import org.dayflower.utility.BufferedImages;
import org.dayflower.utility.ParameterArguments;

/**
 * An {@code LDRImageLight} is a {@link Light} implementation backed by a low-dynamic-range (LDR) image.
 * <p>
 * This class is mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class LDRImageLight implements Light {
	/**
	 * The name of this {@code LDRImageLight} class.
	 */
	public static final String NAME = "LDR Image Light";
	
	/**
	 * The offset for the angle in radians in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_ANGLE_RADIANS = 0;
	
	/**
	 * The offset for the image in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_IMAGE = 5;
	
	/**
	 * The offset for the resolution of the X-axis in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_RESOLUTION_X = 3;
	
	/**
	 * The offset for the resolution of the Y-axis in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_RESOLUTION_Y = 4;
	
	/**
	 * The offset for the {@link Vector2F} instance representing the scale in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_SCALE = 1;
	
	/**
	 * The ID of this {@code LDRImageLight} class.
	 */
	public static final int ID = 4;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AngleF angle;
	private final Vector2F scale;
	private final int resolution;
	private final int resolutionX;
	private final int resolutionY;
	private final int[] image;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code LDRImageLight} instance.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new LDRImageLight(image, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param image an {@link ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	public LDRImageLight(final ImageF image) {
		this(image, AngleF.degrees(0.0F));
	}
	
	/**
	 * Constructs a new {@code LDRImageLight} instance.
	 * <p>
	 * If either {@code image} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new LDRImageLight(image, angle, new Vector2F(1.0F, 1.0F));
	 * }
	 * </pre>
	 * 
	 * @param image an {@link ImageF} instance
	 * @param angle the {@link AngleF} instance to use
	 * @throws NullPointerException thrown if, and only if, either {@code image} or {@code angle} are {@code null}
	 */
	public LDRImageLight(final ImageF image, final AngleF angle) {
		this(image, angle, new Vector2F(1.0F, 1.0F));
	}
	
	/**
	 * Constructs a new {@code LDRImageLight} instance.
	 * <p>
	 * If either {@code image}, {@code angle} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new LDRImageLight(image.getResolutionX(), image.getResolutionY(), image.toIntArrayPackedForm(), angle, scale);
	 * }
	 * </pre>
	 * 
	 * @param image an {@link ImageF} instance
	 * @param angle the {@link AngleF} instance to use
	 * @param scale the {@link Vector2F} instance to use as the scale factor
	 * @throws NullPointerException thrown if, and only if, either {@code image}, {@code angle} or {@code scale} are {@code null}
	 */
	public LDRImageLight(final ImageF image, final AngleF angle, final Vector2F scale) {
		this(image.getResolutionX(), image.getResolutionY(), image.toIntArrayPackedForm(), angle, scale);
	}
	
	/**
	 * Constructs a new {@code LDRImageLight} instance.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new LDRImageLight(resolutionX, resolutionY, image, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param image the image to clone and use
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	public LDRImageLight(final int resolutionX, final int resolutionY, final int[] image) {
		this(resolutionX, resolutionY, image, AngleF.degrees(0.0F));
	}
	
	/**
	 * Constructs a new {@code LDRImageLight} instance.
	 * <p>
	 * If either {@code image} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new LDRImageLight(resolutionX, resolutionY, image, angle, new Vector2F(1.0F, 1.0F));
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param image the image to clone and use
	 * @param angle the {@link AngleF} instance to use
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}
	 * @throws NullPointerException thrown if, and only if, either {@code image} or {@code angle} are {@code null}
	 */
	public LDRImageLight(final int resolutionX, final int resolutionY, final int[] image, final AngleF angle) {
		this(resolutionX, resolutionY, image, angle, new Vector2F(1.0F, 1.0F));
	}
	
	/**
	 * Constructs a new {@code LDRImageLight} instance.
	 * <p>
	 * If either {@code image}, {@code angle} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param image the image to clone and use
	 * @param angle the {@link AngleF} instance to use
	 * @param scale the {@link Vector2F} instance to use as the scale factor
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}
	 * @throws NullPointerException thrown if, and only if, either {@code image}, {@code angle} or {@code scale} are {@code null}
	 */
	public LDRImageLight(final int resolutionX, final int resolutionY, final int[] image, final AngleF angle, final Vector2F scale) {
		this.resolutionX = ParameterArguments.requireRange(resolutionX, 0, Integer.MAX_VALUE, "resolutionX");
		this.resolutionY = ParameterArguments.requireRange(resolutionY, 0, Integer.MAX_VALUE, "resolutionY");
		this.resolution = ParameterArguments.requireRange(resolutionX * resolutionY, 0, Integer.MAX_VALUE, "resolutionX * resolutionY");
		this.image = ParameterArguments.requireExactArrayLength(Objects.requireNonNull(image, "image == null"), resolutionX * resolutionY, "image").clone();
		this.angle = Objects.requireNonNull(angle, "angle == null");
		this.scale = Objects.requireNonNull(scale, "scale == null");
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
	 * Returns a {@link Color3F} instance with the emitted radiance for {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code Light} method {@code Le(const RayDifferential &r)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @return a {@code Color3F} instance with the emitted radiance for {@code ray}
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Color3F evaluateRadianceEmitted(final Ray3F ray) {
		final AngleF angle = this.angle;
		
		final Point2F textureCoordinates = new Point2F(0.5F + atan2(ray.getDirection().getZ(), ray.getDirection().getX()) * PI_MULTIPLIED_BY_2_RECIPROCAL, 0.5F - asinpi(ray.getDirection().getY()));
		
		final Vector2F scale = this.scale;
		
		final float angleRadians = angle.getRadians();
		final float angleRadiansCos = cos(angleRadians);
		final float angleRadiansSin = sin(angleRadians);
		
		final float resolutionX = this.resolutionX;
		final float resolutionY = this.resolutionY;
		
		final float scaleU = scale.getU();
		final float scaleV = scale.getV();
		
		final float textureCoordinatesU = textureCoordinates.getU();
		final float textureCoordinatesV = textureCoordinates.getV();
		
		final float textureCoordinatesRotatedU = textureCoordinatesU * angleRadiansCos - textureCoordinatesV * angleRadiansSin;
		final float textureCoordinatesRotatedV = textureCoordinatesV * angleRadiansCos + textureCoordinatesU * angleRadiansSin;
		
		final float textureCoordinatesScaledU = textureCoordinatesRotatedU * scaleU * resolutionX - 0.5F;
		final float textureCoordinatesScaledV = textureCoordinatesRotatedV * scaleV * resolutionY - 0.5F;
		
		final float x = positiveModulo(textureCoordinatesScaledU, resolutionX);
		final float y = positiveModulo(textureCoordinatesScaledV, resolutionY);
		
		return doGetColorRGB(x, y);
	}
	
	/**
	 * Returns a {@link Color3F} instance with the power of this {@code LDRImageLight} instance.
	 * <p>
	 * This method represents the {@code Light} method {@code Power()} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @return a {@code Color3F} instance with the power of this {@code LDRImageLight} instance
	 */
	@Override
	public Color3F power() {
		return Color3F.BLACK;//TODO: Implement!
	}
	
	/**
	 * Evaluates the probability density functions (PDFs) for emitted radiance.
	 * <p>
	 * Returns an optional {@link LightRadianceEmittedResult} with the result of the evaluation.
	 * <p>
	 * If either {@code ray} or {@code normal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code Light} method {@code Pdf_Le(const Ray &ray, const Normal3f &nLight, Float *pdfPos, Float *pdfDir)} in PBRT.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @param normal a {@link Vector3F} instance
	 * @return an optional {@code LightRadianceEmittedResult} with the result of the evaluation
	 * @throws NullPointerException thrown if, and only if, either {@code ray} or {@code normal} are {@code null}
	 */
	@Override
	public Optional<LightRadianceEmittedResult> evaluateProbabilityDensityFunctionRadianceEmitted(final Ray3F ray, final Vector3F normal) {
		Objects.requireNonNull(ray, "ray == null");
		Objects.requireNonNull(normal, "normal == null");
		
		return Optional.empty();//TODO: Implement!
	}
	
	/**
	 * Samples the emitted radiance.
	 * <p>
	 * Returns an optional {@link LightRadianceEmittedResult} with the result of the sampling.
	 * <p>
	 * If either {@code sampleA} or {@code sampleB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code Light} method {@code Sample_Le(const Point2f &u1, const Point2f &u2, Float time, Ray *ray, Normal3f *nLight, Float *pdfPos, Float *pdfDir)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param sampleA a {@link Point2F} instance
	 * @param sampleB a {@code Point2F} instance
	 * @return an optional {@code LightRadianceEmittedResult} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code sampleA} or {@code sampleB} are {@code null}
	 */
	@Override
	public Optional<LightRadianceEmittedResult> sampleRadianceEmitted(final Point2F sampleA, final Point2F sampleB) {
		Objects.requireNonNull(sampleA, "sampleA == null");
		Objects.requireNonNull(sampleB, "sampleB == null");
		
		return Optional.empty();//TODO: Implement!
	}
	
	/**
	 * Samples the incoming radiance.
	 * <p>
	 * Returns an optional {@link LightRadianceIncomingResult} with the result of the sampling.
	 * <p>
	 * If either {@code intersection} or {@code sample} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code Light} method {@code Sample_Li(const Interaction &ref, const Point2f &u, Vector3f *wi, Float *pdf, VisibilityTester *vis)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param sample a {@link Point2F} instance
	 * @return an optional {@code LightRadianceIncomingResult} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code sample} are {@code null}
	 */
	@Override
	public Optional<LightRadianceIncomingResult> sampleRadianceIncoming(final Intersection intersection, final Point2F sample) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(sample, "sample == null");
		
		return Optional.empty();//TODO: Implement!
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code LDRImageLight} instance.
	 * 
	 * @return a {@code String} with the name of this {@code LDRImageLight} instance
	 */
	@SuppressWarnings("static-method")
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code LDRImageLight} instance.
	 * 
	 * @return a {@code String} representation of this {@code LDRImageLight} instance
	 */
	@Override
	public String toString() {
		return String.format("new LDRImageLight(%d, %d, %s, %s, %s)", Integer.valueOf(this.resolutionX), Integer.valueOf(this.resolutionY), "new int[] {...}", this.angle, this.scale);
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
	 * Compares {@code object} to this {@code LDRImageLight} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code LDRImageLight}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code LDRImageLight} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code LDRImageLight}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof LDRImageLight)) {
			return false;
		} else if(!Objects.equals(this.angle, LDRImageLight.class.cast(object).angle)) {
			return false;
		} else if(!Objects.equals(this.scale, LDRImageLight.class.cast(object).scale)) {
			return false;
		} else if(this.resolution != LDRImageLight.class.cast(object).resolution) {
			return false;
		} else if(this.resolutionX != LDRImageLight.class.cast(object).resolutionX) {
			return false;
		} else if(this.resolutionY != LDRImageLight.class.cast(object).resolutionY) {
			return false;
		} else if(!Arrays.equals(this.image, LDRImageLight.class.cast(object).image)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code LDRImageLight} instance uses a delta distribution, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code LDRImageLight} instance uses a delta distribution, {@code false} otherwise
	 */
	@Override
	public boolean isDeltaDistribution() {
		return false;
	}
	
	/**
	 * Evaluates the probability density function (PDF) for incoming radiance.
	 * <p>
	 * Returns a {@code float} with the probability density function (PDF) value.
	 * <p>
	 * If either {@code intersection} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code Light} method {@code Pdf_Li(const Interaction &ref, const Vector3f &wi)} that returns a {@code Float} in PBRT.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param incoming the incoming direction, called {@code wi} in PBRT
	 * @return a {@code float} with the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code incoming} are {@code null}
	 */
	@Override
	public float evaluateProbabilityDensityFunctionRadianceIncoming(final Intersection intersection, final Vector3F incoming) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		return 0.0F;//TODO: Implement!
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code LDRImageLight} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code LDRImageLight} instance
	 */
	public float[] toArray() {
		final float[] array = new float[getArrayLength()];
		
		array[ARRAY_OFFSET_ANGLE_RADIANS] = this.angle.getRadians();
		array[ARRAY_OFFSET_SCALE + 0] = this.scale.getU();
		array[ARRAY_OFFSET_SCALE + 1] = this.scale.getV();
		array[ARRAY_OFFSET_RESOLUTION_X] = this.resolutionX;
		array[ARRAY_OFFSET_RESOLUTION_Y] = this.resolutionY;
		
		for(int i = 0; i < this.image.length; i++) {
			array[ARRAY_OFFSET_IMAGE + i] = this.image[i];
		}
		
		for(int i = ARRAY_OFFSET_IMAGE + this.image.length; i < array.length; i++) {
			array[i] = 0.0F;
		}
		
		return array;
	}
	
	/**
	 * Returns the length of the {@code float[]}.
	 * 
	 * @return the length of the {@code float[]}
	 */
	public int getArrayLength() {
		return 5 + this.image.length + padding(5 + this.image.length);
	}
	
	/**
	 * Returns the resolution.
	 * 
	 * @return the resolution
	 */
	public int getResolution() {
		return this.resolution;
	}
	
	/**
	 * Returns the resolution of the X-axis.
	 * 
	 * @return the resolution of the X-axis
	 */
	public int getResolutionX() {
		return this.resolutionX;
	}
	
	/**
	 * Returns the resolution of the Y-axis.
	 * 
	 * @return the resolution of the Y-axis
	 */
	public int getResolutionY() {
		return this.resolutionY;
	}
	
	/**
	 * Returns the sample count associated with this {@code LDRImageLight} instance.
	 * 
	 * @return the sample count associated with this {@code LDRImageLight} instance
	 */
	@Override
	public int getSampleCount() {
		return 0;
	}
	
	/**
	 * Returns a hash code for this {@code LDRImageLight} instance.
	 * 
	 * @return a hash code for this {@code LDRImageLight} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.angle, this.scale, Integer.valueOf(this.resolution), Integer.valueOf(this.resolutionX), Integer.valueOf(this.resolutionY), Integer.valueOf(Arrays.hashCode(this.image)));
	}
	
	/**
	 * Returns an {@code int[]} containing the image with its colors in packed form using the order ARGB.
	 * <p>
	 * Modifying the returned {@code int[]} will not affect this {@code LDRImageLight} instance.
	 * 
	 * @return an {@code int[]} containing the image with its colors in packed form using the order ARGB
	 */
	public int[] getImage() {
		return this.image.clone();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Loads an {@code LDRImageLight} from the file represented by {@code file}.
	 * <p>
	 * Returns a new {@code LDRImageLight} instance.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * LDRImageLight.load(file, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} that represents the file to load from
	 * @return a new {@code LDRImageLight} instance
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static LDRImageLight load(final File file) {
		return load(file, AngleF.degrees(0.0F));
	}
	
	/**
	 * Loads an {@code LDRImageLight} from the file represented by {@code file}.
	 * <p>
	 * Returns a new {@code LDRImageLight} instance.
	 * <p>
	 * If either {@code file} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * LDRImageLight.load(file, angle, new Vector2F(1.0F, 1.0F));
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} that represents the file to load from
	 * @param angle the {@link AngleF} instance to use
	 * @return a new {@code LDRImageLight} instance
	 * @throws NullPointerException thrown if, and only if, either {@code file} or {@code angle} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static LDRImageLight load(final File file, final AngleF angle) {
		return load(file, angle, new Vector2F(1.0F, 1.0F));
	}
	
	/**
	 * Loads an {@code LDRImageLight} from the file represented by {@code file}.
	 * <p>
	 * Returns a new {@code LDRImageLight} instance.
	 * <p>
	 * If either {@code file}, {@code angle} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} that represents the file to load from
	 * @param angle the {@link AngleF} instance to use
	 * @param scale the {@link Vector2F} instance to use as the scale factor
	 * @return a new {@code LDRImageLight} instance
	 * @throws NullPointerException thrown if, and only if, either {@code file}, {@code angle} or {@code scale} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static LDRImageLight load(final File file, final AngleF angle, final Vector2F scale) {
		try {
			final BufferedImage bufferedImage = BufferedImages.getCompatibleBufferedImage(ImageIO.read(Objects.requireNonNull(file, "file == null")));
			
			final int resolutionX = bufferedImage.getWidth();
			final int resolutionY = bufferedImage.getHeight();
			
			final int[] image = DataBufferInt.class.cast(bufferedImage.getRaster().getDataBuffer()).getData();
			
			return new LDRImageLight(resolutionX, resolutionY, image, angle, scale);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Loads an {@code LDRImageLight} from the file represented by {@code pathname}.
	 * <p>
	 * Returns a new {@code LDRImageLight} instance.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * LDRImageLight.load(pathname, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} that represents the pathname to the file to load from
	 * @return a new {@code LDRImageLight} instance
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static LDRImageLight load(final String pathname) {
		return load(pathname, AngleF.degrees(0.0F));
	}
	
	/**
	 * Loads an {@code LDRImageLight} from the file represented by {@code pathname}.
	 * <p>
	 * Returns a new {@code LDRImageLight} instance.
	 * <p>
	 * If either {@code pathname} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * LDRImageLight.load(pathname, angle, new Vector2F(1.0F, 1.0F));
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} that represents the pathname to the file to load from
	 * @param angle the {@link AngleF} instance to use
	 * @return a new {@code LDRImageLight} instance
	 * @throws NullPointerException thrown if, and only if, either {@code pathname} or {@code angle} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static LDRImageLight load(final String pathname, final AngleF angle) {
		return load(pathname, angle, new Vector2F(1.0F, 1.0F));
	}
	
	/**
	 * Loads an {@code LDRImageLight} from the file represented by {@code pathname}.
	 * <p>
	 * Returns a new {@code LDRImageLight} instance.
	 * <p>
	 * If either {@code pathname}, {@code angle} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param pathname a {@code String} that represents the pathname to the file to load from
	 * @param angle the {@link AngleF} instance to use
	 * @param scale the {@link Vector2F} instance to use as the scale factor
	 * @return a new {@code LDRImageLight} instance
	 * @throws NullPointerException thrown if, and only if, either {@code pathname}, {@code angle} or {@code scale} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static LDRImageLight load(final String pathname, final AngleF angle, final Vector2F scale) {
		return load(new File(Objects.requireNonNull(pathname, "pathname == null")), angle, scale);
	}
	
	/**
	 * Redoes gamma correction on {@code lDRImageLight} using sRGB.
	 * <p>
	 * Returns a new {@code LDRImageLight} instance.
	 * <p>
	 * If {@code lDRImageLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lDRImageLight an {@code LDRImageLight} instance
	 * @return a new {@code LDRImageLight} instance
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageLight} is {@code null}
	 */
	public static LDRImageLight redoGammaCorrectionSRGB(final LDRImageLight lDRImageLight) {
		final int[] image = new int[lDRImageLight.image.length];
		
		for(int i = 0; i < lDRImageLight.image.length; i++) {
			final Color3F colorA = Color3F.unpack(lDRImageLight.image[i]);
			final Color3F colorB = Color3F.redoGammaCorrectionSRGB(colorA);
			
			image[i] = colorB.pack();
		}
		
		return new LDRImageLight(lDRImageLight.resolutionX, lDRImageLight.resolutionY, image, lDRImageLight.angle, lDRImageLight.scale);
	}
	
	/**
	 * Undoes gamma correction on {@code lDRImageLight} using sRGB.
	 * <p>
	 * Returns a new {@code LDRImageLight} instance.
	 * <p>
	 * If {@code lDRImageLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lDRImageLight an {@code LDRImageLight} instance
	 * @return a new {@code LDRImageLight} instance
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageLight} is {@code null}
	 */
	public static LDRImageLight undoGammaCorrectionSRGB(final LDRImageLight lDRImageLight) {
		final int[] image = new int[lDRImageLight.image.length];
		
		for(int i = 0; i < lDRImageLight.image.length; i++) {
			final Color3F colorA = Color3F.unpack(lDRImageLight.image[i]);
			final Color3F colorB = Color3F.undoGammaCorrectionSRGB(colorA);
			
			image[i] = colorB.pack();
		}
		
		return new LDRImageLight(lDRImageLight.resolutionX, lDRImageLight.resolutionY, image, lDRImageLight.angle, lDRImageLight.scale);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Color3F doGetColorRGB(final float x, final float y) {
		final int minimumX = toInt(floor(x));
		final int maximumX = toInt(ceil(x));
		
		final int minimumY = toInt(floor(y));
		final int maximumY = toInt(ceil(y));
		
		if(minimumX == maximumX && minimumY == maximumY) {
			return doGetColorRGB(minimumX, minimumY);
		}
		
		final Color3F color00 = doGetColorRGB(minimumX, minimumY);
		final Color3F color01 = doGetColorRGB(maximumX, minimumY);
		final Color3F color10 = doGetColorRGB(minimumX, maximumY);
		final Color3F color11 = doGetColorRGB(maximumX, maximumY);
		
		final float xFactor = x - minimumX;
		final float yFactor = y - minimumY;
		
		final Color3F color = Color3F.blend(Color3F.blend(color00, color01, xFactor), Color3F.blend(color10, color11, xFactor), yFactor);
		
		return color;
	}
	
	private Color3F doGetColorRGB(final int x, final int y) {
		return Color3F.unpack(this.image[positiveModulo(y, this.resolutionY) * this.resolutionX + positiveModulo(x, this.resolutionX)]);
	}
}