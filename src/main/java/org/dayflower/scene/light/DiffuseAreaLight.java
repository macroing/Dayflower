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

import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.equal;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.SurfaceSample3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.AreaLight;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.LightRadianceEmittedResult;
import org.dayflower.scene.LightRadianceIncomingResult;

//TODO: Add Javadocs!
public final class DiffuseAreaLight extends AreaLight {
	private final Color3F radianceEmitted;
	private final Shape3F shape;
	private final boolean isTwoSided;
	private final float surfaceArea;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public DiffuseAreaLight(final Matrix44F lightToWorld, final int samples, final Color3F radianceEmitted, final Shape3F shape, final boolean isTwoSided) {
		super(lightToWorld, samples);
		
		this.radianceEmitted = Objects.requireNonNull(radianceEmitted, "radianceEmitted == null");
		this.shape = Objects.requireNonNull(shape, "shape == null");
		this.isTwoSided = isTwoSided;
		this.surfaceArea = shape.getSurfaceArea();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public Color3F evaluateRadiance(final Intersection intersection, final Vector3F direction) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(direction, "direction == null");
		
		return this.isTwoSided || Vector3F.dotProduct(intersection.getSurfaceIntersectionWorldSpace().getSurfaceNormalS(), direction) > 0.0F ? this.radianceEmitted : Color3F.BLACK;
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
		
		return Color3F.BLACK;
	}
	
	/**
	 * Returns a {@link Color3F} instance with the power of this {@code DiffuseAreaLight} instance.
	 * <p>
	 * This method represents the {@code Light} method {@code Power()} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @return a {@code Color3F} instance with the power of this {@code DiffuseAreaLight} instance
	 */
	@Override
	public Color3F power() {
		return Color3F.multiply(Color3F.multiply(Color3F.multiply(this.radianceEmitted, this.isTwoSided ? 2.0F : 1.0F), this.surfaceArea), PI);
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
		
		/*
		Interaction it(ray.o, n, Vector3f(), Vector3f(n), ray.time, mediumInterface);
		
		*pdfPos = shape->Pdf(it);
		*pdfDir = twoSided ? (.5 * CosineHemispherePdf(AbsDot(n, ray.d))) : CosineHemispherePdf(Dot(n, ray.d));
		*/
		
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
		
		/*
//		Sample a point on the area light's _Shape_, _pShape_
		Interaction pShape = shape->Sample(u1, pdfPos);
		
		pShape.mediumInterface = mediumInterface;
		
		*nLight = pShape.n;
		
//		Sample a cosine-weighted outgoing direction _w_ for area light
		Vector3f w;
		
		if (twoSided) {
			Point2f u = u2;
			
//			Choose a side to sample and then remap u[0] to [0,1] before
//			applying cosine-weighted hemisphere sampling for the chosen side.
			if (u[0] < .5) {
				u[0] = std::min(u[0] * 2, OneMinusEpsilon);
				
				w = CosineSampleHemisphere(u);
			} else {
				u[0] = std::min((u[0] - .5f) * 2, OneMinusEpsilon);
				
				w = CosineSampleHemisphere(u);
				w.z *= -1;
			}
			
			*pdfDir = 0.5f * CosineHemispherePdf(std::abs(w.z));
		} else {
			w = CosineSampleHemisphere(u2);
			
		 	*pdfDir = CosineHemispherePdf(w.z);
		}
		
		Vector3f v1, v2, n(pShape.n);
		
		CoordinateSystem(n, &v1, &v2);
		
		w = w.x * v1 + w.y * v2 + w.z * n;
		
		*ray = pShape.SpawnRay(w);
		
		return L(pShape, w);
		*/
		
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
		
		final Matrix44F lightToWorld = getLightToWorld();
		final Matrix44F worldToLight = getWorldToLight();
		
		final SurfaceIntersection3F surfaceIntersectionWorldSpace = intersection.getSurfaceIntersectionWorldSpace();
		
		final Point3F referencePointWorldSpace = surfaceIntersectionWorldSpace.getSurfaceIntersectionPoint();
		final Point3F referencePointLightSpace = Point3F.transformAndDivide(worldToLight, referencePointWorldSpace);
		
		final Vector3F referenceSurfaceNormalWorldSpace = surfaceIntersectionWorldSpace.getSurfaceNormalS();
		final Vector3F referenceSurfaceNormalLightSpace = Vector3F.transformTranspose(lightToWorld, referenceSurfaceNormalWorldSpace);
		
		final Optional<SurfaceSample3F> optionalSurfaceSampleLightSpace = this.shape.sample(referencePointLightSpace, referenceSurfaceNormalLightSpace, sample.getU(), sample.getV());
		
		if(optionalSurfaceSampleLightSpace.isPresent()) {
			final SurfaceSample3F surfaceSampleLightSpace = optionalSurfaceSampleLightSpace.get();
			final SurfaceSample3F surfaceSampleWorldSpace = SurfaceSample3F.transform(surfaceSampleLightSpace, lightToWorld, worldToLight);
			
			final Point3F pointWorldSpace = surfaceSampleWorldSpace.getPoint();
			
			final Vector3F surfaceNormalWorldSpace = surfaceSampleWorldSpace.getSurfaceNormal();
			
			if(this.isTwoSided || Vector3F.dotProduct(surfaceNormalWorldSpace, Vector3F.directionNormalized(pointWorldSpace, referencePointWorldSpace)) > 0.0F) {
				final Color3F result = this.radianceEmitted;
				
				final Point3F point = pointWorldSpace;
				
				final Vector3F incoming = Vector3F.directionNormalized(referencePointWorldSpace, pointWorldSpace);
				
				final float probabilityDensityFunctionValue = surfaceSampleWorldSpace.getProbabilityDensityFunctionValue();
				
				return Optional.of(new LightRadianceIncomingResult(result, point, incoming, probabilityDensityFunctionValue));
			}
		}
		
		return Optional.empty();
	}
	
