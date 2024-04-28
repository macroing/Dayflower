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
public final class SimplexNoiseDUnitTests {
	public SimplexNoiseDUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testFractalX() {
		for(int i = 0; i < 100000; i++) {
			final double x = Randoms.nextDouble(-100000.0D, 100000.0D);
			
			final double amplitude = 1.0D;
			final double frequency = 1.0D;
			final double gain = 1.0D;
			final double lacunarity = 1.0D;
			
			final int octaves = 16;
			
			final double noise = SimplexNoiseD.fractalX(x, amplitude, frequency, gain, lacunarity, octaves);
			
			assertTrue(noise >= -16.0D && noise <= 16.0D);
		}
	}
	
	@Test
	public void testFractalXY() {
		for(int i = 0; i < 100000; i++) {
			final double x = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double y = Randoms.nextDouble(-100000.0D, 100000.0D);
			
			final double amplitude = 1.0D;
			final double frequency = 1.0D;
			final double gain = 1.0D;
			final double lacunarity = 1.0D;
			
			final int octaves = 16;
			
			final double noise = SimplexNoiseD.fractalXY(x, y, amplitude, frequency, gain, lacunarity, octaves);
			
			assertTrue(noise >= -16.0D && noise <= 16.0D);
		}
	}
	
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
			
			final double noise = SimplexNoiseD.fractalXYZ(x, y, z, amplitude, frequency, gain, lacunarity, octaves);
			
