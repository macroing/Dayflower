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

import org.dayflower.geometry.Point3F;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class AxisAlignedBoundingBox3FUnitTests {
	public AxisAlignedBoundingBox3FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConstants() {
		assertEquals(1, AxisAlignedBoundingBox3F.ID);
	}
	
	@Test
	public void testConstructor() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F();
		
		assertEquals(new Point3F(+0.5F, +0.5F, +0.5F), axisAlignedBoundingBox.getMaximum());
		assertEquals(new Point3F(-0.5F, -0.5F, -0.5F), axisAlignedBoundingBox.getMinimum());
	}
	
	@Test
	public void testConstructorPoint3FPoint3F() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F(new Point3F(1.0F, 2.0F, 3.0F), new Point3F(4.0F, 5.0F, 6.0F));
		
		assertEquals(new Point3F(4.0F, 5.0F, 6.0F), axisAlignedBoundingBox.getMaximum());
		assertEquals(new Point3F(1.0F, 2.0F, 3.0F), axisAlignedBoundingBox.getMinimum());
		
		assertThrows(NullPointerException.class, () -> new AxisAlignedBoundingBox3F(new Point3F(), null));
		assertThrows(NullPointerException.class, () -> new AxisAlignedBoundingBox3F(null, new Point3F()));
	}
	
	@Test
	public void testGetID() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F();
		
		assertEquals(AxisAlignedBoundingBox3F.ID, axisAlignedBoundingBox.getID());
	}
	
	@Test
	public void testGetMaximum() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F(new Point3F(-1.0F, -1.0F, -1.0F), new Point3F(+1.0F, +1.0F, +1.0F));
		
		assertEquals(new Point3F(+1.0F, +1.0F, +1.0F), axisAlignedBoundingBox.getMaximum());
	}
	
	@Test
	public void testGetMidpoint() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F(new Point3F(-1.0F, -1.0F, -1.0F), new Point3F(+1.0F, +1.0F, +1.0F));
		
		assertEquals(new Point3F(0.0F, 0.0F, 0.0F), axisAlignedBoundingBox.getMidpoint());
	}
	
	@Test
	public void testGetMinimum() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F(new Point3F(-1.0F, -1.0F, -1.0F), new Point3F(+1.0F, +1.0F, +1.0F));
		
		assertEquals(new Point3F(-1.0F, -1.0F, -1.0F), axisAlignedBoundingBox.getMinimum());
	}
	
	@Test
	public void testGetSurfaceArea() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F(new Point3F(-1.0F, -2.0F, -3.0F), new Point3F(+1.0F, +2.0F, +3.0F));
		
		assertEquals(88.0F, axisAlignedBoundingBox.getSurfaceArea());
	}
	
	@Test
	public void testGetVolume() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F(new Point3F(-1.0F, -2.0F, -3.0F), new Point3F(+1.0F, +2.0F, +3.0F));
		
		assertEquals(48.0F, axisAlignedBoundingBox.getVolume());
	}
	
	@Test
	public void testHashCode() {
		final AxisAlignedBoundingBox3F a = new AxisAlignedBoundingBox3F();
		final AxisAlignedBoundingBox3F b = new AxisAlignedBoundingBox3F();
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testToString() {
		final AxisAlignedBoundingBox3F axisAlignedBoundingBox = new AxisAlignedBoundingBox3F(new Point3F(-1.0F, -1.0F, -1.0F), new Point3F(+1.0F, +1.0F, +1.0F));
		
		assertEquals("new AxisAlignedBoundingBox3F(new Point3F(1.0F, 1.0F, 1.0F), new Point3F(-1.0F, -1.0F, -1.0F))", axisAlignedBoundingBox.toString());
	}
}