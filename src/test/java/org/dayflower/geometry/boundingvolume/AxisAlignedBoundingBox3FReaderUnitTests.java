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
package org.dayflower.geometry.boundingvolume;

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
public final class AxisAlignedBoundingBox3FReaderUnitTests {
	public AxisAlignedBoundingBox3FReaderUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testIsSupported() {
		final AxisAlignedBoundingBox3FReader axisAlignedBoundingBox3FReader = new AxisAlignedBoundingBox3FReader();
		
		assertTrue(axisAlignedBoundingBox3FReader.isSupported(AxisAlignedBoundingBox3F.ID));
		
		assertFalse(axisAlignedBoundingBox3FReader.isSupported(0));
	}
	
	@Test
	public void testReadDataInput() {
		final AxisAlignedBoundingBox3FReader axisAlignedBoundingBox3FReader = new AxisAlignedBoundingBox3FReader();
		
		final AxisAlignedBoundingBox3F axisAlignedBoundingBoxA = new AxisAlignedBoundingBox3F();
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		axisAlignedBoundingBoxA.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final AxisAlignedBoundingBox3F axisAlignedBoundingBoxB = axisAlignedBoundingBox3FReader.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(axisAlignedBoundingBoxA, axisAlignedBoundingBoxB);
		
		assertThrows(IllegalArgumentException.class, () -> axisAlignedBoundingBox3FReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {0, 0, 0, 0}))));
		assertThrows(NullPointerException.class, () -> axisAlignedBoundingBox3FReader.read(null));
		assertThrows(UncheckedIOException.class, () -> axisAlignedBoundingBox3FReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testReadDataInputInt() {
		final AxisAlignedBoundingBox3FReader axisAlignedBoundingBox3FReader = new AxisAlignedBoundingBox3FReader();
		
		assertThrows(IllegalArgumentException.class, () -> axisAlignedBoundingBox3FReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {})), 0));
		assertThrows(NullPointerException.class, () -> axisAlignedBoundingBox3FReader.read(null, AxisAlignedBoundingBox3F.ID));
		assertThrows(UncheckedIOException.class, () -> axisAlignedBoundingBox3FReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {})), AxisAlignedBoundingBox3F.ID));
	}
}