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
package org.dayflower.scene;

import static org.dayflower.util.Floats.equal;

import java.lang.reflect.Field;
import java.util.Objects;

import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;

//TODO: Add Javadocs!
public final class LightIncomingRadianceResult {
	private final Color3F result;
	private final Point3F point;
	private final Vector3F incoming;
	private final float probabilityDensityFunctionValue;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public LightIncomingRadianceResult(final Color3F result, final Point3F point, final Vector3F incoming, final float probabilityDensityFunctionValue) {
		this.result = Objects.requireNonNull(result, "result == null");
		this.point = Objects.requireNonNull(point, "point == null");
		this.incoming = Objects.requireNonNull(incoming, "incoming == null");
		this.probabilityDensityFunctionValue = probabilityDensityFunctionValue;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Color3F getResult() {
		return this.result;
	}
	
//	TODO: Add Javadocs!
	public Point3F getPoint() {
		return this.point;
	}
	
//	TODO: Add Javadocs!
	@Override
	public String toString() {
		return String.format("new LightIncomingRadianceResult(%s, %s, %s, %+.10f)", this.result, this.point, this.incoming, Float.valueOf(this.probabilityDensityFunctionValue));
	}
	
//	TODO: Add Javadocs!
	public Vector3F getIncoming() {
		return this.incoming;
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof LightIncomingRadianceResult)) {
			return false;
		} else if(!Objects.equals(this.result, LightIncomingRadianceResult.class.cast(object).result)) {
			return false;
		} else if(!Objects.equals(this.point, LightIncomingRadianceResult.class.cast(object).point)) {
			return false;
		} else if(!Objects.equals(this.incoming, LightIncomingRadianceResult.class.cast(object).incoming)) {
			return false;
		} else if(!equal(this.probabilityDensityFunctionValue, LightIncomingRadianceResult.class.cast(object).probabilityDensityFunctionValue)) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
	public float getProbabilityDensityFunctionValue() {
		return this.probabilityDensityFunctionValue;
	}
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return Objects.hash(this.result, this.point, this.incoming, Float.valueOf(this.probabilityDensityFunctionValue));
	}
}