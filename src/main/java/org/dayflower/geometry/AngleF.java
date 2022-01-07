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
package org.dayflower.geometry;

import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Floats.asin;
import static org.dayflower.utility.Floats.atan;
import static org.dayflower.utility.Floats.atan2;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.max;
import static org.dayflower.utility.Floats.min;
import static org.dayflower.utility.Floats.tan;
import static org.dayflower.utility.Floats.toDegrees;
import static org.dayflower.utility.Floats.toRadians;
import static org.dayflower.utility.Floats.wrapAround;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;

import org.dayflower.java.lang.Strings;
import org.dayflower.utility.Floats;

/**
 * An {@code AngleF} represents an angle and contains {@code float}-based values for both degrees and radians.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class AngleF {
	private static final float DEGREES_MAXIMUM = 360.0F;
	private static final float DEGREES_MAXIMUM_PITCH = 90.0F;
	private static final float DEGREES_MINIMUM = 0.0F;
	private static final float DEGREES_MINIMUM_PITCH = -90.0F;
	private static final float RADIANS_MAXIMUM = PI_MULTIPLIED_BY_2;
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
	
	/**
	 * Returns a {@code String} representation of this {@code AngleF} instance.
	 * 
	 * @return a {@code String} representation of this {@code AngleF} instance
	 */
	@Override
	public String toString() {
		return String.format("AngleF.degrees(%s, %s, %s)", Strings.toNonScientificNotationJava(this.degrees), Strings.toNonScientificNotationJava(this.degreesMinimum), Strings.toNonScientificNotationJava(this.degreesMaximum));
	}
	
	/**
	 * Compares {@code object} to this {@code AngleF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code AngleF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code AngleF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code AngleF}, and their respective values are equal, {@code false} otherwise
	 */
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
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the trigonometric cosine of this {@code AngleF} instance.
	 * 
	 * @return the trigonometric cosine of this {@code AngleF} instance
	 */
	public float cos() {
		return Floats.cos(this.radians);
	}
	
	/**
	 * Returns the angle in degrees.
	 * 
	 * @return the angle in degrees
	 */
	public float getDegrees() {
		return this.degrees;
	}
	
	/**
	 * Returns the maximum angle in degrees.
	 * 
	 * @return the maximum angle in degrees
	 */
	public float getDegreesMaximum() {
		return this.degreesMaximum;
	}
	
	/**
	 * Returns the minimum angle in degrees.
	 * 
	 * @return the minimum angle in degrees
	 */
	public float getDegreesMinimum() {
		return this.degreesMinimum;
	}
	
	/**
	 * Returns the angle in radians.
	 * 
	 * @return the angle in radians
	 */
	public float getRadians() {
		return this.radians;
	}
	
	/**
	 * Returns the maximum angle in radians.
	 * 
	 * @return the maximum angle in radians
	 */
	public float getRadiansMaximum() {
		return this.radiansMaximum;
	}
	
	/**
	 * Returns the minimum angle in radians.
	 * 
	 * @return the minimum angle in radians
	 */
	public float getRadiansMinimum() {
		return this.radiansMinimum;
	}
	
	/**
	 * Returns the trigonometric sine of this {@code AngleF} instance.
	 * 
	 * @return the trigonometric sine of this {@code AngleF} instance
	 */
	public float sin() {
		return Floats.sin(this.radians);
	}
	
	/**
	 * Returns a hash code for this {@code AngleF} instance.
	 * 
	 * @return a hash code for this {@code AngleF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.degrees), Float.valueOf(this.degreesMaximum), Float.valueOf(this.degreesMinimum));
	}
	
	/**
	 * Writes this {@code AngleF} instance to {@code dataOutput}.
	 * <p>
	 * If {@code dataOutput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataOutput the {@code DataOutput} instance to write to
	 * @throws NullPointerException thrown if, and only if, {@code dataOutput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public void write(final DataOutput dataOutput) {
		try {
			dataOutput.writeFloat(this.degrees);
			dataOutput.writeFloat(this.degreesMinimum);
			dataOutput.writeFloat(this.degreesMaximum);
			dataOutput.writeFloat(this.radians);
			dataOutput.writeFloat(this.radiansMinimum);
			dataOutput.writeFloat(this.radiansMaximum);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds {@code angleRHS} to {@code angleLHS}.
	 * <p>
	 * Returns a new {@code AngleF} instance with the result of the addition.
	 * <p>
	 * If either {@code angleLHS} or {@code angleRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param angleLHS an {@code AngleF} instance
	 * @param angleRHS an {@code AngleF} instance
	 * @return a new {@code AngleF} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code angleLHS} or {@code angleRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static AngleF add(final AngleF angleLHS, final AngleF angleRHS) {
		final float degreesMinimum = min(angleLHS.degreesMinimum, angleRHS.degreesMinimum);
		final float degreesMaximum = max(angleLHS.degreesMaximum, angleRHS.degreesMaximum);
		final float degrees = wrapAround(angleLHS.degrees + angleRHS.degrees, degreesMinimum, degreesMaximum);
		
		return degrees(degrees, degreesMinimum, degreesMaximum);
	}
	
	/**
	 * Returns a new {@code AngleF} instance based on an angle in degrees.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * AngleF.degrees(degrees, 0.0F, 360.0F)
	 * }
	 * </pre>
	 * 
	 * @param degrees the angle in degrees
	 * @return a new {@code AngleF} instance based on an angle in degrees
	 */
	public static AngleF degrees(final float degrees) {
		return degrees(degrees, DEGREES_MINIMUM, DEGREES_MAXIMUM);
	}
	
	/**
	 * Returns a new {@code AngleF} instance based on an angle in degrees and an interval of valid degrees.
	 * 
	 * @param degrees the angle in degrees
	 * @param degreesIntervalEndA the degrees that represents one of the ends of the interval of valid degrees
	 * @param degreesIntervalEndB the degrees that represents one of the ends of the interval of valid degrees
	 * @return a new {@code AngleF} instance based on an angle in degrees and an interval of valid degrees
	 */
	public static AngleF degrees(final float degrees, final float degreesIntervalEndA, final float degreesIntervalEndB) {
		final float newDegreesMinimum = min(degreesIntervalEndA, degreesIntervalEndB);
		final float newDegreesMaximum = max(degreesIntervalEndA, degreesIntervalEndB);
		final float newDegrees = wrapAround(degrees, newDegreesMinimum, newDegreesMaximum);
		
		final float newRadians = toRadians(newDegrees);
		final float newRadiansMinimum = toRadians(newDegreesMinimum);
		final float newRadiansMaximum = toRadians(newDegreesMaximum);
		
		return new AngleF(newDegrees, newDegreesMinimum, newDegreesMaximum, newRadians, newRadiansMinimum, newRadiansMaximum);
	}
	
	/**
	 * Returns a field of view (FOV) {@code AngleF} based on {@code focalDistance} and {@code resolution}.
	 * <p>
	 * This method allows you to use {@code resolution} in either X- or Y-direction. So, either width or height.
	 * 
	 * @param focalDistance the focal distance (also known as focal length}
	 * @param resolution the resolution in X- or Y-direction (width or height)
	 * @return a field of view (FOV) {@code AngleF} based on {@code focalDistance} and {@code resolution}
	 */
