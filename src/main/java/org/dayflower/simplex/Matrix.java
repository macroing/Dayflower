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

import static org.dayflower.utility.Doubles.abs;
import static org.dayflower.utility.Doubles.cos;
import static org.dayflower.utility.Doubles.sin;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on matrices.
 * <p>
 * This class currently supports the following:
 * <ul>
 * <li>{@code Matrix44D} - a 4x4 matrix represented by a {@code double[]}.</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Matrix {
	private Matrix() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the determinant of the matrix contained in {@code matrix44D} at offset {@code 0}.
	 * <p>
	 * If {@code matrix44D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44D.length < 16}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DDeterminant(matrix44D, 0);
	 * }
	 * </pre>
	 * 
	 * @param matrix44D a {@code double[]} that contains a matrix with four rows and four columns
	 * @return the determinant of the matrix contained in {@code matrix44D} at offset {@code 0}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44D.length < 16}
	 * @throws NullPointerException thrown if, and only if, {@code matrix44D} is {@code null}
	 */
	public static double matrix44DDeterminant(final double[] matrix44D) {
		return matrix44DDeterminant(matrix44D, 0);
	}
	
	/**
	 * Returns the determinant of the matrix contained in {@code matrix44D} at offset {@code matrix44DOffset}.
	 * <p>
	 * If {@code matrix44D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44D.length < matrix44DOffset + 16} or {@code matrix44DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param matrix44D a {@code double[]} that contains a matrix with four rows and four columns
	 * @param matrix44DOffset the offset in {@code matrix44D} to start at
	 * @return the determinant of the matrix contained in {@code matrix44D} at offset {@code matrix44DOffset}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44D.length < matrix44DOffset + 16} or {@code matrix44DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code matrix44D} is {@code null}
	 */
	public static double matrix44DDeterminant(final double[] matrix44D, final int matrix44DOffset) {
		final double a = matrix44D[matrix44DOffset +  0] * matrix44D[matrix44DOffset +  5] - matrix44D[matrix44DOffset +  1] * matrix44D[matrix44DOffset +  4];
		final double b = matrix44D[matrix44DOffset +  0] * matrix44D[matrix44DOffset +  6] - matrix44D[matrix44DOffset +  2] * matrix44D[matrix44DOffset +  4];
		final double c = matrix44D[matrix44DOffset +  0] * matrix44D[matrix44DOffset +  7] - matrix44D[matrix44DOffset +  3] * matrix44D[matrix44DOffset +  4];
		final double d = matrix44D[matrix44DOffset +  1] * matrix44D[matrix44DOffset +  6] - matrix44D[matrix44DOffset +  2] * matrix44D[matrix44DOffset +  5];
		final double e = matrix44D[matrix44DOffset +  1] * matrix44D[matrix44DOffset +  7] - matrix44D[matrix44DOffset +  3] * matrix44D[matrix44DOffset +  5];
		final double f = matrix44D[matrix44DOffset +  2] * matrix44D[matrix44DOffset +  7] - matrix44D[matrix44DOffset +  3] * matrix44D[matrix44DOffset +  6];
		final double g = matrix44D[matrix44DOffset +  8] * matrix44D[matrix44DOffset + 13] - matrix44D[matrix44DOffset +  9] * matrix44D[matrix44DOffset + 12];
		final double h = matrix44D[matrix44DOffset +  8] * matrix44D[matrix44DOffset + 14] - matrix44D[matrix44DOffset + 10] * matrix44D[matrix44DOffset + 12];
		final double i = matrix44D[matrix44DOffset +  8] * matrix44D[matrix44DOffset + 15] - matrix44D[matrix44DOffset + 11] * matrix44D[matrix44DOffset + 12];
		final double j = matrix44D[matrix44DOffset +  9] * matrix44D[matrix44DOffset + 14] - matrix44D[matrix44DOffset + 10] * matrix44D[matrix44DOffset + 13];
		final double k = matrix44D[matrix44DOffset +  9] * matrix44D[matrix44DOffset + 15] - matrix44D[matrix44DOffset + 11] * matrix44D[matrix44DOffset + 13];
		final double l = matrix44D[matrix44DOffset + 10] * matrix44D[matrix44DOffset + 15] - matrix44D[matrix44DOffset + 11] * matrix44D[matrix44DOffset + 14];
		
		return a * l - b * k + c * j + d * i - e * h + f * g;
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44D(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	 * }
	 * </pre>
	 * 
	 * @return a {@code double[]} that contains a matrix with four rows and four columns
	 */
	public static double[] matrix44D() {
		return matrix44D(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns.
	 * 
	 * @param element11 the value of the element at row 1 and column 1 or index 0
	 * @param element12 the value of the element at row 1 and column 2 or index 1
	 * @param element13 the value of the element at row 1 and column 3 or index 2
	 * @param element14 the value of the element at row 1 and column 4 or index 3
	 * @param element21 the value of the element at row 2 and column 1 or index 4
	 * @param element22 the value of the element at row 2 and column 2 or index 5
	 * @param element23 the value of the element at row 2 and column 3 or index 6
	 * @param element24 the value of the element at row 2 and column 4 or index 7
	 * @param element31 the value of the element at row 3 and column 1 or index 8
	 * @param element32 the value of the element at row 3 and column 2 or index 9
	 * @param element33 the value of the element at row 3 and column 3 or index 10
	 * @param element34 the value of the element at row 3 and column 4 or index 11
	 * @param element41 the value of the element at row 4 and column 1 or index 12
	 * @param element42 the value of the element at row 4 and column 2 or index 13
	 * @param element43 the value of the element at row 4 and column 3 or index 14
	 * @param element44 the value of the element at row 4 and column 4 or index 15
	 * @return a {@code double[]} that contains a matrix with four rows and four columns
	 */
	public static double[] matrix44D(final double element11, final double element12, final double element13, final double element14, final double element21, final double element22, final double element23, final double element24, final double element31, final double element32, final double element33, final double element34, final double element41, final double element42, final double element43, final double element44) {
		return new double[] {element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44};
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to the identity.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DIdentity(Matrix.matrix44D());
	 * }
	 * </pre>
	 * 
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to the identity
	 */
	public static double[] matrix44DIdentity() {
		return matrix44DIdentity(matrix44D());
	}
	
	/**
	 * Sets the element values of the matrix contained in {@code matrix44DResult} at offset {@code 0} to the identity.
	 * <p>
	 * Returns {@code matrix44DResult}.
	 * <p>
	 * If {@code matrix44DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DResult.length < 16}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DIdentity(matrix44DResult, 0);
	 * }
	 * </pre>
	 * 
	 * @param matrix44DResult a {@code double[]} that contains a matrix with four rows and four columns
	 * @return {@code matrix44DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DResult.length < 16}
	 * @throws NullPointerException thrown if, and only if, {@code matrix44DResult} is {@code null}
	 */
	public static double[] matrix44DIdentity(final double[] matrix44DResult) {
		return matrix44DIdentity(matrix44DResult, 0);
	}
	
	/**
	 * Sets the element values of the matrix contained in {@code matrix44DResult} at offset {@code matrix44DResultOffset} to the identity.
	 * <p>
	 * Returns {@code matrix44DResult}.
	 * <p>
	 * If {@code matrix44DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DResult.length < matrix44DResultOffset + 16} or {@code matrix44DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param matrix44DResult a {@code double[]} that contains a matrix with four rows and four columns
	 * @param matrix44DResultOffset the offset in {@code matrix44DResult} to start at
	 * @return {@code matrix44DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DResult.length < matrix44DResultOffset + 16} or {@code matrix44DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code matrix44DResult} is {@code null}
	 */
	public static double[] matrix44DIdentity(final double[] matrix44DResult, final int matrix44DResultOffset) {
		return matrix44DSet(matrix44DResult, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, matrix44DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to the inverse of {@code matrix44D}.
	 * <p>
	 * If {@code matrix44D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44D.length < 16}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * If {@code matrix44D} cannot be inverted, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DInverse(matrix44D, Matrix.matrix44D());
	 * }
	 * </pre>
	 * 
	 * @param matrix44D a {@code double[]} that contains the matrix to invert
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to the inverse of {@code matrix44D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44D.length < 16}
	 * @throws IllegalArgumentException thrown if, and only if, {@code matrix44D} cannot be inverted
	 * @throws NullPointerException thrown if, and only if, {@code matrix44D} is {@code null}
	 */
	public static double[] matrix44DInverse(final double[] matrix44D) {
		return matrix44DInverse(matrix44D, matrix44D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to the inverse of {@code matrix44D}.
	 * <p>
	 * If either {@code matrix44D} or {@code matrix44DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44D.length < 16} or {@code matrix44DResult.length < 16}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * If {@code matrix44D} cannot be inverted, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DInverse(matrix44D, matrix44DResult, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param matrix44D a {@code double[]} that contains the matrix to invert
	 * @param matrix44DResult a {@code double[]} that contains the matrix to return
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to the inverse of {@code matrix44D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44D.length < 16} or {@code matrix44DResult.length < 16}
	 * @throws IllegalArgumentException thrown if, and only if, {@code matrix44D} cannot be inverted
	 * @throws NullPointerException thrown if, and only if, either {@code matrix44D} or {@code matrix44DResult} are {@code null}
	 */
	public static double[] matrix44DInverse(final double[] matrix44D, final double[] matrix44DResult) {
		return matrix44DInverse(matrix44D, matrix44DResult, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to the inverse of {@code matrix44D}.
	 * <p>
	 * If either {@code matrix44D} or {@code matrix44DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44D.length < matrix44DOffset + 16}, {@code matrix44DOffset < 0}, {@code matrix44DResult.length < matrix44DResultOffset + 16} or {@code matrix44DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * If {@code matrix44D} cannot be inverted, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param matrix44D a {@code double[]} that contains the matrix to invert
	 * @param matrix44DResult a {@code double[]} that contains the matrix to return
	 * @param matrix44DOffset the offset in {@code matrix44D} to start at
	 * @param matrix44DResultOffset the offset in {@code matrix44DResult} to start at
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to the inverse of {@code matrix44D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44D.length < matrix44DOffset + 16}, {@code matrix44DOffset < 0}, {@code matrix44DResult.length < matrix44DResultOffset + 16} or {@code matrix44DResultOffset < 0}
	 * @throws IllegalArgumentException thrown if, and only if, {@code matrix44D} cannot be inverted
	 * @throws NullPointerException thrown if, and only if, either {@code matrix44D} or {@code matrix44DResult} are {@code null}
	 */
	public static double[] matrix44DInverse(final double[] matrix44D, final double[] matrix44DResult, final int matrix44DOffset, final int matrix44DResultOffset) {
		final double a = matrix44D[matrix44DOffset +  0] * matrix44D[matrix44DOffset +  5] - matrix44D[matrix44DOffset +  1] * matrix44D[matrix44DOffset +  4];
		final double b = matrix44D[matrix44DOffset +  0] * matrix44D[matrix44DOffset +  6] - matrix44D[matrix44DOffset +  2] * matrix44D[matrix44DOffset +  4];
		final double c = matrix44D[matrix44DOffset +  0] * matrix44D[matrix44DOffset +  7] - matrix44D[matrix44DOffset +  3] * matrix44D[matrix44DOffset +  4];
		final double d = matrix44D[matrix44DOffset +  1] * matrix44D[matrix44DOffset +  6] - matrix44D[matrix44DOffset +  2] * matrix44D[matrix44DOffset +  5];
		final double e = matrix44D[matrix44DOffset +  1] * matrix44D[matrix44DOffset +  7] - matrix44D[matrix44DOffset +  3] * matrix44D[matrix44DOffset +  5];
		final double f = matrix44D[matrix44DOffset +  2] * matrix44D[matrix44DOffset +  7] - matrix44D[matrix44DOffset +  3] * matrix44D[matrix44DOffset +  6];
		final double g = matrix44D[matrix44DOffset +  8] * matrix44D[matrix44DOffset + 13] - matrix44D[matrix44DOffset +  9] * matrix44D[matrix44DOffset + 12];
		final double h = matrix44D[matrix44DOffset +  8] * matrix44D[matrix44DOffset + 14] - matrix44D[matrix44DOffset + 10] * matrix44D[matrix44DOffset + 12];
		final double i = matrix44D[matrix44DOffset +  8] * matrix44D[matrix44DOffset + 15] - matrix44D[matrix44DOffset + 11] * matrix44D[matrix44DOffset + 12];
		final double j = matrix44D[matrix44DOffset +  9] * matrix44D[matrix44DOffset + 14] - matrix44D[matrix44DOffset + 10] * matrix44D[matrix44DOffset + 13];
		final double k = matrix44D[matrix44DOffset +  9] * matrix44D[matrix44DOffset + 15] - matrix44D[matrix44DOffset + 11] * matrix44D[matrix44DOffset + 13];
		final double l = matrix44D[matrix44DOffset + 10] * matrix44D[matrix44DOffset + 15] - matrix44D[matrix44DOffset + 11] * matrix44D[matrix44DOffset + 14];
		
		final double determinant = a * l - b * k + c * j + d * i - e * h + f * g;
		final double determinantReciprocal = 1.0D / determinant;
		
		if(abs(determinant) < 1.0e-12D) {
			throw new IllegalArgumentException("The matrix 'matrix44D' cannot be inverted!");
		}
		
		final double element11 = (+matrix44D[matrix44DOffset +  5] * l - matrix44D[matrix44DOffset +  6] * k + matrix44D[matrix44DOffset +  7] * j) * determinantReciprocal;
		final double element12 = (-matrix44D[matrix44DOffset +  1] * l + matrix44D[matrix44DOffset +  2] * k - matrix44D[matrix44DOffset +  3] * j) * determinantReciprocal;
		final double element13 = (+matrix44D[matrix44DOffset + 13] * f - matrix44D[matrix44DOffset + 14] * e + matrix44D[matrix44DOffset + 15] * d) * determinantReciprocal;
		final double element14 = (-matrix44D[matrix44DOffset +  9] * f + matrix44D[matrix44DOffset + 10] * e - matrix44D[matrix44DOffset + 11] * d) * determinantReciprocal;
		
		final double element21 = (-matrix44D[matrix44DOffset +  4] * l + matrix44D[matrix44DOffset +  6] * i - matrix44D[matrix44DOffset +  7] * h) * determinantReciprocal;
		final double element22 = (+matrix44D[matrix44DOffset +  0] * l - matrix44D[matrix44DOffset +  2] * i + matrix44D[matrix44DOffset +  3] * h) * determinantReciprocal;
		final double element23 = (-matrix44D[matrix44DOffset + 12] * f + matrix44D[matrix44DOffset + 14] * c - matrix44D[matrix44DOffset + 15] * b) * determinantReciprocal;
		final double element24 = (+matrix44D[matrix44DOffset +  8] * f - matrix44D[matrix44DOffset + 10] * c + matrix44D[matrix44DOffset + 11] * b) * determinantReciprocal;
		
		final double element31 = (+matrix44D[matrix44DOffset +  4] * k - matrix44D[matrix44DOffset +  5] * i + matrix44D[matrix44DOffset +  7] * g) * determinantReciprocal;
		final double element32 = (-matrix44D[matrix44DOffset +  0] * k + matrix44D[matrix44DOffset +  1] * i - matrix44D[matrix44DOffset +  3] * g) * determinantReciprocal;
		final double element33 = (+matrix44D[matrix44DOffset + 12] * e - matrix44D[matrix44DOffset + 13] * c + matrix44D[matrix44DOffset + 15] * a) * determinantReciprocal;
		final double element34 = (-matrix44D[matrix44DOffset +  8] * e + matrix44D[matrix44DOffset +  9] * c - matrix44D[matrix44DOffset + 11] * a) * determinantReciprocal;
		
		final double element41 = (-matrix44D[matrix44DOffset +  4] * j + matrix44D[matrix44DOffset +  5] * h - matrix44D[matrix44DOffset +  6] * g) * determinantReciprocal;
		final double element42 = (+matrix44D[matrix44DOffset +  0] * j - matrix44D[matrix44DOffset +  1] * h + matrix44D[matrix44DOffset +  2] * g) * determinantReciprocal;
		final double element43 = (-matrix44D[matrix44DOffset + 12] * d + matrix44D[matrix44DOffset + 13] * b - matrix44D[matrix44DOffset + 14] * a) * determinantReciprocal;
		final double element44 = (+matrix44D[matrix44DOffset +  8] * d - matrix44D[matrix44DOffset +  9] * b + matrix44D[matrix44DOffset + 10] * a) * determinantReciprocal;
		
		return matrix44DSet(matrix44DResult, element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, matrix44DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to the result of {@code matrix44DLHS} multiplied by {@code matrix44DRHS}.
	 * <p>
	 * If either {@code matrix44DLHS} or {@code matrix44DRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DLHS.length < 16} or {@code matrix44DRHS.length < 16}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DMultiply(matrix44DLHS, matrix44DRHS, Matrix.matrix44D());
	 * }
	 * </pre>
	 * 
	 * @param matrix44DLHS a {@code double[]} that contains the matrix to multiply with on the left-hand side
	 * @param matrix44DRHS a {@code double[]} that contains the matrix to multiply with on the right-hand side
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to the result of {@code matrix44DLHS} multiplied by {@code matrix44DRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DLHS.length < 16} or {@code matrix44DRHS.length < 16}
	 * @throws NullPointerException thrown if, and only if, either {@code matrix44DLHS} or {@code matrix44DRHS} are {@code null}
	 */
	public static double[] matrix44DMultiply(final double[] matrix44DLHS, final double[] matrix44DRHS) {
		return matrix44DMultiply(matrix44DLHS, matrix44DRHS, matrix44D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to the result of {@code matrix44DLHS} multiplied by {@code matrix44DRHS}.
	 * <p>
	 * If either {@code matrix44DLHS}, {@code matrix44DRHS} or {@code matrix44DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DLHS.length < 16}, {@code matrix44DRHS.length < 16} or {@code matrix44DResult.length < 16}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DMultiply(matrix44DLHS, matrix44DRHS, matrix44DResult, 0, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param matrix44DLHS a {@code double[]} that contains the matrix to multiply with on the left-hand side
	 * @param matrix44DRHS a {@code double[]} that contains the matrix to multiply with on the right-hand side
	 * @param matrix44DResult a {@code double[]} that contains the matrix to return
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to the result of {@code matrix44DLHS} multiplied by {@code matrix44DRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DLHS.length < 16}, {@code matrix44DRHS.length < 16} or {@code matrix44DResult.length < 16}
	 * @throws NullPointerException thrown if, and only if, either {@code matrix44DLHS}, {@code matrix44DRHS} or {@code matrix44DResult} are {@code null}
	 */
	public static double[] matrix44DMultiply(final double[] matrix44DLHS, final double[] matrix44DRHS, final double[] matrix44DResult) {
		return matrix44DMultiply(matrix44DLHS, matrix44DRHS, matrix44DResult, 0, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to the result of {@code matrix44DLHS} multiplied by {@code matrix44DRHS}.
	 * <p>
	 * If either {@code matrix44DLHS}, {@code matrix44DRHS} or {@code matrix44DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DLHS.length < matrix44DLHSOffset + 16}, {@code matrix44DLHSOffset < 0}, {@code matrix44DRHS.length < matrix44DRHSOffset + 16}, {@code matrix44DRHSOffset < 0}, {@code matrix44DResult.length < matrix44DResultOffset + 16} or
	 * {@code matrix44DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param matrix44DLHS a {@code double[]} that contains the matrix to multiply with on the left-hand side
	 * @param matrix44DRHS a {@code double[]} that contains the matrix to multiply with on the right-hand side
	 * @param matrix44DResult a {@code double[]} that contains the matrix to return
	 * @param matrix44DLHSOffset the offset in {@code matrix44DLHS} to start at
	 * @param matrix44DRHSOffset the offset in {@code matrix44DRHS} to start at
	 * @param matrix44DResultOffset the offset in {@code matrix44DResult} to start at
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to the result of {@code matrix44DLHS} multiplied by {@code matrix44DRHS}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DLHS.length < matrix44DLHSOffset + 16}, {@code matrix44DLHSOffset < 0}, {@code matrix44DRHS.length < matrix44DRHSOffset + 16}, {@code matrix44DRHSOffset < 0},
	 *                                        {@code matrix44DResult.length < matrix44DResultOffset + 16} or {@code matrix44DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code matrix44DLHS}, {@code matrix44DRHS} or {@code matrix44DResult} are {@code null}
	 */
	public static double[] matrix44DMultiply(final double[] matrix44DLHS, final double[] matrix44DRHS, final double[] matrix44DResult, final int matrix44DLHSOffset, final int matrix44DRHSOffset, final int matrix44DResultOffset) {
		final double element11 = matrix44DLHS[matrix44DLHSOffset +  0] * matrix44DRHS[matrix44DRHSOffset + 0] + matrix44DLHS[matrix44DLHSOffset +  1] * matrix44DRHS[matrix44DRHSOffset + 4] + matrix44DLHS[matrix44DLHSOffset +  2] * matrix44DRHS[matrix44DRHSOffset +  8] + matrix44DLHS[matrix44DLHSOffset +  3] * matrix44DRHS[matrix44DRHSOffset + 12];
		final double element12 = matrix44DLHS[matrix44DLHSOffset +  0] * matrix44DRHS[matrix44DRHSOffset + 1] + matrix44DLHS[matrix44DLHSOffset +  1] * matrix44DRHS[matrix44DRHSOffset + 5] + matrix44DLHS[matrix44DLHSOffset +  2] * matrix44DRHS[matrix44DRHSOffset +  9] + matrix44DLHS[matrix44DLHSOffset +  3] * matrix44DRHS[matrix44DRHSOffset + 13];
		final double element13 = matrix44DLHS[matrix44DLHSOffset +  0] * matrix44DRHS[matrix44DRHSOffset + 2] + matrix44DLHS[matrix44DLHSOffset +  1] * matrix44DRHS[matrix44DRHSOffset + 6] + matrix44DLHS[matrix44DLHSOffset +  2] * matrix44DRHS[matrix44DRHSOffset + 10] + matrix44DLHS[matrix44DLHSOffset +  3] * matrix44DRHS[matrix44DRHSOffset + 14];
		final double element14 = matrix44DLHS[matrix44DLHSOffset +  0] * matrix44DRHS[matrix44DRHSOffset + 3] + matrix44DLHS[matrix44DLHSOffset +  1] * matrix44DRHS[matrix44DRHSOffset + 7] + matrix44DLHS[matrix44DLHSOffset +  2] * matrix44DRHS[matrix44DRHSOffset + 11] + matrix44DLHS[matrix44DLHSOffset +  3] * matrix44DRHS[matrix44DRHSOffset + 15];
		
		final double element21 = matrix44DLHS[matrix44DLHSOffset +  4] * matrix44DRHS[matrix44DRHSOffset + 0] + matrix44DLHS[matrix44DLHSOffset +  5] * matrix44DRHS[matrix44DRHSOffset + 4] + matrix44DLHS[matrix44DLHSOffset +  6] * matrix44DRHS[matrix44DRHSOffset +  8] + matrix44DLHS[matrix44DLHSOffset +  7] * matrix44DRHS[matrix44DRHSOffset + 12];
		final double element22 = matrix44DLHS[matrix44DLHSOffset +  4] * matrix44DRHS[matrix44DRHSOffset + 1] + matrix44DLHS[matrix44DLHSOffset +  5] * matrix44DRHS[matrix44DRHSOffset + 5] + matrix44DLHS[matrix44DLHSOffset +  6] * matrix44DRHS[matrix44DRHSOffset +  9] + matrix44DLHS[matrix44DLHSOffset +  7] * matrix44DRHS[matrix44DRHSOffset + 13];
		final double element23 = matrix44DLHS[matrix44DLHSOffset +  4] * matrix44DRHS[matrix44DRHSOffset + 2] + matrix44DLHS[matrix44DLHSOffset +  5] * matrix44DRHS[matrix44DRHSOffset + 6] + matrix44DLHS[matrix44DLHSOffset +  6] * matrix44DRHS[matrix44DRHSOffset + 10] + matrix44DLHS[matrix44DLHSOffset +  7] * matrix44DRHS[matrix44DRHSOffset + 14];
		final double element24 = matrix44DLHS[matrix44DLHSOffset +  4] * matrix44DRHS[matrix44DRHSOffset + 3] + matrix44DLHS[matrix44DLHSOffset +  5] * matrix44DRHS[matrix44DRHSOffset + 7] + matrix44DLHS[matrix44DLHSOffset +  6] * matrix44DRHS[matrix44DRHSOffset + 11] + matrix44DLHS[matrix44DLHSOffset +  7] * matrix44DRHS[matrix44DRHSOffset + 15];
		
		final double element31 = matrix44DLHS[matrix44DLHSOffset +  8] * matrix44DRHS[matrix44DRHSOffset + 0] + matrix44DLHS[matrix44DLHSOffset +  9] * matrix44DRHS[matrix44DRHSOffset + 4] + matrix44DLHS[matrix44DLHSOffset + 10] * matrix44DRHS[matrix44DRHSOffset +  8] + matrix44DLHS[matrix44DLHSOffset + 11] * matrix44DRHS[matrix44DRHSOffset + 12];
		final double element32 = matrix44DLHS[matrix44DLHSOffset +  8] * matrix44DRHS[matrix44DRHSOffset + 1] + matrix44DLHS[matrix44DLHSOffset +  9] * matrix44DRHS[matrix44DRHSOffset + 5] + matrix44DLHS[matrix44DLHSOffset + 10] * matrix44DRHS[matrix44DRHSOffset +  9] + matrix44DLHS[matrix44DLHSOffset + 11] * matrix44DRHS[matrix44DRHSOffset + 13];
		final double element33 = matrix44DLHS[matrix44DLHSOffset +  8] * matrix44DRHS[matrix44DRHSOffset + 2] + matrix44DLHS[matrix44DLHSOffset +  9] * matrix44DRHS[matrix44DRHSOffset + 6] + matrix44DLHS[matrix44DLHSOffset + 10] * matrix44DRHS[matrix44DRHSOffset + 10] + matrix44DLHS[matrix44DLHSOffset + 11] * matrix44DRHS[matrix44DRHSOffset + 14];
		final double element34 = matrix44DLHS[matrix44DLHSOffset +  8] * matrix44DRHS[matrix44DRHSOffset + 3] + matrix44DLHS[matrix44DLHSOffset +  9] * matrix44DRHS[matrix44DRHSOffset + 7] + matrix44DLHS[matrix44DLHSOffset + 10] * matrix44DRHS[matrix44DRHSOffset + 11] + matrix44DLHS[matrix44DLHSOffset + 11] * matrix44DRHS[matrix44DRHSOffset + 15];
		
		final double element41 = matrix44DLHS[matrix44DLHSOffset + 12] * matrix44DRHS[matrix44DRHSOffset + 0] + matrix44DLHS[matrix44DLHSOffset + 13] * matrix44DRHS[matrix44DRHSOffset + 4] + matrix44DLHS[matrix44DLHSOffset + 14] * matrix44DRHS[matrix44DRHSOffset +  8] + matrix44DLHS[matrix44DLHSOffset + 15] * matrix44DRHS[matrix44DRHSOffset + 12];
		final double element42 = matrix44DLHS[matrix44DLHSOffset + 12] * matrix44DRHS[matrix44DRHSOffset + 1] + matrix44DLHS[matrix44DLHSOffset + 13] * matrix44DRHS[matrix44DRHSOffset + 5] + matrix44DLHS[matrix44DLHSOffset + 14] * matrix44DRHS[matrix44DRHSOffset +  9] + matrix44DLHS[matrix44DLHSOffset + 15] * matrix44DRHS[matrix44DRHSOffset + 13];
		final double element43 = matrix44DLHS[matrix44DLHSOffset + 12] * matrix44DRHS[matrix44DRHSOffset + 2] + matrix44DLHS[matrix44DLHSOffset + 13] * matrix44DRHS[matrix44DRHSOffset + 6] + matrix44DLHS[matrix44DLHSOffset + 14] * matrix44DRHS[matrix44DRHSOffset + 10] + matrix44DLHS[matrix44DLHSOffset + 15] * matrix44DRHS[matrix44DRHSOffset + 14];
		final double element44 = matrix44DLHS[matrix44DLHSOffset + 12] * matrix44DRHS[matrix44DRHSOffset + 3] + matrix44DLHS[matrix44DLHSOffset + 13] * matrix44DRHS[matrix44DRHSOffset + 7] + matrix44DLHS[matrix44DLHSOffset + 14] * matrix44DRHS[matrix44DRHSOffset + 11] + matrix44DLHS[matrix44DLHSOffset + 15] * matrix44DRHS[matrix44DRHSOffset + 15];
		
		return matrix44DSet(matrix44DResult, element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, matrix44DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate using {@code vector3DW}, {@code vector3DV} and {@code vector3DU}.
	 * <p>
	 * If either {@code vector3DW}, {@code vector3DV} or {@code vector3DU} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DW.length < 3}, {@code vector3DV.length < 3} or {@code vector3DU.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DRotateWVU(vector3DW, vector3DV, vector3DU, Matrix.matrix44D());
	 * }
	 * </pre>
	 * 
	 * @param vector3DW a {@code double[]} that contains the W-vector of the rotation
	 * @param vector3DV a {@code double[]} that contains the V-vector of the rotation
	 * @param vector3DU a {@code double[]} that contains the U-vector of the rotation
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate using {@code vector3DW}, {@code vector3DV} and {@code vector3DU}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DW.length < 3}, {@code vector3DV.length < 3} or {@code vector3DU.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DW}, {@code vector3DV} or {@code vector3DU} are {@code null}
	 */
	public static double[] matrix44DRotateWVU(final double[] vector3DW, final double[] vector3DV, final double[] vector3DU) {
		return matrix44DRotateWVU(vector3DW, vector3DV, vector3DU, matrix44D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate using {@code vector3DW}, {@code vector3DV} and {@code vector3DU}.
	 * <p>
	 * If either {@code vector3DW}, {@code vector3DV}, {@code vector3DU} or {@code matrix44DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DW.length < 3}, {@code vector3DV.length < 3}, {@code vector3DU.length < 3} or {@code matrix44DResult.length < 16}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DRotateWVU(vector3DW, vector3DV, vector3DU, matrix44DResult, 0, 0, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3DW a {@code double[]} that contains the W-vector of the rotation
	 * @param vector3DV a {@code double[]} that contains the V-vector of the rotation
	 * @param vector3DU a {@code double[]} that contains the U-vector of the rotation
	 * @param matrix44DResult a {@code double[]} that contains the matrix to return
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate using {@code vector3DW}, {@code vector3DV} and {@code vector3DU}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DW.length < 3}, {@code vector3DV.length < 3}, {@code vector3DU.length < 3} or {@code matrix44DResult.length < 16}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DW}, {@code vector3DV}, {@code vector3DU} or {@code matrix44DResult} are {@code null}
	 */
	public static double[] matrix44DRotateWVU(final double[] vector3DW, final double[] vector3DV, final double[] vector3DU, final double[] matrix44DResult) {
		return matrix44DRotateWVU(vector3DW, vector3DV, vector3DU, matrix44DResult, 0, 0, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate using {@code vector3DW}, {@code vector3DV} and {@code vector3DU}.
	 * <p>
	 * If either {@code vector3DW}, {@code vector3DV}, {@code vector3DU} or {@code matrix44DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DW.length < vector3DWOffset + 3}, {@code vector3DWOffset < 0}, {@code vector3DV.length < vector3DVOffset + 3}, {@code vector3DVOffset < 0}, {@code vector3DU.length < vector3DUOffset + 3}, {@code vector3DUOffset < 0},
	 * {@code matrix44DResult.length < matrix44DResultOffset + 16} or {@code matrix44DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3DW a {@code double[]} that contains the W-vector of the rotation
	 * @param vector3DV a {@code double[]} that contains the V-vector of the rotation
	 * @param vector3DU a {@code double[]} that contains the U-vector of the rotation
	 * @param matrix44DResult a {@code double[]} that contains the matrix to return
	 * @param vector3DWOffset the offset in {@code vector3DW} to start at
	 * @param vector3DVOffset the offset in {@code vector3DV} to start at
	 * @param vector3DUOffset the offset in {@code vector3DU} to start at
	 * @param matrix44DResultOffset the offset in {@code matrix44DResult} to start at
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate using {@code vector3DW}, {@code vector3DV} and {@code vector3DU}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DW.length < vector3DWOffset + 3}, {@code vector3DWOffset < 0}, {@code vector3DV.length < vector3DVOffset + 3}, {@code vector3DVOffset < 0},
	 *                                        {@code vector3DU.length < vector3DUOffset + 3}, {@code vector3DUOffset < 0}, {@code matrix44DResult.length < matrix44DResultOffset + 16} or {@code matrix44DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DW}, {@code vector3DV}, {@code vector3DU} or {@code matrix44DResult} are {@code null}
	 */
	public static double[] matrix44DRotateWVU(final double[] vector3DW, final double[] vector3DV, final double[] vector3DU, final double[] matrix44DResult, final int vector3DWOffset, final int vector3DVOffset, final int vector3DUOffset, final int matrix44DResultOffset) {
		final double element11 = vector3DU[vector3DUOffset + 0];
		final double element12 = vector3DV[vector3DVOffset + 0];
		final double element13 = vector3DW[vector3DWOffset + 0];
		final double element14 = 0.0D;
		
		final double element21 = vector3DU[vector3DUOffset + 1];
		final double element22 = vector3DV[vector3DVOffset + 1];
		final double element23 = vector3DW[vector3DWOffset + 1];
		final double element24 = 0.0D;
		
		final double element31 = vector3DU[vector3DUOffset + 2];
		final double element32 = vector3DV[vector3DVOffset + 2];
		final double element33 = vector3DW[vector3DWOffset + 2];
		final double element34 = 0.0D;
		
		final double element41 = 0.0D;
		final double element42 = 0.0D;
		final double element43 = 0.0D;
		final double element44 = 1.0D;
		
		return matrix44DSet(matrix44DResult, element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, matrix44DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate {@code angle} radians around the X-axis.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DRotateX(angle, Matrix.matrix44D());
	 * }
	 * </pre>
	 * 
	 * @param angle the angle to rotate, in radians
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate {@code angle} radians around the X-axis
	 */
	public static double[] matrix44DRotateX(final double angle) {
		return matrix44DRotateX(angle, matrix44D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate {@code angle} radians around the X-axis.
	 * <p>
	 * If {@code matrix44DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DResult.length < 16}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DRotateX(angle, matrix44DResult, 0);
	 * }
	 * </pre>
	 * 
	 * @param angle the angle to rotate, in radians
	 * @param matrix44DResult a {@code double[]} that contains the matrix to return
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate {@code angle} radians around the X-axis
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DResult.length < 16}
	 * @throws NullPointerException thrown if, and only if, {@code matrix44DResult} is {@code null}
	 */
	public static double[] matrix44DRotateX(final double angle, final double[] matrix44DResult) {
		return matrix44DRotateX(angle, matrix44DResult, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate {@code angle} radians around the X-axis.
	 * <p>
	 * If {@code matrix44DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DResult.length < matrix44DResultOffset + 16} or {@code matrix44DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param angle the angle to rotate, in radians
	 * @param matrix44DResult a {@code double[]} that contains the matrix to return
	 * @param matrix44DResultOffset the offset in {@code matrix44DResult} to start at
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate {@code angle} radians around the X-axis
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DResult.length < matrix44DResultOffset + 16} or {@code matrix44DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code matrix44DResult} is {@code null}
	 */
	public static double[] matrix44DRotateX(final double angle, final double[] matrix44DResult, final int matrix44DResultOffset) {
		final double cos = cos(angle);
		final double sin = sin(angle);
		
		final double element11 = 1.0D;
		final double element12 = 0.0D;
		final double element13 = 0.0D;
		final double element14 = 0.0D;
		
		final double element21 = 0.0D;
		final double element22 = +cos;
		final double element23 = -sin;
		final double element24 = 0.0D;
		
		final double element31 = 0.0D;
		final double element32 = +sin;
		final double element33 = +cos;
		final double element34 = 0.0D;
		
		final double element41 = 0.0D;
		final double element42 = 0.0D;
		final double element43 = 0.0D;
		final double element44 = 1.0D;
		
		return matrix44DSet(matrix44DResult, element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, matrix44DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate {@code angle} radians around the Y-axis.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DRotateY(angle, Matrix.matrix44D());
	 * }
	 * </pre>
	 * 
	 * @param angle the angle to rotate, in radians
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate {@code angle} radians around the Y-axis
	 */
	public static double[] matrix44DRotateY(final double angle) {
		return matrix44DRotateY(angle, matrix44D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate {@code angle} radians around the Y-axis.
	 * <p>
	 * If {@code matrix44DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DResult.length < 16}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DRotateY(angle, matrix44DResult, 0);
	 * }
	 * </pre>
	 * 
	 * @param angle the angle to rotate, in radians
	 * @param matrix44DResult a {@code double[]} that contains the matrix to return
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate {@code angle} radians around the Y-axis
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DResult.length < 16}
	 * @throws NullPointerException thrown if, and only if, {@code matrix44DResult} is {@code null}
	 */
	public static double[] matrix44DRotateY(final double angle, final double[] matrix44DResult) {
		return matrix44DRotateY(angle, matrix44DResult, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate {@code angle} radians around the Y-axis.
	 * <p>
	 * If {@code matrix44DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DResult.length < matrix44DResultOffset + 16} or {@code matrix44DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param angle the angle to rotate, in radians
	 * @param matrix44DResult a {@code double[]} that contains the matrix to return
	 * @param matrix44DResultOffset the offset in {@code matrix44DResult} to start at
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate {@code angle} radians around the Y-axis
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DResult.length < matrix44DResultOffset + 16} or {@code matrix44DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code matrix44DResult} is {@code null}
	 */
	public static double[] matrix44DRotateY(final double angle, final double[] matrix44DResult, final int matrix44DResultOffset) {
		final double cos = cos(angle);
		final double sin = sin(angle);
		
		final double element11 = +cos;
		final double element12 = 0.0D;
		final double element13 = +sin;
		final double element14 = 0.0D;
		
		final double element21 = 0.0D;
		final double element22 = 1.0D;
		final double element23 = 0.0D;
		final double element24 = 0.0D;
		
		final double element31 = -sin;
		final double element32 = 0.0D;
		final double element33 = +cos;
		final double element34 = 0.0D;
		
		final double element41 = 0.0D;
		final double element42 = 0.0D;
		final double element43 = 0.0D;
		final double element44 = 1.0D;
		
		return matrix44DSet(matrix44DResult, element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, matrix44DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate {@code angle} radians around the Z-axis.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DRotateZ(angle, Matrix.matrix44D());
	 * }
	 * </pre>
	 * 
	 * @param angle the angle to rotate, in radians
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate {@code angle} radians around the Z-axis
	 */
	public static double[] matrix44DRotateZ(final double angle) {
		return matrix44DRotateZ(angle, matrix44D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate {@code angle} radians around the Z-axis.
	 * <p>
	 * If {@code matrix44DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DResult.length < 16}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DRotateZ(angle, matrix44DResult, 0);
	 * }
	 * </pre>
	 * 
	 * @param angle the angle to rotate, in radians
	 * @param matrix44DResult a {@code double[]} that contains the matrix to return
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate {@code angle} radians around the Z-axis
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DResult.length < 16}
	 * @throws NullPointerException thrown if, and only if, {@code matrix44DResult} is {@code null}
	 */
	public static double[] matrix44DRotateZ(final double angle, final double[] matrix44DResult) {
		return matrix44DRotateZ(angle, matrix44DResult, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate {@code angle} radians around the Z-axis.
	 * <p>
	 * If {@code matrix44DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DResult.length < matrix44DResultOffset + 16} or {@code matrix44DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param angle the angle to rotate, in radians
	 * @param matrix44DResult a {@code double[]} that contains the matrix to return
	 * @param matrix44DResultOffset the offset in {@code matrix44DResult} to start at
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to rotate {@code angle} radians around the Z-axis
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DResult.length < matrix44DResultOffset + 16} or {@code matrix44DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code matrix44DResult} is {@code null}
	 */
	public static double[] matrix44DRotateZ(final double angle, final double[] matrix44DResult, final int matrix44DResultOffset) {
		final double cos = cos(angle);
		final double sin = sin(angle);
		
		final double element11 = +cos;
		final double element12 = -sin;
		final double element13 = 0.0D;
		final double element14 = 0.0D;
		
		final double element21 = +sin;
		final double element22 = +cos;
		final double element23 = 0.0D;
		final double element24 = 0.0D;
		
		final double element31 = 0.0D;
		final double element32 = 0.0D;
		final double element33 = 1.0D;
		final double element34 = 0.0D;
		
		final double element41 = 0.0D;
		final double element42 = 0.0D;
		final double element43 = 0.0D;
		final double element44 = 1.0D;
		
		return matrix44DSet(matrix44DResult, element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, matrix44DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to scale {@code vector3D}.
	 * <p>
	 * If {@code vector3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DScale(vector3D, Matrix.matrix44D());
	 * }
	 * </pre>
	 * 
	 * @param vector3D a {@code double[]} that contains the vector to scale
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to scale {@code vector3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code vector3D} is {@code null}
	 */
	public static double[] matrix44DScale(final double[] vector3D) {
		return matrix44DScale(vector3D, matrix44D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to scale {@code vector3D}.
	 * <p>
	 * If either {@code vector3D} or {@code matrix44DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < 3} or {@code matrix44DResult.length < 16}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DScale(vector3D, matrix44DResult, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3D a {@code double[]} that contains the vector to scale
	 * @param matrix44DResult a {@code double[]} that contains the matrix to return
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to scale {@code vector3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < 3} or {@code matrix44DResult.length < 16}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3D} or {@code matrix44DResult} are {@code null}
	 */
	public static double[] matrix44DScale(final double[] vector3D, final double[] matrix44DResult) {
		return matrix44DScale(vector3D, matrix44DResult, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to scale {@code vector3D}.
	 * <p>
	 * If either {@code vector3D} or {@code matrix44DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3D.length < vector3DOffset + 3}, {@code vector3DOffset < 0}, {@code matrix44DResult.length < matrix44DResultOffset + 16} or {@code matrix44DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3D a {@code double[]} that contains the vector to scale
	 * @param matrix44DResult a {@code double[]} that contains the matrix to return
	 * @param vector3DOffset the offset in {@code vector3D} to start at
	 * @param matrix44DResultOffset the offset in {@code matrix44DResult} to start at
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to scale {@code vector3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3D.length < vector3DOffset + 3}, {@code vector3DOffset < 0}, {@code matrix44DResult.length < matrix44DResultOffset + 16} or {@code matrix44DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3D} or {@code matrix44DResult} are {@code null}
	 */
	public static double[] matrix44DScale(final double[] vector3D, final double[] matrix44DResult, final int vector3DOffset, final int matrix44DResultOffset) {
		final double element11 = vector3D[vector3DOffset + 0];
		final double element12 = 0.0D;
		final double element13 = 0.0D;
		final double element14 = 0.0D;
		
		final double element21 = 0.0D;
		final double element22 = vector3D[vector3DOffset + 1];
		final double element23 = 0.0D;
		final double element24 = 0.0D;
		
		final double element31 = 0.0D;
		final double element32 = 0.0D;
		final double element33 = vector3D[vector3DOffset + 2];
		final double element34 = 0.0D;
		
		final double element41 = 0.0D;
		final double element42 = 0.0D;
		final double element43 = 0.0D;
		final double element44 = 1.0D;
		
		return matrix44DSet(matrix44DResult, element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, matrix44DResultOffset);
	}
	
	/**
	 * Sets the element values of the matrix contained in {@code matrix44DResult} at offset {@code 0}.
	 * <p>
	 * Returns {@code matrix44DResult}.
	 * <p>
	 * If {@code matrix44DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DResult.length < 16}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DSet(matrix44DResult, element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, 0);
	 * }
	 * </pre>
	 * 
	 * @param matrix44DResult a {@code double[]} that contains a matrix with four rows and four columns
	 * @param element11 the value of the element at row 1 and column 1 or index 0
	 * @param element12 the value of the element at row 1 and column 2 or index 1
	 * @param element13 the value of the element at row 1 and column 3 or index 2
	 * @param element14 the value of the element at row 1 and column 4 or index 3
	 * @param element21 the value of the element at row 2 and column 1 or index 4
	 * @param element22 the value of the element at row 2 and column 2 or index 5
	 * @param element23 the value of the element at row 2 and column 3 or index 6
	 * @param element24 the value of the element at row 2 and column 4 or index 7
	 * @param element31 the value of the element at row 3 and column 1 or index 8
	 * @param element32 the value of the element at row 3 and column 2 or index 9
	 * @param element33 the value of the element at row 3 and column 3 or index 10
	 * @param element34 the value of the element at row 3 and column 4 or index 11
	 * @param element41 the value of the element at row 4 and column 1 or index 12
	 * @param element42 the value of the element at row 4 and column 2 or index 13
	 * @param element43 the value of the element at row 4 and column 3 or index 14
	 * @param element44 the value of the element at row 4 and column 4 or index 15
	 * @return {@code matrix44DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DResult.length < 16}
	 * @throws NullPointerException thrown if, and only if, {@code matrix44DResult} is {@code null}
	 */
	public static double[] matrix44DSet(final double[] matrix44DResult, final double element11, final double element12, final double element13, final double element14, final double element21, final double element22, final double element23, final double element24, final double element31, final double element32, final double element33, final double element34, final double element41, final double element42, final double element43, final double element44) {
		return matrix44DSet(matrix44DResult, element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, 0);
	}
	
	/**
	 * Sets the element values of the matrix contained in {@code matrix44DResult} at offset {@code matrix44DResultOffset}.
	 * <p>
	 * Returns {@code matrix44DResult}.
	 * <p>
	 * If {@code matrix44DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44DResult.length < matrix44DResultOffset + 16} or {@code matrix44DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param matrix44DResult a {@code double[]} that contains a matrix with four rows and four columns
	 * @param element11 the value of the element at row 1 and column 1 or index 0
	 * @param element12 the value of the element at row 1 and column 2 or index 1
	 * @param element13 the value of the element at row 1 and column 3 or index 2
	 * @param element14 the value of the element at row 1 and column 4 or index 3
	 * @param element21 the value of the element at row 2 and column 1 or index 4
	 * @param element22 the value of the element at row 2 and column 2 or index 5
	 * @param element23 the value of the element at row 2 and column 3 or index 6
	 * @param element24 the value of the element at row 2 and column 4 or index 7
	 * @param element31 the value of the element at row 3 and column 1 or index 8
	 * @param element32 the value of the element at row 3 and column 2 or index 9
	 * @param element33 the value of the element at row 3 and column 3 or index 10
	 * @param element34 the value of the element at row 3 and column 4 or index 11
	 * @param element41 the value of the element at row 4 and column 1 or index 12
	 * @param element42 the value of the element at row 4 and column 2 or index 13
	 * @param element43 the value of the element at row 4 and column 3 or index 14
	 * @param element44 the value of the element at row 4 and column 4 or index 15
	 * @param matrix44DResultOffset the offset in {@code matrix44DResult} to start at
	 * @return {@code matrix44DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44DResult.length < matrix44DResultOffset + 16} or {@code matrix44DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code matrix44DResult} is {@code null}
	 */
	public static double[] matrix44DSet(final double[] matrix44DResult, final double element11, final double element12, final double element13, final double element14, final double element21, final double element22, final double element23, final double element24, final double element31, final double element32, final double element33, final double element34, final double element41, final double element42, final double element43, final double element44, final int matrix44DResultOffset) {
		matrix44DResult[matrix44DResultOffset +  0] = element11;
		matrix44DResult[matrix44DResultOffset +  1] = element12;
		matrix44DResult[matrix44DResultOffset +  2] = element13;
		matrix44DResult[matrix44DResultOffset +  3] = element14;
		
		matrix44DResult[matrix44DResultOffset +  4] = element21;
		matrix44DResult[matrix44DResultOffset +  5] = element22;
		matrix44DResult[matrix44DResultOffset +  6] = element23;
		matrix44DResult[matrix44DResultOffset +  7] = element24;
		
		matrix44DResult[matrix44DResultOffset +  8] = element31;
		matrix44DResult[matrix44DResultOffset +  9] = element32;
		matrix44DResult[matrix44DResultOffset + 10] = element33;
		matrix44DResult[matrix44DResultOffset + 11] = element34;
		
		matrix44DResult[matrix44DResultOffset + 12] = element41;
		matrix44DResult[matrix44DResultOffset + 13] = element42;
		matrix44DResult[matrix44DResultOffset + 14] = element43;
		matrix44DResult[matrix44DResultOffset + 15] = element44;
		
		return matrix44DResult;
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to translate {@code point3D}.
	 * <p>
	 * If {@code point3D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point3D.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DTranslate(point3D, Matrix.matrix44D());
	 * }
	 * </pre>
	 * 
	 * @param point3D a {@code double[]} that contains the point to translate
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to translate {@code point3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point3D.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code point3D} is {@code null}
	 */
	public static double[] matrix44DTranslate(final double[] point3D) {
		return matrix44DTranslate(point3D, matrix44D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to translate {@code point3D}.
	 * <p>
	 * If either {@code point3D} or {@code matrix44DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point3D.length < 3} or {@code matrix44DResult.length < 16}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DTranslate(point3D, matrix44DResult, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param point3D a {@code double[]} that contains the point to translate
	 * @param matrix44DResult a {@code double[]} that contains the matrix to return
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to translate {@code point3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point3D.length < 3} or {@code matrix44DResult.length < 16}
	 * @throws NullPointerException thrown if, and only if, either {@code point3D} or {@code matrix44DResult} are {@code null}
	 */
	public static double[] matrix44DTranslate(final double[] point3D, final double[] matrix44DResult) {
		return matrix44DTranslate(point3D, matrix44DResult, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to translate {@code point3D}.
	 * <p>
	 * If either {@code point3D} or {@code matrix44DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point3D.length < point3DOffset + 3}, {@code point3DOffset < 0}, {@code matrix44DResult.length < matrix44DResultOffset + 16} or {@code matrix44DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param point3D a {@code double[]} that contains the point to translate
	 * @param matrix44DResult a {@code double[]} that contains the matrix to return
	 * @param point3DOffset the offset in {@code point3D} to start at
	 * @param matrix44DResultOffset the offset in {@code matrix44DResult} to start at
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to translate {@code point3D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code point3D.length < point3DOffset + 3}, {@code point3DOffset < 0}, {@code matrix44DResult.length < matrix44DResultOffset + 16} or {@code matrix44DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code point3D} or {@code matrix44DResult} are {@code null}
	 */
	public static double[] matrix44DTranslate(final double[] point3D, final double[] matrix44DResult, final int point3DOffset, final int matrix44DResultOffset) {
		final double element11 = 1.0D;
		final double element12 = 0.0D;
		final double element13 = 0.0D;
		final double element14 = point3D[point3DOffset + 0];
		
		final double element21 = 0.0D;
		final double element22 = 1.0D;
		final double element23 = 0.0D;
		final double element24 = point3D[point3DOffset + 1];
		
		final double element31 = 0.0D;
		final double element32 = 0.0D;
		final double element33 = 1.0D;
		final double element34 = point3D[point3DOffset + 2];
		
		final double element41 = 0.0D;
		final double element42 = 0.0D;
		final double element43 = 0.0D;
		final double element44 = 1.0D;
		
		return matrix44DSet(matrix44DResult, element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, matrix44DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to the transpose of {@code matrix44D}.
	 * <p>
	 * If {@code matrix44D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44D.length < 16}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DTranspose(matrix44D, Matrix.matrix44D());
	 * }
	 * </pre>
	 * 
	 * @param matrix44D a {@code double[]} that contains the matrix to transpose
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to the transpose of {@code matrix44D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44D.length < 16}
	 * @throws NullPointerException thrown if, and only if, {@code matrix44D} is {@code null}
	 */
	public static double[] matrix44DTranspose(final double[] matrix44D) {
		return matrix44DTranspose(matrix44D, matrix44D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to the transpose of {@code matrix44D}.
	 * <p>
	 * If either {@code matrix44D} or {@code matrix44DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44D.length < 16} or {@code matrix44DResult.length < 16}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix.matrix44DTranspose(matrix44D, matrix44DResult, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param matrix44D a {@code double[]} that contains the matrix to transpose
	 * @param matrix44DResult a {@code double[]} that contains the matrix to return
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to the transpose of {@code matrix44D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44D.length < 16} or {@code matrix44DResult.length < 16}
	 * @throws NullPointerException thrown if, and only if, either {@code matrix44D} or {@code matrix44DResult} are {@code null}
	 */
	public static double[] matrix44DTranspose(final double[] matrix44D, final double[] matrix44DResult) {
		return matrix44DTranspose(matrix44D, matrix44DResult, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a matrix with four rows and four columns and is set to the transpose of {@code matrix44D}.
	 * <p>
	 * If either {@code matrix44D} or {@code matrix44DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix44D.length < matrix44DOffset + 16}, {@code matrix44DOffset < 0}, {@code matrix44DResult.length < matrix44DResultOffset + 16} or {@code matrix44DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param matrix44D a {@code double[]} that contains the matrix to transpose
	 * @param matrix44DResult a {@code double[]} that contains the matrix to return
	 * @param matrix44DOffset the offset in {@code matrix44D} to start at
	 * @param matrix44DResultOffset the offset in {@code matrix44DResult} to start at
	 * @return a {@code double[]} that contains a matrix with four rows and four columns and is set to the transpose of {@code matrix44D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code matrix44D.length < matrix44DOffset + 16}, {@code matrix44DOffset < 0}, {@code matrix44DResult.length < matrix44DResultOffset + 16} or {@code matrix44DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code matrix44D} or {@code matrix44DResult} are {@code null}
	 */
	public static double[] matrix44DTranspose(final double[] matrix44D, final double[] matrix44DResult, final int matrix44DOffset, final int matrix44DResultOffset) {
		final double element11 = matrix44D[matrix44DOffset +  0];
		final double element12 = matrix44D[matrix44DOffset +  4];
		final double element13 = matrix44D[matrix44DOffset +  8];
		final double element14 = matrix44D[matrix44DOffset + 12];
		
		final double element21 = matrix44D[matrix44DOffset +  1];
		final double element22 = matrix44D[matrix44DOffset +  5];
		final double element23 = matrix44D[matrix44DOffset +  9];
		final double element24 = matrix44D[matrix44DOffset + 13];
		
		final double element31 = matrix44D[matrix44DOffset +  2];
		final double element32 = matrix44D[matrix44DOffset +  6];
		final double element33 = matrix44D[matrix44DOffset + 10];
		final double element34 = matrix44D[matrix44DOffset + 14];
		
		final double element41 = matrix44D[matrix44DOffset +  3];
		final double element42 = matrix44D[matrix44DOffset +  7];
		final double element43 = matrix44D[matrix44DOffset + 11];
		final double element44 = matrix44D[matrix44DOffset + 15];
		
		return matrix44DSet(matrix44DResult, element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, matrix44DResultOffset);
	}
}