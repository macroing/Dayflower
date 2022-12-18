/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
package org.dayflower.test;

import org.macroing.java.lang.Doubles;

public final class Algorithms {
	private Algorithms() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static double distanceKm(final double[] pointA, final double[] pointB) {
		final double radius = 6371.0D;
		
		final double a = Doubles.sin(Doubles.toRadians(getLatitude(pointB) - getLatitude(pointA)) / 2.0D);
		final double b = Doubles.cos(Doubles.toRadians(getLatitude(pointA)));
		final double c = Doubles.cos(Doubles.toRadians(getLatitude(pointB)));
		final double d = Doubles.sin(Doubles.toRadians(getLongitude(pointB) - getLongitude(pointA)) / 2.0D);
		final double e = a * a + b * c * d * d;
		
		final double x = Doubles.sqrt(1.0D - e);
		final double y = Doubles.sqrt(e);
		
		final double distance = radius * 2.0D * Doubles.atan2(y, x);
		
		return distance;
	}
	
	public static double getLongitude(final double[] point) {
		return point[0];
	}
	
	public static double getLatitude(final double[] point) {
		return point[1];
	}
	
	public static double[] addDistanceKm(final double[] point, final double distanceKmLongitude, final double distanceKmLatitude) {
		final double radius = 6371.0D;
		
		final double oldLongitude = getLongitude(point);
		final double oldLatitude = getLatitude(point);
		
		final double newLongitude = oldLongitude + Doubles.toDegrees(distanceKmLongitude / radius) / Doubles.cos(Doubles.toRadians(oldLatitude));
		final double newLatitude = oldLatitude + Doubles.toDegrees(distanceKmLatitude / radius);
		
		return new double[] {newLongitude, newLatitude};
	}
}