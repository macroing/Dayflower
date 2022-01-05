/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
package org.dayflower.noise;

import static org.dayflower.utility.Doubles.abs;

import org.dayflower.utility.Ints;

/**
 * A class that consists exclusively of static methods that performs Simplex noise-based operations using the data type {@code double}.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SimplexNoiseD {
	private static final double SIMPLEX_F2 = 0.3660254037844386D;
	private static final double SIMPLEX_F3 = 1.0D / 3.0D;
	private static final double SIMPLEX_F4 = 0.30901699437494745D;
	private static final double SIMPLEX_G2 = 0.21132486540518713D;
	private static final double SIMPLEX_G3 = 1.0D / 6.0D;
	private static final double SIMPLEX_G4 = 0.1381966011250105D;
	private static final double[] SIMPLEX_GRADIENT_3 = doCreateSimplexGradient3();
	private static final double[] SIMPLEX_GRADIENT_4 = doCreateSimplexGradient4();
	private static final int[] PERMUTATIONS_B = doCreatePermutationsB();
	private static final int[] PERMUTATIONS_B_MODULO_12 = doCreatePermutationsBModulo12();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private SimplexNoiseD() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based fractal algorithm using the coordinate X.
	 * 
	 * @param x the X-coordinate
	 * @param amplitude the amplitude to start at
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based fractal algorithm using the coordinate X
	 */
	public static double fractalX(final double x, final double amplitude, final double frequency, final double gain, final double lacunarity, final int octaves) {
		double result = 0.0D;
		
		double currentAmplitude = amplitude;
		double currentFrequency = frequency;
		
		for(int i = 0; i < octaves; i++) {
			result += currentAmplitude * noiseX(x * currentFrequency);
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return result;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based fractal algorithm using the coordinates X and Y.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param amplitude the amplitude to start at
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based fractal algorithm using the coordinates X and Y
	 */
	public static double fractalXY(final double x, final double y, final double amplitude, final double frequency, final double gain, final double lacunarity, final int octaves) {
		double result = 0.0D;
		
		double currentAmplitude = amplitude;
		double currentFrequency = frequency;
		
		for(int i = 0; i < octaves; i++) {
			result += currentAmplitude * noiseXY(x * currentFrequency, y * currentFrequency);
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return result;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based fractal algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param amplitude the amplitude to start at
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based fractal algorithm using the coordinates X, Y and Z
	 */
	public static double fractalXYZ(final double x, final double y, final double z, final double amplitude, final double frequency, final double gain, final double lacunarity, final int octaves) {
		double result = 0.0D;
		
		double currentAmplitude = amplitude;
		double currentFrequency = frequency;
		
		for(int i = 0; i < octaves; i++) {
			result += currentAmplitude * noiseXYZ(x * currentFrequency, y * currentFrequency, z * currentFrequency);
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return result;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based fractal algorithm using the coordinates X, Y, Z and W.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param w the W-coordinate
	 * @param amplitude the amplitude to start at
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based fractal algorithm using the coordinates X, Y, Z and W
	 */
	public static double fractalXYZW(final double x, final double y, final double z, final double w, final double amplitude, final double frequency, final double gain, final double lacunarity, final int octaves) {
		double result = 0.0D;
		
		double currentAmplitude = amplitude;
		double currentFrequency = frequency;
		
		for(int i = 0; i < octaves; i++) {
			result += currentAmplitude * noiseXYZW(x * currentFrequency, y * currentFrequency, z * currentFrequency, w * currentFrequency);
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return result;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinate X.
	 * 
	 * @param x the X-coordinate
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param minimum the minimum value to return
	 * @param maximum the maximum value to return
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinate X
	 */
	public static double fractionalBrownianMotionX(final double x, final double frequency, final double gain, final double minimum, final double maximum, final int octaves) {
		double currentAmplitude = 1.0D;
		double maximumAmplitude = 0.0D;
		
		double currentFrequency = frequency;
		
		double noise = 0.0D;
		
		for(int i = 0; i < octaves; i++) {
			noise += noiseX(x * currentFrequency) * currentAmplitude;
			
			maximumAmplitude += currentAmplitude;
			currentAmplitude *= gain;
			
			currentFrequency *= 2.0D;
		}
		
		noise /= maximumAmplitude;
		noise = noise * (maximum - minimum) / 2.0D + (maximum + minimum) / 2.0D;
		
		return noise;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinates X and Y.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param minimum the minimum value to return
	 * @param maximum the maximum value to return
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinates X and Y
	 */
	public static double fractionalBrownianMotionXY(final double x, final double y, final double frequency, final double gain, final double minimum, final double maximum, final int octaves) {
		double currentAmplitude = 1.0D;
		double maximumAmplitude = 0.0D;
		
		double currentFrequency = frequency;
		
		double noise = 0.0D;
		
		for(int i = 0; i < octaves; i++) {
			noise += noiseXY(x * currentFrequency, y * currentFrequency) * currentAmplitude;
			
			maximumAmplitude += currentAmplitude;
			currentAmplitude *= gain;
			
			currentFrequency *= 2.0D;
		}
		
		noise /= maximumAmplitude;
		noise = noise * (maximum - minimum) / 2.0D + (maximum + minimum) / 2.0D;
		
		return noise;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param minimum the minimum value to return
	 * @param maximum the maximum value to return
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinates X, Y and Z
	 */
	public static double fractionalBrownianMotionXYZ(final double x, final double y, final double z, final double frequency, final double gain, final double minimum, final double maximum, final int octaves) {
		double currentAmplitude = 1.0D;
		double maximumAmplitude = 0.0D;
		
		double currentFrequency = frequency;
		
		double noise = 0.0D;
		
		for(int i = 0; i < octaves; i++) {
			noise += noiseXYZ(x * currentFrequency, y * currentFrequency, z * currentFrequency) * currentAmplitude;
			
			maximumAmplitude += currentAmplitude;
			currentAmplitude *= gain;
			
			currentFrequency *= 2.0D;
		}
		
		noise /= maximumAmplitude;
		noise = noise * (maximum - minimum) / 2.0D + (maximum + minimum) / 2.0D;
		
		return noise;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinates X, Y, Z and W.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param w the W-coordinate
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param minimum the minimum value to return
	 * @param maximum the maximum value to return
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinates X, Y, Z and W
	 */
	public static double fractionalBrownianMotionXYZW(final double x, final double y, final double z, final double w, final double frequency, final double gain, final double minimum, final double maximum, final int octaves) {
		double currentAmplitude = 1.0D;
		double maximumAmplitude = 0.0D;
		
		double currentFrequency = frequency;
		
		double noise = 0.0D;
		
		for(int i = 0; i < octaves; i++) {
			noise += noiseXYZW(x * currentFrequency, y * currentFrequency, z * currentFrequency, w * currentFrequency) * currentAmplitude;
			
			maximumAmplitude += currentAmplitude;
			currentAmplitude *= gain;
			
			currentFrequency *= 2.0D;
		}
		
		noise /= maximumAmplitude;
		noise = noise * (maximum - minimum) / 2.0D + (maximum + minimum) / 2.0D;
		
		return noise;
	}
	
	/**
	 * Returns a {@code double} with noise computed by the Simplex algorithm using the coordinate X.
	 * 
	 * @param x the X-coordinate
	 * @return a {@code double} with noise computed by the Simplex algorithm using the coordinate X
	 */
	public static double noiseX(final double x) {
		final int i0 = doFastFloorToInt(x);
		final int i1 = i0 + 1;
		
		final double x0 = x - i0;
		final double x1 = x0 - 1.0D;
		
		final double t00 = 1.0D - x0 * x0;
		final double t01 = t00 * t00;
		
		final double t10 = 1.0D - x1 * x1;
		final double t11 = t10 * t10;
		
		final int hash00 = PERMUTATIONS_B[Ints.abs(i0) % PERMUTATIONS_B.length];
		final int hash01 = hash00 & 0x0F;
		final int hash10 = PERMUTATIONS_B[Ints.abs(i1) % PERMUTATIONS_B.length];
		final int hash11 = hash10 & 0x0F;
		
		final double gradient00 = 1.0D + (hash01 & 7);
		final double gradient01 = (hash01 & 8) != 0 ? -gradient00 : gradient00;
		final double gradient02 = gradient01 * x0;
		final double gradient10 = 1.0D + (hash11 & 7);
		final double gradient11 = (hash11 & 8) != 0 ? -gradient10 : gradient10;
		final double gradient12 = gradient11 * x1;
		
		final double n0 = t01 * t01 * gradient02;
		final double n1 = t11 * t11 * gradient12;
		
		return 0.395D * (n0 + n1);
	}
	
	/**
	 * Returns a {@code double} with noise computed by the Simplex algorithm using the coordinates X and Y.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @return a {@code double} with noise computed by the Simplex algorithm using the coordinates X and Y
	 */
	public static double noiseXY(final double x, final double y) {
		final double s = (x + y) * SIMPLEX_F2;
		
		final int i = doFastFloorToInt(x + s);
		final int j = doFastFloorToInt(y + s);
		
		final double t = (i + j) * SIMPLEX_G2;
		
		final double x0 = x - (i - t);
		final double y0 = y - (j - t);
		
		final int i1 = x0 > y0 ? 1 : 0;
		final int j1 = x0 > y0 ? 0 : 1;
		
		final double x1 = x0 - i1 + SIMPLEX_G2;
		final double y1 = y0 - j1 + SIMPLEX_G2;
		final double x2 = x0 - 1.0D + 2.0D * SIMPLEX_G2;
		final double y2 = y0 - 1.0D + 2.0D * SIMPLEX_G2;
		
		final int ii = i & 0xFF;
		final int jj = j & 0xFF;
		
		final int gi0 = PERMUTATIONS_B_MODULO_12[ii +  0 + PERMUTATIONS_B[jj +  0]];
		final int gi1 = PERMUTATIONS_B_MODULO_12[ii + i1 + PERMUTATIONS_B[jj + j1]];
		final int gi2 = PERMUTATIONS_B_MODULO_12[ii +  1 + PERMUTATIONS_B[jj +  1]];
		
		final double t0 = 0.5D - x0 * x0 - y0 * y0;
		final double n0 = t0 < 0.0D ? 0.0D : (t0 * t0) * (t0 * t0) * (SIMPLEX_GRADIENT_3[gi0 * 3 + 0] * x0 + SIMPLEX_GRADIENT_3[gi0 * 3 + 1] * y0);
		
		final double t1 = 0.5D - x1 * x1 - y1 * y1;
		final double n1 = t1 < 0.0D ? 0.0D : (t1 * t1) * (t1 * t1) * (SIMPLEX_GRADIENT_3[gi1 * 3 + 0] * x1 + SIMPLEX_GRADIENT_3[gi1 * 3 + 1] * y1);
		
		final double t2 = 0.5D - x2 * x2 - y2 * y2;
		final double n2 = t2 < 0.0D ? 0.0D : (t2 * t2) * (t2 * t2) * (SIMPLEX_GRADIENT_3[gi2 * 3 + 0] * x2 + SIMPLEX_GRADIENT_3[gi2 * 3 + 1] * y2);
		
		return 70.0D * (n0 + n1 + n2);
	}
	
	/**
	 * Returns a {@code double} with noise computed by the Simplex algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @return a {@code double} with noise computed by the Simplex algorithm using the coordinates X, Y and Z
	 */
	public static double noiseXYZ(final double x, final double y, final double z) {
		final double s = (x + y + z) * SIMPLEX_F3;
		
		final int i = doFastFloorToInt(x + s);
		final int j = doFastFloorToInt(y + s);
		final int k = doFastFloorToInt(z + s);
		
		final double t = (i + j + k) * SIMPLEX_G3;
		
		final double x0 = x - (i - t);
		final double y0 = y - (j - t);
		final double z0 = z - (k - t);
		
		int i1 = 0;
		int j1 = 0;
		int k1 = 0;
		int i2 = 0;
		int j2 = 0;
		int k2 = 0;
		
		if(x0 >= y0) {
			if(y0 >= z0) {
				i1 = 1;
				j1 = 0;
				k1 = 0;
				i2 = 1;
				j2 = 1;
				k2 = 0;
			} else if(x0 >= z0) {
				i1 = 1;
				j1 = 0;
				k1 = 0;
				i2 = 1;
				j2 = 0;
				k2 = 1;
			} else {
				i1 = 0;
				j1 = 0;
				k1 = 1;
				i2 = 1;
				j2 = 0;
				k2 = 1;
			}
		} else {
			if(y0 < z0) {
				i1 = 0;
				j1 = 0;
				k1 = 1;
				i2 = 0;
				j2 = 1;
				k2 = 1;
			} else if(x0 < z0) {
				i1 = 0;
				j1 = 1;
				k1 = 0;
				i2 = 0;
				j2 = 1;
				k2 = 1;
			} else {
				i1 = 0;
				j1 = 1;
				k1 = 0;
				i2 = 1;
				j2 = 1;
				k2 = 0;
			}
		}
		
		final double x1 = x0 - i1 + SIMPLEX_G3;
		final double y1 = y0 - j1 + SIMPLEX_G3;
		final double z1 = z0 - k1 + SIMPLEX_G3;
		final double x2 = x0 - i2 + 2.0D * SIMPLEX_G3;
		final double y2 = y0 - j2 + 2.0D * SIMPLEX_G3;
		final double z2 = z0 - k2 + 2.0D * SIMPLEX_G3;
		final double x3 = x0 - 1.0D + 3.0D * SIMPLEX_G3;
		final double y3 = y0 - 1.0D + 3.0D * SIMPLEX_G3;
		final double z3 = z0 - 1.0D + 3.0D * SIMPLEX_G3;
		
		final int ii = i & 0xFF;
		final int jj = j & 0xFF;
		final int kk = k & 0xFF;
		
		final int gi0 = PERMUTATIONS_B_MODULO_12[ii +  0 + PERMUTATIONS_B[jj +  0 + PERMUTATIONS_B[kk +  0]]];
		final int gi1 = PERMUTATIONS_B_MODULO_12[ii + i1 + PERMUTATIONS_B[jj + j1 + PERMUTATIONS_B[kk + k1]]];
		final int gi2 = PERMUTATIONS_B_MODULO_12[ii + i2 + PERMUTATIONS_B[jj + j2 + PERMUTATIONS_B[kk + k2]]];
		final int gi3 = PERMUTATIONS_B_MODULO_12[ii +  1 + PERMUTATIONS_B[jj +  1 + PERMUTATIONS_B[kk +  1]]];
		
		final double t0 = 0.6D - x0 * x0 - y0 * y0 - z0 * z0;
		final double n0 = t0 < 0.0D ? 0.0D : (t0 * t0) * (t0 * t0) * (SIMPLEX_GRADIENT_3[gi0 * 3 + 0] * x0 + SIMPLEX_GRADIENT_3[gi0 * 3 + 1] * y0 + SIMPLEX_GRADIENT_3[gi0 * 3 + 2] * z0);
		
		final double t1 = 0.6D - x1 * x1 - y1 * y1 - z1 * z1;
		final double n1 = t1 < 0.0D ? 0.0D : (t1 * t1) * (t1 * t1) * (SIMPLEX_GRADIENT_3[gi1 * 3 + 0] * x1 + SIMPLEX_GRADIENT_3[gi1 * 3 + 1] * y1 + SIMPLEX_GRADIENT_3[gi1 * 3 + 2] * z1);
		
		final double t2 = 0.6D - x2 * x2 - y2 * y2 - z2 * z2;
		final double n2 = t2 < 0.0D ? 0.0D : (t2 * t2) * (t2 * t2) * (SIMPLEX_GRADIENT_3[gi2 * 3 + 0] * x2 + SIMPLEX_GRADIENT_3[gi2 * 3 + 1] * y2 + SIMPLEX_GRADIENT_3[gi2 * 3 + 2] * z2);
		
		final double t3 = 0.6D - x3 * x3 - y3 * y3 - z3 * z3;
		final double n3 = t3 < 0.0D ? 0.0D : (t3 * t3) * (t3 * t3) * (SIMPLEX_GRADIENT_3[gi3 * 3 + 0] * x3 + SIMPLEX_GRADIENT_3[gi3 * 3 + 1] * y3 + SIMPLEX_GRADIENT_3[gi3 * 3 + 2] * z3);
		
		return 32.0D * (n0 + n1 + n2 + n3);
	}
	
	/**
	 * Returns a {@code double} with noise computed by the Simplex algorithm using the coordinates X, Y, Z and W.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param w the W-coordinate
	 * @return a {@code double} with noise computed by the Simplex algorithm using the coordinates X, Y, Z and W
	 */
	public static double noiseXYZW(final double x, final double y, final double z, final double w) {
		final double s = (x + y + z + w) * SIMPLEX_F4;
		
		final int i = doFastFloorToInt(x + s);
		final int j = doFastFloorToInt(y + s);
		final int k = doFastFloorToInt(z + s);
		final int l = doFastFloorToInt(w + s);
		
		final double t = (i + j + k + l) * SIMPLEX_G4;
		
		final double x0 = x - (i - t);
		final double y0 = y - (j - t);
		final double z0 = z - (k - t);
		final double w0 = w - (l - t);
		
		int rankX = 0;
		int rankY = 0;
		int rankZ = 0;
		int rankW = 0;
		
		if(x0 > y0) {
			rankX++;
		} else {
			rankY++;
		}
		
		if(x0 > z0) {
			rankX++;
		} else {
			rankZ++;
		}
		
		if(x0 > w0) {
			rankX++;
		} else {
			rankW++;
		}
		
		if(y0 > z0) {
			rankY++;
		} else {
			rankZ++;
		}
		
		if(y0 > w0) {
			rankY++;
		} else {
			rankW++;
		}
		
		if(z0 > w0) {
			rankZ++;
		} else {
			rankW++;
		}
		
		final int i1 = rankX >= 3 ? 1 : 0;
		final int j1 = rankY >= 3 ? 1 : 0;
		final int k1 = rankZ >= 3 ? 1 : 0;
		final int l1 = rankW >= 3 ? 1 : 0;
		final int i2 = rankX >= 2 ? 1 : 0;
		final int j2 = rankY >= 2 ? 1 : 0;
		final int k2 = rankZ >= 2 ? 1 : 0;
		final int l2 = rankW >= 2 ? 1 : 0;
		final int i3 = rankX >= 1 ? 1 : 0;
		final int j3 = rankY >= 1 ? 1 : 0;
		final int k3 = rankZ >= 1 ? 1 : 0;
		final int l3 = rankW >= 1 ? 1 : 0;
		
		final double x1 = x0 - i1 + SIMPLEX_G4;
		final double y1 = y0 - j1 + SIMPLEX_G4;
		final double z1 = z0 - k1 + SIMPLEX_G4;
		final double w1 = w0 - l1 + SIMPLEX_G4;
		final double x2 = x0 - i2 + 2.0D * SIMPLEX_G4;
		final double y2 = y0 - j2 + 2.0D * SIMPLEX_G4;
		final double z2 = z0 - k2 + 2.0D * SIMPLEX_G4;
		final double w2 = w0 - l2 + 2.0D * SIMPLEX_G4;
		final double x3 = x0 - i3 + 3.0D * SIMPLEX_G4;
		final double y3 = y0 - j3 + 3.0D * SIMPLEX_G4;
		final double z3 = z0 - k3 + 3.0D * SIMPLEX_G4;
		final double w3 = w0 - l3 + 3.0D * SIMPLEX_G4;
		final double x4 = x0 - 1.0D + 4.0D * SIMPLEX_G4;
		final double y4 = y0 - 1.0D + 4.0D * SIMPLEX_G4;
		final double z4 = z0 - 1.0D + 4.0D * SIMPLEX_G4;
		final double w4 = w0 - 1.0D + 4.0D * SIMPLEX_G4;
		
		final int ii = i & 0xFF;
		final int jj = j & 0xFF;
		final int kk = k & 0xFF;
		final int ll = l & 0xFF;
		
		final int gi0 = PERMUTATIONS_B[ii +  0 + PERMUTATIONS_B[jj +  0 + PERMUTATIONS_B[kk +  0 + PERMUTATIONS_B[ll +  0]]]] % 32;
		final int gi1 = PERMUTATIONS_B[ii + i1 + PERMUTATIONS_B[jj + j1 + PERMUTATIONS_B[kk + k1 + PERMUTATIONS_B[ll + l1]]]] % 32;
		final int gi2 = PERMUTATIONS_B[ii + i2 + PERMUTATIONS_B[jj + j2 + PERMUTATIONS_B[kk + k2 + PERMUTATIONS_B[ll + l2]]]] % 32;
		final int gi3 = PERMUTATIONS_B[ii + i3 + PERMUTATIONS_B[jj + j3 + PERMUTATIONS_B[kk + k3 + PERMUTATIONS_B[ll + l3]]]] % 32;
		final int gi4 = PERMUTATIONS_B[ii +  1 + PERMUTATIONS_B[jj +  1 + PERMUTATIONS_B[kk +  1 + PERMUTATIONS_B[ll +  1]]]] % 32;
		
		final double t0 = 0.6D - x0 * x0 - y0 * y0 - z0 * z0 - w0 * w0;
		final double n0 = t0 < 0.0D ? 0.0D : (t0 * t0) * (t0 * t0) * (SIMPLEX_GRADIENT_4[gi0 * 4 + 0] * x0 + SIMPLEX_GRADIENT_4[gi0 * 4 + 1] * y0 + SIMPLEX_GRADIENT_4[gi0 * 4 + 2] * z0 + SIMPLEX_GRADIENT_4[gi0 * 4 + 3] * w0);
		
		final double t1 = 0.6D - x1 * x1 - y1 * y1 - z1 * z1 - w1 * w1;
		final double n1 = t1 < 0.0D ? 0.0D : (t1 * t1) * (t1 * t1) * (SIMPLEX_GRADIENT_4[gi1 * 4 + 0] * x1 + SIMPLEX_GRADIENT_4[gi1 * 4 + 1] * y1 + SIMPLEX_GRADIENT_4[gi1 * 4 + 2] * z1 + SIMPLEX_GRADIENT_4[gi1 * 4 + 3] * w1);
		
		final double t2 = 0.6D - x2 * x2 - y2 * y2 - z2 * z2 - w2 * w2;
		final double n2 = t2 < 0.0D ? 0.0D : (t2 * t2) * (t2 * t2) * (SIMPLEX_GRADIENT_4[gi2 * 4 + 0] * x2 + SIMPLEX_GRADIENT_4[gi2 * 4 + 1] * y2 + SIMPLEX_GRADIENT_4[gi2 * 4 + 2] * z2 + SIMPLEX_GRADIENT_4[gi2 * 4 + 3] * w2);
		
		final double t3 = 0.6D - x3 * x3 - y3 * y3 - z3 * z3 - w3 * w3;
		final double n3 = t3 < 0.0D ? 0.0D : (t3 * t3) * (t3 * t3) * (SIMPLEX_GRADIENT_4[gi3 * 4 + 0] * x3 + SIMPLEX_GRADIENT_4[gi3 * 4 + 1] * y3 + SIMPLEX_GRADIENT_4[gi3 * 4 + 2] * z3 + SIMPLEX_GRADIENT_4[gi3 * 4 + 3] * w3);
		
		final double t4 = 0.6D - x4 * x4 - y4 * y4 - z4 * z4 - w4 * w4;
		final double n4 = t4 < 0.0D ? 0.0D : (t4 * t4) * (t4 * t4) * (SIMPLEX_GRADIENT_4[gi4 * 4 + 0] * x4 + SIMPLEX_GRADIENT_4[gi4 * 4 + 1] * y4 + SIMPLEX_GRADIENT_4[gi4 * 4 + 2] * z4 + SIMPLEX_GRADIENT_4[gi4 * 4 + 3] * w4);
		
		return 27.0D * (n0 + n1 + n2 + n3 + n4);
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based turbulence algorithm using the coordinate X.
	 * 
	 * @param x the X-coordinate
	 * @param amplitude the amplitude to start a
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based turbulence algorithm using the coordinate X
	 */
	public static double turbulenceX(final double x, final double amplitude, final double frequency, final double gain, final double lacunarity, final int octaves) {
		double currentAmplitude = amplitude;
		double currentFrequency = frequency;
		
		double noise = 0.0D;
		
		for(int i = 0; i < octaves; i++) {
			noise += currentAmplitude * abs(noiseX(x * currentFrequency));
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return noise;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based turbulence algorithm using the coordinates X and Y.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param amplitude the amplitude to start a
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based turbulence algorithm using the coordinates X and Y
	 */
	public static double turbulenceXY(final double x, final double y, final double amplitude, final double frequency, final double gain, final double lacunarity, final int octaves) {
		double currentAmplitude = amplitude;
		double currentFrequency = frequency;
		
		double noise = 0.0D;
		
		for(int i = 0; i < octaves; i++) {
			noise += currentAmplitude * abs(noiseXY(x * currentFrequency, y * currentFrequency));
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return noise;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based turbulence algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param amplitude the amplitude to start a
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based turbulence algorithm using the coordinates X, Y and Z
	 */
	public static double turbulenceXYZ(final double x, final double y, final double z, final double amplitude, final double frequency, final double gain, final double lacunarity, final int octaves) {
		double currentAmplitude = amplitude;
		double currentFrequency = frequency;
		
		double noise = 0.0D;
		
		for(int i = 0; i < octaves; i++) {
			noise += currentAmplitude * abs(noiseXYZ(x * currentFrequency, y * currentFrequency, z * currentFrequency));
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return noise;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based turbulence algorithm using the coordinates X, Y, Z and W.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param w the W-coordinate
	 * @param amplitude the amplitude to start a
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based turbulence algorithm using the coordinates X, Y, Z and W
	 */
	public static double turbulenceXYZW(final double x, final double y, final double z, final double w, final double amplitude, final double frequency, final double gain, final double lacunarity, final int octaves) {
		double currentAmplitude = amplitude;
		double currentFrequency = frequency;
		
		double noise = 0.0D;
		
		for(int i = 0; i < octaves; i++) {
			noise += currentAmplitude * abs(noiseXYZW(x * currentFrequency, y * currentFrequency, z * currentFrequency, w * currentFrequency));
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return noise;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static double[] doCreateSimplexGradient3() {
		return new double[] {
			+1.0D, +1.0D, +0.0D, -1.0D, +1.0D, +0.0D, +1.0D, -1.0D, +0.0D, -1.0D, -1.0D, +0.0D,
			+1.0D, +0.0D, +1.0D, -1.0D, +0.0D, +1.0D, +1.0D, +0.0D, -1.0D, -1.0D, +0.0D, -1.0D,
			+0.0D, +1.0D, +1.0D, +0.0D, -1.0D, +1.0D, +0.0D, +1.0D, -1.0D, +0.0D, -1.0D, -1.0D
		};
	}
	
	private static double[] doCreateSimplexGradient4() {
		return new double[] {
			+0.0D, +1.0D, +1.0D, +1.0D, +0.0D, +1.0D, +1.0D, -1.0D, +0.0D, +1.0D, -1.0D, +1.0D, +0.0D, +1.0D, -1.0D, -1.0D,
			+0.0D, -1.0D, +1.0D, +1.0D, +0.0D, -1.0D, +1.0D, -1.0D, +0.0D, -1.0D, -1.0D, +1.0D, +0.0D, -1.0D, -1.0D, -1.0D,
			+1.0D, +0.0D, +1.0D, +1.0D, +1.0D, +0.0D, +1.0D, -1.0D, +1.0D, +0.0D, -1.0D, +1.0D, +1.0D, +0.0D, -1.0D, -1.0D,
			-1.0D, +0.0D, +1.0D, +1.0D, -1.0D, +0.0D, +1.0D, -1.0D, -1.0D, +0.0D, -1.0D, +1.0D, -1.0D, +0.0D, -1.0D, -1.0D,
			+1.0D, +1.0D, +0.0D, +1.0D, +1.0D, +1.0D, +0.0D, -1.0D, +1.0D, -1.0D, +0.0D, +1.0D, +1.0D, -1.0D, +0.0D, -1.0D,
			-1.0D, +1.0D, +0.0D, +1.0D, -1.0D, +1.0D, +0.0D, -1.0D, -1.0D, -1.0D, +0.0D, +1.0D, -1.0D, -1.0D, +0.0D, -1.0D,
			+1.0D, +1.0D, +1.0D, +0.0D, +1.0D, +1.0D, -1.0D, +0.0D, +1.0D, -1.0D, +1.0D, +0.0D, +1.0D, -1.0D, -1.0D, +0.0D,
			-1.0D, +1.0D, +1.0D, +0.0D, -1.0D, +1.0D, -1.0D, +0.0D, -1.0D, -1.0D, +1.0D, +0.0D, -1.0D, -1.0D, -1.0D, +0.0D
		};
	}
	
	private static int doFastFloorToInt(final double value) {
		final int i = (int)(value);
		
		return value < i ? i - 1 : i;
	}
	
	private static int[] doCreatePermutationsA() {
		return new int[] {
			151, 160, 137,  91,  90,  15, 131,  13, 201,  95,  96,  53, 194, 233,   7, 225,
			140,  36, 103,  30,  69, 142,   8,  99,  37, 240,  21,  10,  23, 190,   6, 148,
			247, 120, 234,  75,   0,  26, 197,  62,  94, 252, 219, 203, 117,  35,  11,  32,
			 57, 177,  33,  88, 237, 149,  56,  87, 174,  20, 125, 136, 171, 168,  68, 175,
			 74, 165,  71, 134, 139,  48,  27, 166,  77, 146, 158, 231,  83, 111, 229, 122,
			 60, 211, 133, 230, 220, 105,  92,  41,  55,  46, 245,  40, 244, 102, 143,  54,
			 65,  25,  63, 161,   1, 216,  80,  73, 209,  76, 132, 187, 208,  89,  18, 169,
			200, 196, 135, 130, 116, 188, 159,  86, 164, 100, 109, 198, 173, 186,   3,  64,
			 52, 217, 226, 250, 124, 123,   5, 202,  38, 147, 118, 126, 255,  82,  85, 212,
			207, 206,  59, 227,  47,  16,  58,  17, 182, 189,  28,  42,  23, 183, 170, 213,
			119, 248, 152,   2,  44, 154, 163,  70, 221, 153, 101, 155, 167,  43, 172,   9,
			129,  22,  39, 253,  19,  98, 108, 110,  79, 113, 224, 232, 178, 185, 112, 104,
			218, 246,  97, 228, 251,  34, 242, 193, 238, 210, 144,  12, 191, 179, 162, 241,
			 81,  51, 145, 235, 249,  14, 239, 107,  49, 192, 214,  31, 181, 199, 106, 157,
			184,  84, 204, 176, 115, 121,  50,  45, 127,   4, 150, 254, 138, 236, 205,  93,
			222, 114,  67,  29,  24,  72, 243, 141, 128, 195,  78,  66, 215,  61, 156, 180
		};
	}
	
	private static int[] doCreatePermutationsB() {
		final int[] permutationsA = doCreatePermutationsA();
		final int[] permutationsB = new int[permutationsA.length * 2];
		
		for(int i = 0; i < permutationsB.length; i++) {
			permutationsB[i] = permutationsA[i % permutationsA.length];
		}
		
		return permutationsB;
	}
	
	private static int[] doCreatePermutationsBModulo12() {
		final int[] permutationsBModulo12 = doCreatePermutationsB();
		
		for(int i = 0; i < permutationsBModulo12.length; i++) {
			permutationsBModulo12[i] = permutationsBModulo12[i] % 12;
		}
		
		return permutationsBModulo12;
	}
}