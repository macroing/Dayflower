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
public final class Matrix44DUnitTests {
	public Matrix44DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstants() {
		assertEquals( 0, Matrix44D.ARRAY_OFFSET_ELEMENT_1_1);
		assertEquals( 1, Matrix44D.ARRAY_OFFSET_ELEMENT_1_2);
		assertEquals( 2, Matrix44D.ARRAY_OFFSET_ELEMENT_1_3);
		assertEquals( 3, Matrix44D.ARRAY_OFFSET_ELEMENT_1_4);
		assertEquals( 4, Matrix44D.ARRAY_OFFSET_ELEMENT_2_1);
		assertEquals( 5, Matrix44D.ARRAY_OFFSET_ELEMENT_2_2);
		assertEquals( 6, Matrix44D.ARRAY_OFFSET_ELEMENT_2_3);
		assertEquals( 7, Matrix44D.ARRAY_OFFSET_ELEMENT_2_4);
		assertEquals( 8, Matrix44D.ARRAY_OFFSET_ELEMENT_3_1);
		assertEquals( 9, Matrix44D.ARRAY_OFFSET_ELEMENT_3_2);
		assertEquals(10, Matrix44D.ARRAY_OFFSET_ELEMENT_3_3);
		assertEquals(11, Matrix44D.ARRAY_OFFSET_ELEMENT_3_4);
		assertEquals(12, Matrix44D.ARRAY_OFFSET_ELEMENT_4_1);
		assertEquals(13, Matrix44D.ARRAY_OFFSET_ELEMENT_4_2);
		assertEquals(14, Matrix44D.ARRAY_OFFSET_ELEMENT_4_3);
		assertEquals(15, Matrix44D.ARRAY_OFFSET_ELEMENT_4_4);
		assertEquals(16, Matrix44D.ARRAY_SIZE);
	}
	
	@Test
	public void testConstructor() {
		final Matrix44D matrix = new Matrix44D();
		
		assertEquals(1.0D, matrix.getElement11());
		assertEquals(0.0D, matrix.getElement12());
		assertEquals(0.0D, matrix.getElement13());
		assertEquals(0.0D, matrix.getElement14());
		assertEquals(0.0D, matrix.getElement21());
		assertEquals(1.0D, matrix.getElement22());
		assertEquals(0.0D, matrix.getElement23());
		assertEquals(0.0D, matrix.getElement24());
		assertEquals(0.0D, matrix.getElement31());
		assertEquals(0.0D, matrix.getElement32());
		assertEquals(1.0D, matrix.getElement33());
		assertEquals(0.0D, matrix.getElement34());
		assertEquals(0.0D, matrix.getElement41());
		assertEquals(0.0D, matrix.getElement42());
		assertEquals(0.0D, matrix.getElement43());
		assertEquals(1.0D, matrix.getElement44());
	}
	
	@Test
	public void testConstructorDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble() {
		final Matrix44D matrix = new Matrix44D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D, 16.0D);
		
