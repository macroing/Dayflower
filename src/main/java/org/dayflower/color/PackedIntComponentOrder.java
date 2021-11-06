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
package org.dayflower.color;

import java.util.Objects;

/**
 * A {@code PackedIntComponentOrder} is used to determine the order in which the components are stored in an {@code int}, in packed form.
 * <p>
 * The names of the constants in this enum signifies the order of the components, from most significant byte to least significant byte.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public enum PackedIntComponentOrder {
	/**
	 * A {@code PackedIntComponentOrder} that stores the A-, B-, G- and R-components, in that order, from most significant byte to least significant byte.
	 * <p>
	 * The components are stored in the following way:
	 * <pre>
	 * {@code
	 * int a = (colorABGR >> 24) & 0xFF;
	 * int b = (colorABGR >> 16) & 0xFF;
	 * int g = (colorABGR >>  8) & 0xFF;
	 * int r = (colorABGR >>  0) & 0xFF;
	 * }
	 * </pre>
	 */
	ABGR(0, 8, 16, 24),
	
	/**
	 * A {@code PackedIntComponentOrder} that stores the A-, R-, G- and B-components, in that order, from most significant byte to least significant byte.
	 * <p>
	 * The components are stored in the following way:
	 * <pre>
	 * {@code
	 * int a = (colorARGB >> 24) & 0xFF;
	 * int r = (colorARGB >> 16) & 0xFF;
	 * int g = (colorARGB >>  8) & 0xFF;
	 * int b = (colorARGB >>  0) & 0xFF;
	 * }
	 * </pre>
	 */
	ARGB(16, 8, 0, 24),
	
	/**
	 * A {@code PackedIntComponentOrder} that stores the B-, G- and R-components, in that order, from most significant byte to least significant byte.
	 * <p>
	 * The components are stored in the following way:
	 * <pre>
	 * {@code
	 * int b = (colorBGR >> 16) & 0xFF;
	 * int g = (colorBGR >>  8) & 0xFF;
	 * int r = (colorBGR >>  0) & 0xFF;
	 * }
	 * </pre>
	 */
	BGR(0, 8, 16, -1),
	
	/**
	 * A {@code PackedIntComponentOrder} that stores the R-, G- and B-components, in that order, from most significant byte to least significant byte.
	 * <p>
	 * The components are stored in the following way:
	 * <pre>
	 * {@code
	 * int r = (colorRGB >> 16) & 0xFF;
	 * int g = (colorRGB >>  8) & 0xFF;
	 * int b = (colorRGB >>  0) & 0xFF;
	 * }
	 * </pre>
	 */
	RGB(16, 8, 0, -1);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final int shiftA;
	private final int shiftB;
	private final int shiftG;
	private final int shiftR;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private PackedIntComponentOrder(final int shiftR, final int shiftG, final int shiftB, final int shiftA) {
		this.shiftR = shiftR;
		this.shiftG = shiftG;
		this.shiftB = shiftB;
		this.shiftA = shiftA;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, this {@code PackedIntComponentOrder} has a shift for the A-component, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code PackedIntComponentOrder} has a shift for the A-component, {@code false} otherwise
	 */
	public boolean hasShiftA() {
		return this.shiftA != -1;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code PackedIntComponentOrder} has a shift for the B-component, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code PackedIntComponentOrder} has a shift for the B-component, {@code false} otherwise
	 */
	public boolean hasShiftB() {
		return this.shiftB != -1;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code PackedIntComponentOrder} has a shift for the G-component, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code PackedIntComponentOrder} has a shift for the G-component, {@code false} otherwise
	 */
	public boolean hasShiftG() {
		return this.shiftG != -1;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code PackedIntComponentOrder} has a shift for the R-component, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code PackedIntComponentOrder} has a shift for the R-component, {@code false} otherwise
	 */
	public boolean hasShiftR() {
		return this.shiftR != -1;
	}
	
	/**
	 * Returns the component count of this {@code PackedIntComponentOrder} instance.
	 * 
	 * @return the component count of this {@code PackedIntComponentOrder} instance
	 */
	public int getComponentCount() {
		return (hasShiftR() ? 1 : 0) + (hasShiftG() ? 1 : 0) + (hasShiftB() ? 1 : 0) + (hasShiftA() ? 1 : 0);
	}
	
	/**
	 * Returns the shift for the A-component, or {@code -1} if it does not have one.
	 * 
	 * @return the shift for the A-component, or {@code -1} if it does not have one
	 */
	public int getShiftA() {
		return this.shiftA;
	}
	
	/**
	 * Returns the shift for the B-component, or {@code -1} if it does not have one.
	 * 
	 * @return the shift for the B-component, or {@code -1} if it does not have one
	 */
	public int getShiftB() {
		return this.shiftB;
	}
	
	/**
	 * Returns the shift for the G-component, or {@code -1} if it does not have one.
	 * 
	 * @return the shift for the G-component, or {@code -1} if it does not have one
	 */
	public int getShiftG() {
		return this.shiftG;
	}
	
	/**
	 * Returns the shift for the R-component, or {@code -1} if it does not have one.
	 * 
	 * @return the shift for the R-component, or {@code -1} if it does not have one
	 */
	public int getShiftR() {
		return this.shiftR;
	}
	
	/**
	 * Returns an {@code int} with {@code r}, {@code g} and {@code b} in a packed form.
	 * 
	 * @param r the R-component
	 * @param g the G-component
	 * @param b the B-component
	 * @return an {@code int} with {@code r}, {@code g} and {@code b} in a packed form
	 */
	public int pack(final int r, final int g, final int b) {
		return (hasShiftR() ? ((r & 0xFF) << getShiftR()) : 0) | (hasShiftG() ? ((g & 0xFF) << getShiftG()) : 0) | (hasShiftB() ? ((b & 0xFF) << getShiftB()) : 0);
	}
	
	/**
	 * Returns an {@code int} with {@code r}, {@code g}, {@code b} and {@code a} in a packed form.
	 * 
	 * @param r the R-component
	 * @param g the G-component
	 * @param b the B-component
	 * @param a the A-component
	 * @return an {@code int} with {@code r}, {@code g}, {@code b} and {@code a} in a packed form
	 */
	public int pack(final int r, final int g, final int b, final int a) {
		return (hasShiftR() ? ((r & 0xFF) << getShiftR()) : 0) | (hasShiftG() ? ((g & 0xFF) << getShiftG()) : 0) | (hasShiftB() ? ((b & 0xFF) << getShiftB()) : 0) | (hasShiftA() ? ((a & 0xFF) << getShiftA()) : 0);
	}
	
	/**
	 * Returns an {@code int} with the unpacked A-component, or {@code 255} if it could not unpack.
	 * 
	 * @param color an {@code int} with the components in packed form
	 * @return an {@code int} with the unpacked A-component, or {@code 255} if it could not unpack
	 */
	public int unpackA(final int color) {
		return hasShiftA() ? (color >> getShiftA()) & 0xFF : 255;
	}
	
	/**
	 * Returns an {@code int} with the unpacked B-component, or {@code 0} if it could not unpack.
	 * 
	 * @param color an {@code int} with the components in packed form
	 * @return an {@code int} with the unpacked B-component, or {@code 0} if it could not unpack
	 */
	public int unpackB(final int color) {
		return hasShiftB() ? (color >> getShiftB()) & 0xFF : 0;
	}
	
	/**
	 * Returns an {@code int} with the unpacked G-component, or {@code 0} if it could not unpack.
	 * 
	 * @param color an {@code int} with the components in packed form
	 * @return an {@code int} with the unpacked G-component, or {@code 0} if it could not unpack
	 */
	public int unpackG(final int color) {
		return hasShiftG() ? (color >> getShiftG()) & 0xFF : 0;
	}
	
	/**
	 * Returns an {@code int} with the unpacked R-component, or {@code 0} if it could not unpack.
	 * 
	 * @param color an {@code int} with the components in packed form
	 * @return an {@code int} with the unpacked R-component, or {@code 0} if it could not unpack
	 */
	public int unpackR(final int color) {
		return hasShiftR() ? (color >> getShiftR()) & 0xFF : 0;
	}
	
	/**
	 * Packs the {@code byte[]} {@code array} stored as {@code arrayComponentOrder} to a new packed {@code int[]} stores as this {@code PackedIntComponentOrder} instance.
	 * <p>
	 * Returns a new {@code int[]} with the result of the operation.
	 * <p>
	 * If either {@code arrayComponentOrder} or {@code array} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % arrayComponentOrder.getComponentCount() != 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param arrayComponentOrder the {@link ArrayComponentOrder} to read the color component values with
	 * @param array a {@code byte[]} with color component values
	 * @return a new {@code int[]} with the result of the operation
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % arrayComponentOrder.getComponentCount() != 0}
	 * @throws NullPointerException thrown if, and only if, either {@code arrayComponentOrder} or {@code array} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public int[] pack(final ArrayComponentOrder arrayComponentOrder, final byte[] array) {
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		Objects.requireNonNull(array, "array == null");
		
		if(array.length % arrayComponentOrder.getComponentCount() != 0) {
			throw new IllegalArgumentException(String.format("%d %% %d != 0", Integer.valueOf(array.length), Integer.valueOf(arrayComponentOrder.getComponentCount())));
		}
		
		final int resolution = array.length / arrayComponentOrder.getComponentCount();
		
		final int[] arrayPacked = new int[resolution];
		
		for(int i = 0; i < resolution; i++) {
			final int offset = i * arrayComponentOrder.getComponentCount();
			
			final int r = arrayComponentOrder.readRAsInt(array, offset);
			final int g = arrayComponentOrder.readGAsInt(array, offset);
			final int b = arrayComponentOrder.readBAsInt(array, offset);
			final int a = arrayComponentOrder.readAAsInt(array, offset);
			
			arrayPacked[i] = pack(r, g, b, a);
		}
		
		return arrayPacked;
	}
	
	/**
	 * Packs the {@code int[]} {@code array} stored as {@code arrayComponentOrder} to a new packed {@code int[]} stores as this {@code PackedIntComponentOrder} instance.
	 * <p>
	 * Returns a new {@code int[]} with the result of the operation.
	 * <p>
	 * If either {@code arrayComponentOrder} or {@code array} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % arrayComponentOrder.getComponentCount() != 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param arrayComponentOrder the {@link ArrayComponentOrder} to read the color component values with
	 * @param array an {@code int[]} with color component values
	 * @return a new {@code int[]} with the result of the operation
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % arrayComponentOrder.getComponentCount() != 0}
	 * @throws NullPointerException thrown if, and only if, either {@code arrayComponentOrder} or {@code array} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public int[] pack(final ArrayComponentOrder arrayComponentOrder, final int[] array) {
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		Objects.requireNonNull(array, "array == null");
		
		if(array.length % arrayComponentOrder.getComponentCount() != 0) {
			throw new IllegalArgumentException(String.format("%d %% %d != 0", Integer.valueOf(array.length), Integer.valueOf(arrayComponentOrder.getComponentCount())));
		}
		
		final int resolution = array.length / arrayComponentOrder.getComponentCount();
		
		final int[] arrayPacked = new int[resolution];
		
		for(int i = 0; i < resolution; i++) {
			final int offset = i * arrayComponentOrder.getComponentCount();
			
			final int r = arrayComponentOrder.readR(array, offset);
			final int g = arrayComponentOrder.readG(array, offset);
			final int b = arrayComponentOrder.readB(array, offset);
			final int a = arrayComponentOrder.readA(array, offset);
			
			arrayPacked[i] = pack(r, g, b, a);
		}
		
		return arrayPacked;
	}
	
	/**
	 * Unpacks the {@code int[]} {@code array} stored as this {@code PackedIntComponentOrder} instance to a new {@code int[]} stored as {@code arrayComponentOrder}.
	 * <p>
	 * Returns a new {@code int[]} with the result of the operation.
	 * <p>
	 * If either {@code arrayComponentOrder} or {@code array} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param arrayComponentOrder the {@link ArrayComponentOrder} to write the color component values with
	 * @param array an {@code int[]} with packed color component values
	 * @return a new {@code int[]} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code arrayComponentOrder} or {@code array} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public int[] unpack(final ArrayComponentOrder arrayComponentOrder, final int[] array) {
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		Objects.requireNonNull(array, "array == null");
		
		final int[] arrayUnpacked = new int[array.length * arrayComponentOrder.getComponentCount()];
		
		for(int i = 0; i < array.length; i++) {
			final int color = array[i];
			
			final int r = unpackR(color);
			final int g = unpackG(color);
			final int b = unpackB(color);
			final int a = unpackA(color);
			
			final int offset = i * arrayComponentOrder.getComponentCount();
			
			if(arrayComponentOrder.hasOffsetR()) {
				arrayUnpacked[offset + arrayComponentOrder.getOffsetR()] = r;
			}
			
			if(arrayComponentOrder.hasOffsetG()) {
				arrayUnpacked[offset + arrayComponentOrder.getOffsetG()] = g;
			}
			
			if(arrayComponentOrder.hasOffsetB()) {
				arrayUnpacked[offset + arrayComponentOrder.getOffsetB()] = b;
			}
			
			if(arrayComponentOrder.hasOffsetA()) {
				arrayUnpacked[offset + arrayComponentOrder.getOffsetA()] = a;
			}
		}
		
		return arrayUnpacked;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Converts the {@code int[]} {@code array} stored as {@code packedIntComponentOrderA} to a new {@code int[]} stored as {@code packedIntComponentOrderB}.
	 * <p>
	 * Returns a new {@code int[]} with the result of the conversion.
	 * <p>
	 * If either {@code packedIntComponentOrderA}, {@code packedIntComponentOrderB} or {@code array} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param packedIntComponentOrderA the {@code PackedIntComponentOrder} to convert from
	 * @param packedIntComponentOrderB the {@code PackedIntComponentOrder} to convert to
	 * @param array an {@code int[]} with packed color component values
	 * @return a new {@code int[]} with the result of the conversion
	 * @throws NullPointerException thrown if, and only if, either {@code packedIntComponentOrderA}, {@code packedIntComponentOrderB} or {@code array} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static int[] convert(final PackedIntComponentOrder packedIntComponentOrderA, final PackedIntComponentOrder packedIntComponentOrderB, final int[] array) {
		Objects.requireNonNull(packedIntComponentOrderA, "packedIntComponentOrderA == null");
		Objects.requireNonNull(packedIntComponentOrderB, "packedIntComponentOrderB == null");
		Objects.requireNonNull(array, "array == null");
		
		final int[] arrayConverted = new int[array.length];
		
		for(int i = 0; i < array.length; i++) {
			final int color = array[i];
			
			final int r = packedIntComponentOrderA.unpackR(color);
			final int g = packedIntComponentOrderA.unpackG(color);
			final int b = packedIntComponentOrderA.unpackB(color);
			final int a = packedIntComponentOrderA.unpackA(color);
			
			arrayConverted[i] = packedIntComponentOrderB.pack(r, g, b, a);
		}
		
		return arrayConverted;
	}
}