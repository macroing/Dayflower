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
	private final float element1;
	private final float element2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Vector2F() {
		this(0.0F, 0.0F);
	}
	
//	TODO: Add Javadocs!
	public Vector2F(final Point2F p) {
		this(p.getElement1(), p.getElement2());
	}
	
//	TODO: Add Javadocs!
	public Vector2F(final float element1, final float element2) {
		this.element1 = element1;
		this.element2 = element2;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public String toString() {
		return String.format("new Vector2F(%+.10f, %+.10f)", Float.valueOf(this.element1), Float.valueOf(this.element2));
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Vector2F)) {
			return false;
		} else if(!equal(this.element1, Vector2F.class.cast(object).element1)) {
			return false;
		} else if(!equal(this.element2, Vector2F.class.cast(object).element2)) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
	public float getElement1() {
		return this.element1;
	}
	
//	TODO: Add Javadocs!
	public float getElement2() {
		return this.element2;
	}
	
//	TODO: Add Javadocs!
	public float getU() {
		return this.element1;
	}
	
//	TODO: Add Javadocs!
	public float getV() {
		return this.element2;
	}
	
//	TODO: Add Javadocs!
	public float getX() {
		return this.element1;
	}
	
//	TODO: Add Javadocs!
	public float getY() {
		return this.element2;
	}
	
//	TODO: Add Javadocs!
	public float length() {
		return sqrt(lengthSquared());
	}
	
//	TODO: Add Javadocs!
	public float lengthSquared() {
		return this.element1 * this.element1 + this.element2 * this.element2;
	}
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.element1), Float.valueOf(this.element2));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static Vector2F add(final Vector2F vLHS, final Vector2F vRHS) {
		final float element1 = vLHS.element1 + vRHS.element1;
		final float element2 = vLHS.element2 + vRHS.element2;
		
		return new Vector2F(element1, element2);
	}
	
//	TODO: Add Javadocs!
	public static Vector2F direction(final Point2F pEye, final Point2F pLookAt) {
		final float element1 = pLookAt.getElement1() - pEye.getElement1();
		final float element2 = pLookAt.getElement2() - pEye.getElement2();
		
		return new Vector2F(element1, element2);
	}
	
//	TODO: Add Javadocs!
	public static Vector2F divide(final Vector2F vLHS, final float sRHS) {
		final float element1 = vLHS.element1 / sRHS;
		final float element2 = vLHS.element2 / sRHS;
		
		return new Vector2F(element1, element2);
	}
	
//	TODO: Add Javadocs!
	public static Vector2F multiply(final Vector2F vLHS, final float sRHS) {
		final float element1 = vLHS.element1 * sRHS;
		final float element2 = vLHS.element2 * sRHS;
		
		return new Vector2F(element1, element2);
	}
	
//	TODO: Add Javadocs!
	public static Vector2F negate(final Vector2F v) {
		final float element1 = -v.element1;
		final float element2 = -v.element2;
		
		return new Vector2F(element1, element2);
	}
	
//	TODO: Add Javadocs!
	public static Vector2F normalize(final Vector2F v) {
		return divide(v, v.length());
	}
	
//	TODO: Add Javadocs!
	public static Vector2F subtract(final Vector2F vLHS, final Vector2F vRHS) {
		final float element1 = vLHS.element1 - vRHS.element1;
		final float element2 = vLHS.element2 - vRHS.element2;
		
		return new Vector2F(element1, element2);
	}
	
//	TODO: Add Javadocs!
	public static float dotProduct(final Vector2F vLHS, final Vector2F vRHS) {
		return vLHS.element1 * vRHS.element1 + vLHS.element2 * vRHS.element2;
	}
}