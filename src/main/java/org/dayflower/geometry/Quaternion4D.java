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

import static org.dayflower.utility.Doubles.NEXT_DOWN_1_3;
import static org.dayflower.utility.Doubles.NEXT_UP_1_1;
import static org.dayflower.utility.Doubles.abs;
import static org.dayflower.utility.Doubles.atan2;
import static org.dayflower.utility.Doubles.cos;
import static org.dayflower.utility.Doubles.equal;
import static org.dayflower.utility.Doubles.sin;
import static org.dayflower.utility.Doubles.sqrt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;

import org.dayflower.java.lang.Strings;
import org.dayflower.node.Node;

/**
 * A {@code Quaternion4D} represents a quaternion with four {@code double}-based components.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Quaternion4D implements Node {
	private final double component1;
	private final double component2;
	private final double component3;
	private final double component4;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Quaternion4D} instance given the component values {@code 0.0D}, {@code 0.0D}, {@code 0.0D} and {@code 1.0D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Quaternion4D(0.0D, 0.0D, 0.0D);
	 * }
	 * </pre>
	 */
	public Quaternion4D() {
		this(0.0D, 0.0D, 0.0D);
	}
	
	/**
	 * Constructs a new {@code Quaternion4D} instance given the component values {@code vector.getComponent1()}, {@code vector.getComponent2()}, {@code vector.getComponent3()} and {@code 1.0D}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Quaternion4D(vector.getComponent1(), vector.getComponent2(), vector.getComponent3());
	 * }
	 * </pre>
	 * 
	 * @param vector a {@link Vector3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public Quaternion4D(final Vector3D vector) {
		this(vector.getComponent1(), vector.getComponent2(), vector.getComponent3());
	}
	
	/**
	 * Constructs a new {@code Quaternion4D} instance given the component values {@code component1}, {@code component2}, {@code component3} and {@code 1.0D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Quaternion4D(component1, component2, component3, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	public Quaternion4D(final double component1, final double component2, final double component3) {
		this(component1, component2, component3, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Quaternion4D} instance given the component values {@code component1}, {@code component2}, {@code component3} and {@code component4}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 * @param component4 the value of component 4
	 */
	public Quaternion4D(final double component1, final double component2, final double component3, final double component4) {
		this.component1 = component1;
		this.component2 = component2;
		this.component3 = component3;
		this.component4 = component4;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Quaternion4D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Quaternion4D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Quaternion4D(%s, %s, %s, %s)", Strings.toNonScientificNotationJava(this.component1), Strings.toNonScientificNotationJava(this.component2), Strings.toNonScientificNotationJava(this.component3), Strings.toNonScientificNotationJava(this.component4));
	}
	
	/**
	 * Compares {@code object} to this {@code Quaternion4D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Quaternion4D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Quaternion4D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Quaternion4D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Quaternion4D)) {
			return false;
		} else if(!equal(this.component1, Quaternion4D.class.cast(object).component1)) {
			return false;
		} else if(!equal(this.component2, Quaternion4D.class.cast(object).component2)) {
			return false;
		} else if(!equal(this.component3, Quaternion4D.class.cast(object).component3)) {
			return false;
		} else if(!equal(this.component4, Quaternion4D.class.cast(object).component4)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the value of component 1.
	 * 
	 * @return the value of component 1
	 */
	public double getComponent1() {
		return this.component1;
	}
	
	/**
	 * Returns the value of component 2.
	 * 
	 * @return the value of component 2
	 */
	public double getComponent2() {
		return this.component2;
	}
	
	/**
	 * Returns the value of component 3.
	 * 
	 * @return the value of component 3
	 */
	public double getComponent3() {
		return this.component3;
	}
	
	/**
	 * Returns the value of component 4.
	 * 
	 * @return the value of component 4
	 */
	public double getComponent4() {
		return this.component4;
	}
	
	/**
	 * Returns the value of the W-component.
	 * 
	 * @return the value of the W-component
	 */
	public double getW() {
		return this.component4;
	}
	
	/**
	 * Returns the value of the X-component.
	 * 
	 * @return the value of the X-component
	 */
	public double getX() {
		return this.component1;
	}
	
	/**
	 * Returns the value of the Y-component.
	 * 
	 * @return the value of the Y-component
	 */
	public double getY() {
		return this.component2;
	}
	
	/**
	 * Returns the value of the Z-component.
	 * 
	 * @return the value of the Z-component
	 */
	public double getZ() {
		return this.component3;
	}
	
	/**
	 * Returns the length of this {@code Quaternion4D} instance.
	 * 
	 * @return the length of this {@code Quaternion4D} instance
	 */
	public double length() {
		return sqrt(lengthSquared());
	}
	
	/**
	 * Returns the squared length of this {@code Quaternion4D} instance.
	 * 
	 * @return the squared length of this {@code Quaternion4D} instance
	 */
	public double lengthSquared() {
		return this.component1 * this.component1 + this.component2 * this.component2 + this.component3 * this.component3 + this.component4 * this.component4;
	}
	
	/**
	 * Returns a hash code for this {@code Quaternion4D} instance.
	 * 
	 * @return a hash code for this {@code Quaternion4D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(this.component1), Double.valueOf(this.component2), Double.valueOf(this.component3), Double.valueOf(this.component4));
	}
	
	/**
	 * Writes this {@code Quaternion4D} instance to {@code dataOutput}.
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
			dataOutput.writeDouble(this.component1);
			dataOutput.writeDouble(this.component2);
			dataOutput.writeDouble(this.component3);
			dataOutput.writeDouble(this.component4);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds the component values of {@code quaternionRHS} to the component values of {@code quaternionLHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the addition.
	 * <p>
	 * If either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion addition is performed componentwise.
	 * 
	 * @param quaternionLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param quaternionRHS the {@code Quaternion4D} instance on the right-hand side
	 * @return a new {@code Quaternion4D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}
	 */
	public static Quaternion4D add(final Quaternion4D quaternionLHS, final Quaternion4D quaternionRHS) {
		final double component1 = quaternionLHS.component1 + quaternionRHS.component1;
		final double component2 = quaternionLHS.component2 + quaternionRHS.component2;
		final double component3 = quaternionLHS.component3 + quaternionRHS.component3;
		final double component4 = quaternionLHS.component4 + quaternionRHS.component4;
		
		return new Quaternion4D(component1, component2, component3, component4);
	}
	
	/**
	 * Conjugates the component values of {@code quaternion}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the conjugation.
	 * <p>
	 * If {@code quaternion} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param quaternion a {@code Quaternion4D} instance
	 * @return a new {@code Quaternion4D} instance with the result of the conjugation
	 * @throws NullPointerException thrown if, and only if, {@code quaternion} is {@code null}
	 */
	public static Quaternion4D conjugate(final Quaternion4D quaternion) {
		final double component1 = -quaternion.component1;
		final double component2 = -quaternion.component2;
		final double component3 = -quaternion.component3;
		final double component4 = +quaternion.component4;
		
		return new Quaternion4D(component1, component2, component3, component4);
	}
	
	/**
	 * Divides the component values of {@code quaternionLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the division.
	 * <p>
	 * If {@code quaternionLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion division is performed componentwise.
	 * 
	 * @param quaternionLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Quaternion4D} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, {@code quaternionLHS} is {@code null}
	 */
	public static Quaternion4D divide(final Quaternion4D quaternionLHS, final double scalarRHS) {
		final double component1 = quaternionLHS.component1 / scalarRHS;
		final double component2 = quaternionLHS.component2 / scalarRHS;
		final double component3 = quaternionLHS.component3 / scalarRHS;
		final double component4 = quaternionLHS.component4 / scalarRHS;
		
		return new Quaternion4D(component1, component2, component3, component4);
	}
	
	/**
	 * Returns a new {@code Quaternion4D} instance based on an {@link AngleD} instance and a {@link Vector3D} instance.
	 * <p>
	 * If either {@code angle} or {@code vector} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param angle an {@code AngleD} instance
	 * @param vector a {@code Vector3D} instance
	 * @return a new {@code Quaternion4D} instance based on an {@code AngleD} instance and a {@code Vector3D} instance
	 * @throws NullPointerException thrown if, and only if, either {@code angle} or {@code vector} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4D from(final AngleD angle, final Vector3D vector) {
		final AngleD angleHalf = AngleD.half(angle);
		
		final double sin = sin(angleHalf.getRadians());
		final double cos = cos(angleHalf.getRadians());
		
		final double component1 = vector.getComponent1() * sin;
		final double component2 = vector.getComponent2() * sin;
		final double component3 = vector.getComponent3() * sin;
		final double component4 = cos;
		
		return new Quaternion4D(component1, component2, component3, component4);
	}
	
	/**
	 * Returns a new {@code Quaternion4D} instance based on a {@link Matrix44D} instance.
	 * <p>
	 * If {@code matrix} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrix a {@code Matrix44D} instance
	 * @return a new {@code Quaternion4D} instance based on a {@code Matrix44D} instance
	 * @throws NullPointerException thrown if, and only if, {@code matrix} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4D from(final Matrix44D matrix) {
		final double element11 = matrix.getElement11();
		final double element12 = matrix.getElement12();
		final double element13 = matrix.getElement13();
		final double element21 = matrix.getElement21();
		final double element22 = matrix.getElement22();
		final double element23 = matrix.getElement23();
		final double element31 = matrix.getElement31();
		final double element32 = matrix.getElement32();
		final double element33 = matrix.getElement33();
		
		if(element11 + element22 + element33 > 0.0D) {
			final double scalar = 0.5D / sqrt(element11 + element22 + element33 + 1.0D);
			
			final double component1 = (element23 - element32) * scalar;
			final double component2 = (element31 - element13) * scalar;
			final double component3 = (element12 - element21) * scalar;
			final double component4 = 0.25D / scalar;
			
			return normalize(new Quaternion4D(component1, component2, component3, component4));
		} else if(element11 > element22 && element11 > element33) {
			final double scalar = 2.0D * sqrt(1.0D + element11 - element22 - element23);
			final double scalarReciprocal = 1.0D / scalar;
			
			final double component1 = 0.25D * scalar;
			final double component2 = (element21 + element12) * scalarReciprocal;
			final double component3 = (element31 + element13) * scalarReciprocal;
			final double component4 = (element23 - element32) * scalarReciprocal;
			
			return normalize(new Quaternion4D(component1, component2, component3, component4));
		} else if(element22 > element33) {
			final double scalar = 2.0D * sqrt(1.0D + element22 - element11 - element33);
			final double scalarReciprocal = 1.0D / scalar;
			
			final double component1 = (element21 + element12) * scalarReciprocal;
			final double component2 = 0.25D * scalar;
			final double component3 = (element32 + element23) * scalarReciprocal;
			final double component4 = (element31 - element13) * scalarReciprocal;
			
			return normalize(new Quaternion4D(component1, component2, component3, component4));
		} else {
			final double scalar = 2.0F * sqrt(1.0D + element33 - element11 - element22);
			final double scalarReciprocal = 1.0D / scalar;
			
			final double component1 = (element31 + element13) * scalarReciprocal;
			final double component2 = (element23 + element32) * scalarReciprocal;
			final double component3 = 0.25D * scalar;
			final double component4 = (element12 - element21) * scalarReciprocal;
			
			return normalize(new Quaternion4D(component1, component2, component3, component4));
		}
	}
	
	/**
	 * Returns a new {@code Quaternion4D} instance based on the direction {@code direction}.
	 * <p>
	 * If {@code direction} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Quaternion4D.from(Matrix44D.rotate(new OrthonormalBasis33D(direction)));
	 * }
	 * </pre>
	 * 
	 * @param direction a {@link Vector3D} instance
	 * @return a new {@code Quaternion4D} instance based on the direction {@code direction}
	 * @throws NullPointerException thrown if, and only if, {@code direction} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4D from(final Vector3D direction) {
		return from(Matrix44D.rotate(new OrthonormalBasis33D(direction)));
	}
	
	/**
	 * Performs a normalized linear interpolation between {@code quaternionLHS} and {@code quaternionRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the operation.
	 * <p>
	 * If either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Quaternion4D.linearInterpolationNormalized(quaternionLHS, quaternionRHS, 0.5D);
	 * }
	 * </pre>
	 * 
	 * @param quaternionLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param quaternionRHS the {@code Quaternion4D} instance on the right-hand side
	 * @return a new {@code Quaternion4D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4D linearInterpolationNormalized(final Quaternion4D quaternionLHS, final Quaternion4D quaternionRHS) {
		return linearInterpolationNormalized(quaternionLHS, quaternionRHS, 0.5D);
	}
	
	/**
	 * Performs a normalized linear interpolation between {@code quaternionLHS} and {@code quaternionRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the operation.
	 * <p>
	 * If either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Quaternion4D.linearInterpolationNormalized(quaternionLHS, quaternionRHS, t, false);
	 * }
	 * </pre>
	 * 
	 * @param quaternionLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param quaternionRHS the {@code Quaternion4D} instance on the right-hand side
	 * @param t the factor
	 * @return a new {@code Quaternion4D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4D linearInterpolationNormalized(final Quaternion4D quaternionLHS, final Quaternion4D quaternionRHS, final double t) {
		return linearInterpolationNormalized(quaternionLHS, quaternionRHS, t, false);
	}
	
	/**
	 * Performs a normalized linear interpolation between {@code quaternionLHS} and {@code quaternionRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the operation.
	 * <p>
	 * If either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param quaternionLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param quaternionRHS the {@code Quaternion4D} instance on the right-hand side
	 * @param t the factor
	 * @param isInterpolatingShortest {@code true} if, and only if, the shortest interpolation should be used, {@code false} otherwise
	 * @return a new {@code Quaternion4D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4D linearInterpolationNormalized(final Quaternion4D quaternionLHS, final Quaternion4D quaternionRHS, final double t, final boolean isInterpolatingShortest) {
		return normalize(add(multiply(subtract(isInterpolatingShortest && dotProduct(quaternionLHS, quaternionRHS) < 0.0D ? negate(quaternionRHS) : quaternionRHS, quaternionLHS), t), quaternionLHS));
	}
	
	/**
	 * Performs a spherical linear interpolation between {@code quaternionLHS} and {@code quaternionRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the operation.
	 * <p>
	 * If either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Quaternion4D.linearInterpolationSpherical(quaternionLHS, quaternionRHS, 0.5D);
	 * }
	 * </pre>
	 * 
	 * @param quaternionLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param quaternionRHS the {@code Quaternion4D} instance on the right-hand side
	 * @return a new {@code Quaternion4D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4D linearInterpolationSpherical(final Quaternion4D quaternionLHS, final Quaternion4D quaternionRHS) {
		return linearInterpolationSpherical(quaternionLHS, quaternionRHS, 0.5D);
	}
	
	/**
	 * Performs a spherical linear interpolation between {@code quaternionLHS} and {@code quaternionRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the operation.
	 * <p>
	 * If either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Quaternion4D.linearInterpolationSpherical(quaternionLHS, quaternionRHS, t, false);
	 * }
	 * </pre>
	 * 
	 * @param quaternionLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param quaternionRHS the {@code Quaternion4D} instance on the right-hand side
	 * @param t the factor
	 * @return a new {@code Quaternion4D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4D linearInterpolationSpherical(final Quaternion4D quaternionLHS, final Quaternion4D quaternionRHS, final double t) {
		return linearInterpolationSpherical(quaternionLHS, quaternionRHS, t, false);
	}
	
	/**
	 * Performs a spherical linear interpolation between {@code quaternionLHS} and {@code quaternionRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the operation.
	 * <p>
	 * If either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param quaternionLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param quaternionRHS the {@code Quaternion4D} instance on the right-hand side
	 * @param t the factor
	 * @param isInterpolatingShortest {@code true} if, and only if, the shortest interpolation should be used, {@code false} otherwise
	 * @return a new {@code Quaternion4D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4D linearInterpolationSpherical(final Quaternion4D quaternionLHS, final Quaternion4D quaternionRHS, final double t, final boolean isInterpolatingShortest) {
		final double cos = dotProduct(quaternionLHS, quaternionRHS);
		
		final double x = isInterpolatingShortest && cos < 0.0D ? -cos : cos;
		final double y = sqrt(1.0D - x * x);
		
		final Quaternion4D quaternion1 = isInterpolatingShortest && cos < 0.0D ? negate(quaternionRHS) : quaternionRHS;
		
		if(abs(x) >= 1.0D - 1000.0D) {
			return linearInterpolationNormalized(quaternionLHS, quaternion1, t);
		}
		
		final double theta = atan2(y, x);
		
		return add(multiply(quaternionLHS, sin((1.0D - t) * theta) / y), multiply(quaternion1, sin(t * theta) / y));
	}
	
	/**
	 * Multiplies the component values of {@code quaternionLHS} with the component values of {@code quaternionRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the multiplication.
	 * <p>
	 * If either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion multiplication is performed componentwise.
	 * 
	 * @param quaternionLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param quaternionRHS the {@code Quaternion4D} instance on the right-hand side
	 * @return a new {@code Quaternion4D} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}
	 */
	public static Quaternion4D multiply(final Quaternion4D quaternionLHS, final Quaternion4D quaternionRHS) {
		final double component1 = quaternionLHS.component1 * quaternionRHS.component4 + quaternionLHS.component4 * quaternionRHS.component1 + quaternionLHS.component2 * quaternionRHS.component3 - quaternionLHS.component3 * quaternionRHS.component2;
		final double component2 = quaternionLHS.component2 * quaternionRHS.component4 + quaternionLHS.component4 * quaternionRHS.component2 + quaternionLHS.component3 * quaternionRHS.component1 - quaternionLHS.component1 * quaternionRHS.component3;
		final double component3 = quaternionLHS.component3 * quaternionRHS.component4 + quaternionLHS.component4 * quaternionRHS.component3 + quaternionLHS.component1 * quaternionRHS.component2 - quaternionLHS.component2 * quaternionRHS.component1;
		final double component4 = quaternionLHS.component4 * quaternionRHS.component4 - quaternionLHS.component1 * quaternionRHS.component1 - quaternionLHS.component2 * quaternionRHS.component2 - quaternionLHS.component3 * quaternionRHS.component3;
		
		return new Quaternion4D(component1, component2, component3, component4);
	}
	
	/**
	 * Multiplies the component values of {@code quaternionLHS} with the component values of {@code vectorRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the multiplication.
	 * <p>
	 * If either {@code quaternionLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion multiplication is performed componentwise.
	 * 
	 * @param quaternionLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param vectorRHS the {@link Vector3D} instance on the right-hand side
	 * @return a new {@code Quaternion4D} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, either {@code quaternionLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Quaternion4D multiply(final Quaternion4D quaternionLHS, final Vector3D vectorRHS) {
		final double component1 = +quaternionLHS.component4 * vectorRHS.getComponent1() + quaternionLHS.component2 * vectorRHS.getComponent3() - quaternionLHS.component3 * vectorRHS.getComponent2();
		final double component2 = +quaternionLHS.component4 * vectorRHS.getComponent2() + quaternionLHS.component3 * vectorRHS.getComponent1() - quaternionLHS.component1 * vectorRHS.getComponent3();
		final double component3 = +quaternionLHS.component4 * vectorRHS.getComponent3() + quaternionLHS.component1 * vectorRHS.getComponent2() - quaternionLHS.component2 * vectorRHS.getComponent1();
		final double component4 = -quaternionLHS.component1 * vectorRHS.getComponent1() - quaternionLHS.component2 * vectorRHS.getComponent2() - quaternionLHS.component3 * vectorRHS.getComponent3();
		
		return new Quaternion4D(component1, component2, component3, component4);
	}
	
	/**
	 * Multiplies the component values of {@code quaternionLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the multiplication.
	 * <p>
	 * If {@code quaternionLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion multiplication is performed componentwise.
	 * 
	 * @param quaternionLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Quaternion4D} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code quaternionLHS} is {@code null}
	 */
	public static Quaternion4D multiply(final Quaternion4D quaternionLHS, final double scalarRHS) {
		final double component1 = quaternionLHS.component1 * scalarRHS;
		final double component2 = quaternionLHS.component2 * scalarRHS;
		final double component3 = quaternionLHS.component3 * scalarRHS;
		final double component4 = quaternionLHS.component4 * scalarRHS;
		
		return new Quaternion4D(component1, component2, component3, component4);
	}
	
	/**
	 * Negates the component values of {@code quaternion}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the negation.
	 * <p>
	 * If {@code quaternion} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param quaternion a {@code Quaternion4D} instance
	 * @return a new {@code Quaternion4D} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code quaternion} is {@code null}
	 */
	public static Quaternion4D negate(final Quaternion4D quaternion) {
		final double component1 = -quaternion.component1;
		final double component2 = -quaternion.component2;
		final double component3 = -quaternion.component3;
		final double component4 = -quaternion.component4;
		
		return new Quaternion4D(component1, component2, component3, component4);
	}
	
	/**
	 * Normalizes the component values of {@code quaternion}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the normalization.
	 * <p>
	 * If {@code quaternion} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param quaternion a {@code Quaternion4D} instance
	 * @return a new {@code Quaternion4D} instance with the result of the normalization
	 * @throws NullPointerException thrown if, and only if, {@code quaternion} is {@code null}
	 */
	public static Quaternion4D normalize(final Quaternion4D quaternion) {
		final double length = quaternion.length();
		
		final boolean isLengthGTEThreshold = length >= NEXT_DOWN_1_3;
		final boolean isLengthLTEThreshold = length <= NEXT_UP_1_1;
		
		if(isLengthGTEThreshold && isLengthLTEThreshold) {
			return quaternion;
		}
		
		return divide(quaternion, length);
	}
	
	/**
	 * Returns a new {@code Quaternion4D} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Quaternion4D} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Quaternion4D read(final DataInput dataInput) {
		try {
			final double component1 = dataInput.readDouble();
			final double component2 = dataInput.readDouble();
			final double component3 = dataInput.readDouble();
			final double component4 = dataInput.readDouble();
			
			return new Quaternion4D(component1, component2, component3, component4);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Subtracts the component values of {@code quaternionRHS} from the component values of {@code quaternionLHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the subtraction.
	 * <p>
	 * If either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion subtraction is performed componentwise.
	 * 
	 * @param quaternionLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param quaternionRHS the {@code Quaternion4D} instance on the right-hand side
	 * @return a new {@code Quaternion4D} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}
	 */
	public static Quaternion4D subtract(final Quaternion4D quaternionLHS, final Quaternion4D quaternionRHS) {
		final double component1 = quaternionLHS.component1 - quaternionRHS.component1;
		final double component2 = quaternionLHS.component2 - quaternionRHS.component2;
		final double component3 = quaternionLHS.component3 - quaternionRHS.component3;
		final double component4 = quaternionLHS.component4 - quaternionRHS.component4;
		
		return new Quaternion4D(component1, component2, component3, component4);
	}
	
	/**
	 * Returns the dot product of {@code quaternionLHS} and {@code quaternionRHS}.
	 * <p>
	 * If either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param quaternionLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param quaternionRHS the {@code Quaternion4D} instance on the right-hand side
	 * @return the dot product of {@code quaternionLHS} and {@code quaternionRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}
	 */
	public static double dotProduct(final Quaternion4D quaternionLHS, final Quaternion4D quaternionRHS) {
		return quaternionLHS.component1 * quaternionRHS.component1 + quaternionLHS.component2 * quaternionRHS.component2 + quaternionLHS.component3 * quaternionRHS.component3 + quaternionLHS.component4 * quaternionRHS.component4;
	}
}