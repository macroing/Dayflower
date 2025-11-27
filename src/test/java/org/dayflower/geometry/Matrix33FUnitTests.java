/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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
public final class Matrix33FUnitTests {
	public Matrix33FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstructor() {
		final Matrix33F matrix = new Matrix33F();
		
		assertEquals(1.0F, matrix.element11);
		assertEquals(0.0F, matrix.element12);
		assertEquals(0.0F, matrix.element13);
		assertEquals(0.0F, matrix.element21);
		assertEquals(1.0F, matrix.element22);
		assertEquals(0.0F, matrix.element23);
		assertEquals(0.0F, matrix.element31);
		assertEquals(0.0F, matrix.element32);
		assertEquals(1.0F, matrix.element33);
	}
	
	@Test
	public void testConstructorFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
		final Matrix33F matrix = new Matrix33F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F);
		
		assertEquals(1.0F, matrix.element11);
		assertEquals(2.0F, matrix.element12);
		assertEquals(3.0F, matrix.element13);
		assertEquals(4.0F, matrix.element21);
		assertEquals(5.0F, matrix.element22);
		assertEquals(6.0F, matrix.element23);
		assertEquals(7.0F, matrix.element31);
		assertEquals(8.0F, matrix.element32);
		assertEquals(9.0F, matrix.element33);
	}
	
	@Test
	public void testDeterminant() {
		final Matrix33F matrix = new Matrix33F(6.0F, 1.0F, 1.0F, 4.0F, -2.0F, 5.0F, 2.0F, 8.0F, 7.0F);
		
		assertEquals(-306.0F, matrix.determinant());
	}
	
	@Test
	public void testEquals() {
		final Matrix33F a = new Matrix33F(0.0F, 1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F);
		final Matrix33F b = new Matrix33F(0.0F, 1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F);
		final Matrix33F c = new Matrix33F(0.0F, 1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 9.0F);
		final Matrix33F d = new Matrix33F(0.0F, 1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 9.0F, 8.0F);
		final Matrix33F e = new Matrix33F(0.0F, 1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 9.0F, 7.0F, 8.0F);
		final Matrix33F f = new Matrix33F(0.0F, 1.0F, 2.0F, 3.0F, 4.0F, 9.0F, 6.0F, 7.0F, 8.0F);
		final Matrix33F g = new Matrix33F(0.0F, 1.0F, 2.0F, 3.0F, 9.0F, 5.0F, 6.0F, 7.0F, 8.0F);
		final Matrix33F h = new Matrix33F(0.0F, 1.0F, 2.0F, 9.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F);
		final Matrix33F i = new Matrix33F(0.0F, 1.0F, 9.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F);
		final Matrix33F j = new Matrix33F(0.0F, 9.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F);
		final Matrix33F k = new Matrix33F(9.0F, 1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F);
		final Matrix33F l = null;
		
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
		assertNotEquals(a, h);
		assertNotEquals(h, a);
		assertNotEquals(a, i);
		assertNotEquals(i, a);
		assertNotEquals(a, j);
		assertNotEquals(j, a);
		assertNotEquals(a, k);
		assertNotEquals(k, a);
		assertNotEquals(a, l);
		assertNotEquals(l, a);
	}
	
	@Test
	public void testGetElementInt() {
		final Matrix33F matrix = new Matrix33F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F);
		
		assertEquals(1.0F, matrix.getElement(0));
		assertEquals(2.0F, matrix.getElement(1));
		assertEquals(3.0F, matrix.getElement(2));
		assertEquals(4.0F, matrix.getElement(3));
		assertEquals(5.0F, matrix.getElement(4));
		assertEquals(6.0F, matrix.getElement(5));
		assertEquals(7.0F, matrix.getElement(6));
		assertEquals(8.0F, matrix.getElement(7));
		assertEquals(9.0F, matrix.getElement(8));
		
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(-1));
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(+9));
	}
	
	@Test
	public void testGetElementIntInt() {
		final Matrix33F matrix = new Matrix33F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F);
		
		assertEquals(1.0F, matrix.getElement(1, 1));
		assertEquals(2.0F, matrix.getElement(1, 2));
		assertEquals(3.0F, matrix.getElement(1, 3));
		assertEquals(4.0F, matrix.getElement(2, 1));
		assertEquals(5.0F, matrix.getElement(2, 2));
		assertEquals(6.0F, matrix.getElement(2, 3));
		assertEquals(7.0F, matrix.getElement(3, 1));
		assertEquals(8.0F, matrix.getElement(3, 2));
		assertEquals(9.0F, matrix.getElement(3, 3));
		
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(0, 1));
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(1, 0));
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(4, 1));
		assertThrows(IllegalArgumentException.class, () -> matrix.getElement(1, 4));
	}
	
	@Test
	public void testHashCode() {
		final Matrix33F a = new Matrix33F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F);
		final Matrix33F b = new Matrix33F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testIdentity() {
		final Matrix33F matrix = Matrix33F.identity();
		
		assertEquals(1.0F, matrix.element11);
		assertEquals(0.0F, matrix.element12);
		assertEquals(0.0F, matrix.element13);
		assertEquals(0.0F, matrix.element21);
		assertEquals(1.0F, matrix.element22);
		assertEquals(0.0F, matrix.element23);
		assertEquals(0.0F, matrix.element31);
		assertEquals(0.0F, matrix.element32);
		assertEquals(1.0F, matrix.element33);
	}
	
	@Test
	public void testInverse() {
		final Matrix33F a = new Matrix33F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 4.0F, 3.0F, 2.0F, 1.0F);
		final Matrix33F b = Matrix33F.inverse(a);
		final Matrix33F c = Matrix33F.inverse(b);
		final Matrix33F d = Matrix33F.multiply(a, b);
		final Matrix33F e = Matrix33F.identity();
		
		assertEquals(a, c);
		assertEquals(d, e);
		
		assertThrows(IllegalArgumentException.class, () -> Matrix33F.inverse(new Matrix33F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F)));
		assertThrows(NullPointerException.class, () -> Matrix33F.inverse(null));
	}
	
	@Test
	public void testIsInvertible() {
		final Matrix33F a = new Matrix33F(1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F);
		final Matrix33F b = new Matrix33F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F);
		
		assertTrue(a.isInvertible());
		assertFalse(b.isInvertible());
	}
	
	@Test
	public void testMultiply() {
		final Matrix33F a = new Matrix33F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F);
		final Matrix33F b = new Matrix33F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F);
		final Matrix33F c = Matrix33F.multiply(a, b);
		
		assertEquals( 30.0F, c.element11);
		assertEquals( 36.0F, c.element12);
		assertEquals( 42.0F, c.element13);
		assertEquals( 66.0F, c.element21);
		assertEquals( 81.0F, c.element22);
		assertEquals( 96.0F, c.element23);
		assertEquals(102.0F, c.element31);
		assertEquals(126.0F, c.element32);
		assertEquals(150.0F, c.element33);
		
		assertThrows(NullPointerException.class, () -> Matrix33F.multiply(a, null));
		assertThrows(NullPointerException.class, () -> Matrix33F.multiply(null, b));
	}
	
	@Test
	public void testRead() throws IOException {
		final Matrix33F a = new Matrix33F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeFloat(a.element11);
		dataOutput.writeFloat(a.element12);
		dataOutput.writeFloat(a.element13);
		dataOutput.writeFloat(a.element21);
		dataOutput.writeFloat(a.element22);
		dataOutput.writeFloat(a.element23);
		dataOutput.writeFloat(a.element31);
		dataOutput.writeFloat(a.element32);
		dataOutput.writeFloat(a.element33);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Matrix33F b = Matrix33F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Matrix33F.read(null));
		assertThrows(UncheckedIOException.class, () -> Matrix33F.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testRotate() {
		final Matrix33F matrix = Matrix33F.rotate(AngleF.degrees(0.0F));
		
		assertEquals(+1.0F, matrix.element11);
		assertEquals(+0.0F, matrix.element12);
		assertEquals(+0.0F, matrix.element13);
		assertEquals(-0.0F, matrix.element21);
		assertEquals(+1.0F, matrix.element22);
		assertEquals(+0.0F, matrix.element23);
		assertEquals(+0.0F, matrix.element31);
		assertEquals(+0.0F, matrix.element32);
		assertEquals(+1.0F, matrix.element33);
		
		assertThrows(NullPointerException.class, () -> Matrix33F.rotate(null));
	}
	
	@Test
	public void testScaleFloat() {
		final Matrix33F matrix = Matrix33F.scale(2.0F);
		
		assertEquals(2.0F, matrix.element11);
		assertEquals(0.0F, matrix.element12);
		assertEquals(0.0F, matrix.element13);
		assertEquals(0.0F, matrix.element21);
		assertEquals(2.0F, matrix.element22);
		assertEquals(0.0F, matrix.element23);
		assertEquals(0.0F, matrix.element31);
		assertEquals(0.0F, matrix.element32);
		assertEquals(1.0F, matrix.element33);
	}
	
	@Test
	public void testScaleFloatFloat() {
		final Matrix33F matrix = Matrix33F.scale(2.0F, 3.0F);
		
		assertEquals(2.0F, matrix.element11);
		assertEquals(0.0F, matrix.element12);
		assertEquals(0.0F, matrix.element13);
		assertEquals(0.0F, matrix.element21);
		assertEquals(3.0F, matrix.element22);
		assertEquals(0.0F, matrix.element23);
		assertEquals(0.0F, matrix.element31);
		assertEquals(0.0F, matrix.element32);
		assertEquals(1.0F, matrix.element33);
	}
	
	@Test
	public void testScaleVector2F() {
		final Matrix33F matrix = Matrix33F.scale(new Vector2F(2.0F, 3.0F));
		
		assertEquals(2.0F, matrix.element11);
		assertEquals(0.0F, matrix.element12);
		assertEquals(0.0F, matrix.element13);
		assertEquals(0.0F, matrix.element21);
		assertEquals(3.0F, matrix.element22);
		assertEquals(0.0F, matrix.element23);
		assertEquals(0.0F, matrix.element31);
		assertEquals(0.0F, matrix.element32);
		assertEquals(1.0F, matrix.element33);
		
		assertThrows(NullPointerException.class, () -> Matrix33F.scale(null));
	}
	
	@Test
	public void testToString() {
		final Matrix33F matrix = new Matrix33F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F);
		
		assertEquals("new Matrix33F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F)", matrix.toString());
	}
	
	@Test
	public void testTranslateFloatFloat() {
		final Matrix33F matrix = Matrix33F.translate(2.0F, 3.0F);
		
		assertEquals(1.0F, matrix.element11);
		assertEquals(0.0F, matrix.element12);
		assertEquals(2.0F, matrix.element13);
		assertEquals(0.0F, matrix.element21);
		assertEquals(1.0F, matrix.element22);
		assertEquals(3.0F, matrix.element23);
		assertEquals(0.0F, matrix.element31);
		assertEquals(0.0F, matrix.element32);
		assertEquals(1.0F, matrix.element33);
	}
	
	@Test
	public void testTranslatePoint2F() {
		final Matrix33F matrix = Matrix33F.translate(new Point2F(2.0F, 3.0F));
		
		assertEquals(1.0F, matrix.element11);
		assertEquals(0.0F, matrix.element12);
		assertEquals(2.0F, matrix.element13);
		assertEquals(0.0F, matrix.element21);
		assertEquals(1.0F, matrix.element22);
		assertEquals(3.0F, matrix.element23);
		assertEquals(0.0F, matrix.element31);
		assertEquals(0.0F, matrix.element32);
		assertEquals(1.0F, matrix.element33);
		
		assertThrows(NullPointerException.class, () -> Matrix33F.translate(null));
	}
	
	@Test
	public void testTranspose() {
		final Matrix33F a = new Matrix33F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F);
		final Matrix33F b = Matrix33F.transpose(a);
		
		assertEquals(1.0F, b.element11);
		assertEquals(4.0F, b.element12);
		assertEquals(7.0F, b.element13);
		assertEquals(2.0F, b.element21);
		assertEquals(5.0F, b.element22);
		assertEquals(8.0F, b.element23);
		assertEquals(3.0F, b.element31);
		assertEquals(6.0F, b.element32);
		assertEquals(9.0F, b.element33);
		
		assertThrows(NullPointerException.class, () -> Matrix33F.transpose(null));
	}
	
	@Test
	public void testWrite() {
		final Matrix33F a = new Matrix33F(1.0F, 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 8.0F, 9.0F);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Matrix33F b = Matrix33F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}