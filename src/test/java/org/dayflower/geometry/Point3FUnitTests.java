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
public final class Point3FUnitTests {
	public Point3FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAddPoint3FFloat() {
		final Point3F a = new Point3F(1.0F, 2.0F, 3.0F);
		final Point3F b = Point3F.add(a, 2.0F);
		
		assertEquals(3.0F, b.x);
		assertEquals(4.0F, b.y);
		assertEquals(5.0F, b.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.add(null, 2.0F));
	}
	
	@Test
	public void testAddPoint3FVector3F() {
		final Point3F a = new Point3F(1.0F, 2.0F, 3.0F);
		final Point3F b = Point3F.add(a, new Vector3F(1.0F, 2.0F, 3.0F));
		
		assertEquals(2.0F, b.x);
		assertEquals(4.0F, b.y);
		assertEquals(6.0F, b.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.add(a, null));
		assertThrows(NullPointerException.class, () -> Point3F.add(null, new Vector3F(1.0F, 2.0F, 3.0F)));
	}
	
	@Test
	public void testAddPoint3FVector3FFloat() {
		final Point3F a = new Point3F(1.0F, 2.0F, 3.0F);
		final Point3F b = Point3F.add(a, new Vector3F(1.0F, 2.0F, 3.0F), 2.0F);
		
		assertEquals(3.0F, b.x);
		assertEquals(6.0F, b.y);
		assertEquals(9.0F, b.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.add(a, null, 2.0F));
		assertThrows(NullPointerException.class, () -> Point3F.add(null, new Vector3F(1.0F, 2.0F, 3.0F), 2.0F));
	}
	
	@Test
	public void testCentroidPoint3FPoint3F() {
		final Point3F a = new Point3F(2.0F, 4.0F,  6.0F);
		final Point3F b = new Point3F(4.0F, 8.0F, 12.0F);
		final Point3F c = Point3F.centroid(a, b);
		
		assertEquals(3.0F, c.x);
		assertEquals(6.0F, c.y);
		assertEquals(9.0F, c.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.centroid(a, null));
		assertThrows(NullPointerException.class, () -> Point3F.centroid(null, b));
	}
	
	@Test
	public void testCentroidPoint3FPoint3FPoint3F() {
		final Point3F a = new Point3F(2.0F,  4.0F,  6.0F);
		final Point3F b = new Point3F(4.0F,  8.0F, 12.0F);
		final Point3F c = new Point3F(6.0F, 12.0F, 18.0F);
		final Point3F d = Point3F.centroid(a, b, c);
		
		assertEquals( 4.0F, d.x);
		assertEquals( 8.0F, d.y);
		assertEquals(12.0F, d.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.centroid(a, b, null));
		assertThrows(NullPointerException.class, () -> Point3F.centroid(a, null, c));
		assertThrows(NullPointerException.class, () -> Point3F.centroid(null, b, c));
	}
	
	@Test
	public void testCentroidPoint3FPoint3FPoint3FPoint3F() {
		final Point3F a = new Point3F(2.0F,  4.0F,  6.0F);
		final Point3F b = new Point3F(4.0F,  8.0F, 12.0F);
		final Point3F c = new Point3F(6.0F, 12.0F, 18.0F);
		final Point3F d = new Point3F(8.0F, 16.0F, 24.0F);
		final Point3F e = Point3F.centroid(a, b, c, d);
		
		assertEquals( 5.0F, e.x);
		assertEquals(10.0F, e.y);
		assertEquals(15.0F, e.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.centroid(a, b, c, null));
		assertThrows(NullPointerException.class, () -> Point3F.centroid(a, b, null, d));
		assertThrows(NullPointerException.class, () -> Point3F.centroid(a, null, c, d));
		assertThrows(NullPointerException.class, () -> Point3F.centroid(null, b, c, d));
	}
	
