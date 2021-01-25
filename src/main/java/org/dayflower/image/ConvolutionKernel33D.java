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
package org.dayflower.image;

import static org.dayflower.utility.Doubles.equal;
import static org.dayflower.utility.Doubles.isZero;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A {@code ConvolutionKernel33D} is a convolution kernel with three rows and three columns.
 * <p>
 * This class is immutable and therefore suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ConvolutionKernel33D {
	/**
	 * A {@code ConvolutionKernel33D} instance that performs a box blur effect.
	 */
	public static final ConvolutionKernel33D BOX_BLUR = new ConvolutionKernel33D(1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D / 9.0D, 0.0D);
	
	/**
	 * A {@code ConvolutionKernel33D} instance that performs an edge detection effect.
	 */
	public static final ConvolutionKernel33D EDGE_DETECTION = new ConvolutionKernel33D(-1.0D, -1.0D, -1.0D, -1.0D, 8.0D, -1.0D, -1.0D, -1.0D, -1.0D);
	
	/**
	 * A {@code ConvolutionKernel33D} instance that performs an emboss effect.
	 */
	public static final ConvolutionKernel33D EMBOSS = new ConvolutionKernel33D(-1.0D, -1.0D, 0.0D, -1.0D, 0.0D, 1.0D, 0.0D, 1.0D, 1.0D, 1.0D, 0.5D);
	
	/**
	 * A {@code ConvolutionKernel33D} instance that performs a Gaussian blur effect.
	 */
	public static final ConvolutionKernel33D GAUSSIAN_BLUR = new ConvolutionKernel33D(1.0D, 2.0D, 1.0D, 2.0D, 4.0D, 2.0D, 1.0D, 2.0D, 1.0D, 1.0D / 16.0D, 0.0D);
	
	/**
	 * A {@code ConvolutionKernel33D} instance that performs a gradient effect in the horizontal direction.
	 */
	public static final ConvolutionKernel33D GRADIENT_HORIZONTAL = new ConvolutionKernel33D(-1.0D, -1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	
	/**
	 * A {@code ConvolutionKernel33D} instance that performs a gradient effect in the vertical direction.
	 */
	public static final ConvolutionKernel33D GRADIENT_VERTICAL = new ConvolutionKernel33D(-1.0D, 0.0D, 1.0D, -1.0D, 0.0D, 1.0D, -1.0D, 0.0D, 1.0D);
	
	/**
	 * A {@code ConvolutionKernel33D} instance that performs a sharpen effect.
	 */
	public static final ConvolutionKernel33D SHARPEN = new ConvolutionKernel33D(-1.0D, -1.0D, -1.0D, -1.0D, 9.0D, -1.0D, -1.0D, -1.0D, -1.0D);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final double bias;
	private final double element11;
	private final double element12;
	private final double element13;
	private final double element21;
	private final double element22;
	private final double element23;
	private final double element31;
	private final double element32;
	private final double element33;
	private final double factor;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ConvolutionKernel33D} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ConvolutionKernel33D(0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D)
	 * }
	 * </pre>
	 */
	public ConvolutionKernel33D() {
		this(0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	}
	
	/**
	 * Constructs a new {@code ConvolutionKernel33D} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ConvolutionKernel33D(element11, element12, element13, element21, element22, element23, element31, element32, element33, 1.0F, 0.0F)
	 * }
	 * </pre>
	 * 
	 * @param element11 the element at index 0 or row 1 and column 1
	 * @param element12 the element at index 1 or row 1 and column 2
	 * @param element13 the element at index 2 or row 1 and column 3
	 * @param element21 the element at index 3 or row 2 and column 1
	 * @param element22 the element at index 4 or row 2 and column 2
	 * @param element23 the element at index 5 or row 2 and column 3
	 * @param element31 the element at index 6 or row 3 and column 1
	 * @param element32 the element at index 7 or row 3 and column 2
	 * @param element33 the element at index 8 or row 3 and column 3
	 */
	public ConvolutionKernel33D(final double element11, final double element12, final double element13, final double element21, final double element22, final double element23, final double element31, final double element32, final double element33) {
		this(element11, element12, element13, element21, element22, element23, element31, element32, element33, 1.0D, 0.0D);
	}
	
	/**
	 * Constructs a new {@code ConvolutionKernel33D} instance.
	 * 
	 * @param element11 the element at index 0 or row 1 and column 1
	 * @param element12 the element at index 1 or row 1 and column 2
	 * @param element13 the element at index 2 or row 1 and column 3
	 * @param element21 the element at index 3 or row 2 and column 1
	 * @param element22 the element at index 4 or row 2 and column 2
	 * @param element23 the element at index 5 or row 2 and column 3
	 * @param element31 the element at index 6 or row 3 and column 1
	 * @param element32 the element at index 7 or row 3 and column 2
	 * @param element33 the element at index 8 or row 3 and column 3
	 * @param factor the factor to use
	 * @param bias the bias to use
	 */
	public ConvolutionKernel33D(final double element11, final double element12, final double element13, final double element21, final double element22, final double element23, final double element31, final double element32, final double element33, final double factor, final double bias) {
		this.element11 = element11;
		this.element12 = element12;
		this.element13 = element13;
		this.element21 = element21;
		this.element22 = element22;
		this.element23 = element23;
		this.element31 = element31;
		this.element32 = element32;
		this.element33 = element33;
		this.factor = factor;
		this.bias = bias;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code ConvolutionKernel33D} instance.
	 * 
	 * @return a {@code String} representation of this {@code ConvolutionKernel33D} instance
	 */
	@Override
	public String toString() {
		final String row1 = String.format("%+.10f, %+.10f, %+.10f", Double.valueOf(this.element11), Double.valueOf(this.element12), Double.valueOf(this.element13));
		final String row2 = String.format("%+.10f, %+.10f, %+.10f", Double.valueOf(this.element21), Double.valueOf(this.element22), Double.valueOf(this.element23));
		final String row3 = String.format("%+.10f, %+.10f, %+.10f", Double.valueOf(this.element31), Double.valueOf(this.element32), Double.valueOf(this.element33));
		
		return String.format("new ConvolutionKernel33D(%s, %s, %s, %+.10f, %+.10f)", row1, row2, row3, Double.valueOf(this.factor), Double.valueOf(this.bias));
	}
	
	/**
	 * Compares {@code object} to this {@code ConvolutionKernel33D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ConvolutionKernel33D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ConvolutionKernel33D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ConvolutionKernel33D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ConvolutionKernel33D)) {
			return false;
		} else if(!equal(this.bias, ConvolutionKernel33D.class.cast(object).bias)) {
			return false;
		} else if(!equal(this.element11, ConvolutionKernel33D.class.cast(object).element11)) {
			return false;
		} else if(!equal(this.element12, ConvolutionKernel33D.class.cast(object).element12)) {
			return false;
		} else if(!equal(this.element13, ConvolutionKernel33D.class.cast(object).element13)) {
			return false;
		} else if(!equal(this.element21, ConvolutionKernel33D.class.cast(object).element21)) {
			return false;
		} else if(!equal(this.element22, ConvolutionKernel33D.class.cast(object).element22)) {
			return false;
		} else if(!equal(this.element23, ConvolutionKernel33D.class.cast(object).element23)) {
			return false;
		} else if(!equal(this.element31, ConvolutionKernel33D.class.cast(object).element31)) {
			return false;
		} else if(!equal(this.element32, ConvolutionKernel33D.class.cast(object).element32)) {
			return false;
		} else if(!equal(this.element33, ConvolutionKernel33D.class.cast(object).element33)) {
			return false;
		} else if(!equal(this.factor, ConvolutionKernel33D.class.cast(object).factor)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the bias associated with this {@code ConvolutionKernel33D} instance.
	 * <p>
	 * This is the same as Offset in Gimp.
	 * 
	 * @return the bias associated with this {@code ConvolutionKernel33D} instance
	 */
	public double getBias() {
		return this.bias;
	}
	
	/**
	 * Returns the element at index 0 or row 1 and column 1.
	 * 
	 * @return the element at index 0 or row 1 and column 1
	 */
	public double getElement11() {
		return this.element11;
	}
	
	/**
	 * Returns the element at index 1 or row 1 and column 2.
	 * 
	 * @return the element at index 1 or row 1 and column 2
	 */
	public double getElement12() {
		return this.element12;
	}
	
	/**
	 * Returns the element at index 2 or row 1 and column 3.
	 * 
	 * @return the element at index 2 or row 1 and column 3
	 */
	public double getElement13() {
		return this.element13;
	}
	
	/**
	 * Returns the element at index 3 or row 2 and column 1.
	 * 
	 * @return the element at index 3 or row 2 and column 1
	 */
	public double getElement21() {
		return this.element21;
	}
	
	/**
	 * Returns the element at index 4 or row 2 and column 2.
	 * 
	 * @return the element at index 4 or row 2 and column 2
	 */
	public double getElement22() {
		return this.element22;
	}
	
	/**
	 * Returns the element at index 5 or row 2 and column 3.
	 * 
	 * @return the element at index 5 or row 2 and column 3
	 */
	public double getElement23() {
		return this.element23;
	}
	
	/**
	 * Returns the element at index 6 or row 3 and column 1.
	 * 
	 * @return the element at index 6 or row 3 and column 1
	 */
	public double getElement31() {
		return this.element31;
	}
	
	/**
	 * Returns the element at index 7 or row 3 and column 2.
	 * 
	 * @return the element at index 7 or row 3 and column 2
	 */
	public double getElement32() {
		return this.element32;
	}
	
	/**
	 * Returns the element at index 8 or row 3 and column 3.
	 * 
	 * @return the element at index 8 or row 3 and column 3
	 */
	public double getElement33() {
		return this.element33;
	}
	
	/**
	 * Returns the factor associated with this {@code ConvolutionKernel33D} instance.
	 * <p>
	 * This is the same as the reciprocal value of the Divisor in Gimp.
	 * 
	 * @return the factor associated with this {@code ConvolutionKernel33D} instance
	 */
	public double getFactor() {
		return this.factor;
	}
	
	/**
	 * Returns a hash code for this {@code ConvolutionKernel33D} instance.
	 * 
	 * @return a hash code for this {@code ConvolutionKernel33D} instance
	 */
	@Override
	public int hashCode() {
		final Double bias = Double.valueOf(this.bias);
		
		final Double element11 = Double.valueOf(this.element11);
		final Double element12 = Double.valueOf(this.element12);
		final Double element13 = Double.valueOf(this.element13);
		
		final Double element21 = Double.valueOf(this.element21);
		final Double element22 = Double.valueOf(this.element22);
		final Double element23 = Double.valueOf(this.element23);
		
		final Double element31 = Double.valueOf(this.element31);
		final Double element32 = Double.valueOf(this.element32);
		final Double element33 = Double.valueOf(this.element33);
		
		final Double factor = Double.valueOf(this.factor);
		
		return Objects.hash(bias, element11, element12, element13, element21, element22, element23, element31, element32, element33, factor);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code ConvolutionKernel33D} instance that performs a random effect.
	 * 
	 * @return a {@code ConvolutionKernel33D} instance that performs a random effect
	 */
	public static ConvolutionKernel33D random() {
		final double element11 = doNextDouble() * 2.0D - 1.0D;
		final double element12 = doNextDouble() * 2.0D - 1.0D;
		final double element13 = doNextDouble() * 2.0D - 1.0D;
		final double element21 = doNextDouble() * 2.0D - 1.0D;
		final double element22 = 1.0D;
		final double element23 = doNextDouble() * 2.0D - 1.0D;
		final double element31 = doNextDouble() * 2.0D - 1.0D;
		final double element32 = doNextDouble() * 2.0D - 1.0D;
		final double element33 = doNextDouble() * 2.0D - 1.0D;
		
		final double elementTotal = element11 + element12 + element13 + element21 + element22 + element23 + element31 + element32 + element33;
		
		final boolean isFactorBasedOnElementTotal = doNextBoolean();
		final boolean isFactorBasedOnRandomFloat = doNextBoolean();
		final boolean isBiasBasedOnRandomFloat = doNextBoolean();
		
		final double factorBasedOnElementTotal = isZero(elementTotal) ? 1.0D : 1.0D / elementTotal;
		
		final double factor = isFactorBasedOnElementTotal ? factorBasedOnElementTotal : isFactorBasedOnRandomFloat ? doNextDouble() : 1.0D;
		final double bias = isBiasBasedOnRandomFloat ? doNextDouble() : 0.0D;
		
		return new ConvolutionKernel33D(element11, element12, element13, element21, element22, element23, element31, element32, element33, factor, bias);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static boolean doNextBoolean() {
		return ThreadLocalRandom.current().nextBoolean();
	}
	
	private static double doNextDouble() {
		return ThreadLocalRandom.current().nextDouble();
	}
}