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
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.boundingvolume.hierarchy.BVHNode3F;
import org.dayflower.geometry.boundingvolume.hierarchy.LeafBVHNode3F;
import org.dayflower.geometry.boundingvolume.hierarchy.TreeBVHNode3F;
import org.dayflower.geometry.shape.Cone3F;
import org.dayflower.geometry.shape.Cylinder3F;
import org.dayflower.geometry.shape.Disk3F;
import org.dayflower.geometry.shape.Hyperboloid3F;
import org.dayflower.geometry.shape.Paraboloid3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.geometry.shape.Rectangle3F;
import org.dayflower.geometry.shape.RectangularCuboid3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Torus3F;
import org.dayflower.geometry.shape.Triangle3F;
import org.dayflower.geometry.shape.Triangle3F.Vertex3F;
import org.dayflower.java.io.IntArrayOutputStream;
import org.dayflower.geometry.shape.TriangleMesh3F;
import org.dayflower.node.Node;
import org.dayflower.node.NodeCache;
import org.dayflower.node.NodeFilter;
import org.dayflower.scene.Scene;
import org.dayflower.utility.Floats;
import org.dayflower.utility.Ints;
import org.dayflower.utility.ParameterArguments;

final class Shape3FCache {
	private final List<Cone3F> distinctCone3Fs;
	private final List<Cylinder3F> distinctCylinder3Fs;
	private final List<Disk3F> distinctDisk3Fs;
	private final List<Hyperboloid3F> distinctHyperboloid3Fs;
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
	private final Map<Hyperboloid3F, Integer> distinctToOffsetsHyperboloid3Fs;
	private final Map<Paraboloid3F, Integer> distinctToOffsetsParaboloid3Fs;
	private final Map<Plane3F, Integer> distinctToOffsetsPlane3Fs;
	private final Map<Rectangle3F, Integer> distinctToOffsetsRectangle3Fs;
	private final Map<RectangularCuboid3F, Integer> distinctToOffsetsRectangularCuboid3Fs;
	private final Map<Sphere3F, Integer> distinctToOffsetsSphere3Fs;
	private final Map<Torus3F, Integer> distinctToOffsetsTorus3Fs;
	private final Map<Triangle3F, Integer> distinctToOffsetsTriangle3Fs;
	private final Map<TriangleMesh3F, Integer> distinctToOffsetsTriangleMesh3Fs;
	private final NodeCache nodeCache;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Shape3FCache(final NodeCache nodeCache) {
		this.nodeCache = nodeCache;
		this.distinctCone3Fs = new ArrayList<>();
		this.distinctCylinder3Fs = new ArrayList<>();
		this.distinctDisk3Fs = new ArrayList<>();
		this.distinctHyperboloid3Fs = new ArrayList<>();
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
		this.distinctToOffsetsHyperboloid3Fs = new LinkedHashMap<>();
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
		return Floats.toArray(this.distinctCone3Fs, cone3F -> doToArray(cone3F), 1);
	}
	
	public float[] toShape3FCylinder3FArray() {
		return Floats.toArray(this.distinctCylinder3Fs, cylinder3F -> doToArray(cylinder3F), 1);
	}
	
	public float[] toShape3FDisk3FArray() {
		return Floats.toArray(this.distinctDisk3Fs, disk3F -> doToArray(disk3F), 1);
	}
	
	public float[] toShape3FHyperboloid3FArray() {
		return Floats.toArray(this.distinctHyperboloid3Fs, hyperboloid3F -> doToArray(hyperboloid3F), 1);
	}
	
	public float[] toShape3FParaboloid3FArray() {
		return Floats.toArray(this.distinctParaboloid3Fs, paraboloid3F -> doToArray(paraboloid3F), 1);
	}
	
	public float[] toShape3FPlane3FArray() {
		return Floats.toArray(this.distinctPlane3Fs, plane3F -> doToArray(plane3F), 1);
	}
	
	public float[] toShape3FRectangle3FArray() {
		return Floats.toArray(this.distinctRectangle3Fs, rectangle3F -> doToArray(rectangle3F), 1);
	}
	
	public float[] toShape3FRectangularCuboid3FArray() {
		return Floats.toArray(this.distinctRectangularCuboid3Fs, rectangularCuboid3F -> doToArray(rectangularCuboid3F), 1);
	}
	
	public float[] toShape3FSphere3FArray() {
		return Floats.toArray(this.distinctSphere3Fs, sphere3F -> doToArray(sphere3F), 1);
	}
	
	public float[] toShape3FTorus3FArray() {
		return Floats.toArray(this.distinctTorus3Fs, torus3F -> doToArray(torus3F), 1);
	}
	
