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
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.dayflower.geometry.Point3D;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class AxisAlignedBoundingBox3DUnitTests {
	public AxisAlignedBoundingBox3DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstants() {
		assertEquals(1, AxisAlignedBoundingBox3D.ID);
	}
	
	@Test
	public void testConstructor() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D();
		
		assertEquals(new Point3D(+0.5D, +0.5D, +0.5D), axisAlignedBoundingBox.getMaximum());
		assertEquals(new Point3D(-0.5D, -0.5D, -0.5D), axisAlignedBoundingBox.getMinimum());
	}
	
	@Test
	public void testConstructorPoint3DPoint3D() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D(new Point3D(1.0D, 2.0D, 3.0D), new Point3D(4.0D, 5.0D, 6.0D));
		
		assertEquals(new Point3D(4.0D, 5.0D, 6.0D), axisAlignedBoundingBox.getMaximum());
		assertEquals(new Point3D(1.0D, 2.0D, 3.0D), axisAlignedBoundingBox.getMinimum());
		
		assertThrows(NullPointerException.class, () -> new AxisAlignedBoundingBox3D(new Point3D(), null));
		assertThrows(NullPointerException.class, () -> new AxisAlignedBoundingBox3D(null, new Point3D()));
	}
	
	@Test
	public void testGetID() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D();
		
		assertEquals(AxisAlignedBoundingBox3D.ID, axisAlignedBoundingBox.getID());
	}
	
	@Test
	public void testGetMaximum() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D(new Point3D(-1.0D, -1.0D, -1.0D), new Point3D(+1.0D, +1.0D, +1.0D));
		
		assertEquals(new Point3D(+1.0D, +1.0D, +1.0D), axisAlignedBoundingBox.getMaximum());
	}
	
	@Test
	public void testGetMidpoint() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D(new Point3D(-1.0D, -1.0D, -1.0D), new Point3D(+1.0D, +1.0D, +1.0D));
		
		assertEquals(new Point3D(0.0D, 0.0D, 0.0D), axisAlignedBoundingBox.getMidpoint());
	}
	
	@Test
	public void testGetMinimum() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D(new Point3D(-1.0D, -1.0D, -1.0D), new Point3D(+1.0D, +1.0D, +1.0D));
		
		assertEquals(new Point3D(-1.0D, -1.0D, -1.0D), axisAlignedBoundingBox.getMinimum());
	}
	
	@Test
	public void testGetSurfaceArea() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D(new Point3D(-1.0D, -2.0D, -3.0D), new Point3D(+1.0D, +2.0D, +3.0D));
		
		assertEquals(88.0D, axisAlignedBoundingBox.getSurfaceArea());
	}
	
	@Test
	public void testGetVolume() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D(new Point3D(-1.0D, -2.0D, -3.0D), new Point3D(+1.0D, +2.0D, +3.0D));
		
		assertEquals(48.0D, axisAlignedBoundingBox.getVolume());
	}
	
	@Test
	public void testHashCode() {
		final AxisAlignedBoundingBox3D a = new AxisAlignedBoundingBox3D();
		final AxisAlignedBoundingBox3D b = new AxisAlignedBoundingBox3D();
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final AxisAlignedBoundingBox3D axisAlignedBoundingBox = new AxisAlignedBoundingBox3D(new Point3D(-1.0D, -1.0D, -1.0D), new Point3D(+1.0D, +1.0D, +1.0D));
		
		assertEquals("new AxisAlignedBoundingBox3D(new Point3D(1.0D, 1.0D, 1.0D), new Point3D(-1.0D, -1.0D, -1.0D))", axisAlignedBoundingBox.toString());
	}
}