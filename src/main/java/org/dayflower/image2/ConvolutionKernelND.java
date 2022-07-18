/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
package org.dayflower.image2;

import java.lang.reflect.Field;//TODO: Add Javadocs!
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

//TODO: Add Javadocs!
public final class ConvolutionKernelND {
//	TODO: Add Javadocs!
	public static final ConvolutionKernelND BOX_BLUR_3 = new ConvolutionKernelND(0.0D, 1.0D / 9.0D, new double[] {1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D, 1.0D});
	
//	TODO: Add Javadocs!
	public static final ConvolutionKernelND EMBOSS_3 = new ConvolutionKernelND(0.5D, 1.0D, new double[] {-1.0D, -1.0D, 0.0D, -1.0D, 0.0D, 1.0D, 0.0D, 1.0D, 1.0D});
	
//	TODO: Add Javadocs!
	public static final ConvolutionKernelND GAUSSIAN_BLUR_3 = new ConvolutionKernelND(0.0D, 1.0D / 16.0D, new double[] {1.0D, 2.0D, 1.0D, 2.0D, 4.0D, 2.0D, 1.0D, 2.0D, 1.0D});
	
//	TODO: Add Javadocs!
	public static final ConvolutionKernelND GAUSSIAN_BLUR_5 = new ConvolutionKernelND(0.0D, 1.0D / 256.0D, new double[] {1.0D, 4.0D, 6.0D, 4.0D, 1.0D, 4.0D, 16.0D, 24.0D, 16.0D, 4.0D, 6.0D, 24.0D, 36.0D, 24.0D, 6.0D, 4.0D, 16.0D, 24.0D, 16.0D, 4.0D, 1.0D, 4.0D, 6.0D, 4.0D, 1.0D});
	
//	TODO: Add Javadocs!
	public static final ConvolutionKernelND GRADIENT_HORIZONTAL_3 = new ConvolutionKernelND(0.0D, 1.0D, new double[] {-1.0D, -1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D});
	
//	TODO: Add Javadocs!
	public static final ConvolutionKernelND GRADIENT_VERTICAL_3 = new ConvolutionKernelND(0.0D, 1.0D, new double[] {-1.0D, 0.0D, 1.0D, -1.0D, 0.0D, 1.0D, -1.0D, 0.0D, 1.0D});
	
//	TODO: Add Javadocs!
	public static final ConvolutionKernelND IDENTITY_3 = new ConvolutionKernelND(0.0D, 1.0D, new double[] {0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D});
	
//	TODO: Add Javadocs!
	public static final ConvolutionKernelND IDENTITY_5 = new ConvolutionKernelND(0.0D, 1.0D, new double[] {0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D});
	
//	TODO: Add Javadocs!
	public static final ConvolutionKernelND RIDGE_DETECTION_3 = new ConvolutionKernelND(0.0D, 1.0D, new double[] {-1.0D, -1.0D, -1.0D, -1.0D, 8.0D, -1.0D, -1.0D, -1.0D, -1.0D});
	
//	TODO: Add Javadocs!
	public static final ConvolutionKernelND SHARPEN_3 = new ConvolutionKernelND(0.0D, 1.0D, new double[] {-1.0D, -1.0D, -1.0D, -1.0D, 9.0D, -1.0D, -1.0D, -1.0D, -1.0D});
	
//	TODO: Add Javadocs!
	public static final ConvolutionKernelND UNSHARP_MASKING_5 = new ConvolutionKernelND(0.0D, -1.0D / 256.0D, new double[] {1.0D, 4.0D, 6.0D, 4.0D, 1.0D, 4.0D, 16.0D, 24.0D, 16.0D, 4.0D, 6.0D, 24.0D, -476.0D, 24.0D, 6.0D, 4.0D, 16.0D, 24.0D, 16.0D, 4.0D, 1.0D, 4.0D, 6.0D, 4.0D, 1.0D});
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final double bias;
	private final double factor;
	private final double[] elements;
	private final int resolution;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public ConvolutionKernelND(final double bias, final double factor, final double[] elements) {
		this.bias = bias;
		this.factor = factor;
		this.elements = doRequireValidElements(elements);
		this.resolution = (int)(Math.sqrt(this.elements.length));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ConvolutionKernelND)) {
			return false;
		} else if(Double.compare(this.bias, ConvolutionKernelND.class.cast(object).bias) != 0) {
			return false;
		} else if(Double.compare(this.factor, ConvolutionKernelND.class.cast(object).factor) != 0) {
			return false;
		} else if(!Arrays.equals(this.elements, ConvolutionKernelND.class.cast(object).elements)) {
			return false;
		} else if(this.resolution != ConvolutionKernelND.class.cast(object).resolution) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
	public double getBias() {
		return this.bias;
	}
	
