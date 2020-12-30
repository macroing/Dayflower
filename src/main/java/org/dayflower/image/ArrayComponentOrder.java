/**
 * Copyright 2020 - 2021 J&#246;rgen Lundgren
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

/**
 * An {@code ArrayComponentOrder} is used to tell us what order the R-, G-, B- and A-components are stored in arrays.
 * <p>
 * This class has nothing to do with the way the R-, G-, B- and A-components are stored in an {@code int}, in packed form.
 * <p>
 * The names of the constants in this class should not be confused with the names of similar things in other libraries. They only reflect the way the components are stored in an array. The order of the letters signify the order in which they
 * are stored in the array, starting from some offset. An example would be {@code ARGB}, where {@code A} denotes {@code offset + 0}, {@code R} denotes {@code offset + 1}, {@code G} denotes {@code offset + 2} and {@code B} denotes
 * {@code offset + 3}.
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
	public boolean hasOffsetB() {
		return this.offsetB != -1;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code ArrayComponentOrder} has an offset for the G-component, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code ArrayComponentOrder} has an offset for the G-component, {@code false} otherwise
	 */
	public boolean hasOffsetG() {
		return this.offsetG != -1;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code ArrayComponentOrder} has an offset for the R-component, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code ArrayComponentOrder} has an offset for the R-component, {@code false} otherwise
	 */
	public boolean hasOffsetR() {
		return this.offsetR != -1;
	}
	
	/**
	 * Returns the component count of this {@code ArrayComponentOrder} instance.
	 * 
	 * @return the component count of this {@code ArrayComponentOrder} instance
	 */
	public int getComponentCount() {
		return (hasOffsetR() ? 1 : 0) + (hasOffsetG() ? 1 : 0) + (hasOffsetB() ? 1 : 0) + (hasOffsetA() ? 1 : 0);
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
	 * Returns an {@code int} with the A-component, or {@code 255} if it does not have an offset for the A-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetA()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return an {@code int} with the A-component, or {@code 255} if it does not have an offset for the A-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetA()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public int readA(final byte[] array, final int offset) {
		return hasOffsetA() ? array[offset + getOffsetA()] & 0xFF : 255;
	}
	
	/**
	 * Returns an {@code int} with the A-component, or {@code 255} if it does not have an offset for the A-component.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offset + getOffsetA()} is less than {@code 0}, or greater than or equal to {@code array.length}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param offset the absolute offset in the array to read from
	 * @return an {@code int} with the A-component, or {@code 255} if it does not have an offset for the A-component
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code offset + getOffsetA()} is less than {@code 0}, or greater than or equal to {@code array.length}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public int readA(final int[] array, final int offset) {
		return hasOffsetA() ? array[offset + getOffsetA()] & 0xFF : 255;
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
	public int readB(final byte[] array, final int offset) {
		return hasOffsetB() ? array[offset + getOffsetB()] & 0xFF : 0;
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
		return hasOffsetB() ? array[offset + getOffsetB()] & 0xFF : 0;
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
	public int readG(final byte[] array, final int offset) {
		return hasOffsetG() ? array[offset + getOffsetG()] & 0xFF : 0;
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
		return hasOffsetG() ? array[offset + getOffsetG()] & 0xFF : 0;
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
	public int readR(final byte[] array, final int offset) {
		return hasOffsetR() ? array[offset + getOffsetR()] & 0xFF : 0;
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
		return hasOffsetR() ? array[offset + getOffsetR()] & 0xFF : 0;
	}
}