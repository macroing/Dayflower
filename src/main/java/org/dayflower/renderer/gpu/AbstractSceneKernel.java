/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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

import java.util.Objects;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.geometry.boundingvolume.BoundingSphere3F;
import org.dayflower.geometry.boundingvolume.InfiniteBoundingVolume3F;
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
import org.dayflower.scene.Camera;
import org.dayflower.scene.Light;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.compiler.CompiledCameraCache;
import org.dayflower.scene.compiler.CompiledPrimitiveCache;
import org.dayflower.scene.compiler.CompiledScene;
import org.dayflower.scene.compiler.SceneCompiler;

import org.macroing.java.util.Arrays;

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
	/**
	 * A {@code float[]} that contains a {@link Camera} instance.
	 */
	protected float[] cameraArray;
	
	/**
	 * A {@code float[]} that contains {@link Matrix44F} instances used by the {@link Primitive} instances.
	 */
	protected float[] primitiveMatrix44FArray;
	
	/**
	 * A {@code float[]} that contains X- and Y-components for the pixels.
	 */
	protected float[] pixelArray;
	
	/**
	 * The {@link Primitive} count.
	 */
	protected int primitiveCount;
	
	/**
	 * An {@code int[]} that contains {@link Primitive} instances.
	 */
	protected int[] primitiveArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Scene scene;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractSceneKernel} instance.
	 */
	protected AbstractSceneKernel() {
		this.cameraArray = new float[1];
		this.primitiveMatrix44FArray = new float[1];
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
		setup(true);
	}
	
	/**
	 * Sets up all necessary resources for this {@code AbstractSceneKernel} instance.
	 * 
	 * @param isSettingUpScene {@code true} if, and only if, the scene should be setup, {@code false} otherwise
	 */
	public void setup(final boolean isSettingUpScene) {
		super.setup();
		
		doSetupPixelArray();
		
		if(isSettingUpScene) {
			doSetupScene();
		}
	}
	
	/**
	 * Updates the {@link Camera} instance.
	 */
	public final void updateCamera() {
		put(this.cameraArray = CompiledCameraCache.toCamera(getScene().getCamera()));
	}
	
	/**
	 * Updates the {@link Matrix44F} instances.
	 */
	public final void updateMatrix44Fs() {
		final float[] oldPrimitiveMatrix44FArray = this.primitiveMatrix44FArray;
		final float[] newPrimitiveMatrix44FArray = doGetCompatibleArray(CompiledPrimitiveCache.toMatrix44Fs(getScene().getPrimitives()));
		
//		It is possible that the GPU-renderer will crash if the length of the old array differs from the length of the new array:
		if(oldPrimitiveMatrix44FArray.length == newPrimitiveMatrix44FArray.length) {
			put(this.primitiveMatrix44FArray = newPrimitiveMatrix44FArray);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Performs an intersection test against all primitives in the scene and computes intersection information for the closest.
	 * <p>
	 * Returns {@code true} if, and only if, an intersection was found, {@code false} otherwise.
	 * <p>
	 * If an intersection was found, the computed information will be present in {@link #intersectionLHSArray_$private$24} in world space.
	 * 
	 * @return {@code true} if, and only if, an intersection was found, {@code false} otherwise
	 */
	protected final boolean primitiveIntersectionComputeLHS() {
		int primitiveIndex = -1;
		
		this.shape3FTriangleMesh3FArrayToShape3FTriangle3FArray_$private$1[0] = -1;
		
		for(int index = 0; index < this.primitiveCount; index++) {
			final int primitiveArrayOffset = index * CompiledPrimitiveCache.PRIMITIVE_LENGTH;
			final int primitiveArrayOffsetBoundingVolumeID = primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_BOUNDING_VOLUME_ID;
			final int primitiveArrayOffsetBoundingVolumeOffset = primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_BOUNDING_VOLUME_OFFSET;
			final int primitiveArrayOffsetShapeID = primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_SHAPE_ID;
			final int primitiveArrayOffsetShapeOffset = primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_SHAPE_OFFSET;
			
			final int boundingVolumeID = this.primitiveArray[primitiveArrayOffsetBoundingVolumeID];
			final int boundingVolumeOffset = this.primitiveArray[primitiveArrayOffsetBoundingVolumeOffset];
			final int shapeID = this.primitiveArray[primitiveArrayOffsetShapeID];
			final int shapeOffset = this.primitiveArray[primitiveArrayOffsetShapeOffset];
			
			final float tMinimumWorldSpace = ray3FGetTMinimum();
			final float tMaximumWorldSpace = ray3FGetTMaximum();
			
			boolean isIntersectingBoundingVolume = false;
			
//			Find out what causes the order of the if-statements to fail. If InfiniteBoundingVolume3F.ID is checked in the last if-statement, the plane will disappear.
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
				} else if(shapeID == Hyperboloid3F.ID) {
					tObjectSpace = shape3FHyperboloid3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Paraboloid3F.ID) {
					tObjectSpace = shape3FParaboloid3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Plane3F.ID) {
					tObjectSpace = shape3FPlane3FIntersectionT(tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Polygon3F.ID) {
					tObjectSpace = shape3FPolygon3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Rectangle3F.ID) {
					tObjectSpace = shape3FRectangle3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == RectangularCuboid3F.ID) {
					tObjectSpace = shape3FRectangularCuboid3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Sphere3F.ID) {
					tObjectSpace = shape3FSphere3FIntersectionT(tMinimumObjectSpace, tMaximumObjectSpace);
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
			final int primitiveArrayOffset = primitiveIndex * CompiledPrimitiveCache.PRIMITIVE_LENGTH;
			final int primitiveArrayOffsetShapeID = primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_SHAPE_ID;
			final int primitiveArrayOffsetShapeOffset = primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_SHAPE_OFFSET;
			
			final int shapeID = this.primitiveArray[primitiveArrayOffsetShapeID];
			final int shapeOffset = this.primitiveArray[primitiveArrayOffsetShapeOffset];
			
			ray3FSetMatrix44FTransformWorldToObject(primitiveIndex);
			
			final float tObjectSpace = ray3FGetTMaximum();
			
			if(shapeID == Cone3F.ID) {
				shape3FCone3FIntersectionComputeLHS(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Cylinder3F.ID) {
				shape3FCylinder3FIntersectionComputeLHS(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Disk3F.ID) {
				shape3FDisk3FIntersectionComputeLHS(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Hyperboloid3F.ID) {
				shape3FHyperboloid3FIntersectionComputeLHS(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Paraboloid3F.ID) {
				shape3FParaboloid3FIntersectionComputeLHS(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Plane3F.ID) {
				shape3FPlane3FIntersectionComputeLHS(tObjectSpace, primitiveIndex);
			} else if(shapeID == Polygon3F.ID) {
				shape3FPolygon3FIntersectionComputeLHS(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Rectangle3F.ID) {
				shape3FRectangle3FIntersectionComputeLHS(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == RectangularCuboid3F.ID) {
				shape3FRectangularCuboid3FIntersectionComputeLHS(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Sphere3F.ID) {
				shape3FSphere3FIntersectionComputeLHS(tObjectSpace, primitiveIndex);
			} else if(shapeID == Torus3F.ID) {
				shape3FTorus3FIntersectionComputeLHS(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Triangle3F.ID) {
				shape3FTriangle3FIntersectionComputeLHS(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == TriangleMesh3F.ID) {
				shape3FTriangleMesh3FIntersectionComputeLHS(tObjectSpace, primitiveIndex);
			}
			
			ray3FSetMatrix44FTransformObjectToWorld(primitiveIndex);
			
			doIntersectionTransformObjectToWorldLHS(primitiveIndex);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Performs an intersection test against all primitives in the scene and computes intersection information for the closest.
	 * <p>
	 * Returns {@code true} if, and only if, an intersection was found, {@code false} otherwise.
	 * <p>
	 * If an intersection was found, the computed information will be present in {@link #intersectionRHSArray_$private$24} in world space.
	 * 
	 * @return {@code true} if, and only if, an intersection was found, {@code false} otherwise
	 */
	protected final boolean primitiveIntersectionComputeRHS() {
		int primitiveIndex = -1;
		
		this.shape3FTriangleMesh3FArrayToShape3FTriangle3FArray_$private$1[0] = -1;
		
		for(int index = 0; index < this.primitiveCount; index++) {
			final int primitiveArrayOffset = index * CompiledPrimitiveCache.PRIMITIVE_LENGTH;
			final int primitiveArrayOffsetBoundingVolumeID = primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_BOUNDING_VOLUME_ID;
			final int primitiveArrayOffsetBoundingVolumeOffset = primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_BOUNDING_VOLUME_OFFSET;
			final int primitiveArrayOffsetShapeID = primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_SHAPE_ID;
			final int primitiveArrayOffsetShapeOffset = primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_SHAPE_OFFSET;
			
			final int boundingVolumeID = this.primitiveArray[primitiveArrayOffsetBoundingVolumeID];
			final int boundingVolumeOffset = this.primitiveArray[primitiveArrayOffsetBoundingVolumeOffset];
			final int shapeID = this.primitiveArray[primitiveArrayOffsetShapeID];
			final int shapeOffset = this.primitiveArray[primitiveArrayOffsetShapeOffset];
			
			final float tMinimumWorldSpace = ray3FGetTMinimum();
			final float tMaximumWorldSpace = ray3FGetTMaximum();
			
			boolean isIntersectingBoundingVolume = false;
			
//			Find out what causes the order of the if-statements to fail. If InfiniteBoundingVolume3F.ID is checked in the last if-statement, the plane will disappear.
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
				} else if(shapeID == Hyperboloid3F.ID) {
					tObjectSpace = shape3FHyperboloid3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Paraboloid3F.ID) {
					tObjectSpace = shape3FParaboloid3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Plane3F.ID) {
					tObjectSpace = shape3FPlane3FIntersectionT(tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Polygon3F.ID) {
					tObjectSpace = shape3FPolygon3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Rectangle3F.ID) {
					tObjectSpace = shape3FRectangle3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == RectangularCuboid3F.ID) {
					tObjectSpace = shape3FRectangularCuboid3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Sphere3F.ID) {
					tObjectSpace = shape3FSphere3FIntersectionT(tMinimumObjectSpace, tMaximumObjectSpace);
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
			final int primitiveArrayOffset = primitiveIndex * CompiledPrimitiveCache.PRIMITIVE_LENGTH;
			final int primitiveArrayOffsetShapeID = primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_SHAPE_ID;
			final int primitiveArrayOffsetShapeOffset = primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_SHAPE_OFFSET;
			
			final int shapeID = this.primitiveArray[primitiveArrayOffsetShapeID];
			final int shapeOffset = this.primitiveArray[primitiveArrayOffsetShapeOffset];
			
			ray3FSetMatrix44FTransformWorldToObject(primitiveIndex);
			
			final float tObjectSpace = ray3FGetTMaximum();
			
			if(shapeID == Cone3F.ID) {
				shape3FCone3FIntersectionComputeRHS(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Cylinder3F.ID) {
				shape3FCylinder3FIntersectionComputeRHS(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Disk3F.ID) {
				shape3FDisk3FIntersectionComputeRHS(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Hyperboloid3F.ID) {
				shape3FHyperboloid3FIntersectionComputeRHS(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Paraboloid3F.ID) {
				shape3FParaboloid3FIntersectionComputeRHS(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Plane3F.ID) {
				shape3FPlane3FIntersectionComputeRHS(tObjectSpace, primitiveIndex);
			} else if(shapeID == Polygon3F.ID) {
				shape3FPolygon3FIntersectionComputeRHS(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Rectangle3F.ID) {
				shape3FRectangle3FIntersectionComputeRHS(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == RectangularCuboid3F.ID) {
				shape3FRectangularCuboid3FIntersectionComputeRHS(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Sphere3F.ID) {
				shape3FSphere3FIntersectionComputeRHS(tObjectSpace, primitiveIndex);
			} else if(shapeID == Torus3F.ID) {
				shape3FTorus3FIntersectionComputeRHS(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Triangle3F.ID) {
				shape3FTriangle3FIntersectionComputeRHS(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == TriangleMesh3F.ID) {
				shape3FTriangleMesh3FIntersectionComputeRHS(tObjectSpace, primitiveIndex);
			}
			
			ray3FSetMatrix44FTransformObjectToWorld(primitiveIndex);
			
			doIntersectionTransformObjectToWorldRHS(primitiveIndex);
			
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
		for(int index = 0; index < this.primitiveCount; index++) {
			final int primitiveArrayOffset = index * CompiledPrimitiveCache.PRIMITIVE_LENGTH;
			final int primitiveArrayOffsetBoundingVolumeID = primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_BOUNDING_VOLUME_ID;
			final int primitiveArrayOffsetBoundingVolumeOffset = primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_BOUNDING_VOLUME_OFFSET;
			final int primitiveArrayOffsetShapeID = primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_SHAPE_ID;
			final int primitiveArrayOffsetShapeOffset = primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_SHAPE_OFFSET;
			
			final int boundingVolumeID = this.primitiveArray[primitiveArrayOffsetBoundingVolumeID];
			final int boundingVolumeOffset = this.primitiveArray[primitiveArrayOffsetBoundingVolumeOffset];
			final int shapeID = this.primitiveArray[primitiveArrayOffsetShapeID];
			final int shapeOffset = this.primitiveArray[primitiveArrayOffsetShapeOffset];
			
			final float tMinimumWorldSpace = ray3FGetTMinimum();
			final float tMaximumWorldSpace = ray3FGetTMaximum();
			
			boolean isIntersectingBoundingVolume = false;
			
//			Find out what causes the order of the if-statements to fail. If InfiniteBoundingVolume3F.ID is checked in the last if-statement, the plane will disappear.
			if(boundingVolumeID == InfiniteBoundingVolume3F.ID) {
				isIntersectingBoundingVolume = true;
			} else if(boundingVolumeID == AxisAlignedBoundingBox3F.ID) {
				isIntersectingBoundingVolume = boundingVolume3FAxisAlignedBoundingBox3FContainsOrIntersects(boundingVolumeOffset, tMinimumWorldSpace, tMaximumWorldSpace);
			} else if(boundingVolumeID == BoundingSphere3F.ID) {
				isIntersectingBoundingVolume = boundingVolume3FBoundingSphere3FContainsOrIntersects(boundingVolumeOffset, tMinimumWorldSpace, tMaximumWorldSpace);
			}
			
			if(isIntersectingBoundingVolume) {
				ray3FSetMatrix44FTransformWorldToObject(index);
				
				boolean isIntersectingShape = false;
				
				final float tMinimumObjectSpace = ray3FGetTMinimum();
				final float tMaximumObjectSpace = ray3FGetTMaximum();
				
				if(shapeID == Cone3F.ID) {
					isIntersectingShape = shape3FCone3FIntersects(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Cylinder3F.ID) {
					isIntersectingShape = shape3FCylinder3FIntersects(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Disk3F.ID) {
					isIntersectingShape = shape3FDisk3FIntersects(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Hyperboloid3F.ID) {
					isIntersectingShape = shape3FHyperboloid3FIntersects(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Paraboloid3F.ID) {
					isIntersectingShape = shape3FParaboloid3FIntersects(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Plane3F.ID) {
					isIntersectingShape = shape3FPlane3FIntersects(tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Polygon3F.ID) {
					isIntersectingShape = shape3FPolygon3FIntersects(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Rectangle3F.ID) {
					isIntersectingShape = shape3FRectangle3FIntersects(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == RectangularCuboid3F.ID) {
					isIntersectingShape = shape3FRectangularCuboid3FIntersects(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Sphere3F.ID) {
					isIntersectingShape = shape3FSphere3FIntersects(tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Torus3F.ID) {
					isIntersectingShape = shape3FTorus3FIntersects(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Triangle3F.ID) {
					isIntersectingShape = shape3FTriangle3FIntersects(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == TriangleMesh3F.ID) {
					isIntersectingShape = shape3FTriangleMesh3FIntersects(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				}
				
				ray3FSetMatrix44FTransformObjectToWorld(index);
				
				if(isIntersectingShape) {
					return true;
				}
			}
		}
		
		return false;
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
		final float fieldOfViewX = this.cameraArray[CompiledCameraCache.CAMERA_OFFSET_FIELD_OF_VIEW_X];
		final float fieldOfViewY = this.cameraArray[CompiledCameraCache.CAMERA_OFFSET_FIELD_OF_VIEW_Y];
		final float lens = this.cameraArray[CompiledCameraCache.CAMERA_OFFSET_LENS];
		final float uX = this.cameraArray[CompiledCameraCache.CAMERA_OFFSET_ORTHONORMAL_BASIS_U + 0];
		final float uY = this.cameraArray[CompiledCameraCache.CAMERA_OFFSET_ORTHONORMAL_BASIS_U + 1];
		final float uZ = this.cameraArray[CompiledCameraCache.CAMERA_OFFSET_ORTHONORMAL_BASIS_U + 2];
		final float vX = this.cameraArray[CompiledCameraCache.CAMERA_OFFSET_ORTHONORMAL_BASIS_V + 0];
		final float vY = this.cameraArray[CompiledCameraCache.CAMERA_OFFSET_ORTHONORMAL_BASIS_V + 1];
		final float vZ = this.cameraArray[CompiledCameraCache.CAMERA_OFFSET_ORTHONORMAL_BASIS_V + 2];
		final float wX = this.cameraArray[CompiledCameraCache.CAMERA_OFFSET_ORTHONORMAL_BASIS_W + 0];
		final float wY = this.cameraArray[CompiledCameraCache.CAMERA_OFFSET_ORTHONORMAL_BASIS_W + 1];
		final float wZ = this.cameraArray[CompiledCameraCache.CAMERA_OFFSET_ORTHONORMAL_BASIS_W + 2];
		final float eyeX = this.cameraArray[CompiledCameraCache.CAMERA_OFFSET_EYE + 0];
		final float eyeY = this.cameraArray[CompiledCameraCache.CAMERA_OFFSET_EYE + 1];
		final float eyeZ = this.cameraArray[CompiledCameraCache.CAMERA_OFFSET_EYE + 2];
		final float apertureRadius = this.cameraArray[CompiledCameraCache.CAMERA_OFFSET_APERTURE_RADIUS];
		final float focalDistance = this.cameraArray[CompiledCameraCache.CAMERA_OFFSET_FOCAL_DISTANCE];
		final float resolutionX = this.cameraArray[CompiledCameraCache.CAMERA_OFFSET_RESOLUTION_X];
		final float resolutionY = this.cameraArray[CompiledCameraCache.CAMERA_OFFSET_RESOLUTION_Y];
		
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
	 * Generates a ray for the current pixel using the current camera and a triangle filter.
	 * <p>
	 * Returns {@code true} if, and only if, a ray was generated, {@code false} otherwise.
	 * <p>
	 * If the current camera uses a thin lens, this method should always return {@code true}. If, on the other hand, the current camera uses a fisheye lens, this may not always be the case.
	 * <p>
	 * If this method returns {@code true}, the pixel coordinates will be set in {@link #pixelArray}.
	 * 
	 * @return {@code true} if, and only if, a ray was generated, {@code false} otherwise
	 */
	protected final boolean ray3FCameraGenerateTriangleFilter() {
		final float pixelX = 2.0F * random();
		final float pixelY = 2.0F * random();
		
		final float filteredPixelX = pixelX < 1.0F ? sqrt(pixelX) - 1.0F : 1.0F - sqrt(2.0F - pixelX);
		final float filteredPixelY = pixelY < 1.0F ? sqrt(pixelY) - 1.0F : 1.0F - sqrt(2.0F - pixelY);
		
		return ray3FCameraGenerate(filteredPixelX, filteredPixelY);
	}
	
	/**
	 * Returns the parametric T value for the closest primitive in world space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @return the parametric T value for the closest primitive in world space, or {@code 0.0F} if no intersection was found
	 */
	protected final float primitiveIntersectionT() {
		boolean hasFoundIntersection = false;
		
		for(int index = 0; index < this.primitiveCount; index++) {
			final int primitiveArrayOffset = index * CompiledPrimitiveCache.PRIMITIVE_LENGTH;
			final int primitiveArrayOffsetBoundingVolumeID = primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_BOUNDING_VOLUME_ID;
			final int primitiveArrayOffsetBoundingVolumeOffset = primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_BOUNDING_VOLUME_OFFSET;
			final int primitiveArrayOffsetShapeID = primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_SHAPE_ID;
			final int primitiveArrayOffsetShapeOffset = primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_SHAPE_OFFSET;
			
			final int boundingVolumeID = this.primitiveArray[primitiveArrayOffsetBoundingVolumeID];
			final int boundingVolumeOffset = this.primitiveArray[primitiveArrayOffsetBoundingVolumeOffset];
			final int shapeID = this.primitiveArray[primitiveArrayOffsetShapeID];
			final int shapeOffset = this.primitiveArray[primitiveArrayOffsetShapeOffset];
			
			final float tMinimumWorldSpace = ray3FGetTMinimum();
			final float tMaximumWorldSpace = ray3FGetTMaximum();
			
			boolean isIntersectingBoundingVolume = false;
			
//			Find out what causes the order of the if-statements to fail. If InfiniteBoundingVolume3F.ID is checked in the last if-statement, the plane will disappear.
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
				} else if(shapeID == Hyperboloid3F.ID) {
					tObjectSpace = shape3FHyperboloid3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Paraboloid3F.ID) {
					tObjectSpace = shape3FParaboloid3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Plane3F.ID) {
					tObjectSpace = shape3FPlane3FIntersectionT(tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Polygon3F.ID) {
					tObjectSpace = shape3FPolygon3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Rectangle3F.ID) {
					tObjectSpace = shape3FRectangle3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == RectangularCuboid3F.ID) {
					tObjectSpace = shape3FRectangularCuboid3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Sphere3F.ID) {
					tObjectSpace = shape3FSphere3FIntersectionT(tMinimumObjectSpace, tMaximumObjectSpace);
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
	
	/**
	 * Returns the ID of the {@link AreaLight} instance that is used by the intersected {@link Primitive} instance.
	 * 
	 * @return the ID of the {@code AreaLight} instance that is used by the intersected {@code Primitive} instance
	 */
	protected final int primitiveGetAreaLightIDLHS() {
		return (this.primitiveArray[intersectionLHSGetPrimitiveIndex() * CompiledPrimitiveCache.PRIMITIVE_LENGTH + CompiledPrimitiveCache.PRIMITIVE_OFFSET_AREA_LIGHT_ID_AND_OFFSET] >>> 16) & 0xFFFF;
	}
	
	/**
	 * Returns the ID of the {@link AreaLight} instance that is used by the intersected {@link Primitive} instance.
	 * 
	 * @return the ID of the {@code AreaLight} instance that is used by the intersected {@code Primitive} instance
	 */
	protected final int primitiveGetAreaLightIDRHS() {
		return (this.primitiveArray[intersectionRHSGetPrimitiveIndex() * CompiledPrimitiveCache.PRIMITIVE_LENGTH + CompiledPrimitiveCache.PRIMITIVE_OFFSET_AREA_LIGHT_ID_AND_OFFSET] >>> 16) & 0xFFFF;
	}
	
	/**
	 * Returns the offset of the {@link AreaLight} instance that is used by the intersected {@link Primitive} instance.
	 * 
	 * @return the offset of the {@code AreaLight} instance that is used by the intersected {@code Primitive} instance
	 */
	protected final int primitiveGetAreaLightOffsetLHS() {
		return this.primitiveArray[intersectionLHSGetPrimitiveIndex() * CompiledPrimitiveCache.PRIMITIVE_LENGTH + CompiledPrimitiveCache.PRIMITIVE_OFFSET_AREA_LIGHT_ID_AND_OFFSET] & 0xFFFF;
	}
	
	/**
	 * Returns the offset of the {@link AreaLight} instance that is used by the intersected {@link Primitive} instance.
	 * 
	 * @return the offset of the {@code AreaLight} instance that is used by the intersected {@code Primitive} instance
	 */
	protected final int primitiveGetAreaLightOffsetRHS() {
		return this.primitiveArray[intersectionRHSGetPrimitiveIndex() * CompiledPrimitiveCache.PRIMITIVE_LENGTH + CompiledPrimitiveCache.PRIMITIVE_OFFSET_AREA_LIGHT_ID_AND_OFFSET] & 0xFFFF;
	}
	
	/**
	 * Returns the ID of the {@link Material} instance that is used by the intersected {@link Primitive} instance.
	 * 
	 * @return the ID of the {@code Material} instance that is used by the intersected {@code Primitive} instance
	 */
	protected final int primitiveGetMaterialIDLHS() {
		return this.primitiveArray[intersectionLHSGetPrimitiveIndex() * CompiledPrimitiveCache.PRIMITIVE_LENGTH + CompiledPrimitiveCache.PRIMITIVE_OFFSET_MATERIAL_ID];
	}
	
	/**
	 * Returns the ID of the {@link Material} instance that is used by the intersected {@link Primitive} instance.
	 * 
	 * @return the ID of the {@code Material} instance that is used by the intersected {@code Primitive} instance
	 */
	protected final int primitiveGetMaterialIDRHS() {
		return this.primitiveArray[intersectionRHSGetPrimitiveIndex() * CompiledPrimitiveCache.PRIMITIVE_LENGTH + CompiledPrimitiveCache.PRIMITIVE_OFFSET_MATERIAL_ID];
	}
	
	/**
	 * Returns the offset for the {@link Material} instance that is used by the intersected {@link Primitive} instance.
	 * 
	 * @return the offset for the {@code Material} instance that is used by the intersected {@code Primitive} instance
	 */
	protected final int primitiveGetMaterialOffsetLHS() {
		return this.primitiveArray[intersectionLHSGetPrimitiveIndex() * CompiledPrimitiveCache.PRIMITIVE_LENGTH + CompiledPrimitiveCache.PRIMITIVE_OFFSET_MATERIAL_OFFSET];
	}
	
	/**
	 * Returns the offset for the {@link Material} instance that is used by the intersected {@link Primitive} instance.
	 * 
	 * @return the offset for the {@code Material} instance that is used by the intersected {@code Primitive} instance
	 */
	protected final int primitiveGetMaterialOffsetRHS() {
		return this.primitiveArray[intersectionRHSGetPrimitiveIndex() * CompiledPrimitiveCache.PRIMITIVE_LENGTH + CompiledPrimitiveCache.PRIMITIVE_OFFSET_MATERIAL_OFFSET];
	}
	
	/**
	 * Samples one {@link Light} instance using a uniform distribution.
	 * <p>
	 * The result will be set using {@link #color3FLHSSet(float, float, float)}.
	 * <p>
	 * To retrieve the color components of the result, the methods {@link #color3FLHSGetComponent1()}, {@link #color3FLHSGetComponent2()} or {@link #color3FLHSGetComponent3()} may be used.
	 */
	protected final void lightSampleOneLightUniformDistribution() {
		final int lightCount = super.lightCount;
		
		if(lightCount == 0) {
			color3FLHSSet(0.0F, 0.0F, 0.0F);
			
			return;
		}
		
		final float probabilityDensityFunctionValue = 1.0F / lightCount;
		
		final int index = min((int)(random() * lightCount), lightCount - 1);
		
		final int light = super.lightIDAndOffsetArray[index];
		final int lightID = (light >>> 16) & 0xFFFF;
		final int lightOffset = lightToOffset(lightID, light & 0xFFFF);
		
		lightSet(lightID, lightOffset);
		
		doLightEstimateDirectLight(random(), random(), random(), random(), false);
		
		final float lightR = finiteOrZero(color3FLHSGetComponent1() / probabilityDensityFunctionValue);
		final float lightG = finiteOrZero(color3FLHSGetComponent2() / probabilityDensityFunctionValue);
		final float lightB = finiteOrZero(color3FLHSGetComponent3() / probabilityDensityFunctionValue);
		
		color3FLHSSet(lightR, lightG, lightB);
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
	
	private void doIntersectionTransformLHS(final int primitiveMatrix44FArrayOffsetMatrix, final int primitiveMatrix44FArrayOffsetMatrixInverse) {
//		Retrieve the matrix elements:
		final float matrixElement11 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_1_1];
		final float matrixElement12 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_1_2];
		final float matrixElement13 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_1_3];
		final float matrixElement14 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_1_4];
		final float matrixElement21 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_2_1];
		final float matrixElement22 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_2_2];
		final float matrixElement23 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_2_3];
		final float matrixElement24 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_2_4];
		final float matrixElement31 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_3_1];
		final float matrixElement32 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_3_2];
		final float matrixElement33 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_3_3];
		final float matrixElement34 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_3_4];
		final float matrixElement41 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_4_1];
		final float matrixElement42 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_4_2];
		final float matrixElement43 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_4_3];
		final float matrixElement44 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_4_4];
		
//		Retrieve the matrix inverse elements:
		final float matrixInverseElement11 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_1_1];
		final float matrixInverseElement12 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_1_2];
		final float matrixInverseElement13 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_1_3];
		final float matrixInverseElement21 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_2_1];
		final float matrixInverseElement22 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_2_2];
		final float matrixInverseElement23 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_2_3];
		final float matrixInverseElement31 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_3_1];
		final float matrixInverseElement32 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_3_2];
		final float matrixInverseElement33 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_3_3];
		
//		Retrieve the old variables from the intersection array:
		final float oldOrthonormalBasisGUX = intersectionLHSGetOrthonormalBasisGUX();
		final float oldOrthonormalBasisGUY = intersectionLHSGetOrthonormalBasisGUY();
		final float oldOrthonormalBasisGUZ = intersectionLHSGetOrthonormalBasisGUZ();
		final float oldOrthonormalBasisGVX = intersectionLHSGetOrthonormalBasisGVX();
		final float oldOrthonormalBasisGVY = intersectionLHSGetOrthonormalBasisGVY();
		final float oldOrthonormalBasisGVZ = intersectionLHSGetOrthonormalBasisGVZ();
		final float oldOrthonormalBasisGWX = intersectionLHSGetOrthonormalBasisGWX();
		final float oldOrthonormalBasisGWY = intersectionLHSGetOrthonormalBasisGWY();
		final float oldOrthonormalBasisGWZ = intersectionLHSGetOrthonormalBasisGWZ();
		final float oldOrthonormalBasisSUX = intersectionLHSGetOrthonormalBasisSUX();
		final float oldOrthonormalBasisSUY = intersectionLHSGetOrthonormalBasisSUY();
		final float oldOrthonormalBasisSUZ = intersectionLHSGetOrthonormalBasisSUZ();
		final float oldOrthonormalBasisSVX = intersectionLHSGetOrthonormalBasisSVX();
		final float oldOrthonormalBasisSVY = intersectionLHSGetOrthonormalBasisSVY();
		final float oldOrthonormalBasisSVZ = intersectionLHSGetOrthonormalBasisSVZ();
		final float oldOrthonormalBasisSWX = intersectionLHSGetOrthonormalBasisSWX();
		final float oldOrthonormalBasisSWY = intersectionLHSGetOrthonormalBasisSWY();
		final float oldOrthonormalBasisSWZ = intersectionLHSGetOrthonormalBasisSWZ();
		final float oldSurfaceIntersectionPointX = intersectionLHSGetSurfaceIntersectionPointX();
		final float oldSurfaceIntersectionPointY = intersectionLHSGetSurfaceIntersectionPointY();
		final float oldSurfaceIntersectionPointZ = intersectionLHSGetSurfaceIntersectionPointZ();
		
//		Transform the U-direction of the geometric orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisGUX, oldOrthonormalBasisGUY, oldOrthonormalBasisGUZ);
		
//		Retrieve the transformed U-direction of the geometric orthonormal basis:
		final float newOrthonormalBasisGUX = vector3FGetX();
		final float newOrthonormalBasisGUY = vector3FGetY();
		final float newOrthonormalBasisGUZ = vector3FGetZ();
		
//		Transform the V-direction of the geometric orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisGVX, oldOrthonormalBasisGVY, oldOrthonormalBasisGVZ);
		
//		Retrieve the transformed V-direction of the geometric orthonormal basis:
		final float newOrthonormalBasisGVX = vector3FGetX();
		final float newOrthonormalBasisGVY = vector3FGetY();
		final float newOrthonormalBasisGVZ = vector3FGetZ();
		
//		Transform the W-direction of the geometric orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisGWX, oldOrthonormalBasisGWY, oldOrthonormalBasisGWZ);
		
//		Retrieve the transformed W-direction of the geometric orthonormal basis:
		final float newOrthonormalBasisGWX = vector3FGetX();
		final float newOrthonormalBasisGWY = vector3FGetY();
		final float newOrthonormalBasisGWZ = vector3FGetZ();
		
//		Transform the U-direction of the shading orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisSUX, oldOrthonormalBasisSUY, oldOrthonormalBasisSUZ);
		
//		Retrieve the transformed U-direction of the shading orthonormal basis:
		final float newOrthonormalBasisSUX = vector3FGetX();
		final float newOrthonormalBasisSUY = vector3FGetY();
		final float newOrthonormalBasisSUZ = vector3FGetZ();
		
//		Transform the V-direction of the shading orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisSVX, oldOrthonormalBasisSVY, oldOrthonormalBasisSVZ);
		
//		Retrieve the transformed V-direction of the shading orthonormal basis:
		final float newOrthonormalBasisSVX = vector3FGetX();
		final float newOrthonormalBasisSVY = vector3FGetY();
		final float newOrthonormalBasisSVZ = vector3FGetZ();
		
//		Transform the W-direction of the shading orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisSWX, oldOrthonormalBasisSWY, oldOrthonormalBasisSWZ);
		
//		Retrieve the transformed W-direction of the shading orthonormal basis:
		final float newOrthonormalBasisSWX = vector3FGetX();
		final float newOrthonormalBasisSWY = vector3FGetY();
		final float newOrthonormalBasisSWZ = vector3FGetZ();
		
//		Transform the surface intersection point:
		point3FSetMatrix44FTransformAndDivide(matrixElement11, matrixElement12, matrixElement13, matrixElement14, matrixElement21, matrixElement22, matrixElement23, matrixElement24, matrixElement31, matrixElement32, matrixElement33, matrixElement34, matrixElement41, matrixElement42, matrixElement43, matrixElement44, oldSurfaceIntersectionPointX, oldSurfaceIntersectionPointY, oldSurfaceIntersectionPointZ);
		
//		Retrieve the transformed surface intersection point:
		final float newSurfaceIntersectionPointX = point3FGetX();
		final float newSurfaceIntersectionPointY = point3FGetY();
		final float newSurfaceIntersectionPointZ = point3FGetZ();
		
//		Update the intersection array:
		intersectionLHSSetOrthonormalBasisG(newOrthonormalBasisGUX, newOrthonormalBasisGUY, newOrthonormalBasisGUZ, newOrthonormalBasisGVX, newOrthonormalBasisGVY, newOrthonormalBasisGVZ, newOrthonormalBasisGWX, newOrthonormalBasisGWY, newOrthonormalBasisGWZ);
		intersectionLHSSetOrthonormalBasisS(newOrthonormalBasisSUX, newOrthonormalBasisSUY, newOrthonormalBasisSUZ, newOrthonormalBasisSVX, newOrthonormalBasisSVY, newOrthonormalBasisSVZ, newOrthonormalBasisSWX, newOrthonormalBasisSWY, newOrthonormalBasisSWZ);
		intersectionLHSSetSurfaceIntersectionPoint(newSurfaceIntersectionPointX, newSurfaceIntersectionPointY, newSurfaceIntersectionPointZ);
	}
	
	private void doIntersectionTransformRHS(final int primitiveMatrix44FArrayOffsetMatrix, final int primitiveMatrix44FArrayOffsetMatrixInverse) {
//		Retrieve the matrix elements:
		final float matrixElement11 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_1_1];
		final float matrixElement12 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_1_2];
		final float matrixElement13 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_1_3];
		final float matrixElement14 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_1_4];
		final float matrixElement21 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_2_1];
		final float matrixElement22 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_2_2];
		final float matrixElement23 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_2_3];
		final float matrixElement24 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_2_4];
		final float matrixElement31 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_3_1];
		final float matrixElement32 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_3_2];
		final float matrixElement33 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_3_3];
		final float matrixElement34 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_3_4];
		final float matrixElement41 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_4_1];
		final float matrixElement42 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_4_2];
		final float matrixElement43 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_4_3];
		final float matrixElement44 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_4_4];
		
//		Retrieve the matrix inverse elements:
		final float matrixInverseElement11 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_1_1];
		final float matrixInverseElement12 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_1_2];
		final float matrixInverseElement13 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_1_3];
		final float matrixInverseElement21 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_2_1];
		final float matrixInverseElement22 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_2_2];
		final float matrixInverseElement23 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_2_3];
		final float matrixInverseElement31 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_3_1];
		final float matrixInverseElement32 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_3_2];
		final float matrixInverseElement33 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_3_3];
		
