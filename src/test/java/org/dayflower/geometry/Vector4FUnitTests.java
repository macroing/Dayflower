/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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
public final class Vector4FUnitTests {
	public Vector4FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAdd() {
		final Vector4F a = new Vector4F(1.0F, 2.0F, 3.0F, 4.0F);
		final Vector4F b = new Vector4F(2.0F, 3.0F, 4.0F, 5.0F);
		final Vector4F c = Vector4F.add(a, b);
		
		assertEquals(3.0F, c.x);
		assertEquals(5.0F, c.y);
		assertEquals(7.0F, c.z);
		assertEquals(9.0F, c.w);
		
		assertThrows(NullPointerException.class, () -> Vector4F.add(a, null));
		assertThrows(NullPointerException.class, () -> Vector4F.add(null, b));
	}
	
	@Test
	public void testConstructor() {
		final Vector4F vector = new Vector4F();
		
		assertEquals(0.0F, vector.x);
		assertEquals(0.0F, vector.y);
		assertEquals(0.0F, vector.z);
		assertEquals(1.0F, vector.w);
	}
	
	@Test
	public void testConstructorFloatFloatFloat() {
		final Vector4F vector = new Vector4F(2.0F, 3.0F, 4.0F);
		
		assertEquals(2.0F, vector.x);
		assertEquals(3.0F, vector.y);
		assertEquals(4.0F, vector.z);
		assertEquals(1.0F, vector.w);
	}
	
	@Test
	public void testConstructorFloatFloatFloatFloat() {
		final Vector4F vector = new Vector4F(1.0F, 2.0F, 3.0F, 4.0F);
		
		assertEquals(1.0F, vector.x);
		assertEquals(2.0F, vector.y);
		assertEquals(3.0F, vector.z);
		assertEquals(4.0F, vector.w);
	}
	
	@Test
	public void testConstructorPoint4F() {
		final Vector4F vector = new Vector4F(new Point4F(1.0F, 2.0F, 3.0F, 4.0F));
		
		assertEquals(1.0F, vector.x);
		assertEquals(2.0F, vector.y);
		assertEquals(3.0F, vector.z);
		assertEquals(4.0F, vector.w);
		
		assertThrows(NullPointerException.class, () -> new Vector4F(null));
	}
	
	@Test
	public void testDivide() {
		final Vector4F a = new Vector4F(2.0F, 4.0F, 8.0F, 16.0F);
		final Vector4F b = Vector4F.divide(a, 2.0F);
		final Vector4F c = Vector4F.divide(a, Float.NaN);
		
		assertEquals(1.0F, b.x);
		assertEquals(2.0F, b.y);
		assertEquals(4.0F, b.z);
		assertEquals(8.0F, b.w);
		
		assertEquals(0.0F, c.x);
		assertEquals(0.0F, c.y);
		assertEquals(0.0F, c.z);
		assertEquals(0.0F, c.w);
		
		assertThrows(NullPointerException.class, () -> Vector4F.divide(null, 2.0F));
	}
	
	@Test
	public void testDotProduct() {
		final Vector4F a = new Vector4F(+1.0F, +0.0F, +0.0F, +0.0F);
		final Vector4F b = new Vector4F(+1.0F, +0.0F, +0.0F, +0.0F);
		final Vector4F c = new Vector4F(+0.0F, -1.0F, +0.0F, +0.0F);
		final Vector4F d = new Vector4F(-1.0F, +0.0F, +0.0F, +0.0F);
		
		assertEquals(+1.0F, Vector4F.dotProduct(a, b));
		assertEquals(+0.0F, Vector4F.dotProduct(a, c));
		assertEquals(-1.0F, Vector4F.dotProduct(a, d));
		
		assertThrows(NullPointerException.class, () -> Vector4F.dotProduct(a, null));
		assertThrows(NullPointerException.class, () -> Vector4F.dotProduct(null, b));
	}
	
