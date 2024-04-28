/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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

import static org.dayflower.utility.Floats.PI;
import static org.dayflower.utility.Floats.equal;

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
 * A {@code DirectionalLight} is an implementation of {@link Light} that represents a directional light.
 * <p>
 * This class is mutable and not thread-safe.
 * <p>
 * This {@code Light} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DirectionalLight extends Light {
	/**
	 * The name of this {@code DirectionalLight} class.
	 */
	public static final String NAME = "Directional Light";
	
	/**
	 * The ID of this {@code DirectionalLight} class.
	 */
	public static final int ID = 2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Color3F radiance;
	private final Vector3F direction;
	private final float radius;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DirectionalLight} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DirectionalLight(new Transform());
	 * }
	 * </pre>
	 */
	public DirectionalLight() {
		this(new Transform());
	}
	
	/**
	 * Constructs a new {@code DirectionalLight} instance.
	 * <p>
	 * If {@code transform} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DirectionalLight(transform, Color3F.WHITE);
	 * }
	 * </pre>
	 * 
	 * @param transform the {@link Transform} instance associated with this {@code DirectionalLight} instance
	 * @throws NullPointerException thrown if, and only if, {@code transform} is {@code null}
	 */
	public DirectionalLight(final Transform transform) {
		this(transform, Color3F.WHITE);
	}
	
	/**
	 * Constructs a new {@code DirectionalLight} instance.
	 * <p>
	 * If either {@code transform} or {@code radiance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DirectionalLight(transform, radiance, Vector3F.y());
	 * }
	 * </pre>
	 * 
	 * @param transform the {@link Transform} instance associated with this {@code DirectionalLight} instance
	 * @param radiance the {@link Color3F} instance that represents the radiance associated with this {@code DirectionalLight} instance
	 * @throws NullPointerException thrown if, and only if, either {@code transform} or {@code radiance} are {@code null}
	 */
	public DirectionalLight(final Transform transform, final Color3F radiance) {
		this(transform, radiance, Vector3F.y());
	}
	
	/**
	 * Constructs a new {@code DirectionalLight} instance.
	 * <p>
	 * If either {@code transform}, {@code radiance} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DirectionalLight(transform, radiance, direction, 10.0F);
	 * }
	 * </pre>
	 * 
	 * @param transform the {@link Transform} instance associated with this {@code DirectionalLight} instance
	 * @param radiance the {@link Color3F} instance that represents the radiance associated with this {@code DirectionalLight} instance
	 * @param direction the {@link Vector3F} instance that represents the direction associated with this {@code DirectionalLight} instance
	 * @throws NullPointerException thrown if, and only if, either {@code transform}, {@code radiance} or {@code direction} are {@code null}
	 */
	public DirectionalLight(final Transform transform, final Color3F radiance, final Vector3F direction) {
		this(transform, radiance, direction, 10.0F);
	}
	
	/**
	 * Constructs a new {@code DirectionalLight} instance.
	 * <p>
	 * If either {@code transform}, {@code radiance} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param transform the {@link Transform} instance associated with this {@code DirectionalLight} instance
	 * @param radiance the {@link Color3F} instance that represents the radiance associated with this {@code DirectionalLight} instance
	 * @param direction the {@link Vector3F} instance that represents the direction associated with this {@code DirectionalLight} instance
	 * @param radius a {@code float} that represents the radius of the scene
	 * @throws NullPointerException thrown if, and only if, either {@code transform}, {@code radiance} or {@code direction} are {@code null}
	 */
	public DirectionalLight(final Transform transform, final Color3F radiance, final Vector3F direction, final float radius) {
		super(Objects.requireNonNull(transform, "transform == null"), 1, true);
		
		this.radiance = Objects.requireNonNull(radiance, "radiance == null");
		this.direction = Vector3F.normalize(Objects.requireNonNull(direction, "direction == null"));
		this.radius = radius;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link Color3F} instance that represents the radiance associated with this {@code DirectionalLight} instance.
	 * 
	 * @return the {@code Color3F} instance that represents the radiance associated with this {@code DirectionalLight} instance
	 */
	public Color3F getRadiance() {
		return this.radiance;
	}
	
	/**
	 * Returns a {@link Color3F} instance with the power of this {@code DirectionalLight} instance.
	 * 
	 * @return a {@code Color3F} instance with the power of this {@code DirectionalLight} instance
	 */
	@Override
	public Color3F power() {
		return Color3F.multiply(this.radiance, PI * this.radius * this.radius);
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
		
		final Color3F result = this.radiance;
		
		final Vector3F directionWorldSpace = Vector3F.transform(getTransform().getObjectToWorld(), this.direction);
		
		final Point3F surfaceIntersectionPoint = intersection.getSurfaceIntersectionPoint();
		final Point3F point = Point3F.add(surfaceIntersectionPoint, directionWorldSpace, 2.0F * this.radius);
		
		final Vector3F incoming = directionWorldSpace;
		
		final float probabilityDensityFunctionValue = 1.0F;
		
		return Optional.of(new LightSample(result, point, incoming, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code DirectionalLight} instance.
	 * 
	 * @return a {@code String} with the name of this {@code DirectionalLight} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code DirectionalLight} instance.
	 * 
	 * @return a {@code String} representation of this {@code DirectionalLight} instance
	 */
	@Override
	public String toString() {
		return String.format("new DirectionalLight(%s, %s, %s, %+.10f)", getTransform(), this.radiance, this.direction, Float.valueOf(this.radius));
	}
	
	/**
	 * Returns the {@link Vector3F} instance that represents the direction associated with this {@code DirectionalLight} instance.
	 * 
	 * @return the {@code Vector3F} instance that represents the direction associated with this {@code DirectionalLight} instance
	 */
	public Vector3F getDirection() {
		return this.direction;
	}
	
	/**
	 * Compares {@code object} to this {@code DirectionalLight} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code DirectionalLight}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code DirectionalLight} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code DirectionalLight}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof DirectionalLight)) {
			return false;
		} else if(!Objects.equals(getTransform(), DirectionalLight.class.cast(object).getTransform())) {
			return false;
		} else if(!Objects.equals(this.radiance, DirectionalLight.class.cast(object).radiance)) {
			return false;
		} else if(!Objects.equals(this.direction, DirectionalLight.class.cast(object).direction)) {
			return false;
		} else if(!equal(this.radius, DirectionalLight.class.cast(object).radius)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the radius of the scene.
	 * 
	 * @return the radius of the scene
	 */
	public float getRadius() {
		return this.radius;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code DirectionalLight} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code DirectionalLight} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code DirectionalLight} instance.
	 * 
	 * @return a hash code for this {@code DirectionalLight} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getTransform(), this.radiance, this.direction, Float.valueOf(this.radius));
	}
}