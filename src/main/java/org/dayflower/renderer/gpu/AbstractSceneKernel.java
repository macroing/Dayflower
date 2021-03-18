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

import java.lang.reflect.Field;
import java.util.Objects;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.geometry.boundingvolume.BoundingSphere3F;
import org.dayflower.geometry.boundingvolume.InfiniteBoundingVolume3F;
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
import org.dayflower.scene.Camera;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.utility.Floats;

/**
 * An {@code AbstractSceneKernel} is an abstract extension of the {@link AbstractLightKernel} class that adds additional features.
 * <p>
 * The features added are the following:
 * <ul>
 * <li>Camera ray generation methods</li>
 * <li>Primitive intersection methods</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractSceneKernel extends AbstractLightKernel {
//	TODO: Add Javadocs!
	protected float[] cameraArray;
	
//	TODO: Add Javadocs!
	protected float[] matrix44FArray;
	
//	TODO: Add Javadocs!
	protected float[] pixelArray;
	
//	TODO: Add Javadocs!
	protected int primitiveCount;
	
//	TODO: Add Javadocs!
	protected int[] primitiveArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Scene scene;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractSceneKernel} instance.
	 */
	protected AbstractSceneKernel() {
		this.cameraArray = new float[1];
		this.matrix44FArray = new float[1];
		this.pixelArray = new float[1];
		this.primitiveCount = 0;
		this.primitiveArray = new int[1];
		this.scene = new Scene();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link Scene} instance that is associated with this {@code AbstractSceneKernel} instance.
	 * 
	 * @return the {@code Scene} instance that is associated with this {@code AbstractSceneKernel} instance
	 */
	public final Scene getScene() {
		return this.scene;
	}
	
	/**
	 * Sets the {@link Scene} instance that is associated with this {@code AbstractSceneKernel} instance to {@code scene}.
	 * <p>
	 * If {@code scene} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param scene a {@code Scene} instance
	 * @throws NullPointerException thrown if, and only if, {@code scene} is {@code null}
	 */
	public final void setScene(final Scene scene) {
		this.scene = Objects.requireNonNull(scene, "scene == null");
	}
	
	/**
	 * Sets up all necessary resources for this {@code AbstractSceneKernel} instance.
	 */
	@Override
	public void setup() {
		super.setup();
		
		doSetupPixelArray();
		doSetupScene();
	}
	
	/**
	 * Updates the {@link Camera} instance.
	 */
	public final void updateCamera() {
		put(this.cameraArray = getScene().getCamera().toArray());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Performs an intersection test against all primitives in the scene and computes intersection information for the closest.
	 * <p>
	 * Returns {@code true} if, and only if, an intersection was found, {@code false} otherwise.
	 * <p>
	 * If an intersection was found, the computed information will be present in {@link #intersectionArray_$private$24} in world space.
	 * 
	 * @return {@code true} if, and only if, an intersection was found, {@code false} otherwise
	 */
	protected final boolean primitiveIntersectionCompute() {
		int primitiveIndex = -1;
		
		this.shape3FTriangleMesh3FArrayToShape3FTriangle3FArray_$private$1[0] = -1;
		
		for(int index = 0; index < this.primitiveCount; index++) {
			final int primitiveArrayOffset = index * Primitive.ARRAY_LENGTH;
			final int primitiveArrayOffsetBoundingVolumeID = primitiveArrayOffset + Primitive.ARRAY_OFFSET_BOUNDING_VOLUME_ID;
			final int primitiveArrayOffsetBoundingVolumeOffset = primitiveArrayOffset + Primitive.ARRAY_OFFSET_BOUNDING_VOLUME_OFFSET;
			final int primitiveArrayOffsetShapeID = primitiveArrayOffset + Primitive.ARRAY_OFFSET_SHAPE_ID;
			final int primitiveArrayOffsetShapeOffset = primitiveArrayOffset + Primitive.ARRAY_OFFSET_SHAPE_OFFSET;
			
			final int boundingVolumeID = this.primitiveArray[primitiveArrayOffsetBoundingVolumeID];
			final int boundingVolumeOffset = this.primitiveArray[primitiveArrayOffsetBoundingVolumeOffset];
			final int shapeID = this.primitiveArray[primitiveArrayOffsetShapeID];
			final int shapeOffset = this.primitiveArray[primitiveArrayOffsetShapeOffset];
			
			final float tMinimumWorldSpace = ray3FGetTMinimum();
			final float tMaximumWorldSpace = ray3FGetTMaximum();
			
			boolean isIntersectingBoundingVolume = false;
			
//			TODO: Find out what causes the order of the if-statements to fail. If InfiniteBoundingVolume3F.ID is checked in the last if-statement, the plane will disappear.
			if(boundingVolumeID == InfiniteBoundingVolume3F.ID) {
				isIntersectingBoundingVolume = true;
			} else if(boundingVolumeID == AxisAlignedBoundingBox3F.ID) {
				isIntersectingBoundingVolume = boundingVolume3FAxisAlignedBoundingBox3FContainsOrIntersects(boundingVolumeOffset, tMinimumWorldSpace, tMaximumWorldSpace);
			} else if(boundingVolumeID == BoundingSphere3F.ID) {
				isIntersectingBoundingVolume = boundingVolume3FBoundingSphere3FContainsOrIntersects(boundingVolumeOffset, tMinimumWorldSpace, tMaximumWorldSpace);
			}
			
			if(isIntersectingBoundingVolume) {
				ray3FSetMatrix44FTransformWorldToObject(index);
				
				float tObjectSpace = 0.0F;
				
				final float tMinimumObjectSpace = ray3FGetTMinimum();
				final float tMaximumObjectSpace = ray3FGetTMaximum();
				
				if(shapeID == Cone3F.ID) {
					tObjectSpace = shape3FCone3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Cylinder3F.ID) {
					tObjectSpace = shape3FCylinder3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Disk3F.ID) {
					tObjectSpace = shape3FDisk3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Paraboloid3F.ID) {
					tObjectSpace = shape3FParaboloid3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Plane3F.ID) {
					tObjectSpace = shape3FPlane3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == RectangularCuboid3F.ID) {
					tObjectSpace = shape3FRectangularCuboid3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Sphere3F.ID) {
					tObjectSpace = shape3FSphere3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Torus3F.ID) {
					tObjectSpace = shape3FTorus3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Triangle3F.ID) {
					tObjectSpace = shape3FTriangle3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == TriangleMesh3F.ID) {
					tObjectSpace = shape3FTriangleMesh3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				}
				
				if(tObjectSpace > tMinimumObjectSpace && tObjectSpace < tMaximumObjectSpace) {
					ray3FSetTMaximum(tObjectSpace);
					
					primitiveIndex = index;
				}
				
				ray3FSetMatrix44FTransformObjectToWorld(index);
			}
		}
		
		if(primitiveIndex != -1) {
			final int primitiveArrayOffset = primitiveIndex * Primitive.ARRAY_LENGTH;
			final int primitiveArrayOffsetShapeID = primitiveArrayOffset + Primitive.ARRAY_OFFSET_SHAPE_ID;
			final int primitiveArrayOffsetShapeOffset = primitiveArrayOffset + Primitive.ARRAY_OFFSET_SHAPE_OFFSET;
			
			final int shapeID = this.primitiveArray[primitiveArrayOffsetShapeID];
			final int shapeOffset = this.primitiveArray[primitiveArrayOffsetShapeOffset];
			
			ray3FSetMatrix44FTransformWorldToObject(primitiveIndex);
			
			final float tObjectSpace = ray3FGetTMaximum();
			
			if(shapeID == Cone3F.ID) {
				shape3FCone3FIntersectionCompute(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Cylinder3F.ID) {
				shape3FCylinder3FIntersectionCompute(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Disk3F.ID) {
				shape3FDisk3FIntersectionCompute(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Paraboloid3F.ID) {
				shape3FParaboloid3FIntersectionCompute(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Plane3F.ID) {
				shape3FPlane3FIntersectionCompute(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == RectangularCuboid3F.ID) {
				shape3FRectangularCuboid3FIntersectionCompute(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Sphere3F.ID) {
				shape3FSphere3FIntersectionCompute(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Torus3F.ID) {
				shape3FTorus3FIntersectionCompute(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Triangle3F.ID) {
				shape3FTriangle3FIntersectionCompute(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == TriangleMesh3F.ID) {
				shape3FTriangleMesh3FIntersectionCompute(tObjectSpace, primitiveIndex);
			}
			
			ray3FSetMatrix44FTransformObjectToWorld(primitiveIndex);
			
			intersectionTransformObjectToWorld(primitiveIndex);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given primitive in world space, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the current ray intersects a given primitive in world space, {@code false} otherwise
	 */
	protected final boolean primitiveIntersects() {
		return primitiveIntersectionT() > 0.0F;
	}
	
	/**
	 * Generates a ray for the current pixel using the current camera.
	 * <p>
	 * Returns {@code true} if, and only if, a ray was generated, {@code false} otherwise.
	 * <p>
	 * If the current camera uses a thin lens, this method should always return {@code true}. If, on the other hand, the current camera uses a fisheye lens, this may not always be the case.
	 * <p>
	 * If this method returns {@code true}, {@code pixelX} and {@code pixelY} will be set in {@link #pixelArray}.
	 * 
	 * @param pixelX the sample on the X-axis within the current pixel
	 * @param pixelY the sample on the Y-axis within the current pixel
	 * @return {@code true} if, and only if, a ray was generated, {@code false} otherwise
	 */
	protected final boolean ray3FCameraGenerate(final float pixelX, final float pixelY) {
//		Retrieve the image coordinates:
		final float imageX = getGlobalId() % this.resolutionX;
		final float imageY = getGlobalId() / this.resolutionX;
		
//		Retrieve all values from the 'cameraArray' in the correct order:
		final float fieldOfViewX = tan(+this.cameraArray[Camera.ARRAY_OFFSET_FIELD_OF_VIEW_X] * 0.5F);
		final float fieldOfViewY = tan(-this.cameraArray[Camera.ARRAY_OFFSET_FIELD_OF_VIEW_Y] * 0.5F);
		final float lens = this.cameraArray[Camera.ARRAY_OFFSET_LENS];
		final float uX = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_U + 0];
		final float uY = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_U + 1];
		final float uZ = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_U + 2];
		final float vX = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_V + 0];
		final float vY = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_V + 1];
		final float vZ = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_V + 2];
		final float wX = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_W + 0];
		final float wY = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_W + 1];
		final float wZ = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_W + 2];
		final float eyeX = this.cameraArray[Camera.ARRAY_OFFSET_EYE + 0];
		final float eyeY = this.cameraArray[Camera.ARRAY_OFFSET_EYE + 1];
		final float eyeZ = this.cameraArray[Camera.ARRAY_OFFSET_EYE + 2];
		final float apertureRadius = this.cameraArray[Camera.ARRAY_OFFSET_APERTURE_RADIUS];
		final float focalDistance = this.cameraArray[Camera.ARRAY_OFFSET_FOCAL_DISTANCE];
		final float resolutionX = this.cameraArray[Camera.ARRAY_OFFSET_RESOLUTION_X];
		final float resolutionY = this.cameraArray[Camera.ARRAY_OFFSET_RESOLUTION_Y];
		
//		Compute the camera coordinates:
		final float cameraX = 2.0F * ((imageX + pixelX) / (resolutionX - 1.0F)) - 1.0F;
		final float cameraY = 2.0F * ((imageY + pixelY) / (resolutionY - 1.0F)) - 1.0F;
		
//		Compute the 'wFactor' that is used for the fisheye lens:
		float wFactor = 1.0F;
		
		if(lens == 0.0F) {
			final float dotProduct = cameraX * cameraX + cameraY * cameraY;
			
			if(dotProduct > 1.0F) {
				return false;
			}
			
			wFactor = sqrt(1.0F - dotProduct);
		}
		
//		Sample the disk with a uniform distribution:
		final float u = random();
		final float v = random();
		final float r = sqrt(u);
		final float theta = PI_MULTIPLIED_BY_2 * v;
		final float diskX = r * cos(theta);
		final float diskY = r * sin(theta);
		
//		Compute the point on the plane one unit away from the eye:
		final float pointOnPlaneOneUnitAwayFromEyeX = (uX * fieldOfViewX * cameraX) + (vX * fieldOfViewY * cameraY) + (eyeX + wX * wFactor);
		final float pointOnPlaneOneUnitAwayFromEyeY = (uY * fieldOfViewX * cameraX) + (vY * fieldOfViewY * cameraY) + (eyeY + wY * wFactor);
		final float pointOnPlaneOneUnitAwayFromEyeZ = (uZ * fieldOfViewX * cameraX) + (vZ * fieldOfViewY * cameraY) + (eyeZ + wZ * wFactor);
		
//		Compute the point on the image plane:
		final float pointOnImagePlaneX = eyeX + (pointOnPlaneOneUnitAwayFromEyeX - eyeX) * focalDistance;
		final float pointOnImagePlaneY = eyeY + (pointOnPlaneOneUnitAwayFromEyeY - eyeY) * focalDistance;
		final float pointOnImagePlaneZ = eyeZ + (pointOnPlaneOneUnitAwayFromEyeZ - eyeZ) * focalDistance;
		
//		Compute the ray origin:
		final float originX = apertureRadius > 0.00001F ? eyeX + ((uX * diskX * apertureRadius) + (vX * diskY * apertureRadius)) : eyeX;
		final float originY = apertureRadius > 0.00001F ? eyeY + ((uY * diskX * apertureRadius) + (vY * diskY * apertureRadius)) : eyeY;
		final float originZ = apertureRadius > 0.00001F ? eyeZ + ((uZ * diskX * apertureRadius) + (vZ * diskY * apertureRadius)) : eyeZ;
		
//		Compute the ray direction:
		final float directionX = pointOnImagePlaneX - originX;
		final float directionY = pointOnImagePlaneY - originY;
		final float directionZ = pointOnImagePlaneZ - originZ;
		final float directionLengthReciprocal = rsqrt(directionX * directionX + directionY * directionY + directionZ * directionZ);
		final float directionNormalizedX = directionX * directionLengthReciprocal;
		final float directionNormalizedY = directionY * directionLengthReciprocal;
		final float directionNormalizedZ = directionZ * directionLengthReciprocal;
		
//		Compute offsets:
		final int pixelArrayOffset = getGlobalId() * 2;
		
//		Fill in the ray array:
		ray3FSetOrigin(originX, originY, originZ);
		ray3FSetDirection(directionNormalizedX, directionNormalizedY, directionNormalizedZ);
		ray3FSetTMinimum(DEFAULT_T_MINIMUM);
		ray3FSetTMaximum(DEFAULT_T_MAXIMUM);
		
//		Fill in the pixel array:
		this.pixelArray[pixelArrayOffset + 0] = pixelX;
		this.pixelArray[pixelArrayOffset + 1] = pixelY;
		
		return true;
	}
	
	/**
	 * Returns the parametric T value for the closest primitive in world space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @return the parametric T value for the closest primitive in world space, or {@code 0.0F} if no intersection was found
	 */
	protected final float primitiveIntersectionT() {
		boolean hasFoundIntersection = false;
		
		for(int index = 0; index < this.primitiveCount; index++) {
			final int primitiveArrayOffset = index * Primitive.ARRAY_LENGTH;
			final int primitiveArrayOffsetBoundingVolumeID = primitiveArrayOffset + Primitive.ARRAY_OFFSET_BOUNDING_VOLUME_ID;
			final int primitiveArrayOffsetBoundingVolumeOffset = primitiveArrayOffset + Primitive.ARRAY_OFFSET_BOUNDING_VOLUME_OFFSET;
			final int primitiveArrayOffsetShapeID = primitiveArrayOffset + Primitive.ARRAY_OFFSET_SHAPE_ID;
			final int primitiveArrayOffsetShapeOffset = primitiveArrayOffset + Primitive.ARRAY_OFFSET_SHAPE_OFFSET;
			
			final int boundingVolumeID = this.primitiveArray[primitiveArrayOffsetBoundingVolumeID];
			final int boundingVolumeOffset = this.primitiveArray[primitiveArrayOffsetBoundingVolumeOffset];
			final int shapeID = this.primitiveArray[primitiveArrayOffsetShapeID];
			final int shapeOffset = this.primitiveArray[primitiveArrayOffsetShapeOffset];
			
			final float tMinimumWorldSpace = ray3FGetTMinimum();
			final float tMaximumWorldSpace = ray3FGetTMaximum();
			
			boolean isIntersectingBoundingVolume = false;
			
//			TODO: Find out what causes the order of the if-statements to fail. If InfiniteBoundingVolume3F.ID is checked in the last if-statement, the plane will disappear.
			if(boundingVolumeID == InfiniteBoundingVolume3F.ID) {
				isIntersectingBoundingVolume = true;
			} else if(boundingVolumeID == AxisAlignedBoundingBox3F.ID) {
				isIntersectingBoundingVolume = boundingVolume3FAxisAlignedBoundingBox3FContainsOrIntersects(boundingVolumeOffset, tMinimumWorldSpace, tMaximumWorldSpace);
			} else if(boundingVolumeID == BoundingSphere3F.ID) {
				isIntersectingBoundingVolume = boundingVolume3FBoundingSphere3FContainsOrIntersects(boundingVolumeOffset, tMinimumWorldSpace, tMaximumWorldSpace);
			}
			
			if(isIntersectingBoundingVolume) {
				ray3FSetMatrix44FTransformWorldToObject(index);
				
				float tObjectSpace = 0.0F;
				
				final float tMinimumObjectSpace = ray3FGetTMinimum();
				final float tMaximumObjectSpace = ray3FGetTMaximum();
				
				if(shapeID == Cone3F.ID) {
					tObjectSpace = shape3FCone3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Cylinder3F.ID) {
					tObjectSpace = shape3FCylinder3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Disk3F.ID) {
					tObjectSpace = shape3FDisk3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Paraboloid3F.ID) {
					tObjectSpace = shape3FParaboloid3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Plane3F.ID) {
					tObjectSpace = shape3FPlane3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == RectangularCuboid3F.ID) {
					tObjectSpace = shape3FRectangularCuboid3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Sphere3F.ID) {
					tObjectSpace = shape3FSphere3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Torus3F.ID) {
					tObjectSpace = shape3FTorus3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Triangle3F.ID) {
					tObjectSpace = shape3FTriangle3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == TriangleMesh3F.ID) {
					tObjectSpace = shape3FTriangleMesh3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				}
				
				if(tObjectSpace > tMinimumObjectSpace && tObjectSpace < tMaximumObjectSpace) {
					ray3FSetTMaximum(tObjectSpace);
					
					hasFoundIntersection = true;
				}
				
				ray3FSetMatrix44FTransformObjectToWorld(index);
			}
		}
		
		return hasFoundIntersection ? ray3FGetTMaximum() : 0.0F;
	}
	
	/**
	 * Returns the {@code float[]} with the pixel samples.
	 * 
	 * @return the {@code float[]} with the pixel samples
	 */
	protected final float[] getPixelArray() {
		return getAndReturn(this.pixelArray);
	}
	
//	TODO: Add Javadocs!
	protected final int primitiveGetMaterialID() {
		return this.primitiveArray[intersectionGetPrimitiveIndex() * Primitive.ARRAY_LENGTH + Primitive.ARRAY_OFFSET_MATERIAL_ID];
	}
	
//	TODO: Add Javadocs!
	protected final int primitiveGetMaterialOffset() {
		return this.primitiveArray[intersectionGetPrimitiveIndex() * Primitive.ARRAY_LENGTH + Primitive.ARRAY_OFFSET_MATERIAL_OFFSET];
	}
	
//	TODO: Add Javadocs!
	protected final void intersectionTransform(final int matrix44FArrayOffsetMatrix, final int matrix44FArrayOffsetMatrixInverse) {
//		Retrieve the matrix elements:
		final float matrixElement11 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_1_1];
		final float matrixElement12 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_1_2];
		final float matrixElement13 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_1_3];
		final float matrixElement14 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_1_4];
		final float matrixElement21 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_2_1];
		final float matrixElement22 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_2_2];
		final float matrixElement23 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_2_3];
		final float matrixElement24 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_2_4];
		final float matrixElement31 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_3_1];
		final float matrixElement32 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_3_2];
		final float matrixElement33 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_3_3];
		final float matrixElement34 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_3_4];
		final float matrixElement41 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_4_1];
		final float matrixElement42 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_4_2];
		final float matrixElement43 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_4_3];
		final float matrixElement44 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_4_4];
		
