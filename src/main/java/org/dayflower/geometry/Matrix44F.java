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
package org.dayflower.geometry;

import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.cos;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.sin;

import java.util.Objects;

import org.dayflower.node.Node;

/**
 * A {@code Matrix44F} denotes a 4 x 4 matrix with 16 elements of type {@code float}.
 * <p>
 * The default order of this {@code Matrix44F} class is row-major.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Matrix44F implements Node {
	private final float element11;
	private final float element12;
	private final float element13;
	private final float element14;
	private final float element21;
	private final float element22;
	private final float element23;
	private final float element24;
	private final float element31;
	private final float element32;
	private final float element33;
	private final float element34;
	private final float element41;
	private final float element42;
	private final float element43;
	private final float element44;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Matrix44F} instance denoting the identity matrix.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Matrix44F(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
	 * }
	 * </pre>
	 */
	public Matrix44F() {
		this(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Matrix44F} instance given its element values.
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
	public Matrix44F(final float element11, final float element12, final float element13, final float element14, final float element21, final float element22, final float element23, final float element24, final float element31, final float element32, final float element33, final float element34, final float element41, final float element42, final float element43, final float element44) {
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
	 * Returns a {@code String} representation of this {@code Matrix44F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Matrix44F} instance
	 */
	@Override
	public String toString() {
		final String row1 = String.format("%+.10f, %+.10f, %+.10f, %+.10f", Float.valueOf(this.element11), Float.valueOf(this.element12), Float.valueOf(this.element13), Float.valueOf(this.element14));
		final String row2 = String.format("%+.10f, %+.10f, %+.10f, %+.10f", Float.valueOf(this.element21), Float.valueOf(this.element22), Float.valueOf(this.element23), Float.valueOf(this.element24));
		final String row3 = String.format("%+.10f, %+.10f, %+.10f, %+.10f", Float.valueOf(this.element31), Float.valueOf(this.element32), Float.valueOf(this.element33), Float.valueOf(this.element34));
		final String row4 = String.format("%+.10f, %+.10f, %+.10f, %+.10f", Float.valueOf(this.element41), Float.valueOf(this.element42), Float.valueOf(this.element43), Float.valueOf(this.element44));
		
		return String.format("new Matrix44F(%s, %s, %s, %s)", row1, row2, row3, row4);
	}
	
	/**
	 * Compares {@code object} to this {@code Matrix44F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Matrix44F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Matrix44F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Matrix44F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Matrix44F)) {
			return false;
		} else if(!equal(this.element11, Matrix44F.class.cast(object).element11)) {
			return false;
		} else if(!equal(this.element12, Matrix44F.class.cast(object).element12)) {
			return false;
		} else if(!equal(this.element13, Matrix44F.class.cast(object).element13)) {
			return false;
		} else if(!equal(this.element14, Matrix44F.class.cast(object).element14)) {
			return false;
		} else if(!equal(this.element21, Matrix44F.class.cast(object).element21)) {
			return false;
		} else if(!equal(this.element22, Matrix44F.class.cast(object).element22)) {
			return false;
		} else if(!equal(this.element23, Matrix44F.class.cast(object).element23)) {
			return false;
		} else if(!equal(this.element24, Matrix44F.class.cast(object).element24)) {
			return false;
		} else if(!equal(this.element31, Matrix44F.class.cast(object).element31)) {
			return false;
		} else if(!equal(this.element32, Matrix44F.class.cast(object).element32)) {
			return false;
		} else if(!equal(this.element33, Matrix44F.class.cast(object).element33)) {
			return false;
		} else if(!equal(this.element34, Matrix44F.class.cast(object).element34)) {
			return false;
		} else if(!equal(this.element41, Matrix44F.class.cast(object).element41)) {
			return false;
		} else if(!equal(this.element42, Matrix44F.class.cast(object).element42)) {
			return false;
		} else if(!equal(this.element43, Matrix44F.class.cast(object).element43)) {
			return false;
		} else if(!equal(this.element44, Matrix44F.class.cast(object).element44)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Matrix44F} instance is invertible, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Matrix44F} instance is invertible, {@code false} otherwise
	 */
	public boolean isInvertible() {
		return abs(determinant()) >= 1.0e-12F;
	}
	
	/**
	 * Returns the determinant of this {@code Matrix44F} instance.
	 * 
	 * @return the determinant of this {@code Matrix44F} instance
	 */
	public float determinant() {
		final float a = this.element11 * this.element22 - this.element12 * this.element21;
		final float b = this.element11 * this.element23 - this.element13 * this.element21;
		final float c = this.element11 * this.element24 - this.element14 * this.element21;
		final float d = this.element12 * this.element23 - this.element13 * this.element22;
		final float e = this.element12 * this.element24 - this.element14 * this.element22;
		final float f = this.element13 * this.element24 - this.element14 * this.element23;
		final float g = this.element31 * this.element42 - this.element32 * this.element41;
		final float h = this.element31 * this.element43 - this.element33 * this.element41;
		final float i = this.element31 * this.element44 - this.element34 * this.element41;
		final float j = this.element32 * this.element43 - this.element33 * this.element42;
		final float k = this.element32 * this.element44 - this.element34 * this.element42;
		final float l = this.element33 * this.element44 - this.element34 * this.element43;
		
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
	public float getElement(final int index) {
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
	public float getElement(final int row, final int column) {
		if(row < 1 || row > 4) {
			throw new IllegalArgumentException(String.format("Illegal row: row=%d", Integer.valueOf(row)));
		}
		
		if(column < 1 || column > 4) {
			throw new IllegalArgumentException(String.format("Illegal column: column=%d", Integer.valueOf(column)));
		}
		
		final int index = (row - 1) * 4 + (column - 1);
		
		return getElement(index);
	}
	
	/**
	 * Returns the value of the element at index 0 or row 1 and column 1.
	 * 
	 * @return the value of the element at index 0 or row 1 and column 1
	 */
	public float getElement11() {
		return this.element11;
	}
	
	/**
	 * Returns the value of the element at index 1 or row 1 and column 2.
	 * 
	 * @return the value of the element at index 1 or row 1 and column 2
	 */
	public float getElement12() {
		return this.element12;
	}
	
	/**
	 * Returns the value of the element at index 2 or row 1 and column 3.
	 * 
	 * @return the value of the element at index 2 or row 1 and column 3
	 */
	public float getElement13() {
		return this.element13;
	}
	
	/**
	 * Returns the value of the element at index 3 or row 1 and column 4.
	 * 
	 * @return the value of the element at index 3 or row 1 and column 4
	 */
	public float getElement14() {
		return this.element14;
	}
	
	/**
	 * Returns the value of the element at index 4 or row 2 and column 1.
	 * 
	 * @return the value of the element at index 4 or row 2 and column 1
	 */
	public float getElement21() {
		return this.element21;
	}
	
	/**
	 * Returns the value of the element at index 5 or row 2 and column 2.
	 * 
	 * @return the value of the element at index 5 or row 2 and column 2
	 */
	public float getElement22() {
		return this.element22;
	}
	
	/**
	 * Returns the value of the element at index 6 or row 2 and column 3.
	 * 
	 * @return the value of the element at index 6 or row 2 and column 3
	 */
	public float getElement23() {
		return this.element23;
	}
	
	/**
	 * Returns the value of the element at index 7 or row 2 and column 4.
	 * 
	 * @return the value of the element at index 7 or row 2 and column 4
	 */
	public float getElement24() {
		return this.element24;
	}
	
	/**
	 * Returns the value of the element at index 8 or row 3 and column 1.
	 * 
	 * @return the value of the element at index 8 or row 3 and column 1
	 */
	public float getElement31() {
		return this.element31;
	}
	
	/**
	 * Returns the value of the element at index 9 or row 3 and column 2.
	 * 
	 * @return the value of the element at index 9 or row 3 and column 2
	 */
	public float getElement32() {
		return this.element32;
	}
	
	/**
	 * Returns the value of the element at index 10 or row 3 and column 3.
	 * 
	 * @return the value of the element at index 10 or row 3 and column 3
	 */
	public float getElement33() {
		return this.element33;
	}
	
	/**
	 * Returns the value of the element at index 11 or row 3 and column 4.
	 * 
	 * @return the value of the element at index 11 or row 3 and column 4
	 */
	public float getElement34() {
		return this.element34;
	}
	
	/**
	 * Returns the value of the element at index 12 or row 4 and column 1.
	 * 
	 * @return the value of the element at index 12 or row 4 and column 1
	 */
	public float getElement41() {
		return this.element41;
	}
	
	/**
	 * Returns the value of the element at index 13 or row 4 and column 2.
	 * 
	 * @return the value of the element at index 13 or row 4 and column 2
	 */
	public float getElement42() {
		return this.element42;
	}
	
	/**
	 * Returns the value of the element at index 14 or row 4 and column 3.
	 * 
	 * @return the value of the element at index 14 or row 4 and column 3
	 */
	public float getElement43() {
		return this.element43;
	}
	
	/**
	 * Returns the value of the element at index 15 or row 4 and column 4.
	 * 
	 * @return the value of the element at index 15 or row 4 and column 4
	 */
	public float getElement44() {
		return this.element44;
	}
	
	/**
	 * Returns a hash code for this {@code Matrix44F} instance.
	 * 
	 * @return a hash code for this {@code Matrix44F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(new Object[] {
			Float.valueOf(this.element11), Float.valueOf(this.element12), Float.valueOf(this.element13), Float.valueOf(this.element14),
			Float.valueOf(this.element21), Float.valueOf(this.element22), Float.valueOf(this.element23), Float.valueOf(this.element24),
			Float.valueOf(this.element31), Float.valueOf(this.element32), Float.valueOf(this.element33), Float.valueOf(this.element34),
			Float.valueOf(this.element41), Float.valueOf(this.element42), Float.valueOf(this.element43), Float.valueOf(this.element44)
		});
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code Matrix44F} instance denoting the identity matrix.
	 * 
	 * @return a new {@code Matrix44F} instance denoting the identity matrix
	 */
	public static Matrix44F identity() {
		return new Matrix44F();
	}
	
	/**
	 * Returns a new {@code Matrix44F} instance that is the inverse of {@code matrix}.
	 * <p>
	 * If {@code matrix} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix} cannot be inverted, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * To make sure {@code matrix} is invertible, consider calling {@link #isInvertible()}.
	 * 
	 * @return a new {@code Matrix44F} instance that is the inverse of {@code matrix}
	 * @throws IllegalArgumentException thrown if, and only if, {@code matrix} cannot be inverted
	 * @throws NullPointerException thrown if, and only if, {@code matrix} is {@code null}
	 */
	public static Matrix44F inverse(final Matrix44F matrix) {
		final float a = matrix.element11 * matrix.element22 - matrix.element12 * matrix.element21;
		final float b = matrix.element11 * matrix.element23 - matrix.element13 * matrix.element21;
		final float c = matrix.element11 * matrix.element24 - matrix.element14 * matrix.element21;
		final float d = matrix.element12 * matrix.element23 - matrix.element13 * matrix.element22;
		final float e = matrix.element12 * matrix.element24 - matrix.element14 * matrix.element22;
		final float f = matrix.element13 * matrix.element24 - matrix.element14 * matrix.element23;
		final float g = matrix.element31 * matrix.element42 - matrix.element32 * matrix.element41;
		final float h = matrix.element31 * matrix.element43 - matrix.element33 * matrix.element41;
		final float i = matrix.element31 * matrix.element44 - matrix.element34 * matrix.element41;
		final float j = matrix.element32 * matrix.element43 - matrix.element33 * matrix.element42;
		final float k = matrix.element32 * matrix.element44 - matrix.element34 * matrix.element42;
		final float l = matrix.element33 * matrix.element44 - matrix.element34 * matrix.element43;
		
		final float determinant = a * l - b * k + c * j + d * i - e * h + f * g;
		final float determinantReciprocal = 1.0F / determinant;
		
		if(abs(determinant) < 1.0e-12F) {
			throw new IllegalArgumentException("The Matrix44F 'matrix' cannot be inverted!");
		}
		
		final float element11 = (+matrix.element22 * l - matrix.element23 * k + matrix.element24 * j) * determinantReciprocal;
		final float element12 = (-matrix.element12 * l + matrix.element13 * k - matrix.element14 * j) * determinantReciprocal;
		final float element13 = (+matrix.element42 * f - matrix.element43 * e + matrix.element44 * d) * determinantReciprocal;
		final float element14 = (-matrix.element32 * f + matrix.element33 * e - matrix.element34 * d) * determinantReciprocal;
		final float element21 = (-matrix.element21 * l + matrix.element23 * i - matrix.element24 * h) * determinantReciprocal;
		final float element22 = (+matrix.element11 * l - matrix.element13 * i + matrix.element14 * h) * determinantReciprocal;
		final float element23 = (-matrix.element41 * f + matrix.element43 * c - matrix.element44 * b) * determinantReciprocal;
		final float element24 = (+matrix.element31 * f - matrix.element33 * c + matrix.element34 * b) * determinantReciprocal;
		final float element31 = (+matrix.element21 * k - matrix.element22 * i + matrix.element24 * g) * determinantReciprocal;
		final float element32 = (-matrix.element11 * k + matrix.element12 * i - matrix.element14 * g) * determinantReciprocal;
		final float element33 = (+matrix.element41 * e - matrix.element42 * c + matrix.element44 * a) * determinantReciprocal;
		final float element34 = (-matrix.element31 * e + matrix.element32 * c - matrix.element34 * a) * determinantReciprocal;
		final float element41 = (-matrix.element21 * j + matrix.element22 * h - matrix.element23 * g) * determinantReciprocal;
		final float element42 = (+matrix.element11 * j - matrix.element12 * h + matrix.element13 * g) * determinantReciprocal;
		final float element43 = (-matrix.element41 * d + matrix.element42 * b - matrix.element43 * a) * determinantReciprocal;
		final float element44 = (+matrix.element31 * d - matrix.element32 * b + matrix.element33 * a) * determinantReciprocal;
		
		return new Matrix44F(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44);
	}
	
	/**
	 * Returns a new {@code Matrix44F} instance that looks in the direction of {@code eye} to {@code lookAt} and has an up-direction of {@code up}.
	 * <p>
	 * If either {@code eye}, {@code lookAt} or {@code up} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point3F} instance that represents the eye to look from
	 * @param lookAt a {@code Point3F} instance that represents the point to look at
	 * @param up a {@link Vector3F} instance that represents the up-direction
	 * @return a new {@code Matrix44F} instance that looks in the direction of {@code eye} to {@code lookAt} and has an up-direction of {@code up}
	 * @throws NullPointerException thrown if, and only if, either {@code eye}, {@code lookAt} or {@code up} are {@code null}
	 */
	public static Matrix44F lookAt(final Point3F eye, final Point3F lookAt, final Vector3F up) {
		final Vector3F w = Vector3F.directionNormalized(eye, lookAt);
		final Vector3F u = Vector3F.normalize(Vector3F.crossProduct(Vector3F.normalize(up), w));
		final Vector3F v = Vector3F.crossProduct(w, u);
		
		final float element11 = u.getX();
		final float element12 = v.getX();
		final float element13 = w.getX();
		final float element14 = eye.getX();
		final float element21 = u.getY();
		final float element22 = v.getY();
		final float element23 = w.getY();
		final float element24 = eye.getY();
		final float element31 = u.getZ();
		final float element32 = v.getZ();
		final float element33 = w.getZ();
		final float element34 = eye.getZ();
		final float element41 = 0.0F;
		final float element42 = 0.0F;
		final float element43 = 0.0F;
		final float element44 = 1.0F;
		
		return new Matrix44F(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44);
	}
	
	/**
	 * Multiplies the element values of {@code matrixLHS} with the element values of {@code matrixRHS}.
	 * <p>
	 * Returns a new {@code Matrix44F} instance with the result of the multiplication.
	 * <p>
	 * If either {@code matrixLHS} or {@code matrixRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS the {@code Matrix44F} instance on the left-hand side
	 * @param matrixRHS the {@code Matrix44F} instance on the right-hand side
	 * @return a new {@code Matrix44F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code matrixRHS} are {@code null}
	 */
	public static Matrix44F multiply(final Matrix44F matrixLHS, final Matrix44F matrixRHS) {
		final float element11 = matrixLHS.element11 * matrixRHS.element11 + matrixLHS.element12 * matrixRHS.element21 + matrixLHS.element13 * matrixRHS.element31 + matrixLHS.element14 * matrixRHS.element41;
		final float element12 = matrixLHS.element11 * matrixRHS.element12 + matrixLHS.element12 * matrixRHS.element22 + matrixLHS.element13 * matrixRHS.element32 + matrixLHS.element14 * matrixRHS.element42;
		final float element13 = matrixLHS.element11 * matrixRHS.element13 + matrixLHS.element12 * matrixRHS.element23 + matrixLHS.element13 * matrixRHS.element33 + matrixLHS.element14 * matrixRHS.element43;
		final float element14 = matrixLHS.element11 * matrixRHS.element14 + matrixLHS.element12 * matrixRHS.element24 + matrixLHS.element13 * matrixRHS.element34 + matrixLHS.element14 * matrixRHS.element44;
		final float element21 = matrixLHS.element21 * matrixRHS.element11 + matrixLHS.element22 * matrixRHS.element21 + matrixLHS.element23 * matrixRHS.element31 + matrixLHS.element24 * matrixRHS.element41;
		final float element22 = matrixLHS.element21 * matrixRHS.element12 + matrixLHS.element22 * matrixRHS.element22 + matrixLHS.element23 * matrixRHS.element32 + matrixLHS.element24 * matrixRHS.element42;
		final float element23 = matrixLHS.element21 * matrixRHS.element13 + matrixLHS.element22 * matrixRHS.element23 + matrixLHS.element23 * matrixRHS.element33 + matrixLHS.element24 * matrixRHS.element43;
		final float element24 = matrixLHS.element21 * matrixRHS.element14 + matrixLHS.element22 * matrixRHS.element24 + matrixLHS.element23 * matrixRHS.element34 + matrixLHS.element24 * matrixRHS.element44;
		final float element31 = matrixLHS.element31 * matrixRHS.element11 + matrixLHS.element32 * matrixRHS.element21 + matrixLHS.element33 * matrixRHS.element31 + matrixLHS.element34 * matrixRHS.element41;
		final float element32 = matrixLHS.element31 * matrixRHS.element12 + matrixLHS.element32 * matrixRHS.element22 + matrixLHS.element33 * matrixRHS.element32 + matrixLHS.element34 * matrixRHS.element42;
		final float element33 = matrixLHS.element31 * matrixRHS.element13 + matrixLHS.element32 * matrixRHS.element23 + matrixLHS.element33 * matrixRHS.element33 + matrixLHS.element34 * matrixRHS.element43;
		final float element34 = matrixLHS.element31 * matrixRHS.element14 + matrixLHS.element32 * matrixRHS.element24 + matrixLHS.element33 * matrixRHS.element34 + matrixLHS.element34 * matrixRHS.element44;
		final float element41 = matrixLHS.element41 * matrixRHS.element11 + matrixLHS.element42 * matrixRHS.element21 + matrixLHS.element43 * matrixRHS.element31 + matrixLHS.element44 * matrixRHS.element41;
		final float element42 = matrixLHS.element41 * matrixRHS.element12 + matrixLHS.element42 * matrixRHS.element22 + matrixLHS.element43 * matrixRHS.element32 + matrixLHS.element44 * matrixRHS.element42;
		final float element43 = matrixLHS.element41 * matrixRHS.element13 + matrixLHS.element42 * matrixRHS.element23 + matrixLHS.element43 * matrixRHS.element33 + matrixLHS.element44 * matrixRHS.element43;
		final float element44 = matrixLHS.element41 * matrixRHS.element14 + matrixLHS.element42 * matrixRHS.element24 + matrixLHS.element43 * matrixRHS.element34 + matrixLHS.element44 * matrixRHS.element44;
		
		return new Matrix44F(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44);
	}
	
	/**
	 * Returns a {@code Matrix44F} instance that rotates along the axis represented by {@code vector}.
	 * <p>
	 * If either {@code angle} or {@code vector} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param angle an {@link AngleF} instance
	 * @param vector a {@link Vector3F} instance that represents an axis
	 * @return a {@code Matrix44F} instance that rotates along the axis represented by {@code vector}
	 * @throws NullPointerException thrown if, and only if, either {@code angle} or {@code vector} are {@code null}
	 */
	public static Matrix44F rotate(final AngleF angle, final Vector3F vector) {
		final Vector3F vectorNormalized = Vector3F.normalize(vector);
		
		final float cos = cos(angle.getRadians());
		final float sin = sin(angle.getRadians());
		final float oneMinusCos = 1.0F - cos;
		
		final float element11 = oneMinusCos * vectorNormalized.getX() * vectorNormalized.getX() + cos;
		final float element12 = oneMinusCos * vectorNormalized.getX() * vectorNormalized.getY() - sin * vectorNormalized.getZ();
		final float element13 = oneMinusCos * vectorNormalized.getX() * vectorNormalized.getZ() + sin * vectorNormalized.getY();
		final float element14 = 0.0F;
		final float element21 = oneMinusCos * vectorNormalized.getX() * vectorNormalized.getY() + sin * vectorNormalized.getZ();
		final float element22 = oneMinusCos * vectorNormalized.getY() * vectorNormalized.getY() + cos;
		final float element23 = oneMinusCos * vectorNormalized.getY() * vectorNormalized.getZ() - sin * vectorNormalized.getX();
		final float element24 = 0.0F;
		final float element31 = oneMinusCos * vectorNormalized.getX() * vectorNormalized.getZ() - sin * vectorNormalized.getY();
		final float element32 = oneMinusCos * vectorNormalized.getY() * vectorNormalized.getZ() + sin * vectorNormalized.getX();
		final float element33 = oneMinusCos * vectorNormalized.getZ() * vectorNormalized.getZ() + cos;
		final float element34 = 0.0F;
		final float element41 = 0.0F;
		final float element42 = 0.0F;
		final float element43 = 0.0F;
		final float element44 = 1.0F;
		
		return new Matrix44F(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44);
	}
	
	/**
	 * Returns a {@code Matrix44F} instance that rotates along the axis represented by {@code x}, {@code y} and {@code z}.
	 * <p>
	 * If {@code angle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix44F.rotate(angle, new Vector3F(x, y, z));
	 * }
	 * </pre>
	 * 
	 * @param angle an {@link AngleF} instance
	 * @param x the component value along the X-axis
	 * @param y the component value along the Y-axis
	 * @param z the component value along the Z-axis
	 * @return a {@code Matrix44F} instance that rotates along the axis represented by {@code x}, {@code y} and {@code z}
	 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
	 */
	public static Matrix44F rotate(final AngleF angle, final float x, final float y, final float z) {
		return rotate(angle, new Vector3F(x, y, z));
	}
	
	/**
	 * Returns a {@code Matrix44F} instance that rotates using {@code orthonormalBasis}.
	 * <p>
	 * If {@code orthonormalBasis} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param orthonormalBasis an {@link OrthonormalBasis33F} instance
	 * @return a {@code Matrix44F} instance that rotates using {@code orthonormalBasis}
	 * @throws NullPointerException thrown if, and only if, {@code orthonormalBasis} is {@code null}
	 */
	public static Matrix44F rotate(final OrthonormalBasis33F orthonormalBasis) {
		final Vector3F u = Vector3F.transform(Vector3F.u(), orthonormalBasis);
		final Vector3F v = Vector3F.transform(Vector3F.v(), orthonormalBasis);
		final Vector3F w = Vector3F.transform(Vector3F.w(), orthonormalBasis);
		
		return rotate(w, v, u);
	}
	
	/**
	 * Returns a {@code Matrix44F} instance that rotates using {@code quaternion}.
	 * <p>
	 * If {@code quaternion} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param quaternion a {@link Quaternion4F} instance
	 * @return a {@code Matrix44F} instance that rotates using {@code quaternion}
	 * @throws NullPointerException thrown if, and only if, {@code quaternion} is {@code null}
	 */
	public static Matrix44F rotate(final Quaternion4F quaternion) {
		final float uX = 1.0F - 2.0F * (quaternion.getY() * quaternion.getY() + quaternion.getZ() * quaternion.getZ());
		final float uY = 0.0F + 2.0F * (quaternion.getX() * quaternion.getY() - quaternion.getW() * quaternion.getZ());
		final float uZ = 0.0F + 2.0F * (quaternion.getX() * quaternion.getZ() + quaternion.getW() * quaternion.getY());
		final float vX = 0.0F + 2.0F * (quaternion.getX() * quaternion.getY() + quaternion.getW() * quaternion.getZ());
		final float vY = 1.0F - 2.0F * (quaternion.getX() * quaternion.getX() + quaternion.getZ() * quaternion.getZ());
		final float vZ = 0.0F + 2.0F * (quaternion.getY() * quaternion.getZ() - quaternion.getW() * quaternion.getX());
		final float wX = 0.0F + 2.0F * (quaternion.getX() * quaternion.getZ() - quaternion.getW() * quaternion.getY());
		final float wY = 0.0F + 2.0F * (quaternion.getY() * quaternion.getZ() + quaternion.getW() * quaternion.getX());
		final float wZ = 1.0F - 2.0F * (quaternion.getX() * quaternion.getX() + quaternion.getY() * quaternion.getY());
		
		final Vector3F u = new Vector3F(uX, uY, uZ);
		final Vector3F v = new Vector3F(vX, vY, vZ);
		final Vector3F w = new Vector3F(wX, wY, wZ);
		
		return rotate(w, v, u);
	}
	
	/**
	 * Returns a {@code Matrix44F} instance that rotates using {@code w} and {@code v}.
	 * <p>
	 * If either {@code w} or {@code v} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param w a {@link Vector3F} instance
	 * @param v a {@code Vector3F} instance
	 * @return a {@code Matrix44F} instance that rotates using {@code w} and {@code v}
	 * @throws NullPointerException thrown if, and only if, either {@code w} or {@code v} are {@code null}
	 */
	public static Matrix44F rotate(final Vector3F w, final Vector3F v) {
		final Vector3F wNormalized = Vector3F.normalize(w);
		final Vector3F uNormalized = Vector3F.crossProduct(Vector3F.normalize(v), wNormalized);
		final Vector3F vNormalized = Vector3F.crossProduct(wNormalized, uNormalized);
		
		return rotate(wNormalized, vNormalized, uNormalized);
	}
	
	/**
	 * Returns a {@code Matrix44F} instance that rotates using {@code w}, {@code v} and {@code u}.
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
	 * @param w a {@link Vector3F} instance
	 * @param v a {@code Vector3F} instance
	 * @param u a {@code Vector3F} instance
	 * @return a {@code Matrix44F} instance that rotates using {@code w}, {@code v} and {@code u}
	 * @throws NullPointerException thrown if, and only if, either {@code w}, {@code v} or {@code u} are {@code null}
	 */
	public static Matrix44F rotate(final Vector3F w, final Vector3F v, final Vector3F u) {
		final float element11 = u.getX();
		final float element12 = v.getX();
		final float element13 = w.getX();
		final float element14 = 0.0F;
		final float element21 = u.getY();
		final float element22 = v.getY();
		final float element23 = w.getY();
		final float element24 = 0.0F;
		final float element31 = u.getZ();
		final float element32 = v.getZ();
		final float element33 = w.getZ();
		final float element34 = 0.0F;
		final float element41 = 0.0F;
		final float element42 = 0.0F;
		final float element43 = 0.0F;
		final float element44 = 1.0F;
		
		return new Matrix44F(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44);
	}
	
	/**
	 * Returns a {@code Matrix44F} instance that rotates along the X-axis.
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
	 * @param angle an {@link AngleF} instance
	 * @return a {@code Matrix44F} instance that rotates along the X-axis
	 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
	 */
	public static Matrix44F rotateX(final AngleF angle) {
		final float cos = cos(angle.getRadians());
		final float sin = sin(angle.getRadians());
		
		return new Matrix44F(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, cos, -sin, 0.0F, 0.0F, sin, cos, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
	}
	
	/**
	 * Returns a {@code Matrix44F} instance that rotates along the Y-axis.
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
	 * @param angle an {@link AngleF} instance
	 * @return a {@code Matrix44F} instance that rotates along the Y-axis
	 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
	 */
	public static Matrix44F rotateY(final AngleF angle) {
		final float cos = cos(angle.getRadians());
		final float sin = sin(angle.getRadians());
		
		return new Matrix44F(cos, 0.0F, sin, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, -sin, 0.0F, cos, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
	}
	
	/**
	 * Returns a {@code Matrix44F} instance that rotates along the Z-axis.
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
	 * @param angle an {@link AngleF} instance
	 * @return a {@code Matrix44F} instance that rotates along the Z-axis
	 * @throws NullPointerException thrown if, and only if, {@code angle} is {@code null}
	 */
	public static Matrix44F rotateZ(final AngleF angle) {
		final float cos = cos(angle.getRadians());
		final float sin = sin(angle.getRadians());
		
		return new Matrix44F(cos, -sin, 0.0F, 0.0F, sin, cos, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
	}
	
	/**
	 * Returns a {@code Matrix44F} instance that scales along the X-, Y- and Z-axes.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix44F.scale(vector.getX(), vector.getY(), vector.getZ());
	 * }
	 * </pre>
	 * 
	 * @param vector a {@link Vector3F} instance that contains the scale factors along the X-, Y- and Z-axes
	 * @return a {@code Matrix44F} instance that scales along the X-, Y- and Z-axes
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Matrix44F scale(final Vector3F vector) {
		return scale(vector.getX(), vector.getY(), vector.getZ());
	}
	
	/**
	 * Returns a {@code Matrix44F} instance that scales along the X-, Y- and Z-axes.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix44F.scale(scalar, scalar, scalar);
	 * }
	 * </pre>
	 * 
	 * @param scalar the scale factor along the X-, Y- and Z-axes
	 * @return a {@code Matrix44F} instance that scales along the X-, Y- and Z-axes
	 */
	public static Matrix44F scale(final float scalar) {
		return scale(scalar, scalar, scalar);
	}
	
	/**
	 * Returns a {@code Matrix44F} instance that scales along the X-, Y- and Z-axes.
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
	 * @return a {@code Matrix44F} instance that scales along the X-, Y- and Z-axes
	 */
	public static Matrix44F scale(final float x, final float y, final float z) {
		return new Matrix44F(x, 0.0F, 0.0F, 0.0F, 0.0F, y, 0.0F, 0.0F, 0.0F, 0.0F, z, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);
	}
	
	/**
	 * Returns a {@code Matrix44F} instance that translates along the X-, Y- and Z-axes.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Matrix44F.translate(point.getX(), point.getY(), point.getZ());
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point3F} instance that contains the translation factors along the X-, Y- and Z-axes
	 * @return a {@code Matrix44F} instance that translates along the X-, Y- and Z-axes
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public static Matrix44F translate(final Point3F point) {
		return translate(point.getX(), point.getY(), point.getZ());
	}
	
	/**
	 * Returns a {@code Matrix44F} instance that translates along the X-, Y- and Z-axes.
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
	 * @return a {@code Matrix44F} instance that translates along the X-, Y- and Z-axes
	 */
	public static Matrix44F translate(final float x, final float y, final float z) {
		return new Matrix44F(1.0F, 0.0F, 0.0F, x, 0.0F, 1.0F, 0.0F, y, 0.0F, 0.0F, 1.0F, z, 0.0F, 0.0F, 0.0F, 1.0F);
	}
	
	/**
	 * Returns a new {@code Matrix44F} instance that is the transpose of {@code matrix}.
	 * <p>
	 * If {@code matrix} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @return a new {@code Matrix44F} instance that is the transpose of {@code matrix}
	 * @throws NullPointerException thrown if, and only if, {@code matrix} is {@code null}
	 */
	public static Matrix44F transpose(final Matrix44F matrix) {
		final float element11 = matrix.element11;
		final float element12 = matrix.element21;
		final float element13 = matrix.element31;
		final float element14 = matrix.element41;
		final float element21 = matrix.element12;
		final float element22 = matrix.element22;
		final float element23 = matrix.element32;
		final float element24 = matrix.element42;
		final float element31 = matrix.element13;
		final float element32 = matrix.element23;
		final float element33 = matrix.element33;
		final float element34 = matrix.element43;
		final float element41 = matrix.element14;
		final float element42 = matrix.element24;
		final float element43 = matrix.element34;
		final float element44 = matrix.element44;
		
		return new Matrix44F(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44);
	}
}