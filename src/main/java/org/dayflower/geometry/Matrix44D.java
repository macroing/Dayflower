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
package org.dayflower.geometry;

import static org.dayflower.utility.Doubles.abs;
import static org.dayflower.utility.Doubles.cos;
import static org.dayflower.utility.Doubles.equal;
import static org.dayflower.utility.Doubles.sin;
import static org.dayflower.utility.Doubles.toRadians;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;

import org.dayflower.java.lang.Strings;
import org.dayflower.node.Node;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code Matrix44D} represents a 4 x 4 matrix with 16 {@code double}-based elements.
 * <p>
 * The default order of this {@code Matrix44D} class is row-major.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Matrix44D implements Node {
	/**
	 * The offset for the element at index 0 or row 1 and column 1 in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_ELEMENT_1_1 = 0;
	
	/**
	 * The offset for the element at index 1 or row 1 and column 2 in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_ELEMENT_1_2 = 1;
	
	/**
	 * The offset for the element at index 2 or row 1 and column 3 in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_ELEMENT_1_3 = 2;
	
	/**
	 * The offset for the element at index 3 or row 1 and column 4 in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_ELEMENT_1_4 = 3;
	
	/**
	 * The offset for the element at index 4 or row 2 and column 1 in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_ELEMENT_2_1 = 4;
	
	/**
	 * The offset for the element at index 5 or row 2 and column 2 in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_ELEMENT_2_2 = 5;
	
	/**
	 * The offset for the element at index 6 or row 2 and column 3 in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_ELEMENT_2_3 = 6;
	
	/**
	 * The offset for the element at index 7 or row 2 and column 4 in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_ELEMENT_2_4 = 7;
	
	/**
	 * The offset for the element at index 8 or row 3 and column 1 in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_ELEMENT_3_1 = 8;
	
	/**
	 * The offset for the element at index 9 or row 3 and column 2 in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_ELEMENT_3_2 = 9;
	
	/**
	 * The offset for the element at index 10 or row 3 and column 3 in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_ELEMENT_3_3 = 10;
	
	/**
	 * The offset for the element at index 11 or row 3 and column 4 in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_ELEMENT_3_4 = 11;
	
	/**
	 * The offset for the element at index 12 or row 4 and column 1 in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_ELEMENT_4_1 = 12;
	
	/**
	 * The offset for the element at index 13 or row 4 and column 2 in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_ELEMENT_4_2 = 13;
	
	/**
	 * The offset for the element at index 14 or row 4 and column 3 in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_ELEMENT_4_3 = 14;
	
	/**
	 * The offset for the element at index 15 or row 4 and column 4 in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_ELEMENT_4_4 = 15;
	
	/**
	 * The size of the {@code double[]}.
	 */
	public static final int ARRAY_SIZE = 16;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final double element11;
	private final double element12;
	private final double element13;
	private final double element14;
	private final double element21;
	private final double element22;
	private final double element23;
	private final double element24;
	private final double element31;
	private final double element32;
	private final double element33;
	private final double element34;
	private final double element41;
	private final double element42;
	private final double element43;
	private final double element44;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Matrix44D} instance denoting the identity matrix.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Matrix44D(1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D);
	 * }
	 * </pre>
	 */
	public Matrix44D() {
		this(1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D);
	}
	
	/**
	 * Constructs a new {@code Matrix44D} instance given its element values.
	 * 
	 * @param element11 the value of the element at index 0 or row 1 and column 1
	 * @param element12 the value of the element at index 1 or row 1 and column 2
	 * @param element13 the value of the element at index 2 or row 1 and column 3
	 * @param element14 the value of the element at index 3 or row 1 and column 4
	 * @param element21 the value of the element at index 4 or row 2 and column 1
	 * @param element22 the value of the element at index 5 or row 2 and column 2
	 * @param element23 the value of the element at index 6 or row 2 and column 3
	 * @param element24 the value of the element at index 7 or row 2 and column 4
	 * @param element31 the value of the element at index 8 or row 3 and column 1
	 * @param element32 the value of the element at index 9 or row 3 and column 2
	 * @param element33 the value of the element at index 10 or row 3 and column 3
	 * @param element34 the value of the element at index 11 or row 3 and column 4
	 * @param element41 the value of the element at index 12 or row 4 and column 1
	 * @param element42 the value of the element at index 13 or row 4 and column 2
	 * @param element43 the value of the element at index 14 or row 4 and column 3
	 * @param element44 the value of the element at index 15 or row 4 and column 4
	 */
	public Matrix44D(final double element11, final double element12, final double element13, final double element14, final double element21, final double element22, final double element23, final double element24, final double element31, final double element32, final double element33, final double element34, final double element41, final double element42, final double element43, final double element44) {
		this.element11 = element11;
		this.element12 = element12;
		this.element13 = element13;
		this.element14 = element14;
		this.element21 = element21;
		this.element22 = element22;
		this.element23 = element23;
		this.element24 = element24;
		this.element31 = element31;
		this.element32 = element32;
		this.element33 = element33;
		this.element34 = element34;
		this.element41 = element41;
		this.element42 = element42;
		this.element43 = element43;
		this.element44 = element44;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Matrix44D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Matrix44D} instance
	 */
	@Override
	public String toString() {
		final String row1 = String.format("%s, %s, %s, %s", Strings.toNonScientificNotationJava(this.element11), Strings.toNonScientificNotationJava(this.element12), Strings.toNonScientificNotationJava(this.element13), Strings.toNonScientificNotationJava(this.element14));
		final String row2 = String.format("%s, %s, %s, %s", Strings.toNonScientificNotationJava(this.element21), Strings.toNonScientificNotationJava(this.element22), Strings.toNonScientificNotationJava(this.element23), Strings.toNonScientificNotationJava(this.element24));
		final String row3 = String.format("%s, %s, %s, %s", Strings.toNonScientificNotationJava(this.element31), Strings.toNonScientificNotationJava(this.element32), Strings.toNonScientificNotationJava(this.element33), Strings.toNonScientificNotationJava(this.element34));
		final String row4 = String.format("%s, %s, %s, %s", Strings.toNonScientificNotationJava(this.element41), Strings.toNonScientificNotationJava(this.element42), Strings.toNonScientificNotationJava(this.element43), Strings.toNonScientificNotationJava(this.element44));
		
		return String.format("new Matrix44D(%s, %s, %s, %s)", row1, row2, row3, row4);
	}
	
	/**
	 * Compares {@code object} to this {@code Matrix44D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Matrix44D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Matrix44D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Matrix44D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Matrix44D)) {
			return false;
		} else if(!equal(this.element11, Matrix44D.class.cast(object).element11)) {
			return false;
		} else if(!equal(this.element12, Matrix44D.class.cast(object).element12)) {
			return false;
		} else if(!equal(this.element13, Matrix44D.class.cast(object).element13)) {
			return false;
		} else if(!equal(this.element14, Matrix44D.class.cast(object).element14)) {
			return false;
		} else if(!equal(this.element21, Matrix44D.class.cast(object).element21)) {
			return false;
		} else if(!equal(this.element22, Matrix44D.class.cast(object).element22)) {
			return false;
		} else if(!equal(this.element23, Matrix44D.class.cast(object).element23)) {
			return false;
		} else if(!equal(this.element24, Matrix44D.class.cast(object).element24)) {
			return false;
		} else if(!equal(this.element31, Matrix44D.class.cast(object).element31)) {
			return false;
		} else if(!equal(this.element32, Matrix44D.class.cast(object).element32)) {
			return false;
		} else if(!equal(this.element33, Matrix44D.class.cast(object).element33)) {
			return false;
		} else if(!equal(this.element34, Matrix44D.class.cast(object).element34)) {
			return false;
		} else if(!equal(this.element41, Matrix44D.class.cast(object).element41)) {
			return false;
		} else if(!equal(this.element42, Matrix44D.class.cast(object).element42)) {
			return false;
		} else if(!equal(this.element43, Matrix44D.class.cast(object).element43)) {
			return false;
		} else if(!equal(this.element44, Matrix44D.class.cast(object).element44)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Matrix44D} instance is invertible, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Matrix44D} instance is invertible, {@code false} otherwise
	 */
	public boolean isInvertible() {
		return abs(determinant()) >= 1.0e-12D;
	}
	
	/**
	 * Returns the determinant of this {@code Matrix44D} instance.
	 * 
	 * @return the determinant of this {@code Matrix44D} instance
	 */
