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
package org.macroing.dayflower;

import static org.macroing.dayflower.Floats.PI_MULTIPLIED_BY_TWO;
import static org.macroing.dayflower.Floats.asin;
import static org.macroing.dayflower.Floats.atan;
import static org.macroing.dayflower.Floats.atan2;
import static org.macroing.dayflower.Floats.equal;
import static org.macroing.dayflower.Floats.max;
import static org.macroing.dayflower.Floats.min;
import static org.macroing.dayflower.Floats.tan;
import static org.macroing.dayflower.Floats.toDegrees;
import static org.macroing.dayflower.Floats.toRadians;
import static org.macroing.dayflower.Floats.wrapAround;

import java.util.Objects;

public final class AngleF {
	private static final float DEGREES_MAXIMUM = 360.0F;
	private static final float DEGREES_MAXIMUM_PITCH = 90.0F;
	private static final float DEGREES_MINIMUM = 0.0F;
	private static final float DEGREES_MINIMUM_PITCH = -90.0F;
	private static final float RADIANS_MAXIMUM = PI_MULTIPLIED_BY_TWO;
	private static final float RADIANS_MINIMUM = 0.0F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final float degrees;
	private final float degreesMaximum;
	private final float degreesMinimum;
	private final float radians;
	private final float radiansMaximum;
	private final float radiansMinimum;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private AngleF(final float degrees, final float degreesMinimum, final float degreesMaximum, final float radians, final float radiansMinimum, final float radiansMaximum) {
		this.degrees = degrees;
		this.degreesMinimum = degreesMinimum;
		this.degreesMaximum = degreesMaximum;
		this.radians = radians;
		this.radiansMinimum = radiansMinimum;
		this.radiansMaximum = radiansMaximum;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public String toString() {
		return String.format("AngleF.degrees(%+.10f, %+.10f, %+.10f)", Float.valueOf(this.degrees), Float.valueOf(this.degreesMinimum), Float.valueOf(this.degreesMaximum));
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof AngleF)) {
			return false;
		} else if(!equal(this.degrees, AngleF.class.cast(object).degrees)) {
			return false;
		} else if(!equal(this.degreesMaximum, AngleF.class.cast(object).degreesMaximum)) {
			return false;
		} else if(!equal(this.degreesMinimum, AngleF.class.cast(object).degreesMinimum)) {
			return false;
		} else if(!equal(this.radians, AngleF.class.cast(object).radians)) {
			return false;
		} else if(!equal(this.radiansMaximum, AngleF.class.cast(object).radiansMaximum)) {
			return false;
		} else if(!equal(this.radiansMinimum, AngleF.class.cast(object).radiansMinimum)) {
			return false;
		} else {
			return true;
		}
	}
	
	public float getDegrees() {
		return this.degrees;
	}
	
	public float getDegreesMaximum() {
		return this.degreesMaximum;
	}
	
	public float getDegreesMinimum() {
		return this.degreesMinimum;
	}
	
	public float getRadians() {
		return this.radians;
	}
	
	public float getRadiansMaximum() {
		return this.radiansMaximum;
	}
	
	public float getRadiansMinimum() {
		return this.radiansMinimum;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.degrees), Float.valueOf(this.degreesMaximum), Float.valueOf(this.degreesMinimum), Float.valueOf(this.radians), Float.valueOf(this.radiansMaximum), Float.valueOf(this.radiansMinimum));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static AngleF add(final AngleF aLHS, final AngleF aRHS) {
		final float degreesMinimum = min(aLHS.degreesMinimum, aRHS.degreesMinimum);
		final float degreesMaximum = max(aLHS.degreesMaximum, aRHS.degreesMaximum);
		final float degrees = wrapAround(aLHS.degrees + aRHS.degrees, degreesMinimum, degreesMaximum);
		
		return degrees(degrees, degreesMinimum, degreesMaximum);
	}
	
	public static AngleF degrees(final float degrees) {
		return degrees(degrees, DEGREES_MINIMUM, DEGREES_MAXIMUM);
	}
	
	public static AngleF degrees(final float degrees, final float degreesEdgeA, final float degreesEdgeB) {
		final float newDegreesMinimum = min(degreesEdgeA, degreesEdgeB);
		final float newDegreesMaximum = max(degreesEdgeA, degreesEdgeB);
		final float newDegrees = wrapAround(degrees, newDegreesMinimum, newDegreesMaximum);
		
		final float newRadians = toRadians(newDegrees);
		final float newRadiansMinimum = toRadians(newDegreesMinimum);
		final float newRadiansMaximum = toRadians(newDegreesMaximum);
		
		return new AngleF(newDegrees, newDegreesMinimum, newDegreesMaximum, newRadians, newRadiansMinimum, newRadiansMaximum);
	}
	
	public static AngleF fieldOfView(final float focalDistance, final float resolution) {
		return radians(2.0F * atan(resolution * 0.5F / focalDistance));
	}
	
	public static AngleF fieldOfViewX(final AngleF aFieldOfViewY, final float resolutionX, final float resolutionY) {
		return radians(2.0F * atan(tan(aFieldOfViewY.radians * 0.5F) * (resolutionX / resolutionY)));
	}
	
	public static AngleF fieldOfViewY(final AngleF aFieldOfViewX, final float resolutionX, final float resolutionY) {
		return radians(2.0F * atan(tan(aFieldOfViewX.radians * 0.5F) * (resolutionY / resolutionX)));
	}
	
	public static AngleF half(final AngleF a) {
		final float degreesMinimum = a.degreesMinimum;
		final float degreesMaximum = a.degreesMaximum;
		final float degrees = wrapAround(a.degrees * 0.5F, degreesMinimum, degreesMaximum);
		
		return degrees(degrees, degreesMinimum, degreesMaximum);
	}
	
	public static AngleF pitch(final Point3F eye, final Point3F lookAt) {
		return pitch(Vector3F.directionNormalized(eye, lookAt));
	}
	
	public static AngleF pitch(final Vector3F direction) {
		return degrees(toDegrees(asin(direction.getY())), DEGREES_MINIMUM_PITCH, DEGREES_MAXIMUM_PITCH);
	}
	
	public static AngleF radians(final float radians) {
		return radians(radians, RADIANS_MINIMUM, RADIANS_MAXIMUM);
	}
	
	public static AngleF radians(final float radians, final float radiansEdgeA, final float radiansEdgeB) {
		final float newRadiansMinimum = min(radiansEdgeA, radiansEdgeB);
		final float newRadiansMaximum = max(radiansEdgeA, radiansEdgeB);
		final float newRadians = wrapAround(radians, newRadiansMinimum, newRadiansMaximum);
		
		final float newDegrees = toDegrees(newRadians);
		final float newDegreesMinimum = toDegrees(newRadiansMinimum);
		final float newDegreesMaximum = toDegrees(newRadiansMaximum);
		
		return new AngleF(newDegrees, newDegreesMinimum, newDegreesMaximum, newRadians, newRadiansMinimum, newRadiansMaximum);
	}
	
	public static AngleF subtract(final AngleF aLHS, final AngleF aRHS) {
		final float degreesMinimum = min(aLHS.degreesMinimum, aRHS.degreesMinimum);
		final float degreesMaximum = max(aLHS.degreesMaximum, aRHS.degreesMaximum);
		final float degrees = wrapAround(aLHS.degrees - aRHS.degrees, degreesMinimum, degreesMaximum);
		
		return degrees(degrees, degreesMinimum, degreesMaximum);
	}
	
	public static AngleF yaw(final Point3F eye, final Point3F lookAt) {
		return yaw(Vector3F.directionNormalized(eye, lookAt));
	}
	
	public static AngleF yaw(final Vector3F direction) {
		return degrees(toDegrees(atan2(direction.getX(), direction.getZ())));
	}
}