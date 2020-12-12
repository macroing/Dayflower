/**
 * Copyright 2020 J&#246;rgen Lundgren
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

import static org.dayflower.util.Floats.PI_DIVIDED_BY_2;
import static org.dayflower.util.Floats.PI_MULTIPLIED_BY_2;
import static org.dayflower.util.Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.util.Floats.PI_RECIPROCAL;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.geometry.boundingvolume.BoundingSphere3F;
import org.dayflower.geometry.boundingvolume.InfiniteBoundingVolume3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.geometry.shape.RectangularCuboid3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Torus3F;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RendererConfiguration;
import org.dayflower.renderer.RendererObserver;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.util.Floats;
import org.dayflower.util.Timer;

import com.amd.aparapi.Range;

/**
 * An {@code AbstractGPURenderer} is an abstract implementation of {@link Renderer} that takes care of most aspects.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractGPURenderer extends AbstractKernel implements Renderer {
	protected static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U = 0;
	protected static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V = 3;
	protected static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W = 6;
	protected static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U = 9;
	protected static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V = 12;
	protected static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W = 15;
	protected static final int INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX = 18;
	protected static final int INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT = 19;
	protected static final int INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES = 22;
	protected static final int INTERSECTION_ARRAY_SIZE = 24;
	protected static final int RAY_3_F_ARRAY_OFFSET_DIRECTION = 3;
	protected static final int RAY_3_F_ARRAY_OFFSET_ORIGIN = 0;
	protected static final int RAY_3_F_ARRAY_OFFSET_T_MAXIMUM = 7;
	protected static final int RAY_3_F_ARRAY_OFFSET_T_MINIMUM = 6;
	protected static final int RAY_3_F_ARRAY_SIZE = 8;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	protected float[] boundingVolume3FAxisAlignedBoundingBox3FArray;
	protected float[] boundingVolume3FBoundingSphere3FArray;
	protected float[] cameraArray;
	protected float[] intersectionArray_$private$24;
	protected float[] matrix44FArray;
	protected float[] pixelArray;
	protected float[] radianceRGBArray;
	protected float[] ray3FArray_$private$8;
	protected float[] shape3FPlane3FArray;
	protected float[] shape3FRectangularCuboid3FArray;
	protected float[] shape3FSphere3FArray;
	protected float[] shape3FTorus3FArray;
	protected float[] shape3FTriangle3FArray;
	protected int primitiveCount;
	protected int[] primitiveArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AtomicBoolean isClearing;
	private final AtomicBoolean isRendering;
	private final AtomicReference<RendererConfiguration> rendererConfiguration;
	private final AtomicReference<RendererObserver> rendererObserver;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractGPURenderer} instance.
	 * <p>
	 * If either {@code rendererConfiguration} or {@code rendererObserver} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rendererConfiguration the {@link RendererConfiguration} instance associated with this {@code AbstractGPURenderer} instance
	 * @param rendererObserver the {@link RendererObserver} instance associated with this {@code AbstractGPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, either {@code rendererConfiguration} or {@code rendererObserver} are {@code null}
	 */
	protected AbstractGPURenderer(final RendererConfiguration rendererConfiguration, final RendererObserver rendererObserver) {
		this.boundingVolume3FAxisAlignedBoundingBox3FArray = new float[0];
		this.boundingVolume3FBoundingSphere3FArray = new float[0];
		this.cameraArray = new float[0];
		this.intersectionArray_$private$24 = new float[INTERSECTION_ARRAY_SIZE];
		this.matrix44FArray = new float[0];
		this.pixelArray = new float[0];
		this.radianceRGBArray = new float[0];
		this.ray3FArray_$private$8 = new float[RAY_3_F_ARRAY_SIZE];
		this.shape3FPlane3FArray = new float[0];
		this.shape3FRectangularCuboid3FArray = new float[0];
		this.shape3FSphere3FArray = new float[0];
		this.shape3FTorus3FArray = new float[0];
		this.shape3FTriangle3FArray = new float[0];
		this.primitiveCount = 0;
		this.primitiveArray = new int[0];
		this.isClearing = new AtomicBoolean();
		this.isRendering = new AtomicBoolean();
		this.rendererConfiguration = new AtomicReference<>(Objects.requireNonNull(rendererConfiguration, "rendererConfiguration == null"));
		this.rendererObserver = new AtomicReference<>(Objects.requireNonNull(rendererObserver, "rendererObserver == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link RendererConfiguration} instance associated with this {@code AbstractGPURenderer} instance.
	 * 
	 * @return the {@code RendererConfiguration} instance associated with this {@code AbstractGPURenderer} instance
	 */
	@Override
	public final RendererConfiguration getRendererConfiguration() {
		return this.rendererConfiguration.get();
	}
	
	/**
	 * Returns the {@link RendererObserver} instance associated with this {@code AbstractGPURenderer} instance.
	 * 
	 * @return the {@code RendererObserver} instance associated with this {@code AbstractGPURenderer} instance
	 */
	@Override
	public final RendererObserver getRendererObserver() {
		return this.rendererObserver.get();
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code AbstractGPURenderer} instance is clearing the {@link Image} instance in the next {@link #render()} call, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code AbstractGPURenderer} instance is clearing the {@code Image} instance in the next {@code  render()} call, {@code false} otherwise
	 */
	@Override
	public final boolean isClearing() {
		return this.isClearing.get();
	}
	
	/**
	 * Renders the associated {@link Scene} instance to the associated {@link Image} instance and, optionally, updates the associated {@link Display} instance.
	 * <p>
	 * Returns {@code true} if, and only if, rendering was performed, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, rendering was performed, {@code false} otherwise
	 */
	@Override
	public final boolean render() {
		this.isRendering.set(true);
		
		final RendererConfiguration rendererConfiguration = getRendererConfiguration();
		
		final RendererObserver rendererObserver = getRendererObserver();
		
		final Image image = rendererConfiguration.getImage();
		
		final Timer timer = rendererConfiguration.getTimer();
		
		final int renderPasses = rendererConfiguration.getRenderPasses();
		final int renderPassesPerDisplayUpdate = rendererConfiguration.getRenderPassesPerDisplayUpdate();
		final int resolutionX = image.getResolutionX();
		final int resolutionY = image.getResolutionY();
		
		final Range range = Range.create(resolutionX * resolutionY);
		
		for(int renderPass = 1; renderPass <= renderPasses; renderPass++) {
			if(this.isClearing.compareAndSet(true, false)) {
				rendererConfiguration.setRenderPass(0);
				rendererConfiguration.setRenderTime(0L);
				
				image.filmClear();
				image.filmRender();
				
				rendererObserver.onRenderDisplay(this, image);
				
				timer.restart();
			}
			
			rendererObserver.onRenderPassProgress(this, renderPass, renderPasses, 0.0D);
			
			final long currentTimeMillis = System.currentTimeMillis();
			
			execute(range);
			
			final long elapsedTimeMillis = System.currentTimeMillis() - currentTimeMillis;
			
			final float[] pixelArray = doGetPixelArray();
			final float[] radianceRGBArray = doGetRadianceRGBArray();
			
//			final long time0 = System.currentTimeMillis();
			
//			TODO: Fix the following bottleneck using the Kernel itself in a different mode! It taks around 200 milliseconds per render pass.
			for(int y = 0; y < resolutionY; y++) {
				for(int x = 0; x < resolutionX; x++) {
					final int index = y * resolutionX + x;
					final int indexPixelArray = index * 2;
					final int indexRadianceArray = index * 3;
					
					final float r = radianceRGBArray[indexRadianceArray + 0];
					final float g = radianceRGBArray[indexRadianceArray + 1];
					final float b = radianceRGBArray[indexRadianceArray + 2];
					
					final float imageX = x;
					final float imageY = y;
					final float pixelX = pixelArray[indexPixelArray + 0];
					final float pixelY = pixelArray[indexPixelArray + 1];
					
					final Color3F colorRGB = new Color3F(r, g, b);
					final Color3F colorXYZ = Color3F.convertRGBToXYZUsingPBRT(colorRGB);
					
					if(!colorXYZ.hasInfinites() && !colorXYZ.hasNaNs()) {
						image.filmAddColorXYZ(imageX + pixelX, imageY + pixelY, colorXYZ);
					}
				}
			}
			
//			final long time1 = System.currentTimeMillis();
//			final long time2 = time1 - time0;
			
//			System.out.println("RGB -> XYZ = " + time2 + " millis.");
			
			rendererObserver.onRenderPassProgress(this, renderPass, renderPasses, 1.0D);
			
			if(renderPass == 1 || renderPass % renderPassesPerDisplayUpdate == 0 || renderPass == renderPasses) {
				image.filmRender();
				
				rendererObserver.onRenderDisplay(this, image);
			}
			
			rendererConfiguration.setRenderPass(rendererConfiguration.getRenderPass() + 1);
			rendererConfiguration.setRenderTime(elapsedTimeMillis);
			
			rendererObserver.onRenderPassComplete(this, renderPass, renderPasses, elapsedTimeMillis);
		}
		
		this.isRendering.set(false);
		
		return true;
	}
	
	/**
	 * Attempts to shutdown the rendering process of this {@code AbstractGPURenderer} instance.
	 * <p>
	 * Returns {@code true} if, and only if, this {@code AbstractGPURenderer} instance was rendering and is shutting down, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code AbstractGPURenderer} instance was rendering and is shutting down, {@code false} otherwise
	 */
	@Override
	public final boolean renderShutdown() {
		return this.isRendering.compareAndSet(true, false);
	}
	
	/**
	 * Call this method to clear the {@link Image} in the next {@link #render()} call.
	 */
	@Override
	public final void clear() {
		this.isClearing.set(true);
	}
	
	/**
	 * Sets the {@link RendererConfiguration} instance associated with this {@code AbstractGPURenderer} instance to {@code rendererConfiguration}.
	 * <p>
	 * If {@code rendererConfiguration} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rendererConfiguration the {@code RendererConfiguration} instance associated with this {@code AbstractGPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code rendererConfiguration} is {@code null}
	 */
	@Override
	public final void setRendererConfiguration(final RendererConfiguration rendererConfiguration) {
		this.rendererConfiguration.set(Objects.requireNonNull(rendererConfiguration, "rendererConfiguration == null"));
	}
	
	/**
	 * Sets the {@link RendererObserver} instance associated with this {@code AbstractGPURenderer} instance to {@code rendererObserver}.
	 * <p>
	 * If {@code rendererObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rendererObserver the {@code RendererObserver} instance associated with this {@code AbstractGPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code rendererObserver} is {@code null}
	 */
	@Override
	public final void setRendererObserver(final RendererObserver rendererObserver) {
		this.rendererObserver.set(Objects.requireNonNull(rendererObserver, "rendererObserver == null"));
	}
	
	/**
	 * Sets up all necessary resources for this {@code AbstractGPURenderer} instance.
	 */
	@Override
	public final void setup() {
		final Image image = doGetImage();
		
		final int resolutionX = image.getResolutionX();
		final int resolutionY = image.getResolutionY();
		
		setResolution(resolutionX, resolutionY);
		
		super.setup();
		
		doSetupPixelArray();
		doSetupRadianceRGBArray();
		doSetupScene();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	protected final boolean ray3FCameraGenerate() {
//		Retrieve the image coordinates:
		final float imageX = getGlobalId() % this.resolutionX;
		final float imageY = getGlobalId() / this.resolutionX;
		
//		Retrieve the pixel coordinates:
		final float pixelX = random();
		final float pixelY = random();
		
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
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MINIMUM] = 0.001F;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM] = Float.MAX_VALUE;
		
//		Fill in the pixel array:
		this.pixelArray[pixelArrayOffset + 0] = pixelX;
		this.pixelArray[pixelArrayOffset + 1] = pixelY;
		
		return true;
	}
	
//	TODO: Add Javadocs!
	protected final boolean intersectionComputeShape3F() {
		int primitiveIndex = -1;
		
		for(int index = 0; index < this.primitiveCount; index++) {
			final int primitiveArrayOffset = index * Primitive.ARRAY_SIZE;
			final int primitiveArrayOffsetBoundingVolumeID = primitiveArrayOffset + Primitive.ARRAY_OFFSET_BOUNDING_VOLUME_ID;
			final int primitiveArrayOffsetBoundingVolumeOffset = primitiveArrayOffset + Primitive.ARRAY_OFFSET_BOUNDING_VOLUME_OFFSET;
			final int primitiveArrayOffsetShapeID = primitiveArrayOffset + Primitive.ARRAY_OFFSET_SHAPE_ID;
			final int primitiveArrayOffsetShapeOffset = primitiveArrayOffset + Primitive.ARRAY_OFFSET_SHAPE_OFFSET;
			
			final int boundingVolumeID = this.primitiveArray[primitiveArrayOffsetBoundingVolumeID];
			final int boundingVolumeOffset = this.primitiveArray[primitiveArrayOffsetBoundingVolumeOffset];
			final int shapeID = this.primitiveArray[primitiveArrayOffsetShapeID];
			final int shapeOffset = this.primitiveArray[primitiveArrayOffsetShapeOffset];
			
			boolean isIntersectingBoundingVolume = false;
			
			if(boundingVolumeID == AxisAlignedBoundingBox3F.ID) {
				isIntersectingBoundingVolume = intersectsBoundingVolume3FAxisAlignedBoundingBox3F(boundingVolumeOffset);
			}
			
			if(boundingVolumeID == BoundingSphere3F.ID) {
				isIntersectingBoundingVolume = intersectsBoundingVolume3FBoundingSphere3F(boundingVolumeOffset);
			}
			
			if(boundingVolumeID == InfiniteBoundingVolume3F.ID) {
				isIntersectingBoundingVolume = true;
			}
			
			if(isIntersectingBoundingVolume) {
				float t = 0.0F;
				
				ray3FTransformWorldToObject(index);
				
				if(shapeID == Plane3F.ID) {
					t = intersectionTShape3FPlane3F(shapeOffset);
				}
				
				if(shapeID == RectangularCuboid3F.ID) {
					t = intersectionTShape3FRectangularCuboid3F(shapeOffset);
				}
				
				if(shapeID == Sphere3F.ID) {
					t = intersectionTShape3FSphere3F(shapeOffset);
				}
				
				if(shapeID == Torus3F.ID) {
					t = intersectionTShape3FTorus3F(shapeOffset);
				}
				
				ray3FTransformObjectToWorld(index);
				
				if(t > 0.0F && t < this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM]) {
					this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM] = t;
					
					primitiveIndex = index;
				}
			}
		}
		
		if(primitiveIndex != -1) {
			final int primitiveArrayOffset = primitiveIndex * Primitive.ARRAY_SIZE;
			final int primitiveArrayOffsetShapeID = primitiveArrayOffset + Primitive.ARRAY_OFFSET_SHAPE_ID;
			final int primitiveArrayOffsetShapeOffset = primitiveArrayOffset + Primitive.ARRAY_OFFSET_SHAPE_OFFSET;
			
			final int shapeID = this.primitiveArray[primitiveArrayOffsetShapeID];
			final int shapeOffset = this.primitiveArray[primitiveArrayOffsetShapeOffset];
			
//			ray3FTransformWorldToObject(primitiveIndex);
			
			if(shapeID == Plane3F.ID) {
				ray3FTransformWorldToObject(primitiveIndex);
				
				intersectionComputeShape3FPlane3F(this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM], primitiveIndex, shapeOffset);
				
				ray3FTransformObjectToWorld(primitiveIndex);
				
				return true;
			}
			
			if(shapeID == RectangularCuboid3F.ID) {
				ray3FTransformWorldToObject(primitiveIndex);
				
				intersectionComputeShape3FRectangularCuboid3F(this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM], primitiveIndex, shapeOffset);
				
				ray3FTransformObjectToWorld(primitiveIndex);
				
				return true;
			}
			
			if(shapeID == Sphere3F.ID) {
				ray3FTransformWorldToObject(primitiveIndex);
				
				intersectionComputeShape3FSphere3F(this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM], primitiveIndex, shapeOffset);
				
				ray3FTransformObjectToWorld(primitiveIndex);
				
				return true;
			}
			
			if(shapeID == Torus3F.ID) {
				ray3FTransformWorldToObject(primitiveIndex);
				
				intersectionComputeShape3FTorus3F(this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM], primitiveIndex, shapeOffset);
				
				ray3FTransformObjectToWorld(primitiveIndex);
				
				return true;
			}
			
