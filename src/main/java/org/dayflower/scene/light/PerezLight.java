/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
import static org.dayflower.utility.Floats.acos;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.isZero;
import static org.dayflower.utility.Floats.max;
import static org.dayflower.utility.Floats.random;
import static org.dayflower.utility.Floats.saturate;
import static org.dayflower.utility.Floats.sin;
import static org.dayflower.utility.Floats.toFloat;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Quaternion4F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.ImageF;
import org.dayflower.image.PixelImageF;
import org.dayflower.sampler.Distribution2F;
import org.dayflower.sampler.Sample2F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.LightSample;
import org.dayflower.scene.Transform;

import org.macroing.art4j.color.Color3F;
import org.macroing.art4j.color.ColorSpaceF;
import org.macroing.art4j.curve.ChromaticSpectralCurveF;
import org.macroing.art4j.curve.IrregularSpectralCurveF;
import org.macroing.art4j.curve.RegularSpectralCurveF;
import org.macroing.art4j.curve.SpectralCurveF;
import org.macroing.java.lang.Doubles;

/**
 * A {@code PerezLight} is a {@link Light} implementation of the Perez algorithm.
 * <p>
 * This class is mutable and not thread-safe.
 * <p>
 * This {@code Light} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PerezLight extends Light {
	/**
	 * The name of this {@code PerezLight} class.
	 */
	public static final String NAME = "Perez Light";
	
	/**
	 * The ID of this {@code PerezLight} class.
	 */
	public static final int ID = 4;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final SpectralCurveF K_GAS_ABSORPTION_ATTENUATION_SPECTRAL_CURVE;
	private static final SpectralCurveF K_OZONE_ABSORPTION_ATTENUATION_SPECTRAL_CURVE;
	private static final SpectralCurveF K_WATER_VAPOR_ABSORPTION_ATTENUATION_SPECTRAL_CURVE;
	private static final SpectralCurveF SOL_SPECTRAL_CURVE;
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
		
		K_GAS_ABSORPTION_ATTENUATION_SPECTRAL_CURVE = new IrregularSpectralCurveF(K_GAS_ABSORPTION_ATTENUATION_AMPLITUDES, K_GAS_ABSORPTION_ATTENUATION_WAVELENGTHS);
		K_OZONE_ABSORPTION_ATTENUATION_SPECTRAL_CURVE = new IrregularSpectralCurveF(K_OZONE_ABSORPTION_ATTENUATION_AMPLITUDES, K_OZONE_ABSORPTION_ATTENUATION_WAVELENGTHS);
		K_WATER_VAPOR_ABSORPTION_ATTENUATION_SPECTRAL_CURVE = new IrregularSpectralCurveF(K_WATER_VAPOR_ABSORPTION_ATTENUATION_AMPLITUDES, K_WATER_VAPOR_ABSORPTION_ATTENUATION_WAVELENGTHS);
		
		SOL_SPECTRAL_CURVE = new RegularSpectralCurveF(380.0F, 750.0F, SOL_AMPLITUDES);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Color3F sunColor;
	private Distribution2F distribution;
	private Vector3F sunDirectionObjectSpace;
	private Vector3F sunDirectionWorldSpace;
	private double[] perezRelativeLuminance;
	private double[] perezX;
	private double[] perezY;
	private double[] zenith;
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
		super(new Transform(), 1, false);
		
		set(turbidity, sunDirectionWorldSpace);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the radiance emitted along {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @return a {@code Color3F} instance with the radiance emitted along {@code ray}
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Color3F evaluateRadianceEmitted(final Ray3F ray) {
		Objects.requireNonNull(ray, "ray == null");
		
		final Vector3F incomingWorldSpace = ray.getDirection();
		final Vector3F incomingObjectSpace = doTransformToObjectSpace(incomingWorldSpace);
		
		final Color3F result = Color3F.minTo0(doRadianceSky(incomingObjectSpace));
		
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
	 * 
	 * @return a {@code Color3F} instance with the power of this {@code PerezLight} instance
	 */
	@Override
	public Color3F power() {
		final Vector3F incomingObjectSpace = Vector3F.directionSpherical(0.5F, 0.5F);
		
		final Color3F result = Color3F.minTo0(doRadianceSky(incomingObjectSpace));
		
		return Color3F.multiply(result, PI * this.radius * this.radius);
	}
	
	/**
	 * Returns a {@link Distribution2F} instance that represents the distribution used by this {@code PerezLight} instance.
	 * 
	 * @return a {@code Distribution2F} instance that represents the distribution used by this {@code PerezLight} instance
	 */
	public Distribution2F getDistribution() {
		return this.distribution;
	}
	
	/**
	 * Returns an {@link ImageF} representation of this {@code PerezLight} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @return an {@code ImageF} representation of this {@code PerezLight} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
	public ImageF toImage(final int resolutionX, final int resolutionY) {
		final PixelImageF pixelImage = new PixelImageF(resolutionX, resolutionY);
		
		final float resolutionXReciprocal = 1.0F / resolutionX;
		final float resolutionYReciprocal = 1.0F / resolutionY;
		
		for(int x = 0; x < resolutionX; x++) {
			final float sphericalU = (x + 0.5F) * resolutionXReciprocal;
			
			for(int y = 0; y < resolutionY; y++) {
				final float sphericalV = (y + 0.5F) * resolutionYReciprocal;
				
				final Color3F colorRGB = Color3F.maxTo1(Color3F.minTo0(doRadianceSky(Vector3F.directionSpherical(sphericalU, sphericalV))));
				
				pixelImage.setColorRGB(colorRGB, x, y);
			}
		}
		
		return pixelImage;
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
		
		final float random = random();
		
		final boolean isSamplingSun = Vector3F.dotProduct(this.sunDirectionWorldSpace, intersection.getSurfaceNormalGCorrectlyOriented()) > random && Vector3F.dotProduct(this.sunDirectionWorldSpace, intersection.getSurfaceNormalSCorrectlyOriented()) > random;
		
		if(isSamplingSun) {
//			final Vector3F incomingObjectSpace = this.sunDirection;
			final Vector3F incomingWorldSpace = this.sunDirectionWorldSpace;
			
//			final float sinTheta = incomingObjectSpace.sinTheta();
			
//			if(!isZero(sinTheta)) {
				final Color3F result = this.sunColor;
				
				final Point3F point = Point3F.add(intersection.getSurfaceIntersectionPoint(), incomingWorldSpace, 2.0F * this.radius);
				
//				final Point2F sphericalCoordinates = Point2F.sphericalCoordinates(incomingObjectSpace);
				
//				final Sample2F sampleRemapped = new Sample2F(sphericalCoordinates.x, sphericalCoordinates.y);
				
//				final float probabilityDensityFunctionValue = this.distribution.continuousProbabilityDensityFunction(sampleRemapped, true) / (2.0F * PI * PI * sinTheta);
				final float probabilityDensityFunctionValue = 1.0F;
				
				return Optional.of(new LightSample(result, point, incomingWorldSpace, probabilityDensityFunctionValue));
//			}
		}
		
		final Sample2F sampleRemapped = this.distribution.continuousRemap(new Sample2F(sample.x, sample.y));
		
		final float probabilityDensityFunctionValueRemapped = this.distribution.continuousProbabilityDensityFunction(sampleRemapped, true);
		
		if(isZero(probabilityDensityFunctionValueRemapped)) {
			return Optional.empty();
		}
		
		final Vector3F incomingObjectSpace = Vector3F.directionSpherical(sampleRemapped.getU(), sampleRemapped.getV());
		final Vector3F incomingWorldSpace = doTransformToWorldSpace(incomingObjectSpace);
		
		final float sinTheta = incomingObjectSpace.sinTheta();
		
		if(isZero(sinTheta)) {
			return Optional.empty();
		}
		
		final Color3F result = Color3F.minTo0(doRadianceSky(incomingObjectSpace));
		
		final Point3F point = Point3F.add(intersection.getSurfaceIntersectionPoint(), incomingWorldSpace, 2.0F * this.radius);
		
		final float probabilityDensityFunctionValue = probabilityDensityFunctionValueRemapped / (2.0F * PI * PI * sinTheta);
		
		return Optional.of(new LightSample(result, point, incomingWorldSpace, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code PerezLight} instance.
	 * 
	 * @return a {@code String} with the name of this {@code PerezLight} instance
	 */
	@Override
	public String getName() {
		return NAME;
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
	 * Returns the sun direction in object space associated with this {@code PerezLight} instance.
	 * 
	 * @return the sun direction in object space associated with this {@code PerezLight} instance
	 */
	public Vector3F getSunDirectionObjectSpace() {
		return this.sunDirectionObjectSpace;
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
		} else if(!Objects.equals(getTransform(), PerezLight.class.cast(object).getTransform())) {
			return false;
		} else if(!Objects.equals(this.sunColor, PerezLight.class.cast(object).sunColor)) {
			return false;
		} else if(!Objects.equals(this.distribution, PerezLight.class.cast(object).distribution)) {
			return false;
		} else if(!Objects.equals(this.sunDirectionObjectSpace, PerezLight.class.cast(object).sunDirectionObjectSpace)) {
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
	 * Returns a {@code double[]} with the Perez relative luminance.
	 * <p>
	 * Modifications to the returned {@code double[]} will not affect this {@code PerezLight} instance.
	 * 
	 * @return a {@code double[]} with the Perez relative luminance
	 */
	public double[] getPerezRelativeLuminance() {
		return this.perezRelativeLuminance.clone();
	}
	
	/**
	 * Returns a {@code double[]} with the Perez X.
	 * <p>
	 * Modifications to the returned {@code double[]} will not affect this {@code PerezLight} instance.
	 * 
	 * @return a {@code double[]} with the Perez X
	 */
	public double[] getPerezX() {
		return this.perezX.clone();
	}
	
	/**
	 * Returns a {@code double[]} with the Perez Y.
	 * <p>
	 * Modifications to the returned {@code double[]} will not affect this {@code PerezLight} instance.
	 * 
	 * @return a {@code double[]} with the Perez Y
	 */
	public double[] getPerezY() {
		return this.perezY.clone();
	}
	
	/**
	 * Returns a {@code double[]} with the zenith.
	 * <p>
	 * Modifications to the returned {@code double[]} will not affect this {@code PerezLight} instance.
	 * 
	 * @return a {@code double[]} with the zenith
	 */
	public double[] getZenith() {
		return this.zenith.clone();
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
		
		final Vector3F incomingObjectSpace = doTransformToObjectSpace(incoming);
		
		final float sinTheta = sin(incomingObjectSpace.sphericalTheta());
		
		if(isZero(sinTheta)) {
			return 0.0F;
		}
		
		final Point2F sphericalCoordinates = Point2F.sphericalCoordinates(incomingObjectSpace);
		
		final Sample2F sample = new Sample2F(sphericalCoordinates.x, sphericalCoordinates.y);
		
		final float probabilityDensityFunctionValue = this.distribution.continuousProbabilityDensityFunction(sample, true) / (2.0F * PI * PI * sinTheta);
		
		return probabilityDensityFunctionValue;
	}
	
	/**
	 * Returns the radius associated with this {@code PerezLight} instance.
	 * 
	 * @return the radius associated with this {@code PerezLight} instance
	 */
	public float getRadius() {
		return this.radius;
	}
	
	/**
	 * Returns the angle theta associated with this {@code PerezLight} instance.
	 * 
	 * @return the angle theta associated with this {@code PerezLight} instance
	 */
	public float getTheta() {
		return this.theta;
	}
	
	/**
	 * Returns the turbidity associated with this {@code PerezLight} instance.
	 * 
	 * @return the turbidity associated with this {@code PerezLight} instance
	 */
	public float getTurbidity() {
		return this.turbidity;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code PerezLight} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code PerezLight} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code PerezLight} instance.
	 * 
	 * @return a hash code for this {@code PerezLight} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getTransform(), this.sunColor, this.distribution, this.sunDirectionObjectSpace, this.sunDirectionWorldSpace, Integer.valueOf(Arrays.hashCode(this.perezRelativeLuminance)), Integer.valueOf(Arrays.hashCode(this.perezX)), Integer.valueOf(Arrays.hashCode(this.perezY)), Integer.valueOf(Arrays.hashCode(this.zenith)), Float.valueOf(this.radius), Float.valueOf(this.theta), Float.valueOf(this.turbidity));
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
		Objects.requireNonNull(sunDirectionWorldSpace, "sunDirectionWorldSpace == null");
		
		doSetTransform();
		doSetRadius();
		doSetTurbidity(turbidity);
		doSetSunDirectionWorldSpace(sunDirectionWorldSpace);
		doSetSunDirectionObjectSpace();
		doSetTheta();
		doSetSunColor();
		doSetZenith();
		doInitializePerezRelativeLuminance();
		doInitializePerezX();
		doInitializePerezY();
		doInitializeDistribution();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Color3F doRadianceSky(final Vector3F direction) {
		if(direction.z < 0.0F) {
			return Color3F.BLACK;
		}
		
		final Vector3F directionSaturated = Vector3F.normalize(new Vector3F(direction.x, direction.y, max(direction.z, 0.001F)));
		
		final double theta = Doubles.acos(Doubles.saturate(directionSaturated.z, -1.0D, 1.0D));
		final double gamma = Doubles.acos(Doubles.saturate(Vector3F.dotProduct(directionSaturated, this.sunDirectionObjectSpace), -1.0D, 1.0D));
		final double relativeLuminance = doCalculatePerezFunction(this.perezRelativeLuminance, theta, gamma, this.zenith[0]) * 0.0001D;
		final double x = doCalculatePerezFunction(this.perezX, theta, gamma, this.zenith[1]);
		final double y = doCalculatePerezFunction(this.perezY, theta, gamma, this.zenith[2]);
		
		final Color3F colorXYZ = ChromaticSpectralCurveF.toColorXYZ(toFloat(x), toFloat(y));
		
		final float x0 = toFloat(colorXYZ.r * relativeLuminance / colorXYZ.g);
		final float y0 = toFloat(relativeLuminance);
		final float z0 = toFloat(colorXYZ.b * relativeLuminance / colorXYZ.g);
		
		return ColorSpaceF.getDefault().convertXYZToRGB(new Color3F(x0, y0, z0));
	}
	
	private Vector3F doTransformToObjectSpace(final Vector3F vector) {
		return Vector3F.normalize(Vector3F.transform(getTransform().getWorldToObject(), vector));
	}
	
	private Vector3F doTransformToWorldSpace(final Vector3F vector) {
		return Vector3F.normalize(Vector3F.transform(getTransform().getObjectToWorld(), vector));
	}
	
	private double doCalculatePerezFunction(final double[] lam, final double theta, final double gamma, final double lvz) {
		final double den = ((1.0D + lam[0] * Doubles.exp(lam[1])) * (1.0D + lam[2] * Doubles.exp(lam[3] * this.theta) + lam[4] * Doubles.cos(this.theta) * Doubles.cos(this.theta)));
		final double num = ((1.0D + lam[0] * Doubles.exp(lam[1] / Doubles.cos(theta))) * (1.0D + lam[2] * Doubles.exp(lam[3] * gamma) + lam[4] * Doubles.cos(gamma) * Doubles.cos(gamma)));
		
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
				
//				final Color3F colorRGB = doRadianceSky(Vector3F.directionSpherical(sphericalU, sphericalV));
				final Color3F colorRGB = Color3F.minTo0(doRadianceSky(Vector3F.directionSpherical(sphericalU, sphericalV)));
				
				final float luminance = colorRGB.relativeLuminance();
//				final float luminance = 0.2989F * colorRGB.r + 0.5866F * colorRGB.g + 0.1145F * colorRGB.b;
				
				functions[u][v] = luminance * sinTheta;
			}
		}
		
		this.distribution = new Distribution2F(functions, true);
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
	
	private void doSetRadius() {
		this.radius = 10.0F;
	}
	
	private void doSetSunColor() {
		if(this.sunDirectionObjectSpace.z > 0.0F) {
			this.sunColor = Color3F.minTo0(ColorSpaceF.getDefault().convertXYZToRGB(Color3F.multiply(doCalculateAttenuatedSunlight(this.theta, this.turbidity).toColorXYZ(), 0.0001F)));
		} else {
			this.sunColor = Color3F.BLACK;
		}
	}
	
	private void doSetSunDirectionObjectSpace() {
		this.sunDirectionObjectSpace = doTransformToObjectSpace(this.sunDirectionWorldSpace);
	}
	
	private void doSetSunDirectionWorldSpace(final Vector3F sunDirectionWorldSpace) {
		this.sunDirectionWorldSpace = Vector3F.normalize(sunDirectionWorldSpace);
	}
	
	private void doSetTheta() {
		this.theta = acos(saturate(this.sunDirectionObjectSpace.z, -1.0F, 1.0F));
	}
	
	private void doSetTransform() {
		setTransform(new Transform(new Point3F(), Quaternion4F.from(Matrix44F.rotate(new OrthonormalBasis33F(Vector3F.y(), Vector3F.x())))));
	}
	
	private void doSetTurbidity(final float turbidity) {
		this.turbidity = turbidity;
	}
	
	private void doSetZenith() {
		this.zenith    = new double[3];
		this.zenith[0] = ((4.0453D * this.turbidity - 4.9710D) * Doubles.tan((4.0D / 9.0D - this.turbidity / 120.0D) * (Math.PI - 2.0D * this.theta)) - 0.2155D * this.turbidity + 2.4192D) * 1000.0D;
		this.zenith[1] = (0.00165D * (this.theta * this.theta * this.theta) - 0.00374D * (this.theta * this.theta) + 0.00208D * this.theta + 0.0D) * (this.turbidity * this.turbidity) + (-0.02902D * (this.theta * this.theta * this.theta) + 0.06377D * (this.theta * this.theta) - 0.03202D * this.theta + 0.00394D) * this.turbidity + (0.11693D * (this.theta * this.theta * this.theta) - 0.21196D * (this.theta * this.theta) + 0.06052D * this.theta + 0.25885D);
		this.zenith[2] = (0.00275D * (this.theta * this.theta * this.theta) - 0.00610D * (this.theta * this.theta) + 0.00316D * this.theta + 0.0D) * (this.turbidity * this.turbidity) + (-0.04212D * (this.theta * this.theta * this.theta) + 0.08970D * (this.theta * this.theta) - 0.04153D * this.theta + 0.00515D) * this.turbidity + (0.15346D * (this.theta * this.theta * this.theta) - 0.26756D * (this.theta * this.theta) + 0.06669D * this.theta + 0.26688D);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static SpectralCurveF doCalculateAttenuatedSunlight(final float theta, final float turbidity) {
		final float[] spectrum = new float[91];
		
		final double alpha = 1.3D;
		final double lozone = 0.35D;
		final double w = 2.0D;
		final double beta = 0.04608365822050D * turbidity - 0.04586025928522D;
		final double relativeOpticalMass = 1.0D / (Doubles.cos(theta) + 0.000940D * Doubles.pow(1.6386D - theta, -1.253D));
		
		final int wavelengthMin = 350;
		final int wavelengthMax = 800;
		final int wavelengthStep = 5;
		
		for(int i = 0, lambda = wavelengthMin; lambda <= wavelengthMax; i++, lambda += wavelengthStep) {
			final double tauRayleighScattering = Doubles.exp(-relativeOpticalMass * 0.008735D * Doubles.pow(lambda / 1000.0D, -4.08D));
			final double tauAerosolAttenuation = Doubles.exp(-relativeOpticalMass * beta * Doubles.pow(lambda / 1000.0D, -alpha));
			final double tauOzoneAbsorptionAttenuation = Doubles.exp(-relativeOpticalMass * K_OZONE_ABSORPTION_ATTENUATION_SPECTRAL_CURVE.sample(lambda) * lozone);
			final double tauGasAbsorptionAttenuation = Doubles.exp(-1.41D * K_GAS_ABSORPTION_ATTENUATION_SPECTRAL_CURVE.sample(lambda) * relativeOpticalMass / Doubles.pow(1.0D + 118.93D * K_GAS_ABSORPTION_ATTENUATION_SPECTRAL_CURVE.sample(lambda) * relativeOpticalMass, 0.45D));
			final double tauWaterVaporAbsorptionAttenuation = Doubles.exp(-0.2385D * K_WATER_VAPOR_ABSORPTION_ATTENUATION_SPECTRAL_CURVE.sample(lambda) * w * relativeOpticalMass / Doubles.pow(1.0D + 20.07D * K_WATER_VAPOR_ABSORPTION_ATTENUATION_SPECTRAL_CURVE.sample(lambda) * w * relativeOpticalMass, 0.45D));
			final double amplitude = 100.0D * SOL_SPECTRAL_CURVE.sample(lambda) * tauRayleighScattering * tauAerosolAttenuation * tauOzoneAbsorptionAttenuation * tauGasAbsorptionAttenuation * tauWaterVaporAbsorptionAttenuation;
			
			spectrum[i] = toFloat(amplitude);
		}
		
		return new RegularSpectralCurveF(wavelengthMin, wavelengthMax, spectrum);
	}
}