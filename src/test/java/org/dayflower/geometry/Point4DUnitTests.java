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

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class Point4DUnitTests {
	public Point4DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testClearCacheAndGetCacheSizeAndGetCached() {
		assertEquals(0, Point4D.getCacheSize());
		
		final Point4D a = new Point4D(1.0D, 2.0D, 3.0D, 4.0D);
		final Point4D b = new Point4D(1.0D, 2.0D, 3.0D, 4.0D);
		final Point4D c = Point4D.getCached(a);
		final Point4D d = Point4D.getCached(b);
		
		assertThrows(NullPointerException.class, () -> Point4D.getCached(null));
		
		assertEquals(1, Point4D.getCacheSize());
		
		Point4D.clearCache();
		
		assertEquals(0, Point4D.getCacheSize());
		
		assertTrue(a != b);
		assertTrue(a == c);
		assertTrue(a == d);
		
		assertTrue(b != a);
		assertTrue(b != c);
		assertTrue(b != d);
	}
	
	@Test
	public void testConstructor() {
		final Point4D point = new Point4D();
		
		assertEquals(0.0D, point.getComponent1());
		assertEquals(0.0D, point.getComponent2());
		assertEquals(0.0D, point.getComponent3());
		assertEquals(1.0D, point.getComponent4());
	}
	
	@Test
	public void testConstructorDouble() {
		final Point4D point = new Point4D(2.0D);
		
		assertEquals(2.0D, point.getComponent1());
		assertEquals(2.0D, point.getComponent2());
		assertEquals(2.0D, point.getComponent3());
		assertEquals(1.0D, point.getComponent4());
	}
	
	@Test
	public void testConstructorDoubleDoubleDouble() {
		final Point4D point = new Point4D(2.0D, 3.0D, 4.0D);
		
		assertEquals(2.0D, point.getComponent1());
		assertEquals(3.0D, point.getComponent2());
		assertEquals(4.0D, point.getComponent3());
		assertEquals(1.0D, point.getComponent4());
	}
	
	@Test
	public void testConstructorDoubleDoubleDoubleDouble() {
		final Point4D point = new Point4D(1.0D, 2.0D, 3.0D, 4.0D);
		
		assertEquals(1.0D, point.getComponent1());
		assertEquals(2.0D, point.getComponent2());
		assertEquals(3.0D, point.getComponent3());
		assertEquals(4.0D, point.getComponent4());
	}
	
	@Test
	public void testConstructorPoint3D() {
		final Point4D point = new Point4D(new Point3D(2.0D, 3.0D, 4.0D));
		
		assertEquals(2.0D, point.getComponent1());
		assertEquals(3.0D, point.getComponent2());
		assertEquals(4.0D, point.getComponent3());
		assertEquals(1.0D, point.getComponent4());
		
		assertThrows(NullPointerException.class, () -> new Point4D((Point3D)(null)));
	}
	
	@Test
	public void testConstructorVector3D() {
		final Point4D point = new Point4D(new Vector3D(2.0D, 3.0D, 4.0D));
		
		assertEquals(2.0D, point.getComponent1());
		assertEquals(3.0D, point.getComponent2());
		assertEquals(4.0D, point.getComponent3());
		assertEquals(1.0D, point.getComponent4());
		
		assertThrows(NullPointerException.class, () -> new Point4D((Vector3D)(null)));
	}
	
	@Test
	public void testConstructorVector4D() {
		final Point4D point = new Point4D(new Vector4D(1.0D, 2.0D, 3.0D, 4.0D));
		
		assertEquals(1.0D, point.getComponent1());
		assertEquals(2.0D, point.getComponent2());
		assertEquals(3.0D, point.getComponent3());
		assertEquals(4.0D, point.getComponent4());
		
		assertThrows(NullPointerException.class, () -> new Point4D((Vector4D)(null)));
	}
	
	@Test
	public void testEquals() {
		final Point4D a = new Point4D(0.0D, 1.0D, 2.0D, 3.0D);
		final Point4D b = new Point4D(0.0D, 1.0D, 2.0D, 3.0D);
		final Point4D c = new Point4D(0.0D, 1.0D, 2.0D, 4.0D);
		final Point4D d = new Point4D(0.0D, 1.0D, 4.0D, 3.0D);
		final Point4D e = new Point4D(0.0D, 4.0D, 2.0D, 3.0D);
		final Point4D f = new Point4D(4.0D, 1.0D, 2.0D, 3.0D);
		final Point4D g = null;
		
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
	public void testGetComponent() {
		final Point4D point = new Point4D(1.0D, 2.0D, 3.0D, 4.0D);
		
		assertEquals(1.0D, point.getComponent(0));
		assertEquals(2.0D, point.getComponent(1));
		assertEquals(3.0D, point.getComponent(2));
		assertEquals(4.0D, point.getComponent(3));
		
		assertThrows(IllegalArgumentException.class, () -> point.getComponent(-1));
		assertThrows(IllegalArgumentException.class, () -> point.getComponent(+4));
	}
	
	@Test
	public void testGetComponent1() {
		final Point4D point = new Point4D(2.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(2.0D, point.getComponent1());
	}
	
	@Test
	public void testGetComponent2() {
		final Point4D point = new Point4D(0.0D, 2.0D, 0.0D, 0.0D);
		
		assertEquals(2.0D, point.getComponent2());
	}
	
	@Test
	public void testGetComponent3() {
		final Point4D point = new Point4D(0.0D, 0.0D, 2.0D, 0.0D);
		
		assertEquals(2.0D, point.getComponent3());
	}
	
	@Test
	public void testGetComponent4() {
		final Point4D point = new Point4D(0.0D, 0.0D, 0.0D, 2.0D);
		
		assertEquals(2.0D, point.getComponent4());
	}
	
	@Test
	public void testGetW() {
		final Point4D point = new Point4D(0.0D, 0.0D, 0.0D, 2.0D);
		
		assertEquals(2.0D, point.getW());
	}
	
	@Test
	public void testGetX() {
		final Point4D point = new Point4D(2.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(2.0D, point.getX());
	}
	
	@Test
	public void testGetY() {
		final Point4D point = new Point4D(0.0D, 2.0D, 0.0D, 0.0D);
		
		assertEquals(2.0D, point.getY());
	}
	
	@Test
	public void testGetZ() {
		final Point4D point = new Point4D(0.0D, 0.0D, 2.0D, 0.0D);
		
		assertEquals(2.0D, point.getZ());
	}
	
	@Test
	public void testHashCode() {
		final Point4D a = new Point4D(1.0D, 2.0D, 3.0D, 4.0D);
		final Point4D b = new Point4D(1.0D, 2.0D, 3.0D, 4.0D);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testLerp() {
		final Point4D a = new Point4D(1.0D, 1.0D, 1.0D, 1.0D);
		final Point4D b = new Point4D(5.0D, 5.0D, 5.0D, 5.0D);
		final Point4D c = Point4D.lerp(a, b, 0.25D);
		
		assertEquals(2.0D, c.getComponent1());
		assertEquals(2.0D, c.getComponent2());
		assertEquals(2.0D, c.getComponent3());
		assertEquals(2.0D, c.getComponent4());
		
		assertThrows(NullPointerException.class, () -> Point4D.lerp(a, null, 0.25D));
		assertThrows(NullPointerException.class, () -> Point4D.lerp(null, b, 0.25D));
	}
	
	@Test
	public void testRead() throws IOException {
		final Point4D a = new Point4D(1.0D, 2.0D, 3.0D, 4.0D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeDouble(a.getComponent1());
		dataOutput.writeDouble(a.getComponent2());
		dataOutput.writeDouble(a.getComponent3());
		dataOutput.writeDouble(a.getComponent4());
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Point4D b = Point4D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Point4D.read(null));
		assertThrows(UncheckedIOException.class, () -> Point4D.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testToArray() {
		final Point4D point = new Point4D(1.0D, 2.0D, 3.0D, 4.0D);
		
		final double[] array = point.toArray();
		
		assertNotNull(array);
		
		assertEquals(4, array.length);
		
		assertEquals(1.0D, array[0]);
		assertEquals(2.0D, array[1]);
		assertEquals(3.0D, array[2]);
		assertEquals(4.0D, array[3]);
	}
	
	@Test
	public void testToString() {
		final Point4D point = new Point4D(1.0D, 2.0D, 3.0D, 4.0D);
		
		assertEquals("new Point4D(1.0D, 2.0D, 3.0D, 4.0D)", point.toString());
	}
	
	@Test
	public void testTransform() {
		final Point4D a = new Point4D(1.0D, 2.0D, 3.0D, 1.0D);
		final Point4D b = Point4D.transform(Matrix44D.scale(1.0D, 2.0D, 3.0D), a);
		final Point4D c = Point4D.transform(Matrix44D.translate(1.0D, 2.0D, 3.0D), a);
		
		assertEquals(1.0D, b.getComponent1());
		assertEquals(4.0D, b.getComponent2());
		assertEquals(9.0D, b.getComponent3());
		assertEquals(1.0D, b.getComponent4());
		
		assertEquals(2.0D, c.getComponent1());
		assertEquals(4.0D, c.getComponent2());
		assertEquals(6.0D, c.getComponent3());
		assertEquals(1.0D, c.getComponent4());
		
		assertThrows(NullPointerException.class, () -> Point4D.transform(Matrix44D.translate(1.0D, 2.0D, 3.0D), null));
		assertThrows(NullPointerException.class, () -> Point4D.transform(null, a));
	}
	
	@Test
	public void testTransformAndDivide() {
		final Point4D a = new Point4D(1.0D, 2.0D, 3.0D, 1.0D);
		final Point4D b = Point4D.transformAndDivide(Matrix44D.scale(1.0D, 2.0D, 3.0D), a);
		final Point4D c = Point4D.transformAndDivide(Matrix44D.translate(1.0D, 2.0D, 3.0D), a);
		
		assertEquals(1.0D, b.getComponent1());
		assertEquals(4.0D, b.getComponent2());
		assertEquals(9.0D, b.getComponent3());
		assertEquals(1.0D, b.getComponent4());
		
		assertEquals(2.0D, c.getComponent1());
		assertEquals(4.0D, c.getComponent2());
		assertEquals(6.0D, c.getComponent3());
		assertEquals(1.0D, c.getComponent4());
		
		assertThrows(NullPointerException.class, () -> Point4D.transformAndDivide(Matrix44D.translate(1.0D, 2.0D, 3.0D), null));
		assertThrows(NullPointerException.class, () -> Point4D.transformAndDivide(null, a));
	}
	
	@Test
	public void testWrite() {
		final Point4D a = new Point4D(1.0D, 2.0D, 3.0D, 4.0D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Point4D b = Point4D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
	}
}