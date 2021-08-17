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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.ToIntFunction;

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
import org.dayflower.geometry.shape.Triangle3F.Vertex3F;
import org.dayflower.geometry.shape.TriangleMesh3F;
import org.dayflower.java.io.IntArrayOutputStream;
import org.dayflower.node.NodeFilter;
import org.dayflower.utility.Document;
import org.dayflower.utility.FloatArrays;
import org.dayflower.utility.Floats;
import org.dayflower.utility.IntArrays;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code CompiledShape3FCache} contains {@link Shape3F} instances in compiled form.
 * <p>
 * The {@code Shape3F} implementations that are supported are the following:
 * <ul>
 * <li>{@link Cone3F}</li>
 * <li>{@link Cylinder3F}</li>
 * <li>{@link Disk3F}</li>
 * <li>{@link Hyperboloid3F}</li>
 * <li>{@link Paraboloid3F}</li>
 * <li>{@link Plane3F}</li>
 * <li>{@link Rectangle3F}</li>
 * <li>{@link RectangularCuboid3F}</li>
 * <li>{@link Sphere3F}</li>
 * <li>{@link Torus3F}</li>
 * <li>{@link Triangle3F}</li>
 * <li>{@link TriangleMesh3F}</li>
 * </ul>
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
	
	private float[] cone3Fs;
	private float[] cylinder3Fs;
	private float[] disk3Fs;
	private float[] hyperboloid3Fs;
	private float[] paraboloid3Fs;
	private float[] plane3Fs;
	private float[] rectangle3Fs;
	private float[] rectangularCuboid3Fs;
	private float[] sphere3Fs;
	private float[] torus3Fs;
	private float[] triangle3Fs;
	private int[] triangleMesh3FOffsets;
	private int[] triangleMesh3Fs;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledShape3FCache} instance.
	 */
	public CompiledShape3FCache() {
		setCone3Fs(new float[0]);
		setCylinder3Fs(new float[0]);
		setDisk3Fs(new float[0]);
		setHyperboloid3Fs(new float[0]);
		setParaboloid3Fs(new float[0]);
		setPlane3Fs(new float[0]);
		setRectangle3Fs(new float[0]);
		setRectangularCuboid3Fs(new float[0]);
		setSphere3Fs(new float[0]);
		setTorus3Fs(new float[0]);
		setTriangle3Fs(new float[0]);
		setTriangleMesh3FOffsets(new int[0]);
		setTriangleMesh3Fs(new int[0]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Removes {@code cone3F} from this {@code CompiledShape3FCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code cone3F} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code cone3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cone3F.length} is not equal to {@code CompiledShape3FCache.CONE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param cone3F a {@link Cone3F} instance in compiled form
	 * @return {@code true} if, and only if, {@code cone3F} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code cone3F.length} is not equal to {@code CompiledShape3FCache.CONE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code cone3F} is {@code null}
	 */
	public boolean removeCone3F(final float[] cone3F) {
		final int absoluteOffset = getCone3FOffsetAbsolute(cone3F);
		
		if(absoluteOffset != -1) {
			setCone3Fs(FloatArrays.splice(getCone3Fs(), absoluteOffset, CONE_3_F_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code cylinder3F} from this {@code CompiledShape3FCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code cylinder3F} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code cylinder3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cylinder3F.length} is not equal to {@code CompiledShape3FCache.CYLINDER_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param cylinder3F a {@link Cylinder3F} instance in compiled form
	 * @return {@code true} if, and only if, {@code cylinder3F} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code cylinder3F.length} is not equal to {@code CompiledShape3FCache.CYLINDER_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code cylinder3F} is {@code null}
	 */
	public boolean removeCylinder3F(final float[] cylinder3F) {
		final int absoluteOffset = getCylinder3FOffsetAbsolute(cylinder3F);
		
		if(absoluteOffset != -1) {
			setCylinder3Fs(FloatArrays.splice(getCylinder3Fs(), absoluteOffset, CYLINDER_3_F_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code disk3F} from this {@code CompiledShape3FCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code disk3F} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code disk3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code disk3F.length} is not equal to {@code CompiledShape3FCache.DISK_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param disk3F a {@link Disk3F} instance in compiled form
	 * @return {@code true} if, and only if, {@code disk3F} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code disk3F.length} is not equal to {@code CompiledShape3FCache.DISK_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code disk3F} is {@code null}
	 */
	public boolean removeDisk3F(final float[] disk3F) {
		final int absoluteOffset = getDisk3FOffsetAbsolute(disk3F);
		
		if(absoluteOffset != -1) {
			setDisk3Fs(FloatArrays.splice(getDisk3Fs(), absoluteOffset, DISK_3_F_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code hyperboloid3F} from this {@code CompiledShape3FCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code hyperboloid3F} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code hyperboloid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code hyperboloid3F.length} is not equal to {@code CompiledShape3FCache.HYPERBOLOID_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param hyperboloid3F a {@link Hyperboloid3F} instance in compiled form
	 * @return {@code true} if, and only if, {@code hyperboloid3F} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code hyperboloid3F.length} is not equal to {@code CompiledShape3FCache.HYPERBOLOID_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code hyperboloid3F} is {@code null}
	 */
	public boolean removeHyperboloid3F(final float[] hyperboloid3F) {
		final int absoluteOffset = getHyperboloid3FOffsetAbsolute(hyperboloid3F);
		
		if(absoluteOffset != -1) {
			setHyperboloid3Fs(FloatArrays.splice(getHyperboloid3Fs(), absoluteOffset, HYPERBOLOID_3_F_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code paraboloid3F} from this {@code CompiledShape3FCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code paraboloid3F} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code paraboloid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code paraboloid3F.length} is not equal to {@code CompiledShape3FCache.PARABOLOID_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param paraboloid3F a {@link Paraboloid3F} instance in compiled form
	 * @return {@code true} if, and only if, {@code paraboloid3F} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code paraboloid3F.length} is not equal to {@code CompiledShape3FCache.PARABOLOID_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code paraboloid3F} is {@code null}
	 */
	public boolean removeParaboloid3F(final float[] paraboloid3F) {
		final int absoluteOffset = getParaboloid3FOffsetAbsolute(paraboloid3F);
		
		if(absoluteOffset != -1) {
			setParaboloid3Fs(FloatArrays.splice(getParaboloid3Fs(), absoluteOffset, PARABOLOID_3_F_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code plane3F} from this {@code CompiledShape3FCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code plane3F} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code plane3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code plane3F.length} is not equal to {@code CompiledShape3FCache.PLANE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param plane3F a {@link Plane3F} instance in compiled form
	 * @return {@code true} if, and only if, {@code plane3F} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code plane3F.length} is not equal to {@code CompiledShape3FCache.PLANE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code plane3F} is {@code null}
	 */
	public boolean removePlane3F(final float[] plane3F) {
		final int absoluteOffset = getPlane3FOffsetAbsolute(plane3F);
		
		if(absoluteOffset != -1) {
			setPlane3Fs(FloatArrays.splice(getPlane3Fs(), absoluteOffset, PLANE_3_F_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code rectangle3F} from this {@code CompiledShape3FCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code rectangle3F} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code rectangle3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code rectangle3F.length} is not equal to {@code CompiledShape3FCache.RECTANGLE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param rectangle3F a {@link Rectangle3F} instance in compiled form
	 * @return {@code true} if, and only if, {@code rectangle3F} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code rectangle3F.length} is not equal to {@code CompiledShape3FCache.RECTANGLE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code rectangle3F} is {@code null}
	 */
	public boolean removeRectangle3F(final float[] rectangle3F) {
		final int absoluteOffset = getRectangle3FOffsetAbsolute(rectangle3F);
		
		if(absoluteOffset != -1) {
			setRectangle3Fs(FloatArrays.splice(getRectangle3Fs(), absoluteOffset, RECTANGLE_3_F_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code rectangularCuboid3F} from this {@code CompiledShape3FCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code rectangularCuboid3F} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code rectangularCuboid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code rectangularCuboid3F.length} is not equal to {@code CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param rectangularCuboid3F a {@link RectangularCuboid3F} instance in compiled form
	 * @return {@code true} if, and only if, {@code rectangularCuboid3F} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code rectangularCuboid3F.length} is not equal to {@code CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code rectangularCuboid3F} is {@code null}
	 */
	public boolean removeRectangularCuboid3F(final float[] rectangularCuboid3F) {
		final int absoluteOffset = getRectangularCuboid3FOffsetAbsolute(rectangularCuboid3F);
		
		if(absoluteOffset != -1) {
			setRectangularCuboid3Fs(FloatArrays.splice(getRectangularCuboid3Fs(), absoluteOffset, RECTANGULAR_CUBOID_3_F_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code sphere3F} from this {@code CompiledShape3FCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code sphere3F} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code sphere3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code sphere3F.length} is not equal to {@code CompiledShape3FCache.SPHERE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param sphere3F a {@link Sphere3F} instance in compiled form
	 * @return {@code true} if, and only if, {@code sphere3F} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code sphere3F.length} is not equal to {@code CompiledShape3FCache.SPHERE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code sphere3F} is {@code null}
	 */
	public boolean removeSphere3F(final float[] sphere3F) {
		final int absoluteOffset = getSphere3FOffsetAbsolute(sphere3F);
		
		if(absoluteOffset != -1) {
			setSphere3Fs(FloatArrays.splice(getSphere3Fs(), absoluteOffset, SPHERE_3_F_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code torus3F} from this {@code CompiledShape3FCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code torus3F} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code torus3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code torus3F.length} is not equal to {@code CompiledShape3FCache.TORUS_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param torus3F a {@link Torus3F} instance in compiled form
	 * @return {@code true} if, and only if, {@code torus3F} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code torus3F.length} is not equal to {@code CompiledShape3FCache.TORUS_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code torus3F} is {@code null}
	 */
	public boolean removeTorus3F(final float[] torus3F) {
		final int absoluteOffset = getTorus3FOffsetAbsolute(torus3F);
		
		if(absoluteOffset != -1) {
			setTorus3Fs(FloatArrays.splice(getTorus3Fs(), absoluteOffset, TORUS_3_F_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code triangle3F} from this {@code CompiledShape3FCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code triangle3F} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code triangle3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code triangle3F.length} is not equal to {@code CompiledShape3FCache.TRIANGLE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param triangle3F a {@link Triangle3F} instance in compiled form
	 * @return {@code true} if, and only if, {@code triangle3F} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code triangle3F.length} is not equal to {@code CompiledShape3FCache.TRIANGLE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code triangle3F} is {@code null}
	 */
	public boolean removeTriangle3F(final float[] triangle3F) {
		final int absoluteOffset = getTriangle3FOffsetAbsolute(triangle3F);
		
		if(absoluteOffset != -1) {
			setTriangle3Fs(FloatArrays.splice(getTriangle3Fs(), absoluteOffset, TRIANGLE_3_F_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code triangleMesh3F} from this {@code CompiledShape3FCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code triangleMesh3F} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code triangleMesh3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code triangleMesh3F.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param triangleMesh3F a {@link TriangleMesh3F} instance in compiled form
	 * @return {@code true} if, and only if, {@code triangleMesh3F} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code triangleMesh3F.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code triangleMesh3F} is {@code null}
	 */
	public boolean removeTriangleMesh3F(final int[] triangleMesh3F) {
		final int absoluteOffset = getTriangleMesh3FOffsetAbsolute(triangleMesh3F);
		
		if(absoluteOffset != -1) {
			setTriangleMesh3FOffsets(Structures.removeStructureOffset(getTriangleMesh3FOffsets(), absoluteOffset, triangleMesh3F.length));
			setTriangleMesh3Fs(IntArrays.splice(getTriangleMesh3Fs(), absoluteOffset, triangleMesh3F.length));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Cone3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Cone3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getCone3Fs() {
		return this.cone3Fs;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Cylinder3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Cylinder3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getCylinder3Fs() {
		return this.cylinder3Fs;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Disk3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Disk3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getDisk3Fs() {
		return this.disk3Fs;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Hyperboloid3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Hyperboloid3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getHyperboloid3Fs() {
		return this.hyperboloid3Fs;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Paraboloid3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Paraboloid3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getParaboloid3Fs() {
		return this.paraboloid3Fs;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Plane3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Plane3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getPlane3Fs() {
		return this.plane3Fs;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Rectangle3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Rectangle3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getRectangle3Fs() {
		return this.rectangle3Fs;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link RectangularCuboid3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code RectangularCuboid3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getRectangularCuboid3Fs() {
		return this.rectangularCuboid3Fs;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Sphere3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Sphere3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getSphere3Fs() {
		return this.sphere3Fs;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Torus3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Torus3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getTorus3Fs() {
		return this.torus3Fs;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Triangle3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Triangle3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public float[] getTriangle3Fs() {
		return this.triangle3Fs;
	}
	
	/**
	 * Adds {@code cone3F} to this {@code CompiledShape3FCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code cone3F}.
	 * <p>
	 * If {@code cone3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cone3F.length} is not equal to {@code CompiledShape3FCache.CONE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param cone3F a {@link Cone3F} instance in compiled form
	 * @return the relative offset to {@code cone3F}
	 * @throws IllegalArgumentException thrown if, and only if, {@code cone3F.length} is not equal to {@code CompiledShape3FCache.CONE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code cone3F} is {@code null}
	 */
	public int addCone3F(final float[] cone3F) {
		final int relativeOffsetOld = getCone3FOffsetRelative(cone3F);
		final int relativeOffsetNew = getCone3FCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setCone3Fs(FloatArrays.merge(getCone3Fs(), cone3F));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code cylinder3F} to this {@code CompiledShape3FCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code cylinder3F}.
	 * <p>
	 * If {@code cylinder3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cylinder3F.length} is not equal to {@code CompiledShape3FCache.CYLINDER_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param cylinder3F a {@link Cylinder3F} instance in compiled form
	 * @return the relative offset to {@code cylinder3F}
	 * @throws IllegalArgumentException thrown if, and only if, {@code cylinder3F.length} is not equal to {@code CompiledShape3FCache.CYLINDER_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code cylinder3F} is {@code null}
	 */
	public int addCylinder3F(final float[] cylinder3F) {
		final int relativeOffsetOld = getCylinder3FOffsetRelative(cylinder3F);
		final int relativeOffsetNew = getCylinder3FCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setCylinder3Fs(FloatArrays.merge(getCylinder3Fs(), cylinder3F));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code disk3F} to this {@code CompiledShape3FCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code disk3F}.
	 * <p>
	 * If {@code disk3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code disk3F.length} is not equal to {@code CompiledShape3FCache.DISK_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param disk3F a {@link Disk3F} instance in compiled form
	 * @return the relative offset to {@code disk3F}
	 * @throws IllegalArgumentException thrown if, and only if, {@code disk3F.length} is not equal to {@code CompiledShape3FCache.DISK_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code disk3F} is {@code null}
	 */
	public int addDisk3F(final float[] disk3F) {
		final int relativeOffsetOld = getDisk3FOffsetRelative(disk3F);
		final int relativeOffsetNew = getDisk3FCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setDisk3Fs(FloatArrays.merge(getDisk3Fs(), disk3F));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code hyperboloid3F} to this {@code CompiledShape3FCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code hyperboloid3F}.
	 * <p>
	 * If {@code hyperboloid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code hyperboloid3F.length} is not equal to {@code CompiledShape3FCache.HYPERBOLOID_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param hyperboloid3F a {@link Hyperboloid3F} instance in compiled form
	 * @return the relative offset to {@code hyperboloid3F}
	 * @throws IllegalArgumentException thrown if, and only if, {@code hyperboloid3F.length} is not equal to {@code CompiledShape3FCache.HYPERBOLOID_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code hyperboloid3F} is {@code null}
	 */
	public int addHyperboloid3F(final float[] hyperboloid3F) {
		final int relativeOffsetOld = getHyperboloid3FOffsetRelative(hyperboloid3F);
		final int relativeOffsetNew = getHyperboloid3FCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setHyperboloid3Fs(FloatArrays.merge(getHyperboloid3Fs(), hyperboloid3F));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code paraboloid3F} to this {@code CompiledShape3FCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code paraboloid3F}.
	 * <p>
	 * If {@code paraboloid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code paraboloid3F.length} is not equal to {@code CompiledShape3FCache.PARABOLOID_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param paraboloid3F a {@link Paraboloid3F} instance in compiled form
	 * @return the relative offset to {@code paraboloid3F}
	 * @throws IllegalArgumentException thrown if, and only if, {@code paraboloid3F.length} is not equal to {@code CompiledShape3FCache.PARABOLOID_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code paraboloid3F} is {@code null}
	 */
	public int addParaboloid3F(final float[] paraboloid3F) {
		final int relativeOffsetOld = getParaboloid3FOffsetRelative(paraboloid3F);
		final int relativeOffsetNew = getParaboloid3FCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setParaboloid3Fs(FloatArrays.merge(getParaboloid3Fs(), paraboloid3F));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code plane3F} to this {@code CompiledShape3FCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code plane3F}.
	 * <p>
	 * If {@code plane3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code plane3F.length} is not equal to {@code CompiledShape3FCache.PLANE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param plane3F a {@link Plane3F} instance in compiled form
	 * @return the relative offset to {@code plane3F}
	 * @throws IllegalArgumentException thrown if, and only if, {@code plane3F.length} is not equal to {@code CompiledShape3FCache.PLANE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code plane3F} is {@code null}
	 */
	public int addPlane3F(final float[] plane3F) {
		final int relativeOffsetOld = getPlane3FOffsetRelative(plane3F);
		final int relativeOffsetNew = getPlane3FCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setPlane3Fs(FloatArrays.merge(getPlane3Fs(), plane3F));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code rectangle3F} to this {@code CompiledShape3FCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code rectangle3F}.
	 * <p>
	 * If {@code rectangle3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code rectangle3F.length} is not equal to {@code CompiledShape3FCache.RECTANGLE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param rectangle3F a {@link Rectangle3F} instance in compiled form
	 * @return the relative offset to {@code rectangle3F}
	 * @throws IllegalArgumentException thrown if, and only if, {@code rectangle3F.length} is not equal to {@code CompiledShape3FCache.RECTANGLE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code rectangle3F} is {@code null}
	 */
	public int addRectangle3F(final float[] rectangle3F) {
		final int relativeOffsetOld = getRectangle3FOffsetRelative(rectangle3F);
		final int relativeOffsetNew = getRectangle3FCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setRectangle3Fs(FloatArrays.merge(getRectangle3Fs(), rectangle3F));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code rectangularCuboid3F} to this {@code CompiledShape3FCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code rectangularCuboid3F}.
	 * <p>
	 * If {@code rectangularCuboid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code rectangularCuboid3F.length} is not equal to {@code CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param rectangularCuboid3F a {@link RectangularCuboid3F} instance in compiled form
	 * @return the relative offset to {@code rectangularCuboid3F}
	 * @throws IllegalArgumentException thrown if, and only if, {@code rectangularCuboid3F.length} is not equal to {@code CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code rectangularCuboid3F} is {@code null}
	 */
	public int addRectangularCuboid3F(final float[] rectangularCuboid3F) {
		final int relativeOffsetOld = getRectangularCuboid3FOffsetRelative(rectangularCuboid3F);
		final int relativeOffsetNew = getRectangularCuboid3FCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setRectangularCuboid3Fs(FloatArrays.merge(getRectangularCuboid3Fs(), rectangularCuboid3F));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code sphere3F} to this {@code CompiledShape3FCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code sphere3F}.
	 * <p>
	 * If {@code sphere3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code sphere3F.length} is not equal to {@code CompiledShape3FCache.SPHERE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param sphere3F a {@link Sphere3F} instance in compiled form
	 * @return the relative offset to {@code sphere3F}
	 * @throws IllegalArgumentException thrown if, and only if, {@code sphere3F.length} is not equal to {@code CompiledShape3FCache.SPHERE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code sphere3F} is {@code null}
	 */
	public int addSphere3F(final float[] sphere3F) {
		final int relativeOffsetOld = getSphere3FOffsetRelative(sphere3F);
		final int relativeOffsetNew = getSphere3FCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setSphere3Fs(FloatArrays.merge(getSphere3Fs(), sphere3F));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code torus3F} to this {@code CompiledShape3FCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code torus3F}.
	 * <p>
	 * If {@code torus3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code torus3F.length} is not equal to {@code CompiledShape3FCache.TORUS_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param torus3F a {@link Torus3F} instance in compiled form
	 * @return the relative offset to {@code torus3F}
	 * @throws IllegalArgumentException thrown if, and only if, {@code torus3F.length} is not equal to {@code CompiledShape3FCache.TORUS_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code torus3F} is {@code null}
	 */
	public int addTorus3F(final float[] torus3F) {
		final int relativeOffsetOld = getTorus3FOffsetRelative(torus3F);
		final int relativeOffsetNew = getTorus3FCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setTorus3Fs(FloatArrays.merge(getTorus3Fs(), torus3F));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code triangle3F} to this {@code CompiledShape3FCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code triangle3F}.
	 * <p>
	 * If {@code triangle3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code triangle3F.length} is not equal to {@code CompiledShape3FCache.TRIANGLE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param triangle3F a {@link Triangle3F} instance in compiled form
	 * @return the relative offset to {@code triangle3F}
	 * @throws IllegalArgumentException thrown if, and only if, {@code triangle3F.length} is not equal to {@code CompiledShape3FCache.TRIANGLE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code triangle3F} is {@code null}
	 */
	public int addTriangle3F(final float[] triangle3F) {
		final int relativeOffsetOld = getTriangle3FOffsetRelative(triangle3F);
		final int relativeOffsetNew = getTriangle3FCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setTriangle3Fs(FloatArrays.merge(getTriangle3Fs(), triangle3F));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code triangleMesh3F} to this {@code CompiledShape3FCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code triangleMesh3F}.
	 * <p>
	 * If {@code triangleMesh3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code triangleMesh3F.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param triangleMesh3F a {@link TriangleMesh3F} instance in compiled form
	 * @return the relative offset to {@code triangleMesh3F}
	 * @throws IllegalArgumentException thrown if, and only if, {@code triangleMesh3F.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code triangleMesh3F} is {@code null}
	 */
	public int addTriangleMesh3F(final int[] triangleMesh3F) {
		final int absoluteOffsetNew = this.triangleMesh3Fs.length;
		final int relativeOffsetOld = getTriangleMesh3FOffsetRelative(triangleMesh3F);
		final int relativeOffsetNew = getTriangleMesh3FCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setTriangleMesh3FOffsets(IntArrays.merge(getTriangleMesh3FOffsets(), absoluteOffsetNew));
		setTriangleMesh3Fs(IntArrays.merge(getTriangleMesh3Fs(), triangleMesh3F));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Returns the {@link Cone3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Cone3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getCone3FCount() {
		return Structures.getStructureCount(this.cone3Fs, CONE_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code cone3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code cone3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cone3F.length} is not equal to {@code CompiledShape3FCache.CONE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param cone3F a {@link Cone3F} instance in compiled form
	 * @return the absolute offset of {@code cone3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code cone3F.length} is not equal to {@code CompiledShape3FCache.CONE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code cone3F} is {@code null}
	 */
	public int getCone3FOffsetAbsolute(final float[] cone3F) {
		Objects.requireNonNull(cone3F, "cone3F == null");
		
		ParameterArguments.requireExactArrayLength(cone3F, CONE_3_F_LENGTH, "cone3F");
		
		return Structures.getStructureOffsetAbsolute(this.cone3Fs, cone3F);
	}
	
	/**
	 * Returns the relative offset of {@code cone3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code cone3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cone3F.length} is not equal to {@code CompiledShape3FCache.CONE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param cone3F a {@link Cone3F} instance in compiled form
	 * @return the relative offset of {@code cone3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code cone3F.length} is not equal to {@code CompiledShape3FCache.CONE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code cone3F} is {@code null}
	 */
	public int getCone3FOffsetRelative(final float[] cone3F) {
		Objects.requireNonNull(cone3F, "cone3F == null");
		
		ParameterArguments.requireExactArrayLength(cone3F, CONE_3_F_LENGTH, "cone3F");
		
		return Structures.getStructureOffsetRelative(this.cone3Fs, cone3F);
	}
	
	/**
	 * Returns the {@link Cylinder3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Cylinder3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getCylinder3FCount() {
		return Structures.getStructureCount(this.cylinder3Fs, CYLINDER_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code cylinder3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code cylinder3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cylinder3F.length} is not equal to {@code CompiledShape3FCache.CYLINDER_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param cylinder3F a {@link Cylinder3F} instance in compiled form
	 * @return the absolute offset of {@code cylinder3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code cylinder3F.length} is not equal to {@code CompiledShape3FCache.CYLINDER_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code cylinder3F} is {@code null}
	 */
	public int getCylinder3FOffsetAbsolute(final float[] cylinder3F) {
		Objects.requireNonNull(cylinder3F, "cylinder3F == null");
		
		ParameterArguments.requireExactArrayLength(cylinder3F, CYLINDER_3_F_LENGTH, "cylinder3F");
		
		return Structures.getStructureOffsetAbsolute(this.cylinder3Fs, cylinder3F);
	}
	
	/**
	 * Returns the relative offset of {@code cylinder3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code cylinder3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cylinder3F.length} is not equal to {@code CompiledShape3FCache.CYLINDER_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param cylinder3F a {@link Cylinder3F} instance in compiled form
	 * @return the relative offset of {@code cylinder3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code cylinder3F.length} is not equal to {@code CompiledShape3FCache.CYLINDER_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code cylinder3F} is {@code null}
	 */
	public int getCylinder3FOffsetRelative(final float[] cylinder3F) {
		Objects.requireNonNull(cylinder3F, "cylinder3F == null");
		
		ParameterArguments.requireExactArrayLength(cylinder3F, CYLINDER_3_F_LENGTH, "cylinder3F");
		
		return Structures.getStructureOffsetRelative(this.cylinder3Fs, cylinder3F);
	}
	
	/**
	 * Returns the {@link Disk3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Disk3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getDisk3FCount() {
		return Structures.getStructureCount(this.disk3Fs, DISK_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code disk3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code disk3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code disk3F.length} is not equal to {@code CompiledShape3FCache.DISK_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param disk3F a {@link Disk3F} instance in compiled form
	 * @return the absolute offset of {@code disk3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code disk3F.length} is not equal to {@code CompiledShape3FCache.DISK_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code disk3F} is {@code null}
	 */
	public int getDisk3FOffsetAbsolute(final float[] disk3F) {
		Objects.requireNonNull(disk3F, "disk3F == null");
		
		ParameterArguments.requireExactArrayLength(disk3F, DISK_3_F_LENGTH, "disk3F");
		
		return Structures.getStructureOffsetAbsolute(this.disk3Fs, disk3F);
	}
	
	/**
	 * Returns the relative offset of {@code disk3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code disk3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code disk3F.length} is not equal to {@code CompiledShape3FCache.DISK_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param disk3F a {@link Disk3F} instance in compiled form
	 * @return the relative offset of {@code disk3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code disk3F.length} is not equal to {@code CompiledShape3FCache.DISK_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code disk3F} is {@code null}
	 */
	public int getDisk3FOffsetRelative(final float[] disk3F) {
		Objects.requireNonNull(disk3F, "disk3F == null");
		
		ParameterArguments.requireExactArrayLength(disk3F, DISK_3_F_LENGTH, "disk3F");
		
		return Structures.getStructureOffsetRelative(this.disk3Fs, disk3F);
	}
	
	/**
	 * Returns the {@link Hyperboloid3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Hyperboloid3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getHyperboloid3FCount() {
		return Structures.getStructureCount(this.hyperboloid3Fs, HYPERBOLOID_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code hyperboloid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code hyperboloid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code hyperboloid3F.length} is not equal to {@code CompiledShape3FCache.HYPERBOLOID_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param hyperboloid3F a {@link Hyperboloid3F} instance in compiled form
	 * @return the absolute offset of {@code hyperboloid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code hyperboloid3F.length} is not equal to {@code CompiledShape3FCache.HYPERBOLOID_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code hyperboloid3F} is {@code null}
	 */
	public int getHyperboloid3FOffsetAbsolute(final float[] hyperboloid3F) {
		Objects.requireNonNull(hyperboloid3F, "hyperboloid3F == null");
		
		ParameterArguments.requireExactArrayLength(hyperboloid3F, HYPERBOLOID_3_F_LENGTH, "hyperboloid3F");
		
		return Structures.getStructureOffsetAbsolute(this.hyperboloid3Fs, hyperboloid3F);
	}
	
	/**
	 * Returns the relative offset of {@code hyperboloid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code hyperboloid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code hyperboloid3F.length} is not equal to {@code CompiledShape3FCache.HYPERBOLOID_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param hyperboloid3F a {@link Hyperboloid3F} instance in compiled form
	 * @return the relative offset of {@code hyperboloid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code hyperboloid3F.length} is not equal to {@code CompiledShape3FCache.HYPERBOLOID_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code hyperboloid3F} is {@code null}
	 */
	public int getHyperboloid3FOffsetRelative(final float[] hyperboloid3F) {
		Objects.requireNonNull(hyperboloid3F, "hyperboloid3F == null");
		
		ParameterArguments.requireExactArrayLength(hyperboloid3F, HYPERBOLOID_3_F_LENGTH, "hyperboloid3F");
		
		return Structures.getStructureOffsetRelative(this.hyperboloid3Fs, hyperboloid3F);
	}
	
	/**
	 * Returns the {@link Paraboloid3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Paraboloid3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getParaboloid3FCount() {
		return Structures.getStructureCount(this.paraboloid3Fs, PARABOLOID_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code paraboloid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code paraboloid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code paraboloid3F.length} is not equal to {@code CompiledShape3FCache.PARABOLOID_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param paraboloid3F a {@link Paraboloid3F} instance in compiled form
	 * @return the absolute offset of {@code paraboloid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code paraboloid3F.length} is not equal to {@code CompiledShape3FCache.PARABOLOID_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code paraboloid3F} is {@code null}
	 */
	public int getParaboloid3FOffsetAbsolute(final float[] paraboloid3F) {
		Objects.requireNonNull(paraboloid3F, "paraboloid3F == null");
		
		ParameterArguments.requireExactArrayLength(paraboloid3F, PARABOLOID_3_F_LENGTH, "paraboloid3F");
		
		return Structures.getStructureOffsetAbsolute(this.paraboloid3Fs, paraboloid3F);
	}
	
	/**
	 * Returns the relative offset of {@code paraboloid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code paraboloid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code paraboloid3F.length} is not equal to {@code CompiledShape3FCache.PARABOLOID_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param paraboloid3F a {@link Paraboloid3F} instance in compiled form
	 * @return the relative offset of {@code paraboloid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code paraboloid3F.length} is not equal to {@code CompiledShape3FCache.PARABOLOID_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code paraboloid3F} is {@code null}
	 */
	public int getParaboloid3FOffsetRelative(final float[] paraboloid3F) {
		Objects.requireNonNull(paraboloid3F, "paraboloid3F == null");
		
		ParameterArguments.requireExactArrayLength(paraboloid3F, PARABOLOID_3_F_LENGTH, "paraboloid3F");
		
		return Structures.getStructureOffsetRelative(this.paraboloid3Fs, paraboloid3F);
	}
	
	/**
	 * Returns the {@link Plane3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Plane3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getPlane3FCount() {
		return Structures.getStructureCount(this.plane3Fs, PLANE_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code plane3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code plane3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code plane3F.length} is not equal to {@code CompiledShape3FCache.PLANE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param plane3F a {@link Plane3F} instance in compiled form
	 * @return the absolute offset of {@code plane3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code plane3F.length} is not equal to {@code CompiledShape3FCache.PLANE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code plane3F} is {@code null}
	 */
	public int getPlane3FOffsetAbsolute(final float[] plane3F) {
		Objects.requireNonNull(plane3F, "plane3F == null");
		
		ParameterArguments.requireExactArrayLength(plane3F, PLANE_3_F_LENGTH, "plane3F");
		
		return Structures.getStructureOffsetAbsolute(this.plane3Fs, plane3F);
	}
	
	/**
	 * Returns the relative offset of {@code plane3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code plane3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code plane3F.length} is not equal to {@code CompiledShape3FCache.PLANE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param plane3F a {@link Plane3F} instance in compiled form
	 * @return the relative offset of {@code plane3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code plane3F.length} is not equal to {@code CompiledShape3FCache.PLANE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code plane3F} is {@code null}
	 */
	public int getPlane3FOffsetRelative(final float[] plane3F) {
		Objects.requireNonNull(plane3F, "plane3F == null");
		
		ParameterArguments.requireExactArrayLength(plane3F, PLANE_3_F_LENGTH, "plane3F");
		
		return Structures.getStructureOffsetRelative(this.plane3Fs, plane3F);
	}
	
	/**
	 * Returns the {@link Rectangle3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Rectangle3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getRectangle3FCount() {
		return Structures.getStructureCount(this.rectangle3Fs, RECTANGLE_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code rectangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code rectangle3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code rectangle3F.length} is not equal to {@code CompiledShape3FCache.RECTANGLE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param rectangle3F a {@link Rectangle3F} instance in compiled form
	 * @return the absolute offset of {@code rectangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code rectangle3F.length} is not equal to {@code CompiledShape3FCache.RECTANGLE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code rectangle3F} is {@code null}
	 */
	public int getRectangle3FOffsetAbsolute(final float[] rectangle3F) {
		Objects.requireNonNull(rectangle3F, "rectangle3F == null");
		
		ParameterArguments.requireExactArrayLength(rectangle3F, RECTANGLE_3_F_LENGTH, "rectangle3F");
		
		return Structures.getStructureOffsetAbsolute(this.rectangle3Fs, rectangle3F);
	}
	
	/**
	 * Returns the relative offset of {@code rectangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code rectangle3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code rectangle3F.length} is not equal to {@code CompiledShape3FCache.RECTANGLE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param rectangle3F a {@link Rectangle3F} instance in compiled form
	 * @return the relative offset of {@code rectangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code rectangle3F.length} is not equal to {@code CompiledShape3FCache.RECTANGLE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code rectangle3F} is {@code null}
	 */
	public int getRectangle3FOffsetRelative(final float[] rectangle3F) {
		Objects.requireNonNull(rectangle3F, "rectangle3F == null");
		
		ParameterArguments.requireExactArrayLength(rectangle3F, RECTANGLE_3_F_LENGTH, "rectangle3F");
		
		return Structures.getStructureOffsetRelative(this.rectangle3Fs, rectangle3F);
	}
	
	/**
	 * Returns the {@link RectangularCuboid3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code RectangularCuboid3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getRectangularCuboid3FCount() {
		return Structures.getStructureCount(this.rectangularCuboid3Fs, RECTANGULAR_CUBOID_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code rectangularCuboid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code rectangularCuboid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code rectangularCuboid3F.length} is not equal to {@code CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param rectangularCuboid3F a {@link RectangularCuboid3F} instance in compiled form
	 * @return the absolute offset of {@code rectangularCuboid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code rectangularCuboid3F.length} is not equal to {@code CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code rectangularCuboid3F} is {@code null}
	 */
	public int getRectangularCuboid3FOffsetAbsolute(final float[] rectangularCuboid3F) {
		Objects.requireNonNull(rectangularCuboid3F, "rectangularCuboid3F == null");
		
		ParameterArguments.requireExactArrayLength(rectangularCuboid3F, RECTANGULAR_CUBOID_3_F_LENGTH, "rectangularCuboid3F");
		
		return Structures.getStructureOffsetAbsolute(this.rectangularCuboid3Fs, rectangularCuboid3F);
	}
	
	/**
	 * Returns the relative offset of {@code rectangularCuboid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code rectangularCuboid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code rectangularCuboid3F.length} is not equal to {@code CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param rectangularCuboid3F a {@link RectangularCuboid3F} instance in compiled form
	 * @return the relative offset of {@code rectangularCuboid3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code rectangularCuboid3F.length} is not equal to {@code CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code rectangularCuboid3F} is {@code null}
	 */
	public int getRectangularCuboid3FOffsetRelative(final float[] rectangularCuboid3F) {
		Objects.requireNonNull(rectangularCuboid3F, "rectangularCuboid3F == null");
		
		ParameterArguments.requireExactArrayLength(rectangularCuboid3F, RECTANGULAR_CUBOID_3_F_LENGTH, "rectangularCuboid3F");
		
		return Structures.getStructureOffsetRelative(this.rectangularCuboid3Fs, rectangularCuboid3F);
	}
	
	/**
	 * Returns the {@link Sphere3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Sphere3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getSphere3FCount() {
		return Structures.getStructureCount(this.sphere3Fs, SPHERE_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code sphere3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code sphere3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code sphere3F.length} is not equal to {@code CompiledShape3FCache.SPHERE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param sphere3F a {@link Sphere3F} instance in compiled form
	 * @return the absolute offset of {@code sphere3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code sphere3F.length} is not equal to {@code CompiledShape3FCache.SPHERE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code sphere3F} is {@code null}
	 */
	public int getSphere3FOffsetAbsolute(final float[] sphere3F) {
		Objects.requireNonNull(sphere3F, "sphere3F == null");
		
		ParameterArguments.requireExactArrayLength(sphere3F, SPHERE_3_F_LENGTH, "sphere3F");
		
		return Structures.getStructureOffsetAbsolute(this.sphere3Fs, sphere3F);
	}
	
	/**
	 * Returns the relative offset of {@code sphere3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code sphere3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code sphere3F.length} is not equal to {@code CompiledShape3FCache.SPHERE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param sphere3F a {@link Sphere3F} instance in compiled form
	 * @return the relative offset of {@code sphere3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code sphere3F.length} is not equal to {@code CompiledShape3FCache.SPHERE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code sphere3F} is {@code null}
	 */
	public int getSphere3FOffsetRelative(final float[] sphere3F) {
		Objects.requireNonNull(sphere3F, "sphere3F == null");
		
		ParameterArguments.requireExactArrayLength(sphere3F, SPHERE_3_F_LENGTH, "sphere3F");
		
		return Structures.getStructureOffsetRelative(this.sphere3Fs, sphere3F);
	}
	
	/**
	 * Returns the {@link Torus3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Torus3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getTorus3FCount() {
		return Structures.getStructureCount(this.torus3Fs, TORUS_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code torus3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code torus3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code torus3F.length} is not equal to {@code CompiledShape3FCache.TORUS_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param torus3F a {@link Torus3F} instance in compiled form
	 * @return the absolute offset of {@code torus3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code torus3F.length} is not equal to {@code CompiledShape3FCache.TORUS_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code torus3F} is {@code null}
	 */
	public int getTorus3FOffsetAbsolute(final float[] torus3F) {
		Objects.requireNonNull(torus3F, "torus3F == null");
		
		ParameterArguments.requireExactArrayLength(torus3F, TORUS_3_F_LENGTH, "torus3F");
		
		return Structures.getStructureOffsetAbsolute(this.torus3Fs, torus3F);
	}
	
	/**
	 * Returns the relative offset of {@code torus3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code torus3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code torus3F.length} is not equal to {@code CompiledShape3FCache.TORUS_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param torus3F a {@link Torus3F} instance in compiled form
	 * @return the relative offset of {@code torus3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code torus3F.length} is not equal to {@code CompiledShape3FCache.TORUS_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code torus3F} is {@code null}
	 */
	public int getTorus3FOffsetRelative(final float[] torus3F) {
		Objects.requireNonNull(torus3F, "torus3F == null");
		
		ParameterArguments.requireExactArrayLength(torus3F, TORUS_3_F_LENGTH, "torus3F");
		
		return Structures.getStructureOffsetRelative(this.torus3Fs, torus3F);
	}
	
	/**
	 * Returns the {@link Triangle3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code Triangle3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getTriangle3FCount() {
		return Structures.getStructureCount(this.triangle3Fs, TRIANGLE_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code triangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code triangle3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code triangle3F.length} is not equal to {@code CompiledShape3FCache.TRIANGLE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param triangle3F a {@link Triangle3F} instance in compiled form
	 * @return the absolute offset of {@code triangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code triangle3F.length} is not equal to {@code CompiledShape3FCache.TRIANGLE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code triangle3F} is {@code null}
	 */
	public int getTriangle3FOffsetAbsolute(final float[] triangle3F) {
		Objects.requireNonNull(triangle3F, "triangle3F == null");
		
		ParameterArguments.requireExactArrayLength(triangle3F, TRIANGLE_3_F_LENGTH, "triangle3F");
		
		return Structures.getStructureOffsetAbsolute(this.triangle3Fs, triangle3F);
	}
	
	/**
	 * Returns the relative offset of {@code triangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code triangle3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code triangle3F.length} is not equal to {@code CompiledShape3FCache.TRIANGLE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param triangle3F a {@link Triangle3F} instance in compiled form
	 * @return the relative offset of {@code triangle3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code triangle3F.length} is not equal to {@code CompiledShape3FCache.TRIANGLE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code triangle3F} is {@code null}
	 */
	public int getTriangle3FOffsetRelative(final float[] triangle3F) {
		Objects.requireNonNull(triangle3F, "triangle3F == null");
		
		ParameterArguments.requireExactArrayLength(triangle3F, TRIANGLE_3_F_LENGTH, "triangle3F");
		
		return Structures.getStructureOffsetRelative(this.triangle3Fs, triangle3F);
	}
	
	/**
	 * Returns the {@link TriangleMesh3F} count in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return the {@code TriangleMesh3F} count in this {@code CompiledShape3FCache} instance
	 */
	public int getTriangleMesh3FCount() {
		return this.triangleMesh3FOffsets.length;
	}
	
	/**
	 * Returns the absolute offset of {@code triangleMesh3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code triangleMesh3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code triangleMesh3F.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param triangleMesh3F a {@link TriangleMesh3F} instance in compiled form
	 * @return the absolute offset of {@code triangleMesh3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code triangleMesh3F.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code triangleMesh3F} is {@code null}
	 */
	public int getTriangleMesh3FOffsetAbsolute(final int[] triangleMesh3F) {
		Objects.requireNonNull(triangleMesh3F, "triangleMesh3F == null");
		
		ParameterArguments.requireExact(triangleMesh3F.length % 8, 0, "triangleMesh3F.length % 8");
		
		return Structures.getStructureOffsetAbsolute(this.triangleMesh3Fs, triangleMesh3F, this.triangleMesh3FOffsets);
	}
	
	/**
	 * Returns the relative offset of {@code triangleMesh3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code triangleMesh3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code triangleMesh3F.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param triangleMesh3F a {@link TriangleMesh3F} instance in compiled form
	 * @return the relative offset of {@code triangleMesh3F} in this {@code CompiledShape3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code triangleMesh3F.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code triangleMesh3F} is {@code null}
	 */
	public int getTriangleMesh3FOffsetRelative(final int[] triangleMesh3F) {
		Objects.requireNonNull(triangleMesh3F, "triangleMesh3F == null");
		
		ParameterArguments.requireExact(triangleMesh3F.length % 8, 0, "triangleMesh3F.length % 8");
		
		return Structures.getStructureOffsetRelative(this.triangleMesh3Fs, triangleMesh3F, this.triangleMesh3FOffsets);
	}
	
	/**
	 * Returns an {@code int[]} that contains the offsets for all {@link TriangleMesh3F} instances in this {@code CompiledShape3FCache} instance.
	 * 
	 * @return an {@code int[]} that contains the offsets for all {@code TriangleMesh3F} instances in this {@code CompiledShape3FCache} instance
	 */
	public int[] getTriangleMesh3FOffsets() {
		return this.triangleMesh3FOffsets;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link TriangleMesh3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code TriangleMesh3F} instances in compiled form that are associated with this {@code CompiledShape3FCache} instance
	 */
	public int[] getTriangleMesh3Fs() {
		return this.triangleMesh3Fs;
	}
	
	/**
	 * Sets all {@link Cone3F} instances in compiled form to {@code cone3Fs}.
	 * <p>
	 * If {@code cone3Fs} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cone3Fs.length % CompiledShape3FCache.CONE_3_F_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param cone3Fs the {@code Cone3F} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code cone3Fs.length % CompiledShape3FCache.CONE_3_F_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code cone3Fs} is {@code null}
	 */
	public void setCone3Fs(final float[] cone3Fs) {
		Objects.requireNonNull(cone3Fs, "cone3Fs == null");
		
		ParameterArguments.requireExact(cone3Fs.length % CONE_3_F_LENGTH, 0, "cone3Fs.length % CompiledShape3FCache.CONE_3_F_LENGTH");
		
		this.cone3Fs = cone3Fs;
	}
	
	/**
	 * Sets all {@link Cylinder3F} instances in compiled form to {@code cylinder3Fs}.
	 * <p>
	 * If {@code cylinder3Fs} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code cylinder3Fs.length % CompiledShape3FCache.CYLINDER_3_F_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param cylinder3Fs the {@code Cylinder3F} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code cylinder3Fs.length % CompiledShape3FCache.CYLINDER_3_F_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code cylinder3Fs} is {@code null}
	 */
	public void setCylinder3Fs(final float[] cylinder3Fs) {
		Objects.requireNonNull(cylinder3Fs, "cylinder3Fs == null");
		
		ParameterArguments.requireExact(cylinder3Fs.length % CYLINDER_3_F_LENGTH, 0, "cylinder3Fs.length % CompiledShape3FCache.CYLINDER_3_F_LENGTH");
		
		this.cylinder3Fs = cylinder3Fs;
	}
	
	/**
	 * Sets all {@link Disk3F} instances in compiled form to {@code disk3Fs}.
	 * <p>
	 * If {@code disk3Fs} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code disk3Fs.length % CompiledShape3FCache.DISK_3_F_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param disk3Fs the {@code Disk3F} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code disk3Fs.length % CompiledShape3FCache.DISK_3_F_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code disk3Fs} is {@code null}
	 */
	public void setDisk3Fs(final float[] disk3Fs) {
		Objects.requireNonNull(disk3Fs, "disk3Fs == null");
		
		ParameterArguments.requireExact(disk3Fs.length % DISK_3_F_LENGTH, 0, "disk3Fs.length % CompiledShape3FCache.DISK_3_F_LENGTH");
		
		this.disk3Fs = disk3Fs;
	}
	
	/**
	 * Sets all {@link Hyperboloid3F} instances in compiled form to {@code hyperboloid3Fs}.
	 * <p>
	 * If {@code hyperboloid3Fs} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code hyperboloid3Fs.length % CompiledShape3FCache.HYPERBOLOID_3_F_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param hyperboloid3Fs the {@code Hyperboloid3F} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code hyperboloid3Fs.length % CompiledShape3FCache.HYPERBOLOID_3_F_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code hyperboloid3Fs} is {@code null}
	 */
	public void setHyperboloid3Fs(final float[] hyperboloid3Fs) {
		Objects.requireNonNull(hyperboloid3Fs, "hyperboloid3Fs == null");
		
		ParameterArguments.requireExact(hyperboloid3Fs.length % HYPERBOLOID_3_F_LENGTH, 0, "hyperboloid3Fs.length % CompiledShape3FCache.HYPERBOLOID_3_F_LENGTH");
		
		this.hyperboloid3Fs = hyperboloid3Fs;
	}
	
	/**
	 * Sets all {@link Paraboloid3F} instances in compiled form to {@code paraboloid3Fs}.
	 * <p>
	 * If {@code paraboloid3Fs} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code paraboloid3Fs.length % CompiledShape3FCache.PARABOLOID_3_F_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param paraboloid3Fs the {@code Paraboloid3F} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code paraboloid3Fs.length % CompiledShape3FCache.PARABOLOID_3_F_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code paraboloid3Fs} is {@code null}
	 */
	public void setParaboloid3Fs(final float[] paraboloid3Fs) {
		Objects.requireNonNull(paraboloid3Fs, "paraboloid3Fs == null");
		
		ParameterArguments.requireExact(paraboloid3Fs.length % PARABOLOID_3_F_LENGTH, 0, "paraboloid3Fs.length % CompiledShape3FCache.PARABOLOID_3_F_LENGTH");
		
		this.paraboloid3Fs = paraboloid3Fs;
	}
	
	/**
	 * Sets all {@link Plane3F} instances in compiled form to {@code plane3Fs}.
	 * <p>
	 * If {@code plane3Fs} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code plane3Fs.length % CompiledShape3FCache.PLANE_3_F_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param plane3Fs the {@code Plane3F} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code plane3Fs.length % CompiledShape3FCache.PLANE_3_F_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code plane3Fs} is {@code null}
	 */
	public void setPlane3Fs(final float[] plane3Fs) {
		Objects.requireNonNull(plane3Fs, "plane3Fs == null");
		
		ParameterArguments.requireExact(plane3Fs.length % PLANE_3_F_LENGTH, 0, "plane3Fs.length % CompiledShape3FCache.PLANE_3_F_LENGTH");
		
		this.plane3Fs = plane3Fs;
	}
	
	/**
	 * Sets all {@link Rectangle3F} instances in compiled form to {@code rectangle3Fs}.
	 * <p>
	 * If {@code rectangle3Fs} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code rectangle3Fs.length % CompiledShape3FCache.RECTANGLE_3_F_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param rectangle3Fs the {@code Rectangle3F} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code rectangle3Fs.length % CompiledShape3FCache.RECTANGLE_3_F_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code rectangle3Fs} is {@code null}
	 */
	public void setRectangle3Fs(final float[] rectangle3Fs) {
		Objects.requireNonNull(rectangle3Fs, "rectangle3Fs == null");
		
		ParameterArguments.requireExact(rectangle3Fs.length % RECTANGLE_3_F_LENGTH, 0, "rectangle3Fs.length % CompiledShape3FCache.RECTANGLE_3_F_LENGTH");
		
		this.rectangle3Fs = rectangle3Fs;
	}
	
	/**
	 * Sets all {@link RectangularCuboid3F} instances in compiled form to {@code rectangularCuboid3Fs}.
	 * <p>
	 * If {@code rectangularCuboid3Fs} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code rectangularCuboid3Fs.length % CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param rectangularCuboid3Fs the {@code RectangularCuboid3F} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code rectangularCuboid3Fs.length % CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code rectangularCuboid3Fs} is {@code null}
	 */
	public void setRectangularCuboid3Fs(final float[] rectangularCuboid3Fs) {
		Objects.requireNonNull(rectangularCuboid3Fs, "rectangularCuboid3Fs == null");
		
		ParameterArguments.requireExact(rectangularCuboid3Fs.length % RECTANGULAR_CUBOID_3_F_LENGTH, 0, "rectangularCuboid3Fs.length % CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_LENGTH");
		
		this.rectangularCuboid3Fs = rectangularCuboid3Fs;
	}
	
	/**
	 * Sets all {@link Sphere3F} instances in compiled form to {@code sphere3Fs}.
	 * <p>
	 * If {@code sphere3Fs} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code sphere3Fs.length % CompiledShape3FCache.SPHERE_3_F_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param sphere3Fs the {@code Sphere3F} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code sphere3Fs.length % CompiledShape3FCache.SPHERE_3_F_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code sphere3Fs} is {@code null}
	 */
	public void setSphere3Fs(final float[] sphere3Fs) {
		Objects.requireNonNull(sphere3Fs, "sphere3Fs == null");
		
		ParameterArguments.requireExact(sphere3Fs.length % SPHERE_3_F_LENGTH, 0, "sphere3Fs.length % CompiledShape3FCache.SPHERE_3_F_LENGTH");
		
		this.sphere3Fs = sphere3Fs;
	}
	
	/**
	 * Sets all {@link Torus3F} instances in compiled form to {@code torus3Fs}.
	 * <p>
	 * If {@code torus3Fs} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code torus3Fs.length % CompiledShape3FCache.TORUS_3_F_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param torus3Fs the {@code Torus3F} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code torus3Fs.length % CompiledShape3FCache.TORUS_3_F_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code torus3Fs} is {@code null}
	 */
	public void setTorus3Fs(final float[] torus3Fs) {
		Objects.requireNonNull(torus3Fs, "torus3Fs == null");
		
		ParameterArguments.requireExact(torus3Fs.length % TORUS_3_F_LENGTH, 0, "torus3Fs.length % CompiledShape3FCache.TORUS_3_F_LENGTH");
		
		this.torus3Fs = torus3Fs;
	}
	
	/**
	 * Sets all {@link Triangle3F} instances in compiled form to {@code triangle3Fs}.
	 * <p>
	 * If {@code triangle3Fs} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code triangle3Fs.length % CompiledShape3FCache.TRIANGLE_3_F_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param triangle3Fs the {@code Triangle3F} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code triangle3Fs.length % CompiledShape3FCache.TRIANGLE_3_F_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code triangle3Fs} is {@code null}
	 */
	public void setTriangle3Fs(final float[] triangle3Fs) {
		Objects.requireNonNull(triangle3Fs, "triangle3Fs == null");
		
		ParameterArguments.requireExact(triangle3Fs.length % TRIANGLE_3_F_LENGTH, 0, "triangle3Fs.length % CompiledShape3FCache.TRIANGLE_3_F_LENGTH");
		
		this.triangle3Fs = triangle3Fs;
	}
	
	/**
	 * Sets the {@code int[]} that contains the offsets for all {@link TriangleMesh3F} instances to {@code triangleMesh3FOffsets}.
	 * <p>
	 * If {@code triangleMesh3FOffsets} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If at least one offset in {@code triangleMesh3FOffsets} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param triangleMesh3FOffsets the {@code int[]} that contains the offsets for all {@code TriangleMesh3F} instances
	 * @throws IllegalArgumentException thrown if, and only if, at least one offset in {@code triangleMesh3FOffsets} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code triangleMesh3FOffsets} is {@code null}
	 */
	public void setTriangleMesh3FOffsets(final int[] triangleMesh3FOffsets) {
		Objects.requireNonNull(triangleMesh3FOffsets, "triangleMesh3FOffsets == null");
		
		ParameterArguments.requireRange(triangleMesh3FOffsets, 0, Integer.MAX_VALUE, "triangleMesh3FOffsets");
		
		this.triangleMesh3FOffsets = triangleMesh3FOffsets;
	}
	
	/**
	 * Sets all {@link TriangleMesh3F} instances in compiled form to {@code triangleMesh3Fs}.
	 * <p>
	 * If {@code triangleMesh3Fs} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code triangleMesh3Fs.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param triangleMesh3Fs the {@code TriangleMesh3F} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code triangleMesh3Fs.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code triangleMesh3Fs} is {@code null}
	 */
	public void setTriangleMesh3Fs(final int[] triangleMesh3Fs) {
		Objects.requireNonNull(triangleMesh3Fs, "triangleMesh3Fs == null");
		
		ParameterArguments.requireExact(triangleMesh3Fs.length % 8, 0, "triangleMesh3Fs.length % 8");
		
		this.triangleMesh3Fs = triangleMesh3Fs;
	}
	
	/**
	 * Writes this {@code CompiledShape3FCache} instance to {@code document}.
	 * <p>
	 * If {@code document} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param document a {@link Document} instance
	 * @throws NullPointerException thrown if, and only if, {@code document} is {@code null}
	 */
	public void write(final Document document) {
		document.line("CompiledShape3FCache {");
		document.indent();
		document.linef("cone3Fs[%d]", Integer.valueOf(getCone3FCount()));
		document.linef("cylinder3Fs[%d]", Integer.valueOf(getCylinder3FCount()));
		document.linef("disk3Fs[%d]", Integer.valueOf(getDisk3FCount()));
		document.linef("hyperboloid3Fs[%d]", Integer.valueOf(getHyperboloid3FCount()));
		document.linef("paraboloid3Fs[%d]", Integer.valueOf(getParaboloid3FCount()));
		document.linef("plane3Fs[%d]", Integer.valueOf(getPlane3FCount()));
		document.linef("rectangle3Fs[%d]", Integer.valueOf(getRectangle3FCount()));
		document.linef("rectangularCuboid3Fs[%d]", Integer.valueOf(getRectangularCuboid3FCount()));
		document.linef("sphere3Fs[%d]", Integer.valueOf(getSphere3FCount()));
		document.linef("torus3Fs[%d]", Integer.valueOf(getTorus3FCount()));
		document.linef("triangle3Fs[%d]", Integer.valueOf(getTriangle3FCount()));
		document.linef("triangleMesh3Fs[%d]", Integer.valueOf(getTriangleMesh3FCount()));
		document.outdent();
		document.line("}");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, {@code shape3F} is supported, {@code false} otherwise.
	 * <p>
	 * If {@code shape3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3F a {@link Shape3F} instance
	 * @return {@code true} if, and only if, {@code shape3F} is supported, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code shape3F} is {@code null}
	 */
	public static boolean isSupported(final Shape3F shape3F) {
		Objects.requireNonNull(shape3F, "shape3F == null");
		
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
	
	/**
	 * Returns a {@code float[]} with {@code cone3F} in compiled form.
	 * <p>
	 * If {@code cone3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param cone3F a {@link Cone3F} instance
	 * @return a {@code float[]} with {@code cone3F} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code cone3F} is {@code null}
	 */
	public static float[] toCone3F(final Cone3F cone3F) {
		final float phiMax = cone3F.getPhiMax().getRadians();
		final float radius = cone3F.getRadius();
		final float zMax = cone3F.getZMax();
		
		final float[] array = new float[CONE_3_F_LENGTH];
		
		array[CONE_3_F_OFFSET_PHI_MAX] = phiMax;
		array[CONE_3_F_OFFSET_RADIUS] = radius;
		array[CONE_3_F_OFFSET_Z_MAX] = zMax;
		array[3] = 0.0F;
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link Cone3F} instances in {@code cone3Fs} in compiled form.
	 * <p>
	 * If {@code cone3Fs} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param cone3Fs a {@code List} of {@code Cone3F} instances
	 * @return a {@code float[]} with all {@code Cone3F} instances in {@code cone3Fs} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code cone3Fs} or at least one of its elements are {@code null}
	 */
	public static float[] toCone3Fs(final List<Cone3F> cone3Fs) {
		return Floats.toArray(cone3Fs, cone3F -> toCone3F(cone3F));
	}
	
	/**
	 * Returns a {@code float[]} with {@code cylinder3F} in compiled form.
	 * <p>
	 * If {@code cylinder3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param cylinder3F a {@link Cylinder3F} instance
	 * @return a {@code float[]} with {@code cylinder3F} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code cylinder3F} is {@code null}
	 */
	public static float[] toCylinder3F(final Cylinder3F cylinder3F) {
		final float phiMax = cylinder3F.getPhiMax().getRadians();
		final float radius = cylinder3F.getRadius();
		final float zMax = cylinder3F.getZMax();
		final float zMin = cylinder3F.getZMin();
		
		final float[] array = new float[CYLINDER_3_F_LENGTH];
		
		array[CYLINDER_3_F_OFFSET_PHI_MAX] = phiMax;
		array[CYLINDER_3_F_OFFSET_RADIUS] = radius;
		array[CYLINDER_3_F_OFFSET_Z_MAX] = zMax;
		array[CYLINDER_3_F_OFFSET_Z_MIN] = zMin;
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link Cylinder3F} instances in {@code cylinder3Fs} in compiled form.
	 * <p>
	 * If {@code cylinder3Fs} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param cylinder3Fs a {@code List} of {@code Cylinder3F} instances
	 * @return a {@code float[]} with all {@code Cylinder3F} instances in {@code cylinder3Fs} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code cylinder3Fs} or at least one of its elements are {@code null}
	 */
	public static float[] toCylinder3Fs(final List<Cylinder3F> cylinder3Fs) {
		return Floats.toArray(cylinder3Fs, cylinder3F -> toCylinder3F(cylinder3F));
	}
	
	/**
	 * Returns a {@code float[]} with {@code disk3F} in compiled form.
	 * <p>
	 * If {@code disk3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param disk3F a {@link Disk3F} instance
	 * @return a {@code float[]} with {@code disk3F} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code disk3F} is {@code null}
	 */
	public static float[] toDisk3F(final Disk3F disk3F) {
		final float phiMax = disk3F.getPhiMax().getRadians();
		final float radiusInner = disk3F.getRadiusInner();
		final float radiusOuter = disk3F.getRadiusOuter();
		final float zMax = disk3F.getZMax();
		
		final float[] array = new float[DISK_3_F_LENGTH];
		
		array[DISK_3_F_OFFSET_PHI_MAX] = phiMax;
		array[DISK_3_F_OFFSET_RADIUS_INNER] = radiusInner;
		array[DISK_3_F_OFFSET_RADIUS_OUTER] = radiusOuter;
		array[DISK_3_F_OFFSET_Z_MAX] = zMax;
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link Disk3F} instances in {@code disk3Fs} in compiled form.
	 * <p>
	 * If {@code disk3Fs} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param disk3Fs a {@code List} of {@code Disk3F} instances
	 * @return a {@code float[]} with all {@code Disk3F} instances in {@code disk3Fs} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code disk3Fs} or at least one of its elements are {@code null}
	 */
	public static float[] toDisk3Fs(final List<Disk3F> disk3Fs) {
		return Floats.toArray(disk3Fs, disk3F -> toDisk3F(disk3F));
	}
	
	/**
	 * Returns a {@code float[]} with {@code hyperboloid3F} in compiled form.
	 * <p>
	 * If {@code hyperboloid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param hyperboloid3F a {@link Hyperboloid3F} instance
	 * @return a {@code float[]} with {@code hyperboloid3F} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code hyperboloid3F} is {@code null}
	 */
	public static float[] toHyperboloid3F(final Hyperboloid3F hyperboloid3F) {
		final Point3F a = hyperboloid3F.getA();
		final Point3F b = hyperboloid3F.getB();
		
		final float phiMax = hyperboloid3F.getPhiMax().getRadians();
		final float aH = hyperboloid3F.getAH();
		final float cH = hyperboloid3F.getCH();
		final float rMax = hyperboloid3F.getRMax();
		final float zMax = hyperboloid3F.getZMax();
		final float zMin = hyperboloid3F.getZMin();
		
		final float[] array = new float[HYPERBOLOID_3_F_LENGTH];
		
		array[HYPERBOLOID_3_F_OFFSET_PHI_MAX] = phiMax;
		array[HYPERBOLOID_3_F_OFFSET_POINT_A + 0] = a.getX();
		array[HYPERBOLOID_3_F_OFFSET_POINT_A + 1] = a.getY();
		array[HYPERBOLOID_3_F_OFFSET_POINT_A + 2] = a.getZ();
		array[HYPERBOLOID_3_F_OFFSET_POINT_B + 0] = b.getX();
		array[HYPERBOLOID_3_F_OFFSET_POINT_B + 1] = b.getY();
		array[HYPERBOLOID_3_F_OFFSET_POINT_B + 2] = b.getZ();
		array[HYPERBOLOID_3_F_OFFSET_A_H] = aH;
		array[HYPERBOLOID_3_F_OFFSET_C_H] = cH;
		array[HYPERBOLOID_3_F_OFFSET_R_MAX] = rMax;
		array[HYPERBOLOID_3_F_OFFSET_Z_MAX] = zMax;
		array[HYPERBOLOID_3_F_OFFSET_Z_MIN] = zMin;
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link Hyperboloid3F} instances in {@code hyperboloid3Fs} in compiled form.
	 * <p>
	 * If {@code hyperboloid3Fs} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param hyperboloid3Fs a {@code List} of {@code Hyperboloid3F} instances
	 * @return a {@code float[]} with all {@code Hyperboloid3F} instances in {@code hyperboloid3Fs} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code hyperboloid3Fs} or at least one of its elements are {@code null}
	 */
	public static float[] toHyperboloid3Fs(final List<Hyperboloid3F> hyperboloid3Fs) {
		return Floats.toArray(hyperboloid3Fs, hyperboloid3F -> toHyperboloid3F(hyperboloid3F));
	}
	
	/**
	 * Returns a {@code float[]} with {@code paraboloid3F} in compiled form.
	 * <p>
	 * If {@code paraboloid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param paraboloid3F a {@link Paraboloid3F} instance
	 * @return a {@code float[]} with {@code paraboloid3F} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code paraboloid3F} is {@code null}
	 */
	public static float[] toParaboloid3F(final Paraboloid3F paraboloid3F) {
		final float phiMax = paraboloid3F.getPhiMax().getRadians();
		final float radius = paraboloid3F.getRadius();
		final float zMax = paraboloid3F.getZMax();
		final float zMin = paraboloid3F.getZMin();
		
		final float[] array = new float[PARABOLOID_3_F_LENGTH];
		
		array[PARABOLOID_3_F_OFFSET_PHI_MAX] = phiMax;
		array[PARABOLOID_3_F_OFFSET_RADIUS] = radius;
		array[PARABOLOID_3_F_OFFSET_Z_MAX] = zMax;
		array[PARABOLOID_3_F_OFFSET_Z_MIN] = zMin;
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link Paraboloid3F} instances in {@code paraboloid3Fs} in compiled form.
	 * <p>
	 * If {@code paraboloid3Fs} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param paraboloid3Fs a {@code List} of {@code Paraboloid3F} instances
	 * @return a {@code float[]} with all {@code Paraboloid3F} instances in {@code paraboloid3Fs} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code paraboloid3Fs} or at least one of its elements are {@code null}
	 */
	public static float[] toParaboloid3Fs(final List<Paraboloid3F> paraboloid3Fs) {
		return Floats.toArray(paraboloid3Fs, paraboloid3F -> toParaboloid3F(paraboloid3F));
	}
	
	/**
	 * Returns a {@code float[]} with {@code plane3F} in compiled form.
	 * <p>
	 * If {@code plane3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param plane3F a {@link Plane3F} instance
	 * @return a {@code float[]} with {@code plane3F} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code plane3F} is {@code null}
	 */
	public static float[] toPlane3F(final Plane3F plane3F) {
		final Point3F a = plane3F.getA();
		final Point3F b = plane3F.getB();
		final Point3F c = plane3F.getC();
		
		final Vector3F surfaceNormal = plane3F.getSurfaceNormal();
		
		final float[] array = new float[PLANE_3_F_LENGTH];
		
		array[PLANE_3_F_OFFSET_A + 0] = a.getX();							//Block #1
		array[PLANE_3_F_OFFSET_A + 1] = a.getY();							//Block #1
		array[PLANE_3_F_OFFSET_A + 2] = a.getZ();							//Block #1
		array[PLANE_3_F_OFFSET_B + 0] = b.getX();							//Block #1
		array[PLANE_3_F_OFFSET_B + 1] = b.getY();							//Block #1
		array[PLANE_3_F_OFFSET_B + 2] = b.getZ();							//Block #1
		array[PLANE_3_F_OFFSET_C + 0] = c.getX();							//Block #1
		array[PLANE_3_F_OFFSET_C + 1] = c.getY();							//Block #1
		array[PLANE_3_F_OFFSET_C + 2] = c.getZ();							//Block #2
		array[PLANE_3_F_OFFSET_SURFACE_NORMAL + 0] = surfaceNormal.getX();	//Block #2
		array[PLANE_3_F_OFFSET_SURFACE_NORMAL + 1] = surfaceNormal.getY();	//Block #2
		array[PLANE_3_F_OFFSET_SURFACE_NORMAL + 2] = surfaceNormal.getZ();	//Block #2
		array[12] = 0.0F;													//Block #2
		array[13] = 0.0F;													//Block #2
		array[14] = 0.0F;													//Block #2
		array[15] = 0.0F;													//Block #2
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link Plane3F} instances in {@code plane3Fs} in compiled form.
	 * <p>
	 * If {@code plane3Fs} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param plane3Fs a {@code List} of {@code Plane3F} instances
	 * @return a {@code float[]} with all {@code Plane3F} instances in {@code plane3Fs} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code plane3Fs} or at least one of its elements are {@code null}
	 */
	public static float[] toPlane3Fs(final List<Plane3F> plane3Fs) {
		return Floats.toArray(plane3Fs, plane3F -> toPlane3F(plane3F));
	}
	
	/**
	 * Returns a {@code float[]} with {@code rectangle3F} in compiled form.
	 * <p>
	 * If {@code rectangle3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rectangle3F a {@link Rectangle3F} instance
	 * @return a {@code float[]} with {@code rectangle3F} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code rectangle3F} is {@code null}
	 */
	public static float[] toRectangle3F(final Rectangle3F rectangle3F) {
		final Point3F position = rectangle3F.getPosition();
		
		final Vector3F sideA = rectangle3F.getSideA();
		final Vector3F sideB = rectangle3F.getSideB();
		final Vector3F surfaceNormal = rectangle3F.getSurfaceNormal();
		
		final float[] array = new float[RECTANGLE_3_F_LENGTH];
		
		array[RECTANGLE_3_F_OFFSET_POSITION + 0] = position.getX();				//Block #1
		array[RECTANGLE_3_F_OFFSET_POSITION + 1] = position.getY();				//Block #1
		array[RECTANGLE_3_F_OFFSET_POSITION + 2] = position.getZ();				//Block #1
		array[RECTANGLE_3_F_OFFSET_SIDE_A + 0] = sideA.getX();					//Block #1
		array[RECTANGLE_3_F_OFFSET_SIDE_A + 1] = sideA.getY();					//Block #1
		array[RECTANGLE_3_F_OFFSET_SIDE_A + 2] = sideA.getZ();					//Block #1
		array[RECTANGLE_3_F_OFFSET_SIDE_B + 0] = sideB.getX();					//Block #1
		array[RECTANGLE_3_F_OFFSET_SIDE_B + 1] = sideB.getY();					//Block #1
		array[RECTANGLE_3_F_OFFSET_SIDE_B + 2] = sideB.getZ();					//Block #2
		array[RECTANGLE_3_F_OFFSET_SURFACE_NORMAL + 0] = surfaceNormal.getX();	//Block #2
		array[RECTANGLE_3_F_OFFSET_SURFACE_NORMAL + 1] = surfaceNormal.getY();	//Block #2
		array[RECTANGLE_3_F_OFFSET_SURFACE_NORMAL + 2] = surfaceNormal.getZ();	//Block #2
		array[12] = 0.0F;														//Block #2
		array[13] = 0.0F;														//Block #2
		array[14] = 0.0F;														//Block #2
		array[15] = 0.0F;														//Block #2
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link Rectangle3F} instances in {@code rectangle3Fs} in compiled form.
	 * <p>
	 * If {@code rectangle3Fs} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rectangle3Fs a {@code List} of {@code Rectangle3F} instances
	 * @return a {@code float[]} with all {@code Rectangle3F} instances in {@code rectangle3Fs} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code rectangle3Fs} or at least one of its elements are {@code null}
	 */
	public static float[] toRectangle3Fs(final List<Rectangle3F> rectangle3Fs) {
		return Floats.toArray(rectangle3Fs, rectangle3F -> toRectangle3F(rectangle3F));
	}
	
	/**
	 * Returns a {@code float[]} with {@code rectangularCuboid3F} in compiled form.
	 * <p>
	 * If {@code rectangularCuboid3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rectangularCuboid3F a {@link RectangularCuboid3F} instance
	 * @return a {@code float[]} with {@code rectangularCuboid3F} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code rectangularCuboid3F} is {@code null}
	 */
	public static float[] toRectangularCuboid3F(final RectangularCuboid3F rectangularCuboid3F) {
		final Point3F maximum = rectangularCuboid3F.getMaximum();
		final Point3F minimum = rectangularCuboid3F.getMinimum();
		
		final float[] array = new float[RECTANGULAR_CUBOID_3_F_LENGTH];
		
		array[RECTANGULAR_CUBOID_3_F_OFFSET_MAXIMUM + 0] = maximum.getX();	//Block #1
		array[RECTANGULAR_CUBOID_3_F_OFFSET_MAXIMUM + 1] = maximum.getY();	//Block #1
		array[RECTANGULAR_CUBOID_3_F_OFFSET_MAXIMUM + 2] = maximum.getZ();	//Block #1
		array[RECTANGULAR_CUBOID_3_F_OFFSET_MINIMUM + 0] = minimum.getX();	//Block #1
		array[RECTANGULAR_CUBOID_3_F_OFFSET_MINIMUM + 1] = minimum.getY();	//Block #1
		array[RECTANGULAR_CUBOID_3_F_OFFSET_MINIMUM + 2] = minimum.getZ();	//Block #1
		array[6] = 0.0F;													//Block #1
		array[7] = 0.0F;													//Block #1
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link RectangularCuboid3F} instances in {@code rectangularCuboid3Fs} in compiled form.
	 * <p>
	 * If {@code rectangularCuboid3Fs} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rectangularCuboid3Fs a {@code List} of {@code RectangularCuboid3F} instances
	 * @return a {@code float[]} with all {@code RectangularCuboid3F} instances in {@code rectangularCuboid3Fs} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code rectangularCuboid3Fs} or at least one of its elements are {@code null}
	 */
	public static float[] toRectangularCuboid3Fs(final List<RectangularCuboid3F> rectangularCuboid3Fs) {
		return Floats.toArray(rectangularCuboid3Fs, rectangularCuboid3F -> toRectangularCuboid3F(rectangularCuboid3F));
	}
	
	/**
	 * Returns a {@code float[]} with {@code sphere3F} in compiled form.
	 * <p>
	 * If {@code sphere3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sphere3F a {@link Sphere3F} instance
	 * @return a {@code float[]} with {@code sphere3F} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code sphere3F} is {@code null}
	 */
	public static float[] toSphere3F(final Sphere3F sphere3F) {
		final Point3F center = sphere3F.getCenter();
		
		final float radius = sphere3F.getRadius();
		
		final float[] array = new float[SPHERE_3_F_LENGTH];
		
		array[SPHERE_3_F_OFFSET_CENTER + 0] = center.getX();
		array[SPHERE_3_F_OFFSET_CENTER + 1] = center.getY();
		array[SPHERE_3_F_OFFSET_CENTER + 2] = center.getZ();
		array[SPHERE_3_F_OFFSET_RADIUS] = radius;
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link Sphere3F} instances in {@code sphere3Fs} in compiled form.
	 * <p>
	 * If {@code sphere3Fs} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sphere3Fs a {@code List} of {@code Sphere3F} instances
	 * @return a {@code float[]} with all {@code Sphere3F} instances in {@code sphere3Fs} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code sphere3Fs} or at least one of its elements are {@code null}
	 */
	public static float[] toSphere3Fs(final List<Sphere3F> sphere3Fs) {
		return Floats.toArray(sphere3Fs, sphere3F -> toSphere3F(sphere3F));
	}
	
	/**
	 * Returns a {@code float[]} with {@code torus3F} in compiled form.
	 * <p>
	 * If {@code torus3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param torus3F a {@link Torus3F} instance
	 * @return a {@code float[]} with {@code torus3F} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code torus3F} is {@code null}
	 */
	public static float[] toTorus3F(final Torus3F torus3F) {
		final float radiusInner = torus3F.getRadiusInner();
		final float radiusOuter = torus3F.getRadiusOuter();
		
		final float[] array = new float[TORUS_3_F_LENGTH];
		
		array[TORUS_3_F_OFFSET_RADIUS_INNER] = radiusInner;
		array[TORUS_3_F_OFFSET_RADIUS_OUTER] = radiusOuter;
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link Torus3F} instances in {@code torus3Fs} in compiled form.
	 * <p>
	 * If {@code torus3Fs} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param torus3Fs a {@code List} of {@code Torus3F} instances
	 * @return a {@code float[]} with all {@code Torus3F} instances in {@code torus3Fs} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code torus3Fs} or at least one of its elements are {@code null}
	 */
	public static float[] toTorus3Fs(final List<Torus3F> torus3Fs) {
		return Floats.toArray(torus3Fs, torus3F -> toTorus3F(torus3F));
	}
	
	/**
	 * Returns a {@code float[]} with {@code triangle3F} in compiled form.
	 * <p>
	 * If {@code triangle3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param triangle3F a {@link Triangle3F} instance
	 * @return a {@code float[]} with {@code triangle3F} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code triangle3F} is {@code null}
	 */
	public static float[] toTriangle3F(final Triangle3F triangle3F) {
		final Vertex3F a = triangle3F.getA();
		final Vertex3F b = triangle3F.getB();
		final Vertex3F c = triangle3F.getC();
		
		final float[] array = new float[TRIANGLE_3_F_LENGTH];
		
		array[TRIANGLE_3_F_OFFSET_A_POSITION + 0] = a.getPosition().getX();								//Block #1
		array[TRIANGLE_3_F_OFFSET_A_POSITION + 1] = a.getPosition().getY();								//Block #1
		array[TRIANGLE_3_F_OFFSET_A_POSITION + 2] = a.getPosition().getZ();								//Block #1
		array[TRIANGLE_3_F_OFFSET_B_POSITION + 0] = b.getPosition().getX();								//Block #1
		array[TRIANGLE_3_F_OFFSET_B_POSITION + 1] = b.getPosition().getY();								//Block #1
		array[TRIANGLE_3_F_OFFSET_B_POSITION + 2] = b.getPosition().getZ();								//Block #1
		array[TRIANGLE_3_F_OFFSET_C_POSITION + 0] = c.getPosition().getX();								//Block #1
		array[TRIANGLE_3_F_OFFSET_C_POSITION + 1] = c.getPosition().getY();								//Block #1
		array[TRIANGLE_3_F_OFFSET_C_POSITION + 2] = c.getPosition().getZ();								//Block #2
		
		array[TRIANGLE_3_F_OFFSET_A_TEXTURE_COORDINATES + 0] = a.getTextureCoordinates().getU();		//Block #2
		array[TRIANGLE_3_F_OFFSET_A_TEXTURE_COORDINATES + 1] = a.getTextureCoordinates().getV();		//Block #2
		array[TRIANGLE_3_F_OFFSET_B_TEXTURE_COORDINATES + 0] = b.getTextureCoordinates().getU();		//Block #2
		array[TRIANGLE_3_F_OFFSET_B_TEXTURE_COORDINATES + 1] = b.getTextureCoordinates().getV();		//Block #2
		array[TRIANGLE_3_F_OFFSET_C_TEXTURE_COORDINATES + 0] = c.getTextureCoordinates().getU();		//Block #2
		array[TRIANGLE_3_F_OFFSET_C_TEXTURE_COORDINATES + 1] = c.getTextureCoordinates().getV();		//Block #2
		
		array[TRIANGLE_3_F_OFFSET_A_ORTHONORMAL_BASIS_W + 0] = a.getOrthonormalBasis().getW().getX();	//Block #2
		array[TRIANGLE_3_F_OFFSET_A_ORTHONORMAL_BASIS_W + 1] = a.getOrthonormalBasis().getW().getY();	//Block #3
		array[TRIANGLE_3_F_OFFSET_A_ORTHONORMAL_BASIS_W + 2] = a.getOrthonormalBasis().getW().getZ();	//Block #3
		array[TRIANGLE_3_F_OFFSET_B_ORTHONORMAL_BASIS_W + 0] = b.getOrthonormalBasis().getW().getX();	//Block #3
		array[TRIANGLE_3_F_OFFSET_B_ORTHONORMAL_BASIS_W + 1] = b.getOrthonormalBasis().getW().getY();	//Block #3
		array[TRIANGLE_3_F_OFFSET_B_ORTHONORMAL_BASIS_W + 2] = b.getOrthonormalBasis().getW().getZ();	//Block #3
		array[TRIANGLE_3_F_OFFSET_C_ORTHONORMAL_BASIS_W + 0] = c.getOrthonormalBasis().getW().getX();	//Block #3
		array[TRIANGLE_3_F_OFFSET_C_ORTHONORMAL_BASIS_W + 1] = c.getOrthonormalBasis().getW().getY();	//Block #3
		array[TRIANGLE_3_F_OFFSET_C_ORTHONORMAL_BASIS_W + 2] = c.getOrthonormalBasis().getW().getZ();	//Block #3
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link Triangle3F} instances in {@code triangle3Fs} in compiled form.
	 * <p>
	 * If {@code triangle3Fs} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param triangle3Fs a {@code List} of {@code Triangle3F} instances
	 * @return a {@code float[]} with all {@code Triangle3F} instances in {@code triangle3Fs} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code triangle3Fs} or at least one of its elements are {@code null}
	 */
	public static float[] toTriangle3Fs(final List<Triangle3F> triangle3Fs) {
		return Floats.toArray(triangle3Fs, triangle3F -> toTriangle3F(triangle3F));
	}
	
	/**
	 * Returns the length of {@code triangleMesh3F} in compiled form.
	 * <p>
	 * If {@code triangleMesh3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param triangleMesh3F a {@link TriangleMesh3F} instance
	 * @return the length of {@code triangleMesh3F} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code triangleMesh3F} is {@code null}
	 */
	public static int getTriangleMesh3FLength(final TriangleMesh3F triangleMesh3F) {
		final Optional<BVHNode3F> optionalRootBVHNode = triangleMesh3F.getRootBVHNode();
		
		if(optionalRootBVHNode.isPresent()) {
			return NodeFilter.filterAll(optionalRootBVHNode.get(), BVHNode3F.class).stream().mapToInt(bVHNode -> doGetBVHNode3FLength(bVHNode)).sum();
		}
		
		return 0;
	}
	
	/**
	 * Returns an {@code int[]} with {@code triangleMesh3F} in compiled form.
	 * <p>
	 * If {@code triangleMesh3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledShape3FCache.toTriangleMesh3F(triangleMesh3F, boundingVolume3F -> 0, triangle3F -> 0);
	 * }
	 * </pre>
	 * 
	 * @param triangleMesh3F a {@link TriangleMesh3F} instance
	 * @return an {@code int[]} with {@code triangleMesh3F} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code triangleMesh3F} is {@code null}
	 */
	public static int[] toTriangleMesh3F(final TriangleMesh3F triangleMesh3F) {
		return toTriangleMesh3F(triangleMesh3F, boundingVolume3F -> 0, triangle3F -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with {@code triangleMesh3F} in compiled form.
	 * <p>
	 * If either {@code triangleMesh3F}, {@code boundingVolume3FOffsetFunction} or {@code triangle3FOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param triangleMesh3F a {@link TriangleMesh3F} instance
	 * @param boundingVolume3FOffsetFunction a {@code ToIntFunction} that returns {@link BoundingVolume3F} offsets
	 * @param triangle3FOffsetFunction a {@code ToIntFunction} that returns {@link Triangle3F} offsets
	 * @return an {@code int[]} with {@code triangleMesh3F} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code triangleMesh3F}, {@code boundingVolume3FOffsetFunction} or {@code triangle3FOffsetFunction} are {@code null}
	 */
	public static int[] toTriangleMesh3F(final TriangleMesh3F triangleMesh3F, final ToIntFunction<BoundingVolume3F> boundingVolume3FOffsetFunction, final ToIntFunction<Triangle3F> triangle3FOffsetFunction) {
		final Optional<BVHNode3F> optionalRootBVHNode = triangleMesh3F.getRootBVHNode();
		
		if(optionalRootBVHNode.isPresent()) {
			return doToBVHNode3F(optionalRootBVHNode.get(), boundingVolume3FOffsetFunction, triangle3FOffsetFunction);
		}
		
		return new int[0];
	}
	
	/**
	 * Returns an {@code int[]} with the offsets for all {@link TriangleMesh3F} instances in {@code triangleMesh3Fs} in compiled form.
	 * <p>
	 * If {@code triangleMesh3Fs} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param triangleMesh3Fs a {@code List} of {@code TriangleMesh3F} instances
	 * @return an {@code int[]} with the offsets for all {@code TriangleMesh3F} instances in {@code triangleMesh3Fs} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code triangleMesh3Fs} or at least one of its elements are {@code null}
	 */
	public static int[] toTriangleMesh3FOffsets(final List<TriangleMesh3F> triangleMesh3Fs) {
		ParameterArguments.requireNonNullList(triangleMesh3Fs, "triangleMesh3Fs");
		
		final int[] triangleMesh3FOffsets = new int[triangleMesh3Fs.size()];
		
		for(int i = 0, j = 0; i < triangleMesh3Fs.size(); j += getTriangleMesh3FLength(triangleMesh3Fs.get(i)), i++) {
			triangleMesh3FOffsets[i] = j;
		}
		
		return triangleMesh3FOffsets;
	}
	
	/**
	 * Returns an {@code int[]} with all {@link TriangleMesh3F} instances in {@code triangleMesh3Fs} in compiled form.
	 * <p>
	 * If either {@code triangleMesh3Fs} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledShape3FCache.toTriangleMesh3Fs(triangleMesh3Fs, boundingVolume3F -> 0, triangle3F -> 0);
	 * }
	 * </pre>
	 * 
	 * @param triangleMesh3Fs a {@code List} of {@code TriangleMesh3F} instances
	 * @return an {@code int[]} with all {@code TriangleMesh3F} instances in {@code triangleMesh3Fs} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code triangleMesh3Fs} or at least one of its elements are {@code null}
	 */
	public static int[] toTriangleMesh3Fs(final List<TriangleMesh3F> triangleMesh3Fs) {
		return toTriangleMesh3Fs(triangleMesh3Fs, boundingVolume3F -> 0, triangle3F -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with all {@link TriangleMesh3F} instances in {@code triangleMesh3Fs} in compiled form.
	 * <p>
	 * If either {@code triangleMesh3Fs}, at least one of its elements, {@code boundingVolume3FOffsetFunction} or {@code triangle3FOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param triangleMesh3Fs a {@code List} of {@code TriangleMesh3F} instances
	 * @param boundingVolume3FOffsetFunction a {@code ToIntFunction} that returns {@link BoundingVolume3F} offsets
	 * @param triangle3FOffsetFunction a {@code ToIntFunction} that returns {@link Triangle3F} offsets
	 * @return an {@code int[]} with all {@code TriangleMesh3F} instances in {@code triangleMesh3Fs} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code triangleMesh3Fs}, at least one of its elements, {@code boundingVolume3FOffsetFunction} or {@code triangle3FOffsetFunction} are {@code null}
	 */
	public static int[] toTriangleMesh3Fs(final List<TriangleMesh3F> triangleMesh3Fs, final ToIntFunction<BoundingVolume3F> boundingVolume3FOffsetFunction, final ToIntFunction<Triangle3F> triangle3FOffsetFunction) {
		return IntArrays.convert(triangleMesh3Fs, triangleMesh3F -> toTriangleMesh3F(triangleMesh3F, boundingVolume3FOffsetFunction, triangle3FOffsetFunction));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	
	private static int doGetBVHNode3FLength(final BVHNode3F bVHNode3F) {
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
	
	private static int[] doToBVHNode3F(final BVHNode3F rootBVHNode3F, final ToIntFunction<BoundingVolume3F> boundingVolume3FOffsetFunction, final ToIntFunction<Triangle3F> triangle3FOffsetFunction) {
		Objects.requireNonNull(rootBVHNode3F, "rootBVHNode3F == null");
		
		final List<BVHNode3F> bVHNode3Fs = NodeFilter.filterAll(rootBVHNode3F, BVHNode3F.class);
		
		final int[] offsets = new int[bVHNode3Fs.size()];
		
		for(int i = 0, j = 0; i < offsets.length; j += doGetBVHNode3FLength(bVHNode3Fs.get(i)), i++) {
			offsets[i] = j;
		}
		
		try(final IntArrayOutputStream intArrayOutputStream = new IntArrayOutputStream()) {
			for(int i = 0; i < bVHNode3Fs.size(); i++) {
				final BVHNode3F bVHNode3F = bVHNode3Fs.get(i);
				
				if(bVHNode3F instanceof LeafBVHNode3F) {
					final LeafBVHNode3F<?> leafBVHNode3F = LeafBVHNode3F.class.cast(bVHNode3F);
					
					final int id = LeafBVHNode3F.ID;
					final int boundingVolumeOffset = boundingVolume3FOffsetFunction.applyAsInt(leafBVHNode3F.getBoundingVolume());
					final int nextOffset = doFindNextOffset(bVHNode3Fs, leafBVHNode3F.getDepth(), i + 1, offsets);
					final int shapeCount = leafBVHNode3F.getShapeCount();
					
					intArrayOutputStream.writeInt(id);
					intArrayOutputStream.writeInt(boundingVolumeOffset);
					intArrayOutputStream.writeInt(nextOffset);
					intArrayOutputStream.writeInt(shapeCount);
					
					for(final Shape3F shape3F : leafBVHNode3F.getShapes()) {
						intArrayOutputStream.writeInt(triangle3FOffsetFunction.applyAsInt(Triangle3F.class.cast(shape3F)));
					}
					
					final int padding = padding(4 + shapeCount);
					
					for(int j = 0; j < padding; j++) {
						intArrayOutputStream.writeInt(0);
					}
				} else if(bVHNode3F instanceof TreeBVHNode3F) {
					final TreeBVHNode3F treeBVHNode3F = TreeBVHNode3F.class.cast(bVHNode3F);
					
					final int id = TreeBVHNode3F.ID;
					final int boundingVolumeOffset = boundingVolume3FOffsetFunction.applyAsInt(treeBVHNode3F.getBoundingVolume());
					final int nextOffset = doFindNextOffset(bVHNode3Fs, treeBVHNode3F.getDepth(), i + 1, offsets);
					final int leftOffset = doFindLeftOffset(bVHNode3Fs, treeBVHNode3F.getDepth(), i + 1, offsets);
					
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
}