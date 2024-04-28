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
public final class ColorSpaceFUnitTests {
	public ColorSpaceFUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConvertRGBAToXYZAAndConvertXYZAToRGBA() {
		final ColorSpaceF colorSpace = ColorSpaceF.IDENTITY;
		
		final Color4F a = Color4F.GRAY;
		final Color4F b = colorSpace.convertRGBAToXYZA(a);
		final Color4F c = colorSpace.convertXYZAToRGBA(b);
		
		assertEquals(+0.5F, b.r);
		assertEquals(+0.5F, b.g);
		assertEquals(-0.5F, b.b);
		assertEquals(+1.0F, b.a);
		
		assertEquals(+0.5F, c.r);
		assertEquals(+0.5F, c.g);
		assertEquals(+0.5F, c.b);
		assertEquals(+1.0F, c.a);
		
		assertThrows(NullPointerException.class, () -> colorSpace.convertRGBAToXYZA(null));
		assertThrows(NullPointerException.class, () -> colorSpace.convertXYZAToRGBA(null));
	}
	
	@Test
	public void testConvertRGBToXYZAndConvertXYZToRGB() {
		final ColorSpaceF colorSpace = ColorSpaceF.IDENTITY;
		
		final Color3F a = Color3F.GRAY;
		final Color3F b = colorSpace.convertRGBToXYZ(a);
		final Color3F c = colorSpace.convertXYZToRGB(b);
		
		assertEquals(+0.5F, b.r);
		assertEquals(+0.5F, b.g);
		assertEquals(-0.5F, b.b);
		
		assertEquals(+0.5F, c.r);
		assertEquals(+0.5F, c.g);
		assertEquals(+0.5F, c.b);
		
		assertThrows(NullPointerException.class, () -> colorSpace.convertRGBToXYZ(null));
		assertThrows(NullPointerException.class, () -> colorSpace.convertXYZToRGB(null));
	}
	
	@Test
	public void testGetDefault() {
		assertTrue(ColorSpaceF.getDefault() == ColorSpaceF.S_R_G_B);
	}
	
	@Test
	public void testRedoGammaCorrectionColor3F() {
		final ColorSpaceF colorSpace = ColorSpaceF.IDENTITY;
		
		final Color3F a = Color3F.GRAY;
		final Color3F b = colorSpace.redoGammaCorrection(a);
		final Color3F c = new Color3F(2.0F, 2.0F, 2.0F);
		final Color3F d = colorSpace.redoGammaCorrection(c);
		
		assertEquals(a, b);
		assertEquals(c, d);
		
		assertThrows(NullPointerException.class, () -> colorSpace.redoGammaCorrection((Color3F)(null)));
	}
	
	@Test
	public void testRedoGammaCorrectionColor4F() {
		final ColorSpaceF colorSpace = ColorSpaceF.IDENTITY;
		
		final Color4F a = Color4F.GRAY;
		final Color4F b = colorSpace.redoGammaCorrection(a);
		final Color4F c = new Color4F(2.0F, 2.0F, 2.0F, 2.0F);
		final Color4F d = colorSpace.redoGammaCorrection(c);
		
		assertEquals(a, b);
		assertEquals(c, d);
		
		assertThrows(NullPointerException.class, () -> colorSpace.redoGammaCorrection((Color4F)(null)));
	}
	
	@Test
	public void testUndoGammaCorrectionColor3F() {
		final ColorSpaceF colorSpace = ColorSpaceF.IDENTITY;
		
		final Color3F a = Color3F.GRAY;
		final Color3F b = colorSpace.undoGammaCorrection(a);
		final Color3F c = new Color3F(2.0F, 2.0F, 2.0F);
		final Color3F d = colorSpace.undoGammaCorrection(c);
		
		assertEquals(a, b);
		assertEquals(c, d);
		
		assertThrows(NullPointerException.class, () -> colorSpace.undoGammaCorrection((Color3F)(null)));
	}
	
	@Test
	public void testUndoGammaCorrectionColor4F() {
		final ColorSpaceF colorSpace = ColorSpaceF.IDENTITY;
		
		final Color4F a = Color4F.GRAY;
		final Color4F b = colorSpace.undoGammaCorrection(a);
		final Color4F c = new Color4F(2.0F, 2.0F, 2.0F, 2.0F);
		final Color4F d = colorSpace.undoGammaCorrection(c);
		
		assertEquals(a, b);
		assertEquals(c, d);
		
		assertThrows(NullPointerException.class, () -> colorSpace.undoGammaCorrection((Color4F)(null)));
	}
}