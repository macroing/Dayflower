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
package org.dayflower.scene.bxdf;

import static org.dayflower.util.Floats.PI_RECIPROCAL;
import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.max;

import java.util.Objects;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.BXDF;
import org.dayflower.scene.BXDFResult;

/**
 * An {@code OrenNayarBRDF} is an implementation of {@link BXDF} that represents an Oren-Nayar BRDF (Bidirectional Reflectance Distribution Function).
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class OrenNayarBRDF implements BXDF {
	private final AngleF angle;
	private final float a;
	private final float b;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code OrenNayarBRDF} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new OrenNayarBRDF(AngleF.degrees(20.0F));
	 * }
	 * </pre>
	 */
	public OrenNayarBRDF() {
		this(AngleF.degrees(20.0F));
	}
	
	/**
	 * Constructs a new {@code OrenNayarBRDF} instance.
	 * <p>
	 * If {@code angle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param angle an {@link AngleF} instance
	 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
	 */
	public OrenNayarBRDF(final AngleF angle) {
		this.angle = Objects.requireNonNull(angle, "angle == null");
		this.a = 1.0F - (angle.getRadians() * angle.getRadians() / (2.0F * (angle.getRadians() * angle.getRadians() + 0.33F)));
		this.b = 0.45F * angle.getRadians() * angle.getRadians() / (angle.getRadians() * angle.getRadians() + 0.09F);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Evaluates the solid angle for {@code o}, {@code n} and {@code i}.
	 * <p>
	 * Returns a {@link BXDFResult} with the result of the operation.
	 * <p>
	 * If either {@code o}, {@code n} or {@code i} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * orenNayarBRDF.evaluateSolidAngle(o, n, i, false);
	 * }
	 * </pre>
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param i a {@code Vector3F} instance with the incoming direction from the light source to the surface intersection point
	 * @return a {@code BXDFResult} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
	@Override
	public BXDFResult evaluateSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i) {
		return evaluateSolidAngle(o, n, i, false);
	}
	
	/**
	 * Evaluates the solid angle or the projected solid angle for {@code o}, {@code n} and {@code i}.
	 * <p>
	 * Returns a {@link BXDFResult} with the result of the operation.
	 * <p>
	 * If either {@code o}, {@code n} or {@code i} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param i a {@code Vector3F} instance with the incoming direction from the light source to the surface intersection point
	 * @param isProjected {@code true} if, and only if, the projected solid angle should be evaluated, {@code false} otherwise
	 * @return a {@code BXDFResult} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
	@Override
	public BXDFResult evaluateSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i, final boolean isProjected) {
		final float nDotI = Vector3F.dotProduct(n, i);
		final float nDotO = Vector3F.dotProduct(n, o);
		
		final OrthonormalBasis33F orthonormalBasisLocal = new OrthonormalBasis33F(Vector3F.x(), Vector3F.y(), Vector3F.z());
		
		final Vector3F wI = Vector3F.normalize(Vector3F.transformReverse(Vector3F.negate(i), orthonormalBasisLocal));
		final Vector3F wO = Vector3F.normalize(Vector3F.transformReverse(o, orthonormalBasisLocal));
		
		final float cosThetaAbsWI = wI.cosThetaAbs();
		final float cosThetaAbsWO = wO.cosThetaAbs();
		
		final float sinThetaWI = wI.sinTheta();
		final float sinThetaWO = wO.sinTheta();
		
		final float maxCos = sinThetaWI > 1.0e-4F && sinThetaWO > 1.0e-4F ? max(0.0F, wI.cosPhi() * wO.cosPhi() + wI.sinPhi() * wO.sinPhi()) : 0.0F;
		
		final float sinA = cosThetaAbsWI > cosThetaAbsWO ? sinThetaWO : sinThetaWI;
		final float tanB = cosThetaAbsWI > cosThetaAbsWO ? sinThetaWI / cosThetaAbsWI : sinThetaWO / cosThetaAbsWO;
		
		final float a = this.a;
		final float b = this.b;
		final float c = (a + b * maxCos * sinA * tanB);
		
		if(nDotI > 0.0F && nDotO > 0.0F || nDotI < 0.0F && nDotO < 0.0F) {
			return new BXDFResult(o, n, i, 0.0F,                           0.0F);
		} else if(isProjected) {
			return new BXDFResult(o, n, i, PI_RECIPROCAL * c,              PI_RECIPROCAL * c);
		} else {
			return new BXDFResult(o, n, i, PI_RECIPROCAL * c * abs(nDotI), PI_RECIPROCAL * c);
		}
	}
	
	/**
	 * Samples the solid angle for {@code o}, {@code n} and {@code orthonormalBasis}.
	 * <p>
	 * Returns a {@link BXDFResult} with the result of the operation.
	 * <p>
	 * If either {@code o}, {@code n} or {@code orthonormalBasis} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * orenNayarBRDF.sampleSolidAngle(o, n, orthonormalBasis, u, v, false);
	 * }
	 * </pre>
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param orthonormalBasis an {@link OrthonormalBasis33F} instance
	 * @param u the U-coordinate
	 * @param v the V-coordinate
	 * @return a {@code BXDFResult} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code orthonormalBasis} are {@code null}
	 */
	@Override
	public BXDFResult sampleSolidAngle(final Vector3F o, final Vector3F n, final OrthonormalBasis33F orthonormalBasis, final float u, final float v) {
		return sampleSolidAngle(o, n, orthonormalBasis, u, v, false);
	}
	
	/**
	 * Samples the solid angle or the projected solid angle for {@code o}, {@code n} and {@code orthonormalBasis}.
	 * <p>
	 * Returns a {@link BXDFResult} with the result of the operation.
	 * <p>
	 * If either {@code o}, {@code n} or {@code orthonormalBasis} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param orthonormalBasis an {@link OrthonormalBasis33F} instance
	 * @param u the U-coordinate
	 * @param v the V-coordinate
	 * @param isProjected {@code true} if, and only if, the projected solid angle should be sampled, {@code false} otherwise
	 * @return a {@code BXDFResult} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code orthonormalBasis} are {@code null}
	 */
	@Override
	public BXDFResult sampleSolidAngle(final Vector3F o, final Vector3F n, final OrthonormalBasis33F orthonormalBasis, final float u, final float v, final boolean isProjected) {
		final float nDotO = Vector3F.dotProduct(n, o);
		
		final Vector3F iLocalSpace = Vector3F.negate(SampleGeneratorF.sampleHemisphereCosineDistribution(u, v));
		final Vector3F iTransformed = Vector3F.normalize(Vector3F.transform(iLocalSpace, orthonormalBasis));
		final Vector3F i = nDotO < 0.0F ? Vector3F.negate(iTransformed) : iTransformed;
		
		final OrthonormalBasis33F orthonormalBasisLocal = new OrthonormalBasis33F(Vector3F.x(), Vector3F.y(), Vector3F.z());
		
		final Vector3F wI = Vector3F.normalize(Vector3F.transformReverse(Vector3F.negate(i), orthonormalBasisLocal));
		final Vector3F wO = Vector3F.normalize(Vector3F.transformReverse(o, orthonormalBasisLocal));
		
		final float cosThetaAbsWI = wI.cosThetaAbs();
		final float cosThetaAbsWO = wO.cosThetaAbs();
		
		final float sinThetaWI = wI.sinTheta();
		final float sinThetaWO = wO.sinTheta();
		
		final float maxCos = sinThetaWI > 1.0e-4F && sinThetaWO > 1.0e-4F ? max(0.0F, wI.cosPhi() * wO.cosPhi() + wI.sinPhi() * wO.sinPhi()) : 0.0F;
		
		final float sinA = cosThetaAbsWI > cosThetaAbsWO ? sinThetaWO : sinThetaWI;
		final float tanB = cosThetaAbsWI > cosThetaAbsWO ? sinThetaWI / cosThetaAbsWI : sinThetaWO / cosThetaAbsWO;
		
		final float a = this.a;
		final float b = this.b;
		final float c = (a + b * maxCos * sinA * tanB);
		
		if(isProjected) {
			return new BXDFResult(o, n, i, PI_RECIPROCAL * c, PI_RECIPROCAL * c);
		}
		
		final float nDotI = Vector3F.dotProduct(n, i);
		
		return new BXDFResult(o, n, i, PI_RECIPROCAL * c * abs(nDotI), PI_RECIPROCAL * c);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code OrenNayarBRDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code OrenNayarBRDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new OrenNayarBRDF(%s)", this.angle);
	}
	
	/**
	 * Compares {@code object} to this {@code OrenNayarBRDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code OrenNayarBRDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code OrenNayarBRDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code OrenNayarBRDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof OrenNayarBRDF)) {
			return false;
		} else if(!Objects.equals(this.angle, OrenNayarBRDF.class.cast(object).angle)) {
			return false;
		} else if(!equal(this.a, OrenNayarBRDF.class.cast(object).a)) {
			return false;
		} else if(!equal(this.b, OrenNayarBRDF.class.cast(object).b)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code OrenNayarBRDF} instance is using a Dirac distribution, {@code false} otherwise.
	 * <p>
	 * This method always returns {@code false}.
	 * 
	 * @return {@code true} if, and only if, this {@code OrenNayarBRDF} instance is using a Dirac distribution, {@code false} otherwise
	 */
	@Override
	public boolean isDiracDistribution() {
		return false;
	}
	
	/**
	 * Returns the probability density function (PDF) value of the solid angle for {@code o}, {@code n} and {@code i}.
	 * <p>
	 * If either {@code o}, {@code n} or {@code i} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * orenNayarBRDF.probabilityDensityFunctionSolidAngle(o, n, i, false);
	 * }
	 * </pre>
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param i a {@code Vector3F} instance with the incoming direction from the light source to the surface intersection point
	 * @return the probability density function (PDF) value of the solid angle for {@code o}, {@code n} and {@code i}
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
	@Override
	public float probabilityDensityFunctionSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i) {
		return probabilityDensityFunctionSolidAngle(o, n, i, false);
	}
	
	/**
	 * Returns the probability density function (PDF) value of the solid angle or the projected solid angle for {@code o}, {@code n} and {@code i}.
	 * <p>
	 * If either {@code o}, {@code n} or {@code i} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param i a {@code Vector3F} instance with the incoming direction from the light source to the surface intersection point
	 * @param isProjected {@code true} if, and only if, the projected solid angle should be used, {@code false} otherwise
	 * @return the probability density function (PDF) value of the solid angle for {@code o}, {@code n} and {@code i}
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
	@Override
	public float probabilityDensityFunctionSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i, final boolean isProjected) {
		final float nDotI = Vector3F.dotProduct(n, i);
		final float nDotO = Vector3F.dotProduct(n, o);
		
		final OrthonormalBasis33F orthonormalBasisLocal = new OrthonormalBasis33F(Vector3F.x(), Vector3F.y(), Vector3F.z());
		
		final Vector3F wI = Vector3F.normalize(Vector3F.transformReverse(Vector3F.negate(i), orthonormalBasisLocal));
		final Vector3F wO = Vector3F.normalize(Vector3F.transformReverse(o, orthonormalBasisLocal));
		
		final float cosThetaAbsWI = wI.cosThetaAbs();
		final float cosThetaAbsWO = wO.cosThetaAbs();
		
		final float sinThetaWI = wI.sinTheta();
		final float sinThetaWO = wO.sinTheta();
		
		final float maxCos = sinThetaWI > 1.0e-4F && sinThetaWO > 1.0e-4F ? max(0.0F, wI.cosPhi() * wO.cosPhi() + wI.sinPhi() * wO.sinPhi()) : 0.0F;
		
		final float sinA = cosThetaAbsWI > cosThetaAbsWO ? sinThetaWO : sinThetaWI;
		final float tanB = cosThetaAbsWI > cosThetaAbsWO ? sinThetaWI / cosThetaAbsWI : sinThetaWO / cosThetaAbsWO;
		
		final float a = this.a;
		final float b = this.b;
		final float c = (a + b * maxCos * sinA * tanB);
		
		if(nDotI > 0.0F && nDotO > 0.0F || nDotI < 0.0F && nDotO < 0.0F) {
			return 0.0F;
		} else if(isProjected) {
			return PI_RECIPROCAL * c;
		} else {
			return PI_RECIPROCAL * c * abs(nDotI);
		}
	}
	
	/**
	 * Returns a hash code for this {@code OrenNayarBRDF} instance.
	 * 
	 * @return a hash code for this {@code OrenNayarBRDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.angle, Float.valueOf(this.a), Float.valueOf(this.b));
	}
}