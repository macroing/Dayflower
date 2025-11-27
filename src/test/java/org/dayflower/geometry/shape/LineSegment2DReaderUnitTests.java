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
public final class LineSegment2DReaderUnitTests {
	public LineSegment2DReaderUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testIsSupported() {
		final LineSegment2DReader lineSegment2DReader = new LineSegment2DReader();
		
		assertTrue(lineSegment2DReader.isSupported(LineSegment2D.ID));
		
		assertFalse(lineSegment2DReader.isSupported(0));
	}
	
	@Test
	public void testReadDataInput() {
		final LineSegment2DReader lineSegment2DReader = new LineSegment2DReader();
		
		final LineSegment2D lineSegmentA = new LineSegment2D(new Point2D(0.0D, 0.0D), new Point2D(5.0D, 5.0D));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		lineSegmentA.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final LineSegment2D lineSegmentB = lineSegment2DReader.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(lineSegmentA, lineSegmentB);
		
		assertThrows(IllegalArgumentException.class, () -> lineSegment2DReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {0, 0, 0, 0}))));
		assertThrows(NullPointerException.class, () -> lineSegment2DReader.read(null));
		assertThrows(UncheckedIOException.class, () -> lineSegment2DReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testReadDataInputInt() {
		final LineSegment2DReader lineSegment2DReader = new LineSegment2DReader();
		
		assertThrows(IllegalArgumentException.class, () -> lineSegment2DReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {})), 0));
		assertThrows(NullPointerException.class, () -> lineSegment2DReader.read(null, LineSegment2D.ID));
		assertThrows(UncheckedIOException.class, () -> lineSegment2DReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0})), LineSegment2D.ID));
	}
}