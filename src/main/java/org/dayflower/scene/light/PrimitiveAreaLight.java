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

import java.lang.reflect.Field;
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
import org.dayflower.scene.LightRadianceEmittedResult;
import org.dayflower.scene.LightRadianceIncomingResult;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Transform;

/**
 * A {@code PrimitiveAreaLight} is an implementation of {@link AreaLight} that contains a {@link Primitive} instance.
 * <p>
 * This class is indirectly mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PrimitiveAreaLight extends AreaLight {
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
	 * Returns a {@link Color3F} instance with the radiance for {@code intersection} and {@code direction}.
	 * <p>
	 * If either {@code intersection} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code AreaLight} method {@code L(const Interaction &intr, const Vector3f &w)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param direction a {@link Vector3F} instance with a direction
	 * @return a {@code Color3F} instance with the radiance for {@code intersection} and {@code direction}
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code direction} are {@code null}
	 */
	@Override
	public Color3F evaluateRadiance(final Intersection intersection, final Vector3F direction) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(direction, "direction == null");
		
		return Vector3F.dotProduct(intersection.getSurfaceNormalS(), direction) > 0.0F ? this.primitive.getMaterial().emittance(intersection) : Color3F.BLACK;
	}
	
	/**
	 * Returns a {@link Color3F} instance with the emitted radiance for {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code Light} method {@code Le(const RayDifferential &r)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @return a {@code Color3F} instance with the emitted radiance for {@code ray}
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Color3F evaluateRadianceEmitted(final Ray3F ray) {
		Objects.requireNonNull(ray, "ray == null");
		
		return Color3F.BLACK;//TODO: Implement!
	}
	
	/**
	 * Returns a {@link Color3F} instance with the power of this {@code PrimitiveAreaLight} instance.
	 * <p>
	 * This method represents the {@code Light} method {@code Power()} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @return a {@code Color3F} instance with the power of this {@code PrimitiveAreaLight} instance
	 */
	@Override
	public Color3F power() {
		return Color3F.BLACK;//TODO: Implement!
	}
	
	/**
	 * Evaluates the probability density functions (PDFs) for emitted radiance.
	 * <p>
	 * Returns an optional {@link LightRadianceEmittedResult} with the result of the evaluation.
	 * <p>
	 * If either {@code ray} or {@code normal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code Light} method {@code Pdf_Le(const Ray &ray, const Normal3f &nLight, Float *pdfPos, Float *pdfDir)} in PBRT.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @param normal a {@link Vector3F} instance
	 * @return an optional {@code LightRadianceEmittedResult} with the result of the evaluation
	 * @throws NullPointerException thrown if, and only if, either {@code ray} or {@code normal} are {@code null}
	 */
	@Override
	public Optional<LightRadianceEmittedResult> evaluateProbabilityDensityFunctionRadianceEmitted(final Ray3F ray, final Vector3F normal) {
		Objects.requireNonNull(ray, "ray == null");
		Objects.requireNonNull(normal, "normal == null");
		
		return Optional.empty();//TODO: Implement!
	}
	
	/**
	 * Samples the emitted radiance.
	 * <p>
	 * Returns an optional {@link LightRadianceEmittedResult} with the result of the sampling.
	 * <p>
	 * If either {@code sampleA} or {@code sampleB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code Light} method {@code Sample_Le(const Point2f &u1, const Point2f &u2, Float time, Ray *ray, Normal3f *nLight, Float *pdfPos, Float *pdfDir)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param sampleA a {@link Point2F} instance
	 * @param sampleB a {@code Point2F} instance
	 * @return an optional {@code LightRadianceEmittedResult} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code sampleA} or {@code sampleB} are {@code null}
	 */
	@Override
	public Optional<LightRadianceEmittedResult> sampleRadianceEmitted(final Point2F sampleA, final Point2F sampleB) {
		Objects.requireNonNull(sampleA, "sampleA == null");
		Objects.requireNonNull(sampleB, "sampleB == null");
		
		return Optional.empty();//TODO: Implement!
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
	@Override
	public Optional<LightRadianceIncomingResult> sampleRadianceIncoming(final Intersection intersection, final Point2F sample) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(sample, "sample == null");
		
		final Transform transform = getTransform();
		
		final Matrix44F lightToWorld = transform.getObjectToWorld();
		final Matrix44F worldToLight = transform.getWorldToObject();
		
		final SurfaceIntersection3F surfaceIntersectionWorldSpace = intersection.getSurfaceIntersectionWorldSpace();
		final SurfaceIntersection3F surfaceIntersectionLightSpace = SurfaceIntersection3F.transform(surfaceIntersectionWorldSpace, worldToLight, lightToWorld);
		
		final Optional<SurfaceSample3F> optionalSurfaceSampleLightSpace = this.primitive.getShape().sample(sample, surfaceIntersectionLightSpace);
		
		if(optionalSurfaceSampleLightSpace.isPresent()) {
			final SurfaceSample3F surfaceSampleLightSpace = optionalSurfaceSampleLightSpace.get();
			final SurfaceSample3F surfaceSampleWorldSpace = SurfaceSample3F.transform(surfaceSampleLightSpace, lightToWorld, worldToLight);
			
			final float probabilityDensityFunctionValue = surfaceSampleWorldSpace.getProbabilityDensityFunctionValue();
			
			final Point3F pointWorldSpace = surfaceSampleWorldSpace.getPoint();
			
			final Vector3F incomingWorldSpace = Vector3F.directionNormalized(surfaceIntersectionWorldSpace.getSurfaceIntersectionPoint(), pointWorldSpace);
			
			if(probabilityDensityFunctionValue > 0.0F && Vector3F.dotProduct(surfaceSampleWorldSpace.getSurfaceNormal(), Vector3F.negate(incomingWorldSpace)) > 0.0F) {
				final Ray3F ray = new Ray3F(surfaceIntersectionWorldSpace.getSurfaceIntersectionPoint(), incomingWorldSpace);
				
				final Optional<Intersection> optionalIntersection = this.primitive.intersection(ray, 0.001F, Float.MAX_VALUE);
				
				if(optionalIntersection.isPresent()) {
					final Color3F radianceEmitted = this.primitive.getMaterial().emittance(optionalIntersection.get());
					
					if(!radianceEmitted.isBlack()) {
						return Optional.of(new LightRadianceIncomingResult(radianceEmitted, pointWorldSpace, incomingWorldSpace, probabilityDensityFunctionValue));
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
		} else if(!Objects.equals(this.primitive, PrimitiveAreaLight.class.cast(object).primitive)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Evaluates the probability density function (PDF) for incoming radiance.
	 * <p>
	 * Returns a {@code float} with the probability density function (PDF) value.
	 * <p>
	 * If either {@code intersection} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code Light} method {@code Pdf_Li(const Interaction &ref, const Vector3f &wi)} that returns a {@code Float} in PBRT.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param incoming the incoming direction, called {@code wi} in PBRT
	 * @return a {@code float} with the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code incoming} are {@code null}
	 */
	@Override
	public float evaluateProbabilityDensityFunctionRadianceIncoming(final Intersection intersection, final Vector3F incoming) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		final Transform transform = getTransform();
		
		final Matrix44F lightToWorld = transform.getObjectToWorld();
		final Matrix44F worldToLight = transform.getWorldToObject();
		
		final Vector3F incomingLightSpace = Vector3F.transform(worldToLight, incoming);
		
		return this.primitive.getShape().evaluateProbabilityDensityFunction(SurfaceIntersection3F.transform(intersection.getSurfaceIntersectionWorldSpace(), worldToLight, lightToWorld), incomingLightSpace);
	}
	
	/**
	 * Returns a hash code for this {@code PrimitiveAreaLight} instance.
	 * 
	 * @return a hash code for this {@code PrimitiveAreaLight} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.primitive);
	}
}