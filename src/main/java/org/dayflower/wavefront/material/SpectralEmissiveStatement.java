/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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
 * A {@code SpectralEmissiveStatement} represents a non-official emissive statement using spectral data ({@code "Ke spectral"}) of a Wavefront Material file.
 * <p>
 * This class is currently thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SpectralEmissiveStatement implements EmissiveStatement {
	/**
	 * The name of the second part of this {@code SpectralEmissiveStatement}, which is {@code "spectral"}.
	 * <p>
	 * The full name of this {@code SpectralEmissiveStatement} would be {@code "Ke spectral"}.
	 */
	public static final String NAME = "spectral";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final String filename;
	private final float factor;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SpectralEmissiveStatement} instance.
	 * <p>
	 * Calling this constructor is equivalent to {@code new SpectralEmissiveStatement(filename, 1.0F)}.
	 * <p>
	 * If {@code filename} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param filename the filename to use
	 * @throws NullPointerException thrown if, and only if, {@code filename} is {@code null}
	 */
	public SpectralEmissiveStatement(final String filename) {
		this(filename, 1.0F);
	}
	
	/**
	 * Constructs a new {@code SpectralEmissiveStatement} instance.
	 * <p>
	 * If {@code filename} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param filename the filename to use
	 * @param factor the factor to use
	 * @throws NullPointerException thrown if, and only if, {@code filename} is {@code null}
	 */
	public SpectralEmissiveStatement(final String filename, final float factor) {
		this.filename = Objects.requireNonNull(filename, "filename == null");
		this.factor = factor;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the filename of this {@code SpectralEmissiveStatement} instance.
	 * 
	 * @return the filename of this {@code SpectralEmissiveStatement} instance
	 */
	public String getFilename() {
		return this.filename;
	}
	
	/**
	 * Returns the name of this {@code SpectralEmissiveStatement} instance.
	 * 
	 * @return the name of this {@code SpectralEmissiveStatement} instance
	 */
	@Override
	public String getName() {
		return String.format("%s %s", EmissiveStatement.NAME, NAME);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SpectralEmissiveStatement} instance.
	 * 
	 * @return a {@code String} representation of this {@code SpectralEmissiveStatement} instance
	 */
	@Override
	public String toString() {
		return String.format("%s %s %s", getName(), getFilename(), Strings.toNonScientificNotation(getFactor()));
	}
	
	/**
	 * Compares {@code object} to this {@code SpectralEmissiveStatement} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SpectralEmissiveStatement}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SpectralEmissiveStatement} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SpectralEmissiveStatement}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SpectralEmissiveStatement)) {
			return false;
		} else if(!Objects.equals(getName(), SpectralEmissiveStatement.class.cast(object).getName())) {
			return false;
		} else if(Float.compare(getFactor(), SpectralEmissiveStatement.class.cast(object).getFactor()) != 0) {
			return false;
		} else if(!Objects.equals(getFilename(), SpectralEmissiveStatement.class.cast(object).getFilename())) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the factor of this {@code SpectralEmissiveStatement} instance.
	 * 
	 * @return the factor of this {@code SpectralEmissiveStatement} instance
	 */
	public float getFactor() {
		return this.factor;
	}
	
	/**
	 * Returns a hash code for this {@code SpectralEmissiveStatement} instance.
	 * 
	 * @return a hash code for this {@code SpectralEmissiveStatement} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), Float.valueOf(getFactor()), getFilename());
	}
}