//			ray3FTransformObjectToWorld(primitiveIndex);
		}
		
		return false;
	}
	
//	TODO: Add Javadocs!
	protected final boolean intersectsBoundingVolume3FAxisAlignedBoundingBox3F(final int boundingVolume3FAxisAlignedBoundingBox3FArrayOffset) {
		return intersectionTBoundingVolume3FAxisAlignedBoundingBox3F(boundingVolume3FAxisAlignedBoundingBox3FArrayOffset) > 0.0F;
	}
	
//	TODO: Add Javadocs!
	protected final boolean intersectsBoundingVolume3FBoundingSphere3F(final int boundingVolume3FBoundingSphere3FArrayOffset) {
		return intersectionTBoundingVolume3FBoundingSphere3F(boundingVolume3FBoundingSphere3FArrayOffset) > 0.0F;
	}
	
//	TODO: Add Javadocs!
	protected final boolean intersectsShape3F() {
		return intersectionTShape3F() > 0.0F;
	}
	
//	TODO: Add Javadocs!
	protected final boolean intersectsShape3FPlane3F(final int shape3FPlane3FArrayOffset) {
		return intersectionTShape3FPlane3F(shape3FPlane3FArrayOffset) > 0.0F;
	}
	
//	TODO: Add Javadocs!
	protected final boolean intersectsShape3FRectangularCuboid3F(final int shape3FRectangularCuboid3FArrayOffset) {
		return intersectionTShape3FRectangularCuboid3F(shape3FRectangularCuboid3FArrayOffset) > 0.0F;
	}
	
