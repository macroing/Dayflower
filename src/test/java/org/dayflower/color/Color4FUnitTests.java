/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
public final class Color4FUnitTests {
	public Color4FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstants() {
		assertEquals(new Color4F(0.0F, 0.0F, 0.0F, 1.0F), Color4F.BLACK);
		assertEquals(new Color4F(0.0F, 0.0F, 1.0F, 1.0F), Color4F.BLUE);
		assertEquals(new Color4F(0.0F, 1.0F, 1.0F, 1.0F), Color4F.CYAN);
		assertEquals(new Color4F(0.0F, 1.0F, 0.0F, 1.0F), Color4F.GREEN);
		assertEquals(new Color4F(1.0F, 0.0F, 1.0F, 1.0F), Color4F.MAGENTA);
		assertEquals(new Color4F(1.0F, 0.5F, 0.0F, 1.0F), Color4F.ORANGE);
		assertEquals(new Color4F(1.0F, 0.0F, 0.0F, 1.0F), Color4F.RED);
		assertEquals(new Color4F(0.0F, 0.0F, 0.0F, 0.0F), Color4F.TRANSPARENT);
		assertEquals(new Color4F(1.0F, 1.0F, 1.0F, 1.0F), Color4F.WHITE);
		assertEquals(new Color4F(1.0F, 1.0F, 0.0F, 1.0F), Color4F.YELLOW);
	}
	
	@Test
	public void testConstructor() {
		final Color4F color = new Color4F();
		
		assertEquals(0.0F, color.getComponent1());
		assertEquals(0.0F, color.getComponent2());
		assertEquals(0.0F, color.getComponent3());
		assertEquals(1.0F, color.getComponent4());
	}
	
	@Test
	public void testConstructorColor3D() {
		final Color4F color = new Color4F(new Color3D(1.0D, 1.0D, 1.0D));
		
		assertEquals(1.0F, color.getComponent1());
		assertEquals(1.0F, color.getComponent2());
		assertEquals(1.0F, color.getComponent3());
		assertEquals(1.0F, color.getComponent4());
		
		assertThrows(NullPointerException.class, () -> new Color4F((Color3D)(null)));
	}
	
	@Test
	public void testConstructorColor3DFloat() {
		final Color4F color = new Color4F(new Color3D(1.0D, 1.0D, 1.0D), 0.5F);
		
		assertEquals(1.0F, color.getComponent1());
		assertEquals(1.0F, color.getComponent2());
		assertEquals(1.0F, color.getComponent3());
		assertEquals(0.5F, color.getComponent4());
		
		assertThrows(NullPointerException.class, () -> new Color4F((Color3D)(null), 0.5F));
	}
	
	@Test
	public void testConstructorColor3F() {
		final Color4F color = new Color4F(new Color3F(1.0F, 1.0F, 1.0F));
		
		assertEquals(1.0F, color.getComponent1());
		assertEquals(1.0F, color.getComponent2());
		assertEquals(1.0F, color.getComponent3());
		assertEquals(1.0F, color.getComponent4());
		
		assertThrows(NullPointerException.class, () -> new Color4F((Color3F)(null)));
	}
	
	@Test
	public void testConstructorColor3FFloat() {
		final Color4F color = new Color4F(new Color3F(1.0F, 1.0F, 1.0F), 0.5F);
		
		assertEquals(1.0F, color.getComponent1());
		assertEquals(1.0F, color.getComponent2());
		assertEquals(1.0F, color.getComponent3());
		assertEquals(0.5F, color.getComponent4());
		
		assertThrows(NullPointerException.class, () -> new Color4F((Color3F)(null), 0.5F));
	}
	
	@Test
	public void testConstructorFloat() {
		final Color4F color = new Color4F(2.0F);
		
		assertEquals(2.0F, color.getComponent1());
		assertEquals(2.0F, color.getComponent2());
		assertEquals(2.0F, color.getComponent3());
		assertEquals(1.0F, color.getComponent4());
	}
	