//	TODO: Add Unit Tests!
	public static AngleF fieldOfView(final float focalDistance, final float resolution) {
		return radians(2.0F * atan(resolution * 0.5F / focalDistance));
	}
	
	/**
	 * Returns a horizontal field of view (FOV) {@code AngleF} based on {@code fieldOfViewY}, {@code resolutionX} and {@code resolutionY}.
	 * <p>
	 * If {@code fieldOfViewY} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param fieldOfViewY the vertical field of view
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @return a horizontal field of view (FOV) {@code AngleF} based on {@code fieldOfViewY}, {@code resolutionX} and {@code resolutionY}
	 * @throws NullPointerException thrown if, and only if, {@code fieldOfViewY} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static AngleF fieldOfViewX(final AngleF fieldOfViewY, final float resolutionX, final float resolutionY) {
		return radians(2.0F * atan(tan(fieldOfViewY.radians * 0.5F) * (resolutionX / resolutionY)));
	}
	
	/**
	 * Returns a vertical field of view (FOV) {@code AngleF} based on {@code fieldOfViewX}, {@code resolutionX} and {@code resolutionY}.
	 * <p>
	 * If {@code fieldOfViewX} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param fieldOfViewX the horizontal field of view
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @return a vertical field of view (FOV) {@code AngleF} based on {@code fieldOfViewX}, {@code resolutionX} and {@code resolutionY}
	 * @throws NullPointerException thrown if, and only if, {@code fieldOfViewX} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static AngleF fieldOfViewY(final AngleF fieldOfViewX, final float resolutionX, final float resolutionY) {
		return radians(2.0F * atan(tan(fieldOfViewX.radians * 0.5F) * (resolutionY / resolutionX)));
	}
	
	/**
	 * Returns a new {@code AngleF} instance that represents half of {@code angle}.
	 * <p>
	 * If {@code angle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param angle an {@code AngleF} instance
	 * @return a new {@code AngleF} instance that represents half of {@code angle}
	 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static AngleF half(final AngleF angle) {
		final float degreesMinimum = angle.degreesMinimum;
		final float degreesMaximum = angle.degreesMaximum;
		final float degrees = wrapAround(angle.degrees * 0.5F, degreesMinimum, degreesMaximum);
		
		return degrees(degrees, degreesMinimum, degreesMaximum);
	}
	
	/**
	 * Returns an {@code AngleF} instance that is a negated version of {@code angle}.
	 * <p>
	 * If {@code angle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param angle an {@code AngleF} instance
	 * @return an {@code AngleF} instance that is a negated version of {@code angle}
	 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static AngleF negate(final AngleF angle) {
		final float degreesMinimum = -angle.degreesMinimum;
		final float degreesMaximum = -angle.degreesMaximum;
		final float degrees = wrapAround(-angle.degrees, degreesMinimum, degreesMaximum);
		
		return degrees(degrees, degreesMinimum, degreesMaximum);
	}
	
	/**
	 * Returns a new pitch {@code AngleF} instance based on {@code eye} and {@code lookAt}.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye the {@link Point3F} on which the "eye" is positioned
	 * @param lookAt the {@code Point3F} to which the "eye" is looking
	 * @return a new pitch {@code AngleF} instance based on {@code eye} and {@code lookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static AngleF pitch(final Point3F eye, final Point3F lookAt) {
		return pitch(Vector3F.directionNormalized(eye, lookAt));
	}
	
	/**
	 * Returns a new pitch {@code AngleF} instance based on {@code direction}.
	 * <p>
	 * If {@code direction} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param direction a normalized direction {@link Vector3F}
	 * @return a new pitch {@code AngleF} instance based on {@code direction}
	 * @throws NullPointerException thrown if, and only if, {@code direction} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static AngleF pitch(final Vector3F direction) {
		return degrees(toDegrees(asin(direction.getY())), DEGREES_MINIMUM_PITCH, DEGREES_MAXIMUM_PITCH);
	}
	
	/**
	 * Returns a new {@code AngleF} instance based on an angle in radians.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * AngleF.radians(radians, 0.0F, PI * 2.0F)
	 * }
	 * </pre>
	 * 
	 * @param radians the angle in radians
	 * @return a new {@code AngleF} instance based on an angle in radians
	 */
