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

import org.dayflower.color.Color3D;
import org.dayflower.color.ColorSpaceD;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class ChromaticSpectralCurveDUnitTests {
	public ChromaticSpectralCurveDUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final ChromaticSpectralCurveD chromaticSpectralCurveD = new ChromaticSpectralCurveD(1.0D, 2.0D);
		
		assertEquals(1.0D, chromaticSpectralCurveD.getX());
		assertEquals(2.0D, chromaticSpectralCurveD.getY());
	}
	
	@Test
	public void testEquals() {
		final ChromaticSpectralCurveD a = new ChromaticSpectralCurveD(1.0D, 2.0D);
		final ChromaticSpectralCurveD b = new ChromaticSpectralCurveD(1.0D, 2.0D);
		final ChromaticSpectralCurveD c = new ChromaticSpectralCurveD(1.0D, 3.0D);
		final ChromaticSpectralCurveD d = new ChromaticSpectralCurveD(3.0D, 2.0D);
		final ChromaticSpectralCurveD e = null;
		
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
		final ChromaticSpectralCurveD chromaticSpectralCurveD = new ChromaticSpectralCurveD(1.0D, 2.0D);
		
		assertEquals(1.0D, chromaticSpectralCurveD.getX());
	}
	
	@Test
	public void testGetY() {
		final ChromaticSpectralCurveD chromaticSpectralCurveD = new ChromaticSpectralCurveD(1.0D, 2.0D);
		
		assertEquals(2.0D, chromaticSpectralCurveD.getY());
	}
	
	@Test
	public void testHashCode() {
		final ChromaticSpectralCurveD a = new ChromaticSpectralCurveD(1.0D, 2.0D);
		final ChromaticSpectralCurveD b = new ChromaticSpectralCurveD(1.0D, 2.0D);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testSample() {
		final ChromaticSpectralCurveD chromaticSpectralCurveD = new ChromaticSpectralCurveD(0.31271D, 0.32902D);
		
		assertEquals(100.0D, chromaticSpectralCurveD.sample(560.0D));
	}
	
	@Test
	public void testToColorRGB() {
		final ChromaticSpectralCurveD chromaticSpectralCurveD = new ChromaticSpectralCurveD(0.31271D, 0.32902D);
		
		final Color3D a = chromaticSpectralCurveD.toColorRGB();
		final Color3D b = new Color3D(a.r / a.g, 1.0D, a.b / a.g);
		
		assertEquals(1.0009127260268410D, b.r);
		assertEquals(1.0000000000000000D, b.g);
		assertEquals(1.0010692666493874D, b.b);
	}
	
	@Test
	public void testToColorXYZ() {
		final ChromaticSpectralCurveD chromaticSpectralCurveD = new ChromaticSpectralCurveD(0.31271D, 0.32902D);
		
		final Color3D a = chromaticSpectralCurveD.toColorXYZ();
		final Color3D b = new Color3D(a.r / a.g, 1.0D, a.b / a.g);
		final Color3D c = ColorSpaceD.getDefault().convertXYZToRGB(b);
		
		assertEquals(1.0006412930320563D, c.r);
		assertEquals(0.9997288145232578D, c.g);
		assertEquals(1.0007977912030590D, c.b);
	}
	
	@Test
	public void testToColorXYZDoubleDouble() {
		final Color3D a = ChromaticSpectralCurveD.toColorXYZ(0.31271D, 0.32902D);
		final Color3D b = new Color3D(a.r / a.g, 1.0D, a.b / a.g);
		final Color3D c = ColorSpaceD.getDefault().convertXYZToRGB(b);
		
		assertEquals(1.0006412930320567D, c.r);
		assertEquals(0.9997288145232576D, c.g);
		assertEquals(1.0007977912030608D, c.b);
	}
	
	@Test
	public void testToString() {
		final ChromaticSpectralCurveD chromaticSpectralCurveD = new ChromaticSpectralCurveD(1.0D, 2.0D);
		
		assertEquals("new ChromaticSpectralCurveD(1.0D, 2.0D)", chromaticSpectralCurveD.toString());
	}
}