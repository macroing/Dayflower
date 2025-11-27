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

import org.dayflower.geometry.Point2F;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class Polygon2FReaderUnitTests {
	public Polygon2FReaderUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testIsSupported() {
		final Polygon2FReader polygon2FReader = new Polygon2FReader();
		
		assertTrue(polygon2FReader.isSupported(Polygon2F.ID));
		
		assertFalse(polygon2FReader.isSupported(0));
	}
	
	@Test
	public void testReadDataInput() {
		final Polygon2FReader polygon2FReader = new Polygon2FReader();
		
		final Polygon2F polygonA = new Polygon2F(new Point2F(10.0F, 10.0F), new Point2F(20.0F, 10.0F), new Point2F(20.0F, 20.0F), new Point2F(10.0F, 20.0F));
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		polygonA.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Polygon2F polygonB = polygon2FReader.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(polygonA, polygonB);
		
		assertThrows(IllegalArgumentException.class, () -> polygon2FReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {0, 0, 0, 0}))));
		assertThrows(NullPointerException.class, () -> polygon2FReader.read(null));
		assertThrows(UncheckedIOException.class, () -> polygon2FReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testReadDataInputInt() {
		final Polygon2FReader polygon2FReader = new Polygon2FReader();
		
		assertThrows(IllegalArgumentException.class, () -> polygon2FReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {})), 0));
		assertThrows(NullPointerException.class, () -> polygon2FReader.read(null, Polygon2F.ID));
		assertThrows(UncheckedIOException.class, () -> polygon2FReader.read(new DataInputStream(new ByteArrayInputStream(new byte[] {})), Polygon2F.ID));
	}
}