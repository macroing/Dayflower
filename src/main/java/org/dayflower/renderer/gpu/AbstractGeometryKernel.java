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

import static org.dayflower.utility.Floats.MAX_VALUE;
import static org.dayflower.utility.Floats.PI;
import static org.dayflower.utility.Floats.PI_DIVIDED_BY_2;
import static org.dayflower.utility.Floats.PI_DIVIDED_BY_4;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2;

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
	protected static final float DEFAULT_T_MAXIMUM = MAX_VALUE;
	
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
	private static final int POINT_2_F_ARRAY_OFFSET_COMPONENT_1 = 0;
	private static final int POINT_2_F_ARRAY_OFFSET_COMPONENT_2 = 1;
	private static final int POINT_2_F_ARRAY_SIZE = 2;
	private static final int POINT_3_F_ARRAY_OFFSET_COMPONENT_1 = 0;
	private static final int POINT_3_F_ARRAY_OFFSET_COMPONENT_2 = 1;
	private static final int POINT_3_F_ARRAY_OFFSET_COMPONENT_3 = 2;
	private static final int POINT_3_F_ARRAY_SIZE = 3;
	private static final int RAY_3_F_ARRAY_OFFSET_DIRECTION = 3;
	private static final int RAY_3_F_ARRAY_OFFSET_ORIGIN = 0;
	private static final int RAY_3_F_ARRAY_OFFSET_T_MAXIMUM = 7;
	private static final int RAY_3_F_ARRAY_OFFSET_T_MINIMUM = 6;
	private static final int RAY_3_F_ARRAY_SIZE = 8;
	private static final int VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1 = 0;
	private static final int VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2 = 1;
	private static final int VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3 = 2;
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
	 * Returns the value of component 1 of the U-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the U-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGUComponent1() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0];
	}
	
	/**
	 * Returns the value of component 2 of the U-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the U-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGUComponent2() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1];
	}
	
	/**
	 * Returns the value of component 3 of the U-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the U-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGUComponent3() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2];
	}
	
	/**
	 * Returns the value of component 1 of the V-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the V-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGVComponent1() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0];
	}
	
	/**
	 * Returns the value of component 2 of the V-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the V-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGVComponent2() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1];
	}
	
	/**
	 * Returns the value of component 3 of the V-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the V-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGVComponent3() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2];
	}
	
	/**
	 * Returns the value of component 1 of the W-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the W-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGWComponent1() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
	}
	
	/**
	 * Returns the value of component 2 of the W-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the W-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGWComponent2() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
	}
	
	/**
	 * Returns the value of component 3 of the W-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the W-direction of the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisGWComponent3() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
	}
	
	/**
	 * Returns the value of component 1 of the U-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the U-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSUComponent1() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0];
	}
	
	/**
	 * Returns the value of component 2 of the U-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the U-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSUComponent2() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1];
	}
	
	/**
	 * Returns the value of component 3 of the U-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the U-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSUComponent3() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2];
	}
	
	/**
	 * Returns the value of component 1 of the V-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the V-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSVComponent1() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0];
	}
	
	/**
	 * Returns the value of component 2 of the V-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the V-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSVComponent2() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1];
	}
	
	/**
	 * Returns the value of component 3 of the V-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the V-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSVComponent3() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2];
	}
	
	/**
	 * Returns the value of component 1 of the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSWComponent1() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
	}
	
	/**
	 * Returns the value of component 2 of the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSWComponent2() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
	}
	
	/**
	 * Returns the value of component 3 of the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetOrthonormalBasisSWComponent3() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
	}
	
	/**
	 * Returns the value of component 1 of the surface intersection point in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the surface intersection point in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetSurfaceIntersectionPointComponent1() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0];
	}
	
	/**
	 * Returns the value of component 2 of the surface intersection point in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the surface intersection point in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetSurfaceIntersectionPointComponent2() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1];
	}
	
	/**
	 * Returns the value of component 3 of the surface intersection point in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the surface intersection point in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetSurfaceIntersectionPointComponent3() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2];
	}
	
	/**
	 * Returns the value of component 1 of the texture coordinates in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the texture coordinates in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetTextureCoordinatesComponent1() {
		return this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0];
	}
	
	/**
	 * Returns the value of component 2 of the texture coordinates in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the texture coordinates in {@link #intersectionLHSArray_$private$24}
	 */
	protected final float intersectionLHSGetTextureCoordinatesComponent2() {
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
	 * Sets the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @param uComponent1 component 1 of the U-direction
	 * @param uComponent2 component 2 of the U-direction
	 * @param uComponent3 component 3 of the U-direction
	 * @param vComponent1 component 1 of the V-direction
	 * @param vComponent2 component 2 of the V-direction
	 * @param vComponent3 component 3 of the V-direction
	 * @param wComponent1 component 1 of the W-direction
	 * @param wComponent2 component 2 of the W-direction
	 * @param wComponent3 component 3 of the W-direction
	 */
	protected final void intersectionLHSSetOrthonormalBasisG(final float uComponent1, final float uComponent2, final float uComponent3, final float vComponent1, final float vComponent2, final float vComponent3, final float wComponent1, final float wComponent2, final float wComponent3) {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0] = uComponent1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1] = uComponent2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2] = uComponent3;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0] = vComponent1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1] = vComponent2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2] = vComponent3;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0] = wComponent1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1] = wComponent2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2] = wComponent3;
	}
	
	/**
	 * Sets the orthonormal basis for geometry in {@link #intersectionLHSArray_$private$24} from {@link #orthonormalBasis33FArray_$private$9}.
	 */
	protected final void intersectionLHSSetOrthonormalBasisGFromOrthonormalBasis33F() {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0] = orthonormalBasis33FGetUComponent1();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1] = orthonormalBasis33FGetUComponent2();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2] = orthonormalBasis33FGetUComponent3();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0] = orthonormalBasis33FGetVComponent1();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1] = orthonormalBasis33FGetVComponent2();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2] = orthonormalBasis33FGetVComponent3();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0] = orthonormalBasis33FGetWComponent1();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1] = orthonormalBasis33FGetWComponent2();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2] = orthonormalBasis33FGetWComponent3();
	}
	
	/**
	 * Sets the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @param uComponent1 component 1 of the U-direction
	 * @param uComponent2 component 2 of the U-direction
	 * @param uComponent3 component 3 of the U-direction
	 * @param vComponent1 component 1 of the V-direction
	 * @param vComponent2 component 2 of the V-direction
	 * @param vComponent3 component 3 of the V-direction
	 * @param wComponent1 component 1 of the W-direction
	 * @param wComponent2 component 2 of the W-direction
	 * @param wComponent3 component 3 of the W-direction
	 */
	protected final void intersectionLHSSetOrthonormalBasisS(final float uComponent1, final float uComponent2, final float uComponent3, final float vComponent1, final float vComponent2, final float vComponent3, final float wComponent1, final float wComponent2, final float wComponent3) {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = uComponent1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = uComponent2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = uComponent3;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = vComponent1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = vComponent2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = vComponent3;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = wComponent1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = wComponent2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = wComponent3;
	}
	
	/**
	 * Sets the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24} from {@link #orthonormalBasis33FArray_$private$9}.
	 */
	protected final void intersectionLHSSetOrthonormalBasisSFromOrthonormalBasis33F() {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = orthonormalBasis33FGetUComponent1();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = orthonormalBasis33FGetUComponent2();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = orthonormalBasis33FGetUComponent3();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = orthonormalBasis33FGetVComponent1();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = orthonormalBasis33FGetVComponent2();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = orthonormalBasis33FGetVComponent3();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = orthonormalBasis33FGetWComponent1();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = orthonormalBasis33FGetWComponent2();
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = orthonormalBasis33FGetWComponent3();
	}
	
	/**
	 * Sets the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @param component1 component 1 of the W-direction
	 * @param component2 component 2 of the W-direction
	 * @param component3 component 3 of the W-direction
	 */
	protected final void intersectionLHSSetOrthonormalBasisSW(final float component1, final float component2, final float component3) {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = component1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = component2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = component3;
	}
	
	/**
	 * Sets the W-direction of the orthonormal basis for shading in {@link #intersectionLHSArray_$private$24} to a transformed representation.
	 * 
	 * @param component1 component 1 of the vector to transform with
	 * @param component2 component 2 of the vector to transform with
	 * @param component3 component 3 of the vector to transform with
	 */
	protected final void intersectionLHSSetOrthonormalBasisSWTransform(final float component1, final float component2, final float component3) {
		final float oldUComponent1 = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0];
		final float oldUComponent2 = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1];
		final float oldUComponent3 = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2];
		final float oldVComponent1 = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0];
		final float oldVComponent2 = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1];
		final float oldVComponent3 = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2];
		final float oldWComponent1 = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
		final float oldWComponent2 = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
		final float oldWComponent3 = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
		
		final float newWComponent1 = component1 * oldUComponent1 + component2 * oldVComponent1 + component3 * oldWComponent1;
		final float newWComponent2 = component1 * oldUComponent2 + component2 * oldVComponent2 + component3 * oldWComponent2;
		final float newWComponent3 = component1 * oldUComponent3 + component2 * oldVComponent3 + component3 * oldWComponent3;
		final float newWLengthReciprocal = vector3FLengthReciprocal(newWComponent1, newWComponent2, newWComponent3);
		final float newWNormalizedComponent1 = newWComponent1 * newWLengthReciprocal;
		final float newWNormalizedComponent2 = newWComponent2 * newWLengthReciprocal;
		final float newWNormalizedComponent3 = newWComponent3 * newWLengthReciprocal;
		
		final float newUComponent1 = oldVComponent2 * newWNormalizedComponent3 - oldVComponent3 * newWNormalizedComponent2;
		final float newUComponent2 = oldVComponent3 * newWNormalizedComponent1 - oldVComponent1 * newWNormalizedComponent3;
		final float newUComponent3 = oldVComponent1 * newWNormalizedComponent2 - oldVComponent2 * newWNormalizedComponent1;
		final float newULengthReciprocal = vector3FLengthReciprocal(newUComponent1, newUComponent2, newUComponent3);
		final float newUNormalizedComponent1 = newUComponent1 * newULengthReciprocal;
		final float newUNormalizedComponent2 = newUComponent2 * newULengthReciprocal;
		final float newUNormalizedComponent3 = newUComponent3 * newULengthReciprocal;
		
		final float newVNormalizedComponent1 = newWNormalizedComponent2 * newUNormalizedComponent3 - newWNormalizedComponent3 * newUNormalizedComponent2;
		final float newVNormalizedComponent2 = newWNormalizedComponent3 * newUNormalizedComponent1 - newWNormalizedComponent1 * newUNormalizedComponent3;
		final float newVNormalizedComponent3 = newWNormalizedComponent1 * newUNormalizedComponent2 - newWNormalizedComponent2 * newUNormalizedComponent1;
		
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = newUNormalizedComponent1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = newUNormalizedComponent2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = newUNormalizedComponent3;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = newVNormalizedComponent1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = newVNormalizedComponent2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = newVNormalizedComponent3;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = newWNormalizedComponent1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = newWNormalizedComponent2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = newWNormalizedComponent3;
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
	 * @param component1 component 1 of the surface intersection point
	 * @param component2 component 2 of the surface intersection point
	 * @param component3 component 3 of the surface intersection point
	 */
	protected final void intersectionLHSSetSurfaceIntersectionPoint(final float component1, final float component2, final float component3) {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0] = component1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1] = component2;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2] = component3;
	}
	
	/**
	 * Sets the texture coordinates in {@link #intersectionLHSArray_$private$24}.
	 * 
	 * @param component1 component 1 of the texture coordinates
	 * @param component2 component 2 of the texture coordinates
	 */
	protected final void intersectionLHSSetTextureCoordinates(final float component1, final float component2) {
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0] = component1;
		this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1] = component2;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Intersection - RHS //////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the value of component 1 of the U-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the U-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGUComponent1() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0];
	}
	
	/**
	 * Returns the value of component 2 of the U-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the U-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGUComponent2() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1];
	}
	
	/**
	 * Returns the value of component 3 of the U-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the U-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGUComponent3() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2];
	}
	
	/**
	 * Returns the value of component 1 of the V-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the V-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGVComponent1() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0];
	}
	
	/**
	 * Returns the value of component 2 of the V-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the V-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGVComponent2() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1];
	}
	
	/**
	 * Returns the value of component 3 of the V-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the V-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGVComponent3() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2];
	}
	
	/**
	 * Returns the value of component 1 of the W-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the W-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGWComponent1() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
	}
	
	/**
	 * Returns the value of component 2 of the W-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the W-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGWComponent2() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
	}
	
	/**
	 * Returns the value of component 3 of the W-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the W-direction of the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisGWComponent3() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
	}
	
	/**
	 * Returns the value of component 1 of the U-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the U-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSUComponent1() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0];
	}
	
	/**
	 * Returns the value of component 2 of the U-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the U-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSUComponent2() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1];
	}
	
	/**
	 * Returns the value of component 3 of the U-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the U-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSUComponent3() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2];
	}
	
	/**
	 * Returns the value of component 1 of the V-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the V-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSVComponent1() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0];
	}
	
	/**
	 * Returns the value of component 2 of the V-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the V-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSVComponent2() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1];
	}
	
	/**
	 * Returns the value of component 3 of the V-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the V-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSVComponent3() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2];
	}
	
	/**
	 * Returns the value of component 1 of the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSWComponent1() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
	}
	
	/**
	 * Returns the value of component 2 of the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSWComponent2() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
	}
	
	/**
	 * Returns the value of component 3 of the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetOrthonormalBasisSWComponent3() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
	}
	
	/**
	 * Returns the value of component 1 of the surface intersection point in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the surface intersection point in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetSurfaceIntersectionPointComponent1() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0];
	}
	
	/**
	 * Returns the value of component 2 of the surface intersection point in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the surface intersection point in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetSurfaceIntersectionPointComponent2() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1];
	}
	
	/**
	 * Returns the value of component 3 of the surface intersection point in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 3 of the surface intersection point in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetSurfaceIntersectionPointComponent3() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2];
	}
	
	/**
	 * Returns the value of component 1 of the texture coordinates in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 1 of the texture coordinates in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetTextureCoordinatesComponent1() {
		return this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0];
	}
	
	/**
	 * Returns the value of component 2 of the texture coordinates in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @return the value of component 2 of the texture coordinates in {@link #intersectionRHSArray_$private$24}
	 */
	protected final float intersectionRHSGetTextureCoordinatesComponent2() {
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
	 * Sets the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @param uComponent1 component 1 of the U-direction
	 * @param uComponent2 component 2 of the U-direction
	 * @param uComponent3 component 3 of the U-direction
	 * @param vComponent1 component 1 of the V-direction
	 * @param vComponent2 component 2 of the V-direction
	 * @param vComponent3 component 3 of the V-direction
	 * @param wComponent1 component 1 of the W-direction
	 * @param wComponent2 component 2 of the W-direction
	 * @param wComponent3 component 3 of the W-direction
	 */
	protected final void intersectionRHSSetOrthonormalBasisG(final float uComponent1, final float uComponent2, final float uComponent3, final float vComponent1, final float vComponent2, final float vComponent3, final float wComponent1, final float wComponent2, final float wComponent3) {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0] = uComponent1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1] = uComponent2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2] = uComponent3;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0] = vComponent1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1] = vComponent2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2] = vComponent3;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0] = wComponent1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1] = wComponent2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2] = wComponent3;
	}
	
	/**
	 * Sets the orthonormal basis for geometry in {@link #intersectionRHSArray_$private$24} from {@link #orthonormalBasis33FArray_$private$9}.
	 */
	protected final void intersectionRHSSetOrthonormalBasisGFromOrthonormalBasis33F() {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0] = orthonormalBasis33FGetUComponent1();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1] = orthonormalBasis33FGetUComponent2();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2] = orthonormalBasis33FGetUComponent3();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0] = orthonormalBasis33FGetVComponent1();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1] = orthonormalBasis33FGetVComponent2();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2] = orthonormalBasis33FGetVComponent3();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0] = orthonormalBasis33FGetWComponent1();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1] = orthonormalBasis33FGetWComponent2();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2] = orthonormalBasis33FGetWComponent3();
	}
	
	/**
	 * Sets the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @param uComponent1 component 1 of the U-direction
	 * @param uComponent2 component 2 of the U-direction
	 * @param uComponent3 component 3 of the U-direction
	 * @param vComponent1 component 1 of the V-direction
	 * @param vComponent2 component 2 of the V-direction
	 * @param vComponent3 component 3 of the V-direction
	 * @param wComponent1 component 1 of the W-direction
	 * @param wComponent2 component 2 of the W-direction
	 * @param wComponent3 component 3 of the W-direction
	 */
	protected final void intersectionRHSSetOrthonormalBasisS(final float uComponent1, final float uComponent2, final float uComponent3, final float vComponent1, final float vComponent2, final float vComponent3, final float wComponent1, final float wComponent2, final float wComponent3) {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = uComponent1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = uComponent2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = uComponent3;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = vComponent1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = vComponent2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = vComponent3;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = wComponent1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = wComponent2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = wComponent3;
	}
	
	/**
	 * Sets the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24} from {@link #orthonormalBasis33FArray_$private$9}.
	 */
	protected final void intersectionRHSSetOrthonormalBasisSFromOrthonormalBasis33F() {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = orthonormalBasis33FGetUComponent1();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = orthonormalBasis33FGetUComponent2();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = orthonormalBasis33FGetUComponent3();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = orthonormalBasis33FGetVComponent1();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = orthonormalBasis33FGetVComponent2();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = orthonormalBasis33FGetVComponent3();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = orthonormalBasis33FGetWComponent1();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = orthonormalBasis33FGetWComponent2();
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = orthonormalBasis33FGetWComponent3();
	}
	
	/**
	 * Sets the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @param component1 component 1 of the W-direction
	 * @param component2 component 2 of the W-direction
	 * @param component3 component 3 of the W-direction
	 */
	protected final void intersectionRHSSetOrthonormalBasisSW(final float component1, final float component2, final float component3) {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = component1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = component2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = component3;
	}
	
	/**
	 * Sets the W-direction of the orthonormal basis for shading in {@link #intersectionRHSArray_$private$24} to a transformed representation.
	 * 
	 * @param component1 component 1 of the vector to transform with
	 * @param component2 component 2 of the vector to transform with
	 * @param component3 component 3 of the vector to transform with
	 */
	protected final void intersectionRHSSetOrthonormalBasisSWTransform(final float component1, final float component2, final float component3) {
		final float oldUComponent1 = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0];
		final float oldUComponent2 = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1];
		final float oldUComponent3 = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2];
		final float oldVComponent1 = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0];
		final float oldVComponent2 = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1];
		final float oldVComponent3 = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2];
		final float oldWComponent1 = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
		final float oldWComponent2 = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
		final float oldWComponent3 = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
		
		final float newWComponent1 = component1 * oldUComponent1 + component2 * oldVComponent1 + component3 * oldWComponent1;
		final float newWComponent2 = component1 * oldUComponent2 + component2 * oldVComponent2 + component3 * oldWComponent2;
		final float newWComponent3 = component1 * oldUComponent3 + component2 * oldVComponent3 + component3 * oldWComponent3;
		final float newWLengthReciprocal = vector3FLengthReciprocal(newWComponent1, newWComponent2, newWComponent3);
		final float newWNormalizedComponent1 = newWComponent1 * newWLengthReciprocal;
		final float newWNormalizedComponent2 = newWComponent2 * newWLengthReciprocal;
		final float newWNormalizedComponent3 = newWComponent3 * newWLengthReciprocal;
		
		final float newUComponent1 = oldVComponent2 * newWNormalizedComponent3 - oldVComponent3 * newWNormalizedComponent2;
		final float newUComponent2 = oldVComponent3 * newWNormalizedComponent1 - oldVComponent1 * newWNormalizedComponent3;
		final float newUComponent3 = oldVComponent1 * newWNormalizedComponent2 - oldVComponent2 * newWNormalizedComponent1;
		final float newULengthReciprocal = vector3FLengthReciprocal(newUComponent1, newUComponent2, newUComponent3);
		final float newUNormalizedComponent1 = newUComponent1 * newULengthReciprocal;
		final float newUNormalizedComponent2 = newUComponent2 * newULengthReciprocal;
		final float newUNormalizedComponent3 = newUComponent3 * newULengthReciprocal;
		
		final float newVNormalizedComponent1 = newWNormalizedComponent2 * newUNormalizedComponent3 - newWNormalizedComponent3 * newUNormalizedComponent2;
		final float newVNormalizedComponent2 = newWNormalizedComponent3 * newUNormalizedComponent1 - newWNormalizedComponent1 * newUNormalizedComponent3;
		final float newVNormalizedComponent3 = newWNormalizedComponent1 * newUNormalizedComponent2 - newWNormalizedComponent2 * newUNormalizedComponent1;
		
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0] = newUNormalizedComponent1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1] = newUNormalizedComponent2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2] = newUNormalizedComponent3;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0] = newVNormalizedComponent1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1] = newVNormalizedComponent2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2] = newVNormalizedComponent3;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0] = newWNormalizedComponent1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1] = newWNormalizedComponent2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2] = newWNormalizedComponent3;
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
	 * @param component1 component 1 of the surface intersection point
	 * @param component2 component 2 of the surface intersection point
	 * @param component3 component 3 of the surface intersection point
	 */
	protected final void intersectionRHSSetSurfaceIntersectionPoint(final float component1, final float component2, final float component3) {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0] = component1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1] = component2;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2] = component3;
	}
	
	/**
	 * Sets the texture coordinates in {@link #intersectionRHSArray_$private$24}.
	 * 
	 * @param component1 component 1 of the texture coordinates
	 * @param component2 component 2 of the texture coordinates
	 */
	protected final void intersectionRHSSetTextureCoordinates(final float component1, final float component2) {
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 0] = component1;
		this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_TEXTURE_COORDINATES + 1] = component2;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// OrthonormalBasis33F /////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the value of component 1 of the U-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of component 1 of the U-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetUComponent1() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0];
	}
	
	/**
	 * Returns the value of component 2 of the U-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of component 2 of the U-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetUComponent2() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1];
	}
	
	/**
	 * Returns the value of component 3 of the U-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of component 3 of the U-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetUComponent3() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2];
	}
	
	/**
	 * Returns the value of component 1 of the V-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of component 1 of the V-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetVComponent1() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0];
	}
	
	/**
	 * Returns the value of component 2 of the V-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of component 2 of the V-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetVComponent2() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1];
	}
	
	/**
	 * Returns the value of component 3 of the V-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of component 3 of the V-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetVComponent3() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2];
	}
	
	/**
	 * Returns the value of component 1 of the W-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of component 1 of the W-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetWComponent1() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0];
	}
	
	/**
	 * Returns the value of component 2 of the W-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of component 2 of the W-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetWComponent2() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1];
	}
	
	/**
	 * Returns the value of component 3 of the W-direction in {@link #orthonormalBasis33FArray_$private$9}.
	 * 
	 * @return the value of component 3 of the W-direction in {@link #orthonormalBasis33FArray_$private$9}
	 */
	protected final float orthonormalBasis33FGetWComponent3() {
		return this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2];
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the U-direction vector represented by {@code orthonormalBasisUX}, {@code orthonormalBasisUY} and {@code orthonormalBasisUZ} and the V-direction vector represented by {@code orthonormalBasisVX},
	 * {@code orthonormalBasisVY} and {@code orthonormalBasisVZ}.
	 * <p>
	 * This method will normalize the U-direction and V-direction vectors.
	 * 
	 * @param orthonormalBasisUX the X-component of the U-direction
	 * @param orthonormalBasisUY the Y-component of the U-direction
	 * @param orthonormalBasisUZ the Z-component of the U-direction
	 * @param orthonormalBasisVX the X-component of the V-direction
	 * @param orthonormalBasisVY the Y-component of the V-direction
	 * @param orthonormalBasisVZ the Z-component of the V-direction
	 */
	protected final void orthonormalBasis33FSetFromUV(final float orthonormalBasisUX, final float orthonormalBasisUY, final float orthonormalBasisUZ, final float orthonormalBasisVX, final float orthonormalBasisVY, final float orthonormalBasisVZ) {
//		Compute the normalized U-direction of the orthonormal basis:
		final float orthonormalBasisULengthReciprocal = vector3FLengthReciprocal(orthonormalBasisUX, orthonormalBasisUY, orthonormalBasisUZ);
		final float orthonormalBasisUNormalizedX = orthonormalBasisUX * orthonormalBasisULengthReciprocal;
		final float orthonormalBasisUNormalizedY = orthonormalBasisUY * orthonormalBasisULengthReciprocal;
		final float orthonormalBasisUNormalizedZ = orthonormalBasisUZ * orthonormalBasisULengthReciprocal;
		
//		Compute the normalized V-direction of the orthonormal basis:
		final float orthonormalBasisVLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisVX, orthonormalBasisVY, orthonormalBasisVZ);
		final float orthonormalBasisVNormalizedX = orthonormalBasisVX * orthonormalBasisVLengthReciprocal;
		final float orthonormalBasisVNormalizedY = orthonormalBasisVY * orthonormalBasisVLengthReciprocal;
		final float orthonormalBasisVNormalizedZ = orthonormalBasisVZ * orthonormalBasisVLengthReciprocal;
		
