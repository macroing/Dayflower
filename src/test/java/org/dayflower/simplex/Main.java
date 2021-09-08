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
package org.dayflower.simplex;

import static org.dayflower.simplex.Camera.camera3D;
import static org.dayflower.simplex.Camera.camera3DCreatePrimaryRay;
import static org.dayflower.simplex.Color.color4DGetComponent1;
import static org.dayflower.simplex.Color.color4DGetComponent2;
import static org.dayflower.simplex.Color.color4DGetComponent3;
import static org.dayflower.simplex.Color.color4DMultiply;
import static org.dayflower.simplex.Image.image4D;
import static org.dayflower.simplex.Image.image4DGetColor4D;
import static org.dayflower.simplex.Image.image4DGetResolutionX;
import static org.dayflower.simplex.Image.image4DGetResolutionY;
import static org.dayflower.simplex.Image.image4DSave;
import static org.dayflower.simplex.Image.image4DSetColor4D;
import static org.dayflower.simplex.Matrix.matrix44DInverse;
import static org.dayflower.simplex.Matrix.matrix44DRotateX;
import static org.dayflower.simplex.OrthonormalBasis.orthonormalBasis33D;
import static org.dayflower.simplex.OrthonormalBasis.orthonormalBasis33DFromShape3D;
import static org.dayflower.simplex.Point.point2D;
import static org.dayflower.simplex.Point.point2DGetX;
import static org.dayflower.simplex.Point.point2DGetY;
import static org.dayflower.simplex.Point.point2DRotate;
import static org.dayflower.simplex.Point.point2DScale;
import static org.dayflower.simplex.Point.point2DTextureCoordinatesShape3D;
import static org.dayflower.simplex.Point.point2DWrapAround;
import static org.dayflower.simplex.Point.point3D;
import static org.dayflower.simplex.Ray.ray3DGetDirection;
import static org.dayflower.simplex.Ray.ray3DIntersectionShape3D;
import static org.dayflower.simplex.Ray.ray3DTransformMatrix44D;
import static org.dayflower.simplex.Shape.cone3D;
import static org.dayflower.simplex.Shape.cylinder3D;
import static org.dayflower.simplex.Shape.disk3D;
import static org.dayflower.simplex.Shape.hyperboloid3D;
import static org.dayflower.simplex.Shape.paraboloid3D;
import static org.dayflower.simplex.Shape.plane3D;
import static org.dayflower.simplex.Shape.polygon3D;
import static org.dayflower.simplex.Shape.rectangle3D;
import static org.dayflower.simplex.Shape.rectangularCuboid3D;
import static org.dayflower.simplex.Shape.sphere3D;
import static org.dayflower.simplex.Shape.torus3D;
import static org.dayflower.simplex.Shape.triangle3D;
import static org.dayflower.simplex.Texture.dotProductTextureGetColor4D;
import static org.dayflower.simplex.Texture.image4DTextureGetColor4D;
import static org.dayflower.simplex.Vector.vector2D;
import static org.dayflower.simplex.Vector.vector3D;
import static org.dayflower.simplex.Vector.vector3DGetX;
import static org.dayflower.simplex.Vector.vector3DGetY;
import static org.dayflower.simplex.Vector.vector3DGetZ;
import static org.dayflower.simplex.Vector.vector3DMultiply;
import static org.dayflower.simplex.Vector.vector3DNegate;
import static org.dayflower.simplex.Vector.vector3DNormalize;
import static org.dayflower.simplex.Vector.vector3DTransformOrthonormalBasis33D;
import static org.dayflower.simplex.Vector.vector3DTransformReverseOrthonormalBasis33D;
import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.toRadians;

