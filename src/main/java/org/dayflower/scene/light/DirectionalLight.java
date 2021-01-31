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

import static org.dayflower.utility.Floats.PI;
import static org.dayflower.utility.Floats.equal;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Quaternion4F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.Light;
import org.dayflower.scene.Transform;

/**
 * A {@code DirectionalLight} is an implementation of {@link Light} that represents a directional light.
 * <p>
 * This class is mutable and not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DirectionalLight extends Light {
	private final Color3F radiance;
	private final float radius;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DirectionalLight} instance.
	 * <p>
	 * If either {@code radiance}, {@code center} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param radiance a {@link Color3F} instance denoting the radiance for this {@code DirectionalLight} instance
	 * @param center a {@link Point3F} instance denoting the center of the scene
	 * @param direction a {@link Vector3F} instance denoting the direction for this {@code DirectionalLight} instance
	 * @param radius a {@code float} denoting the radius of the scene
	 * @throws NullPointerException thrown if, and only if, either {@code radiance}, {@code center} or {@code direction} are {@code null}
	 */
	public DirectionalLight(final Color3F radiance, final Point3F center, final Vector3F direction, final float radius) {
		super(new Transform(Objects.requireNonNull(center, "center == null"), Quaternion4F.from(Matrix44F.rotate(new OrthonormalBasis33F(Objects.requireNonNull(direction, "direction == null"))))), 1, true);
		
		this.radiance = Objects.requireNonNull(radiance, "radiance == null");
		this.radius = radius;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the power of this {@code DirectionalLight} instance.
	 * <p>
	 * This method represents the {@code Light} method {@code Power()} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @return a {@code Color3F} instance with the power of this {@code DirectionalLight} instance
	 */
	@Override
	public Color3F power() {
		return Color3F.multiply(Color3F.multiply(Color3F.multiply(this.radiance, PI), this.radius), this.radius);
	}
	
	/**
	 * Samples the incoming radiance.
	 * <p>
	 * Returns an optional {@link LightRadianceIncomingResult} with the result of the sampling.
	 * <p>
	 * If either {@code intersection} or {@code sample} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code Light} method {@code Sample_Li(const Interaction &ref, const Point2f &u, Vector3f *wi, Float *pdf, VisibilityTester *vis)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param sample a {@link Point2F} instance
	 * @return an optional {@code LightRadianceIncomingResult} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code sample} are {@code null}
	 */
//	@Override
//	public Optional<LightRadianceIncomingResult> sampleRadianceIncoming(final Intersection intersection, final Point2F sample) {
//		Objects.requireNonNull(intersection, "intersection == null");
//		Objects.requireNonNull(sample, "sample == null");
		
//		final Color3F result = this.radiance;
		
//		final Point3F surfaceIntersectionPoint = intersection.getSurfaceIntersectionPoint();
//		final Point3F point = Point3F.add(surfaceIntersectionPoint, this.direction, 2.0F * this.radius);
		
//		final Vector3F incoming = this.direction;
		
//		final float probabilityDensityFunctionValue = 1.0F;
		
//		return Optional.of(new LightRadianceIncomingResult(result, point, incoming, probabilityDensityFunctionValue));
//	}
	
	/**
	 * Returns a {@code String} representation of this {@code DirectionalLight} instance.
	 * 
	 * @return a {@code String} representation of this {@code DirectionalLight} instance
	 */
//	@Override
//	public String toString() {
//		return String.format("new DirectionalLight(%s, %s, %s, %+.10f)", this.radiance, this.center, this.direction, Float.valueOf(this.radius));
//	}
	
	/**
	 * Returns a {@link Vector3F} instance with the direction associated with this {@code DirectionalLight} instance.
	 * 
	 * @return a {@code Vector3F} instance with the direction associated with this {@code DirectionalLight} instance
	 */
//	public Vector3F getDirection() {
//		return this.direction;
//	}
	
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
		} else if(!Objects.equals(this.radiance, DirectionalLight.class.cast(object).radiance)) {
			return false;
//		} else if(!Objects.equals(this.center, DirectionalLight.class.cast(object).center)) {
//			return false;
//		} else if(!Objects.equals(this.direction, DirectionalLight.class.cast(object).direction)) {
//			return false;
		} else if(!equal(this.radius, DirectionalLight.class.cast(object).radius)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code DirectionalLight} instance.
	 * 
	 * @return a hash code for this {@code DirectionalLight} instance
	 */
//	@Override
//	public int hashCode() {
//		return Objects.hash(this.radiance, this.center, this.direction, Float.valueOf(this.radius));
//	}
}