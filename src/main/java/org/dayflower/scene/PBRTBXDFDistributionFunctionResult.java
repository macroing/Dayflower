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

import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;

//TODO: Add Javadocs!
public final class PBRTBXDFDistributionFunctionResult {
	private final Color3F value;
	private final Vector3F incoming;
	private final Vector3F outgoing;
	private final float probabilityDensityFunctionValue;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public PBRTBXDFDistributionFunctionResult(final Vector3F incoming, final Vector3F outgoing) {
		this(incoming, outgoing, Color3F.BLACK);
	}
	
//	TODO: Add Javadocs!
	public PBRTBXDFDistributionFunctionResult(final Vector3F incoming, final Vector3F outgoing, final Color3F value) {
		this(incoming, outgoing, value, 0.0F);
	}
	
//	TODO: Add Javadocs!
	public PBRTBXDFDistributionFunctionResult(final Vector3F incoming, final Vector3F outgoing, final Color3F value, final float probabilityDensityFunctionValue) {
		this.incoming = Objects.requireNonNull(incoming, "incoming == null");
		this.outgoing = Objects.requireNonNull(outgoing, "outgoing == null");
		this.value = Objects.requireNonNull(value, "value == null");
		this.probabilityDensityFunctionValue = probabilityDensityFunctionValue;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Color3F getValue() {
		return this.value;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code PBRTBXDFDistributionFunctionResult} instance.
	 * 
	 * @return a {@code String} representation of this {@code PBRTBXDFDistributionFunctionResult} instance
	 */
	@Override
	public String toString() {
		return String.format("new PBRTBXDFDistributionFunctionResult(%s, %s, %s, %+.10f)", this.incoming, this.outgoing, this.value, Float.valueOf(this.probabilityDensityFunctionValue));
	}
	
//	TODO: Add Javadocs!
	public Vector3F getIncoming() {
		return this.incoming;
	}
	
//	TODO: Add Javadocs!
	public Vector3F getOutgoing() {
		return this.outgoing;
	}
	
	/**
	 * Compares {@code object} to this {@code PBRTBXDFDistributionFunctionResult} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code PBRTBXDFDistributionFunctionResult}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code PBRTBXDFDistributionFunctionResult} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code PBRTBXDFDistributionFunctionResult}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof PBRTBXDFDistributionFunctionResult)) {
			return false;
		} else if(!Objects.equals(this.incoming, PBRTBXDFDistributionFunctionResult.class.cast(object).incoming)) {
			return false;
		} else if(!Objects.equals(this.outgoing, PBRTBXDFDistributionFunctionResult.class.cast(object).outgoing)) {
			return false;
		} else if(!Objects.equals(this.value, PBRTBXDFDistributionFunctionResult.class.cast(object).value)) {
			return false;
		} else if(!equal(this.probabilityDensityFunctionValue, PBRTBXDFDistributionFunctionResult.class.cast(object).probabilityDensityFunctionValue)) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
	public float getProbabilityDensityFunctionValue() {
		return this.probabilityDensityFunctionValue;
	}
	
	/**
	 * Returns a hash code for this {@code PBRTBXDFDistributionFunctionResult} instance.
	 * 
	 * @return a hash code for this {@code PBRTBXDFDistributionFunctionResult} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.incoming, this.outgoing, this.value, Float.valueOf(this.probabilityDensityFunctionValue));
	}
}