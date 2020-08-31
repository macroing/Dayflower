/**
 * Copyright 2020 J&#246;rgen Lundgren
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

import java.lang.reflect.Field;

//TODO: Add Javadocs!
public final class AxisAlignedBoundingBox3F implements BoundingVolume3F {
	@Override
	public BoundingVolume3F transform(final Matrix44F matrix) {
		return null;
	}
	
	@Override
	public Point3F getClosestPointTo(final Point3F point) {
		return null;
	}
	
	@Override
	public Point3F getMaximum() {
		return null;
	}
	
	@Override
	public Point3F getMinimum() {
		return null;
	}
	
	@Override
	public boolean contains(final Point3F point) {
		return false;
	}
	
	@Override
	public float getSurfaceArea() {
		return 0.0F;
	}
	
	@Override
	public float getVolume() {
		return 0.0F;
	}
	
	@Override
	public float intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		return 0.0F;
	}
}