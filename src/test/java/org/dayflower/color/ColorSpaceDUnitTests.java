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
package org.dayflower.color;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class ColorSpaceDUnitTests {
	public ColorSpaceDUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConvertRGBAToXYZAAndConvertXYZAToRGBA() {
		final ColorSpaceD colorSpace = ColorSpaceD.IDENTITY;
		
		final Color4D a = Color4D.GRAY;
		final Color4D b = colorSpace.convertRGBAToXYZA(a);
		final Color4D c = colorSpace.convertXYZAToRGBA(b);
		
		assertEquals(+0.5D, b.r);
		assertEquals(+0.5D, b.g);
		assertEquals(-0.5D, b.b);
		assertEquals(+1.0D, b.a);
		
		assertEquals(+0.5D, c.r);
		assertEquals(+0.5D, c.g);
		assertEquals(+0.5D, c.b);
		assertEquals(+1.0D, c.a);
		
		assertThrows(NullPointerException.class, () -> colorSpace.convertRGBAToXYZA(null));
		assertThrows(NullPointerException.class, () -> colorSpace.convertXYZAToRGBA(null));
	}
	
	@Test
	public void testConvertRGBToXYZAndConvertXYZToRGB() {
		final ColorSpaceD colorSpace = ColorSpaceD.IDENTITY;
		
		final Color3D a = Color3D.GRAY;
		final Color3D b = colorSpace.convertRGBToXYZ(a);
		final Color3D c = colorSpace.convertXYZToRGB(b);
		
		assertEquals(+0.5D, b.r);
		assertEquals(+0.5D, b.g);
		assertEquals(-0.5D, b.b);
		
		assertEquals(+0.5D, c.r);
		assertEquals(+0.5D, c.g);
		assertEquals(+0.5D, c.b);
		
		assertThrows(NullPointerException.class, () -> colorSpace.convertRGBToXYZ(null));
		assertThrows(NullPointerException.class, () -> colorSpace.convertXYZToRGB(null));
	}
	
	@Test
	public void testGetDefault() {
		assertTrue(ColorSpaceD.getDefault() == ColorSpaceD.S_R_G_B);
	}
	
	@Test
	public void testRedoGammaCorrectionColor3D() {
		final ColorSpaceD colorSpace = ColorSpaceD.IDENTITY;
		
		final Color3D a = Color3D.GRAY;
		final Color3D b = colorSpace.redoGammaCorrection(a);
		final Color3D c = new Color3D(2.0D, 2.0D, 2.0D);
		final Color3D d = colorSpace.redoGammaCorrection(c);
		
		assertEquals(a, b);
		assertEquals(c, d);
		
		assertThrows(NullPointerException.class, () -> colorSpace.redoGammaCorrection((Color3D)(null)));
	}
	
	@Test
	public void testRedoGammaCorrectionColor4D() {
		final ColorSpaceD colorSpace = ColorSpaceD.IDENTITY;
		
		final Color4D a = Color4D.GRAY;
		final Color4D b = colorSpace.redoGammaCorrection(a);
		final Color4D c = new Color4D(2.0D, 2.0D, 2.0D, 2.0D);
		final Color4D d = colorSpace.redoGammaCorrection(c);
		
		assertEquals(a, b);
		assertEquals(c, d);
		
		assertThrows(NullPointerException.class, () -> colorSpace.redoGammaCorrection((Color4D)(null)));
	}
	
	@Test
	public void testUndoGammaCorrectionColor3D() {
		final ColorSpaceD colorSpace = ColorSpaceD.IDENTITY;
		
		final Color3D a = Color3D.GRAY;
		final Color3D b = colorSpace.undoGammaCorrection(a);
		final Color3D c = new Color3D(2.0D, 2.0D, 2.0D);
		final Color3D d = colorSpace.undoGammaCorrection(c);
		
		assertEquals(a, b);
		assertEquals(c, d);
		
		assertThrows(NullPointerException.class, () -> colorSpace.undoGammaCorrection((Color3D)(null)));
	}
	
	@Test
	public void testUndoGammaCorrectionColor4D() {
		final ColorSpaceD colorSpace = ColorSpaceD.IDENTITY;
		
		final Color4D a = Color4D.GRAY;
		final Color4D b = colorSpace.undoGammaCorrection(a);
		final Color4D c = new Color4D(2.0D, 2.0D, 2.0D, 2.0D);
		final Color4D d = colorSpace.undoGammaCorrection(c);
		
		assertEquals(a, b);
		assertEquals(c, d);
		
		assertThrows(NullPointerException.class, () -> colorSpace.undoGammaCorrection((Color4D)(null)));
	}
}