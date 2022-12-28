/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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
public final class Point3DUnitTests {
	public Point3DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAddPoint3DDouble() {
		final Point3D a = new Point3D(1.0D, 2.0D, 3.0D);
		final Point3D b = Point3D.add(a, 2.0D);
		
		assertEquals(3.0D, b.x);
		assertEquals(4.0D, b.y);
		assertEquals(5.0D, b.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.add(null, 2.0D));
	}
	
	@Test
	public void testAddPoint3DVector3D() {
		final Point3D a = new Point3D(1.0D, 2.0D, 3.0D);
		final Point3D b = Point3D.add(a, new Vector3D(1.0D, 2.0D, 3.0D));
		
		assertEquals(2.0D, b.x);
		assertEquals(4.0D, b.y);
		assertEquals(6.0D, b.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.add(a, null));
		assertThrows(NullPointerException.class, () -> Point3D.add(null, new Vector3D(1.0D, 2.0D, 3.0D)));
	}
	
	@Test
	public void testAddPoint3DVector3DDouble() {
		final Point3D a = new Point3D(1.0D, 2.0D, 3.0D);
		final Point3D b = Point3D.add(a, new Vector3D(1.0D, 2.0D, 3.0D), 2.0D);
		
		assertEquals(3.0D, b.x);
		assertEquals(6.0D, b.y);
		assertEquals(9.0D, b.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.add(a, null, 2.0D));
		assertThrows(NullPointerException.class, () -> Point3D.add(null, new Vector3D(1.0D, 2.0D, 3.0D), 2.0D));
	}
	
	@Test
	public void testCentroidPoint3DPoint3D() {
		final Point3D a = new Point3D(2.0D, 4.0D,  6.0D);
		final Point3D b = new Point3D(4.0D, 8.0D, 12.0D);
		final Point3D c = Point3D.centroid(a, b);
		
		assertEquals(3.0D, c.x);
		assertEquals(6.0D, c.y);
		assertEquals(9.0D, c.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.centroid(a, null));
		assertThrows(NullPointerException.class, () -> Point3D.centroid(null, b));
	}
	
	@Test
	public void testCentroidPoint3DPoint3DPoint3D() {
		final Point3D a = new Point3D(2.0D,  4.0D,  6.0D);
		final Point3D b = new Point3D(4.0D,  8.0D, 12.0D);
		final Point3D c = new Point3D(6.0D, 12.0D, 18.0D);
		final Point3D d = Point3D.centroid(a, b, c);
		
		assertEquals( 4.0D, d.x);
		assertEquals( 8.0D, d.y);
		assertEquals(12.0D, d.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.centroid(a, b, null));
		assertThrows(NullPointerException.class, () -> Point3D.centroid(a, null, c));
		assertThrows(NullPointerException.class, () -> Point3D.centroid(null, b, c));
	}
	
	@Test
	public void testCentroidPoint3DPoint3DPoint3DPoint3D() {
		final Point3D a = new Point3D(2.0D,  4.0D,  6.0D);
		final Point3D b = new Point3D(4.0D,  8.0D, 12.0D);
		final Point3D c = new Point3D(6.0D, 12.0D, 18.0D);
		final Point3D d = new Point3D(8.0D, 16.0D, 24.0D);
		final Point3D e = Point3D.centroid(a, b, c, d);
		
		assertEquals( 5.0D, e.x);
		assertEquals(10.0D, e.y);
		assertEquals(15.0D, e.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.centroid(a, b, c, null));
		assertThrows(NullPointerException.class, () -> Point3D.centroid(a, b, null, d));
		assertThrows(NullPointerException.class, () -> Point3D.centroid(a, null, c, d));
		assertThrows(NullPointerException.class, () -> Point3D.centroid(null, b, c, d));
	}
	
