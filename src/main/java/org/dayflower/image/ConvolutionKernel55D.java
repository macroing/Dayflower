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
 * A {@code ConvolutionKernel55D} is a convolution kernel with 25 {@code double}-based elements in five rows and five columns.
 * <p>
 * This class is immutable and therefore suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ConvolutionKernel55D {
	/**
	 * A {@code ConvolutionKernel55D} instance that performs a Gaussian blur effect.
	 */
	public static final ConvolutionKernel55D GAUSSIAN_BLUR = new ConvolutionKernel55D(1.0D, 4.0D, 6.0D, 4.0D, 1.0D, 4.0D, 16.0D, 24.0D, 16.0D, 4.0D, 6.0D, 24.0D, 36.0D, 24.0D, 6.0D, 4.0D, 16.0D, 24.0D, 16.0D, 4.0D, 1.0D, 4.0D, 6.0D, 4.0D, 1.0D, 1.0D / 256.0D, 0.0D);
	
	/**
	 * A {@code ConvolutionKernel55D} instance that performs an unsharp masking effect.
	 */
	public static final ConvolutionKernel55D UNSHARP_MASKING = new ConvolutionKernel55D(1.0D, 4.0D, 6.0D, 4.0D, 1.0D, 4.0D, 16.0D, 24.0D, 16.0D, 4.0D, 6.0D, 24.0D, -476.0D, 24.0D, 6.0D, 4.0D, 16.0D, 24.0D, 16.0D, 4.0D, 1.0D, 4.0D, 6.0D, 4.0D, 1.0D, -1.0D / 256.0D, 0.0D);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final double bias;
	private final double element11;
	private final double element12;
	private final double element13;
	private final double element14;
	private final double element15;
	private final double element21;
	private final double element22;
	private final double element23;
	private final double element24;
	private final double element25;
	private final double element31;
	private final double element32;
	private final double element33;
	private final double element34;
	private final double element35;
	private final double element41;
	private final double element42;
	private final double element43;
	private final double element44;
	private final double element45;
	private final double element51;
	private final double element52;
	private final double element53;
	private final double element54;
	private final double element55;
	private final double factor;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ConvolutionKernel55D} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ConvolutionKernel55D(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	 * }
	 * </pre>
	 */
	public ConvolutionKernel55D() {
		this(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	}
	
	/**
	 * Constructs a new {@code ConvolutionKernel55D} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ConvolutionKernel55D(element11, element12, element13, element14, element15, element21, element22, element23, element24, element25, element31, element32, element33, element34, element35, element41, element42, element43, element44, element45, element51, element52, element53, element54, element55, 1.0F, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @param element11 the element at index 0 or row 1 and column 1
	 * @param element12 the element at index 1 or row 1 and column 2
	 * @param element13 the element at index 2 or row 1 and column 3
	 * @param element14 the element at index 3 or row 1 and column 4
	 * @param element15 the element at index 4 or row 1 and column 5
	 * @param element21 the element at index 5 or row 2 and column 1
	 * @param element22 the element at index 6 or row 2 and column 2
	 * @param element23 the element at index 7 or row 2 and column 3
	 * @param element24 the element at index 8 or row 2 and column 4
	 * @param element25 the element at index 9 or row 2 and column 5
	 * @param element31 the element at index 10 or row 3 and column 1
	 * @param element32 the element at index 11 or row 3 and column 2
	 * @param element33 the element at index 12 or row 3 and column 3
	 * @param element34 the element at index 13 or row 3 and column 4
	 * @param element35 the element at index 14 or row 3 and column 5
	 * @param element41 the element at index 15 or row 4 and column 1
	 * @param element42 the element at index 16 or row 4 and column 2
	 * @param element43 the element at index 17 or row 4 and column 3
	 * @param element44 the element at index 18 or row 4 and column 4
	 * @param element45 the element at index 19 or row 4 and column 5
	 * @param element51 the element at index 20 or row 5 and column 1
	 * @param element52 the element at index 21 or row 5 and column 2
	 * @param element53 the element at index 22 or row 5 and column 3
	 * @param element54 the element at index 23 or row 5 and column 4
	 * @param element55 the element at index 24 or row 5 and column 5
	 */
	public ConvolutionKernel55D(final double element11, final double element12, final double element13, final double element14, final double element15, final double element21, final double element22, final double element23, final double element24, final double element25, final double element31, final double element32, final double element33, final double element34, final double element35, final double element41, final double element42, final double element43, final double element44, final double element45, final double element51, final double element52, final double element53, final double element54, final double element55) {
		this(element11, element12, element13, element14, element15, element21, element22, element23, element24, element25, element31, element32, element33, element34, element35, element41, element42, element43, element44, element45, element51, element52, element53, element54, element55, 1.0D, 0.0D);
	}
	
	/**
	 * Constructs a new {@code ConvolutionKernel55D} instance.
	 * 
	 * @param element11 the element at index 0 or row 1 and column 1
	 * @param element12 the element at index 1 or row 1 and column 2
	 * @param element13 the element at index 2 or row 1 and column 3
	 * @param element14 the element at index 3 or row 1 and column 4
	 * @param element15 the element at index 4 or row 1 and column 5
	 * @param element21 the element at index 5 or row 2 and column 1
	 * @param element22 the element at index 6 or row 2 and column 2
	 * @param element23 the element at index 7 or row 2 and column 3
	 * @param element24 the element at index 8 or row 2 and column 4
	 * @param element25 the element at index 9 or row 2 and column 5
	 * @param element31 the element at index 10 or row 3 and column 1
	 * @param element32 the element at index 11 or row 3 and column 2
	 * @param element33 the element at index 12 or row 3 and column 3
	 * @param element34 the element at index 13 or row 3 and column 4
	 * @param element35 the element at index 14 or row 3 and column 5
	 * @param element41 the element at index 15 or row 4 and column 1
	 * @param element42 the element at index 16 or row 4 and column 2
	 * @param element43 the element at index 17 or row 4 and column 3
	 * @param element44 the element at index 18 or row 4 and column 4
	 * @param element45 the element at index 19 or row 4 and column 5
	 * @param element51 the element at index 20 or row 5 and column 1
	 * @param element52 the element at index 21 or row 5 and column 2
	 * @param element53 the element at index 22 or row 5 and column 3
	 * @param element54 the element at index 23 or row 5 and column 4
	 * @param element55 the element at index 24 or row 5 and column 5
	 * @param factor the factor to use
	 * @param bias the bias to use
	 */
	public ConvolutionKernel55D(final double element11, final double element12, final double element13, final double element14, final double element15, final double element21, final double element22, final double element23, final double element24, final double element25, final double element31, final double element32, final double element33, final double element34, final double element35, final double element41, final double element42, final double element43, final double element44, final double element45, final double element51, final double element52, final double element53, final double element54, final double element55, final double factor, final double bias) {
		this.element11 = element11;
		this.element12 = element12;
		this.element13 = element13;
		this.element14 = element14;
		this.element15 = element15;
		this.element21 = element21;
		this.element22 = element22;
		this.element23 = element23;
		this.element24 = element24;
		this.element25 = element25;
		this.element31 = element31;
		this.element32 = element32;
		this.element33 = element33;
		this.element34 = element34;
		this.element35 = element35;
		this.element41 = element41;
		this.element42 = element42;
		this.element43 = element43;
		this.element44 = element44;
		this.element45 = element45;
		this.element51 = element51;
		this.element52 = element52;
		this.element53 = element53;
		this.element54 = element54;
		this.element55 = element55;
		this.factor = factor;
		this.bias = bias;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code ConvolutionKernel55D} instance.
	 * 
	 * @return a {@code String} representation of this {@code ConvolutionKernel55D} instance
	 */
	@Override
	public String toString() {
		final String row1 = String.format("%+.10f, %+.10f, %+.10f, %+.10f, %+.10f", Double.valueOf(this.element11), Double.valueOf(this.element12), Double.valueOf(this.element13), Double.valueOf(this.element14), Double.valueOf(this.element15));
		final String row2 = String.format("%+.10f, %+.10f, %+.10f, %+.10f, %+.10f", Double.valueOf(this.element21), Double.valueOf(this.element22), Double.valueOf(this.element23), Double.valueOf(this.element24), Double.valueOf(this.element25));
		final String row3 = String.format("%+.10f, %+.10f, %+.10f, %+.10f, %+.10f", Double.valueOf(this.element31), Double.valueOf(this.element32), Double.valueOf(this.element33), Double.valueOf(this.element34), Double.valueOf(this.element35));
		final String row4 = String.format("%+.10f, %+.10f, %+.10f, %+.10f, %+.10f", Double.valueOf(this.element41), Double.valueOf(this.element42), Double.valueOf(this.element43), Double.valueOf(this.element44), Double.valueOf(this.element45));
		final String row5 = String.format("%+.10f, %+.10f, %+.10f, %+.10f, %+.10f", Double.valueOf(this.element51), Double.valueOf(this.element52), Double.valueOf(this.element53), Double.valueOf(this.element54), Double.valueOf(this.element55));
		
		return String.format("new ConvolutionKernel55D(%s, %s, %s, %s, %s, %+.10f, %+.10f)", row1, row2, row3, row4, row5, Double.valueOf(this.factor), Double.valueOf(this.bias));
	}
	
	/**
	 * Compares {@code object} to this {@code ConvolutionKernel55D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ConvolutionKernel55D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ConvolutionKernel55D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ConvolutionKernel55D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ConvolutionKernel55D)) {
			return false;
		} else if(!equal(this.bias, ConvolutionKernel55D.class.cast(object).bias)) {
			return false;
		} else if(!equal(this.element11, ConvolutionKernel55D.class.cast(object).element11)) {
			return false;
		} else if(!equal(this.element12, ConvolutionKernel55D.class.cast(object).element12)) {
			return false;
		} else if(!equal(this.element13, ConvolutionKernel55D.class.cast(object).element13)) {
			return false;
		} else if(!equal(this.element14, ConvolutionKernel55D.class.cast(object).element14)) {
			return false;
		} else if(!equal(this.element15, ConvolutionKernel55D.class.cast(object).element15)) {
			return false;
		} else if(!equal(this.element21, ConvolutionKernel55D.class.cast(object).element21)) {
			return false;
		} else if(!equal(this.element22, ConvolutionKernel55D.class.cast(object).element22)) {
			return false;
		} else if(!equal(this.element23, ConvolutionKernel55D.class.cast(object).element23)) {
			return false;
		} else if(!equal(this.element24, ConvolutionKernel55D.class.cast(object).element24)) {
			return false;
		} else if(!equal(this.element25, ConvolutionKernel55D.class.cast(object).element25)) {
			return false;
		} else if(!equal(this.element31, ConvolutionKernel55D.class.cast(object).element31)) {
			return false;
		} else if(!equal(this.element32, ConvolutionKernel55D.class.cast(object).element32)) {
			return false;
		} else if(!equal(this.element33, ConvolutionKernel55D.class.cast(object).element33)) {
			return false;
		} else if(!equal(this.element34, ConvolutionKernel55D.class.cast(object).element34)) {
			return false;
		} else if(!equal(this.element35, ConvolutionKernel55D.class.cast(object).element35)) {
			return false;
		} else if(!equal(this.element41, ConvolutionKernel55D.class.cast(object).element41)) {
			return false;
		} else if(!equal(this.element42, ConvolutionKernel55D.class.cast(object).element42)) {
			return false;
		} else if(!equal(this.element43, ConvolutionKernel55D.class.cast(object).element43)) {
			return false;
		} else if(!equal(this.element44, ConvolutionKernel55D.class.cast(object).element44)) {
			return false;
		} else if(!equal(this.element45, ConvolutionKernel55D.class.cast(object).element45)) {
			return false;
		} else if(!equal(this.element51, ConvolutionKernel55D.class.cast(object).element51)) {
			return false;
		} else if(!equal(this.element52, ConvolutionKernel55D.class.cast(object).element52)) {
			return false;
		} else if(!equal(this.element53, ConvolutionKernel55D.class.cast(object).element53)) {
			return false;
		} else if(!equal(this.element54, ConvolutionKernel55D.class.cast(object).element54)) {
			return false;
		} else if(!equal(this.element55, ConvolutionKernel55D.class.cast(object).element55)) {
			return false;
		} else if(!equal(this.factor, ConvolutionKernel55D.class.cast(object).factor)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the bias associated with this {@code ConvolutionKernel55D} instance.
	 * <p>
	 * This is the same as Offset in Gimp.
	 * 
	 * @return the bias associated with this {@code ConvolutionKernel55D} instance
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
	 * Returns the element at index 3 or row 1 and column 4.
	 * 
	 * @return the element at index 3 or row 1 and column 4
	 */
	public double getElement14() {
		return this.element14;
	}
	
	/**
	 * Returns the element at index 4 or row 1 and column 5.
	 * 
	 * @return the element at index 4 or row 1 and column 5
	 */
	public double getElement15() {
		return this.element15;
	}
	
	/**
	 * Returns the element at index 5 or row 2 and column 1.
	 * 
	 * @return the element at index 5 or row 2 and column 1
	 */
	public double getElement21() {
		return this.element21;
	}
	
	/**
	 * Returns the element at index 6 or row 2 and column 2.
	 * 
	 * @return the element at index 6 or row 2 and column 2
	 */
	public double getElement22() {
		return this.element22;
	}
	
	/**
	 * Returns the element at index 7 or row 2 and column 3.
	 * 
	 * @return the element at index 7 or row 2 and column 3
	 */
	public double getElement23() {
		return this.element23;
	}
	
	/**
	 * Returns the element at index 8 or row 2 and column 4.
	 * 
	 * @return the element at index 8 or row 2 and column 4
	 */
	public double getElement24() {
		return this.element24;
	}
	
	/**
	 * Returns the element at index 9 or row 2 and column 5.
	 * 
	 * @return the element at index 9 or row 2 and column 5
	 */
	public double getElement25() {
		return this.element25;
	}
	
	/**
	 * Returns the element at index 10 or row 3 and column 1.
	 * 
	 * @return the element at index 10 or row 3 and column 1
	 */
	public double getElement31() {
		return this.element31;
	}
	
	/**
	 * Returns the element at index 11 or row 3 and column 2.
	 * 
	 * @return the element at index 11 or row 3 and column 2
	 */
	public double getElement32() {
		return this.element32;
	}
	
	/**
	 * Returns the element at index 12 or row 3 and column 3.
	 * 
	 * @return the element at index 12 or row 3 and column 3
	 */
	public double getElement33() {
		return this.element33;
	}
	
	/**
	 * Returns the element at index 13 or row 3 and column 4.
	 * 
	 * @return the element at index 13 or row 3 and column 4
	 */
	public double getElement34() {
		return this.element34;
	}
	
	/**
	 * Returns the element at index 14 or row 3 and column 5.
	 * 
	 * @return the element at index 14 or row 3 and column 5
	 */
	public double getElement35() {
		return this.element35;
	}
	
	/**
	 * Returns the element at index 15 or row 4 and column 1.
	 * 
	 * @return the element at index 15 or row 4 and column 1
	 */
	public double getElement41() {
		return this.element41;
	}
	
	/**
	 * Returns the element at index 16 or row 4 and column 2.
	 * 
	 * @return the element at index 16 or row 4 and column 2
	 */
	public double getElement42() {
		return this.element42;
	}
	
	/**
	 * Returns the element at index 17 or row 4 and column 3.
	 * 
	 * @return the element at index 17 or row 4 and column 3
	 */
	public double getElement43() {
		return this.element43;
	}
	
	/**
	 * Returns the element at index 18 or row 4 and column 4.
	 * 
	 * @return the element at index 18 or row 4 and column 4
	 */
	public double getElement44() {
		return this.element44;
	}
	
	/**
	 * Returns the element at index 19 or row 4 and column 5.
	 * 
	 * @return the element at index 19 or row 4 and column 5
	 */
	public double getElement45() {
		return this.element45;
	}
	
	/**
	 * Returns the element at index 20 or row 5 and column 1.
	 * 
	 * @return the element at index 20 or row 5 and column 1
	 */
	public double getElement51() {
		return this.element51;
	}
	
	/**
	 * Returns the element at index 21 or row 5 and column 2.
	 * 
	 * @return the element at index 21 or row 5 and column 2
	 */
	public double getElement52() {
		return this.element52;
	}
	
	/**
	 * Returns the element at index 22 or row 5 and column 3.
	 * 
	 * @return the element at index 22 or row 5 and column 3
	 */
	public double getElement53() {
		return this.element53;
	}
	
	/**
	 * Returns the element at index 23 or row 5 and column 4.
	 * 
	 * @return the element at index 23 or row 5 and column 4
	 */
	public double getElement54() {
		return this.element54;
	}
	
	/**
	 * Returns the element at index 24 or row 5 and column 5.
	 * 
	 * @return the element at index 24 or row 5 and column 5
	 */
	public double getElement55() {
		return this.element55;
	}
	
	/**
	 * Returns the factor associated with this {@code ConvolutionKernel55D} instance.
	 * <p>
	 * This is the same as the reciprocal value of the Divisor in Gimp.
	 * 
	 * @return the factor associated with this {@code ConvolutionKernel55D} instance
	 */
	public double getFactor() {
		return this.factor;
	}
	
	/**
	 * Returns a hash code for this {@code ConvolutionKernel55D} instance.
	 * 
	 * @return a hash code for this {@code ConvolutionKernel55D} instance
	 */
	@Override
	public int hashCode() {
		final Double bias = Double.valueOf(this.bias);
		
		final Double element11 = Double.valueOf(this.element11);
		final Double element12 = Double.valueOf(this.element12);
		final Double element13 = Double.valueOf(this.element13);
		final Double element14 = Double.valueOf(this.element14);
		final Double element15 = Double.valueOf(this.element15);
		
		final Double element21 = Double.valueOf(this.element21);
		final Double element22 = Double.valueOf(this.element22);
		final Double element23 = Double.valueOf(this.element23);
		final Double element24 = Double.valueOf(this.element24);
		final Double element25 = Double.valueOf(this.element25);
		
		final Double element31 = Double.valueOf(this.element31);
		final Double element32 = Double.valueOf(this.element32);
		final Double element33 = Double.valueOf(this.element33);
		final Double element34 = Double.valueOf(this.element34);
		final Double element35 = Double.valueOf(this.element35);
		
		final Double element41 = Double.valueOf(this.element41);
		final Double element42 = Double.valueOf(this.element42);
		final Double element43 = Double.valueOf(this.element43);
		final Double element44 = Double.valueOf(this.element44);
		final Double element45 = Double.valueOf(this.element45);
		
		final Double element51 = Double.valueOf(this.element51);
		final Double element52 = Double.valueOf(this.element52);
		final Double element53 = Double.valueOf(this.element53);
		final Double element54 = Double.valueOf(this.element54);
		final Double element55 = Double.valueOf(this.element55);
		
		final Double factor = Double.valueOf(this.factor);
		
		return Objects.hash(bias, element11, element12, element13, element14, element15, element21, element22, element23, element24, element25, element31, element32, element33, element34, element35, element41, element42, element43, element44, element45, element51, element52, element53, element54, element55, factor);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code ConvolutionKernel55D} instance that performs a random effect.
	 * 
	 * @return a {@code ConvolutionKernel55D} instance that performs a random effect
	 */
	public static ConvolutionKernel55D random() {
		final double element11 = doNextDouble() * 2.0D - 1.0D;
		final double element12 = doNextDouble() * 2.0D - 1.0D;
		final double element13 = doNextDouble() * 2.0D - 1.0D;
		final double element14 = doNextDouble() * 2.0D - 1.0D;
		final double element15 = doNextDouble() * 2.0D - 1.0D;
		final double element21 = doNextDouble() * 2.0D - 1.0D;
		final double element22 = doNextDouble() * 2.0D - 1.0D;
		final double element23 = doNextDouble() * 2.0D - 1.0D;
		final double element24 = doNextDouble() * 2.0D - 1.0D;
		final double element25 = doNextDouble() * 2.0D - 1.0D;
		final double element31 = doNextDouble() * 2.0D - 1.0D;
		final double element32 = doNextDouble() * 2.0D - 1.0D;
		final double element33 = 1.0D;
		final double element34 = doNextDouble() * 2.0D - 1.0D;
		final double element35 = doNextDouble() * 2.0D - 1.0D;
		final double element41 = doNextDouble() * 2.0D - 1.0D;
		final double element42 = doNextDouble() * 2.0D - 1.0D;
		final double element43 = doNextDouble() * 2.0D - 1.0D;
		final double element44 = doNextDouble() * 2.0D - 1.0D;
		final double element45 = doNextDouble() * 2.0D - 1.0D;
		final double element51 = doNextDouble() * 2.0D - 1.0D;
		final double element52 = doNextDouble() * 2.0D - 1.0D;
		final double element53 = doNextDouble() * 2.0D - 1.0D;
		final double element54 = doNextDouble() * 2.0D - 1.0D;
		final double element55 = doNextDouble() * 2.0D - 1.0D;
		
		final double elementTotal = element11 + element12 + element13 + element14 + element15 + element21 + element22 + element23 + element24 + element25 + element31 + element32 + element33 + element34 + element35 + element41 + element42 + element43 + element44 + element45 + element51 + element52 + element53 + element54 + element55;
		
		final boolean isFactorBasedOnElementTotal = doNextBoolean();
		final boolean isFactorBasedOnRandomFloat = doNextBoolean();
		final boolean isBiasBasedOnRandomFloat = doNextBoolean();
		
		final double factorBasedOnElementTotal = isZero(elementTotal) ? 1.0D : 1.0D / elementTotal;
		
		final double factor = isFactorBasedOnElementTotal ? factorBasedOnElementTotal : isFactorBasedOnRandomFloat ? doNextDouble() : 1.0D;
		final double bias = isBiasBasedOnRandomFloat ? doNextDouble() : 0.0D;
		
		return new ConvolutionKernel55D(element11, element12, element13, element14, element15, element21, element22, element23, element24, element25, element31, element32, element33, element34, element35, element41, element42, element43, element44, element45, element51, element52, element53, element54, element55, factor, bias);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static boolean doNextBoolean() {
		return ThreadLocalRandom.current().nextBoolean();
	}
	
	private static double doNextDouble() {
		return ThreadLocalRandom.current().nextDouble();
	}
}