//	TODO: Add Javadocs!
	protected final boolean intersectsShape3FSphere3F(final int shape3FSphere3FArrayOffset) {
		return intersectionTShape3FSphere3F(shape3FSphere3FArrayOffset) > 0.0F;
	}
	
//	TODO: Add Javadocs!
	protected final boolean intersectsShape3FTorus3F(final int shape3FTorus3FArrayOffset) {
		return intersectionTShape3FTorus3F(shape3FTorus3FArrayOffset) > 0.0F;
	}
	
//	TODO: Add Javadocs!
	protected final boolean intersectsShape3FTriangle3F() {
		return intersectionTShape3FTriangle3F() > 0.0F;
	}
	
//	TODO: Add Javadocs!
	protected final float intersectionTBoundingVolume3FAxisAlignedBoundingBox3F(final int boundingVolume3FAxisAlignedBoundingBox3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float rayOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float rayOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		final float rayDirectionReciprocalX = 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionReciprocalY = 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionReciprocalZ = 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		final float rayTMinimum = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MINIMUM];
		final float rayTMaximum = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM];
		
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
	
//	TODO: Add Javadocs!
	protected final float intersectionTBoundingVolume3FBoundingSphere3F(final int boundingVolume3FBoundingSphere3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float rayOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float rayOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		final float rayDirectionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		final float rayTMinimum = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MINIMUM];
		final float rayTMaximum = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM];
		
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
	
