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
public final class Vector2IUnitTests {
	public Vector2IUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAdd() {
		final Vector2I a = new Vector2I(1, 2);
		final Vector2I b = new Vector2I(2, 3);
		final Vector2I c = Vector2I.add(a, b);
		
		assertEquals(3, c.getComponent1());
		assertEquals(5, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2I.add(a, null));
		assertThrows(NullPointerException.class, () -> Vector2I.add(null, b));
	}
	
	@Test
	public void testConstructor() {
		final Vector2I vector = new Vector2I();
		
		assertEquals(0, vector.getComponent1());
		assertEquals(0, vector.getComponent2());
	}
	
	@Test
	public void testConstructorIntInt() {
		final Vector2I vector = new Vector2I(1, 2);
		
		assertEquals(1, vector.getComponent1());
		assertEquals(2, vector.getComponent2());
	}
	
	@Test
	public void testConstructorPoint2I() {
		final Vector2I vector = new Vector2I(new Point2I(1, 2));
		
		assertEquals(1, vector.getComponent1());
		assertEquals(2, vector.getComponent2());
		
		assertThrows(NullPointerException.class, () -> new Vector2I(null));
	}
	
	@Test
	public void testDirection() {
		final Vector2I vector = Vector2I.direction(new Point2I(1, 2), new Point2I(2, 3));
		
		assertEquals(1, vector.getComponent1());
		assertEquals(1, vector.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2I.direction(new Point2I(1, 2), null));
		assertThrows(NullPointerException.class, () -> Vector2I.direction(null, new Point2I(2, 3)));
	}
	
	@Test
	public void testDivide() {
		final Vector2I a = new Vector2I(2, 4);
		final Vector2I b = Vector2I.divide(a, 2);
		
		assertEquals(1, b.getComponent1());
		assertEquals(2, b.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2I.divide(null, 2));
	}
	
	@Test
	public void testEquals() {
		final Vector2I a = new Vector2I(0, 1);
		final Vector2I b = new Vector2I(0, 1);
		final Vector2I c = new Vector2I(0, 2);
		final Vector2I d = new Vector2I(2, 1);
		final Vector2I e = null;
		
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
		final Vector2I vector = new Vector2I(2, 0);
		
		assertEquals(2, vector.getComponent1());
	}
	
	@Test
	public void testGetComponent2() {
		final Vector2I vector = new Vector2I(0, 2);
		
		assertEquals(2, vector.getComponent2());
	}
	
	@Test
	public void testGetU() {
		final Vector2I vector = new Vector2I(2, 0);
		
		assertEquals(2, vector.getU());
	}
	
	@Test
	public void testGetV() {
		final Vector2I vector = new Vector2I(0, 2);
		
		assertEquals(2, vector.getV());
	}
	
	@Test
	public void testGetX() {
		final Vector2I vector = new Vector2I(2, 0);
		
		assertEquals(2, vector.getX());
	}
	
	@Test
	public void testGetY() {
		final Vector2I vector = new Vector2I(0, 2);
		
		assertEquals(2, vector.getY());
	}
	
	@Test
	public void testHashCode() {
		final Vector2I a = new Vector2I(1, 2);
		final Vector2I b = new Vector2I(1, 2);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testLength() {
		final Vector2I a = new Vector2I(4, 0);
		final Vector2I b = new Vector2I(0, 4);
		
		assertEquals(4, a.length());
		assertEquals(4, b.length());
	}
	
	@Test
	public void testLengthSquared() {
		final Vector2I vector = new Vector2I(2, 4);
		
		assertEquals(20, vector.lengthSquared());
	}
	
	@Test
	public void testMultiply() {
		final Vector2I a = new Vector2I(1, 2);
		final Vector2I b = Vector2I.multiply(a, 2);
		
		assertEquals(2, b.getComponent1());
		assertEquals(4, b.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2I.multiply(null, 2));
	}
	
	@Test
	public void testNegate() {
		final Vector2I a = new Vector2I(1, 2);
		final Vector2I b = Vector2I.negate(a);
		final Vector2I c = Vector2I.negate(b);
		
		assertEquals(-1, b.getComponent1());
		assertEquals(-2, b.getComponent2());
		
		assertEquals(+1, c.getComponent1());
		assertEquals(+2, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2I.negate(null));
	}
	
	@Test
	public void testPerpendicular() {
		final Vector2I a = new Vector2I(1, 2);
		final Vector2I b = Vector2I.perpendicular(a);
		
		assertEquals(+2, b.getComponent1());
		assertEquals(-1, b.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2I.perpendicular(null));
	}
	
	@Test
	public void testRead() throws IOException {
		final Vector2I a = new Vector2I(1, 2);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeInt(a.getComponent1());
		dataOutput.writeInt(a.getComponent2());
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Vector2I b = Vector2I.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Vector2I.read(null));
		assertThrows(UncheckedIOException.class, () -> Vector2I.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testSubtract() {
		final Vector2I a = new Vector2I(3, 5);
		final Vector2I b = new Vector2I(2, 3);
		final Vector2I c = Vector2I.subtract(a, b);
		
		assertEquals(1, c.getComponent1());
		assertEquals(2, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2I.subtract(a, null));
		assertThrows(NullPointerException.class, () -> Vector2I.subtract(null, b));
	}
	
	@Test
	public void testToArray() {
		final Vector2I vector = new Vector2I(1, 2);
		
		final int[] array = vector.toArray();
		
		assertNotNull(array);
		
		assertEquals(2, array.length);
		
		assertEquals(1, array[0]);
		assertEquals(2, array[1]);
	}
	
	@Test
	public void testToString() {
		final Vector2I vector = new Vector2I(1, 2);
		
		assertEquals("new Vector2I(1, 2)", vector.toString());
	}
	
	@Test
	public void testWrite() {
		final Vector2I a = new Vector2I(1, 2);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Vector2I b = Vector2I.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}