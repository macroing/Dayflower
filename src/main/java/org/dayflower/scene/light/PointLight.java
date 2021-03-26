/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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
package org.dayflower.scene.light;

import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_4;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.LightSample;
import org.dayflower.scene.Transform;

/**
 * A {@code PointLight} is an implementation of {@link Light} that represents a point light.
 * <p>
 * This class is mutable and not thread-safe.
 * <p>
 * This {@code Light} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PointLight extends Light {
	/**
	 * The name of this {@code PointLight} class.
	 */
	public static final String NAME = "Point Light";
	
	/**
	 * The length of the {@code float[]}.
	 */
	public static final int ARRAY_LENGTH = 8;
	
	/**
	 * The offset for the {@link Color3F} denoted by {@code Intensity} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_INTENSITY = 0;
	
	/**
	 * The offset for the {@link Point3F} denoted by {@code Position} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_POSITION = 3;
	
	/**
	 * The ID of this {@code PointLight} class.
	 */
	public static final int ID = 5;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Color3F intensity;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PointLight} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PointLight(Color3F.WHITE);
	 * }
	 * </pre>
	 */
	public PointLight() {
		this(Color3F.WHITE);
	}
	
	/**
	 * Constructs a new {@code PointLight} instance.
	 * <p>
	 * If {@code intensity} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PointLight(intensity, new Point3F());
	 * }
	 * </pre>
	 * 
	 * @param intensity a {@link Color3F} instance with the intensity associated with this {@code PointLight} instance
	 * @throws NullPointerException thrown if, and only if, {@code intensity} is {@code null}
	 */
	public PointLight(final Color3F intensity) {
		this(intensity, new Point3F());
	}
	
	/**
	 * Constructs a new {@code PointLight} instance.
	 * <p>
	 * If either {@code intensity} or {@code position} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intensity a {@link Color3F} instance with the intensity associated with this {@code PointLight} instance
	 * @param position a {@link Point3F} instance with the position associated with this {@code PointLight} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intensity} or {@code position} are {@code null}
	 */
	public PointLight(final Color3F intensity, final Point3F position) {
		super(new Transform(Objects.requireNonNull(position, "position == null")), 1, true);
		
		this.intensity = Objects.requireNonNull(intensity, "intensity == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the intensity associated with this {@code PointLight} instance.
	 * 
	 * @return a {@code Color3F} instance with the intensity associated with this {@code PointLight} instance
	 */
	public Color3F getIntensity() {
		return this.intensity;
	}
	
	/**
	 * Returns a {@link Color3F} instance with the power of this {@code PointLight} instance.
	 * 
	 * @return a {@code Color3F} instance with the power of this {@code PointLight} instance
	 */
	@Override
	public Color3F power() {
		return Color3F.multiply(this.intensity, PI_MULTIPLIED_BY_4);
	}
	
	/**
	 * Samples the incoming radiance.
	 * <p>
	 * Returns an optional {@link LightSample} with the result of the sampling.
	 * <p>
	 * If either {@code intersection} or {@code sample} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param sample a {@link Point2F} instance
	 * @return an optional {@code LightSample} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code sample} are {@code null}
	 */
	@Override
	public Optional<LightSample> sampleRadianceIncoming(final Intersection intersection, final Point2F sample) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(sample, "sample == null");
		
		final Point3F position = getTransform().getPosition();
		final Point3F surfaceIntersectionPoint = intersection.getSurfaceIntersectionPoint();
		
		final Color3F result = Color3F.divide(this.intensity, Point3F.distanceSquared(surfaceIntersectionPoint, position));
		
		final Vector3F incoming = Vector3F.normalize(Vector3F.direction(surfaceIntersectionPoint, position));
		
		final float probabilityDensityFunctionValue = 1.0F;
		
		return Optional.of(new LightSample(result, position, incoming, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code PointLight} instance.
	 * 
	 * @return a {@code String} with the name of this {@code PointLight} instance
	 */
	@SuppressWarnings("static-method")
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code PointLight} instance.
	 * 
	 * @return a {@code String} representation of this {@code PointLight} instance
	 */
	@Override
	public String toString() {
		return String.format("new PointLight(%s, %s)", getTransform().getPosition(), this.intensity);
	}
	
	/**
	 * Compares {@code object} to this {@code PointLight} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code PointLight}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code PointLight} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code PointLight}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof PointLight)) {
			return false;
		} else if(!Objects.equals(getTransform(), PointLight.class.cast(object).getTransform())) {
			return false;
		} else if(!Objects.equals(this.intensity, PointLight.class.cast(object).intensity)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code PointLight} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code PointLight} instance
	 */
	public float[] toArray() {
		final float[] array = new float[ARRAY_LENGTH];
		
//		Because the PointLight occupy 8/8 positions in a block, it should be aligned.
		array[ARRAY_OFFSET_INTENSITY + 0] = this.intensity.getR();				//Block #1
		array[ARRAY_OFFSET_INTENSITY + 1] = this.intensity.getG();				//Block #1
		array[ARRAY_OFFSET_INTENSITY + 2] = this.intensity.getB();				//Block #1
		array[ARRAY_OFFSET_POSITION + 0] = getTransform().getPosition().getX();	//Block #1
		array[ARRAY_OFFSET_POSITION + 1] = getTransform().getPosition().getY();	//Block #1
		array[ARRAY_OFFSET_POSITION + 2] = getTransform().getPosition().getZ();	//Block #1
		array[6] = 0.0F;														//Block #1
		array[7] = 0.0F;														//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code PointLight} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code PointLight} instance
	 */
	@SuppressWarnings("static-method")
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code PointLight} instance.
	 * 
	 * @return a hash code for this {@code PointLight} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getTransform(), this.intensity);
	}
}