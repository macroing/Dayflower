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

import org.macroing.java.lang.Floats;
import org.macroing.java.lang.Ints;
import org.macroing.java.lang.Strings;

/**
 * A {@code RegularSpectralCurveF} is an implementation of {@link SpectralCurveF} that contains regular spectral data.
 * <p>
 * This class is immutable and therefore suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class RegularSpectralCurveF extends SpectralCurveF {
	private final float delta;
	private final float deltaReciprocal;
	private final float lambdaMax;
	private final float lambdaMin;
	private final float[] spectrum;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code RegularSpectralCurveF} instance.
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
	public RegularSpectralCurveF(final float lambdaMin, final float lambdaMax, final float[] spectrum) {
		this.lambdaMin = lambdaMin;
		this.lambdaMax = lambdaMax;
		this.spectrum = spectrum.clone();
		this.delta = (this.lambdaMax - this.lambdaMin) / (this.spectrum.length - 1);
		this.deltaReciprocal = 1.0F / this.delta;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code RegularSpectralCurveF} instance.
	 * 
	 * @return a {@code String} representation of this {@code RegularSpectralCurveF} instance
	 */
	@Override
	public String toString() {
		return String.format("new RegularSpectralCurveF(%s, %s, %s)", Strings.toNonScientificNotationJava(this.lambdaMin), Strings.toNonScientificNotationJava(this.lambdaMax), Strings.toNonScientificNotationJava(this.spectrum));
	}
	
	/**
	 * Compares {@code object} to this {@code RegularSpectralCurveF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code RegularSpectralCurveF}, and they are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code RegularSpectralCurveF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code RegularSpectralCurveF}, and they are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof RegularSpectralCurveF)) {
			return false;
		} else if(!Floats.equals(this.lambdaMax, RegularSpectralCurveF.class.cast(object).lambdaMax)) {
			return false;
		} else if(!Floats.equals(this.lambdaMin, RegularSpectralCurveF.class.cast(object).lambdaMin)) {
			return false;
		} else if(!Arrays.equals(this.spectrum, RegularSpectralCurveF.class.cast(object).spectrum)) {
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
	public float getLambdaMax() {
		return this.lambdaMax;
	}
	
	/**
	 * Returns the minimum wavelength in nanometers.
	 * 
	 * @return the minimum wavelength in nanometers
	 */
	public float getLambdaMin() {
		return this.lambdaMin;
	}
	
	/**
	 * Returns a sample based on the wavelength {@code lambda} in nanometers.
	 * 
	 * @param lambda the wavelength in nanometers
	 * @return a sample based on the wavelength {@code lambda} in nanometers
	 */
	@Override
	public float sample(final float lambda) {
		if(lambda < this.lambdaMin || lambda > this.lambdaMax) {
			return 0.0F;
		}
		
		final float x = (lambda - this.lambdaMin) * this.deltaReciprocal;
		
		final int index0 = (int)(x);
		final int index1 = Ints.min(index0 + 1, this.spectrum.length - 1);
		
		final float deltaX = x - index0;
		
		final float sample = (1.0F - deltaX) * this.spectrum[index0] + deltaX * this.spectrum[index1];
		
		return sample;
	}
	
	/**
	 * Returns a {@code float[]} with the spectral data of this {@code RegularSpectralCurveF} instance.
	 * <p>
	 * Modifications to the {@code float[]} will not affect this {@code RegularSpectralCurveF} instance.
	 * 
	 * @return a {@code float[]} with the spectral data of this {@code RegularSpectralCurveF} instance
	 */
	public float[] getSpectrum() {
		return this.spectrum.clone();
	}
	
	/**
	 * Returns a hash code for this {@code RegularSpectralCurveF} instance.
	 * 
	 * @return a hash code for this {@code RegularSpectralCurveF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.lambdaMax), Float.valueOf(this.lambdaMin), Integer.valueOf(Arrays.hashCode(this.spectrum)));
	}
}