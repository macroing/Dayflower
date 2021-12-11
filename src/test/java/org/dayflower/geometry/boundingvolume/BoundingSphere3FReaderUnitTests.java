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
public final class BoundingSphere3FReaderUnitTests {
	public BoundingSphere3FReaderUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testIsSupported() {
		final BoundingSphere3FReader boundingSphere3FReader = new BoundingSphere3FReader();
		
		assertTrue(boundingSphere3FReader.isSupported(BoundingSphere3F.ID));
		
		assertFalse(boundingSphere3FReader.isSupported(0));
	}
	
	@Test
	public void testReadDataInput() {
		final BoundingSphere3FReader boundingSphere3FReader = new BoundingSphere3FReader();
		
		final BoundingSphere3F boundingSphereA = new BoundingSphere3F();
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		boundingSphereA.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final BoundingSphere3F boundingSphereB = boundingSphere3FReader.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(boundingSphereA, boundingSphereB);
		
		assertThrows(IllegalArgumentException.class, () -> boundingSphere3FReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {0, 0, 0, 0}))));
		assertThrows(NullPointerException.class, () -> boundingSphere3FReader.read(null));
		assertThrows(UncheckedIOException.class, () -> boundingSphere3FReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testReadDataInputInt() {
		final BoundingSphere3FReader boundingSphere3FReader = new BoundingSphere3FReader();
		
		assertThrows(IllegalArgumentException.class, () -> boundingSphere3FReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {})), 0));
		assertThrows(NullPointerException.class, () -> boundingSphere3FReader.read(null, BoundingSphere3F.ID));
		assertThrows(UncheckedIOException.class, () -> boundingSphere3FReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {})), BoundingSphere3F.ID));
	}
}