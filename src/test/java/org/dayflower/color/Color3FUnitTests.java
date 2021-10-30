/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class Color3FUnitTests {
	public Color3FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final Color3F color = new Color3F();
		
		assertEquals(0.0F, color.getComponent1());
		assertEquals(0.0F, color.getComponent2());
		assertEquals(0.0F, color.getComponent3());
	}
	
	@Test
	public void testConstructorColor3D() {
		final Color3F color = new Color3F(new Color3D(1.0D, 1.0D, 1.0D));
		
		assertEquals(1.0F, color.getComponent1());
		assertEquals(1.0F, color.getComponent2());
		assertEquals(1.0F, color.getComponent3());
		assertThrows(NullPointerException.class, () -> new Color3F((Color3D)(null)));
	}
	
	@Test
	public void testConstructorColor4F() {
		final Color3F color = new Color3F(new Color4F(1.0F, 1.0F, 1.0F));
		
		assertEquals(1.0F, color.getComponent1());
		assertEquals(1.0F, color.getComponent2());
		assertEquals(1.0F, color.getComponent3());
		assertThrows(NullPointerException.class, () -> new Color3F((Color4F)(null)));
	}
	
	@Test
	public void testConstructorFloat() {
		final Color3F color = new Color3F(2.0F);
		
		assertEquals(2.0F, color.getComponent1());
		assertEquals(2.0F, color.getComponent2());
		assertEquals(2.0F, color.getComponent3());
	}
	
	@Test
	public void testConstructorFloatFloatFloat() {
		final Color3F color = new Color3F(0.0F, 1.0F, 2.0F);
		
		assertEquals(0.0F, color.getComponent1());
		assertEquals(1.0F, color.getComponent2());
		assertEquals(2.0F, color.getComponent3());
	}
	
	@Test
	public void testConstructorInt() {
		final Color3F color = new Color3F(255);
		
		assertEquals(1.0F, color.getComponent1());
		assertEquals(1.0F, color.getComponent2());
		assertEquals(1.0F, color.getComponent3());
	}
	
	@Test
	public void testConstructorIntIntInt() {
		final Color3F color = new Color3F(0, 255, 300);
		
		assertEquals(0.0F, color.getComponent1());
		assertEquals(1.0F, color.getComponent2());
		assertEquals(1.0F, color.getComponent3());
	}
	
	@Test
	public void testEquals() {
		final Color3F a = new Color3F(0.0F, 0.5F, 1.0F);
		final Color3F b = new Color3F(0.0F, 0.5F, 1.0F);
		final Color3F c = new Color3F(0.0F, 0.5F, 2.0F);
		final Color3F d = new Color3F(0.0F, 2.0F, 1.0F);
		final Color3F e = new Color3F(2.0F, 0.5F, 1.0F);
		final Color3F f = null;
		
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
	public void testGetB() {
		final Color3F color = new Color3F(0.0F, 0.0F, 2.0F);
		
		assertEquals(2.0F, color.getB());
	}
	
	@Test
	public void testGetComponent1() {
		final Color3F color = new Color3F(2.0F, 0.0F, 0.0F);
		
		assertEquals(2.0F, color.getComponent1());
	}
	
	@Test
	public void testGetComponent2() {
		final Color3F color = new Color3F(0.0F, 2.0F, 0.0F);
		
		assertEquals(2.0F, color.getComponent2());
	}
	
	@Test
	public void testGetComponent3() {
		final Color3F color = new Color3F(0.0F, 0.0F, 2.0F);
		
		assertEquals(2.0F, color.getComponent3());
	}
	
	@Test
	public void testGetG() {
		final Color3F color = new Color3F(0.0F, 2.0F, 0.0F);
		
		assertEquals(2.0F, color.getG());
	}
	
	@Test
	public void testGetR() {
		final Color3F color = new Color3F(2.0F, 0.0F, 0.0F);
		
		assertEquals(2.0F, color.getR());
	}
	
	@Test
	public void testGetX() {
		final Color3F color = new Color3F(2.0F, 0.0F, 0.0F);
		
		assertEquals(2.0F, color.getX());
	}
	
	@Test
	public void testGetY() {
		final Color3F color = new Color3F(0.0F, 2.0F, 0.0F);
		
		assertEquals(2.0F, color.getY());
	}
	
	@Test
	public void testGetZ() {
		final Color3F color = new Color3F(0.0F, 0.0F, 2.0F);
		
		assertEquals(2.0F, color.getZ());
	}
	
	@Test
	public void testHasInfinities() {
		final Color3F a = new Color3F(Float.POSITIVE_INFINITY, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, Float.POSITIVE_INFINITY, 0.0F);
		final Color3F c = new Color3F(0.0F, 0.0F, Float.POSITIVE_INFINITY);
		final Color3F d = new Color3F(0.0F, 0.0F, 0.0F);
		
		assertTrue(a.hasInfinites());
		assertTrue(b.hasInfinites());
		assertTrue(c.hasInfinites());
		assertFalse(d.hasInfinites());
	}
	
	@Test
	public void testHashCode() {
		final Color3F a = new Color3F(0.0F, 0.5F, 1.0F);
		final Color3F b = new Color3F(0.0F, 0.5F, 1.0F);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testHasNaNs() {
		final Color3F a = new Color3F(Float.NaN, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, Float.NaN, 0.0F);
		final Color3F c = new Color3F(0.0F, 0.0F, Float.NaN);
		final Color3F d = new Color3F(0.0F, 0.0F, 0.0F);
		
		assertTrue(a.hasNaNs());
		assertTrue(b.hasNaNs());
		assertTrue(c.hasNaNs());
		assertFalse(d.hasNaNs());
	}
	
	@Test
	public void testIsBlack() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, 0.0F, 1.0F);
		
		assertTrue(a.isBlack());
		assertFalse(b.isBlack());
	}
	
	@Test
	public void testIsBlue() {
		final Color3F a = new Color3F(0.0F, 0.0F, 1.0F);
		final Color3F b = new Color3F(0.5F, 0.5F, 1.0F);
		
		assertTrue(a.isBlue());
		assertFalse(b.isBlue());
	}
	
	@Test
	public void testIsBlueFloatFloat() {
		final Color3F a = new Color3F(0.0F, 0.0F, 1.0F);
		final Color3F b = new Color3F(0.5F, 0.5F, 1.0F);
		final Color3F c = new Color3F(1.0F, 1.0F, 1.0F);
		
		assertTrue(a.isBlue(0.5F, 0.5F));
		assertTrue(b.isBlue(0.5F, 0.5F));
		assertFalse(c.isBlue(0.5F, 0.5F));
	}
	
	@Test
	public void testIsCyan() {
		final Color3F a = new Color3F(0.0F, 1.0F, 1.0F);
		final Color3F b = new Color3F(0.0F, 0.5F, 0.5F);
		final Color3F c = new Color3F(1.0F, 1.0F, 1.0F);
		
		assertTrue(a.isCyan());
		assertTrue(b.isCyan());
		assertFalse(c.isCyan());
	}
	
	@Test
	public void testIsGrayscale() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.5F, 0.5F, 0.5F);
		final Color3F c = new Color3F(1.0F, 1.0F, 1.0F);
		final Color3F d = new Color3F(0.0F, 0.0F, 0.5F);
		final Color3F e = new Color3F(0.0F, 0.5F, 0.5F);
		final Color3F f = new Color3F(0.0F, 0.5F, 0.0F);
		final Color3F g = new Color3F(0.0F, 0.5F, 1.0F);
		
		assertTrue(a.isGrayscale());
		assertTrue(b.isGrayscale());
		assertTrue(c.isGrayscale());
		assertFalse(d.isGrayscale());
		assertFalse(e.isGrayscale());
		assertFalse(f.isGrayscale());
		assertFalse(g.isGrayscale());
	}
	
	@Test
	public void testIsGreen() {
		final Color3F a = new Color3F(0.0F, 1.0F, 0.0F);
		final Color3F b = new Color3F(0.5F, 1.0F, 0.5F);
		
		assertTrue(a.isGreen());
		assertFalse(b.isGreen());
	}
	
	@Test
	public void testIsGreenFloatFloat() {
		final Color3F a = new Color3F(0.0F, 1.0F, 0.0F);
		final Color3F b = new Color3F(0.5F, 1.0F, 0.5F);
		final Color3F c = new Color3F(1.0F, 1.0F, 1.0F);
		
		assertTrue(a.isGreen(0.5F, 0.5F));
		assertTrue(b.isGreen(0.5F, 0.5F));
		assertFalse(c.isGreen(0.5F, 0.5F));
	}
	
	@Test
	public void testIsMagenta() {
		final Color3F a = new Color3F(1.0F, 0.0F, 1.0F);
		final Color3F b = new Color3F(0.5F, 0.0F, 0.5F);
		final Color3F c = new Color3F(1.0F, 1.0F, 1.0F);
		
		assertTrue(a.isMagenta());
		assertTrue(b.isMagenta());
		assertFalse(c.isMagenta());
	}
	
	@Test
	public void testIsRed() {
		final Color3F a = new Color3F(1.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(1.0F, 0.5F, 0.5F);
		
		assertTrue(a.isRed());
		assertFalse(b.isRed());
	}
	
	@Test
	public void testIsRedFloatFloat() {
		final Color3F a = new Color3F(1.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(1.0F, 0.5F, 0.5F);
		final Color3F c = new Color3F(1.0F, 1.0F, 1.0F);
		
		assertTrue(a.isRed(0.5F, 0.5F));
		assertTrue(b.isRed(0.5F, 0.5F));
		assertFalse(c.isRed(0.5F, 0.5F));
	}
	
	@Test
	public void testIsWhite() {
		final Color3F a = new Color3F(1.0F, 1.0F, 1.0F);
		final Color3F b = new Color3F(2.0F, 2.0F, 2.0F);
		final Color3F c = new Color3F(1.0F, 1.5F, 2.0F);
		
		assertTrue(a.isWhite());
		assertTrue(b.isWhite());
		assertFalse(c.isWhite());
	}
	
	@Test
	public void testIsYellow() {
		final Color3F a = new Color3F(1.0F, 1.0F, 0.0F);
		final Color3F b = new Color3F(0.5F, 0.5F, 0.0F);
		final Color3F c = new Color3F(1.0F, 1.0F, 1.0F);
		
		assertTrue(a.isYellow());
		assertTrue(b.isYellow());
		assertFalse(c.isYellow());
	}
	
	@Test
	public void testMaximum() {
		final Color3F color = new Color3F(0.0F, 1.0F, 2.0F);
		
		assertEquals(2.0F, color.maximum());
	}
	
	@Test
	public void testMinimum() {
		final Color3F color = new Color3F(0.0F, 1.0F, 2.0F);
		
		assertEquals(0.0F, color.minimum());
	}
	
	@Test
	public void testToString() {
		final Color3F color = new Color3F(0.0F, 0.5F, 1.0F);
		
		assertEquals("new Color3F(0.0F, 0.5F, 1.0F)", color.toString());
	}
}