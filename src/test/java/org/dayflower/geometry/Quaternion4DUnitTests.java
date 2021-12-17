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
public final class Quaternion4DUnitTests {
	public Quaternion4DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAdd() {
		final Quaternion4D a = new Quaternion4D(1.0D, 2.0D, 3.0D, 4.0D);
		final Quaternion4D b = new Quaternion4D(2.0D, 3.0D, 4.0D, 5.0D);
		final Quaternion4D c = Quaternion4D.add(a, b);
		
		assertEquals(3.0D, c.getComponent1());
		assertEquals(5.0D, c.getComponent2());
		assertEquals(7.0D, c.getComponent3());
		assertEquals(9.0D, c.getComponent4());
		
		assertThrows(NullPointerException.class, () -> Quaternion4D.add(a, null));
		assertThrows(NullPointerException.class, () -> Quaternion4D.add(null, b));
	}
	
	@Test
	public void testConjugate() {
		final Quaternion4D a = new Quaternion4D(1.0D, 2.0D, 3.0D, 4.0D);
		final Quaternion4D b = Quaternion4D.conjugate(a);
		final Quaternion4D c = Quaternion4D.conjugate(b);
		
		assertEquals(-1.0D, b.getComponent1());
		assertEquals(-2.0D, b.getComponent2());
		assertEquals(-3.0D, b.getComponent3());
		assertEquals(+4.0D, b.getComponent4());
		
		assertEquals(+1.0D, c.getComponent1());
		assertEquals(+2.0D, c.getComponent2());
		assertEquals(+3.0D, c.getComponent3());
		assertEquals(+4.0D, c.getComponent4());
		
		assertThrows(NullPointerException.class, () -> Quaternion4D.conjugate(null));
	}
	
	@Test
	public void testConstructor() {
		final Quaternion4D quaternion = new Quaternion4D();
		
		assertEquals(0.0D, quaternion.getComponent1());
		assertEquals(0.0D, quaternion.getComponent2());
		assertEquals(0.0D, quaternion.getComponent3());
		assertEquals(1.0D, quaternion.getComponent4());
	}
	
	@Test
	public void testConstructorDoubleDoubleDouble() {
		final Quaternion4D quaternion = new Quaternion4D(2.0D, 3.0D, 4.0D);
		
		assertEquals(2.0D, quaternion.getComponent1());
		assertEquals(3.0D, quaternion.getComponent2());
		assertEquals(4.0D, quaternion.getComponent3());
		assertEquals(1.0D, quaternion.getComponent4());
	}
	
	@Test
	public void testConstructorDoubleDoubleDoubleDouble() {
		final Quaternion4D quaternion = new Quaternion4D(1.0D, 2.0D, 3.0D, 4.0D);
		
		assertEquals(1.0D, quaternion.getComponent1());
		assertEquals(2.0D, quaternion.getComponent2());
		assertEquals(3.0D, quaternion.getComponent3());
		assertEquals(4.0D, quaternion.getComponent4());
	}
	
	@Test
	public void testConstructorVector3D() {
		final Quaternion4D quaternion = new Quaternion4D(new Vector3D(2.0D, 3.0D, 4.0D));
		
		assertEquals(2.0D, quaternion.getComponent1());
		assertEquals(3.0D, quaternion.getComponent2());
		assertEquals(4.0D, quaternion.getComponent3());
		assertEquals(1.0D, quaternion.getComponent4());
		
		assertThrows(NullPointerException.class, () -> new Quaternion4D(null));
	}
	
	@Test
	public void testDivide() {
		final Quaternion4D a = new Quaternion4D(2.0D, 4.0D, 8.0D, 16.0D);
		final Quaternion4D b = Quaternion4D.divide(a, 2.0D);
		final Quaternion4D c = Quaternion4D.divide(a, Double.NaN);
		
		assertEquals(1.0D, b.getComponent1());
		assertEquals(2.0D, b.getComponent2());
		assertEquals(4.0D, b.getComponent3());
		assertEquals(8.0D, b.getComponent4());
		
		assertEquals(Double.NaN, c.getComponent1());
		assertEquals(Double.NaN, c.getComponent2());
		assertEquals(Double.NaN, c.getComponent3());
		assertEquals(Double.NaN, c.getComponent4());
		
		assertThrows(NullPointerException.class, () -> Quaternion4D.divide(null, 2.0D));
	}
	
