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
public final class Point2FUnitTests {
	public Point2FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAddPoint2FFloat() {
		final Point2F a = new Point2F(1.0F, 2.0F);
		final Point2F b = Point2F.add(a, 2.0F);
		
		assertEquals(3.0F, b.getComponent1());
		assertEquals(4.0F, b.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2F.add(null, 2.0F));
	}
	
	@Test
	public void testAddPoint2FVector2F() {
		final Point2F a = new Point2F(1.0F, 2.0F);
		final Point2F b = Point2F.add(a, new Vector2F(1.0F, 2.0F));
		
		assertEquals(2.0F, b.getComponent1());
		assertEquals(4.0F, b.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2F.add(a, null));
		assertThrows(NullPointerException.class, () -> Point2F.add(null, new Vector2F(1.0F, 2.0F)));
	}
	
	@Test
	public void testAddPoint2FVector2FFloat() {
		final Point2F a = new Point2F(1.0F, 2.0F);
		final Point2F b = Point2F.add(a, new Vector2F(1.0F, 2.0F), 2.0F);
		
		assertEquals(3.0F, b.getComponent1());
		assertEquals(6.0F, b.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2F.add(a, null, 2.0F));
		assertThrows(NullPointerException.class, () -> Point2F.add(null, new Vector2F(1.0F, 2.0F), 2.0F));
	}
	
	@Test
	public void testCentroidPoint2FPoint2F() {
		final Point2F a = new Point2F(2.0F, 4.0F);
		final Point2F b = new Point2F(4.0F, 8.0F);
		final Point2F c = Point2F.centroid(a, b);
		
		assertEquals(3.0F, c.getComponent1());
		assertEquals(6.0F, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2F.centroid(a, null));
		assertThrows(NullPointerException.class, () -> Point2F.centroid(null, b));
	}
	
	@Test
	public void testCentroidPoint2FPoint2FPoint2F() {
		final Point2F a = new Point2F(2.0F,  4.0F);
		final Point2F b = new Point2F(4.0F,  8.0F);
		final Point2F c = new Point2F(6.0F, 12.0F);
		final Point2F d = Point2F.centroid(a, b, c);
		
		assertEquals(4.0F, d.getComponent1());
		assertEquals(8.0F, d.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2F.centroid(a, b, null));
		assertThrows(NullPointerException.class, () -> Point2F.centroid(a, null, c));
		assertThrows(NullPointerException.class, () -> Point2F.centroid(null, b, c));
	}
	
	@Test
	public void testCentroidPoint2FPoint2FPoint2FPoint2F() {
		final Point2F a = new Point2F(2.0F,  4.0F);
		final Point2F b = new Point2F(4.0F,  8.0F);
		final Point2F c = new Point2F(6.0F, 12.0F);
		final Point2F d = new Point2F(8.0F, 16.0F);
		final Point2F e = Point2F.centroid(a, b, c, d);
		
		assertEquals( 5.0F, e.getComponent1());
		assertEquals(10.0F, e.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2F.centroid(a, b, c, null));
		assertThrows(NullPointerException.class, () -> Point2F.centroid(a, b, null, d));
		assertThrows(NullPointerException.class, () -> Point2F.centroid(a, null, c, d));
		assertThrows(NullPointerException.class, () -> Point2F.centroid(null, b, c, d));
	}
	
