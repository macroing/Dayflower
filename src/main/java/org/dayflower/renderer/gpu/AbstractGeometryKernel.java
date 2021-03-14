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

import static org.dayflower.utility.Floats.PI_DIVIDED_BY_2;
import static org.dayflower.utility.Floats.PI_DIVIDED_BY_4;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.utility.Floats.PI_RECIPROCAL;

import java.lang.reflect.Field;

import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.geometry.boundingvolume.BoundingSphere3F;
import org.dayflower.geometry.shape.Cone3F;
import org.dayflower.geometry.shape.Cylinder3F;
import org.dayflower.geometry.shape.Disk3F;
import org.dayflower.geometry.shape.Paraboloid3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.geometry.shape.RectangularCuboid3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Torus3F;
import org.dayflower.geometry.shape.Triangle3F;
import org.dayflower.geometry.shape.TriangleMesh3F;

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
	protected static final float DEFAULT_T_MAXIMUM = Float.MAX_VALUE;
	
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
	protected float[] intersectionArray_$private$24;
	
	/**
	 * A {@code float[]} that contains an orthonormal basis that consists of three 3-dimensional vectors.
	 */
	protected float[] orthonormalBasis33FArray_$private$9;
	
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
	 * A {@code float[]} that contains paraboloids.
	 */
	protected float[] shape3FParaboloid3FArray;
	
	/**
	 * A {@code float[]} that contains planes.
	 */
	protected float[] shape3FPlane3FArray;
	
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
		this.intersectionArray_$private$24 = new float[INTERSECTION_ARRAY_SIZE];
		this.orthonormalBasis33FArray_$private$9 = new float[ORTHONORMAL_BASIS_3_3_F_ARRAY_SIZE];
		this.point3FArray_$private$3 = new float[POINT_3_F_ARRAY_SIZE];
		this.ray3FArray_$private$8 = new float[RAY_3_F_ARRAY_SIZE];
		this.shape3FCone3FArray = new float[1];
		this.shape3FCylinder3FArray = new float[1];
		this.shape3FDisk3FArray = new float[1];
		this.shape3FParaboloid3FArray = new float[1];
		this.shape3FPlane3FArray = new float[1];
		this.shape3FRectangularCuboid3FArray = new float[1];
		this.shape3FSphere3FArray = new float[1];
		this.shape3FTorus3FArray = new float[1];
		this.shape3FTriangle3FArray = new float[1];
		this.vector3FArray_$private$3 = new float[VECTOR_3_F_ARRAY_SIZE];
		this.shape3FTriangleMesh3FArray = new int[1];
		this.shape3FTriangleMesh3FArrayToShape3FTriangle3FArray_$private$1 = new int[1];
	}
	
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
		final float axisAlignedBoundingBoxMaximumX = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + AxisAlignedBoundingBox3F.ARRAY_OFFSET_MAXIMUM + 0];
		final float axisAlignedBoundingBoxMaximumY = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + AxisAlignedBoundingBox3F.ARRAY_OFFSET_MAXIMUM + 1];
		final float axisAlignedBoundingBoxMaximumZ = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + AxisAlignedBoundingBox3F.ARRAY_OFFSET_MAXIMUM + 2];
		final float axisAlignedBoundingBoxMinimumX = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + AxisAlignedBoundingBox3F.ARRAY_OFFSET_MINIMUM + 0];
		final float axisAlignedBoundingBoxMinimumY = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + AxisAlignedBoundingBox3F.ARRAY_OFFSET_MINIMUM + 1];
		final float axisAlignedBoundingBoxMinimumZ = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + AxisAlignedBoundingBox3F.ARRAY_OFFSET_MINIMUM + 2];
		
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
		final float axisAlignedBoundingBoxMaximumX = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + AxisAlignedBoundingBox3F.ARRAY_OFFSET_MAXIMUM + 0];
		final float axisAlignedBoundingBoxMaximumY = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + AxisAlignedBoundingBox3F.ARRAY_OFFSET_MAXIMUM + 1];
		final float axisAlignedBoundingBoxMaximumZ = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + AxisAlignedBoundingBox3F.ARRAY_OFFSET_MAXIMUM + 2];
		final float axisAlignedBoundingBoxMinimumX = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + AxisAlignedBoundingBox3F.ARRAY_OFFSET_MINIMUM + 0];
		final float axisAlignedBoundingBoxMinimumY = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + AxisAlignedBoundingBox3F.ARRAY_OFFSET_MINIMUM + 1];
		final float axisAlignedBoundingBoxMinimumZ = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + AxisAlignedBoundingBox3F.ARRAY_OFFSET_MINIMUM + 2];
		
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
		
		if(intersectionTMinimum > rayTMinimum && intersectionTMinimum < rayTMaximum) {
			return true;
		}
		
		if(intersectionTMaximum > rayTMinimum && intersectionTMaximum < rayTMaximum) {
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
	 * Returns {@code true} if, and only if, a point is contained by a given bounding sphere, {@code false} otherwise.
	 * 
	 * @param boundingVolume3FBoundingSphere3FArrayOffset the offset for the bounding sphere in {@link #boundingVolume3FBoundingSphere3FArray}
	 * @param pointX the X-component of the point
	 * @param pointY the Y-component of the point
	 * @param pointZ the Z-component of the point
	 * @return {@code true} if, and only if, a point is contained by a given bounding sphere, {@code false} otherwise
	 */
	protected final boolean boundingVolume3FBoundingSphere3FContains(final int boundingVolume3FBoundingSphere3FArrayOffset, final float pointX, final float pointY, final float pointZ) {
		final float boundingSphereCenterX = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + BoundingSphere3F.ARRAY_OFFSET_CENTER + 0];
		final float boundingSphereCenterY = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + BoundingSphere3F.ARRAY_OFFSET_CENTER + 1];
		final float boundingSphereCenterZ = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + BoundingSphere3F.ARRAY_OFFSET_CENTER + 2];
		final float boundingSphereRadius = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + BoundingSphere3F.ARRAY_OFFSET_RADIUS];
		
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
		final float boundingSphereCenterX = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + BoundingSphere3F.ARRAY_OFFSET_CENTER + 0];
		final float boundingSphereCenterY = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + BoundingSphere3F.ARRAY_OFFSET_CENTER + 1];
		final float boundingSphereCenterZ = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + BoundingSphere3F.ARRAY_OFFSET_CENTER + 2];
		final float boundingSphereRadius = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + BoundingSphere3F.ARRAY_OFFSET_RADIUS];
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
	 * Returns {@code true} if, and only if, the current ray intersects a given triangle mesh in object space, {@code false} otherwise.
	 * 
	 * @param shape3FTriangleMesh3FArrayOffset the offset for the triangle mesh in {@link #shape3FTriangleMesh3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given triangle mesh in object space, {@code false} otherwise
	 */
	protected final boolean shape3FTriangleMesh3FIntersects(final int shape3FTriangleMesh3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return shape3FTriangleMesh3FIntersectionT(shape3FTriangleMesh3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
//	TODO: Add Javadocs!
	protected final boolean vector3FSameHemisphere(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		return vector3FDotProduct(component1LHS, component2LHS, component3LHS, component1RHS, component2RHS, component3RHS) > 0.0F;
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings({"static-method", "unused"})
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
//		PBRT:
//		final float cosThetaI = vector3FDotProduct(directionX, directionY, directionZ, normalX, normalY, normalZ);
//		final float sinThetaISquared = max(0.0F, 1.0F - cosThetaI * cosThetaI);
//		final float sinThetaTSquared = eta * eta * sinThetaISquared;
//		final float cosThetaT = sqrt(1.0F - sinThetaTSquared);
		
//		final boolean isTotalInternalReflection = sinThetaTSquared >= 1.0F;
		
//		Small PT:
		final float cosThetaI = vector3FDotProduct(directionX, directionY, directionZ, normalX, normalY, normalZ);
		final float sinThetaISquared = 1.0F - cosThetaI * cosThetaI;
		final float sinThetaTSquared = 1.0F - eta * eta * sinThetaISquared;
		final float cosThetaT = sqrt(sinThetaTSquared);
		
		final boolean isTotalInternalReflection = sinThetaTSquared < 0.0F;
		
//		PBRT and Small PT:
		if(isTotalInternalReflection) {
			return false;
		}
		
//		PBRT:
//		final float refractionDirectionX = -directionX * eta + normalX * (eta * cosThetaI - cosThetaT);
//		final float refractionDirectionY = -directionY * eta + normalY * (eta * cosThetaI - cosThetaT);
//		final float refractionDirectionZ = -directionZ * eta + normalZ * (eta * cosThetaI - cosThetaT);
//		final float refractionDirectionLengthReciprocal = vector3FLengthReciprocal(refractionDirectionX, refractionDirectionY, refractionDirectionZ);
//		final float refractionDirectionNormalizedX = refractionDirectionX * refractionDirectionLengthReciprocal;
//		final float refractionDirectionNormalizedY = refractionDirectionY * refractionDirectionLengthReciprocal;
//		final float refractionDirectionNormalizedZ = refractionDirectionZ * refractionDirectionLengthReciprocal;
		
//		PBRT and Small PT:
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
		final float axisAlignedBoundingBoxMaximumX = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + AxisAlignedBoundingBox3F.ARRAY_OFFSET_MAXIMUM + 0];
		final float axisAlignedBoundingBoxMaximumY = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + AxisAlignedBoundingBox3F.ARRAY_OFFSET_MAXIMUM + 1];
		final float axisAlignedBoundingBoxMaximumZ = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + AxisAlignedBoundingBox3F.ARRAY_OFFSET_MAXIMUM + 2];
		final float axisAlignedBoundingBoxMinimumX = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + AxisAlignedBoundingBox3F.ARRAY_OFFSET_MINIMUM + 0];
		final float axisAlignedBoundingBoxMinimumY = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + AxisAlignedBoundingBox3F.ARRAY_OFFSET_MINIMUM + 1];
		final float axisAlignedBoundingBoxMinimumZ = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffset + AxisAlignedBoundingBox3F.ARRAY_OFFSET_MINIMUM + 2];
		
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
		final float boundingSphereCenterX = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + BoundingSphere3F.ARRAY_OFFSET_CENTER + 0];
		final float boundingSphereCenterY = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + BoundingSphere3F.ARRAY_OFFSET_CENTER + 1];
		final float boundingSphereCenterZ = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + BoundingSphere3F.ARRAY_OFFSET_CENTER + 2];
		final float boundingSphereRadius = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + BoundingSphere3F.ARRAY_OFFSET_RADIUS];
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
	
	/**
	 * Returns the value of component 1 of the U-direction of the orthonormal basis for geometry in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 1 of the U-direction of the orthonormal basis for geometry in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetOrthonormalBasisGUComponent1() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0];
	}
	
	/**
	 * Returns the value of component 2 of the U-direction of the orthonormal basis for geometry in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 2 of the U-direction of the orthonormal basis for geometry in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetOrthonormalBasisGUComponent2() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1];
	}
	
	/**
	 * Returns the value of component 3 of the U-direction of the orthonormal basis for geometry in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 3 of the U-direction of the orthonormal basis for geometry in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetOrthonormalBasisGUComponent3() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2];
	}
	
	/**
	 * Returns the value of component 1 of the V-direction of the orthonormal basis for geometry in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 1 of the V-direction of the orthonormal basis for geometry in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetOrthonormalBasisGVComponent1() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0];
	}
	
	/**
	 * Returns the value of component 2 of the V-direction of the orthonormal basis for geometry in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 2 of the V-direction of the orthonormal basis for geometry in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetOrthonormalBasisGVComponent2() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1];
	}
	
	/**
	 * Returns the value of component 3 of the V-direction of the orthonormal basis for geometry in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 3 of the V-direction of the orthonormal basis for geometry in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetOrthonormalBasisGVComponent3() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2];
	}
	
	/**
	 * Returns the value of component 1 of the W-direction of the orthonormal basis for geometry in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 1 of the W-direction of the orthonormal basis for geometry in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetOrthonormalBasisGWComponent1() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
	}
	
	/**
	 * Returns the value of component 2 of the W-direction of the orthonormal basis for geometry in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 2 of the W-direction of the orthonormal basis for geometry in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetOrthonormalBasisGWComponent2() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
	}
	
	/**
	 * Returns the value of component 3 of the W-direction of the orthonormal basis for geometry in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 3 of the W-direction of the orthonormal basis for geometry in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetOrthonormalBasisGWComponent3() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
	}
	
	/**
	 * Returns the value of component 1 of the U-direction of the orthonormal basis for shading in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 1 of the U-direction of the orthonormal basis for shading in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetOrthonormalBasisSUComponent1() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0];
	}
	
	/**
	 * Returns the value of component 2 of the U-direction of the orthonormal basis for shading in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 2 of the U-direction of the orthonormal basis for shading in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetOrthonormalBasisSUComponent2() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1];
	}
	
	/**
	 * Returns the value of component 3 of the U-direction of the orthonormal basis for shading in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 3 of the U-direction of the orthonormal basis for shading in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetOrthonormalBasisSUComponent3() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2];
	}
	
	/**
	 * Returns the value of component 1 of the V-direction of the orthonormal basis for shading in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 1 of the V-direction of the orthonormal basis for shading in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetOrthonormalBasisSVComponent1() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0];
	}
	
	/**
	 * Returns the value of component 2 of the V-direction of the orthonormal basis for shading in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 2 of the V-direction of the orthonormal basis for shading in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetOrthonormalBasisSVComponent2() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1];
	}
	
	/**
	 * Returns the value of component 3 of the V-direction of the orthonormal basis for shading in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 3 of the V-direction of the orthonormal basis for shading in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetOrthonormalBasisSVComponent3() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2];
	}
	
	/**
	 * Returns the value of component 1 of the W-direction of the orthonormal basis for shading in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 1 of the W-direction of the orthonormal basis for shading in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetOrthonormalBasisSWComponent1() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
	}
	
	/**
	 * Returns the value of component 2 of the W-direction of the orthonormal basis for shading in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 2 of the W-direction of the orthonormal basis for shading in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetOrthonormalBasisSWComponent2() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
	}
	
	/**
	 * Returns the value of component 3 of the W-direction of the orthonormal basis for shading in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 3 of the W-direction of the orthonormal basis for shading in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetOrthonormalBasisSWComponent3() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
	}
	
	/**
	 * Returns the value of component 1 of the surface intersection point in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 1 of the surface intersection point in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetSurfaceIntersectionPointComponent1() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0];
	}
	
	/**
	 * Returns the value of component 2 of the surface intersection point in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 2 of the surface intersection point in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetSurfaceIntersectionPointComponent2() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1];
	}
	
	/**
	 * Returns the value of component 3 of the surface intersection point in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 3 of the surface intersection point in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetSurfaceIntersectionPointComponent3() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2];
	}
	
	/**
	 * Returns the value of component 1 of the texture coordinates in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 1 of the texture coordinates in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetTextureCoordinatesComponent1() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0];
	}
	
	/**
	 * Returns the value of component 2 of the texture coordinates in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the value of component 2 of the texture coordinates in {@link #intersectionArray_$private$24}
	 */
	protected final float intersectionGetTextureCoordinatesComponent2() {
		return this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1];
	}
	
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
		final float conePhiMax = this.shape3FCone3FArray[shape3FCone3FArrayOffset + Cone3F.ARRAY_OFFSET_PHI_MAX];
		final float coneRadius = this.shape3FCone3FArray[shape3FCone3FArrayOffset + Cone3F.ARRAY_OFFSET_RADIUS];
		final float coneZMax = this.shape3FCone3FArray[shape3FCone3FArrayOffset + Cone3F.ARRAY_OFFSET_Z_MAX];
		
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
		final float cylinderPhiMax = this.shape3FCylinder3FArray[shape3FCylinder3FArrayOffset + Cylinder3F.ARRAY_OFFSET_PHI_MAX];
		final float cylinderRadius = this.shape3FCylinder3FArray[shape3FCylinder3FArrayOffset + Cylinder3F.ARRAY_OFFSET_RADIUS];
		final float cylinderZMax = this.shape3FCylinder3FArray[shape3FCylinder3FArrayOffset + Cylinder3F.ARRAY_OFFSET_Z_MAX];
		final float cylinderZMin = this.shape3FCylinder3FArray[shape3FCylinder3FArrayOffset + Cylinder3F.ARRAY_OFFSET_Z_MIN];
		
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
		final float diskPhiMax = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + Disk3F.ARRAY_OFFSET_PHI_MAX];
		final float diskRadiusInner = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + Disk3F.ARRAY_OFFSET_RADIUS_INNER];
		final float diskRadiusOuter = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + Disk3F.ARRAY_OFFSET_RADIUS_OUTER];
		final float diskZMax = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + Disk3F.ARRAY_OFFSET_Z_MAX];
		
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
		final float paraboloidPhiMax = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + Paraboloid3F.ARRAY_OFFSET_PHI_MAX];
		final float paraboloidRadius = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + Paraboloid3F.ARRAY_OFFSET_RADIUS];
		final float paraboloidZMax = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + Paraboloid3F.ARRAY_OFFSET_Z_MAX];
		final float paraboloidZMin = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + Paraboloid3F.ARRAY_OFFSET_Z_MIN];
		
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
		final float planeAX = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + Plane3F.ARRAY_OFFSET_A + 0];
		final float planeAY = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + Plane3F.ARRAY_OFFSET_A + 1];
		final float planeAZ = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + Plane3F.ARRAY_OFFSET_A + 2];
		final float planeSurfaceNormalX = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + Plane3F.ARRAY_OFFSET_SURFACE_NORMAL + 0];
		final float planeSurfaceNormalY = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + Plane3F.ARRAY_OFFSET_SURFACE_NORMAL + 1];
		final float planeSurfaceNormalZ = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + Plane3F.ARRAY_OFFSET_SURFACE_NORMAL + 2];
		
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
		final float rectangularCuboidMaximumX = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + RectangularCuboid3F.ARRAY_OFFSET_MAXIMUM + 0];
		final float rectangularCuboidMaximumY = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + RectangularCuboid3F.ARRAY_OFFSET_MAXIMUM + 1];
		final float rectangularCuboidMaximumZ = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + RectangularCuboid3F.ARRAY_OFFSET_MAXIMUM + 2];
		final float rectangularCuboidMinimumX = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + RectangularCuboid3F.ARRAY_OFFSET_MINIMUM + 0];
		final float rectangularCuboidMinimumY = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + RectangularCuboid3F.ARRAY_OFFSET_MINIMUM + 1];
		final float rectangularCuboidMinimumZ = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + RectangularCuboid3F.ARRAY_OFFSET_MINIMUM + 2];
		
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
		final float sphereCenterX = this.shape3FSphere3FArray[shape3FSphere3FArrayOffset + Sphere3F.ARRAY_OFFSET_CENTER + 0];
		final float sphereCenterY = this.shape3FSphere3FArray[shape3FSphere3FArrayOffset + Sphere3F.ARRAY_OFFSET_CENTER + 1];
		final float sphereCenterZ = this.shape3FSphere3FArray[shape3FSphere3FArrayOffset + Sphere3F.ARRAY_OFFSET_CENTER + 2];
		final float sphereRadius = this.shape3FSphere3FArray[shape3FSphere3FArrayOffset + Sphere3F.ARRAY_OFFSET_RADIUS];
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
		final float torusRadiusInner = this.shape3FTorus3FArray[shape3FTorus3FArrayOffset + Torus3F.ARRAY_OFFSET_RADIUS_INNER];
		final float torusRadiusInnerSquared = torusRadiusInner * torusRadiusInner;
		final float torusRadiusOuter = this.shape3FTorus3FArray[shape3FTorus3FArrayOffset + Torus3F.ARRAY_OFFSET_RADIUS_OUTER];
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
		final float triangleAPositionX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_A_POSITION + 0];
		final float triangleAPositionY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_A_POSITION + 1];
		final float triangleAPositionZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_A_POSITION + 2];
		final float triangleBPositionX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_B_POSITION + 0];
		final float triangleBPositionY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_B_POSITION + 1];
		final float triangleBPositionZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_B_POSITION + 2];
		final float triangleCPositionX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_C_POSITION + 0];
		final float triangleCPositionY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_C_POSITION + 1];
		final float triangleCPositionZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_C_POSITION + 2];
		
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
			final int id = this.shape3FTriangleMesh3FArray[offset + TriangleMesh3F.ARRAY_OFFSET_ID];
			final int boundingVolumeOffset = this.shape3FTriangleMesh3FArray[offset + TriangleMesh3F.ARRAY_OFFSET_BOUNDING_VOLUME_OFFSET];
			final int nextOffset = this.shape3FTriangleMesh3FArray[offset + TriangleMesh3F.ARRAY_OFFSET_NEXT_OFFSET];
			final int leftOffsetOrTriangleCount = this.shape3FTriangleMesh3FArray[offset + TriangleMesh3F.ARRAY_OFFSET_LEFT_OFFSET_OR_TRIANGLE_COUNT];
			
			final boolean isIntersectingBoundingVolume = boundingVolume3FAxisAlignedBoundingBox3FContainsOrIntersects(boundingVolumeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
			
			if(isIntersectingBoundingVolume && id == TriangleMesh3F.ID_LEAF_B_V_H_NODE) {
				for(int i = 0; i < leftOffsetOrTriangleCount; i++) {
					final int triangleOffset = this.shape3FTriangleMesh3FArray[offset + TriangleMesh3F.ARRAY_OFFSET_LEFT_OFFSET_OR_TRIANGLE_COUNT + 1 + i];
					
					final float tObjectSpace = this.shape3FTriangle3FIntersectionT(triangleOffset, tMinimumObjectSpace, tMaximumObjectSpace);
					
					if(tObjectSpace > tMinimumObjectSpace && tObjectSpace < tMaximumObjectSpace) {
						this.shape3FTriangleMesh3FArrayToShape3FTriangle3FArray_$private$1[0] = triangleOffset;
						
						tMaximumObjectSpace = tObjectSpace;
						
						t = tObjectSpace;
					}
				}
				
				relativeOffset = nextOffset;
			} else if(isIntersectingBoundingVolume && id == TriangleMesh3F.ID_TREE_B_V_H_NODE) {
				relativeOffset = leftOffsetOrTriangleCount;
			} else {
				relativeOffset = nextOffset;
			}
		}
		
		return t;
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
		
		if(checkIsZero(sinTheta)) {
			return 1.0F;
		}
		
		return saturateF(component1 / sinTheta, -1.0F, 1.0F);
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
		
		if(checkIsZero(sinTheta)) {
			return 0.0F;
		}
		
		return saturateF(component2 / sinTheta, -1.0F, 1.0F);
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
	 * Returns the primitive index in {@link #intersectionArray_$private$24}.
	 * 
	 * @return the primitive index in {@link #intersectionArray_$private$24}
	 */
	protected final int intersectionGetPrimitiveIndex() {
		return (int)(this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX]);
	}
	
	/**
	 * Sets the orthonormal basis for geometry in {@link #intersectionArray_$private$24}.
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
	protected final void intersectionSetOrthonormalBasisG(final float uComponent1, final float uComponent2, final float uComponent3, final float vComponent1, final float vComponent2, final float vComponent3, final float wComponent1, final float wComponent2, final float wComponent3) {
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0] = uComponent1;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1] = uComponent2;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2] = uComponent3;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0] = vComponent1;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1] = vComponent2;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2] = vComponent3;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0] = wComponent1;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1] = wComponent2;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2] = wComponent3;
	}
	
	/**
	 * Sets the orthonormal basis for shading in {@link #intersectionArray_$private$24}.
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
	protected final void intersectionSetOrthonormalBasisS(final float uComponent1, final float uComponent2, final float uComponent3, final float vComponent1, final float vComponent2, final float vComponent3, final float wComponent1, final float wComponent2, final float wComponent3) {
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = uComponent1;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = uComponent2;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = uComponent3;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = vComponent1;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = vComponent2;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = vComponent3;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = wComponent1;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = wComponent2;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = wComponent3;
	}
	
	/**
	 * Sets the primitive index in {@link #intersectionArray_$private$24}.
	 * 
	 * @param primitiveIndex the primitive index
	 */
	protected final void intersectionSetPrimitiveIndex(final int primitiveIndex) {
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX] = primitiveIndex;
	}
	
	/**
	 * Sets the surface intersection point in {@link #intersectionArray_$private$24}.
	 * 
	 * @param component1 component 1 of the surface intersection point
	 * @param component2 component 2 of the surface intersection point
	 * @param component3 component 3 of the surface intersection point
	 */
	protected final void intersectionSetSurfaceIntersectionPoint(final float component1, final float component2, final float component3) {
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0] = component1;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1] = component2;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2] = component3;
	}
	
	/**
	 * Sets the texture coordinates in {@link #intersectionArray_$private$24}.
	 * 
	 * @param component1 component 1 of the texture coordinates
	 * @param component2 component 2 of the texture coordinates
	 */
	protected final void intersectionSetTextureCoordinates(final float component1, final float component2) {
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0] = component1;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1] = component2;
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
		final float orthonormalBasisUNormalizedX = orthonormalBasisVReferenceNormalizedY * orthonormalBasisWNormalizedZ - orthonormalBasisVReferenceNormalizedZ * orthonormalBasisWNormalizedY;
		final float orthonormalBasisUNormalizedY = orthonormalBasisVReferenceNormalizedZ * orthonormalBasisWNormalizedX - orthonormalBasisVReferenceNormalizedX * orthonormalBasisWNormalizedZ;
		final float orthonormalBasisUNormalizedZ = orthonormalBasisVReferenceNormalizedX * orthonormalBasisWNormalizedY - orthonormalBasisVReferenceNormalizedY * orthonormalBasisWNormalizedX;
		
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
	 * The orthonormal basis is constructed from the orthonormal basis in {@link #intersectionArray_$private$24} that is used for geometry.
	 */
	protected final void orthonormalBasis33FSetIntersectionOrthonormalBasisG() {
//		Get the orthonormal basis:
		final float orthonormalBasisGUX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0];
		final float orthonormalBasisGUY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1];
		final float orthonormalBasisGUZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2];
		final float orthonormalBasisGVX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0];
		final float orthonormalBasisGVY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1];
		final float orthonormalBasisGVZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2];
		final float orthonormalBasisGWX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
		final float orthonormalBasisGWY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
		final float orthonormalBasisGWZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
		
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
	 * The orthonormal basis is constructed from the orthonormal basis in {@link #intersectionArray_$private$24} that is used for shading.
	 */
	protected final void orthonormalBasis33FSetIntersectionOrthonormalBasisS() {
//		Get the orthonormal basis:
		final float orthonormalBasisSUX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0];
		final float orthonormalBasisSUY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1];
		final float orthonormalBasisSUZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2];
		final float orthonormalBasisSVX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0];
		final float orthonormalBasisSVY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1];
		final float orthonormalBasisSVZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2];
		final float orthonormalBasisSWX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
		final float orthonormalBasisSWY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
		final float orthonormalBasisSWZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
		
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
	
	/**
	 * Sets a point in {@link #point3FArray_$private$3}.
	 * <p>
	 * The point is constructed by sampling a point on a disk with a uniform distribution using concentric mapping.
	 * <p>
	 * The point is 2-dimensional and not 3-dimensional as the name of this method may suggest. However, adding a new {@code point2FArray_$private$2} array just for this purpose seems unnecessary, at least for now.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param radius the radius of the disk
	 */
	protected final void point3FSetSampleDiskUniformDistributionByConcentricMapping(final float u, final float v, final float radius) {
		if(checkIsZero(u) && checkIsZero(v)) {
			this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_1] = 0.0F;
			this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_2] = 0.0F;
			this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_3] = 0.0F;
		} else {
			final float a = u * 2.0F - 1.0F;
			final float b = v * 2.0F - 1.0F;
			
			if(a * a > b * b) {
				final float phi = PI_DIVIDED_BY_4 * (b / a);
				final float r = radius * a;
				
				this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_1] = r * cos(phi);
				this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_2] = r * sin(phi);
				this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_3] = 0.0F;
			} else {
				final float phi = PI_DIVIDED_BY_2 - PI_DIVIDED_BY_4 * (a / b);
				final float r = radius * b;
				
				this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_1] = r * cos(phi);
				this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_2] = r * sin(phi);
				this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_3] = 0.0F;
			}
		}
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
	 * The ray direction is constructed using a normalized representation of the current vector in {@link #vector3FArray_$private$3}. The origin is constructed by offsetting the surface intersection point in {@link #intersectionArray_$private$24}
	 * slightly, in the direction of the ray itself.
	 */
	protected final void ray3FSetFromSurfaceIntersectionPointAndVector3F() {
		final float surfaceIntersectionPointX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0];
		final float surfaceIntersectionPointY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1];
		final float surfaceIntersectionPointZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2];
		
		final float directionX = vector3FGetComponent1();
		final float directionY = vector3FGetComponent2();
		final float directionZ = vector3FGetComponent3();
		final float directionLengthReciprocal = vector3FLengthReciprocal(directionX, directionY, directionZ);
		final float directionNormalizedX = directionX * directionLengthReciprocal;
		final float directionNormalizedY = directionY * directionLengthReciprocal;
		final float directionNormalizedZ = directionZ * directionLengthReciprocal;
		
		final float originX = surfaceIntersectionPointX + directionNormalizedX * 0.001F;
		final float originY = surfaceIntersectionPointY + directionNormalizedY * 0.001F;
		final float originZ = surfaceIntersectionPointZ + directionNormalizedZ * 0.001F;
		
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
	
	/**
	 * Computes the intersection properties for the cone at offset {@code shape3FCone3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the cone
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FCone3FArrayOffset the offset in {@link #shape3FCone3FArray}
	 */
	protected final void shape3FCone3FIntersectionCompute(final float t, final int primitiveIndex, final int shape3FCone3FArrayOffset) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the disk variables that will be referred to by 'conePhiMax' and 'coneZMax' in the comments:
		final float conePhiMax = this.shape3FCone3FArray[shape3FCone3FArrayOffset + Cone3F.ARRAY_OFFSET_PHI_MAX];
		final float coneZMax = this.shape3FCone3FArray[shape3FCone3FArrayOffset + Cone3F.ARRAY_OFFSET_Z_MAX];
		
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
		final float phi = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, PI_MULTIPLIED_BY_2);
		
		final float textureCoordinatesU = phi / conePhiMax;
		final float textureCoordinatesV = surfaceIntersectionPointZ / coneZMax;
		
		final float orthonormalBasisGUX = -conePhiMax * surfaceIntersectionPointY;
		final float orthonormalBasisGUY = +conePhiMax * surfaceIntersectionPointX;
		final float orthonormalBasisGUZ = 0.0F;
		final float orthonormalBasisGULengthReciprocal = vector3FLengthReciprocal(orthonormalBasisGUX, orthonormalBasisGUY, orthonormalBasisGUZ);
		final float orthonormalBasisGUNormalizedX = orthonormalBasisGUX * orthonormalBasisGULengthReciprocal;
		final float orthonormalBasisGUNormalizedY = orthonormalBasisGUY * orthonormalBasisGULengthReciprocal;
		final float orthonormalBasisGUNormalizedZ = orthonormalBasisGUZ * orthonormalBasisGULengthReciprocal;
		
		final float orthonormalBasisGVX = -surfaceIntersectionPointX / (1.0F - textureCoordinatesV);
		final float orthonormalBasisGVY = -surfaceIntersectionPointY / (1.0F - textureCoordinatesV);
		final float orthonormalBasisGVZ = coneZMax;
		final float orthonormalBasisGVLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisGVX, orthonormalBasisGVY, orthonormalBasisGVZ);
		final float orthonormalBasisGVNormalizedX = orthonormalBasisGVX * orthonormalBasisGVLengthReciprocal;
		final float orthonormalBasisGVNormalizedY = orthonormalBasisGVY * orthonormalBasisGVLengthReciprocal;
		final float orthonormalBasisGVNormalizedZ = orthonormalBasisGVZ * orthonormalBasisGVLengthReciprocal;
		
		final float orthonormalBasisGWNormalizedX = orthonormalBasisGUNormalizedY * orthonormalBasisGVNormalizedZ - orthonormalBasisGUNormalizedZ * orthonormalBasisGVNormalizedY;
		final float orthonormalBasisGWNormalizedY = orthonormalBasisGUNormalizedZ * orthonormalBasisGVNormalizedX - orthonormalBasisGUNormalizedX * orthonormalBasisGVNormalizedZ;
		final float orthonormalBasisGWNormalizedZ = orthonormalBasisGUNormalizedX * orthonormalBasisGVNormalizedY - orthonormalBasisGUNormalizedY * orthonormalBasisGVNormalizedX;
		
