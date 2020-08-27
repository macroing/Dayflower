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

import static org.dayflower.util.Floats.PI_DIVIDED_BY_2;
import static org.dayflower.util.Floats.PI_DIVIDED_BY_4;
import static org.dayflower.util.Floats.PI_MULTIPLIED_BY_2;
import static org.dayflower.util.Floats.cos;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.random;
import static org.dayflower.util.Floats.sin;
import static org.dayflower.util.Floats.sqrt;

import java.lang.reflect.Field;
import java.util.Objects;

//TODO: Add Javadocs!
public final class Point2F {
	private final float element1;
	private final float element2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Point2F() {
		this(0.0F, 0.0F);
	}
	
//	TODO: Add Javadocs!
	public Point2F(final Vector2F v) {
		this(v.getElement1(), v.getElement2());
	}
	
//	TODO: Add Javadocs!
	public Point2F(final float element1, final float element2) {
		this.element1 = element1;
		this.element2 = element2;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public String toString() {
		return String.format("new Point2F(%+.10f, %+.10f)", Float.valueOf(this.element1), Float.valueOf(this.element2));
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Point2F)) {
			return false;
		} else if(!equal(this.element1, Point2F.class.cast(object).element1)) {
			return false;
		} else if(!equal(this.element2, Point2F.class.cast(object).element2)) {
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
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.element1), Float.valueOf(this.element2));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static Point2F sampleConcentricDisk() {
		return sampleConcentricDisk(random(), random());
	}
	
//	TODO: Add Javadocs!
	public static Point2F sampleConcentricDisk(final float u, final float v) {
		return sampleConcentricDisk(u, v, 1.0F);
	}
	
//	TODO: Add Javadocs!
	public static Point2F sampleConcentricDisk(final float u, final float v, final float radius) {
		if(equal(u, 0.0F) && equal(v, 0.0F)) {
			return new Point2F();
		}
		
		final float a = u * 2.0F - 1.0F;
		final float b = v * 2.0F - 1.0F;
		
		if(a * a > b * b) {
			final float phi = PI_DIVIDED_BY_4 * (b / a);
			final float r = radius * a;
			
			final float element1 = cos(phi) * r;
			final float element2 = sin(phi) * r;
			
			return new Point2F(element1, element2);
		}
		
		final float phi = PI_DIVIDED_BY_2 - (PI_DIVIDED_BY_4 * (a / b));
		final float r = radius * b;
		
		final float element1 = cos(phi) * r;
		final float element2 = sin(phi) * r;
		
		return new Point2F(element1, element2);
	}
	
//	TODO: Add Javadocs!
	public static Point2F sampleDiskUniformDistribution() {
		return sampleDiskUniformDistribution(random(), random());
	}
	
//	TODO: Add Javadocs!
	public static Point2F sampleDiskUniformDistribution(final float u, final float v) {
		final float phi = PI_MULTIPLIED_BY_2 * v;
		final float r = sqrt(u);
		
		final float element1 = cos(phi) * r;
		final float element2 = sin(phi) * r;
		
		return new Point2F(element1, element2);
	}
	
//	TODO: Add Javadocs!
	public static float distance(final Point2F pEye, final Point2F pLookAt) {
		return Vector2F.direction(pEye, pLookAt).length();
	}
	
//	TODO: Add Javadocs!
	public static float distanceSquared(final Point2F pEye, final Point2F pLookAt) {
		return Vector2F.direction(pEye, pLookAt).lengthSquared();
	}
}