	@Test
	public void testCentroidPoint2FPoint2FPoint2FPoint2FPoint2FPoint2FPoint2FPoint2F() {
		final Point2F a = new Point2F( 2.0F,  4.0F);
		final Point2F b = new Point2F( 4.0F,  8.0F);
		final Point2F c = new Point2F( 6.0F, 12.0F);
		final Point2F d = new Point2F( 8.0F, 16.0F);
		final Point2F e = new Point2F(10.0F, 20.0F);
		final Point2F f = new Point2F(12.0F, 24.0F);
		final Point2F g = new Point2F(14.0F, 28.0F);
		final Point2F h = new Point2F(16.0F, 32.0F);
		final Point2F i = Point2F.centroid(a, b, c, d, e, f, g, h);
		
		assertEquals( 9.0F, i.getComponent1());
		assertEquals(18.0F, i.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2F.centroid(a, b, c, d, e, f, g, null));
		assertThrows(NullPointerException.class, () -> Point2F.centroid(a, b, c, d, e, f, null, h));
		assertThrows(NullPointerException.class, () -> Point2F.centroid(a, b, c, d, e, null, g, h));
		assertThrows(NullPointerException.class, () -> Point2F.centroid(a, b, c, d, null, f, g, h));
		assertThrows(NullPointerException.class, () -> Point2F.centroid(a, b, c, null, e, f, g, h));
		assertThrows(NullPointerException.class, () -> Point2F.centroid(a, b, null, d, e, f, g, h));
		assertThrows(NullPointerException.class, () -> Point2F.centroid(a, null, c, d, e, f, g, h));
		assertThrows(NullPointerException.class, () -> Point2F.centroid(null, b, c, d, e, f, g, h));
	}
	
	@Test
	public void testClearCacheAndGetCacheSizeAndGetCached() {
		assertEquals(0, Point2F.getCacheSize());
		
		final Point2F a = new Point2F(1.0F, 2.0F);
		final Point2F b = new Point2F(1.0F, 2.0F);
		final Point2F c = Point2F.getCached(a);
		final Point2F d = Point2F.getCached(b);
		
		assertThrows(NullPointerException.class, () -> Point2F.getCached(null));
		
		assertEquals(1, Point2F.getCacheSize());
		
		Point2F.clearCache();
		
		assertEquals(0, Point2F.getCacheSize());
		
		assertTrue(a != b);
		assertTrue(a == c);
		assertTrue(a == d);
		
		assertTrue(b != a);
		assertTrue(b != c);
		assertTrue(b != d);
	}
	
	@Test
	public void testConstants() {
		assertEquals(new Point2F(+Float.MAX_VALUE, +Float.MAX_VALUE), Point2F.MAXIMUM);
		assertEquals(new Point2F(-Float.MAX_VALUE, -Float.MAX_VALUE), Point2F.MINIMUM);
	}
	
	@Test
	public void testConstructor() {
		final Point2F point = new Point2F();
		
		assertEquals(0.0F, point.getComponent1());
		assertEquals(0.0F, point.getComponent2());
	}
	
	@Test
	public void testConstructorFloat() {
		final Point2F point = new Point2F(1.0F);
		
		assertEquals(1.0F, point.getComponent1());
		assertEquals(1.0F, point.getComponent2());
	}
	
	@Test
	public void testConstructorFloatFloat() {
		final Point2F point = new Point2F(1.0F, 2.0F);
		
		assertEquals(1.0F, point.getComponent1());
		assertEquals(2.0F, point.getComponent2());
	}
	
	@Test
	public void testConstructorPoint2D() {
		final Point2F point = new Point2F(new Point2D(1.0D, 1.0D));
		
		assertEquals(1.0F, point.getComponent1());
		assertEquals(1.0F, point.getComponent2());
		
		assertThrows(NullPointerException.class, () -> new Point2F((Point2D)(null)));
	}
	
	@Test
	public void testConstructorVector2F() {
		final Point2F point = new Point2F(new Vector2F(1.0F, 1.0F));
		
		assertEquals(1.0F, point.getComponent1());
		assertEquals(1.0F, point.getComponent2());
		
		assertThrows(NullPointerException.class, () -> new Point2F((Vector2F)(null)));
	}
	
	@Test
	public void testDistance() {
		final Point2F a = new Point2F(0.0F, 0.0F);
		final Point2F b = new Point2F(9.0F, 0.0F);
		final Point2F c = new Point2F(0.0F, 9.0F);
		
		assertEquals(9.0F, Point2F.distance(a, b));
		assertEquals(9.0F, Point2F.distance(a, c));
		
		assertThrows(NullPointerException.class, () -> Point2F.distance(a, null));
		assertThrows(NullPointerException.class, () -> Point2F.distance(null, b));
	}
	