//		Update the intersection array:
		intersectionSetOrthonormalBasisG(orthonormalBasisGUNormalizedX, orthonormalBasisGUNormalizedY, orthonormalBasisGUNormalizedZ, orthonormalBasisGVNormalizedX, orthonormalBasisGVNormalizedY, orthonormalBasisGVNormalizedZ, orthonormalBasisGWNormalizedX, orthonormalBasisGWNormalizedY, orthonormalBasisGWNormalizedZ);
		intersectionSetOrthonormalBasisS(orthonormalBasisGUNormalizedX, orthonormalBasisGUNormalizedY, orthonormalBasisGUNormalizedZ, orthonormalBasisGVNormalizedX, orthonormalBasisGVNormalizedY, orthonormalBasisGVNormalizedZ, orthonormalBasisGWNormalizedX, orthonormalBasisGWNormalizedY, orthonormalBasisGWNormalizedZ);
		intersectionSetPrimitiveIndex(primitiveIndex);
		intersectionSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the cylinder at offset {@code shape3FCylinder3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the cylinder
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FCylinder3FArrayOffset the offset in {@link #shape3FCylinder3FArray}
	 */
	protected final void shape3FCylinder3FIntersectionCompute(final float t, final int primitiveIndex, final int shape3FCylinder3FArrayOffset) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the cylinder variables that will be referred to by 'cylinderPhiMax', 'cylinderRadius', 'cylinderZMax' and 'cylinderZMin' in the comments:
		final float cylinderPhiMax = this.shape3FCylinder3FArray[shape3FCylinder3FArrayOffset + Cylinder3F.ARRAY_OFFSET_PHI_MAX];
		final float cylinderRadius = this.shape3FCylinder3FArray[shape3FCylinder3FArrayOffset + Cylinder3F.ARRAY_OFFSET_RADIUS];
		final float cylinderZMax = this.shape3FCylinder3FArray[shape3FCylinder3FArrayOffset + Cylinder3F.ARRAY_OFFSET_Z_MAX];
		final float cylinderZMin = this.shape3FCylinder3FArray[shape3FCylinder3FArrayOffset + Cylinder3F.ARRAY_OFFSET_Z_MIN];
		
		final float x = rayOriginX + rayDirectionX * t;
		final float y = rayOriginY + rayDirectionY * t;
		final float z = rayOriginZ + rayDirectionZ * t;
		
		final float radius = sqrt(x * x + y * y);
		
		final float surfaceIntersectionPointX = x * (cylinderRadius / radius);
		final float surfaceIntersectionPointY = y * (cylinderRadius / radius);
		final float surfaceIntersectionPointZ = z;
		
		final float phi = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, PI_MULTIPLIED_BY_2);
		
		final float orthonormalBasisGUX = -cylinderPhiMax * surfaceIntersectionPointY;
		final float orthonormalBasisGUY = +cylinderPhiMax * surfaceIntersectionPointX;
		final float orthonormalBasisGUZ = 0.0F;
		final float orthonormalBasisGULengthReciprocal = vector3FLengthReciprocal(orthonormalBasisGUX, orthonormalBasisGUY, orthonormalBasisGUZ);
		final float orthonormalBasisGUNormalizedX = orthonormalBasisGUX * orthonormalBasisGULengthReciprocal;
		final float orthonormalBasisGUNormalizedY = orthonormalBasisGUY * orthonormalBasisGULengthReciprocal;
		final float orthonormalBasisGUNormalizedZ = orthonormalBasisGUZ * orthonormalBasisGULengthReciprocal;
		
		final float orthonormalBasisGVX = 0.0F;
		final float orthonormalBasisGVY = 0.0F;
		final float orthonormalBasisGVZ = cylinderZMax - cylinderZMin;
		final float orthonormalBasisGVLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisGVX, orthonormalBasisGVY, orthonormalBasisGVZ);
		final float orthonormalBasisGVNormalizedX = orthonormalBasisGVX * orthonormalBasisGVLengthReciprocal;
		final float orthonormalBasisGVNormalizedY = orthonormalBasisGVY * orthonormalBasisGVLengthReciprocal;
		final float orthonormalBasisGVNormalizedZ = orthonormalBasisGVZ * orthonormalBasisGVLengthReciprocal;
		
		final float orthonormalBasisGWNormalizedX = orthonormalBasisGUNormalizedY * orthonormalBasisGVNormalizedZ - orthonormalBasisGUNormalizedZ * orthonormalBasisGVNormalizedY;
		final float orthonormalBasisGWNormalizedY = orthonormalBasisGUNormalizedZ * orthonormalBasisGVNormalizedX - orthonormalBasisGUNormalizedX * orthonormalBasisGVNormalizedZ;
		final float orthonormalBasisGWNormalizedZ = orthonormalBasisGUNormalizedX * orthonormalBasisGVNormalizedY - orthonormalBasisGUNormalizedY * orthonormalBasisGVNormalizedX;
		
		final float textureCoordinatesU = phi / cylinderPhiMax;
		final float textureCoordinatesV = (surfaceIntersectionPointZ - cylinderZMin) / (cylinderZMax - cylinderZMin);
		
