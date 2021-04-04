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
package org.dayflower.scene.material;

import static org.dayflower.utility.Ints.pack;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.scene.BSDF;
import org.dayflower.scene.BSSRDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.FresnelBlendBRDF;
import org.dayflower.scene.microfacet.MicrofacetDistribution;
import org.dayflower.scene.microfacet.TrowbridgeReitzMicrofacetDistribution;
import org.dayflower.scene.modifier.Modifier;
import org.dayflower.scene.modifier.NoOpModifier;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

/**
 * A {@code SubstrateMaterial} is an implementation of {@link Material} that represents a substrate material.
 * <p>
 * This class is immutable and thread-safe as long as the {@link Modifier} instance and all {@link Texture} instances are.
 * <p>
 * This {@code Material} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SubstrateMaterial implements Material {
	/**
	 * The name of this {@code SubstrateMaterial} class.
	 */
	public static final String NAME = "Substrate";
	
	/**
	 * The length of the {@code int[]}.
	 */
	public static final int ARRAY_LENGTH = 4;
	
	/**
	 * The offset for the roughness remapping flag in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_IS_REMAPPING_ROUGHNESS = 3;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code KD} and {@code KS} in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_K_D_AND_TEXTURE_K_S = 1;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Roughness U} and {@code Roughness V} in the {@code int[]}.
	 */
	public static final int ARRAY_OFFSET_TEXTURE_ROUGHNESS_U_AND_TEXTURE_ROUGHNESS_V = 2;
	
	/**
	 * The ID of this {@code SubstrateMaterial} class.
	 */
	public static final int ID = 112;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Modifier modifier;
	private final Texture textureEmission;
	private final Texture textureKD;
	private final Texture textureKS;
	private final Texture textureRoughnessU;
	private final Texture textureRoughnessV;
	private final boolean isRemappingRoughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SubstrateMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SubstrateMaterial(Color3F.GRAY_0_50);
	 * }
	 * </pre>
	 */
	public SubstrateMaterial() {
		this(Color3F.GRAY_0_50);
	}
	
	/**
	 * Constructs a new {@code SubstrateMaterial} instance.
	 * <p>
	 * If {@code colorKD} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SubstrateMaterial(colorKD, Color3F.GRAY_0_50);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @throws NullPointerException thrown if, and only if, {@code colorKD} is {@code null}
	 */
	public SubstrateMaterial(final Color3F colorKD) {
		this(colorKD, Color3F.GRAY_0_50);
	}
	
	/**
	 * Constructs a new {@code SubstrateMaterial} instance.
	 * <p>
	 * If either {@code colorKD} or {@code colorKS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SubstrateMaterial(colorKD, colorKS, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD} or {@code colorKS} are {@code null}
	 */
	public SubstrateMaterial(final Color3F colorKD, final Color3F colorKS) {
		this(colorKD, colorKS, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code SubstrateMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SubstrateMaterial(colorKD, colorKS, colorEmission, 0.1F);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}
	 */
	public SubstrateMaterial(final Color3F colorKD, final Color3F colorKS, final Color3F colorEmission) {
		this(colorKD, colorKS, colorEmission, 0.1F);
	}
	
	/**
	 * Constructs a new {@code SubstrateMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SubstrateMaterial(colorKD, colorKS, colorEmission, floatRoughness, floatRoughness);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param floatRoughness a {@code float} for the roughness along the U-axis and the V-axis
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}
	 */
	public SubstrateMaterial(final Color3F colorKD, final Color3F colorKS, final Color3F colorEmission, final float floatRoughness) {
		this(colorKD, colorKS, colorEmission, floatRoughness, floatRoughness);
	}
	
	/**
	 * Constructs a new {@code SubstrateMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SubstrateMaterial(colorKD, colorKS, colorEmission, floatRoughnessU, floatRoughnessV, true);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param floatRoughnessU a {@code float} for the roughness along the U-axis
	 * @param floatRoughnessV a {@code float} for the roughness along the V-axis
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}
	 */
	public SubstrateMaterial(final Color3F colorKD, final Color3F colorKS, final Color3F colorEmission, final float floatRoughnessU, final float floatRoughnessV) {
		this(colorKD, colorKS, colorEmission, floatRoughnessU, floatRoughnessV, true);
	}
	
	/**
	 * Constructs a new {@code SubstrateMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SubstrateMaterial(colorKD, colorKS, colorEmission, floatRoughnessU, floatRoughnessV, isRemappingRoughness, new NoOpModifier());
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param floatRoughnessU a {@code float} for the roughness along the U-axis
	 * @param floatRoughnessV a {@code float} for the roughness along the V-axis
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}
	 */
	public SubstrateMaterial(final Color3F colorKD, final Color3F colorKS, final Color3F colorEmission, final float floatRoughnessU, final float floatRoughnessV, final boolean isRemappingRoughness) {
		this(colorKD, colorKS, colorEmission, floatRoughnessU, floatRoughnessV, isRemappingRoughness, new NoOpModifier());
	}
	
	/**
	 * Constructs a new {@code SubstrateMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKS}, {@code colorEmission} or {@code modifier} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param floatRoughnessU a {@code float} for the roughness along the U-axis
	 * @param floatRoughnessV a {@code float} for the roughness along the V-axis
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @param modifier a {@link Modifier} instance
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKS}, {@code colorEmission} or {@code modifier} are {@code null}
	 */
	public SubstrateMaterial(final Color3F colorKD, final Color3F colorKS, final Color3F colorEmission, final float floatRoughnessU, final float floatRoughnessV, final boolean isRemappingRoughness, final Modifier modifier) {
		this.textureKD = new ConstantTexture(Objects.requireNonNull(colorKD, "colorKD == null"));
		this.textureKS = new ConstantTexture(Objects.requireNonNull(colorKS, "colorKS == null"));
		this.textureEmission = new ConstantTexture(Objects.requireNonNull(colorEmission, "colorEmission == null"));
		this.textureRoughnessU = new ConstantTexture(floatRoughnessU);
		this.textureRoughnessV = new ConstantTexture(floatRoughnessV);
		this.isRemappingRoughness = isRemappingRoughness;
		this.modifier = Objects.requireNonNull(modifier, "modifier == null");
	}
	
	/**
	 * Constructs a new {@code SubstrateMaterial} instance.
	 * <p>
	 * If {@code textureKD} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SubstrateMaterial(textureKD, ConstantTexture.GRAY_0_50);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @throws NullPointerException thrown if, and only if, {@code textureKD} is {@code null}
	 */
	public SubstrateMaterial(final Texture textureKD) {
		this(textureKD, ConstantTexture.GRAY_0_50);
	}
	
	/**
	 * Constructs a new {@code SubstrateMaterial} instance.
	 * <p>
	 * If either {@code textureKD} or {@code textureKS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SubstrateMaterial(textureKD, textureKS, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD} or {@code textureKS} are {@code null}
	 */
	public SubstrateMaterial(final Texture textureKD, final Texture textureKS) {
		this(textureKD, textureKS, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code SubstrateMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKS} or {@code textureEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SubstrateMaterial(textureKD, textureKS, textureEmission, ConstantTexture.GRAY_0_10);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKS} or {@code textureEmission} are {@code null}
	 */
	public SubstrateMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission) {
		this(textureKD, textureKS, textureEmission, ConstantTexture.GRAY_0_10);
	}
	
	/**
	 * Constructs a new {@code SubstrateMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKS}, {@code textureEmission} or {@code textureRoughness} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SubstrateMaterial(textureKD, textureKS, textureEmission, textureRoughness, textureRoughness);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureRoughness a {@code Texture} instance for the roughness along the U-axis and the V-axis
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKS}, {@code textureEmission} or {@code textureRoughness} are {@code null}
	 */
	public SubstrateMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission, final Texture textureRoughness) {
		this(textureKD, textureKS, textureEmission, textureRoughness, textureRoughness);
	}
	
	/**
	 * Constructs a new {@code SubstrateMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKS}, {@code textureEmission}, {@code textureRoughnessU} or {@code textureRoughnessV} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SubstrateMaterial(textureKD, textureKS, textureEmission, textureRoughnessU, textureRoughnessV, true);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureRoughnessU a {@code Texture} instance for the roughness along the U-axis
	 * @param textureRoughnessV a {@code Texture} instance for the roughness along the V-axis
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKS}, {@code textureEmission}, {@code textureRoughnessU} or {@code textureRoughnessV} are {@code null}
	 */
	public SubstrateMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission, final Texture textureRoughnessU, final Texture textureRoughnessV) {
		this(textureKD, textureKS, textureEmission, textureRoughnessU, textureRoughnessV, true);
	}
	
	/**
	 * Constructs a new {@code SubstrateMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKS}, {@code textureEmission}, {@code textureRoughnessU} or {@code textureRoughnessV} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SubstrateMaterial(textureKD, textureKS, textureEmission, textureRoughnessU, textureRoughnessV, isRemappingRoughness, new NoOpModifier());
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureRoughnessU a {@code Texture} instance for the roughness along the U-axis
	 * @param textureRoughnessV a {@code Texture} instance for the roughness along the V-axis
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKS}, {@code textureEmission}, {@code textureRoughnessU} or {@code textureRoughnessV} are {@code null}
	 */
	public SubstrateMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission, final Texture textureRoughnessU, final Texture textureRoughnessV, final boolean isRemappingRoughness) {
		this(textureKD, textureKS, textureEmission, textureRoughnessU, textureRoughnessV, isRemappingRoughness, new NoOpModifier());
	}
	
	/**
	 * Constructs a new {@code SubstrateMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKS}, {@code textureEmission}, {@code textureRoughnessU}, {@code textureRoughnessV} or {@code modifier} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureRoughnessU a {@code Texture} instance for the roughness along the U-axis
	 * @param textureRoughnessV a {@code Texture} instance for the roughness along the V-axis
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @param modifier a {@link Modifier} instance
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKS}, {@code textureEmission}, {@code textureRoughnessU}, {@code textureRoughnessV} or {@code modifier} are {@code null}
	 */
	public SubstrateMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission, final Texture textureRoughnessU, final Texture textureRoughnessV, final boolean isRemappingRoughness, final Modifier modifier) {
		this.textureKD = Objects.requireNonNull(textureKD, "textureKD == null");
		this.textureKS = Objects.requireNonNull(textureKS, "textureKS == null");
		this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
		this.textureRoughnessU = Objects.requireNonNull(textureRoughnessU, "textureRoughnessU == null");
		this.textureRoughnessV = Objects.requireNonNull(textureRoughnessV, "textureRoughnessV == null");
		this.isRemappingRoughness = isRemappingRoughness;
		this.modifier = Objects.requireNonNull(modifier, "modifier == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code SubstrateMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code SubstrateMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F emittance(final Intersection intersection) {
		Objects.requireNonNull(intersection, "intersection == null");
		
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
		
		final Color3F colorKD = Color3F.saturate(this.textureKD.getColor(intersection), 0.0F, Float.MAX_VALUE);
		final Color3F colorKS = Color3F.saturate(this.textureKS.getColor(intersection), 0.0F, Float.MAX_VALUE);
		
		if(!colorKD.isBlack() || !colorKS.isBlack()) {
			final float roughnessU = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(this.textureRoughnessU.getFloat(intersection)) : this.textureRoughnessU.getFloat(intersection);
			final float roughnessV = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(this.textureRoughnessV.getFloat(intersection)) : this.textureRoughnessV.getFloat(intersection);
			
			final MicrofacetDistribution microfacetDistribution = new TrowbridgeReitzMicrofacetDistribution(true, false, roughnessU, roughnessV);
			
			return Optional.of(new BSDF(intersection, new FresnelBlendBRDF(colorKD, colorKS, microfacetDistribution)));
		}
		
		return Optional.empty();
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
	 * Returns a {@code String} with the name of this {@code SubstrateMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code SubstrateMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SubstrateMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code SubstrateMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new SubstrateMaterial(%s, %s, %s, %s, %s, %s, %s)", this.textureKD, this.textureKS, this.textureEmission, this.textureRoughnessU, this.textureRoughnessV, Boolean.toString(this.isRemappingRoughness), this.modifier);
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
	 * Returns the {@link Texture} instance for the diffuse coefficient.
	 * 
	 * @return the {@code Texture} instance for the diffuse coefficient
	 */
	public Texture getTextureKD() {
		return this.textureKD;
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
				
				if(!this.textureKD.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureKS.accept(nodeHierarchicalVisitor)) {
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
	 * Compares {@code object} to this {@code SubstrateMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SubstrateMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SubstrateMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SubstrateMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SubstrateMaterial)) {
			return false;
		} else if(!Objects.equals(this.modifier, SubstrateMaterial.class.cast(object).modifier)) {
			return false;
		} else if(!Objects.equals(this.textureEmission, SubstrateMaterial.class.cast(object).textureEmission)) {
			return false;
		} else if(!Objects.equals(this.textureKD, SubstrateMaterial.class.cast(object).textureKD)) {
			return false;
		} else if(!Objects.equals(this.textureKS, SubstrateMaterial.class.cast(object).textureKS)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessU, SubstrateMaterial.class.cast(object).textureRoughnessU)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessV, SubstrateMaterial.class.cast(object).textureRoughnessV)) {
			return false;
		} else if(this.isRemappingRoughness != SubstrateMaterial.class.cast(object).isRemappingRoughness) {
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
	 * Returns an {@code int} with the ID of this {@code SubstrateMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code SubstrateMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code SubstrateMaterial} instance.
	 * 
	 * @return a hash code for this {@code SubstrateMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.modifier, this.textureEmission, this.textureKD, this.textureKS, this.textureRoughnessU, this.textureRoughnessV, Boolean.valueOf(this.isRemappingRoughness));
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code SubstrateMaterial} instance.
	 * 
	 * @return an {@code int[]} representation of this {@code SubstrateMaterial} instance
	 */
	public int[] toArray() {
		final int[] array = new int[ARRAY_LENGTH];
		
//		Because the SubstrateMaterial occupy 4/8 positions in a block, it should be aligned.
		array[ARRAY_OFFSET_TEXTURE_EMISSION] = pack(this.textureEmission.getID(), 0, 0, 0);																//Block #1
		array[ARRAY_OFFSET_TEXTURE_K_D_AND_TEXTURE_K_S] = pack(this.textureKD.getID(), 0, this.textureKS.getID(), 0);									//Block #1
		array[ARRAY_OFFSET_TEXTURE_ROUGHNESS_U_AND_TEXTURE_ROUGHNESS_V] = pack(this.textureRoughnessU.getID(), 0, this.textureRoughnessV.getID(), 0);	//Block #1
		array[ARRAY_OFFSET_IS_REMAPPING_ROUGHNESS] = this.isRemappingRoughness ? 1 : 0;																	//Block #1
		
		return array;
	}
}