	public float[] toShape3FTriangle3FArray() {
		return Floats.toArray(this.distinctTriangle3Fs, triangle3F -> doToArray(triangle3F), 1);
	}
	
	public int findOffsetFor(final Shape3F shape3F) {
		Objects.requireNonNull(shape3F, "shape3F == null");
		
		if(shape3F instanceof Cone3F) {
			return this.distinctToOffsetsCone3Fs.get(shape3F).intValue();
		} else if(shape3F instanceof Cylinder3F) {
			return this.distinctToOffsetsCylinder3Fs.get(shape3F).intValue();
		} else if(shape3F instanceof Disk3F) {
			return this.distinctToOffsetsDisk3Fs.get(shape3F).intValue();
		} else if(shape3F instanceof Hyperboloid3F) {
			return this.distinctToOffsetsHyperboloid3Fs.get(shape3F).intValue();
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
		
		final int[] shape3FTriangleMesh3FArray = Ints.toArray(this.distinctTriangleMesh3Fs, triangleMesh3F -> doToArray(triangleMesh3F), 1);
		
		for(int i = 0; i < this.distinctTriangleMesh3Fs.size(); i++) {
			final TriangleMesh3F triangleMesh = this.distinctTriangleMesh3Fs.get(i);
			
			final List<BoundingVolume3F> boundingVolumes = triangleMesh.getBoundingVolumes();
			final List<Triangle3F> triangles = triangleMesh.getTriangles();
			
			final int shape3FTriangleMesh3FArrayOffset = this.distinctToOffsetsTriangleMesh3Fs.get(triangleMesh).intValue();
			final int shape3FTriangleMesh3FArrayLength = shape3FTriangleMesh3FArrayOffset + doGetArrayLength(triangleMesh);
			
			for(int j = shape3FTriangleMesh3FArrayOffset; j < shape3FTriangleMesh3FArrayLength;) {
				final int boundingVolumeOffset = j + CompiledShape3FCache.TRIANGLE_MESH_3_F_B_V_H_NODE_3_F_OFFSET_BOUNDING_VOLUME_OFFSET;
				final int idOffset = j + CompiledShape3FCache.TRIANGLE_MESH_3_F_B_V_H_NODE_3_F_OFFSET_ID;
				final int id = shape3FTriangleMesh3FArray[idOffset];
				
				shape3FTriangleMesh3FArray[boundingVolumeOffset] = boundingVolume3FCache.findOffsetFor(boundingVolumes.get(shape3FTriangleMesh3FArray[boundingVolumeOffset]));
				
				if(id == LeafBVHNode3F.ID) {
					final int triangleCountOffset = j + CompiledShape3FCache.TRIANGLE_MESH_3_F_LEAF_B_V_H_NODE_3_F_OFFSET_SHAPE_COUNT;
					final int triangleCount = shape3FTriangleMesh3FArray[triangleCountOffset];
					final int triangleStartOffset = triangleCountOffset + 1;
					
					for(int k = triangleStartOffset; k < triangleStartOffset + triangleCount; k++) {
						shape3FTriangleMesh3FArray[k] = this.distinctToOffsetsTriangle3Fs.get(triangles.get(shape3FTriangleMesh3FArray[k])).intValue();
					}
					
					j += 4 + triangleCount + padding(4 + triangleCount);
				} else if(id == TreeBVHNode3F.ID) {
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
		this.distinctHyperboloid3Fs.clear();
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
		this.distinctToOffsetsHyperboloid3Fs.clear();
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
		if(this.nodeCache != null) {
			doSetupNew(scene);
		} else {
			doSetupOld(scene);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static boolean filter(final Node node) {
		return node instanceof Shape3F;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doSetupNew(final Scene scene) {
		Objects.requireNonNull(scene, "scene == null");
		
//		Add all distinct Cone3F instances:
		this.distinctCone3Fs.clear();
		this.distinctCone3Fs.addAll(this.nodeCache.getAllDistinct(Cone3F.class));
		
//		Add all distinct Cylinder3F instances:
		this.distinctCylinder3Fs.clear();
		this.distinctCylinder3Fs.addAll(this.nodeCache.getAllDistinct(Cylinder3F.class));
		
//		Add all distinct Disk3F instances:
		this.distinctDisk3Fs.clear();
		this.distinctDisk3Fs.addAll(this.nodeCache.getAllDistinct(Disk3F.class));
		
//		Add all distinct Hyperboloid3F instances:
		this.distinctHyperboloid3Fs.clear();
		this.distinctHyperboloid3Fs.addAll(this.nodeCache.getAllDistinct(Hyperboloid3F.class));
		
//		Add all distinct Paraboloid3F instances:
		this.distinctParaboloid3Fs.clear();
		this.distinctParaboloid3Fs.addAll(this.nodeCache.getAllDistinct(Paraboloid3F.class));
		
//		Add all distinct Plane3F instances:
		this.distinctPlane3Fs.clear();
		this.distinctPlane3Fs.addAll(this.nodeCache.getAllDistinct(Plane3F.class));
		
//		Add all distinct Rectangle3F instances:
		this.distinctRectangle3Fs.clear();
		this.distinctRectangle3Fs.addAll(this.nodeCache.getAllDistinct(Rectangle3F.class));
		
//		Add all distinct RectangularCuboid3F instances:
		this.distinctRectangularCuboid3Fs.clear();
		this.distinctRectangularCuboid3Fs.addAll(this.nodeCache.getAllDistinct(RectangularCuboid3F.class));
		
//		Add all distinct Sphere3F instances:
		this.distinctSphere3Fs.clear();
		this.distinctSphere3Fs.addAll(this.nodeCache.getAllDistinct(Sphere3F.class));
		
//		Add all distinct Torus3F instances:
		this.distinctTorus3Fs.clear();
		this.distinctTorus3Fs.addAll(this.nodeCache.getAllDistinct(Torus3F.class));
		
//		Add all distinct Triangle3F instances:
		this.distinctTriangle3Fs.clear();
		this.distinctTriangle3Fs.addAll(this.nodeCache.getAllDistinct(Triangle3F.class));
		
//		Add all distinct TriangleMesh3F instances:
		this.distinctTriangleMesh3Fs.clear();
		this.distinctTriangleMesh3Fs.addAll(this.nodeCache.getAllDistinct(TriangleMesh3F.class));
		
//		Add all distinct Shape3F instances:
		this.distinctShape3Fs.clear();
		this.distinctShape3Fs.addAll(this.distinctCone3Fs);
		this.distinctShape3Fs.addAll(this.distinctCylinder3Fs);
		this.distinctShape3Fs.addAll(this.distinctDisk3Fs);
		this.distinctShape3Fs.addAll(this.distinctHyperboloid3Fs);
		this.distinctShape3Fs.addAll(this.distinctParaboloid3Fs);
		this.distinctShape3Fs.addAll(this.distinctPlane3Fs);
		this.distinctShape3Fs.addAll(this.distinctRectangle3Fs);
		this.distinctShape3Fs.addAll(this.distinctRectangularCuboid3Fs);
		this.distinctShape3Fs.addAll(this.distinctSphere3Fs);
		this.distinctShape3Fs.addAll(this.distinctTorus3Fs);
		this.distinctShape3Fs.addAll(this.distinctTriangle3Fs);
		this.distinctShape3Fs.addAll(this.distinctTriangleMesh3Fs);
		
//		Create offset mappings for all distinct Cone3F instances:
		this.distinctToOffsetsCone3Fs.clear();
		this.distinctToOffsetsCone3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctCone3Fs, CompiledShape3FCache.CONE_3_F_LENGTH));
		
//		Create offset mappings for all distinct Cylinder3F instances:
		this.distinctToOffsetsCylinder3Fs.clear();
		this.distinctToOffsetsCylinder3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctCylinder3Fs, CompiledShape3FCache.CYLINDER_3_F_LENGTH));
		
//		Create offset mappings for all distinct Disk3F instances:
		this.distinctToOffsetsDisk3Fs.clear();
		this.distinctToOffsetsDisk3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctDisk3Fs, CompiledShape3FCache.DISK_3_F_LENGTH));
		
//		Create offset mappings for all distinct Hyperboloid3F instances:
		this.distinctToOffsetsHyperboloid3Fs.clear();
		this.distinctToOffsetsHyperboloid3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctHyperboloid3Fs, CompiledShape3FCache.HYPERBOLOID_3_F_LENGTH));
		
//		Create offset mappings for all distinct Paraboloid3F instances:
		this.distinctToOffsetsParaboloid3Fs.clear();
		this.distinctToOffsetsParaboloid3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctParaboloid3Fs, CompiledShape3FCache.PARABOLOID_3_F_LENGTH));
		
