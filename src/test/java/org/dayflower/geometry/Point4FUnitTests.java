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
public final class Point4FUnitTests {
	public Point4FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testClearCacheAndGetCacheSizeAndGetCached() {
		assertEquals(0, Point4F.getCacheSize());
		
		final Point4F a = new Point4F(1.0F, 2.0F, 3.0F, 4.0F);
		final Point4F b = new Point4F(1.0F, 2.0F, 3.0F, 4.0F);
		final Point4F c = Point4F.getCached(a);
		final Point4F d = Point4F.getCached(b);
		
		assertThrows(NullPointerException.class, () -> Point4F.getCached(null));
		
		assertEquals(1, Point4F.getCacheSize());
		
		Point4F.clearCache();
		
		assertEquals(0, Point4F.getCacheSize());
		
		assertTrue(a != b);
		assertTrue(a == c);
		assertTrue(a == d);
		
		assertTrue(b != a);
		assertTrue(b != c);
		assertTrue(b != d);
	}
	
	@Test
	public void testConstructor() {
		final Point4F point = new Point4F();
		
		assertEquals(0.0F, point.x);
		assertEquals(0.0F, point.y);
		assertEquals(0.0F, point.z);
		assertEquals(1.0F, point.w);
	}
	
	@Test
	public void testConstructorFloat() {
		final Point4F point = new Point4F(2.0F);
		
		assertEquals(2.0F, point.x);
		assertEquals(2.0F, point.y);
		assertEquals(2.0F, point.z);
		assertEquals(1.0F, point.w);
	}
	
	@Test
	public void testConstructorFloatFloatFloat() {
		final Point4F point = new Point4F(2.0F, 3.0F, 4.0F);
		
		assertEquals(2.0F, point.x);
		assertEquals(3.0F, point.y);
		assertEquals(4.0F, point.z);
		assertEquals(1.0F, point.w);
	}
	
	@Test
	public void testConstructorFloatFloatFloatFloat() {
		final Point4F point = new Point4F(1.0F, 2.0F, 3.0F, 4.0F);
		
		assertEquals(1.0F, point.x);
		assertEquals(2.0F, point.y);
		assertEquals(3.0F, point.z);
		assertEquals(4.0F, point.w);
	}
	
	@Test
	public void testConstructorPoint3F() {
		final Point4F point = new Point4F(new Point3F(2.0F, 3.0F, 4.0F));
		
		assertEquals(2.0F, point.x);
		assertEquals(3.0F, point.y);
		assertEquals(4.0F, point.z);
		assertEquals(1.0F, point.w);
		
		assertThrows(NullPointerException.class, () -> new Point4F((Point3F)(null)));
	}
	
	@Test
	public void testConstructorVector3F() {
		final Point4F point = new Point4F(new Vector3F(2.0F, 3.0F, 4.0F));
		
		assertEquals(2.0F, point.x);
		assertEquals(3.0F, point.y);
		assertEquals(4.0F, point.z);
		assertEquals(1.0F, point.w);
		
		assertThrows(NullPointerException.class, () -> new Point4F((Vector3F)(null)));
	}
	
	@Test
	public void testConstructorVector4F() {
		final Point4F point = new Point4F(new Vector4F(1.0F, 2.0F, 3.0F, 4.0F));
		
		assertEquals(1.0F, point.x);
		assertEquals(2.0F, point.y);
		assertEquals(3.0F, point.z);
		assertEquals(4.0F, point.w);
		
		assertThrows(NullPointerException.class, () -> new Point4F((Vector4F)(null)));
	}
	
	@Test
	public void testEquals() {
		final Point4F a = new Point4F(0.0F, 1.0F, 2.0F, 3.0F);
		final Point4F b = new Point4F(0.0F, 1.0F, 2.0F, 3.0F);
		final Point4F c = new Point4F(0.0F, 1.0F, 2.0F, 4.0F);
		final Point4F d = new Point4F(0.0F, 1.0F, 4.0F, 3.0F);
		final Point4F e = new Point4F(0.0F, 4.0F, 2.0F, 3.0F);
		final Point4F f = new Point4F(4.0F, 1.0F, 2.0F, 3.0F);
		final Point4F g = null;
		
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
		final Point4F point = new Point4F(1.0F, 2.0F, 3.0F, 4.0F);
		
		assertEquals(1.0F, point.getComponent(0));
		assertEquals(2.0F, point.getComponent(1));
		assertEquals(3.0F, point.getComponent(2));
		assertEquals(4.0F, point.getComponent(3));
		
		assertThrows(IllegalArgumentException.class, () -> point.getComponent(-1));
		assertThrows(IllegalArgumentException.class, () -> point.getComponent(+4));
	}
	
