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

import static org.dayflower.utility.Floats.PI;
import static org.dayflower.utility.Floats.PI_DIVIDED_BY_2;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.utility.Floats.PI_RECIPROCAL;

import java.lang.reflect.Field;
import java.util.Objects;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.geometry.boundingvolume.BoundingSphere3F;
import org.dayflower.geometry.boundingvolume.InfiniteBoundingVolume3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.geometry.shape.RectangularCuboid3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Torus3F;
import org.dayflower.geometry.shape.Triangle3F;
import org.dayflower.geometry.shape.TriangleMesh3F;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.light.LDRImageLight;
import org.dayflower.scene.material.rayito.GlassRayitoMaterial;
import org.dayflower.scene.material.rayito.MatteRayitoMaterial;
import org.dayflower.scene.material.rayito.MetalRayitoMaterial;
import org.dayflower.scene.material.rayito.MirrorRayitoMaterial;
import org.dayflower.scene.material.smallpt.ClearCoatSmallPTMaterial;
import org.dayflower.scene.material.smallpt.GlassSmallPTMaterial;
import org.dayflower.scene.material.smallpt.MatteSmallPTMaterial;
import org.dayflower.scene.material.smallpt.MetalSmallPTMaterial;
import org.dayflower.scene.material.smallpt.MirrorSmallPTMaterial;
import org.dayflower.scene.material.smallpt.SmallPTMaterial;
import org.dayflower.scene.texture.BlendTexture;
import org.dayflower.scene.texture.BullseyeTexture;
import org.dayflower.scene.texture.CheckerboardTexture;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.FunctionTexture;
import org.dayflower.scene.texture.LDRImageTexture;
import org.dayflower.scene.texture.MarbleTexture;
import org.dayflower.scene.texture.SimplexFractionalBrownianMotionTexture;
import org.dayflower.scene.texture.SurfaceNormalTexture;
import org.dayflower.scene.texture.UVTexture;
import org.dayflower.utility.Floats;