//		Update the intersection array:
		intersectionSetOrthonormalBasisG(orthonormalBasisGUNormalizedX, orthonormalBasisGUNormalizedY, orthonormalBasisGUNormalizedZ, orthonormalBasisGVNormalizedX, orthonormalBasisGVNormalizedY, orthonormalBasisGVNormalizedZ, orthonormalBasisGWNormalizedX, orthonormalBasisGWNormalizedY, orthonormalBasisGWNormalizedZ);
		intersectionSetOrthonormalBasisS(orthonormalBasisGUNormalizedX, orthonormalBasisGUNormalizedY, orthonormalBasisGUNormalizedZ, orthonormalBasisGVNormalizedX, orthonormalBasisGVNormalizedY, orthonormalBasisGVNormalizedZ, orthonormalBasisGWNormalizedX, orthonormalBasisGWNormalizedY, orthonormalBasisGWNormalizedZ);
		intersectionSetPrimitiveIndex(primitiveIndex);
		intersectionSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the disk at offset {@code shape3FDisk3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the disk
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FDisk3FArrayOffset the offset in {@link #shape3FDisk3FArray}
	 */
	protected final void shape3FDisk3FIntersectionCompute(final float t, final int primitiveIndex, final int shape3FDisk3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
		if(rayDirectionZ == 0.0F) {
			return;
		}
		
//		Retrieve the disk variables that will be referred to by 'diskPhiMax', 'diskRadiusInner', 'diskRadiusOuter' and 'diskZMax' in the comments:
		final float diskPhiMax = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + Disk3F.ARRAY_OFFSET_PHI_MAX];
		final float diskRadiusInner = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + Disk3F.ARRAY_OFFSET_RADIUS_INNER];
		final float diskRadiusOuter = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + Disk3F.ARRAY_OFFSET_RADIUS_OUTER];
		final float diskZMax = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + Disk3F.ARRAY_OFFSET_Z_MAX];
		
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = diskZMax;
		
		final float distanceSquared = surfaceIntersectionPointX * surfaceIntersectionPointX + surfaceIntersectionPointY * surfaceIntersectionPointY;
		
		if(distanceSquared > diskRadiusOuter * diskRadiusOuter || distanceSquared < diskRadiusInner * diskRadiusInner) {
			return;
		}
		
		final float phi = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, PI_MULTIPLIED_BY_2);
		
		if(phi > diskPhiMax) {
			return;
		}
		
		final float distance = sqrt(distanceSquared);
		
		final float orthonormalBasisGUX = -diskPhiMax * surfaceIntersectionPointY;
		final float orthonormalBasisGUY = +diskPhiMax * surfaceIntersectionPointX;
		final float orthonormalBasisGUZ = 0.0F;
		final float orthonormalBasisGULengthReciprocal = vector3FLengthReciprocal(orthonormalBasisGUX, orthonormalBasisGUY, orthonormalBasisGUZ);
		final float orthonormalBasisGUNormalizedX = orthonormalBasisGUX * orthonormalBasisGULengthReciprocal;
		final float orthonormalBasisGUNormalizedY = orthonormalBasisGUY * orthonormalBasisGULengthReciprocal;
		final float orthonormalBasisGUNormalizedZ = orthonormalBasisGUZ * orthonormalBasisGULengthReciprocal;
		
		final float orthonormalBasisGVX = surfaceIntersectionPointX * (diskRadiusInner - diskRadiusOuter) / distance;
		final float orthonormalBasisGVY = surfaceIntersectionPointY * (diskRadiusInner - diskRadiusOuter) / distance;
		final float orthonormalBasisGVZ = 0.0F;
		final float orthonormalBasisGVLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisGVX, orthonormalBasisGVY, orthonormalBasisGVZ);
		final float orthonormalBasisGVNormalizedX = orthonormalBasisGVX * orthonormalBasisGVLengthReciprocal;
		final float orthonormalBasisGVNormalizedY = orthonormalBasisGVY * orthonormalBasisGVLengthReciprocal;
		final float orthonormalBasisGVNormalizedZ = orthonormalBasisGVZ * orthonormalBasisGVLengthReciprocal;
		
		final float orthonormalBasisGWNormalizedX = orthonormalBasisGUNormalizedY * orthonormalBasisGVNormalizedZ - orthonormalBasisGUNormalizedZ * orthonormalBasisGVNormalizedY;
		final float orthonormalBasisGWNormalizedY = orthonormalBasisGUNormalizedZ * orthonormalBasisGVNormalizedX - orthonormalBasisGUNormalizedX * orthonormalBasisGVNormalizedZ;
		final float orthonormalBasisGWNormalizedZ = orthonormalBasisGUNormalizedX * orthonormalBasisGVNormalizedY - orthonormalBasisGUNormalizedY * orthonormalBasisGVNormalizedX;
		
		final float textureCoordinatesU = phi / diskPhiMax;
		final float textureCoordinatesV = (diskRadiusOuter - distance) / (diskRadiusOuter - diskRadiusInner);
		
