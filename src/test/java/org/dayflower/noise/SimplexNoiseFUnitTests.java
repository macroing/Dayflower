/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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
public final class SimplexNoiseFUnitTests {
	public SimplexNoiseFUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testFractalX() {
		for(int i = 0; i < 100000; i++) {
			final float x = Randoms.nextFloat(-100000.0F, 100000.0F);
			
			final float amplitude = 1.0F;
			final float frequency = 1.0F;
			final float gain = 1.0F;
			final float lacunarity = 1.0F;
			
			final int octaves = 16;
			
			final float noise = SimplexNoiseF.fractalX(x, amplitude, frequency, gain, lacunarity, octaves);
			
			assertTrue(noise >= -16.0F && noise <= 16.0F);
		}
	}
	
	@Test
	public void testFractalXY() {
		for(int i = 0; i < 100000; i++) {
			final float x = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float y = Randoms.nextFloat(-100000.0F, 100000.0F);
			
			final float amplitude = 1.0F;
			final float frequency = 1.0F;
			final float gain = 1.0F;
			final float lacunarity = 1.0F;
			
			final int octaves = 16;
			
			final float noise = SimplexNoiseF.fractalXY(x, y, amplitude, frequency, gain, lacunarity, octaves);
			
			assertTrue(noise >= -16.0F && noise <= 16.0F);
		}
	}
	
	@Test
	public void testFractalXYZ() {
		for(int i = 0; i < 100000; i++) {
			final float x = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float y = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float z = Randoms.nextFloat(-100000.0F, 100000.0F);
			
			final float amplitude = 1.0F;
			final float frequency = 1.0F;
			final float gain = 1.0F;
			final float lacunarity = 1.0F;
			
			final int octaves = 16;
			
			final float noise = SimplexNoiseF.fractalXYZ(x, y, z, amplitude, frequency, gain, lacunarity, octaves);
			
			assertTrue(noise >= -16.0F && noise <= 16.0F);
		}
	}
	
	@Test
	public void testFractalXYZW() {
		for(int i = 0; i < 100000; i++) {
			final float x = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float y = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float z = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float w = Randoms.nextFloat(-100000.0F, 100000.0F);
			
			final float amplitude = 1.0F;
			final float frequency = 1.0F;
			final float gain = 1.0F;
			final float lacunarity = 1.0F;
			
			final int octaves = 16;
			
			final float noise = SimplexNoiseF.fractalXYZW(x, y, z, w, amplitude, frequency, gain, lacunarity, octaves);
			
			assertTrue(noise >= -16.0F && noise <= 16.0F);
		}
	}
	
	@Test
	public void testFractionalBrownianMotionX() {
		for(int i = 0; i < 100000; i++) {
			final float x = Randoms.nextFloat(-1000.0F, 1000.0F);
			
			final float frequency = 1.0F;
			final float gain = 1.0F;
			final float minimum = 0.0F;
			final float maximum = 1.0F;
			
			final int octaves = 16;
			
			final float noise = SimplexNoiseF.fractionalBrownianMotionX(x, frequency, gain, minimum, maximum, octaves);
			
			assertTrue(noise >= minimum && noise <= maximum);
		}
	}
	
	@Test
	public void testFractionalBrownianMotionXY() {
		for(int i = 0; i < 100000; i++) {
			final float x = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float y = Randoms.nextFloat(-100000.0F, 100000.0F);
			
			final float frequency = 1.0F;
			final float gain = 1.0F;
			final float minimum = 0.0F;
			final float maximum = 1.0F;
			
			final int octaves = 16;
			
			final float noise = SimplexNoiseF.fractionalBrownianMotionXY(x, y, frequency, gain, minimum, maximum, octaves);
			
			assertTrue(noise >= minimum && noise <= maximum);
		}
	}
	
	@Test
	public void testFractionalBrownianMotionXYZ() {
		for(int i = 0; i < 100000; i++) {
			final float x = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float y = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float z = Randoms.nextFloat(-100000.0F, 100000.0F);
			
			final float frequency = 1.0F;
			final float gain = 1.0F;
			final float minimum = 0.0F;
			final float maximum = 1.0F;
			
			final int octaves = 16;
			
			final float noise = SimplexNoiseF.fractionalBrownianMotionXYZ(x, y, z, frequency, gain, minimum, maximum, octaves);
			
			assertTrue(noise >= minimum && noise <= maximum);
		}
	}
	
	@Test
	public void testFractionalBrownianMotionXYZW() {
		for(int i = 0; i < 100000; i++) {
			final float x = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float y = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float z = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float w = Randoms.nextFloat(-100000.0F, 100000.0F);
			
			final float frequency = 1.0F;
			final float gain = 1.0F;
			final float minimum = 0.0F;
			final float maximum = 1.0F;
			
			final int octaves = 16;
			
			final float noise = SimplexNoiseF.fractionalBrownianMotionXYZW(x, y, z, w, frequency, gain, minimum, maximum, octaves);
			
			assertTrue(noise >= minimum && noise <= maximum);
		}
	}
	