		assertEquals( 1.0D, matrix.getElement11());
		assertEquals( 2.0D, matrix.getElement12());
		assertEquals( 3.0D, matrix.getElement13());
		assertEquals( 4.0D, matrix.getElement14());
		assertEquals( 5.0D, matrix.getElement21());
		assertEquals( 6.0D, matrix.getElement22());
		assertEquals( 7.0D, matrix.getElement23());
		assertEquals( 8.0D, matrix.getElement24());
		assertEquals( 9.0D, matrix.getElement31());
		assertEquals(10.0D, matrix.getElement32());
		assertEquals(11.0D, matrix.getElement33());
		assertEquals(12.0D, matrix.getElement34());
		assertEquals(13.0D, matrix.getElement41());
		assertEquals(14.0D, matrix.getElement42());
		assertEquals(15.0D, matrix.getElement43());
		assertEquals(16.0D, matrix.getElement44());
	}
	
	@Test
	public void testDeterminant() {
		final Matrix44D matrix = Matrix44D.identity();
		
		assertEquals(1.0D, matrix.determinant());
	}
	
	@Test
	public void testEquals() {
		final Matrix44D a = new Matrix44D( 0.0D,  1.0D,  2.0D,  3.0D,  4.0D,  5.0D,  6.0D,  7.0D,  8.0D,  9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D);
		final Matrix44D b = new Matrix44D( 0.0D,  1.0D,  2.0D,  3.0D,  4.0D,  5.0D,  6.0D,  7.0D,  8.0D,  9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D);
		final Matrix44D c = new Matrix44D( 0.0D,  1.0D,  2.0D,  3.0D,  4.0D,  5.0D,  6.0D,  7.0D,  8.0D,  9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 16.0D);
		final Matrix44D d = new Matrix44D( 0.0D,  1.0D,  2.0D,  3.0D,  4.0D,  5.0D,  6.0D,  7.0D,  8.0D,  9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 16.0D, 15.0D);
		final Matrix44D e = new Matrix44D( 0.0D,  1.0D,  2.0D,  3.0D,  4.0D,  5.0D,  6.0D,  7.0D,  8.0D,  9.0D, 10.0D, 11.0D, 12.0D, 16.0D, 14.0D, 15.0D);
		final Matrix44D f = new Matrix44D( 0.0D,  1.0D,  2.0D,  3.0D,  4.0D,  5.0D,  6.0D,  7.0D,  8.0D,  9.0D, 10.0D, 11.0D, 16.0D, 13.0D, 14.0D, 15.0D);
		final Matrix44D g = new Matrix44D( 0.0D,  1.0D,  2.0D,  3.0D,  4.0D,  5.0D,  6.0D,  7.0D,  8.0D,  9.0D, 10.0D, 16.0D, 12.0D, 13.0D, 14.0D, 15.0D);
		final Matrix44D h = new Matrix44D( 0.0D,  1.0D,  2.0D,  3.0D,  4.0D,  5.0D,  6.0D,  7.0D,  8.0D,  9.0D, 16.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D);
		final Matrix44D i = new Matrix44D( 0.0D,  1.0D,  2.0D,  3.0D,  4.0D,  5.0D,  6.0D,  7.0D,  8.0D, 16.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D);
		final Matrix44D j = new Matrix44D( 0.0D,  1.0D,  2.0D,  3.0D,  4.0D,  5.0D,  6.0D,  7.0D, 16.0D,  9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D);
		final Matrix44D k = new Matrix44D( 0.0D,  1.0D,  2.0D,  3.0D,  4.0D,  5.0D,  6.0D, 16.0D,  8.0D,  9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D);
		final Matrix44D l = new Matrix44D( 0.0D,  1.0D,  2.0D,  3.0D,  4.0D,  5.0D, 16.0D,  7.0D,  8.0D,  9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D);
		final Matrix44D m = new Matrix44D( 0.0D,  1.0D,  2.0D,  3.0D,  4.0D, 16.0D,  6.0D,  7.0D,  8.0D,  9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D);
		final Matrix44D n = new Matrix44D( 0.0D,  1.0D,  2.0D,  3.0D, 16.0D,  5.0D,  6.0D,  7.0D,  8.0D,  9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D);
		final Matrix44D o = new Matrix44D( 0.0D,  1.0D,  2.0D, 16.0D,  4.0D,  5.0D,  6.0D,  7.0D,  8.0D,  9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D);
		final Matrix44D p = new Matrix44D( 0.0D,  1.0D, 16.0D,  3.0D,  4.0D,  5.0D,  6.0D,  7.0D,  8.0D,  9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D);
		final Matrix44D q = new Matrix44D( 0.0D, 16.0D,  2.0D,  3.0D,  4.0D,  5.0D,  6.0D,  7.0D,  8.0D,  9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D);
		final Matrix44D r = new Matrix44D(16.0D,  1.0D,  2.0D,  3.0D,  4.0D,  5.0D,  6.0D,  7.0D,  8.0D,  9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D);
		final Matrix44D s = null;
		
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
		final Matrix44D matrix = new Matrix44D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D, 16.0D);
		
		assertEquals( 1.0D, matrix.getElement( 0));
		assertEquals( 2.0D, matrix.getElement( 1));
		assertEquals( 3.0D, matrix.getElement( 2));
		assertEquals( 4.0D, matrix.getElement( 3));
		assertEquals( 5.0D, matrix.getElement( 4));
		assertEquals( 6.0D, matrix.getElement( 5));
		assertEquals( 7.0D, matrix.getElement( 6));
		assertEquals( 8.0D, matrix.getElement( 7));
		assertEquals( 9.0D, matrix.getElement( 8));
		assertEquals(10.0D, matrix.getElement( 9));
		assertEquals(11.0D, matrix.getElement(10));
		assertEquals(12.0D, matrix.getElement(11));
		assertEquals(13.0D, matrix.getElement(12));
		assertEquals(14.0D, matrix.getElement(13));
		assertEquals(15.0D, matrix.getElement(14));
		assertEquals(16.0D, matrix.getElement(15));
		
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(- 1));
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(+16));
	}
	
	@Test
	public void testGetElementIntInt() {
		final Matrix44D matrix = new Matrix44D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D, 16.0D);
		
		assertEquals( 1.0D, matrix.getElement(1, 1));
		assertEquals( 2.0D, matrix.getElement(1, 2));
		assertEquals( 3.0D, matrix.getElement(1, 3));
		assertEquals( 4.0D, matrix.getElement(1, 4));
		assertEquals( 5.0D, matrix.getElement(2, 1));
		assertEquals( 6.0D, matrix.getElement(2, 2));
		assertEquals( 7.0D, matrix.getElement(2, 3));
		assertEquals( 8.0D, matrix.getElement(2, 4));
		assertEquals( 9.0D, matrix.getElement(3, 1));
		assertEquals(10.0D, matrix.getElement(3, 2));
		assertEquals(11.0D, matrix.getElement(3, 3));
		assertEquals(12.0D, matrix.getElement(3, 4));
		assertEquals(13.0D, matrix.getElement(4, 1));
		assertEquals(14.0D, matrix.getElement(4, 2));
		assertEquals(15.0D, matrix.getElement(4, 3));
		assertEquals(16.0D, matrix.getElement(4, 4));
		
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(0, 1));
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(1, 0));
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(5, 1));
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(1, 5));
	}
	
	@Test
	public void testGetElement11() {
		final Matrix44D matrix = new Matrix44D(1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement11());
	}
	
	@Test
	public void testGetElement12() {
		final Matrix44D matrix = new Matrix44D(0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement12());
	}
	
	@Test
	public void testGetElement13() {
		final Matrix44D matrix = new Matrix44D(0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement13());
	}
	
	@Test
	public void testGetElement14() {
		final Matrix44D matrix = new Matrix44D(0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement14());
	}
	
	@Test
	public void testGetElement21() {
		final Matrix44D matrix = new Matrix44D(0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement21());
	}
	
	@Test
	public void testGetElement22() {
		final Matrix44D matrix = new Matrix44D(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement22());
	}
	
	@Test
	public void testGetElement23() {
		final Matrix44D matrix = new Matrix44D(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement23());
	}
	
	@Test
	public void testGetElement24() {
		final Matrix44D matrix = new Matrix44D(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement24());
	}
	
	@Test
	public void testGetElement31() {
		final Matrix44D matrix = new Matrix44D(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement31());
	}
	
	@Test
	public void testGetElement32() {
		final Matrix44D matrix = new Matrix44D(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement32());
	}
	
	@Test
	public void testGetElement33() {
		final Matrix44D matrix = new Matrix44D(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement33());
	}
	
	@Test
	public void testGetElement34() {
		final Matrix44D matrix = new Matrix44D(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement34());
	}
	
	@Test
	public void testGetElement41() {
		final Matrix44D matrix = new Matrix44D(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement41());
	}
	
	@Test
	public void testGetElement42() {
		final Matrix44D matrix = new Matrix44D(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement42());
	}
	
	@Test
	public void testGetElement43() {
		final Matrix44D matrix = new Matrix44D(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement43());
	}
	
	@Test
	public void testGetElement44() {
		final Matrix44D matrix = new Matrix44D(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D);
		
		assertEquals(1.0D, matrix.getElement44());
	}
	
	@Test
	public void testHashCode() {
		final Matrix44D a = new Matrix44D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D, 16.0D);
		final Matrix44D b = new Matrix44D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D, 16.0D);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIdentity() {
		final Matrix44D matrix = Matrix44D.identity();
		
		assertEquals(1.0D, matrix.getElement11());
		assertEquals(0.0D, matrix.getElement12());
		assertEquals(0.0D, matrix.getElement13());
		assertEquals(0.0D, matrix.getElement14());
		assertEquals(0.0D, matrix.getElement21());
		assertEquals(1.0D, matrix.getElement22());
		assertEquals(0.0D, matrix.getElement23());
		assertEquals(0.0D, matrix.getElement24());
		assertEquals(0.0D, matrix.getElement31());
		assertEquals(0.0D, matrix.getElement32());
		assertEquals(1.0D, matrix.getElement33());
		assertEquals(0.0D, matrix.getElement34());
		assertEquals(0.0D, matrix.getElement41());
		assertEquals(0.0D, matrix.getElement42());
		assertEquals(0.0D, matrix.getElement43());
		assertEquals(1.0D, matrix.getElement44());
	}
	
	@Test
	public void testInverse() {
		final Matrix44D a = new Matrix44D(2.0D, 0.0D, 0.0D, 0.0D, 0.0D, 2.0D, 0.0D, 0.0D, 0.0D, 0.0D, 2.0D, 0.0D, 0.0D, 0.0D, 0.0D, 2.0D);
		final Matrix44D b = Matrix44D.inverse(a);
		final Matrix44D c = Matrix44D.inverse(b);
		final Matrix44D d = Matrix44D.multiply(a, b);
		final Matrix44D e = Matrix44D.identity();
		
		assertEquals(a, c);
		assertEquals(d, e);
		
		assertThrows(IllegalArgumentException.class, () -> Matrix44D.inverse(new Matrix44D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D, 16.0D)));
		assertThrows(NullPointerException.class, () -> Matrix44D.inverse(null));
	}
	
	@Test
	public void testIsInvertible() {
		final Matrix44D a = new Matrix44D(1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D,  0.0D,  1.0D,  0.0D,  0.0D,  0.0D,  0.0D,  1.0D);
		final Matrix44D b = new Matrix44D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D, 16.0D);
		
		assertTrue(a.isInvertible());
		assertFalse(b.isInvertible());
	}
	
	@Test
	public void testLookAt() {
		final Matrix44D matrix = Matrix44D.lookAt(new Point3D(1.0D, 1.0D, 1.0D), new Point3D(1.0D, 1.0D, 2.0D), Vector3D.y());
		
		assertEquals(1.0D, matrix.getElement11());
		assertEquals(0.0D, matrix.getElement12());
		assertEquals(0.0D, matrix.getElement13());
		assertEquals(1.0D, matrix.getElement14());
		assertEquals(0.0D, matrix.getElement21());
		assertEquals(1.0D, matrix.getElement22());
		assertEquals(0.0D, matrix.getElement23());
		assertEquals(1.0D, matrix.getElement24());
		assertEquals(0.0D, matrix.getElement31());
		assertEquals(0.0D, matrix.getElement32());
		assertEquals(1.0D, matrix.getElement33());
		assertEquals(1.0D, matrix.getElement34());
		assertEquals(0.0D, matrix.getElement41());
		assertEquals(0.0D, matrix.getElement42());
		assertEquals(0.0D, matrix.getElement43());
		assertEquals(1.0D, matrix.getElement44());
		
		assertThrows(NullPointerException.class, () -> Matrix44D.lookAt(new Point3D(), new Point3D(), null));
		assertThrows(NullPointerException.class, () -> Matrix44D.lookAt(new Point3D(), null, new Vector3D()));
		assertThrows(NullPointerException.class, () -> Matrix44D.lookAt(null, new Point3D(), new Vector3D()));
	}
	
	@Test
	public void testMultiply() {
		final Matrix44D a = new Matrix44D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D, 16.0D);
		final Matrix44D b = new Matrix44D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D, 16.0D);
		final Matrix44D c = Matrix44D.multiply(a, b);
		
		assertEquals( 90.0D, c.getElement11());
		assertEquals(100.0D, c.getElement12());
		assertEquals(110.0D, c.getElement13());
		assertEquals(120.0D, c.getElement14());
		assertEquals(202.0D, c.getElement21());
		assertEquals(228.0D, c.getElement22());
		assertEquals(254.0D, c.getElement23());
		assertEquals(280.0D, c.getElement24());
		assertEquals(314.0D, c.getElement31());
		assertEquals(356.0D, c.getElement32());
		assertEquals(398.0D, c.getElement33());
		assertEquals(440.0D, c.getElement34());
		assertEquals(426.0D, c.getElement41());
		assertEquals(484.0D, c.getElement42());
		assertEquals(542.0D, c.getElement43());
		assertEquals(600.0D, c.getElement44());
		
		assertThrows(NullPointerException.class, () -> Matrix44D.multiply(a, null));
		assertThrows(NullPointerException.class, () -> Matrix44D.multiply(null, b));
	}
	
	@Test
	public void testRead() throws IOException {
		final Matrix44D a = new Matrix44D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D, 16.0D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeDouble(a.getElement11());
		dataOutput.writeDouble(a.getElement12());
		dataOutput.writeDouble(a.getElement13());
		dataOutput.writeDouble(a.getElement14());
		dataOutput.writeDouble(a.getElement21());
		dataOutput.writeDouble(a.getElement22());
		dataOutput.writeDouble(a.getElement23());
		dataOutput.writeDouble(a.getElement24());
		dataOutput.writeDouble(a.getElement31());
		dataOutput.writeDouble(a.getElement32());
		dataOutput.writeDouble(a.getElement33());
		dataOutput.writeDouble(a.getElement34());
		dataOutput.writeDouble(a.getElement41());
		dataOutput.writeDouble(a.getElement42());
		dataOutput.writeDouble(a.getElement43());
		dataOutput.writeDouble(a.getElement44());
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Matrix44D b = Matrix44D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Matrix44D.read(null));
		assertThrows(UncheckedIOException.class, () -> Matrix44D.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testRotateAngleDDoubleDoubleDouble() {
		final Matrix44D a = Matrix44D.rotate(AngleD.degrees( 90.0D), 1.0D, 0.0D, 0.0D);
		final Matrix44D b = Matrix44D.rotate(AngleD.degrees(180.0D), 0.0D, 1.0D, 0.0D);
		final Matrix44D c = Matrix44D.rotate(AngleD.degrees(270.0D), 0.0D, 0.0D, 1.0D);
		
		final Matrix44D d = Matrix44D.rotateX(AngleD.degrees( 90.0D));
		final Matrix44D e = Matrix44D.rotateY(AngleD.degrees(180.0D));
		final Matrix44D f = Matrix44D.rotateZ(AngleD.degrees(270.0D));
		
		assertEquals(d, a);
		assertEquals(e, b);
		assertEquals(f, c);
		
		assertThrows(NullPointerException.class, () -> Matrix44D.rotate(null, 1.0D, 0.0D, 0.0D));
	}
	
	@Test
	public void testRotateAngleDVector3D() {
		final Matrix44D a = Matrix44D.rotate(AngleD.degrees( 90.0D), Vector3D.x());
		final Matrix44D b = Matrix44D.rotate(AngleD.degrees(180.0D), Vector3D.y());
		final Matrix44D c = Matrix44D.rotate(AngleD.degrees(270.0D), Vector3D.z());
		
		final Matrix44D d = Matrix44D.rotateX(AngleD.degrees( 90.0D));
		final Matrix44D e = Matrix44D.rotateY(AngleD.degrees(180.0D));
		final Matrix44D f = Matrix44D.rotateZ(AngleD.degrees(270.0D));
		
		assertEquals(d, a);
		assertEquals(e, b);
		assertEquals(f, c);
		
		assertThrows(NullPointerException.class, () -> Matrix44D.rotate(AngleD.degrees(0.0D), null));
		assertThrows(NullPointerException.class, () -> Matrix44D.rotate((AngleD)(null), Vector3D.x()));
	}
	
	@Test
	public void testRotateOrthonormalBasis33F() {
		final Matrix44D matrix = Matrix44D.rotate(new OrthonormalBasis33D(Vector3D.z(), Vector3D.y(), Vector3D.x()));
		
		assertEquals(1.0D, matrix.getElement11());
		assertEquals(0.0D, matrix.getElement12());
		assertEquals(0.0D, matrix.getElement13());
		assertEquals(0.0D, matrix.getElement14());
		assertEquals(0.0D, matrix.getElement21());
		assertEquals(1.0D, matrix.getElement22());
		assertEquals(0.0D, matrix.getElement23());
		assertEquals(0.0D, matrix.getElement24());
		assertEquals(0.0D, matrix.getElement31());
		assertEquals(0.0D, matrix.getElement32());
		assertEquals(1.0D, matrix.getElement33());
		assertEquals(0.0D, matrix.getElement34());
		assertEquals(0.0D, matrix.getElement41());
		assertEquals(0.0D, matrix.getElement42());
		assertEquals(0.0D, matrix.getElement43());
		assertEquals(1.0D, matrix.getElement44());
		
		assertThrows(NullPointerException.class, () -> Matrix44D.rotate((OrthonormalBasis33D)(null)));
	}
	
	@Test
	public void testRotateVector3DVector3D() {
		final Matrix44D matrix = Matrix44D.rotate(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D));
		
		assertEquals(1.0D, matrix.getElement11());
		assertEquals(0.0D, matrix.getElement12());
		assertEquals(0.0D, matrix.getElement13());
		assertEquals(0.0D, matrix.getElement14());
		assertEquals(0.0D, matrix.getElement21());
		assertEquals(1.0D, matrix.getElement22());
		assertEquals(0.0D, matrix.getElement23());
		assertEquals(0.0D, matrix.getElement24());
		assertEquals(0.0D, matrix.getElement31());
		assertEquals(0.0D, matrix.getElement32());
		assertEquals(1.0D, matrix.getElement33());
		assertEquals(0.0D, matrix.getElement34());
		assertEquals(0.0D, matrix.getElement41());
		assertEquals(0.0D, matrix.getElement42());
		assertEquals(0.0D, matrix.getElement43());
		assertEquals(1.0D, matrix.getElement44());
		
		assertThrows(NullPointerException.class, () -> Matrix44D.rotate(new Vector3D(0.0D, 0.0D, 1.0D), null));
		assertThrows(NullPointerException.class, () -> Matrix44D.rotate((Vector3D)(null), new Vector3D(0.0D, 1.0D, 0.0D)));
	}
	
	@Test
	public void testRotateVector3DVector3DVector3D() {
		final Matrix44D matrix = Matrix44D.rotate(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(1.0D, matrix.getElement11());
		assertEquals(0.0D, matrix.getElement12());
		assertEquals(0.0D, matrix.getElement13());
		assertEquals(0.0D, matrix.getElement14());
		assertEquals(0.0D, matrix.getElement21());
		assertEquals(1.0D, matrix.getElement22());
		assertEquals(0.0D, matrix.getElement23());
		assertEquals(0.0D, matrix.getElement24());
		assertEquals(0.0D, matrix.getElement31());
		assertEquals(0.0D, matrix.getElement32());
		assertEquals(1.0D, matrix.getElement33());
		assertEquals(0.0D, matrix.getElement34());
		assertEquals(0.0D, matrix.getElement41());
		assertEquals(0.0D, matrix.getElement42());
		assertEquals(0.0D, matrix.getElement43());
		assertEquals(1.0D, matrix.getElement44());
		
		assertThrows(NullPointerException.class, () -> Matrix44D.rotate(new Vector3D(0.0D, 0.0D, 1.0D), new Vector3D(0.0D, 1.0D, 0.0D), null));
		assertThrows(NullPointerException.class, () -> Matrix44D.rotate(new Vector3D(0.0D, 0.0D, 1.0D), null, new Vector3D(1.0D, 0.0D, 0.0D)));
		assertThrows(NullPointerException.class, () -> Matrix44D.rotate(null, new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D)));
	}
	
	@Test
	public void testRotateXAngleD() {
		final double angleDegrees = 90.0D;
		final double angleRadians = Math.toRadians(angleDegrees);
		final double angleRadiansCos = Math.cos(angleRadians);
		final double angleRadiansSin = Math.sin(angleRadians);
		
		final Matrix44D matrix = Matrix44D.rotateX(AngleD.degrees(angleDegrees));
		
		assertEquals(1.0D,             matrix.getElement11());
		assertEquals(0.0D,             matrix.getElement12());
		assertEquals(0.0D,             matrix.getElement13());
		assertEquals(0.0D,             matrix.getElement14());
		assertEquals(0.0D,             matrix.getElement21());
		assertEquals(+angleRadiansCos, matrix.getElement22());
		assertEquals(-angleRadiansSin, matrix.getElement23());
		assertEquals(0.0D,             matrix.getElement24());
		assertEquals(0.0D,             matrix.getElement31());
		assertEquals(+angleRadiansSin, matrix.getElement32());
		assertEquals(+angleRadiansCos, matrix.getElement33());
		assertEquals(0.0D,             matrix.getElement34());
		assertEquals(0.0D,             matrix.getElement41());
		assertEquals(0.0D,             matrix.getElement42());
		assertEquals(0.0D,             matrix.getElement43());
		assertEquals(1.0D,             matrix.getElement44());
		
		assertThrows(NullPointerException.class, () -> Matrix44D.rotateX(null));
	}
	
	@Test
	public void testRotateXDouble() {
		final double angleDegrees = 90.0D;
		final double angleRadians = Math.toRadians(angleDegrees);
		final double angleRadiansCos = Math.cos(angleRadians);
		final double angleRadiansSin = Math.sin(angleRadians);
		
		final Matrix44D matrix = Matrix44D.rotateX(angleDegrees);
		
		assertEquals(1.0D,             matrix.getElement11());
		assertEquals(0.0D,             matrix.getElement12());
		assertEquals(0.0D,             matrix.getElement13());
		assertEquals(0.0D,             matrix.getElement14());
		assertEquals(0.0D,             matrix.getElement21());
		assertEquals(+angleRadiansCos, matrix.getElement22());
		assertEquals(-angleRadiansSin, matrix.getElement23());
		assertEquals(0.0D,             matrix.getElement24());
		assertEquals(0.0D,             matrix.getElement31());
		assertEquals(+angleRadiansSin, matrix.getElement32());
		assertEquals(+angleRadiansCos, matrix.getElement33());
		assertEquals(0.0D,             matrix.getElement34());
		assertEquals(0.0D,             matrix.getElement41());
		assertEquals(0.0D,             matrix.getElement42());
		assertEquals(0.0D,             matrix.getElement43());
		assertEquals(1.0D,             matrix.getElement44());
	}
	
	@Test
	public void testRotateXDoubleBoolean() {
		final double angleDegrees = 90.0D;
		final double angleRadians = Math.toRadians(angleDegrees);
		final double angleRadiansCos = Math.cos(angleRadians);
		final double angleRadiansSin = Math.sin(angleRadians);
		
		final Matrix44D a = Matrix44D.rotateX(angleDegrees, false);
		final Matrix44D b = Matrix44D.rotateX(angleRadians, true);
		
		assertEquals(1.0D,             a.getElement11());
		assertEquals(0.0D,             a.getElement12());
		assertEquals(0.0D,             a.getElement13());
		assertEquals(0.0D,             a.getElement14());
		assertEquals(0.0D,             a.getElement21());
		assertEquals(+angleRadiansCos, a.getElement22());
		assertEquals(-angleRadiansSin, a.getElement23());
		assertEquals(0.0D,             a.getElement24());
		assertEquals(0.0D,             a.getElement31());
		assertEquals(+angleRadiansSin, a.getElement32());
		assertEquals(+angleRadiansCos, a.getElement33());
		assertEquals(0.0D,             a.getElement34());
		assertEquals(0.0D,             a.getElement41());
		assertEquals(0.0D,             a.getElement42());
		assertEquals(0.0D,             a.getElement43());
		assertEquals(1.0D,             a.getElement44());
		
		assertEquals(1.0D,             b.getElement11());
		assertEquals(0.0D,             b.getElement12());
		assertEquals(0.0D,             b.getElement13());
		assertEquals(0.0D,             b.getElement14());
		assertEquals(0.0D,             b.getElement21());
		assertEquals(+angleRadiansCos, b.getElement22());
		assertEquals(-angleRadiansSin, b.getElement23());
		assertEquals(0.0D,             b.getElement24());
		assertEquals(0.0D,             b.getElement31());
		assertEquals(+angleRadiansSin, b.getElement32());
		assertEquals(+angleRadiansCos, b.getElement33());
		assertEquals(0.0D,             b.getElement34());
		assertEquals(0.0D,             b.getElement41());
		assertEquals(0.0D,             b.getElement42());
		assertEquals(0.0D,             b.getElement43());
		assertEquals(1.0D,             b.getElement44());
	}
	
	@Test
	public void testRotateYAngleD() {
		final double angleDegrees = 90.0D;
		final double angleRadians = Math.toRadians(angleDegrees);
		final double angleRadiansCos = Math.cos(angleRadians);
		final double angleRadiansSin = Math.sin(angleRadians);
		
		final Matrix44D matrix = Matrix44D.rotateY(AngleD.degrees(angleDegrees));
		
		assertEquals(+angleRadiansCos, matrix.getElement11());
		assertEquals(0.0D,             matrix.getElement12());
		assertEquals(+angleRadiansSin, matrix.getElement13());
		assertEquals(0.0D,             matrix.getElement14());
		assertEquals(0.0D,             matrix.getElement21());
		assertEquals(1.0D,             matrix.getElement22());
		assertEquals(0.0D,             matrix.getElement23());
		assertEquals(0.0D,             matrix.getElement24());
		assertEquals(-angleRadiansSin, matrix.getElement31());
		assertEquals(0.0D,             matrix.getElement32());
		assertEquals(+angleRadiansCos, matrix.getElement33());
		assertEquals(0.0D,             matrix.getElement34());
		assertEquals(0.0D,             matrix.getElement41());
		assertEquals(0.0D,             matrix.getElement42());
		assertEquals(0.0D,             matrix.getElement43());
		assertEquals(1.0D,             matrix.getElement44());
		
		assertThrows(NullPointerException.class, () -> Matrix44D.rotateY(null));
	}
	
	@Test
	public void testRotateYDouble() {
		final double angleDegrees = 90.0D;
		final double angleRadians = Math.toRadians(angleDegrees);
		final double angleRadiansCos = Math.cos(angleRadians);
		final double angleRadiansSin = Math.sin(angleRadians);
		
		final Matrix44D matrix = Matrix44D.rotateY(angleDegrees);
		
		assertEquals(+angleRadiansCos, matrix.getElement11());
		assertEquals(0.0D,             matrix.getElement12());
		assertEquals(+angleRadiansSin, matrix.getElement13());
		assertEquals(0.0D,             matrix.getElement14());
		assertEquals(0.0D,             matrix.getElement21());
		assertEquals(1.0D,             matrix.getElement22());
		assertEquals(0.0D,             matrix.getElement23());
		assertEquals(0.0D,             matrix.getElement24());
		assertEquals(-angleRadiansSin, matrix.getElement31());
		assertEquals(0.0D,             matrix.getElement32());
		assertEquals(+angleRadiansCos, matrix.getElement33());
		assertEquals(0.0D,             matrix.getElement34());
		assertEquals(0.0D,             matrix.getElement41());
		assertEquals(0.0D,             matrix.getElement42());
		assertEquals(0.0D,             matrix.getElement43());
		assertEquals(1.0D,             matrix.getElement44());
	}
	
	@Test
	public void testRotateYDoubleBoolean() {
		final double angleDegrees = 90.0D;
		final double angleRadians = Math.toRadians(angleDegrees);
		final double angleRadiansCos = Math.cos(angleRadians);
		final double angleRadiansSin = Math.sin(angleRadians);
		
		final Matrix44D a = Matrix44D.rotateY(angleDegrees, false);
		final Matrix44D b = Matrix44D.rotateY(angleRadians, true);
		
		assertEquals(+angleRadiansCos, a.getElement11());
		assertEquals(0.0D,             a.getElement12());
		assertEquals(+angleRadiansSin, a.getElement13());
		assertEquals(0.0D,             a.getElement14());
		assertEquals(0.0D,             a.getElement21());
		assertEquals(1.0D,             a.getElement22());
		assertEquals(0.0D,             a.getElement23());
		assertEquals(0.0D,             a.getElement24());
		assertEquals(-angleRadiansSin, a.getElement31());
		assertEquals(0.0D,             a.getElement32());
		assertEquals(+angleRadiansCos, a.getElement33());
		assertEquals(0.0D,             a.getElement34());
		assertEquals(0.0D,             a.getElement41());
		assertEquals(0.0D,             a.getElement42());
		assertEquals(0.0D,             a.getElement43());
		assertEquals(1.0D,             a.getElement44());
		
		assertEquals(+angleRadiansCos, b.getElement11());
		assertEquals(0.0D,             b.getElement12());
		assertEquals(+angleRadiansSin, b.getElement13());
		assertEquals(0.0D,             b.getElement14());
		assertEquals(0.0D,             b.getElement21());
		assertEquals(1.0D,             b.getElement22());
		assertEquals(0.0D,             b.getElement23());
		assertEquals(0.0D,             b.getElement24());
		assertEquals(-angleRadiansSin, b.getElement31());
		assertEquals(0.0D,             b.getElement32());
		assertEquals(+angleRadiansCos, b.getElement33());
		assertEquals(0.0D,             b.getElement34());
		assertEquals(0.0D,             b.getElement41());
		assertEquals(0.0D,             b.getElement42());
		assertEquals(0.0D,             b.getElement43());
		assertEquals(1.0D,             b.getElement44());
	}
	
	@Test
	public void testRotateZAngleD() {
		final double angleDegrees = 90.0D;
		final double angleRadians = Math.toRadians(angleDegrees);
		final double angleRadiansCos = Math.cos(angleRadians);
		final double angleRadiansSin = Math.sin(angleRadians);
		
		final Matrix44D matrix = Matrix44D.rotateZ(AngleD.degrees(angleDegrees));
		
		assertEquals(+angleRadiansCos, matrix.getElement11());
		assertEquals(-angleRadiansSin, matrix.getElement12());
		assertEquals(0.0D,             matrix.getElement13());
		assertEquals(0.0D,             matrix.getElement14());
		assertEquals(+angleRadiansSin, matrix.getElement21());
		assertEquals(+angleRadiansCos, matrix.getElement22());
		assertEquals(0.0D,             matrix.getElement23());
		assertEquals(0.0D,             matrix.getElement24());
		assertEquals(0.0D,             matrix.getElement31());
		assertEquals(0.0D,             matrix.getElement32());
		assertEquals(1.0D,             matrix.getElement33());
		assertEquals(0.0D,             matrix.getElement34());
		assertEquals(0.0D,             matrix.getElement41());
		assertEquals(0.0D,             matrix.getElement42());
		assertEquals(0.0D,             matrix.getElement43());
		assertEquals(1.0D,             matrix.getElement44());
		
		assertThrows(NullPointerException.class, () -> Matrix44D.rotateZ(null));
	}
	
	@Test
	public void testRotateZDouble() {
		final double angleDegrees = 90.0D;
		final double angleRadians = Math.toRadians(angleDegrees);
		final double angleRadiansCos = Math.cos(angleRadians);
		final double angleRadiansSin = Math.sin(angleRadians);
		
		final Matrix44D matrix = Matrix44D.rotateZ(angleDegrees);
		
		assertEquals(+angleRadiansCos, matrix.getElement11());
		assertEquals(-angleRadiansSin, matrix.getElement12());
		assertEquals(0.0D,             matrix.getElement13());
		assertEquals(0.0D,             matrix.getElement14());
		assertEquals(+angleRadiansSin, matrix.getElement21());
		assertEquals(+angleRadiansCos, matrix.getElement22());
		assertEquals(0.0D,             matrix.getElement23());
		assertEquals(0.0D,             matrix.getElement24());
		assertEquals(0.0D,             matrix.getElement31());
		assertEquals(0.0D,             matrix.getElement32());
		assertEquals(1.0D,             matrix.getElement33());
		assertEquals(0.0D,             matrix.getElement34());
		assertEquals(0.0D,             matrix.getElement41());
		assertEquals(0.0D,             matrix.getElement42());
		assertEquals(0.0D,             matrix.getElement43());
		assertEquals(1.0D,             matrix.getElement44());
	}
	
	@Test
	public void testRotateZDoubleBoolean() {
		final double angleDegrees = 90.0D;
		final double angleRadians = Math.toRadians(angleDegrees);
		final double angleRadiansCos = Math.cos(angleRadians);
		final double angleRadiansSin = Math.sin(angleRadians);
		
		final Matrix44D a = Matrix44D.rotateZ(angleDegrees, false);
		final Matrix44D b = Matrix44D.rotateZ(angleRadians, true);
		
		assertEquals(+angleRadiansCos, a.getElement11());
		assertEquals(-angleRadiansSin, a.getElement12());
		assertEquals(0.0D,             a.getElement13());
		assertEquals(0.0D,             a.getElement14());
		assertEquals(+angleRadiansSin, a.getElement21());
		assertEquals(+angleRadiansCos, a.getElement22());
		assertEquals(0.0D,             a.getElement23());
		assertEquals(0.0D,             a.getElement24());
		assertEquals(0.0D,             a.getElement31());
		assertEquals(0.0D,             a.getElement32());
		assertEquals(1.0D,             a.getElement33());
		assertEquals(0.0D,             a.getElement34());
		assertEquals(0.0D,             a.getElement41());
		assertEquals(0.0D,             a.getElement42());
		assertEquals(0.0D,             a.getElement43());
		assertEquals(1.0D,             a.getElement44());
		
		assertEquals(+angleRadiansCos, b.getElement11());
		assertEquals(-angleRadiansSin, b.getElement12());
		assertEquals(0.0D,             b.getElement13());
		assertEquals(0.0D,             b.getElement14());
		assertEquals(+angleRadiansSin, b.getElement21());
		assertEquals(+angleRadiansCos, b.getElement22());
		assertEquals(0.0D,             b.getElement23());
		assertEquals(0.0D,             b.getElement24());
		assertEquals(0.0D,             b.getElement31());
		assertEquals(0.0D,             b.getElement32());
		assertEquals(1.0D,             b.getElement33());
		assertEquals(0.0D,             b.getElement34());
		assertEquals(0.0D,             b.getElement41());
		assertEquals(0.0D,             b.getElement42());
		assertEquals(0.0D,             b.getElement43());
		assertEquals(1.0D,             b.getElement44());
	}
	
	@Test
	public void testScaleDouble() {
		final Matrix44D matrix = Matrix44D.scale(2.0D);
		
		assertEquals(2.0D, matrix.getElement11());
		assertEquals(0.0D, matrix.getElement12());
		assertEquals(0.0D, matrix.getElement13());
		assertEquals(0.0D, matrix.getElement14());
		assertEquals(0.0D, matrix.getElement21());
		assertEquals(2.0D, matrix.getElement22());
		assertEquals(0.0D, matrix.getElement23());
		assertEquals(0.0D, matrix.getElement24());
		assertEquals(0.0D, matrix.getElement31());
		assertEquals(0.0D, matrix.getElement32());
		assertEquals(2.0D, matrix.getElement33());
		assertEquals(0.0D, matrix.getElement34());
		assertEquals(0.0D, matrix.getElement41());
		assertEquals(0.0D, matrix.getElement42());
		assertEquals(0.0D, matrix.getElement43());
		assertEquals(1.0D, matrix.getElement44());
	}
	
	@Test
	public void testScaleDoubleDoubleDouble() {
		final Matrix44D matrix = Matrix44D.scale(2.0D, 3.0D, 4.0D);
		
		assertEquals(2.0D, matrix.getElement11());
		assertEquals(0.0D, matrix.getElement12());
		assertEquals(0.0D, matrix.getElement13());
		assertEquals(0.0D, matrix.getElement14());
		assertEquals(0.0D, matrix.getElement21());
		assertEquals(3.0D, matrix.getElement22());
		assertEquals(0.0D, matrix.getElement23());
		assertEquals(0.0D, matrix.getElement24());
		assertEquals(0.0D, matrix.getElement31());
		assertEquals(0.0D, matrix.getElement32());
		assertEquals(4.0D, matrix.getElement33());
		assertEquals(0.0D, matrix.getElement34());
		assertEquals(0.0D, matrix.getElement41());
		assertEquals(0.0D, matrix.getElement42());
		assertEquals(0.0D, matrix.getElement43());
		assertEquals(1.0D, matrix.getElement44());
	}
	
	@Test
	public void testScaleVector3D() {
		final Matrix44D matrix = Matrix44D.scale(new Vector3D(2.0D, 3.0D, 4.0D));
		
		assertEquals(2.0D, matrix.getElement11());
		assertEquals(0.0D, matrix.getElement12());
		assertEquals(0.0D, matrix.getElement13());
		assertEquals(0.0D, matrix.getElement14());
		assertEquals(0.0D, matrix.getElement21());
		assertEquals(3.0D, matrix.getElement22());
		assertEquals(0.0D, matrix.getElement23());
		assertEquals(0.0D, matrix.getElement24());
		assertEquals(0.0D, matrix.getElement31());
		assertEquals(0.0D, matrix.getElement32());
		assertEquals(4.0D, matrix.getElement33());
		assertEquals(0.0D, matrix.getElement34());
		assertEquals(0.0D, matrix.getElement41());
		assertEquals(0.0D, matrix.getElement42());
		assertEquals(0.0D, matrix.getElement43());
		assertEquals(1.0D, matrix.getElement44());
		
		assertThrows(NullPointerException.class, () -> Matrix44D.scale(null));
	}
	
	@Test
	public void testToArray() {
		final Matrix44D matrix = new Matrix44D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D, 16.0D);
		
		final double[] array = matrix.toArray();
		
		assertNotNull(array);
		
		assertEquals(16, array.length);
		
		assertEquals( 1.0D, array[ 0]);
		assertEquals( 2.0D, array[ 1]);
		assertEquals( 3.0D, array[ 2]);
		assertEquals( 4.0D, array[ 3]);
		assertEquals( 5.0D, array[ 4]);
		assertEquals( 6.0D, array[ 5]);
		assertEquals( 7.0D, array[ 6]);
		assertEquals( 8.0D, array[ 7]);
		assertEquals( 9.0D, array[ 8]);
		assertEquals(10.0D, array[ 9]);
		assertEquals(11.0D, array[10]);
		assertEquals(12.0D, array[11]);
		assertEquals(13.0D, array[12]);
		assertEquals(14.0D, array[13]);
		assertEquals(15.0D, array[14]);
		assertEquals(16.0D, array[15]);
	}
	
	@Test
	public void testToString() {
		final Matrix44D matrix = new Matrix44D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D, 16.0D);
		
		assertEquals("new Matrix44D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D, 16.0D)", matrix.toString());
	}
	
	@Test
	public void testTranslateDoubleDoubleDouble() {
		final Matrix44D matrix = Matrix44D.translate(2.0D, 3.0D, 4.0D);
		
		assertEquals(1.0D, matrix.getElement11());
		assertEquals(0.0D, matrix.getElement12());
		assertEquals(0.0D, matrix.getElement13());
		assertEquals(2.0D, matrix.getElement14());
		assertEquals(0.0D, matrix.getElement21());
		assertEquals(1.0D, matrix.getElement22());
		assertEquals(0.0D, matrix.getElement23());
		assertEquals(3.0D, matrix.getElement24());
		assertEquals(0.0D, matrix.getElement31());
		assertEquals(0.0D, matrix.getElement32());
		assertEquals(1.0D, matrix.getElement33());
		assertEquals(4.0D, matrix.getElement34());
		assertEquals(0.0D, matrix.getElement41());
		assertEquals(0.0D, matrix.getElement42());
		assertEquals(0.0D, matrix.getElement43());
		assertEquals(1.0D, matrix.getElement44());
	}
	
	@Test
	public void testTranslatePoint3D() {
		final Matrix44D matrix = Matrix44D.translate(new Point3D(2.0D, 3.0D, 4.0D));
		
		assertEquals(1.0D, matrix.getElement11());
		assertEquals(0.0D, matrix.getElement12());
		assertEquals(0.0D, matrix.getElement13());
		assertEquals(2.0D, matrix.getElement14());
		assertEquals(0.0D, matrix.getElement21());
		assertEquals(1.0D, matrix.getElement22());
		assertEquals(0.0D, matrix.getElement23());
		assertEquals(3.0D, matrix.getElement24());
		assertEquals(0.0D, matrix.getElement31());
		assertEquals(0.0D, matrix.getElement32());
		assertEquals(1.0D, matrix.getElement33());
		assertEquals(4.0D, matrix.getElement34());
		assertEquals(0.0D, matrix.getElement41());
		assertEquals(0.0D, matrix.getElement42());
		assertEquals(0.0D, matrix.getElement43());
		assertEquals(1.0D, matrix.getElement44());
		
		assertThrows(NullPointerException.class, () -> Matrix44D.translate(null));
	}
	
	@Test
	public void testTranspose() {
		final Matrix44D a = new Matrix44D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D, 16.0D);
		final Matrix44D b = Matrix44D.transpose(a);
		
		assertEquals( 1.0D, b.getElement11());
		assertEquals( 5.0D, b.getElement12());
		assertEquals( 9.0D, b.getElement13());
		assertEquals(13.0D, b.getElement14());
		assertEquals( 2.0D, b.getElement21());
		assertEquals( 6.0D, b.getElement22());
		assertEquals(10.0D, b.getElement23());
		assertEquals(14.0D, b.getElement24());
		assertEquals( 3.0D, b.getElement31());
		assertEquals( 7.0D, b.getElement32());
		assertEquals(11.0D, b.getElement33());
		assertEquals(15.0D, b.getElement34());
		assertEquals( 4.0D, b.getElement41());
		assertEquals( 8.0D, b.getElement42());
		assertEquals(12.0D, b.getElement43());
		assertEquals(16.0D, b.getElement44());
		
		assertThrows(NullPointerException.class, () -> Matrix44D.transpose(null));
	}
	
	@Test
	public void testWrite() {
		final Matrix44D a = new Matrix44D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D, 10.0D, 11.0D, 12.0D, 13.0D, 14.0D, 15.0D, 16.0D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Matrix44D b = Matrix44D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}