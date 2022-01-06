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
public final class Matrix33DUnitTests {
	public Matrix33DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final Matrix33D matrix = new Matrix33D();
		
		assertEquals(1.0D, matrix.getElement11());
		assertEquals(0.0D, matrix.getElement12());
		assertEquals(0.0D, matrix.getElement13());
		assertEquals(0.0D, matrix.getElement21());
		assertEquals(1.0D, matrix.getElement22());
		assertEquals(0.0D, matrix.getElement23());
		assertEquals(0.0D, matrix.getElement31());
		assertEquals(0.0D, matrix.getElement32());
		assertEquals(1.0D, matrix.getElement33());
	}
	
	@Test
	public void testConstructorDoubleDoubleDoubleDoubleDoubleDoubleDoubleDoubleDouble() {
		final Matrix33D matrix = new Matrix33D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D);
		
		assertEquals(1.0D, matrix.getElement11());
		assertEquals(2.0D, matrix.getElement12());
		assertEquals(3.0D, matrix.getElement13());
		assertEquals(4.0D, matrix.getElement21());
		assertEquals(5.0D, matrix.getElement22());
		assertEquals(6.0D, matrix.getElement23());
		assertEquals(7.0D, matrix.getElement31());
		assertEquals(8.0D, matrix.getElement32());
		assertEquals(9.0D, matrix.getElement33());
	}
	
	@Test
	public void testDeterminant() {
		final Matrix33D matrix = new Matrix33D(6.0D, 1.0D, 1.0D, 4.0D, -2.0D, 5.0D, 2.0D, 8.0D, 7.0D);
		
		assertEquals(-306.0D, matrix.determinant());
	}
	
	@Test
	public void testEquals() {
		final Matrix33D a = new Matrix33D(0.0D, 1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D);
		final Matrix33D b = new Matrix33D(0.0D, 1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D);
		final Matrix33D c = new Matrix33D(0.0D, 1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 9.0D);
		final Matrix33D d = new Matrix33D(0.0D, 1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 9.0D, 8.0D);
		final Matrix33D e = new Matrix33D(0.0D, 1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 9.0D, 7.0D, 8.0D);
		final Matrix33D f = new Matrix33D(0.0D, 1.0D, 2.0D, 3.0D, 4.0D, 9.0D, 6.0D, 7.0D, 8.0D);
		final Matrix33D g = new Matrix33D(0.0D, 1.0D, 2.0D, 3.0D, 9.0D, 5.0D, 6.0D, 7.0D, 8.0D);
		final Matrix33D h = new Matrix33D(0.0D, 1.0D, 2.0D, 9.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D);
		final Matrix33D i = new Matrix33D(0.0D, 1.0D, 9.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D);
		final Matrix33D j = new Matrix33D(0.0D, 9.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D);
		final Matrix33D k = new Matrix33D(9.0D, 1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D);
		final Matrix33D l = null;
		
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
	}
	
	@Test
	public void testGetElementInt() {
		final Matrix33D matrix = new Matrix33D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D);
		
		assertEquals(1.0D, matrix.getElement(0));
		assertEquals(2.0D, matrix.getElement(1));
		assertEquals(3.0D, matrix.getElement(2));
		assertEquals(4.0D, matrix.getElement(3));
		assertEquals(5.0D, matrix.getElement(4));
		assertEquals(6.0D, matrix.getElement(5));
		assertEquals(7.0D, matrix.getElement(6));
		assertEquals(8.0D, matrix.getElement(7));
		assertEquals(9.0D, matrix.getElement(8));
		
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(-1));
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(+9));
	}
	
	@Test
	public void testGetElementIntInt() {
		final Matrix33D matrix = new Matrix33D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D);
		
		assertEquals(1.0D, matrix.getElement(1, 1));
		assertEquals(2.0D, matrix.getElement(1, 2));
		assertEquals(3.0D, matrix.getElement(1, 3));
		assertEquals(4.0D, matrix.getElement(2, 1));
		assertEquals(5.0D, matrix.getElement(2, 2));
		assertEquals(6.0D, matrix.getElement(2, 3));
		assertEquals(7.0D, matrix.getElement(3, 1));
		assertEquals(8.0D, matrix.getElement(3, 2));
		assertEquals(9.0D, matrix.getElement(3, 3));
		
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(0, 1));
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(1, 0));
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(4, 1));
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(1, 4));
	}
	
	@Test
	public void testGetElement11() {
		final Matrix33D matrix = new Matrix33D(1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement11());
	}
	
	@Test
	public void testGetElement12() {
		final Matrix33D matrix = new Matrix33D(0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement12());
	}
	
	@Test
	public void testGetElement13() {
		final Matrix33D matrix = new Matrix33D(0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement13());
	}
	
	@Test
	public void testGetElement21() {
		final Matrix33D matrix = new Matrix33D(0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement21());
	}
	
	@Test
	public void testGetElement22() {
		final Matrix33D matrix = new Matrix33D(0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement22());
	}
	
	@Test
	public void testGetElement23() {
		final Matrix33D matrix = new Matrix33D(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement23());
	}
	
	@Test
	public void testGetElement31() {
		final Matrix33D matrix = new Matrix33D(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement31());
	}
	
	@Test
	public void testGetElement32() {
		final Matrix33D matrix = new Matrix33D(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D);
		
		assertEquals(1.0D, matrix.getElement32());
	}
	
	@Test
	public void testGetElement33() {
		final Matrix33D matrix = new Matrix33D(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D);
		
		assertEquals(1.0D, matrix.getElement33());
	}
	
	@Test
	public void testHashCode() {
		final Matrix33D a = new Matrix33D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D);
		final Matrix33D b = new Matrix33D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIdentity() {
		final Matrix33D matrix = Matrix33D.identity();
		
		assertEquals(1.0D, matrix.getElement11());
		assertEquals(0.0D, matrix.getElement12());
		assertEquals(0.0D, matrix.getElement13());
		assertEquals(0.0D, matrix.getElement21());
		assertEquals(1.0D, matrix.getElement22());
		assertEquals(0.0D, matrix.getElement23());
		assertEquals(0.0D, matrix.getElement31());
		assertEquals(0.0D, matrix.getElement32());
		assertEquals(1.0D, matrix.getElement33());
	}
	
	@Test
	public void testInverse() {
		final Matrix33D a = new Matrix33D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 4.0D, 3.0D, 2.0D, 1.0D);
		final Matrix33D b = Matrix33D.inverse(a);
		final Matrix33D c = Matrix33D.inverse(b);
		final Matrix33D d = Matrix33D.multiply(a, b);
		final Matrix33D e = Matrix33D.identity();
		
		assertEquals(a, c);
		assertEquals(d, e);
		
		assertThrows(IllegalArgumentException.class, () -> Matrix33D.inverse(new Matrix33D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D)));
		assertThrows(NullPointerException.class, () -> Matrix33D.inverse(null));
	}
	
	@Test
	public void testIsInvertible() {
		final Matrix33D a = new Matrix33D(1.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 1.0D);
		final Matrix33D b = new Matrix33D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D);
		
		assertTrue(a.isInvertible());
		assertFalse(b.isInvertible());
	}
	
	@Test
	public void testMultiply() {
		final Matrix33D a = new Matrix33D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D);
		final Matrix33D b = new Matrix33D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D);
		final Matrix33D c = Matrix33D.multiply(a, b);
		
		assertEquals( 30.0D, c.getElement11());
		assertEquals( 36.0D, c.getElement12());
		assertEquals( 42.0D, c.getElement13());
		assertEquals( 66.0D, c.getElement21());
		assertEquals( 81.0D, c.getElement22());
		assertEquals( 96.0D, c.getElement23());
		assertEquals(102.0D, c.getElement31());
		assertEquals(126.0D, c.getElement32());
		assertEquals(150.0D, c.getElement33());
		
		assertThrows(NullPointerException.class, () -> Matrix33D.multiply(a, null));
		assertThrows(NullPointerException.class, () -> Matrix33D.multiply(null, b));
	}
	
	@Test
	public void testRead() throws IOException {
		final Matrix33D a = new Matrix33D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeDouble(a.getElement11());
		dataOutput.writeDouble(a.getElement12());
		dataOutput.writeDouble(a.getElement13());
		dataOutput.writeDouble(a.getElement21());
		dataOutput.writeDouble(a.getElement22());
		dataOutput.writeDouble(a.getElement23());
		dataOutput.writeDouble(a.getElement31());
		dataOutput.writeDouble(a.getElement32());
		dataOutput.writeDouble(a.getElement33());
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Matrix33D b = Matrix33D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Matrix33D.read(null));
		assertThrows(UncheckedIOException.class, () -> Matrix33D.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testRotate() {
		final Matrix33D matrix = Matrix33D.rotate(AngleD.degrees(0.0D));
		
		assertEquals(+1.0D, matrix.getElement11());
		assertEquals(+0.0D, matrix.getElement12());
		assertEquals(+0.0D, matrix.getElement13());
		assertEquals(-0.0D, matrix.getElement21());
		assertEquals(+1.0D, matrix.getElement22());
		assertEquals(+0.0D, matrix.getElement23());
		assertEquals(+0.0D, matrix.getElement31());
		assertEquals(+0.0D, matrix.getElement32());
		assertEquals(+1.0D, matrix.getElement33());
		
		assertThrows(NullPointerException.class, () -> Matrix33D.rotate(null));
	}
	
	@Test
	public void testScaleDouble() {
		final Matrix33D matrix = Matrix33D.scale(2.0D);
		
		assertEquals(2.0D, matrix.getElement11());
		assertEquals(0.0D, matrix.getElement12());
		assertEquals(0.0D, matrix.getElement13());
		assertEquals(0.0D, matrix.getElement21());
		assertEquals(2.0D, matrix.getElement22());
		assertEquals(0.0D, matrix.getElement23());
		assertEquals(0.0D, matrix.getElement31());
		assertEquals(0.0D, matrix.getElement32());
		assertEquals(1.0D, matrix.getElement33());
	}
	
	@Test
	public void testScaleDoubleDouble() {
		final Matrix33D matrix = Matrix33D.scale(2.0D, 3.0D);
		
		assertEquals(2.0D, matrix.getElement11());
		assertEquals(0.0D, matrix.getElement12());
		assertEquals(0.0D, matrix.getElement13());
		assertEquals(0.0D, matrix.getElement21());
		assertEquals(3.0D, matrix.getElement22());
		assertEquals(0.0D, matrix.getElement23());
		assertEquals(0.0D, matrix.getElement31());
		assertEquals(0.0D, matrix.getElement32());
		assertEquals(1.0D, matrix.getElement33());
	}
	
	@Test
	public void testScaleVector2D() {
		final Matrix33D matrix = Matrix33D.scale(new Vector2D(2.0D, 3.0D));
		
		assertEquals(2.0D, matrix.getElement11());
		assertEquals(0.0D, matrix.getElement12());
		assertEquals(0.0D, matrix.getElement13());
		assertEquals(0.0D, matrix.getElement21());
		assertEquals(3.0D, matrix.getElement22());
		assertEquals(0.0D, matrix.getElement23());
		assertEquals(0.0D, matrix.getElement31());
		assertEquals(0.0D, matrix.getElement32());
		assertEquals(1.0D, matrix.getElement33());
		
		assertThrows(NullPointerException.class, () -> Matrix33D.scale(null));
	}
	
	@Test
	public void testToString() {
		final Matrix33D matrix = new Matrix33D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D);
		
		assertEquals("new Matrix33D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D)", matrix.toString());
	}
	
	@Test
	public void testTranslateDoubleDouble() {
		final Matrix33D matrix = Matrix33D.translate(2.0D, 3.0D);
		
		assertEquals(1.0D, matrix.getElement11());
		assertEquals(0.0D, matrix.getElement12());
		assertEquals(2.0D, matrix.getElement13());
		assertEquals(0.0D, matrix.getElement21());
		assertEquals(1.0D, matrix.getElement22());
		assertEquals(3.0D, matrix.getElement23());
		assertEquals(0.0D, matrix.getElement31());
		assertEquals(0.0D, matrix.getElement32());
		assertEquals(1.0D, matrix.getElement33());
	}
	
	@Test
	public void testTranslatePoint2D() {
		final Matrix33D matrix = Matrix33D.translate(new Point2D(2.0D, 3.0D));
		
		assertEquals(1.0D, matrix.getElement11());
		assertEquals(0.0D, matrix.getElement12());
		assertEquals(2.0D, matrix.getElement13());
		assertEquals(0.0D, matrix.getElement21());
		assertEquals(1.0D, matrix.getElement22());
		assertEquals(3.0D, matrix.getElement23());
		assertEquals(0.0D, matrix.getElement31());
		assertEquals(0.0D, matrix.getElement32());
		assertEquals(1.0D, matrix.getElement33());
		
		assertThrows(NullPointerException.class, () -> Matrix33D.translate(null));
	}
	
	@Test
	public void testTranspose() {
		final Matrix33D a = new Matrix33D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D);
		final Matrix33D b = Matrix33D.transpose(a);
		
		assertEquals(1.0D, b.getElement11());
		assertEquals(4.0D, b.getElement12());
		assertEquals(7.0D, b.getElement13());
		assertEquals(2.0D, b.getElement21());
		assertEquals(5.0D, b.getElement22());
		assertEquals(8.0D, b.getElement23());
		assertEquals(3.0D, b.getElement31());
		assertEquals(6.0D, b.getElement32());
		assertEquals(9.0D, b.getElement33());
		
		assertThrows(NullPointerException.class, () -> Matrix33D.transpose(null));
	}
	
	@Test
	public void testWrite() {
		final Matrix33D a = new Matrix33D(1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Matrix33D b = Matrix33D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}