/**
 * An {@code AbstractSceneKernel} is an abstract extension of the {@code AbstractImageKernel} class that adds additional features.
 * <p>
 * The features added are the following:
 * <ul>
 * <li>Bounding volume intersection methods</li>
 * <li>Camera ray generation methods</li>
 * <li>Primitive intersection methods</li>
 * <li>Shape intersection methods</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractSceneKernel extends AbstractImageKernel {
//	TODO: Add Javadocs!
	protected static final float DEFAULT_T_MAXIMUM = Float.MAX_VALUE;
	
//	TODO: Add Javadocs!
	protected static final float DEFAULT_T_MINIMUM = 0.001F;
	
//	TODO: Add Javadocs!
	protected static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U = 0;
	
//	TODO: Add Javadocs!
	protected static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V = 3;
	
//	TODO: Add Javadocs!
	protected static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W = 6;
	
//	TODO: Add Javadocs!
	protected static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U = 9;
	
//	TODO: Add Javadocs!
	protected static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V = 12;
	
//	TODO: Add Javadocs!
	protected static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W = 15;
	
//	TODO: Add Javadocs!
	protected static final int INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX = 18;
	
//	TODO: Add Javadocs!
	protected static final int INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT = 19;
	
//	TODO: Add Javadocs!
	protected static final int INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES = 22;
	
//	TODO: Add Javadocs!
	protected static final int INTERSECTION_ARRAY_SIZE = 24;
	
//	TODO: Add Javadocs!
	protected static final int MATERIAL_B_X_D_F_ARRAY_OFFSET_INCOMING = 0;
	
//	TODO: Add Javadocs!
	protected static final int MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL = 3;
	
//	TODO: Add Javadocs!
	protected static final int MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING = 6;
	
//	TODO: Add Javadocs!
	protected static final int MATERIAL_B_X_D_F_ARRAY_SIZE = 16;
	
//	TODO: Add Javadocs!
	protected static final int RAY_3_F_ARRAY_OFFSET_DIRECTION = 3;
	
//	TODO: Add Javadocs!
	protected static final int RAY_3_F_ARRAY_OFFSET_ORIGIN = 0;
	
//	TODO: Add Javadocs!
	protected static final int RAY_3_F_ARRAY_OFFSET_T_MAXIMUM = 7;
	
//	TODO: Add Javadocs!
	protected static final int RAY_3_F_ARRAY_OFFSET_T_MINIMUM = 6;
	
//	TODO: Add Javadocs!
	protected static final int RAY_3_F_ARRAY_SIZE = 8;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	protected float[] boundingVolume3FAxisAlignedBoundingBox3FArray;
	
//	TODO: Add Javadocs!
	protected float[] boundingVolume3FBoundingSphere3FArray;
	
//	TODO: Add Javadocs!
	protected float[] cameraArray;
	
//	TODO: Add Javadocs!
	protected float[] intersectionArray_$private$24;
	
//	TODO: Add Javadocs!
	protected float[] lightLDRImageLightArray;
	
//	TODO: Add Javadocs!
	protected float[] materialBXDFResultArray_$private$16;
	
//	TODO: Add Javadocs!
	protected float[] matrix44FArray;
	
//	TODO: Add Javadocs!
	protected float[] pixelArray;
	
//	TODO: Add Javadocs!
	protected float[] ray3FArray_$private$8;
	
//	TODO: Add Javadocs!
	protected float[] shape3FPlane3FArray;
	
//	TODO: Add Javadocs!
	protected float[] shape3FRectangularCuboid3FArray;
	
//	TODO: Add Javadocs!
	protected float[] shape3FSphere3FArray;
	
//	TODO: Add Javadocs!
	protected float[] shape3FTorus3FArray;
	
//	TODO: Add Javadocs!
	protected float[] shape3FTriangle3FArray;
	
//	TODO: Add Javadocs!
	protected float[] textureBlendTextureArray;
	
//	TODO: Add Javadocs!
	protected float[] textureBullseyeTextureArray;
	
//	TODO: Add Javadocs!
	protected float[] textureCheckerboardTextureArray;
	
//	TODO: Add Javadocs!
	protected float[] textureConstantTextureArray;
	
//	TODO: Add Javadocs!
	protected float[] textureLDRImageTextureArray;
	
//	TODO: Add Javadocs!
	protected float[] textureMarbleTextureArray;
	
//	TODO: Add Javadocs!
	protected float[] textureSimplexFractionalBrownianMotionTextureArray;
	
//	TODO: Add Javadocs!
	protected int lightLDRImageLightCount;
	
//	TODO: Add Javadocs!
	protected int primitiveCount;
	
//	TODO: Add Javadocs!
	protected int[] lightLDRImageLightOffsetArray;
	
//	TODO: Add Javadocs!
	protected int[] materialClearCoatSmallPTMaterialArray;
	
//	TODO: Add Javadocs!
	protected int[] materialGlassRayitoMaterialArray;
	
//	TODO: Add Javadocs!
	protected int[] materialGlassSmallPTMaterialArray;
	
//	TODO: Add Javadocs!
	protected int[] materialMatteRayitoMaterialArray;
	
//	TODO: Add Javadocs!
	protected int[] materialMatteSmallPTMaterialArray;
	
//	TODO: Add Javadocs!
	protected int[] materialMetalRayitoMaterialArray;
	
//	TODO: Add Javadocs!
	protected int[] materialMetalSmallPTMaterialArray;
	
//	TODO: Add Javadocs!
	protected int[] materialMirrorRayitoMaterialArray;
	
//	TODO: Add Javadocs!
	protected int[] materialMirrorSmallPTMaterialArray;
	
//	TODO: Add Javadocs!
	protected int[] primitiveArray;
	
//	TODO: Add Javadocs!
	protected int[] shape3FTriangleMesh3FArray;
	
//	TODO: Add Javadocs!
	protected int[] shape3FTriangleMesh3FArrayToShape3FTriangle3FArray_$private$1;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Scene scene;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractSceneKernel} instance.
	 */
	protected AbstractSceneKernel() {
		this.boundingVolume3FAxisAlignedBoundingBox3FArray = new float[1];
		this.boundingVolume3FBoundingSphere3FArray = new float[1];
		this.cameraArray = new float[1];
		this.intersectionArray_$private$24 = new float[INTERSECTION_ARRAY_SIZE];
		this.lightLDRImageLightArray = new float[1];
		this.materialBXDFResultArray_$private$16 = new float[MATERIAL_B_X_D_F_ARRAY_SIZE];
		this.matrix44FArray = new float[1];
		this.pixelArray = new float[1];
		this.ray3FArray_$private$8 = new float[RAY_3_F_ARRAY_SIZE];
		this.shape3FPlane3FArray = new float[1];
		this.shape3FRectangularCuboid3FArray = new float[1];
		this.shape3FSphere3FArray = new float[1];
		this.shape3FTorus3FArray = new float[1];
		this.shape3FTriangle3FArray = new float[1];
		this.textureBlendTextureArray = new float[1];
		this.textureBullseyeTextureArray = new float[1];
		this.textureCheckerboardTextureArray = new float[1];
		this.textureConstantTextureArray = new float[1];
		this.textureLDRImageTextureArray = new float[1];
		this.textureMarbleTextureArray = new float[1];
		this.textureSimplexFractionalBrownianMotionTextureArray = new float[1];
		this.lightLDRImageLightCount = 0;
		this.primitiveCount = 0;
		this.lightLDRImageLightOffsetArray = new int[1];
		this.materialClearCoatSmallPTMaterialArray = new int[1];
		this.materialGlassRayitoMaterialArray = new int[1];
		this.materialGlassSmallPTMaterialArray = new int[1];
		this.materialMatteRayitoMaterialArray = new int[1];
		this.materialMatteSmallPTMaterialArray = new int[1];
		this.materialMetalRayitoMaterialArray = new int[1];
		this.materialMetalSmallPTMaterialArray = new int[1];
		this.materialMirrorRayitoMaterialArray = new int[1];
		this.materialMirrorSmallPTMaterialArray = new int[1];
		this.primitiveArray = new int[1];
		this.shape3FTriangleMesh3FArray = new int[1];
		this.shape3FTriangleMesh3FArrayToShape3FTriangle3FArray_$private$1 = new int[1];
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
	 * Returns {@code true} if, and only if, a point is contained by a given axis aligned bounding box, {@code false} otherwise.
	 * 
	 * @param boundingVolume3FAxisAlignedBoundingBox3FArrayOffset the offset for the axis aligned bounding box in {@link #boundingVolume3FAxisAlignedBoundingBox3FArray}
	 * @param pointX the X-component of the point
	 * @param pointY the Y-component of the point
	 * @param pointZ the Z-component of the point
	 * @return {@code true} if, and only if, a point is contained by a given axis aligned bounding box, {@code false} otherwise
	 */
	protected final boolean containsBoundingVolume3FAxisAlignedBoundingBox3F(final int boundingVolume3FAxisAlignedBoundingBox3FArrayOffset, final float pointX, final float pointY, final float pointZ) {
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
	 * Returns {@code true} if, and only if, a point is contained by a given bounding sphere, {@code false} otherwise.
	 * 
	 * @param boundingVolume3FBoundingSphere3FArrayOffset the offset for the bounding sphere in {@link #boundingVolume3FBoundingSphere3FArray}
	 * @param pointX the X-component of the point
	 * @param pointY the Y-component of the point
	 * @param pointZ the Z-component of the point
	 * @return {@code true} if, and only if, a point is contained by a given bounding sphere, {@code false} otherwise
	 */
	protected final boolean containsBoundingVolume3FBoundingSphere3F(final int boundingVolume3FBoundingSphere3FArrayOffset, final float pointX, final float pointY, final float pointZ) {
		final float boundingSphereCenterX = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + BoundingSphere3F.ARRAY_OFFSET_CENTER + 0];
		final float boundingSphereCenterY = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + BoundingSphere3F.ARRAY_OFFSET_CENTER + 1];
		final float boundingSphereCenterZ = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + BoundingSphere3F.ARRAY_OFFSET_CENTER + 2];
		final float boundingSphereRadius = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + BoundingSphere3F.ARRAY_OFFSET_RADIUS];
		
		final float distanceSquared = point3FDistanceSquared(boundingSphereCenterX, boundingSphereCenterY, boundingSphereCenterZ, pointX, pointY, pointZ);
		
		return distanceSquared < boundingSphereRadius * boundingSphereRadius;
	}
	
	/**
	 * Returns {@code true} if, and only if, the current ray is contained by or intersects a given axis aligned bounding box, {@code false} otherwise.
	 * 
	 * @param boundingVolume3FAxisAlignedBoundingBox3FArrayOffset the offset for the axis aligned bounding box in {@link #boundingVolume3FAxisAlignedBoundingBox3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray is contained by or intersects a given axis aligned bounding box, {@code false} otherwise
	 */
	protected final boolean containsOrIntersectsBoundingVolume3FAxisAlignedBoundingBox3F(final int boundingVolume3FAxisAlignedBoundingBox3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables:
		final float rayOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float rayOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float rayOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		final float rayDirectionReciprocalX = 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionReciprocalY = 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionReciprocalZ = 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
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
	 * Returns {@code true} if, and only if, the current ray is contained by or intersects a given bounding sphere, {@code false} otherwise.
	 * 
	 * @param boundingVolume3FBoundingSphere3FArrayOffset the offset for the bounding sphere in {@link #boundingVolume3FBoundingSphere3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray is contained by or intersects a given bounding sphere, {@code false} otherwise
	 */
	protected final boolean containsOrIntersectsBoundingVolume3FBoundingSphere3F(final int boundingVolume3FBoundingSphere3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables:
		final float rayOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float rayOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float rayOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		final float rayDirectionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
//		Retrieve the bounding sphere variables:
		final float boundingSphereCenterX = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + BoundingSphere3F.ARRAY_OFFSET_CENTER + 0];
		final float boundingSphereCenterY = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + BoundingSphere3F.ARRAY_OFFSET_CENTER + 1];
		final float boundingSphereCenterZ = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + BoundingSphere3F.ARRAY_OFFSET_CENTER + 2];
		final float boundingSphereRadius = this.boundingVolume3FBoundingSphere3FArray[boundingVolume3FBoundingSphere3FArrayOffset + BoundingSphere3F.ARRAY_OFFSET_RADIUS];
		final float boundingSphereRadiusSquared = boundingSphereRadius * boundingSphereRadius;
		
		final float distanceSquared = point3FDistanceSquared(boundingSphereCenterX, boundingSphereCenterY, boundingSphereCenterZ, rayOriginX, rayOriginY, rayOriginZ);
		
		if(distanceSquared < boundingSphereRadius * boundingSphereRadius) {
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
	 * Performs an intersection test against all primitives in the scene and computes intersection information for the closest.
	 * <p>
	 * Returns {@code true} if, and only if, an intersection was found, {@code false} otherwise.
	 * <p>
	 * If an intersection was found, the computed information will be present in {@link #intersectionArray_$private$24} in world space.
	 * 
	 * @return {@code true} if, and only if, an intersection was found, {@code false} otherwise
	 */
	protected final boolean intersectionComputeShape3F() {
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
			
			final float tMinimumWorldSpace = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MINIMUM];
			final float tMaximumWorldSpace = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM];
			
			boolean isIntersectingBoundingVolume = false;
			
//			TODO: Find out what causes the order of the if-statements to fail. If InfiniteBoundingVolume3F.ID is checked in the last if-statement, the plane will disappear.
			if(boundingVolumeID == InfiniteBoundingVolume3F.ID) {
				isIntersectingBoundingVolume = true;
			} else if(boundingVolumeID == AxisAlignedBoundingBox3F.ID) {
				isIntersectingBoundingVolume = containsOrIntersectsBoundingVolume3FAxisAlignedBoundingBox3F(boundingVolumeOffset, tMinimumWorldSpace, tMaximumWorldSpace);
			} else if(boundingVolumeID == BoundingSphere3F.ID) {
				isIntersectingBoundingVolume = containsOrIntersectsBoundingVolume3FBoundingSphere3F(boundingVolumeOffset, tMinimumWorldSpace, tMaximumWorldSpace);
			}
			
			if(isIntersectingBoundingVolume) {
				ray3FSetMatrix44FTransformWorldToObject(index);
				
				float tObjectSpace = 0.0F;
				
				final float tMinimumObjectSpace = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MINIMUM];
				final float tMaximumObjectSpace = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM];
				
				if(shapeID == Plane3F.ID) {
					tObjectSpace = intersectionTShape3FPlane3F(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == RectangularCuboid3F.ID) {
					tObjectSpace = intersectionTShape3FRectangularCuboid3F(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Sphere3F.ID) {
					tObjectSpace = intersectionTShape3FSphere3F(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Torus3F.ID) {
					tObjectSpace = intersectionTShape3FTorus3F(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Triangle3F.ID) {
					tObjectSpace = intersectionTShape3FTriangle3F(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == TriangleMesh3F.ID) {
					tObjectSpace = intersectionTShape3FTriangleMesh3F(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				}
				
				if(tObjectSpace > tMinimumObjectSpace && tObjectSpace < tMaximumObjectSpace) {
					this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM] = tObjectSpace;
					
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
			
			final float tObjectSpace = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM];
			
			if(shapeID == Plane3F.ID) {
				intersectionComputeShape3FPlane3F(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == RectangularCuboid3F.ID) {
				intersectionComputeShape3FRectangularCuboid3F(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Sphere3F.ID) {
				intersectionComputeShape3FSphere3F(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Torus3F.ID) {
				intersectionComputeShape3FTorus3F(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Triangle3F.ID) {
				intersectionComputeShape3FTriangle3F(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == TriangleMesh3F.ID) {
				intersectionComputeShape3FTriangleMesh3F(tObjectSpace, primitiveIndex);
			}
			
			ray3FSetMatrix44FTransformObjectToWorld(primitiveIndex);
			
			intersectionTransformObjectToWorld(primitiveIndex);
			
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
	protected final boolean intersectsBoundingVolume3FAxisAlignedBoundingBox3F(final int boundingVolume3FAxisAlignedBoundingBox3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return intersectionTBoundingVolume3FAxisAlignedBoundingBox3F(boundingVolume3FAxisAlignedBoundingBox3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given bounding sphere, {@code false} otherwise.
	 * 
	 * @param boundingVolume3FBoundingSphere3FArrayOffset the offset for the bounding sphere in {@link #boundingVolume3FBoundingSphere3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given bounding sphere, {@code false} otherwise
	 */
	protected final boolean intersectsBoundingVolume3FBoundingSphere3F(final int boundingVolume3FBoundingSphere3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return intersectionTBoundingVolume3FBoundingSphere3F(boundingVolume3FBoundingSphere3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given shape in world space, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the current ray intersects a given shape in world space, {@code false} otherwise
	 */
	protected final boolean intersectsShape3F() {
		return intersectionTShape3F() > 0.0F;
	}
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given plane in object space, {@code false} otherwise.
	 * 
	 * @param shape3FPlane3FArrayOffset the offset for the plane in {@link #shape3FPlane3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given plane in object space, {@code false} otherwise
	 */
	protected final boolean intersectsShape3FPlane3F(final int shape3FPlane3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return intersectionTShape3FPlane3F(shape3FPlane3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given rectangular cuboid in object space, {@code false} otherwise.
	 * 
	 * @param shape3FRectangularCuboid3FArrayOffset the offset for the rectangular cuboid in {@link #shape3FRectangularCuboid3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given rectangular cuboid in object space, {@code false} otherwise
	 */
	protected final boolean intersectsShape3FRectangularCuboid3F(final int shape3FRectangularCuboid3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return intersectionTShape3FRectangularCuboid3F(shape3FRectangularCuboid3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given sphere in object space, {@code false} otherwise.
	 * 
	 * @param shape3FSphere3FArrayOffset the offset for the sphere in {@link #shape3FSphere3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given sphere in object space, {@code false} otherwise
	 */
	protected final boolean intersectsShape3FSphere3F(final int shape3FSphere3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return intersectionTShape3FSphere3F(shape3FSphere3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given torus in object space, {@code false} otherwise.
	 * 
	 * @param shape3FTorus3FArrayOffset the offset for the torus in {@link #shape3FTorus3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given torus in object space, {@code false} otherwise
	 */
	protected final boolean intersectsShape3FTorus3F(final int shape3FTorus3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return intersectionTShape3FTorus3F(shape3FTorus3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given triangle in object space, {@code false} otherwise.
	 * 
	 * @param shape3FTriangle3FArrayOffset the offset for the triangle in {@link #shape3FTriangle3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given triangle in object space, {@code false} otherwise
	 */
	protected final boolean intersectsShape3FTriangle3F(final int shape3FTriangle3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return intersectionTShape3FTriangle3F(shape3FTriangle3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given triangle mesh in object space, {@code false} otherwise.
	 * 
	 * @param shape3FTriangleMesh3FArrayOffset the offset for the triangle mesh in {@link #shape3FTriangleMesh3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return {@code true} if, and only if, the current ray intersects a given triangle mesh in object space, {@code false} otherwise
	 */
	protected final boolean intersectsShape3FTriangleMesh3F(final int shape3FTriangleMesh3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
		return intersectionTShape3FTriangleMesh3F(shape3FTriangleMesh3FArrayOffset, rayTMinimum, rayTMaximum) > 0.0F;
	}
	
	/**
	 * Returns {@code true} if, and only if, the material of the currently intersected primitive is specular, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the material of the currently intersected primitive is specular, {@code false} otherwise
	 */
	protected final boolean materialIsSpecular() {
		final int primitiveIndex = (int)(this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX]);
		final int primitiveArrayOffset = primitiveIndex * Primitive.ARRAY_LENGTH;
		final int materialID = this.primitiveArray[primitiveArrayOffset + Primitive.ARRAY_OFFSET_MATERIAL_ID];
		
		if(materialID == GlassRayitoMaterial.ID) {
			return true;
		}
		
		if(materialID == GlassSmallPTMaterial.ID) {
			return true;
		}
		
		if(materialID == MirrorRayitoMaterial.ID) {
			return true;
		}
		
		if(materialID == MirrorSmallPTMaterial.ID) {
			return true;
		}
		
		return false;
	}
	
//	TODO: Add Javadocs!
	protected final boolean materialSampleDistributionFunction() {
		final int primitiveIndex = (int)(this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX]);
		final int primitiveArrayOffset = primitiveIndex * Primitive.ARRAY_LENGTH;
		final int materialID = this.primitiveArray[primitiveArrayOffset + Primitive.ARRAY_OFFSET_MATERIAL_ID];
		
		if(materialID == ClearCoatSmallPTMaterial.ID) {
			return materialSampleDistributionFunctionClearCoatSmallPTMaterial();
		}
		
		if(materialID == GlassRayitoMaterial.ID) {
			return materialSampleDistributionFunctionGlassRayitoMaterial();
		}
		
		if(materialID == GlassSmallPTMaterial.ID) {
			return materialSampleDistributionFunctionGlassSmallPTMaterial();
		}
		
		if(materialID == MatteRayitoMaterial.ID) {
			return materialSampleDistributionFunctionMatteRayitoMaterial();
		}
		
		if(materialID == MatteSmallPTMaterial.ID) {
			return materialSampleDistributionFunctionMatteSmallPTMaterial();
		}
		
		if(materialID == MetalRayitoMaterial.ID) {
			return materialSampleDistributionFunctionMetalRayitoMaterial();
		}
		
		if(materialID == MetalSmallPTMaterial.ID) {
			return materialSampleDistributionFunctionMetalSmallPTMaterial();
		}
		
		if(materialID == MirrorRayitoMaterial.ID) {
			return materialSampleDistributionFunctionMirrorRayitoMaterial();
		}
		
		if(materialID == MirrorSmallPTMaterial.ID) {
			return materialSampleDistributionFunctionMirrorSmallPTMaterial();
		}
		
		return false;
	}
	
//	TODO: Add Javadocs!
	protected final boolean materialSampleDistributionFunctionClearCoatSmallPTMaterial() {
		final int primitiveIndex = (int)(this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX]);
		final int primitiveArrayOffset = primitiveIndex * Primitive.ARRAY_LENGTH;
		final int materialOffset = this.primitiveArray[primitiveArrayOffset + Primitive.ARRAY_OFFSET_MATERIAL_OFFSET];
		final int textureKDID = this.materialClearCoatSmallPTMaterialArray[materialOffset + ClearCoatSmallPTMaterial.ARRAY_OFFSET_TEXTURE_K_D_ID];
		final int textureKDOffset = this.materialClearCoatSmallPTMaterialArray[materialOffset + ClearCoatSmallPTMaterial.ARRAY_OFFSET_TEXTURE_K_D_OFFSET];
		final int textureKSID = this.materialClearCoatSmallPTMaterialArray[materialOffset + ClearCoatSmallPTMaterial.ARRAY_OFFSET_TEXTURE_K_S_ID];
		final int textureKSOffset = this.materialClearCoatSmallPTMaterialArray[materialOffset + ClearCoatSmallPTMaterial.ARRAY_OFFSET_TEXTURE_K_S_OFFSET];
		
		textureEvaluate(textureKDID, textureKDOffset);
		
		final float colorKDR = color3FLHSGetComponent1();
		final float colorKDG = color3FLHSGetComponent2();
		final float colorKDB = color3FLHSGetComponent3();
		
		textureEvaluate(textureKSID, textureKSOffset);
		
		final float colorKSR = color3FLHSGetComponent1();
		final float colorKSG = color3FLHSGetComponent2();
		final float colorKSB = color3FLHSGetComponent3();
		
		final float directionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float directionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float directionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
		final float surfaceNormalX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
		final float surfaceNormalY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
		final float surfaceNormalZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
		
		vector3FSetFaceForwardNegated(surfaceNormalX, surfaceNormalY, surfaceNormalZ, directionX, directionY, directionZ);
		
		final float surfaceNormalCorrectlyOrientedX = vector3FGetComponent1();
		final float surfaceNormalCorrectlyOrientedY = vector3FGetComponent2();
		final float surfaceNormalCorrectlyOrientedZ = vector3FGetComponent3();
		
		final boolean isEntering = vector3FDotProduct(surfaceNormalX, surfaceNormalY, surfaceNormalZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ) > 0.0F;
		
		final float etaA = ETA_VACUUM;
		final float etaB = ETA_GLASS;
		final float etaI = isEntering ? etaA : etaB;
		final float etaT = isEntering ? etaB : etaA;
		final float eta = etaI / etaT;
		
		final boolean isRefracting = vector3FSetRefraction(directionX, directionY, directionZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ, eta);
		final boolean isReflecting = !isRefracting;
		
		if(isRefracting) {
			final float refractionDirectionX = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
			final float refractionDirectionY = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
			final float refractionDirectionZ = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
			
			final float cosThetaI = vector3FDotProduct(directionX, directionY, directionZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ);
			final float cosThetaICorrectlyOriented = isEntering ? -cosThetaI : vector3FDotProduct(refractionDirectionX, refractionDirectionY, refractionDirectionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ);
			
//			final float reflectance = fresnelDielectricSchlick(cosThetaICorrectlyOriented, ((etaB - etaA) * (etaB - etaA)) / ((etaB + etaA) * (etaB + etaA)));
			final float reflectance = fresnelDielectric(cosThetaICorrectlyOriented, etaA, etaB);
			final float transmittance = 1.0F - reflectance;
			
			final float probabilityRussianRoulette = 0.25F + 0.5F * reflectance;
			final float probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
			final float probabilityRussianRouletteTransmission = transmittance / (1.0F - probabilityRussianRoulette);
			
			final boolean isChoosingSpecularReflection = random() < probabilityRussianRoulette;
			
			if(isChoosingSpecularReflection) {
				vector3FSetSpecularReflection(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ, true);
				
				color3FLHSSet(colorKSR * probabilityRussianRouletteReflection, colorKSG * probabilityRussianRouletteReflection, colorKSB * probabilityRussianRouletteReflection);
			} else {
				vector3FSetDiffuseReflection(surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ, random(), random());
				
				color3FLHSSet(colorKDR * probabilityRussianRouletteTransmission, colorKDG * probabilityRussianRouletteTransmission, colorKDB * probabilityRussianRouletteTransmission);
			}
		}
		
		if(isReflecting) {
			vector3FSetSpecularReflection(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ, true);
			
			color3FLHSSet(colorKSR, colorKSG, colorKSB);
		}
		
		return true;
	}
	
//	TODO: Add Javadocs!
	protected final boolean materialSampleDistributionFunctionGlassRayitoMaterial() {
		return false;
	}
	
//	TODO: Add Javadocs!
	protected final boolean materialSampleDistributionFunctionGlassSmallPTMaterial() {
		final int primitiveIndex = (int)(this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX]);
		final int primitiveArrayOffset = primitiveIndex * Primitive.ARRAY_LENGTH;
		final int materialOffset = this.primitiveArray[primitiveArrayOffset + Primitive.ARRAY_OFFSET_MATERIAL_OFFSET];
//		final int textureEtaID = this.materialGlassSmallPTMaterialArray[materialOffset + GlassSmallPTMaterial.ARRAY_OFFSET_TEXTURE_ETA_ID];
//		final int textureEtaOffset = this.materialGlassSmallPTMaterialArray[materialOffset + GlassSmallPTMaterial.ARRAY_OFFSET_TEXTURE_ETA_OFFSET];
		final int textureKRID = this.materialGlassSmallPTMaterialArray[materialOffset + GlassSmallPTMaterial.ARRAY_OFFSET_TEXTURE_K_R_ID];
		final int textureKROffset = this.materialGlassSmallPTMaterialArray[materialOffset + GlassSmallPTMaterial.ARRAY_OFFSET_TEXTURE_K_R_OFFSET];
		final int textureKTID = this.materialGlassSmallPTMaterialArray[materialOffset + GlassSmallPTMaterial.ARRAY_OFFSET_TEXTURE_K_T_ID];
		final int textureKTOffset = this.materialGlassSmallPTMaterialArray[materialOffset + GlassSmallPTMaterial.ARRAY_OFFSET_TEXTURE_K_T_OFFSET];
		
//		textureEvaluate(textureEtaID, textureEtaOffset);
		
//		final float colorEtaR = color3FLHSGetComponent1();
//		final float colorEtaG = color3FLHSGetComponent2();
//		final float colorEtaB = color3FLHSGetComponent3();
		
		textureEvaluate(textureKRID, textureKROffset);
		
		final float colorKRR = color3FLHSGetComponent1();
		final float colorKRG = color3FLHSGetComponent2();
		final float colorKRB = color3FLHSGetComponent3();
		
		textureEvaluate(textureKTID, textureKTOffset);
		
		final float colorKTR = color3FLHSGetComponent1();
		final float colorKTG = color3FLHSGetComponent2();
		final float colorKTB = color3FLHSGetComponent3();
		
		final float directionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float directionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float directionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
		final float surfaceNormalX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
		final float surfaceNormalY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
		final float surfaceNormalZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
		
		vector3FSetFaceForwardNegated(surfaceNormalX, surfaceNormalY, surfaceNormalZ, directionX, directionY, directionZ);
		
		final float surfaceNormalCorrectlyOrientedX = vector3FGetComponent1();
		final float surfaceNormalCorrectlyOrientedY = vector3FGetComponent2();
		final float surfaceNormalCorrectlyOrientedZ = vector3FGetComponent3();
		
		final boolean isEntering = vector3FDotProduct(surfaceNormalX, surfaceNormalY, surfaceNormalZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ) > 0.0F;
		
		final float etaA = ETA_VACUUM;
		final float etaB = ETA_GLASS;
//		final float etaB = (colorEtaR + colorEtaG + colorEtaB) / 3.0F;
		final float etaI = isEntering ? etaA : etaB;
		final float etaT = isEntering ? etaB : etaA;
		final float eta = etaI / etaT;
		
		final boolean isRefracting = vector3FSetRefraction(directionX, directionY, directionZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ, eta);
		final boolean isReflecting = !isRefracting;
		
		if(isRefracting) {
			final float refractionDirectionX = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
			final float refractionDirectionY = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
			final float refractionDirectionZ = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
			
			final float cosThetaI = vector3FDotProduct(directionX, directionY, directionZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ);
			final float cosThetaICorrectlyOriented = isEntering ? -cosThetaI : vector3FDotProduct(refractionDirectionX, refractionDirectionY, refractionDirectionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ);
			
//			final float reflectance = fresnelDielectricSchlick(cosThetaICorrectlyOriented, ((etaB - etaA) * (etaB - etaA)) / ((etaB + etaA) * (etaB + etaA)));
			final float reflectance = fresnelDielectric(cosThetaICorrectlyOriented, etaA, etaB);
			final float transmittance = 1.0F - reflectance;
			
			final float probabilityRussianRoulette = 0.25F + 0.5F * reflectance;
			final float probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
			final float probabilityRussianRouletteTransmission = transmittance / (1.0F - probabilityRussianRoulette);
			
			final boolean isChoosingSpecularReflection = random() < probabilityRussianRoulette;
			
			if(isChoosingSpecularReflection) {
				vector3FSetSpecularReflection(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ, true);
				
				color3FLHSSet(colorKRR * probabilityRussianRouletteReflection, colorKRG * probabilityRussianRouletteReflection, colorKRB * probabilityRussianRouletteReflection);
			} else {
				color3FLHSSet(colorKTR * probabilityRussianRouletteTransmission, colorKTG * probabilityRussianRouletteTransmission, colorKTB * probabilityRussianRouletteTransmission);
			}
		}
		
		if(isReflecting) {
			vector3FSetSpecularReflection(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ, true);
			
			color3FLHSSet(colorKRR, colorKRG, colorKRB);
		}
		
		return true;
	}
	
//	TODO: Add Javadocs!
	protected final boolean materialSampleDistributionFunctionMatteRayitoMaterial() {
		/*
		 * Material:
		 */
		
//		Retrieve indices and offsets:
		final int primitiveIndex = (int)(this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX]);
		final int primitiveArrayOffset = primitiveIndex * Primitive.ARRAY_LENGTH;
		final int materialOffset = this.primitiveArray[primitiveArrayOffset + Primitive.ARRAY_OFFSET_MATERIAL_OFFSET];
		final int textureKDID = this.materialMatteRayitoMaterialArray[materialOffset + MatteRayitoMaterial.ARRAY_OFFSET_TEXTURE_K_D_ID];
		final int textureKDOffset = this.materialMatteRayitoMaterialArray[materialOffset + MatteRayitoMaterial.ARRAY_OFFSET_TEXTURE_K_D_OFFSET];
		
//		Evaluate the KD texture:
		textureEvaluate(textureKDID, textureKDOffset);
		
//		Retrieve the color from the KD texture:
		final float colorKDR = color3FLHSGetComponent1();
		final float colorKDG = color3FLHSGetComponent2();
		final float colorKDB = color3FLHSGetComponent3();
		
		
		
		/*
		 * BSDF:
		 */
		
//		Perform world space to shade space transformations:
		materialBXDFBegin();
		
		
		
		/*
		 * BXDF:
		 */
		
//		Retrieve the normal in shade space:
		final float normalShadeSpaceX = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL + 0];
		final float normalShadeSpaceY = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL + 1];
		final float normalShadeSpaceZ = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL + 2];
		
//		Retrieve the outgoing direction in shade space:
		final float outgoingShadeSpaceX = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING + 0];
		final float outgoingShadeSpaceY = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING + 1];
		final float outgoingShadeSpaceZ = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING + 2];
		
//		Compute the dot product between the normal in shade space and the outgoing direction in shade space:
		final float normalShadeSpaceDotOutgoingShadeSpace = vector3FDotProduct(normalShadeSpaceX, normalShadeSpaceY, normalShadeSpaceZ, outgoingShadeSpaceX, outgoingShadeSpaceY, outgoingShadeSpaceZ);
		
//		Sample the hemisphere in local space using a cosine distribution:
		vector3FSetSampleHemisphereCosineDistribution2(random(), random());
		
//		Retrieve the incoming direction in local space and transform it into shade space:
		final float incomingLocalSpaceX = -super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float incomingLocalSpaceY = -super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float incomingLocalSpaceZ = -super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		final float incomingShadeSpaceX = normalShadeSpaceDotOutgoingShadeSpace < 0.0F ? -incomingLocalSpaceX : incomingLocalSpaceX;
		final float incomingShadeSpaceY = normalShadeSpaceDotOutgoingShadeSpace < 0.0F ? -incomingLocalSpaceY : incomingLocalSpaceY;
		final float incomingShadeSpaceZ = normalShadeSpaceDotOutgoingShadeSpace < 0.0F ? -incomingLocalSpaceZ : incomingLocalSpaceZ;
		
//		Compute the dot product between the normal in shade space and the incoming direction in shade space and its absolute representation:
		final float normalShadeSpaceDotIncomingShadeSpace = vector3FDotProduct(normalShadeSpaceX, normalShadeSpaceY, normalShadeSpaceZ, incomingShadeSpaceX, incomingShadeSpaceY, incomingShadeSpaceZ);
		final float normalShadeSpaceDotIncomingShadeSpaceAbs = abs(normalShadeSpaceDotIncomingShadeSpace);
		
//		Check that the dot products are opposite:
		if(normalShadeSpaceDotIncomingShadeSpace > 0.0F && normalShadeSpaceDotOutgoingShadeSpace > 0.0F || normalShadeSpaceDotIncomingShadeSpace < 0.0F && normalShadeSpaceDotOutgoingShadeSpace < 0.0F) {
			return false;
		}
		
//		Compute the probability density function (PDF) value:
		final float probabilityDensityFunctionValue = PI_RECIPROCAL * normalShadeSpaceDotIncomingShadeSpaceAbs;
		
		if(probabilityDensityFunctionValue <= 0.0F) {
			return false;
		}
		
//		Compute the result:
		final float resultR = colorKDR * PI_RECIPROCAL;
		final float resultG = colorKDG * PI_RECIPROCAL;
		final float resultB = colorKDB * PI_RECIPROCAL;
		
		
		
		/*
		 * BSDF:
		 */
		
//		Set the incoming direction in shade space and perform shade space to world space transformations:
		materialBXDFSetIncoming(incomingShadeSpaceX, incomingShadeSpaceY, incomingShadeSpaceZ);
		materialBXDFEnd();
		
		
		
		/*
		 * Material:
		 */
		
		final float incomingWorldSpaceX = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float incomingWorldSpaceY = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float incomingWorldSpaceZ = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
		final float normalWorldSpaceX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
		final float normalWorldSpaceY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
		final float normalWorldSpaceZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
		
		final float incomingWorldSpaceDotNormalWorldSpace = vector3FDotProduct(incomingWorldSpaceX, incomingWorldSpaceY, incomingWorldSpaceZ, normalWorldSpaceX, normalWorldSpaceY, normalWorldSpaceZ);
		final float incomingWorldSpaceDotNormalWorldSpaceAbs = abs(incomingWorldSpaceDotNormalWorldSpace);
		
		final float colorR = resultR * (incomingWorldSpaceDotNormalWorldSpaceAbs / probabilityDensityFunctionValue);
		final float colorG = resultG * (incomingWorldSpaceDotNormalWorldSpaceAbs / probabilityDensityFunctionValue);
		final float colorB = resultB * (incomingWorldSpaceDotNormalWorldSpaceAbs / probabilityDensityFunctionValue);
		
		color3FLHSSet(colorR, colorG, colorB);
		
		
		
		return true;
	}
	
//	TODO: Add Javadocs!
	protected final boolean materialSampleDistributionFunctionMatteSmallPTMaterial() {
		final int primitiveIndex = (int)(this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX]);
		final int primitiveArrayOffset = primitiveIndex * Primitive.ARRAY_LENGTH;
		final int materialOffset = this.primitiveArray[primitiveArrayOffset + Primitive.ARRAY_OFFSET_MATERIAL_OFFSET];
		final int textureKDID = this.materialMatteSmallPTMaterialArray[materialOffset + MatteSmallPTMaterial.ARRAY_OFFSET_TEXTURE_K_D_ID];
		final int textureKDOffset = this.materialMatteSmallPTMaterialArray[materialOffset + MatteSmallPTMaterial.ARRAY_OFFSET_TEXTURE_K_D_OFFSET];
		
		textureEvaluate(textureKDID, textureKDOffset);
		
		final float colorKDR = color3FLHSGetComponent1();
		final float colorKDG = color3FLHSGetComponent2();
		final float colorKDB = color3FLHSGetComponent3();
		
		final float directionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float directionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float directionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
		final float surfaceNormalX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
		final float surfaceNormalY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
		final float surfaceNormalZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
		
		vector3FSetFaceForwardNegated(surfaceNormalX, surfaceNormalY, surfaceNormalZ, directionX, directionY, directionZ);
		vector3FSetDiffuseReflection(vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3(), random(), random());
		
		color3FLHSSet(colorKDR, colorKDG, colorKDB);
		
		return true;
	}
	
//	TODO: Add Javadocs!
	protected final boolean materialSampleDistributionFunctionMetalRayitoMaterial() {
		/*
		 * Material:
		 */
		
//		Retrieve indices and offsets:
		final int primitiveIndex = (int)(this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX]);
		final int primitiveArrayOffset = primitiveIndex * Primitive.ARRAY_LENGTH;
		final int materialOffset = this.primitiveArray[primitiveArrayOffset + Primitive.ARRAY_OFFSET_MATERIAL_OFFSET];
		final int textureKRID = this.materialMetalRayitoMaterialArray[materialOffset + MetalRayitoMaterial.ARRAY_OFFSET_TEXTURE_K_R_ID];
		final int textureKROffset = this.materialMetalRayitoMaterialArray[materialOffset + MetalRayitoMaterial.ARRAY_OFFSET_TEXTURE_K_R_OFFSET];
		final int textureRoughnessID = this.materialMetalRayitoMaterialArray[materialOffset + MetalRayitoMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS_ID];
		final int textureRoughnessOffset = this.materialMetalRayitoMaterialArray[materialOffset + MetalRayitoMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS_OFFSET];
		
//		Evaluate the KR texture:
		textureEvaluate(textureKRID, textureKROffset);
		
//		Retrieve the color from the KR texture:
		final float colorKRR = color3FLHSGetComponent1();
		final float colorKRG = color3FLHSGetComponent2();
		final float colorKRB = color3FLHSGetComponent3();
		
//		Evaluate the Roughness texture:
		textureEvaluate(textureRoughnessID, textureRoughnessOffset);
		
//		Retrieve the color from the Roughness texture:
		final float colorRoughnessR = color3FLHSGetComponent1();
		final float colorRoughnessG = color3FLHSGetComponent2();
		final float colorRoughnessB = color3FLHSGetComponent3();
		
//		Compute the roughness and exponent:
		final float roughness = (colorRoughnessR + colorRoughnessG + colorRoughnessB) / 3.0F;
		final float exponent = 1.0F / (roughness * roughness);
		
		
		
		/*
		 * BSDF:
		 */
		
//		Perform world space to shade space transformations:
		materialBXDFBegin();
		
		
		
		/*
		 * BXDF:
		 */
		
//		Retrieve the normal in shade space:
		final float normalShadeSpaceX = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL + 0];
		final float normalShadeSpaceY = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL + 1];
		final float normalShadeSpaceZ = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL + 2];
		
//		Retrieve the outgoing direction in shade space:
		final float outgoingShadeSpaceX = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING + 0];
		final float outgoingShadeSpaceY = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING + 1];
		final float outgoingShadeSpaceZ = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING + 2];
		
//		Compute the dot product between the normal in shade space and the outgoing direction in shade space:
		final float normalShadeSpaceDotOutgoingShadeSpace = vector3FDotProduct(normalShadeSpaceX, normalShadeSpaceY, normalShadeSpaceZ, outgoingShadeSpaceX, outgoingShadeSpaceY, outgoingShadeSpaceZ);
		
//		Sample the hemisphere in local space using a power-cosine distribution:
		vector3FSetSampleHemispherePowerCosineDistribution(random(), random(), exponent);
		
//		Retrieve the half vector in local space:
		final float halfLocalSpaceX = normalShadeSpaceDotOutgoingShadeSpace < 0.0F ? -super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] : super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float halfLocalSpaceY = normalShadeSpaceDotOutgoingShadeSpace < 0.0F ? -super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] : super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float halfLocalSpaceZ = normalShadeSpaceDotOutgoingShadeSpace < 0.0F ? -super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] : super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
//		Compute the dot product between the outgoing direction in shade space and the half vector in local space:
		final float outgoingShadeSpaceDotHalfLocalSpace = vector3FDotProduct(outgoingShadeSpaceX, outgoingShadeSpaceY, outgoingShadeSpaceZ, halfLocalSpaceX, halfLocalSpaceY, halfLocalSpaceZ);
		
//		Compute the incoming direction in shade space:
		final float incomingShadeSpaceX = outgoingShadeSpaceX - halfLocalSpaceX * 2.0F * outgoingShadeSpaceDotHalfLocalSpace;
		final float incomingShadeSpaceY = outgoingShadeSpaceY - halfLocalSpaceY * 2.0F * outgoingShadeSpaceDotHalfLocalSpace;
		final float incomingShadeSpaceZ = outgoingShadeSpaceZ - halfLocalSpaceZ * 2.0F * outgoingShadeSpaceDotHalfLocalSpace;
		
//		Compute the half vector in shade space:
		vector3FSetHalf(outgoingShadeSpaceX, outgoingShadeSpaceY, outgoingShadeSpaceZ, normalShadeSpaceX, normalShadeSpaceY, normalShadeSpaceZ, incomingShadeSpaceX, incomingShadeSpaceY, incomingShadeSpaceZ);
		
//		Retrieve the half vector in shade space:
		final float halfShadeSpaceX = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float halfShadeSpaceY = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float halfShadeSpaceZ = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
//		Compute the dot product between the normal in shade space and the half vector in shade space:
		final float normalShadeSpaceDotHalfShadeSpace = vector3FDotProduct(normalShadeSpaceX, normalShadeSpaceY, normalShadeSpaceZ, halfShadeSpaceX, halfShadeSpaceY, halfShadeSpaceZ);
		
//		Compute the dot product between the normal in shade space and the incoming direction in shade space and its absolute representation:
		final float normalShadeSpaceDotIncomingShadeSpace = vector3FDotProduct(normalShadeSpaceX, normalShadeSpaceY, normalShadeSpaceZ, incomingShadeSpaceX, incomingShadeSpaceY, incomingShadeSpaceZ);
		
//		Compute the dot product between the outgoing direction in shade space and the half vector in shade space:
		final float outgoingShadeSpaceDotHalfShadeSpace = vector3FDotProduct(outgoingShadeSpaceX, outgoingShadeSpaceY, outgoingShadeSpaceZ, halfShadeSpaceX, halfShadeSpaceY, halfShadeSpaceZ);
		
//		Check that the dot products are opposite:
		if(normalShadeSpaceDotIncomingShadeSpace > 0.0F && normalShadeSpaceDotOutgoingShadeSpace > 0.0F || normalShadeSpaceDotIncomingShadeSpace < 0.0F && normalShadeSpaceDotOutgoingShadeSpace < 0.0F) {
			return false;
		}
		
//		Compute the probability density function (PDF) value:
		final float probabilityDensityFunctionValue = (exponent + 1.0F) * pow(abs(normalShadeSpaceDotHalfShadeSpace), exponent) / (PI * 8.0F * abs(outgoingShadeSpaceDotHalfShadeSpace));
		
		if(probabilityDensityFunctionValue <= 0.0F) {
			return false;
		}
		
		final float d = (exponent + 1.0F) * pow(abs(normalShadeSpaceDotHalfShadeSpace), exponent) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float f = fresnelDielectricSchlick(outgoingShadeSpaceDotHalfShadeSpace, 1.0F);
		final float g = 4.0F * abs(normalShadeSpaceDotOutgoingShadeSpace + -normalShadeSpaceDotIncomingShadeSpace - normalShadeSpaceDotOutgoingShadeSpace * -normalShadeSpaceDotIncomingShadeSpace);
		
//		Compute the result:
		final float resultR = colorKRR * d * f / g;
		final float resultG = colorKRG * d * f / g;
		final float resultB = colorKRB * d * f / g;
		
		
		
		/*
		 * BSDF:
		 */
		
//		Set the incoming direction in shade space and perform shade space to world space transformations:
		materialBXDFSetIncoming(incomingShadeSpaceX, incomingShadeSpaceY, incomingShadeSpaceZ);
		materialBXDFEnd();
		
		
		
		/*
		 * Material:
		 */
		
		final float incomingWorldSpaceX = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float incomingWorldSpaceY = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float incomingWorldSpaceZ = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
		final float normalWorldSpaceX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
		final float normalWorldSpaceY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
		final float normalWorldSpaceZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
		
		final float incomingWorldSpaceDotNormalWorldSpace = vector3FDotProduct(incomingWorldSpaceX, incomingWorldSpaceY, incomingWorldSpaceZ, normalWorldSpaceX, normalWorldSpaceY, normalWorldSpaceZ);
		final float incomingWorldSpaceDotNormalWorldSpaceAbs = abs(incomingWorldSpaceDotNormalWorldSpace);
		
		final float colorR = resultR * (incomingWorldSpaceDotNormalWorldSpaceAbs / probabilityDensityFunctionValue);
		final float colorG = resultG * (incomingWorldSpaceDotNormalWorldSpaceAbs / probabilityDensityFunctionValue);
		final float colorB = resultB * (incomingWorldSpaceDotNormalWorldSpaceAbs / probabilityDensityFunctionValue);
		
		color3FLHSSet(colorR, colorG, colorB);
		
		
		
		return true;
	}
	
//	TODO: Add Javadocs!
	protected final boolean materialSampleDistributionFunctionMetalSmallPTMaterial() {
		final int primitiveIndex = (int)(this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX]);
		final int primitiveArrayOffset = primitiveIndex * Primitive.ARRAY_LENGTH;
		final int materialOffset = this.primitiveArray[primitiveArrayOffset + Primitive.ARRAY_OFFSET_MATERIAL_OFFSET];
		final int textureKRID = this.materialMetalSmallPTMaterialArray[materialOffset + MetalSmallPTMaterial.ARRAY_OFFSET_TEXTURE_K_R_ID];
		final int textureKROffset = this.materialMetalSmallPTMaterialArray[materialOffset + MetalSmallPTMaterial.ARRAY_OFFSET_TEXTURE_K_R_OFFSET];
		final int textureRoughnessID = this.materialMetalSmallPTMaterialArray[materialOffset + MetalSmallPTMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS_ID];
		final int textureRoughnessOffset = this.materialMetalSmallPTMaterialArray[materialOffset + MetalSmallPTMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS_OFFSET];
		
		textureEvaluate(textureKRID, textureKROffset);
		
		final float colorKRR = color3FLHSGetComponent1();
		final float colorKRG = color3FLHSGetComponent2();
		final float colorKRB = color3FLHSGetComponent3();
		
		textureEvaluate(textureRoughnessID, textureRoughnessOffset);
		
		final float colorRoughnessR = color3FLHSGetComponent1();
		final float colorRoughnessG = color3FLHSGetComponent2();
		final float colorRoughnessB = color3FLHSGetComponent3();
		
		final float roughness = (colorRoughnessR + colorRoughnessG + colorRoughnessB) / 3.0F;
		final float exponent = 1.0F / (roughness * roughness);
		
		final float directionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float directionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float directionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
		final float surfaceNormalX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
		final float surfaceNormalY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
		final float surfaceNormalZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
		
		vector3FSetGlossyReflection(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ, true, random(), random(), exponent);
		
		color3FLHSSet(colorKRR, colorKRG, colorKRB);
		
		return true;
	}
	
//	TODO: Add Javadocs!
	protected final boolean materialSampleDistributionFunctionMirrorRayitoMaterial() {
		return false;
	}
	
//	TODO: Add Javadocs!
	protected final boolean materialSampleDistributionFunctionMirrorSmallPTMaterial() {
		final int primitiveIndex = (int)(this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX]);
		final int primitiveArrayOffset = primitiveIndex * Primitive.ARRAY_LENGTH;
		final int materialOffset = this.primitiveArray[primitiveArrayOffset + Primitive.ARRAY_OFFSET_MATERIAL_OFFSET];
		final int textureKRID = this.materialMirrorSmallPTMaterialArray[materialOffset + MirrorSmallPTMaterial.ARRAY_OFFSET_TEXTURE_K_R_ID];
		final int textureKROffset = this.materialMirrorSmallPTMaterialArray[materialOffset + MirrorSmallPTMaterial.ARRAY_OFFSET_TEXTURE_K_R_OFFSET];
		
		textureEvaluate(textureKRID, textureKROffset);
		
		final float colorKRR = color3FLHSGetComponent1();
		final float colorKRG = color3FLHSGetComponent2();
		final float colorKRB = color3FLHSGetComponent3();
		
		final float directionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float directionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float directionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
		final float surfaceNormalX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
		final float surfaceNormalY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
		final float surfaceNormalZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
		
		vector3FSetSpecularReflection(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ, true);
		
		color3FLHSSet(colorKRR, colorKRG, colorKRB);
		
		return true;
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
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0] = originX;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1] = originY;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2] = originZ;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0] = directionNormalizedX;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1] = directionNormalizedY;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2] = directionNormalizedZ;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MINIMUM] = DEFAULT_T_MINIMUM;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM] = DEFAULT_T_MAXIMUM;
		
