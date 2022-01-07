/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
package org.dayflower.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.UncheckedIOException;

import org.dayflower.mock.DataOutputMock;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class AngleFUnitTests {
	public AngleFUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testCos() {
		final AngleF angle = AngleF.degrees(123.0F);
		
		assertEquals((float)(Math.cos((float)(Math.toRadians(123.0F)))), angle.cos());
	}
	
	@Test
	public void testDegreesFloat() {
		final AngleF a = AngleF.degrees(+360.0F);
		final AngleF b = AngleF.degrees(-360.0F);
		
		assertEquals(360.0F, a.getDegrees());
		assertEquals(360.0F, a.getDegreesMaximum());
		assertEquals(  0.0F, a.getDegreesMinimum());
		
		assertEquals((float)(Math.toRadians(360.0F)), a.getRadians());
		assertEquals((float)(Math.toRadians(360.0F)), a.getRadiansMaximum());
		assertEquals((float)(Math.toRadians(  0.0F)), a.getRadiansMinimum());
		
		assertEquals(  0.0F, b.getDegrees());
		assertEquals(360.0F, b.getDegreesMaximum());
		assertEquals(  0.0F, b.getDegreesMinimum());
		
		assertEquals((float)(Math.toRadians(  0.0F)), b.getRadians());
		assertEquals((float)(Math.toRadians(360.0F)), b.getRadiansMaximum());
		assertEquals((float)(Math.toRadians(  0.0F)), b.getRadiansMinimum());
	}
	
	@Test
	public void testDegreesFloatFloatFloat() {
		final AngleF a = AngleF.degrees(200.0F, 100.0F, 300.0F);
		final AngleF b = AngleF.degrees( 99.0F, 300.0F, 100.0F);
		
		assertEquals(200.0F, a.getDegrees());
		assertEquals(300.0F, a.getDegreesMaximum());
		assertEquals(100.0F, a.getDegreesMinimum());
		
		assertEquals((float)(Math.toRadians(200.0F)), a.getRadians());
		assertEquals((float)(Math.toRadians(300.0F)), a.getRadiansMaximum());
		assertEquals((float)(Math.toRadians(100.0F)), a.getRadiansMinimum());
		
		assertEquals(299.0F, b.getDegrees());
		assertEquals(300.0F, b.getDegreesMaximum());
		assertEquals(100.0F, b.getDegreesMinimum());
		
		assertEquals((float)(Math.toRadians(299.0F)), b.getRadians());
		assertEquals((float)(Math.toRadians(300.0F)), b.getRadiansMaximum());
		assertEquals((float)(Math.toRadians(100.0F)), b.getRadiansMinimum());
	}
	
	@Test
	public void testEquals() {
		final AngleF a = AngleF.degrees(180.0F, 0.0F, 360.0F);
		final AngleF b = AngleF.degrees(180.0F, 0.0F, 360.0F);
		final AngleF c = AngleF.degrees(180.0F, 0.0F, 359.0F);
		final AngleF d = AngleF.degrees(180.0F, 1.0F, 360.0F);
		final AngleF e = AngleF.degrees(181.0F, 1.0F, 360.0F);
		final AngleF f = null;
		
		assertEquals(a, a);
		assertEquals(a, b);
		assertEquals(b, a);
		assertNotEquals(a, c);
		assertNotEquals(c, a);
		assertNotEquals(a, d);
		assertNotEquals(d, a);
		assertNotEquals(a, e);
		assertNotEquals(e, a);
		assertNotEquals(a, f);
		assertNotEquals(f, a);
	}
	
	@Test
	public void testGetDegrees() {
		final AngleF a = AngleF.degrees(250.0F, 200.0F, 300.0F);
		final AngleF b = AngleF.degrees(301.0F, 200.0F, 300.0F);
		final AngleF c = AngleF.degrees(199.0F, 200.0F, 300.0F);
		
		assertEquals(250.0F, a.getDegrees());
		assertEquals(201.0F, b.getDegrees());
		assertEquals(299.0F, c.getDegrees());
	}
	
	@Test
	public void testGetDegreesMaximum() {
		final AngleF a = AngleF.degrees(0.0F,   0.0F, 360.0F);
		final AngleF b = AngleF.degrees(0.0F, 360.0F,   0.0F);
		
		assertEquals(360.0F, a.getDegreesMaximum());
		assertEquals(360.0F, b.getDegreesMaximum());
	}
	
	@Test
	public void testGetDegreesMinimum() {
		final AngleF a = AngleF.degrees(0.0F,   0.0F, 360.0F);
		final AngleF b = AngleF.degrees(0.0F, 360.0F,   0.0F);
		
		assertEquals(0.0F, a.getDegreesMinimum());
		assertEquals(0.0F, b.getDegreesMinimum());
	}
	
	@Test
	public void testGetRadians() {
		final AngleF a = AngleF.radians(250.0F, 200.0F, 300.0F);
		final AngleF b = AngleF.radians(301.0F, 200.0F, 300.0F);
		final AngleF c = AngleF.radians(199.0F, 200.0F, 300.0F);
		
		assertEquals(250.0F, a.getRadians());
		assertEquals(201.0F, b.getRadians());
		assertEquals(299.0F, c.getRadians());
	}
	
	@Test
	public void testGetRadiansMaximum() {
		final AngleF a = AngleF.radians(0.0F,   0.0F, 360.0F);
		final AngleF b = AngleF.radians(0.0F, 360.0F,   0.0F);
		
		assertEquals(360.0F, a.getRadiansMaximum());
		assertEquals(360.0F, b.getRadiansMaximum());
	}
	
	@Test
	public void testGetRadiansMinimum() {
		final AngleF a = AngleF.radians(0.0F,   0.0F, 360.0F);
		final AngleF b = AngleF.radians(0.0F, 360.0F,   0.0F);
		
		assertEquals(0.0F, a.getRadiansMinimum());
		assertEquals(0.0F, b.getRadiansMinimum());
	}
	
	@Test
	public void testHashCode() {
		final AngleF a = AngleF.degrees(0.0F, 360.0F, 0.0F);
		final AngleF b = AngleF.degrees(0.0F, 360.0F, 0.0F);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testSin() {
		final AngleF angle = AngleF.degrees(123.0F);
		
		assertEquals((float)(Math.sin((float)(Math.toRadians(123.0F)))), angle.sin());
	}
	
	@Test
	public void testToString() {
		final AngleF angle = AngleF.degrees(180.0F, 0.0F, 360.0F);
		
		assertEquals("AngleF.degrees(180.0F, 0.0F, 360.0F)", angle.toString());
	}
	
	@Test
	public void testWrite() {
		final AngleF a = AngleF.degrees(200.0F, 100.0F, 300.0F);
		final AngleF b = AngleF.radians(200.0F, 100.0F, 300.0F);
		
		final ByteArrayOutputStream byteArrayOutputStreamA = new ByteArrayOutputStream();
		final ByteArrayOutputStream byteArrayOutputStreamB = new ByteArrayOutputStream();
		
		final DataOutput dataOutputA = new DataOutputStream(byteArrayOutputStreamA);
		final DataOutput dataOutputB = new DataOutputStream(byteArrayOutputStreamB);
		
		a.write(dataOutputA);
		b.write(dataOutputB);
		
		final byte[] bytesA = byteArrayOutputStreamA.toByteArray();
		final byte[] bytesB = byteArrayOutputStreamB.toByteArray();
		
		final AngleF c = AngleF.read(new DataInputStream(new ByteArrayInputStream(bytesA)));
		final AngleF d = AngleF.read(new DataInputStream(new ByteArrayInputStream(bytesB)));
		
		assertEquals(a, c);
		assertEquals(b, d);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}