//		Create offset mappings for all distinct Plane3F instances:
		this.distinctToOffsetsPlane3Fs.clear();
		this.distinctToOffsetsPlane3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctPlane3Fs, CompiledShape3FCache.PLANE_3_F_LENGTH));
		
//		Create offset mappings for all distinct Rectangle3F instances:
		this.distinctToOffsetsRectangle3Fs.clear();
		this.distinctToOffsetsRectangle3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctRectangle3Fs, CompiledShape3FCache.RECTANGLE_3_F_LENGTH));
		
//		Create offset mappings for all distinct RectangularCuboid3F instances:
		this.distinctToOffsetsRectangularCuboid3Fs.clear();
		this.distinctToOffsetsRectangularCuboid3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctRectangularCuboid3Fs, CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_LENGTH));
		
//		Create offset mappings for all distinct Sphere3F instances:
		this.distinctToOffsetsSphere3Fs.clear();
		this.distinctToOffsetsSphere3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctSphere3Fs, CompiledShape3FCache.SPHERE_3_F_LENGTH));
		
//		Create offset mappings for all distinct Torus3F instances:
		this.distinctToOffsetsTorus3Fs.clear();
		this.distinctToOffsetsTorus3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctTorus3Fs, CompiledShape3FCache.TORUS_3_F_LENGTH));
		