//		Fill in the pixel array:
		this.pixelArray[pixelArrayOffset + 0] = pixelX;
		this.pixelArray[pixelArrayOffset + 1] = pixelY;
		
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
	protected final float intersectionTBoundingVolume3FAxisAlignedBoundingBox3F(final int boundingVolume3FAxisAlignedBoundingBox3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables:
		final float rayOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float rayOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float rayOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		final float rayDirectionReciprocalX = 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionReciprocalY = 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionReciprocalZ = 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
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
	protected final float intersectionTBoundingVolume3FBoundingSphere3F(final int boundingVolume3FBoundingSphere3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables:
		final float rayOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float rayOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float rayOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		final float rayDirectionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
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
	 * Returns the parametric T value for the closest shape in world space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @return the parametric T value for the closest shape in world space, or {@code 0.0F} if no intersection was found
	 */
	protected final float intersectionTShape3F() {
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
			
			final float tMinimumWorldSpace = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MINIMUM];
			final float tMaximumWorldSpace = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM];
			
			boolean isIntersectingBoundingVolume = false;
			
//			TODO: Find out what causes the order of the if-statements to fail. If InfiniteBoundingVolume3F.ID is checked in the last if-statement, the plane will disappear.
			if(boundingVolumeID == InfiniteBoundingVolume3F.ID) {
				isIntersectingBoundingVolume = true;
			} else if(boundingVolumeID == AxisAlignedBoundingBox3F.ID) {
				isIntersectingBoundingVolume = containsOrIntersectsBoundingVolume3FAxisAlignedBoundingBox3F(boundingVolumeOffset, tMinimumWorldSpace, tMaximumWorldSpace);
			} else if(boundingVolumeID == BoundingSphere3F.ID) {
				isIntersectingBoundingVolume = containsOrIntersectsBoundingVolume3FBoundingSphere3F(boundingVolumeOffset, tMinimumWorldSpace, tMaximumWorldSpace);
			}
			
			if(isIntersectingBoundingVolume) {
				ray3FSetMatrix44FTransformWorldToObject(index);
				
				float tObjectSpace = 0.0F;
				
				final float tMinimumObjectSpace = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MINIMUM];
				final float tMaximumObjectSpace = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM];
				
				if(shapeID == Plane3F.ID) {
					tObjectSpace = intersectionTShape3FPlane3F(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == RectangularCuboid3F.ID) {
					tObjectSpace = intersectionTShape3FRectangularCuboid3F(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Sphere3F.ID) {
					tObjectSpace = intersectionTShape3FSphere3F(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Torus3F.ID) {
					tObjectSpace = intersectionTShape3FTorus3F(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Triangle3F.ID) {
					tObjectSpace = intersectionTShape3FTriangle3F(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == TriangleMesh3F.ID) {
					tObjectSpace = intersectionTShape3FTriangleMesh3F(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				}
				
				if(tObjectSpace > tMinimumObjectSpace && tObjectSpace < tMaximumObjectSpace) {
					this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM] = tObjectSpace;
					
					hasFoundIntersection = true;
				}
				
				ray3FSetMatrix44FTransformObjectToWorld(index);
			}
		}
		
		return hasFoundIntersection ? this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM] : 0.0F;
	}
	
	/**
	 * Returns the parametric T value for a given plane in object space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @param shape3FPlane3FArrayOffset the offset for the plane in {@link #shape3FPlane3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return the parametric T value for a given plane in object space, or {@code 0.0F} if no intersection was found
	 */
	protected final float intersectionTShape3FPlane3F(final int shape3FPlane3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float rayOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float rayOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		final float rayDirectionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
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
	protected final float intersectionTShape3FRectangularCuboid3F(final int shape3FRectangularCuboid3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables:
		final float rayOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float rayOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float rayOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		final float rayDirectionReciprocalX = 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionReciprocalY = 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionReciprocalZ = 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
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
	protected final float intersectionTShape3FSphere3F(final int shape3FSphere3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables:
		final float rayOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float rayOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float rayOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		final float rayDirectionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
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
	protected final float intersectionTShape3FTorus3F(final int shape3FTorus3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables:
		final float rayOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float rayOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float rayOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		final float rayDirectionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
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
	protected final float intersectionTShape3FTriangle3F(final int shape3FTriangle3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float rayOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float rayOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		final float rayDirectionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
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
		
//		Compute the direction from 'triangleAPosition' to 'triangleBPosition', denoted by 'direction0' in the comments:
		final float direction0X = triangleBPositionX - triangleAPositionX;
		final float direction0Y = triangleBPositionY - triangleAPositionY;
		final float direction0Z = triangleBPositionZ - triangleAPositionZ;
		
//		Compute the direction from 'triangleAPosition' to 'triangleCPosition', denoted by 'direction1' in the comments:
		final float direction1X = triangleCPositionX - triangleAPositionX;
		final float direction1Y = triangleCPositionY - triangleAPositionY;
		final float direction1Z = triangleCPositionZ - triangleAPositionZ;
		
//		Compute the cross product between 'rayDirection' and 'direction1', denoted by 'direction2' in the comments:
		final float direction2X = rayDirectionY * direction1Z - rayDirectionZ * direction1Y;
		final float direction2Y = rayDirectionZ * direction1X - rayDirectionX * direction1Z;
		final float direction2Z = rayDirectionX * direction1Y - rayDirectionY * direction1X;
		
//		Compute the determinant, which is the dot product between 'direction0' and 'direction2':
		final float determinant = direction0X * direction2X + direction0Y * direction2Y + direction0Z * direction2Z;
		
//		Check if the determinant is close to 0.0 and, if that is the case, return a miss:
		if(determinant >= -0.0001F && determinant <= +0.0001F) {
			return 0.0F;
		}
		
//		Compute the direction from 'triangleAPosition' to 'rayOrigin', denoted by 'direction3' in the comments:
		final float direction3X = rayOriginX - triangleAPositionX;
		final float direction3Y = rayOriginY - triangleAPositionY;
		final float direction3Z = rayOriginZ - triangleAPositionZ;
		
//		Compute the reciprocal (or inverse) of the determinant, such that multiplications can be used instead of divisions:
		final float determinantReciprocal = 1.0F / determinant;
		
//		Compute the Barycentric U-coordinate as the dot product between 'direction3' and 'direction2' followed by a multiplication with the reciprocal (or inverse) determinant:
		final float barycentricU = (direction3X * direction2X + direction3Y * direction2Y + direction3Z * direction2Z) * determinantReciprocal;
		
//		Check that the Barycentric U-coordinate is in the interval [0.0, 1.0] and, if it is not, return a miss:
		if(barycentricU < 0.0F || barycentricU > 1.0F) {
			return 0.0F;
		}
		
//		Compute the cross product between 'direction3' and 'direction0', denoted by 'direction4' in the comments:
		final float direction4X = direction3Y * direction0Z - direction3Z * direction0Y;
		final float direction4Y = direction3Z * direction0X - direction3X * direction0Z;
		final float direction4Z = direction3X * direction0Y - direction3Y * direction0X;
		
//		Compute the Barycentric V-coordinate as the dot product between 'rayDirection' and 'direction4' followed by a multiplication with the reciprocal (or inverse) determinant:
		final float barycentricV = (rayDirectionX * direction4X + rayDirectionY * direction4Y + rayDirectionZ * direction4Z) * determinantReciprocal;
		
//		Check that the Barycentric V-coordinate is in the interval [0.0, 1.0] and, if it is not, return a miss:
		if(barycentricV < 0.0F || barycentricV > 1.0F) {
			return 0.0F;
		}
		
//		Check that the sum of the Barycentric U-coordinate and the Barycentric V-coordinate is in the interval [0.0, 1.0] and, if it is not, return a miss:
		if(barycentricU + barycentricV > 1.0F) {
			return 0.0F;
		}
		
//		Compute the intersection as the dot product between 'direction1' and 'direction4' followed by a multiplication with the reciprocal (or inverse) determinant:
		final float intersectionT = (direction1X * direction4X + direction1Y * direction4Y + direction1Z * direction4Z) * determinantReciprocal;
		
		if(intersectionT > rayTMinimum && intersectionT < rayTMaximum) {
			return intersectionT;
		}
		
		return 0.0F;
	}
	
	/**
	 * Returns the parametric T value for a given triangle mesh in object space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @param shape3FTriangleMesh3FArrayOffset the offset for the triangle mesh in {@link #shape3FTriangleMesh3FArray}
	 * @param rayTMinimum the minimum parametric T value
	 * @param rayTMaximum the maximum parametric T value
	 * @return the parametric T value for a given triangle mesh in object space, or {@code 0.0F} if no intersection was found
	 */
	protected final float intersectionTShape3FTriangleMesh3F(final int shape3FTriangleMesh3FArrayOffset, final float rayTMinimum, final float rayTMaximum) {
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
			
			final boolean isIntersectingBoundingVolume = containsOrIntersectsBoundingVolume3FAxisAlignedBoundingBox3F(boundingVolumeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
			
			if(isIntersectingBoundingVolume && id == TriangleMesh3F.ID_LEAF_B_V_H_NODE) {
				for(int i = 0; i < leftOffsetOrTriangleCount; i++) {
					final int triangleOffset = this.shape3FTriangleMesh3FArray[offset + TriangleMesh3F.ARRAY_OFFSET_LEFT_OFFSET_OR_TRIANGLE_COUNT + 1 + i];
					
					final float tObjectSpace = this.intersectionTShape3FTriangle3F(triangleOffset, tMinimumObjectSpace, tMaximumObjectSpace);
					
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
	 * Returns the {@code float[]} with the pixel samples.
	 * 
	 * @return the {@code float[]} with the pixel samples
	 */
	protected final float[] getPixelArray() {
		return getAndReturn(this.pixelArray);
	}
	
//	TODO: Add Javadocs!
	protected final void intersectionComputeShape3FPlane3F(final float t, final int primitiveIndex, final int shape3FPlane3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float rayOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float rayOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		final float rayDirectionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
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
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0] = orthonormalBasisGUNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1] = orthonormalBasisGUNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2] = orthonormalBasisGUNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0] = orthonormalBasisGVNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1] = orthonormalBasisGVNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2] = orthonormalBasisGVNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0] = orthonormalBasisGWNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1] = orthonormalBasisGWNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2] = orthonormalBasisGWNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = orthonormalBasisGUNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = orthonormalBasisGUNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = orthonormalBasisGUNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = orthonormalBasisGVNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = orthonormalBasisGVNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = orthonormalBasisGVNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = orthonormalBasisGWNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = orthonormalBasisGWNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = orthonormalBasisGWNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX] = primitiveIndex;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0] = surfaceIntersectionPointX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1] = surfaceIntersectionPointY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2] = surfaceIntersectionPointZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0] = textureCoordinatesU;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1] = textureCoordinatesV;
	}
	
