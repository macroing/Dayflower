/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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

import java.lang.reflect.Field;//TODO: Add Javadocs!
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Vector3F;
import org.dayflower.utility.Floats;
import org.macroing.art4j.color.Color3F;

//TODO: Add Javadocs!
public final class BSSRDFResult {
	private final BSDF bSDF;
	private final Color3F result;
	private final Intersection intersection;
	private final Vector3F outgoing;
	private final float probabilityDensityFunctionValue;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public BSSRDFResult(final Color3F result, final Intersection intersection, final float probabilityDensityFunctionValue) {
		this.result = Objects.requireNonNull(result, "result == null");
		this.intersection = intersection;
		this.probabilityDensityFunctionValue = probabilityDensityFunctionValue;
		this.bSDF = null;
		this.outgoing = null;
	}
	
//	TODO: Add Javadocs!
	public BSSRDFResult(final Color3F result, final Intersection intersection, final float probabilityDensityFunctionValue, final BSDF bSDF, final Vector3F outgoing) {
		this.result = Objects.requireNonNull(result, "result == null");
		this.intersection = Objects.requireNonNull(intersection, "intersection == null");
		this.probabilityDensityFunctionValue = probabilityDensityFunctionValue;
		this.bSDF = Objects.requireNonNull(bSDF, "bSDF == null");
		this.outgoing = Objects.requireNonNull(outgoing, "outgoing == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Color3F getResult() {
		return this.result;
	}
	
//	TODO: Add Javadocs!
	public Intersection getIntersection() {
		return this.intersection;
	}
	
//	TODO: Add Javadocs!
	public Optional<BSDF> getBSDF() {
		return Optional.ofNullable(this.bSDF);
	}
	
//	TODO: Add Javadocs!
	public Optional<Vector3F> getOutgoing() {
		return Optional.ofNullable(this.outgoing);
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof BSSRDFResult)) {
			return false;
		} else if(!Objects.equals(this.bSDF, BSSRDFResult.class.cast(object).bSDF)) {
			return false;
		} else if(!Objects.equals(this.result, BSSRDFResult.class.cast(object).result)) {
			return false;
		} else if(!Objects.equals(this.intersection, BSSRDFResult.class.cast(object).intersection)) {
			return false;
		} else if(!Objects.equals(this.outgoing, BSSRDFResult.class.cast(object).outgoing)) {
			return false;
		} else if(!Floats.equal(this.probabilityDensityFunctionValue, BSSRDFResult.class.cast(object).probabilityDensityFunctionValue)) {
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
		return Objects.hash(this.bSDF, this.result, this.intersection, this.outgoing, Float.valueOf(this.probabilityDensityFunctionValue));
	}
}