//	TODO: Add Unit Tests!
	public double determinant() {
		final double a = this.element11 * this.element22 - this.element12 * this.element21;
		final double b = this.element11 * this.element23 - this.element13 * this.element21;
		final double c = this.element11 * this.element24 - this.element14 * this.element21;
		final double d = this.element12 * this.element23 - this.element13 * this.element22;
		final double e = this.element12 * this.element24 - this.element14 * this.element22;
		final double f = this.element13 * this.element24 - this.element14 * this.element23;
		final double g = this.element31 * this.element42 - this.element32 * this.element41;
		final double h = this.element31 * this.element43 - this.element33 * this.element41;
		final double i = this.element31 * this.element44 - this.element34 * this.element41;
		final double j = this.element32 * this.element43 - this.element33 * this.element42;
		final double k = this.element32 * this.element44 - this.element34 * this.element42;
		final double l = this.element33 * this.element44 - this.element34 * this.element43;
		
		return a * l - b * k + c * j + d * i - e * h + f * g;
	}
	
	/**
	 * Returns the value of the element at index {@code index}.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than {@code 15}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param index the index of the element whose value to return
	 * @return the value of the element at index {@code index}
	 * @throws IllegalArgumentException thrown if, and only if, {@code index} is less than {@code 0} or greater than {@code 15}
	 */
	public double getElement(final int index) {
		switch(index) {
			case 0:
				return this.element11;
			case 1:
				return this.element12;
			case 2:
				return this.element13;
			case 3:
				return this.element14;
			case 4:
				return this.element21;
			case 5:
				return this.element22;
			case 6:
				return this.element23;
			case 7:
				return this.element24;
			case 8:
				return this.element31;
			case 9:
				return this.element32;
			case 10:
				return this.element33;
			case 11:
				return this.element34;
			case 12:
				return this.element41;
			case 13:
				return this.element42;
			case 14:
				return this.element43;
			case 15:
				return this.element44;
			default:
				throw new IllegalArgumentException(String.format("Illegal index: index=%s", Integer.toString(index)));
		}
	}
	
	/**
	 * Returns the value of the element at row {@code row} and column {@code column}.
	 * <p>
	 * If either {@code row} or {@code column} are less than {@code 1} or greater than {@code 4}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * <strong>Note:</strong> Both {@code row} and {@code column} starts at {@code 1}, rather than {@code 0}, as is sometimes the case.
	 * 
	 * @param row the row of the element whose value to return
	 * @param column the column of the element whose value to return
	 * @return the value of the element at row {@code row} and column {@code column}
	 * @throws IllegalArgumentException thrown if, and only if, either {@code row} or {@code column} are less than {@code 1} or greater than {@code 4}
	 */
	public double getElement(final int row, final int column) {
		ParameterArguments.requireRange(row, 1, 4, "row");
		ParameterArguments.requireRange(column, 1, 4, "column");
		
		return getElement((row - 1) * 4 + (column - 1));
	}
	
	/**
	 * Returns the value of the element at index 0 or row 1 and column 1.
	 * 
	 * @return the value of the element at index 0 or row 1 and column 1
	 */
	public double getElement11() {
		return this.element11;
	}
	
	/**
	 * Returns the value of the element at index 1 or row 1 and column 2.
	 * 
	 * @return the value of the element at index 1 or row 1 and column 2
	 */
	public double getElement12() {
		return this.element12;
	}
	
	/**
	 * Returns the value of the element at index 2 or row 1 and column 3.
	 * 
	 * @return the value of the element at index 2 or row 1 and column 3
	 */
	public double getElement13() {
		return this.element13;
	}
	
	/**
	 * Returns the value of the element at index 3 or row 1 and column 4.
	 * 
	 * @return the value of the element at index 3 or row 1 and column 4
	 */
	public double getElement14() {
		return this.element14;
	}
	
	/**
	 * Returns the value of the element at index 4 or row 2 and column 1.
	 * 
	 * @return the value of the element at index 4 or row 2 and column 1
	 */
	public double getElement21() {
		return this.element21;
	}
	
	/**
	 * Returns the value of the element at index 5 or row 2 and column 2.
	 * 
	 * @return the value of the element at index 5 or row 2 and column 2
	 */
	public double getElement22() {
		return this.element22;
	}
	
	/**
	 * Returns the value of the element at index 6 or row 2 and column 3.
	 * 
	 * @return the value of the element at index 6 or row 2 and column 3
	 */
	public double getElement23() {
		return this.element23;
	}
	
	/**
	 * Returns the value of the element at index 7 or row 2 and column 4.
	 * 
	 * @return the value of the element at index 7 or row 2 and column 4
	 */
	public double getElement24() {
		return this.element24;
	}
	
	/**
	 * Returns the value of the element at index 8 or row 3 and column 1.
	 * 
	 * @return the value of the element at index 8 or row 3 and column 1
	 */
	public double getElement31() {
		return this.element31;
	}
	
	/**
	 * Returns the value of the element at index 9 or row 3 and column 2.
	 * 
	 * @return the value of the element at index 9 or row 3 and column 2
	 */
	public double getElement32() {
		return this.element32;
	}
	
	/**
	 * Returns the value of the element at index 10 or row 3 and column 3.
	 * 
	 * @return the value of the element at index 10 or row 3 and column 3
	 */
	public double getElement33() {
		return this.element33;
	}
	
	/**
	 * Returns the value of the element at index 11 or row 3 and column 4.
	 * 
	 * @return the value of the element at index 11 or row 3 and column 4
	 */
	public double getElement34() {
		return this.element34;
	}
	
	/**
	 * Returns the value of the element at index 12 or row 4 and column 1.
	 * 
	 * @return the value of the element at index 12 or row 4 and column 1
	 */
	public double getElement41() {
		return this.element41;
	}
	
	/**
	 * Returns the value of the element at index 13 or row 4 and column 2.
	 * 
	 * @return the value of the element at index 13 or row 4 and column 2
	 */
	public double getElement42() {
		return this.element42;
	}
	
	/**
	 * Returns the value of the element at index 14 or row 4 and column 3.
	 * 
	 * @return the value of the element at index 14 or row 4 and column 3
	 */
	public double getElement43() {
		return this.element43;
	}
	
	/**
	 * Returns the value of the element at index 15 or row 4 and column 4.
	 * 
	 * @return the value of the element at index 15 or row 4 and column 4
	 */
	public double getElement44() {
		return this.element44;
	}
	
	/**
	 * Returns a {@code double[]} representation of this {@code Matrix44D} instance.
	 * 
	 * @return a {@code double[]} representation of this {@code Matrix44D} instance
	 */
	public double[] toArray() {
		final double[] array = new double[ARRAY_SIZE];
		
		array[ARRAY_OFFSET_ELEMENT_1_1] = this.element11;
		array[ARRAY_OFFSET_ELEMENT_1_2] = this.element12;
		array[ARRAY_OFFSET_ELEMENT_1_3] = this.element13;
		array[ARRAY_OFFSET_ELEMENT_1_4] = this.element14;
		array[ARRAY_OFFSET_ELEMENT_2_1] = this.element21;
		array[ARRAY_OFFSET_ELEMENT_2_2] = this.element22;
		array[ARRAY_OFFSET_ELEMENT_2_3] = this.element23;
		array[ARRAY_OFFSET_ELEMENT_2_4] = this.element24;
		array[ARRAY_OFFSET_ELEMENT_3_1] = this.element31;
		array[ARRAY_OFFSET_ELEMENT_3_2] = this.element32;
		array[ARRAY_OFFSET_ELEMENT_3_3] = this.element33;
		array[ARRAY_OFFSET_ELEMENT_3_4] = this.element34;
		array[ARRAY_OFFSET_ELEMENT_4_1] = this.element41;
		array[ARRAY_OFFSET_ELEMENT_4_2] = this.element42;
		array[ARRAY_OFFSET_ELEMENT_4_3] = this.element43;
		array[ARRAY_OFFSET_ELEMENT_4_4] = this.element44;
		
		return array;
	}
	
	/**
	 * Returns a hash code for this {@code Matrix44D} instance.
	 * 
	 * @return a hash code for this {@code Matrix44D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(new Object[] {
			Double.valueOf(this.element11), Double.valueOf(this.element12), Double.valueOf(this.element13), Double.valueOf(this.element14),
			Double.valueOf(this.element21), Double.valueOf(this.element22), Double.valueOf(this.element23), Double.valueOf(this.element24),
			Double.valueOf(this.element31), Double.valueOf(this.element32), Double.valueOf(this.element33), Double.valueOf(this.element34),
			Double.valueOf(this.element41), Double.valueOf(this.element42), Double.valueOf(this.element43), Double.valueOf(this.element44)
		});
	}
	
	/**
	 * Writes this {@code Matrix44D} instance to {@code dataOutput}.
	 * <p>
	 * If {@code dataOutput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataOutput the {@code DataOutput} instance to write to
	 * @throws NullPointerException thrown if, and only if, {@code dataOutput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public void write(final DataOutput dataOutput) {
		try {
			dataOutput.writeDouble(this.element11);
			dataOutput.writeDouble(this.element12);
			dataOutput.writeDouble(this.element13);
			dataOutput.writeDouble(this.element14);
			dataOutput.writeDouble(this.element21);
			dataOutput.writeDouble(this.element22);
			dataOutput.writeDouble(this.element23);
			dataOutput.writeDouble(this.element24);
			dataOutput.writeDouble(this.element31);
			dataOutput.writeDouble(this.element32);
			dataOutput.writeDouble(this.element33);
			dataOutput.writeDouble(this.element34);
			dataOutput.writeDouble(this.element41);
			dataOutput.writeDouble(this.element42);
			dataOutput.writeDouble(this.element43);
			dataOutput.writeDouble(this.element44);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code Matrix44D} instance denoting the identity matrix.
	 * 
	 * @return a new {@code Matrix44D} instance denoting the identity matrix
	 */
	public static Matrix44D identity() {
		return new Matrix44D();
	}
	
	/**
	 * Returns a new {@code Matrix44D} instance that is the inverse of {@code matrix}.
	 * <p>
	 * If {@code matrix} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix} cannot be inverted, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * To make sure {@code matrix} is invertible, consider calling {@link #isInvertible()}.
	 * 
	 * @param matrix a {@code Matrix44D} instance
	 * @return a new {@code Matrix44D} instance that is the inverse of {@code matrix}
	 * @throws IllegalArgumentException thrown if, and only if, {@code matrix} cannot be inverted
	 * @throws NullPointerException thrown if, and only if, {@code matrix} is {@code null}
	 */
	public static Matrix44D inverse(final Matrix44D matrix) {
		final double a = matrix.element11 * matrix.element22 - matrix.element12 * matrix.element21;
		final double b = matrix.element11 * matrix.element23 - matrix.element13 * matrix.element21;
		final double c = matrix.element11 * matrix.element24 - matrix.element14 * matrix.element21;
		final double d = matrix.element12 * matrix.element23 - matrix.element13 * matrix.element22;
		final double e = matrix.element12 * matrix.element24 - matrix.element14 * matrix.element22;
		final double f = matrix.element13 * matrix.element24 - matrix.element14 * matrix.element23;
		final double g = matrix.element31 * matrix.element42 - matrix.element32 * matrix.element41;
		final double h = matrix.element31 * matrix.element43 - matrix.element33 * matrix.element41;
		final double i = matrix.element31 * matrix.element44 - matrix.element34 * matrix.element41;
		final double j = matrix.element32 * matrix.element43 - matrix.element33 * matrix.element42;
		final double k = matrix.element32 * matrix.element44 - matrix.element34 * matrix.element42;
		final double l = matrix.element33 * matrix.element44 - matrix.element34 * matrix.element43;
		
		final double determinant = a * l - b * k + c * j + d * i - e * h + f * g;
		final double determinantReciprocal = 1.0D / determinant;
		
		if(abs(determinant) < 1.0e-12D) {
			throw new IllegalArgumentException("The Matrix44D 'matrix' cannot be inverted!");
		}
		
		final double element11 = (+matrix.element22 * l - matrix.element23 * k + matrix.element24 * j) * determinantReciprocal;
		final double element12 = (-matrix.element12 * l + matrix.element13 * k - matrix.element14 * j) * determinantReciprocal;
		final double element13 = (+matrix.element42 * f - matrix.element43 * e + matrix.element44 * d) * determinantReciprocal;
		final double element14 = (-matrix.element32 * f + matrix.element33 * e - matrix.element34 * d) * determinantReciprocal;
		final double element21 = (-matrix.element21 * l + matrix.element23 * i - matrix.element24 * h) * determinantReciprocal;
		final double element22 = (+matrix.element11 * l - matrix.element13 * i + matrix.element14 * h) * determinantReciprocal;
		final double element23 = (-matrix.element41 * f + matrix.element43 * c - matrix.element44 * b) * determinantReciprocal;
		final double element24 = (+matrix.element31 * f - matrix.element33 * c + matrix.element34 * b) * determinantReciprocal;
		final double element31 = (+matrix.element21 * k - matrix.element22 * i + matrix.element24 * g) * determinantReciprocal;
		final double element32 = (-matrix.element11 * k + matrix.element12 * i - matrix.element14 * g) * determinantReciprocal;
		final double element33 = (+matrix.element41 * e - matrix.element42 * c + matrix.element44 * a) * determinantReciprocal;
		final double element34 = (-matrix.element31 * e + matrix.element32 * c - matrix.element34 * a) * determinantReciprocal;
		final double element41 = (-matrix.element21 * j + matrix.element22 * h - matrix.element23 * g) * determinantReciprocal;
		final double element42 = (+matrix.element11 * j - matrix.element12 * h + matrix.element13 * g) * determinantReciprocal;
		final double element43 = (-matrix.element41 * d + matrix.element42 * b - matrix.element43 * a) * determinantReciprocal;
		final double element44 = (+matrix.element31 * d - matrix.element32 * b + matrix.element33 * a) * determinantReciprocal;
		
		return new Matrix44D(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44);
	}
	
	/**
	 * Returns a new {@code Matrix44D} instance that looks in the direction of {@code eye} to {@code lookAt} and has an up-direction of {@code up}.
	 * <p>
	 * If either {@code eye}, {@code lookAt} or {@code up} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point3D} instance that represents the eye to look from
	 * @param lookAt a {@code Point3D} instance that represents the point to look at
	 * @param up a {@link Vector3D} instance that represents the up-direction
	 * @return a new {@code Matrix44D} instance that looks in the direction of {@code eye} to {@code lookAt} and has an up-direction of {@code up}
	 * @throws NullPointerException thrown if, and only if, either {@code eye}, {@code lookAt} or {@code up} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Matrix44D lookAt(final Point3D eye, final Point3D lookAt, final Vector3D up) {
		final Vector3D w = Vector3D.directionNormalized(eye, lookAt);
		final Vector3D u = Vector3D.normalize(Vector3D.crossProduct(Vector3D.normalize(up), w));
		final Vector3D v = Vector3D.crossProduct(w, u);
		
		final double element11 = u.getX();
		final double element12 = v.getX();
		final double element13 = w.getX();
		final double element14 = eye.getX();
		final double element21 = u.getY();
		final double element22 = v.getY();
		final double element23 = w.getY();
		final double element24 = eye.getY();
		final double element31 = u.getZ();
		final double element32 = v.getZ();
		final double element33 = w.getZ();
		final double element34 = eye.getZ();
		final double element41 = 0.0D;
		final double element42 = 0.0D;
		final double element43 = 0.0D;
		final double element44 = 1.0D;
		
		return new Matrix44D(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44);
	}
	
	/**
	 * Multiplies the element values of {@code matrixLHS} with the element values of {@code matrixRHS}.
	 * <p>
	 * Returns a new {@code Matrix44D} instance with the result of the multiplication.
	 * <p>
	 * If either {@code matrixLHS} or {@code matrixRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS the {@code Matrix44D} instance on the left-hand side
	 * @param matrixRHS the {@code Matrix44D} instance on the right-hand side
	 * @return a new {@code Matrix44D} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code matrixRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Matrix44D multiply(final Matrix44D matrixLHS, final Matrix44D matrixRHS) {
		final double element11 = matrixLHS.element11 * matrixRHS.element11 + matrixLHS.element12 * matrixRHS.element21 + matrixLHS.element13 * matrixRHS.element31 + matrixLHS.element14 * matrixRHS.element41;
		final double element12 = matrixLHS.element11 * matrixRHS.element12 + matrixLHS.element12 * matrixRHS.element22 + matrixLHS.element13 * matrixRHS.element32 + matrixLHS.element14 * matrixRHS.element42;
		final double element13 = matrixLHS.element11 * matrixRHS.element13 + matrixLHS.element12 * matrixRHS.element23 + matrixLHS.element13 * matrixRHS.element33 + matrixLHS.element14 * matrixRHS.element43;
		final double element14 = matrixLHS.element11 * matrixRHS.element14 + matrixLHS.element12 * matrixRHS.element24 + matrixLHS.element13 * matrixRHS.element34 + matrixLHS.element14 * matrixRHS.element44;
		final double element21 = matrixLHS.element21 * matrixRHS.element11 + matrixLHS.element22 * matrixRHS.element21 + matrixLHS.element23 * matrixRHS.element31 + matrixLHS.element24 * matrixRHS.element41;
		final double element22 = matrixLHS.element21 * matrixRHS.element12 + matrixLHS.element22 * matrixRHS.element22 + matrixLHS.element23 * matrixRHS.element32 + matrixLHS.element24 * matrixRHS.element42;
		final double element23 = matrixLHS.element21 * matrixRHS.element13 + matrixLHS.element22 * matrixRHS.element23 + matrixLHS.element23 * matrixRHS.element33 + matrixLHS.element24 * matrixRHS.element43;
		final double element24 = matrixLHS.element21 * matrixRHS.element14 + matrixLHS.element22 * matrixRHS.element24 + matrixLHS.element23 * matrixRHS.element34 + matrixLHS.element24 * matrixRHS.element44;
		final double element31 = matrixLHS.element31 * matrixRHS.element11 + matrixLHS.element32 * matrixRHS.element21 + matrixLHS.element33 * matrixRHS.element31 + matrixLHS.element34 * matrixRHS.element41;
		final double element32 = matrixLHS.element31 * matrixRHS.element12 + matrixLHS.element32 * matrixRHS.element22 + matrixLHS.element33 * matrixRHS.element32 + matrixLHS.element34 * matrixRHS.element42;
		final double element33 = matrixLHS.element31 * matrixRHS.element13 + matrixLHS.element32 * matrixRHS.element23 + matrixLHS.element33 * matrixRHS.element33 + matrixLHS.element34 * matrixRHS.element43;
		final double element34 = matrixLHS.element31 * matrixRHS.element14 + matrixLHS.element32 * matrixRHS.element24 + matrixLHS.element33 * matrixRHS.element34 + matrixLHS.element34 * matrixRHS.element44;
		final double element41 = matrixLHS.element41 * matrixRHS.element11 + matrixLHS.element42 * matrixRHS.element21 + matrixLHS.element43 * matrixRHS.element31 + matrixLHS.element44 * matrixRHS.element41;
		final double element42 = matrixLHS.element41 * matrixRHS.element12 + matrixLHS.element42 * matrixRHS.element22 + matrixLHS.element43 * matrixRHS.element32 + matrixLHS.element44 * matrixRHS.element42;
		final double element43 = matrixLHS.element41 * matrixRHS.element13 + matrixLHS.element42 * matrixRHS.element23 + matrixLHS.element43 * matrixRHS.element33 + matrixLHS.element44 * matrixRHS.element43;
		final double element44 = matrixLHS.element41 * matrixRHS.element14 + matrixLHS.element42 * matrixRHS.element24 + matrixLHS.element43 * matrixRHS.element34 + matrixLHS.element44 * matrixRHS.element44;
		
		return new Matrix44D(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44);
	}
	
	/**
	 * Returns a new {@code Matrix44D} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Matrix44D} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Matrix44D read(final DataInput dataInput) {
		try {
			final double element11 = dataInput.readDouble();
			final double element12 = dataInput.readDouble();
			final double element13 = dataInput.readDouble();
			final double element14 = dataInput.readDouble();
			final double element21 = dataInput.readDouble();
			final double element22 = dataInput.readDouble();
			final double element23 = dataInput.readDouble();
			final double element24 = dataInput.readDouble();
			final double element31 = dataInput.readDouble();
			final double element32 = dataInput.readDouble();
			final double element33 = dataInput.readDouble();
			final double element34 = dataInput.readDouble();
			final double element41 = dataInput.readDouble();
			final double element42 = dataInput.readDouble();
			final double element43 = dataInput.readDouble();
			final double element44 = dataInput.readDouble();
			
			return new Matrix44D(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Returns a {@code Matrix44D} instance that rotates along the axis represented by {@code vector}.
	 * <p>
	 * If either {@code angle} or {@code vector} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param angle an {@link AngleD} instance
	 * @param vector a {@link Vector3D} instance that represents an axis
	 * @return a {@code Matrix44D} instance that rotates along the axis represented by {@code vector}
	 * @throws NullPointerException thrown if, and only if, either {@code angle} or {@code vector} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Matrix44D rotate(final AngleD angle, final Vector3D vector) {
		final Vector3D vectorNormalized = Vector3D.normalize(vector);
		
		final double cos = cos(angle.getRadians());
		final double sin = sin(angle.getRadians());
		final double oneMinusCos = 1.0D - cos;
		
		final double element11 = oneMinusCos * vectorNormalized.getX() * vectorNormalized.getX() + cos;
		final double element12 = oneMinusCos * vectorNormalized.getX() * vectorNormalized.getY() - sin * vectorNormalized.getZ();
		final double element13 = oneMinusCos * vectorNormalized.getX() * vectorNormalized.getZ() + sin * vectorNormalized.getY();
		final double element14 = 0.0D;
		final double element21 = oneMinusCos * vectorNormalized.getX() * vectorNormalized.getY() + sin * vectorNormalized.getZ();
		final double element22 = oneMinusCos * vectorNormalized.getY() * vectorNormalized.getY() + cos;
		final double element23 = oneMinusCos * vectorNormalized.getY() * vectorNormalized.getZ() - sin * vectorNormalized.getX();
		final double element24 = 0.0D;
		final double element31 = oneMinusCos * vectorNormalized.getX() * vectorNormalized.getZ() - sin * vectorNormalized.getY();
		final double element32 = oneMinusCos * vectorNormalized.getY() * vectorNormalized.getZ() + sin * vectorNormalized.getX();
		final double element33 = oneMinusCos * vectorNormalized.getZ() * vectorNormalized.getZ() + cos;
		final double element34 = 0.0D;
		final double element41 = 0.0D;
		final double element42 = 0.0D;
		final double element43 = 0.0D;
		final double element44 = 1.0D;
		
		return new Matrix44D(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44);
	}
	
	/**
	 * Returns a {@code Matrix44D} instance that rotates along the axis represented by {@code x}, {@code y} and {@code z}.
	 * <p>
	 * If {@code angle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix44D.rotate(angle, new Vector3D(x, y, z));
	 * }
	 * </pre>
	 * 
	 * @param angle an {@link AngleD} instance
	 * @param x the component value along the X-axis
	 * @param y the component value along the Y-axis
	 * @param z the component value along the Z-axis
	 * @return a {@code Matrix44D} instance that rotates along the axis represented by {@code x}, {@code y} and {@code z}
	 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Matrix44D rotate(final AngleD angle, final double x, final double y, final double z) {
		return rotate(angle, new Vector3D(x, y, z));
	}
	
	/**
	 * Returns a {@code Matrix44D} instance that rotates using {@code orthonormalBasis}.
	 * <p>
	 * If {@code orthonormalBasis} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param orthonormalBasis an {@link OrthonormalBasis33D} instance
	 * @return a {@code Matrix44D} instance that rotates using {@code orthonormalBasis}
	 * @throws NullPointerException thrown if, and only if, {@code orthonormalBasis} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Matrix44D rotate(final OrthonormalBasis33D orthonormalBasis) {
		final Vector3D u = Vector3D.transform(Vector3D.u(), orthonormalBasis);
		final Vector3D v = Vector3D.transform(Vector3D.v(), orthonormalBasis);
		final Vector3D w = Vector3D.transform(Vector3D.w(), orthonormalBasis);
		
		return rotate(w, v, u);
	}
	
	/**
	 * Returns a {@code Matrix44D} instance that rotates using {@code quaternion}.
	 * <p>
	 * If {@code quaternion} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param quaternion a {@link Quaternion4D} instance
	 * @return a {@code Matrix44D} instance that rotates using {@code quaternion}
	 * @throws NullPointerException thrown if, and only if, {@code quaternion} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Matrix44D rotate(final Quaternion4D quaternion) {
		final double uX = 1.0D - 2.0D * (quaternion.getY() * quaternion.getY() + quaternion.getZ() * quaternion.getZ());
		final double uY = 0.0D + 2.0D * (quaternion.getX() * quaternion.getY() - quaternion.getW() * quaternion.getZ());
		final double uZ = 0.0D + 2.0D * (quaternion.getX() * quaternion.getZ() + quaternion.getW() * quaternion.getY());
		final double vX = 0.0D + 2.0D * (quaternion.getX() * quaternion.getY() + quaternion.getW() * quaternion.getZ());
		final double vY = 1.0D - 2.0D * (quaternion.getX() * quaternion.getX() + quaternion.getZ() * quaternion.getZ());
		final double vZ = 0.0D + 2.0D * (quaternion.getY() * quaternion.getZ() - quaternion.getW() * quaternion.getX());
		final double wX = 0.0D + 2.0D * (quaternion.getX() * quaternion.getZ() - quaternion.getW() * quaternion.getY());
		final double wY = 0.0D + 2.0D * (quaternion.getY() * quaternion.getZ() + quaternion.getW() * quaternion.getX());
		final double wZ = 1.0D - 2.0D * (quaternion.getX() * quaternion.getX() + quaternion.getY() * quaternion.getY());
		
		final Vector3D u = new Vector3D(uX, uY, uZ);
		final Vector3D v = new Vector3D(vX, vY, vZ);
		final Vector3D w = new Vector3D(wX, wY, wZ);
		
		return rotate(w, v, u);
	}
	
	/**
	 * Returns a {@code Matrix44D} instance that rotates using {@code w} and {@code v}.
	 * <p>
	 * If either {@code w} or {@code v} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param w a {@link Vector3D} instance
	 * @param v a {@code Vector3D} instance
	 * @return a {@code Matrix44D} instance that rotates using {@code w} and {@code v}
	 * @throws NullPointerException thrown if, and only if, either {@code w} or {@code v} are {@code null}
	 */
	public static Matrix44D rotate(final Vector3D w, final Vector3D v) {
		final Vector3D wNormalized = Vector3D.normalize(w);
		final Vector3D uNormalized = Vector3D.normalize(Vector3D.crossProduct(Vector3D.normalize(v), wNormalized));
		final Vector3D vNormalized = Vector3D.crossProduct(wNormalized, uNormalized);
		
		return rotate(wNormalized, vNormalized, uNormalized);
	}
	
	/**
	 * Returns a {@code Matrix44D} instance that rotates using {@code w}, {@code v} and {@code u}.
	 * <p>
	 * If either {@code w}, {@code v} or {@code u} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The layout looks like this:
	 * <pre>
	 * {@code
	 * UX, VX, WX, 0
	 * UY, VY, WY, 0
	 * UZ, VZ, WZ, 0
	 *  0,  0,  0, 1
	 * }
	 * </pre>
	 * 
	 * @param w a {@link Vector3D} instance
	 * @param v a {@code Vector3D} instance
	 * @param u a {@code Vector3D} instance
	 * @return a {@code Matrix44D} instance that rotates using {@code w}, {@code v} and {@code u}
	 * @throws NullPointerException thrown if, and only if, either {@code w}, {@code v} or {@code u} are {@code null}
	 */
	public static Matrix44D rotate(final Vector3D w, final Vector3D v, final Vector3D u) {
		final double element11 = u.getX();
		final double element12 = v.getX();
		final double element13 = w.getX();
		final double element14 = 0.0D;
		final double element21 = u.getY();
		final double element22 = v.getY();
		final double element23 = w.getY();
		final double element24 = 0.0D;
		final double element31 = u.getZ();
		final double element32 = v.getZ();
		final double element33 = w.getZ();
		final double element34 = 0.0D;
		final double element41 = 0.0D;
		final double element42 = 0.0D;
		final double element43 = 0.0D;
		final double element44 = 1.0D;
		
		return new Matrix44D(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44);
	}
	
	/**
	 * Returns a {@code Matrix44D} instance that rotates along the X-axis.
	 * <p>
	 * If {@code angle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The layout looks like this:
	 * <pre>
	 * {@code
	 * 1,   0,    0, 0
	 * 0, cos, -sin, 0
	 * 0, sin,  cos, 0
	 * 0,   0,    0, 1
	 * }
	 * </pre>
	 * 
	 * @param angle an {@link AngleD} instance
	 * @return a {@code Matrix44D} instance that rotates along the X-axis
	 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Matrix44D rotateX(final AngleD angle) {
		final double cos = cos(angle.getRadians());
		final double sin = sin(angle.getRadians());
		
		return new Matrix44D(1.0D, 0.0D, 0.0D, 0.0D, 0.0D, cos, -sin, 0.0D, 0.0D, sin, cos, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D);
	}
	
	/**
	 * Returns a {@code Matrix44D} instance that rotates along the X-axis.
	 * <p>
	 * The layout looks like this:
	 * <pre>
	 * {@code
	 * 1,   0,    0, 0
	 * 0, cos, -sin, 0
	 * 0, sin,  cos, 0
	 * 0,   0,    0, 1
	 * }
	 * </pre>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix44D.rotateX(angle, false);
	 * }
	 * </pre>
	 * 
	 * @param angle an angle in degrees
	 * @return a {@code Matrix44D} instance that rotates along the X-axis
	 */
