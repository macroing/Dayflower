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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dayflower.geometry.Point2F;
import org.dayflower.util.Lists;

//TODO: Add Javadocs!
public final class PBRTBXDFReflectanceFunctionResult {
	private final List<Point2F> samplesA;
	private final List<Point2F> samplesB;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public PBRTBXDFReflectanceFunctionResult(final List<Point2F> samplesA) {
		this(samplesA, new ArrayList<>());
	}
	
//	TODO: Add Javadocs!
	public PBRTBXDFReflectanceFunctionResult(final List<Point2F> samplesA, final List<Point2F> samplesB) {
		this.samplesA = Lists.requireNonNullList(samplesA, "samplesA");
		this.samplesB = Lists.requireNonNullList(samplesB, "samplesB");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public List<Point2F> getSamplesA() {
		return new ArrayList<>(this.samplesA);
	}
	
//	TODO: Add Javadocs!
	public List<Point2F> getSamplesB() {
		return new ArrayList<>(this.samplesB);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code PBRTBXDFReflectanceFunctionResult} instance.
	 * 
	 * @return a {@code String} representation of this {@code PBRTBXDFReflectanceFunctionResult} instance
	 */
	@Override
	public String toString() {
		return "new PBRTBXDFReflectanceFunctionResult(..., ...)";
	}
	
	/**
	 * Compares {@code object} to this {@code PBRTBXDFReflectanceFunctionResult} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code PBRTBXDFReflectanceFunctionResult}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code PBRTBXDFReflectanceFunctionResult} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code PBRTBXDFReflectanceFunctionResult}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof PBRTBXDFReflectanceFunctionResult)) {
			return false;
		} else if(!Objects.equals(this.samplesA, PBRTBXDFReflectanceFunctionResult.class.cast(object).samplesA)) {
			return false;
		} else if(!Objects.equals(this.samplesB, PBRTBXDFReflectanceFunctionResult.class.cast(object).samplesB)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code PBRTBXDFReflectanceFunctionResult} instance.
	 * 
	 * @return a hash code for this {@code PBRTBXDFReflectanceFunctionResult} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.samplesA, this.samplesB);
	}
}