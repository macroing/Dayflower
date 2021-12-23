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
package org.dayflower.renderer.gpu;

import static org.dayflower.utility.Floats.MAX_VALUE;
import static org.dayflower.utility.Floats.PI;
import static org.dayflower.utility.Floats.PI_DIVIDED_BY_2;
import static org.dayflower.utility.Floats.PI_DIVIDED_BY_4;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.utility.Floats.PI_RECIPROCAL;

import org.dayflower.geometry.boundingvolume.hierarchy.LeafBVHNode3F;
import org.dayflower.geometry.boundingvolume.hierarchy.TreeBVHNode3F;
import org.dayflower.scene.compiler.CompiledBoundingVolume3FCache;
import org.dayflower.scene.compiler.CompiledShape3FCache;

/**
 * An {@code AbstractGeometryKernel} is an abstract extension of the {@code AbstractImageKernel} class that adds additional features.
 * <p>
 * The features added are the following:
 * <ul>
 * <li>Bounding volume methods</li>
 * <li>Intersection methods</li>
 * <li>Orthonormal basis methods</li>
 * <li>Point methods</li>
 * <li>Ray methods</li>
 * <li>Shape methods</li>
 * <li>Vector methods</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractGeometryKernel extends AbstractImageKernel {
	/**
	 * The default maximum parametric distance value.
	 */
	protected static final float DEFAULT_T_MAXIMUM = MAX_VALUE;
	
	/**
	 * The default minimum parametric distance value.
	 */
	protected static final float DEFAULT_T_MINIMUM = 0.001F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U = 0;
	private static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V = 3;
	private static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W = 6;
	private static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U = 9;
	private static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V = 12;
	private static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W = 15;
	private static final int INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX = 18;
	private static final int INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT = 19;
	private static final int INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES = 22;
	private static final int INTERSECTION_ARRAY_SIZE = 24;
	private static final int ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U = 0;
	private static final int ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V = 3;
	private static final int ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W = 6;
	private static final int ORTHONORMAL_BASIS_3_3_F_ARRAY_SIZE = 9;
	private static final int POINT_2_F_ARRAY_OFFSET_COMPONENT_1 = 0;
	private static final int POINT_2_F_ARRAY_OFFSET_COMPONENT_2 = 1;
	private static final int POINT_2_F_ARRAY_SIZE = 2;
	private static final int POINT_3_F_ARRAY_OFFSET_COMPONENT_1 = 0;
	private static final int POINT_3_F_ARRAY_OFFSET_COMPONENT_2 = 1;
	private static final int POINT_3_F_ARRAY_OFFSET_COMPONENT_3 = 2;
	private static final int POINT_3_F_ARRAY_SIZE = 3;
	private static final int RAY_3_F_ARRAY_OFFSET_DIRECTION = 3;
	private static final int RAY_3_F_ARRAY_OFFSET_ORIGIN = 0;
	private static final int RAY_3_F_ARRAY_OFFSET_T_MAXIMUM = 7;
	private static final int RAY_3_F_ARRAY_OFFSET_T_MINIMUM = 6;
	private static final int RAY_3_F_ARRAY_SIZE = 8;
	private static final int VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1 = 0;
	private static final int VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2 = 1;
	private static final int VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3 = 2;
	private static final int VECTOR_3_F_ARRAY_SIZE = 3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * A {@code float[]} that contains axis aligned bounding boxes.
	 */
	protected float[] boundingVolume3FAxisAlignedBoundingBox3FArray;
	
	/**
	 * A {@code float[]} that contains bounding spheres.
	 */
	protected float[] boundingVolume3FBoundingSphere3FArray;
	
	/**
	 * A {@code float[]} that contains the current intersection.
	 */
	protected float[] intersectionLHSArray_$private$24;
	
	/**
	 * A {@code float[]} that contains the current intersection.
	 */
	protected float[] intersectionRHSArray_$private$24;
	
	/**
	 * A {@code float[]} that contains an orthonormal basis that consists of three 3-dimensional vectors.
	 */
	protected float[] orthonormalBasis33FArray_$private$9;
	
	/**
	 * A {@code float[]} that contains a point that consists of two components.
	 */
	protected float[] point2FArray_$private$2;
	
	/**
	 * A {@code float[]} that contains a point that consists of three components.
	 */
	protected float[] point3FArray_$private$3;
	
	/**
	 * A {@code float[]} that contains a ray that consists of a point called origin, a vector called direction, the minimum parametric distance value and the maximum parametric distance value.
	 */
	protected float[] ray3FArray_$private$8;
	
	/**
	 * A {@code float[]} that contains cones.
	 */
	protected float[] shape3FCone3FArray;
	
	/**
	 * A {@code float[]} that contains cylinders.
	 */
	protected float[] shape3FCylinder3FArray;
	
	/**
	 * A {@code float[]} that contains disks.
	 */
	protected float[] shape3FDisk3FArray;
	
	/**
	 * A {@code float[]} that contains hyperboloids.
	 */
	protected float[] shape3FHyperboloid3FArray;
	
	/**
	 * A {@code float[]} that contains paraboloids.
	 */
	protected float[] shape3FParaboloid3FArray;
	
	/**
	 * A {@code float[]} that contains planes.
	 */
	protected float[] shape3FPlane3FArray;
	
	/**
	 * A {@code float[]} that contains polygons.
	 */
	protected float[] shape3FPolygon3FArray;
	
	/**
	 * A {@code float[]} that contains rectangles.
	 */
	protected float[] shape3FRectangle3FArray;
	
	/**
	 * A {@code float[]} that contains rectangular cuboids.
	 */
	protected float[] shape3FRectangularCuboid3FArray;
	
	/**
	 * A {@code float[]} that contains spheres.
	 */
	protected float[] shape3FSphere3FArray;
	
	/**
	 * A {@code float[]} that contains torii.
	 */
	protected float[] shape3FTorus3FArray;
	
	/**
	 * A {@code float[]} that contains triangles.
	 */
	protected float[] shape3FTriangle3FArray;
	
	/**
	 * A {@code float[]} that contains a vector that consists of three components.
	 */
	protected float[] vector3FArray_$private$3;
	
	/**
	 * An {@code int[]} that contains triangle meshes.
	 */
	protected int[] shape3FTriangleMesh3FArray;
	
	/**
	 * An {@code int[]} that contains a triangle offset for a triangle mesh.
	 */
	protected int[] shape3FTriangleMesh3FArrayToShape3FTriangle3FArray_$private$1;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractGeometryKernel} instance.
	 */
	protected AbstractGeometryKernel() {
		this.boundingVolume3FAxisAlignedBoundingBox3FArray = new float[1];
		this.boundingVolume3FBoundingSphere3FArray = new float[1];
		this.intersectionLHSArray_$private$24 = new float[INTERSECTION_ARRAY_SIZE];
		this.intersectionRHSArray_$private$24 = new float[INTERSECTION_ARRAY_SIZE];
		this.orthonormalBasis33FArray_$private$9 = new float[ORTHONORMAL_BASIS_3_3_F_ARRAY_SIZE];
		this.point2FArray_$private$2 = new float[POINT_2_F_ARRAY_SIZE];
		this.point3FArray_$private$3 = new float[POINT_3_F_ARRAY_SIZE];
		this.ray3FArray_$private$8 = new float[RAY_3_F_ARRAY_SIZE];
		this.shape3FCone3FArray = new float[1];
		this.shape3FCylinder3FArray = new float[1];
		this.shape3FDisk3FArray = new float[1];
		this.shape3FHyperboloid3FArray = new float[1];
		this.shape3FParaboloid3FArray = new float[1];
		this.shape3FPlane3FArray = new float[1];
		this.shape3FPolygon3FArray = new float[1];
		this.shape3FRectangle3FArray = new float[1];
		this.shape3FRectangularCuboid3FArray = new float[1];
		this.shape3FSphere3FArray = new float[1];
		this.shape3FTorus3FArray = new float[1];
		this.shape3FTriangle3FArray = new float[1];
		this.vector3FArray_$private$3 = new float[VECTOR_3_F_ARRAY_SIZE];
		this.shape3FTriangleMesh3FArray = new int[1];
		this.shape3FTriangleMesh3FArrayToShape3FTriangle3FArray_$private$1 = new int[1];
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BoundingVolume3F - AxisAlignedBoundingBox3F /////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, a point is contained by a given axis aligned bounding box, {@code false} otherwise.
	 * 
	 * @param boundingVolume3FAxisAlignedBoundingBox3FArrayOffset the offset for the axis aligned bounding box in {@link #boundingVolume3FAxisAlignedBoundingBox3FArray}
	 * @param pointX the X-component of the point
	 * @param pointY the Y-component of the point
	 * @param pointZ the Z-component of the point
	 * @return {@code true} if, and only if, a point is contained by a given axis aligned bounding box, {@code false} otherwise
	 */
	protected final boolean boundingVolume3FAxisAlignedBoundingBox3FContains(final int boundingVolume3FAxisAlignedBoundingBox3FArrayOffset, final float pointX, final float pointY, final float pointZ) {
		final float axisAlignedBoundingBoxMaximumX = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM + 0];
		final float axisAlignedBoundingBoxMaximumY = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM + 1];
		final float axisAlignedBoundingBoxMaximumZ = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM + 2];
		final float axisAlignedBoundingBoxMinimumX = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM + 0];
		final float axisAlignedBoundingBoxMinimumY = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM + 1];
		final float axisAlignedBoundingBoxMinimumZ = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM + 2];
		
		final boolean containsX = pointX >= axisAlignedBoundingBoxMinimumX && pointX <= axisAlignedBoundingBoxMaximumX;
		final boolean containsY = pointY >= axisAlignedBoundingBoxMinimumY && pointY <= axisAlignedBoundingBoxMaximumY;
		final boolean containsZ = pointZ >= axisAlignedBoundingBoxMinimumZ && pointZ <= axisAlignedBoundingBoxMaximumZ;
		
		return containsX && containsY && containsZ;
	}
	
	/**
	 * Returns {@code true} if, and only if, the current ray is contained by or intersects a given axis aligned bounding box, {@code false} otherwise.
	 * 
	 * @param boundingVolume3FAxisAlignedBoundingBox3FArrayOffset the offset for the axis aligned bounding box in {@link #boundingVolume3FAxisAlignedBoundingBox3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray is contained by or intersects a given axis aligned bounding box, {@code false} otherwise
	 */
	protected final boolean boundingVolume3FAxisAlignedBoundingBox3FContainsOrIntersects(final int boundingVolume3FAxisAlignedBoundingBox3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionReciprocalX = ray3FGetDirectionReciprocalComponent1();
		final float rayDirectionReciprocalY = ray3FGetDirectionReciprocalComponent2();
		final float rayDirectionReciprocalZ = ray3FGetDirectionReciprocalComponent3();
		
//		Retrieve the axis aligned bounding box variables:
		final float axisAlignedBoundingBoxMaximumX = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM + 0];
		final float axisAlignedBoundingBoxMaximumY = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM + 1];
		final float axisAlignedBoundingBoxMaximumZ = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM + 2];
		final float axisAlignedBoundingBoxMinimumX = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM + 0];
		final float axisAlignedBoundingBoxMinimumY = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM + 1];
		final float axisAlignedBoundingBoxMinimumZ = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM + 2];
		
		final boolean containsX = rayOriginX >= axisAlignedBoundingBoxMinimumX && rayOriginX <= axisAlignedBoundingBoxMaximumX;
		final boolean containsY = rayOriginY >= axisAlignedBoundingBoxMinimumY && rayOriginY <= axisAlignedBoundingBoxMaximumY;
		final boolean containsZ = rayOriginZ >= axisAlignedBoundingBoxMinimumZ && rayOriginZ <= axisAlignedBoundingBoxMaximumZ;
		
		if(containsX && containsY && containsZ) {
			return true;
		}
		
//		Compute the intersection:
		final float intersectionTMinimumX = (axisAlignedBoundingBoxMinimumX - rayOriginX) * rayDirectionReciprocalX;
		final float intersectionTMinimumY = (axisAlignedBoundingBoxMinimumY - rayOriginY) * rayDirectionReciprocalY;
		final float intersectionTMinimumZ = (axisAlignedBoundingBoxMinimumZ - rayOriginZ) * rayDirectionReciprocalZ;
		final float intersectionTMaximumX = (axisAlignedBoundingBoxMaximumX - rayOriginX) * rayDirectionReciprocalX;
		final float intersectionTMaximumY = (axisAlignedBoundingBoxMaximumY - rayOriginY) * rayDirectionReciprocalY;
		final float intersectionTMaximumZ = (axisAlignedBoundingBoxMaximumZ - rayOriginZ) * rayDirectionReciprocalZ;
		final float intersectionTMinimum = max(min(intersectionTMinimumX, intersectionTMaximumX), min(intersectionTMinimumY, intersectionTMaximumY), min(intersectionTMinimumZ, intersectionTMaximumZ));
		final float intersectionTMaximum = min(max(intersectionTMinimumX, intersectionTMaximumX), max(intersectionTMinimumY, intersectionTMaximumY), max(intersectionTMinimumZ, intersectionTMaximumZ));
		
		if(intersectionTMinimum > intersectionTMaximum) {
			return false;
		}
		
		if(intersectionTMinimum > rayTMinimum && intersectionTMinimum < rayTMaximum || intersectionTMaximum > rayTMinimum && intersectionTMaximum < rayTMaximum) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given axis aligned bounding box, {@code false} otherwise.
	 * 
	 * @param boundingVolume3FAxisAlignedBoundingBox3FArrayOffset the offset for the axis aligned bounding box in {@link #boundingVolume3FAxisAlignedBoundingBox3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given axis aligned bounding box, {@code false} otherwise
	 */
	protected final boolean boundingVolume3FAxisAlignedBoundingBox3FIntersects(final int boundingVolume3FAxisAlignedBoundingBox3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return boundingVolume3FAxisAlignedBoundingBox3FIntersectionT(boundingVolume3FAxisAlignedBoundingBox3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns the parametric T value for a given axis aligned bounding box, or {@code 0.0F} if no intersection was found.
	 * 
	 * @param boundingVolume3FAxisAlignedBoundingBox3FArrayOffset the offset for the axis aligned bounding box in {@link #boundingVolume3FAxisAlignedBoundingBox3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return the parametric T value for a given axis aligned bounding box, or {@code 0.0F} if no intersection was found
	 */
	protected final float boundingVolume3FAxisAlignedBoundingBox3FIntersectionT(final int boundingVolume3FAxisAlignedBoundingBox3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionReciprocalX = ray3FGetDirectionReciprocalComponent1();
		final float rayDirectionReciprocalY = ray3FGetDirectionReciprocalComponent2();
		final float rayDirectionReciprocalZ = ray3FGetDirectionReciprocalComponent3();
		
//		Retrieve the axis aligned bounding box variables:
		final float axisAlignedBoundingBoxMaximumX = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM + 0];
		final float axisAlignedBoundingBoxMaximumY = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM + 1];
		final float axisAlignedBoundingBoxMaximumZ = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM + 2];
		final float axisAlignedBoundingBoxMinimumX = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM + 0];
		final float axisAlignedBoundingBoxMinimumY = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM + 1];
		final float axisAlignedBoundingBoxMinimumZ = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM + 2];
		
//		Compute the intersection:
		final float intersectionTMinimumX = (axisAlignedBoundingBoxMinimumX - rayOriginX) * rayDirectionReciprocalX;
		final float intersectionTMinimumY = (axisAlignedBoundingBoxMinimumY - rayOriginY) * rayDirectionReciprocalY;
		final float intersectionTMinimumZ = (axisAlignedBoundingBoxMinimumZ - rayOriginZ) * rayDirectionReciprocalZ;
		final float intersectionTMaximumX = (axisAlignedBoundingBoxMaximumX - rayOriginX) * rayDirectionReciprocalX;
		final float intersectionTMaximumY = (axisAlignedBoundingBoxMaximumY - rayOriginY) * rayDirectionReciprocalY;
		final float intersectionTMaximumZ = (axisAlignedBoundingBoxMaximumZ - rayOriginZ) * rayDirectionReciprocalZ;
		final float intersectionTMinimum = max(min(intersectionTMinimumX, intersectionTMaximumX), min(intersectionTMinimumY, intersectionTMaximumY), min(intersectionTMinimumZ, intersectionTMaximumZ));
		final float intersectionTMaximum = min(max(intersectionTMinimumX, intersectionTMaximumX), max(intersectionTMinimumY, intersectionTMaximumY), max(intersectionTMinimumZ, intersectionTMaximumZ));
		
		if(intersectionTMinimum > intersectionTMaximum) {
			return 0.0F;
		}
		
		if(intersectionTMinimum > rayTMinimum && intersectionTMinimum < rayTMaximum) {
			return intersectionTMinimum;
		}
		
		if(intersectionTMaximum > rayTMinimum && intersectionTMaximum < rayTMaximum) {
			return intersectionTMaximum;
		}
		
		return 0.0F;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BoundingVolume3F - BoundingSphere3F /////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, a point is contained by a given bounding sphere, {@code false} otherwise.
	 * 
	 * @param boundingVolume3FBoundingSphere3FArrayOffset the offset for the bounding sphere in {@link #boundingVolume3FBoundingSphere3FArray}
	 * @param pointX the X-component of the point
	 * @param pointY the Y-component of the point
	 * @param pointZ the Z-component of the point
	 * @return {@code true} if, and only if, a point is contained by a given bounding sphere, {@code false} otherwise
	 */
	protected final boolean boundingVolume3FBoundingSphere3FContains(final int boundingVolume3FBoundingSphere3FArrayOffset, final float pointX, final float pointY, final float pointZ) {
		final float boundingSphereCenterX = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_CENTER + 0];
		final float boundingSphereCenterY = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_CENTER + 1];
		final float boundingSphereCenterZ = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_CENTER + 2];
		final float boundingSphereRadius = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_RADIUS];
		
		final float distanceSquared = point3FDistanceSquared(boundingSphereCenterX, boundingSphereCenterY, boundingSphereCenterZ, pointX, pointY, pointZ);
		
		return distanceSquared < boundingSphereRadius * boundingSphereRadius;
	}
	
	/**
	 * Returns {@code true} if, and only if, the current ray is contained by or intersects a given bounding sphere, {@code false} otherwise.
	 * 
	 * @param boundingVolume3FBoundingSphere3FArrayOffset the offset for the bounding sphere in {@link #boundingVolume3FBoundingSphere3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray is contained by or intersects a given bounding sphere, {@code false} otherwise
	 */
	protected final boolean boundingVolume3FBoundingSphere3FContainsOrIntersects(final int boundingVolume3FBoundingSphere3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the bounding sphere variables:
		final float boundingSphereCenterX = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_CENTER + 0];
		final float boundingSphereCenterY = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_CENTER + 1];
		final float boundingSphereCenterZ = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_CENTER + 2];
		final float boundingSphereRadius = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_RADIUS];
		final float boundingSphereRadiusSquared = boundingSphereRadius * boundingSphereRadius;
		
		final float distanceSquared = point3FDistanceSquared(boundingSphereCenterX, boundingSphereCenterY, boundingSphereCenterZ, rayOriginX, rayOriginY, rayOriginZ);
		
		if(distanceSquared < boundingSphereRadiusSquared) {
			return true;
		}
		
//		Compute the direction from the bounding sphere center to the ray origin:
		final float boundingSphereCenterToRayOriginX = rayOriginX - boundingSphereCenterX;
		final float boundingSphereCenterToRayOriginY = rayOriginY - boundingSphereCenterY;
		final float boundingSphereCenterToRayOriginZ = rayOriginZ - boundingSphereCenterZ;
		
//		Compute the variables for the quadratic system:
		final float a = rayDirectionX * rayDirectionX + rayDirectionY * rayDirectionY + rayDirectionZ * rayDirectionZ;
		final float b = 2.0F * (boundingSphereCenterToRayOriginX * rayDirectionX + boundingSphereCenterToRayOriginY * rayDirectionY + boundingSphereCenterToRayOriginZ * rayDirectionZ);
		final float c = (boundingSphereCenterToRayOriginX * boundingSphereCenterToRayOriginX + boundingSphereCenterToRayOriginY * boundingSphereCenterToRayOriginY + boundingSphereCenterToRayOriginZ * boundingSphereCenterToRayOriginZ) - boundingSphereRadiusSquared;
		
