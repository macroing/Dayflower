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

/**
 * An {@code IrregularSpectralCurveD} is an implementation of {@link SpectralCurveD} that contains irregular spectral data.
 * <p>
 * This class is immutable and therefore suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class IrregularSpectralCurveD extends SpectralCurveD {
	/**
	 * An {@code IrregularSpectralCurveD} instance for silver (Ag).
	 */
	public static final IrregularSpectralCurveD AG_ETA = new IrregularSpectralCurveD(SPDD.AG_ETA, SPDD.AG_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveD} instance for silver (Ag).
	 */
	public static final IrregularSpectralCurveD AG_K = new IrregularSpectralCurveD(SPDD.AG_K, SPDD.AG_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveD} instance for aluminum (Al).
	 */
	public static final IrregularSpectralCurveD AL_ETA = new IrregularSpectralCurveD(SPDD.AL_ETA, SPDD.AL_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveD} instance for aluminum (Al).
	 */
	public static final IrregularSpectralCurveD AL_K = new IrregularSpectralCurveD(SPDD.AL_K, SPDD.AL_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveD} instance for gold (Au).
	 */
	public static final IrregularSpectralCurveD AU_ETA = new IrregularSpectralCurveD(SPDD.AU_ETA, SPDD.AU_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveD} instance for gold (Au).
	 */
	public static final IrregularSpectralCurveD AU_K = new IrregularSpectralCurveD(SPDD.AU_K, SPDD.AU_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveD} instance for beryllium (Be).
	 */
	public static final IrregularSpectralCurveD BE_ETA = new IrregularSpectralCurveD(SPDD.BE_ETA, SPDD.BE_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveD} instance for beryllium (Be).
	 */
	public static final IrregularSpectralCurveD BE_K = new IrregularSpectralCurveD(SPDD.BE_K, SPDD.BE_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveD} instance for chromium (Cr).
	 */
	public static final IrregularSpectralCurveD CR_ETA = new IrregularSpectralCurveD(SPDD.CR_ETA, SPDD.CR_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveD} instance for chromium (Cr).
	 */
	public static final IrregularSpectralCurveD CR_K = new IrregularSpectralCurveD(SPDD.CR_K, SPDD.CR_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveD} instance for copper (Cu).
	 */
	public static final IrregularSpectralCurveD CU_ETA = new IrregularSpectralCurveD(SPDD.CU_ETA, SPDD.CU_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveD} instance for copper (Cu).
	 */
	public static final IrregularSpectralCurveD CU_K = new IrregularSpectralCurveD(SPDD.CU_K, SPDD.CU_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveD} instance for mercury (Hg).
	 */
	public static final IrregularSpectralCurveD HG_ETA = new IrregularSpectralCurveD(SPDD.HG_ETA, SPDD.HG_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveD} instance for mercury (Hg).
	 */
	public static final IrregularSpectralCurveD HG_K = new IrregularSpectralCurveD(SPDD.HG_K, SPDD.HG_WAVELENGTH);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final double[] amplitudes;
	private final double[] wavelengths;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code IrregularSpectralCurveD} instance.
	 * <p>
	 * If either {@code amplitudes} or {@code wavelengths} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Both arrays {@code amplitudes} and {@code wavelengths} will be cloned.
	 * 
	 * @param amplitudes an array with amplitudes
	 * @param wavelengths an array with wavelengths in nanometers
	 * @throws NullPointerException thrown if, and only if, either {@code amplitudes} or {@code wavelengths} are {@code null}
	 */
	public IrregularSpectralCurveD(final double[] amplitudes, final double[] wavelengths) {
		this.amplitudes = amplitudes.clone();
		this.wavelengths = wavelengths.clone();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a sample based on the wavelength {@code lambda} in nanometers.
	 * 
	 * @param lambda the wavelength in nanometers
	 * @return a sample based on the wavelength {@code lambda} in nanometers
	 */
	@Override
	public double sample(final double lambda) {
		if(this.wavelengths.length == 0) {
			return 0.0D;
		} else if(this.wavelengths.length == 1 || lambda <= this.wavelengths[0]) {
			return this.amplitudes[0];
		} else if(lambda >= this.wavelengths[this.wavelengths.length - 1]) {
			return this.amplitudes[this.wavelengths.length - 1];
		} else {
			for(int i = 1; i < this.wavelengths.length; i++) {
				if(lambda < this.wavelengths[i]) {
					final double deltaX = (lambda - this.wavelengths[i - 1]) / (this.wavelengths[i] - this.wavelengths[i - 1]);
					final double sample = (1.0D - deltaX) * this.amplitudes[i - 1] + deltaX * this.amplitudes[i];
					
					return sample;
				}
			}
			
			return this.amplitudes[this.wavelengths.length - 1];
		}
	}
}