//		Create offset mappings for all distinct Triangle3F instances:
		this.distinctToOffsetsTriangle3Fs.clear();
		this.distinctToOffsetsTriangle3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctTriangle3Fs, CompiledShape3FCache.TRIANGLE_3_F_LENGTH));
		
//		Create offset mappings for all distinct TriangleMesh3F instances:
		this.distinctToOffsetsTriangleMesh3Fs.clear();
		this.distinctToOffsetsTriangleMesh3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctTriangleMesh3Fs, triangleMesh3F -> doGetArrayLength(triangleMesh3F)));
	}
	
	private void doSetupOld(final Scene scene) {
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
		
//		Add all distinct Hyperboloid3F instances:
		this.distinctHyperboloid3Fs.clear();
		this.distinctHyperboloid3Fs.addAll(this.distinctShape3Fs.stream().filter(shape3F -> shape3F instanceof Hyperboloid3F).map(shape3F -> Hyperboloid3F.class.cast(shape3F)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
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
		this.distinctToOffsetsCone3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctCone3Fs, CompiledShape3FCache.CONE_3_F_LENGTH));
		
//		Create offset mappings for all distinct Cylinder3F instances:
		this.distinctToOffsetsCylinder3Fs.clear();
		this.distinctToOffsetsCylinder3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctCylinder3Fs, CompiledShape3FCache.CYLINDER_3_F_LENGTH));
		
//		Create offset mappings for all distinct Disk3F instances:
		this.distinctToOffsetsDisk3Fs.clear();
		this.distinctToOffsetsDisk3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctDisk3Fs, CompiledShape3FCache.DISK_3_F_LENGTH));
		
//		Create offset mappings for all distinct Hyperboloid3F instances:
		this.distinctToOffsetsHyperboloid3Fs.clear();
		this.distinctToOffsetsHyperboloid3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctHyperboloid3Fs, CompiledShape3FCache.HYPERBOLOID_3_F_LENGTH));
		
//		Create offset mappings for all distinct Paraboloid3F instances:
		this.distinctToOffsetsParaboloid3Fs.clear();
		this.distinctToOffsetsParaboloid3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctParaboloid3Fs, CompiledShape3FCache.PARABOLOID_3_F_LENGTH));
		
//		Create offset mappings for all distinct Plane3F instances:
		this.distinctToOffsetsPlane3Fs.clear();
		this.distinctToOffsetsPlane3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctPlane3Fs, CompiledShape3FCache.PLANE_3_F_LENGTH));
		
//		Create offset mappings for all distinct Rectangle3F instances:
		this.distinctToOffsetsRectangle3Fs.clear();
		this.distinctToOffsetsRectangle3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctRectangle3Fs, CompiledShape3FCache.RECTANGLE_3_F_LENGTH));
		
//		Create offset mappings for all distinct RectangularCuboid3F instances:
		this.distinctToOffsetsRectangularCuboid3Fs.clear();
		this.distinctToOffsetsRectangularCuboid3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctRectangularCuboid3Fs, CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_LENGTH));
		
//		Create offset mappings for all distinct Sphere3F instances:
		this.distinctToOffsetsSphere3Fs.clear();
		this.distinctToOffsetsSphere3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctSphere3Fs, CompiledShape3FCache.SPHERE_3_F_LENGTH));
		
//		Create offset mappings for all distinct Torus3F instances:
		this.distinctToOffsetsTorus3Fs.clear();
		this.distinctToOffsetsTorus3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctTorus3Fs, CompiledShape3FCache.TORUS_3_F_LENGTH));
		
//		Create offset mappings for all distinct Triangle3F instances:
		this.distinctToOffsetsTriangle3Fs.clear();
		this.distinctToOffsetsTriangle3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctTriangle3Fs, CompiledShape3FCache.TRIANGLE_3_F_LENGTH));
		
