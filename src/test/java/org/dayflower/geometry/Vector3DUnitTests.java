/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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
public final class Vector3DUnitTests {
	public Vector3DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAbsolute() {
		final Vector3D a = Vector3D.absolute(new Vector3D(+1.0D, +2.0D, +3.0D));
		final Vector3D b = Vector3D.absolute(new Vector3D(-1.0D, -2.0D, -3.0D));
		
		assertEquals(1.0D, a.x);
		assertEquals(2.0D, a.y);
		assertEquals(3.0D, a.z);
		
		assertEquals(1.0D, b.x);
		assertEquals(2.0D, b.y);
		assertEquals(3.0D, b.z);
		
		assertThrows(NullPointerException.class, () -> Vector3D.absolute(null));
	}
	
	@Test
	public void testAddVector3DVector3D() {
		final Vector3D a = new Vector3D(1.0D, 2.0D, 3.0D);
		final Vector3D b = new Vector3D(2.0D, 3.0D, 4.0D);
		final Vector3D c = Vector3D.add(a, b);
		
		assertEquals(3.0D, c.x);
		assertEquals(5.0D, c.y);
		assertEquals(7.0D, c.z);
		
		assertThrows(NullPointerException.class, () -> Vector3D.add(a, null));
		assertThrows(NullPointerException.class, () -> Vector3D.add(null, b));
	}
	
	@Test
	public void testAddVector3DVector3DVector3D() {
		final Vector3D a = new Vector3D(1.0D, 2.0D, 3.0D);
		final Vector3D b = new Vector3D(2.0D, 3.0D, 4.0D);
		final Vector3D c = new Vector3D(3.0D, 4.0D, 5.0D);
		final Vector3D d = Vector3D.add(a, b, c);
		
		assertEquals( 6.0D, d.x);
		assertEquals( 9.0D, d.y);
		assertEquals(12.0D, d.z);
		
		assertThrows(NullPointerException.class, () -> Vector3D.add(a, b, null));
		assertThrows(NullPointerException.class, () -> Vector3D.add(a, null, c));
		assertThrows(NullPointerException.class, () -> Vector3D.add(null, b, c));
	}
	
