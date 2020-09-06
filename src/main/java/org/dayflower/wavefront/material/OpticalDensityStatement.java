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
 * An {@code OpticalDensityStatement} represents an optical density (or index of refraction) statement ({@code "Ni"}) of a Wavefront Material file.
 * <p>
 * This class is currently thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class OpticalDensityStatement implements MaterialFileStatement {
	/**
	 * The name of this {@code OpticalDensityStatement} which is {@code "Ni"}.
	 */
	public static final String NAME = "Ni";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final float opticalDensity;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code OpticalDensityStatement} instance.
	 * 
	 * @param opticalDensity the optical density to use
	 */
	public OpticalDensityStatement(final float opticalDensity) {
		this.opticalDensity = opticalDensity;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the name of this {@code OpticalDensityStatement} instance.
	 * 
	 * @return the name of this {@code OpticalDensityStatement} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code OpticalDensityStatement} instance.
	 * 
	 * @return a {@code String} representation of this {@code OpticalDensityStatement} instance
	 */
	@Override
	public String toString() {
		return String.format("%s %s", getName(), Strings.toNonScientificNotation(getOpticalDensity()));
	}
	
	/**
	 * Compares {@code object} to this {@code OpticalDensityStatement} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code OpticalDensityStatement}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code OpticalDensityStatement} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code OpticalDensityStatement}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof OpticalDensityStatement)) {
			return false;
		} else if(!Objects.equals(getName(), OpticalDensityStatement.class.cast(object).getName())) {
			return false;
		} else if(Float.compare(getOpticalDensity(), OpticalDensityStatement.class.cast(object).getOpticalDensity()) != 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the optical density.
	 * 
	 * @return the optical density
	 */
	public float getOpticalDensity() {
		return this.opticalDensity;
	}
	
	/**
	 * Returns a hash code for this {@code OpticalDensityStatement} instance.
	 * 
	 * @return a hash code for this {@code OpticalDensityStatement} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), Float.valueOf(getOpticalDensity()));
	}
}