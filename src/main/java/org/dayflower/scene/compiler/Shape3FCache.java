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

import org.dayflower.geometry.Shape3F;
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
import org.dayflower.geometry.shape.TriangleMesh3F;
import org.dayflower.node.Node;
import org.dayflower.node.NodeCache;
import org.dayflower.node.NodeFilter;

final class Shape3FCache {
	private final BoundingVolume3FCache boundingVolume3FCache;
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
	
	public Shape3FCache(final NodeCache nodeCache, final BoundingVolume3FCache boundingVolume3FCache) {
		this.nodeCache = Objects.requireNonNull(nodeCache, "nodeCache == null");
		this.boundingVolume3FCache = Objects.requireNonNull(boundingVolume3FCache, "boundingVolume3FCache == null");
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
	
	public float[] toCone3Fs() {
		return CompiledShape3FCache.toCone3Fs(this.distinctCone3Fs);
	}
	
	public float[] toCylinder3Fs() {
		return CompiledShape3FCache.toCylinder3Fs(this.distinctCylinder3Fs);
	}
	
	public float[] toDisk3Fs() {
		return CompiledShape3FCache.toDisk3Fs(this.distinctDisk3Fs);
	}
	
	public float[] toHyperboloid3Fs() {
		return CompiledShape3FCache.toHyperboloid3Fs(this.distinctHyperboloid3Fs);
	}
	
	public float[] toParaboloid3Fs() {
		return CompiledShape3FCache.toParaboloid3Fs(this.distinctParaboloid3Fs);
	}
	
	public float[] toPlane3Fs() {
		return CompiledShape3FCache.toPlane3Fs(this.distinctPlane3Fs);
	}
	
	public float[] toRectangle3Fs() {
		return CompiledShape3FCache.toRectangle3Fs(this.distinctRectangle3Fs);
	}
	
	public float[] toRectangularCuboid3Fs() {
		return CompiledShape3FCache.toRectangularCuboid3Fs(this.distinctRectangularCuboid3Fs);
	}
	
	public float[] toSphere3Fs() {
		return CompiledShape3FCache.toSphere3Fs(this.distinctSphere3Fs);
	}
	
	public float[] toTorus3Fs() {
		return CompiledShape3FCache.toTorus3Fs(this.distinctTorus3Fs);
	}
	
	public float[] toTriangle3Fs() {
		return CompiledShape3FCache.toTriangle3Fs(this.distinctTriangle3Fs);
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
	
	public int findOffsetFor(final Triangle3F triangle3F) {
		Objects.requireNonNull(triangle3F, "triangle3F == null");
		
		return this.distinctToOffsetsTriangle3Fs.get(triangle3F).intValue();
	}
	
	public int[] toTriangleMesh3Fs() {
		return CompiledShape3FCache.toTriangleMesh3Fs(this.distinctTriangleMesh3Fs, this.boundingVolume3FCache::findOffsetFor, this::findOffsetFor);
	}
	
	public void build(final CompiledScene compiledScene) {
		build(compiledScene.getCompiledShape3FCache());
	}
	
	public void build(final CompiledShape3FCache compiledShape3FCache) {
		compiledShape3FCache.setCone3Fs(toCone3Fs());
		compiledShape3FCache.setCylinder3Fs(toCylinder3Fs());
		compiledShape3FCache.setDisk3Fs(toDisk3Fs());
		compiledShape3FCache.setHyperboloid3Fs(toHyperboloid3Fs());
		compiledShape3FCache.setParaboloid3Fs(toParaboloid3Fs());
		compiledShape3FCache.setPlane3Fs(toPlane3Fs());
		compiledShape3FCache.setRectangle3Fs(toRectangle3Fs());
		compiledShape3FCache.setRectangularCuboid3Fs(toRectangularCuboid3Fs());
		compiledShape3FCache.setSphere3Fs(toSphere3Fs());
		compiledShape3FCache.setTorus3Fs(toTorus3Fs());
		compiledShape3FCache.setTriangle3Fs(toTriangle3Fs());
		compiledShape3FCache.setTriangleMesh3Fs(toTriangleMesh3Fs());
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
	
	public void setup() {
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
		this.distinctToOffsetsTriangleMesh3Fs.putAll(NodeFilter.mapDistinctToOffsets(this.distinctTriangleMesh3Fs, triangleMesh3F -> CompiledShape3FCache.getTriangleMesh3FLength(triangleMesh3F)));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static boolean filter(final Node node) {
		return node instanceof Shape3F;
	}
}