//	TODO: Add Unit Tests!
	public static Matrix44D rotateX(final double angle) {
		return rotateX(angle, false);
	}
	
	/**
	 * Returns a {@code Matrix44D} instance that rotates along the X-axis.
	 * <p>
	 * The layout looks like this:
	 * <pre>
	 * {@code
	 * 1,   0,    0, 0
	 * 0, cos, -sin, 0
	 * 0, sin,  cos, 0
	 * 0,   0,    0, 1
	 * }
	 * </pre>
	 * 
	 * @param angle an angle in degrees or radians
	 * @param isRadians {@code true} if, and only if, {@code angle} is in radians, {@code false} otherwise
	 * @return a {@code Matrix44D} instance that rotates along the X-axis
	 */
//	TODO: Add Unit Tests!
	public static Matrix44D rotateX(final double angle, final boolean isRadians) {
		final double angleRadians = isRadians ? angle : toRadians(angle);
		
		final double cos = cos(angleRadians);
		final double sin = sin(angleRadians);
		
		return new Matrix44D(1.0D, 0.0D, 0.0D, 0.0D, 0.0D, cos, -sin, 0.0D, 0.0D, sin, cos, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D);
	}
	
	/**
	 * Returns a {@code Matrix44D} instance that rotates along the Y-axis.
	 * <p>
	 * If {@code angle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The layout looks like this:
	 * <pre>
	 * {@code
	 *  cos, 0, sin, 0
	 *    0, 1,   0, 0
	 * -sin, 0, cos, 0
	 *    0, 0,   0, 1
	 * }
	 * </pre>
	 * 
	 * @param angle an {@link AngleD} instance
	 * @return a {@code Matrix44D} instance that rotates along the Y-axis
	 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Matrix44D rotateY(final AngleD angle) {
		final double cos = cos(angle.getRadians());
		final double sin = sin(angle.getRadians());
		
		return new Matrix44D(cos, 0.0D, sin, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, -sin, 0.0D, cos, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D);
	}
	
	/**
	 * Returns a {@code Matrix44D} instance that rotates along the Y-axis.
	 * <p>
	 * The layout looks like this:
	 * <pre>
	 * {@code
	 *  cos, 0, sin, 0
	 *    0, 1,   0, 0
	 * -sin, 0, cos, 0
	 *    0, 0,   0, 1
	 * }
	 * </pre>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix44D.rotateY(angle, false);
	 * }
	 * </pre>
	 * 
	 * @param angle an angle in degrees
	 * @return a {@code Matrix44D} instance that rotates along the Y-axis
	 */
