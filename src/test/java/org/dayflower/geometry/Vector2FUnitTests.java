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
public final class Vector2FUnitTests {
	public Vector2FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAbsolute() {
		final Vector2F a = Vector2F.absolute(new Vector2F(+1.0F, +2.0F));
		final Vector2F b = Vector2F.absolute(new Vector2F(-1.0F, -2.0F));
		
		assertEquals(1.0F, a.getComponent1());
		assertEquals(2.0F, a.getComponent2());
		
		assertEquals(1.0F, b.getComponent1());
		assertEquals(2.0F, b.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2F.absolute(null));
	}
	
	@Test
	public void testAddVector2FVector2F() {
		final Vector2F a = new Vector2F(1.0F, 2.0F);
		final Vector2F b = new Vector2F(2.0F, 3.0F);
		final Vector2F c = Vector2F.add(a, b);
		
		assertEquals(3.0F, c.getComponent1());
		assertEquals(5.0F, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2F.add(a, null));
		assertThrows(NullPointerException.class, () -> Vector2F.add(null, b));
	}
	
	@Test
	public void testAddVector2FVector2FVector2F() {
		final Vector2F a = new Vector2F(1.0F, 2.0F);
		final Vector2F b = new Vector2F(2.0F, 3.0F);
		final Vector2F c = new Vector2F(3.0F, 4.0F);
		final Vector2F d = Vector2F.add(a, b, c);
		
		assertEquals(6.0F, d.getComponent1());
		assertEquals(9.0F, d.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2F.add(a, b, null));
		assertThrows(NullPointerException.class, () -> Vector2F.add(a, null, c));
		assertThrows(NullPointerException.class, () -> Vector2F.add(null, b, c));
	}
	
	@Test
	public void testClearCacheAndGetCacheSizeAndGetCached() {
		assertEquals(0, Vector2F.getCacheSize());
		
		final Vector2F a = new Vector2F(1.0F, 2.0F);
		final Vector2F b = new Vector2F(1.0F, 2.0F);
		final Vector2F c = Vector2F.getCached(a);
		final Vector2F d = Vector2F.getCached(b);
		
		assertThrows(NullPointerException.class, () -> Vector2F.getCached(null));
		
		assertEquals(1, Vector2F.getCacheSize());
		
		Vector2F.clearCache();
		
		assertEquals(0, Vector2F.getCacheSize());
		
		assertTrue(a != b);
		assertTrue(a == c);
		assertTrue(a == d);
		
		assertTrue(b != a);
		assertTrue(b != c);
		assertTrue(b != d);
	}
	
	@Test
	public void testConstants() {
		assertEquals(new Vector2F(Float.NaN, Float.NaN), Vector2F.NaN);
		assertEquals(new Vector2F(0.0F, 0.0F), Vector2F.ZERO);
	}
	
	@Test
	public void testConstructor() {
		final Vector2F vector = new Vector2F();
		
		assertEquals(0.0F, vector.getComponent1());
		assertEquals(0.0F, vector.getComponent2());
	}
	
	@Test
	public void testConstructorFloat() {
		final Vector2F vector = new Vector2F(1.0F);
		
		assertEquals(1.0F, vector.getComponent1());
		assertEquals(1.0F, vector.getComponent2());
	}
	
	@Test
	public void testConstructorFloatFloat() {
		final Vector2F vector = new Vector2F(1.0F, 2.0F);
		
		assertEquals(1.0F, vector.getComponent1());
		assertEquals(2.0F, vector.getComponent2());
	}
	
	@Test
	public void testConstructorPoint2F() {
		final Vector2F vector = new Vector2F(new Point2F(1.0F, 2.0F));
		
		assertEquals(1.0F, vector.getComponent1());
		assertEquals(2.0F, vector.getComponent2());
		
		assertThrows(NullPointerException.class, () -> new Vector2F((Point2F)(null)));
	}
	
	@Test
	public void testConstructorPoint3F() {
		final Vector2F vector = new Vector2F(new Point3F(1.0F, 2.0F, 3.0F));
		
		assertEquals(1.0F, vector.getComponent1());
		assertEquals(2.0F, vector.getComponent2());
		
		assertThrows(NullPointerException.class, () -> new Vector2F((Point3F)(null)));
	}
	