//		Create offset mappings for all distinct TriangleMesh3F instances:
		this.distinctToOffsetsTriangleMesh3Fs.clear();
		this.distinctToOffsetsTriangleMesh3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctTriangleMesh3Fs, triangleMesh3F -> doGetArrayLength(triangleMesh3F)));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static boolean doFilterShape3F(final Shape3F shape3F) {
		if(shape3F instanceof Cone3F) {
			return true;
		} else if(shape3F instanceof Cylinder3F) {
			return true;
		} else if(shape3F instanceof Disk3F) {
			return true;
		} else if(shape3F instanceof Hyperboloid3F) {
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
	
	private static float[] doToArray(final Cone3F cone3F) {
		final float phiMax = cone3F.getPhiMax().getRadians();
		final float radius = cone3F.getRadius();
		final float zMax = cone3F.getZMax();
		
		final float[] array = new float[CompiledShape3FCache.CONE_3_F_LENGTH];
		
		array[CompiledShape3FCache.CONE_3_F_OFFSET_PHI_MAX] = phiMax;
		array[CompiledShape3FCache.CONE_3_F_OFFSET_RADIUS] = radius;
		array[CompiledShape3FCache.CONE_3_F_OFFSET_Z_MAX] = zMax;
		array[3] = 0.0F;
		
		return array;
	}
	
	private static float[] doToArray(final Cylinder3F cylinder3F) {
		final float phiMax = cylinder3F.getPhiMax().getRadians();
		final float radius = cylinder3F.getRadius();
		final float zMax = cylinder3F.getZMax();
		final float zMin = cylinder3F.getZMin();
		
		final float[] array = new float[CompiledShape3FCache.CYLINDER_3_F_LENGTH];
		
		array[CompiledShape3FCache.CYLINDER_3_F_OFFSET_PHI_MAX] = phiMax;
		array[CompiledShape3FCache.CYLINDER_3_F_OFFSET_RADIUS] = radius;
		array[CompiledShape3FCache.CYLINDER_3_F_OFFSET_Z_MAX] = zMax;
		array[CompiledShape3FCache.CYLINDER_3_F_OFFSET_Z_MIN] = zMin;
		
		return array;
	}
	
	private static float[] doToArray(final Disk3F disk3F) {
		final float phiMax = disk3F.getPhiMax().getRadians();
		final float radiusInner = disk3F.getRadiusInner();
		final float radiusOuter = disk3F.getRadiusOuter();
		final float zMax = disk3F.getZMax();
		
		final float[] array = new float[CompiledShape3FCache.DISK_3_F_LENGTH];
		
		array[CompiledShape3FCache.DISK_3_F_OFFSET_PHI_MAX] = phiMax;
		array[CompiledShape3FCache.DISK_3_F_OFFSET_RADIUS_INNER] = radiusInner;
		array[CompiledShape3FCache.DISK_3_F_OFFSET_RADIUS_OUTER] = radiusOuter;
		array[CompiledShape3FCache.DISK_3_F_OFFSET_Z_MAX] = zMax;
		
		return array;
	}
	
	private static float[] doToArray(final Hyperboloid3F hyperboloid3F) {
		final Point3F a = hyperboloid3F.getA();
		final Point3F b = hyperboloid3F.getB();
		
		final float phiMax = hyperboloid3F.getPhiMax().getRadians();
		final float aH = hyperboloid3F.getAH();
		final float cH = hyperboloid3F.getCH();
		final float rMax = hyperboloid3F.getRMax();
		final float zMax = hyperboloid3F.getZMax();
		final float zMin = hyperboloid3F.getZMin();
		
		final float[] array = new float[CompiledShape3FCache.HYPERBOLOID_3_F_LENGTH];
		
		array[CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_PHI_MAX] = phiMax;
		array[CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_A + 0] = a.getX();
		array[CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_A + 1] = a.getY();
		array[CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_A + 2] = a.getZ();
		array[CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_B + 0] = b.getX();
		array[CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_B + 1] = b.getY();
		array[CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_B + 2] = b.getZ();
		array[CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_A_H] = aH;
		array[CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_C_H] = cH;
		array[CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_R_MAX] = rMax;
		array[CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_Z_MAX] = zMax;
		array[CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_Z_MIN] = zMin;
		
		return array;
	}
	
	private static float[] doToArray(final Paraboloid3F paraboloid3F) {
		final float phiMax = paraboloid3F.getPhiMax().getRadians();
		final float radius = paraboloid3F.getRadius();
		final float zMax = paraboloid3F.getZMax();
		final float zMin = paraboloid3F.getZMin();
		
		final float[] array = new float[CompiledShape3FCache.PARABOLOID_3_F_LENGTH];
		
		array[CompiledShape3FCache.PARABOLOID_3_F_OFFSET_PHI_MAX] = phiMax;
		array[CompiledShape3FCache.PARABOLOID_3_F_OFFSET_RADIUS] = radius;
		array[CompiledShape3FCache.PARABOLOID_3_F_OFFSET_Z_MAX] = zMax;
		array[CompiledShape3FCache.PARABOLOID_3_F_OFFSET_Z_MIN] = zMin;
		
		return array;
	}
	
	private static float[] doToArray(final Plane3F plane3F) {
		final Point3F a = plane3F.getA();
		final Point3F b = plane3F.getB();
		final Point3F c = plane3F.getC();
		
		final Vector3F surfaceNormal = plane3F.getSurfaceNormal();
		
		final float[] array = new float[CompiledShape3FCache.PLANE_3_F_LENGTH];
		
		array[CompiledShape3FCache.PLANE_3_F_OFFSET_A + 0] = a.getX();							//Block #1
		array[CompiledShape3FCache.PLANE_3_F_OFFSET_A + 1] = a.getY();							//Block #1
		array[CompiledShape3FCache.PLANE_3_F_OFFSET_A + 2] = a.getZ();							//Block #1
		array[CompiledShape3FCache.PLANE_3_F_OFFSET_B + 0] = b.getX();							//Block #1
		array[CompiledShape3FCache.PLANE_3_F_OFFSET_B + 1] = b.getY();							//Block #1
		array[CompiledShape3FCache.PLANE_3_F_OFFSET_B + 2] = b.getZ();							//Block #1
		array[CompiledShape3FCache.PLANE_3_F_OFFSET_C + 0] = c.getX();							//Block #1
		array[CompiledShape3FCache.PLANE_3_F_OFFSET_C + 1] = c.getY();							//Block #1
		array[CompiledShape3FCache.PLANE_3_F_OFFSET_C + 2] = c.getZ();							//Block #2
		array[CompiledShape3FCache.PLANE_3_F_OFFSET_SURFACE_NORMAL + 0] = surfaceNormal.getX();	//Block #2
		array[CompiledShape3FCache.PLANE_3_F_OFFSET_SURFACE_NORMAL + 1] = surfaceNormal.getY();	//Block #2
		array[CompiledShape3FCache.PLANE_3_F_OFFSET_SURFACE_NORMAL + 2] = surfaceNormal.getZ();	//Block #2
		array[12] = 0.0F;																		//Block #2
		array[13] = 0.0F;																		//Block #2
		array[14] = 0.0F;																		//Block #2
		array[15] = 0.0F;																		//Block #2
		
		return array;
	}
	
	private static float[] doToArray(final Rectangle3F rectangle3F) {
		final Point3F position = rectangle3F.getPosition();
		
		final Vector3F sideA = rectangle3F.getSideA();
		final Vector3F sideB = rectangle3F.getSideB();
		final Vector3F surfaceNormal = rectangle3F.getSurfaceNormal();
		
		final float[] array = new float[CompiledShape3FCache.RECTANGLE_3_F_LENGTH];
		
		array[CompiledShape3FCache.RECTANGLE_3_F_OFFSET_POSITION + 0] = position.getX();			//Block #1
		array[CompiledShape3FCache.RECTANGLE_3_F_OFFSET_POSITION + 1] = position.getY();			//Block #1
		array[CompiledShape3FCache.RECTANGLE_3_F_OFFSET_POSITION + 2] = position.getZ();			//Block #1
		array[CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_A + 0] = sideA.getX();					//Block #1
		array[CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_A + 1] = sideA.getY();					//Block #1
		array[CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_A + 2] = sideA.getZ();					//Block #1
		array[CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_B + 0] = sideB.getX();					//Block #1
		array[CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_B + 1] = sideB.getY();					//Block #1
		array[CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_B + 2] = sideB.getZ();					//Block #2
		array[CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SURFACE_NORMAL + 0] = surfaceNormal.getX();	//Block #2
		array[CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SURFACE_NORMAL + 1] = surfaceNormal.getY();	//Block #2
		array[CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SURFACE_NORMAL + 2] = surfaceNormal.getZ();	//Block #2
		array[12] = 0.0F;																			//Block #2
		array[13] = 0.0F;																			//Block #2
		array[14] = 0.0F;																			//Block #2
		array[15] = 0.0F;																			//Block #2
		
		return array;
	}
	
	private static float[] doToArray(final RectangularCuboid3F rectangularCuboid3F) {
		final Point3F maximum = rectangularCuboid3F.getMaximum();
		final Point3F minimum = rectangularCuboid3F.getMinimum();
		
		final float[] array = new float[CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_LENGTH];
		
		array[CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MAXIMUM + 0] = maximum.getX();	//Block #1
		array[CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MAXIMUM + 1] = maximum.getY();	//Block #1
		array[CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MAXIMUM + 2] = maximum.getZ();	//Block #1
		array[CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MINIMUM + 0] = minimum.getX();	//Block #1
		array[CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MINIMUM + 1] = minimum.getY();	//Block #1
		array[CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MINIMUM + 2] = minimum.getZ();	//Block #1
		array[6] = 0.0F;																		//Block #1
		array[7] = 0.0F;																		//Block #1
		
		return array;
	}
	
	private static float[] doToArray(final Sphere3F sphere3F) {
		final Point3F center = sphere3F.getCenter();
		
		final float radius = sphere3F.getRadius();
		
		final float[] array = new float[CompiledShape3FCache.SPHERE_3_F_LENGTH];
		
		array[CompiledShape3FCache.SPHERE_3_F_OFFSET_CENTER + 0] = center.getX();
		array[CompiledShape3FCache.SPHERE_3_F_OFFSET_CENTER + 1] = center.getY();
		array[CompiledShape3FCache.SPHERE_3_F_OFFSET_CENTER + 2] = center.getZ();
		array[CompiledShape3FCache.SPHERE_3_F_OFFSET_RADIUS] = radius;
		
		return array;
	}
	
	private static float[] doToArray(final Torus3F torus3F) {
		final float radiusInner = torus3F.getRadiusInner();
		final float radiusOuter = torus3F.getRadiusOuter();
		
		final float[] array = new float[CompiledShape3FCache.TORUS_3_F_LENGTH];
		
		array[CompiledShape3FCache.TORUS_3_F_OFFSET_RADIUS_INNER] = radiusInner;
		array[CompiledShape3FCache.TORUS_3_F_OFFSET_RADIUS_OUTER] = radiusOuter;
		
		return array;
	}
	
	private static float[] doToArray(final Triangle3F triangle3F) {
		final Vertex3F a = triangle3F.getA();
		final Vertex3F b = triangle3F.getB();
		final Vertex3F c = triangle3F.getC();
		
		final float[] array = new float[CompiledShape3FCache.TRIANGLE_3_F_LENGTH];
		
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_POSITION + 0] = a.getPosition().getX();								//Block #1
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_POSITION + 1] = a.getPosition().getY();								//Block #1
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_POSITION + 2] = a.getPosition().getZ();								//Block #1
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_POSITION + 0] = b.getPosition().getX();								//Block #1
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_POSITION + 1] = b.getPosition().getY();								//Block #1
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_POSITION + 2] = b.getPosition().getZ();								//Block #1
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_POSITION + 0] = c.getPosition().getX();								//Block #1
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_POSITION + 1] = c.getPosition().getY();								//Block #1
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_POSITION + 2] = c.getPosition().getZ();								//Block #2
		
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_TEXTURE_COORDINATES + 0] = a.getTextureCoordinates().getU();			//Block #2
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_TEXTURE_COORDINATES + 1] = a.getTextureCoordinates().getV();			//Block #2
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_TEXTURE_COORDINATES + 0] = b.getTextureCoordinates().getU();			//Block #2
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_TEXTURE_COORDINATES + 1] = b.getTextureCoordinates().getV();			//Block #2
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_TEXTURE_COORDINATES + 0] = c.getTextureCoordinates().getU();			//Block #2
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_TEXTURE_COORDINATES + 1] = c.getTextureCoordinates().getV();			//Block #2
		
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_ORTHONORMAL_BASIS_W + 0] = a.getOrthonormalBasis().getW().getX();		//Block #2
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_ORTHONORMAL_BASIS_W + 1] = a.getOrthonormalBasis().getW().getY();		//Block #3
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_ORTHONORMAL_BASIS_W + 2] = a.getOrthonormalBasis().getW().getZ();		//Block #3
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_ORTHONORMAL_BASIS_W + 0] = b.getOrthonormalBasis().getW().getX();		//Block #3
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_ORTHONORMAL_BASIS_W + 1] = b.getOrthonormalBasis().getW().getY();		//Block #3
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_ORTHONORMAL_BASIS_W + 2] = b.getOrthonormalBasis().getW().getZ();		//Block #3
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_ORTHONORMAL_BASIS_W + 0] = c.getOrthonormalBasis().getW().getX();		//Block #3
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_ORTHONORMAL_BASIS_W + 1] = c.getOrthonormalBasis().getW().getY();		//Block #3
		array[CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_ORTHONORMAL_BASIS_W + 2] = c.getOrthonormalBasis().getW().getZ();		//Block #3
		
		return array;
	}
	
	private static int doFindLeftOffset(final List<BVHNode3F> bVHNodes, final int depth, final int index, final int[] offsets) {
		for(int i = index; i < bVHNodes.size(); i++) {
			if(bVHNodes.get(i).getDepth() == depth + 1) {
				return offsets[i];
			}
		}
		
		return -1;
	}
	
	private static int doFindNextOffset(final List<BVHNode3F> bVHNodes, final int depth, final int index, final int[] offsets) {
		for(int i = index; i < bVHNodes.size(); i++) {
			if(bVHNodes.get(i).getDepth() <= depth) {
				return offsets[i];
			}
		}
		
		return -1;
	}
	
	private static int doGetArrayLength(final BVHNode3F bVHNode3F) {
		if(bVHNode3F instanceof LeafBVHNode3F) {
			final int a = 4;
			final int b = LeafBVHNode3F.class.cast(bVHNode3F).getShapeCount();
			final int c = padding(a + b);
			
			return a + b + c;
		} else if(bVHNode3F instanceof TreeBVHNode3F) {
			return 8;
		} else {
			return 0;
		}
	}
	
	private static int doGetArrayLength(final TriangleMesh3F triangleMesh3F) {
		final Optional<BVHNode3F> optionalRootBVHNode = triangleMesh3F.getRootBVHNode();
		
		if(optionalRootBVHNode.isPresent()) {
			final BVHNode3F rootBVHNode = optionalRootBVHNode.get();
			
			return NodeFilter.filterAll(rootBVHNode, BVHNode3F.class).stream().mapToInt(bVHNode -> doGetArrayLength(bVHNode)).sum();
		}
		
		return 0;
	}
	
	private static int[] doToArray(final BVHNode3F rootBVHNode, final List<Triangle3F> shapes) {
		Objects.requireNonNull(rootBVHNode, "rootBVHNode == null");
		
		ParameterArguments.requireNonNullList(shapes, "shapes");
		
		final List<BVHNode3F> bVHNodes = NodeFilter.filterAll(rootBVHNode, BVHNode3F.class);
		
		final int[] offsets = new int[bVHNodes.size()];
		
		for(int i = 0, j = 0; i < offsets.length; j += doGetArrayLength(bVHNodes.get(i)), i++) {
			offsets[i] = j;
		}
		
		try(final IntArrayOutputStream intArrayOutputStream = new IntArrayOutputStream()) {
			for(int i = 0; i < bVHNodes.size(); i++) {
				final BVHNode3F bVHNode = bVHNodes.get(i);
				
				if(bVHNode instanceof LeafBVHNode3F) {
					final LeafBVHNode3F<?> leafBVHNode = LeafBVHNode3F.class.cast(bVHNode);
					
					final int id = LeafBVHNode3F.ID;
					final int boundingVolumeOffset = i;
					final int nextOffset = doFindNextOffset(bVHNodes, leafBVHNode.getDepth(), i + 1, offsets);
					final int shapeCount = leafBVHNode.getShapeCount();
					
					intArrayOutputStream.writeInt(id);
					intArrayOutputStream.writeInt(boundingVolumeOffset);
					intArrayOutputStream.writeInt(nextOffset);
					intArrayOutputStream.writeInt(shapeCount);
					
					for(final Shape3F shape : leafBVHNode.getShapes()) {
						intArrayOutputStream.writeInt(shapes.indexOf(shape));
					}
					
					final int padding = padding(4 + shapeCount);
					
					for(int j = 0; j < padding; j++) {
						intArrayOutputStream.writeInt(0);
					}
				} else if(bVHNode instanceof TreeBVHNode3F) {
					final TreeBVHNode3F treeBVHNode = TreeBVHNode3F.class.cast(bVHNode);
					
					final int id = TreeBVHNode3F.ID;
					final int boundingVolumeOffset = i;
					final int nextOffset = doFindNextOffset(bVHNodes, treeBVHNode.getDepth(), i + 1, offsets);
					final int leftOffset = doFindLeftOffset(bVHNodes, treeBVHNode.getDepth(), i + 1, offsets);
					
					intArrayOutputStream.writeInt(id);
					intArrayOutputStream.writeInt(boundingVolumeOffset);
					intArrayOutputStream.writeInt(nextOffset);
					intArrayOutputStream.writeInt(leftOffset);
					intArrayOutputStream.writeInt(0);
					intArrayOutputStream.writeInt(0);
					intArrayOutputStream.writeInt(0);
					intArrayOutputStream.writeInt(0);
				}
			}
			
			return intArrayOutputStream.toIntArray();
		}
	}
	
	private static int[] doToArray(final TriangleMesh3F triangleMesh3F) {
		final Optional<BVHNode3F> optionalRootBVHNode = triangleMesh3F.getRootBVHNode();
		
		if(optionalRootBVHNode.isPresent()) {
			return doToArray(optionalRootBVHNode.get(), triangleMesh3F.getTriangles());
		}
		
		return new int[0];
	}
}