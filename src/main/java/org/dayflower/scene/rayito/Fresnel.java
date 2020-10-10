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
package org.dayflower.scene.rayito;

import static org.dayflower.util.Doubles.max;
import static org.dayflower.util.Doubles.pow;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.pow;

import java.lang.reflect.Field;

//TODO: Add Javadocs!
public final class Fresnel {
	private Fresnel() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static double dielectricSchlick(final double cosTheta, final double f0) {
		return f0 + (1.0D - f0) * pow(max(1.0D - cosTheta, 0.0D), 5.0D);
	}
	
//	TODO: Add Javadocs!
	public static float dielectricSchlick(final float cosTheta, final float f0) {
		return f0 + (1.0F - f0) * pow(max(1.0F - cosTheta, 0.0F), 5.0F);
	}
}