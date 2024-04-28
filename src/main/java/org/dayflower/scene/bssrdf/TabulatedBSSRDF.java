/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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

import org.dayflower.color.Color3F;
import org.dayflower.interpolation.Interpolation;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.TransportMode;

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
	public TabulatedBSSRDF(final Intersection intersection, final float eta, final TransportMode transportMode, final Color3F sigmaA, final Color3F sigmaS, final BSSRDFTable bSSRDFTable) {
		super(intersection, eta, transportMode);
		
		this.bSSRDFTable = Objects.requireNonNull(bSSRDFTable, "bSSRDFTable == null");
		this.sigmaT = Color3F.add(sigmaA, sigmaS);
		this.rho = new Color3F(this.sigmaT.r != 0.0F ? sigmaS.r / this.sigmaT.r : 0.0F, this.sigmaT.g != 0.0F ? sigmaS.g / this.sigmaT.g : 0.0F, this.sigmaT.b != 0.0F ? sigmaS.b / this.sigmaT.b : 0.0F);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public Color3F evaluateSR(final float distance) {
		final float[] srResult = {0.0F, 0.0F, 0.0F};
		final float[] sigmaT = {this.sigmaT.r, this.sigmaT.g, this.sigmaT.b};
		final float[] rho = {this.rho.r, this.rho.g, this.rho.b};
		
		for(int i = 0; i < 3; i++) {
			final float rOptical = distance * sigmaT[i];
			
			final int[] rhoOffset = new int[1];
			final int[] radiusOffset = new int[1];
			
			final float[] rhoWeights = new float[4];
			final float[] radiusWeights = new float[4];
			
			if(!Interpolation.catmullRomWeights(this.bSSRDFTable.getRhoSamples().length, this.bSSRDFTable.getRhoSamples(), rho[i], rhoOffset, rhoWeights) || !Interpolation.catmullRomWeights(this.bSSRDFTable.getRadiusSamples().length, this.bSSRDFTable.getRadiusSamples(), rOptical, radiusOffset, radiusWeights)) {
				continue;
			}
			
			float sr = 0.0F;
			
			for(int j = 0; j < 4; j++) {
				for(int k = 0; k < 4; k++) {
					final float weight = rhoWeights[j] * radiusWeights[k];
					
					if(weight != 0.0F) {
						sr += weight * this.bSSRDFTable.evaluateProfile(rhoOffset[0] + j, radiusOffset[0] + k);
					}
				}
			}
			
			if(rOptical != 0.0F) {
				sr /= Floats.PI_MULTIPLIED_BY_2 * rOptical;
			}
			
			srResult[i] = sr;
		}
		
		for(int i = 0; i < 3; i++) {
			srResult[i] *= sigmaT[i] * sigmaT[i];
			srResult[i] = Floats.saturate(srResult[i], 0.0F, Floats.MAX_VALUE);
		}
		
		return new Color3F(srResult[0], srResult[1], srResult[2]);
	}
	
//	TODO: Add Javadocs!
	@Override
	public float evaluateProbabilityDensityFunctionSR(final int index, final float distance) {
		final float rOptical = distance * doGetComponentAt(this.sigmaT, index);
		
		final int[] rhoOffset = new int[1];
		final int[] radiusOffset = new int[1];
		
		final float[] rhoWeights = new float[4];
		final float[] radiusWeights = new float[4];
		
		if(!Interpolation.catmullRomWeights(this.bSSRDFTable.getRhoSamples().length, this.bSSRDFTable.getRhoSamples(), doGetComponentAt(this.rho, index), rhoOffset, rhoWeights) || !Interpolation.catmullRomWeights(this.bSSRDFTable.getRadiusSamples().length, this.bSSRDFTable.getRadiusSamples(), rOptical, radiusOffset, radiusWeights)) {
			return 0.0F;
		}
		
		float sr = 0.0F;
		float rhoEff = 0.0F;
		
		for(int i = 0; i < 4; i++) {
			if(rhoWeights[i] == 0.0F) {
				continue;
			}
			
			rhoEff += this.bSSRDFTable.getRhoEff()[rhoOffset[0] + i] * rhoWeights[i];
			
			for(int j = 0; j < 4; j++) {
				if(radiusWeights[j] == 0.0F) {
					continue;
				}
				
				sr += this.bSSRDFTable.evaluateProfile(rhoOffset[0] + i, radiusOffset[0] + j) * rhoWeights[i] * radiusWeights[j];
			}
		}
		
		if(rOptical != 0.0F) {
			sr /= Floats.PI_MULTIPLIED_BY_2 * rOptical;
		}
		
		return Floats.max(0.0F, sr * doGetComponentAt(this.sigmaT, index) * doGetComponentAt(this.sigmaT, index) / rhoEff);
	}
	
//	TODO: Add Javadocs!
	@Override
	public float sampleSR(final int index, final float u) {
		if(doGetComponentAt(this.sigmaT, index) == 0.0F) {
			return -1.0F;
		}
		
		return Interpolation.sampleCatmullRom2D(this.bSSRDFTable.getRhoSamples().length, this.bSSRDFTable.getRadiusSamples().length, this.bSSRDFTable.getRhoSamples(), this.bSSRDFTable.getRadiusSamples(), this.bSSRDFTable.getProfile(), this.bSSRDFTable.getProfileCDF(), doGetComponentAt(this.rho, index), u, new float[1], new float[1]) / doGetComponentAt(this.sigmaT, index);
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