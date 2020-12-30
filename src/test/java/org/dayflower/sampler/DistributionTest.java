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

public final class DistributionTest {
	private DistributionTest() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
		doTestDistribution1F();
		doTestDistribution2F();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static void doTestDistribution1F() {
		final float[] function = new float[] {0.0F, 0.1F, 0.2F, 0.3F, 0.4F, 0.5F, 0.6F, 0.7F, 0.8F, 0.9F, 1.0F};
		
		final Distribution1F distribution = new Distribution1F(function);
		
		final float value = 0.5F;
		
		final int index = distribution.index(value);
		
		final float continuousProbabilityDensityFunctionValue = distribution.continuousProbabilityDensityFunction(index);
		final float discreteProbabilityDensityFunctionValue = distribution.discreteProbabilityDensityFunction(index);
		
		final float continuousValue = distribution.continuousRemap(value, index);
		final float discreteValue = distribution.discreteRemap(value, index);
		
		System.out.println("Continuous: Index=" + index + ", PDF=" + continuousProbabilityDensityFunctionValue + ", Value=" + continuousValue);
		System.out.println("Discrete: Index=" + index + ", PDF=" + discreteProbabilityDensityFunctionValue + ", Value=" + discreteValue);
	}
	
	private static void doTestDistribution2F() {
		final float[][] functions = new float[][] {
			new float[] {0.0F, 0.1F, 0.2F, 0.3F, 0.4F, 0.5F, 0.6F, 0.7F, 0.8F, 0.9F, 1.0F},
			new float[] {0.0F, 0.1F, 0.2F, 0.3F, 0.4F, 0.5F, 0.6F, 0.7F, 0.8F, 0.9F, 1.0F},
			new float[] {0.0F, 0.1F, 0.2F, 0.3F, 0.4F, 0.5F, 0.6F, 0.7F, 0.8F, 0.9F, 1.0F}
		};
		
		final Distribution2F distribution = new Distribution2F(functions);
		
		final Sample2F sample = new Sample2F(0.5F, 0.5F);
		final Sample2F sampleRemapped = distribution.continuousRemap(sample);
		
		final float probabilityDensityFunctionValue0 = distribution.continuousProbabilityDensityFunction(sample);
		final float probabilityDensityFunctionValue1 = distribution.probabilityDensityFunction(sampleRemapped);
		
		System.out.println("SampleRemapped=" + sampleRemapped + ", PDF #0=" + probabilityDensityFunctionValue0 + ", PDF #1=" + probabilityDensityFunctionValue1);
	}
}