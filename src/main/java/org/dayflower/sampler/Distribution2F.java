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
package org.dayflower.sampler;

import static org.dayflower.util.Ints.saturate;
import static org.dayflower.util.Ints.toInt;

import java.lang.reflect.Field;

//TODO: Add Javadocs!
public final class Distribution2F {
	private final Distribution1F marginal;
	private final Distribution1F[] conditional;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Distribution2F(final float[][] functions) {
		this.conditional = new Distribution1F[functions.length];
		
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
	public Sample2F continuousRemap(final Sample2F sample) {
		final float u = sample.getU();
		final float v = sample.getV();
		
		final int indexV = this.marginal.index(v);
		final int indexU = this.conditional[indexV].index(u);
		
		final float uRemapped = this.conditional[indexV].continuousRemap(u, indexU);
		final float vRemapped = this.marginal.continuousRemap(v, indexV);
		
		return new Sample2F(uRemapped, vRemapped);
	}
	
//	TODO: Add Javadocs!
	public float continuousProbabilityDensityFunction(final Sample2F sample) {
		final float u = sample.getU();
		final float v = sample.getV();
		
		final int indexV = this.marginal.index(v);
		final int indexU = this.conditional[indexV].index(u);
		
		final float probabilityDensityFunctionValueU = this.conditional[indexV].continuousProbabilityDensityFunction(indexU);
		final float probabilityDensityFunctionValueV = this.marginal.continuousProbabilityDensityFunction(indexV);
		final float probabilityDensityFunctionValue = probabilityDensityFunctionValueU * probabilityDensityFunctionValueV;
		
		return probabilityDensityFunctionValue;
	}
	
//	TODO: Add Javadocs!
	public float probabilityDensityFunction(final Sample2F sample) {
		final int indexU = saturate(toInt(sample.getU() * this.conditional[0].size()), 0, this.conditional[0].size() - 1);
		final int indexV = saturate(toInt(sample.getV() * this.marginal.size()), 0, this.marginal.size() - 1);
		
		final float probabilityDensityFunctionValue = this.conditional[indexV].function(indexU) / this.marginal.functionIntegral();
		
		return probabilityDensityFunctionValue;
	}
}