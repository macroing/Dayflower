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
package org.dayflower.curve;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class ConstantSpectralCurveDUnitTests {
	public ConstantSpectralCurveDUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final ConstantSpectralCurveD constantSpectralCurveD = new ConstantSpectralCurveD(1.0D);
		
		assertEquals(1.0D, constantSpectralCurveD.getAmplitude());
	}
	
	@Test
	public void testEquals() {
		final ConstantSpectralCurveD a = new ConstantSpectralCurveD(1.0D);
		final ConstantSpectralCurveD b = new ConstantSpectralCurveD(1.0D);
		final ConstantSpectralCurveD c = new ConstantSpectralCurveD(2.0D);
		final ConstantSpectralCurveD d = null;
		
		assertEquals(a, a);
		assertEquals(a, b);
		assertEquals(b, a);
		
		assertNotEquals(a, c);
		assertNotEquals(c, a);
		assertNotEquals(a, d);
		assertNotEquals(d, a);
	}
	
	@Test
	public void testGetAmplitude() {
		final ConstantSpectralCurveD constantSpectralCurveD = new ConstantSpectralCurveD(1.0D);
		
		assertEquals(1.0D, constantSpectralCurveD.getAmplitude());
	}
	
	@Test
	public void testHashCode() {
		final ConstantSpectralCurveD a = new ConstantSpectralCurveD(1.0D);
		final ConstantSpectralCurveD b = new ConstantSpectralCurveD(1.0D);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testSample() {
		final ConstantSpectralCurveD constantSpectralCurveD = new ConstantSpectralCurveD(1.0D);
		
		assertEquals(1.0D, constantSpectralCurveD.sample(0.5D));
	}
	
	@Test
	public void testToString() {
		final ConstantSpectralCurveD constantSpectralCurveD = new ConstantSpectralCurveD(1.0D);
		
		assertEquals("new ConstantSpectralCurveD(1.0D)", constantSpectralCurveD.toString());
	}
}