//		Compute the intersection by solving the quadratic system and checking the valid intersection interval:
		final float t = solveQuadraticSystem(a, b, c, rayTMinimum, rayTMaximum);
		
		return t > 0.0F;
	}
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given bounding sphere, {@code false} otherwise.
	 * 
	 * @param boundingVolume3FBoundingSphere3FArrayOffset the offset for the bounding sphere in {@link #boundingVolume3FBoundingSphere3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given bounding sphere, {@code false} otherwise
	 */
	protected final boolean boundingVolume3FBoundingSphere3FIntersects(final int boundingVolume3FBoundingSphere3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return boundingVolume3FBoundingSphere3FIntersectionT(boundingVolume3FBoundingSphere3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns the parametric T value for a given bounding sphere, or {@code 0.0F} if no intersection was found.
	 * 
	 * @param boundingVolume3FBoundingSphere3FArrayOffset the offset for the bounding sphere in {@link #boundingVolume3FBoundingSphere3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return the parametric T value for a given bounding sphere, or {@code 0.0F} if no intersection was found
	 */
	protected final float boundingVolume3FBoundingSphere3FIntersectionT(final int boundingVolume3FBoundingSphere3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the bounding sphere variables:
		final float boundingSphereCenterX = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_CENTER + 0];
		final float boundingSphereCenterY = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_CENTER + 1];
		final float boundingSphereCenterZ = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_CENTER + 2];
		final float boundingSphereRadius = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_RADIUS];
		final float boundingSphereRadiusSquared = boundingSphereRadius * boundingSphereRadius;
		
//		Compute the direction from the bounding sphere center to the ray origin:
		final float boundingSphereCenterToRayOriginX = rayOriginX - boundingSphereCenterX;
		final float boundingSphereCenterToRayOriginY = rayOriginY - boundingSphereCenterY;
		final float boundingSphereCenterToRayOriginZ = rayOriginZ - boundingSphereCenterZ;
		
//		Compute the variables for the quadratic system:
		final float a = rayDirectionX * rayDirectionX + rayDirectionY * rayDirectionY + rayDirectionZ * rayDirectionZ;
		final float b = 2.0F * (boundingSphereCenterToRayOriginX * rayDirectionX + boundingSphereCenterToRayOriginY * rayDirectionY + boundingSphereCenterToRayOriginZ * rayDirectionZ);
		final float c = (boundingSphereCenterToRayOriginX * boundingSphereCenterToRayOriginX + boundingSphereCenterToRayOriginY * boundingSphereCenterToRayOriginY + boundingSphereCenterToRayOriginZ * boundingSphereCenterToRayOriginZ) - boundingSphereRadiusSquared;
		
//		Compute the intersection by solving the quadratic system and checking the valid intersection interval:
		final float t = solveQuadraticSystem(a, b, c, rayTMinimum, rayTMaximum);
		
		return t;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Intersection - LHS //////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the value of component 1 of the U-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the U-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGUComponent1() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0];
	}
	
	/**
	 * Returns the value of component 2 of the U-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the U-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGUComponent2() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1];
	}
	
	/**
	 * Returns the value of component 3 of the U-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the U-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGUComponent3() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2];
	}
	
	/**
	 * Returns the value of component 1 of the V-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the V-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGVComponent1() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0];
	}
	
	/**
	 * Returns the value of component 2 of the V-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the V-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGVComponent2() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1];
	}
	
	/**
	 * Returns the value of component 3 of the V-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the V-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGVComponent3() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2];
	}
	
	/**
	 * Returns the value of component 1 of the W-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the W-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGWComponent1() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
	}
	
	/**
	 * Returns the value of component 2 of the W-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the W-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGWComponent2() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
	}
	
	/**
	 * Returns the value of component 3 of the W-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the W-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGWComponent3() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
	}
	
	/**
	 * Returns the value of component 1 of the U-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the U-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSUComponent1() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0];
	}
	
	/**
	 * Returns the value of component 2 of the U-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the U-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSUComponent2() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1];
	}
	
	/**
	 * Returns the value of component 3 of the U-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the U-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSUComponent3() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2];
	}
	
	/**
	 * Returns the value of component 1 of the V-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the V-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSVComponent1() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0];
	}
	
	/**
	 * Returns the value of component 2 of the V-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the V-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSVComponent2() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1];
	}
	
	/**
	 * Returns the value of component 3 of the V-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the V-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSVComponent3() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2];
	}
	
	/**
	 * Returns the value of component 1 of the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSWComponent1() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
	}
	
	/**
	 * Returns the value of component 2 of the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSWComponent2() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
	}
	
	/**
	 * Returns the value of component 3 of the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSWComponent3() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
	}
	
	/**
	 * Returns the value of component 1 of the surface intersection point in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the surface intersection point in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetSurfaceIntersectionPointComponent1() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0];
	}
	
	/**
	 * Returns the value of component 2 of the surface intersection point in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the surface intersection point in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetSurfaceIntersectionPointComponent2() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1];
	}
	
	/**
	 * Returns the value of component 3 of the surface intersection point in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the surface intersection point in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetSurfaceIntersectionPointComponent3() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2];
	}
	
	/**
	 * Returns the value of component 1 of the texture coordinates in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the texture coordinates in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetTextureCoordinatesComponent1() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0];
	}
	
	/**
	 * Returns the value of component 2 of the texture coordinates in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the texture coordinates in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetTextureCoordinatesComponent2() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1];
	}
	
	/**
	 * Returns the primitive index in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the primitive index in {@link #intersectionLHSArray_$private$24}
	 */
	protected final int intersectionLHSGetPrimitiveIndex() {
		return (int)(this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX]);
	}
	
	/**
	 * Sets the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @param uComponent1 component 1 of the U-direction
	 * @param uComponent2 component 2 of the U-direction
	 * @param uComponent3 component 3 of the U-direction
	 * @param vComponent1 component 1 of the V-direction
	 * @param vComponent2 component 2 of the V-direction
	 * @param vComponent3 component 3 of the V-direction
	 * @param wComponent1 component 1 of the W-direction
	 * @param wComponent2 component 2 of the W-direction
	 * @param wComponent3 component 3 of the W-direction
	 */
	protected final void intersectionLHSSetOrthonormalBasisG(final float uComponent1, final float uComponent2, final float uComponent3, final float vComponent1, final float vComponent2, final float vComponent3, final float wComponent1, final float wComponent2, final float wComponent3) {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0] = uComponent1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1] = uComponent2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2] = uComponent3;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0] = vComponent1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1] = vComponent2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2] = vComponent3;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0] = wComponent1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1] = wComponent2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2] = wComponent3;
	}
	
	/**
	 * Sets the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24} from {@link #orthonormalBasis33FArray_$private$9}.
	 */
	protected final void intersectionLHSSetOrthonormalBasisGFromOrthonormalBasis33F() {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0] = orthonormalBasis33FGetUComponent1();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1] = orthonormalBasis33FGetUComponent2();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2] = orthonormalBasis33FGetUComponent3();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0] = orthonormalBasis33FGetVComponent1();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1] = orthonormalBasis33FGetVComponent2();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2] = orthonormalBasis33FGetVComponent3();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0] = orthonormalBasis33FGetWComponent1();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1] = orthonormalBasis33FGetWComponent2();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2] = orthonormalBasis33FGetWComponent3();
	}
	
	/**
	 * Sets the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @param uComponent1 component 1 of the U-direction
	 * @param uComponent2 component 2 of the U-direction
	 * @param uComponent3 component 3 of the U-direction
	 * @param vComponent1 component 1 of the V-direction
	 * @param vComponent2 component 2 of the V-direction
	 * @param vComponent3 component 3 of the V-direction
	 * @param wComponent1 component 1 of the W-direction
	 * @param wComponent2 component 2 of the W-direction
	 * @param wComponent3 component 3 of the W-direction
	 */
	protected final void intersectionLHSSetOrthonormalBasisS(final float uComponent1, final float uComponent2, final float uComponent3, final float vComponent1, final float vComponent2, final float vComponent3, final float wComponent1, final float wComponent2, final float wComponent3) {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = uComponent1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = uComponent2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = uComponent3;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = vComponent1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = vComponent2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = vComponent3;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = wComponent1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = wComponent2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = wComponent3;
	}
	
	/**
	 * Sets the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24} from {@link #orthonormalBasis33FArray_$private$9}.
	 */
	protected final void intersectionLHSSetOrthonormalBasisSFromOrthonormalBasis33F() {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = orthonormalBasis33FGetUComponent1();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = orthonormalBasis33FGetUComponent2();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = orthonormalBasis33FGetUComponent3();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = orthonormalBasis33FGetVComponent1();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = orthonormalBasis33FGetVComponent2();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = orthonormalBasis33FGetVComponent3();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = orthonormalBasis33FGetWComponent1();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = orthonormalBasis33FGetWComponent2();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = orthonormalBasis33FGetWComponent3();
	}
	
	/**
	 * Sets the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @param component1 component 1 of the W-direction
	 * @param component2 component 2 of the W-direction
	 * @param component3 component 3 of the W-direction
	 */
	protected final void intersectionLHSSetOrthonormalBasisSW(final float component1, final float component2, final float component3) {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = component1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = component2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = component3;
	}
	
	/**
	 * Sets the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24} to a transformed representation.
	 * 
	 * @param component1 component 1 of the vector to transform with
	 * @param component2 component 2 of the vector to transform with
	 * @param component3 component 3 of the vector to transform with
	 */
	protected final void intersectionLHSSetOrthonormalBasisSWTransform(final float component1, final float component2, final float component3) {
		final float oldUComponent1 = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0];
		final float oldUComponent2 = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1];
		final float oldUComponent3 = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2];
		final float oldVComponent1 = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0];
		final float oldVComponent2 = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1];
		final float oldVComponent3 = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2];
		final float oldWComponent1 = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
		final float oldWComponent2 = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
		final float oldWComponent3 = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
		
		final float newWComponent1 = component1 * oldUComponent1 + component2 * oldVComponent1 + component3 * oldWComponent1;
		final float newWComponent2 = component1 * oldUComponent2 + component2 * oldVComponent2 + component3 * oldWComponent2;
		final float newWComponent3 = component1 * oldUComponent3 + component2 * oldVComponent3 + component3 * oldWComponent3;
		final float newWLengthReciprocal = vector3FLengthReciprocal(newWComponent1, newWComponent2, newWComponent3);
		final float newWNormalizedComponent1 = newWComponent1 * newWLengthReciprocal;
		final float newWNormalizedComponent2 = newWComponent2 * newWLengthReciprocal;
		final float newWNormalizedComponent3 = newWComponent3 * newWLengthReciprocal;
		
		final float newUComponent1 = oldVComponent2 * newWNormalizedComponent3 - oldVComponent3 * newWNormalizedComponent2;
		final float newUComponent2 = oldVComponent3 * newWNormalizedComponent1 - oldVComponent1 * newWNormalizedComponent3;
		final float newUComponent3 = oldVComponent1 * newWNormalizedComponent2 - oldVComponent2 * newWNormalizedComponent1;
		final float newULengthReciprocal = vector3FLengthReciprocal(newUComponent1, newUComponent2, newUComponent3);
		final float newUNormalizedComponent1 = newUComponent1 * newULengthReciprocal;
		final float newUNormalizedComponent2 = newUComponent2 * newULengthReciprocal;
		final float newUNormalizedComponent3 = newUComponent3 * newULengthReciprocal;
		
		final float newVNormalizedComponent1 = newWNormalizedComponent2 * newUNormalizedComponent3 - newWNormalizedComponent3 * newUNormalizedComponent2;
		final float newVNormalizedComponent2 = newWNormalizedComponent3 * newUNormalizedComponent1 - newWNormalizedComponent1 * newUNormalizedComponent3;
		final float newVNormalizedComponent3 = newWNormalizedComponent1 * newUNormalizedComponent2 - newWNormalizedComponent2 * newUNormalizedComponent1;
		
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = newUNormalizedComponent1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = newUNormalizedComponent2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = newUNormalizedComponent3;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = newVNormalizedComponent1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = newVNormalizedComponent2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = newVNormalizedComponent3;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = newWNormalizedComponent1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = newWNormalizedComponent2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = newWNormalizedComponent3;
	}
	
	/**
	 * Sets the primitive index in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @param primitiveIndex the primitive index
	 */
	protected final void intersectionLHSSetPrimitiveIndex(final int primitiveIndex) {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX] = primitiveIndex;
	}
	
	/**
	 * Sets the surface intersection point in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @param component1 component 1 of the surface intersection point
	 * @param component2 component 2 of the surface intersection point
	 * @param component3 component 3 of the surface intersection point
	 */
	protected final void intersectionLHSSetSurfaceIntersectionPoint(final float component1, final float component2, final float component3) {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0] = component1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1] = component2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2] = component3;
	}
	
	/**
	 * Sets the texture coordinates in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @param component1 component 1 of the texture coordinates
	 * @param component2 component 2 of the texture coordinates
	 */
	protected final void intersectionLHSSetTextureCoordinates(final float component1, final float component2) {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0] = component1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1] = component2;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Intersection - RHS //////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the value of component 1 of the U-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the U-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGUComponent1() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0];
	}
	
	/**
	 * Returns the value of component 2 of the U-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the U-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGUComponent2() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1];
	}
	
	/**
	 * Returns the value of component 3 of the U-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the U-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGUComponent3() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2];
	}
	
	/**
	 * Returns the value of component 1 of the V-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the V-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGVComponent1() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0];
	}
	
	/**
	 * Returns the value of component 2 of the V-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the V-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGVComponent2() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1];
	}
	
	/**
	 * Returns the value of component 3 of the V-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the V-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGVComponent3() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2];
	}
	
	/**
	 * Returns the value of component 1 of the W-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the W-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGWComponent1() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
	}
	
	/**
	 * Returns the value of component 2 of the W-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the W-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGWComponent2() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
	}
	
	/**
	 * Returns the value of component 3 of the W-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the W-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGWComponent3() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
	}
	
	/**
	 * Returns the value of component 1 of the U-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the U-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSUComponent1() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0];
	}
	
	/**
	 * Returns the value of component 2 of the U-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the U-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSUComponent2() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1];
	}
	
	/**
	 * Returns the value of component 3 of the U-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the U-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSUComponent3() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2];
	}
	
	/**
	 * Returns the value of component 1 of the V-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the V-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSVComponent1() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0];
	}
	
	/**
	 * Returns the value of component 2 of the V-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the V-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSVComponent2() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1];
	}
	
	/**
	 * Returns the value of component 3 of the V-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the V-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSVComponent3() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2];
	}
	
	/**
	 * Returns the value of component 1 of the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSWComponent1() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
	}
	
	/**
	 * Returns the value of component 2 of the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSWComponent2() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
	}
	
	/**
	 * Returns the value of component 3 of the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSWComponent3() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
	}
	
	/**
	 * Returns the value of component 1 of the surface intersection point in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the surface intersection point in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetSurfaceIntersectionPointComponent1() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0];
	}
	
	/**
	 * Returns the value of component 2 of the surface intersection point in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the surface intersection point in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetSurfaceIntersectionPointComponent2() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1];
	}
	
	/**
	 * Returns the value of component 3 of the surface intersection point in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the surface intersection point in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetSurfaceIntersectionPointComponent3() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2];
	}
	
	/**
	 * Returns the value of component 1 of the texture coordinates in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the texture coordinates in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetTextureCoordinatesComponent1() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0];
	}
	
	/**
	 * Returns the value of component 2 of the texture coordinates in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the texture coordinates in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetTextureCoordinatesComponent2() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1];
	}
	
	/**
	 * Returns the primitive index in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the primitive index in {@link #intersectionRHSArray_$private$24}
	 */
	protected final int intersectionRHSGetPrimitiveIndex() {
		return (int)(this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX]);
	}
	
	/**
	 * Sets the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @param uComponent1 component 1 of the U-direction
	 * @param uComponent2 component 2 of the U-direction
	 * @param uComponent3 component 3 of the U-direction
	 * @param vComponent1 component 1 of the V-direction
	 * @param vComponent2 component 2 of the V-direction
	 * @param vComponent3 component 3 of the V-direction
	 * @param wComponent1 component 1 of the W-direction
	 * @param wComponent2 component 2 of the W-direction
	 * @param wComponent3 component 3 of the W-direction
	 */
	protected final void intersectionRHSSetOrthonormalBasisG(final float uComponent1, final float uComponent2, final float uComponent3, final float vComponent1, final float vComponent2, final float vComponent3, final float wComponent1, final float wComponent2, final float wComponent3) {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0] = uComponent1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1] = uComponent2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2] = uComponent3;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0] = vComponent1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1] = vComponent2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2] = vComponent3;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0] = wComponent1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1] = wComponent2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2] = wComponent3;
	}
	
	/**
	 * Sets the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24} from {@link #orthonormalBasis33FArray_$private$9}.
	 */
	protected final void intersectionRHSSetOrthonormalBasisGFromOrthonormalBasis33F() {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0] = orthonormalBasis33FGetUComponent1();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1] = orthonormalBasis33FGetUComponent2();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2] = orthonormalBasis33FGetUComponent3();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0] = orthonormalBasis33FGetVComponent1();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1] = orthonormalBasis33FGetVComponent2();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2] = orthonormalBasis33FGetVComponent3();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0] = orthonormalBasis33FGetWComponent1();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1] = orthonormalBasis33FGetWComponent2();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2] = orthonormalBasis33FGetWComponent3();
	}
	
	/**
	 * Sets the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @param uComponent1 component 1 of the U-direction
	 * @param uComponent2 component 2 of the U-direction
	 * @param uComponent3 component 3 of the U-direction
	 * @param vComponent1 component 1 of the V-direction
	 * @param vComponent2 component 2 of the V-direction
	 * @param vComponent3 component 3 of the V-direction
	 * @param wComponent1 component 1 of the W-direction
	 * @param wComponent2 component 2 of the W-direction
	 * @param wComponent3 component 3 of the W-direction
	 */
	protected final void intersectionRHSSetOrthonormalBasisS(final float uComponent1, final float uComponent2, final float uComponent3, final float vComponent1, final float vComponent2, final float vComponent3, final float wComponent1, final float wComponent2, final float wComponent3) {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = uComponent1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = uComponent2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = uComponent3;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = vComponent1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = vComponent2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = vComponent3;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = wComponent1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = wComponent2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = wComponent3;
	}
	
	/**
	 * Sets the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24} from {@link #orthonormalBasis33FArray_$private$9}.
	 */
	protected final void intersectionRHSSetOrthonormalBasisSFromOrthonormalBasis33F() {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = orthonormalBasis33FGetUComponent1();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = orthonormalBasis33FGetUComponent2();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = orthonormalBasis33FGetUComponent3();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = orthonormalBasis33FGetVComponent1();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = orthonormalBasis33FGetVComponent2();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = orthonormalBasis33FGetVComponent3();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = orthonormalBasis33FGetWComponent1();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = orthonormalBasis33FGetWComponent2();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = orthonormalBasis33FGetWComponent3();
	}
	
	/**
	 * Sets the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @param component1 component 1 of the W-direction
	 * @param component2 component 2 of the W-direction
	 * @param component3 component 3 of the W-direction
	 */
	protected final void intersectionRHSSetOrthonormalBasisSW(final float component1, final float component2, final float component3) {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = component1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = component2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = component3;
	}
	
	/**
	 * Sets the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24} to a transformed representation.
	 * 
	 * @param component1 component 1 of the vector to transform with
	 * @param component2 component 2 of the vector to transform with
	 * @param component3 component 3 of the vector to transform with
	 */
	protected final void intersectionRHSSetOrthonormalBasisSWTransform(final float component1, final float component2, final float component3) {
		final float oldUComponent1 = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0];
		final float oldUComponent2 = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1];
		final float oldUComponent3 = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2];
		final float oldVComponent1 = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0];
		final float oldVComponent2 = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1];
		final float oldVComponent3 = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2];
		final float oldWComponent1 = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
		final float oldWComponent2 = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
		final float oldWComponent3 = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
		
		final float newWComponent1 = component1 * oldUComponent1 + component2 * oldVComponent1 + component3 * oldWComponent1;
		final float newWComponent2 = component1 * oldUComponent2 + component2 * oldVComponent2 + component3 * oldWComponent2;
		final float newWComponent3 = component1 * oldUComponent3 + component2 * oldVComponent3 + component3 * oldWComponent3;
		final float newWLengthReciprocal = vector3FLengthReciprocal(newWComponent1, newWComponent2, newWComponent3);
		final float newWNormalizedComponent1 = newWComponent1 * newWLengthReciprocal;
		final float newWNormalizedComponent2 = newWComponent2 * newWLengthReciprocal;
		final float newWNormalizedComponent3 = newWComponent3 * newWLengthReciprocal;
		
		final float newUComponent1 = oldVComponent2 * newWNormalizedComponent3 - oldVComponent3 * newWNormalizedComponent2;
		final float newUComponent2 = oldVComponent3 * newWNormalizedComponent1 - oldVComponent1 * newWNormalizedComponent3;
		final float newUComponent3 = oldVComponent1 * newWNormalizedComponent2 - oldVComponent2 * newWNormalizedComponent1;
		final float newULengthReciprocal = vector3FLengthReciprocal(newUComponent1, newUComponent2, newUComponent3);
		final float newUNormalizedComponent1 = newUComponent1 * newULengthReciprocal;
		final float newUNormalizedComponent2 = newUComponent2 * newULengthReciprocal;
		final float newUNormalizedComponent3 = newUComponent3 * newULengthReciprocal;
		
		final float newVNormalizedComponent1 = newWNormalizedComponent2 * newUNormalizedComponent3 - newWNormalizedComponent3 * newUNormalizedComponent2;
		final float newVNormalizedComponent2 = newWNormalizedComponent3 * newUNormalizedComponent1 - newWNormalizedComponent1 * newUNormalizedComponent3;
		final float newVNormalizedComponent3 = newWNormalizedComponent1 * newUNormalizedComponent2 - newWNormalizedComponent2 * newUNormalizedComponent1;
		
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = newUNormalizedComponent1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = newUNormalizedComponent2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = newUNormalizedComponent3;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = newVNormalizedComponent1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = newVNormalizedComponent2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = newVNormalizedComponent3;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = newWNormalizedComponent1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = newWNormalizedComponent2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = newWNormalizedComponent3;
	}
	
	/**
	 * Sets the primitive index in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @param primitiveIndex the primitive index
	 */
	protected final void intersectionRHSSetPrimitiveIndex(final int primitiveIndex) {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX] = primitiveIndex;
	}
	
	/**
	 * Sets the surface intersection point in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @param component1 component 1 of the surface intersection point
	 * @param component2 component 2 of the surface intersection point
	 * @param component3 component 3 of the surface intersection point
	 */
	protected final void intersectionRHSSetSurfaceIntersectionPoint(final float component1, final float component2, final float component3) {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0] = component1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1] = component2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2] = component3;
	}
	
	/**
	 * Sets the texture coordinates in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @param component1 component 1 of the texture coordinates
	 * @param component2 component 2 of the texture coordinates
	 */
	protected final void intersectionRHSSetTextureCoordinates(final float component1, final float component2) {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0] = component1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1] = component2;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// OrthonormalBasis33F /////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the value of component 1 of the U-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of component 1 of the U-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetUComponent1() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0];
	}
	
	/**
	 * Returns the value of component 2 of the U-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of component 2 of the U-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetUComponent2() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1];
	}
	
	/**
	 * Returns the value of component 3 of the U-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of component 3 of the U-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetUComponent3() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2];
	}
	
	/**
	 * Returns the value of component 1 of the V-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of component 1 of the V-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetVComponent1() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0];
	}
	
	/**
	 * Returns the value of component 2 of the V-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of component 2 of the V-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetVComponent2() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1];
	}
	
	/**
	 * Returns the value of component 3 of the V-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of component 3 of the V-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetVComponent3() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2];
	}
	
	/**
	 * Returns the value of component 1 of the W-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of component 1 of the W-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetWComponent1() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0];
	}
	
	/**
	 * Returns the value of component 2 of the W-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of component 2 of the W-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetWComponent2() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1];
	}
	
	/**
	 * Returns the value of component 3 of the W-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of component 3 of the W-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetWComponent3() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2];
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the U-direction vector represented by {@code orthonormalBasisUX}, {@code orthonormalBasisUY} and {@code orthonormalBasisUZ} and the V-direction vector represented by {@code orthonormalBasisVX},
	 * {@code orthonormalBasisVY} and {@code orthonormalBasisVZ}.
	 * <p>
	 * This method will normalize the U-direction and V-direction vectors.
	 * 
	 * @param orthonormalBasisUX the X-component of the U-direction
	 * @param orthonormalBasisUY the Y-component of the U-direction
	 * @param orthonormalBasisUZ the Z-component of the U-direction
	 * @param orthonormalBasisVX the X-component of the V-direction
	 * @param orthonormalBasisVY the Y-component of the V-direction
	 * @param orthonormalBasisVZ the Z-component of the V-direction
	 */
	protected final void orthonormalBasis33FSetFromUV(final float orthonormalBasisUX, final float orthonormalBasisUY, final float orthonormalBasisUZ, final float orthonormalBasisVX, final float orthonormalBasisVY, final float orthonormalBasisVZ) {
//		Compute the normalized U-direction of the orthonormal basis:
		final float orthonormalBasisULengthReciprocal = vector3FLengthReciprocal(orthonormalBasisUX, orthonormalBasisUY, orthonormalBasisUZ);
		final float orthonormalBasisUNormalizedX = orthonormalBasisUX * orthonormalBasisULengthReciprocal;
		final float orthonormalBasisUNormalizedY = orthonormalBasisUY * orthonormalBasisULengthReciprocal;
		final float orthonormalBasisUNormalizedZ = orthonormalBasisUZ * orthonormalBasisULengthReciprocal;
		
//		Compute the normalized V-direction of the orthonormal basis:
		final float orthonormalBasisVLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisVX, orthonormalBasisVY, orthonormalBasisVZ);
		final float orthonormalBasisVNormalizedX = orthonormalBasisVX * orthonormalBasisVLengthReciprocal;
		final float orthonormalBasisVNormalizedY = orthonormalBasisVY * orthonormalBasisVLengthReciprocal;
		final float orthonormalBasisVNormalizedZ = orthonormalBasisVZ * orthonormalBasisVLengthReciprocal;
		
//		Compute the normalized W-direction of the orthonormal basis:
		final float orthonormalBasisWNormalizedX = orthonormalBasisUNormalizedY * orthonormalBasisVNormalizedZ - orthonormalBasisUNormalizedZ * orthonormalBasisVNormalizedY;
		final float orthonormalBasisWNormalizedY = orthonormalBasisUNormalizedZ * orthonormalBasisVNormalizedX - orthonormalBasisUNormalizedX * orthonormalBasisVNormalizedZ;
		final float orthonormalBasisWNormalizedZ = orthonormalBasisUNormalizedX * orthonormalBasisVNormalizedY - orthonormalBasisUNormalizedY * orthonormalBasisVNormalizedX;
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = orthonormalBasisUNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = orthonormalBasisUNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = orthonormalBasisUNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = orthonormalBasisVNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = orthonormalBasisVNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = orthonormalBasisVNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = orthonormalBasisWNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = orthonormalBasisWNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = orthonormalBasisWNormalizedZ;
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the W-direction vector represented by {@code orthonormalBasisWX}, {@code orthonormalBasisWY} and {@code orthonormalBasisWZ}.
	 * <p>
	 * This method will normalize the W-direction vector.
	 * 
	 * @param orthonormalBasisWX the X-component of the W-direction
	 * @param orthonormalBasisWY the Y-component of the W-direction
	 * @param orthonormalBasisWZ the Z-component of the W-direction
	 */
	protected final void orthonormalBasis33FSetFromW(final float orthonormalBasisWX, final float orthonormalBasisWY, final float orthonormalBasisWZ) {
//		Compute the normalized W-direction of the orthonormal basis:
		final float orthonormalBasisWLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisWX, orthonormalBasisWY, orthonormalBasisWZ);
		final float orthonormalBasisWNormalizedX = orthonormalBasisWX * orthonormalBasisWLengthReciprocal;
		final float orthonormalBasisWNormalizedY = orthonormalBasisWY * orthonormalBasisWLengthReciprocal;
		final float orthonormalBasisWNormalizedZ = orthonormalBasisWZ * orthonormalBasisWLengthReciprocal;
		
//		Compute the absolute component values of the normalized W-direction, which are used to determine the orientation of the V-direction of the orthonormal basis:
		final float orthonormalBasisWNormalizedXAbs = abs(orthonormalBasisWNormalizedX);
		final float orthonormalBasisWNormalizedYAbs = abs(orthonormalBasisWNormalizedY);
		final float orthonormalBasisWNormalizedZAbs = abs(orthonormalBasisWNormalizedZ);
		
//		Compute variables used to determine the orientation of the V-direction of the orthonormal basis:
		final boolean isX = orthonormalBasisWNormalizedXAbs < orthonormalBasisWNormalizedYAbs && orthonormalBasisWNormalizedXAbs < orthonormalBasisWNormalizedZAbs;
		final boolean isY = orthonormalBasisWNormalizedYAbs < orthonormalBasisWNormalizedZAbs;
		
//		Compute the normalized V-direction of the orthonormal basis:
		final float orthonormalBasisVX = isX ? +0.0F                         : isY ? +orthonormalBasisWNormalizedZ : +orthonormalBasisWNormalizedY;
		final float orthonormalBasisVY = isX ? +orthonormalBasisWNormalizedZ : isY ? +0.0F                         : -orthonormalBasisWNormalizedX;
		final float orthonormalBasisVZ = isX ? -orthonormalBasisWNormalizedY : isY ? -orthonormalBasisWNormalizedX : +0.0F;
		final float orthonormalBasisVLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisVX, orthonormalBasisVY, orthonormalBasisVZ);
		final float orthonormalBasisVNormalizedX = orthonormalBasisVX * orthonormalBasisVLengthReciprocal;
		final float orthonormalBasisVNormalizedY = orthonormalBasisVY * orthonormalBasisVLengthReciprocal;
		final float orthonormalBasisVNormalizedZ = orthonormalBasisVZ * orthonormalBasisVLengthReciprocal;
		
//		Compute the normalized U-direction of the orthonormal basis:
		final float orthonormalBasisUNormalizedX = orthonormalBasisVNormalizedY * orthonormalBasisWNormalizedZ - orthonormalBasisVNormalizedZ * orthonormalBasisWNormalizedY;
		final float orthonormalBasisUNormalizedY = orthonormalBasisVNormalizedZ * orthonormalBasisWNormalizedX - orthonormalBasisVNormalizedX * orthonormalBasisWNormalizedZ;
		final float orthonormalBasisUNormalizedZ = orthonormalBasisVNormalizedX * orthonormalBasisWNormalizedY - orthonormalBasisVNormalizedY * orthonormalBasisWNormalizedX;
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = orthonormalBasisUNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = orthonormalBasisUNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = orthonormalBasisUNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = orthonormalBasisVNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = orthonormalBasisVNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = orthonormalBasisVNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = orthonormalBasisWNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = orthonormalBasisWNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = orthonormalBasisWNormalizedZ;
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the W-direction vector represented by {@code orthonormalBasisWX}, {@code orthonormalBasisWY} and {@code orthonormalBasisWZ} and the reference V-direction vector represented by
	 * {@code orthonormalBasisVReferenceX}, {@code orthonormalBasisVReferenceY} and {@code orthonormalBasisVReferenceZ}.
	 * <p>
	 * This method will normalize the W-direction and reference V-direction vectors.
	 * 
	 * @param orthonormalBasisWX the X-component of the W-direction
	 * @param orthonormalBasisWY the Y-component of the W-direction
	 * @param orthonormalBasisWZ the Z-component of the W-direction
	 * @param orthonormalBasisVReferenceX the X-component of the reference V-direction
	 * @param orthonormalBasisVReferenceY the Y-component of the reference V-direction
	 * @param orthonormalBasisVReferenceZ the Z-component of the reference V-direction
	 */
	protected final void orthonormalBasis33FSetFromWV(final float orthonormalBasisWX, final float orthonormalBasisWY, final float orthonormalBasisWZ, final float orthonormalBasisVReferenceX, final float orthonormalBasisVReferenceY, final float orthonormalBasisVReferenceZ) {
//		Compute the normalized W-direction of the orthonormal basis:
		final float orthonormalBasisWLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisWX, orthonormalBasisWY, orthonormalBasisWZ);
		final float orthonormalBasisWNormalizedX = orthonormalBasisWX * orthonormalBasisWLengthReciprocal;
		final float orthonormalBasisWNormalizedY = orthonormalBasisWY * orthonormalBasisWLengthReciprocal;
		final float orthonormalBasisWNormalizedZ = orthonormalBasisWZ * orthonormalBasisWLengthReciprocal;
		
//		Compute the normalized V-direction used as a reference for the orthonormal basis:
		final float orthonormalBasisVReferenceLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisVReferenceX, orthonormalBasisVReferenceY, orthonormalBasisVReferenceZ);
		final float orthonormalBasisVReferenceNormalizedX = orthonormalBasisVReferenceX * orthonormalBasisVReferenceLengthReciprocal;
		final float orthonormalBasisVReferenceNormalizedY = orthonormalBasisVReferenceY * orthonormalBasisVReferenceLengthReciprocal;
		final float orthonormalBasisVReferenceNormalizedZ = orthonormalBasisVReferenceZ * orthonormalBasisVReferenceLengthReciprocal;
		
//		Compute the normalized U-direction of the orthonormal basis:
		final float orthonormalBasisUX = orthonormalBasisVReferenceNormalizedY * orthonormalBasisWNormalizedZ - orthonormalBasisVReferenceNormalizedZ * orthonormalBasisWNormalizedY;
		final float orthonormalBasisUY = orthonormalBasisVReferenceNormalizedZ * orthonormalBasisWNormalizedX - orthonormalBasisVReferenceNormalizedX * orthonormalBasisWNormalizedZ;
		final float orthonormalBasisUZ = orthonormalBasisVReferenceNormalizedX * orthonormalBasisWNormalizedY - orthonormalBasisVReferenceNormalizedY * orthonormalBasisWNormalizedX;
		final float orthonormalBasisULengthReciprocal = vector3FLengthReciprocal(orthonormalBasisUX, orthonormalBasisUY, orthonormalBasisUZ);
		final float orthonormalBasisUNormalizedX = orthonormalBasisUX * orthonormalBasisULengthReciprocal;
		final float orthonormalBasisUNormalizedY = orthonormalBasisUY * orthonormalBasisULengthReciprocal;
		final float orthonormalBasisUNormalizedZ = orthonormalBasisUZ * orthonormalBasisULengthReciprocal;
		