//	TODO: Add Javadocs!
	protected final void intersectionComputeShape3FRectangularCuboid3F(final float t, final int primitiveIndex, final int shape3FRectangularCuboid3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float rayOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float rayOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		final float rayDirectionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
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
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0] = orthonormalBasisGUNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1] = orthonormalBasisGUNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2] = orthonormalBasisGUNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0] = orthonormalBasisGVNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1] = orthonormalBasisGVNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2] = orthonormalBasisGVNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0] = orthonormalBasisGWNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1] = orthonormalBasisGWNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2] = orthonormalBasisGWNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = orthonormalBasisGUNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = orthonormalBasisGUNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = orthonormalBasisGUNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = orthonormalBasisGVNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = orthonormalBasisGVNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = orthonormalBasisGVNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = orthonormalBasisGWNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = orthonormalBasisGWNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = orthonormalBasisGWNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX] = primitiveIndex;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0] = surfaceIntersectionPointX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1] = surfaceIntersectionPointY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2] = surfaceIntersectionPointZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0] = textureCoordinatesU;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1] = textureCoordinatesV;
	}
	
//	TODO: Add Javadocs!
	protected final void intersectionComputeShape3FSphere3F(final float t, final int primitiveIndex, final int shape3FSphere3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float rayOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float rayOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		final float rayDirectionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
//		Retrieve the sphere variables:
		final float sphereCenterX = this.shape3FSphere3FArray[shape3FSphere3FArrayOffset + Sphere3F.ARRAY_OFFSET_CENTER + 0];
		final float sphereCenterY = this.shape3FSphere3FArray[shape3FSphere3FArrayOffset + Sphere3F.ARRAY_OFFSET_CENTER + 1];
		final float sphereCenterZ = this.shape3FSphere3FArray[shape3FSphere3FArrayOffset + Sphere3F.ARRAY_OFFSET_CENTER + 2];
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute the W-direction (surface normal) of the geometric orthonormal basis:
		final float orthonormalBasisGWX = surfaceIntersectionPointX - sphereCenterX;
		final float orthonormalBasisGWY = surfaceIntersectionPointY - sphereCenterY;
		final float orthonormalBasisGWZ = surfaceIntersectionPointZ - sphereCenterZ;
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
		final float textureCoordinatesU = 0.5F + atan2(orthonormalBasisGWNormalizedZ, orthonormalBasisGWNormalizedX) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float textureCoordinatesV = 0.5F - asinpi(orthonormalBasisGWNormalizedY);
		
//		Update the intersection array:
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0] = orthonormalBasisGUNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1] = orthonormalBasisGUNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2] = orthonormalBasisGUNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0] = orthonormalBasisGVNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1] = orthonormalBasisGVNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2] = orthonormalBasisGVNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0] = orthonormalBasisGWNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1] = orthonormalBasisGWNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2] = orthonormalBasisGWNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = orthonormalBasisGUNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = orthonormalBasisGUNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = orthonormalBasisGUNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = orthonormalBasisGVNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = orthonormalBasisGVNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = orthonormalBasisGVNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = orthonormalBasisGWNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = orthonormalBasisGWNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = orthonormalBasisGWNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX] = primitiveIndex;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0] = surfaceIntersectionPointX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1] = surfaceIntersectionPointY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2] = surfaceIntersectionPointZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0] = textureCoordinatesU;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1] = textureCoordinatesV;
	}
	
