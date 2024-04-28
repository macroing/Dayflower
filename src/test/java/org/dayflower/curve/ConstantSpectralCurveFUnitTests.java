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
public final class ConstantSpectralCurveFUnitTests {
	public ConstantSpectralCurveFUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final ConstantSpectralCurveF constantSpectralCurveF = new ConstantSpectralCurveF(1.0F);
		
		assertEquals(1.0F, constantSpectralCurveF.getAmplitude());
	}
	
	@Test
	public void testEquals() {
		final ConstantSpectralCurveF a = new ConstantSpectralCurveF(1.0F);
		final ConstantSpectralCurveF b = new ConstantSpectralCurveF(1.0F);
		final ConstantSpectralCurveF c = new ConstantSpectralCurveF(2.0F);
		final ConstantSpectralCurveF d = null;
		
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
		final ConstantSpectralCurveF constantSpectralCurveF = new ConstantSpectralCurveF(1.0F);
		
		assertEquals(1.0F, constantSpectralCurveF.getAmplitude());
	}
	
	@Test
	public void testHashCode() {
		final ConstantSpectralCurveF a = new ConstantSpectralCurveF(1.0F);
		final ConstantSpectralCurveF b = new ConstantSpectralCurveF(1.0F);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testSample() {
		final ConstantSpectralCurveF constantSpectralCurveF = new ConstantSpectralCurveF(1.0F);
		
		assertEquals(1.0F, constantSpectralCurveF.sample(0.5F));
	}
	
	@Test
	public void testToString() {
		final ConstantSpectralCurveF constantSpectralCurveF = new ConstantSpectralCurveF(1.0F);
		
		assertEquals("new ConstantSpectralCurveF(1.0F)", constantSpectralCurveF.toString());
	}
}