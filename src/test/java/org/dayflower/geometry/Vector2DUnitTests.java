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

import org.dayflower.mock.DataOutputMock;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class Vector2DUnitTests {
	public Vector2DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAbsolute() {
		final Vector2D a = Vector2D.absolute(new Vector2D(+1.0D, +2.0D));
		final Vector2D b = Vector2D.absolute(new Vector2D(-1.0D, -2.0D));
		
		assertEquals(1.0D, a.x);
		assertEquals(2.0D, a.y);
		
		assertEquals(1.0D, b.x);
		assertEquals(2.0D, b.y);
		
		assertThrows(NullPointerException.class, () -> Vector2D.absolute(null));
	}
	
	@Test
	public void testAddVector2DVector2D() {
		final Vector2D a = new Vector2D(1.0D, 2.0D);
		final Vector2D b = new Vector2D(2.0D, 3.0D);
		final Vector2D c = Vector2D.add(a, b);
		
		assertEquals(3.0D, c.x);
		assertEquals(5.0D, c.y);
		
		assertThrows(NullPointerException.class, () -> Vector2D.add(a, null));
		assertThrows(NullPointerException.class, () -> Vector2D.add(null, b));
	}
	
	@Test
	public void testAddVector2DVector2DVector2D() {
		final Vector2D a = new Vector2D(1.0D, 2.0D);
		final Vector2D b = new Vector2D(2.0D, 3.0D);
		final Vector2D c = new Vector2D(3.0D, 4.0D);
		final Vector2D d = Vector2D.add(a, b, c);
		
		assertEquals(6.0D, d.x);
		assertEquals(9.0D, d.y);
		
		assertThrows(NullPointerException.class, () -> Vector2D.add(a, b, null));
		assertThrows(NullPointerException.class, () -> Vector2D.add(a, null, c));
		assertThrows(NullPointerException.class, () -> Vector2D.add(null, b, c));
	}
	
	@Test
	public void testClearCacheAndGetCacheSizeAndGetCached() {
		assertEquals(0, Vector2D.getCacheSize());
		
		final Vector2D a = new Vector2D(1.0D, 2.0D);
		final Vector2D b = new Vector2D(1.0D, 2.0D);
		final Vector2D c = Vector2D.getCached(a);
		final Vector2D d = Vector2D.getCached(b);
		
		assertThrows(NullPointerException.class, () -> Vector2D.getCached(null));
		
		assertEquals(1, Vector2D.getCacheSize());
		
		Vector2D.clearCache();
		
		assertEquals(0, Vector2D.getCacheSize());
		
		assertTrue(a != b);
		assertTrue(a == c);
		assertTrue(a == d);
		
		assertTrue(b != a);
		assertTrue(b != c);
		assertTrue(b != d);
	}
	
	@Test
	public void testConstants() {
		assertEquals(new Vector2D(Double.NaN, Double.NaN), Vector2D.NaN);
		assertEquals(new Vector2D(0.0D, 0.0D), Vector2D.ZERO);
	}
	
	@Test
	public void testConstructor() {
		final Vector2D vector = new Vector2D();
		
		assertEquals(0.0D, vector.x);
		assertEquals(0.0D, vector.y);
	}
	
	@Test
	public void testConstructorDouble() {
		final Vector2D vector = new Vector2D(1.0D);
		
		assertEquals(1.0D, vector.x);
		assertEquals(1.0D, vector.y);
	}
	
	@Test
	public void testConstructorDoubleDouble() {
		final Vector2D vector = new Vector2D(1.0D, 2.0D);
		
		assertEquals(1.0D, vector.x);
		assertEquals(2.0D, vector.y);
	}
	
	@Test
	public void testConstructorPoint2D() {
		final Vector2D vector = new Vector2D(new Point2D(1.0D, 2.0D));
		
		assertEquals(1.0D, vector.x);
		assertEquals(2.0D, vector.y);
		
		assertThrows(NullPointerException.class, () -> new Vector2D((Point2D)(null)));
	}
	
	@Test
	public void testConstructorPoint3D() {
		final Vector2D vector = new Vector2D(new Point3D(1.0D, 2.0D, 3.0D));
		
		assertEquals(1.0D, vector.x);
		assertEquals(2.0D, vector.y);
		
		assertThrows(NullPointerException.class, () -> new Vector2D((Point3D)(null)));
	}
	
	@Test
	public void testCrossProduct() {
		final Vector2D a = new Vector2D(+1.0D, +0.0D);
		final Vector2D b = new Vector2D(+0.0D, +1.0D);
		
		assertEquals(1.0D, Vector2D.crossProduct(a, b));
		
		assertThrows(NullPointerException.class, () -> Vector2D.crossProduct(a, null));
		assertThrows(NullPointerException.class, () -> Vector2D.crossProduct(null, b));
	}
	
	@Test
	public void testDirection() {
		final Vector2D vector = Vector2D.direction(new Point2D(1.0D, 2.0D), new Point2D(2.0D, 3.0D));
		
		assertEquals(1.0D, vector.x);
		assertEquals(1.0D, vector.y);
		
		assertThrows(NullPointerException.class, () -> Vector2D.direction(new Point2D(1.0D, 2.0D), null));
		assertThrows(NullPointerException.class, () -> Vector2D.direction(null, new Point2D(2.0D, 3.0D)));
	}
	
	@Test
	public void testDirectionNormalized() {
		final Vector2D vector = Vector2D.directionNormalized(new Point2D(1.0D, 2.0D), new Point2D(2.0D, 2.0D));
		
		assertEquals(1.0D, vector.x);
		assertEquals(0.0D, vector.y);
		
		assertTrue(vector.isUnitVector());
		
		assertThrows(NullPointerException.class, () -> Vector2D.directionNormalized(new Point2D(1.0D, 2.0D), null));
		assertThrows(NullPointerException.class, () -> Vector2D.directionNormalized(null, new Point2D(2.0D, 2.0D)));
	}
	
	@Test
	public void testDirectionXY() {
		final Vector2D vector = Vector2D.directionXY(new Point3D(1.0D, 2.0D, 3.0D));
		
		assertEquals(1.0D, vector.x);
		assertEquals(2.0D, vector.y);
		
		assertThrows(NullPointerException.class, () -> Vector2D.directionXY(null));
	}
	
	@Test
	public void testDirectionYZ() {
		final Vector2D vector = Vector2D.directionYZ(new Point3D(1.0D, 2.0D, 3.0D));
		
		assertEquals(2.0D, vector.x);
		assertEquals(3.0D, vector.y);
		
		assertThrows(NullPointerException.class, () -> Vector2D.directionYZ(null));
	}
	
	@Test
	public void testDirectionZX() {
		final Vector2D vector = Vector2D.directionZX(new Point3D(1.0D, 2.0D, 3.0D));
		
		assertEquals(3.0D, vector.x);
		assertEquals(1.0D, vector.y);
		
		assertThrows(NullPointerException.class, () -> Vector2D.directionZX(null));
	}
	
	@Test
	public void testDivide() {
		final Vector2D a = new Vector2D(2.0D, 4.0D);
		final Vector2D b = Vector2D.divide(a, 2.0D);
		final Vector2D c = Vector2D.divide(a, Double.NaN);
		
		assertEquals(1.0D, b.x);
		assertEquals(2.0D, b.y);
		
		assertEquals(0.0D, c.x);
		assertEquals(0.0D, c.y);
		
		assertThrows(NullPointerException.class, () -> Vector2D.divide(null, 2.0D));
	}
	
	@Test
	public void testDotProduct() {
		final Vector2D a = new Vector2D(+1.0D, +0.0D);
		final Vector2D b = new Vector2D(+1.0D, +0.0D);
		final Vector2D c = new Vector2D(+0.0D, -1.0D);
		final Vector2D d = new Vector2D(-1.0D, +0.0D);
		
		assertEquals(+1.0D, Vector2D.dotProduct(a, b));
		assertEquals(+0.0D, Vector2D.dotProduct(a, c));
		assertEquals(-1.0D, Vector2D.dotProduct(a, d));
		
		assertThrows(NullPointerException.class, () -> Vector2D.dotProduct(a, null));
		assertThrows(NullPointerException.class, () -> Vector2D.dotProduct(null, b));
	}
	
	@Test
	public void testEquals() {
		final Vector2D a = new Vector2D(0.0D, 1.0D);
		final Vector2D b = new Vector2D(0.0D, 1.0D);
		final Vector2D c = new Vector2D(0.0D, 2.0D);
		final Vector2D d = new Vector2D(2.0D, 1.0D);
		final Vector2D e = null;
		
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
	public void testHashCode() {
		final Vector2D a = new Vector2D(0.5D, 1.0D);
		final Vector2D b = new Vector2D(0.5D, 1.0D);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIsUnitVector() {
		assertTrue(new Vector2D(+1.0D, +0.0D).isUnitVector());
		assertTrue(new Vector2D(+0.0D, +1.0D).isUnitVector());
		assertTrue(new Vector2D(-1.0D, -0.0D).isUnitVector());
		assertTrue(new Vector2D(-0.0D, -1.0D).isUnitVector());
		assertTrue(Vector2D.normalize(new Vector2D(1.0D, 1.0D)).isUnitVector());
		
		assertFalse(new Vector2D(+1.0D, +1.0D).isUnitVector());
		assertFalse(new Vector2D(+0.5D, +0.5D).isUnitVector());
		assertFalse(new Vector2D(-1.0D, -1.0D).isUnitVector());
		assertFalse(new Vector2D(-0.5D, -0.5D).isUnitVector());
	}
	
	@Test
	public void testLength() {
		final Vector2D a = new Vector2D(4.0D, 0.0D);
		final Vector2D b = new Vector2D(0.0D, 4.0D);
		
		assertEquals(4.0D, a.length());
		assertEquals(4.0D, b.length());
	}
	
	@Test
	public void testLengthSquared() {
		final Vector2D vector = new Vector2D(2.0D, 4.0D);
		
		assertEquals(20.0D, vector.lengthSquared());
	}
	
	@Test
	public void testLerp() {
		final Vector2D a = new Vector2D(1.0D, 1.0D);
		final Vector2D b = new Vector2D(5.0D, 5.0D);
		final Vector2D c = Vector2D.lerp(a, b, 0.25D);
		
		assertEquals(2.0D, c.x);
		assertEquals(2.0D, c.y);
		
		assertThrows(NullPointerException.class, () -> Vector2D.lerp(a, null, 0.25D));
		assertThrows(NullPointerException.class, () -> Vector2D.lerp(null, b, 0.25D));
	}
	
	@Test
	public void testMultiply() {
		final Vector2D a = new Vector2D(1.0D, 2.0D);
		final Vector2D b = Vector2D.multiply(a, 2.0D);
		
		assertEquals(2.0D, b.x);
		assertEquals(4.0D, b.y);
		
		assertThrows(NullPointerException.class, () -> Vector2D.multiply(null, 2.0D));
	}
	
	@Test
	public void testNegate() {
		final Vector2D a = new Vector2D(1.0D, 2.0D);
		final Vector2D b = Vector2D.negate(a);
		final Vector2D c = Vector2D.negate(b);
		
		assertEquals(-1.0D, b.x);
		assertEquals(-2.0D, b.y);
		
		assertEquals(+1.0D, c.x);
		assertEquals(+2.0D, c.y);
		
		assertThrows(NullPointerException.class, () -> Vector2D.negate(null));
	}
	
	@Test
	public void testNormalize() {
		final Vector2D a = new Vector2D(+1.0D, +0.0D);
		final Vector2D b = Vector2D.normalize(a);
		final Vector2D c = new Vector2D(+0.0D, +1.0D);
		final Vector2D d = Vector2D.normalize(c);
		final Vector2D e = new Vector2D(+1.0D, +1.0D);
		final Vector2D f = Vector2D.normalize(e);
		final Vector2D g = Vector2D.normalize(f);
		final Vector2D h = new Vector2D(-1.0D, -1.0D);
		final Vector2D i = Vector2D.normalize(h);
		final Vector2D j = Vector2D.normalize(i);
		
		assertEquals(a, b);
		assertEquals(c, d);
		assertEquals(f, g);
		assertEquals(i, j);
		
		assertTrue(a == b);
		assertTrue(c == d);
		assertTrue(f == g);
		assertTrue(i == j);
		
		assertTrue(a.isUnitVector());
		assertTrue(c.isUnitVector());
		assertTrue(f.isUnitVector());
		assertTrue(i.isUnitVector());
		
		assertFalse(e.isUnitVector());
		assertFalse(h.isUnitVector());
		
		assertThrows(NullPointerException.class, () -> Vector2D.normalize(null));
	}
	
	@Test
	public void testOrthogonal() {
		final Vector2D a = new Vector2D(1.0D, 0.0D);
		final Vector2D b = new Vector2D(0.0D, 1.0D);
		final Vector2D c = new Vector2D(1.0D, 1.0D);
		
		assertTrue(Vector2D.orthogonal(a, b));
		
		assertFalse(Vector2D.orthogonal(a, c));
		
		assertThrows(NullPointerException.class, () -> Vector2D.orthogonal(a, null));
		assertThrows(NullPointerException.class, () -> Vector2D.orthogonal(null, b));
	}
	
	@Test
	public void testPerpendicular() {
		final Vector2D a = new Vector2D(1.0D, 2.0D);
		final Vector2D b = Vector2D.perpendicular(a);
		
		assertEquals(+2.0D, b.x);
		assertEquals(-1.0D, b.y);
		
		assertThrows(NullPointerException.class, () -> Vector2D.perpendicular(null));
	}
	
	@Test
	public void testRandom() {
		final Vector2D vector = Vector2D.random();
		
		assertTrue(vector.x >= -1.0D && vector.x <= 1.0D);
		assertTrue(vector.y >= -1.0D && vector.y <= 1.0D);
	}
	
	@Test
	public void testRandomNormalized() {
		final Vector2D vector = Vector2D.randomNormalized();
		
		assertTrue(vector.x >= -1.0D && vector.x <= 1.0D);
		assertTrue(vector.y >= -1.0D && vector.y <= 1.0D);
		
		assertTrue(vector.isUnitVector());
	}
	
	@Test
	public void testRead() throws IOException {
		final Vector2D a = new Vector2D(1.0D, 0.5D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeDouble(a.x);
		dataOutput.writeDouble(a.y);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Vector2D b = Vector2D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Vector2D.read(null));
		assertThrows(UncheckedIOException.class, () -> Vector2D.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testReciprocal() {
		final Vector2D a = Vector2D.reciprocal(new Vector2D(Double.NaN, Double.NaN));
		final Vector2D b = Vector2D.reciprocal(new Vector2D(2.0D, 4.0D));
		
		assertEquals(Double.NaN, a.x);
		assertEquals(Double.NaN, a.y);
		
		assertEquals(0.50D, b.x);
		assertEquals(0.25D, b.y);
		
		assertThrows(NullPointerException.class, () -> Vector2D.reciprocal(null));
	}
	
	@Test
	public void testSubtract() {
		final Vector2D a = new Vector2D(3.0D, 5.0D);
		final Vector2D b = new Vector2D(2.0D, 3.0D);
		final Vector2D c = Vector2D.subtract(a, b);
		
		assertEquals(1.0D, c.x);
		assertEquals(2.0D, c.y);
		
		assertThrows(NullPointerException.class, () -> Vector2D.subtract(a, null));
		assertThrows(NullPointerException.class, () -> Vector2D.subtract(null, b));
	}
	
	@Test
	public void testToArray() {
		final Vector2D vector = new Vector2D(1.0D, 2.0D);
		
		final double[] array = vector.toArray();
		
		assertNotNull(array);
		
		assertEquals(2, array.length);
		
		assertEquals(1.0D, array[0]);
		assertEquals(2.0D, array[1]);
	}
	
	@Test
	public void testToString() {
		final Vector2D vector = new Vector2D(0.5D, 1.0D);
		
		assertEquals("new Vector2D(0.5D, 1.0D)", vector.toString());
	}
	
	@Test
	public void testTransform() {
		final Matrix33D matrix = Matrix33D.scale(2.0D, 4.0D);
		
		final Vector2D a = new Vector2D(1.0D, 1.0D);
		final Vector2D b = Vector2D.transform(matrix, a);
		
		assertEquals(2.0D, b.x);
		assertEquals(4.0D, b.y);
		
		assertThrows(NullPointerException.class, () -> Vector2D.transform(matrix, null));
		assertThrows(NullPointerException.class, () -> Vector2D.transform(null, a));
	}
	
	@Test
	public void testTransformTranspose() {
		final Matrix33D matrix = Matrix33D.transpose(Matrix33D.rotate(AngleD.degrees(180.0D)));
		
		final Vector2D a = new Vector2D(1.0D, 0.0D);
		final Vector2D b = Vector2D.transformTranspose(matrix, a);
		
		assertEquals(-1.00000000000000000000000000000000D, b.x);
		assertEquals(-0.00000000000000012246467991473532D, b.y);
		
		assertThrows(NullPointerException.class, () -> Vector2D.transformTranspose(matrix, null));
		assertThrows(NullPointerException.class, () -> Vector2D.transformTranspose(null, a));
	}
	
	@Test
	public void testWrite() {
		final Vector2D a = new Vector2D(1.0D, 0.5D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Vector2D b = Vector2D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
	
	@Test
	public void testX() {
		final Vector2D vector = Vector2D.x();
		
		assertEquals(1.0D, vector.x);
		assertEquals(0.0D, vector.y);
	}
	
	@Test
	public void testXDouble() {
		final Vector2D vector = Vector2D.x(2.0D);
		
		assertEquals(2.0D, vector.x);
		assertEquals(0.0D, vector.y);
	}
	
	@Test
	public void testY() {
		final Vector2D vector = Vector2D.y();
		
		assertEquals(0.0D, vector.x);
		assertEquals(1.0D, vector.y);
	}
	
	@Test
	public void testYDouble() {
		final Vector2D vector = Vector2D.y(2.0D);
		
		assertEquals(0.0D, vector.x);
		assertEquals(2.0D, vector.y);
	}
}