	@Test
	public void testCentroidPoint3DPoint3DPoint3DPoint3DPoint3DPoint3DPoint3DPoint3D() {
		final Point3D a = new Point3D( 2.0D,  4.0D,  6.0D);
		final Point3D b = new Point3D( 4.0D,  8.0D, 12.0D);
		final Point3D c = new Point3D( 6.0D, 12.0D, 18.0D);
		final Point3D d = new Point3D( 8.0D, 16.0D, 24.0D);
		final Point3D e = new Point3D(10.0D, 20.0D, 30.0D);
		final Point3D f = new Point3D(12.0D, 24.0D, 36.0D);
		final Point3D g = new Point3D(14.0D, 28.0D, 42.0D);
		final Point3D h = new Point3D(16.0D, 32.0D, 48.0D);
		final Point3D i = Point3D.centroid(a, b, c, d, e, f, g, h);
		
		assertEquals( 9.0D, i.x);
		assertEquals(18.0D, i.y);
		assertEquals(27.0D, i.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.centroid(a, b, c, d, e, f, g, null));
		assertThrows(NullPointerException.class, () -> Point3D.centroid(a, b, c, d, e, f, null, h));
		assertThrows(NullPointerException.class, () -> Point3D.centroid(a, b, c, d, e, null, g, h));
		assertThrows(NullPointerException.class, () -> Point3D.centroid(a, b, c, d, null, f, g, h));
		assertThrows(NullPointerException.class, () -> Point3D.centroid(a, b, c, null, e, f, g, h));
		assertThrows(NullPointerException.class, () -> Point3D.centroid(a, b, null, d, e, f, g, h));
		assertThrows(NullPointerException.class, () -> Point3D.centroid(a, null, c, d, e, f, g, h));
		assertThrows(NullPointerException.class, () -> Point3D.centroid(null, b, c, d, e, f, g, h));
	}
	
	@Test
	public void testClearCacheAndGetCacheSizeAndGetCached() {
		assertEquals(0, Point3D.getCacheSize());
		
		final Point3D a = new Point3D(1.0D, 2.0D, 3.0D);
		final Point3D b = new Point3D(1.0D, 2.0D, 3.0D);
		final Point3D c = Point3D.getCached(a);
		final Point3D d = Point3D.getCached(b);
		
		assertThrows(NullPointerException.class, () -> Point3D.getCached(null));
		
		assertEquals(1, Point3D.getCacheSize());
		
		Point3D.clearCache();
		
		assertEquals(0, Point3D.getCacheSize());
		
		assertTrue(a != b);
		assertTrue(a == c);
		assertTrue(a == d);
		
		assertTrue(b != a);
		assertTrue(b != c);
		assertTrue(b != d);
	}
	
	@Test
	public void testConstants() {
		assertEquals(new Point3D(+Double.MAX_VALUE, +Double.MAX_VALUE, +Double.MAX_VALUE), Point3D.MAXIMUM);
		assertEquals(new Point3D(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE), Point3D.MINIMUM);
	}
	
	@Test
	public void testConstructor() {
		final Point3F point = new Point3F();
		
		assertEquals(0.0F, point.x);
		assertEquals(0.0F, point.y);
		assertEquals(0.0F, point.z);
	}
	
	@Test
	public void testConstructorDouble() {
		final Point3D point = new Point3D(1.0D);
		
		assertEquals(1.0D, point.x);
		assertEquals(1.0D, point.y);
		assertEquals(1.0D, point.z);
	}
	
	@Test
	public void testConstructorDoubleDoubleDouble() {
		final Point3D point = new Point3D(1.0D, 2.0D, 3.0D);
		
		assertEquals(1.0D, point.x);
		assertEquals(2.0D, point.y);
		assertEquals(3.0D, point.z);
	}
	
	@Test
	public void testConstructorPoint4D() {
		final Point3D point = new Point3D(new Point4D(1.0D, 2.0D, 3.0D, 4.0D));
		
		assertEquals(1.0D, point.x);
		assertEquals(2.0D, point.y);
		assertEquals(3.0D, point.z);
		
		assertThrows(NullPointerException.class, () -> new Point3D((Point4D)(null)));
	}
	