	@Test
	public void testConstructorFloatFloat() {
		final Color4F color = new Color4F(2.0F, 0.5F);
		
		assertEquals(2.0F, color.getComponent1());
		assertEquals(2.0F, color.getComponent2());
		assertEquals(2.0F, color.getComponent3());
		assertEquals(0.5F, color.getComponent4());
	}
	
	@Test
	public void testConstructorFloatFloatFloat() {
		final Color4F color = new Color4F(0.0F, 1.0F, 2.0F);
		
		assertEquals(0.0F, color.getComponent1());
		assertEquals(1.0F, color.getComponent2());
		assertEquals(2.0F, color.getComponent3());
		assertEquals(1.0F, color.getComponent4());
	}
	
	@Test
	public void testConstructorFloatFloatFloatFloat() {
		final Color4F color = new Color4F(0.0F, 1.0F, 2.0F, 0.5F);
		
		assertEquals(0.0F, color.getComponent1());
		assertEquals(1.0F, color.getComponent2());
		assertEquals(2.0F, color.getComponent3());
		assertEquals(0.5F, color.getComponent4());
	}
	
	@Test
	public void testConstructorInt() {
		final Color4F color = new Color4F(255);
		
		assertEquals(1.0F, color.getComponent1());
		assertEquals(1.0F, color.getComponent2());
		assertEquals(1.0F, color.getComponent3());
		assertEquals(1.0F, color.getComponent4());
	}
	
	@Test
	public void testConstructorIntIntInt() {
		final Color4F color = new Color4F(0, 255, 300);
		
		assertEquals(0.0F, color.getComponent1());
		assertEquals(1.0F, color.getComponent2());
		assertEquals(1.0F, color.getComponent3());
		assertEquals(1.0F, color.getComponent4());
	}
	
	@Test
	public void testConstructorIntIntIntInt() {
		final Color4F color = new Color4F(0, 255, 300, 255);
		
		assertEquals(0.0F, color.getComponent1());
		assertEquals(1.0F, color.getComponent2());
		assertEquals(1.0F, color.getComponent3());
		assertEquals(1.0F, color.getComponent4());
	}
	
	@Test
	public void testEqualsColor4F() {
		final Color4F a = new Color4F(0.0F, 0.5F, 1.0F, 1.0F);
		final Color4F b = new Color4F(0.0F, 0.5F, 1.0F, 1.0F);
		final Color4F c = new Color4F(0.0F, 0.5F, 1.0F, 2.0F);
		final Color4F d = new Color4F(0.0F, 0.5F, 2.0F, 1.0F);
		final Color4F e = new Color4F(0.0F, 2.0F, 1.0F, 1.0F);
		final Color4F f = new Color4F(2.0F, 0.5F, 1.0F, 1.0F);
		final Color4F g = null;
		
		assertTrue(a.equals(a));
		assertTrue(a.equals(b));
		assertTrue(b.equals(a));
		assertFalse(a.equals(c));
		assertFalse(c.equals(a));
		assertFalse(a.equals(d));
		assertFalse(d.equals(a));
		assertFalse(a.equals(e));
		assertFalse(e.equals(a));
		assertFalse(a.equals(f));
		assertFalse(f.equals(a));
		assertFalse(a.equals(g));
	}
	
	@Test
	public void testEqualsObject() {
		final Color4F a = new Color4F(0.0F, 0.5F, 1.0F, 1.0F);
		final Color4F b = new Color4F(0.0F, 0.5F, 1.0F, 1.0F);
		final Color4F c = new Color4F(0.0F, 0.5F, 1.0F, 2.0F);
		final Color4F d = new Color4F(0.0F, 0.5F, 2.0F, 1.0F);
		final Color4F e = new Color4F(0.0F, 2.0F, 1.0F, 1.0F);
		final Color4F f = new Color4F(2.0F, 0.5F, 1.0F, 1.0F);
		final Color4F g = null;
		
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
		assertNotEquals(a, g);
		assertNotEquals(g, a);
	}
	
