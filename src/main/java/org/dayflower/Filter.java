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
package org.dayflower;

public abstract class Filter {
	private final float resolutionX;
	private final float resolutionXReciprocal;
	private final float resolutionY;
	private final float resolutionYReciprocal;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	protected Filter(final float resolutionX, final float resolutionY) {
		this.resolutionX = resolutionX;
		this.resolutionY = resolutionY;
		this.resolutionXReciprocal = 1.0F / this.resolutionX;
		this.resolutionYReciprocal = 1.0F / this.resolutionY;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public abstract float evaluate(final float x, final float y);
	
	public final float getResolutionX() {
		return this.resolutionX;
	}
	
	public final float getResolutionXReciprocal() {
		return this.resolutionXReciprocal;
	}
	
	public final float getResolutionY() {
		return this.resolutionY;
	}
	
	public final float getResolutionYReciprocal() {
		return this.resolutionYReciprocal;
	}
}