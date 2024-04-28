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
package org.dayflower.curve;

import java.util.Objects;

import org.macroing.java.lang.Doubles;
import org.macroing.java.lang.Strings;

/**
 * A {@code ConstantSpectralCurveD} is an implementation of {@link SpectralCurveD} that returns a constant value.
 * <p>
 * This class is immutable and therefore suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ConstantSpectralCurveD extends SpectralCurveD {
	private final double amplitude;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ConstantSpectralCurveD} instance.
	 * 
	 * @param amplitude the constant value to use
	 */
	public ConstantSpectralCurveD(final double amplitude) {
		this.amplitude = amplitude;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code ConstantSpectralCurveD} instance.
	 * 
	 * @return a {@code String} representation of this {@code ConstantSpectralCurveD} instance
	 */
	@Override
	public String toString() {
		return String.format("new ConstantSpectralCurveD(%s)", Strings.toNonScientificNotationJava(this.amplitude));
	}
	
	/**
	 * Compares {@code object} to this {@code ConstantSpectralCurveD} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ConstantSpectralCurveD}, and they are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ConstantSpectralCurveD} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ConstantSpectralCurveD}, and they are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ConstantSpectralCurveD)) {
			return false;
		} else if(!Doubles.equals(this.amplitude, ConstantSpectralCurveD.class.cast(object).amplitude)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the amplitude associated with this {@code ConstantSpectralCurveD} instance.
	 * 
	 * @return the amplitude associated with this {@code ConstantSpectralCurveD} instance
	 */
	public double getAmplitude() {
		return this.amplitude;
	}
	
	/**
	 * Returns a sample based on the wavelength {@code lambda} in nanometers.
	 * <p>
	 * This implementation returns a constant value.
	 * 
	 * @param lambda the wavelength in nanometers
	 * @return a sample based on the wavelength {@code lambda} in nanometers
	 */
	@Override
	public double sample(final double lambda) {
		return this.amplitude;
	}
	
	/**
	 * Returns a hash code for this {@code ConstantSpectralCurveD} instance.
	 * 
	 * @return a hash code for this {@code ConstantSpectralCurveD} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(this.amplitude));
	}
}