//		Retrieve the matrix inverse elements:
		final float matrixInverseElement11 = this.matrix44FArray[matrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_1_1];
		final float matrixInverseElement12 = this.matrix44FArray[matrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_1_2];
		final float matrixInverseElement13 = this.matrix44FArray[matrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_1_3];
		final float matrixInverseElement21 = this.matrix44FArray[matrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_2_1];
		final float matrixInverseElement22 = this.matrix44FArray[matrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_2_2];
		final float matrixInverseElement23 = this.matrix44FArray[matrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_2_3];
		final float matrixInverseElement31 = this.matrix44FArray[matrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_3_1];
		final float matrixInverseElement32 = this.matrix44FArray[matrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_3_2];
		final float matrixInverseElement33 = this.matrix44FArray[matrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_3_3];
		
//		Retrieve the old variables from the intersection array:
		final float oldOrthonormalBasisGUX = intersectionGetOrthonormalBasisGUComponent1();
		final float oldOrthonormalBasisGUY = intersectionGetOrthonormalBasisGUComponent2();
		final float oldOrthonormalBasisGUZ = intersectionGetOrthonormalBasisGUComponent3();
		final float oldOrthonormalBasisGVX = intersectionGetOrthonormalBasisGVComponent1();
		final float oldOrthonormalBasisGVY = intersectionGetOrthonormalBasisGVComponent2();
		final float oldOrthonormalBasisGVZ = intersectionGetOrthonormalBasisGVComponent3();
		final float oldOrthonormalBasisGWX = intersectionGetOrthonormalBasisGWComponent1();
		final float oldOrthonormalBasisGWY = intersectionGetOrthonormalBasisGWComponent2();
		final float oldOrthonormalBasisGWZ = intersectionGetOrthonormalBasisGWComponent3();
		final float oldOrthonormalBasisSUX = intersectionGetOrthonormalBasisSUComponent1();
		final float oldOrthonormalBasisSUY = intersectionGetOrthonormalBasisSUComponent2();
		final float oldOrthonormalBasisSUZ = intersectionGetOrthonormalBasisSUComponent3();
		final float oldOrthonormalBasisSVX = intersectionGetOrthonormalBasisSVComponent1();
		final float oldOrthonormalBasisSVY = intersectionGetOrthonormalBasisSVComponent2();
		final float oldOrthonormalBasisSVZ = intersectionGetOrthonormalBasisSVComponent3();
		final float oldOrthonormalBasisSWX = intersectionGetOrthonormalBasisSWComponent1();
		final float oldOrthonormalBasisSWY = intersectionGetOrthonormalBasisSWComponent2();
		final float oldOrthonormalBasisSWZ = intersectionGetOrthonormalBasisSWComponent3();
		final float oldSurfaceIntersectionPointX = intersectionGetSurfaceIntersectionPointComponent1();
		final float oldSurfaceIntersectionPointY = intersectionGetSurfaceIntersectionPointComponent2();
		final float oldSurfaceIntersectionPointZ = intersectionGetSurfaceIntersectionPointComponent3();
		