//		Compute the normalized V-direction of the orthonormal basis:
		final float orthonormalBasisVNormalizedX = orthonormalBasisWNormalizedY * orthonormalBasisUNormalizedZ - orthonormalBasisWNormalizedZ * orthonormalBasisUNormalizedY;
		final float orthonormalBasisVNormalizedY = orthonormalBasisWNormalizedZ * orthonormalBasisUNormalizedX - orthonormalBasisWNormalizedX * orthonormalBasisUNormalizedZ;
		final float orthonormalBasisVNormalizedZ = orthonormalBasisWNormalizedX * orthonormalBasisUNormalizedY - orthonormalBasisWNormalizedY * orthonormalBasisUNormalizedX;
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = orthonormalBasisUNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = orthonormalBasisUNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = orthonormalBasisUNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = orthonormalBasisVNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = orthonormalBasisVNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = orthonormalBasisVNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = orthonormalBasisWNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = orthonormalBasisWNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = orthonormalBasisWNormalizedZ;
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the orthonormal basis in {@link #intersectionLHSArray_$private$24} that is used for geometry.
	 */
	protected final void orthonormalBasis33FSetIntersectionOrthonormalBasisGLHS() {
//		Get the orthonormal basis:
		final float orthonormalBasisGUX = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0];
		final float orthonormalBasisGUY = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1];
		final float orthonormalBasisGUZ = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2];
		final float orthonormalBasisGVX = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0];
		final float orthonormalBasisGVY = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1];
		final float orthonormalBasisGVZ = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2];
		final float orthonormalBasisGWX = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
		final float orthonormalBasisGWY = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
		final float orthonormalBasisGWZ = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = orthonormalBasisGUX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = orthonormalBasisGUY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = orthonormalBasisGUZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = orthonormalBasisGVX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = orthonormalBasisGVY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = orthonormalBasisGVZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = orthonormalBasisGWX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = orthonormalBasisGWY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = orthonormalBasisGWZ;
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the orthonormal basis in {@link #intersectionRHSArray_$private$24} that is used for geometry.
	 */
	protected final void orthonormalBasis33FSetIntersectionOrthonormalBasisGRHS() {
//		Get the orthonormal basis:
		final float orthonormalBasisGUX = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0];
		final float orthonormalBasisGUY = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1];
		final float orthonormalBasisGUZ = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2];
		final float orthonormalBasisGVX = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0];
		final float orthonormalBasisGVY = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1];
		final float orthonormalBasisGVZ = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2];
		final float orthonormalBasisGWX = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
		final float orthonormalBasisGWY = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
		final float orthonormalBasisGWZ = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = orthonormalBasisGUX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = orthonormalBasisGUY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = orthonormalBasisGUZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = orthonormalBasisGVX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = orthonormalBasisGVY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = orthonormalBasisGVZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = orthonormalBasisGWX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = orthonormalBasisGWY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = orthonormalBasisGWZ;
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the orthonormal basis in {@link #intersectionLHSArray_$private$24} that is used for shading.
	 */
	protected final void orthonormalBasis33FSetIntersectionOrthonormalBasisSLHS() {
//		Get the orthonormal basis:
		final float orthonormalBasisSUX = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0];
		final float orthonormalBasisSUY = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1];
		final float orthonormalBasisSUZ = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2];
		final float orthonormalBasisSVX = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0];
		final float orthonormalBasisSVY = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1];
		final float orthonormalBasisSVZ = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2];
		final float orthonormalBasisSWX = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
		final float orthonormalBasisSWY = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
		final float orthonormalBasisSWZ = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = orthonormalBasisSUX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = orthonormalBasisSUY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = orthonormalBasisSUZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = orthonormalBasisSVX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = orthonormalBasisSVY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = orthonormalBasisSVZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = orthonormalBasisSWX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = orthonormalBasisSWY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = orthonormalBasisSWZ;
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the orthonormal basis in {@link #intersectionRHSArray_$private$24} that is used for shading.
	 */
	protected final void orthonormalBasis33FSetIntersectionOrthonormalBasisSRHS() {
//		Get the orthonormal basis:
		final float orthonormalBasisSUX = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0];
		final float orthonormalBasisSUY = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1];
		final float orthonormalBasisSUZ = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2];
		final float orthonormalBasisSVX = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0];
		final float orthonormalBasisSVY = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1];
		final float orthonormalBasisSVZ = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2];
		final float orthonormalBasisSWX = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
		final float orthonormalBasisSWY = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
		final float orthonormalBasisSWZ = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = orthonormalBasisSUX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = orthonormalBasisSUY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = orthonormalBasisSUZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = orthonormalBasisSVX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = orthonormalBasisSVY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = orthonormalBasisSVZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = orthonormalBasisSWX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = orthonormalBasisSWY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = orthonormalBasisSWZ;
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the W-direction vector found in {@link #vector3FArray_$private$3}.
	 * <p>
	 * This method will normalize the W-direction vector.
	 */
	protected final void orthonormalBasis33FSetVector3F() {
		final float orthonormalBasisWX = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float orthonormalBasisWY = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float orthonormalBasisWZ = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
		orthonormalBasis33FSetFromW(orthonormalBasisWX, orthonormalBasisWY, orthonormalBasisWZ);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Point2F /////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the value of component 1 in {@link #point2FArray_$private$2}.
	 * 
	 * @return the value of component 1 in {@link #point2FArray_$private$2}
	 */
	protected final float point2FGetComponent1() {
		return this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_COMPONENT_1];
	}
	
	/**
	 * Returns the value of component 2 in {@link #point2FArray_$private$2}.
	 * 
	 * @return the value of component 2 in {@link #point2FArray_$private$2}
	 */
	protected final float point2FGetComponent2() {
		return this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_COMPONENT_2];
	}
	
	/**
	 * Sets a point in {@link #point2FArray_$private$2}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 */
	protected final void point2FSet(final float component1, final float component2) {
		this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_COMPONENT_1] = component1;
		this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_COMPONENT_2] = component2;
	}
	
	/**
	 * Sets a point in {@link #point2FArray_$private$2}.
	 * <p>
	 * The point is constructed by sampling a point on a disk with a uniform distribution using concentric mapping.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param radius the radius of the disk
	 */
	protected final void point2FSetSampleDiskUniformDistributionByConcentricMapping(final float u, final float v, final float radius) {
		if(checkIsZero(u) && checkIsZero(v)) {
			this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_COMPONENT_1] = 0.0F;
			this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_COMPONENT_2] = 0.0F;
		} else {
			final float a = u * 2.0F - 1.0F;
			final float b = v * 2.0F - 1.0F;
			
			final boolean isCaseA = a * a > b * b;
			
			final float phi = isCaseA ? PI_DIVIDED_BY_4 * (b / a) : PI_DIVIDED_BY_2 - PI_DIVIDED_BY_4 * (a / b);
			final float r = isCaseA ? radius * a : radius * b;
			
			this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_COMPONENT_1] = r * cos(phi);
			this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_COMPONENT_2] = r * sin(phi);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Point3F /////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the distance from the point represented by {@code eyeComponent1}, {@code eyeComponent2} and {@code eyeComponent3} to the point represented by {@code lookAtComponent1}, {@code lookAtComponent2} and {@code lookAtComponent3}.
	 * 
	 * @param eyeComponent1 the value of component 1 for the eye point
	 * @param eyeComponent2 the value of component 2 for the eye point
	 * @param eyeComponent3 the value of component 3 for the eye point
	 * @param lookAtComponent1 the value of component 1 for the look at point
	 * @param lookAtComponent2 the value of component 2 for the look at point
	 * @param lookAtComponent3 the value of component 3 for the look at point
	 * @return the distance from the point represented by {@code eyeComponent1}, {@code eyeComponent2} and {@code eyeComponent3} to the point represented by {@code lookAtComponent1}, {@code lookAtComponent2} and {@code lookAtComponent3}
	 */
	protected final float point3FDistance(final float eyeComponent1, final float eyeComponent2, final float eyeComponent3, final float lookAtComponent1, final float lookAtComponent2, final float lookAtComponent3) {
		return sqrt(point3FDistanceSquared(eyeComponent1, eyeComponent2, eyeComponent3, lookAtComponent1, lookAtComponent2, lookAtComponent3));
	}
	
	/**
	 * Returns the squared distance from the point represented by {@code eyeComponent1}, {@code eyeComponent2} and {@code eyeComponent3} to the point represented by {@code lookAtComponent1}, {@code lookAtComponent2} and {@code lookAtComponent3}.
	 * 
	 * @param eyeComponent1 the value of component 1 for the eye point
	 * @param eyeComponent2 the value of component 2 for the eye point
	 * @param eyeComponent3 the value of component 3 for the eye point
	 * @param lookAtComponent1 the value of component 1 for the look at point
	 * @param lookAtComponent2 the value of component 2 for the look at point
	 * @param lookAtComponent3 the value of component 3 for the look at point
	 * @return the squared distance from the point represented by {@code eyeComponent1}, {@code eyeComponent2} and {@code eyeComponent3} to the point represented by {@code lookAtComponent1}, {@code lookAtComponent2} and {@code lookAtComponent3}
	 */
	protected final float point3FDistanceSquared(final float eyeComponent1, final float eyeComponent2, final float eyeComponent3, final float lookAtComponent1, final float lookAtComponent2, final float lookAtComponent3) {
		return vector3FLengthSquared(lookAtComponent1 - eyeComponent1, lookAtComponent2 - eyeComponent2, lookAtComponent3 - eyeComponent3);
	}
	
	/**
	 * Returns the value of component 1 in {@link #point3FArray_$private$3}.
	 * 
	 * @return the value of component 1 in {@link #point3FArray_$private$3}
	 */
	protected final float point3FGetComponent1() {
		return this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_1];
	}
	
	/**
	 * Returns the value of component 2 in {@link #point3FArray_$private$3}.
	 * 
	 * @return the value of component 2 in {@link #point3FArray_$private$3}
	 */
	protected final float point3FGetComponent2() {
		return this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_2];
	}
	
	/**
	 * Returns the value of component 3 in {@link #point3FArray_$private$3}.
	 * 
	 * @return the value of component 3 in {@link #point3FArray_$private$3}
	 */
	protected final float point3FGetComponent3() {
		return this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_3];
	}
	
	/**
	 * Sets a point in {@link #point3FArray_$private$3}.
	 * <p>
	 * The point is constructed by transforming the point represented by {@code component1}, {@code component2} and {@code component3} with the supplied matrix.
	 * 
	 * @param element11 the value of the element at row 1 and column 1 in the matrix
	 * @param element12 the value of the element at row 1 and column 2 in the matrix
	 * @param element13 the value of the element at row 1 and column 3 in the matrix
	 * @param element14 the value of the element at row 1 and column 4 in the matrix
	 * @param element21 the value of the element at row 2 and column 1 in the matrix
	 * @param element22 the value of the element at row 2 and column 2 in the matrix
	 * @param element23 the value of the element at row 2 and column 3 in the matrix
	 * @param element24 the value of the element at row 2 and column 4 in the matrix
	 * @param element31 the value of the element at row 3 and column 1 in the matrix
	 * @param element32 the value of the element at row 3 and column 2 in the matrix
	 * @param element33 the value of the element at row 3 and column 3 in the matrix
	 * @param element34 the value of the element at row 3 and column 4 in the matrix
	 * @param component1 the value of component 1 of the point
	 * @param component2 the value of component 2 of the point
	 * @param component3 the value of component 3 of the point
	 */
	protected final void point3FSetMatrix44FTransform(final float element11, final float element12, final float element13, final float element14, final float element21, final float element22, final float element23, final float element24, final float element31, final float element32, final float element33, final float element34, final float component1, final float component2, final float component3) {
		final float newComponent1 = element11 * component1 + element12 * component2 + element13 * component3 + element14;
		final float newComponent2 = element21 * component1 + element22 * component2 + element23 * component3 + element24;
		final float newComponent3 = element31 * component1 + element32 * component2 + element33 * component3 + element34;
		
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_1] = newComponent1;
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_2] = newComponent2;
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_3] = newComponent3;
	}
	
	/**
	 * Sets a point in {@link #point3FArray_$private$3}.
	 * <p>
	 * The point is constructed by transforming the point represented by {@code component1}, {@code component2} and {@code component3} with the supplied matrix.
	 * <p>
	 * If the value of component 4, which is produced by the transformation, is anything but {@code 0.0} or {@code 1.0}, the values of component 1, component 2 and component 3 will be divided by the value of component 4.
	 * 
	 * @param element11 the value of the element at row 1 and column 1 in the matrix
	 * @param element12 the value of the element at row 1 and column 2 in the matrix
	 * @param element13 the value of the element at row 1 and column 3 in the matrix
	 * @param element14 the value of the element at row 1 and column 4 in the matrix
	 * @param element21 the value of the element at row 2 and column 1 in the matrix
	 * @param element22 the value of the element at row 2 and column 2 in the matrix
	 * @param element23 the value of the element at row 2 and column 3 in the matrix
	 * @param element24 the value of the element at row 2 and column 4 in the matrix
	 * @param element31 the value of the element at row 3 and column 1 in the matrix
	 * @param element32 the value of the element at row 3 and column 2 in the matrix
	 * @param element33 the value of the element at row 3 and column 3 in the matrix
	 * @param element34 the value of the element at row 3 and column 4 in the matrix
	 * @param element41 the value of the element at row 4 and column 1 in the matrix
	 * @param element42 the value of the element at row 4 and column 2 in the matrix
	 * @param element43 the value of the element at row 4 and column 3 in the matrix
	 * @param element44 the value of the element at row 4 and column 4 in the matrix
	 * @param component1 the value of component 1 of the point
	 * @param component2 the value of component 2 of the point
	 * @param component3 the value of component 3 of the point
	 */
	protected final void point3FSetMatrix44FTransformAndDivide(final float element11, final float element12, final float element13, final float element14, final float element21, final float element22, final float element23, final float element24, final float element31, final float element32, final float element33, final float element34, final float element41, final float element42, final float element43, final float element44, final float component1, final float component2, final float component3) {
		final float newComponent1 = element11 * component1 + element12 * component2 + element13 * component3 + element14;
		final float newComponent2 = element21 * component1 + element22 * component2 + element23 * component3 + element24;
		final float newComponent3 = element31 * component1 + element32 * component2 + element33 * component3 + element34;
		final float newComponent4 = element41 * component1 + element42 * component2 + element43 * component3 + element44;
		
		final boolean isDividing = newComponent4 != +0.0F && newComponent4 != -0.0F && newComponent4 != 1.0F;
		
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_1] = isDividing ? newComponent1 / newComponent4 : newComponent1;
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_2] = isDividing ? newComponent2 / newComponent4 : newComponent2;
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_3] = isDividing ? newComponent3 / newComponent4 : newComponent3;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Ray3F ///////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the value of component 1 of the vector called direction of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the value of component 1 of the vector called direction of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetDirectionComponent1() {
		return this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
	}
	
	/**
	 * Returns the value of component 2 of the vector called direction of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the value of component 2 of the vector called direction of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetDirectionComponent2() {
		return this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
	}
	
	/**
	 * Returns the value of component 3 of the vector called direction of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the value of component 3 of the vector called direction of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetDirectionComponent3() {
		return this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
	}
	
	/**
	 * Returns the reciprocal (or inverse) value of component 1 of the vector called direction of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the reciprocal (or inverse) value of component 1 of the vector called direction of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetDirectionReciprocalComponent1() {
		return 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
	}
	
	/**
	 * Returns the reciprocal (or inverse) value of component 2 of the vector called direction of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the reciprocal (or inverse) value of component 2 of the vector called direction of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetDirectionReciprocalComponent2() {
		return 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
	}
	
	/**
	 * Returns the reciprocal (or inverse) value of component 3 of the vector called direction of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the reciprocal (or inverse) value of component 3 of the vector called direction of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetDirectionReciprocalComponent3() {
		return 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
	}
	
	/**
	 * Returns the value of component 1 of the point called origin of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the value of component 1 of the point called origin of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetOriginComponent1() {
		return this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
	}
	
	/**
	 * Returns the value of component 2 of the point called origin of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the value of component 2 of the point called origin of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetOriginComponent2() {
		return this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
	}
	
	/**
	 * Returns the value of component 3 of the point called origin of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the value of component 3 of the point called origin of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetOriginComponent3() {
		return this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
	}
	
	/**
	 * Returns the maximum parametric distance of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the maximum parametric distance of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetTMaximum() {
		return this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM];
	}
	
	/**
	 * Returns the minimum parametric distance of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the minimum parametric distance of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetTMinimum() {
		return this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MINIMUM];
	}
	
	/**
	 * Sets the component values for the vector called direction of the ray in {@link ray3FArray_$private$8}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	protected final void ray3FSetDirection(final float component1, final float component2, final float component3) {
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0] = component1;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1] = component2;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2] = component3;
	}
	
	/**
	 * Sets a ray in {@link #ray3FArray_$private$8}.
	 * <p>
	 * The ray direction is constructed using a normalized representation of the current vector in {@link #vector3FArray_$private$3}. The origin is constructed by offsetting the surface intersection point in {@link #intersectionLHSArray_$private$24}
	 * slightly, in the direction of the ray itself.
	 */
	protected final void ray3FSetFromSurfaceIntersectionPointAndVector3FLHS() {
		final float surfaceIntersectionPointX = intersectionLHSGetSurfaceIntersectionPointComponent1();
		final float surfaceIntersectionPointY = intersectionLHSGetSurfaceIntersectionPointComponent2();
		final float surfaceIntersectionPointZ = intersectionLHSGetSurfaceIntersectionPointComponent3();
		
		final float surfaceNormalSX = intersectionLHSGetOrthonormalBasisSWComponent1();
		final float surfaceNormalSY = intersectionLHSGetOrthonormalBasisSWComponent2();
		final float surfaceNormalSZ = intersectionLHSGetOrthonormalBasisSWComponent3();
		
		final float directionX = vector3FGetComponent1();
		final float directionY = vector3FGetComponent2();
		final float directionZ = vector3FGetComponent3();
		final float directionLengthReciprocal = vector3FLengthReciprocal(directionX, directionY, directionZ);
		final float directionNormalizedX = directionX * directionLengthReciprocal;
		final float directionNormalizedY = directionY * directionLengthReciprocal;
		final float directionNormalizedZ = directionZ * directionLengthReciprocal;
		
		final float nDotD = vector3FDotProduct(surfaceNormalSX, surfaceNormalSY, surfaceNormalSZ, directionNormalizedX, directionNormalizedY, directionNormalizedZ);
		final float nDotE = 0.0F;
		
		final float offsetX = surfaceNormalSX * nDotE;
		final float offsetY = surfaceNormalSY * nDotE;
		final float offsetZ = surfaceNormalSZ * nDotE;
		final float offsetCorrectlyOrientedX = nDotD < 0.0F ? -offsetX : offsetX;
		final float offsetCorrectlyOrientedY = nDotD < 0.0F ? -offsetY : offsetY;
		final float offsetCorrectlyOrientedZ = nDotD < 0.0F ? -offsetZ : offsetZ;
		
		final float originOffsetX = surfaceIntersectionPointX + offsetCorrectlyOrientedX;
		final float originOffsetY = surfaceIntersectionPointY + offsetCorrectlyOrientedY;
		final float originOffsetZ = surfaceIntersectionPointZ + offsetCorrectlyOrientedZ;
		final float originX = nextAfter(originOffsetX, originOffsetX > 0.0F ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY);
		final float originY = nextAfter(originOffsetY, originOffsetY > 0.0F ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY);
		final float originZ = nextAfter(originOffsetZ, originOffsetZ > 0.0F ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY);
		
		ray3FSetOrigin(originX, originY, originZ);
		ray3FSetDirection(directionNormalizedX, directionNormalizedY, directionNormalizedZ);
		ray3FSetTMinimum(DEFAULT_T_MINIMUM);
		ray3FSetTMaximum(DEFAULT_T_MAXIMUM);
	}
	
	/**
	 * Sets a ray in {@link #ray3FArray_$private$8}.
	 * <p>
	 * The ray direction is constructed using a normalized representation of the current vector in {@link #vector3FArray_$private$3}. The origin is constructed by offsetting the surface intersection point in {@link #intersectionRHSArray_$private$24}
	 * slightly, in the direction of the ray itself.
	 */
	protected final void ray3FSetFromSurfaceIntersectionPointAndVector3FRHS() {
		final float surfaceIntersectionPointX = intersectionRHSGetSurfaceIntersectionPointComponent1();
		final float surfaceIntersectionPointY = intersectionRHSGetSurfaceIntersectionPointComponent2();
		final float surfaceIntersectionPointZ = intersectionRHSGetSurfaceIntersectionPointComponent3();
		
		final float surfaceNormalSX = intersectionRHSGetOrthonormalBasisSWComponent1();
		final float surfaceNormalSY = intersectionRHSGetOrthonormalBasisSWComponent2();
		final float surfaceNormalSZ = intersectionRHSGetOrthonormalBasisSWComponent3();
		
		final float directionX = vector3FGetComponent1();
		final float directionY = vector3FGetComponent2();
		final float directionZ = vector3FGetComponent3();
		final float directionLengthReciprocal = vector3FLengthReciprocal(directionX, directionY, directionZ);
		final float directionNormalizedX = directionX * directionLengthReciprocal;
		final float directionNormalizedY = directionY * directionLengthReciprocal;
		final float directionNormalizedZ = directionZ * directionLengthReciprocal;
		
		final float nDotD = vector3FDotProduct(surfaceNormalSX, surfaceNormalSY, surfaceNormalSZ, directionNormalizedX, directionNormalizedY, directionNormalizedZ);
		final float nDotE = 0.0F;
		
		final float offsetX = surfaceNormalSX * nDotE;
		final float offsetY = surfaceNormalSY * nDotE;
		final float offsetZ = surfaceNormalSZ * nDotE;
		final float offsetCorrectlyOrientedX = nDotD < 0.0F ? -offsetX : offsetX;
		final float offsetCorrectlyOrientedY = nDotD < 0.0F ? -offsetY : offsetY;
		final float offsetCorrectlyOrientedZ = nDotD < 0.0F ? -offsetZ : offsetZ;
		
		final float originOffsetX = surfaceIntersectionPointX + offsetCorrectlyOrientedX;
		final float originOffsetY = surfaceIntersectionPointY + offsetCorrectlyOrientedY;
		final float originOffsetZ = surfaceIntersectionPointZ + offsetCorrectlyOrientedZ;
		final float originX = nextAfter(originOffsetX, originOffsetX > 0.0F ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY);
		final float originY = nextAfter(originOffsetY, originOffsetY > 0.0F ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY);
		final float originZ = nextAfter(originOffsetZ, originOffsetZ > 0.0F ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY);
		
		ray3FSetOrigin(originX, originY, originZ);
		ray3FSetDirection(directionNormalizedX, directionNormalizedY, directionNormalizedZ);
		ray3FSetTMinimum(DEFAULT_T_MINIMUM);
		ray3FSetTMaximum(DEFAULT_T_MAXIMUM);
	}
	
	/**
	 * Sets the component values for the point called origin of the ray in {@link ray3FArray_$private$8}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	protected final void ray3FSetOrigin(final float component1, final float component2, final float component3) {
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0] = component1;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1] = component2;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2] = component3;
	}
	
	/**
	 * Sets the maximum parametric distance of the ray in {@link ray3FArray_$private$8}.
	 * 
	 * @param tMaximum the maximum parametric distance
	 */
	protected final void ray3FSetTMaximum(final float tMaximum) {
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM] = tMaximum;
	}
	
	/**
	 * Sets the minimum parametric distance of the ray in {@link ray3FArray_$private$8}.
	 * 
	 * @param tMinimum the minimum parametric distance
	 */
	protected final void ray3FSetTMinimum(final float tMinimum) {
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MINIMUM] = tMinimum;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Shape2F - Line2F ////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, the line denoted by the points {@code pointAX}, {@code pointAY}, {@code pointBX} and {@code pointBY} contains the point denoted by {@code pointPX} and {@code pointPY}, {@code false} otherwise.
	 * 
	 * @param pointAX the X-component of the point denoted by A on the line
	 * @param pointAY the Y-component of the point denoted by A on the line
	 * @param pointBX the X-component of the point denoted by B on the line
	 * @param pointBY the Y-component of the point denoted by B on the line
	 * @param pointPX the X-component of the point to check
	 * @param pointPY the Y-component of the point to check
	 * @return {@code true} if, and only if, the line denoted by the points {@code pointAX}, {@code pointAY}, {@code pointBX} and {@code pointBY} contains the point denoted by {@code pointPX} and {@code pointPY}, {@code false} otherwise
	 */
	protected final boolean shape2FLine2FContains(final float pointAX, final float pointAY, final float pointBX, final float pointBY, final float pointPX, final float pointPY) {
		if(checkIsNearlyEqual(pointPX, pointAX) && checkIsNearlyEqual(pointPY, pointAY)) {
			return true;
		}
		
		if(checkIsNearlyEqual(pointPX, pointBX) && checkIsNearlyEqual(pointPY, pointBY)) {
			return true;
		}
		
		final float vectorABX = pointBX - pointAX;
		final float vectorABY = pointBY - pointAY;
		
		final float vectorAPX = pointPX - pointAX;
		final float vectorAPY = pointPY - pointAY;
		
		final float crossProduct = vectorAPX * vectorABY - vectorAPY * vectorABX;
		
		if(!checkIsZero(crossProduct)) {
			return false;
		}
		
		final boolean containsX = vectorABX > 0.0F ? pointAX <= pointPX && pointPX <= pointBX : pointBX <= pointPX && pointPX <= pointAX;
		final boolean containsY = vectorABY > 0.0F ? pointAY <= pointPY && pointPY <= pointBY : pointBY <= pointPY && pointPY <= pointAY;
		final boolean contains = abs(vectorABX) >= abs(vectorABY) ? containsX : containsY;
		
		return contains;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Shape3F - Cone3F ////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given cone in object space, {@code false} otherwise.
	 * 
	 * @param shape3FCone3FArrayOffset the offset for the cone in {@link #shape3FCone3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given cone in object space, {@code false} otherwise
	 */
	protected final boolean shape3FCone3FIntersects(final int shape3FCone3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return shape3FCone3FIntersectionT(shape3FCone3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns the parametric T value for a given cone in object space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @param shape3FCone3FArrayOffset the offset for the cone in {@link #shape3FCone3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return the parametric T value for a given cone in object space, or {@code 0.0F} if no intersection was found
	 */
	protected final float shape3FCone3FIntersectionT(final int shape3FCone3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the disk variables that will be referred to by 'conePhiMax', 'coneRadius' and 'coneZMax' in the comments:
		final float conePhiMax = this.shape3FCone3FArray[shape3FCone3FArrayOffset + CompiledShape3FCache.CONE_3_F_OFFSET_PHI_MAX];
		final float coneRadius = this.shape3FCone3FArray[shape3FCone3FArrayOffset + CompiledShape3FCache.CONE_3_F_OFFSET_RADIUS];
		final float coneZMax = this.shape3FCone3FArray[shape3FCone3FArrayOffset + CompiledShape3FCache.CONE_3_F_OFFSET_Z_MAX];
		
		final float k = (coneRadius / coneZMax) * (coneRadius / coneZMax);
		
		final float a = rayDirectionX * rayDirectionX + rayDirectionY * rayDirectionY - k * rayDirectionZ * rayDirectionZ;
		final float b = 2.0F * (rayDirectionX * rayOriginX + rayDirectionY * rayOriginY - k * rayDirectionZ * (rayOriginZ - coneZMax));
		final float c = rayOriginX * rayOriginX + rayOriginY * rayOriginY - k * (rayOriginZ - coneZMax) * (rayOriginZ - coneZMax);
		
		solveQuadraticSystemToArray(a, b, c, rayTMinimum, rayTMaximum);
		
		final float tMinimum = solveQuadraticSystemToArrayGetMinimum();
		final float tMaximum = solveQuadraticSystemToArrayGetMaximum();
		
		if(tMinimum == 0.0F) {
			return 0.0F;
		}
		
		final float xMinimum = rayOriginX + rayDirectionX * tMinimum;
		final float yMinimum = rayOriginY + rayDirectionY * tMinimum;
		final float zMinimum = rayOriginZ + rayDirectionZ * tMinimum;
		
		final float phiMinimum = addIfLessThanThreshold(atan2(yMinimum, xMinimum), 0.0F, PI_MULTIPLIED_BY_2);
		
		if(zMinimum < 0.0F || zMinimum > coneZMax || phiMinimum > conePhiMax) {
			if(tMaximum == 0.0F) {
				return 0.0F;
			}
			
			final float xMaximum = rayOriginX + rayDirectionX * tMaximum;
			final float yMaximum = rayOriginY + rayDirectionY * tMaximum;
			final float zMaximum = rayOriginZ + rayDirectionZ * tMaximum;
			
			final float phiMaximum = addIfLessThanThreshold(atan2(yMaximum, xMaximum), 0.0F, PI_MULTIPLIED_BY_2);
			
			if(zMaximum < 0.0F || zMaximum > coneZMax || phiMaximum > conePhiMax) {
				return 0.0F;
			}
			
			return tMaximum;
		}
		
		return tMinimum;
	}
	
	/**
	 * Computes the intersection properties for the cone at offset {@code shape3FCone3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the cone
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FCone3FArrayOffset the offset in {@link #shape3FCone3FArray}
	 */
	protected final void shape3FCone3FIntersectionComputeLHS(final float t, final int primitiveIndex, final int shape3FCone3FArrayOffset) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the disk variables that will be referred to by 'conePhiMax' and 'coneZMax' in the comments:
		final float conePhiMax = this.shape3FCone3FArray[shape3FCone3FArrayOffset + CompiledShape3FCache.CONE_3_F_OFFSET_PHI_MAX];
		final float coneZMax = this.shape3FCone3FArray[shape3FCone3FArrayOffset + CompiledShape3FCache.CONE_3_F_OFFSET_Z_MAX];
		
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, PI_MULTIPLIED_BY_2) / conePhiMax;
		final float textureCoordinatesV = surfaceIntersectionPointZ / coneZMax;
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromUV(-conePhiMax * surfaceIntersectionPointY, +conePhiMax * surfaceIntersectionPointX, 0.0F, -surfaceIntersectionPointX / (1.0F - textureCoordinatesV), -surfaceIntersectionPointY / (1.0F - textureCoordinatesV), coneZMax);
		
//		Update the intersection array:
		intersectionLHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionLHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionLHSSetPrimitiveIndex(primitiveIndex);
		intersectionLHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionLHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the cone at offset {@code shape3FCone3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the cone
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FCone3FArrayOffset the offset in {@link #shape3FCone3FArray}
	 */
	protected final void shape3FCone3FIntersectionComputeRHS(final float t, final int primitiveIndex, final int shape3FCone3FArrayOffset) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the disk variables that will be referred to by 'conePhiMax' and 'coneZMax' in the comments:
		final float conePhiMax = this.shape3FCone3FArray[shape3FCone3FArrayOffset + CompiledShape3FCache.CONE_3_F_OFFSET_PHI_MAX];
		final float coneZMax = this.shape3FCone3FArray[shape3FCone3FArrayOffset + CompiledShape3FCache.CONE_3_F_OFFSET_Z_MAX];
		
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, PI_MULTIPLIED_BY_2) / conePhiMax;
		final float textureCoordinatesV = surfaceIntersectionPointZ / coneZMax;
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromUV(-conePhiMax * surfaceIntersectionPointY, +conePhiMax * surfaceIntersectionPointX, 0.0F, -surfaceIntersectionPointX / (1.0F - textureCoordinatesV), -surfaceIntersectionPointY / (1.0F - textureCoordinatesV), coneZMax);
		
//		Update the intersection array:
		intersectionRHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionRHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionRHSSetPrimitiveIndex(primitiveIndex);
		intersectionRHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionRHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Shape3F - Cylinder3F ////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given cylinder in object space, {@code false} otherwise.
	 * 
	 * @param shape3FCylinder3FArrayOffset the offset for the cone in {@link #shape3FCylinder3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given cylinder in object space, {@code false} otherwise
	 */
	protected final boolean shape3FCylinder3FIntersects(final int shape3FCylinder3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return shape3FCylinder3FIntersectionT(shape3FCylinder3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns the parametric T value for a given cylinder in object space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @param shape3FCylinder3FArrayOffset the offset for the cylinder in {@link #shape3FCylinder3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return the parametric T value for a given cylinder in object space, or {@code 0.0F} if no intersection was found
	 */
	protected final float shape3FCylinder3FIntersectionT(final int shape3FCylinder3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the cylinder variables that will be referred to by 'cylinderPhiMax', 'cylinderRadius', 'cylinderZMax' and 'cylinderZMin' in the comments:
		final float cylinderPhiMax = this.shape3FCylinder3FArray[shape3FCylinder3FArrayOffset + CompiledShape3FCache.CYLINDER_3_F_OFFSET_PHI_MAX];
		final float cylinderRadius = this.shape3FCylinder3FArray[shape3FCylinder3FArrayOffset + CompiledShape3FCache.CYLINDER_3_F_OFFSET_RADIUS];
		final float cylinderZMax = this.shape3FCylinder3FArray[shape3FCylinder3FArrayOffset + CompiledShape3FCache.CYLINDER_3_F_OFFSET_Z_MAX];
		final float cylinderZMin = this.shape3FCylinder3FArray[shape3FCylinder3FArrayOffset + CompiledShape3FCache.CYLINDER_3_F_OFFSET_Z_MIN];
		
		final float a = rayDirectionX * rayDirectionX + rayDirectionY * rayDirectionY;
		final float b = 2.0F * (rayDirectionX * rayOriginX + rayDirectionY * rayOriginY);
		final float c = rayOriginX * rayOriginX + rayOriginY * rayOriginY - cylinderRadius * cylinderRadius;
		
		solveQuadraticSystemToArray(a, b, c, rayTMinimum, rayTMaximum);
		
		final float tMinimum = solveQuadraticSystemToArrayGetMinimum();
		final float tMaximum = solveQuadraticSystemToArrayGetMaximum();
		
		if(tMinimum == 0.0F) {
			return 0.0F;
		}
		
		final float xMinimum0 = rayOriginX + rayDirectionX * tMinimum;
		final float yMinimum0 = rayOriginY + rayDirectionY * tMinimum;
		final float zMinimum0 = rayOriginZ + rayDirectionZ * tMinimum;
		
		final float radiusMinimum = sqrt(xMinimum0 * xMinimum0 + yMinimum0 * yMinimum0);
		
		final float xMinimum1 = xMinimum0 * (cylinderRadius / radiusMinimum);
		final float yMinimum1 = yMinimum0 * (cylinderRadius / radiusMinimum);
		final float zMinimum1 = zMinimum0;
		
		final float phiMinimum = addIfLessThanThreshold(atan2(yMinimum1, xMinimum1), 0.0F, PI_MULTIPLIED_BY_2);
		
		if(zMinimum1 < cylinderZMin || zMinimum1 > cylinderZMax || phiMinimum > cylinderPhiMax) {
			if(tMaximum == 0.0F) {
				return 0.0F;
			}
			
			final float xMaximum0 = rayOriginX + rayDirectionX * tMaximum;
			final float yMaximum0 = rayOriginY + rayDirectionY * tMaximum;
			final float zMaximum0 = rayOriginZ + rayDirectionZ * tMaximum;
			
			final float radiusMaximum = sqrt(xMaximum0 * xMaximum0 + yMaximum0 * yMaximum0);
			
			final float xMaximum1 = xMaximum0 * (cylinderRadius / radiusMaximum);
			final float yMaximum1 = yMaximum0 * (cylinderRadius / radiusMaximum);
			final float zMaximum1 = zMaximum0;
			
			final float phiMaximum = addIfLessThanThreshold(atan2(yMaximum1, xMaximum1), 0.0F, PI_MULTIPLIED_BY_2);
			
			if(zMaximum1 < cylinderZMin || zMaximum1 > cylinderZMax || phiMaximum > cylinderPhiMax) {
				return 0.0F;
			}
			
			return tMaximum;
		}
		
		return tMinimum;
	}
	
	/**
	 * Computes the intersection properties for the cylinder at offset {@code shape3FCylinder3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the cylinder
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FCylinder3FArrayOffset the offset in {@link #shape3FCylinder3FArray}
	 */
	protected final void shape3FCylinder3FIntersectionComputeLHS(final float t, final int primitiveIndex, final int shape3FCylinder3FArrayOffset) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the cylinder variables that will be referred to by 'cylinderPhiMax', 'cylinderRadius', 'cylinderZMax' and 'cylinderZMin' in the comments:
		final float cylinderPhiMax = this.shape3FCylinder3FArray[shape3FCylinder3FArrayOffset + CompiledShape3FCache.CYLINDER_3_F_OFFSET_PHI_MAX];
		final float cylinderRadius = this.shape3FCylinder3FArray[shape3FCylinder3FArrayOffset + CompiledShape3FCache.CYLINDER_3_F_OFFSET_RADIUS];
		final float cylinderZMax = this.shape3FCylinder3FArray[shape3FCylinder3FArrayOffset + CompiledShape3FCache.CYLINDER_3_F_OFFSET_Z_MAX];
		final float cylinderZMin = this.shape3FCylinder3FArray[shape3FCylinder3FArrayOffset + CompiledShape3FCache.CYLINDER_3_F_OFFSET_Z_MIN];
		
		final float x = rayOriginX + rayDirectionX * t;
		final float y = rayOriginY + rayDirectionY * t;
		final float z = rayOriginZ + rayDirectionZ * t;
		
		final float radius = sqrt(x * x + y * y);
		
		final float surfaceIntersectionPointX = x * (cylinderRadius / radius);
		final float surfaceIntersectionPointY = y * (cylinderRadius / radius);
		final float surfaceIntersectionPointZ = z;
		
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, PI_MULTIPLIED_BY_2) / cylinderPhiMax;
		final float textureCoordinatesV = (surfaceIntersectionPointZ - cylinderZMin) / (cylinderZMax - cylinderZMin);
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromUV(-cylinderPhiMax * surfaceIntersectionPointY, +cylinderPhiMax * surfaceIntersectionPointX, 0.0F, 0.0F, 0.0F, cylinderZMax - cylinderZMin);
		
//		Update the intersection array:
		intersectionLHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionLHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionLHSSetPrimitiveIndex(primitiveIndex);
		intersectionLHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionLHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the cylinder at offset {@code shape3FCylinder3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the cylinder
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FCylinder3FArrayOffset the offset in {@link #shape3FCylinder3FArray}
	 */
	protected final void shape3FCylinder3FIntersectionComputeRHS(final float t, final int primitiveIndex, final int shape3FCylinder3FArrayOffset) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the cylinder variables that will be referred to by 'cylinderPhiMax', 'cylinderRadius', 'cylinderZMax' and 'cylinderZMin' in the comments:
		final float cylinderPhiMax = this.shape3FCylinder3FArray[shape3FCylinder3FArrayOffset + CompiledShape3FCache.CYLINDER_3_F_OFFSET_PHI_MAX];
		final float cylinderRadius = this.shape3FCylinder3FArray[shape3FCylinder3FArrayOffset + CompiledShape3FCache.CYLINDER_3_F_OFFSET_RADIUS];
		final float cylinderZMax = this.shape3FCylinder3FArray[shape3FCylinder3FArrayOffset + CompiledShape3FCache.CYLINDER_3_F_OFFSET_Z_MAX];
		final float cylinderZMin = this.shape3FCylinder3FArray[shape3FCylinder3FArrayOffset + CompiledShape3FCache.CYLINDER_3_F_OFFSET_Z_MIN];
		
		final float x = rayOriginX + rayDirectionX * t;
		final float y = rayOriginY + rayDirectionY * t;
		final float z = rayOriginZ + rayDirectionZ * t;
		
		final float radius = sqrt(x * x + y * y);
		
		final float surfaceIntersectionPointX = x * (cylinderRadius / radius);
		final float surfaceIntersectionPointY = y * (cylinderRadius / radius);
		final float surfaceIntersectionPointZ = z;
		
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, PI_MULTIPLIED_BY_2) / cylinderPhiMax;
		final float textureCoordinatesV = (surfaceIntersectionPointZ - cylinderZMin) / (cylinderZMax - cylinderZMin);
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromUV(-cylinderPhiMax * surfaceIntersectionPointY, +cylinderPhiMax * surfaceIntersectionPointX, 0.0F, 0.0F, 0.0F, cylinderZMax - cylinderZMin);
		
//		Update the intersection array:
		intersectionRHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionRHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionRHSSetPrimitiveIndex(primitiveIndex);
		intersectionRHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionRHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Shape3F - Disk3F ////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given disk in object space, {@code false} otherwise.
	 * 
	 * @param shape3FDisk3FArrayOffset the offset for the disk in {@link #shape3FDisk3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given disk in object space, {@code false} otherwise
	 */
	protected final boolean shape3FDisk3FIntersects(final int shape3FDisk3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return shape3FDisk3FIntersectionT(shape3FDisk3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns the parametric T value for a given disk in object space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @param shape3FDisk3FArrayOffset the offset for the disk in {@link #shape3FDisk3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return the parametric T value for a given disk in object space, or {@code 0.0F} if no intersection was found
	 */
	protected final float shape3FDisk3FIntersectionT(final int shape3FDisk3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
		if(rayDirectionZ == 0.0F) {
			return 0.0F;
		}
		
//		Retrieve the disk variables that will be referred to by 'diskPhiMax', 'diskRadiusInner', 'diskRadiusOuter' and 'diskZMax' in the comments:
		final float diskPhiMax = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + CompiledShape3FCache.DISK_3_F_OFFSET_PHI_MAX];
		final float diskRadiusInner = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + CompiledShape3FCache.DISK_3_F_OFFSET_RADIUS_INNER];
		final float diskRadiusOuter = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + CompiledShape3FCache.DISK_3_F_OFFSET_RADIUS_OUTER];
		final float diskZMax = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + CompiledShape3FCache.DISK_3_F_OFFSET_Z_MAX];
		
		final float t = (diskZMax - rayOriginZ) / rayDirectionZ;
		
		if(t <= rayTMinimum || t >= rayTMaximum) {
			return 0.0F;
		}
		
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		
		final float distanceSquared = surfaceIntersectionPointX * surfaceIntersectionPointX + surfaceIntersectionPointY * surfaceIntersectionPointY;
		
		if(distanceSquared > diskRadiusOuter * diskRadiusOuter || distanceSquared < diskRadiusInner * diskRadiusInner) {
			return 0.0F;
		}
		
		final float phi = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, PI_MULTIPLIED_BY_2);
		
		if(phi > diskPhiMax) {
			return 0.0F;
		}
		
		return t;
	}
	
	/**
	 * Computes the intersection properties for the disk at offset {@code shape3FDisk3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the disk
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FDisk3FArrayOffset the offset in {@link #shape3FDisk3FArray}
	 */
	protected final void shape3FDisk3FIntersectionComputeLHS(final float t, final int primitiveIndex, final int shape3FDisk3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		
//		Retrieve the disk variables that will be referred to by 'diskPhiMax', 'diskRadiusInner', 'diskRadiusOuter' and 'diskZMax' in the comments:
		final float diskPhiMax = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + CompiledShape3FCache.DISK_3_F_OFFSET_PHI_MAX];
		final float diskRadiusInner = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + CompiledShape3FCache.DISK_3_F_OFFSET_RADIUS_INNER];
		final float diskRadiusOuter = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + CompiledShape3FCache.DISK_3_F_OFFSET_RADIUS_OUTER];
		final float diskZMax = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + CompiledShape3FCache.DISK_3_F_OFFSET_Z_MAX];
		
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = diskZMax;
		
		final float distance = sqrt(surfaceIntersectionPointX * surfaceIntersectionPointX + surfaceIntersectionPointY * surfaceIntersectionPointY);
		
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, PI_MULTIPLIED_BY_2) / diskPhiMax;
		final float textureCoordinatesV = (diskRadiusOuter - distance) / (diskRadiusOuter - diskRadiusInner);
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromUV(-diskPhiMax * surfaceIntersectionPointY, +diskPhiMax * surfaceIntersectionPointX, 0.0F, surfaceIntersectionPointX * (diskRadiusInner - diskRadiusOuter) / distance, surfaceIntersectionPointY * (diskRadiusInner - diskRadiusOuter) / distance, 0.0F);
		
//		Update the intersection array:
		intersectionLHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionLHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionLHSSetPrimitiveIndex(primitiveIndex);
		intersectionLHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionLHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the disk at offset {@code shape3FDisk3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the disk
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FDisk3FArrayOffset the offset in {@link #shape3FDisk3FArray}
	 */
	protected final void shape3FDisk3FIntersectionComputeRHS(final float t, final int primitiveIndex, final int shape3FDisk3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		
//		Retrieve the disk variables that will be referred to by 'diskPhiMax', 'diskRadiusInner', 'diskRadiusOuter' and 'diskZMax' in the comments:
		final float diskPhiMax = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + CompiledShape3FCache.DISK_3_F_OFFSET_PHI_MAX];
		final float diskRadiusInner = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + CompiledShape3FCache.DISK_3_F_OFFSET_RADIUS_INNER];
		final float diskRadiusOuter = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + CompiledShape3FCache.DISK_3_F_OFFSET_RADIUS_OUTER];
		final float diskZMax = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + CompiledShape3FCache.DISK_3_F_OFFSET_Z_MAX];
		
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = diskZMax;
		
		final float distance = sqrt(surfaceIntersectionPointX * surfaceIntersectionPointX + surfaceIntersectionPointY * surfaceIntersectionPointY);
		
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, PI_MULTIPLIED_BY_2) / diskPhiMax;
		final float textureCoordinatesV = (diskRadiusOuter - distance) / (diskRadiusOuter - diskRadiusInner);
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromUV(-diskPhiMax * surfaceIntersectionPointY, +diskPhiMax * surfaceIntersectionPointX, 0.0F, surfaceIntersectionPointX * (diskRadiusInner - diskRadiusOuter) / distance, surfaceIntersectionPointY * (diskRadiusInner - diskRadiusOuter) / distance, 0.0F);
		
//		Update the intersection array:
		intersectionRHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionRHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionRHSSetPrimitiveIndex(primitiveIndex);
		intersectionRHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionRHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Shape3F - Hyperboloid3F /////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given hyperboloid in object space, {@code false} otherwise.
	 * 
	 * @param shape3FHyperboloid3FArrayOffset the offset for the disk in {@link #shape3FHyperboloid3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given hyperboloid in object space, {@code false} otherwise
	 */
	protected final boolean shape3FHyperboloid3FIntersects(final int shape3FHyperboloid3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return shape3FHyperboloid3FIntersectionT(shape3FHyperboloid3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns the parametric T value for a given hyperboloid in object space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @param shape3FHyperboloid3FArrayOffset the offset for the disk in {@link #shape3FHyperboloid3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return the parametric T value for a given hyperboloid in object space, or {@code 0.0F} if no intersection was found
	 */
	protected final float shape3FHyperboloid3FIntersectionT(final int shape3FHyperboloid3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the hyperboloid variables that will be referred to by 'hyperboloidPhiMax', 'hyperboloidA', 'hyperboloidB', 'hyperboloidAH', 'hyperboloidCH', 'hyperboloidZMax' and 'hyperboloidZMin' in the comments:
		final float hyperboloidPhiMax = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_PHI_MAX];
		final float hyperboloidAX = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_A + 0];
		final float hyperboloidAY = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_A + 1];
		final float hyperboloidAZ = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_A + 2];
		final float hyperboloidBX = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_B + 0];
		final float hyperboloidBY = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_B + 1];
		final float hyperboloidBZ = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_B + 2];
		final float hyperboloidAH = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_A_H];
		final float hyperboloidCH = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_C_H];
		final float hyperboloidZMax = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_Z_MAX];
		final float hyperboloidZMin = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_Z_MIN];
		
		final float a = hyperboloidAH * rayDirectionX * rayDirectionX + hyperboloidAH * rayDirectionY * rayDirectionY - hyperboloidCH * rayDirectionZ * rayDirectionZ;
		final float b = 2.0F * (hyperboloidAH * rayDirectionX * rayOriginX + hyperboloidAH * rayDirectionY * rayOriginY - hyperboloidCH * rayDirectionZ * rayOriginZ);
		final float c = hyperboloidAH * rayOriginX * rayOriginX + hyperboloidAH * rayOriginY * rayOriginY - hyperboloidCH * rayOriginZ * rayOriginZ - 1.0F;
		
		solveQuadraticSystemToArray(a, b, c, rayTMinimum, rayTMaximum);
		
		final float tMinimum = solveQuadraticSystemToArrayGetMinimum();
		final float tMaximum = solveQuadraticSystemToArrayGetMaximum();
		
		if(tMinimum == 0.0F) {
			return 0.0F;
		}
		
		final float xMinimum = rayOriginX + rayDirectionX * tMinimum;
		final float yMinimum = rayOriginY + rayDirectionY * tMinimum;
		final float zMinimum = rayOriginZ + rayDirectionZ * tMinimum;
		final float vMinimum = (zMinimum - hyperboloidAZ) / (hyperboloidBZ - hyperboloidAZ);
		final float xMinimumR = (1.0F - vMinimum) * hyperboloidAX + vMinimum * hyperboloidBX;
		final float yMinimumR = (1.0F - vMinimum) * hyperboloidAY + vMinimum * hyperboloidBY;
		
		final float phiMinimum = addIfLessThanThreshold(atan2(yMinimum * xMinimumR - xMinimum * yMinimumR, xMinimum * xMinimumR + yMinimum * yMinimumR), 0.0F, PI_MULTIPLIED_BY_2);
		
		if(zMinimum < hyperboloidZMin || zMinimum > hyperboloidZMax || phiMinimum > hyperboloidPhiMax) {
			if(tMaximum == 0.0F) {
				return 0.0F;
			}
			
			final float xMaximum = rayOriginX + rayDirectionX * tMaximum;
			final float yMaximum = rayOriginY + rayDirectionY * tMaximum;
			final float zMaximum = rayOriginZ + rayDirectionZ * tMaximum;
			final float vMaximum = (zMaximum - hyperboloidAZ) / (hyperboloidBZ - hyperboloidAZ);
			final float xMaximumR = (1.0F - vMaximum) * hyperboloidAX + vMaximum * hyperboloidBX;
			final float yMaximumR = (1.0F - vMaximum) * hyperboloidAY + vMaximum * hyperboloidBY;
			
			final float phiMaximum = addIfLessThanThreshold(atan2(yMaximum * xMaximumR - xMaximum * yMaximumR, xMaximum * xMaximumR + yMaximum * yMaximumR), 0.0F, PI_MULTIPLIED_BY_2);
			
			if(zMaximum < hyperboloidZMin || zMaximum > hyperboloidZMax || phiMaximum > hyperboloidPhiMax) {
				return 0.0F;
			}
			
			return tMaximum;
		}
		
		return tMinimum;
	}
	
	/**
	 * Computes the intersection properties for the hyperboloid at offset {@code shape3FHyperboloid3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the hyperboloid
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FHyperboloid3FArrayOffset the offset in {@link #shape3FHyperboloid3FArray}
	 */
	protected final void shape3FHyperboloid3FIntersectionComputeLHS(final float t, final int primitiveIndex, final int shape3FHyperboloid3FArrayOffset) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the hyperboloid variables that will be referred to by 'hyperboloidPhiMax', 'hyperboloidA' and 'hyperboloidB' in the comments:
		final float hyperboloidPhiMax = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_PHI_MAX];
		final float hyperboloidAX = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_A + 0];
		final float hyperboloidAY = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_A + 1];
		final float hyperboloidAZ = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_A + 2];
		final float hyperboloidBX = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_B + 0];
		final float hyperboloidBY = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_B + 1];
		final float hyperboloidBZ = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_B + 2];
		
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
		final float phi = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, PI_MULTIPLIED_BY_2);
		
		final float cosPhi = cos(phi);
		final float sinPhi = sin(phi);
		
		final float textureCoordinatesU = phi / hyperboloidPhiMax;
		final float textureCoordinatesV = ((rayOriginZ + rayDirectionZ * t) - hyperboloidAZ) / (hyperboloidBZ - hyperboloidAZ);
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromUV(-hyperboloidPhiMax * surfaceIntersectionPointY, +hyperboloidPhiMax * surfaceIntersectionPointX, 0.0F, (hyperboloidBX - hyperboloidAX) * cosPhi - (hyperboloidBY - hyperboloidAY) * sinPhi, (hyperboloidBX - hyperboloidAX) * sinPhi + (hyperboloidBY - hyperboloidAY) * cosPhi, hyperboloidBZ - hyperboloidAZ);
		
