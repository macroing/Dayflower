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
import static org.dayflower.simplex.Color.color4D;
import static org.dayflower.simplex.Image.image4D;
import static org.dayflower.simplex.Image.image4DSave;
import static org.dayflower.simplex.Image.image4DSetColor4D;
import static org.dayflower.simplex.Matrix.matrix44DInverse;
import static org.dayflower.simplex.Matrix.matrix44DRotateX;
import static org.dayflower.simplex.OrthonormalBasis.orthonormalBasis33D;
import static org.dayflower.simplex.Point.point2DGetU;
import static org.dayflower.simplex.Point.point2DGetV;
import static org.dayflower.simplex.Point.point3D;
import static org.dayflower.simplex.Point.point3DAdd;
import static org.dayflower.simplex.Ray.ray3DGetDirection;
import static org.dayflower.simplex.Ray.ray3DGetOrigin;
import static org.dayflower.simplex.Ray.ray3DTransformMatrix44D;
import static org.dayflower.simplex.Shape.cone3D;
import static org.dayflower.simplex.Shape.cylinder3D;
import static org.dayflower.simplex.Shape.disk3D;
import static org.dayflower.simplex.Shape.paraboloid3D;
import static org.dayflower.simplex.Shape.plane3D;
import static org.dayflower.simplex.Shape.polygon3D;
import static org.dayflower.simplex.Shape.rectangle3D;
import static org.dayflower.simplex.Shape.rectangularCuboid3D;
import static org.dayflower.simplex.Shape.shape3DComputeSurfaceNormal;
import static org.dayflower.simplex.Shape.shape3DComputeTextureCoordinates;
import static org.dayflower.simplex.Shape.shape3DIntersection;
import static org.dayflower.simplex.Shape.sphere3D;
import static org.dayflower.simplex.Shape.torus3D;
import static org.dayflower.simplex.Shape.triangle3D;
import static org.dayflower.simplex.Vector.vector3DDirection;
import static org.dayflower.simplex.Vector.vector3DDotProduct;
import static org.dayflower.simplex.Vector.vector3DLength;
import static org.dayflower.utility.Doubles.abs;
import static org.dayflower.utility.Doubles.cos;
import static org.dayflower.utility.Doubles.fractionalPart;
import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.remainder;
import static org.dayflower.utility.Doubles.sin;
import static org.dayflower.utility.Doubles.toRadians;

public final class Main {
	private Main() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
		doRender("./generated/Simplex/Cone.png", 270.0D, cone3D());
		doRender("./generated/Simplex/Cylinder.png", 270.0D, cylinder3D());
		doRender("./generated/Simplex/Disk.png", 0.0D, disk3D());
		doRender("./generated/Simplex/Paraboloid.png", 270.0D, paraboloid3D());
		doRender("./generated/Simplex/Plane.png", 0.0D, plane3D(point3D(0.0D, -1.0D, 0.0D), point3D(0.0D, -1.0D, 1.0D), point3D(1.0D, -1.0D, 0.0D)));
		doRender("./generated/Simplex/Polygon.png", 0.0D, polygon3D());
		doRender("./generated/Simplex/Rectangle.png", 0.0D, rectangle3D());
		doRender("./generated/Simplex/RectangularCuboid.png", 0.0D, rectangularCuboid3D());
		doRender("./generated/Simplex/Sphere.png", 270.0D, sphere3D());
		doRender("./generated/Simplex/Torus.png", 0.0D, torus3D());
		doRender("./generated/Simplex/Triangle.png", 0.0D, triangle3D());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static double[] doColor4D(final double[] point2DTextureCoordinates, final double[] point3DSurfaceIntersectionPoint, final double[] vector3DDirection, final double[] vector3DSurfaceNormal) {
		final boolean isBullseye = false;
		final boolean isCheckerboard = true;
		
		final double[] color4DBullseye = doColor4DBullseye(point3DSurfaceIntersectionPoint);
		final double[] color4DCheckerboard = doColor4DCheckerboard(point2DTextureCoordinates);
		final double[] color4DDotProduct = doColor4DDotProduct(vector3DDirection, vector3DSurfaceNormal);
		
		final double component1 = color4DDotProduct[0] * ((isBullseye ? color4DBullseye[0] : 0.0D) + (isCheckerboard ? color4DCheckerboard[0] : 0.0D));
		final double component2 = color4DDotProduct[1] * ((isBullseye ? color4DBullseye[1] : 0.0D) + (isCheckerboard ? color4DCheckerboard[1] : 0.0D));
		final double component3 = color4DDotProduct[2] * ((isBullseye ? color4DBullseye[2] : 0.0D) + (isCheckerboard ? color4DCheckerboard[2] : 0.0D));
		
		return color4D(component1, component2, component3);
	}
	