public final class Main {
//	private static final double[] IMAGE_4_D_ALBEDO_MAP = Image.image4DLoad("./resources/textures/bricks2.jpg");
	private static final double[] IMAGE_4_D_DISPLACEMENT_MAP = Image.image4DLoad("./resources/textures/bricks2_disp.jpg");
	private static final double[] IMAGE_4_D_NORMAL_MAP = Image.image4DLoad("./resources/textures/bricks2_normal.jpg");
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Main() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
		doRender("./generated/Simplex/Cone.png", 270.0D, cone3D(), 4.0D);
		doRender("./generated/Simplex/Cylinder.png", 270.0D, cylinder3D(), 4.0D);
		doRender("./generated/Simplex/Disk.png", 0.0D, disk3D(), 2.0D);
		doRender("./generated/Simplex/Hyperboloid.png", 270.0D, hyperboloid3D(), 4.0D);
		doRender("./generated/Simplex/Paraboloid.png", 270.0D, paraboloid3D(), 4.0D);
		doRender("./generated/Simplex/Plane.png", 0.0D, plane3D(point3D(0.0D, -1.0D, 0.0D), point3D(0.0D, -1.0D, 1.0D), point3D(1.0D, -1.0D, 0.0D)), 0.1D);
		doRender("./generated/Simplex/Polygon.png", 0.0D, polygon3D(), 4.0D);
		doRender("./generated/Simplex/Rectangle.png", 0.0D, rectangle3D(), 4.0D);
		doRender("./generated/Simplex/RectangularCuboid.png", 0.0D, rectangularCuboid3D(), 4.0D);
		doRender("./generated/Simplex/Sphere.png", 270.0D, sphere3D(), 4.0D);
		doRender("./generated/Simplex/Torus.png", 0.0D, torus3D(), 4.0D);
		doRender("./generated/Simplex/Triangle.png", 0.0D, triangle3D(), 4.0D);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static double[] doComputeColor4D(final double[] image4D, final double[] point2DTextureCoordinates, final double scale) {
		final double angle = toRadians(0.0D);
		
		final double[] vector2DScale = vector2D(scale, scale);
		
		final int resolutionX = image4DGetResolutionX(image4D);
		final int resolutionY = image4DGetResolutionY(image4D);
		
		final double[] point2DTextureCoordinatesRotated = point2DRotate(point2DTextureCoordinates, angle);
		final double[] point2DTextureCoordinatesScaled = point2DScale(point2DTextureCoordinatesRotated, vector2DScale);
		final double[] point2DTextureCoordinatesWrappedAround = point2DWrapAround(point2DTextureCoordinatesScaled, resolutionX, resolutionY);
		
		final double x = point2DGetX(point2DTextureCoordinatesWrappedAround);
		final double y = point2DGetY(point2DTextureCoordinatesWrappedAround);
		
		final double[] color4D = image4DGetColor4D(image4D, x, y);
		
		return color4D;
	}
	
	private static double[] doComputeParallaxMapping(final double[] orthonormalBasis33D, final double[] point2DTextureCoordinates, final double[] vector3DDirection, final double scale) {
		final double[] color4D = doComputeColor4D(IMAGE_4_D_DISPLACEMENT_MAP, point2DTextureCoordinates, scale);
		
		final double height = color4DGetComponent1(color4D);
		final double heightScale = 0.1D;
		
		final double[] vector3D = vector3DNormalize(vector3DTransformReverseOrthonormalBasis33D(vector3DDirection, orthonormalBasis33D));
		
		final double u = point2DGetX(point2DTextureCoordinates) + (vector3DGetX(vector3D) / vector3DGetZ(vector3D) * (height * heightScale));
		final double v = point2DGetY(point2DTextureCoordinates) + (vector3DGetY(vector3D) / vector3DGetZ(vector3D) * (height * heightScale));
		
		return point2D(u, v);
		
		/*
		final double r = color4DGetComponent1(color4D);
		
		final double[] vector3D = vector3DNormalize(vector3DTransformReverseOrthonormalBasis33D(vector3DDirection, orthonormalBasis33D));
		
		final double offset = -1.0D;
		final double scale = 0.04D;
		final double scaleHalf = scale * 0.5D;
		final double bias = -scaleHalf + scaleHalf * offset;
		
		final double u = point2DGetX(point2DTextureCoordinates) + vector3DGetX(vector3D) * (r * scale + bias);
		final double v = point2DGetY(point2DTextureCoordinates) + vector3DGetY(vector3D) * (r * scale + bias);
		
		return point2D(u, v);
		*/
	}
	
	/*
		vec2 ParallaxMapping(vec2 texCoords, vec3 viewDir) { 
//			const float numLayers = 10;
//			
//			float layerDepth = 1.0 / numLayers;
//			float currentLayerDepth = 0.0;
//			
//			vec2 P = viewDir.xy * height_scale; 
//			vec2 deltaTexCoords = P / numLayers;
//			
//			vec2  currentTexCoords = texCoords;
//			
//			float currentDepthMapValue = texture(depthMap, currentTexCoords).r;
//			
			while(currentLayerDepth < currentDepthMapValue) {
				currentTexCoords -= deltaTexCoords;
				currentDepthMapValue = texture(depthMap, currentTexCoords).r;  
				currentLayerDepth += layerDepth;  
			}
			
			return currentTexCoords;
		}
	 */
	