//		Update the intersection array:
		intersectionLHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionLHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionLHSSetPrimitiveIndex(primitiveIndex);
		intersectionLHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionLHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the hyperboloid at offset {@code shape3FHyperboloid3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the hyperboloid
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FHyperboloid3FArrayOffset the offset in {@link #shape3FHyperboloid3FArray}
	 */
	protected final void shape3FHyperboloid3FIntersectionComputeRHS(final float t, final int primitiveIndex, final int shape3FHyperboloid3FArrayOffset) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the hyperboloid variables that will be referred to by 'hyperboloidPhiMax', 'hyperboloidA' and 'hyperboloidB' in the comments:
		final float hyperboloidPhiMax = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_PHI_MAX];
		final float hyperboloidAX = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_A + 0];
		final float hyperboloidAY = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_A + 1];
		final float hyperboloidAZ = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_A + 2];
		final float hyperboloidBX = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_B + 0];
		final float hyperboloidBY = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_B + 1];
		final float hyperboloidBZ = this.shape3FHyperboloid3FArray[shape3FHyperboloid3FArrayOffset + CompiledShape3FCache.HYPERBOLOID_3_F_OFFSET_POINT_B + 2];
		
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
		final float phi = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, PI_MULTIPLIED_BY_2);
		
		final float cosPhi = cos(phi);
		final float sinPhi = sin(phi);
		
		final float textureCoordinatesU = phi / hyperboloidPhiMax;
		final float textureCoordinatesV = ((rayOriginZ + rayDirectionZ * t) - hyperboloidAZ) / (hyperboloidBZ - hyperboloidAZ);
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromUV(-hyperboloidPhiMax * surfaceIntersectionPointY, +hyperboloidPhiMax * surfaceIntersectionPointX, 0.0F, (hyperboloidBX - hyperboloidAX) * cosPhi - (hyperboloidBY - hyperboloidAY) * sinPhi, (hyperboloidBX - hyperboloidAX) * sinPhi + (hyperboloidBY - hyperboloidAY) * cosPhi, hyperboloidBZ - hyperboloidAZ);
		
//		Update the intersection array:
		intersectionRHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionRHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionRHSSetPrimitiveIndex(primitiveIndex);
		intersectionRHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionRHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Shape3F - Paraboloid3F //////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given paraboloid in object space, {@code false} otherwise.
	 * 
	 * @param shape3FParaboloid3FArrayOffset the offset for the plane in {@link #shape3FParaboloid3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given paraboloid in object space, {@code false} otherwise
	 */
	protected final boolean shape3FParaboloid3FIntersects(final int shape3FParaboloid3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return shape3FParaboloid3FIntersectionT(shape3FParaboloid3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns the parametric T value for a given paraboloid in object space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @param shape3FParaboloid3FArrayOffset the offset for the plane in {@link #shape3FParaboloid3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return the parametric T value for a given paraboloid in object space, or {@code 0.0F} if no intersection was found
	 */
	protected final float shape3FParaboloid3FIntersectionT(final int shape3FParaboloid3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the paraboloid variables that will be referred to by 'paraboloidPhiMax', 'paraboloidRadius', 'paraboloidZMax' and 'paraboloidZMin' in the comments:
		final float paraboloidPhiMax = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + CompiledShape3FCache.PARABOLOID_3_F_OFFSET_PHI_MAX];
		final float paraboloidRadius = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + CompiledShape3FCache.PARABOLOID_3_F_OFFSET_RADIUS];
		final float paraboloidZMax = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + CompiledShape3FCache.PARABOLOID_3_F_OFFSET_Z_MAX];
		final float paraboloidZMin = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + CompiledShape3FCache.PARABOLOID_3_F_OFFSET_Z_MIN];
		
		final float k = paraboloidZMax / (paraboloidRadius * paraboloidRadius);
		
		final float a = k * (rayDirectionX * rayDirectionX + rayDirectionY * rayDirectionY);
		final float b = 2.0F * k * (rayDirectionX * rayOriginX + rayDirectionY * rayOriginY) - rayDirectionZ;
		final float c = k * (rayOriginX * rayOriginX + rayOriginY * rayOriginY) - rayOriginZ;
		
		solveQuadraticSystemToArray(a, b, c, rayTMinimum, rayTMaximum);
		
		final float tMinimum = solveQuadraticSystemToArrayGetMinimum();
		final float tMaximum = solveQuadraticSystemToArrayGetMaximum();
		
		if(tMinimum == 0.0F) {
			return 0.0F;
		}
		
		final float xMinimum = rayOriginX + rayDirectionX * tMinimum;
		final float yMinimum = rayOriginY + rayDirectionY * tMinimum;
		final float zMinimum = rayOriginZ + rayDirectionZ * tMinimum;
		
		final float phiMinimum = addIfLessThanThreshold(atan2(yMinimum, xMinimum), 0.0F, PI_MULTIPLIED_BY_2);
		
		if(zMinimum < paraboloidZMin || zMinimum > paraboloidZMax || phiMinimum > paraboloidPhiMax) {
			if(tMaximum == 0.0F) {
				return 0.0F;
			}
			
			final float xMaximum = rayOriginX + rayDirectionX * tMaximum;
			final float yMaximum = rayOriginY + rayDirectionY * tMaximum;
			final float zMaximum = rayOriginZ + rayDirectionZ * tMaximum;
			
			final float phiMaximum = addIfLessThanThreshold(atan2(yMaximum, xMaximum), 0.0F, PI_MULTIPLIED_BY_2);
			
			if(zMaximum < paraboloidZMin || zMaximum > paraboloidZMax || phiMaximum > paraboloidPhiMax) {
				return 0.0F;
			}
			
			return tMaximum;
		}
		
		return tMinimum;
	}
	
	/**
	 * Computes the intersection properties for the paraboloid at offset {@code shape3FParaboloid3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the paraboloid
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FParaboloid3FArrayOffset the offset in {@link #shape3FParaboloid3FArray}
	 */
	protected final void shape3FParaboloid3FIntersectionComputeLHS(final float t, final int primitiveIndex, final int shape3FParaboloid3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the paraboloid variables that will be referred to by 'paraboloidPhiMax', 'paraboloidZMax' and 'paraboloidZMin' in the comments:
		final float paraboloidPhiMax = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + CompiledShape3FCache.PARABOLOID_3_F_OFFSET_PHI_MAX];
		final float paraboloidZMax = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + CompiledShape3FCache.PARABOLOID_3_F_OFFSET_Z_MAX];
		final float paraboloidZMin = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + CompiledShape3FCache.PARABOLOID_3_F_OFFSET_Z_MIN];
		
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, PI_MULTIPLIED_BY_2) / paraboloidPhiMax;
		final float textureCoordinatesV = (surfaceIntersectionPointZ - paraboloidZMin) / (paraboloidZMax - paraboloidZMin);
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromUV(-paraboloidPhiMax * surfaceIntersectionPointY, +paraboloidPhiMax * surfaceIntersectionPointX, 0.0F, (paraboloidZMax - paraboloidZMin) * (surfaceIntersectionPointX / (2.0F * surfaceIntersectionPointZ)), (paraboloidZMax - paraboloidZMin) * (surfaceIntersectionPointY / (2.0F * surfaceIntersectionPointZ)), paraboloidZMax - paraboloidZMin);
		
//		Update the intersection array:
		intersectionLHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionLHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionLHSSetPrimitiveIndex(primitiveIndex);
		intersectionLHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionLHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the paraboloid at offset {@code shape3FParaboloid3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the paraboloid
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FParaboloid3FArrayOffset the offset in {@link #shape3FParaboloid3FArray}
	 */
	protected final void shape3FParaboloid3FIntersectionComputeRHS(final float t, final int primitiveIndex, final int shape3FParaboloid3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the paraboloid variables that will be referred to by 'paraboloidPhiMax', 'paraboloidZMax' and 'paraboloidZMin' in the comments:
		final float paraboloidPhiMax = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + CompiledShape3FCache.PARABOLOID_3_F_OFFSET_PHI_MAX];
		final float paraboloidZMax = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + CompiledShape3FCache.PARABOLOID_3_F_OFFSET_Z_MAX];
		final float paraboloidZMin = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + CompiledShape3FCache.PARABOLOID_3_F_OFFSET_Z_MIN];
		
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, PI_MULTIPLIED_BY_2) / paraboloidPhiMax;
		final float textureCoordinatesV = (surfaceIntersectionPointZ - paraboloidZMin) / (paraboloidZMax - paraboloidZMin);
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromUV(-paraboloidPhiMax * surfaceIntersectionPointY, +paraboloidPhiMax * surfaceIntersectionPointX, 0.0F, (paraboloidZMax - paraboloidZMin) * (surfaceIntersectionPointX / (2.0F * surfaceIntersectionPointZ)), (paraboloidZMax - paraboloidZMin) * (surfaceIntersectionPointY / (2.0F * surfaceIntersectionPointZ)), paraboloidZMax - paraboloidZMin);
		
//		Update the intersection array:
		intersectionRHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionRHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionRHSSetPrimitiveIndex(primitiveIndex);
		intersectionRHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionRHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Shape3F - Plane3F ///////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given plane in object space, {@code false} otherwise.
	 * 
	 * @param shape3FPlane3FArrayOffset the offset for the plane in {@link #shape3FPlane3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given plane in object space, {@code false} otherwise
	 */
	protected final boolean shape3FPlane3FIntersects(final int shape3FPlane3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return shape3FPlane3FIntersectionT(shape3FPlane3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns the parametric T value for a given plane in object space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @param shape3FPlane3FArrayOffset the offset for the plane in {@link #shape3FPlane3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return the parametric T value for a given plane in object space, or {@code 0.0F} if no intersection was found
	 */
	protected final float shape3FPlane3FIntersectionT(final int shape3FPlane3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the plane variables that will be referred to by 'planeA' and 'planeSurfaceNormal' in the comments:
		final float planeAX = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_A + 0];
		final float planeAY = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_A + 1];
		final float planeAZ = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_A + 2];
		final float planeSurfaceNormalX = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_SURFACE_NORMAL + 0];
		final float planeSurfaceNormalY = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_SURFACE_NORMAL + 1];
		final float planeSurfaceNormalZ = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_SURFACE_NORMAL + 2];
		
//		Compute the determinant, which is the dot product between 'planeSurfaceNormal' and 'rayDirection':
		final float determinant = planeSurfaceNormalX * rayDirectionX + planeSurfaceNormalY * rayDirectionY + planeSurfaceNormalZ * rayDirectionZ;
		
//		Check if the determinant is close to 0.0 and, if that is the case, return a miss:
		if(determinant >= -0.0001F && determinant <= +0.0001F) {
			return 0.0F;
		}
		
//		Compute the direction from 'rayOrigin' to 'planeA', denoted by 'rayOriginToPlaneA' in the comments:
		final float rayOriginToPlaneAX = planeAX - rayOriginX;
		final float rayOriginToPlaneAY = planeAY - rayOriginY;
		final float rayOriginToPlaneAZ = planeAZ - rayOriginZ;
		
//		Compute the intersection as the dot product between 'rayOriginToPlaneA' and 'planeSurfaceNormal' followed by a division with the determinant:
		final float intersectionT = (rayOriginToPlaneAX * planeSurfaceNormalX + rayOriginToPlaneAY * planeSurfaceNormalY + rayOriginToPlaneAZ * planeSurfaceNormalZ) / determinant;
		
		if(intersectionT > rayTMinimum && intersectionT < rayTMaximum) {
			return intersectionT;
		}
		
		return 0.0F;
	}
	
	/**
	 * Computes the intersection properties for the plane at offset {@code shape3FPlane3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the plane
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FPlane3FArrayOffset the offset in {@link #shape3FPlane3FArray}
	 */
	protected final void shape3FPlane3FIntersectionComputeLHS(final float t, final int primitiveIndex, final int shape3FPlane3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the plane variables:
		final float planeAX = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_A + 0];
		final float planeAY = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_A + 1];
		final float planeAZ = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_A + 2];
		final float planeBX = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_B + 0];
		final float planeBY = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_B + 1];
		final float planeBZ = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_B + 2];
		final float planeCX = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_C + 0];
		final float planeCY = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_C + 1];
		final float planeCZ = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_C + 2];
		final float planeSurfaceNormalX = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_SURFACE_NORMAL + 0];
		final float planeSurfaceNormalY = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_SURFACE_NORMAL + 1];
		final float planeSurfaceNormalZ = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_SURFACE_NORMAL + 2];
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute variables necessary for computing the texture coordinates:
		final boolean isXLarger = abs(planeSurfaceNormalX) > abs(planeSurfaceNormalY) && abs(planeSurfaceNormalX) > abs(planeSurfaceNormalZ);
		final boolean isYLarger = abs(planeSurfaceNormalY) > abs(planeSurfaceNormalZ);
		
//		Compute variables necessary for computing the texture coordinates:
		final float aX = isXLarger ? planeAY      : isYLarger ? planeAZ      : planeAX;
		final float aY = isXLarger ? planeAZ      : isYLarger ? planeAX      : planeAY;
		final float bX = isXLarger ? planeCY - aX : isYLarger ? planeCZ - aX : planeCX - aX;
		final float bY = isXLarger ? planeCZ - aY : isYLarger ? planeCX - aY : planeCY - aY;
		final float cX = isXLarger ? planeBY - aX : isYLarger ? planeBZ - aX : planeBX - aX;
		final float cY = isXLarger ? planeBZ - aY : isYLarger ? planeBX - aY : planeBY - aY;
		
//		Compute variables necessary for computing the texture coordinates:
		final float determinantReciprocal = 1.0F / (bX * cY - bY * cX);
		
//		Compute variables necessary for computing the texture coordinates:
		final float u = isXLarger ? surfaceIntersectionPointY : isYLarger ? surfaceIntersectionPointZ : surfaceIntersectionPointX;
		final float v = isXLarger ? surfaceIntersectionPointZ : isYLarger ? surfaceIntersectionPointX : surfaceIntersectionPointY;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = u * (-bY * determinantReciprocal) + v * (+bX * determinantReciprocal) + (bY * aX - bX * aY) * determinantReciprocal;
		final float textureCoordinatesV = u * (+cY * determinantReciprocal) + v * (-cX * determinantReciprocal) + (cX * aY - cY * aX) * determinantReciprocal;
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromW(planeSurfaceNormalX, planeSurfaceNormalY, planeSurfaceNormalZ);
		
//		Update the intersection array:
		intersectionLHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionLHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionLHSSetPrimitiveIndex(primitiveIndex);
		intersectionLHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionLHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the plane at offset {@code shape3FPlane3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the plane
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FPlane3FArrayOffset the offset in {@link #shape3FPlane3FArray}
	 */
	protected final void shape3FPlane3FIntersectionComputeRHS(final float t, final int primitiveIndex, final int shape3FPlane3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the plane variables:
		final float planeAX = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_A + 0];
		final float planeAY = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_A + 1];
		final float planeAZ = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_A + 2];
		final float planeBX = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_B + 0];
		final float planeBY = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_B + 1];
		final float planeBZ = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_B + 2];
		final float planeCX = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_C + 0];
		final float planeCY = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_C + 1];
		final float planeCZ = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_C + 2];
		final float planeSurfaceNormalX = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_SURFACE_NORMAL + 0];
		final float planeSurfaceNormalY = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_SURFACE_NORMAL + 1];
		final float planeSurfaceNormalZ = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + CompiledShape3FCache.PLANE_3_F_OFFSET_SURFACE_NORMAL + 2];
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute variables necessary for computing the texture coordinates:
		final boolean isXLarger = abs(planeSurfaceNormalX) > abs(planeSurfaceNormalY) && abs(planeSurfaceNormalX) > abs(planeSurfaceNormalZ);
		final boolean isYLarger = abs(planeSurfaceNormalY) > abs(planeSurfaceNormalZ);
		
//		Compute variables necessary for computing the texture coordinates:
		final float aX = isXLarger ? planeAY      : isYLarger ? planeAZ      : planeAX;
		final float aY = isXLarger ? planeAZ      : isYLarger ? planeAX      : planeAY;
		final float bX = isXLarger ? planeCY - aX : isYLarger ? planeCZ - aX : planeCX - aX;
		final float bY = isXLarger ? planeCZ - aY : isYLarger ? planeCX - aY : planeCY - aY;
		final float cX = isXLarger ? planeBY - aX : isYLarger ? planeBZ - aX : planeBX - aX;
		final float cY = isXLarger ? planeBZ - aY : isYLarger ? planeBX - aY : planeBY - aY;
		
//		Compute variables necessary for computing the texture coordinates:
		final float determinantReciprocal = 1.0F / (bX * cY - bY * cX);
		
//		Compute variables necessary for computing the texture coordinates:
		final float u = isXLarger ? surfaceIntersectionPointY : isYLarger ? surfaceIntersectionPointZ : surfaceIntersectionPointX;
		final float v = isXLarger ? surfaceIntersectionPointZ : isYLarger ? surfaceIntersectionPointX : surfaceIntersectionPointY;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = u * (-bY * determinantReciprocal) + v * (+bX * determinantReciprocal) + (bY * aX - bX * aY) * determinantReciprocal;
		final float textureCoordinatesV = u * (+cY * determinantReciprocal) + v * (-cX * determinantReciprocal) + (cX * aY - cY * aX) * determinantReciprocal;
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromW(planeSurfaceNormalX, planeSurfaceNormalY, planeSurfaceNormalZ);
		