//		Transform the U-direction of the geometric orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisGUX, oldOrthonormalBasisGUY, oldOrthonormalBasisGUZ);
		
//		Retrieve the transformed U-direction of the geometric orthonormal basis:
		final float newOrthonormalBasisGUX = vector3FGetComponent1();
		final float newOrthonormalBasisGUY = vector3FGetComponent2();
		final float newOrthonormalBasisGUZ = vector3FGetComponent3();
		
//		Transform the V-direction of the geometric orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisGVX, oldOrthonormalBasisGVY, oldOrthonormalBasisGVZ);
		
//		Retrieve the transformed V-direction of the geometric orthonormal basis:
		final float newOrthonormalBasisGVX = vector3FGetComponent1();
		final float newOrthonormalBasisGVY = vector3FGetComponent2();
		final float newOrthonormalBasisGVZ = vector3FGetComponent3();
		
//		Transform the W-direction of the geometric orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisGWX, oldOrthonormalBasisGWY, oldOrthonormalBasisGWZ);
		
//		Retrieve the transformed W-direction of the geometric orthonormal basis:
		final float newOrthonormalBasisGWX = vector3FGetComponent1();
		final float newOrthonormalBasisGWY = vector3FGetComponent2();
		final float newOrthonormalBasisGWZ = vector3FGetComponent3();
		
