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
package org.dayflower.image;

import static org.dayflower.utility.Doubles.NaN;
import static org.dayflower.utility.Doubles.abs;
import static org.dayflower.utility.Doubles.ceil;
import static org.dayflower.utility.Doubles.floor;
import static org.dayflower.utility.Doubles.maxOrNaN;
import static org.dayflower.utility.Doubles.min;
import static org.dayflower.utility.Doubles.minOrNaN;
import static org.dayflower.utility.Doubles.sqrt;
import static org.dayflower.utility.Doubles.toDouble;
import static org.dayflower.utility.Ints.max;
import static org.dayflower.utility.Ints.min;
import static org.dayflower.utility.Ints.toInt;

import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import org.dayflower.change.Change;
import org.dayflower.change.ChangeCombiner;
import org.dayflower.change.ChangeHistory;
import org.dayflower.color.ArrayComponentOrder;
import org.dayflower.color.Color3D;
import org.dayflower.color.Color4D;
import org.dayflower.color.ColorSpaceD;
import org.dayflower.geometry.AngleD;
import org.dayflower.geometry.Point2D;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.Shape2I;
import org.dayflower.geometry.Vector2D;
import org.dayflower.geometry.shape.Rectangle2D;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.noise.SimplexNoiseD;

import org.macroing.java.util.function.TriFunction;

/**
 * An {@code ImageD} is an extension of {@link Image} that adds additional methods that operates on {@code double}-based data types.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class ImageD extends Image {
	private final ChangeCombiner changeCombiner;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ImageD} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	protected ImageD(final int resolutionX, final int resolutionY) {
		super(resolutionX, resolutionY);
		
		this.changeCombiner = new ChangeCombiner();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link Color3D} of the pixel represented by {@code point}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color3D} instances.
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
	 * @param point a {@link Point2D} instance that contains the coordinates of the pixel
	 * @return the {@code Color3D} of the pixel represented by {@code point}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final Color3D getColorRGB(final Point2D point) {
		return getColorRGB(point, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color3D} of the pixel represented by {@code point}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color3D} instances.
	 * <p>
	 * If either {@code point} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGB(point.x, point.y, pixelOperation);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2D} instance that contains the coordinates of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color3D} of the pixel represented by {@code point}
	 * @throws NullPointerException thrown if, and only if, either {@code point} or {@code pixelOperation} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final Color3D getColorRGB(final Point2D point, final PixelOperation pixelOperation) {
		return getColorRGB(point.x, point.y, pixelOperation);
	}
	
	/**
	 * Returns the {@link Color3D} of the pixel represented by {@code point}.
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
	 * @return the {@code Color3D} of the pixel represented by {@code point}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final Color3D getColorRGB(final Point2I point) {
		return getColorRGB(point, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color3D} of the pixel represented by {@code point}.
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
	 * @return the {@code Color3D} of the pixel represented by {@code point}
	 * @throws NullPointerException thrown if, and only if, either {@code point} or {@code pixelOperation} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final Color3D getColorRGB(final Point2I point, final PixelOperation pixelOperation) {
		return getColorRGB(point.getX(), point.getY(), pixelOperation);
	}
	
	/**
	 * Returns the {@link Color3D} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color3D} instances.
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
	 * @return the {@code Color3D} of the pixel represented by {@code x} and {@code y}
	 */
//	TODO: Add Unit Tests!
	public final Color3D getColorRGB(final double x, final double y) {
		return getColorRGB(x, y, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color3D} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color3D} instances.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color3D} of the pixel represented by {@code x} and {@code y}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final Color3D getColorRGB(final double x, final double y, final PixelOperation pixelOperation) {
		final int minimumX = toInt(floor(x));
		final int maximumX = toInt(ceil(x));
		
		final int minimumY = toInt(floor(y));
		final int maximumY = toInt(ceil(y));
		
		if(minimumX == maximumX && minimumY == maximumY) {
			return getColorRGB(minimumX, minimumY, pixelOperation);
		}
		
		final Color3D color00 = getColorRGB(minimumX, minimumY, pixelOperation);
		final Color3D color01 = getColorRGB(maximumX, minimumY, pixelOperation);
		final Color3D color10 = getColorRGB(minimumX, maximumY, pixelOperation);
		final Color3D color11 = getColorRGB(maximumX, maximumY, pixelOperation);
		
		final double xFactor = x - minimumX;
		final double yFactor = y - minimumY;
		
		final Color3D color = Color3D.blend(Color3D.blend(color00, color01, xFactor), Color3D.blend(color10, color11, xFactor), yFactor);
		
		return color;
	}
	
	/**
	 * Returns the {@link Color3D} of the pixel represented by {@code index}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGB(index, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param index the index of the pixel
	 * @return the {@code Color3D} of the pixel represented by {@code index}
	 */
//	TODO: Add Unit Tests!
	public final Color3D getColorRGB(final int index) {
		return getColorRGB(index, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color3D} of the pixel represented by {@code index}.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param index the index of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color3D} of the pixel represented by {@code index}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final Color3D getColorRGB(final int index, final PixelOperation pixelOperation) {
		return new Color3D(getColorRGBA(index, pixelOperation));
	}
	
	/**
	 * Returns the {@link Color3D} of the pixel represented by {@code x} and {@code y}.
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
	 * @return the {@code Color3D} of the pixel represented by {@code x} and {@code y}
	 */
//	TODO: Add Unit Tests!
	public final Color3D getColorRGB(final int x, final int y) {
		return getColorRGB(x, y, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color3D} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color3D} of the pixel represented by {@code x} and {@code y}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final Color3D getColorRGB(final int x, final int y, final PixelOperation pixelOperation) {
		return new Color3D(getColorRGBA(x, y, pixelOperation));
	}
	
	/**
	 * Returns the {@link Color4D} of the pixel represented by {@code point}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color4D} instances.
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
	 * @param point a {@link Point2D} instance with the coordinates of the pixel
	 * @return the {@code Color4D} of the pixel represented by {@code point}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final Color4D getColorRGBA(final Point2D point) {
		return getColorRGBA(point, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color4D} of the pixel represented by {@code point}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color4D} instances.
	 * <p>
	 * If either {@code point} or {@code function} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGBA(point.x, point.y, function);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2D} instance with the coordinates of the pixel
	 * @param function a {@code Function} that returns a {@code Color4D} instance if any of the points are outside the bounds
	 * @return the {@code Color4D} of the pixel represented by {@code point}
	 * @throws NullPointerException thrown if, and only if, either {@code point} or {@code function} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final Color4D getColorRGBA(final Point2D point, final Function<Point2I, Color4D> function) {
		return getColorRGBA(point.x, point.y, function);
	}
	
	/**
	 * Returns the {@link Color4D} of the pixel represented by {@code point}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color4D} instances.
	 * <p>
	 * If either {@code point} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGBA(point.x, point.y, pixelOperation);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2D} instance with the coordinates of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color4D} of the pixel represented by {@code point}
	 * @throws NullPointerException thrown if, and only if, either {@code point} or {@code pixelOperation} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final Color4D getColorRGBA(final Point2D point, final PixelOperation pixelOperation) {
		return getColorRGBA(point.x, point.y, pixelOperation);
	}
	
	/**
	 * Returns the {@link Color4D} of the pixel represented by {@code point}.
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
	 * @return the {@code Color4D} of the pixel represented by {@code point}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final Color4D getColorRGBA(final Point2I point) {
		return getColorRGBA(point, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color4D} of the pixel represented by {@code point}.
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
	 * @param function a {@code Function} that returns a {@code Color4D} instance if any of the points are outside the bounds
	 * @return the {@code Color4D} of the pixel represented by {@code point}
	 * @throws NullPointerException thrown if, and only if, either {@code point} or {@code function} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final Color4D getColorRGBA(final Point2I point, final Function<Point2I, Color4D> function) {
		return getColorRGBA(point.getX(), point.getY(), function);
	}
	
	/**
	 * Returns the {@link Color4D} of the pixel represented by {@code point}.
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
	 * @return the {@code Color4D} of the pixel represented by {@code point}
	 * @throws NullPointerException thrown if, and only if, either {@code point} or {@code pixelOperation} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final Color4D getColorRGBA(final Point2I point, final PixelOperation pixelOperation) {
		return getColorRGBA(point.getX(), point.getY(), pixelOperation);
	}
	
	/**
	 * Returns the {@link Color4D} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color4D} instances.
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
	 * @return the {@code Color4D} of the pixel represented by {@code x} and {@code y}
	 */
