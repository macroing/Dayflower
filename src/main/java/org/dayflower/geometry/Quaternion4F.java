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

import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.sqrt;

import java.util.Objects;

/**
 * A {@code Quaternion4F} denotes a 4-dimensional quaternion with four components, of type {@code float}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Quaternion4F {
	private final float component1;
	private final float component2;
	private final float component3;
	private final float component4;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Quaternion4F} instance given the component values {@code 0.0F}, {@code 0.0F}, {@code 0.0F} and {@code 1.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Quaternion4F(0.0F, 0.0F, 0.0F);
	 * }
	 * </pre>
	 */
	public Quaternion4F() {
		this(0.0F, 0.0F, 0.0F);
	}
	
	/**
	 * Constructs a new {@code Quaternion4F} instance given the component values {@code vector.getComponent1()}, {@code vector.getComponent2()}, {@code vector.getComponent3()} and {@code 1.0F}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Quaternion4F(vector.getComponent1(), vector.getComponent2(), vector.getComponent3());
	 * }
	 * </pre>
	 * 
	 * @param vector a {@link Vector3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public Quaternion4F(final Vector3F vector) {
		this(vector.getComponent1(), vector.getComponent2(), vector.getComponent3());
	}
	
	/**
	 * Constructs a new {@code Quaternion4F} instance given the component values {@code component1}, {@code component2}, {@code component3} and {@code 1.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Quaternion4F(component1, component2, component3, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	public Quaternion4F(final float component1, final float component2, final float component3) {
		this(component1, component2, component3, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Quaternion4F} instance given the component values {@code component1}, {@code component2}, {@code component3} and {@code component4}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 * @param component4 the value of component 4
	 */
	public Quaternion4F(final float component1, final float component2, final float component3, final float component4) {
		this.component1 = component1;
		this.component2 = component2;
		this.component3 = component3;
		this.component4 = component4;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Quaternion4F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Quaternion4F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Quaternion4F(%+.10f, %+.10f, %+.10f, %+.10f)", Float.valueOf(this.component1), Float.valueOf(this.component2), Float.valueOf(this.component3), Float.valueOf(this.component4));
	}
	
	/**
	 * Compares {@code object} to this {@code Quaternion4F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Quaternion4F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Quaternion4F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Quaternion4F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Quaternion4F)) {
			return false;
		} else if(!equal(this.component1, Quaternion4F.class.cast(object).component1)) {
			return false;
		} else if(!equal(this.component2, Quaternion4F.class.cast(object).component2)) {
			return false;
		} else if(!equal(this.component3, Quaternion4F.class.cast(object).component3)) {
			return false;
		} else if(!equal(this.component4, Quaternion4F.class.cast(object).component4)) {
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
	public float getComponent1() {
		return this.component1;
	}
	
	/**
	 * Returns the value of component 2.
	 * 
	 * @return the value of component 2
	 */
	public float getComponent2() {
		return this.component2;
	}
	
	/**
	 * Returns the value of component 3.
	 * 
	 * @return the value of component 3
	 */
	public float getComponent3() {
		return this.component3;
	}
	
	/**
	 * Returns the value of component 4.
	 * 
	 * @return the value of component 4
	 */
	public float getComponent4() {
		return this.component4;
	}
	
	/**
	 * Returns the value of the W-component.
	 * 
	 * @return the value of the W-component
	 */
	public float getW() {
		return this.component4;
	}
	
	/**
	 * Returns the value of the X-component.
	 * 
	 * @return the value of the X-component
	 */
	public float getX() {
		return this.component1;
	}
	
	/**
	 * Returns the value of the Y-component.
	 * 
	 * @return the value of the Y-component
	 */
	public float getY() {
		return this.component2;
	}
	
	/**
	 * Returns the value of the Z-component.
	 * 
	 * @return the value of the Z-component
	 */
	public float getZ() {
		return this.component3;
	}
	
	/**
	 * Returns the length of this {@code Quaternion4F} instance.
	 * 
	 * @return the length of this {@code Quaternion4F} instance
	 */
	public float length() {
		return sqrt(lengthSquared());
	}
	
	/**
	 * Returns the squared length of this {@code Quaternion4F} instance.
	 * 
	 * @return the squared length of this {@code Quaternion4F} instance
	 */
	public float lengthSquared() {
		return this.component1 * this.component1 + this.component2 * this.component2 + this.component3 * this.component3 + this.component4 * this.component4;
	}
	
	/**
	 * Returns a hash code for this {@code Quaternion4F} instance.
	 * 
	 * @return a hash code for this {@code Quaternion4F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.component1), Float.valueOf(this.component2), Float.valueOf(this.component3), Float.valueOf(this.component4));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds the component values of {@code quaternionRHS} to the component values of {@code quaternionLHS}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the addition.
	 * <p>
	 * If either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion addition is performed componentwise.
	 * 
	 * @param quaternionLHS the {@code Quaternion4F} instance on the left-hand side
	 * @param quaternionRHS the {@code Quaternion4F} instance on the right-hand side
	 * @return a new {@code Quaternion4F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}
	 */
	public static Quaternion4F add(final Quaternion4F quaternionLHS, final Quaternion4F quaternionRHS) {
		final float component1 = quaternionLHS.component1 + quaternionRHS.component1;
		final float component2 = quaternionLHS.component2 + quaternionRHS.component2;
		final float component3 = quaternionLHS.component3 + quaternionRHS.component3;
		final float component4 = quaternionLHS.component4 + quaternionRHS.component4;
		
		return new Quaternion4F(component1, component2, component3, component4);
	}
	
	/**
	 * Conjugates the component values of {@code quaternion}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the conjugation.
	 * <p>
	 * If {@code quaternion} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param quaternion a {@code Quaternion4F} instance
	 * @return a new {@code Quaternion4F} instance with the result of the conjugation
	 * @throws NullPointerException thrown if, and only if, {@code quaternion} is {@code null}
	 */
	public static Quaternion4F conjugate(final Quaternion4F quaternion) {
		final float component1 = -quaternion.component1;
		final float component2 = -quaternion.component2;
		final float component3 = -quaternion.component3;
		final float component4 = +quaternion.component4;
		
		return new Quaternion4F(component1, component2, component3, component4);
	}
	
	/**
	 * Divides the component values of {@code quaternionLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the division.
	 * <p>
	 * If {@code quaternionLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion division is performed componentwise.
	 * 
	 * @param quaternionLHS the {@code Quaternion4F} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Quaternion4F} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, {@code quaternionLHS} is {@code null}
	 */
	public static Quaternion4F divide(final Quaternion4F quaternionLHS, final float scalarRHS) {
		final float component1 = quaternionLHS.component1 / scalarRHS;
		final float component2 = quaternionLHS.component2 / scalarRHS;
		final float component3 = quaternionLHS.component3 / scalarRHS;
		final float component4 = quaternionLHS.component4 / scalarRHS;
		
		return new Quaternion4F(component1, component2, component3, component4);
	}
	
	/**
	 * Multiplies the component values of {@code quaternionLHS} with the component values of {@code quaternionRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the multiplication.
	 * <p>
	 * If either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion multiplication is performed componentwise.
	 * 
	 * @param quaternionLHS the {@code Quaternion4F} instance on the left-hand side
	 * @param quaternionRHS the {@code Quaternion4F} instance on the right-hand side
	 * @return a new {@code Quaternion4F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}
	 */
	public static Quaternion4F multiply(final Quaternion4F quaternionLHS, final Quaternion4F quaternionRHS) {
		final float component1 = quaternionLHS.component1 * quaternionRHS.component4 + quaternionLHS.component4 * quaternionRHS.component1 + quaternionLHS.component2 * quaternionRHS.component3 - quaternionLHS.component3 * quaternionRHS.component2;
		final float component2 = quaternionLHS.component2 * quaternionRHS.component4 + quaternionLHS.component4 * quaternionRHS.component2 + quaternionLHS.component3 * quaternionRHS.component1 - quaternionLHS.component1 * quaternionRHS.component3;
		final float component3 = quaternionLHS.component3 * quaternionRHS.component4 + quaternionLHS.component4 * quaternionRHS.component3 + quaternionLHS.component1 * quaternionRHS.component2 - quaternionLHS.component2 * quaternionRHS.component1;
		final float component4 = quaternionLHS.component4 * quaternionRHS.component4 - quaternionLHS.component1 * quaternionRHS.component1 - quaternionLHS.component2 * quaternionRHS.component2 - quaternionLHS.component3 * quaternionRHS.component3;
		
		return new Quaternion4F(component1, component2, component3, component4);
	}
	
	/**
	 * Multiplies the component values of {@code quaternionLHS} with the component values of {@code vectorRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the multiplication.
	 * <p>
	 * If either {@code quaternionLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion multiplication is performed componentwise.
	 * 
	 * @param quaternionLHS the {@code Quaternion4F} instance on the left-hand side
	 * @param vectorRHS the {@link Vector3F} instance on the right-hand side
	 * @return a new {@code Quaternion4F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, either {@code quaternionLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Quaternion4F multiply(final Quaternion4F quaternionLHS, final Vector3F vectorRHS) {
		final float component1 = +quaternionLHS.component4 * vectorRHS.getComponent1() + quaternionLHS.component2 * vectorRHS.getComponent3() - quaternionLHS.component3 * vectorRHS.getComponent2();
		final float component2 = +quaternionLHS.component4 * vectorRHS.getComponent2() + quaternionLHS.component3 * vectorRHS.getComponent1() - quaternionLHS.component1 * vectorRHS.getComponent3();
		final float component3 = +quaternionLHS.component4 * vectorRHS.getComponent3() + quaternionLHS.component1 * vectorRHS.getComponent2() - quaternionLHS.component2 * vectorRHS.getComponent1();
		final float component4 = -quaternionLHS.component1 * vectorRHS.getComponent1() - quaternionLHS.component2 * vectorRHS.getComponent2() - quaternionLHS.component3 * vectorRHS.getComponent3();
		
		return new Quaternion4F(component1, component2, component3, component4);
	}
	
	/**
	 * Multiplies the component values of {@code quaternionLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the multiplication.
	 * <p>
	 * If {@code quaternionLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion multiplication is performed componentwise.
	 * 
	 * @param quaternionLHS the {@code Quaternion4F} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Quaternion4F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code quaternionLHS} is {@code null}
	 */
	public static Quaternion4F multiply(final Quaternion4F quaternionLHS, final float scalarRHS) {
		final float component1 = quaternionLHS.component1 * scalarRHS;
		final float component2 = quaternionLHS.component2 * scalarRHS;
		final float component3 = quaternionLHS.component3 * scalarRHS;
		final float component4 = quaternionLHS.component4 * scalarRHS;
		
		return new Quaternion4F(component1, component2, component3, component4);
	}
	
	/**
	 * Negates the component values of {@code quaternion}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the negation.
	 * <p>
	 * If {@code quaternion} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param quaternion a {@code Quaternion4F} instance
	 * @return a new {@code Quaternion4F} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code quaternion} is {@code null}
	 */
	public static Quaternion4F negate(final Quaternion4F quaternion) {
		final float component1 = -quaternion.component1;
		final float component2 = -quaternion.component2;
		final float component3 = -quaternion.component3;
		final float component4 = -quaternion.component4;
		
		return new Quaternion4F(component1, component2, component3, component4);
	}
	
	/**
	 * Subtracts the component values of {@code quaternionRHS} from the component values of {@code quaternionLHS}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the subtraction.
	 * <p>
	 * If either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion subtraction is performed componentwise.
	 * 
	 * @param quaternionLHS the {@code Quaternion4F} instance on the left-hand side
	 * @param quaternionRHS the {@code Quaternion4F} instance on the right-hand side
	 * @return a new {@code Quaternion4F} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code quaternionLHS} or {@code quaternionRHS} are {@code null}
	 */
	public static Quaternion4F subtract(final Quaternion4F quaternionLHS, final Quaternion4F quaternionRHS) {
		final float component1 = quaternionLHS.component1 - quaternionRHS.component1;
		final float component2 = quaternionLHS.component2 - quaternionRHS.component2;
		final float component3 = quaternionLHS.component3 - quaternionRHS.component3;
		final float component4 = quaternionLHS.component4 - quaternionRHS.component4;
		
		return new Quaternion4F(component1, component2, component3, component4);
	}
}