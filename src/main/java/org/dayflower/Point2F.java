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
package org.dayflower;

import static org.dayflower.Floats.PI_DIVIDED_BY_FOUR;
import static org.dayflower.Floats.PI_DIVIDED_BY_TWO;
import static org.dayflower.Floats.PI_MULTIPLIED_BY_TWO;
import static org.dayflower.Floats.cos;
import static org.dayflower.Floats.equal;
import static org.dayflower.Floats.random;
import static org.dayflower.Floats.sin;
import static org.dayflower.Floats.sqrt;

import java.util.Objects;

public final class Point2F {
	private final float element1;
	private final float element2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Point2F() {
		this(0.0F, 0.0F);
	}
	
	public Point2F(final Vector2F v) {
		this(v.getElement1(), v.getElement2());
	}
	
	public Point2F(final float element1, final float element2) {
		this.element1 = element1;
		this.element2 = element2;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public String toString() {
		return String.format("new Point2F(%+.10f, %+.10f)", Float.valueOf(this.element1), Float.valueOf(this.element2));
	}
	
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
	
	public float getElement1() {
		return this.element1;
	}
	
	public float getElement2() {
		return this.element2;
	}
	
	public float getU() {
		return this.element1;
	}
	
	public float getV() {
		return this.element2;
	}
	
	public float getX() {
		return this.element1;
	}
	
	public float getY() {
		return this.element2;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.element1), Float.valueOf(this.element2));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Point2F sampleConcentricDisk() {
		return sampleConcentricDisk(random(), random());
	}
	
	public static Point2F sampleConcentricDisk(final float u, final float v) {
		return sampleConcentricDisk(u, v, 1.0F);
	}
	
	public static Point2F sampleConcentricDisk(final float u, final float v, final float radius) {
		if(equal(u, 0.0F) && equal(v, 0.0F)) {
			return new Point2F();
		}
		
		final float a = u * 2.0F - 1.0F;
		final float b = v * 2.0F - 1.0F;
		
		if(a * a > b * b) {
			final float phi = PI_DIVIDED_BY_FOUR * (b / a);
			final float r = radius * a;
			
			final float element1 = cos(phi) * r;
			final float element2 = sin(phi) * r;
			
			return new Point2F(element1, element2);
		}
		
		final float phi = PI_DIVIDED_BY_TWO - (PI_DIVIDED_BY_FOUR * (a / b));
		final float r = radius * b;
		
		final float element1 = cos(phi) * r;
		final float element2 = sin(phi) * r;
		
		return new Point2F(element1, element2);
	}
	
	public static Point2F sampleDiskUniformDistribution() {
		return sampleDiskUniformDistribution(random(), random());
	}
	
	public static Point2F sampleDiskUniformDistribution(final float u, final float v) {
		final float phi = PI_MULTIPLIED_BY_TWO * v;
		final float r = sqrt(u);
		
		final float element1 = cos(phi) * r;
		final float element2 = sin(phi) * r;
		
		return new Point2F(element1, element2);
	}
	
	public static float distance(final Point2F pEye, final Point2F pLookAt) {
		return Vector2F.direction(pEye, pLookAt).length();
	}
	
	public static float distanceSquared(final Point2F pEye, final Point2F pLookAt) {
		return Vector2F.direction(pEye, pLookAt).lengthSquared();
	}
}