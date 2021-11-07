/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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
package org.dayflower.color;

import static org.dayflower.utility.Ints.min;

import java.lang.reflect.Field;//TODO: Add Unit Tests!

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
//	TODO: Add Unit Tests!
	public RegularSpectralCurveD(final double lambdaMin, final double lambdaMax, final double[] spectrum) {
		this.lambdaMin = lambdaMin;
		this.lambdaMax = lambdaMax;
		this.spectrum = spectrum.clone();
		this.delta = (this.lambdaMax - this.lambdaMin) / (this.spectrum.length - 1);
		this.deltaReciprocal = 1.0D / this.delta;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a sample based on the wavelength {@code lambda} in nanometers.
	 * 
	 * @param lambda the wavelength in nanometers
	 * @return a sample based on the wavelength {@code lambda} in nanometers
	 */
//	TODO: Add Unit Tests!
	@Override
	public double sample(final double lambda) {
		if(lambda < this.lambdaMin || lambda > this.lambdaMax) {
			return 0.0D;
		}
		
		final double x = (lambda - this.lambdaMin) * this.deltaReciprocal;
		
		final int index0 = (int)(x);
		final int index1 = min(index0 + 1, this.spectrum.length - 1);
		
		final double deltaX = x - index0;
		
		final double sample = (1.0D - deltaX) * this.spectrum[index0] + deltaX * this.spectrum[index1];
		
		return sample;
	}
}