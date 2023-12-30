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
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.UncheckedIOException;

import org.dayflower.geometry.BoundingVolume3F;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class DefaultBoundingVolume3FReaderUnitTests {
	public DefaultBoundingVolume3FReaderUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testIsSupported() {
		final DefaultBoundingVolume3FReader defaultBoundingVolume3FReader = new DefaultBoundingVolume3FReader();
		
		assertTrue(defaultBoundingVolume3FReader.isSupported(AxisAlignedBoundingBox3F.ID));
		assertTrue(defaultBoundingVolume3FReader.isSupported(BoundingSphere3F.ID));
		assertTrue(defaultBoundingVolume3FReader.isSupported(InfiniteBoundingVolume3F.ID));
		
		assertFalse(defaultBoundingVolume3FReader.isSupported(0));
	}
	
	@Test
	public void testReadDataInput() {
		final DefaultBoundingVolume3FReader defaultBoundingVolume3FReader = new DefaultBoundingVolume3FReader();
		
		final BoundingVolume3F boundingVolumeA = new AxisAlignedBoundingBox3F();
		final BoundingVolume3F boundingVolumeB = new BoundingSphere3F();
		final BoundingVolume3F boundingVolumeC = new InfiniteBoundingVolume3F();
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		boundingVolumeA.write(dataOutput);
		boundingVolumeB.write(dataOutput);
		boundingVolumeC.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final DataInput dataInput = new DataInputStream(new ByteArrayInputStream(bytes));
		
		final BoundingVolume3F boundingVolumeD = defaultBoundingVolume3FReader.read(dataInput);
		final BoundingVolume3F boundingVolumeE = defaultBoundingVolume3FReader.read(dataInput);
		final BoundingVolume3F boundingVolumeF = defaultBoundingVolume3FReader.read(dataInput);
		
		assertEquals(boundingVolumeA, boundingVolumeD);
		assertEquals(boundingVolumeB, boundingVolumeE);
		assertEquals(boundingVolumeC, boundingVolumeF);
		
		assertThrows(IllegalArgumentException.class, () -> defaultBoundingVolume3FReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {0, 0, 0, 0}))));
		assertThrows(NullPointerException.class, () -> defaultBoundingVolume3FReader.read(null));
		assertThrows(UncheckedIOException.class, () -> defaultBoundingVolume3FReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testReadDataInputInt() {
		final DefaultBoundingVolume3FReader defaultBoundingVolume3FReader = new DefaultBoundingVolume3FReader();
		
		assertThrows(IllegalArgumentException.class, () -> defaultBoundingVolume3FReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {})), 0));
		assertThrows(NullPointerException.class, () -> defaultBoundingVolume3FReader.read(null, AxisAlignedBoundingBox3F.ID));
		assertThrows(UncheckedIOException.class, () -> defaultBoundingVolume3FReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {})), AxisAlignedBoundingBox3F.ID));
	}
}