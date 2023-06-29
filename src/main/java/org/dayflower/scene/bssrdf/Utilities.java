/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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

import java.util.function.IntPredicate;

import org.dayflower.scene.fresnel.DielectricFresnel;

import org.macroing.java.lang.Floats;
import org.macroing.java.lang.Ints;

final class Utilities {
	private Utilities() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static boolean catmullRomWeights(final int size, final float[] nodes, final float x, final int[] offset, final float[] weights) {
		if(!(x >= nodes[0] && x <= nodes[size - 1])) {
			return false;
		}
		
		final int index = findInterval(size, i -> nodes[i] <= x);
		
		offset[0] = index - 1;
		
		final float x0 = nodes[index + 0];
		final float x1 = nodes[index + 1];
		
		final float t1 = (x - x0) / (x1 - x0);
		final float t2 = t1 * t1;
		final float t3 = t2 * t1;
		
		weights[1] = +2.0F * t3 - 3.0F * t2 + 1.0F;
		weights[2] = -2.0F * t3 + 3.0F * t2;
		
		if(index > 0) {
			final float w0 = (t3 - 2.0F * t2 + t1) * (x1 - x0) / (x1 - nodes[index - 1]);
			
			weights[0] = -w0;
			weights[2] += w0;
		} else {
			final float w0 = t3 - 2.0F * t2 + t1;
			
			weights[0] = 0.0F;
			weights[1] -= w0;
			weights[2] += w0;
		}
		
		if(index + 2 < size) {
			final float w3 = (t3 - t2) * (x1 - x0) / (nodes[index + 2] - x0);
			
			weights[1] -= w3;
			weights[3] = w3;
		} else {
			final float w3 = t3 - t2;
			
			weights[1] -= w3;
			weights[2] += w3;
			weights[3] = 0.0F;
		}
		
		return true;
	}
	
	public static float catmullRom(final int size, final float[] nodes, final float[] values, final float x) {
		if(!(x >= nodes[0] && x <= nodes[size - 1])) {
			return 0.0F;
		}
		
		final int index = findInterval(size, i -> nodes[i] <= x);
		
		final float x0 = nodes[index + 0];
		final float x1 = nodes[index + 1];
		
		final float f0 = values[index + 0];
		final float f1 = values[index + 1];
		
		final float width = x1 - x0;
		
		float d0;
		float d1;
		
		if(index > 0) {
			d0 = width * (f1 - values[index - 1]) / (x1 - nodes[index - 1]);
		} else {
			d0 = f1 - f0;
		}
		
		if(index + 2 < size) {
			d1 = width * (values[index + 2] - f0) / (nodes[index + 2] - x0);
		} else {
			d1 = f1 - f0;
		}
		
		final float t1 = (x - x0) / (x1 - x0);
		final float t2 = t1 * t1;
		final float t3 = t2 * t1;
		
		return (2.0F * t3 - 3.0F * t2 + 1.0F) * f0 + (-2.0F * t3 + 3.0F * t2) * f1 + (t3 - 2.0F * t2 + t1) * d0 + (t3 - t2) * d1;
	}
	
	public static float computeBeamDiffusionMS(final float sigmaS, final float sigmaA, final float g, final float eta, final float r) {
		final float sigmaPS = sigmaS * (1.0F - g);
		final float sigmaPT = sigmaA + sigmaPS;
		final float albedo = sigmaPS / sigmaPT;
		final float diffusionCoefficient = (2.0F * sigmaA + sigmaS) / (3.0F * sigmaPT * sigmaPT);
		final float sigmaTransport = Floats.sqrt(sigmaA / diffusionCoefficient);
		final float fresnelMoment1 = computeFresnelMoment1(eta);
		final float fresnelMoment2 = computeFresnelMoment2(eta);
		final float depthE = -2.0F * diffusionCoefficient * (1.0F + 3.0F * fresnelMoment2) / (1.0F - 2.0F * fresnelMoment1);
		final float cPhi = 0.25F * (1.0F - 2.0F * fresnelMoment1);
		final float cExitance = 0.5F * (1.0F - 3.0F * fresnelMoment2);
		
		final int samples = 100;
		
		float result = 0.0F;
		
		for(int i = 0; i < samples; i++) {
			final float depthR = -Floats.log(1.0F - (i + 0.5F) / samples) / sigmaPT;
			final float depthV = -depthR + 2.0F * depthE;
			final float dipoleR = Floats.sqrt(r * r + depthR * depthR);
			final float dipoleRCubed = dipoleR * dipoleR * dipoleR;
			final float dipoleV = Floats.sqrt(r * r + depthV * depthV);
			final float dipoleVCubed = dipoleV * dipoleV * dipoleV;
			final float dipoleFluenceRateR = Floats.exp(-sigmaTransport * dipoleR) / dipoleR;
			final float dipoleFluenceRateV = Floats.exp(-sigmaTransport * dipoleV) / dipoleV;
			final float dipoleFluenceRate = Floats.PI_MULTIPLIED_BY_4_RECIPROCAL / diffusionCoefficient * (dipoleFluenceRateR - dipoleFluenceRateV);
			final float dipoleVectorIrradianceR = depthR * (1.0F + sigmaTransport * dipoleR) * Floats.exp(-sigmaTransport * dipoleR) / dipoleRCubed;
			final float dipoleVectorIrradianceV = depthV * (1.0F + sigmaTransport * dipoleV) * Floats.exp(-sigmaTransport * dipoleV) / dipoleVCubed;
			final float dipoleVectorIrradiance = Floats.PI_MULTIPLIED_BY_4_RECIPROCAL * (dipoleVectorIrradianceR - dipoleVectorIrradianceV);
			
			result += (1.0F - Floats.exp(-2.0F * sigmaPT * (dipoleR + depthR))) * albedo * albedo * (dipoleFluenceRate * cPhi + dipoleVectorIrradiance * cExitance);
		}
		
		return result / samples;
	}
	
