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
package org.dayflower.wavefront.material;

import java.util.Objects;

import org.dayflower.util.Strings;

/**
 * An {@code RGBAmbientReflectivityStatement} represents an ambient reflectivity statement using RGB ({@code "Ka"}) of a Wavefront Material file.
 * <p>
 * This class is currently thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class RGBAmbientReflectivityStatement implements AmbientReflectivityStatement {
	private final float b;
	private final float g;
	private final float r;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code RGBAmbientReflectivityStatement} instance.
	 * 
	 * @param r the R-component
	 * @param g the G-component
	 * @param b the B-component
	 */
	public RGBAmbientReflectivityStatement(final float r, final float g, final float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the name of this {@code RGBAmbientReflectivityStatement} instance.
	 * 
	 * @return the name of this {@code RGBAmbientReflectivityStatement} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code RGBAmbientReflectivityStatement} instance.
	 * 
	 * @return a {@code String} representation of this {@code RGBAmbientReflectivityStatement} instance
	 */
	@Override
	public String toString() {
		return String.format("%s %s %s %s", getName(), Strings.toNonScientificNotation(getR()), Strings.toNonScientificNotation(getG()), Strings.toNonScientificNotation(getB()));
	}
	
	/**
	 * Compares {@code object} to this {@code RGBAmbientReflectivityStatement} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code RGBAmbientReflectivityStatement}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code RGBAmbientReflectivityStatement} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code RGBAmbientReflectivityStatement}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof RGBAmbientReflectivityStatement)) {
			return false;
		} else if(!Objects.equals(getName(), RGBAmbientReflectivityStatement.class.cast(object).getName())) {
			return false;
		} else if(Float.compare(getB(), RGBAmbientReflectivityStatement.class.cast(object).getB()) != 0) {
			return false;
		} else if(Float.compare(getG(), RGBAmbientReflectivityStatement.class.cast(object).getG()) != 0) {
			return false;
		} else if(Float.compare(getR(), RGBAmbientReflectivityStatement.class.cast(object).getR()) != 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the B-component.
	 * 
	 * @return the B-component
	 */
	public float getB() {
		return this.b;
	}
	
	/**
	 * Returns the G-component.
	 * 
	 * @return the G-component
	 */
	public float getG() {
		return this.g;
	}
	
	/**
	 * Returns the R-component.
	 * 
	 * @return the R-component
	 */
	public float getR() {
		return this.r;
	}
	
	/**
	 * Returns a hash code for this {@code RGBAmbientReflectivityStatement} instance.
	 * 
	 * @return a hash code for this {@code RGBAmbientReflectivityStatement} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), Float.valueOf(getB()), Float.valueOf(getG()), Float.valueOf(getR()));
	}
}