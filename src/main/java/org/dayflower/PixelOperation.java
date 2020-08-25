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

import static org.dayflower.Ints.modulo;

public enum PixelOperation {
	NO_CHANGE,
	WRAP_AROUND;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private PixelOperation() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public int getIndex(final int index, final int resolution) {
		switch(this) {
			case NO_CHANGE:
				return index;
			case WRAP_AROUND:
				return modulo(index, resolution);
			default:
				return index;
		}
	}
	
	public int getX(final int x, final int resolutionX) {
		switch(this) {
			case NO_CHANGE:
				return x;
			case WRAP_AROUND:
				return modulo(x, resolutionX);
			default:
				return x;
		}
	}
	
	public int getY(final int y, final int resolutionY) {
		switch(this) {
			case NO_CHANGE:
				return y;
			case WRAP_AROUND:
				return modulo(y, resolutionY);
			default:
				return y;
		}
	}
}