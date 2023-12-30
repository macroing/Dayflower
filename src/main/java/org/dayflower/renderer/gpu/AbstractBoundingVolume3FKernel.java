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
package org.dayflower.renderer.gpu;

import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.geometry.boundingvolume.BoundingSphere3F;
import org.dayflower.scene.compiler.CompiledBoundingVolume3FCache;

/**
 * An {@code AbstractBoundingVolume3FKernel} is an abstract extension of the {@code AbstractGeometryKernel} class that adds additional features.
 * <p>
 * The features added are the following:
 * <ul>
 * <li>{@link AxisAlignedBoundingBox3F}</li>
 * <li>{@link BoundingSphere3F}</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractBoundingVolume3FKernel extends AbstractGeometryKernel {
	/**
	 * A {@code float[]} that contains axis aligned bounding boxes.
	 */
	protected float[] boundingVolume3FAxisAlignedBoundingBox3FArray;
	
	/**
	 * A {@code float[]} that contains bounding spheres.
	 */
	protected float[] boundingVolume3FBoundingSphere3FArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractBoundingVolume3FKernel} instance.
	 */
	protected AbstractBoundingVolume3FKernel() {
		this.boundingVolume3FAxisAlignedBoundingBox3FArray = new float[1];
		this.boundingVolume3FBoundingSphere3FArray = new float[1];
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
		final int boundingVolume3FAxisAlignedBoundingBox3FArrayOffsetAbsolute = boundingVolume3FAxisAlignedBoundingBox3FArrayOffset * CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH;
		
		final float axisAlignedBoundingBoxMaximumX = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM + 0];
		final float axisAlignedBoundingBoxMaximumY = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM + 1];
		final float axisAlignedBoundingBoxMaximumZ = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM + 2];
		final float axisAlignedBoundingBoxMinimumX = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM + 0];
		final float axisAlignedBoundingBoxMinimumY = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM + 1];
		final float axisAlignedBoundingBoxMinimumZ = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM + 2];
		
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
		final int boundingVolume3FAxisAlignedBoundingBox3FArrayOffsetAbsolute = boundingVolume3FAxisAlignedBoundingBox3FArrayOffset * CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH;
		
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionReciprocalX = ray3FGetDirectionReciprocalX();
		final float rayDirectionReciprocalY = ray3FGetDirectionReciprocalY();
		final float rayDirectionReciprocalZ = ray3FGetDirectionReciprocalZ();
		
//		Retrieve the axis aligned bounding box variables:
		final float axisAlignedBoundingBoxMaximumX = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM + 0];
		final float axisAlignedBoundingBoxMaximumY = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM + 1];
		final float axisAlignedBoundingBoxMaximumZ = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM + 2];
		final float axisAlignedBoundingBoxMinimumX = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM + 0];
		final float axisAlignedBoundingBoxMinimumY = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM + 1];
		final float axisAlignedBoundingBoxMinimumZ = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM + 2];
		
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
		final int boundingVolume3FAxisAlignedBoundingBox3FArrayOffsetAbsolute = boundingVolume3FAxisAlignedBoundingBox3FArrayOffset * CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH;
		
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionReciprocalX = ray3FGetDirectionReciprocalX();
		final float rayDirectionReciprocalY = ray3FGetDirectionReciprocalY();
		final float rayDirectionReciprocalZ = ray3FGetDirectionReciprocalZ();
		
//		Retrieve the axis aligned bounding box variables:
		final float axisAlignedBoundingBoxMaximumX = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM + 0];
		final float axisAlignedBoundingBoxMaximumY = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM + 1];
		final float axisAlignedBoundingBoxMaximumZ = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM + 2];
		final float axisAlignedBoundingBoxMinimumX = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM + 0];
		final float axisAlignedBoundingBoxMinimumY = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM + 1];
		final float axisAlignedBoundingBoxMinimumZ = this.boundingVolume3FAxisAlignedBoundingBox3FArray[boundingVolume3FAxisAlignedBoundingBox3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM + 2];
		
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
		final int boundingVolume3FBoundingSphere3FArrayOffsetAbsolute = boundingVolume3FBoundingSphere3FArrayOffset * CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_LENGTH;
		
		final float boundingSphereCenterX = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_CENTER + 0];
		final float boundingSphereCenterY = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_CENTER + 1];
		final float boundingSphereCenterZ = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_CENTER + 2];
		final float boundingSphereRadius = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_RADIUS];
		
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
		final int boundingVolume3FBoundingSphere3FArrayOffsetAbsolute = boundingVolume3FBoundingSphere3FArrayOffset * CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_LENGTH;
		
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
//		Retrieve the bounding sphere variables:
		final float boundingSphereCenterX = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_CENTER + 0];
		final float boundingSphereCenterY = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_CENTER + 1];
		final float boundingSphereCenterZ = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_CENTER + 2];
		final float boundingSphereRadius = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_RADIUS];
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
		final int boundingVolume3FBoundingSphere3FArrayOffsetAbsolute = boundingVolume3FBoundingSphere3FArrayOffset * CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_LENGTH;
		
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
//		Retrieve the bounding sphere variables:
		final float boundingSphereCenterX = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_CENTER + 0];
		final float boundingSphereCenterY = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_CENTER + 1];
		final float boundingSphereCenterZ = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_CENTER + 2];
		final float boundingSphereRadius = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffsetAbsolute + CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_OFFSET_RADIUS];
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
}