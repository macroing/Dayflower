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
	public void testAddAndMultiplyColor3FColor3FColor3F() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = new Color3F(2.0F, 3.0F, 4.0F);
		final Color3F c = new Color3F(3.0F, 4.0F, 5.0F);
		final Color3F d = Color3F.addAndMultiply(a, b, c);
		
		assertEquals( 7.0F, d.getComponent1());
		assertEquals(14.0F, d.getComponent2());
		assertEquals(23.0F, d.getComponent3());
		assertThrows(NullPointerException.class, () -> Color3F.addAndMultiply(a, b, null));
		assertThrows(NullPointerException.class, () -> Color3F.addAndMultiply(a, null, c));
		assertThrows(NullPointerException.class, () -> Color3F.addAndMultiply(null, b, c));
	}
	
	@Test
	public void testAddAndMultiplyColor3FColor3FColor3FFloat() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = new Color3F(2.0F, 3.0F, 4.0F);
		final Color3F c = new Color3F(3.0F, 4.0F, 5.0F);
		final Color3F d = Color3F.addAndMultiply(a, b, c, 2.0F);
		
		assertEquals(13.0F, d.getComponent1());
		assertEquals(26.0F, d.getComponent2());
		assertEquals(43.0F, d.getComponent3());
		assertThrows(NullPointerException.class, () -> Color3F.addAndMultiply(a, b, null, 2.0F));
		assertThrows(NullPointerException.class, () -> Color3F.addAndMultiply(a, null, c, 2.0F));
		assertThrows(NullPointerException.class, () -> Color3F.addAndMultiply(null, b, c, 2.0F));
	}
	
	@Test
	public void testAddColor3FColor3F() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = new Color3F(2.0F, 3.0F, 4.0F);
		final Color3F c = Color3F.add(a, b);
		
		assertEquals(3.0F, c.getComponent1());
		assertEquals(5.0F, c.getComponent2());
		assertEquals(7.0F, c.getComponent3());
		assertThrows(NullPointerException.class, () -> Color3F.add(a, null));
		assertThrows(NullPointerException.class, () -> Color3F.add(null, b));
	}
	
	@Test
	public void testAddColor3FFloat() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = Color3F.add(a, 2.0F);
		
		assertEquals(3.0F, b.getComponent1());
		assertEquals(4.0F, b.getComponent2());
		assertEquals(5.0F, b.getComponent3());
		assertThrows(NullPointerException.class, () -> Color3F.add(null, 2.0F));
	}
	
	@Test
	public void testAddMultiplyAndDivideColor3FColor3FColor3FColor3FFloatFloat() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = new Color3F(2.0F, 3.0F, 4.0F);
		final Color3F c = new Color3F(3.0F, 4.0F, 5.0F);
		final Color3F d = new Color3F(4.0F, 5.0F, 6.0F);
		final Color3F e = Color3F.addMultiplyAndDivide(a, b, c, d, 2.0F, 2.0F);
		
		assertEquals( 25.0F, e.getComponent1());
		assertEquals( 62.0F, e.getComponent2());
		assertEquals(123.0F, e.getComponent3());
		assertThrows(NullPointerException.class, () -> Color3F.addMultiplyAndDivide(a, b, c, null, 2.0F, 2.0F));
		assertThrows(NullPointerException.class, () -> Color3F.addMultiplyAndDivide(a, b, null, d, 2.0F, 2.0F));
		assertThrows(NullPointerException.class, () -> Color3F.addMultiplyAndDivide(a, null, c, d, 2.0F, 2.0F));
		assertThrows(NullPointerException.class, () -> Color3F.addMultiplyAndDivide(null, b, c, d, 2.0F, 2.0F));
	}
	
	@Test
	public void testAddMultiplyAndDivideColor3FColor3FColor3FFloat() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = new Color3F(2.0F, 3.0F, 4.0F);
		final Color3F c = new Color3F(3.0F, 4.0F, 5.0F);
		final Color3F d = Color3F.addMultiplyAndDivide(a, b, c, 2.0F);
		
		assertEquals( 4.0F, d.getComponent1());
		assertEquals( 8.0F, d.getComponent2());
		assertEquals(13.0F, d.getComponent3());
		assertThrows(NullPointerException.class, () -> Color3F.addMultiplyAndDivide(a, b, null, 2.0F));
		assertThrows(NullPointerException.class, () -> Color3F.addMultiplyAndDivide(a, null, c, 2.0F));
		assertThrows(NullPointerException.class, () -> Color3F.addMultiplyAndDivide(null, b, c, 2.0F));
	}
	
	@Test
	public void testAddMultiplyAndDivideColor3FColor3FColor3FFloatFloat() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = new Color3F(2.0F, 3.0F, 4.0F);
		final Color3F c = new Color3F(3.0F, 4.0F, 5.0F);
		final Color3F d = Color3F.addMultiplyAndDivide(a, b, c, 2.0F, 2.0F);
		
		assertEquals( 7.0F, d.getComponent1());
		assertEquals(14.0F, d.getComponent2());
		assertEquals(23.0F, d.getComponent3());
		assertThrows(NullPointerException.class, () -> Color3F.addMultiplyAndDivide(a, b, null, 2.0F, 2.0F));
		assertThrows(NullPointerException.class, () -> Color3F.addMultiplyAndDivide(a, null, c, 2.0F, 2.0F));
		assertThrows(NullPointerException.class, () -> Color3F.addMultiplyAndDivide(null, b, c, 2.0F, 2.0F));
	}
	
	@Test
	public void testAddSample() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.5F, 0.5F, 0.5F);
		final Color3F c = new Color3F(1.0F, 1.0F, 1.0F);
		final Color3F d = new Color3F(1.5F, 1.5F, 1.5F);
		final Color3F e = Color3F.addSample(a, c, 2);
		final Color3F f = Color3F.addSample(e, b, 3);
		final Color3F g = Color3F.addSample(f, d, 4);
		
		assertEquals(0.5F, e.getComponent1());
		assertEquals(0.5F, e.getComponent2());
		assertEquals(0.5F, e.getComponent3());
		
		assertEquals(0.5F, f.getComponent1());
		assertEquals(0.5F, f.getComponent2());
		assertEquals(0.5F, f.getComponent3());
		
		assertEquals(0.75F, g.getComponent1());
		assertEquals(0.75F, g.getComponent2());
		assertEquals(0.75F, g.getComponent3());
		
		assertThrows(NullPointerException.class, () -> Color3F.addSample(a, null, 1));
		assertThrows(NullPointerException.class, () -> Color3F.addSample(null, b, 1));
	}
	
	@Test
	public void testAverage() {
		final Color3F color = new Color3F(0.0F, 0.5F, 1.0F);
		
		assertEquals(0.5F, color.average());
	}
	
	@Test
	public void testBlendColor3FColor3F() {
		final Color3F a = new Color3F(1.0F, 1.0F, 1.0F);
		final Color3F b = new Color3F(3.0F, 3.0F, 3.0F);
		final Color3F c = Color3F.blend(a, b);
		
		assertEquals(2.0F, c.getComponent1());
		assertEquals(2.0F, c.getComponent2());
		assertEquals(2.0F, c.getComponent3());
		
		assertThrows(NullPointerException.class, () -> Color3F.blend(a, null));
		assertThrows(NullPointerException.class, () -> Color3F.blend(null, b));
	}
	
	@Test
	public void testBlendColor3FColor3FFloat() {
		final Color3F a = new Color3F(1.0F, 1.0F, 1.0F);
		final Color3F b = new Color3F(5.0F, 5.0F, 5.0F);
		final Color3F c = Color3F.blend(a, b, 0.25F);
		
		assertEquals(2.0F, c.getComponent1());
		assertEquals(2.0F, c.getComponent2());
		assertEquals(2.0F, c.getComponent3());
		
		assertThrows(NullPointerException.class, () -> Color3F.blend(a, null, 0.25F));
		assertThrows(NullPointerException.class, () -> Color3F.blend(null, b, 0.25F));
	}
	
	@Test
	public void testBlendColor3FColor3FFloatFloatFloat() {
		final Color3F a = new Color3F(1.0F, 1.0F, 1.0F);
		final Color3F b = new Color3F(2.0F, 2.0F, 2.0F);
		final Color3F c = Color3F.blend(a, b, 0.0F, 0.5F, 1.0F);
		
		assertEquals(1.0F, c.getComponent1());
		assertEquals(1.5F, c.getComponent2());
		assertEquals(2.0F, c.getComponent3());
		
		assertThrows(NullPointerException.class, () -> Color3F.blend(a, null, 0.0F, 0.5F, 1.0F));
		assertThrows(NullPointerException.class, () -> Color3F.blend(null, b, 0.0F, 0.5F, 1.0F));
	}
	
	@Test
	public void testConstants() {
		assertEquals(new Color3F(0.76F, 0.60F, 0.33F), Color3F.AU_AZTEK);
		assertEquals(new Color3F(0.83F, 0.69F, 0.22F), Color3F.AU_METALLIC);
		assertEquals(new Color3F(0.00F, 0.00F, 0.00F), Color3F.BLACK);
		assertEquals(new Color3F(0.00F, 0.00F, 1.00F), Color3F.BLUE);
		assertEquals(new Color3F(0.72F, 0.45F, 0.20F), Color3F.CU);
		assertEquals(new Color3F(0.00F, 1.00F, 1.00F), Color3F.CYAN);
		assertEquals(new Color3F(0.01F, 0.01F, 0.01F), Color3F.GRAY_0_01);
		assertEquals(new Color3F(0.10F, 0.10F, 0.10F), Color3F.GRAY_0_10);
		assertEquals(new Color3F(0.20F, 0.20F, 0.20F), Color3F.GRAY_0_20);
		assertEquals(new Color3F(0.25F, 0.25F, 0.25F), Color3F.GRAY_0_25);
		assertEquals(new Color3F(0.30F, 0.30F, 0.30F), Color3F.GRAY_0_30);
		assertEquals(new Color3F(0.50F, 0.50F, 0.50F), Color3F.GRAY_0_50);
		assertEquals(new Color3F(1.50F, 1.50F, 1.50F), Color3F.GRAY_1_50);
		assertEquals(new Color3F(1.55F, 1.55F, 1.55F), Color3F.GRAY_1_55);
		assertEquals(new Color3F(2.00F, 2.00F, 2.00F), Color3F.GRAY_2_00);
		assertEquals(new Color3F(0.00F, 1.00F, 0.00F), Color3F.GREEN);
		assertEquals(new Color3F(1.00F, 0.00F, 1.00F), Color3F.MAGENTA);
		assertEquals(new Color3F(1.00F, 0.50F, 0.00F), Color3F.ORANGE);
		assertEquals(new Color3F(1.00F, 0.00F, 0.00F), Color3F.RED);
		assertEquals(new Color3F(1.00F, 1.00F, 1.00F), Color3F.WHITE);
		assertEquals(new Color3F(1.00F, 1.00F, 0.00F), Color3F.YELLOW);
	}
	
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
	public void testGetAsByteB() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, 0.0F, 0.5F);
		final Color3F c = new Color3F(0.0F, 0.0F, 1.0F);
		
		assertEquals(   0, a.getAsByteB());
		assertEquals(-128, b.getAsByteB());
		assertEquals(-  1, c.getAsByteB());
		
		assertEquals(  0, a.getAsByteB() & 0xFF);
		assertEquals(128, b.getAsByteB() & 0xFF);
		assertEquals(255, c.getAsByteB() & 0xFF);
	}
	
	@Test
	public void testGetAsByteComponent1() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.5F, 0.0F, 0.0F);
		final Color3F c = new Color3F(1.0F, 0.0F, 0.0F);
		
		assertEquals(   0, a.getAsByteComponent1());
		assertEquals(-128, b.getAsByteComponent1());
		assertEquals(-  1, c.getAsByteComponent1());
		
		assertEquals(  0, a.getAsByteComponent1() & 0xFF);
		assertEquals(128, b.getAsByteComponent1() & 0xFF);
		assertEquals(255, c.getAsByteComponent1() & 0xFF);
	}
	
	@Test
	public void testGetAsByteComponent2() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, 0.5F, 0.0F);
		final Color3F c = new Color3F(0.0F, 1.0F, 0.0F);
		
		assertEquals(   0, a.getAsByteComponent2());
		assertEquals(-128, b.getAsByteComponent2());
		assertEquals(-  1, c.getAsByteComponent2());
		
		assertEquals(  0, a.getAsByteComponent2() & 0xFF);
		assertEquals(128, b.getAsByteComponent2() & 0xFF);
		assertEquals(255, c.getAsByteComponent2() & 0xFF);
	}
	
	@Test
	public void testGetAsByteComponent3() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, 0.0F, 0.5F);
		final Color3F c = new Color3F(0.0F, 0.0F, 1.0F);
		
		assertEquals(   0, a.getAsByteComponent3());
		assertEquals(-128, b.getAsByteComponent3());
		assertEquals(-  1, c.getAsByteComponent3());
		
		assertEquals(  0, a.getAsByteComponent3() & 0xFF);
		assertEquals(128, b.getAsByteComponent3() & 0xFF);
		assertEquals(255, c.getAsByteComponent3() & 0xFF);
	}
	
	@Test
	public void testGetAsByteG() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, 0.5F, 0.0F);
		final Color3F c = new Color3F(0.0F, 1.0F, 0.0F);
		
		assertEquals(   0, a.getAsByteG());
		assertEquals(-128, b.getAsByteG());
		assertEquals(-  1, c.getAsByteG());
		
		assertEquals(  0, a.getAsByteG() & 0xFF);
		assertEquals(128, b.getAsByteG() & 0xFF);
		assertEquals(255, c.getAsByteG() & 0xFF);
	}
	
	@Test
	public void testGetAsByteR() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.5F, 0.0F, 0.0F);
		final Color3F c = new Color3F(1.0F, 0.0F, 0.0F);
		
		assertEquals(   0, a.getAsByteR());
		assertEquals(-128, b.getAsByteR());
		assertEquals(-  1, c.getAsByteR());
		
		assertEquals(  0, a.getAsByteR() & 0xFF);
		assertEquals(128, b.getAsByteR() & 0xFF);
		assertEquals(255, c.getAsByteR() & 0xFF);
	}
	
	@Test
	public void testGetAsByteX() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.5F, 0.0F, 0.0F);
		final Color3F c = new Color3F(1.0F, 0.0F, 0.0F);
		
		assertEquals(   0, a.getAsByteX());
		assertEquals(-128, b.getAsByteX());
		assertEquals(-  1, c.getAsByteX());
		
		assertEquals(  0, a.getAsByteX() & 0xFF);
		assertEquals(128, b.getAsByteX() & 0xFF);
		assertEquals(255, c.getAsByteX() & 0xFF);
	}
	
	@Test
	public void testGetAsByteY() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, 0.5F, 0.0F);
		final Color3F c = new Color3F(0.0F, 1.0F, 0.0F);
		
		assertEquals(   0, a.getAsByteY());
		assertEquals(-128, b.getAsByteY());
		assertEquals(-  1, c.getAsByteY());
		
		assertEquals(  0, a.getAsByteY() & 0xFF);
		assertEquals(128, b.getAsByteY() & 0xFF);
		assertEquals(255, c.getAsByteY() & 0xFF);
	}
	
	@Test
	public void testGetAsByteZ() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, 0.0F, 0.5F);
		final Color3F c = new Color3F(0.0F, 0.0F, 1.0F);
		
		assertEquals(   0, a.getAsByteZ());
		assertEquals(-128, b.getAsByteZ());
		assertEquals(-  1, c.getAsByteZ());
		
		assertEquals(  0, a.getAsByteZ() & 0xFF);
		assertEquals(128, b.getAsByteZ() & 0xFF);
		assertEquals(255, c.getAsByteZ() & 0xFF);
	}
	
	@Test
	public void testGetAsIntB() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, 0.0F, 0.5F);
		final Color3F c = new Color3F(0.0F, 0.0F, 1.0F);
		
		assertEquals(  0, a.getAsIntB());
		assertEquals(128, b.getAsIntB());
		assertEquals(255, c.getAsIntB());
	}
	
	@Test
	public void testGetAsIntComponent1() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.5F, 0.0F, 0.0F);
		final Color3F c = new Color3F(1.0F, 0.0F, 0.0F);
		
		assertEquals(  0, a.getAsIntComponent1());
		assertEquals(128, b.getAsIntComponent1());
		assertEquals(255, c.getAsIntComponent1());
	}
	
	@Test
	public void testGetAsIntComponent2() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, 0.5F, 0.0F);
		final Color3F c = new Color3F(0.0F, 1.0F, 0.0F);
		
		assertEquals(  0, a.getAsIntComponent2());
		assertEquals(128, b.getAsIntComponent2());
		assertEquals(255, c.getAsIntComponent2());
	}
	
	@Test
	public void testGetAsIntComponent3() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, 0.0F, 0.5F);
		final Color3F c = new Color3F(0.0F, 0.0F, 1.0F);
		
		assertEquals(  0, a.getAsIntComponent3());
		assertEquals(128, b.getAsIntComponent3());
		assertEquals(255, c.getAsIntComponent3());
	}
	
	@Test
	public void testGetAsIntG() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, 0.5F, 0.0F);
		final Color3F c = new Color3F(0.0F, 1.0F, 0.0F);
		
		assertEquals(  0, a.getAsIntG());
		assertEquals(128, b.getAsIntG());
		assertEquals(255, c.getAsIntG());
	}
	
	@Test
	public void testGetAsIntR() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.5F, 0.0F, 0.0F);
		final Color3F c = new Color3F(1.0F, 0.0F, 0.0F);
		
		assertEquals(  0, a.getAsIntR());
		assertEquals(128, b.getAsIntR());
		assertEquals(255, c.getAsIntR());
	}
	
	@Test
	public void testGetAsIntX() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.5F, 0.0F, 0.0F);
		final Color3F c = new Color3F(1.0F, 0.0F, 0.0F);
		
		assertEquals(  0, a.getAsIntX());
		assertEquals(128, b.getAsIntX());
		assertEquals(255, c.getAsIntX());
	}
	
	@Test
	public void testGetAsIntY() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, 0.5F, 0.0F);
		final Color3F c = new Color3F(0.0F, 1.0F, 0.0F);
		
		assertEquals(  0, a.getAsIntY());
		assertEquals(128, b.getAsIntY());
		assertEquals(255, c.getAsIntY());
	}
	
	@Test
	public void testGetAsIntZ() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, 0.0F, 0.5F);
		final Color3F c = new Color3F(0.0F, 0.0F, 1.0F);
		
		assertEquals(  0, a.getAsIntZ());
		assertEquals(128, b.getAsIntZ());
		assertEquals(255, c.getAsIntZ());
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
	public void testGrayscaleAverage() {
		final Color3F a = new Color3F(0.0F, 0.5F, 1.0F);
		final Color3F b = Color3F.grayscaleAverage(a);
		
		assertEquals(0.5F, b.getComponent1());
		assertEquals(0.5F, b.getComponent2());
		assertEquals(0.5F, b.getComponent3());
		assertThrows(NullPointerException.class, () -> Color3F.grayscaleAverage(null));
	}
	
	@Test
	public void testGrayscaleComponent1() {
		final Color3F a = new Color3F(0.0F, 0.5F, 1.0F);
		final Color3F b = Color3F.grayscaleComponent1(a);
		
		assertEquals(0.0F, b.getComponent1());
		assertEquals(0.0F, b.getComponent2());
		assertEquals(0.0F, b.getComponent3());
		assertThrows(NullPointerException.class, () -> Color3F.grayscaleComponent1(null));
	}
	
	@Test
	public void testGrayscaleComponent2() {
		final Color3F a = new Color3F(0.0F, 0.5F, 1.0F);
		final Color3F b = Color3F.grayscaleComponent2(a);
		
		assertEquals(0.5F, b.getComponent1());
		assertEquals(0.5F, b.getComponent2());
		assertEquals(0.5F, b.getComponent3());
		assertThrows(NullPointerException.class, () -> Color3F.grayscaleComponent2(null));
	}
	
	@Test
	public void testGrayscaleComponent3() {
		final Color3F a = new Color3F(0.0F, 0.5F, 1.0F);
		final Color3F b = Color3F.grayscaleComponent3(a);
		
		assertEquals(1.0F, b.getComponent1());
		assertEquals(1.0F, b.getComponent2());
		assertEquals(1.0F, b.getComponent3());
		assertThrows(NullPointerException.class, () -> Color3F.grayscaleComponent3(null));
	}
	
	@Test
	public void testGrayscaleLightness() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = Color3F.grayscaleLightness(a);
		
		assertEquals(2.0F, b.getComponent1());
		assertEquals(2.0F, b.getComponent2());
		assertEquals(2.0F, b.getComponent3());
		assertThrows(NullPointerException.class, () -> Color3F.grayscaleLightness(null));
	}
	
	@Test
	public void testGrayscaleLuminance() {
		final Color3F a = new Color3F(1.0F / 0.212671F, 1.0F / 0.715160F, 1.0F / 0.072169F);
		final Color3F b = Color3F.grayscaleLuminance(a);
		
		assertEquals(3.0F, b.getComponent1());
		assertEquals(3.0F, b.getComponent2());
		assertEquals(3.0F, b.getComponent3());
		assertThrows(NullPointerException.class, () -> Color3F.grayscaleLuminance(null));
	}
	
	@Test
	public void testGrayscaleMaximum() {
		final Color3F a = new Color3F(0.0F, 1.0F, 2.0F);
		final Color3F b = Color3F.grayscaleMaximum(a);
		
		assertEquals(2.0F, b.getComponent1());
		assertEquals(2.0F, b.getComponent2());
		assertEquals(2.0F, b.getComponent3());
		assertThrows(NullPointerException.class, () -> Color3F.grayscaleMaximum(null));
	}
	
	@Test
	public void testGrayscaleMinimum() {
		final Color3F a = new Color3F(0.0F, 1.0F, 2.0F);
		final Color3F b = Color3F.grayscaleMinimum(a);
		
		assertEquals(0.0F, b.getComponent1());
		assertEquals(0.0F, b.getComponent2());
		assertEquals(0.0F, b.getComponent3());
		assertThrows(NullPointerException.class, () -> Color3F.grayscaleMinimum(null));
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
	public void testInvert() {
		final Color3F a = new Color3F(0.75F, 0.75F, 0.75F);
		final Color3F b = Color3F.invert(a);
		
		assertEquals(0.25F, b.getComponent1());
		assertEquals(0.25F, b.getComponent2());
		assertEquals(0.25F, b.getComponent3());
		assertThrows(NullPointerException.class, () -> Color3F.invert(null));
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
	public void testLightness() {
		final Color3F color = new Color3F(1.0F, 2.0F, 3.0F);
		
		assertEquals(2.0F, color.lightness());
	}
	
	@Test
	public void testLuminance() {
		final Color3F color = new Color3F(1.0F / 0.212671F, 1.0F / 0.715160F, 1.0F / 0.072169F);
		
		assertEquals(3.0F, color.luminance());
	}
	
	@Test
	public void testMaximum() {
		final Color3F color = new Color3F(0.0F, 1.0F, 2.0F);
		
		assertEquals(2.0F, color.maximum());
	}
	
	@Test
	public void testMaximumColor3FColor3F() {
		final Color3F a = new Color3F(0.0F, 1.0F, 2.0F);
		final Color3F b = new Color3F(1.0F, 0.0F, 2.0F);
		final Color3F c = Color3F.maximum(a, b);
		
		assertEquals(1.0F, c.getComponent1());
		assertEquals(1.0F, c.getComponent2());
		assertEquals(2.0F, c.getComponent3());
		assertThrows(NullPointerException.class, () -> Color3F.maximum(a, null));
		assertThrows(NullPointerException.class, () -> Color3F.maximum(null, b));
	}
	
	@Test
	public void testMaximumTo1() {
		final Color3F a = new Color3F(1.0F, 1.0F, 2.0F);
		final Color3F b = Color3F.maximumTo1(a);
		
		assertEquals(0.5F, b.getComponent1());
		assertEquals(0.5F, b.getComponent2());
		assertEquals(1.0F, b.getComponent3());
		assertThrows(NullPointerException.class, () -> Color3F.maximumTo1(null));
	}
	
	@Test
	public void testMinimum() {
		final Color3F color = new Color3F(0.0F, 1.0F, 2.0F);
		
		assertEquals(0.0F, color.minimum());
	}
	
	@Test
	public void testMinimumColor3FColor3F() {
		final Color3F a = new Color3F(0.0F, 1.0F, 2.0F);
		final Color3F b = new Color3F(1.0F, 0.0F, 2.0F);
		final Color3F c = Color3F.minimum(a, b);
		
		assertEquals(0.0F, c.getComponent1());
		assertEquals(0.0F, c.getComponent2());
		assertEquals(2.0F, c.getComponent3());
		assertThrows(NullPointerException.class, () -> Color3F.minimum(a, null));
		assertThrows(NullPointerException.class, () -> Color3F.minimum(null, b));
	}
	
	@Test
	public void testMinimumTo0() {
		final Color3F a = new Color3F(-1.0F, 0.0F, 1.0F);
		final Color3F b = Color3F.minimumTo0(a);
		
		assertEquals(0.0F, b.getComponent1());
		assertEquals(1.0F, b.getComponent2());
		assertEquals(2.0F, b.getComponent3());
		assertThrows(NullPointerException.class, () -> Color3F.minimumTo0(null));
	}
	
	@Test
	public void testToString() {
		final Color3F color = new Color3F(0.0F, 0.5F, 1.0F);
		
		assertEquals("new Color3F(0.0F, 0.5F, 1.0F)", color.toString());
	}
}