//		Transform the U-direction of the shading orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisSUX, oldOrthonormalBasisSUY, oldOrthonormalBasisSUZ);
		
//		Retrieve the transformed U-direction of the shading orthonormal basis:
		final float newOrthonormalBasisSUX = vector3FGetComponent1();
		final float newOrthonormalBasisSUY = vector3FGetComponent2();
		final float newOrthonormalBasisSUZ = vector3FGetComponent3();
		
//		Transform the V-direction of the shading orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisSVX, oldOrthonormalBasisSVY, oldOrthonormalBasisSVZ);
		
//		Retrieve the transformed V-direction of the shading orthonormal basis:
		final float newOrthonormalBasisSVX = vector3FGetComponent1();
		final float newOrthonormalBasisSVY = vector3FGetComponent2();
		final float newOrthonormalBasisSVZ = vector3FGetComponent3();
		
//		Transform the W-direction of the shading orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisSWX, oldOrthonormalBasisSWY, oldOrthonormalBasisSWZ);
		
//		Retrieve the transformed W-direction of the shading orthonormal basis:
		final float newOrthonormalBasisSWX = vector3FGetComponent1();
		final float newOrthonormalBasisSWY = vector3FGetComponent2();
		final float newOrthonormalBasisSWZ = vector3FGetComponent3();
		
