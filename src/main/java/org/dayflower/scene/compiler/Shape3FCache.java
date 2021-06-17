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

import static org.dayflower.utility.Ints.padding;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.shape.Cone3F;
import org.dayflower.geometry.shape.Cylinder3F;
import org.dayflower.geometry.shape.Disk3F;
import org.dayflower.geometry.shape.Paraboloid3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.geometry.shape.Rectangle3F;
import org.dayflower.geometry.shape.RectangularCuboid3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Torus3F;
import org.dayflower.geometry.shape.Triangle3F;
import org.dayflower.geometry.shape.TriangleMesh3F;
import org.dayflower.node.NodeFilter;
import org.dayflower.scene.Scene;
import org.dayflower.utility.Floats;
import org.dayflower.utility.Ints;

final class Shape3FCache {
	private final List<Cone3F> distinctCone3Fs;
	private final List<Cylinder3F> distinctCylinder3Fs;
	private final List<Disk3F> distinctDisk3Fs;
	private final List<Paraboloid3F> distinctParaboloid3Fs;
	private final List<Plane3F> distinctPlane3Fs;
	private final List<Rectangle3F> distinctRectangle3Fs;
	private final List<RectangularCuboid3F> distinctRectangularCuboid3Fs;
	private final List<Shape3F> distinctShape3Fs;
	private final List<Sphere3F> distinctSphere3Fs;
	private final List<Torus3F> distinctTorus3Fs;
	private final List<Triangle3F> distinctTriangle3Fs;
	private final List<TriangleMesh3F> distinctTriangleMesh3Fs;
	private final Map<Cone3F, Integer> distinctToOffsetsCone3Fs;
	private final Map<Cylinder3F, Integer> distinctToOffsetsCylinder3Fs;
	private final Map<Disk3F, Integer> distinctToOffsetsDisk3Fs;
	private final Map<Paraboloid3F, Integer> distinctToOffsetsParaboloid3Fs;
	private final Map<Plane3F, Integer> distinctToOffsetsPlane3Fs;
	private final Map<Rectangle3F, Integer> distinctToOffsetsRectangle3Fs;
	private final Map<RectangularCuboid3F, Integer> distinctToOffsetsRectangularCuboid3Fs;
	private final Map<Sphere3F, Integer> distinctToOffsetsSphere3Fs;
	private final Map<Torus3F, Integer> distinctToOffsetsTorus3Fs;
	private final Map<Triangle3F, Integer> distinctToOffsetsTriangle3Fs;
	private final Map<TriangleMesh3F, Integer> distinctToOffsetsTriangleMesh3Fs;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Shape3FCache() {
		this.distinctCone3Fs = new ArrayList<>();
		this.distinctCylinder3Fs = new ArrayList<>();
		this.distinctDisk3Fs = new ArrayList<>();
		this.distinctParaboloid3Fs = new ArrayList<>();
		this.distinctPlane3Fs = new ArrayList<>();
		this.distinctRectangle3Fs = new ArrayList<>();
		this.distinctRectangularCuboid3Fs = new ArrayList<>();
		this.distinctShape3Fs = new ArrayList<>();
		this.distinctSphere3Fs = new ArrayList<>();
		this.distinctTorus3Fs = new ArrayList<>();
		this.distinctTriangle3Fs = new ArrayList<>();
		this.distinctTriangleMesh3Fs = new ArrayList<>();
		this.distinctToOffsetsCone3Fs = new LinkedHashMap<>();
		this.distinctToOffsetsCylinder3Fs = new LinkedHashMap<>();
		this.distinctToOffsetsDisk3Fs = new LinkedHashMap<>();
		this.distinctToOffsetsParaboloid3Fs = new LinkedHashMap<>();
		this.distinctToOffsetsPlane3Fs = new LinkedHashMap<>();
		this.distinctToOffsetsRectangle3Fs = new LinkedHashMap<>();
		this.distinctToOffsetsRectangularCuboid3Fs = new LinkedHashMap<>();
		this.distinctToOffsetsSphere3Fs = new LinkedHashMap<>();
		this.distinctToOffsetsTorus3Fs = new LinkedHashMap<>();
		this.distinctToOffsetsTriangle3Fs = new LinkedHashMap<>();
		this.distinctToOffsetsTriangleMesh3Fs = new LinkedHashMap<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean contains(final Shape3F shape3F) {
		return this.distinctShape3Fs.contains(Objects.requireNonNull(shape3F, "shape3F == null"));
	}
	
	public float[] toShape3FCone3FArray() {
		return Floats.toArray(this.distinctCone3Fs, cone3F -> cone3F.toArray(), 1);
	}
	
	public float[] toShape3FCylinder3FArray() {
		return Floats.toArray(this.distinctCylinder3Fs, cylinder3F -> cylinder3F.toArray(), 1);
	}
	
	public float[] toShape3FDisk3FArray() {
		return Floats.toArray(this.distinctDisk3Fs, disk3F -> disk3F.toArray(), 1);
	}
	
	public float[] toShape3FParaboloid3FArray() {
		return Floats.toArray(this.distinctParaboloid3Fs, paraboloid3F -> paraboloid3F.toArray(), 1);
	}
	
	public float[] toShape3FPlane3FArray() {
		return Floats.toArray(this.distinctPlane3Fs, plane3F -> plane3F.toArray(), 1);
	}
	
	public float[] toShape3FRectangle3FArray() {
		return Floats.toArray(this.distinctRectangle3Fs, rectangle3F -> rectangle3F.toArray(), 1);
	}
	
	public float[] toShape3FRectangularCuboid3FArray() {
		return Floats.toArray(this.distinctRectangularCuboid3Fs, rectangularCuboid3F -> rectangularCuboid3F.toArray(), 1);
	}
	
	public float[] toShape3FSphere3FArray() {
		return Floats.toArray(this.distinctSphere3Fs, sphere3F -> sphere3F.toArray(), 1);
	}
	
	public float[] toShape3FTorus3FArray() {
		return Floats.toArray(this.distinctTorus3Fs, torus3F -> torus3F.toArray(), 1);
	}
	
	public float[] toShape3FTriangle3FArray() {
		return Floats.toArray(this.distinctTriangle3Fs, triangle3F -> triangle3F.toArray(), 1);
	}
	
	public int findOffsetFor(final Shape3F shape3F) {
		Objects.requireNonNull(shape3F, "shape3F == null");
		
		if(shape3F instanceof Cone3F) {
			return this.distinctToOffsetsCone3Fs.get(shape3F).intValue();
		} else if(shape3F instanceof Cylinder3F) {
			return this.distinctToOffsetsCylinder3Fs.get(shape3F).intValue();
		} else if(shape3F instanceof Disk3F) {
			return this.distinctToOffsetsDisk3Fs.get(shape3F).intValue();
		} else if(shape3F instanceof Paraboloid3F) {
			return this.distinctToOffsetsParaboloid3Fs.get(shape3F).intValue();
		} else if(shape3F instanceof Plane3F) {
			return this.distinctToOffsetsPlane3Fs.get(shape3F).intValue();
		} else if(shape3F instanceof Rectangle3F) {
			return this.distinctToOffsetsRectangle3Fs.get(shape3F).intValue();
		} else if(shape3F instanceof RectangularCuboid3F) {
			return this.distinctToOffsetsRectangularCuboid3Fs.get(shape3F).intValue();
		} else if(shape3F instanceof Sphere3F) {
			return this.distinctToOffsetsSphere3Fs.get(shape3F).intValue();
		} else if(shape3F instanceof Torus3F) {
			return this.distinctToOffsetsTorus3Fs.get(shape3F).intValue();
		} else if(shape3F instanceof Triangle3F) {
			return this.distinctToOffsetsTriangle3Fs.get(shape3F).intValue();
		} else if(shape3F instanceof TriangleMesh3F) {
			return this.distinctToOffsetsTriangleMesh3Fs.get(shape3F).intValue();
		} else {
			return 0;
		}
	}
	
	public int[] toShape3FTriangleMesh3FArray(final BoundingVolume3FCache boundingVolume3FCache) {
		Objects.requireNonNull(boundingVolume3FCache, "boundingVolume3FCache == null");
		
		final int[] shape3FTriangleMesh3FArray = Ints.toArray(this.distinctTriangleMesh3Fs, triangleMesh3F -> triangleMesh3F.toArray(), 1);
		
		for(int i = 0; i < this.distinctTriangleMesh3Fs.size(); i++) {
			final TriangleMesh3F triangleMesh = this.distinctTriangleMesh3Fs.get(i);
			
			final List<BoundingVolume3F> boundingVolumes = triangleMesh.getBoundingVolumes();
			final List<Triangle3F> triangles = triangleMesh.getTriangles();
			
			final int shape3FTriangleMesh3FArrayOffset = this.distinctToOffsetsTriangleMesh3Fs.get(triangleMesh).intValue();
			final int shape3FTriangleMesh3FArrayLength = shape3FTriangleMesh3FArrayOffset + triangleMesh.getArrayLength();
			
			for(int j = shape3FTriangleMesh3FArrayOffset; j < shape3FTriangleMesh3FArrayLength;) {
				final int boundingVolumeOffset = j + TriangleMesh3F.ARRAY_OFFSET_BOUNDING_VOLUME_OFFSET;
				final int idOffset = j + TriangleMesh3F.ARRAY_OFFSET_ID;
				final int id = shape3FTriangleMesh3FArray[idOffset];
				
				shape3FTriangleMesh3FArray[boundingVolumeOffset] = boundingVolume3FCache.findOffsetFor(boundingVolumes.get(shape3FTriangleMesh3FArray[boundingVolumeOffset]));
				
				if(id == TriangleMesh3F.ID_LEAF_B_V_H_NODE) {
					final int triangleCountOffset = j + TriangleMesh3F.ARRAY_OFFSET_TRIANGLE_COUNT;
					final int triangleCount = shape3FTriangleMesh3FArray[triangleCountOffset];
					final int triangleStartOffset = triangleCountOffset + 1;
					
					for(int k = triangleStartOffset; k < triangleStartOffset + triangleCount; k++) {
						shape3FTriangleMesh3FArray[k] = this.distinctToOffsetsTriangle3Fs.get(triangles.get(shape3FTriangleMesh3FArray[k])).intValue();
					}
					
					j += 4 + triangleCount + padding(4 + triangleCount);
				} else if(id == TriangleMesh3F.ID_TREE_B_V_H_NODE) {
					j += 8;
				} else {
					break;
				}
			}
		}
		
		return shape3FTriangleMesh3FArray;
	}
	
	public void clear() {
		this.distinctCone3Fs.clear();
		this.distinctCylinder3Fs.clear();
		this.distinctDisk3Fs.clear();
		this.distinctParaboloid3Fs.clear();
		this.distinctPlane3Fs.clear();
		this.distinctRectangle3Fs.clear();
		this.distinctRectangularCuboid3Fs.clear();
		this.distinctShape3Fs.clear();
		this.distinctSphere3Fs.clear();
		this.distinctTorus3Fs.clear();
		this.distinctTriangle3Fs.clear();
		this.distinctTriangleMesh3Fs.clear();
		this.distinctToOffsetsCone3Fs.clear();
		this.distinctToOffsetsCylinder3Fs.clear();
		this.distinctToOffsetsDisk3Fs.clear();
		this.distinctToOffsetsParaboloid3Fs.clear();
		this.distinctToOffsetsPlane3Fs.clear();
		this.distinctToOffsetsRectangle3Fs.clear();
		this.distinctToOffsetsRectangularCuboid3Fs.clear();
		this.distinctToOffsetsSphere3Fs.clear();
		this.distinctToOffsetsTorus3Fs.clear();
		this.distinctToOffsetsTriangle3Fs.clear();
		this.distinctToOffsetsTriangleMesh3Fs.clear();
	}
	
	public void setup(final Scene scene) {
		Objects.requireNonNull(scene, "scene == null");
		
//		Add all distinct Shape3F instances:
		this.distinctShape3Fs.clear();
		this.distinctShape3Fs.addAll(NodeFilter.filterAllDistinct(scene, Shape3F.class).stream().filter(Shape3FCache::doFilterShape3F).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct Cone3F instances:
		this.distinctCone3Fs.clear();
		this.distinctCone3Fs.addAll(this.distinctShape3Fs.stream().filter(shape3F -> shape3F instanceof Cone3F).map(shape3F -> Cone3F.class.cast(shape3F)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct Cylinder3F instances:
		this.distinctCylinder3Fs.clear();
		this.distinctCylinder3Fs.addAll(this.distinctShape3Fs.stream().filter(shape3F -> shape3F instanceof Cylinder3F).map(shape3F -> Cylinder3F.class.cast(shape3F)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct Disk3F instances:
		this.distinctDisk3Fs.clear();
		this.distinctDisk3Fs.addAll(this.distinctShape3Fs.stream().filter(shape3F -> shape3F instanceof Disk3F).map(shape3F -> Disk3F.class.cast(shape3F)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct Paraboloid3F instances:
		this.distinctParaboloid3Fs.clear();
		this.distinctParaboloid3Fs.addAll(this.distinctShape3Fs.stream().filter(shape3F -> shape3F instanceof Paraboloid3F).map(shape3F -> Paraboloid3F.class.cast(shape3F)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct Plane3F instances:
		this.distinctPlane3Fs.clear();
		this.distinctPlane3Fs.addAll(this.distinctShape3Fs.stream().filter(shape3F -> shape3F instanceof Plane3F).map(shape3F -> Plane3F.class.cast(shape3F)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct Rectangle3F instances:
		this.distinctRectangle3Fs.clear();
		this.distinctRectangle3Fs.addAll(this.distinctShape3Fs.stream().filter(shape3F -> shape3F instanceof Rectangle3F).map(shape3F -> Rectangle3F.class.cast(shape3F)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct RectangularCuboid3F instances:
		this.distinctRectangularCuboid3Fs.clear();
		this.distinctRectangularCuboid3Fs.addAll(this.distinctShape3Fs.stream().filter(shape3F -> shape3F instanceof RectangularCuboid3F).map(shape3F -> RectangularCuboid3F.class.cast(shape3F)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct Sphere3F instances:
		this.distinctSphere3Fs.clear();
		this.distinctSphere3Fs.addAll(this.distinctShape3Fs.stream().filter(shape3F -> shape3F instanceof Sphere3F).map(shape3F -> Sphere3F.class.cast(shape3F)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct Torus3F instances:
		this.distinctTorus3Fs.clear();
		this.distinctTorus3Fs.addAll(this.distinctShape3Fs.stream().filter(shape3F -> shape3F instanceof Torus3F).map(shape3F -> Torus3F.class.cast(shape3F)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct Triangle3F instances:
		this.distinctTriangle3Fs.clear();
		this.distinctTriangle3Fs.addAll(this.distinctShape3Fs.stream().filter(shape3F -> shape3F instanceof Triangle3F).map(shape3F -> Triangle3F.class.cast(shape3F)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct TriangleMesh3F instances:
		this.distinctTriangleMesh3Fs.clear();
		this.distinctTriangleMesh3Fs.addAll(this.distinctShape3Fs.stream().filter(shape3F -> shape3F instanceof TriangleMesh3F).map(shape3F -> TriangleMesh3F.class.cast(shape3F)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Create offset mappings for all distinct Cone3F instances:
		this.distinctToOffsetsCone3Fs.clear();
		this.distinctToOffsetsCone3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctCone3Fs, Cone3F.ARRAY_LENGTH));
		
//		Create offset mappings for all distinct Cylinder3F instances:
		this.distinctToOffsetsCylinder3Fs.clear();
		this.distinctToOffsetsCylinder3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctCylinder3Fs, Cylinder3F.ARRAY_LENGTH));
		
//		Create offset mappings for all distinct Disk3F instances:
		this.distinctToOffsetsDisk3Fs.clear();
		this.distinctToOffsetsDisk3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctDisk3Fs, Disk3F.ARRAY_LENGTH));
		
//		Create offset mappings for all distinct Paraboloid3F instances:
		this.distinctToOffsetsParaboloid3Fs.clear();
		this.distinctToOffsetsParaboloid3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctParaboloid3Fs, Paraboloid3F.ARRAY_LENGTH));
		
//		Create offset mappings for all distinct Plane3F instances:
		this.distinctToOffsetsPlane3Fs.clear();
		this.distinctToOffsetsPlane3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctPlane3Fs, Plane3F.ARRAY_LENGTH));
		
//		Create offset mappings for all distinct Rectangle3F instances:
		this.distinctToOffsetsRectangle3Fs.clear();
		this.distinctToOffsetsRectangle3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctRectangle3Fs, Rectangle3F.ARRAY_LENGTH));
		
//		Create offset mappings for all distinct RectangularCuboid3F instances:
		this.distinctToOffsetsRectangularCuboid3Fs.clear();
		this.distinctToOffsetsRectangularCuboid3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctRectangularCuboid3Fs, RectangularCuboid3F.ARRAY_LENGTH));
		
//		Create offset mappings for all distinct Sphere3F instances:
		this.distinctToOffsetsSphere3Fs.clear();
		this.distinctToOffsetsSphere3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctSphere3Fs, Sphere3F.ARRAY_LENGTH));
		
//		Create offset mappings for all distinct Torus3F instances:
		this.distinctToOffsetsTorus3Fs.clear();
		this.distinctToOffsetsTorus3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctTorus3Fs, Torus3F.ARRAY_LENGTH));
		
//		Create offset mappings for all distinct Triangle3F instances:
		this.distinctToOffsetsTriangle3Fs.clear();
		this.distinctToOffsetsTriangle3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctTriangle3Fs, Triangle3F.ARRAY_LENGTH));
		
//		Create offset mappings for all distinct TriangleMesh3F instances:
		this.distinctToOffsetsTriangleMesh3Fs.clear();
		this.distinctToOffsetsTriangleMesh3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctTriangleMesh3Fs, triangleMesh3F -> triangleMesh3F.getArrayLength()));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static boolean doFilterShape3F(final Shape3F shape3F) {
		if(shape3F instanceof Cone3F) {
			return true;
		} else if(shape3F instanceof Cylinder3F) {
			return true;
		} else if(shape3F instanceof Disk3F) {
			return true;
		} else if(shape3F instanceof Paraboloid3F) {
			return true;
		} else if(shape3F instanceof Plane3F) {
			return true;
		} else if(shape3F instanceof Rectangle3F) {
			return true;
		} else if(shape3F instanceof RectangularCuboid3F) {
			return true;
		} else if(shape3F instanceof Sphere3F) {
			return true;
		} else if(shape3F instanceof Torus3F) {
			return true;
		} else if(shape3F instanceof Triangle3F) {
			return true;
		} else if(shape3F instanceof TriangleMesh3F) {
			return true;
		} else {
			return false;
		}
	}
}