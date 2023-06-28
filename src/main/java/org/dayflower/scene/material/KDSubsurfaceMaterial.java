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

import static org.dayflower.utility.Floats.MAX_VALUE;

import java.lang.reflect.Field;//TODO: Add Javadocs!
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dayflower.scene.BSDF;
import org.dayflower.scene.BSSRDF;
import org.dayflower.scene.BXDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.ScatteringFunctions;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bssrdf.BSSRDFTable;
import org.dayflower.scene.bssrdf.TabulatedBSSRDF;
import org.dayflower.scene.bxdf.FresnelSpecularBXDF;
import org.dayflower.scene.bxdf.SpecularBRDF;
import org.dayflower.scene.bxdf.SpecularBTDF;
import org.dayflower.scene.bxdf.TorranceSparrowBRDF;
import org.dayflower.scene.bxdf.TorranceSparrowBTDF;
import org.dayflower.scene.fresnel.DielectricFresnel;
import org.dayflower.scene.fresnel.Fresnel;
import org.dayflower.scene.microfacet.MicrofacetDistribution;
import org.dayflower.scene.microfacet.TrowbridgeReitzMicrofacetDistribution;
import org.dayflower.scene.modifier.Modifier;
import org.dayflower.scene.modifier.NoOpModifier;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

import org.macroing.art4j.color.Color3F;

//TODO: Add Javadocs!
public final class KDSubsurfaceMaterial implements Material {
	/**
	 * The name of this {@code KDSubsurfaceMaterial} class.
	 */
	public static final String NAME = "KD Subsurface";
	