//		Compute the normalized W-direction of the orthonormal basis:
		final float orthonormalBasisWNormalizedX = orthonormalBasisUNormalizedY * orthonormalBasisVNormalizedZ - orthonormalBasisUNormalizedZ * orthonormalBasisVNormalizedY;
		final float orthonormalBasisWNormalizedY = orthonormalBasisUNormalizedZ * orthonormalBasisVNormalizedX - orthonormalBasisUNormalizedX * orthonormalBasisVNormalizedZ;
		final float orthonormalBasisWNormalizedZ = orthonormalBasisUNormalizedX * orthonormalBasisVNormalizedY - orthonormalBasisUNormalizedY * orthonormalBasisVNormalizedX;
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = orthonormalBasisUNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = orthonormalBasisUNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = orthonormalBasisUNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = orthonormalBasisVNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = orthonormalBasisVNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = orthonormalBasisVNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = orthonormalBasisWNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = orthonormalBasisWNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = orthonormalBasisWNormalizedZ;
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the W-direction vector represented by {@code orthonormalBasisWX}, {@code orthonormalBasisWY} and {@code orthonormalBasisWZ}.
	 * <p>
	 * This method will normalize the W-direction vector.
	 * 
	 * @param orthonormalBasisWX the X-component of the W-direction
	 * @param orthonormalBasisWY the Y-component of the W-direction
	 * @param orthonormalBasisWZ the Z-component of the W-direction
	 */
	protected final void orthonormalBasis33FSetFromW(final float orthonormalBasisWX, final float orthonormalBasisWY, final float orthonormalBasisWZ) {
//		Compute the normalized W-direction of the orthonormal basis:
		final float orthonormalBasisWLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisWX, orthonormalBasisWY, orthonormalBasisWZ);
		final float orthonormalBasisWNormalizedX = orthonormalBasisWX * orthonormalBasisWLengthReciprocal;
		final float orthonormalBasisWNormalizedY = orthonormalBasisWY * orthonormalBasisWLengthReciprocal;
		final float orthonormalBasisWNormalizedZ = orthonormalBasisWZ * orthonormalBasisWLengthReciprocal;
		
