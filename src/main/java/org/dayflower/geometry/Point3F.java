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

//TODO: Add Javadocs!
public final class Point3F {
	private final float element1;
	private final float element2;
	private final float element3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Point3F() {
		this(0.0F, 0.0F, 0.0F);
	}
	
//	TODO: Add Javadocs!
	public Point3F(final Vector3F v) {
		this(v.getElement1(), v.getElement2(), v.getElement3());
	}
	
//	TODO: Add Javadocs!
	public Point3F(final float element1, final float element2, final float element3) {
		this.element1 = element1;
		this.element2 = element2;
		this.element3 = element3;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public String toString() {
		return String.format("new Point3F(%+.10f, %+.10f, %+.10f)", Float.valueOf(this.element1), Float.valueOf(this.element2), Float.valueOf(this.element3));
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Point3F)) {
			return false;
		} else if(!equal(this.element1, Point3F.class.cast(object).element1)) {
			return false;
		} else if(!equal(this.element2, Point3F.class.cast(object).element2)) {
			return false;
		} else if(!equal(this.element3, Point3F.class.cast(object).element3)) {
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
	public float getElement3() {
		return this.element3;
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
	public float getW() {
		return this.element3;
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
	public float getZ() {
		return this.element3;
	}
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.element1), Float.valueOf(this.element2), Float.valueOf(this.element3));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static Point3F add(final Point3F pLHS, final Vector3F vRHS) {
		final float element1 = pLHS.element1 + vRHS.getElement1();
		final float element2 = pLHS.element2 + vRHS.getElement2();
		final float element3 = pLHS.element3 + vRHS.getElement3();
		
		return new Point3F(element1, element2, element3);
	}
	
//	TODO: Add Javadocs!
	public static Point3F centroid(final Point3F pA, final Point3F pB, final Point3F pC) {
		final float element1 = (pA.element1 + pB.element1 + pC.element1) / 3.0F;
		final float element2 = (pA.element2 + pB.element2 + pC.element2) / 3.0F;
		final float element3 = (pA.element3 + pB.element3 + pC.element3) / 3.0F;
		
		return new Point3F(element1, element2, element3);
	}
	
//	TODO: Add Javadocs!
	public static Point3F maximum() {
		final float element1 = Float.MAX_VALUE;
		final float element2 = Float.MAX_VALUE;
		final float element3 = Float.MAX_VALUE;
		
		return new Point3F(element1, element2, element3);
	}
	
//	TODO: Add Javadocs!
	public static Point3F maximum(final Point3F pA, final Point3F pB) {
		final float element1 = max(pA.element1, pB.element1);
		final float element2 = max(pA.element2, pB.element2);
		final float element3 = max(pA.element3, pB.element3);
		
		return new Point3F(element1, element2, element3);
	}
	
//	TODO: Add Javadocs!
	public static Point3F maximum(final Point3F pA, final Point3F pB, final Point3F pC) {
		final float element1 = max(pA.element1, pB.element1, pC.element1);
		final float element2 = max(pA.element2, pB.element2, pC.element2);
		final float element3 = max(pA.element3, pB.element3, pC.element3);
		
		return new Point3F(element1, element2, element3);
	}
	
//	TODO: Add Javadocs!
	public static Point3F midpoint(final Point3F pA, final Point3F pB) {
		final float element1 = (pA.element1 + pB.element1) * 0.5F;
		final float element2 = (pA.element2 + pB.element2) * 0.5F;
		final float element3 = (pA.element3 + pB.element3) * 0.5F;
		
		return new Point3F(element1, element2, element3);
	}
	
//	TODO: Add Javadocs!
	public static Point3F minimum() {
		final float element1 = Float.MIN_VALUE;
		final float element2 = Float.MIN_VALUE;
		final float element3 = Float.MIN_VALUE;
		
		return new Point3F(element1, element2, element3);
	}
	
//	TODO: Add Javadocs!
	public static Point3F minimum(final Point3F pA, final Point3F pB) {
		final float element1 = min(pA.element1, pB.element1);
		final float element2 = min(pA.element2, pB.element2);
		final float element3 = min(pA.element3, pB.element3);
		
		return new Point3F(element1, element2, element3);
	}
	
//	TODO: Add Javadocs!
	public static Point3F minimum(final Point3F pA, final Point3F pB, final Point3F pC) {
		final float element1 = min(pA.element1, pB.element1, pC.element1);
		final float element2 = min(pA.element2, pB.element2, pC.element2);
		final float element3 = min(pA.element3, pB.element3, pC.element3);
		
		return new Point3F(element1, element2, element3);
	}
	
//	TODO: Add Javadocs!
	public static Point3F subtract(final Point3F pLHS, final Vector3F vRHS) {
		final float element1 = pLHS.element1 - vRHS.getElement1();
		final float element2 = pLHS.element2 - vRHS.getElement2();
		final float element3 = pLHS.element3 - vRHS.getElement3();
		
		return new Point3F(element1, element2, element3);
	}
	
//	TODO: Add Javadocs!
	public static Point3F transform(final Matrix44F mLHS, final Point3F pRHS) {
		final float element1 = mLHS.getElement11() * pRHS.element1 + mLHS.getElement12() * pRHS.element2 + mLHS.getElement13() * pRHS.element3 + mLHS.getElement14();
		final float element2 = mLHS.getElement21() * pRHS.element1 + mLHS.getElement22() * pRHS.element2 + mLHS.getElement23() * pRHS.element3 + mLHS.getElement24();
		final float element3 = mLHS.getElement31() * pRHS.element1 + mLHS.getElement32() * pRHS.element2 + mLHS.getElement33() * pRHS.element3 + mLHS.getElement34();
		
		return new Point3F(element1, element2, element3);
	}
	
//	TODO: Add Javadocs!
	public static Point3F transformAndDivide(final Matrix44F mLHS, final Point3F pRHS) {
		final float element1 = mLHS.getElement11() * pRHS.element1 + mLHS.getElement12() * pRHS.element2 + mLHS.getElement13() * pRHS.element3 + mLHS.getElement14();
		final float element2 = mLHS.getElement21() * pRHS.element1 + mLHS.getElement22() * pRHS.element2 + mLHS.getElement23() * pRHS.element3 + mLHS.getElement24();
		final float element3 = mLHS.getElement31() * pRHS.element1 + mLHS.getElement32() * pRHS.element2 + mLHS.getElement33() * pRHS.element3 + mLHS.getElement34();
		final float element4 = mLHS.getElement41() * pRHS.element1 + mLHS.getElement42() * pRHS.element2 + mLHS.getElement43() * pRHS.element3 + mLHS.getElement44();
		
		return equal(element4, 1.0F) || equal(element4, 0.0F) ? new Point3F(element1, element2, element3) : new Point3F(element1 / element4, element2 / element4, element3 / element4);
	}
	
//	TODO: Add Javadocs!
	public static float distance(final Point3F pEye, final Point3F pLookAt) {
		return Vector3F.direction(pEye, pLookAt).length();
	}
	
//	TODO: Add Javadocs!
	public static float distanceSquared(final Point3F pEye, final Point3F pLookAt) {
		return Vector3F.direction(pEye, pLookAt).lengthSquared();
	}
}