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

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class Circle2DReaderUnitTests {
	public Circle2DReaderUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testIsSupported() {
		final Circle2DReader circle2DReader = new Circle2DReader();
		
		assertTrue(circle2DReader.isSupported(Circle2D.ID));
		
		assertFalse(circle2DReader.isSupported(0));
	}
	
	@Test
	public void testReadDataInput() {
		final Circle2DReader circle2DReader = new Circle2DReader();
		
		final Circle2D circleA = new Circle2D();
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		circleA.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Circle2D circleB = circle2DReader.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(circleA, circleB);
		
		assertThrows(IllegalArgumentException.class, () -> circle2DReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {0, 0, 0, 0}))));
		assertThrows(NullPointerException.class, () -> circle2DReader.read(null));
		assertThrows(UncheckedIOException.class, () -> circle2DReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testReadDataInputInt() {
		final Circle2DReader circle2DReader = new Circle2DReader();
		
		assertThrows(IllegalArgumentException.class, () -> circle2DReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {})), 0));
		assertThrows(NullPointerException.class, () -> circle2DReader.read(null, Circle2D.ID));
		assertThrows(UncheckedIOException.class, () -> circle2DReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0})), Circle2D.ID));
	}
}