//		Retrieve the old variables from the intersection array:
		final float oldOrthonormalBasisGUX = intersectionRHSGetOrthonormalBasisGUX();
		final float oldOrthonormalBasisGUY = intersectionRHSGetOrthonormalBasisGUY();
		final float oldOrthonormalBasisGUZ = intersectionRHSGetOrthonormalBasisGUZ();
		final float oldOrthonormalBasisGVX = intersectionRHSGetOrthonormalBasisGVX();
		final float oldOrthonormalBasisGVY = intersectionRHSGetOrthonormalBasisGVY();
		final float oldOrthonormalBasisGVZ = intersectionRHSGetOrthonormalBasisGVZ();
		final float oldOrthonormalBasisGWX = intersectionRHSGetOrthonormalBasisGWX();
		final float oldOrthonormalBasisGWY = intersectionRHSGetOrthonormalBasisGWY();
		final float oldOrthonormalBasisGWZ = intersectionRHSGetOrthonormalBasisGWZ();
		final float oldOrthonormalBasisSUX = intersectionRHSGetOrthonormalBasisSUX();
		final float oldOrthonormalBasisSUY = intersectionRHSGetOrthonormalBasisSUY();
		final float oldOrthonormalBasisSUZ = intersectionRHSGetOrthonormalBasisSUZ();
		final float oldOrthonormalBasisSVX = intersectionRHSGetOrthonormalBasisSVX();
		final float oldOrthonormalBasisSVY = intersectionRHSGetOrthonormalBasisSVY();
		final float oldOrthonormalBasisSVZ = intersectionRHSGetOrthonormalBasisSVZ();
		final float oldOrthonormalBasisSWX = intersectionRHSGetOrthonormalBasisSWX();
		final float oldOrthonormalBasisSWY = intersectionRHSGetOrthonormalBasisSWY();
		final float oldOrthonormalBasisSWZ = intersectionRHSGetOrthonormalBasisSWZ();
		final float oldSurfaceIntersectionPointX = intersectionRHSGetSurfaceIntersectionPointX();
		final float oldSurfaceIntersectionPointY = intersectionRHSGetSurfaceIntersectionPointY();
		final float oldSurfaceIntersectionPointZ = intersectionRHSGetSurfaceIntersectionPointZ();
		