	@Test
	public void testConstructorVector3D() {
		final Point3D point = new Point3D(new Vector3D(1.0D, 2.0D, 3.0D));
		
		assertEquals(1.0D, point.x);
		assertEquals(2.0D, point.y);
		assertEquals(3.0D, point.z);
		
		assertThrows(NullPointerException.class, () -> new Point3D((Vector3D)(null)));
	}
	
	@Test
	public void testCoplanar() {
		assertTrue(Point3D.coplanar(new Point3D(1.0D, 1.0D, 1.0D), new Point3D(1.0D, 3.0D, 1.0D), new Point3D(3.0D, 1.0D, 1.0D)));
		assertTrue(Point3D.coplanar(new Point3D(1.0D, 1.0D, 1.0D), new Point3D(1.0D, 3.0D, 1.0D), new Point3D(3.0D, 3.0D, 1.0D), new Point3D(3.0D, 1.0D, 1.0D)));
		assertTrue(Point3D.coplanar(new Point3D(1.0D, 1.0D, 1.0D), new Point3D(1.0D, 2.0D, 1.0D), new Point3D(2.0D, 3.0D, 1.0D), new Point3D(3.0D, 2.0D, 1.0D), new Point3D(3.0D, 1.0D, 1.0D)));
		
		assertFalse(Point3D.coplanar(new Point3D(1.0D, 1.0D, 1.0D), new Point3D(1.0D, 3.0D, 2.0D), new Point3D(3.0D, 3.0D, 3.0D), new Point3D(3.0D, 1.0D, 4.0D)));
		assertFalse(Point3D.coplanar(new Point3D(1.0D, 1.0D, 1.0D), new Point3D(1.0D, 2.0D, 2.0D), new Point3D(2.0D, 3.0D, 3.0D), new Point3D(3.0D, 2.0D, 4.0D), new Point3D(3.0D, 1.0D, 5.0D)));
		
		assertThrows(IllegalArgumentException.class, () -> Point3D.coplanar(new Point3D(), new Point3D()));
		assertThrows(NullPointerException.class, () -> Point3D.coplanar((Point3D[])(null)));
		assertThrows(NullPointerException.class, () -> Point3D.coplanar(new Point3D(), new Point3D(), new Point3D(), null));
		assertThrows(NullPointerException.class, () -> Point3D.coplanar(new Point3D[] {new Point3D(), new Point3D(), new Point3D(), null}));
	}
	
	@Test
	public void testDistance() {
		final Point3D a = new Point3D(0.0D, 0.0D, 0.0D);
		final Point3D b = new Point3D(9.0D, 0.0D, 0.0D);
		final Point3D c = new Point3D(0.0D, 9.0D, 0.0D);
		final Point3D d = new Point3D(0.0D, 0.0D, 9.0D);
		
		assertEquals(9.0D, Point3D.distance(a, b));
		assertEquals(9.0D, Point3D.distance(a, c));
		assertEquals(9.0D, Point3D.distance(a, d));
		
		assertThrows(NullPointerException.class, () -> Point3D.distance(a, null));
		assertThrows(NullPointerException.class, () -> Point3D.distance(null, b));
	}
	
	@Test
	public void testDistanceSquared() {
		final Point3D a = new Point3D(0.0D, 0.0D, 0.0D);
		final Point3D b = new Point3D(9.0D, 0.0D, 0.0D);
		final Point3D c = new Point3D(0.0D, 9.0D, 0.0D);
		final Point3D d = new Point3D(0.0D, 0.0D, 9.0D);
		
		assertEquals(81.0D, Point3D.distanceSquared(a, b));
		assertEquals(81.0D, Point3D.distanceSquared(a, c));
		assertEquals(81.0D, Point3D.distanceSquared(a, d));
		
		assertThrows(NullPointerException.class, () -> Point3D.distanceSquared(a, null));
		assertThrows(NullPointerException.class, () -> Point3D.distanceSquared(null, b));
	}
	