	@Test
	public void testGetAsByteA() {
		final Color4F a = new Color4F(0.0F, 0.0F, 0.0F, 0.0F);
		final Color4F b = new Color4F(0.0F, 0.0F, 0.0F, 0.5F);
		final Color4F c = new Color4F(0.0F, 0.0F, 0.0F, 1.0F);
		
		assertEquals(   0, a.getAsByteA());
		assertEquals(-128, b.getAsByteA());
		assertEquals(-  1, c.getAsByteA());
		
		assertEquals(  0, a.getAsByteA() & 0xFF);
		assertEquals(128, b.getAsByteA() & 0xFF);
		assertEquals(255, c.getAsByteA() & 0xFF);
	}
	
	@Test
	public void testGetAsByteB() {
		final Color4F a = new Color4F(0.0F, 0.0F, 0.0F, 0.0F);
		final Color4F b = new Color4F(0.0F, 0.0F, 0.5F, 0.0F);
		final Color4F c = new Color4F(0.0F, 0.0F, 1.0F, 0.0F);
		
		assertEquals(   0, a.getAsByteB());
		assertEquals(-128, b.getAsByteB());
		assertEquals(-  1, c.getAsByteB());
		
		assertEquals(  0, a.getAsByteB() & 0xFF);
		assertEquals(128, b.getAsByteB() & 0xFF);
		assertEquals(255, c.getAsByteB() & 0xFF);
	}
	
	@Test
	public void testGetAsByteComponent1() {
		final Color4F a = new Color4F(0.0F, 0.0F, 0.0F, 0.0F);
		final Color4F b = new Color4F(0.5F, 0.0F, 0.0F, 0.0F);
		final Color4F c = new Color4F(1.0F, 0.0F, 0.0F, 0.0F);
		
		assertEquals(   0, a.getAsByteComponent1());
		assertEquals(-128, b.getAsByteComponent1());
		assertEquals(-  1, c.getAsByteComponent1());
		
		assertEquals(  0, a.getAsByteComponent1() & 0xFF);
		assertEquals(128, b.getAsByteComponent1() & 0xFF);
		assertEquals(255, c.getAsByteComponent1() & 0xFF);
	}
	
	@Test
	public void testGetAsByteComponent2() {
		final Color4F a = new Color4F(0.0F, 0.0F, 0.0F, 0.0F);
		final Color4F b = new Color4F(0.0F, 0.5F, 0.0F, 0.0F);
		final Color4F c = new Color4F(0.0F, 1.0F, 0.0F, 0.0F);
		
		assertEquals(   0, a.getAsByteComponent2());
		assertEquals(-128, b.getAsByteComponent2());
		assertEquals(-  1, c.getAsByteComponent2());
		
		assertEquals(  0, a.getAsByteComponent2() & 0xFF);
		assertEquals(128, b.getAsByteComponent2() & 0xFF);
		assertEquals(255, c.getAsByteComponent2() & 0xFF);
	}
	
	@Test
	public void testGetAsByteComponent3() {
		final Color4F a = new Color4F(0.0F, 0.0F, 0.0F, 0.0F);
		final Color4F b = new Color4F(0.0F, 0.0F, 0.5F, 0.0F);
		final Color4F c = new Color4F(0.0F, 0.0F, 1.0F, 0.0F);
		
		assertEquals(   0, a.getAsByteComponent3());
		assertEquals(-128, b.getAsByteComponent3());
		assertEquals(-  1, c.getAsByteComponent3());
		
		assertEquals(  0, a.getAsByteComponent3() & 0xFF);
		assertEquals(128, b.getAsByteComponent3() & 0xFF);
		assertEquals(255, c.getAsByteComponent3() & 0xFF);
	}
	
