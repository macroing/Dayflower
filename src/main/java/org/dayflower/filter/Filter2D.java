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
package org.dayflower.filter;

import java.lang.reflect.Field;//TODO: Add Unit Tests!

/**
 * A {@code Filter2D} represents a 2-dimensional filter that operates on and returns {@code double} values.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class Filter2D {
	/**
	 * The size of the table created by {@link #createFilterTable()}.
	 */
	public static final int FILTER_TABLE_SIZE = 16;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final double resolutionX;
	private final double resolutionXReciprocal;
	private final double resolutionY;
	private final double resolutionYReciprocal;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Filter2D} instance given {@code resolutionX} and {@code resolutionY}.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 */
//	TODO: Add Unit Tests!
	protected Filter2D(final double resolutionX, final double resolutionY) {
		this.resolutionX = resolutionX;
		this.resolutionY = resolutionY;
		this.resolutionXReciprocal = 1.0D / this.resolutionX;
		this.resolutionYReciprocal = 1.0D / this.resolutionY;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Evaluates this {@code Filter2D} instance given {@code x} and {@code y}.
	 * <p>
	 * Returns the evaluated value.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @return the evaluated value
	 */
//	TODO: Add Unit Tests!
	public abstract double evaluate(final double x, final double y);
	
	/**
	 * Returns the resolution of the X-axis.
	 * 
	 * @return the resolution of the X-axis
	 */
//	TODO: Add Unit Tests!
	public final double getResolutionX() {
		return this.resolutionX;
	}
	
	/**
	 * Returns the reciprocal (or inverse) value of the resolution of the X-axis.
	 * 
	 * @return the reciprocal (or inverse) value of the resolution of the X-axis
	 */
//	TODO: Add Unit Tests!
	public final double getResolutionXReciprocal() {
		return this.resolutionXReciprocal;
	}
	
	/**
	 * Returns the resolution of the Y-axis.
	 * 
	 * @return the resolution of the Y-axis
	 */
//	TODO: Add Unit Tests!
	public final double getResolutionY() {
		return this.resolutionY;
	}
	
	/**
	 * Returns the reciprocal (or inverse) value of the resolution of the Y-axis.
	 * 
	 * @return the reciprocal (or inverse) value of the resolution of the Y-axis
	 */
//	TODO: Add Unit Tests!
	public final double getResolutionYReciprocal() {
		return this.resolutionYReciprocal;
	}
	
	/**
	 * Returns a table with cached values.
	 * 
	 * @return a table with cached values
	 */
//	TODO: Add Unit Tests!
	public final double[] createFilterTable() {
		final double[] filterTable = new double[FILTER_TABLE_SIZE * FILTER_TABLE_SIZE];
		
		final double filterResolutionX = getResolutionX();
		final double filterResolutionY = getResolutionY();
		final double filterTableSizeReciprocal = 1.0D / FILTER_TABLE_SIZE;
		
		for(int i = 0, y = 0; y < FILTER_TABLE_SIZE; y++) {
			for(int x = 0; x < FILTER_TABLE_SIZE; x++) {
				final double filterX = (x + 0.5D) * filterResolutionX * filterTableSizeReciprocal;
				final double filterY = (y + 0.5D) * filterResolutionY * filterTableSizeReciprocal;
				
				filterTable[i++] = evaluate(filterX, filterY);
			}
		}
		
		return filterTable;
	}
}