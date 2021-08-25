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

import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Doubles.cos;
import static org.dayflower.utility.Doubles.equal;
import static org.dayflower.utility.Doubles.isZero;
import static org.dayflower.utility.Doubles.random;
import static org.dayflower.utility.Doubles.sin;
import static org.dayflower.utility.Doubles.sqrt;

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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Point3D /////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
}