	@Test
	public void testHashCode() {
		final Point4F a = new Point4F(1.0F, 2.0F, 3.0F, 4.0F);
		final Point4F b = new Point4F(1.0F, 2.0F, 3.0F, 4.0F);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testLerp() {
		final Point4F a = new Point4F(1.0F, 1.0F, 1.0F, 1.0F);
		final Point4F b = new Point4F(5.0F, 5.0F, 5.0F, 5.0F);
		final Point4F c = Point4F.lerp(a, b, 0.25F);
		
		assertEquals(2.0F, c.x);
		assertEquals(2.0F, c.y);
		assertEquals(2.0F, c.z);
		assertEquals(2.0F, c.w);
		
		assertThrows(NullPointerException.class, () -> Point4F.lerp(a, null, 0.25F));
		assertThrows(NullPointerException.class, () -> Point4F.lerp(null, b, 0.25F));
	}
	
	@Test
	public void testRead() throws IOException {
		final Point4F a = new Point4F(1.0F, 2.0F, 3.0F, 4.0F);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeFloat(a.x);
		dataOutput.writeFloat(a.y);
		dataOutput.writeFloat(a.z);
		dataOutput.writeFloat(a.w);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Point4F b = Point4F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Point4F.read(null));
		assertThrows(UncheckedIOException.class, () -> Point4F.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testToArray() {
		final Point4F point = new Point4F(1.0F, 2.0F, 3.0F, 4.0F);
		
		final float[] array = point.toArray();
		
		assertNotNull(array);
		
		assertEquals(4, array.length);
		
		assertEquals(1.0F, array[0]);
		assertEquals(2.0F, array[1]);
		assertEquals(3.0F, array[2]);
		assertEquals(4.0F, array[3]);
	}
	
	@Test
	public void testToString() {
		final Point4F point = new Point4F(1.0F, 2.0F, 3.0F, 4.0F);
		
		assertEquals("new Point4F(1.0F, 2.0F, 3.0F, 4.0F)", point.toString());
	}
	
	@Test
	public void testTransform() {
		final Point4F a = new Point4F(1.0F, 2.0F, 3.0F, 1.0F);
		final Point4F b = Point4F.transform(Matrix44F.scale(1.0F, 2.0F, 3.0F), a);
		final Point4F c = Point4F.transform(Matrix44F.translate(1.0F, 2.0F, 3.0F), a);
		
		assertEquals(1.0F, b.x);
		assertEquals(4.0F, b.y);
		assertEquals(9.0F, b.z);
		assertEquals(1.0F, b.w);
		
		assertEquals(2.0F, c.x);
		assertEquals(4.0F, c.y);
		assertEquals(6.0F, c.z);
		assertEquals(1.0F, c.w);
		
		assertThrows(NullPointerException.class, () -> Point4F.transform(Matrix44F.translate(1.0F, 2.0F, 3.0F), null));
		assertThrows(NullPointerException.class, () -> Point4F.transform(null, a));
	}
	
	@Test
	public void testTransformAndDivide() {
		final Point4F a = new Point4F(1.0F, 2.0F, 3.0F, 1.0F);
		final Point4F b = Point4F.transformAndDivide(Matrix44F.scale(1.0F, 2.0F, 3.0F), a);
		final Point4F c = Point4F.transformAndDivide(Matrix44F.translate(1.0F, 2.0F, 3.0F), a);
		final Point4F d = new Point4F(1.0F, 2.0F, 3.0F, 2.0F);
		final Point4F e = Point4F.transformAndDivide(Matrix44F.translate(1.0F, 2.0F, 3.0F), d);
		final Point4F f = new Point4F(1.0F, 2.0F, 3.0F, 0.0F);
		final Point4F g = Point4F.transformAndDivide(Matrix44F.scale(1.0F, 2.0F, 3.0F), f);
		
		assertEquals(1.0F, b.x);
		assertEquals(4.0F, b.y);
		assertEquals(9.0F, b.z);
		assertEquals(1.0F, b.w);
		
		assertEquals(2.0F, c.x);
		assertEquals(4.0F, c.y);
		assertEquals(6.0F, c.z);
		assertEquals(1.0F, c.w);
		
		assertEquals(1.5F, e.x);
		assertEquals(3.0F, e.y);
		assertEquals(4.5F, e.z);
		assertEquals(2.0F, e.w);
		
		assertEquals(1.0F, g.x);
		assertEquals(4.0F, g.y);
		assertEquals(9.0F, g.z);
		assertEquals(0.0F, g.w);
		
		assertThrows(NullPointerException.class, () -> Point4F.transformAndDivide(Matrix44F.translate(1.0F, 2.0F, 3.0F), null));
		assertThrows(NullPointerException.class, () -> Point4F.transformAndDivide(null, a));
	}
	
	@Test
	public void testWrite() {
		final Point4F a = new Point4F(1.0F, 2.0F, 3.0F, 4.0F);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Point4F b = Point4F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}