	@Test
	public void testCrossProduct() {
		final Vector2F a = new Vector2F(+1.0F, +0.0F);
		final Vector2F b = new Vector2F(+0.0F, +1.0F);
		
		assertEquals(1.0F, Vector2F.crossProduct(a, b));
		
		assertThrows(NullPointerException.class, () -> Vector2F.crossProduct(a, null));
		assertThrows(NullPointerException.class, () -> Vector2F.crossProduct(null, b));
	}
	
	@Test
	public void testDirection() {
		final Vector2F vector = Vector2F.direction(new Point2F(1.0F, 2.0F), new Point2F(2.0F, 3.0F));
		
		assertEquals(1.0F, vector.getComponent1());
		assertEquals(1.0F, vector.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2F.direction(new Point2F(1.0F, 2.0F), null));
		assertThrows(NullPointerException.class, () -> Vector2F.direction(null, new Point2F(2.0F, 3.0F)));
	}
	
	@Test
	public void testDirectionNormalized() {
		final Vector2F vector = Vector2F.directionNormalized(new Point2F(1.0F, 2.0F), new Point2F(2.0F, 2.0F));
		
		assertEquals(1.0F, vector.getComponent1());
		assertEquals(0.0F, vector.getComponent2());
		
		assertTrue(vector.isUnitVector());
		
		assertThrows(NullPointerException.class, () -> Vector2F.directionNormalized(new Point2F(1.0F, 2.0F), null));
		assertThrows(NullPointerException.class, () -> Vector2F.directionNormalized(null, new Point2F(2.0F, 2.0F)));
	}
	
	@Test
	public void testDirectionXY() {
		final Vector2F vector = Vector2F.directionXY(new Point3F(1.0F, 2.0F, 3.0F));
		
		assertEquals(1.0F, vector.getComponent1());
		assertEquals(2.0F, vector.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2F.directionXY(null));
	}
	
	@Test
	public void testDirectionYZ() {
		final Vector2F vector = Vector2F.directionYZ(new Point3F(1.0F, 2.0F, 3.0F));
		
		assertEquals(2.0F, vector.getComponent1());
		assertEquals(3.0F, vector.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2F.directionYZ(null));
	}
	
	@Test
	public void testDirectionZX() {
		final Vector2F vector = Vector2F.directionZX(new Point3F(1.0F, 2.0F, 3.0F));
		
		assertEquals(3.0F, vector.getComponent1());
		assertEquals(1.0F, vector.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2F.directionZX(null));
	}
	
	@Test
	public void testDivide() {
		final Vector2F a = new Vector2F(2.0F, 4.0F);
		final Vector2F b = Vector2F.divide(a, 2.0F);
		final Vector2F c = Vector2F.divide(a, Float.NaN);
		
		assertEquals(1.0F, b.getComponent1());
		assertEquals(2.0F, b.getComponent2());
		
		assertEquals(0.0F, c.getComponent1());
		assertEquals(0.0F, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2F.divide(null, 2.0F));
	}
	
	@Test
	public void testDotProduct() {
		final Vector2F a = new Vector2F(+1.0F, +0.0F);
		final Vector2F b = new Vector2F(+1.0F, +0.0F);
		final Vector2F c = new Vector2F(+0.0F, -1.0F);
		final Vector2F d = new Vector2F(-1.0F, +0.0F);
		
		assertEquals(+1.0F, Vector2F.dotProduct(a, b));
		assertEquals(+0.0F, Vector2F.dotProduct(a, c));
		assertEquals(-1.0F, Vector2F.dotProduct(a, d));
		
		assertThrows(NullPointerException.class, () -> Vector2F.dotProduct(a, null));
		assertThrows(NullPointerException.class, () -> Vector2F.dotProduct(null, b));
	}
	
