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
package org.dayflower.scene.material.smallpt;

import java.lang.reflect.Field;
import java.util.Objects;

import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;

//TODO: Add Javadocs!
public final class SmallPTSample {
	private final Color3F result;
	private final Vector3F direction;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public SmallPTSample(final Color3F result, final Vector3F direction) {
		this.result = Objects.requireNonNull(result, "result == null");
		this.direction = Objects.requireNonNull(direction, "direction == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Color3F getResult() {
		return this.result;
	}
	
//	TODO: Add Javadocs!
	@Override
	public String toString() {
		return String.format("new SmallPTSample(%s, %s)", this.result, this.direction);
	}
	
//	TODO: Add Javadocs!
	public Vector3F getDirection() {
		return this.direction;
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SmallPTSample)) {
			return false;
		} else if(!Objects.equals(this.result, SmallPTSample.class.cast(object).result)) {
			return false;
		} else if(!Objects.equals(this.direction, SmallPTSample.class.cast(object).direction)) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return Objects.hash(this.result, this.direction);
	}
}