//		Update the intersection array:
		intersectionSetOrthonormalBasisG(orthonormalBasisGUNormalizedX, orthonormalBasisGUNormalizedY, orthonormalBasisGUNormalizedZ, orthonormalBasisGVNormalizedX, orthonormalBasisGVNormalizedY, orthonormalBasisGVNormalizedZ, orthonormalBasisGWNormalizedX, orthonormalBasisGWNormalizedY, orthonormalBasisGWNormalizedZ);
		intersectionSetOrthonormalBasisS(orthonormalBasisGUNormalizedX, orthonormalBasisGUNormalizedY, orthonormalBasisGUNormalizedZ, orthonormalBasisGVNormalizedX, orthonormalBasisGVNormalizedY, orthonormalBasisGVNormalizedZ, orthonormalBasisGWNormalizedX, orthonormalBasisGWNormalizedY, orthonormalBasisGWNormalizedZ);
		intersectionSetPrimitiveIndex(primitiveIndex);
		intersectionSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the paraboloid at offset {@code shape3FParaboloid3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the paraboloid
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FParaboloid3FArrayOffset the offset in {@link #shape3FParaboloid3FArray}
	 */
	protected final void shape3FParaboloid3FIntersectionCompute(final float t, final int primitiveIndex, final int shape3FParaboloid3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the paraboloid variables that will be referred to by 'paraboloidPhiMax', 'paraboloidZMax' and 'paraboloidZMin' in the comments:
		final float paraboloidPhiMax = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + Paraboloid3F.ARRAY_OFFSET_PHI_MAX];
		final float paraboloidZMax = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + Paraboloid3F.ARRAY_OFFSET_Z_MAX];
		final float paraboloidZMin = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + Paraboloid3F.ARRAY_OFFSET_Z_MIN];
		
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
		final float phi = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, PI_MULTIPLIED_BY_2);
		
		final float orthonormalBasisGUX = -paraboloidPhiMax * surfaceIntersectionPointY;
		final float orthonormalBasisGUY = +paraboloidPhiMax * surfaceIntersectionPointX;
		final float orthonormalBasisGUZ = 0.0F;
		final float orthonormalBasisGULengthReciprocal = vector3FLengthReciprocal(orthonormalBasisGUX, orthonormalBasisGUY, orthonormalBasisGUZ);
		final float orthonormalBasisGUNormalizedX = orthonormalBasisGUX * orthonormalBasisGULengthReciprocal;
		final float orthonormalBasisGUNormalizedY = orthonormalBasisGUY * orthonormalBasisGULengthReciprocal;
		final float orthonormalBasisGUNormalizedZ = orthonormalBasisGUZ * orthonormalBasisGULengthReciprocal;
		
		final float orthonormalBasisGVX = (paraboloidZMax - paraboloidZMin) * (surfaceIntersectionPointX / (2.0F * surfaceIntersectionPointZ));
		final float orthonormalBasisGVY = (paraboloidZMax - paraboloidZMin) * (surfaceIntersectionPointY / (2.0F * surfaceIntersectionPointZ));
		final float orthonormalBasisGVZ = paraboloidZMax - paraboloidZMin;
		final float orthonormalBasisGVLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisGVX, orthonormalBasisGVY, orthonormalBasisGVZ);
		final float orthonormalBasisGVNormalizedX = orthonormalBasisGVX * orthonormalBasisGVLengthReciprocal;
		final float orthonormalBasisGVNormalizedY = orthonormalBasisGVY * orthonormalBasisGVLengthReciprocal;
		final float orthonormalBasisGVNormalizedZ = orthonormalBasisGVZ * orthonormalBasisGVLengthReciprocal;
		
		final float orthonormalBasisGWNormalizedX = orthonormalBasisGUNormalizedY * orthonormalBasisGVNormalizedZ - orthonormalBasisGUNormalizedZ * orthonormalBasisGVNormalizedY;
		final float orthonormalBasisGWNormalizedY = orthonormalBasisGUNormalizedZ * orthonormalBasisGVNormalizedX - orthonormalBasisGUNormalizedX * orthonormalBasisGVNormalizedZ;
		final float orthonormalBasisGWNormalizedZ = orthonormalBasisGUNormalizedX * orthonormalBasisGVNormalizedY - orthonormalBasisGUNormalizedY * orthonormalBasisGVNormalizedX;
		
		final float textureCoordinatesU = phi / paraboloidPhiMax;
		final float textureCoordinatesV = (surfaceIntersectionPointZ - paraboloidZMin) / (paraboloidZMax - paraboloidZMin);
		