	/**
	 * The ID of this {@code KDSubsurfaceMaterial} class.
	 */
	public static final int ID = 9;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final BSSRDFTable bSSRDFTable;
	private final Modifier modifier;
	private final Texture textureEmission;
	private final Texture textureKD;
	private final Texture textureKR;
	private final Texture textureKT;
	private final Texture textureMFP;
	private final Texture textureRoughnessU;
	private final Texture textureRoughnessV;
	private final boolean isRemappingRoughness;
	private final float eta;
	private final float g;
	private final float scale;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public KDSubsurfaceMaterial() {
		this(1.0F);
	}
	
//	TODO: Add Javadocs!
	public KDSubsurfaceMaterial(final float scale) {
		this(scale, ConstantTexture.GRAY_0_50);
	}
	
//	TODO: Add Javadocs!
	public KDSubsurfaceMaterial(final float scale, final Texture textureKD) {
		this(scale, textureKD, ConstantTexture.WHITE);
	}
	
//	TODO: Add Javadocs!
	public KDSubsurfaceMaterial(final float scale, final Texture textureKD, final Texture textureKR) {
		this(scale, textureKD, textureKR, ConstantTexture.WHITE);
	}
	
//	TODO: Add Javadocs!
	public KDSubsurfaceMaterial(final float scale, final Texture textureKD, final Texture textureKR, final Texture textureKT) {
		this(scale, textureKD, textureKR, textureKT, ConstantTexture.BLACK);
	}
	
//	TODO: Add Javadocs!
	public KDSubsurfaceMaterial(final float scale, final Texture textureKD, final Texture textureKR, final Texture textureKT, final Texture textureEmission) {
		this(scale, textureKD, textureKR, textureKT, textureEmission, ConstantTexture.WHITE);
	}
	
//	TODO: Add Javadocs!
	public KDSubsurfaceMaterial(final float scale, final Texture textureKD, final Texture textureKR, final Texture textureKT, final Texture textureEmission, final Texture textureMFP) {
		this(scale, textureKD, textureKR, textureKT, textureEmission, textureMFP, 0.0F);
	}
	
//	TODO: Add Javadocs!
	public KDSubsurfaceMaterial(final float scale, final Texture textureKD, final Texture textureKR, final Texture textureKT, final Texture textureEmission, final Texture textureMFP, final float g) {
		this(scale, textureKD, textureKR, textureKT, textureEmission, textureMFP, g, 1.33F);
	}
	
//	TODO: Add Javadocs!
	public KDSubsurfaceMaterial(final float scale, final Texture textureKD, final Texture textureKR, final Texture textureKT, final Texture textureEmission, final Texture textureMFP, final float g, final float eta) {
		this(scale, textureKD, textureKR, textureKT, textureEmission, textureMFP, g, eta, ConstantTexture.BLACK);
	}
	
//	TODO: Add Javadocs!
	public KDSubsurfaceMaterial(final float scale, final Texture textureKD, final Texture textureKR, final Texture textureKT, final Texture textureEmission, final Texture textureMFP, final float g, final float eta, final Texture textureRoughness) {
		this(scale, textureKD, textureKR, textureKT, textureEmission, textureMFP, g, eta, textureRoughness, textureRoughness);
	}
	
//	TODO: Add Javadocs!
	public KDSubsurfaceMaterial(final float scale, final Texture textureKD, final Texture textureKR, final Texture textureKT, final Texture textureEmission, final Texture textureMFP, final float g, final float eta, final Texture textureRoughnessU, final Texture textureRoughnessV) {
		this(scale, textureKD, textureKR, textureKT, textureEmission, textureMFP, g, eta, textureRoughnessU, textureRoughnessV, true);
	}
	
//	TODO: Add Javadocs!
	public KDSubsurfaceMaterial(final float scale, final Texture textureKD, final Texture textureKR, final Texture textureKT, final Texture textureEmission, final Texture textureMFP, final float g, final float eta, final Texture textureRoughnessU, final Texture textureRoughnessV, final boolean isRemappingRoughness) {
		this(scale, textureKD, textureKR, textureKT, textureEmission, textureMFP, g, eta, textureRoughnessU, textureRoughnessV, isRemappingRoughness, new NoOpModifier());
	}
	
//	TODO: Add Javadocs!
	public KDSubsurfaceMaterial(final float scale, final Texture textureKD, final Texture textureKR, final Texture textureKT, final Texture textureEmission, final Texture textureMFP, final float g, final float eta, final Texture textureRoughnessU, final Texture textureRoughnessV, final boolean isRemappingRoughness, final Modifier modifier) {
		this.scale = scale;
		this.textureKD = Objects.requireNonNull(textureKD, "textureKD == null");
		this.textureKR = Objects.requireNonNull(textureKR, "textureKR == null");
		this.textureKT = Objects.requireNonNull(textureKT, "textureKT == null");
		this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
		this.textureMFP = Objects.requireNonNull(textureMFP, "textureMFP == null");
		this.g = g;
		this.eta = eta;
		this.textureRoughnessU = Objects.requireNonNull(textureRoughnessU, "textureRoughnessU == null");
		this.textureRoughnessV = Objects.requireNonNull(textureRoughnessV, "textureRoughnessV == null");
		this.isRemappingRoughness = isRemappingRoughness;
		this.modifier = Objects.requireNonNull(modifier, "modifier == null");
		this.bSSRDFTable = new BSSRDFTable(100, 64);
		this.bSSRDFTable.computeBeamDiffusionBSSRDF(g, eta);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public Color3F emittance(final Intersection intersection) {
		Objects.requireNonNull(intersection, "intersection == null");
		
		return this.textureEmission.getColor(intersection);
	}
	
//	TODO: Add Javadocs!
	@Override
	public ScatteringFunctions computeScatteringFunctions(final Intersection intersection, final TransportMode transportMode, final boolean isAllowingMultipleLobes) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(transportMode, "transportMode == null");
		
		this.modifier.modify(intersection);
		
		final Color3F colorKR = Color3F.saturate(this.textureKR.getColor(intersection), 0.0F, MAX_VALUE);
		final Color3F colorKT = Color3F.saturate(this.textureKT.getColor(intersection), 0.0F, MAX_VALUE);
		
		if(colorKR.isBlack() && colorKT.isBlack()) {
			return new ScatteringFunctions();
		}
		
		final float roughnessUComputed = this.textureRoughnessU.getFloat(intersection);
		final float roughnessVComputed = this.textureRoughnessV.getFloat(intersection);
		
		final boolean isSpecular = roughnessUComputed == 0.0F && roughnessVComputed == 0.0F;
		
		final List<BXDF> bXDFs = new ArrayList<>();
		
		if(isSpecular && isAllowingMultipleLobes) {
			bXDFs.add(new FresnelSpecularBXDF(colorKR, colorKT, transportMode, 1.0F, this.eta));
		} else {
			final float roughnessU = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(roughnessUComputed) : roughnessUComputed;
			final float roughnessV = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(roughnessVComputed) : roughnessVComputed;
			
			final MicrofacetDistribution microfacetDistribution = isSpecular ? null : new TrowbridgeReitzMicrofacetDistribution(true, false, roughnessU, roughnessV);
			
			if(!colorKR.isBlack()) {
				final Fresnel fresnel = new DielectricFresnel(1.0F, this.eta);
				
				if(isSpecular) {
					bXDFs.add(new SpecularBRDF(colorKR, fresnel));
				} else {
					bXDFs.add(new TorranceSparrowBRDF(colorKR, fresnel, microfacetDistribution));
				}
			}
			
			if(!colorKT.isBlack()) {
				if(isSpecular) {
					bXDFs.add(new SpecularBTDF(colorKT, transportMode, 1.0F, this.eta));
				} else {
					bXDFs.add(new TorranceSparrowBTDF(colorKT, microfacetDistribution, transportMode, 1.0F, this.eta));
				}
			}
		}
		
		final Color3F colorMFree = Color3F.multiply(Color3F.saturate(this.textureMFP.getColor(intersection), 0.0F, MAX_VALUE), this.scale);
		final Color3F colorKD = Color3F.saturate(this.textureKD.getColor(intersection), 0.0F, MAX_VALUE);
		
		final Color3F[] sigma = this.bSSRDFTable.computeSubsurfaceFromDiffuse(colorKD, colorMFree);
		
		final Color3F sigmaA = sigma[0];
		final Color3F sigmaS = sigma[1];
		
		final BSDF bSDF = new BSDF(intersection, bXDFs, false, this.eta);
		
		final BSSRDF bSSRDF = new TabulatedBSSRDF(intersection, this.eta, this, transportMode, sigmaA, sigmaS, this.bSSRDFTable);
		
		return new ScatteringFunctions(bSDF, bSSRDF);
	}
	
//	TODO: Add Javadocs!
	@Override
	public String getName() {
		return NAME;
	}
	
//	TODO: Add Javadocs!
	@Override
	public int getID() {
		return ID;
	}
}