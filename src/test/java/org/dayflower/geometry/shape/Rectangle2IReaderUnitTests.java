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
public final class Rectangle2IReaderUnitTests {
	public Rectangle2IReaderUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testIsSupported() {
		final Rectangle2IReader rectangle2IReader = new Rectangle2IReader();
		
		assertTrue(rectangle2IReader.isSupported(Rectangle2I.ID));
		
		assertFalse(rectangle2IReader.isSupported(0));
	}
	
	@Test
	public void testReadDataInput() {
		final Rectangle2IReader rectangle2IReader = new Rectangle2IReader();
		
		final Rectangle2I rectangleA = new Rectangle2I(new Point2I(10, 10), new Point2I(20, 10), new Point2I(20, 20), new Point2I(10, 20));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		rectangleA.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Rectangle2I rectangleB = rectangle2IReader.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(rectangleA, rectangleB);
		
		assertThrows(IllegalArgumentException.class, () -> rectangle2IReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {0, 0, 0, 0}))));
		assertThrows(NullPointerException.class, () -> rectangle2IReader.read(null));
		assertThrows(UncheckedIOException.class, () -> rectangle2IReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testReadDataInputInt() {
		final Rectangle2IReader rectangle2IReader = new Rectangle2IReader();
		
		assertThrows(IllegalArgumentException.class, () -> rectangle2IReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {})), 0));
		assertThrows(NullPointerException.class, () -> rectangle2IReader.read(null, Rectangle2I.ID));
		assertThrows(UncheckedIOException.class, () -> rectangle2IReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {0, 0, 0, 0, 0, 0, 0, 0})), Rectangle2I.ID));
	}
}