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

import static org.dayflower.utility.Doubles.atan2;
import static org.dayflower.utility.Doubles.cos;
import static org.dayflower.utility.Doubles.sin;
import static org.dayflower.utility.Doubles.sqrt;
import static org.dayflower.utility.Doubles.toDegrees;
import static org.dayflower.utility.Doubles.toRadians;

public final class Algorithms {
	private Algorithms() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static double distanceKm(final double[] pointA, final double[] pointB) {
		final double radius = 6371.0D;
		
		final double a = sin(toRadians(getLatitude(pointB) - getLatitude(pointA)) / 2.0D);
		final double b = cos(toRadians(getLatitude(pointA)));
		final double c = cos(toRadians(getLatitude(pointB)));
		final double d = sin(toRadians(getLongitude(pointB) - getLongitude(pointA)) / 2.0D);
		final double e = a * a + b * c * d * d;
		
		final double x = sqrt(1.0D - e);
		final double y = sqrt(e);
		
		final double distance = radius * 2.0D * atan2(y, x);
		
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
		
		final double newLongitude = oldLongitude + toDegrees(distanceKmLongitude / radius) / cos(toRadians(oldLatitude));
		final double newLatitude = oldLatitude + toDegrees(distanceKmLatitude / radius);
		
		return new double[] {newLongitude, newLatitude};
	}
}