//	TODO: Add Javadocs!
	protected final float intersectionTShape3F() {
		boolean hasFoundIntersection = false;
		
		for(int index = 0; index < this.primitiveCount; index++) {
			final int primitiveArrayOffset = index * Primitive.ARRAY_SIZE;
			final int primitiveArrayOffsetBoundingVolumeID = primitiveArrayOffset + Primitive.ARRAY_OFFSET_BOUNDING_VOLUME_ID;
			final int primitiveArrayOffsetBoundingVolumeOffset = primitiveArrayOffset + Primitive.ARRAY_OFFSET_BOUNDING_VOLUME_OFFSET;
			final int primitiveArrayOffsetShapeID = primitiveArrayOffset + Primitive.ARRAY_OFFSET_SHAPE_ID;
			final int primitiveArrayOffsetShapeOffset = primitiveArrayOffset + Primitive.ARRAY_OFFSET_SHAPE_OFFSET;
			
			final int boundingVolumeID = this.primitiveArray[primitiveArrayOffsetBoundingVolumeID];
			final int boundingVolumeOffset = this.primitiveArray[primitiveArrayOffsetBoundingVolumeOffset];
			final int shapeID = this.primitiveArray[primitiveArrayOffsetShapeID];
			final int shapeOffset = this.primitiveArray[primitiveArrayOffsetShapeOffset];
			
			boolean isIntersectingBoundingVolume = false;
			
			if(boundingVolumeID == AxisAlignedBoundingBox3F.ID) {
				isIntersectingBoundingVolume = intersectsBoundingVolume3FAxisAlignedBoundingBox3F(boundingVolumeOffset);
			}
			
			if(boundingVolumeID == BoundingSphere3F.ID) {
				isIntersectingBoundingVolume = intersectsBoundingVolume3FBoundingSphere3F(boundingVolumeOffset);
			}
			
			if(boundingVolumeID == InfiniteBoundingVolume3F.ID) {
				isIntersectingBoundingVolume = true;
			}
			
			if(isIntersectingBoundingVolume) {
				float t = 0.0F;
				
				ray3FTransformWorldToObject(index);
				
				if(shapeID == Plane3F.ID) {
					t = intersectionTShape3FPlane3F(shapeOffset);
				}
				
				if(shapeID == RectangularCuboid3F.ID) {
					t = intersectionTShape3FRectangularCuboid3F(shapeOffset);
				}
				
				if(shapeID == Sphere3F.ID) {
					t = intersectionTShape3FSphere3F(shapeOffset);
				}
				
				if(shapeID == Torus3F.ID) {
					t = intersectionTShape3FTorus3F(shapeOffset);
				}
				
				ray3FTransformObjectToWorld(index);
				
				if(t > 0.0F && t < this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM]) {
					this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM] = t;
					
					hasFoundIntersection = true;
				}
			}
		}
		
		return hasFoundIntersection ? this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM] : 0.0F;
	}
	
