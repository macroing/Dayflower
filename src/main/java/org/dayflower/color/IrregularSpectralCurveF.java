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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

import org.dayflower.java.io.FloatArrayOutputStream;
import org.dayflower.utility.Strings;

/**
 * An {@code IrregularSpectralCurveF} is an implementation of {@link SpectralCurveF} that contains irregular spectral data.
 * <p>
 * This class is immutable and therefore suitable for concurrent use without external synchronization.
 * <p>
 * To read SPD files from PBRT, the method {@link #parseSPD(File)} may be used.
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
	
	/**
	 * Returns a {@code float[]} with the amplitudes of this {@code IrregularSpectralCurveF} instance.
	 * <p>
	 * Modifications to the {@code float[]} will not affect this {@code IrregularSpectralCurveF} instance.
	 * 
	 * @return a {@code float[]} with the amplitudes of this {@code IrregularSpectralCurveF} instance
	 */
	public float[] getAmplitudes() {
		return this.amplitudes.clone();
	}
	
	/**
	 * Returns a {@code float[]} with the wavelengths of this {@code IrregularSpectralCurveF} instance.
	 * <p>
	 * Modifications to the {@code float[]} will not affect this {@code IrregularSpectralCurveF} instance.
	 * 
	 * @return a {@code float[]} with the wavelengths of this {@code IrregularSpectralCurveF} instance
	 */
	public float[] getWavelengths() {
		return this.wavelengths.clone();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Parses an SPD file in the PBRT format.
	 * <p>
	 * Returns an {@code IrregularSpectralCurveF} instance.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} that represents the file to parse
	 * @return an {@code IrregularSpectralCurveF} instance
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static IrregularSpectralCurveF parseSPD(final File file) {
		try(final BufferedReader bufferedReader = new BufferedReader(new FileReader(Objects.requireNonNull(file, "file == null"))); final FloatArrayOutputStream floatArrayOutputStreamAmplitudes = new FloatArrayOutputStream(); final FloatArrayOutputStream floatArrayOutputStreamWavelengths = new FloatArrayOutputStream()) {
			for(String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
				final String[] elements = line.split("\\s+");
				
				final float amplitude = Float.parseFloat(elements[1]);
				final float wavelength = Float.parseFloat(elements[0]);
				
				floatArrayOutputStreamAmplitudes.write(amplitude);
				floatArrayOutputStreamWavelengths.write(wavelength);
			}
			
			final float[] amplitudes = floatArrayOutputStreamAmplitudes.toFloatArray();
			final float[] wavelengths = floatArrayOutputStreamWavelengths.toFloatArray();
			
			return new IrregularSpectralCurveF(amplitudes, wavelengths);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Prints the {@code IrregularSpectralCurveF} representations of the SPD files in the PBRT format to standard output in Java source code format.
	 * <p>
	 * If either {@code fileEta} or {@code fileK} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param fileEta a {@code File} that represents the file to parse for Eta
	 * @param fileK a {@code File} that represents the file to parse for K
	 * @throws NullPointerException thrown if, and only if, either {@code fileEta} or {@code fileK} are {@code null}
	 */
	public static void printSPDToJava(final File fileEta, final File fileK) {
		Objects.requireNonNull(fileEta, "fileEta == null");
		Objects.requireNonNull(fileK, "fileK == null");
		
		final IrregularSpectralCurveF irregularSpectralCurveEta = parseSPD(fileEta);
		final IrregularSpectralCurveF irregularSpectralCurveK = parseSPD(fileK);
		
		System.out.println(Strings.toConstantJavaField("ETA", irregularSpectralCurveEta.getAmplitudes()));
		System.out.println(Strings.toConstantJavaField("K", irregularSpectralCurveK.getAmplitudes()));
		System.out.println(Strings.toConstantJavaField("WAVELENGTH", irregularSpectralCurveEta.getWavelengths()));
	}
}