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
package org.dayflower.scene.bssrdf;

import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_4_RECIPROCAL;
import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.exp;
import static org.dayflower.utility.Floats.log;
import static org.dayflower.utility.Floats.sqrt;

import org.dayflower.scene.fresnel.DielectricFresnel;

final class Utilities {
	private Utilities() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static float computeBeamDiffusionMS(final float sigmaS, final float sigmaA, final float g, final float eta, final float r) {
		final float sigmaPS = sigmaS * (1.0F - g);
		final float sigmaPT = sigmaA + sigmaPS;
		final float albedo = sigmaPS / sigmaPT;
		final float diffusionCoefficient = (2.0F * sigmaA + sigmaS) / (3.0F * sigmaPT * sigmaPT);
		final float sigmaTransport = sqrt(sigmaA / diffusionCoefficient);
		final float fresnelMoment1 = computeFresnelMoment1(eta);
		final float fresnelMoment2 = computeFresnelMoment2(eta);
		final float depthE = -2.0F * diffusionCoefficient * (1.0F + 3.0F * fresnelMoment2) / (1.0F - 2.0F * fresnelMoment1);
		final float cPhi = 0.25F * (1.0F - 2.0F * fresnelMoment1);
		final float cExitance = 0.5F * (1.0F - 3.0F * fresnelMoment2);
		
		final int samples = 100;
		
		float result = 0.0F;
		
		for(int i = 0; i < samples; i++) {
			final float depthR = -log(1.0F - (i + 0.5F) / samples) / sigmaPT;
			final float depthV = -depthR + 2.0F * depthE;
			final float dipoleR = sqrt(r * r + depthR * depthR);
			final float dipoleRCubed = dipoleR * dipoleR * dipoleR;
			final float dipoleV = sqrt(r * r + depthV * depthV);
			final float dipoleVCubed = dipoleV * dipoleV * dipoleV;
			final float dipoleFluenceRateR = exp(-sigmaTransport * dipoleR) / dipoleR;
			final float dipoleFluenceRateV = exp(-sigmaTransport * dipoleV) / dipoleV;
			final float dipoleFluenceRate = PI_MULTIPLIED_BY_4_RECIPROCAL / diffusionCoefficient * (dipoleFluenceRateR - dipoleFluenceRateV);
			final float dipoleVectorIrradianceR = depthR * (1.0F + sigmaTransport * dipoleR) * exp(-sigmaTransport * dipoleR) / dipoleRCubed;
			final float dipoleVectorIrradianceV = depthV * (1.0F + sigmaTransport * dipoleV) * exp(-sigmaTransport * dipoleV) / dipoleVCubed;
			final float dipoleVectorIrradiance = PI_MULTIPLIED_BY_4_RECIPROCAL * (dipoleVectorIrradianceR - dipoleVectorIrradianceV);
			
			result += (1.0F - exp(-2.0F * sigmaPT * (dipoleR + depthR))) * albedo * albedo * (dipoleFluenceRate * cPhi + dipoleVectorIrradiance * cExitance);
		}
		
		return result / samples;
	}
	
	public static float computeBeamDiffusionSS(final float sigmaS, final float sigmaA, final float g, final float eta, final float r) {
		final float sigmaT = sigmaA + sigmaS;
		final float albedo = sigmaS / sigmaT;
		final float tCriticalAngle = r * sqrt(eta * eta - 1.0F);
		
		final int samples = 100;
		
		float result = 0.0F;
		
		for(int i = 0; i < samples; i++) {
			final float tCurrent = tCriticalAngle - log(1.0F - (i + 0.5F) / samples) / sigmaT;
			final float depth = sqrt(r * r + tCurrent * tCurrent);
			final float cosThetaO = tCurrent / depth;
			
			result += albedo * exp(-sigmaT * (depth + tCriticalAngle)) / (depth * depth) * computePhaseHG(cosThetaO, g) * (1.0F - DielectricFresnel.evaluate(-cosThetaO, 1.0F, eta)) * abs(cosThetaO);
		}
		
		return result / samples;
	}
	
	public static float computeFresnelMoment1(final float eta) {
		final float eta1 = eta;
		final float eta2 = eta * eta1;
		final float eta3 = eta * eta2;
		final float eta4 = eta * eta3;
		final float eta5 = eta * eta4;
		
		if(eta < 1.0F) {
			return 0.45966F - 1.73965F * eta1 + 3.37668F * eta2 - 3.904945F * eta3 + 2.49277F * eta4 - 0.68441F * eta5;
		}
		
		return -4.61686F + 11.11360F * eta1 - 10.46460F * eta2 + 5.114550F * eta3 - 1.27198F * eta4 + 0.12746F * eta5;
	}
	
	public static float computeFresnelMoment2(final float eta) {
		final float eta1 = eta;
		final float eta2 = eta * eta1;
		final float eta3 = eta * eta2;
		final float eta4 = eta * eta3;
		final float eta5 = eta * eta4;
		
		if(eta < 1.0F) {
			return 0.27614F - 0.87350F * eta1 + 1.12077F * eta2 - 0.65095F * eta3 + 0.07883F * eta4 + 0.04860F * eta5;
		}
		
		final float etaReciprocal = 1.0F / eta;
		
		final float eta1Reciprocal = etaReciprocal;
		final float eta2Reciprocal = etaReciprocal * eta1Reciprocal;
		final float eta3Reciprocal = etaReciprocal * eta2Reciprocal;
		
		return -547.033F + 45.3087F * eta3Reciprocal - 218.725F * eta2Reciprocal + 458.843F * eta1Reciprocal + 404.557F * eta1 - 189.519F * eta2 + 54.9327F * eta3 - 9.00603F * eta4 + 0.63942F * eta5;
	}
	
	public static float computePhaseHG(final float cosTheta, final float g) {
		final float denominator = 1.0F + g * g + 2.0F * g * cosTheta;
		final float phaseHG = PI_MULTIPLIED_BY_4_RECIPROCAL * (1.0F - g * g) / (denominator * sqrt(denominator));
		
		return phaseHG;
	}
}