//		Transform the surface intersection point:
		point3FSetMatrix44FTransformAndDivide(matrixElement11, matrixElement12, matrixElement13, matrixElement14, matrixElement21, matrixElement22, matrixElement23, matrixElement24, matrixElement31, matrixElement32, matrixElement33, matrixElement34, matrixElement41, matrixElement42, matrixElement43, matrixElement44, oldSurfaceIntersectionPointX, oldSurfaceIntersectionPointY, oldSurfaceIntersectionPointZ);
		
//		Retrieve the transformed surface intersection point:
		final float newSurfaceIntersectionPointX = point3FGetComponent1();
		final float newSurfaceIntersectionPointY = point3FGetComponent2();
		final float newSurfaceIntersectionPointZ = point3FGetComponent3();
		
//		Update the intersection array:
		intersectionSetOrthonormalBasisG(newOrthonormalBasisGUX, newOrthonormalBasisGUY, newOrthonormalBasisGUZ, newOrthonormalBasisGVX, newOrthonormalBasisGVY, newOrthonormalBasisGVZ, newOrthonormalBasisGWX, newOrthonormalBasisGWY, newOrthonormalBasisGWZ);
		intersectionSetOrthonormalBasisS(newOrthonormalBasisSUX, newOrthonormalBasisSUY, newOrthonormalBasisSUZ, newOrthonormalBasisSVX, newOrthonormalBasisSVY, newOrthonormalBasisSVZ, newOrthonormalBasisSWX, newOrthonormalBasisSWY, newOrthonormalBasisSWZ);
		intersectionSetSurfaceIntersectionPoint(newSurfaceIntersectionPointX, newSurfaceIntersectionPointY, newSurfaceIntersectionPointZ);
	}
	