	@Test
	public void testDistanceSquared() {
		final Point2F a = new Point2F(0.0F, 0.0F);
		final Point2F b = new Point2F(9.0F, 0.0F);
		final Point2F c = new Point2F(0.0F, 9.0F);
		
		assertEquals(81.0F, Point2F.distanceSquared(a, b));
		assertEquals(81.0F, Point2F.distanceSquared(a, c));
		
		assertThrows(NullPointerException.class, () -> Point2F.distanceSquared(a, null));
		assertThrows(NullPointerException.class, () -> Point2F.distanceSquared(null, b));
	}
	
	@Test
	public void testEquals() {
		final Point2F a = new Point2F(0.0F, 1.0F);
		final Point2F b = new Point2F(0.0F, 1.0F);
		final Point2F c = new Point2F(0.0F, 2.0F);
		final Point2F d = new Point2F(2.0F, 1.0F);
		final Point2F e = null;
		
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
		final Point2F point = new Point2F(1.0F, 2.0F);
		
		assertEquals(1.0F, point.getComponent(0));
		assertEquals(2.0F, point.getComponent(1));
		
		assertThrows(IllegalArgumentException.class, () -> point.getComponent(-1));
		assertThrows(IllegalArgumentException.class, () -> point.getComponent(+2));
	}
	
	@Test
	public void testGetComponent1() {
		final Point2F point = new Point2F(2.0F, 0.0F);
		
		assertEquals(2.0F, point.getComponent1());
	}
	
	@Test
	public void testGetComponent2() {
		final Point2F point = new Point2F(0.0F, 2.0F);
		
		assertEquals(2.0F, point.getComponent2());
	}
	
	@Test
	public void testGetLatitude() {
		final Point2F point = new Point2F(0.0F, 2.0F);
		
		assertEquals(2.0F, point.getLatitude());
	}
	
	@Test
	public void testGetLongitude() {
		final Point2F point = new Point2F(2.0F, 0.0F);
		
		assertEquals(2.0F, point.getLongitude());
	}
	
	@Test
	public void testGetU() {
		final Point2F point = new Point2F(2.0F, 0.0F);
		
		assertEquals(2.0F, point.getU());
	}
	
	@Test
	public void testGetV() {
		final Point2F point = new Point2F(0.0F, 2.0F);
		
		assertEquals(2.0F, point.getV());
	}
	
	@Test
	public void testGetX() {
		final Point2F point = new Point2F(2.0F, 0.0F);
		
		assertEquals(2.0F, point.getX());
	}
	
	@Test
	public void testGetY() {
		final Point2F point = new Point2F(0.0F, 2.0F);
		
		assertEquals(2.0F, point.getY());
	}
	