//	TODO: Add Unit Tests!
	public final Color4D getColorRGBA(final double x, final double y) {
		return getColorRGBA(x, y, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color4D} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color4D} instances.
	 * <p>
	 * If {@code function} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param function a {@code Function} that returns a {@code Color4D} instance if {@code x} or {@code y} are outside the bounds
	 * @return the {@code Color4D} of the pixel represented by {@code x} and {@code y}
	 * @throws NullPointerException thrown if, and only if, {@code function} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final Color4D getColorRGBA(final double x, final double y, final Function<Point2I, Color4D> function) {
		Objects.requireNonNull(function, "function == null");
		
		final int minimumX = toInt(floor(x));
		final int maximumX = toInt(ceil(x));
		
		final int minimumY = toInt(floor(y));
		final int maximumY = toInt(ceil(y));
		
		if(minimumX == maximumX && minimumY == maximumY) {
			return getColorRGBA(minimumX, minimumY, function);
		}
		
		final Color4D color00 = getColorRGBA(minimumX, minimumY, function);
		final Color4D color01 = getColorRGBA(maximumX, minimumY, function);
		final Color4D color10 = getColorRGBA(minimumX, maximumY, function);
		final Color4D color11 = getColorRGBA(maximumX, maximumY, function);
		
		final double xFactor = x - minimumX;
		final double yFactor = y - minimumY;
		
		final Color4D color = Color4D.blend(Color4D.blend(color00, color01, xFactor), Color4D.blend(color10, color11, xFactor), yFactor);
		
		return color;
	}
	
	/**
	 * Returns the {@link Color4D} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color4D} instances.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color4D} of the pixel represented by {@code x} and {@code y}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final Color4D getColorRGBA(final double x, final double y, final PixelOperation pixelOperation) {
		final int minimumX = toInt(floor(x));
		final int maximumX = toInt(ceil(x));
		
		final int minimumY = toInt(floor(y));
		final int maximumY = toInt(ceil(y));
		
		if(minimumX == maximumX && minimumY == maximumY) {
			return getColorRGBA(minimumX, minimumY, pixelOperation);
		}
		
		final Color4D color00 = getColorRGBA(minimumX, minimumY, pixelOperation);
		final Color4D color01 = getColorRGBA(maximumX, minimumY, pixelOperation);
		final Color4D color10 = getColorRGBA(minimumX, maximumY, pixelOperation);
		final Color4D color11 = getColorRGBA(maximumX, maximumY, pixelOperation);
		
		final double xFactor = x - minimumX;
		final double yFactor = y - minimumY;
		
		final Color4D color = Color4D.blend(Color4D.blend(color00, color01, xFactor), Color4D.blend(color10, color11, xFactor), yFactor);
		
		return color;
	}
	
	/**
	 * Returns the {@link Color4D} of the pixel represented by {@code index}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGBA(index, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param index the index of the pixel
	 * @return the {@code Color4D} of the pixel represented by {@code index}
	 */
//	TODO: Add Unit Tests!
	public final Color4D getColorRGBA(final int index) {
		return getColorRGBA(index, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color4D} of the pixel represented by {@code index}.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param index the index of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color4D} of the pixel represented by {@code index}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
	public abstract Color4D getColorRGBA(final int index, final PixelOperation pixelOperation);
	
	/**
	 * Returns the {@link Color4D} of the pixel represented by {@code x} and {@code y}.
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
	 * @return the {@code Color4D} of the pixel represented by {@code x} and {@code y}
	 */