//	TODO: Add Javadocs!
	protected final void intersectionComputeShape3FTorus3F(final float t, final int primitiveIndex, final int shape3FTorus3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float rayOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float rayOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		final float rayDirectionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
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
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0] = orthonormalBasisGUNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1] = orthonormalBasisGUNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2] = orthonormalBasisGUNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0] = orthonormalBasisGVNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1] = orthonormalBasisGVNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2] = orthonormalBasisGVNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0] = orthonormalBasisGWNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1] = orthonormalBasisGWNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2] = orthonormalBasisGWNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = orthonormalBasisGUNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = orthonormalBasisGUNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = orthonormalBasisGUNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = orthonormalBasisGVNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = orthonormalBasisGVNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = orthonormalBasisGVNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = orthonormalBasisGWNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = orthonormalBasisGWNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = orthonormalBasisGWNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX] = primitiveIndex;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0] = surfaceIntersectionPointX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1] = surfaceIntersectionPointY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2] = surfaceIntersectionPointZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0] = textureCoordinatesU;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1] = textureCoordinatesV;
	}
	
//	TODO: Add Javadocs!
	protected final void intersectionComputeShape3FTriangle3F(final float t, final int primitiveIndex, final int shape3FTriangle3FArrayOffset) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float rayOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float rayOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		final float rayDirectionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
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
		final float triangleAOrthonormalBasisVX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_A_ORTHONORMAL_BASIS_V + 0];
		final float triangleAOrthonormalBasisVY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_A_ORTHONORMAL_BASIS_V + 1];
		final float triangleAOrthonormalBasisVZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_A_ORTHONORMAL_BASIS_V + 2];
		final float triangleBOrthonormalBasisVX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_B_ORTHONORMAL_BASIS_V + 0];
		final float triangleBOrthonormalBasisVY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_B_ORTHONORMAL_BASIS_V + 1];
		final float triangleBOrthonormalBasisVZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_B_ORTHONORMAL_BASIS_V + 2];
		final float triangleCOrthonormalBasisVX = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_C_ORTHONORMAL_BASIS_V + 0];
		final float triangleCOrthonormalBasisVY = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_C_ORTHONORMAL_BASIS_V + 1];
		final float triangleCOrthonormalBasisVZ = this.shape3FTriangle3FArray[shape3FTriangle3FArrayOffset + Triangle3F.ARRAY_OFFSET_C_ORTHONORMAL_BASIS_V + 2];
		
//		Compute the direction from 'triangleAPosition' to 'triangleBPosition', denoted by 'direction0' in the comments:
		final float direction0X = triangleBPositionX - triangleAPositionX;
		final float direction0Y = triangleBPositionY - triangleAPositionY;
		final float direction0Z = triangleBPositionZ - triangleAPositionZ;
		final float direction0LengthReciprocal = vector3FLengthReciprocal(direction0X, direction0Y, direction0Z);
		final float direction0NormalizedX = direction0X * direction0LengthReciprocal;
		final float direction0NormalizedY = direction0Y * direction0LengthReciprocal;
		final float direction0NormalizedZ = direction0Z * direction0LengthReciprocal;
		
//		Compute the direction from 'triangleAPosition' to 'triangleCPosition', denoted by 'direction1' in the comments:
		final float direction1X = triangleCPositionX - triangleAPositionX;
		final float direction1Y = triangleCPositionY - triangleAPositionY;
		final float direction1Z = triangleCPositionZ - triangleAPositionZ;
		
//		Compute the cross product between 'rayDirection' and 'direction1', denoted by 'direction2' in the comments:
		final float direction2X = rayDirectionY * direction1Z - rayDirectionZ * direction1Y;
		final float direction2Y = rayDirectionZ * direction1X - rayDirectionX * direction1Z;
		final float direction2Z = rayDirectionX * direction1Y - rayDirectionY * direction1X;
		
//		Compute the determinant, which is the dot product between 'direction0' and 'direction2':
		final float determinant = direction0X * direction2X + direction0Y * direction2Y + direction0Z * direction2Z;
		
//		Check if the determinant is close to 0.0 and, if that is the case, return a miss:
		if(determinant >= -0.0001F && determinant <= +0.0001F) {
			return;
		}
		
//		Compute the direction from 'triangleAPosition' to 'rayOrigin', denoted by 'direction3' in the comments:
		final float direction3X = rayOriginX - triangleAPositionX;
		final float direction3Y = rayOriginY - triangleAPositionY;
		final float direction3Z = rayOriginZ - triangleAPositionZ;
		
//		Compute the reciprocal (or inverse) of the determinant, such that multiplications can be used instead of divisions:
		final float determinantReciprocal = 1.0F / determinant;
		
//		Compute the Barycentric U-coordinate as the dot product between 'direction3' and 'direction2' followed by a multiplication with the reciprocal (or inverse) determinant:
		final float barycentricU = (direction3X * direction2X + direction3Y * direction2Y + direction3Z * direction2Z) * determinantReciprocal;
		
//		Check that the Barycentric U-coordinate is in the interval [0.0, 1.0] and, if it is not, return a miss:
		if(barycentricU < 0.0F || barycentricU > 1.0F) {
			return;
		}
		
//		Compute the cross product between 'direction3' and 'direction0', denoted by 'direction4' in the comments:
		final float direction4X = direction3Y * direction0Z - direction3Z * direction0Y;
		final float direction4Y = direction3Z * direction0X - direction3X * direction0Z;
		final float direction4Z = direction3X * direction0Y - direction3Y * direction0X;
		
//		Compute the Barycentric V-coordinate as the dot product between 'rayDirection' and 'direction4' followed by a multiplication with the reciprocal (or inverse) determinant:
		final float barycentricV = (rayDirectionX * direction4X + rayDirectionY * direction4Y + rayDirectionZ * direction4Z) * determinantReciprocal;
		
//		Check that the Barycentric V-coordinate is in the interval [0.0, 1.0] and, if it is not, return a miss:
		if(barycentricV < 0.0F || barycentricV > 1.0F) {
			return;
		}
		
//		Check that the sum of the Barycentric U-coordinate and the Barycentric V-coordinate is in the interval [0.0, 1.0] and, if it is not, return a miss:
		if(barycentricU + barycentricV > 1.0F) {
			return;
		}
		
//		Compute the Barycentric W-coordinate:
		final float barycentricW = 1.0F - barycentricU - barycentricV;
		
//		Compute the surface intersection point:
		final float surfaceIntersectionPointX = rayOriginX + rayDirectionX * t;
		final float surfaceIntersectionPointY = rayOriginY + rayDirectionY * t;
		final float surfaceIntersectionPointZ = rayOriginZ + rayDirectionZ * t;
		
//		Compute the W-direction (surface normal) of the geometric orthonormal basis:
		final float orthonormalBasisGWX = direction0Y * direction1Z - direction0Z * direction1Y;
		final float orthonormalBasisGWY = direction0Z * direction1X - direction0X * direction1Z;
		final float orthonormalBasisGWZ = direction0X * direction1Y - direction0Y * direction1X;
		final float orthonormalBasisGWLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisGWX, orthonormalBasisGWY, orthonormalBasisGWZ);
		final float orthonormalBasisGWNormalizedX = orthonormalBasisGWX * orthonormalBasisGWLengthReciprocal;
		final float orthonormalBasisGWNormalizedY = orthonormalBasisGWY * orthonormalBasisGWLengthReciprocal;
		final float orthonormalBasisGWNormalizedZ = orthonormalBasisGWZ * orthonormalBasisGWLengthReciprocal;
		
//		Compute the U-direction of the geometric orthonormal basis:
		final float orthonormalBasisGUX = direction0NormalizedY * orthonormalBasisGWNormalizedZ - direction0NormalizedZ * orthonormalBasisGWNormalizedY;
		final float orthonormalBasisGUY = direction0NormalizedZ * orthonormalBasisGWNormalizedX - direction0NormalizedX * orthonormalBasisGWNormalizedZ;
		final float orthonormalBasisGUZ = direction0NormalizedX * orthonormalBasisGWNormalizedY - direction0NormalizedY * orthonormalBasisGWNormalizedX;
		final float orthonormalBasisGULengthReciprocal = vector3FLengthReciprocal(orthonormalBasisGUX, orthonormalBasisGUY, orthonormalBasisGUZ);
		final float orthonormalBasisGUNormalizedX = orthonormalBasisGUX * orthonormalBasisGULengthReciprocal;
		final float orthonormalBasisGUNormalizedY = orthonormalBasisGUY * orthonormalBasisGULengthReciprocal;
		final float orthonormalBasisGUNormalizedZ = orthonormalBasisGUZ * orthonormalBasisGULengthReciprocal;
		
//		Compute the V-direction of the geometric orthonormal basis:
		final float orthonormalBasisGVNormalizedX = orthonormalBasisGWNormalizedY * orthonormalBasisGUNormalizedZ - orthonormalBasisGWNormalizedZ * orthonormalBasisGUNormalizedY;
		final float orthonormalBasisGVNormalizedY = orthonormalBasisGWNormalizedZ * orthonormalBasisGUNormalizedX - orthonormalBasisGWNormalizedX * orthonormalBasisGUNormalizedZ;
		final float orthonormalBasisGVNormalizedZ = orthonormalBasisGWNormalizedX * orthonormalBasisGUNormalizedY - orthonormalBasisGWNormalizedY * orthonormalBasisGUNormalizedX;
		
//		Compute the W-direction of the shading orthonormal basis:
		final float orthonormalBasisSWX = triangleAOrthonormalBasisWX * barycentricW + triangleBOrthonormalBasisWX * barycentricU + triangleCOrthonormalBasisWX * barycentricV;
		final float orthonormalBasisSWY = triangleAOrthonormalBasisWY * barycentricW + triangleBOrthonormalBasisWY * barycentricU + triangleCOrthonormalBasisWY * barycentricV;
		final float orthonormalBasisSWZ = triangleAOrthonormalBasisWZ * barycentricW + triangleBOrthonormalBasisWZ * barycentricU + triangleCOrthonormalBasisWZ * barycentricV;
		final float orthonormalBasisSWLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisSWX, orthonormalBasisSWY, orthonormalBasisSWZ);
		final float orthonormalBasisSWNormalizedX = orthonormalBasisSWX * orthonormalBasisSWLengthReciprocal;
		final float orthonormalBasisSWNormalizedY = orthonormalBasisSWY * orthonormalBasisSWLengthReciprocal;
		final float orthonormalBasisSWNormalizedZ = orthonormalBasisSWZ * orthonormalBasisSWLengthReciprocal;
		
