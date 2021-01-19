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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	static {
		S0_AMPLITUDES = new float[] {0.04F, 6.0F, 29.6F, 55.3F, 57.3F, 61.8F, 61.5F, 68.8F, 63.4F, 65.8F, 94.8F, 104.8F, 105.9F, 96.8F, 113.9F, 125.6F, 125.5F, 121.3F, 121.3F, 113.5F, 113.1F, 110.8F, 106.5F, 108.8F, 105.3F, 104.4F, 100.0F, 96.0F, 95.1F, 89.1F, 90.5F, 90.3F, 88.4F, 84.0F, 85.1F, 81.9F, 82.6F, 84.9F, 81.3F, 71.9F, 74.3F, 76.4F, 63.3F, 71.7F, 77.0F, 65.2F, 47.7F, 68.6F, 65.0F, 66.0F, 61.0F, 53.3F, 58.9F, 61.9F};
		S1_AMPLITUDES = new float[] {0.02F, 4.5F, 22.4F, 42.0F, 40.6F, 41.6F, 38.0F, 42.4F, 38.5F, 35.0F, 43.4F, 46.3F, 43.9F, 37.1F, 36.7F, 35.9F, 32.6F, 27.9F, 24.3F, 20.1F, 16.2F, 13.2F, 8.6F, 6.1F, 4.2F, 1.9F, 0.0F, -1.6F, -3.5F, -3.5F, -5.8F, -7.2F, -8.6F, -9.5F, -10.9F, -10.7F, -12.0F, -14.0F, -13.6F, -12.0F, -13.3F, -12.9F, -10.6F, -11.6F, -12.2F, -10.2F, -7.8F, -11.2F, -10.4F, -10.6F, -9.7F, -8.3F, -9.3F, -9.8F};
		S2_AMPLITUDES = new float[] {0.0F, 2.0F, 4.0F, 8.5F, 7.8F, 6.7F, 5.3F, 6.1F, 3.0F, 1.2F, -1.1F, -0.5F, -0.7F, -1.2F, -2.6F, -2.9F, -2.8F, -2.6F, -2.6F, -1.8F, -1.5F, -1.3F, -1.2F, -1.0F, -0.5F, -0.3F, 0.0F, 0.2F, 0.5F, 2.1F, 3.2F, 4.1F, 4.7F, 5.1F, 6.7F, 7.3F, 8.6F, 9.8F, 10.2F, 8.3F, 9.6F, 8.5F, 7.0F, 7.6F, 8.0F, 6.7F, 5.2F, 7.4F, 6.8F, 7.0F, 6.4F, 5.5F, 6.1F, 6.5F};
		
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
	 * @param x the X of the chromaticity pair
	 * @param y the Y of the chromaticity pair
	 */
	public ChromaticSpectralCurveF(final float x, final float y) {
		this.m1 = (-1.3515F -  1.7703F * x +  5.9114F * y) / (0.0241F + 0.2562F * x - 0.7341F * y);
		this.m2 = (+0.03F   - 31.4424F * x + 30.0717F * y) / (0.0241F + 0.2562F * x - 0.7341F * y);
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
		return K_S0_SPECTRAL_CURVE.sample(lambda) + this.m1 * K_S1_SPECTRAL_CURVE.sample(lambda) + this.m2 * K_S2_SPECTRAL_CURVE.sample(lambda);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} given {@code x} and {@code y} in XYZ-color space.
	 * 
	 * @param x the X of the chromaticity pair
	 * @param y the Y of the chromaticity pair
	 * @return a {@code Color3F} given {@code x} and {@code y} in XYZ-color space
	 */
	public static Color3F getColorXYZ(final float x, final float y) {
		final float m1 = (-1.3515F - 1.7703F  * x +  5.9114F * y) / (0.0241F + 0.2562F * x - 0.7341F * y);
		final float m2 = (+0.03F   - 31.4424F * x + 30.0717F * y) / (0.0241F + 0.2562F * x - 0.7341F * y);
		
		final float x0 = S0_XYZ.getX() + m1 * S1_XYZ.getX() + m2 * S2_XYZ.getX();
		final float y0 = S0_XYZ.getY() + m1 * S1_XYZ.getY() + m2 * S2_XYZ.getY();
		final float z0 = S0_XYZ.getZ() + m1 * S1_XYZ.getZ() + m2 * S2_XYZ.getZ();
		
		return new Color3F(x0, y0, z0);
	}
}