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
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.min;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * A {@code Point3F} denotes a 3-dimensional point with three coordinates, of type {@code float}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Point3F {
	private final float component1;
	private final float component2;
	private final float component3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Point3F} instance given the component values {@code 0.0F}, {@code 0.0F} and {@code 0.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point3F(0.0F, 0.0F, 0.0F);
	 * }
	 * </pre>
	 */
	public Point3F() {
		this(0.0F, 0.0F, 0.0F);
	}
	
	/**
	 * Constructs a new {@code Point3F} instance given the component values {@code v.getComponent1()}, {@code v.getComponent2()} and {@code v.getComponent3()}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point3F(v.getComponent1(), v.getComponent2(), v.getComponent3());
	 * }
	 * </pre>
	 * 
	 * @param v a {@link Vector3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public Point3F(final Vector3F v) {
		this(v.getComponent1(), v.getComponent2(), v.getComponent3());
	}
	
	/**
	 * Constructs a new {@code Point3F} instance given the component values {@code component1}, {@code component2} and {@code component3}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	public Point3F(final float component1, final float component2, final float component3) {
		this.component1 = component1;
		this.component2 = component2;
		this.component3 = component3;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Point3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Point3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Point3F(%+.10f, %+.10f, %+.10f)", Float.valueOf(this.component1), Float.valueOf(this.component2), Float.valueOf(this.component3));
	}
	
	/**
	 * Compares {@code object} to this {@code Point3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Point3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Point3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Point3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Point3F)) {
			return false;
		} else if(!equal(this.component1, Point3F.class.cast(object).component1)) {
			return false;
		} else if(!equal(this.component2, Point3F.class.cast(object).component2)) {
			return false;
		} else if(!equal(this.component3, Point3F.class.cast(object).component3)) {
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
	 * Returns the value of the U-component.
	 * 
	 * @return the value of the U-component
	 */
	public float getU() {
		return this.component1;
	}
	
	/**
	 * Returns the value of the V-component.
	 * 
	 * @return the value of the V-component
	 */
	public float getV() {
		return this.component2;
	}
	
	/**
	 * Returns the value of the W-component.
	 * 
	 * @return the value of the W-component
	 */
	public float getW() {
		return this.component3;
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
	 * Returns a hash code for this {@code Point3F} instance.
	 * 
	 * @return a hash code for this {@code Point3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.component1), Float.valueOf(this.component2), Float.valueOf(this.component3));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds the component values of {@code vRHS} to the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point3F} instance with the result of the addition.
	 * <p>
	 * If either {@code pLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point3F} instance on the left-hand side
	 * @param vRHS the {@link Vector3F} instance on the right-hand side
	 * @return a new {@code Point3F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code pLHS} or {@code vRHS} are {@code null}
	 */
	public static Point3F add(final Point3F pLHS, final Vector3F vRHS) {
		final float component1 = pLHS.component1 + vRHS.getComponent1();
		final float component2 = pLHS.component2 + vRHS.getComponent2();
		final float component3 = pLHS.component3 + vRHS.getComponent3();
		
		return new Point3F(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
	public static Point3F centroid(final Point3F pA, final Point3F pB, final Point3F pC) {
		final float component1 = (pA.component1 + pB.component1 + pC.component1) / 3.0F;
		final float component2 = (pA.component2 + pB.component2 + pC.component2) / 3.0F;
		final float component3 = (pA.component3 + pB.component3 + pC.component3) / 3.0F;
		
		return new Point3F(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
	public static Point3F maximum() {
		final float component1 = Float.MAX_VALUE;
		final float component2 = Float.MAX_VALUE;
		final float component3 = Float.MAX_VALUE;
		
		return new Point3F(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
	public static Point3F maximum(final Point3F pA, final Point3F pB) {
		final float component1 = max(pA.component1, pB.component1);
		final float component2 = max(pA.component2, pB.component2);
		final float component3 = max(pA.component3, pB.component3);
		
		return new Point3F(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
	public static Point3F maximum(final Point3F pA, final Point3F pB, final Point3F pC) {
		final float component1 = max(pA.component1, pB.component1, pC.component1);
		final float component2 = max(pA.component2, pB.component2, pC.component2);
		final float component3 = max(pA.component3, pB.component3, pC.component3);
		
		return new Point3F(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
	public static Point3F midpoint(final Point3F pA, final Point3F pB) {
		final float component1 = (pA.component1 + pB.component1) * 0.5F;
		final float component2 = (pA.component2 + pB.component2) * 0.5F;
		final float component3 = (pA.component3 + pB.component3) * 0.5F;
		
		return new Point3F(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
	public static Point3F minimum() {
		final float component1 = Float.MIN_VALUE;
		final float component2 = Float.MIN_VALUE;
		final float component3 = Float.MIN_VALUE;
		
		return new Point3F(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
	public static Point3F minimum(final Point3F pA, final Point3F pB) {
		final float component1 = min(pA.component1, pB.component1);
		final float component2 = min(pA.component2, pB.component2);
		final float component3 = min(pA.component3, pB.component3);
		
		return new Point3F(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
	public static Point3F minimum(final Point3F pA, final Point3F pB, final Point3F pC) {
		final float component1 = min(pA.component1, pB.component1, pC.component1);
		final float component2 = min(pA.component2, pB.component2, pC.component2);
		final float component3 = min(pA.component3, pB.component3, pC.component3);
		
		return new Point3F(component1, component2, component3);
	}
	
	/**
	 * Subtracts the component values of {@code vRHS} from the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point3F} instance with the result of the subtraction.
	 * <p>
	 * If either {@code pLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point3F} instance on the left-hand side
	 * @param vRHS the {@link Vector3F} instance on the right-hand side
	 * @return a new {@code Point3F} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code pLHS} or {@code vRHS} are {@code null}
	 */
	public static Point3F subtract(final Point3F pLHS, final Vector3F vRHS) {
		final float component1 = pLHS.component1 - vRHS.getComponent1();
		final float component2 = pLHS.component2 - vRHS.getComponent2();
		final float component3 = pLHS.component3 - vRHS.getComponent3();
		
		return new Point3F(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
	public static Point3F transform(final Matrix44F mLHS, final Point3F pRHS) {
		final float component1 = mLHS.getElement11() * pRHS.component1 + mLHS.getElement12() * pRHS.component2 + mLHS.getElement13() * pRHS.component3 + mLHS.getElement14();
		final float component2 = mLHS.getElement21() * pRHS.component1 + mLHS.getElement22() * pRHS.component2 + mLHS.getElement23() * pRHS.component3 + mLHS.getElement24();
		final float component3 = mLHS.getElement31() * pRHS.component1 + mLHS.getElement32() * pRHS.component2 + mLHS.getElement33() * pRHS.component3 + mLHS.getElement34();
		
		return new Point3F(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
	public static Point3F transformAndDivide(final Matrix44F mLHS, final Point3F pRHS) {
		final float component1 = mLHS.getElement11() * pRHS.component1 + mLHS.getElement12() * pRHS.component2 + mLHS.getElement13() * pRHS.component3 + mLHS.getElement14();
		final float component2 = mLHS.getElement21() * pRHS.component1 + mLHS.getElement22() * pRHS.component2 + mLHS.getElement23() * pRHS.component3 + mLHS.getElement24();
		final float component3 = mLHS.getElement31() * pRHS.component1 + mLHS.getElement32() * pRHS.component2 + mLHS.getElement33() * pRHS.component3 + mLHS.getElement34();
		final float component4 = mLHS.getElement41() * pRHS.component1 + mLHS.getElement42() * pRHS.component2 + mLHS.getElement43() * pRHS.component3 + mLHS.getElement44();
		
		return equal(component4, 1.0F) || equal(component4, 0.0F) ? new Point3F(component1, component2, component3) : new Point3F(component1 / component4, component2 / component4, component3 / component4);
	}
	
	/**
	 * Returns the distance from {@code pEye} to {@code pLookAt}.
	 * <p>
	 * If either {@code pEye} or {@code pLookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pEye a {@code Point3F} instance
	 * @param pLookAt a {@code Point3F} instance
	 * @return the distance from {@code pEye} to {@code pLookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code pEye} or {@code pLookAt} are {@code null}
	 */
	public static float distance(final Point3F pEye, final Point3F pLookAt) {
		return Vector3F.direction(pEye, pLookAt).length();
	}
	
	/**
	 * Returns the squared distance from {@code pEye} to {@code pLookAt}.
	 * <p>
	 * If either {@code pEye} or {@code pLookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pEye a {@code Point3F} instance
	 * @param pLookAt a {@code Point3F} instance
	 * @return the squared distance from {@code pEye} to {@code pLookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code pEye} or {@code pLookAt} are {@code null}
	 */
	public static float distanceSquared(final Point3F pEye, final Point3F pLookAt) {
		return Vector3F.direction(pEye, pLookAt).lengthSquared();
	}
}