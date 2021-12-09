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
public final class Vector4DUnitTests {
	public Vector4DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAdd() {
		final Vector4D a = new Vector4D(1.0D, 2.0D, 3.0D, 4.0D);
		final Vector4D b = new Vector4D(2.0D, 3.0D, 4.0D, 5.0D);
		final Vector4D c = Vector4D.add(a, b);
		
		assertEquals(3.0D, c.getComponent1());
		assertEquals(5.0D, c.getComponent2());
		assertEquals(7.0D, c.getComponent3());
		assertEquals(9.0D, c.getComponent4());
		
		assertThrows(NullPointerException.class, () -> Vector4D.add(a, null));
		assertThrows(NullPointerException.class, () -> Vector4D.add(null, b));
	}
	
	@Test
	public void testConstructor() {
		final Vector4D vector = new Vector4D();
		
		assertEquals(0.0D, vector.getComponent1());
		assertEquals(0.0D, vector.getComponent2());
		assertEquals(0.0D, vector.getComponent3());
		assertEquals(1.0D, vector.getComponent4());
	}
	
	@Test
	public void testConstructorDoubleDoubleDouble() {
		final Vector4D vector = new Vector4D(2.0D, 3.0D, 4.0D);
		
		assertEquals(2.0D, vector.getComponent1());
		assertEquals(3.0D, vector.getComponent2());
		assertEquals(4.0D, vector.getComponent3());
		assertEquals(1.0D, vector.getComponent4());
	}
	
	@Test
	public void testConstructorDoubleDoubleDoubleDouble() {
		final Vector4D vector = new Vector4D(1.0D, 2.0D, 3.0D, 4.0D);
		
		assertEquals(1.0D, vector.getComponent1());
		assertEquals(2.0D, vector.getComponent2());
		assertEquals(3.0D, vector.getComponent3());
		assertEquals(4.0D, vector.getComponent4());
	}
	
	@Test
	public void testConstructorPoint4D() {
		final Vector4D vector = new Vector4D(new Point4D(1.0D, 2.0D, 3.0D, 4.0D));
		
		assertEquals(1.0D, vector.getComponent1());
		assertEquals(2.0D, vector.getComponent2());
		assertEquals(3.0D, vector.getComponent3());
		assertEquals(4.0D, vector.getComponent4());
		
		assertThrows(NullPointerException.class, () -> new Vector4D(null));
	}
	
	@Test
	public void testDivide() {
		final Vector4D a = new Vector4D(2.0D, 4.0D, 8.0D, 16.0D);
		final Vector4D b = Vector4D.divide(a, 2.0D);
		final Vector4D c = Vector4D.divide(a, Double.NaN);
		
		assertEquals(1.0D, b.getComponent1());
		assertEquals(2.0D, b.getComponent2());
		assertEquals(4.0D, b.getComponent3());
		assertEquals(8.0D, b.getComponent4());
		
		assertEquals(0.0D, c.getComponent1());
		assertEquals(0.0D, c.getComponent2());
		assertEquals(0.0D, c.getComponent3());
		assertEquals(0.0D, c.getComponent4());
		
		assertThrows(NullPointerException.class, () -> Vector4D.divide(null, 2.0D));
	}
	
	@Test
	public void testDotProduct() {
		final Vector4D a = new Vector4D(+1.0D, +0.0D, +0.0D, +0.0D);
		final Vector4D b = new Vector4D(+1.0D, +0.0D, +0.0D, +0.0D);
		final Vector4D c = new Vector4D(+0.0D, -1.0D, +0.0D, +0.0D);
		final Vector4D d = new Vector4D(-1.0D, +0.0D, +0.0D, +0.0D);
		
		assertEquals(+1.0D, Vector4D.dotProduct(a, b));
		assertEquals(+0.0D, Vector4D.dotProduct(a, c));
		assertEquals(-1.0D, Vector4D.dotProduct(a, d));
		
		assertThrows(NullPointerException.class, () -> Vector4D.dotProduct(a, null));
		assertThrows(NullPointerException.class, () -> Vector4D.dotProduct(null, b));
	}
	