//		Update the intersection array:
		intersectionSetOrthonormalBasisG(orthonormalBasisGUNormalizedX, orthonormalBasisGUNormalizedY, orthonormalBasisGUNormalizedZ, orthonormalBasisGVNormalizedX, orthonormalBasisGVNormalizedY, orthonormalBasisGVNormalizedZ, orthonormalBasisGWNormalizedX, orthonormalBasisGWNormalizedY, orthonormalBasisGWNormalizedZ);
		intersectionSetOrthonormalBasisS(orthonormalBasisGUNormalizedX, orthonormalBasisGUNormalizedY, orthonormalBasisGUNormalizedZ, orthonormalBasisGVNormalizedX, orthonormalBasisGVNormalizedY, orthonormalBasisGVNormalizedZ, orthonormalBasisGWNormalizedX, orthonormalBasisGWNormalizedY, orthonormalBasisGWNormalizedZ);
		intersectionSetPrimitiveIndex(primitiveIndex);
		intersectionSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the plane at offset {@code shape3FPlane3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the plane
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FPlane3FArrayOffset the offset in {@link #shape3FPlane3FArray}
	 */
	protected final void shape3FPlane3FIntersectionCompute(final float t, final int primitiveIndex, final int shape3FPlane3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the plane variables:
		final float planeAX = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + Plane3F.ARRAY_OFFSET_A + 0];
		final float planeAY = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + Plane3F.ARRAY_OFFSET_A + 1];
		final float planeAZ = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + Plane3F.ARRAY_OFFSET_A + 2];
		final float planeBX = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + Plane3F.ARRAY_OFFSET_B + 0];
		final float planeBY = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + Plane3F.ARRAY_OFFSET_B + 1];
		final float planeBZ = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + Plane3F.ARRAY_OFFSET_B + 2];
		final float planeCX = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + Plane3F.ARRAY_OFFSET_C + 0];
		final float planeCY = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + Plane3F.ARRAY_OFFSET_C + 1];
		final float planeCZ = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + Plane3F.ARRAY_OFFSET_C + 2];
		final float planeSurfaceNormalX = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + Plane3F.ARRAY_OFFSET_SURFACE_NORMAL + 0];
		final float planeSurfaceNormalY = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + Plane3F.ARRAY_OFFSET_SURFACE_NORMAL + 1];
		final float planeSurfaceNormalZ = this.shape3FPlane3FArray[shape3FPlane3FArrayOffset + Plane3F.ARRAY_OFFSET_SURFACE_NORMAL + 2];
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Retrieve the W-direction (surface normal) of the geometric orthonormal basis:
		final float orthonormalBasisGWNormalizedX = planeSurfaceNormalX;
		final float orthonormalBasisGWNormalizedY = planeSurfaceNormalY;
		final float orthonormalBasisGWNormalizedZ = planeSurfaceNormalZ;
		
//		Compute the absolute component values of the W-direction, which are used to determine the orientation of the V-direction of the geometric orthonormal basis and other things:
		final float orthonormalBasisGWNormalizedXAbs = abs(orthonormalBasisGWNormalizedX);
		final float orthonormalBasisGWNormalizedYAbs = abs(orthonormalBasisGWNormalizedY);
		final float orthonormalBasisGWNormalizedZAbs = abs(orthonormalBasisGWNormalizedZ);
		
//		Compute variables used to determine the orientation of the V-direction of the geometric orthonormal basis:
		final boolean isXSmaller = orthonormalBasisGWNormalizedXAbs < orthonormalBasisGWNormalizedYAbs && orthonormalBasisGWNormalizedXAbs < orthonormalBasisGWNormalizedZAbs;
		final boolean isYSmaller = orthonormalBasisGWNormalizedYAbs < orthonormalBasisGWNormalizedZAbs;
		
//		Compute the V-direction of the geometric orthonormal basis:
		final float orthonormalBasisGVX = isXSmaller ? +0.0F                          : isYSmaller ? +orthonormalBasisGWNormalizedZ : +orthonormalBasisGWNormalizedY;
		final float orthonormalBasisGVY = isXSmaller ? +orthonormalBasisGWNormalizedZ : isYSmaller ? +0.0F                          : -orthonormalBasisGWNormalizedX;
		final float orthonormalBasisGVZ = isXSmaller ? -orthonormalBasisGWNormalizedY : isYSmaller ? -orthonormalBasisGWNormalizedX : +0.0F;
		final float orthonormalBasisGVLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisGVX, orthonormalBasisGVY, orthonormalBasisGVZ);
		final float orthonormalBasisGVNormalizedX = orthonormalBasisGVX * orthonormalBasisGVLengthReciprocal;
		final float orthonormalBasisGVNormalizedY = orthonormalBasisGVY * orthonormalBasisGVLengthReciprocal;
		final float orthonormalBasisGVNormalizedZ = orthonormalBasisGVZ * orthonormalBasisGVLengthReciprocal;
		
