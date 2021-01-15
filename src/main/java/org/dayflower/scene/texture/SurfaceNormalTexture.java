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
package org.dayflower.scene.texture;

import java.util.Objects;

import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;

/**
 * A {@code SurfaceNormalTexture} is a {@link Texture} implementation that returns a {@link Color3F} instance based on the surface normal of a surface.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Texture} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SurfaceNormalTexture implements Texture {
	/**
	 * The ID of this {@code SurfaceNormalTexture} class.
	 */
	public static final int ID = 9;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SurfaceNormalTexture} instance.
	 */
	public SurfaceNormalTexture() {
		
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
		final Vector3F surfaceNormal = intersection.getSurfaceIntersectionObjectSpace().getOrthonormalBasisS().getW();
		
		final float r = (surfaceNormal.getX() + 1.0F) / 2.0F;
		final float g = (surfaceNormal.getY() + 1.0F) / 2.0F;
		final float b = (surfaceNormal.getZ() + 1.0F) / 2.0F;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SurfaceNormalTexture} instance.
	 * 
	 * @return a {@code String} representation of this {@code SurfaceNormalTexture} instance
	 */
	@Override
	public String toString() {
		return "new SurfaceNormalTexture()";
	}
	
	/**
	 * Compares {@code object} to this {@code SurfaceNormalTexture} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SurfaceNormalTexture}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SurfaceNormalTexture} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SurfaceNormalTexture}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SurfaceNormalTexture)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a {@code float} representing the value of the surface at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code float} representing the value of the surface at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public float getFloat(final Intersection intersection) {
		return getColor(intersection).average();
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code SurfaceNormalTexture} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code SurfaceNormalTexture} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code SurfaceNormalTexture} instance.
	 * 
	 * @return a hash code for this {@code SurfaceNormalTexture} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash();
	}
}