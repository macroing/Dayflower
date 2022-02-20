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
public final class Color4DUnitTests {
	public Color4DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstants() {
		assertEquals(new Color4D(0.0D, 0.0D, 0.0D, 1.0D), Color4D.BLACK);
		assertEquals(new Color4D(0.0D, 0.0D, 1.0D, 1.0D), Color4D.BLUE);
		assertEquals(new Color4D(0.0D, 1.0D, 1.0D, 1.0D), Color4D.CYAN);
		assertEquals(new Color4D(0.0D, 1.0D, 0.0D, 1.0D), Color4D.GREEN);
		assertEquals(new Color4D(1.0D, 0.0D, 1.0D, 1.0D), Color4D.MAGENTA);
		assertEquals(new Color4D(1.0D, 0.5D, 0.0D, 1.0D), Color4D.ORANGE);
		assertEquals(new Color4D(1.0D, 0.0D, 0.0D, 1.0D), Color4D.RED);
		assertEquals(new Color4D(0.0D, 0.0D, 0.0D, 0.0D), Color4D.TRANSPARENT);
		assertEquals(new Color4D(1.0D, 1.0D, 1.0D, 1.0D), Color4D.WHITE);
		assertEquals(new Color4D(1.0D, 1.0D, 0.0D, 1.0D), Color4D.YELLOW);
	}
	
	@Test
	public void testConstructor() {
		final Color4D color = new Color4D();
		
		assertEquals(0.0D, color.getComponent1());
		assertEquals(0.0D, color.getComponent2());
		assertEquals(0.0D, color.getComponent3());
		assertEquals(1.0D, color.getComponent4());
	}
	
	@Test
	public void testConstructorColor3D() {
		final Color4D color = new Color4D(new Color3D(1.0D, 1.0D, 1.0D));
		
		assertEquals(1.0D, color.getComponent1());
		assertEquals(1.0D, color.getComponent2());
		assertEquals(1.0D, color.getComponent3());
		assertEquals(1.0D, color.getComponent4());
		
		assertThrows(NullPointerException.class, () -> new Color4D((Color3D)(null)));
	}
	
	@Test
	public void testConstructorColor3DDouble() {
		final Color4D color = new Color4D(new Color3D(1.0D, 1.0D, 1.0D), 0.5D);
		
		assertEquals(1.0D, color.getComponent1());
		assertEquals(1.0D, color.getComponent2());
		assertEquals(1.0D, color.getComponent3());
		assertEquals(0.5D, color.getComponent4());
		
		assertThrows(NullPointerException.class, () -> new Color4D((Color3D)(null), 0.5D));
	}
	
	@Test
	public void testConstructorColor3F() {
		final Color4D color = new Color4D(new Color3F(1.0F, 1.0F, 1.0F));
		
		assertEquals(1.0D, color.getComponent1());
		assertEquals(1.0D, color.getComponent2());
		assertEquals(1.0D, color.getComponent3());
		assertEquals(1.0D, color.getComponent4());
		
		assertThrows(NullPointerException.class, () -> new Color4D((Color3F)(null)));
	}
	
	@Test
	public void testConstructorColor3FDouble() {
		final Color4D color = new Color4D(new Color3F(1.0F, 1.0F, 1.0F), 0.5D);
		
		assertEquals(1.0D, color.getComponent1());
		assertEquals(1.0D, color.getComponent2());
		assertEquals(1.0D, color.getComponent3());
		assertEquals(0.5D, color.getComponent4());
		
		assertThrows(NullPointerException.class, () -> new Color4D((Color3F)(null), 0.5D));
	}
	
	@Test
	public void testConstructorDouble() {
		final Color4D color = new Color4D(2.0D);
		
		assertEquals(2.0D, color.getComponent1());
		assertEquals(2.0D, color.getComponent2());
		assertEquals(2.0D, color.getComponent3());
		assertEquals(1.0D, color.getComponent4());
	}
	
	@Test
	public void testConstructorDoubleDouble() {
		final Color4D color = new Color4D(2.0D, 0.5D);
		
		assertEquals(2.0D, color.getComponent1());
		assertEquals(2.0D, color.getComponent2());
		assertEquals(2.0D, color.getComponent3());
		assertEquals(0.5D, color.getComponent4());
	}
	
	@Test
	public void testConstructorDoubleDoubleDouble() {
		final Color4D color = new Color4D(0.0D, 1.0D, 2.0D);
		
		assertEquals(0.0D, color.getComponent1());
		assertEquals(1.0D, color.getComponent2());
		assertEquals(2.0D, color.getComponent3());
		assertEquals(1.0D, color.getComponent4());
	}
	
	@Test
	public void testConstructorDoubleDoubleDoubleDouble() {
		final Color4D color = new Color4D(0.0D, 1.0D, 2.0D, 0.5D);
		
		assertEquals(0.0D, color.getComponent1());
		assertEquals(1.0D, color.getComponent2());
		assertEquals(2.0D, color.getComponent3());
		assertEquals(0.5D, color.getComponent4());
	}
	
	@Test
	public void testConstructorInt() {
		final Color4D color = new Color4D(255);
		
		assertEquals(1.0D, color.getComponent1());
		assertEquals(1.0D, color.getComponent2());
		assertEquals(1.0D, color.getComponent3());
		assertEquals(1.0D, color.getComponent4());
	}
	
	@Test
	public void testConstructorIntIntInt() {
		final Color4D color = new Color4D(0, 255, 300);
		
		assertEquals(0.0D, color.getComponent1());
		assertEquals(1.0D, color.getComponent2());
		assertEquals(1.0D, color.getComponent3());
		assertEquals(1.0D, color.getComponent4());
	}
	
	@Test
	public void testConstructorIntIntIntInt() {
		final Color4D color = new Color4D(0, 255, 300, 255);
		
		assertEquals(0.0D, color.getComponent1());
		assertEquals(1.0D, color.getComponent2());
		assertEquals(1.0D, color.getComponent3());
		assertEquals(1.0D, color.getComponent4());
	}
	
	@Test
	public void testEqualsColor4D() {
		final Color4D a = new Color4D(0.0D, 0.5D, 1.0D, 1.0D);
		final Color4D b = new Color4D(0.0D, 0.5D, 1.0D, 1.0D);
		final Color4D c = new Color4D(0.0D, 0.5D, 1.0D, 2.0D);
		final Color4D d = new Color4D(0.0D, 0.5D, 2.0D, 1.0D);
		final Color4D e = new Color4D(0.0D, 2.0D, 1.0D, 1.0D);
		final Color4D f = new Color4D(2.0D, 0.5D, 1.0D, 1.0D);
		final Color4D g = null;
		
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
		final Color4D a = new Color4D(0.0D, 0.5D, 1.0D, 1.0D);
		final Color4D b = new Color4D(0.0D, 0.5D, 1.0D, 1.0D);
		final Color4D c = new Color4D(0.0D, 0.5D, 1.0D, 2.0D);
		final Color4D d = new Color4D(0.0D, 0.5D, 2.0D, 1.0D);
		final Color4D e = new Color4D(0.0D, 2.0D, 1.0D, 1.0D);
		final Color4D f = new Color4D(2.0D, 0.5D, 1.0D, 1.0D);
		final Color4D g = null;
		
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
	public void testToString() {
		final Color4D color = new Color4D(0.0D, 0.5D, 1.0D, 0.5D);
		
		assertEquals("new Color4D(0.0D, 0.5D, 1.0D, 0.5D)", color.toString());
	}
}