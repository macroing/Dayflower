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

import org.dayflower.color.Color3F;
import org.macroing.java.lang.Floats;
import org.macroing.java.lang.Strings;

/**
 * A {@code ChromaticSpectralCurveF} is an implementation of {@link SpectralCurveF} that contains chromaticity pairs.
 * <p>
 * This class is immutable and therefore suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ChromaticSpectralCurveF extends SpectralCurveF {
	private static final Color3F S0_XYZ;
	private static final Color3F S1_XYZ;
	private static final Color3F S2_XYZ;
	private static final SpectralCurveF K_S0_SPECTRAL_CURVE;
	private static final SpectralCurveF K_S1_SPECTRAL_CURVE;
	private static final SpectralCurveF K_S2_SPECTRAL_CURVE;
	private static final float[] S0_AMPLITUDES;
	private static final float[] S1_AMPLITUDES;
	private static final float[] S2_AMPLITUDES;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final float m1;
	private final float m2;
	private final float x;
	private final float y;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	static {
		S0_AMPLITUDES = new float[] {0.04F, 6.0F, 29.6F, 55.3F, 57.3F, 61.8F, 61.5F, 68.8F, 63.4F, 65.8F, 94.8F, 104.8F, 105.9F, 96.8F, 113.9F, 125.6F, 125.5F, 121.3F, 121.3F, 113.5F, 113.1F, 110.8F, 106.5F, 108.8F, 105.3F, 104.4F, 100.0F, 96.0F, 95.1F, 89.1F, 90.5F, 90.3F, 88.4F, 84.0F,  85.1F,  81.9F,  82.6F,  84.9F,  81.3F,  71.9F,  74.3F,  76.4F,  63.3F,  71.7F,  77.0F,  65.2F, 47.7F,  68.6F,  65.0F,  66.0F, 61.0F, 53.3F, 58.9F, 61.9F};
		S1_AMPLITUDES = new float[] {0.02F, 4.5F, 22.4F, 42.0F, 40.6F, 41.6F, 38.0F, 42.4F, 38.5F, 35.0F, 43.4F,  46.3F,  43.9F, 37.1F,  36.7F,  35.9F,  32.6F,  27.9F,  24.3F,  20.1F,  16.2F,  13.2F,   8.6F,   6.1F,   4.2F,   1.9F,   0.0F, -1.6F, -3.5F, -3.5F, -5.8F, -7.2F, -8.6F, -9.5F, -10.9F, -10.7F, -12.0F, -14.0F, -13.6F, -12.0F, -13.3F, -12.9F, -10.6F, -11.6F, -12.2F, -10.2F, -7.8F, -11.2F, -10.4F, -10.6F, -9.7F, -8.3F, -9.3F, -9.8F};
		S2_AMPLITUDES = new float[] {0.00F, 2.0F,  4.0F,  8.5F,  7.8F,  6.7F,  5.3F,  6.1F,  3.0F,  1.2F, -1.1F,  -0.5F,  -0.7F, -1.2F,  -2.6F,  -2.9F,  -2.8F,  -2.6F,  -2.6F,  -1.8F,  -1.5F,  -1.3F,  -1.2F,  -1.0F,  -0.5F,  -0.3F,   0.0F,  0.2F,  0.5F,  2.1F,  3.2F,  4.1F,  4.7F,  5.1F,   6.7F,   7.3F,   8.6F,   9.8F,  10.2F,   8.3F,   9.6F,   8.5F,   7.0F,   7.6F,   8.0F,   6.7F,  5.2F,   7.4F,   6.8F,   7.0F,  6.4F,  5.5F,  6.1F,  6.5F};
		
		K_S0_SPECTRAL_CURVE = new RegularSpectralCurveF(300.0F, 830.0F, S0_AMPLITUDES);
		K_S1_SPECTRAL_CURVE = new RegularSpectralCurveF(300.0F, 830.0F, S1_AMPLITUDES);
		K_S2_SPECTRAL_CURVE = new RegularSpectralCurveF(300.0F, 830.0F, S2_AMPLITUDES);
		
		S0_XYZ = K_S0_SPECTRAL_CURVE.toColorXYZ();
		S1_XYZ = K_S1_SPECTRAL_CURVE.toColorXYZ();
		S2_XYZ = K_S2_SPECTRAL_CURVE.toColorXYZ();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ChromaticSpectralCurveF} instance given the chromaticity pair of {@code x} and {@code y}.
	 * 
	 * @param x the X-component of the chromaticity pair
	 * @param y the Y-component of the chromaticity pair
	 */
	public ChromaticSpectralCurveF(final float x, final float y) {
		this.m1 = (-1.3515F -  1.7703F * x +  5.9114F * y) / (0.0241F + 0.2562F * x - 0.7341F * y);
		this.m2 = (+0.03F   - 31.4424F * x + 30.0717F * y) / (0.0241F + 0.2562F * x - 0.7341F * y);
		this.x = x;
		this.y = y;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code ChromaticSpectralCurveF} instance.
	 * 
	 * @return a {@code String} representation of this {@code ChromaticSpectralCurveF} instance
	 */
	@Override
	public String toString() {
		return String.format("new ChromaticSpectralCurveF(%s, %s)", Strings.toNonScientificNotationJava(this.x), Strings.toNonScientificNotationJava(this.y));
	}
	
	/**
	 * Compares {@code object} to this {@code ChromaticSpectralCurveF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ChromaticSpectralCurveF}, and they are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ChromaticSpectralCurveF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ChromaticSpectralCurveF}, and they are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ChromaticSpectralCurveF)) {
			return false;
		} else if(!Floats.equals(this.x, ChromaticSpectralCurveF.class.cast(object).x)) {
			return false;
		} else if(!Floats.equals(this.y, ChromaticSpectralCurveF.class.cast(object).y)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the X-component of the chromaticity pair that is associated with this {@code ChromaticSpectralCurveF} instance.
	 * 
	 * @return the X-component of the chromaticity pair that is associated with this {@code ChromaticSpectralCurveF} instance
	 */
	public float getX() {
		return this.x;
	}
	
	/**
	 * Returns the Y-component of the chromaticity pair that is associated with this {@code ChromaticSpectralCurveF} instance.
	 * 
	 * @return the Y-component of the chromaticity pair that is associated with this {@code ChromaticSpectralCurveF} instance
	 */
	public float getY() {
		return this.y;
	}
	
	/**
	 * Returns a sample based on the wavelength {@code lambda} in nanometers.
	 * 
	 * @param lambda the wavelength in nanometers
	 * @return a sample based on the wavelength {@code lambda} in nanometers
	 */
	@Override
	public float sample(final float lambda) {
		return K_S0_SPECTRAL_CURVE.sample(lambda) + this.m1 * K_S1_SPECTRAL_CURVE.sample(lambda) + this.m2 * K_S2_SPECTRAL_CURVE.sample(lambda);
	}
	
	/**
	 * Returns a hash code for this {@code ChromaticSpectralCurveF} instance.
	 * 
	 * @return a hash code for this {@code ChromaticSpectralCurveF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.x), Float.valueOf(this.y));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} given {@code x} and {@code y} in XYZ-color space.
	 * 
	 * @param x the X-component of the chromaticity pair
	 * @param y the Y-component of the chromaticity pair
	 * @return a {@code Color3F} given {@code x} and {@code y} in XYZ-color space
	 */
	public static Color3F toColorXYZ(final float x, final float y) {
		final float m1 = (-1.3515F - 1.7703F  * x +  5.9114F * y) / (0.0241F + 0.2562F * x - 0.7341F * y);
		final float m2 = (+0.03F   - 31.4424F * x + 30.0717F * y) / (0.0241F + 0.2562F * x - 0.7341F * y);
		
		final float x0 = S0_XYZ.r + m1 * S1_XYZ.r + m2 * S2_XYZ.r;
		final float y0 = S0_XYZ.g + m1 * S1_XYZ.g + m2 * S2_XYZ.g;
		final float z0 = S0_XYZ.b + m1 * S1_XYZ.b + m2 * S2_XYZ.b;
		
		return new Color3F(x0, y0, z0);
	}
}