//	TODO: Add Javadocs!
	protected final float intersectionTShape3FPlane3F(final int shape3FPlane3FArrayOffset) {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float rayOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float rayOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		final float rayDirectionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		final float rayTMinimum = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MINIMUM];
		final float rayTMaximum = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM];
		
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
	
//	TODO: Add Javadocs!
	protected final float intersectionTShape3FRectangularCuboid3F(final int shape3FRectangularCuboid3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float rayOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float rayOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		final float rayDirectionReciprocalX = 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionReciprocalY = 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionReciprocalZ = 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		final float rayTMinimum = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MINIMUM];
		final float rayTMaximum = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM];
		
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
	
//	TODO: Add Javadocs!
	protected final float intersectionTShape3FSphere3F(final int shape3FSphere3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float rayOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float rayOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		final float rayDirectionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		final float rayTMinimum = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MINIMUM];
		final float rayTMaximum = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM];
		
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
	
//	TODO: Add Javadocs!
	protected final float intersectionTShape3FTorus3F(final int shape3FTorus3FArrayOffset) {
//		Retrieve the ray variables:
		final float rayOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float rayOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float rayOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		final float rayDirectionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		final float rayTMinimum = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MINIMUM];
		final float rayTMaximum = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM];
		
//		Retrieve the torus variables:
		final float torusRadiusInner = this.shape3FTorus3FArray[shape3FTorus3FArrayOffset + Torus3F.ARRAY_OFFSET_RADIUS_INNER];
		final float torusRadiusInnerSquared = torusRadiusInner * torusRadiusInner;
		final float torusRadiusOuter = this.shape3FTorus3FArray[shape3FTorus3FArrayOffset + Torus3F.ARRAY_OFFSET_RADIUS_OUTER];
		final float torusRadiusOuterSquared = torusRadiusOuter * torusRadiusOuter;
		