	@Test
	public void testClearCacheAndGetCacheSizeAndGetCached() {
		Vector3D.clearCache();
		
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
	public void testComputeV() {
		final Vector3D a = new Vector3D(1.0D, 0.0D, 0.0D);
		final Vector3D b = Vector3D.computeV(a);
		final Vector3D c = new Vector3D(0.0D, 1.0D, 0.0D);
		final Vector3D d = Vector3D.computeV(c);
		final Vector3D e = new Vector3D(0.0D, 0.0D, 1.0D);
		final Vector3D f = Vector3D.computeV(e);
		
		assertEquals(+0.0D, b.x);
		assertEquals(-1.0D, b.y);
		assertEquals(+0.0D, b.z);
		
		assertEquals(+1.0D, d.x);
		assertEquals(-0.0D, d.y);
		assertEquals(+0.0D, d.z);
		
		assertEquals(+1.0D, f.x);
		assertEquals(+0.0D, f.y);
		assertEquals(-0.0D, f.z);
		
		assertThrows(NullPointerException.class, () -> Vector3D.computeV(null));
	}
	
	@Test
	public void testConstants() {
		assertEquals(new Vector3D(Double.NaN, Double.NaN, Double.NaN), Vector3D.NaN);
		assertEquals(new Vector3D(0.0D, 0.0D, 0.0D), Vector3D.ZERO);
	}
	
	@Test
	public void testConstructor() {
		final Vector3D vector = new Vector3D();
		
		assertEquals(0.0D, vector.x);
		assertEquals(0.0D, vector.y);
		assertEquals(0.0D, vector.z);
	}
	
	@Test
	public void testConstructorDouble() {
		final Vector3D vector = new Vector3D(1.0D);
		
		assertEquals(1.0D, vector.x);
		assertEquals(1.0D, vector.y);
		assertEquals(1.0D, vector.z);
	}
	
	@Test
	public void testConstructorDoubleDoubleDouble() {
		final Vector3D vector = new Vector3D(1.0D, 2.0D, 3.0D);
		
		assertEquals(1.0D, vector.x);
		assertEquals(2.0D, vector.y);
		assertEquals(3.0D, vector.z);
	}
	
	@Test
	public void testConstructorPoint3D() {
		final Vector3D vector = new Vector3D(new Point3D(1.0D, 2.0D, 3.0D));
		
		assertEquals(1.0D, vector.x);
		assertEquals(2.0D, vector.y);
		assertEquals(3.0D, vector.z);
		
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
	public void testCrossProduct() {
		final Vector3D a = new Vector3D(1.0D, 0.0D, 0.0D);
		final Vector3D b = new Vector3D(0.0D, 1.0D, 0.0D);
		final Vector3D c = Vector3D.crossProduct(a, b);
		
		assertEquals(0.0D, c.x);
		assertEquals(0.0D, c.y);
		assertEquals(1.0D, c.z);
		
		assertTrue(c.isUnitVector());
		
		assertThrows(NullPointerException.class, () -> Vector3D.crossProduct(a, null));
		assertThrows(NullPointerException.class, () -> Vector3D.crossProduct(null, b));
	}
	
	@Test
	public void testDirectionNormalizedPoint3DPoint3D() {
		final Vector3D vector = Vector3D.directionNormalized(new Point3D(1.0D, 2.0D, 3.0D), new Point3D(2.0D, 2.0D, 3.0D));
		
		assertEquals(1.0D, vector.x);
		assertEquals(0.0D, vector.y);
		assertEquals(0.0D, vector.z);
		
		assertTrue(vector.isUnitVector());
		
		assertThrows(NullPointerException.class, () -> Vector3D.directionNormalized(new Point3D(1.0D, 2.0D, 3.0D), null));
		assertThrows(NullPointerException.class, () -> Vector3D.directionNormalized(null, new Point3D(2.0D, 2.0D, 3.0D)));
	}
	
	@Test
	public void testDirectionNormalizedPoint4DPoint4D() {
		final Vector3D vector = Vector3D.directionNormalized(new Point4D(1.0D, 2.0D, 3.0D), new Point4D(2.0D, 2.0D, 3.0D));
		
		assertEquals(1.0D, vector.x);
		assertEquals(0.0D, vector.y);
		assertEquals(0.0D, vector.z);
		
		assertTrue(vector.isUnitVector());
		
		assertThrows(NullPointerException.class, () -> Vector3D.directionNormalized(new Point4D(1.0D, 2.0D, 3.0D), null));
		assertThrows(NullPointerException.class, () -> Vector3D.directionNormalized(null, new Point4D(2.0D, 2.0D, 3.0D)));
	}
	
	@Test
	public void testDirectionPoint3DPoint3D() {
		final Vector3D vector = Vector3D.direction(new Point3D(1.0D, 2.0D, 3.0D), new Point3D(2.0D, 3.0D, 4.0D));
		
		assertEquals(1.0D, vector.x);
		assertEquals(1.0D, vector.y);
		assertEquals(1.0D, vector.z);
		
		assertThrows(NullPointerException.class, () -> Vector3D.direction(new Point3D(1.0D, 2.0D, 3.0D), null));
		assertThrows(NullPointerException.class, () -> Vector3D.direction(null, new Point3D(2.0D, 3.0D, 4.0D)));
	}
	
	@Test
	public void testDirectionPoint4DPoint4D() {
		final Vector3D vector = Vector3D.direction(new Point4D(1.0D, 2.0D, 3.0D), new Point4D(2.0D, 3.0D, 4.0D));
		
		assertEquals(1.0D, vector.x);
		assertEquals(1.0D, vector.y);
		assertEquals(1.0D, vector.z);
		
		assertThrows(NullPointerException.class, () -> Vector3D.direction(new Point4D(1.0D, 2.0D, 3.0D), null));
		assertThrows(NullPointerException.class, () -> Vector3D.direction(null, new Point4D(2.0D, 3.0D, 4.0D)));
	}
	
	@Test
	public void testDivide() {
		final Vector3D a = new Vector3D(2.0D, 4.0D, 8.0D);
		final Vector3D b = Vector3D.divide(a, 2.0D);
		final Vector3D c = Vector3D.divide(a, Double.NaN);
		
		assertEquals(1.0D, b.x);
		assertEquals(2.0D, b.y);
		assertEquals(4.0D, b.z);
		
		assertEquals(0.0D, c.x);
		assertEquals(0.0D, c.y);
		assertEquals(0.0D, c.z);
		
		assertThrows(NullPointerException.class, () -> Vector3D.divide(null, 2.0D));
	}
	
	@Test
	public void testDotProduct() {
		final Vector3D a = new Vector3D(+1.0D, +0.0D, +0.0D);
		final Vector3D b = new Vector3D(+1.0D, +0.0D, +0.0D);
		final Vector3D c = new Vector3D(+0.0D, -1.0D, +0.0D);
		final Vector3D d = new Vector3D(-1.0D, +0.0D, +0.0D);
		
		assertEquals(+1.0D, Vector3D.dotProduct(a, b));
		assertEquals(+0.0D, Vector3D.dotProduct(a, c));
		assertEquals(-1.0D, Vector3D.dotProduct(a, d));
		
		assertThrows(NullPointerException.class, () -> Vector3D.dotProduct(a, null));
		assertThrows(NullPointerException.class, () -> Vector3D.dotProduct(null, b));
	}
	
	@Test
	public void testDotProductAbs() {
		final Vector3D a = new Vector3D(+1.0D, +0.0D, +0.0D);
		final Vector3D b = new Vector3D(+1.0D, +0.0D, +0.0D);
		final Vector3D c = new Vector3D(+0.0D, -1.0D, +0.0D);
		final Vector3D d = new Vector3D(-1.0D, +0.0D, +0.0D);
		
		assertEquals(+1.0D, Vector3D.dotProductAbs(a, b));
		assertEquals(+0.0D, Vector3D.dotProductAbs(a, c));
		assertEquals(+1.0D, Vector3D.dotProductAbs(a, d));
		
		assertThrows(NullPointerException.class, () -> Vector3D.dotProductAbs(a, null));
		assertThrows(NullPointerException.class, () -> Vector3D.dotProductAbs(null, b));
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
	public void testFaceForwardVector3DVector3D() {
		final Vector3D a = new Vector3D(-1.0D, -0.0D, -0.0D);
		final Vector3D b = new Vector3D(+1.0D, +0.0D, +0.0D);
		final Vector3D c = Vector3D.faceForward(a, b);
		final Vector3D d = Vector3D.faceForward(b, b);
		
		assertEquals(b, c);
		assertEquals(b, d);
		
		assertThrows(NullPointerException.class, () -> Vector3D.faceForward(a, null));
		assertThrows(NullPointerException.class, () -> Vector3D.faceForward(null, b));
	}
	
	@Test
	public void testFaceForwardVector3DVector3DVector3D() {
		final Vector3D a = new Vector3D(-1.0D, -0.0D, -0.0D);
		final Vector3D b = new Vector3D(+1.0D, +0.0D, +0.0D);
		final Vector3D c = Vector3D.faceForward(a, b, a);
		final Vector3D d = Vector3D.faceForward(b, b, b);
		
		assertEquals(b, c);
		assertEquals(b, d);
		
		assertThrows(NullPointerException.class, () -> Vector3D.faceForward(a, b, null));
		assertThrows(NullPointerException.class, () -> Vector3D.faceForward(a, null, c));
		assertThrows(NullPointerException.class, () -> Vector3D.faceForward(null, b, c));
	}
	
	@Test
	public void testFaceForwardZ() {
		final Vector3D a = new Vector3D(+0.0D, +0.0D, -1.0D);
		final Vector3D b = new Vector3D(+0.0D, +0.0D, +1.0D);
		final Vector3D c = Vector3D.faceForwardZ(a, a);
		final Vector3D d = Vector3D.faceForwardZ(b, b);
		
		assertEquals(b, c);
		assertEquals(b, d);
		
		assertThrows(NullPointerException.class, () -> Vector3D.faceForwardZ(a, null));
		assertThrows(NullPointerException.class, () -> Vector3D.faceForwardZ(null, b));
	}
	
	@Test
	public void testHadamardProduct() {
		final Vector3D a = new Vector3D(1.0D, 2.0D, 3.0D);
		final Vector3D b = new Vector3D(2.0D, 3.0D, 4.0D);
		final Vector3D c = Vector3D.hadamardProduct(a, b);
		
		assertEquals( 2.0D, c.x);
		assertEquals( 6.0D, c.y);
		assertEquals(12.0D, c.z);
		
		assertThrows(NullPointerException.class, () -> Vector3D.hadamardProduct(a, null));
		assertThrows(NullPointerException.class, () -> Vector3D.hadamardProduct(null, b));
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
	public void testLerp() {
		final Vector3D a = new Vector3D(1.0D, 1.0D, 1.0D);
		final Vector3D b = new Vector3D(5.0D, 5.0D, 5.0D);
		final Vector3D c = Vector3D.lerp(a, b, 0.25D);
		
		assertEquals(2.0D, c.x);
		assertEquals(2.0D, c.y);
		assertEquals(2.0D, c.z);
		
		assertThrows(NullPointerException.class, () -> Vector3D.lerp(a, null, 0.25D));
		assertThrows(NullPointerException.class, () -> Vector3D.lerp(null, b, 0.25D));
	}
	
	@Test
	public void testMultiply() {
		final Vector3D a = new Vector3D(1.0D, 2.0D, 3.0D);
		final Vector3D b = Vector3D.multiply(a, 2.0D);
		
		assertEquals(2.0D, b.x);
		assertEquals(4.0D, b.y);
		assertEquals(6.0D, b.z);
		
		assertThrows(NullPointerException.class, () -> Vector3D.multiply(null, 2.0D));
	}
	
	@Test
	public void testNegate() {
		final Vector3D a = new Vector3D(1.0D, 2.0D, 3.0D);
		final Vector3D b = Vector3D.negate(a);
		final Vector3D c = Vector3D.negate(b);
		
		assertEquals(-1.0D, b.x);
		assertEquals(-2.0D, b.y);
		assertEquals(-3.0D, b.z);
		
		assertEquals(+1.0D, c.x);
		assertEquals(+2.0D, c.y);
		assertEquals(+3.0D, c.z);
		
		assertThrows(NullPointerException.class, () -> Vector3D.negate(null));
	}
	
	@Test
	public void testNegateX() {
		final Vector3D a = new Vector3D(1.0D, 2.0D, 3.0D);
		final Vector3D b = Vector3D.negateX(a);
		final Vector3D c = Vector3D.negateX(b);
		
		assertEquals(-1.0D, b.x);
		assertEquals(+2.0D, b.y);
		assertEquals(+3.0D, b.z);
		
		assertEquals(+1.0D, c.x);
		assertEquals(+2.0D, c.y);
		assertEquals(+3.0D, c.z);
		
		assertThrows(NullPointerException.class, () -> Vector3D.negateX(null));
	}
	
	@Test
	public void testNegateY() {
		final Vector3D a = new Vector3D(1.0D, 2.0D, 3.0D);
		final Vector3D b = Vector3D.negateY(a);
		final Vector3D c = Vector3D.negateY(b);
		
		assertEquals(+1.0D, b.x);
		assertEquals(-2.0D, b.y);
		assertEquals(+3.0D, b.z);
		
		assertEquals(+1.0D, c.x);
		assertEquals(+2.0D, c.y);
		assertEquals(+3.0D, c.z);
		
		assertThrows(NullPointerException.class, () -> Vector3D.negateY(null));
	}
	
	@Test
	public void testNegateZ() {
		final Vector3D a = new Vector3D(1.0D, 2.0D, 3.0D);
		final Vector3D b = Vector3D.negateZ(a);
		final Vector3D c = Vector3D.negateZ(b);
		
		assertEquals(+1.0D, b.x);
		assertEquals(+2.0D, b.y);
		assertEquals(-3.0D, b.z);
		
		assertEquals(+1.0D, c.x);
		assertEquals(+2.0D, c.y);
		assertEquals(+3.0D, c.z);
		
		assertThrows(NullPointerException.class, () -> Vector3D.negateZ(null));
	}
	
	@Test
	public void testNormalNormalizedPoint3DPoint3DPoint3D() {
		final Point3D a = new Point3D(0.0D, 0.0D, 0.0D);
		final Point3D b = new Point3D(1.0D, 0.0D, 0.0D);
		final Point3D c = new Point3D(0.0D, 1.0D, 0.0D);
		
		final Vector3D vector = Vector3D.normalNormalized(a, b, c);
		
		assertEquals(0.0D, vector.x);
		assertEquals(0.0D, vector.y);
		assertEquals(1.0D, vector.z);
		
		assertThrows(NullPointerException.class, () -> Vector3D.normalNormalized(a, b, null));
		assertThrows(NullPointerException.class, () -> Vector3D.normalNormalized(a, null, c));
		assertThrows(NullPointerException.class, () -> Vector3D.normalNormalized(null, b, c));
	}
	
	@Test
	public void testNormalNormalizedVector3DVector3DVector3DPoint3D() {
		final Vector3D vector = Vector3D.normalNormalized(new Vector3D(1.0D, 0.0D, 0.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 0.0D, 0.0D));
		
		assertEquals(1.0D, vector.x);
		assertEquals(0.0D, vector.y);
		assertEquals(0.0D, vector.z);
		
		assertThrows(NullPointerException.class, () -> Vector3D.normalNormalized(new Vector3D(1.0D, 0.0D, 0.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(0.0D, 0.0D, 1.0D), null));
		assertThrows(NullPointerException.class, () -> Vector3D.normalNormalized(new Vector3D(1.0D, 0.0D, 0.0D), new Vector3D(0.0D, 1.0D, 0.0D), null, new Point3D(1.0D, 1.0D, 1.0D)));
		assertThrows(NullPointerException.class, () -> Vector3D.normalNormalized(new Vector3D(1.0D, 0.0D, 0.0D), null, new Vector3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 1.0D, 1.0D)));
		assertThrows(NullPointerException.class, () -> Vector3D.normalNormalized(null, new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 1.0D, 1.0D)));
	}
	
	@Test
	public void testNormalPoint3DPoint3DPoint3D() {
		final Point3D a = new Point3D(0.0D, 0.0D, 0.0D);
		final Point3D b = new Point3D(1.0D, 0.0D, 0.0D);
		final Point3D c = new Point3D(0.0D, 1.0D, 0.0D);
		
		final Vector3D vector = Vector3D.normal(a, b, c);
		
		assertEquals(0.0D, vector.x);
		assertEquals(0.0D, vector.y);
		assertEquals(1.0D, vector.z);
		
		assertThrows(NullPointerException.class, () -> Vector3D.normal(a, b, null));
		assertThrows(NullPointerException.class, () -> Vector3D.normal(a, null, c));
		assertThrows(NullPointerException.class, () -> Vector3D.normal(null, b, c));
	}
	
	@Test
	public void testNormalVector3DVector3DVector3DPoint3D() {
		final Vector3D vector = Vector3D.normal(new Vector3D(1.0D, 0.0D, 0.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 1.0D, 1.0D));
		
		assertEquals(1.0D, vector.x);
		assertEquals(1.0D, vector.y);
		assertEquals(1.0D, vector.z);
		
		assertThrows(NullPointerException.class, () -> Vector3D.normal(new Vector3D(1.0D, 0.0D, 0.0D), new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(0.0D, 0.0D, 1.0D), null));
		assertThrows(NullPointerException.class, () -> Vector3D.normal(new Vector3D(1.0D, 0.0D, 0.0D), new Vector3D(0.0D, 1.0D, 0.0D), null, new Point3D(1.0D, 1.0D, 1.0D)));
		assertThrows(NullPointerException.class, () -> Vector3D.normal(new Vector3D(1.0D, 0.0D, 0.0D), null, new Vector3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 1.0D, 1.0D)));
		assertThrows(NullPointerException.class, () -> Vector3D.normal(null, new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 1.0D, 1.0D)));
	}
	
	@Test
	public void testNormalize() {
		final Vector3D a = new Vector3D(+1.0D, +0.0D, +0.0D);
		final Vector3D b = Vector3D.normalize(a);
		final Vector3D c = new Vector3D(+0.0D, +1.0D, +0.0D);
		final Vector3D d = Vector3D.normalize(c);
		final Vector3D e = new Vector3D(+0.0D, +0.0D, +1.0D);
		final Vector3D f = Vector3D.normalize(e);
		final Vector3D g = new Vector3D(+1.0D, +1.0D, +1.0D);
		final Vector3D h = Vector3D.normalize(g);
		final Vector3D i = Vector3D.normalize(h);
		final Vector3D j = new Vector3D(-1.0D, -1.0D, -1.0D);
		final Vector3D k = Vector3D.normalize(j);
		final Vector3D l = Vector3D.normalize(k);
		
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
		
		assertThrows(NullPointerException.class, () -> Vector3D.normalize(null));
	}
	
	@Test
	public void testOrthogonal() {
		final Vector3D a = new Vector3D(1.0D, 0.0D, 0.0D);
		final Vector3D b = new Vector3D(0.0D, 1.0D, 0.0D);
		final Vector3D c = new Vector3D(1.0D, 1.0D, 1.0D);
		
		assertTrue(Vector3D.orthogonal(a, b));
		
		assertFalse(Vector3D.orthogonal(a, c));
		
		assertThrows(NullPointerException.class, () -> Vector3D.orthogonal(a, null));
		assertThrows(NullPointerException.class, () -> Vector3D.orthogonal(null, b));
	}
	
	@Test
	public void testRandom() {
		final Vector3D vector = Vector3D.random();
		
		assertTrue(vector.x >= -1.0D && vector.x <= 1.0D);
		assertTrue(vector.y >= -1.0D && vector.y <= 1.0D);
		assertTrue(vector.z >= -1.0D && vector.z <= 1.0D);
	}
	
	@Test
	public void testRandomNormalized() {
		final Vector3D vector = Vector3D.randomNormalized();
		
		assertTrue(vector.x >= -1.0D && vector.x <= 1.0D);
		assertTrue(vector.y >= -1.0D && vector.y <= 1.0D);
		assertTrue(vector.z >= -1.0D && vector.z <= 1.0D);
		
		assertTrue(vector.isUnitVector());
	}
	
	@Test
	public void testRead() throws IOException {
		final Vector3D a = new Vector3D(1.0D, 2.0D, 3.0D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeDouble(a.x);
		dataOutput.writeDouble(a.y);
		dataOutput.writeDouble(a.z);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Vector3D b = Vector3D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Vector3D.read(null));
		assertThrows(UncheckedIOException.class, () -> Vector3D.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testReciprocal() {
		final Vector3D a = Vector3D.reciprocal(new Vector3D(Double.NaN, Double.NaN, Double.NaN));
		final Vector3D b = Vector3D.reciprocal(new Vector3D(2.0D, 4.0D, 8.0D));
		
		assertEquals(Double.NaN, a.x);
		assertEquals(Double.NaN, a.y);
		assertEquals(Double.NaN, a.z);
		
		assertEquals(0.500D, b.x);
		assertEquals(0.250D, b.y);
		assertEquals(0.125D, b.z);
		
		assertThrows(NullPointerException.class, () -> Vector3D.reciprocal(null));
	}
	
	@Test
	public void testRefraction() {
		final Optional<Vector3D> a = Vector3D.refraction(new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(0.0D, 1.0D, 0.0D), 1.0D);
		final Optional<Vector3D> b = Vector3D.refraction(new Vector3D(0.0D, 1.0D, 0.0D), new Vector3D(1.0D, 0.0D, 0.0D), 1.0D);
		
		assertNotNull(a);
		assertNotNull(b);
		
		assertTrue(a.isPresent());
		
		assertFalse(b.isPresent());
		
		assertEquals(+0.0D, a.get().x);
		assertEquals(-1.0D, a.get().y);
		assertEquals(+0.0D, a.get().z);
		
		assertThrows(NullPointerException.class, () -> Vector3D.refraction(new Vector3D(0.0D, 1.0D, 0.0D), null, 1.0D));
		assertThrows(NullPointerException.class, () -> Vector3D.refraction(null, new Vector3D(0.0D, 1.0D, 0.0D), 1.0D));
	}
	
	@Test
	public void testSameHemisphere() {
		final Vector3D a = new Vector3D(+1.0D, 0.0D, 0.0D);
		final Vector3D b = new Vector3D(-1.0D, 0.0D, 0.0D);
		
		assertTrue(Vector3D.sameHemisphere(a, a));
		
		assertFalse(Vector3D.sameHemisphere(a, b));
		
		assertThrows(NullPointerException.class, () -> Vector3D.sameHemisphere(a, null));
		assertThrows(NullPointerException.class, () -> Vector3D.sameHemisphere(null, b));
	}
	
	@Test
	public void testSameHemisphereZ() {
		final Vector3D a = new Vector3D(0.0D, 0.0D, +1.0D);
		final Vector3D b = new Vector3D(0.0D, 0.0D, -1.0D);
		
		assertTrue(Vector3D.sameHemisphereZ(a, a));
		
		assertFalse(Vector3D.sameHemisphereZ(a, b));
		
		assertThrows(NullPointerException.class, () -> Vector3D.sameHemisphereZ(a, null));
		assertThrows(NullPointerException.class, () -> Vector3D.sameHemisphereZ(null, b));
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
	public void testSubtract() {
		final Vector3D a = new Vector3D(3.0D, 5.0D, 7.0D);
		final Vector3D b = new Vector3D(2.0D, 3.0D, 4.0D);
		final Vector3D c = Vector3D.subtract(a, b);
		
		assertEquals(1.0D, c.x);
		assertEquals(2.0D, c.y);
		assertEquals(3.0D, c.z);
		
		assertThrows(NullPointerException.class, () -> Vector3D.subtract(a, null));
		assertThrows(NullPointerException.class, () -> Vector3D.subtract(null, b));
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
	public void testU() {
		final Vector3D vector = Vector3D.u();
		
		assertEquals(1.0D, vector.x);
		assertEquals(0.0D, vector.y);
		assertEquals(0.0D, vector.z);
	}
	
	@Test
	public void testUDouble() {
		final Vector3D vector = Vector3D.u(2.0D);
		
		assertEquals(2.0D, vector.x);
		assertEquals(0.0D, vector.y);
		assertEquals(0.0D, vector.z);
	}
	
	@Test
	public void testV() {
		final Vector3D vector = Vector3D.v();
		
		assertEquals(0.0D, vector.x);
		assertEquals(1.0D, vector.y);
		assertEquals(0.0D, vector.z);
	}
	
	@Test
	public void testVDouble() {
		final Vector3D vector = Vector3D.v(2.0D);
		
		assertEquals(0.0D, vector.x);
		assertEquals(2.0D, vector.y);
		assertEquals(0.0D, vector.z);
	}
	
	@Test
	public void testW() {
		final Vector3D vector = Vector3D.w();
		
		assertEquals(0.0D, vector.x);
		assertEquals(0.0D, vector.y);
		assertEquals(1.0D, vector.z);
	}
	
	@Test
	public void testWDouble() {
		final Vector3D vector = Vector3D.w(2.0D);
		
		assertEquals(0.0D, vector.x);
		assertEquals(0.0D, vector.y);
		assertEquals(2.0D, vector.z);
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
	
	@Test
	public void testX() {
		final Vector3D vector = Vector3D.x();
		
		assertEquals(1.0D, vector.x);
		assertEquals(0.0D, vector.y);
		assertEquals(0.0D, vector.z);
	}
	
	@Test
	public void testXDouble() {
		final Vector3D vector = Vector3D.x(2.0D);
		
		assertEquals(2.0D, vector.x);
		assertEquals(0.0D, vector.y);
		assertEquals(0.0D, vector.z);
	}
	
	@Test
	public void testY() {
		final Vector3D vector = Vector3D.y();
		
		assertEquals(0.0D, vector.x);
		assertEquals(1.0D, vector.y);
		assertEquals(0.0D, vector.z);
	}
	
	@Test
	public void testYDouble() {
		final Vector3D vector = Vector3D.y(2.0D);
		
		assertEquals(0.0D, vector.x);
		assertEquals(2.0D, vector.y);
		assertEquals(0.0D, vector.z);
	}
	
	@Test
	public void testZ() {
		final Vector3D vector = Vector3D.z();
		
		assertEquals(0.0D, vector.x);
		assertEquals(0.0D, vector.y);
		assertEquals(1.0D, vector.z);
	}
	
	@Test
	public void testZDouble() {
		final Vector3D vector = Vector3D.z(2.0D);
		
		assertEquals(0.0D, vector.x);
		assertEquals(0.0D, vector.y);
		assertEquals(2.0D, vector.z);
	}
}