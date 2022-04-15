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

import org.dayflower.color.Color3F;

import org.macroing.java.util.Arrays;

/**
 * An {@code AbstractImageKernel} is an abstract extension of the {@code AbstractKernel} class that adds additional features.
 * <p>
 * The features added are the following:
 * <ul>
 * <li>Color conversion methods</li>
 * <li>Image rendering methods</li>
 * <li>Monte Carlo-method based image sampling using a stable Moving Average algorithm</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractImageKernel extends AbstractKernel {
	private static final float S_R_G_B_BREAK_POINT = 0.00304F;
	private static final float S_R_G_B_BREAK_POINT_MULTIPLIED_BY_SLOPE = 0.03928609F;
	private static final float S_R_G_B_GAMMA = 2.4F;
	private static final float S_R_G_B_GAMMA_RECIPROCAL = 0.41666666F;
	private static final float S_R_G_B_SEGMENT_OFFSET = 0.05500052F;
	private static final float S_R_G_B_SLOPE = 12.9230566F;
	private static final float S_R_G_B_SLOPE_MATCH = 1.05500054F;
	private static final float S_R_G_B_SLOPE_MATCH_RECIPROCAL = 0.9478668F;
	private static final float S_R_G_B_SLOPE_RECIPROCAL = 0.07738107F;
	private static final int COLOR_3_F_L_H_S_ARRAY_OFFSET_COMPONENT_1 = 0;
	private static final int COLOR_3_F_L_H_S_ARRAY_OFFSET_COMPONENT_2 = 1;
	private static final int COLOR_3_F_L_H_S_ARRAY_OFFSET_COMPONENT_3 = 2;
	private static final int COLOR_3_F_L_H_S_ARRAY_SIZE = 3;
	private static final int COLOR_3_F_R_H_S_ARRAY_OFFSET_COMPONENT_1 = 0;
	private static final int COLOR_3_F_R_H_S_ARRAY_OFFSET_COMPONENT_2 = 1;
	private static final int COLOR_3_F_R_H_S_ARRAY_OFFSET_COMPONENT_3 = 2;
	private static final int COLOR_3_F_R_H_S_ARRAY_SIZE = 3;
	private static final int FILM_FLAG_CLEAR = 0x0001;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * A {@code byte[]} with image colors.
	 */
	protected byte[] imageColorByteArray;
	
	/**
	 * A {@code float[]} that contains a color that consists of three components.
	 */
	protected float[] color3FLHSArray_$private$3;
	
	/**
	 * A {@code float[]} that contains a color that consists of three components.
	 */
	protected float[] color3FRHSArray_$private$3;
	
	/**
	 * A {@code float[]} with film colors.
	 */
	protected float[] filmColorFloatArray;
	
	/**
	 * A {@code float[]} with image colors.
	 */
	protected float[] imageColorFloatArray;
	
	/**
	 * An {@code int} with film flags.
	 */
	protected int filmFlags;
	
	/**
	 * An {@code int[]} with film samples.
	 */
	protected int[] filmSampleIntArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractImageKernel} instance.
	 */
	protected AbstractImageKernel() {
//		Initialize the color variable:
		this.color3FLHSArray_$private$3 = new float[COLOR_3_F_L_H_S_ARRAY_SIZE];
		this.color3FRHSArray_$private$3 = new float[COLOR_3_F_R_H_S_ARRAY_SIZE];
		
//		Initialize the film variables:
		this.filmColorFloatArray = new float[0];
		this.filmFlags = 0;
		this.filmSampleIntArray = new int[0];
		
//		Initialize the image variables:
		this.imageColorByteArray = new byte[0];
		this.imageColorFloatArray = new float[0];
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@code byte[]} with image colors.
	 * 
	 * @return the {@code byte[]} with image colors
	 */
	public final byte[] getImageColorByteArray() {
		return getAndReturn(this.imageColorByteArray);//TODO: Find out why this method crashes the JVM sometimes.
	}
	
	/**
	 * Returns the {@code float[]} with image colors.
	 * 
	 * @return the {@code float[]} with image colors
	 */
	public final float[] getImageColorFloatArray() {
		return getAndReturn(this.imageColorFloatArray);
	}
	
	/**
	 * Call this method to hint to this {@code AbstractImageKernel} instance that it should clear the film before rendering to it in the next render pass.
	 */
	public final void filmClear() {
		this.filmFlags |= FILM_FLAG_CLEAR;
	}
	
	/**
	 * Call this method to clear the film flags.
	 */
	public final void filmClearFilmFlags() {
		this.filmFlags = 0;
	}
	
	/**
	 * Sets up all necessary resources for this {@code AbstractImageKernel} instance.
	 * <p>
	 * If overriding this method, make sure to call this method using {@code super.setup();}.
	 */
	@Override
	public void setup() {
		super.setup();
		
		doSetupFilmColorFloatArray();
		doSetupFilmSampleIntArray();
		doSetupImageColorByteArray();
		doSetupImageColorFloatArray();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Color3F /////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the average component value.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 * @return the average component value
	 */
	@SuppressWarnings("static-method")
	protected final float color3FAverage(final float component1, final float component2, final float component3) {
		return (component1 + component2 + component3) / 3.0F;
	}
	
	/**
	 * Returns the lightness of the color.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 * @return the lightness of the color
	 */
	protected final float color3FLightness(final float component1, final float component2, final float component3) {
		return (max(component1, component2, component3) + min(component1, component2, component3)) / 2.0F;
	}
	
	/**
	 * Returns the relative luminance of the color.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 * @return the relative luminance of the color
	 */
	@SuppressWarnings("static-method")
	protected final float color3FLuminance(final float component1, final float component2, final float component3) {
		return component1 * 0.212671F + component2 * 0.715160F + component3 * 0.072169F;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Color3F - LHS ///////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the value of the B-component in {@link #color3FLHSArray_$private$3}.
	 * 
	 * @return the value of the B-component in {@link #color3FLHSArray_$private$3}
	 */
	protected final float color3FLHSGetB() {
		return this.color3FLHSArray_$private$3[COLOR_3_F_L_H_S_ARRAY_OFFSET_COMPONENT_3];
	}
	
	/**
	 * Returns the value of component 1 in {@link #color3FLHSArray_$private$3}.
	 * 
	 * @return the value of component 1 in {@link #color3FLHSArray_$private$3}
	 */
	protected final float color3FLHSGetComponent1() {
		return this.color3FLHSArray_$private$3[COLOR_3_F_L_H_S_ARRAY_OFFSET_COMPONENT_1];
	}
	
	/**
	 * Returns the value of component 2 in {@link #color3FLHSArray_$private$3}.
	 * 
	 * @return the value of component 2 in {@link #color3FLHSArray_$private$3}
	 */
	protected final float color3FLHSGetComponent2() {
		return this.color3FLHSArray_$private$3[COLOR_3_F_L_H_S_ARRAY_OFFSET_COMPONENT_2];
	}
	
	/**
	 * Returns the value of component 3 in {@link #color3FLHSArray_$private$3}.
	 * 
	 * @return the value of component 3 in {@link #color3FLHSArray_$private$3}
	 */
	protected final float color3FLHSGetComponent3() {
		return this.color3FLHSArray_$private$3[COLOR_3_F_L_H_S_ARRAY_OFFSET_COMPONENT_3];
	}
	
	/**
	 * Returns the value of the G-component in {@link #color3FLHSArray_$private$3}.
	 * 
	 * @return the value of the G-component in {@link #color3FLHSArray_$private$3}
	 */
	protected final float color3FLHSGetG() {
		return this.color3FLHSArray_$private$3[COLOR_3_F_L_H_S_ARRAY_OFFSET_COMPONENT_2];
	}
	
	/**
	 * Returns the value of the R-component in {@link #color3FLHSArray_$private$3}.
	 * 
	 * @return the value of the R-component in {@link #color3FLHSArray_$private$3}
	 */
	protected final float color3FLHSGetR() {
		return this.color3FLHSArray_$private$3[COLOR_3_F_L_H_S_ARRAY_OFFSET_COMPONENT_1];
	}
	
	/**
	 * Returns the value of the X-component in {@link #color3FLHSArray_$private$3}.
	 * 
	 * @return the value of the X-component in {@link #color3FLHSArray_$private$3}
	 */
	protected final float color3FLHSGetX() {
		return this.color3FLHSArray_$private$3[COLOR_3_F_L_H_S_ARRAY_OFFSET_COMPONENT_1];
	}
	
	/**
	 * Returns the value of the Y-component in {@link #color3FLHSArray_$private$3}.
	 * 
	 * @return the value of the Y-component in {@link #color3FLHSArray_$private$3}
	 */
	protected final float color3FLHSGetY() {
		return this.color3FLHSArray_$private$3[COLOR_3_F_L_H_S_ARRAY_OFFSET_COMPONENT_2];
	}
	
	/**
	 * Returns the value of the Z-component in {@link #color3FLHSArray_$private$3}.
	 * 
	 * @return the value of the Z-component in {@link #color3FLHSArray_$private$3}
	 */
	protected final float color3FLHSGetZ() {
		return this.color3FLHSArray_$private$3[COLOR_3_F_L_H_S_ARRAY_OFFSET_COMPONENT_3];
	}
	
	/**
	 * Sets a color in {@link #color3FLHSArray_$private$3}.
	 * <p>
	 * The color is constructed using the color represented by {@code component1}, {@code component2} and {@code component3}.
	 * 
	 * @param component1 the value of component 1 of the color
	 * @param component2 the value of component 2 of the color
	 * @param component3 the value of component 3 of the color
	 */
	protected final void color3FLHSSet(final float component1, final float component2, final float component3) {
		this.color3FLHSArray_$private$3[COLOR_3_F_L_H_S_ARRAY_OFFSET_COMPONENT_1] = component1;
		this.color3FLHSArray_$private$3[COLOR_3_F_L_H_S_ARRAY_OFFSET_COMPONENT_2] = component2;
		this.color3FLHSArray_$private$3[COLOR_3_F_L_H_S_ARRAY_OFFSET_COMPONENT_3] = component3;
	}
	
	/**
	 * Sets a color in {@link #color3FLHSArray_$private$3}.
	 * <p>
	 * The color is constructed by adding the color represented by {@code component1RHS}, {@code component2RHS} and {@code component3RHS} to the color represented by {@code component1LHS}, {@code component2LHS} and {@code component3LHS},
	 * component-wize.
	 * 
	 * @param component1LHS the value of component 1 of the color on the left-hand side
	 * @param component2LHS the value of component 2 of the color on the left-hand side
	 * @param component3LHS the value of component 3 of the color on the left-hand side
	 * @param component1RHS the value of component 1 of the color on the right-hand side
	 * @param component2RHS the value of component 2 of the color on the right-hand side
	 * @param component3RHS the value of component 3 of the color on the right-hand side
	 */
	protected final void color3FLHSSetAdd(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		final float component1 = component1LHS + component1RHS;
		final float component2 = component2LHS + component2RHS;
		final float component3 = component3LHS + component3RHS;
		
		color3FLHSSet(component1, component2, component3);
	}
	
	/**
	 * Sets a color in {@link #color3FLHSArray_$private$3}.
	 * <p>
	 * The color is constructed by blending the color in {@link #color3FLHSArray_$private$3} with the color in {@link #color3FRHSArray_$private$3} using the factors {@code tComponent1}, {@code tComponent2} and {@code tComponent3}, respectively.
	 * 
	 * @param tComponent1 the factor to use for component 1 in the blending process
	 * @param tComponent2 the factor to use for component 2 in the blending process
	 * @param tComponent3 the factor to use for component 3 in the blending process
	 */
	protected final void color3FLHSSetBlend(final float tComponent1, final float tComponent2, final float tComponent3) {
		final float colorLHSComponent1 = color3FLHSGetComponent1();
		final float colorLHSComponent2 = color3FLHSGetComponent2();
		final float colorLHSComponent3 = color3FLHSGetComponent3();
		
		final float colorRHSComponent1 = color3FRHSGetComponent1();
		final float colorRHSComponent2 = color3FRHSGetComponent2();
		final float colorRHSComponent3 = color3FRHSGetComponent3();
		
		final float component1 = lerp(colorLHSComponent1, colorRHSComponent1, tComponent1);
		final float component2 = lerp(colorLHSComponent2, colorRHSComponent2, tComponent2);
		final float component3 = lerp(colorLHSComponent3, colorRHSComponent3, tComponent3);
		
		color3FLHSSet(component1, component2, component3);
	}
	
	/**
	 * Sets a color in {@link #color3FLHSArray_$private$3}.
	 * <p>
	 * The color is constructed by converting the color represented by {@code component1}, {@code component2} and {@code component3} from the XYZ color space to the RGB color space sRGB.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	protected final void color3FLHSSetConvertXYZToRGB(final float component1, final float component2, final float component3) {
		final float newComponent1 = +3.24100423F * component1 + -1.53739941F * component2 + -0.49861607F * component3;
		final float newComponent2 = -0.96922410F * component1 + +1.87592983F * component2 + +0.04155424F * component3;
		final float newComponent3 = +0.05563942F * component1 + -0.20401107F * component2 + +1.05714858F * component3;
		
		color3FLHSSet(newComponent1, newComponent2, newComponent3);
	}
	
	/**
	 * Sets a color in {@link #color3FLHSArray_$private$3}.
	 * <p>
	 * The color is constructed by dividing the color represented by {@code component1LHS}, {@code component2LHS} and {@code component3LHS} with the color represented by {@code component1RHS}, {@code component2RHS} and {@code component3RHS},
	 * component-wize.
	 * 
	 * @param component1LHS the value of component 1 of the color on the left-hand side
	 * @param component2LHS the value of component 2 of the color on the left-hand side
	 * @param component3LHS the value of component 3 of the color on the left-hand side
	 * @param component1RHS the value of component 1 of the color on the right-hand side
	 * @param component2RHS the value of component 2 of the color on the right-hand side
	 * @param component3RHS the value of component 3 of the color on the right-hand side
	 */
	protected final void color3FLHSSetDivide(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		final float component1 = component1LHS / component1RHS;
		final float component2 = component2LHS / component2RHS;
		final float component3 = component3LHS / component3RHS;
		
		color3FLHSSet(component1, component2, component3);
	}
	
	/**
	 * Sets a color in {@link #color3FLHSArray_$private$3}.
	 * <p>
	 * This method works like {@link Color3F#maximumTo1(Color3F)}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	protected final void color3FLHSSetMaximumTo1(final float component1, final float component2, final float component3) {
		final float maximum = max(component1, component2, component3);
		
		if(maximum > 1.0F) {
			color3FLHSSet(component1 / maximum, component2 / maximum, component3 / maximum);
		} else {
			color3FLHSSet(component1, component2, component3);
		}
	}
	
	/**
	 * Sets a color in {@link #color3FLHSArray_$private$3}.
	 * <p>
	 * This method works like {@link Color3F#minimumTo0(Color3F)}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	protected final void color3FLHSSetMinimumTo0(final float component1, final float component2, final float component3) {
		final float minimum = min(component1, component2, component3);
		
		if(minimum < 0.0F) {
			color3FLHSSet(component1 + -minimum, component2 + -minimum, component3 + -minimum);
		} else {
			color3FLHSSet(component1, component2, component3);
		}
	}
	
	/**
	 * Sets a color in {@link #color3FLHSArray_$private$3}.
	 * <p>
	 * The color is constructed by multiplying the color represented by {@code component1LHS}, {@code component2LHS} and {@code component3LHS} with the color represented by {@code component1RHS}, {@code component2RHS} and {@code component3RHS},
	 * component-wize.
	 * 
	 * @param component1LHS the value of component 1 of the color on the left-hand side
	 * @param component2LHS the value of component 2 of the color on the left-hand side
	 * @param component3LHS the value of component 3 of the color on the left-hand side
	 * @param component1RHS the value of component 1 of the color on the right-hand side
	 * @param component2RHS the value of component 2 of the color on the right-hand side
	 * @param component3RHS the value of component 3 of the color on the right-hand side
	 */
	protected final void color3FLHSSetMultiply(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		final float component1 = component1LHS * component1RHS;
		final float component2 = component2LHS * component2RHS;
		final float component3 = component3LHS * component3RHS;
		
		color3FLHSSet(component1, component2, component3);
	}
	
	/**
	 * Sets a color in {@link #color3FLHSArray_$private$3}.
	 * <p>
	 * The color is constructed by subtracting the color represented by {@code component1RHS}, {@code component2RHS} and {@code component3RHS} from the color represented by {@code component1LHS}, {@code component2LHS} and {@code component3LHS},
	 * component-wize.
	 * 
	 * @param component1LHS the value of component 1 of the color on the left-hand side
	 * @param component2LHS the value of component 2 of the color on the left-hand side
	 * @param component3LHS the value of component 3 of the color on the left-hand side
	 * @param component1RHS the value of component 1 of the color on the right-hand side
	 * @param component2RHS the value of component 2 of the color on the right-hand side
	 * @param component3RHS the value of component 3 of the color on the right-hand side
	 */
	protected final void color3FLHSSetSubtract(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		final float component1 = component1LHS - component1RHS;
		final float component2 = component2LHS - component2RHS;
		final float component3 = component3LHS - component3RHS;
		
		color3FLHSSet(component1, component2, component3);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Color3F - RHS ///////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the value of the B-component in {@link #color3FRHSArray_$private$3}.
	 * 
	 * @return the value of the B-component in {@link #color3FRHSArray_$private$3}
	 */
	protected final float color3FRHSGetB() {
		return this.color3FRHSArray_$private$3[COLOR_3_F_R_H_S_ARRAY_OFFSET_COMPONENT_3];
	}
	
	/**
	 * Returns the value of component 1 in {@link #color3FRHSArray_$private$3}.
	 * 
	 * @return the value of component 1 in {@link #color3FRHSArray_$private$3}
	 */
	protected final float color3FRHSGetComponent1() {
		return this.color3FRHSArray_$private$3[COLOR_3_F_R_H_S_ARRAY_OFFSET_COMPONENT_1];
	}
	
	/**
	 * Returns the value of component 2 in {@link #color3FRHSArray_$private$3}.
	 * 
	 * @return the value of component 2 in {@link #color3FRHSArray_$private$3}
	 */
	protected final float color3FRHSGetComponent2() {
		return this.color3FRHSArray_$private$3[COLOR_3_F_R_H_S_ARRAY_OFFSET_COMPONENT_2];
	}
	
	/**
	 * Returns the value of component 3 in {@link #color3FRHSArray_$private$3}.
	 * 
	 * @return the value of component 3 in {@link #color3FRHSArray_$private$3}
	 */
	protected final float color3FRHSGetComponent3() {
		return this.color3FRHSArray_$private$3[COLOR_3_F_R_H_S_ARRAY_OFFSET_COMPONENT_3];
	}
	
	/**
	 * Returns the value of the G-component in {@link #color3FRHSArray_$private$3}.
	 * 
	 * @return the value of the G-component in {@link #color3FRHSArray_$private$3}
	 */
	protected final float color3FRHSGetG() {
		return this.color3FRHSArray_$private$3[COLOR_3_F_R_H_S_ARRAY_OFFSET_COMPONENT_2];
	}
	
	/**
	 * Returns the value of the R-component in {@link #color3FRHSArray_$private$3}.
	 * 
	 * @return the value of the R-component in {@link #color3FRHSArray_$private$3}
	 */
	protected final float color3FRHSGetR() {
		return this.color3FRHSArray_$private$3[COLOR_3_F_R_H_S_ARRAY_OFFSET_COMPONENT_1];
	}
	
	/**
	 * Returns the value of the X-component in {@link #color3FRHSArray_$private$3}.
	 * 
	 * @return the value of the X-component in {@link #color3FRHSArray_$private$3}
	 */
	protected final float color3FRHSGetX() {
		return this.color3FRHSArray_$private$3[COLOR_3_F_R_H_S_ARRAY_OFFSET_COMPONENT_1];
	}
	
	/**
	 * Returns the value of the Y-component in {@link #color3FRHSArray_$private$3}.
	 * 
	 * @return the value of the Y-component in {@link #color3FRHSArray_$private$3}
	 */
	protected final float color3FRHSGetY() {
		return this.color3FRHSArray_$private$3[COLOR_3_F_R_H_S_ARRAY_OFFSET_COMPONENT_2];
	}
	
	/**
	 * Returns the value of the Z-component in {@link #color3FRHSArray_$private$3}.
	 * 
	 * @return the value of the Z-component in {@link #color3FRHSArray_$private$3}
	 */
	protected final float color3FRHSGetZ() {
		return this.color3FRHSArray_$private$3[COLOR_3_F_R_H_S_ARRAY_OFFSET_COMPONENT_3];
	}
	
	/**
	 * Sets a color in {@link #color3FRHSArray_$private$3}.
	 * <p>
	 * The color is constructed using the color represented by {@code component1}, {@code component2} and {@code component3}.
	 * 
	 * @param component1 the value of component 1 of the color
	 * @param component2 the value of component 2 of the color
	 * @param component3 the value of component 3 of the color
	 */
	protected final void color3FRHSSet(final float component1, final float component2, final float component3) {
		this.color3FRHSArray_$private$3[COLOR_3_F_R_H_S_ARRAY_OFFSET_COMPONENT_1] = component1;
		this.color3FRHSArray_$private$3[COLOR_3_F_R_H_S_ARRAY_OFFSET_COMPONENT_2] = component2;
		this.color3FRHSArray_$private$3[COLOR_3_F_R_H_S_ARRAY_OFFSET_COMPONENT_3] = component3;
	}
	
	/**
	 * Sets a color in {@link #color3FRHSArray_$private$3}.
	 * <p>
	 * The color is constructed by adding the color represented by {@code component1RHS}, {@code component2RHS} and {@code component3RHS} to the color represented by {@code component1LHS}, {@code component2LHS} and {@code component3LHS},
	 * component-wize.
	 * 
	 * @param component1LHS the value of component 1 of the color on the left-hand side
	 * @param component2LHS the value of component 2 of the color on the left-hand side
	 * @param component3LHS the value of component 3 of the color on the left-hand side
	 * @param component1RHS the value of component 1 of the color on the right-hand side
	 * @param component2RHS the value of component 2 of the color on the right-hand side
	 * @param component3RHS the value of component 3 of the color on the right-hand side
	 */
	protected final void color3FRHSSetAdd(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		final float component1 = component1LHS + component1RHS;
		final float component2 = component2LHS + component2RHS;
		final float component3 = component3LHS + component3RHS;
		
		color3FRHSSet(component1, component2, component3);
	}
	
	/**
	 * Sets a color in {@link #color3FRHSArray_$private$3}.
	 * <p>
	 * The color is constructed by blending the color in {@link #color3FLHSArray_$private$3} with the color in {@link #color3FRHSArray_$private$3} using the factors {@code tComponent1}, {@code tComponent2} and {@code tComponent3}, respectively.
	 * 
	 * @param tComponent1 the factor to use for component 1 in the blending process
	 * @param tComponent2 the factor to use for component 2 in the blending process
	 * @param tComponent3 the factor to use for component 3 in the blending process
	 */
	protected final void color3FRHSSetBlend(final float tComponent1, final float tComponent2, final float tComponent3) {
		final float colorLHSComponent1 = color3FLHSGetComponent1();
		final float colorLHSComponent2 = color3FLHSGetComponent2();
		final float colorLHSComponent3 = color3FLHSGetComponent3();
		
		final float colorRHSComponent1 = color3FRHSGetComponent1();
		final float colorRHSComponent2 = color3FRHSGetComponent2();
		final float colorRHSComponent3 = color3FRHSGetComponent3();
		
		final float component1 = lerp(colorLHSComponent1, colorRHSComponent1, tComponent1);
		final float component2 = lerp(colorLHSComponent2, colorRHSComponent2, tComponent2);
		final float component3 = lerp(colorLHSComponent3, colorRHSComponent3, tComponent3);
		
		color3FRHSSet(component1, component2, component3);
	}
	
	/**
	 * Sets a color in {@link #color3FRHSArray_$private$3}.
	 * <p>
	 * The color is constructed by converting the color represented by {@code component1}, {@code component2} and {@code component3} from the XYZ color space to the RGB color space sRGB.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	protected final void color3FRHSSetConvertXYZToRGB(final float component1, final float component2, final float component3) {
		final float newComponent1 = +3.24100423F * component1 + -1.53739941F * component2 + -0.49861607F * component3;
		final float newComponent2 = -0.96922410F * component1 + +1.87592983F * component2 + +0.04155424F * component3;
		final float newComponent3 = +0.05563942F * component1 + -0.20401107F * component2 + +1.05714858F * component3;
		
		color3FRHSSet(newComponent1, newComponent2, newComponent3);
	}
	
	/**
	 * Sets a color in {@link #color3FRHSArray_$private$3}.
	 * <p>
	 * The color is constructed by dividing the color represented by {@code component1LHS}, {@code component2LHS} and {@code component3LHS} with the color represented by {@code component1RHS}, {@code component2RHS} and {@code component3RHS},
	 * component-wize.
	 * 
	 * @param component1LHS the value of component 1 of the color on the left-hand side
	 * @param component2LHS the value of component 2 of the color on the left-hand side
	 * @param component3LHS the value of component 3 of the color on the left-hand side
	 * @param component1RHS the value of component 1 of the color on the right-hand side
	 * @param component2RHS the value of component 2 of the color on the right-hand side
	 * @param component3RHS the value of component 3 of the color on the right-hand side
	 */
	protected final void color3FRHSSetDivide(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		final float component1 = component1LHS / component1RHS;
		final float component2 = component2LHS / component2RHS;
		final float component3 = component3LHS / component3RHS;
		
		color3FRHSSet(component1, component2, component3);
	}
	
	/**
	 * Sets a color in {@link #color3FRHSArray_$private$3}.
	 * <p>
	 * This method works like {@link Color3F#maximumTo1(Color3F)}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	protected final void color3FRHSSetMaximumTo1(final float component1, final float component2, final float component3) {
		final float maximum = max(component1, component2, component3);
		
		if(maximum > 1.0F) {
			color3FRHSSet(component1 / maximum, component2 / maximum, component3 / maximum);
		} else {
			color3FRHSSet(component1, component2, component3);
		}
	}
	
	/**
	 * Sets a color in {@link #color3FRHSArray_$private$3}.
	 * <p>
	 * This method works like {@link Color3F#minimumTo0(Color3F)}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	protected final void color3FRHSSetMinimumTo0(final float component1, final float component2, final float component3) {
		final float minimum = min(component1, component2, component3);
		
		if(minimum < 0.0F) {
			color3FRHSSet(component1 + -minimum, component2 + -minimum, component3 + -minimum);
		} else {
			color3FRHSSet(component1, component2, component3);
		}
	}
	
	/**
	 * Sets a color in {@link #color3FRHSArray_$private$3}.
	 * <p>
	 * The color is constructed by multiplying the color represented by {@code component1LHS}, {@code component2LHS} and {@code component3LHS} with the color represented by {@code component1RHS}, {@code component2RHS} and {@code component3RHS},
	 * component-wize.
	 * 
	 * @param component1LHS the value of component 1 of the color on the left-hand side
	 * @param component2LHS the value of component 2 of the color on the left-hand side
	 * @param component3LHS the value of component 3 of the color on the left-hand side
	 * @param component1RHS the value of component 1 of the color on the right-hand side
	 * @param component2RHS the value of component 2 of the color on the right-hand side
	 * @param component3RHS the value of component 3 of the color on the right-hand side
	 */
	protected final void color3FRHSSetMultiply(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		final float component1 = component1LHS * component1RHS;
		final float component2 = component2LHS * component2RHS;
		final float component3 = component3LHS * component3RHS;
		
		color3FRHSSet(component1, component2, component3);
	}
	
	/**
	 * Sets a color in {@link #color3FRHSArray_$private$3}.
	 * <p>
	 * The color is constructed by subtracting the color represented by {@code component1RHS}, {@code component2RHS} and {@code component3RHS} from the color represented by {@code component1LHS}, {@code component2LHS} and {@code component3LHS},
	 * component-wize.
	 * 
	 * @param component1LHS the value of component 1 of the color on the left-hand side
	 * @param component2LHS the value of component 2 of the color on the left-hand side
	 * @param component3LHS the value of component 3 of the color on the left-hand side
	 * @param component1RHS the value of component 1 of the color on the right-hand side
	 * @param component2RHS the value of component 2 of the color on the right-hand side
	 * @param component3RHS the value of component 3 of the color on the right-hand side
	 */
	protected final void color3FRHSSetSubtract(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		final float component1 = component1LHS - component1RHS;
		final float component2 = component2LHS - component2RHS;
		final float component3 = component3LHS - component3RHS;
		
		color3FRHSSet(component1, component2, component3);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Color3F - Utility ///////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the B-component of {@code colorRGB} as a {@code float} in the interval [0.0, 1.0].
	 * 
	 * @param colorRGB an {@code int} with a color in packed form using either RGB or ARGB as its order
	 * @return the B-component of {@code colorRGB} as a {@code float} in the interval [0.0, 1.0]
	 */
	protected final float colorRGBIntToBFloat(final int colorRGB) {
		return colorRGBIntToBInt(colorRGB) / 255.0F;
	}
	
	/**
	 * Returns the G-component of {@code colorRGB} as a {@code float} in the interval [0.0, 1.0].
	 * 
	 * @param colorRGB an {@code int} with a color in packed form using either RGB or ARGB as its order
	 * @return the G-component of {@code colorRGB} as a {@code float} in the interval [0.0, 1.0]
	 */
	protected final float colorRGBIntToGFloat(final int colorRGB) {
		return colorRGBIntToGInt(colorRGB) / 255.0F;
	}
	
	/**
	 * Returns the R-component of {@code colorRGB} as a {@code float} in the interval [0.0, 1.0].
	 * 
	 * @param colorRGB an {@code int} with a color in packed form using either RGB or ARGB as its order
	 * @return the R-component of {@code colorRGB} as a {@code float} in the interval [0.0, 1.0]
	 */
	protected final float colorRGBIntToRFloat(final int colorRGB) {
		return colorRGBIntToRInt(colorRGB) / 255.0F;
	}
	
	/**
	 * Returns an {@code int} representation of {@code colorB}.
	 * <p>
	 * If {@code colorB} is outside the interval [0.0, 1.0], it will be saturated.
	 * 
	 * @param colorB a {@code float} with the B-component
	 * @return an {@code int} representation of {@code colorB}
	 */
	protected final int colorBFloatToBInt(final float colorB) {
		return (int)(saturateF(colorB, 0.0F, 1.0F) * 255.0F + 0.5F);
	}
	
	/**
	 * Returns an {@code int} representation of {@code colorG}.
	 * <p>
	 * If {@code colorG} is outside the interval [0.0, 1.0], it will be saturated.
	 * 
	 * @param colorG a {@code float} with the G-component
	 * @return an {@code int} representation of {@code colorG}
	 */
	protected final int colorGFloatToGInt(final float colorG) {
		return (int)(saturateF(colorG, 0.0F, 1.0F) * 255.0F + 0.5F);
	}
	
	/**
	 * Returns an {@code int} representation of {@code colorR}.
	 * <p>
	 * If {@code colorR} is outside the interval [0.0, 1.0], it will be saturated.
	 * 
	 * @param colorR a {@code float} with the R-component
	 * @return an {@code int} representation of {@code colorR}
	 */
	protected final int colorRFloatToRInt(final float colorR) {
		return (int)(saturateF(colorR, 0.0F, 1.0F) * 255.0F + 0.5F);
	}
	
	/**
	 * Returns an {@code int} with the color represented by {@code colorR}, {@code colorG} and {@code colorB} in packed form using either RGB or ARGB as its order.
	 * <p>
	 * If either {@code colorR}, {@code colorG} or {@code colorB} are outside the interval [0.0, 1.0], they will be saturated.
	 * 
	 * @param colorR a {@code float} with the R-component
	 * @param colorG a {@code float} with the G-component
	 * @param colorB a {@code float} with the B-component
	 * @return an {@code int} with the color represented by {@code colorR}, {@code colorG} and {@code colorB} in packed form using either RGB or ARGB as its order
	 */
	protected final int colorRGBFloatToRGBInt(final float colorR, final float colorG, final float colorB) {
		return colorRGBIntToRGBInt(colorRFloatToRInt(colorR), colorGFloatToGInt(colorG), colorBFloatToBInt(colorB));
	}
	
	/**
	 * Returns an {@code int} with the color represented by {@code colorR}, {@code colorG} and {@code colorB} in packed form using either RGB or ARGB as its order.
	 * <p>
	 * If either {@code colorR}, {@code colorG} or {@code colorB} are outside the interval [0, 255], they will be saturated.
	 * 
	 * @param colorR an {@code int} with the R-component
	 * @param colorG an {@code int} with the G-component
	 * @param colorB an {@code int} with the B-component
	 * @return an {@code int} with the color represented by {@code colorR}, {@code colorG} and {@code colorB} in packed form using either RGB or ARGB as its order
	 */
	protected final int colorRGBIntToRGBInt(final int colorR, final int colorG, final int colorB) {
		final int colorRSaturated = saturateI(colorR, 0, 255);
		final int colorGSaturated = saturateI(colorG, 0, 255);
		final int colorBSaturated = saturateI(colorB, 0, 255);
		
		final int colorRGB = (colorRSaturated << 16) | (colorGSaturated << 8) | (colorBSaturated << 0);
		
		return colorRGB;
	}
	
	/**
	 * Returns the B-component of {@code colorRGB} as an {@code int} in the interval [0, 255].
	 * 
	 * @param colorRGB an {@code int} with a color in packed form using either RGB or ARGB as its order
	 * @return the B-component of {@code colorRGB} as an {@code int} in the interval [0, 255]
	 */
	@SuppressWarnings("static-method")
	protected final int colorRGBIntToBInt(final int colorRGB) {
		return (colorRGB >>  0) & 0xFF;
	}
	
	/**
	 * Returns the G-component of {@code colorRGB} as an {@code int} in the interval [0, 255].
	 * 
	 * @param colorRGB an {@code int} with a color in packed form using either RGB or ARGB as its order
	 * @return the G-component of {@code colorRGB} as an {@code int} in the interval [0, 255]
	 */
	@SuppressWarnings("static-method")
	protected final int colorRGBIntToGInt(final int colorRGB) {
		return (colorRGB >>  8) & 0xFF;
	}
	
	/**
	 * Returns the R-component of {@code colorRGB} as an {@code int} in the interval [0, 255].
	 * 
	 * @param colorRGB an {@code int} with a color in packed form using either RGB or ARGB as its order
	 * @return the R-component of {@code colorRGB} as an {@code int} in the interval [0, 255]
	 */
	@SuppressWarnings("static-method")
	protected final int colorRGBIntToRInt(final int colorRGB) {
		return (colorRGB >> 16) & 0xFF;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Film ////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the resolution along the X-axis of the film.
	 * 
	 * @return the resolution along the X-axis of the film
	 */
	protected final int filmGetResolutionX() {
		return super.resolutionX;
	}
	
	/**
	 * Returns the resolution along the Y-axis of the film.
	 * 
	 * @return the resolution along the Y-axis of the film
	 */
	protected final int filmGetResolutionY() {
		return super.resolutionY;
	}
	
	/**
	 * Returns the X-coordinate of the film.
	 * 
	 * @return the X-coordinate of the film
	 */
	protected final int filmGetX() {
		return getGlobalId() % super.resolutionX;
	}
	
	/**
	 * Returns the Y-coordinate of the film.
	 * 
	 * @return the Y-coordinate of the film
	 */
	protected final int filmGetY() {
		return getGlobalId() / super.resolutionX;
	}
	
	/**
	 * Adds the sample RGB component values {@code colorR}, {@code colorG} and {@code colorB} to the current pixel of the film.
	 * <p>
	 * This method is useful for Monte Carlo-method based rendering. It should be avoided if you already know the final RGB color.
	 * <p>
	 * The current moving average algorithm used by this method is stable enough, that adding the same RGB color repeatedly won't cause major precision loss.
	 * 
	 * @param colorR the value of the R-component
	 * @param colorG the value of the G-component
	 * @param colorB the value of the B-component
	 */
	protected final void filmAddColor(final float colorR, final float colorG, final float colorB) {
		final int filmColorFloatArrayOffset = getGlobalId() * 3;
		final int filmSampleIntArrayOffset = getGlobalId();
		
		if((this.filmFlags & FILM_FLAG_CLEAR) != 0) {
			this.filmColorFloatArray[filmColorFloatArrayOffset + 0] = colorR;
			this.filmColorFloatArray[filmColorFloatArrayOffset + 1] = colorG;
			this.filmColorFloatArray[filmColorFloatArrayOffset + 2] = colorB;
			this.filmSampleIntArray[filmSampleIntArrayOffset] = 1;
		} else {
			final int oldFilmSample = this.filmSampleIntArray[filmSampleIntArrayOffset];
			final int newFilmSample = oldFilmSample + 1;
			
			final float oldFilmColorR = this.filmColorFloatArray[filmColorFloatArrayOffset + 0];
			final float oldFilmColorG = this.filmColorFloatArray[filmColorFloatArrayOffset + 1];
			final float oldFilmColorB = this.filmColorFloatArray[filmColorFloatArrayOffset + 2];
			
			final float newFilmColorR = oldFilmColorR + ((colorR - oldFilmColorR) / newFilmSample);
			final float newFilmColorG = oldFilmColorG + ((colorG - oldFilmColorG) / newFilmSample);
			final float newFilmColorB = oldFilmColorB + ((colorB - oldFilmColorB) / newFilmSample);
			
			this.filmColorFloatArray[filmColorFloatArrayOffset + 0] = newFilmColorR;
			this.filmColorFloatArray[filmColorFloatArrayOffset + 1] = newFilmColorG;
			this.filmColorFloatArray[filmColorFloatArrayOffset + 2] = newFilmColorB;
			this.filmSampleIntArray[filmSampleIntArrayOffset] = newFilmSample;
		}
	}
	
	/**
	 * Sets the RGB component values {@code colorR}, {@code colorG} and {@code colorB} for the current pixel of the film.
	 * <p>
	 * This method is useful if you already know the final RGB color. It should be avoided if you're using Monte Carlo-method based rendering.
	 * 
	 * @param colorR the value of the R-component
	 * @param colorG the value of the G-component
	 * @param colorB the value of the B-component
	 */
	protected final void filmSetColor(final float colorR, final float colorG, final float colorB) {
		final int filmColorFloatArrayOffset = getGlobalId() * 3;
		final int filmSampleIntArrayOffset = getGlobalId();
		
		this.filmColorFloatArray[filmColorFloatArrayOffset + 0] = colorR;
		this.filmColorFloatArray[filmColorFloatArrayOffset + 1] = colorG;
		this.filmColorFloatArray[filmColorFloatArrayOffset + 2] = colorB;
		this.filmSampleIntArray[filmSampleIntArrayOffset] = 1;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// ImageF //////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float} with the B-component of the image at the current pixel.
	 * 
	 * @return a {@code float} with the B-component of the image at the current pixel
	 */
	protected final float imageGetColorB() {
		return this.imageColorFloatArray[getGlobalId() * 3 + 2];
	}
	
	/**
	 * Returns a {@code float} with the G-component of the image at the current pixel.
	 * 
	 * @return a {@code float} with the G-component of the image at the current pixel
	 */
	protected final float imageGetColorG() {
		return this.imageColorFloatArray[getGlobalId() * 3 + 1];
	}
	
	/**
	 * Returns a {@code float} with the R-component of the image at the current pixel.
	 * 
	 * @return a {@code float} with the R-component of the image at the current pixel
	 */
	protected final float imageGetColorR() {
		return this.imageColorFloatArray[getGlobalId() * 3 + 0];
	}
	
	/**
	 * Returns the resolution along the X-axis of the image.
	 * 
	 * @return the resolution along the X-axis of the image
	 */
	protected final int imageGetResolutionX() {
		return super.resolutionX;
	}
	
	/**
	 * Returns the resolution along the Y-axis of the image.
	 * 
	 * @return the resolution along the Y-axis of the image
	 */
	protected final int imageGetResolutionY() {
		return super.resolutionY;
	}
	
	/**
	 * Returns the X-coordinate of the image.
	 * 
	 * @return the X-coordinate of the image
	 */
	protected final int imageGetX() {
		return getGlobalId() % super.resolutionX;
	}
	
	/**
	 * Returns the Y-coordinate of the image.
	 * 
	 * @return the Y-coordinate of the image
	 */
	protected final int imageGetY() {
		return getGlobalId() / super.resolutionX;
	}
	
	/**
	 * Adds the RGB component values {@code colorR}, {@code colorG} and {@code colorB} to the current pixel of the image.
	 * 
	 * @param colorR the value of the R-component
	 * @param colorG the value of the G-component
	 * @param colorB the value of the B-component
	 */
	protected final void imageAddColor(final float colorR, final float colorG, final float colorB) {
		final int imageColorFloatArrayOffset = getGlobalId() * 3;
		
		final float oldColorR = this.imageColorFloatArray[imageColorFloatArrayOffset + 0];
		final float oldColorG = this.imageColorFloatArray[imageColorFloatArrayOffset + 1];
		final float oldColorB = this.imageColorFloatArray[imageColorFloatArrayOffset + 2];
		
		final float newColorR = oldColorR + colorR;
		final float newColorG = oldColorG + colorG;
		final float newColorB = oldColorB + colorB;
		
		this.imageColorFloatArray[imageColorFloatArrayOffset + 0] = newColorR;
		this.imageColorFloatArray[imageColorFloatArrayOffset + 1] = newColorG;
		this.imageColorFloatArray[imageColorFloatArrayOffset + 2] = newColorB;
	}
	
	/**
	 * The image processing stage begins.
	 * <p>
	 * This method copies the current film color into an image color buffer. The image color buffer can be manipulated by other image processing methods.
	 * <p>
	 * When the image processing is done, {@link #imageEnd()} has to be called in order to end the image processing stage.
	 */
	protected final void imageBegin() {
		final int filmColorFloatArrayOffset = getGlobalId() * 3;
		final int imageColorFloatArrayOffset = getGlobalId() * 3;
		
		final float colorR = this.filmColorFloatArray[filmColorFloatArrayOffset + 0];
		final float colorG = this.filmColorFloatArray[filmColorFloatArrayOffset + 1];
		final float colorB = this.filmColorFloatArray[filmColorFloatArrayOffset + 2];
		
		this.imageColorFloatArray[imageColorFloatArrayOffset + 0] = colorR;
		this.imageColorFloatArray[imageColorFloatArrayOffset + 1] = colorG;
		this.imageColorFloatArray[imageColorFloatArrayOffset + 2] = colorB;
	}
	
	/**
	 * The image processing stage ends.
	 * <p>
	 * This method copies the current image color to its final destination so it can be displayed.
	 */
	protected final void imageEnd() {
		final int imageColorByteArrayOffset = getGlobalId() * 4;
		final int imageColorFloatArrayOffset = getGlobalId() * 3;
		
		final float imageColorFloatR = this.imageColorFloatArray[imageColorFloatArrayOffset + 0];
		final float imageColorFloatG = this.imageColorFloatArray[imageColorFloatArrayOffset + 1];
		final float imageColorFloatB = this.imageColorFloatArray[imageColorFloatArrayOffset + 2];
		
		final int imageColorIntR = (int)(min(max(imageColorFloatR, 0.0F), 1.0F) * 255.0F + 0.5F);
		final int imageColorIntG = (int)(min(max(imageColorFloatG, 0.0F), 1.0F) * 255.0F + 0.5F);
		final int imageColorIntB = (int)(min(max(imageColorFloatB, 0.0F), 1.0F) * 255.0F + 0.5F);
		final int imageColorIntA = 255;
		
		this.imageColorByteArray[imageColorByteArrayOffset + 0] = (byte)(imageColorIntR);
		this.imageColorByteArray[imageColorByteArrayOffset + 1] = (byte)(imageColorIntG);
		this.imageColorByteArray[imageColorByteArrayOffset + 2] = (byte)(imageColorIntB);
		this.imageColorByteArray[imageColorByteArrayOffset + 3] = (byte)(imageColorIntA);
	}
	
	/**
	 * Redoes the gamma correction for the current pixel of the image using sRGB.
	 * <p>
	 * This method assumes the RGB color for the current pixel is linear.
	 */
	protected final void imageRedoGammaCorrection() {
		final int imageColorFloatArrayOffset = getGlobalId() * 3;
		
		final float oldColorR = this.imageColorFloatArray[imageColorFloatArrayOffset + 0];
		final float oldColorG = this.imageColorFloatArray[imageColorFloatArrayOffset + 1];
		final float oldColorB = this.imageColorFloatArray[imageColorFloatArrayOffset + 2];
		
		final float newColorR = oldColorR <= S_R_G_B_BREAK_POINT ? oldColorR * S_R_G_B_SLOPE : S_R_G_B_SLOPE_MATCH * pow(oldColorR, S_R_G_B_GAMMA_RECIPROCAL) - S_R_G_B_SEGMENT_OFFSET;
		final float newColorG = oldColorG <= S_R_G_B_BREAK_POINT ? oldColorG * S_R_G_B_SLOPE : S_R_G_B_SLOPE_MATCH * pow(oldColorG, S_R_G_B_GAMMA_RECIPROCAL) - S_R_G_B_SEGMENT_OFFSET;
		final float newColorB = oldColorB <= S_R_G_B_BREAK_POINT ? oldColorB * S_R_G_B_SLOPE : S_R_G_B_SLOPE_MATCH * pow(oldColorB, S_R_G_B_GAMMA_RECIPROCAL) - S_R_G_B_SEGMENT_OFFSET;
		
		this.imageColorFloatArray[imageColorFloatArrayOffset + 0] = newColorR;
		this.imageColorFloatArray[imageColorFloatArrayOffset + 1] = newColorG;
		this.imageColorFloatArray[imageColorFloatArrayOffset + 2] = newColorB;
	}
	
	/**
	 * Sets the RGB component values {@code colorR}, {@code colorG} and {@code colorB} for the current pixel of the image.
	 * 
	 * @param colorR the value of the R-component
	 * @param colorG the value of the G-component
	 * @param colorB the value of the B-component
	 */
	protected final void imageSetColor(final float colorR, final float colorG, final float colorB) {
		final int imageColorFloatArrayOffset = getGlobalId() * 3;
		
		this.imageColorFloatArray[imageColorFloatArrayOffset + 0] = colorR;
		this.imageColorFloatArray[imageColorFloatArrayOffset + 1] = colorG;
		this.imageColorFloatArray[imageColorFloatArrayOffset + 2] = colorB;
	}
	
	/**
	 * Undoes the gamma correction for the current pixel of the image using sRGB.
	 * <p>
	 * This method assumes the RGB color for the current pixel is non-linear.
	 */
	protected final void imageUndoGammaCorrection() {
		final int imageColorFloatArrayOffset = getGlobalId() * 3;
		
		final float oldColorR = this.imageColorFloatArray[imageColorFloatArrayOffset + 0];
		final float oldColorG = this.imageColorFloatArray[imageColorFloatArrayOffset + 1];
		final float oldColorB = this.imageColorFloatArray[imageColorFloatArrayOffset + 2];
		
		final float newColorR = oldColorR <= S_R_G_B_BREAK_POINT_MULTIPLIED_BY_SLOPE ? oldColorR * S_R_G_B_SLOPE_RECIPROCAL : pow((oldColorR + S_R_G_B_SEGMENT_OFFSET) * S_R_G_B_SLOPE_MATCH_RECIPROCAL, S_R_G_B_GAMMA);
		final float newColorG = oldColorG <= S_R_G_B_BREAK_POINT_MULTIPLIED_BY_SLOPE ? oldColorG * S_R_G_B_SLOPE_RECIPROCAL : pow((oldColorG + S_R_G_B_SEGMENT_OFFSET) * S_R_G_B_SLOPE_MATCH_RECIPROCAL, S_R_G_B_GAMMA);
		final float newColorB = oldColorB <= S_R_G_B_BREAK_POINT_MULTIPLIED_BY_SLOPE ? oldColorB * S_R_G_B_SLOPE_RECIPROCAL : pow((oldColorB + S_R_G_B_SEGMENT_OFFSET) * S_R_G_B_SLOPE_MATCH_RECIPROCAL, S_R_G_B_GAMMA);
		
		this.imageColorFloatArray[imageColorFloatArrayOffset + 0] = newColorR;
		this.imageColorFloatArray[imageColorFloatArrayOffset + 1] = newColorG;
		this.imageColorFloatArray[imageColorFloatArrayOffset + 2] = newColorB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doSetupFilmColorFloatArray() {
		put(this.filmColorFloatArray = Arrays.repeat(new float[] {0.0F}, getResolution() * 3));
	}
	
	private void doSetupFilmSampleIntArray() {
		put(this.filmSampleIntArray = Arrays.repeat(new int[] {0}, getResolution()));
	}
	
	private void doSetupImageColorByteArray() {
		put(this.imageColorByteArray = Arrays.repeat(new byte[] {(byte)(0)}, getResolution() * 4));
	}
	
	private void doSetupImageColorFloatArray() {
		put(this.imageColorFloatArray = Arrays.repeat(new float[] {0.0F}, getResolution() * 3));
	}
}