//		Compute a vector that will be used when constructing the U-direction of the shading orthonormal basis:
		final float vX = triangleAOrthonormalBasisVX * barycentricW + triangleBOrthonormalBasisVX * barycentricU + triangleCOrthonormalBasisVX * barycentricV;
		final float vY = triangleAOrthonormalBasisVY * barycentricW + triangleBOrthonormalBasisVY * barycentricU + triangleCOrthonormalBasisVY * barycentricV;
		final float vZ = triangleAOrthonormalBasisVZ * barycentricW + triangleBOrthonormalBasisVZ * barycentricU + triangleCOrthonormalBasisVZ * barycentricV;
		final float vLengthReciprocal = vector3FLengthReciprocal(vX, vY, vZ);
		final float vNormalizedX = vX * vLengthReciprocal;
		final float vNormalizedY = vY * vLengthReciprocal;
		final float vNormalizedZ = vZ * vLengthReciprocal;
		
//		Compute the U-direction of the shading orthonormal basis:
		final float orthonormalBasisSUX = vNormalizedY * orthonormalBasisSWNormalizedZ - vNormalizedZ * orthonormalBasisSWNormalizedY;
		final float orthonormalBasisSUY = vNormalizedZ * orthonormalBasisSWNormalizedX - vNormalizedX * orthonormalBasisSWNormalizedZ;
		final float orthonormalBasisSUZ = vNormalizedX * orthonormalBasisSWNormalizedY - vNormalizedY * orthonormalBasisSWNormalizedX;
		final float orthonormalBasisSULengthReciprocal = vector3FLengthReciprocal(orthonormalBasisSUX, orthonormalBasisSUY, orthonormalBasisSUZ);
		final float orthonormalBasisSUNormalizedX = orthonormalBasisSUX * orthonormalBasisSULengthReciprocal;
		final float orthonormalBasisSUNormalizedY = orthonormalBasisSUY * orthonormalBasisSULengthReciprocal;
		final float orthonormalBasisSUNormalizedZ = orthonormalBasisSUZ * orthonormalBasisSULengthReciprocal;
		
//		Compute the V-direction of the shading orthonormal basis:
		final float orthonormalBasisSVNormalizedX = orthonormalBasisSWNormalizedY * orthonormalBasisSUNormalizedZ - orthonormalBasisSWNormalizedZ * orthonormalBasisSUNormalizedY;
		final float orthonormalBasisSVNormalizedY = orthonormalBasisSWNormalizedZ * orthonormalBasisSUNormalizedX - orthonormalBasisSWNormalizedX * orthonormalBasisSUNormalizedZ;
		final float orthonormalBasisSVNormalizedZ = orthonormalBasisSWNormalizedX * orthonormalBasisSUNormalizedY - orthonormalBasisSWNormalizedY * orthonormalBasisSUNormalizedX;
		
//		Compute the texture coordinates:
		final float textureCoordinatesU = triangleATextureCoordinatesU * barycentricW + triangleBTextureCoordinatesU * barycentricU + triangleCTextureCoordinatesU * barycentricV;
		final float textureCoordinatesV = triangleATextureCoordinatesV * barycentricW + triangleBTextureCoordinatesV * barycentricU + triangleCTextureCoordinatesV * barycentricV;
		
//		Update the intersection array:
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0] = orthonormalBasisGUNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1] = orthonormalBasisGUNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2] = orthonormalBasisGUNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0] = orthonormalBasisGVNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1] = orthonormalBasisGVNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2] = orthonormalBasisGVNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0] = orthonormalBasisGWNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1] = orthonormalBasisGWNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2] = orthonormalBasisGWNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = orthonormalBasisSUNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = orthonormalBasisSUNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = orthonormalBasisSUNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = orthonormalBasisSVNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = orthonormalBasisSVNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = orthonormalBasisSVNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = orthonormalBasisSWNormalizedX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = orthonormalBasisSWNormalizedY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = orthonormalBasisSWNormalizedZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX] = primitiveIndex;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0] = surfaceIntersectionPointX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1] = surfaceIntersectionPointY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2] = surfaceIntersectionPointZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0] = textureCoordinatesU;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1] = textureCoordinatesV;
	}
	
//	TODO: Add Javadocs!
	protected final void intersectionComputeShape3FTriangleMesh3F(final float t, final int primitiveIndex) {
		final int shape3FTriangle3FArrayOffset = this.shape3FTriangleMesh3FArrayToShape3FTriangle3FArray_$private$1[0];
		
		if(shape3FTriangle3FArrayOffset != -1) {
			intersectionComputeShape3FTriangle3F(t, primitiveIndex, shape3FTriangle3FArrayOffset);
		}
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
		final float oldOrthonormalBasisGUX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0];
		final float oldOrthonormalBasisGUY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1];
		final float oldOrthonormalBasisGUZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2];
		final float oldOrthonormalBasisGVX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0];
		final float oldOrthonormalBasisGVY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1];
		final float oldOrthonormalBasisGVZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2];
		final float oldOrthonormalBasisGWX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
		final float oldOrthonormalBasisGWY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
		final float oldOrthonormalBasisGWZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
		final float oldOrthonormalBasisSUX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0];
		final float oldOrthonormalBasisSUY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1];
		final float oldOrthonormalBasisSUZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2];
		final float oldOrthonormalBasisSVX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0];
		final float oldOrthonormalBasisSVY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1];
		final float oldOrthonormalBasisSVZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2];
		final float oldOrthonormalBasisSWX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
		final float oldOrthonormalBasisSWY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
		final float oldOrthonormalBasisSWZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
		final float oldSurfaceIntersectionPointX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0];
		final float oldSurfaceIntersectionPointY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1];
		final float oldSurfaceIntersectionPointZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2];
		
//		Transform the U-direction of the geometric orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisGUX, oldOrthonormalBasisGUY, oldOrthonormalBasisGUZ);
		
//		Retrieve the transformed U-direction of the geometric orthonormal basis:
		final float newOrthonormalBasisGUX = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float newOrthonormalBasisGUY = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float newOrthonormalBasisGUZ = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
//		Transform the V-direction of the geometric orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisGVX, oldOrthonormalBasisGVY, oldOrthonormalBasisGVZ);
		
//		Retrieve the transformed V-direction of the geometric orthonormal basis:
		final float newOrthonormalBasisGVX = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float newOrthonormalBasisGVY = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float newOrthonormalBasisGVZ = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
//		Transform the W-direction of the geometric orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisGWX, oldOrthonormalBasisGWY, oldOrthonormalBasisGWZ);
		
//		Retrieve the transformed W-direction of the geometric orthonormal basis:
		final float newOrthonormalBasisGWX = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float newOrthonormalBasisGWY = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float newOrthonormalBasisGWZ = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
//		Transform the U-direction of the shading orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisSUX, oldOrthonormalBasisSUY, oldOrthonormalBasisSUZ);
		
//		Retrieve the transformed U-direction of the shading orthonormal basis:
		final float newOrthonormalBasisSUX = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float newOrthonormalBasisSUY = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float newOrthonormalBasisSUZ = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
//		Transform the V-direction of the shading orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisSVX, oldOrthonormalBasisSVY, oldOrthonormalBasisSVZ);
		
//		Retrieve the transformed V-direction of the shading orthonormal basis:
		final float newOrthonormalBasisSVX = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float newOrthonormalBasisSVY = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float newOrthonormalBasisSVZ = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
//		Transform the W-direction of the shading orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisSWX, oldOrthonormalBasisSWY, oldOrthonormalBasisSWZ);
		
//		Retrieve the transformed W-direction of the shading orthonormal basis:
		final float newOrthonormalBasisSWX = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float newOrthonormalBasisSWY = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float newOrthonormalBasisSWZ = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
//		Transform the surface intersection point:
		point3FSetMatrix44FTransformAndDivide(matrixElement11, matrixElement12, matrixElement13, matrixElement14, matrixElement21, matrixElement22, matrixElement23, matrixElement24, matrixElement31, matrixElement32, matrixElement33, matrixElement34, matrixElement41, matrixElement42, matrixElement43, matrixElement44, oldSurfaceIntersectionPointX, oldSurfaceIntersectionPointY, oldSurfaceIntersectionPointZ);
		
//		Retrieve the transformed surface intersection point:
		final float newSurfaceIntersectionPointX = this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float newSurfaceIntersectionPointY = this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float newSurfaceIntersectionPointZ = this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_3];
		
//		Update the intersection array:
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0] = newOrthonormalBasisGUX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1] = newOrthonormalBasisGUY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2] = newOrthonormalBasisGUZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0] = newOrthonormalBasisGVX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1] = newOrthonormalBasisGVY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2] = newOrthonormalBasisGVZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0] = newOrthonormalBasisGWX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1] = newOrthonormalBasisGWY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2] = newOrthonormalBasisGWZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = newOrthonormalBasisSUX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = newOrthonormalBasisSUY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = newOrthonormalBasisSUZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = newOrthonormalBasisSVX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = newOrthonormalBasisSVY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = newOrthonormalBasisSVZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = newOrthonormalBasisSWX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = newOrthonormalBasisSWY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = newOrthonormalBasisSWZ;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0] = newSurfaceIntersectionPointX;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1] = newSurfaceIntersectionPointY;
		this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2] = newSurfaceIntersectionPointZ;
	}
	
//	TODO: Add Javadocs!
	protected final void intersectionTransformObjectToWorld(final int primitiveIndex) {
		intersectionTransform(primitiveIndex * Matrix44F.ARRAY_SIZE * 2, primitiveIndex * Matrix44F.ARRAY_SIZE * 2 + Matrix44F.ARRAY_SIZE);
	}
	
//	TODO: Add Javadocs!
	protected final void intersectionTransformWorldToObject(final int primitiveIndex) {
		intersectionTransform(primitiveIndex * Matrix44F.ARRAY_SIZE * 2 + Matrix44F.ARRAY_SIZE, primitiveIndex * Matrix44F.ARRAY_SIZE * 2);
	}
	
//	TODO: Add Javadocs!
	protected final void lightEvaluateRadianceEmittedAny() {
		lightEvaluateRadianceEmittedClear();
		lightEvaluateRadianceEmittedAnyLDRImageLight();
	}
	
//	TODO: Add Javadocs!
	protected final void lightEvaluateRadianceEmittedAnyLDRImageLight() {
		final int lightLDRImageLightCount = this.lightLDRImageLightCount;
		
		if(lightLDRImageLightCount > 0) {
			final int offset = min((int)(random() * lightLDRImageLightCount), lightLDRImageLightCount - 1);
			
			final float probabilityDensityFunctionValue = 1.0F / lightLDRImageLightCount;
			
			lightEvaluateRadianceEmittedOneLDRImageLight(offset, probabilityDensityFunctionValue);
		}
	}
	
//	TODO: Add Javadocs!
	protected final void lightEvaluateRadianceEmittedClear() {
		color3FLHSSet(0.0F, 0.0F, 0.0F);
	}
	
//	TODO: Add Javadocs!
	protected final void lightEvaluateRadianceEmittedOneLDRImageLight(final int offset, final float probabilityDensityFunctionValue) {
		final float rayDirectionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
		final float textureCoordinatesU = 0.5F + atan2(rayDirectionZ, rayDirectionX) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float textureCoordinatesV = 0.5F - asinpi(rayDirectionY);
		
		final float angleRadians = this.lightLDRImageLightArray[offset + LDRImageLight.ARRAY_OFFSET_ANGLE_RADIANS];
		final float angleRadiansCos = cos(angleRadians);
		final float angleRadiansSin = sin(angleRadians);
		
		final float scaleU = this.lightLDRImageLightArray[offset + LDRImageLight.ARRAY_OFFSET_SCALE + 0];
		final float scaleV = this.lightLDRImageLightArray[offset + LDRImageLight.ARRAY_OFFSET_SCALE + 1];
		
		final int resolutionX = (int)(this.lightLDRImageLightArray[offset + LDRImageLight.ARRAY_OFFSET_RESOLUTION_X]);
		final int resolutionY = (int)(this.lightLDRImageLightArray[offset + LDRImageLight.ARRAY_OFFSET_RESOLUTION_Y]);
		
		final float textureCoordinatesRotatedU = textureCoordinatesU * angleRadiansCos - textureCoordinatesV * angleRadiansSin;
		final float textureCoordinatesRotatedV = textureCoordinatesV * angleRadiansCos + textureCoordinatesU * angleRadiansSin;
		
		final float textureCoordinatesScaledU = textureCoordinatesRotatedU * scaleU * resolutionX - 0.5F;
		final float textureCoordinatesScaledV = textureCoordinatesRotatedV * scaleV * resolutionY - 0.5F;
		
		final float x = positiveModuloF(textureCoordinatesScaledU, resolutionX);
		final float y = positiveModuloF(textureCoordinatesScaledV, resolutionY);
		
		final int minimumX = (int)(floor(x));
		final int maximumX = (int)(ceil(x));
		
		final int minimumY = (int)(floor(y));
		final int maximumY = (int)(ceil(y));
		
		final int offsetImage = offset + LDRImageLight.ARRAY_OFFSET_IMAGE;
		final int offsetColor00RGB = offsetImage + (positiveModuloI(minimumY, resolutionY) * resolutionX + positiveModuloI(minimumX, resolutionX));
		final int offsetColor01RGB = offsetImage + (positiveModuloI(minimumY, resolutionY) * resolutionX + positiveModuloI(maximumX, resolutionX));
		final int offsetColor10RGB = offsetImage + (positiveModuloI(maximumY, resolutionY) * resolutionX + positiveModuloI(minimumX, resolutionX));
		final int offsetColor11RGB = offsetImage + (positiveModuloI(maximumY, resolutionY) * resolutionX + positiveModuloI(maximumX, resolutionX));
		
		final int color00RGB = (int)(this.lightLDRImageLightArray[offsetColor00RGB]);
		final int color01RGB = (int)(this.lightLDRImageLightArray[offsetColor01RGB]);
		final int color10RGB = (int)(this.lightLDRImageLightArray[offsetColor10RGB]);
		final int color11RGB = (int)(this.lightLDRImageLightArray[offsetColor11RGB]);
		
		final float tX = x - minimumX;
		final float tY = y - minimumY;
		
		final float component1 = lerp(lerp(colorRGBIntToRFloat(color00RGB), colorRGBIntToRFloat(color01RGB), tX), lerp(colorRGBIntToRFloat(color10RGB), colorRGBIntToRFloat(color11RGB), tX), tY);
		final float component2 = lerp(lerp(colorRGBIntToGFloat(color00RGB), colorRGBIntToGFloat(color01RGB), tX), lerp(colorRGBIntToGFloat(color10RGB), colorRGBIntToGFloat(color11RGB), tX), tY);
		final float component3 = lerp(lerp(colorRGBIntToBFloat(color00RGB), colorRGBIntToBFloat(color01RGB), tX), lerp(colorRGBIntToBFloat(color10RGB), colorRGBIntToBFloat(color11RGB), tX), tY);
		
		color3FLHSSet(component1 / probabilityDensityFunctionValue, component2 / probabilityDensityFunctionValue, component3 / probabilityDensityFunctionValue);
	}
	
