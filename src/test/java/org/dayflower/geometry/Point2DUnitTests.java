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