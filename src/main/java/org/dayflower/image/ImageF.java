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
package org.dayflower.image;

import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.ceil;
import static org.dayflower.utility.Floats.floor;
import static org.dayflower.utility.Floats.min;
import static org.dayflower.utility.Floats.toFloat;
import static org.dayflower.utility.Ints.max;
import static org.dayflower.utility.Ints.min;
import static org.dayflower.utility.Ints.toInt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.dayflower.change.Change;
import org.dayflower.change.ChangeCombiner;
import org.dayflower.change.ChangeHistory;
import org.dayflower.color.Color3F;
import org.dayflower.color.Color4F;
import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.Vector2F;
import org.dayflower.geometry.rasterizer.Rasterizer2I;
import org.dayflower.geometry.shape.Circle2I;
import org.dayflower.geometry.shape.LineSegment2I;
import org.dayflower.geometry.shape.Polygon2I;
import org.dayflower.geometry.shape.Rectangle2F;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.geometry.shape.Triangle2I;
import org.dayflower.java.util.function.TriFunction;
import org.dayflower.noise.SimplexNoiseF;

/**
 * An {@code ImageF} is an extension of {@link Image} that adds additional methods that operates on {@code float}-based data types.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class ImageF extends Image {
	private final ChangeCombiner changeCombiner;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ImageF} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
	protected ImageF(final int resolutionX, final int resolutionY) {
		super(resolutionX, resolutionY);
		
		this.changeCombiner = new ChangeCombiner();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link Color3F} of the pixel represented by {@code point}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color3F} instances.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGB(point, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2F} instance that contains the coordinates of the pixel
	 * @return the {@code Color3F} of the pixel represented by {@code point}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public final Color3F getColorRGB(final Point2F point) {
		return getColorRGB(point, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color3F} of the pixel represented by {@code point}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color3F} instances.
	 * <p>
	 * If either {@code point} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGB(point.getX(), point.getY(), pixelOperation);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2F} instance that contains the coordinates of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color3F} of the pixel represented by {@code point}
	 * @throws NullPointerException thrown if, and only if, either {@code point} or {@code pixelOperation} are {@code null}
	 */
	public final Color3F getColorRGB(final Point2F point, final PixelOperation pixelOperation) {
		return getColorRGB(point.getX(), point.getY(), pixelOperation);
	}
	
	/**
	 * Returns the {@link Color3F} of the pixel represented by {@code point}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGB(point, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2I} instance that contains the coordinates of the pixel
	 * @return the {@code Color3F} of the pixel represented by {@code point}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public final Color3F getColorRGB(final Point2I point) {
		return getColorRGB(point, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color3F} of the pixel represented by {@code point}.
	 * <p>
	 * If either {@code point} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGB(point.getX(), point.getY(), pixelOperation);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2I} instance that contains the coordinates of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color3F} of the pixel represented by {@code point}
	 * @throws NullPointerException thrown if, and only if, either {@code point} or {@code pixelOperation} are {@code null}
	 */
	public final Color3F getColorRGB(final Point2I point, final PixelOperation pixelOperation) {
		return getColorRGB(point.getX(), point.getY(), pixelOperation);
	}
	
	/**
	 * Returns the {@link Color3F} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color3F} instances.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGB(x, y, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @return the {@code Color3F} of the pixel represented by {@code x} and {@code y}
	 */
	public final Color3F getColorRGB(final float x, final float y) {
		return getColorRGB(x, y, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color3F} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color3F} instances.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color3F} of the pixel represented by {@code x} and {@code y}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
	public final Color3F getColorRGB(final float x, final float y, final PixelOperation pixelOperation) {
		final int minimumX = toInt(floor(x));
		final int maximumX = toInt(ceil(x));
		
		final int minimumY = toInt(floor(y));
		final int maximumY = toInt(ceil(y));
		
		if(minimumX == maximumX && minimumY == maximumY) {
			return getColorRGB(minimumX, minimumY, pixelOperation);
		}
		
		final Color3F color00 = getColorRGB(minimumX, minimumY, pixelOperation);
		final Color3F color01 = getColorRGB(maximumX, minimumY, pixelOperation);
		final Color3F color10 = getColorRGB(minimumX, maximumY, pixelOperation);
		final Color3F color11 = getColorRGB(maximumX, maximumY, pixelOperation);
		
		final float xFactor = x - minimumX;
		final float yFactor = y - minimumY;
		
		final Color3F color = Color3F.blend(Color3F.blend(color00, color01, xFactor), Color3F.blend(color10, color11, xFactor), yFactor);
		
		return color;
	}
	
	/**
	 * Returns the {@link Color3F} of the pixel represented by {@code index}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGB(index, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param index the index of the pixel
	 * @return the {@code Color3F} of the pixel represented by {@code index}
	 */
	public final Color3F getColorRGB(final int index) {
		return getColorRGB(index, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color3F} of the pixel represented by {@code index}.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param index the index of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color3F} of the pixel represented by {@code index}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
	public final Color3F getColorRGB(final int index, final PixelOperation pixelOperation) {
		return new Color3F(getColorRGBA(index, pixelOperation));
	}
	
	/**
	 * Returns the {@link Color3F} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGB(x, y, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @return the {@code Color3F} of the pixel represented by {@code x} and {@code y}
	 */
	public final Color3F getColorRGB(final int x, final int y) {
		return getColorRGB(x, y, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color3F} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color3F} of the pixel represented by {@code x} and {@code y}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
	public final Color3F getColorRGB(final int x, final int y, final PixelOperation pixelOperation) {
		return new Color3F(getColorRGBA(x, y, pixelOperation));
	}
	
	/**
	 * Returns the {@link Color4F} of the pixel represented by {@code point}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color4F} instances.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGBA(point, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2F} instance with the coordinates of the pixel
	 * @return the {@code Color4F} of the pixel represented by {@code point}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public final Color4F getColorRGBA(final Point2F point) {
		return getColorRGBA(point, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color4F} of the pixel represented by {@code point}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color4F} instances.
	 * <p>
	 * If either {@code point} or {@code function} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGBA(point.getX(), point.getY(), function);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2F} instance with the coordinates of the pixel
	 * @param function a {@code Function} that returns a {@code Color4F} instance if any of the points are outside the bounds
	 * @return the {@code Color4F} of the pixel represented by {@code point}
	 * @throws NullPointerException thrown if, and only if, either {@code point} or {@code function} are {@code null}
	 */
	public final Color4F getColorRGBA(final Point2F point, final Function<Point2I, Color4F> function) {
		return getColorRGBA(point.getX(), point.getY(), function);
	}
	
	/**
	 * Returns the {@link Color4F} of the pixel represented by {@code point}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color4F} instances.
	 * <p>
	 * If either {@code point} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGBA(point.getX(), point.getY(), pixelOperation);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2F} instance with the coordinates of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color4F} of the pixel represented by {@code point}
	 * @throws NullPointerException thrown if, and only if, either {@code point} or {@code pixelOperation} are {@code null}
	 */
	public final Color4F getColorRGBA(final Point2F point, final PixelOperation pixelOperation) {
		return getColorRGBA(point.getX(), point.getY(), pixelOperation);
	}
	
	/**
	 * Returns the {@link Color4F} of the pixel represented by {@code point}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGBA(point, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2I} instance with the coordinates of the pixel
	 * @return the {@code Color4F} of the pixel represented by {@code point}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public final Color4F getColorRGBA(final Point2I point) {
		return getColorRGBA(point, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color4F} of the pixel represented by {@code point}.
	 * <p>
	 * If either {@code point} or {@code function} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGBA(point.getX(), point.getY(), function);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2I} instance with the coordinates of the pixel
	 * @param function a {@code Function} that returns a {@code Color4F} instance if any of the points are outside the bounds
	 * @return the {@code Color4F} of the pixel represented by {@code point}
	 * @throws NullPointerException thrown if, and only if, either {@code point} or {@code function} are {@code null}
	 */
	public final Color4F getColorRGBA(final Point2I point, final Function<Point2I, Color4F> function) {
		return getColorRGBA(point.getX(), point.getY(), function);
	}
	
	/**
	 * Returns the {@link Color4F} of the pixel represented by {@code point}.
	 * <p>
	 * If either {@code point} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGBA(point.getX(), point.getY(), pixelOperation);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2I} instance with the coordinates of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color4F} of the pixel represented by {@code point}
	 * @throws NullPointerException thrown if, and only if, either {@code point} or {@code pixelOperation} are {@code null}
	 */
	public final Color4F getColorRGBA(final Point2I point, final PixelOperation pixelOperation) {
		return getColorRGBA(point.getX(), point.getY(), pixelOperation);
	}
	
	/**
	 * Returns the {@link Color4F} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color4F} instances.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGBA(x, y, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @return the {@code Color4F} of the pixel represented by {@code x} and {@code y}
	 */
	public final Color4F getColorRGBA(final float x, final float y) {
		return getColorRGBA(x, y, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color4F} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color4F} instances.
	 * <p>
	 * If {@code function} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param function a {@code Function} that returns a {@code Color4F} instance if {@code x} or {@code y} are outside the bounds
	 * @return the {@code Color4F} of the pixel represented by {@code x} and {@code y}
	 * @throws NullPointerException thrown if, and only if, {@code function} is {@code null}
	 */
	public final Color4F getColorRGBA(final float x, final float y, final Function<Point2I, Color4F> function) {
		Objects.requireNonNull(function, "function == null");
		
		final int minimumX = toInt(floor(x));
		final int maximumX = toInt(ceil(x));
		
		final int minimumY = toInt(floor(y));
		final int maximumY = toInt(ceil(y));
		
		if(minimumX == maximumX && minimumY == maximumY) {
			return getColorRGBA(minimumX, minimumY, function);
		}
		
		final Color4F color00 = getColorRGBA(minimumX, minimumY, function);
		final Color4F color01 = getColorRGBA(maximumX, minimumY, function);
		final Color4F color10 = getColorRGBA(minimumX, maximumY, function);
		final Color4F color11 = getColorRGBA(maximumX, maximumY, function);
		
		final float xFactor = x - minimumX;
		final float yFactor = y - minimumY;
		
		final Color4F color = Color4F.blend(Color4F.blend(color00, color01, xFactor), Color4F.blend(color10, color11, xFactor), yFactor);
		
		return color;
	}
	
	/**
	 * Returns the {@link Color4F} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color4F} instances.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color4F} of the pixel represented by {@code x} and {@code y}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
	public final Color4F getColorRGBA(final float x, final float y, final PixelOperation pixelOperation) {
		final int minimumX = toInt(floor(x));
		final int maximumX = toInt(ceil(x));
		
		final int minimumY = toInt(floor(y));
		final int maximumY = toInt(ceil(y));
		
		if(minimumX == maximumX && minimumY == maximumY) {
			return getColorRGBA(minimumX, minimumY, pixelOperation);
		}
		
		final Color4F color00 = getColorRGBA(minimumX, minimumY, pixelOperation);
		final Color4F color01 = getColorRGBA(maximumX, minimumY, pixelOperation);
		final Color4F color10 = getColorRGBA(minimumX, maximumY, pixelOperation);
		final Color4F color11 = getColorRGBA(maximumX, maximumY, pixelOperation);
		
		final float xFactor = x - minimumX;
		final float yFactor = y - minimumY;
		
		final Color4F color = Color4F.blend(Color4F.blend(color00, color01, xFactor), Color4F.blend(color10, color11, xFactor), yFactor);
		
		return color;
	}
	
	/**
	 * Returns the {@link Color4F} of the pixel represented by {@code index}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGBA(index, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param index the index of the pixel
	 * @return the {@code Color4F} of the pixel represented by {@code index}
	 */
	public final Color4F getColorRGBA(final int index) {
		return getColorRGBA(index, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color4F} of the pixel represented by {@code index}.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param index the index of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color4F} of the pixel represented by {@code index}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
	public abstract Color4F getColorRGBA(final int index, final PixelOperation pixelOperation);
	
	/**
	 * Returns the {@link Color4F} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGBA(x, y, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @return the {@code Color4F} of the pixel represented by {@code x} and {@code y}
	 */
	public final Color4F getColorRGBA(final int x, final int y) {
		return getColorRGBA(x, y, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color4F} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * If {@code function} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param function a {@code Function} that returns a {@code Color4F} instance if {@code x} or {@code y} are outside the bounds
	 * @return the {@code Color4F} of the pixel represented by {@code x} and {@code y}
	 * @throws NullPointerException thrown if, and only if, {@code function} is {@code null}
	 */
	public abstract Color4F getColorRGBA(final int x, final int y, final Function<Point2I, Color4F> function);
	
	/**
	 * Returns the {@link Color4F} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color4F} of the pixel represented by {@code x} and {@code y}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
	public abstract Color4F getColorRGBA(final int x, final int y, final PixelOperation pixelOperation);
	
	/**
	 * Blends this {@code ImageF} instance over {@code image}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param image an {@code ImageF} instance that acts as a background
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	public final ImageF blendOver(final ImageF image) {
		return fillImage(image, image.getBounds(), getBounds(), (sourceColorRGBA, targetColorRGBA, targetPoint) -> Color4F.blendOver(targetColorRGBA, sourceColorRGBA));
	}
	
	/**
	 * Clears this {@code ImageF} instance with a {@link Color4F} of {@code Color4F.BLACK}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.clear(Color4F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @return this {@code ImageF} instance
	 */
	public final ImageF clear() {
		return clear(Color4F.BLACK);
	}
	
	/**
	 * Clears this {@code ImageF} instance with a {@link Color3F} of {@code colorRGB}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If {@code colorRGB} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.clear(new Color4F(colorRGB));
	 * }
	 * </pre>
	 * 
	 * @param colorRGB the {@code Color3F} to clear with
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code colorRGB} is {@code null}
	 */
	public final ImageF clear(final Color3F colorRGB) {
		return clear(new Color4F(colorRGB));
	}
	
	/**
	 * Clears this {@code ImageF} instance with a {@link Color4F} of {@code colorRGBA}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If {@code colorRGBA} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorRGBA the {@code Color4F} to clear with
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
	public final ImageF clear(final Color4F colorRGBA) {
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		doChangeBegin();
		
		final int resolution = getResolution();
		
		for(int i = 0; i < resolution; i++) {
			doSetColorRGBA(colorRGBA, i);
		}
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Returns a copy of this {@code ImageF} instance.
	 * 
	 * @return a copy of this {@code ImageF} instance
	 */
	@Override
	public abstract ImageF copy();
	
	/**
	 * Returns a copy of this {@code ImageF} instance within {@code bounds}.
	 * <p>
	 * If {@code bounds} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bounds a {@link Rectangle2I} instance that represents the bounds within this {@code ImageF} instance to copy
	 * @return a copy of this {@code ImageF} instance within {@code bounds}
	 * @throws NullPointerException thrown if, and only if, {@code bounds} is {@code null}
	 */
	@Override
	public abstract ImageF copy(final Rectangle2I bounds);
	
	/**
	 * Draws {@code circle} to this {@code ImageF} instance with {@code Color4F.BLACK} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If {@code circle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawCircle(circle, Color4F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param circle the {@link Circle2I} to draw
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code circle} is {@code null}
	 */
	public final ImageF drawCircle(final Circle2I circle) {
		return drawCircle(circle, Color4F.BLACK);
	}
	
	/**
	 * Draws {@code circle} to this {@code ImageF} instance with {@code colorRGBA} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code circle} or {@code colorRGBA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawCircle(circle, (color, point) -> colorRGBA);
	 * }
	 * </pre>
	 * 
	 * @param circle the {@link Circle2I} to draw
	 * @param colorRGBA the {@link Color4F} to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code circle} or {@code colorRGBA} are {@code null}
	 */
	public final ImageF drawCircle(final Circle2I circle, final Color4F colorRGBA) {
		Objects.requireNonNull(circle, "circle == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		return drawCircle(circle, (color, point) -> colorRGBA);
	}
	
	/**
	 * Draws {@code circle} to this {@code ImageF} instance with {@link Color4F} instances returned by {@code biFunction} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code circle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param circle the {@link Circle2I} to draw
	 * @param biFunction a {@code BiFunction} that returns {@code Color4F} instances to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code circle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final ImageF drawCircle(final Circle2I circle, final BiFunction<Color4F, Point2I, Color4F> biFunction) {
		Objects.requireNonNull(circle, "circle == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		doChangeBegin();
		
		circle.findPointsOfIntersection(getBounds(), true).forEach(point -> doSetColorRGBA(Objects.requireNonNull(biFunction.apply(getColorRGBA(point.getX(), point.getY()), point)), point.getX(), point.getY()));
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Draws everything except for {@code circle} in this {@code ImageF} instance with {@link Color4F} instances returned by {@code biFunction} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code circle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param circle the {@link Circle2I} not to draw
	 * @param biFunction a {@code BiFunction} that returns {@code Color4F} instances to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code circle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final ImageF drawCircleComplement(final Circle2I circle, final BiFunction<Color4F, Point2I, Color4F> biFunction) {
		Objects.requireNonNull(circle, "circle == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		doChangeBegin();
		
		circle.findPointsOfComplement(getBounds(), true).forEach(point -> doSetColorRGBA(Objects.requireNonNull(biFunction.apply(getColorRGBA(point.getX(), point.getY()), point)), point.getX(), point.getY()));
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Draws {@code lineSegment} to this {@code ImageF} instance with {@code Color4F.BLACK} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If {@code lineSegment} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawLineSegment(lineSegment, Color4F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param lineSegment the {@link LineSegment2I} to draw
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code lineSegment} is {@code null}
	 */
	public final ImageF drawLineSegment(final LineSegment2I lineSegment) {
		return drawLineSegment(lineSegment, Color4F.BLACK);
	}
	
	/**
	 * Draws {@code lineSegment} to this {@code ImageF} instance with {@code colorRGBA} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code lineSegment} or {@code colorRGBA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawLineSegment(lineSegment, (color, point) -> colorRGBA);
	 * }
	 * </pre>
	 * 
	 * @param lineSegment the {@link LineSegment2I} to draw
	 * @param colorRGBA the {@link Color4F} to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code lineSegment} or {@code colorRGBA} are {@code null}
	 */
	public final ImageF drawLineSegment(final LineSegment2I lineSegment, final Color4F colorRGBA) {
		Objects.requireNonNull(lineSegment, "lineSegment == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		return drawLineSegment(lineSegment, (color, point) -> colorRGBA);
	}
	
	/**
	 * Draws {@code lineSegment} to this {@code ImageF} instance with {@link Color4F} instances returned by {@code biFunction} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code lineSegment} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lineSegment the {@link LineSegment2I} to draw
	 * @param biFunction a {@code BiFunction} that returns {@code Color4F} instances to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code lineSegment} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final ImageF drawLineSegment(final LineSegment2I lineSegment, final BiFunction<Color4F, Point2I, Color4F> biFunction) {
		Objects.requireNonNull(lineSegment, "lineSegment == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		doChangeBegin();
		doDrawLineSegment(lineSegment, biFunction);
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Draws {@code polygon} to this {@code ImageF} instance with {@code Color4F.BLACK} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If {@code polygon} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawPolygon(polygon, Color4F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param polygon the {@link Polygon2I} to draw
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code polygon} is {@code null}
	 */
	public final ImageF drawPolygon(final Polygon2I polygon) {
		return drawPolygon(polygon, Color4F.BLACK);
	}
	
	/**
	 * Draws {@code polygon} to this {@code ImageF} instance with {@code colorRGBA} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code polygon} or {@code colorRGBA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawPolygon(polygon, (color, point) -> colorRGBA);
	 * }
	 * </pre>
	 * 
	 * @param polygon the {@link Polygon2I} to draw
	 * @param colorRGBA the {@link Color4F} to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code polygon} or {@code colorRGBA} are {@code null}
	 */
	public final ImageF drawPolygon(final Polygon2I polygon, final Color4F colorRGBA) {
		Objects.requireNonNull(polygon, "polygon == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		return drawPolygon(polygon, (color, point) -> colorRGBA);
	}
	
	/**
	 * Draws {@code polygon} to this {@code ImageF} instance with {@link Color4F} instances returned by {@code biFunction} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code polygon} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param polygon the {@link Polygon2I} to draw
	 * @param biFunction a {@code BiFunction} that returns {@code Color4F} instances to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code polygon} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final ImageF drawPolygon(final Polygon2I polygon, final BiFunction<Color4F, Point2I, Color4F> biFunction) {
		Objects.requireNonNull(polygon, "polygon == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		final List<LineSegment2I> lineSegments = polygon.getLineSegments();
		
		doChangeBegin();
		
		for(final LineSegment2I lineSegment : lineSegments) {
			doDrawLineSegment(lineSegment, biFunction);
		}
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Draws {@code rectangle} to this {@code ImageF} instance with {@code Color4F.BLACK} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawRectangle(rectangle, Color4F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param rectangle the {@link Rectangle2I} to draw
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 */
	public final ImageF drawRectangle(final Rectangle2I rectangle) {
		return drawRectangle(rectangle, Color4F.BLACK);
	}
	
	/**
	 * Draws {@code rectangle} to this {@code ImageF} instance with {@code colorRGBA} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code rectangle} or {@code colorRGBA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawRectangle(rectangle, (color, point) -> colorRGBA);
	 * }
	 * </pre>
	 * 
	 * @param rectangle the {@link Rectangle2I} to draw
	 * @param colorRGBA the {@link Color4F} to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code rectangle} or {@code colorRGBA} are {@code null}
	 */
	public final ImageF drawRectangle(final Rectangle2I rectangle, final Color4F colorRGBA) {
		Objects.requireNonNull(rectangle, "rectangle == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		return drawRectangle(rectangle, (color, point) -> colorRGBA);
	}
	
	/**
	 * Draws {@code rectangle} to this {@code ImageF} instance with {@link Color4F} instances returned by {@code biFunction} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code rectangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rectangle the {@link Rectangle2I} to draw
	 * @param biFunction a {@code BiFunction} that returns {@code Color4F} instances to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code rectangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final ImageF drawRectangle(final Rectangle2I rectangle, final BiFunction<Color4F, Point2I, Color4F> biFunction) {
		Objects.requireNonNull(rectangle, "rectangle == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		doChangeBegin();
		
		final int minimumX = rectangle.getA().getX();
		final int minimumY = rectangle.getA().getY();
		final int maximumX = rectangle.getC().getX();
		final int maximumY = rectangle.getC().getY();
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		for(int y = minimumY; y <= maximumY; y++) {
			for(int x = minimumX; x <= maximumX; x++) {
				if((x == minimumX || x == maximumX || y == minimumY || y == maximumY) && x >= 0 && x < resolutionX && y >= 0 && y < resolutionY) {
					final Point2I point = new Point2I(x, y);
					
					final Color4F oldColorRGBA = getColorRGBA(x, y);
					final Color4F newColorRGBA = Objects.requireNonNull(biFunction.apply(oldColorRGBA, point));
					
					doSetColorRGBA(newColorRGBA, x, y);
				}
			}
		}
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Draws {@code triangle} to this {@code ImageF} instance with {@code Color4F.BLACK} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If {@code triangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawTriangle(triangle, Color4F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param triangle the {@link Triangle2I} to draw
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code triangle} is {@code null}
	 */
	public final ImageF drawTriangle(final Triangle2I triangle) {
		return drawTriangle(triangle, Color4F.BLACK);
	}
	
	/**
	 * Draws {@code triangle} to this {@code ImageF} instance with {@code colorRGBA} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code triangle} or {@code colorRGBA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawTriangle(triangle, (color, point) -> colorRGBA);
	 * }
	 * </pre>
	 * 
	 * @param triangle the {@link Triangle2I} to draw
	 * @param colorRGBA the {@link Color4F} to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code triangle} or {@code colorRGBA} are {@code null}
	 */
	public final ImageF drawTriangle(final Triangle2I triangle, final Color4F colorRGBA) {
		Objects.requireNonNull(triangle, "triangle == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		return drawTriangle(triangle, (color, point) -> colorRGBA);
	}
	
	/**
	 * Draws {@code triangle} to this {@code ImageF} instance with {@link Color4F} instances returned by {@code biFunction} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code triangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param triangle the {@link Triangle2I} to draw
	 * @param biFunction a {@code BiFunction} that returns {@code Color4F} instances to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code triangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final ImageF drawTriangle(final Triangle2I triangle, final BiFunction<Color4F, Point2I, Color4F> biFunction) {
		Objects.requireNonNull(triangle, "triangle == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		doChangeBegin();
		doDrawLineSegment(new LineSegment2I(triangle.getA(), triangle.getB()), biFunction);
		doDrawLineSegment(new LineSegment2I(triangle.getB(), triangle.getC()), biFunction);
		doDrawLineSegment(new LineSegment2I(triangle.getC(), triangle.getA()), biFunction);
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Fills {@code circle} in this {@code ImageF} instance with {@code Color4F.BLACK} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If {@code circle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillCircle(circle, Color4F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param circle the {@link Circle2I} to fill
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code circle} is {@code null}
	 */
	public final ImageF fillCircle(final Circle2I circle) {
		return fillCircle(circle, Color4F.BLACK);
	}
	
	/**
	 * Fills {@code circle} in this {@code ImageF} instance with {@code colorRGBA} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code circle} or {@code colorRGBA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillCircle(circle, (color, point) -> colorRGBA);
	 * }
	 * </pre>
	 * 
	 * @param circle the {@link Circle2I} to fill
	 * @param colorRGBA the {@link Color4F} to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code circle} or {@code colorRGBA} are {@code null}
	 */
	public final ImageF fillCircle(final Circle2I circle, final Color4F colorRGBA) {
		Objects.requireNonNull(circle, "circle == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		return fillCircle(circle, (color, point) -> colorRGBA);
	}
	
	/**
	 * Fills {@code circle} in this {@code ImageF} instance with {@link Color4F} instances returned by {@code biFunction} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code circle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param circle the {@link Circle2I} to fill
	 * @param biFunction a {@code BiFunction} that returns {@code Color4F} instances to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code circle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final ImageF fillCircle(final Circle2I circle, final BiFunction<Color4F, Point2I, Color4F> biFunction) {
		Objects.requireNonNull(circle, "circle == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		doChangeBegin();
		
		circle.findPointsOfIntersection(getBounds()).forEach(point -> doSetColorRGBA(Objects.requireNonNull(biFunction.apply(getColorRGBA(point.getX(), point.getY()), point)), point.getX(), point.getY()));
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Fills everything except for {@code circle} in this {@code ImageF} instance with {@link Color4F} instances returned by {@code biFunction} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code circle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param circle the {@link Circle2I} not to fill
	 * @param biFunction a {@code BiFunction} that returns {@code Color4F} instances to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code circle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final ImageF fillCircleComplement(final Circle2I circle, final BiFunction<Color4F, Point2I, Color4F> biFunction) {
		Objects.requireNonNull(circle, "circle == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		doChangeBegin();
		
		circle.findPointsOfComplement(getBounds()).forEach(point -> doSetColorRGBA(Objects.requireNonNull(biFunction.apply(getColorRGBA(point.getX(), point.getY()), point)), point.getX(), point.getY()));
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Fills a gradient in this {@code ImageF} instance.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillGradient(Color3F.BLACK, Color3F.RED, Color3F.GREEN, Color3F.YELLOW);
	 * }
	 * </pre>
	 * 
	 * @return this {@code ImageF} instance
	 */
	public final ImageF fillGradient() {
		return fillGradient(Color3F.BLACK, Color3F.RED, Color3F.GREEN, Color3F.YELLOW);
	}
	
	/**
	 * Fills a gradient in this {@code ImageF} instance.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a the {@link Color3F} instance in the top left corner
	 * @param b the {@code Color3F} instance in the top right corner
	 * @param c the {@code Color3F} instance in the bottom left corner
	 * @param d the {@code Color3F} instance in the bottom right corner
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}
	 */
	public final ImageF fillGradient(final Color3F a, final Color3F b, final Color3F c, final Color3F d) {
		Objects.requireNonNull(a, "a == null");
		Objects.requireNonNull(b, "b == null");
		Objects.requireNonNull(c, "c == null");
		Objects.requireNonNull(d, "d == null");
		
		final float resolutionX = getResolutionX();
		final float resolutionY = getResolutionY();
		
		return update((color, point) -> {
			final float tX = 1.0F / resolutionX * point.getX();
			final float tY = 1.0F / resolutionY * point.getY();
			
			return new Color4F(Color3F.blend(Color3F.blend(a, b, tX), Color3F.blend(c, d, tX), tY));
		});
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code ImageF} instance.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If {@code sourceImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillImage(sourceImage, sourceImage.getBounds());
	 * }
	 * </pre>
	 * 
	 * @param sourceImage the {@code ImageF} to fill
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code sourceImage} is {@code null}
	 */
	public final ImageF fillImage(final ImageF sourceImage) {
		return fillImage(sourceImage, sourceImage.getBounds());
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code ImageF} instance.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code sourceImage} or {@code targetPosition} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillImage(sourceImage, targetPosition, (sourceColorRGBA, targetColorRGBA, targetPoint) -> Color4F.blendOver(sourceColorRGBA, targetColorRGBA));
	 * }
	 * </pre>
	 * 
	 * @param sourceImage the {@code ImageF} to fill
	 * @param targetPosition a {@link Point2I} that represents the position in this {@code ImageF} instance to start filling {@code sourceImage}
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code sourceImage} or {@code targetPosition} are {@code null}
	 */
	public final ImageF fillImage(final ImageF sourceImage, final Point2I targetPosition) {
		return fillImage(sourceImage, targetPosition, (sourceColorRGBA, targetColorRGBA, targetPoint) -> Color4F.blendOver(sourceColorRGBA, targetColorRGBA));
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code ImageF} instance.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code sourceImage}, {@code targetPosition} or {@code triFunction} are {@code null} or {@code triFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sourceImage the {@code ImageF} to fill
	 * @param targetPosition a {@link Point2I} that represents the position in this {@code ImageF} instance to start filling {@code sourceImage}
	 * @param triFunction a {@code TriFunction} that returns {@code Color4F} instances to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code sourceImage}, {@code targetPosition} or {@code triFunction} are {@code null} or {@code triFunction} returns {@code null}
	 */
	public final ImageF fillImage(final ImageF sourceImage, final Point2I targetPosition, final TriFunction<Color4F, Color4F, Point2I, Color4F> triFunction) {
		final Rectangle2I sourceBounds = sourceImage.getBounds();
		final Rectangle2I targetBounds = new Rectangle2I(targetPosition, new Point2I(targetPosition.getX() + (sourceBounds.getC().getX() - sourceBounds.getA().getX()), targetPosition.getY() + (sourceBounds.getC().getY() - sourceBounds.getA().getY())));
		
		return fillImage(sourceImage, sourceBounds, targetBounds, triFunction);
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code ImageF} instance.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code sourceImage} or {@code sourceBounds} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillImage(sourceImage, sourceBounds, image.getBounds());
	 * }
	 * </pre>
	 * 
	 * @param sourceImage the {@code ImageF} to fill
	 * @param sourceBounds a {@link Rectangle2I} that represents the bounds of the region in {@code sourceImage} to use
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code sourceImage} or {@code sourceBounds} are {@code null}
	 */
	public final ImageF fillImage(final ImageF sourceImage, final Rectangle2I sourceBounds) {
		return fillImage(sourceImage, sourceBounds, getBounds());
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code ImageF} instance.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code sourceImage}, {@code sourceBounds} or {@code targetBounds} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillImage(sourceImage, sourceBounds, targetBounds, (sourceColorRGBA, targetColorRGBA, targetPoint) -> Color4F.blendOver(sourceColorRGBA, targetColorRGBA));
	 * }
	 * </pre>
	 * 
	 * @param sourceImage the {@code ImageF} to fill
	 * @param sourceBounds a {@link Rectangle2I} that represents the bounds of the region in {@code sourceImage} to use
	 * @param targetBounds a {@code Rectangle2I} that represents the bounds of the region in this {@code ImageF} instance to use
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code sourceImage}, {@code sourceBounds} or {@code targetBounds} are {@code null}
	 */
	public final ImageF fillImage(final ImageF sourceImage, final Rectangle2I sourceBounds, final Rectangle2I targetBounds) {
		return fillImage(sourceImage, sourceBounds, targetBounds, (sourceColorRGBA, targetColorRGBA, targetPoint) -> Color4F.blendOver(sourceColorRGBA, targetColorRGBA));
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code ImageF} instance with {@link Color4F} instances returned by {@code triFunction} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code sourceImage}, {@code sourceBounds}, {@code targetBounds} or {@code triFunction} are {@code null} or {@code triFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sourceImage the {@code ImageF} to fill
	 * @param sourceBounds a {@link Rectangle2I} that represents the bounds of the region in {@code sourceImage} to use
	 * @param targetBounds a {@code Rectangle2I} that represents the bounds of the region in this {@code ImageF} instance to use
	 * @param triFunction a {@code TriFunction} that returns {@code Color4F} instances to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code sourceImage}, {@code sourceBounds}, {@code targetBounds} or {@code triFunction} are {@code null} or {@code triFunction} returns {@code null}
	 */
	public final ImageF fillImage(final ImageF sourceImage, final Rectangle2I sourceBounds, final Rectangle2I targetBounds, final TriFunction<Color4F, Color4F, Point2I, Color4F> triFunction) {
		Objects.requireNonNull(sourceImage, "sourceImage == null");
		Objects.requireNonNull(sourceBounds, "sourceBounds == null");
		Objects.requireNonNull(targetBounds, "targetBounds == null");
		Objects.requireNonNull(triFunction, "triFunction == null");
		
		doChangeBegin();
		
		final ImageF targetImage = this;
		
		final int sourceMinimumX = sourceBounds.getA().getX();
		final int sourceMinimumY = sourceBounds.getA().getY();
		final int sourceMaximumX = sourceBounds.getC().getX();
		final int sourceMaximumY = sourceBounds.getC().getY();
		final int targetMinimumX = targetBounds.getA().getX();
		final int targetMinimumY = targetBounds.getA().getY();
		final int targetMaximumX = targetBounds.getC().getX();
		final int targetMaximumY = targetBounds.getC().getY();
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		for(int sourceY = sourceMinimumY, targetY = targetMinimumY; sourceY < sourceMaximumY && targetY < targetMaximumY; sourceY++, targetY++) {
			for(int sourceX = sourceMinimumX, targetX = targetMinimumX; sourceX < sourceMaximumX && targetX < targetMaximumX; sourceX++, targetX++) {
				if(targetX >= 0 && targetX < resolutionX && targetY >= 0 && targetY < resolutionY) {
					final Color4F sourceColorRGBA = sourceImage.getColorRGBA(sourceX, sourceY);
					final Color4F targetColorRGBA = targetImage.getColorRGBA(targetX, targetY);
					final Color4F colorRGBA = Objects.requireNonNull(triFunction.apply(sourceColorRGBA, targetColorRGBA, new Point2I(targetX, targetY)));
					
					targetImage.doSetColorRGBA(colorRGBA, targetX, targetY);
				}
			}
		}
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Fills {@code polygon} in this {@code ImageF} instance with {@code Color4F.BLACK} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If {@code polygon} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillPolygon(polygon, Color4F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param polygon the {@link Polygon2I} to fill
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code polygon} is {@code null}
	 */
	public final ImageF fillPolygon(final Polygon2I polygon) {
		return fillPolygon(polygon, Color4F.BLACK);
	}
	
	/**
	 * Fills {@code polygon} in this {@code ImageF} instance with {@code colorRGBA} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code polygon} or {@code colorRGBA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillPolygon(polygon, (color, point) -> colorRGBA);
	 * }
	 * </pre>
	 * 
	 * @param polygon the {@link Polygon2I} to fill
	 * @param colorRGBA the {@link Color4F} to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code polygon} or {@code colorRGBA} are {@code null}
	 */
	public final ImageF fillPolygon(final Polygon2I polygon, final Color4F colorRGBA) {
		Objects.requireNonNull(polygon, "polygon == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		return fillPolygon(polygon, (color, point) -> colorRGBA);
	}
	
	/**
	 * Fills {@code polygon} in this {@code ImageF} instance with {@link Color4F} instances returned by {@code biFunction} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code polygon} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param polygon the {@link Polygon2I} to fill
	 * @param biFunction a {@code BiFunction} that returns {@code Color4F} instances to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code polygon} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final ImageF fillPolygon(final Polygon2I polygon, final BiFunction<Color4F, Point2I, Color4F> biFunction) {
		Objects.requireNonNull(polygon, "polygon == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		doChangeBegin();
		
		polygon.findPointsOfIntersection(getBounds()).forEach(point -> doSetColorRGBA(Objects.requireNonNull(biFunction.apply(getColorRGBA(point.getX(), point.getY()), point)), point.getX(), point.getY()));
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Fills {@code rectangle} in this {@code ImageF} instance with {@code Color4F.BLACK} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillRectangle(rectangle, Color4F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param rectangle the {@link Rectangle2I} to fill
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 */
	public final ImageF fillRectangle(final Rectangle2I rectangle) {
		return fillRectangle(rectangle, Color4F.BLACK);
	}
	
	/**
	 * Fills {@code rectangle} in this {@code ImageF} instance with {@code colorRGB} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code rectangle} or {@code colorRGB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillRectangle(rectangle, new Color4F(colorRGB));
	 * }
	 * </pre>
	 * 
	 * @param rectangle the {@link Rectangle2I} to fill
	 * @param colorRGB the {@link Color3F} to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code rectangle} or {@code colorRGB} are {@code null}
	 */
	public final ImageF fillRectangle(final Rectangle2I rectangle, final Color3F colorRGB) {
		Objects.requireNonNull(rectangle, "rectangle == null");
		Objects.requireNonNull(colorRGB, "colorRGB == null");
		
		return fillRectangle(rectangle, new Color4F(colorRGB));
	}
	
	/**
	 * Fills {@code rectangle} in this {@code ImageF} instance with {@code colorRGBA} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code rectangle} or {@code colorRGBA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillRectangle(rectangle, (color, point) -> colorRGBA);
	 * }
	 * </pre>
	 * 
	 * @param rectangle the {@link Rectangle2I} to fill
	 * @param colorRGBA the {@link Color4F} to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code rectangle} or {@code colorRGBA} are {@code null}
	 */
	public final ImageF fillRectangle(final Rectangle2I rectangle, final Color4F colorRGBA) {
		Objects.requireNonNull(rectangle, "rectangle == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		return fillRectangle(rectangle, (color, point) -> colorRGBA);
	}
	
	/**
	 * Fills {@code rectangle} in this {@code ImageF} instance with {@link Color4F} instances returned by {@code biFunction} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code rectangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rectangle the {@link Rectangle2I} to fill
	 * @param biFunction a {@code BiFunction} that returns {@code Color4F} instances to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code rectangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final ImageF fillRectangle(final Rectangle2I rectangle, final BiFunction<Color4F, Point2I, Color4F> biFunction) {
		Objects.requireNonNull(rectangle, "rectangle == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		doChangeBegin();
		
		final int minimumX = rectangle.getA().getX();
		final int minimumY = rectangle.getA().getY();
		final int maximumX = rectangle.getC().getX();
		final int maximumY = rectangle.getC().getY();
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		for(int y = minimumY; y <= maximumY; y++) {
			for(int x = minimumX; x <= maximumX; x++) {
				if(x >= 0 && x < resolutionX && y >= 0 && y < resolutionY) {
					final Point2I point = new Point2I(x, y);
					
					final Color4F oldColorRGBA = getColorRGBA(x, y);
					final Color4F newColorRGBA = Objects.requireNonNull(biFunction.apply(oldColorRGBA, point));
					
					doSetColorRGBA(newColorRGBA, x, y);
				}
			}
		}
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Fills this {@code ImageF} instance with {@link Color4F} instances that are generated using a Simplex-based fractional Brownian motion (fBm) algorithm.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillSimplexFractionalBrownianMotion(new Color3F(0.75F, 0.5F, 0.75F));
	 * }
	 * </pre>
	 * 
	 * @return this {@code ImageF} instance
	 */
	public final ImageF fillSimplexFractionalBrownianMotion() {
		return fillSimplexFractionalBrownianMotion(new Color3F(0.75F, 0.5F, 0.75F));
	}
	
	/**
	 * Fills this {@code ImageF} instance with {@link Color4F} instances that are generated using a Simplex-based fractional Brownian motion (fBm) algorithm.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If {@code baseColor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillSimplexFractionalBrownianMotion(baseColor, 5.0F, 0.5F, 16);
	 * }
	 * </pre>
	 * 
	 * @param baseColor a {@link Color3F} instance that is used as the base color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code baseColor} is {@code null}
	 */
	public final ImageF fillSimplexFractionalBrownianMotion(final Color3F baseColor) {
		return fillSimplexFractionalBrownianMotion(baseColor, 5.0F, 0.5F, 16);
	}
	
	/**
	 * Fills this {@code ImageF} instance with {@link Color4F} instances that are generated using a Simplex-based fractional Brownian motion (fBm) algorithm.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If {@code baseColor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param baseColor a {@link Color3F} instance that is used as the base color
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param octaves the number of iterations to perform
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code baseColor} is {@code null}
	 */
	public final ImageF fillSimplexFractionalBrownianMotion(final Color3F baseColor, final float frequency, final float gain, final int octaves) {
		Objects.requireNonNull(baseColor, "baseColor == null");
		
		final float minimumX = 0.0F;
		final float minimumY = 0.0F;
		final float maximumX = getResolutionX();
		final float maximumY = getResolutionY();
		
		return update((color, point) -> {
			final float x = (point.getX() - minimumX) / (maximumX - minimumX);
			final float y = (point.getY() - minimumY) / (maximumY - minimumY);
			
			final float noise = SimplexNoiseF.fractionalBrownianMotionXY(x, y, frequency, gain, 0.0F, 1.0F, octaves);
			
			return new Color4F(Color3F.multiply(baseColor, noise));
		});
	}
	
	/**
	 * Fills {@code triangle} in this {@code ImageF} instance with {@code Color4F.BLACK} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If {@code triangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillTriangle(triangle, Color4F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param triangle the {@link Triangle2I} to fill
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code triangle} is {@code null}
	 */
	public final ImageF fillTriangle(final Triangle2I triangle) {
		return fillTriangle(triangle, Color4F.BLACK);
	}
	
	/**
	 * Fills {@code triangle} in this {@code ImageF} instance with {@code colorRGBA} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code triangle} or {@code colorRGBA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillTriangle(triangle, (color, point) -> colorRGBA);
	 * }
	 * </pre>
	 * 
	 * @param triangle the {@link Triangle2I} to fill
	 * @param colorRGBA the {@link Color4F} to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code triangle} or {@code colorRGBA} are {@code null}
	 */
	public final ImageF fillTriangle(final Triangle2I triangle, final Color4F colorRGBA) {
		Objects.requireNonNull(triangle, "triangle == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		return fillTriangle(triangle, (color, point) -> colorRGBA);
	}
	
	/**
	 * Fills {@code triangle} in this {@code ImageF} instance with {@link Color4F} instances returned by {@code biFunction} as its color.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code triangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param triangle the {@link Triangle2I} to fill
	 * @param biFunction a {@code BiFunction} that returns {@code Color4F} instances to use as its color
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code triangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final ImageF fillTriangle(final Triangle2I triangle, final BiFunction<Color4F, Point2I, Color4F> biFunction) {
		Objects.requireNonNull(triangle, "triangle == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		doChangeBegin();
		
		final Rectangle2I rectangle = new Rectangle2I(new Point2I(), new Point2I(getResolutionX(), getResolutionY()));
		
		final Point2I[][] scanlines = Rasterizer2I.rasterize(triangle, rectangle);
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		for(final Point2I[] scanline : scanlines) {
			for(final Point2I point : scanline) {
				final int x = point.getX();
				final int y = point.getY();
				
				if(x >= 0 && x < resolutionX && y >= 0 && y < resolutionY) {
					final Color4F oldColorRGBA = getColorRGBA(x, y);
					final Color4F newColorRGBA = Objects.requireNonNull(biFunction.apply(oldColorRGBA, point));
					
					doSetColorRGBA(newColorRGBA, point.getX(), point.getY());
				}
			}
		}
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Converts this {@code ImageF} instance into grayscale using {@link Color4F#grayscaleAverage(Color4F)}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * 
	 * @return this {@code ImageF} instance
	 */
	public final ImageF grayscaleAverage() {
		return update((color, point) -> Color4F.grayscaleAverage(color));
	}
	
	/**
	 * Converts this {@code ImageF} instance into grayscale using {@link Color4F#grayscaleComponent1(Color4F)}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * 
	 * @return this {@code ImageF} instance
	 */
	public final ImageF grayscaleComponent1() {
		return update((color, point) -> Color4F.grayscaleComponent1(color));
	}
	
	/**
	 * Converts this {@code ImageF} instance into grayscale using {@link Color4F#grayscaleComponent2(Color4F)}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * 
	 * @return this {@code ImageF} instance
	 */
	public final ImageF grayscaleComponent2() {
		return update((color, point) -> Color4F.grayscaleComponent2(color));
	}
	
	/**
	 * Converts this {@code ImageF} instance into grayscale using {@link Color4F#grayscaleComponent3(Color4F)}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * 
	 * @return this {@code ImageF} instance
	 */
	public final ImageF grayscaleComponent3() {
		return update((color, point) -> Color4F.grayscaleComponent3(color));
	}
	
	/**
	 * Converts this {@code ImageF} instance into grayscale using {@link Color4F#grayscaleLightness(Color4F)}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * 
	 * @return this {@code ImageF} instance
	 */
	public final ImageF grayscaleLightness() {
		return update((color, point) -> Color4F.grayscaleLightness(color));
	}
	
	/**
	 * Converts this {@code ImageF} instance into grayscale using {@link Color4F#grayscaleLuminance(Color4F)}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * 
	 * @return this {@code ImageF} instance
	 */
	public final ImageF grayscaleLuminance() {
		return update((color, point) -> Color4F.grayscaleLuminance(color));
	}
	
	/**
	 * Converts this {@code ImageF} instance into grayscale using {@link Color4F#grayscaleMaximum(Color4F)}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * 
	 * @return this {@code ImageF} instance
	 */
	public final ImageF grayscaleMaximum() {
		return update((color, point) -> Color4F.grayscaleMaximum(color));
	}
	
	/**
	 * Converts this {@code ImageF} instance into grayscale using {@link Color4F#grayscaleMinimum(Color4F)}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * 
	 * @return this {@code ImageF} instance
	 */
	public final ImageF grayscaleMinimum() {
		return update((color, point) -> Color4F.grayscaleMinimum(color));
	}
	
	/**
	 * Inverts this {@code ImageF} instance.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * 
	 * @return this {@code ImageF} instance
	 */
	public final ImageF invert() {
		return update((color, point) -> Color4F.invert(color));
	}
	
	/**
	 * Multiplies this {@code ImageF} instance with {@code convolutionKernel}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If {@code convolutionKernel} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param convolutionKernel a {@link ConvolutionKernel33F} instance
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code convolutionKernel} is {@code null}
	 */
	public final ImageF multiply(final ConvolutionKernel33F convolutionKernel) {
		final Color3F factor = new Color3F(convolutionKernel.getFactor());
		final Color3F bias = new Color3F(convolutionKernel.getBias());
		
		doChangeBegin();
		
		final ImageF image = copy();
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++) {
				Color3F colorRGB = Color3F.BLACK;
				Color4F colorRGBA = image.getColorRGBA(x, y);
				
//				Row #1:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -1, y + -1), convolutionKernel.getElement11()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +0, y + -1), convolutionKernel.getElement12()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +1, y + -1), convolutionKernel.getElement13()));
				
//				Row #2:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -1, y + +0), convolutionKernel.getElement21()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +0, y + +0), convolutionKernel.getElement22()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +1, y + +0), convolutionKernel.getElement23()));
				
//				Row #3:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -1, y + +1), convolutionKernel.getElement31()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +0, y + +1), convolutionKernel.getElement32()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +1, y + +1), convolutionKernel.getElement33()));
				
//				Multiply with the factor and add the bias:
				colorRGB = Color3F.multiply(colorRGB, factor);
				colorRGB = Color3F.add(colorRGB, bias);
				colorRGB = Color3F.minimumTo0(colorRGB);
				colorRGB = Color3F.maximumTo1(colorRGB);
				
				colorRGBA = new Color4F(colorRGB.getR(), colorRGB.getG(), colorRGB.getB(), colorRGBA.getA());
				
				doSetColorRGBA(colorRGBA, x, y);
			}
		}
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Multiplies this {@code ImageF} instance with {@code convolutionKernel}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If {@code convolutionKernel} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param convolutionKernel a {@link ConvolutionKernel55F} instance
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code convolutionKernel} is {@code null}
	 */
	public final ImageF multiply(final ConvolutionKernel55F convolutionKernel) {
		final Color3F factor = new Color3F(convolutionKernel.getFactor());
		final Color3F bias = new Color3F(convolutionKernel.getBias());
		
		doChangeBegin();
		
		final ImageF image = copy();
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++) {
				Color3F colorRGB = Color3F.BLACK;
				Color4F colorRGBA = image.getColorRGBA(x, y);
				
//				Row #1:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -2, y + -2), convolutionKernel.getElement11()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -1, y + -2), convolutionKernel.getElement12()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +0, y + -2), convolutionKernel.getElement13()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +1, y + -2), convolutionKernel.getElement14()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +2, y + -2), convolutionKernel.getElement15()));
				
//				Row #2:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -2, y + -1), convolutionKernel.getElement21()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -1, y + -1), convolutionKernel.getElement22()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +0, y + -1), convolutionKernel.getElement23()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +1, y + -1), convolutionKernel.getElement24()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +2, y + -1), convolutionKernel.getElement25()));
				
//				Row #3:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -2, y + +0), convolutionKernel.getElement31()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -1, y + +0), convolutionKernel.getElement32()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +0, y + +0), convolutionKernel.getElement33()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +1, y + +0), convolutionKernel.getElement34()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +2, y + +0), convolutionKernel.getElement35()));
				
//				Row #4:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -2, y + +1), convolutionKernel.getElement41()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -1, y + +1), convolutionKernel.getElement42()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +0, y + +1), convolutionKernel.getElement43()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +1, y + +1), convolutionKernel.getElement44()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +2, y + +1), convolutionKernel.getElement45()));
				
//				Row #5:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -2, y + +2), convolutionKernel.getElement51()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -1, y + +2), convolutionKernel.getElement52()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +0, y + +2), convolutionKernel.getElement53()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +1, y + +2), convolutionKernel.getElement54()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +2, y + +2), convolutionKernel.getElement55()));
				
//				Multiply with the factor and add the bias:
				colorRGB = Color3F.multiply(colorRGB, factor);
				colorRGB = Color3F.add(colorRGB, bias);
				colorRGB = Color3F.minimumTo0(colorRGB);
				colorRGB = Color3F.maximumTo1(colorRGB);
				
				colorRGBA = new Color4F(colorRGB.getR(), colorRGB.getG(), colorRGB.getB(), colorRGBA.getA());
				
				doSetColorRGBA(colorRGBA, x, y);
			}
		}
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Redoes gamma correction on this {@code ImageF} instance using PBRT.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * 
	 * @return this {@code ImageF} instance
	 */
	public final ImageF redoGammaCorrectionPBRT() {
		return update((color, point) -> Color4F.redoGammaCorrectionPBRT(color));
	}
	
	/**
	 * Redoes gamma correction on this {@code ImageF} instance using sRGB.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * 
	 * @return this {@code ImageF} instance
	 */
	public final ImageF redoGammaCorrectionSRGB() {
		return update((color, point) -> Color4F.redoGammaCorrectionSRGB(color));
	}
	
	/**
	 * Rotates this {@code ImageF} instance with an angle of {@code angle}.
	 * <p>
	 * Returns a new rotated version of this {@code ImageF} instance.
	 * <p>
	 * If {@code angle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.rotate(angle, false);
	 * }
	 * </pre>
	 * 
	 * @param angle an {@link AngleF} instance
	 * @return a new rotated version of this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
	 */
	public final ImageF rotate(final AngleF angle) {
		return rotate(angle, false);
	}
	
	/**
	 * Rotates this {@code ImageF} instance with an angle of {@code angle}.
	 * <p>
	 * Returns a new rotated version of this {@code ImageF} instance.
	 * <p>
	 * If {@code angle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param angle an {@link AngleF} instance
	 * @param isWrappingAround {@code true} if, and only if, a wrap-around operation should be used, {@code false} otherwise
	 * @return a new rotated version of this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
	 */
	public final ImageF rotate(final AngleF angle, final boolean isWrappingAround) {
		Objects.requireNonNull(angle, "angle == null");
		
//		Retrieve the resolution for the old ImageF instance:
		final float oldResolutionX = getResolutionX();
		final float oldResolutionY = getResolutionY();
		
//		Compute the first translation directions from the old to the new ImageF instance and from the new to the old ImageF instance:
		final Vector2F directionAToNewImage = new Vector2F(-oldResolutionX * 0.5F, -oldResolutionY * 0.5F);
		final Vector2F directionAToOldImage = Vector2F.negate(directionAToNewImage);
		
//		Retrieve the AngleF instances to rotate from the old to the new ImageF instance and from the new to the old ImageF instance:
		final AngleF angleToNewImage = angle;
		final AngleF angleToOldImage = AngleF.negate(angleToNewImage);
		
//		Compute the original, translated and rotated Rectangle2F instances for the new ImageF instance:
		final Rectangle2F rectangleA = new Rectangle2F(new Point2F(), new Point2F(oldResolutionX, oldResolutionY));
		final Rectangle2F rectangleB = Rectangle2F.translate(rectangleA, directionAToNewImage);
		final Rectangle2F rectangleC = Rectangle2F.rotate(rectangleB, angleToNewImage);
		
//		Compute the minimum and maximum Point2F instances from the translated and rotated Rectangle2F instance:
		final Point2F minimum = Point2F.minimum(rectangleC.getA(), rectangleC.getB(), rectangleC.getC(), rectangleC.getD());
		final Point2F maximum = Point2F.maximum(rectangleC.getA(), rectangleC.getB(), rectangleC.getC(), rectangleC.getD());
		
//		Compute the second translation direction for the new and old ImageF instances:
		final Vector2F directionBToNewImage = new Vector2F(abs(min(minimum.getX(), 0.0F)), abs(min(minimum.getY(), 0.0F)));
		final Vector2F directionBToOldImage = Vector2F.negate(directionBToNewImage);
		
//		Compute the resolution for the new ImageF instance:
		final int newResolutionX = toInt(maximum.getX() - minimum.getX());
		final int newResolutionY = toInt(maximum.getY() - minimum.getY());
		
//		Initialize the old and new ImageF instances:
		final ImageF oldImage = this;
		final ImageF newImage = oldImage.newImage(newResolutionX, newResolutionY);
		
		for(int y = 0; y < newResolutionY; y++) {
			for(int x = 0; x < newResolutionX; x++) {
//				Compute the current Point2F instance for the new ImageF instance and reverse the operations to get the equivalent Point2F instance for the old ImageF instance:
				final Point2F a = new Point2F(x, y);
				final Point2F b = Point2F.add(a, directionBToOldImage);
				final Point2F c = Point2F.rotate(b, angleToOldImage);
				final Point2F d = Point2F.add(c, directionAToOldImage);
				
//				Set the Color4F instance in the new ImageF instance:
				newImage.doSetColorRGBA(isWrappingAround ? oldImage.getColorRGBA(d, PixelOperation.WRAP_AROUND) : oldImage.getColorRGBA(d, point -> Color4F.TRANSPARENT), x, y);
			}
		}
		
		return newImage;
	}
	
	/**
	 * Scales this {@code ImageF} instance with a scale of {@code scale}.
	 * <p>
	 * Returns a new scaled version of this {@code ImageF} instance.
	 * <p>
	 * If {@code scale} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the scaled resolution is invalid, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param scale a {@link Vector2F} instance
	 * @return a new scaled version of this {@code ImageF} instance
	 * @throws IllegalArgumentException thrown if, and only if, the scaled resolution is invalid
	 * @throws NullPointerException thrown if, and only if, {@code scale} is {@code null}
	 */
	public final ImageF scale(final Vector2F scale) {
		return scale(toInt(ceil(getResolutionX() * scale.getX())), toInt(ceil(getResolutionY() * scale.getY())));
	}
	
	/**
	 * Scales this {@code ImageF} instance to the resolution given by {@code resolutionX} and {@code resolutionY}.
	 * <p>
	 * Returns a new scaled version of this {@code ImageF} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @return a new scaled version of this {@code ImageF} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
	public final ImageF scale(final int resolutionX, final int resolutionY) {
		final ImageF oldImage = this;
		final ImageF newImage = oldImage.newImage(resolutionX, resolutionY);
		
		final float scaleX = resolutionX == 0 ? 0.0F : toFloat(getResolutionX()) / toFloat(resolutionX);
		final float scaleY = resolutionY == 0 ? 0.0F : toFloat(getResolutionY()) / toFloat(resolutionY);
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++) {
				newImage.doSetColorRGBA(oldImage.getColorRGBA(x * scaleX, y * scaleY), x, y);
			}
		}
		
		return newImage;
	}
	
	/**
	 * Converts this {@code ImageF} instance into its sepia-representation.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * 
	 * @return this {@code ImageF} instance
	 */
	public final ImageF sepia() {
		return update((color, point) -> Color4F.sepia(color));
	}
	
	/**
	 * Sets the {@link Color3F} of the pixel represented by {@code index} to {@code colorRGB}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If {@code colorRGB} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.setColorRGB(colorRGB, index, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param colorRGB the {@code Color3F} to set
	 * @param index the index of the pixel
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code colorRGB} is {@code null}
	 */
	public final ImageF setColorRGB(final Color3F colorRGB, final int index) {
		return setColorRGB(colorRGB, index, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Sets the {@link Color3F} of the pixel represented by {@code index} to {@code colorRGB}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code colorRGB} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param colorRGB the {@code Color3F} to set
	 * @param index the index of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGB} or {@code pixelOperation} are {@code null}
	 */
	public final ImageF setColorRGB(final Color3F colorRGB, final int index, final PixelOperation pixelOperation) {
		return setColorRGBA(new Color4F(colorRGB), index, pixelOperation);
	}
	
	/**
	 * Sets the {@link Color3F} of the pixel represented by {@code x} and {@code y} to {@code colorRGB}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If {@code colorRGB} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.setColor(colorRGB, x, y, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param colorRGB the {@code Color3F} to set
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code colorRGB} is {@code null}
	 */
	public final ImageF setColorRGB(final Color3F colorRGB, final int x, final int y) {
		return setColorRGB(colorRGB, x, y, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Sets the {@link Color3F} of the pixel represented by {@code x} and {@code y} to {@code colorRGB}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code colorRGB} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param colorRGB the {@code Color3F} to set
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGB} or {@code pixelOperation} are {@code null}
	 */
	public final ImageF setColorRGB(final Color3F colorRGB, final int x, final int y, final PixelOperation pixelOperation) {
		return setColorRGBA(new Color4F(colorRGB), x, y, pixelOperation);
	}
	
	/**
	 * Sets the {@link Color4F} of the pixel represented by {@code index} to {@code colorRGBA}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If {@code colorRGBA} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.setColorRGBA(colorRGBA, index, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param colorRGBA the {@code Color4F} to set
	 * @param index the index of the pixel
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
	public final ImageF setColorRGBA(final Color4F colorRGBA, final int index) {
		return setColorRGBA(colorRGBA, index, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Sets the {@link Color4F} of the pixel represented by {@code index} to {@code colorRGBA}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code colorRGBA} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param colorRGBA the {@code Color4F} to set
	 * @param index the index of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBA} or {@code pixelOperation} are {@code null}
	 */
	public final ImageF setColorRGBA(final Color4F colorRGBA, final int index, final PixelOperation pixelOperation) {
		doChangeBegin();
		doSetColorRGBA(colorRGBA, index, pixelOperation);
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Sets the {@link Color4F} of the pixel represented by {@code x} and {@code y} to {@code colorRGBA}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If {@code colorRGBA} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.setColorRGBA(colorRGBA, x, y, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param colorRGBA the {@code Color4F} to set
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
	public final ImageF setColorRGBA(final Color4F colorRGBA, final int x, final int y) {
		return setColorRGBA(colorRGBA, x, y, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Sets the {@link Color4F} of the pixel represented by {@code x} and {@code y} to {@code colorRGBA}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code colorRGBA} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param colorRGBA the {@code Color4F} to set
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBA} or {@code pixelOperation} are {@code null}
	 */
	public final ImageF setColorRGBA(final Color4F colorRGBA, final int x, final int y, final PixelOperation pixelOperation) {
		doChangeBegin();
		doSetColorRGBA(colorRGBA, x, y, pixelOperation);
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Sets the transparency for this {@code ImageF} instance to {@code transparency}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * 
	 * @param transparency the transparency
	 * @return this {@code ImageF} instance
	 */
	public final ImageF transparency(final float transparency) {
		return update((color, point) -> new Color4F(color.getComponent1(), color.getComponent2(), color.getComponent3(), transparency));
	}
	
	/**
	 * Undoes gamma correction on this {@code ImageF} instance using PBRT.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * 
	 * @return this {@code ImageF} instance
	 */
	public final ImageF undoGammaCorrectionPBRT() {
		return update((color, point) -> Color4F.undoGammaCorrectionPBRT(color));
	}
	
	/**
	 * Undoes gamma correction on this {@code ImageF} instance using sRGB.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * 
	 * @return this {@code ImageF} instance
	 */
	public final ImageF undoGammaCorrectionSRGB() {
		return update((color, point) -> Color4F.undoGammaCorrectionSRGB(color));
	}
	
	/**
	 * Updates this {@code ImageF} instance by applying {@code biFunction} to all pixels.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code biFunction} or the result returned by {@code biFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * imageF.update(biFunction, imageF.getBounds());
	 * }
	 * </pre>
	 * 
	 * @param biFunction a {@code BiFunction} that returns {@link Color4F} instances
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code biFunction} or the result returned by {@code biFunction} are {@code null}
	 */
	public final ImageF update(final BiFunction<Color4F, Point2I, Color4F> biFunction) {
		return update(biFunction, getBounds());
	}
	
	/**
	 * Updates this {@code ImageF} instance by applying {@code biFunction} to all pixels within {@code bounds}.
	 * <p>
	 * Returns this {@code ImageF} instance.
	 * <p>
	 * If either {@code biFunction}, the result returned by {@code biFunction} or {@code bounds} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param biFunction a {@code BiFunction} that returns {@link Color4F} instances
	 * @param bounds a {@link Rectangle2I} instance used as the bounds for the update
	 * @return this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code biFunction}, the result returned by {@code biFunction} or {@code bounds} are {@code null}
	 */
	public final ImageF update(final BiFunction<Color4F, Point2I, Color4F> biFunction, final Rectangle2I bounds) {
		Objects.requireNonNull(biFunction, "biFunction == null");
		Objects.requireNonNull(bounds, "bounds == null");
		
		doChangeBegin();
		
		final Point2I minimum = bounds.getA();
		final Point2I maximum = bounds.getC();
		
		final int minimumX = max(minimum.getX(), 0);
		final int minimumY = max(minimum.getY(), 0);
		final int maximumX = min(maximum.getX(), getResolutionX());
		final int maximumY = min(maximum.getY(), getResolutionY());
		
		for(int y = minimumY; y < maximumY; y++) {
			for(int x = minimumX; x < maximumX; x++) {
				doSetColorRGBA(biFunction.apply(getColorRGBA(x, y), new Point2I(x, y)), x, y);
			}
		}
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Finds the bounds for {@code image} in this {@code ImageF} instance.
	 * <p>
	 * Returns a {@code List} with all {@link Rectangle2I} bounds found for {@code image} in this {@code ImageF} instance.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param image an {@code ImageF} instance
	 * @return a {@code List} with all {@code Rectangle2I} bounds found for {@code image} in this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	public final List<Rectangle2I> findBoundsFor(final ImageF image) {
		Objects.requireNonNull(image, "image == null");
		
		final List<Rectangle2I> rectangles = new ArrayList<>();
		
		for(int y = 0; y < getResolutionY() - image.getResolutionY(); y++) {
			for(int x = 0; x < getResolutionX() - image.getResolutionX(); x++) {
				Rectangle2I rectangle = new Rectangle2I(new Point2I(x, y), new Point2I(x, y));
				
				labelImage:
				if(getColorRGBA(x, y).equals(image.getColorRGBA(0, 0))) {
					for(int imageY = 0; imageY < image.getResolutionY(); imageY++) {
						for(int imageX = 0; imageX < image.getResolutionX(); imageX++) {
							if(!getColorRGBA(x + imageX, y + imageY).equals(image.getColorRGBA(imageX, imageY))) {
								break labelImage;
							}
							
							rectangle = new Rectangle2I(new Point2I(x, y), new Point2I(x + imageX + 1, y + imageY + 1));
						}
					}
					
					rectangles.add(rectangle);
				}
			}
		}
		
		return rectangles;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code ImageF} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @return a new {@code ImageF} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
	protected abstract ImageF newImage(final int resolutionX, final int resolutionY);
	
	/**
	 * Sets the {@link Color4F} of the pixel represented by {@code index} to {@code colorRGBA}.
	 * <p>
	 * If {@code colorRGBA} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorRGBA the {@code Color4F} to set
	 * @param index the index of the pixel
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
	protected abstract void putColorRGBA(final Color4F colorRGBA, final int index);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private ImageF doSetColorRGBA(final Color4F colorRGBA, final int index) {
		return doSetColorRGBA(colorRGBA, index, PixelOperation.NO_CHANGE);
	}
	
	private ImageF doSetColorRGBA(final Color4F colorRGBA, final int index, final PixelOperation pixelOperation) {
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		Objects.requireNonNull(pixelOperation, "pixelOperation == null");
		
		final int resolution = getResolution();
		
		final int indexTransformed = pixelOperation.getIndex(index, resolution);
		
		if(indexTransformed >= 0 && indexTransformed < resolution) {
			if(isChangeHistoryEnabled()) {
				doChangeCreate(colorRGBA, getColorRGBA(indexTransformed), indexTransformed);
			}
			
			putColorRGBA(colorRGBA, indexTransformed);
		}
		
		return this;
	}
	
	private ImageF doSetColorRGBA(final Color4F colorRGBA, final int x, final int y) {
		return doSetColorRGBA(colorRGBA, x, y, PixelOperation.NO_CHANGE);
	}
	
	private ImageF doSetColorRGBA(final Color4F colorRGBA, final int x, final int y, final PixelOperation pixelOperation) {
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		Objects.requireNonNull(pixelOperation, "pixelOperation == null");
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		final int xTransformed = pixelOperation.getX(x, resolutionX);
		final int yTransformed = pixelOperation.getY(y, resolutionY);
		
		if(xTransformed >= 0 && xTransformed < resolutionX && yTransformed >= 0 && yTransformed < resolutionY) {
			final int index = yTransformed * resolutionX + xTransformed;
			
			if(isChangeHistoryEnabled()) {
				doChangeCreate(colorRGBA, getColorRGBA(index), index);
			}
			
			putColorRGBA(colorRGBA, index);
		}
		
		return this;
	}
	
	private void doChangeBegin() {
		if(isChangeHistoryEnabled()) {
			final
			ChangeCombiner changeCombiner = this.changeCombiner;
			changeCombiner.clear();
		}
	}
	
	private void doChangeCreate(final Color4F newColorRGBA, final Color4F oldColorRGBA, final int index) {
		if(isChangeHistoryEnabled()) {
			final
			ChangeCombiner changeCombiner = this.changeCombiner;
			changeCombiner.add(new Change(() -> putColorRGBA(newColorRGBA, index), () -> putColorRGBA(oldColorRGBA, index)));
		}
	}
	
	private void doChangeEnd() {
		if(isChangeHistoryEnabled()) {
			final ChangeCombiner changeCombiner = this.changeCombiner;
			
			final Change change = changeCombiner.toChange();
			
			final
			ChangeHistory changeHistory = getChangeHistory();
			changeHistory.push(change);
			
			changeCombiner.clear();
		}
	}
	
	private void doDrawLineSegment(final LineSegment2I lineSegment, final BiFunction<Color4F, Point2I, Color4F> biFunction) {
		final Rectangle2I rectangle = new Rectangle2I(new Point2I(), new Point2I(getResolutionX(), getResolutionY()));
		
		final Point2I[] scanline = Rasterizer2I.rasterize(lineSegment, rectangle);
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		for(final Point2I point : scanline) {
			final int x = point.getX();
			final int y = point.getY();
			
			if(x >= 0 && x < resolutionX && y >= 0 && y < resolutionY) {
				final Color4F oldColorRGBA = getColorRGBA(x, y);
				final Color4F newColorRGBA = Objects.requireNonNull(biFunction.apply(oldColorRGBA, point));
				
				doSetColorRGBA(newColorRGBA, point.getX(), point.getY());
			}
		}
	}
}