//		Compute the U-direction of the geometric orthonormal basis:
		final float orthonormalBasisGUNormalizedX = orthonormalBasisGVNormalizedY * orthonormalBasisGWNormalizedZ - orthonormalBasisGVNormalizedZ * orthonormalBasisGWNormalizedY;
		final float orthonormalBasisGUNormalizedY = orthonormalBasisGVNormalizedZ * orthonormalBasisGWNormalizedX - orthonormalBasisGVNormalizedX * orthonormalBasisGWNormalizedZ;
		final float orthonormalBasisGUNormalizedZ = orthonormalBasisGVNormalizedX * orthonormalBasisGWNormalizedY - orthonormalBasisGVNormalizedY * orthonormalBasisGWNormalizedX;
		
//		Compute variables necessary for computing the texture coordinates:
		final boolean isXLarger = orthonormalBasisGWNormalizedXAbs > orthonormalBasisGWNormalizedYAbs && orthonormalBasisGWNormalizedXAbs > orthonormalBasisGWNormalizedZAbs;
		final boolean isYLarger = orthonormalBasisGWNormalizedYAbs > orthonormalBasisGWNormalizedZAbs;
		
//		Compute variables necessary for computing the texture coordinates:
		final float aX = isXLarger ? planeAY      : isYLarger ? planeAZ      : planeAX;
		final float aY = isXLarger ? planeAZ      : isYLarger ? planeAX      : planeAY;
		final float bX = isXLarger ? planeCY - aX : isYLarger ? planeCZ - aX : planeCX - aX;
		final float bY = isXLarger ? planeCZ - aY : isYLarger ? planeCX - aY : planeCY - aY;
		final float cX = isXLarger ? planeBY - aX : isYLarger ? planeBZ - aX : planeBX - aX;
		final float cY = isXLarger ? planeBZ - aY : isYLarger ? planeBX - aY : planeBY - aY;
		
//		Compute variables necessary for computing the texture coordinates:
		final float determinant = bX * cY - bY * cX;
		final float determinantReciprocal = 1.0F / determinant;
		
//		Compute variables necessary for computing the texture coordinates:
		final float u = isXLarger ? surfaceIntersectionPointY : isYLarger ? surfaceIntersectionPointZ : surfaceIntersectionPointX;
		final float v = isXLarger ? surfaceIntersectionPointZ : isYLarger ? surfaceIntersectionPointX : surfaceIntersectionPointY;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = u * (-bY * determinantReciprocal) + v * (+bX * determinantReciprocal) + (bY * aX - bX * aY) * determinantReciprocal;
		final float textureCoordinatesV = u * (+cY * determinantReciprocal) + v * (-cX * determinantReciprocal) + (cX * aY - cY * aX) * determinantReciprocal;
		
//		Update the intersection array:
		intersectionSetOrthonormalBasisG(orthonormalBasisGUNormalizedX, orthonormalBasisGUNormalizedY, orthonormalBasisGUNormalizedZ, orthonormalBasisGVNormalizedX, orthonormalBasisGVNormalizedY, orthonormalBasisGVNormalizedZ, orthonormalBasisGWNormalizedX, orthonormalBasisGWNormalizedY, orthonormalBasisGWNormalizedZ);
		intersectionSetOrthonormalBasisS(orthonormalBasisGUNormalizedX, orthonormalBasisGUNormalizedY, orthonormalBasisGUNormalizedZ, orthonormalBasisGVNormalizedX, orthonormalBasisGVNormalizedY, orthonormalBasisGVNormalizedZ, orthonormalBasisGWNormalizedX, orthonormalBasisGWNormalizedY, orthonormalBasisGWNormalizedZ);
		intersectionSetPrimitiveIndex(primitiveIndex);
		intersectionSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the rectangular cuboid at offset {@code shape3FRectangularCuboid3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the rectangular cuboid
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FRectangularCuboid3FArrayOffset the offset in {@link #shape3FRectangularCuboid3FArray}
	 */
	protected final void shape3FRectangularCuboid3FIntersectionCompute(final float t, final int primitiveIndex, final int shape3FRectangularCuboid3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the rectangular cuboid variables:
		final float rectangularCuboidMaximumX = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + RectangularCuboid3F.ARRAY_OFFSET_MAXIMUM + 0];
		final float rectangularCuboidMaximumY = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + RectangularCuboid3F.ARRAY_OFFSET_MAXIMUM + 1];
		final float rectangularCuboidMaximumZ = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + RectangularCuboid3F.ARRAY_OFFSET_MAXIMUM + 2];
		final float rectangularCuboidMinimumX = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + RectangularCuboid3F.ARRAY_OFFSET_MINIMUM + 0];
		final float rectangularCuboidMinimumY = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + RectangularCuboid3F.ARRAY_OFFSET_MINIMUM + 1];
		final float rectangularCuboidMinimumZ = this.shape3FRectangularCuboid3FArray[shape3FRectangularCuboid3FArrayOffset + RectangularCuboid3F.ARRAY_OFFSET_MINIMUM + 2];
		
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
		
//		Retrieve the W-direction (surface normal) of the geometric orthonormal basis:
		final float orthonormalBasisGWNormalizedX = face == 1 ? -1.0F : face == 2 ? +1.0F : 0.0F;
		final float orthonormalBasisGWNormalizedY = face == 3 ? -1.0F : face == 4 ? +1.0F : 0.0F;
		final float orthonormalBasisGWNormalizedZ = face == 5 ? -1.0F : face == 6 ? +1.0F : 0.0F;
		
//		Compute the absolute component values of the W-direction, which are used to determine the orientation of the V-direction of the geometric orthonormal basis:
		final float orthonormalBasisGWNormalizedXAbs = abs(orthonormalBasisGWNormalizedX);
		final float orthonormalBasisGWNormalizedYAbs = abs(orthonormalBasisGWNormalizedY);
		final float orthonormalBasisGWNormalizedZAbs = abs(orthonormalBasisGWNormalizedZ);
		
//		Compute variables used to determine the orientation of the V-direction of the geometric orthonormal basis:
		final boolean isX = orthonormalBasisGWNormalizedXAbs < orthonormalBasisGWNormalizedYAbs && orthonormalBasisGWNormalizedXAbs < orthonormalBasisGWNormalizedZAbs;
		final boolean isY = orthonormalBasisGWNormalizedYAbs < orthonormalBasisGWNormalizedZAbs;
		
//		Compute the V-direction of the geometric orthonormal basis:
		final float orthonormalBasisGVX = isX ? +0.0F                          : isY ? +orthonormalBasisGWNormalizedZ : +orthonormalBasisGWNormalizedY;
		final float orthonormalBasisGVY = isX ? +orthonormalBasisGWNormalizedZ : isY ? +0.0F                          : -orthonormalBasisGWNormalizedX;
		final float orthonormalBasisGVZ = isX ? -orthonormalBasisGWNormalizedY : isY ? -orthonormalBasisGWNormalizedX : +0.0F;
		final float orthonormalBasisGVLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisGVX, orthonormalBasisGVY, orthonormalBasisGVZ);
		final float orthonormalBasisGVNormalizedX = orthonormalBasisGVX * orthonormalBasisGVLengthReciprocal;
		final float orthonormalBasisGVNormalizedY = orthonormalBasisGVY * orthonormalBasisGVLengthReciprocal;
		final float orthonormalBasisGVNormalizedZ = orthonormalBasisGVZ * orthonormalBasisGVLengthReciprocal;
		
//		Compute the U-direction of the geometric orthonormal basis:
		final float orthonormalBasisGUNormalizedX = orthonormalBasisGVNormalizedY * orthonormalBasisGWNormalizedZ - orthonormalBasisGVNormalizedZ * orthonormalBasisGWNormalizedY;
		final float orthonormalBasisGUNormalizedY = orthonormalBasisGVNormalizedZ * orthonormalBasisGWNormalizedX - orthonormalBasisGVNormalizedX * orthonormalBasisGWNormalizedZ;
		final float orthonormalBasisGUNormalizedZ = orthonormalBasisGVNormalizedX * orthonormalBasisGWNormalizedY - orthonormalBasisGVNormalizedY * orthonormalBasisGWNormalizedX;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = faceX != 0 ? normalize(surfaceIntersectionPointZ, rectangularCuboidMinimumZ, rectangularCuboidMaximumZ) : normalize(surfaceIntersectionPointX, rectangularCuboidMinimumX, rectangularCuboidMaximumX);
		final float textureCoordinatesV = faceY != 0 ? normalize(surfaceIntersectionPointZ, rectangularCuboidMinimumZ, rectangularCuboidMaximumZ) : normalize(surfaceIntersectionPointY, rectangularCuboidMinimumY, rectangularCuboidMaximumY);
		