//	TODO: Add Javadocs!
	protected final void intersectionTransformObjectToWorld(final int primitiveIndex) {
		intersectionTransform(primitiveIndex * Matrix44F.ARRAY_SIZE * 2, primitiveIndex * Matrix44F.ARRAY_SIZE * 2 + Matrix44F.ARRAY_SIZE);
	}
	
//	TODO: Add Javadocs!
	protected final void intersectionTransformWorldToObject(final int primitiveIndex) {
		intersectionTransform(primitiveIndex * Matrix44F.ARRAY_SIZE * 2 + Matrix44F.ARRAY_SIZE, primitiveIndex * Matrix44F.ARRAY_SIZE * 2);
	}
	
	/**
	 * Sets a ray in {@link #ray3FArray_$private$8}.
	 * <p>
	 * The ray is constructed by transforming the current ray in {@code ray3FArray_$private$8} with the object-to-world matrix of the primitive at index {@code primitiveIndex}.
	 * 
	 * @param primitiveIndex the index of the primitive
	 */
	protected final void ray3FSetMatrix44FTransformObjectToWorld(final int primitiveIndex) {
		doRay3FSetMatrix44FTransform(primitiveIndex * Matrix44F.ARRAY_SIZE * 2);
	}
	
	/**
	 * Sets a ray in {@link #ray3FArray_$private$8}.
	 * <p>
	 * The ray is constructed by transforming the current ray in {@code ray3FArray_$private$8} with the world-to-object matrix of the primitive at index {@code primitiveIndex}.
	 * 
	 * @param primitiveIndex the index of the primitive
	 */
	protected final void ray3FSetMatrix44FTransformWorldToObject(final int primitiveIndex) {
		doRay3FSetMatrix44FTransform(primitiveIndex * Matrix44F.ARRAY_SIZE * 2 + Matrix44F.ARRAY_SIZE);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doRay3FSetMatrix44FTransform(final int matrix44FArrayOffset) {
//		Retrieve the matrix elements:
		final float element11 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_1_1];
		final float element12 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_1_2];
		final float element13 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_1_3];
		final float element14 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_1_4];
		final float element21 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_2_1];
		final float element22 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_2_2];
		final float element23 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_2_3];
		final float element24 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_2_4];
		final float element31 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_3_1];
		final float element32 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_3_2];
		final float element33 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_3_3];
		final float element34 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_3_4];
		final float element41 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_4_1];
		final float element42 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_4_2];
		final float element43 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_4_3];
		final float element44 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_4_4];
		
