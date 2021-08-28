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

import static org.dayflower.simplex.Point.point3DGetU;
import static org.dayflower.simplex.Point.point3DGetV;
import static org.dayflower.simplex.Point.point3DGetW;
import static org.dayflower.simplex.Point.point3DGetX;
import static org.dayflower.simplex.Point.point3DGetY;
import static org.dayflower.simplex.Point.point3DGetZ;
import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Doubles.acos;
import static org.dayflower.utility.Doubles.getOrAdd;
import static org.dayflower.utility.Doubles.atan2;
import static org.dayflower.utility.Doubles.saturate;
import static org.dayflower.utility.Doubles.sqrt;

import java.lang.reflect.Field;//TODO: Add Javadocs!

/**
 * A class that consists exclusively of static methods that returns or performs various operations on vectors.
 * <p>
 * This class currently supports the following:
 * <ul>
 * <li>{@code Vector2D} - a 2-dimensional vector represented by a {@code double[]}.</li>
 * <li>{@code Vector3D} - a 3-dimensional vector represented by a {@code double[]}.</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Vector {
	private Vector() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Vector2D ////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static double vector2DCrossProduct(final double[] vector2DLHS, final double[] vector2DRHS) {
		return vector2DCrossProduct(vector2DLHS, vector2DRHS, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double vector2DCrossProduct(final double[] vector2DLHS, final double[] vector2DRHS, final int vector2DLHSOffset, final int vector2DRHSOffset) {
		return vector2DLHS[vector2DLHSOffset + 0] * vector2DRHS[vector2DRHSOffset + 1] - vector2DLHS[vector2DLHSOffset + 1] * vector2DRHS[vector2DRHSOffset + 0];
	}
	
	/**
	 * Returns the value of component 1 from the vector contained in {@code vector2D} at offset {@code 0}.
	 * <p>
	 * If {@code vector2D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector2D.length < 1}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector2DGetComponent1(vector2D, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector2D a {@code double[]} that contains a vector with two components
	 * @return the value of component 1 from the vector contained in {@code vector2D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector2D.length < 1}
	 * @throws NullPointerException thrown if, and only if, {@code vector2D} is {@code null}
	 */
	public static double vector2DGetComponent1(final double[] vector2D) {
		return vector2DGetComponent1(vector2D, 0);
	}
	
	/**
	 * Returns the value of component 1 from the vector contained in {@code vector2D} at offset {@code vector2DOffset}.
	 * <p>
	 * If {@code vector2D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector2D.length < vector2DOffset + 1} or {@code vector2DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector2D a {@code double[]} that contains a vector with two components
	 * @param vector2DOffset the offset in {@code vector2D} to start at
	 * @return the value of component 1 from the vector contained in {@code vector2D} at offset {@code vector2DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector2D.length < vector2DOffset + 1} or {@code vector2DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code vector2D} is {@code null}
	 */
	public static double vector2DGetComponent1(final double[] vector2D, final int vector2DOffset) {
		return vector2D[vector2DOffset + 0];
	}
	
	/**
	 * Returns the value of component 2 from the vector contained in {@code vector2D} at offset {@code 0}.
	 * <p>
	 * If {@code vector2D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector2D.length < 2}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector2DGetComponent2(vector2D, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector2D a {@code double[]} that contains a vector with two components
	 * @return the value of component 2 from the vector contained in {@code vector2D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector2D.length < 2}
	 * @throws NullPointerException thrown if, and only if, {@code vector2D} is {@code null}
	 */
	public static double vector2DGetComponent2(final double[] vector2D) {
		return vector2DGetComponent2(vector2D, 0);
	}
	
	/**
	 * Returns the value of component 2 from the vector contained in {@code vector2D} at offset {@code vector2DOffset}.
	 * <p>
	 * If {@code vector2D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector2D.length < vector2DOffset + 2} or {@code vector2DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector2D a {@code double[]} that contains a vector with two components
	 * @param vector2DOffset the offset in {@code vector2D} to start at
	 * @return the value of component 2 from the vector contained in {@code vector2D} at offset {@code vector2DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector2D.length < vector2DOffset + 2} or {@code vector2DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code vector2D} is {@code null}
	 */
	public static double vector2DGetComponent2(final double[] vector2D, final int vector2DOffset) {
		return vector2D[vector2DOffset + 1];
	}
	
	/**
	 * Returns the value of the U-component from the vector contained in {@code vector2D} at offset {@code 0}.
	 * <p>
	 * If {@code vector2D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector2D.length < 1}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector2DGetU(vector2D, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector2D a {@code double[]} that contains a vector with two components
	 * @return the value of the U-component from the vector contained in {@code vector2D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector2D.length < 1}
	 * @throws NullPointerException thrown if, and only if, {@code vector2D} is {@code null}
	 */
	public static double vector2DGetU(final double[] vector2D) {
		return vector2DGetU(vector2D, 0);
	}
	
	/**
	 * Returns the value of the U-component from the vector contained in {@code vector2D} at offset {@code vector2DOffset}.
	 * <p>
	 * If {@code vector2D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector2D.length < vector2DOffset + 1} or {@code vector2DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector2D a {@code double[]} that contains a vector with two components
	 * @param vector2DOffset the offset in {@code vector2D} to start at
	 * @return the value of the U-component from the vector contained in {@code vector2D} at offset {@code vector2DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector2D.length < vector2DOffset + 1} or {@code vector2DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code vector2D} is {@code null}
	 */
	public static double vector2DGetU(final double[] vector2D, final int vector2DOffset) {
		return vector2D[vector2DOffset + 0];
	}
	
	/**
	 * Returns the value of the V-component from the vector contained in {@code vector2D} at offset {@code 0}.
	 * <p>
	 * If {@code vector2D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector2D.length < 2}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector2DGetV(vector2D, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector2D a {@code double[]} that contains a vector with two components
	 * @return the value of the V-component from the vector contained in {@code vector2D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector2D.length < 2}
	 * @throws NullPointerException thrown if, and only if, {@code vector2D} is {@code null}
	 */
	public static double vector2DGetV(final double[] vector2D) {
		return vector2DGetV(vector2D, 0);
	}
	
	/**
	 * Returns the value of the V-component from the vector contained in {@code vector2D} at offset {@code vector2DOffset}.
	 * <p>
	 * If {@code vector2D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector2D.length < vector2DOffset + 2} or {@code vector2DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector2D a {@code double[]} that contains a vector with two components
	 * @param vector2DOffset the offset in {@code vector2D} to start at
	 * @return the value of the V-component from the vector contained in {@code vector2D} at offset {@code vector2DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector2D.length < vector2DOffset + 2} or {@code vector2DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code vector2D} is {@code null}
	 */
	public static double vector2DGetV(final double[] vector2D, final int vector2DOffset) {
		return vector2D[vector2DOffset + 1];
	}
	
	/**
	 * Returns the value of the X-component from the vector contained in {@code vector2D} at offset {@code 0}.
	 * <p>
	 * If {@code vector2D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector2D.length < 1}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector2DGetX(vector2D, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector2D a {@code double[]} that contains a vector with two components
	 * @return the value of the X-component from the vector contained in {@code vector2D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector2D.length < 1}
	 * @throws NullPointerException thrown if, and only if, {@code vector2D} is {@code null}
	 */
	public static double vector2DGetX(final double[] vector2D) {
		return vector2DGetX(vector2D, 0);
	}
	
	/**
	 * Returns the value of the X-component from the vector contained in {@code vector2D} at offset {@code vector2DOffset}.
	 * <p>
	 * If {@code vector2D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector2D.length < vector2DOffset + 1} or {@code vector2DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector2D a {@code double[]} that contains a vector with two components
	 * @param vector2DOffset the offset in {@code vector2D} to start at
	 * @return the value of the X-component from the vector contained in {@code vector2D} at offset {@code vector2DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector2D.length < vector2DOffset + 1} or {@code vector2DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code vector2D} is {@code null}
	 */
	public static double vector2DGetX(final double[] vector2D, final int vector2DOffset) {
		return vector2D[vector2DOffset + 0];
	}
	
	/**
	 * Returns the value of the Y-component from the vector contained in {@code vector2D} at offset {@code 0}.
	 * <p>
	 * If {@code vector2D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector2D.length < 2}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector2DGetY(vector2D, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector2D a {@code double[]} that contains a vector with two components
	 * @return the value of the Y-component from the vector contained in {@code vector2D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector2D.length < 2}
	 * @throws NullPointerException thrown if, and only if, {@code vector2D} is {@code null}
	 */
	public static double vector2DGetY(final double[] vector2D) {
		return vector2DGetY(vector2D, 0);
	}
	
	/**
	 * Returns the value of the Y-component from the vector contained in {@code vector2D} at offset {@code vector2DOffset}.
	 * <p>
	 * If {@code vector2D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector2D.length < vector2DOffset + 2} or {@code vector2DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector2D a {@code double[]} that contains a vector with two components
	 * @param vector2DOffset the offset in {@code vector2D} to start at
	 * @return the value of the Y-component from the vector contained in {@code vector2D} at offset {@code vector2DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector2D.length < vector2DOffset + 2} or {@code vector2DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code vector2D} is {@code null}
	 */
	public static double vector2DGetY(final double[] vector2D, final int vector2DOffset) {
		return vector2D[vector2DOffset + 1];
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with two components.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector2D(0.0D, 0.0D);
	 * }
	 * </pre>
	 * 
	 * @return a {@code double[]} that contains a vector with two components
	 */
	public static double[] vector2D() {
		return vector2D(0.0D, 0.0D);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with two components.
	 * 
	 * @param component1 the value of component 1, also known as X and U
	 * @param component2 the value of component 2, also known as Y and V
	 * @return a {@code double[]} that contains a vector with two components
	 */
	public static double[] vector2D(final double component1, final double component2) {
		return new double[] {component1, component2};
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with two components and is set to the direction from {@code point2DEye} to {@code point2DTarget}.
	 * <p>
	 * If either {@code point2DEye} or {@code point2DTarget} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point2DEye.length < 2} or {@code point2DTarget.length < 2}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector2DDirection(point2DEye, point2DTarget, Vector.vector2D());
	 * }
	 * </pre>
	 * 
	 * @param point2DEye a {@code double[]} that contains the point to look from
	 * @param point2DTarget a {@code double[]} that contains the point to look at
	 * @return a {@code double[]} that contains a vector with two components and is set to the direction from {@code point2DEye} to {@code point2DTarget}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point2DEye.length < 2} or {@code point2DTarget.length < 2}
	 * @throws NullPointerException thrown if, and only if, either {@code point2DEye} or {@code point2DTarget} are {@code null}
	 */
	public static double[] vector2DDirection(final double[] point2DEye, final double[] point2DTarget) {
		return vector2DDirection(point2DEye, point2DTarget, vector2D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with two components and is set to the direction from {@code point2DEye} to {@code point2DTarget}.
	 * <p>
	 * If either {@code point2DEye}, {@code point2DTarget} or {@code vector2DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point2DEye.length < 2}, {@code point2DTarget.length < 2} or {@code vector2DResult.length < 2}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector2DDirection(point2DEye, point2DTarget, vector2DResult, 0, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param point2DEye a {@code double[]} that contains the point to look from
	 * @param point2DTarget a {@code double[]} that contains the point to look at
	 * @param vector2DResult a {@code double[]} that contains the vector to return
	 * @return a {@code double[]} that contains a vector with two components and is set to the direction from {@code point2DEye} to {@code point2DTarget}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point2DEye.length < 2}, {@code point2DTarget.length < 2} or {@code vector2DResult.length < 2}
	 * @throws NullPointerException thrown if, and only if, either {@code point2DEye}, {@code point2DTarget} or {@code vector2DResult} are {@code null}
	 */
	public static double[] vector2DDirection(final double[] point2DEye, final double[] point2DTarget, final double[] vector2DResult) {
		return vector2DDirection(point2DEye, point2DTarget, vector2DResult, 0, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with two components and is set to the direction from {@code point2DEye} to {@code point2DTarget}.
	 * <p>
	 * If either {@code point2DEye}, {@code point2DTarget} or {@code vector2DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point2DEye.length < point2DEyeOffset + 2}, {@code point2DEyeOffset < 0}, {@code point2DTarget.length < point2DTargetOffset + 2}, {@code point2DTargetOffset < 0}, {@code vector2DResult.length < vector2DResultOffset + 2} or
	 * {@code vector2DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param point2DEye a {@code double[]} that contains the point to look from
	 * @param point2DTarget a {@code double[]} that contains the point to look at
	 * @param vector2DResult a {@code double[]} that contains the vector to return
	 * @param point2DEyeOffset the offset in {@code point2DEye} to start at
	 * @param point2DTargetOffset the offset in {@code point2DTarget} to start at
	 * @param vector2DResultOffset the offset in {@code vector2DResult} to start at
	 * @return a {@code double[]} that contains a vector with two components and is set to the direction from {@code point2DEye} to {@code point2DTarget}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point2DEye.length < point2DEyeOffset + 2}, {@code point2DEyeOffset < 0}, {@code point2DTarget.length < point2DTargetOffset + 2}, {@code point2DTargetOffset < 0},
	 *                                        {@code vector2DResult.length < vector2DResultOffset + 2} or {@code vector2DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code point2DEye}, {@code point2DTarget} or {@code vector2DResult} are {@code null}
	 */
	public static double[] vector2DDirection(final double[] point2DEye, final double[] point2DTarget, final double[] vector2DResult, final int point2DEyeOffset, final int point2DTargetOffset, final int vector2DResultOffset) {
		final double component1 = point2DTarget[point2DTargetOffset + 0] - point2DEye[point2DEyeOffset + 0];
		final double component2 = point2DTarget[point2DTargetOffset + 1] - point2DEye[point2DEyeOffset + 1];
		
		return vector2DSet(vector2DResult, component1, component2, vector2DResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] vector2DDirectionXY(final double[] point3D) {
		return vector2DDirectionXY(point3D, vector2D());
	}
	
//	TODO: Add Javadocs!
	public static double[] vector2DDirectionXY(final double[] point3D, final double[] vector2DResult) {
		return vector2DDirectionXY(point3D, vector2DResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] vector2DDirectionXY(final double[] point3D, final double[] vector2DResult, final int point3DOffset, final int vector2DResultOffset) {
		final double component1 = point3DGetX(point3D, point3DOffset);
		final double component2 = point3DGetY(point3D, point3DOffset);
		
		return vector2DSet(vector2DResult, component1, component2, vector2DResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] vector2DDirectionYZ(final double[] point3D) {
		return vector2DDirectionYZ(point3D, vector2D());
	}
	
//	TODO: Add Javadocs!
	public static double[] vector2DDirectionYZ(final double[] point3D, final double[] vector2DResult) {
		return vector2DDirectionYZ(point3D, vector2DResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] vector2DDirectionYZ(final double[] point3D, final double[] vector2DResult, final int point3DOffset, final int vector2DResultOffset) {
		final double component1 = point3DGetY(point3D, point3DOffset);
		final double component2 = point3DGetZ(point3D, point3DOffset);
		
		return vector2DSet(vector2DResult, component1, component2, vector2DResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] vector2DDirectionZX(final double[] point3D) {
		return vector2DDirectionZX(point3D, vector2D());
	}
	
//	TODO: Add Javadocs!
	public static double[] vector2DDirectionZX(final double[] point3D, final double[] vector2DResult) {
		return vector2DDirectionZX(point3D, vector2DResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] vector2DDirectionZX(final double[] point3D, final double[] vector2DResult, final int point3DOffset, final int vector2DResultOffset) {
		final double component1 = point3DGetZ(point3D, point3DOffset);
		final double component2 = point3DGetX(point3D, point3DOffset);
		
		return vector2DSet(vector2DResult, component1, component2, vector2DResultOffset);
	}
	
	/**
	 * Sets the component values of the vector contained in {@code vector2DResult} at offset {@code 0}.
	 * <p>
	 * Returns {@code vector2DResult}.
	 * <p>
	 * If {@code vector2DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector2DResult.length < 2}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector2DSet(vector2DResult, component1, component2, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector2DResult a {@code double[]} that contains a vector with two components
	 * @param component1 the value of component 1, also known as X or U
	 * @param component2 the value of component 2, also known as Y or V
	 * @return {@code vector2DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector2DResult.length < 2}
	 * @throws NullPointerException thrown if, and only if, {@code vector2DResult} is {@code null}
	 */
	public static double[] vector2DSet(final double[] vector2DResult, final double component1, final double component2) {
		return vector2DSet(vector2DResult, component1, component2, 0);
	}
	
	/**
	 * Sets the component values of the vector contained in {@code vector2DResult} at offset {@code vector2DResultOffset}.
	 * <p>
	 * Returns {@code vector2DResult}.
	 * <p>
	 * If {@code vector2DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector2DResult.length < vector2DResultOffset + 2} or {@code vector2DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector2DResult a {@code double[]} that contains a vector with two components
	 * @param component1 the value of component 1, also known as X or U
	 * @param component2 the value of component 2, also known as Y or V
	 * @param vector2DResultOffset the offset in {@code vector2DResult} to start at
	 * @return {@code vector2DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector2DResult.length < vector2DResultOffset + 2} or {@code vector2DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code vector2DResult} is {@code null}
	 */
	public static double[] vector2DSet(final double[] vector2DResult, final double component1, final double component2, final int vector2DResultOffset) {
		vector2DResult[vector2DResultOffset + 0] = component1;
		vector2DResult[vector2DResultOffset + 1] = component2;
		
		return vector2DResult;
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with two components and is set to the result of {@code vector2DLHS - vector2DRHS}.
	 * <p>
	 * If either {@code vector2DLHS} or {@code vector2DRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector2DLHS.length < 2} or {@code vector2DRHS.length < 2}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector2DSubtract(vector2DLHS, vector2DRHS, Vector.vector2D());
	 * }
	 * </pre>
	 * 
	 * @param vector2DLHS a {@code double[]} that contains the vector on the left-hand side of the expression
	 * @param vector2DRHS a {@code double[]} that contains the vector on the right-hand side of the expression
	 * @return a {@code double[]} that contains a vector with two components and is set to the result of {@code vector2DLHS - vector2DRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector2DLHS.length < 2} or {@code vector2DRHS.length < 2}
	 * @throws NullPointerException thrown if, and only if, either {@code vector2DLHS} or {@code vector2DRHS} are {@code null}
	 */
	public static double[] vector2DSubtract(final double[] vector2DLHS, final double[] vector2DRHS) {
		return vector2DSubtract(vector2DLHS, vector2DRHS, vector2D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with two components and is set to the result of {@code vector2DLHS - vector2DRHS}.
	 * <p>
	 * If either {@code vector2DLHS}, {@code vector2DRHS} or {@code vector2DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector2DLHS.length < 2}, {@code vector2DRHS.length < 2} or {@code vector2DResult.length < 2}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector2DSubtract(vector2DLHS, vector2DRHS, vector2DResult, 0, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector2DLHS a {@code double[]} that contains the vector on the left-hand side of the expression
	 * @param vector2DRHS a {@code double[]} that contains the vector on the right-hand side of the expression
	 * @param vector2DResult a {@code double[]} that contains the vector to return
	 * @return a {@code double[]} that contains a vector with two components and is set to the result of {@code vector2DLHS - vector2DRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector2DLHS.length < 2}, {@code vector2DRHS.length < 2} or {@code vector2DResult.length < 2}
	 * @throws NullPointerException thrown if, and only if, either {@code vector2DLHS}, {@code vector2DRHS} or {@code vector2DResult} are {@code null}
	 */
	public static double[] vector2DSubtract(final double[] vector2DLHS, final double[] vector2DRHS, final double[] vector2DResult) {
		return vector2DSubtract(vector2DLHS, vector2DRHS, vector2DResult, 0, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with two components and is set to the result of {@code vector2DLHS - vector2DRHS}.
	 * <p>
	 * If either {@code vector2DLHS}, {@code vector2DRHS} or {@code vector2DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector2DLHS.length < vector2DLHSOffset + 2}, {@code vector2DLHSOffset < 0}, {@code vector2DRHS.length < vector2DRHSOffset + 2}, {@code vector2DRHSOffset < 0}, {@code vector2DResult.length < vector2DResultOffset + 2} or
	 * {@code vector2DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector2DLHS a {@code double[]} that contains the vector on the left-hand side of the expression
	 * @param vector2DRHS a {@code double[]} that contains the vector on the right-hand side of the expression
	 * @param vector2DResult a {@code double[]} that contains the vector to return
	 * @param vector2DLHSOffset the offset in {@code vector2DLHS} to start at
	 * @param vector2DRHSOffset the offset in {@code vector2DRHS} to start at
	 * @param vector2DResultOffset the offset in {@code vector2DResult} to start at
	 * @return a {@code double[]} that contains a vector with two components and is set to the result of {@code vector2DLHS - vector2DRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector2DLHS.length < vector2DLHSOffset + 2}, {@code vector2DLHSOffset < 0}, {@code vector2DRHS.length < vector2DRHSOffset + 2}, {@code vector2DRHSOffset < 0},
	 *                                        {@code vector2DResult.length < vector2DResultOffset + 2} or {@code vector2DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code vector2DLHS}, {@code vector2DRHS} or {@code vector2DResult} are {@code null}
	 */
	public static double[] vector2DSubtract(final double[] vector2DLHS, final double[] vector2DRHS, final double[] vector2DResult, final int vector2DLHSOffset, final int vector2DRHSOffset, final int vector2DResultOffset) {
		final double component1 = vector2DLHS[vector2DLHSOffset + 0] - vector2DRHS[vector2DRHSOffset + 0];
		final double component2 = vector2DLHS[vector2DLHSOffset + 1] - vector2DRHS[vector2DRHSOffset + 1];
		
		return vector2DSet(vector2DResult, component1, component2, vector2DResultOffset);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Vector3D ////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code double} that contains the dot product between the vectors contained in {@code vector3DLHS} and {@code vector3DRHS}.
	 * <p>
	 * If either {@code vector3DLHS} or {@code vector3DRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DLHS.length < 3} or {@code vector3DRHS.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DDotProduct(vector3DLHS, vector3DRHS, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3DLHS a {@code double[]} that contains the vector on the left-hand side of the dot product
	 * @param vector3DRHS a {@code double[]} that contains the vector on the right-hand side of the dot product
	 * @return a {@code double} that contains the dot product between the vectors contained in {@code vector3DLHS} and {@code vector3DRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DLHS.length < 3} or {@code vector3DRHS.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DLHS} or {@code vector3DRHS} are {@code null}
	 */
	public static double vector3DDotProduct(final double[] vector3DLHS, final double[] vector3DRHS) {
		return vector3DDotProduct(vector3DLHS, vector3DRHS, 0, 0);
	}
	
	/**
	 * Returns a {@code double} that contains the dot product between the vectors contained in {@code vector3DLHS} and {@code vector3DRHS}.
	 * <p>
	 * If either {@code vector3DLHS} or {@code vector3DRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DLHS.length < vector3DLHSOffset + 3}, {@code vector3DLHSOffset < 0}, {@code vector3DRHS.length < vector3DRHSOffset + 3} or {@code vector3DRHSOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3DLHS a {@code double[]} that contains the vector on the left-hand side of the dot product
	 * @param vector3DRHS a {@code double[]} that contains the vector on the right-hand side of the dot product
	 * @param vector3DLHSOffset the offset in {@code vector3DLHS} to start at
	 * @param vector3DRHSOffset the offset in {@code vector3DRHS} to start at
	 * @return a {@code double} that contains the dot product between the vectors contained in {@code vector3DLHS} and {@code vector3DRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DLHS.length < vector3DLHSOffset + 3}, {@code vector3DLHSOffset < 0}, {@code vector3DRHS.length < vector3DRHSOffset + 3} or {@code vector3DRHSOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DLHS} or {@code vector3DRHS} are {@code null}
	 */
	public static double vector3DDotProduct(final double[] vector3DLHS, final double[] vector3DRHS, final int vector3DLHSOffset, final int vector3DRHSOffset) {
		return vector3DLHS[vector3DLHSOffset + 0] * vector3DRHS[vector3DRHSOffset + 0] + vector3DLHS[vector3DLHSOffset + 1] * vector3DRHS[vector3DRHSOffset + 1] + vector3DLHS[vector3DLHSOffset + 2] * vector3DRHS[vector3DRHSOffset + 2];
	}
	
	/**
	 * Returns the value of component 1 from the vector contained in {@code vector3D} at offset {@code 0}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < 1}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DGetComponent1(vector3D, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @return the value of component 1 from the vector contained in {@code vector3D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < 1}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DGetComponent1(final double[] vector3D) {
		return vector3DGetComponent1(vector3D, 0);
	}
	
	/**
	 * Returns the value of component 1 from the vector contained in {@code vector3D} at offset {@code vector3DOffset}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < vector3DOffset + 1} or {@code vector3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @param vector3DOffset the offset in {@code vector3D} to start at
	 * @return the value of component 1 from the vector contained in {@code vector3D} at offset {@code vector3DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < vector3DOffset + 1} or {@code vector3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DGetComponent1(final double[] vector3D, final int vector3DOffset) {
		return vector3D[vector3DOffset + 0];
	}
	
	/**
	 * Returns the value of component 2 from the vector contained in {@code vector3D} at offset {@code 0}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < 2}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DGetComponent2(vector3D, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @return the value of component 2 from the vector contained in {@code vector3D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < 2}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DGetComponent2(final double[] vector3D) {
		return vector3DGetComponent2(vector3D, 0);
	}
	
	/**
	 * Returns the value of component 2 from the vector contained in {@code vector3D} at offset {@code vector3DOffset}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < vector3DOffset + 2} or {@code vector3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @param vector3DOffset the offset in {@code vector3D} to start at
	 * @return the value of component 2 from the vector contained in {@code vector3D} at offset {@code vector3DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < vector3DOffset + 2} or {@code vector3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DGetComponent2(final double[] vector3D, final int vector3DOffset) {
		return vector3D[vector3DOffset + 1];
	}
	
	/**
	 * Returns the value of component 3 from the vector contained in {@code vector3D} at offset {@code 0}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DGetComponent3(vector3D, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @return the value of component 3 from the vector contained in {@code vector3D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DGetComponent3(final double[] vector3D) {
		return vector3DGetComponent3(vector3D, 0);
	}
	
	/**
	 * Returns the value of component 3 from the vector contained in {@code vector3D} at offset {@code vector3DOffset}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < vector3DOffset + 3} or {@code vector3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @param vector3DOffset the offset in {@code vector3D} to start at
	 * @return the value of component 3 from the vector contained in {@code vector3D} at offset {@code vector3DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < vector3DOffset + 3} or {@code vector3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DGetComponent3(final double[] vector3D, final int vector3DOffset) {
		return vector3D[vector3DOffset + 2];
	}
	
	/**
	 * Returns the value of the U-component from the vector contained in {@code vector3D} at offset {@code 0}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < 1}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DGetU(vector3D, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @return the value of the U-component from the vector contained in {@code vector3D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < 1}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DGetU(final double[] vector3D) {
		return vector3DGetU(vector3D, 0);
	}
	
	/**
	 * Returns the value of the U-component from the vector contained in {@code vector3D} at offset {@code vector3DOffset}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < vector3DOffset + 1} or {@code vector3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @param vector3DOffset the offset in {@code vector3D} to start at
	 * @return the value of the U-component from the vector contained in {@code vector3D} at offset {@code vector3DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < vector3DOffset + 1} or {@code vector3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DGetU(final double[] vector3D, final int vector3DOffset) {
		return vector3D[vector3DOffset + 0];
	}
	
	/**
	 * Returns the value of the V-component from the vector contained in {@code vector3D} at offset {@code 0}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < 2}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DGetV(vector3D, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @return the value of the V-component from the vector contained in {@code vector3D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < 2}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DGetV(final double[] vector3D) {
		return vector3DGetV(vector3D, 0);
	}
	
	/**
	 * Returns the value of the V-component from the vector contained in {@code vector3D} at offset {@code vector3DOffset}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < vector3DOffset + 2} or {@code vector3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @param vector3DOffset the offset in {@code vector3D} to start at
	 * @return the value of the V-component from the vector contained in {@code vector3D} at offset {@code vector3DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < vector3DOffset + 2} or {@code vector3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DGetV(final double[] vector3D, final int vector3DOffset) {
		return vector3D[vector3DOffset + 1];
	}
	
	/**
	 * Returns the value of the W-component from the vector contained in {@code vector3D} at offset {@code 0}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DGetW(vector3D, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @return the value of the W-component from the vector contained in {@code vector3D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DGetW(final double[] vector3D) {
		return vector3DGetW(vector3D, 0);
	}
	
	/**
	 * Returns the value of the W-component from the vector contained in {@code vector3D} at offset {@code vector3DOffset}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < vector3DOffset + 3} or {@code vector3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @param vector3DOffset the offset in {@code vector3D} to start at
	 * @return the value of the W-component from the vector contained in {@code vector3D} at offset {@code vector3DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < vector3DOffset + 3} or {@code vector3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DGetW(final double[] vector3D, final int vector3DOffset) {
		return vector3D[vector3DOffset + 2];
	}
	
	/**
	 * Returns the value of the X-component from the vector contained in {@code vector3D} at offset {@code 0}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < 1}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DGetX(vector3D, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @return the value of the X-component from the vector contained in {@code vector3D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < 1}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DGetX(final double[] vector3D) {
		return vector3DGetX(vector3D, 0);
	}
	
	/**
	 * Returns the value of the X-component from the vector contained in {@code vector3D} at offset {@code vector3DOffset}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < vector3DOffset + 1} or {@code vector3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @param vector3DOffset the offset in {@code vector3D} to start at
	 * @return the value of the X-component from the vector contained in {@code vector3D} at offset {@code vector3DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < vector3DOffset + 1} or {@code vector3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DGetX(final double[] vector3D, final int vector3DOffset) {
		return vector3D[vector3DOffset + 0];
	}
	
	/**
	 * Returns the value of the Y-component from the vector contained in {@code vector3D} at offset {@code 0}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < 2}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DGetY(vector3D, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @return the value of the Y-component from the vector contained in {@code vector3D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < 2}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DGetY(final double[] vector3D) {
		return vector3DGetY(vector3D, 0);
	}
	
	/**
	 * Returns the value of the Y-component from the vector contained in {@code vector3D} at offset {@code vector3DOffset}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < vector3DOffset + 2} or {@code vector3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @param vector3DOffset the offset in {@code vector3D} to start at
	 * @return the value of the Y-component from the vector contained in {@code vector3D} at offset {@code vector3DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < vector3DOffset + 2} or {@code vector3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DGetY(final double[] vector3D, final int vector3DOffset) {
		return vector3D[vector3DOffset + 1];
	}
	
	/**
	 * Returns the value of the Z-component from the vector contained in {@code vector3D} at offset {@code 0}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DGetZ(vector3D, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @return the value of the Z-component from the vector contained in {@code vector3D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DGetZ(final double[] vector3D) {
		return vector3DGetZ(vector3D, 0);
	}
	
	/**
	 * Returns the value of the Z-component from the vector contained in {@code vector3D} at offset {@code vector3DOffset}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < vector3DOffset + 3} or {@code vector3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @param vector3DOffset the offset in {@code vector3D} to start at
	 * @return the value of the Z-component from the vector contained in {@code vector3D} at offset {@code vector3DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < vector3DOffset + 3} or {@code vector3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DGetZ(final double[] vector3D, final int vector3DOffset) {
		return vector3D[vector3DOffset + 2];
	}
	
	/**
	 * Returns a {@code double} that contains the length of the vector contained in {@code vector3D}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DLength(vector3D, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @return a {@code double} that contains the length of the vector contained in {@code vector3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DLength(final double[] vector3D) {
		return vector3DLength(vector3D, 0);
	}
	
	/**
	 * Returns a {@code double} that contains the length of the vector contained in {@code vector3D}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < vector3DOffset + 3} or {@code vector3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @param vector3DOffset the offset in {@code vector3D} to start at
	 * @return a {@code double} that contains the length of the vector contained in {@code vector3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < vector3DOffset + 3} or {@code vector3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DLength(final double[] vector3D, final int vector3DOffset) {
		return sqrt(vector3DLengthSquared(vector3D, vector3DOffset));
	}
	
	/**
	 * Returns a {@code double} that contains the squared length of the vector contained in {@code vector3D}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DLengthSquared(vector3D, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @return a {@code double} that contains the squared length of the vector contained in {@code vector3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DLengthSquared(final double[] vector3D) {
		return vector3DLengthSquared(vector3D, 0);
	}
	
	/**
	 * Returns a {@code double} that contains the squared length of the vector contained in {@code vector3D}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < vector3DOffset + 3} or {@code vector3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @param vector3DOffset the offset in {@code vector3D} to start at
	 * @return a {@code double} that contains the squared length of the vector contained in {@code vector3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < vector3DOffset + 3} or {@code vector3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double vector3DLengthSquared(final double[] vector3D, final int vector3DOffset) {
		return vector3D[vector3DOffset + 0] * vector3D[vector3DOffset + 0] + vector3D[vector3DOffset + 1] * vector3D[vector3DOffset + 1] + vector3D[vector3DOffset + 2] * vector3D[vector3DOffset + 2];
	}
	
//	TODO: Add Javadocs!
	public static double vector3DSphericalPhi(final double[] vector3D) {
		return vector3DSphericalPhi(vector3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double vector3DSphericalPhi(final double[] vector3D, final int vector3DOffset) {
		return getOrAdd(atan2(vector3DGetY(vector3D, vector3DOffset), vector3DGetX(vector3D, vector3DOffset)), 0.0D, PI_MULTIPLIED_BY_2);
	}
	
//	TODO: Add Javadocs!
	public static double vector3DSphericalTheta(final double[] vector3D) {
		return vector3DSphericalTheta(vector3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double vector3DSphericalTheta(final double[] vector3D, final int vector3DOffset) {
		return acos(saturate(vector3DGetZ(vector3D, vector3DOffset), -1.0D, 1.0D));
	}
	
//	TODO: Add Javadocs!
	public static double vector3DTripleProduct(final double[] vector3DLHSDP, final double[] vector3DLHSCP, final double[] vector3DRHSCP) {
		return vector3DTripleProduct(vector3DLHSDP, vector3DLHSCP, vector3DRHSCP, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double vector3DTripleProduct(final double[] vector3DLHSDP, final double[] vector3DLHSCP, final double[] vector3DRHSCP, final int vector3DLHSDPOffset, final int vector3DLHSCPOffset, final int vector3DRHSCPOffset) {
		return vector3DDotProduct(vector3DLHSDP, vector3DCrossProduct(vector3DLHSCP, vector3DRHSCP, vector3D(), vector3DLHSCPOffset, vector3DRHSCPOffset, 0), vector3DLHSDPOffset, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3D(0.0D, 0.0D, 0.0D);
	 * }
	 * </pre>
	 * 
	 * @return a {@code double[]} that contains a vector with three components
	 */
	public static double[] vector3D() {
		return vector3D(0.0D, 0.0D, 0.0D);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components.
	 * 
	 * @param component1 the value of component 1, also known as X and U
	 * @param component2 the value of component 2, also known as Y and V
	 * @param component3 the value of component 3, also known as Z and W
	 * @return a {@code double[]} that contains a vector with three components
	 */
	public static double[] vector3D(final double component1, final double component2, final double component3) {
		return new double[] {component1, component2, component3};
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS + vector3DRHS}.
	 * <p>
	 * If either {@code vector3DLHS} or {@code vector3DRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DLHS.length < 3} or {@code vector3DRHS.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DAdd(vector3DLHS, vector3DRHS, Vector.vector3D());
	 * }
	 * </pre>
	 * 
	 * @param vector3DLHS a {@code double[]} that contains the vector on the left-hand side of the expression
	 * @param vector3DRHS a {@code double[]} that contains the vector on the right-hand side of the expression
	 * @return a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS + vector3DRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DLHS.length < 3} or {@code vector3DRHS.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DLHS} or {@code vector3DRHS} are {@code null}
	 */
	public static double[] vector3DAdd(final double[] vector3DLHS, final double[] vector3DRHS) {
		return vector3DAdd(vector3DLHS, vector3DRHS, vector3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS + vector3DRHS}.
	 * <p>
	 * If either {@code vector3DLHS}, {@code vector3DRHS} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DLHS.length < 3}, {@code vector3DRHS.length < 3} or {@code vector3DResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DAdd(vector3DLHS, vector3DRHS, vector3DResult, 0, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3DLHS a {@code double[]} that contains the vector on the left-hand side of the expression
	 * @param vector3DRHS a {@code double[]} that contains the vector on the right-hand side of the expression
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @return a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS + vector3DRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DLHS.length < 3}, {@code vector3DRHS.length < 3} or {@code vector3DResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DLHS}, {@code vector3DRHS} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DAdd(final double[] vector3DLHS, final double[] vector3DRHS, final double[] vector3DResult) {
		return vector3DAdd(vector3DLHS, vector3DRHS, vector3DResult, 0, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS + vector3DRHS}.
	 * <p>
	 * If either {@code vector3DLHS}, {@code vector3DRHS} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DLHS.length < vector3DLHSOffset + 3}, {@code vector3DLHSOffset < 0}, {@code vector3DRHS.length < vector3DRHSOffset + 3}, {@code vector3DRHSOffset < 0}, {@code vector3DResult.length < vector3DResultOffset + 3} or
	 * {@code vector3DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3DLHS a {@code double[]} that contains the vector on the left-hand side of the expression
	 * @param vector3DRHS a {@code double[]} that contains the vector on the right-hand side of the expression
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @param vector3DLHSOffset the offset in {@code vector3DLHS} to start at
	 * @param vector3DRHSOffset the offset in {@code vector3DRHS} to start at
	 * @param vector3DResultOffset the offset in {@code vector3DResult} to start at
	 * @return a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS + vector3DRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DLHS.length < vector3DLHSOffset + 3}, {@code vector3DLHSOffset < 0}, {@code vector3DRHS.length < vector3DRHSOffset + 3}, {@code vector3DRHSOffset < 0},
	 *                                        {@code vector3DResult.length < vector3DResultOffset + 3} or {@code vector3DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DLHS}, {@code vector3DRHS} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DAdd(final double[] vector3DLHS, final double[] vector3DRHS, final double[] vector3DResult, final int vector3DLHSOffset, final int vector3DRHSOffset, final int vector3DResultOffset) {
		final double component1 = vector3DLHS[vector3DLHSOffset + 0] + vector3DRHS[vector3DRHSOffset + 0];
		final double component2 = vector3DLHS[vector3DLHSOffset + 1] + vector3DRHS[vector3DRHSOffset + 1];
		final double component3 = vector3DLHS[vector3DLHSOffset + 2] + vector3DRHS[vector3DRHSOffset + 2];
		
		return vector3DSet(vector3DResult, component1, component2, component3, vector3DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the cross product between {@code vector3DLHS} and {@code vector3DRHS}.
	 * <p>
	 * If either {@code vector3DLHS} or {@code vector3DRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DLHS.length < 3} or {@code vector3DRHS.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DCrossProduct(vector3DLHS, vector3DRHS, Vector.vector3D());
	 * }
	 * </pre>
	 * 
	 * @param vector3DLHS a {@code double[]} that contains the vector on the left-hand side of the cross product
	 * @param vector3DRHS a {@code double[]} that contains the vector on the right-hand side of the cross product
	 * @return a {@code double[]} that contains a vector with three components and is set to the cross product between {@code vector3DLHS} and {@code vector3DRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DLHS.length < 3} or {@code vector3DRHS.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DLHS} or {@code vector3DRHS} are {@code null}
	 */
	public static double[] vector3DCrossProduct(final double[] vector3DLHS, final double[] vector3DRHS) {
		return vector3DCrossProduct(vector3DLHS, vector3DRHS, vector3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the cross product between {@code vector3DLHS} and {@code vector3DRHS}.
	 * <p>
	 * If either {@code vector3DLHS}, {@code vector3DRHS} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DLHS.length < 3}, {@code vector3DRHS.length < 3} or {@code vector3DResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DCrossProduct(vector3DLHS, vector3DRHS, vector3DResult, 0, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3DLHS a {@code double[]} that contains the vector on the left-hand side of the cross product
	 * @param vector3DRHS a {@code double[]} that contains the vector on the right-hand side of the cross product
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @return a {@code double[]} that contains a vector with three components and is set to the cross product between {@code vector3DLHS} and {@code vector3DRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DLHS.length < 3}, {@code vector3DRHS.length < 3} or {@code vector3DResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DLHS}, {@code vector3DRHS} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DCrossProduct(final double[] vector3DLHS, final double[] vector3DRHS, final double[] vector3DResult) {
		return vector3DCrossProduct(vector3DLHS, vector3DRHS, vector3DResult, 0, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the cross product between {@code vector3DLHS} and {@code vector3DRHS}.
	 * <p>
	 * If either {@code vector3DLHS}, {@code vector3DRHS} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DLHS.length < vector3DLHSOffset + 3}, {@code vector3DLHSOffset < 0}, {@code vector3DRHS.length < vector3DRHSOffset + 3}, {@code vector3DRHSOffset < 0}, {@code vector3DResult.length < vector3DResultOffset + 3} or
	 * {@code vector3DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3DLHS a {@code double[]} that contains the vector on the left-hand side of the cross product
	 * @param vector3DRHS a {@code double[]} that contains the vector on the right-hand side of the cross product
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @param vector3DLHSOffset the offset in {@code vector3DLHS} to start at
	 * @param vector3DRHSOffset the offset in {@code vector3DRHS} to start at
	 * @param vector3DResultOffset the offset in {@code vector3DResult} to start at
	 * @return a {@code double[]} that contains a vector with three components and is set to the cross product between {@code vector3DLHS} and {@code vector3DRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DLHS.length < vector3DLHSOffset + 3}, {@code vector3DLHSOffset < 0}, {@code vector3DRHS.length < vector3DRHSOffset + 3}, {@code vector3DRHSOffset < 0},
	 *                                        {@code vector3DResult.length < vector3DResultOffset + 3} or {@code vector3DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DLHS}, {@code vector3DRHS} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DCrossProduct(final double[] vector3DLHS, final double[] vector3DRHS, final double[] vector3DResult, final int vector3DLHSOffset, final int vector3DRHSOffset, final int vector3DResultOffset) {
		final double component1 = vector3DLHS[vector3DLHSOffset + 1] * vector3DRHS[vector3DRHSOffset + 2] - vector3DLHS[vector3DLHSOffset + 2] * vector3DRHS[vector3DRHSOffset + 1];
		final double component2 = vector3DLHS[vector3DLHSOffset + 2] * vector3DRHS[vector3DRHSOffset + 0] - vector3DLHS[vector3DLHSOffset + 0] * vector3DRHS[vector3DRHSOffset + 2];
		final double component3 = vector3DLHS[vector3DLHSOffset + 0] * vector3DRHS[vector3DRHSOffset + 1] - vector3DLHS[vector3DLHSOffset + 1] * vector3DRHS[vector3DRHSOffset + 0];
		
		return vector3DSet(vector3DResult, component1, component2, component3, vector3DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the direction from {@code point3DEye} to {@code point3DTarget}.
	 * <p>
	 * If either {@code point3DEye} or {@code point3DTarget} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point3DEye.length < 3} or {@code point3DTarget.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DDirection(point3DEye, point3DTarget, Vector.vector3D());
	 * }
	 * </pre>
	 * 
	 * @param point3DEye a {@code double[]} that contains the point to look from
	 * @param point3DTarget a {@code double[]} that contains the point to look at
	 * @return a {@code double[]} that contains a vector with three components and is set to the direction from {@code point3DEye} to {@code point3DTarget}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point3DEye.length < 3} or {@code point3DTarget.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code point3DEye} or {@code point3DTarget} are {@code null}
	 */
	public static double[] vector3DDirection(final double[] point3DEye, final double[] point3DTarget) {
		return vector3DDirection(point3DEye, point3DTarget, vector3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the direction from {@code point3DEye} to {@code point3DTarget}.
	 * <p>
	 * If either {@code point3DEye}, {@code point3DTarget} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point3DEye.length < 3}, {@code point3DTarget.length < 3} or {@code vector3DResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DDirection(point3DEye, point3DTarget, vector3DResult, 0, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param point3DEye a {@code double[]} that contains the point to look from
	 * @param point3DTarget a {@code double[]} that contains the point to look at
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @return a {@code double[]} that contains a vector with three components and is set to the direction from {@code point3DEye} to {@code point3DTarget}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point3DEye.length < 3}, {@code point3DTarget.length < 3} or {@code vector3DResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code point3DEye}, {@code point3DTarget} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DDirection(final double[] point3DEye, final double[] point3DTarget, final double[] vector3DResult) {
		return vector3DDirection(point3DEye, point3DTarget, vector3DResult, 0, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the direction from {@code point3DEye} to {@code point3DTarget}.
	 * <p>
	 * If either {@code point3DEye}, {@code point3DTarget} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point3DEye.length < point3DEyeOffset + 3}, {@code point3DEyeOffset < 0}, {@code point3DTarget.length < point3DTargetOffset + 3}, {@code point3DTargetOffset < 0}, {@code vector3DResult.length < vector3DResultOffset + 3} or
	 * {@code vector3DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param point3DEye a {@code double[]} that contains the point to look from
	 * @param point3DTarget a {@code double[]} that contains the point to look at
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @param point3DEyeOffset the offset in {@code point3DEye} to start at
	 * @param point3DTargetOffset the offset in {@code point3DTarget} to start at
	 * @param vector3DResultOffset the offset in {@code vector3DResult} to start at
	 * @return a {@code double[]} that contains a vector with three components and is set to the direction from {@code point3DEye} to {@code point3DTarget}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point3DEye.length < point3DEyeOffset + 3}, {@code point3DEyeOffset < 0}, {@code point3DTarget.length < point3DTargetOffset + 3}, {@code point3DTargetOffset < 0},
	 *                                        {@code vector3DResult.length < vector3DResultOffset + 3} or {@code vector3DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code point3DEye}, {@code point3DTarget} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DDirection(final double[] point3DEye, final double[] point3DTarget, final double[] vector3DResult, final int point3DEyeOffset, final int point3DTargetOffset, final int vector3DResultOffset) {
		final double component1 = point3DTarget[point3DTargetOffset + 0] - point3DEye[point3DEyeOffset + 0];
		final double component2 = point3DTarget[point3DTargetOffset + 1] - point3DEye[point3DEyeOffset + 1];
		final double component3 = point3DTarget[point3DTargetOffset + 2] - point3DEye[point3DEyeOffset + 2];
		
		return vector3DSet(vector3DResult, component1, component2, component3, vector3DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the normalized direction from {@code point3DEye} to {@code point3DTarget}.
	 * <p>
	 * If either {@code point3DEye} or {@code point3DTarget} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point3DEye.length < 3} or {@code point3DTarget.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DDirectionNormalized(point3DEye, point3DTarget, Vector.vector3D());
	 * }
	 * </pre>
	 * 
	 * @param point3DEye a {@code double[]} that contains the point to look from
	 * @param point3DTarget a {@code double[]} that contains the point to look at
	 * @return a {@code double[]} that contains a vector with three components and is set to the normalized direction from {@code point3DEye} to {@code point3DTarget}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point3DEye.length < 3} or {@code point3DTarget.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code point3DEye} or {@code point3DTarget} are {@code null}
	 */
	public static double[] vector3DDirectionNormalized(final double[] point3DEye, final double[] point3DTarget) {
		return vector3DDirectionNormalized(point3DEye, point3DTarget, vector3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the normalized direction from {@code point3DEye} to {@code point3DTarget}.
	 * <p>
	 * If either {@code point3DEye}, {@code point3DTarget} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point3DEye.length < 3}, {@code point3DTarget.length < 3} or {@code vector3DResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DDirectionNormalized(point3DEye, point3DTarget, vector3DResult, 0, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param point3DEye a {@code double[]} that contains the point to look from
	 * @param point3DTarget a {@code double[]} that contains the point to look at
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @return a {@code double[]} that contains a vector with three components and is set to the normalized direction from {@code point3DEye} to {@code point3DTarget}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point3DEye.length < 3}, {@code point3DTarget.length < 3} or {@code vector3DResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code point3DEye}, {@code point3DTarget} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DDirectionNormalized(final double[] point3DEye, final double[] point3DTarget, final double[] vector3DResult) {
		return vector3DDirectionNormalized(point3DEye, point3DTarget, vector3DResult, 0, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the normalized direction from {@code point3DEye} to {@code point3DTarget}.
	 * <p>
	 * If either {@code point3DEye}, {@code point3DTarget} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point3DEye.length < point3DEyeOffset + 3}, {@code point3DEyeOffset < 0}, {@code point3DTarget.length < point3DTargetOffset + 3}, {@code point3DTargetOffset < 0}, {@code vector3DResult.length < vector3DResultOffset + 3} or
	 * {@code vector3DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param point3DEye a {@code double[]} that contains the point to look from
	 * @param point3DTarget a {@code double[]} that contains the point to look at
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @param point3DEyeOffset the offset in {@code point3DEye} to start at
	 * @param point3DTargetOffset the offset in {@code point3DTarget} to start at
	 * @param vector3DResultOffset the offset in {@code vector3DResult} to start at
	 * @return a {@code double[]} that contains a vector with three components and is set to the normalized direction from {@code point3DEye} to {@code point3DTarget}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point3DEye.length < point3DEyeOffset + 3}, {@code point3DEyeOffset < 0}, {@code point3DTarget.length < point3DTargetOffset + 3}, {@code point3DTargetOffset < 0},
	 *                                        {@code vector3DResult.length < vector3DResultOffset + 3} or {@code vector3DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code point3DEye}, {@code point3DTarget} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DDirectionNormalized(final double[] point3DEye, final double[] point3DTarget, final double[] vector3DResult, final int point3DEyeOffset, final int point3DTargetOffset, final int vector3DResultOffset) {
		return vector3DNormalize(vector3DDirection(point3DEye, point3DTarget, vector3DResult, point3DEyeOffset, point3DTargetOffset, vector3DResultOffset), vector3DResult, vector3DResultOffset, vector3DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS / scalarRHS}.
	 * <p>
	 * If {@code vector3DLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DLHS.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DDivide(vector3DLHS, scalarRHS, Vector.vector3D());
	 * }
	 * </pre>
	 * 
	 * @param vector3DLHS a {@code double[]} that contains the vector on the left-hand side of the expression
	 * @param scalarRHS a {@code double} that contains the scalar value on the right-hand side of the expression
	 * @return a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS / scalarRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DLHS.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code vector3DLHS} is {@code null}
	 */
	public static double[] vector3DDivide(final double[] vector3DLHS, final double scalarRHS) {
		return vector3DDivide(vector3DLHS, scalarRHS, vector3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS / scalarRHS}.
	 * <p>
	 * If either {@code vector3DLHS} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DLHS.length < 3} or {@code vector3DResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DDivide(vector3DLHS, scalarRHS, vector3DResult, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3DLHS a {@code double[]} that contains the vector on the left-hand side of the expression
	 * @param scalarRHS a {@code double} that contains the scalar value on the right-hand side of the expression
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @return a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS / scalarRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DLHS.length < 3} or {@code vector3DResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DLHS} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DDivide(final double[] vector3DLHS, final double scalarRHS, final double[] vector3DResult) {
		return vector3DDivide(vector3DLHS, scalarRHS, vector3DResult, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS / scalarRHS}.
	 * <p>
	 * If either {@code vector3DLHS} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DLHS.length < vector3DLHSOffset + 3}, {@code vector3DLHSOffset < 0}, {@code vector3DResult.length < vector3DResultOffset + 3} or {@code vector3DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3DLHS a {@code double[]} that contains the vector on the left-hand side of the expression
	 * @param scalarRHS a {@code double} that contains the scalar value on the right-hand side of the expression
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @param vector3DLHSOffset the offset in {@code vector3DLHS} to start at
	 * @param vector3DResultOffset the offset in {@code vector3DResult} to start at
	 * @return a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS / scalarRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DLHS.length < vector3DLHSOffset + 3}, {@code vector3DLHSOffset < 0}, {@code vector3DResult.length < vector3DResultOffset + 3} or {@code vector3DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DLHS} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DDivide(final double[] vector3DLHS, final double scalarRHS, final double[] vector3DResult, final int vector3DLHSOffset, final int vector3DResultOffset) {
		final double component1 = vector3DLHS[vector3DLHSOffset + 0] / scalarRHS;
		final double component2 = vector3DLHS[vector3DLHSOffset + 1] / scalarRHS;
		final double component3 = vector3DLHS[vector3DLHSOffset + 2] / scalarRHS;
		
		return vector3DSet(vector3DResult, component1, component2, component3, vector3DResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] vector3DFromBarycentricCoordinates(final double[] vector3DA, final double[] vector3DB, final double[] vector3DC, final double[] point3DBarycentricCoordinates) {
		return vector3DFromBarycentricCoordinates(vector3DA, vector3DB, vector3DC, point3DBarycentricCoordinates, vector3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] vector3DFromBarycentricCoordinates(final double[] vector3DA, final double[] vector3DB, final double[] vector3DC, final double[] point3DBarycentricCoordinates, final double[] vector3DResult) {
		return vector3DFromBarycentricCoordinates(vector3DA, vector3DB, vector3DC, point3DBarycentricCoordinates, vector3DResult, 0, 0, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] vector3DFromBarycentricCoordinates(final double[] vector3DA, final double[] vector3DB, final double[] vector3DC, final double[] point3DBarycentricCoordinates, final double[] vector3DResult, final int vector3DAOffset, final int vector3DBOffset, final int vector3DCOffset, final int point3DBarycentricCoordinatesOffset, final int vector3DResultOffset) {
		final double aComponent1 = vector3DGetComponent1(vector3DA, vector3DAOffset);
		final double aComponent2 = vector3DGetComponent2(vector3DA, vector3DAOffset);
		final double aComponent3 = vector3DGetComponent3(vector3DA, vector3DAOffset);
		
		final double bComponent1 = vector3DGetComponent1(vector3DB, vector3DBOffset);
		final double bComponent2 = vector3DGetComponent2(vector3DB, vector3DBOffset);
		final double bComponent3 = vector3DGetComponent3(vector3DB, vector3DBOffset);
		
		final double cComponent1 = vector3DGetComponent1(vector3DC, vector3DCOffset);
		final double cComponent2 = vector3DGetComponent2(vector3DC, vector3DCOffset);
		final double cComponent3 = vector3DGetComponent3(vector3DC, vector3DCOffset);
		
		final double barycentricCoordinatesU = point3DGetU(point3DBarycentricCoordinates, point3DBarycentricCoordinatesOffset);
		final double barycentricCoordinatesV = point3DGetV(point3DBarycentricCoordinates, point3DBarycentricCoordinatesOffset);
		final double barycentricCoordinatesW = point3DGetW(point3DBarycentricCoordinates, point3DBarycentricCoordinatesOffset);
		
		final double component1 = aComponent1 * barycentricCoordinatesU + bComponent1 * barycentricCoordinatesV + cComponent1 * barycentricCoordinatesW;
		final double component2 = aComponent2 * barycentricCoordinatesU + bComponent2 * barycentricCoordinatesV + cComponent2 * barycentricCoordinatesW;
		final double component3 = aComponent3 * barycentricCoordinatesU + bComponent3 * barycentricCoordinatesV + cComponent3 * barycentricCoordinatesW;
		
		return vector3DSet(vector3DResult, component1, component2, component3, vector3DResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] vector3DFromBarycentricCoordinatesNormalized(final double[] vector3DA, final double[] vector3DB, final double[] vector3DC, final double[] point3DBarycentricCoordinates) {
		return vector3DFromBarycentricCoordinatesNormalized(vector3DA, vector3DB, vector3DC, point3DBarycentricCoordinates, vector3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] vector3DFromBarycentricCoordinatesNormalized(final double[] vector3DA, final double[] vector3DB, final double[] vector3DC, final double[] point3DBarycentricCoordinates, final double[] vector3DResult) {
		return vector3DFromBarycentricCoordinatesNormalized(vector3DA, vector3DB, vector3DC, point3DBarycentricCoordinates, vector3DResult, 0, 0, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] vector3DFromBarycentricCoordinatesNormalized(final double[] vector3DA, final double[] vector3DB, final double[] vector3DC, final double[] point3DBarycentricCoordinates, final double[] vector3DResult, final int vector3DAOffset, final int vector3DBOffset, final int vector3DCOffset, final int point3DBarycentricCoordinatesOffset, final int vector3DResultOffset) {
		return vector3DNormalize(vector3DFromBarycentricCoordinates(vector3DA, vector3DB, vector3DC, point3DBarycentricCoordinates, vector3DResult, vector3DAOffset, vector3DBOffset, vector3DCOffset, point3DBarycentricCoordinatesOffset, vector3DResultOffset), vector3DResult, vector3DResultOffset, vector3DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the component values of the point {@code point3D}.
	 * <p>
	 * If {@code point3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point3D.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DFromPoint3D(point3D, Vector.vector3D());
	 * }
	 * </pre>
	 * 
	 * @param point3D a {@code double[]} that contains a point with three components
	 * @return a {@code double[]} that contains a vector with three components and is set to the component values of the point {@code point3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point3D.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code point3D} is {@code null}
	 */
	public static double[] vector3DFromPoint3D(final double[] point3D) {
		return vector3DFromPoint3D(point3D, vector3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the component values of the point {@code point3D}.
	 * <p>
	 * If either {@code point3D} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point3D.length < 3} or {@code vector3DResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DFromPoint3D(point3D, vector3DResult, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param point3D a {@code double[]} that contains a point with three components
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @return a {@code double[]} that contains a vector with three components and is set to the component values of the point {@code point3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point3D.length < 3} or {@code vector3DResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code point3D} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DFromPoint3D(final double[] point3D, final double[] vector3DResult) {
		return vector3DFromPoint3D(point3D, vector3DResult, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the component values of the point {@code point3D}.
	 * <p>
	 * If either {@code point3D} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point3D.length < point3DOffset + 3}, {@code point3DOffset < 0}, {@code vector3DResult.length < vector3DResultOffset + 3} or {@code vector3DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param point3D a {@code double[]} that contains a point with three components
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @param point3DOffset the offset in {@code point3D} to start at
	 * @param vector3DResultOffset the offset in {@code vector3DResult} to start at
	 * @return a {@code double[]} that contains a vector with three components and is set to the component values of the point {@code point3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point3D.length < point3DOffset + 3}, {@code point3DOffset < 0}, {@code vector3DResult.length < vector3DResultOffset + 3} or {@code vector3DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code point3D} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DFromPoint3D(final double[] point3D, final double[] vector3DResult, final int point3DOffset, final int vector3DResultOffset) {
		final double component1 = point3D[point3DOffset + 0];
		final double component2 = point3D[point3DOffset + 1];
		final double component3 = point3D[point3DOffset + 2];
		
		return vector3DSet(vector3DResult, component1, component2, component3, vector3DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS * scalarRHS}.
	 * <p>
	 * If {@code vector3DLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DLHS.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DMultiply(vector3DLHS, scalarRHS, Vector.vector3D());
	 * }
	 * </pre>
	 * 
	 * @param vector3DLHS a {@code double[]} that contains the vector on the left-hand side of the expression
	 * @param scalarRHS a {@code double} that contains the scalar value on the right-hand side of the expression
	 * @return a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS * scalarRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DLHS.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code vector3DLHS} is {@code null}
	 */
	public static double[] vector3DMultiply(final double[] vector3DLHS, final double scalarRHS) {
		return vector3DMultiply(vector3DLHS, scalarRHS, vector3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS * scalarRHS}.
	 * <p>
	 * If either {@code vector3DLHS} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DLHS.length < 3} or {@code vector3DResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DMultiply(vector3DLHS, scalarRHS, vector3DResult, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3DLHS a {@code double[]} that contains the vector on the left-hand side of the expression
	 * @param scalarRHS a {@code double} that contains the scalar value on the right-hand side of the expression
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @return a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS * scalarRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DLHS.length < 3} or {@code vector3DResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DLHS} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DMultiply(final double[] vector3DLHS, final double scalarRHS, final double[] vector3DResult) {
		return vector3DMultiply(vector3DLHS, scalarRHS, vector3DResult, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS * scalarRHS}.
	 * <p>
	 * If either {@code vector3DLHS} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DLHS.length < vector3DLHSOffset + 3}, {@code vector3DLHSOffset < 0}, {@code vector3DResult.length < vector3DResultOffset + 3} or {@code vector3DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3DLHS a {@code double[]} that contains the vector on the left-hand side of the expression
	 * @param scalarRHS a {@code double} that contains the scalar value on the right-hand side of the expression
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @param vector3DLHSOffset the offset in {@code vector3DLHS} to start at
	 * @param vector3DResultOffset the offset in {@code vector3DResult} to start at
	 * @return a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS * scalarRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DLHS.length < vector3DLHSOffset + 3}, {@code vector3DLHSOffset < 0}, {@code vector3DResult.length < vector3DResultOffset + 3} or {@code vector3DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DLHS} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DMultiply(final double[] vector3DLHS, final double scalarRHS, final double[] vector3DResult, final int vector3DLHSOffset, final int vector3DResultOffset) {
		final double component1 = vector3DLHS[vector3DLHSOffset + 0] * scalarRHS;
		final double component2 = vector3DLHS[vector3DLHSOffset + 1] * scalarRHS;
		final double component3 = vector3DLHS[vector3DLHSOffset + 2] * scalarRHS;
		
		return vector3DSet(vector3DResult, component1, component2, component3, vector3DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the normalized representation of {@code vector3D}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DNormalize(vector3D, Vector.vector3D());
	 * }
	 * </pre>
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @return a {@code double[]} that contains a vector with three components and is set to the normalized representation of {@code vector3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double[] vector3DNormalize(final double[] vector3D) {
		return vector3DNormalize(vector3D, vector3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the normalized representation of {@code vector3D}.
	 * <p>
	 * If either {@code vector3D} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < 3} or {@code vector3DResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DNormalize(vector3D, vector3DResult, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @return a {@code double[]} that contains a vector with three components and is set to the normalized representation of {@code vector3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < 3} or {@code vector3DResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3D} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DNormalize(final double[] vector3D, final double[] vector3DResult) {
		return vector3DNormalize(vector3D, vector3DResult, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the normalized representation of {@code vector3D}.
	 * <p>
	 * If either {@code vector3D} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < vector3DOffset + 3}, {@code vector3DOffset < 0}, {@code vector3DResult.length < vector3DResultOffset + 3} or {@code vector3DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3D a {@code double[]} that contains a vector with three components
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @param vector3DOffset the offset in {@code vector3D} to start at
	 * @param vector3DResultOffset the offset in {@code vector3DResult} to start at
	 * @return a {@code double[]} that contains a vector with three components and is set to the normalized representation of {@code vector3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < vector3DOffset + 3}, {@code vector3DOffset < 0}, {@code vector3DResult.length < vector3DResultOffset + 3} or {@code vector3DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3D} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DNormalize(final double[] vector3D, final double[] vector3DResult, final int vector3DOffset, final int vector3DResultOffset) {
		return vector3DDivide(vector3D, vector3DLength(vector3D, vector3DOffset), vector3DResult, vector3DOffset, vector3DResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] vector3DReciprocal(final double[] vector3D) {
		return vector3DReciprocal(vector3D, vector3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] vector3DReciprocal(final double[] vector3D, final double[] vector3DResult) {
		return vector3DReciprocal(vector3D, vector3DResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] vector3DReciprocal(final double[] vector3D, final double[] vector3DResult, final int vector3DOffset, final int vector3DResultOffset) {
		final double component1 = 1.0D / vector3DGetComponent1(vector3D, vector3DOffset);
		final double component2 = 1.0D / vector3DGetComponent2(vector3D, vector3DOffset);
		final double component3 = 1.0D / vector3DGetComponent3(vector3D, vector3DOffset);
		
		return vector3DSet(vector3DResult, component1, component2, component3, vector3DResultOffset);
	}
	
	/**
	 * Sets the component values of the vector contained in {@code vector3DResult} at offset {@code 0}.
	 * <p>
	 * Returns {@code vector3DResult}.
	 * <p>
	 * If {@code vector3DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DSet(vector3DResult, component1, component2, component3, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3DResult a {@code double[]} that contains a vector with three components
	 * @param component1 the value of component 1, also known as X or U
	 * @param component2 the value of component 2, also known as Y or V
	 * @param component3 the value of component 3, also known as Z or W
	 * @return {@code vector3DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code vector3DResult} is {@code null}
	 */
	public static double[] vector3DSet(final double[] vector3DResult, final double component1, final double component2, final double component3) {
		return vector3DSet(vector3DResult, component1, component2, component3, 0);
	}
	
	/**
	 * Sets the component values of the vector contained in {@code vector3DResult} at offset {@code vector3DResultOffset}.
	 * <p>
	 * Returns {@code vector3DResult}.
	 * <p>
	 * If {@code vector3DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DResult.length < vector3DResultOffset + 3} or {@code vector3DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3DResult a {@code double[]} that contains a vector with three components
	 * @param component1 the value of component 1, also known as X or U
	 * @param component2 the value of component 2, also known as Y or V
	 * @param component3 the value of component 3, also known as Z or W
	 * @param vector3DResultOffset the offset in {@code vector3DResult} to start at
	 * @return {@code vector3DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DResult.length < vector3DResultOffset + 3} or {@code vector3DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code vector3DResult} is {@code null}
	 */
	public static double[] vector3DSet(final double[] vector3DResult, final double component1, final double component2, final double component3, final int vector3DResultOffset) {
		vector3DResult[vector3DResultOffset + 0] = component1;
		vector3DResult[vector3DResultOffset + 1] = component2;
		vector3DResult[vector3DResultOffset + 2] = component3;
		
		return vector3DResult;
	}
	
	/**
	 * Sets the component values of the vector contained in {@code vector3DResult} at offset {@code vector3DResultOffset}.
	 * <p>
	 * Returns {@code vector3DResult}.
	 * <p>
	 * If either {@code vector3DResult} or {@code vector3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DResult.length < vector3DResultOffset + 3}, {@code vector3DResultOffset < 0}, {@code vector3D.length < vector3DOffset + 3} or {@code vector3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3DResult a {@code double[]} that contains a vector with three components
	 * @param vector3D a {@code double[]} that contains a vector with component values to set
	 * @param vector3DResultOffset the offset in {@code vector3DResult} to start at
	 * @param vector3DOffset the offset in {@code vector3D} to start at
	 * @return {@code point3DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DResult.length < vector3DResultOffset + 3}, {@code vector3DResultOffset < 0}, {@code vector3D.length < vector3DOffset + 3} or {@code vector3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DResult} or {@code vector3D} are {@code null}
	 */
	public static double[] vector3DSet(final double[] vector3DResult, final double[] vector3D, final int vector3DResultOffset, final int vector3DOffset) {
		vector3DResult[vector3DResultOffset + 0] = vector3D[vector3DOffset + 0];
		vector3DResult[vector3DResultOffset + 1] = vector3D[vector3DOffset + 1];
		vector3DResult[vector3DResultOffset + 2] = vector3D[vector3DOffset + 2];
		
		return vector3DResult;
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS - vector3DRHS}.
	 * <p>
	 * If either {@code vector3DLHS} or {@code vector3DRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DLHS.length < 3} or {@code vector3DRHS.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DSubtract(vector3DLHS, vector3DRHS, Vector.vector3D());
	 * }
	 * </pre>
	 * 
	 * @param vector3DLHS a {@code double[]} that contains the vector on the left-hand side of the expression
	 * @param vector3DRHS a {@code double[]} that contains the vector on the right-hand side of the expression
	 * @return a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS - vector3DRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DLHS.length < 3} or {@code vector3DRHS.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DLHS} or {@code vector3DRHS} are {@code null}
	 */
	public static double[] vector3DSubtract(final double[] vector3DLHS, final double[] vector3DRHS) {
		return vector3DSubtract(vector3DLHS, vector3DRHS, vector3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS - vector3DRHS}.
	 * <p>
	 * If either {@code vector3DLHS}, {@code vector3DRHS} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DLHS.length < 3}, {@code vector3DRHS.length < 3} or {@code vector3DResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DSubtract(vector3DLHS, vector3DRHS, vector3DResult, 0, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3DLHS a {@code double[]} that contains the vector on the left-hand side of the expression
	 * @param vector3DRHS a {@code double[]} that contains the vector on the right-hand side of the expression
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @return a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS - vector3DRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DLHS.length < 3}, {@code vector3DRHS.length < 3} or {@code vector3DResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DLHS}, {@code vector3DRHS} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DSubtract(final double[] vector3DLHS, final double[] vector3DRHS, final double[] vector3DResult) {
		return vector3DSubtract(vector3DLHS, vector3DRHS, vector3DResult, 0, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS - vector3DRHS}.
	 * <p>
	 * If either {@code vector3DLHS}, {@code vector3DRHS} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DLHS.length < vector3DLHSOffset + 3}, {@code vector3DLHSOffset < 0}, {@code vector3DRHS.length < vector3DRHSOffset + 3}, {@code vector3DRHSOffset < 0}, {@code vector3DResult.length < vector3DResultOffset + 3} or
	 * {@code vector3DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3DLHS a {@code double[]} that contains the vector on the left-hand side of the expression
	 * @param vector3DRHS a {@code double[]} that contains the vector on the right-hand side of the expression
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @param vector3DLHSOffset the offset in {@code vector3DLHS} to start at
	 * @param vector3DRHSOffset the offset in {@code vector3DRHS} to start at
	 * @param vector3DResultOffset the offset in {@code vector3DResult} to start at
	 * @return a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DLHS - vector3DRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DLHS.length < vector3DLHSOffset + 3}, {@code vector3DLHSOffset < 0}, {@code vector3DRHS.length < vector3DRHSOffset + 3}, {@code vector3DRHSOffset < 0},
	 *                                        {@code vector3DResult.length < vector3DResultOffset + 3} or {@code vector3DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DLHS}, {@code vector3DRHS} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DSubtract(final double[] vector3DLHS, final double[] vector3DRHS, final double[] vector3DResult, final int vector3DLHSOffset, final int vector3DRHSOffset, final int vector3DResultOffset) {
		final double component1 = vector3DLHS[vector3DLHSOffset + 0] - vector3DRHS[vector3DRHSOffset + 0];
		final double component2 = vector3DLHS[vector3DLHSOffset + 1] - vector3DRHS[vector3DRHSOffset + 1];
		final double component3 = vector3DLHS[vector3DLHSOffset + 2] - vector3DRHS[vector3DRHSOffset + 2];
		
		return vector3DSet(vector3DResult, component1, component2, component3, vector3DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DRHS} transformed by {@code matrix44DLHS}.
	 * <p>
	 * If either {@code matrix44DLHS} or {@code vector3DRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DLHS.length < 16} or {@code vector3DRHS.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DTransformMatrix44D(matrix44DLHS, vector3DRHS, Vector.vector3D());
	 * }
	 * </pre>
	 * 
	 * @param matrix44DLHS a {@code double[]} that contains the matrix on the left-hand side of the transformation
	 * @param vector3DRHS a {@code double[]} that contains the vector on the right-hand side of the transformation
	 * @return a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DRHS} transformed by {@code matrix44DLHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DLHS.length < 16} or {@code vector3DRHS.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code matrix44DLHS} or {@code vector3DRHS} are {@code null}
	 */
	public static double[] vector3DTransformMatrix44D(final double[] matrix44DLHS, final double[] vector3DRHS) {
		return vector3DTransformMatrix44D(matrix44DLHS, vector3DRHS, vector3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DRHS} transformed by {@code matrix44DLHS}.
	 * <p>
	 * If either {@code matrix44DLHS}, {@code vector3DRHS} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DLHS.length < 16}, {@code vector3DRHS.length < 3} or {@code vector3DResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DTransformMatrix44D(matrix44DLHS, vector3DRHS, vector3DResult, 0, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param matrix44DLHS a {@code double[]} that contains the matrix on the left-hand side of the transformation
	 * @param vector3DRHS a {@code double[]} that contains the vector on the right-hand side of the transformation
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @return a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DRHS} transformed by {@code matrix44DLHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DLHS.length < 16}, {@code vector3DRHS.length < 3} or {@code vector3DResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code matrix44DLHS}, {@code vector3DRHS} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DTransformMatrix44D(final double[] matrix44DLHS, final double[] vector3DRHS, final double[] vector3DResult) {
		return vector3DTransformMatrix44D(matrix44DLHS, vector3DRHS, vector3DResult, 0, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DRHS} transformed by {@code matrix44DLHS}.
	 * <p>
	 * If either {@code matrix44DLHS}, {@code vector3DRHS} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DLHS.length < matrix44DLHSOffset + 16}, {@code matrix44DLHSOffset < 0}, {@code vector3DRHS.length < vector3DRHSOffset + 3}, {@code vector3DRHSOffset < 0}, {@code vector3DResult.length < vector3DResultOffset + 3} or
	 * {@code vector3DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param matrix44DLHS a {@code double[]} that contains the matrix on the left-hand side of the transformation
	 * @param vector3DRHS a {@code double[]} that contains the vector on the right-hand side of the transformation
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @param matrix44DLHSOffset the offset in {@code matrix44DLHS} to start at
	 * @param vector3DRHSOffset the offset in {@code vector3DRHS} to start at
	 * @param vector3DResultOffset the offset in {@code vector3DResult} to start at
	 * @return a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DRHS} transformed by {@code matrix44DLHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DLHS.length < matrix44DLHSOffset + 16}, {@code matrix44DLHSOffset < 0}, {@code vector3DRHS.length < vector3DRHSOffset + 3}, {@code vector3DRHSOffset < 0},
	 *                                        {@code vector3DResult.length < vector3DResultOffset + 3} or {@code vector3DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code matrix44DLHS}, {@code vector3DRHS} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DTransformMatrix44D(final double[] matrix44DLHS, final double[] vector3DRHS, final double[] vector3DResult, final int matrix44DLHSOffset, final int vector3DRHSOffset, final int vector3DResultOffset) {
		final double component1 = matrix44DLHS[matrix44DLHSOffset + 0] * vector3DRHS[vector3DRHSOffset + 0] + matrix44DLHS[matrix44DLHSOffset + 1] * vector3DRHS[vector3DRHSOffset + 1] + matrix44DLHS[matrix44DLHSOffset +  2] * vector3DRHS[vector3DRHSOffset + 2];
		final double component2 = matrix44DLHS[matrix44DLHSOffset + 4] * vector3DRHS[vector3DRHSOffset + 0] + matrix44DLHS[matrix44DLHSOffset + 5] * vector3DRHS[vector3DRHSOffset + 1] + matrix44DLHS[matrix44DLHSOffset +  6] * vector3DRHS[vector3DRHSOffset + 2];
		final double component3 = matrix44DLHS[matrix44DLHSOffset + 8] * vector3DRHS[vector3DRHSOffset + 0] + matrix44DLHS[matrix44DLHSOffset + 9] * vector3DRHS[vector3DRHSOffset + 1] + matrix44DLHS[matrix44DLHSOffset + 10] * vector3DRHS[vector3DRHSOffset + 2];
		
		return vector3DSet(vector3DResult, component1, component2, component3, vector3DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DRHS} transformed by {@code matrix44DLHS} in transpose order.
	 * <p>
	 * If either {@code matrix44DLHS} or {@code vector3DRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DLHS.length < 16} or {@code vector3DRHS.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DTransformTransposeMatrix44D(matrix44DLHS, vector3DRHS, Vector.vector3D());
	 * }
	 * </pre>
	 * 
	 * @param matrix44DLHS a {@code double[]} that contains the matrix on the left-hand side of the transformation
	 * @param vector3DRHS a {@code double[]} that contains the vector on the right-hand side of the transformation
	 * @return a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DRHS} transformed by {@code matrix44DLHS} in transpose order
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DLHS.length < 16} or {@code vector3DRHS.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code matrix44DLHS} or {@code vector3DRHS} are {@code null}
	 */
	public static double[] vector3DTransformTransposeMatrix44D(final double[] matrix44DLHS, final double[] vector3DRHS) {
		return vector3DTransformTransposeMatrix44D(matrix44DLHS, vector3DRHS, vector3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DRHS} transformed by {@code matrix44DLHS} in transpose order.
	 * <p>
	 * If either {@code matrix44DLHS}, {@code vector3DRHS} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DLHS.length < 16}, {@code vector3DRHS.length < 3} or {@code vector3DResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector.vector3DTransformTransposeMatrix44D(matrix44DLHS, vector3DRHS, vector3DResult, 0, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param matrix44DLHS a {@code double[]} that contains the matrix on the left-hand side of the transformation
	 * @param vector3DRHS a {@code double[]} that contains the vector on the right-hand side of the transformation
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @return a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DRHS} transformed by {@code matrix44DLHS} in transpose order
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DLHS.length < 16}, {@code vector3DRHS.length < 3} or {@code vector3DResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code matrix44DLHS}, {@code vector3DRHS} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DTransformTransposeMatrix44D(final double[] matrix44DLHS, final double[] vector3DRHS, final double[] vector3DResult) {
		return vector3DTransformTransposeMatrix44D(matrix44DLHS, vector3DRHS, vector3DResult, 0, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DRHS} transformed by {@code matrix44DLHS} in transpose order.
	 * <p>
	 * If either {@code matrix44DLHS}, {@code vector3DRHS} or {@code vector3DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DLHS.length < matrix44DLHSOffset + 16}, {@code matrix44DLHSOffset < 0}, {@code vector3DRHS.length < vector3DRHSOffset + 3}, {@code vector3DRHSOffset < 0}, {@code vector3DResult.length < vector3DResultOffset + 3} or
	 * {@code vector3DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param matrix44DLHS a {@code double[]} that contains the matrix on the left-hand side of the transformation
	 * @param vector3DRHS a {@code double[]} that contains the vector on the right-hand side of the transformation
	 * @param vector3DResult a {@code double[]} that contains the vector to return
	 * @param matrix44DLHSOffset the offset in {@code matrix44DLHS} to start at
	 * @param vector3DRHSOffset the offset in {@code vector3DRHS} to start at
	 * @param vector3DResultOffset the offset in {@code vector3DResult} to start at
	 * @return a {@code double[]} that contains a vector with three components and is set to the result of {@code vector3DRHS} transformed by {@code matrix44DLHS} in transpose order
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DLHS.length < matrix44DLHSOffset + 16}, {@code matrix44DLHSOffset < 0}, {@code vector3DRHS.length < vector3DRHSOffset + 3}, {@code vector3DRHSOffset < 0},
	 *                                        {@code vector3DResult.length < vector3DResultOffset + 3} or {@code vector3DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code matrix44DLHS}, {@code vector3DRHS} or {@code vector3DResult} are {@code null}
	 */
	public static double[] vector3DTransformTransposeMatrix44D(final double[] matrix44DLHS, final double[] vector3DRHS, final double[] vector3DResult, final int matrix44DLHSOffset, final int vector3DRHSOffset, final int vector3DResultOffset) {
		final double component1 = matrix44DLHS[matrix44DLHSOffset + 0] * vector3DRHS[vector3DRHSOffset + 0] + matrix44DLHS[matrix44DLHSOffset + 4] * vector3DRHS[vector3DRHSOffset + 1] + matrix44DLHS[matrix44DLHSOffset +  8] * vector3DRHS[vector3DRHSOffset + 2];
		final double component2 = matrix44DLHS[matrix44DLHSOffset + 1] * vector3DRHS[vector3DRHSOffset + 0] + matrix44DLHS[matrix44DLHSOffset + 5] * vector3DRHS[vector3DRHSOffset + 1] + matrix44DLHS[matrix44DLHSOffset +  9] * vector3DRHS[vector3DRHSOffset + 2];
		final double component3 = matrix44DLHS[matrix44DLHSOffset + 2] * vector3DRHS[vector3DRHSOffset + 0] + matrix44DLHS[matrix44DLHSOffset + 6] * vector3DRHS[vector3DRHSOffset + 1] + matrix44DLHS[matrix44DLHSOffset + 10] * vector3DRHS[vector3DRHSOffset + 2];
		
		return vector3DSet(vector3DResult, component1, component2, component3, vector3DResultOffset);
	}
}