			assertTrue(noise >= -16.0D && noise <= 16.0D);
		}
	}
	
	@Test
	public void testFractalXYZW() {
		for(int i = 0; i < 100000; i++) {
			final double x = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double y = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double z = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double w = Randoms.nextDouble(-100000.0D, 100000.0D);
			
			final double amplitude = 1.0D;
			final double frequency = 1.0D;
			final double gain = 1.0D;
			final double lacunarity = 1.0D;
			
			final int octaves = 16;
			
			final double noise = SimplexNoiseD.fractalXYZW(x, y, z, w, amplitude, frequency, gain, lacunarity, octaves);
			
			assertTrue(noise >= -16.0D && noise <= 16.0D);
		}
	}
	
	@Test
	public void testFractionalBrownianMotionX() {
		for(int i = 0; i < 100000; i++) {
			final double x = Randoms.nextDouble(-1000.0D, 1000.0D);
			
			final double frequency = 1.0D;
			final double gain = 1.0D;
			final double minimum = 0.0D;
			final double maximum = 1.0D;
			
			final int octaves = 16;
			
			final double noise = SimplexNoiseD.fractionalBrownianMotionX(x, frequency, gain, minimum, maximum, octaves);
			
			assertTrue(noise >= minimum && noise <= maximum);
		}
	}
	
	@Test
	public void testFractionalBrownianMotionXY() {
		for(int i = 0; i < 100000; i++) {
			final double x = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double y = Randoms.nextDouble(-100000.0D, 100000.0D);
			
			final double frequency = 1.0D;
			final double gain = 1.0D;
			final double minimum = 0.0D;
			final double maximum = 1.0D;
			
			final int octaves = 16;
			
			final double noise = SimplexNoiseD.fractionalBrownianMotionXY(x, y, frequency, gain, minimum, maximum, octaves);
			
			assertTrue(noise >= minimum && noise <= maximum);
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
			
			final double noise = SimplexNoiseD.fractionalBrownianMotionXYZ(x, y, z, frequency, gain, minimum, maximum, octaves);
			
			assertTrue(noise >= minimum && noise <= maximum);
		}
	}
	
	@Test
	public void testFractionalBrownianMotionXYZW() {
		for(int i = 0; i < 100000; i++) {
			final double x = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double y = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double z = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double w = Randoms.nextDouble(-100000.0D, 100000.0D);
			
			final double frequency = 1.0D;
			final double gain = 1.0D;
			final double minimum = 0.0D;
			final double maximum = 1.0D;
			
			final int octaves = 16;
			
			final double noise = SimplexNoiseD.fractionalBrownianMotionXYZW(x, y, z, w, frequency, gain, minimum, maximum, octaves);
			
			assertTrue(noise >= minimum && noise <= maximum);
		}
	}
	
	@Test
	public void testNoiseX() {
		for(int i = 0; i < 100000; i++) {
			final double x = Randoms.nextDouble(-100000.0D, 100000.0D);
			
			final double noise = SimplexNoiseD.noiseX(x);
			
			assertTrue(noise >= -1.0D && noise <= 1.0D);
		}
		
		assertEquals(0.0D, SimplexNoiseD.noiseX(1.0D));
		
		assertEquals(0.0D, SimplexNoiseD.noiseX(Double.NaN));
	}
	
	@Test
	public void testNoiseXY() {
		for(int i = 0; i < 100000; i++) {
			final double x = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double y = Randoms.nextDouble(-100000.0D, 100000.0D);
			
			final double noise = SimplexNoiseD.noiseXY(x, y);
			
			assertTrue(noise >= -1.0D && noise <= 1.0D);
		}
		
		assertEquals(0.0D, SimplexNoiseD.noiseXY(Double.NaN, 0.0D));
		assertEquals(0.0D, SimplexNoiseD.noiseXY(0.0D, Double.NaN));
	}
	
	@Test
	public void testNoiseXYZ() {
		for(int i = 0; i < 100000; i++) {
			final double x = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double y = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double z = Randoms.nextDouble(-100000.0D, 100000.0D);
			
			final double noise = SimplexNoiseD.noiseXYZ(x, y, z);
			
			assertTrue(noise >= -1.0D && noise <= 1.0D);
		}
		
		assertEquals(0.0D, SimplexNoiseD.noiseXYZ(1.0D, 2.0D, 3.0D));
		
		assertEquals(0.0D, SimplexNoiseD.noiseXYZ(Double.NaN, 0.0D, 0.0D));
		assertEquals(0.0D, SimplexNoiseD.noiseXYZ(0.0D, Double.NaN, 0.0D));
		assertEquals(0.0D, SimplexNoiseD.noiseXYZ(0.0D, 0.0D, Double.NaN));
	}
	
	@Test
	public void testNoiseXYZW() {
		for(int i = 0; i < 100000; i++) {
			final double x = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double y = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double z = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double w = Randoms.nextDouble(-100000.0D, 100000.0D);
			
			final double noise = SimplexNoiseD.noiseXYZW(x, y, z, w);
			
			assertTrue(noise >= -1.0D && noise <= 1.0D);
		}
		
		assertEquals(0.0D, SimplexNoiseD.noiseXYZW(Double.NaN, 0.0D, 0.0D, 0.0D));
		assertEquals(0.0D, SimplexNoiseD.noiseXYZW(0.0D, Double.NaN, 0.0D, 0.0D));
		assertEquals(0.0D, SimplexNoiseD.noiseXYZW(0.0D, 0.0D, Double.NaN, 0.0D));
		assertEquals(0.0D, SimplexNoiseD.noiseXYZW(0.0D, 0.0D, 0.0D, Double.NaN));
	}
	
	@Test
	public void testTurbulenceX() {
		for(int i = 0; i < 100000; i++) {
			final double x = Randoms.nextDouble(-100000.0D, 100000.0D);
			
			final double amplitude = 1.0D;
			final double frequency = 1.0D;
			final double gain = 1.0D;
			final double lacunarity = 1.0D;
			
			final int octaves = 16;
			
			final double noise = SimplexNoiseD.turbulenceX(x, amplitude, frequency, gain, lacunarity, octaves);
			
			assertTrue(noise >= 0.0D && noise <= 16.0D);
		}
	}
	
	@Test
	public void testTurbulenceXY() {
		for(int i = 0; i < 100000; i++) {
			final double x = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double y = Randoms.nextDouble(-100000.0D, 100000.0D);
			
			final double amplitude = 1.0D;
			final double frequency = 1.0D;
			final double gain = 1.0D;
			final double lacunarity = 1.0D;
			
			final int octaves = 16;
			
			final double noise = SimplexNoiseD.turbulenceXY(x, y, amplitude, frequency, gain, lacunarity, octaves);
			
			assertTrue(noise >= 0.0D && noise <= 16.0D);
		}
	}
	
	@Test
	public void testTurbulenceXYZ() {
		for(int i = 0; i < 100000; i++) {
			final double x = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double y = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double z = Randoms.nextDouble(-100000.0D, 100000.0D);
			
			final double amplitude = 1.0D;
			final double frequency = 1.0D;
			final double gain = 1.0D;
			final double lacunarity = 1.0D;
			
			final int octaves = 16;
			
			final double noise = SimplexNoiseD.turbulenceXYZ(x, y, z, amplitude, frequency, gain, lacunarity, octaves);
			
			assertTrue(noise >= 0.0D && noise <= 16.0D);
		}
	}
	
	@Test
	public void testTurbulenceXYZW() {
		for(int i = 0; i < 100000; i++) {
			final double x = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double y = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double z = Randoms.nextDouble(-100000.0D, 100000.0D);
			final double w = Randoms.nextDouble(-100000.0D, 100000.0D);
			
			final double amplitude = 1.0D;
			final double frequency = 1.0D;
			final double gain = 1.0D;
			final double lacunarity = 1.0D;
			
			final int octaves = 16;
			
			final double noise = SimplexNoiseD.turbulenceXYZW(x, y, z, w, amplitude, frequency, gain, lacunarity, octaves);
			
			assertTrue(noise >= 0.0D && noise <= 16.0D);
		}
	}
}