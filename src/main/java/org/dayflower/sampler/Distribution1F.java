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

import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.toFloat;
import static org.dayflower.util.Ints.findInterval;
import static org.dayflower.util.Ints.requireRange;

import java.lang.reflect.Field;
import java.util.Objects;

//TODO: Add Javadocs!
public final class Distribution1F {
	private final float[] cumulativeDistributionFunction;
	private final float[] function;
	private final float functionIntegral;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Distribution1F(final float[] function) {
		this.cumulativeDistributionFunction = new float[Objects.requireNonNull(function, "function == null").length + 1];
		this.function = function.clone();
		
		for(int i = 1; i < this.cumulativeDistributionFunction.length; i++) {
			this.cumulativeDistributionFunction[i] = this.cumulativeDistributionFunction[i - 1] + this.function[i - 1] / this.function.length;
		}
		
		this.functionIntegral = this.cumulativeDistributionFunction[this.function.length];
		
		if(equal(this.functionIntegral, 0.0F)) {
			for(int i = 1; i < this.cumulativeDistributionFunction.length; i++) {
				this.cumulativeDistributionFunction[i] = toFloat(i) / toFloat(this.function.length);
			}
		} else {
			for(int i = 1; i < this.cumulativeDistributionFunction.length; i++) {
				this.cumulativeDistributionFunction[i] /= this.functionIntegral;
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public float continuousProbabilityDensityFunction(final int index) {
		requireRange(index, 0, size(), "index");
		
		return this.functionIntegral > 0.0F ? this.function[index] / this.functionIntegral : 0.0F;
	}
	
//	TODO: Add Javadocs!
	public float continuousRemapU(final float u, final int index) {
		requireRange(index, 0, size(), "index");
		
		if((this.cumulativeDistributionFunction[index + 1] - this.cumulativeDistributionFunction[index]) > 0.0F) {
			return (index + ((u - this.cumulativeDistributionFunction[index]) / (this.cumulativeDistributionFunction[index + 1] - this.cumulativeDistributionFunction[index]))) / size();
		}
		
		return (index + (u - this.cumulativeDistributionFunction[index])) / size();
	}
	
//	TODO: Add Javadocs!
	public float discreteProbabilityDensityFunction(final int index) {
		requireRange(index, 0, size(), "index");
		
		return this.functionIntegral > 0.0F ? this.function[index] / (this.functionIntegral * size()) : 0.0F;
	}
	
//	TODO: Add Javadocs!
	public float discreteRemapU(final float u, final int index) {
		requireRange(index, 0, size(), "index");
		
		return (u - this.cumulativeDistributionFunction[index]) / (this.cumulativeDistributionFunction[index + 1] - this.cumulativeDistributionFunction[index]);
	}
	
//	TODO: Add Javadocs!
	public int index(final float u) {
		return findInterval(this.cumulativeDistributionFunction.length, currentIndex -> this.cumulativeDistributionFunction[currentIndex] <= u);
	}
	
//	TODO: Add Javadocs!
	public int size() {
		return this.function.length;
	}
}