	public static float computeBeamDiffusionSS(final float sigmaS, final float sigmaA, final float g, final float eta, final float r) {
		final float sigmaT = sigmaA + sigmaS;
		final float albedo = sigmaS / sigmaT;
		final float tCriticalAngle = r * Floats.sqrt(eta * eta - 1.0F);
		
		final int samples = 100;
		
		float result = 0.0F;
		
		for(int i = 0; i < samples; i++) {
			final float tCurrent = tCriticalAngle - Floats.log(1.0F - (i + 0.5F) / samples) / sigmaT;
			final float depth = Floats.sqrt(r * r + tCurrent * tCurrent);
			final float cosThetaO = tCurrent / depth;
			
			result += albedo * Floats.exp(-sigmaT * (depth + tCriticalAngle)) / (depth * depth) * computePhaseHG(cosThetaO, g) * (1.0F - DielectricFresnel.evaluate(-cosThetaO, 1.0F, eta)) * Floats.abs(cosThetaO);
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
		final float phaseHG = Floats.PI_MULTIPLIED_BY_4_RECIPROCAL * (1.0F - g * g) / (denominator * Floats.sqrt(denominator));
		
		return phaseHG;
	}
	
	public static float integrateCatmullRom(final int n, final float[] x, final float[] values, final float[] cDF, final int valuesOffset, final int cDFOffset) {
		float sum = 0.0F;
		
		cDF[cDFOffset] = 0.0F;
		
		for(int i = 0; i < n - 1; i++) {
			final float x0 = x[i + 0];
			final float x1 = x[i + 1];
			
			final float f0 = values[valuesOffset + i + 0];
			final float f1 = values[valuesOffset + i + 1];
			
			final float width = x1 - x0;
			
			float d0;
			float d1;
			
			if(i > 0) {
				d0 = width * (f1 - values[valuesOffset + i - 1]) / (x1 - x[i - 1]);
			} else {
				d0 = f1 - f0;
			}
			
			if(i + 2 < n) {
				d1 = width * (values[valuesOffset + i + 2] - f0) / (x[i + 2] - x0);
			} else {
				d1 = f1 - f0;
			}
			
			sum += ((d0 - d1) * (1.0F / 12.0F) + (f0 + f1) * 0.5F) * width;
			
			cDF[cDFOffset + i + 1] = sum;
		}
		
		return sum;
	}
	
	public static float interpolate(final float[] array, final int index, final float[] weights, final int offset, final int size) {
		float value = 0.0F;
		
		for(int i = 0; i < 4; i++) {
			if(weights[i] != 0.0F) {
				value += array[(offset + i) * size + index] * weights[i];
			}
		}
		
		return value;
	}
	
	public static float invertCatmullRom(final int n, final float[] x, final float[] values, final float u) {
		if(!(u > values[0])) {
			return x[0];
		} else if(!(u < values[n - 1])) {
			return x[n - 1];
		}
		
		final int index = findInterval(n, i -> values[i] <= u);
		
		final float x0 = x[index + 0];
		final float x1 = x[index + 1];
		
		final float f0 = values[index + 0];
		final float f1 = values[index + 1];
		
		final float width = x1 - x0;
		
		float d0;
		float d1;
		
		if(index > 0) {
			d0 = width * (f1 - values[index - 1]) / (x1 - x[index - 1]);
		} else {
			d0 = f1 - f0;
		}
		
		if(index + 2 < n) {
			d1 = width * (values[index + 2] - f0) / (x[index + 2] - x0);
		} else {
			d1 = f1 - f0;
		}
		
		float a = 0.0F;
		float b = 1.0F;
		
		float t1 = 0.5F;
		
		while(true) {
			if(!(t1 > a && t1 < b)) {
				t1 = 0.5F * (a + b);
			}
			
			final float t2 = t1 * t1;
			final float t3 = t2 * t1;
			
			final float fHat1 = (2.0F * t3 - 3.0F * t2 + 1.0F) * f0 + (-2.0F * t3 + 3.0F * t2) * f1 + (t3 - 2.0F * t2 + t1) * d0 + (t3 - t2) * d1;
			final float fHat2 = (6.0F * t2 - 6.0F * t1) * f0 + (-6.0F * t2 + 6.0F * t1) * f1 + (3.0F * t2 - 4.0F * t1 + 1.0F) * d0 + (3.0F * t2 - 2.0F * t1) * d1;
			
			if(Floats.abs(fHat1 - u) < 1.0e-6F || b - a < 1.0e-6F) {
				break;
			}
			
			if(fHat1 - u < 0.0F) {
				a = t1;
			} else {
				b = t1;
			}
			
			t1 -= (fHat1 - u) / fHat2;
		}
		
		return x0 + t1 * width;
	}
	
	public static float sampleCatmullRom2D(final int size1, final int size2, final float[] nodes1, final float[] nodes2, final float[] values, final float[] cDF, final float alpha, final float u, final float[] fVal, final float[] pDF) {
		final int[] offset = new int[1];
		
		final float[] weights = new float[4];
		
		if(!catmullRomWeights(size1, nodes1, alpha, offset, weights)) {
			return 0.0F;
		}
		
		final float maximum = interpolate(cDF, size2 - 1, weights, offset[0], size2);
		
		float v = u * maximum;
		
		final float w = v;
		
		final int index = findInterval(size2, i -> interpolate(cDF, i, weights, offset[0], size2) <= w);
		
		final float f0 = interpolate(values, index + 0, weights, offset[0], size2);
		final float f1 = interpolate(values, index + 1, weights, offset[0], size2);
		
		final float x0 = nodes2[index + 0];
		final float x1 = nodes2[index + 1];
		
		final float width = x1 - x0;
		
		float d0;
		float d1;
		
		v = (v - interpolate(cDF, index, weights, offset[0], size2)) / width;
		
		if(index > 0) {
			d0 = width * (f1 - interpolate(values, index - 1, weights, offset[0], size2)) / (x1 - nodes2[index - 1]);
		} else {
			d0 = f1 - f0;
		}
		
		if(index + 2 < size2) {
			d1 = width * (interpolate(values, index + 2, weights, offset[0], size2) - f0) / (nodes2[index + 2] - x0);
		} else {
			d1 = f1 - f0;
		}
		
		float t;
		
		if(f0 != f1) {
			t = (f0 - Floats.sqrt(Floats.max(0.0F, f0 * f0 + 2.0F * v * (f1 - f0)))) / (f0 - f1);
		} else {
			t = v / f0;
		}
		
		float a = 0.0F;
		float b = 1.0F;
		
		float fHat1;
		float fHat2;
		
		while(true) {
			if(!(t >= a && t <= b)) {
				t = 0.5F * (a + b);
			}
			
			fHat1 = t * (f0 + t * (0.5F * d0 + t * ((1.0F / 3.0F) * (-2.0F * d0 - d1) + f1 - f0 + t * (0.25F * (d0 + d1) + 0.5F * (f0 - f1)))));
			fHat2 = f0 + t * (d0 + t * (-2.0F * d0 - d1 + 3.0F * (f1 - f0) + t * (d0 + d1 + 2.0F * (f0 - f1))));
			
			if(Floats.abs(fHat1 - v) < 1.0e-6F || b - a < 1.0e-6F) {
				break;
			}
			
			if(fHat1 - v < 0.0F) {
				a = t;
			} else {
				b = t;
			}
			
			t -= (fHat1 - v) / fHat2;
		}
		
		fVal[0] = fHat2;
		
		pDF[0] = fHat2 / maximum;
		
		return x0 + width * t;
	}
	
	public static int findInterval(final int size, final IntPredicate predicate) {
		int first = 0;
		int length = size;
		
		while(length > 0) {
			final int half = length >> 1;
			final int middle = first + half;
			
			if(predicate.test(middle)) {
				first = middle + 1;
				length -= half + 1;
			} else {
				length = half;
			}
		}
		
		return Ints.saturate(first - 1, 0, size - 2);
	}
}