	@Test
	public void testEquals() {
		final Vector2F a = new Vector2F(0.0F, 1.0F);
		final Vector2F b = new Vector2F(0.0F, 1.0F);
		final Vector2F c = new Vector2F(0.0F, 2.0F);
		final Vector2F d = new Vector2F(2.0F, 1.0F);
		final Vector2F e = null;
		
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
		final Vector2F vector = new Vector2F(2.0F, 0.0F);
		
		assertEquals(2.0F, vector.getComponent1());
	}
	
	@Test
	public void testGetComponent2() {
		final Vector2F vector = new Vector2F(0.0F, 2.0F);
		
		assertEquals(2.0F, vector.getComponent2());
	}
	
	@Test
	public void testGetU() {
		final Vector2F vector = new Vector2F(2.0F, 0.0F);
		
		assertEquals(2.0F, vector.getU());
	}
	
	@Test
	public void testGetV() {
		final Vector2F vector = new Vector2F(0.0F, 2.0F);
		
		assertEquals(2.0F, vector.getV());
	}
	
	@Test
	public void testGetX() {
		final Vector2F vector = new Vector2F(2.0F, 0.0F);
		
		assertEquals(2.0F, vector.getX());
	}
	
	@Test
	public void testGetY() {
		final Vector2F vector = new Vector2F(0.0F, 2.0F);
		
		assertEquals(2.0F, vector.getY());
	}
	
