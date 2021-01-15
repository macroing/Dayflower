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
package org.dayflower.scene.bxdf.rayito;

import static org.dayflower.util.Floats.PI_RECIPROCAL;
import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.max;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.BXDFResult;
import org.dayflower.scene.BXDFType;

/**
 * An {@code OrenNayarRayitoBRDF} is an implementation of {@link RayitoBXDF} that represents an Oren-Nayar BRDF (Bidirectional Reflectance Distribution Function).
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class OrenNayarRayitoBRDF extends RayitoBXDF {
	private final AngleF angle;
	private final Color3F reflectanceScale;
	private final float a;
	private final float b;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code OrenNayarRayitoBRDF} instance.
	 * <p>
	 * If either {@code angle} or {@code reflectanceScale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param angle an {@link AngleF} instance
	 * @param reflectanceScale a {@link Color3F} instance that represents the reflectance scale
	 * @throws NullPointerException thrown if, and only if, either {@code angle} or {@code reflectanceScale} are {@code null}
	 */
	public OrenNayarRayitoBRDF(final AngleF angle, final Color3F reflectanceScale) {
		super(BXDFType.DIFFUSE_REFLECTION_AND_TRANSMISSION);
		
		this.angle = Objects.requireNonNull(angle, "angle == null");
		this.reflectanceScale = Objects.requireNonNull(reflectanceScale, "reflectanceScale == null");
		this.a = 1.0F - (angle.getRadians() * angle.getRadians() / (2.0F * (angle.getRadians() * angle.getRadians() + 0.33F)));
		this.b = 0.45F * angle.getRadians() * angle.getRadians() / (angle.getRadians() * angle.getRadians() + 0.09F);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Evaluates the distribution function.
	 * <p>
	 * Returns a {@link Color3F} with the result of the evaluation.
	 * <p>
	 * If either {@code outgoing}, {@code normal} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param outgoing the outgoing direction
	 * @param normal the normal
	 * @param incoming the incoming direction
	 * @return a {@code Color3F} with the result of the evaluation
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing}, {@code normal} or {@code incoming} are {@code null}
	 */
	@Override
	public Color3F evaluateDistributionFunction(final Vector3F outgoing, final Vector3F normal, final Vector3F incoming) {
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(normal, "normal == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		final float normalDotIncoming = Vector3F.dotProduct(normal, incoming);
		final float normalDotOutgoing = Vector3F.dotProduct(normal, outgoing);
		
		final OrthonormalBasis33F orthonormalBasisShadeSpace = new OrthonormalBasis33F();
		
		final Vector3F incomingShadeSpace = Vector3F.normalize(Vector3F.transformReverse(Vector3F.negate(incoming), orthonormalBasisShadeSpace));
		final Vector3F outgoingShadeSpace = Vector3F.normalize(Vector3F.transformReverse(outgoing, orthonormalBasisShadeSpace));
		
		final float cosThetaAbsIncomingShadeSpace = incomingShadeSpace.cosThetaAbs();
		final float cosThetaAbsOutgoingShadeSpace = outgoingShadeSpace.cosThetaAbs();
		
		final float sinThetaIncomingLocalSpace = incomingShadeSpace.sinTheta();
		final float sinThetaOutgoingLocalSpace = outgoingShadeSpace.sinTheta();
		
		final float maxCos = sinThetaIncomingLocalSpace > 1.0e-4F && sinThetaOutgoingLocalSpace > 1.0e-4F ? max(0.0F, incomingShadeSpace.cosPhi() * outgoingShadeSpace.cosPhi() + incomingShadeSpace.sinPhi() * outgoingShadeSpace.sinPhi()) : 0.0F;
		
		final float sinA = cosThetaAbsIncomingShadeSpace > cosThetaAbsOutgoingShadeSpace ? sinThetaOutgoingLocalSpace : sinThetaIncomingLocalSpace;
		final float tanB = cosThetaAbsIncomingShadeSpace > cosThetaAbsOutgoingShadeSpace ? sinThetaIncomingLocalSpace / cosThetaAbsIncomingShadeSpace : sinThetaOutgoingLocalSpace / cosThetaAbsOutgoingShadeSpace;
		
		final float a = this.a;
		final float b = this.b;
		final float c = (a + b * maxCos * sinA * tanB);
		
		if(normalDotIncoming > 0.0F && normalDotOutgoing > 0.0F || normalDotIncoming < 0.0F && normalDotOutgoing < 0.0F) {
			return Color3F.BLACK;
		}
		
		return Color3F.multiply(Color3F.multiply(this.reflectanceScale, PI_RECIPROCAL), c);
	}
	
	/**
	 * Samples the distribution function.
	 * <p>
	 * Returns an optional {@link BXDFResult} with the result of the sampling.
	 * <p>
	 * If either {@code outgoing}, {@code normal} or {@code sample} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param outgoing the outgoing direction
	 * @param normal the normal
	 * @param sample the sample point
	 * @return an optional {@code BXDFResult} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing}, {@code normal} or {@code sample} are {@code null}
	 */
	@Override
	public Optional<BXDFResult> sampleDistributionFunction(final Vector3F outgoing, final Vector3F normal, final Point2F sample) {
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(normal, "normal == null");
		Objects.requireNonNull(sample, "sample == null");
		
		final Vector3F incomingSample = SampleGeneratorF.sampleHemisphereCosineDistribution(sample.getU(), sample.getV());
		final Vector3F incoming = outgoing.getZ() < 0.0F ? new Vector3F(incomingSample.getX(), incomingSample.getY(), -incomingSample.getZ()) : incomingSample;
		
		final BXDFType bXDFType = getBXDFType();
		
		final Color3F result = evaluateDistributionFunction(outgoing, normal, incoming);
		
		final float probabilityDensityFunctionValue = evaluateProbabilityDensityFunction(outgoing, normal, incoming);
		
		return Optional.of(new BXDFResult(bXDFType, result, incoming, outgoing, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code OrenNayarRayitoBRDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code OrenNayarRayitoBRDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new OrenNayarRayitoBRDF(%s, %s)", this.angle, this.reflectanceScale);
	}
	
	/**
	 * Compares {@code object} to this {@code OrenNayarRayitoBRDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code OrenNayarRayitoBRDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code OrenNayarRayitoBRDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code OrenNayarRayitoBRDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof OrenNayarRayitoBRDF)) {
			return false;
		} else if(!Objects.equals(this.angle, OrenNayarRayitoBRDF.class.cast(object).angle)) {
			return false;
		} else if(!Objects.equals(this.reflectanceScale, OrenNayarRayitoBRDF.class.cast(object).reflectanceScale)) {
			return false;
		} else if(!equal(this.a, OrenNayarRayitoBRDF.class.cast(object).a)) {
			return false;
		} else if(!equal(this.b, OrenNayarRayitoBRDF.class.cast(object).b)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Evaluates the probability density function (PDF).
	 * <p>
	 * Returns a {@code float} with the probability density function (PDF) value.
	 * <p>
	 * If either {@code outgoing}, {@code normal} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param outgoing the outgoing direction
	 * @param normal the normal
	 * @param incoming the incoming direction
	 * @return a {@code float} with the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing}, {@code normal} or {@code incoming} are {@code null}
	 */
	@Override
	public float evaluateProbabilityDensityFunction(final Vector3F outgoing, final Vector3F normal, final Vector3F incoming) {
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(normal, "normal == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		final float normalDotIncoming = Vector3F.dotProduct(normal, incoming);
		final float normalDotOutgoing = Vector3F.dotProduct(normal, outgoing);
		
		final OrthonormalBasis33F orthonormalBasisShadeSpace = new OrthonormalBasis33F();
		
		final Vector3F incomingShadeSpace = Vector3F.normalize(Vector3F.transformReverse(Vector3F.negate(incoming), orthonormalBasisShadeSpace));
		final Vector3F outgoingShadeSpace = Vector3F.normalize(Vector3F.transformReverse(outgoing, orthonormalBasisShadeSpace));
		
		final float cosThetaAbsIncomingShadeSpace = incomingShadeSpace.cosThetaAbs();
		final float cosThetaAbsOutgoingShadeSpace = outgoingShadeSpace.cosThetaAbs();
		
		final float sinThetaIncomingShadeSpace = incomingShadeSpace.sinTheta();
		final float sinThetaOutgoingShadeSpace = outgoingShadeSpace.sinTheta();
		
		final float maxCos = sinThetaIncomingShadeSpace > 1.0e-4F && sinThetaOutgoingShadeSpace > 1.0e-4F ? max(0.0F, incomingShadeSpace.cosPhi() * outgoingShadeSpace.cosPhi() + incomingShadeSpace.sinPhi() * outgoingShadeSpace.sinPhi()) : 0.0F;
		
		final float sinA = cosThetaAbsIncomingShadeSpace > cosThetaAbsOutgoingShadeSpace ? sinThetaOutgoingShadeSpace : sinThetaIncomingShadeSpace;
		final float tanB = cosThetaAbsIncomingShadeSpace > cosThetaAbsOutgoingShadeSpace ? sinThetaIncomingShadeSpace / cosThetaAbsIncomingShadeSpace : sinThetaOutgoingShadeSpace / cosThetaAbsOutgoingShadeSpace;
		
		final float a = this.a;
		final float b = this.b;
		final float c = (a + b * maxCos * sinA * tanB);
		
		if(normalDotIncoming > 0.0F && normalDotOutgoing > 0.0F || normalDotIncoming < 0.0F && normalDotOutgoing < 0.0F) {
			return 0.0F;
		}
		
		return PI_RECIPROCAL * c * abs(normalDotIncoming);
	}
	
	/**
	 * Returns a hash code for this {@code OrenNayarRayitoBRDF} instance.
	 * 
	 * @return a hash code for this {@code OrenNayarRayitoBRDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.angle, this.reflectanceScale, Float.valueOf(this.a), Float.valueOf(this.b));
	}
}