	@Test
	public void testGetAsByteComponent4() {
		final Color4F a = new Color4F(0.0F, 0.0F, 0.0F, 0.0F);
		final Color4F b = new Color4F(0.0F, 0.0F, 0.0F, 0.5F);
		final Color4F c = new Color4F(0.0F, 0.0F, 0.0F, 1.0F);
		
		assertEquals(   0, a.getAsByteComponent4());
		assertEquals(-128, b.getAsByteComponent4());
		assertEquals(-  1, c.getAsByteComponent4());
		
		assertEquals(  0, a.getAsByteComponent4() & 0xFF);
		assertEquals(128, b.getAsByteComponent4() & 0xFF);
		assertEquals(255, c.getAsByteComponent4() & 0xFF);
	}
	
	@Test
	public void testGetAsByteG() {
		final Color4F a = new Color4F(0.0F, 0.0F, 0.0F, 0.0F);
		final Color4F b = new Color4F(0.0F, 0.5F, 0.0F, 0.0F);
		final Color4F c = new Color4F(0.0F, 1.0F, 0.0F, 0.0F);
		
		assertEquals(   0, a.getAsByteG());
		assertEquals(-128, b.getAsByteG());
		assertEquals(-  1, c.getAsByteG());
		
		assertEquals(  0, a.getAsByteG() & 0xFF);
		assertEquals(128, b.getAsByteG() & 0xFF);
		assertEquals(255, c.getAsByteG() & 0xFF);
	}
	
	@Test
	public void testGetAsByteR() {
		final Color4F a = new Color4F(0.0F, 0.0F, 0.0F, 0.0F);
		final Color4F b = new Color4F(0.5F, 0.0F, 0.0F, 0.0F);
		final Color4F c = new Color4F(1.0F, 0.0F, 0.0F, 0.0F);
		
		assertEquals(   0, a.getAsByteR());
		assertEquals(-128, b.getAsByteR());
		assertEquals(-  1, c.getAsByteR());
		
		assertEquals(  0, a.getAsByteR() & 0xFF);
		assertEquals(128, b.getAsByteR() & 0xFF);
		assertEquals(255, c.getAsByteR() & 0xFF);
	}
	
	@Test
	public void testGetAsByteW() {
		final Color4F a = new Color4F(0.0F, 0.0F, 0.0F, 0.0F);
		final Color4F b = new Color4F(0.0F, 0.0F, 0.0F, 0.5F);
		final Color4F c = new Color4F(0.0F, 0.0F, 0.0F, 1.0F);
		
		assertEquals(   0, a.getAsByteW());
		assertEquals(-128, b.getAsByteW());
		assertEquals(-  1, c.getAsByteW());
		
		assertEquals(  0, a.getAsByteW() & 0xFF);
		assertEquals(128, b.getAsByteW() & 0xFF);
		assertEquals(255, c.getAsByteW() & 0xFF);
	}
	
	@Test
	public void testGetAsByteX() {
		final Color4F a = new Color4F(0.0F, 0.0F, 0.0F, 0.0F);
		final Color4F b = new Color4F(0.5F, 0.0F, 0.0F, 0.0F);
		final Color4F c = new Color4F(1.0F, 0.0F, 0.0F, 0.0F);
		
		assertEquals(   0, a.getAsByteX());
		assertEquals(-128, b.getAsByteX());
		assertEquals(-  1, c.getAsByteX());
		
		assertEquals(  0, a.getAsByteX() & 0xFF);
		assertEquals(128, b.getAsByteX() & 0xFF);
		assertEquals(255, c.getAsByteX() & 0xFF);
	}
	
	@Test
	public void testGetAsByteY() {
		final Color4F a = new Color4F(0.0F, 0.0F, 0.0F, 0.0F);
		final Color4F b = new Color4F(0.0F, 0.5F, 0.0F, 0.0F);
		final Color4F c = new Color4F(0.0F, 1.0F, 0.0F, 0.0F);
		
		assertEquals(   0, a.getAsByteY());
		assertEquals(-128, b.getAsByteY());
		assertEquals(-  1, c.getAsByteY());
		
		assertEquals(  0, a.getAsByteY() & 0xFF);
		assertEquals(128, b.getAsByteY() & 0xFF);
		assertEquals(255, c.getAsByteY() & 0xFF);
	}
	
