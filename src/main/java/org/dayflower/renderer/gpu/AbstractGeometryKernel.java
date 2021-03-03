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

import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2;

import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.geometry.boundingvolume.BoundingSphere3F;

/**
 * An {@code AbstractGeometryKernel} is an abstract extension of the {@code AbstractImageKernel} class that adds additional features.
 * <p>
 * The features added are the following:
 * <ul>
 * <li>Orthonormal basis methods</li>
 * <li>Point methods</li>
 * <li>Ray methods</li>
 * <li>Vector methods</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractGeometryKernel extends AbstractImageKernel {
	/**
	 * The offset for the vector that points in the U-direction of the orthonormal basis represented in the array {@link #orthonormalBasis33FArray_$private$9}.
	 */
	protected static final int ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U = 0;
	
	/**
	 * The offset for the vector that points in the V-direction of the orthonormal basis represented in the array {@link #orthonormalBasis33FArray_$private$9}.
	 */
	protected static final int ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V = 3;
	
	/**
	 * The offset for the vector that points in the W-direction of the orthonormal basis represented in the array {@link #orthonormalBasis33FArray_$private$9}.
	 */
	protected static final int ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W = 6;
	
	/**
	 * The size of the orthonormal basis represented in the array {@link #orthonormalBasis33FArray_$private$9}.
	 */
	protected static final int ORTHONORMAL_BASIS_3_3_F_ARRAY_SIZE = 9;
	
	/**
	 * The offset for component 1 of the point represented in the array {@link #point3FArray_$private$3}.
	 */
	protected static final int POINT_3_F_ARRAY_OFFSET_COMPONENT_1 = 0;
	
	/**
	 * The offset for component 2 of the point represented in the array {@link #point3FArray_$private$3}.
	 */
	protected static final int POINT_3_F_ARRAY_OFFSET_COMPONENT_2 = 1;
	
	/**
	 * The offset for component 3 of the point represented in the array {@link #point3FArray_$private$3}.
	 */
	protected static final int POINT_3_F_ARRAY_OFFSET_COMPONENT_3 = 2;
	
	/**
	 * The size of the point represented in the array {@link #point3FArray_$private$3}.
	 */
	protected static final int POINT_3_F_ARRAY_SIZE = 3;
	
	/**
	 * The offset for the vector called direction of the ray represented in the array {@link #ray3FArray_$private$8}.
	 */
	protected static final int RAY_3_F_ARRAY_OFFSET_DIRECTION = 3;
	
	/**
	 * The offset for the point called origin of the ray represented in the array {@link #ray3FArray_$private$8}.
	 */
	protected static final int RAY_3_F_ARRAY_OFFSET_ORIGIN = 0;
	
	/**
	 * The offset for the maximum parametric distance of the ray represented in the array {@link #ray3FArray_$private$8}.
	 */
	protected static final int RAY_3_F_ARRAY_OFFSET_T_MAXIMUM = 7;
	
	/**
	 * The offset for the minimum parametric distance of the ray represented in the array {@link #ray3FArray_$private$8}.
	 */
	protected static final int RAY_3_F_ARRAY_OFFSET_T_MINIMUM = 6;
	
	/**
	 * The size of the ray represented in the array {@link #ray3FArray_$private$8}.
	 */
	protected static final int RAY_3_F_ARRAY_SIZE = 8;
	
	/**
	 * The offset for component 1 of the vector represented in the array {@link #vector3FArray_$private$3}.
	 */
	protected static final int VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1 = 0;
	
	/**
	 * The offset for component 2 of the vector represented in the array {@link #vector3FArray_$private$3}.
	 */
	protected static final int VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2 = 1;
	
	/**
	 * The offset for component 3 of the vector represented in the array {@link #vector3FArray_$private$3}.
	 */
	protected static final int VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3 = 2;
	
	/**
	 * The size of the vector represented in the array {@link #vector3FArray_$private$3}.
	 */
	protected static final int VECTOR_3_F_ARRAY_SIZE = 3;
	
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
	 * A {@code float[]} that contains a vector that consists of three components.
	 */
	protected float[] vector3FArray_$private$3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractGeometryKernel} instance.
	 */
	protected AbstractGeometryKernel() {
		this.boundingVolume3FAxisAlignedBoundingBox3FArray = new float[1];
		this.boundingVolume3FBoundingSphere3FArray = new float[1];
		this.orthonormalBasis33FArray_$private$9 = new float[ORTHONORMAL_BASIS_3_3_F_ARRAY_SIZE];
		this.point3FArray_$private$3 = new float[POINT_3_F_ARRAY_SIZE];
		this.ray3FArray_$private$8 = new float[RAY_3_F_ARRAY_SIZE];
		this.vector3FArray_$private$3 = new float[VECTOR_3_F_ARRAY_SIZE];
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
	protected final void vector3FSetFaceForward(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
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
	protected final void vector3FSetFaceForwardNegated(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		if(vector3FDotProduct(component1LHS, component2LHS, component3LHS, component1RHS, component2RHS, component3RHS) < 0.0F) {
			vector3FSet(+component1LHS, +component2LHS, +component3LHS);
		} else {
			vector3FSet(-component1LHS, -component2LHS, -component3LHS);
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