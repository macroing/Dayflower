/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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

import org.macroing.java.lang.Strings;

/**
 * A {@code GeometricVertexStatement} represents a geometric vertex statement ({@code "v"}) of a Wavefront Object file.
 * <p>
 * This class is currently thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class GeometricVertexStatement implements ObjectFileStatement {
	/**
	 * The name of this {@code GeometricVertexStatement} which is {@code "v"}.
	 */
	public static final String NAME = "v";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final float w;
	private final float x;
	private final float y;
	private final float z;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code GeometricVertexStatement} instance.
	 * <p>
	 * Calling this constuctor is equivalent to {@code new GeometricVertexStatement(x, y, z, 1.0F)}.
	 * 
	 * @param x the X-component
	 * @param y the Y-component
	 * @param z the Z-component
	 */
	public GeometricVertexStatement(final float x, final float y, final float z) {
		this(x, y, z, 1.0F);
	}
	
	/**
	 * Constructs a new {@code GeometricVertexStatement} instance.
	 * 
	 * @param x the X-component
	 * @param y the Y-component
	 * @param z the Z-component
	 * @param w the W-component
	 */
	public GeometricVertexStatement(final float x, final float y, final float z, final float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the name of this {@code GeometricVertexStatement} instance.
	 * 
	 * @return the name of this {@code GeometricVertexStatement} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code GeometricVertexStatement} instance.
	 * 
	 * @return a {@code String} representation of this {@code GeometricVertexStatement} instance
	 */
	@Override
	public String toString() {
		return String.format("%s %s %s %s %s", getName(), Strings.toNonScientificNotation(getX()), Strings.toNonScientificNotation(getY()), Strings.toNonScientificNotation(getZ()), Strings.toNonScientificNotation(getW()));
	}
	
	/**
	 * Compares {@code object} to this {@code GeometricVertexStatement} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code GeometricVertexStatement}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code GeometricVertexStatement} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code GeometricVertexStatement}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof GeometricVertexStatement)) {
			return false;
		} else if(!Objects.equals(getName(), GeometricVertexStatement.class.cast(object).getName())) {
			return false;
		} else if(Float.compare(getW(), GeometricVertexStatement.class.cast(object).getW()) != 0) {
			return false;
		} else if(Float.compare(getX(), GeometricVertexStatement.class.cast(object).getX()) != 0) {
			return false;
		} else if(Float.compare(getY(), GeometricVertexStatement.class.cast(object).getY()) != 0) {
			return false;
		} else if(Float.compare(getZ(), GeometricVertexStatement.class.cast(object).getZ()) != 0) {
			return false;
		} else {
			return true;
		}
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
	 * Returns the X-component.
	 * 
	 * @return the X-component
	 */
	public float getX() {
		return this.x;
	}
	
	/**
	 * Returns the Y-component.
	 * 
	 * @return the Y-component
	 */
	public float getY() {
		return this.y;
	}
	
	/**
	 * Returns the Z-component.
	 * 
	 * @return the Z-component
	 */
	public float getZ() {
		return this.z;
	}
	
	/**
	 * Returns a hash code for this {@code GeometricVertexStatement} instance.
	 * 
	 * @return a hash code for this {@code GeometricVertexStatement} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), Float.valueOf(getW()), Float.valueOf(getX()), Float.valueOf(getY()), Float.valueOf(getZ()));
	}
}