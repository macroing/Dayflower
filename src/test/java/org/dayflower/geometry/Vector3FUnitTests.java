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
public final class Vector3FUnitTests {
	public Vector3FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAbsolute() {
		final Vector3F a = Vector3F.absolute(new Vector3F(+1.0F, +2.0F, +3.0F));
		final Vector3F b = Vector3F.absolute(new Vector3F(-1.0F, -2.0F, -3.0F));
		
		assertEquals(1.0F, a.getComponent1());
		assertEquals(2.0F, a.getComponent2());
		assertEquals(3.0F, a.getComponent3());
		
		assertEquals(1.0F, b.getComponent1());
		assertEquals(2.0F, b.getComponent2());
		assertEquals(3.0F, b.getComponent3());
		
		assertThrows(NullPointerException.class, () -> Vector3F.absolute(null));
	}
	
	@Test
	public void testClearCacheAndGetCacheSizeAndGetCached() {
		assertEquals(0, Vector3F.getCacheSize());
		
		final Vector3F a = new Vector3F(1.0F, 2.0F, 3.0F);
		final Vector3F b = new Vector3F(1.0F, 2.0F, 3.0F);
		final Vector3F c = Vector3F.getCached(a);
		final Vector3F d = Vector3F.getCached(b);
		
		assertThrows(NullPointerException.class, () -> Vector3F.getCached(null));
		
		assertEquals(1, Vector3F.getCacheSize());
		
		Vector3F.clearCache();
		
		assertEquals(0, Vector3F.getCacheSize());
		
		assertTrue(a != b);
		assertTrue(a == c);
		assertTrue(a == d);
		
		assertTrue(b != a);
		assertTrue(b != c);
		assertTrue(b != d);
	}
	
	@Test
	public void testConstants() {
		assertEquals(new Vector3F(Float.NaN, Float.NaN, Float.NaN), Vector3F.NaN);
		assertEquals(new Vector3F(0.0F, 0.0F, 0.0F), Vector3F.ZERO);
	}
	
	@Test
	public void testConstructor() {
		final Vector3F vector = new Vector3F();
		
		assertEquals(0.0F, vector.getComponent1());
		assertEquals(0.0F, vector.getComponent2());
		assertEquals(0.0F, vector.getComponent3());
	}
	
	@Test
	public void testConstructorFloat() {
		final Vector3F vector = new Vector3F(1.0F);
		
		assertEquals(1.0F, vector.getComponent1());
		assertEquals(1.0F, vector.getComponent2());
		assertEquals(1.0F, vector.getComponent3());
	}
	
	@Test
	public void testConstructorFloatFloatFloat() {
		final Vector3F vector = new Vector3F(1.0F, 2.0F, 3.0F);
		
		assertEquals(1.0F, vector.getComponent1());
		assertEquals(2.0F, vector.getComponent2());
		assertEquals(3.0F, vector.getComponent3());
	}
	
	@Test
	public void testConstructorPoint3F() {
		final Vector3F vector = new Vector3F(new Point3F(1.0F, 2.0F, 3.0F));
		
		assertEquals(1.0F, vector.getComponent1());
		assertEquals(2.0F, vector.getComponent2());
		assertEquals(3.0F, vector.getComponent3());
		
		assertThrows(NullPointerException.class, () -> new Vector3F(null));
	}
	
	@Test
	public void testCosPhi() {
		final Vector3F a = new Vector3F(1.0F, 0.0F, 0.0000000000000000F);
		final Vector3F b = new Vector3F(1.0F, 0.0F, 0.7071067811865475F);
		final Vector3F c = new Vector3F(1.0F, 0.0F, 1.0000000000000000F);
		
		assertEquals(1.0F, a.cosPhi());
		assertEquals(1.0F, b.cosPhi());
		assertEquals(1.0F, c.cosPhi());
	}
	
	@Test
	public void testCosPhiSquared() {
		final Vector3F a = new Vector3F(1.0F, 0.0F, 0.0000000000000000F);
		final Vector3F b = new Vector3F(1.0F, 0.0F, 0.7071067811865475F);
		final Vector3F c = new Vector3F(1.0F, 0.0F, 1.0000000000000000F);
		
		assertEquals(1.0F, a.cosPhiSquared());
		assertEquals(1.0F, b.cosPhiSquared());
		assertEquals(1.0F, c.cosPhiSquared());
	}
	
	@Test
	public void testCosTheta() {
		final Vector3F vector = new Vector3F(0.0F, 0.0F, 1.0F);
		
		assertEquals(1.0F, vector.cosTheta());
	}
	
	@Test
	public void testCosThetaAbs() {
		final Vector3F a = new Vector3F(0.0F, 0.0F, +1.0F);
		final Vector3F b = new Vector3F(0.0F, 0.0F, -1.0F);
		
		assertEquals(1.0F, a.cosThetaAbs());
		assertEquals(1.0F, b.cosThetaAbs());
	}
	