	@Test
	public void testCentroidPoint3FPoint3FPoint3FPoint3FPoint3FPoint3FPoint3FPoint3F() {
		final Point3F a = new Point3F( 2.0F,  4.0F,  6.0F);
		final Point3F b = new Point3F( 4.0F,  8.0F, 12.0F);
		final Point3F c = new Point3F( 6.0F, 12.0F, 18.0F);
		final Point3F d = new Point3F( 8.0F, 16.0F, 24.0F);
		final Point3F e = new Point3F(10.0F, 20.0F, 30.0F);
		final Point3F f = new Point3F(12.0F, 24.0F, 36.0F);
		final Point3F g = new Point3F(14.0F, 28.0F, 42.0F);
		final Point3F h = new Point3F(16.0F, 32.0F, 48.0F);
		final Point3F i = Point3F.centroid(a, b, c, d, e, f, g, h);
		
		assertEquals( 9.0F, i.x);
		assertEquals(18.0F, i.y);
		assertEquals(27.0F, i.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.centroid(a, b, c, d, e, f, g, null));
		assertThrows(NullPointerException.class, () -> Point3F.centroid(a, b, c, d, e, f, null, h));
		assertThrows(NullPointerException.class, () -> Point3F.centroid(a, b, c, d, e, null, g, h));
		assertThrows(NullPointerException.class, () -> Point3F.centroid(a, b, c, d, null, f, g, h));
		assertThrows(NullPointerException.class, () -> Point3F.centroid(a, b, c, null, e, f, g, h));
		assertThrows(NullPointerException.class, () -> Point3F.centroid(a, b, null, d, e, f, g, h));
		assertThrows(NullPointerException.class, () -> Point3F.centroid(a, null, c, d, e, f, g, h));
		assertThrows(NullPointerException.class, () -> Point3F.centroid(null, b, c, d, e, f, g, h));
	}
	
	@Test
	public void testClearCacheAndGetCacheSizeAndGetCached() {
		assertEquals(0, Point3F.getCacheSize());
		
		final Point3F a = new Point3F(1.0F, 2.0F, 3.0F);
		final Point3F b = new Point3F(1.0F, 2.0F, 3.0F);
		final Point3F c = Point3F.getCached(a);
		final Point3F d = Point3F.getCached(b);
		
		assertThrows(NullPointerException.class, () -> Point3F.getCached(null));
		
		assertEquals(1, Point3F.getCacheSize());
		
		Point3F.clearCache();
		
		assertEquals(0, Point3F.getCacheSize());
		
		assertTrue(a != b);
		assertTrue(a == c);
		assertTrue(a == d);
		
		assertTrue(b != a);
		assertTrue(b != c);
		assertTrue(b != d);
	}
	
	@Test
	public void testConstants() {
		assertEquals(new Point3F(+Float.MAX_VALUE, +Float.MAX_VALUE, +Float.MAX_VALUE), Point3F.MAXIMUM);
		assertEquals(new Point3F(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE), Point3F.MINIMUM);
	}
	
	@Test
	public void testConstructor() {
		final Point3F point = new Point3F();
		
		assertEquals(0.0F, point.x);
		assertEquals(0.0F, point.y);
		assertEquals(0.0F, point.z);
	}
	
	@Test
	public void testConstructorFloat() {
		final Point3F point = new Point3F(1.0F);
		
		assertEquals(1.0F, point.x);
		assertEquals(1.0F, point.y);
		assertEquals(1.0F, point.z);
	}
	
	@Test
	public void testConstructorFloatFloatFloat() {
		final Point3F point = new Point3F(1.0F, 2.0F, 3.0F);
		
		assertEquals(1.0F, point.x);
		assertEquals(2.0F, point.y);
		assertEquals(3.0F, point.z);
	}
	
	@Test
	public void testConstructorPoint4F() {
		final Point3F point = new Point3F(new Point4F(1.0F, 2.0F, 3.0F, 4.0F));
		
		assertEquals(1.0F, point.x);
		assertEquals(2.0F, point.y);
		assertEquals(3.0F, point.z);
		
		assertThrows(NullPointerException.class, () -> new Point3F((Point4F)(null)));
	}
	
