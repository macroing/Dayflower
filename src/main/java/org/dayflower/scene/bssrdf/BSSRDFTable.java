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

import org.macroing.art4j.color.Color3F;
import org.macroing.java.lang.Floats;

//TODO: Add Javadocs!
public final class BSSRDFTable {
	private final float[] profile;
	private final float[] profileCDF;
	private final float[] radiusSamples;
	private final float[] rhoEff;
	private final float[] rhoSamples;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public BSSRDFTable(final int rhoSamples, final int radiusSamples) {
		this.profile = new float[radiusSamples * rhoSamples];
		this.profileCDF = new float[radiusSamples * rhoSamples];
		this.radiusSamples = new float[radiusSamples];
		this.rhoEff = new float[rhoSamples];
		this.rhoSamples = new float[rhoSamples];
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Color3F[] computeSubsurfaceFromDiffuse(final Color3F rhoEff, final Color3F mfp) {
		final float[] sigmaA = {0.0F, 0.0F, 0.0F};
		final float[] sigmaS = {0.0F, 0.0F, 0.0F};
		
		for(int i = 0; i < 3; i++) {
			final float rho = Utilities.invertCatmullRom(this.rhoSamples.length, this.rhoSamples, this.rhoEff, doGetComponentAt(rhoEff, i));
			
			sigmaS[i] = rho / doGetComponentAt(mfp, i);
			sigmaA[i] = (1.0F - rho) / doGetComponentAt(mfp, i);
		}
		
		return new Color3F[] {
			new Color3F(sigmaA[0], sigmaA[1], sigmaA[2]),
			new Color3F(sigmaS[0], sigmaS[1], sigmaS[2])
		};
	}
	
//	TODO: Add Javadocs!
	public float evaluateProfile(final int rhoIndex, final int radiusIndex) {
		return this.profile[rhoIndex * this.radiusSamples.length + radiusIndex];
	}
	
//	TODO: Add Javadocs!
	public float[] getProfile() {
		return this.profile;
	}
	
//	TODO: Add Javadocs!
	public float[] getProfileCDF() {
		return this.profileCDF;
	}
	
//	TODO: Add Javadocs!
	public float[] getRadiusSamples() {
		return this.radiusSamples;
	}
	
//	TODO: Add Javadocs!
	public float[] getRhoEff() {
		return this.rhoEff;
	}
	
//	TODO: Add Javadocs!
	public float[] getRhoSamples() {
		return this.rhoSamples;
	}
	
//	TODO: Add Javadocs!
	public void computeBeamDiffusionBSSRDF(final float g, final float eta) {
		this.radiusSamples[0] = 0.0F;
		this.radiusSamples[1] = 2.5e-3F;
		
		for(int i = 2; i < this.radiusSamples.length; i++) {
			this.radiusSamples[i] = this.radiusSamples[i - 1] * 1.2F;
		}
		
		for(int i = 0; i < this.rhoSamples.length; i++) {
			this.rhoSamples[i] = (1.0F - Floats.exp(-8.0F * i / (this.rhoSamples.length - 1))) / (1.0F - Floats.exp(-8.0F));
		}
		
		for(int i = 0; i < this.rhoSamples.length; i++) {
			for(int j = 0; j < this.radiusSamples.length; j++) {
				final float rho = this.rhoSamples[i];
				final float radius = this.radiusSamples[j];
				
				this.profile[i * this.radiusSamples.length + j] = Floats.PI_MULTIPLIED_BY_2 * radius * (Utilities.computeBeamDiffusionSS(rho, 1.0F - rho, g, eta, radius) + Utilities.computeBeamDiffusionMS(rho, 1.0F - rho, g, eta, radius));
			}
			
			this.rhoEff[i] = Utilities.integrateCatmullRom(this.radiusSamples.length, this.radiusSamples, this.profile, this.profileCDF, i * this.radiusSamples.length, i * this.radiusSamples.length);
		}
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