	@Test
	public void testCosThetaQuartic() {
		final Vector3F vector = new Vector3F(0.0F, 0.0F, 2.0F);
		
		assertEquals(16.0F, vector.cosThetaQuartic());
	}
	
	@Test
	public void testCosThetaSquared() {
		final Vector3F vector = new Vector3F(0.0F, 0.0F, 2.0F);
		
		assertEquals(4.0F, vector.cosThetaSquared());
	}
	
	@Test
	public void testEquals() {
		final Vector3F a = new Vector3F(0.0F, 1.0F, 2.0F);
		final Vector3F b = new Vector3F(0.0F, 1.0F, 2.0F);
		final Vector3F c = new Vector3F(0.0F, 1.0F, 3.0F);
		final Vector3F d = new Vector3F(0.0F, 3.0F, 2.0F);
		final Vector3F e = new Vector3F(3.0F, 1.0F, 2.0F);
		final Vector3F f = null;
		
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
		final Vector3F vector = new Vector3F(2.0F, 0.0F, 0.0F);
		
		assertEquals(2.0F, vector.getComponent1());
	}
	
	@Test
	public void testGetComponent2() {
		final Vector3F vector = new Vector3F(0.0F, 2.0F, 0.0F);
		
		assertEquals(2.0F, vector.getComponent2());
	}
	
	@Test
	public void testGetComponent3() {
		final Vector3F vector = new Vector3F(0.0F, 0.0F, 2.0F);
		
		assertEquals(2.0F, vector.getComponent3());
	}
	
	@Test
	public void testGetU() {
		final Vector3F vector = new Vector3F(2.0F, 0.0F, 0.0F);
		
		assertEquals(2.0F, vector.getU());
	}
	
	@Test
	public void testGetV() {
		final Vector3F vector = new Vector3F(0.0F, 2.0F, 0.0F);
		
		assertEquals(2.0F, vector.getV());
	}
	
	@Test
	public void testGetW() {
		final Vector3F vector = new Vector3F(0.0F, 0.0F, 2.0F);
		
		assertEquals(2.0F, vector.getW());
	}
	
	@Test
	public void testGetX() {
		final Vector3F vector = new Vector3F(2.0F, 0.0F, 0.0F);
		
		assertEquals(2.0F, vector.getX());
	}
	
	@Test
	public void testGetY() {
		final Vector3F vector = new Vector3F(0.0F, 2.0F, 0.0F);
		
		assertEquals(2.0F, vector.getY());
	}
	
	@Test
	public void testGetZ() {
		final Vector3F vector = new Vector3F(0.0F, 0.0F, 2.0F);
		
		assertEquals(2.0F, vector.getZ());
	}
	