//		Transform the U-direction of the geometric orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisGUX, oldOrthonormalBasisGUY, oldOrthonormalBasisGUZ);
		
//		Retrieve the transformed U-direction of the geometric orthonormal basis:
		final float newOrthonormalBasisGUX = vector3FGetX();
		final float newOrthonormalBasisGUY = vector3FGetY();
		final float newOrthonormalBasisGUZ = vector3FGetZ();
		
//		Transform the V-direction of the geometric orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisGVX, oldOrthonormalBasisGVY, oldOrthonormalBasisGVZ);
		
//		Retrieve the transformed V-direction of the geometric orthonormal basis:
		final float newOrthonormalBasisGVX = vector3FGetX();
		final float newOrthonormalBasisGVY = vector3FGetY();
		final float newOrthonormalBasisGVZ = vector3FGetZ();
		
//		Transform the W-direction of the geometric orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisGWX, oldOrthonormalBasisGWY, oldOrthonormalBasisGWZ);
		
//		Retrieve the transformed W-direction of the geometric orthonormal basis:
		final float newOrthonormalBasisGWX = vector3FGetX();
		final float newOrthonormalBasisGWY = vector3FGetY();
		final float newOrthonormalBasisGWZ = vector3FGetZ();
		
//		Transform the U-direction of the shading orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisSUX, oldOrthonormalBasisSUY, oldOrthonormalBasisSUZ);
		
