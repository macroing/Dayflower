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

import java.lang.reflect.Field;//TODO: Add Unit Tests!

/**
 * A {@code ChromaticSpectralCurveD} is an implementation of {@link SpectralCurveD} that contains chromaticity pairs.
 * <p>
 * This class is immutable and therefore suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ChromaticSpectralCurveD extends SpectralCurveD {
	private static final Color3D S0_XYZ;
	private static final Color3D S1_XYZ;
	private static final Color3D S2_XYZ;
	private static final SpectralCurveD K_S0_SPECTRAL_CURVE;
	private static final SpectralCurveD K_S1_SPECTRAL_CURVE;
	private static final SpectralCurveD K_S2_SPECTRAL_CURVE;
	private static final double[] S0_AMPLITUDES;
	private static final double[] S1_AMPLITUDES;
	private static final double[] S2_AMPLITUDES;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final double m1;
	private final double m2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	static {
		S0_AMPLITUDES = new double[] {0.04D, 6.0D, 29.6D, 55.3D, 57.3D, 61.8D, 61.5D, 68.8D, 63.4D, 65.8D, 94.8D, 104.8D, 105.9D, 96.8D, 113.9D, 125.6D, 125.5D, 121.3D, 121.3D, 113.5D, 113.1D, 110.8D, 106.5D, 108.8D, 105.3D, 104.4D, 100.0D, 96.0D, 95.1D, 89.1D, 90.5D, 90.3D, 88.4D, 84.0D, 85.1D, 81.9D, 82.6D, 84.9D, 81.3D, 71.9D, 74.3D, 76.4D, 63.3D, 71.7D, 77.0D, 65.2D, 47.7D, 68.6D, 65.0D, 66.0D, 61.0D, 53.3D, 58.9D, 61.9D};
		S1_AMPLITUDES = new double[] {0.02D, 4.5D, 22.4D, 42.0D, 40.6D, 41.6D, 38.0D, 42.4D, 38.5D, 35.0D, 43.4D, 46.3D, 43.9D, 37.1D, 36.7D, 35.9D, 32.6D, 27.9D, 24.3D, 20.1D, 16.2D, 13.2D, 8.6D, 6.1D, 4.2D, 1.9D, 0.0D, -1.6D, -3.5D, -3.5D, -5.8D, -7.2D, -8.6D, -9.5D, -10.9D, -10.7D, -12.0D, -14.0D, -13.6D, -12.0D, -13.3D, -12.9D, -10.6D, -11.6D, -12.2D, -10.2D, -7.8D, -11.2D, -10.4D, -10.6D, -9.7D, -8.3D, -9.3D, -9.8D};
		S2_AMPLITUDES = new double[] {0.00D, 2.0D, 4.0D, 8.5D, 7.8D, 6.7D, 5.3D, 6.1D, 3.0D, 1.2D, -1.1D, -0.5D, -0.7D, -1.2D, -2.6D, -2.9D, -2.8D, -2.6D, -2.6D, -1.8D, -1.5D, -1.3D, -1.2D, -1.0D, -0.5D, -0.3D, 0.0D, 0.2D, 0.5D, 2.1D, 3.2D, 4.1D, 4.7D, 5.1D, 6.7D, 7.3D, 8.6D, 9.8D, 10.2D, 8.3D, 9.6D, 8.5D, 7.0D, 7.6D, 8.0D, 6.7D, 5.2D, 7.4D, 6.8D, 7.0D, 6.4D, 5.5D, 6.1D, 6.5D};
		
		K_S0_SPECTRAL_CURVE = new RegularSpectralCurveD(300.0D, 830.0D, S0_AMPLITUDES);
		K_S1_SPECTRAL_CURVE = new RegularSpectralCurveD(300.0D, 830.0D, S1_AMPLITUDES);
		K_S2_SPECTRAL_CURVE = new RegularSpectralCurveD(300.0D, 830.0D, S2_AMPLITUDES);
		
		S0_XYZ = K_S0_SPECTRAL_CURVE.toColorXYZ();
		S1_XYZ = K_S1_SPECTRAL_CURVE.toColorXYZ();
		S2_XYZ = K_S2_SPECTRAL_CURVE.toColorXYZ();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ChromaticSpectralCurveD} instance given the chromaticity pair of {@code x} and {@code y}.
	 * 
	 * @param x the X of the chromaticity pair
	 * @param y the Y of the chromaticity pair
	 */
//	TODO: Add Unit Tests!
	public ChromaticSpectralCurveD(final double x, final double y) {
		this.m1 = (-1.3515D -  1.7703D * x +  5.9114D * y) / (0.0241D + 0.2562D * x - 0.7341D * y);
		this.m2 = (+0.03D   - 31.4424D * x + 30.0717D * y) / (0.0241D + 0.2562D * x - 0.7341D * y);
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
		return K_S0_SPECTRAL_CURVE.sample(lambda) + this.m1 * K_S1_SPECTRAL_CURVE.sample(lambda) + this.m2 * K_S2_SPECTRAL_CURVE.sample(lambda);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3D} given {@code x} and {@code y} in XYZ-color space.
	 * 
	 * @param x the X of the chromaticity pair
	 * @param y the Y of the chromaticity pair
	 * @return a {@code Color3D} given {@code x} and {@code y} in XYZ-color space
	 */
//	TODO: Add Unit Tests!
	public static Color3D getColorXYZ(final double x, final double y) {
		final double m1 = (-1.3515D - 1.7703D  * x +  5.9114D * y) / (0.0241D + 0.2562D * x - 0.7341D * y);
		final double m2 = (+0.03D   - 31.4424D * x + 30.0717D * y) / (0.0241D + 0.2562D * x - 0.7341D * y);
		
		final double x0 = S0_XYZ.getX() + m1 * S1_XYZ.getX() + m2 * S2_XYZ.getX();
		final double y0 = S0_XYZ.getY() + m1 * S1_XYZ.getY() + m2 * S2_XYZ.getY();
		final double z0 = S0_XYZ.getZ() + m1 * S1_XYZ.getZ() + m2 * S2_XYZ.getZ();
		
		return new Color3D(x0, y0, z0);
	}
}