//	TODO: Add Javadocs!
	public double getFactor() {
		return this.factor;
	}
	
//	TODO: Add Javadocs!
	public double[] getElements() {
		return this.elements.clone();
	}
	
//	TODO: Add Javadocs!
	public int getResolution() {
		return this.resolution;
	}
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(this.bias), Double.valueOf(this.factor), Integer.valueOf(Arrays.hashCode(this.elements)), Integer.valueOf(this.resolution));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static ConvolutionKernelND random(final int resolution) {
		if(resolution < 1) {
			throw new IllegalArgumentException(String.format("The value of resolution, %d, is invalid. It must be greater than or equal to 1.", Integer.valueOf(resolution)));
		}
		
		if(resolution % 2 == 0) {
			throw new IllegalArgumentException(String.format("The value of resolution, %d, is invalid. It cannot be even.", Integer.valueOf(resolution)));
		}
		
		if(resolution * resolution < 1) {
			throw new IllegalArgumentException(String.format("The value of resolution * resolution, %d, is invalid. It must be greater than or equal to 1.", Integer.valueOf(resolution * resolution)));
		}
		
		final double[] elements = new double[resolution * resolution];
		
		final int middle = (resolution - 1) / 2;
		
		for(int y = 0; y < resolution; y++) {
			for(int x = 0; x < resolution; x++) {
				if(x == middle && y == middle) {
					elements[y * resolution + x] = 1.0D;
				} else {
					elements[y * resolution + x] = doNextDouble(-1.0D, 1.0D);
				}
			}
		}
		
		final double elementTotal = Arrays.stream(elements).sum();
		
		final boolean isBiasBasedOnRandomDouble = doNextBoolean();
		final boolean isFactorBasedOnElementTotal = doNextBoolean();
		final boolean isFactorBasedOnRandomDouble = doNextBoolean();
		
		final double bias = isBiasBasedOnRandomDouble ? doNextDouble() : 0.0D;
		final double factor = isFactorBasedOnElementTotal ? doIsZero(elementTotal) ? 1.0D : 1.0D / elementTotal : isFactorBasedOnRandomDouble ? doNextDouble() : 1.0D;
		
		return new ConvolutionKernelND(bias, factor, elements);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static boolean doIsZero(final double value) {
		return Double.compare(+0.0D, value) == 0 || Double.compare(-0.0D, value) == 0;
	}
	
	private static boolean doNextBoolean() {
		return ThreadLocalRandom.current().nextBoolean();
	}
	
	private static double doNextDouble() {
		return ThreadLocalRandom.current().nextDouble();
	}
	
	private static double doNextDouble(final double origin, final double bound) {
		return ThreadLocalRandom.current().nextDouble(origin, bound);
	}
	
	private static double[] doRequireValidElements(final double[] elements) {
//		Check that 'elements' is not 'null':
		Objects.requireNonNull(elements, "elements == null");
		
//		Check that 'elements.length' is odd:
		if(elements.length % 2 == 0) {
			throw new IllegalArgumentException(String.format("The value of elements.length, %d, is invalid. It cannot be even.", Integer.valueOf(elements.length)));
		}
		
//		Compute the square root of 'elements.length' and the value that is closest to it and represents a mathematical integer:
		final double vA = Math.sqrt(elements.length);
		final double vB = Math.rint(vA);
		
//		Check that the square root of 'elements.length' and the value that is closest to it and represents a mathematical integer are equal:
		if(Double.compare(vA, vB) != 0) {
			throw new IllegalArgumentException(String.format("The value of elements.length, %d, is invalid. Math.sqrt(elements.length) must return a value that is a mathematical integer.", Integer.valueOf(elements.length)));
		}
		
//		Compute the resolution by converting the square root of 'elements.length' to an 'int':
		final int kR = (int)(vA);
		
//		Check that the resolution is odd:
		if(kR % 2 == 0) {
			throw new IllegalArgumentException(String.format("The value of elements.length, %d, is invalid. Math.sqrt(elements.length) must return a value that is odd.", Integer.valueOf(elements.length)));
		}
		
		return elements.clone();
	}
}