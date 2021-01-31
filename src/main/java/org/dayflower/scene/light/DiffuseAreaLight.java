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

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.AreaLight;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.LightSample;

/**
 * A {@code DiffuseAreaLight} is an implementation of {@link AreaLight} that represents a diffuse area light.
 * <p>
 * This class can be considered immutable and thread-safe if, and only if, its associated {@link Shape3F} instance is.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DiffuseAreaLight extends AreaLight {
	private final Color3F radianceEmitted;
	private final Shape3F shape;
	private final boolean isTwoSided;
	private final float surfaceArea;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DiffuseAreaLight} instance.
	 * <p>
	 * If either {@code lightToWorld}, {@code radianceEmitted} or {@code shape} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code sampleCount} is less than {@code 1}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param lightToWorld the {@link Matrix44F} instance that is used to transform from light space to world space and is associated with this {@code DiffuseAreaLight} instance
	 * @param sampleCount the sample count associated with this {@code AreaLight} instance
	 * @param radianceEmitted a {@link Color3F} instance with the radiance emitted
	 * @param shape a {@link Shape3F} instance
	 * @param isTwoSided {@code true} if, and only if, this {@code DiffuseAreaLight} is two-sided, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code sampleCount} is less than {@code 1}
	 * @throws NullPointerException thrown if, and only if, either {@code lightToWorld}, {@code radianceEmitted} or {@code shape} are {@code null}
	 */
	public DiffuseAreaLight(final Matrix44F lightToWorld, final int sampleCount, final Color3F radianceEmitted, final Shape3F shape, final boolean isTwoSided) {
//		super(lightToWorld, sampleCount);
		super(null, sampleCount);
		
		this.radianceEmitted = Objects.requireNonNull(radianceEmitted, "radianceEmitted == null");
		this.shape = Objects.requireNonNull(shape, "shape == null");
		this.isTwoSided = isTwoSided;
		this.surfaceArea = shape.getSurfaceArea();
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
//	@Override
	public Color3F evaluateRadiance(final Intersection intersection, final Vector3F direction) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(direction, "direction == null");
		
		return this.isTwoSided || Vector3F.dotProduct(intersection.getSurfaceNormalS(), direction) > 0.0F ? this.radianceEmitted : Color3F.BLACK;
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
//	@Override
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
//	@Override
	public Color3F power() {
		return Color3F.multiply(this.radianceEmitted, (this.isTwoSided ? 2.0F : 1.0F) * this.surfaceArea * PI);
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
//	@Override
	public Optional<LightSample> sampleRadianceIncoming(final Intersection intersection, final Point2F sample) {
//		Spectrum DiffuseAreaLight::Sample_Li(const Interaction &ref, const Point2f &u, Vector3f *wi, Float *pdf, VisibilityTester *vis) const {
//			Interaction pShape = shape->Sample(ref, u, pdf);
//			
//			pShape.mediumInterface = mediumInterface;
//			
//			if (*pdf == 0 || (pShape.p - ref.p).LengthSquared() == 0) {
//				*pdf = 0;
//				
//				return 0.f;
//			}
//			
//			*wi = Normalize(pShape.p - ref.p);
//			
//			*vis = VisibilityTester(ref, pShape);
//			
//			return L(pShape, -*wi);
//		}
//		
//		Spectrum L(const Interaction &intr, const Vector3f &w) const {
//			return (twoSided || Dot(intr.n, w) > 0) ? Lemit : Spectrum(0.f);
//		}
		
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(sample, "sample == null");
		
//		final Matrix44F lightToWorld = getLightToWorld();
//		final Matrix44F worldToLight = getWorldToLight();
		
//		final SurfaceIntersection3F surfaceIntersectionWorldSpace = intersection.getSurfaceIntersectionWorldSpace();
//		final SurfaceIntersection3F surfaceIntersectionLightSpace = SurfaceIntersection3F.transform(surfaceIntersectionWorldSpace, worldToLight, lightToWorld);
		
//		final Optional<SurfaceSample3F> optionalSurfaceSampleLightSpace = this.shape.sample(sample, surfaceIntersectionLightSpace);
		
//		if(optionalSurfaceSampleLightSpace.isPresent()) {
//			final SurfaceSample3F surfaceSampleLightSpace = optionalSurfaceSampleLightSpace.get();
//			final SurfaceSample3F surfaceSampleWorldSpace = SurfaceSample3F.transform(surfaceSampleLightSpace, lightToWorld, worldToLight);
			
//			final float probabilityDensityFunctionValue = surfaceSampleWorldSpace.getProbabilityDensityFunctionValue();
			
//			final Point3F pointWorldSpace = surfaceSampleWorldSpace.getPoint();
			
//			final Vector3F incomingWorldSpace = Vector3F.directionNormalized(surfaceIntersectionWorldSpace.getSurfaceIntersectionPoint(), pointWorldSpace);
			
//			if(probabilityDensityFunctionValue > 0.0F && (this.isTwoSided || Vector3F.dotProduct(surfaceSampleWorldSpace.getSurfaceNormal(), Vector3F.negate(incomingWorldSpace)) > 0.0F)) {
//				return Optional.of(new LightRadianceIncomingResult(this.radianceEmitted, pointWorldSpace, incomingWorldSpace, probabilityDensityFunctionValue));
//			}
//		}
		
		return Optional.empty();
	}
	
	/**
	 * Returns a {@code String} representation of this {@code DiffuseAreaLight} instance.
	 * 
	 * @return a {@code String} representation of this {@code DiffuseAreaLight} instance
	 */
//	@Override
//	public String toString() {
//		return String.format("new DiffuseAreaLight(%s, %d, %s, %s, %s)", getLightToWorld(), Integer.valueOf(getSampleCount()), this.radianceEmitted, this.shape, Boolean.toString(this.isTwoSided));
//	}
	
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
//		} else if(!Objects.equals(getLightToWorld(), DiffuseAreaLight.class.cast(object).getLightToWorld())) {
//			return false;
//		} else if(!Objects.equals(getWorldToLight(), DiffuseAreaLight.class.cast(object).getWorldToLight())) {
//			return false;
//		} else if(getSampleCount() != DiffuseAreaLight.class.cast(object).getSampleCount()) {
//			return false;
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
//	@Override
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
//	@Override
//	public float evaluateProbabilityDensityFunctionRadianceIncoming(final Intersection intersection, final Vector3F incoming) {
//		Objects.requireNonNull(intersection, "intersection == null");
//		Objects.requireNonNull(incoming, "incoming == null");
//		
//		final Matrix44F lightToWorld = getLightToWorld();
//		final Matrix44F worldToLight = getWorldToLight();
//		
//		final Vector3F incomingLightSpace = Vector3F.transform(worldToLight, incoming);
//		
//		return this.shape.evaluateProbabilityDensityFunction(SurfaceIntersection3F.transform(intersection.getSurfaceIntersectionWorldSpace(), worldToLight, lightToWorld), incomingLightSpace);
//	}
	
	/**
	 * Returns a hash code for this {@code DiffuseAreaLight} instance.
	 * 
	 * @return a hash code for this {@code DiffuseAreaLight} instance
	 */
//	@Override
//	public int hashCode() {
//		return Objects.hash(getLightToWorld(), getWorldToLight(), Integer.valueOf(getSampleCount()), this.radianceEmitted, this.shape, Boolean.valueOf(this.isTwoSided), Float.valueOf(this.surfaceArea));
//	}
}