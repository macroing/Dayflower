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
package org.dayflower.sampler;

import static org.dayflower.utility.Ints.saturate;
import static org.dayflower.utility.Ints.toInt;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntPredicate;

import org.dayflower.utility.ParameterArguments;

import org.macroing.java.lang.Floats;

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
		
		if(Floats.isZero(this.functionIntegral)) {
			for(int i = 1; i < this.cumulativeDistributionFunction.length; i++) {
				this.cumulativeDistributionFunction[i] = (float)(i) / (float)(this.function.length);
			}
		} else {
			for(int i = 1; i < this.cumulativeDistributionFunction.length; i++) {
				this.cumulativeDistributionFunction[i] /= this.functionIntegral;
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Compares {@code object} to this {@code Distribution1F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Distribution1F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Distribution1F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Distribution1F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Distribution1F)) {
			return false;
		} else if(!Arrays.equals(this.cumulativeDistributionFunction, Distribution1F.class.cast(object).cumulativeDistributionFunction)) {
			return false;
		} else if(!Arrays.equals(this.function, Distribution1F.class.cast(object).function)) {
			return false;
		} else if(!Floats.equals(this.functionIntegral, Distribution1F.class.cast(object).functionIntegral)) {
			return false;
		} else {
			return true;
		}
	}
	
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
	 * This class contains static methods to perform operations on the {@code float[]}. These methods take an offset as a parameter argument, which makes it possible to combine the {@code float[]} with another and still use them.
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
	 * Returns a hash code for this {@code Distribution1F} instance.
	 * 
	 * @return a hash code for this {@code Distribution1F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(Arrays.hashCode(this.cumulativeDistributionFunction)), Integer.valueOf(Arrays.hashCode(this.function)), Float.valueOf(this.functionIntegral));
	}
	
	/**
	 * Returns the index of the closest value to {@code value} in the cumulative distribution function (CDF).
	 * 
	 * @param value a {@code float} value
	 * @return the index of the closest value to {@code value} in the cumulative distribution function (CDF)
	 */
	public int index(final float value) {
		return doFindInterval(this.cumulativeDistributionFunction.length, currentIndex -> cumulativeDistributionFunction(currentIndex) <= value);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the continuous probability density function (PDF) value at index {@code index} in {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset} or any offsets derived from it are invalid or {@code index} is less than {@code 0} or greater than {@code count(array, offset) - 1}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param array a {@code float[]} that contains the data for a {@code Distribution1F} instance
	 * @param offset the offset in {@code array} that points to the start of the data for a {@code Distribution1F} instance
	 * @param index the index
	 * @return the continuous probability density function (PDF) value at index {@code index} in {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code offset} or any offsets derived from it are invalid or {@code index} is less than {@code 0} or greater than {@code count(array, offset) - 1}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static float continuousProbabilityDensityFunction(final float[] array, final int offset, final int index) {
		Objects.requireNonNull(array, "array == null");
		
		ParameterArguments.requireRange(offset, 0, array.length - 1, "offset");
		ParameterArguments.requireRange(index, 0, count(array, offset) - 1, "index");
		
		final float functionIntegral = functionIntegral(array, offset);
		final float function = function(array, offset, index);
		
		return functionIntegral > 0.0F ? function / functionIntegral : 0.0F;
	}
	
	/**
	 * Performs a continuous remapping of {@code value} at index {@code index} in {@code array} using the cumulative distribution function (CDF).
	 * <p>
	 * Returns the remapped value.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset} or any offsets derived from it are invalid or {@code index} is less than {@code 0} or greater than {@code count(array, offset) - 1}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param array a {@code float[]} that contains the data for a {@code Distribution1F} instance
	 * @param offset the offset in {@code array} that points to the start of the data for a {@code Distribution1F} instance
	 * @param value the value
	 * @param index the index
	 * @return the remapped value
	 * @throws IllegalArgumentException thrown if, and only if, {@code offset} or any offsets derived from it are invalid or {@code index} is less than {@code 0} or greater than {@code count(array, offset) - 1}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static float continuousRemap(final float[] array, final int offset, final float value, final int index) {
		Objects.requireNonNull(array, "array == null");
		
		ParameterArguments.requireRange(offset, 0, array.length - 1, "offset");
		ParameterArguments.requireRange(index, 0, count(array, offset) - 1, "index");
		
		final int count = count(array, offset);
		
		final float cumulativeDistributionFunction0 = cumulativeDistributionFunction(array, offset, index + 0);
		final float cumulativeDistributionFunction1 = cumulativeDistributionFunction(array, offset, index + 1);
		
		if(cumulativeDistributionFunction1 - cumulativeDistributionFunction0 > 0.0F) {
			return (index + ((value - cumulativeDistributionFunction0) / (cumulativeDistributionFunction1 - cumulativeDistributionFunction0))) / count;
		}
		
		return (index + (value - cumulativeDistributionFunction0)) / count;
	}
	
	/**
	 * Returns the value of the cumulative distribution function (CDF) at index {@code index} in {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code offset}, any offsets derived from it or {@code index} are invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * For {@code offset}, its derived offsets and {@code index} to be valid, the following conditions have to be met:
	 * <ul>
	 * <li>{@code offset >= 0 && offset < array.length}</li>
	 * <li>{@code offset + 2 >= 0 && offset + 2 < array.length}</li>
	 * <li>{@code index >= 0 && index < count(array, offset)}</li>
	 * <li>{@code offset + 4 + index >= 0 && offset + 4 + index < array.length}</li>
	 * </ul>
	 * 
	 * @param array a {@code float[]} that contains the data for a {@code Distribution1F} instance
	 * @param offset the offset in {@code array} that points to the start of the data for a {@code Distribution1F} instance
	 * @param index the index
	 * @return the value of the cumulative distribution function (CDF) at index {@code index} in {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, either {@code offset}, any offsets derived from it or {@code index} are invalid
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static float cumulativeDistributionFunction(final float[] array, final int offset, final int index) {
		Objects.requireNonNull(array, "array == null");
		
		ParameterArguments.requireRange(offset, 0, array.length - 1, "offset");
		ParameterArguments.requireRange(index, 0, count(array, offset), "index");
		ParameterArguments.requireRange(offset + 4 + index, 0, array.length - 1, "(offset + 4 + index)");
		
		return array[offset + 4 + index];
	}
	
	/**
	 * Returns the discrete probability density function (PDF) value at index {@code index} in {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset} or any offsets derived from it are invalid or {@code index} is less than {@code 0} or greater than {@code count(array, offset) - 1}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param array a {@code float[]} that contains the data for a {@code Distribution1F} instance
	 * @param offset the offset in {@code array} that points to the start of the data for a {@code Distribution1F} instance
	 * @param index the index
	 * @return the discrete probability density function (PDF) value at index {@code index} in {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code offset} or any offsets derived from it are invalid or {@code index} is less than {@code 0} or greater than {@code count(array, offset) - 1}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static float discreteProbabilityDensityFunction(final float[] array, final int offset, final int index) {
		Objects.requireNonNull(array, "array == null");
		
		ParameterArguments.requireRange(offset, 0, array.length - 1, "offset");
		ParameterArguments.requireRange(index, 0, count(array, offset) - 1, "index");
		
		final int count = count(array, offset);
		
		final float functionIntegral = functionIntegral(array, offset);
		final float function = function(array, offset, index);
		
		return functionIntegral > 0.0F ? function / (functionIntegral * count) : 0.0F;
	}
	
	/**
	 * Performs a discrete remapping of {@code value} at index {@code index} in {@code array} using the cumulative distribution function (CDF).
	 * <p>
	 * Returns the remapped value.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset} or any offsets derived from it are invalid or {@code index} is less than {@code 0} or greater than {@code count(array, offset) - 1}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param array a {@code float[]} that contains the data for a {@code Distribution1F} instance
	 * @param offset the offset in {@code array} that points to the start of the data for a {@code Distribution1F} instance
	 * @param value the value
	 * @param index the index
	 * @return the remapped value
	 * @throws IllegalArgumentException thrown if, and only if, {@code offset} or any offsets derived from it are invalid or {@code index} is less than {@code 0} or greater than {@code count(array, offset) - 1}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static float discreteRemap(final float[] array, final int offset, final float value, final int index) {
		Objects.requireNonNull(array, "array == null");
		
		ParameterArguments.requireRange(offset, 0, array.length - 1, "offset");
		ParameterArguments.requireRange(index, 0, count(array, offset) - 1, "index");
		
		final float cumulativeDistributionFunction0 = cumulativeDistributionFunction(array, offset, index + 0);
		final float cumulativeDistributionFunction1 = cumulativeDistributionFunction(array, offset, index + 1);
		
		return (value - cumulativeDistributionFunction0) / (cumulativeDistributionFunction1 - cumulativeDistributionFunction0);
	}
	
	/**
	 * Returns the value of the function at index {@code index} in {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code offset}, any offsets derived from it or {@code index} are invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * For {@code offset}, its derived offsets and {@code index} to be valid, the following conditions have to be met:
	 * <ul>
	 * <li>{@code offset >= 0 && offset < array.length}</li>
	 * <li>{@code offset + 2 >= 0 && offset + 2 < array.length}</li>
	 * <li>{@code index >= 0 && index < count(array, offset)}</li>
	 * <li>{@code toInt(array[offset + 1]) >= 0 && toInt(array[offset + 1]) < array.length}</li>
	 * <li>{@code offset + 4 + index + toInt(array[offset + 1]) >= 0 && offset + 4 + index + toInt(array[offset + 1]) < array.length}</li>
	 * </ul>
	 * 
	 * @param array a {@code float[]} that contains the data for a {@code Distribution1F} instance
	 * @param offset the offset in {@code array} that points to the start of the data for a {@code Distribution1F} instance
	 * @param index the index
	 * @return the value of the function at index {@code index} in {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, either {@code offset}, any offsets derived from it or {@code index} are invalid
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static float function(final float[] array, final int offset, final int index) {
		Objects.requireNonNull(array, "array == null");
		
		ParameterArguments.requireRange(offset, 0, array.length - 1, "offset");
		ParameterArguments.requireRange(index, 0, count(array, offset) - 1, "index");
		ParameterArguments.requireRange(toInt(array[offset + 1]), 0, array.length - 1, "toInt(array[offset + 1])");
		ParameterArguments.requireRange(offset + 4 + index + toInt(array[offset + 1]), 0, array.length - 1, "(offset + 4 + index + toInt(array[offset + 1]))");
		
		return array[offset + 4 + index + toInt(array[offset + 1])];
	}
	
	/**
	 * Returns the function integral in {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset} or any offsets derived from it are invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * For {@code offset} and its derived offset to be valid, the following conditions have to be met:
	 * <ul>
	 * <li>{@code offset >= 0 && offset < array.length}</li>
	 * <li>{@code offset + 3 >= 0 && offset + 3 < array.length}</li>
	 * </ul>
	 * 
	 * @param array a {@code float[]} that contains the data for a {@code Distribution1F} instance
	 * @param offset the offset in {@code array} that points to the start of the data for a {@code Distribution1F} instance
	 * @return the function integral in {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code offset} or any offsets derived from it are invalid
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static float functionIntegral(final float[] array, final int offset) {
		Objects.requireNonNull(array, "array == null");
		
		ParameterArguments.requireRange(offset, 0, array.length - 1, "offset");
		ParameterArguments.requireRange(offset + 3, 0, array.length - 1, "(offset + 3)");
		
		return array[offset + 3];
	}
	
	/**
	 * Returns the value count of the function in {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset} or any offsets derived from it are invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * For {@code offset} and its derived offset to be valid, the following conditions have to be met:
	 * <ul>
	 * <li>{@code offset >= 0 && offset < array.length}</li>
	 * <li>{@code offset + 2 >= 0 && offset + 2 < array.length}</li>
	 * </ul>
	 * 
	 * @param array a {@code float[]} that contains the data for a {@code Distribution1F} instance
	 * @param offset the offset in {@code array} that points to the start of the data for a {@code Distribution1F} instance
	 * @return the value count of the function in {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code offset} or any offsets derived from it are invalid
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static int count(final float[] array, final int offset) {
		Objects.requireNonNull(array, "array == null");
		
		ParameterArguments.requireRange(offset, 0, array.length - 1, "offset");
		ParameterArguments.requireRange(offset + 2, 0, array.length - 1, "(offset + 2)");
		
		return toInt(array[offset + 2]);
	}
	
	/**
	 * Returns the index of the closest value to {@code value} in the cumulative distribution function (CDF) in {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset} or any offsets derived from it are invalid, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param array a {@code float[]} that contains the data for a {@code Distribution1F} instance
	 * @param offset the offset in {@code array} that points to the start of the data for a {@code Distribution1F} instance
	 * @param value a {@code float} value
	 * @return the index of the closest value to {@code value} in the cumulative distribution function (CDF) in {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code offset} or any offsets derived from it are invalid
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static int index(final float[] array, final int offset, final float value) {
		Objects.requireNonNull(array, "array == null");
		
		ParameterArguments.requireRange(offset, 0, array.length - 1, "offset");
		ParameterArguments.requireRange(offset + 1, 0, array.length - 1, "(offset + 1)");
		
		final int length = toInt(array[offset + 1]);
		
		int currentMinimum = 0;
		int currentLength = length;
		
		while(currentLength > 0) {
			int currentHalf = currentLength >> 1;
			int currentMiddle = currentMinimum + currentHalf;
			
			if(cumulativeDistributionFunction(array, offset, currentMiddle) <= value) {
				currentMinimum = currentMiddle + 1;
				currentLength -= currentHalf + 1;
			} else {
				currentLength = currentHalf;
			}
		}
		
		return saturate(currentMinimum - 1, 0, length - 2);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static int doFindInterval(final int length, final IntPredicate intPredicate) {
		ParameterArguments.requireRange(length, 0, Integer.MAX_VALUE, "length");
		
		Objects.requireNonNull(intPredicate, "intPredicate == null");
		
		int currentMinimum = 0;
		int currentLength = length;
		
		while(currentLength > 0) {
			int currentHalf = currentLength >> 1;
			int currentMiddle = currentMinimum + currentHalf;
			
			if(intPredicate.test(currentMiddle)) {
				currentMinimum = currentMiddle + 1;
				currentLength -= currentHalf + 1;
			} else {
				currentLength = currentHalf;
			}
		}
		
		return saturate(currentMinimum - 1, 0, length - 2);
	}
}