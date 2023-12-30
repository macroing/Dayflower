/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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

import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import org.macroing.java.lang.Floats;

/**
 * A {@code ConvolutionKernel55F} is a convolution kernel with 25 {@code float}-based elements in five rows and five columns.
 * <p>
 * This class is immutable and therefore suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ConvolutionKernel55F {
	/**
	 * A {@code ConvolutionKernel55F} instance that performs a Gaussian blur effect.
	 */
//	TODO: Add Unit Tests!
	public static final ConvolutionKernel55F GAUSSIAN_BLUR = new ConvolutionKernel55F(1.0F, 4.0F, 6.0F, 4.0F, 1.0F, 4.0F, 16.0F, 24.0F, 16.0F, 4.0F, 6.0F, 24.0F, 36.0F, 24.0F, 6.0F, 4.0F, 16.0F, 24.0F, 16.0F, 4.0F, 1.0F, 4.0F, 6.0F, 4.0F, 1.0F, 1.0F / 256.0F, 0.0F);
	
	/**
	 * A {@code ConvolutionKernel55F} instance that performs no effect.
	 */
//	TODO: Add Unit Tests!
	public static final ConvolutionKernel55F IDENTITY = new ConvolutionKernel55F(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
	
	/**
	 * A {@code ConvolutionKernel55F} instance that performs an unsharp masking effect.
	 */
//	TODO: Add Unit Tests!
	public static final ConvolutionKernel55F UNSHARP_MASKING = new ConvolutionKernel55F(1.0F, 4.0F, 6.0F, 4.0F, 1.0F, 4.0F, 16.0F, 24.0F, 16.0F, 4.0F, 6.0F, 24.0F, -476.0F, 24.0F, 6.0F, 4.0F, 16.0F, 24.0F, 16.0F, 4.0F, 1.0F, 4.0F, 6.0F, 4.0F, 1.0F, -1.0F / 256.0F, 0.0F);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final float bias;
	private final float element11;
	private final float element12;
	private final float element13;
	private final float element14;
	private final float element15;
	private final float element21;
	private final float element22;
	private final float element23;
	private final float element24;
	private final float element25;
	private final float element31;
	private final float element32;
	private final float element33;
	private final float element34;
	private final float element35;
	private final float element41;
	private final float element42;
	private final float element43;
	private final float element44;
	private final float element45;
	private final float element51;
	private final float element52;
	private final float element53;
	private final float element54;
	private final float element55;
	private final float factor;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ConvolutionKernel55F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ConvolutionKernel55F(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
	 * }
	 * </pre>
	 */
//	TODO: Add Unit Tests!
	public ConvolutionKernel55F() {
		this(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
	}
	
	/**
	 * Constructs a new {@code ConvolutionKernel55F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ConvolutionKernel55F(element11, element12, element13, element14, element15, element21, element22, element23, element24, element25, element31, element32, element33, element34, element35, element41, element42, element43, element44, element45, element51, element52, element53, element54, element55, 1.0F, 0.0F);
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
//	TODO: Add Unit Tests!
	public ConvolutionKernel55F(final float element11, final float element12, final float element13, final float element14, final float element15, final float element21, final float element22, final float element23, final float element24, final float element25, final float element31, final float element32, final float element33, final float element34, final float element35, final float element41, final float element42, final float element43, final float element44, final float element45, final float element51, final float element52, final float element53, final float element54, final float element55) {
		this(element11, element12, element13, element14, element15, element21, element22, element23, element24, element25, element31, element32, element33, element34, element35, element41, element42, element43, element44, element45, element51, element52, element53, element54, element55, 1.0F, 0.0F);
	}
	
	/**
	 * Constructs a new {@code ConvolutionKernel55F} instance.
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
//	TODO: Add Unit Tests!
	public ConvolutionKernel55F(final float element11, final float element12, final float element13, final float element14, final float element15, final float element21, final float element22, final float element23, final float element24, final float element25, final float element31, final float element32, final float element33, final float element34, final float element35, final float element41, final float element42, final float element43, final float element44, final float element45, final float element51, final float element52, final float element53, final float element54, final float element55, final float factor, final float bias) {
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
	 * Returns a {@code String} representation of this {@code ConvolutionKernel55F} instance.
	 * 
	 * @return a {@code String} representation of this {@code ConvolutionKernel55F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		final String row1 = String.format("%+.10f, %+.10f, %+.10f, %+.10f, %+.10f", Float.valueOf(this.element11), Float.valueOf(this.element12), Float.valueOf(this.element13), Float.valueOf(this.element14), Float.valueOf(this.element15));
		final String row2 = String.format("%+.10f, %+.10f, %+.10f, %+.10f, %+.10f", Float.valueOf(this.element21), Float.valueOf(this.element22), Float.valueOf(this.element23), Float.valueOf(this.element24), Float.valueOf(this.element25));
		final String row3 = String.format("%+.10f, %+.10f, %+.10f, %+.10f, %+.10f", Float.valueOf(this.element31), Float.valueOf(this.element32), Float.valueOf(this.element33), Float.valueOf(this.element34), Float.valueOf(this.element35));
		final String row4 = String.format("%+.10f, %+.10f, %+.10f, %+.10f, %+.10f", Float.valueOf(this.element41), Float.valueOf(this.element42), Float.valueOf(this.element43), Float.valueOf(this.element44), Float.valueOf(this.element45));
		final String row5 = String.format("%+.10f, %+.10f, %+.10f, %+.10f, %+.10f", Float.valueOf(this.element51), Float.valueOf(this.element52), Float.valueOf(this.element53), Float.valueOf(this.element54), Float.valueOf(this.element55));
		
		return String.format("new ConvolutionKernel55F(%s, %s, %s, %s, %s, %+.10f, %+.10f)", row1, row2, row3, row4, row5, Float.valueOf(this.factor), Float.valueOf(this.bias));
	}
	
	/**
	 * Compares {@code object} to this {@code ConvolutionKernel55F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ConvolutionKernel55F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ConvolutionKernel55F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ConvolutionKernel55F}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ConvolutionKernel55F)) {
			return false;
		} else if(!Floats.equals(this.bias, ConvolutionKernel55F.class.cast(object).bias)) {
			return false;
		} else if(!Floats.equals(this.element11, ConvolutionKernel55F.class.cast(object).element11)) {
			return false;
		} else if(!Floats.equals(this.element12, ConvolutionKernel55F.class.cast(object).element12)) {
			return false;
		} else if(!Floats.equals(this.element13, ConvolutionKernel55F.class.cast(object).element13)) {
			return false;
		} else if(!Floats.equals(this.element14, ConvolutionKernel55F.class.cast(object).element14)) {
			return false;
		} else if(!Floats.equals(this.element15, ConvolutionKernel55F.class.cast(object).element15)) {
			return false;
		} else if(!Floats.equals(this.element21, ConvolutionKernel55F.class.cast(object).element21)) {
			return false;
		} else if(!Floats.equals(this.element22, ConvolutionKernel55F.class.cast(object).element22)) {
			return false;
		} else if(!Floats.equals(this.element23, ConvolutionKernel55F.class.cast(object).element23)) {
			return false;
		} else if(!Floats.equals(this.element24, ConvolutionKernel55F.class.cast(object).element24)) {
			return false;
		} else if(!Floats.equals(this.element25, ConvolutionKernel55F.class.cast(object).element25)) {
			return false;
		} else if(!Floats.equals(this.element31, ConvolutionKernel55F.class.cast(object).element31)) {
			return false;
		} else if(!Floats.equals(this.element32, ConvolutionKernel55F.class.cast(object).element32)) {
			return false;
		} else if(!Floats.equals(this.element33, ConvolutionKernel55F.class.cast(object).element33)) {
			return false;
		} else if(!Floats.equals(this.element34, ConvolutionKernel55F.class.cast(object).element34)) {
			return false;
		} else if(!Floats.equals(this.element35, ConvolutionKernel55F.class.cast(object).element35)) {
			return false;
		} else if(!Floats.equals(this.element41, ConvolutionKernel55F.class.cast(object).element41)) {
			return false;
		} else if(!Floats.equals(this.element42, ConvolutionKernel55F.class.cast(object).element42)) {
			return false;
		} else if(!Floats.equals(this.element43, ConvolutionKernel55F.class.cast(object).element43)) {
			return false;
		} else if(!Floats.equals(this.element44, ConvolutionKernel55F.class.cast(object).element44)) {
			return false;
		} else if(!Floats.equals(this.element45, ConvolutionKernel55F.class.cast(object).element45)) {
			return false;
		} else if(!Floats.equals(this.element51, ConvolutionKernel55F.class.cast(object).element51)) {
			return false;
		} else if(!Floats.equals(this.element52, ConvolutionKernel55F.class.cast(object).element52)) {
			return false;
		} else if(!Floats.equals(this.element53, ConvolutionKernel55F.class.cast(object).element53)) {
			return false;
		} else if(!Floats.equals(this.element54, ConvolutionKernel55F.class.cast(object).element54)) {
			return false;
		} else if(!Floats.equals(this.element55, ConvolutionKernel55F.class.cast(object).element55)) {
			return false;
		} else if(!Floats.equals(this.factor, ConvolutionKernel55F.class.cast(object).factor)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the bias associated with this {@code ConvolutionKernel55F} instance.
	 * <p>
	 * This is the same as Offset in Gimp.
	 * 
	 * @return the bias associated with this {@code ConvolutionKernel55F} instance
	 */
//	TODO: Add Unit Tests!
	public float getBias() {
		return this.bias;
	}
	
	/**
	 * Returns the element at index 0 or row 1 and column 1.
	 * 
	 * @return the element at index 0 or row 1 and column 1
	 */
//	TODO: Add Unit Tests!
	public float getElement11() {
		return this.element11;
	}
	
	/**
	 * Returns the element at index 1 or row 1 and column 2.
	 * 
	 * @return the element at index 1 or row 1 and column 2
	 */
//	TODO: Add Unit Tests!
	public float getElement12() {
		return this.element12;
	}
	
	/**
	 * Returns the element at index 2 or row 1 and column 3.
	 * 
	 * @return the element at index 2 or row 1 and column 3
	 */
//	TODO: Add Unit Tests!
	public float getElement13() {
		return this.element13;
	}
	
	/**
	 * Returns the element at index 3 or row 1 and column 4.
	 * 
	 * @return the element at index 3 or row 1 and column 4
	 */
//	TODO: Add Unit Tests!
	public float getElement14() {
		return this.element14;
	}
	
	/**
	 * Returns the element at index 4 or row 1 and column 5.
	 * 
	 * @return the element at index 4 or row 1 and column 5
	 */
//	TODO: Add Unit Tests!
	public float getElement15() {
		return this.element15;
	}
	
	/**
	 * Returns the element at index 5 or row 2 and column 1.
	 * 
	 * @return the element at index 5 or row 2 and column 1
	 */
//	TODO: Add Unit Tests!
	public float getElement21() {
		return this.element21;
	}
	
	/**
	 * Returns the element at index 6 or row 2 and column 2.
	 * 
	 * @return the element at index 6 or row 2 and column 2
	 */
//	TODO: Add Unit Tests!
	public float getElement22() {
		return this.element22;
	}
	
	/**
	 * Returns the element at index 7 or row 2 and column 3.
	 * 
	 * @return the element at index 7 or row 2 and column 3
	 */
//	TODO: Add Unit Tests!
	public float getElement23() {
		return this.element23;
	}
	
	/**
	 * Returns the element at index 8 or row 2 and column 4.
	 * 
	 * @return the element at index 8 or row 2 and column 4
	 */
//	TODO: Add Unit Tests!
	public float getElement24() {
		return this.element24;
	}
	
	/**
	 * Returns the element at index 9 or row 2 and column 5.
	 * 
	 * @return the element at index 9 or row 2 and column 5
	 */
//	TODO: Add Unit Tests!
	public float getElement25() {
		return this.element25;
	}
	
	/**
	 * Returns the element at index 10 or row 3 and column 1.
	 * 
	 * @return the element at index 10 or row 3 and column 1
	 */
//	TODO: Add Unit Tests!
	public float getElement31() {
		return this.element31;
	}
	
	/**
	 * Returns the element at index 11 or row 3 and column 2.
	 * 
	 * @return the element at index 11 or row 3 and column 2
	 */
//	TODO: Add Unit Tests!
	public float getElement32() {
		return this.element32;
	}
	
	/**
	 * Returns the element at index 12 or row 3 and column 3.
	 * 
	 * @return the element at index 12 or row 3 and column 3
	 */
//	TODO: Add Unit Tests!
	public float getElement33() {
		return this.element33;
	}
	
	/**
	 * Returns the element at index 13 or row 3 and column 4.
	 * 
	 * @return the element at index 13 or row 3 and column 4
	 */
//	TODO: Add Unit Tests!
	public float getElement34() {
		return this.element34;
	}
	
	/**
	 * Returns the element at index 14 or row 3 and column 5.
	 * 
	 * @return the element at index 14 or row 3 and column 5
	 */
//	TODO: Add Unit Tests!
	public float getElement35() {
		return this.element35;
	}
	
	/**
	 * Returns the element at index 15 or row 4 and column 1.
	 * 
	 * @return the element at index 15 or row 4 and column 1
	 */
//	TODO: Add Unit Tests!
	public float getElement41() {
		return this.element41;
	}
	
	/**
	 * Returns the element at index 16 or row 4 and column 2.
	 * 
	 * @return the element at index 16 or row 4 and column 2
	 */
//	TODO: Add Unit Tests!
	public float getElement42() {
		return this.element42;
	}
	
	/**
	 * Returns the element at index 17 or row 4 and column 3.
	 * 
	 * @return the element at index 17 or row 4 and column 3
	 */
//	TODO: Add Unit Tests!
	public float getElement43() {
		return this.element43;
	}
	
	/**
	 * Returns the element at index 18 or row 4 and column 4.
	 * 
	 * @return the element at index 18 or row 4 and column 4
	 */
//	TODO: Add Unit Tests!
	public float getElement44() {
		return this.element44;
	}
	
	/**
	 * Returns the element at index 19 or row 4 and column 5.
	 * 
	 * @return the element at index 19 or row 4 and column 5
	 */
//	TODO: Add Unit Tests!
	public float getElement45() {
		return this.element45;
	}
	
	/**
	 * Returns the element at index 20 or row 5 and column 1.
	 * 
	 * @return the element at index 20 or row 5 and column 1
	 */
//	TODO: Add Unit Tests!
	public float getElement51() {
		return this.element51;
	}
	
	/**
	 * Returns the element at index 21 or row 5 and column 2.
	 * 
	 * @return the element at index 21 or row 5 and column 2
	 */
//	TODO: Add Unit Tests!
	public float getElement52() {
		return this.element52;
	}
	
	/**
	 * Returns the element at index 22 or row 5 and column 3.
	 * 
	 * @return the element at index 22 or row 5 and column 3
	 */
//	TODO: Add Unit Tests!
	public float getElement53() {
		return this.element53;
	}
	
	/**
	 * Returns the element at index 23 or row 5 and column 4.
	 * 
	 * @return the element at index 23 or row 5 and column 4
	 */
//	TODO: Add Unit Tests!
	public float getElement54() {
		return this.element54;
	}
	
	/**
	 * Returns the element at index 24 or row 5 and column 5.
	 * 
	 * @return the element at index 24 or row 5 and column 5
	 */
//	TODO: Add Unit Tests!
	public float getElement55() {
		return this.element55;
	}
	
	/**
	 * Returns the factor associated with this {@code ConvolutionKernel55F} instance.
	 * <p>
	 * This is the same as the reciprocal value of the Divisor in Gimp.
	 * 
	 * @return the factor associated with this {@code ConvolutionKernel55F} instance
	 */
//	TODO: Add Unit Tests!
	public float getFactor() {
		return this.factor;
	}
	
	/**
	 * Returns a hash code for this {@code ConvolutionKernel55F} instance.
	 * 
	 * @return a hash code for this {@code ConvolutionKernel55F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		final Float bias = Float.valueOf(this.bias);
		
		final Float element11 = Float.valueOf(this.element11);
		final Float element12 = Float.valueOf(this.element12);
		final Float element13 = Float.valueOf(this.element13);
		final Float element14 = Float.valueOf(this.element14);
		final Float element15 = Float.valueOf(this.element15);
		
		final Float element21 = Float.valueOf(this.element21);
		final Float element22 = Float.valueOf(this.element22);
		final Float element23 = Float.valueOf(this.element23);
		final Float element24 = Float.valueOf(this.element24);
		final Float element25 = Float.valueOf(this.element25);
		
		final Float element31 = Float.valueOf(this.element31);
		final Float element32 = Float.valueOf(this.element32);
		final Float element33 = Float.valueOf(this.element33);
		final Float element34 = Float.valueOf(this.element34);
		final Float element35 = Float.valueOf(this.element35);
		
		final Float element41 = Float.valueOf(this.element41);
		final Float element42 = Float.valueOf(this.element42);
		final Float element43 = Float.valueOf(this.element43);
		final Float element44 = Float.valueOf(this.element44);
		final Float element45 = Float.valueOf(this.element45);
		
		final Float element51 = Float.valueOf(this.element51);
		final Float element52 = Float.valueOf(this.element52);
		final Float element53 = Float.valueOf(this.element53);
		final Float element54 = Float.valueOf(this.element54);
		final Float element55 = Float.valueOf(this.element55);
		
		final Float factor = Float.valueOf(this.factor);
		
		return Objects.hash(bias, element11, element12, element13, element14, element15, element21, element22, element23, element24, element25, element31, element32, element33, element34, element35, element41, element42, element43, element44, element45, element51, element52, element53, element54, element55, factor);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code ConvolutionKernel55F} instance that performs a random effect.
	 * 
	 * @return a {@code ConvolutionKernel55F} instance that performs a random effect
	 */
//	TODO: Add Unit Tests!
	public static ConvolutionKernel55F random() {
		final float element11 = doNextFloat() * 2.0F - 1.0F;
		final float element12 = doNextFloat() * 2.0F - 1.0F;
		final float element13 = doNextFloat() * 2.0F - 1.0F;
		final float element14 = doNextFloat() * 2.0F - 1.0F;
		final float element15 = doNextFloat() * 2.0F - 1.0F;
		final float element21 = doNextFloat() * 2.0F - 1.0F;
		final float element22 = doNextFloat() * 2.0F - 1.0F;
		final float element23 = doNextFloat() * 2.0F - 1.0F;
		final float element24 = doNextFloat() * 2.0F - 1.0F;
		final float element25 = doNextFloat() * 2.0F - 1.0F;
		final float element31 = doNextFloat() * 2.0F - 1.0F;
		final float element32 = doNextFloat() * 2.0F - 1.0F;
		final float element33 = 1.0F;
		final float element34 = doNextFloat() * 2.0F - 1.0F;
		final float element35 = doNextFloat() * 2.0F - 1.0F;
		final float element41 = doNextFloat() * 2.0F - 1.0F;
		final float element42 = doNextFloat() * 2.0F - 1.0F;
		final float element43 = doNextFloat() * 2.0F - 1.0F;
		final float element44 = doNextFloat() * 2.0F - 1.0F;
		final float element45 = doNextFloat() * 2.0F - 1.0F;
		final float element51 = doNextFloat() * 2.0F - 1.0F;
		final float element52 = doNextFloat() * 2.0F - 1.0F;
		final float element53 = doNextFloat() * 2.0F - 1.0F;
		final float element54 = doNextFloat() * 2.0F - 1.0F;
		final float element55 = doNextFloat() * 2.0F - 1.0F;
		
		final float elementTotal = element11 + element12 + element13 + element14 + element15 + element21 + element22 + element23 + element24 + element25 + element31 + element32 + element33 + element34 + element35 + element41 + element42 + element43 + element44 + element45 + element51 + element52 + element53 + element54 + element55;
		
		final boolean isFactorBasedOnElementTotal = doNextBoolean();
		final boolean isFactorBasedOnRandomFloat = doNextBoolean();
		final boolean isBiasBasedOnRandomFloat = doNextBoolean();
		
		final float factorBasedOnElementTotal = Floats.isZero(elementTotal) ? 1.0F : 1.0F / elementTotal;
		
		final float factor = isFactorBasedOnElementTotal ? factorBasedOnElementTotal : isFactorBasedOnRandomFloat ? doNextFloat() : 1.0F;
		final float bias = isBiasBasedOnRandomFloat ? doNextFloat() : 0.0F;
		
		return new ConvolutionKernel55F(element11, element12, element13, element14, element15, element21, element22, element23, element24, element25, element31, element32, element33, element34, element35, element41, element42, element43, element44, element45, element51, element52, element53, element54, element55, factor, bias);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static boolean doNextBoolean() {
		return ThreadLocalRandom.current().nextBoolean();
	}
	
	private static float doNextFloat() {
		return ThreadLocalRandom.current().nextFloat();
	}
}