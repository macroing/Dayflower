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
package org.dayflower.renderer.gpu;

import org.macroing.java.lang.Floats;

/**
 * An {@code AbstractGeometryKernel} is an abstract extension of the {@code AbstractImageKernel} class that adds additional features.
 * <p>
 * The features added are the following:
 * <ul>
 * <li>Intersection methods</li>
 * <li>Orthonormal basis methods</li>
 * <li>Point methods</li>
 * <li>Ray methods</li>
 * <li>Vector methods</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractGeometryKernel extends AbstractImageKernel {
	/**
	 * The default maximum parametric distance value.
	 */
	protected static final float DEFAULT_T_MAXIMUM = Floats.MAX_VALUE;
	
	/**
	 * The default minimum parametric distance value.
	 */
	protected static final float DEFAULT_T_MINIMUM = 0.001F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U = 0;
	private static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V = 3;
	private static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W = 6;
	private static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U = 9;
	private static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V = 12;
	private static final int INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W = 15;
	private static final int INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX = 18;
	private static final int INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT = 19;
	private static final int INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES = 22;
	private static final int INTERSECTION_ARRAY_SIZE = 24;
	private static final int ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U = 0;
	private static final int ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V = 3;
	private static final int ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W = 6;
	private static final int ORTHONORMAL_BASIS_3_3_F_ARRAY_SIZE = 9;
	private static final int POINT_2_F_ARRAY_OFFSET_X = 0;
	private static final int POINT_2_F_ARRAY_OFFSET_Y = 1;
	private static final int POINT_2_F_ARRAY_SIZE = 2;
	private static final int POINT_3_F_ARRAY_OFFSET_X = 0;
	private static final int POINT_3_F_ARRAY_OFFSET_Y = 1;
	private static final int POINT_3_F_ARRAY_OFFSET_Z = 2;
	private static final int POINT_3_F_ARRAY_SIZE = 3;
	private static final int RAY_3_F_ARRAY_OFFSET_DIRECTION = 3;
	private static final int RAY_3_F_ARRAY_OFFSET_ORIGIN = 0;
	private static final int RAY_3_F_ARRAY_OFFSET_T_MAXIMUM = 7;
	private static final int RAY_3_F_ARRAY_OFFSET_T_MINIMUM = 6;
	private static final int RAY_3_F_ARRAY_SIZE = 8;
	private static final int VECTOR_3_F_ARRAY_OFFSET_X = 0;
	private static final int VECTOR_3_F_ARRAY_OFFSET_Y = 1;
	private static final int VECTOR_3_F_ARRAY_OFFSET_Z = 2;
	private static final int VECTOR_3_F_ARRAY_SIZE = 3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * A {@code float[]} that contains the current intersection.
	 */
	protected float[] intersectionLHSArray_$private$24;
	
	/**
	 * A {@code float[]} that contains the current intersection.
	 */
	protected float[] intersectionRHSArray_$private$24;
	
	/**
	 * A {@code float[]} that contains an orthonormal basis that consists of three 3-dimensional vectors.
	 */
	protected float[] orthonormalBasis33FArray_$private$9;
	
	/**
	 * A {@code float[]} that contains a point that consists of two components.
	 */
	protected float[] point2FArray_$private$2;
	
	/**
	 * A {@code float[]} that contains a point that consists of three components.
	 */
	protected float[] point3FArray_$private$3;
	
	/**
	 * A {@code float[]} that contains a ray that consists of a point called origin, a vector called direction, the minimum parametric distance value and the maximum parametric distance value.
	 */
	protected float[] ray3FArray_$private$8;
	
	/**
	 * A {@code float[]} that contains a vector that consists of three components.
	 */
	protected float[] vector3FArray_$private$3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractGeometryKernel} instance.
	 */
	protected AbstractGeometryKernel() {
		this.intersectionLHSArray_$private$24 = new float[INTERSECTION_ARRAY_SIZE];
		this.intersectionRHSArray_$private$24 = new float[INTERSECTION_ARRAY_SIZE];
		this.orthonormalBasis33FArray_$private$9 = new float[ORTHONORMAL_BASIS_3_3_F_ARRAY_SIZE];
		this.point2FArray_$private$2 = new float[POINT_2_F_ARRAY_SIZE];
		this.point3FArray_$private$3 = new float[POINT_3_F_ARRAY_SIZE];
		this.ray3FArray_$private$8 = new float[RAY_3_F_ARRAY_SIZE];
		this.vector3FArray_$private$3 = new float[VECTOR_3_F_ARRAY_SIZE];
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Intersection - LHS //////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the value of the X-component of the U-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the X-component of the U-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGUX() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0];
	}
	
	/**
	 * Returns the value of the Y-component of the U-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the Y-component of the U-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGUY() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1];
	}
	
	/**
	 * Returns the value of the Z-component of the U-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the Z-component of the U-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGUZ() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2];
	}
	
	/**
	 * Returns the value of X-component of the V-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of X-component of the V-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGVX() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0];
	}
	
	/**
	 * Returns the value of the Y-component of the V-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the Y-component of the V-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGVY() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1];
	}
	
	/**
	 * Returns the value of the Z-component of the V-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the Z-component of the V-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGVZ() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2];
	}
	
	/**
	 * Returns the value of the X-component of the W-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the X-component of the W-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGWX() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
	}
	
	/**
	 * Returns the value of the Y-component of the W-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the Y-component of the W-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGWY() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
	}
	
	/**
	 * Returns the value of the Z-component of the W-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the Z-component of the W-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGWZ() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
	}
	
	/**
	 * Returns the value of the X-component of the U-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the X-component of the U-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSUX() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0];
	}
	
	/**
	 * Returns the value of the Y-component of the U-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the Y-component of the U-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSUY() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1];
	}
	
	/**
	 * Returns the value of the Z-component of the U-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the Z-component of the U-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSUZ() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2];
	}
	
	/**
	 * Returns the value of the X-component of the V-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the X-component of the V-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSVX() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0];
	}
	
	/**
	 * Returns the value of the Y-component of the V-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the Y-component of the V-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSVY() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1];
	}
	
	/**
	 * Returns the value of the Z-component of the V-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the Z-component of the V-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSVZ() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2];
	}
	
	/**
	 * Returns the value of the X-component of the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the X-component of the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSWX() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
	}
	
	/**
	 * Returns the value of the Y-component of the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the Y-component of the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSWY() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
	}
	
	/**
	 * Returns the value of the Z-component of the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the Z-component of the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSWZ() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
	}
	
	/**
	 * Returns the value of the X-component of the surface intersection point in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the X-component of the surface intersection point in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetSurfaceIntersectionPointX() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0];
	}
	
	/**
	 * Returns the value of the Y-component of the surface intersection point in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the Y-component of the surface intersection point in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetSurfaceIntersectionPointY() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1];
	}
	
	/**
	 * Returns the value of the Z-component of the surface intersection point in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the Z-component of the surface intersection point in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetSurfaceIntersectionPointZ() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2];
	}
	
	/**
	 * Returns the value of the X-component of the texture coordinates in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the X-component of the texture coordinates in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetTextureCoordinatesX() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0];
	}
	
	/**
	 * Returns the value of the Y-component of the texture coordinates in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of the Y-component of the texture coordinates in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetTextureCoordinatesY() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1];
	}
	
	/**
	 * Returns the primitive index in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the primitive index in {@link #intersectionLHSArray_$private$24}
	 */
	protected final int intersectionLHSGetPrimitiveIndex() {
		return (int)(this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX]);
	}
	
	/**
	 * Moves the intersection data from left-hand side to right-hand side.
	 */
	protected final void intersectionLHSMoveToRHS() {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0];
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1];
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2];
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0];
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1];
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2];
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
		
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0];
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1];
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2];
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0];
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1];
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2];
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
		
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX];
		
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0];
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1];
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2];
		
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0];
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1] = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1];
	}
	
	/**
	 * Sets the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @param uX the X-component of the U-direction
	 * @param uY the Y-component of the U-direction
	 * @param uZ the Z-component of the U-direction
	 * @param vX the X-component of the V-direction
	 * @param vY the Y-component of the V-direction
	 * @param vZ the Z-component of the V-direction
	 * @param wX the X-component of the W-direction
	 * @param wY the Y-component of the W-direction
	 * @param wZ the Z-component of the W-direction
	 */
	protected final void intersectionLHSSetOrthonormalBasisG(final float uX, final float uY, final float uZ, final float vX, final float vY, final float vZ, final float wX, final float wY, final float wZ) {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0] = uX;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1] = uY;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2] = uZ;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0] = vX;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1] = vY;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2] = vZ;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0] = wX;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1] = wY;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2] = wZ;
	}
	
	/**
	 * Sets the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24} from {@link #orthonormalBasis33FArray_$private$9}.
	 */
	protected final void intersectionLHSSetOrthonormalBasisGFromOrthonormalBasis33F() {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0] = orthonormalBasis33FGetUX();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1] = orthonormalBasis33FGetUY();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2] = orthonormalBasis33FGetUZ();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0] = orthonormalBasis33FGetVX();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1] = orthonormalBasis33FGetVY();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2] = orthonormalBasis33FGetVZ();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0] = orthonormalBasis33FGetWX();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1] = orthonormalBasis33FGetWY();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2] = orthonormalBasis33FGetWZ();
	}
	
	/**
	 * Sets the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @param uX the X-component of the U-direction
	 * @param uY the Y-component of the U-direction
	 * @param uZ the Z-component of the U-direction
	 * @param vX the X-component of the V-direction
	 * @param vY the Y-component of the V-direction
	 * @param vZ the Z-component of the V-direction
	 * @param wX the X-component of the W-direction
	 * @param wY the Y-component of the W-direction
	 * @param wZ the Z-component of the W-direction
	 */
	protected final void intersectionLHSSetOrthonormalBasisS(final float uX, final float uY, final float uZ, final float vX, final float vY, final float vZ, final float wX, final float wY, final float wZ) {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = uX;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = uY;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = uZ;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = vX;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = vY;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = vZ;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = wX;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = wY;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = wZ;
	}
	
	/**
	 * Sets the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24} from {@link #orthonormalBasis33FArray_$private$9}.
	 */
	protected final void intersectionLHSSetOrthonormalBasisSFromOrthonormalBasis33F() {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = orthonormalBasis33FGetUX();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = orthonormalBasis33FGetUY();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = orthonormalBasis33FGetUZ();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = orthonormalBasis33FGetVX();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = orthonormalBasis33FGetVY();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = orthonormalBasis33FGetVZ();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = orthonormalBasis33FGetWX();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = orthonormalBasis33FGetWY();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = orthonormalBasis33FGetWZ();
	}
	
	/**
	 * Sets the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @param x the X-component of the W-direction
	 * @param y the Y-component of the W-direction
	 * @param z the Z-component of the W-direction
	 */
	protected final void intersectionLHSSetOrthonormalBasisSW(final float x, final float y, final float z) {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = x;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = y;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = z;
	}
	
	/**
	 * Sets the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24} to a transformed representation.
	 * 
	 * @param x the X-component of the vector to transform with
	 * @param y the Y-component of the vector to transform with
	 * @param z the Z-component of the vector to transform with
	 */
	protected final void intersectionLHSSetOrthonormalBasisSWTransform(final float x, final float y, final float z) {
		final float oldUX = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0];
		final float oldUY = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1];
		final float oldUZ = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2];
		final float oldVX = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0];
		final float oldVY = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1];
		final float oldVZ = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2];
		final float oldWX = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
		final float oldWY = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
		final float oldWZ = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
		
		final float newWX = x * oldUX + y * oldVX + z * oldWX;
		final float newWY = x * oldUY + y * oldVY + z * oldWY;
		final float newWZ = x * oldUZ + y * oldVZ + z * oldWZ;
		final float newWLengthReciprocal = vector3FLengthReciprocal(newWX, newWY, newWZ);
		final float newWNormalizedX = newWX * newWLengthReciprocal;
		final float newWNormalizedY = newWY * newWLengthReciprocal;
		final float newWNormalizedZ = newWZ * newWLengthReciprocal;
		
		final float newUX = oldVY * newWNormalizedZ - oldVZ * newWNormalizedY;
		final float newUY = oldVZ * newWNormalizedX - oldVX * newWNormalizedZ;
		final float newUZ = oldVX * newWNormalizedY - oldVY * newWNormalizedX;
		final float newULengthReciprocal = vector3FLengthReciprocal(newUX, newUY, newUZ);
		final float newUNormalizedX = newUX * newULengthReciprocal;
		final float newUNormalizedY = newUY * newULengthReciprocal;
		final float newUNormalizedZ = newUZ * newULengthReciprocal;
		
		final float newVNormalizedX = newWNormalizedY * newUNormalizedZ - newWNormalizedZ * newUNormalizedY;
		final float newVNormalizedY = newWNormalizedZ * newUNormalizedX - newWNormalizedX * newUNormalizedZ;
		final float newVNormalizedZ = newWNormalizedX * newUNormalizedY - newWNormalizedY * newUNormalizedX;
		
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = newUNormalizedX;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = newUNormalizedY;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = newUNormalizedZ;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = newVNormalizedX;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = newVNormalizedY;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = newVNormalizedZ;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = newWNormalizedX;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = newWNormalizedY;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = newWNormalizedZ;
	}
	
	/**
	 * Sets the primitive index in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @param primitiveIndex the primitive index
	 */
	protected final void intersectionLHSSetPrimitiveIndex(final int primitiveIndex) {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX] = primitiveIndex;
	}
	
	/**
	 * Sets the surface intersection point in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @param x the X-component of the surface intersection point
	 * @param y the Y-component of the surface intersection point
	 * @param z the Z-component of the surface intersection point
	 */
	protected final void intersectionLHSSetSurfaceIntersectionPoint(final float x, final float y, final float z) {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0] = x;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1] = y;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2] = z;
	}
	
	/**
	 * Sets the texture coordinates in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @param x the X-component of the texture coordinates
	 * @param y the Y-component of the texture coordinates
	 */
	protected final void intersectionLHSSetTextureCoordinates(final float x, final float y) {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0] = x;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1] = y;
	}
	
	/**
	 * Transforms the intersection on the left-hand side.
	 * 
	 * @param matrixElement11 the element at row 1 and column 1 in the matrix
	 * @param matrixElement12 the element at row 1 and column 2 in the matrix
	 * @param matrixElement13 the element at row 1 and column 3 in the matrix
	 * @param matrixElement14 the element at row 1 and column 4 in the matrix
	 * @param matrixElement21 the element at row 2 and column 1 in the matrix
	 * @param matrixElement22 the element at row 2 and column 2 in the matrix
	 * @param matrixElement23 the element at row 2 and column 3 in the matrix
	 * @param matrixElement24 the element at row 2 and column 4 in the matrix
	 * @param matrixElement31 the element at row 3 and column 1 in the matrix
	 * @param matrixElement32 the element at row 3 and column 2 in the matrix
	 * @param matrixElement33 the element at row 3 and column 3 in the matrix
	 * @param matrixElement34 the element at row 3 and column 4 in the matrix
	 * @param matrixElement41 the element at row 4 and column 1 in the matrix
	 * @param matrixElement42 the element at row 4 and column 2 in the matrix
	 * @param matrixElement43 the element at row 4 and column 3 in the matrix
	 * @param matrixElement44 the element at row 4 and column 4 in the matrix
	 * @param matrixInverseElement11 the element at row 1 and column 1 in the inverse matrix
	 * @param matrixInverseElement12 the element at row 1 and column 2 in the inverse matrix
	 * @param matrixInverseElement13 the element at row 1 and column 3 in the inverse matrix
	 * @param matrixInverseElement21 the element at row 2 and column 1 in the inverse matrix
	 * @param matrixInverseElement22 the element at row 2 and column 2 in the inverse matrix
	 * @param matrixInverseElement23 the element at row 2 and column 3 in the inverse matrix
	 * @param matrixInverseElement31 the element at row 3 and column 1 in the inverse matrix
	 * @param matrixInverseElement32 the element at row 3 and column 2 in the inverse matrix
	 * @param matrixInverseElement33 the element at row 3 and column 3 in the inverse matrix
	 */
	protected final void intersectionLHSTransform(final float matrixElement11, final float matrixElement12, final float matrixElement13, final float matrixElement14, final float matrixElement21, final float matrixElement22, final float matrixElement23, final float matrixElement24, final float matrixElement31, final float matrixElement32, final float matrixElement33, final float matrixElement34, final float matrixElement41, final float matrixElement42, final float matrixElement43, final float matrixElement44, final float matrixInverseElement11, final float matrixInverseElement12, final float matrixInverseElement13, final float matrixInverseElement21, final float matrixInverseElement22, final float matrixInverseElement23, final float matrixInverseElement31, final float matrixInverseElement32, final float matrixInverseElement33) {
//		Retrieve the old variables from the intersection array:
		final float oldOrthonormalBasisGUX = intersectionLHSGetOrthonormalBasisGUX();
		final float oldOrthonormalBasisGUY = intersectionLHSGetOrthonormalBasisGUY();
		final float oldOrthonormalBasisGUZ = intersectionLHSGetOrthonormalBasisGUZ();
		final float oldOrthonormalBasisGVX = intersectionLHSGetOrthonormalBasisGVX();
		final float oldOrthonormalBasisGVY = intersectionLHSGetOrthonormalBasisGVY();
		final float oldOrthonormalBasisGVZ = intersectionLHSGetOrthonormalBasisGVZ();
		final float oldOrthonormalBasisGWX = intersectionLHSGetOrthonormalBasisGWX();
		final float oldOrthonormalBasisGWY = intersectionLHSGetOrthonormalBasisGWY();
		final float oldOrthonormalBasisGWZ = intersectionLHSGetOrthonormalBasisGWZ();
		final float oldOrthonormalBasisSUX = intersectionLHSGetOrthonormalBasisSUX();
		final float oldOrthonormalBasisSUY = intersectionLHSGetOrthonormalBasisSUY();
		final float oldOrthonormalBasisSUZ = intersectionLHSGetOrthonormalBasisSUZ();
		final float oldOrthonormalBasisSVX = intersectionLHSGetOrthonormalBasisSVX();
		final float oldOrthonormalBasisSVY = intersectionLHSGetOrthonormalBasisSVY();
		final float oldOrthonormalBasisSVZ = intersectionLHSGetOrthonormalBasisSVZ();
		final float oldOrthonormalBasisSWX = intersectionLHSGetOrthonormalBasisSWX();
		final float oldOrthonormalBasisSWY = intersectionLHSGetOrthonormalBasisSWY();
		final float oldOrthonormalBasisSWZ = intersectionLHSGetOrthonormalBasisSWZ();
		final float oldSurfaceIntersectionPointX = intersectionLHSGetSurfaceIntersectionPointX();
		final float oldSurfaceIntersectionPointY = intersectionLHSGetSurfaceIntersectionPointY();
		final float oldSurfaceIntersectionPointZ = intersectionLHSGetSurfaceIntersectionPointZ();
		
//		Transform the U-direction of the geometric orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisGUX, oldOrthonormalBasisGUY, oldOrthonormalBasisGUZ);
		
//		Retrieve the transformed U-direction of the geometric orthonormal basis:
		final float newOrthonormalBasisGUX = vector3FGetX();
		final float newOrthonormalBasisGUY = vector3FGetY();
		final float newOrthonormalBasisGUZ = vector3FGetZ();
		
//		Transform the V-direction of the geometric orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisGVX, oldOrthonormalBasisGVY, oldOrthonormalBasisGVZ);
		
//		Retrieve the transformed V-direction of the geometric orthonormal basis:
		final float newOrthonormalBasisGVX = vector3FGetX();
		final float newOrthonormalBasisGVY = vector3FGetY();
		final float newOrthonormalBasisGVZ = vector3FGetZ();
		
//		Transform the W-direction of the geometric orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisGWX, oldOrthonormalBasisGWY, oldOrthonormalBasisGWZ);
		
//		Retrieve the transformed W-direction of the geometric orthonormal basis:
		final float newOrthonormalBasisGWX = vector3FGetX();
		final float newOrthonormalBasisGWY = vector3FGetY();
		final float newOrthonormalBasisGWZ = vector3FGetZ();
		
//		Transform the U-direction of the shading orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisSUX, oldOrthonormalBasisSUY, oldOrthonormalBasisSUZ);
		
//		Retrieve the transformed U-direction of the shading orthonormal basis:
		final float newOrthonormalBasisSUX = vector3FGetX();
		final float newOrthonormalBasisSUY = vector3FGetY();
		final float newOrthonormalBasisSUZ = vector3FGetZ();
		
//		Transform the V-direction of the shading orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisSVX, oldOrthonormalBasisSVY, oldOrthonormalBasisSVZ);
		
//		Retrieve the transformed V-direction of the shading orthonormal basis:
		final float newOrthonormalBasisSVX = vector3FGetX();
		final float newOrthonormalBasisSVY = vector3FGetY();
		final float newOrthonormalBasisSVZ = vector3FGetZ();
		
//		Transform the W-direction of the shading orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisSWX, oldOrthonormalBasisSWY, oldOrthonormalBasisSWZ);
		
//		Retrieve the transformed W-direction of the shading orthonormal basis:
		final float newOrthonormalBasisSWX = vector3FGetX();
		final float newOrthonormalBasisSWY = vector3FGetY();
		final float newOrthonormalBasisSWZ = vector3FGetZ();
		
//		Transform the surface intersection point:
		point3FSetMatrix44FTransformAndDivide(matrixElement11, matrixElement12, matrixElement13, matrixElement14, matrixElement21, matrixElement22, matrixElement23, matrixElement24, matrixElement31, matrixElement32, matrixElement33, matrixElement34, matrixElement41, matrixElement42, matrixElement43, matrixElement44, oldSurfaceIntersectionPointX, oldSurfaceIntersectionPointY, oldSurfaceIntersectionPointZ);
		
//		Retrieve the transformed surface intersection point:
		final float newSurfaceIntersectionPointX = point3FGetX();
		final float newSurfaceIntersectionPointY = point3FGetY();
		final float newSurfaceIntersectionPointZ = point3FGetZ();
		
//		Update the intersection array:
		intersectionLHSSetOrthonormalBasisG(newOrthonormalBasisGUX, newOrthonormalBasisGUY, newOrthonormalBasisGUZ, newOrthonormalBasisGVX, newOrthonormalBasisGVY, newOrthonormalBasisGVZ, newOrthonormalBasisGWX, newOrthonormalBasisGWY, newOrthonormalBasisGWZ);
		intersectionLHSSetOrthonormalBasisS(newOrthonormalBasisSUX, newOrthonormalBasisSUY, newOrthonormalBasisSUZ, newOrthonormalBasisSVX, newOrthonormalBasisSVY, newOrthonormalBasisSVZ, newOrthonormalBasisSWX, newOrthonormalBasisSWY, newOrthonormalBasisSWZ);
		intersectionLHSSetSurfaceIntersectionPoint(newSurfaceIntersectionPointX, newSurfaceIntersectionPointY, newSurfaceIntersectionPointZ);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Intersection - RHS //////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the value of the X-component of the U-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the X-component of the U-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGUX() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0];
	}
	
	/**
	 * Returns the value of the Y-component of the U-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the Y-component of the U-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGUY() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1];
	}
	
	/**
	 * Returns the value of the Z-component of the U-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the Z-component of the U-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGUZ() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2];
	}
	
	/**
	 * Returns the value of the X-component of the V-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the X-component of the V-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGVX() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0];
	}
	
	/**
	 * Returns the value of the Y-component of the V-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the Y-component of the V-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGVY() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1];
	}
	
	/**
	 * Returns the value of the Z-component of the V-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the Z-component of the V-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGVZ() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2];
	}
	
	/**
	 * Returns the value of the X-component of the W-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the X-component of the W-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGWX() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
	}
	
	/**
	 * Returns the value of the Y-component of the W-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the Y-component of the W-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGWY() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
	}
	
	/**
	 * Returns the value of the Z-component of the W-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the Z-component of the W-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGWZ() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
	}
	
	/**
	 * Returns the value of the X-component of the U-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the X-component of the U-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSUX() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0];
	}
	
	/**
	 * Returns the value of the Y-component of the U-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the Y-component of the U-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSUY() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1];
	}
	
	/**
	 * Returns the value of the Z-component of the U-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the Z-component of the U-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSUZ() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2];
	}
	
	/**
	 * Returns the value of the X-component of the V-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the X-component of the V-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSVX() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0];
	}
	
	/**
	 * Returns the value of the Y-component of the V-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the Y-component of the V-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSVY() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1];
	}
	
	/**
	 * Returns the value of the Z-component of the V-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the Z-component of the V-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSVZ() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2];
	}
	
	/**
	 * Returns the value of the X-component of the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the X-component of the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSWX() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
	}
	
	/**
	 * Returns the value of the Y-component of the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the Y-component of the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSWY() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
	}
	
	/**
	 * Returns the value of the Z-component of the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the Z-component of the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSWZ() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
	}
	
	/**
	 * Returns the value of the X-component of the surface intersection point in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the X-component of the surface intersection point in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetSurfaceIntersectionPointX() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0];
	}
	
	/**
	 * Returns the value of the Y-component of the surface intersection point in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the Y-component of the surface intersection point in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetSurfaceIntersectionPointY() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1];
	}
	
	/**
	 * Returns the value of the Z-component of the surface intersection point in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the Z-component of the surface intersection point in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetSurfaceIntersectionPointZ() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2];
	}
	
	/**
	 * Returns the value of the X-component of the texture coordinates in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the X-component of the texture coordinates in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetTextureCoordinatesX() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0];
	}
	
	/**
	 * Returns the value of the Y-component of the texture coordinates in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of the Y-component of the texture coordinates in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetTextureCoordinatesY() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1];
	}
	
	/**
	 * Returns the primitive index in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the primitive index in {@link #intersectionRHSArray_$private$24}
	 */
	protected final int intersectionRHSGetPrimitiveIndex() {
		return (int)(this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX]);
	}
	
	/**
	 * Moves the intersection data from right-hand side to left-hand side.
	 */
	protected final void intersectionRHSMoveToLHS() {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0];
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1];
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2];
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0];
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1];
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2];
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
		
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0];
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1];
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2];
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0];
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1];
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2];
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
		
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX];
		
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0];
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1];
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2];
		
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0];
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1] = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1];
	}
	
	/**
	 * Sets the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @param uX the X-component of the U-direction
	 * @param uY the Y-component of the U-direction
	 * @param uZ the Z-component of the U-direction
	 * @param vX the X-component of the V-direction
	 * @param vY the Y-component of the V-direction
	 * @param vZ the Z-component of the V-direction
	 * @param wX the X-component of the W-direction
	 * @param wY the Y-component of the W-direction
	 * @param wZ the Z-component of the W-direction
	 */
	protected final void intersectionRHSSetOrthonormalBasisG(final float uX, final float uY, final float uZ, final float vX, final float vY, final float vZ, final float wX, final float wY, final float wZ) {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0] = uX;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1] = uY;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2] = uZ;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0] = vX;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1] = vY;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2] = vZ;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0] = wX;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1] = wY;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2] = wZ;
	}
	
	/**
	 * Sets the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24} from {@link #orthonormalBasis33FArray_$private$9}.
	 */
	protected final void intersectionRHSSetOrthonormalBasisGFromOrthonormalBasis33F() {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0] = orthonormalBasis33FGetUX();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1] = orthonormalBasis33FGetUY();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2] = orthonormalBasis33FGetUZ();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0] = orthonormalBasis33FGetVX();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1] = orthonormalBasis33FGetVY();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2] = orthonormalBasis33FGetVZ();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0] = orthonormalBasis33FGetWX();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1] = orthonormalBasis33FGetWY();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2] = orthonormalBasis33FGetWZ();
	}
	
	/**
	 * Sets the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @param uX the X-component of the U-direction
	 * @param uY the Y-component of the U-direction
	 * @param uZ the Z-component of the U-direction
	 * @param vX the X-component of the V-direction
	 * @param vY the Y-component of the V-direction
	 * @param vZ the Z-component of the V-direction
	 * @param wX the X-component of the W-direction
	 * @param wY the Y-component of the W-direction
	 * @param wZ the Z-component of the W-direction
	 */
	protected final void intersectionRHSSetOrthonormalBasisS(final float uX, final float uY, final float uZ, final float vX, final float vY, final float vZ, final float wX, final float wY, final float wZ) {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = uX;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = uY;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = uZ;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = vX;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = vY;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = vZ;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = wX;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = wY;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = wZ;
	}
	
	/**
	 * Sets the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24} from {@link #orthonormalBasis33FArray_$private$9}.
	 */
	protected final void intersectionRHSSetOrthonormalBasisSFromOrthonormalBasis33F() {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = orthonormalBasis33FGetUX();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = orthonormalBasis33FGetUY();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = orthonormalBasis33FGetUZ();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = orthonormalBasis33FGetVX();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = orthonormalBasis33FGetVY();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = orthonormalBasis33FGetVZ();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = orthonormalBasis33FGetWX();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = orthonormalBasis33FGetWY();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = orthonormalBasis33FGetWZ();
	}
	
	/**
	 * Sets the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @param x the X-component of the W-direction
	 * @param y the Y-component of the W-direction
	 * @param z the Z-component of the W-direction
	 */
	protected final void intersectionRHSSetOrthonormalBasisSW(final float x, final float y, final float z) {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = x;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = y;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = z;
	}
	
	/**
	 * Sets the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24} to a transformed representation.
	 * 
	 * @param x the X-component of the vector to transform with
	 * @param y the Y-component of the vector to transform with
	 * @param z the Z-component of the vector to transform with
	 */
	protected final void intersectionRHSSetOrthonormalBasisSWTransform(final float x, final float y, final float z) {
		final float oldUX = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0];
		final float oldUY = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1];
		final float oldUZ = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2];
		final float oldVX = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0];
		final float oldVY = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1];
		final float oldVZ = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2];
		final float oldWX = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
		final float oldWY = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
		final float oldWZ = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
		
		final float newWX = x * oldUX + y * oldVX + z * oldWX;
		final float newWY = x * oldUY + y * oldVY + z * oldWY;
		final float newWZ = x * oldUZ + y * oldVZ + z * oldWZ;
		final float newWLengthReciprocal = vector3FLengthReciprocal(newWX, newWY, newWZ);
		final float newWNormalizedX = newWX * newWLengthReciprocal;
		final float newWNormalizedY = newWY * newWLengthReciprocal;
		final float newWNormalizedZ = newWZ * newWLengthReciprocal;
		
		final float newUX = oldVY * newWNormalizedZ - oldVZ * newWNormalizedY;
		final float newUY = oldVZ * newWNormalizedX - oldVX * newWNormalizedZ;
		final float newUZ = oldVX * newWNormalizedY - oldVY * newWNormalizedX;
		final float newULengthReciprocal = vector3FLengthReciprocal(newUX, newUY, newUZ);
		final float newUNormalizedX = newUX * newULengthReciprocal;
		final float newUNormalizedY = newUY * newULengthReciprocal;
		final float newUNormalizedZ = newUZ * newULengthReciprocal;
		
		final float newVNormalizedX = newWNormalizedY * newUNormalizedZ - newWNormalizedZ * newUNormalizedY;
		final float newVNormalizedY = newWNormalizedZ * newUNormalizedX - newWNormalizedX * newUNormalizedZ;
		final float newVNormalizedZ = newWNormalizedX * newUNormalizedY - newWNormalizedY * newUNormalizedX;
		
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = newUNormalizedX;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = newUNormalizedY;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = newUNormalizedZ;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = newVNormalizedX;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = newVNormalizedY;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = newVNormalizedZ;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = newWNormalizedX;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = newWNormalizedY;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = newWNormalizedZ;
	}
	
	/**
	 * Sets the primitive index in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @param primitiveIndex the primitive index
	 */
	protected final void intersectionRHSSetPrimitiveIndex(final int primitiveIndex) {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX] = primitiveIndex;
	}
	
	/**
	 * Sets the surface intersection point in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @param x the X-component of the surface intersection point
	 * @param y the Y-component of the surface intersection point
	 * @param z the Z-component of the surface intersection point
	 */
	protected final void intersectionRHSSetSurfaceIntersectionPoint(final float x, final float y, final float z) {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0] = x;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1] = y;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2] = z;
	}
	
	/**
	 * Sets the texture coordinates in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @param x the X-component of the texture coordinates
	 * @param y the Y-component of the texture coordinates
	 */
	protected final void intersectionRHSSetTextureCoordinates(final float x, final float y) {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0] = x;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1] = y;
	}
	
	/**
	 * Transforms the intersection on the right-hand side.
	 * 
	 * @param matrixElement11 the element at row 1 and column 1 in the matrix
	 * @param matrixElement12 the element at row 1 and column 2 in the matrix
	 * @param matrixElement13 the element at row 1 and column 3 in the matrix
	 * @param matrixElement14 the element at row 1 and column 4 in the matrix
	 * @param matrixElement21 the element at row 2 and column 1 in the matrix
	 * @param matrixElement22 the element at row 2 and column 2 in the matrix
	 * @param matrixElement23 the element at row 2 and column 3 in the matrix
	 * @param matrixElement24 the element at row 2 and column 4 in the matrix
	 * @param matrixElement31 the element at row 3 and column 1 in the matrix
	 * @param matrixElement32 the element at row 3 and column 2 in the matrix
	 * @param matrixElement33 the element at row 3 and column 3 in the matrix
	 * @param matrixElement34 the element at row 3 and column 4 in the matrix
	 * @param matrixElement41 the element at row 4 and column 1 in the matrix
	 * @param matrixElement42 the element at row 4 and column 2 in the matrix
	 * @param matrixElement43 the element at row 4 and column 3 in the matrix
	 * @param matrixElement44 the element at row 4 and column 4 in the matrix
	 * @param matrixInverseElement11 the element at row 1 and column 1 in the inverse matrix
	 * @param matrixInverseElement12 the element at row 1 and column 2 in the inverse matrix
	 * @param matrixInverseElement13 the element at row 1 and column 3 in the inverse matrix
	 * @param matrixInverseElement21 the element at row 2 and column 1 in the inverse matrix
	 * @param matrixInverseElement22 the element at row 2 and column 2 in the inverse matrix
	 * @param matrixInverseElement23 the element at row 2 and column 3 in the inverse matrix
	 * @param matrixInverseElement31 the element at row 3 and column 1 in the inverse matrix
	 * @param matrixInverseElement32 the element at row 3 and column 2 in the inverse matrix
	 * @param matrixInverseElement33 the element at row 3 and column 3 in the inverse matrix
	 */
	protected final void intersectionRHSTransform(final float matrixElement11, final float matrixElement12, final float matrixElement13, final float matrixElement14, final float matrixElement21, final float matrixElement22, final float matrixElement23, final float matrixElement24, final float matrixElement31, final float matrixElement32, final float matrixElement33, final float matrixElement34, final float matrixElement41, final float matrixElement42, final float matrixElement43, final float matrixElement44, final float matrixInverseElement11, final float matrixInverseElement12, final float matrixInverseElement13, final float matrixInverseElement21, final float matrixInverseElement22, final float matrixInverseElement23, final float matrixInverseElement31, final float matrixInverseElement32, final float matrixInverseElement33) {
//		Retrieve the old variables from the intersection array:
		final float oldOrthonormalBasisGUX = intersectionRHSGetOrthonormalBasisGUX();
		final float oldOrthonormalBasisGUY = intersectionRHSGetOrthonormalBasisGUY();
		final float oldOrthonormalBasisGUZ = intersectionRHSGetOrthonormalBasisGUZ();
		final float oldOrthonormalBasisGVX = intersectionRHSGetOrthonormalBasisGVX();
		final float oldOrthonormalBasisGVY = intersectionRHSGetOrthonormalBasisGVY();
		final float oldOrthonormalBasisGVZ = intersectionRHSGetOrthonormalBasisGVZ();
		final float oldOrthonormalBasisGWX = intersectionRHSGetOrthonormalBasisGWX();
		final float oldOrthonormalBasisGWY = intersectionRHSGetOrthonormalBasisGWY();
		final float oldOrthonormalBasisGWZ = intersectionRHSGetOrthonormalBasisGWZ();
		final float oldOrthonormalBasisSUX = intersectionRHSGetOrthonormalBasisSUX();
		final float oldOrthonormalBasisSUY = intersectionRHSGetOrthonormalBasisSUY();
		final float oldOrthonormalBasisSUZ = intersectionRHSGetOrthonormalBasisSUZ();
		final float oldOrthonormalBasisSVX = intersectionRHSGetOrthonormalBasisSVX();
		final float oldOrthonormalBasisSVY = intersectionRHSGetOrthonormalBasisSVY();
		final float oldOrthonormalBasisSVZ = intersectionRHSGetOrthonormalBasisSVZ();
		final float oldOrthonormalBasisSWX = intersectionRHSGetOrthonormalBasisSWX();
		final float oldOrthonormalBasisSWY = intersectionRHSGetOrthonormalBasisSWY();
		final float oldOrthonormalBasisSWZ = intersectionRHSGetOrthonormalBasisSWZ();
		final float oldSurfaceIntersectionPointX = intersectionRHSGetSurfaceIntersectionPointX();
		final float oldSurfaceIntersectionPointY = intersectionRHSGetSurfaceIntersectionPointY();
		final float oldSurfaceIntersectionPointZ = intersectionRHSGetSurfaceIntersectionPointZ();
		
//		Transform the U-direction of the geometric orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisGUX, oldOrthonormalBasisGUY, oldOrthonormalBasisGUZ);
		
//		Retrieve the transformed U-direction of the geometric orthonormal basis:
		final float newOrthonormalBasisGUX = vector3FGetX();
		final float newOrthonormalBasisGUY = vector3FGetY();
		final float newOrthonormalBasisGUZ = vector3FGetZ();
		
//		Transform the V-direction of the geometric orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisGVX, oldOrthonormalBasisGVY, oldOrthonormalBasisGVZ);
		
//		Retrieve the transformed V-direction of the geometric orthonormal basis:
		final float newOrthonormalBasisGVX = vector3FGetX();
		final float newOrthonormalBasisGVY = vector3FGetY();
		final float newOrthonormalBasisGVZ = vector3FGetZ();
		
//		Transform the W-direction of the geometric orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisGWX, oldOrthonormalBasisGWY, oldOrthonormalBasisGWZ);
		
//		Retrieve the transformed W-direction of the geometric orthonormal basis:
		final float newOrthonormalBasisGWX = vector3FGetX();
		final float newOrthonormalBasisGWY = vector3FGetY();
		final float newOrthonormalBasisGWZ = vector3FGetZ();
		
//		Transform the U-direction of the shading orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisSUX, oldOrthonormalBasisSUY, oldOrthonormalBasisSUZ);
		
//		Retrieve the transformed U-direction of the shading orthonormal basis:
		final float newOrthonormalBasisSUX = vector3FGetX();
		final float newOrthonormalBasisSUY = vector3FGetY();
		final float newOrthonormalBasisSUZ = vector3FGetZ();
		
//		Transform the V-direction of the shading orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisSVX, oldOrthonormalBasisSVY, oldOrthonormalBasisSVZ);
		
//		Retrieve the transformed V-direction of the shading orthonormal basis:
		final float newOrthonormalBasisSVX = vector3FGetX();
		final float newOrthonormalBasisSVY = vector3FGetY();
		final float newOrthonormalBasisSVZ = vector3FGetZ();
		
//		Transform the W-direction of the shading orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisSWX, oldOrthonormalBasisSWY, oldOrthonormalBasisSWZ);
		
//		Retrieve the transformed W-direction of the shading orthonormal basis:
		final float newOrthonormalBasisSWX = vector3FGetX();
		final float newOrthonormalBasisSWY = vector3FGetY();
		final float newOrthonormalBasisSWZ = vector3FGetZ();
		
//		Transform the surface intersection point:
		point3FSetMatrix44FTransformAndDivide(matrixElement11, matrixElement12, matrixElement13, matrixElement14, matrixElement21, matrixElement22, matrixElement23, matrixElement24, matrixElement31, matrixElement32, matrixElement33, matrixElement34, matrixElement41, matrixElement42, matrixElement43, matrixElement44, oldSurfaceIntersectionPointX, oldSurfaceIntersectionPointY, oldSurfaceIntersectionPointZ);
		
//		Retrieve the transformed surface intersection point:
		final float newSurfaceIntersectionPointX = point3FGetX();
		final float newSurfaceIntersectionPointY = point3FGetY();
		final float newSurfaceIntersectionPointZ = point3FGetZ();
		
//		Update the intersection array:
		intersectionRHSSetOrthonormalBasisG(newOrthonormalBasisGUX, newOrthonormalBasisGUY, newOrthonormalBasisGUZ, newOrthonormalBasisGVX, newOrthonormalBasisGVY, newOrthonormalBasisGVZ, newOrthonormalBasisGWX, newOrthonormalBasisGWY, newOrthonormalBasisGWZ);
		intersectionRHSSetOrthonormalBasisS(newOrthonormalBasisSUX, newOrthonormalBasisSUY, newOrthonormalBasisSUZ, newOrthonormalBasisSVX, newOrthonormalBasisSVY, newOrthonormalBasisSVZ, newOrthonormalBasisSWX, newOrthonormalBasisSWY, newOrthonormalBasisSWZ);
		intersectionRHSSetSurfaceIntersectionPoint(newSurfaceIntersectionPointX, newSurfaceIntersectionPointY, newSurfaceIntersectionPointZ);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// OrthonormalBasis33F /////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the value of the X-component of the U-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of the X-component of the U-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetUX() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0];
	}
	
	/**
	 * Returns the value of the Y-component of the U-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of the Y-component of the U-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetUY() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1];
	}
	
	/**
	 * Returns the value of the Z-component of the U-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of the Z-component of the U-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetUZ() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2];
	}
	
	/**
	 * Returns the value of the X-component of the V-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of the X-component of the V-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetVX() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0];
	}
	
	/**
	 * Returns the value of the Y-component of the V-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of the Y-component of the V-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetVY() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1];
	}
	
	/**
	 * Returns the value of the Z-component of the V-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of the Z-component of the V-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetVZ() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2];
	}
	
	/**
	 * Returns the value of the X-component of the W-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of the X-component of the W-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetWX() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0];
	}
	
	/**
	 * Returns the value of the Y-component of the W-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of the Y-component of the W-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetWY() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1];
	}
	
	/**
	 * Returns the value of the Z-component of the W-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of the Z-component of the W-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetWZ() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2];
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the U-direction vector represented by {@code uX}, {@code uY} and {@code uZ} and the V-direction vector represented by {@code vX}, {@code vY} and {@code vZ}.
	 * <p>
	 * This method will normalize the U-direction and V-direction vectors.
	 * 
	 * @param uX the X-component of the U-direction
	 * @param uY the Y-component of the U-direction
	 * @param uZ the Z-component of the U-direction
	 * @param vX the X-component of the V-direction
	 * @param vY the Y-component of the V-direction
	 * @param vZ the Z-component of the V-direction
	 */
	protected final void orthonormalBasis33FSetFromUV(final float uX, final float uY, final float uZ, final float vX, final float vY, final float vZ) {
//		Compute the normalized U-direction of the orthonormal basis:
		final float uLengthReciprocal = vector3FLengthReciprocal(uX, uY, uZ);
		final float uNormalizedX = uX * uLengthReciprocal;
		final float uNormalizedY = uY * uLengthReciprocal;
		final float uNormalizedZ = uZ * uLengthReciprocal;
		
//		Compute the normalized V-direction of the orthonormal basis:
		final float vLengthReciprocal = vector3FLengthReciprocal(vX, vY, vZ);
		final float vNormalizedX = vX * vLengthReciprocal;
		final float vNormalizedY = vY * vLengthReciprocal;
		final float vNormalizedZ = vZ * vLengthReciprocal;
		
//		Compute the normalized W-direction of the orthonormal basis:
		final float wNormalizedX = uNormalizedY * vNormalizedZ - uNormalizedZ * vNormalizedY;
		final float wNormalizedY = uNormalizedZ * vNormalizedX - uNormalizedX * vNormalizedZ;
		final float wNormalizedZ = uNormalizedX * vNormalizedY - uNormalizedY * vNormalizedX;
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = uNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = uNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = uNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = vNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = vNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = vNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = wNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = wNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = wNormalizedZ;
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the W-direction vector represented by {@code wX}, {@code wY} and {@code wZ}.
	 * <p>
	 * This method will normalize the W-direction vector.
	 * 
	 * @param wX the X-component of the W-direction
	 * @param wY the Y-component of the W-direction
	 * @param wZ the Z-component of the W-direction
	 */
	protected final void orthonormalBasis33FSetFromW(final float wX, final float wY, final float wZ) {
//		Compute the normalized W-direction of the orthonormal basis:
		final float wLengthReciprocal = vector3FLengthReciprocal(wX, wY, wZ);
		final float wNormalizedX = wX * wLengthReciprocal;
		final float wNormalizedY = wY * wLengthReciprocal;
		final float wNormalizedZ = wZ * wLengthReciprocal;
		
//		Compute the absolute component values of the normalized W-direction, which are used to determine the orientation of the V-direction of the orthonormal basis:
		final float wNormalizedXAbs = abs(wNormalizedX);
		final float wNormalizedYAbs = abs(wNormalizedY);
		final float wNormalizedZAbs = abs(wNormalizedZ);
		
//		Compute variables used to determine the orientation of the V-direction of the orthonormal basis:
		final boolean isX = wNormalizedXAbs < wNormalizedYAbs && wNormalizedXAbs < wNormalizedZAbs;
		final boolean isY = wNormalizedYAbs < wNormalizedZAbs;
		
//		Compute the normalized V-direction of the orthonormal basis:
		final float vX = isX ? +0.0F         : isY ? +wNormalizedZ : +wNormalizedY;
		final float vY = isX ? +wNormalizedZ : isY ? +0.0F         : -wNormalizedX;
		final float vZ = isX ? -wNormalizedY : isY ? -wNormalizedX : +0.0F;
		final float vLengthReciprocal = vector3FLengthReciprocal(vX, vY, vZ);
		final float vNormalizedX = vX * vLengthReciprocal;
		final float vNormalizedY = vY * vLengthReciprocal;
		final float vNormalizedZ = vZ * vLengthReciprocal;
		
//		Compute the normalized U-direction of the orthonormal basis:
		final float uNormalizedX = vNormalizedY * wNormalizedZ - vNormalizedZ * wNormalizedY;
		final float uNormalizedY = vNormalizedZ * wNormalizedX - vNormalizedX * wNormalizedZ;
		final float uNormalizedZ = vNormalizedX * wNormalizedY - vNormalizedY * wNormalizedX;
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = uNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = uNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = uNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = vNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = vNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = vNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = wNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = wNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = wNormalizedZ;
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the W-direction vector represented by {@code wX}, {@code wY} and {@code wZ} and the reference V-direction vector represented by {@code vReferenceX}, {@code vReferenceY} and {@code vReferenceZ}.
	 * <p>
	 * This method will normalize the W-direction and reference V-direction vectors.
	 * 
	 * @param wX the X-component of the W-direction
	 * @param wY the Y-component of the W-direction
	 * @param wZ the Z-component of the W-direction
	 * @param vReferenceX the X-component of the reference V-direction
	 * @param vReferenceY the Y-component of the reference V-direction
	 * @param vReferenceZ the Z-component of the reference V-direction
	 */
	protected final void orthonormalBasis33FSetFromWV(final float wX, final float wY, final float wZ, final float vReferenceX, final float vReferenceY, final float vReferenceZ) {
//		Compute the normalized W-direction of the orthonormal basis:
		final float wLengthReciprocal = vector3FLengthReciprocal(wX, wY, wZ);
		final float wNormalizedX = wX * wLengthReciprocal;
		final float wNormalizedY = wY * wLengthReciprocal;
		final float wNormalizedZ = wZ * wLengthReciprocal;
		
//		Compute the normalized V-direction used as a reference for the orthonormal basis:
		final float vReferenceLengthReciprocal = vector3FLengthReciprocal(vReferenceX, vReferenceY, vReferenceZ);
		final float vReferenceNormalizedX = vReferenceX * vReferenceLengthReciprocal;
		final float vReferenceNormalizedY = vReferenceY * vReferenceLengthReciprocal;
		final float vReferenceNormalizedZ = vReferenceZ * vReferenceLengthReciprocal;
		
//		Compute the normalized U-direction of the orthonormal basis:
		final float uX = vReferenceNormalizedY * wNormalizedZ - vReferenceNormalizedZ * wNormalizedY;
		final float uY = vReferenceNormalizedZ * wNormalizedX - vReferenceNormalizedX * wNormalizedZ;
		final float uZ = vReferenceNormalizedX * wNormalizedY - vReferenceNormalizedY * wNormalizedX;
		final float uLengthReciprocal = vector3FLengthReciprocal(uX, uY, uZ);
		final float uNormalizedX = uX * uLengthReciprocal;
		final float uNormalizedY = uY * uLengthReciprocal;
		final float uNormalizedZ = uZ * uLengthReciprocal;
		
//		Compute the normalized V-direction of the orthonormal basis:
		final float vNormalizedX = wNormalizedY * uNormalizedZ - wNormalizedZ * uNormalizedY;
		final float vNormalizedY = wNormalizedZ * uNormalizedX - wNormalizedX * uNormalizedZ;
		final float vNormalizedZ = wNormalizedX * uNormalizedY - wNormalizedY * uNormalizedX;
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = uNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = uNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = uNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = vNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = vNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = vNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = wNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = wNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = wNormalizedZ;
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the orthonormal basis in {@link #intersectionLHSArray_$private$24} that is used for geometry.
	 */
	protected final void orthonormalBasis33FSetIntersectionOrthonormalBasisGLHS() {
//		Get the orthonormal basis:
		final float uX = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0];
		final float uY = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1];
		final float uZ = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2];
		final float vX = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0];
		final float vY = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1];
		final float vZ = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2];
		final float wX = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
		final float wY = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
		final float wZ = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = uX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = uY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = uZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = vX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = vY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = vZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = wX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = wY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = wZ;
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the orthonormal basis in {@link #intersectionRHSArray_$private$24} that is used for geometry.
	 */
	protected final void orthonormalBasis33FSetIntersectionOrthonormalBasisGRHS() {
//		Get the orthonormal basis:
		final float uX = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0];
		final float uY = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1];
		final float uZ = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2];
		final float vX = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0];
		final float vY = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1];
		final float vZ = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2];
		final float wX = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
		final float wY = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
		final float wZ = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = uX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = uY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = uZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = vX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = vY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = vZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = wX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = wY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = wZ;
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the orthonormal basis in {@link #intersectionLHSArray_$private$24} that is used for shading.
	 */
	protected final void orthonormalBasis33FSetIntersectionOrthonormalBasisSLHS() {
//		Get the orthonormal basis:
		final float uX = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0];
		final float uY = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1];
		final float uZ = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2];
		final float vX = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0];
		final float vY = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1];
		final float vZ = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2];
		final float wX = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
		final float wY = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
		final float wZ = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = uX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = uY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = uZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = vX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = vY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = vZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = wX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = wY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = wZ;
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the orthonormal basis in {@link #intersectionRHSArray_$private$24} that is used for shading.
	 */
	protected final void orthonormalBasis33FSetIntersectionOrthonormalBasisSRHS() {
//		Get the orthonormal basis:
		final float uX = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0];
		final float uY = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1];
		final float uZ = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2];
		final float vX = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0];
		final float vY = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1];
		final float vZ = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2];
		final float wX = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
		final float wY = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
		final float wZ = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = uX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = uY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = uZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = vX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = vY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = vZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = wX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = wY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = wZ;
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the W-direction vector found in {@link #vector3FArray_$private$3}.
	 * <p>
	 * This method will normalize the W-direction vector.
	 */
	protected final void orthonormalBasis33FSetVector3F() {
		final float wX = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_X];
		final float wY = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Y];
		final float wZ = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Z];
		
		orthonormalBasis33FSetFromW(wX, wY, wZ);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Point2F /////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the value of the X-component in {@link #point2FArray_$private$2}.
	 * 
	 * @return the value of the X-component in {@link #point2FArray_$private$2}
	 */
	protected final float point2FGetX() {
		return this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_X];
	}
	
	/**
	 * Returns the value of the Y-component in {@link #point2FArray_$private$2}.
	 * 
	 * @return the value of the Y-component in {@link #point2FArray_$private$2}
	 */
	protected final float point2FGetY() {
		return this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_Y];
	}
	
	/**
	 * Sets a point in {@link #point2FArray_$private$2}.
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 */
	protected final void point2FSet(final float x, final float y) {
		this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_X] = x;
		this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_Y] = y;
	}
	
	/**
	 * Sets a point in {@link #point2FArray_$private$2}.
	 * <p>
	 * The point is constructed by sampling a point on a disk with a uniform distribution using concentric mapping.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param radius the radius of the disk
	 */
	protected final void point2FSetSampleDiskUniformDistributionByConcentricMapping(final float u, final float v, final float radius) {
		if(u == 0.0F && v == 0.0F) {
			this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_X] = 0.0F;
			this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_Y] = 0.0F;
		} else {
			final float a = u * 2.0F - 1.0F;
			final float b = v * 2.0F - 1.0F;
			
			final boolean isCaseA = a * a > b * b;
			
			final float phi = isCaseA ? Floats.PI_DIVIDED_BY_4 * (b / a) : Floats.PI_DIVIDED_BY_2 - Floats.PI_DIVIDED_BY_4 * (a / b);
			final float r = isCaseA ? radius * a : radius * b;
			
			this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_X] = r * cos(phi);
			this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_Y] = r * sin(phi);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Point3F /////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the distance from the point represented by {@code eyeX}, {@code eyeY} and {@code eyeZ} to the point represented by {@code lookAtX}, {@code lookAtY} and {@code lookAtZ}.
	 * 
	 * @param eyeX the value of the X-component for the eye point
	 * @param eyeY the value of the Y-component for the eye point
	 * @param eyeZ the value of the Z-component for the eye point
	 * @param lookAtX the value of the X-component for the look at point
	 * @param lookAtY the value of the Y-component for the look at point
	 * @param lookAtZ the value of the Z-component for the look at point
	 * @return the distance from the point represented by {@code eyeX}, {@code eyeY} and {@code eyeZ} to the point represented by {@code lookAtX}, {@code lookAtY} and {@code lookAtZ}
	 */
	protected final float point3FDistance(final float eyeX, final float eyeY, final float eyeZ, final float lookAtX, final float lookAtY, final float lookAtZ) {
		return sqrt(point3FDistanceSquared(eyeX, eyeY, eyeZ, lookAtX, lookAtY, lookAtZ));
	}
	
	/**
	 * Returns the squared distance from the point represented by {@code eyeX}, {@code eyeY} and {@code eyeZ} to the point represented by {@code lookAtX}, {@code lookAtY} and {@code lookAtZ}.
	 * 
	 * @param eyeX the value of the X-component for the eye point
	 * @param eyeY the value of the Y-component for the eye point
	 * @param eyeZ the value of the Z-component for the eye point
	 * @param lookAtX the value of the X-component for the look at point
	 * @param lookAtY the value of the Y-component for the look at point
	 * @param lookAtZ the value of the Z-component for the look at point
	 * @return the squared distance from the point represented by {@code eyeX}, {@code eyeY} and {@code eyeZ} to the point represented by {@code lookAtX}, {@code lookAtY} and {@code lookAtZ}
	 */
	protected final float point3FDistanceSquared(final float eyeX, final float eyeY, final float eyeZ, final float lookAtX, final float lookAtY, final float lookAtZ) {
		return vector3FLengthSquared(lookAtX - eyeX, lookAtY - eyeY, lookAtZ - eyeZ);
	}
	
	/**
	 * Returns the value of the X-component in {@link #point3FArray_$private$3}.
	 * 
	 * @return the value of the X-component in {@link #point3FArray_$private$3}
	 */
	protected final float point3FGetX() {
		return this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_X];
	}
	
	/**
	 * Returns the value of the Y-component in {@link #point3FArray_$private$3}.
	 * 
	 * @return the value of the Y-component in {@link #point3FArray_$private$3}
	 */
	protected final float point3FGetY() {
		return this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_Y];
	}
	
	/**
	 * Returns the value of the Z-component in {@link #point3FArray_$private$3}.
	 * 
	 * @return the value of the Z-component in {@link #point3FArray_$private$3}
	 */
	protected final float point3FGetZ() {
		return this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_Z];
	}
	
	/**
	 * Sets a point in {@link #point3FArray_$private$3}.
	 * <p>
	 * The point is constructed using the point represented by {@code x}, {@code y} and {@code z}.
	 * 
	 * @param x the value of the X-component of the point
	 * @param y the value of the Y-component of the point
	 * @param z the value of the Z-component of the point
	 */
	protected final void point3FSet(final float x, final float y, final float z) {
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_X] = x;
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_Y] = y;
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_Z] = z;
	}
	
	/**
	 * Sets a point in {@link #point3FArray_$private$3}.
	 * <p>
	 * The point is constructed by transforming the point represented by {@code x}, {@code y} and {@code z} with the supplied matrix.
	 * 
	 * @param element11 the value of the element at row 1 and column 1 in the matrix
	 * @param element12 the value of the element at row 1 and column 2 in the matrix
	 * @param element13 the value of the element at row 1 and column 3 in the matrix
	 * @param element14 the value of the element at row 1 and column 4 in the matrix
	 * @param element21 the value of the element at row 2 and column 1 in the matrix
	 * @param element22 the value of the element at row 2 and column 2 in the matrix
	 * @param element23 the value of the element at row 2 and column 3 in the matrix
	 * @param element24 the value of the element at row 2 and column 4 in the matrix
	 * @param element31 the value of the element at row 3 and column 1 in the matrix
	 * @param element32 the value of the element at row 3 and column 2 in the matrix
	 * @param element33 the value of the element at row 3 and column 3 in the matrix
	 * @param element34 the value of the element at row 3 and column 4 in the matrix
	 * @param x the value of the X-component of the point
	 * @param y the value of the Y-component of the point
	 * @param z the value of the Z-component of the point
	 */
	protected final void point3FSetMatrix44FTransform(final float element11, final float element12, final float element13, final float element14, final float element21, final float element22, final float element23, final float element24, final float element31, final float element32, final float element33, final float element34, final float x, final float y, final float z) {
		final float newX = element11 * x + element12 * y + element13 * z + element14;
		final float newY = element21 * x + element22 * y + element23 * z + element24;
		final float newZ = element31 * x + element32 * y + element33 * z + element34;
		
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_X] = newX;
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_Y] = newY;
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_Z] = newZ;
	}
	
	/**
	 * Sets a point in {@link #point3FArray_$private$3}.
	 * <p>
	 * The point is constructed by transforming the point represented by {@code x}, {@code y} and {@code z} with the supplied matrix.
	 * <p>
	 * If the value of component 4, which is produced by the transformation, is anything but {@code 0.0} or {@code 1.0}, the values of component 1, component 2 and component 3 will be divided by the value of component 4.
	 * 
	 * @param element11 the value of the element at row 1 and column 1 in the matrix
	 * @param element12 the value of the element at row 1 and column 2 in the matrix
	 * @param element13 the value of the element at row 1 and column 3 in the matrix
	 * @param element14 the value of the element at row 1 and column 4 in the matrix
	 * @param element21 the value of the element at row 2 and column 1 in the matrix
	 * @param element22 the value of the element at row 2 and column 2 in the matrix
	 * @param element23 the value of the element at row 2 and column 3 in the matrix
	 * @param element24 the value of the element at row 2 and column 4 in the matrix
	 * @param element31 the value of the element at row 3 and column 1 in the matrix
	 * @param element32 the value of the element at row 3 and column 2 in the matrix
	 * @param element33 the value of the element at row 3 and column 3 in the matrix
	 * @param element34 the value of the element at row 3 and column 4 in the matrix
	 * @param element41 the value of the element at row 4 and column 1 in the matrix
	 * @param element42 the value of the element at row 4 and column 2 in the matrix
	 * @param element43 the value of the element at row 4 and column 3 in the matrix
	 * @param element44 the value of the element at row 4 and column 4 in the matrix
	 * @param x the value of the X-component of the point
	 * @param y the value of the Y-component of the point
	 * @param z the value of the Z-component of the point
	 */
	protected final void point3FSetMatrix44FTransformAndDivide(final float element11, final float element12, final float element13, final float element14, final float element21, final float element22, final float element23, final float element24, final float element31, final float element32, final float element33, final float element34, final float element41, final float element42, final float element43, final float element44, final float x, final float y, final float z) {
		final float newX = element11 * x + element12 * y + element13 * z + element14;
		final float newY = element21 * x + element22 * y + element23 * z + element24;
		final float newZ = element31 * x + element32 * y + element33 * z + element34;
		final float newW = element41 * x + element42 * y + element43 * z + element44;
		
		final boolean isDividing = newW != +0.0F && newW != -0.0F && newW != 1.0F;
		
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_X] = isDividing ? newX / newW : newX;
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_Y] = isDividing ? newY / newW : newY;
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_Z] = isDividing ? newZ / newW : newZ;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Ray3F ///////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the value of the X-component of the vector called direction of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the value of the X-component of the vector called direction of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetDirectionX() {
		return this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
	}
	
	/**
	 * Returns the value of the Y-component of the vector called direction of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the value of the Y-component of the vector called direction of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetDirectionY() {
		return this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
	}
	
	/**
	 * Returns the value of the Z-component of the vector called direction of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the value of the Z-component of the vector called direction of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetDirectionZ() {
		return this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
	}
	
	/**
	 * Returns the reciprocal (or inverse) value of the X-component of the vector called direction of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the reciprocal (or inverse) value of the X-component of the vector called direction of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetDirectionReciprocalX() {
		return 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
	}
	
	/**
	 * Returns the reciprocal (or inverse) value of the Y-component of the vector called direction of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the reciprocal (or inverse) value of the Y-component of the vector called direction of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetDirectionReciprocalY() {
		return 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
	}
	
	/**
	 * Returns the reciprocal (or inverse) value of the Z-component of the vector called direction of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the reciprocal (or inverse) value of the Z-component of the vector called direction of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetDirectionReciprocalZ() {
		return 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
	}
	
	/**
	 * Returns the value of the X-component of the point called origin of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the value of the X-component of the point called origin of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetOriginX() {
		return this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
	}
	
	/**
	 * Returns the value of the Y-component of the point called origin of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the value of the Y-component of the point called origin of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetOriginY() {
		return this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
	}
	
	/**
	 * Returns the value of the Z-component of the point called origin of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the value of the Z-component of the point called origin of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetOriginZ() {
		return this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2];
	}
	
	/**
	 * Returns the maximum parametric distance of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the maximum parametric distance of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetTMaximum() {
		return this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM];
	}
	
	/**
	 * Returns the minimum parametric distance of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the minimum parametric distance of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetTMinimum() {
		return this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MINIMUM];
	}
	
	/**
	 * Sets the component values for the vector called direction of the ray in {@link ray3FArray_$private$8}.
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 * @param z the value of the Z-component
	 */
	protected final void ray3FSetDirection(final float x, final float y, final float z) {
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0] = x;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1] = y;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2] = z;
	}
	
	/**
	 * Sets a ray in {@link #ray3FArray_$private$8}.
	 * <p>
	 * The ray direction is constructed using a normalized representation of the current vector in {@link #vector3FArray_$private$3}.
	 */
	protected final void ray3FSetFromSurfaceIntersectionPointAndVector3FLHS() {
		final float surfaceIntersectionPointX = intersectionLHSGetSurfaceIntersectionPointX();
		final float surfaceIntersectionPointY = intersectionLHSGetSurfaceIntersectionPointY();
		final float surfaceIntersectionPointZ = intersectionLHSGetSurfaceIntersectionPointZ();
		
		final float directionX = vector3FGetX();
		final float directionY = vector3FGetY();
		final float directionZ = vector3FGetZ();
		final float directionLengthReciprocal = vector3FLengthReciprocal(directionX, directionY, directionZ);
		final float directionNormalizedX = directionX * directionLengthReciprocal;
		final float directionNormalizedY = directionY * directionLengthReciprocal;
		final float directionNormalizedZ = directionZ * directionLengthReciprocal;
		
		final float originX = surfaceIntersectionPointX;
		final float originY = surfaceIntersectionPointY;
		final float originZ = surfaceIntersectionPointZ;
		
		ray3FSetOrigin(originX, originY, originZ);
		ray3FSetDirection(directionNormalizedX, directionNormalizedY, directionNormalizedZ);
		ray3FSetTMinimum(DEFAULT_T_MINIMUM);
		ray3FSetTMaximum(DEFAULT_T_MAXIMUM);
	}
	
	/**
	 * Sets a ray in {@link #ray3FArray_$private$8}.
	 * <p>
	 * The ray direction is constructed using a normalized representation of the current vector in {@link #vector3FArray_$private$3}. The origin is constructed by offsetting the surface intersection point in {@link #intersectionRHSArray_$private$24}
	 * slightly, in the direction of the ray itself.
	 * 
	 * @param rayDirectionX the X-coordinate of the ray direction
	 * @param rayDirectionY the Y-coordinate of the ray direction
	 * @param rayDirectionZ the Z-coordinate of the ray direction
	 */
	protected final void ray3FSetFromSurfaceIntersectionPointAndVector3FRHS(final float rayDirectionX, final float rayDirectionY, final float rayDirectionZ) {
		final float surfaceIntersectionPointX = intersectionRHSGetSurfaceIntersectionPointX();
		final float surfaceIntersectionPointY = intersectionRHSGetSurfaceIntersectionPointY();
		final float surfaceIntersectionPointZ = intersectionRHSGetSurfaceIntersectionPointZ();
		
		final float directionX = vector3FGetX();
		final float directionY = vector3FGetY();
		final float directionZ = vector3FGetZ();
		final float directionLengthReciprocal = vector3FLengthReciprocal(directionX, directionY, directionZ);
		final float directionNormalizedX = directionX * directionLengthReciprocal;
		final float directionNormalizedY = directionY * directionLengthReciprocal;
		final float directionNormalizedZ = directionZ * directionLengthReciprocal;
		
		final float originX = surfaceIntersectionPointX;
		final float originY = surfaceIntersectionPointY;
		final float originZ = surfaceIntersectionPointZ;
		
		ray3FSetOrigin(originX, originY, originZ);
		ray3FSetDirection(directionNormalizedX, directionNormalizedY, directionNormalizedZ);
		ray3FSetTMinimum(DEFAULT_T_MINIMUM);
		ray3FSetTMaximum(DEFAULT_T_MAXIMUM);
	}
	
	/**
	 * Sets the component values for the point called origin of the ray in {@link ray3FArray_$private$8}.
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 * @param z the value of the Z-component
	 */
	protected final void ray3FSetOrigin(final float x, final float y, final float z) {
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0] = x;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1] = y;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2] = z;
	}
	
	/**
	 * Sets the maximum parametric distance of the ray in {@link ray3FArray_$private$8}.
	 * 
	 * @param tMaximum the maximum parametric distance
	 */
	protected final void ray3FSetTMaximum(final float tMaximum) {
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM] = tMaximum;
	}
	
	/**
	 * Sets the minimum parametric distance of the ray in {@link ray3FArray_$private$8}.
	 * 
	 * @param tMinimum the minimum parametric distance
	 */
	protected final void ray3FSetTMinimum(final float tMinimum) {
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MINIMUM] = tMinimum;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Vector3F ////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * Returns {@code true} if, and only if, the vector was set, {@code false} otherwise.
	 * <p>
	 * The vector is constructed as the refraction vector of the direction vector represented by {@code directionX}, {@code directionY} and {@code directionZ} with regards to the normal vector represented by {@code normalX}, {@code normalY}
	 * and {@code normalZ}.
	 * 
	 * @param directionX the X-component of the direction vector
	 * @param directionY the Y-component of the direction vector
	 * @param directionZ the Z-component of the direction vector
	 * @param normalX the X-component of the normal vector
	 * @param normalY the Y-component of the normal vector
	 * @param normalZ the Z-component of the normal vector
	 * @param eta the index of refraction (IOR)
	 * @return {@code true} if, and only if, the vector was set, {@code false} otherwise
	 */
	protected final boolean vector3FSetRefraction(final float directionX, final float directionY, final float directionZ, final float normalX, final float normalY, final float normalZ, final float eta) {
		final float cosThetaI = vector3FDotProduct(directionX, directionY, directionZ, normalX, normalY, normalZ);
		final float sinThetaISquared = max(0.0F, 1.0F - cosThetaI * cosThetaI);
		final float sinThetaTSquared = eta * eta * sinThetaISquared;
		final float cosThetaT = sqrt(1.0F - sinThetaTSquared);
		
		final boolean isTotalInternalReflection = sinThetaTSquared >= 1.0F;
		
		if(isTotalInternalReflection) {
			return false;
		}
		
		final float refractionDirectionX = -directionX * eta + normalX * (eta * cosThetaI - cosThetaT);
		final float refractionDirectionY = -directionY * eta + normalY * (eta * cosThetaI - cosThetaT);
		final float refractionDirectionZ = -directionZ * eta + normalZ * (eta * cosThetaI - cosThetaT);
		final float refractionDirectionLengthReciprocal = vector3FLengthReciprocal(refractionDirectionX, refractionDirectionY, refractionDirectionZ);
		final float refractionDirectionNormalizedX = refractionDirectionX * refractionDirectionLengthReciprocal;
		final float refractionDirectionNormalizedY = refractionDirectionY * refractionDirectionLengthReciprocal;
		final float refractionDirectionNormalizedZ = refractionDirectionZ * refractionDirectionLengthReciprocal;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_X] = refractionDirectionNormalizedX;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Y] = refractionDirectionNormalizedY;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Z] = refractionDirectionNormalizedZ;
		
		return true;
	}
	
	/**
	 * Returns the dot product between the vector represented by {@code vLHSX}, {@code vLHSY} and {@code vLHSZ} and the vector represented by {@code vRHSX}, {@code vRHSY} and {@code vRHSZ}.
	 * 
	 * @param vLHSX the value of the X-component for the vector on the left hand side
	 * @param vLHSY the value of the Y-component for the vector on the left hand side
	 * @param vLHSZ the value of the Z-component for the vector on the left hand side
	 * @param vRHSX the value of the X-component for the vector on the right hand side
	 * @param vRHSY the value of the Y-component for the vector on the right hand side
	 * @param vRHSZ the value of the Z-component for the vector on the right hand side
	 * @return the dot product between the vector represented by {@code vLHSX}, {@code vLHSY} and {@code vLHSZ} and the vector represented by {@code vRHSX}, {@code vRHSY} and {@code vRHSZ}
	 */
	@SuppressWarnings("static-method")
	protected final float vector3FDotProduct(final float vLHSX, final float vLHSY, final float vLHSZ, final float vRHSX, final float vRHSY, final float vRHSZ) {
		return vLHSX * vRHSX + vLHSY * vRHSY + vLHSZ * vRHSZ;
	}
	
	/**
	 * Returns the value of the X-component in {@link #vector3FArray_$private$3}.
	 * 
	 * @return the value of the X-component in {@link #vector3FArray_$private$3}
	 */
	protected final float vector3FGetX() {
		return this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_X];
	}
	
	/**
	 * Returns the value of the Y-component in {@link #vector3FArray_$private$3}.
	 * 
	 * @return the value of the Y-component in {@link #vector3FArray_$private$3}
	 */
	protected final float vector3FGetY() {
		return this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Y];
	}
	
	/**
	 * Returns the value of the Z-component in {@link #vector3FArray_$private$3}.
	 * 
	 * @return the value of the Z-component in {@link #vector3FArray_$private$3}
	 */
	protected final float vector3FGetZ() {
		return this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Z];
	}
	
	/**
	 * Returns the length of the vector represented by {@code x}, {@code y} and {@code z}.
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 * @param z the value of the Z-component
	 * @return the length of the vector represented by {@code x}, {@code y} and {@code z}
	 */
	protected final float vector3FLength(final float x, final float y, final float z) {
		return sqrt(vector3FLengthSquared(x, y, z));
	}
	
	/**
	 * Returns the reciprocal (or inverse) length of the vector represented by {@code x}, {@code y} and {@code z}.
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 * @param z the value of the Z-component
	 * @return the reciprocal (or inverse) length of the vector represented by {@code x}, {@code y} and {@code z}
	 */
	protected final float vector3FLengthReciprocal(final float x, final float y, final float z) {
		return rsqrt(vector3FLengthSquared(x, y, z));
	}
	
	/**
	 * Returns the squared length of the vector represented by {@code x}, {@code y} and {@code z}.
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 * @param z the value of the Z-component
	 * @return the squared length of the vector represented by {@code x}, {@code y} and {@code z}
	 */
	@SuppressWarnings("static-method")
	protected final float vector3FLengthSquared(final float x, final float y, final float z) {
		return x * x + y * y + z * z;
	}
	
	/**
	 * Returns the spherical phi angle.
	 * 
	 * @param x the value of the X-component for the vector
	 * @param y the value of the Y-component for the vector
	 * @return the spherical phi angle
	 */
	protected final float vector3FSphericalPhi(final float x, final float y) {
		return addIfLessThanThreshold(atan2(y, x), 0.0F, Floats.PI_MULTIPLIED_BY_2);
	}
	
	/**
	 * Returns the spherical theta angle.
	 * 
	 * @param z the value of the Z-component for the vector
	 * @return the spherical theta angle
	 */
	protected final float vector3FSphericalTheta(final float z) {
		return acos(saturateF(z, -1.0F, 1.0F));
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed using the vector represented by {@code x}, {@code y} and {@code z}.
	 * 
	 * @param x the value of the X-component of the vector
	 * @param y the value of the Y-component of the vector
	 * @param z the value of the Z-component of the vector
	 */
	protected final void vector3FSet(final float x, final float y, final float z) {
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_X] = x;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Y] = y;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Z] = z;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed to point in the direction of the spherical coordinates given by {@code sinTheta}, {@code cosTheta}, {@code phi} and three direction vectors.
	 * 
	 * @param sinTheta the sine of the angle theta
	 * @param cosTheta the cosine of the angle theta
	 * @param phi the angle phi
	 * @param uX the X-component of the U-direction
	 * @param uY the Y-component of the U-direction
	 * @param uZ the Z-component of the U-direction
	 * @param vX the X-component of the V-direction
	 * @param vY the Y-component of the V-direction
	 * @param vZ the Z-component of the V-direction
	 * @param wX the X-component of the W-direction
	 * @param wY the Y-component of the W-direction
	 * @param wZ the Z-component of the W-direction
	 */
	protected final void vector3FSetDirectionSpherical12(final float sinTheta, final float cosTheta, final float phi, final float uX, final float uY, final float uZ, final float vX, final float vY, final float vZ, final float wX, final float wY, final float wZ) {
		final float u = sinTheta * cos(phi);
		final float v = sinTheta * sin(phi);
		final float w = cosTheta;
		
		final float x = uX * u + vX * v + wX * w;
		final float y = uY * u + vY * v + wY * w;
		final float z = uZ * u + vZ * v + wZ * w;
		
		vector3FSet(x, y, z);
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed to point in the direction of the spherical coordinates {@code u} and {@code v}.
	 * 
	 * @param u the spherical U-coordinate
	 * @param v the spherical V-coordinate
	 */
	protected final void vector3FSetDirectionSpherical2(final float u, final float v) {
		vector3FSetDirectionSpherical3(sin(v * Floats.PI), cos(v * Floats.PI), u * Floats.PI_MULTIPLIED_BY_2);
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed to point in the direction of the spherical coordinates given by {@code sinTheta}, {@code cosTheta} and {@code phi}.
	 * 
	 * @param sinTheta the sine of the angle theta
	 * @param cosTheta the cosine of the angle theta
	 * @param phi the angle phi
	 */
	protected final void vector3FSetDirectionSpherical3(final float sinTheta, final float cosTheta, final float phi) {
		vector3FSet(sinTheta * cos(phi), sinTheta * sin(phi), cosTheta);
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by negating the Z-component of the vector represented by {@code vRHSX}, {@code vRHSY} and {@code vRHSZ} if, and only if, {@code vLHSZ} is less than {@code 0.0F}. Otherwise, its current value will be used.
	 * 
	 * @param vLHSX the value of the X-component of the vector on the left-hand side
	 * @param vLHSY the value of the Y-component of the vector on the left-hand side
	 * @param vLHSZ the value of the Z-component of the vector on the left-hand side
	 * @param vRHSX the value of the X-component of the vector on the right-hand side
	 * @param vRHSY the value of the Y-component of the vector on the right-hand side
	 * @param vRHSZ the value of the Z-component of the vector on the right-hand side
	 */
	protected final void vector3FSetFaceForwardRHSZ(final float vLHSX, final float vLHSY, final float vLHSZ, final float vRHSX, final float vRHSY, final float vRHSZ) {
		vector3FSet(+vRHSX, +vRHSY, vLHSZ < 0.0F ? -vRHSZ : +vRHSZ);
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by negating the Z-component of the vector represented by {@code vRHSX}, {@code vRHSY} and {@code vRHSZ} if, and only if, {@code vLHSZ} is greater than {@code 0.0F}. Otherwise, its current value will be used.
	 * 
	 * @param vLHSX the value of the X-component of the vector on the left-hand side
	 * @param vLHSY the value of the Y-component of the vector on the left-hand side
	 * @param vLHSZ the value of the Z-component of the vector on the left-hand side
	 * @param vRHSX the value of the X-component of the vector on the right-hand side
	 * @param vRHSY the value of the Y-component of the vector on the right-hand side
	 * @param vRHSZ the value of the Z-component of the vector on the right-hand side
	 */
	protected final void vector3FSetFaceForwardRHSZNegated(final float vLHSX, final float vLHSY, final float vLHSZ, final float vRHSX, final float vRHSY, final float vRHSZ) {
		vector3FSet(+vRHSX, +vRHSY, vLHSZ > 0.0F ? -vRHSZ : +vRHSZ);
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector represented by {@code x}, {@code y} and {@code z} with the supplied matrix.
	 * 
	 * @param element11 the value of the element at row 1 and column 1 in the matrix
	 * @param element12 the value of the element at row 1 and column 2 in the matrix
	 * @param element13 the value of the element at row 1 and column 3 in the matrix
	 * @param element21 the value of the element at row 2 and column 1 in the matrix
	 * @param element22 the value of the element at row 2 and column 2 in the matrix
	 * @param element23 the value of the element at row 2 and column 3 in the matrix
	 * @param element31 the value of the element at row 3 and column 1 in the matrix
	 * @param element32 the value of the element at row 3 and column 2 in the matrix
	 * @param element33 the value of the element at row 3 and column 3 in the matrix
	 * @param x the value of the X-component of the vector
	 * @param y the value of the Y-component of the vector
	 * @param z the value of the Z-component of the vector
	 */
	protected final void vector3FSetMatrix44FTransform(final float element11, final float element12, final float element13, final float element21, final float element22, final float element23, final float element31, final float element32, final float element33, final float x, final float y, final float z) {
		final float newX = element11 * x + element12 * y + element13 * z;
		final float newY = element21 * x + element22 * y + element23 * z;
		final float newZ = element31 * x + element32 * y + element33 * z;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_X] = newX;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Y] = newY;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Z] = newZ;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector represented by {@code x}, {@code y} and {@code z} with the supplied matrix and normalizing it.
	 * 
	 * @param element11 the value of the element at row 1 and column 1 in the matrix
	 * @param element12 the value of the element at row 1 and column 2 in the matrix
	 * @param element13 the value of the element at row 1 and column 3 in the matrix
	 * @param element21 the value of the element at row 2 and column 1 in the matrix
	 * @param element22 the value of the element at row 2 and column 2 in the matrix
	 * @param element23 the value of the element at row 2 and column 3 in the matrix
	 * @param element31 the value of the element at row 3 and column 1 in the matrix
	 * @param element32 the value of the element at row 3 and column 2 in the matrix
	 * @param element33 the value of the element at row 3 and column 3 in the matrix
	 * @param x the value of the X-component of the vector
	 * @param y the value of the Y-component of the vector
	 * @param z the value of the Z-component of the vector
	 */
	protected final void vector3FSetMatrix44FTransformNormalize(final float element11, final float element12, final float element13, final float element21, final float element22, final float element23, final float element31, final float element32, final float element33, final float x, final float y, final float z) {
		final float newX = element11 * x + element12 * y + element13 * z;
		final float newY = element21 * x + element22 * y + element23 * z;
		final float newZ = element31 * x + element32 * y + element33 * z;
		final float newLengthReciprocal = vector3FLengthReciprocal(newX, newY, newZ);
		final float newNormalizedX = newX * newLengthReciprocal;
		final float newNormalizedY = newY * newLengthReciprocal;
		final float newNormalizedZ = newZ * newLengthReciprocal;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_X] = newNormalizedX;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Y] = newNormalizedY;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Z] = newNormalizedZ;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector represented by {@code x}, {@code y} and {@code z} with the supplied matrix in transpose order.
	 * 
	 * @param element11 the value of the element at row 1 and column 1 in the matrix
	 * @param element12 the value of the element at row 1 and column 2 in the matrix
	 * @param element13 the value of the element at row 1 and column 3 in the matrix
	 * @param element21 the value of the element at row 2 and column 1 in the matrix
	 * @param element22 the value of the element at row 2 and column 2 in the matrix
	 * @param element23 the value of the element at row 2 and column 3 in the matrix
	 * @param element31 the value of the element at row 3 and column 1 in the matrix
	 * @param element32 the value of the element at row 3 and column 2 in the matrix
	 * @param element33 the value of the element at row 3 and column 3 in the matrix
	 * @param x the value of the X-component of the vector
	 * @param y the value of the Y-component of the vector
	 * @param z the value of the Z-component of the vector
	 */
	protected final void vector3FSetMatrix44FTransformTranspose(final float element11, final float element12, final float element13, final float element21, final float element22, final float element23, final float element31, final float element32, final float element33, final float x, final float y, final float z) {
		final float newX = element11 * x + element21 * y + element31 * z;
		final float newY = element12 * x + element22 * y + element32 * z;
		final float newZ = element13 * x + element23 * y + element33 * z;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_X] = newX;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Y] = newY;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Z] = newZ;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector represented by {@code x}, {@code y} and {@code z} with the supplied matrix in transpose order and normalizing it.
	 * 
	 * @param element11 the value of the element at row 1 and column 1 in the matrix
	 * @param element12 the value of the element at row 1 and column 2 in the matrix
	 * @param element13 the value of the element at row 1 and column 3 in the matrix
	 * @param element21 the value of the element at row 2 and column 1 in the matrix
	 * @param element22 the value of the element at row 2 and column 2 in the matrix
	 * @param element23 the value of the element at row 2 and column 3 in the matrix
	 * @param element31 the value of the element at row 3 and column 1 in the matrix
	 * @param element32 the value of the element at row 3 and column 2 in the matrix
	 * @param element33 the value of the element at row 3 and column 3 in the matrix
	 * @param x the value of the X-component of the vector
	 * @param y the value of the Y-component of the vector
	 * @param z the value of the Z-component of the vector
	 */
	protected final void vector3FSetMatrix44FTransformTransposeNormalize(final float element11, final float element12, final float element13, final float element21, final float element22, final float element23, final float element31, final float element32, final float element33, final float x, final float y, final float z) {
		final float newX = element11 * x + element21 * y + element31 * z;
		final float newY = element12 * x + element22 * y + element32 * z;
		final float newZ = element13 * x + element23 * y + element33 * z;
		final float newLengthReciprocal = vector3FLengthReciprocal(newX, newY, newZ);
		final float newNormalizedX = newX * newLengthReciprocal;
		final float newNormalizedY = newY * newLengthReciprocal;
		final float newNormalizedZ = newZ * newLengthReciprocal;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_X] = newNormalizedX;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Y] = newNormalizedY;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Z] = newNormalizedZ;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by normalizing the vector represented by {@code x}, {@code y} and {@code z}.
	 * 
	 * @param x the value of the X-component of the vector
	 * @param y the value of the Y-component of the vector
	 * @param z the value of the Z-component of the vector
	 */
	protected final void vector3FSetNormalize(final float x, final float y, final float z) {
		final float length = vector3FLength(x, y, z);
		
		final boolean isLengthGTEThreshold = length >= 0.99999982F;
		final boolean isLengthLTEThreshold = length <= 1.00000012F;
		
		if(isLengthGTEThreshold && isLengthLTEThreshold) {
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_X] = x;
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Y] = y;
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Z] = z;
		} else {
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_X] = x / length;
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Y] = y / length;
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Z] = z / length;
		}
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector represented by {@code x}, {@code y} and {@code z} with the orthonormal basis in {@link #orthonormalBasis33FArray_$private$9} and normalizing it.
	 * 
	 * @param x the value of the X-component of the vector
	 * @param y the value of the Y-component of the vector
	 * @param z the value of the Z-component of the vector
	 */
	protected final void vector3FSetOrthonormalBasis33FTransformNormalize(final float x, final float y, final float z) {
		final float uX = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0];
		final float uY = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1];
		final float uZ = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2];
		final float vX = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0];
		final float vY = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1];
		final float vZ = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2];
		final float wX = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0];
		final float wY = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1];
		final float wZ = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2];
		
		final float newX = uX * x + vX * y + wX * z;
		final float newY = uY * x + vY * y + wY * z;
		final float newZ = uZ * x + vZ * y + wZ * z;
		final float newLengthReciprocal = vector3FLengthReciprocal(newX, newY, newZ);
		final float newNormalizedX = newX * newLengthReciprocal;
		final float newNormalizedY = newY * newLengthReciprocal;
		final float newNormalizedZ = newZ * newLengthReciprocal;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_X] = newNormalizedX;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Y] = newNormalizedY;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Z] = newNormalizedZ;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector in {@code vector3FArray_$private$3} with the orthonormal basis in {@link #orthonormalBasis33FArray_$private$9} and normalizing it.
	 */
	protected final void vector3FSetOrthonormalBasis33FTransformNormalizeFromVector3F() {
		final float x = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_X];
		final float y = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Y];
		final float z = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Z];
		
		vector3FSetOrthonormalBasis33FTransformNormalize(x, y, z);
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector represented by {@code x}, {@code y} and {@code z} with the orthonormal basis in {@link #orthonormalBasis33FArray_$private$9} in reverse order and normalizing it.
	 * 
	 * @param x the value of the X-component of the vector
	 * @param y the value of the Y-component of the vector
	 * @param z the value of the Z-component of the vector
	 */
	protected final void vector3FSetOrthonormalBasis33FTransformReverseNormalize(final float x, final float y, final float z) {
		final float uX = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0];
		final float uY = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1];
		final float uZ = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2];
		final float vX = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0];
		final float vY = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1];
		final float vZ = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2];
		final float wX = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0];
		final float wY = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1];
		final float wZ = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2];
		
		final float newX = x * uX + y * uY + z * uZ;
		final float newY = x * vX + y * vY + z * vZ;
		final float newZ = x * wX + y * wY + z * wZ;
		final float newLengthReciprocal = vector3FLengthReciprocal(newX, newY, newZ);
		final float newNormalizedX = newX * newLengthReciprocal;
		final float newNormalizedY = newY * newLengthReciprocal;
		final float newNormalizedZ = newZ * newLengthReciprocal;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_X] = newNormalizedX;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Y] = newNormalizedY;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Z] = newNormalizedZ;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by sampling a direction on a hemisphere with a cosine distribution.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 */
	protected final void vector3FSetSampleHemisphereCosineDistribution(final float u, final float v) {
		point2FSetSampleDiskUniformDistributionByConcentricMapping(u, v, 1.0F);
		
		final float x = point2FGetX();
		final float y = point2FGetY();
		final float z = sqrt(max(0.0F, 1.0F - x * x - y * y));
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_X] = x;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Y] = y;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Z] = z;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by sampling a direction on a hemisphere with a uniform distribution.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 */
	protected final void vector3FSetSampleHemisphereUniformDistribution(final float u, final float v) {
		final float cosTheta = u;
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float phi = Floats.PI_MULTIPLIED_BY_2 * v;
		
		final float x = sinTheta * cos(phi);
		final float y = sinTheta * sin(phi);
		final float z = cosTheta;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_X] = x;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Y] = y;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Z] = z;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by sampling a direction on a sphere with a uniform distribution.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 */
	protected final void vector3FSetSampleSphereUniformDistribution(final float u, final float v) {
		final float cosTheta = 1.0F - 2.0F * u;
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float phi = Floats.PI_MULTIPLIED_BY_2 * v;
		
		final float x = sinTheta * cos(phi);
		final float y = sinTheta * sin(phi);
		final float z = cosTheta;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_X] = x;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Y] = y;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Z] = z;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed as the specular reflection vector of the direction vector represented by {@code directionX}, {@code directionY} and {@code directionZ} with regards to the normal vector represented by {@code normalX}, {@code normalY}
	 * and {@code normalZ}.
	 * <p>
	 * This method assumes that the direction vector is pointing away from the surface. This is usually the case for BRDFs and BTDFs.
	 * 
	 * @param directionX the X-component of the direction vector
	 * @param directionY the Y-component of the direction vector
	 * @param directionZ the Z-component of the direction vector
	 * @param normalX the X-component of the normal vector
	 * @param normalY the Y-component of the normal vector
	 * @param normalZ the Z-component of the normal vector
	 */
	protected final void vector3FSetSpecularReflection(final float directionX, final float directionY, final float directionZ, final float normalX, final float normalY, final float normalZ) {
		final float directionDotNormal = vector3FDotProduct(directionX, directionY, directionZ, normalX, normalY, normalZ);
		final float directionDotNormalMultipliedByTwo = directionDotNormal * 2.0F;
		
		final float reflectionX = normalX * directionDotNormalMultipliedByTwo - directionX;
		final float reflectionY = normalY * directionDotNormalMultipliedByTwo - directionY;
		final float reflectionZ = normalZ * directionDotNormalMultipliedByTwo - directionZ;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_X] = reflectionX;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Y] = reflectionY;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_Z] = reflectionZ;
	}
}