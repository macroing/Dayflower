/**
 * Copyright 2020 J&#246;rgen Lundgren
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
package org.dayflower.image;

import java.lang.reflect.Field;

//TODO: Add Javadocs!
public abstract class Filter {
//	TODO: Add Javadocs!
	public static final int FILTER_TABLE_SIZE = 16;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final float resolutionX;
	private final float resolutionXReciprocal;
	private final float resolutionY;
	private final float resolutionYReciprocal;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	protected Filter(final float resolutionX, final float resolutionY) {
		this.resolutionX = resolutionX;
		this.resolutionY = resolutionY;
		this.resolutionXReciprocal = 1.0F / this.resolutionX;
		this.resolutionYReciprocal = 1.0F / this.resolutionY;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public abstract float evaluate(final float x, final float y);
	
//	TODO: Add Javadocs!
	public final float getResolutionX() {
		return this.resolutionX;
	}
	
//	TODO: Add Javadocs!
	public final float getResolutionXReciprocal() {
		return this.resolutionXReciprocal;
	}
	
//	TODO: Add Javadocs!
	public final float getResolutionY() {
		return this.resolutionY;
	}
	
//	TODO: Add Javadocs!
	public final float getResolutionYReciprocal() {
		return this.resolutionYReciprocal;
	}
	
//	TODO: Add Javadocs!
	public final float[] createFilterTable() {
		final float[] filterTable = new float[FILTER_TABLE_SIZE * FILTER_TABLE_SIZE];
		
		final float filterResolutionX = getResolutionX();
		final float filterResolutionY = getResolutionY();
		final float filterTableSizeReciprocal = 1.0F / FILTER_TABLE_SIZE;
		
		for(int i = 0, y = 0; y < FILTER_TABLE_SIZE; y++) {
			for(int x = 0; x < FILTER_TABLE_SIZE; x++) {
				final float filterX = (x + 0.5F) * filterResolutionX * filterTableSizeReciprocal;
				final float filterY = (y + 0.5F) * filterResolutionY * filterTableSizeReciprocal;
				
				filterTable[i++] = evaluate(filterX, filterY);
			}
		}
		
		return filterTable;
	}
}