	@Test
	public void testGetAsByteZ() {
		final Color4F a = new Color4F(0.0F, 0.0F, 0.0F, 0.0F);
		final Color4F b = new Color4F(0.0F, 0.0F, 0.5F, 0.0F);
		final Color4F c = new Color4F(0.0F, 0.0F, 1.0F, 0.0F);
		
		assertEquals(   0, a.getAsByteZ());
		assertEquals(-128, b.getAsByteZ());
		assertEquals(-  1, c.getAsByteZ());
		
		assertEquals(  0, a.getAsByteZ() & 0xFF);
		assertEquals(128, b.getAsByteZ() & 0xFF);
		assertEquals(255, c.getAsByteZ() & 0xFF);
	}
	
	@Test
	public void testHasInfinities() {
		final Color4F a = new Color4F(Float.POSITIVE_INFINITY, 0.0F, 0.0F, 0.0F);
		final Color4F b = new Color4F(0.0F, Float.POSITIVE_INFINITY, 0.0F, 0.0F);
		final Color4F c = new Color4F(0.0F, 0.0F, Float.POSITIVE_INFINITY, 0.0F);
		final Color4F d = new Color4F(0.0F, 0.0F, 0.0F, Float.POSITIVE_INFINITY);
		final Color4F e = new Color4F(0.0F, 0.0F, 0.0F, 0.0F);
		
		assertTrue(a.hasInfinites());
		assertTrue(b.hasInfinites());
		assertTrue(c.hasInfinites());
		assertTrue(d.hasInfinites());
		assertFalse(e.hasInfinites());
	}
	
	@Test
	public void testHasNaNs() {
		final Color4F a = new Color4F(Float.NaN, 0.0F, 0.0F, 0.0F);
		final Color4F b = new Color4F(0.0F, Float.NaN, 0.0F, 0.0F);
		final Color4F c = new Color4F(0.0F, 0.0F, Float.NaN, 0.0F);
		final Color4F d = new Color4F(0.0F, 0.0F, 0.0F, Float.NaN);
		final Color4F e = new Color4F(0.0F, 0.0F, 0.0F, 0.0F);
		
		assertTrue(a.hasNaNs());
		assertTrue(b.hasNaNs());
		assertTrue(c.hasNaNs());
		assertTrue(d.hasNaNs());
		assertFalse(e.hasNaNs());
	}
	
	@Test
	public void testIsBlack() {
		final Color4F a = new Color4F(0.0F, 0.0F, 0.0F, 0.5F);
		final Color4F b = new Color4F(0.0F, 0.0F, 1.0F, 0.5F);
		
		assertTrue(a.isBlack());
		assertFalse(b.isBlack());
	}
	
	@Test
	public void testIsBlue() {
		final Color4F a = new Color4F(0.0F, 0.0F, 1.0F, 0.5F);
		final Color4F b = new Color4F(0.5F, 0.5F, 1.0F, 0.5F);
		
		assertTrue(a.isBlue());
		assertFalse(b.isBlue());
	}
	
	@Test
	public void testIsBlueFloatFloat() {
		final Color4F a = new Color4F(0.0F, 0.0F, 1.0F, 0.5F);
		final Color4F b = new Color4F(0.5F, 0.5F, 1.0F, 0.5F);
		final Color4F c = new Color4F(1.0F, 1.0F, 1.0F, 0.5F);
		
		assertTrue(a.isBlue(0.5F, 0.5F));
		assertTrue(b.isBlue(0.5F, 0.5F));
		assertFalse(c.isBlue(0.5F, 0.5F));
	}
	
	@Test
	public void testIsCyan() {
		final Color4F a = new Color4F(0.0F, 1.0F, 1.0F, 0.5F);
		final Color4F b = new Color4F(0.0F, 0.5F, 0.5F, 0.5F);
		final Color4F c = new Color4F(1.0F, 1.0F, 1.0F, 0.5F);
		
		assertTrue(a.isCyan());
		assertTrue(b.isCyan());
		assertFalse(c.isCyan());
	}
	
