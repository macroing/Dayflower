/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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
package org.dayflower.scene.material.pbrt;

import static org.dayflower.utility.Floats.isZero;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.scene.BSDF;
import org.dayflower.scene.BSSRDF;
import org.dayflower.scene.BXDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.pbrt.PBRTBSDF;
import org.dayflower.scene.bxdf.pbrt.FresnelSpecularPBRTBXDF;
import org.dayflower.scene.bxdf.pbrt.SpecularPBRTBRDF;
import org.dayflower.scene.bxdf.pbrt.SpecularPBRTBTDF;
import org.dayflower.scene.bxdf.pbrt.TorranceSparrowPBRTBRDF;
import org.dayflower.scene.bxdf.pbrt.TorranceSparrowPBRTBTDF;
import org.dayflower.scene.fresnel.DielectricFresnel;
import org.dayflower.scene.fresnel.Fresnel;
import org.dayflower.scene.microfacet.MicrofacetDistribution;
import org.dayflower.scene.microfacet.TrowbridgeReitzMicrofacetDistribution;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

/**
 * A {@code GlassPBRTMaterial} is an implementation of {@link PBRTMaterial} that represents glass.
 * <p>
 * This class is immutable and thread-safe as long as all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class GlassPBRTMaterial implements PBRTMaterial {
	/**
	 * The name of this {@code GlassPBRTMaterial} class.
	 */
	public static final String NAME = "PBRT - Glass";
	
	/**
	 * The ID of this {@code GlassPBRTMaterial} class.
	 */
	public static final int ID = 101;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Texture textureEmission;
	private final Texture textureEta;
	private final Texture textureKR;
	private final Texture textureKT;
	private final Texture textureRoughnessU;
	private final Texture textureRoughnessV;
	private final boolean isRemappingRoughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code GlassPBRTMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassPBRTMaterial(Color3F.WHITE);
	 * }
	 * </pre>
	 */
	public GlassPBRTMaterial() {
		this(Color3F.WHITE);
	}
	
	/**
	 * Constructs a new {@code GlassPBRTMaterial} instance.
	 * <p>
	 * If {@code colorK} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassPBRTMaterial(colorK, colorK);
	 * }
	 * </pre>
	 * 
	 * @param colorK a {@link Color3F} instance for the reflection and transmission coefficients
	 * @throws NullPointerException thrown if, and only if, {@code colorK} is {@code null}
	 */
	public GlassPBRTMaterial(final Color3F colorK) {
		this(colorK, colorK);
	}
	
	/**
	 * Constructs a new {@code GlassPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKR} or {@code colorKT} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassPBRTMaterial(colorKR, colorKT, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorKR a {@link Color3F} instance for the reflection coefficient
	 * @param colorKT a {@code Color3F} instance for the transmission coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code colorKR} or {@code colorKT} are {@code null}
	 */
	public GlassPBRTMaterial(final Color3F colorKR, final Color3F colorKT) {
		this(colorKR, colorKT, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code GlassPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKR}, {@code colorKT} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassPBRTMaterial(colorKR, colorKT, colorEmission, 1.5F);
	 * }
	 * </pre>
	 * 
	 * @param colorKR a {@link Color3F} instance for the reflection coefficient
	 * @param colorKT a {@code Color3F} instance for the transmission coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code colorKR}, {@code colorKT} or {@code colorEmission} are {@code null}
	 */
	public GlassPBRTMaterial(final Color3F colorKR, final Color3F colorKT, final Color3F colorEmission) {
		this(colorKR, colorKT, colorEmission, 1.5F);
	}
	
	/**
	 * Constructs a new {@code GlassPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKR}, {@code colorKT} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassPBRTMaterial(colorKR, colorKT, colorEmission, floatEta, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @param colorKR a {@link Color3F} instance for the reflection coefficient
	 * @param colorKT a {@code Color3F} instance for the transmission coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @throws NullPointerException thrown if, and only if, either {@code colorKR}, {@code colorKT} or {@code colorEmission} are {@code null}
	 */
	public GlassPBRTMaterial(final Color3F colorKR, final Color3F colorKT, final Color3F colorEmission, final float floatEta) {
		this(colorKR, colorKT, colorEmission, floatEta, 0.0F);
	}
	
	/**
	 * Constructs a new {@code GlassPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKR}, {@code colorKT} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassPBRTMaterial(colorKR, colorKT, colorEmission, floatEta, floatRoughness, floatRoughness);
	 * }
	 * </pre>
	 * 
	 * @param colorKR a {@link Color3F} instance for the reflection coefficient
	 * @param colorKT a {@code Color3F} instance for the transmission coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @param floatRoughness a {@code float} for the roughness along the U-axis and the V-axis
	 * @throws NullPointerException thrown if, and only if, either {@code colorKR}, {@code colorKT} or {@code colorEmission} are {@code null}
	 */
	public GlassPBRTMaterial(final Color3F colorKR, final Color3F colorKT, final Color3F colorEmission, final float floatEta, final float floatRoughness) {
		this(colorKR, colorKT, colorEmission, floatEta, floatRoughness, floatRoughness);
	}
	
	/**
	 * Constructs a new {@code GlassPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKR}, {@code colorKT} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassPBRTMaterial(colorKR, colorKT, colorEmission, floatEta, floatRoughnessU, floatRoughnessV, true);
	 * }
	 * </pre>
	 * 
	 * @param colorKR a {@link Color3F} instance for the reflection coefficient
	 * @param colorKT a {@code Color3F} instance for the transmission coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @param floatRoughnessU a {@code float} for the roughness along the U-axis
	 * @param floatRoughnessV a {@code float} for the roughness along the V-axis
	 * @throws NullPointerException thrown if, and only if, either {@code colorKR}, {@code colorKT} or {@code colorEmission} are {@code null}
	 */
	public GlassPBRTMaterial(final Color3F colorKR, final Color3F colorKT, final Color3F colorEmission, final float floatEta, final float floatRoughnessU, final float floatRoughnessV) {
		this(colorKR, colorKT, colorEmission, floatEta, floatRoughnessU, floatRoughnessV, true);
	}
	
	/**
	 * Constructs a new {@code GlassPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKR}, {@code colorKT} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorKR a {@link Color3F} instance for the reflection coefficient
	 * @param colorKT a {@code Color3F} instance for the transmission coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @param floatRoughnessU a {@code float} for the roughness along the U-axis
	 * @param floatRoughnessV a {@code float} for the roughness along the V-axis
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code colorKR}, {@code colorKT} or {@code colorEmission} are {@code null}
	 */
	public GlassPBRTMaterial(final Color3F colorKR, final Color3F colorKT, final Color3F colorEmission, final float floatEta, final float floatRoughnessU, final float floatRoughnessV, final boolean isRemappingRoughness) {
		this.textureKR = new ConstantTexture(Objects.requireNonNull(colorKR, "colorKR == null"));
		this.textureKT = new ConstantTexture(Objects.requireNonNull(colorKT, "colorKT == null"));
		this.textureEmission = new ConstantTexture(Objects.requireNonNull(colorEmission, "colorEmission == null"));
		this.textureEta = new ConstantTexture(floatEta);
		this.textureRoughnessU = new ConstantTexture(floatRoughnessU);
		this.textureRoughnessV = new ConstantTexture(floatRoughnessV);
		this.isRemappingRoughness = isRemappingRoughness;
	}
	
	/**
	 * Constructs a new {@code GlassPBRTMaterial} instance.
	 * <p>
	 * If {@code textureK} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassPBRTMaterial(textureK, textureK);
	 * }
	 * </pre>
	 * 
	 * @param textureK a {@link Texture} instance for the reflection and transmission coefficients
	 * @throws NullPointerException thrown if, and only if, {@code textureK} is {@code null}
	 */
	public GlassPBRTMaterial(final Texture textureK) {
		this(textureK, textureK);
	}
	
	/**
	 * Constructs a new {@code GlassPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKR} or {@code textureKT} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassPBRTMaterial(textureKR, textureKT, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @param textureKT a {@code Texture} instance for the transmission coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code textureKR} or {@code textureKT} are {@code null}
	 */
	public GlassPBRTMaterial(final Texture textureKR, final Texture textureKT) {
		this(textureKR, textureKT, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code GlassPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKR}, {@code textureKT} or {@code textureEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassPBRTMaterial(textureKR, textureKT, textureEmission, ConstantTexture.GRAY_1_50);
	 * }
	 * </pre>
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @param textureKT a {@code Texture} instance for the transmission coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code textureKR}, {@code textureKT} or {@code textureEmission} are {@code null}
	 */
	public GlassPBRTMaterial(final Texture textureKR, final Texture textureKT, final Texture textureEmission) {
		this(textureKR, textureKT, textureEmission, ConstantTexture.GRAY_1_50);
	}
	
	/**
	 * Constructs a new {@code GlassPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKR}, {@code textureKT}, {@code textureEmission} or {@code textureEta} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassPBRTMaterial(textureKR, textureKT, textureEmission, textureEta, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @param textureKT a {@code Texture} instance for the transmission coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @throws NullPointerException thrown if, and only if, either {@code textureKR}, {@code textureKT}, {@code textureEmission} or {@code textureEta} are {@code null}
	 */
	public GlassPBRTMaterial(final Texture textureKR, final Texture textureKT, final Texture textureEmission, final Texture textureEta) {
		this(textureKR, textureKT, textureEmission, textureEta, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code GlassPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKR}, {@code textureKT}, {@code textureEmission}, {@code textureEta} or {@code textureRoughness} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassPBRTMaterial(textureKR, textureKT, textureEmission, textureEta, textureRoughness, textureRoughness);
	 * }
	 * </pre>
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @param textureKT a {@code Texture} instance for the transmission coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureRoughness a {@code Texture} instance for the roughness along the U-axis and the V-axis
	 * @throws NullPointerException thrown if, and only if, either {@code textureKR}, {@code textureKT}, {@code textureEmission}, {@code textureEta} or {@code textureRoughness} are {@code null}
	 */
	public GlassPBRTMaterial(final Texture textureKR, final Texture textureKT, final Texture textureEmission, final Texture textureEta, final Texture textureRoughness) {
		this(textureKR, textureKT, textureEmission, textureEta, textureRoughness, textureRoughness);
	}
	
	/**
	 * Constructs a new {@code GlassPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKR}, {@code textureKT}, {@code textureEmission}, {@code textureEta}, {@code textureRoughnessU} or {@code textureRoughnessV} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GlassPBRTMaterial(textureKR, textureKT, textureEmission, textureEta, textureRoughnessU, textureRoughnessV, true);
	 * }
	 * </pre>
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @param textureKT a {@code Texture} instance for the transmission coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureRoughnessU a {@code Texture} instance for the roughness along the U-axis
	 * @param textureRoughnessV a {@code Texture} instance for the roughness along the V-axis
	 * @throws NullPointerException thrown if, and only if, either {@code textureKR}, {@code textureKT}, {@code textureEmission}, {@code textureEta}, {@code textureRoughnessU} or {@code textureRoughnessV} are {@code null}
	 */
	public GlassPBRTMaterial(final Texture textureKR, final Texture textureKT, final Texture textureEmission, final Texture textureEta, final Texture textureRoughnessU, final Texture textureRoughnessV) {
		this(textureKR, textureKT, textureEmission, textureEta, textureRoughnessU, textureRoughnessV, true);
	}
	
	/**
	 * Constructs a new {@code GlassPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKR}, {@code textureKT}, {@code textureEmission}, {@code textureEta}, {@code textureRoughnessU} or {@code textureRoughnessV} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureKR a {@link Texture} instance for the reflection coefficient
	 * @param textureKT a {@code Texture} instance for the transmission coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureRoughnessU a {@code Texture} instance for the roughness along the U-axis
	 * @param textureRoughnessV a {@code Texture} instance for the roughness along the V-axis
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code textureKR}, {@code textureKT}, {@code textureEmission}, {@code textureEta}, {@code textureRoughnessU} or {@code textureRoughnessV} are {@code null}
	 */
	public GlassPBRTMaterial(final Texture textureKR, final Texture textureKT, final Texture textureEmission, final Texture textureEta, final Texture textureRoughnessU, final Texture textureRoughnessV, final boolean isRemappingRoughness) {
		this.textureKR = Objects.requireNonNull(textureKR, "textureKR == null");
		this.textureKT = Objects.requireNonNull(textureKT, "textureKT == null");
		this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
		this.textureEta = Objects.requireNonNull(textureEta, "textureEta == null");
		this.textureRoughnessU = Objects.requireNonNull(textureRoughnessU, "textureRoughnessU == null");
		this.textureRoughnessV = Objects.requireNonNull(textureRoughnessV, "textureRoughnessV == null");
		this.isRemappingRoughness = isRemappingRoughness;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code GlassPBRTMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code GlassPBRTMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F emittance(final Intersection intersection) {
		return this.textureEmission.getColor(intersection);
	}
	
	/**
	 * Computes the {@link BSDF} at {@code intersection}.
	 * <p>
	 * Returns an optional {@code BSDF} instance.
	 * <p>
	 * If either {@code intersection} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection the {@link Intersection} to compute the {@code BSDF} for
	 * @param transportMode the {@link TransportMode} to use
	 * @param isAllowingMultipleLobes {@code true} if, and only if, multiple lobes are allowed, {@code false} otherwise
	 * @return an optional {@code BSDF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code transportMode} are {@code null}
	 */
	@Override
	public Optional<BSDF> computeBSDF(final Intersection intersection, final TransportMode transportMode, final boolean isAllowingMultipleLobes) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(transportMode, "transportMode == null");
		
		final Color3F colorKR = Color3F.saturate(this.textureKR.getColor(intersection), 0.0F, Float.MAX_VALUE);
		final Color3F colorKT = Color3F.saturate(this.textureKT.getColor(intersection), 0.0F, Float.MAX_VALUE);
		
		final float eta = this.textureEta.getFloat(intersection);
		final float roughnessU = this.textureRoughnessU.getFloat(intersection);
		final float roughnessV = this.textureRoughnessV.getFloat(intersection);
		
		if(colorKR.isBlack() && colorKT.isBlack()) {
			return Optional.empty();
		}
		
		final boolean isSpecular = isZero(roughnessU) && isZero(roughnessV);
		
		if(isSpecular && isAllowingMultipleLobes) {
			return Optional.of(new PBRTBSDF(intersection, new FresnelSpecularPBRTBXDF(colorKR, colorKT, transportMode, 1.0F, eta), eta));
		}
		
		if(isSpecular) {
			final List<BXDF> bXDFs = new ArrayList<>();
			
			if(!colorKR.isBlack()) {
				final Fresnel fresnel = new DielectricFresnel(1.0F, eta);
				
				bXDFs.add(new SpecularPBRTBRDF(colorKR, fresnel));
			}
			
			if(!colorKT.isBlack()) {
				bXDFs.add(new SpecularPBRTBTDF(colorKT, transportMode, 1.0F, eta));
			}
			
			return Optional.of(new PBRTBSDF(intersection, bXDFs, eta));
		}
		
		final List<BXDF> bXDFs = new ArrayList<>();
		
		final float roughnessURemapped = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(roughnessU) : roughnessU;
		final float roughnessVRemapped = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(roughnessV) : roughnessV;
		
		final MicrofacetDistribution microfacetDistribution = new TrowbridgeReitzMicrofacetDistribution(true, false, roughnessURemapped, roughnessVRemapped);
		
		if(!colorKR.isBlack()) {
			final Fresnel fresnel = new DielectricFresnel(1.0F, eta);
			
			bXDFs.add(new TorranceSparrowPBRTBRDF(colorKR, fresnel, microfacetDistribution));
		}
		
		if(!colorKT.isBlack()) {
			bXDFs.add(new TorranceSparrowPBRTBTDF(colorKT, microfacetDistribution, transportMode, 1.0F, eta));
		}
		
		return Optional.of(new PBRTBSDF(intersection, bXDFs, eta));
	}
	
	/**
	 * Computes the {@link BSSRDF} at {@code intersection}.
	 * <p>
	 * Returns an optional {@code BSSRDF} instance.
	 * <p>
	 * If either {@code intersection} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection the {@link Intersection} to compute the {@code BSSRDF} for
	 * @param transportMode the {@link TransportMode} to use
	 * @param isAllowingMultipleLobes {@code true} if, and only if, multiple lobes are allowed, {@code false} otherwise
	 * @return an optional {@code BSSRDF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code transportMode} are {@code null}
	 */
	@Override
	public Optional<BSSRDF> computeBSSRDF(final Intersection intersection, final TransportMode transportMode, final boolean isAllowingMultipleLobes) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(transportMode, "transportMode == null");
		
		return Optional.empty();
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code GlassPBRTMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code GlassPBRTMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code GlassPBRTMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code GlassPBRTMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new GlassPBRTMaterial(%s, %s, %s, %s, %s, %s, %s)", this.textureKR, this.textureKT, this.textureEmission, this.textureEta, this.textureRoughnessU, this.textureRoughnessV, Boolean.toString(this.isRemappingRoughness));
	}
	
	/**
	 * Returns the {@link Texture} instance for emission.
	 * 
	 * @return the {@code Texture} instance for emission
	 */
	public Texture getTextureEmission() {
		return this.textureEmission;
	}
	
	/**
	 * Returns the {@link Texture} instance for the index of refraction (IOR).
	 * 
	 * @return the {@code Texture} instance for the index of refraction (IOR)
	 */
	public Texture getTextureEta() {
		return this.textureEta;
	}
	
	/**
	 * Returns the {@link Texture} instance for the reflection coefficient.
	 * 
	 * @return the {@code Texture} instance for the reflection coefficient
	 */
	public Texture getTextureKR() {
		return this.textureKR;
	}
	
	/**
	 * Returns the {@link Texture} instance for the transmission coefficient.
	 * 
	 * @return the {@code Texture} instance for the transmission coefficient
	 */
	public Texture getTextureKT() {
		return this.textureKT;
	}
	
	/**
	 * Returns the {@link Texture} instance for the roughness along the U-axis.
	 * 
	 * @return the {@code Texture} instance for the roughness along the U-axis
	 */
	public Texture getTextureRoughnessU() {
		return this.textureRoughnessU;
	}
	
	/**
	 * Returns the {@link Texture} instance for the roughness along the V-axis.
	 * 
	 * @return the {@code Texture} instance for the roughness along the V-axis
	 */
	public Texture getTextureRoughnessV() {
		return this.textureRoughnessV;
	}
	
	/**
	 * Accepts a {@link NodeHierarchicalVisitor}.
	 * <p>
	 * Returns the result of {@code nodeHierarchicalVisitor.visitLeave(this)}.
	 * <p>
	 * If {@code nodeHierarchicalVisitor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}, a {@code NodeTraversalException} will be thrown with the {@code RuntimeException} wrapped.
	 * <p>
	 * This implementation will:
	 * <ul>
	 * <li>throw a {@code NullPointerException} if {@code nodeHierarchicalVisitor} is {@code null}.</li>
	 * <li>throw a {@code NodeTraversalException} if {@code nodeHierarchicalVisitor} throws a {@code RuntimeException}.</li>
	 * <li>traverse its child {@code Node} instances.</li>
	 * </ul>
	 * 
	 * @param nodeHierarchicalVisitor the {@code NodeHierarchicalVisitor} to accept
	 * @return the result of {@code nodeHierarchicalVisitor.visitLeave(this)}
	 * @throws NodeTraversalException thrown if, and only if, a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}
	 * @throws NullPointerException thrown if, and only if, {@code nodeHierarchicalVisitor} is {@code null}
	 */
	@Override
	public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
		Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
		
		try {
			if(nodeHierarchicalVisitor.visitEnter(this)) {
				if(!this.textureEmission.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureEta.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureKR.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureKT.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureRoughnessU.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureRoughnessV.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code GlassPBRTMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code GlassPBRTMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code GlassPBRTMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code GlassPBRTMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof GlassPBRTMaterial)) {
			return false;
		} else if(!Objects.equals(this.textureEmission, GlassPBRTMaterial.class.cast(object).textureEmission)) {
			return false;
		} else if(!Objects.equals(this.textureEta, GlassPBRTMaterial.class.cast(object).textureEta)) {
			return false;
		} else if(!Objects.equals(this.textureKR, GlassPBRTMaterial.class.cast(object).textureKR)) {
			return false;
		} else if(!Objects.equals(this.textureKT, GlassPBRTMaterial.class.cast(object).textureKT)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessU, GlassPBRTMaterial.class.cast(object).textureRoughnessU)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessV, GlassPBRTMaterial.class.cast(object).textureRoughnessV)) {
			return false;
		} else if(this.isRemappingRoughness != GlassPBRTMaterial.class.cast(object).isRemappingRoughness) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 */
	public boolean isRemappingRoughness() {
		return this.isRemappingRoughness;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code GlassPBRTMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code GlassPBRTMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code GlassPBRTMaterial} instance.
	 * 
	 * @return a hash code for this {@code GlassPBRTMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureEmission, this.textureEta, this.textureKR, this.textureKT, this.textureRoughnessU, this.textureRoughnessV, Boolean.valueOf(this.isRemappingRoughness));
	}
}