	@Test
	public void testEquals() {
		final Point3D a = new Point3D(0.0D, 1.0D, 2.0D);
		final Point3D b = new Point3D(0.0D, 1.0D, 2.0D);
		final Point3D c = new Point3D(0.0D, 1.0D, 3.0D);
		final Point3D d = new Point3D(0.0D, 3.0D, 2.0D);
		final Point3D e = new Point3D(3.0D, 1.0D, 2.0D);
		final Point3D f = null;
		
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
	public void testGetComponent() {
		final Point3D point = new Point3D(1.0D, 2.0D, 3.0D);
		
		assertEquals(1.0D, point.getComponent(0));
		assertEquals(2.0D, point.getComponent(1));
		assertEquals(3.0D, point.getComponent(2));
		
		assertThrows(IllegalArgumentException.class, () -> point.getComponent(-1));
		assertThrows(IllegalArgumentException.class, () -> point.getComponent(+3));
	}
	
	@Test
	public void testHashCode() {
		final Point3D a = new Point3D(1.0D, 2.0D, 3.0D);
		final Point3D b = new Point3D(1.0D, 2.0D, 3.0D);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testLerp() {
		final Point3D a = new Point3D(1.0D, 1.0D, 1.0D);
		final Point3D b = new Point3D(5.0D, 5.0D, 5.0D);
		final Point3D c = Point3D.lerp(a, b, 0.25D);
		
		assertEquals(2.0D, c.x);
		assertEquals(2.0D, c.y);
		assertEquals(2.0D, c.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.lerp(a, null, 0.25D));
		assertThrows(NullPointerException.class, () -> Point3D.lerp(null, b, 0.25D));
	}
	
	@Test
	public void testMaximum() {
		assertEquals(new Point3D(+Double.MAX_VALUE, +Double.MAX_VALUE, +Double.MAX_VALUE), Point3D.maximum());
	}
	
	@Test
	public void testMaximumPoint3DPoint3D() {
		final Point3D a = new Point3D(1.0D, 2.0D, 3.0D);
		final Point3D b = new Point3D(4.0D, 5.0D, 6.0D);
		final Point3D c = Point3D.maximum(a, b);
		
		assertEquals(4.0D, c.x);
		assertEquals(5.0D, c.y);
		assertEquals(6.0D, c.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.maximum(a, null));
		assertThrows(NullPointerException.class, () -> Point3D.maximum(null, b));
	}
	
	@Test
	public void testMaximumPoint3DPoint3DPoint3D() {
		final Point3D a = new Point3D(1.0D, 2.0D, 3.0D);
		final Point3D b = new Point3D(4.0D, 5.0D, 6.0D);
		final Point3D c = new Point3D(7.0D, 8.0D, 9.0D);
		final Point3D d = Point3D.maximum(a, b, c);
		
		assertEquals(7.0D, d.x);
		assertEquals(8.0D, d.y);
		assertEquals(9.0D, d.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.maximum(a, b, null));
		assertThrows(NullPointerException.class, () -> Point3D.maximum(a, null, c));
		assertThrows(NullPointerException.class, () -> Point3D.maximum(null, b, c));
	}
	
	@Test
	public void testMaximumPoint3DPoint3DPoint3DPoint3D() {
		final Point3D a = new Point3D( 1.0D,  2.0D,  3.0D);
		final Point3D b = new Point3D( 4.0D,  5.0D,  6.0D);
		final Point3D c = new Point3D( 7.0D,  8.0D,  9.0D);
		final Point3D d = new Point3D(10.0D, 11.0D, 12.0D);
		final Point3D e = Point3D.maximum(a, b, c, d);
		
		assertEquals(10.0D, e.x);
		assertEquals(11.0D, e.y);
		assertEquals(12.0D, e.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.maximum(a, b, c, null));
		assertThrows(NullPointerException.class, () -> Point3D.maximum(a, b, null, d));
		assertThrows(NullPointerException.class, () -> Point3D.maximum(a, null, c, d));
		assertThrows(NullPointerException.class, () -> Point3D.maximum(null, b, c, d));
	}
	
	@Test
	public void testMidpoint() {
		final Point3D a = new Point3D(2.0D, 4.0D,  8.0D);
		final Point3D b = new Point3D(4.0D, 8.0D, 16.0D);
		final Point3D c = Point3D.midpoint(a, b);
		
		assertEquals( 3.0D, c.x);
		assertEquals( 6.0D, c.y);
		assertEquals(12.0D, c.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.midpoint(a, null));
		assertThrows(NullPointerException.class, () -> Point3D.midpoint(null, b));
	}
	
	@Test
	public void testMinimum() {
		assertEquals(new Point3D(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE), Point3D.minimum());
	}
	
	@Test
	public void testMinimumPoint3DPoint3D() {
		final Point3D a = new Point3D(1.0D, 2.0D, 3.0D);
		final Point3D b = new Point3D(4.0D, 5.0D, 6.0D);
		final Point3D c = Point3D.minimum(a, b);
		
		assertEquals(1.0D, c.x);
		assertEquals(2.0D, c.y);
		assertEquals(3.0D, c.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.minimum(a, null));
		assertThrows(NullPointerException.class, () -> Point3D.minimum(null, b));
	}
	
	@Test
	public void testMinimumPoint3DPoint3DPoint3D() {
		final Point3D a = new Point3D(1.0D, 2.0D, 3.0D);
		final Point3D b = new Point3D(4.0D, 5.0D, 6.0D);
		final Point3D c = new Point3D(7.0D, 8.0D, 9.0D);
		final Point3D d = Point3D.minimum(a, b, c);
		
		assertEquals(1.0D, d.x);
		assertEquals(2.0D, d.y);
		assertEquals(3.0D, d.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.minimum(a, b, null));
		assertThrows(NullPointerException.class, () -> Point3D.minimum(a, null, c));
		assertThrows(NullPointerException.class, () -> Point3D.minimum(null, b, c));
	}
	
	@Test
	public void testMinimumPoint3DPoint3DPoint3DPoint3D() {
		final Point3D a = new Point3D( 1.0D,  2.0D,  3.0D);
		final Point3D b = new Point3D( 4.0D,  5.0D,  6.0D);
		final Point3D c = new Point3D( 7.0D,  8.0D,  9.0D);
		final Point3D d = new Point3D(10.0D, 11.0D, 12.0D);
		final Point3D e = Point3D.minimum(a, b, c, d);
		
		assertEquals(1.0D, e.x);
		assertEquals(2.0D, e.y);
		assertEquals(3.0D, e.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.minimum(a, b, c, null));
		assertThrows(NullPointerException.class, () -> Point3D.minimum(a, b, null, d));
		assertThrows(NullPointerException.class, () -> Point3D.minimum(a, null, c, d));
		assertThrows(NullPointerException.class, () -> Point3D.minimum(null, b, c, d));
	}
	
	@Test
	public void testOffset() {
		final Point3D a = Point3D.offset(new Point3D(1.0D, 1.0D, 1.0D), Vector3D.z(+1.0D), Vector3D.z(+1.0D), new Vector3D(0.0D, 0.0D, 0.0D));
		final Point3D b = Point3D.offset(new Point3D(1.0D, 1.0D, 1.0D), Vector3D.z(+1.0D), Vector3D.z(-1.0D), new Vector3D(0.0D, 0.0D, 0.0D));
		final Point3D c = Point3D.offset(new Point3D(1.0D, 1.0D, 1.0D), Vector3D.x(), Vector3D.x(), Vector3D.x());
		final Point3D d = Point3D.offset(new Point3D(1.0D, 1.0D, 1.0D), Vector3D.y(), Vector3D.y(), Vector3D.y());
		final Point3D e = Point3D.offset(new Point3D(1.0D, 1.0D, 1.0D), Vector3D.z(), Vector3D.z(), Vector3D.z());
		
		assertEquals(1.0D, a.x);
		assertEquals(1.0D, a.y);
		assertEquals(1.0D, a.z);
		
		assertEquals(1.0D, b.x);
		assertEquals(1.0D, b.y);
		assertEquals(1.0D, b.z);
		
		assertEquals(Math.nextUp(2.0D), c.x);
		assertEquals(1.0D,              c.y);
		assertEquals(1.0D,              c.z);
		
		assertEquals(1.0D,              d.x);
		assertEquals(Math.nextUp(2.0D), d.y);
		assertEquals(1.0D,              d.z);
		
		assertEquals(1.0D,              e.x);
		assertEquals(1.0D,              e.y);
		assertEquals(Math.nextUp(2.0D), e.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.offset(new Point3D(), new Vector3D(), new Vector3D(), null));
		assertThrows(NullPointerException.class, () -> Point3D.offset(new Point3D(), new Vector3D(), null, new Vector3D()));
		assertThrows(NullPointerException.class, () -> Point3D.offset(new Point3D(), null, new Vector3D(), new Vector3D()));
		assertThrows(NullPointerException.class, () -> Point3D.offset(null, new Vector3D(), new Vector3D(), new Vector3D()));
	}
	
	@Test
	public void testRead() throws IOException {
		final Point3D a = new Point3D(1.0D, 2.0D, 3.0D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeDouble(a.x);
		dataOutput.writeDouble(a.y);
		dataOutput.writeDouble(a.z);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Point3D b = Point3D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Point3D.read(null));
		assertThrows(UncheckedIOException.class, () -> Point3D.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testScalePoint3DVector2D() {
		final Point3D a = new Point3D(1.0D, 2.0D, 3.0D);
		final Point3D b = Point3D.scale(a, new Vector2D(2.0D, 3.0D));
		
		assertEquals(2.0D, b.x);
		assertEquals(6.0D, b.y);
		assertEquals(3.0D, b.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.scale(a, (Vector2D)(null)));
		assertThrows(NullPointerException.class, () -> Point3D.scale(null, new Vector2D(1.0D, 1.0D)));
	}
	
	@Test
	public void testScalePoint3DVector3D() {
		final Point3D a = new Point3D(1.0D, 2.0D, 3.0D);
		final Point3D b = Point3D.scale(a, new Vector3D(2.0D, 3.0D, 4.0D));
		
		assertEquals( 2.0D, b.x);
		assertEquals( 6.0D, b.y);
		assertEquals(12.0D, b.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.scale(a, (Vector3D)(null)));
		assertThrows(NullPointerException.class, () -> Point3D.scale(null, new Vector3D(1.0D, 1.0D, 1.0D)));
	}
	
	@Test
	public void testSphericalPhi() {
		final Point3D x = new Point3D(1.0D, 0.0D, 0.0D);
		final Point3D y = new Point3D(0.0D, 1.0D, 0.0D);
		final Point3D z = new Point3D(0.0D, 0.0D, 1.0D);
		
		assertEquals(0.0D,           x.sphericalPhi());
		assertEquals(Math.PI / 2.0D, y.sphericalPhi());
		assertEquals(0.0D,           z.sphericalPhi());
	}
	
	@Test
	public void testSubtractPoint3DDouble() {
		final Point3D a = new Point3D(3.0D, 4.0D, 5.0D);
		final Point3D b = Point3D.subtract(a, 3.0D);
		
		assertEquals(0.0D, b.x);
		assertEquals(1.0D, b.y);
		assertEquals(2.0D, b.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.subtract(null, 1.0D));
	}
	
	@Test
	public void testSubtractPoint3DVector3D() {
		final Point3D a = new Point3D(3.0D, 4.0D, 5.0D);
		final Point3D b = Point3D.subtract(a, new Vector3D(1.0D, 3.0D, 5.0D));
		
		assertEquals(2.0D, b.x);
		assertEquals(1.0D, b.y);
		assertEquals(0.0D, b.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.subtract(a, (Vector3D)(null)));
		assertThrows(NullPointerException.class, () -> Point3D.subtract(null, new Vector3D(1.0D, 1.0D, 1.0D)));
	}
	
	@Test
	public void testToArray() {
		final Point3D point = new Point3D(1.0D, 2.0D, 3.0D);
		
		final double[] array = point.toArray();
		
		assertNotNull(array);
		
		assertEquals(3, array.length);
		
		assertEquals(1.0D, array[0]);
		assertEquals(2.0D, array[1]);
		assertEquals(3.0D, array[2]);
	}
	
	@Test
	public void testToString() {
		final Point3D point = new Point3D(1.0D, 2.0D, 3.0D);
		
		assertEquals("new Point3D(1.0D, 2.0D, 3.0D)", point.toString());
	}
	
	@Test
	public void testToStringPoint3DArray() {
		final Point3D a = new Point3D(1.0D, 1.0D, 1.0D);
		final Point3D b = new Point3D(2.0D, 2.0D, 2.0D);
		final Point3D c = new Point3D(3.0D, 3.0D, 3.0D);
		
		assertEquals("new Point3D[] {new Point3D(1.0D, 1.0D, 1.0D), new Point3D(2.0D, 2.0D, 2.0D), new Point3D(3.0D, 3.0D, 3.0D)}", Point3D.toString(a, b, c));
		
		assertThrows(NullPointerException.class, () -> Point3D.toString((Point3D[])(null)));
		assertThrows(NullPointerException.class, () -> Point3D.toString(a, b, c, null));
		assertThrows(NullPointerException.class, () -> Point3D.toString(new Point3D[] {a, b, c, null}));
	}
	
	@Test
	public void testTransform() {
		final Point3D a = new Point3D(1.0D, 2.0D, 3.0D);
		final Point3D b = Point3D.transform(Matrix44D.scale(1.0D, 2.0D, 3.0D), a);
		final Point3D c = Point3D.transform(Matrix44D.translate(1.0D, 2.0D, 3.0D), a);
		
		assertEquals(1.0D, b.x);
		assertEquals(4.0D, b.y);
		assertEquals(9.0D, b.z);
		
		assertEquals(2.0D, c.x);
		assertEquals(4.0D, c.y);
		assertEquals(6.0D, c.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.transform(Matrix44D.translate(1.0D, 2.0D, 3.0D), null));
		assertThrows(NullPointerException.class, () -> Point3D.transform(null, a));
	}
	
	@Test
	public void testTransformAndDivide() {
		final Point3D a = new Point3D(1.0D, 2.0D, 3.0D);
		final Point3D b = Point3D.transformAndDivide(Matrix44D.scale(1.0D, 2.0D, 3.0D), a);
		final Point3D c = Point3D.transformAndDivide(Matrix44D.translate(1.0D, 2.0D, 3.0D), a);
		final Point3D d = Point3D.transformAndDivide(new Matrix44D(1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 2.0D), a);
		final Point3D e = Point3D.transformAndDivide(new Matrix44D(1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D), a);
		
		assertEquals(1.0D, b.x);
		assertEquals(4.0D, b.y);
		assertEquals(9.0D, b.z);
		
		assertEquals(2.0D, c.x);
		assertEquals(4.0D, c.y);
		assertEquals(6.0D, c.z);
		
		assertEquals(0.5D, d.x);
		assertEquals(1.0D, d.y);
		assertEquals(1.5D, d.z);
		
		assertEquals(1.0D, e.x);
		assertEquals(2.0D, e.y);
		assertEquals(3.0D, e.z);
		
		assertThrows(NullPointerException.class, () -> Point3D.transformAndDivide(Matrix44D.translate(1.0D, 2.0D, 3.0D), null));
		assertThrows(NullPointerException.class, () -> Point3D.transformAndDivide(null, a));
	}
	
	@Test
	public void testWrite() {
		final Point3D a = new Point3D(1.0D, 2.0D, 3.0D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Point3D b = Point3D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}