//		Update the intersection array:
		intersectionRHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionRHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionRHSSetPrimitiveIndex(primitiveIndex);
		intersectionRHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionRHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Shape3F - Polygon3F /////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given polygon in object space, {@code false} otherwise.
	 * 
	 * @param shape3FPolygon3FArrayOffset the offset for the polygon in {@link #shape3FPolygon3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given polygon in object space, {@code false} otherwise
	 */
	protected final boolean shape3FPolygon3FIntersects(final int shape3FPolygon3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return shape3FPolygon3FIntersectionT(shape3FPolygon3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns the parametric T value for a given polygon in object space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @param shape3FPolygon3FArrayOffset the offset for the polygon in {@link #shape3FPolygon3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return the parametric T value for a given polygon in object space, or {@code 0.0F} if no intersection was found
	 */
	protected final float shape3FPolygon3FIntersectionT(final int shape3FPolygon3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the polygon variables that will be referred to by 'polygonA', 'polygonB' and 'polygonSurfaceNormal' in the comments:
		final float polygonAX = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_0 + 0];
		final float polygonAY = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_0 + 1];
		final float polygonAZ = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_0 + 2];
		final float polygonBX = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_1 + 0];
		final float polygonBY = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_1 + 1];
		final float polygonBZ = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_1 + 2];
		final float polygonSurfaceNormalX = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_SURFACE_NORMAL + 0];
		final float polygonSurfaceNormalY = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_SURFACE_NORMAL + 1];
		final float polygonSurfaceNormalZ = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_SURFACE_NORMAL + 2];
		
//		Compute the determinant, which is the dot product between 'polygonSurfaceNormal' and 'rayDirection':
		final float determinant = polygonSurfaceNormalX * rayDirectionX + polygonSurfaceNormalY * rayDirectionY + polygonSurfaceNormalZ * rayDirectionZ;
		
//		Check if the determinant is close to 0.0 and, if that is the case, return a miss:
		if(determinant >= -0.0001F && determinant <= +0.0001F) {
			return 0.0F;
		}
		
//		Compute the direction from 'rayOrigin' to 'polygonA', denoted by 'rayOriginToPolygonA' in the comments:
		final float rayOriginToPolygonAX = polygonAX - rayOriginX;
		final float rayOriginToPolygonAY = polygonAY - rayOriginY;
		final float rayOriginToPolygonAZ = polygonAZ - rayOriginZ;
		
//		Compute the intersection as the dot product between 'rayOriginToPolygonA' and 'polygonSurfaceNormal' followed by a division with the determinant:
		final float intersectionT = (rayOriginToPolygonAX * polygonSurfaceNormalX + rayOriginToPolygonAY * polygonSurfaceNormalY + rayOriginToPolygonAZ * polygonSurfaceNormalZ) / determinant;
		
		if(intersectionT <= rayTMinimum || intersectionT >= rayTMaximum) {
			return 0.0F;
		}
		
//		Compute the surface intersection point, denoted by 'surfaceIntersectionPoint' in the comments:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * intersectionT;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * intersectionT;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * intersectionT;
		
//		Set the orthonormal basis W-direction to 'polygonSurfaceNormal', denoted by 'orthonormalBasisW' in the comments:
		final float orthonormalBasisWNormalizedX = polygonSurfaceNormalX;
		final float orthonormalBasisWNormalizedY = polygonSurfaceNormalY;
		final float orthonormalBasisWNormalizedZ = polygonSurfaceNormalZ;
		
//		Compute the orthonormal basis U-direction as the normalized direction from 'polygonA' to 'polygonB', denoted by 'orthonormalBasisU' in the comments:
		final float orthonormalBasisUX = polygonBX - polygonAX;
		final float orthonormalBasisUY = polygonBY - polygonAY;
		final float orthonormalBasisUZ = polygonBZ - polygonAZ;
		final float orthonormalBasisULengthReciprocal = vector3FLengthReciprocal(orthonormalBasisUX, orthonormalBasisUY, orthonormalBasisUZ);
		final float orthonormalBasisUNormalizedX = orthonormalBasisUX * orthonormalBasisULengthReciprocal;
		final float orthonormalBasisUNormalizedY = orthonormalBasisUY * orthonormalBasisULengthReciprocal;
		final float orthonormalBasisUNormalizedZ = orthonormalBasisUZ * orthonormalBasisULengthReciprocal;
		
//		Compute the orthonormal basis V-direction as the cross product between 'orthonormalBasisW' and 'orthonormalBasisU', denoted by 'orthonormalBasisV' in the comments:
		final float orthonormalBasisVNormalizedX = orthonormalBasisWNormalizedY * orthonormalBasisUNormalizedZ - orthonormalBasisWNormalizedZ * orthonormalBasisUNormalizedY;
		final float orthonormalBasisVNormalizedY = orthonormalBasisWNormalizedZ * orthonormalBasisUNormalizedX - orthonormalBasisWNormalizedX * orthonormalBasisUNormalizedZ;
		final float orthonormalBasisVNormalizedZ = orthonormalBasisWNormalizedX * orthonormalBasisUNormalizedY - orthonormalBasisWNormalizedY * orthonormalBasisUNormalizedX;
		
//		Compute the direction from 'polygonA' to 'surfaceIntersectionPoint', denoted by 'polygonAToSurfaceIntersectionPoint' in the comments:
		final float polygonAToSurfaceIntersectionPointX = surfaceIntersectionPointX - polygonAX;
		final float polygonAToSurfaceIntersectionPointY = surfaceIntersectionPointY - polygonAY;
		final float polygonAToSurfaceIntersectionPointZ = surfaceIntersectionPointZ - polygonAZ;
		
//		Compute the 2D-projected surface intersection point, denoted by 'projectedSurfaceIntersectionPoint' in the comments:
		final float projectedSurfaceIntersectionPointX = vector3FDotProduct(polygonAToSurfaceIntersectionPointX, polygonAToSurfaceIntersectionPointY, polygonAToSurfaceIntersectionPointZ, orthonormalBasisUNormalizedX, orthonormalBasisUNormalizedY, orthonormalBasisUNormalizedZ);
		final float projectedSurfaceIntersectionPointY = vector3FDotProduct(polygonAToSurfaceIntersectionPointX, polygonAToSurfaceIntersectionPointY, polygonAToSurfaceIntersectionPointZ, orthonormalBasisVNormalizedX, orthonormalBasisVNormalizedY, orthonormalBasisVNormalizedZ);
		
//		Retrieve the polygon variable that will be referred to by 'polygonPointCount' in the comments:
		final int polygonPointCount = (int)(this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_2_F_COUNT + 0]);
		
		boolean isInside = false;
		
		for(int i = 0, j = polygonPointCount - 1; i < polygonPointCount; j = i, i++) {
			final float pointIX = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_2_F + i * 2 + 0];
			final float pointIY = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_2_F + i * 2 + 1];
			final float pointJX = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_2_F + j * 2 + 0];
			final float pointJY = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_2_F + j * 2 + 1];
			
			if(shape2FLine2FContains(pointIX, pointIY, pointJX, pointJY, projectedSurfaceIntersectionPointX, projectedSurfaceIntersectionPointY)) {
				return intersectionT;
			}
			
			if((pointIY > projectedSurfaceIntersectionPointY) != (pointJY > projectedSurfaceIntersectionPointY) && projectedSurfaceIntersectionPointX < (pointJX - pointIX) * (projectedSurfaceIntersectionPointY - pointIY) / (pointJY - pointIY) + pointIX) {
				isInside = !isInside;
			}
		}
		
		return isInside ? intersectionT : 0.0F;
	}
	
	/**
	 * Computes the intersection properties for the polygon at offset {@code shape3FPolygon3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the polygon
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FPolygon3FArrayOffset the offset in {@link #shape3FPolygon3FArray}
	 */
	protected final void shape3FPolygon3FIntersectionComputeLHS(final float t, final int primitiveIndex, final int shape3FPolygon3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the polygon variables:
		final float polygonAX = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_0 + 0];
		final float polygonAY = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_0 + 1];
		final float polygonAZ = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_0 + 2];
		final float polygonBX = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_1 + 0];
		final float polygonBY = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_1 + 1];
		final float polygonBZ = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_1 + 2];
		final float polygonCX = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_N + 0];
		final float polygonCY = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_N + 1];
		final float polygonCZ = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_N + 2];
		final float polygonSurfaceNormalX = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_SURFACE_NORMAL + 0];
		final float polygonSurfaceNormalY = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_SURFACE_NORMAL + 1];
		final float polygonSurfaceNormalZ = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_SURFACE_NORMAL + 2];
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute variables necessary for computing the texture coordinates:
		final boolean isXLarger = abs(polygonSurfaceNormalX) > abs(polygonSurfaceNormalY) && abs(polygonSurfaceNormalX) > abs(polygonSurfaceNormalZ);
		final boolean isYLarger = abs(polygonSurfaceNormalY) > abs(polygonSurfaceNormalZ);
		
//		Compute variables necessary for computing the texture coordinates:
		final float aX = isXLarger ? polygonAY      : isYLarger ? polygonAZ      : polygonAX;
		final float aY = isXLarger ? polygonAZ      : isYLarger ? polygonAX      : polygonAY;
		final float bX = isXLarger ? polygonCY - aX : isYLarger ? polygonCZ - aX : polygonCX - aX;
		final float bY = isXLarger ? polygonCZ - aY : isYLarger ? polygonCX - aY : polygonCY - aY;
		final float cX = isXLarger ? polygonBY - aX : isYLarger ? polygonBZ - aX : polygonBX - aX;
		final float cY = isXLarger ? polygonBZ - aY : isYLarger ? polygonBX - aY : polygonBY - aY;
		
//		Compute variables necessary for computing the texture coordinates:
		final float determinant = bX * cY - bY * cX;
		final float determinantReciprocal = 1.0F / determinant;
		
//		Compute variables necessary for computing the texture coordinates:
		final float u = isXLarger ? surfaceIntersectionPointY : isYLarger ? surfaceIntersectionPointZ : surfaceIntersectionPointX;
		final float v = isXLarger ? surfaceIntersectionPointZ : isYLarger ? surfaceIntersectionPointX : surfaceIntersectionPointY;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = u * (-bY * determinantReciprocal) + v * (+bX * determinantReciprocal) + (bY * aX - bX * aY) * determinantReciprocal;
		final float textureCoordinatesV = u * (+cY * determinantReciprocal) + v * (-cX * determinantReciprocal) + (cX * aY - cY * aX) * determinantReciprocal;
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromW(polygonSurfaceNormalX, polygonSurfaceNormalY, polygonSurfaceNormalZ);
		
//		Update the intersection array:
		intersectionLHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionLHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionLHSSetPrimitiveIndex(primitiveIndex);
		intersectionLHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionLHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the polygon at offset {@code shape3FPolygon3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the polygon
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FPolygon3FArrayOffset the offset in {@link #shape3FPolygon3FArray}
	 */
	protected final void shape3FPolygon3FIntersectionComputeRHS(final float t, final int primitiveIndex, final int shape3FPolygon3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the polygon variables:
		final float polygonAX = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_0 + 0];
		final float polygonAY = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_0 + 1];
		final float polygonAZ = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_0 + 2];
		final float polygonBX = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_1 + 0];
		final float polygonBY = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_1 + 1];
		final float polygonBZ = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_1 + 2];
		final float polygonCX = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_N + 0];
		final float polygonCY = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_N + 1];
		final float polygonCZ = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_POINT_3_F_N + 2];
		final float polygonSurfaceNormalX = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_SURFACE_NORMAL + 0];
		final float polygonSurfaceNormalY = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_SURFACE_NORMAL + 1];
		final float polygonSurfaceNormalZ = this.shape3FPolygon3FArray[shape3FPolygon3FArrayOffset + CompiledShape3FCache.POLYGON_3_F_OFFSET_SURFACE_NORMAL + 2];
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute variables necessary for computing the texture coordinates:
		final boolean isXLarger = abs(polygonSurfaceNormalX) > abs(polygonSurfaceNormalY) && abs(polygonSurfaceNormalX) > abs(polygonSurfaceNormalZ);
		final boolean isYLarger = abs(polygonSurfaceNormalY) > abs(polygonSurfaceNormalZ);
		
//		Compute variables necessary for computing the texture coordinates:
		final float aX = isXLarger ? polygonAY      : isYLarger ? polygonAZ      : polygonAX;
		final float aY = isXLarger ? polygonAZ      : isYLarger ? polygonAX      : polygonAY;
		final float bX = isXLarger ? polygonCY - aX : isYLarger ? polygonCZ - aX : polygonCX - aX;
		final float bY = isXLarger ? polygonCZ - aY : isYLarger ? polygonCX - aY : polygonCY - aY;
		final float cX = isXLarger ? polygonBY - aX : isYLarger ? polygonBZ - aX : polygonBX - aX;
		final float cY = isXLarger ? polygonBZ - aY : isYLarger ? polygonBX - aY : polygonBY - aY;
		
//		Compute variables necessary for computing the texture coordinates:
		final float determinant = bX * cY - bY * cX;
		final float determinantReciprocal = 1.0F / determinant;
		
//		Compute variables necessary for computing the texture coordinates:
		final float u = isXLarger ? surfaceIntersectionPointY : isYLarger ? surfaceIntersectionPointZ : surfaceIntersectionPointX;
		final float v = isXLarger ? surfaceIntersectionPointZ : isYLarger ? surfaceIntersectionPointX : surfaceIntersectionPointY;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = u * (-bY * determinantReciprocal) + v * (+bX * determinantReciprocal) + (bY * aX - bX * aY) * determinantReciprocal;
		final float textureCoordinatesV = u * (+cY * determinantReciprocal) + v * (-cX * determinantReciprocal) + (cX * aY - cY * aX) * determinantReciprocal;
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromW(polygonSurfaceNormalX, polygonSurfaceNormalY, polygonSurfaceNormalZ);
		
//		Update the intersection array:
		intersectionRHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionRHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionRHSSetPrimitiveIndex(primitiveIndex);
		intersectionRHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionRHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Shape3F - Rectangle3F ///////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given rectangle in object space, {@code false} otherwise.
	 * 
	 * @param shape3FRectangle3FArrayOffset the offset for the rectangle in {@link #shape3FRectangle3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given rectangle in object space, {@code false} otherwise
	 */
	protected final boolean shape3FRectangle3FIntersects(final int shape3FRectangle3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return shape3FRectangle3FIntersectionT(shape3FRectangle3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns the parametric T value for a given rectangle in object space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @param shape3FRectangle3FArrayOffset the offset for the rectangle in {@link #shape3FRectangle3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return the parametric T value for a given rectangle in object space, or {@code 0.0F} if no intersection was found
	 */
	protected final float shape3FRectangle3FIntersectionT(final int shape3FRectangle3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the rectangle variables that will be referred to by 'rectanglePosition', 'rectangleSideA', 'rectangleSideB' and 'rectangleSurfaceNormal' in the comments:
		final float rectanglePositionX = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_POSITION + 0];
		final float rectanglePositionY = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_POSITION + 1];
		final float rectanglePositionZ = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_POSITION + 2];
		final float rectangleSideAX = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_A + 0];
		final float rectangleSideAY = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_A + 1];
		final float rectangleSideAZ = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_A + 2];
		final float rectangleSideBX = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_B + 0];
		final float rectangleSideBY = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_B + 1];
		final float rectangleSideBZ = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_B + 2];
		final float rectangleSurfaceNormalX = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SURFACE_NORMAL + 0];
		final float rectangleSurfaceNormalY = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SURFACE_NORMAL + 1];
		final float rectangleSurfaceNormalZ = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SURFACE_NORMAL + 2];
		
//		Compute the determinant, which is the dot product between 'rectangleSurfaceNormal' and 'rayDirection':
		final float determinant = rectangleSurfaceNormalX * rayDirectionX + rectangleSurfaceNormalY * rayDirectionY + rectangleSurfaceNormalZ * rayDirectionZ;
		
//		Check if the determinant is close to 0.0 and, if that is the case, return a miss:
		if(determinant >= -0.0001F && determinant <= +0.0001F) {
			return 0.0F;
		}
		
//		Compute the direction from 'rayOrigin' to 'rectanglePosition', denoted by 'rayOriginToRectanglePosition' in the comments:
		final float rayOriginToRectanglePositionX = rectanglePositionX - rayOriginX;
		final float rayOriginToRectanglePositionY = rectanglePositionY - rayOriginY;
		final float rayOriginToRectanglePositionZ = rectanglePositionZ - rayOriginZ;
		
//		Compute the intersection as the dot product between 'rayOriginToRectanglePosition' and 'rectangleSurfaceNormal' followed by a division with the determinant:
		final float intersectionT = (rayOriginToRectanglePositionX * rectangleSurfaceNormalX + rayOriginToRectanglePositionY * rectangleSurfaceNormalY + rayOriginToRectanglePositionZ * rectangleSurfaceNormalZ) / determinant;
		
		if(intersectionT <= rayTMinimum || intersectionT >= rayTMaximum) {
			return 0.0F;
		}
		
//		Compute the side lengths and their reciprocal values:
		final float rectangleSideALength = vector3FLength(rectangleSideAX, rectangleSideAY, rectangleSideAZ);
		final float rectangleSideALengthReciprocal = 1.0F / rectangleSideALength;
		final float rectangleSideBLength = vector3FLength(rectangleSideBX, rectangleSideBY, rectangleSideBZ);
		final float rectangleSideBLengthReciprocal = 1.0F / rectangleSideBLength;
		
//		Compute the normalized version of 'rectangleSideA':
		final float rectangleSideANormalizedX = rectangleSideAX * rectangleSideALengthReciprocal;
		final float rectangleSideANormalizedY = rectangleSideAY * rectangleSideALengthReciprocal;
		final float rectangleSideANormalizedZ = rectangleSideAZ * rectangleSideALengthReciprocal;
		
//		Compute the normalized version of 'rectangleSideB':
		final float rectangleSideBNormalizedX = rectangleSideBX * rectangleSideBLengthReciprocal;
		final float rectangleSideBNormalizedY = rectangleSideBY * rectangleSideBLengthReciprocal;
		final float rectangleSideBNormalizedZ = rectangleSideBZ * rectangleSideBLengthReciprocal;
		
//		Compute the surface intersection point that will be referred to by 'surfaceIntersectionPoint' in the comments:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * intersectionT;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * intersectionT;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * intersectionT;
		
//		Compute the direction from 'rectanglePosition' to 'surfaceIntersectionPoint':
		final float rectanglePositionToSurfaceIntersectionPointX = surfaceIntersectionPointX - rectanglePositionX;
		final float rectanglePositionToSurfaceIntersectionPointY = surfaceIntersectionPointY - rectanglePositionY;
		final float rectanglePositionToSurfaceIntersectionPointZ = surfaceIntersectionPointZ - rectanglePositionZ;
		
//		Compute the sides:
		final float sideX = vector3FDotProduct(rectanglePositionToSurfaceIntersectionPointX, rectanglePositionToSurfaceIntersectionPointY, rectanglePositionToSurfaceIntersectionPointZ, rectangleSideANormalizedX, rectangleSideANormalizedY, rectangleSideANormalizedZ);
		final float sideY = vector3FDotProduct(rectanglePositionToSurfaceIntersectionPointX, rectanglePositionToSurfaceIntersectionPointY, rectanglePositionToSurfaceIntersectionPointZ, rectangleSideBNormalizedX, rectangleSideBNormalizedY, rectangleSideBNormalizedZ);
		
		if(sideX < 0.0F || sideX > rectangleSideALength || sideY < 0.0F || sideY > rectangleSideBLength) {
			return 0.0F;
		}
		
		return intersectionT;
	}
	
	/**
	 * Computes the intersection properties for the rectangle at offset {@code shape3FRectangle3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the rectangle
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FRectangle3FArrayOffset the offset in {@link #shape3FRectangle3FArray}
	 */
	protected final void shape3FRectangle3FIntersectionComputeLHS(final float t, final int primitiveIndex, final int shape3FRectangle3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the rectangle variables that will be referred to by 'rectanglePosition', 'rectangleSideA', 'rectangleSideB' and 'rectangleSurfaceNormal' in the comments:
		final float rectanglePositionX = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_POSITION + 0];
		final float rectanglePositionY = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_POSITION + 1];
		final float rectanglePositionZ = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_POSITION + 2];
		final float rectangleSideAX = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_A + 0];
		final float rectangleSideAY = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_A + 1];
		final float rectangleSideAZ = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_A + 2];
		final float rectangleSideBX = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_B + 0];
		final float rectangleSideBY = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_B + 1];
		final float rectangleSideBZ = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_B + 2];
		final float rectangleSurfaceNormalX = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SURFACE_NORMAL + 0];
		final float rectangleSurfaceNormalY = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SURFACE_NORMAL + 1];
		final float rectangleSurfaceNormalZ = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SURFACE_NORMAL + 2];
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute variables necessary for computing the texture coordinates:
		final boolean isXLarger = abs(rectangleSurfaceNormalX) > abs(rectangleSurfaceNormalY) && abs(rectangleSurfaceNormalX) > abs(rectangleSurfaceNormalZ);
		final boolean isYLarger = abs(rectangleSurfaceNormalY) > abs(rectangleSurfaceNormalZ);
		
//		Compute three points:
		final float rectangleAX = rectanglePositionX;
		final float rectangleAY = rectanglePositionY;
		final float rectangleAZ = rectanglePositionZ;
		final float rectangleBX = rectangleAX + rectangleSideAX;
		final float rectangleBY = rectangleAY + rectangleSideAY;
		final float rectangleBZ = rectangleAZ + rectangleSideAZ;
		final float rectangleCX = rectangleAX + rectangleSideBX;
		final float rectangleCY = rectangleAY + rectangleSideBY;
		final float rectangleCZ = rectangleAZ + rectangleSideBZ;
		
//		Compute variables necessary for computing the texture coordinates:
		final float aX = isXLarger ? rectangleAY      : isYLarger ? rectangleAZ      : rectangleAX;
		final float aY = isXLarger ? rectangleAZ      : isYLarger ? rectangleAX      : rectangleAY;
		final float bX = isXLarger ? rectangleCY - aX : isYLarger ? rectangleCZ - aX : rectangleCX - aX;
		final float bY = isXLarger ? rectangleCZ - aY : isYLarger ? rectangleCX - aY : rectangleCY - aY;
		final float cX = isXLarger ? rectangleBY - aX : isYLarger ? rectangleBZ - aX : rectangleBX - aX;
		final float cY = isXLarger ? rectangleBZ - aY : isYLarger ? rectangleBX - aY : rectangleBY - aY;
		
//		Compute variables necessary for computing the texture coordinates:
		final float determinant = bX * cY - bY * cX;
		final float determinantReciprocal = 1.0F / determinant;
		
//		Compute variables necessary for computing the texture coordinates:
		final float u = isXLarger ? surfaceIntersectionPointY : isYLarger ? surfaceIntersectionPointZ : surfaceIntersectionPointX;
		final float v = isXLarger ? surfaceIntersectionPointZ : isYLarger ? surfaceIntersectionPointX : surfaceIntersectionPointY;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = u * (-bY * determinantReciprocal) + v * (+bX * determinantReciprocal) + (bY * aX - bX * aY) * determinantReciprocal;
		final float textureCoordinatesV = u * (+cY * determinantReciprocal) + v * (-cX * determinantReciprocal) + (cX * aY - cY * aX) * determinantReciprocal;
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromW(rectangleSurfaceNormalX, rectangleSurfaceNormalY, rectangleSurfaceNormalZ);
		
//		Update the intersection array:
		intersectionLHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionLHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionLHSSetPrimitiveIndex(primitiveIndex);
		intersectionLHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionLHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the rectangle at offset {@code shape3FRectangle3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the rectangle
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FRectangle3FArrayOffset the offset in {@link #shape3FRectangle3FArray}
	 */
	protected final void shape3FRectangle3FIntersectionComputeRHS(final float t, final int primitiveIndex, final int shape3FRectangle3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the rectangle variables that will be referred to by 'rectanglePosition', 'rectangleSideA', 'rectangleSideB' and 'rectangleSurfaceNormal' in the comments:
		final float rectanglePositionX = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_POSITION + 0];
		final float rectanglePositionY = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_POSITION + 1];
		final float rectanglePositionZ = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_POSITION + 2];
		final float rectangleSideAX = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_A + 0];
		final float rectangleSideAY = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_A + 1];
		final float rectangleSideAZ = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_A + 2];
		final float rectangleSideBX = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_B + 0];
		final float rectangleSideBY = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_B + 1];
		final float rectangleSideBZ = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SIDE_B + 2];
		final float rectangleSurfaceNormalX = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SURFACE_NORMAL + 0];
		final float rectangleSurfaceNormalY = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SURFACE_NORMAL + 1];
		final float rectangleSurfaceNormalZ = this.shape3FRectangle3FArray[shape3FRectangle3FArrayOffset + CompiledShape3FCache.RECTANGLE_3_F_OFFSET_SURFACE_NORMAL + 2];
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute variables necessary for computing the texture coordinates:
		final boolean isXLarger = abs(rectangleSurfaceNormalX) > abs(rectangleSurfaceNormalY) && abs(rectangleSurfaceNormalX) > abs(rectangleSurfaceNormalZ);
		final boolean isYLarger = abs(rectangleSurfaceNormalY) > abs(rectangleSurfaceNormalZ);
		
//		Compute three points:
		final float rectangleAX = rectanglePositionX;
		final float rectangleAY = rectanglePositionY;
		final float rectangleAZ = rectanglePositionZ;
		final float rectangleBX = rectangleAX + rectangleSideAX;
		final float rectangleBY = rectangleAY + rectangleSideAY;
		final float rectangleBZ = rectangleAZ + rectangleSideAZ;
		final float rectangleCX = rectangleAX + rectangleSideBX;
		final float rectangleCY = rectangleAY + rectangleSideBY;
		final float rectangleCZ = rectangleAZ + rectangleSideBZ;
		
//		Compute variables necessary for computing the texture coordinates:
		final float aX = isXLarger ? rectangleAY      : isYLarger ? rectangleAZ      : rectangleAX;
		final float aY = isXLarger ? rectangleAZ      : isYLarger ? rectangleAX      : rectangleAY;
		final float bX = isXLarger ? rectangleCY - aX : isYLarger ? rectangleCZ - aX : rectangleCX - aX;
		final float bY = isXLarger ? rectangleCZ - aY : isYLarger ? rectangleCX - aY : rectangleCY - aY;
		final float cX = isXLarger ? rectangleBY - aX : isYLarger ? rectangleBZ - aX : rectangleBX - aX;
		final float cY = isXLarger ? rectangleBZ - aY : isYLarger ? rectangleBX - aY : rectangleBY - aY;
		
//		Compute variables necessary for computing the texture coordinates:
		final float determinant = bX * cY - bY * cX;
		final float determinantReciprocal = 1.0F / determinant;
		
//		Compute variables necessary for computing the texture coordinates:
		final float u = isXLarger ? surfaceIntersectionPointY : isYLarger ? surfaceIntersectionPointZ : surfaceIntersectionPointX;
		final float v = isXLarger ? surfaceIntersectionPointZ : isYLarger ? surfaceIntersectionPointX : surfaceIntersectionPointY;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = u * (-bY * determinantReciprocal) + v * (+bX * determinantReciprocal) + (bY * aX - bX * aY) * determinantReciprocal;
		final float textureCoordinatesV = u * (+cY * determinantReciprocal) + v * (-cX * determinantReciprocal) + (cX * aY - cY * aX) * determinantReciprocal;
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromW(rectangleSurfaceNormalX, rectangleSurfaceNormalY, rectangleSurfaceNormalZ);
		
//		Update the intersection array:
		intersectionRHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionRHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionRHSSetPrimitiveIndex(primitiveIndex);
		intersectionRHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionRHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Shape3F - RectangularCuboid3F ///////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given rectangular cuboid in object space, {@code false} otherwise.
	 * 
	 * @param shape3FRectangularCuboid3FArrayOffset the offset for the rectangular cuboid in {@link #shape3FRectangularCuboid3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given rectangular cuboid in object space, {@code false} otherwise
	 */
	protected final boolean shape3FRectangularCuboid3FIntersects(final int shape3FRectangularCuboid3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return shape3FRectangularCuboid3FIntersectionT(shape3FRectangularCuboid3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns the parametric T value for a given rectangular cuboid in object space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @param shape3FRectangularCuboid3FArrayOffset the offset for the rectangular cuboid in {@link #shape3FRectangularCuboid3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return the parametric T value for a given rectangular cuboid in object space, or {@code 0.0F} if no intersection was found
	 */
	protected final float shape3FRectangularCuboid3FIntersectionT(final int shape3FRectangularCuboid3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionReciprocalX = ray3FGetDirectionReciprocalComponent1();
		final float rayDirectionReciprocalY = ray3FGetDirectionReciprocalComponent2();
		final float rayDirectionReciprocalZ = ray3FGetDirectionReciprocalComponent3();
		
//		Retrieve the rectangular cuboid variables:
		final float rectangularCuboidMaximumX = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MAXIMUM + 0];
		final float rectangularCuboidMaximumY = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MAXIMUM + 1];
		final float rectangularCuboidMaximumZ = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MAXIMUM + 2];
		final float rectangularCuboidMinimumX = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MINIMUM + 0];
		final float rectangularCuboidMinimumY = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MINIMUM + 1];
		final float rectangularCuboidMinimumZ = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MINIMUM + 2];
		
//		Compute the intersection:
		final float intersectionTMinimumX = (rectangularCuboidMinimumX - rayOriginX) * rayDirectionReciprocalX;
		final float intersectionTMinimumY = (rectangularCuboidMinimumY - rayOriginY) * rayDirectionReciprocalY;
		final float intersectionTMinimumZ = (rectangularCuboidMinimumZ - rayOriginZ) * rayDirectionReciprocalZ;
		final float intersectionTMaximumX = (rectangularCuboidMaximumX - rayOriginX) * rayDirectionReciprocalX;
		final float intersectionTMaximumY = (rectangularCuboidMaximumY - rayOriginY) * rayDirectionReciprocalY;
		final float intersectionTMaximumZ = (rectangularCuboidMaximumZ - rayOriginZ) * rayDirectionReciprocalZ;
		final float intersectionTMinimum = max(min(intersectionTMinimumX, intersectionTMaximumX), min(intersectionTMinimumY, intersectionTMaximumY), min(intersectionTMinimumZ, intersectionTMaximumZ));
		final float intersectionTMaximum = min(max(intersectionTMinimumX, intersectionTMaximumX), max(intersectionTMinimumY, intersectionTMaximumY), max(intersectionTMinimumZ, intersectionTMaximumZ));
		
		if(intersectionTMinimum > intersectionTMaximum) {
			return 0.0F;
		}
		
		if(intersectionTMinimum > rayTMinimum && intersectionTMinimum < rayTMaximum) {
			return intersectionTMinimum;
		}
		
		if(intersectionTMaximum > rayTMinimum && intersectionTMaximum < rayTMaximum) {
			return intersectionTMaximum;
		}
		
		return 0.0F;
	}
	
	/**
	 * Computes the intersection properties for the rectangular cuboid at offset {@code shape3FRectangularCuboid3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the rectangular cuboid
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FRectangularCuboid3FArrayOffset the offset in {@link #shape3FRectangularCuboid3FArray}
	 */
	protected final void shape3FRectangularCuboid3FIntersectionComputeLHS(final float t, final int primitiveIndex, final int shape3FRectangularCuboid3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the rectangular cuboid variables:
		final float rectangularCuboidMaximumX = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MAXIMUM + 0];
		final float rectangularCuboidMaximumY = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MAXIMUM + 1];
		final float rectangularCuboidMaximumZ = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MAXIMUM + 2];
		final float rectangularCuboidMinimumX = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MINIMUM + 0];
		final float rectangularCuboidMinimumY = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MINIMUM + 1];
		final float rectangularCuboidMinimumZ = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MINIMUM + 2];
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute the midpoint of the rectangular cuboid:
		final float midpointX = (rectangularCuboidMaximumX + rectangularCuboidMinimumX) * 0.5F;
		final float midpointY = (rectangularCuboidMaximumY + rectangularCuboidMinimumY) * 0.5F;
		final float midpointZ = (rectangularCuboidMaximumZ + rectangularCuboidMinimumZ) * 0.5F;
		