	@Test
	public void testConstructorVector3F() {
		final Point3F point = new Point3F(new Vector3F(1.0F, 2.0F, 3.0F));
		
		assertEquals(1.0F, point.x);
		assertEquals(2.0F, point.y);
		assertEquals(3.0F, point.z);
		
		assertThrows(NullPointerException.class, () -> new Point3F((Vector3F)(null)));
	}
	
	@Test
	public void testCoplanar() {
		assertTrue(Point3F.coplanar(new Point3F(1.0F, 1.0F, 1.0F), new Point3F(1.0F, 3.0F, 1.0F), new Point3F(3.0F, 1.0F, 1.0F)));
		assertTrue(Point3F.coplanar(new Point3F(1.0F, 1.0F, 1.0F), new Point3F(1.0F, 3.0F, 1.0F), new Point3F(3.0F, 3.0F, 1.0F), new Point3F(3.0F, 1.0F, 1.0F)));
		assertTrue(Point3F.coplanar(new Point3F(1.0F, 1.0F, 1.0F), new Point3F(1.0F, 2.0F, 1.0F), new Point3F(2.0F, 3.0F, 1.0F), new Point3F(3.0F, 2.0F, 1.0F), new Point3F(3.0F, 1.0F, 1.0F)));
		
		assertFalse(Point3F.coplanar(new Point3F(1.0F, 1.0F, 1.0F), new Point3F(1.0F, 3.0F, 2.0F), new Point3F(3.0F, 3.0F, 3.0F), new Point3F(3.0F, 1.0F, 4.0F)));
		assertFalse(Point3F.coplanar(new Point3F(1.0F, 1.0F, 1.0F), new Point3F(1.0F, 2.0F, 2.0F), new Point3F(2.0F, 3.0F, 3.0F), new Point3F(3.0F, 2.0F, 4.0F), new Point3F(3.0F, 1.0F, 5.0F)));
		
		assertThrows(IllegalArgumentException.class, () -> Point3F.coplanar(new Point3F(), new Point3F()));
		assertThrows(NullPointerException.class, () -> Point3F.coplanar((Point3F[])(null)));
		assertThrows(NullPointerException.class, () -> Point3F.coplanar(new Point3F(), new Point3F(), new Point3F(), null));
		assertThrows(NullPointerException.class, () -> Point3F.coplanar(new Point3F[] {new Point3F(), new Point3F(), new Point3F(), null}));
	}
	
	@Test
	public void testDistance() {
		final Point3F a = new Point3F(0.0F, 0.0F, 0.0F);
		final Point3F b = new Point3F(9.0F, 0.0F, 0.0F);
		final Point3F c = new Point3F(0.0F, 9.0F, 0.0F);
		final Point3F d = new Point3F(0.0F, 0.0F, 9.0F);
		
		assertEquals(9.0F, Point3F.distance(a, b));
		assertEquals(9.0F, Point3F.distance(a, c));
		assertEquals(9.0F, Point3F.distance(a, d));
		
		assertThrows(NullPointerException.class, () -> Point3F.distance(a, null));
		assertThrows(NullPointerException.class, () -> Point3F.distance(null, b));
	}
	
	@Test
	public void testDistanceSquared() {
		final Point3F a = new Point3F(0.0F, 0.0F, 0.0F);
		final Point3F b = new Point3F(9.0F, 0.0F, 0.0F);
		final Point3F c = new Point3F(0.0F, 9.0F, 0.0F);
		final Point3F d = new Point3F(0.0F, 0.0F, 9.0F);
		
		assertEquals(81.0F, Point3F.distanceSquared(a, b));
		assertEquals(81.0F, Point3F.distanceSquared(a, c));
		assertEquals(81.0F, Point3F.distanceSquared(a, d));
		
		assertThrows(NullPointerException.class, () -> Point3F.distanceSquared(a, null));
		assertThrows(NullPointerException.class, () -> Point3F.distanceSquared(null, b));
	}
	
