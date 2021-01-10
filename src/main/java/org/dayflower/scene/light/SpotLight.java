/**
 * Copyright 2020 - 2021 J&#246;rgen Lundgren
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

import static org.dayflower.util.Floats.PI_MULTIPLIED_BY_2;
import static org.dayflower.util.Floats.cos;
import static org.dayflower.util.Floats.equal;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.LightRadianceEmittedResult;
import org.dayflower.scene.LightRadianceIncomingResult;

/**
 * A {@code SpotLight} is an implementation of {@link Light} that represents a spotlight.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SpotLight implements Light {
	private final AngleF coneAngle;
	private final AngleF coneAngleDelta;
	private final Color3F intensity;
	private final Matrix44F lightToWorld;
	private final Matrix44F lightToWorldInternal;
	private final Matrix44F worldToLightInternal;
	private final Point3F eye;
	private final Point3F lookAt;
	private final Point3F position;
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
	 * new SpotLight(coneAngle, coneAngleDelta, intensity, Matrix44F.identity());
	 * }
	 * </pre>
	 * 
	 * @param coneAngle an {@link AngleF} instance with the cone angle
	 * @param coneAngleDelta an {@code AngleF} instance with the cone angle delta
	 * @param intensity a {@link Color3F} instance with the intensity
	 * @throws NullPointerException thrown if, and only if, either {@code coneAngle}, {@code coneAngleDelta} or {@code intensity} are {@code null}
	 */
	public SpotLight(final AngleF coneAngle, final AngleF coneAngleDelta, final Color3F intensity) {
		this(coneAngle, coneAngleDelta, intensity, Matrix44F.identity());
	}
	
	/**
	 * Constructs a new {@code SpotLight} instance.
	 * <p>
	 * If either {@code coneAngle}, {@code coneAngleDelta}, {@code intensity} or {@code lightToWorld} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SpotLight(coneAngle, coneAngleDelta, intensity, lightToWorld, new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, 0.0F, 1.0F));
	 * }
	 * </pre>
	 * 
	 * @param coneAngle an {@link AngleF} instance with the cone angle
	 * @param coneAngleDelta an {@code AngleF} instance with the cone angle delta
	 * @param intensity a {@link Color3F} instance with the intensity
	 * @param lightToWorld a {@link Matrix44F} instance with the light to world transformation
	 * @throws NullPointerException thrown if, and only if, either {@code coneAngle}, {@code coneAngleDelta}, {@code intensity} or {@code lightToWorld} are {@code null}
	 */
	public SpotLight(final AngleF coneAngle, final AngleF coneAngleDelta, final Color3F intensity, final Matrix44F lightToWorld) {
		this(coneAngle, coneAngleDelta, intensity, lightToWorld, new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, 0.0F, 1.0F));
	}
	
	/**
	 * Constructs a new {@code SpotLight} instance.
	 * <p>
	 * If either {@code coneAngle}, {@code coneAngleDelta}, {@code intensity}, {@code lightToWorld}, {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param coneAngle an {@link AngleF} instance with the cone angle
	 * @param coneAngleDelta an {@code AngleF} instance with the cone angle delta
	 * @param intensity a {@link Color3F} instance with the intensity
	 * @param lightToWorld a {@link Matrix44F} instance with the light to world transformation
	 * @param eye a {@link Point3F} instance with the point to look from
	 * @param lookAt a {@code Point3F} instance with the point to look at
	 * @throws NullPointerException thrown if, and only if, either {@code coneAngle}, {@code coneAngleDelta}, {@code intensity}, {@code lightToWorld}, {@code eye} or {@code lookAt} are {@code null}
	 */
	public SpotLight(final AngleF coneAngle, final AngleF coneAngleDelta, final Color3F intensity, final Matrix44F lightToWorld, final Point3F eye, final Point3F lookAt) {
		this.coneAngle = Objects.requireNonNull(coneAngle, "coneAngle == null");
		this.coneAngleDelta = Objects.requireNonNull(coneAngleDelta, "coneAngleDelta == null");
		this.intensity = Objects.requireNonNull(intensity, "intensity == null");
		this.lightToWorld = Objects.requireNonNull(lightToWorld, "lightToWorld == null");
//		this.lightToWorldInternal = Matrix44F.multiply(Matrix44F.multiply(lightToWorld, Matrix44F.translate(eye)), Matrix44F.inverse(Matrix44F.transpose(Matrix44F.rotate(new OrthonormalBasis33F(Vector3F.directionNormalized(eye, lookAt))))));
		this.lightToWorldInternal = Matrix44F.multiply(Matrix44F.multiply(lightToWorld, Matrix44F.translate(eye)), Matrix44F.rotate(new OrthonormalBasis33F(Vector3F.directionNormalized(eye, lookAt))));
		this.worldToLightInternal = Matrix44F.inverse(this.lightToWorldInternal);
		this.eye = Objects.requireNonNull(eye, "eye == null");
		this.lookAt = Objects.requireNonNull(lookAt, "lookAt == null");
		this.position = Point3F.transformAndDivide(this.lightToWorldInternal, new Point3F());
		this.cosConeAngle = cos(coneAngle.getRadians());
		this.cosConeAngleMinusConeAngleDelta = cos(AngleF.subtract(coneAngle, coneAngleDelta).getRadians());
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
	 * Returns a {@link Color3F} instance with the power of this {@code SpotLight} instance.
	 * <p>
	 * This method represents the {@code Light} method {@code Power()} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @return a {@code Color3F} instance with the power of this {@code SpotLight} instance
	 */
	@Override
	public Color3F power() {
		return Color3F.multiply(this.intensity, PI_MULTIPLIED_BY_2 * (1.0F - 0.5F * (this.cosConeAngleMinusConeAngleDelta + this.cosConeAngle)));
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
		
		final Color3F result = Color3F.multiply(this.intensity, doComputeFalloff(ray.getDirection()));
		
		final float probabilityDensityFunctionValueDirection = Vector3F.transform(this.worldToLightInternal, ray.getDirection()).cosTheta() >= this.cosConeAngle ? SampleGeneratorF.coneUniformDistributionProbabilityDensityFunction(this.cosConeAngle) : 0.0F;
		final float probabilityDensityFunctionValuePosition = 0.0F;
		
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
		
		final Vector3F directionLightSpace = SampleGeneratorF.sampleConeUniformDistribution(sampleA.getU(), sampleA.getV(), this.cosConeAngle);
		final Vector3F directionWorldSpace = Vector3F.transform(this.lightToWorldInternal, directionLightSpace);
		
		final Ray3F ray = new Ray3F(this.position, directionWorldSpace);
		
		final Color3F result = Color3F.multiply(this.intensity, doComputeFalloff(ray.getDirection()));
		
		final Vector3F normal = ray.getDirection();
		
		final float probabilityDensityFunctionValueDirection = SampleGeneratorF.coneUniformDistributionProbabilityDensityFunction(this.cosConeAngle);
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
		
		final Vector3F incoming = Vector3F.directionNormalized(surfaceIntersectionPoint, position);
		
		final Color3F intensity = this.intensity;
		final Color3F result = Color3F.divide(Color3F.multiply(intensity, doComputeFalloff(Vector3F.negate(incoming))), Point3F.distanceSquared(surfaceIntersectionPoint, position));
		
		final float probabilityDensityFunctionValue = 1.0F;
		
		return Optional.of(new LightRadianceIncomingResult(result, position, incoming, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SpotLight} instance.
	 * 
	 * @return a {@code String} representation of this {@code SpotLight} instance
	 */
	@Override
	public String toString() {
		return String.format("new SpotLight(%s, %s, %s, %s, %s, %s)", this.coneAngle, this.coneAngleDelta, this.intensity, this.lightToWorld, this.eye, this.lookAt);
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
		} else if(!Objects.equals(this.coneAngle, SpotLight.class.cast(object).coneAngle)) {
			return false;
		} else if(!Objects.equals(this.coneAngleDelta, SpotLight.class.cast(object).coneAngleDelta)) {
			return false;
		} else if(!Objects.equals(this.intensity, SpotLight.class.cast(object).intensity)) {
			return false;
		} else if(!Objects.equals(this.lightToWorld, SpotLight.class.cast(object).lightToWorld)) {
			return false;
		} else if(!Objects.equals(this.lightToWorldInternal, SpotLight.class.cast(object).lightToWorldInternal)) {
			return false;
		} else if(!Objects.equals(this.worldToLightInternal, SpotLight.class.cast(object).worldToLightInternal)) {
			return false;
		} else if(!Objects.equals(this.eye, SpotLight.class.cast(object).eye)) {
			return false;
		} else if(!Objects.equals(this.lookAt, SpotLight.class.cast(object).lookAt)) {
			return false;
		} else if(!Objects.equals(this.position, SpotLight.class.cast(object).position)) {
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
	 * Returns {@code true} if, and only if, this {@link Light} instance uses a delta distribution, {@code false} otherwise.
	 * <p>
	 * This {@code SpotLight} class uses a delta distribution, so this method will return {@code true}.
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
	 * Returns the sample count associated with this {@code SpotLight} instance.
	 * 
	 * @return the sample count associated with this {@code SpotLight} instance
	 */
	@Override
	public int getSampleCount() {
		return 1;
	}
	
	/**
	 * Returns a hash code for this {@code SpotLight} instance.
	 * 
	 * @return a hash code for this {@code SpotLight} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.coneAngle, this.coneAngleDelta, this.intensity, this.lightToWorld, this.lightToWorldInternal, this.worldToLightInternal, this.eye, this.lookAt, this.position, Float.valueOf(this.cosConeAngle), Float.valueOf(this.cosConeAngleMinusConeAngleDelta));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private float doComputeFalloff(final Vector3F direction) {
		final Vector3F directionLightSpace = Vector3F.normalize(Vector3F.transform(this.worldToLightInternal, direction));
		
		final float cosTheta = directionLightSpace.cosTheta();
		
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