//		Retrieve the ray origin components from the old space:
		final float oldOriginX = ray3FGetOriginComponent1();
		final float oldOriginY = ray3FGetOriginComponent2();
		final float oldOriginZ = ray3FGetOriginComponent3();
		
//		Retrieve the ray direction components from the old space:
		final float oldDirectionX = ray3FGetDirectionComponent1();
		final float oldDirectionY = ray3FGetDirectionComponent2();
		final float oldDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the ray boundary variables from the old space:
		final float oldTMinimum = ray3FGetTMinimum();
		final float oldTMaximum = ray3FGetTMaximum();
		
//		Transform the ray origin from the old space to the new space:
		point3FSetMatrix44FTransformAndDivide(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, oldOriginX, oldOriginY, oldOriginZ);
		
//		Transform the ray direction from the old space to the new space:
		vector3FSetMatrix44FTransformNormalize(element11, element12, element13, element21, element22, element23, element31, element32, element33, oldDirectionX, oldDirectionY, oldDirectionZ);
		
//		Retrieve the ray origin components from the new space:
		final float newOriginX = point3FGetComponent1();
		final float newOriginY = point3FGetComponent2();
		final float newOriginZ = point3FGetComponent3();
		
//		Retrieve the ray direction components from the new space:
		final float newDirectionX = vector3FGetComponent1();
		final float newDirectionY = vector3FGetComponent2();
		final float newDirectionZ = vector3FGetComponent3();
		
//		Initialize the ray boundary variables of the new space to the ray boundary variables from the old space:
		float newTMinimum = oldTMinimum;
		float newTMaximum = oldTMaximum;
		
//		Check if the new minimum ray boundary should be computed:
		if(newTMinimum > DEFAULT_T_MINIMUM && newTMinimum < DEFAULT_T_MAXIMUM) {
//			Compute a reference point in the old space:
			final float oldReferencePointTMinimumX = oldOriginX + oldDirectionX * oldTMinimum;
			final float oldReferencePointTMinimumY = oldOriginY + oldDirectionY * oldTMinimum;
			final float oldReferencePointTMinimumZ = oldOriginZ + oldDirectionZ * oldTMinimum;
			
//			Transform the reference point from the old space to the new space:
			point3FSetMatrix44FTransformAndDivide(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, oldReferencePointTMinimumX, oldReferencePointTMinimumY, oldReferencePointTMinimumZ);
			
//			Retrieve the reference point from the new space:
			final float newReferencePointTMinimumX = point3FGetComponent1();
			final float newReferencePointTMinimumY = point3FGetComponent2();
			final float newReferencePointTMinimumZ = point3FGetComponent3();
			
//			Compute the distance from the origin in the new space to the reference point in the new space:
			final float distanceOriginToReferencePointTMinimum = point3FDistance(newOriginX, newOriginY, newOriginZ, newReferencePointTMinimumX, newReferencePointTMinimumY, newReferencePointTMinimumZ);
			
//			Update the new minimum ray boundary:
			newTMinimum = abs(distanceOriginToReferencePointTMinimum);
		}
		