//		Compute the variables used in the process of computing the variables for the quartic system:
		final float f0 = rayDirectionX * rayDirectionX + rayDirectionY * rayDirectionY + rayDirectionZ * rayDirectionZ;
		final float f1 = (rayOriginX * rayDirectionX + rayOriginY * rayDirectionY + rayOriginZ * rayDirectionZ) * 2.0F;
		final float f2 = torusRadiusInnerSquared;
		final float f3 = torusRadiusOuterSquared;
		final float f4 = (rayOriginX * rayOriginX + rayOriginY * rayOriginY + rayOriginZ * rayOriginZ) - f2 - f3;
		final float f5 = rayDirectionZ;
		final float f6 = rayOriginZ;
		
//		Compute the variables for the quartic system:
		final float a = f0 * f0;
		final float b = f0 * 2.0F * f1;
		final float c = f1 * f1 + 2.0F * f0 * f4 + 4.0F * f3 * f5 * f5;
		final float d = f1 * 2.0F * f4 + 8.0F * f3 * f6 * f5;
		final float e = f4 * f4 + 4.0F * f3 * f6 * f6 - 4.0F * f3 * f2;
		
//		Compute the intersection by solving the quartic system and checking the valid intersection interval:
		final float t = solveQuarticSystem(a, b, c, d, e, rayTMinimum, rayTMaximum);
		
		return t;
	}
	
