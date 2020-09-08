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

import org.dayflower.image.Color3F;

//TODO: Add Javadocs!
public final class AshikhminShirleyMaterial implements Material {
	private final BXDF selectedBXDF;
	private final float selectedBXDFWeight;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public AshikhminShirleyMaterial() {
		this(new AshikhminShirleyBRDF());
	}
	
//	TODO: Add Javadocs!
	public AshikhminShirleyMaterial(final AshikhminShirleyBRDF ashikhminShirleyBRDF) {
		this.selectedBXDF = Objects.requireNonNull(ashikhminShirleyBRDF, "ashikhminShirleyBRDF == null");
		this.selectedBXDFWeight = 1.0F;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public Color3F emittance(final Intersection intersection) {
		return intersection.getPrimitive().getTextureEmittance().getColor(intersection);
	}
	
//	TODO: Add Javadocs!
	@Override
	public MaterialResult evaluate(final Intersection intersection) {
		return new MaterialResult(intersection.getPrimitive().getTextureAlbedo().getColor(intersection), this.selectedBXDF, this.selectedBXDFWeight);
	}
	
//	TODO: Add Javadocs!
	@Override
	public String toString() {
		return String.format("new AshikhminShirleyMaterial(%s)", this.selectedBXDF);
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof AshikhminShirleyMaterial)) {
			return false;
		} else if(!Objects.equals(this.selectedBXDF, AshikhminShirleyMaterial.class.cast(object).selectedBXDF)) {
			return false;
		} else if(!equal(this.selectedBXDFWeight, AshikhminShirleyMaterial.class.cast(object).selectedBXDFWeight)) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return Objects.hash(this.selectedBXDF, Float.valueOf(this.selectedBXDFWeight));
	}
}