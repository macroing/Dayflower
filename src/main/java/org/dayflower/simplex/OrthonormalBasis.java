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

import static org.dayflower.simplex.Vector.vector3D;
import static org.dayflower.simplex.Vector.vector3DCrossProduct;
import static org.dayflower.simplex.Vector.vector3DNormalize;
import static org.dayflower.simplex.Vector.vector3DSet;

import java.lang.reflect.Field;//TODO: Add Javadocs!

/**
 * A class that consists exclusively of static methods that returns or performs various operations on orthonormal bases.
 * <p>
 * This class currently supports the following:
 * <ul>
 * <li>{@code OrthonormalBasis33D} - an orthonormal basis that is constructed by three 3-dimensional vectors and is represented by a {@code double[]}.</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class OrthonormalBasis {
	/**
	 * The relative offset for the vector denoted by U in a {@code double[]} that contains an orthonormal basis.
	 */
	public static final int ORTHONORMAL_BASIS_OFFSET_U = 0;
	
	/**
	 * The relative offset for the vector denoted by V in a {@code double[]} that contains an orthonormal basis.
	 */
	public static final int ORTHONORMAL_BASIS_OFFSET_V = 3;
	
	/**
	 * The relative offset for the vector denoted by W in a {@code double[]} that contains an orthonormal basis.
	 */
	public static final int ORTHONORMAL_BASIS_OFFSET_W = 6;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private OrthonormalBasis() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// OrthonormalBasis33D /////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code double[]} that contains an orthonormal basis that consists of three vectors with three components each.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * OrthonormalBasis.orthonormalBasis33D(Vector.vector3D(1.0D, 0.0D, 0.0D), Vector.vector3D(0.0D, 1.0D, 0.0D), Vector.vector3D(0.0D, 0.0D, 1.0D));
	 * }
	 * </pre>
	 * 
	 * @return a {@code double[]} that contains an orthonormal basis that consists of three vectors with three components each
	 */
	public static double[] orthonormalBasis33D() {
		return orthonormalBasis33D(vector3D(1.0D, 0.0D, 0.0D), vector3D(0.0D, 1.0D, 0.0D), vector3D(0.0D, 0.0D, 1.0D));
	}
	
	/**
	 * Returns a {@code double[]} that contains an orthonormal basis that consists of three vectors with three components each.
	 * <p>
	 * If either {@code vector3DU}, {@code vector3DV} or {@code vector3DW} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DU.length < 3}, {@code vector3DV.length < 3} or {@code vector3DW.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * OrthonormalBasis.orthonormalBasis33D(vector3DU, vector3DV, vector3DW, 0, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param vector3DU a {@code double[]} that contains the vector denoted by U
	 * @param vector3DV a {@code double[]} that contains the vector denoted by V
	 * @param vector3DW a {@code double[]} that contains the vector denoted by W
	 * @return a {@code double[]} that contains an orthonormal basis that consists of three vectors with three components each
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DU.length < 3}, {@code vector3DV.length < 3} or {@code vector3DW.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DU}, {@code vector3DV} or {@code vector3DW} are {@code null}
	 */
	public static double[] orthonormalBasis33D(final double[] vector3DU, final double[] vector3DV, final double[] vector3DW) {
		return orthonormalBasis33D(vector3DU, vector3DV, vector3DW, 0, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains an orthonormal basis that consists of three vectors with three components each.
	 * <p>
	 * If either {@code vector3DU}, {@code vector3DV} or {@code vector3DW} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code vector3DU.length < vector3DUOffset + 3}, {@code vector3DUOffset < 0}, {@code vector3DV.length < vector3DVOffset + 3}, {@code vector3DVOffset < 0}, {@code vector3DW.length < vector3DWOffset} or {@code vector3DWOffset < 0}, an
	 * {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param vector3DU a {@code double[]} that contains the vector denoted by U
	 * @param vector3DV a {@code double[]} that contains the vector denoted by V
	 * @param vector3DW a {@code double[]} that contains the vector denoted by W
	 * @param vector3DUOffset the offset in {@code vector3DU} to start at
	 * @param vector3DVOffset the offset in {@code vector3DV} to start at
	 * @param vector3DWOffset the offset in {@code vector3DW} to start at
	 * @return a {@code double[]} that contains an orthonormal basis that consists of three vectors with three components each
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code vector3DU.length < vector3DUOffset + 3}, {@code vector3DUOffset < 0}, {@code vector3DV.length < vector3DVOffset + 3}, {@code vector3DVOffset < 0},
	 *                                        {@code vector3DW.length < vector3DWOffset} or {@code vector3DWOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code vector3DU}, {@code vector3DV} or {@code vector3DW} are {@code null}
	 */
	public static double[] orthonormalBasis33D(final double[] vector3DU, final double[] vector3DV, final double[] vector3DW, final int vector3DUOffset, final int vector3DVOffset, final int vector3DWOffset) {
		final double uX = vector3DU[vector3DUOffset + 0];
		final double uY = vector3DU[vector3DUOffset + 1];
		final double uZ = vector3DU[vector3DUOffset + 2];
		
		final double vX = vector3DV[vector3DVOffset + 0];
		final double vY = vector3DV[vector3DVOffset + 1];
		final double vZ = vector3DV[vector3DVOffset + 2];
		
		final double wX = vector3DW[vector3DWOffset + 0];
		final double wY = vector3DW[vector3DWOffset + 1];
		final double wZ = vector3DW[vector3DWOffset + 2];
		
		return new double[] {uX, uY, uZ, vX, vY, vZ, wX, wY, wZ};
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromWV(final double[] vector3DW, final double[] vector3DV) {
		return orthonormalBasis33DFromWV(vector3DW, vector3DV, orthonormalBasis33D());
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromWV(final double[] vector3DW, final double[] vector3DV, final double[] orthonormalBasis33DResult) {
		return orthonormalBasis33DFromWV(vector3DW, vector3DV, orthonormalBasis33DResult, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DFromWV(final double[] vector3DW, final double[] vector3DV, final double[] orthonormalBasis33DResult, final int vector3DWOffset, final int vector3DVOffset, final int orthonormalBasis33DResultOffset) {
		final double[] vector3DWNormalized = vector3DNormalize(vector3DW, vector3D(), vector3DWOffset, 0);
		final double[] vector3DUNormalized = vector3DCrossProduct(vector3DNormalize(vector3DV, vector3D(), vector3DVOffset, 0), vector3DWNormalized);
		final double[] vector3DVNormalized = vector3DCrossProduct(vector3DWNormalized, vector3DUNormalized);
		
		return orthonormalBasis33DSet(orthonormalBasis33DResult, vector3DUNormalized, vector3DVNormalized, vector3DWNormalized, orthonormalBasis33DResultOffset, 0, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains the vector denoted by U and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}.
	 * <p>
	 * If {@code orthonormalBasis33D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code orthonormalBasis33D.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * OrthonormalBasis.orthonormalBasis33DGetU(orthonormalBasis33D, Vector.vector3D());
	 * }
	 * </pre>
	 * 
	 * @param orthonormalBasis33D a {@code double[]} that contains an orthonormal basis
	 * @return a {@code double[]} that contains the vector denoted by U and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code orthonormalBasis33D.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code orthonormalBasis33D} is {@code null}
	 */
	public static double[] orthonormalBasis33DGetU(final double[] orthonormalBasis33D) {
		return orthonormalBasis33DGetU(orthonormalBasis33D, vector3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains the vector denoted by U and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}.
	 * <p>
	 * If either {@code orthonormalBasis33D} or {@code vector3DUResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code orthonormalBasis33D.length < 3} or {@code vector3DUResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * OrthonormalBasis.orthonormalBasis33DGetU(orthonormalBasis33D, vector3DUResult, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param orthonormalBasis33D a {@code double[]} that contains an orthonormal basis
	 * @param vector3DUResult a {@code double[]} that contains the vector to return
	 * @return a {@code double[]} that contains the vector denoted by U and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code orthonormalBasis33D.length < 3} or {@code vector3DUResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code orthonormalBasis33D} or {@code vector3DUResult} are {@code null}
	 */
	public static double[] orthonormalBasis33DGetU(final double[] orthonormalBasis33D, final double[] vector3DUResult) {
		return orthonormalBasis33DGetU(orthonormalBasis33D, vector3DUResult, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains the vector denoted by U and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}.
	 * <p>
	 * If either {@code orthonormalBasis33D} or {@code vector3DUResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code orthonormalBasis33D.length < orthonormalBasis33DOffset + 3}, {@code orthonormalBasis33DOffset < 0}, {@code vector3DUResult.length < vector3DUResultOffset + 3} or {@code vector3DUResultOffset < 0}, an
	 * {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param orthonormalBasis33D a {@code double[]} that contains an orthonormal basis
	 * @param vector3DUResult a {@code double[]} that contains the vector to return
	 * @param orthonormalBasis33DOffset the offset in {@code orthonormalBasis33D} to start at
	 * @param vector3DUResultOffset the offset in {@code vector3DUResult} to start at
	 * @return a {@code double[]} that contains the vector denoted by U and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code orthonormalBasis33D.length < orthonormalBasis33DOffset + 3}, {@code orthonormalBasis33DOffset < 0}, {@code vector3DUResult.length < vector3DUResultOffset + 3} or
	 *                                        {@code vector3DUResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code orthonormalBasis33D} or {@code vector3DUResult} are {@code null}
	 */
	public static double[] orthonormalBasis33DGetU(final double[] orthonormalBasis33D, final double[] vector3DUResult, final int orthonormalBasis33DOffset, final int vector3DUResultOffset) {
		final double component1 = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_U + 0];
		final double component2 = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_U + 1];
		final double component3 = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_U + 2];
		
		return vector3DSet(vector3DUResult, component1, component2, component3, vector3DUResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains the vector denoted by V and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}.
	 * <p>
	 * If {@code orthonormalBasis33D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code orthonormalBasis33D.length < 6}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * OrthonormalBasis.orthonormalBasis33DGetV(orthonormalBasis33D, Vector.vector3D());
	 * }
	 * </pre>
	 * 
	 * @param orthonormalBasis33D a {@code double[]} that contains an orthonormal basis
	 * @return a {@code double[]} that contains the vector denoted by V and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code orthonormalBasis33D.length < 6}
	 * @throws NullPointerException thrown if, and only if, {@code orthonormalBasis33D} is {@code null}
	 */
	public static double[] orthonormalBasis33DGetV(final double[] orthonormalBasis33D) {
		return orthonormalBasis33DGetV(orthonormalBasis33D, vector3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains the vector denoted by V and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}.
	 * <p>
	 * If either {@code orthonormalBasis33D} or {@code vector3DVResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code orthonormalBasis33D.length < 6} or {@code vector3DVResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * OrthonormalBasis.orthonormalBasis33DGetV(orthonormalBasis33D, vector3DVResult, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param orthonormalBasis33D a {@code double[]} that contains an orthonormal basis
	 * @param vector3DVResult a {@code double[]} that contains the vector to return
	 * @return a {@code double[]} that contains the vector denoted by V and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code orthonormalBasis33D.length < 6} or {@code vector3DVResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code orthonormalBasis33D} or {@code vector3DVResult} are {@code null}
	 */
	public static double[] orthonormalBasis33DGetV(final double[] orthonormalBasis33D, final double[] vector3DVResult) {
		return orthonormalBasis33DGetV(orthonormalBasis33D, vector3DVResult, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains the vector denoted by V and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}.
	 * <p>
	 * If either {@code orthonormalBasis33D} or {@code vector3DVResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code orthonormalBasis33D.length < orthonormalBasis33DOffset + 6}, {@code orthonormalBasis33DOffset < 0}, {@code vector3DVResult.length < vector3DVResultOffset + 3} or {@code vector3DVResultOffset < 0}, an
	 * {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param orthonormalBasis33D a {@code double[]} that contains an orthonormal basis
	 * @param vector3DVResult a {@code double[]} that contains the vector to return
	 * @param orthonormalBasis33DOffset the offset in {@code orthonormalBasis33D} to start at
	 * @param vector3DVResultOffset the offset in {@code vector3DVResult} to start at
	 * @return a {@code double[]} that contains the vector denoted by V and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code orthonormalBasis33D.length < orthonormalBasis33DOffset + 6}, {@code orthonormalBasis33DOffset < 0}, {@code vector3DVResult.length < vector3DVResultOffset + 3} or
	 *                                        {@code vector3DVResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code orthonormalBasis33D} or {@code vector3DVResult} are {@code null}
	 */
	public static double[] orthonormalBasis33DGetV(final double[] orthonormalBasis33D, final double[] vector3DVResult, final int orthonormalBasis33DOffset, final int vector3DVResultOffset) {
		final double component1 = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_V + 0];
		final double component2 = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_V + 1];
		final double component3 = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_V + 2];
		
		return vector3DSet(vector3DVResult, component1, component2, component3, vector3DVResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains the vector denoted by W and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}.
	 * <p>
	 * If {@code orthonormalBasis33D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code orthonormalBasis33D.length < 9}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * OrthonormalBasis.orthonormalBasis33DGetW(orthonormalBasis33D, Vector.vector3D());
	 * }
	 * </pre>
	 * 
	 * @param orthonormalBasis33D a {@code double[]} that contains an orthonormal basis
	 * @return a {@code double[]} that contains the vector denoted by W and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code orthonormalBasis33D.length < 9}
	 * @throws NullPointerException thrown if, and only if, {@code orthonormalBasis33D} is {@code null}
	 */
	public static double[] orthonormalBasis33DGetW(final double[] orthonormalBasis33D) {
		return orthonormalBasis33DGetW(orthonormalBasis33D, vector3D());
	}
	
	/**
	 * Returns a {@code double[]} that contains the vector denoted by W and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}.
	 * <p>
	 * If either {@code orthonormalBasis33D} or {@code vector3DWResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code orthonormalBasis33D.length < 9} or {@code vector3DWResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * OrthonormalBasis.orthonormalBasis33DGetW(orthonormalBasis33D, vector3DWResult, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param orthonormalBasis33D a {@code double[]} that contains an orthonormal basis
	 * @param vector3DWResult a {@code double[]} that contains the vector to return
	 * @return a {@code double[]} that contains the vector denoted by W and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code orthonormalBasis33D.length < 9} or {@code vector3DWResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, either {@code orthonormalBasis33D} or {@code vector3DWResult} are {@code null}
	 */
	public static double[] orthonormalBasis33DGetW(final double[] orthonormalBasis33D, final double[] vector3DWResult) {
		return orthonormalBasis33DGetW(orthonormalBasis33D, vector3DWResult, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains the vector denoted by W and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}.
	 * <p>
	 * If either {@code orthonormalBasis33D} or {@code vector3DWResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code orthonormalBasis33D.length < orthonormalBasis33DOffset + 9}, {@code orthonormalBasis33DOffset < 0}, {@code vector3DWResult.length < vector3DWResultOffset + 3} or {@code vector3DWResultOffset < 0}, an
	 * {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param orthonormalBasis33D a {@code double[]} that contains an orthonormal basis
	 * @param vector3DWResult a {@code double[]} that contains the vector to return
	 * @param orthonormalBasis33DOffset the offset in {@code orthonormalBasis33D} to start at
	 * @param vector3DWResultOffset the offset in {@code vector3DWResult} to start at
	 * @return a {@code double[]} that contains the vector denoted by W and is associated with the orthonormal basis contained in {@code orthonormalBasis33D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code orthonormalBasis33D.length < orthonormalBasis33DOffset + 9}, {@code orthonormalBasis33DOffset < 0}, {@code vector3DWResult.length < vector3DWResultOffset + 3} or
	 *                                        {@code vector3DWResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code orthonormalBasis33D} or {@code vector3DWResult} are {@code null}
	 */
	public static double[] orthonormalBasis33DGetW(final double[] orthonormalBasis33D, final double[] vector3DWResult, final int orthonormalBasis33DOffset, final int vector3DWResultOffset) {
		final double component1 = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_W + 0];
		final double component2 = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_W + 1];
		final double component3 = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_W + 2];
		
		return vector3DSet(vector3DWResult, component1, component2, component3, vector3DWResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DSet(final double[] orthonormalBasis33DResult, final double[] vector3DU, final double[] vector3DV, final double[] vector3DW) {
		return orthonormalBasis33DSet(orthonormalBasis33DResult, vector3DU, vector3DV, vector3DW, 0, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DSet(final double[] orthonormalBasis33DResult, final double[] vector3DU, final double[] vector3DV, final double[] vector3DW, final int orthonormalBasis33DResultOffset, final int vector3DUOffset, final int vector3DVOffset, final int vector3DWOffset) {
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_U + 0] = vector3DU[vector3DUOffset + 0];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_U + 1] = vector3DU[vector3DUOffset + 1];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_U + 2] = vector3DU[vector3DUOffset + 2];
		
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_V + 0] = vector3DV[vector3DVOffset + 0];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_V + 1] = vector3DV[vector3DVOffset + 1];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_V + 2] = vector3DV[vector3DVOffset + 2];
		
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_W + 0] = vector3DW[vector3DWOffset + 0];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_W + 1] = vector3DW[vector3DWOffset + 1];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_W + 2] = vector3DW[vector3DWOffset + 2];
		
		return orthonormalBasis33DResult;
	}
	
//	TODO: Add Javadocs!
	public static double[] orthonormalBasis33DSet(final double[] orthonormalBasis33DResult, final double[] orthonormalBasis33D, final int orthonormalBasis33DResultOffset, final int orthonormalBasis33DOffset) {
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_U + 0] = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_U + 0];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_U + 1] = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_U + 1];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_U + 2] = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_U + 2];
		
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_V + 0] = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_V + 0];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_V + 1] = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_V + 1];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_V + 2] = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_V + 2];
		
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_W + 0] = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_W + 0];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_W + 1] = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_W + 1];
		orthonormalBasis33DResult[orthonormalBasis33DResultOffset + ORTHONORMAL_BASIS_OFFSET_W + 2] = orthonormalBasis33D[orthonormalBasis33DOffset + ORTHONORMAL_BASIS_OFFSET_W + 2];
		
		return orthonormalBasis33DResult;
	}
}