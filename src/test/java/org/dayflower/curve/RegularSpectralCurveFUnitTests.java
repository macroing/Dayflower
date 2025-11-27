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
public final class RegularSpectralCurveFUnitTests {
	public RegularSpectralCurveFUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final RegularSpectralCurveF regularSpectralCurveF = new RegularSpectralCurveF(1.0F, 2.0F, new float[] {1.0F, 2.0F, 3.0F});
		
		assertEquals(1.0F, regularSpectralCurveF.getLambdaMin());
		assertEquals(2.0F, regularSpectralCurveF.getLambdaMax());
		
		assertArrayEquals(new float[] {1.0F, 2.0F, 3.0F}, regularSpectralCurveF.getSpectrum());
		
		assertThrows(NullPointerException.class, () -> new RegularSpectralCurveF(1.0F, 2.0F, null));
	}
	
	@Test
	public void testEquals() {
		final RegularSpectralCurveF a = new RegularSpectralCurveF(1.0F, 2.0F, new float[] {1.0F});
		final RegularSpectralCurveF b = new RegularSpectralCurveF(1.0F, 2.0F, new float[] {1.0F});
		final RegularSpectralCurveF c = new RegularSpectralCurveF(1.0F, 2.0F, new float[] {2.0F});
		final RegularSpectralCurveF d = new RegularSpectralCurveF(1.0F, 3.0F, new float[] {1.0F});
		final RegularSpectralCurveF e = new RegularSpectralCurveF(0.0F, 2.0F, new float[] {1.0F});
		final RegularSpectralCurveF f = null;
		
		assertEquals(a, a);
		assertEquals(a, b);
		assertEquals(b, a);
		
		assertNotEquals(a, c);
		assertNotEquals(c, a);
		assertNotEquals(a, d);
		assertNotEquals(d, a);
		assertNotEquals(a, e);
		assertNotEquals(e, a);
		assertNotEquals(a, f);
		assertNotEquals(f, a);
	}
	
	@Test
	public void testGetLambdaMax() {
		final RegularSpectralCurveF regularSpectralCurveF = new RegularSpectralCurveF(1.0F, 2.0F, new float[] {1.0F, 2.0F, 3.0F});
		
		assertEquals(2.0F, regularSpectralCurveF.getLambdaMax());
	}
	
	@Test
	public void testGetLambdaMin() {
		final RegularSpectralCurveF regularSpectralCurveF = new RegularSpectralCurveF(1.0F, 2.0F, new float[] {1.0F, 2.0F, 3.0F});
		
		assertEquals(1.0F, regularSpectralCurveF.getLambdaMin());
	}
	
	@Test
	public void testGetSpectrum() {
		final float[] spectrum = new float[] {1.0F, 2.0F, 3.0F};
		
		final RegularSpectralCurveF regularSpectralCurveF = new RegularSpectralCurveF(1.0F, 2.0F, spectrum);
		
		assertArrayEquals(spectrum, regularSpectralCurveF.getSpectrum());
		
		assertTrue(regularSpectralCurveF.getSpectrum() != spectrum);
	}
	
	@Test
	public void testHashCode() {
		final RegularSpectralCurveF a = new RegularSpectralCurveF(1.0F, 2.0F, new float[] {1.0F});
		final RegularSpectralCurveF b = new RegularSpectralCurveF(1.0F, 2.0F, new float[] {1.0F});
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testSample() {
		assertEquals(0.0F, new RegularSpectralCurveF(1.0F, 2.0F, new float[] {1.0F, 2.0F, 3.0F}).sample(0.0F));
		assertEquals(0.0F, new RegularSpectralCurveF(1.0F, 2.0F, new float[] {1.0F, 2.0F, 3.0F}).sample(3.0F));
		assertEquals(1.0F, new RegularSpectralCurveF(1.0F, 2.0F, new float[] {1.0F, 2.0F, 3.0F}).sample(1.0F));
		assertEquals(2.0F, new RegularSpectralCurveF(1.0F, 2.0F, new float[] {1.0F, 2.0F, 3.0F}).sample(1.5F));
		assertEquals(3.0F, new RegularSpectralCurveF(1.0F, 2.0F, new float[] {1.0F, 2.0F, 3.0F}).sample(2.0F));
	}
	
	@Test
	public void testToString() {
		final RegularSpectralCurveF regularSpectralCurveF = new RegularSpectralCurveF(1.0F, 2.0F, new float[] {1.0F});
		
		assertEquals("new RegularSpectralCurveF(1.0F, 2.0F, new float[] {1.0F})", regularSpectralCurveF.toString());
	}
}