	@Test
	public void testNoiseX() {
		for(int i = 0; i < 100000; i++) {
			final float x = Randoms.nextFloat(-100000.0F, 100000.0F);
			
			final float noise = SimplexNoiseF.noiseX(x);
			
			assertTrue(noise >= -1.0F && noise <= 1.0F);
		}
		
		assertEquals(0.0F, SimplexNoiseF.noiseX(1.0F));
		
		assertEquals(0.0F, SimplexNoiseF.noiseX(Float.NaN));
	}
	
	@Test
	public void testNoiseXY() {
		for(int i = 0; i < 100000; i++) {
			final float x = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float y = Randoms.nextFloat(-100000.0F, 100000.0F);
			
			final float noise = SimplexNoiseF.noiseXY(x, y);
			
			assertTrue(noise >= -1.0F && noise <= 1.0F);
		}
		
		assertEquals(0.0F, SimplexNoiseF.noiseXY(Float.NaN, 0.0F));
		assertEquals(0.0F, SimplexNoiseF.noiseXY(0.0F, Float.NaN));
	}
	
	@Test
	public void testNoiseXYZ() {
		for(int i = 0; i < 100000; i++) {
			final float x = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float y = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float z = Randoms.nextFloat(-100000.0F, 100000.0F);
			
			final float noise = SimplexNoiseF.noiseXYZ(x, y, z);
			
			assertTrue(noise >= -1.0F && noise <= 1.0F);
		}
		
		assertEquals(0.0F, SimplexNoiseF.noiseXYZ(1.0F, 2.0F, 3.0F));
		
		assertEquals(0.0F, SimplexNoiseF.noiseXYZ(Float.NaN, 0.0F, 0.0F));
		assertEquals(0.0F, SimplexNoiseF.noiseXYZ(0.0F, Float.NaN, 0.0F));
		assertEquals(0.0F, SimplexNoiseF.noiseXYZ(0.0F, 0.0F, Float.NaN));
	}
	
	@Test
	public void testNoiseXYZW() {
		for(int i = 0; i < 100000; i++) {
			final float x = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float y = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float z = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float w = Randoms.nextFloat(-100000.0F, 100000.0F);
			
			final float noise = SimplexNoiseF.noiseXYZW(x, y, z, w);
			
			assertTrue(noise >= -1.0F && noise <= 1.0F);
		}
		
		assertEquals(0.0F, SimplexNoiseF.noiseXYZW(Float.NaN, 0.0F, 0.0F, 0.0F));
		assertEquals(0.0F, SimplexNoiseF.noiseXYZW(0.0F, Float.NaN, 0.0F, 0.0F));
		assertEquals(0.0F, SimplexNoiseF.noiseXYZW(0.0F, 0.0F, Float.NaN, 0.0F));
		assertEquals(0.0F, SimplexNoiseF.noiseXYZW(0.0F, 0.0F, 0.0F, Float.NaN));
	}
	
	@Test
	public void testTurbulenceX() {
		for(int i = 0; i < 100000; i++) {
			final float x = Randoms.nextFloat(-100000.0F, 100000.0F);
			
			final float amplitude = 1.0F;
			final float frequency = 1.0F;
			final float gain = 1.0F;
			final float lacunarity = 1.0F;
			
			final int octaves = 16;
			
			final float noise = SimplexNoiseF.turbulenceX(x, amplitude, frequency, gain, lacunarity, octaves);
			
			assertTrue(noise >= 0.0F && noise <= 16.0F);
		}
	}
	
	@Test
	public void testTurbulenceXY() {
		for(int i = 0; i < 100000; i++) {
			final float x = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float y = Randoms.nextFloat(-100000.0F, 100000.0F);
			
			final float amplitude = 1.0F;
			final float frequency = 1.0F;
			final float gain = 1.0F;
			final float lacunarity = 1.0F;
			
			final int octaves = 16;
			
			final float noise = SimplexNoiseF.turbulenceXY(x, y, amplitude, frequency, gain, lacunarity, octaves);
			
			assertTrue(noise >= 0.0F && noise <= 16.0F);
		}
	}
	
	@Test
	public void testTurbulenceXYZ() {
		for(int i = 0; i < 100000; i++) {
			final float x = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float y = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float z = Randoms.nextFloat(-100000.0F, 100000.0F);
			
			final float amplitude = 1.0F;
			final float frequency = 1.0F;
			final float gain = 1.0F;
			final float lacunarity = 1.0F;
			
			final int octaves = 16;
			
			final float noise = SimplexNoiseF.turbulenceXYZ(x, y, z, amplitude, frequency, gain, lacunarity, octaves);
			
			assertTrue(noise >= 0.0F && noise <= 16.0F);
		}
	}
	
	@Test
	public void testTurbulenceXYZW() {
		for(int i = 0; i < 100000; i++) {
			final float x = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float y = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float z = Randoms.nextFloat(-100000.0F, 100000.0F);
			final float w = Randoms.nextFloat(-100000.0F, 100000.0F);
			
			final float amplitude = 1.0F;
			final float frequency = 1.0F;
			final float gain = 1.0F;
			final float lacunarity = 1.0F;
			
			final int octaves = 16;
			
			final float noise = SimplexNoiseF.turbulenceXYZW(x, y, z, w, amplitude, frequency, gain, lacunarity, octaves);
			
			assertTrue(noise >= 0.0F && noise <= 16.0F);
		}
	}
}