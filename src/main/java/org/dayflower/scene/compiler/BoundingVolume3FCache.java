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
package org.dayflower.scene.compiler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.geometry.boundingvolume.BoundingSphere3F;
import org.dayflower.geometry.boundingvolume.InfiniteBoundingVolume3F;
import org.dayflower.node.NodeFilter;
import org.dayflower.scene.Scene;
import org.dayflower.utility.Floats;

final class BoundingVolume3FCache {
	private final List<AxisAlignedBoundingBox3F> distinctAxisAlignedBoundingBox3Fs;
	private final List<BoundingSphere3F> distinctBoundingSphere3Fs;
	private final List<BoundingVolume3F> distinctBoundingVolume3Fs;
	private final List<InfiniteBoundingVolume3F> distinctInfiniteBoundingVolume3Fs;
	private final Map<AxisAlignedBoundingBox3F, Integer> distinctToOffsetsAxisAlignedBoundingBox3Fs;
	private final Map<BoundingSphere3F, Integer> distinctToOffsetsBoundingSphere3Fs;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public BoundingVolume3FCache() {
		this.distinctAxisAlignedBoundingBox3Fs = new ArrayList<>();
		this.distinctBoundingSphere3Fs = new ArrayList<>();
		this.distinctBoundingVolume3Fs = new ArrayList<>();
		this.distinctInfiniteBoundingVolume3Fs = new ArrayList<>();
		this.distinctToOffsetsAxisAlignedBoundingBox3Fs = new LinkedHashMap<>();
		this.distinctToOffsetsBoundingSphere3Fs = new LinkedHashMap<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean contains(final BoundingVolume3F boundingVolume3F) {
		return this.distinctBoundingVolume3Fs.contains(Objects.requireNonNull(boundingVolume3F, "boundingVolume3F == null"));
	}
	
	public float[] toBoundingVolume3FAxisAlignedBoundingBox3FArray() {
		return Floats.toArray(this.distinctAxisAlignedBoundingBox3Fs, axisAlignedBoundingBox3F -> axisAlignedBoundingBox3F.toArray(), 1);
	}
	
	public float[] toBoundingVolume3FBoundingSphere3FArray() {
		return Floats.toArray(this.distinctBoundingSphere3Fs, boundingSphere3F -> boundingSphere3F.toArray(), 1);
	}
	
	public int findOffsetFor(final BoundingVolume3F boundingVolume3F) {
		Objects.requireNonNull(boundingVolume3F, "boundingVolume3F == null");
		
		if(boundingVolume3F instanceof AxisAlignedBoundingBox3F) {
			return this.distinctToOffsetsAxisAlignedBoundingBox3Fs.get(boundingVolume3F).intValue();
		} else if(boundingVolume3F instanceof BoundingSphere3F) {
			return this.distinctToOffsetsBoundingSphere3Fs.get(boundingVolume3F).intValue();
		} else if(boundingVolume3F instanceof InfiniteBoundingVolume3F) {
			return 0;
		} else {
			return 0;
		}
	}
	
	public void clear() {
		this.distinctAxisAlignedBoundingBox3Fs.clear();
		this.distinctBoundingSphere3Fs.clear();
		this.distinctBoundingVolume3Fs.clear();
		this.distinctInfiniteBoundingVolume3Fs.clear();
		this.distinctToOffsetsAxisAlignedBoundingBox3Fs.clear();
		this.distinctToOffsetsBoundingSphere3Fs.clear();
	}
	
	public void setup(final Scene scene) {
		Objects.requireNonNull(scene, "scene == null");
		
//		Add all distinct BoundingVolume3F instances:
		this.distinctBoundingVolume3Fs.clear();
		this.distinctBoundingVolume3Fs.addAll(NodeFilter.filterAllDistinct(scene, BoundingVolume3F.class).stream().filter(BoundingVolume3FCache::doFilterBoundingVolume3F).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct AxisAlignedBoundingBox3F instances:
		this.distinctAxisAlignedBoundingBox3Fs.clear();
		this.distinctAxisAlignedBoundingBox3Fs.addAll(this.distinctBoundingVolume3Fs.stream().filter(boundingVolume3F -> boundingVolume3F instanceof AxisAlignedBoundingBox3F).map(boundingVolume3F -> AxisAlignedBoundingBox3F.class.cast(boundingVolume3F)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct BoundingSphere3F instances:
		this.distinctBoundingSphere3Fs.clear();
		this.distinctBoundingSphere3Fs.addAll(this.distinctBoundingVolume3Fs.stream().filter(boundingVolume3F -> boundingVolume3F instanceof BoundingSphere3F).map(boundingVolume3F -> BoundingSphere3F.class.cast(boundingVolume3F)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct InfiniteBoundingVolume3F instances:
		this.distinctInfiniteBoundingVolume3Fs.clear();
		this.distinctInfiniteBoundingVolume3Fs.addAll(this.distinctBoundingVolume3Fs.stream().filter(boundingVolume3F -> boundingVolume3F instanceof InfiniteBoundingVolume3F).map(boundingVolume3F -> InfiniteBoundingVolume3F.class.cast(boundingVolume3F)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Create offset mappings for all distinct AxisAlignedBoundingBox3F instances:
		this.distinctToOffsetsAxisAlignedBoundingBox3Fs.clear();
		this.distinctToOffsetsAxisAlignedBoundingBox3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctAxisAlignedBoundingBox3Fs, AxisAlignedBoundingBox3F.ARRAY_LENGTH));
		
//		Create offset mappings for all distinct BoundingSphere3F instances:
		this.distinctToOffsetsBoundingSphere3Fs.clear();
		this.distinctToOffsetsBoundingSphere3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctBoundingSphere3Fs, BoundingSphere3F.ARRAY_LENGTH));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static boolean doFilterBoundingVolume3F(final BoundingVolume3F boundingVolume3F) {
		if(boundingVolume3F instanceof AxisAlignedBoundingBox3F) {
			return true;
		} else if(boundingVolume3F instanceof BoundingSphere3F) {
			return true;
		} else if(boundingVolume3F instanceof InfiniteBoundingVolume3F) {
			return true;
		} else {
			return true;
		}
	}
}