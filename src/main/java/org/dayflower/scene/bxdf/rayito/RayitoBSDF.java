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
package org.dayflower.scene.bxdf.rayito;

import java.util.Objects;

import org.dayflower.image.Color3F;

/**
 * A {@code RayitoBSDF} represents a BSDF (Bidirectional Scattering Distribution Function).
 * <p>
 * This class can be considered immutable and thread-safe if, and only if, its associated {@link RayitoBXDF} instance is.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class RayitoBSDF {
	private final Color3F color;
	private final RayitoBXDF rayitoBXDF;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code RayitoBSDF} instance.
	 * <p>
	 * If either {@code color} or {@code rayitoBXDF} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color the {@link Color3F} instance associated with this {@code RayitoBSDF} instance
	 * @param rayitoBXDF the {@link RayitoBXDF} instance associated with this {@code RayitoBSDF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code color} or {@code rayitoBXDF} are {@code null}
	 */
	public RayitoBSDF(final Color3F color, final RayitoBXDF rayitoBXDF) {
		this.color = Objects.requireNonNull(color, "color == null");
		this.rayitoBXDF = Objects.requireNonNull(rayitoBXDF, "rayitoBXDF == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link Color3F} instance associated with this {@code RayitoBSDF} instance.
	 * 
	 * @return the {@code Color3F} instance associated with this {@code RayitoBSDF} instance
	 */
	public Color3F getColor() {
		return this.color;
	}
	
	/**
	 * Returns the {@link RayitoBXDF} instance associated with this {@code RayitoBSDF} instance.
	 * 
	 * @return the {@code RayitoBXDF} instance associated with this {@code RayitoBSDF} instance
	 */
	public RayitoBXDF getRayitoBXDF() {
		return this.rayitoBXDF;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code RayitoBSDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code RayitoBSDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new RayitoBSDF(%s, %s)", this.color, this.rayitoBXDF);
	}
	
	/**
	 * Compares {@code object} to this {@code RayitoBSDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code RayitoBSDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code RayitoBSDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code RayitoBSDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof RayitoBSDF)) {
			return false;
		} else if(!Objects.equals(this.color, RayitoBSDF.class.cast(object).color)) {
			return false;
		} else if(!Objects.equals(this.rayitoBXDF, RayitoBSDF.class.cast(object).rayitoBXDF)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code RayitoBSDF} instance.
	 * 
	 * @return a hash code for this {@code RayitoBSDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.color, this.rayitoBXDF);
	}
}