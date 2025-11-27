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
package org.dayflower.curve;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class IrregularSpectralCurveFUnitTests {
	public IrregularSpectralCurveFUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final IrregularSpectralCurveF irregularSpectralCurveF = new IrregularSpectralCurveF(new float[] {1.0F, 2.0F, 3.0F}, new float[] {4.0F, 5.0F, 6.0F});
		
		assertArrayEquals(new float[] {1.0F, 2.0F, 3.0F}, irregularSpectralCurveF.getAmplitudes());
		assertArrayEquals(new float[] {4.0F, 5.0F, 6.0F}, irregularSpectralCurveF.getWavelengths());
		
		assertThrows(NullPointerException.class, () -> new IrregularSpectralCurveF(new float[0], null));
		assertThrows(NullPointerException.class, () -> new IrregularSpectralCurveF(null, new float[0]));
		
		assertThrows(IllegalArgumentException.class, () -> new IrregularSpectralCurveF(new float[0], new float[1]));
	}
	
	@Test
	public void testEquals() {
		final IrregularSpectralCurveF a = new IrregularSpectralCurveF(new float[] {1.0F}, new float[] {1.0F});
		final IrregularSpectralCurveF b = new IrregularSpectralCurveF(new float[] {1.0F}, new float[] {1.0F});
		final IrregularSpectralCurveF c = new IrregularSpectralCurveF(new float[] {1.0F}, new float[] {2.0F});
		final IrregularSpectralCurveF d = new IrregularSpectralCurveF(new float[] {2.0F}, new float[] {1.0F});
		final IrregularSpectralCurveF e = null;
		
		assertEquals(a, a);
		assertEquals(a, b);
		assertEquals(b, a);
		
		assertNotEquals(a, c);
		assertNotEquals(c, a);
		assertNotEquals(a, d);
		assertNotEquals(d, a);
		assertNotEquals(a, e);
		assertNotEquals(e, a);
	}
	
	@Test
	public void testGetAmplitudes() {
		final float[] amplitudes = new float[] {1.0F, 2.0F, 3.0F};
		final float[] wavelengths = new float[] {4.0F, 5.0F, 6.0F};
		
		final IrregularSpectralCurveF irregularSpectralCurveF = new IrregularSpectralCurveF(amplitudes, wavelengths);
		
		assertArrayEquals(amplitudes, irregularSpectralCurveF.getAmplitudes());
		
		assertTrue(irregularSpectralCurveF.getAmplitudes() != amplitudes);
	}
	
	@Test
	public void testGetWavelengths() {
		final float[] amplitudes = new float[] {1.0F, 2.0F, 3.0F};
		final float[] wavelengths = new float[] {4.0F, 5.0F, 6.0F};
		
		final IrregularSpectralCurveF irregularSpectralCurveF = new IrregularSpectralCurveF(amplitudes, wavelengths);
		
		assertArrayEquals(wavelengths, irregularSpectralCurveF.getWavelengths());
		
		assertTrue(irregularSpectralCurveF.getWavelengths() != wavelengths);
	}
	
	@Test
	public void testHashCode() {
		final IrregularSpectralCurveF a = new IrregularSpectralCurveF(new float[] {1.0F}, new float[] {1.0F});
		final IrregularSpectralCurveF b = new IrregularSpectralCurveF(new float[] {1.0F}, new float[] {1.0F});
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testSample() {
		assertEquals(0.0F, new IrregularSpectralCurveF(new float[] {}, new float[] {}).sample(0.0F));
		assertEquals(1.0F, new IrregularSpectralCurveF(new float[] {1.0F}, new float[] {1.0F}).sample(0.0F));
		assertEquals(1.0F, new IrregularSpectralCurveF(new float[] {1.0F, 2.0F}, new float[] {1.0F, 2.0F}).sample(0.0F));
		assertEquals(2.0F, new IrregularSpectralCurveF(new float[] {1.0F, 2.0F}, new float[] {1.0F, 2.0F}).sample(3.0F));
		assertEquals(3.0F, new IrregularSpectralCurveF(new float[] {1.0F, 2.0F, 3.0F, 4.0F, 5.0F}, new float[] {1.0F, 2.0F, 3.0F, 4.0F, 5.0F}).sample(3.0F));
		assertEquals(5.0F, new IrregularSpectralCurveF(new float[] {1.0F, 2.0F, 3.0F, 4.0F, 5.0F}, new float[] {1.0F, 2.0F, 3.0F, 4.0F, 5.0F}).sample(5.0F));
	}
	
	@Test
	public void testToString() {
		final IrregularSpectralCurveF irregularSpectralCurveF = new IrregularSpectralCurveF(new float[] {1.0F}, new float[] {1.0F});
		
		assertEquals("new IrregularSpectralCurveF(new float[] {1.0F}, new float[] {1.0F})", irregularSpectralCurveF.toString());
	}
}