//		Check if the new maximum ray bounday should be computed:
		if(newTMaximum > DEFAULT_T_MINIMUM && newTMaximum < DEFAULT_T_MAXIMUM) {
//			Compute a reference point in the old space:
			final float oldReferencePointTMaximumX = oldOriginX + oldDirectionX * oldTMaximum;
			final float oldReferencePointTMaximumY = oldOriginY + oldDirectionY * oldTMaximum;
			final float oldReferencePointTMaximumZ = oldOriginZ + oldDirectionZ * oldTMaximum;
			
//			Transform the reference point from the old space to the new space:
			point3FSetMatrix44FTransformAndDivide(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, oldReferencePointTMaximumX, oldReferencePointTMaximumY, oldReferencePointTMaximumZ);
			
//			Retrieve the reference point from the new space:
			final float newReferencePointTMaximumX = point3FGetComponent1();
			final float newReferencePointTMaximumY = point3FGetComponent2();
			final float newReferencePointTMaximumZ = point3FGetComponent3();
			
//			Compute the distance from the origin in the new space to the reference point in the new space:
			final float distanceOriginToReferencePointTMaximum = point3FDistance(newOriginX, newOriginY, newOriginZ, newReferencePointTMaximumX, newReferencePointTMaximumY, newReferencePointTMaximumZ);
			
//			Update the new maximum ray boundary:
			newTMaximum = abs(distanceOriginToReferencePointTMaximum);
		}
		
//		Set the new variables:
		ray3FSetOrigin(newOriginX, newOriginY, newOriginZ);
		ray3FSetDirection(newDirectionX, newDirectionY, newDirectionZ);
		ray3FSetTMinimum(newTMinimum);
		ray3FSetTMaximum(newTMaximum);
	}
	
	private void doSetupPixelArray() {
		put(this.pixelArray = Floats.array(getResolution() * 2, 0.0F));
	}
	
	private void doSetupScene() {
		final SceneCompiler sceneCompiler = new SceneCompiler();
		
		final CompiledScene compiledScene = sceneCompiler.compile(getScene());
		
		put(super.boundingVolume3FAxisAlignedBoundingBox3FArray = compiledScene.getBoundingVolume3FAxisAlignedBoundingBox3FArray());
		put(super.boundingVolume3FBoundingSphere3FArray = compiledScene.getBoundingVolume3FBoundingSphere3FArray());
		put(super.shape3FCone3FArray = compiledScene.getShape3FCone3FArray());
		put(super.shape3FCylinder3FArray = compiledScene.getShape3FCylinder3FArray());
		put(super.shape3FDisk3FArray = compiledScene.getShape3FDisk3FArray());
		put(super.shape3FParaboloid3FArray = compiledScene.getShape3FParaboloid3FArray());
		put(super.shape3FPlane3FArray = compiledScene.getShape3FPlane3FArray());
		put(super.shape3FRectangularCuboid3FArray = compiledScene.getShape3FRectangularCuboid3FArray());
		put(super.shape3FSphere3FArray = compiledScene.getShape3FSphere3FArray());
		put(super.shape3FTorus3FArray = compiledScene.getShape3FTorus3FArray());
		put(super.shape3FTriangle3FArray = compiledScene.getShape3FTriangle3FArray());
		put(super.shape3FTriangleMesh3FArray = compiledScene.getShape3FTriangleMesh3FArray());
		
		put(super.textureBlendTextureArray = compiledScene.getTextureBlendTextureArray());
		put(super.textureBullseyeTextureArray = compiledScene.getTextureBullseyeTextureArray());
		put(super.textureCheckerboardTextureArray = compiledScene.getTextureCheckerboardTextureArray());
		put(super.textureConstantTextureArray = compiledScene.getTextureConstantTextureArray());
		put(super.textureLDRImageTextureArray = compiledScene.getTextureLDRImageTextureArray());
		put(super.textureMarbleTextureArray = compiledScene.getTextureMarbleTextureArray());
		put(super.textureSimplexFractionalBrownianMotionTextureArray = compiledScene.getTextureSimplexFractionalBrownianMotionTextureArray());
		
		put(super.materialClearCoatMaterialArray = compiledScene.getMaterialClearCoatMaterialArray());
		put(super.materialGlassMaterialArray = compiledScene.getMaterialGlassMaterialArray());
		put(super.materialGlossyMaterialArray = compiledScene.getMaterialGlossyMaterialArray());
		put(super.materialMatteMaterialArray = compiledScene.getMaterialMatteMaterialArray());
		put(super.materialMetalMaterialArray = compiledScene.getMaterialMetalMaterialArray());
		put(super.materialMirrorMaterialArray = compiledScene.getMaterialMirrorMaterialArray());
		put(super.materialPlasticMaterialArray = compiledScene.getMaterialPlasticMaterialArray());
		put(super.materialSubstrateMaterialArray = compiledScene.getMaterialSubstrateMaterialArray());
		
		put(super.lightLDRImageLightArray = compiledScene.getLightLDRImageLightArray());
		put(super.lightLDRImageLightOffsetArray = compiledScene.getLightLDRImageLightOffsetArray());
		
		put(this.cameraArray = compiledScene.getCameraArray());
		put(this.matrix44FArray = compiledScene.getMatrix44FArray());
		put(this.primitiveArray = compiledScene.getPrimitiveArray());
		
		super.lightLDRImageLightCount = compiledScene.getLightLDRImageLightCount();
		
		this.primitiveCount = compiledScene.getPrimitiveCount();
	}
}