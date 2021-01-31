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
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.LightSample;
import org.dayflower.utility.ParameterArguments;

/**
 * An {@code InfiniteAreaLight} is a {@link Light} implementation that represents an infinite area light.
 * <p>
 * This class is mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class InfiniteAreaLight extends Light {
	private final Color3F intensity;
	private final Matrix44F lightToWorld;
	private final Matrix44F worldToLight;
	private final int sampleCount;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code InfiniteAreaLight} instance.
	 * <p>
	 * If either {@code intensity} or {@code lightToWorld} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code lightToWorld} cannot be inverted or {@code sampleCount} is less than {@code 1}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param intensity a {@link Color3F} instance with the intensity associated with this {@code InfiniteAreaLight} instance
	 * @param lightToWorld the {@link Matrix44F} instance that is used to transform from light space to world space and is associated with this {@code InfiniteAreaLight} instance
	 * @param sampleCount the sample count associated with this {@code InfiniteAreaLight} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code lightToWorld} cannot be inverted or {@code sampleCount} is less than {@code 1}
	 * @throws NullPointerException thrown if, and only if, either {@code intensity} or {@code lightToWorld} are {@code null}
	 */
	public InfiniteAreaLight(final Color3F intensity, final Matrix44F lightToWorld, final int sampleCount) {
		super(null, 1, false);
		
		this.intensity = Objects.requireNonNull(intensity, "intensity == null");
		this.lightToWorld = Objects.requireNonNull(lightToWorld, "lightToWorld == null");
		this.worldToLight = Matrix44F.inverse(lightToWorld);
		this.sampleCount = ParameterArguments.requireRange(sampleCount, 1, Integer.MAX_VALUE, "sampleCount");
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
		
//		TODO: Implement!
//		Spectrum InfiniteAreaLight::Le(const RayDifferential &ray) const {
//			Vector3f w = Normalize(WorldToLight(ray.d));
//			
//			Point2f st(SphericalPhi(w) * Inv2Pi, SphericalTheta(w) * InvPi);
//			
//			return Spectrum(Lmap->Lookup(st), SpectrumType::Illuminant);
//		}
		
		return Color3F.BLACK;
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
//		TODO: Implement!
//		Spectrum InfiniteAreaLight::Power() const {
//			return Pi * worldRadius * worldRadius * Spectrum(Lmap->Lookup(Point2f(.5f, .5f), .5f), SpectrumType::Illuminant);
//		}
		
		return Color3F.BLACK;
	}
	
	/**
	 * Samples the incoming radiance.
	 * <p>
	 * Returns an optional {@link LightSample} with the result of the sampling.
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
	public Optional<LightSample> sampleRadianceIncoming(final Intersection intersection, final Point2F sample) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(sample, "sample == null");
		
//		TODO: Implement!
//		Spectrum InfiniteAreaLight::Sample_Li(const Interaction &ref, const Point2f &u, Vector3f *wi, Float *pdf, VisibilityTester *vis) const {
//			Float mapPdf;
//			
//			Point2f uv = distribution->SampleContinuous(u, &mapPdf);
//			
//			if (mapPdf == 0) {
//				return Spectrum(0.f);
//			}
//			
//			Float theta = uv[1] * Pi, phi = uv[0] * 2 * Pi;
//			Float cosTheta = std::cos(theta), sinTheta = std::sin(theta);
//			Float sinPhi = std::sin(phi), cosPhi = std::cos(phi);
//			
//			*wi = LightToWorld(Vector3f(sinTheta * cosPhi, sinTheta * sinPhi, cosTheta));
//			
//			*pdf = mapPdf / (2 * Pi * Pi * sinTheta);
//			
//			if (sinTheta == 0) {
//				*pdf = 0;
//			}
//			
//			*vis = VisibilityTester(ref, Interaction(ref.p + *wi * (2 * worldRadius), ref.time, mediumInterface));
//			
//			return Spectrum(Lmap->Lookup(uv), SpectrumType::Illuminant);
//		}
		
		return Optional.empty();
	}
	
	/**
	 * Returns a {@code String} representation of this {@code InfiniteAreaLight} instance.
	 * 
	 * @return a {@code String} representation of this {@code InfiniteAreaLight} instance
	 */
	@Override
	public String toString() {
		return String.format("new InfiniteAreaLight(%s, %s, %d)", this.intensity, this.lightToWorld, Integer.valueOf(this.sampleCount));
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
		if(object == this) {
			return true;
		} else if(!(object instanceof InfiniteAreaLight)) {
			return false;
		} else if(!Objects.equals(this.intensity, InfiniteAreaLight.class.cast(object).intensity)) {
			return false;
		} else if(!Objects.equals(this.lightToWorld, InfiniteAreaLight.class.cast(object).lightToWorld)) {
			return false;
		} else if(!Objects.equals(this.worldToLight, InfiniteAreaLight.class.cast(object).worldToLight)) {
			return false;
		} else if(this.sampleCount != InfiniteAreaLight.class.cast(object).sampleCount) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@link Light} instance uses a delta distribution, {@code false} otherwise.
	 * <p>
	 * This {@code InfiniteAreaLight} class does not use a delta distribution, so this method will return {@code false}.
	 * 
	 * @return {@code true} if, and only if, this {@code Light} instance uses a delta distribution, {@code false} otherwise
	 */
//	@Override
//	public boolean isDeltaDistribution() {
//		return false;
//	}
	
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
		
//		TODO: Implement!
//		Float InfiniteAreaLight::Pdf_Li(const Interaction &, const Vector3f &w) const {
//			Vector3f wi = WorldToLight(w);
//			
//			Float theta = SphericalTheta(wi), phi = SphericalPhi(wi);
//			Float sinTheta = std::sin(theta);
//			
//			if (sinTheta == 0) {
//				return 0;
//			}
//			
//			return distribution->Pdf(Point2f(phi * Inv2Pi, theta * InvPi)) / (2 * Pi * Pi * sinTheta);
//		}
		
		return 0.0F;
	}
	
	/**
	 * Returns the sample count associated with this {@code InfiniteAreaLight} instance.
	 * 
	 * @return the sample count associated with this {@code InfiniteAreaLight} instance
	 */
//	@Override
//	public int getSampleCount() {
//		return this.sampleCount;
//	}
	
	/**
	 * Returns a hash code for this {@code InfiniteAreaLight} instance.
	 * 
	 * @return a hash code for this {@code InfiniteAreaLight} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.intensity, this.lightToWorld, this.worldToLight, Integer.valueOf(this.sampleCount));
	}
}