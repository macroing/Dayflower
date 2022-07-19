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

import org.dayflower.utility.Floats;

//TODO: Add Javadocs!
public final class ConvolutionKernelNF {
//	TODO: Add Javadocs!
	public static final ConvolutionKernelNF BOX_BLUR_3 = new ConvolutionKernelNF(0.0F, 1.0F / 9.0F, new float[] {1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F});
	
//	TODO: Add Javadocs!
	public static final ConvolutionKernelNF EMBOSS_3 = new ConvolutionKernelNF(0.5F, 1.0F, new float[] {-1.0F, -1.0F, 0.0F, -1.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F});
	
//	TODO: Add Javadocs!
	public static final ConvolutionKernelNF GAUSSIAN_BLUR_3 = new ConvolutionKernelNF(0.0F, 1.0F / 16.0F, new float[] {1.0F, 2.0F, 1.0F, 2.0F, 4.0F, 2.0F, 1.0F, 2.0F, 1.0F});
	
//	TODO: Add Javadocs!
	public static final ConvolutionKernelNF GAUSSIAN_BLUR_5 = new ConvolutionKernelNF(0.0F, 1.0F / 256.0F, new float[] {1.0F, 4.0F, 6.0F, 4.0F, 1.0F, 4.0F, 16.0F, 24.0F, 16.0F, 4.0F, 6.0F, 24.0F, 36.0F, 24.0F, 6.0F, 4.0F, 16.0F, 24.0F, 16.0F, 4.0F, 1.0F, 4.0F, 6.0F, 4.0F, 1.0F});
	
//	TODO: Add Javadocs!
	public static final ConvolutionKernelNF GRADIENT_HORIZONTAL_3 = new ConvolutionKernelNF(0.0F, 1.0F, new float[] {-1.0F, -1.0F, -1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F});
	
//	TODO: Add Javadocs!
	public static final ConvolutionKernelNF GRADIENT_VERTICAL_3 = new ConvolutionKernelNF(0.0F, 1.0F, new float[] {-1.0F, 0.0F, 1.0F, -1.0F, 0.0F, 1.0F, -1.0F, 0.0F, 1.0F});
	
//	TODO: Add Javadocs!
	public static final ConvolutionKernelNF IDENTITY_3 = new ConvolutionKernelNF(0.0F, 1.0F, new float[] {0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F});
	
//	TODO: Add Javadocs!
	public static final ConvolutionKernelNF IDENTITY_5 = new ConvolutionKernelNF(0.0F, 1.0F, new float[] {0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F});
	
//	TODO: Add Javadocs!
	public static final ConvolutionKernelNF RIDGE_DETECTION_3 = new ConvolutionKernelNF(0.0F, 1.0F, new float[] {-1.0F, -1.0F, -1.0F, -1.0F, 8.0F, -1.0F, -1.0F, -1.0F, -1.0F});
	
//	TODO: Add Javadocs!
	public static final ConvolutionKernelNF SHARPEN_3 = new ConvolutionKernelNF(0.0F, 1.0F, new float[] {-1.0F, -1.0F, -1.0F, -1.0F, 9.0F, -1.0F, -1.0F, -1.0F, -1.0F});
	
//	TODO: Add Javadocs!
	public static final ConvolutionKernelNF UNSHARP_MASKING_5 = new ConvolutionKernelNF(0.0F, -1.0F / 256.0F, new float[] {1.0F, 4.0F, 6.0F, 4.0F, 1.0F, 4.0F, 16.0F, 24.0F, 16.0F, 4.0F, 6.0F, 24.0F, -476.0F, 24.0F, 6.0F, 4.0F, 16.0F, 24.0F, 16.0F, 4.0F, 1.0F, 4.0F, 6.0F, 4.0F, 1.0F});
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final float bias;
	private final float factor;
	private final float[] elements;
	private final int resolution;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public ConvolutionKernelNF(final float bias, final float factor, final float[] elements) {
		this.bias = bias;
		this.factor = factor;
		this.elements = doRequireValidElements(elements);
		this.resolution = (int)(Floats.sqrt(this.elements.length));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ConvolutionKernelNF)) {
			return false;
		} else if(!Floats.equal(this.bias, ConvolutionKernelNF.class.cast(object).bias)) {
			return false;
		} else if(!Floats.equal(this.factor, ConvolutionKernelNF.class.cast(object).factor)) {
			return false;
		} else if(!Arrays.equals(this.elements, ConvolutionKernelNF.class.cast(object).elements)) {
			return false;
		} else if(this.resolution != ConvolutionKernelNF.class.cast(object).resolution) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
	public float getBias() {
		return this.bias;
	}
	
