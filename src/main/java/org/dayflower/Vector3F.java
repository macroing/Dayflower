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
package org.dayflower;

import static org.dayflower.Floats.PI;
import static org.dayflower.Floats.PI_MULTIPLIED_BY_TWO;
import static org.dayflower.Floats.abs;
import static org.dayflower.Floats.cos;
import static org.dayflower.Floats.equal;
import static org.dayflower.Floats.max;
import static org.dayflower.Floats.pow;
import static org.dayflower.Floats.random;
import static org.dayflower.Floats.saturate;
import static org.dayflower.Floats.sin;
import static org.dayflower.Floats.sqrt;

import java.util.Objects;

public final class Vector3F {
	private final float element1;
	private final float element2;
	private final float element3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Vector3F() {
		this(0.0F, 0.0F, 0.0F);
	}
	
	public Vector3F(final Point3F p) {
		this(p.getElement1(), p.getElement2(), p.getElement3());
	}
	
	public Vector3F(final float element1, final float element2, final float element3) {
		this.element1 = element1;
		this.element2 = element2;
		this.element3 = element3;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public String toString() {
		return String.format("new Vector3F(%+.10f, %+.10f, %+.10f)", Float.valueOf(this.element1), Float.valueOf(this.element2), Float.valueOf(this.element3));
	}
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Vector3F)) {
			return false;
		} else if(!equal(this.element1, Vector3F.class.cast(object).element1)) {
			return false;
		} else if(!equal(this.element2, Vector3F.class.cast(object).element2)) {
			return false;
		} else if(!equal(this.element3, Vector3F.class.cast(object).element3)) {
			return false;
		} else {
			return true;
		}
	}
	
	public float cosPhi() {
		final float sinTheta = sinTheta();
		
		if(equal(sinTheta, 0.0F)) {
			return 1.0F;
		}
		
		return saturate(this.element1 / sinTheta, -1.0F, 1.0F);
	}
	
	public float cosPhiSquared() {
		return cosPhi() * cosPhi();
	}
	
	public float cosTheta() {
		return this.element3;
	}
	
	public float cosThetaAbs() {
		return abs(cosTheta());
	}
	
	public float cosThetaSquared() {
		return cosTheta() * cosTheta();
	}
	
	public float getElement1() {
		return this.element1;
	}
	
	public float getElement2() {
		return this.element2;
	}
	
	public float getElement3() {
		return this.element3;
	}
	
	public float getU() {
		return this.element1;
	}
	
	public float getV() {
		return this.element2;
	}
	
	public float getW() {
		return this.element3;
	}
	
	public float getX() {
		return this.element1;
	}
	
	public float getY() {
		return this.element2;
	}
	
	public float getZ() {
		return this.element3;
	}
	
	public float length() {
		return sqrt(lengthSquared());
	}
	
	public float lengthSquared() {
		return this.element1 * this.element1 + this.element2 * this.element2 + this.element3 * this.element3;
	}
	
	public float sinPhi() {
		final float sinTheta = sinTheta();
		
		if(equal(sinTheta, 0.0F)) {
			return 0.0F;
		}
		
		return saturate(this.element2 / sinTheta, -1.0F, 1.0F);
	}
	
	public float sinPhiSquared() {
		return sinPhi() * sinPhi();
	}
	
	public float sinTheta() {
		return sqrt(sinThetaSquared());
	}
	
	public float sinThetaSquared() {
		return max(0.0F, 1.0F - cosThetaSquared());
	}
	
	public float tanTheta() {
		return sinTheta() / cosTheta();
	}
	
	public float tanThetaSquared() {
		return sinThetaSquared() / cosThetaSquared();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.element1), Float.valueOf(this.element2), Float.valueOf(this.element3));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Vector3F add(final Vector3F vLHS, final Vector3F vRHS) {
		final float element1 = vLHS.element1 + vRHS.element1;
		final float element2 = vLHS.element2 + vRHS.element2;
		final float element3 = vLHS.element3 + vRHS.element3;
		
		return new Vector3F(element1, element2, element3);
	}
	
	public static Vector3F computeV(final Vector3F w) {
		final Vector3F wNormalized = normalize(w);
		
		final float absWNormalizedElement1 = abs(wNormalized.element1);
		final float absWNormalizedElement2 = abs(wNormalized.element2);
		final float absWNormalizedElement3 = abs(wNormalized.element3);
		
		if(absWNormalizedElement1 < absWNormalizedElement2 && absWNormalizedElement1 < absWNormalizedElement3) {
			return normalize(new Vector3F(0.0F, wNormalized.element3, -wNormalized.element2));
		}
		
		if(absWNormalizedElement2 < absWNormalizedElement3) {
			return normalize(new Vector3F(wNormalized.element3, 0.0F, -wNormalized.element1));
		}
		
		return normalize(new Vector3F(wNormalized.element2, -wNormalized.element1, 0.0F));
	}
	
	public static Vector3F crossProduct(final Vector3F vLHS, final Vector3F vRHS) {
		final float element1 = vLHS.element2 * vRHS.element3 - vLHS.element3 * vRHS.element2;
		final float element2 = vLHS.element3 * vRHS.element1 - vLHS.element1 * vRHS.element3;
		final float element3 = vLHS.element1 * vRHS.element2 - vLHS.element2 * vRHS.element1;
		
		return new Vector3F(element1, element2, element3);
	}
	
	public static Vector3F direction(final Point3F pEye, final Point3F pLookAt) {
		final float element1 = pLookAt.getElement1() - pEye.getElement1();
		final float element2 = pLookAt.getElement2() - pEye.getElement2();
		final float element3 = pLookAt.getElement3() - pEye.getElement3();
		
		return new Vector3F(element1, element2, element3);
	}
	
	public static Vector3F directionNormalized(final Point3F pEye, final Point3F pLookAt) {
		return normalize(direction(pEye, pLookAt));
	}
	
	public static Vector3F directionSpherical(final float u, final float v) {
		final float theta = u * PI_MULTIPLIED_BY_TWO;
		final float phi = v * PI;
		
		final float cosPhi = cos(phi);
		final float cosTheta = cos(theta);
		
		final float sinPhi = sin(phi);
		final float sinTheta = sin(theta);
		
		final float element1 = -sinPhi * cosTheta;
		final float element2 = cosPhi;
		final float element3 = sinPhi * sinTheta;
		
		return new Vector3F(element1, element2, element3);
	}
	
	public static Vector3F directionSphericalNormalized(final float u, final float v) {
		return normalize(directionSpherical(u, v));
	}
	
	public static Vector3F divide(final Vector3F vLHS, final float sRHS) {
		final float element1 = vLHS.element1 / sRHS;
		final float element2 = vLHS.element2 / sRHS;
		final float element3 = vLHS.element3 / sRHS;
		
		return new Vector3F(element1, element2, element3);
	}
	
	public static Vector3F faceForward(final Vector3F vLHS, final Vector3F vRHS) {
		return dotProduct(vLHS, vRHS) < 0.0F ? negate(vLHS) : vLHS;
	}
	
	public static Vector3F half(final Vector3F outgoing, final Vector3F normal, final Vector3F incoming) {
		return dotProduct(outgoing, incoming) > 0.999F ? normal : normalize(subtract(outgoing, incoming));
	}
	
	public static Vector3F multiply(final Vector3F vLHS, final float sRHS) {
		final float element1 = vLHS.element1 * sRHS;
		final float element2 = vLHS.element2 * sRHS;
		final float element3 = vLHS.element3 * sRHS;
		
		return new Vector3F(element1, element2, element3);
	}
	
	public static Vector3F negate(final Vector3F v) {
		final float element1 = -v.element1;
		final float element2 = -v.element2;
		final float element3 = -v.element3;
		
		return new Vector3F(element1, element2, element3);
	}
	
	public static Vector3F normal(final Point3F pA, final Point3F pB, final Point3F pC) {
		final Vector3F edgeAB = directionNormalized(pA, pB);
		final Vector3F edgeAC = directionNormalized(pA, pC);
		
		return crossProduct(edgeAB, edgeAC);
	}
	
	public static Vector3F normal(final Vector3F normalA, final Vector3F normalB, final Vector3F normalC, final Point3F barycentricCoordinates) {
		final float element1 = normalA.element1 * barycentricCoordinates.getU() + normalB.element1 * barycentricCoordinates.getV() + normalC.element1 * barycentricCoordinates.getW();
		final float element2 = normalA.element2 * barycentricCoordinates.getU() + normalB.element2 * barycentricCoordinates.getV() + normalC.element2 * barycentricCoordinates.getW();
		final float element3 = normalA.element3 * barycentricCoordinates.getU() + normalB.element3 * barycentricCoordinates.getV() + normalC.element3 * barycentricCoordinates.getW();
		
		return new Vector3F(element1, element2, element3);
	}
	
	public static Vector3F normalNormalized(final Point3F pA, final Point3F pB, final Point3F pC) {
		return normalize(normal(pA, pB, pC));
	}
	
	public static Vector3F normalNormalized(final Vector3F normalA, final Vector3F normalB, final Vector3F normalC, final Point3F barycentricCoordinates) {
		return normalize(normal(normalA, normalB, normalC, barycentricCoordinates));
	}
	
	public static Vector3F normalize(final Vector3F v) {
		return divide(v, v.length());
	}
	
	public static Vector3F reflection(final Vector3F direction, final Vector3F normal) {
		return reflection(direction, normal, false);
	}
	
	public static Vector3F reflection(final Vector3F direction, final Vector3F normal, final boolean isFacingSurface) {
		return isFacingSurface ? subtract(direction, multiply(normal, dotProduct(direction, normal) * 2.0F)) : subtract(multiply(normal, dotProduct(direction, normal) * 2.0F), direction);
	}
	
	public static Vector3F reflectionNormalized(final Vector3F direction, final Vector3F normal) {
		return reflectionNormalized(direction, normal, false);
	}
	
	public static Vector3F reflectionNormalized(final Vector3F direction, final Vector3F normal, final boolean isFacingSurface) {
		return normalize(reflection(direction, normal, isFacingSurface));
	}
	
	public static Vector3F sampleConeUniformDistribution(final float u, final float v, final float cosThetaMax) {
		final float cosTheta = u * (cosThetaMax - 1.0F) + 1.0F;
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float phi = PI_MULTIPLIED_BY_TWO * v;
		
		final float element1 = cos(phi) * sinTheta;
		final float element2 = sin(phi) * sinTheta;
		final float element3 = cosTheta;
		
		return new Vector3F(element1, element2, element3);
	}
	
	public static Vector3F sampleHemisphereCosineDistribution() {
		return sampleHemisphereCosineDistribution(random(), random());
	}
	
	public static Vector3F sampleHemisphereCosineDistribution(final float u, final float v) {
		final Point2F p = Point2F.sampleConcentricDisk(u, v);
		
		final float element1 = p.getElement1();
		final float element2 = p.getElement2();
		final float element3 = sqrt(max(0.0F, 1.0F - element1 * element1 - element2 * element2));
		
		return new Vector3F(element1, element2, element3);
	}
	
	public static Vector3F sampleHemispherePowerCosineDistribution() {
		return sampleHemispherePowerCosineDistribution(random(), random());
	}
	
	public static Vector3F sampleHemispherePowerCosineDistribution(final float u, final float v) {
		return sampleHemispherePowerCosineDistribution(u, v, 20.0F);
	}
	
	public static Vector3F sampleHemispherePowerCosineDistribution(final float u, final float v, final float exponent) {
		final float cosTheta = pow(1.0F - u, 1.0F / (exponent + 1.0F));
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float phi = PI_MULTIPLIED_BY_TWO * v;
		
		final float element1 = cos(phi) * sinTheta;
		final float element2 = sin(phi) * sinTheta;
		final float element3 = cosTheta;
		
		return new Vector3F(element1, element2, element3);
	}
	
	public static Vector3F sampleHemisphereUniformDistribution() {
		return sampleHemisphereUniformDistribution(random(), random());
	}
	
	public static Vector3F sampleHemisphereUniformDistribution(final float u, final float v) {
		final float cosTheta = u;
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float phi = PI_MULTIPLIED_BY_TWO * v;
		
		final float element1 = cos(phi) * sinTheta;
		final float element2 = sin(phi) * sinTheta;
		final float element3 = cosTheta;
		
		return new Vector3F(element1, element2, element3);
	}
	
	public static Vector3F sampleSphereUniformDistribution() {
		return sampleSphereUniformDistribution(random(), random());
	}
	
	public static Vector3F sampleSphereUniformDistribution(final float u, final float v) {
		final float cosTheta = 1.0F - 2.0F * u;
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float phi = PI_MULTIPLIED_BY_TWO * v;
		
		final float element1 = cos(phi) * sinTheta;
		final float element2 = sin(phi) * sinTheta;
		final float element3 = cosTheta;
		
		return new Vector3F(element1, element2, element3);
	}
	
	public static Vector3F subtract(final Vector3F vLHS, final Vector3F vRHS) {
		final float element1 = vLHS.element1 - vRHS.element1;
		final float element2 = vLHS.element2 - vRHS.element2;
		final float element3 = vLHS.element3 - vRHS.element3;
		
		return new Vector3F(element1, element2, element3);
	}
	
	public static Vector3F transform(final Matrix44F mLHS, final Vector3F vRHS) {
		final float element1 = mLHS.getElement11() * vRHS.element1 + mLHS.getElement12() * vRHS.element2 + mLHS.getElement13() * vRHS.element3;
		final float element2 = mLHS.getElement21() * vRHS.element1 + mLHS.getElement22() * vRHS.element2 + mLHS.getElement23() * vRHS.element3;
		final float element3 = mLHS.getElement31() * vRHS.element1 + mLHS.getElement32() * vRHS.element2 + mLHS.getElement33() * vRHS.element3;
		
		return new Vector3F(element1, element2, element3);
	}
	
	public static Vector3F transform(final Vector3F vLHS, final OrthoNormalBasis33F oRHS) {
		final float element1 = vLHS.element1 * oRHS.getU().element1 + vLHS.element2 * oRHS.getV().element1 + vLHS.element3 * oRHS.getW().element1;
		final float element2 = vLHS.element1 * oRHS.getU().element2 + vLHS.element2 * oRHS.getV().element2 + vLHS.element3 * oRHS.getW().element2;
		final float element3 = vLHS.element1 * oRHS.getU().element3 + vLHS.element2 * oRHS.getV().element3 + vLHS.element3 * oRHS.getW().element3;
		
		return new Vector3F(element1, element2, element3);
	}
	
	public static Vector3F transformReverse(final Vector3F vLHS, final OrthoNormalBasis33F oRHS) {
		final float element1 = dotProduct(vLHS, oRHS.getU());
		final float element2 = dotProduct(vLHS, oRHS.getV());
		final float element3 = dotProduct(vLHS, oRHS.getW());
		
		return new Vector3F(element1, element2, element3);
	}
	
	public static Vector3F transformTranspose(final Matrix44F mLHS, final Vector3F vRHS) {
		final float element1 = mLHS.getElement11() * vRHS.element1 + mLHS.getElement21() * vRHS.element2 + mLHS.getElement31() * vRHS.element3;
		final float element2 = mLHS.getElement12() * vRHS.element1 + mLHS.getElement22() * vRHS.element2 + mLHS.getElement32() * vRHS.element3;
		final float element3 = mLHS.getElement13() * vRHS.element1 + mLHS.getElement23() * vRHS.element2 + mLHS.getElement33() * vRHS.element3;
		
		return new Vector3F(element1, element2, element3);
	}
	
	public static Vector3F u() {
		return u(1.0F);
	}
	
	public static Vector3F u(final float u) {
		return new Vector3F(u, 0.0F, 0.0F);
	}
	
	public static Vector3F v() {
		return v(1.0F);
	}
	
	public static Vector3F v(final float v) {
		return new Vector3F(0.0F, v, 0.0F);
	}
	
	public static Vector3F w() {
		return w(1.0F);
	}
	
	public static Vector3F w(final float w) {
		return new Vector3F(0.0F, 0.0F, w);
	}
	
	public static Vector3F x() {
		return x(1.0F);
	}
	
	public static Vector3F x(final float x) {
		return new Vector3F(x, 0.0F, 0.0F);
	}
	
	public static Vector3F y() {
		return y(1.0F);
	}
	
	public static Vector3F y(final float y) {
		return new Vector3F(0.0F, y, 0.0F);
	}
	
	public static Vector3F z() {
		return z(1.0F);
	}
	
	public static Vector3F z(final float z) {
		return new Vector3F(0.0F, 0.0F, z);
	}
	
	public static float dotProduct(final Vector3F vLHS, final Vector3F vRHS) {
		return vLHS.element1 * vRHS.element1 + vLHS.element2 * vRHS.element2 + vLHS.element3 * vRHS.element3;
	}
}