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

import java.util.Objects;

import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.image.Color3F;

/**
 * A {@code BlendTexture} is a {@link Texture} implementation that returns a {@link Color3F} instance by blending the result of two other {@code Texture} instances.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BlendTexture implements Texture {
	private final Texture textureA;
	private final Texture textureB;
	private final float tComponent1;
	private final float tComponent2;
	private final float tComponent3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constucts a new {@code BlendTexture} instance.
	 * <p>
	 * If either {@code textureA} or {@code textureB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BlendTexture(textureA, textureB, 0.5F);
	 * }
	 * </pre>
	 * 
	 * @param textureA one of the two {@link Texture} instances to blend
	 * @param textureB one of the two {@code Texture} instances to blend
	 * @throws NullPointerException thrown if, and only if, either {@code textureA} or {@code textureB} are {@code null}
	 */
	public BlendTexture(final Texture textureA, final Texture textureB) {
		this(textureA, textureB, 0.5F);
	}
	
	/**
	 * Constucts a new {@code BlendTexture} instance.
	 * <p>
	 * If either {@code textureA} or {@code textureB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BlendTexture(textureA, textureB, t, t, t);
	 * }
	 * </pre>
	 * 
	 * @param textureA one of the two {@link Texture} instances to blend
	 * @param textureB one of the two {@code Texture} instances to blend
	 * @param t the factor to use for all components in the blending process
	 * @throws NullPointerException thrown if, and only if, either {@code textureA} or {@code textureB} are {@code null}
	 */
	public BlendTexture(final Texture textureA, final Texture textureB, final float t) {
		this(textureA, textureB, t, t, t);
	}
	
	/**
	 * Constucts a new {@code BlendTexture} instance.
	 * <p>
	 * If either {@code textureA} or {@code textureB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureA one of the two {@link Texture} instances to blend
	 * @param textureB one of the two {@code Texture} instances to blend
	 * @param tComponent1 the factor to use for component 1 in the blending process
	 * @param tComponent2 the factor to use for component 2 in the blending process
	 * @param tComponent3 the factor to use for component 3 in the blending process
	 * @throws NullPointerException thrown if, and only if, either {@code textureA} or {@code textureB} are {@code null}
	 */
	public BlendTexture(final Texture textureA, final Texture textureB, final float tComponent1, final float tComponent2, final float tComponent3) {
		this.textureA = Objects.requireNonNull(textureA, "textureA == null");
		this.textureB = Objects.requireNonNull(textureB, "textureB == null");
		this.tComponent1 = tComponent1;
		this.tComponent2 = tComponent2;
		this.tComponent3 = tComponent3;
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
		Objects.requireNonNull(surfaceIntersection, "surfaceIntersection == null");
		
		final Color3F colorA = this.textureA.getColor(surfaceIntersection);
		final Color3F colorB = this.textureB.getColor(surfaceIntersection);
		final Color3F colorC = Color3F.blend(colorA, colorB, this.tComponent1, this.tComponent2, this.tComponent3);
		
		return colorC;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code BlendTexture} instance.
	 * 
	 * @return a {@code String} representation of this {@code BlendTexture} instance
	 */
	@Override
	public String toString() {
		return String.format("new BlendTexture(%s, %s, %+.10f, %+.10f, %+.10f)", this.textureA, this.textureB, Float.valueOf(this.tComponent1), Float.valueOf(this.tComponent2), Float.valueOf(this.tComponent3));
	}
	
	/**
	 * Compares {@code object} to this {@code BlendTexture} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code BlendTexture}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code BlendTexture} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code BlendTexture}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof BlendTexture)) {
			return false;
		} else if(!Objects.equals(this.textureA, BlendTexture.class.cast(object).textureA)) {
			return false;
		} else if(!Objects.equals(this.textureB, BlendTexture.class.cast(object).textureB)) {
			return false;
		} else if(!equal(this.tComponent1, BlendTexture.class.cast(object).tComponent1)) {
			return false;
		} else if(!equal(this.tComponent2, BlendTexture.class.cast(object).tComponent2)) {
			return false;
		} else if(!equal(this.tComponent3, BlendTexture.class.cast(object).tComponent3)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code BlendTexture} instance.
	 * 
	 * @return a hash code for this {@code BlendTexture} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureA, this.textureB, Float.valueOf(this.tComponent1), Float.valueOf(this.tComponent2), Float.valueOf(this.tComponent3));
	}
}