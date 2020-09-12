/**
 * Copyright 2020 J&#246;rgen Lundgren
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
package org.dayflower.scene.texture;

import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.cos;
import static org.dayflower.util.Floats.remainder;
import static org.dayflower.util.Floats.sin;

import java.util.Objects;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.image.PixelOperation;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Texture;

/**
 * An {@code ImageTexture} is a {@link Texture} implementation that returns a {@link Color3F} instance from an {@link Image} instance.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ImageTexture implements Texture {
	private final AngleF angle;
	private final Image image;
	private final Vector2F scale;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ImageTexture} instance.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ImageTexture(image, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param image the {@link Image} instance to copy and use
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	public ImageTexture(final Image image) {
		this(image, AngleF.degrees(0.0F));
	}
	
	/**
	 * Constructs a new {@code ImageTexture} instance.
	 * <p>
	 * If either {@code image} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ImageTexture(image, angle, new Vector2F(1.0F, 1.0F));
	 * }
	 * </pre>
	 * 
	 * @param image the {@link Image} instance to copy and use
	 * @param angle the {@link AngleF} instance to use
	 * @throws NullPointerException thrown if, and only if, either {@code image} or {@code angle} are {@code null}
	 */
	public ImageTexture(final Image image, final AngleF angle) {
		this(image, angle, new Vector2F(1.0F, 1.0F));
	}
	
	/**
	 * Constructs a new {@code ImageTexture} instance.
	 * <p>
	 * If either {@code image}, {@code angle} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param image the {@link Image} instance to copy and use
	 * @param angle the {@link AngleF} instance to use
	 * @param scale the {@link Vector2F} instance to use as the scale factor
	 * @throws NullPointerException thrown if, and only if, either {@code image}, {@code angle} or {@code scale} are {@code null}
	 */
	public ImageTexture(final Image image, final AngleF angle, final Vector2F scale) {
		this.image = Objects.requireNonNull(image, "image == null").copy();
		this.angle = Objects.requireNonNull(angle, "angle == null");
		this.scale = Objects.requireNonNull(scale, "scale == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance representing the color of the surface at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance representing the color of the surface at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F getColor(final Intersection intersection) {
		final Point2F textureCoordinates = intersection.getSurfaceIntersectionObjectSpace().getTextureCoordinates();
		
		final float cosAngleRadians = cos(this.angle.getRadians());
		final float sinAngleRadians = sin(this.angle.getRadians());
		
		final float resolutionX = this.image.getResolutionX();
		final float resolutionY = this.image.getResolutionY();
		
		final float u = remainder((textureCoordinates.getU() * cosAngleRadians - textureCoordinates.getV() * sinAngleRadians) * this.scale.getU() * resolutionX, resolutionX);
		final float v = remainder((textureCoordinates.getV() * cosAngleRadians + textureCoordinates.getU() * sinAngleRadians) * this.scale.getV() * resolutionY, resolutionY);
		
		final float x = u >= 0.0F ? u : resolutionX - abs(u);
		final float y = v >= 0.0F ? v : resolutionY - abs(v);
		
		return this.image.getColorRGB(x, y, PixelOperation.WRAP_AROUND);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ImageTexture} instance.
	 * 
	 * @return a {@code String} representation of this {@code ImageTexture} instance
	 */
	@Override
	public String toString() {
		return String.format("new ImageTexture(%s, %s, %s)", this.image, this.angle, this.scale);
	}
	
	/**
	 * Compares {@code object} to this {@code ImageTexture} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ImageTexture}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ImageTexture} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ImageTexture}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ImageTexture)) {
			return false;
		} else if(!Objects.equals(this.angle, ImageTexture.class.cast(object).angle)) {
			return false;
		} else if(!Objects.equals(this.image, ImageTexture.class.cast(object).image)) {
			return false;
		} else if(!Objects.equals(this.scale, ImageTexture.class.cast(object).scale)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code ImageTexture} instance.
	 * 
	 * @return a hash code for this {@code ImageTexture} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.angle, this.image, this.scale);
	}
}