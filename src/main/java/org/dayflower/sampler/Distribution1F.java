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
package org.dayflower.sampler;

import static org.dayflower.utility.Floats.isZero;
import static org.dayflower.utility.Floats.toFloat;
import static org.dayflower.utility.Ints.findInterval;

import java.util.Objects;

import org.dayflower.utility.ParameterArguments;

/**
 * A {@code Distribution1F} represents a 1-dimensional distribution and contains methods to sample it in a continuous or discrete way.
 * <p>
 * To use this class consider the following example:
 * <pre>
 * {@code
 * float[] function = ...;
 * 
 * Distribution1F distribution1F = new Distribution1F(function);
 * 
 * float value = 0.5F;
 * 
 * int index = distribution1F.index(value);
 * 
 * float valueContinuous = distribution1F.continuousRemap(value, index);
 * float valueDiscrete = distribution1F.discreteRemap(value, index);
 * 
 * float probabilityDensityFunctionContinuous = distribution1F.continuousProbabilityDensityFunction(index);
 * float probabilityDensityFunctionDiscrete = distribution1F.discreteProbabilityDensityFunction(index);
 * }
 * </pre>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Distribution1F {
	private final float[] cumulativeDistributionFunction;
	private final float[] function;
	private final float functionIntegral;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Distribution1F} instance.
	 * <p>
	 * If {@code function} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Modifications to the {@code float[]} {@code function} will not affect this {@code Distribution1F} instance.
	 * 
	 * @param function a {@code float[]} with the values of the function
	 * @throws NullPointerException thrown if, and only if, {@code function} is {@code null}
	 */
	public Distribution1F(final float[] function) {
		this.cumulativeDistributionFunction = new float[Objects.requireNonNull(function, "function == null").length + 1];
		this.function = function.clone();
		
		for(int i = 1; i < this.cumulativeDistributionFunction.length; i++) {
			this.cumulativeDistributionFunction[i] = this.cumulativeDistributionFunction[i - 1] + this.function[i - 1] / this.function.length;
		}
		
		this.functionIntegral = this.cumulativeDistributionFunction[this.function.length];
		
		if(isZero(this.functionIntegral)) {
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
	
	/**
	 * Returns the continuous probability density function (PDF) value at index {@code index}.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than {@code count() - 1}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param index the index
	 * @return the continuous probability density function (PDF) value at index {@code index}
	 * @throws IllegalArgumentException thrown if, and only if, {@code index} is less than {@code 0} or greater than {@code count() - 1}
	 */
	public float continuousProbabilityDensityFunction(final int index) {
		ParameterArguments.requireRange(index, 0, count() - 1, "index");
		
		return functionIntegral() > 0.0F ? function(index) / functionIntegral() : 0.0F;
	}
	
	/**
	 * Performs a continuous remapping of {@code value} at index {@code index} using the cumulative distribution function (CDF).
	 * <p>
	 * Returns the remapped value.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than {@code count() - 1}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param value the value
	 * @param index the index
	 * @return the remapped value
	 * @throws IllegalArgumentException thrown if, and only if, {@code index} is less than {@code 0} or greater than {@code count() - 1}
	 */
	public float continuousRemap(final float value, final int index) {
		ParameterArguments.requireRange(index, 0, count() - 1, "index");
		
		if(cumulativeDistributionFunction(index + 1) - cumulativeDistributionFunction(index) > 0.0F) {
			return (index + ((value - cumulativeDistributionFunction(index)) / (cumulativeDistributionFunction(index + 1) - cumulativeDistributionFunction(index)))) / count();
		}
		
		return (index + (value - cumulativeDistributionFunction(index))) / count();
	}
	
	/**
	 * Returns the value of the cumulative distribution function (CDF) at index {@code index}.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than {@code count()}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param index the index
	 * @return the value of the cumulative distribution function (CDF) at index {@code index}
	 * @throws IllegalArgumentException thrown if, and only if, {@code index} is less than {@code 0} or greater than {@code count()}
	 */
	public float cumulativeDistributionFunction(final int index) {
		return this.cumulativeDistributionFunction[ParameterArguments.requireRange(index, 0, count(), "index")];
	}
	
	/**
	 * Returns the discrete probability density function (PDF) value at index {@code index}.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than {@code count() - 1}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param index the index
	 * @return the discrete probability density function (PDF) value at index {@code index}
	 * @throws IllegalArgumentException thrown if, and only if, {@code index} is less than {@code 0} or greater than {@code count() - 1}
	 */
	public float discreteProbabilityDensityFunction(final int index) {
		ParameterArguments.requireRange(index, 0, count() - 1, "index");
		
		return functionIntegral() > 0.0F ? function(index) / (functionIntegral() * count()) : 0.0F;
	}
	
	/**
	 * Performs a discrete remapping of {@code value} at index {@code index} using the cumulative distribution function (CDF).
	 * <p>
	 * Returns the remapped value.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than {@code count() - 1}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param value the value
	 * @param index the index
	 * @return the remapped value
	 * @throws IllegalArgumentException thrown if, and only if, {@code index} is less than {@code 0} or greater than {@code count() - 1}
	 */
	public float discreteRemap(final float value, final int index) {
		ParameterArguments.requireRange(index, 0, count() - 1, "index");
		
		return (value - cumulativeDistributionFunction(index)) / (cumulativeDistributionFunction(index + 1) - cumulativeDistributionFunction(index));
	}
	
	/**
	 * Returns the value of the function at index {@code index}.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than {@code count() - 1}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param index the index
	 * @return the value of the function at index {@code index}
	 * @throws IllegalArgumentException thrown if, and only if, {@code index} is less than {@code 0} or greater than {@code count() - 1}
	 */
	public float function(final int index) {
		return this.function[ParameterArguments.requireRange(index, 0, count() - 1, "index")];
	}
	
	/**
	 * Returns the function integral.
	 * 
	 * @return the function integral
	 */
	public float functionIntegral() {
		return this.functionIntegral;
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code Distribution1F} instance.
	 * <p>
	 * The {@code float[]} will contain the following data:
	 * <ol>
	 * <li>The length of the array itself.</li>
	 * <li>The length of the cumulative distribution function (CDF).</li>
	 * <li>The length of the function.</li>
	 * <li>The function integral.</li>
	 * <li>The cumulative distribution function (CDF) itself.</li>
	 * <li>The function itself.</li>
	 * </ol>
	 * 
	 * @return a {@code float[]} representation of this {@code Distribution1F} instance
	 */
	public float[] toArray() {
		final float[] cumulativeDistributionFunction = this.cumulativeDistributionFunction;
		final float[] function = this.function;
		
		final float functionIntegral = this.functionIntegral;
		
		final float[] array = new float[1 + 1 + cumulativeDistributionFunction.length + 1 + function.length + 1];
		
		int index = 0;
		
		array[index++] = array.length;
		array[index++] = cumulativeDistributionFunction.length;
		array[index++] = function.length;
		array[index++] = functionIntegral;
		
		for(int i = 0; i < cumulativeDistributionFunction.length; i++) {
			array[index++] = cumulativeDistributionFunction[i];
		}
		
		for(int i = 0; i < function.length; i++) {
			array[index++] = function[i];
		}
		
		return array;
	}
	
	/**
	 * Returns the value count of the function.
	 * 
	 * @return the value count of the function
	 */
	public int count() {
		return this.function.length;
	}
	
	/**
	 * Returns the index of the closest value to {@code value} in the cumulative distribution function (CDF).
	 * 
	 * @param value a {@code float} value
	 * @return the index of the closest value to {@code value} in the cumulative distribution function (CDF)
	 */
	public int index(final float value) {
		return findInterval(this.cumulativeDistributionFunction.length, currentIndex -> cumulativeDistributionFunction(currentIndex) <= value);
	}
}