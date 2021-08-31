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

import static org.dayflower.utility.Doubles.lerp;
import static org.dayflower.utility.Doubles.saturate;
import static org.dayflower.utility.Ints.saturate;
import static org.dayflower.utility.Ints.toInt;

import java.lang.reflect.Field;//TODO: Add Javadocs!

/**
 * A class that consists exclusively of static methods that returns or performs various operations on colors.
 * <p>
 * This class currently supports the following:
 * <ul>
 * <li>{@code Color4D} - a 4-dimensional color represented by a {@code double[]}.</li>
 * <li>{@code Color4I} - a 4-dimensional color represented by an {@code int[]}.</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Color {
	private Color() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Color4D /////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the average component value of component 1, component 2 and component 3 from the color contained in {@code color4D}.
	 * <p>
	 * If {@code color4D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4D.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4DAverage(color4D, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4D a {@code double[]} that contains a color with four components
	 * @return the average component value of component 1, component 2 and component 3 from the color contained in {@code color4D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4D.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code color4D} is {@code null}
	 */
	public static double color4DAverage(final double[] color4D) {
		return color4DAverage(color4D, 0);
	}
	
	/**
	 * Returns the average component value of component 1, component 2 and component 3 from the color contained in {@code color4D}.
	 * <p>
	 * If {@code color4D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4D.length < color4DOffset + 3} or {@code color4DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4D a {@code double[]} that contains a color with four components
	 * @param color4DOffset the offset in {@code color4D} to start at
	 * @return the average component value of component 1, component 2 and component 3 from the color contained in {@code color4D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4D.length < color4DOffset + 3} or {@code color4DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code color4D} is {@code null}
	 */
	public static double color4DAverage(final double[] color4D, final int color4DOffset) {
		final double component1 = color4DGetComponent1(color4D, color4DOffset);
		final double component2 = color4DGetComponent2(color4D, color4DOffset);
		final double component3 = color4DGetComponent3(color4D, color4DOffset);
		
		final double average = (component1 + component2 + component3) / 3.0D;
		
		return average;
	}
	
	/**
	 * Returns the value of component 1 from the color contained in {@code color4D}.
	 * <p>
	 * If {@code color4D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4D.length < 1}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4DGetComponent1(color4D, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4D a {@code double[]} that contains a color with four components
	 * @return the value of component 1 from the color contained in {@code color4D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4D.length < 1}
	 * @throws NullPointerException thrown if, and only if, {@code color4D} is {@code null}
	 */
	public static double color4DGetComponent1(final double[] color4D) {
		return color4DGetComponent1(color4D, 0);
	}
	
	/**
	 * Returns the value of component 1 from the color contained in {@code color4D}.
	 * <p>
	 * If {@code color4D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4D.length < color4DOffset + 1} or {@code color4DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4D a {@code double[]} that contains a color with four components
	 * @param color4DOffset the offset in {@code color4D} to start at
	 * @return the value of component 1 from the color contained in {@code color4D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4D.length < color4DOffset + 1} or {@code color4DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code color4D} is {@code null}
	 */
	public static double color4DGetComponent1(final double[] color4D, final int color4DOffset) {
		return color4D[color4DOffset + 0];
	}
	
	/**
	 * Returns the value of component 2 from the color contained in {@code color4D}.
	 * <p>
	 * If {@code color4D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4D.length < 2}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4DGetComponent2(color4D, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4D a {@code double[]} that contains a color with four components
	 * @return the value of component 2 from the color contained in {@code color4D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4D.length < 2}
	 * @throws NullPointerException thrown if, and only if, {@code color4D} is {@code null}
	 */
	public static double color4DGetComponent2(final double[] color4D) {
		return color4DGetComponent2(color4D, 0);
	}
	
	/**
	 * Returns the value of component 2 from the color contained in {@code color4D}.
	 * <p>
	 * If {@code color4D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4D.length < color4DOffset + 2} or {@code color4DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4D a {@code double[]} that contains a color with four components
	 * @param color4DOffset the offset in {@code color4D} to start at
	 * @return the value of component 2 from the color contained in {@code color4D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4D.length < color4DOffset + 2} or {@code color4DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code color4D} is {@code null}
	 */
	public static double color4DGetComponent2(final double[] color4D, final int color4DOffset) {
		return color4D[color4DOffset + 1];
	}
	
	/**
	 * Returns the value of component 3 from the color contained in {@code color4D}.
	 * <p>
	 * If {@code color4D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4D.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4DGetComponent3(color4D, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4D a {@code double[]} that contains a color with four components
	 * @return the value of component 3 from the color contained in {@code color4D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4D.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code color4D} is {@code null}
	 */
	public static double color4DGetComponent3(final double[] color4D) {
		return color4DGetComponent3(color4D, 0);
	}
	
	/**
	 * Returns the value of component 3 from the color contained in {@code color4D}.
	 * <p>
	 * If {@code color4D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4D.length < color4DOffset + 3} or {@code color4DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4D a {@code double[]} that contains a color with four components
	 * @param color4DOffset the offset in {@code color4D} to start at
	 * @return the value of component 3 from the color contained in {@code color4D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4D.length < color4DOffset + 3} or {@code color4DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code color4D} is {@code null}
	 */
	public static double color4DGetComponent3(final double[] color4D, final int color4DOffset) {
		return color4D[color4DOffset + 2];
	}
	
	/**
	 * Returns the value of component 4 from the color contained in {@code color4D}.
	 * <p>
	 * If {@code color4D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4D.length < 4}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4DGetComponent4(color4D, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4D a {@code double[]} that contains a color with four components
	 * @return the value of component 4 from the color contained in {@code color4D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4D.length < 4}
	 * @throws NullPointerException thrown if, and only if, {@code color4D} is {@code null}
	 */
	public static double color4DGetComponent4(final double[] color4D) {
		return color4DGetComponent4(color4D, 0);
	}
	
	/**
	 * Returns the value of component 4 from the color contained in {@code color4D}.
	 * <p>
	 * If {@code color4D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4D.length < color4DOffset + 4} or {@code color4DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4D a {@code double[]} that contains a color with four components
	 * @param color4DOffset the offset in {@code color4D} to start at
	 * @return the value of component 4 from the color contained in {@code color4D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4D.length < color4DOffset + 4} or {@code color4DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code color4D} is {@code null}
	 */
	public static double color4DGetComponent4(final double[] color4D, final int color4DOffset) {
		return color4D[color4DOffset + 3];
	}
	
	/**
	 * Returns a {@code double[]} that contains a color with four components.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4D(0.0D);
	 * }
	 * </pre>
	 * 
	 * @return a {@code double[]} that contains a color with four components
	 */
	public static double[] color4D() {
		return color4D(0.0D);
	}
	
	/**
	 * Returns a {@code double[]} that contains a color with four components.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4D(component, component, component);
	 * }
	 * </pre>
	 * 
	 * @param component the value of component 1, component 2 and component 3
	 * @return a {@code double[]} that contains a color with four components
	 */
	public static double[] color4D(final double component) {
		return color4D(component, component, component);
	}
	
	/**
	 * Returns a {@code double[]} that contains a color with four components.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4D(component1, component2, component3, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param component1 the value of component 1, also known as R or X
	 * @param component2 the value of component 2, also known as G or Y
	 * @param component3 the value of component 3, also known as B or Z
	 * @return a {@code double[]} that contains a color with four components
	 */
	public static double[] color4D(final double component1, final double component2, final double component3) {
		return color4D(component1, component2, component3, 1.0D);
	}
	
	/**
	 * Returns a {@code double[]} that contains a color with four components.
	 * 
	 * @param component1 the value of component 1, also known as R or X
	 * @param component2 the value of component 2, also known as G or Y
	 * @param component3 the value of component 3, also known as B or Z
	 * @param component4 the value of component 4, also known as A or W
	 * @return a {@code double[]} that contains a color with four components
	 */
	public static double[] color4D(final double component1, final double component2, final double component3, final double component4) {
		return new double[] {component1, component2, component3, component4};
	}
	
