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
public final class Point2DUnitTests {
	public Point2DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAddPoint2DDouble() {
		final Point2D a = new Point2D(1.0D, 2.0D);
		final Point2D b = Point2D.add(a, 2.0D);
		
		assertEquals(3.0D, b.getComponent1());
		assertEquals(4.0D, b.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2D.add(null, 2.0D));
	}
	
	@Test
	public void testAddPoint2DVector2D() {
		final Point2D a = new Point2D(1.0D, 2.0D);
		final Point2D b = Point2D.add(a, new Vector2D(1.0D, 2.0D));
		
		assertEquals(2.0D, b.getComponent1());
		assertEquals(4.0D, b.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2D.add(a, null));
		assertThrows(NullPointerException.class, () -> Point2D.add(null, new Vector2D(1.0D, 2.0D)));
	}
	
	@Test
	public void testAddPoint2DVector2DDouble() {
		final Point2D a = new Point2D(1.0D, 2.0D);
		final Point2D b = Point2D.add(a, new Vector2D(1.0D, 2.0D), 2.0D);
		
		assertEquals(3.0D, b.getComponent1());
		assertEquals(6.0D, b.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2D.add(a, null, 2.0D));
		assertThrows(NullPointerException.class, () -> Point2D.add(null, new Vector2D(1.0D, 2.0D), 2.0D));
	}
	
	@Test
	public void testCentroidPoint2DPoint2D() {
		final Point2D a = new Point2D(2.0D, 4.0D);
		final Point2D b = new Point2D(4.0D, 8.0D);
		final Point2D c = Point2D.centroid(a, b);
		
		assertEquals(3.0D, c.getComponent1());
		assertEquals(6.0D, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2D.centroid(a, null));
		assertThrows(NullPointerException.class, () -> Point2D.centroid(null, b));
	}
	
	@Test
	public void testCentroidPoint2DPoint2DPoint2D() {
		final Point2D a = new Point2D(2.0D,  4.0D);
		final Point2D b = new Point2D(4.0D,  8.0D);
		final Point2D c = new Point2D(6.0D, 12.0D);
		final Point2D d = Point2D.centroid(a, b, c);
		
		assertEquals(4.0D, d.getComponent1());
		assertEquals(8.0D, d.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2D.centroid(a, b, null));
		assertThrows(NullPointerException.class, () -> Point2D.centroid(a, null, c));
		assertThrows(NullPointerException.class, () -> Point2D.centroid(null, b, c));
	}
	
	@Test
	public void testCentroidPoint2DPoint2DPoint2DPoint2D() {
		final Point2D a = new Point2D(2.0D,  4.0D);
		final Point2D b = new Point2D(4.0D,  8.0D);
		final Point2D c = new Point2D(6.0D, 12.0D);
		final Point2D d = new Point2D(8.0D, 16.0D);
		final Point2D e = Point2D.centroid(a, b, c, d);
		
		assertEquals( 5.0D, e.getComponent1());
		assertEquals(10.0D, e.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2D.centroid(a, b, c, null));
		assertThrows(NullPointerException.class, () -> Point2D.centroid(a, b, null, d));
		assertThrows(NullPointerException.class, () -> Point2D.centroid(a, null, c, d));
		assertThrows(NullPointerException.class, () -> Point2D.centroid(null, b, c, d));
	}
	
	@Test
	public void testCentroidPoint2DPoint2DPoint2DPoint2DPoint2DPoint2DPoint2DPoint2D() {
		final Point2D a = new Point2D( 2.0D,  4.0D);
		final Point2D b = new Point2D( 4.0D,  8.0D);
		final Point2D c = new Point2D( 6.0D, 12.0D);
		final Point2D d = new Point2D( 8.0D, 16.0D);
		final Point2D e = new Point2D(10.0D, 20.0D);
		final Point2D f = new Point2D(12.0D, 24.0D);
		final Point2D g = new Point2D(14.0D, 28.0D);
		final Point2D h = new Point2D(16.0D, 32.0D);
		final Point2D i = Point2D.centroid(a, b, c, d, e, f, g, h);
		
		assertEquals( 9.0D, i.getComponent1());
		assertEquals(18.0D, i.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2D.centroid(a, b, c, d, e, f, g, null));
		assertThrows(NullPointerException.class, () -> Point2D.centroid(a, b, c, d, e, f, null, h));
		assertThrows(NullPointerException.class, () -> Point2D.centroid(a, b, c, d, e, null, g, h));
		assertThrows(NullPointerException.class, () -> Point2D.centroid(a, b, c, d, null, f, g, h));
		assertThrows(NullPointerException.class, () -> Point2D.centroid(a, b, c, null, e, f, g, h));
		assertThrows(NullPointerException.class, () -> Point2D.centroid(a, b, null, d, e, f, g, h));
		assertThrows(NullPointerException.class, () -> Point2D.centroid(a, null, c, d, e, f, g, h));
		assertThrows(NullPointerException.class, () -> Point2D.centroid(null, b, c, d, e, f, g, h));
	}
	
