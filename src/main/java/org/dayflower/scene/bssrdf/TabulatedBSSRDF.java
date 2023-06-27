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
package org.dayflower.scene.bssrdf;

import java.lang.reflect.Field;//TODO: Add Javadocs!
import java.util.Objects;

import org.dayflower.geometry.Point2F;
import org.dayflower.scene.BSSRDFResult;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.Scene;
import org.dayflower.scene.TransportMode;

import org.macroing.art4j.color.Color3F;

/**
 * A {@code TabulatedBSSRDF} represents a tabulated BSSRDF (Bidirectional Scattering Surface Reflectance Distribution Function).
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class TabulatedBSSRDF extends SeparableBSSRDF {
	private final BSSRDFTable bSSRDFTable;
	private final Color3F rho;
	private final Color3F sigmaT;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public TabulatedBSSRDF(final Intersection intersection, final float eta, final Material material, final TransportMode transportMode, final Color3F sigmaA, final Color3F sigmaS, final BSSRDFTable bSSRDFTable) {
		super(intersection, eta, material, transportMode);
		
		this.bSSRDFTable = Objects.requireNonNull(bSSRDFTable, "bSSRDFTable == null");
		this.sigmaT = Color3F.add(sigmaA, sigmaS);
		this.rho = new Color3F(this.sigmaT.r != 0.0F ? sigmaS.r / this.sigmaT.r : 0.0F, this.sigmaT.g != 0.0F ? sigmaS.g / this.sigmaT.g : 0.0F, this.sigmaT.b != 0.0F ? sigmaS.b / this.sigmaT.b : 0.0F);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public BSSRDFResult sampleS(final Scene scene, final float u1, final Point2F u2) {
		return null;
	}
	
//	TODO: Add Javadocs!
	@Override
	public Color3F evaluateSR(final float distance) {
		return Color3F.BLACK;
	}
	
//	TODO: Add Javadocs!
	@Override
	public SeparableBSSRDFResult sampleSP(final Scene scene, final float u1, final Point2F u2) {
		return null;
	}
	
//	TODO: Add Javadocs!
	@Override
	public float evaluateProbabilityDensityFunctionSP(final Intersection intersection) {
		return 0.0F;
	}
	
//	TODO: Add Javadocs!
	@Override
	public float evaluateProbabilityDensityFunctionSR(final int index, final float distance) {
		return 0.0F;
	}
	
//	TODO: Add Javadocs!
	@Override
	public float sampleSR(final int index, final float u) {
		return 0.0F;
	}
}