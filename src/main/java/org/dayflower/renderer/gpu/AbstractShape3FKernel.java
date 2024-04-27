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

import org.dayflower.geometry.boundingvolume.hierarchy.LeafBVHNode3F;
import org.dayflower.geometry.boundingvolume.hierarchy.TreeBVHNode3F;
import org.dayflower.geometry.shape.Cone3F;
import org.dayflower.geometry.shape.Cylinder3F;
import org.dayflower.geometry.shape.Disk3F;
import org.dayflower.geometry.shape.Hyperboloid3F;
import org.dayflower.geometry.shape.Paraboloid3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.geometry.shape.Polygon3F;
import org.dayflower.geometry.shape.Rectangle3F;
import org.dayflower.geometry.shape.RectangularCuboid3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Torus3F;
import org.dayflower.geometry.shape.Triangle3F;
import org.dayflower.geometry.shape.TriangleMesh3F;
import org.dayflower.scene.compiler.CompiledShape3FCache;

import org.macroing.java.lang.Floats;

/**
 * An {@code AbstractShape3FKernel} is an abstract extension of the {@code AbstractBoundingVolume3FKernel} class that adds additional features.
 * <p>
 * The features added are the following:
 * <ul>
 * <li>{@link Cone3F}</li>
 * <li>{@link Cylinder3F}</li>
 * <li>{@link Disk3F}</li>
 * <li>{@link Hyperboloid3F}</li>
 * <li>{@link Paraboloid3F}</li>
 * <li>{@link Plane3F}</li>
 * <li>{@link Polygon3F}</li>
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
public abstract class AbstractShape3FKernel extends AbstractBoundingVolume3FKernel {
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
	 * A {@code float[]} that contains torii.
	 */
	protected float[] shape3FTorus3FArray;
	
	/**
	 * A {@code float[]} that contains triangles.
	 */
	protected float[] shape3FTriangle3FArray;
	
	/**
	 * An {@code int[]} that contains triangle meshes.
	 */
	protected int[] shape3FTriangleMesh3FArray;
	
	/**
	 * An {@code int[]} that contains a triangle offset for a triangle mesh.
	 */
	protected int[] shape3FTriangleMesh3FArrayToShape3FTriangle3FArrayLHS_$private$1;
	
	/**
	 * An {@code int[]} that contains a triangle offset for a triangle mesh.
	 */
	protected int[] shape3FTriangleMesh3FArrayToShape3FTriangle3FArrayRHS_$private$1;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractShape3FKernel} instance.
	 */
	protected AbstractShape3FKernel() {
		this.shape3FCone3FArray = new float[1];
		this.shape3FCylinder3FArray = new float[1];
		this.shape3FDisk3FArray = new float[1];
		this.shape3FHyperboloid3FArray = new float[1];
		this.shape3FParaboloid3FArray = new float[1];
		this.shape3FPolygon3FArray = new float[1];
		this.shape3FRectangle3FArray = new float[1];
		this.shape3FRectangularCuboid3FArray = new float[1];
		this.shape3FTorus3FArray = new float[1];
		this.shape3FTriangle3FArray = new float[1];
		this.shape3FTriangleMesh3FArray = new int[1];
		this.shape3FTriangleMesh3FArrayToShape3FTriangle3FArrayLHS_$private$1 = new int[1];
		this.shape3FTriangleMesh3FArrayToShape3FTriangle3FArrayRHS_$private$1 = new int[1];
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
		
		if(crossProduct != 0.0F) {
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		
		final float phiMinimum = addIfLessThanThreshold(atan2(yMinimum, xMinimum), 0.0F, Floats.PI_MULTIPLIED_BY_2);
		
		if(zMinimum < 0.0F || zMinimum > coneZMax || phiMinimum > conePhiMax) {
			if(tMaximum == 0.0F) {
				return 0.0F;
			}
			
			final float xMaximum = rayOriginX + rayDirectionX * tMaximum;
			final float yMaximum = rayOriginY + rayDirectionY * tMaximum;
			final float zMaximum = rayOriginZ + rayDirectionZ * tMaximum;
			
			final float phiMaximum = addIfLessThanThreshold(atan2(yMaximum, xMaximum), 0.0F, Floats.PI_MULTIPLIED_BY_2);
			
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
//		Retrieve the disk variables that will be referred to by 'conePhiMax' and 'coneZMax' in the comments:
		final float conePhiMax = this.shape3FCone3FArray[shape3FCone3FArrayOffset + CompiledShape3FCache.CONE_3_F_OFFSET_PHI_MAX];
		final float coneZMax = this.shape3FCone3FArray[shape3FCone3FArrayOffset + CompiledShape3FCache.CONE_3_F_OFFSET_Z_MAX];
		
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, Floats.PI_MULTIPLIED_BY_2) / conePhiMax;
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
//		Retrieve the disk variables that will be referred to by 'conePhiMax' and 'coneZMax' in the comments:
		final float conePhiMax = this.shape3FCone3FArray[shape3FCone3FArrayOffset + CompiledShape3FCache.CONE_3_F_OFFSET_PHI_MAX];
		final float coneZMax = this.shape3FCone3FArray[shape3FCone3FArrayOffset + CompiledShape3FCache.CONE_3_F_OFFSET_Z_MAX];
		
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, Floats.PI_MULTIPLIED_BY_2) / conePhiMax;
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		
		final float phiMinimum = addIfLessThanThreshold(atan2(yMinimum1, xMinimum1), 0.0F, Floats.PI_MULTIPLIED_BY_2);
		
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
			
			final float phiMaximum = addIfLessThanThreshold(atan2(yMaximum1, xMaximum1), 0.0F, Floats.PI_MULTIPLIED_BY_2);
			
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, Floats.PI_MULTIPLIED_BY_2) / cylinderPhiMax;
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, Floats.PI_MULTIPLIED_BY_2) / cylinderPhiMax;
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		
		final float phi = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, Floats.PI_MULTIPLIED_BY_2);
		
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		
//		Retrieve the disk variables that will be referred to by 'diskPhiMax', 'diskRadiusInner', 'diskRadiusOuter' and 'diskZMax' in the comments:
		final float diskPhiMax = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + CompiledShape3FCache.DISK_3_F_OFFSET_PHI_MAX];
		final float diskRadiusInner = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + CompiledShape3FCache.DISK_3_F_OFFSET_RADIUS_INNER];
		final float diskRadiusOuter = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + CompiledShape3FCache.DISK_3_F_OFFSET_RADIUS_OUTER];
		final float diskZMax = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + CompiledShape3FCache.DISK_3_F_OFFSET_Z_MAX];
		
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = diskZMax;
		
		final float distance = sqrt(surfaceIntersectionPointX * surfaceIntersectionPointX + surfaceIntersectionPointY * surfaceIntersectionPointY);
		
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, Floats.PI_MULTIPLIED_BY_2) / diskPhiMax;
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		
//		Retrieve the disk variables that will be referred to by 'diskPhiMax', 'diskRadiusInner', 'diskRadiusOuter' and 'diskZMax' in the comments:
		final float diskPhiMax = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + CompiledShape3FCache.DISK_3_F_OFFSET_PHI_MAX];
		final float diskRadiusInner = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + CompiledShape3FCache.DISK_3_F_OFFSET_RADIUS_INNER];
		final float diskRadiusOuter = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + CompiledShape3FCache.DISK_3_F_OFFSET_RADIUS_OUTER];
		final float diskZMax = this.shape3FDisk3FArray[shape3FDisk3FArrayOffset + CompiledShape3FCache.DISK_3_F_OFFSET_Z_MAX];
		
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = diskZMax;
		
		final float distance = sqrt(surfaceIntersectionPointX * surfaceIntersectionPointX + surfaceIntersectionPointY * surfaceIntersectionPointY);
		
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, Floats.PI_MULTIPLIED_BY_2) / diskPhiMax;
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		
		final float phiMinimum = addIfLessThanThreshold(atan2(yMinimum * xMinimumR - xMinimum * yMinimumR, xMinimum * xMinimumR + yMinimum * yMinimumR), 0.0F, Floats.PI_MULTIPLIED_BY_2);
		
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
			
			final float phiMaximum = addIfLessThanThreshold(atan2(yMaximum * xMaximumR - xMaximum * yMaximumR, xMaximum * xMaximumR + yMaximum * yMaximumR), 0.0F, Floats.PI_MULTIPLIED_BY_2);
			
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		
		final float phi = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, Floats.PI_MULTIPLIED_BY_2);
		
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		
		final float phi = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, Floats.PI_MULTIPLIED_BY_2);
		
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		
		final float phiMinimum = addIfLessThanThreshold(atan2(yMinimum, xMinimum), 0.0F, Floats.PI_MULTIPLIED_BY_2);
		
		if(zMinimum < paraboloidZMin || zMinimum > paraboloidZMax || phiMinimum > paraboloidPhiMax) {
			if(tMaximum == 0.0F) {
				return 0.0F;
			}
			
			final float xMaximum = rayOriginX + rayDirectionX * tMaximum;
			final float yMaximum = rayOriginY + rayDirectionY * tMaximum;
			final float zMaximum = rayOriginZ + rayDirectionZ * tMaximum;
			
			final float phiMaximum = addIfLessThanThreshold(atan2(yMaximum, xMaximum), 0.0F, Floats.PI_MULTIPLIED_BY_2);
			
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
//		Retrieve the paraboloid variables that will be referred to by 'paraboloidPhiMax', 'paraboloidZMax' and 'paraboloidZMin' in the comments:
		final float paraboloidPhiMax = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + CompiledShape3FCache.PARABOLOID_3_F_OFFSET_PHI_MAX];
		final float paraboloidZMax = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + CompiledShape3FCache.PARABOLOID_3_F_OFFSET_Z_MAX];
		final float paraboloidZMin = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + CompiledShape3FCache.PARABOLOID_3_F_OFFSET_Z_MIN];
		
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, Floats.PI_MULTIPLIED_BY_2) / paraboloidPhiMax;
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
//		Retrieve the paraboloid variables that will be referred to by 'paraboloidPhiMax', 'paraboloidZMax' and 'paraboloidZMin' in the comments:
		final float paraboloidPhiMax = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + CompiledShape3FCache.PARABOLOID_3_F_OFFSET_PHI_MAX];
		final float paraboloidZMax = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + CompiledShape3FCache.PARABOLOID_3_F_OFFSET_Z_MAX];
		final float paraboloidZMin = this.shape3FParaboloid3FArray[shape3FParaboloid3FArrayOffset + CompiledShape3FCache.PARABOLOID_3_F_OFFSET_Z_MIN];
		
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, Floats.PI_MULTIPLIED_BY_2) / paraboloidPhiMax;
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
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given plane in object space, {@code false} otherwise
	 */
	protected final boolean shape3FPlane3FIntersects(final float rayTMinimum, final float rayTMaximum) {
		return shape3FPlane3FIntersectionT(rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns the parametric T value for a given plane in object space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return the parametric T value for a given plane in object space, or {@code 0.0F} if no intersection was found
	 */
	protected final float shape3FPlane3FIntersectionT(final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
		final float dotProduct = vector3FDotProduct(0.0F, 0.0F, 1.0F, rayDirectionX, rayDirectionY, rayDirectionZ);
		
		if(dotProduct == -0.0F || dotProduct == +0.0F) {
			return 0.0F;
		}
		
		final float t = vector3FDotProduct(-rayOriginX, -rayOriginY, -rayOriginZ, 0.0F, 0.0F, 1.0F) / dotProduct;
		
		if(t > rayTMinimum && t < rayTMaximum) {
			return t;
		}
		
		return 0.0F;
	}
	
	/**
	 * Computes the intersection properties for the plane at offset {@code shape3FPlane3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the plane
	 * @param primitiveIndex the index of the primitive
	 */
	protected final void shape3FPlane3FIntersectionComputeLHS(final float t, final int primitiveIndex) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
		final float uX = 1.0F;
		final float uY = 0.0F;
		final float uZ = 0.0F;
		final float vX = 0.0F;
		final float vY = 1.0F;
		final float vZ = 0.0F;
		final float wX = 0.0F;
		final float wY = 0.0F;
		final float wZ = 1.0F;
		
		final float wDotRayDirection = vector3FDotProduct(wX, wY, wZ, rayDirectionX, rayDirectionY, rayDirectionZ);
		
		final float uCorrectlyOrientedX = wDotRayDirection > 0.0F ? uX : uX;
		final float uCorrectlyOrientedY = wDotRayDirection > 0.0F ? uY : uY;
		final float uCorrectlyOrientedZ = wDotRayDirection > 0.0F ? uZ : uZ;
		
		final float wCorrectlyOrientedX = wDotRayDirection > 0.0F ? wX : wX;
		final float wCorrectlyOrientedY = wDotRayDirection > 0.0F ? wY : wY;
		final float wCorrectlyOrientedZ = wDotRayDirection > 0.0F ? wZ : wZ;
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = vector3FDotProduct(1.0F, 0.0F, 0.0F, surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		final float textureCoordinatesV = vector3FDotProduct(0.0F, 1.0F, 0.0F, surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		
//		Update the intersection array:
		intersectionLHSSetOrthonormalBasisG(uCorrectlyOrientedX, uCorrectlyOrientedY, uCorrectlyOrientedZ, vX, vY, vZ, wCorrectlyOrientedX, wCorrectlyOrientedY, wCorrectlyOrientedZ);
		intersectionLHSSetOrthonormalBasisS(uCorrectlyOrientedX, uCorrectlyOrientedY, uCorrectlyOrientedZ, vX, vY, vZ, wCorrectlyOrientedX, wCorrectlyOrientedY, wCorrectlyOrientedZ);
		intersectionLHSSetPrimitiveIndex(primitiveIndex);
		intersectionLHSSetSurfaceIntersectionPoint(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		intersectionLHSSetTextureCoordinates(textureCoordinatesU, textureCoordinatesV);
	}
	
	/**
	 * Computes the intersection properties for the plane at offset {@code shape3FPlane3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the plane
	 * @param primitiveIndex the index of the primitive
	 */
	protected final void shape3FPlane3FIntersectionComputeRHS(final float t, final int primitiveIndex) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
		final float uX = 1.0F;
		final float uY = 0.0F;
		final float uZ = 0.0F;
		final float vX = 0.0F;
		final float vY = 1.0F;
		final float vZ = 0.0F;
		final float wX = 0.0F;
		final float wY = 0.0F;
		final float wZ = 1.0F;
		
		final float wDotRayDirection = vector3FDotProduct(wX, wY, wZ, rayDirectionX, rayDirectionY, rayDirectionZ);
		
		final float uCorrectlyOrientedX = wDotRayDirection > 0.0F ? uX : uX;
		final float uCorrectlyOrientedY = wDotRayDirection > 0.0F ? uY : uY;
		final float uCorrectlyOrientedZ = wDotRayDirection > 0.0F ? uZ : uZ;
		
		final float wCorrectlyOrientedX = wDotRayDirection > 0.0F ? wX : wX;
		final float wCorrectlyOrientedY = wDotRayDirection > 0.0F ? wY : wY;
		final float wCorrectlyOrientedZ = wDotRayDirection > 0.0F ? wZ : wZ;
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = vector3FDotProduct(1.0F, 0.0F, 0.0F, surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		final float textureCoordinatesV = vector3FDotProduct(0.0F, 1.0F, 0.0F, surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		
//		Update the intersection array:
		intersectionRHSSetOrthonormalBasisG(uCorrectlyOrientedX, uCorrectlyOrientedY, uCorrectlyOrientedZ, vX, vY, vZ, wCorrectlyOrientedX, wCorrectlyOrientedY, wCorrectlyOrientedZ);
		intersectionRHSSetOrthonormalBasisS(uCorrectlyOrientedX, uCorrectlyOrientedY, uCorrectlyOrientedZ, vX, vY, vZ, wCorrectlyOrientedX, wCorrectlyOrientedY, wCorrectlyOrientedZ);
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		
		final float normalDotRayDirection = vector3FDotProduct(polygonSurfaceNormalX, polygonSurfaceNormalY, polygonSurfaceNormalZ, rayDirectionX, rayDirectionY, rayDirectionZ);
		
		final float polygonSurfaceNormalCorrectlyOrientedX = normalDotRayDirection > 0.0F ? polygonSurfaceNormalX : polygonSurfaceNormalX;
		final float polygonSurfaceNormalCorrectlyOrientedY = normalDotRayDirection > 0.0F ? polygonSurfaceNormalY : polygonSurfaceNormalY;
		final float polygonSurfaceNormalCorrectlyOrientedZ = normalDotRayDirection > 0.0F ? polygonSurfaceNormalZ : polygonSurfaceNormalZ;
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute variables necessary for computing the texture coordinates:
		final boolean isXLarger = abs(polygonSurfaceNormalCorrectlyOrientedX) > abs(polygonSurfaceNormalCorrectlyOrientedY) && abs(polygonSurfaceNormalCorrectlyOrientedX) > abs(polygonSurfaceNormalCorrectlyOrientedZ);
		final boolean isYLarger = abs(polygonSurfaceNormalCorrectlyOrientedY) > abs(polygonSurfaceNormalCorrectlyOrientedZ);
		
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
		orthonormalBasis33FSetFromW(polygonSurfaceNormalCorrectlyOrientedX, polygonSurfaceNormalCorrectlyOrientedY, polygonSurfaceNormalCorrectlyOrientedZ);
		
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		
		final float normalDotRayDirection = vector3FDotProduct(polygonSurfaceNormalX, polygonSurfaceNormalY, polygonSurfaceNormalZ, rayDirectionX, rayDirectionY, rayDirectionZ);
		
		final float polygonSurfaceNormalCorrectlyOrientedX = normalDotRayDirection > 0.0F ? polygonSurfaceNormalX : polygonSurfaceNormalX;
		final float polygonSurfaceNormalCorrectlyOrientedY = normalDotRayDirection > 0.0F ? polygonSurfaceNormalY : polygonSurfaceNormalY;
		final float polygonSurfaceNormalCorrectlyOrientedZ = normalDotRayDirection > 0.0F ? polygonSurfaceNormalZ : polygonSurfaceNormalZ;
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute variables necessary for computing the texture coordinates:
		final boolean isXLarger = abs(polygonSurfaceNormalCorrectlyOrientedX) > abs(polygonSurfaceNormalCorrectlyOrientedY) && abs(polygonSurfaceNormalCorrectlyOrientedX) > abs(polygonSurfaceNormalCorrectlyOrientedZ);
		final boolean isYLarger = abs(polygonSurfaceNormalCorrectlyOrientedY) > abs(polygonSurfaceNormalCorrectlyOrientedZ);
		
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
		orthonormalBasis33FSetFromW(polygonSurfaceNormalCorrectlyOrientedX, polygonSurfaceNormalCorrectlyOrientedY, polygonSurfaceNormalCorrectlyOrientedZ);
		
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		
		final float normalDotRayDirection = vector3FDotProduct(rectangleSurfaceNormalX, rectangleSurfaceNormalY, rectangleSurfaceNormalZ, rayDirectionX, rayDirectionY, rayDirectionZ);
		
		final float rectangleSurfaceNormalCorrectlyOrientedX = normalDotRayDirection > 0.0F ? rectangleSurfaceNormalX : rectangleSurfaceNormalX;
		final float rectangleSurfaceNormalCorrectlyOrientedY = normalDotRayDirection > 0.0F ? rectangleSurfaceNormalY : rectangleSurfaceNormalY;
		final float rectangleSurfaceNormalCorrectlyOrientedZ = normalDotRayDirection > 0.0F ? rectangleSurfaceNormalZ : rectangleSurfaceNormalZ;
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute variables necessary for computing the texture coordinates:
		final boolean isXLarger = abs(rectangleSurfaceNormalCorrectlyOrientedX) > abs(rectangleSurfaceNormalCorrectlyOrientedY) && abs(rectangleSurfaceNormalCorrectlyOrientedX) > abs(rectangleSurfaceNormalCorrectlyOrientedZ);
		final boolean isYLarger = abs(rectangleSurfaceNormalCorrectlyOrientedY) > abs(rectangleSurfaceNormalCorrectlyOrientedZ);
		
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
		orthonormalBasis33FSetFromW(rectangleSurfaceNormalCorrectlyOrientedX, rectangleSurfaceNormalCorrectlyOrientedY, rectangleSurfaceNormalCorrectlyOrientedZ);
		
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		
		final float normalDotRayDirection = vector3FDotProduct(rectangleSurfaceNormalX, rectangleSurfaceNormalY, rectangleSurfaceNormalZ, rayDirectionX, rayDirectionY, rayDirectionZ);
		
		final float rectangleSurfaceNormalCorrectlyOrientedX = normalDotRayDirection > 0.0F ? rectangleSurfaceNormalX : rectangleSurfaceNormalX;
		final float rectangleSurfaceNormalCorrectlyOrientedY = normalDotRayDirection > 0.0F ? rectangleSurfaceNormalY : rectangleSurfaceNormalY;
		final float rectangleSurfaceNormalCorrectlyOrientedZ = normalDotRayDirection > 0.0F ? rectangleSurfaceNormalZ : rectangleSurfaceNormalZ;
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute variables necessary for computing the texture coordinates:
		final boolean isXLarger = abs(rectangleSurfaceNormalCorrectlyOrientedX) > abs(rectangleSurfaceNormalCorrectlyOrientedY) && abs(rectangleSurfaceNormalCorrectlyOrientedX) > abs(rectangleSurfaceNormalCorrectlyOrientedZ);
		final boolean isYLarger = abs(rectangleSurfaceNormalCorrectlyOrientedY) > abs(rectangleSurfaceNormalCorrectlyOrientedZ);
		
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
		orthonormalBasis33FSetFromW(rectangleSurfaceNormalCorrectlyOrientedX, rectangleSurfaceNormalCorrectlyOrientedY, rectangleSurfaceNormalCorrectlyOrientedZ);
		
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionReciprocalX = ray3FGetDirectionReciprocalX();
		final float rayDirectionReciprocalY = ray3FGetDirectionReciprocalY();
		final float rayDirectionReciprocalZ = ray3FGetDirectionReciprocalZ();
		
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		final int faceX = surfaceIntersectionPointX + halfDimensionX - epsilon < midpointX ? -1 : surfaceIntersectionPointX - halfDimensionX + epsilon > midpointX ? 1 : 0;
		final int faceY = surfaceIntersectionPointY + halfDimensionY - epsilon < midpointY ? -1 : surfaceIntersectionPointY - halfDimensionY + epsilon > midpointY ? 1 : 0;
		final int faceZ = surfaceIntersectionPointZ + halfDimensionZ - epsilon < midpointZ ? -1 : surfaceIntersectionPointZ - halfDimensionZ + epsilon > midpointZ ? 1 : 0;
		
//		Compute the face to use:
		final int face = faceX == -1 ? 1 : faceX == 1 ? 2 : faceY == -1 ? 3 : faceY == 1 ? 4 : faceZ == -1 ? 5 : faceZ == 1 ? 6 : 0;
		
//		Compute the surface normal:
		final float surfaceNormalX = face == 1 ? -1.0F : face == 2 ? +1.0F : 0.0F;
		final float surfaceNormalY = face == 3 ? -1.0F : face == 4 ? +1.0F : 0.0F;
		final float surfaceNormalZ = face == 5 ? -1.0F : face == 6 ? +1.0F : 0.0F;
		
		final float normalDotRayDirection = vector3FDotProduct(surfaceNormalX, surfaceNormalY, surfaceNormalZ, rayDirectionX, rayDirectionY, rayDirectionZ);
		
		final float surfaceNormalCorrectlyOrientedX = normalDotRayDirection > 0.0F ? surfaceNormalX : surfaceNormalX;
		final float surfaceNormalCorrectlyOrientedY = normalDotRayDirection > 0.0F ? surfaceNormalY : surfaceNormalY;
		final float surfaceNormalCorrectlyOrientedZ = normalDotRayDirection > 0.0F ? surfaceNormalZ : surfaceNormalZ;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = faceX != 0 ? normalize(surfaceIntersectionPointZ, rectangularCuboidMinimumZ, rectangularCuboidMaximumZ) : normalize(surfaceIntersectionPointX, rectangularCuboidMinimumX, rectangularCuboidMaximumX);
		final float textureCoordinatesV = faceY != 0 ? normalize(surfaceIntersectionPointZ, rectangularCuboidMinimumZ, rectangularCuboidMaximumZ) : normalize(surfaceIntersectionPointY, rectangularCuboidMinimumY, rectangularCuboidMaximumY);
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromW(surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ);
		
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		final int faceX = surfaceIntersectionPointX + halfDimensionX - epsilon < midpointX ? -1 : surfaceIntersectionPointX - halfDimensionX + epsilon > midpointX ? 1 : 0;
		final int faceY = surfaceIntersectionPointY + halfDimensionY - epsilon < midpointY ? -1 : surfaceIntersectionPointY - halfDimensionY + epsilon > midpointY ? 1 : 0;
		final int faceZ = surfaceIntersectionPointZ + halfDimensionZ - epsilon < midpointZ ? -1 : surfaceIntersectionPointZ - halfDimensionZ + epsilon > midpointZ ? 1 : 0;
		
//		Compute the face to use:
		final int face = faceX == -1 ? 1 : faceX == 1 ? 2 : faceY == -1 ? 3 : faceY == 1 ? 4 : faceZ == -1 ? 5 : faceZ == 1 ? 6 : 0;
		
//		Compute the surface normal:
		final float surfaceNormalX = face == 1 ? -1.0F : face == 2 ? +1.0F : 0.0F;
		final float surfaceNormalY = face == 3 ? -1.0F : face == 4 ? +1.0F : 0.0F;
		final float surfaceNormalZ = face == 5 ? -1.0F : face == 6 ? +1.0F : 0.0F;
		
		final float normalDotRayDirection = vector3FDotProduct(surfaceNormalX, surfaceNormalY, surfaceNormalZ, rayDirectionX, rayDirectionY, rayDirectionZ);
		
		final float surfaceNormalCorrectlyOrientedX = normalDotRayDirection > 0.0F ? surfaceNormalX : surfaceNormalX;
		final float surfaceNormalCorrectlyOrientedY = normalDotRayDirection > 0.0F ? surfaceNormalY : surfaceNormalY;
		final float surfaceNormalCorrectlyOrientedZ = normalDotRayDirection > 0.0F ? surfaceNormalZ : surfaceNormalZ;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = faceX != 0 ? normalize(surfaceIntersectionPointZ, rectangularCuboidMinimumZ, rectangularCuboidMaximumZ) : normalize(surfaceIntersectionPointX, rectangularCuboidMinimumX, rectangularCuboidMaximumX);
		final float textureCoordinatesV = faceY != 0 ? normalize(surfaceIntersectionPointZ, rectangularCuboidMinimumZ, rectangularCuboidMaximumZ) : normalize(surfaceIntersectionPointY, rectangularCuboidMinimumY, rectangularCuboidMaximumY);
		
//		Compute the orthonormal basis:
		orthonormalBasis33FSetFromW(surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ);
		
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
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given sphere in object space, {@code false} otherwise
	 */
	protected final boolean shape3FSphere3FIntersects(final float rayTMinimum, final float rayTMaximum) {
		return shape3FSphere3FIntersectionT(rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns the probability density function (PDF) value.
	 * 
	 * @param incomingX the X-component of the incoming direction
	 * @param incomingY the Y-component of the incoming direction
	 * @param incomingZ the Z-component of the incoming direction
	 * @return the probability density function (PDF) value
	 */
	protected final float shape3FSphere3FEvaluateProbabilityDensityFunctionRHS(final float incomingX, final float incomingY, final float incomingZ) {
		final float currentRayOriginX = ray3FGetOriginX();
		final float currentRayOriginY = ray3FGetOriginY();
		final float currentRayOriginZ = ray3FGetOriginZ();
		
		final float currentRayDirectionX = ray3FGetDirectionX();
		final float currentRayDirectionY = ray3FGetDirectionY();
		final float currentRayDirectionZ = ray3FGetDirectionZ();
		
		final float surfaceIntersectionPointX = intersectionRHSGetSurfaceIntersectionPointX();
		final float surfaceIntersectionPointY = intersectionRHSGetSurfaceIntersectionPointY();
		final float surfaceIntersectionPointZ = intersectionRHSGetSurfaceIntersectionPointZ();
		
		ray3FSetOrigin(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		ray3FSetDirection(incomingX, incomingY, incomingZ);
		
		final float t = shape3FSphere3FIntersectionT(DEFAULT_T_MINIMUM, DEFAULT_T_MAXIMUM);
		
		if(t > 0.0F) {
			final float currentSurfaceIntersectionPointX = surfaceIntersectionPointX + incomingX * t;
			final float currentSurfaceIntersectionPointY = surfaceIntersectionPointY + incomingY * t;
			final float currentSurfaceIntersectionPointZ = surfaceIntersectionPointZ + incomingZ * t;
			
			final float distanceSquared = point3FDistanceSquared(currentSurfaceIntersectionPointX, currentSurfaceIntersectionPointY, currentSurfaceIntersectionPointZ, surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
			
			final float currentSurfaceNormalLengthReciprocal = vector3FLengthReciprocal(currentSurfaceIntersectionPointX, currentSurfaceIntersectionPointY, currentSurfaceIntersectionPointZ);
			final float currentSurfaceNormalX = currentSurfaceIntersectionPointX * currentSurfaceNormalLengthReciprocal;
			final float currentSurfaceNormalY = currentSurfaceIntersectionPointY * currentSurfaceNormalLengthReciprocal;
			final float currentSurfaceNormalZ = currentSurfaceIntersectionPointZ * currentSurfaceNormalLengthReciprocal;
			
			final float nDotO = vector3FDotProduct(currentSurfaceNormalX, currentSurfaceNormalY, currentSurfaceNormalZ, -incomingX, -incomingY, -incomingZ);
			final float nDotOAbs = abs(nDotO);
			
			final float surfaceArea = Floats.PI_MULTIPLIED_BY_4;
			
			final float probabilityDensityFunctionValue = distanceSquared / nDotOAbs * surfaceArea;
			
			if(checkIsFinite(probabilityDensityFunctionValue) && probabilityDensityFunctionValue > 0.0F) {
				ray3FSetOrigin(currentRayOriginX, currentRayOriginY, currentRayOriginZ);
				ray3FSetDirection(currentRayDirectionX, currentRayDirectionY, currentRayDirectionZ);
				
				return probabilityDensityFunctionValue;
			}
		}
		
		ray3FSetOrigin(currentRayOriginX, currentRayOriginY, currentRayOriginZ);
		ray3FSetDirection(currentRayDirectionX, currentRayDirectionY, currentRayDirectionZ);
		
		return 0.0F;
	}
	
	/**
	 * Returns the parametric T value for a given sphere in object space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return the parametric T value for a given sphere in object space, or {@code 0.0F} if no intersection was found
	 */
	protected final float shape3FSphere3FIntersectionT(final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
//		Retrieve the sphere variables:
//		final float sphereCenterX = 0.0F;
//		final float sphereCenterY = 0.0F;
//		final float sphereCenterZ = 0.0F;
		final float sphereRadius = 1.0F;
//		final float sphereRadiusSquared = sphereRadius * sphereRadius;
		
		final float dx = rayDirectionX;
		final float dy = rayDirectionY;
		final float dz = rayDirectionZ;
		final float ox = rayOriginX;
		final float oy = rayOriginY;
		final float oz = rayOriginZ;
		
		eFloatSetMultiply(0, dx, dx, dx, dx, dx, dx);
		eFloatSetMultiply(1, dy, dy, dy, dy, dy, dy);
		eFloatSetMultiply(2, dz, dz, dz, dz, dz, dz);
		
		eFloatSetAdd(0, super.eFloatArray_$private$9[0], super.eFloatArray_$private$9[1], super.eFloatArray_$private$9[2], super.eFloatArray_$private$9[3], super.eFloatArray_$private$9[4], super.eFloatArray_$private$9[5]);
		eFloatSetAdd(0, super.eFloatArray_$private$9[0], super.eFloatArray_$private$9[1], super.eFloatArray_$private$9[2], super.eFloatArray_$private$9[6], super.eFloatArray_$private$9[7], super.eFloatArray_$private$9[8]);
		
		final float aValue = super.eFloatArray_$private$9[0];
		final float aLowerBound = super.eFloatArray_$private$9[1];
		final float aUpperBound = super.eFloatArray_$private$9[2];
		
		eFloatSetMultiply(0, dx, dx, dx, ox, ox, ox);
		eFloatSetMultiply(1, dy, dy, dy, oy, oy, oy);
		eFloatSetMultiply(2, dz, dz, dz, oz, oz, oz);
		
		eFloatSetAdd(0, super.eFloatArray_$private$9[0], super.eFloatArray_$private$9[1], super.eFloatArray_$private$9[2], super.eFloatArray_$private$9[3], super.eFloatArray_$private$9[4], super.eFloatArray_$private$9[5]);
		eFloatSetAdd(0, super.eFloatArray_$private$9[0], super.eFloatArray_$private$9[1], super.eFloatArray_$private$9[2], super.eFloatArray_$private$9[6], super.eFloatArray_$private$9[7], super.eFloatArray_$private$9[8]);
		
		eFloatSetMultiply(0, 2.0F, 2.0F, 2.0F, super.eFloatArray_$private$9[0], super.eFloatArray_$private$9[1], super.eFloatArray_$private$9[2]);
		
		final float bValue = super.eFloatArray_$private$9[0];
		final float bLowerBound = super.eFloatArray_$private$9[1];
		final float bUpperBound = super.eFloatArray_$private$9[2];
		
		eFloatSetMultiply(0, ox, ox, ox, ox, ox, ox);
		eFloatSetMultiply(1, oy, oy, oy, oy, oy, oy);
		eFloatSetMultiply(2, oz, oz, oz, oz, oz, oz);
		
		eFloatSetAdd(0, super.eFloatArray_$private$9[0], super.eFloatArray_$private$9[1], super.eFloatArray_$private$9[2], super.eFloatArray_$private$9[3], super.eFloatArray_$private$9[4], super.eFloatArray_$private$9[5]);
		eFloatSetAdd(0, super.eFloatArray_$private$9[0], super.eFloatArray_$private$9[1], super.eFloatArray_$private$9[2], super.eFloatArray_$private$9[6], super.eFloatArray_$private$9[7], super.eFloatArray_$private$9[8]);
		
		final float c0Value = super.eFloatArray_$private$9[0];
		final float c0LowerBound = super.eFloatArray_$private$9[1];
		final float c0UpperBound = super.eFloatArray_$private$9[2];
		
		eFloatSetMultiply(0, sphereRadius, sphereRadius, sphereRadius, sphereRadius, sphereRadius, sphereRadius);
		
		final float c1Value = super.eFloatArray_$private$9[0];
		final float c1LowerBound = super.eFloatArray_$private$9[1];
		final float c1UpperBound = super.eFloatArray_$private$9[2];
		
		eFloatSetSubtract(0, c0Value, c0LowerBound, c0UpperBound, c1Value, c1LowerBound, c1UpperBound);
		
		final float cValue = super.eFloatArray_$private$9[0];
		final float cLowerBound = super.eFloatArray_$private$9[1];
		final float cUpperBound = super.eFloatArray_$private$9[2];
		
		if(eFloatSetQuadratic(aValue, aLowerBound, aUpperBound, bValue, bLowerBound, bUpperBound, cValue, cLowerBound, cUpperBound)) {
			final float t0Value = super.eFloatArray_$private$9[0];
			final float t0LowerBound = super.eFloatArray_$private$9[1];
			final float t0UpperBound = super.eFloatArray_$private$9[2];
			
			final float t1Value = super.eFloatArray_$private$9[3];
			final float t1LowerBound = super.eFloatArray_$private$9[4];
			final float t1UpperBound = super.eFloatArray_$private$9[5];
			
			if(t0UpperBound > rayTMaximum || t1LowerBound <= rayTMinimum) {
				return 0.0F;
			}
			
			return t0LowerBound <= rayTMinimum ? t1UpperBound > rayTMaximum ? 0.0F : t1Value : t0Value;
		}
		
		return 0.0F;
		
//		Compute the direction from the sphere center to the ray origin:
//		final float sphereCenterToRayOriginX = rayOriginX - sphereCenterX;
//		final float sphereCenterToRayOriginY = rayOriginY - sphereCenterY;
//		final float sphereCenterToRayOriginZ = rayOriginZ - sphereCenterZ;
		
//		Compute the variables for the quadratic system:
//		final float a = rayDirectionX * rayDirectionX + rayDirectionY * rayDirectionY + rayDirectionZ * rayDirectionZ;
//		final float b = 2.0F * (sphereCenterToRayOriginX * rayDirectionX + sphereCenterToRayOriginY * rayDirectionY + sphereCenterToRayOriginZ * rayDirectionZ);
//		final float c = (sphereCenterToRayOriginX * sphereCenterToRayOriginX + sphereCenterToRayOriginY * sphereCenterToRayOriginY + sphereCenterToRayOriginZ * sphereCenterToRayOriginZ) - sphereRadiusSquared;
		
//		Compute the intersection by solving the quadratic system and checking the valid intersection interval:
//		final float t = solveQuadraticSystem(a, b, c, rayTMinimum, rayTMaximum);
		
//		return t;
	}
	
	/**
	 * Samples a sphere.
	 * <p>
	 * Returns the probability density function (PDF) value or {@code 0.0F} if no sample was created.
	 * 
	 * @param u a random value between {@code 0.0F} (inclusive) and {@code 1.0F} (inclusive)
	 * @param v a random value between {@code 0.0F} (inclusive) and {@code 1.0F} (inclusive)
	 * @return the probability density function (PDF) value or {@code 0.0F} if no sample was created
	 */
	protected final float shape3FSphere3FSample(final float u, final float v) {
		vector3FSetSampleSphereUniformDistribution(u, v);
		
		final float directionX = vector3FGetX();
		final float directionY = vector3FGetY();
		final float directionZ = vector3FGetZ();
		
		final float lengthReciprocal = vector3FLengthReciprocal(directionX, directionY, directionZ);
		
		final float surfaceNormalX = directionX * lengthReciprocal;
		final float surfaceNormalY = directionY * lengthReciprocal;
		final float surfaceNormalZ = directionZ * lengthReciprocal;
		
		final float pointX = surfaceNormalX;
		final float pointY = surfaceNormalY;
		final float pointZ = surfaceNormalZ;
		
		point3FSet(pointX, pointY, pointZ);
		
		vector3FSet(surfaceNormalX, surfaceNormalY, surfaceNormalZ);
		
		return 1.0F / Floats.PI_MULTIPLIED_BY_4;
	}
	
	/**
	 * Samples a sphere.
	 * <p>
	 * Returns the probability density function (PDF) value or {@code 0.0F} if no sample was created.
	 * 
	 * @param u a random value between {@code 0.0F} (inclusive) and {@code 1.0F} (inclusive)
	 * @param v a random value between {@code 0.0F} (inclusive) and {@code 1.0F} (inclusive)
	 * @return the probability density function (PDF) value or {@code 0.0F} if no sample was created
	 */
	protected final float shape3FSphere3FSampleRHS(final float u, final float v) {
		final float centerX = 0.0F;
		final float centerY = 0.0F;
		final float centerZ = 0.0F;
		
		final float surfaceIntersectionPointX = intersectionRHSGetSurfaceIntersectionPointX();
		final float surfaceIntersectionPointY = intersectionRHSGetSurfaceIntersectionPointY();
		final float surfaceIntersectionPointZ = intersectionRHSGetSurfaceIntersectionPointZ();
		
		final float originX = surfaceIntersectionPointX;
		final float originY = surfaceIntersectionPointY;
		final float originZ = surfaceIntersectionPointZ;
		
		final float radius = 1.0F;
		final float radiusSquared = radius * radius;
		
		final float distanceSquared = point3FDistanceSquared(centerX, centerY, centerZ, originX, originY, originZ);
		
		if(distanceSquared <= radiusSquared) {
			shape3FSphere3FSample(u, v);
			
			final float pointX = point3FGetX();
			final float pointY = point3FGetY();
			final float pointZ = point3FGetZ();
			
			final float surfaceNormalX = vector3FGetX();
			final float surfaceNormalY = vector3FGetY();
			final float surfaceNormalZ = vector3FGetZ();
			
			final float incomingX = pointX - surfaceIntersectionPointX;
			final float incomingY = pointY - surfaceIntersectionPointY;
			final float incomingZ = pointZ - surfaceIntersectionPointZ;
			
			final float incomingLengthSquared = vector3FLengthSquared(incomingX, incomingY, incomingZ);
			
			if(incomingLengthSquared == 0.0F) {
				return 0.0F;
			}
			
			final float incomingLengthReciprocal = rsqrt(incomingLengthSquared);
			
			final float incomingNormalizedX = incomingX * incomingLengthReciprocal;
			final float incomingNormalizedY = incomingY * incomingLengthReciprocal;
			final float incomingNormalizedZ = incomingZ * incomingLengthReciprocal;
			
			final float incomingNormalizedNegatedX = -incomingNormalizedX;
			final float incomingNormalizedNegatedY = -incomingNormalizedY;
			final float incomingNormalizedNegatedZ = -incomingNormalizedZ;
			
			final float probabilityDensityFunctionValue = point3FDistanceSquared(pointX, pointY, pointZ, surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ) / abs(vector3FDotProduct(surfaceNormalX, surfaceNormalY, surfaceNormalZ, incomingNormalizedNegatedX, incomingNormalizedNegatedY, incomingNormalizedNegatedZ));
			
			if(!checkIsFinite(probabilityDensityFunctionValue) || probabilityDensityFunctionValue <= 0.0F) {
				return 0.0F;
			}
			
			return probabilityDensityFunctionValue;
		}
		
		final float directionX = centerX - surfaceIntersectionPointX;
		final float directionY = centerY - surfaceIntersectionPointY;
		final float directionZ = centerZ - surfaceIntersectionPointZ;
		
		final float distance = point3FDistance(centerX, centerY, centerZ, surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
		final float distanceReciprocal = 1.0F / distance;
		
		orthonormalBasis33FSetFromW(directionX * distanceReciprocal, directionY * distanceReciprocal, directionZ * distanceReciprocal);
		
		final float uX = -orthonormalBasis33FGetUX();
		final float uY = -orthonormalBasis33FGetUY();
		final float uZ = -orthonormalBasis33FGetUZ();
		
		final float vX = -orthonormalBasis33FGetVX();
		final float vY = -orthonormalBasis33FGetVY();
		final float vZ = -orthonormalBasis33FGetVZ();
		
		final float wX = -orthonormalBasis33FGetWX();
		final float wY = -orthonormalBasis33FGetWY();
		final float wZ = -orthonormalBasis33FGetWZ();
		
		final float sinThetaMax = radius * distanceReciprocal;
		final float sinThetaMaxSquared = sinThetaMax * sinThetaMax;
		final float sinThetaMaxReciprocal = 1.0F / sinThetaMax;
		final float cosThetaMax = sqrt(max(0.0F, 1.0F - sinThetaMaxSquared));
		final float cosTheta = sinThetaMaxSquared < 0.00068523F ? sqrt(1.0F - sinThetaMaxSquared * u) : (cosThetaMax - 1.0F) * u + 1.0F;
		final float sinThetaSquared = sinThetaMaxSquared < 0.00068523F ? sinThetaMaxSquared * u : 1.0F - cosTheta * cosTheta;
		final float cosAlpha = sinThetaSquared * sinThetaMaxReciprocal + cosTheta * sqrt(max(0.0F, 1.0F - sinThetaSquared * sinThetaMaxReciprocal * sinThetaMaxReciprocal));
		final float sinAlpha = sqrt(max(0.0F, 1.0F - cosAlpha * cosAlpha));
		final float phi = v * 2.0F * Floats.PI;
		
		vector3FSetDirectionSpherical12(sinAlpha, cosAlpha, phi, uX, uY, uZ, vX, vY, vZ, wX, wY, wZ);
		
		final float sphericalDirectionX = vector3FGetX();
		final float sphericalDirectionY = vector3FGetY();
		final float sphericalDirectionZ = vector3FGetZ();
		
		final float samplePointX = centerX + sphericalDirectionX * radius;
		final float samplePointY = centerY + sphericalDirectionY * radius;
		final float samplePointZ = centerZ + sphericalDirectionZ * radius;
		
		final float sphericalDirectionLengthReciprocal = vector3FLengthReciprocal(sphericalDirectionX, sphericalDirectionY, sphericalDirectionZ);
		
		final float sphericalDirectionNormalizedX = sphericalDirectionX * sphericalDirectionLengthReciprocal;
		final float sphericalDirectionNormalizedY = sphericalDirectionY * sphericalDirectionLengthReciprocal;
		final float sphericalDirectionNormalizedZ = sphericalDirectionZ * sphericalDirectionLengthReciprocal;
		
		point3FSet(samplePointX, samplePointY, samplePointZ);
		
		vector3FSet(sphericalDirectionNormalizedX, sphericalDirectionNormalizedY, sphericalDirectionNormalizedZ);
		
		return 1.0F / (2.0F * Floats.PI * (1.0F - cosThetaMax));
	}
	
	/**
	 * Computes the intersection properties for the sphere at offset {@code shape3FSphere3FArrayOffset}.
	 * 
	 * @param t the parametric distance to the sphere
	 * @param primitiveIndex the index of the primitive
	 */
	protected final void shape3FSphere3FIntersectionComputeLHS(final float t, final int primitiveIndex) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
//		Retrieve the sphere variables:
		final float sphereCenterX = 0.0F;
		final float sphereCenterY = 0.0F;
		final float sphereCenterZ = 0.0F;
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute the geometric surface normal:
		final float surfaceNormalGX = surfaceIntersectionPointX - sphereCenterX;
		final float surfaceNormalGY = surfaceIntersectionPointY - sphereCenterY;
		final float surfaceNormalGZ = surfaceIntersectionPointZ - sphereCenterZ;
		
//		Compute the V-direction of the geometric orthonormal basis:
		final float vGX = surfaceNormalGX == 0.0F && surfaceNormalGY == 0.0F ? 0.0F : -Floats.PI_MULTIPLIED_BY_2 * surfaceNormalGY;
		final float vGY = surfaceNormalGX == 0.0F && surfaceNormalGY == 0.0F ? 1.0F : +Floats.PI_MULTIPLIED_BY_2 * surfaceNormalGX;
		final float vGZ = 0.0F;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceNormalGY, surfaceNormalGX), 0.0F, Floats.PI_MULTIPLIED_BY_2) * Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float textureCoordinatesV = acos(saturateF(surfaceNormalGZ, -1.0F, 1.0F)) * Floats.PI_RECIPROCAL;
		
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
	 */
	protected final void shape3FSphere3FIntersectionComputeRHS(final float t, final int primitiveIndex) {
//		Retrieve the ray variables:
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
//		Retrieve the sphere variables:
		final float sphereCenterX = 0.0F;
		final float sphereCenterY = 0.0F;
		final float sphereCenterZ = 0.0F;
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute the geometric surface normal:
		final float surfaceNormalGX = surfaceIntersectionPointX - sphereCenterX;
		final float surfaceNormalGY = surfaceIntersectionPointY - sphereCenterY;
		final float surfaceNormalGZ = surfaceIntersectionPointZ - sphereCenterZ;
		
//		Compute the V-direction of the geometric orthonormal basis:
		final float vGX = surfaceNormalGX == 0.0F && surfaceNormalGY == 0.0F ? 0.0F : -Floats.PI_MULTIPLIED_BY_2 * surfaceNormalGY;
		final float vGY = surfaceNormalGX == 0.0F && surfaceNormalGY == 0.0F ? 1.0F : +Floats.PI_MULTIPLIED_BY_2 * surfaceNormalGX;
		final float vGZ = 0.0F;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceNormalGY, surfaceNormalGX), 0.0F, Floats.PI_MULTIPLIED_BY_2) * Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float textureCoordinatesV = acos(saturateF(surfaceNormalGZ, -1.0F, 1.0F)) * Floats.PI_RECIPROCAL;
		
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, Floats.PI_MULTIPLIED_BY_2) * Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float textureCoordinatesV = (asin(saturateF(surfaceIntersectionPointZ / torusRadiusInner, -1.0F, 1.0F)) + Floats.PI_DIVIDED_BY_2) * Floats.PI_RECIPROCAL;
		
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		final float textureCoordinatesU = addIfLessThanThreshold(atan2(surfaceIntersectionPointY, surfaceIntersectionPointX), 0.0F, Floats.PI_MULTIPLIED_BY_2) * Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float textureCoordinatesV = (asin(saturateF(surfaceIntersectionPointZ / torusRadiusInner, -1.0F, 1.0F)) + Floats.PI_DIVIDED_BY_2) * Floats.PI_RECIPROCAL;
		
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		
		final float p0T0X = triangleAPositionX - rayOriginX;
		final float p0T0Y = triangleAPositionY - rayOriginY;
		final float p0T0Z = triangleAPositionZ - rayOriginZ;
		
		final float p1T0X = triangleBPositionX - rayOriginX;
		final float p1T0Y = triangleBPositionY - rayOriginY;
		final float p1T0Z = triangleBPositionZ - rayOriginZ;
		
		final float p2T0X = triangleCPositionX - rayOriginX;
		final float p2T0Y = triangleCPositionY - rayOriginY;
		final float p2T0Z = triangleCPositionZ - rayOriginZ;
		
		int kz = abs(rayDirectionX) > abs(rayDirectionY) ? abs(rayDirectionX) > abs(rayDirectionZ) ? 0 : 2 : abs(rayDirectionY) > abs(rayDirectionZ) ? 1 : 2;
		int kx = kz + 1;
		
		if(kx == 3) {
			kx = 0;
		}
		
		int ky = kx + 1;
		
		if(ky == 3) {
			ky = 0;
		}
		
		final float dX = kx == 0 ? rayDirectionX : kx == 1 ? rayDirectionY : rayDirectionZ;
		final float dY = ky == 0 ? rayDirectionX : ky == 1 ? rayDirectionY : rayDirectionZ;
		final float dZ = kz == 0 ? rayDirectionX : kz == 1 ? rayDirectionY : rayDirectionZ;
		
		final float p0T1X = kx == 0 ? p0T0X : kx == 1 ? p0T0Y : p0T0Z;
		final float p0T1Y = ky == 0 ? p0T0X : ky == 1 ? p0T0Y : p0T0Z;
		final float p0T1Z = kz == 0 ? p0T0X : kz == 1 ? p0T0Y : p0T0Z;
		
		final float p1T1X = kx == 0 ? p1T0X : kx == 1 ? p1T0Y : p1T0Z;
		final float p1T1Y = ky == 0 ? p1T0X : ky == 1 ? p1T0Y : p1T0Z;
		final float p1T1Z = kz == 0 ? p1T0X : kz == 1 ? p1T0Y : p1T0Z;
		
		final float p2T1X = kx == 0 ? p2T0X : kx == 1 ? p2T0Y : p2T0Z;
		final float p2T1Y = ky == 0 ? p2T0X : ky == 1 ? p2T0Y : p2T0Z;
		final float p2T1Z = kz == 0 ? p2T0X : kz == 1 ? p2T0Y : p2T0Z;
		
		final float sx = -dX / dZ;
		final float sy = -dY / dZ;
		final float sz = 1.0F / dZ;
		
		final float p0T2X = p0T1X + sx * p0T1Z;
		final float p0T2Y = p0T1Y + sy * p0T1Z;
		final float p0T2Z = p0T1Z;
		
		final float p1T2X = p1T1X + sx * p1T1Z;
		final float p1T2Y = p1T1Y + sy * p1T1Z;
		final float p1T2Z = p1T1Z;
		
		final float p2T2X = p2T1X + sx * p2T1Z;
		final float p2T2Y = p2T1Y + sy * p2T1Z;
		final float p2T2Z = p2T1Z;
		
		float e0 = p1T2X * p2T2Y - p1T2Y * p2T2X;
		float e1 = p2T2X * p0T2Y - p2T2Y * p0T2X;
		float e2 = p0T2X * p1T2Y - p0T2Y * p1T2X;
		
		if(e0 == 0.0F || e1 == 0.0F || e2 == 0.0F) {
			final double p2txp1ty = (double)(p2T2X) * (double)(p1T2Y);
			final double p2typ1tx = (double)(p2T2Y) * (double)(p1T2X);
			
			e0 = (float)(p2typ1tx - p2txp1ty);
			
			final double p0txp2ty = (double)(p0T2X) * (double)(p2T2Y);
			final double p0typ2tx = (double)(p0T2Y) * (double)(p2T2X);
			
			e1 = (float)(p0typ2tx - p0txp2ty);
			
			final double p1txp0ty = (double)(p1T2X) * (double)(p0T2Y);
			final double p1typ0tx = (double)(p1T2Y) * (double)(p0T2X);
			
			e2 = (float)(p1typ0tx - p1txp0ty);
		}
		
		if((e0 < 0.0F || e1 < 0.0F || e2 < 0.0F) && (e0 > 0.0F || e1 > 0.0F || e2 > 0.0F)) {
			return 0.0F;
		}
		
		final float det = e0 + e1 + e2;
		
		if(det == 0.0F) {
			return 0.0F;
		}
		
		final float p0T3X = p0T2X;
		final float p0T3Y = p0T2Y;
		final float p0T3Z = p0T2Z * sz;
		
		final float p1T3X = p1T2X;
		final float p1T3Y = p1T2Y;
		final float p1T3Z = p1T2Z * sz;
		
		final float p2T3X = p2T2X;
		final float p2T3Y = p2T2Y;
		final float p2T3Z = p2T2Z * sz;
		
		float tScaled = e0 * p0T3Z + e1 * p1T3Z + e2 * p2T3Z;
		
		if(det < 0.0F && (tScaled >= rayTMinimum || tScaled < rayTMaximum * det)) {
			return 0.0F;
		} else if(det > 0.0F && (tScaled <= rayTMinimum || tScaled > rayTMaximum * det)) {
			return 0.0F;
		}
		
		final float invDet = 1.0F / det;
		
		final float t = tScaled * invDet;
		
		final float maxZT = max(abs(p0T3Z), abs(p1T3Z), abs(p2T3Z));
		final float maxXT = max(abs(p0T3X), abs(p1T3X), abs(p2T3X));
		final float maxYT = max(abs(p0T3Y), abs(p1T3Y), abs(p2T3Y));
		
		final float deltaZ = gamma(3) * maxZT;
		final float deltaX = gamma(5) * (maxXT + maxZT);
		final float deltaY = gamma(5) * (maxYT + maxZT);
		final float deltaE = 2.0F * (gamma(2) * maxXT * maxYT + deltaY * maxXT + deltaX * maxYT);
		
		final float maxE = max(abs(e0), abs(e1), abs(e2));
		
		final float deltaT = 3.0F * (gamma(3) * maxE * maxZT + deltaE * maxZT + deltaZ * maxE) * abs(invDet);
		
		if(t <= deltaT) {
			return 0.0F;
		}
		
		return t;
		
		/*
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
		*/
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		
		final float p0T0X = triangleAPositionX - rayOriginX;
		final float p0T0Y = triangleAPositionY - rayOriginY;
		final float p0T0Z = triangleAPositionZ - rayOriginZ;
		
		final float p1T0X = triangleBPositionX - rayOriginX;
		final float p1T0Y = triangleBPositionY - rayOriginY;
		final float p1T0Z = triangleBPositionZ - rayOriginZ;
		
		final float p2T0X = triangleCPositionX - rayOriginX;
		final float p2T0Y = triangleCPositionY - rayOriginY;
		final float p2T0Z = triangleCPositionZ - rayOriginZ;
		
		int kz = abs(rayDirectionX) > abs(rayDirectionY) ? abs(rayDirectionX) > abs(rayDirectionZ) ? 0 : 2 : abs(rayDirectionY) > abs(rayDirectionZ) ? 1 : 2;
		int kx = kz + 1;
		
		if(kx == 3) {
			kx = 0;
		}
		
		int ky = kx + 1;
		
		if(ky == 3) {
			ky = 0;
		}
		
		final float dX = kx == 0 ? rayDirectionX : kx == 1 ? rayDirectionY : rayDirectionZ;
		final float dY = ky == 0 ? rayDirectionX : ky == 1 ? rayDirectionY : rayDirectionZ;
		final float dZ = kz == 0 ? rayDirectionX : kz == 1 ? rayDirectionY : rayDirectionZ;
		
		final float p0T1X = kx == 0 ? p0T0X : kx == 1 ? p0T0Y : p0T0Z;
		final float p0T1Y = ky == 0 ? p0T0X : ky == 1 ? p0T0Y : p0T0Z;
		final float p0T1Z = kz == 0 ? p0T0X : kz == 1 ? p0T0Y : p0T0Z;
		
		final float p1T1X = kx == 0 ? p1T0X : kx == 1 ? p1T0Y : p1T0Z;
		final float p1T1Y = ky == 0 ? p1T0X : ky == 1 ? p1T0Y : p1T0Z;
		final float p1T1Z = kz == 0 ? p1T0X : kz == 1 ? p1T0Y : p1T0Z;
		
		final float p2T1X = kx == 0 ? p2T0X : kx == 1 ? p2T0Y : p2T0Z;
		final float p2T1Y = ky == 0 ? p2T0X : ky == 1 ? p2T0Y : p2T0Z;
		final float p2T1Z = kz == 0 ? p2T0X : kz == 1 ? p2T0Y : p2T0Z;
		
		final float sx = -dX / dZ;
		final float sy = -dY / dZ;
		
		final float p0T2X = p0T1X + sx * p0T1Z;
		final float p0T2Y = p0T1Y + sy * p0T1Z;
		
		final float p1T2X = p1T1X + sx * p1T1Z;
		final float p1T2Y = p1T1Y + sy * p1T1Z;
		
		final float p2T2X = p2T1X + sx * p2T1Z;
		final float p2T2Y = p2T1Y + sy * p2T1Z;
		
		float e0 = p1T2X * p2T2Y - p1T2Y * p2T2X;
		float e1 = p2T2X * p0T2Y - p2T2Y * p0T2X;
		float e2 = p0T2X * p1T2Y - p0T2Y * p1T2X;
		
		if(e0 == 0.0F || e1 == 0.0F || e2 == 0.0F) {
			final double p2txp1ty = (double)(p2T2X) * (double)(p1T2Y);
			final double p2typ1tx = (double)(p2T2Y) * (double)(p1T2X);
			
			e0 = (float)(p2typ1tx - p2txp1ty);
			
			final double p0txp2ty = (double)(p0T2X) * (double)(p2T2Y);
			final double p0typ2tx = (double)(p0T2Y) * (double)(p2T2X);
			
			e1 = (float)(p0typ2tx - p0txp2ty);
			
			final double p1txp0ty = (double)(p1T2X) * (double)(p0T2Y);
			final double p1typ0tx = (double)(p1T2Y) * (double)(p0T2X);
			
			e2 = (float)(p1typ0tx - p1txp0ty);
		}
		
		final float det = e0 + e1 + e2;
		
		final float invDet = 1.0F / det;
		
		final float b0 = e0 * invDet;
		final float b1 = e1 * invDet;
		final float b2 = e2 * invDet;
		
		float dpduX = 0.0F;
		float dpduY = 0.0F;
		float dpduZ = 0.0F;
		
		float dpdvX = 0.0F;
		float dpdvY = 0.0F;
		float dpdvZ = 0.0F;
		
		final float duv02X = triangleATextureCoordinatesU - triangleCTextureCoordinatesU;
		final float duv02Y = triangleATextureCoordinatesV - triangleCTextureCoordinatesV;
		
		final float duv12X = triangleBTextureCoordinatesU - triangleCTextureCoordinatesU;
		final float duv12Y = triangleBTextureCoordinatesV - triangleCTextureCoordinatesV;
		
		final float dp02X = triangleAPositionX - triangleCPositionX;
		final float dp02Y = triangleAPositionY - triangleCPositionY;
		final float dp02Z = triangleAPositionZ - triangleCPositionZ;
		
		final float dp12X = triangleBPositionX - triangleCPositionX;
		final float dp12Y = triangleBPositionY - triangleCPositionY;
		final float dp12Z = triangleBPositionZ - triangleCPositionZ;
		
		final float determinant = duv02X * duv12Y - duv02Y * duv12X;
		
		final boolean degenerateUV = abs(determinant) < 1.0e-8F;
		
		if(!degenerateUV) {
			final float invDeterminant = 1.0F / determinant;
			
			dpduX = (duv12Y * dp02X - duv02Y * dp12X) * invDeterminant;
			dpduY = (duv12Y * dp02Y - duv02Y * dp12Y) * invDeterminant;
			dpduZ = (duv12Y * dp02Z - duv02Y * dp12Z) * invDeterminant;
			
			dpdvX = (-duv12X * dp02X + duv02X * dp12X) * invDeterminant;
			dpdvY = (-duv12X * dp02Y + duv02X * dp12Y) * invDeterminant;
			dpdvZ = (-duv12X * dp02Z + duv02X * dp12Z) * invDeterminant;
		}
		
		final float crossProductX = dpduY * dpdvZ - dpduZ * dpdvY;
		final float crossProductY = dpduZ * dpdvX - dpduX * dpdvZ;
		final float crossProductZ = dpduX * dpdvY - dpduY * dpdvX;
		
		final float lengthSquared = crossProductX * crossProductX + crossProductY * crossProductY + crossProductZ * crossProductZ;
		
		if(degenerateUV || lengthSquared == 0.0F) {
			final float aX = triangleCPositionX - triangleAPositionX;
			final float aY = triangleCPositionY - triangleAPositionY;
			final float aZ = triangleCPositionZ - triangleAPositionZ;
			
			final float bX = triangleBPositionX - triangleAPositionX;
			final float bY = triangleBPositionY - triangleAPositionY;
			final float bZ = triangleBPositionZ - triangleAPositionZ;
			
			final float ngX = aY * bZ - aZ * bY;
			final float ngY = aZ * bX - aX * bZ;
			final float ngZ = aX * bY - aY * bX;
			
			final float lengthSquared2 = ngX * ngX + ngY * ngY + ngZ * ngZ;
			
			if(lengthSquared2 == 0.0F) {
				return;
			}
			
			final float length = sqrt(lengthSquared2);
			final float lengthReciprocal = 1.0F / length;
			
			final float ngNormalizedX = ngX * lengthReciprocal;
			final float ngNormalizedY = ngY * lengthReciprocal;
			final float ngNormalizedZ = ngZ * lengthReciprocal;
			
			final float wX = ngNormalizedX;
			final float wY = ngNormalizedY;
			final float wZ = ngNormalizedZ;
			
			final float uUnnormalizedX = abs(wX) > abs(wY) ? -wZ : 0.0F;
			final float uUnnormalizedY = abs(wX) > abs(wY) ? 0.0F : wZ;
			final float uUnnormalizedZ = abs(wX) > abs(wY) ? wX : -wY;
			final float uLengthReciprocal = vector3FLengthReciprocal(uUnnormalizedX, uUnnormalizedY, uUnnormalizedZ);
			final float uX = uUnnormalizedX * uLengthReciprocal;
			final float uY = uUnnormalizedY * uLengthReciprocal;
			final float uZ = uUnnormalizedZ * uLengthReciprocal;
			
			final float vX = wY * uZ - wZ * uY;
			final float vY = wZ * uX - wX * uZ;
			final float vZ = wX * uY - wY * uX;
			
			dpduX = uX;
			dpduY = uY;
			dpduZ = uZ;
			
			dpdvX = vX;
			dpdvY = vY;
			dpdvZ = vZ;
		}
		
		final float pHitX = b0 * triangleAPositionX + b1 * triangleBPositionX + b2 * triangleCPositionX;
		final float pHitY = b0 * triangleAPositionY + b1 * triangleBPositionY + b2 * triangleCPositionY;
		final float pHitZ = b0 * triangleAPositionZ + b1 * triangleBPositionZ + b2 * triangleCPositionZ;
		
		final float uvHitX = b0 * triangleATextureCoordinatesU + b1 * triangleBTextureCoordinatesU + b2 * triangleCTextureCoordinatesU;
		final float uvHitY = b0 * triangleATextureCoordinatesV + b1 * triangleBTextureCoordinatesV + b2 * triangleCTextureCoordinatesV;
		
		final float nUnnormalizedX = dp02Y * dp12Z - dp02Z * dp12Y;
		final float nUnnormalizedY = dp02Z * dp12X - dp02X * dp12Z;
		final float nUnnormalizedZ = dp02X * dp12Y - dp02Y * dp12X;
		final float nLengthReciprocal = vector3FLengthReciprocal(nUnnormalizedX, nUnnormalizedY, nUnnormalizedZ);
		final float nX = nUnnormalizedX * nLengthReciprocal;
		final float nY = nUnnormalizedY * nLengthReciprocal;
		final float nZ = nUnnormalizedZ * nLengthReciprocal;
		
		float nsX = b0 * triangleAOrthonormalBasisWX + b1 * triangleBOrthonormalBasisWX + b2 * triangleCOrthonormalBasisWX;
		float nsY = b0 * triangleAOrthonormalBasisWY + b1 * triangleBOrthonormalBasisWY + b2 * triangleCOrthonormalBasisWY;
		float nsZ = b0 * triangleAOrthonormalBasisWZ + b1 * triangleBOrthonormalBasisWZ + b2 * triangleCOrthonormalBasisWZ;
		
		if(vector3FLengthSquared(nsX, nsY, nsZ) > 0.0F) {
			final float nsLengthReciprocal = vector3FLengthReciprocal(nsX, nsY, nsZ);
			
			nsX *= nsLengthReciprocal;
			nsY *= nsLengthReciprocal;
			nsZ *= nsLengthReciprocal;
		} else {
			nsX = nX;
			nsY = nY;
			nsZ = nZ;
		}
		
		final float ssLengthReciprocal = vector3FLengthReciprocal(dpduX, dpduY, dpduZ);
		
		float ssX = dpduX * ssLengthReciprocal;
		float ssY = dpduY * ssLengthReciprocal;
		float ssZ = dpduZ * ssLengthReciprocal;
		
		float tsX = ssY * nsZ - ssZ * nsY;
		float tsY = ssZ * nsX - ssX * nsZ;
		float tsZ = ssX * nsY - ssY * nsX;
		
		if(vector3FLengthSquared(tsX, tsY, tsZ) > 0.0F) {
			final float tsLengthReciprocal = vector3FLengthReciprocal(tsX, tsY, tsZ);
			
			tsX *= tsLengthReciprocal;
			tsY *= tsLengthReciprocal;
			tsZ *= tsLengthReciprocal;
			
			ssX = tsY * nsZ - tsZ * nsY;
			ssY = tsZ * nsX - tsX * nsZ;
			ssZ = tsX * nsY - tsY * nsX;
		} else {
			final float wX = nX;
			final float wY = nY;
			final float wZ = nZ;
			
			final float uUnnormalizedX = abs(wX) > abs(wY) ? -wZ : 0.0F;
			final float uUnnormalizedY = abs(wX) > abs(wY) ? 0.0F : wZ;
			final float uUnnormalizedZ = abs(wX) > abs(wY) ? wX : -wY;
			final float uLengthReciprocal = vector3FLengthReciprocal(uUnnormalizedX, uUnnormalizedY, uUnnormalizedZ);
			final float uX = uUnnormalizedX * uLengthReciprocal;
			final float uY = uUnnormalizedY * uLengthReciprocal;
			final float uZ = uUnnormalizedZ * uLengthReciprocal;
			
			final float vX = wY * uZ - wZ * uY;
			final float vY = wZ * uX - wX * uZ;
			final float vZ = wX * uY - wY * uX;
			
			ssX = uX;
			ssY = uY;
			ssZ = uZ;
			
			tsX = vX;
			tsY = vY;
			tsZ = vZ;
		}
		
		final float wUnnormalizedX = ssY * tsZ - ssZ * tsY;
		final float wUnnormalizedY = ssZ * tsX - ssX * tsZ;
		final float wUnnormalizedZ = ssX * tsY - ssY * tsX;
		final float wLengthReciprocal = vector3FLengthReciprocal(wUnnormalizedX, wUnnormalizedY, wUnnormalizedZ);
		final float wX = wUnnormalizedX * wLengthReciprocal;
		final float wY = wUnnormalizedY * wLengthReciprocal;
		final float wZ = wUnnormalizedZ * wLengthReciprocal;
		
		final boolean isNegating = nsX * wX + nsY * wY + nsZ * wZ < 0.0F;
		
		if(isNegating) {
			nsX = -nsX;
			nsY = -nsY;
			nsZ = -nsZ;
		}
		
		orthonormalBasis33FSetFromWVU(nsX, nsY, nsZ, tsX, tsY, tsZ, ssX, ssY, ssZ);
		
		intersectionLHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		
		orthonormalBasis33FSetFromWVU(wX, wY, wZ, tsX, tsY, tsZ, ssX, ssY, ssZ);
		
//		Update the intersection array:
		intersectionLHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionLHSSetPrimitiveIndex(primitiveIndex);
		intersectionLHSSetSurfaceIntersectionPoint(pHitX, pHitY, pHitZ);
		intersectionLHSSetTextureCoordinates(uvHitX, uvHitY);
		
		/*
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
		if(determinantUV != 0.0F) {
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
		*/
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
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
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
		
		final float p0T0X = triangleAPositionX - rayOriginX;
		final float p0T0Y = triangleAPositionY - rayOriginY;
		final float p0T0Z = triangleAPositionZ - rayOriginZ;
		
		final float p1T0X = triangleBPositionX - rayOriginX;
		final float p1T0Y = triangleBPositionY - rayOriginY;
		final float p1T0Z = triangleBPositionZ - rayOriginZ;
		
		final float p2T0X = triangleCPositionX - rayOriginX;
		final float p2T0Y = triangleCPositionY - rayOriginY;
		final float p2T0Z = triangleCPositionZ - rayOriginZ;
		
		int kz = abs(rayDirectionX) > abs(rayDirectionY) ? abs(rayDirectionX) > abs(rayDirectionZ) ? 0 : 2 : abs(rayDirectionY) > abs(rayDirectionZ) ? 1 : 2;
		int kx = kz + 1;
		
		if(kx == 3) {
			kx = 0;
		}
		
		int ky = kx + 1;
		
		if(ky == 3) {
			ky = 0;
		}
		
		final float dX = kx == 0 ? rayDirectionX : kx == 1 ? rayDirectionY : rayDirectionZ;
		final float dY = ky == 0 ? rayDirectionX : ky == 1 ? rayDirectionY : rayDirectionZ;
		final float dZ = kz == 0 ? rayDirectionX : kz == 1 ? rayDirectionY : rayDirectionZ;
		
		final float p0T1X = kx == 0 ? p0T0X : kx == 1 ? p0T0Y : p0T0Z;
		final float p0T1Y = ky == 0 ? p0T0X : ky == 1 ? p0T0Y : p0T0Z;
		final float p0T1Z = kz == 0 ? p0T0X : kz == 1 ? p0T0Y : p0T0Z;
		
		final float p1T1X = kx == 0 ? p1T0X : kx == 1 ? p1T0Y : p1T0Z;
		final float p1T1Y = ky == 0 ? p1T0X : ky == 1 ? p1T0Y : p1T0Z;
		final float p1T1Z = kz == 0 ? p1T0X : kz == 1 ? p1T0Y : p1T0Z;
		
		final float p2T1X = kx == 0 ? p2T0X : kx == 1 ? p2T0Y : p2T0Z;
		final float p2T1Y = ky == 0 ? p2T0X : ky == 1 ? p2T0Y : p2T0Z;
		final float p2T1Z = kz == 0 ? p2T0X : kz == 1 ? p2T0Y : p2T0Z;
		
		final float sx = -dX / dZ;
		final float sy = -dY / dZ;
		
		final float p0T2X = p0T1X + sx * p0T1Z;
		final float p0T2Y = p0T1Y + sy * p0T1Z;
		
		final float p1T2X = p1T1X + sx * p1T1Z;
		final float p1T2Y = p1T1Y + sy * p1T1Z;
		
		final float p2T2X = p2T1X + sx * p2T1Z;
		final float p2T2Y = p2T1Y + sy * p2T1Z;
		
		float e0 = p1T2X * p2T2Y - p1T2Y * p2T2X;
		float e1 = p2T2X * p0T2Y - p2T2Y * p0T2X;
		float e2 = p0T2X * p1T2Y - p0T2Y * p1T2X;
		
		if(e0 == 0.0F || e1 == 0.0F || e2 == 0.0F) {
			final double p2txp1ty = (double)(p2T2X) * (double)(p1T2Y);
			final double p2typ1tx = (double)(p2T2Y) * (double)(p1T2X);
			
			e0 = (float)(p2typ1tx - p2txp1ty);
			
			final double p0txp2ty = (double)(p0T2X) * (double)(p2T2Y);
			final double p0typ2tx = (double)(p0T2Y) * (double)(p2T2X);
			
			e1 = (float)(p0typ2tx - p0txp2ty);
			
			final double p1txp0ty = (double)(p1T2X) * (double)(p0T2Y);
			final double p1typ0tx = (double)(p1T2Y) * (double)(p0T2X);
			
			e2 = (float)(p1typ0tx - p1txp0ty);
		}
		
		final float det = e0 + e1 + e2;
		
		final float invDet = 1.0F / det;
		
		final float b0 = e0 * invDet;
		final float b1 = e1 * invDet;
		final float b2 = e2 * invDet;
		
		float dpduX = 0.0F;
		float dpduY = 0.0F;
		float dpduZ = 0.0F;
		
		float dpdvX = 0.0F;
		float dpdvY = 0.0F;
		float dpdvZ = 0.0F;
		
		final float duv02X = triangleATextureCoordinatesU - triangleCTextureCoordinatesU;
		final float duv02Y = triangleATextureCoordinatesV - triangleCTextureCoordinatesV;
		
		final float duv12X = triangleBTextureCoordinatesU - triangleCTextureCoordinatesU;
		final float duv12Y = triangleBTextureCoordinatesV - triangleCTextureCoordinatesV;
		
		final float dp02X = triangleAPositionX - triangleCPositionX;
		final float dp02Y = triangleAPositionY - triangleCPositionY;
		final float dp02Z = triangleAPositionZ - triangleCPositionZ;
		
		final float dp12X = triangleBPositionX - triangleCPositionX;
		final float dp12Y = triangleBPositionY - triangleCPositionY;
		final float dp12Z = triangleBPositionZ - triangleCPositionZ;
		
		final float determinant = duv02X * duv12Y - duv02Y * duv12X;
		
		final boolean degenerateUV = abs(determinant) < 1.0e-8F;
		
		if(!degenerateUV) {
			final float invDeterminant = 1.0F / determinant;
			
			dpduX = (duv12Y * dp02X - duv02Y * dp12X) * invDeterminant;
			dpduY = (duv12Y * dp02Y - duv02Y * dp12Y) * invDeterminant;
			dpduZ = (duv12Y * dp02Z - duv02Y * dp12Z) * invDeterminant;
			
			dpdvX = (-duv12X * dp02X + duv02X * dp12X) * invDeterminant;
			dpdvY = (-duv12X * dp02Y + duv02X * dp12Y) * invDeterminant;
			dpdvZ = (-duv12X * dp02Z + duv02X * dp12Z) * invDeterminant;
		}
		
		final float crossProductX = dpduY * dpdvZ - dpduZ * dpdvY;
		final float crossProductY = dpduZ * dpdvX - dpduX * dpdvZ;
		final float crossProductZ = dpduX * dpdvY - dpduY * dpdvX;
		
		final float lengthSquared = crossProductX * crossProductX + crossProductY * crossProductY + crossProductZ * crossProductZ;
		
		if(degenerateUV || lengthSquared == 0.0F) {
			final float aX = triangleCPositionX - triangleAPositionX;
			final float aY = triangleCPositionY - triangleAPositionY;
			final float aZ = triangleCPositionZ - triangleAPositionZ;
			
			final float bX = triangleBPositionX - triangleAPositionX;
			final float bY = triangleBPositionY - triangleAPositionY;
			final float bZ = triangleBPositionZ - triangleAPositionZ;
			
			final float ngX = aY * bZ - aZ * bY;
			final float ngY = aZ * bX - aX * bZ;
			final float ngZ = aX * bY - aY * bX;
			
			final float lengthSquared2 = ngX * ngX + ngY * ngY + ngZ * ngZ;
			
			if(lengthSquared2 == 0.0F) {
				return;
			}
			
			final float length = sqrt(lengthSquared2);
			final float lengthReciprocal = 1.0F / length;
			
			final float ngNormalizedX = ngX * lengthReciprocal;
			final float ngNormalizedY = ngY * lengthReciprocal;
			final float ngNormalizedZ = ngZ * lengthReciprocal;
			
			final float wX = ngNormalizedX;
			final float wY = ngNormalizedY;
			final float wZ = ngNormalizedZ;
			
			final float uUnnormalizedX = abs(wX) > abs(wY) ? -wZ : 0.0F;
			final float uUnnormalizedY = abs(wX) > abs(wY) ? 0.0F : wZ;
			final float uUnnormalizedZ = abs(wX) > abs(wY) ? wX : -wY;
			final float uLengthReciprocal = vector3FLengthReciprocal(uUnnormalizedX, uUnnormalizedY, uUnnormalizedZ);
			final float uX = uUnnormalizedX * uLengthReciprocal;
			final float uY = uUnnormalizedY * uLengthReciprocal;
			final float uZ = uUnnormalizedZ * uLengthReciprocal;
			
			final float vX = wY * uZ - wZ * uY;
			final float vY = wZ * uX - wX * uZ;
			final float vZ = wX * uY - wY * uX;
			
			dpduX = uX;
			dpduY = uY;
			dpduZ = uZ;
			
			dpdvX = vX;
			dpdvY = vY;
			dpdvZ = vZ;
		}
		
		final float pHitX = b0 * triangleAPositionX + b1 * triangleBPositionX + b2 * triangleCPositionX;
		final float pHitY = b0 * triangleAPositionY + b1 * triangleBPositionY + b2 * triangleCPositionY;
		final float pHitZ = b0 * triangleAPositionZ + b1 * triangleBPositionZ + b2 * triangleCPositionZ;
		
		final float uvHitX = b0 * triangleATextureCoordinatesU + b1 * triangleBTextureCoordinatesU + b2 * triangleCTextureCoordinatesU;
		final float uvHitY = b0 * triangleATextureCoordinatesV + b1 * triangleBTextureCoordinatesV + b2 * triangleCTextureCoordinatesV;
		
		final float nUnnormalizedX = dp02Y * dp12Z - dp02Z * dp12Y;
		final float nUnnormalizedY = dp02Z * dp12X - dp02X * dp12Z;
		final float nUnnormalizedZ = dp02X * dp12Y - dp02Y * dp12X;
		final float nLengthReciprocal = vector3FLengthReciprocal(nUnnormalizedX, nUnnormalizedY, nUnnormalizedZ);
		final float nX = nUnnormalizedX * nLengthReciprocal;
		final float nY = nUnnormalizedY * nLengthReciprocal;
		final float nZ = nUnnormalizedZ * nLengthReciprocal;
		
		float nsX = b0 * triangleAOrthonormalBasisWX + b1 * triangleBOrthonormalBasisWX + b2 * triangleCOrthonormalBasisWX;
		float nsY = b0 * triangleAOrthonormalBasisWY + b1 * triangleBOrthonormalBasisWY + b2 * triangleCOrthonormalBasisWY;
		float nsZ = b0 * triangleAOrthonormalBasisWZ + b1 * triangleBOrthonormalBasisWZ + b2 * triangleCOrthonormalBasisWZ;
		
		if(vector3FLengthSquared(nsX, nsY, nsZ) > 0.0F) {
			final float nsLengthReciprocal = vector3FLengthReciprocal(nsX, nsY, nsZ);
			
			nsX *= nsLengthReciprocal;
			nsY *= nsLengthReciprocal;
			nsZ *= nsLengthReciprocal;
		} else {
			nsX = nX;
			nsY = nY;
			nsZ = nZ;
		}
		
		final float ssLengthReciprocal = vector3FLengthReciprocal(dpduX, dpduY, dpduZ);
		
		float ssX = dpduX * ssLengthReciprocal;
		float ssY = dpduY * ssLengthReciprocal;
		float ssZ = dpduZ * ssLengthReciprocal;
		
		float tsX = ssY * nsZ - ssZ * nsY;
		float tsY = ssZ * nsX - ssX * nsZ;
		float tsZ = ssX * nsY - ssY * nsX;
		
		if(vector3FLengthSquared(tsX, tsY, tsZ) > 0.0F) {
			final float tsLengthReciprocal = vector3FLengthReciprocal(tsX, tsY, tsZ);
			
			tsX *= tsLengthReciprocal;
			tsY *= tsLengthReciprocal;
			tsZ *= tsLengthReciprocal;
			
			ssX = tsY * nsZ - tsZ * nsY;
			ssY = tsZ * nsX - tsX * nsZ;
			ssZ = tsX * nsY - tsY * nsX;
		} else {
			final float wX = nX;
			final float wY = nY;
			final float wZ = nZ;
			
			final float uUnnormalizedX = abs(wX) > abs(wY) ? -wZ : 0.0F;
			final float uUnnormalizedY = abs(wX) > abs(wY) ? 0.0F : wZ;
			final float uUnnormalizedZ = abs(wX) > abs(wY) ? wX : -wY;
			final float uLengthReciprocal = vector3FLengthReciprocal(uUnnormalizedX, uUnnormalizedY, uUnnormalizedZ);
			final float uX = uUnnormalizedX * uLengthReciprocal;
			final float uY = uUnnormalizedY * uLengthReciprocal;
			final float uZ = uUnnormalizedZ * uLengthReciprocal;
			
			final float vX = wY * uZ - wZ * uY;
			final float vY = wZ * uX - wX * uZ;
			final float vZ = wX * uY - wY * uX;
			
			ssX = uX;
			ssY = uY;
			ssZ = uZ;
			
			tsX = vX;
			tsY = vY;
			tsZ = vZ;
		}
		
		final float wUnnormalizedX = ssY * tsZ - ssZ * tsY;
		final float wUnnormalizedY = ssZ * tsX - ssX * tsZ;
		final float wUnnormalizedZ = ssX * tsY - ssY * tsX;
		final float wLengthReciprocal = vector3FLengthReciprocal(wUnnormalizedX, wUnnormalizedY, wUnnormalizedZ);
		final float wX = wUnnormalizedX * wLengthReciprocal;
		final float wY = wUnnormalizedY * wLengthReciprocal;
		final float wZ = wUnnormalizedZ * wLengthReciprocal;
		
		final boolean isNegating = nsX * wX + nsY * wY + nsZ * wZ < 0.0F;
		
		if(isNegating) {
			nsX = -nsX;
			nsY = -nsY;
			nsZ = -nsZ;
		}
		
		orthonormalBasis33FSetFromWVU(nsX, nsY, nsZ, tsX, tsY, tsZ, ssX, ssY, ssZ);
		
		intersectionRHSSetOrthonormalBasisGFromOrthonormalBasis33F();
		
		orthonormalBasis33FSetFromWVU(wX, wY, wZ, tsX, tsY, tsZ, ssX, ssY, ssZ);
		
//		Update the intersection array:
		intersectionRHSSetOrthonormalBasisSFromOrthonormalBasis33F();
		intersectionRHSSetPrimitiveIndex(primitiveIndex);
		intersectionRHSSetSurfaceIntersectionPoint(pHitX, pHitY, pHitZ);
		intersectionRHSSetTextureCoordinates(uvHitX, uvHitY);
		
		/*
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
		if(determinantUV != 0.0F) {
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
		*/
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
	protected final float shape3FTriangleMesh3FIntersectionTLHS(final int shape3FTriangleMesh3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
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
						this.shape3FTriangleMesh3FArrayToShape3FTriangle3FArrayLHS_$private$1[0] = triangleOffset;
						
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
	 * Returns the parametric T value for a given triangle mesh in object space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @param shape3FTriangleMesh3FArrayOffset the offset for the triangle mesh in {@link #shape3FTriangleMesh3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return the parametric T value for a given triangle mesh in object space, or {@code 0.0F} if no intersection was found
	 */
	protected final float shape3FTriangleMesh3FIntersectionTRHS(final int shape3FTriangleMesh3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
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
						this.shape3FTriangleMesh3FArrayToShape3FTriangle3FArrayRHS_$private$1[0] = triangleOffset;
						
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
		final int shape3FTriangle3FArrayOffset = this.shape3FTriangleMesh3FArrayToShape3FTriangle3FArrayLHS_$private$1[0];
		
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
		final int shape3FTriangle3FArrayOffset = this.shape3FTriangleMesh3FArrayToShape3FTriangle3FArrayRHS_$private$1[0];
		
		if(shape3FTriangle3FArrayOffset != -1) {
			shape3FTriangle3FIntersectionComputeRHS(t, primitiveIndex, shape3FTriangle3FArrayOffset);
		}
	}
}