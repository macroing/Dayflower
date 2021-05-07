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

import static org.dayflower.utility.Doubles.abs;
import static org.dayflower.utility.Doubles.ceil;
import static org.dayflower.utility.Doubles.floor;
import static org.dayflower.utility.Doubles.min;
import static org.dayflower.utility.Doubles.toDouble;
import static org.dayflower.utility.Ints.max;
import static org.dayflower.utility.Ints.min;
import static org.dayflower.utility.Ints.toInt;
import static org.dayflower.utility.NoiseD.simplexFractionalBrownianMotionXY;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

import org.dayflower.color.Color3D;
import org.dayflower.color.Color4D;
import org.dayflower.geometry.AngleD;
import org.dayflower.geometry.Point2D;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.Vector2D;
import org.dayflower.geometry.rasterizer.Rasterizer2I;
import org.dayflower.geometry.shape.Circle2I;
import org.dayflower.geometry.shape.Line2I;
import org.dayflower.geometry.shape.Rectangle2D;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.geometry.shape.Triangle2I;
import org.macroing.java.util.function.TriFunction;

/**
 * An {@code ImageD} is an {@link Image} implementation that operates using the data type {@code double}.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class ImageD extends Image {
	/**
	 * Constructs a new {@code ImageD} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
	protected ImageD(final int resolutionX, final int resolutionY) {
		super(resolutionX, resolutionY);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	public final Color3D getColorRGB(final int x, final int y, final PixelOperation pixelOperation) {
		return new Color3D(getColorRGBA(x, y, pixelOperation));
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
	public final Color4D getColorRGBA(final double x, final double y) {
		return getColorRGBA(x, y, PixelOperation.NO_CHANGE);
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
	public final Color4D getColorRGBA(final int x, final int y) {
		return getColorRGBA(x, y, PixelOperation.NO_CHANGE);
	}
	
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
	 * Returns a copy of this {@code ImageD} instance.
	 * 
	 * @return a copy of this {@code ImageD} instance
	 */
	@Override
	public abstract ImageD copy();
	
	/**
	 * Returns a copy of this {@code ImageD} instance within {@code bounds}.
	 * <p>
	 * If {@code bounds} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bounds a {@link Rectangle2I} instance that represents the bounds within this {@code ImageD} instance to copy
	 * @return a copy of this {@code ImageD} instance within {@code bounds}
	 * @throws NullPointerException thrown if, and only if, {@code bounds} is {@code null}
	 */
	@Override
	public abstract ImageD copy(final Rectangle2I bounds);
	
	/**
	 * Rotates this {@code ImageD} instance with an angle of {@code angle}.
	 * <p>
	 * Returns a new rotated version of this {@code ImageD} instance.
	 * <p>
	 * If {@code angle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param angle an {@link AngleD} instance
	 * @return a new rotated version of this {@code ImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
	 */
	public final ImageD rotate(final AngleD angle) {
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
		final Vector2D directionBToNewImage = new Vector2D(abs(min(minimum.getX(), 0.0D)), abs(min(minimum.getY(), 0.0D)));
		final Vector2D directionBToOldImage = Vector2D.negate(directionBToNewImage);
		
//		Compute the resolution for the new ImageD instance:
		final int newResolutionX = toInt(ceil(maximum.getX() - minimum.getX()));
		final int newResolutionY = toInt(ceil(maximum.getY() - minimum.getY()));
		
//		Initialize the old and new ImageD instances:
		final ImageD oldImage = this;
		final ImageD newImage = oldImage.newImage(newResolutionX, newResolutionY);
		
		for(int y = 0; y < newResolutionY; y++) {
			for(int x = 0; x < newResolutionX; x++) {
//				Compute the current Point2D instance for the new ImageD instance and reverse the operations to get the equivalent Point2D instance for the old ImageD instance:
				final Point2D a = new Point2D(x, y);
				final Point2D b = Point2D.add(a, directionBToOldImage);
				final Point2D c = Point2D.rotate(b, angleToOldImage);
				final Point2D d = Point2D.add(c, directionAToOldImage);
				
//				Set the Color4D instance in the new ImageD instance:
				newImage.setColorRGBA(oldImage.getColorRGBA(d.getX(), d.getY()), x, y);
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
	public final ImageD scale(final int resolutionX, final int resolutionY) {
		final ImageD oldImage = this;
		final ImageD newImage = oldImage.newImage(resolutionX, resolutionY);
		
		final double scaleX = resolutionX == 0 ? 0.0D : toDouble(getResolutionX()) / toDouble(resolutionX);
		final double scaleY = resolutionY == 0 ? 0.0D : toDouble(getResolutionY()) / toDouble(resolutionY);
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++) {
				newImage.setColorRGBA(oldImage.getColorRGBA(x * scaleX, y * scaleY), x, y);
			}
		}
		
		return newImage;
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
	 * Blends this {@code ImageD} instance over {@code image}.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param image an {@code ImageD} instance that acts as a background
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	public final void blendOver(final ImageD image) {
		fillImage(image, image.getBounds(), getBounds(), (sourceColorRGBA, targetColorRGBA, targetPoint) -> Color4D.blendOver(targetColorRGBA, sourceColorRGBA));
	}
	
	/**
	 * Clears this {@code ImageD} instance with a {@link Color4D} of {@code Color4D.BLACK}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.clear(Color4D.BLACK);
	 * }
	 * </pre>
	 */
	public final void clear() {
		clear(Color4D.BLACK);
	}
	
	/**
	 * Clears this {@code ImageD} instance with a {@link Color3D} of {@code colorRGB}.
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
	 * @throws NullPointerException thrown if, and only if, {@code colorRGB} is {@code null}
	 */
	public final void clear(final Color3D colorRGB) {
		clear(new Color4D(colorRGB));
	}
	
	/**
	 * Clears this {@code ImageD} instance with a {@link Color4D} of {@code colorRGBA}.
	 * <p>
	 * If {@code colorRGBA} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorRGBA the {@code Color4D} to clear with
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
	public final void clear(final Color4D colorRGBA) {
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		final int resolution = getResolution();
		
		for(int i = 0; i < resolution; i++) {
			setColorRGBA(colorRGBA, i);
		}
	}
	
	/**
	 * Draws {@code circle} to this {@code ImageD} instance with {@code Color4D.BLACK} as its color.
	 * <p>
	 * If {@code circle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawCircle(circle, Color4D.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param circle the {@link Circle2I} to draw
	 * @throws NullPointerException thrown if, and only if, {@code circle} is {@code null}
	 */
	public final void drawCircle(final Circle2I circle) {
		drawCircle(circle, Color4D.BLACK);
	}
	
	/**
	 * Draws {@code circle} to this {@code ImageD} instance with {@code colorRGBA} as its color.
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
	 * @param colorRGBA the {@link Color4D} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code circle} or {@code colorRGBA} are {@code null}
	 */
	public final void drawCircle(final Circle2I circle, final Color4D colorRGBA) {
		Objects.requireNonNull(circle, "circle == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		drawCircle(circle, (color, point) -> colorRGBA);
	}
	
	/**
	 * Draws {@code circle} to this {@code ImageD} instance with {@link Color4D} instances returned by {@code biFunction} as its color.
	 * <p>
	 * If either {@code circle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param circle the {@link Circle2I} to draw
	 * @param biFunction a {@code BiFunction} that returns {@code Color4D} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code circle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final void drawCircle(final Circle2I circle, final BiFunction<Color4D, Point2I, Color4D> biFunction) {
		Objects.requireNonNull(circle, "circle == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		for(int y = -circle.getRadius(); y <= circle.getRadius(); y++) {
			for(int x = -circle.getRadius(); x <= circle.getRadius(); x++) {
				if(x * x + y * y <= circle.getRadius() * circle.getRadius() && x * x + y * y > (circle.getRadius() - 1) * (circle.getRadius() - 1)) {
					final int circleX = x + circle.getCenter().getX();
					final int circleY = y + circle.getCenter().getY();
					
					if(circleX >= 0 && circleX < resolutionX && circleY >= 0 && circleY < resolutionY) {
						final Point2I point = new Point2I(circleX, circleY);
						
						final Color4D oldColorRGBA = getColorRGBA(circleX, circleY);
						final Color4D newColorRGBA = Objects.requireNonNull(biFunction.apply(oldColorRGBA, point));
						
						setColorRGBA(newColorRGBA, circleX, circleY);
					}
				}
			}
		}
	}
	
	/**
	 * Draws {@code line} to this {@code ImageD} instance with {@code Color4D.BLACK} as its color.
	 * <p>
	 * If {@code line} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawLine(line, Color4D.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param line the {@link Line2I} to draw
	 * @throws NullPointerException thrown if, and only if, {@code line} is {@code null}
	 */
	public final void drawLine(final Line2I line) {
		drawLine(line, Color4D.BLACK);
	}
	
	/**
	 * Draws {@code line} to this {@code ImageD} instance with {@code colorRGBA} as its color.
	 * <p>
	 * If either {@code line} or {@code colorRGBA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawLine(line, (color, point) -> colorRGBA);
	 * }
	 * </pre>
	 * 
	 * @param line the {@link Line2I} to draw
	 * @param colorRGBA the {@link Color4D} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code line} or {@code colorRGBA} are {@code null}
	 */
	public final void drawLine(final Line2I line, final Color4D colorRGBA) {
		Objects.requireNonNull(line, "line == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		drawLine(line, (color, point) -> colorRGBA);
	}
	
	/**
	 * Draws {@code line} to this {@code ImageD} instance with {@link Color4D} instances returned by {@code biFunction} as its color.
	 * <p>
	 * If either {@code line} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param line the {@link Line2I} to draw
	 * @param biFunction a {@code BiFunction} that returns {@code Color4D} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code line} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final void drawLine(final Line2I line, final BiFunction<Color4D, Point2I, Color4D> biFunction) {
		Objects.requireNonNull(line, "line == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		final Rectangle2I rectangle = new Rectangle2I(new Point2I(), new Point2I(getResolutionX(), getResolutionY()));
		
		final Point2I[] scanline = Rasterizer2I.rasterize(line, rectangle);
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		for(final Point2I point : scanline) {
			final int x = point.getX();
			final int y = point.getY();
			
			if(x >= 0 && x < resolutionX && y >= 0 && y < resolutionY) {
				final Color4D oldColorRGBA = getColorRGBA(x, y);
				final Color4D newColorRGBA = Objects.requireNonNull(biFunction.apply(oldColorRGBA, point));
				
				setColorRGBA(newColorRGBA, point.getX(), point.getY());
			}
		}
	}
	
	/**
	 * Draws {@code rectangle} to this {@code ImageD} instance with {@code Color4D.BLACK} as its color.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawRectangle(rectangle, Color4D.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param rectangle the {@link Rectangle2I} to draw
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 */
	public final void drawRectangle(final Rectangle2I rectangle) {
		drawRectangle(rectangle, Color4D.BLACK);
	}
	
	/**
	 * Draws {@code rectangle} to this {@code ImageD} instance with {@code colorRGBA} as its color.
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
	 * @param colorRGBA the {@link Color4D} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code rectangle} or {@code colorRGBA} are {@code null}
	 */
	public final void drawRectangle(final Rectangle2I rectangle, final Color4D colorRGBA) {
		Objects.requireNonNull(rectangle, "rectangle == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		drawRectangle(rectangle, (color, point) -> colorRGBA);
	}
	
	/**
	 * Draws {@code rectangle} to this {@code ImageD} instance with {@link Color4D} instances returned by {@code biFunction} as its color.
	 * <p>
	 * If either {@code rectangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rectangle the {@link Rectangle2I} to draw
	 * @param biFunction a {@code BiFunction} that returns {@code Color4D} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code rectangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final void drawRectangle(final Rectangle2I rectangle, final BiFunction<Color4D, Point2I, Color4D> biFunction) {
		Objects.requireNonNull(rectangle, "rectangle == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
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
					
					final Color4D oldColorRGBA = getColorRGBA(x, y);
					final Color4D newColorRGBA = Objects.requireNonNull(biFunction.apply(oldColorRGBA, point));
					
					setColorRGBA(newColorRGBA, x, y);
				}
			}
		}
	}
	
	/**
	 * Draws {@code triangle} to this {@code ImageD} instance with {@code Color4D.BLACK} as its color.
	 * <p>
	 * If {@code triangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawTriangle(triangle, Color4D.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param triangle the {@link Triangle2I} to draw
	 * @throws NullPointerException thrown if, and only if, {@code triangle} is {@code null}
	 */
	public final void drawTriangle(final Triangle2I triangle) {
		drawTriangle(triangle, Color4D.BLACK);
	}
	
	/**
	 * Draws {@code triangle} to this {@code ImageD} instance with {@code colorRGBA} as its color.
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
	 * @param colorRGBA the {@link Color4D} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code triangle} or {@code colorRGBA} are {@code null}
	 */
	public final void drawTriangle(final Triangle2I triangle, final Color4D colorRGBA) {
		Objects.requireNonNull(triangle, "triangle == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		drawTriangle(triangle, (color, point) -> colorRGBA);
	}
	
	/**
	 * Draws {@code triangle} to this {@code ImageD} instance with {@link Color4D} instances returned by {@code biFunction} as its color.
	 * <p>
	 * If either {@code triangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param triangle the {@link Triangle2I} to draw
	 * @param biFunction a {@code BiFunction} that returns {@code Color4D} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code triangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final void drawTriangle(final Triangle2I triangle, final BiFunction<Color4D, Point2I, Color4D> biFunction) {
		Objects.requireNonNull(triangle, "triangle == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		drawLine(new Line2I(triangle.getA(), triangle.getB()), biFunction);
		drawLine(new Line2I(triangle.getB(), triangle.getC()), biFunction);
		drawLine(new Line2I(triangle.getC(), triangle.getA()), biFunction);
	}
	
	/**
	 * Fills {@code circle} in this {@code ImageD} instance with {@code Color4D.BLACK} as its color.
	 * <p>
	 * If {@code circle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillCircle(circle, Color4D.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param circle the {@link Circle2I} to fill
	 * @throws NullPointerException thrown if, and only if, {@code circle} is {@code null}
	 */
	public final void fillCircle(final Circle2I circle) {
		fillCircle(circle, Color4D.BLACK);
	}
	
	/**
	 * Fills {@code circle} in this {@code ImageD} instance with {@code colorRGBA} as its color.
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
	 * @param colorRGBA the {@link Color4D} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code circle} or {@code colorRGBA} are {@code null}
	 */
	public final void fillCircle(final Circle2I circle, final Color4D colorRGBA) {
		Objects.requireNonNull(circle, "circle == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		fillCircle(circle, (color, point) -> colorRGBA);
	}
	
	/**
	 * Fills {@code circle} in this {@code ImageD} instance with {@link Color4D} instances returned by {@code biFunction} as its color.
	 * <p>
	 * If either {@code circle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param circle the {@link Circle2I} to fill
	 * @param biFunction a {@code BiFunction} that returns {@code Color4D} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code circle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final void fillCircle(final Circle2I circle, final BiFunction<Color4D, Point2I, Color4D> biFunction) {
		Objects.requireNonNull(circle, "circle == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		for(int y = -circle.getRadius(); y <= circle.getRadius(); y++) {
			for(int x = -circle.getRadius(); x <= circle.getRadius(); x++) {
				if(x * x + y * y <= circle.getRadius() * circle.getRadius()) {
					final int circleX = x + circle.getCenter().getX();
					final int circleY = y + circle.getCenter().getY();
					
					if(circleX >= 0 && circleX < resolutionX && circleY >= 0 && circleY < resolutionY) {
						final Point2I point = new Point2I(circleX, circleY);
						
						final Color4D oldColorRGBA = getColorRGBA(circleX, circleY);
						final Color4D newColorRGBA = Objects.requireNonNull(biFunction.apply(oldColorRGBA, point));
						
						setColorRGBA(newColorRGBA, circleX, circleY);
					}
				}
			}
		}
	}
	
	/**
	 * Fills a gradient in this {@code ImageD} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillGradient(Color3D.BLACK, Color3D.RED, Color3D.GREEN, Color3D.YELLOW);
	 * }
	 * </pre>
	 */
	public final void fillGradient() {
		fillGradient(Color3D.BLACK, Color3D.RED, Color3D.GREEN, Color3D.YELLOW);
	}
	
	/**
	 * Fills a gradient in this {@code ImageD} instance.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a the {@link Color3D} instance in the top left corner
	 * @param b the {@code Color3D} instance in the top right corner
	 * @param c the {@code Color3D} instance in the bottom left corner
	 * @param d the {@code Color3D} instance in the bottom right corner
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}
	 */
	public final void fillGradient(final Color3D a, final Color3D b, final Color3D c, final Color3D d) {
		Objects.requireNonNull(a, "a == null");
		Objects.requireNonNull(b, "b == null");
		Objects.requireNonNull(c, "c == null");
		Objects.requireNonNull(d, "d == null");
		
		final double resolutionX = getResolutionX();
		final double resolutionY = getResolutionY();
		
		update((color, point) -> {
			final double tX = 1.0D / resolutionX * point.getX();
			final double tY = 1.0D / resolutionY * point.getY();
			
			return new Color4D(Color3D.blend(Color3D.blend(a, b, tX), Color3D.blend(c, d, tX), tY));
		});
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code ImageD} instance.
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
	 * @throws NullPointerException thrown if, and only if, {@code sourceImage} is {@code null}
	 */
	public final void fillImage(final ImageD sourceImage) {
		fillImage(sourceImage, sourceImage.getBounds());
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code ImageD} instance.
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
	 * @throws NullPointerException thrown if, and only if, either {@code sourceImage} or {@code sourceBounds} are {@code null}
	 */
	public final void fillImage(final ImageD sourceImage, final Rectangle2I sourceBounds) {
		fillImage(sourceImage, sourceBounds, getBounds());
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code ImageD} instance.
	 * <p>
	 * If either {@code sourceImage}, {@code sourceBounds} or {@code targetBounds} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillImage(sourceImage, sourceBounds, targetBounds, (sourceColorRGBA, targetColorRGBA, targetPoint) -> sourceColorRGBA);
	 * }
	 * </pre>
	 * 
	 * @param sourceImage the {@code ImageD} to fill
	 * @param sourceBounds a {@link Rectangle2I} that represents the bounds of the region in {@code sourceImage} to use
	 * @param targetBounds a {@code Rectangle2I} that represents the bounds of the region in this {@code ImageD} instance to use
	 * @throws NullPointerException thrown if, and only if, either {@code sourceImage}, {@code sourceBounds} or {@code targetBounds} are {@code null}
	 */
	public final void fillImage(final ImageD sourceImage, final Rectangle2I sourceBounds, final Rectangle2I targetBounds) {
		fillImage(sourceImage, sourceBounds, targetBounds, (sourceColorRGBA, targetColorRGBA, targetPoint) -> sourceColorRGBA);
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code ImageD} instance with {@link Color4D} instances returned by {@code triFunction} as its color.
	 * <p>
	 * If either {@code sourceImage}, {@code sourceBounds}, {@code targetBounds} or {@code triFunction} are {@code null} or {@code triFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sourceImage the {@code ImageD} to fill
	 * @param sourceBounds a {@link Rectangle2I} that represents the bounds of the region in {@code sourceImage} to use
	 * @param targetBounds a {@code Rectangle2I} that represents the bounds of the region in this {@code ImageD} instance to use
	 * @param triFunction a {@code TriFunction} that returns {@code Color4D} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code sourceImage}, {@code sourceBounds}, {@code targetBounds} or {@code triFunction} are {@code null} or {@code triFunction} returns {@code null}
	 */
	public final void fillImage(final ImageD sourceImage, final Rectangle2I sourceBounds, final Rectangle2I targetBounds, final TriFunction<Color4D, Color4D, Point2I, Color4D> triFunction) {
		Objects.requireNonNull(sourceImage, "sourceImage == null");
		Objects.requireNonNull(sourceBounds, "sourceBounds == null");
		Objects.requireNonNull(targetBounds, "targetBounds == null");
		Objects.requireNonNull(triFunction, "triFunction == null");
		
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
					
					targetImage.setColorRGBA(colorRGBA, targetX, targetY);
				}
			}
		}
	}
	
	/**
	 * Fills {@code rectangle} in this {@code ImageD} instance with {@code Color4D.BLACK} as its color.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillRectangle(rectangle, Color4D.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param rectangle the {@link Rectangle2I} to fill
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 */
	public final void fillRectangle(final Rectangle2I rectangle) {
		fillRectangle(rectangle, Color4D.BLACK);
	}
	
	/**
	 * Fills {@code rectangle} in this {@code ImageD} instance with {@code colorRGB} as its color.
	 * <p>
	 * If either {@code rectangle} or {@code colorRGB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillRectangle(rectangle, new Color4D(colorRGB));
	 * }
	 * </pre>
	 * 
	 * @param rectangle the {@link Rectangle2I} to fill
	 * @param colorRGB the {@link Color3D} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code rectangle} or {@code colorRGB} are {@code null}
	 */
	public final void fillRectangle(final Rectangle2I rectangle, final Color3D colorRGB) {
		Objects.requireNonNull(rectangle, "rectangle == null");
		Objects.requireNonNull(colorRGB, "colorRGB == null");
		
		fillRectangle(rectangle, new Color4D(colorRGB));
	}
	
	/**
	 * Fills {@code rectangle} in this {@code ImageD} instance with {@code colorRGBA} as its color.
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
	 * @param colorRGBA the {@link Color4D} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code rectangle} or {@code colorRGBA} are {@code null}
	 */
	public final void fillRectangle(final Rectangle2I rectangle, final Color4D colorRGBA) {
		Objects.requireNonNull(rectangle, "rectangle == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		fillRectangle(rectangle, (color, point) -> colorRGBA);
	}
	
	/**
	 * Fills {@code rectangle} in this {@code ImageD} instance with {@link Color4D} instances returned by {@code biFunction} as its color.
	 * <p>
	 * If either {@code rectangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rectangle the {@link Rectangle2I} to fill
	 * @param biFunction a {@code BiFunction} that returns {@code Color4D} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code rectangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final void fillRectangle(final Rectangle2I rectangle, final BiFunction<Color4D, Point2I, Color4D> biFunction) {
		Objects.requireNonNull(rectangle, "rectangle == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
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
					
					final Color4D oldColorRGBA = getColorRGBA(x, y);
					final Color4D newColorRGBA = Objects.requireNonNull(biFunction.apply(oldColorRGBA, point));
					
					setColorRGBA(newColorRGBA, x, y);
				}
			}
		}
	}
	
	/**
	 * Fills this {@code ImageD} instance with {@link Color4D} instances that are generated using a Simplex-based fractional Brownian motion (fBm) algorithm.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillSimplexFractionalBrownianMotion(new Color3D(0.75D, 0.5D, 0.75D));
	 * }
	 * </pre>
	 */
	public final void fillSimplexFractionalBrownianMotion() {
		fillSimplexFractionalBrownianMotion(new Color3D(0.75D, 0.5D, 0.75D));
	}
	
	/**
	 * Fills this {@code ImageD} instance with {@link Color4D} instances that are generated using a Simplex-based fractional Brownian motion (fBm) algorithm.
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
	 * @throws NullPointerException thrown if, and only if, {@code baseColor} is {@code null}
	 */
	public final void fillSimplexFractionalBrownianMotion(final Color3D baseColor) {
		fillSimplexFractionalBrownianMotion(baseColor, 5.0D, 0.5D, 16);
	}
	
	/**
	 * Fills this {@code ImageD} instance with {@link Color4D} instances that are generated using a Simplex-based fractional Brownian motion (fBm) algorithm.
	 * <p>
	 * If {@code baseColor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param baseColor a {@link Color3D} instance that is used as the base color
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param octaves the number of iterations to perform
	 * @throws NullPointerException thrown if, and only if, {@code baseColor} is {@code null}
	 */
	public final void fillSimplexFractionalBrownianMotion(final Color3D baseColor, final double frequency, final double gain, final int octaves) {
		Objects.requireNonNull(baseColor, "baseColor == null");
		
		final double minimumX = 0.0D;
		final double minimumY = 0.0D;
		final double maximumX = getResolutionX();
		final double maximumY = getResolutionY();
		
		update((color, point) -> {
			final double x = (point.getX() - minimumX) / (maximumX - minimumX);
			final double y = (point.getY() - minimumY) / (maximumY - minimumY);
			
			final double noise = simplexFractionalBrownianMotionXY(x, y, frequency, gain, 0.0D, 1.0D, octaves);
			
			return new Color4D(Color3D.multiply(baseColor, noise));
		});
	}
	
	/**
	 * Fills {@code triangle} in this {@code ImageD} instance with {@code Color4D.BLACK} as its color.
	 * <p>
	 * If {@code triangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillTriangle(triangle, Color4D.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param triangle the {@link Triangle2I} to fill
	 * @throws NullPointerException thrown if, and only if, {@code triangle} is {@code null}
	 */
	public final void fillTriangle(final Triangle2I triangle) {
		fillTriangle(triangle, Color4D.BLACK);
	}
	
	/**
	 * Fills {@code triangle} in this {@code ImageD} instance with {@code colorRGBA} as its color.
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
	 * @param colorRGBA the {@link Color4D} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code triangle} or {@code colorRGBA} are {@code null}
	 */
	public final void fillTriangle(final Triangle2I triangle, final Color4D colorRGBA) {
		Objects.requireNonNull(triangle, "triangle == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		fillTriangle(triangle, (color, point) -> colorRGBA);
	}
	
	/**
	 * Fills {@code triangle} in this {@code ImageD} instance with {@link Color4D} instances returned by {@code biFunction} as its color.
	 * <p>
	 * If either {@code triangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param triangle the {@link Triangle2I} to fill
	 * @param biFunction a {@code BiFunction} that returns {@code Color4D} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code triangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final void fillTriangle(final Triangle2I triangle, final BiFunction<Color4D, Point2I, Color4D> biFunction) {
		Objects.requireNonNull(triangle, "triangle == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		final Rectangle2I rectangle = new Rectangle2I(new Point2I(), new Point2I(getResolutionX(), getResolutionY()));
		
		final Point2I[][] scanlines = Rasterizer2I.rasterize(triangle, rectangle);
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		for(final Point2I[] scanline : scanlines) {
			for(final Point2I point : scanline) {
				final int x = point.getX();
				final int y = point.getY();
				
				if(x >= 0 && x < resolutionX && y >= 0 && y < resolutionY) {
					final Color4D oldColorRGBA = getColorRGBA(x, y);
					final Color4D newColorRGBA = Objects.requireNonNull(biFunction.apply(oldColorRGBA, point));
					
					setColorRGBA(newColorRGBA, point.getX(), point.getY());
				}
			}
		}
	}
	
	/**
	 * Converts this {@code ImageD} instance into grayscale using {@link Color4D#grayscaleAverage(Color4D)}.
	 */
	public final void grayscaleAverage() {
		update((color, point) -> Color4D.grayscaleAverage(color));
	}
	
	/**
	 * Converts this {@code ImageD} instance into grayscale using {@link Color4D#grayscaleComponent1(Color4D)}.
	 */
	public final void grayscaleComponent1() {
		update((color, point) -> Color4D.grayscaleComponent1(color));
	}
	
	/**
	 * Converts this {@code ImageD} instance into grayscale using {@link Color4D#grayscaleComponent2(Color4D)}.
	 */
	public final void grayscaleComponent2() {
		update((color, point) -> Color4D.grayscaleComponent2(color));
	}
	
	/**
	 * Converts this {@code ImageD} instance into grayscale using {@link Color4D#grayscaleComponent3(Color4D)}.
	 */
	public final void grayscaleComponent3() {
		update((color, point) -> Color4D.grayscaleComponent3(color));
	}
	
	/**
	 * Converts this {@code ImageD} instance into grayscale using {@link Color4D#grayscaleLightness(Color4D)}.
	 */
	public final void grayscaleLightness() {
		update((color, point) -> Color4D.grayscaleLightness(color));
	}
	
	/**
	 * Converts this {@code ImageD} instance into grayscale using {@link Color4D#grayscaleLuminance(Color4D)}.
	 */
	public final void grayscaleLuminance() {
		update((color, point) -> Color4D.grayscaleLuminance(color));
	}
	
	/**
	 * Converts this {@code ImageD} instance into grayscale using {@link Color4D#grayscaleMaximum(Color4D)}.
	 */
	public final void grayscaleMaximum() {
		update((color, point) -> Color4D.grayscaleMaximum(color));
	}
	
	/**
	 * Converts this {@code ImageD} instance into grayscale using {@link Color4D#grayscaleMinimum(Color4D)}.
	 */
	public final void grayscaleMinimum() {
		update((color, point) -> Color4D.grayscaleMinimum(color));
	}
	
	/**
	 * Inverts this {@code ImageD} instance.
	 */
	public final void invert() {
		update((color, point) -> Color4D.invert(color));
	}
	
	/**
	 * Multiplies this {@code ImageD} instance with {@code convolutionKernel}.
	 * <p>
	 * If {@code convolutionKernel} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param convolutionKernel a {@link ConvolutionKernel33D} instance
	 * @throws NullPointerException thrown if, and only if, {@code convolutionKernel} is {@code null}
	 */
	public final void multiply(final ConvolutionKernel33D convolutionKernel) {
		final Color3D factor = new Color3D(convolutionKernel.getFactor());
		final Color3D bias = new Color3D(convolutionKernel.getBias());
		
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
				
				setColorRGBA(colorRGBA, x, y);
			}
		}
	}
	
	/**
	 * Multiplies this {@code ImageD} instance with {@code convolutionKernel}.
	 * <p>
	 * If {@code convolutionKernel} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param convolutionKernel a {@link ConvolutionKernel55D} instance
	 * @throws NullPointerException thrown if, and only if, {@code convolutionKernel} is {@code null}
	 */
	public final void multiply(final ConvolutionKernel55D convolutionKernel) {
		final Color3D factor = new Color3D(convolutionKernel.getFactor());
		final Color3D bias = new Color3D(convolutionKernel.getBias());
		
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
				
				setColorRGBA(colorRGBA, x, y);
			}
		}
	}
	
	/**
	 * Redoes gamma correction on this {@code ImageD} instance using PBRT.
	 */
	public final void redoGammaCorrectionPBRT() {
		update((color, point) -> Color4D.redoGammaCorrectionPBRT(color));
	}
	
	/**
	 * Redoes gamma correction on this {@code ImageD} instance using sRGB.
	 */
	public final void redoGammaCorrectionSRGB() {
		update((color, point) -> Color4D.redoGammaCorrectionSRGB(color));
	}
	
	/**
	 * Converts this {@code ImageD} instance into its sepia-representation.
	 */
	public final void sepia() {
		update((color, point) -> Color4D.sepia(color));
	}
	
	/**
	 * Sets the {@link Color3D} of the pixel represented by {@code index} to {@code colorRGB}.
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
	 * @throws NullPointerException thrown if, and only if, {@code colorRGB} is {@code null}
	 */
	public final void setColorRGB(final Color3D colorRGB, final int index) {
		setColorRGB(colorRGB, index, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Sets the {@link Color3D} of the pixel represented by {@code index} to {@code colorRGB}.
	 * <p>
	 * If either {@code colorRGB} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param colorRGB the {@code Color3D} to set
	 * @param index the index of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGB} or {@code pixelOperation} are {@code null}
	 */
	public final void setColorRGB(final Color3D colorRGB, final int index, final PixelOperation pixelOperation) {
		setColorRGBA(new Color4D(colorRGB), index, pixelOperation);
	}
	
	/**
	 * Sets the {@link Color3D} of the pixel represented by {@code x} and {@code y} to {@code colorRGB}.
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
	 * @throws NullPointerException thrown if, and only if, {@code colorRGB} is {@code null}
	 */
	public final void setColorRGB(final Color3D colorRGB, final int x, final int y) {
		setColorRGB(colorRGB, x, y, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Sets the {@link Color3D} of the pixel represented by {@code x} and {@code y} to {@code colorRGB}.
	 * <p>
	 * If either {@code colorRGB} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param colorRGB the {@code Color3D} to set
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGB} or {@code pixelOperation} are {@code null}
	 */
	public final void setColorRGB(final Color3D colorRGB, final int x, final int y, final PixelOperation pixelOperation) {
		setColorRGBA(new Color4D(colorRGB), x, y, pixelOperation);
	}
	
	/**
	 * Sets the {@link Color4D} of the pixel represented by {@code index} to {@code colorRGBA}.
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
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
	public final void setColorRGBA(final Color4D colorRGBA, final int index) {
		setColorRGBA(colorRGBA, index, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Sets the {@link Color4D} of the pixel represented by {@code index} to {@code colorRGBA}.
	 * <p>
	 * If either {@code colorRGBA} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param colorRGBA the {@code Color4D} to set
	 * @param index the index of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBA} or {@code pixelOperation} are {@code null}
	 */
	public abstract void setColorRGBA(final Color4D colorRGBA, final int index, final PixelOperation pixelOperation);
	
	/**
	 * Sets the {@link Color4D} of the pixel represented by {@code x} and {@code y} to {@code colorRGBA}.
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
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
	public final void setColorRGBA(final Color4D colorRGBA, final int x, final int y) {
		setColorRGBA(colorRGBA, x, y, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Sets the {@link Color4D} of the pixel represented by {@code x} and {@code y} to {@code colorRGBA}.
	 * <p>
	 * If either {@code colorRGBA} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param colorRGBA the {@code Color4D} to set
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBA} or {@code pixelOperation} are {@code null}
	 */
	public abstract void setColorRGBA(final Color4D colorRGBA, final int x, final int y, final PixelOperation pixelOperation);
	
	/**
	 * Sets the transparency for this {@code ImageD} instance to {@code transparency}.
	 * 
	 * @param transparency the transparency
	 */
	public final void transparency(final double transparency) {
		update((color, point) -> new Color4D(color.getComponent1(), color.getComponent2(), color.getComponent3(), transparency));
	}
	
	/**
	 * Undoes gamma correction on this {@code ImageD} instance using PBRT.
	 */
	public final void undoGammaCorrectionPBRT() {
		update((color, point) -> Color4D.undoGammaCorrectionPBRT(color));
	}
	
	/**
	 * Undoes gamma correction on this {@code ImageD} instance using sRGB.
	 */
	public final void undoGammaCorrectionSRGB() {
		update((color, point) -> Color4D.undoGammaCorrectionSRGB(color));
	}
	
	/**
	 * Updates this {@code ImageD} instance by applying {@code biFunction} to all pixels.
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
	 * @throws NullPointerException thrown if, and only if, either {@code biFunction} or the result returned by {@code biFunction} are {@code null}
	 */
	public final void update(final BiFunction<Color4D, Point2I, Color4D> biFunction) {
		update(biFunction, getBounds());
	}
	
	/**
	 * Updates this {@code ImageD} instance by applying {@code biFunction} to all pixels within {@code bounds}.
	 * <p>
	 * If either {@code biFunction}, the result returned by {@code biFunction} or {@code bounds} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param biFunction a {@code BiFunction} that returns {@link Color4D} instances
	 * @param bounds a {@link Rectangle2I} instance used as the bounds for the update
	 * @throws NullPointerException thrown if, and only if, either {@code biFunction}, the result returned by {@code biFunction} or {@code bounds} are {@code null}
	 */
	public final void update(final BiFunction<Color4D, Point2I, Color4D> biFunction, final Rectangle2I bounds) {
		Objects.requireNonNull(biFunction, "biFunction == null");
		Objects.requireNonNull(bounds, "bounds == null");
		
		final Point2I minimum = bounds.getA();
		final Point2I maximum = bounds.getC();
		
		final int minimumX = max(minimum.getX(), 0);
		final int minimumY = max(minimum.getY(), 0);
		final int maximumX = min(maximum.getX(), getResolutionX());
		final int maximumY = min(maximum.getY(), getResolutionY());
		
		for(int y = minimumY; y < maximumY; y++) {
			for(int x = minimumX; x < maximumX; x++) {
				setColorRGBA(biFunction.apply(getColorRGBA(x, y), new Point2I(x, y)), x, y);
			}
		}
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
}