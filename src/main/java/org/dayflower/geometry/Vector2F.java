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

import java.lang.reflect.Field;
import java.util.Objects;

//TODO: Add Javadocs!
public final class Vector2F {
	private final float component1;
	private final float component2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Vector2F() {
		this(0.0F, 0.0F);
	}
	
//	TODO: Add Javadocs!
	public Vector2F(final Point2F p) {
		this(p.getComponent1(), p.getComponent2());
	}
	
//	TODO: Add Javadocs!
	public Vector2F(final float component1, final float component2) {
		this.component1 = component1;
		this.component2 = component2;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public String toString() {
		return String.format("new Vector2F(%+.10f, %+.10f)", Float.valueOf(this.component1), Float.valueOf(this.component2));
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Vector2F)) {
			return false;
		} else if(!equal(this.component1, Vector2F.class.cast(object).component1)) {
			return false;
		} else if(!equal(this.component2, Vector2F.class.cast(object).component2)) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
	public float getComponent1() {
		return this.component1;
	}
	
//	TODO: Add Javadocs!
	public float getComponent2() {
		return this.component2;
	}
	
//	TODO: Add Javadocs!
	public float getU() {
		return this.component1;
	}
	
//	TODO: Add Javadocs!
	public float getV() {
		return this.component2;
	}
	
//	TODO: Add Javadocs!
	public float getX() {
		return this.component1;
	}
	
//	TODO: Add Javadocs!
	public float getY() {
		return this.component2;
	}
	
//	TODO: Add Javadocs!
	public float length() {
		return sqrt(lengthSquared());
	}
	
//	TODO: Add Javadocs!
	public float lengthSquared() {
		return this.component1 * this.component1 + this.component2 * this.component2;
	}
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.component1), Float.valueOf(this.component2));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static Vector2F add(final Vector2F vLHS, final Vector2F vRHS) {
		final float component1 = vLHS.component1 + vRHS.component1;
		final float component2 = vLHS.component2 + vRHS.component2;
		
		return new Vector2F(component1, component2);
	}
	
//	TODO: Add Javadocs!
	public static Vector2F direction(final Point2F pEye, final Point2F pLookAt) {
		final float component1 = pLookAt.getComponent1() - pEye.getComponent1();
		final float component2 = pLookAt.getComponent2() - pEye.getComponent2();
		
		return new Vector2F(component1, component2);
	}
	
//	TODO: Add Javadocs!
	public static Vector2F divide(final Vector2F vLHS, final float sRHS) {
		final float component1 = vLHS.component1 / sRHS;
		final float component2 = vLHS.component2 / sRHS;
		
		return new Vector2F(component1, component2);
	}
	
//	TODO: Add Javadocs!
	public static Vector2F multiply(final Vector2F vLHS, final float sRHS) {
		final float component1 = vLHS.component1 * sRHS;
		final float component2 = vLHS.component2 * sRHS;
		
		return new Vector2F(component1, component2);
	}
	
//	TODO: Add Javadocs!
	public static Vector2F negate(final Vector2F v) {
		final float component1 = -v.component1;
		final float component2 = -v.component2;
		
		return new Vector2F(component1, component2);
	}
	
//	TODO: Add Javadocs!
	public static Vector2F normalize(final Vector2F v) {
		return divide(v, v.length());
	}
	
//	TODO: Add Javadocs!
	public static Vector2F subtract(final Vector2F vLHS, final Vector2F vRHS) {
		final float component1 = vLHS.component1 - vRHS.component1;
		final float component2 = vLHS.component2 - vRHS.component2;
		
		return new Vector2F(component1, component2);
	}
	
//	TODO: Add Javadocs!
	public static float dotProduct(final Vector2F vLHS, final Vector2F vRHS) {
		return vLHS.component1 * vRHS.component1 + vLHS.component2 * vRHS.component2;
	}
}