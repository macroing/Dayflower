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
package org.dayflower.scene;

import static org.dayflower.util.Floats.cos;
import static org.dayflower.util.Floats.fractionalPart;
import static org.dayflower.util.Floats.sin;

import java.util.Objects;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Vector2F;
import org.dayflower.image.Color3F;

/**
 * A {@code CheckerboardTexture} is a {@link Texture} implementation that returns a {@link Color3F} instance by alternating between two other {@code Texture} instances in a checkerboard pattern.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CheckerboardTexture implements Texture {
	private final AngleF angle;
	private final Texture textureA;
	private final Texture textureB;
	private final Vector2F scale;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CheckerboardTexture} instance.
	 * <p>
	 * If either {@code textureA} or {@code textureB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new CheckerboardTexture(textureA, textureB, AngleF.degrees(0.0F));
	 * }
	 * </pre>
	 * 
	 * @param textureA one of the two {@link Texture} instances to use
	 * @param textureB one of the two {@code Texture} instances to use
	 * @throws NullPointerException thrown if, and only if, either {@code textureA} or {@code textureB} are {@code null}
	 */
	public CheckerboardTexture(final Texture textureA, final Texture textureB) {
		this(textureA, textureB, AngleF.degrees(0.0F));
	}
	
	/**
	 * Constructs a new {@code CheckerboardTexture} instance.
	 * <p>
	 * If either {@code textureA}, {@code textureB} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new CheckerboardTexture(textureA, textureB, angle, new Vector2F(1.0F, 1.0F));
	 * }
	 * </pre>
	 * 
	 * @param textureA one of the two {@link Texture} instances to use
	 * @param textureB one of the two {@code Texture} instances to use
	 * @param angle the {@link AngleF} instance to use
	 * @throws NullPointerException thrown if, and only if, either {@code textureA}, {@code textureB} or {@code angle} are {@code null}
	 */
	public CheckerboardTexture(final Texture textureA, final Texture textureB, final AngleF angle) {
		this(textureA, textureB, angle, new Vector2F(1.0F, 1.0F));
	}
	
	/**
	 * Constructs a new {@code CheckerboardTexture} instance.
	 * <p>
	 * If either {@code textureA}, {@code textureB}, {@code angle} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureA one of the two {@link Texture} instances to use
	 * @param textureB one of the two {@code Texture} instances to use
	 * @param angle the {@link AngleF} instance to use
	 * @param scale the {@link Vector2F} instance to use as the scale factor
	 * @throws NullPointerException thrown if, and only if, either {@code textureA}, {@code textureB}, {@code angle} or {@code scale} are {@code null}
	 */
	public CheckerboardTexture(final Texture textureA, final Texture textureB, final AngleF angle, final Vector2F scale) {
		this.textureA = Objects.requireNonNull(textureA, "textureA == null");
		this.textureB = Objects.requireNonNull(textureB, "textureB == null");
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
		final float cosAngleRadians = cos(this.angle.getRadians());
		final float sinAngleRadians = sin(this.angle.getRadians());
		
		final float u = intersection.getSurfaceIntersectionObjectSpace().getTextureCoordinates().getU();
		final float v = intersection.getSurfaceIntersectionObjectSpace().getTextureCoordinates().getV();
		
		final boolean isU = fractionalPart((u * cosAngleRadians - v * sinAngleRadians) * this.scale.getU()) > 0.5F;
		final boolean isV = fractionalPart((v * cosAngleRadians + u * sinAngleRadians) * this.scale.getV()) > 0.5F;
		
		return isU ^ isV ? this.textureA.getColor(intersection) : this.textureB.getColor(intersection);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code CheckerboardTexture} instance.
	 * 
	 * @return a {@code String} representation of this {@code CheckerboardTexture} instance
	 */
	@Override
	public String toString() {
		return String.format("new CheckerboardTexture(%s, %s, %s, %s)", this.angle, this.textureA, this.textureB, this.scale);
	}
	
	/**
	 * Compares {@code object} to this {@code CheckerboardTexture} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code CheckerboardTexture}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code CheckerboardTexture} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code CheckerboardTexture}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof CheckerboardTexture)) {
			return false;
		} else if(!Objects.equals(this.angle, CheckerboardTexture.class.cast(object).angle)) {
			return false;
		} else if(!Objects.equals(this.textureA, CheckerboardTexture.class.cast(object).textureA)) {
			return false;
		} else if(!Objects.equals(this.textureB, CheckerboardTexture.class.cast(object).textureB)) {
			return false;
		} else if(!Objects.equals(this.scale, CheckerboardTexture.class.cast(object).scale)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code CheckerboardTexture} instance.
	 * 
	 * @return a hash code for this {@code CheckerboardTexture} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.angle, this.textureA, this.textureB, this.scale);
	}
}