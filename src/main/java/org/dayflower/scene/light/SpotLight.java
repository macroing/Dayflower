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

import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Floats.cos;
import static org.dayflower.utility.Floats.equal;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.LightSample;
import org.dayflower.scene.Transform;

/**
 * A {@code SpotLight} is an implementation of {@link Light} that represents a spotlight.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SpotLight extends Light {
	private final AngleF coneAngle;
	private final AngleF coneAngleDelta;
	private final Color3F intensity;
	private final float cosConeAngle;
	private final float cosConeAngleMinusConeAngleDelta;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SpotLight} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SpotLight(AngleF.degrees(30.0F));
	 * }
	 * </pre>
	 */
	public SpotLight() {
		this(AngleF.degrees(30.0F));
	}
	
	/**
	 * Constructs a new {@code SpotLight} instance.
	 * <p>
	 * If {@code coneAngle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SpotLight(coneAngle, AngleF.degrees(5.0F));
	 * }
	 * </pre>
	 * 
	 * @param coneAngle an {@link AngleF} instance with the cone angle
	 * @throws NullPointerException thrown if, and only if, {@code coneAngle} is {@code null}
	 */
	public SpotLight(final AngleF coneAngle) {
		this(coneAngle, AngleF.degrees(5.0F));
	}
	
	/**
	 * Constructs a new {@code SpotLight} instance.
	 * <p>
	 * If either {@code coneAngle} or {@code coneAngleDelta} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SpotLight(coneAngle, coneAngleDelta, Color3F.WHITE);
	 * }
	 * </pre>
	 * 
	 * @param coneAngle an {@link AngleF} instance with the cone angle
	 * @param coneAngleDelta an {@code AngleF} instance with the cone angle delta
	 * @throws NullPointerException thrown if, and only if, either {@code coneAngle} or {@code coneAngleDelta} are {@code null}
	 */
	public SpotLight(final AngleF coneAngle, final AngleF coneAngleDelta) {
		this(coneAngle, coneAngleDelta, Color3F.WHITE);
	}
	
	/**
	 * Constructs a new {@code SpotLight} instance.
	 * <p>
	 * If either {@code coneAngle}, {@code coneAngleDelta} or {@code intensity} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SpotLight(coneAngle, coneAngleDelta, intensity, new Point3F());
	 * }
	 * </pre>
	 * 
	 * @param coneAngle an {@link AngleF} instance with the cone angle
	 * @param coneAngleDelta an {@code AngleF} instance with the cone angle delta
	 * @param intensity a {@link Color3F} instance with the intensity
	 * @throws NullPointerException thrown if, and only if, either {@code coneAngle}, {@code coneAngleDelta} or {@code intensity} are {@code null}
	 */
	public SpotLight(final AngleF coneAngle, final AngleF coneAngleDelta, final Color3F intensity) {
		this(coneAngle, coneAngleDelta, intensity, new Point3F());
	}
	
	/**
	 * Constructs a new {@code SpotLight} instance.
	 * <p>
	 * If either {@code coneAngle}, {@code coneAngleDelta}, {@code intensity} or {@code position} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SpotLight(coneAngle, coneAngleDelta, intensity, position, Vector3F.z());
	 * }
	 * </pre>
	 * 
	 * @param coneAngle an {@link AngleF} instance with the cone angle
	 * @param coneAngleDelta an {@code AngleF} instance with the cone angle delta
	 * @param intensity a {@link Color3F} instance with the intensity
	 * @param position a {@link Point3F} instance with the position
	 * @throws NullPointerException thrown if, and only if, either {@code coneAngle}, {@code coneAngleDelta}, {@code intensity} or {@code position} are {@code null}
	 */
	public SpotLight(final AngleF coneAngle, final AngleF coneAngleDelta, final Color3F intensity, final Point3F position) {
		this(coneAngle, coneAngleDelta, intensity, position, Vector3F.z());
	}
	
	/**
	 * Constructs a new {@code SpotLight} instance.
	 * <p>
	 * If either {@code coneAngle}, {@code coneAngleDelta}, {@code intensity}, {@code position} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SpotLight(coneAngle, coneAngleDelta, intensity, new Transform(position, direction));
	 * }
	 * </pre>
	 * 
	 * @param coneAngle an {@link AngleF} instance with the cone angle
	 * @param coneAngleDelta an {@code AngleF} instance with the cone angle delta
	 * @param intensity a {@link Color3F} instance with the intensity
	 * @param position a {@link Point3F} instance with the position
	 * @param direction a {@link Vector3F} instance with the direction
	 * @throws NullPointerException thrown if, and only if, either {@code coneAngle}, {@code coneAngleDelta}, {@code intensity}, {@code position} or {@code direction} are {@code null}
	 */
	public SpotLight(final AngleF coneAngle, final AngleF coneAngleDelta, final Color3F intensity, final Point3F position, final Vector3F direction) {
		this(coneAngle, coneAngleDelta, intensity, new Transform(position, direction));
	}
	
	/**
	 * Constructs a new {@code SpotLight} instance.
	 * <p>
	 * If either {@code coneAngle}, {@code coneAngleDelta}, {@code intensity} or {@code transform} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param coneAngle an {@link AngleF} instance with the cone angle
	 * @param coneAngleDelta an {@code AngleF} instance with the cone angle delta
	 * @param intensity a {@link Color3F} instance with the intensity
	 * @param transform a {@link Transform} instance
	 * @throws NullPointerException thrown if, and only if, either {@code coneAngle}, {@code coneAngleDelta}, {@code intensity} or {@code transform} are {@code null}
	 */
	public SpotLight(final AngleF coneAngle, final AngleF coneAngleDelta, final Color3F intensity, final Transform transform) {
		super(Objects.requireNonNull(transform, "transform == null"), 1, true);
		
		this.coneAngle = Objects.requireNonNull(coneAngle, "coneAngle == null");
		this.coneAngleDelta = Objects.requireNonNull(coneAngleDelta, "coneAngleDelta == null");
		this.intensity = Objects.requireNonNull(intensity, "intensity == null");
		this.cosConeAngle = cos(coneAngle.getRadians());
		this.cosConeAngleMinusConeAngleDelta = cos(AngleF.subtract(coneAngle, coneAngleDelta).getRadians());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the power of this {@code SpotLight} instance.
	 * 
	 * @return a {@code Color3F} instance with the power of this {@code SpotLight} instance
	 */
	@Override
	public Color3F power() {
		return Color3F.multiply(this.intensity, PI_MULTIPLIED_BY_2 * (1.0F - 0.5F * (this.cosConeAngleMinusConeAngleDelta + this.cosConeAngle)));
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
		
		final Point3F position = Point3F.transformAndDivide(getTransform().getObjectToWorld(), new Point3F());
		final Point3F surfaceIntersectionPoint = intersection.getSurfaceIntersectionPoint();
		
		final Vector3F incoming = Vector3F.directionNormalized(surfaceIntersectionPoint, position);
		
		final Color3F intensity = this.intensity;
		final Color3F result = Color3F.divide(Color3F.multiply(intensity, doComputeFalloff(Vector3F.negate(incoming))), Point3F.distanceSquared(surfaceIntersectionPoint, position));
		
		final float probabilityDensityFunctionValue = 1.0F;
		
		return Optional.of(new LightSample(result, position, incoming, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SpotLight} instance.
	 * 
	 * @return a {@code String} representation of this {@code SpotLight} instance
	 */
	@Override
	public String toString() {
		return String.format("new SpotLight(%s, %s, %s, %s)", this.coneAngle, this.coneAngleDelta, this.intensity, getTransform());
	}
	
	/**
	 * Compares {@code object} to this {@code SpotLight} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SpotLight}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SpotLight} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SpotLight}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SpotLight)) {
			return false;
		} else if(!Objects.equals(getTransform(), SpotLight.class.cast(object).getTransform())) {
			return false;
		} else if(!Objects.equals(this.coneAngle, SpotLight.class.cast(object).coneAngle)) {
			return false;
		} else if(!Objects.equals(this.coneAngleDelta, SpotLight.class.cast(object).coneAngleDelta)) {
			return false;
		} else if(!Objects.equals(this.intensity, SpotLight.class.cast(object).intensity)) {
			return false;
		} else if(!equal(this.cosConeAngle, SpotLight.class.cast(object).cosConeAngle)) {
			return false;
		} else if(!equal(this.cosConeAngleMinusConeAngleDelta, SpotLight.class.cast(object).cosConeAngleMinusConeAngleDelta)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code SpotLight} instance.
	 * 
	 * @return a hash code for this {@code SpotLight} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getTransform(), this.coneAngle, this.coneAngleDelta, this.intensity, Float.valueOf(this.cosConeAngle), Float.valueOf(this.cosConeAngleMinusConeAngleDelta));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private float doComputeFalloff(final Vector3F direction) {
		final Vector3F directionObjectSpace = Vector3F.normalize(Vector3F.transform(getTransform().getWorldToObject(), direction));
		
		final float cosTheta = directionObjectSpace.cosTheta();
		
		if(cosTheta < this.cosConeAngle) {
			return 0.0F;
		}
		
		if(cosTheta >= this.cosConeAngleMinusConeAngleDelta) {
			return 1.0F;
		}
		
		final float delta = (cosTheta - this.cosConeAngle) / (this.cosConeAngleMinusConeAngleDelta - this.cosConeAngle);
		
		return (delta * delta) * (delta * delta);
	}
}