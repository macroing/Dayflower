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
package org.dayflower.util;

import java.lang.reflect.Field;
import java.util.concurrent.ThreadLocalRandom;

//TODO: Add Javadocs!
public final class Floats {
//	TODO: Add Javadocs!
	public static final float PI = toFloat(Math.PI);
	
//	TODO: Add Javadocs!
	public static final float PI_DIVIDED_BY_FOUR = PI / 4.0F;
	
//	TODO: Add Javadocs!
	public static final float PI_DIVIDED_BY_TWO = PI / 2.0F;
	
//	TODO: Add Javadocs!
	public static final float PI_MULTIPLIED_BY_TWO = PI * 2.0F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Floats() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static boolean equal(final float a, final float b) {
		return Float.compare(a, b) == 0;
	}
	
//	TODO: Add Javadocs!
	public static boolean equal(final float a, final float b, final float c) {
		return equal(a, b) && equal(b, c);
	}
	
//	TODO: Add Javadocs!
	public static boolean isNaN(final float value) {
		return Float.isNaN(value);
	}
	
//	TODO: Add Javadocs!
	public static float abs(final float value) {
		return Math.abs(value);
	}
	
//	TODO: Add Javadocs!
	public static float asin(final float value) {
		return toFloat(Math.asin(value));
	}
	
//	TODO: Add Javadocs!
	public static float atan(final float value) {
		return toFloat(Math.atan(value));
	}
	
//	TODO: Add Javadocs!
	public static float atan2(final float y, final float x) {
		return toFloat(Math.atan2(y, x));
	}
	
//	TODO: Add Javadocs!
	public static float ceil(final float value) {
		return toFloat(Math.ceil(value));
	}
	
//	TODO: Add Javadocs!
	public static float cos(final float angleRadians) {
		return toFloat(Math.cos(angleRadians));
	}
	
//	TODO: Add Javadocs!
	public static float exp(final float exponent) {
		return toFloat(Math.exp(exponent));
	}
	
//	TODO: Add Javadocs!
	public static float floor(final float value) {
		return toFloat(Math.floor(value));
	}
	
//	TODO: Add Javadocs!
	public static float lerp(final float value1, final float value2, final float t) {
		return (1.0F - t) * value1 + t * value2;
	}
	
//	TODO: Add Javadocs!
	public static float max(final float a, final float b) {
		return Math.max(a, b);
	}
	
//	TODO: Add Javadocs!
	public static float max(final float a, final float b, final float c) {
		return max(max(a, b), c);
	}
	
//	TODO: Add Javadocs!
	public static float min(final float a, final float b) {
		return Math.min(a, b);
	}
	
//	TODO: Add Javadocs!
	public static float min(final float a, final float b, final float c) {
		return min(min(a, b), c);
	}
	
//	TODO: Add Javadocs!
	public static float pow(final float base, final float exponent) {
		return toFloat(Math.pow(base, exponent));
	}
	
//	TODO: Add Javadocs!
	public static float random() {
		return ThreadLocalRandom.current().nextFloat();
	}
	
//	TODO: Add Javadocs!
	public static float saturate(final float value) {
		return saturate(value, 0.0F, 1.0F);
	}
	
//	TODO: Add Javadocs!
	public static float saturate(final float value, final float edgeA, final float edgeB) {
		final float minimum = min(edgeA, edgeB);
		final float maximum = max(edgeA, edgeB);
		
		if(value < minimum) {
			return minimum;
		} else if(value > maximum) {
			return maximum;
		} else {
			return value;
		}
	}
	
//	TODO: Add Javadocs!
	public static float sin(final float angleRadians) {
		return toFloat(Math.sin(angleRadians));
	}
	
//	TODO: Add Javadocs!
	public static float sqrt(final float value) {
		return toFloat(Math.sqrt(value));
	}
	
//	TODO: Add Javadocs!
	public static float tan(final float angleRadians) {
		return toFloat(Math.tan(angleRadians));
	}
	
//	TODO: Add Javadocs!
	public static float toDegrees(final float angleRadians) {
		return toFloat(Math.toDegrees(angleRadians));
	}
	
//	TODO: Add Javadocs!
	public static float toFloat(final double value) {
		return (float)(value);
	}
	
//	TODO: Add Javadocs!
	public static float toRadians(final float angleDegrees) {
		return toFloat(Math.toRadians(angleDegrees));
	}
	
//	TODO: Add Javadocs!
	public static float wrapAround(final float value, final float a, final float b) {
		final float minimumValue = min(a, b);
		final float maximumValue = max(a, b);
		
		float currentValue = value;
		
		while(currentValue < minimumValue || currentValue > maximumValue) {
			if(currentValue < minimumValue) {
				currentValue = maximumValue - (minimumValue - currentValue);
			} else if(currentValue > maximumValue) {
				currentValue = minimumValue + (currentValue - maximumValue);
			}
		}
		
		return currentValue;
	}
}