	@Test
	public void testDotProduct() {
		final Quaternion4D a = new Quaternion4D(+1.0D, +0.0D, +0.0D, +0.0D);
		final Quaternion4D b = new Quaternion4D(+1.0D, +0.0D, +0.0D, +0.0D);
		final Quaternion4D c = new Quaternion4D(+0.0D, -1.0D, +0.0D, +0.0D);
		final Quaternion4D d = new Quaternion4D(-1.0D, +0.0D, +0.0D, +0.0D);
		
		assertEquals(+1.0D, Quaternion4D.dotProduct(a, b));
		assertEquals(+0.0D, Quaternion4D.dotProduct(a, c));
		assertEquals(-1.0D, Quaternion4D.dotProduct(a, d));
		
		assertThrows(NullPointerException.class, () -> Quaternion4D.dotProduct(a, null));
		assertThrows(NullPointerException.class, () -> Quaternion4D.dotProduct(null, b));
	}
	
	@Test
	public void testEquals() {
		final Quaternion4D a = new Quaternion4D(0.0D, 1.0D, 2.0D, 3.0D);
		final Quaternion4D b = new Quaternion4D(0.0D, 1.0D, 2.0D, 3.0D);
		final Quaternion4D c = new Quaternion4D(0.0D, 1.0D, 2.0D, 4.0D);
		final Quaternion4D d = new Quaternion4D(0.0D, 1.0D, 4.0D, 3.0D);
		final Quaternion4D e = new Quaternion4D(0.0D, 4.0D, 2.0D, 3.0D);
		final Quaternion4D f = new Quaternion4D(4.0D, 1.0D, 2.0D, 3.0D);
		final Quaternion4D g = null;
		
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
	public void testGetComponent1() {
		final Quaternion4D quaternion = new Quaternion4D(1.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, quaternion.getComponent1());
	}
	
	@Test
	public void testGetComponent2() {
		final Quaternion4D quaternion = new Quaternion4D(0.0D, 1.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, quaternion.getComponent2());
	}
	
	@Test
	public void testGetComponent3() {
		final Quaternion4D quaternion = new Quaternion4D(0.0D, 0.0D, 1.0D, 0.0D);
		
		assertEquals(1.0D, quaternion.getComponent3());
	}
	
	@Test
	public void testGetComponent4() {
		final Quaternion4D quaternion = new Quaternion4D(0.0D, 0.0D, 0.0D, 1.0D);
		
		assertEquals(1.0D, quaternion.getComponent4());
	}
	
	@Test
	public void testGetW() {
		final Quaternion4D quaternion = new Quaternion4D(0.0D, 0.0D, 0.0D, 1.0D);
		
		assertEquals(1.0D, quaternion.getW());
	}
	
	@Test
	public void testGetX() {
		final Quaternion4D quaternion = new Quaternion4D(1.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, quaternion.getX());
	}
	
	@Test
	public void testGetY() {
		final Quaternion4D quaternion = new Quaternion4D(0.0D, 1.0D, 0.0D, 0.0D);
		
		assertEquals(1.0D, quaternion.getY());
	}
	
	@Test
	public void testGetZ() {
		final Quaternion4D quaternion = new Quaternion4D(0.0D, 0.0D, 1.0D, 0.0D);
		
		assertEquals(1.0D, quaternion.getZ());
	}
	
	@Test
	public void testHashCode() {
		final Quaternion4D a = new Quaternion4D(1.0D, 2.0D, 3.0D, 4.0D);
		final Quaternion4D b = new Quaternion4D(1.0D, 2.0D, 3.0D, 4.0D);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testLength() {
		final Quaternion4D a = new Quaternion4D(4.0D, 0.0D, 0.0D, 0.0D);
		final Quaternion4D b = new Quaternion4D(0.0D, 4.0D, 0.0D, 0.0D);
		final Quaternion4D c = new Quaternion4D(0.0D, 0.0D, 4.0D, 0.0D);
		final Quaternion4D d = new Quaternion4D(0.0D, 0.0D, 0.0D, 4.0D);
		
		assertEquals(4.0D, a.length());
		assertEquals(4.0D, b.length());
		assertEquals(4.0D, c.length());
		assertEquals(4.0D, d.length());
	}
	
	@Test
	public void testLengthSquared() {
		final Quaternion4D quaternion = new Quaternion4D(2.0D, 4.0D, 8.0D, 16.0D);
		
		assertEquals(340.0D, quaternion.lengthSquared());
	}
	
	@Test
	public void testMultiply() {
		final Quaternion4D a = new Quaternion4D(1.0D, 2.0D, 3.0D, 4.0D);
		final Quaternion4D b = Quaternion4D.multiply(a, 2.0D);
		
		assertEquals(2.0D, b.getComponent1());
		assertEquals(4.0D, b.getComponent2());
		assertEquals(6.0D, b.getComponent3());
		assertEquals(8.0D, b.getComponent4());
		
		assertThrows(NullPointerException.class, () -> Quaternion4D.multiply(null, 2.0D));
	}
	
	@Test
	public void testNegate() {
		final Quaternion4D a = new Quaternion4D(1.0D, 2.0D, 3.0D, 4.0D);
		final Quaternion4D b = Quaternion4D.negate(a);
		final Quaternion4D c = Quaternion4D.negate(b);
		
		assertEquals(-1.0D, b.getComponent1());
		assertEquals(-2.0D, b.getComponent2());
		assertEquals(-3.0D, b.getComponent3());
		assertEquals(-4.0D, b.getComponent4());
		
		assertEquals(+1.0D, c.getComponent1());
		assertEquals(+2.0D, c.getComponent2());
		assertEquals(+3.0D, c.getComponent3());
		assertEquals(+4.0D, c.getComponent4());
		
		assertThrows(NullPointerException.class, () -> Quaternion4D.negate(null));
	}
	
	@Test
	public void testRead() throws IOException {
		final Quaternion4D a = new Quaternion4D(1.0D, 2.0D, 3.0D, 4.0D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeDouble(a.getComponent1());
		dataOutput.writeDouble(a.getComponent2());
		dataOutput.writeDouble(a.getComponent3());
		dataOutput.writeDouble(a.getComponent4());
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Quaternion4D b = Quaternion4D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Quaternion4D.read(null));
		assertThrows(UncheckedIOException.class, () -> Quaternion4D.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testSubtract() {
		final Quaternion4D a = new Quaternion4D(3.0D, 5.0D, 7.0D, 9.0D);
		final Quaternion4D b = new Quaternion4D(2.0D, 3.0D, 4.0D, 5.0D);
		final Quaternion4D c = Quaternion4D.subtract(a, b);
		
		assertEquals(1.0D, c.getComponent1());
		assertEquals(2.0D, c.getComponent2());
		assertEquals(3.0D, c.getComponent3());
		assertEquals(4.0D, c.getComponent4());
		
		assertThrows(NullPointerException.class, () -> Quaternion4D.subtract(a, null));
		assertThrows(NullPointerException.class, () -> Quaternion4D.subtract(null, b));
	}
	
	@Test
	public void testToString() {
		final Quaternion4D quaternion = new Quaternion4D(1.0D, 2.0D, 3.0D, 4.0D);
		
		assertEquals("new Quaternion4D(1.0D, 2.0D, 3.0D, 4.0D)", quaternion.toString());
	}
	
	@Test
	public void testWrite() {
		final Quaternion4D a = new Quaternion4D(1.0D, 2.0D, 3.0D, 4.0D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Quaternion4D b = Quaternion4D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}