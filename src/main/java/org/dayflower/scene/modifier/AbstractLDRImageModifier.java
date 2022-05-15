/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
package org.dayflower.scene.modifier;

import static org.dayflower.utility.Floats.ceil;
import static org.dayflower.utility.Floats.floor;
import static org.dayflower.utility.Ints.positiveModulo;
import static org.dayflower.utility.Ints.toInt;

import java.util.Objects;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.image.ImageF;
import org.dayflower.utility.ParameterArguments;

/**
 * An {@code AbstractLDRImageModifier} is an abstract {@link Modifier} implementation that contains a low-dynamic-range (LDR) image.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code AbstractLDRImageModifier} class stores the image as an {@code int[]} with the colors in packed form and in the order ARGB. It is, however, possible to create an {@code AbstractLDRImageModifier} instance from an {@link ImageF} instance.
 * This is useful if the requirement is to generate an image procedurally or load it from disk.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractLDRImageModifier implements Modifier {
	private final AngleF angle;
	private final Vector2F scale;
	private final int resolution;
	private final int resolutionX;
	private final int resolutionY;
	private final int[] image;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractLDRImageModifier} instance.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * super(image, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param image an {@link ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	protected AbstractLDRImageModifier(final ImageF image) {
		this(image, AngleF.degrees(0.0F));
	}
	
	/**
	 * Constructs a new {@code AbstractLDRImageModifier} instance.
	 * <p>
	 * If either {@code image} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * super(image, angle, new Vector2F(1.0F, 1.0F));
	 * }
	 * </pre>
	 * 
	 * @param image an {@link ImageF} instance
	 * @param angle the {@link AngleF} instance to use
	 * @throws NullPointerException thrown if, and only if, either {@code image} or {@code angle} are {@code null}
	 */
	protected AbstractLDRImageModifier(final ImageF image, final AngleF angle) {
		this(image, angle, new Vector2F(1.0F, 1.0F));
	}
	
	/**
	 * Constructs a new {@code AbstractLDRImageModifier} instance.
	 * <p>
	 * If either {@code image}, {@code angle} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * super(image.getResolutionX(), image.getResolutionY(), image.toIntArrayPackedForm(), angle, scale);
	 * }
	 * </pre>
	 * 
	 * @param image an {@link ImageF} instance
	 * @param angle the {@link AngleF} instance to use
	 * @param scale the {@link Vector2F} instance to use as the scale factor
	 * @throws NullPointerException thrown if, and only if, either {@code image}, {@code angle} or {@code scale} are {@code null}
	 */
	protected AbstractLDRImageModifier(final ImageF image, final AngleF angle, final Vector2F scale) {
		this(image.getResolutionX(), image.getResolutionY(), image.toIntArrayPackedForm(), angle, scale);
	}
	
	/**
	 * Constructs a new {@code AbstractLDRImageModifier} instance.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * super(resolutionX, resolutionY, image, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param image the image to clone and use
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	protected AbstractLDRImageModifier(final int resolutionX, final int resolutionY, final int[] image) {
		this(resolutionX, resolutionY, image, AngleF.degrees(0.0F));
	}
	
	/**
	 * Constructs a new {@code AbstractLDRImageModifier} instance.
	 * <p>
	 * If either {@code image} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != image.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * super(resolutionX, resolutionY, image, angle, new Vector2F(1.0F, 1.0F));
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
	protected AbstractLDRImageModifier(final int resolutionX, final int resolutionY, final int[] image, final AngleF angle) {
		this(resolutionX, resolutionY, image, angle, new Vector2F(1.0F, 1.0F));
	}
	
	/**
	 * Constructs a new {@code AbstractLDRImageModifier} instance.
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
	protected AbstractLDRImageModifier(final int resolutionX, final int resolutionY, final int[] image, final AngleF angle, final Vector2F scale) {
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
	public final AngleF getAngle() {
		return this.angle;
	}
	
	/**
	 * Returns the {@link Vector2F} instance to use as the scale factor.
	 * 
	 * @return the {@code Vector2F} instance to use as the scale factor
	 */
	public final Vector2F getScale() {
		return this.scale;
	}
	
	/**
	 * Returns the resolution.
	 * 
	 * @return the resolution
	 */
	public final int getResolution() {
		return this.resolution;
	}
	
	/**
	 * Returns the resolution of the X-axis.
	 * 
	 * @return the resolution of the X-axis
	 */
	public final int getResolutionX() {
		return this.resolutionX;
	}
	
	/**
	 * Returns the resolution of the Y-axis.
	 * 
	 * @return the resolution of the Y-axis
	 */
	public final int getResolutionY() {
		return this.resolutionY;
	}
	
	/**
	 * Returns an {@code int[]} containing the image with its colors in packed form using the order ARGB.
	 * <p>
	 * Modifying the returned {@code int[]} will not affect this {@code AbstractLDRImageModifier} instance.
	 * 
	 * @return an {@code int[]} containing the image with its colors in packed form using the order ARGB
	 */
	public final int[] getImage() {
		return this.image.clone();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the color at {@code point}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * abstractLDRImageModifier.getColorRGB(point, false);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2F} instance
	 * @return a {@code Color3F} instance with the color at {@code point}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	protected final Color3F getColorRGB(final Point2F point) {
		return getColorRGB(point, false);
	}
	
	/**
	 * Returns a {@link Color3F} instance with the color at {@code point}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point2F} instance
	 * @param isUsingBilinearInterpolation {@code true} if, and only if, bilinear interpolation should be used, {@code false} otherwise
	 * @return a {@code Color3F} instance with the color at {@code point}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	protected final Color3F getColorRGB(final Point2F point, final boolean isUsingBilinearInterpolation) {
		final Point2F pointRotated = Point2F.rotateCounterclockwise(point, this.angle);
		final Point2F pointScaled = Point2F.scale(pointRotated, this.scale);
		final Point2F pointImage = Point2F.toImage(pointScaled, this.resolutionX, this.resolutionY);
		
		if(isUsingBilinearInterpolation) {
			return doGetColorRGBBilinearInterpolation(pointImage.x, pointImage.y);
		}
		
		return doGetColorRGB(toInt(pointImage.x), toInt(pointImage.y));
	}
	
	/**
	 * Returns a {@link Color3F} instance with the color at {@code x} and {@code y}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * abstractLDRImageModifier.getColorRGB(x, y, false);
	 * }
	 * </pre>
	 * 
	 * @param x the X-coordinate of the point
	 * @param y the Y-coordinate of the point
	 * @return a {@code Color3F} instance with the color at {@code x} and {@code y}
	 */
	protected final Color3F getColorRGB(final float x, final float y) {
		return getColorRGB(x, y, false);
	}
	
	/**
	 * Returns a {@link Color3F} instance with the color at {@code x} and {@code y}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * abstractLDRImageModifier.getColorRGB(new Point2F(x, y), isUsingBilinearInterpolation);
	 * }
	 * </pre>
	 * 
	 * @param x the X-coordinate of the point
	 * @param y the Y-coordinate of the point
	 * @param isUsingBilinearInterpolation {@code true} if, and only if, bilinear interpolation should be used, {@code false} otherwise
	 * @return a {@code Color3F} instance with the color at {@code x} and {@code y}
	 */
	protected final Color3F getColorRGB(final float x, final float y, final boolean isUsingBilinearInterpolation) {
		return getColorRGB(new Point2F(x, y), isUsingBilinearInterpolation);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Color3F doGetColorRGB(final int x, final int y) {
		return Color3F.unpack(this.image[positiveModulo(y, this.resolutionY) * this.resolutionX + positiveModulo(x, this.resolutionX)]);
	}
	
	private Color3F doGetColorRGBBilinearInterpolation(final float x, final float y) {
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
		
		return Color3F.blend(Color3F.blend(color00, color01, xFactor), Color3F.blend(color10, color11, xFactor), yFactor);
	}
}