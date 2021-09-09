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

import static org.dayflower.simplex.Vector.vector2D;
import static org.dayflower.simplex.Vector.vector2DDirection;
import static org.dayflower.simplex.Vector.vector2DGetComponent1;
import static org.dayflower.simplex.Vector.vector2DGetComponent2;
import static org.dayflower.simplex.Vector.vector2DLength;
import static org.dayflower.simplex.Vector.vector2DLengthSquared;
import static org.dayflower.simplex.Vector.vector3D;
import static org.dayflower.simplex.Vector.vector3DDirection;
import static org.dayflower.simplex.Vector.vector3DDirectionNormalized;
import static org.dayflower.simplex.Vector.vector3DLength;
import static org.dayflower.simplex.Vector.vector3DLengthSquared;
import static org.dayflower.simplex.Vector.vector3DSphericalPhi;
import static org.dayflower.simplex.Vector.vector3DSphericalTheta;
import static org.dayflower.simplex.Vector.vector3DTripleProduct;
import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.utility.Doubles.PI_RECIPROCAL;
import static org.dayflower.utility.Doubles.cos;
import static org.dayflower.utility.Doubles.equal;
import static org.dayflower.utility.Doubles.isZero;
import static org.dayflower.utility.Doubles.lerp;
import static org.dayflower.utility.Doubles.max;
import static org.dayflower.utility.Doubles.min;
import static org.dayflower.utility.Doubles.positiveModulo;
import static org.dayflower.utility.Doubles.random;
import static org.dayflower.utility.Doubles.sin;
import static org.dayflower.utility.Doubles.sqrt;

import java.lang.reflect.Field;//TODO: Add Javadocs!

