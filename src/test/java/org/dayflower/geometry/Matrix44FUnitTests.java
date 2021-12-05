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
package org.dayflower.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.dayflower.mock.DataOutputMock;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class Matrix44FUnitTests {
	public Matrix44FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstants() {
		assertEquals( 0, Matrix44F.ARRAY_OFFSET_ELEMENT_1_1);
		assertEquals( 1, Matrix44F.ARRAY_OFFSET_ELEMENT_1_2);
		assertEquals( 2, Matrix44F.ARRAY_OFFSET_ELEMENT_1_3);
		assertEquals( 3, Matrix44F.ARRAY_OFFSET_ELEMENT_1_4);
		assertEquals( 4, Matrix44F.ARRAY_OFFSET_ELEMENT_2_1);
		assertEquals( 5, Matrix44F.ARRAY_OFFSET_ELEMENT_2_2);
		assertEquals( 6, Matrix44F.ARRAY_OFFSET_ELEMENT_2_3);
		assertEquals( 7, Matrix44F.ARRAY_OFFSET_ELEMENT_2_4);
		assertEquals( 8, Matrix44F.ARRAY_OFFSET_ELEMENT_3_1);
		assertEquals( 9, Matrix44F.ARRAY_OFFSET_ELEMENT_3_2);
		assertEquals(10, Matrix44F.ARRAY_OFFSET_ELEMENT_3_3);
		assertEquals(11, Matrix44F.ARRAY_OFFSET_ELEMENT_3_4);
		assertEquals(12, Matrix44F.ARRAY_OFFSET_ELEMENT_4_1);
		assertEquals(13, Matrix44F.ARRAY_OFFSET_ELEMENT_4_2);
		assertEquals(14, Matrix44F.ARRAY_OFFSET_ELEMENT_4_3);
		assertEquals(15, Matrix44F.ARRAY_OFFSET_ELEMENT_4_4);
		assertEquals(16, Matrix44F.ARRAY_SIZE);
	}
	
	@Test
	public void testConstructor() {
		final Matrix44F matrix = new Matrix44F();
		
		assertEquals(1.0F, matrix.getElement11());
		assertEquals(0.0F, matrix.getElement12());
		assertEquals(0.0F, matrix.getElement13());
		assertEquals(0.0F, matrix.getElement14());
		assertEquals(0.0F, matrix.getElement21());
		assertEquals(1.0F, matrix.getElement22());
		assertEquals(0.0F, matrix.getElement23());
		assertEquals(0.0F, matrix.getElement24());
		assertEquals(0.0F, matrix.getElement31());
		assertEquals(0.0F, matrix.getElement32());
		assertEquals(1.0F, matrix.getElement33());
		assertEquals(0.0F, matrix.getElement34());
		assertEquals(0.0F, matrix.getElement41());
		assertEquals(0.0F, matrix.getElement42());
		assertEquals(0.0F, matrix.getElement43());
		assertEquals(1.0F, matrix.getElement44());
	}
	
	@Test
	public void testConstructorFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
		final Matrix44F matrix = new Matrix44F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F, 16.0F);
		
		assertEquals( 1.0F, matrix.getElement11());
		assertEquals( 2.0F, matrix.getElement12());
		assertEquals( 3.0F, matrix.getElement13());
		assertEquals( 4.0F, matrix.getElement14());
		assertEquals( 5.0F, matrix.getElement21());
		assertEquals( 6.0F, matrix.getElement22());
		assertEquals( 7.0F, matrix.getElement23());
		assertEquals( 8.0F, matrix.getElement24());
		assertEquals( 9.0F, matrix.getElement31());
		assertEquals(10.0F, matrix.getElement32());
		assertEquals(11.0F, matrix.getElement33());
		assertEquals(12.0F, matrix.getElement34());
		assertEquals(13.0F, matrix.getElement41());
		assertEquals(14.0F, matrix.getElement42());
		assertEquals(15.0F, matrix.getElement43());
		assertEquals(16.0F, matrix.getElement44());
	}
	
	@Test
	public void testEquals() {
		final Matrix44F a = new Matrix44F( 0.0F,  1.0F,  2.0F,  3.0F,  4.0F,  5.0F,  6.0F,  7.0F,  8.0F,  9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F);
		final Matrix44F b = new Matrix44F( 0.0F,  1.0F,  2.0F,  3.0F,  4.0F,  5.0F,  6.0F,  7.0F,  8.0F,  9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F);
		final Matrix44F c = new Matrix44F( 0.0F,  1.0F,  2.0F,  3.0F,  4.0F,  5.0F,  6.0F,  7.0F,  8.0F,  9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 16.0F);
		final Matrix44F d = new Matrix44F( 0.0F,  1.0F,  2.0F,  3.0F,  4.0F,  5.0F,  6.0F,  7.0F,  8.0F,  9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 16.0F, 15.0F);
		final Matrix44F e = new Matrix44F( 0.0F,  1.0F,  2.0F,  3.0F,  4.0F,  5.0F,  6.0F,  7.0F,  8.0F,  9.0F, 10.0F, 11.0F, 12.0F, 16.0F, 14.0F, 15.0F);
		final Matrix44F f = new Matrix44F( 0.0F,  1.0F,  2.0F,  3.0F,  4.0F,  5.0F,  6.0F,  7.0F,  8.0F,  9.0F, 10.0F, 11.0F, 16.0F, 13.0F, 14.0F, 15.0F);
		final Matrix44F g = new Matrix44F( 0.0F,  1.0F,  2.0F,  3.0F,  4.0F,  5.0F,  6.0F,  7.0F,  8.0F,  9.0F, 10.0F, 16.0F, 12.0F, 13.0F, 14.0F, 15.0F);
		final Matrix44F h = new Matrix44F( 0.0F,  1.0F,  2.0F,  3.0F,  4.0F,  5.0F,  6.0F,  7.0F,  8.0F,  9.0F, 16.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F);
		final Matrix44F i = new Matrix44F( 0.0F,  1.0F,  2.0F,  3.0F,  4.0F,  5.0F,  6.0F,  7.0F,  8.0F, 16.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F);
		final Matrix44F j = new Matrix44F( 0.0F,  1.0F,  2.0F,  3.0F,  4.0F,  5.0F,  6.0F,  7.0F, 16.0F,  9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F);
		final Matrix44F k = new Matrix44F( 0.0F,  1.0F,  2.0F,  3.0F,  4.0F,  5.0F,  6.0F, 16.0F,  8.0F,  9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F);
		final Matrix44F l = new Matrix44F( 0.0F,  1.0F,  2.0F,  3.0F,  4.0F,  5.0F, 16.0F,  7.0F,  8.0F,  9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F);
		final Matrix44F m = new Matrix44F( 0.0F,  1.0F,  2.0F,  3.0F,  4.0F, 16.0F,  6.0F,  7.0F,  8.0F,  9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F);
		final Matrix44F n = new Matrix44F( 0.0F,  1.0F,  2.0F,  3.0F, 16.0F,  5.0F,  6.0F,  7.0F,  8.0F,  9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F);
		final Matrix44F o = new Matrix44F( 0.0F,  1.0F,  2.0F, 16.0F,  4.0F,  5.0F,  6.0F,  7.0F,  8.0F,  9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F);
		final Matrix44F p = new Matrix44F( 0.0F,  1.0F, 16.0F,  3.0F,  4.0F,  5.0F,  6.0F,  7.0F,  8.0F,  9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F);
		final Matrix44F q = new Matrix44F( 0.0F, 16.0F,  2.0F,  3.0F,  4.0F,  5.0F,  6.0F,  7.0F,  8.0F,  9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F);
		final Matrix44F r = new Matrix44F(16.0F,  1.0F,  2.0F,  3.0F,  4.0F,  5.0F,  6.0F,  7.0F,  8.0F,  9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F);
		final Matrix44F s = null;
		
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
		assertNotEquals(a, h);
		assertNotEquals(h, a);
		assertNotEquals(a, i);
		assertNotEquals(i, a);
		assertNotEquals(a, j);
		assertNotEquals(j, a);
		assertNotEquals(a, k);
		assertNotEquals(k, a);
		assertNotEquals(a, l);
		assertNotEquals(l, a);
		assertNotEquals(a, m);
		assertNotEquals(m, a);
		assertNotEquals(a, n);
		assertNotEquals(n, a);
		assertNotEquals(a, o);
		assertNotEquals(o, a);
		assertNotEquals(a, p);
		assertNotEquals(p, a);
		assertNotEquals(a, q);
		assertNotEquals(q, a);
		assertNotEquals(a, r);
		assertNotEquals(r, a);
		assertNotEquals(a, s);
		assertNotEquals(s, a);
	}
	
	@Test
	public void testGetElementInt() {
		final Matrix44F matrix = new Matrix44F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F, 16.0F);
		
		assertEquals( 1.0F, matrix.getElement( 0));
		assertEquals( 2.0F, matrix.getElement( 1));
		assertEquals( 3.0F, matrix.getElement( 2));
		assertEquals( 4.0F, matrix.getElement( 3));
		assertEquals( 5.0F, matrix.getElement( 4));
		assertEquals( 6.0F, matrix.getElement( 5));
		assertEquals( 7.0F, matrix.getElement( 6));
		assertEquals( 8.0F, matrix.getElement( 7));
		assertEquals( 9.0F, matrix.getElement( 8));
		assertEquals(10.0F, matrix.getElement( 9));
		assertEquals(11.0F, matrix.getElement(10));
		assertEquals(12.0F, matrix.getElement(11));
		assertEquals(13.0F, matrix.getElement(12));
		assertEquals(14.0F, matrix.getElement(13));
		assertEquals(15.0F, matrix.getElement(14));
		assertEquals(16.0F, matrix.getElement(15));
		
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(- 1));
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(+16));
	}
	
	@Test
	public void testGetElementIntInt() {
		final Matrix44F matrix = new Matrix44F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F, 16.0F);
		
		assertEquals( 1.0F, matrix.getElement(1, 1));
		assertEquals( 2.0F, matrix.getElement(1, 2));
		assertEquals( 3.0F, matrix.getElement(1, 3));
		assertEquals( 4.0F, matrix.getElement(1, 4));
		assertEquals( 5.0F, matrix.getElement(2, 1));
		assertEquals( 6.0F, matrix.getElement(2, 2));
		assertEquals( 7.0F, matrix.getElement(2, 3));
		assertEquals( 8.0F, matrix.getElement(2, 4));
		assertEquals( 9.0F, matrix.getElement(3, 1));
		assertEquals(10.0F, matrix.getElement(3, 2));
		assertEquals(11.0F, matrix.getElement(3, 3));
		assertEquals(12.0F, matrix.getElement(3, 4));
		assertEquals(13.0F, matrix.getElement(4, 1));
		assertEquals(14.0F, matrix.getElement(4, 2));
		assertEquals(15.0F, matrix.getElement(4, 3));
		assertEquals(16.0F, matrix.getElement(4, 4));
		
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(0, 1));
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(1, 0));
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(5, 1));
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(1, 5));
	}
	
	@Test
	public void testGetElement11() {
		final Matrix44F matrix = new Matrix44F(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		
		assertEquals(1.0F, matrix.getElement11());
	}
	
	@Test
	public void testGetElement12() {
		final Matrix44F matrix = new Matrix44F(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		
		assertEquals(1.0F, matrix.getElement12());
	}
	
	@Test
	public void testGetElement13() {
		final Matrix44F matrix = new Matrix44F(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		
		assertEquals(1.0F, matrix.getElement13());
	}
	
	@Test
	public void testGetElement14() {
		final Matrix44F matrix = new Matrix44F(0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		
		assertEquals(1.0F, matrix.getElement14());
	}
	
	@Test
	public void testGetElement21() {
		final Matrix44F matrix = new Matrix44F(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		
		assertEquals(1.0F, matrix.getElement21());
	}
	
	@Test
	public void testGetElement22() {
		final Matrix44F matrix = new Matrix44F(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		
		assertEquals(1.0F, matrix.getElement22());
	}
	
	@Test
	public void testGetElement23() {
		final Matrix44F matrix = new Matrix44F(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		
		assertEquals(1.0F, matrix.getElement23());
	}
	
	@Test
	public void testGetElement24() {
		final Matrix44F matrix = new Matrix44F(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		
		assertEquals(1.0F, matrix.getElement24());
	}
	
	@Test
	public void testGetElement31() {
		final Matrix44F matrix = new Matrix44F(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		
		assertEquals(1.0F, matrix.getElement31());
	}
	
	@Test
	public void testGetElement32() {
		final Matrix44F matrix = new Matrix44F(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		
		assertEquals(1.0F, matrix.getElement32());
	}
	
	@Test
	public void testGetElement33() {
		final Matrix44F matrix = new Matrix44F(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		
		assertEquals(1.0F, matrix.getElement33());
	}
	
	@Test
	public void testGetElement34() {
		final Matrix44F matrix = new Matrix44F(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		
		assertEquals(1.0F, matrix.getElement34());
	}
	
	@Test
	public void testGetElement41() {
		final Matrix44F matrix = new Matrix44F(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F);
		
		assertEquals(1.0F, matrix.getElement41());
	}
	
	@Test
	public void testGetElement42() {
		final Matrix44F matrix = new Matrix44F(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F);
		
		assertEquals(1.0F, matrix.getElement42());
	}
	
	@Test
	public void testGetElement43() {
		final Matrix44F matrix = new Matrix44F(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F);
		
		assertEquals(1.0F, matrix.getElement43());
	}
	
	@Test
	public void testGetElement44() {
		final Matrix44F matrix = new Matrix44F(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
		
		assertEquals(1.0F, matrix.getElement44());
	}
	
	@Test
	public void testHashCode() {
		final Matrix44F a = new Matrix44F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F, 16.0F);
		final Matrix44F b = new Matrix44F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F, 16.0F);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIdentity() {
		final Matrix44F matrix = Matrix44F.identity();
		
		assertEquals(1.0F, matrix.getElement11());
		assertEquals(0.0F, matrix.getElement12());
		assertEquals(0.0F, matrix.getElement13());
		assertEquals(0.0F, matrix.getElement14());
		assertEquals(0.0F, matrix.getElement21());
		assertEquals(1.0F, matrix.getElement22());
		assertEquals(0.0F, matrix.getElement23());
		assertEquals(0.0F, matrix.getElement24());
		assertEquals(0.0F, matrix.getElement31());
		assertEquals(0.0F, matrix.getElement32());
		assertEquals(1.0F, matrix.getElement33());
		assertEquals(0.0F, matrix.getElement34());
		assertEquals(0.0F, matrix.getElement41());
		assertEquals(0.0F, matrix.getElement42());
		assertEquals(0.0F, matrix.getElement43());
		assertEquals(1.0F, matrix.getElement44());
	}
	
	@Test
	public void testInverse() {
		final Matrix44F a = new Matrix44F(2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0F);
		final Matrix44F b = Matrix44F.inverse(a);
		final Matrix44F c = Matrix44F.inverse(b);
		final Matrix44F d = Matrix44F.multiply(a, b);
		final Matrix44F e = Matrix44F.identity();
		
		assertEquals(a, c);
		assertEquals(d, e);
		
		assertThrows(IllegalArgumentException.class, () -> Matrix44F.inverse(new Matrix44F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F, 16.0F)));
		assertThrows(NullPointerException.class, () -> Matrix44F.inverse(null));
	}
	
	@Test
	public void testIsInvertible() {
		final Matrix44F a = new Matrix44F(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F,  0.0F,  1.0F,  0.0F,  0.0F,  0.0F,  0.0F,  1.0F);
		final Matrix44F b = new Matrix44F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F, 16.0F);
		
		assertTrue(a.isInvertible());
		assertFalse(b.isInvertible());
	}
	
	@Test
	public void testRead() throws IOException {
		final Matrix44F a = new Matrix44F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F, 16.0F);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeFloat(a.getElement11());
		dataOutput.writeFloat(a.getElement12());
		dataOutput.writeFloat(a.getElement13());
		dataOutput.writeFloat(a.getElement14());
		dataOutput.writeFloat(a.getElement21());
		dataOutput.writeFloat(a.getElement22());
		dataOutput.writeFloat(a.getElement23());
		dataOutput.writeFloat(a.getElement24());
		dataOutput.writeFloat(a.getElement31());
		dataOutput.writeFloat(a.getElement32());
		dataOutput.writeFloat(a.getElement33());
		dataOutput.writeFloat(a.getElement34());
		dataOutput.writeFloat(a.getElement41());
		dataOutput.writeFloat(a.getElement42());
		dataOutput.writeFloat(a.getElement43());
		dataOutput.writeFloat(a.getElement44());
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Matrix44F b = Matrix44F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Matrix44F.read(null));
		assertThrows(UncheckedIOException.class, () -> Matrix44F.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testRotateVector3FVector3F() {
		final Matrix44F matrix = Matrix44F.rotate(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F));
		
		assertEquals(1.0F, matrix.getElement11());
		assertEquals(0.0F, matrix.getElement12());
		assertEquals(0.0F, matrix.getElement13());
		assertEquals(0.0F, matrix.getElement14());
		assertEquals(0.0F, matrix.getElement21());
		assertEquals(1.0F, matrix.getElement22());
		assertEquals(0.0F, matrix.getElement23());
		assertEquals(0.0F, matrix.getElement24());
		assertEquals(0.0F, matrix.getElement31());
		assertEquals(0.0F, matrix.getElement32());
		assertEquals(1.0F, matrix.getElement33());
		assertEquals(0.0F, matrix.getElement34());
		assertEquals(0.0F, matrix.getElement41());
		assertEquals(0.0F, matrix.getElement42());
		assertEquals(0.0F, matrix.getElement43());
		assertEquals(1.0F, matrix.getElement44());
		
		assertThrows(NullPointerException.class, () -> Matrix44F.rotate(new Vector3F(0.0F, 0.0F, 1.0F), null));
		assertThrows(NullPointerException.class, () -> Matrix44F.rotate((Vector3F)(null), new Vector3F(0.0F, 1.0F, 0.0F)));
	}
	
	@Test
	public void testRotateVector3FVector3FVector3F() {
		final Matrix44F matrix = Matrix44F.rotate(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F));
		
		assertEquals(1.0F, matrix.getElement11());
		assertEquals(0.0F, matrix.getElement12());
		assertEquals(0.0F, matrix.getElement13());
		assertEquals(0.0F, matrix.getElement14());
		assertEquals(0.0F, matrix.getElement21());
		assertEquals(1.0F, matrix.getElement22());
		assertEquals(0.0F, matrix.getElement23());
		assertEquals(0.0F, matrix.getElement24());
		assertEquals(0.0F, matrix.getElement31());
		assertEquals(0.0F, matrix.getElement32());
		assertEquals(1.0F, matrix.getElement33());
		assertEquals(0.0F, matrix.getElement34());
		assertEquals(0.0F, matrix.getElement41());
		assertEquals(0.0F, matrix.getElement42());
		assertEquals(0.0F, matrix.getElement43());
		assertEquals(1.0F, matrix.getElement44());
		
		assertThrows(NullPointerException.class, () -> Matrix44F.rotate(new Vector3F(0.0F, 0.0F, 1.0F), new Vector3F(0.0F, 1.0F, 0.0F), null));
		assertThrows(NullPointerException.class, () -> Matrix44F.rotate(new Vector3F(0.0F, 0.0F, 1.0F), null, new Vector3F(1.0F, 0.0F, 0.0F)));
		assertThrows(NullPointerException.class, () -> Matrix44F.rotate(null, new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F)));
	}
	
	@Test
	public void testScaleFloat() {
		final Matrix44F matrix = Matrix44F.scale(2.0F);
		
		assertEquals(2.0F, matrix.getElement11());
		assertEquals(0.0F, matrix.getElement12());
		assertEquals(0.0F, matrix.getElement13());
		assertEquals(0.0F, matrix.getElement14());
		assertEquals(0.0F, matrix.getElement21());
		assertEquals(2.0F, matrix.getElement22());
		assertEquals(0.0F, matrix.getElement23());
		assertEquals(0.0F, matrix.getElement24());
		assertEquals(0.0F, matrix.getElement31());
		assertEquals(0.0F, matrix.getElement32());
		assertEquals(2.0F, matrix.getElement33());
		assertEquals(0.0F, matrix.getElement34());
		assertEquals(0.0F, matrix.getElement41());
		assertEquals(0.0F, matrix.getElement42());
		assertEquals(0.0F, matrix.getElement43());
		assertEquals(1.0F, matrix.getElement44());
	}
	
	@Test
	public void testScaleFloatFloatFloat() {
		final Matrix44F matrix = Matrix44F.scale(2.0F, 3.0F, 4.0F);
		
		assertEquals(2.0F, matrix.getElement11());
		assertEquals(0.0F, matrix.getElement12());
		assertEquals(0.0F, matrix.getElement13());
		assertEquals(0.0F, matrix.getElement14());
		assertEquals(0.0F, matrix.getElement21());
		assertEquals(3.0F, matrix.getElement22());
		assertEquals(0.0F, matrix.getElement23());
		assertEquals(0.0F, matrix.getElement24());
		assertEquals(0.0F, matrix.getElement31());
		assertEquals(0.0F, matrix.getElement32());
		assertEquals(4.0F, matrix.getElement33());
		assertEquals(0.0F, matrix.getElement34());
		assertEquals(0.0F, matrix.getElement41());
		assertEquals(0.0F, matrix.getElement42());
		assertEquals(0.0F, matrix.getElement43());
		assertEquals(1.0F, matrix.getElement44());
	}
	
	@Test
	public void testScaleVector3F() {
		final Matrix44F matrix = Matrix44F.scale(new Vector3F(2.0F, 3.0F, 4.0F));
		
		assertEquals(2.0F, matrix.getElement11());
		assertEquals(0.0F, matrix.getElement12());
		assertEquals(0.0F, matrix.getElement13());
		assertEquals(0.0F, matrix.getElement14());
		assertEquals(0.0F, matrix.getElement21());
		assertEquals(3.0F, matrix.getElement22());
		assertEquals(0.0F, matrix.getElement23());
		assertEquals(0.0F, matrix.getElement24());
		assertEquals(0.0F, matrix.getElement31());
		assertEquals(0.0F, matrix.getElement32());
		assertEquals(4.0F, matrix.getElement33());
		assertEquals(0.0F, matrix.getElement34());
		assertEquals(0.0F, matrix.getElement41());
		assertEquals(0.0F, matrix.getElement42());
		assertEquals(0.0F, matrix.getElement43());
		assertEquals(1.0F, matrix.getElement44());
		
		assertThrows(NullPointerException.class, () -> Matrix44F.scale(null));
	}
	
	@Test
	public void testToArray() {
		final Matrix44F matrix = new Matrix44F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F, 16.0F);
		
		final float[] array = matrix.toArray();
		
		assertNotNull(array);
		
		assertEquals(16, array.length);
		
		assertEquals( 1.0F, array[ 0]);
		assertEquals( 2.0F, array[ 1]);
		assertEquals( 3.0F, array[ 2]);
		assertEquals( 4.0F, array[ 3]);
		assertEquals( 5.0F, array[ 4]);
		assertEquals( 6.0F, array[ 5]);
		assertEquals( 7.0F, array[ 6]);
		assertEquals( 8.0F, array[ 7]);
		assertEquals( 9.0F, array[ 8]);
		assertEquals(10.0F, array[ 9]);
		assertEquals(11.0F, array[10]);
		assertEquals(12.0F, array[11]);
		assertEquals(13.0F, array[12]);
		assertEquals(14.0F, array[13]);
		assertEquals(15.0F, array[14]);
		assertEquals(16.0F, array[15]);
	}
	
	@Test
	public void testToString() {
		final Matrix44F matrix = new Matrix44F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F, 16.0F);
		
		assertEquals("new Matrix44F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F, 16.0F)", matrix.toString());
	}
	
	@Test
	public void testTranslateFloatFloatFloat() {
		final Matrix44F matrix = Matrix44F.translate(2.0F, 3.0F, 4.0F);
		
		assertEquals(1.0F, matrix.getElement11());
		assertEquals(0.0F, matrix.getElement12());
		assertEquals(0.0F, matrix.getElement13());
		assertEquals(2.0F, matrix.getElement14());
		assertEquals(0.0F, matrix.getElement21());
		assertEquals(1.0F, matrix.getElement22());
		assertEquals(0.0F, matrix.getElement23());
		assertEquals(3.0F, matrix.getElement24());
		assertEquals(0.0F, matrix.getElement31());
		assertEquals(0.0F, matrix.getElement32());
		assertEquals(1.0F, matrix.getElement33());
		assertEquals(4.0F, matrix.getElement34());
		assertEquals(0.0F, matrix.getElement41());
		assertEquals(0.0F, matrix.getElement42());
		assertEquals(0.0F, matrix.getElement43());
		assertEquals(1.0F, matrix.getElement44());
	}
	
	@Test
	public void testTranslatePoint3F() {
		final Matrix44F matrix = Matrix44F.translate(new Point3F(2.0F, 3.0F, 4.0F));
		
		assertEquals(1.0F, matrix.getElement11());
		assertEquals(0.0F, matrix.getElement12());
		assertEquals(0.0F, matrix.getElement13());
		assertEquals(2.0F, matrix.getElement14());
		assertEquals(0.0F, matrix.getElement21());
		assertEquals(1.0F, matrix.getElement22());
		assertEquals(0.0F, matrix.getElement23());
		assertEquals(3.0F, matrix.getElement24());
		assertEquals(0.0F, matrix.getElement31());
		assertEquals(0.0F, matrix.getElement32());
		assertEquals(1.0F, matrix.getElement33());
		assertEquals(4.0F, matrix.getElement34());
		assertEquals(0.0F, matrix.getElement41());
		assertEquals(0.0F, matrix.getElement42());
		assertEquals(0.0F, matrix.getElement43());
		assertEquals(1.0F, matrix.getElement44());
		
		assertThrows(NullPointerException.class, () -> Matrix44F.translate(null));
	}
	
	@Test
	public void testTranspose() {
		final Matrix44F a = new Matrix44F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F, 16.0F);
		final Matrix44F b = Matrix44F.transpose(a);
		
		assertEquals( 1.0F, b.getElement11());
		assertEquals( 5.0F, b.getElement12());
		assertEquals( 9.0F, b.getElement13());
		assertEquals(13.0F, b.getElement14());
		assertEquals( 2.0F, b.getElement21());
		assertEquals( 6.0F, b.getElement22());
		assertEquals(10.0F, b.getElement23());
		assertEquals(14.0F, b.getElement24());
		assertEquals( 3.0F, b.getElement31());
		assertEquals( 7.0F, b.getElement32());
		assertEquals(11.0F, b.getElement33());
		assertEquals(15.0F, b.getElement34());
		assertEquals( 4.0F, b.getElement41());
		assertEquals( 8.0F, b.getElement42());
		assertEquals(12.0F, b.getElement43());
		assertEquals(16.0F, b.getElement44());
		
		assertThrows(NullPointerException.class, () -> Matrix44F.transpose(null));
	}
	
	@Test
	public void testWrite() {
		final Matrix44F a = new Matrix44F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F, 10.0F, 11.0F, 12.0F, 13.0F, 14.0F, 15.0F, 16.0F);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Matrix44F b = Matrix44F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}