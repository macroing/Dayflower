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

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class Vector2DUnitTests {
	public Vector2DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAbsolute() {
		final Vector2D a = Vector2D.absolute(new Vector2D(+1.0D, +2.0D));
		final Vector2D b = Vector2D.absolute(new Vector2D(-1.0D, -2.0D));
		
		assertEquals(1.0D, a.getComponent1());
		assertEquals(2.0D, a.getComponent2());
		
		assertEquals(1.0D, b.getComponent1());
		assertEquals(2.0D, b.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2D.absolute(null));
	}
	
	@Test
	public void testAddVector2DVector2D() {
		final Vector2D a = new Vector2D(1.0D, 2.0D);
		final Vector2D b = new Vector2D(2.0D, 3.0D);
		final Vector2D c = Vector2D.add(a, b);
		
		assertEquals(3.0D, c.getComponent1());
		assertEquals(5.0D, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2D.add(a, null));
		assertThrows(NullPointerException.class, () -> Vector2D.add(null, b));
	}
	
	@Test
	public void testAddVector2DVector2DVector2D() {
		final Vector2D a = new Vector2D(1.0D, 2.0D);
		final Vector2D b = new Vector2D(2.0D, 3.0D);
		final Vector2D c = new Vector2D(3.0D, 4.0D);
		final Vector2D d = Vector2D.add(a, b, c);
		
		assertEquals(6.0D, d.getComponent1());
		assertEquals(9.0D, d.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2D.add(a, b, null));
		assertThrows(NullPointerException.class, () -> Vector2D.add(a, null, c));
		assertThrows(NullPointerException.class, () -> Vector2D.add(null, b, c));
	}
	
	@Test
	public void testClearCacheAndGetCacheSizeAndGetCached() {
		assertEquals(0, Vector2D.getCacheSize());
		
		final Vector2D a = new Vector2D(1.0D, 2.0D);
		final Vector2D b = new Vector2D(1.0D, 2.0D);
		final Vector2D c = Vector2D.getCached(a);
		final Vector2D d = Vector2D.getCached(b);
		
		assertThrows(NullPointerException.class, () -> Vector2D.getCached(null));
		
		assertEquals(1, Vector2D.getCacheSize());
		
		Vector2D.clearCache();
		
		assertEquals(0, Vector2D.getCacheSize());
		
		assertTrue(a != b);
		assertTrue(a == c);
		assertTrue(a == d);
		
		assertTrue(b != a);
		assertTrue(b != c);
		assertTrue(b != d);
	}
	
	@Test
	public void testConstants() {
		assertEquals(new Vector2D(Double.NaN, Double.NaN), Vector2D.NaN);
		assertEquals(new Vector2D(0.0D, 0.0D), Vector2D.ZERO);
	}
	
	@Test
	public void testConstructor() {
		final Vector2D vector = new Vector2D();
		
		assertEquals(0.0D, vector.getComponent1());
		assertEquals(0.0D, vector.getComponent2());
	}
	
	@Test
	public void testConstructorDouble() {
		final Vector2D vector = new Vector2D(1.0D);
		
		assertEquals(1.0D, vector.getComponent1());
		assertEquals(1.0D, vector.getComponent2());
	}
	
	@Test
	public void testConstructorDoubleDouble() {
		final Vector2D vector = new Vector2D(1.0D, 2.0D);
		
		assertEquals(1.0D, vector.getComponent1());
		assertEquals(2.0D, vector.getComponent2());
	}
	
	@Test
	public void testConstructorPoint2D() {
		final Vector2D vector = new Vector2D(new Point2D(1.0D, 2.0D));
		
		assertEquals(1.0D, vector.getComponent1());
		assertEquals(2.0D, vector.getComponent2());
		
		assertThrows(NullPointerException.class, () -> new Vector2D((Point2D)(null)));
	}
	
	@Test
	public void testConstructorPoint3D() {
		final Vector2D vector = new Vector2D(new Point3D(1.0D, 2.0D, 3.0D));
		
		assertEquals(1.0D, vector.getComponent1());
		assertEquals(2.0D, vector.getComponent2());
		
		assertThrows(NullPointerException.class, () -> new Vector2D((Point3D)(null)));
	}
	
	@Test
	public void testDirection() {
		final Vector2D vector = Vector2D.direction(new Point2D(1.0D, 2.0D), new Point2D(2.0D, 3.0D));
		
		assertEquals(1.0D, vector.getComponent1());
		assertEquals(1.0D, vector.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2D.direction(new Point2D(1.0D, 2.0D), null));
		assertThrows(NullPointerException.class, () -> Vector2D.direction(null, new Point2D(2.0D, 3.0D)));
	}
	
	@Test
	public void testEquals() {
		final Vector2D a = new Vector2D(0.0D, 1.0D);
		final Vector2D b = new Vector2D(0.0D, 1.0D);
		final Vector2D c = new Vector2D(0.0D, 2.0D);
		final Vector2D d = new Vector2D(2.0D, 1.0D);
		final Vector2D e = null;
		
		assertEquals(a, a);
		assertEquals(a, b);
		assertEquals(b, a);
		assertNotEquals(a, c);
		assertNotEquals(c, a);
		assertNotEquals(a, d);
		assertNotEquals(d, a);
		assertNotEquals(a, e);
		assertNotEquals(e, a);
	}
	
	@Test
	public void testGetComponent1() {
		final Vector2D vector = new Vector2D(2.0D, 0.0D);
		
		assertEquals(2.0D, vector.getComponent1());
	}
	
	@Test
	public void testGetComponent2() {
		final Vector2D vector = new Vector2D(0.0D, 2.0D);
		
		assertEquals(2.0D, vector.getComponent2());
	}
	
	@Test
	public void testGetU() {
		final Vector2D vector = new Vector2D(2.0D, 0.0D);
		
		assertEquals(2.0D, vector.getU());
	}
	
	@Test
	public void testGetV() {
		final Vector2D vector = new Vector2D(0.0D, 2.0D);
		
		assertEquals(2.0D, vector.getV());
	}
	
	@Test
	public void testGetX() {
		final Vector2D vector = new Vector2D(2.0D, 0.0D);
		
		assertEquals(2.0D, vector.getX());
	}
	
	@Test
	public void testGetY() {
		final Vector2D vector = new Vector2D(0.0D, 2.0D);
		
		assertEquals(2.0D, vector.getY());
	}
	
	@Test
	public void testHashCode() {
		final Vector2D a = new Vector2D(0.5D, 1.0D);
		final Vector2D b = new Vector2D(0.5D, 1.0D);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIsUnitVector() {
		assertTrue(new Vector2D(+1.0D, +0.0D).isUnitVector());
		assertTrue(new Vector2D(+0.0D, +1.0D).isUnitVector());
		assertTrue(new Vector2D(-1.0D, -0.0D).isUnitVector());
		assertTrue(new Vector2D(-0.0D, -1.0D).isUnitVector());
		assertTrue(Vector2D.normalize(new Vector2D(1.0D, 1.0D)).isUnitVector());
		
		assertFalse(new Vector2D(1.0D, 1.0D).isUnitVector());
	}
	
	@Test
	public void testLength() {
		final Vector2D a = new Vector2D(4.0D, 0.0D);
		final Vector2D b = new Vector2D(0.0D, 4.0D);
		
		assertEquals(4.0D, a.length());
		assertEquals(4.0D, b.length());
	}
	
	@Test
	public void testLengthSquared() {
		final Vector2D vector = new Vector2D(2.0D, 4.0D);
		
		assertEquals(20.0D, vector.lengthSquared());
	}
	
	@Test
	public void testNormalize() {
		final Vector2D a = new Vector2D(1.0D, 0.0D);
		final Vector2D b = Vector2D.normalize(a);
		final Vector2D c = new Vector2D(0.0D, 1.0D);
		final Vector2D d = Vector2D.normalize(c);
		final Vector2D e = new Vector2D(1.0D, 1.0D);
		final Vector2D f = Vector2D.normalize(e);
		final Vector2D g = Vector2D.normalize(f);
		
		assertEquals(a, b);
		assertEquals(c, d);
		assertEquals(f, g);
		
		assertTrue(a == b);
		assertTrue(c == d);
		assertTrue(f == g);
		
		assertTrue(a.isUnitVector());
		assertTrue(c.isUnitVector());
		assertTrue(f.isUnitVector());
		
		assertFalse(e.isUnitVector());
		
		assertThrows(NullPointerException.class, () -> Vector2D.normalize(null));
	}
	
	@Test
	public void testRead() throws IOException {
		final Vector2D a = new Vector2D(1.0D, 0.5D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeDouble(a.getComponent1());
		dataOutput.writeDouble(a.getComponent2());
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Vector2D b = Vector2D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Vector2D.read(null));
		assertThrows(UncheckedIOException.class, () -> Vector2D.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testToArray() {
		final Vector2D vector = new Vector2D(1.0D, 2.0D);
		
		final double[] array = vector.toArray();
		
		assertNotNull(array);
		
		assertEquals(2, array.length);
		
		assertEquals(1.0D, array[0]);
		assertEquals(2.0D, array[1]);
	}
	
	@Test
	public void testToString() {
		final Vector2D vector = new Vector2D(0.5D, 1.0D);
		
		assertEquals("new Vector2D(0.5D, 1.0D)", vector.toString());
	}
	
	@Test
	public void testU() {
		final Vector2D vector = Vector2D.u();
		
		assertEquals(1.0D, vector.getU());
		assertEquals(0.0D, vector.getV());
	}
	
	@Test
	public void testUDouble() {
		final Vector2D vector = Vector2D.u(2.0D);
		
		assertEquals(2.0D, vector.getU());
		assertEquals(0.0D, vector.getV());
	}
	
	@Test
	public void testV() {
		final Vector2D vector = Vector2D.v();
		
		assertEquals(0.0D, vector.getU());
		assertEquals(1.0D, vector.getV());
	}
	
	@Test
	public void testVDouble() {
		final Vector2D vector = Vector2D.v(2.0D);
		
		assertEquals(0.0D, vector.getU());
		assertEquals(2.0D, vector.getV());
	}
	
	@Test
	public void testWrite() {
		final Vector2D a = new Vector2D(1.0D, 0.5D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Vector2D b = Vector2D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
	}
	
	@Test
	public void testX() {
		final Vector2D vector = Vector2D.x();
		
		assertEquals(1.0D, vector.getX());
		assertEquals(0.0D, vector.getY());
	}
	
	@Test
	public void testXDouble() {
		final Vector2D vector = Vector2D.x(2.0D);
		
		assertEquals(2.0D, vector.getX());
		assertEquals(0.0D, vector.getY());
	}
	
	@Test
	public void testY() {
		final Vector2D vector = Vector2D.y();
		
		assertEquals(0.0D, vector.getX());
		assertEquals(1.0D, vector.getY());
	}
	
	@Test
	public void testYDouble() {
		final Vector2D vector = Vector2D.y(2.0D);
		
		assertEquals(0.0D, vector.getX());
		assertEquals(2.0D, vector.getY());
	}
}