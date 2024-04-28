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
package org.dayflower.color;

import java.util.Objects;

/**
 * An {@code ArrayComponentOrder} is used to determine the order in which the components are stored in an array.
 * <p>
 * This class has nothing to do with the way the components are stored in an {@code int}, in packed form.
 * <p>
 * The names of the constants in this class should not be confused with the names of similar things in other libraries. They only reflect the way the components are stored in an array. The order of the letters signify the order in which they are stored in the array, starting from some offset. An example would be {@code ARGB}, where {@code A} denotes {@code offset + 0}, {@code R} denotes {@code offset + 1}, {@code G} denotes {@code offset + 2} and {@code B} denotes {@code offset + 3}.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public enum ArrayComponentOrder {
	/**
	 * The components are stored as A, R, G and B.
	 * <p>
	 * The following code demonstrates the way it is stored in an array:
	 * <pre>
	 * {@code
	 * array = new int[4];
	 * array[0] = a;
	 * array[1] = r;
	 * array[2] = g;
	 * array[3] = b;
	 * }
	 * </pre>
	 */
	ARGB(1, 2, 3, 0),
	
	/**
	 * The components are stored as B, G and R.
	 * <p>
	 * The following code demonstrates the way it is stored in an array:
	 * <pre>
	 * {@code
	 * array = new int[3];
	 * array[0] = b;
	 * array[1] = g;
	 * array[2] = r;
	 * }
	 * </pre>
	 */
	BGR(2, 1, 0, -1),
	
	/**
	 * The components are stored as B, G, R and A.
	 * <p>
	 * The following code demonstrates the way it is stored in an array:
	 * <pre>
	 * {@code
	 * array = new int[4];
	 * array[0] = b;
	 * array[1] = g;
	 * array[2] = r;
	 * array[3] = a;
	 * }
	 * </pre>
	 */
	BGRA(2, 1, 0, 3),
	
	/**
	 * The components are stored as R, G and B.
	 * <p>
	 * The following code demonstrates the way it is stored in an array:
	 * <pre>
	 * {@code
	 * array = new int[3];
	 * array[0] = r;
	 * array[1] = g;
	 * array[2] = b;
	 * }
	 * </pre>
	 */
	RGB(0, 1, 2, -1),
	
	/**
	 * The components are stored as R, G, B and A.
	 * <p>
	 * The following code demonstrates the way it is stored in an array:
	 * <pre>
	 * {@code
	 * array = new int[4];
	 * array[0] = r;
	 * array[1] = g;
	 * array[2] = b;
	 * array[3] = a;
	 * }
	 * </pre>
	 */
	RGBA(0, 1, 2, 3);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final int offsetA;
	private final int offsetB;
	private final int offsetG;
	private final int offsetR;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private ArrayComponentOrder(final int offsetR, final int offsetG, final int offsetB, final int offsetA) {
		this.offsetR = offsetR;
		this.offsetG = offsetG;
		this.offsetB = offsetB;
		this.offsetA = offsetA;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, this {@code ArrayComponentOrder} has an offset for the A-component, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code ArrayComponentOrder} has an offset for the A-component, {@code false} otherwise
	 */
	public boolean hasOffsetA() {
		return this.offsetA != -1;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code ArrayComponentOrder} has an offset for the B-component, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code ArrayComponentOrder} has an offset for the B-component, {@code false} otherwise
	 */
	@SuppressWarnings("static-method")
	public boolean hasOffsetB() {
//		return this.offsetB != -1;
		return true;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code ArrayComponentOrder} has an offset for the G-component, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code ArrayComponentOrder} has an offset for the G-component, {@code false} otherwise
	 */
	@SuppressWarnings("static-method")
	public boolean hasOffsetG() {
//		return this.offsetG != -1;
		return true;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code ArrayComponentOrder} has an offset for the R-component, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code ArrayComponentOrder} has an offset for the R-component, {@code false} otherwise
	 */
	@SuppressWarnings("static-method")
	public boolean hasOffsetR() {
//		return this.offsetR != -1;
		return true;
	}
	
	/**
	 * Returns a {@code byte} with the A-component, or {@code (byte)(0)} if it does not have an offset for the A-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetA()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return a {@code byte} with the A-component, or {@code (byte)(0)} if it does not have an offset for the A-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetA()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public byte readA(final byte[] array, final int offset) {
		return hasOffsetA() ? array[offset + getOffsetA()] : (byte)(0);
	}
	
	/**
	 * Returns a {@code byte} with the B-component, or {@code 0} if it does not have an offset for the B-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetB()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return a {@code byte} with the B-component, or {@code 0} if it does not have an offset for the B-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetB()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public byte readB(final byte[] array, final int offset) {
//		return hasOffsetB() ? array[offset + getOffsetB()] : 0;
		return array[offset + getOffsetB()];
	}
	
	/**
	 * Returns a {@code byte} with the G-component, or {@code 0} if it does not have an offset for the G-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetG()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return a {@code byte} with the G-component, or {@code 0} if it does not have an offset for the G-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetG()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public byte readG(final byte[] array, final int offset) {
//		return hasOffsetG() ? array[offset + getOffsetG()] : 0;
		return array[offset + getOffsetG()];
	}
	
	/**
	 * Returns a {@code byte} with the R-component, or {@code 0} if it does not have an offset for the R-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetR()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return a {@code byte} with the R-component, or {@code 0} if it does not have an offset for the R-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetR()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public byte readR(final byte[] array, final int offset) {
//		return hasOffsetR() ? array[offset + getOffsetR()] : 0;
		return array[offset + getOffsetR()];
	}
	
	/**
	 * Returns a {@code double} with the A-component, or {@code 1.0D} if it does not have an offset for the A-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetA()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return a {@code double} with the A-component, or {@code 1.0D} if it does not have an offset for the A-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetA()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public double readA(final double[] array, final int offset) {
		return hasOffsetA() ? array[offset + getOffsetA()] : 1.0D;
	}
	
	/**
	 * Returns a {@code double} with the B-component, or {@code 0.0D} if it does not have an offset for the B-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetB()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return a {@code double} with the B-component, or {@code 0.0D} if it does not have an offset for the B-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetB()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public double readB(final double[] array, final int offset) {
//		return hasOffsetB() ? array[offset + getOffsetB()] : 0.0D;
		return array[offset + getOffsetB()];
	}
	
	/**
	 * Returns a {@code double} with the G-component, or {@code 0.0D} if it does not have an offset for the G-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetG()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return a {@code double} with the G-component, or {@code 0.0D} if it does not have an offset for the G-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetG()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public double readG(final double[] array, final int offset) {
//		return hasOffsetG() ? array[offset + getOffsetG()] : 0.0D;
		return array[offset + getOffsetG()];
	}
	
	/**
	 * Returns a {@code double} with the R-component, or {@code 0.0D} if it does not have an offset for the R-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetR()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return a {@code double} with the R-component, or {@code 0.0D} if it does not have an offset for the R-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetR()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public double readR(final double[] array, final int offset) {
//		return hasOffsetR() ? array[offset + getOffsetR()] : 0.0D;
		return array[offset + getOffsetR()];
	}
	
	/**
	 * Returns a {@code float} with the A-component, or {@code 1.0F} if it does not have an offset for the A-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetA()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return a {@code float} with the A-component, or {@code 1.0F} if it does not have an offset for the A-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetA()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public float readA(final float[] array, final int offset) {
		return hasOffsetA() ? array[offset + getOffsetA()] : 1.0F;
	}
	
	/**
	 * Returns a {@code float} with the B-component, or {@code 0.0F} if it does not have an offset for the B-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetB()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return a {@code float} with the B-component, or {@code 0.0F} if it does not have an offset for the B-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetB()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public float readB(final float[] array, final int offset) {
//		return hasOffsetB() ? array[offset + getOffsetB()] : 0.0F;
		return array[offset + getOffsetB()];
	}
	
	/**
	 * Returns a {@code float} with the G-component, or {@code 0.0F} if it does not have an offset for the G-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetG()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return a {@code float} with the G-component, or {@code 0.0F} if it does not have an offset for the G-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetG()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public float readG(final float[] array, final int offset) {
//		return hasOffsetG() ? array[offset + getOffsetG()] : 0.0F;
		return array[offset + getOffsetG()];
	}
	
	/**
	 * Returns a {@code float} with the R-component, or {@code 0.0F} if it does not have an offset for the R-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetR()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return a {@code float} with the R-component, or {@code 0.0F} if it does not have an offset for the R-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetR()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public float readR(final float[] array, final int offset) {
//		return hasOffsetR() ? array[offset + getOffsetR()] : 0.0F;
		return array[offset + getOffsetR()];
	}
	
	/**
	 * Returns the component count of this {@code ArrayComponentOrder} instance.
	 * 
	 * @return the component count of this {@code ArrayComponentOrder} instance
	 */
	public int getComponentCount() {
//		return (hasOffsetR() ? 1 : 0) + (hasOffsetG() ? 1 : 0) + (hasOffsetB() ? 1 : 0) + (hasOffsetA() ? 1 : 0);
		return 3 + (hasOffsetA() ? 1 : 0);
	}
	
	/**
	 * Returns the offset for the A-component, or {@code -1} if it does not have one.
	 * 
	 * @return the offset for the A-component, or {@code -1} if it does not have one
	 */
	public int getOffsetA() {
		return this.offsetA;
	}
	
	/**
	 * Returns the offset for the B-component, or {@code -1} if it does not have one.
	 * 
	 * @return the offset for the B-component, or {@code -1} if it does not have one
	 */
	public int getOffsetB() {
		return this.offsetB;
	}
	
	/**
	 * Returns the offset for the G-component, or {@code -1} if it does not have one.
	 * 
	 * @return the offset for the G-component, or {@code -1} if it does not have one
	 */
	public int getOffsetG() {
		return this.offsetG;
	}
	
	/**
	 * Returns the offset for the R-component, or {@code -1} if it does not have one.
	 * 
	 * @return the offset for the R-component, or {@code -1} if it does not have one
	 */
	public int getOffsetR() {
		return this.offsetR;
	}
	
	/**
	 * Returns an {@code int} with the A-component, or {@code 0} if it does not have an offset for the A-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetA()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return an {@code int} with the A-component, or {@code 0} if it does not have an offset for the A-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetA()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public int readA(final int[] array, final int offset) {
		return hasOffsetA() ? array[offset + getOffsetA()] & 0xFF : 0;
	}
	
	/**
	 * Returns an {@code int} with the A-component, or {@code 0} if it does not have an offset for the A-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetA()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return an {@code int} with the A-component, or {@code 0} if it does not have an offset for the A-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetA()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public int readAAsInt(final byte[] array, final int offset) {
		return readA(array, offset) & 0xFF;
	}
	
	/**
	 * Returns an {@code int} with the B-component, or {@code 0} if it does not have an offset for the B-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetB()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return an {@code int} with the B-component, or {@code 0} if it does not have an offset for the B-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetB()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public int readB(final int[] array, final int offset) {
//		return hasOffsetB() ? array[offset + getOffsetB()] & 0xFF : 0;
		return array[offset + getOffsetB()] & 0xFF;
	}
	
	/**
	 * Returns an {@code int} with the B-component, or {@code 0} if it does not have an offset for the B-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetB()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return an {@code int} with the B-component, or {@code 0} if it does not have an offset for the B-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetB()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public int readBAsInt(final byte[] array, final int offset) {
		return readB(array, offset) & 0xFF;
	}
	
	/**
	 * Returns an {@code int} with the G-component, or {@code 0} if it does not have an offset for the G-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetG()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return an {@code int} with the G-component, or {@code 0} if it does not have an offset for the G-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetG()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public int readG(final int[] array, final int offset) {
//		return hasOffsetG() ? array[offset + getOffsetG()] & 0xFF : 0;
		return array[offset + getOffsetG()];
	}
	
	/**
	 * Returns an {@code int} with the G-component, or {@code 0} if it does not have an offset for the G-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetG()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return an {@code int} with the G-component, or {@code 0} if it does not have an offset for the G-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetG()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public int readGAsInt(final byte[] array, final int offset) {
		return readG(array, offset) & 0xFF;
	}
	
	/**
	 * Returns an {@code int} with the R-component, or {@code 0} if it does not have an offset for the R-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetR()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return an {@code int} with the R-component, or {@code 0} if it does not have an offset for the R-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetR()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public int readR(final int[] array, final int offset) {
//		return hasOffsetR() ? array[offset + getOffsetR()] & 0xFF : 0;
		return array[offset + getOffsetR()] & 0xFF;
	}
	
	/**
	 * Returns an {@code int} with the R-component, or {@code 0} if it does not have an offset for the R-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetR()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return an {@code int} with the R-component, or {@code 0} if it does not have an offset for the R-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetR()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public int readRAsInt(final byte[] array, final int offset) {
		return readR(array, offset) & 0xFF;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Converts the {@code byte[]} {@code array} stored as {@code arrayComponentOrderA} to a new {@code byte[]} stored as {@code arrayComponentOrderB}.
	 * <p>
	 * Returns a new {@code byte[]} with the result of the conversion.
	 * <p>
	 * If either {@code arrayComponentOrderA}, {@code arrayComponentOrderB} or {@code array} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % arrayComponentOrderA.getComponentCount() != 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param arrayComponentOrderA the {@code ArrayComponentOrder} to convert from
	 * @param arrayComponentOrderB the {@code ArrayComponentOrder} to convert to
	 * @param array a {@code byte[]} with color component values
	 * @return a new {@code byte[]} with the result of the conversion
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % arrayComponentOrderA.getComponentCount() != 0}
	 * @throws NullPointerException thrown if, and only if, either {@code arrayComponentOrderA}, {@code arrayComponentOrderB} or {@code array} are {@code null}
	 */
	public static byte[] convert(final ArrayComponentOrder arrayComponentOrderA, final ArrayComponentOrder arrayComponentOrderB, final byte[] array) {
		Objects.requireNonNull(arrayComponentOrderA, "arrayComponentOrderA == null");
		Objects.requireNonNull(arrayComponentOrderB, "arrayComponentOrderB == null");
		Objects.requireNonNull(array, "array == null");
		
		if(array.length % arrayComponentOrderA.getComponentCount() != 0) {
			throw new IllegalArgumentException(String.format("%d %% %d != 0", Integer.valueOf(array.length), Integer.valueOf(arrayComponentOrderA.getComponentCount())));
		}
		
		final int resolution = array.length / arrayComponentOrderA.getComponentCount();
		
		final byte[] arrayConverted = new byte[resolution * arrayComponentOrderB.getComponentCount()];
		
		for(int i = 0; i < resolution; i++) {
			final int offsetA = i * arrayComponentOrderA.getComponentCount();
			final int offsetB = i * arrayComponentOrderB.getComponentCount();
			
			final byte r = arrayComponentOrderA.readR(array, offsetA);
			final byte g = arrayComponentOrderA.readG(array, offsetA);
			final byte b = arrayComponentOrderA.readB(array, offsetA);
			final byte a = arrayComponentOrderA.readA(array, offsetA);
			
//			if(arrayComponentOrderB.hasOffsetR()) {
				arrayConverted[offsetB + arrayComponentOrderB.getOffsetR()] = r;
//			}
			
//			if(arrayComponentOrderB.hasOffsetG()) {
				arrayConverted[offsetB + arrayComponentOrderB.getOffsetG()] = g;
//			}
			
//			if(arrayComponentOrderB.hasOffsetB()) {
				arrayConverted[offsetB + arrayComponentOrderB.getOffsetB()] = b;
//			}
			
			if(arrayComponentOrderB.hasOffsetA()) {
				arrayConverted[offsetB + arrayComponentOrderB.getOffsetA()] = a;
			}
		}
		
		return arrayConverted;
	}
	
	/**
	 * Converts the {@code double[]} {@code array} stored as {@code arrayComponentOrderA} to a new {@code double[]} stored as {@code arrayComponentOrderB}.
	 * <p>
	 * Returns a new {@code double[]} with the result of the conversion.
	 * <p>
	 * If either {@code arrayComponentOrderA}, {@code arrayComponentOrderB} or {@code array} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % arrayComponentOrderA.getComponentCount() != 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param arrayComponentOrderA the {@code ArrayComponentOrder} to convert from
	 * @param arrayComponentOrderB the {@code ArrayComponentOrder} to convert to
	 * @param array a {@code double[]} with color component values
	 * @return a new {@code double[]} with the result of the conversion
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % arrayComponentOrderA.getComponentCount() != 0}
	 * @throws NullPointerException thrown if, and only if, either {@code arrayComponentOrderA}, {@code arrayComponentOrderB} or {@code array} are {@code null}
	 */
	public static double[] convert(final ArrayComponentOrder arrayComponentOrderA, final ArrayComponentOrder arrayComponentOrderB, final double[] array) {
		Objects.requireNonNull(arrayComponentOrderA, "arrayComponentOrderA == null");
		Objects.requireNonNull(arrayComponentOrderB, "arrayComponentOrderB == null");
		Objects.requireNonNull(array, "array == null");
		
		if(array.length % arrayComponentOrderA.getComponentCount() != 0) {
			throw new IllegalArgumentException(String.format("%d %% %d != 0", Integer.valueOf(array.length), Integer.valueOf(arrayComponentOrderA.getComponentCount())));
		}
		
		final int resolution = array.length / arrayComponentOrderA.getComponentCount();
		
		final double[] arrayConverted = new double[resolution * arrayComponentOrderB.getComponentCount()];
		
		for(int i = 0; i < resolution; i++) {
			final int offsetA = i * arrayComponentOrderA.getComponentCount();
			final int offsetB = i * arrayComponentOrderB.getComponentCount();
			
			final double r = arrayComponentOrderA.readR(array, offsetA);
			final double g = arrayComponentOrderA.readG(array, offsetA);
			final double b = arrayComponentOrderA.readB(array, offsetA);
			final double a = arrayComponentOrderA.readA(array, offsetA);
			
//			if(arrayComponentOrderB.hasOffsetR()) {
				arrayConverted[offsetB + arrayComponentOrderB.getOffsetR()] = r;
//			}
			
//			if(arrayComponentOrderB.hasOffsetG()) {
				arrayConverted[offsetB + arrayComponentOrderB.getOffsetG()] = g;
//			}
			
//			if(arrayComponentOrderB.hasOffsetB()) {
				arrayConverted[offsetB + arrayComponentOrderB.getOffsetB()] = b;
//			}
			
			if(arrayComponentOrderB.hasOffsetA()) {
				arrayConverted[offsetB + arrayComponentOrderB.getOffsetA()] = a;
			}
		}
		
		return arrayConverted;
	}
	
	/**
	 * Converts the {@code float[]} {@code array} stored as {@code arrayComponentOrderA} to a new {@code double[]} stored as {@code arrayComponentOrderB}.
	 * <p>
	 * Returns a new {@code float[]} with the result of the conversion.
	 * <p>
	 * If either {@code arrayComponentOrderA}, {@code arrayComponentOrderB} or {@code array} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % arrayComponentOrderA.getComponentCount() != 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param arrayComponentOrderA the {@code ArrayComponentOrder} to convert from
	 * @param arrayComponentOrderB the {@code ArrayComponentOrder} to convert to
	 * @param array a {@code float[]} with color component values
	 * @return a new {@code float[]} with the result of the conversion
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % arrayComponentOrderA.getComponentCount() != 0}
	 * @throws NullPointerException thrown if, and only if, either {@code arrayComponentOrderA}, {@code arrayComponentOrderB} or {@code array} are {@code null}
	 */
	public static float[] convert(final ArrayComponentOrder arrayComponentOrderA, final ArrayComponentOrder arrayComponentOrderB, final float[] array) {
		Objects.requireNonNull(arrayComponentOrderA, "arrayComponentOrderA == null");
		Objects.requireNonNull(arrayComponentOrderB, "arrayComponentOrderB == null");
		Objects.requireNonNull(array, "array == null");
		
		if(array.length % arrayComponentOrderA.getComponentCount() != 0) {
			throw new IllegalArgumentException(String.format("%d %% %d != 0", Integer.valueOf(array.length), Integer.valueOf(arrayComponentOrderA.getComponentCount())));
		}
		
		final int resolution = array.length / arrayComponentOrderA.getComponentCount();
		
		final float[] arrayConverted = new float[resolution * arrayComponentOrderB.getComponentCount()];
		
		for(int i = 0; i < resolution; i++) {
			final int offsetA = i * arrayComponentOrderA.getComponentCount();
			final int offsetB = i * arrayComponentOrderB.getComponentCount();
			
			final float r = arrayComponentOrderA.readR(array, offsetA);
			final float g = arrayComponentOrderA.readG(array, offsetA);
			final float b = arrayComponentOrderA.readB(array, offsetA);
			final float a = arrayComponentOrderA.readA(array, offsetA);
			
//			if(arrayComponentOrderB.hasOffsetR()) {
				arrayConverted[offsetB + arrayComponentOrderB.getOffsetR()] = r;
//			}
			
//			if(arrayComponentOrderB.hasOffsetG()) {
				arrayConverted[offsetB + arrayComponentOrderB.getOffsetG()] = g;
//			}
			
//			if(arrayComponentOrderB.hasOffsetB()) {
				arrayConverted[offsetB + arrayComponentOrderB.getOffsetB()] = b;
//			}
			
			if(arrayComponentOrderB.hasOffsetA()) {
				arrayConverted[offsetB + arrayComponentOrderB.getOffsetA()] = a;
			}
		}
		
		return arrayConverted;
	}
	
	/**
	 * Converts the {@code int[]} {@code array} stored as {@code arrayComponentOrderA} to a new {@code int[]} stored as {@code arrayComponentOrderB}.
	 * <p>
	 * Returns a new {@code int[]} with the result of the conversion.
	 * <p>
	 * If either {@code arrayComponentOrderA}, {@code arrayComponentOrderB} or {@code array} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % arrayComponentOrderA.getComponentCount() != 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param arrayComponentOrderA the {@code ArrayComponentOrder} to convert from
	 * @param arrayComponentOrderB the {@code ArrayComponentOrder} to convert to
	 * @param array an {@code int[]} with color component values
	 * @return a new {@code int[]} with the result of the conversion
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % arrayComponentOrderA.getComponentCount() != 0}
	 * @throws NullPointerException thrown if, and only if, either {@code arrayComponentOrderA}, {@code arrayComponentOrderB} or {@code array} are {@code null}
	 */
	public static int[] convert(final ArrayComponentOrder arrayComponentOrderA, final ArrayComponentOrder arrayComponentOrderB, final int[] array) {
		Objects.requireNonNull(arrayComponentOrderA, "arrayComponentOrderA == null");
		Objects.requireNonNull(arrayComponentOrderB, "arrayComponentOrderB == null");
		Objects.requireNonNull(array, "array == null");
		
		if(array.length % arrayComponentOrderA.getComponentCount() != 0) {
			throw new IllegalArgumentException(String.format("%d %% %d != 0", Integer.valueOf(array.length), Integer.valueOf(arrayComponentOrderA.getComponentCount())));
		}
		
		final int resolution = array.length / arrayComponentOrderA.getComponentCount();
		
		final int[] arrayConverted = new int[resolution * arrayComponentOrderB.getComponentCount()];
		
		for(int i = 0; i < resolution; i++) {
			final int offsetA = i * arrayComponentOrderA.getComponentCount();
			final int offsetB = i * arrayComponentOrderB.getComponentCount();
			
			final int r = arrayComponentOrderA.readR(array, offsetA);
			final int g = arrayComponentOrderA.readG(array, offsetA);
			final int b = arrayComponentOrderA.readB(array, offsetA);
			final int a = arrayComponentOrderA.readA(array, offsetA);
			
//			if(arrayComponentOrderB.hasOffsetR()) {
				arrayConverted[offsetB + arrayComponentOrderB.getOffsetR()] = r;
//			}
			
//			if(arrayComponentOrderB.hasOffsetG()) {
				arrayConverted[offsetB + arrayComponentOrderB.getOffsetG()] = g;
//			}
			
//			if(arrayComponentOrderB.hasOffsetB()) {
				arrayConverted[offsetB + arrayComponentOrderB.getOffsetB()] = b;
//			}
			
			if(arrayComponentOrderB.hasOffsetA()) {
				arrayConverted[offsetB + arrayComponentOrderB.getOffsetA()] = a;
			}
		}
		
		return arrayConverted;
	}
}