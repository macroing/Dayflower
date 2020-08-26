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

public final class Ints {
	private Ints() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static int max(final int a, final int b) {
		return Math.max(a, b);
	}
	
	public static int min(final int a, final int b) {
		return Math.min(a, b);
	}
	
	public static int modulo(final int value, final int maximumValue) {
		return value < 0 ? (value % maximumValue + maximumValue) % maximumValue : value % maximumValue;
	}
	
	public static int requireExact(final int value, final int valueExpected, final String name) {
		if(value != valueExpected) {
			throw new IllegalArgumentException(String.format("%s != %d: %s == %d", name, Integer.valueOf(valueExpected), name, Integer.valueOf(value)));
		}
		
		return value;
	}
	
	public static int requireRange(final int value, final int edgeA, final int edgeB, final String name) {
		final int minimum = min(edgeA, edgeB);
		final int maximum = max(edgeA, edgeB);
		
		if(value < minimum) {
			throw new IllegalArgumentException(String.format("%s < %d: %s == %d", name, Integer.valueOf(minimum), name, Integer.valueOf(value)));
		} else if(value > maximum) {
			throw new IllegalArgumentException(String.format("%s > %d: %s == %d", name, Integer.valueOf(maximum), name, Integer.valueOf(value)));
		} else {
			return value;
		}
	}
	
	public static int saturate(final int value) {
		return saturate(value, 0, 255);
	}
	
	public static int saturate(final int value, final int edgeA, final int edgeB) {
		final int minimum = min(edgeA, edgeB);
		final int maximum = max(edgeA, edgeB);
		
		if(value < minimum) {
			return minimum;
		} else if(value > maximum) {
			return maximum;
		} else {
			return value;
		}
	}
	
	public static int toInt(final float value) {
		return (int)(value);
	}
}