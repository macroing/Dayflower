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

import static org.dayflower.util.Floats.exp;
import static org.dayflower.util.Floats.max;

import java.lang.reflect.Field;

//TODO: Add Javadocs!
public final class GaussianFilter extends Filter {
	private final float falloff;
	private final float x;
	private final float y;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public GaussianFilter() {
		this(2.0F, 2.0F, 2.0F);
	}
	
//	TODO: Add Javadocs!
	public GaussianFilter(final float resolutionX, final float resolutionY, final float falloff) {
		super(resolutionX, resolutionY);
		
		this.falloff = falloff;
		this.x = exp(-falloff * resolutionX * resolutionX);
		this.y = exp(-falloff * resolutionY * resolutionY);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public float evaluate(final float x, final float y) {
		return doGaussian(x, this.x, this.falloff) * doGaussian(y, this.y, this.falloff);
	}
	
//	TODO: Add Javadocs!
	public float getFalloff() {
		return this.falloff;
	}
	
//	TODO: Add Javadocs!
	public float getX() {
		return this.x;
	}
	
//	TODO: Add Javadocs!
	public float getY() {
		return this.y;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float doGaussian(final float a, final float b, final float c) {
		return max(0.0F, exp(-c * a * a) - b);
	}
}