//	TODO: Add Javadocs!
	protected final float intersectionTShape3FTriangle3F() {
//		Retrieve the ray variables that will be referred to by 'rayOrigin' and 'rayDirection' in the comments:
		final float rayOriginX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
		final float rayOriginY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
		final float rayOriginZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
		final float rayDirectionX = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
		final float rayDirectionY = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
		final float rayDirectionZ = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
		final float rayTMinimum = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MINIMUM];
		final float rayTMaximum = this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM];
		
//		Retrieve the triangle variables that will be referred to by 'triangleAPosition', 'triangleBPosition' and 'triangleCPosition' in the comments:
		final float triangleAPositionX = +0.0F;
		final float triangleAPositionY = +1.0F;
		final float triangleAPositionZ = +5.0F;
		final float triangleBPositionX = +1.0F;
		final float triangleBPositionY = -1.0F;
		final float triangleBPositionZ = +5.0F;
		final float triangleCPositionX = -1.0F;
		final float triangleCPositionY = -1.0F;
		final float triangleCPositionZ = +5.0F;
		
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
		
//		Compute variables necessary for computing the texture coordinates:
		final float aX = isX ? planeAY      : isY ? planeAZ      : planeAX;
		final float aY = isX ? planeAZ      : isY ? planeAX      : planeAY;
		final float bX = isX ? planeCY - aX : isY ? planeCZ - aX : planeCX - aX;
		final float bY = isX ? planeCZ - aY : isY ? planeCX - aY : planeCY - aY;
		final float cX = isX ? planeBY - aX : isY ? planeBZ - aX : planeBX - aX;
		final float cY = isX ? planeBZ - aY : isY ? planeBX - aY : planeBY - aY;
		
//		Compute variables necessary for computing the texture coordinates:
		final float determinant = bX * cY - bY * cX;
		final float determinantReciprocal = 1.0F / determinant;
		
//		Compute variables necessary for computing the texture coordinates:
		final float u = isX ? surfaceIntersectionPointY : isY ? surfaceIntersectionPointZ : surfaceIntersectionPointX;
		final float v = isX ? surfaceIntersectionPointZ : isY ? surfaceIntersectionPointX : surfaceIntersectionPointY;
		
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
		final float textureCoordinatesV = (asin(saturate(surfaceIntersectionPointZ / torusRadiusInner, -1.0F, 1.0F)) + PI_DIVIDED_BY_2) * PI_RECIPROCAL;
		
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
	protected final void ray3FTransformObjectToWorld(final int primitiveIndex) {
		doRay3FTransform(primitiveIndex * Matrix44F.ARRAY_SIZE * 2);
	}
	