//		Retrieve the transformed U-direction of the shading orthonormal basis:
		final float newOrthonormalBasisSUX = vector3FGetX();
		final float newOrthonormalBasisSUY = vector3FGetY();
		final float newOrthonormalBasisSUZ = vector3FGetZ();
		
//		Transform the V-direction of the shading orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisSVX, oldOrthonormalBasisSVY, oldOrthonormalBasisSVZ);
		
//		Retrieve the transformed V-direction of the shading orthonormal basis:
		final float newOrthonormalBasisSVX = vector3FGetX();
		final float newOrthonormalBasisSVY = vector3FGetY();
		final float newOrthonormalBasisSVZ = vector3FGetZ();
		
//		Transform the W-direction of the shading orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisSWX, oldOrthonormalBasisSWY, oldOrthonormalBasisSWZ);
		
//		Retrieve the transformed W-direction of the shading orthonormal basis:
		final float newOrthonormalBasisSWX = vector3FGetX();
		final float newOrthonormalBasisSWY = vector3FGetY();
		final float newOrthonormalBasisSWZ = vector3FGetZ();
		
//		Transform the surface intersection point:
		point3FSetMatrix44FTransformAndDivide(matrixElement11, matrixElement12, matrixElement13, matrixElement14, matrixElement21, matrixElement22, matrixElement23, matrixElement24, matrixElement31, matrixElement32, matrixElement33, matrixElement34, matrixElement41, matrixElement42, matrixElement43, matrixElement44, oldSurfaceIntersectionPointX, oldSurfaceIntersectionPointY, oldSurfaceIntersectionPointZ);
		