	/**
	 * Returns a {@code String} representation of this {@code DiffuseAreaLight} instance.
	 * 
	 * @return a {@code String} representation of this {@code DiffuseAreaLight} instance
	 */
	@Override
	public String toString() {
		return String.format("new DiffuseAreaLight(%s, %d, %s, %s, %s)", getLightToWorld(), Integer.valueOf(getSamples()), this.radianceEmitted, this.shape, Boolean.toString(this.isTwoSided));
	}
	
	/**
	 * Compares {@code object} to this {@code DiffuseAreaLight} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code DiffuseAreaLight}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code DiffuseAreaLight} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code DiffuseAreaLight}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof DiffuseAreaLight)) {
			return false;
		} else if(!Objects.equals(getLightToWorld(), DiffuseAreaLight.class.cast(object).getLightToWorld())) {
			return false;
		} else if(!Objects.equals(getWorldToLight(), DiffuseAreaLight.class.cast(object).getWorldToLight())) {
			return false;
		} else if(getSamples() != DiffuseAreaLight.class.cast(object).getSamples()) {
			return false;
		} else if(!Objects.equals(this.radianceEmitted, DiffuseAreaLight.class.cast(object).radianceEmitted)) {
			return false;
		} else if(!Objects.equals(this.shape, DiffuseAreaLight.class.cast(object).shape)) {
			return false;
		} else if(this.isTwoSided != DiffuseAreaLight.class.cast(object).isTwoSided) {
			return false;
		} else if(!equal(this.surfaceArea, DiffuseAreaLight.class.cast(object).surfaceArea)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@link Light} instance uses a delta distribution, {@code false} otherwise.
	 * <p>
	 * This {@code DiffuseAreaLight} class does not use a delta distribution, so this method will return {@code false}.
	 * 
	 * @return {@code true} if, and only if, this {@code Light} instance uses a delta distribution, {@code false} otherwise
	 */
	@Override
	public boolean isDeltaDistribution() {
		return false;
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
		
		final Matrix44F lightToWorld = getLightToWorld();
		final Matrix44F worldToLight = getWorldToLight();
		
		final SurfaceIntersection3F surfaceIntersectionWorldSpace = intersection.getSurfaceIntersectionWorldSpace();
		
		final Point3F referencePointWorldSpace = surfaceIntersectionWorldSpace.getSurfaceIntersectionPoint();
		final Point3F referencePointLightSpace = Point3F.transformAndDivide(worldToLight, referencePointWorldSpace);
		
		final Vector3F referenceSurfaceNormalWorldSpace = surfaceIntersectionWorldSpace.getSurfaceNormalS();
		final Vector3F referenceSurfaceNormalLightSpace = Vector3F.transformTranspose(lightToWorld, referenceSurfaceNormalWorldSpace);
		
		final Vector3F incomingLightSpace = Vector3F.transform(worldToLight, incoming);
		
		return this.shape.calculateProbabilityDensityFunctionValueForSolidAngle(referencePointLightSpace, referenceSurfaceNormalLightSpace, incomingLightSpace);
	}
	
	/**
	 * Returns a hash code for this {@code DiffuseAreaLight} instance.
	 * 
	 * @return a hash code for this {@code DiffuseAreaLight} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getLightToWorld(), getWorldToLight(), Integer.valueOf(getSamples()), this.radianceEmitted, this.shape, Boolean.valueOf(this.isTwoSided), Float.valueOf(this.surfaceArea));
	}
}