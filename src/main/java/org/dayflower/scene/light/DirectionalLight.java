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

import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.equal;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.LightRadianceEmittedResult;
import org.dayflower.scene.LightRadianceIncomingResult;

/**
 * A {@code DirectionalLight} is an implementation of {@link Light} that represents a directional light.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DirectionalLight implements Light {
	private final Color3F radiance;
	private final Point3F center;
	private final Vector3F direction;
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
		this.radiance = Objects.requireNonNull(radiance, "radiance == null");
		this.center = Objects.requireNonNull(center, "center == null");
		this.direction = Vector3F.normalize(Objects.requireNonNull(direction, "direction == null"));
		this.radius = radius;
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
		
		final Color3F result = this.radiance;
		
		final float probabilityDensityFunctionValueDirection = 0.0F;
		final float probabilityDensityFunctionValuePosition = 1.0F / (PI * this.radius * this.radius);
		
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
		
		final OrthonormalBasis33F orthonormalBasis = new OrthonormalBasis33F(this.direction);
		
		final Vector3F u = orthonormalBasis.getU();
		final Vector3F v = orthonormalBasis.getV();
		
		final Point2F pointA = SampleGeneratorF.sampleDiskUniformDistributionByConcentricMapping(sampleA.getU(), sampleA.getV());
		
		final Point3F pointB = Point3F.add(this.center, Vector3F.multiply(Vector3F.add(Vector3F.multiply(u, pointA.getU()), Vector3F.multiply(v, pointA.getV())), this.radius));
		
		final Color3F result = this.radiance;
		
		final Ray3F ray = new Ray3F(Point3F.add(pointB, this.direction, this.radius), Vector3F.negate(this.direction));
		
		final Vector3F normal = ray.getDirection();
		
		final float probabilityDensityFunctionValueDirection = 1.0F;
		final float probabilityDensityFunctionValuePosition = 1.0F / (PI * this.radius * this.radius);
		
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
		
		final Color3F result = this.radiance;
		
		final Point3F surfaceIntersectionPoint = intersection.getSurfaceIntersectionWorldSpace().getSurfaceIntersectionPoint();
		final Point3F point = Point3F.add(surfaceIntersectionPoint, this.direction, 2.0F * this.radius);
		
		final Vector3F incoming = this.direction;
		
		final float probabilityDensityFunctionValue = 1.0F;
		
		return Optional.of(new LightRadianceIncomingResult(result, point, incoming, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code DirectionalLight} instance.
	 * 
	 * @return a {@code String} representation of this {@code DirectionalLight} instance
	 */
	@Override
	public String toString() {
		return String.format("new DirectionalLight(%s, %s, %s, %+.10f)", this.radiance, this.center, this.direction, Float.valueOf(this.radius));
	}
	
	/**
	 * Returns a {@link Vector3F} instance with the direction associated with this {@code DirectionalLight} instance.
	 * 
	 * @return a {@code Vector3F} instance with the direction associated with this {@code DirectionalLight} instance
	 */
	public Vector3F getDirection() {
		return this.direction;
	}
	
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
		} else if(!Objects.equals(this.center, DirectionalLight.class.cast(object).center)) {
			return false;
		} else if(!Objects.equals(this.direction, DirectionalLight.class.cast(object).direction)) {
			return false;
		} else if(!equal(this.radius, DirectionalLight.class.cast(object).radius)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@link Light} instance uses a delta distribution, {@code false} otherwise.
	 * <p>
	 * This {@code DirectionalLight} class uses a delta distribution, so this method will return {@code true}.
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
	 * Returns the sample count associated with this {@code DirectionalLight} instance.
	 * 
	 * @return the sample count associated with this {@code DirectionalLight} instance
	 */
	@Override
	public int getSampleCount() {
		return 1;
	}
	
	/**
	 * Returns a hash code for this {@code DirectionalLight} instance.
	 * 
	 * @return a hash code for this {@code DirectionalLight} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.radiance, this.center, this.direction, Float.valueOf(this.radius));
	}
}