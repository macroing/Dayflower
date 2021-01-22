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
 * An {@code IrregularSpectralCurveF} is an implementation of {@link SpectralCurveF} that contains irregular spectral data.
 * <p>
 * This class is immutable and therefore suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class IrregularSpectralCurveF extends SpectralCurveF {
	/**
	 * An {@code IrregularSpectralCurveF} instance for silver (Ag).
	 */
	public static final IrregularSpectralCurveF AG_ETA = new IrregularSpectralCurveF(SPDF.AG_ETA, SPDF.AG_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveF} instance for silver (Ag).
	 */
	public static final IrregularSpectralCurveF AG_K = new IrregularSpectralCurveF(SPDF.AG_K, SPDF.AG_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveF} instance for aluminum (Al).
	 */
	public static final IrregularSpectralCurveF AL_ETA = new IrregularSpectralCurveF(SPDF.AL_ETA, SPDF.AL_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveF} instance for aluminum (Al).
	 */
	public static final IrregularSpectralCurveF AL_K = new IrregularSpectralCurveF(SPDF.AL_K, SPDF.AL_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveF} instance for gold (Au).
	 */
	public static final IrregularSpectralCurveF AU_ETA = new IrregularSpectralCurveF(SPDF.AU_ETA, SPDF.AU_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveF} instance for gold (Au).
	 */
	public static final IrregularSpectralCurveF AU_K = new IrregularSpectralCurveF(SPDF.AU_K, SPDF.AU_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveF} instance for beryllium (Be).
	 */
	public static final IrregularSpectralCurveF BE_ETA = new IrregularSpectralCurveF(SPDF.BE_ETA, SPDF.BE_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveF} instance for beryllium (Be).
	 */
	public static final IrregularSpectralCurveF BE_K = new IrregularSpectralCurveF(SPDF.BE_K, SPDF.BE_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveF} instance for chromium (Cr).
	 */
	public static final IrregularSpectralCurveF CR_ETA = new IrregularSpectralCurveF(SPDF.CR_ETA, SPDF.CR_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveF} instance for chromium (Cr).
	 */
	public static final IrregularSpectralCurveF CR_K = new IrregularSpectralCurveF(SPDF.CR_K, SPDF.CR_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveF} instance for copper (Cu).
	 */
	public static final IrregularSpectralCurveF CU_ETA = new IrregularSpectralCurveF(SPDF.CU_ETA, SPDF.CU_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveF} instance for copper (Cu).
	 */
	public static final IrregularSpectralCurveF CU_K = new IrregularSpectralCurveF(SPDF.CU_K, SPDF.CU_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveF} instance for mercury (Hg).
	 */
	public static final IrregularSpectralCurveF HG_ETA = new IrregularSpectralCurveF(SPDF.HG_ETA, SPDF.HG_WAVELENGTH);
	
	/**
	 * An {@code IrregularSpectralCurveF} instance for mercury (Hg).
	 */
	public static final IrregularSpectralCurveF HG_K = new IrregularSpectralCurveF(SPDF.HG_K, SPDF.HG_WAVELENGTH);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final float[] amplitudes;
	private final float[] wavelengths;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code IrregularSpectralCurveF} instance.
	 * <p>
	 * If either {@code amplitudes} or {@code wavelengths} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Both arrays {@code amplitudes} and {@code wavelengths} will be cloned.
	 * 
	 * @param amplitudes an array with amplitudes
	 * @param wavelengths an array with wavelengths in nanometers
	 * @throws NullPointerException thrown if, and only if, either {@code amplitudes} or {@code wavelengths} are {@code null}
	 */
	public IrregularSpectralCurveF(final float[] amplitudes, final float[] wavelengths) {
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
	public float sample(final float lambda) {
		if(this.wavelengths.length == 0) {
			return 0.0F;
		} else if(this.wavelengths.length == 1 || lambda <= this.wavelengths[0]) {
			return this.amplitudes[0];
		} else if(lambda >= this.wavelengths[this.wavelengths.length - 1]) {
			return this.amplitudes[this.wavelengths.length - 1];
		} else {
			for(int i = 1; i < this.wavelengths.length; i++) {
				if(lambda < this.wavelengths[i]) {
					final float deltaX = (lambda - this.wavelengths[i - 1]) / (this.wavelengths[i] - this.wavelengths[i - 1]);
					final float sample = (1.0F - deltaX) * this.amplitudes[i - 1] + deltaX * this.amplitudes[i];
					
					return sample;
				}
			}
			
			return this.amplitudes[this.wavelengths.length - 1];
		}
	}
}