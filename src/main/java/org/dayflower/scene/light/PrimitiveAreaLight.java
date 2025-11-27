/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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

import static org.dayflower.utility.Floats.MAX_VALUE;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.SurfaceSample3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.AreaLight;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.LightSample;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Transform;

/**
 * A {@code PrimitiveAreaLight} is an implementation of {@link AreaLight} that contains a {@link Primitive} instance.
 * <p>
 * This class is mutable and not thread-safe.
 * <p>
 * This {@code Light} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PrimitiveAreaLight extends AreaLight {
	/**
	 * The name of this {@code PrimitiveAreaLight} class.
	 */
	public static final String NAME = "Primitive Area Light";
	
	/**
	 * The ID of this {@code PrimitiveAreaLight} class.
	 */
	public static final int ID = 6;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Primitive primitive;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PrimitiveAreaLight} instance.
	 * <p>
	 * If {@code primitive} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive the {@link Primitive} instance associated with this {@code PrimitiveAreaLight} instance
	 * @throws NullPointerException thrown if, and only if, {@code primitive} is {@code null}
	 */
	public PrimitiveAreaLight(final Primitive primitive) {
		super(primitive.getTransform(), 1);
		
		this.primitive = primitive;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the radiance on {@code intersection} emitted in the direction of {@code direction}.
	 * <p>
	 * If either {@code intersection} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance from which the radiance is emitted
	 * @param direction a {@link Vector3F} instance with a direction in which the radiance is emitted
	 * @return a {@code Color3F} instance with the radiance on {@code intersection} emitted in the direction of {@code direction}
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code direction} are {@code null}
	 */
	@Override
	public Color3F evaluateRadianceEmitted(final Intersection intersection, final Vector3F direction) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(direction, "direction == null");
		
		return Vector3F.dotProduct(intersection.getSurfaceNormalS(), direction) > 0.0F ? this.primitive.getMaterial().emittance(intersection) : Color3F.BLACK;
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
		
		final Transform transform = getTransform();
		
		final Matrix44F objectToWorld = transform.getObjectToWorld();
		final Matrix44F worldToObject = transform.getWorldToObject();
		
		final SurfaceIntersection3F surfaceIntersectionWorldSpace = intersection.getSurfaceIntersectionWorldSpace();
		final SurfaceIntersection3F surfaceIntersectionObjectSpace = SurfaceIntersection3F.transform(surfaceIntersectionWorldSpace, worldToObject, objectToWorld);
		
		final Optional<SurfaceSample3F> optionalSurfaceSampleObjectSpace = this.primitive.getShape().sample(sample, surfaceIntersectionObjectSpace);
		
		if(optionalSurfaceSampleObjectSpace.isPresent()) {
			final SurfaceSample3F surfaceSampleObjectSpace = optionalSurfaceSampleObjectSpace.get();
			final SurfaceSample3F surfaceSampleWorldSpace = SurfaceSample3F.transform(surfaceSampleObjectSpace, objectToWorld, worldToObject);
			
			final float probabilityDensityFunctionValue = surfaceSampleWorldSpace.getProbabilityDensityFunctionValue();
			
			final Point3F pointWorldSpace = surfaceSampleWorldSpace.getPoint();
			
			final Vector3F incomingWorldSpace = Vector3F.directionNormalized(surfaceIntersectionWorldSpace.getSurfaceIntersectionPoint(), pointWorldSpace);
			
			if(probabilityDensityFunctionValue > 0.0F && Vector3F.dotProduct(surfaceSampleWorldSpace.getSurfaceNormal(), Vector3F.negate(incomingWorldSpace)) > 0.0F) {
				final Ray3F ray = intersection.createRay(incomingWorldSpace);
				
				final Optional<Intersection> optionalIntersection = this.primitive.intersection(ray, 0.001F, MAX_VALUE);
				
				if(optionalIntersection.isPresent()) {
					final Color3F radianceEmitted = this.primitive.getMaterial().emittance(optionalIntersection.get());
					
					if(!radianceEmitted.isBlack()) {
						return Optional.of(new LightSample(radianceEmitted, pointWorldSpace, incomingWorldSpace, probabilityDensityFunctionValue));
					}
				}
			}
		}
		
		return Optional.empty();
	}
	
	/**
	 * Returns the {@link Primitive} instance associated with this {@code PrimitiveAreaLight} instance.
	 * 
	 * @return the {@code Primitive} instance associated with this {@code PrimitiveAreaLight} instance
	 */
	public Primitive getPrimitive() {
		return this.primitive;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code PrimitiveAreaLight} instance.
	 * 
	 * @return a {@code String} with the name of this {@code PrimitiveAreaLight} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code PrimitiveAreaLight} instance.
	 * 
	 * @return a {@code String} representation of this {@code PrimitiveAreaLight} instance
	 */
	@Override
	public String toString() {
		return String.format("new PrimitiveAreaLight(%s)", this.primitive);
	}
	
	/**
	 * Compares {@code object} to this {@code PrimitiveAreaLight} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code PrimitiveAreaLight}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code PrimitiveAreaLight} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code PrimitiveAreaLight}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof PrimitiveAreaLight)) {
			return false;
		} else if(!Objects.equals(getTransform(), PrimitiveAreaLight.class.cast(object).getTransform())) {
			return false;
		} else if(!Objects.equals(this.primitive, PrimitiveAreaLight.class.cast(object).primitive)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Evaluates the probability density function (PDF) for the incoming radiance.
	 * <p>
	 * Returns a {@code float} with the probability density function (PDF) value.
	 * <p>
	 * If either {@code intersection} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param incoming the incoming direction
	 * @return a {@code float} with the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code incoming} are {@code null}
	 */
	@Override
	public float evaluateProbabilityDensityFunctionRadianceIncoming(final Intersection intersection, final Vector3F incoming) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		final Transform transform = getTransform();
		
		final Matrix44F objectToWorld = transform.getObjectToWorld();
		final Matrix44F worldToObject = transform.getWorldToObject();
		
		final Vector3F incomingObjectSpace = Vector3F.transform(worldToObject, incoming);
		
		final SurfaceIntersection3F surfaceIntersectionWorldSpace = intersection.getSurfaceIntersectionWorldSpace();
		final SurfaceIntersection3F surfaceIntersectionObjectSpace = SurfaceIntersection3F.transform(surfaceIntersectionWorldSpace, worldToObject, objectToWorld);
		
		return this.primitive.getShape().evaluateProbabilityDensityFunction(surfaceIntersectionObjectSpace, incomingObjectSpace);
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code PrimitiveAreaLight} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code PrimitiveAreaLight} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code PrimitiveAreaLight} instance.
	 * 
	 * @return a hash code for this {@code PrimitiveAreaLight} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getTransform(), this.primitive);
	}
}