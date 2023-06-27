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

import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.BSSRDFResult;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.Scene;
import org.dayflower.scene.TransportMode;

import org.macroing.art4j.color.Color3F;
import org.macroing.java.lang.Floats;

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
		final OrthonormalBasis33F orthonormalBasis = getIntersection().getOrthonormalBasisS();
		
		final Vector3F u = orthonormalBasis.u;
		final Vector3F v = orthonormalBasis.v;
		final Vector3F w = orthonormalBasis.w;
		
		final Vector3F d = Vector3F.direction(intersection.getSurfaceIntersectionPoint(), getIntersection().getSurfaceIntersectionPoint());
		final Vector3F dLocal = new Vector3F(Vector3F.dotProduct(u, d), Vector3F.dotProduct(v, d), Vector3F.dotProduct(w, d));
		
		final Vector3F n = intersection.getSurfaceNormalS();
		final Vector3F nLocal = new Vector3F(Vector3F.dotProduct(u, n), Vector3F.dotProduct(v, n), Vector3F.dotProduct(w, n));
		
		final float[] rProj = {
			Floats.sqrt(dLocal.y * dLocal.y + dLocal.z * dLocal.z),
			Floats.sqrt(dLocal.z * dLocal.z + dLocal.x * dLocal.x),
			Floats.sqrt(dLocal.x * dLocal.x + dLocal.y * dLocal.y)
		};
		
		final float probability = 1.0F / 3.0F;
		
		final float[] axisProbability = {0.25F, 0.25F, 0.5F};
		
		float probabilityDensityFunctionValue = 0.0F;
		
		for(int i = 0; i < 3; i++) {
			probabilityDensityFunctionValue += evaluateProbabilityDensityFunctionSR(0, rProj[i]) * Floats.abs(nLocal.getComponentAt(i)) * probability * axisProbability[i];
			probabilityDensityFunctionValue += evaluateProbabilityDensityFunctionSR(1, rProj[i]) * Floats.abs(nLocal.getComponentAt(i)) * probability * axisProbability[i];
			probabilityDensityFunctionValue += evaluateProbabilityDensityFunctionSR(2, rProj[i]) * Floats.abs(nLocal.getComponentAt(i)) * probability * axisProbability[i];
		}
		
		return probabilityDensityFunctionValue;
	}
	
//	TODO: Add Javadocs!
	@Override
	public float evaluateProbabilityDensityFunctionSR(final int index, final float distance) {
		return 0.0F;
	}
	
//	TODO: Add Javadocs!
	@Override
	public float sampleSR(final int index, final float u) {
		if(doGetComponentAt(this.sigmaT, index) == 0.0F) {
			return -1.0F;
		}
		
		return Utilities.sampleCatmullRom2D(this.bSSRDFTable.getRhoSamples().length, this.bSSRDFTable.getRadiusSamples().length, this.bSSRDFTable.getRhoSamples(), this.bSSRDFTable.getRadiusSamples(), this.bSSRDFTable.getProfile(), this.bSSRDFTable.getProfileCDF(), doGetComponentAt(this.rho, index), u, new float[1], new float[1]) / doGetComponentAt(this.sigmaT, index);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float doGetComponentAt(final Color3F color, final int index) {
		switch(index) {
			case 0:
				return color.r;
			case 1:
				return color.g;
			case 2:
				return color.b;
			default:
				throw new IllegalArgumentException();
		}
	}
}