	@Test
	public void testEquals() {
		final Vector4F a = new Vector4F(0.0F, 1.0F, 2.0F, 3.0F);
		final Vector4F b = new Vector4F(0.0F, 1.0F, 2.0F, 3.0F);
		final Vector4F c = new Vector4F(0.0F, 1.0F, 2.0F, 4.0F);
		final Vector4F d = new Vector4F(0.0F, 1.0F, 4.0F, 3.0F);
		final Vector4F e = new Vector4F(0.0F, 4.0F, 2.0F, 3.0F);
		final Vector4F f = new Vector4F(4.0F, 1.0F, 2.0F, 3.0F);
		final Vector4F g = null;
		
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
	public void testHashCode() {
		final Vector4F a = new Vector4F(1.0F, 2.0F, 3.0F, 4.0F);
		final Vector4F b = new Vector4F(1.0F, 2.0F, 3.0F, 4.0F);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testLength() {
		final Vector4F a = new Vector4F(4.0F, 0.0F, 0.0F, 0.0F);
		final Vector4F b = new Vector4F(0.0F, 4.0F, 0.0F, 0.0F);
		final Vector4F c = new Vector4F(0.0F, 0.0F, 4.0F, 0.0F);
		final Vector4F d = new Vector4F(0.0F, 0.0F, 0.0F, 4.0F);
		
		assertEquals(4.0F, a.length());
		assertEquals(4.0F, b.length());
		assertEquals(4.0F, c.length());
		assertEquals(4.0F, d.length());
	}
	
	@Test
	public void testLengthSquared() {
		final Vector4F vector = new Vector4F(2.0F, 4.0F, 8.0F, 16.0F);
		
		assertEquals(340.0F, vector.lengthSquared());
	}
	
	@Test
	public void testMultiply() {
		final Vector4F a = new Vector4F(1.0F, 2.0F, 3.0F, 4.0F);
		final Vector4F b = Vector4F.multiply(a, 2.0F);
		
		assertEquals(2.0F, b.x);
		assertEquals(4.0F, b.y);
		assertEquals(6.0F, b.z);
		assertEquals(8.0F, b.w);
		
		assertThrows(NullPointerException.class, () -> Vector4F.multiply(null, 2.0F));
	}
	
	@Test
	public void testNegate() {
		final Vector4F a = new Vector4F(1.0F, 2.0F, 3.0F, 4.0F);
		final Vector4F b = Vector4F.negate(a);
		final Vector4F c = Vector4F.negate(b);
		
		assertEquals(-1.0F, b.x);
		assertEquals(-2.0F, b.y);
		assertEquals(-3.0F, b.z);
		assertEquals(-4.0F, b.w);
		
		assertEquals(+1.0F, c.x);
		assertEquals(+2.0F, c.y);
		assertEquals(+3.0F, c.z);
		assertEquals(+4.0F, c.w);
		
		assertThrows(NullPointerException.class, () -> Vector4F.negate(null));
	}
	
	@Test
	public void testNormalize() {
		final Vector4F a = new Vector4F(+1.0F, +0.0F, +0.0F, +0.0F);
		final Vector4F b = Vector4F.normalize(a);
		final Vector4F c = new Vector4F(+0.0F, +1.0F, +0.0F, +0.0F);
		final Vector4F d = Vector4F.normalize(c);
		final Vector4F e = new Vector4F(+0.0F, +0.0F, +1.0F, +0.0F);
		final Vector4F f = Vector4F.normalize(e);
		final Vector4F g = new Vector4F(+0.0F, +0.0F, +0.0F, +1.0F);
		final Vector4F h = Vector4F.normalize(g);
		final Vector4F i = new Vector4F(+1.0F, +1.0F, +1.0F, +1.0F);
		final Vector4F j = Vector4F.normalize(i);
		final Vector4F k = Vector4F.normalize(j);
		final Vector4F l = new Vector4F(+0.4F, +0.4F, +0.4F, +0.4F);
		final Vector4F m = Vector4F.normalize(l);
		final Vector4F n = Vector4F.normalize(m);
		
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
		
		assertThrows(NullPointerException.class, () -> Vector4F.normalize(null));
	}
	
	@Test
	public void testRead() throws IOException {
		final Vector4F a = new Vector4F(1.0F, 2.0F, 3.0F, 4.0F);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeFloat(a.x);
		dataOutput.writeFloat(a.y);
		dataOutput.writeFloat(a.z);
		dataOutput.writeFloat(a.w);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Vector4F b = Vector4F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Vector4F.read(null));
		assertThrows(UncheckedIOException.class, () -> Vector4F.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testSubtract() {
		final Vector4F a = new Vector4F(3.0F, 5.0F, 7.0F, 9.0F);
		final Vector4F b = new Vector4F(2.0F, 3.0F, 4.0F, 5.0F);
		final Vector4F c = Vector4F.subtract(a, b);
		
		assertEquals(1.0F, c.x);
		assertEquals(2.0F, c.y);
		assertEquals(3.0F, c.z);
		assertEquals(4.0F, c.w);
		
		assertThrows(NullPointerException.class, () -> Vector4F.subtract(a, null));
		assertThrows(NullPointerException.class, () -> Vector4F.subtract(null, b));
	}
	
	@Test
	public void testToArray() {
		final Vector4F vector = new Vector4F(1.0F, 2.0F, 3.0F, 4.0F);
		
		final float[] array = vector.toArray();
		
		assertNotNull(array);
		
		assertEquals(4, array.length);
		
		assertEquals(1.0F, array[0]);
		assertEquals(2.0F, array[1]);
		assertEquals(3.0F, array[2]);
		assertEquals(4.0F, array[3]);
	}
	
	@Test
	public void testToString() {
		final Vector4F vector = new Vector4F(1.0F, 2.0F, 3.0F, 4.0F);
		
		assertEquals("new Vector4F(1.0F, 2.0F, 3.0F, 4.0F)", vector.toString());
	}
	
	@Test
	public void testWrite() {
		final Vector4F a = new Vector4F(1.0F, 2.0F, 3.0F, 4.0F);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Vector4F b = Vector4F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}