//	TODO: Add Javadocs!
	protected final void ray3FTransformWorldToObject(final int primitiveIndex) {
		doRay3FTransform(primitiveIndex * Matrix44F.ARRAY_SIZE * 2 + Matrix44F.ARRAY_SIZE);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Image doGetImage() {
		return getRendererConfiguration().getImage();
	}
	
	private Scene doGetScene() {
		return getRendererConfiguration().getScene();
	}
	
	private float[] doGetPixelArray() {
		return getAndReturn(this.pixelArray);
	}
	
	private float[] doGetRadianceRGBArray() {
		return getAndReturn(this.radianceRGBArray);
	}
	
	private void doRay3FTransform(final int matrix44FArrayOffset) {
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
		point3FTransform(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, oldOriginX, oldOriginY, oldOriginZ);
		
//		Transform the ray direction from the old space to the new space:
		vector3FTransform(element11, element12, element13, element21, element22, element23, element31, element32, element33, oldDirectionX, oldDirectionY, oldDirectionZ);
		
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
		if(newTMinimum > 0.0F && newTMinimum < Float.MAX_VALUE) {
//			Compute a reference point in the old space:
			final float oldReferencePointTMinimumX = oldOriginX + oldDirectionX * oldTMinimum;
			final float oldReferencePointTMinimumY = oldOriginY + oldDirectionY * oldTMinimum;
			final float oldReferencePointTMinimumZ = oldOriginZ + oldDirectionZ * oldTMinimum;
			
//			Transform the reference point from the old space to the new space:
			point3FTransform(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, oldReferencePointTMinimumX, oldReferencePointTMinimumY, oldReferencePointTMinimumZ);
			
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
		if(newTMaximum > 0.0F && newTMaximum < Float.MAX_VALUE) {
//			Compute a reference point in the old space:
			final float oldReferencePointTMaximumX = oldOriginX + oldDirectionX * oldTMaximum;
			final float oldReferencePointTMaximumY = oldOriginY + oldDirectionY * oldTMaximum;
			final float oldReferencePointTMaximumZ = oldOriginZ + oldDirectionZ * oldTMaximum;
			
//			Transform the reference point from the old space to the new space:
			point3FTransform(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, oldReferencePointTMaximumX, oldReferencePointTMaximumY, oldReferencePointTMaximumZ);
			
//			Retrieve the reference point from the new space:
			final float newReferencePointTMaximumX = this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_1];
			final float newReferencePointTMaximumY = this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_2];
			final float newReferencePointTMaximumZ = this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_3];
			
//			Compute the distance from the origin in the new space to the reference point in the new space:
			final float distanceOriginToReferencePointTMaximum = point3FDistance(newOriginX, newOriginY, newOriginZ, newReferencePointTMaximumX, newReferencePointTMaximumY, newReferencePointTMaximumZ);
			
//			Update the new maximum ray boundary:
			newTMinimum = abs(distanceOriginToReferencePointTMaximum);
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
	
	private void doSetupRadianceRGBArray() {
		put(this.radianceRGBArray = Floats.array(getResolution() * 3, 0.0F));
	}
	
	private void doSetupScene() {
		final CompiledScene compiledScene = SceneCompiler.compile(doGetScene());
		
		put(this.boundingVolume3FAxisAlignedBoundingBox3FArray = compiledScene.getBoundingVolume3FAxisAlignedBoundingBox3FArray());
		put(this.boundingVolume3FBoundingSphere3FArray = compiledScene.getBoundingVolume3FBoundingSphere3FArray());
		put(this.cameraArray = compiledScene.getCameraArray());
		put(this.matrix44FArray = compiledScene.getMatrix44FArray());
		put(this.primitiveArray = compiledScene.getPrimitiveArray());
		put(this.shape3FPlane3FArray = compiledScene.getShape3FPlane3FArray());
		put(this.shape3FRectangularCuboid3FArray = compiledScene.getShape3FRectangularCuboid3FArray());
		put(this.shape3FSphere3FArray = compiledScene.getShape3FSphere3FArray());
		put(this.shape3FTorus3FArray = compiledScene.getShape3FTorus3FArray());
		put(this.shape3FTriangle3FArray = compiledScene.getShape3FTriangle3FArray());
		
		this.primitiveCount = compiledScene.getPrimitiveCount();
	}
}