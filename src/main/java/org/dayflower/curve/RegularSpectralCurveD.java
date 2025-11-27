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
package org.dayflower.curve;

import java.util.Arrays;
import java.util.Objects;

import org.macroing.java.lang.Doubles;
import org.macroing.java.lang.Ints;
import org.macroing.java.lang.Strings;

/**
 * A {@code RegularSpectralCurveD} is an implementation of {@link SpectralCurveD} that contains regular spectral data.
 * <p>
 * This class is immutable and therefore suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class RegularSpectralCurveD extends SpectralCurveD {
	private final double delta;
	private final double deltaReciprocal;
	private final double lambdaMax;
	private final double lambdaMin;
	private final double[] spectrum;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code RegularSpectralCurveD} instance.
	 * <p>
	 * If {@code spectrum} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The array {@code spectrum} will be cloned.
	 * 
	 * @param lambdaMin the minimum wavelength in nanometers
	 * @param lambdaMax the maximum wavelength in nanometers
	 * @param spectrum an array with spectral data
	 * @throws NullPointerException thrown if, and only if, {@code spectrum} is {@code null}
	 */
	public RegularSpectralCurveD(final double lambdaMin, final double lambdaMax, final double[] spectrum) {
		this.lambdaMin = lambdaMin;
		this.lambdaMax = lambdaMax;
		this.spectrum = spectrum.clone();
		this.delta = (this.lambdaMax - this.lambdaMin) / (this.spectrum.length - 1);
		this.deltaReciprocal = 1.0D / this.delta;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code RegularSpectralCurveD} instance.
	 * 
	 * @return a {@code String} representation of this {@code RegularSpectralCurveD} instance
	 */
	@Override
	public String toString() {
		return String.format("new RegularSpectralCurveD(%s, %s, %s)", Strings.toNonScientificNotationJava(this.lambdaMin), Strings.toNonScientificNotationJava(this.lambdaMax), Strings.toNonScientificNotationJava(this.spectrum));
	}
	
	/**
	 * Compares {@code object} to this {@code RegularSpectralCurveD} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code RegularSpectralCurveD}, and they are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code RegularSpectralCurveD} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code RegularSpectralCurveD}, and they are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof RegularSpectralCurveD)) {
			return false;
		} else if(!Doubles.equals(this.lambdaMax, RegularSpectralCurveD.class.cast(object).lambdaMax)) {
			return false;
		} else if(!Doubles.equals(this.lambdaMin, RegularSpectralCurveD.class.cast(object).lambdaMin)) {
			return false;
		} else if(!Arrays.equals(this.spectrum, RegularSpectralCurveD.class.cast(object).spectrum)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the maximum wavelength in nanometers.
	 * 
	 * @return the maximum wavelength in nanometers
	 */
	public double getLambdaMax() {
		return this.lambdaMax;
	}
	
	/**
	 * Returns the minimum wavelength in nanometers.
	 * 
	 * @return the minimum wavelength in nanometers
	 */
	public double getLambdaMin() {
		return this.lambdaMin;
	}
	
	/**
	 * Returns a sample based on the wavelength {@code lambda} in nanometers.
	 * 
	 * @param lambda the wavelength in nanometers
	 * @return a sample based on the wavelength {@code lambda} in nanometers
	 */
	@Override
	public double sample(final double lambda) {
		if(lambda < this.lambdaMin || lambda > this.lambdaMax) {
			return 0.0D;
		}
		
		final double x = (lambda - this.lambdaMin) * this.deltaReciprocal;
		
		final int index0 = (int)(x);
		final int index1 = Ints.min(index0 + 1, this.spectrum.length - 1);
		
		final double deltaX = x - index0;
		
		final double sample = (1.0D - deltaX) * this.spectrum[index0] + deltaX * this.spectrum[index1];
		
		return sample;
	}
	
	/**
	 * Returns a {@code double[]} with the spectral data of this {@code RegularSpectralCurveD} instance.
	 * <p>
	 * Modifications to the {@code double[]} will not affect this {@code RegularSpectralCurveD} instance.
	 * 
	 * @return a {@code double[]} with the spectral data of this {@code RegularSpectralCurveD} instance
	 */
	public double[] getSpectrum() {
		return this.spectrum.clone();
	}
	
	/**
	 * Returns a hash code for this {@code RegularSpectralCurveD} instance.
	 * 
	 * @return a hash code for this {@code RegularSpectralCurveD} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(this.lambdaMax), Double.valueOf(this.lambdaMin), Integer.valueOf(Arrays.hashCode(this.spectrum)));
	}
}