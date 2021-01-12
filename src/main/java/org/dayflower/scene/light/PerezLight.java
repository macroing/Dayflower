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

import static org.dayflower.util.Doubles.acos;
import static org.dayflower.util.Doubles.cos;
import static org.dayflower.util.Doubles.exp;
import static org.dayflower.util.Doubles.pow;
import static org.dayflower.util.Doubles.saturate;
import static org.dayflower.util.Doubles.tan;
import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.util.Floats.PI_RECIPROCAL;
import static org.dayflower.util.Floats.acos;
import static org.dayflower.util.Floats.cos;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.isZero;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.saturate;
import static org.dayflower.util.Floats.sin;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.ChromaticSpectralCurve;
import org.dayflower.image.Color3F;
import org.dayflower.image.ConstantSpectralCurve;
import org.dayflower.image.IrregularSpectralCurve;
import org.dayflower.image.RegularSpectralCurve;
import org.dayflower.image.SpectralCurve;
import org.dayflower.sampler.Distribution2F;
import org.dayflower.sampler.Sample2F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.LightRadianceEmittedResult;
import org.dayflower.scene.LightRadianceIncomingResult;

/**
 * A {@code PerezLight} is a {@link Light} implementation of the Perez algorithm.
 * <p>
 * This class is mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PerezLight implements Light {
	private static final SpectralCurve K_GAS_ABSORPTION_ATTENUATION_SPECTRAL_CURVE;
	private static final SpectralCurve K_OZONE_ABSORPTION_ATTENUATION_SPECTRAL_CURVE;
	private static final SpectralCurve K_WATER_VAPOR_ABSORPTION_ATTENUATION_SPECTRAL_CURVE;
	private static final SpectralCurve SOL_SPECTRAL_CURVE;
	private static final float[] K_GAS_ABSORPTION_ATTENUATION_AMPLITUDES;
	private static final float[] K_GAS_ABSORPTION_ATTENUATION_WAVELENGTHS;
	private static final float[] K_OZONE_ABSORPTION_ATTENUATION_AMPLITUDES;
	private static final float[] K_OZONE_ABSORPTION_ATTENUATION_WAVELENGTHS;
	private static final float[] K_WATER_VAPOR_ABSORPTION_ATTENUATION_AMPLITUDES;
	private static final float[] K_WATER_VAPOR_ABSORPTION_ATTENUATION_WAVELENGTHS;
	private static final float[] SOL_AMPLITUDES;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	static {
		K_GAS_ABSORPTION_ATTENUATION_AMPLITUDES = new float[] {0.0F, 3.0F, 0.210F, 0.0F};
		K_GAS_ABSORPTION_ATTENUATION_WAVELENGTHS = new float[] {759.0F, 760.0F, 770.0F, 771.0F};
		K_OZONE_ABSORPTION_ATTENUATION_AMPLITUDES = new float[] {10.0F, 4.8F, 2.7F, 1.35F, 0.8F, 0.380F, 0.160F, 0.075F, 0.04F, 0.019F, 0.007F, 0.0F, 0.003F, 0.003F, 0.004F, 0.006F, 0.008F, 0.009F, 0.012F, 0.014F, 0.017F, 0.021F, 0.025F, 0.03F, 0.035F, 0.04F, 0.045F, 0.048F, 0.057F, 0.063F, 0.07F, 0.075F, 0.08F, 0.085F, 0.095F, 0.103F, 0.110F, 0.12F, 0.122F, 0.12F, 0.118F, 0.115F, 0.12F, 0.125F, 0.130F, 0.12F, 0.105F, 0.09F, 0.079F, 0.067F, 0.057F, 0.048F, 0.036F, 0.028F, 0.023F, 0.018F, 0.014F, 0.011F, 0.010F, 0.009F, 0.007F, 0.004F, 0.0F, 0.0F};
		K_OZONE_ABSORPTION_ATTENUATION_WAVELENGTHS = new float[] {300.0F, 305.0F, 310.0F, 315.0F, 320.0F, 325.0F, 330.0F, 335.0F, 340.0F, 345.0F, 350.0F, 355.0F, 445.0F, 450.0F, 455.0F, 460.0F, 465.0F, 470.0F, 475.0F, 480.0F, 485.0F, 490.0F, 495.0F, 500.0F, 505.0F, 510.0F, 515.0F, 520.0F, 525.0F, 530.0F, 535.0F, 540.0F, 545.0F, 550.0F, 555.0F, 560.0F, 565.0F, 570.0F, 575.0F, 580.0F, 585.0F, 590.0F, 595.0F, 600.0F, 605.0F, 610.0F, 620.0F, 630.0F, 640.0F, 650.0F, 660.0F, 670.0F, 680.0F, 690.0F, 700.0F, 710.0F, 720.0F, 730.0F, 740.0F, 750.0F, 760.0F, 770.0F, 780.0F, 790.0F};
		K_WATER_VAPOR_ABSORPTION_ATTENUATION_AMPLITUDES = new float[] {0.0F, 0.160e-1F, 0.240e-1F, 0.125e-1F, 0.100e+1F, 0.870F, 0.610e-1F, 0.100e-2F, 0.100e-4F, 0.100e-4F, 0.600e-3F, 0.175e-1F, 0.360e-1F};
		K_WATER_VAPOR_ABSORPTION_ATTENUATION_WAVELENGTHS = new float[] {689.0F, 690.0F, 700.0F, 710.0F, 720.0F, 730.0F, 740.0F, 750.0F, 760.0F, 770.0F, 780.0F, 790.0F, 800.0F};
		
		SOL_AMPLITUDES = new float[] {165.5F, 162.3F, 211.2F, 258.8F, 258.2F, 242.3F, 267.6F, 296.6F, 305.4F, 300.6F, 306.6F, 288.3F, 287.1F, 278.2F, 271.0F, 272.3F, 263.6F, 255.0F, 250.6F, 253.1F, 253.5F, 251.3F, 246.3F, 241.7F, 236.8F, 232.1F, 228.2F, 223.4F, 219.7F, 215.3F, 211.0F, 207.3F, 202.4F, 198.7F, 194.3F, 190.7F, 186.3F, 182.6F};
		
		K_GAS_ABSORPTION_ATTENUATION_SPECTRAL_CURVE = new IrregularSpectralCurve(K_GAS_ABSORPTION_ATTENUATION_AMPLITUDES, K_GAS_ABSORPTION_ATTENUATION_WAVELENGTHS);
		K_OZONE_ABSORPTION_ATTENUATION_SPECTRAL_CURVE = new IrregularSpectralCurve(K_OZONE_ABSORPTION_ATTENUATION_AMPLITUDES, K_OZONE_ABSORPTION_ATTENUATION_WAVELENGTHS);
		K_WATER_VAPOR_ABSORPTION_ATTENUATION_SPECTRAL_CURVE = new IrregularSpectralCurve(K_WATER_VAPOR_ABSORPTION_ATTENUATION_AMPLITUDES, K_WATER_VAPOR_ABSORPTION_ATTENUATION_WAVELENGTHS);
		
		SOL_SPECTRAL_CURVE = new RegularSpectralCurve(380.0F, 750.0F, SOL_AMPLITUDES);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Color3F sunColor;
	private Distribution2F distribution;
	private OrthonormalBasis33F orthonormalBasis;
	private SpectralCurve sunSpectralRadiance;
	private Vector3F sunDirection;
	private Vector3F sunDirectionWorldSpace;
	private double[] perezRelativeLuminance;
	private double[] perezX;
	private double[] perezY;
	private double[] zenith;
	private float jacobian;
	private float radius;
	private float theta;
	private float turbidity;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PerezLight} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PerezLight(2.0F);
	 * }
	 * </pre>
	 */
	public PerezLight() {
		this(2.0F);
	}
	
	/**
	 * Constructs a new {@code PerezLight} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PerezLight(turbidity, new Vector3F(1.0F, 1.0F, -1.0F));
	 * }
	 * </pre>
	 * 
	 * @param turbidity the turbidity associated with this {@code PerezLight} instance
	 */
	public PerezLight(final float turbidity) {
		this(turbidity, new Vector3F(1.0F, 1.0F, -1.0F));
	}
	
	/**
	 * Constructs a new {@code PerezLight} instance.
	 * <p>
	 * If {@code sunDirectionWorldSpace} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param turbidity the turbidity associated with this {@code PerezLight} instance
	 * @param sunDirectionWorldSpace the sun direction in world space associated with this {@code PerezLight} instance
	 * @throws NullPointerException thrown if, and only if, {@code sunDirectionWorldSpace} is {@code null}
	 */
	public PerezLight(final float turbidity, final Vector3F sunDirectionWorldSpace) {
		set(turbidity, sunDirectionWorldSpace);
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
//		TODO: Verify!
//		Spectrum InfiniteAreaLight::Le(const RayDifferential &ray) const {
//			Vector3f w = Normalize(WorldToLight(ray.d));
//			
//			Point2f st(SphericalPhi(w) * Inv2Pi, SphericalTheta(w) * InvPi);
//			
//			return Spectrum(Lmap->Lookup(st), SpectrumType::Illuminant);
//		}
		
		final Vector3F incoming = ray.getDirection();
		final Vector3F incomingLocal = doTransformToLocalSpace(incoming);
		
		final Color3F result = Color3F.minimumTo0(doRadianceSky(incomingLocal));
		
		return result;
	}
	
	/**
	 * Returns the sun color associated with this {@code PerezLight} instance.
	 * 
	 * @return the sun color associated with this {@code PerezLight} instance
	 */
	public Color3F getSunColor() {
		return this.sunColor;
	}
	
	/**
	 * Returns a {@link Color3F} instance with the power of this {@code PerezLight} instance.
	 * <p>
	 * This method represents the {@code Light} method {@code Power()} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @return a {@code Color3F} instance with the power of this {@code PerezLight} instance
	 */
	@Override
	public Color3F power() {
//		TODO: Verify!
//		Spectrum InfiniteAreaLight::Power() const {
//			return Pi * worldRadius * worldRadius * Spectrum(Lmap->Lookup(Point2f(.5f, .5f), .5f), SpectrumType::Illuminant);
//		}
		
		final Vector3F incomingLocal = Vector3F.directionSpherical(0.5F, 0.5F);
		
		final Color3F result = doRadianceSky(incomingLocal);
		
		return Color3F.multiply(result, PI * this.radius * this.radius);
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
		
		final Vector3F incoming = ray.getDirection();
		final Vector3F incomingLocal = doTransformToLocalSpace(incoming);
		final Vector3F direction = Vector3F.negate(incomingLocal);
		
		final float phi = direction.sphericalPhi();
		final float theta = direction.sphericalTheta();
		
		final Sample2F sample = new Sample2F(phi * PI_MULTIPLIED_BY_2_RECIPROCAL, theta * PI_RECIPROCAL);
		
		final float radius = this.radius;
		
		final Color3F result = doRadianceSky(incomingLocal);
		
		final float probabilityDensityFunctionValue = this.distribution.probabilityDensityFunction(sample);
		final float probabilityDensityFunctionValueDirection = sin(theta) * this.jacobian / probabilityDensityFunctionValue;
		final float probabilityDensityFunctionValuePosition = 1.0F / (PI * radius * radius);
		
		return Optional.of(new LightRadianceEmittedResult(result, ray, normal, probabilityDensityFunctionValueDirection, probabilityDensityFunctionValuePosition));
		
//		TODO: Verify!
//		void InfiniteAreaLight::Pdf_Le(const Ray &ray, const Normal3f &, Float *pdfPos, Float *pdfDir) const {
//			Vector3f d = -WorldToLight(ray.d);
//			
//			Float theta = SphericalTheta(d), phi = SphericalPhi(d);
//			
//			Point2f uv(phi * Inv2Pi, theta * InvPi);
//			
//			Float mapPdf = distribution->Pdf(uv);
//			
//			*pdfDir = mapPdf / (2 * Pi * Pi * std::sin(theta));
//			*pdfPos = 1 / (Pi * worldRadius * worldRadius);
//		}
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
		
//		TODO: Implement!
//		Spectrum InfiniteAreaLight::Sample_Le(const Point2f &u1, const Point2f &u2, Float time, Ray *ray, Normal3f *nLight, Float *pdfPos, Float *pdfDir) const {
//			Point2f u = u1;
//			
//			Float mapPdf;
//			
//			Point2f uv = distribution->SampleContinuous(u, &mapPdf);
//			
//			if (mapPdf == 0) {
//				return Spectrum(0.f);
//			}
//			
//			Float theta = uv[1] * Pi, phi = uv[0] * 2.f * Pi;
//			Float cosTheta = std::cos(theta), sinTheta = std::sin(theta);
//			Float sinPhi = std::sin(phi), cosPhi = std::cos(phi);
//			
//			Vector3f d = -LightToWorld(Vector3f(sinTheta * cosPhi, sinTheta * sinPhi, cosTheta));
//			
//			*nLight = (Normal3f)d;
//			
//			Vector3f v1, v2;
//			
//			CoordinateSystem(-d, &v1, &v2);
//			
//			Point2f cd = ConcentricSampleDisk(u2);
//			
//			Point3f pDisk = worldCenter + worldRadius * (cd.x * v1 + cd.y * v2);
//			
//			*ray = Ray(pDisk + worldRadius * -d, d, Infinity, time);
//			
//			*pdfDir = sinTheta == 0 ? 0 : mapPdf / (2 * Pi * Pi * sinTheta);
//			*pdfPos = 1 / (Pi * worldRadius * worldRadius);
//			
//			return Spectrum(Lmap->Lookup(uv), SpectrumType::Illuminant);
//		}
		
		return Optional.empty();
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
		
//		TODO: Verify!
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
		
		final Sample2F sample0 = new Sample2F(sample.getU(), sample.getV());
		final Sample2F sample1 = this.distribution.discreteRemap(sample0);
		
		final float probabilityDensityFunctionValue0 = this.distribution.discreteProbabilityDensityFunction(sample0);
		
		if(isZero(probabilityDensityFunctionValue0)) {
			return Optional.empty();
		}
		
		final float u = sample1.getU();
		final float v = sample1.getV();
		
		final int indexM = this.distribution.getMarginal().index(sample.getU());
		final int indexC = this.distribution.getConditional(indexM).index(sample.getV());
		
		final float su = (indexM + u) / 32.0F;
		final float sv = (indexC + v) / 32.0F;
		
		final Vector3F incomingLocal = Vector3F.directionSpherical(su, sv);
		final Vector3F incoming = doTransformToWorldSpace(incomingLocal);
		
		final Color3F result = doRadianceSky(incomingLocal);
		
		final Point3F surfaceIntersectionPoint = intersection.getSurfaceIntersectionWorldSpace().getSurfaceIntersectionPoint();
		final Point3F point = Point3F.add(surfaceIntersectionPoint, incoming, 2.0F * this.radius);
		
		final float sinTheta = sin(PI * sv);
		final float probabilityDensityFunctionValue1 = sinTheta * this.jacobian / probabilityDensityFunctionValue0;
		
		return Optional.of(new LightRadianceIncomingResult(result, point, incoming, probabilityDensityFunctionValue1));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code PerezLight} instance.
	 * 
	 * @return a {@code String} representation of this {@code PerezLight} instance
	 */
	@Override
	public String toString() {
		return String.format("new PerezLight(%+.10f, %s)", Float.valueOf(this.turbidity), this.sunDirectionWorldSpace);
	}
	
	/**
	 * Returns the sun direction in world space associated with this {@code PerezLight} instance.
	 * 
	 * @return the sun direction in world space associated with this {@code PerezLight} instance
	 */
	public Vector3F getSunDirectionWorldSpace() {
		return this.sunDirectionWorldSpace;
	}
	
	/**
	 * Compares {@code object} to this {@code PerezLight} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code PerezLight}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code PerezLight} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code PerezLight}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof PerezLight)) {
			return false;
		} else if(!Objects.equals(this.sunColor, PerezLight.class.cast(object).sunColor)) {
			return false;
		} else if(!Objects.equals(this.distribution, PerezLight.class.cast(object).distribution)) {
			return false;
		} else if(!Objects.equals(this.orthonormalBasis, PerezLight.class.cast(object).orthonormalBasis)) {
			return false;
		} else if(!Objects.equals(this.sunSpectralRadiance, PerezLight.class.cast(object).sunSpectralRadiance)) {
			return false;
		} else if(!Objects.equals(this.sunDirection, PerezLight.class.cast(object).sunDirection)) {
			return false;
		} else if(!Objects.equals(this.sunDirectionWorldSpace, PerezLight.class.cast(object).sunDirectionWorldSpace)) {
			return false;
		} else if(!Arrays.equals(this.perezRelativeLuminance, PerezLight.class.cast(object).perezRelativeLuminance)) {
			return false;
		} else if(!Arrays.equals(this.perezX, PerezLight.class.cast(object).perezX)) {
			return false;
		} else if(!Arrays.equals(this.perezY, PerezLight.class.cast(object).perezY)) {
			return false;
		} else if(!Arrays.equals(this.zenith, PerezLight.class.cast(object).zenith)) {
			return false;
		} else if(!equal(this.radius, PerezLight.class.cast(object).radius)) {
			return false;
		} else if(!equal(this.theta, PerezLight.class.cast(object).theta)) {
			return false;
		} else if(!equal(this.turbidity, PerezLight.class.cast(object).turbidity)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code PerezLight} instance uses a delta distribution, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code PerezLight} instance uses a delta distribution, {@code false} otherwise
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
		
//		TODO: Verify!
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
		
		final Vector3F incomingLocal = doTransformToLocalSpace(incoming);
		
		final float phi = incomingLocal.sphericalPhi();
		final float theta = incomingLocal.sphericalTheta();
		final float sinTheta = sin(theta);
		
		if(isZero(sinTheta)) {
			return 0.0F;
		}
		
		final float u = phi * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float v = theta * PI_RECIPROCAL;
		
		final Sample2F sample = new Sample2F(u, v);
		
		final float probabilityDensityFunctionValue0 = this.distribution.probabilityDensityFunction(sample);
		final float probabilityDensityFunctionValue1 = sinTheta * this.jacobian / probabilityDensityFunctionValue0;
		
		return probabilityDensityFunctionValue1;
	}
	
	/**
	 * Returns the sample count associated with this {@code PerezLight} instance.
	 * 
	 * @return the sample count associated with this {@code PerezLight} instance
	 */
	@Override
	public int getSampleCount() {
		return 1;
	}
	
	/**
	 * Returns a hash code for this {@code PerezLight} instance.
	 * 
	 * @return a hash code for this {@code PerezLight} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.sunColor, this.distribution, this.orthonormalBasis, this.sunSpectralRadiance, this.sunDirection, this.sunDirectionWorldSpace, Integer.valueOf(Arrays.hashCode(this.perezRelativeLuminance)), Integer.valueOf(Arrays.hashCode(this.perezX)), Integer.valueOf(Arrays.hashCode(this.perezY)), Integer.valueOf(Arrays.hashCode(this.zenith)), Float.valueOf(this.radius), Float.valueOf(this.theta), Float.valueOf(this.turbidity));
	}
	
	/**
	 * Sets the parameters for this {@code PerezLight} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * perezLight.set(2.0F);
	 * }
	 * </pre>
	 */
	public void set() {
		set(2.0F);
	}
	
	/**
	 * Sets the parameters for this {@code PerezLight} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * perezLight.set(turbidity, new Vector3F(1.0F, 1.0F, -1.0F));
	 * }
	 * </pre>
	 * 
	 * @param turbidity the turbidity associated with this {@code PerezLight} instance
	 */
	public void set(final float turbidity) {
		set(turbidity, new Vector3F(1.0F, 1.0F, -1.0F));
	}
	
	/**
	 * Sets the parameters for this {@code PerezLight} instance.
	 * <p>
	 * If {@code sunDirectionWorldSpace} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param turbidity the turbidity associated with this {@code PerezLight} instance
	 * @param sunDirectionWorldSpace the sun direction in world space associated with this {@code PerezLight} instance
	 * @throws NullPointerException thrown if, and only if, {@code sunDirectionWorldSpace} is {@code null}
	 */
	public void set(final float turbidity, final Vector3F sunDirectionWorldSpace) {
		doSetOrthonormalBasis();
		doSetRadius();
		doSetTurbidity(turbidity);
		doSetSunDirectionWorldSpace(sunDirectionWorldSpace);
		doSetSunDirection();
		doSetTheta();
		doSetSunColorAndSunSpectralRadiance();
		doSetZenith();
		doInitializeJacobian();
		doInitializePerezRelativeLuminance();
		doInitializePerezX();
		doInitializePerezY();
		doInitializeDistribution();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Color3F doRadianceSky(final Vector3F direction) {
		if(direction.getZ() < 0.0F) {
			return Color3F.BLACK;
		}
		
		final Vector3F directionSaturated = Vector3F.normalize(new Vector3F(direction.getX(), direction.getY(), max(direction.getZ(), 0.001F)));
		
		final double theta = acos(saturate(directionSaturated.getZ(), -1.0D, 1.0D));
		final double gamma = acos(saturate(Vector3F.dotProduct(directionSaturated, this.sunDirection), -1.0D, 1.0D));
		final double relativeLuminance = doCalculatePerezFunction(this.perezRelativeLuminance, theta, gamma, this.zenith[0]) * 1.0e-4D;
		final double x = doCalculatePerezFunction(this.perezX, theta, gamma, this.zenith[1]);
		final double y = doCalculatePerezFunction(this.perezY, theta, gamma, this.zenith[2]);
		
		final Color3F colorXYZ = ChromaticSpectralCurve.getColorXYZ((float)(x), (float)(y));
		
		final float x0 = (float)(colorXYZ.getX() * relativeLuminance / colorXYZ.getY());
		final float y0 = (float)(relativeLuminance);
		final float z0 = (float)(colorXYZ.getZ() * relativeLuminance / colorXYZ.getY());
		
		return Color3F.convertXYZToRGBUsingPBRT(new Color3F(x0, y0, z0));
	}
	
	private Vector3F doTransformToLocalSpace(final Vector3F vector) {
		return Vector3F.normalize(Vector3F.transformReverse(vector, this.orthonormalBasis));
	}
	
	private Vector3F doTransformToWorldSpace(final Vector3F vector) {
		return Vector3F.normalize(Vector3F.transform(vector, this.orthonormalBasis));
	}
	
	private double doCalculatePerezFunction(final double[] lam, final double theta, final double gamma, final double lvz) {
		final double den = ((1.0D + lam[0] * exp(lam[1])) * (1.0D + lam[2] * exp(lam[3] * this.theta) + lam[4] * cos(this.theta) * cos(this.theta)));
		final double num = ((1.0D + lam[0] * exp(lam[1] / cos(theta))) * (1.0D + lam[2] * exp(lam[3] * gamma) + lam[4] * cos(gamma) * cos(gamma)));
		
		return lvz * num / den;
	}
	
	private void doInitializeDistribution() {
		final int resolutionU = 32;
		final int resolutionV = 32;
		
		final float resolutionUReciprocal = 1.0F / resolutionU;
		final float resolutionVReciprocal = 1.0F / resolutionV;
		
		final float[][] functions = new float[resolutionU][resolutionV];
		
		for(int u = 0; u < resolutionU; u++) {
			final float sphericalU = (u + 0.5F) * resolutionUReciprocal;
			
			for(int v = 0; v < resolutionV; v++) {
				final float sphericalV = (v + 0.5F) * resolutionVReciprocal;
				final float sinTheta = sin(PI * sphericalV);
				
				final Color3F colorRGB = doRadianceSky(Vector3F.directionSpherical(sphericalU, sphericalV));
				
				functions[u][v] = colorRGB.luminance() * sinTheta;
			}
		}
		
		this.distribution = new Distribution2F(functions, true);
	}
	
	private void doInitializeJacobian() {
		this.jacobian = (2.0F * PI * PI) / (32.0F * 32.0F);
	}
	
	private void doInitializePerezRelativeLuminance() {
		this.perezRelativeLuminance    = new double[5];
		this.perezRelativeLuminance[0] = +0.17872D * this.turbidity - 1.46303D;
		this.perezRelativeLuminance[1] = -0.35540D * this.turbidity + 0.42749D;
		this.perezRelativeLuminance[2] = -0.02266D * this.turbidity + 5.32505D;
		this.perezRelativeLuminance[3] = +0.12064D * this.turbidity - 2.57705D;
		this.perezRelativeLuminance[4] = -0.06696D * this.turbidity + 0.37027D;
	}
	
	private void doInitializePerezX() {
		this.perezX    = new double[5];
		this.perezX[0] = -0.01925D * this.turbidity - 0.25922D;
		this.perezX[1] = -0.06651D * this.turbidity + 0.00081D;
		this.perezX[2] = -0.00041D * this.turbidity + 0.21247D;
		this.perezX[3] = -0.06409D * this.turbidity - 0.89887D;
		this.perezX[4] = -0.00325D * this.turbidity + 0.04517D;
	}
	
	private void doInitializePerezY() {
		this.perezY    = new double[5];
		this.perezY[0] = -0.01669D * this.turbidity - 0.26078D;
		this.perezY[1] = -0.09495D * this.turbidity + 0.00921D;
		this.perezY[2] = -0.00792D * this.turbidity + 0.21023D;
		this.perezY[3] = -0.04405D * this.turbidity - 1.65369D;
		this.perezY[4] = -0.01092D * this.turbidity + 0.05291D;
	}
	
	private void doSetOrthonormalBasis() {
		this.orthonormalBasis = new OrthonormalBasis33F(Vector3F.y(), Vector3F.x());
	}
	
	private void doSetRadius() {
		this.radius = 1000.0F;
	}
	
	private void doSetSunColorAndSunSpectralRadiance() {
		if(this.sunDirection.getZ() > 0.0F) {
			this.sunSpectralRadiance = doCalculateAttenuatedSunlight(this.theta, this.turbidity);
			this.sunColor = Color3F.minimumTo0(Color3F.convertXYZToRGBUsingSRGB(Color3F.multiply(this.sunSpectralRadiance.toColorXYZ(), 1.0e-4F)));
		} else {
			this.sunSpectralRadiance = new ConstantSpectralCurve(0.0F);
			this.sunColor = Color3F.BLACK;
		}
	}
	
	private void doSetSunDirection() {
		this.sunDirection = Vector3F.normalize(Vector3F.transformReverse(this.sunDirectionWorldSpace, this.orthonormalBasis));
	}
	
	private void doSetSunDirectionWorldSpace(final Vector3F sunDirectionWorldSpace) {
		this.sunDirectionWorldSpace = Vector3F.normalize(sunDirectionWorldSpace);
	}
	
	private void doSetTheta() {
		this.theta = acos(saturate(this.sunDirection.getZ(), -1.0F, 1.0F));
	}
	
	private void doSetTurbidity(final float turbidity) {
		this.turbidity = turbidity;
	}
	
	private void doSetZenith() {
		this.zenith    = new double[3];
		this.zenith[0] = ((4.0453D * this.turbidity - 4.9710D) * tan((4.0D / 9.0D - this.turbidity / 120.0D) * (Math.PI - 2.0D * this.theta)) - 0.2155D * this.turbidity + 2.4192D) * 1000.0D;
		this.zenith[1] = (0.00165D * (this.theta * this.theta * this.theta) - 0.00374D * (this.theta * this.theta) + 0.00208D * this.theta + 0.0D) * (this.turbidity * this.turbidity) + (-0.02902D * (this.theta * this.theta * this.theta) + 0.06377D * (this.theta * this.theta) - 0.03202D * this.theta + 0.00394D) * this.turbidity + (0.11693D * (this.theta * this.theta * this.theta) - 0.21196D * (this.theta * this.theta) + 0.06052D * this.theta + 0.25885D);
		this.zenith[2] = (0.00275D * (this.theta * this.theta * this.theta) - 0.00610D * (this.theta * this.theta) + 0.00316D * this.theta + 0.0D) * (this.turbidity * this.turbidity) + (-0.04212D * (this.theta * this.theta * this.theta) + 0.08970D * (this.theta * this.theta) - 0.04153D * this.theta + 0.00515D) * this.turbidity + (0.15346D * (this.theta * this.theta * this.theta) - 0.26756D * (this.theta * this.theta) + 0.06669D * this.theta + 0.26688D);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static SpectralCurve doCalculateAttenuatedSunlight(final float theta, final float turbidity) {
		final float[] spectrum = new float[91];
		
		final double alpha = 1.3D;
		final double lozone = 0.35D;
		final double w = 2.0D;
		final double beta = 0.04608365822050D * turbidity - 0.04586025928522D;
		final double relativeOpticalMass = 1.0D / (cos(theta) + 0.000940D * pow(1.6386D - theta, -1.253D));
		
		for(int i = 0, lambda = 350; lambda <= 800; i++, lambda += 5) {
			final double tauRayleighScattering = exp(-relativeOpticalMass * 0.008735D * pow(lambda / 1000.0D, -4.08D));
			final double tauAerosolAttenuation = exp(-relativeOpticalMass * beta * pow(lambda / 1000.0D, -alpha));
			final double tauOzoneAbsorptionAttenuation = exp(-relativeOpticalMass * K_OZONE_ABSORPTION_ATTENUATION_SPECTRAL_CURVE.sample(lambda) * lozone);
			final double tauGasAbsorptionAttenuation = exp(-1.41D * K_GAS_ABSORPTION_ATTENUATION_SPECTRAL_CURVE.sample(lambda) * relativeOpticalMass / pow(1.0D + 118.93D * K_GAS_ABSORPTION_ATTENUATION_SPECTRAL_CURVE.sample(lambda) * relativeOpticalMass, 0.45D));
			final double tauWaterVaporAbsorptionAttenuation = exp(-0.2385D * K_WATER_VAPOR_ABSORPTION_ATTENUATION_SPECTRAL_CURVE.sample(lambda) * w * relativeOpticalMass / pow(1.0D + 20.07D * K_WATER_VAPOR_ABSORPTION_ATTENUATION_SPECTRAL_CURVE.sample(lambda) * w * relativeOpticalMass, 0.45D));
			final double amplitude = SOL_SPECTRAL_CURVE.sample(lambda) * tauRayleighScattering * tauAerosolAttenuation * tauOzoneAbsorptionAttenuation * tauGasAbsorptionAttenuation * tauWaterVaporAbsorptionAttenuation;
			
			spectrum[i] = (float)(amplitude);
		}
		
		return new RegularSpectralCurve(350.0F, 800.0F, spectrum);
	}
}