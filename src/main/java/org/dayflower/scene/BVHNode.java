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
package org.dayflower.scene;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;

import org.macroing.java.util.visitor.Node;

abstract class BVHNode implements Node {
	private final BoundingVolume3F boundingVolume;
	private final int depth;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	protected BVHNode(final Point3F a, final Point3F b, final int depth) {
		this.boundingVolume = new AxisAlignedBoundingBox3F(a, b);
		this.depth = depth;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public final BoundingVolume3F getBoundingVolume() {
		return this.boundingVolume;
	}
	
	public abstract boolean intersection(final Intersector intersector);
	
	public abstract boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum);
	
	public abstract float intersectionT(final Ray3F ray, final float[] tBounds);
	
	public final int getDepth() {
		return this.depth;
	}
}