	@Test
	public void testIsGrayscale() {
		final Color4F a = new Color4F(0.0F, 0.0F, 0.0F, 1.0F);
		final Color4F b = new Color4F(0.5F, 0.5F, 0.5F, 0.5F);
		final Color4F c = new Color4F(1.0F, 1.0F, 1.0F, 0.0F);
		final Color4F d = new Color4F(0.0F, 0.0F, 0.5F, 0.5F);
		final Color4F e = new Color4F(0.0F, 0.5F, 0.5F, 0.5F);
		final Color4F f = new Color4F(0.0F, 0.5F, 0.0F, 0.5F);
		final Color4F g = new Color4F(0.0F, 0.5F, 1.0F, 0.5F);
		
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
		final Color4F a = new Color4F(0.0F, 1.0F, 0.0F, 0.5F);
		final Color4F b = new Color4F(0.5F, 1.0F, 0.5F, 0.5F);
		
		assertTrue(a.isGreen());
		assertFalse(b.isGreen());
	}
	
	@Test
	public void testIsGreenFloatFloat() {
		final Color4F a = new Color4F(0.0F, 1.0F, 0.0F, 0.5F);
		final Color4F b = new Color4F(0.5F, 1.0F, 0.5F, 0.5F);
		final Color4F c = new Color4F(1.0F, 1.0F, 1.0F, 0.5F);
		
		assertTrue(a.isGreen(0.5F, 0.5F));
		assertTrue(b.isGreen(0.5F, 0.5F));
		assertFalse(c.isGreen(0.5F, 0.5F));
	}
	
	@Test
	public void testIsMagenta() {
		final Color4F a = new Color4F(1.0F, 0.0F, 1.0F, 0.5F);
		final Color4F b = new Color4F(0.5F, 0.0F, 0.5F, 0.5F);
		final Color4F c = new Color4F(1.0F, 1.0F, 1.0F, 0.5F);
		
		assertTrue(a.isMagenta());
		assertTrue(b.isMagenta());
		assertFalse(c.isMagenta());
	}
	
	@Test
	public void testIsRed() {
		final Color4F a = new Color4F(1.0F, 0.0F, 0.0F, 0.5F);
		final Color4F b = new Color4F(1.0F, 0.5F, 0.5F, 0.5F);
		
		assertTrue(a.isRed());
		assertFalse(b.isRed());
	}
	
	@Test
	public void testIsRedFloatFloat() {
		final Color4F a = new Color4F(1.0F, 0.0F, 0.0F, 0.5F);
		final Color4F b = new Color4F(1.0F, 0.5F, 0.5F, 0.5F);
		final Color4F c = new Color4F(1.0F, 1.0F, 1.0F, 0.5F);
		
		assertTrue(a.isRed(0.5F, 0.5F));
		assertTrue(b.isRed(0.5F, 0.5F));
		assertFalse(c.isRed(0.5F, 0.5F));
	}
	
	@Test
	public void testIsWhite() {
		final Color4F a = new Color4F(1.0F, 1.0F, 1.0F, 0.5F);
		final Color4F b = new Color4F(2.0F, 2.0F, 2.0F, 0.5F);
		final Color4F c = new Color4F(1.0F, 1.5F, 2.0F, 0.5F);
		
		assertTrue(a.isWhite());
		assertTrue(b.isWhite());
		assertFalse(c.isWhite());
	}
	
	@Test
	public void testIsYellow() {
		final Color4F a = new Color4F(1.0F, 1.0F, 0.0F, 0.5F);
		final Color4F b = new Color4F(0.5F, 0.5F, 0.0F, 0.5F);
		final Color4F c = new Color4F(1.0F, 1.0F, 1.0F, 0.5F);
		
		assertTrue(a.isYellow());
		assertTrue(b.isYellow());
		assertFalse(c.isYellow());
	}
	
	@Test
	public void testToString() {
		final Color4F color = new Color4F(0.0F, 0.5F, 1.0F, 0.5F);
		
		assertEquals("new Color4F(0.0F, 0.5F, 1.0F, 0.5F)", color.toString());
	}
}