//	TODO: Add Javadocs!
	protected final void materialBXDFBegin() {
//		Initialize the orthonormal basis:
		orthonormalBasis33FSetIntersectionOrthonormalBasisS();
		
//		Retrieve the ray direction in world space:
		final float rayDirectionWorldSpaceX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionWorldSpaceY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionWorldSpaceZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
//		Compute the outgoing direction in world space as the negated ray direction in world space:
		final float outgoingWorldSpaceX = -rayDirectionWorldSpaceX;
		final float outgoingWorldSpaceY = -rayDirectionWorldSpaceY;
		final float outgoingWorldSpaceZ = -rayDirectionWorldSpaceZ;
		
//		Transform the outgoing direction in world space to the outgoing direction in shade space:
		vector3FSetOrthonormalBasis33FTransformReverseNormalize(outgoingWorldSpaceX, outgoingWorldSpaceY, outgoingWorldSpaceZ);
		
//		Retrieve the outgoing direction in shade space:
		final float outgoingShadeSpaceX = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float outgoingShadeSpaceY = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float outgoingShadeSpaceZ = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
//		Retrieve the normal in world space:
		final float normalWorldSpaceX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
		final float normalWorldSpaceY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
		final float normalWorldSpaceZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
		
//		Transform the normal in world space to the normal in shade space:
		vector3FSetOrthonormalBasis33FTransformReverseNormalize(normalWorldSpaceX, normalWorldSpaceY, normalWorldSpaceZ);
		
//		Retrieve the normal in shade space:
		final float normalShadeSpaceX = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float normalShadeSpaceY = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float normalShadeSpaceZ = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
//		Set the values:
		materialBXDFSetNormal(normalShadeSpaceX, normalShadeSpaceY, normalShadeSpaceZ);
		materialBXDFSetOutgoing(outgoingShadeSpaceX, outgoingShadeSpaceY, outgoingShadeSpaceZ);
	}
	
//	TODO: Add Javadocs!
	protected final void materialBXDFEnd() {
//		Retrieve the incoming direction in shade space:
		final float incomingShadeSpaceX = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_INCOMING + 0];
		final float incomingShadeSpaceY = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_INCOMING + 1];
		final float incomingShadeSpaceZ = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_INCOMING + 2];
		
//		Transform the incoming direction in shade space to the incoming direction in world space:
		vector3FSetOrthonormalBasis33FTransformNormalize(incomingShadeSpaceX, incomingShadeSpaceY, incomingShadeSpaceZ);
		
//		Retrieve the incoming direction in world space:
		final float incomingWorldSpaceX = -super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float incomingWorldSpaceY = -super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float incomingWorldSpaceZ = -super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
//		Set the values:
		vector3FSet(incomingWorldSpaceX, incomingWorldSpaceY, incomingWorldSpaceZ);
	}
	
//	TODO: Add Javadocs!
	protected final void materialBXDFSetIncoming(final float incomingX, final float incomingY, final float incomingZ) {
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_INCOMING + 0] = incomingX;
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_INCOMING + 1] = incomingY;
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_INCOMING + 2] = incomingZ;
	}
	
//	TODO: Add Javadocs!
	protected final void materialBXDFSetNormal(final float normalX, final float normalY, final float normalZ) {
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL + 0] = normalX;
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL + 1] = normalY;
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL + 2] = normalZ;
	}
	
//	TODO: Add Javadocs!
	protected final void materialBXDFSetOutgoing(final float outgoingX, final float outgoingY, final float outgoingZ) {
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING + 0] = outgoingX;
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING + 1] = outgoingY;
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING + 2] = outgoingZ;
	}
	
//	TODO: Add Javadocs!
	protected final void materialEmittance() {
		final int primitiveIndex = (int)(this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX]);
		final int primitiveArrayOffset = primitiveIndex * Primitive.ARRAY_LENGTH;
		final int materialID = this.primitiveArray[primitiveArrayOffset + Primitive.ARRAY_OFFSET_MATERIAL_ID];
		final int materialOffset = this.primitiveArray[primitiveArrayOffset + Primitive.ARRAY_OFFSET_MATERIAL_OFFSET];
		final int materialOffsetTextureEmissionIDRayitoMaterial = materialOffset + Material.ARRAY_OFFSET_TEXTURE_EMISSION_ID;
		final int materialOffsetTextureEmissionOffsetRayitoMaterial = materialOffset + Material.ARRAY_OFFSET_TEXTURE_EMISSION_OFFSET;
		final int materialOffsetTextureEmissionIDSmallPTMaterial = materialOffset + SmallPTMaterial.ARRAY_OFFSET_TEXTURE_EMISSION_ID;
		final int materialOffsetTextureEmissionOffsetSmallPTMaterial = materialOffset + SmallPTMaterial.ARRAY_OFFSET_TEXTURE_EMISSION_OFFSET;
		
		int textureEmissionID = 0;
		int textureEmissionOffset = 0;
		
		if(materialID == ClearCoatSmallPTMaterial.ID) {
			textureEmissionID = this.materialClearCoatSmallPTMaterialArray[materialOffsetTextureEmissionIDSmallPTMaterial];
			textureEmissionOffset = this.materialClearCoatSmallPTMaterialArray[materialOffsetTextureEmissionOffsetSmallPTMaterial];
		} else if(materialID == GlassRayitoMaterial.ID) {
			textureEmissionID = this.materialGlassRayitoMaterialArray[materialOffsetTextureEmissionIDRayitoMaterial];
			textureEmissionOffset = this.materialGlassRayitoMaterialArray[materialOffsetTextureEmissionOffsetRayitoMaterial];
		} else if(materialID == GlassSmallPTMaterial.ID) {
			textureEmissionID = this.materialGlassSmallPTMaterialArray[materialOffsetTextureEmissionIDSmallPTMaterial];
			textureEmissionOffset = this.materialGlassSmallPTMaterialArray[materialOffsetTextureEmissionOffsetSmallPTMaterial];
		} else if(materialID == MatteRayitoMaterial.ID) {
			textureEmissionID = this.materialMatteRayitoMaterialArray[materialOffsetTextureEmissionIDRayitoMaterial];
			textureEmissionOffset = this.materialMatteRayitoMaterialArray[materialOffsetTextureEmissionOffsetRayitoMaterial];
		} else if(materialID == MatteSmallPTMaterial.ID) {
			textureEmissionID = this.materialMatteSmallPTMaterialArray[materialOffsetTextureEmissionIDSmallPTMaterial];
			textureEmissionOffset = this.materialMatteSmallPTMaterialArray[materialOffsetTextureEmissionOffsetSmallPTMaterial];
		} else if(materialID == MetalRayitoMaterial.ID) {
			textureEmissionID = this.materialMetalRayitoMaterialArray[materialOffsetTextureEmissionIDRayitoMaterial];
			textureEmissionOffset = this.materialMetalRayitoMaterialArray[materialOffsetTextureEmissionOffsetRayitoMaterial];
		} else if(materialID == MetalSmallPTMaterial.ID) {
			textureEmissionID = this.materialMetalSmallPTMaterialArray[materialOffsetTextureEmissionIDSmallPTMaterial];
			textureEmissionOffset = this.materialMetalSmallPTMaterialArray[materialOffsetTextureEmissionOffsetSmallPTMaterial];
		} else if(materialID == MirrorRayitoMaterial.ID) {
			textureEmissionID = this.materialMirrorRayitoMaterialArray[materialOffsetTextureEmissionIDRayitoMaterial];
			textureEmissionOffset = this.materialMirrorRayitoMaterialArray[materialOffsetTextureEmissionOffsetRayitoMaterial];
		} else if(materialID == MirrorSmallPTMaterial.ID) {
			textureEmissionID = this.materialMirrorSmallPTMaterialArray[materialOffsetTextureEmissionIDSmallPTMaterial];
			textureEmissionOffset = this.materialMirrorSmallPTMaterialArray[materialOffsetTextureEmissionOffsetSmallPTMaterial];
		}
		
		textureEvaluate(textureEmissionID, textureEmissionOffset);
	}
	
//	TODO: Add Javadocs!
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
		super.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = orthonormalBasisGUX;
		super.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = orthonormalBasisGUY;
		super.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = orthonormalBasisGUZ;
		super.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = orthonormalBasisGVX;
		super.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = orthonormalBasisGVY;
		super.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = orthonormalBasisGVZ;
		super.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = orthonormalBasisGWX;
		super.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = orthonormalBasisGWY;
		super.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = orthonormalBasisGWZ;
	}
	
//	TODO: Add Javadocs!
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
		super.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = orthonormalBasisSUX;
		super.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = orthonormalBasisSUY;
		super.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = orthonormalBasisSUZ;
		super.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = orthonormalBasisSVX;
		super.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = orthonormalBasisSVY;
		super.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = orthonormalBasisSVZ;
		super.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = orthonormalBasisSWX;
		super.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = orthonormalBasisSWY;
		super.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = orthonormalBasisSWZ;
	}
	
//	TODO: Add Javadocs!
	protected final void ray3FSetFromSurfaceIntersectionPointAndVector3F() {
		final float surfaceIntersectionPointX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0];
		final float surfaceIntersectionPointY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1];
		final float surfaceIntersectionPointZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2];
		
		final float directionX = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float directionY = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float directionZ = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		final float directionLengthReciprocal = vector3FLengthReciprocal(directionX, directionY, directionZ);
		final float directionNormalizedX = directionX * directionLengthReciprocal;
		final float directionNormalizedY = directionY * directionLengthReciprocal;
		final float directionNormalizedZ = directionZ * directionLengthReciprocal;
		
		final float originX = surfaceIntersectionPointX + directionNormalizedX * 0.001F;
		final float originY = surfaceIntersectionPointY + directionNormalizedY * 0.001F;
		final float originZ = surfaceIntersectionPointZ + directionNormalizedZ * 0.001F;
		
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0] = originX;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1] = originY;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2] = originZ;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0] = directionNormalizedX;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1] = directionNormalizedY;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2] = directionNormalizedZ;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MINIMUM] = DEFAULT_T_MINIMUM;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM] = DEFAULT_T_MAXIMUM;
	}
	
//	TODO: Add Javadocs!
	protected final void ray3FSetMatrix44FTransformObjectToWorld(final int primitiveIndex) {
		doRay3FSetMatrix44FTransform(primitiveIndex * Matrix44F.ARRAY_SIZE * 2);
	}
	
//	TODO: Add Javadocs!
	protected final void ray3FSetMatrix44FTransformWorldToObject(final int primitiveIndex) {
		doRay3FSetMatrix44FTransform(primitiveIndex * Matrix44F.ARRAY_SIZE * 2 + Matrix44F.ARRAY_SIZE);
	}
	
//	TODO: Add Javadocs!
	protected final void textureEvaluate(final int textureID, final int textureOffset) {
		/*
//		This takes too long. From 9 - 10 ms to 13 - 14 ms.
		if(textureID == BlendTexture.ID) {
			final int textureAID = (int)(this.textureBlendTextureArray[textureOffset + BlendTexture.ARRAY_OFFSET_TEXTURE_A_ID]);
			final int textureAOffset = (int)(this.textureBlendTextureArray[textureOffset + BlendTexture.ARRAY_OFFSET_TEXTURE_A_OFFSET]);
			final int textureBID = (int)(this.textureBlendTextureArray[textureOffset + BlendTexture.ARRAY_OFFSET_TEXTURE_B_ID]);
			final int textureBOffset = (int)(this.textureBlendTextureArray[textureOffset + BlendTexture.ARRAY_OFFSET_TEXTURE_B_OFFSET]);
			
			final float tComponent1 = this.textureBlendTextureArray[textureOffset + BlendTexture.ARRAY_OFFSET_T_COMPONENT_1];
			final float tComponent2 = this.textureBlendTextureArray[textureOffset + BlendTexture.ARRAY_OFFSET_T_COMPONENT_2];
			final float tComponent3 = this.textureBlendTextureArray[textureOffset + BlendTexture.ARRAY_OFFSET_T_COMPONENT_3];
			
			textureEvaluateExcludingBlendTexture(textureAID, textureAOffset);
			
			final float textureAComponent1 = color3FLHSGetComponent1();
			final float textureAComponent2 = color3FLHSGetComponent2();
			final float textureAComponent3 = color3FLHSGetComponent3();
			
			textureEvaluateExcludingBlendTexture(textureBID, textureBOffset);
			
			final float textureBComponent1 = color3FLHSGetComponent1();
			final float textureBComponent2 = color3FLHSGetComponent2();
			final float textureBComponent3 = color3FLHSGetComponent3();
			
			final float component1 = lerp(textureAComponent1, textureBComponent1, tComponent1);
			final float component2 = lerp(textureAComponent2, textureBComponent2, tComponent2);
			final float component3 = lerp(textureAComponent3, textureBComponent3, tComponent3);
			
			color3FLHSSet(component1, component2, component3);
		} else {
			textureEvaluateExcludingBlendTexture(textureID, textureOffset);
		}
		*/
		
		textureEvaluateExcludingBlendTexture(textureID, textureOffset);
	}
	