//		Compute the absolute component values of the normalized W-direction, which are used to determine the orientation of the V-direction of the orthonormal basis:
		final float orthonormalBasisWNormalizedXAbs = abs(orthonormalBasisWNormalizedX);
		final float orthonormalBasisWNormalizedYAbs = abs(orthonormalBasisWNormalizedY);
		final float orthonormalBasisWNormalizedZAbs = abs(orthonormalBasisWNormalizedZ);
		
//		Compute variables used to determine the orientation of the V-direction of the orthonormal basis:
		final boolean isX = orthonormalBasisWNormalizedXAbs < orthonormalBasisWNormalizedYAbs && orthonormalBasisWNormalizedXAbs < orthonormalBasisWNormalizedZAbs;
		final boolean isY = orthonormalBasisWNormalizedYAbs < orthonormalBasisWNormalizedZAbs;
		
//		Compute the normalized V-direction of the orthonormal basis:
		final float orthonormalBasisVX = isX ? +0.0F                         : isY ? +orthonormalBasisWNormalizedZ : +orthonormalBasisWNormalizedY;
		final float orthonormalBasisVY = isX ? +orthonormalBasisWNormalizedZ : isY ? +0.0F                         : -orthonormalBasisWNormalizedX;
		final float orthonormalBasisVZ = isX ? -orthonormalBasisWNormalizedY : isY ? -orthonormalBasisWNormalizedX : +0.0F;
		final float orthonormalBasisVLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisVX, orthonormalBasisVY, orthonormalBasisVZ);
		final float orthonormalBasisVNormalizedX = orthonormalBasisVX * orthonormalBasisVLengthReciprocal;
		final float orthonormalBasisVNormalizedY = orthonormalBasisVY * orthonormalBasisVLengthReciprocal;
		final float orthonormalBasisVNormalizedZ = orthonormalBasisVZ * orthonormalBasisVLengthReciprocal;
		
//		Compute the normalized U-direction of the orthonormal basis:
		final float orthonormalBasisUNormalizedX = orthonormalBasisVNormalizedY * orthonormalBasisWNormalizedZ - orthonormalBasisVNormalizedZ * orthonormalBasisWNormalizedY;
		final float orthonormalBasisUNormalizedY = orthonormalBasisVNormalizedZ * orthonormalBasisWNormalizedX - orthonormalBasisVNormalizedX * orthonormalBasisWNormalizedZ;
		final float orthonormalBasisUNormalizedZ = orthonormalBasisVNormalizedX * orthonormalBasisWNormalizedY - orthonormalBasisVNormalizedY * orthonormalBasisWNormalizedX;
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = orthonormalBasisUNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = orthonormalBasisUNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = orthonormalBasisUNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = orthonormalBasisVNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = orthonormalBasisVNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = orthonormalBasisVNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = orthonormalBasisWNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = orthonormalBasisWNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = orthonormalBasisWNormalizedZ;
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the W-direction vector represented by {@code orthonormalBasisWX}, {@code orthonormalBasisWY} and {@code orthonormalBasisWZ} and the reference V-direction vector represented by
	 * {@code orthonormalBasisVReferenceX}, {@code orthonormalBasisVReferenceY} and {@code orthonormalBasisVReferenceZ}.
	 * <p>
	 * This method will normalize the W-direction and reference V-direction vectors.
	 * 
	 * @param orthonormalBasisWX the X-component of the W-direction
	 * @param orthonormalBasisWY the Y-component of the W-direction
	 * @param orthonormalBasisWZ the Z-component of the W-direction
	 * @param orthonormalBasisVReferenceX the X-component of the reference V-direction
	 * @param orthonormalBasisVReferenceY the Y-component of the reference V-direction
	 * @param orthonormalBasisVReferenceZ the Z-component of the reference V-direction
	 */
	protected final void orthonormalBasis33FSetFromWV(final float orthonormalBasisWX, final float orthonormalBasisWY, final float orthonormalBasisWZ, final float orthonormalBasisVReferenceX, final float orthonormalBasisVReferenceY, final float orthonormalBasisVReferenceZ) {
//		Compute the normalized W-direction of the orthonormal basis:
		final float orthonormalBasisWLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisWX, orthonormalBasisWY, orthonormalBasisWZ);
		final float orthonormalBasisWNormalizedX = orthonormalBasisWX * orthonormalBasisWLengthReciprocal;
		final float orthonormalBasisWNormalizedY = orthonormalBasisWY * orthonormalBasisWLengthReciprocal;
		final float orthonormalBasisWNormalizedZ = orthonormalBasisWZ * orthonormalBasisWLengthReciprocal;
		
//		Compute the normalized V-direction used as a reference for the orthonormal basis:
		final float orthonormalBasisVReferenceLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisVReferenceX, orthonormalBasisVReferenceY, orthonormalBasisVReferenceZ);
		final float orthonormalBasisVReferenceNormalizedX = orthonormalBasisVReferenceX * orthonormalBasisVReferenceLengthReciprocal;
		final float orthonormalBasisVReferenceNormalizedY = orthonormalBasisVReferenceY * orthonormalBasisVReferenceLengthReciprocal;
		final float orthonormalBasisVReferenceNormalizedZ = orthonormalBasisVReferenceZ * orthonormalBasisVReferenceLengthReciprocal;
		
//		Compute the normalized U-direction of the orthonormal basis:
		final float orthonormalBasisUX = orthonormalBasisVReferenceNormalizedY * orthonormalBasisWNormalizedZ - orthonormalBasisVReferenceNormalizedZ * orthonormalBasisWNormalizedY;
		final float orthonormalBasisUY = orthonormalBasisVReferenceNormalizedZ * orthonormalBasisWNormalizedX - orthonormalBasisVReferenceNormalizedX * orthonormalBasisWNormalizedZ;
		final float orthonormalBasisUZ = orthonormalBasisVReferenceNormalizedX * orthonormalBasisWNormalizedY - orthonormalBasisVReferenceNormalizedY * orthonormalBasisWNormalizedX;
		final float orthonormalBasisULengthReciprocal = vector3FLengthReciprocal(orthonormalBasisUX, orthonormalBasisUY, orthonormalBasisUZ);
		final float orthonormalBasisUNormalizedX = orthonormalBasisUX * orthonormalBasisULengthReciprocal;
		final float orthonormalBasisUNormalizedY = orthonormalBasisUY * orthonormalBasisULengthReciprocal;
		final float orthonormalBasisUNormalizedZ = orthonormalBasisUZ * orthonormalBasisULengthReciprocal;
		
