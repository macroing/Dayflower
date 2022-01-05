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
import static org.dayflower.utility.Doubles.floor;

/**
 * A class that consists exclusively of static methods that performs Perlin noise-based operations using the data type {@code double}.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PerlinNoiseD {
	private static final int[] PERMUTATIONS_B = doCreatePermutationsB();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private PerlinNoiseD() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code double} with noise computed by a Perlin-based fractal algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param amplitude the amplitude to start at
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Perlin-based fractal algorithm using the coordinates X, Y and Z
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
	 * Returns a {@code double} with noise computed by a Perlin-based fractional Brownian motion (fBm) algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param minimum the minimum value to return
	 * @param maximum the maximum value to return
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Perlin-based fractional Brownian motion (fBm) algorithm using the coordinates X, Y and Z
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
	 * Returns a {@code double} with noise computed by the Perlin algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @return a {@code double} with noise computed by the Perlin algorithm using the coordinates X, Y and Z
	 */
	public static double noiseXYZ(final double x, final double y, final double z) {
		final double floorX = floor(x);
		final double floorY = floor(y);
		final double floorZ = floor(z);
		
		final int x0 = (int)(floorX) & 0xFF;
		final int y0 = (int)(floorY) & 0xFF;
		final int z0 = (int)(floorZ) & 0xFF;
		
		final double x1 = x - floorX;
		final double y1 = y - floorY;
		final double z1 = z - floorZ;
		
		final double u = x1 * x1 * x1 * (x1 * (x1 * 6.0D - 15.0D) + 10.0D);
		final double v = y1 * y1 * y1 * (y1 * (y1 * 6.0D - 15.0D) + 10.0D);
		final double w = z1 * z1 * z1 * (z1 * (z1 * 6.0D - 15.0D) + 10.0D);
		
		final int a0 = PERMUTATIONS_B[x0] + y0;
		final int a1 = PERMUTATIONS_B[a0] + z0;
		final int a2 = PERMUTATIONS_B[a0 + 1] + z0;
		final int b0 = PERMUTATIONS_B[x0 + 1] + y0;
		final int b1 = PERMUTATIONS_B[b0] + z0;
		final int b2 = PERMUTATIONS_B[b0 + 1] + z0;
		final int hash0 = PERMUTATIONS_B[a1] & 15;
		final int hash1 = PERMUTATIONS_B[b1] & 15;
		final int hash2 = PERMUTATIONS_B[a2] & 15;
		final int hash3 = PERMUTATIONS_B[b2] & 15;
		final int hash4 = PERMUTATIONS_B[a1 + 1] & 15;
		final int hash5 = PERMUTATIONS_B[b1 + 1] & 15;
		final int hash6 = PERMUTATIONS_B[a2 + 1] & 15;
		final int hash7 = PERMUTATIONS_B[b2 + 1] & 15;
		
		final double gradient0U = hash0 < 8 || hash0 == 12 || hash0 == 13 ? x1 : y1;
		final double gradient0V = hash0 < 4 || hash0 == 12 || hash0 == 13 ? y1 : z1;
		final double gradient0 = ((hash0 & 1) == 0 ? gradient0U : -gradient0U) + ((hash0 & 2) == 0 ? gradient0V : -gradient0V);
		final double gradient1U = hash1 < 8 || hash1 == 12 || hash1 == 13 ? x1 - 1.0D : y1;
		final double gradient1V = hash1 < 4 || hash1 == 12 || hash1 == 13 ? y1 : z1;
		final double gradient1 = ((hash1 & 1) == 0 ? gradient1U : -gradient1U) + ((hash1 & 2) == 0 ? gradient1V : -gradient1V);
		final double gradient2U = hash2 < 8 || hash2 == 12 || hash2 == 13 ? x1 : y1 - 1.0D;
		final double gradient2V = hash2 < 4 || hash2 == 12 || hash2 == 13 ? y1 - 1.0D : z1;
		final double gradient2 = ((hash2 & 1) == 0 ? gradient2U : -gradient2U) + ((hash2 & 2) == 0 ? gradient2V : -gradient2V);
		final double gradient3U = hash3 < 8 || hash3 == 12 || hash3 == 13 ? x1 - 1.0D : y1 - 1.0D;
		final double gradient3V = hash3 < 4 || hash3 == 12 || hash3 == 13 ? y1 - 1.0D : z1;
		final double gradient3 = ((hash3 & 1) == 0 ? gradient3U : -gradient3U) + ((hash3 & 2) == 0 ? gradient3V : -gradient3V);
		final double gradient4U = hash4 < 8 || hash4 == 12 || hash4 == 13 ? x1 : y1;
		final double gradient4V = hash4 < 4 || hash4 == 12 || hash4 == 13 ? y1 : z1 - 1.0D;
		final double gradient4 = ((hash4 & 1) == 0 ? gradient4U : -gradient4U) + ((hash4 & 2) == 0 ? gradient4V : -gradient4V);
		final double gradient5U = hash5 < 8 || hash5 == 12 || hash5 == 13 ? x1 - 1.0D : y1;
		final double gradient5V = hash5 < 4 || hash5 == 12 || hash5 == 13 ? y1 : z1 - 1.0D;
		final double gradient5 = ((hash5 & 1) == 0 ? gradient5U : -gradient5U) + ((hash5 & 2) == 0 ? gradient5V : -gradient5V);
		final double gradient6U = hash6 < 8 || hash6 == 12 || hash6 == 13 ? x1 : y1 - 1.0D;
		final double gradient6V = hash6 < 4 || hash6 == 12 || hash6 == 13 ? y1 - 1.0D : z1 - 1.0D;
		final double gradient6 = ((hash6 & 1) == 0 ? gradient6U : -gradient6U) + ((hash6 & 2) == 0 ? gradient6V : -gradient6V);
		final double gradient7U = hash7 < 8 || hash7 == 12 || hash7 == 13 ? x1 - 1.0D : y1 - 1.0D;
		final double gradient7V = hash7 < 4 || hash7 == 12 || hash7 == 13 ? y1 - 1.0D : z1 - 1.0D;
		final double gradient7 = ((hash7 & 1) == 0 ? gradient7U : -gradient7U) + ((hash7 & 2) == 0 ? gradient7V : -gradient7V);
		
		final double lerp0 = gradient0 + u * (gradient1 - gradient0);
		final double lerp1 = gradient2 + u * (gradient3 - gradient2);
		final double lerp2 = gradient4 + u * (gradient5 - gradient4);
		final double lerp3 = gradient6 + u * (gradient7 - gradient6);
		final double lerp4 = lerp0 + v * (lerp1 - lerp0);
		final double lerp5 = lerp2 + v * (lerp3 - lerp2);
		final double lerp6 = lerp4 + w * (lerp5 - lerp4);
		
		return lerp6;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Perlin-based turbulence algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param amplitude the amplitude to start a
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Perlin-based turbulence algorithm using the coordinates X, Y and Z
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
	 * Returns a {@code double} with noise computed by a Perlin-based turbulence algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Perlin-based turbulence algorithm using the coordinates X, Y and Z
	 */
	public static double turbulenceXYZ(final double x, final double y, final double z, final int octaves) {
		double currentX = x;
		double currentY = y;
		double currentZ = z;
		
		double noise = abs(noiseXYZ(x, y, z));
		
		double weight = 1.0D;
		
		for(int i = 1; i < octaves; i++) {
			weight *= 2.0D;
			
			currentX = x * weight;
			currentY = y * weight;
			currentZ = z * weight;
			
			noise += abs(noiseXYZ(currentX, currentY, currentZ)) / weight;
		}
		
		return noise;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
}