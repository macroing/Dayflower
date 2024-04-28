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
package org.dayflower.noise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import org.macroing.java.util.Randoms;

@SuppressWarnings("static-method")
public final class PerlinNoiseDUnitTests {
	public PerlinNoiseDUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testFractalXYZ() {
		for(int i = 0; i < 100000; i++) {
			final double x = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double y = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double z = Randoms.nextDouble(-100000.0D, 100000.0D);
			
			final double amplitude = 1.0D;
			final double frequency = 1.0D;
			final double gain = 1.0D;
			final double lacunarity = 1.0D;
			
			final int octaves = 16;
			
			final double noise = PerlinNoiseD.fractalXYZ(x, y, z, amplitude, frequency, gain, lacunarity, octaves);
			
			assertTrue(noise >= -16.0D && noise <= 16.0D);
		}
	}
	
	@Test
	public void testFractionalBrownianMotionXYZ() {
		for(int i = 0; i < 100000; i++) {
			final double x = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double y = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double z = Randoms.nextDouble(-100000.0D, 100000.0D);
			
			final double frequency = 1.0D;
			final double gain = 1.0D;
			final double minimum = 0.0D;
			final double maximum = 1.0D;
			
			final int octaves = 16;
			
			final double noise = PerlinNoiseD.fractionalBrownianMotionXYZ(x, y, z, frequency, gain, minimum, maximum, octaves);
			
			assertTrue(noise >= minimum && noise <= maximum);
		}
	}
	
	@Test
	public void testNoiseXYZ() {
		for(int i = 0; i < 100000; i++) {
			final double x = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double y = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double z = Randoms.nextDouble(-100000.0D, 100000.0D);
			
			final double noise = PerlinNoiseD.noiseXYZ(x, y, z);
			
			assertTrue(noise >= -1.0D && noise <= 1.0D);
		}
		
		assertEquals(0.0D, PerlinNoiseD.noiseXYZ(1.0D, 2.0D, 3.0D));
		
		assertEquals(0.0D, PerlinNoiseD.noiseXYZ(Double.NaN, 0.0D, 0.0D));
		assertEquals(0.0D, PerlinNoiseD.noiseXYZ(0.0D, Double.NaN, 0.0D));
		assertEquals(0.0D, PerlinNoiseD.noiseXYZ(0.0D, 0.0D, Double.NaN));
	}
	
	@Test
	public void testTurbulenceXYZDoubleDoubleDoubleDoubleDoubleDoubleDoubleInt() {
		for(int i = 0; i < 100000; i++) {
			final double x = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double y = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double z = Randoms.nextDouble(-100000.0D, 100000.0D);
			
			final double amplitude = 1.0D;
			final double frequency = 1.0D;
			final double gain = 1.0D;
			final double lacunarity = 1.0D;
			
			final int octaves = 16;
			
			final double noise = PerlinNoiseD.turbulenceXYZ(x, y, z, amplitude, frequency, gain, lacunarity, octaves);
			
			assertTrue(noise >= 0.0D && noise <= 16.0D);
		}
	}
	
	@Test
	public void testTurbulenceXYZDoubleDoubleDoubleInt() {
		for(int i = 0; i < 100000; i++) {
			final double x = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double y = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double z = Randoms.nextDouble(-100000.0D, 100000.0D);
			
			final int octaves = 16;
			
			final double noise = PerlinNoiseD.turbulenceXYZ(x, y, z, octaves);
			
			assertTrue(noise >= 0.0D && noise <= 2.0D);
		}
	}
}