//		Compute the normalized V-direction of the orthonormal basis:
		final float orthonormalBasisVNormalizedX = orthonormalBasisWNormalizedY * orthonormalBasisUNormalizedZ - orthonormalBasisWNormalizedZ * orthonormalBasisUNormalizedY;
		final float orthonormalBasisVNormalizedY = orthonormalBasisWNormalizedZ * orthonormalBasisUNormalizedX - orthonormalBasisWNormalizedX * orthonormalBasisUNormalizedZ;
		final float orthonormalBasisVNormalizedZ = orthonormalBasisWNormalizedX * orthonormalBasisUNormalizedY - orthonormalBasisWNormalizedY * orthonormalBasisUNormalizedX;
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = orthonormalBasisUNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = orthonormalBasisUNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = orthonormalBasisUNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = orthonormalBasisVNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = orthonormalBasisVNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = orthonormalBasisVNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = orthonormalBasisWNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = orthonormalBasisWNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = orthonormalBasisWNormalizedZ;
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the orthonormal basis in {@link #intersectionLHSArray_$private$24} that is used for geometry.
	 */
	protected final void orthonormalBasis33FSetIntersectionOrthonormalBasisGLHS() {
//		Get the orthonormal basis:
		final float orthonormalBasisGUX = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0];
		final float orthonormalBasisGUY = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1];
		final float orthonormalBasisGUZ = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2];
		final float orthonormalBasisGVX = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0];
		final float orthonormalBasisGVY = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1];
		final float orthonormalBasisGVZ = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2];
		final float orthonormalBasisGWX = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
		final float orthonormalBasisGWY = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
		final float orthonormalBasisGWZ = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = orthonormalBasisGUX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = orthonormalBasisGUY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = orthonormalBasisGUZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = orthonormalBasisGVX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = orthonormalBasisGVY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = orthonormalBasisGVZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = orthonormalBasisGWX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = orthonormalBasisGWY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = orthonormalBasisGWZ;
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the orthonormal basis in {@link #intersectionRHSArray_$private$24} that is used for geometry.
	 */
	protected final void orthonormalBasis33FSetIntersectionOrthonormalBasisGRHS() {
//		Get the orthonormal basis:
		final float orthonormalBasisGUX = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 0];
		final float orthonormalBasisGUY = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 1];
		final float orthonormalBasisGUZ = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_U + 2];
		final float orthonormalBasisGVX = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 0];
		final float orthonormalBasisGVY = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 1];
		final float orthonormalBasisGVZ = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_V + 2];
		final float orthonormalBasisGWX = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
		final float orthonormalBasisGWY = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
		final float orthonormalBasisGWZ = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = orthonormalBasisGUX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = orthonormalBasisGUY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = orthonormalBasisGUZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = orthonormalBasisGVX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = orthonormalBasisGVY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = orthonormalBasisGVZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = orthonormalBasisGWX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = orthonormalBasisGWY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = orthonormalBasisGWZ;
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the orthonormal basis in {@link #intersectionLHSArray_$private$24} that is used for shading.
	 */
	protected final void orthonormalBasis33FSetIntersectionOrthonormalBasisSLHS() {
//		Get the orthonormal basis:
		final float orthonormalBasisSUX = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0];
		final float orthonormalBasisSUY = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1];
		final float orthonormalBasisSUZ = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2];
		final float orthonormalBasisSVX = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0];
		final float orthonormalBasisSVY = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1];
		final float orthonormalBasisSVZ = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2];
		final float orthonormalBasisSWX = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
		final float orthonormalBasisSWY = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
		final float orthonormalBasisSWZ = this.intersectionLHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = orthonormalBasisSUX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = orthonormalBasisSUY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = orthonormalBasisSUZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = orthonormalBasisSVX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = orthonormalBasisSVY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = orthonormalBasisSVZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = orthonormalBasisSWX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = orthonormalBasisSWY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = orthonormalBasisSWZ;
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the orthonormal basis in {@link #intersectionRHSArray_$private$24} that is used for shading.
	 */
	protected final void orthonormalBasis33FSetIntersectionOrthonormalBasisSRHS() {
//		Get the orthonormal basis:
		final float orthonormalBasisSUX = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 0];
		final float orthonormalBasisSUY = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 1];
		final float orthonormalBasisSUZ = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_U + 2];
		final float orthonormalBasisSVX = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 0];
		final float orthonormalBasisSVY = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 1];
		final float orthonormalBasisSVZ = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_V + 2];
		final float orthonormalBasisSWX = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
		final float orthonormalBasisSWY = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
		final float orthonormalBasisSWZ = this.intersectionRHSArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = orthonormalBasisSUX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = orthonormalBasisSUY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = orthonormalBasisSUZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = orthonormalBasisSVX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = orthonormalBasisSVY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = orthonormalBasisSVZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = orthonormalBasisSWX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = orthonormalBasisSWY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = orthonormalBasisSWZ;
	}
	
	/**
	 * Sets an orthonormal basis in {@link #orthonormalBasis33FArray_$private$9}.
	 * <p>
	 * The orthonormal basis is constructed from the W-direction vector found in {@link #vector3FArray_$private$3}.
	 * <p>
	 * This method will normalize the W-direction vector.
	 */
	protected final void orthonormalBasis33FSetVector3F() {
		final float orthonormalBasisWX = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float orthonormalBasisWY = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float orthonormalBasisWZ = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
		orthonormalBasis33FSetFromW(orthonormalBasisWX, orthonormalBasisWY, orthonormalBasisWZ);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Point2F /////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the value of component 1 in {@link #point2FArray_$private$2}.
	 * 
	 * @return the value of component 1 in {@link #point2FArray_$private$2}
	 */
	protected final float point2FGetComponent1() {
		return this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_COMPONENT_1];
	}
	
	/**
	 * Returns the value of component 2 in {@link #point2FArray_$private$2}.
	 * 
	 * @return the value of component 2 in {@link #point2FArray_$private$2}
	 */
	protected final float point2FGetComponent2() {
		return this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_COMPONENT_2];
	}
	
	/**
	 * Sets a point in {@link #point2FArray_$private$2}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 */
	protected final void point2FSet(final float component1, final float component2) {
		this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_COMPONENT_1] = component1;
		this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_COMPONENT_2] = component2;
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
		if(checkIsZero(u) && checkIsZero(v)) {
			this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_COMPONENT_1] = 0.0F;
			this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_COMPONENT_2] = 0.0F;
		} else {
			final float a = u * 2.0F - 1.0F;
			final float b = v * 2.0F - 1.0F;
			
			final boolean isCaseA = a * a > b * b;
			
			final float phi = isCaseA ? PI_DIVIDED_BY_4 * (b / a) : PI_DIVIDED_BY_2 - PI_DIVIDED_BY_4 * (a / b);
			final float r = isCaseA ? radius * a : radius * b;
			
			this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_COMPONENT_1] = r * cos(phi);
			this.point2FArray_$private$2[POINT_2_F_ARRAY_OFFSET_COMPONENT_2] = r * sin(phi);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Point3F /////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the distance from the point represented by {@code eyeComponent1}, {@code eyeComponent2} and {@code eyeComponent3} to the point represented by {@code lookAtComponent1}, {@code lookAtComponent2} and {@code lookAtComponent3}.
	 * 
	 * @param eyeComponent1 the value of component 1 for the eye point
	 * @param eyeComponent2 the value of component 2 for the eye point
	 * @param eyeComponent3 the value of component 3 for the eye point
	 * @param lookAtComponent1 the value of component 1 for the look at point
	 * @param lookAtComponent2 the value of component 2 for the look at point
	 * @param lookAtComponent3 the value of component 3 for the look at point
	 * @return the distance from the point represented by {@code eyeComponent1}, {@code eyeComponent2} and {@code eyeComponent3} to the point represented by {@code lookAtComponent1}, {@code lookAtComponent2} and {@code lookAtComponent3}
	 */
	protected final float point3FDistance(final float eyeComponent1, final float eyeComponent2, final float eyeComponent3, final float lookAtComponent1, final float lookAtComponent2, final float lookAtComponent3) {
		return sqrt(point3FDistanceSquared(eyeComponent1, eyeComponent2, eyeComponent3, lookAtComponent1, lookAtComponent2, lookAtComponent3));
	}
	
	/**
	 * Returns the squared distance from the point represented by {@code eyeComponent1}, {@code eyeComponent2} and {@code eyeComponent3} to the point represented by {@code lookAtComponent1}, {@code lookAtComponent2} and {@code lookAtComponent3}.
	 * 
	 * @param eyeComponent1 the value of component 1 for the eye point
	 * @param eyeComponent2 the value of component 2 for the eye point
	 * @param eyeComponent3 the value of component 3 for the eye point
	 * @param lookAtComponent1 the value of component 1 for the look at point
	 * @param lookAtComponent2 the value of component 2 for the look at point
	 * @param lookAtComponent3 the value of component 3 for the look at point
	 * @return the squared distance from the point represented by {@code eyeComponent1}, {@code eyeComponent2} and {@code eyeComponent3} to the point represented by {@code lookAtComponent1}, {@code lookAtComponent2} and {@code lookAtComponent3}
	 */
	protected final float point3FDistanceSquared(final float eyeComponent1, final float eyeComponent2, final float eyeComponent3, final float lookAtComponent1, final float lookAtComponent2, final float lookAtComponent3) {
		return vector3FLengthSquared(lookAtComponent1 - eyeComponent1, lookAtComponent2 - eyeComponent2, lookAtComponent3 - eyeComponent3);
	}
	
	/**
	 * Returns the value of component 1 in {@link #point3FArray_$private$3}.
	 * 
	 * @return the value of component 1 in {@link #point3FArray_$private$3}
	 */
	protected final float point3FGetComponent1() {
		return this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_1];
	}
	
	/**
	 * Returns the value of component 2 in {@link #point3FArray_$private$3}.
	 * 
	 * @return the value of component 2 in {@link #point3FArray_$private$3}
	 */
	protected final float point3FGetComponent2() {
		return this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_2];
	}
	
	/**
	 * Returns the value of component 3 in {@link #point3FArray_$private$3}.
	 * 
	 * @return the value of component 3 in {@link #point3FArray_$private$3}
	 */
	protected final float point3FGetComponent3() {
		return this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_3];
	}
	
	/**
	 * Sets a point in {@link #point3FArray_$private$3}.
	 * <p>
	 * The point is constructed by transforming the point represented by {@code component1}, {@code component2} and {@code component3} with the supplied matrix.
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
	 * @param component1 the value of component 1 of the point
	 * @param component2 the value of component 2 of the point
	 * @param component3 the value of component 3 of the point
	 */
	protected final void point3FSetMatrix44FTransform(final float element11, final float element12, final float element13, final float element14, final float element21, final float element22, final float element23, final float element24, final float element31, final float element32, final float element33, final float element34, final float component1, final float component2, final float component3) {
		final float newComponent1 = element11 * component1 + element12 * component2 + element13 * component3 + element14;
		final float newComponent2 = element21 * component1 + element22 * component2 + element23 * component3 + element24;
		final float newComponent3 = element31 * component1 + element32 * component2 + element33 * component3 + element34;
		
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_1] = newComponent1;
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_2] = newComponent2;
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_3] = newComponent3;
	}
	
	/**
	 * Sets a point in {@link #point3FArray_$private$3}.
	 * <p>
	 * The point is constructed by transforming the point represented by {@code component1}, {@code component2} and {@code component3} with the supplied matrix.
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
	 * @param component1 the value of component 1 of the point
	 * @param component2 the value of component 2 of the point
	 * @param component3 the value of component 3 of the point
	 */
	protected final void point3FSetMatrix44FTransformAndDivide(final float element11, final float element12, final float element13, final float element14, final float element21, final float element22, final float element23, final float element24, final float element31, final float element32, final float element33, final float element34, final float element41, final float element42, final float element43, final float element44, final float component1, final float component2, final float component3) {
		final float newComponent1 = element11 * component1 + element12 * component2 + element13 * component3 + element14;
		final float newComponent2 = element21 * component1 + element22 * component2 + element23 * component3 + element24;
		final float newComponent3 = element31 * component1 + element32 * component2 + element33 * component3 + element34;
		final float newComponent4 = element41 * component1 + element42 * component2 + element43 * component3 + element44;
		
		final boolean isDividing = newComponent4 != +0.0F && newComponent4 != -0.0F && newComponent4 != 1.0F;
		
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_1] = isDividing ? newComponent1 / newComponent4 : newComponent1;
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_2] = isDividing ? newComponent2 / newComponent4 : newComponent2;
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_3] = isDividing ? newComponent3 / newComponent4 : newComponent3;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Ray3F ///////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the value of component 1 of the vector called direction of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the value of component 1 of the vector called direction of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetDirectionComponent1() {
		return this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
	}
	
	/**
	 * Returns the value of component 2 of the vector called direction of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the value of component 2 of the vector called direction of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetDirectionComponent2() {
		return this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
	}
	
	/**
	 * Returns the value of component 3 of the vector called direction of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the value of component 3 of the vector called direction of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetDirectionComponent3() {
		return this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
	}
	
	/**
	 * Returns the reciprocal (or inverse) value of component 1 of the vector called direction of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the reciprocal (or inverse) value of component 1 of the vector called direction of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetDirectionReciprocalComponent1() {
		return 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
	}
	
	/**
	 * Returns the reciprocal (or inverse) value of component 2 of the vector called direction of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the reciprocal (or inverse) value of component 2 of the vector called direction of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetDirectionReciprocalComponent2() {
		return 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
	}
	
	/**
	 * Returns the reciprocal (or inverse) value of component 3 of the vector called direction of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the reciprocal (or inverse) value of component 3 of the vector called direction of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetDirectionReciprocalComponent3() {
		return 1.0F / this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
	}
	
	/**
	 * Returns the value of component 1 of the point called origin of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the value of component 1 of the point called origin of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetOriginComponent1() {
		return this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0];
	}
	
	/**
	 * Returns the value of component 2 of the point called origin of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the value of component 2 of the point called origin of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetOriginComponent2() {
		return this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1];
	}
	
	/**
	 * Returns the value of component 3 of the point called origin of the ray in {@link #ray3FArray_$private$8}.
	 * 
	 * @return the value of component 3 of the point called origin of the ray in {@link #ray3FArray_$private$8}
	 */
	protected final float ray3FGetOriginComponent3() {
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
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	protected final void ray3FSetDirection(final float component1, final float component2, final float component3) {
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0] = component1;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1] = component2;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2] = component3;
	}
	
	/**
	 * Sets a ray in {@link #ray3FArray_$private$8}.
	 * <p>
	 * The ray direction is constructed using a normalized representation of the current vector in {@link #vector3FArray_$private$3}. The origin is constructed by offsetting the surface intersection point in {@link #intersectionLHSArray_$private$24}
	 * slightly, in the direction of the ray itself.
	 * 
	 * @param rayDirectionX the X-coordinate of the ray direction
	 * @param rayDirectionY the Y-coordinate of the ray direction
	 * @param rayDirectionZ the Z-coordinate of the ray direction
	 */
	protected final void ray3FSetFromSurfaceIntersectionPointAndVector3FLHS(final float rayDirectionX, final float rayDirectionY, final float rayDirectionZ) {
		final float surfaceIntersectionPointX = intersectionLHSGetSurfaceIntersectionPointComponent1();
		final float surfaceIntersectionPointY = intersectionLHSGetSurfaceIntersectionPointComponent2();
		final float surfaceIntersectionPointZ = intersectionLHSGetSurfaceIntersectionPointComponent3();
		
		final float surfaceNormalSX = intersectionLHSGetOrthonormalBasisSWComponent1();
		final float surfaceNormalSY = intersectionLHSGetOrthonormalBasisSWComponent2();
		final float surfaceNormalSZ = intersectionLHSGetOrthonormalBasisSWComponent3();
		final float surfaceNormalSDotRayDirection = vector3FDotProduct(surfaceNormalSX, surfaceNormalSY, surfaceNormalSZ, rayDirectionX, rayDirectionY, rayDirectionZ);
		final float surfaceNormalSCorrectlyOrientedX = surfaceNormalSDotRayDirection > 0.0F ? -surfaceNormalSX : surfaceNormalSX;
		final float surfaceNormalSCorrectlyOrientedY = surfaceNormalSDotRayDirection > 0.0F ? -surfaceNormalSY : surfaceNormalSY;
		final float surfaceNormalSCorrectlyOrientedZ = surfaceNormalSDotRayDirection > 0.0F ? -surfaceNormalSZ : surfaceNormalSZ;
		
		final float directionX = vector3FGetComponent1();
		final float directionY = vector3FGetComponent2();
		final float directionZ = vector3FGetComponent3();
		final float directionLengthReciprocal = vector3FLengthReciprocal(directionX, directionY, directionZ);
		final float directionNormalizedX = directionX * directionLengthReciprocal;
		final float directionNormalizedY = directionY * directionLengthReciprocal;
		final float directionNormalizedZ = directionZ * directionLengthReciprocal;
		
		final float nDotD = vector3FDotProduct(surfaceNormalSCorrectlyOrientedX, surfaceNormalSCorrectlyOrientedY, surfaceNormalSCorrectlyOrientedZ, directionNormalizedX, directionNormalizedY, directionNormalizedZ);
		final float nDotE = 0.0F;
		
		final float offsetX = surfaceNormalSCorrectlyOrientedX * nDotE;
		final float offsetY = surfaceNormalSCorrectlyOrientedY * nDotE;
		final float offsetZ = surfaceNormalSCorrectlyOrientedZ * nDotE;
		final float offsetCorrectlyOrientedX = nDotD < 0.0F ? -offsetX : offsetX;
		final float offsetCorrectlyOrientedY = nDotD < 0.0F ? -offsetY : offsetY;
		final float offsetCorrectlyOrientedZ = nDotD < 0.0F ? -offsetZ : offsetZ;
		
		final float originOffsetX = surfaceIntersectionPointX + offsetCorrectlyOrientedX;
		final float originOffsetY = surfaceIntersectionPointY + offsetCorrectlyOrientedY;
		final float originOffsetZ = surfaceIntersectionPointZ + offsetCorrectlyOrientedZ;
		final float originX = nextAfter(originOffsetX, originOffsetX > 0.0F ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY);
		final float originY = nextAfter(originOffsetY, originOffsetY > 0.0F ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY);
		final float originZ = nextAfter(originOffsetZ, originOffsetZ > 0.0F ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY);
		
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
		final float surfaceIntersectionPointX = intersectionRHSGetSurfaceIntersectionPointComponent1();
		final float surfaceIntersectionPointY = intersectionRHSGetSurfaceIntersectionPointComponent2();
		final float surfaceIntersectionPointZ = intersectionRHSGetSurfaceIntersectionPointComponent3();
		
		final float surfaceNormalSX = intersectionRHSGetOrthonormalBasisSWComponent1();
		final float surfaceNormalSY = intersectionRHSGetOrthonormalBasisSWComponent2();
		final float surfaceNormalSZ = intersectionRHSGetOrthonormalBasisSWComponent3();
		final float surfaceNormalSDotRayDirection = vector3FDotProduct(surfaceNormalSX, surfaceNormalSY, surfaceNormalSZ, rayDirectionX, rayDirectionY, rayDirectionZ);
		final float surfaceNormalSCorrectlyOrientedX = surfaceNormalSDotRayDirection > 0.0F ? -surfaceNormalSX : surfaceNormalSX;
		final float surfaceNormalSCorrectlyOrientedY = surfaceNormalSDotRayDirection > 0.0F ? -surfaceNormalSY : surfaceNormalSY;
		final float surfaceNormalSCorrectlyOrientedZ = surfaceNormalSDotRayDirection > 0.0F ? -surfaceNormalSZ : surfaceNormalSZ;
		
		final float directionX = vector3FGetComponent1();
		final float directionY = vector3FGetComponent2();
		final float directionZ = vector3FGetComponent3();
		final float directionLengthReciprocal = vector3FLengthReciprocal(directionX, directionY, directionZ);
		final float directionNormalizedX = directionX * directionLengthReciprocal;
		final float directionNormalizedY = directionY * directionLengthReciprocal;
		final float directionNormalizedZ = directionZ * directionLengthReciprocal;
		
		final float nDotD = vector3FDotProduct(surfaceNormalSCorrectlyOrientedX, surfaceNormalSCorrectlyOrientedY, surfaceNormalSCorrectlyOrientedZ, directionNormalizedX, directionNormalizedY, directionNormalizedZ);
		final float nDotE = 0.0F;
		
		final float offsetX = surfaceNormalSCorrectlyOrientedX * nDotE;
		final float offsetY = surfaceNormalSCorrectlyOrientedY * nDotE;
		final float offsetZ = surfaceNormalSCorrectlyOrientedZ * nDotE;
		final float offsetCorrectlyOrientedX = nDotD < 0.0F ? -offsetX : offsetX;
		final float offsetCorrectlyOrientedY = nDotD < 0.0F ? -offsetY : offsetY;
		final float offsetCorrectlyOrientedZ = nDotD < 0.0F ? -offsetZ : offsetZ;
		
		final float originOffsetX = surfaceIntersectionPointX + offsetCorrectlyOrientedX;
		final float originOffsetY = surfaceIntersectionPointY + offsetCorrectlyOrientedY;
		final float originOffsetZ = surfaceIntersectionPointZ + offsetCorrectlyOrientedZ;
		final float originX = nextAfter(originOffsetX, originOffsetX > 0.0F ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY);
		final float originY = nextAfter(originOffsetY, originOffsetY > 0.0F ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY);
		final float originZ = nextAfter(originOffsetZ, originOffsetZ > 0.0F ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY);
		
		ray3FSetOrigin(originX, originY, originZ);
		ray3FSetDirection(directionNormalizedX, directionNormalizedY, directionNormalizedZ);
		ray3FSetTMinimum(DEFAULT_T_MINIMUM);
		ray3FSetTMaximum(DEFAULT_T_MAXIMUM);
	}
	
	/**
	 * Sets the component values for the point called origin of the ray in {@link ray3FArray_$private$8}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	protected final void ray3FSetOrigin(final float component1, final float component2, final float component3) {
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0] = component1;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1] = component2;
		this.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2] = component3;
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
	 * Returns {@code true} if, and only if, both vectors are in the same hemisphere, {@code false} otherwise.
	 * 
	 * @param component1LHS the value of component 1 for the vector on the left-hand side
	 * @param component2LHS the value of component 2 for the vector on the left-hand side
	 * @param component3LHS the value of component 3 for the vector on the left-hand side
	 * @param component1RHS the value of component 1 for the vector on the right-hand side
	 * @param component2RHS the value of component 2 for the vector on the right-hand side
	 * @param component3RHS the value of component 3 for the vector on the right-hand side
	 * @return {@code true} if, and only if, both vectors are in the same hemisphere, {@code false} otherwise
	 */
	protected final boolean vector3FSameHemisphere(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		return vector3FDotProduct(component1LHS, component2LHS, component3LHS, component1RHS, component2RHS, component3RHS) > 0.0F;
	}
	
	/**
	 * Returns {@code true} if, and only if, both vectors are in the same hemisphere, {@code false} otherwise.
	 * <p>
	 * This method only operates on the Z-component (component 3), just like PBRT.
	 * 
	 * @param component1LHS the value of component 1 for the vector on the left-hand side
	 * @param component2LHS the value of component 2 for the vector on the left-hand side
	 * @param component3LHS the value of component 3 for the vector on the left-hand side
	 * @param component1RHS the value of component 1 for the vector on the right-hand side
	 * @param component2RHS the value of component 2 for the vector on the right-hand side
	 * @param component3RHS the value of component 3 for the vector on the right-hand side
	 * @return {@code true} if, and only if, both vectors are in the same hemisphere, {@code false} otherwise
	 */
	@SuppressWarnings("static-method")
	protected final boolean vector3FSameHemisphereZ(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		return component3LHS * component3RHS > 0.0F;
	}
	
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
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = refractionDirectionNormalizedX;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = refractionDirectionNormalizedY;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = refractionDirectionNormalizedZ;
		
		return true;
	}
	
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
	protected final boolean vector3FSetRefraction2(final float directionX, final float directionY, final float directionZ, final float normalX, final float normalY, final float normalZ, final float eta) {
		final float cosThetaI = vector3FDotProduct(directionX, directionY, directionZ, normalX, normalY, normalZ);
		final float sinThetaISquared = 1.0F - cosThetaI * cosThetaI;
		final float sinThetaTSquared = 1.0F - eta * eta * sinThetaISquared;
		final float cosThetaT = sqrt(sinThetaTSquared);
		
		final boolean isTotalInternalReflection = sinThetaTSquared < 0.0F;
		
		if(isTotalInternalReflection) {
			return false;
		}
		
		final float refractionDirectionX = directionX * eta - normalX * (eta * cosThetaI + cosThetaT);
		final float refractionDirectionY = directionY * eta - normalY * (eta * cosThetaI + cosThetaT);
		final float refractionDirectionZ = directionZ * eta - normalZ * (eta * cosThetaI + cosThetaT);
		final float refractionDirectionLengthReciprocal = vector3FLengthReciprocal(refractionDirectionX, refractionDirectionY, refractionDirectionZ);
		final float refractionDirectionNormalizedX = refractionDirectionX * refractionDirectionLengthReciprocal;
		final float refractionDirectionNormalizedY = refractionDirectionY * refractionDirectionLengthReciprocal;
		final float refractionDirectionNormalizedZ = refractionDirectionZ * refractionDirectionLengthReciprocal;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = refractionDirectionNormalizedX;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = refractionDirectionNormalizedY;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = refractionDirectionNormalizedZ;
		
		return true;
	}
	
	/**
	 * Returns the cosine of the angle phi.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the cosine of the angle phi
	 */
	protected final float vector3FCosPhi(final float component1, final float component2, final float component3) {
		final float sinTheta = vector3FSinTheta(component1, component2, component3);
		
		return checkIsZero(sinTheta) ? 1.0F : saturateF(component1 / sinTheta, -1.0F, 1.0F);
	}
	
	/**
	 * Returns the cosine of the angle phi in squared form.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the cosine of the angle phi in squared form
	 */
	protected final float vector3FCosPhiSquared(final float component1, final float component2, final float component3) {
		return vector3FCosPhi(component1, component2, component3) * vector3FCosPhi(component1, component2, component3);
	}
	
	/**
	 * Returns the cosine of the angle theta.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the cosine of the angle theta
	 */
	@SuppressWarnings("static-method")
	protected final float vector3FCosTheta(final float component1, final float component2, final float component3) {
		return component3;
	}
	
	/**
	 * Returns the cosine of the angle theta in absolute form.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the cosine of the angle theta in absolute form
	 */
	protected final float vector3FCosThetaAbs(final float component1, final float component2, final float component3) {
		return abs(vector3FCosTheta(component1, component2, component3));
	}
	
	/**
	 * Returns the cosine of the angle theta in quartic form.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the cosine of the angle theta in quartic form
	 */
	protected final float vector3FCosThetaQuartic(final float component1, final float component2, final float component3) {
		return vector3FCosThetaSquared(component1, component2, component3) * vector3FCosThetaSquared(component1, component2, component3);
	}
	
	/**
	 * Returns the cosine of the angle theta in squared form.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the cosine of the angle theta in squared form
	 */
	protected final float vector3FCosThetaSquared(final float component1, final float component2, final float component3) {
		return vector3FCosTheta(component1, component2, component3) * vector3FCosTheta(component1, component2, component3);
	}
	
	/**
	 * Returns the dot product between the vector represented by {@code component1LHS}, {@code component2LHS} and {@code component3LHS} and the vector represented by {@code component1RHS}, {@code component2RHS} and {@code component3RHS}.
	 * 
	 * @param component1LHS the value of component 1 for the vector on the left hand side
	 * @param component2LHS the value of component 2 for the vector on the left hand side
	 * @param component3LHS the value of component 3 for the vector on the left hand side
	 * @param component1RHS the value of component 1 for the vector on the right hand side
	 * @param component2RHS the value of component 2 for the vector on the right hand side
	 * @param component3RHS the value of component 3 for the vector on the right hand side
	 * @return the dot product between the vector represented by {@code component1LHS}, {@code component2LHS} and {@code component3LHS} and the vector represented by {@code component1RHS}, {@code component2RHS} and {@code component3RHS}
	 */
	@SuppressWarnings("static-method")
	protected final float vector3FDotProduct(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		return component1LHS * component1RHS + component2LHS * component2RHS + component3LHS * component3RHS;
	}
	
	/**
	 * Returns the value of component 1 in {@link #vector3FArray_$private$3}.
	 * 
	 * @return the value of component 1 in {@link #vector3FArray_$private$3}
	 */
	protected final float vector3FGetComponent1() {
		return this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
	}
	
	/**
	 * Returns the value of component 2 in {@link #vector3FArray_$private$3}.
	 * 
	 * @return the value of component 2 in {@link #vector3FArray_$private$3}
	 */
	protected final float vector3FGetComponent2() {
		return this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
	}
	
	/**
	 * Returns the value of component 3 in {@link #vector3FArray_$private$3}.
	 * 
	 * @return the value of component 3 in {@link #vector3FArray_$private$3}
	 */
	protected final float vector3FGetComponent3() {
		return this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
	}
	
	/**
	 * Returns the length of the vector represented by {@code component1}, {@code component2} and {@code component3}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 * @return the length of the vector represented by {@code component1}, {@code component2} and {@code component3}
	 */
	protected final float vector3FLength(final float component1, final float component2, final float component3) {
		return sqrt(vector3FLengthSquared(component1, component2, component3));
	}
	
	/**
	 * Returns the reciprocal (or inverse) length of the vector represented by {@code component1}, {@code component2} and {@code component3}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 * @return the reciprocal (or inverse) length of the vector represented by {@code component1}, {@code component2} and {@code component3}
	 */
	protected final float vector3FLengthReciprocal(final float component1, final float component2, final float component3) {
		return rsqrt(vector3FLengthSquared(component1, component2, component3));
	}
	
	/**
	 * Returns the squared length of the vector represented by {@code component1}, {@code component2} and {@code component3}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 * @return the squared length of the vector represented by {@code component1}, {@code component2} and {@code component3}
	 */
	@SuppressWarnings("static-method")
	protected final float vector3FLengthSquared(final float component1, final float component2, final float component3) {
		return component1 * component1 + component2 * component2 + component3 * component3;
	}
	
	/**
	 * Returns the sine of the angle phi.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the sine of the angle phi
	 */
	protected final float vector3FSinPhi(final float component1, final float component2, final float component3) {
		final float sinTheta = vector3FSinTheta(component1, component2, component3);
		
		return checkIsZero(sinTheta) ? 0.0F : saturateF(component2 / sinTheta, -1.0F, 1.0F);
	}
	
	/**
	 * Returns the sine of the angle phi in squared form.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the sine of the angle phi in squared form
	 */
	protected final float vector3FSinPhiSquared(final float component1, final float component2, final float component3) {
		return vector3FSinPhi(component1, component2, component3) * vector3FSinPhi(component1, component2, component3);
	}
	
	/**
	 * Returns the sine of the angle theta.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the sine of the angle theta
	 */
	protected final float vector3FSinTheta(final float component1, final float component2, final float component3) {
		return sqrt(vector3FSinThetaSquared(component1, component2, component3));
	}
	
	/**
	 * Returns the sine of the angle theta in squared form.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the sine of the angle theta in squared form
	 */
	protected final float vector3FSinThetaSquared(final float component1, final float component2, final float component3) {
		return max(0.0F, 1.0F - vector3FCosThetaSquared(component1, component2, component3));
	}
	
	/**
	 * Returns the spherical phi angle.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the spherical phi angle
	 */
	protected final float vector3FSphericalPhi(final float component1, final float component2, final float component3) {
		return addIfLessThanThreshold(atan2(component2, component1), 0.0F, PI_MULTIPLIED_BY_2);
	}
	
	/**
	 * Returns the spherical theta angle.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the spherical theta angle
	 */
	protected final float vector3FSphericalTheta(final float component1, final float component2, final float component3) {
		return acos(saturateF(component3, -1.0F, 1.0F));
	}
	
	/**
	 * Returns the tangent of the angle theta.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the tangent of the angle theta
	 */
	protected final float vector3FTanTheta(final float component1, final float component2, final float component3) {
		return vector3FSinTheta(component1, component2, component3) / vector3FCosTheta(component1, component2, component3);
	}
	
	/**
	 * Returns the tangent of the angle theta in absolute form.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the tangent of the angle theta in absolute form
	 */
	protected final float vector3FTanThetaAbs(final float component1, final float component2, final float component3) {
		return abs(vector3FTanTheta(component1, component2, component3));
	}
	
	/**
	 * Returns the tangent of the angle theta in squared form.
	 * 
	 * @param component1 the value of component 1 for the vector
	 * @param component2 the value of component 2 for the vector
	 * @param component3 the value of component 3 for the vector
	 * @return the tangent of the angle theta in squared form
	 */
	protected final float vector3FTanThetaSquared(final float component1, final float component2, final float component3) {
		return vector3FSinThetaSquared(component1, component2, component3) / vector3FCosThetaSquared(component1, component2, component3);
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed using the vector represented by {@code component1}, {@code component2} and {@code component3}.
	 * 
	 * @param component1 the value of component 1 of the vector
	 * @param component2 the value of component 2 of the vector
	 * @param component3 the value of component 3 of the vector
	 */
	protected final void vector3FSet(final float component1, final float component2, final float component3) {
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = component1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = component2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = component3;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by perturbing the normal, which is represented by {@code normalX}, {@code normalY} and {@code normalZ}, with a direction sampled using a hemisphere cosine distribution.
	 * 
	 * @param normalX the X-component of the normal vector
	 * @param normalY the Y-component of the normal vector
	 * @param normalZ the Z-component of the normal vector
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 */
	protected final void vector3FSetDiffuseReflection(final float normalX, final float normalY, final float normalZ, final float u, final float v) {
		orthonormalBasis33FSetFromW(normalX, normalY, normalZ);
		
		vector3FSetSampleHemisphereCosineDistribution2(u, v);
		vector3FSetOrthonormalBasis33FTransformNormalizeFromVector3F();
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
		vector3FSetDirectionSpherical3(sin(v * PI), cos(v * PI), u * PI_MULTIPLIED_BY_2);
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
	 * The vector is constructed by negating the vector represented by {@code component1Direction}, {@code component2Direction} and {@code component3Direction} if, and only if, the dot product between the vector represented by {@code component1LHS},
	 * {@code component2LHS} and {@code component3LHS} and the vector represented by {@code component1RHS}, {@code component2RHS} and {@code component3RHS} is less than {@code 0.0F}. Otherwise, its current values will be used.
	 * 
	 * @param component1LHS the value of component 1 of the vector on the left-hand side
	 * @param component2LHS the value of component 2 of the vector on the left-hand side
	 * @param component3LHS the value of component 3 of the vector on the left-hand side
	 * @param component1RHS the value of component 1 of the vector on the right-hand side
	 * @param component2RHS the value of component 2 of the vector on the right-hand side
	 * @param component3RHS the value of component 3 of the vector on the right-hand side
	 * @param component1Direction the value of component 1 of the vector to set, whether it is negated or not
	 * @param component2Direction the value of component 2 of the vector to set, whether it is negated or not
	 * @param component3Direction the value of component 3 of the vector to set, whether it is negated or not
	 */
	protected final void vector3FSetFaceForwardDirection(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS, final float component1Direction, final float component2Direction, final float component3Direction) {
		if(vector3FDotProduct(component1LHS, component2LHS, component3LHS, component1RHS, component2RHS, component3RHS) < 0.0F) {
			vector3FSet(-component1Direction, -component2Direction, -component3Direction);
		} else {
			vector3FSet(+component1Direction, +component2Direction, +component3Direction);
		}
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by negating the vector represented by {@code component1LHS}, {@code component2LHS} and {@code component3LHS} if, and only if, the dot product between that vector and the vector represented by {@code component1RHS},
	 * {@code component2RHS} and {@code component3RHS} is less than {@code 0.0F}. Otherwise, its current values will be used.
	 * 
	 * @param component1LHS the value of component 1 of the vector on the left-hand side
	 * @param component2LHS the value of component 2 of the vector on the left-hand side
	 * @param component3LHS the value of component 3 of the vector on the left-hand side
	 * @param component1RHS the value of component 1 of the vector on the right-hand side
	 * @param component2RHS the value of component 2 of the vector on the right-hand side
	 * @param component3RHS the value of component 3 of the vector on the right-hand side
	 */
	protected final void vector3FSetFaceForwardLHS(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		if(vector3FDotProduct(component1LHS, component2LHS, component3LHS, component1RHS, component2RHS, component3RHS) < 0.0F) {
			vector3FSet(-component1LHS, -component2LHS, -component3LHS);
		} else {
			vector3FSet(+component1LHS, +component2LHS, +component3LHS);
		}
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by negating the vector represented by {@code component1LHS}, {@code component2LHS} and {@code component3LHS} if, and only if, the dot product between that vector and the vector represented by {@code component1RHS},
	 * {@code component2RHS} and {@code component3RHS} is greater than or equal to {@code 0.0F}. Otherwise, its current values will be used.
	 * 
	 * @param component1LHS the value of component 1 of the vector on the left-hand side
	 * @param component2LHS the value of component 2 of the vector on the left-hand side
	 * @param component3LHS the value of component 3 of the vector on the left-hand side
	 * @param component1RHS the value of component 1 of the vector on the right-hand side
	 * @param component2RHS the value of component 2 of the vector on the right-hand side
	 * @param component3RHS the value of component 3 of the vector on the right-hand side
	 */
	protected final void vector3FSetFaceForwardLHSNegated(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		if(vector3FDotProduct(component1LHS, component2LHS, component3LHS, component1RHS, component2RHS, component3RHS) < 0.0F) {
			vector3FSet(+component1LHS, +component2LHS, +component3LHS);
		} else {
			vector3FSet(-component1LHS, -component2LHS, -component3LHS);
		}
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by negating component 3 of the vector represented by {@code component1RHS}, {@code component2RHS} and {@code component3RHS} if, and only if, {@code component3LHS} is less than {@code 0.0F}. Otherwise, its current value
	 * will be used.
	 * 
	 * @param component1LHS the value of component 1 of the vector on the left-hand side
	 * @param component2LHS the value of component 2 of the vector on the left-hand side
	 * @param component3LHS the value of component 3 of the vector on the left-hand side
	 * @param component1RHS the value of component 1 of the vector on the right-hand side
	 * @param component2RHS the value of component 2 of the vector on the right-hand side
	 * @param component3RHS the value of component 3 of the vector on the right-hand side
	 */
	protected final void vector3FSetFaceForwardRHSComponent3(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		vector3FSet(+component1RHS, +component2RHS, component3LHS < 0.0F ? -component3RHS : +component3RHS);
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by negating component 3 of the vector represented by {@code component1RHS}, {@code component2RHS} and {@code component3RHS} if, and only if, {@code component3LHS} is greater than {@code 0.0F}. Otherwise, its current
	 * value will be used.
	 * 
	 * @param component1LHS the value of component 1 of the vector on the left-hand side
	 * @param component2LHS the value of component 2 of the vector on the left-hand side
	 * @param component3LHS the value of component 3 of the vector on the left-hand side
	 * @param component1RHS the value of component 1 of the vector on the right-hand side
	 * @param component2RHS the value of component 2 of the vector on the right-hand side
	 * @param component3RHS the value of component 3 of the vector on the right-hand side
	 */
	protected final void vector3FSetFaceForwardRHSComponent3Negated(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		vector3FSet(+component1RHS, +component2RHS, component3LHS > 0.0F ? -component3RHS : +component3RHS);
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by perturbing the specular reflection direction with a direction sampled using a hemisphere power-cosine distribution.
	 * <p>
	 * The specular reflection direction is constructed by calling {@link #vector3FSetSpecularReflection(float, float, float, float, float, float)}, with the parameter arguments {@code directionX}, {@code directionY}, {@code directionZ},
	 * {@code normalX}, {@code normalY} and {@code normalZ}.
	 * 
	 * @param directionX the X-component of the direction vector
	 * @param directionY the Y-component of the direction vector
	 * @param directionZ the Z-component of the direction vector
	 * @param normalX the X-component of the normal vector
	 * @param normalY the Y-component of the normal vector
	 * @param normalZ the Z-component of the normal vector
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param exponent the exponent to use
	 */
	protected final void vector3FSetGlossyReflection(final float directionX, final float directionY, final float directionZ, final float normalX, final float normalY, final float normalZ, final float u, final float v, final float exponent) {
		vector3FSetSpecularReflection(directionX, directionY, directionZ, normalX, normalY, normalZ);
		
		orthonormalBasis33FSetVector3F();
		
		vector3FSetSampleHemispherePowerCosineDistribution(u, v, exponent);
		vector3FSetOrthonormalBasis33FTransformNormalizeFromVector3F();
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by perturbing the specular reflection direction with a direction sampled using a hemisphere power-cosine distribution.
	 * <p>
	 * The specular reflection direction is constructed by calling {@link #vector3FSetSpecularReflectionFacingSurface(float, float, float, float, float, float)}, with the parameter arguments {@code directionX}, {@code directionY}, {@code directionZ},
	 * {@code normalX}, {@code normalY} and {@code normalZ}.
	 * 
	 * @param directionX the X-component of the direction vector
	 * @param directionY the Y-component of the direction vector
	 * @param directionZ the Z-component of the direction vector
	 * @param normalX the X-component of the normal vector
	 * @param normalY the Y-component of the normal vector
	 * @param normalZ the Z-component of the normal vector
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param exponent the exponent to use
	 */
	protected final void vector3FSetGlossyReflectionFacingSurface(final float directionX, final float directionY, final float directionZ, final float normalX, final float normalY, final float normalZ, final float u, final float v, final float exponent) {
		vector3FSetSpecularReflectionFacingSurface(directionX, directionY, directionZ, normalX, normalY, normalZ);
		
		orthonormalBasis33FSetVector3F();
		
		vector3FSetSampleHemispherePowerCosineDistribution(u, v, exponent);
		vector3FSetOrthonormalBasis33FTransformNormalizeFromVector3F();
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed using {@code vector3FSet(normalX, normalY, normalZ)} or {@code vector3FSetNormalize(outgoingX - incomingX, outgoingY - incomingY, outgoingZ - incomingZ)}, as
	 * {@code vector3FDotProduct(outgoingX, outgoingY, outgoingZ, incomingX, incomingY, incomingZ)} is greater than {@code 0.999F} or less than or equal to {@code 0.999F}, respectively.
	 * 
	 * @param outgoingX the X-component of the vector that points in the opposite direction of the ray
	 * @param outgoingY the Y-component of the vector that points in the opposite direction of the ray
	 * @param outgoingZ the Z-component of the vector that points in the opposite direction of the ray
	 * @param normalX the X-component of the vector that points in the direction of the surface normal
	 * @param normalY the Y-component of the vector that points in the direction of the surface normal
	 * @param normalZ the Z-component of the vector that points in the direction of the surface normal
	 * @param incomingX the X-component of the vector that points in the direction of the light source to the surface intersection point
	 * @param incomingY the Y-component of the vector that points in the direction of the light source to the surface intersection point
	 * @param incomingZ the Z-component of the vector that points in the direction of the light source to the surface intersection point
	 */
	protected final void vector3FSetHalf(final float outgoingX, final float outgoingY, final float outgoingZ, final float normalX, final float normalY, final float normalZ, final float incomingX, final float incomingY, final float incomingZ) {
		if(vector3FDotProduct(outgoingX, outgoingY, outgoingZ, incomingX, incomingY, incomingZ) > 0.999F) {
			vector3FSet(normalX, normalY, normalZ);
		} else {
			vector3FSetNormalize(outgoingX - incomingX, outgoingY - incomingY, outgoingZ - incomingZ);
		}
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector represented by {@code component1}, {@code component2} and {@code component3} with the supplied matrix.
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
	 * @param component1 the value of component 1 of the vector
	 * @param component2 the value of component 2 of the vector
	 * @param component3 the value of component 3 of the vector
	 */
	protected final void vector3FSetMatrix44FTransform(final float element11, final float element12, final float element13, final float element21, final float element22, final float element23, final float element31, final float element32, final float element33, final float component1, final float component2, final float component3) {
		final float newComponent1 = element11 * component1 + element12 * component2 + element13 * component3;
		final float newComponent2 = element21 * component1 + element22 * component2 + element23 * component3;
		final float newComponent3 = element31 * component1 + element32 * component2 + element33 * component3;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = newComponent1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = newComponent2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = newComponent3;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector represented by {@code component1}, {@code component2} and {@code component3} with the supplied matrix and normalizing it.
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
	 * @param component1 the value of component 1 of the vector
	 * @param component2 the value of component 2 of the vector
	 * @param component3 the value of component 3 of the vector
	 */
	protected final void vector3FSetMatrix44FTransformNormalize(final float element11, final float element12, final float element13, final float element21, final float element22, final float element23, final float element31, final float element32, final float element33, final float component1, final float component2, final float component3) {
		final float newComponent1 = element11 * component1 + element12 * component2 + element13 * component3;
		final float newComponent2 = element21 * component1 + element22 * component2 + element23 * component3;
		final float newComponent3 = element31 * component1 + element32 * component2 + element33 * component3;
		final float newLengthReciprocal = vector3FLengthReciprocal(newComponent1, newComponent2, newComponent3);
		final float newNormalizedComponent1 = newComponent1 * newLengthReciprocal;
		final float newNormalizedComponent2 = newComponent2 * newLengthReciprocal;
		final float newNormalizedComponent3 = newComponent3 * newLengthReciprocal;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = newNormalizedComponent1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = newNormalizedComponent2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = newNormalizedComponent3;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector represented by {@code component1}, {@code component2} and {@code component3} with the supplied matrix in transpose order.
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
	 * @param component1 the value of component 1 of the vector
	 * @param component2 the value of component 2 of the vector
	 * @param component3 the value of component 3 of the vector
	 */
	protected final void vector3FSetMatrix44FTransformTranspose(final float element11, final float element12, final float element13, final float element21, final float element22, final float element23, final float element31, final float element32, final float element33, final float component1, final float component2, final float component3) {
		final float newComponent1 = element11 * component1 + element21 * component2 + element31 * component3;
		final float newComponent2 = element12 * component1 + element22 * component2 + element32 * component3;
		final float newComponent3 = element13 * component1 + element23 * component2 + element33 * component3;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = newComponent1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = newComponent2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = newComponent3;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector represented by {@code component1}, {@code component2} and {@code component3} with the supplied matrix in transpose order and normalizing it.
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
	 * @param component1 the value of component 1 of the vector
	 * @param component2 the value of component 2 of the vector
	 * @param component3 the value of component 3 of the vector
	 */
	protected final void vector3FSetMatrix44FTransformTransposeNormalize(final float element11, final float element12, final float element13, final float element21, final float element22, final float element23, final float element31, final float element32, final float element33, final float component1, final float component2, final float component3) {
		final float newComponent1 = element11 * component1 + element21 * component2 + element31 * component3;
		final float newComponent2 = element12 * component1 + element22 * component2 + element32 * component3;
		final float newComponent3 = element13 * component1 + element23 * component2 + element33 * component3;
		final float newLengthReciprocal = vector3FLengthReciprocal(newComponent1, newComponent2, newComponent3);
		final float newNormalizedComponent1 = newComponent1 * newLengthReciprocal;
		final float newNormalizedComponent2 = newComponent2 * newLengthReciprocal;
		final float newNormalizedComponent3 = newComponent3 * newLengthReciprocal;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = newNormalizedComponent1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = newNormalizedComponent2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = newNormalizedComponent3;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by normalizing the vector represented by {@code component1}, {@code component2} and {@code component3}.
	 * 
	 * @param component1 the value of component 1 of the vector
	 * @param component2 the value of component 2 of the vector
	 * @param component3 the value of component 3 of the vector
	 */
	protected final void vector3FSetNormalize(final float component1, final float component2, final float component3) {
		final float length = vector3FLength(component1, component2, component3);
		
		final boolean isLengthGTEThreshold = length >= 0.99999982F;
		final boolean isLengthLTEThreshold = length <= 1.00000012F;
		
		if(isLengthGTEThreshold && isLengthLTEThreshold) {
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = component1;
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = component2;
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = component3;
		} else {
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = component1 / length;
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = component2 / length;
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = component3 / length;
		}
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector represented by {@code component1}, {@code component2} and {@code component3} with the orthonormal basis in {@link #orthonormalBasis33FArray_$private$9} and normalizing it.
	 * 
	 * @param component1 the value of component 1 of the vector
	 * @param component2 the value of component 2 of the vector
	 * @param component3 the value of component 3 of the vector
	 */
	protected final void vector3FSetOrthonormalBasis33FTransformNormalize(final float component1, final float component2, final float component3) {
		final float orthonormalBasisUX = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0];
		final float orthonormalBasisUY = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1];
		final float orthonormalBasisUZ = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2];
		final float orthonormalBasisVX = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0];
		final float orthonormalBasisVY = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1];
		final float orthonormalBasisVZ = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2];
		final float orthonormalBasisWX = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0];
		final float orthonormalBasisWY = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1];
		final float orthonormalBasisWZ = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2];
		
		final float newComponent1 = orthonormalBasisUX * component1 + orthonormalBasisVX * component2 + orthonormalBasisWX * component3;
		final float newComponent2 = orthonormalBasisUY * component1 + orthonormalBasisVY * component2 + orthonormalBasisWY * component3;
		final float newComponent3 = orthonormalBasisUZ * component1 + orthonormalBasisVZ * component2 + orthonormalBasisWZ * component3;
		final float newLengthReciprocal = vector3FLengthReciprocal(newComponent1, newComponent2, newComponent3);
		final float newNormalizedComponent1 = newComponent1 * newLengthReciprocal;
		final float newNormalizedComponent2 = newComponent2 * newLengthReciprocal;
		final float newNormalizedComponent3 = newComponent3 * newLengthReciprocal;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = newNormalizedComponent1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = newNormalizedComponent2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = newNormalizedComponent3;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector in {@code vector3FArray_$private$3} with the orthonormal basis in {@link #orthonormalBasis33FArray_$private$9} and normalizing it.
	 */
	protected final void vector3FSetOrthonormalBasis33FTransformNormalizeFromVector3F() {
		final float component1 = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float component2 = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float component3 = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
		vector3FSetOrthonormalBasis33FTransformNormalize(component1, component2, component3);
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector represented by {@code component1}, {@code component2} and {@code component3} with the orthonormal basis in {@link #orthonormalBasis33FArray_$private$9} in reverse order and normalizing it.
	 * 
	 * @param component1 the value of component 1 of the vector
	 * @param component2 the value of component 2 of the vector
	 * @param component3 the value of component 3 of the vector
	 */
	protected final void vector3FSetOrthonormalBasis33FTransformReverseNormalize(final float component1, final float component2, final float component3) {
		final float orthonormalBasisUX = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0];
		final float orthonormalBasisUY = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1];
		final float orthonormalBasisUZ = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2];
		final float orthonormalBasisVX = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0];
		final float orthonormalBasisVY = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1];
		final float orthonormalBasisVZ = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2];
		final float orthonormalBasisWX = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0];
		final float orthonormalBasisWY = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1];
		final float orthonormalBasisWZ = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2];
		
		final float newComponent1 = component1 * orthonormalBasisUX + component2 * orthonormalBasisUY + component3 * orthonormalBasisUZ;
		final float newComponent2 = component1 * orthonormalBasisVX + component2 * orthonormalBasisVY + component3 * orthonormalBasisVZ;
		final float newComponent3 = component1 * orthonormalBasisWX + component2 * orthonormalBasisWY + component3 * orthonormalBasisWZ;
		final float newLengthReciprocal = vector3FLengthReciprocal(newComponent1, newComponent2, newComponent3);
		final float newNormalizedComponent1 = newComponent1 * newLengthReciprocal;
		final float newNormalizedComponent2 = newComponent2 * newLengthReciprocal;
		final float newNormalizedComponent3 = newComponent3 * newLengthReciprocal;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = newNormalizedComponent1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = newNormalizedComponent2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = newNormalizedComponent3;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by transforming the vector in {@code vector3FArray_$private$3} with the orthonormal basis in {@link #orthonormalBasis33FArray_$private$9} in reverse order and normalizing it.
	 */
	protected final void vector3FSetOrthonormalBasis33FTransformReverseNormalizeFromVector3F() {
		final float component1 = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float component2 = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float component3 = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
		vector3FSetOrthonormalBasis33FTransformReverseNormalize(component1, component2, component3);
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
		
		final float component1 = point2FGetComponent1();
		final float component2 = point2FGetComponent2();
		final float component3 = sqrt(max(0.0F, 1.0F - component1 * component1 - component2 * component2));
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = component1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = component2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = component3;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by sampling a direction on a hemisphere with a cosine distribution.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 */
	protected final void vector3FSetSampleHemisphereCosineDistribution2(final float u, final float v) {
		final float sinTheta = sqrt(v);
		final float cosTheta = sqrt(1.0F - v);
		final float phi = PI_MULTIPLIED_BY_2 * u;
		
		final float component1 = sinTheta * cos(phi);
		final float component2 = sinTheta * sin(phi);
		final float component3 = cosTheta;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = component1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = component2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = component3;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed by sampling a direction on a hemisphere with a power-cosine distribution.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param exponent the exponent to use
	 */
	protected final void vector3FSetSampleHemispherePowerCosineDistribution(final float u, final float v, final float exponent) {
		final float cosTheta = pow(1.0F - u, 1.0F / (exponent + 1.0F));
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float phi = PI_MULTIPLIED_BY_2 * v;
		
		final float component1 = sinTheta * cos(phi);
		final float component2 = sinTheta * sin(phi);
		final float component3 = cosTheta;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = component1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = component2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = component3;
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
		final float phi = PI_MULTIPLIED_BY_2 * v;
		
		final float component1 = sinTheta * cos(phi);
		final float component2 = sinTheta * sin(phi);
		final float component3 = cosTheta;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = component1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = component2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = component3;
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
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = reflectionX;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = reflectionY;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = reflectionZ;
	}
	
	/**
	 * Sets a vector in {@link #vector3FArray_$private$3}.
	 * <p>
	 * The vector is constructed as the specular reflection vector of the direction vector represented by {@code directionX}, {@code directionY} and {@code directionZ} with regards to the normal vector represented by {@code normalX}, {@code normalY}
	 * and {@code normalZ}.
	 * <p>
	 * This method assumes that the direction vector is pointing towards the surface. It is facing it. This is usually the case for the direction of a ray that intersects the surface.
	 * 
	 * @param directionX the X-component of the direction vector
	 * @param directionY the Y-component of the direction vector
	 * @param directionZ the Z-component of the direction vector
	 * @param normalX the X-component of the normal vector
	 * @param normalY the Y-component of the normal vector
	 * @param normalZ the Z-component of the normal vector
	 */
	protected final void vector3FSetSpecularReflectionFacingSurface(final float directionX, final float directionY, final float directionZ, final float normalX, final float normalY, final float normalZ) {
		final float directionDotNormal = vector3FDotProduct(directionX, directionY, directionZ, normalX, normalY, normalZ);
		final float directionDotNormalMultipliedByTwo = directionDotNormal * 2.0F;
		
		final float reflectionX = directionX - normalX * directionDotNormalMultipliedByTwo;
		final float reflectionY = directionY - normalY * directionDotNormalMultipliedByTwo;
		final float reflectionZ = directionZ - normalZ * directionDotNormalMultipliedByTwo;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = reflectionX;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = reflectionY;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = reflectionZ;
	}
}