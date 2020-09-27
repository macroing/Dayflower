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

import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.acos;
import static org.dayflower.util.Floats.atan;
import static org.dayflower.util.Floats.cos;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.error;
import static org.dayflower.util.Floats.errorReciprocal;
import static org.dayflower.util.Floats.exp;
import static org.dayflower.util.Floats.isInfinite;
import static org.dayflower.util.Floats.log;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.pow;
import static org.dayflower.util.Floats.sin;
import static org.dayflower.util.Floats.sqrt;
import static org.dayflower.util.Floats.tan;

import java.lang.reflect.Field;

import org.dayflower.geometry.Vector2F;
import org.dayflower.geometry.Vector3F;

//TODO: Add Javadocs!
public final class BeckmannDistribution extends MicrofacetDistribution {
	private final float alphaX;
	private final float alphaY;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public BeckmannDistribution(final boolean isSamplingVisibleArea, final float alphaX, final float alphaY) {
		super(isSamplingVisibleArea);
		
		this.alphaX = max(alphaX, 0.001F);
		this.alphaY = max(alphaY, 0.001F);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public Vector3F sampleN(final Vector3F o, final float u, final float v) {
		final float alphaX = this.alphaX;
		final float alphaY = this.alphaY;
		
		if(isSamplingVisibleArea()) {
			if(o.getZ() >= 0.0F) {
				return doSample(o, alphaX, alphaY, u, v);
			}
			
			return Vector3F.negate(doSample(Vector3F.negate(o), alphaX, alphaY, u, v));
		} else if(equal(alphaX, alphaY)) {
			final float logSample = log(1.0F - u);
			final float phi = v * 2.0F * PI;
			final float tanThetaSquared = -alphaX * alphaX * logSample;
			final float cosTheta = 1.0F / sqrt(1.0F + tanThetaSquared);
			final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
			
			final Vector3F n = Vector3F.directionSpherical(sinTheta, cosTheta, phi);
			final Vector3F nCorrectlyOriented = o.getZ() * n.getZ() > 0.0F ? n : Vector3F.negate(n);
			
			return nCorrectlyOriented;
		} else {
			final float logSample = log(1.0F - u);
			final float phi = atan(alphaY / alphaX * tan(2.0F * PI * v + 0.5F * PI)) + (v > 0.5F ? PI : 0.0F);
			final float sinPhi = sin(phi);
			final float cosPhi = cos(phi);
			final float alphaXSquared = alphaX * alphaX;
			final float alphaYSquared = alphaY * alphaY;
			final float tanThetaSquared = -logSample / (cosPhi * cosPhi / alphaXSquared + sinPhi * sinPhi / alphaYSquared);
			final float cosTheta = 1.0F / sqrt(1.0F + tanThetaSquared);
			final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
			
			final Vector3F n = Vector3F.directionSpherical(sinTheta, cosTheta, phi);
			final Vector3F nCorrectlyOriented = o.getZ() * n.getZ() > 0.0F ? n : Vector3F.negate(n);
			
			return nCorrectlyOriented;
		}
	}
	
//	TODO: Add Javadocs!
	@Override
	public float computeDifferentialArea(final Vector3F n) {
		final float tanThetaSquared = n.tanThetaSquared();
		
		if(isInfinite(tanThetaSquared)) {
			return 0.0F;
		}
		
		final float alphaX = this.alphaX;
		final float alphaXSquared = alphaX * alphaX;
		final float alphaY = this.alphaY;
		final float alphaYSquared = alphaY * alphaY;
		
		final float cosPhiSquared = n.cosPhiSquared();
		final float sinPhiSquared = n.sinPhiSquared();
		
		final float cosThetaQuartic = n.cosThetaQuartic();
		
		final float differentialArea = exp(-tanThetaSquared * (cosPhiSquared / alphaXSquared + sinPhiSquared / alphaYSquared)) / (PI * alphaX * alphaY * cosThetaQuartic);
		
		return differentialArea;
	}
	
//	TODO: Add Javadocs!
	@Override
	public float computeLambda(final Vector3F o) {
		final float tanThetaAbs = o.tanThetaAbs();
		
		if(isInfinite(tanThetaAbs)) {
			return 0.0F;
		}
		
		final float alphaX = this.alphaX;
		final float alphaXSquared = alphaX * alphaX;
		final float alphaY = this.alphaY;
		final float alphaYSquared = alphaY * alphaY;
		
		final float cosPhiSquared = o.cosPhiSquared();
		final float sinPhiSquared = o.sinPhiSquared();
		
		final float alpha = sqrt(cosPhiSquared * alphaXSquared + sinPhiSquared * alphaYSquared);
		final float alphaReciprocal = 1.0F / (alpha * tanThetaAbs);
		
		if(alphaReciprocal >= 1.6F) {
			return 0.0F;
		}
		
		final float lambda = (1.0F - 1.259F * alphaReciprocal + 0.396F * alphaReciprocal * alphaReciprocal) / (3.535F * alphaReciprocal + 2.181F * alphaReciprocal * alphaReciprocal);
		
		return lambda;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Vector2F doComputeSlope(final float cosThetaI, final float u, final float v) {
		if(cosThetaI > 0.9999F) {
			final float r = sqrt(-log(1.0F - u));
			final float cosPhi = cos(2.0F * PI * v);
			final float sinPhi = sin(2.0F * PI * v);
			final float x = r * cosPhi;
			final float y = r * sinPhi;
			
			return new Vector2F(x, y);
		}
		
		final float sinThetaI = sqrt(max(0.0F, 1.0F - cosThetaI * cosThetaI));
		final float tanThetaI = sinThetaI / cosThetaI;
		final float cotThetaI = 1.0F / tanThetaI;
		
		final float sampleX = max(u, 1.0e-6F);
		final float thetaI = acos(cosThetaI);
		final float fit = 1.0F + thetaI * (-0.876F + thetaI * (0.4265F - 0.0594F * thetaI));
		final float sqrtPiReciprocal = 1.0F / sqrt(PI);
		
		float a = -1.0F;
		float b = error(cotThetaI);
		float c = b - (1.0F + b) * pow(1.0F - sampleX, fit);
		
		final float normalization = 1.0F / (1.0F + c + sqrtPiReciprocal * tanThetaI * exp(-cotThetaI * cotThetaI));
		
		for(int i = 1; i < 10; i++) {
			if(!(c >= a && c <= b)) {
				c = 0.5F * (a + b);
			}
			
			final float errorReciprocal = errorReciprocal(c);
			final float value = normalization * (1.0F + b + sqrtPiReciprocal * tanThetaI * exp(-errorReciprocal * errorReciprocal)) - sampleX;
			final float derivative = normalization * (1.0F - errorReciprocal * tanThetaI);
			
			if(abs(value) < 1.0e-5F) {
				break;
			}
			
			if(value > 0.0F) {
				b = c;
			} else {
				a = c;
			}
			
			b -= value / derivative;
		}
		
		final float x = errorReciprocal(c);
		final float y = errorReciprocal(2.0F * max(v, 1.0e-6F) - 1.0F);
		
		return new Vector2F(x, y);
	}
	
	private static Vector3F doSample(final Vector3F i, final float alphaX, final float alphaY, final float u, final float v) {
		final Vector3F iStretched = Vector3F.normalize(new Vector3F(i.getX() * alphaX, i.getY() * alphaY, i.getZ()));
		
		final Vector2F slope = doComputeSlope(iStretched.cosTheta(), u, v);
		
		final float x = -(iStretched.cosPhi() * slope.getX() - iStretched.sinPhi() * slope.getY());
		final float y = -(iStretched.sinPhi() * slope.getX() + iStretched.cosPhi() * slope.getY());
		final float z = 1.0F;
		
		return Vector3F.normalize(new Vector3F(x, y, z));
	}
}