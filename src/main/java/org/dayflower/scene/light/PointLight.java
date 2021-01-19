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

import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_4;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.LightRadianceEmittedResult;
import org.dayflower.scene.LightRadianceIncomingResult;

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
	 * Returns a {@link Color3F} instance with the intensity associated with this {@code PointLight} instance.
	 * 
	 * @return a {@code Color3F} instance with the intensity associated with this {@code PointLight} instance
	 */
	public Color3F getIntensity() {
		return this.intensity;
	}
	
	/**
	 * Returns a {@link Color3F} instance with the power of this {@code PointLight} instance.
	 * <p>
	 * This method represents the {@code Light} method {@code Power()} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @return a {@code Color3F} instance with the power of this {@code PointLight} instance
	 */
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
		
		final Color3F result = this.intensity;
		
		final float probabilityDensityFunctionValueDirection = SampleGeneratorF.sphereUniformDistributionProbabilityDensityFunction();
		final float probabilityDensityFunctionValuePosition = 1.0F;
		
		return Optional.of(new LightRadianceEmittedResult(result, ray, normal, probabilityDensityFunctionValueDirection, probabilityDensityFunctionValuePosition));
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
		
		final Color3F result = this.intensity;
		
		final Ray3F ray = new Ray3F(this.position, SampleGeneratorF.sampleSphereUniformDistribution(sampleA.getU(), sampleA.getV()));
		
		final Vector3F normal = ray.getDirection();
		
		final float probabilityDensityFunctionValueDirection = SampleGeneratorF.sphereUniformDistributionProbabilityDensityFunction();
		final float probabilityDensityFunctionValuePosition = 1.0F;
		
		return Optional.of(new LightRadianceEmittedResult(result, ray, normal, probabilityDensityFunctionValueDirection, probabilityDensityFunctionValuePosition));
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
		
		final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
		
		final Point3F position = this.position;
		final Point3F surfaceIntersectionPoint = surfaceIntersection.getSurfaceIntersectionPoint();
		
		final Color3F intensity = this.intensity;
		final Color3F result = Color3F.divide(intensity, Point3F.distanceSquared(surfaceIntersectionPoint, position));
		
		final Vector3F incoming = Vector3F.normalize(Vector3F.direction(surfaceIntersectionPoint, position));
		
		final float probabilityDensityFunctionValue = 1.0F;
		
		return Optional.of(new LightRadianceIncomingResult(result, position, incoming, probabilityDensityFunctionValue));
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
	
	/**
	 * Returns {@code true} if, and only if, this {@link Light} instance uses a delta distribution, {@code false} otherwise.
	 * <p>
	 * This {@code PointLight} class uses a delta distribution, so this method will return {@code true}.
	 * 
	 * @return {@code true} if, and only if, this {@code Light} instance uses a delta distribution, {@code false} otherwise
	 */
	@Override
	public boolean isDeltaDistribution() {
		return true;
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
		
		return 0.0F;
	}
	
	/**
	 * Returns the sample count associated with this {@code PointLight} instance.
	 * 
	 * @return the sample count associated with this {@code PointLight} instance
	 */
	@Override
	public int getSampleCount() {
		return 1;
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