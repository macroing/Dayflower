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

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.LightRadianceEmittedResult;
import org.dayflower.scene.LightRadianceIncomingResult;

//TODO: Add Javadocs!
public final class InfiniteAreaLight implements Light {
	private final Color3F intensity;
	private final Matrix44F lightToWorld;
	private final Matrix44F worldToLight;
	private final int samples;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public InfiniteAreaLight(final Color3F intensity, final Matrix44F lightToWorld, final int samples) {
		this.intensity = Objects.requireNonNull(intensity, "intensity == null");
		this.lightToWorld = Objects.requireNonNull(lightToWorld, "lightToWorld == null");
		this.worldToLight = Matrix44F.inverse(lightToWorld);
		this.samples = samples;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
		
		/*
Spectrum InfiniteAreaLight::Le(const RayDifferential &ray) const {
	Vector3f w = Normalize(WorldToLight(ray.d));
	
	Point2f st(SphericalPhi(w) * Inv2Pi, SphericalTheta(w) * InvPi);
	
	return Spectrum(Lmap->Lookup(st), SpectrumType::Illuminant);
}
		 */
		
		return Color3F.BLACK;//TODO: Implement!
	}
	
	/**
	 * Returns a {@link Color3F} instance with the power of this {@code InfiniteAreaLight} instance.
	 * <p>
	 * This method represents the {@code Light} method {@code Power()} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @return a {@code Color3F} instance with the power of this {@code InfiniteAreaLight} instance
	 */
	@Override
	public Color3F power() {
		/*
Spectrum InfiniteAreaLight::Power() const {
	return Pi * worldRadius * worldRadius * Spectrum(Lmap->Lookup(Point2f(.5f, .5f), .5f), SpectrumType::Illuminant);
}
		 */
		
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
		
		/*
void InfiniteAreaLight::Pdf_Le(const Ray &ray, const Normal3f &, Float *pdfPos, Float *pdfDir) const {
	ProfilePhase _(Prof::LightPdf);
	
	Vector3f d = -WorldToLight(ray.d);
	
	Float theta = SphericalTheta(d), phi = SphericalPhi(d);
	
	Point2f uv(phi * Inv2Pi, theta * InvPi);
	
	Float mapPdf = distribution->Pdf(uv);
	
	*pdfDir = mapPdf / (2 * Pi * Pi * std::sin(theta));
	*pdfPos = 1 / (Pi * worldRadius * worldRadius);
}
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
Spectrum InfiniteAreaLight::Sample_Le(const Point2f &u1, const Point2f &u2, Float time, Ray *ray, Normal3f *nLight, Float *pdfPos, Float *pdfDir) const {
	ProfilePhase _(Prof::LightSample);
	
//	Compute direction for infinite light sample ray
	Point2f u = u1;
	
//	Find $(u,v)$ sample coordinates in infinite light texture
	Float mapPdf;
	
	Point2f uv = distribution->SampleContinuous(u, &mapPdf);
	
	if (mapPdf == 0) {
		return Spectrum(0.f);
	}
	
	Float theta = uv[1] * Pi, phi = uv[0] * 2.f * Pi;
	Float cosTheta = std::cos(theta), sinTheta = std::sin(theta);
	Float sinPhi = std::sin(phi), cosPhi = std::cos(phi);
	
	Vector3f d = -LightToWorld(Vector3f(sinTheta * cosPhi, sinTheta * sinPhi, cosTheta));
	
	*nLight = (Normal3f)d;
	
//	Compute origin for infinite light sample ray
	Vector3f v1, v2;
	
	CoordinateSystem(-d, &v1, &v2);
	
	Point2f cd = ConcentricSampleDisk(u2);
	
	Point3f pDisk = worldCenter + worldRadius * (cd.x * v1 + cd.y * v2);
	
	*ray = Ray(pDisk + worldRadius * -d, d, Infinity, time);
	
//	Compute _InfiniteAreaLight_ ray PDFs
	*pdfDir = sinTheta == 0 ? 0 : mapPdf / (2 * Pi * Pi * sinTheta);
	*pdfPos = 1 / (Pi * worldRadius * worldRadius);
	
	return Spectrum(Lmap->Lookup(uv), SpectrumType::Illuminant);
}
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
		
		/*
Spectrum InfiniteAreaLight::Sample_Li(const Interaction &ref, const Point2f &u, Vector3f *wi, Float *pdf, VisibilityTester *vis) const {
	ProfilePhase _(Prof::LightSample);
	
//	Find $(u,v)$ sample coordinates in infinite light texture
	Float mapPdf;
	
	Point2f uv = distribution->SampleContinuous(u, &mapPdf);
	
	if (mapPdf == 0) {
		return Spectrum(0.f);
	}
	
//	Convert infinite light sample point to direction
	Float theta = uv[1] * Pi, phi = uv[0] * 2 * Pi;
	Float cosTheta = std::cos(theta), sinTheta = std::sin(theta);
	Float sinPhi = std::sin(phi), cosPhi = std::cos(phi);
	
	*wi = LightToWorld(Vector3f(sinTheta * cosPhi, sinTheta * sinPhi, cosTheta));
	
//	Compute PDF for sampled infinite light direction
	*pdf = mapPdf / (2 * Pi * Pi * sinTheta);
	
	if (sinTheta == 0) {
		*pdf = 0;
	}
	
//	Return radiance value for infinite light direction
	*vis = VisibilityTester(ref, Interaction(ref.p + *wi * (2 * worldRadius), ref.time, mediumInterface));
	
	return Spectrum(Lmap->Lookup(uv), SpectrumType::Illuminant);
}
		 */
		
		return Optional.empty();//TODO: Implement!
	}
	
	/**
	 * Returns a {@code String} representation of this {@code InfiniteAreaLight} instance.
	 * 
	 * @return a {@code String} representation of this {@code InfiniteAreaLight} instance
	 */
	@Override
	public String toString() {
		return "";//TODO: Implement!
	}
	
	/**
	 * Compares {@code object} to this {@code InfiniteAreaLight} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code InfiniteAreaLight}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code InfiniteAreaLight} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code InfiniteAreaLight}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		return false;//TODO: Implement!
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@link Light} instance uses a delta distribution, {@code false} otherwise.
	 * <p>
	 * This {@code InfiniteAreaLight} class does not use a delta distribution, so this method will return {@code false}.
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
		
		/*
Float InfiniteAreaLight::Pdf_Li(const Interaction &, const Vector3f &w) const {
	ProfilePhase _(Prof::LightPdf);
	
	Vector3f wi = WorldToLight(w);
	
	Float theta = SphericalTheta(wi), phi = SphericalPhi(wi);
	Float sinTheta = std::sin(theta);
	
	if (sinTheta == 0) {
		return 0;
	}
	
	return distribution->Pdf(Point2f(phi * Inv2Pi, theta * InvPi)) / (2 * Pi * Pi * sinTheta);
}
		 */
		
		return 0.0F;//TODO: Implement!
	}
	
	/**
	 * Returns a hash code for this {@code InfiniteAreaLight} instance.
	 * 
	 * @return a hash code for this {@code InfiniteAreaLight} instance
	 */
	@Override
	public int hashCode() {
		return 0;//TODO: Implement!
	}
}