	@Test
	public void testClearCacheAndGetCacheSizeAndGetCached() {
		assertEquals(0, Point2D.getCacheSize());
		
		final Point2D a = new Point2D(1.0D, 2.0D);
		final Point2D b = new Point2D(1.0D, 2.0D);
		final Point2D c = Point2D.getCached(a);
		final Point2D d = Point2D.getCached(b);
		
		assertThrows(NullPointerException.class, () -> Point2D.getCached(null));
		
		assertEquals(1, Point2D.getCacheSize());
		
		Point2D.clearCache();
		
		assertEquals(0, Point2D.getCacheSize());
		
		assertTrue(a != b);
		assertTrue(a == c);
		assertTrue(a == d);
		
		assertTrue(b != a);
		assertTrue(b != c);
		assertTrue(b != d);
	}
	
	@Test
	public void testConstants() {
		assertEquals(new Point2D(+Double.MAX_VALUE, +Double.MAX_VALUE), Point2D.MAXIMUM);
		assertEquals(new Point2D(-Double.MAX_VALUE, -Double.MAX_VALUE), Point2D.MINIMUM);
	}
	
	@Test
	public void testConstructor() {
		final Point2D point = new Point2D();
		
		assertEquals(0.0D, point.getComponent1());
		assertEquals(0.0D, point.getComponent2());
	}
	
	@Test
	public void testConstructorDouble() {
		final Point2D point = new Point2D(1.0D);
		
		assertEquals(1.0D, point.getComponent1());
		assertEquals(1.0D, point.getComponent2());
	}
	
	@Test
	public void testConstructorDoubleDouble() {
		final Point2D point = new Point2D(1.0D, 2.0D);
		
		assertEquals(1.0D, point.getComponent1());
		assertEquals(2.0D, point.getComponent2());
	}
	
	@Test
	public void testConstructorPoint2F() {
		final Point2D point = new Point2D(new Point2F(1.0F, 1.0F));
		
		assertEquals(1.0D, point.getComponent1());
		assertEquals(1.0D, point.getComponent2());
		
		assertThrows(NullPointerException.class, () -> new Point2D((Point2F)(null)));
	}
	
	@Test
	public void testConstructorVector2D() {
		final Point2D point = new Point2D(new Vector2D(1.0F, 1.0F));
		
		assertEquals(1.0D, point.getComponent1());
		assertEquals(1.0D, point.getComponent2());
		
		assertThrows(NullPointerException.class, () -> new Point2D((Vector2D)(null)));
	}
	
	@Test
	public void testDistance() {
		final Point2D a = new Point2D(0.0D, 0.0D);
		final Point2D b = new Point2D(9.0D, 0.0D);
		final Point2D c = new Point2D(0.0D, 9.0D);
		
		assertEquals(9.0D, Point2D.distance(a, b));
		assertEquals(9.0D, Point2D.distance(a, c));
		
		assertThrows(NullPointerException.class, () -> Point2D.distance(a, null));
		assertThrows(NullPointerException.class, () -> Point2D.distance(null, b));
	}
	
	@Test
	public void testDistanceSquared() {
		final Point2D a = new Point2D(0.0D, 0.0D);
		final Point2D b = new Point2D(9.0D, 0.0D);
		final Point2D c = new Point2D(0.0D, 9.0D);
		
		assertEquals(81.0D, Point2D.distanceSquared(a, b));
		assertEquals(81.0D, Point2D.distanceSquared(a, c));
		
		assertThrows(NullPointerException.class, () -> Point2D.distanceSquared(a, null));
		assertThrows(NullPointerException.class, () -> Point2D.distanceSquared(null, b));
	}
	
