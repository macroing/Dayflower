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
package org.dayflower.simplex;

import static org.dayflower.simplex.Color.color4D;
import static org.dayflower.simplex.Image.image4DGetColor4D;
import static org.dayflower.simplex.Image.image4DGetResolutionX;
import static org.dayflower.simplex.Image.image4DGetResolutionY;
import static org.dayflower.simplex.Image.image4DLoad;
import static org.dayflower.simplex.Point.point2DGetU;
import static org.dayflower.simplex.Point.point2DGetV;
import static org.dayflower.simplex.Point.point2DGetX;
import static org.dayflower.simplex.Point.point2DGetY;
import static org.dayflower.simplex.Point.point2DRotate;
import static org.dayflower.simplex.Point.point2DScale;
import static org.dayflower.simplex.Point.point2DWrapAround;
import static org.dayflower.simplex.Point.point3D;
import static org.dayflower.simplex.Vector.vector2D;
import static org.dayflower.simplex.Vector.vector3DDirection;
import static org.dayflower.simplex.Vector.vector3DDotProduct;
import static org.dayflower.simplex.Vector.vector3DLength;
import static org.dayflower.utility.Doubles.cos;
import static org.dayflower.utility.Doubles.fractionalPart;
import static org.dayflower.utility.Doubles.remainder;
import static org.dayflower.utility.Doubles.saturate;
import static org.dayflower.utility.Doubles.sin;
import static org.dayflower.utility.Doubles.toRadians;
import static org.dayflower.utility.Ints.toInt;

import java.lang.reflect.Field;//TODO: Add Javadocs and refactor!

/**
 * A class that consists exclusively of static methods that returns or performs various operations on textures.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Texture {
	private static final double[] IMAGE_4_D = image4DLoad("./resources/textures/bricks2.jpg");//image4DLoad("./generated/Test/Image-Original.jpg");//TODO: Refactor!
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Texture() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Refactor!
	public static double[] bullseyeTextureGetColor4D(final double[] point3DSurfaceIntersectionPoint) {
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
	
//	TODO: Refactor!
	public static double[] checkerboardTextureGetColor4D(final double[] point2DTextureCoordinates) {
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
	
//	TODO: Refactor!
	public static double[] dotProductTextureGetColor4D(final double[] vector3DDirection, final double[] vector3DSurfaceNormal) {
		final double directionDotSurfaceNormal = -vector3DDotProduct(vector3DDirection, vector3DSurfaceNormal);
		
		final double component = saturate(directionDotSurfaceNormal);
		
		return color4D(component);
	}
	
//	TODO: Refactor!
	public static double[] image4DTextureGetColor4D(final double[] point2DTextureCoordinates, final double scale) {
		return image4DTextureGetColor4D(point2DTextureCoordinates, scale, IMAGE_4_D, true);
	}
	
//	TODO: Refactor!
	public static double[] image4DTextureGetColor4D(final double[] point2DTextureCoordinates, final double scale, final double[] image4D, final boolean isInterpolating) {
		final double angle = toRadians(0.0D);
		
		final double[] vector2DScale = vector2D(scale, scale);
		
		final int resolutionX = image4DGetResolutionX(image4D);
		final int resolutionY = image4DGetResolutionY(image4D);
		
		final double[] point2DTextureCoordinatesRotated = point2DRotate(point2DTextureCoordinates, angle);
		final double[] point2DTextureCoordinatesScaled = point2DScale(point2DTextureCoordinatesRotated, vector2DScale);
		final double[] point2DTextureCoordinatesWrappedAround = point2DWrapAround(point2DTextureCoordinatesScaled, resolutionX, resolutionY);
		
		if(isInterpolating) {
			final double x = point2DGetX(point2DTextureCoordinatesWrappedAround);
			final double y = point2DGetY(point2DTextureCoordinatesWrappedAround);
			
			return image4DGetColor4D(image4D, x, y);
		}
		
		final int x = toInt(point2DGetX(point2DTextureCoordinatesWrappedAround));
		final int y = toInt(point2DGetY(point2DTextureCoordinatesWrappedAround));
		
		return image4DGetColor4D(image4D, x, y);
	}
}