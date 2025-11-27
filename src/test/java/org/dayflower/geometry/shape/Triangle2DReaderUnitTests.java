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
package org.dayflower.geometry.shape;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.UncheckedIOException;

import org.dayflower.geometry.Point2D;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class Triangle2DReaderUnitTests {
	public Triangle2DReaderUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testIsSupported() {
		final Triangle2DReader triangle2DReader = new Triangle2DReader();
		
		assertTrue(triangle2DReader.isSupported(Triangle2D.ID));
		
		assertFalse(triangle2DReader.isSupported(0));
	}
	
	@Test
	public void testReadDataInput() {
		final Triangle2DReader triangle2DReader = new Triangle2DReader();
		
		final Triangle2D triangleA = new Triangle2D(new Point2D(10.0D, 10.0D), new Point2D(20.0D, 10.0D), new Point2D(20.0D, 20.0D));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		triangleA.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Triangle2D triangleB = triangle2DReader.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(triangleA, triangleB);
		
		assertThrows(IllegalArgumentException.class, () -> triangle2DReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {0, 0, 0, 0}))));
		assertThrows(NullPointerException.class, () -> triangle2DReader.read(null));
		assertThrows(UncheckedIOException.class, () -> triangle2DReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testReadDataInputInt() {
		final Triangle2DReader triangle2DReader = new Triangle2DReader();
		
		assertThrows(IllegalArgumentException.class, () -> triangle2DReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {})), 0));
		assertThrows(NullPointerException.class, () -> triangle2DReader.read(null, Triangle2D.ID));
		assertThrows(UncheckedIOException.class, () -> triangle2DReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {0, 0, 0, 0, 0, 0, 0, 0})), Triangle2D.ID));
	}
}