	@Test
	public void testHashCode() {
		final Point2F a = new Point2F(0.5F, 1.0F);
		final Point2F b = new Point2F(0.5F, 1.0F);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testLerp() {
		final Point2F a = new Point2F(1.0F, 1.0F);
		final Point2F b = new Point2F(5.0F, 5.0F);
		final Point2F c = Point2F.lerp(a, b, 0.25F);
		
		assertEquals(2.0F, c.getComponent1());
		assertEquals(2.0F, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2F.lerp(a, null, 0.25F));
		assertThrows(NullPointerException.class, () -> Point2F.lerp(null, b, 0.25F));
	}
	
	@Test
	public void testMaximum() {
		assertEquals(new Point2F(+Float.MAX_VALUE, +Float.MAX_VALUE), Point2F.maximum());
	}
	
	@Test
	public void testMaximumPoint2FPoint2F() {
		final Point2F a = new Point2F(1.0F, 2.0F);
		final Point2F b = new Point2F(3.0F, 4.0F);
		final Point2F c = Point2F.maximum(a, b);
		
		assertEquals(3.0F, c.getComponent1());
		assertEquals(4.0F, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2F.maximum(a, null));
		assertThrows(NullPointerException.class, () -> Point2F.maximum(null, b));
	}
	
	@Test
	public void testMaximumPoint2FPoint2FPoint2F() {
		final Point2F a = new Point2F(1.0F, 2.0F);
		final Point2F b = new Point2F(3.0F, 4.0F);
		final Point2F c = new Point2F(5.0F, 6.0F);
		final Point2F d = Point2F.maximum(a, b, c);
		
		assertEquals(5.0F, d.getComponent1());
		assertEquals(6.0F, d.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2F.maximum(a, b, null));
		assertThrows(NullPointerException.class, () -> Point2F.maximum(a, null, c));
		assertThrows(NullPointerException.class, () -> Point2F.maximum(null, b, c));
	}
	
	@Test
	public void testMaximumPoint2FPoint2FPoint2FPoint2F() {
		final Point2F a = new Point2F(1.0F, 2.0F);
		final Point2F b = new Point2F(3.0F, 4.0F);
		final Point2F c = new Point2F(5.0F, 6.0F);
		final Point2F d = new Point2F(7.0F, 8.0F);
		final Point2F e = Point2F.maximum(a, b, c, d);
		
		assertEquals(7.0F, e.getComponent1());
		assertEquals(8.0F, e.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2F.maximum(a, b, c, null));
		assertThrows(NullPointerException.class, () -> Point2F.maximum(a, b, null, d));
		assertThrows(NullPointerException.class, () -> Point2F.maximum(a, null, c, d));
		assertThrows(NullPointerException.class, () -> Point2F.maximum(null, b, c, d));
	}
	
	@Test
	public void testMidpoint() {
		final Point2F a = new Point2F(2.0F, 4.0F);
		final Point2F b = new Point2F(4.0F, 8.0F);
		final Point2F c = Point2F.midpoint(a, b);
		
		assertEquals(3.0F, c.getComponent1());
		assertEquals(6.0F, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2F.midpoint(a, null));
		assertThrows(NullPointerException.class, () -> Point2F.midpoint(null, b));
	}
	
	@Test
	public void testMinimum() {
		assertEquals(new Point2F(-Float.MAX_VALUE, -Float.MAX_VALUE), Point2F.minimum());
	}
	
	@Test
	public void testMinimumPoint2FPoint2F() {
		final Point2F a = new Point2F(1.0F, 2.0F);
		final Point2F b = new Point2F(3.0F, 4.0F);
		final Point2F c = Point2F.minimum(a, b);
		
		assertEquals(1.0F, c.getComponent1());
		assertEquals(2.0F, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2F.minimum(a, null));
		assertThrows(NullPointerException.class, () -> Point2F.minimum(null, b));
	}
	
	@Test
	public void testMinimumPoint2FPoint2FPoint2F() {
		final Point2F a = new Point2F(1.0F, 2.0F);
		final Point2F b = new Point2F(3.0F, 4.0F);
		final Point2F c = new Point2F(5.0F, 6.0F);
		final Point2F d = Point2F.minimum(a, b, c);
		
		assertEquals(1.0F, d.getComponent1());
		assertEquals(2.0F, d.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2F.minimum(a, b, null));
		assertThrows(NullPointerException.class, () -> Point2F.minimum(a, null, c));
		assertThrows(NullPointerException.class, () -> Point2F.minimum(null, b, c));
	}
	
	@Test
	public void testMinimumPoint2FPoint2FPoint2FPoint2F() {
		final Point2F a = new Point2F(1.0F, 2.0F);
		final Point2F b = new Point2F(3.0F, 4.0F);
		final Point2F c = new Point2F(5.0F, 6.0F);
		final Point2F d = new Point2F(7.0F, 8.0F);
		final Point2F e = Point2F.minimum(a, b, c, d);
		
		assertEquals(1.0F, e.getComponent1());
		assertEquals(2.0F, e.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2F.minimum(a, b, c, null));
		assertThrows(NullPointerException.class, () -> Point2F.minimum(a, b, null, d));
		assertThrows(NullPointerException.class, () -> Point2F.minimum(a, null, c, d));
		assertThrows(NullPointerException.class, () -> Point2F.minimum(null, b, c, d));
	}
	
	@Test
	public void testRead() throws IOException {
		final Point2F a = new Point2F(1.0F, 0.5F);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeFloat(a.getComponent1());
		dataOutput.writeFloat(a.getComponent2());
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Point2F b = Point2F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Point2F.read(null));
		assertThrows(UncheckedIOException.class, () -> Point2F.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testSubtractPoint2FFloat() {
		final Point2F a = new Point2F(2.0F, 4.0F);
		final Point2F b = Point2F.subtract(a, 2.0F);
		
		assertEquals(0.0F, b.getComponent1());
		assertEquals(2.0F, b.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2F.subtract(null, 2.0F));
	}
	
	@Test
	public void testSubtractPoint2FVector2F() {
		final Point2F a = new Point2F(2.0F, 4.0F);
		final Point2F b = Point2F.subtract(a, new Vector2F(1.0F, 2.0F));
		
		assertEquals(1.0F, b.getComponent1());
		assertEquals(2.0F, b.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2F.subtract(null, new Vector2F(1.0F, 2.0F)));
		assertThrows(NullPointerException.class, () -> Point2F.subtract(a, null));
	}
	
	@Test
	public void testToArray() {
		final Point2F point = new Point2F(1.0F, 2.0F);
		
		final float[] array = point.toArray();
		
		assertNotNull(array);
		
		assertEquals(2, array.length);
		
		assertEquals(1.0F, array[0]);
		assertEquals(2.0F, array[1]);
	}
	
	@Test
	public void testToString() {
		final Point2F point = new Point2F(0.5F, 1.0F);
		
		assertEquals("new Point2F(0.5F, 1.0F)", point.toString());
	}
	
	@Test
	public void testToStringPoint2FArray() {
		final Point2F a = new Point2F(1.0F, 1.0F);
		final Point2F b = new Point2F(2.0F, 2.0F);
		final Point2F c = new Point2F(3.0F, 3.0F);
		
		assertEquals("new Point2F[] {new Point2F(1.0F, 1.0F), new Point2F(2.0F, 2.0F), new Point2F(3.0F, 3.0F)}", Point2F.toString(a, b, c));
		
		assertThrows(NullPointerException.class, () -> Point2F.toString((Point2F[])(null)));
		assertThrows(NullPointerException.class, () -> Point2F.toString(a, b, c, null));
		assertThrows(NullPointerException.class, () -> Point2F.toString(new Point2F[] {a, b, c, null}));
	}
	
	@Test
	public void testTransform() {
		final Point2F a = new Point2F(1.0F, 2.0F);
		final Point2F b = Point2F.transform(Matrix33F.scale(1.0F, 2.0F), a);
		final Point2F c = Point2F.transform(Matrix33F.translate(1.0F, 2.0F), a);
		
		assertEquals(1.0F, b.getComponent1());
		assertEquals(4.0F, b.getComponent2());
		
		assertEquals(2.0F, c.getComponent1());
		assertEquals(4.0F, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2F.transform(Matrix33F.translate(1.0F, 2.0F), null));
		assertThrows(NullPointerException.class, () -> Point2F.transform(null, a));
	}
	
	@Test
	public void testTransformAndDivide() {
		final Point2F a = new Point2F(1.0F, 2.0F);
		final Point2F b = Point2F.transformAndDivide(Matrix33F.scale(1.0F, 2.0F), a);
		final Point2F c = Point2F.transformAndDivide(Matrix33F.translate(1.0F, 2.0F), a);
		
		assertEquals(1.0F, b.getComponent1());
		assertEquals(4.0F, b.getComponent2());
		
		assertEquals(2.0F, c.getComponent1());
		assertEquals(4.0F, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Point2F.transformAndDivide(Matrix33F.translate(1.0F, 2.0F), null));
		assertThrows(NullPointerException.class, () -> Point2F.transformAndDivide(null, a));
	}
	
	@Test
	public void testWrite() {
		final Point2F a = new Point2F(1.0F, 0.5F);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Point2F b = Point2F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}