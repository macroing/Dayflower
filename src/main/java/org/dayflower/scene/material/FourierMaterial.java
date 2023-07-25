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
package org.dayflower.scene.material;

import java.lang.reflect.Field;//TODO: Add Javadoc!
import java.util.Objects;

import org.dayflower.scene.BSDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.ScatteringFunctions;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.FourierBXDF;
import org.dayflower.scene.bxdf.FourierBXDFTable;
import org.dayflower.scene.modifier.Modifier;
import org.dayflower.scene.texture.Texture;
import org.macroing.art4j.color.Color3F;

//TODO: Add Javadoc!
public final class FourierMaterial implements Material {
	/**
	 * The name of this {@code FourierMaterial} class.
	 */
	public static final String NAME = "Fourier";
	
	/**
	 * The ID of this {@code FourierMaterial} class.
	 */
	public static final int ID = 5;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final FourierBXDFTable fourierBXDFTable;
	private final Modifier modifier;
	private final String filename;
	private final Texture textureEmission;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadoc!
	public FourierMaterial(final String filename, final Texture textureEmission, final Modifier modifier) {
		this.fourierBXDFTable = new FourierBXDFTable();
		this.fourierBXDFTable.read(Objects.requireNonNull(filename, "filename == null"));
		this.filename = filename;
		this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
		this.modifier = Objects.requireNonNull(modifier, "modifier == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadoc!
	@Override
	public Color3F emittance(final Intersection intersection) {
		Objects.requireNonNull(intersection, "intersection == null");
		
		return this.textureEmission.getColor(intersection);
	}
	
//	TODO: Add Javadoc!
	@Override
	public ScatteringFunctions computeScatteringFunctions(final Intersection intersection, final TransportMode transportMode, final boolean isAllowingMultipleLobes) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(transportMode, "transportMode == null");
		
		this.modifier.modify(intersection);
		
		if(this.fourierBXDFTable.getNChannels() > 0) {
			return new ScatteringFunctions(new BSDF(intersection, new FourierBXDF(this.fourierBXDFTable, transportMode)));
		}
		
		return new ScatteringFunctions();
	}
	
//	TODO: Add Javadoc!
	@Override
	public String getName() {
		return NAME;
	}
	
//	TODO: Add Javadoc!
	@Override
	public int getID() {
		return ID;
	}
}