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
import org.dayflower.scene.Material;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.LambertianBRDF;
import org.dayflower.scene.bxdf.pbrt.SpecularPBRTBRDF;
import org.dayflower.scene.bxdf.pbrt.SpecularPBRTBTDF;
import org.dayflower.scene.bxdf.pbrt.TorranceSparrowPBRTBRDF;
import org.dayflower.scene.fresnel.DielectricFresnel;
import org.dayflower.scene.microfacet.MicrofacetDistribution;
import org.dayflower.scene.microfacet.TrowbridgeReitzMicrofacetDistribution;
import org.dayflower.scene.modifier.Modifier;
import org.dayflower.scene.modifier.NoOpModifier;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

/**
 * An {@code UberPBRTMaterial} is an implementation of {@link Material} that can represent a wide variety of materials.
 * <p>
 * This class is immutable and thread-safe as long as the {@link Modifier} instance and all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class UberPBRTMaterial implements Material {
	/**
	 * The name of this {@code UberPBRTMaterial} class.
	 */
	public static final String NAME = "PBRT - Uber";
	
	/**
	 * The ID of this {@code UberPBRTMaterial} class.
	 */
	public static final int ID = 108;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Modifier modifier;
	private final Texture textureEmission;
	private final Texture textureEta;
	private final Texture textureKD;
	private final Texture textureKR;
	private final Texture textureKS;
	private final Texture textureKT;
	private final Texture textureOpacity;
	private final Texture textureRoughnessU;
	private final Texture textureRoughnessV;
	private final boolean isRemappingRoughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberPBRTMaterial(Color3F.GRAY_0_25);
	 * }
	 * </pre>
	 */
	public UberPBRTMaterial() {
		this(Color3F.GRAY_0_25);
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If {@code colorKD} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberPBRTMaterial(colorKD, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @throws NullPointerException thrown if, and only if, {@code colorKD} is {@code null}
	 */
	public UberPBRTMaterial(final Color3F colorKD) {
		this(colorKD, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKD} or {@code colorKR} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberPBRTMaterial(colorKD, colorKR, Color3F.GRAY_0_25);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKR a {@code Color3F} instance for the reflection coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD} or {@code colorKR} are {@code null}
	 */
	public UberPBRTMaterial(final Color3F colorKD, final Color3F colorKR) {
		this(colorKD, colorKR, Color3F.GRAY_0_25);
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKR} or {@code colorKS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberPBRTMaterial(colorKD, colorKR, colorKS, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKR a {@code Color3F} instance for the reflection coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKR} or {@code colorKS} are {@code null}
	 */
	public UberPBRTMaterial(final Color3F colorKD, final Color3F colorKR, final Color3F colorKS) {
		this(colorKD, colorKR, colorKS, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKR}, {@code colorKS} or {@code colorKT} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberPBRTMaterial(colorKD, colorKR, colorKS, colorKT, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKR a {@code Color3F} instance for the reflection coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorKT a {@code Color3F} instance for the transmission coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKR}, {@code colorKS} or {@code colorKT} are {@code null}
	 */
	public UberPBRTMaterial(final Color3F colorKD, final Color3F colorKR, final Color3F colorKS, final Color3F colorKT) {
		this(colorKD, colorKR, colorKS, colorKT, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKR}, {@code colorKS}, {@code colorKT} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberPBRTMaterial(colorKD, colorKR, colorKS, colorKT, colorEmission, Color3F.WHITE);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKR a {@code Color3F} instance for the reflection coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorKT a {@code Color3F} instance for the transmission coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKR}, {@code colorKS}, {@code colorKT} or {@code colorEmission} are {@code null}
	 */
	public UberPBRTMaterial(final Color3F colorKD, final Color3F colorKR, final Color3F colorKS, final Color3F colorKT, final Color3F colorEmission) {
		this(colorKD, colorKR, colorKS, colorKT, colorEmission, Color3F.WHITE);
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKR}, {@code colorKS}, {@code colorKT}, {@code colorEmission} or {@code colorOpacity} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberPBRTMaterial(colorKD, colorKR, colorKS, colorKT, colorEmission, colorOpacity, 1.5F);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKR a {@code Color3F} instance for the reflection coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorKT a {@code Color3F} instance for the transmission coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorOpacity a {@code Color3F} instance for the opacity
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKR}, {@code colorKS}, {@code colorKT}, {@code colorEmission} or {@code colorOpacity} are {@code null}
	 */
	public UberPBRTMaterial(final Color3F colorKD, final Color3F colorKR, final Color3F colorKS, final Color3F colorKT, final Color3F colorEmission, final Color3F colorOpacity) {
		this(colorKD, colorKR, colorKS, colorKT, colorEmission, colorOpacity, 1.5F);
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKR}, {@code colorKS}, {@code colorKT}, {@code colorEmission} or {@code colorOpacity} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberPBRTMaterial(colorKD, colorKR, colorKS, colorKT, colorEmission, colorOpacity, floatEta, 0.1F);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKR a {@code Color3F} instance for the reflection coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorKT a {@code Color3F} instance for the transmission coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorOpacity a {@code Color3F} instance for the opacity
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKR}, {@code colorKS}, {@code colorKT}, {@code colorEmission} or {@code colorOpacity} are {@code null}
	 */
	public UberPBRTMaterial(final Color3F colorKD, final Color3F colorKR, final Color3F colorKS, final Color3F colorKT, final Color3F colorEmission, final Color3F colorOpacity, final float floatEta) {
		this(colorKD, colorKR, colorKS, colorKT, colorEmission, colorOpacity, floatEta, 0.1F);
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKR}, {@code colorKS}, {@code colorKT}, {@code colorEmission} or {@code colorOpacity} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberPBRTMaterial(colorKD, colorKR, colorKS, colorKT, colorEmission, colorOpacity, floatEta, floatRoughness, floatRoughness);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKR a {@code Color3F} instance for the reflection coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorKT a {@code Color3F} instance for the transmission coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorOpacity a {@code Color3F} instance for the opacity
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @param floatRoughness a {@code float} for the roughness along the U-axis and the V-axis
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKR}, {@code colorKS}, {@code colorKT}, {@code colorEmission} or {@code colorOpacity} are {@code null}
	 */
	public UberPBRTMaterial(final Color3F colorKD, final Color3F colorKR, final Color3F colorKS, final Color3F colorKT, final Color3F colorEmission, final Color3F colorOpacity, final float floatEta, final float floatRoughness) {
		this(colorKD, colorKR, colorKS, colorKT, colorEmission, colorOpacity, floatEta, floatRoughness, floatRoughness);
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKR}, {@code colorKS}, {@code colorKT}, {@code colorEmission} or {@code colorOpacity} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberPBRTMaterial(colorKD, colorKR, colorKS, colorKT, colorEmission, colorOpacity, floatEta, floatRoughnessU, floatRoughnessV, true);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKR a {@code Color3F} instance for the reflection coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorKT a {@code Color3F} instance for the transmission coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorOpacity a {@code Color3F} instance for the opacity
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @param floatRoughnessU a {@code float} for the roughness along the U-axis
	 * @param floatRoughnessV a {@code float} for the roughness along the V-axis
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKR}, {@code colorKS}, {@code colorKT}, {@code colorEmission} or {@code colorOpacity} are {@code null}
	 */
	public UberPBRTMaterial(final Color3F colorKD, final Color3F colorKR, final Color3F colorKS, final Color3F colorKT, final Color3F colorEmission, final Color3F colorOpacity, final float floatEta, final float floatRoughnessU, final float floatRoughnessV) {
		this(colorKD, colorKR, colorKS, colorKT, colorEmission, colorOpacity, floatEta, floatRoughnessU, floatRoughnessV, true);
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKR}, {@code colorKS}, {@code colorKT}, {@code colorEmission} or {@code colorOpacity} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberPBRTMaterial(colorKD, colorKR, colorKS, colorKT, colorEmission, colorOpacity, floatEta, floatRoughnessU, floatRoughnessV, isRemappingRoughness, new NoOpModifier());
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKR a {@code Color3F} instance for the reflection coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorKT a {@code Color3F} instance for the transmission coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorOpacity a {@code Color3F} instance for the opacity
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @param floatRoughnessU a {@code float} for the roughness along the U-axis
	 * @param floatRoughnessV a {@code float} for the roughness along the V-axis
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKR}, {@code colorKS}, {@code colorKT}, {@code colorEmission} or {@code colorOpacity} are {@code null}
	 */
	public UberPBRTMaterial(final Color3F colorKD, final Color3F colorKR, final Color3F colorKS, final Color3F colorKT, final Color3F colorEmission, final Color3F colorOpacity, final float floatEta, final float floatRoughnessU, final float floatRoughnessV, final boolean isRemappingRoughness) {
		this(colorKD, colorKR, colorKS, colorKT, colorEmission, colorOpacity, floatEta, floatRoughnessU, floatRoughnessV, isRemappingRoughness, new NoOpModifier());
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKR}, {@code colorKS}, {@code colorKT}, {@code colorEmission}, {@code colorOpacity} or {@code modifier} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKR a {@code Color3F} instance for the reflection coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorKT a {@code Color3F} instance for the transmission coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param colorOpacity a {@code Color3F} instance for the opacity
	 * @param floatEta a {@code float} for the index of refraction (IOR)
	 * @param floatRoughnessU a {@code float} for the roughness along the U-axis
	 * @param floatRoughnessV a {@code float} for the roughness along the V-axis
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @param modifier a {@link Modifier} instance
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKR}, {@code colorKS}, {@code colorKT}, {@code colorEmission}, {@code colorOpacity} or {@code modifier} are {@code null}
	 */
	public UberPBRTMaterial(final Color3F colorKD, final Color3F colorKR, final Color3F colorKS, final Color3F colorKT, final Color3F colorEmission, final Color3F colorOpacity, final float floatEta, final float floatRoughnessU, final float floatRoughnessV, final boolean isRemappingRoughness, final Modifier modifier) {
		this.textureKD = new ConstantTexture(Objects.requireNonNull(colorKD, "colorKD == null"));
		this.textureKR = new ConstantTexture(Objects.requireNonNull(colorKR, "colorKR == null"));
		this.textureKS = new ConstantTexture(Objects.requireNonNull(colorKS, "colorKS == null"));
		this.textureKT = new ConstantTexture(Objects.requireNonNull(colorKT, "colorKT == null"));
		this.textureEmission = new ConstantTexture(Objects.requireNonNull(colorEmission, "colorEmission == null"));
		this.textureOpacity = new ConstantTexture(Objects.requireNonNull(colorOpacity, "colorOpacity == null"));
		this.textureEta = new ConstantTexture(floatEta);
		this.textureRoughnessU = new ConstantTexture(floatRoughnessU);
		this.textureRoughnessV = new ConstantTexture(floatRoughnessV);
		this.isRemappingRoughness = isRemappingRoughness;
		this.modifier = Objects.requireNonNull(modifier, "modifier == null");
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If {@code textureKD} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberPBRTMaterial(textureKD, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @throws NullPointerException thrown if, and only if, {@code textureKD} is {@code null}
	 */
	public UberPBRTMaterial(final Texture textureKD) {
		this(textureKD, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKD} or {@code textureKR} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberPBRTMaterial(textureKD, textureKR, ConstantTexture.GRAY_0_25);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKR a {@code Texture} instance for the reflection coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD} or {@code textureKR} are {@code null}
	 */
	public UberPBRTMaterial(final Texture textureKD, final Texture textureKR) {
		this(textureKD, textureKR, ConstantTexture.GRAY_0_25);
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKR} or {@code textureKS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberPBRTMaterial(textureKD, textureKR, textureKS, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKR a {@code Texture} instance for the reflection coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKR} or {@code textureKS} are {@code null}
	 */
	public UberPBRTMaterial(final Texture textureKD, final Texture textureKR, final Texture textureKS) {
		this(textureKD, textureKR, textureKS, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKR}, {@code textureKS} or {@code textureKT} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberPBRTMaterial(textureKD, textureKR, textureKS, textureKT, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKR a {@code Texture} instance for the reflection coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureKT a {@code Texture} instance for the transmission coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKR}, {@code textureKS} or {@code textureKT} are {@code null}
	 */
	public UberPBRTMaterial(final Texture textureKD, final Texture textureKR, final Texture textureKS, final Texture textureKT) {
		this(textureKD, textureKR, textureKS, textureKT, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKR}, {@code textureKS}, {@code textureKT} or {@code textureEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberPBRTMaterial(textureKD, textureKR, textureKS, textureKT, textureEmission, ConstantTexture.WHITE);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKR a {@code Texture} instance for the reflection coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureKT a {@code Texture} instance for the transmission coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKR}, {@code textureKS}, {@code textureKT} or {@code textureEmission} are {@code null}
	 */
	public UberPBRTMaterial(final Texture textureKD, final Texture textureKR, final Texture textureKS, final Texture textureKT, final Texture textureEmission) {
		this(textureKD, textureKR, textureKS, textureKT, textureEmission, ConstantTexture.WHITE);
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKR}, {@code textureKS}, {@code textureKT}, {@code textureEmission} or {@code textureOpacity} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberPBRTMaterial(textureKD, textureKR, textureKS, textureKT, textureEmission, textureOpacity, ConstantTexture.GRAY_1_50);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKR a {@code Texture} instance for the reflection coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureKT a {@code Texture} instance for the transmission coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureOpacity a {@code Texture} instance for the opacity
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKR}, {@code textureKS}, {@code textureKT}, {@code textureEmission} or {@code textureOpacity} are {@code null}
	 */
	public UberPBRTMaterial(final Texture textureKD, final Texture textureKR, final Texture textureKS, final Texture textureKT, final Texture textureEmission, final Texture textureOpacity) {
		this(textureKD, textureKR, textureKS, textureKT, textureEmission, textureOpacity, ConstantTexture.GRAY_1_50);
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKR}, {@code textureKS}, {@code textureKT}, {@code textureEmission}, {@code textureOpacity} or {@code textureEta} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberPBRTMaterial(textureKD, textureKR, textureKS, textureKT, textureEmission, textureOpacity, textureEta, ConstantTexture.GRAY_0_10);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKR a {@code Texture} instance for the reflection coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureKT a {@code Texture} instance for the transmission coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureOpacity a {@code Texture} instance for the opacity
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKR}, {@code textureKS}, {@code textureKT}, {@code textureEmission}, {@code textureOpacity} or {@code textureEta} are {@code null}
	 */
	public UberPBRTMaterial(final Texture textureKD, final Texture textureKR, final Texture textureKS, final Texture textureKT, final Texture textureEmission, final Texture textureOpacity, final Texture textureEta) {
		this(textureKD, textureKR, textureKS, textureKT, textureEmission, textureOpacity, textureEta, ConstantTexture.GRAY_0_10);
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKR}, {@code textureKS}, {@code textureKT}, {@code textureEmission}, {@code textureOpacity}, {@code textureEta} or {@code textureRoughness} are {@code null}, a {@code NullPointerException} will be
	 * thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberPBRTMaterial(textureKD, textureKR, textureKS, textureKT, textureEmission, textureOpacity, textureEta, textureRoughness, textureRoughness);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKR a {@code Texture} instance for the reflection coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureKT a {@code Texture} instance for the transmission coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureOpacity a {@code Texture} instance for the opacity
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureRoughness a {@code Texture} instance for the roughness along the U-axis and the V-axis
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKR}, {@code textureKS}, {@code textureKT}, {@code textureEmission}, {@code textureOpacity}, {@code textureEta} or {@code textureRoughness} are
	 *                              {@code null}
	 */
	public UberPBRTMaterial(final Texture textureKD, final Texture textureKR, final Texture textureKS, final Texture textureKT, final Texture textureEmission, final Texture textureOpacity, final Texture textureEta, final Texture textureRoughness) {
		this(textureKD, textureKR, textureKS, textureKT, textureEmission, textureOpacity, textureEta, textureRoughness, textureRoughness);
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKR}, {@code textureKS}, {@code textureKT}, {@code textureEmission}, {@code textureOpacity}, {@code textureEta}, {@code textureRoughnessU} or {@code textureRoughnessV} are {@code null}, a
	 * {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberPBRTMaterial(textureKD, textureKR, textureKS, textureKT, textureEmission, textureOpacity, textureEta, textureRoughnessU, textureRoughnessV, true);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKR a {@code Texture} instance for the reflection coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureKT a {@code Texture} instance for the transmission coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureOpacity a {@code Texture} instance for the opacity
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureRoughnessU a {@code Texture} instance for the roughness along the U-axis
	 * @param textureRoughnessV a {@code Texture} instance for the roughness along the V-axis
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKR}, {@code textureKS}, {@code textureKT}, {@code textureEmission}, {@code textureOpacity}, {@code textureEta}, {@code textureRoughnessU} or
	 *                              {@code textureRoughnessV} are {@code null}
	 */
	public UberPBRTMaterial(final Texture textureKD, final Texture textureKR, final Texture textureKS, final Texture textureKT, final Texture textureEmission, final Texture textureOpacity, final Texture textureEta, final Texture textureRoughnessU, final Texture textureRoughnessV) {
		this(textureKD, textureKR, textureKS, textureKT, textureEmission, textureOpacity, textureEta, textureRoughnessU, textureRoughnessV, true);
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKR}, {@code textureKS}, {@code textureKT}, {@code textureEmission}, {@code textureOpacity}, {@code textureEta}, {@code textureRoughnessU} or {@code textureRoughnessV} are {@code null}, a
	 * {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberPBRTMaterial(textureKD, textureKR, textureKS, textureKT, textureEmission, textureOpacity, textureEta, textureRoughnessU, textureRoughnessV, isRemappingRoughness, new NoOpModifier());
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKR a {@code Texture} instance for the reflection coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureKT a {@code Texture} instance for the transmission coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureOpacity a {@code Texture} instance for the opacity
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureRoughnessU a {@code Texture} instance for the roughness along the U-axis
	 * @param textureRoughnessV a {@code Texture} instance for the roughness along the V-axis
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKR}, {@code textureKS}, {@code textureKT}, {@code textureEmission}, {@code textureOpacity}, {@code textureEta}, {@code textureRoughnessU} or
	 *                              {@code textureRoughnessV} are {@code null}
	 */
	public UberPBRTMaterial(final Texture textureKD, final Texture textureKR, final Texture textureKS, final Texture textureKT, final Texture textureEmission, final Texture textureOpacity, final Texture textureEta, final Texture textureRoughnessU, final Texture textureRoughnessV, final boolean isRemappingRoughness) {
		this(textureKD, textureKR, textureKS, textureKT, textureEmission, textureOpacity, textureEta, textureRoughnessU, textureRoughnessV, isRemappingRoughness, new NoOpModifier());
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKR}, {@code textureKS}, {@code textureKT}, {@code textureEmission}, {@code textureOpacity}, {@code textureEta}, {@code textureRoughnessU}, {@code textureRoughnessV} or {@code modifier} are {@code null},
	 * a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKR a {@code Texture} instance for the reflection coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureKT a {@code Texture} instance for the transmission coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureOpacity a {@code Texture} instance for the opacity
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureRoughnessU a {@code Texture} instance for the roughness along the U-axis
	 * @param textureRoughnessV a {@code Texture} instance for the roughness along the V-axis
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @param modifier a {@link Modifier} instance
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKR}, {@code textureKS}, {@code textureKT}, {@code textureEmission}, {@code textureOpacity}, {@code textureEta}, {@code textureRoughnessU},
	 *                              {@code textureRoughnessV} or {@code modifier} are {@code null}
	 */
	public UberPBRTMaterial(final Texture textureKD, final Texture textureKR, final Texture textureKS, final Texture textureKT, final Texture textureEmission, final Texture textureOpacity, final Texture textureEta, final Texture textureRoughnessU, final Texture textureRoughnessV, final boolean isRemappingRoughness, final Modifier modifier) {
		this.textureKD = Objects.requireNonNull(textureKD, "textureKD == null");
		this.textureKR = Objects.requireNonNull(textureKR, "textureKR == null");
		this.textureKS = Objects.requireNonNull(textureKS, "textureKS == null");
		this.textureKT = Objects.requireNonNull(textureKT, "textureKT == null");
		this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
		this.textureOpacity = Objects.requireNonNull(textureOpacity, "textureOpacity == null");
		this.textureEta = Objects.requireNonNull(textureEta, "textureEta == null");
		this.textureRoughnessU = Objects.requireNonNull(textureRoughnessU, "textureRoughnessU == null");
		this.textureRoughnessV = Objects.requireNonNull(textureRoughnessV, "textureRoughnessV == null");
		this.isRemappingRoughness = isRemappingRoughness;
		this.modifier = Objects.requireNonNull(modifier, "modifier == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code UberPBRTMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code UberPBRTMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F emittance(final Intersection intersection) {
		return this.textureEmission.getColor(intersection);
	}
	
	/**
	 * Returns the {@link Modifier} instance.
	 * 
	 * @return the {@code Modifier} instance
	 */
	public Modifier getModifier() {
		return this.modifier;
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
		
		this.modifier.modify(intersection);
		
		final Color3F colorOpacity = Color3F.saturate(this.textureOpacity.getColor(intersection), 0.0F, Float.MAX_VALUE);
		final Color3F colorKD = Color3F.multiply(colorOpacity, Color3F.saturate(this.textureKD.getColor(intersection), 0.0F, Float.MAX_VALUE));
		final Color3F colorKR = Color3F.multiply(colorOpacity, Color3F.saturate(this.textureKR.getColor(intersection), 0.0F, Float.MAX_VALUE));
		final Color3F colorKS = Color3F.multiply(colorOpacity, Color3F.saturate(this.textureKS.getColor(intersection), 0.0F, Float.MAX_VALUE));
		final Color3F colorKT = Color3F.multiply(colorOpacity, Color3F.saturate(this.textureKT.getColor(intersection), 0.0F, Float.MAX_VALUE));
		final Color3F colorTransmittanceScale = Color3F.saturate(Color3F.subtract(Color3F.WHITE, colorOpacity), 0.0F, Float.MAX_VALUE);
		
		final float eta = this.textureEta.getFloat(intersection);
		final float roughnessU = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(this.textureRoughnessU.getFloat(intersection)) : this.textureRoughnessU.getFloat(intersection);
		final float roughnessV = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(this.textureRoughnessV.getFloat(intersection)) : this.textureRoughnessV.getFloat(intersection);
		
		final List<BXDF> bXDFs = new ArrayList<>();
		
		if(!colorTransmittanceScale.isBlack()) {
			bXDFs.add(new SpecularPBRTBTDF(colorTransmittanceScale, transportMode, 1.0F, 1.0F));
		}
		
		if(!colorKD.isBlack()) {
			bXDFs.add(new LambertianBRDF(colorKD));
		}
		
		if(!colorKS.isBlack()) {
			bXDFs.add(new TorranceSparrowPBRTBRDF(colorKS, new DielectricFresnel(1.0F, eta), new TrowbridgeReitzMicrofacetDistribution(true, false, roughnessU, roughnessV)));
		}
		
		if(!colorKR.isBlack()) {
			bXDFs.add(new SpecularPBRTBRDF(colorKR, new DielectricFresnel(1.0F, eta)));
		}
		
		if(!colorKT.isBlack()) {
			bXDFs.add(new SpecularPBRTBTDF(colorKT, transportMode, 1.0F, eta));
		}
		
		return Optional.of(new BSDF(intersection, bXDFs, false, colorTransmittanceScale.isBlack() ? eta : 1.0F));
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
	 * Returns a {@code String} with the name of this {@code UberPBRTMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code UberPBRTMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code UberPBRTMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code UberPBRTMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new UberPBRTMaterial(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", this.textureKD, this.textureKR, this.textureKS, this.textureKT, this.textureEmission, this.textureOpacity, this.textureEta, this.textureRoughnessU, this.textureRoughnessV, Boolean.toString(this.isRemappingRoughness), this.modifier);
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
	 * Returns the {@link Texture} instance for the diffuse coefficient.
	 * 
	 * @return the {@code Texture} instance for the diffuse coefficient
	 */
	public Texture getTextureKD() {
		return this.textureKD;
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
	 * Returns the {@link Texture} instance for the specular coefficient.
	 * 
	 * @return the {@code Texture} instance for the specular coefficient
	 */
	public Texture getTextureKS() {
		return this.textureKS;
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
	 * Returns the {@link Texture} instance for the opacity.
	 * 
	 * @return the {@code Texture} instance for the opacity
	 */
	public Texture getTextureOpacity() {
		return this.textureOpacity;
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
				if(!this.modifier.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureEmission.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureEta.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureKD.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureKR.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureKS.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureKT.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureOpacity.accept(nodeHierarchicalVisitor)) {
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
	 * Compares {@code object} to this {@code UberPBRTMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code UberPBRTMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code UberPBRTMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code UberPBRTMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof UberPBRTMaterial)) {
			return false;
		} else if(!Objects.equals(this.modifier, UberPBRTMaterial.class.cast(object).modifier)) {
			return false;
		} else if(!Objects.equals(this.textureEmission, UberPBRTMaterial.class.cast(object).textureEmission)) {
			return false;
		} else if(!Objects.equals(this.textureEta, UberPBRTMaterial.class.cast(object).textureEta)) {
			return false;
		} else if(!Objects.equals(this.textureKD, UberPBRTMaterial.class.cast(object).textureKD)) {
			return false;
		} else if(!Objects.equals(this.textureKR, UberPBRTMaterial.class.cast(object).textureKR)) {
			return false;
		} else if(!Objects.equals(this.textureKS, UberPBRTMaterial.class.cast(object).textureKS)) {
			return false;
		} else if(!Objects.equals(this.textureKT, UberPBRTMaterial.class.cast(object).textureKT)) {
			return false;
		} else if(!Objects.equals(this.textureOpacity, UberPBRTMaterial.class.cast(object).textureOpacity)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessU, UberPBRTMaterial.class.cast(object).textureRoughnessU)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessV, UberPBRTMaterial.class.cast(object).textureRoughnessV)) {
			return false;
		} else if(this.isRemappingRoughness != UberPBRTMaterial.class.cast(object).isRemappingRoughness) {
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
	 * Returns an {@code int} with the ID of this {@code UberPBRTMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code UberPBRTMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code UberPBRTMaterial} instance.
	 * 
	 * @return a hash code for this {@code UberPBRTMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.modifier, this.textureEmission, this.textureEta, this.textureKD, this.textureKR, this.textureKS, this.textureKT, this.textureOpacity, this.textureRoughnessU, this.textureRoughnessV, Boolean.valueOf(this.isRemappingRoughness));
	}
}