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

import org.dayflower.mock.DataOutputMock;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class Vector3DUnitTests {
	public Vector3DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAbsolute() {
		final Vector3D a = Vector3D.absolute(new Vector3D(+1.0D, +2.0D, +3.0D));
		final Vector3D b = Vector3D.absolute(new Vector3D(-1.0D, -2.0D, -3.0D));
		
		assertEquals(1.0D, a.getComponent1());
		assertEquals(2.0D, a.getComponent2());
		assertEquals(3.0D, a.getComponent3());
		
		assertEquals(1.0D, b.getComponent1());
		assertEquals(2.0D, b.getComponent2());
		assertEquals(3.0D, b.getComponent3());
		
		assertThrows(NullPointerException.class, () -> Vector3D.absolute(null));
	}
	
	@Test
	public void testClearCacheAndGetCacheSizeAndGetCached() {
		assertEquals(0, Vector3D.getCacheSize());
		
		final Vector3D a = new Vector3D(1.0D, 2.0D, 3.0D);
		final Vector3D b = new Vector3D(1.0D, 2.0D, 3.0D);
		final Vector3D c = Vector3D.getCached(a);
		final Vector3D d = Vector3D.getCached(b);
		
		assertThrows(NullPointerException.class, () -> Vector3D.getCached(null));
		
		assertEquals(1, Vector3D.getCacheSize());
		
		Vector3D.clearCache();
		
		assertEquals(0, Vector3D.getCacheSize());
		
		assertTrue(a != b);
		assertTrue(a == c);
		assertTrue(a == d);
		
		assertTrue(b != a);
		assertTrue(b != c);
		assertTrue(b != d);
	}
	
	@Test
	public void testConstants() {
		assertEquals(new Vector3D(Double.NaN, Double.NaN, Double.NaN), Vector3D.NaN);
		assertEquals(new Vector3D(0.0D, 0.0D, 0.0D), Vector3D.ZERO);
	}
	
	@Test
	public void testConstructor() {
		final Vector3D vector = new Vector3D();
		
		assertEquals(0.0D, vector.getComponent1());
		assertEquals(0.0D, vector.getComponent2());
		assertEquals(0.0D, vector.getComponent3());
	}
	
	@Test
	public void testConstructorDouble() {
		final Vector3D vector = new Vector3D(1.0D);
		
		assertEquals(1.0D, vector.getComponent1());
		assertEquals(1.0D, vector.getComponent2());
		assertEquals(1.0D, vector.getComponent3());
	}
	
	@Test
	public void testConstructorDoubleDoubleDouble() {
		final Vector3D vector = new Vector3D(1.0D, 2.0D, 3.0D);
		
		assertEquals(1.0D, vector.getComponent1());
		assertEquals(2.0D, vector.getComponent2());
		assertEquals(3.0D, vector.getComponent3());
	}
	
	@Test
	public void testConstructorPoint3D() {
		final Vector3D vector = new Vector3D(new Point3D(1.0D, 2.0D, 3.0D));
		
		assertEquals(1.0D, vector.getComponent1());
		assertEquals(2.0D, vector.getComponent2());
		assertEquals(3.0D, vector.getComponent3());
		
		assertThrows(NullPointerException.class, () -> new Vector3D(null));
	}
	
	@Test
	public void testCosPhi() {
		final Vector3D a = new Vector3D(1.0D, 0.0D, 0.0000000000000000D);
		final Vector3D b = new Vector3D(1.0D, 0.0D, 0.7071067811865475D);
		final Vector3D c = new Vector3D(1.0D, 0.0D, 1.0000000000000000D);
		
		assertEquals(1.0D, a.cosPhi());
		assertEquals(1.0D, b.cosPhi());
		assertEquals(1.0D, c.cosPhi());
	}
	
	@Test
	public void testCosPhiSquared() {
		final Vector3D a = new Vector3D(1.0D, 0.0D, 0.0000000000000000D);
		final Vector3D b = new Vector3D(1.0D, 0.0D, 0.7071067811865475D);
		final Vector3D c = new Vector3D(1.0D, 0.0D, 1.0000000000000000D);
		
		assertEquals(1.0D, a.cosPhiSquared());
		assertEquals(1.0D, b.cosPhiSquared());
		assertEquals(1.0D, c.cosPhiSquared());
	}
	
	@Test
	public void testCosTheta() {
		final Vector3D vector = new Vector3D(0.0D, 0.0D, 1.0D);
		
		assertEquals(1.0D, vector.cosTheta());
	}
	
	@Test
	public void testCosThetaAbs() {
		final Vector3D a = new Vector3D(0.0D, 0.0D, +1.0D);
		final Vector3D b = new Vector3D(0.0D, 0.0D, -1.0D);
		
		assertEquals(1.0D, a.cosThetaAbs());
		assertEquals(1.0D, b.cosThetaAbs());
	}
	
	@Test
	public void testCosThetaQuartic() {
		final Vector3D vector = new Vector3D(0.0D, 0.0D, 2.0D);
		
		assertEquals(16.0D, vector.cosThetaQuartic());
	}
	
	@Test
	public void testCosThetaSquared() {
		final Vector3D vector = new Vector3D(0.0D, 0.0D, 2.0D);
		
		assertEquals(4.0D, vector.cosThetaSquared());
	}
	
	@Test
	public void testEquals() {
		final Vector3D a = new Vector3D(0.0D, 1.0D, 2.0D);
		final Vector3D b = new Vector3D(0.0D, 1.0D, 2.0D);
		final Vector3D c = new Vector3D(0.0D, 1.0D, 3.0D);
		final Vector3D d = new Vector3D(0.0D, 3.0D, 2.0D);
		final Vector3D e = new Vector3D(3.0D, 1.0D, 2.0D);
		final Vector3D f = null;
		
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
	public void testGetComponent1() {
		final Vector3D vector = new Vector3D(2.0D, 0.0D, 0.0D);
		
		assertEquals(2.0D, vector.getComponent1());
	}
	
	@Test
	public void testGetComponent2() {
		final Vector3D vector = new Vector3D(0.0D, 2.0D, 0.0D);
		
		assertEquals(2.0D, vector.getComponent2());
	}
	
	@Test
	public void testGetComponent3() {
		final Vector3D vector = new Vector3D(0.0D, 0.0D, 2.0D);
		
		assertEquals(2.0D, vector.getComponent3());
	}
	
	@Test
	public void testGetU() {
		final Vector3D vector = new Vector3D(2.0D, 0.0D, 0.0D);
		
		assertEquals(2.0D, vector.getU());
	}
	
	@Test
	public void testGetV() {
		final Vector3D vector = new Vector3D(0.0D, 2.0D, 0.0D);
		
		assertEquals(2.0D, vector.getV());
	}
	
	@Test
	public void testGetW() {
		final Vector3D vector = new Vector3D(0.0D, 0.0D, 2.0D);
		
		assertEquals(2.0D, vector.getW());
	}
	
	@Test
	public void testGetX() {
		final Vector3D vector = new Vector3D(2.0D, 0.0D, 0.0D);
		
		assertEquals(2.0D, vector.getX());
	}
	
	@Test
	public void testGetY() {
		final Vector3D vector = new Vector3D(0.0D, 2.0D, 0.0D);
		
		assertEquals(2.0D, vector.getY());
	}
	
	@Test
	public void testGetZ() {
		final Vector3D vector = new Vector3D(0.0D, 0.0D, 2.0D);
		
		assertEquals(2.0D, vector.getZ());
	}
	
	@Test
	public void testHashCode() {
		final Vector3D a = new Vector3D(1.0D, 2.0D, 3.0D);
		final Vector3D b = new Vector3D(1.0D, 2.0D, 3.0D);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIsUnitVector() {
		assertTrue(new Vector3D(+1.0D, +0.0D, +0.0D).isUnitVector());
		assertTrue(new Vector3D(+0.0D, +1.0D, +0.0D).isUnitVector());
		assertTrue(new Vector3D(+0.0D, +0.0D, +1.0D).isUnitVector());
		assertTrue(new Vector3D(-1.0D, -0.0D, -0.0D).isUnitVector());
		assertTrue(new Vector3D(-0.0D, -1.0D, -0.0D).isUnitVector());
		assertTrue(new Vector3D(-0.0D, -0.0D, -1.0D).isUnitVector());
		assertTrue(Vector3D.normalize(new Vector3D(1.0D, 1.0D, 1.0D)).isUnitVector());
		
		assertFalse(new Vector3D(+1.0D, +1.0D, +1.0D).isUnitVector());
		assertFalse(new Vector3D(+0.5D, +0.5D, +0.5D).isUnitVector());
		assertFalse(new Vector3D(-1.0D, -1.0D, -1.0D).isUnitVector());
		assertFalse(new Vector3D(-0.5D, -0.5D, -0.5D).isUnitVector());
	}
	
	@Test
	public void testLength() {
		final Vector3D a = new Vector3D(4.0D, 0.0D, 0.0D);
		final Vector3D b = new Vector3D(0.0D, 4.0D, 0.0D);
		final Vector3D c = new Vector3D(0.0D, 0.0D, 4.0D);
		
		assertEquals(4.0D, a.length());
		assertEquals(4.0D, b.length());
		assertEquals(4.0D, c.length());
	}
	
	@Test
	public void testLengthSquared() {
		final Vector3D vector = new Vector3D(2.0D, 4.0D, 8.0D);
		
		assertEquals(84.0D, vector.lengthSquared());
	}
	
	@Test
	public void testRead() throws IOException {
		final Vector3D a = new Vector3D(1.0D, 2.0D, 3.0D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeDouble(a.getComponent1());
		dataOutput.writeDouble(a.getComponent2());
		dataOutput.writeDouble(a.getComponent3());
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Vector3D b = Vector3D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Vector3D.read(null));
		assertThrows(UncheckedIOException.class, () -> Vector3D.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testSinPhi() {
		final Vector3D a = new Vector3D(0.0D, 1.0D, 0.0000000000000000D);
		final Vector3D b = new Vector3D(0.0D, 1.0D, 0.7071067811865475D);
		final Vector3D c = new Vector3D(0.0D, 1.0D, 1.0000000000000000D);
		
		assertEquals(1.0D, a.sinPhi());
		assertEquals(1.0D, b.sinPhi());
		assertEquals(0.0D, c.sinPhi());
	}
	
	@Test
	public void testSinPhiSquared() {
		final Vector3D a = new Vector3D(0.0D, 1.0D, 0.0000000000000000D);
		final Vector3D b = new Vector3D(0.0D, 1.0D, 0.7071067811865475D);
		final Vector3D c = new Vector3D(0.0D, 1.0D, 1.0000000000000000D);
		
		assertEquals(1.0D, a.sinPhiSquared());
		assertEquals(1.0D, b.sinPhiSquared());
		assertEquals(0.0D, c.sinPhiSquared());
	}
	
	@Test
	public void testSinTheta() {
		final Vector3D a = new Vector3D(0.0D, 0.0D, 0.0000000000000000D);
		final Vector3D b = new Vector3D(0.0D, 0.0D, 0.7071067811865475D);
		final Vector3D c = new Vector3D(0.0D, 0.0D, 1.0000000000000000D);
		
		assertEquals(1.0000000000000000D, a.sinTheta());
		assertEquals(0.7071067811865476D, b.sinTheta());
		assertEquals(0.0000000000000000D, c.sinTheta());
	}
	
	@Test
	public void testSinThetaSquared() {
		final Vector3D a = new Vector3D(0.0D, 0.0D, 0.0000000000000000D);
		final Vector3D b = new Vector3D(0.0D, 0.0D, 0.7071067811865475D);
		final Vector3D c = new Vector3D(0.0D, 0.0D, 1.0000000000000000D);
		
		assertEquals(1.0000000000000000D, a.sinThetaSquared());
		assertEquals(0.5000000000000001D, b.sinThetaSquared());
		assertEquals(0.0000000000000000D, c.sinThetaSquared());
	}
	
	@Test
	public void testTanTheta() {
		final Vector3D a = new Vector3D(0.0D, 0.0D, 0.0000000000000000D);
		final Vector3D b = new Vector3D(0.0D, 0.0D, 0.7071067811865475D);
		final Vector3D c = new Vector3D(0.0D, 0.0D, 1.0000000000000000D);
		
		assertEquals(Double.POSITIVE_INFINITY, a.tanTheta());
		assertEquals(1.000000000000000200000D, b.tanTheta());
		assertEquals(0.000000000000000000000D, c.tanTheta());
	}
	
	@Test
	public void testTanThetaAbs() {
		final Vector3D a = new Vector3D(0.0D, 0.0D, 0.0000000000000000D);
		final Vector3D b = new Vector3D(0.0D, 0.0D, 0.7071067811865475D);
		final Vector3D c = new Vector3D(0.0D, 0.0D, 1.0000000000000000D);
		
		assertEquals(Double.POSITIVE_INFINITY, a.tanThetaAbs());
		assertEquals(1.000000000000000200000D, b.tanThetaAbs());
		assertEquals(0.000000000000000000000D, c.tanThetaAbs());
	}
	
	@Test
	public void testTanThetaSquared() {
		final Vector3D a = new Vector3D(0.0D, 0.0D, 0.0000000000000000D);
		final Vector3D b = new Vector3D(0.0D, 0.0D, 0.7071067811865475D);
		final Vector3D c = new Vector3D(0.0D, 0.0D, 1.0000000000000000D);
		
		assertEquals(Double.POSITIVE_INFINITY, a.tanThetaSquared());
		assertEquals(1.000000000000000400000D, b.tanThetaSquared());
		assertEquals(0.000000000000000000000D, c.tanThetaSquared());
	}
	
	@Test
	public void testToArray() {
		final Vector3D vector = new Vector3D(1.0D, 2.0D, 3.0D);
		
		final double[] array = vector.toArray();
		
		assertNotNull(array);
		
		assertEquals(3, array.length);
		
		assertEquals(1.0D, array[0]);
		assertEquals(2.0D, array[1]);
		assertEquals(3.0D, array[2]);
	}
	
	@Test
	public void testToString() {
		final Vector3D vector = new Vector3D(1.0D, 2.0D, 3.0D);
		
		assertEquals("new Vector3D(1.0D, 2.0D, 3.0D)", vector.toString());
	}
	
	@Test
	public void testWrite() {
		final Vector3D a = new Vector3D(1.0D, 2.0D, 3.0D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Vector3D b = Vector3D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}