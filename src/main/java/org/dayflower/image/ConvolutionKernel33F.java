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
package org.dayflower.image;

import static org.dayflower.util.Floats.equal;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A {@code ConvolutionKernel33F} is a convolution kernel with three rows and three columns.
 * <p>
 * This class is immutable and therefore suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ConvolutionKernel33F {
	/**
	 * A {@code ConvolutionKernel33F} instance that performs a box blur effect.
	 */
	public static ConvolutionKernel33F BOX_BLUR = new ConvolutionKernel33F(1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F / 9.0F, 0.0F);
	
	/**
	 * A {@code ConvolutionKernel33F} instance that performs an edge detection effect.
	 */
	public static ConvolutionKernel33F EDGE_DETECTION = new ConvolutionKernel33F(-1.0F, -1.0F, -1.0F, -1.0F, 8.0F, -1.0F, -1.0F, -1.0F, -1.0F);
	
	/**
	 * A {@code ConvolutionKernel33F} instance that performs an emboss effect.
	 */
	public static ConvolutionKernel33F EMBOSS = new ConvolutionKernel33F(-1.0F, -1.0F, 0.0F, -1.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.5F);
	
	/**
	 * A {@code ConvolutionKernel33F} instance that performs a Gaussian blur effect.
	 */
	public static ConvolutionKernel33F GAUSSIAN_BLUR = new ConvolutionKernel33F(1.0F, 2.0F, 1.0F, 2.0F, 4.0F, 2.0F, 1.0F, 2.0F, 1.0F, 1.0F / 16.0F, 0.0F);
	
	/**
	 * A {@code ConvolutionKernel33F} instance that performs a gradient effect in the horizontal direction.
	 */
	public static ConvolutionKernel33F GRADIENT_HORIZONTAL = new ConvolutionKernel33F(-1.0F, -1.0F, -1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	
	/**
	 * A {@code ConvolutionKernel33F} instance that performs a gradient effect in the vertical direction.
	 */
	public static ConvolutionKernel33F GRADIENT_VERTICAL = new ConvolutionKernel33F(-1.0F, 0.0F, 1.0F, -1.0F, 0.0F, 1.0F, -1.0F, 0.0F, 1.0F);
	
	/**
	 * A {@code ConvolutionKernel33F} instance that performs a sharpen effect.
	 */
	public static ConvolutionKernel33F SHARPEN = new ConvolutionKernel33F(-1.0F, -1.0F, -1.0F, -1.0F, 9.0F, -1.0F, -1.0F, -1.0F, -1.0F);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final float bias;
	private final float element11;
	private final float element12;
	private final float element13;
	private final float element21;
	private final float element22;
	private final float element23;
	private final float element31;
	private final float element32;
	private final float element33;
	private final float factor;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ConvolutionKernel33F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ConvolutionKernel33F(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F)
	 * }
	 * </pre>
	 */
	public ConvolutionKernel33F() {
		this(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F);
	}
	
	/**
	 * Constructs a new {@code ConvolutionKernel33F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ConvolutionKernel33F(element11, element12, element13, element21, element22, element23, element31, element32, element33, 1.0F, 0.0F)
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
	public ConvolutionKernel33F(final float element11, final float element12, final float element13, final float element21, final float element22, final float element23, final float element31, final float element32, final float element33) {
		this(element11, element12, element13, element21, element22, element23, element31, element32, element33, 1.0F, 0.0F);
	}
	
	/**
	 * Constructs a new {@code ConvolutionKernel33F} instance.
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
	public ConvolutionKernel33F(final float element11, final float element12, final float element13, final float element21, final float element22, final float element23, final float element31, final float element32, final float element33, final float factor, final float bias) {
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
	 * Returns a {@code String} representation of this {@code ConvolutionKernel33F} instance.
	 * 
	 * @return a {@code String} representation of this {@code ConvolutionKernel33F} instance
	 */
	@Override
	public String toString() {
		final String row1 = String.format("%+.10f, %+.10f, %+.10f", Float.valueOf(this.element11), Float.valueOf(this.element12), Float.valueOf(this.element13));
		final String row2 = String.format("%+.10f, %+.10f, %+.10f", Float.valueOf(this.element21), Float.valueOf(this.element22), Float.valueOf(this.element23));
		final String row3 = String.format("%+.10f, %+.10f, %+.10f", Float.valueOf(this.element31), Float.valueOf(this.element32), Float.valueOf(this.element33));
		
		return String.format("new ConvolutionKernel33F(%s, %s, %s, %+.10f, %+.10f)", row1, row2, row3, Float.valueOf(this.factor), Float.valueOf(this.bias));
	}
	
	/**
	 * Compares {@code object} to this {@code ConvolutionKernel33F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ConvolutionKernel33F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ConvolutionKernel33F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ConvolutionKernel33F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ConvolutionKernel33F)) {
			return false;
		} else if(!equal(this.bias, ConvolutionKernel33F.class.cast(object).bias)) {
			return false;
		} else if(!equal(this.element11, ConvolutionKernel33F.class.cast(object).element11)) {
			return false;
		} else if(!equal(this.element12, ConvolutionKernel33F.class.cast(object).element12)) {
			return false;
		} else if(!equal(this.element13, ConvolutionKernel33F.class.cast(object).element13)) {
			return false;
		} else if(!equal(this.element21, ConvolutionKernel33F.class.cast(object).element21)) {
			return false;
		} else if(!equal(this.element22, ConvolutionKernel33F.class.cast(object).element22)) {
			return false;
		} else if(!equal(this.element23, ConvolutionKernel33F.class.cast(object).element23)) {
			return false;
		} else if(!equal(this.element31, ConvolutionKernel33F.class.cast(object).element31)) {
			return false;
		} else if(!equal(this.element32, ConvolutionKernel33F.class.cast(object).element32)) {
			return false;
		} else if(!equal(this.element33, ConvolutionKernel33F.class.cast(object).element33)) {
			return false;
		} else if(!equal(this.factor, ConvolutionKernel33F.class.cast(object).factor)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the bias associated with this {@code ConvolutionKernel33F} instance.
	 * <p>
	 * This is the same as Offset in Gimp.
	 * 
	 * @return the bias associated with this {@code ConvolutionKernel33F} instance
	 */
	public float getBias() {
		return this.bias;
	}
	
	/**
	 * Returns the element at index 0 or row 1 and column 1.
	 * 
	 * @return the element at index 0 or row 1 and column 1
	 */
	public float getElement11() {
		return this.element11;
	}
	
	/**
	 * Returns the element at index 1 or row 1 and column 2.
	 * 
	 * @return the element at index 1 or row 1 and column 2
	 */
	public float getElement12() {
		return this.element12;
	}
	
	/**
	 * Returns the element at index 2 or row 1 and column 3.
	 * 
	 * @return the element at index 2 or row 1 and column 3
	 */
	public float getElement13() {
		return this.element13;
	}
	
	/**
	 * Returns the element at index 3 or row 2 and column 1.
	 * 
	 * @return the element at index 3 or row 2 and column 1
	 */
	public float getElement21() {
		return this.element21;
	}
	
	/**
	 * Returns the element at index 4 or row 2 and column 2.
	 * 
	 * @return the element at index 4 or row 2 and column 2
	 */
	public float getElement22() {
		return this.element22;
	}
	
	/**
	 * Returns the element at index 5 or row 2 and column 3.
	 * 
	 * @return the element at index 5 or row 2 and column 3
	 */
	public float getElement23() {
		return this.element23;
	}
	
	/**
	 * Returns the element at index 6 or row 3 and column 1.
	 * 
	 * @return the element at index 6 or row 3 and column 1
	 */
	public float getElement31() {
		return this.element31;
	}
	
	/**
	 * Returns the element at index 7 or row 3 and column 2.
	 * 
	 * @return the element at index 7 or row 3 and column 2
	 */
	public float getElement32() {
		return this.element32;
	}
	
	/**
	 * Returns the element at index 8 or row 3 and column 3.
	 * 
	 * @return the element at index 8 or row 3 and column 3
	 */
	public float getElement33() {
		return this.element33;
	}
	
	/**
	 * Returns the factor associated with this {@code ConvolutionKernel33F} instance.
	 * <p>
	 * This is the same as the reciprocal value of the Divisor in Gimp.
	 * 
	 * @return the factor associated with this {@code ConvolutionKernel33F} instance
	 */
	public float getFactor() {
		return this.factor;
	}
	
	/**
	 * Returns a hash code for this {@code ConvolutionKernel33F} instance.
	 * 
	 * @return a hash code for this {@code ConvolutionKernel33F} instance
	 */
	@Override
	public int hashCode() {
		final Float bias = Float.valueOf(this.bias);
		
		final Float element11 = Float.valueOf(this.element11);
		final Float element12 = Float.valueOf(this.element12);
		final Float element13 = Float.valueOf(this.element13);
		
		final Float element21 = Float.valueOf(this.element21);
		final Float element22 = Float.valueOf(this.element22);
		final Float element23 = Float.valueOf(this.element23);
		
		final Float element31 = Float.valueOf(this.element31);
		final Float element32 = Float.valueOf(this.element32);
		final Float element33 = Float.valueOf(this.element33);
		
		final Float factor = Float.valueOf(this.factor);
		
		return Objects.hash(bias, element11, element12, element13, element21, element22, element23, element31, element32, element33, factor);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code ConvolutionKernel33F} instance that performs a random effect.
	 * 
	 * @return a {@code ConvolutionKernel33F} instance that performs a random effect
	 */
	public static ConvolutionKernel33F random() {
		final float element11 = doNextFloat() * 2.0F - 1.0F;
		final float element12 = doNextFloat() * 2.0F - 1.0F;
		final float element13 = doNextFloat() * 2.0F - 1.0F;
		final float element21 = doNextFloat() * 2.0F - 1.0F;
		final float element22 = 1.0F;
		final float element23 = doNextFloat() * 2.0F - 1.0F;
		final float element31 = doNextFloat() * 2.0F - 1.0F;
		final float element32 = doNextFloat() * 2.0F - 1.0F;
		final float element33 = doNextFloat() * 2.0F - 1.0F;
		
		final float elementTotal = element11 + element12 + element13 + element21 + element22 + element23 + element31 + element32 + element33;
		
		final boolean isFactorBasedOnElementTotal = doNextBoolean();
		final boolean isFactorBasedOnRandomFloat = doNextBoolean();
		final boolean isBiasBasedOnRandomFloat = doNextBoolean();
		
		final float factorBasedOnElementTotal = equal(elementTotal, 0.0F) ? 1.0F : 1.0F / elementTotal;
		
		final float factor = isFactorBasedOnElementTotal ? factorBasedOnElementTotal : isFactorBasedOnRandomFloat ? doNextFloat() : 1.0F;
		final float bias = isBiasBasedOnRandomFloat ? doNextFloat() : 0.0F;
		
		return new ConvolutionKernel33F(element11, element12, element13, element21, element22, element23, element31, element32, element33, factor, bias);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static boolean doNextBoolean() {
		return ThreadLocalRandom.current().nextBoolean();
	}
	
	private static float doNextFloat() {
		return ThreadLocalRandom.current().nextFloat();
	}
}