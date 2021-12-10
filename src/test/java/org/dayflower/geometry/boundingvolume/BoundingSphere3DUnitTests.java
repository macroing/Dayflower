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

import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.Vector3D;
import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class BoundingSphere3DUnitTests {
	public BoundingSphere3DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testGetMidpoint() {
		final BoundingSphere3D boundingSphere = new BoundingSphere3D(5.0D, new Point3D(10.0D, 10.0D, 10.0D));
		
		assertEquals(new Point3D(10.0D, 10.0D, 10.0D), boundingSphere.getMidpoint());
	}
	
	@Test
	public void testIntersectsRay3DDoubleDouble() {
		final BoundingSphere3D boundingSphere = new BoundingSphere3D(5.0D, new Point3D(0.0D, 0.0D, 10.0D));
		
		assertTrue(boundingSphere.intersects(new Ray3D(new Point3D(0.0D, 0.0D, 0.0D), Vector3D.z()), 0.0D, Double.MAX_VALUE));
		
		assertFalse(boundingSphere.intersects(new Ray3D(new Point3D(0.0D, 0.0D, 0.0D), Vector3D.x()), 0.0D, Double.MAX_VALUE));
		
		assertThrows(NullPointerException.class, () -> boundingSphere.intersects(null, 0.0D, Double.MAX_VALUE));
	}
}