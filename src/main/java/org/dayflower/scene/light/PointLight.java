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
package org.dayflower.scene.light;

import static org.dayflower.util.Floats.PI_MULTIPLIED_BY_4;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.LightIncomingRadianceResult;

/**
 * A {@code PointLight} is an implementation of {@link Light} that represents a point light.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PointLight implements Light {
	private final Color3F intensity;
	private final Point3F position;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PointLight} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PointLight(new Point3F());
	 * }
	 * </pre>
	 */
	public PointLight() {
		this(new Point3F());
	}
	
	/**
	 * Constructs a new {@code PointLight} instance.
	 * <p>
	 * If {@code position} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PointLight(position, Color3F.WHITE);
	 * }
	 * </pre>
	 * 
	 * @param position a {@link Point3F} instance with the position associated with this {@code PointLight} instance
	 * @throws NullPointerException thrown if, and only if, {@code position} is {@code null}
	 */
	public PointLight(final Point3F position) {
		this(position, Color3F.WHITE);
	}
	
	/**
	 * Constructs a new {@code PointLight} instance.
	 * <p>
	 * If either {@code position} or {@code intensity} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param position a {@link Point3F} instance with the position associated with this {@code PointLight} instance
	 * @param intensity a {@link Color3F} instance with the intensity associated with this {@code PointLight} instance
	 * @throws NullPointerException thrown if, and only if, either {@code position} or {@code intensity} are {@code null}
	 */
	public PointLight(final Point3F position, final Color3F intensity) {
		this.position = Objects.requireNonNull(position, "position == null");
		this.intensity = Objects.requireNonNull(intensity, "intensity == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emitted radiance for {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @return a {@code Color3F} instance with the emitted radiance for {@code ray}
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Color3F evaluateEmittedRadiance(final Ray3F ray) {
		Objects.requireNonNull(ray, "ray == null");
		
		return Color3F.BLACK;
	}
	
	/**
	 * Returns a {@link Color3F} instance with the intensity associated with this {@code PointLight} instance.
	 * 
	 * @return a {@code Color3F} instance with the intensity associated with this {@code PointLight} instance
	 */
	public Color3F getIntensity() {
		return this.intensity;
	}
	
//	TODO: Add Javadocs!
	@Override
	public Color3F power() {
		return Color3F.multiply(this.intensity, PI_MULTIPLIED_BY_4);
	}
	
	/**
	 * Returns a {@link Point3F} instance with the position associated with this {@code PointLight} instance.
	 * 
	 * @return a {@code Point3F} instance with the position associated with this {@code PointLight} instance
	 */
	public Point3F getPosition() {
		return this.position;
	}
	
//	TODO: Add Javadocs!
	@Override
	public Optional<LightIncomingRadianceResult> sampleIncomingRadiance(final Intersection intersection, final Point2F sample) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(sample, "sample == null");
		
		final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
		
		final Point3F position = this.position;
		final Point3F surfaceIntersectionPoint = surfaceIntersection.getSurfaceIntersectionPoint();
		
		final Color3F intensity = this.intensity;
		final Color3F result = Color3F.divide(intensity, Point3F.distanceSquared(surfaceIntersectionPoint, position));
		
		final Vector3F incoming = Vector3F.normalize(Vector3F.direction(surfaceIntersectionPoint, position));
		
		final Ray3F shadowRay = surfaceIntersection.createRay(incoming);
		
		final float probabilityDensityFunctionValue = 1.0F;
		
		return Optional.of(new LightIncomingRadianceResult(result, shadowRay, incoming, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code PointLight} instance.
	 * 
	 * @return a {@code String} representation of this {@code PointLight} instance
	 */
	@Override
	public String toString() {
		return String.format("new PointLight(%s, %s)", this.position, this.intensity);
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
		} else if(!Objects.equals(this.intensity, PointLight.class.cast(object).intensity)) {
			return false;
		} else if(!Objects.equals(this.position, PointLight.class.cast(object).position)) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean isDeltaDistribution() {
		return true;
	}
	
//	TODO: Add Javadocs!
	@Override
	public float evaluateProbabilityDensityFunctionIncomingRadiance(final Intersection intersection, final Vector3F incoming) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		return 0.0F;
	}
	
	/**
	 * Returns a hash code for this {@code PointLight} instance.
	 * 
	 * @return a hash code for this {@code PointLight} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.intensity, this.position);
	}
}