//		Compute half of the dimensions of the rectangular cuboid:
		final float halfDimensionX = (rectangularCuboidMaximumX - rectangularCuboidMinimumX) * 0.5F;
		final float halfDimensionY = (rectangularCuboidMaximumY - rectangularCuboidMinimumY) * 0.5F;
		final float halfDimensionZ = (rectangularCuboidMaximumZ - rectangularCuboidMinimumZ) * 0.5F;
		
//		Initialize an epsilon value:
		final float epsilon = 0.0001F;
		
//		Compute the face for each axis:
		final int faceX = surfaceIntersectionPointX < midpointX && surfaceIntersectionPointX + halfDimensionX - epsilon < midpointX ? -1 : surfaceIntersectionPointX > midpointX && surfaceIntersectionPointX - halfDimensionX + epsilon > midpointX ? 1 : 0;
		final int faceY = surfaceIntersectionPointY < midpointY && surfaceIntersectionPointY + halfDimensionY - epsilon < midpointY ? -1 : surfaceIntersectionPointY > midpointY && surfaceIntersectionPointY - halfDimensionY + epsilon > midpointY ? 1 : 0;
		final int faceZ = surfaceIntersectionPointZ < midpointZ && surfaceIntersectionPointZ + halfDimensionZ - epsilon < midpointZ ? -1 : surfaceIntersectionPointZ > midpointZ && surfaceIntersectionPointZ - halfDimensionZ + epsilon > midpointZ ? 1 : 0;
		
//		Compute the face to use:
		final int face = faceX == -1 ? 1 : faceX == 1 ? 2 : faceY == -1 ? 3 : faceY == 1 ? 4 : faceZ == -1 ? 5 : faceZ == 1 ? 6 : 0;
		
//		Compute the surface normal:
		final float surfaceNormalX = face == 1 ? -1.0F : face == 2 ? +1.0F : 0.0F;
		final float surfaceNormalY = face == 3 ? -1.0F : face == 4 ? +1.0F : 0.0F;
		final float surfaceNormalZ = face == 5 ? -1.0F : face == 6 ? +1.0F : 0.0F;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = faceX != 0 ? normalize(surfaceIntersectionPointZ, rectangularCuboidMinimumZ, rectangularCuboidMaximumZ) : normalize(surfaceIntersectionPointX, rectangularCuboidMinimumX, rectangularCuboidMaximumX);
		final float textureCoordinatesV = faceY != 0 ? normalize(surfaceIntersectionPointZ, rectangularCuboidMinimumZ, rectangularCuboidMaximumZ) : normalize(surfaceIntersectionPointY, rectangularCuboidMinimumY, rectangularCuboidMaximumY);
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromW(surfaceNormalX, surfaceNormalY, surfaceNormalZ);
		
//		Update the intersection array:
		intersectionLHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionLHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionLHSSetPrimitiveIndex(primitiveIndex);
		intersectionLHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionLHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the rectangular cuboid at offset {@code shape3FRectangularCuboid3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the rectangular cuboid
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FRectangularCuboid3FArrayOffset the offset in {@link #shape3FRectangularCuboid3FArray}
	 */
	protected final void shape3FRectangularCuboid3FIntersectionComputeRHS(final float t, final int primitiveIndex, final int shape3FRectangularCuboid3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the rectangular cuboid variables:
		final float rectangularCuboidMaximumX = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MAXIMUM + 0];
		final float rectangularCuboidMaximumY = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MAXIMUM + 1];
		final float rectangularCuboidMaximumZ = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MAXIMUM + 2];
		final float rectangularCuboidMinimumX = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MINIMUM + 0];
		final float rectangularCuboidMinimumY = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MINIMUM + 1];
		final float rectangularCuboidMinimumZ = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + CompiledShape3FCache.RECTANGULAR_CUBOID_3_F_OFFSET_MINIMUM + 2];
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute the midpoint of the rectangular cuboid:
		final float midpointX = (rectangularCuboidMaximumX + rectangularCuboidMinimumX) * 0.5F;
		final float midpointY = (rectangularCuboidMaximumY + rectangularCuboidMinimumY) * 0.5F;
		final float midpointZ = (rectangularCuboidMaximumZ + rectangularCuboidMinimumZ) * 0.5F;
		
//		Compute half of the dimensions of the rectangular cuboid:
		final float halfDimensionX = (rectangularCuboidMaximumX - rectangularCuboidMinimumX) * 0.5F;
		final float halfDimensionY = (rectangularCuboidMaximumY - rectangularCuboidMinimumY) * 0.5F;
		final float halfDimensionZ = (rectangularCuboidMaximumZ - rectangularCuboidMinimumZ) * 0.5F;
		
//		Initialize an epsilon value:
		final float epsilon = 0.0001F;
		
//		Compute the face for each axis:
		final int faceX = surfaceIntersectionPointX < midpointX && surfaceIntersectionPointX + halfDimensionX - epsilon < midpointX ? -1 : surfaceIntersectionPointX > midpointX && surfaceIntersectionPointX - halfDimensionX + epsilon > midpointX ? 1 : 0;
		final int faceY = surfaceIntersectionPointY < midpointY && surfaceIntersectionPointY + halfDimensionY - epsilon < midpointY ? -1 : surfaceIntersectionPointY > midpointY && surfaceIntersectionPointY - halfDimensionY + epsilon > midpointY ? 1 : 0;
		final int faceZ = surfaceIntersectionPointZ < midpointZ && surfaceIntersectionPointZ + halfDimensionZ - epsilon < midpointZ ? -1 : surfaceIntersectionPointZ > midpointZ && surfaceIntersectionPointZ - halfDimensionZ + epsilon > midpointZ ? 1 : 0;
		
//		Compute the face to use:
		final int face = faceX == -1 ? 1 : faceX == 1 ? 2 : faceY == -1 ? 3 : faceY == 1 ? 4 : faceZ == -1 ? 5 : faceZ == 1 ? 6 : 0;
		
//		Compute the surface normal:
		final float surfaceNormalX = face == 1 ? -1.0F : face == 2 ? +1.0F : 0.0F;
		final float surfaceNormalY = face == 3 ? -1.0F : face == 4 ? +1.0F : 0.0F;
		final float surfaceNormalZ = face == 5 ? -1.0F : face == 6 ? +1.0F : 0.0F;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = faceX != 0 ? normalize(surfaceIntersectionPointZ, rectangularCuboidMinimumZ, rectangularCuboidMaximumZ) : normalize(surfaceIntersectionPointX, rectangularCuboidMinimumX, rectangularCuboidMaximumX);
		final float textureCoordinatesV = faceY != 0 ? normalize(surfaceIntersectionPointZ, rectangularCuboidMinimumZ, rectangularCuboidMaximumZ) : normalize(surfaceIntersectionPointY, rectangularCuboidMinimumY, rectangularCuboidMaximumY);
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromW(surfaceNormalX, surfaceNormalY, surfaceNormalZ);
		
//		Update the intersection array:
		intersectionRHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionRHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionRHSSetPrimitiveIndex(primitiveIndex);
		intersectionRHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionRHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Shape3F - Sphere3F //////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given sphere in object space, {@code false} otherwise.
	 * 
	 * @param shape3FSphere3FArrayOffset the offset for the sphere in {@link #shape3FSphere3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given sphere in object space, {@code false} otherwise
	 */
	protected final boolean shape3FSphere3FIntersects(final int shape3FSphere3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return shape3FSphere3FIntersectionT(shape3FSphere3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns the parametric T value for a given sphere in object space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @param shape3FSphere3FArrayOffset the offset for the sphere in {@link #shape3FSphere3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return the parametric T value for a given sphere in object space, or {@code 0.0F} if no intersection was found
	 */
	protected final float shape3FSphere3FIntersectionT(final int shape3FSphere3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the sphere variables:
		final float sphereCenterX = this.shape3FSphere3FArray[shape3FSphere3FArrayOffset + CompiledShape3FCache.SPHERE_3_F_OFFSET_CENTER + 0];
		final float sphereCenterY = this.shape3FSphere3FArray[shape3FSphere3FArrayOffset + CompiledShape3FCache.SPHERE_3_F_OFFSET_CENTER + 1];
		final float sphereCenterZ = this.shape3FSphere3FArray[shape3FSphere3FArrayOffset + CompiledShape3FCache.SPHERE_3_F_OFFSET_CENTER + 2];
		final float sphereRadius = this.shape3FSphere3FArray[shape3FSphere3FArrayOffset + CompiledShape3FCache.SPHERE_3_F_OFFSET_RADIUS];
		final float sphereRadiusSquared = sphereRadius * sphereRadius;
		
//		Compute the direction from the sphere center to the ray origin:
		final float sphereCenterToRayOriginX = rayOriginX - sphereCenterX;
		final float sphereCenterToRayOriginY = rayOriginY - sphereCenterY;
		final float sphereCenterToRayOriginZ = rayOriginZ - sphereCenterZ;
		
//		Compute the variables for the quadratic system:
		final float a = rayDirectionX * rayDirectionX + rayDirectionY * rayDirectionY + rayDirectionZ * rayDirectionZ;
		final float b = 2.0F * (sphereCenterToRayOriginX * rayDirectionX + sphereCenterToRayOriginY * rayDirectionY + sphereCenterToRayOriginZ * rayDirectionZ);
		final float c = (sphereCenterToRayOriginX * sphereCenterToRayOriginX + sphereCenterToRayOriginY * sphereCenterToRayOriginY + sphereCenterToRayOriginZ * sphereCenterToRayOriginZ) - sphereRadiusSquared;
		
//		Compute the intersection by solving the quadratic system and checking the valid intersection interval:
		final float t = solveQuadraticSystem(a, b, c, rayTMinimum, rayTMaximum);
		
		return t;
	}
	
	/**
	 * Computes the intersection properties for the sphere at offset {@code shape3FSphere3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the sphere
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FSphere3FArrayOffset the offset in {@link #shape3FSphere3FArray}
	 */
	protected final void shape3FSphere3FIntersectionComputeLHS(final float t, final int primitiveIndex, final int shape3FSphere3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the sphere variables:
		final float sphereCenterX = this.shape3FSphere3FArray[shape3FSphere3FArrayOffset + CompiledShape3FCache.SPHERE_3_F_OFFSET_CENTER + 0];
		final float sphereCenterY = this.shape3FSphere3FArray[shape3FSphere3FArrayOffset + CompiledShape3FCache.SPHERE_3_F_OFFSET_CENTER + 1];
		final float sphereCenterZ = this.shape3FSphere3FArray[shape3FSphere3FArrayOffset + CompiledShape3FCache.SPHERE_3_F_OFFSET_CENTER + 2];
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute the geometric surface normal:
		final float surfaceNormalGX = surfaceIntersectionPointX - sphereCenterX;
		final float surfaceNormalGY = surfaceIntersectionPointY - sphereCenterY;
		final float surfaceNormalGZ = surfaceIntersectionPointZ - sphereCenterZ;
		
//		Compute the V-direction of the geometric orthonormal basis:
		final float vGX = -PI_MULTIPLIED_BY_2 * surfaceNormalGY;
		final float vGY = +PI_MULTIPLIED_BY_2 * surfaceNormalGX;
		final float vGZ = 0.0F;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceNormalGY, surfaceNormalGX), 0.0F, PI_MULTIPLIED_BY_2) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float textureCoordinatesV = acos(saturateF(surfaceNormalGZ, -1.0F, 1.0F)) * PI_RECIPROCAL;
		
//		Compute the geometric orthonormal basis:
		orthonormalBasis33FSetFromWV(surfaceNormalGX, surfaceNormalGY, surfaceNormalGZ, vGX, vGY, vGZ);
		
//		Update the intersection array:
		intersectionLHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionLHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionLHSSetPrimitiveIndex(primitiveIndex);
		intersectionLHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionLHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the sphere at offset {@code shape3FSphere3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the sphere
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FSphere3FArrayOffset the offset in {@link #shape3FSphere3FArray}
	 */
	protected final void shape3FSphere3FIntersectionComputeRHS(final float t, final int primitiveIndex, final int shape3FSphere3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the sphere variables:
		final float sphereCenterX = this.shape3FSphere3FArray[shape3FSphere3FArrayOffset + CompiledShape3FCache.SPHERE_3_F_OFFSET_CENTER + 0];
		final float sphereCenterY = this.shape3FSphere3FArray[shape3FSphere3FArrayOffset + CompiledShape3FCache.SPHERE_3_F_OFFSET_CENTER + 1];
		final float sphereCenterZ = this.shape3FSphere3FArray[shape3FSphere3FArrayOffset + CompiledShape3FCache.SPHERE_3_F_OFFSET_CENTER + 2];
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute the geometric surface normal:
		final float surfaceNormalGX = surfaceIntersectionPointX - sphereCenterX;
		final float surfaceNormalGY = surfaceIntersectionPointY - sphereCenterY;
		final float surfaceNormalGZ = surfaceIntersectionPointZ - sphereCenterZ;
		
//		Compute the V-direction of the geometric orthonormal basis:
		final float vGX = -PI_MULTIPLIED_BY_2 * surfaceNormalGY;
		final float vGY = +PI_MULTIPLIED_BY_2 * surfaceNormalGX;
		final float vGZ = 0.0F;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceNormalGY, surfaceNormalGX), 0.0F, PI_MULTIPLIED_BY_2) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float textureCoordinatesV = acos(saturateF(surfaceNormalGZ, -1.0F, 1.0F)) * PI_RECIPROCAL;
		
//		Compute the geometric orthonormal basis:
		orthonormalBasis33FSetFromWV(surfaceNormalGX, surfaceNormalGY, surfaceNormalGZ, vGX, vGY, vGZ);
		
//		Update the intersection array:
		intersectionRHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionRHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionRHSSetPrimitiveIndex(primitiveIndex);
		intersectionRHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionRHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Shape3F - Torus3F ///////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given torus in object space, {@code false} otherwise.
	 * 
	 * @param shape3FTorus3FArrayOffset the offset for the torus in {@link #shape3FTorus3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given torus in object space, {@code false} otherwise
	 */
	protected final boolean shape3FTorus3FIntersects(final int shape3FTorus3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return shape3FTorus3FIntersectionT(shape3FTorus3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns the parametric T value for a given torus in object space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @param shape3FTorus3FArrayOffset the offset for the torus in {@link #shape3FTorus3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return the parametric T value for a given torus in object space, or {@code 0.0F} if no intersection was found
	 */
	protected final float shape3FTorus3FIntersectionT(final int shape3FTorus3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the torus variables:
		final float torusRadiusInner = this.shape3FTorus3FArray[shape3FTorus3FArrayOffset + CompiledShape3FCache.TORUS_3_F_OFFSET_RADIUS_INNER];
		final float torusRadiusInnerSquared = torusRadiusInner * torusRadiusInner;
		final float torusRadiusOuter = this.shape3FTorus3FArray[shape3FTorus3FArrayOffset + CompiledShape3FCache.TORUS_3_F_OFFSET_RADIUS_OUTER];
		final float torusRadiusOuterSquared = torusRadiusOuter * torusRadiusOuter;
		
		/*
		 * The quartic system solvers below present with different problems.
		 * When using double precision, the result is good but it is very slow.
		 * When using single precision, the result is poor but it is very fast.
		 * The one with good result will be used for now, until it can be fixed.
		 */
		
//		Compute the variables used in the process of computing the variables for the quartic system:
		final double f0 = rayDirectionX * rayDirectionX + rayDirectionY * rayDirectionY + rayDirectionZ * rayDirectionZ;
		final double f1 = (rayOriginX * rayDirectionX + rayOriginY * rayDirectionY + rayOriginZ * rayDirectionZ) * 2.0D;
		final double f2 = torusRadiusInnerSquared;
		final double f3 = torusRadiusOuterSquared;
		final double f4 = (rayOriginX * rayOriginX + rayOriginY * rayOriginY + rayOriginZ * rayOriginZ) - f2 - f3;
		final double f5 = rayDirectionZ;
		final double f6 = rayOriginZ;
		
//		Compute the variables for the quartic system:
		final double a = f0 * f0;
		final double b = f0 * 2.0D * f1;
		final double c = f1 * f1 + 2.0D * f0 * f4 + 4.0D * f3 * f5 * f5;
		final double d = f1 * 2.0D * f4 + 8.0D * f3 * f6 * f5;
		final double e = f4 * f4 + 4.0D * f3 * f6 * f6 - 4.0D * f3 * f2;
		
//		Compute the intersection by solving the quartic system and checking the valid intersection interval:
		final float t = solveQuarticSystemD(a, b, c, d, e, rayTMinimum, rayTMaximum);
		
//		Compute the variables used in the process of computing the variables for the quartic system:
//		final float f0 = rayDirectionX * rayDirectionX + rayDirectionY * rayDirectionY + rayDirectionZ * rayDirectionZ;
//		final float f1 = (rayOriginX * rayDirectionX + rayOriginY * rayDirectionY + rayOriginZ * rayDirectionZ) * 2.0F;
//		final float f2 = torusRadiusInnerSquared;
//		final float f3 = torusRadiusOuterSquared;
//		final float f4 = (rayOriginX * rayOriginX + rayOriginY * rayOriginY + rayOriginZ * rayOriginZ) - f2 - f3;
//		final float f5 = rayDirectionZ;
//		final float f6 = rayOriginZ;
		
//		Compute the variables for the quartic system:
//		final float a = f0 * f0;
//		final float b = f0 * 2.0F * f1;
//		final float c = f1 * f1 + 2.0F * f0 * f4 + 4.0F * f3 * f5 * f5;
//		final float d = f1 * 2.0F * f4 + 8.0F * f3 * f6 * f5;
//		final float e = f4 * f4 + 4.0F * f3 * f6 * f6 - 4.0F * f3 * f2;
		
//		Compute the intersection by solving the quartic system and checking the valid intersection interval:
//		final float t = solveQuarticSystemF(a, b, c, d, e, rayTMinimum, rayTMaximum);
		
		return t;
	}
	
	/**
	 * Computes the intersection properties for the torus at offset {@code shape3FTorus3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the torus
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FTorus3FArrayOffset the offset in {@link #shape3FTorus3FArray}
	 */
	protected final void shape3FTorus3FIntersectionComputeLHS(final float t, final int primitiveIndex, final int shape3FTorus3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the torus variables:
		final float torusRadiusInner = this.shape3FTorus3FArray[shape3FTorus3FArrayOffset + CompiledShape3FCache.TORUS_3_F_OFFSET_RADIUS_INNER];
		final float torusRadiusInnerSquared = torusRadiusInner * torusRadiusInner;
		final float torusRadiusOuter = this.shape3FTorus3FArray[shape3FTorus3FArrayOffset + CompiledShape3FCache.TORUS_3_F_OFFSET_RADIUS_OUTER];
		final float torusRadiusOuterSquared = torusRadiusOuter * torusRadiusOuter;
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute the derivative, which is used to create the surface normal:
		final float derivative = (surfaceIntersectionPointX * surfaceIntersectionPointX + surfaceIntersectionPointY * surfaceIntersectionPointY + surfaceIntersectionPointZ * surfaceIntersectionPointZ) - torusRadiusInnerSquared - torusRadiusOuterSquared;
		
//		Compute the surface normal:
		final float surfaceNormalX = surfaceIntersectionPointX * derivative;
		final float surfaceNormalY = surfaceIntersectionPointY * derivative;
		final float surfaceNormalZ = surfaceIntersectionPointZ * derivative + 2.0F * torusRadiusOuterSquared * surfaceIntersectionPointZ;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, PI_MULTIPLIED_BY_2) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float textureCoordinatesV = (asin(saturateF(surfaceIntersectionPointZ / torusRadiusInner, -1.0F, 1.0F)) + PI_DIVIDED_BY_2) * PI_RECIPROCAL;
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromW(surfaceNormalX, surfaceNormalY, surfaceNormalZ);
		
//		Update the intersection array:
		intersectionLHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionLHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionLHSSetPrimitiveIndex(primitiveIndex);
		intersectionLHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionLHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the torus at offset {@code shape3FTorus3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the torus
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FTorus3FArrayOffset the offset in {@link #shape3FTorus3FArray}
	 */
	protected final void shape3FTorus3FIntersectionComputeRHS(final float t, final int primitiveIndex, final int shape3FTorus3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the torus variables:
		final float torusRadiusInner = this.shape3FTorus3FArray[shape3FTorus3FArrayOffset + CompiledShape3FCache.TORUS_3_F_OFFSET_RADIUS_INNER];
		final float torusRadiusInnerSquared = torusRadiusInner * torusRadiusInner;
		final float torusRadiusOuter = this.shape3FTorus3FArray[shape3FTorus3FArrayOffset + CompiledShape3FCache.TORUS_3_F_OFFSET_RADIUS_OUTER];
		final float torusRadiusOuterSquared = torusRadiusOuter * torusRadiusOuter;
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute the derivative, which is used to create the surface normal:
		final float derivative = (surfaceIntersectionPointX * surfaceIntersectionPointX + surfaceIntersectionPointY * surfaceIntersectionPointY + surfaceIntersectionPointZ * surfaceIntersectionPointZ) - torusRadiusInnerSquared - torusRadiusOuterSquared;
		
//		Compute surface normal:
		final float surfaceNormalX = surfaceIntersectionPointX * derivative;
		final float surfaceNormalY = surfaceIntersectionPointY * derivative;
		final float surfaceNormalZ = surfaceIntersectionPointZ * derivative + 2.0F * torusRadiusOuterSquared * surfaceIntersectionPointZ;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, PI_MULTIPLIED_BY_2) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float textureCoordinatesV = (asin(saturateF(surfaceIntersectionPointZ / torusRadiusInner, -1.0F, 1.0F)) + PI_DIVIDED_BY_2) * PI_RECIPROCAL;
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromW(surfaceNormalX, surfaceNormalY, surfaceNormalZ);
		
//		Update the intersection array:
		intersectionRHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		intersectionRHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionRHSSetPrimitiveIndex(primitiveIndex);
		intersectionRHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionRHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Shape3F - Triangle3F ////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given triangle in object space, {@code false} otherwise.
	 * 
	 * @param shape3FTriangle3FArrayOffset the offset for the triangle in {@link #shape3FTriangle3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given triangle in object space, {@code false} otherwise
	 */
	protected final boolean shape3FTriangle3FIntersects(final int shape3FTriangle3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return shape3FTriangle3FIntersectionT(shape3FTriangle3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns the parametric T value for a given triangle in object space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @param shape3FTriangle3FArrayOffset the offset for the triangle in {@link #shape3FTriangle3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return the parametric T value for a given triangle in object space, or {@code 0.0F} if no intersection was found
	 */
	protected final float shape3FTriangle3FIntersectionT(final int shape3FTriangle3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the triangle variables that will be referred to by 'triangleAPosition', 'triangleBPosition' and 'triangleCPosition' in the comments:
		final float triangleAPositionX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_POSITION + 0];
		final float triangleAPositionY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_POSITION + 1];
		final float triangleAPositionZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_POSITION + 2];
		final float triangleBPositionX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_POSITION + 0];
		final float triangleBPositionY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_POSITION + 1];
		final float triangleBPositionZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_POSITION + 2];
		final float triangleCPositionX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_POSITION + 0];
		final float triangleCPositionY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_POSITION + 1];
		final float triangleCPositionZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_POSITION + 2];
		
//		Compute the direction from 'triangleAPosition' to 'triangleBPosition', denoted by 'edgeAB' in the comments:
		final float edgeABX = triangleBPositionX - triangleAPositionX;
		final float edgeABY = triangleBPositionY - triangleAPositionY;
		final float edgeABZ = triangleBPositionZ - triangleAPositionZ;
		
//		Compute the direction from 'triangleCPosition' to 'triangleAPosition', denoted by 'edgeCA' in the comments:
		final float edgeCAX = triangleAPositionX - triangleCPositionX;
		final float edgeCAY = triangleAPositionY - triangleCPositionY;
		final float edgeCAZ = triangleAPositionZ - triangleCPositionZ;
		
//		Compute the cross product between 'edgeAB' and 'edgeCA', denoted by 'direction0' in the comments:
		final float direction0X = edgeABY * edgeCAZ - edgeABZ * edgeCAY;
		final float direction0Y = edgeABZ * edgeCAX - edgeABX * edgeCAZ;
		final float direction0Z = edgeABX * edgeCAY - edgeABY * edgeCAX;
		
//		Compute the determinant, which is the dot product between 'rayDirection' and 'direction0' and its reciprocal (or inverse):
		final float determinant = rayDirectionX * direction0X + rayDirectionY * direction0Y + rayDirectionZ * direction0Z;
		final float determinantReciprocal = 1.0F / determinant;
		
//		Compute the direction from 'rayOrigin' to 'triangleAPosition', denoted by 'direction1' in the comments:
		final float direction1X = triangleAPositionX - rayOriginX;
		final float direction1Y = triangleAPositionY - rayOriginY;
		final float direction1Z = triangleAPositionZ - rayOriginZ;
		
//		Compute the intersection as the dot product between 'direction0' and 'direction1' followed by a multiplication with the reciprocal (or inverse) determinant:
		final float t = (direction0X * direction1X + direction0Y * direction1Y + direction0Z * direction1Z) * determinantReciprocal;
		
		if(t <= rayTMinimum || t >= rayTMaximum) {
			return 0.0F;
		}
		
//		Compute the cross product between 'direction1' and 'rayDirection', denoted by 'direction2' in the comments:
		final float direction2X = direction1Y * rayDirectionZ - direction1Z * rayDirectionY;
		final float direction2Y = direction1Z * rayDirectionX - direction1X * rayDirectionZ;
		final float direction2Z = direction1X * rayDirectionY - direction1Y * rayDirectionX;
		
//		Compute the Barycentric U-coordinate:
		final float uScaled = direction2X * edgeCAX + direction2Y * edgeCAY + direction2Z * edgeCAZ;
		final float u = uScaled * determinantReciprocal;
		
		if(u < 0.0F) {
			return 0.0F;
		}
		
//		Compute the Barycentric V-coordinate:
		final float vScaled = direction2X * edgeABX + direction2Y * edgeABY + direction2Z * edgeABZ;
		final float v = vScaled * determinantReciprocal;
		
		if(v < 0.0F || (uScaled + vScaled) * determinant > determinant * determinant) {
			return 0.0F;
		}
		
		return t;
	}
	
	/**
	 * Computes the intersection properties for the triangle at offset {@code shape3FTriangle3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the triangle
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FTriangle3FArrayOffset the offset in {@link #shape3FTriangle3FArray}
	 */
	protected final void shape3FTriangle3FIntersectionComputeLHS(final float t, final int primitiveIndex, final int shape3FTriangle3FArrayOffset) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the triangle variables that will be referred to by 'triangleAPosition', 'triangleBPosition' and 'triangleCPosition' in the comments:
		final float triangleAPositionX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_POSITION + 0];
		final float triangleAPositionY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_POSITION + 1];
		final float triangleAPositionZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_POSITION + 2];
		final float triangleBPositionX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_POSITION + 0];
		final float triangleBPositionY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_POSITION + 1];
		final float triangleBPositionZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_POSITION + 2];
		final float triangleCPositionX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_POSITION + 0];
		final float triangleCPositionY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_POSITION + 1];
		final float triangleCPositionZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_POSITION + 2];
		
//		Retrieve the triangle variables that will be used for texturing:
		final float triangleATextureCoordinatesU = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_TEXTURE_COORDINATES + 0];
		final float triangleATextureCoordinatesV = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_TEXTURE_COORDINATES + 1];
		final float triangleBTextureCoordinatesU = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_TEXTURE_COORDINATES + 0];
		final float triangleBTextureCoordinatesV = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_TEXTURE_COORDINATES + 1];
		final float triangleCTextureCoordinatesU = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_TEXTURE_COORDINATES + 0];
		final float triangleCTextureCoordinatesV = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_TEXTURE_COORDINATES + 1];
		
//		Retrieve the triangle variables that will be used when constructing the geometric and shading orthonormal bases:
		final float triangleAOrthonormalBasisWX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_ORTHONORMAL_BASIS_W + 0];
		final float triangleAOrthonormalBasisWY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_ORTHONORMAL_BASIS_W + 1];
		final float triangleAOrthonormalBasisWZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_ORTHONORMAL_BASIS_W + 2];
		final float triangleBOrthonormalBasisWX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_ORTHONORMAL_BASIS_W + 0];
		final float triangleBOrthonormalBasisWY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_ORTHONORMAL_BASIS_W + 1];
		final float triangleBOrthonormalBasisWZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_ORTHONORMAL_BASIS_W + 2];
		final float triangleCOrthonormalBasisWX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_ORTHONORMAL_BASIS_W + 0];
		final float triangleCOrthonormalBasisWY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_ORTHONORMAL_BASIS_W + 1];
		final float triangleCOrthonormalBasisWZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_ORTHONORMAL_BASIS_W + 2];
		
//		Compute the direction from 'triangleAPosition' to 'triangleBPosition', denoted by 'edgeAB' in the comments:
		final float edgeABX = triangleBPositionX - triangleAPositionX;
		final float edgeABY = triangleBPositionY - triangleAPositionY;
		final float edgeABZ = triangleBPositionZ - triangleAPositionZ;
		
//		Compute the direction from 'triangleAPosition' to 'triangleCPosition', denoted by 'edgeAC' in the comments:
		final float edgeACX = triangleCPositionX - triangleAPositionX;
		final float edgeACY = triangleCPositionY - triangleAPositionY;
		final float edgeACZ = triangleCPositionZ - triangleAPositionZ;
		
//		Compute the direction from 'triangleCPosition' to 'triangleAPosition', denoted by 'edgeCA' in the comments:
		final float edgeCAX = triangleAPositionX - triangleCPositionX;
		final float edgeCAY = triangleAPositionY - triangleCPositionY;
		final float edgeCAZ = triangleAPositionZ - triangleCPositionZ;
		
//		Compute the cross product between 'edgeAB' and 'edgeCA', denoted by 'direction0' in the comments:
		final float direction0X = edgeABY * edgeCAZ - edgeABZ * edgeCAY;
		final float direction0Y = edgeABZ * edgeCAX - edgeABX * edgeCAZ;
		final float direction0Z = edgeABX * edgeCAY - edgeABY * edgeCAX;
		
