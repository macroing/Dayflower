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
package org.dayflower.interpolation;

import java.lang.reflect.Field;//TODO: Add Javadocs!

import org.dayflower.utility.Floats;
import org.dayflower.utility.Ints;

//TODO: Add Javadocs!
public final class Interpolation {
	private Interpolation() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static boolean catmullRomWeights(final int size, final float[] nodes, final float x, final int[] offset, final float[] weights) {
		if(!(x >= nodes[0] && x <= nodes[size - 1])) {
			return false;
		}
		
		final int index = Ints.findInterval(size, i -> nodes[i] <= x);
		
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
	
//	TODO: Add Javadocs!
	public static float catmullRom(final int size, final float[] nodes, final float[] values, final float x) {
		if(!(x >= nodes[0] && x <= nodes[size - 1])) {
			return 0.0F;
		}
		
		final int index = Ints.findInterval(size, i -> nodes[i] <= x);
		
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
	
//	TODO: Add Javadocs!
	public static float fourier(final float[] a, final int m, final double cosPhi, final int aOffset) {
		double value = 0.0D;
		double cosKMinusOnePhi = cosPhi;
		double cosKPhi = 1.0D;
		
		for(int k = 0; k < m; k++) {
			value += a[aOffset + k] * cosKPhi;
			
			final double cosKPlusOnePhi = 2.0D * cosPhi * cosKPhi - cosKMinusOnePhi;
			
			cosKMinusOnePhi = cosKPhi;
			cosKPhi = cosKPlusOnePhi;
		}
		
		return (float)(value);
	}
	
//	TODO: Add Javadocs!
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
	
//	TODO: Add Javadocs!
	public static float interpolate(final float[] array, final int index, final float[] weights, final int offset, final int size) {
		float value = 0.0F;
		
		for(int i = 0; i < 4; i++) {
			if(weights[i] != 0.0F) {
				value += array[(offset + i) * size + index] * weights[i];
			}
		}
		
		return value;
	}
	
//	TODO: Add Javadocs!
	public static float invertCatmullRom(final int n, final float[] x, final float[] values, final float u) {
		if(!(u > values[0])) {
			return x[0];
		} else if(!(u < values[n - 1])) {
			return x[n - 1];
		}
		
		final int index = Ints.findInterval(n, i -> values[i] <= u);
		
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
	
//	TODO: Add Javadocs!
	public static float sampleCatmullRom2D(final int size1, final int size2, final float[] nodes1, final float[] nodes2, final float[] values, final float[] cDF, final float alpha, final float u, final float[] fVal, final float[] pDF) {
		final int[] offset = new int[1];
		
		final float[] weights = new float[4];
		
		if(!catmullRomWeights(size1, nodes1, alpha, offset, weights)) {
			return 0.0F;
		}
		
		final float maximum = interpolate(cDF, size2 - 1, weights, offset[0], size2);
		
		float v = u * maximum;
		
		final float w = v;
		
		final int index = Ints.findInterval(size2, i -> interpolate(cDF, i, weights, offset[0], size2) <= w);
		
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
		
		if(fVal != null && fVal.length > 0) {
			fVal[0] = fHat2;
		}
		
		pDF[0] = fHat2 / maximum;
		
		return x0 + width * t;
	}
	
//	TODO: Add Javadocs!
	public static float sampleFourier(final float[] ak, final float[] recip, final int m, final float u, final float[] pdf, final float[] phiPtr) {
		float v = u;
		
		final boolean flip = v >= 0.5F;
		
		if(flip) {
			v = 1.0F - 2.0F * (v - 0.5F);
		} else {
			v *= 2.0F;
		}
		
		double a = 0.0D;
		double b = Math.PI;
		double phi = 0.5D * Math.PI;
		double f0;
		double f1;
		
		while(true) {
			final double cosPhi = Math.cos(phi);
			final double sinPhi = Math.sqrt(Math.max(0.0D, 1.0D - cosPhi * cosPhi));
			
			double cosPhiPrev = cosPhi;
			double cosPhiCur = 1.0D;
			double sinPhiPrev = -sinPhi;
			double sinPhiCur = 0.0D;
			
			f0 = ak[0] * phi;
			f1 = ak[0];
			
			for(int k = 1; k < m; k++) {
				final double sinPhiNext = 2.0D * cosPhi * sinPhiCur - sinPhiPrev;
				final double cosPhiNext = 2.0D * cosPhi * cosPhiCur - cosPhiPrev;
				
				sinPhiPrev = sinPhiCur;
				sinPhiCur = sinPhiNext;
				
				cosPhiPrev = cosPhiCur;
				cosPhiCur = cosPhiNext;
				
				f0 += ak[k] * recip[k] * sinPhiNext;
				f1 += ak[k] * cosPhiNext;
			}
			
			f0 -= v * ak[0] * Math.PI;
			
			if(f0 > 0.0D) {
				b = phi;
			} else {
				a = phi;
			}
			
			if(Math.abs(f0) < 1.0e-6D || b - a < 1.0e-6D) {
				break;
			}
			
			phi -= f0 / f1;
			
			if(!(phi > a && phi < b)) {
				phi = 0.5D * (a + b);
			}
		}
		
		if(flip) {
			phi = 2.0D * Math.PI - phi;
		}
		
		pdf[0] = (float)((1.0D / (Math.PI * 2.0D)) * f1 / ak[0]);
		
		phiPtr[0] = (float)(phi);
		
		return (float)(f1);
	}
}