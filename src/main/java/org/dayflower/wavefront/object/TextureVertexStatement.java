/**
 * Copyright 2020 - 2021 J&#246;rgen Lundgren
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
package org.dayflower.wavefront.object;

import java.util.Objects;

import org.dayflower.util.Strings;

/**
 * A {@code TextureVertexStatement} represents a texture vertex statement ({@code "vt"}) of a Wavefront Object file.
 * <p>
 * This class is currently thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class TextureVertexStatement implements ObjectFileStatement {
	/**
	 * The name of this {@code TextureVertexStatement} which is {@code "vt"}.
	 */
	public static final String NAME = "vt";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final float u;
	private final float v;
	private final float w;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code TextureVertexStatement} instance.
	 * <p>
	 * Calling this constructor is equivalent to {@code new TextureVertexStatement(u, 0.0F)}.
	 * 
	 * @param u the U-component
	 */
	public TextureVertexStatement(final float u) {
		this(u, 0.0F);
	}
	
	/**
	 * Constructs a new {@code TextureVertexStatement} instance.
	 * <p>
	 * Calling this constructor is equivalent to {@code new TextureVertexStatement(u, v, 0.0F)}.
	 * 
	 * @param u the U-component
	 * @param v the V-component
	 */
	public TextureVertexStatement(final float u, final float v) {
		this(u, v, 0.0F);
	}
	
	/**
	 * Constructs a new {@code TextureVertexStatement} instance.
	 * 
	 * @param u the U-component
	 * @param v the V-component
	 * @param w the W-component
	 */
	public TextureVertexStatement(final float u, final float v, final float w) {
		this.u = u;
		this.v = v;
		this.w = w;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the name of this {@code TextureVertexStatement} instance.
	 * 
	 * @return the name of this {@code TextureVertexStatement} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code TextureVertexStatement} instance.
	 * 
	 * @return a {@code String} representation of this {@code TextureVertexStatement} instance
	 */
	@Override
	public String toString() {
		return String.format("%s %s %s %s", getName(), Strings.toNonScientificNotation(getU()), Strings.toNonScientificNotation(getV()), Strings.toNonScientificNotation(getW()));
	}
	
	/**
	 * Compares {@code object} to this {@code TextureVertexStatement} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code TextureVertexStatement}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code TextureVertexStatement} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code TextureVertexStatement}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof TextureVertexStatement)) {
			return false;
		} else if(!Objects.equals(getName(), TextureVertexStatement.class.cast(object).getName())) {
			return false;
		} else if(Float.compare(getU(), TextureVertexStatement.class.cast(object).getU()) != 0) {
			return false;
		} else if(Float.compare(getV(), TextureVertexStatement.class.cast(object).getV()) != 0) {
			return false;
		} else if(Float.compare(getW(), TextureVertexStatement.class.cast(object).getW()) != 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the U-component.
	 * 
	 * @return the U-component
	 */
	public float getU() {
		return this.u;
	}
	
	/**
	 * Returns the V-component.
	 * 
	 * @return the V-component
	 */
	public float getV() {
		return this.v;
	}
	
	/**
	 * Returns the W-component.
	 * 
	 * @return the W-component
	 */
	public float getW() {
		return this.w;
	}
	
	/**
	 * Returns a hash code for this {@code TextureVertexStatement} instance.
	 * 
	 * @return a hash code for this {@code TextureVertexStatement} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), Float.valueOf(getU()), Float.valueOf(getV()), Float.valueOf(getW()));
	}
}