	@Test
	public void testEquals() {
		final Point2D a = new Point2D(0.0D, 1.0D);
		final Point2D b = new Point2D(0.0D, 1.0D);
		final Point2D c = new Point2D(0.0D, 2.0D);
		final Point2D d = new Point2D(2.0D, 1.0D);
		final Point2D e = null;
		
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
	public void testGetComponent() {
		final Point2D point = new Point2D(1.0D, 2.0D);
		
		assertEquals(1.0D, point.getComponent(0));
		assertEquals(2.0D, point.getComponent(1));
		
		assertThrows(IllegalArgumentException.class, () -> point.getComponent(-1));
		assertThrows(IllegalArgumentException.class, () -> point.getComponent(+2));
	}
	
	@Test
	public void testGetComponent1() {
		final Point2D point = new Point2D(2.0D, 0.0D);
		
		assertEquals(2.0D, point.getComponent1());
	}
	
	@Test
	public void testGetComponent2() {
		final Point2D point = new Point2D(0.0D, 2.0D);
		
		assertEquals(2.0D, point.getComponent2());
	}
	
	@Test
	public void testGetLatitude() {
		final Point2D point = new Point2D(0.0D, 2.0D);
		
		assertEquals(2.0D, point.getLatitude());
	}
	
	@Test
	public void testGetLongitude() {
		final Point2D point = new Point2D(2.0D, 0.0D);
		
		assertEquals(2.0D, point.getLongitude());
	}
	
	@Test
	public void testGetU() {
		final Point2D point = new Point2D(2.0D, 0.0D);
		
		assertEquals(2.0D, point.getU());
	}
	
	@Test
	public void testGetV() {
		final Point2D point = new Point2D(0.0D, 2.0D);
		
		assertEquals(2.0D, point.getV());
	}
	
	@Test
	public void testGetX() {
		final Point2D point = new Point2D(2.0D, 0.0D);
		
		assertEquals(2.0D, point.getX());
	}
	
	@Test
	public void testGetY() {
		final Point2D point = new Point2D(0.0D, 2.0D);
		
		assertEquals(2.0D, point.getY());
	}
	
	@Test
	public void testHashCode() {
		final Point2D a = new Point2D(0.5D, 1.0D);
		final Point2D b = new Point2D(0.5D, 1.0D);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testLerp() {
		final Point2D a = new Point2D(1.0D, 1.0D);
		final Point2D b = new Point2D(5.0D, 5.0D);
		final Point2D c = Point2D.lerp(a, b, 0.25D);
		
		assertEquals(2.0D, c.getComponent1());
		assertEquals(2.0D, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2D.lerp(a, null, 0.25D));
		assertThrows(NullPointerException.class, () -> Point2D.lerp(null, b, 0.25D));
	}
	
	@Test
	public void testMaximum() {
		assertEquals(new Point2D(+Double.MAX_VALUE, +Double.MAX_VALUE), Point2D.maximum());
	}
	
	@Test
	public void testMaximumPoint2DPoint2D() {
		final Point2D a = new Point2D(1.0D, 2.0D);
		final Point2D b = new Point2D(3.0D, 4.0D);
		final Point2D c = Point2D.maximum(a, b);
		
		assertEquals(3.0D, c.getComponent1());
		assertEquals(4.0D, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2D.maximum(a, null));
		assertThrows(NullPointerException.class, () -> Point2D.maximum(null, b));
	}
	
	@Test
	public void testMaximumPoint2DPoint2DPoint2D() {
		final Point2D a = new Point2D(1.0D, 2.0D);
		final Point2D b = new Point2D(3.0D, 4.0D);
		final Point2D c = new Point2D(5.0D, 6.0D);
		final Point2D d = Point2D.maximum(a, b, c);
		
		assertEquals(5.0D, d.getComponent1());
		assertEquals(6.0D, d.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2D.maximum(a, b, null));
		assertThrows(NullPointerException.class, () -> Point2D.maximum(a, null, c));
		assertThrows(NullPointerException.class, () -> Point2D.maximum(null, b, c));
	}
	
	@Test
	public void testMaximumPoint2DPoint2DPoint2DPoint2D() {
		final Point2D a = new Point2D(1.0D, 2.0D);
		final Point2D b = new Point2D(3.0D, 4.0D);
		final Point2D c = new Point2D(5.0D, 6.0D);
		final Point2D d = new Point2D(7.0D, 8.0D);
		final Point2D e = Point2D.maximum(a, b, c, d);
		
		assertEquals(7.0D, e.getComponent1());
		assertEquals(8.0D, e.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2D.maximum(a, b, c, null));
		assertThrows(NullPointerException.class, () -> Point2D.maximum(a, b, null, d));
		assertThrows(NullPointerException.class, () -> Point2D.maximum(a, null, c, d));
		assertThrows(NullPointerException.class, () -> Point2D.maximum(null, b, c, d));
	}
	
	@Test
	public void testMidpoint() {
		final Point2D a = new Point2D(2.0D, 4.0D);
		final Point2D b = new Point2D(4.0D, 8.0D);
		final Point2D c = Point2D.midpoint(a, b);
		
		assertEquals(3.0D, c.getComponent1());
		assertEquals(6.0D, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2D.midpoint(a, null));
		assertThrows(NullPointerException.class, () -> Point2D.midpoint(null, b));
	}
	
	@Test
	public void testMinimum() {
		assertEquals(new Point2D(-Double.MAX_VALUE, -Double.MAX_VALUE), Point2D.minimum());
	}
	
	@Test
	public void testMinimumPoint2DPoint2D() {
		final Point2D a = new Point2D(1.0D, 2.0D);
		final Point2D b = new Point2D(3.0D, 4.0D);
		final Point2D c = Point2D.minimum(a, b);
		
		assertEquals(1.0D, c.getComponent1());
		assertEquals(2.0D, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2D.minimum(a, null));
		assertThrows(NullPointerException.class, () -> Point2D.minimum(null, b));
	}
	
	@Test
	public void testMinimumPoint2DPoint2DPoint2D() {
		final Point2D a = new Point2D(1.0D, 2.0D);
		final Point2D b = new Point2D(3.0D, 4.0D);
		final Point2D c = new Point2D(5.0D, 6.0D);
		final Point2D d = Point2D.minimum(a, b, c);
		
		assertEquals(1.0D, d.getComponent1());
		assertEquals(2.0D, d.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2D.minimum(a, b, null));
		assertThrows(NullPointerException.class, () -> Point2D.minimum(a, null, c));
		assertThrows(NullPointerException.class, () -> Point2D.minimum(null, b, c));
	}
	
	@Test
	public void testMinimumPoint2DPoint2DPoint2DPoint2D() {
		final Point2D a = new Point2D(1.0D, 2.0D);
		final Point2D b = new Point2D(3.0D, 4.0D);
		final Point2D c = new Point2D(5.0D, 6.0D);
		final Point2D d = new Point2D(7.0D, 8.0D);
		final Point2D e = Point2D.minimum(a, b, c, d);
		
		assertEquals(1.0D, e.getComponent1());
		assertEquals(2.0D, e.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2D.minimum(a, b, c, null));
		assertThrows(NullPointerException.class, () -> Point2D.minimum(a, b, null, d));
		assertThrows(NullPointerException.class, () -> Point2D.minimum(a, null, c, d));
		assertThrows(NullPointerException.class, () -> Point2D.minimum(null, b, c, d));
	}
	
	@Test
	public void testRead() throws IOException {
		final Point2D a = new Point2D(1.0D, 0.5D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeDouble(a.getComponent1());
		dataOutput.writeDouble(a.getComponent2());
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Point2D b = Point2D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Point2D.read(null));
		assertThrows(UncheckedIOException.class, () -> Point2D.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testSubtractPoint2DDouble() {
		final Point2D a = new Point2D(2.0D, 4.0D);
		final Point2D b = Point2D.subtract(a, 2.0D);
		
		assertEquals(0.0D, b.getComponent1());
		assertEquals(2.0D, b.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2D.subtract(null, 2.0D));
	}
	
	@Test
	public void testSubtractPoint2DVector2D() {
		final Point2D a = new Point2D(2.0D, 4.0D);
		final Point2D b = Point2D.subtract(a, new Vector2D(1.0D, 2.0D));
		
		assertEquals(1.0D, b.getComponent1());
		assertEquals(2.0D, b.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2D.subtract(null, new Vector2D(1.0D, 2.0D)));
		assertThrows(NullPointerException.class, () -> Point2D.subtract(a, null));
	}
	
	@Test
	public void testToArray() {
		final Point2D point = new Point2D(1.0D, 2.0D);
		
		final double[] array = point.toArray();
		
		assertNotNull(array);
		
		assertEquals(2, array.length);
		
		assertEquals(1.0D, array[0]);
		assertEquals(2.0D, array[1]);
	}
	
	@Test
	public void testToString() {
		final Point2D point = new Point2D(0.5D, 1.0D);
		
		assertEquals("new Point2D(0.5D, 1.0D)", point.toString());
	}
	
	@Test
	public void testToStringPoint2DArray() {
		final Point2D a = new Point2D(1.0D, 1.0D);
		final Point2D b = new Point2D(2.0D, 2.0D);
		final Point2D c = new Point2D(3.0D, 3.0D);
		
		assertEquals("new Point2D[] {new Point2D(1.0D, 1.0D), new Point2D(2.0D, 2.0D), new Point2D(3.0D, 3.0D)}", Point2D.toString(a, b, c));
		
		assertThrows(NullPointerException.class, () -> Point2D.toString((Point2D[])(null)));
		assertThrows(NullPointerException.class, () -> Point2D.toString(a, b, c, null));
		assertThrows(NullPointerException.class, () -> Point2D.toString(new Point2D[] {a, b, c, null}));
	}
	
	@Test
	public void testWrite() {
		final Point2D a = new Point2D(1.0D, 0.5D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Point2D b = Point2D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
	}
}