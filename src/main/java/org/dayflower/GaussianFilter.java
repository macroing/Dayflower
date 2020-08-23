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

import static org.dayflower.Floats.exp;
import static org.dayflower.Floats.max;

public final class GaussianFilter extends Filter {
	private final float falloff;
	private final float x;
	private final float y;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public GaussianFilter() {
		this(2.0F, 2.0F, 2.0F);
	}
	
	public GaussianFilter(final float resolutionX, final float resolutionY, final float falloff) {
		super(resolutionX, resolutionY);
		
		this.falloff = falloff;
		this.x = exp(-falloff * resolutionX * resolutionX);
		this.y = exp(-falloff * resolutionY * resolutionY);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public float evaluate(final float x, final float y) {
		return doGaussian(x, this.x, this.falloff) * doGaussian(y, this.y, this.falloff);
	}
	
	public float getFalloff() {
		return this.falloff;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float doGaussian(final float a, final float b, final float c) {
		return max(0.0F, exp(-c * a * a) - b);
	}
}