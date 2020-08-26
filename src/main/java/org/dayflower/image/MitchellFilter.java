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

import static org.dayflower.util.Floats.abs;

public final class MitchellFilter extends Filter {
	private final float b;
	private final float c;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public MitchellFilter() {
		this(2.0F, 2.0F, 1.0F / 3.0F, 1.0F / 3.0F);
	}
	
	public MitchellFilter(final float resolutionX, final float resolutionY, final float b, final float c) {
		super(resolutionX, resolutionY);
		
		this.b = b;
		this.c = c;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public float evaluate(final float x, final float y) {
		return doMitchell(x * getResolutionXReciprocal(), this.b, this.c) * doMitchell(y * getResolutionYReciprocal(), this.b, this.c);
	}
	
	public float getB() {
		return this.b;
	}
	
	public float getC() {
		return this.c;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float doMitchell(final float x, final float b, final float c) {
		final float xAbsolute = abs(2.0F * x);
		
		if(xAbsolute > 1.0F) {
			return ((-b - 6.0F * c) * xAbsolute * xAbsolute * xAbsolute + (6.0F * b + 30.0F * c) * xAbsolute * xAbsolute + (-12.0F * b - 48.0F * c) * xAbsolute + (8.0F * b + 24.0F * c)) * (1.0F / 6.0F);
		}
		
		return ((12.0F - 9.0F * b - 6.0F * c) * xAbsolute * xAbsolute * xAbsolute + (-18.0F + 12.0F * b + 6.0F * c) * xAbsolute * xAbsolute + (6.0F - 2.0F * b)) * (1.0F / 6.0F);
	}
}