	@Test
	public void testHashCode() {
		final Vector3F a = new Vector3F(1.0F, 2.0F, 3.0F);
		final Vector3F b = new Vector3F(1.0F, 2.0F, 3.0F);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIsUnitVector() {
		assertTrue(new Vector3F(+1.0F, +0.0F, +0.0F).isUnitVector());
		assertTrue(new Vector3F(+0.0F, +1.0F, +0.0F).isUnitVector());
		assertTrue(new Vector3F(+0.0F, +0.0F, +1.0F).isUnitVector());
		assertTrue(new Vector3F(-1.0F, -0.0F, -0.0F).isUnitVector());
		assertTrue(new Vector3F(-0.0F, -1.0F, -0.0F).isUnitVector());
		assertTrue(new Vector3F(-0.0F, -0.0F, -1.0F).isUnitVector());
		assertTrue(Vector3F.normalize(new Vector3F(1.0F, 1.0F, 1.0F)).isUnitVector());
		
		assertFalse(new Vector3F(+1.0F, +1.0F, +1.0F).isUnitVector());
		assertFalse(new Vector3F(+0.5F, +0.5F, +0.5F).isUnitVector());
		assertFalse(new Vector3F(-1.0F, -1.0F, -1.0F).isUnitVector());
		assertFalse(new Vector3F(-0.5F, -0.5F, -0.5F).isUnitVector());
	}
	
	@Test
	public void testLength() {
		final Vector3F a = new Vector3F(4.0F, 0.0F, 0.0F);
		final Vector3F b = new Vector3F(0.0F, 4.0F, 0.0F);
		final Vector3F c = new Vector3F(0.0F, 0.0F, 4.0F);
		
		assertEquals(4.0F, a.length());
		assertEquals(4.0F, b.length());
		assertEquals(4.0F, c.length());
	}
	
	@Test
	public void testLengthSquared() {
		final Vector3F vector = new Vector3F(2.0F, 4.0F, 8.0F);
		
		assertEquals(84.0F, vector.lengthSquared());
	}
	
	@Test
	public void testRead() throws IOException {
		final Vector3F a = new Vector3F(1.0F, 2.0F, 3.0F);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeFloat(a.getComponent1());
		dataOutput.writeFloat(a.getComponent2());
		dataOutput.writeFloat(a.getComponent3());
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Vector3F b = Vector3F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Vector3F.read(null));
		assertThrows(UncheckedIOException.class, () -> Vector3F.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testSinPhi() {
		final Vector3F a = new Vector3F(0.0F, 1.0F, 0.0000000000000000F);
		final Vector3F b = new Vector3F(0.0F, 1.0F, 0.7071067811865475F);
		final Vector3F c = new Vector3F(0.0F, 1.0F, 1.0000000000000000F);
		
		assertEquals(1.0F, a.sinPhi());
		assertEquals(1.0F, b.sinPhi());
		assertEquals(0.0F, c.sinPhi());
	}
	
	@Test
	public void testSinPhiSquared() {
		final Vector3F a = new Vector3F(0.0F, 1.0F, 0.0000000000000000F);
		final Vector3F b = new Vector3F(0.0F, 1.0F, 0.7071067811865475F);
		final Vector3F c = new Vector3F(0.0F, 1.0F, 1.0000000000000000F);
		
		assertEquals(1.0F, a.sinPhiSquared());
		assertEquals(1.0F, b.sinPhiSquared());
		assertEquals(0.0F, c.sinPhiSquared());
	}
	
	@Test
	public void testSinTheta() {
		final Vector3F a = new Vector3F(0.0F, 0.0F, 0.0000000000000000F);
		final Vector3F b = new Vector3F(0.0F, 0.0F, 0.7071067811865475F);
		final Vector3F c = new Vector3F(0.0F, 0.0F, 1.0000000000000000F);
		
		assertEquals(1.0000000000000000F, a.sinTheta());
		assertEquals(0.7071067811865476F, b.sinTheta());
		assertEquals(0.0000000000000000F, c.sinTheta());
	}
	
	@Test
	public void testSinThetaSquared() {
		final Vector3F a = new Vector3F(0.0F, 0.0F, 0.0000000000000000F);
		final Vector3F b = new Vector3F(0.0F, 0.0F, 0.7071067811865475F);
		final Vector3F c = new Vector3F(0.0F, 0.0F, 1.0000000000000000F);
		
		assertEquals(1.0000000000000000F, a.sinThetaSquared());
		assertEquals(0.5000000000000001F, b.sinThetaSquared());
		assertEquals(0.0000000000000000F, c.sinThetaSquared());
	}
	
	@Test
	public void testTanTheta() {
		final Vector3F a = new Vector3F(0.0F, 0.0F, 0.0000000000000000F);
		final Vector3F b = new Vector3F(0.0F, 0.0F, 0.7071067811865475F);
		final Vector3F c = new Vector3F(0.0F, 0.0F, 1.0000000000000000F);
		
		assertEquals(Float.POSITIVE_INFINITY, a.tanTheta());
		assertEquals(1.00000000000000020000F, b.tanTheta());
		assertEquals(0.00000000000000000000F, c.tanTheta());
	}
	
	@Test
	public void testTanThetaAbs() {
		final Vector3F a = new Vector3F(0.0F, 0.0F, 0.0000000000000000F);
		final Vector3F b = new Vector3F(0.0F, 0.0F, 0.7071067811865475F);
		final Vector3F c = new Vector3F(0.0F, 0.0F, 1.0000000000000000F);
		
		assertEquals(Float.POSITIVE_INFINITY, a.tanThetaAbs());
		assertEquals(1.00000000000000020000F, b.tanThetaAbs());
		assertEquals(0.00000000000000000000F, c.tanThetaAbs());
	}
	
	@Test
	public void testTanThetaSquared() {
		final Vector3F a = new Vector3F(0.0F, 0.0F, 0.0000000000000000F);
		final Vector3F b = new Vector3F(0.0F, 0.0F, 0.7071067811865475F);
		final Vector3F c = new Vector3F(0.0F, 0.0F, 1.0000000000000000F);
		
		assertEquals(Float.POSITIVE_INFINITY, a.tanThetaSquared());
		assertEquals(1.00000010000000000000F, b.tanThetaSquared());
		assertEquals(0.00000000000000000000F, c.tanThetaSquared());
	}
	
	@Test
	public void testToArray() {
		final Vector3F vector = new Vector3F(1.0F, 2.0F, 3.0F);
		
		final float[] array = vector.toArray();
		
		assertNotNull(array);
		
		assertEquals(3, array.length);
		
		assertEquals(1.0F, array[0]);
		assertEquals(2.0F, array[1]);
		assertEquals(3.0F, array[2]);
	}
	
	@Test
	public void testToString() {
		final Vector3F vector = new Vector3F(1.0F, 2.0F, 3.0F);
		
		assertEquals("new Vector3F(1.0F, 2.0F, 3.0F)", vector.toString());
	}
	
	@Test
	public void testWrite() {
		final Vector3F a = new Vector3F(1.0F, 2.0F, 3.0F);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Vector3F b = Vector3F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}