//		Update the intersection array:
		intersectionSetOrthonormalBasisG(orthonormalBasisGUNormalizedX, orthonormalBasisGUNormalizedY, orthonormalBasisGUNormalizedZ, orthonormalBasisGVNormalizedX, orthonormalBasisGVNormalizedY, orthonormalBasisGVNormalizedZ, orthonormalBasisGWNormalizedX, orthonormalBasisGWNormalizedY, orthonormalBasisGWNormalizedZ);
		intersectionSetOrthonormalBasisS(orthonormalBasisGUNormalizedX, orthonormalBasisGUNormalizedY, orthonormalBasisGUNormalizedZ, orthonormalBasisGVNormalizedX, orthonormalBasisGVNormalizedY, orthonormalBasisGVNormalizedZ, orthonormalBasisGWNormalizedX, orthonormalBasisGWNormalizedY, orthonormalBasisGWNormalizedZ);
		intersectionSetPrimitiveIndex(primitiveIndex);
		intersectionSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the sphere at offset {@code shape3FSphere3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the sphere
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FSphere3FArrayOffset the offset in {@link #shape3FSphere3FArray}
	 */
	protected final void shape3FSphere3FIntersectionCompute(final float t, final int primitiveIndex, final int shape3FSphere3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the sphere variables:
		final float sphereCenterX = this.shape3FSphere3FArray[shape3FSphere3FArrayOffset + Sphere3F.ARRAY_OFFSET_CENTER + 0];
		final float sphereCenterY = this.shape3FSphere3FArray[shape3FSphere3FArrayOffset + Sphere3F.ARRAY_OFFSET_CENTER + 1];
		final float sphereCenterZ = this.shape3FSphere3FArray[shape3FSphere3FArrayOffset + Sphere3F.ARRAY_OFFSET_CENTER + 2];
		
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
		
//		Compute the geometric orthonormal basis:
		orthonormalBasis33FSetFromWV(surfaceNormalGX, surfaceNormalGY, surfaceNormalGZ, vGX, vGY, vGZ);
		
//		Retrieve the geometric orthonormal basis:
		final float orthonormalBasisGUNormalizedX = orthonormalBasis33FGetUComponent1();
		final float orthonormalBasisGUNormalizedY = orthonormalBasis33FGetUComponent2();
		final float orthonormalBasisGUNormalizedZ = orthonormalBasis33FGetUComponent3();
		final float orthonormalBasisGVNormalizedX = orthonormalBasis33FGetVComponent1();
		final float orthonormalBasisGVNormalizedY = orthonormalBasis33FGetVComponent2();
		final float orthonormalBasisGVNormalizedZ = orthonormalBasis33FGetVComponent3();
		final float orthonormalBasisGWNormalizedX = orthonormalBasis33FGetWComponent1();
		final float orthonormalBasisGWNormalizedY = orthonormalBasis33FGetWComponent2();
		final float orthonormalBasisGWNormalizedZ = orthonormalBasis33FGetWComponent3();
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceNormalGY, surfaceNormalGX), 0.0F, PI_MULTIPLIED_BY_2) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float textureCoordinatesV = acos(saturateF(surfaceNormalGZ, -1.0F, 1.0F)) * PI_RECIPROCAL;
		
//		Update the intersection array:
		intersectionSetOrthonormalBasisG(orthonormalBasisGUNormalizedX, orthonormalBasisGUNormalizedY, orthonormalBasisGUNormalizedZ, orthonormalBasisGVNormalizedX, orthonormalBasisGVNormalizedY, orthonormalBasisGVNormalizedZ, orthonormalBasisGWNormalizedX, orthonormalBasisGWNormalizedY, orthonormalBasisGWNormalizedZ);
		intersectionSetOrthonormalBasisS(orthonormalBasisGUNormalizedX, orthonormalBasisGUNormalizedY, orthonormalBasisGUNormalizedZ, orthonormalBasisGVNormalizedX, orthonormalBasisGVNormalizedY, orthonormalBasisGVNormalizedZ, orthonormalBasisGWNormalizedX, orthonormalBasisGWNormalizedY, orthonormalBasisGWNormalizedZ);
		intersectionSetPrimitiveIndex(primitiveIndex);
		intersectionSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the torus at offset {@code shape3FTorus3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the torus
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FTorus3FArrayOffset the offset in {@link #shape3FTorus3FArray}
	 */
	protected final void shape3FTorus3FIntersectionCompute(final float t, final int primitiveIndex, final int shape3FTorus3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the torus variables:
		final float torusRadiusInner = this.shape3FTorus3FArray[shape3FTorus3FArrayOffset + Torus3F.ARRAY_OFFSET_RADIUS_INNER];
		final float torusRadiusInnerSquared = torusRadiusInner * torusRadiusInner;
		final float torusRadiusOuter = this.shape3FTorus3FArray[shape3FTorus3FArrayOffset + Torus3F.ARRAY_OFFSET_RADIUS_OUTER];
		final float torusRadiusOuterSquared = torusRadiusOuter * torusRadiusOuter;
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute the derivative, which is used to create the W-direction (surface normal) of the geometric orthonormal basis:
		final float derivative = (surfaceIntersectionPointX * surfaceIntersectionPointX + surfaceIntersectionPointY * surfaceIntersectionPointY + surfaceIntersectionPointZ * surfaceIntersectionPointZ) - torusRadiusInnerSquared - torusRadiusOuterSquared;
		
//		Compute the W-direction (surface normal) of the geometric orthonormal basis:
		final float orthonormalBasisGWX = surfaceIntersectionPointX * derivative;
		final float orthonormalBasisGWY = surfaceIntersectionPointY * derivative;
		final float orthonormalBasisGWZ = surfaceIntersectionPointZ * derivative + 2.0F * torusRadiusOuterSquared * surfaceIntersectionPointZ;
		final float orthonormalBasisGWLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisGWX, orthonormalBasisGWY, orthonormalBasisGWZ);
		final float orthonormalBasisGWNormalizedX = orthonormalBasisGWX * orthonormalBasisGWLengthReciprocal;
		final float orthonormalBasisGWNormalizedY = orthonormalBasisGWY * orthonormalBasisGWLengthReciprocal;
		final float orthonormalBasisGWNormalizedZ = orthonormalBasisGWZ * orthonormalBasisGWLengthReciprocal;
		
//		Compute the absolute component values of the W-direction, which are used to determine the orientation of the V-direction of the geometric orthonormal basis:
		final float orthonormalBasisGWNormalizedXAbs = abs(orthonormalBasisGWNormalizedX);
		final float orthonormalBasisGWNormalizedYAbs = abs(orthonormalBasisGWNormalizedY);
		final float orthonormalBasisGWNormalizedZAbs = abs(orthonormalBasisGWNormalizedZ);
		
//		Compute variables used to determine the orientation of the V-direction of the geometric orthonormal basis:
		final boolean isX = orthonormalBasisGWNormalizedXAbs < orthonormalBasisGWNormalizedYAbs && orthonormalBasisGWNormalizedXAbs < orthonormalBasisGWNormalizedZAbs;
		final boolean isY = orthonormalBasisGWNormalizedYAbs < orthonormalBasisGWNormalizedZAbs;
		
//		Compute the V-direction of the geometric orthonormal basis:
		final float orthonormalBasisGVX = isX ? +0.0F                          : isY ? +orthonormalBasisGWNormalizedZ : +orthonormalBasisGWNormalizedY;
		final float orthonormalBasisGVY = isX ? +orthonormalBasisGWNormalizedZ : isY ? +0.0F                          : -orthonormalBasisGWNormalizedX;
		final float orthonormalBasisGVZ = isX ? -orthonormalBasisGWNormalizedY : isY ? -orthonormalBasisGWNormalizedX : +0.0F;
		final float orthonormalBasisGVLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisGVX, orthonormalBasisGVY, orthonormalBasisGVZ);
		final float orthonormalBasisGVNormalizedX = orthonormalBasisGVX * orthonormalBasisGVLengthReciprocal;
		final float orthonormalBasisGVNormalizedY = orthonormalBasisGVY * orthonormalBasisGVLengthReciprocal;
		final float orthonormalBasisGVNormalizedZ = orthonormalBasisGVZ * orthonormalBasisGVLengthReciprocal;
		
//		Compute the U-direction of the geometric orthonormal basis:
		final float orthonormalBasisGUNormalizedX = orthonormalBasisGVNormalizedY * orthonormalBasisGWNormalizedZ - orthonormalBasisGVNormalizedZ * orthonormalBasisGWNormalizedY;
		final float orthonormalBasisGUNormalizedY = orthonormalBasisGVNormalizedZ * orthonormalBasisGWNormalizedX - orthonormalBasisGVNormalizedX * orthonormalBasisGWNormalizedZ;
		final float orthonormalBasisGUNormalizedZ = orthonormalBasisGVNormalizedX * orthonormalBasisGWNormalizedY - orthonormalBasisGVNormalizedY * orthonormalBasisGWNormalizedX;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, PI_MULTIPLIED_BY_2) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float textureCoordinatesV = (asin(saturateF(surfaceIntersectionPointZ / torusRadiusInner, -1.0F, 1.0F)) + PI_DIVIDED_BY_2) * PI_RECIPROCAL;
		
//		Update the intersection array:
		intersectionSetOrthonormalBasisG(orthonormalBasisGUNormalizedX, orthonormalBasisGUNormalizedY, orthonormalBasisGUNormalizedZ, orthonormalBasisGVNormalizedX, orthonormalBasisGVNormalizedY, orthonormalBasisGVNormalizedZ, orthonormalBasisGWNormalizedX, orthonormalBasisGWNormalizedY, orthonormalBasisGWNormalizedZ);
		intersectionSetOrthonormalBasisS(orthonormalBasisGUNormalizedX, orthonormalBasisGUNormalizedY, orthonormalBasisGUNormalizedZ, orthonormalBasisGVNormalizedX, orthonormalBasisGVNormalizedY, orthonormalBasisGVNormalizedZ, orthonormalBasisGWNormalizedX, orthonormalBasisGWNormalizedY, orthonormalBasisGWNormalizedZ);
		intersectionSetPrimitiveIndex(primitiveIndex);
		intersectionSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the triangle at offset {@code shape3FTriangle3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the triangle
	 * @param primitiveIndex the index of the primitive
	 * @param shape3FTriangle3FArrayOffset the offset in {@link #shape3FTriangle3FArray}
	 */
	protected final void shape3FTriangle3FIntersectionCompute(final float t, final int primitiveIndex, final int shape3FTriangle3FArrayOffset) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = ray3FGetOriginComponent1();
		final float rayOriginY = ray3FGetOriginComponent2();
		final float rayOriginZ = ray3FGetOriginComponent3();
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the triangle variables that will be referred to by 'triangleAPosition', 'triangleBPosition' and 'triangleCPosition' in the comments:
		final float triangleAPositionX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_A_POSITION + 0];
		final float triangleAPositionY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_A_POSITION + 1];
		final float triangleAPositionZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_A_POSITION + 2];
		final float triangleBPositionX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_B_POSITION + 0];
		final float triangleBPositionY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_B_POSITION + 1];
		final float triangleBPositionZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_B_POSITION + 2];
		final float triangleCPositionX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_C_POSITION + 0];
		final float triangleCPositionY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_C_POSITION + 1];
		final float triangleCPositionZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_C_POSITION + 2];
		
//		Retrieve the triangle variables that will be used for texturing:
		final float triangleATextureCoordinatesU = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_A_TEXTURE_COORDINATES + 0];
		final float triangleATextureCoordinatesV = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_A_TEXTURE_COORDINATES + 1];
		final float triangleBTextureCoordinatesU = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_B_TEXTURE_COORDINATES + 0];
		final float triangleBTextureCoordinatesV = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_B_TEXTURE_COORDINATES + 1];
		final float triangleCTextureCoordinatesU = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_C_TEXTURE_COORDINATES + 0];
		final float triangleCTextureCoordinatesV = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_C_TEXTURE_COORDINATES + 1];
		