	@Test
	public void testHashCode() {
		final Vector2F a = new Vector2F(0.5F, 1.0F);
		final Vector2F b = new Vector2F(0.5F, 1.0F);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIsUnitVector() {
		assertTrue(new Vector2F(+1.0F, +0.0F).isUnitVector());
		assertTrue(new Vector2F(+0.0F, +1.0F).isUnitVector());
		assertTrue(new Vector2F(-1.0F, -0.0F).isUnitVector());
		assertTrue(new Vector2F(-0.0F, -1.0F).isUnitVector());
		assertTrue(Vector2F.normalize(new Vector2F(1.0F, 1.0F)).isUnitVector());
		
		assertFalse(new Vector2F(+1.0F, +1.0F).isUnitVector());
		assertFalse(new Vector2F(+0.5F, +0.5F).isUnitVector());
		assertFalse(new Vector2F(-1.0F, -1.0F).isUnitVector());
		assertFalse(new Vector2F(-0.5F, -0.5F).isUnitVector());
	}
	
	@Test
	public void testLength() {
		final Vector2F a = new Vector2F(4.0F, 0.0F);
		final Vector2F b = new Vector2F(0.0F, 4.0F);
		
		assertEquals(4.0F, a.length());
		assertEquals(4.0F, b.length());
	}
	
	@Test
	public void testLengthSquared() {
		final Vector2F vector = new Vector2F(2.0F, 4.0F);
		
		assertEquals(20.0F, vector.lengthSquared());
	}
	
	@Test
	public void testLerp() {
		final Vector2F a = new Vector2F(1.0F, 1.0F);
		final Vector2F b = new Vector2F(5.0F, 5.0F);
		final Vector2F c = Vector2F.lerp(a, b, 0.25F);
		
		assertEquals(2.0F, c.getComponent1());
		assertEquals(2.0F, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2F.lerp(a, null, 0.25F));
		assertThrows(NullPointerException.class, () -> Vector2F.lerp(null, b, 0.25F));
	}
	
	@Test
	public void testMultiply() {
		final Vector2F a = new Vector2F(1.0F, 2.0F);
		final Vector2F b = Vector2F.multiply(a, 2.0F);
		
		assertEquals(2.0F, b.getComponent1());
		assertEquals(4.0F, b.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2F.multiply(null, 2.0F));
	}
	
	@Test
	public void testNegate() {
		final Vector2F a = new Vector2F(1.0F, 2.0F);
		final Vector2F b = Vector2F.negate(a);
		final Vector2F c = Vector2F.negate(b);
		
		assertEquals(-1.0F, b.getComponent1());
		assertEquals(-2.0F, b.getComponent2());
		
		assertEquals(+1.0F, c.getComponent1());
		assertEquals(+2.0F, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2F.negate(null));
	}
	
	@Test
	public void testNegateComponent1() {
		final Vector2F a = new Vector2F(1.0F, 2.0F);
		final Vector2F b = Vector2F.negateComponent1(a);
		final Vector2F c = Vector2F.negateComponent1(b);
		
		assertEquals(-1.0F, b.getComponent1());
		assertEquals(+2.0F, b.getComponent2());
		
		assertEquals(+1.0F, c.getComponent1());
		assertEquals(+2.0F, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2F.negateComponent1(null));
	}
	
	@Test
	public void testNegateComponent2() {
		final Vector2F a = new Vector2F(1.0F, 2.0F);
		final Vector2F b = Vector2F.negateComponent2(a);
		final Vector2F c = Vector2F.negateComponent2(b);
		
		assertEquals(+1.0F, b.getComponent1());
		assertEquals(-2.0F, b.getComponent2());
		
		assertEquals(+1.0F, c.getComponent1());
		assertEquals(+2.0F, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2F.negateComponent2(null));
	}
	
	@Test
	public void testNormalize() {
		final Vector2F a = new Vector2F(+1.0F, +0.0F);
		final Vector2F b = Vector2F.normalize(a);
		final Vector2F c = new Vector2F(+0.0F, +1.0F);
		final Vector2F d = Vector2F.normalize(c);
		final Vector2F e = new Vector2F(+1.0F, +1.0F);
		final Vector2F f = Vector2F.normalize(e);
		final Vector2F g = Vector2F.normalize(f);
		final Vector2F h = new Vector2F(-1.0F, -1.0F);
		final Vector2F i = Vector2F.normalize(h);
		final Vector2F j = Vector2F.normalize(i);
		
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
		
		assertThrows(NullPointerException.class, () -> Vector2F.normalize(null));
	}
	
	@Test
	public void testOrthogonal() {
		final Vector2F a = new Vector2F(1.0F, 0.0F);
		final Vector2F b = new Vector2F(0.0F, 1.0F);
		final Vector2F c = new Vector2F(1.0F, 1.0F);
		
		assertTrue(Vector2F.orthogonal(a, b));
		
		assertFalse(Vector2F.orthogonal(a, c));
		
		assertThrows(NullPointerException.class, () -> Vector2F.orthogonal(a, null));
		assertThrows(NullPointerException.class, () -> Vector2F.orthogonal(null, b));
	}
	
	@Test
	public void testPerpendicular() {
		final Vector2F a = new Vector2F(1.0F, 2.0F);
		final Vector2F b = Vector2F.perpendicular(a);
		
		assertEquals(+2.0F, b.getComponent1());
		assertEquals(-1.0F, b.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2F.perpendicular(null));
	}
	
	@Test
	public void testRandom() {
		final Vector2F vector = Vector2F.random();
		
		assertTrue(vector.getComponent1() >= -1.0F && vector.getComponent1() <= 1.0F);
		assertTrue(vector.getComponent2() >= -1.0F && vector.getComponent2() <= 1.0F);
	}
	
	@Test
	public void testRandomNormalized() {
		final Vector2F vector = Vector2F.randomNormalized();
		
		assertTrue(vector.getComponent1() >= -1.0F && vector.getComponent1() <= 1.0F);
		assertTrue(vector.getComponent2() >= -1.0F && vector.getComponent2() <= 1.0F);
		
		assertTrue(vector.isUnitVector());
	}
	
	@Test
	public void testRead() throws IOException {
		final Vector2F a = new Vector2F(1.0F, 0.5F);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeFloat(a.getComponent1());
		dataOutput.writeFloat(a.getComponent2());
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Vector2F b = Vector2F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Vector2F.read(null));
		assertThrows(UncheckedIOException.class, () -> Vector2F.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testReciprocal() {
		final Vector2F a = Vector2F.reciprocal(new Vector2F(Float.NaN, Float.NaN));
		final Vector2F b = Vector2F.reciprocal(new Vector2F(2.0F, 4.0F));
		
		assertEquals(0.0F, a.getComponent1());
		assertEquals(0.0F, a.getComponent2());
		
		assertEquals(0.50F, b.getComponent1());
		assertEquals(0.25F, b.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2F.reciprocal(null));
	}
	
	@Test
	public void testSubtract() {
		final Vector2F a = new Vector2F(3.0F, 5.0F);
		final Vector2F b = new Vector2F(2.0F, 3.0F);
		final Vector2F c = Vector2F.subtract(a, b);
		
		assertEquals(1.0F, c.getComponent1());
		assertEquals(2.0F, c.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2F.subtract(a, null));
		assertThrows(NullPointerException.class, () -> Vector2F.subtract(null, b));
	}
	
	@Test
	public void testToArray() {
		final Vector2F vector = new Vector2F(1.0F, 2.0F);
		
		final float[] array = vector.toArray();
		
		assertNotNull(array);
		
		assertEquals(2, array.length);
		
		assertEquals(1.0F, array[0]);
		assertEquals(2.0F, array[1]);
	}
	
	@Test
	public void testToString() {
		final Vector2F vector = new Vector2F(0.5F, 1.0F);
		
		assertEquals("new Vector2F(0.5F, 1.0F)", vector.toString());
	}
	
	@Test
	public void testTransform() {
		final Matrix33F matrix = Matrix33F.scale(2.0F, 4.0F);
		
		final Vector2F a = new Vector2F(1.0F, 1.0F);
		final Vector2F b = Vector2F.transform(matrix, a);
		
		assertEquals(2.0F, b.getComponent1());
		assertEquals(4.0F, b.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2F.transform(matrix, null));
		assertThrows(NullPointerException.class, () -> Vector2F.transform(null, a));
	}
	
	@Test
	public void testTransformTranspose() {
		final Matrix33F matrix = Matrix33F.transpose(Matrix33F.rotate(AngleF.degrees(180.0F)));
		
		final Vector2F a = new Vector2F(1.0F, 0.0F);
		final Vector2F b = Vector2F.transformTranspose(matrix, a);
		
		assertEquals(-1.0000000000000000F, b.getComponent1());
		assertEquals(+0.0000000874227766F, b.getComponent2());
		
		assertThrows(NullPointerException.class, () -> Vector2F.transformTranspose(matrix, null));
		assertThrows(NullPointerException.class, () -> Vector2F.transformTranspose(null, a));
	}
	
	@Test
	public void testU() {
		final Vector2F vector = Vector2F.u();
		
		assertEquals(1.0F, vector.getU());
		assertEquals(0.0F, vector.getV());
	}
	
	@Test
	public void testUFloat() {
		final Vector2F vector = Vector2F.u(2.0F);
		
		assertEquals(2.0F, vector.getU());
		assertEquals(0.0F, vector.getV());
	}
	
	@Test
	public void testV() {
		final Vector2F vector = Vector2F.v();
		
		assertEquals(0.0F, vector.getU());
		assertEquals(1.0F, vector.getV());
	}
	
	@Test
	public void testVFloat() {
		final Vector2F vector = Vector2F.v(2.0F);
		
		assertEquals(0.0F, vector.getU());
		assertEquals(2.0F, vector.getV());
	}
	
	@Test
	public void testWrite() {
		final Vector2F a = new Vector2F(1.0F, 0.5F);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Vector2F b = Vector2F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
	
	@Test
	public void testX() {
		final Vector2F vector = Vector2F.x();
		
		assertEquals(1.0F, vector.getX());
		assertEquals(0.0F, vector.getY());
	}
	
	@Test
	public void testXFloat() {
		final Vector2F vector = Vector2F.x(2.0F);
		
		assertEquals(2.0F, vector.getX());
		assertEquals(0.0F, vector.getY());
	}
	
	@Test
	public void testY() {
		final Vector2F vector = Vector2F.y();
		
		assertEquals(0.0F, vector.getX());
		assertEquals(1.0F, vector.getY());
	}
	
	@Test
	public void testYFloat() {
		final Vector2F vector = Vector2F.y(2.0F);
		
		assertEquals(0.0F, vector.getX());
		assertEquals(2.0F, vector.getY());
	}
}