/**
 * A class that consists exclusively of static methods that returns or performs various operations on points.
 * <p>
 * This class currently supports the following:
 * <ul>
 * <li>{@code Point2D} - a 2-dimensional point represented by a {@code double[]}.</li>
 * <li>{@code Point3D} - a 3-dimensional point represented by a {@code double[]}.</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Point {
	private Point() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Point2D /////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static boolean point2DEquals(final double[] point2DA, final double[] point2DB) {
		return point2DEquals(point2DA, point2DB, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static boolean point2DEquals(final double[] point2DA, final double[] point2DB, final int point2DAOffset, final int point2DBOffset) {
		final double component1A = point2DGetComponent1(point2DA, point2DAOffset);
		final double component2A = point2DGetComponent2(point2DA, point2DAOffset);
		
		final double component1B = point2DGetComponent1(point2DB, point2DBOffset);
		final double component2B = point2DGetComponent2(point2DB, point2DBOffset);
		
		final boolean equals = equal(component1A, component1B) && equal(component2A, component2B);
		
		return equals;
	}
	
//	TODO: Add Javadocs!
	public static double point2DDistance(final double[] point2DEye, final double[] point2DTarget) {
		return point2DDistance(point2DEye, point2DTarget, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double point2DDistance(final double[] point2DEye, final double[] point2DTarget, final int point2DEyeOffset, final int point2DTargetOffset) {
		return vector2DLength(vector2DDirection(point2DEye, point2DTarget, vector2D(), point2DEyeOffset, point2DTargetOffset, 0));
	}
	
//	TODO: Add Javadocs!
	public static double point2DDistanceSquared(final double[] point2DEye, final double[] point2DTarget) {
		return point2DDistanceSquared(point2DEye, point2DTarget, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double point2DDistanceSquared(final double[] point2DEye, final double[] point2DTarget, final int point2DEyeOffset, final int point2DTargetOffset) {
		return vector2DLengthSquared(vector2DDirection(point2DEye, point2DTarget, vector2D(), point2DEyeOffset, point2DTargetOffset, 0));
	}
	
//	TODO: Add Javadocs!
	public static double point2DGetComponent1(final double[] point2D) {
		return point2DGetComponent1(point2D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double point2DGetComponent1(final double[] point2D, final int point2DOffset) {
		return point2D[point2DOffset + 0];
	}
	
//	TODO: Add Javadocs!
	public static double point2DGetComponent2(final double[] point2D) {
		return point2DGetComponent2(point2D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double point2DGetComponent2(final double[] point2D, final int point2DOffset) {
		return point2D[point2DOffset + 1];
	}
	
//	TODO: Add Javadocs!
	public static double point2DGetU(final double[] point2D) {
		return point2DGetU(point2D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double point2DGetU(final double[] point2D, final int point2DOffset) {
		return point2D[point2DOffset + 0];
	}
	
//	TODO: Add Javadocs!
	public static double point2DGetV(final double[] point2D) {
		return point2DGetV(point2D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double point2DGetV(final double[] point2D, final int point2DOffset) {
		return point2D[point2DOffset + 1];
	}
	
//	TODO: Add Javadocs!
	public static double point2DGetX(final double[] point2D) {
		return point2DGetX(point2D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double point2DGetX(final double[] point2D, final int point2DOffset) {
		return point2D[point2DOffset + 0];
	}
	
//	TODO: Add Javadocs!
	public static double point2DGetY(final double[] point2D) {
		return point2DGetY(point2D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double point2DGetY(final double[] point2D, final int point2DOffset) {
		return point2D[point2DOffset + 1];
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with two components.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Point.point2D(0.0D, 0.0D);
	 * }
	 * </pre>
	 * 
	 * @return a {@code double[]} that contains a point with two components
	 */
	public static double[] point2D() {
		return point2D(0.0D, 0.0D);
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with two components.
	 * 
	 * @param component1 the value of component 1, also known as X or U
	 * @param component2 the value of component 2, also known as Y or V
	 * @return a {@code double[]} that contains a point with two components
	 */
	public static double[] point2D(final double component1, final double component2) {
		return new double[] {component1, component2};
	}
	
//	TODO: Add Javadocs!
	public static double[] point2DFromBarycentricCoordinates(final double[] point2DA, final double[] point2DB, final double[] point2DC, final double[] point3DBarycentricCoordinates) {
		return point2DFromBarycentricCoordinates(point2DA, point2DB, point2DC, point3DBarycentricCoordinates, point2D());
	}
	
//	TODO: Add Javadocs!
	public static double[] point2DFromBarycentricCoordinates(final double[] point2DA, final double[] point2DB, final double[] point2DC, final double[] point3DBarycentricCoordinates, final double[] point2DResult) {
		return point2DFromBarycentricCoordinates(point2DA, point2DB, point2DC, point3DBarycentricCoordinates, point2DResult, 0, 0, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] point2DFromBarycentricCoordinates(final double[] point2DA, final double[] point2DB, final double[] point2DC, final double[] point3DBarycentricCoordinates, final double[] point2DResult, final int point2DAOffset, final int point2DBOffset, final int point2DCOffset, final int point3DBarycentricCoordinatesOffset, final int point2DResultOffset) {
		final double aU = point2DGetU(point2DA, point2DAOffset);
		final double aV = point2DGetV(point2DA, point2DAOffset);
		
		final double bU = point2DGetU(point2DB, point2DBOffset);
		final double bV = point2DGetV(point2DB, point2DBOffset);
		
		final double cU = point2DGetU(point2DC, point2DCOffset);
		final double cV = point2DGetV(point2DC, point2DCOffset);
		
		final double barycentricCoordinatesU = point3DGetU(point3DBarycentricCoordinates, point3DBarycentricCoordinatesOffset);
		final double barycentricCoordinatesV = point3DGetV(point3DBarycentricCoordinates, point3DBarycentricCoordinatesOffset);
		final double barycentricCoordinatesW = point3DGetW(point3DBarycentricCoordinates, point3DBarycentricCoordinatesOffset);
		
		final double component1 = aU * barycentricCoordinatesU + bU * barycentricCoordinatesV + cU * barycentricCoordinatesW;
		final double component2 = aV * barycentricCoordinatesU + bV * barycentricCoordinatesV + cV * barycentricCoordinatesW;
		
		return point2DSet(point2DResult, component1, component2, point2DResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] point2DRotate(final double[] point2D, final double angle) {
		return point2DRotate(point2D, angle, point2D());
	}
	
//	TODO: Add Javadocs!
	public static double[] point2DRotate(final double[] point2D, final double angle, final double[] point2DResult) {
		return point2DRotate(point2D, angle, point2DResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] point2DRotate(final double[] point2D, final double angle, final double[] point2DResult, final int point2DOffset, final int point2DResultOffset) {
		final double angleCos = cos(angle);
		final double angleSin = sin(angle);
		
		final double component1 = point2DGetComponent1(point2D, point2DOffset) * angleCos - point2DGetComponent2(point2D, point2DOffset) * angleSin;
		final double component2 = point2DGetComponent2(point2D, point2DOffset) * angleCos + point2DGetComponent1(point2D, point2DOffset) * angleSin;
		
		return point2DSet(point2DResult, component1, component2, point2DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with two components and is set to a disk sample with uniform distribution.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Point.point2DSampleDiskUniformDistribution(Doubles.random(), Doubles.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code double[]} that contains a point with two components and is set to a disk sample with uniform distribution
	 */
	public static double[] point2DSampleDiskUniformDistribution() {
		return point2DSampleDiskUniformDistribution(random(), random());
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with two components and is set to a disk sample with uniform distribution.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Point.point2DSampleDiskUniformDistribution(u, v, Point.point2D());
	 * }
	 * </pre>
	 * 
	 * @param u a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param v a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @return a {@code double[]} that contains a point with two components and is set to a disk sample with uniform distribution
	 */
	public static double[] point2DSampleDiskUniformDistribution(final double u, final double v) {
		return point2DSampleDiskUniformDistribution(u, v, point2D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with two components and is set to a disk sample with uniform distribution.
	 * <p>
	 * If {@code point2DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point2DResult.length < 2}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Point.point2DSampleDiskUniformDistribution(u, v, point2DResult, 0);
	 * }
	 * </pre>
	 * 
	 * @param u a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param v a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param point2DResult a {@code double[]} that contains the point to return
	 * @return a {@code double[]} that contains a point with two components and is set to a disk sample with uniform distribution
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point2DResult.length < 2}
	 * @throws NullPointerException thrown if, and only if, {@code point2DResult} is {@code null}
	 */
	public static double[] point2DSampleDiskUniformDistribution(final double u, final double v, final double[] point2DResult) {
		return point2DSampleDiskUniformDistribution(u, v, point2DResult, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with two components and is set to a disk sample with uniform distribution.
	 * <p>
	 * If {@code point2DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point2DResult.length < point2DResultOffset + 2} or {@code point2DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param u a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param v a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param point2DResult a {@code double[]} that contains the point to return
	 * @param point2DResultOffset the offset in {@code point2DResult} to start at
	 * @return a {@code double[]} that contains a point with two components and is set to a disk sample with uniform distribution
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point2DResult.length < point2DResultOffset + 2} or {@code point2DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code point2DResult} is {@code null}
	 */
	public static double[] point2DSampleDiskUniformDistribution(final double u, final double v, final double[] point2DResult, final int point2DResultOffset) {
		final double r = sqrt(u);
		final double theta = PI_MULTIPLIED_BY_2 * v;
		
		final double component1 = r * cos(theta);
		final double component2 = r * sin(theta);
		
		return point2DSet(point2DResult, component1, component2, point2DResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] point2DScale(final double[] point2D, final double[] vector2DScale) {
		return point2DScale(point2D, vector2DScale, point2D());
	}
	
//	TODO: Add Javadocs!
	public static double[] point2DScale(final double[] point2D, final double[] vector2DScale, final double[] point2DResult) {
		return point2DScale(point2D, vector2DScale, point2DResult, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] point2DScale(final double[] point2D, final double[] vector2DScale, final double[] point2DResult, final int point2DOffset, final int vector2DScaleOffset, final int point2DResultOffset) {
		final double component1 = point2DGetComponent1(point2D, point2DOffset) * vector2DGetComponent1(vector2DScale, vector2DScaleOffset);
		final double component2 = point2DGetComponent2(point2D, point2DOffset) * vector2DGetComponent2(vector2DScale, vector2DScaleOffset);
		
		return point2DSet(point2DResult, component1, component2, point2DResultOffset);
	}
	
	/**
	 * Sets the component values of the point contained in {@code point2DResult} at offset {@code 0}.
	 * <p>
	 * Returns {@code point2DResult}.
	 * <p>
	 * If {@code point2DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point2DResult.length < 2}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Point.point2DSet(point2DResult, component1, component2, 0);
	 * }
	 * </pre>
	 * 
	 * @param point2DResult a {@code double[]} that contains a point with two components
	 * @param component1 the value of component 1, also known as X or U
	 * @param component2 the value of component 2, also known as Y or V
	 * @return {@code point2DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point2DResult.length < 2}
	 * @throws NullPointerException thrown if, and only if, {@code point2DResult} is {@code null}
	 */
	public static double[] point2DSet(final double[] point2DResult, final double component1, final double component2) {
		return point2DSet(point2DResult, component1, component2, 0);
	}
	
	/**
	 * Sets the component values of the point contained in {@code point2DResult} at offset {@code point2DResultOffset}.
	 * <p>
	 * Returns {@code point2DResult}.
	 * <p>
	 * If {@code point2DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point2DResult.length < point2DResultOffset + 2} or {@code point2DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param point2DResult a {@code double[]} that contains a point with two components
	 * @param component1 the value of component 1, also known as X or U
	 * @param component2 the value of component 2, also known as Y or V
	 * @param point2DResultOffset the offset in {@code point2DResult} to start at
	 * @return {@code point2DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point2DResult.length < point2DResultOffset + 2} or {@code point2DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code point2DResult} is {@code null}
	 */
	public static double[] point2DSet(final double[] point2DResult, final double component1, final double component2, final int point2DResultOffset) {
		point2DResult[point2DResultOffset + 0] = component1;
		point2DResult[point2DResultOffset + 1] = component2;
		
		return point2DResult;
	}
	
	/**
	 * Sets the component values of the point contained in {@code point2DResult} at offset {@code point2DResultOffset}.
	 * <p>
	 * Returns {@code point2DResult}.
	 * <p>
	 * If either {@code point2DResult} or {@code point2D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point2DResult.length < point2DResultOffset + 2}, {@code point2DResultOffset < 0}, {@code point2D.length < point2DOffset + 2} or {@code point2DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param point2DResult a {@code double[]} that contains a point with two components
	 * @param point2D a {@code double[]} that contains a point with component values to set
	 * @param point2DResultOffset the offset in {@code point2DResult} to start at
	 * @param point2DOffset the offset in {@code point2D} to start at
	 * @return {@code point2DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point2DResult.length < point2DResultOffset + 2}, {@code point2DResultOffset < 0}, {@code point2D.length < point2DOffset + 2} or {@code point2DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code point2DResult} or {@code point2D} are {@code null}
	 */
	public static double[] point2DSet(final double[] point2DResult, final double[] point2D, final int point2DResultOffset, final int point2DOffset) {
		point2DResult[point2DResultOffset + 0] = point2D[point2DOffset + 0];
		point2DResult[point2DResultOffset + 1] = point2D[point2DOffset + 1];
		
		return point2DResult;
	}
	
//	TODO: Add Javadocs!
	public static double[] point2DSphericalCoordinates(final double[] vector3D) {
		return point2DSphericalCoordinates(vector3D, point2D());
	}
	
//	TODO: Add Javadocs!
	public static double[] point2DSphericalCoordinates(final double[] vector3D, final double[] point2DResult) {
		return point2DSphericalCoordinates(vector3D, point2DResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] point2DSphericalCoordinates(final double[] vector3D, final double[] point2DResult, final int vector3DOffset, final int point2DResultOffset) {
		final double component1 = vector3DSphericalPhi(vector3D, vector3DOffset) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final double component2 = vector3DSphericalTheta(vector3D, vector3DOffset) * PI_RECIPROCAL;
		
		return point2DSet(point2DResult, component1, component2, point2DResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] point2DWrapAround(final double[] point2D, final double resolution) {
		return point2DWrapAround(point2D, resolution, resolution);
	}
	
//	TODO: Add Javadocs!
	public static double[] point2DWrapAround(final double[] point2D, final double resolutionX, final double resolutionY) {
		return point2DWrapAround(point2D, resolutionX, resolutionY, point2D());
	}
	
//	TODO: Add Javadocs!
	public static double[] point2DWrapAround(final double[] point2D, final double resolutionX, final double resolutionY, final double[] point2DResult) {
		return point2DWrapAround(point2D, resolutionX, resolutionY, point2DResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] point2DWrapAround(final double[] point2D, final double resolutionX, final double resolutionY, final double[] point2DResult, final int point2DOffset, final int point2DResultOffset) {
		final double component1 = positiveModulo(point2DGetComponent1(point2D, point2DOffset) * resolutionX - 0.5D, resolutionX);
		final double component2 = positiveModulo(point2DGetComponent2(point2D, point2DOffset) * resolutionY - 0.5D, resolutionY);
		
		return point2DSet(point2DResult, component1, component2, point2DResultOffset);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Point3D /////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static boolean point3DCoplanar(final double[] point3DA, final double[] point3DB, final double[] point3DC, final double[] point3DD) {
		return point3DCoplanar(point3DA, point3DB, point3DC, point3DD, 0, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static boolean point3DCoplanar(final double[] point3DA, final double[] point3DB, final double[] point3DC, final double[] point3DD, final int point3DAOffset, final int point3DBOffset, final int point3DCOffset, final int point3DDOffset) {
		final double[] vector3DEdgeAB = vector3DDirectionNormalized(point3DA, point3DB, vector3D(), point3DAOffset, point3DBOffset, 0);
		final double[] vector3DEdgeAC = vector3DDirectionNormalized(point3DA, point3DC, vector3D(), point3DAOffset, point3DCOffset, 0);
		final double[] vector3DEdgeAD = vector3DDirectionNormalized(point3DA, point3DD, vector3D(), point3DAOffset, point3DDOffset, 0);
		
		final double tripleProduct = vector3DTripleProduct(vector3DEdgeAB, vector3DEdgeAD, vector3DEdgeAC);
		
		return isZero(tripleProduct);
	}
	
//	TODO: Add Javadocs!
	public static boolean point3DEquals(final double[] point3DA, final double[] point3DB) {
		return point3DEquals(point3DA, point3DB, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static boolean point3DEquals(final double[] point3DA, final double[] point3DB, final int point3DAOffset, final int point3DBOffset) {
		final double component1A = point3DGetComponent1(point3DA, point3DAOffset);
		final double component2A = point3DGetComponent2(point3DA, point3DAOffset);
		final double component3A = point3DGetComponent3(point3DA, point3DAOffset);
		
		final double component1B = point3DGetComponent1(point3DB, point3DBOffset);
		final double component2B = point3DGetComponent2(point3DB, point3DBOffset);
		final double component3B = point3DGetComponent3(point3DB, point3DBOffset);
		
		final boolean equals = equal(component1A, component1B) && equal(component2A, component2B) && equal(component3A, component3B);
		
		return equals;
	}
	
//	TODO: Add Javadocs!
	public static double point3DDistance(final double[] point3DEye, final double[] point3DTarget) {
		return point3DDistance(point3DEye, point3DTarget, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double point3DDistance(final double[] point3DEye, final double[] point3DTarget, final int point3DEyeOffset, final int point3DTargetOffset) {
		return vector3DLength(vector3DDirection(point3DEye, point3DTarget, vector3D(), point3DEyeOffset, point3DTargetOffset, 0));
	}
	
//	TODO: Add Javadocs!
	public static double point3DDistanceSquared(final double[] point3DEye, final double[] point3DTarget) {
		return point3DDistanceSquared(point3DEye, point3DTarget, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double point3DDistanceSquared(final double[] point3DEye, final double[] point3DTarget, final int point3DEyeOffset, final int point3DTargetOffset) {
		return vector3DLengthSquared(vector3DDirection(point3DEye, point3DTarget, vector3D(), point3DEyeOffset, point3DTargetOffset, 0));
	}
	
//	TODO: Add Javadocs!
	public static double point3DGetComponent1(final double[] point3D) {
		return point3DGetComponent1(point3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double point3DGetComponent1(final double[] point3D, final int point3DOffset) {
		return point3D[point3DOffset + 0];
	}
	
//	TODO: Add Javadocs!
	public static double point3DGetComponent2(final double[] point3D) {
		return point3DGetComponent2(point3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double point3DGetComponent2(final double[] point3D, final int point3DOffset) {
		return point3D[point3DOffset + 1];
	}
	
//	TODO: Add Javadocs!
	public static double point3DGetComponent3(final double[] point3D) {
		return point3DGetComponent3(point3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double point3DGetComponent3(final double[] point3D, final int point3DOffset) {
		return point3D[point3DOffset + 2];
	}
	
//	TODO: Add Javadocs!
	public static double point3DGetU(final double[] point3D) {
		return point3DGetU(point3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double point3DGetU(final double[] point3D, final int point3DOffset) {
		return point3D[point3DOffset + 0];
	}
	
//	TODO: Add Javadocs!
	public static double point3DGetV(final double[] point3D) {
		return point3DGetV(point3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double point3DGetV(final double[] point3D, final int point3DOffset) {
		return point3D[point3DOffset + 1];
	}
	
//	TODO: Add Javadocs!
	public static double point3DGetW(final double[] point3D) {
		return point3DGetW(point3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double point3DGetW(final double[] point3D, final int point3DOffset) {
		return point3D[point3DOffset + 2];
	}
	
//	TODO: Add Javadocs!
	public static double point3DGetX(final double[] point3D) {
		return point3DGetX(point3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double point3DGetX(final double[] point3D, final int point3DOffset) {
		return point3D[point3DOffset + 0];
	}
	
//	TODO: Add Javadocs!
	public static double point3DGetY(final double[] point3D) {
		return point3DGetY(point3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double point3DGetY(final double[] point3D, final int point3DOffset) {
		return point3D[point3DOffset + 1];
	}
	
//	TODO: Add Javadocs!
	public static double point3DGetZ(final double[] point3D) {
		return point3DGetZ(point3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double point3DGetZ(final double[] point3D, final int point3DOffset) {
		return point3D[point3DOffset + 2];
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with three components.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Point.point3D(0.0D, 0.0D, 0.0D);
	 * }
	 * </pre>
	 * 
	 * @return a {@code double[]} that contains a point with three components
	 */
	public static double[] point3D() {
		return point3D(0.0D, 0.0D, 0.0D);
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with three components.
	 * 
	 * @param component1 the value of component 1, also known as X or U
	 * @param component2 the value of component 2, also known as Y or V
	 * @param component3 the value of component 3, also known as Z or W
	 * @return a {@code double[]} that contains a point with three components
	 */
	public static double[] point3D(final double component1, final double component2, final double component3) {
		return new double[] {component1, component2, component3};
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with three components and is set to the result of {@code point3D + vector3D}.
	 * <p>
	 * If either {@code point3D} or {@code vector3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point3D.length < 3} or {@code vector3D.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Point.point3DAdd(point3D, vector3D, Point.point3D());
	 * }
	 * </pre>
	 * 
	 * @param point3D a {@code double[]} that contains a point with three components
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @return a {@code double[]} that contains a point with three components and is set to the result of {@code point3D + vector3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point3D.length < 3} or {@code vector3D.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code point3D} or {@code vector3D} are {@code null}
	 */
	public static double[] point3DAdd(final double[] point3D, final double[] vector3D) {
		return point3DAdd(point3D, vector3D, point3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with three components and is set to the result of {@code point3D + vector3D}.
	 * <p>
	 * If either {@code point3D}, {@code vector3D} or {@code point3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point3D.length < 3}, {@code vector3D.length < 3} or {@code point3DResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Point.point3DAdd(point3D, vector3D, point3DResult, 0, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param point3D a {@code double[]} that contains a point with three components
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @param point3DResult a {@code double[]} that contains the point to return
	 * @return a {@code double[]} that contains a point with three components and is set to the result of {@code point3D + vector3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point3D.length < 3}, {@code vector3D.length < 3} or {@code point3DResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code point3D}, {@code vector3D} or {@code point3DResult} are {@code null}
	 */
	public static double[] point3DAdd(final double[] point3D, final double[] vector3D, final double[] point3DResult) {
		return point3DAdd(point3D, vector3D, point3DResult, 0, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with three components and is set to the result of {@code point3D + vector3D}.
	 * <p>
	 * If either {@code point3D}, {@code vector3D} or {@code point3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point3D.length < point3DOffset + 3}, {@code point3DOffset < 0}, {@code vector3D.length < vector3DOffset + 3}, {@code vector3DOffset < 0}, {@code point3DResult.length < point3DResultOffset + 3} or {@code point3DResultOffset < 0}, an
	 * {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param point3D a {@code double[]} that contains a point with three components
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @param point3DResult a {@code double[]} that contains the point to return
	 * @param point3DOffset the offset in {@code point3D} to start at
	 * @param vector3DOffset the offset in {@code vector3D} to start at
	 * @param point3DResultOffset the offset in {@code point3DResult} to start at
	 * @return a {@code double[]} that contains a point with three components and is set to the result of {@code point3D + vector3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point3D.length < point3DOffset + 3}, {@code point3DOffset < 0}, {@code vector3D.length < vector3DOffset + 3}, {@code vector3DOffset < 0},
	 *                                        {@code point3DResult.length < point3DResultOffset + 3} or {@code point3DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code point3D}, {@code vector3D} or {@code point3DResult} are {@code null}
	 */
	public static double[] point3DAdd(final double[] point3D, final double[] vector3D, final double[] point3DResult, final int point3DOffset, final int vector3DOffset, final int point3DResultOffset) {
		final double component1 = point3D[point3DOffset + 0] + vector3D[vector3DOffset + 0];
		final double component2 = point3D[point3DOffset + 1] + vector3D[vector3DOffset + 1];
		final double component3 = point3D[point3DOffset + 2] + vector3D[vector3DOffset + 2];
		
		return point3DSet(point3DResult, component1, component2, component3, point3DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with three components and is set to the result of {@code point3D + vector3D * scalar}.
	 * <p>
	 * If either {@code point3D} or {@code vector3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point3D.length < 3} or {@code vector3D.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Point.point3DAdd(point3D, vector3D, scalar, Point.point3D());
	 * }
	 * </pre>
	 * 
	 * @param point3D a {@code double[]} that contains a point with three components
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @param scalar a {@code double} scalar value
	 * @return a {@code double[]} that contains a point with three components and is set to the result of {@code point3D + vector3D * scalar}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point3D.length < 3} or {@code vector3D.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code point3D} or {@code vector3D} are {@code null}
	 */
	public static double[] point3DAdd(final double[] point3D, final double[] vector3D, final double scalar) {
		return point3DAdd(point3D, vector3D, scalar, point3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with three components and is set to the result of {@code point3D + vector3D * scalar}.
	 * <p>
	 * If either {@code point3D}, {@code vector3D} or {@code point3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point3D.length < 3}, {@code vector3D.length < 3} or {@code point3DResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Point.point3DAdd(point3D, vector3D, scalar, point3DResult, 0, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param point3D a {@code double[]} that contains a point with three components
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @param scalar a {@code double} scalar value
	 * @param point3DResult a {@code double[]} that contains the point to return
	 * @return a {@code double[]} that contains a point with three components and is set to the result of {@code point3D + vector3D * scalar}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point3D.length < 3}, {@code vector3D.length < 3} or {@code point3DResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code point3D}, {@code vector3D} or {@code point3DResult} are {@code null}
	 */
	public static double[] point3DAdd(final double[] point3D, final double[] vector3D, final double scalar, final double[] point3DResult) {
		return point3DAdd(point3D, vector3D, scalar, point3DResult, 0, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with three components and is set to the result of {@code point3D + vector3D * scalar}.
	 * <p>
	 * If either {@code point3D}, {@code vector3D} or {@code point3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point3D.length < point3DOffset + 3}, {@code point3DOffset < 0}, {@code vector3D.length < vector3DOffset + 3}, {@code vector3DOffset < 0}, {@code point3DResult.length < point3DResultOffset + 3} or {@code point3DResultOffset < 0}, an
	 * {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param point3D a {@code double[]} that contains a point with three components
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @param scalar a {@code double} scalar value
	 * @param point3DResult a {@code double[]} that contains the point to return
	 * @param point3DOffset the offset in {@code point3D} to start at
	 * @param vector3DOffset the offset in {@code vector3D} to start at
	 * @param point3DResultOffset the offset in {@code point3DResult} to start at
	 * @return a {@code double[]} that contains a point with three components and is set to the result of {@code point3D + vector3D * scalar}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point3D.length < point3DOffset + 3}, {@code point3DOffset < 0}, {@code vector3D.length < vector3DOffset + 3}, {@code vector3DOffset < 0},
	 *                                        {@code point3DResult.length < point3DResultOffset + 3} or {@code point3DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code point3D}, {@code vector3D} or {@code point3DResult} are {@code null}
	 */
	public static double[] point3DAdd(final double[] point3D, final double[] vector3D, final double scalar, final double[] point3DResult, final int point3DOffset, final int vector3DOffset, final int point3DResultOffset) {
		final double component1 = point3D[point3DOffset + 0] + vector3D[vector3DOffset + 0] * scalar;
		final double component2 = point3D[point3DOffset + 1] + vector3D[vector3DOffset + 1] * scalar;
		final double component3 = point3D[point3DOffset + 2] + vector3D[vector3DOffset + 2] * scalar;
		
		return point3DSet(point3DResult, component1, component2, component3, point3DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with three components and is set to the component values of the vector {@code vector3D}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Point.point3DFromVector3D(vector3D, Point.point3D());
	 * }
	 * </pre>
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @return a {@code double[]} that contains a point with three components and is set to the component values of the vector {@code vector3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double[] point3DFromVector3D(final double[] vector3D) {
		return point3DFromVector3D(vector3D, point3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with three components and is set to the component values of the vector {@code vector3D}.
	 * <p>
	 * If either {@code vector3D} or {@code point3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < 3} or {@code point3DResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Point.point3DFromVector3D(vector3D, point3DResult, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @param point3DResult a {@code double[]} that contains the point to return
	 * @return a {@code double[]} that contains a point with three components and is set to the component values of the vector {@code vector3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < 3} or {@code point3DResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3D} or {@code point3DResult} are {@code null}
	 */
	public static double[] point3DFromVector3D(final double[] vector3D, final double[] point3DResult) {
		return point3DFromVector3D(vector3D, point3DResult, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with three components and is set to the component values of the vector {@code vector3D}.
	 * <p>
	 * If either {@code vector3D} or {@code point3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < vector3DOffset + 3}, {@code vector3DOffset < 0}, {@code point3DResult.length < point3DResultOffset + 3} or {@code point3DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @param point3DResult a {@code double[]} that contains the point to return
	 * @param vector3DOffset the offset in {@code vector3D} to start at
	 * @param point3DResultOffset the offset in {@code point3DResult} to start at
	 * @return a {@code double[]} that contains a point with three components and is set to the component values of the vector {@code vector3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < vector3DOffset + 3}, {@code vector3DOffset < 0}, {@code point3DResult.length < point3DResultOffset + 3} or {@code point3DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3D} or {@code point3DResult} are {@code null}
	 */
	public static double[] point3DFromVector3D(final double[] vector3D, final double[] point3DResult, final int vector3DOffset, final int point3DResultOffset) {
		final double component1 = vector3D[vector3DOffset + 0];
		final double component2 = vector3D[vector3DOffset + 1];
		final double component3 = vector3D[vector3DOffset + 2];
		
		return point3DSet(point3DResult, component1, component2, component3, point3DResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] point3DLerp(final double[] point3DA, final double[] point3DB, final double t) {
		return point3DLerp(point3DA, point3DB, t, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] point3DLerp(final double[] point3DA, final double[] point3DB, final double t, final double[] point3DResult) {
		return point3DLerp(point3DA, point3DB, t, point3DResult, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] point3DLerp(final double[] point3DA, final double[] point3DB, final double t, final double[] point3DResult, final int point3DAOffset, final int point3DBOffset, final int point3DResultOffset) {
		final double component1 = lerp(point3DGetComponent1(point3DA, point3DAOffset), point3DGetComponent1(point3DB, point3DBOffset), t);
		final double component2 = lerp(point3DGetComponent2(point3DA, point3DAOffset), point3DGetComponent2(point3DB, point3DBOffset), t);
		final double component3 = lerp(point3DGetComponent3(point3DA, point3DAOffset), point3DGetComponent3(point3DB, point3DBOffset), t);
		
		return point3DSet(point3DResult, component1, component2, component3, point3DResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] point3DMaximum(final double[] point3DA, final double[] point3DB) {
		return point3DMaximum(point3DA, point3DB, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] point3DMaximum(final double[] point3DA, final double[] point3DB, final double[] point3DResult) {
		return point3DMaximum(point3DA, point3DB, point3DResult, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] point3DMaximum(final double[] point3DA, final double[] point3DB, final double[] point3DResult, final int point3DAOffset, final int point3DBOffset, final int point3DResultOffset) {
		final double component1 = max(point3DGetComponent1(point3DA, point3DAOffset), point3DGetComponent1(point3DB, point3DBOffset));
		final double component2 = max(point3DGetComponent2(point3DA, point3DAOffset), point3DGetComponent2(point3DB, point3DBOffset));
		final double component3 = max(point3DGetComponent3(point3DA, point3DAOffset), point3DGetComponent3(point3DB, point3DBOffset));
		
		return point3DSet(point3DResult, component1, component2, component3, point3DResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] point3DMidpoint(final double[] point3DA, final double[] point3DB) {
		return point3DMidpoint(point3DA, point3DB, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] point3DMidpoint(final double[] point3DA, final double[] point3DB, final double[] point3DResult) {
		return point3DMidpoint(point3DA, point3DB, point3DResult, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] point3DMidpoint(final double[] point3DA, final double[] point3DB, final double[] point3DResult, final int point3DAOffset, final int point3DBOffset, final int point3DResultOffset) {
		final double component1 = (point3DGetComponent1(point3DA, point3DAOffset) + point3DGetComponent1(point3DB, point3DBOffset)) * 0.5D;
		final double component2 = (point3DGetComponent2(point3DA, point3DAOffset) + point3DGetComponent2(point3DB, point3DBOffset)) * 0.5D;
		final double component3 = (point3DGetComponent3(point3DA, point3DAOffset) + point3DGetComponent3(point3DB, point3DBOffset)) * 0.5D;
		
		return point3DSet(point3DResult, component1, component2, component3, point3DResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] point3DMinimum(final double[] point3DA, final double[] point3DB) {
		return point3DMinimum(point3DA, point3DB, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] point3DMinimum(final double[] point3DA, final double[] point3DB, final double[] point3DResult) {
		return point3DMinimum(point3DA, point3DB, point3DResult, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] point3DMinimum(final double[] point3DA, final double[] point3DB, final double[] point3DResult, final int point3DAOffset, final int point3DBOffset, final int point3DResultOffset) {
		final double component1 = min(point3DGetComponent1(point3DA, point3DAOffset), point3DGetComponent1(point3DB, point3DBOffset));
		final double component2 = min(point3DGetComponent2(point3DA, point3DAOffset), point3DGetComponent2(point3DB, point3DBOffset));
		final double component3 = min(point3DGetComponent3(point3DA, point3DAOffset), point3DGetComponent3(point3DB, point3DBOffset));
		
		return point3DSet(point3DResult, component1, component2, component3, point3DResultOffset);
	}
	
	/**
	 * Sets the component values of the point contained in {@code point3DResult} at offset {@code 0}.
	 * <p>
	 * Returns {@code point3DResult}.
	 * <p>
	 * If {@code point3DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point3DResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Point.point3DSet(point3DResult, component1, component2, component3, 0);
	 * }
	 * </pre>
	 * 
	 * @param point3DResult a {@code double[]} that contains a point with three components
	 * @param component1 the value of component 1, also known as X or U
	 * @param component2 the value of component 2, also known as Y or V
	 * @param component3 the value of component 3, also known as Z or W
	 * @return {@code point3DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point3DResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code point3DResult} is {@code null}
	 */
	public static double[] point3DSet(final double[] point3DResult, final double component1, final double component2, final double component3) {
		return point3DSet(point3DResult, component1, component2, component3, 0);
	}
	
	/**
	 * Sets the component values of the point contained in {@code point3DResult} at offset {@code point3DResultOffset}.
	 * <p>
	 * Returns {@code point3DResult}.
	 * <p>
	 * If {@code point3DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point3DResult.length < point3DResultOffset + 3} or {@code point3DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param point3DResult a {@code double[]} that contains a point with three components
	 * @param component1 the value of component 1, also known as X or U
	 * @param component2 the value of component 2, also known as Y or V
	 * @param component3 the value of component 3, also known as Z or W
	 * @param point3DResultOffset the offset in {@code point3DResult} to start at
	 * @return {@code point3DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point3DResult.length < point3DResultOffset + 3} or {@code point3DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code point3DResult} is {@code null}
	 */
	public static double[] point3DSet(final double[] point3DResult, final double component1, final double component2, final double component3, final int point3DResultOffset) {
		point3DResult[point3DResultOffset + 0] = component1;
		point3DResult[point3DResultOffset + 1] = component2;
		point3DResult[point3DResultOffset + 2] = component3;
		
		return point3DResult;
	}
	
	/**
	 * Sets the component values of the point contained in {@code point3DResult} at offset {@code point3DResultOffset}.
	 * <p>
	 * Returns {@code point3DResult}.
	 * <p>
	 * If either {@code point3DResult} or {@code point3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point3DResult.length < point3DResultOffset + 3}, {@code point3DResultOffset < 0}, {@code point3D.length < point3DOffset + 3} or {@code point3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param point3DResult a {@code double[]} that contains a point with three components
	 * @param point3D a {@code double[]} that contains a point with component values to set
	 * @param point3DResultOffset the offset in {@code point3DResult} to start at
	 * @param point3DOffset the offset in {@code point3D} to start at
	 * @return {@code point3DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point3DResult.length < point3DResultOffset + 3}, {@code point3DResultOffset < 0}, {@code point3D.length < point3DOffset + 3} or {@code point3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code point3DResult} or {@code point3D} are {@code null}
	 */
	public static double[] point3DSet(final double[] point3DResult, final double[] point3D, final int point3DResultOffset, final int point3DOffset) {
		point3DResult[point3DResultOffset + 0] = point3D[point3DOffset + 0];
		point3DResult[point3DResultOffset + 1] = point3D[point3DOffset + 1];
		point3DResult[point3DResultOffset + 2] = point3D[point3DOffset + 2];
		
		return point3DResult;
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with three components and is set to the result of {@code point3DRHS} transformed by {@code matrix44DLHS}.
	 * <p>
	 * If either {@code matrix44DLHS} or {@code point3DRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DLHS.length < 16} or {@code point3DRHS.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Point.point3DTransformMatrix44D(matrix44DLHS, point3DRHS, Point.point3D());
	 * }
	 * </pre>
	 * 
	 * @param matrix44DLHS a {@code double[]} that contains the matrix on the left-hand side of the transformation
	 * @param point3DRHS a {@code double[]} that contains the point on the right-hand side of the transformation
	 * @return a {@code double[]} that contains a point with three components and is set to the result of {@code point3DRHS} transformed by {@code matrix44DLHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DLHS.length < 16} or {@code point3DRHS.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code matrix44DLHS} or {@code point3DRHS} are {@code null}
	 */
	public static double[] point3DTransformMatrix44D(final double[] matrix44DLHS, final double[] point3DRHS) {
		return point3DTransformMatrix44D(matrix44DLHS, point3DRHS, point3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with three components and is set to the result of {@code point3DRHS} transformed by {@code matrix44DLHS}.
	 * <p>
	 * If either {@code matrix44DLHS}, {@code point3DRHS} or {@code point3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DLHS.length < 16}, {@code point3DRHS.length < 3} or {@code point3DResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Point.point3DTransformMatrix44D(matrix44DLHS, point3DRHS, point3DResult, 0, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param matrix44DLHS a {@code double[]} that contains the matrix on the left-hand side of the transformation
	 * @param point3DRHS a {@code double[]} that contains the point on the right-hand side of the transformation
	 * @param point3DResult a {@code double[]} that contains the point to return
	 * @return a {@code double[]} that contains a point with three components and is set to the result of {@code point3DRHS} transformed by {@code matrix44DLHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DLHS.length < 16}, {@code point3DRHS.length < 3} or {@code point3DResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code matrix44DLHS}, {@code point3DRHS} or {@code point3DResult} are {@code null}
	 */
	public static double[] point3DTransformMatrix44D(final double[] matrix44DLHS, final double[] point3DRHS, final double[] point3DResult) {
		return point3DTransformMatrix44D(matrix44DLHS, point3DRHS, point3DResult, 0, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with three components and is set to the result of {@code point3DRHS} transformed by {@code matrix44DLHS}.
	 * <p>
	 * If either {@code matrix44DLHS}, {@code point3DRHS} or {@code point3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DLHS.length < matrix44DLHSOffset + 16}, {@code matrix44DLHSOffset < 0}, {@code point3DRHS.length < point3DRHSOffset + 3}, {@code point3DRHSOffset < 0}, {@code point3DResult.length < point3DResultOffset + 3} or
	 * {@code point3DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param matrix44DLHS a {@code double[]} that contains the matrix on the left-hand side of the transformation
	 * @param point3DRHS a {@code double[]} that contains the point on the right-hand side of the transformation
	 * @param point3DResult a {@code double[]} that contains the point to return
	 * @param matrix44DLHSOffset the offset in {@code matrix44DLHS} to start at
	 * @param point3DRHSOffset the offset in {@code point3DRHS} to start at
	 * @param point3DResultOffset the offset in {@code point3DResult} to start at
	 * @return a {@code double[]} that contains a point with three components and is set to the result of {@code point3DRHS} transformed by {@code matrix44DLHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DLHS.length < matrix44DLHSOffset + 16}, {@code matrix44DLHSOffset < 0}, {@code point3DRHS.length < point3DRHSOffset + 3}, {@code point3DRHSOffset < 0},
	 *                                        {@code point3DResult.length < point3DResultOffset + 3} or {@code point3DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code matrix44DLHS}, {@code point3DRHS} or {@code point3DResult} are {@code null}
	 */
	public static double[] point3DTransformMatrix44D(final double[] matrix44DLHS, final double[] point3DRHS, final double[] point3DResult, final int matrix44DLHSOffset, final int point3DRHSOffset, final int point3DResultOffset) {
		final double component1 = matrix44DLHS[matrix44DLHSOffset + 0] * point3DRHS[point3DRHSOffset + 0] + matrix44DLHS[matrix44DLHSOffset + 1] * point3DRHS[point3DRHSOffset + 1] + matrix44DLHS[matrix44DLHSOffset +  2] * point3DRHS[point3DRHSOffset + 2] + matrix44DLHS[matrix44DLHSOffset +  3];
		final double component2 = matrix44DLHS[matrix44DLHSOffset + 4] * point3DRHS[point3DRHSOffset + 0] + matrix44DLHS[matrix44DLHSOffset + 5] * point3DRHS[point3DRHSOffset + 1] + matrix44DLHS[matrix44DLHSOffset +  6] * point3DRHS[point3DRHSOffset + 2] + matrix44DLHS[matrix44DLHSOffset +  7];
		final double component3 = matrix44DLHS[matrix44DLHSOffset + 8] * point3DRHS[point3DRHSOffset + 0] + matrix44DLHS[matrix44DLHSOffset + 9] * point3DRHS[point3DRHSOffset + 1] + matrix44DLHS[matrix44DLHSOffset + 10] * point3DRHS[point3DRHSOffset + 2] + matrix44DLHS[matrix44DLHSOffset + 11];
		
		return point3DSet(point3DResult, component1, component2, component3, point3DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with three components and is set to the result of {@code point3DRHS} transformed and divided by {@code matrix44DLHS}.
	 * <p>
	 * If either {@code matrix44DLHS} or {@code point3DRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DLHS.length < 16} or {@code point3DRHS.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Point.point3DTransformAndDivideMatrix44D(matrix44DLHS, point3DRHS, Point.point3D());
	 * }
	 * </pre>
	 * 
	 * @param matrix44DLHS a {@code double[]} that contains the matrix on the left-hand side of the transformation
	 * @param point3DRHS a {@code double[]} that contains the point on the right-hand side of the transformation
	 * @return a {@code double[]} that contains a point with three components and is set to the result of {@code point3DRHS} transformed and divided by {@code matrix44DLHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DLHS.length < 16} or {@code point3DRHS.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code matrix44DLHS} or {@code point3DRHS} are {@code null}
	 */
	public static double[] point3DTransformAndDivideMatrix44D(final double[] matrix44DLHS, final double[] point3DRHS) {
		return point3DTransformAndDivideMatrix44D(matrix44DLHS, point3DRHS, point3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with three components and is set to the result of {@code point3DRHS} transformed and divided by {@code matrix44DLHS}.
	 * <p>
	 * If either {@code matrix44DLHS}, {@code point3DRHS} or {@code point3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DLHS.length < 16}, {@code point3DRHS.length < 3} or {@code point3DResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Point.point3DTransformAndDivideMatrix44D(matrix44DLHS, point3DRHS, point3DResult, 0, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param matrix44DLHS a {@code double[]} that contains the matrix on the left-hand side of the transformation
	 * @param point3DRHS a {@code double[]} that contains the point on the right-hand side of the transformation
	 * @param point3DResult a {@code double[]} that contains the point to return
	 * @return a {@code double[]} that contains a point with three components and is set to the result of {@code point3DRHS} transformed and divided by {@code matrix44DLHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DLHS.length < 16}, {@code point3DRHS.length < 3} or {@code point3DResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code matrix44DLHS}, {@code point3DRHS} or {@code point3DResult} are {@code null}
	 */
	public static double[] point3DTransformAndDivideMatrix44D(final double[] matrix44DLHS, final double[] point3DRHS, final double[] point3DResult) {
		return point3DTransformAndDivideMatrix44D(matrix44DLHS, point3DRHS, point3DResult, 0, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a point with three components and is set to the result of {@code point3DRHS} transformed and divided by {@code matrix44DLHS}.
	 * <p>
	 * If either {@code matrix44DLHS}, {@code point3DRHS} or {@code point3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DLHS.length < matrix44DLHSOffset + 16}, {@code matrix44DLHSOffset < 0}, {@code point3DRHS.length < point3DRHSOffset + 3}, {@code point3DRHSOffset < 0}, {@code point3DResult.length < point3DResultOffset + 3} or
	 * {@code point3DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param matrix44DLHS a {@code double[]} that contains the matrix on the left-hand side of the transformation
	 * @param point3DRHS a {@code double[]} that contains the point on the right-hand side of the transformation
	 * @param point3DResult a {@code double[]} that contains the point to return
	 * @param matrix44DLHSOffset the offset in {@code matrix44DLHS} to start at
	 * @param point3DRHSOffset the offset in {@code point3DRHS} to start at
	 * @param point3DResultOffset the offset in {@code point3DResult} to start at
	 * @return a {@code double[]} that contains a point with three components and is set to the result of {@code point3DRHS} transformed and divided by {@code matrix44DLHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DLHS.length < matrix44DLHSOffset + 16}, {@code matrix44DLHSOffset < 0}, {@code point3DRHS.length < point3DRHSOffset + 3}, {@code point3DRHSOffset < 0},
	 *                                        {@code point3DResult.length < point3DResultOffset + 3} or {@code point3DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code matrix44DLHS}, {@code point3DRHS} or {@code point3DResult} are {@code null}
	 */
	public static double[] point3DTransformAndDivideMatrix44D(final double[] matrix44DLHS, final double[] point3DRHS, final double[] point3DResult, final int matrix44DLHSOffset, final int point3DRHSOffset, final int point3DResultOffset) {
		final double component1 = matrix44DLHS[matrix44DLHSOffset +  0] * point3DRHS[point3DRHSOffset + 0] + matrix44DLHS[matrix44DLHSOffset +  1] * point3DRHS[point3DRHSOffset + 1] + matrix44DLHS[matrix44DLHSOffset +  2] * point3DRHS[point3DRHSOffset + 2] + matrix44DLHS[matrix44DLHSOffset +  3];
		final double component2 = matrix44DLHS[matrix44DLHSOffset +  4] * point3DRHS[point3DRHSOffset + 0] + matrix44DLHS[matrix44DLHSOffset +  5] * point3DRHS[point3DRHSOffset + 1] + matrix44DLHS[matrix44DLHSOffset +  6] * point3DRHS[point3DRHSOffset + 2] + matrix44DLHS[matrix44DLHSOffset +  7];
		final double component3 = matrix44DLHS[matrix44DLHSOffset +  8] * point3DRHS[point3DRHSOffset + 0] + matrix44DLHS[matrix44DLHSOffset +  9] * point3DRHS[point3DRHSOffset + 1] + matrix44DLHS[matrix44DLHSOffset + 10] * point3DRHS[point3DRHSOffset + 2] + matrix44DLHS[matrix44DLHSOffset + 11];
		final double component4 = matrix44DLHS[matrix44DLHSOffset + 12] * point3DRHS[point3DRHSOffset + 0] + matrix44DLHS[matrix44DLHSOffset + 13] * point3DRHS[point3DRHSOffset + 1] + matrix44DLHS[matrix44DLHSOffset + 14] * point3DRHS[point3DRHSOffset + 2] + matrix44DLHS[matrix44DLHSOffset + 15];
		
		if(equal(component4, 1.0D) || isZero(component4)) {
			return point3DSet(point3DResult, component1, component2, component3, point3DResultOffset);
		}
		
		return point3DSet(point3DResult, component1 / component4, component2 / component4, component3 / component4, point3DResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static void point3DSwap(final double[] point3DA, final double[] point3DB) {
		point3DSwap(point3DA, point3DB, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static void point3DSwap(final double[] point3DA, final double[] point3DB, final int point3DAOffset, final int point3DBOffset) {
		final double component1A = point3DGetComponent1(point3DA, point3DAOffset);
		final double component2A = point3DGetComponent2(point3DA, point3DAOffset);
		final double component3A = point3DGetComponent3(point3DA, point3DAOffset);
		
		final double component1B = point3DGetComponent1(point3DB, point3DBOffset);
		final double component2B = point3DGetComponent2(point3DB, point3DBOffset);
		final double component3B = point3DGetComponent3(point3DB, point3DBOffset);
		
		point3DSet(point3DA, component1B, component2B, component3B, point3DAOffset);
		point3DSet(point3DB, component1A, component2A, component3A, point3DBOffset);
	}
}