	private static double[] doColor4DBullseye(final double[] point3DSurfaceIntersectionPoint) {
		final double[] vector3DDirection = vector3DDirection(point3D(10.0D, 10.0D, 10.0D), point3DSurfaceIntersectionPoint);
		
		final double directionLength = vector3DLength(vector3DDirection);
		final double directionLengthScaled = directionLength * 4.0D;
		final double directionLengthScaledRemainder = remainder(directionLengthScaled, 1.0D);
		
		final boolean is = directionLengthScaledRemainder > 0.5D;
		
		final double component1 = is ? 1.0D : 0.0D;
		final double component2 = is ? 0.0D : 1.0D;
		final double component3 = is ? 0.0D : 0.0D;
		
		return color4D(component1, component2, component3);
	}
	
	private static double[] doColor4DCheckerboard(final double[] point2DTextureCoordinates) {
		final double angle = toRadians(0.0D);
		final double angleCos = cos(angle);
		final double angleSin = sin(angle);
		
		final double scaleU = 4.0D;
		final double scaleV = 4.0D;
		
		final double u = point2DGetU(point2DTextureCoordinates);
		final double v = point2DGetV(point2DTextureCoordinates);
		
		final boolean isU = fractionalPart((u * angleCos - v * angleSin) * scaleU) > 0.5D;
		final boolean isV = fractionalPart((v * angleCos + u * angleSin) * scaleV) > 0.5D;
		final boolean is = isU ^ isV;
		
		final double component1 = is ? 1.0D : 0.0D;
		final double component2 = is ? 0.0D : 1.0D;
		final double component3 = is ? 0.0D : 0.0D;
		
		return color4D(component1, component2, component3);
	}
	
	private static double[] doColor4DDotProduct(final double[] vector3DDirection, final double[] vector3DSurfaceNormal) {
		final double directionDotSurfaceNormal = abs(vector3DDotProduct(vector3DDirection, vector3DSurfaceNormal));
		
		final double component = 0.5D * directionDotSurfaceNormal;
		
		return color4D(component);
	}
	
	private static void doRender(final String pathname, final double angle, final double[] shape3D) {
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
				
				final double t = shape3DIntersection(ray3DTransformed, shape3D);
				
				if(!isNaN(t)) {
					final double[] point2DTextureCoordinates = shape3DComputeTextureCoordinates(ray3DTransformed, shape3D, t);
					
					final double[] vector3DDirection = ray3DGetDirection(ray3DTransformed);
					final double[] vector3DSurfaceNormal = shape3DComputeSurfaceNormal(ray3DTransformed, shape3D, t);
					
					final double[] point3DOrigin = ray3DGetOrigin(ray3DTransformed);
					final double[] point3DSurfaceIntersectionPoint = point3DAdd(point3DOrigin, vector3DDirection, t);
					
					final double[] color4D = doColor4D(point2DTextureCoordinates, point3DSurfaceIntersectionPoint, vector3DDirection, vector3DSurfaceNormal);
					
					image4DSetColor4D(image4D, color4D, x, y);
				}
			}
		}
		
		final long elapsedTimeMillis = System.currentTimeMillis() - currentTimeMillis;
		
		System.out.println(elapsedTimeMillis);
		
		image4DSave(image4D, pathname);
	}
}