//	TODO: Add Unit Tests!
	public static Matrix44D rotateY(final double angle) {
		return rotateY(angle, false);
	}
	
	/**
	 * Returns a {@code Matrix44D} instance that rotates along the Y-axis.
	 * <p>
	 * The layout looks like this:
	 * <pre>
	 * {@code
	 *  cos, 0, sin, 0
	 *    0, 1,   0, 0
	 * -sin, 0, cos, 0
	 *    0, 0,   0, 1
	 * }
	 * </pre>
	 * 
	 * @param angle an angle in degrees or radians
	 * @param isRadians {@code true} if, and only if, {@code angle} is in radians, {@code false} otherwise
	 * @return a {@code Matrix44D} instance that rotates along the Y-axis
	 */
//	TODO: Add Unit Tests!
	public static Matrix44D rotateY(final double angle, final boolean isRadians) {
		final double angleRadians = isRadians ? angle : toRadians(angle);
		
		final double cos = cos(angleRadians);
		final double sin = sin(angleRadians);
		
		return new Matrix44D(cos, 0.0D, sin, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, -sin, 0.0D, cos, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D);
	}
	
	/**
	 * Returns a {@code Matrix44D} instance that rotates along the Z-axis.
	 * <p>
	 * If {@code angle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The layout looks like this:
	 * <pre>
	 * {@code
	 * cos, -sin, 0, 0
	 * sin,  cos, 0, 0
	 *   0,    0, 1, 0
	 *   0,    0, 0, 1
	 * }
	 * </pre>
	 * 
	 * @param angle an {@link AngleD} instance
	 * @return a {@code Matrix44D} instance that rotates along the Z-axis
	 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Matrix44D rotateZ(final AngleD angle) {
		final double cos = cos(angle.getRadians());
		final double sin = sin(angle.getRadians());
		
		return new Matrix44D(cos, -sin, 0.0D, 0.0D, sin, cos, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D);
	}
	
	/**
	 * Returns a {@code Matrix44D} instance that rotates along the Z-axis.
	 * <p>
	 * The layout looks like this:
	 * <pre>
	 * {@code
	 * cos, -sin, 0, 0
	 * sin,  cos, 0, 0
	 *   0,    0, 1, 0
	 *   0,    0, 0, 1
	 * }
	 * </pre>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix44D.rotateZ(angle, false);
	 * }
	 * </pre>
	 * 
	 * @param angle an angle in degrees
	 * @return a {@code Matrix44D} instance that rotates along the Z-axis
	 */
