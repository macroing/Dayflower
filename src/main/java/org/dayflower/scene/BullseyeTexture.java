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

import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.remainder;

import java.util.Objects;

import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;

/**
 * A {@code BullseyeTexture} is a {@link Texture} implementation that returns a {@link Color3F} instance by alternating between two other {@code Texture} instances in a bullseye pattern.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BullseyeTexture implements Texture {
	private final Point3F origin;
	private final Texture textureA;
	private final Texture textureB;
	private final float scale;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code BullseyeTexture} instance.
	 * <p>
	 * If either {@code textureA} or {@code textureB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BullseyeTexture(textureA, textureB, new Point3F());
	 * }
	 * </pre>
	 * 
	 * @param textureA one of the two {@link Texture} instances to use
	 * @param textureB one of the two {@code Texture} instances to use
	 * @throws NullPointerException thrown if, and only if, either {@code textureA} or {@code textureB} are {@code null}
	 */
	public BullseyeTexture(final Texture textureA, final Texture textureB) {
		this(textureA, textureB, new Point3F());
	}
	
	/**
	 * Constructs a new {@code BullseyeTexture} instance.
	 * <p>
	 * If either {@code textureA}, {@code textureB} or {@code origin} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BullseyeTexture(textureA, textureB, origin, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param textureA one of the two {@link Texture} instances to use
	 * @param textureB one of the two {@code Texture} instances to use
	 * @param origin a {@link Point3F} instance used as the origin for the bullseye pattern
	 * @throws NullPointerException thrown if, and only if, either {@code textureA}, {@code textureB} or {@code origin} are {@code null}
	 */
	public BullseyeTexture(final Texture textureA, final Texture textureB, final Point3F origin) {
		this(textureA, textureB, origin, 1.0F);
	}
	
	/**
	 * Constructs a new {@code BullseyeTexture} instance.
	 * <p>
	 * If either {@code textureA}, {@code textureB} or {@code origin} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureA one of the two {@link Texture} instances to use
	 * @param textureB one of the two {@code Texture} instances to use
	 * @param origin a {@link Point3F} instance used as the origin for the bullseye pattern
	 * @param scale the scale for the bullseye pattern
	 * @throws NullPointerException thrown if, and only if, either {@code textureA}, {@code textureB} or {@code origin} are {@code null}
	 */
	public BullseyeTexture(final Texture textureA, final Texture textureB, final Point3F origin, final float scale) {
		this.textureA = Objects.requireNonNull(textureA, "textureA == null");
		this.textureB = Objects.requireNonNull(textureB, "textureB == null");
		this.origin = Objects.requireNonNull(origin, "origin == null");
		this.scale = scale;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance representing the color of the surface at {@code surfaceIntersection}.
	 * <p>
	 * If {@code surfaceIntersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param surfaceIntersection a {@link SurfaceIntersection3F} instance
	 * @return a {@code Color3F} instance representing the color of the surface at {@code surfaceIntersection}
	 * @throws NullPointerException thrown if, and only if, {@code surfaceIntersection} is {@code null}
	 */
	@Override
	public Color3F getColor(final SurfaceIntersection3F surfaceIntersection) {
		final Vector3F direction = Vector3F.direction(this.origin, surfaceIntersection.getSurfaceIntersectionPoint());
		
		final float directionLength = direction.length();
		final float directionLengthScaled = directionLength * this.scale;
		final float directionLengthScaledRemainder = remainder(directionLengthScaled, 1.0F);
		
		return directionLengthScaledRemainder > 0.5F ? this.textureA.getColor(surfaceIntersection) : this.textureB.getColor(surfaceIntersection);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code BullseyeTexture} instance.
	 * 
	 * @return a {@code String} representation of this {@code BullseyeTexture} instance
	 */
	@Override
	public String toString() {
		return String.format("new BullseyeTexture(%s, %s, %s, %+.10f)", this.origin, this.textureA, this.textureB, Float.valueOf(this.scale));
	}
	
	/**
	 * Compares {@code object} to this {@code BullseyeTexture} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code BullseyeTexture}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code BullseyeTexture} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code BullseyeTexture}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof BullseyeTexture)) {
			return false;
		} else if(!Objects.equals(this.origin, BullseyeTexture.class.cast(object).origin)) {
			return false;
		} else if(!Objects.equals(this.textureA, BullseyeTexture.class.cast(object).textureA)) {
			return false;
		} else if(!Objects.equals(this.textureB, BullseyeTexture.class.cast(object).textureB)) {
			return false;
		} else if(!equal(this.scale, BullseyeTexture.class.cast(object).scale)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code BullseyeTexture} instance.
	 * 
	 * @return a hash code for this {@code BullseyeTexture} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.origin, this.textureA, this.textureB, Float.valueOf(this.scale));
	}
}