//		Compute the determinant, which is the dot product between 'rayDirection' and 'direction0' and its reciprocal (or inverse):
		final float determinant = rayDirectionX * direction0X + rayDirectionY * direction0Y + rayDirectionZ * direction0Z;
		final float determinantReciprocal = 1.0F / determinant;
		
//		Compute the direction from 'rayOrigin' to 'triangleAPosition', denoted by 'direction1' in the comments:
		final float direction1X = triangleAPositionX - rayOriginX;
		final float direction1Y = triangleAPositionY - rayOriginY;
		final float direction1Z = triangleAPositionZ - rayOriginZ;
		
//		Compute the cross product between 'direction1' and 'rayDirection', denoted by 'direction2' in the comments:
		final float direction2X = direction1Y * rayDirectionZ - direction1Z * rayDirectionY;
		final float direction2Y = direction1Z * rayDirectionX - direction1X * rayDirectionZ;
		final float direction2Z = direction1X * rayDirectionY - direction1Y * rayDirectionX;
		
//		Compute the Barycentric U-coordinate:
		final float uScaled = direction2X * edgeCAX + direction2Y * edgeCAY + direction2Z * edgeCAZ;
		final float u = uScaled * determinantReciprocal;
		
//		Compute the Barycentric V-coordinate:
		final float vScaled = direction2X * edgeABX + direction2Y * edgeABY + direction2Z * edgeABZ;
		final float v = vScaled * determinantReciprocal;
		
//		Compute the Barycentric W-coordinate:
		final float w = 1.0F - u - v;
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute the surface normal for the geometry:
		final float surfaceNormalGX = edgeABY * edgeACZ - edgeABZ * edgeACY;
		final float surfaceNormalGY = edgeABZ * edgeACX - edgeABX * edgeACZ;
		final float surfaceNormalGZ = edgeABX * edgeACY - edgeABY * edgeACX;
		
//		Compute the surface normal for shading:
		final float surfaceNormalSX = triangleAOrthonormalBasisWX * w + triangleBOrthonormalBasisWX * u + triangleCOrthonormalBasisWX * v;
		final float surfaceNormalSY = triangleAOrthonormalBasisWY * w + triangleBOrthonormalBasisWY * u + triangleCOrthonormalBasisWY * v;
		final float surfaceNormalSZ = triangleAOrthonormalBasisWZ * w + triangleBOrthonormalBasisWZ * u + triangleCOrthonormalBasisWZ * v;
		
		final float dU1 = triangleATextureCoordinatesU - triangleCTextureCoordinatesU;
		final float dU2 = triangleBTextureCoordinatesU - triangleCTextureCoordinatesU;
		final float dV1 = triangleATextureCoordinatesV - triangleCTextureCoordinatesV;
		final float dV2 = triangleBTextureCoordinatesV - triangleCTextureCoordinatesV;
		
		final float determinantUV = dU1 * dV2 - dV1 * dU2;
		
//		Compute the orthonormal basis for the geometry:
		orthonormalBasis33FSetFromW(surfaceNormalGX, surfaceNormalGY, surfaceNormalGZ);
		
//		Update the intersection array:
		intersectionLHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		
//		Compute the orthonormal basis for shading:
		if(!checkIsZero(determinantUV)) {
			final float determinantUVReciprocal = 1.0F / determinantUV;
			
			final float vSX = (-dU2 * (triangleAPositionX - triangleCPositionX) + dU1 * (triangleBPositionX - triangleCPositionX)) * determinantUVReciprocal;
			final float vSY = (-dU2 * (triangleAPositionY - triangleCPositionY) + dU1 * (triangleBPositionY - triangleCPositionY)) * determinantUVReciprocal;
			final float vSZ = (-dU2 * (triangleAPositionZ - triangleCPositionZ) + dU1 * (triangleBPositionZ - triangleCPositionZ)) * determinantUVReciprocal;
			
			orthonormalBasis33FSetFromWV(surfaceNormalSX, surfaceNormalSY, surfaceNormalSZ, vSX, vSY, vSZ);
		} else {
			orthonormalBasis33FSetFromW(surfaceNormalSX, surfaceNormalSY, surfaceNormalSZ);
		}
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = triangleATextureCoordinatesU * w + triangleBTextureCoordinatesU * u + triangleCTextureCoordinatesU * v;
		final float textureCoordinatesV = triangleATextureCoordinatesV * w + triangleBTextureCoordinatesV * u + triangleCTextureCoordinatesV * v;
		
//		Update the intersection array:
		intersectionLHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionLHSSetPrimitiveIndex(primitiveIndex);
		intersectionLHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionLHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the triangle at offset {@code shape3FTriangle3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the triangle
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FTriangle3FArrayOffset the offset in {@link #shape3FTriangle3FArray}
	 */
	protected final void shape3FTriangle3FIntersectionComputeRHS(final float t, final int primitiveIndex, final int shape3FTriangle3FArrayOffset) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the triangle variables that will be referred to by 'triangleAPosition', 'triangleBPosition' and 'triangleCPosition' in the comments:
		final float triangleAPositionX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_POSITION + 0];
		final float triangleAPositionY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_POSITION + 1];
		final float triangleAPositionZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_POSITION + 2];
		final float triangleBPositionX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_POSITION + 0];
		final float triangleBPositionY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_POSITION + 1];
		final float triangleBPositionZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_POSITION + 2];
		final float triangleCPositionX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_POSITION + 0];
		final float triangleCPositionY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_POSITION + 1];
		final float triangleCPositionZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_POSITION + 2];
		
//		Retrieve the triangle variables that will be used for texturing:
		final float triangleATextureCoordinatesU = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_TEXTURE_COORDINATES + 0];
		final float triangleATextureCoordinatesV = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_TEXTURE_COORDINATES + 1];
		final float triangleBTextureCoordinatesU = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_TEXTURE_COORDINATES + 0];
		final float triangleBTextureCoordinatesV = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_TEXTURE_COORDINATES + 1];
		final float triangleCTextureCoordinatesU = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_TEXTURE_COORDINATES + 0];
		final float triangleCTextureCoordinatesV = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_TEXTURE_COORDINATES + 1];
		
//		Retrieve the triangle variables that will be used when constructing the geometric and shading orthonormal bases:
		final float triangleAOrthonormalBasisWX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_ORTHONORMAL_BASIS_W + 0];
		final float triangleAOrthonormalBasisWY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_ORTHONORMAL_BASIS_W + 1];
		final float triangleAOrthonormalBasisWZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_A_ORTHONORMAL_BASIS_W + 2];
		final float triangleBOrthonormalBasisWX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_ORTHONORMAL_BASIS_W + 0];
		final float triangleBOrthonormalBasisWY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_ORTHONORMAL_BASIS_W + 1];
		final float triangleBOrthonormalBasisWZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_B_ORTHONORMAL_BASIS_W + 2];
		final float triangleCOrthonormalBasisWX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_ORTHONORMAL_BASIS_W + 0];
		final float triangleCOrthonormalBasisWY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_ORTHONORMAL_BASIS_W + 1];
		final float triangleCOrthonormalBasisWZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + CompiledShape3FCache.TRIANGLE_3_F_OFFSET_C_ORTHONORMAL_BASIS_W + 2];
		
//		Compute the direction from 'triangleAPosition' to 'triangleBPosition', denoted by 'edgeAB' in the comments:
		final float edgeABX = triangleBPositionX - triangleAPositionX;
		final float edgeABY = triangleBPositionY - triangleAPositionY;
		final float edgeABZ = triangleBPositionZ - triangleAPositionZ;
		
//		Compute the direction from 'triangleAPosition' to 'triangleCPosition', denoted by 'edgeAC' in the comments:
		final float edgeACX = triangleCPositionX - triangleAPositionX;
		final float edgeACY = triangleCPositionY - triangleAPositionY;
		final float edgeACZ = triangleCPositionZ - triangleAPositionZ;
		
//		Compute the direction from 'triangleCPosition' to 'triangleAPosition', denoted by 'edgeCA' in the comments:
		final float edgeCAX = triangleAPositionX - triangleCPositionX;
		final float edgeCAY = triangleAPositionY - triangleCPositionY;
		final float edgeCAZ = triangleAPositionZ - triangleCPositionZ;
		
//		Compute the cross product between 'edgeAB' and 'edgeCA', denoted by 'direction0' in the comments:
		final float direction0X = edgeABY * edgeCAZ - edgeABZ * edgeCAY;
		final float direction0Y = edgeABZ * edgeCAX - edgeABX * edgeCAZ;
		final float direction0Z = edgeABX * edgeCAY - edgeABY * edgeCAX;
		
//		Compute the determinant, which is the dot product between 'rayDirection' and 'direction0' and its reciprocal (or inverse):
		final float determinant = rayDirectionX * direction0X + rayDirectionY * direction0Y + rayDirectionZ * direction0Z;
		final float determinantReciprocal = 1.0F / determinant;
		
//		Compute the direction from 'rayOrigin' to 'triangleAPosition', denoted by 'direction1' in the comments:
		final float direction1X = triangleAPositionX - rayOriginX;
		final float direction1Y = triangleAPositionY - rayOriginY;
		final float direction1Z = triangleAPositionZ - rayOriginZ;
		
//		Compute the cross product between 'direction1' and 'rayDirection', denoted by 'direction2' in the comments:
		final float direction2X = direction1Y * rayDirectionZ - direction1Z * rayDirectionY;
		final float direction2Y = direction1Z * rayDirectionX - direction1X * rayDirectionZ;
		final float direction2Z = direction1X * rayDirectionY - direction1Y * rayDirectionX;
		
//		Compute the Barycentric U-coordinate:
		final float uScaled = direction2X * edgeCAX + direction2Y * edgeCAY + direction2Z * edgeCAZ;
		final float u = uScaled * determinantReciprocal;
		
//		Compute the Barycentric V-coordinate:
		final float vScaled = direction2X * edgeABX + direction2Y * edgeABY + direction2Z * edgeABZ;
		final float v = vScaled * determinantReciprocal;
		
//		Compute the Barycentric W-coordinate:
		final float w = 1.0F - u - v;
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute the surface normal for the geometry:
		final float surfaceNormalGX = edgeABY * edgeACZ - edgeABZ * edgeACY;
		final float surfaceNormalGY = edgeABZ * edgeACX - edgeABX * edgeACZ;
		final float surfaceNormalGZ = edgeABX * edgeACY - edgeABY * edgeACX;
		
//		Compute the surface normal for shading:
		final float surfaceNormalSX = triangleAOrthonormalBasisWX * w + triangleBOrthonormalBasisWX * u + triangleCOrthonormalBasisWX * v;
		final float surfaceNormalSY = triangleAOrthonormalBasisWY * w + triangleBOrthonormalBasisWY * u + triangleCOrthonormalBasisWY * v;
		final float surfaceNormalSZ = triangleAOrthonormalBasisWZ * w + triangleBOrthonormalBasisWZ * u + triangleCOrthonormalBasisWZ * v;
		
		final float dU1 = triangleATextureCoordinatesU - triangleCTextureCoordinatesU;
		final float dU2 = triangleBTextureCoordinatesU - triangleCTextureCoordinatesU;
		final float dV1 = triangleATextureCoordinatesV - triangleCTextureCoordinatesV;
		final float dV2 = triangleBTextureCoordinatesV - triangleCTextureCoordinatesV;
		
		final float determinantUV = dU1 * dV2 - dV1 * dU2;
		
//		Compute the orthonormal basis for the geometry:
		orthonormalBasis33FSetFromW(surfaceNormalGX, surfaceNormalGY, surfaceNormalGZ);
		
//		Update the intersection array:
		intersectionRHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		
//		Compute the orthonormal basis for shading:
		if(!checkIsZero(determinantUV)) {
			final float determinantUVReciprocal = 1.0F / determinantUV;
			
			final float vSX = (-dU2 * (triangleAPositionX - triangleCPositionX) + dU1 * (triangleBPositionX - triangleCPositionX)) * determinantUVReciprocal;
			final float vSY = (-dU2 * (triangleAPositionY - triangleCPositionY) + dU1 * (triangleBPositionY - triangleCPositionY)) * determinantUVReciprocal;
			final float vSZ = (-dU2 * (triangleAPositionZ - triangleCPositionZ) + dU1 * (triangleBPositionZ - triangleCPositionZ)) * determinantUVReciprocal;
			
			orthonormalBasis33FSetFromWV(surfaceNormalSX, surfaceNormalSY, surfaceNormalSZ, vSX, vSY, vSZ);
		} else {
			orthonormalBasis33FSetFromW(surfaceNormalSX, surfaceNormalSY, surfaceNormalSZ);
		}
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = triangleATextureCoordinatesU * w + triangleBTextureCoordinatesU * u + triangleCTextureCoordinatesU * v;
		final float textureCoordinatesV = triangleATextureCoordinatesV * w + triangleBTextureCoordinatesV * u + triangleCTextureCoordinatesV * v;
		
