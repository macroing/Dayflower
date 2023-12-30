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

import org.dayflower.geometry.BoundingVolume3D;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class DefaultBoundingVolume3DReaderUnitTests {
	public DefaultBoundingVolume3DReaderUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testIsSupported() {
		final DefaultBoundingVolume3DReader defaultBoundingVolume3DReader = new DefaultBoundingVolume3DReader();
		
		assertTrue(defaultBoundingVolume3DReader.isSupported(AxisAlignedBoundingBox3D.ID));
		assertTrue(defaultBoundingVolume3DReader.isSupported(BoundingSphere3D.ID));
		assertTrue(defaultBoundingVolume3DReader.isSupported(InfiniteBoundingVolume3D.ID));
		
		assertFalse(defaultBoundingVolume3DReader.isSupported(0));
	}
	
	@Test
	public void testReadDataInput() {
		final DefaultBoundingVolume3DReader defaultBoundingVolume3DReader = new DefaultBoundingVolume3DReader();
		
		final BoundingVolume3D boundingVolumeA = new AxisAlignedBoundingBox3D();
		final BoundingVolume3D boundingVolumeB = new BoundingSphere3D();
		final BoundingVolume3D boundingVolumeC = new InfiniteBoundingVolume3D();
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		boundingVolumeA.write(dataOutput);
		boundingVolumeB.write(dataOutput);
		boundingVolumeC.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final DataInput dataInput = new DataInputStream(new ByteArrayInputStream(bytes));
		
		final BoundingVolume3D boundingVolumeD = defaultBoundingVolume3DReader.read(dataInput);
		final BoundingVolume3D boundingVolumeE = defaultBoundingVolume3DReader.read(dataInput);
		final BoundingVolume3D boundingVolumeF = defaultBoundingVolume3DReader.read(dataInput);
		
		assertEquals(boundingVolumeA, boundingVolumeD);
		assertEquals(boundingVolumeB, boundingVolumeE);
		assertEquals(boundingVolumeC, boundingVolumeF);
		
		assertThrows(IllegalArgumentException.class, () -> defaultBoundingVolume3DReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {0, 0, 0, 0}))));
		assertThrows(NullPointerException.class, () -> defaultBoundingVolume3DReader.read(null));
		assertThrows(UncheckedIOException.class, () -> defaultBoundingVolume3DReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testReadDataInputInt() {
		final DefaultBoundingVolume3DReader defaultBoundingVolume3DReader = new DefaultBoundingVolume3DReader();
		
		assertThrows(IllegalArgumentException.class, () -> defaultBoundingVolume3DReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {})), 0));
		assertThrows(NullPointerException.class, () -> defaultBoundingVolume3DReader.read(null, AxisAlignedBoundingBox3D.ID));
		assertThrows(UncheckedIOException.class, () -> defaultBoundingVolume3DReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {})), AxisAlignedBoundingBox3D.ID));
	}
}