//		Retrieve the triangle variables that will be used when constructing the geometric and shading orthonormal bases:
		final float triangleAOrthonormalBasisWX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_A_ORTHONORMAL_BASIS_W + 0];
		final float triangleAOrthonormalBasisWY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_A_ORTHONORMAL_BASIS_W + 1];
		final float triangleAOrthonormalBasisWZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_A_ORTHONORMAL_BASIS_W + 2];
		final float triangleBOrthonormalBasisWX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_B_ORTHONORMAL_BASIS_W + 0];
		final float triangleBOrthonormalBasisWY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_B_ORTHONORMAL_BASIS_W + 1];
		final float triangleBOrthonormalBasisWZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_B_ORTHONORMAL_BASIS_W + 2];
		final float triangleCOrthonormalBasisWX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_C_ORTHONORMAL_BASIS_W + 0];
		final float triangleCOrthonormalBasisWY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_C_ORTHONORMAL_BASIS_W + 1];
		final float triangleCOrthonormalBasisWZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_C_ORTHONORMAL_BASIS_W + 2];
		
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
		
		if(u < 0.0F) {
			return;
		}
		
//		Compute the Barycentric V-coordinate:
		final float vScaled = direction2X * edgeABX + direction2Y * edgeABY + direction2Z * edgeABZ;
		final float v = vScaled * determinantReciprocal;
		
		if(v < 0.0F || (uScaled + vScaled) * determinant > determinant * determinant) {
			return;
		}
		
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
		
//		Retrieve the orthonormal basis for the geometry:
		final float orthonormalBasisGUNormalizedX = orthonormalBasis33FGetUComponent1();
		final float orthonormalBasisGUNormalizedY = orthonormalBasis33FGetUComponent2();
		final float orthonormalBasisGUNormalizedZ = orthonormalBasis33FGetUComponent3();
		final float orthonormalBasisGVNormalizedX = orthonormalBasis33FGetVComponent1();
		final float orthonormalBasisGVNormalizedY = orthonormalBasis33FGetVComponent2();
		final float orthonormalBasisGVNormalizedZ = orthonormalBasis33FGetVComponent3();
		final float orthonormalBasisGWNormalizedX = orthonormalBasis33FGetWComponent1();
		final float orthonormalBasisGWNormalizedY = orthonormalBasis33FGetWComponent2();
		final float orthonormalBasisGWNormalizedZ = orthonormalBasis33FGetWComponent3();
		
//		Compute the orthonormal basis for shading:
		if(determinantUV != 0.0F) {
			final float dPUX = triangleAPositionX - triangleCPositionX;
			final float dPUY = triangleAPositionY - triangleCPositionY;
			final float dPUZ = triangleAPositionZ - triangleCPositionZ;
			final float dPVX = triangleBPositionX - triangleCPositionX;
			final float dPVY = triangleBPositionY - triangleCPositionY;
			final float dPVZ = triangleBPositionZ - triangleCPositionZ;
			
			final float determinantUVReciprocal = 1.0F / determinantUV;
			
			final float vSX = (-dU2 * dPUX + dU1 * dPVX) * determinantUVReciprocal;
			final float vSY = (-dU2 * dPUY + dU1 * dPVY) * determinantUVReciprocal;
			final float vSZ = (-dU2 * dPUZ + dU1 * dPVZ) * determinantUVReciprocal;
			
			orthonormalBasis33FSetFromWV(surfaceNormalSX, surfaceNormalSY, surfaceNormalSZ, vSX, vSY, vSZ);
		} else {
			orthonormalBasis33FSetFromW(surfaceNormalSX, surfaceNormalSY, surfaceNormalSZ);
		}
		
//		Retrieve the orthonormal basis for shading:
		final float orthonormalBasisSUNormalizedX = orthonormalBasis33FGetUComponent1();
		final float orthonormalBasisSUNormalizedY = orthonormalBasis33FGetUComponent2();
		final float orthonormalBasisSUNormalizedZ = orthonormalBasis33FGetUComponent3();
		final float orthonormalBasisSVNormalizedX = orthonormalBasis33FGetVComponent1();
		final float orthonormalBasisSVNormalizedY = orthonormalBasis33FGetVComponent2();
		final float orthonormalBasisSVNormalizedZ = orthonormalBasis33FGetVComponent3();
		final float orthonormalBasisSWNormalizedX = orthonormalBasis33FGetWComponent1();
		final float orthonormalBasisSWNormalizedY = orthonormalBasis33FGetWComponent2();
		final float orthonormalBasisSWNormalizedZ = orthonormalBasis33FGetWComponent3();
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = triangleATextureCoordinatesU * w + triangleBTextureCoordinatesU * u + triangleCTextureCoordinatesU * v;
		final float textureCoordinatesV = triangleATextureCoordinatesV * w + triangleBTextureCoordinatesV * u + triangleCTextureCoordinatesV * v;
		
//		Update the intersection array:
		intersectionSetOrthonormalBasisG(orthonormalBasisGUNormalizedX, orthonormalBasisGUNormalizedY, orthonormalBasisGUNormalizedZ, orthonormalBasisGVNormalizedX, orthonormalBasisGVNormalizedY, orthonormalBasisGVNormalizedZ, orthonormalBasisGWNormalizedX, orthonormalBasisGWNormalizedY, orthonormalBasisGWNormalizedZ);
		intersectionSetOrthonormalBasisS(orthonormalBasisSUNormalizedX, orthonormalBasisSUNormalizedY, orthonormalBasisSUNormalizedZ, orthonormalBasisSVNormalizedX, orthonormalBasisSVNormalizedY, orthonormalBasisSVNormalizedZ, orthonormalBasisSWNormalizedX, orthonormalBasisSWNormalizedY, orthonormalBasisSWNormalizedZ);
		intersectionSetPrimitiveIndex(primitiveIndex);
		intersectionSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the triangle at offset {@code shape3FTriangleMesh3FArrayToShape3FTriangle3FArray_$private$1[0]} in the triangle mesh.
	 * 
	 * @param t the parametric distance to the triangle mesh
	 * @param primitiveIndex the index of the primitive
	 */
	protected final void shape3FTriangleMesh3FIntersectionCompute(final float t, final int primitiveIndex) {
		final int shape3FTriangle3FArrayOffset = this.shape3FTriangleMesh3FArrayToShape3FTriangle3FArray_$private$1[0];
		
		if(shape3FTriangle3FArrayOffset != -1) {
			shape3FTriangle3FIntersectionCompute(t, primitiveIndex, shape3FTriangle3FArrayOffset);
		}
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
		if(component3LHS < 0.0F) {
			vector3FSet(+component1RHS, +component2RHS, -component3RHS);
		} else {
			vector3FSet(+component1RHS, +component2RHS, +component3RHS);
		}
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
		if(component3LHS > 0.0F) {
			vector3FSet(+component1RHS, +component2RHS, -component3RHS);
		} else {
			vector3FSet(+component1RHS, +component2RHS, +component3RHS);
		}
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by perturbing the specular reflection direction with a direction sampled using a hemisphere power-cosine distribution.
	 * <p>
	 * The specular reflection direction is constructed by calling {@link #vector3FSetSpecularReflection(float, float, float, float, float, float, boolean)}, with the parameter arguments {@code directionX}, {@code directionY}, {@code directionZ},
	 * {@code normalX}, {@code normalY}, {@code normalZ} and {@code isFacingSurface}.
	 * 
	 * @param directionX the X-component of the direction vector
	 * @param directionY the Y-component of the direction vector
	 * @param directionZ the Z-component of the direction vector
	 * @param normalX the X-component of the normal vector
	 * @param normalY the Y-component of the normal vector
	 * @param normalZ the Z-component of the normal vector
	 * @param isFacingSurface {@code true} if, and only if, the direction vector is facing the surface, {@code false} otherwise
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param exponent the exponent to use
	 */
	protected final void vector3FSetGlossyReflection(final float directionX, final float directionY, final float directionZ, final float normalX, final float normalY, final float normalZ, final boolean isFacingSurface, final float u, final float v, final float exponent) {
		vector3FSetSpecularReflection(directionX, directionY, directionZ, normalX, normalY, normalZ, isFacingSurface);
		
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
		final float oldLengthReciprocal = vector3FLengthReciprocal(component1, component2, component3);
		
		final float newComponent1 = component1 * oldLengthReciprocal;
		final float newComponent2 = component2 * oldLengthReciprocal;
		final float newComponent3 = component3 * oldLengthReciprocal;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = newComponent1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = newComponent2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = newComponent3;
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
		point3FSetSampleDiskUniformDistributionByConcentricMapping(u, v, 1.0F);
		
		final float component1 = point3FGetComponent1();
		final float component2 = point3FGetComponent2();
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
	 * If {@code isFacingSurface} is {@code true}, it is assumed that the direction vector is facing the surface. This is usually the case for the direction of a ray that intersects the surface. If {@code isFacingSurface} is {@code false}, it is
	 * assumed that the direction vector is pointing in the opposite direction. That is, the ray starts at the surface intersection point and is directed away from the surface.
	 * 
	 * @param directionX the X-component of the direction vector
	 * @param directionY the Y-component of the direction vector
	 * @param directionZ the Z-component of the direction vector
	 * @param normalX the X-component of the normal vector
	 * @param normalY the Y-component of the normal vector
	 * @param normalZ the Z-component of the normal vector
	 * @param isFacingSurface {@code true} if, and only if, the direction vector is facing the surface, {@code false} otherwise
	 */
	protected final void vector3FSetSpecularReflection(final float directionX, final float directionY, final float directionZ, final float normalX, final float normalY, final float normalZ, final boolean isFacingSurface) {
		final float directionDotNormal = vector3FDotProduct(directionX, directionY, directionZ, normalX, normalY, normalZ);
		final float directionDotNormalMultipliedByTwo = directionDotNormal * 2.0F;
		
		if(isFacingSurface) {
			final float reflectionX = directionX - normalX * directionDotNormalMultipliedByTwo;
			final float reflectionY = directionY - normalY * directionDotNormalMultipliedByTwo;
			final float reflectionZ = directionZ - normalZ * directionDotNormalMultipliedByTwo;
			
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = reflectionX;
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = reflectionY;
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = reflectionZ;
		} else {
			final float reflectionX = normalX * directionDotNormalMultipliedByTwo - directionX;
			final float reflectionY = normalY * directionDotNormalMultipliedByTwo - directionY;
			final float reflectionZ = normalZ * directionDotNormalMultipliedByTwo - directionZ;
			
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = reflectionX;
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = reflectionY;
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = reflectionZ;
		}
	}
}