//		Retrieve the transformed surface intersection point:
		final float newSurfaceIntersectionPointX = point3FGetX();
		final float newSurfaceIntersectionPointY = point3FGetY();
		final float newSurfaceIntersectionPointZ = point3FGetZ();
		
//		Update the intersection array:
		intersectionRHSSetOrthonormalBasisG(newOrthonormalBasisGUX, newOrthonormalBasisGUY, newOrthonormalBasisGUZ, newOrthonormalBasisGVX, newOrthonormalBasisGVY, newOrthonormalBasisGVZ, newOrthonormalBasisGWX, newOrthonormalBasisGWY, newOrthonormalBasisGWZ);
		intersectionRHSSetOrthonormalBasisS(newOrthonormalBasisSUX, newOrthonormalBasisSUY, newOrthonormalBasisSUZ, newOrthonormalBasisSVX, newOrthonormalBasisSVY, newOrthonormalBasisSVZ, newOrthonormalBasisSWX, newOrthonormalBasisSWY, newOrthonormalBasisSWZ);
		intersectionRHSSetSurfaceIntersectionPoint(newSurfaceIntersectionPointX, newSurfaceIntersectionPointY, newSurfaceIntersectionPointZ);
	}
	
	private void doIntersectionTransformObjectToWorldLHS(final int primitiveIndex) {
		doIntersectionTransformLHS(primitiveIndex * Matrix44F.ARRAY_SIZE * 2, primitiveIndex * Matrix44F.ARRAY_SIZE * 2 + Matrix44F.ARRAY_SIZE);
	}
	
	private void doIntersectionTransformObjectToWorldRHS(final int primitiveIndex) {
		doIntersectionTransformRHS(primitiveIndex * Matrix44F.ARRAY_SIZE * 2, primitiveIndex * Matrix44F.ARRAY_SIZE * 2 + Matrix44F.ARRAY_SIZE);
	}
	
	@SuppressWarnings("unused")
	private void doIntersectionTransformWorldToObjectLHS(final int primitiveIndex) {
		doIntersectionTransformLHS(primitiveIndex * Matrix44F.ARRAY_SIZE * 2 + Matrix44F.ARRAY_SIZE, primitiveIndex * Matrix44F.ARRAY_SIZE * 2);
	}
	
	@SuppressWarnings("unused")
	private void doIntersectionTransformWorldToObjectRHS(final int primitiveIndex) {
		doIntersectionTransformRHS(primitiveIndex * Matrix44F.ARRAY_SIZE * 2 + Matrix44F.ARRAY_SIZE, primitiveIndex * Matrix44F.ARRAY_SIZE * 2);
	}
	
	private void doLightEstimateDirectLight(final float sampleAU, final float sampleAV, final float sampleBU, final float sampleBV, final boolean isSpecular) {
//		Retrieve the current ray origin:
		final float rayOriginX = ray3FGetOriginX();
		final float rayOriginY = ray3FGetOriginY();
		final float rayOriginZ = ray3FGetOriginZ();
		
//		Retrieve the current ray direction:
		final float rayDirectionX = ray3FGetDirectionX();
		final float rayDirectionY = ray3FGetDirectionY();
		final float rayDirectionZ = ray3FGetDirectionZ();
		
//		Retrieve the parametric t values from the current ray:
		final float rayTMaximum = ray3FGetTMaximum();
		final float rayTMinimum = ray3FGetTMinimum();
		
//		Initialize the BXDF bit flags to use:
		final int bitFlags = isSpecular ? B_X_D_F_TYPE_BIT_FLAG_ALL : B_X_D_F_TYPE_BIT_FLAG_ALL_EXCEPT_SPECULAR;
		
//		Retrieve the ID and offset for the current light source:
		final int lightID = lightGetID();
		final int lightOffset = lightGetOffset();
		
//		Initialize a flag that indicates whether the current light source is an area light or not:
		final boolean isAreaLight = lightIsAreaLight();
		
//		Initialize a flag that indicates whether the current light source is using a delta distribution or not:
		final boolean isUsingDeltaDistribution = lightIsUsingDeltaDistribution();
		
//		Attempt to compute a light sample for incoming radiance along the current ray direction:
		final boolean isSampling = lightSampleRadianceIncoming(sampleAU, sampleAV, rayDirectionX, rayDirectionY, rayDirectionZ);
		
//		Retrieve the surface normal from the current surface intersection:
		final float normalX = intersectionLHSGetOrthonormalBasisSWX();
		final float normalY = intersectionLHSGetOrthonormalBasisSWY();
		final float normalZ = intersectionLHSGetOrthonormalBasisSWZ();
		
//		Compute the dot product between the surface normal and the current ray direction:
		final float normalDotRayDirection = vector3FDotProduct(normalX, normalY, normalZ, rayDirectionX, rayDirectionY, rayDirectionZ);
		
//		Compute the correctly oriented surface normal:
		final float normalCorrectlyOrientedX = normalDotRayDirection > 0.0F ? -normalX : normalX;
		final float normalCorrectlyOrientedY = normalDotRayDirection > 0.0F ? -normalY : normalY;
		final float normalCorrectlyOrientedZ = normalDotRayDirection > 0.0F ? -normalZ : normalZ;
		
//		Retrieve the surface intersection point from the current surface intersection:
		final float surfaceIntersectionPointX = intersectionLHSGetSurfaceIntersectionPointX();
		final float surfaceIntersectionPointY = intersectionLHSGetSurfaceIntersectionPointY();
		final float surfaceIntersectionPointZ = intersectionLHSGetSurfaceIntersectionPointZ();
		
//		Initialize the direct light to black:
		float lightDirectR = 0.0F;
		float lightDirectG = 0.0F;
		float lightDirectB = 0.0F;
		
//		Perform the following if the current light source is using a delta distribution and a sample for incoming radiance was created:
		if(isUsingDeltaDistribution && isSampling) {
			final float lightResultR = lightSampleGetResultR();
			final float lightResultG = lightSampleGetResultG();
			final float lightResultB = lightSampleGetResultB();
			
			final float lightIncomingX = lightSampleGetIncomingX();
			final float lightIncomingY = lightSampleGetIncomingY();
			final float lightIncomingZ = lightSampleGetIncomingZ();
			
			final float lightPointX = lightSampleGetPointX();
			final float lightPointY = lightSampleGetPointY();
			final float lightPointZ = lightSampleGetPointZ();
			
			final float lightProbabilityDensityFunctionValue = lightSampleGetProbabilityDensityFunctionValue();
			
			final boolean hasLightResult = !checkIsZero(lightResultR) || !checkIsZero(lightResultG) || !checkIsZero(lightResultB);
			
			if(hasLightResult && lightProbabilityDensityFunctionValue > 0.0F) {
				materialBSDFEvaluateDistributionFunction(bitFlags, lightIncomingX, lightIncomingY, lightIncomingZ, rayDirectionX, rayDirectionY, rayDirectionZ);
				
				final float materialBSDFResultR = materialBSDFResultGetResultR();
				final float materialBSDFResultG = materialBSDFResultGetResultG();
				final float materialBSDFResultB = materialBSDFResultGetResultB();
				
				final float lightIncomingDotNormalAbs = abs(vector3FDotProduct(lightIncomingX, lightIncomingY, lightIncomingZ, normalCorrectlyOrientedX, normalCorrectlyOrientedY, normalCorrectlyOrientedZ));
				
				final float scatteringResultR = materialBSDFResultR * lightIncomingDotNormalAbs;
				final float scatteringResultG = materialBSDFResultG * lightIncomingDotNormalAbs;
				final float scatteringResultB = materialBSDFResultB * lightIncomingDotNormalAbs;
				
				final boolean hasScatteringResult = !checkIsZero(scatteringResultR) || !checkIsZero(scatteringResultG) || !checkIsZero(scatteringResultB);
				
				final float directionX = lightPointX - surfaceIntersectionPointX;
				final float directionY = lightPointY - surfaceIntersectionPointY;
				final float directionZ = lightPointZ - surfaceIntersectionPointZ;
				final float directionLength = vector3FLength(directionX, directionY, directionZ);
				final float directionLengthReciprocal = 1.0F / directionLength;
				final float directionNormalizedX = directionX * directionLengthReciprocal;
				final float directionNormalizedY = directionY * directionLengthReciprocal;
				final float directionNormalizedZ = directionZ * directionLengthReciprocal;
				
				final float tMaximum = abs(directionLength) + 0.001F;
				final float tMinimum = DEFAULT_T_MINIMUM;
				
				ray3FSetOrigin(surfaceIntersectionPointX + directionNormalizedX * 0.001F, surfaceIntersectionPointY + directionNormalizedY * 0.001F, surfaceIntersectionPointZ + directionNormalizedZ * 0.001F);
				ray3FSetDirection(directionNormalizedX, directionNormalizedY, directionNormalizedZ);
				ray3FSetTMaximum(tMaximum);
				ray3FSetTMinimum(tMinimum);
				
				if(hasScatteringResult && !primitiveIntersects()) {
					lightDirectR += scatteringResultR * lightResultR / lightProbabilityDensityFunctionValue;
					lightDirectG += scatteringResultG * lightResultG / lightProbabilityDensityFunctionValue;
					lightDirectB += scatteringResultB * lightResultB / lightProbabilityDensityFunctionValue;
				}
			}
		}
		
//		Perform the following if the current light source is not using a delta distribution and a sample for incoming radiance was created:
		if(!isUsingDeltaDistribution && isSampling) {
			final float lightResultR = lightSampleGetResultR();
			final float lightResultG = lightSampleGetResultG();
			final float lightResultB = lightSampleGetResultB();
			
			final float lightIncomingX = lightSampleGetIncomingX();
			final float lightIncomingY = lightSampleGetIncomingY();
			final float lightIncomingZ = lightSampleGetIncomingZ();
			
			final float lightPointX = lightSampleGetPointX();
			final float lightPointY = lightSampleGetPointY();
			final float lightPointZ = lightSampleGetPointZ();
			
			final float lightProbabilityDensityFunctionValue = lightSampleGetProbabilityDensityFunctionValue();
			final float lightProbabilityDensityFunctionValueSquared = lightProbabilityDensityFunctionValue * lightProbabilityDensityFunctionValue;
			
			final boolean hasLightResult = !checkIsZero(lightResultR) || !checkIsZero(lightResultG) || !checkIsZero(lightResultB);
			
			if(hasLightResult && lightProbabilityDensityFunctionValue > 0.0F) {
				materialBSDFEvaluateDistributionFunction(bitFlags, lightIncomingX, lightIncomingY, lightIncomingZ, rayDirectionX, rayDirectionY, rayDirectionZ);
				
				final float materialBSDFResultR = materialBSDFResultGetResultR();
				final float materialBSDFResultG = materialBSDFResultGetResultG();
				final float materialBSDFResultB = materialBSDFResultGetResultB();
				
				final float lightIncomingDotNormalAbs = abs(vector3FDotProduct(lightIncomingX, lightIncomingY, lightIncomingZ, normalCorrectlyOrientedX, normalCorrectlyOrientedY, normalCorrectlyOrientedZ));
				
				final float scatteringResultR = materialBSDFResultR * lightIncomingDotNormalAbs;
				final float scatteringResultG = materialBSDFResultG * lightIncomingDotNormalAbs;
				final float scatteringResultB = materialBSDFResultB * lightIncomingDotNormalAbs;
				
				final boolean hasScatteringResult = !checkIsZero(scatteringResultR) || !checkIsZero(scatteringResultG) || !checkIsZero(scatteringResultB);
				
				final float directionX = lightPointX - surfaceIntersectionPointX;
				final float directionY = lightPointY - surfaceIntersectionPointY;
				final float directionZ = lightPointZ - surfaceIntersectionPointZ;
				final float directionLength = vector3FLength(directionX, directionY, directionZ);
				final float directionLengthReciprocal = 1.0F / directionLength;
				final float directionNormalizedX = directionX * directionLengthReciprocal;
				final float directionNormalizedY = directionY * directionLengthReciprocal;
				final float directionNormalizedZ = directionZ * directionLengthReciprocal;
				
				final float tMaximum = abs(directionLength) + 0.001F;
				final float tMinimum = DEFAULT_T_MINIMUM;
				
				ray3FSetOrigin(surfaceIntersectionPointX + directionNormalizedX * 0.001F, surfaceIntersectionPointY + directionNormalizedY * 0.001F, surfaceIntersectionPointZ + directionNormalizedZ * 0.001F);
				ray3FSetDirection(directionNormalizedX, directionNormalizedY, directionNormalizedZ);
				ray3FSetTMaximum(tMaximum);
				ray3FSetTMinimum(tMinimum);
				
				if(hasScatteringResult && isAreaLight) {
					final boolean isIntersecting = primitiveIntersectionComputeRHS();
					final boolean isIntersectingAreaLight = isIntersecting && primitiveGetAreaLightIDRHS() == lightID && primitiveGetAreaLightOffsetRHS() == lightOffset;
					
					if(isIntersectingAreaLight) {
						materialBSDFEvaluateProbabilityDensityFunction(bitFlags, lightIncomingX, lightIncomingY, lightIncomingZ);
						
						final float scatteringProbabilityDensityFunctionValue = materialBSDFResultGetProbabilityDensityFunctionValue();
						final float scatteringProbabilityDensityFunctionValueSquared = scatteringProbabilityDensityFunctionValue * scatteringProbabilityDensityFunctionValue;
						
						final float weight = lightProbabilityDensityFunctionValueSquared / (lightProbabilityDensityFunctionValueSquared + scatteringProbabilityDensityFunctionValueSquared);
						
						lightDirectR += scatteringResultR * lightResultR * weight / lightProbabilityDensityFunctionValue;
						lightDirectG += scatteringResultG * lightResultG * weight / lightProbabilityDensityFunctionValue;
						lightDirectB += scatteringResultB * lightResultB * weight / lightProbabilityDensityFunctionValue;
					}
				}
				
				if(hasScatteringResult && !isAreaLight && !primitiveIntersects()) {
					materialBSDFEvaluateProbabilityDensityFunction(bitFlags, lightIncomingX, lightIncomingY, lightIncomingZ);
					
					final float scatteringProbabilityDensityFunctionValue = materialBSDFResultGetProbabilityDensityFunctionValue();
					final float scatteringProbabilityDensityFunctionValueSquared = scatteringProbabilityDensityFunctionValue * scatteringProbabilityDensityFunctionValue;
					
					final float weight = lightProbabilityDensityFunctionValueSquared / (lightProbabilityDensityFunctionValueSquared + scatteringProbabilityDensityFunctionValueSquared);
					
					lightDirectR += scatteringResultR * lightResultR * weight / lightProbabilityDensityFunctionValue;
					lightDirectG += scatteringResultG * lightResultG * weight / lightProbabilityDensityFunctionValue;
					lightDirectB += scatteringResultB * lightResultB * weight / lightProbabilityDensityFunctionValue;
				}
			}
		}
		
//		Perform the following if the current light source is not using a delta distribution and a sample from the BSDF was created:
		if(!isUsingDeltaDistribution && materialBSDFSampleDistributionFunction(bitFlags, sampleBU, sampleBV, rayDirectionX, rayDirectionY, rayDirectionZ)) {
			final boolean hasSampledSpecular = materialBSDFResultBXDFIsSpecular();
			
			final float incomingX = materialBSDFResultGetIncomingX();
			final float incomingY = materialBSDFResultGetIncomingY();
			final float incomingZ = materialBSDFResultGetIncomingZ();
			
			final float scatteringProbabilityDensityFunctionValue = materialBSDFResultGetProbabilityDensityFunctionValue();
			final float scatteringProbabilityDensityFunctionValueSquared = scatteringProbabilityDensityFunctionValue * scatteringProbabilityDensityFunctionValue;
			
			final float resultR = materialBSDFResultGetResultR();
			final float resultG = materialBSDFResultGetResultG();
			final float resultB = materialBSDFResultGetResultB();
			
			final float incomingDotNormalAbs = abs(vector3FDotProduct(incomingX, incomingY, incomingZ, normalCorrectlyOrientedX, normalCorrectlyOrientedY, normalCorrectlyOrientedZ));
			
			final float scatteringResultR = resultR * incomingDotNormalAbs;
			final float scatteringResultG = resultG * incomingDotNormalAbs;
			final float scatteringResultB = resultB * incomingDotNormalAbs;
			
			final boolean hasScatteringResult = !checkIsZero(scatteringResultR) || !checkIsZero(scatteringResultG) || !checkIsZero(scatteringResultB);
			
			if(hasScatteringResult && scatteringProbabilityDensityFunctionValue > 0.0F) {
				float weight = 1.0F;
				
				if(!hasSampledSpecular) {
					final float lightProbabilityDensityFunctionValue = lightEvaluateProbabilityDensityFunctionRadianceIncoming(incomingX, incomingY, incomingZ);
					final float lightProbabilityDensityFunctionValueSquared = lightProbabilityDensityFunctionValue * lightProbabilityDensityFunctionValue;
					
					if(checkIsZero(lightProbabilityDensityFunctionValue)) {
						color3FLHSSet(lightDirectR, lightDirectG, lightDirectB);
						
						return;
					}
					
					weight = scatteringProbabilityDensityFunctionValueSquared / (scatteringProbabilityDensityFunctionValueSquared + lightProbabilityDensityFunctionValueSquared);
				}
				
				final float directionX = incomingX;
				final float directionY = incomingY;
				final float directionZ = incomingZ;
				final float directionLength = vector3FLength(directionX, directionY, directionZ);
				final float directionLengthReciprocal = 1.0F / directionLength;
				final float directionNormalizedX = directionX * directionLengthReciprocal;
				final float directionNormalizedY = directionY * directionLengthReciprocal;
				final float directionNormalizedZ = directionZ * directionLengthReciprocal;
				
				final float tMaximum = DEFAULT_T_MAXIMUM;
				final float tMinimum = DEFAULT_T_MINIMUM;
				
				ray3FSetOrigin(surfaceIntersectionPointX + directionNormalizedX * 0.001F, surfaceIntersectionPointY + directionNormalizedY * 0.001F, surfaceIntersectionPointZ + directionNormalizedZ * 0.001F);
				ray3FSetDirection(directionNormalizedX, directionNormalizedY, directionNormalizedZ);
				ray3FSetTMaximum(tMaximum);
				ray3FSetTMinimum(tMinimum);
				
				if(isAreaLight && primitiveIntersectionComputeRHS() && primitiveGetAreaLightIDRHS() == lightID && primitiveGetAreaLightOffsetRHS() == lightOffset) {
					lightEvaluateRadianceEmittedAreaLight(normalCorrectlyOrientedX, normalCorrectlyOrientedY, normalCorrectlyOrientedZ, ray3FGetDirectionX(), ray3FGetDirectionY(), ray3FGetDirectionZ());
					
					final float lightIncomingR = color3FLHSGetR();
					final float lightIncomingG = color3FLHSGetG();
					final float lightIncomingB = color3FLHSGetB();
					
					final boolean hasLightIncoming = !checkIsZero(lightIncomingR) || !checkIsZero(lightIncomingG) || !checkIsZero(lightIncomingB);
					
					if(hasLightIncoming) {
						lightDirectR += scatteringResultR * lightIncomingR * weight / scatteringProbabilityDensityFunctionValue;
						lightDirectG += scatteringResultG * lightIncomingG * weight / scatteringProbabilityDensityFunctionValue;
						lightDirectB += scatteringResultB * lightIncomingB * weight / scatteringProbabilityDensityFunctionValue;
					}
				}
				
				if(!isAreaLight && !primitiveIntersects()) {
					lightEvaluateRadianceEmitted(ray3FGetDirectionX(), ray3FGetDirectionY(), ray3FGetDirectionZ());
					
					final float lightIncomingR = color3FLHSGetR();
					final float lightIncomingG = color3FLHSGetG();
					final float lightIncomingB = color3FLHSGetB();
					
					final boolean hasLightIncoming = !checkIsZero(lightIncomingR) || !checkIsZero(lightIncomingG) || !checkIsZero(lightIncomingB);
					
					if(hasLightIncoming) {
						lightDirectR += scatteringResultR * lightIncomingR * weight / scatteringProbabilityDensityFunctionValue;
						lightDirectG += scatteringResultG * lightIncomingG * weight / scatteringProbabilityDensityFunctionValue;
						lightDirectB += scatteringResultB * lightIncomingB * weight / scatteringProbabilityDensityFunctionValue;
					}
				}
			}
		}
		
//		Reset the current ray:
		ray3FSetOrigin(rayOriginX, rayOriginY, rayOriginZ);
		ray3FSetDirection(rayDirectionX, rayDirectionY, rayDirectionZ);
		ray3FSetTMaximum(rayTMaximum);
		ray3FSetTMinimum(rayTMinimum);
		
		color3FLHSSet(lightDirectR, lightDirectG, lightDirectB);
	}
	
	private void doRay3FSetMatrix44FTransform(final int primitiveMatrix44FArrayOffset) {
//		Retrieve the matrix elements:
		final float element11 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_1_1];
		final float element12 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_1_2];
		final float element13 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_1_3];
		final float element14 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_1_4];
		final float element21 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_2_1];
		final float element22 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_2_2];
		final float element23 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_2_3];
		final float element24 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_2_4];
		final float element31 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_3_1];
		final float element32 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_3_2];
		final float element33 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_3_3];
		final float element34 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_3_4];
		final float element41 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_4_1];
		final float element42 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_4_2];
		final float element43 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_4_3];
		final float element44 = this.primitiveMatrix44FArray[primitiveMatrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_4_4];
		