//	TODO: Add Javadocs!
	protected final void textureEvaluateExcludingBlendTexture(final int textureID, final int textureOffset) {
		int currentTextureID = textureID;
		int currentTextureOffset = textureOffset;
		
		float component1 = 0.0F;
		float component2 = 0.0F;
		float component3 = 0.0F;
		
		final float surfaceNormalX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
		final float surfaceNormalY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
		final float surfaceNormalZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
		
		final float surfaceIntersectionPointX = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0];
		final float surfaceIntersectionPointY = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1];
		final float surfaceIntersectionPointZ = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2];
		
		final float textureCoordinatesU = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0];
		final float textureCoordinatesV = this.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1];
		
		while(currentTextureID != -1 && currentTextureOffset != -1) {
			if(currentTextureID == BlendTexture.ID) {
//				TODO: Implement!
				component1 = 0.5F;
				component2 = 0.5F;
				component3 = 0.5F;
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			} else if(currentTextureID == BullseyeTexture.ID) {
				final float originX = this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_ORIGIN + 0];
				final float originY = this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_ORIGIN + 1];
				final float originZ = this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_ORIGIN + 2];
				
				final int textureAID = (int)(this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_TEXTURE_A_ID]);
				final int textureAOffset = (int)(this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_TEXTURE_A_OFFSET]);
				final int textureBID = (int)(this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_TEXTURE_B_ID]);
				final int textureBOffset = (int)(this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_TEXTURE_B_OFFSET]);
				
				final float scale = this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_SCALE];
				
				final float distance = point3FDistance(originX, originY, originZ, surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
				final float distanceScaled = distance * scale;
				final float distanceScaledRemainder = remainder(distanceScaled, 1.0F);
				
				final boolean isTextureA = distanceScaledRemainder > 0.5F;
				
				currentTextureID = isTextureA ? textureAID : textureBID;
				currentTextureOffset = isTextureA ? textureAOffset : textureBOffset;
			} else if(currentTextureID == CheckerboardTexture.ID) {
				final float angleRadians = this.textureCheckerboardTextureArray[currentTextureOffset + CheckerboardTexture.ARRAY_OFFSET_ANGLE_RADIANS];
				final float angleRadiansCos = cos(angleRadians);
				final float angleRadiansSin = sin(angleRadians);
				
				final int textureAID = (int)(this.textureCheckerboardTextureArray[currentTextureOffset + CheckerboardTexture.ARRAY_OFFSET_TEXTURE_A_ID]);
				final int textureAOffset = (int)(this.textureCheckerboardTextureArray[currentTextureOffset + CheckerboardTexture.ARRAY_OFFSET_TEXTURE_A_OFFSET]);
				final int textureBID = (int)(this.textureCheckerboardTextureArray[currentTextureOffset + CheckerboardTexture.ARRAY_OFFSET_TEXTURE_B_ID]);
				final int textureBOffset = (int)(this.textureCheckerboardTextureArray[currentTextureOffset + CheckerboardTexture.ARRAY_OFFSET_TEXTURE_B_OFFSET]);
				
				final float scaleU = this.textureCheckerboardTextureArray[currentTextureOffset + CheckerboardTexture.ARRAY_OFFSET_SCALE + 0];
				final float scaleV = this.textureCheckerboardTextureArray[currentTextureOffset + CheckerboardTexture.ARRAY_OFFSET_SCALE + 1];
				
				final boolean isU = fractionalPart((textureCoordinatesU * angleRadiansCos - textureCoordinatesV * angleRadiansSin) * scaleU, false) > 0.5F;
				final boolean isV = fractionalPart((textureCoordinatesV * angleRadiansCos + textureCoordinatesU * angleRadiansSin) * scaleV, false) > 0.5F;
				
				final boolean isTextureA = isU ^ isV;
				
				currentTextureID = isTextureA ? textureAID : textureBID;
				currentTextureOffset = isTextureA ? textureAOffset : textureBOffset;
			} else if(currentTextureID == ConstantTexture.ID) {
				component1 = this.textureConstantTextureArray[currentTextureOffset + ConstantTexture.ARRAY_OFFSET_COLOR + 0];
				component2 = this.textureConstantTextureArray[currentTextureOffset + ConstantTexture.ARRAY_OFFSET_COLOR + 1];
				component3 = this.textureConstantTextureArray[currentTextureOffset + ConstantTexture.ARRAY_OFFSET_COLOR + 2];
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			} else if(currentTextureID == FunctionTexture.ID) {
//				The FunctionTexture is not supported:
				component1 = 0.5F;
				component2 = 0.5F;
				component3 = 0.5F;
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			} else if(currentTextureID == LDRImageTexture.ID) {
				final float angleRadians = this.textureLDRImageTextureArray[currentTextureOffset + LDRImageTexture.ARRAY_OFFSET_ANGLE_RADIANS];
				final float angleRadiansCos = cos(angleRadians);
				final float angleRadiansSin = sin(angleRadians);
				
				final float scaleU = this.textureLDRImageTextureArray[currentTextureOffset + LDRImageTexture.ARRAY_OFFSET_SCALE + 0];
				final float scaleV = this.textureLDRImageTextureArray[currentTextureOffset + LDRImageTexture.ARRAY_OFFSET_SCALE + 1];
				
				final int resolutionX = (int)(this.textureLDRImageTextureArray[currentTextureOffset + LDRImageTexture.ARRAY_OFFSET_RESOLUTION_X]);
				final int resolutionY = (int)(this.textureLDRImageTextureArray[currentTextureOffset + LDRImageTexture.ARRAY_OFFSET_RESOLUTION_Y]);
				
				final float textureCoordinatesRotatedU = textureCoordinatesU * angleRadiansCos - textureCoordinatesV * angleRadiansSin;
				final float textureCoordinatesRotatedV = textureCoordinatesV * angleRadiansCos + textureCoordinatesU * angleRadiansSin;
				
				final float textureCoordinatesScaledU = textureCoordinatesRotatedU * scaleU * resolutionX - 0.5F;
				final float textureCoordinatesScaledV = textureCoordinatesRotatedV * scaleV * resolutionY - 0.5F;
				
				final float x = positiveModuloF(textureCoordinatesScaledU, resolutionX);
				final float y = positiveModuloF(textureCoordinatesScaledV, resolutionY);
				
				final int minimumX = (int)(floor(x));
				final int maximumX = (int)(ceil(x));
				
				final int minimumY = (int)(floor(y));
				final int maximumY = (int)(ceil(y));
				
				final int offsetImage = currentTextureOffset + LDRImageTexture.ARRAY_OFFSET_IMAGE;
				final int offsetColor00RGB = offsetImage + (positiveModuloI(minimumY, resolutionY) * resolutionX + positiveModuloI(minimumX, resolutionX));
				final int offsetColor01RGB = offsetImage + (positiveModuloI(minimumY, resolutionY) * resolutionX + positiveModuloI(maximumX, resolutionX));
				final int offsetColor10RGB = offsetImage + (positiveModuloI(maximumY, resolutionY) * resolutionX + positiveModuloI(minimumX, resolutionX));
				final int offsetColor11RGB = offsetImage + (positiveModuloI(maximumY, resolutionY) * resolutionX + positiveModuloI(maximumX, resolutionX));
				
				final int color00RGB = (int)(this.textureLDRImageTextureArray[offsetColor00RGB]);
				final int color01RGB = (int)(this.textureLDRImageTextureArray[offsetColor01RGB]);
				final int color10RGB = (int)(this.textureLDRImageTextureArray[offsetColor10RGB]);
				final int color11RGB = (int)(this.textureLDRImageTextureArray[offsetColor11RGB]);
				
				final float tX = x - minimumX;
				final float tY = y - minimumY;
				
				component1 = lerp(lerp(colorRGBIntToRFloat(color00RGB), colorRGBIntToRFloat(color01RGB), tX), lerp(colorRGBIntToRFloat(color10RGB), colorRGBIntToRFloat(color11RGB), tX), tY);
				component2 = lerp(lerp(colorRGBIntToGFloat(color00RGB), colorRGBIntToGFloat(color01RGB), tX), lerp(colorRGBIntToGFloat(color10RGB), colorRGBIntToGFloat(color11RGB), tX), tY);
				component3 = lerp(lerp(colorRGBIntToBFloat(color00RGB), colorRGBIntToBFloat(color01RGB), tX), lerp(colorRGBIntToBFloat(color10RGB), colorRGBIntToBFloat(color11RGB), tX), tY);
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			} else if(currentTextureID == MarbleTexture.ID) {
				final int colorARGB = (int)(this.textureMarbleTextureArray[currentTextureOffset + MarbleTexture.ARRAY_OFFSET_COLOR_A]);
				final int colorBRGB = (int)(this.textureMarbleTextureArray[currentTextureOffset + MarbleTexture.ARRAY_OFFSET_COLOR_B]);
				final int colorCRGB = (int)(this.textureMarbleTextureArray[currentTextureOffset + MarbleTexture.ARRAY_OFFSET_COLOR_C]);
				
				final float frequency = this.textureMarbleTextureArray[currentTextureOffset + MarbleTexture.ARRAY_OFFSET_FREQUENCY];
				final float scale = this.textureMarbleTextureArray[currentTextureOffset + MarbleTexture.ARRAY_OFFSET_SCALE];
				
				final int octaves = (int)(this.textureMarbleTextureArray[currentTextureOffset + MarbleTexture.ARRAY_OFFSET_OCTAVES]);
				
				final float x = surfaceIntersectionPointX * frequency;
				final float y = surfaceIntersectionPointY * frequency;
				final float z = surfaceIntersectionPointZ * frequency;
				final float r = scale * perlinTurbulenceXYZ(x, y, z, octaves);
				final float s = 2.0F * abs(sin(x + r));
				final float t = s < 1.0F ? s : s - 1.0F;
				
				component1 = s < 1.0F ? lerp(colorRGBIntToRFloat(colorCRGB), colorRGBIntToRFloat(colorBRGB), t) : lerp(colorRGBIntToRFloat(colorBRGB), colorRGBIntToRFloat(colorARGB), t);
				component2 = s < 1.0F ? lerp(colorRGBIntToGFloat(colorCRGB), colorRGBIntToGFloat(colorBRGB), t) : lerp(colorRGBIntToGFloat(colorBRGB), colorRGBIntToGFloat(colorARGB), t);
				component3 = s < 1.0F ? lerp(colorRGBIntToBFloat(colorCRGB), colorRGBIntToBFloat(colorBRGB), t) : lerp(colorRGBIntToBFloat(colorBRGB), colorRGBIntToBFloat(colorARGB), t);
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			} else if(currentTextureID == SimplexFractionalBrownianMotionTexture.ID) {
				final int colorRGB = (int)(this.textureSimplexFractionalBrownianMotionTextureArray[currentTextureOffset + SimplexFractionalBrownianMotionTexture.ARRAY_OFFSET_COLOR]);
				
				final float frequency = this.textureSimplexFractionalBrownianMotionTextureArray[currentTextureOffset + SimplexFractionalBrownianMotionTexture.ARRAY_OFFSET_FREQUENCY];
				final float gain = this.textureSimplexFractionalBrownianMotionTextureArray[currentTextureOffset + SimplexFractionalBrownianMotionTexture.ARRAY_OFFSET_GAIN];
				
				final int octaves = (int)(this.textureSimplexFractionalBrownianMotionTextureArray[currentTextureOffset + SimplexFractionalBrownianMotionTexture.ARRAY_OFFSET_OCTAVES]);
				
				final float colorR = colorRGBIntToRFloat(colorRGB);
				final float colorG = colorRGBIntToGFloat(colorRGB);
				final float colorB = colorRGBIntToBFloat(colorRGB);
				
				final float noise = simplexFractionalBrownianMotionXYZ(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ, frequency, gain, 0.0F, 1.0F, octaves);
				
				component1 = colorR * noise;
				component2 = colorG * noise;
				component3 = colorB * noise;
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			} else if(currentTextureID == SurfaceNormalTexture.ID) {
				component1 = (surfaceNormalX + 1.0F) * 0.5F;
				component2 = (surfaceNormalY + 1.0F) * 0.5F;
				component3 = (surfaceNormalZ + 1.0F) * 0.5F;
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			} else if(currentTextureID == UVTexture.ID) {
				component1 = textureCoordinatesU;
				component2 = textureCoordinatesV;
				component3 = 0.0F;
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			} else {
//				The Texture is not supported:
				component1 = 0.5F;
				component2 = 0.5F;
				component3 = 0.5F;
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			}
		}
		
		color3FLHSSet(component1, component2, component3);
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
		final float oldOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float oldOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float oldOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		
//		Retrieve the ray direction components from the old space:
		final float oldDirectionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float oldDirectionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float oldDirectionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		
//		Retrieve the ray boundary variables from the old space:
		final float oldTMinimum = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MINIMUM];
		final float oldTMaximum = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM];
		
//		Transform the ray origin from the old space to the new space:
		point3FSetMatrix44FTransformAndDivide(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, oldOriginX, oldOriginY, oldOriginZ);
		
//		Transform the ray direction from the old space to the new space:
		vector3FSetMatrix44FTransformNormalize(element11, element12, element13, element21, element22, element23, element31, element32, element33, oldDirectionX, oldDirectionY, oldDirectionZ);
		
//		Retrieve the ray origin components from the new space:
		final float newOriginX = this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float newOriginY = this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float newOriginZ = this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_3];
		
//		Retrieve the ray direction components from the new space:
		final float newDirectionX = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float newDirectionY = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float newDirectionZ = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
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
			final float newReferencePointTMinimumX = this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_1];
			final float newReferencePointTMinimumY = this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_2];
			final float newReferencePointTMinimumZ = this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_3];
			
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
			final float newReferencePointTMaximumX = this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_1];
			final float newReferencePointTMaximumY = this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_2];
			final float newReferencePointTMaximumZ = this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_3];
			
//			Compute the distance from the origin in the new space to the reference point in the new space:
			final float distanceOriginToReferencePointTMaximum = point3FDistance(newOriginX, newOriginY, newOriginZ, newReferencePointTMaximumX, newReferencePointTMaximumY, newReferencePointTMaximumZ);
			
//			Update the new maximum ray boundary:
			newTMaximum = abs(distanceOriginToReferencePointTMaximum);
		}
		
//		Set the new variables:
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0] = newOriginX;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1] = newOriginY;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2] = newOriginZ;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0] = newDirectionX;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1] = newDirectionY;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2] = newDirectionZ;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MINIMUM] = newTMinimum;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM] = newTMaximum;
	}
	
	private void doSetupPixelArray() {
		put(this.pixelArray = Floats.array(getResolution() * 2, 0.0F));
	}
	
	private void doSetupScene() {
		final SceneCompiler sceneCompiler = new SceneCompiler();
		
		final CompiledScene compiledScene = sceneCompiler.compile(getScene());
		
		put(this.boundingVolume3FAxisAlignedBoundingBox3FArray = compiledScene.getBoundingVolume3FAxisAlignedBoundingBox3FArray());
		put(this.boundingVolume3FBoundingSphere3FArray = compiledScene.getBoundingVolume3FBoundingSphere3FArray());
		put(this.cameraArray = compiledScene.getCameraArray());
		put(this.lightLDRImageLightArray = compiledScene.getLightLDRImageLightArray());
		put(this.lightLDRImageLightOffsetArray = compiledScene.getLightLDRImageLightOffsetArray());
		put(this.materialClearCoatSmallPTMaterialArray = compiledScene.getMaterialClearCoatSmallPTMaterialArray());
		put(this.materialGlassRayitoMaterialArray = compiledScene.getMaterialGlassRayitoMaterialArray());
		put(this.materialGlassSmallPTMaterialArray = compiledScene.getMaterialGlassSmallPTMaterialArray());
		put(this.materialMatteRayitoMaterialArray = compiledScene.getMaterialMatteRayitoMaterialArray());
		put(this.materialMatteSmallPTMaterialArray = compiledScene.getMaterialMatteSmallPTMaterialArray());
		put(this.materialMetalRayitoMaterialArray = compiledScene.getMaterialMetalRayitoMaterialArray());
		put(this.materialMetalSmallPTMaterialArray = compiledScene.getMaterialMetalSmallPTMaterialArray());
		put(this.materialMirrorRayitoMaterialArray = compiledScene.getMaterialMirrorRayitoMaterialArray());
		put(this.materialMirrorSmallPTMaterialArray = compiledScene.getMaterialMirrorSmallPTMaterialArray());
		put(this.matrix44FArray = compiledScene.getMatrix44FArray());
		put(this.primitiveArray = compiledScene.getPrimitiveArray());
		put(this.shape3FPlane3FArray = compiledScene.getShape3FPlane3FArray());
		put(this.shape3FRectangularCuboid3FArray = compiledScene.getShape3FRectangularCuboid3FArray());
		put(this.shape3FSphere3FArray = compiledScene.getShape3FSphere3FArray());
		put(this.shape3FTorus3FArray = compiledScene.getShape3FTorus3FArray());
		put(this.shape3FTriangle3FArray = compiledScene.getShape3FTriangle3FArray());
		put(this.shape3FTriangleMesh3FArray = compiledScene.getShape3FTriangleMesh3FArray());
		put(this.textureBlendTextureArray = compiledScene.getTextureBlendTextureArray());
		put(this.textureBullseyeTextureArray = compiledScene.getTextureBullseyeTextureArray());
		put(this.textureCheckerboardTextureArray = compiledScene.getTextureCheckerboardTextureArray());
		put(this.textureConstantTextureArray = compiledScene.getTextureConstantTextureArray());
		put(this.textureLDRImageTextureArray = compiledScene.getTextureLDRImageTextureArray());
		put(this.textureMarbleTextureArray = compiledScene.getTextureMarbleTextureArray());
		put(this.textureSimplexFractionalBrownianMotionTextureArray = compiledScene.getTextureSimplexFractionalBrownianMotionTextureArray());
		
		this.lightLDRImageLightCount = compiledScene.getLightLDRImageLightCount();
		this.primitiveCount = compiledScene.getPrimitiveCount();
	}
}