//	TODO: Add Unit Tests!
	public final Color4D getColorRGBA(final int x, final int y) {
		return getColorRGBA(x, y, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color4D} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * If {@code function} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param function a {@code Function} that returns a {@code Color4D} instance if {@code x} or {@code y} are outside the bounds
	 * @return the {@code Color4D} of the pixel represented by {@code x} and {@code y}
	 * @throws NullPointerException thrown if, and only if, {@code function} is {@code null}
	 */
	public abstract Color4D getColorRGBA(final int x, final int y, final Function<Point2I, Color4D> function);
	
	/**
	 * Returns the {@link Color4D} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color4D} of the pixel represented by {@code x} and {@code y}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
	public abstract Color4D getColorRGBA(final int x, final int y, final PixelOperation pixelOperation);
	
	/**
	 * Blends this {@code ImageD} instance over {@code image}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param image an {@code ImageD} instance that acts as a background
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD blendOver(final ImageD image) {
		return fillImage(image, image.getBounds(), getBounds(), (sourceColorRGBA, targetColorRGBA, targetPoint) -> Color4D.blendOver(targetColorRGBA, sourceColorRGBA));
	}
	
	/**
	 * Clears this {@code ImageD} instance with a {@link Color4D} of {@code Color4D.BLACK}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.clear(Color4D.BLACK);
	 * }
	 * </pre>
	 * 
	 * @return this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	public final ImageD clear() {
		return clear(Color4D.BLACK);
	}
	
	/**
	 * Clears this {@code ImageD} instance with a {@link Color3D} of {@code colorRGB}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If {@code colorRGB} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.clear(new Color4D(colorRGB));
	 * }
	 * </pre>
	 * 
	 * @param colorRGB the {@code Color3D} to clear with
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code colorRGB} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD clear(final Color3D colorRGB) {
		return clear(new Color4D(colorRGB));
	}
	
	/**
	 * Clears this {@code ImageD} instance with a {@link Color4D} of {@code colorRGBA}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If {@code colorRGBA} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorRGBA the {@code Color4D} to clear with
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD clear(final Color4D colorRGBA) {
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
	 * Returns a copy of this {@code ImageD} instance.
	 * <p>
	 * This method may or may not copy everything stored in an {@code ImageD} instance.
	 * <p>
	 * The data being copied is that which can be obtained by a call to {@link #getColorRGBA(int, int)}. This means that the change history and additional data stored per pixel is not copied.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * imageD.copy(imageD.getBounds());
	 * }
	 * </pre>
	 * 
	 * @return a copy of this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public final ImageD copy() {
		return copy(getBounds());
	}
	
	/**
	 * Returns a copy of this {@code ImageD} instance within {@code shape}.
	 * <p>
	 * If {@code shape} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method may or may not copy everything stored in an {@code ImageD} instance.
	 * <p>
	 * The data being copied is that which can be obtained by a call to {@link #getColorRGBA(int, int)}. This means that the change history and additional data stored per pixel is not copied.
	 * 
	 * @param shape a {@link Shape2I} instance that represents the shape within this {@code ImageD} instance to copy
	 * @return a copy of this {@code ImageD} instance within {@code shape}
	 * @throws NullPointerException thrown if, and only if, {@code shape} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public final ImageD copy(final Shape2I shape) {
		final Point2I maximum = shape.getMaximum();
		final Point2I minimum = shape.getMinimum();
		
		final int resolutionX = maximum.getX() - minimum.getX() + 1;
		final int resolutionY = maximum.getY() - minimum.getY() + 1;
		
		final ImageD image = newImage(resolutionX, resolutionY);
		
		final List<Point2I> points = shape.findPoints();
		
		for(final Point2I point : points) {
			final int sourceX = point.getX();
			final int sourceY = point.getY();
			
			final int targetX = sourceX - minimum.getX();
			final int targetY = sourceY - minimum.getY();
			
			final Color4D colorRGBA = getColorRGBA(sourceX, sourceY);
			
			image.setColorRGBA(colorRGBA, targetX, targetY);
		}
		
		return image;
	}
	
	/**
	 * Draws {@code shape} to this {@code ImageD} instance with {@code Color4D.BLACK} as its color.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If {@code shape} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawShape(shape, Color4D.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param shape the {@link Shape2I} to draw
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code shape} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD drawShape(final Shape2I shape) {
		return drawShape(shape, Color4D.BLACK);
	}
	
	/**
	 * Draws {@code shape} to this {@code ImageD} instance with {@link Color4D} instances returned by {@code biFunction} as its color.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code shape} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape the {@link Shape2I} to draw
	 * @param biFunction a {@code BiFunction} that returns {@code Color4D} instances to use as its color
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code shape} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD drawShape(final Shape2I shape, final BiFunction<Color4D, Point2I, Color4D> biFunction) {
		Objects.requireNonNull(shape, "shape == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		doChangeBegin();
		
		shape.findPointsOfIntersection(getBounds(), true).forEach(point -> doSetColorRGBA(Objects.requireNonNull(biFunction.apply(getColorRGBA(point.getX(), point.getY()), point)), point.getX(), point.getY()));
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Draws {@code shape} to this {@code ImageD} instance with {@code colorRGB} as its color.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code shape} or {@code colorRGB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawShape(shape, new Color4D(colorRGB));
	 * }
	 * </pre>
	 * 
	 * @param shape the {@link Shape2I} to draw
	 * @param colorRGB the {@link Color3D} to use as its color
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code shape} or {@code colorRGB} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD drawShape(final Shape2I shape, final Color3D colorRGB) {
		Objects.requireNonNull(shape, "shape == null");
		Objects.requireNonNull(colorRGB, "colorRGB == null");
		
		return drawShape(shape, new Color4D(colorRGB));
	}
	
	/**
	 * Draws {@code shape} to this {@code ImageD} instance with {@code colorRGBA} as its color.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code shape} or {@code colorRGBA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawShape(shape, (color, point) -> colorRGBA);
	 * }
	 * </pre>
	 * 
	 * @param shape the {@link Shape2I} to draw
	 * @param colorRGBA the {@link Color4D} to use as its color
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code shape} or {@code colorRGBA} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD drawShape(final Shape2I shape, final Color4D colorRGBA) {
		Objects.requireNonNull(shape, "shape == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		return drawShape(shape, (color, point) -> colorRGBA);
	}
	
	/**
	 * Draws everything except for {@code shape} in this {@code ImageD} instance with {@code Color4D.BLACK} as its color.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If {@code shape} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawShapeComplement(shape, Color4D.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param shape the {@link Shape2I} not to draw
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code shape} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD drawShapeComplement(final Shape2I shape) {
		return drawShapeComplement(shape, Color4D.BLACK);
	}
	
	/**
	 * Draws everything except for {@code shape} in this {@code ImageD} instance with {@link Color4D} instances returned by {@code biFunction} as its color.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code shape} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape the {@link Shape2I} not to draw
	 * @param biFunction a {@code BiFunction} that returns {@code Color4D} instances to use as its color
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code shape} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD drawShapeComplement(final Shape2I shape, final BiFunction<Color4D, Point2I, Color4D> biFunction) {
		Objects.requireNonNull(shape, "shape == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		doChangeBegin();
		
		shape.findPointsOfComplement(getBounds(), true).forEach(point -> doSetColorRGBA(Objects.requireNonNull(biFunction.apply(getColorRGBA(point.getX(), point.getY()), point)), point.getX(), point.getY()));
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Draws everything except for {@code shape} in this {@code ImageD} instance with {@code colorRGB} as its color.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code shape} or {@code colorRGB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawShapeComplement(shape, new Color4D(colorRGB));
	 * }
	 * </pre>
	 * 
	 * @param shape the {@link Shape2I} not to draw
	 * @param colorRGB the {@link Color3D} to use as its color
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code shape} or {@code colorRGB} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD drawShapeComplement(final Shape2I shape, final Color3D colorRGB) {
		Objects.requireNonNull(shape, "shape == null");
		Objects.requireNonNull(colorRGB, "colorRGB == null");
		
		return drawShapeComplement(shape, new Color4D(colorRGB));
	}
	
	/**
	 * Draws everything except for {@code shape} in this {@code ImageD} instance with {@code colorRGBA} as its color.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code shape} or {@code colorRGBA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawShapeComplement(shape, (color, point) -> colorRGBA);
	 * }
	 * </pre>
	 * 
	 * @param shape the {@link Shape2I} not to draw
	 * @param colorRGBA the {@link Color4D} to use as its color
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code shape} or {@code colorRGBA} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD drawShapeComplement(final Shape2I shape, final Color4D colorRGBA) {
		Objects.requireNonNull(shape, "shape == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		return drawShapeComplement(shape, (color, point) -> colorRGBA);
	}
	
	/**
	 * Fills a gradient in this {@code ImageD} instance.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillGradient(Color3D.BLACK, Color3D.RED, Color3D.GREEN, Color3D.YELLOW);
	 * }
	 * </pre>
	 * 
	 * @return this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	public final ImageD fillGradient() {
		return fillGradient(Color3D.BLACK, Color3D.RED, Color3D.GREEN, Color3D.YELLOW);
	}
	
	/**
	 * Fills a gradient in this {@code ImageD} instance.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a the {@link Color3D} instance in the top left corner
	 * @param b the {@code Color3D} instance in the top right corner
	 * @param c the {@code Color3D} instance in the bottom left corner
	 * @param d the {@code Color3D} instance in the bottom right corner
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD fillGradient(final Color3D a, final Color3D b, final Color3D c, final Color3D d) {
		Objects.requireNonNull(a, "a == null");
		Objects.requireNonNull(b, "b == null");
		Objects.requireNonNull(c, "c == null");
		Objects.requireNonNull(d, "d == null");
		
		final double resolutionX = getResolutionX();
		final double resolutionY = getResolutionY();
		
		return update((color, point) -> {
			final double tX = 1.0D / resolutionX * point.getX();
			final double tY = 1.0D / resolutionY * point.getY();
			
			return new Color4D(Color3D.blend(Color3D.blend(a, b, tX), Color3D.blend(c, d, tX), tY));
		});
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code ImageD} instance.
	 * <p>
	 * Returns this {@code ImageD} instance.
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
	 * @param sourceImage the {@code ImageD} to fill
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code sourceImage} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD fillImage(final ImageD sourceImage) {
		return fillImage(sourceImage, sourceImage.getBounds());
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code ImageD} instance.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code sourceImage} or {@code targetPosition} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillImage(sourceImage, targetPosition, (sourceColorRGBA, targetColorRGBA, targetPoint) -> Color4D.blendOver(sourceColorRGBA, targetColorRGBA));
	 * }
	 * </pre>
	 * 
	 * @param sourceImage the {@code ImageD} to fill
	 * @param targetPosition a {@link Point2I} that represents the position in this {@code ImageD} instance to start filling {@code sourceImage}
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code sourceImage} or {@code targetPosition} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD fillImage(final ImageD sourceImage, final Point2I targetPosition) {
		return fillImage(sourceImage, targetPosition, (sourceColorRGBA, targetColorRGBA, targetPoint) -> Color4D.blendOver(sourceColorRGBA, targetColorRGBA));
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code ImageD} instance.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code sourceImage}, {@code targetPosition} or {@code triFunction} are {@code null} or {@code triFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sourceImage the {@code ImageD} to fill
	 * @param targetPosition a {@link Point2I} that represents the position in this {@code ImageD} instance to start filling {@code sourceImage}
	 * @param triFunction a {@code TriFunction} that returns {@code Color4D} instances to use as its color
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code sourceImage}, {@code targetPosition} or {@code triFunction} are {@code null} or {@code triFunction} returns {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD fillImage(final ImageD sourceImage, final Point2I targetPosition, final TriFunction<Color4D, Color4D, Point2I, Color4D> triFunction) {
		final Rectangle2I sourceBounds = sourceImage.getBounds();
		final Rectangle2I targetBounds = new Rectangle2I(targetPosition, new Point2I(targetPosition.getX() + (sourceBounds.getC().getX() - sourceBounds.getA().getX()), targetPosition.getY() + (sourceBounds.getC().getY() - sourceBounds.getA().getY())));
		
		return fillImage(sourceImage, sourceBounds, targetBounds, triFunction);
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code ImageD} instance.
	 * <p>
	 * Returns this {@code ImageD} instance.
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
	 * @param sourceImage the {@code ImageD} to fill
	 * @param sourceBounds a {@link Rectangle2I} that represents the bounds of the region in {@code sourceImage} to use
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code sourceImage} or {@code sourceBounds} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD fillImage(final ImageD sourceImage, final Rectangle2I sourceBounds) {
		return fillImage(sourceImage, sourceBounds, getBounds());
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code ImageD} instance.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code sourceImage}, {@code sourceBounds} or {@code targetBounds} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillImage(sourceImage, sourceBounds, targetBounds, (sourceColorRGBA, targetColorRGBA, targetPoint) -> Color4D.blendOver(sourceColorRGBA, targetColorRGBA));
	 * }
	 * </pre>
	 * 
	 * @param sourceImage the {@code ImageD} to fill
	 * @param sourceBounds a {@link Rectangle2I} that represents the bounds of the region in {@code sourceImage} to use
	 * @param targetBounds a {@code Rectangle2I} that represents the bounds of the region in this {@code ImageD} instance to use
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code sourceImage}, {@code sourceBounds} or {@code targetBounds} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD fillImage(final ImageD sourceImage, final Rectangle2I sourceBounds, final Rectangle2I targetBounds) {
		return fillImage(sourceImage, sourceBounds, targetBounds, (sourceColorRGBA, targetColorRGBA, targetPoint) -> Color4D.blendOver(sourceColorRGBA, targetColorRGBA));
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code ImageD} instance with {@link Color4D} instances returned by {@code triFunction} as its color.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code sourceImage}, {@code sourceBounds}, {@code targetBounds} or {@code triFunction} are {@code null} or {@code triFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sourceImage the {@code ImageD} to fill
	 * @param sourceBounds a {@link Rectangle2I} that represents the bounds of the region in {@code sourceImage} to use
	 * @param targetBounds a {@code Rectangle2I} that represents the bounds of the region in this {@code ImageD} instance to use
	 * @param triFunction a {@code TriFunction} that returns {@code Color4D} instances to use as its color
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code sourceImage}, {@code sourceBounds}, {@code targetBounds} or {@code triFunction} are {@code null} or {@code triFunction} returns {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD fillImage(final ImageD sourceImage, final Rectangle2I sourceBounds, final Rectangle2I targetBounds, final TriFunction<Color4D, Color4D, Point2I, Color4D> triFunction) {
		Objects.requireNonNull(sourceImage, "sourceImage == null");
		Objects.requireNonNull(sourceBounds, "sourceBounds == null");
		Objects.requireNonNull(targetBounds, "targetBounds == null");
		Objects.requireNonNull(triFunction, "triFunction == null");
		
		doChangeBegin();
		
		final ImageD targetImage = this;
		
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
					final Color4D sourceColorRGBA = sourceImage.getColorRGBA(sourceX, sourceY);
					final Color4D targetColorRGBA = targetImage.getColorRGBA(targetX, targetY);
					final Color4D colorRGBA = Objects.requireNonNull(triFunction.apply(sourceColorRGBA, targetColorRGBA, new Point2I(targetX, targetY)));
					
					targetImage.doSetColorRGBA(colorRGBA, targetX, targetY);
				}
			}
		}
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Fills the region of pixels that are color-connected to the pixel at {@code x} and {@code y} with {@link Color4D} instances provided by {@code biFunction}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If {@code biFunction} is {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This operation works in a similar way to the Bucket Fill tool in Microsoft Paint.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillRegion(x, y, biFunction, (colorRGBA, point) -> true);
	 * }
	 * </pre>
	 * 
	 * @param x the X-component of the pixel to start at
	 * @param y the Y-component of the pixel to start at
	 * @param biFunction a {@code BiFunction} that returns {@code Color4D} instances for each pixel in the region
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code biFunction} is {@code null} or {@code biFunction} returns {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD fillRegion(final int x, final int y, final BiFunction<Color4D, Point2I, Color4D> biFunction) {
		return fillRegion(x, y, biFunction, (colorRGBA, point) -> true);
	}
	
	/**
	 * Fills the region of pixels that are color-connected to the pixel at {@code x} and {@code y} and accepted by {@code biPredicate} with {@link Color4D} instances provided by {@code biFunction}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code biFunction} or {@code biPredicate} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This operation works in a similar way to the Bucket Fill tool in Microsoft Paint.
	 * 
	 * @param x the X-component of the pixel to start at
	 * @param y the Y-component of the pixel to start at
	 * @param biFunction a {@code BiFunction} that returns {@code Color4D} instances for each pixel in the region
	 * @param biPredicate a {@code BiPredicate} that accepts or rejects pixels
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code biFunction} or {@code biPredicate} are {@code null} or {@code biFunction} returns {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD fillRegion(final int x, final int y, final BiFunction<Color4D, Point2I, Color4D> biFunction, final BiPredicate<Color4D, Point2I> biPredicate) {
		Objects.requireNonNull(biFunction, "biFunction == null");
		Objects.requireNonNull(biPredicate, "biPredicate == null");
		
		doChangeBegin();
		doFillRegion(x, y, biFunction, biPredicate, getColorRGBA(x, y));
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Fills {@code shape} in this {@code ImageD} instance with {@code Color4D.BLACK} as its color.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If {@code shape} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillShape(shape, Color4D.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param shape the {@link Shape2I} to fill
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code shape} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD fillShape(final Shape2I shape) {
		return fillShape(shape, Color4D.BLACK);
	}
	
	/**
	 * Fills {@code shape} in this {@code ImageD} instance with {@link Color4D} instances returned by {@code biFunction} as its color.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code shape} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape the {@link Shape2I} to fill
	 * @param biFunction a {@code BiFunction} that returns {@code Color4D} instances to use as its color
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code shape} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD fillShape(final Shape2I shape, final BiFunction<Color4D, Point2I, Color4D> biFunction) {
		Objects.requireNonNull(shape, "shape == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		doChangeBegin();
		
		shape.findPointsOfIntersection(getBounds()).forEach(point -> doSetColorRGBA(Objects.requireNonNull(biFunction.apply(getColorRGBA(point.getX(), point.getY()), point)), point.getX(), point.getY()));
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Fills {@code shape} in this {@code ImageD} instance with {@code colorRGB} as its color.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code shape} or {@code colorRGB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillShape(shape, new Color4D(colorRGB));
	 * }
	 * </pre>
	 * 
	 * @param shape the {@link Shape2I} to fill
	 * @param colorRGB the {@link Color3D} to use as its color
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code shape} or {@code colorRGB} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD fillShape(final Shape2I shape, final Color3D colorRGB) {
		Objects.requireNonNull(shape, "shape == null");
		Objects.requireNonNull(colorRGB, "colorRGB == null");
		
		return fillShape(shape, new Color4D(colorRGB));
	}
	
	/**
	 * Fills {@code shape} in this {@code ImageD} instance with {@code colorRGBA} as its color.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code shape} or {@code colorRGBA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillShape(shape, (color, point) -> colorRGBA);
	 * }
	 * </pre>
	 * 
	 * @param shape the {@link Shape2I} to fill
	 * @param colorRGBA the {@link Color4D} to use as its color
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code shape} or {@code colorRGBA} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD fillShape(final Shape2I shape, final Color4D colorRGBA) {
		Objects.requireNonNull(shape, "shape == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		return fillShape(shape, (color, point) -> colorRGBA);
	}
	
	/**
	 * Fills everything except for {@code shape} in this {@code ImageD} instance with {@code Color4D.BLACK} as its color.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If {@code shape} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillShapeComplement(shape, Color4D.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param shape the {@link Shape2I} not to fill
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code shape} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD fillShapeComplement(final Shape2I shape) {
		return fillShapeComplement(shape, Color4D.BLACK);
	}
	
	/**
	 * Fills everything except for {@code shape} in this {@code ImageD} instance with {@link Color4D} instances returned by {@code biFunction} as its color.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code shape} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape the {@link Shape2I} not to fill
	 * @param biFunction a {@code BiFunction} that returns {@code Color4D} instances to use as its color
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code shape} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD fillShapeComplement(final Shape2I shape, final BiFunction<Color4D, Point2I, Color4D> biFunction) {
		Objects.requireNonNull(shape, "shape == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		doChangeBegin();
		
		shape.findPointsOfComplement(getBounds()).forEach(point -> doSetColorRGBA(Objects.requireNonNull(biFunction.apply(getColorRGBA(point.getX(), point.getY()), point)), point.getX(), point.getY()));
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Fills everything except for {@code shape} in this {@code ImageD} instance with {@code colorRGB} as its color.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code shape} or {@code colorRGB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillShapeComplement(shape, new Color4D(colorRGB));
	 * }
	 * </pre>
	 * 
	 * @param shape the {@link Shape2I} not to fill
	 * @param colorRGB the {@link Color3D} to use as its color
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code shape} or {@code colorRGB} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD fillShapeComplement(final Shape2I shape, final Color3D colorRGB) {
		Objects.requireNonNull(shape, "shape == null");
		Objects.requireNonNull(colorRGB, "colorRGB == null");
		
		return fillShapeComplement(shape, new Color4D(colorRGB));
	}
	
	/**
	 * Fills everything except for {@code shape} in this {@code ImageD} instance with {@code colorRGBA} as its color.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code shape} or {@code colorRGBA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillShapeComplement(shape, (color, point) -> colorRGBA);
	 * }
	 * </pre>
	 * 
	 * @param shape the {@link Shape2I} not to fill
	 * @param colorRGBA the {@link Color4D} to use as its color
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code shape} or {@code colorRGBA} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD fillShapeComplement(final Shape2I shape, final Color4D colorRGBA) {
		Objects.requireNonNull(shape, "shape == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		return fillShapeComplement(shape, (color, point) -> colorRGBA);
	}
	
	/**
	 * Fills this {@code ImageD} instance with {@link Color4D} instances that are generated using a Simplex-based fractional Brownian motion (fBm) algorithm.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillSimplexFractionalBrownianMotion(new Color3D(0.75D, 0.5D, 0.75D));
	 * }
	 * </pre>
	 * 
	 * @return this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	public final ImageD fillSimplexFractionalBrownianMotion() {
		return fillSimplexFractionalBrownianMotion(new Color3D(0.75D, 0.5D, 0.75D));
	}
	
	/**
	 * Fills this {@code ImageD} instance with {@link Color4D} instances that are generated using a Simplex-based fractional Brownian motion (fBm) algorithm.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If {@code baseColor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillSimplexFractionalBrownianMotion(baseColor, 5.0D, 0.5D, 16);
	 * }
	 * </pre>
	 * 
	 * @param baseColor a {@link Color3D} instance that is used as the base color
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code baseColor} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD fillSimplexFractionalBrownianMotion(final Color3D baseColor) {
		return fillSimplexFractionalBrownianMotion(baseColor, 5.0D, 0.5D, 16);
	}
	
	/**
	 * Fills this {@code ImageD} instance with {@link Color4D} instances that are generated using a Simplex-based fractional Brownian motion (fBm) algorithm.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If {@code baseColor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param baseColor a {@link Color3D} instance that is used as the base color
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param octaves the number of iterations to perform
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code baseColor} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD fillSimplexFractionalBrownianMotion(final Color3D baseColor, final double frequency, final double gain, final int octaves) {
		Objects.requireNonNull(baseColor, "baseColor == null");
		
		final double minimumX = 0.0D;
		final double minimumY = 0.0D;
		final double maximumX = getResolutionX();
		final double maximumY = getResolutionY();
		
		return update((color, point) -> {
			final double x = (point.getX() - minimumX) / (maximumX - minimumX);
			final double y = (point.getY() - minimumY) / (maximumY - minimumY);
			
			final double noise = SimplexNoiseD.fractionalBrownianMotionXY(x, y, frequency, gain, 0.0D, 1.0D, octaves);
			
			return new Color4D(Color3D.multiply(baseColor, noise));
		});
	}
	
	/**
	 * Converts this {@code ImageD} instance into grayscale using {@link Color4D#grayscaleAverage(Color4D)}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * 
	 * @return this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	public final ImageD grayscaleAverage() {
		return update((color, point) -> Color4D.grayscaleAverage(color));
	}
	
	/**
	 * Converts this {@code ImageD} instance into grayscale using {@link Color4D#grayscaleComponent1(Color4D)}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * 
	 * @return this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	public final ImageD grayscaleComponent1() {
		return update((color, point) -> Color4D.grayscaleComponent1(color));
	}
	
	/**
	 * Converts this {@code ImageD} instance into grayscale using {@link Color4D#grayscaleComponent2(Color4D)}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * 
	 * @return this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	public final ImageD grayscaleComponent2() {
		return update((color, point) -> Color4D.grayscaleComponent2(color));
	}
	
	/**
	 * Converts this {@code ImageD} instance into grayscale using {@link Color4D#grayscaleComponent3(Color4D)}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * 
	 * @return this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	public final ImageD grayscaleComponent3() {
		return update((color, point) -> Color4D.grayscaleComponent3(color));
	}
	
	/**
	 * Converts this {@code ImageD} instance into grayscale using {@link Color4D#grayscaleLightness(Color4D)}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * 
	 * @return this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	public final ImageD grayscaleLightness() {
		return update((color, point) -> Color4D.grayscaleLightness(color));
	}
	
	/**
	 * Converts this {@code ImageD} instance into grayscale using {@link Color4D#grayscaleLuminance(Color4D)}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * 
	 * @return this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	public final ImageD grayscaleLuminance() {
		return update((color, point) -> Color4D.grayscaleLuminance(color));
	}
	
	/**
	 * Converts this {@code ImageD} instance into grayscale using {@link Color4D#grayscaleMaximum(Color4D)}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * 
	 * @return this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	public final ImageD grayscaleMaximum() {
		return update((color, point) -> Color4D.grayscaleMaximum(color));
	}
	
	/**
	 * Converts this {@code ImageD} instance into grayscale using {@link Color4D#grayscaleMinimum(Color4D)}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * 
	 * @return this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	public final ImageD grayscaleMinimum() {
		return update((color, point) -> Color4D.grayscaleMinimum(color));
	}
	
	/**
	 * Inverts this {@code ImageD} instance.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * 
	 * @return this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	public final ImageD invert() {
		return update((color, point) -> Color4D.invert(color));
	}
	
	/**
	 * Multiplies this {@code ImageD} instance with {@code convolutionKernel}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If {@code convolutionKernel} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param convolutionKernel a {@link ConvolutionKernel33D} instance
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code convolutionKernel} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD multiply(final ConvolutionKernel33D convolutionKernel) {
		final Color3D factor = new Color3D(convolutionKernel.getFactor());
		final Color3D bias = new Color3D(convolutionKernel.getBias());
		
		doChangeBegin();
		
		final ImageD image = copy();
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++) {
				Color3D colorRGB = Color3D.BLACK;
				Color4D colorRGBA = image.getColorRGBA(x, y);
				
//				Row #1:
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + -1, y + -1), convolutionKernel.getElement11()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + +0, y + -1), convolutionKernel.getElement12()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + +1, y + -1), convolutionKernel.getElement13()));
				
//				Row #2:
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + -1, y + +0), convolutionKernel.getElement21()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + +0, y + +0), convolutionKernel.getElement22()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + +1, y + +0), convolutionKernel.getElement23()));
				
//				Row #3:
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + -1, y + +1), convolutionKernel.getElement31()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + +0, y + +1), convolutionKernel.getElement32()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + +1, y + +1), convolutionKernel.getElement33()));
				
//				Multiply with the factor and add the bias:
				colorRGB = Color3D.multiply(colorRGB, factor);
				colorRGB = Color3D.add(colorRGB, bias);
				colorRGB = Color3D.minimumTo0(colorRGB);
				colorRGB = Color3D.maximumTo1(colorRGB);
				
				colorRGBA = new Color4D(colorRGB.getR(), colorRGB.getG(), colorRGB.getB(), colorRGBA.getA());
				
				doSetColorRGBA(colorRGBA, x, y);
			}
		}
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Multiplies this {@code ImageD} instance with {@code convolutionKernel}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If {@code convolutionKernel} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param convolutionKernel a {@link ConvolutionKernel55D} instance
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code convolutionKernel} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD multiply(final ConvolutionKernel55D convolutionKernel) {
		final Color3D factor = new Color3D(convolutionKernel.getFactor());
		final Color3D bias = new Color3D(convolutionKernel.getBias());
		
		doChangeBegin();
		
		final ImageD image = copy();
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++) {
				Color3D colorRGB = Color3D.BLACK;
				Color4D colorRGBA = image.getColorRGBA(x, y);
				
//				Row #1:
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + -2, y + -2), convolutionKernel.getElement11()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + -1, y + -2), convolutionKernel.getElement12()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + +0, y + -2), convolutionKernel.getElement13()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + +1, y + -2), convolutionKernel.getElement14()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + +2, y + -2), convolutionKernel.getElement15()));
				
//				Row #2:
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + -2, y + -1), convolutionKernel.getElement21()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + -1, y + -1), convolutionKernel.getElement22()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + +0, y + -1), convolutionKernel.getElement23()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + +1, y + -1), convolutionKernel.getElement24()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + +2, y + -1), convolutionKernel.getElement25()));
				
//				Row #3:
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + -2, y + +0), convolutionKernel.getElement31()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + -1, y + +0), convolutionKernel.getElement32()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + +0, y + +0), convolutionKernel.getElement33()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + +1, y + +0), convolutionKernel.getElement34()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + +2, y + +0), convolutionKernel.getElement35()));
				
//				Row #4:
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + -2, y + +1), convolutionKernel.getElement41()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + -1, y + +1), convolutionKernel.getElement42()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + +0, y + +1), convolutionKernel.getElement43()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + +1, y + +1), convolutionKernel.getElement44()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + +2, y + +1), convolutionKernel.getElement45()));
				
//				Row #5:
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + -2, y + +2), convolutionKernel.getElement51()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + -1, y + +2), convolutionKernel.getElement52()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + +0, y + +2), convolutionKernel.getElement53()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + +1, y + +2), convolutionKernel.getElement54()));
				colorRGB = Color3D.add(colorRGB, Color3D.multiply(image.getColorRGB(x + +2, y + +2), convolutionKernel.getElement55()));
				
//				Multiply with the factor and add the bias:
				colorRGB = Color3D.multiply(colorRGB, factor);
				colorRGB = Color3D.add(colorRGB, bias);
				colorRGB = Color3D.minimumTo0(colorRGB);
				colorRGB = Color3D.maximumTo1(colorRGB);
				
				colorRGBA = new Color4D(colorRGB.getR(), colorRGB.getG(), colorRGB.getB(), colorRGBA.getA());
				
				doSetColorRGBA(colorRGBA, x, y);
			}
		}
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Redoes gamma correction on this {@code ImageD} instance using {@link ColorSpaceD#getDefault()}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * imageD.redoGammaCorrection(ColorSpaceD.getDefault());
	 * }
	 * </pre>
	 * 
	 * @return this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	public final ImageD redoGammaCorrection() {
		return redoGammaCorrection(ColorSpaceD.getDefault());
	}
	
	/**
	 * Redoes gamma correction on this {@code ImageD} instance using {@code colorSpace}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If {@code colorSpace} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorSpace a {@link ColorSpaceD} instance
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code colorSpace} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD redoGammaCorrection(final ColorSpaceD colorSpace) {
		Objects.requireNonNull(colorSpace, "colorSpace == null");
		
		return update((color, point) -> colorSpace.redoGammaCorrection(color));
	}
	
	/**
	 * Rotates this {@code ImageD} instance with an angle of {@code angle}.
	 * <p>
	 * Returns a new rotated version of this {@code ImageD} instance.
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
	 * @param angle an {@link AngleD} instance
	 * @return a new rotated version of this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD rotate(final AngleD angle) {
		return rotate(angle, false);
	}
	
	/**
	 * Rotates this {@code ImageD} instance with an angle of {@code angle}.
	 * <p>
	 * Returns a new rotated version of this {@code ImageD} instance.
	 * <p>
	 * If {@code angle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param angle an {@link AngleD} instance
	 * @param isWrappingAround {@code true} if, and only if, a wrap-around operation should be used, {@code false} otherwise
	 * @return a new rotated version of this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD rotate(final AngleD angle, final boolean isWrappingAround) {
		Objects.requireNonNull(angle, "angle == null");
		
//		Retrieve the resolution for the old ImageD instance:
		final double oldResolutionX = getResolutionX();
		final double oldResolutionY = getResolutionY();
		
//		Compute the first translation directions from the old to the new ImageD instance and from the new to the old ImageD instance:
		final Vector2D directionAToNewImage = new Vector2D(-oldResolutionX * 0.5D, -oldResolutionY * 0.5D);
		final Vector2D directionAToOldImage = Vector2D.negate(directionAToNewImage);
		
//		Retrieve the AngleD instances to rotate from the old to the new ImageD instance and from the new to the old ImageD instance:
		final AngleD angleToNewImage = angle;
		final AngleD angleToOldImage = AngleD.negate(angleToNewImage);
		
//		Compute the original, translated and rotated Rectangle2D instances for the new ImageD instance:
		final Rectangle2D rectangleA = new Rectangle2D(new Point2D(), new Point2D(oldResolutionX, oldResolutionY));
		final Rectangle2D rectangleB = Rectangle2D.translate(rectangleA, directionAToNewImage);
		final Rectangle2D rectangleC = Rectangle2D.rotate(rectangleB, angleToNewImage);
		
//		Compute the minimum and maximum Point2D instances from the translated and rotated Rectangle2D instance:
		final Point2D minimum = Point2D.minimum(rectangleC.getA(), rectangleC.getB(), rectangleC.getC(), rectangleC.getD());
		final Point2D maximum = Point2D.maximum(rectangleC.getA(), rectangleC.getB(), rectangleC.getC(), rectangleC.getD());
		
//		Compute the second translation direction for the new and old ImageD instances:
		final Vector2D directionBToNewImage = new Vector2D(abs(min(minimum.x, 0.0D)), abs(min(minimum.y, 0.0D)));
		final Vector2D directionBToOldImage = Vector2D.negate(directionBToNewImage);
		
//		Compute the resolution for the new ImageD instance:
		final int newResolutionX = toInt(maximum.x - minimum.x);
		final int newResolutionY = toInt(maximum.y - minimum.y);
		
//		Initialize the old and new ImageD instances:
		final ImageD oldImage = this;
		final ImageD newImage = oldImage.newImage(newResolutionX, newResolutionY);
		
		for(int y = 0; y < newResolutionY; y++) {
			for(int x = 0; x < newResolutionX; x++) {
//				Compute the current Point2D instance for the new ImageD instance and reverse the operations to get the equivalent Point2D instance for the old ImageD instance:
				final Point2D a = new Point2D(x, y);
				final Point2D b = Point2D.add(a, directionBToOldImage);
				final Point2D c = Point2D.rotateCounterclockwise(b, angleToOldImage);
				final Point2D d = Point2D.add(c, directionAToOldImage);
				
//				Set the Color4D instance in the new ImageD instance:
				newImage.doSetColorRGBA(isWrappingAround ? oldImage.getColorRGBA(d, PixelOperation.WRAP_AROUND) : oldImage.getColorRGBA(d, point -> Color4D.TRANSPARENT), x, y);
			}
		}
		
		return newImage;
	}
	
	/**
	 * Scales this {@code ImageD} instance with a scale of {@code scale}.
	 * <p>
	 * Returns a new scaled version of this {@code ImageD} instance.
	 * <p>
	 * If {@code scale} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the scaled resolution is invalid, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param scale a {@link Vector2D} instance
	 * @return a new scaled version of this {@code ImageD} instance
	 * @throws IllegalArgumentException thrown if, and only if, the scaled resolution is invalid
	 * @throws NullPointerException thrown if, and only if, {@code scale} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD scale(final Vector2D scale) {
		return scale(toInt(ceil(getResolutionX() * scale.getX())), toInt(ceil(getResolutionY() * scale.getY())));
	}
	
	/**
	 * Scales this {@code ImageD} instance to the resolution given by {@code resolutionX} and {@code resolutionY}.
	 * <p>
	 * Returns a new scaled version of this {@code ImageD} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @return a new scaled version of this {@code ImageD} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	public final ImageD scale(final int resolutionX, final int resolutionY) {
		final ImageD oldImage = this;
		final ImageD newImage = oldImage.newImage(resolutionX, resolutionY);
		
		final double scaleX = resolutionX == 0 ? 0.0D : toDouble(getResolutionX()) / toDouble(resolutionX);
		final double scaleY = resolutionY == 0 ? 0.0D : toDouble(getResolutionY()) / toDouble(resolutionY);
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++) {
				newImage.doSetColorRGBA(oldImage.getColorRGBA(x * scaleX, y * scaleY), x, y);
			}
		}
		
		return newImage;
	}
	
	/**
	 * Converts this {@code ImageD} instance into its sepia-representation.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * 
	 * @return this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	public final ImageD sepia() {
		return update((color, point) -> Color4D.sepia(color));
	}
	
	/**
	 * Sets the {@link Color3D} of the pixel represented by {@code index} to {@code colorRGB}.
	 * <p>
	 * Returns this {@code ImageD} instance.
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
	 * @param colorRGB the {@code Color3D} to set
	 * @param index the index of the pixel
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code colorRGB} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD setColorRGB(final Color3D colorRGB, final int index) {
		return setColorRGB(colorRGB, index, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Sets the {@link Color3D} of the pixel represented by {@code index} to {@code colorRGB}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code colorRGB} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param colorRGB the {@code Color3D} to set
	 * @param index the index of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGB} or {@code pixelOperation} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD setColorRGB(final Color3D colorRGB, final int index, final PixelOperation pixelOperation) {
		return setColorRGBA(new Color4D(colorRGB), index, pixelOperation);
	}
	
	/**
	 * Sets the {@link Color3D} of the pixel represented by {@code x} and {@code y} to {@code colorRGB}.
	 * <p>
	 * Returns this {@code ImageD} instance.
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
	 * @param colorRGB the {@code Color3D} to set
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code colorRGB} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD setColorRGB(final Color3D colorRGB, final int x, final int y) {
		return setColorRGB(colorRGB, x, y, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Sets the {@link Color3D} of the pixel represented by {@code x} and {@code y} to {@code colorRGB}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code colorRGB} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param colorRGB the {@code Color3D} to set
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGB} or {@code pixelOperation} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD setColorRGB(final Color3D colorRGB, final int x, final int y, final PixelOperation pixelOperation) {
		return setColorRGBA(new Color4D(colorRGB), x, y, pixelOperation);
	}
	
	/**
	 * Sets the {@link Color4D} of the pixel represented by {@code index} to {@code colorRGBA}.
	 * <p>
	 * Returns this {@code ImageD} instance.
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
	 * @param colorRGBA the {@code Color4D} to set
	 * @param index the index of the pixel
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD setColorRGBA(final Color4D colorRGBA, final int index) {
		return setColorRGBA(colorRGBA, index, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Sets the {@link Color4D} of the pixel represented by {@code index} to {@code colorRGBA}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code colorRGBA} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param colorRGBA the {@code Color4D} to set
	 * @param index the index of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBA} or {@code pixelOperation} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD setColorRGBA(final Color4D colorRGBA, final int index, final PixelOperation pixelOperation) {
		doChangeBegin();
		doSetColorRGBA(colorRGBA, index, pixelOperation);
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Sets the {@link Color4D} of the pixel represented by {@code x} and {@code y} to {@code colorRGBA}.
	 * <p>
	 * Returns this {@code ImageD} instance.
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
	 * @param colorRGBA the {@code Color4D} to set
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD setColorRGBA(final Color4D colorRGBA, final int x, final int y) {
		return setColorRGBA(colorRGBA, x, y, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Sets the {@link Color4D} of the pixel represented by {@code x} and {@code y} to {@code colorRGBA}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code colorRGBA} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param colorRGBA the {@code Color4D} to set
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBA} or {@code pixelOperation} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD setColorRGBA(final Color4D colorRGBA, final int x, final int y, final PixelOperation pixelOperation) {
		doChangeBegin();
		doSetColorRGBA(colorRGBA, x, y, pixelOperation);
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Applies a Sobel operator to this {@code ImageD} instance.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * 
	 * @return this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	public final ImageD sobel() {
		doChangeBegin();
		
		final ImageD image = copy();
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		final double[][] kernel = {{-1.0D, 0.0D, 1.0D}, {-2.0D, 0.0D, 2.0D}, {-1.0D, 0.0D, 1.0D}};
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++) {
				double magnitudeX = 0.0D;
				double magnitudeY = 0.0D;
				
				for(int kernelY = 0; kernelY < 3; kernelY++) {
					for(int kernelX = 0; kernelX < 3; kernelX++) {
						final int currentX = x + kernelX - 1;
						final int currentY = y + kernelY - 1;
						
						if(currentX >= 0 && currentX < resolutionX && currentY >= 0 && currentY < resolutionY) {
							final int index = currentY * resolutionX + currentX;
							
							final double intensity = image.getColorRGB(index).average();
							
							magnitudeX += intensity * kernel[kernelY][kernelX];
							magnitudeY += intensity * kernel[kernelX][kernelY];
						}
					}
				}
				
				doSetColorRGBA(new Color4D(sqrt(magnitudeX * magnitudeX + magnitudeY * magnitudeY)), x, y);
			}
		}
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Performs tone mapping on this {@code ImageD} instance given {@code luminanceMaximum} as the maximum luminance.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * 
	 * @param luminanceMaximum the maximum luminance
	 * @return this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	public final ImageD toneMap(final double luminanceMaximum) {
		return update((color, point) -> {
			final double luminance = color.luminance();
			final double scale = (1.0D + luminance / (luminanceMaximum * luminanceMaximum)) / (1.0D + luminance);
			
			return new Color4D(Color3D.multiply(new Color3D(color), scale), color.getA());
		});
	}
	
	/**
	 * Sets the transparency for this {@code ImageD} instance to {@code transparency}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * 
	 * @param transparency the transparency
	 * @return this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	public final ImageD transparency(final double transparency) {
		return update((color, point) -> new Color4D(color.getComponent1(), color.getComponent2(), color.getComponent3(), transparency));
	}
	
	/**
	 * Undoes gamma correction on this {@code ImageD} instance using {@link ColorSpaceD#getDefault()}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * imageD.undoGammaCorrection(ColorSpaceD.getDefault());
	 * }
	 * </pre>
	 * 
	 * @return this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	public final ImageD undoGammaCorrection() {
		return undoGammaCorrection(ColorSpaceD.getDefault());
	}
	
	/**
	 * Undoes gamma correction on this {@code ImageD} instance using {@code colorSpace}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If {@code colorSpace} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorSpace a {@link ColorSpaceD} instance
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code colorSpace} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD undoGammaCorrection(final ColorSpaceD colorSpace) {
		Objects.requireNonNull(colorSpace, "colorSpace == null");
		
		return update((color, point) -> colorSpace.undoGammaCorrection(color));
	}
	
	/**
	 * Updates this {@code ImageD} instance by applying {@code biFunction} to all pixels.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code biFunction} or the result returned by {@code biFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * imageD.update(biFunction, imageD.getBounds());
	 * }
	 * </pre>
	 * 
	 * @param biFunction a {@code BiFunction} that returns {@link Color4D} instances
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code biFunction} or the result returned by {@code biFunction} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD update(final BiFunction<Color4D, Point2I, Color4D> biFunction) {
		return update(biFunction, getBounds());
	}
	
	/**
	 * Updates this {@code ImageD} instance by applying {@code biFunction} to all pixels within {@code bounds}.
	 * <p>
	 * Returns this {@code ImageD} instance.
	 * <p>
	 * If either {@code biFunction}, the result returned by {@code biFunction} or {@code bounds} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param biFunction a {@code BiFunction} that returns {@link Color4D} instances
	 * @param bounds a {@link Rectangle2I} instance used as the bounds for the update
	 * @return this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code biFunction}, the result returned by {@code biFunction} or {@code bounds} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final ImageD update(final BiFunction<Color4D, Point2I, Color4D> biFunction, final Rectangle2I bounds) {
		Objects.requireNonNull(biFunction, "biFunction == null");
		Objects.requireNonNull(bounds, "bounds == null");
		
		doChangeBegin();
		
		final Point2I minimum = bounds.getA();
		final Point2I maximum = bounds.getC();
		
		final int minimumX = max(minimum.getX(), 0);
		final int minimumY = max(minimum.getY(), 0);
		final int maximumX = min(maximum.getX(), getResolutionX() - 1);
		final int maximumY = min(maximum.getY(), getResolutionY() - 1);
		
		for(int y = minimumY; y <= maximumY; y++) {
			for(int x = minimumX; x <= maximumX; x++) {
				doSetColorRGBA(biFunction.apply(getColorRGBA(x, y), new Point2I(x, y)), x, y);
			}
		}
		
		doChangeEnd();
		
		return this;
	}
	
	/**
	 * Finds the bounds for {@code image} in this {@code ImageD} instance.
	 * <p>
	 * Returns a {@code List} with all {@link Rectangle2I} bounds found for {@code image} in this {@code ImageD} instance.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param image an {@code ImageD} instance
	 * @return a {@code List} with all {@code Rectangle2I} bounds found for {@code image} in this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final List<Rectangle2I> findBoundsFor(final ImageD image) {
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
	
	/**
	 * Returns the maximum luminance in this {@code ImageD} instance.
	 * 
	 * @return the maximum luminance in this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	public final double luminanceMaximum() {
		double luminanceMaximum = NaN;
		
		for(int i = 0; i < getResolution(); i++) {
			luminanceMaximum = maxOrNaN(luminanceMaximum, getColorRGB(i).luminance());
		}
		
		return luminanceMaximum;
	}
	
	/**
	 * Returns the minimum luminance in this {@code ImageD} instance.
	 * 
	 * @return the minimum luminance in this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	public final double luminanceMinimum() {
		double luminanceMinimum = NaN;
		
		for(int i = 0; i < getResolution(); i++) {
			luminanceMinimum = minOrNaN(luminanceMinimum, getColorRGB(i).luminance());
		}
		
		return luminanceMinimum;
	}
	
	/**
	 * Returns a {@code double[]} representation of this {@code ImageD} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * imageD.toDoubleArray(ArrayComponentOrder.RGBA);
	 * }
	 * </pre>
	 * 
	 * @return a {@code double[]} representation of this {@code ImageD} instance
	 */
//	TODO: Add Unit Tests!
	public final double[] toDoubleArray() {
		return toDoubleArray(ArrayComponentOrder.RGBA);
	}
	
	/**
	 * Returns a {@code double[]} representation of this {@code ImageD} instance.
	 * <p>
	 * If {@code arrayComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param arrayComponentOrder an {@link ArrayComponentOrder}
	 * @return a {@code double[]} representation of this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code arrayComponentOrder} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final double[] toDoubleArray(final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
		final int componentCount = arrayComponentOrder.getComponentCount();
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		final double[] array = new double[resolutionX * resolutionY * componentCount];
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0, index = (y * resolutionX + x) * componentCount; x < resolutionX; x++, index += componentCount) {
				final Color4D colorRGBA = getColorRGBA(x, y);
				
				if(arrayComponentOrder.hasOffsetR()) {
					array[index + arrayComponentOrder.getOffsetR()] = colorRGBA.getR();
				}
				
				if(arrayComponentOrder.hasOffsetG()) {
					array[index + arrayComponentOrder.getOffsetG()] = colorRGBA.getG();
				}
				
				if(arrayComponentOrder.hasOffsetB()) {
					array[index + arrayComponentOrder.getOffsetB()] = colorRGBA.getB();
				}
				
				if(arrayComponentOrder.hasOffsetA()) {
					array[index + arrayComponentOrder.getOffsetA()] = colorRGBA.getA();
				}
			}
		}
		
		return array;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code ImageD} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @return a new {@code ImageD} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
	protected abstract ImageD newImage(final int resolutionX, final int resolutionY);
	
	/**
	 * Sets the {@link Color4D} of the pixel represented by {@code index} to {@code colorRGBA}.
	 * <p>
	 * If {@code colorRGBA} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorRGBA the {@code Color4D} to set
	 * @param index the index of the pixel
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
	protected abstract void putColorRGBA(final Color4D colorRGBA, final int index);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private ImageD doSetColorRGBA(final Color4D colorRGBA, final int index) {
		return doSetColorRGBA(colorRGBA, index, PixelOperation.NO_CHANGE);
	}
	
	private ImageD doSetColorRGBA(final Color4D colorRGBA, final int index, final PixelOperation pixelOperation) {
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
	
	private ImageD doSetColorRGBA(final Color4D colorRGBA, final int x, final int y) {
		return doSetColorRGBA(colorRGBA, x, y, PixelOperation.NO_CHANGE);
	}
	
	private ImageD doSetColorRGBA(final Color4D colorRGBA, final int x, final int y, final PixelOperation pixelOperation) {
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
	
	private void doChangeCreate(final Color4D newColorRGBA, final Color4D oldColorRGBA, final int index) {
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
	
	private void doFillRegion(final int x, final int y, final BiFunction<Color4D, Point2I, Color4D> biFunction, final BiPredicate<Color4D, Point2I> biPredicate, final Color4D oldColorRGBA) {
		final int resolution = getResolution();
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		final int minimumX = 0;
		final int maximumX = resolutionX - 1;
		final int minimumY = 0;
		final int maximumY = resolutionY - 1;
		
		if(x >= minimumX && x <= maximumX && y >= minimumY && y <= maximumY) {
			final boolean[] isFilled = new boolean[resolution];
			
			final int[] stackX = new int[resolution];
			final int[] stackY = new int[resolution];
			
			stackX[0] = x;
			stackY[0] = y;
			
			int stackLength = 1;
			
			while(stackLength > 0) {
				final int currentX = stackX[stackLength - 1];
				final int currentY = stackY[stackLength - 1];
				
				stackLength--;
				
				final Color4D colorARGB = getColorRGBA(currentX, currentY);
				
				final Point2I point = new Point2I(currentX, currentY);
				
				if(biPredicate.test(colorARGB, point)) {
					doSetColorRGBA(biFunction.apply(colorARGB, point), currentX, currentY);
				}
				
				isFilled[currentY * resolutionX + currentX] = true;
				
				if(currentX + 1 <= maximumX && !isFilled[currentY * resolutionX + currentX + 1] && getColorRGBA(currentX + 1, currentY).equals(oldColorRGBA)) {
					stackX[stackLength] = currentX + 1;
					stackY[stackLength] = currentY;
					
					stackLength++;
				}
				
				if(currentX - 1 >= minimumX && !isFilled[currentY * resolutionX + currentX - 1] && getColorRGBA(currentX - 1, currentY).equals(oldColorRGBA)) {
					stackX[stackLength] = currentX - 1;
					stackY[stackLength] = currentY;
					
					stackLength++;
				}
				
				if(currentY + 1 <= maximumY && !isFilled[(currentY + 1) * resolutionX + currentX] && getColorRGBA(currentX, currentY + 1).equals(oldColorRGBA)) {
					stackX[stackLength] = currentX;
					stackY[stackLength] = currentY + 1;
					
					stackLength++;
				}
				
				if(currentY - 1 >= minimumY && !isFilled[(currentY - 1) * resolutionX + currentX] && getColorRGBA(currentX, currentY - 1).equals(oldColorRGBA)) {
					stackX[stackLength] = currentX;
					stackY[stackLength] = currentY - 1;
					
					stackLength++;
				}
			}
		}
	}
}