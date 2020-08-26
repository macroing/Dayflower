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

import static org.dayflower.util.Floats.sqrt;

public final class CatmullRomFilter extends Filter {
	public CatmullRomFilter(final float resolutionX, final float resolutionY) {
		super(resolutionX, resolutionY);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public float evaluate(final float x, final float y) {
		final float a = x * x + y * y;
		final float b = sqrt(a);
		final float c = b < 2.0F ? b < 1.0F ? 3.0F * b * a - 5.0F * a + 2.0F : -b * a + 5.0F * a - 8.0F * b + 4.0F : 0.0F;
		
		return c;
	}
}