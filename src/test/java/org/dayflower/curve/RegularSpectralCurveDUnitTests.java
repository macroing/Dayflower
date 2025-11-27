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
public final class RegularSpectralCurveDUnitTests {
	public RegularSpectralCurveDUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final RegularSpectralCurveD regularSpectralCurveD = new RegularSpectralCurveD(1.0D, 2.0D, new double[] {1.0D, 2.0D, 3.0D});
		
		assertEquals(1.0D, regularSpectralCurveD.getLambdaMin());
		assertEquals(2.0D, regularSpectralCurveD.getLambdaMax());
		
		assertArrayEquals(new double[] {1.0D, 2.0D, 3.0D}, regularSpectralCurveD.getSpectrum());
		
		assertThrows(NullPointerException.class, () -> new RegularSpectralCurveD(1.0D, 2.0D, null));
	}
	
	@Test
	public void testEquals() {
		final RegularSpectralCurveD a = new RegularSpectralCurveD(1.0D, 2.0D, new double[] {1.0D});
		final RegularSpectralCurveD b = new RegularSpectralCurveD(1.0D, 2.0D, new double[] {1.0D});
		final RegularSpectralCurveD c = new RegularSpectralCurveD(1.0D, 2.0D, new double[] {2.0D});
		final RegularSpectralCurveD d = new RegularSpectralCurveD(1.0D, 3.0D, new double[] {1.0D});
		final RegularSpectralCurveD e = new RegularSpectralCurveD(0.0D, 2.0D, new double[] {1.0D});
		final RegularSpectralCurveD f = null;
		
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
		final RegularSpectralCurveD regularSpectralCurveD = new RegularSpectralCurveD(1.0D, 2.0D, new double[] {1.0D, 2.0D, 3.0D});
		
		assertEquals(2.0D, regularSpectralCurveD.getLambdaMax());
	}
	
	@Test
	public void testGetLambdaMin() {
		final RegularSpectralCurveD regularSpectralCurveD = new RegularSpectralCurveD(1.0D, 2.0D, new double[] {1.0D, 2.0D, 3.0D});
		
		assertEquals(1.0D, regularSpectralCurveD.getLambdaMin());
	}
	
	@Test
	public void testGetSpectrum() {
		final double[] spectrum = new double[] {1.0D, 2.0D, 3.0D};
		
		final RegularSpectralCurveD regularSpectralCurveD = new RegularSpectralCurveD(1.0D, 2.0D, spectrum);
		
		assertArrayEquals(spectrum, regularSpectralCurveD.getSpectrum());
		
		assertTrue(regularSpectralCurveD.getSpectrum() != spectrum);
	}
	
	@Test
	public void testHashCode() {
		final RegularSpectralCurveD a = new RegularSpectralCurveD(1.0D, 2.0D, new double[] {1.0D});
		final RegularSpectralCurveD b = new RegularSpectralCurveD(1.0D, 2.0D, new double[] {1.0D});
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testSample() {
		assertEquals(0.0D, new RegularSpectralCurveD(1.0D, 2.0D, new double[] {1.0D, 2.0D, 3.0D}).sample(0.0D));
		assertEquals(0.0D, new RegularSpectralCurveD(1.0D, 2.0D, new double[] {1.0D, 2.0D, 3.0D}).sample(3.0D));
		assertEquals(1.0D, new RegularSpectralCurveD(1.0D, 2.0D, new double[] {1.0D, 2.0D, 3.0D}).sample(1.0D));
		assertEquals(2.0D, new RegularSpectralCurveD(1.0D, 2.0D, new double[] {1.0D, 2.0D, 3.0D}).sample(1.5D));
		assertEquals(3.0D, new RegularSpectralCurveD(1.0D, 2.0D, new double[] {1.0D, 2.0D, 3.0D}).sample(2.0D));
	}
	
	@Test
	public void testToString() {
		final RegularSpectralCurveD regularSpectralCurveD = new RegularSpectralCurveD(1.0D, 2.0D, new double[] {1.0D});
		
		assertEquals("new RegularSpectralCurveD(1.0D, 2.0D, new double[] {1.0D})", regularSpectralCurveD.toString());
	}
}