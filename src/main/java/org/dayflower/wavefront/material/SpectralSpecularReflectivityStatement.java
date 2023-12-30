/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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

import org.macroing.java.lang.Strings;

/**
 * A {@code SpectralSpecularReflectivityStatement} represents a specular reflectivity statement using spectral data ({@code "Ks spectral"}) of a Wavefront Material file.
 * <p>
 * This class is currently thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SpectralSpecularReflectivityStatement implements SpecularReflectivityStatement {
	/**
	 * The name of the second part of this {@code SpectralSpecularReflectivityStatement}, which is {@code "spectral"}.
	 * <p>
	 * The full name of this {@code SpectralSpecularReflectivityStatement} would be {@code "Ks spectral"}.
	 */
	public static final String NAME = "spectral";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final String filename;
	private final float factor;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SpectralSpecularReflectivityStatement} instance.
	 * <p>
	 * Calling this constructor is equivalent to {@code new SpectralSpecularReflectivityStatement(filename, 1.0F)}.
	 * <p>
	 * If {@code filename} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param filename the filename to use
	 * @throws NullPointerException thrown if, and only if, {@code filename} is {@code null}
	 */
	public SpectralSpecularReflectivityStatement(final String filename) {
		this(filename, 1.0F);
	}
	
	/**
	 * Constructs a new {@code SpectralSpecularReflectivityStatement} instance.
	 * <p>
	 * If {@code filename} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param filename the filename to use
	 * @param factor the factor to use
	 * @throws NullPointerException thrown if, and only if, {@code filename} is {@code null}
	 */
	public SpectralSpecularReflectivityStatement(final String filename, final float factor) {
		this.filename = Objects.requireNonNull(filename, "filename == null");
		this.factor = factor;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the filename of this {@code SpectralSpecularReflectivityStatement} instance.
	 * 
	 * @return the filename of this {@code SpectralSpecularReflectivityStatement} instance
	 */
	public String getFilename() {
		return this.filename;
	}
	
	/**
	 * Returns the name of this {@code SpectralSpecularReflectivityStatement} instance.
	 * 
	 * @return the name of this {@code SpectralSpecularReflectivityStatement} instance
	 */
	@Override
	public String getName() {
		return String.format("%s %s", SpecularReflectivityStatement.NAME, NAME);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SpectralSpecularReflectivityStatement} instance.
	 * 
	 * @return a {@code String} representation of this {@code SpectralSpecularReflectivityStatement} instance
	 */
	@Override
	public String toString() {
		return String.format("%s %s %s", getName(), getFilename(), Strings.toNonScientificNotation(getFactor()));
	}
	
	/**
	 * Compares {@code object} to this {@code SpectralSpecularReflectivityStatement} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SpectralSpecularReflectivityStatement}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SpectralSpecularReflectivityStatement} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SpectralSpecularReflectivityStatement}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SpectralSpecularReflectivityStatement)) {
			return false;
		} else if(!Objects.equals(getName(), SpectralSpecularReflectivityStatement.class.cast(object).getName())) {
			return false;
		} else if(Float.compare(getFactor(), SpectralSpecularReflectivityStatement.class.cast(object).getFactor()) != 0) {
			return false;
		} else if(!Objects.equals(getFilename(), SpectralSpecularReflectivityStatement.class.cast(object).getFilename())) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the factor of this {@code SpectralSpecularReflectivityStatement} instance.
	 * 
	 * @return the factor of this {@code SpectralSpecularReflectivityStatement} instance
	 */
	public float getFactor() {
		return this.factor;
	}
	
	/**
	 * Returns a hash code for this {@code SpectralSpecularReflectivityStatement} instance.
	 * 
	 * @return a hash code for this {@code SpectralSpecularReflectivityStatement} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), Float.valueOf(getFactor()), getFilename());
	}
}