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

import static org.dayflower.utility.Doubles.sqrt;

import java.lang.reflect.Field;//TODO: Add Javadocs!

/**
 * A class that consists exclusively of static methods that returns or performs various operations on vectors.
 * <p>
 * This class currently supports the following:
 * <ul>
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
	
//	TODO: Add Javadocs!
	public static double[] vector3DDirection(final double[] point3DEye, final double[] point3DTarget) {
		return vector3DDirection(point3DEye, point3DTarget, vector3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] vector3DDirection(final double[] point3DEye, final double[] point3DTarget, final double[] vector3DResult) {
		return vector3DDirection(point3DEye, point3DTarget, vector3DResult, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] vector3DDirection(final double[] point3DEye, final double[] point3DTarget, final double[] vector3DResult, final int point3DEyeOffset, final int point3DTargetOffset, final int vector3DResultOffset) {
		final double component1 = point3DTarget[point3DTargetOffset + 0] - point3DEye[point3DEyeOffset + 0];
		final double component2 = point3DTarget[point3DTargetOffset + 1] - point3DEye[point3DEyeOffset + 1];
		final double component3 = point3DTarget[point3DTargetOffset + 2] - point3DEye[point3DEyeOffset + 2];
		
		return vector3DSet(vector3DResult, component1, component2, component3, vector3DResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] vector3DDirectionNormalized(final double[] point3DEye, final double[] point3DTarget) {
		return vector3DDirectionNormalized(point3DEye, point3DTarget, vector3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] vector3DDirectionNormalized(final double[] point3DEye, final double[] point3DTarget, final double[] vector3DResult) {
		return vector3DDirectionNormalized(point3DEye, point3DTarget, vector3DResult, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
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