//	TODO: Add Javadocs!
	public float getFactor() {
		return this.factor;
	}
	
//	TODO: Add Javadocs!
	public float[] getElements() {
		return this.elements.clone();
	}
	
//	TODO: Add Javadocs!
	public int getResolution() {
		return this.resolution;
	}
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.bias), Float.valueOf(this.factor), Integer.valueOf(Arrays.hashCode(this.elements)), Integer.valueOf(this.resolution));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static ConvolutionKernelNF random(final int resolution) {
		if(resolution < 1) {
			throw new IllegalArgumentException(String.format("The value of resolution, %d, is invalid. It must be greater than or equal to 1.", Integer.valueOf(resolution)));
		}
		
		if(resolution % 2 == 0) {
			throw new IllegalArgumentException(String.format("The value of resolution, %d, is invalid. It cannot be even.", Integer.valueOf(resolution)));
		}
		
		if(resolution * resolution < 1) {
			throw new IllegalArgumentException(String.format("The value of resolution * resolution, %d, is invalid. It must be greater than or equal to 1.", Integer.valueOf(resolution * resolution)));
		}
		
		final float[] elements = new float[resolution * resolution];
		
		final int middle = (resolution - 1) / 2;
		
		for(int y = 0; y < resolution; y++) {
			for(int x = 0; x < resolution; x++) {
				if(x == middle && y == middle) {
					elements[y * resolution + x] = 1.0F;
				} else {
					elements[y * resolution + x] = Floats.random(-1.0F, 1.0F);
				}
			}
		}
		
		float elementTotal = 0.0F;
		
		for(final float element : elements) {
			elementTotal += element;
		}
		
		final boolean isBiasBasedOnRandomDouble = doNextBoolean();
		final boolean isFactorBasedOnElementTotal = doNextBoolean();
		final boolean isFactorBasedOnRandomDouble = doNextBoolean();
		
		final float bias = isBiasBasedOnRandomDouble ? Floats.random() : 0.0F;
		final float factor = isFactorBasedOnElementTotal ? Floats.isZero(elementTotal) ? 1.0F : 1.0F / elementTotal : isFactorBasedOnRandomDouble ? Floats.random() : 1.0F;
		
		return new ConvolutionKernelNF(bias, factor, elements);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static boolean doNextBoolean() {
		return ThreadLocalRandom.current().nextBoolean();
	}
	
	private static float[] doRequireValidElements(final float[] elements) {
//		Check that 'elements' is not 'null':
		Objects.requireNonNull(elements, "elements == null");
		
//		Check that 'elements.length' is odd:
		if(elements.length % 2 == 0) {
			throw new IllegalArgumentException(String.format("The value of elements.length, %d, is invalid. It cannot be even.", Integer.valueOf(elements.length)));
		}
		
//		Compute the square root of 'elements.length' and the value that is closest to it and represents a mathematical integer:
		final float vA = Floats.sqrt(elements.length);
		final float vB = Floats.rint(vA);
		
//		Check that the square root of 'elements.length' and the value that is closest to it and represents a mathematical integer are equal:
		if(!Floats.equal(vA, vB)) {
			throw new IllegalArgumentException(String.format("The value of elements.length, %d, is invalid. Floats.sqrt(elements.length) must return a value that is a mathematical integer.", Integer.valueOf(elements.length)));
		}
		
//		Compute the resolution by converting the square root of 'elements.length' to an 'int':
		final int kR = (int)(vA);
		
//		Check that the resolution is odd:
		if(kR % 2 == 0) {
			throw new IllegalArgumentException(String.format("The value of elements.length, %d, is invalid. Floats.sqrt(elements.length) must return a value that is odd.", Integer.valueOf(elements.length)));
		}
		
		return elements.clone();
	}
}