//	TODO: Add Javadocs!
	public static double[] color4DBlend(final double[] color4DLHS, final double[] color4DRHS) {
		return color4DBlend(color4DLHS, color4DRHS, 0.5D);
	}
	
//	TODO: Add Javadocs!
	public static double[] color4DBlend(final double[] color4DLHS, final double[] color4DRHS, final double t) {
		return color4DBlend(color4DLHS, color4DRHS, t, t, t, t);
	}
	
//	TODO: Add Javadocs!
	public static double[] color4DBlend(final double[] color4DLHS, final double[] color4DRHS, final double tComponent1, final double tComponent2, final double tComponent3, final double tComponent4) {
		return color4DBlend(color4DLHS, color4DRHS, tComponent1, tComponent2, tComponent3, tComponent4, color4D());
	}
	
//	TODO: Add Javadocs!
	public static double[] color4DBlend(final double[] color4DLHS, final double[] color4DRHS, final double tComponent1, final double tComponent2, final double tComponent3, final double tComponent4, final double[] color4DResult) {
		return color4DBlend(color4DLHS, color4DRHS, tComponent1, tComponent2, tComponent3, tComponent4, color4DResult, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] color4DBlend(final double[] color4DLHS, final double[] color4DRHS, final double tComponent1, final double tComponent2, final double tComponent3, final double tComponent4, final double[] color4DResult, final int color4DLHSOffset, final int color4DRHSOffset, final int color4DResultOffset) {
		final double component1 = lerp(color4DGetComponent1(color4DLHS, color4DLHSOffset), color4DGetComponent1(color4DRHS, color4DRHSOffset), tComponent1);
		final double component2 = lerp(color4DGetComponent2(color4DLHS, color4DLHSOffset), color4DGetComponent2(color4DRHS, color4DRHSOffset), tComponent2);
		final double component3 = lerp(color4DGetComponent3(color4DLHS, color4DLHSOffset), color4DGetComponent3(color4DRHS, color4DRHSOffset), tComponent3);
		final double component4 = lerp(color4DGetComponent4(color4DLHS, color4DLHSOffset), color4DGetComponent4(color4DRHS, color4DRHSOffset), tComponent4);
		
		return color4DSet(color4DResult, component1, component2, component3, component4, color4DResultOffset);
	}
	
	/**
	 * Returns a {@code double[]} that contains a color with four components and is set to the component values of the color {@code color4I}.
	 * <p>
	 * If {@code color4I} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4I.length < 4}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4DFromColor4I(color4I, Color.color4D());
	 * }
	 * </pre>
	 * 
	 * @param color4I an {@code int[]} that contains a color with four components
	 * @return a {@code double[]} that contains a color with four components and is set to the component values of the color {@code color4I}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4I.length < 4}
	 * @throws NullPointerException thrown if, and only if, {@code color4I} is {@code null}
	 */
	public static double[] color4DFromColor4I(final int[] color4I) {
		return color4DFromColor4I(color4I, color4D());
	}
	
	/**
	 * Returns a {@code double[]} that contains a color with four components and is set to the component values of the color {@code color4I}.
	 * <p>
	 * If either {@code color4I} or {@code color4DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4I.length < 4} or {@code color4DResult.length < 4}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4DFromColor4I(color4I, color4DResult, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4I an {@code int[]} that contains a color with four components
	 * @param color4DResult a {@code double[]} that contains the color to return
	 * @return a {@code double[]} that contains a color with four components and is set to the component values of the color {@code color4I}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4I.length < 4} or {@code color4DResult.length < 4}
	 * @throws NullPointerException thrown if, and only if, either {@code color4I} or {@code color4DResult} are {@code null}
	 */
	public static double[] color4DFromColor4I(final int[] color4I, final double[] color4DResult) {
		return color4DFromColor4I(color4I, color4DResult, 0, 0);
	}
	
	/**
	 * Returns a {@code double[]} that contains a color with four components and is set to the component values of the color {@code color4I}.
	 * <p>
	 * If either {@code color4I} or {@code color4DResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4I.length < color4IOffset + 4}, {@code color4IOffset < 0}, {@code color4DResult.length < color4DResultOffset + 4} or {@code color4DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4I an {@code int[]} that contains a color with four components
	 * @param color4DResult a {@code double[]} that contains the color to return
	 * @param color4IOffset the offset in {@code color4I} to start at
	 * @param color4DResultOffset the offset in {@code color4DResult} to start at
	 * @return a {@code double[]} that contains a color with four components and is set to the component values of the color {@code color4I}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4I.length < color4IOffset + 4}, {@code color4IOffset < 0}, {@code color4DResult.length < color4DResultOffset + 4} or {@code color4DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code color4I} or {@code color4DResult} are {@code null}
	 */
	public static double[] color4DFromColor4I(final int[] color4I, final double[] color4DResult, final int color4IOffset, final int color4DResultOffset) {
		final double component1 = saturate(color4IGetComponent1(color4I, color4IOffset)) / 255.0D;
		final double component2 = saturate(color4IGetComponent2(color4I, color4IOffset)) / 255.0D;
		final double component3 = saturate(color4IGetComponent3(color4I, color4IOffset)) / 255.0D;
		final double component4 = saturate(color4IGetComponent4(color4I, color4IOffset)) / 255.0D;
		
		return color4DSet(color4DResult, component1, component2, component3, component4, color4DResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] color4DMultiply(final double[] color4DLHS, final double[] color4DRHS) {
		return color4DMultiply(color4DLHS, color4DRHS, color4D());
	}
	
//	TODO: Add Javadocs!
	public static double[] color4DMultiply(final double[] color4DLHS, final double[] color4DRHS, final double[] color4DResult) {
		return color4DMultiply(color4DLHS, color4DRHS, color4DResult, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] color4DMultiply(final double[] color4DLHS, final double[] color4DRHS, final double[] color4DResult, final int color4DLHSOffset, final int color4DRHSOffset, final int color4DResultOffset) {
		final double component1 = color4DGetComponent1(color4DLHS, color4DLHSOffset) * color4DGetComponent1(color4DRHS, color4DRHSOffset);
		final double component2 = color4DGetComponent2(color4DLHS, color4DLHSOffset) * color4DGetComponent2(color4DRHS, color4DRHSOffset);
		final double component3 = color4DGetComponent3(color4DLHS, color4DLHSOffset) * color4DGetComponent3(color4DRHS, color4DRHSOffset);
		final double component4 = color4DGetComponent4(color4DLHS, color4DLHSOffset) * color4DGetComponent4(color4DRHS, color4DRHSOffset);
		
		return color4DSet(color4DResult, component1, component2, component3, component4, color4DResultOffset);
	}
	
	/**
	 * Sets the component values of the color contained in {@code color4DResult} at offset {@code 0}.
	 * <p>
	 * Returns {@code color4DResult}.
	 * <p>
	 * If {@code color4DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4DResult.length < 4}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4DSet(color4DResult, component1, component2, component3, component4, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4DResult a {@code double[]} that contains a color with four components
	 * @param component1 the value of component 1, also known as R or X
	 * @param component2 the value of component 2, also known as G or Y
	 * @param component3 the value of component 3, also known as B or Z
	 * @param component4 the value of component 4, also known as A or W
	 * @return {@code color4DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4DResult.length < 4}
	 * @throws NullPointerException thrown if, and only if, {@code color4DResult} is {@code null}
	 */
	public static double[] color4DSet(final double[] color4DResult, final double component1, final double component2, final double component3, final double component4) {
		return color4DSet(color4DResult, component1, component2, component3, component4, 0);
	}
	
	/**
	 * Sets the component values of the color contained in {@code color4DResult} at offset {@code color4DResultOffset}.
	 * <p>
	 * Returns {@code color4DResult}.
	 * <p>
	 * If {@code color4DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4DResult.length < color4DResultOffset + 4} or {@code color4DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4DResult a {@code double[]} that contains a color with four components
	 * @param component1 the value of component 1, also known as R or X
	 * @param component2 the value of component 2, also known as G or Y
	 * @param component3 the value of component 3, also known as B or Z
	 * @param component4 the value of component 4, also known as A or W
	 * @param color4DResultOffset the offset in {@code color4DResult} to start at
	 * @return {@code color4DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4DResult.length < color4DResultOffset + 4} or {@code color4DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code color4DResult} is {@code null}
	 */
	public static double[] color4DSet(final double[] color4DResult, final double component1, final double component2, final double component3, final double component4, final int color4DResultOffset) {
		color4DSetComponent1(color4DResult, component1, color4DResultOffset);
		color4DSetComponent2(color4DResult, component2, color4DResultOffset);
		color4DSetComponent3(color4DResult, component3, color4DResultOffset);
		color4DSetComponent4(color4DResult, component4, color4DResultOffset);
		
		return color4DResult;
	}
	
	/**
	 * Sets the component values of the color contained in {@code color4DResult} at offset {@code 0}.
	 * <p>
	 * Returns {@code color4DResult}.
	 * <p>
	 * If either {@code color4DResult} or {@code color4D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4DResult.length < 4} or {@code color4D.length < 4}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4DSet(color4DResult, color4D, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4DResult a {@code double[]} that contains a color with four components
	 * @param color4D a {@code double[]} that contains a color with component values to set
	 * @return {@code color4DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4DResult.length < 4} or {@code color4D.length < 4}
	 * @throws NullPointerException thrown if, and only if, either {@code color4DResult} or {@code color4D} are {@code null}
	 */
	public static double[] color4DSet(final double[] color4DResult, final double[] color4D) {
		return color4DSet(color4DResult, color4D, 0, 0);
	}
	
	/**
	 * Sets the component values of the color contained in {@code color4DResult} at offset {@code color4DResultOffset}.
	 * <p>
	 * Returns {@code color4DResult}.
	 * <p>
	 * If either {@code color4DResult} or {@code color4D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4DResult.length < color4DResultOffset + 4}, {@code color4DResultOffset < 0}, {@code color4D.length < color4DOffset + 4} or {@code color4DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4DResult a {@code double[]} that contains a color with four components
	 * @param color4D a {@code double[]} that contains a color with component values to set
	 * @param color4DResultOffset the offset in {@code color4DResult} to start at
	 * @param color4DOffset the offset in {@code color4D} to start at
	 * @return {@code color4DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4DResult.length < color4DResultOffset + 4}, {@code color4DResultOffset < 0}, {@code color4D.length < color4DOffset + 4} or {@code color4DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code color4DResult} or {@code color4D} are {@code null}
	 */
	public static double[] color4DSet(final double[] color4DResult, final double[] color4D, final int color4DResultOffset, final int color4DOffset) {
		color4DSetComponent1(color4DResult, color4DGetComponent1(color4D, color4DOffset), color4DResultOffset);
		color4DSetComponent2(color4DResult, color4DGetComponent2(color4D, color4DOffset), color4DResultOffset);
		color4DSetComponent3(color4DResult, color4DGetComponent3(color4D, color4DOffset), color4DResultOffset);
		color4DSetComponent4(color4DResult, color4DGetComponent4(color4D, color4DOffset), color4DResultOffset);
		
		return color4DResult;
	}
	
	/**
	 * Sets the value of component 1 for the color contained in {@code color4DResult} at offset {@code 0}.
	 * <p>
	 * Returns {@code color4DResult}.
	 * <p>
	 * If {@code color4DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4DResult.length < 1}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4DSetComponent1(color4DResult, component1, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4DResult a {@code double[]} that contains a color with four components
	 * @param component1 the value of component 1, also known as R or X
	 * @return {@code color4DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4DResult.length < 1}
	 * @throws NullPointerException thrown if, and only if, {@code color4DResult} is {@code null}
	 */
	public static double[] color4DSetComponent1(final double[] color4DResult, final double component1) {
		return color4DSetComponent1(color4DResult, component1, 0);
	}
	
	/**
	 * Sets the value of component 1 for the color contained in {@code color4DResult} at offset {@code color4DResultOffset}.
	 * <p>
	 * Returns {@code color4DResult}.
	 * <p>
	 * If {@code color4DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4DResult.length < color4DResultOffset + 1} or {@code color4DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4DResult a {@code double[]} that contains a color with four components
	 * @param component1 the value of component 1, also known as R or X
	 * @param color4DResultOffset the offset in {@code color4DResult} to start at
	 * @return {@code color4DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4DResult.length < color4DResultOffset + 1} or {@code color4DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code color4DResult} is {@code null}
	 */
	public static double[] color4DSetComponent1(final double[] color4DResult, final double component1, final int color4DResultOffset) {
		color4DResult[color4DResultOffset + 0] = component1;
		
		return color4DResult;
	}
	
	/**
	 * Sets the value of component 2 for the color contained in {@code color4DResult} at offset {@code 0}.
	 * <p>
	 * Returns {@code color4DResult}.
	 * <p>
	 * If {@code color4DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4DResult.length < 2}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4DSetComponent2(color4DResult, component2, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4DResult a {@code double[]} that contains a color with four components
	 * @param component2 the value of component 2, also known as G or Y
	 * @return {@code color4DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4DResult.length < 2}
	 * @throws NullPointerException thrown if, and only if, {@code color4DResult} is {@code null}
	 */
	public static double[] color4DSetComponent2(final double[] color4DResult, final double component2) {
		return color4DSetComponent2(color4DResult, component2, 0);
	}
	
	/**
	 * Sets the value of component 2 for the color contained in {@code color4DResult} at offset {@code color4DResultOffset}.
	 * <p>
	 * Returns {@code color4DResult}.
	 * <p>
	 * If {@code color4DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4DResult.length < color4DResultOffset + 2} or {@code color4DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4DResult a {@code double[]} that contains a color with four components
	 * @param component2 the value of component 2, also known as G or Y
	 * @param color4DResultOffset the offset in {@code color4DResult} to start at
	 * @return {@code color4DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4DResult.length < color4DResultOffset + 2} or {@code color4DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code color4DResult} is {@code null}
	 */
	public static double[] color4DSetComponent2(final double[] color4DResult, final double component2, final int color4DResultOffset) {
		color4DResult[color4DResultOffset + 1] = component2;
		
		return color4DResult;
	}
	
	/**
	 * Sets the value of component 3 for the color contained in {@code color4DResult} at offset {@code 0}.
	 * <p>
	 * Returns {@code color4DResult}.
	 * <p>
	 * If {@code color4DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4DResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4DSetComponent3(color4DResult, component3, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4DResult a {@code double[]} that contains a color with four components
	 * @param component3 the value of component 3, also known as B or Z
	 * @return {@code color4DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4DResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code color4DResult} is {@code null}
	 */
	public static double[] color4DSetComponent3(final double[] color4DResult, final double component3) {
		return color4DSetComponent3(color4DResult, component3, 0);
	}
	
	/**
	 * Sets the value of component 3 for the color contained in {@code color4DResult} at offset {@code color4DResultOffset}.
	 * <p>
	 * Returns {@code color4DResult}.
	 * <p>
	 * If {@code color4DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4DResult.length < color4DResultOffset + 3} or {@code color4DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4DResult a {@code double[]} that contains a color with four components
	 * @param component3 the value of component 3, also known as B or Z
	 * @param color4DResultOffset the offset in {@code color4DResult} to start at
	 * @return {@code color4DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4DResult.length < color4DResultOffset + 3} or {@code color4DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code color4DResult} is {@code null}
	 */
	public static double[] color4DSetComponent3(final double[] color4DResult, final double component3, final int color4DResultOffset) {
		color4DResult[color4DResultOffset + 2] = component3;
		
		return color4DResult;
	}
	
	/**
	 * Sets the value of component 4 for the color contained in {@code color4DResult} at offset {@code 0}.
	 * <p>
	 * Returns {@code color4DResult}.
	 * <p>
	 * If {@code color4DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4DResult.length < 4}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4DSetComponent4(color4DResult, component4, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4DResult a {@code double[]} that contains a color with four components
	 * @param component4 the value of component 4, also known as A or W
	 * @return {@code color4DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4DResult.length < 4}
	 * @throws NullPointerException thrown if, and only if, {@code color4DResult} is {@code null}
	 */
	public static double[] color4DSetComponent4(final double[] color4DResult, final double component4) {
		return color4DSetComponent4(color4DResult, component4, 0);
	}
	
	/**
	 * Sets the value of component 4 for the color contained in {@code color4DResult} at offset {@code color4DResultOffset}.
	 * <p>
	 * Returns {@code color4DResult}.
	 * <p>
	 * If {@code color4DResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4DResult.length < color4DResultOffset + 4} or {@code color4DResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4DResult a {@code double[]} that contains a color with four components
	 * @param component4 the value of component 4, also known as A or W
	 * @param color4DResultOffset the offset in {@code color4DResult} to start at
	 * @return {@code color4DResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4DResult.length < color4DResultOffset + 4} or {@code color4DResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code color4DResult} is {@code null}
	 */
	public static double[] color4DSetComponent4(final double[] color4DResult, final double component4, final int color4DResultOffset) {
		color4DResult[color4DResultOffset + 3] = component4;
		
		return color4DResult;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Color4I /////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the average component value of component 1, component 2 and component 3 from the color contained in {@code color4I}.
	 * <p>
	 * If {@code color4I} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4I.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4IAverage(color4I, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4I an {@code int[]} that contains a color with four components
	 * @return the average component value of component 1, component 2 and component 3 from the color contained in {@code color4I}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4I.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code color4I} is {@code null}
	 */
	public static int color4IAverage(final int[] color4I) {
		return color4IAverage(color4I, 0);
	}
	
	/**
	 * Returns the average component value of component 1, component 2 and component 3 from the color contained in {@code color4I}.
	 * <p>
	 * If {@code color4I} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4I.length < color4IOffset + 3} or {@code color4IOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4I an {@code int[]} that contains a color with four components
	 * @param color4IOffset the offset in {@code color4I} to start at
	 * @return the average component value of component 1, component 2 and component 3 from the color contained in {@code color4I}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4I.length < color4IOffset + 3} or {@code color4IOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code color4I} is {@code null}
	 */
	public static int color4IAverage(final int[] color4I, final int color4IOffset) {
		final int component1 = color4IGetComponent1(color4I, color4IOffset);
		final int component2 = color4IGetComponent2(color4I, color4IOffset);
		final int component3 = color4IGetComponent3(color4I, color4IOffset);
		
		final int average = (component1 + component2 + component3) / 3;
		
		return average;
	}
	
	/**
	 * Returns the value of component 1 from the color contained in {@code color4I}.
	 * <p>
	 * If {@code color4I} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4I.length < 1}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4IGetComponent1(color4I, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4D an {@code int[]} that contains a color with four components
	 * @return the value of component 1 from the color contained in {@code color4I}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4I.length < 1}
	 * @throws NullPointerException thrown if, and only if, {@code color4I} is {@code null}
	 */
	public static int color4IGetComponent1(final int[] color4I) {
		return color4IGetComponent1(color4I, 0);
	}
	
	/**
	 * Returns the value of component 1 from the color contained in {@code color4I}.
	 * <p>
	 * If {@code color4I} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4I.length < color4IOffset + 1} or {@code color4IOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4I an {@code int[]} that contains a color with four components
	 * @param color4IOffset the offset in {@code color4I} to start at
	 * @return the value of component 1 from the color contained in {@code color4I}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4I.length < color4IOffset + 1} or {@code color4IOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code color4I} is {@code null}
	 */
	public static int color4IGetComponent1(final int[] color4I, final int color4IOffset) {
		return color4I[color4IOffset + 0];
	}
	
	/**
	 * Returns the value of component 2 from the color contained in {@code color4I}.
	 * <p>
	 * If {@code color4I} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4I.length < 2}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4IGetComponent2(color4I, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4D an {@code int[]} that contains a color with four components
	 * @return the value of component 2 from the color contained in {@code color4I}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4I.length < 2}
	 * @throws NullPointerException thrown if, and only if, {@code color4I} is {@code null}
	 */
	public static int color4IGetComponent2(final int[] color4I) {
		return color4IGetComponent2(color4I, 0);
	}
	
	/**
	 * Returns the value of component 2 from the color contained in {@code color4I}.
	 * <p>
	 * If {@code color4I} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4I.length < color4IOffset + 2} or {@code color4IOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4I an {@code int[]} that contains a color with four components
	 * @param color4IOffset the offset in {@code color4I} to start at
	 * @return the value of component 2 from the color contained in {@code color4I}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4I.length < color4IOffset + 2} or {@code color4IOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code color4I} is {@code null}
	 */
	public static int color4IGetComponent2(final int[] color4I, final int color4IOffset) {
		return color4I[color4IOffset + 1];
	}
	
	/**
	 * Returns the value of component 3 from the color contained in {@code color4I}.
	 * <p>
	 * If {@code color4I} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4I.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4IGetComponent3(color4I, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4D an {@code int[]} that contains a color with four components
	 * @return the value of component 3 from the color contained in {@code color4I}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4I.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code color4I} is {@code null}
	 */
	public static int color4IGetComponent3(final int[] color4I) {
		return color4IGetComponent3(color4I, 0);
	}
	
	/**
	 * Returns the value of component 3 from the color contained in {@code color4I}.
	 * <p>
	 * If {@code color4I} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4I.length < color4IOffset + 3} or {@code color4IOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4I an {@code int[]} that contains a color with four components
	 * @param color4IOffset the offset in {@code color4I} to start at
	 * @return the value of component 3 from the color contained in {@code color4I}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4I.length < color4IOffset + 3} or {@code color4IOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code color4I} is {@code null}
	 */
	public static int color4IGetComponent3(final int[] color4I, final int color4IOffset) {
		return color4I[color4IOffset + 2];
	}
	
	/**
	 * Returns the value of component 4 from the color contained in {@code color4I}.
	 * <p>
	 * If {@code color4I} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4I.length < 4}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4IGetComponent4(color4I, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4D an {@code int[]} that contains a color with four components
	 * @return the value of component 4 from the color contained in {@code color4I}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4I.length < 4}
	 * @throws NullPointerException thrown if, and only if, {@code color4I} is {@code null}
	 */
	public static int color4IGetComponent4(final int[] color4I) {
		return color4IGetComponent4(color4I, 0);
	}
	
	/**
	 * Returns the value of component 4 from the color contained in {@code color4I}.
	 * <p>
	 * If {@code color4I} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4I.length < color4IOffset + 4} or {@code color4IOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4I an {@code int[]} that contains a color with four components
	 * @param color4IOffset the offset in {@code color4I} to start at
	 * @return the value of component 4 from the color contained in {@code color4I}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4I.length < color4IOffset + 4} or {@code color4IOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code color4I} is {@code null}
	 */
	public static int color4IGetComponent4(final int[] color4I, final int color4IOffset) {
		return color4I[color4IOffset + 3];
	}
	
//	TODO: Add Javadocs!
	public static int color4IPackABGR(final int[] color4I) {
		return color4IPackABGR(color4I, 0);
	}
	
//	TODO: Add Javadocs!
	public static int color4IPackABGR(final int[] color4I, final int color4IOffset) {
		final int component1 = (color4IGetComponent1(color4I, color4IOffset) & 0xFF) <<  0;
		final int component2 = (color4IGetComponent2(color4I, color4IOffset) & 0xFF) <<  8;
		final int component3 = (color4IGetComponent3(color4I, color4IOffset) & 0xFF) << 16;
		final int component4 = (color4IGetComponent4(color4I, color4IOffset) & 0xFF) << 24;
		
		final int color4IPacked = component1 | component2 | component3 | component4;
		
		return color4IPacked;
	}
	
//	TODO: Add Javadocs!
	public static int color4IPackARGB(final int[] color4I) {
		return color4IPackARGB(color4I, 0);
	}
	
//	TODO: Add Javadocs!
	public static int color4IPackARGB(final int[] color4I, final int color4IOffset) {
		final int component1 = (color4IGetComponent1(color4I, color4IOffset) & 0xFF) << 16;
		final int component2 = (color4IGetComponent2(color4I, color4IOffset) & 0xFF) <<  8;
		final int component3 = (color4IGetComponent3(color4I, color4IOffset) & 0xFF) <<  0;
		final int component4 = (color4IGetComponent4(color4I, color4IOffset) & 0xFF) << 24;
		
		final int color4IPacked = component1 | component2 | component3 | component4;
		
		return color4IPacked;
	}
	
	/**
	 * Returns an {@code int[]} that contains a color with four components.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4I(0);
	 * }
	 * </pre>
	 * 
	 * @return an {@code int[]} that contains a color with four components
	 */
	public static int[] color4I() {
		return color4I(0);
	}
	
	/**
	 * Returns an {@code int[]} that contains a color with four components.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4I(component, component, component);
	 * }
	 * </pre>
	 * 
	 * @param component the value of component 1, component 2 and component 3
	 * @return an {@code int[]} that contains a color with four components
	 */
	public static int[] color4I(final int component) {
		return color4I(component, component, component);
	}
	
	/**
	 * Returns an {@code int[]} that contains a color with four components.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4I(component1, component2, component3, 255);
	 * }
	 * </pre>
	 * 
	 * @param component1 the value of component 1, also known as R or X
	 * @param component2 the value of component 2, also known as G or Y
	 * @param component3 the value of component 3, also known as B or Z
	 * @return an {@code int[]} that contains a color with four components
	 */
	public static int[] color4I(final int component1, final int component2, final int component3) {
		return color4I(component1, component2, component3, 255);
	}
	
	/**
	 * Returns an {@code int[]} that contains a color with four components.
	 * 
	 * @param component1 the value of component 1, also known as R or X
	 * @param component2 the value of component 2, also known as G or Y
	 * @param component3 the value of component 3, also known as B or Z
	 * @param component4 the value of component 4, also known as A or W
	 * @return an {@code int[]} that contains a color with four components
	 */
	public static int[] color4I(final int component1, final int component2, final int component3, final int component4) {
		return new int[] {component1, component2, component3, component4};
	}
	
	/**
	 * Returns an {@code int[]} that contains a color with four components and is set to the component values of the color {@code color4D}.
	 * <p>
	 * If {@code color4D} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4D.length < 4}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4IFromColor4D(color4D, Color.color4I());
	 * }
	 * </pre>
	 * 
	 * @param color4D a {@code double[]} that contains a color with four components
	 * @return an {@code int[]} that contains a color with four components and is set to the component values of the color {@code color4D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4D.length < 4}
	 * @throws NullPointerException thrown if, and only if, {@code color4D} is {@code null}
	 */
	public static int[] color4IFromColor4D(final double[] color4D) {
		return color4IFromColor4D(color4D, color4I());
	}
	
	/**
	 * Returns an {@code int[]} that contains a color with four components and is set to the component values of the color {@code color4D}.
	 * <p>
	 * If either {@code color4D} or {@code color4IResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4D.length < 4} or {@code color4IResult.length < 4}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4IFromColor4D(color4D, color4IResult, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4D a {@code double[]} that contains a color with four components
	 * @param color4IResult an {@code int[]} that contains the color to return
	 * @return an {@code int[]} that contains a color with four components and is set to the component values of the color {@code color4D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4D.length < 4} or {@code color4IResult.length < 4}
	 * @throws NullPointerException thrown if, and only if, either {@code color4D} or {@code color4IResult} are {@code null}
	 */
	public static int[] color4IFromColor4D(final double[] color4D, final int[] color4IResult) {
		return color4IFromColor4D(color4D, color4IResult, 0, 0);
	}
	
	/**
	 * Returns an {@code int[]} that contains a color with four components and is set to the component values of the color {@code color4D}.
	 * <p>
	 * If either {@code color4D} or {@code color4IResult} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4D.length < color4DOffset + 4}, {@code color4DOffset < 0}, {@code color4IResult.length < color4IResultOffset + 4} or {@code color4IResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4D a {@code double[]} that contains a color with four components
	 * @param color4IResult an {@code int[]} that contains the color to return
	 * @param color4DOffset the offset in {@code color4D} to start at
	 * @param color4IResultOffset the offset in {@code color4IResult} to start at
	 * @return an {@code int[]} that contains a color with four components and is set to the component values of the color {@code color4D}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4D.length < color4DOffset + 4}, {@code color4DOffset < 0}, {@code color4IResult.length < color4IResultOffset + 4} or {@code color4IResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code color4D} or {@code color4IResult} are {@code null}
	 */
	public static int[] color4IFromColor4D(final double[] color4D, final int[] color4IResult, final int color4DOffset, final int color4IResultOffset) {
		final int component1 = toInt(saturate(color4DGetComponent1(color4D, color4DOffset)) * 255.0D + 0.5D);
		final int component2 = toInt(saturate(color4DGetComponent2(color4D, color4DOffset)) * 255.0D + 0.5D);
		final int component3 = toInt(saturate(color4DGetComponent3(color4D, color4DOffset)) * 255.0D + 0.5D);
		final int component4 = toInt(saturate(color4DGetComponent4(color4D, color4DOffset)) * 255.0D + 0.5D);
		
		return color4ISet(color4IResult, component1, component2, component3, component4, color4IResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static int[] color4IFromColor4IPacked(final int color4IPacked) {
		return color4IFromColor4IPacked(color4IPacked, color4I());
	}
	
//	TODO: Add Javadocs!
	public static int[] color4IFromColor4IPacked(final int color4IPacked, final int[] color4IResult) {
		return color4IFromColor4IPacked(color4IPacked, color4IResult, 0);
	}
	
//	TODO: Add Javadocs!
	public static int[] color4IFromColor4IPacked(final int color4IPacked, final int[] color4IResult, final int color4IResultOffset) {
		final int component1 = color4IPackedGetComponent1(color4IPacked);
		final int component2 = color4IPackedGetComponent2(color4IPacked);
		final int component3 = color4IPackedGetComponent3(color4IPacked);
		final int component4 = color4IPackedGetComponent4(color4IPacked);
		
		return color4ISet(color4IResult, component1, component2, component3, component4, color4IResultOffset);
	}
	
	/**
	 * Sets the component values of the color contained in {@code color4IResult} at offset {@code 0}.
	 * <p>
	 * Returns {@code color4IResult}.
	 * <p>
	 * If {@code color4IResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4IResult.length < 4}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4ISet(color4IResult, component1, component2, component3, component4, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4IResult an {@code int[]} that contains a color with four components
	 * @param component1 the value of component 1, also known as R or X
	 * @param component2 the value of component 2, also known as G or Y
	 * @param component3 the value of component 3, also known as B or Z
	 * @param component4 the value of component 4, also known as A or W
	 * @return {@code color4IResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4IResult.length < 4}
	 * @throws NullPointerException thrown if, and only if, {@code color4IResult} is {@code null}
	 */
	public static int[] color4ISet(final int[] color4IResult, final int component1, final int component2, final int component3, final int component4) {
		return color4ISet(color4IResult, component1, component2, component3, component4, 0);
	}
	
	/**
	 * Sets the component values of the color contained in {@code color4IResult} at offset {@code color4IResultOffset}.
	 * <p>
	 * Returns {@code color4IResult}.
	 * <p>
	 * If {@code color4IResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4IResult.length < color4IResultOffset + 4} or {@code color4IResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4IResult an {@code int[]} that contains a color with four components
	 * @param component1 the value of component 1, also known as R or X
	 * @param component2 the value of component 2, also known as G or Y
	 * @param component3 the value of component 3, also known as B or Z
	 * @param component4 the value of component 4, also known as A or W
	 * @param color4IResultOffset the offset in {@code color4IResult} to start at
	 * @return {@code color4IResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4IResult.length < color4IResultOffset + 4} or {@code color4IResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code color4IResult} is {@code null}
	 */
	public static int[] color4ISet(final int[] color4IResult, final int component1, final int component2, final int component3, final int component4, final int color4IResultOffset) {
		color4ISetComponent1(color4IResult, component1, color4IResultOffset);
		color4ISetComponent2(color4IResult, component2, color4IResultOffset);
		color4ISetComponent3(color4IResult, component3, color4IResultOffset);
		color4ISetComponent4(color4IResult, component4, color4IResultOffset);
		
		return color4IResult;
	}
	
	/**
	 * Sets the component values of the color contained in {@code color4IResult} at offset {@code 0}.
	 * <p>
	 * Returns {@code color4IResult}.
	 * <p>
	 * If either {@code color4IResult} or {@code color4I} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4IResult.length < 4} or {@code color4I.length < 4}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4ISet(color4IResult, color4I, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4IResult an {@code int[]} that contains a color with four components
	 * @param color4I an {@code int[]} that contains a color with component values to set
	 * @return {@code color4IResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4IResult.length < 4} or {@code color4I.length < 4}
	 * @throws NullPointerException thrown if, and only if, either {@code color4IResult} or {@code color4I} are {@code null}
	 */
	public static int[] color4ISet(final int[] color4IResult, final int[] color4I) {
		return color4ISet(color4IResult, color4I, 0, 0);
	}
	
	/**
	 * Sets the component values of the color contained in {@code color4IResult} at offset {@code color4IResultOffset}.
	 * <p>
	 * Returns {@code color4IResult}.
	 * <p>
	 * If either {@code color4IResult} or {@code color4I} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4IResult.length < color4IResultOffset + 4}, {@code color4IResultOffset < 0}, {@code color4I.length < color4IOffset + 4} or {@code color4IOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4IResult an {@code int[]} that contains a color with four components
	 * @param color4I an {@code int[]} that contains a color with component values to set
	 * @param color4IResultOffset the offset in {@code color4IResult} to start at
	 * @param color4IOffset the offset in {@code color4I} to start at
	 * @return {@code color4IResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4IResult.length < color4IResultOffset + 4}, {@code color4IResultOffset < 0}, {@code color4I.length < color4IOffset + 4} or {@code color4IOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code color4IResult} or {@code color4I} are {@code null}
	 */
	public static int[] color4ISet(final int[] color4IResult, final int[] color4I, final int color4IResultOffset, final int color4IOffset) {
		color4ISetComponent1(color4IResult, color4IGetComponent1(color4I, color4IOffset), color4IResultOffset);
		color4ISetComponent2(color4IResult, color4IGetComponent2(color4I, color4IOffset), color4IResultOffset);
		color4ISetComponent3(color4IResult, color4IGetComponent3(color4I, color4IOffset), color4IResultOffset);
		color4ISetComponent4(color4IResult, color4IGetComponent4(color4I, color4IOffset), color4IResultOffset);
		
		return color4IResult;
	}
	
	/**
	 * Sets the value of component 1 for the color contained in {@code color4IResult} at offset {@code 0}.
	 * <p>
	 * Returns {@code color4IResult}.
	 * <p>
	 * If {@code color4IResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4IResult.length < 1}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4ISetComponent1(color4IResult, component1, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4IResult an {@code int[]} that contains a color with four components
	 * @param component1 the value of component 1, also known as R or X
	 * @return {@code color4IResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4IResult.length < 1}
	 * @throws NullPointerException thrown if, and only if, {@code color4IResult} is {@code null}
	 */
	public static int[] color4ISetComponent1(final int[] color4IResult, final int component1) {
		return color4ISetComponent1(color4IResult, component1, 0);
	}
	
	/**
	 * Sets the value of component 1 for the color contained in {@code color4IResult} at offset {@code color4IResultOffset}.
	 * <p>
	 * Returns {@code color4IResult}.
	 * <p>
	 * If {@code color4IResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4IResult.length < color4IResultOffset + 1} or {@code color4IResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4IResult an {@code int[]} that contains a color with four components
	 * @param component1 the value of component 1, also known as R or X
	 * @param color4IResultOffset the offset in {@code color4IResult} to start at
	 * @return {@code color4IResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4IResult.length < color4IResultOffset + 1} or {@code color4IResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code color4IResult} is {@code null}
	 */
	public static int[] color4ISetComponent1(final int[] color4IResult, final int component1, final int color4IResultOffset) {
		color4IResult[color4IResultOffset + 0] = component1;
		
		return color4IResult;
	}
	
	/**
	 * Sets the value of component 2 for the color contained in {@code color4IResult} at offset {@code 0}.
	 * <p>
	 * Returns {@code color4IResult}.
	 * <p>
	 * If {@code color4IResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4IResult.length < 2}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4ISetComponent2(color4IResult, component2, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4IResult an {@code int[]} that contains a color with four components
	 * @param component2 the value of component 2, also known as G or Y
	 * @return {@code color4IResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4IResult.length < 2}
	 * @throws NullPointerException thrown if, and only if, {@code color4IResult} is {@code null}
	 */
	public static int[] color4ISetComponent2(final int[] color4IResult, final int component2) {
		return color4ISetComponent2(color4IResult, component2, 0);
	}
	
	/**
	 * Sets the value of component 2 for the color contained in {@code color4IResult} at offset {@code color4IResultOffset}.
	 * <p>
	 * Returns {@code color4IResult}.
	 * <p>
	 * If {@code color4IResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4IResult.length < color4IResultOffset + 2} or {@code color4IResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4IResult an {@code int[]} that contains a color with four components
	 * @param component2 the value of component 2, also known as G or Y
	 * @param color4IResultOffset the offset in {@code color4IResult} to start at
	 * @return {@code color4IResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4IResult.length < color4IResultOffset + 2} or {@code color4IResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code color4IResult} is {@code null}
	 */
	public static int[] color4ISetComponent2(final int[] color4IResult, final int component2, final int color4IResultOffset) {
		color4IResult[color4IResultOffset + 1] = component2;
		
		return color4IResult;
	}
	
	/**
	 * Sets the value of component 3 for the color contained in {@code color4IResult} at offset {@code 0}.
	 * <p>
	 * Returns {@code color4IResult}.
	 * <p>
	 * If {@code color4IResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4IResult.length < 3}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4ISetComponent3(color4IResult, component3, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4IResult an {@code int[]} that contains a color with four components
	 * @param component3 the value of component 3, also known as B or Z
	 * @return {@code color4IResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4IResult.length < 3}
	 * @throws NullPointerException thrown if, and only if, {@code color4IResult} is {@code null}
	 */
	public static int[] color4ISetComponent3(final int[] color4IResult, final int component3) {
		return color4ISetComponent3(color4IResult, component3, 0);
	}
	
	/**
	 * Sets the value of component 3 for the color contained in {@code color4IResult} at offset {@code color4IResultOffset}.
	 * <p>
	 * Returns {@code color4IResult}.
	 * <p>
	 * If {@code color4IResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4IResult.length < color4IResultOffset + 3} or {@code color4IResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4IResult an {@code int[]} that contains a color with four components
	 * @param component3 the value of component 3, also known as B or Z
	 * @param color4IResultOffset the offset in {@code color4IResult} to start at
	 * @return {@code color4IResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4IResult.length < color4IResultOffset + 3} or {@code color4IResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code color4IResult} is {@code null}
	 */
	public static int[] color4ISetComponent3(final int[] color4IResult, final int component3, final int color4IResultOffset) {
		color4IResult[color4IResultOffset + 2] = component3;
		
		return color4IResult;
	}
	
	/**
	 * Sets the value of component 4 for the color contained in {@code color4IResult} at offset {@code 0}.
	 * <p>
	 * Returns {@code color4IResult}.
	 * <p>
	 * If {@code color4IResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4IResult.length < 4}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.color4ISetComponent4(color4IResult, component4, 0);
	 * }
	 * </pre>
	 * 
	 * @param color4IResult an {@code int[]} that contains a color with four components
	 * @param component4 the value of component 4, also known as A or W
	 * @return {@code color4IResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4IResult.length < 4}
	 * @throws NullPointerException thrown if, and only if, {@code color4IResult} is {@code null}
	 */
	public static int[] color4ISetComponent4(final int[] color4IResult, final int component4) {
		return color4ISetComponent4(color4IResult, component4, 0);
	}
	
	/**
	 * Sets the value of component 4 for the color contained in {@code color4IResult} at offset {@code color4IResultOffset}.
	 * <p>
	 * Returns {@code color4IResult}.
	 * <p>
	 * If {@code color4IResult} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code color4IResult.length < color4IResultOffset + 4} or {@code color4IResultOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param color4IResult an {@code int[]} that contains a color with four components
	 * @param component4 the value of component 4, also known as A or W
	 * @param color4IResultOffset the offset in {@code color4IResult} to start at
	 * @return {@code color4IResult}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code color4IResult.length < color4IResultOffset + 4} or {@code color4IResultOffset < 0}
	 * @throws NullPointerException thrown if, and only if, {@code color4IResult} is {@code null}
	 */
	public static int[] color4ISetComponent4(final int[] color4IResult, final int component4, final int color4IResultOffset) {
		color4IResult[color4IResultOffset + 3] = component4;
		
		return color4IResult;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Color4IPacked ///////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static int color4IPacked() {
		return color4IPacked(0);
	}
	
//	TODO: Add Javadocs!
	public static int color4IPacked(final int component) {
		return color4IPacked(component, component, component);
	}
	
//	TODO: Add Javadocs!
	public static int color4IPacked(final int component1, final int component2, final int component3) {
		return color4IPacked(component1, component2, component3, 255);
	}
	
//	TODO: Add Javadocs!
	public static int color4IPacked(final int component1, final int component2, final int component3, final int component4) {
		return ((component1 & 0xFF) << 16) | ((component2 & 0xFF) << 8) | ((component3 & 0xFF) << 0) | ((component4 & 0xFF) << 24);
	}
	
//	TODO: Add Javadocs!
	public static int color4IPackedFromColor4I(final int[] color4I) {
		return color4IPackedFromColor4I(color4I, 0);
	}
	
//	TODO: Add Javadocs!
	public static int color4IPackedFromColor4I(final int[] color4I, final int color4IOffset) {
		final int component1 = color4IGetComponent1(color4I, color4IOffset);
		final int component2 = color4IGetComponent2(color4I, color4IOffset);
		final int component3 = color4IGetComponent3(color4I, color4IOffset);
		final int component4 = color4IGetComponent4(color4I, color4IOffset);
		
		return color4IPacked(component1, component2, component3, component4);
	}
	
//	TODO: Add Javadocs!
	public static int color4IPackedGetComponent1(final int color4IPacked) {
		return (color4IPacked >> 16) & 0xFF;
	}
	
//	TODO: Add Javadocs!
	public static int color4IPackedGetComponent2(final int color4IPacked) {
		return (color4IPacked >> 8) & 0xFF;
	}
	
//	TODO: Add Javadocs!
	public static int color4IPackedGetComponent3(final int color4IPacked) {
		return (color4IPacked >> 0) & 0xFF;
	}
	
//	TODO: Add Javadocs!
	public static int color4IPackedGetComponent4(final int color4IPacked) {
		return (color4IPacked >> 24) & 0xFF;
	}
}