	@Test
	public void testEquals() {
		final Point3F a = new Point3F(0.0F, 1.0F, 2.0F);
		final Point3F b = new Point3F(0.0F, 1.0F, 2.0F);
		final Point3F c = new Point3F(0.0F, 1.0F, 3.0F);
		final Point3F d = new Point3F(0.0F, 3.0F, 2.0F);
		final Point3F e = new Point3F(3.0F, 1.0F, 2.0F);
		final Point3F f = null;
		
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
		final Point3F point = new Point3F(1.0F, 2.0F, 3.0F);
		
		assertEquals(1.0F, point.getComponent(0));
		assertEquals(2.0F, point.getComponent(1));
		assertEquals(3.0F, point.getComponent(2));
		
		assertThrows(IllegalArgumentException.class, () -> point.getComponent(-1));
		assertThrows(IllegalArgumentException.class, () -> point.getComponent(+3));
	}
	
	@Test
	public void testHashCode() {
		final Point3F a = new Point3F(1.0F, 2.0F, 3.0F);
		final Point3F b = new Point3F(1.0F, 2.0F, 3.0F);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testLerp() {
		final Point3F a = new Point3F(1.0F, 1.0F, 1.0F);
		final Point3F b = new Point3F(5.0F, 5.0F, 5.0F);
		final Point3F c = Point3F.lerp(a, b, 0.25F);
		
		assertEquals(2.0F, c.x);
		assertEquals(2.0F, c.y);
		assertEquals(2.0F, c.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.lerp(a, null, 0.25F));
		assertThrows(NullPointerException.class, () -> Point3F.lerp(null, b, 0.25F));
	}
	
	@Test
	public void testMaximum() {
		assertEquals(new Point3F(+Float.MAX_VALUE, +Float.MAX_VALUE, +Float.MAX_VALUE), Point3F.maximum());
	}
	
	@Test
	public void testMaximumPoint3FPoint3F() {
		final Point3F a = new Point3F(1.0F, 2.0F, 3.0F);
		final Point3F b = new Point3F(4.0F, 5.0F, 6.0F);
		final Point3F c = Point3F.maximum(a, b);
		
		assertEquals(4.0F, c.x);
		assertEquals(5.0F, c.y);
		assertEquals(6.0F, c.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.maximum(a, null));
		assertThrows(NullPointerException.class, () -> Point3F.maximum(null, b));
	}
	
	@Test
	public void testMaximumPoint3FPoint3FPoint3F() {
		final Point3F a = new Point3F(1.0F, 2.0F, 3.0F);
		final Point3F b = new Point3F(4.0F, 5.0F, 6.0F);
		final Point3F c = new Point3F(7.0F, 8.0F, 9.0F);
		final Point3F d = Point3F.maximum(a, b, c);
		
		assertEquals(7.0F, d.x);
		assertEquals(8.0F, d.y);
		assertEquals(9.0F, d.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.maximum(a, b, null));
		assertThrows(NullPointerException.class, () -> Point3F.maximum(a, null, c));
		assertThrows(NullPointerException.class, () -> Point3F.maximum(null, b, c));
	}
	
	@Test
	public void testMaximumPoint3FPoint3FPoint3FPoint3F() {
		final Point3F a = new Point3F( 1.0F,  2.0F,  3.0F);
		final Point3F b = new Point3F( 4.0F,  5.0F,  6.0F);
		final Point3F c = new Point3F( 7.0F,  8.0F,  9.0F);
		final Point3F d = new Point3F(10.0F, 11.0F, 12.0F);
		final Point3F e = Point3F.maximum(a, b, c, d);
		
		assertEquals(10.0F, e.x);
		assertEquals(11.0F, e.y);
		assertEquals(12.0F, e.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.maximum(a, b, c, null));
		assertThrows(NullPointerException.class, () -> Point3F.maximum(a, b, null, d));
		assertThrows(NullPointerException.class, () -> Point3F.maximum(a, null, c, d));
		assertThrows(NullPointerException.class, () -> Point3F.maximum(null, b, c, d));
	}
	
	@Test
	public void testMidpoint() {
		final Point3F a = new Point3F(2.0F, 4.0F,  8.0F);
		final Point3F b = new Point3F(4.0F, 8.0F, 16.0F);
		final Point3F c = Point3F.midpoint(a, b);
		
		assertEquals( 3.0F, c.x);
		assertEquals( 6.0F, c.y);
		assertEquals(12.0F, c.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.midpoint(a, null));
		assertThrows(NullPointerException.class, () -> Point3F.midpoint(null, b));
	}
	
	@Test
	public void testMinimum() {
		assertEquals(new Point3F(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE), Point3F.minimum());
	}
	
	@Test
	public void testMinimumPoint3FPoint3F() {
		final Point3F a = new Point3F(1.0F, 2.0F, 3.0F);
		final Point3F b = new Point3F(4.0F, 5.0F, 6.0F);
		final Point3F c = Point3F.minimum(a, b);
		
		assertEquals(1.0F, c.x);
		assertEquals(2.0F, c.y);
		assertEquals(3.0F, c.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.minimum(a, null));
		assertThrows(NullPointerException.class, () -> Point3F.minimum(null, b));
	}
	
	@Test
	public void testMinimumPoint3FPoint3FPoint3F() {
		final Point3F a = new Point3F(1.0F, 2.0F, 3.0F);
		final Point3F b = new Point3F(4.0F, 5.0F, 6.0F);
		final Point3F c = new Point3F(7.0F, 8.0F, 9.0F);
		final Point3F d = Point3F.minimum(a, b, c);
		
		assertEquals(1.0F, d.x);
		assertEquals(2.0F, d.y);
		assertEquals(3.0F, d.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.minimum(a, b, null));
		assertThrows(NullPointerException.class, () -> Point3F.minimum(a, null, c));
		assertThrows(NullPointerException.class, () -> Point3F.minimum(null, b, c));
	}
	
	@Test
	public void testMinimumPoint3FPoint3FPoint3FPoint3F() {
		final Point3F a = new Point3F( 1.0F,  2.0F,  3.0F);
		final Point3F b = new Point3F( 4.0F,  5.0F,  6.0F);
		final Point3F c = new Point3F( 7.0F,  8.0F,  9.0F);
		final Point3F d = new Point3F(10.0F, 11.0F, 12.0F);
		final Point3F e = Point3F.minimum(a, b, c, d);
		
		assertEquals(1.0F, e.x);
		assertEquals(2.0F, e.y);
		assertEquals(3.0F, e.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.minimum(a, b, c, null));
		assertThrows(NullPointerException.class, () -> Point3F.minimum(a, b, null, d));
		assertThrows(NullPointerException.class, () -> Point3F.minimum(a, null, c, d));
		assertThrows(NullPointerException.class, () -> Point3F.minimum(null, b, c, d));
	}
	
	@Test
	public void testOffset() {
		final Point3F a = Point3F.offset(new Point3F(1.0F, 1.0F, 1.0F), Vector3F.z(+1.0F), Vector3F.z(+1.0F), new Vector3F(0.0F, 0.0F, 0.0F));
		final Point3F b = Point3F.offset(new Point3F(1.0F, 1.0F, 1.0F), Vector3F.z(+1.0F), Vector3F.z(-1.0F), new Vector3F(0.0F, 0.0F, 0.0F));
		final Point3F c = Point3F.offset(new Point3F(1.0F, 1.0F, 1.0F), Vector3F.x(), Vector3F.x(), Vector3F.x());
		final Point3F d = Point3F.offset(new Point3F(1.0F, 1.0F, 1.0F), Vector3F.y(), Vector3F.y(), Vector3F.y());
		final Point3F e = Point3F.offset(new Point3F(1.0F, 1.0F, 1.0F), Vector3F.z(), Vector3F.z(), Vector3F.z());
		
		assertEquals(Math.nextDown(1.0F), a.x);
		assertEquals(Math.nextDown(1.0F), a.y);
		assertEquals(Math.nextDown(1.0F), a.z);
		
		assertEquals(Math.nextDown(1.0F), b.x);
		assertEquals(Math.nextDown(1.0F), b.y);
		assertEquals(Math.nextDown(1.0F), b.z);
		
		assertEquals(Math.nextUp(2.0F),   c.x);
		assertEquals(Math.nextDown(1.0F), c.y);
		assertEquals(Math.nextDown(1.0F), c.z);
		
		assertEquals(Math.nextDown(1.0F), d.x);
		assertEquals(Math.nextUp(2.0F),   d.y);
		assertEquals(Math.nextDown(1.0F), d.z);
		
		assertEquals(Math.nextDown(1.0F), e.x);
		assertEquals(Math.nextDown(1.0F), e.y);
		assertEquals(Math.nextUp(2.0F),   e.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.offset(new Point3F(), new Vector3F(), new Vector3F(), null));
		assertThrows(NullPointerException.class, () -> Point3F.offset(new Point3F(), new Vector3F(), null, new Vector3F()));
		assertThrows(NullPointerException.class, () -> Point3F.offset(new Point3F(), null, new Vector3F(), new Vector3F()));
		assertThrows(NullPointerException.class, () -> Point3F.offset(null, new Vector3F(), new Vector3F(), new Vector3F()));
	}
	
	@Test
	public void testRead() throws IOException {
		final Point3F a = new Point3F(1.0F, 2.0F, 3.0F);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeFloat(a.x);
		dataOutput.writeFloat(a.y);
		dataOutput.writeFloat(a.z);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Point3F b = Point3F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Point3F.read(null));
		assertThrows(UncheckedIOException.class, () -> Point3F.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testScalePoint3FVector2F() {
		final Point3F a = new Point3F(1.0F, 2.0F, 3.0F);
		final Point3F b = Point3F.scale(a, new Vector2F(2.0F, 3.0F));
		
		assertEquals(2.0F, b.x);
		assertEquals(6.0F, b.y);
		assertEquals(3.0F, b.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.scale(a, (Vector2F)(null)));
		assertThrows(NullPointerException.class, () -> Point3F.scale(null, new Vector2F(1.0F, 1.0F)));
	}
	
	@Test
	public void testScalePoint3FVector3F() {
		final Point3F a = new Point3F(1.0F, 2.0F, 3.0F);
		final Point3F b = Point3F.scale(a, new Vector3F(2.0F, 3.0F, 4.0F));
		
		assertEquals( 2.0F, b.x);
		assertEquals( 6.0F, b.y);
		assertEquals(12.0F, b.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.scale(a, (Vector3F)(null)));
		assertThrows(NullPointerException.class, () -> Point3F.scale(null, new Vector3F(1.0F, 1.0F, 1.0F)));
	}
	
	@Test
	public void testSphericalPhi() {
		final Point3F x = new Point3F(1.0F, 0.0F, 0.0F);
		final Point3F y = new Point3F(0.0F, 1.0F, 0.0F);
		final Point3F z = new Point3F(0.0F, 0.0F, 1.0F);
		
		assertEquals(0.0F,                    x.sphericalPhi());
		assertEquals((float)(Math.PI / 2.0D), y.sphericalPhi());
		assertEquals(0.0F,                    z.sphericalPhi());
	}
	
	@Test
	public void testSubtractPoint3FFloat() {
		final Point3F a = new Point3F(3.0F, 4.0F, 5.0F);
		final Point3F b = Point3F.subtract(a, 3.0F);
		
		assertEquals(0.0F, b.x);
		assertEquals(1.0F, b.y);
		assertEquals(2.0F, b.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.subtract(null, 1.0F));
	}
	
	@Test
	public void testSubtractPoint3FVector3F() {
		final Point3F a = new Point3F(3.0F, 4.0F, 5.0F);
		final Point3F b = Point3F.subtract(a, new Vector3F(1.0F, 3.0F, 5.0F));
		
		assertEquals(2.0F, b.x);
		assertEquals(1.0F, b.y);
		assertEquals(0.0F, b.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.subtract(a, (Vector3F)(null)));
		assertThrows(NullPointerException.class, () -> Point3F.subtract(null, new Vector3F(1.0F, 1.0F, 1.0F)));
	}
	
	@Test
	public void testToArray() {
		final Point3F point = new Point3F(1.0F, 2.0F, 3.0F);
		
		final float[] array = point.toArray();
		
		assertNotNull(array);
		
		assertEquals(3, array.length);
		
		assertEquals(1.0F, array[0]);
		assertEquals(2.0F, array[1]);
		assertEquals(3.0F, array[2]);
	}
	
	@Test
	public void testToString() {
		final Point3F point = new Point3F(1.0F, 2.0F, 3.0F);
		
		assertEquals("new Point3F(1.0F, 2.0F, 3.0F)", point.toString());
	}
	
	@Test
	public void testToStringPoint3FArray() {
		final Point3F a = new Point3F(1.0F, 1.0F, 1.0F);
		final Point3F b = new Point3F(2.0F, 2.0F, 2.0F);
		final Point3F c = new Point3F(3.0F, 3.0F, 3.0F);
		
		assertEquals("new Point3F[] {new Point3F(1.0F, 1.0F, 1.0F), new Point3F(2.0F, 2.0F, 2.0F), new Point3F(3.0F, 3.0F, 3.0F)}", Point3F.toString(a, b, c));
		
		assertThrows(NullPointerException.class, () -> Point3F.toString((Point3F[])(null)));
		assertThrows(NullPointerException.class, () -> Point3F.toString(a, b, c, null));
		assertThrows(NullPointerException.class, () -> Point3F.toString(new Point3F[] {a, b, c, null}));
	}
	
	@Test
	public void testTransform() {
		final Point3F a = new Point3F(1.0F, 2.0F, 3.0F);
		final Point3F b = Point3F.transform(Matrix44F.scale(1.0F, 2.0F, 3.0F), a);
		final Point3F c = Point3F.transform(Matrix44F.translate(1.0F, 2.0F, 3.0F), a);
		
		assertEquals(1.0F, b.x);
		assertEquals(4.0F, b.y);
		assertEquals(9.0F, b.z);
		
		assertEquals(2.0F, c.x);
		assertEquals(4.0F, c.y);
		assertEquals(6.0F, c.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.transform(Matrix44F.translate(1.0F, 2.0F, 3.0F), null));
		assertThrows(NullPointerException.class, () -> Point3F.transform(null, a));
	}
	
	@Test
	public void testTransformAndDivide() {
		final Point3F a = new Point3F(1.0F, 2.0F, 3.0F);
		final Point3F b = Point3F.transformAndDivide(Matrix44F.scale(1.0F, 2.0F, 3.0F), a);
		final Point3F c = Point3F.transformAndDivide(Matrix44F.translate(1.0F, 2.0F, 3.0F), a);
		final Point3F d = Point3F.transformAndDivide(new Matrix44F(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0F), a);
		final Point3F e = Point3F.transformAndDivide(new Matrix44F(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), a);
		
		assertEquals(1.0F, b.x);
		assertEquals(4.0F, b.y);
		assertEquals(9.0F, b.z);
		
		assertEquals(2.0F, c.x);
		assertEquals(4.0F, c.y);
		assertEquals(6.0F, c.z);
		
		assertEquals(0.5F, d.x);
		assertEquals(1.0F, d.y);
		assertEquals(1.5F, d.z);
		
		assertEquals(1.0F, e.x);
		assertEquals(2.0F, e.y);
		assertEquals(3.0F, e.z);
		
		assertThrows(NullPointerException.class, () -> Point3F.transformAndDivide(Matrix44F.translate(1.0F, 2.0F, 3.0F), null));
		assertThrows(NullPointerException.class, () -> Point3F.transformAndDivide(null, a));
	}
	
	@Test
	public void testWrite() {
		final Point3F a = new Point3F(1.0F, 2.0F, 3.0F);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Point3F b = Point3F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}