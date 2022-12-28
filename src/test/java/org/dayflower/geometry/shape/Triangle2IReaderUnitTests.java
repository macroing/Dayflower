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

import org.dayflower.geometry.Point2I;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class Triangle2IReaderUnitTests {
	public Triangle2IReaderUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testIsSupported() {
		final Triangle2IReader triangle2IReader = new Triangle2IReader();
		
		assertTrue(triangle2IReader.isSupported(Triangle2I.ID));
		
		assertFalse(triangle2IReader.isSupported(0));
	}
	
	@Test
	public void testReadDataInput() {
		final Triangle2IReader triangle2IReader = new Triangle2IReader();
		
		final Triangle2I triangleA = new Triangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		triangleA.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Triangle2I triangleB = triangle2IReader.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(triangleA, triangleB);
		
		assertThrows(IllegalArgumentException.class, () -> triangle2IReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {0, 0, 0, 0}))));
		assertThrows(NullPointerException.class, () -> triangle2IReader.read(null));
		assertThrows(UncheckedIOException.class, () -> triangle2IReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testReadDataInputInt() {
		final Triangle2IReader triangle2IReader = new Triangle2IReader();
		
		assertThrows(IllegalArgumentException.class, () -> triangle2IReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {})), 0));
		assertThrows(NullPointerException.class, () -> triangle2IReader.read(null, Triangle2I.ID));
		assertThrows(UncheckedIOException.class, () -> triangle2IReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {0, 0, 0, 0, 0, 0, 0, 0})), Triangle2I.ID));
	}
}