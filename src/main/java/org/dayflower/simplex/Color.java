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

import java.lang.reflect.Field;//TODO: Add Javadocs!

//TODO: Add Javadocs!
public final class Color {
	private Color() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Color4D /////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static double[] color4D() {
		return color4D(0.0D);
	}
	
//	TODO: Add Javadocs!
	public static double[] color4D(final double component) {
		return color4D(component, component, component);
	}
	
//	TODO: Add Javadocs!
	public static double[] color4D(final double component1, final double component2, final double component3) {
		return color4D(component1, component2, component3, 1.0D);
	}
	
//	TODO: Add Javadocs!
	public static double[] color4D(final double component1, final double component2, final double component3, final double component4) {
		return new double[] {component1, component2, component3, component4};
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Color4I /////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static int[] color4I() {
		return color4I(0);
	}
	
//	TODO: Add Javadocs!
	public static int[] color4I(final int component) {
		return color4I(component, component, component);
	}
	
//	TODO: Add Javadocs!
	public static int[] color4I(final int component1, final int component2, final int component3) {
		return color4I(component1, component2, component3, 255);
	}
	
//	TODO: Add Javadocs!
	public static int[] color4I(final int component1, final int component2, final int component3, final int component4) {
		return new int[] {component1, component2, component3, component4};
	}
}