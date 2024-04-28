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

import org.dayflower.color.Color3F;
import org.dayflower.color.ColorSpaceF;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class ChromaticSpectralCurveFUnitTests {
	public ChromaticSpectralCurveFUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final ChromaticSpectralCurveF chromaticSpectralCurveF = new ChromaticSpectralCurveF(1.0F, 2.0F);
		
		assertEquals(1.0F, chromaticSpectralCurveF.getX());
		assertEquals(2.0F, chromaticSpectralCurveF.getY());
	}
	
	@Test
	public void testEquals() {
		final ChromaticSpectralCurveF a = new ChromaticSpectralCurveF(1.0F, 2.0F);
		final ChromaticSpectralCurveF b = new ChromaticSpectralCurveF(1.0F, 2.0F);
		final ChromaticSpectralCurveF c = new ChromaticSpectralCurveF(1.0F, 3.0F);
		final ChromaticSpectralCurveF d = new ChromaticSpectralCurveF(3.0F, 2.0F);
		final ChromaticSpectralCurveF e = null;
		
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
	public void testGetX() {
		final ChromaticSpectralCurveF chromaticSpectralCurveF = new ChromaticSpectralCurveF(1.0F, 2.0F);
		
		assertEquals(1.0F, chromaticSpectralCurveF.getX());
	}
	
	@Test
	public void testGetY() {
		final ChromaticSpectralCurveF chromaticSpectralCurveF = new ChromaticSpectralCurveF(1.0F, 2.0F);
		
		assertEquals(2.0F, chromaticSpectralCurveF.getY());
	}
	
	@Test
	public void testHashCode() {
		final ChromaticSpectralCurveF a = new ChromaticSpectralCurveF(1.0F, 2.0F);
		final ChromaticSpectralCurveF b = new ChromaticSpectralCurveF(1.0F, 2.0F);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testSample() {
		final ChromaticSpectralCurveF chromaticSpectralCurveF = new ChromaticSpectralCurveF(0.31271F, 0.32902F);
		
		assertEquals(100.0F, chromaticSpectralCurveF.sample(560.0F));
	}
	
	@Test
	public void testToColorRGB() {
		final ChromaticSpectralCurveF chromaticSpectralCurveF = new ChromaticSpectralCurveF(0.31271F, 0.32902F);
		
		final Color3F a = chromaticSpectralCurveF.toColorRGB();
		final Color3F b = new Color3F(a.r / a.g, 1.0F, a.b / a.g);
		
		assertEquals(1.0009133F, b.r);
		assertEquals(1.0000000F, b.g);
		assertEquals(1.0010694F, b.b);
	}
	
	@Test
	public void testToColorXYZ() {
		final ChromaticSpectralCurveF chromaticSpectralCurveF = new ChromaticSpectralCurveF(0.31271F, 0.32902F);
		
		final Color3F a = chromaticSpectralCurveF.toColorXYZ();
		final Color3F b = new Color3F(a.r / a.g, 1.0F, a.b / a.g);
		final Color3F c = ColorSpaceF.getDefault().convertXYZToRGB(b);
		
		assertEquals(1.00064180F, c.r);
		assertEquals(0.99972874F, c.g);
		assertEquals(1.00079790F, c.b);
	}
	
	@Test
	public void testToColorXYZFloatFloat() {
		final Color3F a = ChromaticSpectralCurveF.toColorXYZ(0.31271F, 0.32902F);
		final Color3F b = new Color3F(a.r / a.g, 1.0F, a.b / a.g);
		final Color3F c = ColorSpaceF.getDefault().convertXYZToRGB(b);
		
		assertEquals(1.00064220F, c.r);
		assertEquals(0.99972856F, c.g);
		assertEquals(1.00079760F, c.b);
	}
	
	@Test
	public void testToString() {
		final ChromaticSpectralCurveF chromaticSpectralCurveF = new ChromaticSpectralCurveF(1.0F, 2.0F);
		
		assertEquals("new ChromaticSpectralCurveF(1.0F, 2.0F)", chromaticSpectralCurveF.toString());
	}
}