//		Update the intersection array:
		intersectionRHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionRHSSetPrimitiveIndex(primitiveIndex);
		intersectionRHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionRHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Shape3F - TriangleMesh3F ////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given triangle mesh in object space, {@code false} otherwise.
	 * 
	 * @param shape3FTriangleMesh3FArrayOffset the offset for the triangle mesh in {@link #shape3FTriangleMesh3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given triangle mesh in object space, {@code false} otherwise
	 */
	protected final boolean shape3FTriangleMesh3FIntersects(final int shape3FTriangleMesh3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		return shape3FTriangleMesh3FIntersectionT(shape3FTriangleMesh3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
		
		/*
		 * T0 (Next = NO, Left = T1)
		 *     T1 (Next = T4, Left = T2)
		 *         T2 (Next = T3, Left = L0)
		 *             L0 (Next = L1)
		 *             L1 (Next = L2)
		 *             L2 (Next = T3)
		 *         T3 (Next = T4, Left = L3)
		 *             L3 (Next = L4)
		 *             L4 (Next = L5)
		 *             L5 (Next = T4)
		 *     T4 (Next = NO, Left = L6)
		 *         L6 (Next = L7)
		 *         L7 (Next = L8)
		 *         L8 (Next = NO)
		 */
		
		float tMinimumObjectSpace = rayTMinimum;
		float tMaximumObjectSpace = rayTMaximum;
		
		int absoluteOffset = shape3FTriangleMesh3FArrayOffset;
		int relativeOffset = 0;
		
		while(relativeOffset != -1) {
			final int offset = absoluteOffset + relativeOffset;
			final int id = this.shape3FTriangleMesh3FArray[offset + CompiledShape3FCache.TRIANGLE_MESH_3_F_B_V_H_NODE_3_F_OFFSET_ID];
			final int boundingVolumeOffset = this.shape3FTriangleMesh3FArray[offset + CompiledShape3FCache.TRIANGLE_MESH_3_F_B_V_H_NODE_3_F_OFFSET_BOUNDING_VOLUME_OFFSET];
			final int nextOffset = this.shape3FTriangleMesh3FArray[offset + CompiledShape3FCache.TRIANGLE_MESH_3_F_B_V_H_NODE_3_F_OFFSET_NEXT_OFFSET];
			final int leftOffsetOrTriangleCount = this.shape3FTriangleMesh3FArray[offset + CompiledShape3FCache.TRIANGLE_MESH_3_F_B_V_H_NODE_3_F_OFFSET_LEFT_OFFSET_OR_SHAPE_COUNT];
			
			final boolean isIntersectingBoundingVolume = boundingVolume3FAxisAlignedBoundingBox3FContainsOrIntersects(boundingVolumeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
			
			if(isIntersectingBoundingVolume && id == LeafBVHNode3F.ID) {
				for(int i = 0; i < leftOffsetOrTriangleCount; i++) {
					final int triangleOffset = this.shape3FTriangleMesh3FArray[offset + CompiledShape3FCache.TRIANGLE_MESH_3_F_B_V_H_NODE_3_F_OFFSET_LEFT_OFFSET_OR_SHAPE_COUNT + 1 + i];
					
					final float tObjectSpace = this.shape3FTriangle3FIntersectionT(triangleOffset, tMinimumObjectSpace, tMaximumObjectSpace);
					
					if(tObjectSpace > tMinimumObjectSpace && tObjectSpace < tMaximumObjectSpace) {
						return true;
					}
				}
				
				relativeOffset = nextOffset;
			} else if(isIntersectingBoundingVolume && id == TreeBVHNode3F.ID) {
				relativeOffset = leftOffsetOrTriangleCount;
			} else {
				relativeOffset = nextOffset;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns the parametric T value for a given triangle mesh in object space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @param shape3FTriangleMesh3FArrayOffset the offset for the triangle mesh in {@link #shape3FTriangleMesh3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return the parametric T value for a given triangle mesh in object space, or {@code 0.0F} if no intersection was found
	 */
	protected final float shape3FTriangleMesh3FIntersectionT(final int shape3FTriangleMesh3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		/*
		 * T0 (Next = NO, Left = T1)
		 *     T1 (Next = T4, Left = T2)
		 *         T2 (Next = T3, Left = L0)
		 *             L0 (Next = L1)
		 *             L1 (Next = L2)
		 *             L2 (Next = T3)
		 *         T3 (Next = T4, Left = L3)
		 *             L3 (Next = L4)
		 *             L4 (Next = L5)
		 *             L5 (Next = T4)
		 *     T4 (Next = NO, Left = L6)
		 *         L6 (Next = L7)
		 *         L7 (Next = L8)
		 *         L8 (Next = NO)
		 */
		
		float t = 0.0F;
		float tMinimumObjectSpace = rayTMinimum;
		float tMaximumObjectSpace = rayTMaximum;
		
		int absoluteOffset = shape3FTriangleMesh3FArrayOffset;
		int relativeOffset = 0;
		
		while(relativeOffset != -1) {
			final int offset = absoluteOffset + relativeOffset;
			final int id = this.shape3FTriangleMesh3FArray[offset + CompiledShape3FCache.TRIANGLE_MESH_3_F_B_V_H_NODE_3_F_OFFSET_ID];
			final int boundingVolumeOffset = this.shape3FTriangleMesh3FArray[offset + CompiledShape3FCache.TRIANGLE_MESH_3_F_B_V_H_NODE_3_F_OFFSET_BOUNDING_VOLUME_OFFSET];
			final int nextOffset = this.shape3FTriangleMesh3FArray[offset + CompiledShape3FCache.TRIANGLE_MESH_3_F_B_V_H_NODE_3_F_OFFSET_NEXT_OFFSET];
			final int leftOffsetOrTriangleCount = this.shape3FTriangleMesh3FArray[offset + CompiledShape3FCache.TRIANGLE_MESH_3_F_B_V_H_NODE_3_F_OFFSET_LEFT_OFFSET_OR_SHAPE_COUNT];
			
			final boolean isIntersectingBoundingVolume = boundingVolume3FAxisAlignedBoundingBox3FContainsOrIntersects(boundingVolumeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
			
			if(isIntersectingBoundingVolume && id == LeafBVHNode3F.ID) {
				for(int i = 0; i < leftOffsetOrTriangleCount; i++) {
					final int triangleOffset = this.shape3FTriangleMesh3FArray[offset + CompiledShape3FCache.TRIANGLE_MESH_3_F_B_V_H_NODE_3_F_OFFSET_LEFT_OFFSET_OR_SHAPE_COUNT + 1 + i];
					
					final float tObjectSpace = this.shape3FTriangle3FIntersectionT(triangleOffset, tMinimumObjectSpace, tMaximumObjectSpace);
					
					if(tObjectSpace > tMinimumObjectSpace && tObjectSpace < tMaximumObjectSpace) {
						this.shape3FTriangleMesh3FArrayToShape3FTriangle3FArray_$private$1[0] = triangleOffset;
						
						tMaximumObjectSpace = tObjectSpace;
						
						t = tObjectSpace;
					}
				}
				
				relativeOffset = nextOffset;
			} else if(isIntersectingBoundingVolume && id == TreeBVHNode3F.ID) {
				relativeOffset = leftOffsetOrTriangleCount;
			} else {
				relativeOffset = nextOffset;
			}
		}
		
		return t;
	}
	
	/**
	 * Computes the intersection properties for the triangle at offset {@code shape3FTriangleMesh3FArrayToShape3FTriangle3FArray_$private$1[0]} in the triangle mesh.
	 * 
	 * @param t the parametric distance to the triangle mesh
	 * @param primitiveIndex the index of the primitive
	 */
	protected final void shape3FTriangleMesh3FIntersectionComputeLHS(final float t, final int primitiveIndex) {
		final int shape3FTriangle3FArrayOffset = this.shape3FTriangleMesh3FArrayToShape3FTriangle3FArray_$private$1[0];
		
		if(shape3FTriangle3FArrayOffset != -1) {
			shape3FTriangle3FIntersectionComputeLHS(t, primitiveIndex, shape3FTriangle3FArrayOffset);
		}
	}
	
	/**
	 * Computes the intersection properties for the triangle at offset {@code shape3FTriangleMesh3FArrayToShape3FTriangle3FArray_$private$1[0]} in the triangle mesh.
	 * 
	 * @param t the parametric distance to the triangle mesh
	 * @param primitiveIndex the index of the primitive
	 */
	protected final void shape3FTriangleMesh3FIntersectionComputeRHS(final float t, final int primitiveIndex) {
		final int shape3FTriangle3FArrayOffset = this.shape3FTriangleMesh3FArrayToShape3FTriangle3FArray_$private$1[0];
		
		if(shape3FTriangle3FArrayOffset != -1) {
			shape3FTriangle3FIntersectionComputeRHS(t, primitiveIndex, shape3FTriangle3FArrayOffset);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Vector3F ////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, both vectors are in the same hemisphere, {@code false} otherwise.
	 * 
	 * @param component1LHS the value of component 1 for the vector on the left-hand side
	 * @param component2LHS the value of component 2 for the vector on the left-hand side
	 * @param component3LHS the value of component 3 for the vector on the left-hand side
	 * @param component1RHS the value of component 1 for the vector on the right-hand side
	 * @param component2RHS the value of component 2 for the vector on the right-hand side
	 * @param component3RHS the value of component 3 for the vector on the right-hand side
	 * @return {@code true} if, and only if, both vectors are in the same hemisphere, {@code false} otherwise
	 */
	protected final boolean vector3FSameHemisphere(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		return vector3FDotProduct(component1LHS, component2LHS, component3LHS, component1RHS, component2RHS, component3RHS) > 0.0F;
	}
	
	/**
	 * Returns {@code true} if, and only if, both vectors are in the same hemisphere, {@code false} otherwise.
	 * <p>
	 * This method only operates on the Z-component (component 3), just like PBRT.
	 * 
	 * @param component1LHS the value of component 1 for the vector on the left-hand side
	 * @param component2LHS the value of component 2 for the vector on the left-hand side
	 * @param component3LHS the value of component 3 for the vector on the left-hand side
	 * @param component1RHS the value of component 1 for the vector on the right-hand side
	 * @param component2RHS the value of component 2 for the vector on the right-hand side
	 * @param component3RHS the value of component 3 for the vector on the right-hand side
	 * @return {@code true} if, and only if, both vectors are in the same hemisphere, {@code false} otherwise
	 */
	@SuppressWarnings("static-method")
	protected final boolean vector3FSameHemisphereZ(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		return component3LHS * component3RHS > 0.0F;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * Returns {@code true} if, and only if, the vector was set, {@code false} otherwise.
	 * <p>
	 * The vector is constructed as the refraction vector of the direction vector represented by {@code directionX}, {@code directionY} and {@code directionZ} with regards to the normal vector represented by {@code normalX}, {@code normalY}
	 * and {@code normalZ}.
	 * 
	 * @param directionX the X-component of the direction vector
	 * @param directionY the Y-component of the direction vector
	 * @param directionZ the Z-component of the direction vector
	 * @param normalX the X-component of the normal vector
	 * @param normalY the Y-component of the normal vector
	 * @param normalZ the Z-component of the normal vector
	 * @param eta the index of refraction (IOR)
	 * @return {@code true} if, and only if, the vector was set, {@code false} otherwise
	 */
	protected final boolean vector3FSetRefraction(final float directionX, final float directionY, final float directionZ, final float normalX, final float normalY, final float normalZ, final float eta) {
		final float cosThetaI = vector3FDotProduct(directionX, directionY, directionZ, normalX, normalY, normalZ);
		final float sinThetaISquared = max(0.0F, 1.0F - cosThetaI * cosThetaI);
		final float sinThetaTSquared = eta * eta * sinThetaISquared;
		final float cosThetaT = sqrt(1.0F - sinThetaTSquared);
		
		final boolean isTotalInternalReflection = sinThetaTSquared >= 1.0F;
		
		if(isTotalInternalReflection) {
			return false;
		}
		
		final float refractionDirectionX = -directionX * eta + normalX * (eta * cosThetaI - cosThetaT);
		final float refractionDirectionY = -directionY * eta + normalY * (eta * cosThetaI - cosThetaT);
		final float refractionDirectionZ = -directionZ * eta + normalZ * (eta * cosThetaI - cosThetaT);
		final float refractionDirectionLengthReciprocal = vector3FLengthReciprocal(refractionDirectionX, refractionDirectionY, refractionDirectionZ);
		final float refractionDirectionNormalizedX = refractionDirectionX * refractionDirectionLengthReciprocal;
		final float refractionDirectionNormalizedY = refractionDirectionY * refractionDirectionLengthReciprocal;
		final float refractionDirectionNormalizedZ = refractionDirectionZ * refractionDirectionLengthReciprocal;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = refractionDirectionNormalizedX;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = refractionDirectionNormalizedY;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = refractionDirectionNormalizedZ;
		
		return true;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * Returns {@code true} if, and only if, the vector was set, {@code false} otherwise.
	 * <p>
	 * The vector is constructed as the refraction vector of the direction vector represented by {@code directionX}, {@code directionY} and {@code directionZ} with regards to the normal vector represented by {@code normalX}, {@code normalY}
	 * and {@code normalZ}.
	 * 
	 * @param directionX the X-component of the direction vector
	 * @param directionY the Y-component of the direction vector
	 * @param directionZ the Z-component of the direction vector
	 * @param normalX the X-component of the normal vector
	 * @param normalY the Y-component of the normal vector
	 * @param normalZ the Z-component of the normal vector
	 * @param eta the index of refraction (IOR)
	 * @return {@code true} if, and only if, the vector was set, {@code false} otherwise
	 */
	protected final boolean vector3FSetRefraction2(final float directionX, final float directionY, final float directionZ, final float normalX, final float normalY, final float normalZ, final float eta) {
		final float cosThetaI = vector3FDotProduct(directionX, directionY, directionZ, normalX, normalY, normalZ);
		final float sinThetaISquared = 1.0F - cosThetaI * cosThetaI;
		final float sinThetaTSquared = 1.0F - eta * eta * sinThetaISquared;
		final float cosThetaT = sqrt(sinThetaTSquared);
		
		final boolean isTotalInternalReflection = sinThetaTSquared < 0.0F;
		
		if(isTotalInternalReflection) {
			return false;
		}
		
		final float refractionDirectionX = directionX * eta - normalX * (eta * cosThetaI + cosThetaT);
		final float refractionDirectionY = directionY * eta - normalY * (eta * cosThetaI + cosThetaT);
		final float refractionDirectionZ = directionZ * eta - normalZ * (eta * cosThetaI + cosThetaT);
		final float refractionDirectionLengthReciprocal = vector3FLengthReciprocal(refractionDirectionX, refractionDirectionY, refractionDirectionZ);
		final float refractionDirectionNormalizedX = refractionDirectionX * refractionDirectionLengthReciprocal;
		final float refractionDirectionNormalizedY = refractionDirectionY * refractionDirectionLengthReciprocal;
		final float refractionDirectionNormalizedZ = refractionDirectionZ * refractionDirectionLengthReciprocal;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = refractionDirectionNormalizedX;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = refractionDirectionNormalizedY;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = refractionDirectionNormalizedZ;
		
		return true;
	}
	
	/**
	 * Returns the cosine of the angle phi.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the cosine of the angle phi
	 */
	protected final float vector3FCosPhi(final float component1, final float component2, final float component3) {
		final float sinTheta = vector3FSinTheta(component1, component2, component3);
		
		return checkIsZero(sinTheta) ? 1.0F : saturateF(component1 / sinTheta, -1.0F, 1.0F);
	}
	
	/**
	 * Returns the cosine of the angle phi in squared form.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the cosine of the angle phi in squared form
	 */
	protected final float vector3FCosPhiSquared(final float component1, final float component2, final float component3) {
		return vector3FCosPhi(component1, component2, component3) * vector3FCosPhi(component1, component2, component3);
	}
	
	/**
	 * Returns the cosine of the angle theta.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the cosine of the angle theta
	 */
	@SuppressWarnings("static-method")
	protected final float vector3FCosTheta(final float component1, final float component2, final float component3) {
		return component3;
	}
	
	/**
	 * Returns the cosine of the angle theta in absolute form.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the cosine of the angle theta in absolute form
	 */
	protected final float vector3FCosThetaAbs(final float component1, final float component2, final float component3) {
		return abs(vector3FCosTheta(component1, component2, component3));
	}
	
	/**
	 * Returns the cosine of the angle theta in quartic form.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the cosine of the angle theta in quartic form
	 */
	protected final float vector3FCosThetaQuartic(final float component1, final float component2, final float component3) {
		return vector3FCosThetaSquared(component1, component2, component3) * vector3FCosThetaSquared(component1, component2, component3);
	}
	
	/**
	 * Returns the cosine of the angle theta in squared form.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the cosine of the angle theta in squared form
	 */
	protected final float vector3FCosThetaSquared(final float component1, final float component2, final float component3) {
		return vector3FCosTheta(component1, component2, component3) * vector3FCosTheta(component1, component2, component3);
	}
	
	/**
	 * Returns the dot product between the vector represented by {@code component1LHS}, {@code component2LHS} and {@code component3LHS} and the vector represented by {@code component1RHS}, {@code component2RHS} and {@code component3RHS}.
	 * 
	 * @param component1LHS the value of component 1 for the vector on the left hand side
	 * @param component2LHS the value of component 2 for the vector on the left hand side
	 * @param component3LHS the value of component 3 for the vector on the left hand side
	 * @param component1RHS the value of component 1 for the vector on the right hand side
	 * @param component2RHS the value of component 2 for the vector on the right hand side
	 * @param component3RHS the value of component 3 for the vector on the right hand side
	 * @return the dot product between the vector represented by {@code component1LHS}, {@code component2LHS} and {@code component3LHS} and the vector represented by {@code component1RHS}, {@code component2RHS} and {@code component3RHS}
	 */
	@SuppressWarnings("static-method")
	protected final float vector3FDotProduct(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		return component1LHS * component1RHS + component2LHS * component2RHS + component3LHS * component3RHS;
	}
	
	/**
	 * Returns the value of component 1 in {@link #vector3FArray_$private$3}.
	 * 
	 * @return the value of component 1 in {@link #vector3FArray_$private$3}
	 */
	protected final float vector3FGetComponent1() {
		return this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
	}
	
	/**
	 * Returns the value of component 2 in {@link #vector3FArray_$private$3}.
	 * 
	 * @return the value of component 2 in {@link #vector3FArray_$private$3}
	 */
	protected final float vector3FGetComponent2() {
		return this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
	}
	
	/**
	 * Returns the value of component 3 in {@link #vector3FArray_$private$3}.
	 * 
	 * @return the value of component 3 in {@link #vector3FArray_$private$3}
	 */
	protected final float vector3FGetComponent3() {
		return this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
	}
	
	/**
	 * Returns the length of the vector represented by {@code component1}, {@code component2} and {@code component3}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 * @return the length of the vector represented by {@code component1}, {@code component2} and {@code component3}
	 */
	protected final float vector3FLength(final float component1, final float component2, final float component3) {
		return sqrt(vector3FLengthSquared(component1, component2, component3));
	}
	
	/**
	 * Returns the reciprocal (or inverse) length of the vector represented by {@code component1}, {@code component2} and {@code component3}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 * @return the reciprocal (or inverse) length of the vector represented by {@code component1}, {@code component2} and {@code component3}
	 */
	protected final float vector3FLengthReciprocal(final float component1, final float component2, final float component3) {
		return rsqrt(vector3FLengthSquared(component1, component2, component3));
	}
	
	/**
	 * Returns the squared length of the vector represented by {@code component1}, {@code component2} and {@code component3}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 * @return the squared length of the vector represented by {@code component1}, {@code component2} and {@code component3}
	 */
	@SuppressWarnings("static-method")
	protected final float vector3FLengthSquared(final float component1, final float component2, final float component3) {
		return component1 * component1 + component2 * component2 + component3 * component3;
	}
	
	/**
	 * Returns the sine of the angle phi.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the sine of the angle phi
	 */
	protected final float vector3FSinPhi(final float component1, final float component2, final float component3) {
		final float sinTheta = vector3FSinTheta(component1, component2, component3);
		
		return checkIsZero(sinTheta) ? 0.0F : saturateF(component2 / sinTheta, -1.0F, 1.0F);
	}
	
	/**
	 * Returns the sine of the angle phi in squared form.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the sine of the angle phi in squared form
	 */
	protected final float vector3FSinPhiSquared(final float component1, final float component2, final float component3) {
		return vector3FSinPhi(component1, component2, component3) * vector3FSinPhi(component1, component2, component3);
	}
	
	/**
	 * Returns the sine of the angle theta.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the sine of the angle theta
	 */
	protected final float vector3FSinTheta(final float component1, final float component2, final float component3) {
		return sqrt(vector3FSinThetaSquared(component1, component2, component3));
	}
	
	/**
	 * Returns the sine of the angle theta in squared form.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the sine of the angle theta in squared form
	 */
	protected final float vector3FSinThetaSquared(final float component1, final float component2, final float component3) {
		return max(0.0F, 1.0F - vector3FCosThetaSquared(component1, component2, component3));
	}
	
	/**
	 * Returns the spherical phi angle.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the spherical phi angle
	 */
	protected final float vector3FSphericalPhi(final float component1, final float component2, final float component3) {
		return addIfLessThanThreshold(atan2(component2, component1), 0.0F, PI_MULTIPLIED_BY_2);
	}
	
	/**
	 * Returns the spherical theta angle.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the spherical theta angle
	 */
	protected final float vector3FSphericalTheta(final float component1, final float component2, final float component3) {
		return acos(saturateF(component3, -1.0F, 1.0F));
	}
	
	/**
	 * Returns the tangent of the angle theta.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the tangent of the angle theta
	 */
	protected final float vector3FTanTheta(final float component1, final float component2, final float component3) {
		return vector3FSinTheta(component1, component2, component3) / vector3FCosTheta(component1, component2, component3);
	}
	
	/**
	 * Returns the tangent of the angle theta in absolute form.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the tangent of the angle theta in absolute form
	 */
	protected final float vector3FTanThetaAbs(final float component1, final float component2, final float component3) {
		return abs(vector3FTanTheta(component1, component2, component3));
	}
	
	/**
	 * Returns the tangent of the angle theta in squared form.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the tangent of the angle theta in squared form
	 */
	protected final float vector3FTanThetaSquared(final float component1, final float component2, final float component3) {
		return vector3FSinThetaSquared(component1, component2, component3) / vector3FCosThetaSquared(component1, component2, component3);
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed using the vector represented by {@code component1}, {@code component2} and {@code component3}.
	 * 
	 * @param component1 the value of component 1 of the vector
	 * @param component2 the value of component 2 of the vector
	 * @param component3 the value of component 3 of the vector
	 */
	protected final void vector3FSet(final float component1, final float component2, final float component3) {
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = component1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = component2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = component3;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by perturbing the normal, which is represented by {@code normalX}, {@code normalY} and {@code normalZ}, with a direction sampled using a hemisphere cosine distribution.
	 * 
	 * @param normalX the X-component of the normal vector
	 * @param normalY the Y-component of the normal vector
	 * @param normalZ the Z-component of the normal vector
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 */
	protected final void vector3FSetDiffuseReflection(final float normalX, final float normalY, final float normalZ, final float u, final float v) {
		orthonormalBasis33FSetFromW(normalX, normalY, normalZ);
		
		vector3FSetSampleHemisphereCosineDistribution2(u, v);
		vector3FSetOrthonormalBasis33FTransformNormalizeFromVector3F();
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed to point in the direction of the spherical coordinates {@code u} and {@code v}.
	 * 
	 * @param u the spherical U-coordinate
	 * @param v the spherical V-coordinate
	 */
	protected final void vector3FSetDirectionSpherical2(final float u, final float v) {
		vector3FSetDirectionSpherical3(sin(v * PI), cos(v * PI), u * PI_MULTIPLIED_BY_2);
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed to point in the direction of the spherical coordinates given by {@code sinTheta}, {@code cosTheta} and {@code phi}.
	 * 
	 * @param sinTheta the sine of the angle theta
	 * @param cosTheta the cosine of the angle theta
	 * @param phi the angle phi
	 */
	protected final void vector3FSetDirectionSpherical3(final float sinTheta, final float cosTheta, final float phi) {
		vector3FSet(sinTheta * cos(phi), sinTheta * sin(phi), cosTheta);
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by negating the vector represented by {@code component1Direction}, {@code component2Direction} and {@code component3Direction} if, and only if, the dot product between the vector represented by {@code component1LHS},
	 * {@code component2LHS} and {@code component3LHS} and the vector represented by {@code component1RHS}, {@code component2RHS} and {@code component3RHS} is less than {@code 0.0F}. Otherwise, its current values will be used.
	 * 
	 * @param component1LHS the value of component 1 of the vector on the left-hand side
	 * @param component2LHS the value of component 2 of the vector on the left-hand side
	 * @param component3LHS the value of component 3 of the vector on the left-hand side
	 * @param component1RHS the value of component 1 of the vector on the right-hand side
	 * @param component2RHS the value of component 2 of the vector on the right-hand side
	 * @param component3RHS the value of component 3 of the vector on the right-hand side
	 * @param component1Direction the value of component 1 of the vector to set, whether it is negated or not
	 * @param component2Direction the value of component 2 of the vector to set, whether it is negated or not
	 * @param component3Direction the value of component 3 of the vector to set, whether it is negated or not
	 */
	protected final void vector3FSetFaceForwardDirection(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS, final float component1Direction, final float component2Direction, final float component3Direction) {
		if(vector3FDotProduct(component1LHS, component2LHS, component3LHS, component1RHS, component2RHS, component3RHS) < 0.0F) {
			vector3FSet(-component1Direction, -component2Direction, -component3Direction);
		} else {
			vector3FSet(+component1Direction, +component2Direction, +component3Direction);
		}
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by negating the vector represented by {@code component1LHS}, {@code component2LHS} and {@code component3LHS} if, and only if, the dot product between that vector and the vector represented by {@code component1RHS},
	 * {@code component2RHS} and {@code component3RHS} is less than {@code 0.0F}. Otherwise, its current values will be used.
	 * 
	 * @param component1LHS the value of component 1 of the vector on the left-hand side
	 * @param component2LHS the value of component 2 of the vector on the left-hand side
	 * @param component3LHS the value of component 3 of the vector on the left-hand side
	 * @param component1RHS the value of component 1 of the vector on the right-hand side
	 * @param component2RHS the value of component 2 of the vector on the right-hand side
	 * @param component3RHS the value of component 3 of the vector on the right-hand side
	 */
	protected final void vector3FSetFaceForwardLHS(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		if(vector3FDotProduct(component1LHS, component2LHS, component3LHS, component1RHS, component2RHS, component3RHS) < 0.0F) {
			vector3FSet(-component1LHS, -component2LHS, -component3LHS);
		} else {
			vector3FSet(+component1LHS, +component2LHS, +component3LHS);
		}
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by negating the vector represented by {@code component1LHS}, {@code component2LHS} and {@code component3LHS} if, and only if, the dot product between that vector and the vector represented by {@code component1RHS},
	 * {@code component2RHS} and {@code component3RHS} is greater than or equal to {@code 0.0F}. Otherwise, its current values will be used.
	 * 
	 * @param component1LHS the value of component 1 of the vector on the left-hand side
	 * @param component2LHS the value of component 2 of the vector on the left-hand side
	 * @param component3LHS the value of component 3 of the vector on the left-hand side
	 * @param component1RHS the value of component 1 of the vector on the right-hand side
	 * @param component2RHS the value of component 2 of the vector on the right-hand side
	 * @param component3RHS the value of component 3 of the vector on the right-hand side
	 */
	protected final void vector3FSetFaceForwardLHSNegated(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		if(vector3FDotProduct(component1LHS, component2LHS, component3LHS, component1RHS, component2RHS, component3RHS) < 0.0F) {
			vector3FSet(+component1LHS, +component2LHS, +component3LHS);
		} else {
			vector3FSet(-component1LHS, -component2LHS, -component3LHS);
		}
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by negating component 3 of the vector represented by {@code component1RHS}, {@code component2RHS} and {@code component3RHS} if, and only if, {@code component3LHS} is less than {@code 0.0F}. Otherwise, its current value
	 * will be used.
	 * 
	 * @param component1LHS the value of component 1 of the vector on the left-hand side
	 * @param component2LHS the value of component 2 of the vector on the left-hand side
	 * @param component3LHS the value of component 3 of the vector on the left-hand side
	 * @param component1RHS the value of component 1 of the vector on the right-hand side
	 * @param component2RHS the value of component 2 of the vector on the right-hand side
	 * @param component3RHS the value of component 3 of the vector on the right-hand side
	 */
	protected final void vector3FSetFaceForwardRHSComponent3(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		vector3FSet(+component1RHS, +component2RHS, component3LHS < 0.0F ? -component3RHS : +component3RHS);
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by negating component 3 of the vector represented by {@code component1RHS}, {@code component2RHS} and {@code component3RHS} if, and only if, {@code component3LHS} is greater than {@code 0.0F}. Otherwise, its current
	 * value will be used.
	 * 
	 * @param component1LHS the value of component 1 of the vector on the left-hand side
	 * @param component2LHS the value of component 2 of the vector on the left-hand side
	 * @param component3LHS the value of component 3 of the vector on the left-hand side
	 * @param component1RHS the value of component 1 of the vector on the right-hand side
	 * @param component2RHS the value of component 2 of the vector on the right-hand side
	 * @param component3RHS the value of component 3 of the vector on the right-hand side
	 */
	protected final void vector3FSetFaceForwardRHSComponent3Negated(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		vector3FSet(+component1RHS, +component2RHS, component3LHS > 0.0F ? -component3RHS : +component3RHS);
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by perturbing the specular reflection direction with a direction sampled using a hemisphere power-cosine distribution.
	 * <p>
	 * The specular reflection direction is constructed by calling {@link #vector3FSetSpecularReflection(float, float, float, float, float, float)}, with the parameter arguments {@code directionX}, {@code directionY}, {@code directionZ},
	 * {@code normalX}, {@code normalY} and {@code normalZ}.
	 * 
	 * @param directionX the X-component of the direction vector
	 * @param directionY the Y-component of the direction vector
	 * @param directionZ the Z-component of the direction vector
	 * @param normalX the X-component of the normal vector
	 * @param normalY the Y-component of the normal vector
	 * @param normalZ the Z-component of the normal vector
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param exponent the exponent to use
	 */
	protected final void vector3FSetGlossyReflection(final float directionX, final float directionY, final float directionZ, final float normalX, final float normalY, final float normalZ, final float u, final float v, final float exponent) {
		vector3FSetSpecularReflection(directionX, directionY, directionZ, normalX, normalY, normalZ);
		
		orthonormalBasis33FSetVector3F();
		
		vector3FSetSampleHemispherePowerCosineDistribution(u, v, exponent);
		vector3FSetOrthonormalBasis33FTransformNormalizeFromVector3F();
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by perturbing the specular reflection direction with a direction sampled using a hemisphere power-cosine distribution.
	 * <p>
	 * The specular reflection direction is constructed by calling {@link #vector3FSetSpecularReflectionFacingSurface(float, float, float, float, float, float)}, with the parameter arguments {@code directionX}, {@code directionY}, {@code directionZ},
	 * {@code normalX}, {@code normalY} and {@code normalZ}.
	 * 
	 * @param directionX the X-component of the direction vector
	 * @param directionY the Y-component of the direction vector
	 * @param directionZ the Z-component of the direction vector
	 * @param normalX the X-component of the normal vector
	 * @param normalY the Y-component of the normal vector
	 * @param normalZ the Z-component of the normal vector
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param exponent the exponent to use
	 */
	protected final void vector3FSetGlossyReflectionFacingSurface(final float directionX, final float directionY, final float directionZ, final float normalX, final float normalY, final float normalZ, final float u, final float v, final float exponent) {
		vector3FSetSpecularReflectionFacingSurface(directionX, directionY, directionZ, normalX, normalY, normalZ);
		
		orthonormalBasis33FSetVector3F();
		
		vector3FSetSampleHemispherePowerCosineDistribution(u, v, exponent);
		vector3FSetOrthonormalBasis33FTransformNormalizeFromVector3F();
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed using {@code vector3FSet(normalX, normalY, normalZ)} or {@code vector3FSetNormalize(outgoingX - incomingX, outgoingY - incomingY, outgoingZ - incomingZ)}, as
	 * {@code vector3FDotProduct(outgoingX, outgoingY, outgoingZ, incomingX, incomingY, incomingZ)} is greater than {@code 0.999F} or less than or equal to {@code 0.999F}, respectively.
	 * 
	 * @param outgoingX the X-component of the vector that points in the opposite direction of the ray
	 * @param outgoingY the Y-component of the vector that points in the opposite direction of the ray
	 * @param outgoingZ the Z-component of the vector that points in the opposite direction of the ray
	 * @param normalX the X-component of the vector that points in the direction of the surface normal
	 * @param normalY the Y-component of the vector that points in the direction of the surface normal
	 * @param normalZ the Z-component of the vector that points in the direction of the surface normal
	 * @param incomingX the X-component of the vector that points in the direction of the light source to the surface intersection point
	 * @param incomingY the Y-component of the vector that points in the direction of the light source to the surface intersection point
	 * @param incomingZ the Z-component of the vector that points in the direction of the light source to the surface intersection point
	 */
	protected final void vector3FSetHalf(final float outgoingX, final float outgoingY, final float outgoingZ, final float normalX, final float normalY, final float normalZ, final float incomingX, final float incomingY, final float incomingZ) {
		if(vector3FDotProduct(outgoingX, outgoingY, outgoingZ, incomingX, incomingY, incomingZ) > 0.999F) {
			vector3FSet(normalX, normalY, normalZ);
		} else {
			vector3FSetNormalize(outgoingX - incomingX, outgoingY - incomingY, outgoingZ - incomingZ);
		}
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector represented by {@code component1}, {@code component2} and {@code component3} with the supplied matrix.
	 * 
	 * @param element11 the value of the element at row 1 and column 1 in the matrix
	 * @param element12 the value of the element at row 1 and column 2 in the matrix
	 * @param element13 the value of the element at row 1 and column 3 in the matrix
	 * @param element21 the value of the element at row 2 and column 1 in the matrix
	 * @param element22 the value of the element at row 2 and column 2 in the matrix
	 * @param element23 the value of the element at row 2 and column 3 in the matrix
	 * @param element31 the value of the element at row 3 and column 1 in the matrix
	 * @param element32 the value of the element at row 3 and column 2 in the matrix
	 * @param element33 the value of the element at row 3 and column 3 in the matrix
	 * @param component1 the value of component 1 of the vector
	 * @param component2 the value of component 2 of the vector
	 * @param component3 the value of component 3 of the vector
	 */
	protected final void vector3FSetMatrix44FTransform(final float element11, final float element12, final float element13, final float element21, final float element22, final float element23, final float element31, final float element32, final float element33, final float component1, final float component2, final float component3) {
		final float newComponent1 = element11 * component1 + element12 * component2 + element13 * component3;
		final float newComponent2 = element21 * component1 + element22 * component2 + element23 * component3;
		final float newComponent3 = element31 * component1 + element32 * component2 + element33 * component3;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = newComponent1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = newComponent2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = newComponent3;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector represented by {@code component1}, {@code component2} and {@code component3} with the supplied matrix and normalizing it.
	 * 
	 * @param element11 the value of the element at row 1 and column 1 in the matrix
	 * @param element12 the value of the element at row 1 and column 2 in the matrix
	 * @param element13 the value of the element at row 1 and column 3 in the matrix
	 * @param element21 the value of the element at row 2 and column 1 in the matrix
	 * @param element22 the value of the element at row 2 and column 2 in the matrix
	 * @param element23 the value of the element at row 2 and column 3 in the matrix
	 * @param element31 the value of the element at row 3 and column 1 in the matrix
	 * @param element32 the value of the element at row 3 and column 2 in the matrix
	 * @param element33 the value of the element at row 3 and column 3 in the matrix
	 * @param component1 the value of component 1 of the vector
	 * @param component2 the value of component 2 of the vector
	 * @param component3 the value of component 3 of the vector
	 */
	protected final void vector3FSetMatrix44FTransformNormalize(final float element11, final float element12, final float element13, final float element21, final float element22, final float element23, final float element31, final float element32, final float element33, final float component1, final float component2, final float component3) {
		final float newComponent1 = element11 * component1 + element12 * component2 + element13 * component3;
		final float newComponent2 = element21 * component1 + element22 * component2 + element23 * component3;
		final float newComponent3 = element31 * component1 + element32 * component2 + element33 * component3;
		final float newLengthReciprocal = vector3FLengthReciprocal(newComponent1, newComponent2, newComponent3);
		final float newNormalizedComponent1 = newComponent1 * newLengthReciprocal;
		final float newNormalizedComponent2 = newComponent2 * newLengthReciprocal;
		final float newNormalizedComponent3 = newComponent3 * newLengthReciprocal;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = newNormalizedComponent1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = newNormalizedComponent2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = newNormalizedComponent3;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector represented by {@code component1}, {@code component2} and {@code component3} with the supplied matrix in transpose order.
	 * 
	 * @param element11 the value of the element at row 1 and column 1 in the matrix
	 * @param element12 the value of the element at row 1 and column 2 in the matrix
	 * @param element13 the value of the element at row 1 and column 3 in the matrix
	 * @param element21 the value of the element at row 2 and column 1 in the matrix
	 * @param element22 the value of the element at row 2 and column 2 in the matrix
	 * @param element23 the value of the element at row 2 and column 3 in the matrix
	 * @param element31 the value of the element at row 3 and column 1 in the matrix
	 * @param element32 the value of the element at row 3 and column 2 in the matrix
	 * @param element33 the value of the element at row 3 and column 3 in the matrix
	 * @param component1 the value of component 1 of the vector
	 * @param component2 the value of component 2 of the vector
	 * @param component3 the value of component 3 of the vector
	 */
	protected final void vector3FSetMatrix44FTransformTranspose(final float element11, final float element12, final float element13, final float element21, final float element22, final float element23, final float element31, final float element32, final float element33, final float component1, final float component2, final float component3) {
		final float newComponent1 = element11 * component1 + element21 * component2 + element31 * component3;
		final float newComponent2 = element12 * component1 + element22 * component2 + element32 * component3;
		final float newComponent3 = element13 * component1 + element23 * component2 + element33 * component3;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = newComponent1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = newComponent2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = newComponent3;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector represented by {@code component1}, {@code component2} and {@code component3} with the supplied matrix in transpose order and normalizing it.
	 * 
	 * @param element11 the value of the element at row 1 and column 1 in the matrix
	 * @param element12 the value of the element at row 1 and column 2 in the matrix
	 * @param element13 the value of the element at row 1 and column 3 in the matrix
	 * @param element21 the value of the element at row 2 and column 1 in the matrix
	 * @param element22 the value of the element at row 2 and column 2 in the matrix
	 * @param element23 the value of the element at row 2 and column 3 in the matrix
	 * @param element31 the value of the element at row 3 and column 1 in the matrix
	 * @param element32 the value of the element at row 3 and column 2 in the matrix
	 * @param element33 the value of the element at row 3 and column 3 in the matrix
	 * @param component1 the value of component 1 of the vector
	 * @param component2 the value of component 2 of the vector
	 * @param component3 the value of component 3 of the vector
	 */
	protected final void vector3FSetMatrix44FTransformTransposeNormalize(final float element11, final float element12, final float element13, final float element21, final float element22, final float element23, final float element31, final float element32, final float element33, final float component1, final float component2, final float component3) {
		final float newComponent1 = element11 * component1 + element21 * component2 + element31 * component3;
		final float newComponent2 = element12 * component1 + element22 * component2 + element32 * component3;
		final float newComponent3 = element13 * component1 + element23 * component2 + element33 * component3;
		final float newLengthReciprocal = vector3FLengthReciprocal(newComponent1, newComponent2, newComponent3);
		final float newNormalizedComponent1 = newComponent1 * newLengthReciprocal;
		final float newNormalizedComponent2 = newComponent2 * newLengthReciprocal;
		final float newNormalizedComponent3 = newComponent3 * newLengthReciprocal;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = newNormalizedComponent1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = newNormalizedComponent2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = newNormalizedComponent3;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by normalizing the vector represented by {@code component1}, {@code component2} and {@code component3}.
	 * 
	 * @param component1 the value of component 1 of the vector
	 * @param component2 the value of component 2 of the vector
	 * @param component3 the value of component 3 of the vector
	 */
	protected final void vector3FSetNormalize(final float component1, final float component2, final float component3) {
		final float length = vector3FLength(component1, component2, component3);
		
		final boolean isLengthGTEThreshold = length >= 0.99999982F;
		final boolean isLengthLTEThreshold = length <= 1.00000012F;
		
		if(isLengthGTEThreshold && isLengthLTEThreshold) {
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = component1;
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = component2;
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = component3;
		} else {
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = component1 / length;
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = component2 / length;
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = component3 / length;
		}
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector represented by {@code component1}, {@code component2} and {@code component3} with the orthonormal basis in {@link #orthonormalBasis33FArray_$private$9} and normalizing it.
	 * 
	 * @param component1 the value of component 1 of the vector
	 * @param component2 the value of component 2 of the vector
	 * @param component3 the value of component 3 of the vector
	 */
	protected final void vector3FSetOrthonormalBasis33FTransformNormalize(final float component1, final float component2, final float component3) {
		final float orthonormalBasisUX = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0];
		final float orthonormalBasisUY = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1];
		final float orthonormalBasisUZ = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2];
		final float orthonormalBasisVX = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0];
		final float orthonormalBasisVY = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1];
		final float orthonormalBasisVZ = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2];
		final float orthonormalBasisWX = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0];
		final float orthonormalBasisWY = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1];
		final float orthonormalBasisWZ = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2];
		
		final float newComponent1 = orthonormalBasisUX * component1 + orthonormalBasisVX * component2 + orthonormalBasisWX * component3;
		final float newComponent2 = orthonormalBasisUY * component1 + orthonormalBasisVY * component2 + orthonormalBasisWY * component3;
		final float newComponent3 = orthonormalBasisUZ * component1 + orthonormalBasisVZ * component2 + orthonormalBasisWZ * component3;
		final float newLengthReciprocal = vector3FLengthReciprocal(newComponent1, newComponent2, newComponent3);
		final float newNormalizedComponent1 = newComponent1 * newLengthReciprocal;
		final float newNormalizedComponent2 = newComponent2 * newLengthReciprocal;
		final float newNormalizedComponent3 = newComponent3 * newLengthReciprocal;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = newNormalizedComponent1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = newNormalizedComponent2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = newNormalizedComponent3;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector in {@code vector3FArray_$private$3} with the orthonormal basis in {@link #orthonormalBasis33FArray_$private$9} and normalizing it.
	 */
	protected final void vector3FSetOrthonormalBasis33FTransformNormalizeFromVector3F() {
		final float component1 = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float component2 = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float component3 = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
		vector3FSetOrthonormalBasis33FTransformNormalize(component1, component2, component3);
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector represented by {@code component1}, {@code component2} and {@code component3} with the orthonormal basis in {@link #orthonormalBasis33FArray_$private$9} in reverse order and normalizing it.
	 * 
	 * @param component1 the value of component 1 of the vector
	 * @param component2 the value of component 2 of the vector
	 * @param component3 the value of component 3 of the vector
	 */
	protected final void vector3FSetOrthonormalBasis33FTransformReverseNormalize(final float component1, final float component2, final float component3) {
		final float orthonormalBasisUX = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0];
		final float orthonormalBasisUY = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1];
		final float orthonormalBasisUZ = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2];
		final float orthonormalBasisVX = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0];
		final float orthonormalBasisVY = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1];
		final float orthonormalBasisVZ = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2];
		final float orthonormalBasisWX = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0];
		final float orthonormalBasisWY = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1];
		final float orthonormalBasisWZ = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2];
		
		final float newComponent1 = component1 * orthonormalBasisUX + component2 * orthonormalBasisUY + component3 * orthonormalBasisUZ;
		final float newComponent2 = component1 * orthonormalBasisVX + component2 * orthonormalBasisVY + component3 * orthonormalBasisVZ;
		final float newComponent3 = component1 * orthonormalBasisWX + component2 * orthonormalBasisWY + component3 * orthonormalBasisWZ;
		final float newLengthReciprocal = vector3FLengthReciprocal(newComponent1, newComponent2, newComponent3);
		final float newNormalizedComponent1 = newComponent1 * newLengthReciprocal;
		final float newNormalizedComponent2 = newComponent2 * newLengthReciprocal;
		final float newNormalizedComponent3 = newComponent3 * newLengthReciprocal;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = newNormalizedComponent1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = newNormalizedComponent2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = newNormalizedComponent3;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector in {@code vector3FArray_$private$3} with the orthonormal basis in {@link #orthonormalBasis33FArray_$private$9} in reverse order and normalizing it.
	 */
	protected final void vector3FSetOrthonormalBasis33FTransformReverseNormalizeFromVector3F() {
		final float component1 = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float component2 = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float component3 = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
		vector3FSetOrthonormalBasis33FTransformReverseNormalize(component1, component2, component3);
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by sampling a direction on a hemisphere with a cosine distribution.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 */
	protected final void vector3FSetSampleHemisphereCosineDistribution(final float u, final float v) {
		point2FSetSampleDiskUniformDistributionByConcentricMapping(u, v, 1.0F);
		
		final float component1 = point2FGetComponent1();
		final float component2 = point2FGetComponent2();
		final float component3 = sqrt(max(0.0F, 1.0F - component1 * component1 - component2 * component2));
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = component1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = component2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = component3;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by sampling a direction on a hemisphere with a cosine distribution.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 */
	protected final void vector3FSetSampleHemisphereCosineDistribution2(final float u, final float v) {
		final float sinTheta = sqrt(v);
		final float cosTheta = sqrt(1.0F - v);
		final float phi = PI_MULTIPLIED_BY_2 * u;
		
		final float component1 = sinTheta * cos(phi);
		final float component2 = sinTheta * sin(phi);
		final float component3 = cosTheta;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = component1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = component2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = component3;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by sampling a direction on a hemisphere with a power-cosine distribution.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param exponent the exponent to use
	 */
	protected final void vector3FSetSampleHemispherePowerCosineDistribution(final float u, final float v, final float exponent) {
		final float cosTheta = pow(1.0F - u, 1.0F / (exponent + 1.0F));
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float phi = PI_MULTIPLIED_BY_2 * v;
		
		final float component1 = sinTheta * cos(phi);
		final float component2 = sinTheta * sin(phi);
		final float component3 = cosTheta;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = component1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = component2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = component3;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by sampling a direction on a hemisphere with a uniform distribution.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 */
	protected final void vector3FSetSampleHemisphereUniformDistribution(final float u, final float v) {
		final float cosTheta = u;
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float phi = PI_MULTIPLIED_BY_2 * v;
		
		final float component1 = sinTheta * cos(phi);
		final float component2 = sinTheta * sin(phi);
		final float component3 = cosTheta;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = component1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = component2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = component3;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed as the specular reflection vector of the direction vector represented by {@code directionX}, {@code directionY} and {@code directionZ} with regards to the normal vector represented by {@code normalX}, {@code normalY}
	 * and {@code normalZ}.
	 * <p>
	 * This method assumes that the direction vector is pointing away from the surface. This is usually the case for BRDFs and BTDFs.
	 * 
	 * @param directionX the X-component of the direction vector
	 * @param directionY the Y-component of the direction vector
	 * @param directionZ the Z-component of the direction vector
	 * @param normalX the X-component of the normal vector
	 * @param normalY the Y-component of the normal vector
	 * @param normalZ the Z-component of the normal vector
	 */
	protected final void vector3FSetSpecularReflection(final float directionX, final float directionY, final float directionZ, final float normalX, final float normalY, final float normalZ) {
		final float directionDotNormal = vector3FDotProduct(directionX, directionY, directionZ, normalX, normalY, normalZ);
		final float directionDotNormalMultipliedByTwo = directionDotNormal * 2.0F;
		
		final float reflectionX = normalX * directionDotNormalMultipliedByTwo - directionX;
		final float reflectionY = normalY * directionDotNormalMultipliedByTwo - directionY;
		final float reflectionZ = normalZ * directionDotNormalMultipliedByTwo - directionZ;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = reflectionX;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = reflectionY;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = reflectionZ;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed as the specular reflection vector of the direction vector represented by {@code directionX}, {@code directionY} and {@code directionZ} with regards to the normal vector represented by {@code normalX}, {@code normalY}
	 * and {@code normalZ}.
	 * <p>
	 * This method assumes that the direction vector is pointing towards the surface. It is facing it. This is usually the case for the direction of a ray that intersects the surface.
	 * 
	 * @param directionX the X-component of the direction vector
	 * @param directionY the Y-component of the direction vector
	 * @param directionZ the Z-component of the direction vector
	 * @param normalX the X-component of the normal vector
	 * @param normalY the Y-component of the normal vector
	 * @param normalZ the Z-component of the normal vector
	 */
	protected final void vector3FSetSpecularReflectionFacingSurface(final float directionX, final float directionY, final float directionZ, final float normalX, final float normalY, final float normalZ) {
		final float directionDotNormal = vector3FDotProduct(directionX, directionY, directionZ, normalX, normalY, normalZ);
		final float directionDotNormalMultipliedByTwo = directionDotNormal * 2.0F;
		
		final float reflectionX = directionX - normalX * directionDotNormalMultipliedByTwo;
		final float reflectionY = directionY - normalY * directionDotNormalMultipliedByTwo;
		final float reflectionZ = directionZ - normalZ * directionDotNormalMultipliedByTwo;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = reflectionX;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = reflectionY;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = reflectionZ;
	}
}