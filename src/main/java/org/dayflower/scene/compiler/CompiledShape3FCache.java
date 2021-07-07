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

import java.util.Objects;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
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
import org.dayflower.geometry.shape.TriangleMesh3F;
import org.dayflower.geometry.shape.Triangle3F.Vertex3F;

/**
 * A {@code CompiledShape3FCache} contains {@link Shape3F} instances in compiled form.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompiledShape3FCache {
	/**
	 * The length of a compiled {@link Cone3F} instance.
	 */
	public static final int CONE_3_F_LENGTH = 4;
	
	/**
	 * The offset for the {@link AngleF} instance that represents the maximum phi in a compiled {@link Cone3F} instance.
	 */
	public static final int CONE_3_F_OFFSET_PHI_MAX = 0;
	
	/**
	 * The offset for the radius in a compiled {@link Cone3F} instance.
	 */
	public static final int CONE_3_F_OFFSET_RADIUS = 1;
	
	/**
	 * The offset for the maximum Z in a compiled {@link Cone3F} instance.
	 */
	public static final int CONE_3_F_OFFSET_Z_MAX = 2;
	
	/**
	 * The length of a compiled {@link Cylinder3F} instance.
	 */
	public static final int CYLINDER_3_F_LENGTH = 4;
	
	/**
	 * The offset for the {@link AngleF} instance that represents the maximum phi in a compiled {@link Cylinder3F} instance.
	 */
	public static final int CYLINDER_3_F_OFFSET_PHI_MAX = 0;
	
	/**
	 * The offset for the radius in a compiled {@link Cylinder3F} instance.
	 */
	public static final int CYLINDER_3_F_OFFSET_RADIUS = 1;
	
	/**
	 * The offset for the maximum Z in a compiled {@link Cylinder3F} instance.
	 */
	public static final int CYLINDER_3_F_OFFSET_Z_MAX = 2;
	
	/**
	 * The offset for the minimum Z in a compiled {@link Cylinder3F} instance.
	 */
	public static final int CYLINDER_3_F_OFFSET_Z_MIN = 3;
	
	/**
	 * The length of a compiled {@link Disk3F} instance.
	 */
	public static final int DISK_3_F_LENGTH = 4;
	
	/**
	 * The offset for the {@link AngleF} instance that represents the maximum phi in a compiled {@link Disk3F} instance.
	 */
	public static final int DISK_3_F_OFFSET_PHI_MAX = 0;
	
	/**
	 * The offset for the inner radius in a compiled {@link Disk3F} instance.
	 */
	public static final int DISK_3_F_OFFSET_RADIUS_INNER = 1;
	
	/**
	 * The offset for the outer radius in a compiled {@link Disk3F} instance.
	 */
	public static final int DISK_3_F_OFFSET_RADIUS_OUTER = 2;
	
	/**
	 * The offset for the maximum Z in a compiled {@link Disk3F} instance.
	 */
	public static final int DISK_3_F_OFFSET_Z_MAX = 3;
	
	/**
	 * The length of a compiled {@link Hyperboloid3F} instance.
	 */
	public static final int HYPERBOLOID_3_F_LENGTH = 16;
	
	/**
	 * The offset for the variable denoted by {@code AH} in a compiled {@link Hyperboloid3F} instance.
	 */
	public static final int HYPERBOLOID_3_F_OFFSET_A_H = 7;
	
	/**
	 * The offset for the variable denoted by {@code CH} in a compiled {@link Hyperboloid3F} instance.
	 */
	public static final int HYPERBOLOID_3_F_OFFSET_C_H = 8;
	
	/**
	 * The offset for the {@link AngleF} instance that represents the maximum phi in a compiled {@link Hyperboloid3F} instance.
	 */
	public static final int HYPERBOLOID_3_F_OFFSET_PHI_MAX = 0;
	
	/**
	 * The offset for the {@link Point3F} instance denoted by {@code A} in a compiled {@link Hyperboloid3F} instance.
	 */
	public static final int HYPERBOLOID_3_F_OFFSET_POINT_A = 1;
	
	/**
	 * The offset for the {@link Point3F} instance denoted by {@code B} in a compiled {@link Hyperboloid3F} instance.
	 */
	public static final int HYPERBOLOID_3_F_OFFSET_POINT_B = 4;
	
	/**
	 * The offset for the maximum radius in a compiled {@link Hyperboloid3F} instance.
	 */
	public static final int HYPERBOLOID_3_F_OFFSET_R_MAX = 9;
	
	/**
	 * The offset for the maximum Z in a compiled {@link Hyperboloid3F} instance.
	 */
	public static final int HYPERBOLOID_3_F_OFFSET_Z_MAX = 10;
	
	/**
	 * The offset for the minimum Z in a compiled {@link Hyperboloid3F} instance.
	 */
	public static final int HYPERBOLOID_3_F_OFFSET_Z_MIN = 11;
	
	/**
	 * The length of a compiled {@link Paraboloid3F} instance.
	 */
	public static final int PARABOLOID_3_F_LENGTH = 4;
	
	/**
	 * The offset for the {@link AngleF} instance that represents the maximum phi in a compiled {@link Paraboloid3F} instance.
	 */
	public static final int PARABOLOID_3_F_OFFSET_PHI_MAX = 0;
	
	/**
	 * The offset for the radius in a compiled {@link Paraboloid3F} instance.
	 */
	public static final int PARABOLOID_3_F_OFFSET_RADIUS = 1;
	
	/**
	 * The offset for the maximum Z in a compiled {@link Paraboloid3F} instance.
	 */
	public static final int PARABOLOID_3_F_OFFSET_Z_MAX = 2;
	
	/**
	 * The offset for the minimum Z in a compiled {@link Paraboloid3F} instance.
	 */
	public static final int PARABOLOID_3_F_OFFSET_Z_MIN = 3;
	
	/**
	 * The length of a compiled {@link Plane3F} instance.
	 */
	public static final int PLANE_3_F_LENGTH = 16;
	
	/**
	 * The offset for the {@link Point3F} instance denoted by {@code A} in a compiled {@link Plane3F} instance.
	 */
	public static final int PLANE_3_F_OFFSET_A = 0;
	
	/**
	 * The offset for the {@link Point3F} instance denoted by {@code B} in a compiled {@link Plane3F} instance.
	 */
	public static final int PLANE_3_F_OFFSET_B = 3;
	
	/**
	 * The offset for the {@link Point3F} instance denoted by {@code C} in a compiled {@link Plane3F} instance.
	 */
	public static final int PLANE_3_F_OFFSET_C = 6;
	
	/**
	 * The offset for the {@link Vector3F} instance that represents the surface normal in a compiled {@link Plane3F} instance.
	 */
	public static final int PLANE_3_F_OFFSET_SURFACE_NORMAL = 9;
	
	/**
	 * The length of a compiled {@link Rectangle3F} instance.
	 */
	public static final int RECTANGLE_3_F_LENGTH = 16;
	
	/**
	 * The offset for the {@link Point3F} instance denoted by {@code Position} in a compiled {@link Rectangle3F} instance.
	 */
	public static final int RECTANGLE_3_F_OFFSET_POSITION = 0;
	
	/**
	 * The offset for the {@link Vector3F} instance denoted by {@code Side A} in a compiled {@link Rectangle3F} instance.
	 */
	public static final int RECTANGLE_3_F_OFFSET_SIDE_A = 3;
	
	/**
	 * The offset for the {@link Vector3F} instance denoted by {@code Side B} in a compiled {@link Rectangle3F} instance.
	 */
	public static final int RECTANGLE_3_F_OFFSET_SIDE_B = 6;
	
	/**
	 * The offset for the {@link Vector3F} instance denoted by {@code Surface Normal} in a compiled {@link Rectangle3F} instance.
	 */
	public static final int RECTANGLE_3_F_OFFSET_SURFACE_NORMAL = 9;
	
	/**
	 * The length of a compiled {@link RectangularCuboid3F} instance.
	 */
	public static final int RECTANGULAR_CUBOID_3_F_LENGTH = 8;
	
	/**
	 * The offset for the {@link Point3F} instance that represents the maximum point in a compiled {@link RectangularCuboid3F} instance.
	 */
	public static final int RECTANGULAR_CUBOID_3_F_OFFSET_MAXIMUM = 0;
	
	/**
	 * The offset for the {@link Point3F} instance that represents the minimum point in a compiled {@link RectangularCuboid3F} instance.
	 */
	public static final int RECTANGULAR_CUBOID_3_F_OFFSET_MINIMUM = 3;
	
	/**
	 * The length of a compiled {@link Sphere3F} instance.
	 */
	public static final int SPHERE_3_F_LENGTH = 4;
	
	/**
	 * The offset for the {@link Point3F} instance that represents the center in the a compiled {@link Sphere3F} instance.
	 */
	public static final int SPHERE_3_F_OFFSET_CENTER = 0;
	
	/**
	 * The offset for the radius in a compiled {@link Sphere3F} instance.
	 */
	public static final int SPHERE_3_F_OFFSET_RADIUS = 3;
	
	/**
	 * The length of a compiled {@link Torus3F} instance.
	 */
	public static final int TORUS_3_F_LENGTH = 2;
	
	/**
	 * The offset for the inner radius in a compiled {@link Torus3F} instance.
	 */
	public static final int TORUS_3_F_OFFSET_RADIUS_INNER = 0;
	
	/**
	 * The offset for the outer radius in a compiled {@link Torus3F} instance.
	 */
	public static final int TORUS_3_F_OFFSET_RADIUS_OUTER = 1;
	
	/**
	 * The length of a compiled {@link Triangle3F} instance.
	 */
	public static final int TRIANGLE_3_F_LENGTH = 24;
	
	/**
	 * The offset for the {@link Vector3F} instance that represents the W-direction of the {@link OrthonormalBasis33F} of {@link Vertex3F} {@code A} in a compiled {@link Triangle3F} instance.
	 */
	public static final int TRIANGLE_3_F_OFFSET_A_ORTHONORMAL_BASIS_W = 15;
	
	/**
	 * The offset for the {@link Point3F} instance that represents the position of {@link Vertex3F} {@code A} in a compiled {@link Triangle3F} instance.
	 */
	public static final int TRIANGLE_3_F_OFFSET_A_POSITION = 0;
	
	/**
	 * The offset for the {@link Point2F} instance that represents the texture coordinates of {@link Vertex3F} {@code A} in a compiled {@link Triangle3F} instance.
	 */
	public static final int TRIANGLE_3_F_OFFSET_A_TEXTURE_COORDINATES = 9;
	
	/**
	 * The offset for the {@link Vector3F} instance that represents the W-direction of the {@link OrthonormalBasis33F} of {@link Vertex3F} {@code B} in a compiled {@link Triangle3F} instance.
	 */
	public static final int TRIANGLE_3_F_OFFSET_B_ORTHONORMAL_BASIS_W = 18;
	
	/**
	 * The offset for the {@link Point3F} instance that represents the position of {@link Vertex3F} {@code B} in a compiled {@link Triangle3F} instance.
	 */
	public static final int TRIANGLE_3_F_OFFSET_B_POSITION = 3;
	
	/**
	 * The offset for the {@link Point2F} instance that represents the texture coordinates of {@link Vertex3F} {@code B} in a compiled {@link Triangle3F} instance.
	 */
	public static final int TRIANGLE_3_F_OFFSET_B_TEXTURE_COORDINATES = 11;
	
	/**
	 * The offset for the {@link Vector3F} instance that represents the W-direction of the {@link OrthonormalBasis33F} of {@link Vertex3F} {@code C} in a compiled {@link Triangle3F} instance.
	 */
	public static final int TRIANGLE_3_F_OFFSET_C_ORTHONORMAL_BASIS_W = 21;
	
	/**
	 * The offset for the {@link Point3F} instance that represents the position of {@link Vertex3F} {@code C} in a compiled {@link Triangle3F} instance.
	 */
	public static final int TRIANGLE_3_F_OFFSET_C_POSITION = 6;
	
	/**
	 * The offset for the {@link Point2F} instance that represents the texture coordinates of {@link Vertex3F} {@code C} in a compiled {@link Triangle3F} instance.
	 */
	public static final int TRIANGLE_3_F_OFFSET_C_TEXTURE_COORDINATES = 13;
	
	/**
	 * The offset for the offset of the {@link BoundingVolume3F} in a given {@link BVHNode3F} in a compiled {@link TriangleMesh3F} instance.
	 * <p>
	 * The {@code BoundingVolume3F} is always an {@link AxisAlignedBoundingBox3F}.
	 * <p>
	 * This offset is used for both {@link LeafBVHNode3F} and {@link TreeBVHNode3F}.
	 */
	public static final int TRIANGLE_MESH_3_F_B_V_H_NODE_3_F_OFFSET_BOUNDING_VOLUME_OFFSET = 1;
	
	/**
	 * The offset for the ID of a given {@link BVHNode3F} in a compiled {@link TriangleMesh3F} instance.
	 * <p>
	 * This offset is used for both {@link LeafBVHNode3F} and {@link TreeBVHNode3F}.
	 */
	public static final int TRIANGLE_MESH_3_F_B_V_H_NODE_3_F_OFFSET_ID = 0;
	
	/**
	 * The offset for the left {@link BVHNode3F} in a {@link TreeBVHNode3F} or the {@link Shape3F} count in a {@link LeafBVHNode3F} in a compiled {@link TriangleMesh3F} instance.
	 * <p>
	 * This offset is used for both {@code LeafBVHNode3F} and {@code TreeBVHNode3F}.
	 */
	public static final int TRIANGLE_MESH_3_F_B_V_H_NODE_3_F_OFFSET_LEFT_OFFSET_OR_SHAPE_COUNT = 3;
	
	/**
	 * The offset for the next {@link BVHNode3F} of a given {@code BVHNode3F} in a compiled {@link TriangleMesh3F} instance.
	 * <p>
	 * This offset is used for both {@link LeafBVHNode3F} and {@link TreeBVHNode3F}.
	 */
	public static final int TRIANGLE_MESH_3_F_B_V_H_NODE_3_F_OFFSET_NEXT_OFFSET = 2;
	
	/**
	 * The offset for the {@link Shape3F} count in a {@link LeafBVHNode3F} in a compiled {@link TriangleMesh3F} instance.
	 * <p>
	 * This offset is used for {@code LeafBVHNode3F} only.
	 */
	public static final int TRIANGLE_MESH_3_F_LEAF_B_V_H_NODE_3_F_OFFSET_SHAPE_COUNT = 3;
	
	/**
	 * The offset for the left {@link BVHNode3F} in a {@link TreeBVHNode3F} in a compiled {@link TriangleMesh3F} instance.
	 * <p>
	 * This offset is used for {@code TreeBVHNode3F} only.
	 */
	public static final int TRIANGLE_MESH_3_F_TREE_B_V_H_NODE_3_F_OFFSET_LEFT_OFFSET = 3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private float[] shape3FCone3FArray;
	private float[] shape3FCylinder3FArray;
	private float[] shape3FDisk3FArray;
	private float[] shape3FHyperboloid3FArray;
	private float[] shape3FParaboloid3FArray;
	private float[] shape3FPlane3FArray;
	private float[] shape3FRectangle3FArray;
	private float[] shape3FRectangularCuboid3FArray;
	private float[] shape3FSphere3FArray;
	private float[] shape3FTorus3FArray;
	private float[] shape3FTriangle3FArray;
	private int[] shape3FTriangleMesh3FArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledShape3FCache} instance.
	 */
	public CompiledShape3FCache() {
		setShape3FCone3FArray(new float[1]);
		setShape3FCylinder3FArray(new float[1]);
		setShape3FDisk3FArray(new float[1]);
		setShape3FHyperboloid3FArray(new float[1]);
		setShape3FParaboloid3FArray(new float[1]);
		setShape3FPlane3FArray(new float[1]);
		setShape3FRectangle3FArray(new float[1]);
		setShape3FRectangularCuboid3FArray(new float[1]);
		setShape3FSphere3FArray(new float[1]);
		setShape3FTorus3FArray(new float[1]);
		setShape3FTriangle3FArray(new float[1]);
		setShape3FTriangleMesh3FArray(new int[1]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float[]} that contains all {@link Cone3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Cone3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getShape3FCone3FArray() {
		return this.shape3FCone3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Cylinder3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Cylinder3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getShape3FCylinder3FArray() {
		return this.shape3FCylinder3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Disk3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Disk3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getShape3FDisk3FArray() {
		return this.shape3FDisk3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Hyperboloid3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Hyperboloid3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getShape3FHyperboloid3FArray() {
		return this.shape3FHyperboloid3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Paraboloid3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Paraboloid3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getShape3FParaboloid3FArray() {
		return this.shape3FParaboloid3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Plane3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Plane3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getShape3FPlane3FArray() {
		return this.shape3FPlane3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Rectangle3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Rectangle3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getShape3FRectangle3FArray() {
		return this.shape3FRectangle3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link RectangularCuboid3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code RectangularCuboid3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getShape3FRectangularCuboid3FArray() {
		return this.shape3FRectangularCuboid3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Sphere3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Sphere3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getShape3FSphere3FArray() {
		return this.shape3FSphere3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Torus3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Torus3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getShape3FTorus3FArray() {
		return this.shape3FTorus3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Triangle3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Triangle3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getShape3FTriangle3FArray() {
		return this.shape3FTriangle3FArray;
	}
	
	/**
	 * Returns the {@link Cone3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Cone3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getShape3FCone3FCount() {
		return Structures.getStructureCount(this.shape3FCone3FArray, CONE_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code shape3FCone3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FCone3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FCone3F a {@link Cone3F} instance in compiled form
	 * @return the absolute offset of {@code shape3FCone3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FCone3F} is {@code null}
	 */
	public int getShape3FCone3FOffsetAbsolute(final float[] shape3FCone3F) {
		return Structures.getStructureOffsetAbsolute(this.shape3FCone3FArray, Objects.requireNonNull(shape3FCone3F, "shape3FCone3F == null"), getShape3FCone3FCount(), CONE_3_F_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code shape3FCone3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FCone3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FCone3F a {@link Cone3F} instance in compiled form
	 * @return the relative offset of {@code shape3FCone3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FCone3F} is {@code null}
	 */
	public int getShape3FCone3FOffsetRelative(final float[] shape3FCone3F) {
		return Structures.getStructureOffsetRelative(this.shape3FCone3FArray, Objects.requireNonNull(shape3FCone3F, "shape3FCone3F == null"), getShape3FCone3FCount(), CONE_3_F_LENGTH);
	}
	
	/**
	 * Returns the {@link Cylinder3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Cylinder3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getShape3FCylinder3FCount() {
		return Structures.getStructureCount(this.shape3FCylinder3FArray, CYLINDER_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code shape3FCylinder3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FCylinder3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FCylinder3F a {@link Cylinder3F} instance in compiled form
	 * @return the absolute offset of {@code shape3FCylinder3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FCylinder3F} is {@code null}
	 */
	public int getShape3FCylinder3FOffsetAbsolute(final float[] shape3FCylinder3F) {
		return Structures.getStructureOffsetAbsolute(this.shape3FCylinder3FArray, Objects.requireNonNull(shape3FCylinder3F, "shape3FCylinder3F == null"), getShape3FCylinder3FCount(), CYLINDER_3_F_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code shape3FCylinder3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FCylinder3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FCylinder3F a {@link Cylinder3F} instance in compiled form
	 * @return the relative offset of {@code shape3FCylinder3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FCylinder3F} is {@code null}
	 */
	public int getShape3FCylinder3FOffsetRelative(final float[] shape3FCylinder3F) {
		return Structures.getStructureOffsetRelative(this.shape3FCylinder3FArray, Objects.requireNonNull(shape3FCylinder3F, "shape3FCylinder3F == null"), getShape3FCylinder3FCount(), CYLINDER_3_F_LENGTH);
	}
	
	/**
	 * Returns the {@link Disk3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Disk3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getShape3FDisk3FCount() {
		return Structures.getStructureCount(this.shape3FDisk3FArray, DISK_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code shape3FDisk3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FDisk3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FDisk3F a {@link Disk3F} instance in compiled form
	 * @return the absolute offset of {@code shape3FDisk3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FDisk3F} is {@code null}
	 */
	public int getShape3FDisk3FOffsetAbsolute(final float[] shape3FDisk3F) {
		return Structures.getStructureOffsetAbsolute(this.shape3FDisk3FArray, Objects.requireNonNull(shape3FDisk3F, "shape3FDisk3F == null"), getShape3FDisk3FCount(), DISK_3_F_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code shape3FDisk3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FDisk3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FDisk3F a {@link Disk3F} instance in compiled form
	 * @return the relative offset of {@code shape3FDisk3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FDisk3F} is {@code null}
	 */
	public int getShape3FDisk3FOffsetRelative(final float[] shape3FDisk3F) {
		return Structures.getStructureOffsetRelative(this.shape3FDisk3FArray, Objects.requireNonNull(shape3FDisk3F, "shape3FDisk3F == null"), getShape3FDisk3FCount(), DISK_3_F_LENGTH);
	}
	
	/**
	 * Returns the {@link Hyperboloid3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Hyperboloid3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getShape3FHyperboloid3FCount() {
		return Structures.getStructureCount(this.shape3FHyperboloid3FArray, HYPERBOLOID_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code shape3FHyperboloid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FHyperboloid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FHyperboloid3F a {@link Hyperboloid3F} instance in compiled form
	 * @return the absolute offset of {@code shape3FHyperboloid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FHyperboloid3F} is {@code null}
	 */
	public int getShape3FHyperboloid3FOffsetAbsolute(final float[] shape3FHyperboloid3F) {
		return Structures.getStructureOffsetAbsolute(this.shape3FHyperboloid3FArray, Objects.requireNonNull(shape3FHyperboloid3F, "shape3FHyperboloid3F == null"), getShape3FHyperboloid3FCount(), HYPERBOLOID_3_F_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code shape3FHyperboloid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FHyperboloid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FHyperboloid3F a {@link Hyperboloid3F} instance in compiled form
	 * @return the relative offset of {@code shape3FHyperboloid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FHyperboloid3F} is {@code null}
	 */
	public int getShape3FHyperboloid3FOffsetRelative(final float[] shape3FHyperboloid3F) {
		return Structures.getStructureOffsetRelative(this.shape3FHyperboloid3FArray, Objects.requireNonNull(shape3FHyperboloid3F, "shape3FHyperboloid3F == null"), getShape3FHyperboloid3FCount(), HYPERBOLOID_3_F_LENGTH);
	}
	
	/**
	 * Returns the {@link Paraboloid3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Paraboloid3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getShape3FParaboloid3FCount() {
		return Structures.getStructureCount(this.shape3FParaboloid3FArray, PARABOLOID_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code shape3FParaboloid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FParaboloid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FParaboloid3F a {@link Paraboloid3F} instance in compiled form
	 * @return the absolute offset of {@code shape3FParaboloid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FParaboloid3F} is {@code null}
	 */
	public int getShape3FParaboloid3FOffsetAbsolute(final float[] shape3FParaboloid3F) {
		return Structures.getStructureOffsetAbsolute(this.shape3FParaboloid3FArray, Objects.requireNonNull(shape3FParaboloid3F, "shape3FParaboloid3F == null"), getShape3FParaboloid3FCount(), PARABOLOID_3_F_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code shape3FParaboloid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FParaboloid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FParaboloid3F a {@link Paraboloid3F} instance in compiled form
	 * @return the relative offset of {@code shape3FParaboloid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FParaboloid3F} is {@code null}
	 */
	public int getShape3FParaboloid3FOffsetRelative(final float[] shape3FParaboloid3F) {
		return Structures.getStructureOffsetRelative(this.shape3FParaboloid3FArray, Objects.requireNonNull(shape3FParaboloid3F, "shape3FParaboloid3F == null"), getShape3FParaboloid3FCount(), PARABOLOID_3_F_LENGTH);
	}
	
	/**
	 * Returns the {@link Plane3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Plane3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getShape3FPlane3FCount() {
		return Structures.getStructureCount(this.shape3FPlane3FArray, PLANE_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code shape3FPlane3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FPlane3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FPlane3F a {@link Plane3F} instance in compiled form
	 * @return the absolute offset of {@code shape3FPlane3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FPlane3F} is {@code null}
	 */
	public int getShape3FPlane3FOffsetAbsolute(final float[] shape3FPlane3F) {
		return Structures.getStructureOffsetAbsolute(this.shape3FPlane3FArray, Objects.requireNonNull(shape3FPlane3F, "shape3FPlane3F == null"), getShape3FPlane3FCount(), PLANE_3_F_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code shape3FPlane3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FPlane3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FPlane3F a {@link Plane3F} instance in compiled form
	 * @return the relative offset of {@code shape3FPlane3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FPlane3F} is {@code null}
	 */
	public int getShape3FPlane3FOffsetRelative(final float[] shape3FPlane3F) {
		return Structures.getStructureOffsetRelative(this.shape3FPlane3FArray, Objects.requireNonNull(shape3FPlane3F, "shape3FPlane3F == null"), getShape3FPlane3FCount(), PLANE_3_F_LENGTH);
	}
	
	/**
	 * Returns the {@link Rectangle3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Rectangle3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getShape3FRectangle3FCount() {
		return Structures.getStructureCount(this.shape3FRectangle3FArray, RECTANGLE_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code shape3FRectangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FRectangle3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FRectangle3F a {@link Rectangle3F} instance in compiled form
	 * @return the absolute offset of {@code shape3FRectangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FRectangle3F} is {@code null}
	 */
	public int getShape3FRectangle3FOffsetAbsolute(final float[] shape3FRectangle3F) {
		return Structures.getStructureOffsetAbsolute(this.shape3FRectangle3FArray, Objects.requireNonNull(shape3FRectangle3F, "shape3FRectangle3F == null"), getShape3FRectangle3FCount(), RECTANGLE_3_F_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code shape3FRectangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FRectangle3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FRectangle3F a {@link Rectangle3F} instance in compiled form
	 * @return the relative offset of {@code shape3FRectangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FRectangle3F} is {@code null}
	 */
	public int getShape3FRectangle3FOffsetRelative(final float[] shape3FRectangle3F) {
		return Structures.getStructureOffsetRelative(this.shape3FRectangle3FArray, Objects.requireNonNull(shape3FRectangle3F, "shape3FRectangle3F == null"), getShape3FRectangle3FCount(), RECTANGLE_3_F_LENGTH);
	}
	
	/**
	 * Returns the {@link RectangularCuboid3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code RectangularCuboid3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getShape3FRectangularCuboid3FCount() {
		return Structures.getStructureCount(this.shape3FRectangularCuboid3FArray, RECTANGULAR_CUBOID_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code shape3FRectangularCuboid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FRectangularCuboid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FRectangularCuboid3F a {@link RectangularCuboid3F} instance in compiled form
	 * @return the absolute offset of {@code shape3FRectangularCuboid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FRectangularCuboid3F} is {@code null}
	 */
	public int getShape3FRectangularCuboid3FOffsetAbsolute(final float[] shape3FRectangularCuboid3F) {
		return Structures.getStructureOffsetAbsolute(this.shape3FRectangularCuboid3FArray, Objects.requireNonNull(shape3FRectangularCuboid3F, "shape3FRectangularCuboid3F == null"), getShape3FRectangularCuboid3FCount(), RECTANGULAR_CUBOID_3_F_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code shape3FRectangularCuboid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FRectangularCuboid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FRectangularCuboid3F a {@link RectangularCuboid3F} instance in compiled form
	 * @return the relative offset of {@code shape3FRectangularCuboid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FRectangularCuboid3F} is {@code null}
	 */
	public int getShape3FRectangularCuboid3FOffsetRelative(final float[] shape3FRectangularCuboid3F) {
		return Structures.getStructureOffsetRelative(this.shape3FRectangularCuboid3FArray, Objects.requireNonNull(shape3FRectangularCuboid3F, "shape3FRectangularCuboid3F == null"), getShape3FRectangularCuboid3FCount(), RECTANGULAR_CUBOID_3_F_LENGTH);
	}
	
	/**
	 * Returns the {@link Sphere3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Sphere3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getShape3FSphere3FCount() {
		return Structures.getStructureCount(this.shape3FSphere3FArray, SPHERE_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code shape3FSphere3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FSphere3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FSphere3F a {@link Sphere3F} instance in compiled form
	 * @return the absolute offset of {@code shape3FSphere3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FSphere3F} is {@code null}
	 */
	public int getShape3FSphere3FOffsetAbsolute(final float[] shape3FSphere3F) {
		return Structures.getStructureOffsetAbsolute(this.shape3FSphere3FArray, Objects.requireNonNull(shape3FSphere3F, "shape3FSphere3F == null"), getShape3FSphere3FCount(), SPHERE_3_F_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code shape3FSphere3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FSphere3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FSphere3F a {@link Sphere3F} instance in compiled form
	 * @return the relative offset of {@code shape3FSphere3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FSphere3F} is {@code null}
	 */
	public int getShape3FSphere3FOffsetRelative(final float[] shape3FSphere3F) {
		return Structures.getStructureOffsetRelative(this.shape3FSphere3FArray, Objects.requireNonNull(shape3FSphere3F, "shape3FSphere3F == null"), getShape3FSphere3FCount(), SPHERE_3_F_LENGTH);
	}
	
	/**
	 * Returns the {@link Torus3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Torus3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getShape3FTorus3FCount() {
		return Structures.getStructureCount(this.shape3FTorus3FArray, TORUS_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code shape3FTorus3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FTorus3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FTorus3F a {@link Torus3F} instance in compiled form
	 * @return the absolute offset of {@code shape3FTorus3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FTorus3F} is {@code null}
	 */
	public int getShape3FTorus3FOffsetAbsolute(final float[] shape3FTorus3F) {
		return Structures.getStructureOffsetAbsolute(this.shape3FTorus3FArray, Objects.requireNonNull(shape3FTorus3F, "shape3FTorus3F == null"), getShape3FTorus3FCount(), TORUS_3_F_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code shape3FTorus3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FTorus3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FTorus3F a {@link Torus3F} instance in compiled form
	 * @return the relative offset of {@code shape3FTorus3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FTorus3F} is {@code null}
	 */
	public int getShape3FTorus3FOffsetRelative(final float[] shape3FTorus3F) {
		return Structures.getStructureOffsetRelative(this.shape3FTorus3FArray, Objects.requireNonNull(shape3FTorus3F, "shape3FTorus3F == null"), getShape3FTorus3FCount(), TORUS_3_F_LENGTH);
	}
	
	/**
	 * Returns the {@link Triangle3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Triangle3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getShape3FTriangle3FCount() {
		return Structures.getStructureCount(this.shape3FTriangle3FArray, TRIANGLE_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code shape3FTriangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FTriangle3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FTriangle3F a {@link Triangle3F} instance in compiled form
	 * @return the absolute offset of {@code shape3FTriangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FTriangle3F} is {@code null}
	 */
	public int getShape3FTriangle3FOffsetAbsolute(final float[] shape3FTriangle3F) {
		return Structures.getStructureOffsetAbsolute(this.shape3FTriangle3FArray, Objects.requireNonNull(shape3FTriangle3F, "shape3FTriangle3F == null"), getShape3FTriangle3FCount(), TRIANGLE_3_F_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code shape3FTriangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code shape3FTriangle3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FTriangle3F a {@link Triangle3F} instance in compiled form
	 * @return the relative offset of {@code shape3FTriangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code shape3FTriangle3F} is {@code null}
	 */
	public int getShape3FTriangle3FOffsetRelative(final float[] shape3FTriangle3F) {
		return Structures.getStructureOffsetRelative(this.shape3FTriangle3FArray, Objects.requireNonNull(shape3FTriangle3F, "shape3FTriangle3F == null"), getShape3FTriangle3FCount(), TRIANGLE_3_F_LENGTH);
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link TriangleMesh3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code TriangleMesh3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public int[] getShape3FTriangleMesh3FArray() {
		return this.shape3FTriangleMesh3FArray;
	}
	
	/**
	 * Sets all {@link Cone3F} instances in compiled form to {@code shape3FCone3FArray}.
	 * <p>
	 * If {@code shape3FCone3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FCone3FArray the {@code Cone3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FCone3FArray} is {@code null}
	 */
	public void setShape3FCone3FArray(final float[] shape3FCone3FArray) {
		this.shape3FCone3FArray = Objects.requireNonNull(shape3FCone3FArray, "shape3FCone3FArray == null");
	}
	
	/**
	 * Sets all {@link Cylinder3F} instances in compiled form to {@code shape3FCylinder3FArray}.
	 * <p>
	 * If {@code shape3FCylinder3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FCylinder3FArray the {@code Cylinder3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FCylinder3FArray} is {@code null}
	 */
	public void setShape3FCylinder3FArray(final float[] shape3FCylinder3FArray) {
		this.shape3FCylinder3FArray = Objects.requireNonNull(shape3FCylinder3FArray, "shape3FCylinder3FArray == null");
	}
	
	/**
	 * Sets all {@link Disk3F} instances in compiled form to {@code shape3FDisk3FArray}.
	 * <p>
	 * If {@code shape3FDisk3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FDisk3FArray the {@code Disk3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FDisk3FArray} is {@code null}
	 */
	public void setShape3FDisk3FArray(final float[] shape3FDisk3FArray) {
		this.shape3FDisk3FArray = Objects.requireNonNull(shape3FDisk3FArray, "shape3FDisk3FArray == null");
	}
	
	/**
	 * Sets all {@link Hyperboloid3F} instances in compiled form to {@code shape3FHyperboloid3FArray}.
	 * <p>
	 * If {@code shape3FHyperboloid3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FHyperboloid3FArray the {@code Hyperboloid3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FHyperboloid3FArray} is {@code null}
	 */
	public void setShape3FHyperboloid3FArray(final float[] shape3FHyperboloid3FArray) {
		this.shape3FHyperboloid3FArray = Objects.requireNonNull(shape3FHyperboloid3FArray, "shape3FHyperboloid3FArray == null");
	}
	
	/**
	 * Sets all {@link Paraboloid3F} instances in compiled form to {@code shape3FParaboloid3FArray}.
	 * <p>
	 * If {@code shape3FParaboloid3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FParaboloid3FArray the {@code Paraboloid3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FParaboloid3FArray} is {@code null}
	 */
	public void setShape3FParaboloid3FArray(final float[] shape3FParaboloid3FArray) {
		this.shape3FParaboloid3FArray = Objects.requireNonNull(shape3FParaboloid3FArray, "shape3FParaboloid3FArray == null");
	}
	
	/**
	 * Sets all {@link Plane3F} instances in compiled form to {@code shape3FPlane3FArray}.
	 * <p>
	 * If {@code shape3FPlane3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FPlane3FArray the {@code Plane3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FPlane3FArray} is {@code null}
	 */
	public void setShape3FPlane3FArray(final float[] shape3FPlane3FArray) {
		this.shape3FPlane3FArray = Objects.requireNonNull(shape3FPlane3FArray, "shape3FPlane3FArray == null");
	}
	
	/**
	 * Sets all {@link Rectangle3F} instances in compiled form to {@code shape3FRectangle3FArray}.
	 * <p>
	 * If {@code shape3FRectangle3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FRectangle3FArray the {@code Rectangle3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FRectangle3FArray} is {@code null}
	 */
	public void setShape3FRectangle3FArray(final float[] shape3FRectangle3FArray) {
		this.shape3FRectangle3FArray = Objects.requireNonNull(shape3FRectangle3FArray, "shape3FRectangle3FArray == null");
	}
	
	/**
	 * Sets all {@link RectangularCuboid3F} instances in compiled form to {@code shape3FRectangularCuboid3FArray}.
	 * <p>
	 * If {@code shape3FRectangularCuboid3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FRectangularCuboid3FArray the {@code RectangularCuboid3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FRectangularCuboid3FArray} is {@code null}
	 */
	public void setShape3FRectangularCuboid3FArray(final float[] shape3FRectangularCuboid3FArray) {
		this.shape3FRectangularCuboid3FArray = Objects.requireNonNull(shape3FRectangularCuboid3FArray, "shape3FRectangularCuboid3FArray == null");
	}
	
	/**
	 * Sets all {@link Sphere3F} instances in compiled form to {@code shape3FSphere3FArray}.
	 * <p>
	 * If {@code shape3FSphere3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FSphere3FArray the {@code Sphere3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FSphere3FArray} is {@code null}
	 */
	public void setShape3FSphere3FArray(final float[] shape3FSphere3FArray) {
		this.shape3FSphere3FArray = Objects.requireNonNull(shape3FSphere3FArray, "shape3FSphere3FArray == null");
	}
	
	/**
	 * Sets all {@link Torus3F} instances in compiled form to {@code shape3FTorus3FArray}.
	 * <p>
	 * If {@code shape3FTorus3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FTorus3FArray the {@code Torus3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FTorus3FArray} is {@code null}
	 */
	public void setShape3FTorus3FArray(final float[] shape3FTorus3FArray) {
		this.shape3FTorus3FArray = Objects.requireNonNull(shape3FTorus3FArray, "shape3FTorus3FArray == null");
	}
	
	/**
	 * Sets all {@link Triangle3F} instances in compiled form to {@code shape3FTriangle3FArray}.
	 * <p>
	 * If {@code shape3FTriangle3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FTriangle3FArray the {@code Triangle3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FTriangle3FArray} is {@code null}
	 */
	public void setShape3FTriangle3FArray(final float[] shape3FTriangle3FArray) {
		this.shape3FTriangle3FArray = Objects.requireNonNull(shape3FTriangle3FArray, "shape3FTriangle3FArray == null");
	}
	
	/**
	 * Sets all {@link TriangleMesh3F} instances in compiled form to {@code shape3FTriangleMesh3FArray}.
	 * <p>
	 * If {@code shape3FTriangleMesh3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FTriangleMesh3FArray the {@code TriangleMesh3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FTriangleMesh3FArray} is {@code null}
	 */
	public void setShape3FTriangleMesh3FArray(final int[] shape3FTriangleMesh3FArray) {
		this.shape3FTriangleMesh3FArray = Objects.requireNonNull(shape3FTriangleMesh3FArray, "shape3FTriangleMesh3FArray == null");
	}
}