//	TODO: Add Unit Tests!
	public static Matrix44D rotateZ(final double angle) {
		return rotateZ(angle, false);
	}
	
	/**
	 * Returns a {@code Matrix44D} instance that rotates along the Z-axis.
	 * <p>
	 * The layout looks like this:
	 * <pre>
	 * {@code
	 * cos, -sin, 0, 0
	 * sin,  cos, 0, 0
	 *   0,    0, 1, 0
	 *   0,    0, 0, 1
	 * }
	 * </pre>
	 * 
	 * @param angle an angle in degrees or radians
	 * @param isRadians {@code true} if, and only if, {@code angle} is in radians, {@code false} otherwise
	 * @return a {@code Matrix44D} instance that rotates along the Z-axis
	 */
//	TODO: Add Unit Tests!
	public static Matrix44D rotateZ(final double angle, final boolean isRadians) {
		final double angleRadians = isRadians ? angle : toRadians(angle);
		
		final double cos = cos(angleRadians);
		final double sin = sin(angleRadians);
		
		return new Matrix44D(cos, -sin, 0.0D, 0.0D, sin, cos, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D);
	}
	
	/**
	 * Returns a {@code Matrix44D} instance that scales along the X-, Y- and Z-axes.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix44D.scale(vector.getX(), vector.getY(), vector.getZ());
	 * }
	 * </pre>
	 * 
	 * @param vector a {@link Vector3D} instance that contains the scale factors along the X-, Y- and Z-axes
	 * @return a {@code Matrix44D} instance that scales along the X-, Y- and Z-axes
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Matrix44D scale(final Vector3D vector) {
		return scale(vector.getX(), vector.getY(), vector.getZ());
	}
	
	/**
	 * Returns a {@code Matrix44D} instance that scales along the X-, Y- and Z-axes.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix44D.scale(scalar, scalar, scalar);
	 * }
	 * </pre>
	 * 
	 * @param scalar the scale factor along the X-, Y- and Z-axes
	 * @return a {@code Matrix44D} instance that scales along the X-, Y- and Z-axes
	 */
	public static Matrix44D scale(final double scalar) {
		return scale(scalar, scalar, scalar);
	}
	
	/**
	 * Returns a {@code Matrix44D} instance that scales along the X-, Y- and Z-axes.
	 * <p>
	 * The layout looks like this:
	 * <pre>
	 * {@code
	 * x, 0, 0, 0
	 * 0, y, 0, 0
	 * 0, 0, z, 0
	 * 0, 0, 0, 1
	 * }
	 * </pre>
	 * 
	 * @param x the scale factor along the X-axis
	 * @param y the scale factor along the Y-axis
	 * @param z the scale factor along the Z-axis
	 * @return a {@code Matrix44D} instance that scales along the X-, Y- and Z-axes
	 */
	public static Matrix44D scale(final double x, final double y, final double z) {
		return new Matrix44D(x, 0.0D, 0.0D, 0.0D, 0.0D, y, 0.0D, 0.0D, 0.0D, 0.0D, z, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D);
	}
	
	/**
	 * Returns a {@code Matrix44D} instance that translates along the X-, Y- and Z-axes.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix44D.translate(point.getX(), point.getY(), point.getZ());
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point3D} instance that contains the translation factors along the X-, Y- and Z-axes
	 * @return a {@code Matrix44D} instance that translates along the X-, Y- and Z-axes
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public static Matrix44D translate(final Point3D point) {
		return translate(point.getX(), point.getY(), point.getZ());
	}
	
	/**
	 * Returns a {@code Matrix44D} instance that translates along the X-, Y- and Z-axes.
	 * <p>
	 * The layout looks like this:
	 * <pre>
	 * {@code
	 * 1, 0, 0, x
	 * 0, 1, 0, y
	 * 0, 0, 1, z
	 * 0, 0, 0, 1
	 * }
	 * </pre>
	 * 
	 * @param x the translation factor along the X-axis
	 * @param y the translation factor along the Y-axis
	 * @param z the translation factor along the Z-axis
	 * @return a {@code Matrix44D} instance that translates along the X-, Y- and Z-axes
	 */
	public static Matrix44D translate(final double x, final double y, final double z) {
		return new Matrix44D(1.0D, 0.0D, 0.0D, x, 0.0D, 1.0D, 0.0D, y, 0.0D, 0.0D, 1.0D, z, 0.0D, 0.0D, 0.0D, 1.0D);
	}
	
	/**
	 * Returns a new {@code Matrix44D} instance that is the transpose of {@code matrix}.
	 * <p>
	 * If {@code matrix} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrix a {@code Matrix44D} instance
	 * @return a new {@code Matrix44D} instance that is the transpose of {@code matrix}
	 * @throws NullPointerException thrown if, and only if, {@code matrix} is {@code null}
	 */
	public static Matrix44D transpose(final Matrix44D matrix) {
		final double element11 = matrix.element11;
		final double element12 = matrix.element21;
		final double element13 = matrix.element31;
		final double element14 = matrix.element41;
		final double element21 = matrix.element12;
		final double element22 = matrix.element22;
		final double element23 = matrix.element32;
		final double element24 = matrix.element42;
		final double element31 = matrix.element13;
		final double element32 = matrix.element23;
		final double element33 = matrix.element33;
		final double element34 = matrix.element43;
		final double element41 = matrix.element14;
		final double element42 = matrix.element24;
		final double element43 = matrix.element34;
		final double element44 = matrix.element44;
		
		return new Matrix44D(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44);
	}
}