//		Retrieve the ray origin components from the old space:
		final float oldOriginX = ray3FGetOriginX();
		final float oldOriginY = ray3FGetOriginY();
		final float oldOriginZ = ray3FGetOriginZ();
		
//		Retrieve the ray direction components from the old space:
		final float oldDirectionX = ray3FGetDirectionX();
		final float oldDirectionY = ray3FGetDirectionY();
		final float oldDirectionZ = ray3FGetDirectionZ();
		
//		Retrieve the ray boundary variables from the old space:
		final float oldTMinimum = ray3FGetTMinimum();
		final float oldTMaximum = ray3FGetTMaximum();
		
//		Transform the ray origin from the old space to the new space:
		point3FSetMatrix44FTransformAndDivide(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, oldOriginX, oldOriginY, oldOriginZ);
		
//		Transform the ray direction from the old space to the new space:
		vector3FSetMatrix44FTransformNormalize(element11, element12, element13, element21, element22, element23, element31, element32, element33, oldDirectionX, oldDirectionY, oldDirectionZ);
		
//		Retrieve the ray origin components from the new space:
		final float newOriginX = point3FGetX();
		final float newOriginY = point3FGetY();
		final float newOriginZ = point3FGetZ();
		
//		Retrieve the ray direction components from the new space:
		final float newDirectionX = vector3FGetX();
		final float newDirectionY = vector3FGetY();
		final float newDirectionZ = vector3FGetZ();
		
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
			final float newReferencePointTMinimumX = point3FGetX();
			final float newReferencePointTMinimumY = point3FGetY();
			final float newReferencePointTMinimumZ = point3FGetZ();
			
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
			final float newReferencePointTMaximumX = point3FGetX();
			final float newReferencePointTMaximumY = point3FGetY();
			final float newReferencePointTMaximumZ = point3FGetZ();
			
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
		put(this.pixelArray = Arrays.repeat(new float[] {0.0F}, getResolution() * 2));
	}
	
	private void doSetupScene() {
		final SceneCompiler sceneCompiler = new SceneCompiler();
		
		final CompiledScene compiledScene = sceneCompiler.compile(getScene());
		
		put(super.boundingVolume3FAxisAlignedBoundingBox3FArray = doGetCompatibleArray(compiledScene.getCompiledBoundingVolume3FCache().getAxisAlignedBoundingBox3Fs()));
		put(super.boundingVolume3FBoundingSphere3FArray = doGetCompatibleArray(compiledScene.getCompiledBoundingVolume3FCache().getBoundingSphere3Fs()));
		
		put(super.shape3FCone3FArray = doGetCompatibleArray(compiledScene.getCompiledShape3FCache().getCone3Fs()));
		put(super.shape3FCylinder3FArray = doGetCompatibleArray(compiledScene.getCompiledShape3FCache().getCylinder3Fs()));
		put(super.shape3FDisk3FArray = doGetCompatibleArray(compiledScene.getCompiledShape3FCache().getDisk3Fs()));
		put(super.shape3FHyperboloid3FArray = doGetCompatibleArray(compiledScene.getCompiledShape3FCache().getHyperboloid3Fs()));
		put(super.shape3FParaboloid3FArray = doGetCompatibleArray(compiledScene.getCompiledShape3FCache().getParaboloid3Fs()));
		put(super.shape3FPolygon3FArray = doGetCompatibleArray(compiledScene.getCompiledShape3FCache().getPolygon3Fs()));
		put(super.shape3FRectangle3FArray = doGetCompatibleArray(compiledScene.getCompiledShape3FCache().getRectangle3Fs()));
		put(super.shape3FRectangularCuboid3FArray = doGetCompatibleArray(compiledScene.getCompiledShape3FCache().getRectangularCuboid3Fs()));
		put(super.shape3FTorus3FArray = doGetCompatibleArray(compiledScene.getCompiledShape3FCache().getTorus3Fs()));
		put(super.shape3FTriangle3FArray = doGetCompatibleArray(compiledScene.getCompiledShape3FCache().getTriangle3Fs()));
		put(super.shape3FTriangleMesh3FArray = doGetCompatibleArray(compiledScene.getCompiledShape3FCache().getTriangleMesh3Fs()));
		
		put(super.textureBlendTextureArray = doGetCompatibleArray(compiledScene.getCompiledTextureCache().getBlendTextures()));
		put(super.textureBullseyeTextureArray = doGetCompatibleArray(compiledScene.getCompiledTextureCache().getBullseyeTextures()));
		put(super.textureCheckerboardTextureArray = doGetCompatibleArray(compiledScene.getCompiledTextureCache().getCheckerboardTextures()));
		put(super.textureConstantTextureArray = doGetCompatibleArray(compiledScene.getCompiledTextureCache().getConstantTextures()));
		put(super.textureLDRImageTextureArray = doGetCompatibleArray(compiledScene.getCompiledTextureCache().getLDRImageTextures()));
		put(super.textureLDRImageTextureOffsetArray = doGetCompatibleArray(compiledScene.getCompiledTextureCache().getLDRImageTextureOffsets()));
		put(super.textureMarbleTextureArray = doGetCompatibleArray(compiledScene.getCompiledTextureCache().getMarbleTextures()));
		put(super.texturePolkaDotTextureArray = doGetCompatibleArray(compiledScene.getCompiledTextureCache().getPolkaDotTextures()));
		put(super.textureSimplexFractionalBrownianMotionTextureArray = doGetCompatibleArray(compiledScene.getCompiledTextureCache().getSimplexFractionalBrownianMotionTextures()));
		
		put(super.materialBullseyeMaterialArray = doGetCompatibleArray(compiledScene.getCompiledMaterialCache().getBullseyeMaterials()));
		put(super.materialCheckerboardMaterialArray = doGetCompatibleArray(compiledScene.getCompiledMaterialCache().getCheckerboardMaterials()));
		put(super.materialClearCoatMaterialArray = doGetCompatibleArray(compiledScene.getCompiledMaterialCache().getClearCoatMaterials()));
		put(super.materialDisneyMaterialArray = doGetCompatibleArray(compiledScene.getCompiledMaterialCache().getDisneyMaterials()));
		put(super.materialGlassMaterialArray = doGetCompatibleArray(compiledScene.getCompiledMaterialCache().getGlassMaterials()));
		put(super.materialGlossyMaterialArray = doGetCompatibleArray(compiledScene.getCompiledMaterialCache().getGlossyMaterials()));
		put(super.materialMatteMaterialArray = doGetCompatibleArray(compiledScene.getCompiledMaterialCache().getMatteMaterials()));
		put(super.materialMetalMaterialArray = doGetCompatibleArray(compiledScene.getCompiledMaterialCache().getMetalMaterials()));
		put(super.materialMirrorMaterialArray = doGetCompatibleArray(compiledScene.getCompiledMaterialCache().getMirrorMaterials()));
		put(super.materialPlasticMaterialArray = doGetCompatibleArray(compiledScene.getCompiledMaterialCache().getPlasticMaterials()));
		put(super.materialPolkaDotMaterialArray = doGetCompatibleArray(compiledScene.getCompiledMaterialCache().getPolkaDotMaterials()));
		put(super.materialSubstrateMaterialArray = doGetCompatibleArray(compiledScene.getCompiledMaterialCache().getSubstrateMaterials()));
		
		put(super.modifierNormalMapLDRImageModifierArray = doGetCompatibleArray(compiledScene.getCompiledModifierCache().getNormalMapLDRImageModifiers()));
		put(super.modifierNormalMapLDRImageModifierOffsetArray = doGetCompatibleArray(compiledScene.getCompiledModifierCache().getNormalMapLDRImageModifierOffsets()));
		put(super.modifierSimplexNoiseNormalMapModifierArray = doGetCompatibleArray(compiledScene.getCompiledModifierCache().getSimplexNoiseNormalMapModifiers()));
		
		put(super.lightIDAndOffsetArray = doGetCompatibleArray(compiledScene.getCompiledLightCache().getLightIDsAndOffsets()));
		put(super.lightDiffuseAreaLightArray = doGetCompatibleArray(compiledScene.getCompiledLightCache().getDiffuseAreaLights()));
		put(super.lightDirectionalLightArray = doGetCompatibleArray(compiledScene.getCompiledLightCache().getDirectionalLights()));
		put(super.lightImageLightArray = doGetCompatibleArray(compiledScene.getCompiledLightCache().getImageLights()));
		put(super.lightImageLightOffsetArray = doGetCompatibleArray(compiledScene.getCompiledLightCache().getImageLightOffsets()));
		put(super.lightPerezLightArray = doGetCompatibleArray(compiledScene.getCompiledLightCache().getPerezLights()));
		put(super.lightPerezLightOffsetArray = doGetCompatibleArray(compiledScene.getCompiledLightCache().getPerezLightOffsets()));
		put(super.lightPointLightArray = doGetCompatibleArray(compiledScene.getCompiledLightCache().getPointLights()));
		put(super.lightSpotLightArray = doGetCompatibleArray(compiledScene.getCompiledLightCache().getSpotLights()));
		
		put(this.cameraArray = doGetCompatibleArray(compiledScene.getCompiledCameraCache().getCamera()));
		
		put(this.primitiveArray = doGetCompatibleArray(compiledScene.getCompiledPrimitiveCache().getPrimitives()));
		put(this.primitiveMatrix44FArray = doGetCompatibleArray(compiledScene.getCompiledPrimitiveCache().getMatrix44Fs()));
		
		super.lightCount = compiledScene.getCompiledLightCache().getLightCount();
		super.lightDiffuseAreaLightCount = compiledScene.getCompiledLightCache().getDiffuseAreaLightCount();
		super.lightDirectionalLightCount = compiledScene.getCompiledLightCache().getDirectionalLightCount();
		super.lightImageLightCount = compiledScene.getCompiledLightCache().getImageLightCount();
		super.lightPerezLightCount = compiledScene.getCompiledLightCache().getPerezLightCount();
		super.lightPointLightCount = compiledScene.getCompiledLightCache().getPointLightCount();
		super.lightSpotLightCount = compiledScene.getCompiledLightCache().getSpotLightCount();
		
		this.primitiveCount = compiledScene.getCompiledPrimitiveCache().getPrimitiveCount();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float[] doGetCompatibleArray(final float[] array) {
		return array.length > 0 ? array : new float[1];
	}
	
	private static int[] doGetCompatibleArray(final int[] array) {
		return array.length > 0 ? array : new int[1];
	}
}