//	TODO: Add Unit Tests!
	public static AngleF radians(final float radians) {
		return radians(radians, RADIANS_MINIMUM, RADIANS_MAXIMUM);
	}
	
	/**
	 * Returns a new {@code AngleF} instance based on an angle in radians and an interval of valid radians.
	 * 
	 * @param radians the angle in radians
	 * @param radiansIntervalEndA the radians that represents one of the ends of the interval of valid radians
	 * @param radiansIntervalEndB the radians that represents one of the ends of the interval of valid radians
	 * @return a new {@code AngleF} instance based on an angle in radians and an interval of valid radians
	 */
//	TODO: Add Unit Tests!
	public static AngleF radians(final float radians, final float radiansIntervalEndA, final float radiansIntervalEndB) {
		final float newRadiansMinimum = min(radiansIntervalEndA, radiansIntervalEndB);
		final float newRadiansMaximum = max(radiansIntervalEndA, radiansIntervalEndB);
		final float newRadians = wrapAround(radians, newRadiansMinimum, newRadiansMaximum);
		
		final float newDegrees = toDegrees(newRadians);
		final float newDegreesMinimum = toDegrees(newRadiansMinimum);
		final float newDegreesMaximum = toDegrees(newRadiansMaximum);
		
		return new AngleF(newDegrees, newDegreesMinimum, newDegreesMaximum, newRadians, newRadiansMinimum, newRadiansMaximum);
	}
	
	/**
	 * Returns a new {@code AngleF} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code AngleF} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	public static AngleF read(final DataInput dataInput) {
		try {
			final float degrees = dataInput.readFloat();
			final float degreesMinimum = dataInput.readFloat();
			final float degreesMaximum = dataInput.readFloat();
			final float radians = dataInput.readFloat();
			final float radiansMinimum = dataInput.readFloat();
			final float radiansMaximum = dataInput.readFloat();
			
			return new AngleF(degrees, degreesMinimum, degreesMaximum, radians, radiansMinimum, radiansMaximum);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Subtracts {@code angleRHS} from {@code angleLHS}.
	 * <p>
	 * Returns a new {@code AngleF} instance with the result of the subtraction.
	 * <p>
	 * If either {@code angleLHS} or {@code angleRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param angleLHS an {@code AngleF} instance
	 * @param angleRHS an {@code AngleF} instance
	 * @return a new {@code AngleF} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code angleLHS} or {@code angleRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static AngleF subtract(final AngleF angleLHS, final AngleF angleRHS) {
		final float degreesMinimum = min(angleLHS.degreesMinimum, angleRHS.degreesMinimum);
		final float degreesMaximum = max(angleLHS.degreesMaximum, angleRHS.degreesMaximum);
		final float degrees = wrapAround(angleLHS.degrees - angleRHS.degrees, degreesMinimum, degreesMaximum);
		
		return degrees(degrees, degreesMinimum, degreesMaximum);
	}
	
	/**
	 * Returns a new yaw {@code AngleF} instance based on {@code eye} and {@code lookAt}.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye the {@link Point3F} on which the "eye" is positioned
	 * @param lookAt the {@code Point3F} to which the "eye" is looking
	 * @return a new yaw {@code AngleF} instance based on {@code eye} and {@code lookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static AngleF yaw(final Point3F eye, final Point3F lookAt) {
		return yaw(Vector3F.directionNormalized(eye, lookAt));
	}
	
	/**
	 * Returns a new yaw {@code AngleF} instance based on {@code direction}.
	 * <p>
	 * If {@code direction} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param direction a normalized direction {@link Vector3F}
	 * @return a new yaw {@code AngleF} instance based on {@code direction}
	 * @throws NullPointerException thrown if, and only if, {@code direction} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static AngleF yaw(final Vector3F direction) {
		return degrees(toDegrees(atan2(direction.getX(), direction.getZ())));
	}
}