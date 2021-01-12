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
package org.dayflower.sampler;

import static org.dayflower.util.Ints.saturate;
import static org.dayflower.util.Ints.toInt;

import java.lang.reflect.Field;

//TODO: Add Javadocs!
public final class Distribution2F {
	private final Distribution1F marginal;
	private final Distribution1F[] conditional;
	private final boolean isUV;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Distribution2F(final float[][] functions) {
		this(functions, false);
	}
	
//	TODO: Add Javadocs!
	public Distribution2F(final float[][] functions, final boolean isUV) {
		this.conditional = new Distribution1F[functions.length];
		this.isUV = isUV;
		
		final float[] function = new float[functions.length];
		
		for(int i = 0; i < functions.length; i++) {
			final Distribution1F distribution = new Distribution1F(functions[i]);
			
			this.conditional[i] = distribution;
			
			function[i] = distribution.functionIntegral();
		}
		
		this.marginal = new Distribution1F(function);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Distribution1F getConditional(final int index) {
		return this.conditional[index];
	}
	
//	TODO: Add Javadocs!
	public Distribution1F getMarginal() {
		return this.marginal;
	}
	
//	TODO: Add Javadocs!
	public Sample2F continuousRemap(final Sample2F sample) {
		final float m = this.isUV ? sample.getU() : sample.getV();
		final float c = this.isUV ? sample.getV() : sample.getU();
		
		final int indexM = this.marginal.index(m);
		final int indexC = this.conditional[indexM].index(c);
		
		final float mRemapped = this.marginal.continuousRemap(m, indexM);
		final float cRemapped = this.conditional[indexM].continuousRemap(c, indexC);
		
		return new Sample2F(this.isUV ? mRemapped : cRemapped, this.isUV ? cRemapped : mRemapped);
	}
	
//	TODO: Add Javadocs!
	public Sample2F discreteRemap(final Sample2F sample) {
		final float m = this.isUV ? sample.getU() : sample.getV();
		final float c = this.isUV ? sample.getV() : sample.getU();
		
		final int indexM = this.marginal.index(m);
		final int indexC = this.conditional[indexM].index(c);
		
		final float mRemapped = this.marginal.discreteRemap(m, indexM);
		final float cRemapped = this.conditional[indexM].discreteRemap(c, indexC);
		
		return new Sample2F(this.isUV ? mRemapped : cRemapped, this.isUV ? cRemapped : mRemapped);
	}
	
//	TODO: Add Javadocs!
	public float continuousProbabilityDensityFunction(final Sample2F sample) {
		final float m = this.isUV ? sample.getU() : sample.getV();
		final float c = this.isUV ? sample.getV() : sample.getU();
		
		final int indexM = this.marginal.index(m);
		final int indexC = this.conditional[indexM].index(c);
		
		final float probabilityDensityFunctionValueM = this.marginal.continuousProbabilityDensityFunction(indexM);
		final float probabilityDensityFunctionValueC = this.conditional[indexM].continuousProbabilityDensityFunction(indexC);
		final float probabilityDensityFunctionValue = probabilityDensityFunctionValueM * probabilityDensityFunctionValueC;
		
		return probabilityDensityFunctionValue;
	}
	
//	TODO: Add Javadocs!
	public float discreteProbabilityDensityFunction(final Sample2F sample) {
		final float m = this.isUV ? sample.getU() : sample.getV();
		final float c = this.isUV ? sample.getV() : sample.getU();
		
		final int indexM = this.marginal.index(m);
		final int indexC = this.conditional[indexM].index(c);
		
		final float probabilityDensityFunctionValueM = this.marginal.discreteProbabilityDensityFunction(indexM);
		final float probabilityDensityFunctionValueC = this.conditional[indexM].discreteProbabilityDensityFunction(indexC);
		final float probabilityDensityFunctionValue = probabilityDensityFunctionValueM * probabilityDensityFunctionValueC;
		
		return probabilityDensityFunctionValue;
	}
	
//	TODO: Add Javadocs!
	public float probabilityDensityFunction(final Sample2F sample) {
		final float m = this.isUV ? sample.getU() : sample.getV();
		final float c = this.isUV ? sample.getV() : sample.getU();
		
		final int indexM = saturate(toInt(m * this.marginal.count()), 0, this.marginal.count() - 1);
		final int indexC = saturate(toInt(c * this.conditional[0].count()), 0, this.conditional[0].count() - 1);
		
		final float probabilityDensityFunctionValue = this.conditional[indexM].function(indexC) / this.marginal.functionIntegral();
		
		return probabilityDensityFunctionValue;
	}
}