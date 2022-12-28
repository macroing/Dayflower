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
import java.util.Optional;

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
		
		assertEquals(1.0F, a.x);
		assertEquals(2.0F, a.y);
		assertEquals(3.0F, a.z);
		
		assertEquals(1.0F, b.x);
		assertEquals(2.0F, b.y);
		assertEquals(3.0F, b.z);
		
		assertThrows(NullPointerException.class, () -> Vector3F.absolute(null));
	}
	
	@Test
	public void testAddVector3FVector3F() {
		final Vector3F a = new Vector3F(1.0F, 2.0F, 3.0F);
		final Vector3F b = new Vector3F(2.0F, 3.0F, 4.0F);
		final Vector3F c = Vector3F.add(a, b);
		
		assertEquals(3.0F, c.x);
		assertEquals(5.0F, c.y);
		assertEquals(7.0F, c.z);
		
		assertThrows(NullPointerException.class, () -> Vector3F.add(a, null));
		assertThrows(NullPointerException.class, () -> Vector3F.add(null, b));
	}
	
	@Test
	public void testAddVector3FVector3FVector3F() {
		final Vector3F a = new Vector3F(1.0F, 2.0F, 3.0F);
		final Vector3F b = new Vector3F(2.0F, 3.0F, 4.0F);
		final Vector3F c = new Vector3F(3.0F, 4.0F, 5.0F);
		final Vector3F d = Vector3F.add(a, b, c);
		
		assertEquals( 6.0F, d.x);
		assertEquals( 9.0F, d.y);
		assertEquals(12.0F, d.z);
		
		assertThrows(NullPointerException.class, () -> Vector3F.add(a, b, null));
		assertThrows(NullPointerException.class, () -> Vector3F.add(a, null, c));
		assertThrows(NullPointerException.class, () -> Vector3F.add(null, b, c));
	}
	
	@Test
	public void testClearCacheAndGetCacheSizeAndGetCached() {
		Vector3F.clearCache();
		
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
	public void testComputeV() {
		final Vector3F a = new Vector3F(1.0F, 0.0F, 0.0F);
		final Vector3F b = Vector3F.computeV(a);
		final Vector3F c = new Vector3F(0.0F, 1.0F, 0.0F);
		final Vector3F d = Vector3F.computeV(c);
		final Vector3F e = new Vector3F(0.0F, 0.0F, 1.0F);
		final Vector3F f = Vector3F.computeV(e);
		
		assertEquals(+0.0F, b.x);
		assertEquals(-1.0F, b.y);
		assertEquals(+0.0F, b.z);
		
		assertEquals(+1.0F, d.x);
		assertEquals(-0.0F, d.y);
		assertEquals(+0.0F, d.z);
		
		assertEquals(+1.0F, f.x);
		assertEquals(+0.0F, f.y);
		assertEquals(-0.0F, f.z);
		
		assertThrows(NullPointerException.class, () -> Vector3F.computeV(null));
	}
	
	@Test
	public void testConstants() {
		assertEquals(new Vector3F(Float.NaN, Float.NaN, Float.NaN), Vector3F.NaN);
		assertEquals(new Vector3F(0.0F, 0.0F, 0.0F), Vector3F.ZERO);
	}
	
	@Test
	public void testConstructor() {
		final Vector3F vector = new Vector3F();
		
		assertEquals(0.0F, vector.x);
		assertEquals(0.0F, vector.y);
		assertEquals(0.0F, vector.z);
	}
	
	@Test
	public void testConstructorFloat() {
		final Vector3F vector = new Vector3F(1.0F);
		
		assertEquals(1.0F, vector.x);
		assertEquals(1.0F, vector.y);
		assertEquals(1.0F, vector.z);
	}
	
	@Test
	public void testConstructorFloatFloatFloat() {
		final Vector3F vector = new Vector3F(1.0F, 2.0F, 3.0F);
		
		assertEquals(1.0F, vector.x);
		assertEquals(2.0F, vector.y);
		assertEquals(3.0F, vector.z);
	}
	
	@Test
	public void testConstructorPoint3F() {
		final Vector3F vector = new Vector3F(new Point3F(1.0F, 2.0F, 3.0F));
		
		assertEquals(1.0F, vector.x);
		assertEquals(2.0F, vector.y);
		assertEquals(3.0F, vector.z);
		
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
	public void testCrossProduct() {
		final Vector3F a = new Vector3F(1.0F, 0.0F, 0.0F);
		final Vector3F b = new Vector3F(0.0F, 1.0F, 0.0F);
		final Vector3F c = Vector3F.crossProduct(a, b);
		
		assertEquals(0.0F, c.x);
		assertEquals(0.0F, c.y);
		assertEquals(1.0F, c.z);
		
		assertTrue(c.isUnitVector());
		
		assertThrows(NullPointerException.class, () -> Vector3F.crossProduct(a, null));
		assertThrows(NullPointerException.class, () -> Vector3F.crossProduct(null, b));
	}
	
	@Test
	public void testDirectionNormalizedPoint3FPoint3F() {
		final Vector3F vector = Vector3F.directionNormalized(new Point3F(1.0F, 2.0F, 3.0F), new Point3F(2.0F, 2.0F, 3.0F));
		
		assertEquals(1.0F, vector.x);
		assertEquals(0.0F, vector.y);
		assertEquals(0.0F, vector.z);
		
		assertTrue(vector.isUnitVector());
		
		assertThrows(NullPointerException.class, () -> Vector3F.directionNormalized(new Point3F(1.0F, 2.0F, 3.0F), null));
		assertThrows(NullPointerException.class, () -> Vector3F.directionNormalized(null, new Point3F(2.0F, 2.0F, 3.0F)));
	}
	
	@Test
	public void testDirectionNormalizedPoint4FPoint4F() {
		final Vector3F vector = Vector3F.directionNormalized(new Point4F(1.0F, 2.0F, 3.0F), new Point4F(2.0F, 2.0F, 3.0F));
		
		assertEquals(1.0F, vector.x);
		assertEquals(0.0F, vector.y);
		assertEquals(0.0F, vector.z);
		
		assertTrue(vector.isUnitVector());
		
		assertThrows(NullPointerException.class, () -> Vector3F.directionNormalized(new Point4F(1.0F, 2.0F, 3.0F), null));
		assertThrows(NullPointerException.class, () -> Vector3F.directionNormalized(null, new Point4F(2.0F, 2.0F, 3.0F)));
	}
	
	@Test
	public void testDirectionPoint3FPoint3F() {
		final Vector3F vector = Vector3F.direction(new Point3F(1.0F, 2.0F, 3.0F), new Point3F(2.0F, 3.0F, 4.0F));
		
		assertEquals(1.0F, vector.x);
		assertEquals(1.0F, vector.y);
		assertEquals(1.0F, vector.z);
		
		assertThrows(NullPointerException.class, () -> Vector3F.direction(new Point3F(1.0F, 2.0F, 3.0F), null));
		assertThrows(NullPointerException.class, () -> Vector3F.direction(null, new Point3F(2.0F, 3.0F, 4.0F)));
	}
	
	@Test
	public void testDirectionPoint4FPoint4F() {
		final Vector3F vector = Vector3F.direction(new Point4F(1.0F, 2.0F, 3.0F), new Point4F(2.0F, 3.0F, 4.0F));
		
		assertEquals(1.0F, vector.x);
		assertEquals(1.0F, vector.y);
		assertEquals(1.0F, vector.z);
		
		assertThrows(NullPointerException.class, () -> Vector3F.direction(new Point4F(1.0F, 2.0F, 3.0F), null));
		assertThrows(NullPointerException.class, () -> Vector3F.direction(null, new Point4F(2.0F, 3.0F, 4.0F)));
	}
	
	@Test
	public void testDivide() {
		final Vector3F a = new Vector3F(2.0F, 4.0F, 8.0F);
		final Vector3F b = Vector3F.divide(a, 2.0F);
		final Vector3F c = Vector3F.divide(a, Float.NaN);
		
		assertEquals(1.0F, b.x);
		assertEquals(2.0F, b.y);
		assertEquals(4.0F, b.z);
		
		assertEquals(0.0F, c.x);
		assertEquals(0.0F, c.y);
		assertEquals(0.0F, c.z);
		
		assertThrows(NullPointerException.class, () -> Vector3F.divide(null, 2.0F));
	}
	
	@Test
	public void testDotProduct() {
		final Vector3F a = new Vector3F(+1.0F, +0.0F, +0.0F);
		final Vector3F b = new Vector3F(+1.0F, +0.0F, +0.0F);
		final Vector3F c = new Vector3F(+0.0F, -1.0F, +0.0F);
		final Vector3F d = new Vector3F(-1.0F, +0.0F, +0.0F);
		
		assertEquals(+1.0F, Vector3F.dotProduct(a, b));
		assertEquals(+0.0F, Vector3F.dotProduct(a, c));
		assertEquals(-1.0F, Vector3F.dotProduct(a, d));
		
		assertThrows(NullPointerException.class, () -> Vector3F.dotProduct(a, null));
		assertThrows(NullPointerException.class, () -> Vector3F.dotProduct(null, b));
	}
	
	@Test
	public void testDotProductAbs() {
		final Vector3F a = new Vector3F(+1.0F, +0.0F, +0.0F);
		final Vector3F b = new Vector3F(+1.0F, +0.0F, +0.0F);
		final Vector3F c = new Vector3F(+0.0F, -1.0F, +0.0F);
		final Vector3F d = new Vector3F(-1.0F, +0.0F, +0.0F);
		
		assertEquals(+1.0F, Vector3F.dotProductAbs(a, b));
		assertEquals(+0.0F, Vector3F.dotProductAbs(a, c));
		assertEquals(+1.0F, Vector3F.dotProductAbs(a, d));
		
		assertThrows(NullPointerException.class, () -> Vector3F.dotProductAbs(a, null));
		assertThrows(NullPointerException.class, () -> Vector3F.dotProductAbs(null, b));
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
	public void testFaceForwardVector3FVector3F() {
		final Vector3F a = new Vector3F(-1.0F, -0.0F, -0.0F);
		final Vector3F b = new Vector3F(+1.0F, +0.0F, +0.0F);
		final Vector3F c = Vector3F.faceForward(a, b);
		final Vector3F d = Vector3F.faceForward(b, b);
		
		assertEquals(b, c);
		assertEquals(b, d);
		
		assertThrows(NullPointerException.class, () -> Vector3F.faceForward(a, null));
		assertThrows(NullPointerException.class, () -> Vector3F.faceForward(null, b));
	}
	
	@Test
	public void testFaceForwardVector3FVector3FVector3F() {
		final Vector3F a = new Vector3F(-1.0F, -0.0F, -0.0F);
		final Vector3F b = new Vector3F(+1.0F, +0.0F, +0.0F);
		final Vector3F c = Vector3F.faceForward(a, b, a);
		final Vector3F d = Vector3F.faceForward(b, b, b);
		
		assertEquals(b, c);
		assertEquals(b, d);
		
		assertThrows(NullPointerException.class, () -> Vector3F.faceForward(a, b, null));
		assertThrows(NullPointerException.class, () -> Vector3F.faceForward(a, null, c));
		assertThrows(NullPointerException.class, () -> Vector3F.faceForward(null, b, c));
	}
	
	@Test
	public void testFaceForwardZ() {
		final Vector3F a = new Vector3F(+0.0F, +0.0F, -1.0F);
		final Vector3F b = new Vector3F(+0.0F, +0.0F, +1.0F);
		final Vector3F c = Vector3F.faceForwardZ(a, a);
		final Vector3F d = Vector3F.faceForwardZ(b, b);
		
		assertEquals(b, c);
		assertEquals(b, d);
		
		assertThrows(NullPointerException.class, () -> Vector3F.faceForwardZ(a, null));
		assertThrows(NullPointerException.class, () -> Vector3F.faceForwardZ(null, b));
	}
	
	@Test
	public void testHadamardProduct() {
		final Vector3F a = new Vector3F(1.0F, 2.0F, 3.0F);
		final Vector3F b = new Vector3F(2.0F, 3.0F, 4.0F);
		final Vector3F c = Vector3F.hadamardProduct(a, b);
		
		assertEquals( 2.0F, c.x);
		assertEquals( 6.0F, c.y);
		assertEquals(12.0F, c.z);
		
		assertThrows(NullPointerException.class, () -> Vector3F.hadamardProduct(a, null));
		assertThrows(NullPointerException.class, () -> Vector3F.hadamardProduct(null, b));
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
	public void testLerp() {
		final Vector3F a = new Vector3F(1.0F, 1.0F, 1.0F);
		final Vector3F b = new Vector3F(5.0F, 5.0F, 5.0F);
		final Vector3F c = Vector3F.lerp(a, b, 0.25F);
		
		assertEquals(2.0F, c.x);
		assertEquals(2.0F, c.y);
		assertEquals(2.0F, c.z);
		
		assertThrows(NullPointerException.class, () -> Vector3F.lerp(a, null, 0.25F));
		assertThrows(NullPointerException.class, () -> Vector3F.lerp(null, b, 0.25F));
	}
	
	@Test
	public void testMultiply() {
		final Vector3F a = new Vector3F(1.0F, 2.0F, 3.0F);
		final Vector3F b = Vector3F.multiply(a, 2.0F);
		
		assertEquals(2.0F, b.x);
		assertEquals(4.0F, b.y);
		assertEquals(6.0F, b.z);
		
		assertThrows(NullPointerException.class, () -> Vector3F.multiply(null, 2.0F));
	}
	
	@Test
	public void testNegate() {
		final Vector3F a = new Vector3F(1.0F, 2.0F, 3.0F);
		final Vector3F b = Vector3F.negate(a);
		final Vector3F c = Vector3F.negate(b);
		
		assertEquals(-1.0F, b.x);
		assertEquals(-2.0F, b.y);
		assertEquals(-3.0F, b.z);
		
		assertEquals(+1.0F, c.x);
		assertEquals(+2.0F, c.y);
		assertEquals(+3.0F, c.z);
		
		assertThrows(NullPointerException.class, () -> Vector3F.negate(null));
	}
	
	@Test
	public void testNegateX() {
		final Vector3F a = new Vector3F(1.0F, 2.0F, 3.0F);
		final Vector3F b = Vector3F.negateX(a);
		final Vector3F c = Vector3F.negateX(b);
		
		assertEquals(-1.0F, b.x);
		assertEquals(+2.0F, b.y);
		assertEquals(+3.0F, b.z);
		
		assertEquals(+1.0F, c.x);
		assertEquals(+2.0F, c.y);
		assertEquals(+3.0F, c.z);
		
		assertThrows(NullPointerException.class, () -> Vector3F.negateX(null));
	}
	
	@Test
	public void testNegateY() {
		final Vector3F a = new Vector3F(1.0F, 2.0F, 3.0F);
		final Vector3F b = Vector3F.negateY(a);
		final Vector3F c = Vector3F.negateY(b);
		
		assertEquals(+1.0F, b.x);
		assertEquals(-2.0F, b.y);
		assertEquals(+3.0F, b.z);
		
		assertEquals(+1.0F, c.x);
		assertEquals(+2.0F, c.y);
		assertEquals(+3.0F, c.z);
		
		assertThrows(NullPointerException.class, () -> Vector3F.negateY(null));
	}
	
	@Test
	public void testNegateZ() {
		final Vector3F a = new Vector3F(1.0F, 2.0F, 3.0F);
		final Vector3F b = Vector3F.negateZ(a);
		final Vector3F c = Vector3F.negateZ(b);
		
		assertEquals(+1.0F, b.x);
		assertEquals(+2.0F, b.y);
		assertEquals(-3.0F, b.z);
		
		assertEquals(+1.0F, c.x);
		assertEquals(+2.0F, c.y);
		assertEquals(+3.0F, c.z);
		
		assertThrows(NullPointerException.class, () -> Vector3F.negateZ(null));
	}
	
	@Test
	public void testNormalNormalizedPoint3FPoint3FPoint3F() {
		final Point3F a = new Point3F(0.0F, 0.0F, 0.0F);
		final Point3F b = new Point3F(1.0F, 0.0F, 0.0F);
		final Point3F c = new Point3F(0.0F, 1.0F, 0.0F);
		
		final Vector3F vector = Vector3F.normalNormalized(a, b, c);
		
		assertEquals(0.0F, vector.x);
		assertEquals(0.0F, vector.y);
		assertEquals(1.0F, vector.z);
		
		assertThrows(NullPointerException.class, () -> Vector3F.normalNormalized(a, b, null));
		assertThrows(NullPointerException.class, () -> Vector3F.normalNormalized(a, null, c));
		assertThrows(NullPointerException.class, () -> Vector3F.normalNormalized(null, b, c));
	}
	
	@Test
	public void testNormalNormalizedVector3FVector3FVector3FPoint3F() {
		final Vector3F vector = Vector3F.normalNormalized(new Vector3F(1.0F, 0.0F, 0.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(0.0F, 0.0F, 1.0F), new Point3F(1.0F, 0.0F, 0.0F));
		
		assertEquals(1.0F, vector.x);
		assertEquals(0.0F, vector.y);
		assertEquals(0.0F, vector.z);
		
		assertThrows(NullPointerException.class, () -> Vector3F.normalNormalized(new Vector3F(1.0F, 0.0F, 0.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(0.0F, 0.0F, 1.0F), null));
		assertThrows(NullPointerException.class, () -> Vector3F.normalNormalized(new Vector3F(1.0F, 0.0F, 0.0F), new Vector3F(0.0F, 1.0F, 0.0F), null, new Point3F(1.0F, 1.0F, 1.0F)));
		assertThrows(NullPointerException.class, () -> Vector3F.normalNormalized(new Vector3F(1.0F, 0.0F, 0.0F), null, new Vector3F(0.0F, 0.0F, 1.0F), new Point3F(1.0F, 1.0F, 1.0F)));
		assertThrows(NullPointerException.class, () -> Vector3F.normalNormalized(null, new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(0.0F, 0.0F, 1.0F), new Point3F(1.0F, 1.0F, 1.0F)));
	}
	
	@Test
	public void testNormalPoint3FPoint3FPoint3F() {
		final Point3F a = new Point3F(0.0F, 0.0F, 0.0F);
		final Point3F b = new Point3F(1.0F, 0.0F, 0.0F);
		final Point3F c = new Point3F(0.0F, 1.0F, 0.0F);
		
		final Vector3F vector = Vector3F.normal(a, b, c);
		
		assertEquals(0.0F, vector.x);
		assertEquals(0.0F, vector.y);
		assertEquals(1.0F, vector.z);
		
		assertThrows(NullPointerException.class, () -> Vector3F.normal(a, b, null));
		assertThrows(NullPointerException.class, () -> Vector3F.normal(a, null, c));
		assertThrows(NullPointerException.class, () -> Vector3F.normal(null, b, c));
	}
	
	@Test
	public void testNormalVector3FVector3FVector3FPoint3F() {
		final Vector3F vector = Vector3F.normal(new Vector3F(1.0F, 0.0F, 0.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(0.0F, 0.0F, 1.0F), new Point3F(1.0F, 1.0F, 1.0F));
		
		assertEquals(1.0F, vector.x);
		assertEquals(1.0F, vector.y);
		assertEquals(1.0F, vector.z);
		
		assertThrows(NullPointerException.class, () -> Vector3F.normal(new Vector3F(1.0F, 0.0F, 0.0F), new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(0.0F, 0.0F, 1.0F), null));
		assertThrows(NullPointerException.class, () -> Vector3F.normal(new Vector3F(1.0F, 0.0F, 0.0F), new Vector3F(0.0F, 1.0F, 0.0F), null, new Point3F(1.0F, 1.0F, 1.0F)));
		assertThrows(NullPointerException.class, () -> Vector3F.normal(new Vector3F(1.0F, 0.0F, 0.0F), null, new Vector3F(0.0F, 0.0F, 1.0F), new Point3F(1.0F, 1.0F, 1.0F)));
		assertThrows(NullPointerException.class, () -> Vector3F.normal(null, new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(0.0F, 0.0F, 1.0F), new Point3F(1.0F, 1.0F, 1.0F)));
	}
	
	@Test
	public void testNormalize() {
		final Vector3F a = new Vector3F(+1.0F, +0.0F, +0.0F);
		final Vector3F b = Vector3F.normalize(a);
		final Vector3F c = new Vector3F(+0.0F, +1.0F, +0.0F);
		final Vector3F d = Vector3F.normalize(c);
		final Vector3F e = new Vector3F(+0.0F, +0.0F, +1.0F);
		final Vector3F f = Vector3F.normalize(e);
		final Vector3F g = new Vector3F(+1.0F, +1.0F, +1.0F);
		final Vector3F h = Vector3F.normalize(g);
		final Vector3F i = Vector3F.normalize(h);
		final Vector3F j = new Vector3F(-1.0F, -1.0F, -1.0F);
		final Vector3F k = Vector3F.normalize(j);
		final Vector3F l = Vector3F.normalize(k);
		
		assertEquals(a, b);
		assertEquals(c, d);
		assertEquals(e, f);
		assertEquals(h, i);
		assertEquals(k, l);
		
		assertTrue(a == b);
		assertTrue(c == d);
		assertTrue(e == f);
		assertTrue(h == i);
		assertTrue(k == l);
		
		assertTrue(a.isUnitVector());
		assertTrue(c.isUnitVector());
		assertTrue(e.isUnitVector());
		assertTrue(h.isUnitVector());
		assertTrue(k.isUnitVector());
		
		assertFalse(g.isUnitVector());
		assertFalse(j.isUnitVector());
		
		assertThrows(NullPointerException.class, () -> Vector3F.normalize(null));
	}
	
	@Test
	public void testOrthogonal() {
		final Vector3F a = new Vector3F(1.0F, 0.0F, 0.0F);
		final Vector3F b = new Vector3F(0.0F, 1.0F, 0.0F);
		final Vector3F c = new Vector3F(1.0F, 1.0F, 1.0F);
		
		assertTrue(Vector3F.orthogonal(a, b));
		
		assertFalse(Vector3F.orthogonal(a, c));
		
		assertThrows(NullPointerException.class, () -> Vector3F.orthogonal(a, null));
		assertThrows(NullPointerException.class, () -> Vector3F.orthogonal(null, b));
	}
	
	@Test
	public void testRandom() {
		final Vector3F vector = Vector3F.random();
		
		assertTrue(vector.x >= -1.0F && vector.x <= 1.0F);
		assertTrue(vector.y >= -1.0F && vector.y <= 1.0F);
		assertTrue(vector.z >= -1.0F && vector.z <= 1.0F);
	}
	
	@Test
	public void testRandomNormalized() {
		final Vector3F vector = Vector3F.randomNormalized();
		
		assertTrue(vector.x >= -1.0F && vector.x <= 1.0F);
		assertTrue(vector.y >= -1.0F && vector.y <= 1.0F);
		assertTrue(vector.z >= -1.0F && vector.z <= 1.0F);
		
		assertTrue(vector.isUnitVector());
	}
	
	@Test
	public void testRead() throws IOException {
		final Vector3F a = new Vector3F(1.0F, 2.0F, 3.0F);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeFloat(a.x);
		dataOutput.writeFloat(a.y);
		dataOutput.writeFloat(a.z);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Vector3F b = Vector3F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Vector3F.read(null));
		assertThrows(UncheckedIOException.class, () -> Vector3F.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testReciprocal() {
		final Vector3F a = Vector3F.reciprocal(new Vector3F(Float.NaN, Float.NaN, Float.NaN));
		final Vector3F b = Vector3F.reciprocal(new Vector3F(2.0F, 4.0F, 8.0F));
		
		assertEquals(Float.NaN, a.x);
		assertEquals(Float.NaN, a.y);
		assertEquals(Float.NaN, a.z);
		
		assertEquals(0.500F, b.x);
		assertEquals(0.250F, b.y);
		assertEquals(0.125F, b.z);
		
		assertThrows(NullPointerException.class, () -> Vector3F.reciprocal(null));
	}
	
	@Test
	public void testRefraction() {
		final Optional<Vector3F> a = Vector3F.refraction(new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(0.0F, 1.0F, 0.0F), 1.0F);
		final Optional<Vector3F> b = Vector3F.refraction(new Vector3F(0.0F, 1.0F, 0.0F), new Vector3F(1.0F, 0.0F, 0.0F), 1.0F);
		
		assertNotNull(a);
		assertNotNull(b);
		
		assertTrue(a.isPresent());
		
		assertFalse(b.isPresent());
		
		assertEquals(+0.0F, a.get().x);
		assertEquals(-1.0F, a.get().y);
		assertEquals(+0.0F, a.get().z);
		
		assertThrows(NullPointerException.class, () -> Vector3F.refraction(new Vector3F(0.0F, 1.0F, 0.0F), null, 1.0F));
		assertThrows(NullPointerException.class, () -> Vector3F.refraction(null, new Vector3F(0.0F, 1.0F, 0.0F), 1.0F));
	}
	
	@Test
	public void testSameHemisphere() {
		final Vector3F a = new Vector3F(+1.0F, 0.0F, 0.0F);
		final Vector3F b = new Vector3F(-1.0F, 0.0F, 0.0F);
		
		assertTrue(Vector3F.sameHemisphere(a, a));
		
		assertFalse(Vector3F.sameHemisphere(a, b));
		
		assertThrows(NullPointerException.class, () -> Vector3F.sameHemisphere(a, null));
		assertThrows(NullPointerException.class, () -> Vector3F.sameHemisphere(null, b));
	}
	
	@Test
	public void testSameHemisphereZ() {
		final Vector3F a = new Vector3F(0.0F, 0.0F, +1.0F);
		final Vector3F b = new Vector3F(0.0F, 0.0F, -1.0F);
		
		assertTrue(Vector3F.sameHemisphereZ(a, a));
		
		assertFalse(Vector3F.sameHemisphereZ(a, b));
		
		assertThrows(NullPointerException.class, () -> Vector3F.sameHemisphereZ(a, null));
		assertThrows(NullPointerException.class, () -> Vector3F.sameHemisphereZ(null, b));
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
	public void testSubtract() {
		final Vector3F a = new Vector3F(3.0F, 5.0F, 7.0F);
		final Vector3F b = new Vector3F(2.0F, 3.0F, 4.0F);
		final Vector3F c = Vector3F.subtract(a, b);
		
		assertEquals(1.0F, c.x);
		assertEquals(2.0F, c.y);
		assertEquals(3.0F, c.z);
		
		assertThrows(NullPointerException.class, () -> Vector3F.subtract(a, null));
		assertThrows(NullPointerException.class, () -> Vector3F.subtract(null, b));
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
	public void testU() {
		final Vector3F vector = Vector3F.u();
		
		assertEquals(1.0F, vector.x);
		assertEquals(0.0F, vector.y);
		assertEquals(0.0F, vector.z);
	}
	
	@Test
	public void testUFloat() {
		final Vector3F vector = Vector3F.u(2.0F);
		
		assertEquals(2.0F, vector.x);
		assertEquals(0.0F, vector.y);
		assertEquals(0.0F, vector.z);
	}
	
	@Test
	public void testV() {
		final Vector3F vector = Vector3F.v();
		
		assertEquals(0.0F, vector.x);
		assertEquals(1.0F, vector.y);
		assertEquals(0.0F, vector.z);
	}
	
	@Test
	public void testVFloat() {
		final Vector3F vector = Vector3F.v(2.0F);
		
		assertEquals(0.0F, vector.x);
		assertEquals(2.0F, vector.y);
		assertEquals(0.0F, vector.z);
	}
	
	@Test
	public void testW() {
		final Vector3F vector = Vector3F.w();
		
		assertEquals(0.0F, vector.x);
		assertEquals(0.0F, vector.y);
		assertEquals(1.0F, vector.z);
	}
	
	@Test
	public void testWFloat() {
		final Vector3F vector = Vector3F.w(2.0F);
		
		assertEquals(0.0F, vector.x);
		assertEquals(0.0F, vector.y);
		assertEquals(2.0F, vector.z);
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
	
	@Test
	public void testX() {
		final Vector3F vector = Vector3F.x();
		
		assertEquals(1.0F, vector.x);
		assertEquals(0.0F, vector.y);
		assertEquals(0.0F, vector.z);
	}
	
	@Test
	public void testXFloat() {
		final Vector3F vector = Vector3F.x(2.0F);
		
		assertEquals(2.0F, vector.x);
		assertEquals(0.0F, vector.y);
		assertEquals(0.0F, vector.z);
	}
	
	@Test
	public void testY() {
		final Vector3F vector = Vector3F.y();
		
		assertEquals(0.0F, vector.x);
		assertEquals(1.0F, vector.y);
		assertEquals(0.0F, vector.z);
	}
	
	@Test
	public void testYFloat() {
		final Vector3F vector = Vector3F.y(2.0F);
		
		assertEquals(0.0F, vector.x);
		assertEquals(2.0F, vector.y);
		assertEquals(0.0F, vector.z);
	}
	
	@Test
	public void testZ() {
		final Vector3F vector = Vector3F.z();
		
		assertEquals(0.0F, vector.x);
		assertEquals(0.0F, vector.y);
		assertEquals(1.0F, vector.z);
	}
	
	@Test
	public void testZFloat() {
		final Vector3F vector = Vector3F.z(2.0F);
		
		assertEquals(0.0F, vector.x);
		assertEquals(0.0F, vector.y);
		assertEquals(2.0F, vector.z);
	}
}