	@Test
	public void testEquals() {
		final Vector4D a = new Vector4D(0.0D, 1.0D, 2.0D, 3.0D);
		final Vector4D b = new Vector4D(0.0D, 1.0D, 2.0D, 3.0D);
		final Vector4D c = new Vector4D(0.0D, 1.0D, 2.0D, 4.0D);
		final Vector4D d = new Vector4D(0.0D, 1.0D, 4.0D, 3.0D);
		final Vector4D e = new Vector4D(0.0D, 4.0D, 2.0D, 3.0D);
		final Vector4D f = new Vector4D(4.0D, 1.0D, 2.0D, 3.0D);
		final Vector4D g = null;
		
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
	public void testGetComponent1() {
		final Vector4D vector = new Vector4D(2.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(2.0D, vector.getComponent1());
	}
	
	@Test
	public void testGetComponent2() {
		final Vector4D vector = new Vector4D(0.0D, 2.0D, 0.0D, 0.0D);
		
		assertEquals(2.0D, vector.getComponent2());
	}
	
	@Test
	public void testGetComponent3() {
		final Vector4D vector = new Vector4D(0.0D, 0.0D, 2.0D, 0.0D);
		
		assertEquals(2.0D, vector.getComponent3());
	}
	
	@Test
	public void testGetComponent4() {
		final Vector4D vector = new Vector4D(0.0D, 0.0D, 0.0D, 2.0D);
		
		assertEquals(2.0D, vector.getComponent4());
	}
	
	@Test
	public void testGetW() {
		final Vector4D vector = new Vector4D(0.0D, 0.0D, 0.0D, 2.0D);
		
		assertEquals(2.0D, vector.getW());
	}
	
	@Test
	public void testGetX() {
		final Vector4D vector = new Vector4D(2.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(2.0D, vector.getX());
	}
	
	@Test
	public void testGetY() {
		final Vector4D vector = new Vector4D(0.0D, 2.0D, 0.0D, 0.0D);
		
		assertEquals(2.0D, vector.getY());
	}
	
	@Test
	public void testGetZ() {
		final Vector4D vector = new Vector4D(0.0D, 0.0D, 2.0D, 0.0D);
		
		assertEquals(2.0D, vector.getZ());
	}
	
	@Test
	public void testHashCode() {
		final Vector4D a = new Vector4D(1.0D, 2.0D, 3.0D, 4.0D);
		final Vector4D b = new Vector4D(1.0D, 2.0D, 3.0D, 4.0D);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testLength() {
		final Vector4D a = new Vector4D(4.0D, 0.0D, 0.0D, 0.0D);
		final Vector4D b = new Vector4D(0.0D, 4.0D, 0.0D, 0.0D);
		final Vector4D c = new Vector4D(0.0D, 0.0D, 4.0D, 0.0D);
		final Vector4D d = new Vector4D(0.0D, 0.0D, 0.0D, 4.0D);
		
		assertEquals(4.0D, a.length());
		assertEquals(4.0D, b.length());
		assertEquals(4.0D, c.length());
		assertEquals(4.0D, d.length());
	}
	
	@Test
	public void testLengthSquared() {
		final Vector4D vector = new Vector4D(2.0D, 4.0D, 8.0D, 16.0D);
		
		assertEquals(340.0D, vector.lengthSquared());
	}
	
	@Test
	public void testMultiply() {
		final Vector4D a = new Vector4D(1.0D, 2.0D, 3.0D, 4.0D);
		final Vector4D b = Vector4D.multiply(a, 2.0D);
		
		assertEquals(2.0D, b.getComponent1());
		assertEquals(4.0D, b.getComponent2());
		assertEquals(6.0D, b.getComponent3());
		assertEquals(8.0D, b.getComponent4());
		
		assertThrows(NullPointerException.class, () -> Vector4D.multiply(null, 2.0D));
	}
	
	@Test
	public void testNegate() {
		final Vector4D a = new Vector4D(1.0D, 2.0D, 3.0D, 4.0D);
		final Vector4D b = Vector4D.negate(a);
		final Vector4D c = Vector4D.negate(b);
		
		assertEquals(-1.0D, b.getComponent1());
		assertEquals(-2.0D, b.getComponent2());
		assertEquals(-3.0D, b.getComponent3());
		assertEquals(-4.0D, b.getComponent4());
		
		assertEquals(+1.0D, c.getComponent1());
		assertEquals(+2.0D, c.getComponent2());
		assertEquals(+3.0D, c.getComponent3());
		assertEquals(+4.0D, c.getComponent4());
		
		assertThrows(NullPointerException.class, () -> Vector4D.negate(null));
	}
	
	@Test
	public void testNormalize() {
		final Vector4D a = new Vector4D(+1.0D, +0.0D, +0.0D, +0.0D);
		final Vector4D b = Vector4D.normalize(a);
		final Vector4D c = new Vector4D(+0.0D, +1.0D, +0.0D, +0.0D);
		final Vector4D d = Vector4D.normalize(c);
		final Vector4D e = new Vector4D(+0.0D, +0.0D, +1.0D, +0.0D);
		final Vector4D f = Vector4D.normalize(e);
		final Vector4D g = new Vector4D(+0.0D, +0.0D, +0.0D, +1.0D);
		final Vector4D h = Vector4D.normalize(g);
		final Vector4D i = new Vector4D(+1.0D, +1.0D, +1.0D, +1.0D);
		final Vector4D j = Vector4D.normalize(i);
		final Vector4D k = Vector4D.normalize(j);
		final Vector4D l = new Vector4D(+0.4D, +0.4D, +0.4D, +0.4D);
		final Vector4D m = Vector4D.normalize(l);
		final Vector4D n = Vector4D.normalize(m);
		
		assertEquals(a, b);
		assertEquals(c, d);
		assertEquals(e, f);
		assertEquals(g, h);
		assertEquals(j, k);
		assertEquals(m, n);
		
		assertTrue(a == b);
		assertTrue(c == d);
		assertTrue(e == f);
		assertTrue(j == k);
		assertTrue(m == n);
		
		assertThrows(NullPointerException.class, () -> Vector4D.normalize(null));
	}
	
	@Test
	public void testRead() throws IOException {
		final Vector4D a = new Vector4D(1.0D, 2.0D, 3.0D, 4.0D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeDouble(a.getComponent1());
		dataOutput.writeDouble(a.getComponent2());
		dataOutput.writeDouble(a.getComponent3());
		dataOutput.writeDouble(a.getComponent4());
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Vector4D b = Vector4D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Vector4D.read(null));
		assertThrows(UncheckedIOException.class, () -> Vector4D.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testSubtract() {
		final Vector4D a = new Vector4D(3.0D, 5.0D, 7.0D, 9.0D);
		final Vector4D b = new Vector4D(2.0D, 3.0D, 4.0D, 5.0D);
		final Vector4D c = Vector4D.subtract(a, b);
		
		assertEquals(1.0D, c.getComponent1());
		assertEquals(2.0D, c.getComponent2());
		assertEquals(3.0D, c.getComponent3());
		assertEquals(4.0D, c.getComponent4());
		
		assertThrows(NullPointerException.class, () -> Vector4D.subtract(a, null));
		assertThrows(NullPointerException.class, () -> Vector4D.subtract(null, b));
	}
	
	@Test
	public void testToArray() {
		final Vector4D vector = new Vector4D(1.0D, 2.0D, 3.0D, 4.0D);
		
		final double[] array = vector.toArray();
		
		assertNotNull(array);
		
		assertEquals(4, array.length);
		
		assertEquals(1.0D, array[0]);
		assertEquals(2.0D, array[1]);
		assertEquals(3.0D, array[2]);
		assertEquals(4.0D, array[3]);
	}
	
	@Test
	public void testToString() {
		final Vector4D vector = new Vector4D(1.0D, 2.0D, 3.0D, 4.0D);
		
		assertEquals("new Vector4D(1.0D, 2.0D, 3.0D, 4.0D)", vector.toString());
	}
	
	@Test
	public void testWrite() {
		final Vector4D a = new Vector4D(1.0D, 2.0D, 3.0D, 4.0D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Vector4D b = Vector4D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}