	private static double[] doComputeSteepParallaxMapping(final double[] orthonormalBasis33D, final double[] point2DTextureCoordinates, final double[] vector3DDirection, final double scale) {
		final double layers = 10.0D;
		final double layersReciprocal = 1.0D / layers;
		
		final double heightScale = 0.1D;
		
		final double[] vector3D = vector3DNormalize(vector3DTransformReverseOrthonormalBasis33D(vector3DDirection, orthonormalBasis33D));
		final double[] vector3DScaled = vector3DMultiply(vector3D, heightScale);
		
		final double[] point2DTextureCoordinatesDelta = point2D(vector3DGetX(vector3DScaled) / layers, vector3DGetY(vector3DScaled) / layers);
		final double[] point2DTextureCoordinatesCurrent = point2D(point2DGetX(point2DTextureCoordinates), point2DGetY(point2DTextureCoordinates));
		
		double currentHeight = color4DGetComponent1(doComputeColor4D(IMAGE_4_D_DISPLACEMENT_MAP, point2DTextureCoordinatesCurrent, scale));
		double currentLayerDepth = 0.0D;
		
		while(currentLayerDepth < currentHeight) {
			point2DTextureCoordinatesCurrent[0] -= point2DTextureCoordinatesDelta[0];
			point2DTextureCoordinatesCurrent[1] -= point2DTextureCoordinatesDelta[1];
			
			currentHeight = color4DGetComponent1(doComputeColor4D(IMAGE_4_D_DISPLACEMENT_MAP, point2DTextureCoordinatesCurrent, scale));
			currentLayerDepth += layersReciprocal;
		}
		
		return point2DTextureCoordinatesCurrent;
	}
	
	private static double[] doComputeSurfaceNormal(final double[] orthonormalBasis33D, final double[] point2DTextureCoordinates, final double scale) {
		final double[] color4D = doComputeColor4D(IMAGE_4_D_NORMAL_MAP, point2DTextureCoordinates, scale);
		
		final double r = color4DGetComponent1(color4D);
		final double g = color4DGetComponent2(color4D);
		final double b = color4DGetComponent3(color4D);
		
		final double[] vector3D = vector3D(r * 2.0D - 1.0D, g * 2.0D - 1.0D, b * 2.0D - 1.0D);
		
		final double[] vector3DSurfaceNormal = vector3DNormalize(vector3DTransformOrthonormalBasis33D(vector3D, orthonormalBasis33D));
		
		return vector3DSurfaceNormal;
	}
	
	private static void doRender(final String pathname, final double angle, final double[] shape3D, final double scale) {
		final double[] camera3D = camera3D(toRadians(40.0D), toRadians(40.0D), 0, orthonormalBasis33D(), point3D(0.0D, 0.0D, -5.0D), 0.0D, 30.0D, 800.0D, 800.0D);
		
		final double[] matrix44DObjectToWorld = matrix44DRotateX(toRadians(angle));
		final double[] matrix44DWorldToObject = matrix44DInverse(matrix44DObjectToWorld);
		
		final int resolutionX = 800;
		final int resolutionY = 800;
		
		final double[] image4D = image4D(resolutionX, resolutionY);
		
		final long currentTimeMillis = System.currentTimeMillis();
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++) {
				final double[] ray3D = camera3DCreatePrimaryRay(camera3D, x, y);
				final double[] ray3DTransformed = ray3DTransformMatrix44D(matrix44DWorldToObject, ray3D);
				
				final double t = ray3DIntersectionShape3D(ray3DTransformed, shape3D);
				
				if(!isNaN(t)) {
					final double[] orthonormalBasis33D = orthonormalBasis33DFromShape3D(ray3DTransformed, shape3D, t);
					
					final double[] vector3DDirection = ray3DGetDirection(ray3DTransformed);
					final double[] vector3DDirectionNegated = vector3DNegate(vector3DDirection);
					
//					final double[] point2DTextureCoordinates = point2DTextureCoordinatesShape3D(ray3DTransformed, shape3D, t);
					final double[] point2DTextureCoordinates = doComputeSteepParallaxMapping(orthonormalBasis33D, point2DTextureCoordinatesShape3D(ray3DTransformed, shape3D, t), vector3DDirectionNegated, scale);
					
//					final double[] vector3DSurfaceNormal = vector3DSurfaceNormalShape3D(ray3DTransformed, shape3D, t);
					final double[] vector3DSurfaceNormal = doComputeSurfaceNormal(orthonormalBasis33D, point2DTextureCoordinates, scale);
					
//					final double[] point3DOrigin = ray3DGetOrigin(ray3DTransformed);
//					final double[] point3DSurfaceIntersectionPoint = point3DAdd(point3DOrigin, vector3DDirection, t);
					
//					final double[] color4DSurfaceNormal = {(vector3DSurfaceNormal[0] + 1.0D) / 2.0D, (vector3DSurfaceNormal[1] + 1.0D) / 2.0D, (vector3DSurfaceNormal[2] + 1.0D) / 2.0D, 1.0D};
					final double[] color4D = color4DMultiply(dotProductTextureGetColor4D(vector3DDirection, vector3DSurfaceNormal), image4DTextureGetColor4D(point2DTextureCoordinates, scale));
					
					image4DSetColor4D(image4D, color4D, x, y);
				}
			}
		}
		
		final long elapsedTimeMillis = System.currentTimeMillis() - currentTimeMillis;
		
		System.out.println(elapsedTimeMillis);
		
		image4DSave(image4D, pathname);
	}
}