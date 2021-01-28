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
import org.dayflower.scene.bxdf.pbrt.TorranceSparrowPBRTBRDF;
import org.dayflower.scene.fresnel.DielectricFresnel;
import org.dayflower.scene.fresnel.Fresnel;
import org.dayflower.scene.microfacet.MicrofacetDistribution;
import org.dayflower.scene.microfacet.TrowbridgeReitzMicrofacetDistribution;
import org.dayflower.scene.modifier.Modifier;
import org.dayflower.scene.modifier.NoOpModifier;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

/**
 * A {@code PlasticPBRTMaterial} is an implementation of {@link Material} that represents plastic.
 * <p>
 * This class is immutable and thread-safe as long as the {@link Modifier} instance and all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PlasticPBRTMaterial implements Material {
	/**
	 * The name of this {@code PlasticPBRTMaterial} class.
	 */
	public static final String NAME = "PBRT - Plastic";
	
	/**
	 * The ID of this {@code PlasticPBRTMaterial} class.
	 */
	public static final int ID = 106;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Modifier modifier;
	private final Texture textureEmission;
	private final Texture textureKD;
	private final Texture textureKS;
	private final Texture textureRoughness;
	private final boolean isRemappingRoughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PlasticPBRTMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticPBRTMaterial(Color3F.GRAY_0_25);
	 * }
	 * </pre>
	 */
	public PlasticPBRTMaterial() {
		this(Color3F.GRAY_0_25);
	}
	
	/**
	 * Constructs a new {@code PlasticPBRTMaterial} instance.
	 * <p>
	 * If {@code colorKD} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticPBRTMaterial(colorKD, Color3F.GRAY_0_25);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @throws NullPointerException thrown if, and only if, {@code colorKD} is {@code null}
	 */
	public PlasticPBRTMaterial(final Color3F colorKD) {
		this(colorKD, Color3F.GRAY_0_25);
	}
	
	/**
	 * Constructs a new {@code PlasticPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKD} or {@code colorKS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticPBRTMaterial(colorKD, colorKS, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD} or {@code colorKS} are {@code null}
	 */
	public PlasticPBRTMaterial(final Color3F colorKD, final Color3F colorKS) {
		this(colorKD, colorKS, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code PlasticPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticPBRTMaterial(colorKD, colorKS, colorEmission, 0.1F);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}
	 */
	public PlasticPBRTMaterial(final Color3F colorKD, final Color3F colorKS, final Color3F colorEmission) {
		this(colorKD, colorKS, colorEmission, 0.1F);
	}
	
	/**
	 * Constructs a new {@code PlasticPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticPBRTMaterial(colorKD, colorKS, colorEmission, floatRoughness, true);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param floatRoughness a {@code float} for the roughness
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}
	 */
	public PlasticPBRTMaterial(final Color3F colorKD, final Color3F colorKS, final Color3F colorEmission, final float floatRoughness) {
		this(colorKD, colorKS, colorEmission, floatRoughness, true);
	}
	
	/**
	 * Constructs a new {@code PlasticPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticPBRTMaterial(colorKD, colorKS, colorEmission, floatRoughness, isRemappingRoughness, new NoOpModifier());
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param floatRoughness a {@code float} for the roughness
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}
	 */
	public PlasticPBRTMaterial(final Color3F colorKD, final Color3F colorKS, final Color3F colorEmission, final float floatRoughness, final boolean isRemappingRoughness) {
		this(colorKD, colorKS, colorEmission, floatRoughness, isRemappingRoughness, new NoOpModifier());
	}
	
	/**
	 * Constructs a new {@code PlasticPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKS}, {@code colorEmission} or {@code modifier} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param floatRoughness a {@code float} for the roughness
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @param modifier a {@link Modifier} instance
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKS}, {@code colorEmission} or {@code modifier} are {@code null}
	 */
	public PlasticPBRTMaterial(final Color3F colorKD, final Color3F colorKS, final Color3F colorEmission, final float floatRoughness, final boolean isRemappingRoughness, final Modifier modifier) {
		this.textureKD = new ConstantTexture(Objects.requireNonNull(colorKD, "colorKD == null"));
		this.textureKS = new ConstantTexture(Objects.requireNonNull(colorKS, "colorKS == null"));
		this.textureEmission = new ConstantTexture(Objects.requireNonNull(colorEmission, "colorEmission == null"));
		this.textureRoughness = new ConstantTexture(floatRoughness);
		this.isRemappingRoughness = isRemappingRoughness;
		this.modifier = Objects.requireNonNull(modifier, "modifier == null");
	}
	
	/**
	 * Constructs a new {@code PlasticPBRTMaterial} instance.
	 * <p>
	 * If {@code textureKD} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticPBRTMaterial(textureKD, ConstantTexture.GRAY_0_25);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @throws NullPointerException thrown if, and only if, {@code textureKD} is {@code null}
	 */
	public PlasticPBRTMaterial(final Texture textureKD) {
		this(textureKD, ConstantTexture.GRAY_0_25);
	}
	
	/**
	 * Constructs a new {@code PlasticPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKD} or {@code textureKS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticPBRTMaterial(textureKD, textureKS, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD} or {@code textureKS} are {@code null}
	 */
	public PlasticPBRTMaterial(final Texture textureKD, final Texture textureKS) {
		this(textureKD, textureKS, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code PlasticPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKS} or {@code textureEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticPBRTMaterial(textureKD, textureKS, textureEmission, ConstantTexture.GRAY_0_10);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKS} or {@code textureEmission} are {@code null}
	 */
	public PlasticPBRTMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission) {
		this(textureKD, textureKS, textureEmission, ConstantTexture.GRAY_0_10);
	}
	
	/**
	 * Constructs a new {@code PlasticPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKS}, {@code textureEmission} or {@code textureRoughness} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticPBRTMaterial(textureKD, textureKS, textureEmission, textureRoughness, true);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureRoughness a {@code Texture} instance for the roughness
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKS}, {@code textureEmission} or {@code textureRoughness} are {@code null}
	 */
	public PlasticPBRTMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission, final Texture textureRoughness) {
		this(textureKD, textureKS, textureEmission, textureRoughness, true);
	}
	
	/**
	 * Constructs a new {@code PlasticPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKS}, {@code textureEmission} or {@code textureRoughness} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticPBRTMaterial(textureKD, textureKS, textureEmission, textureRoughness, isRemappingRoughness, new NoOpModifier());
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureRoughness a {@code Texture} instance for the roughness
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKS}, {@code textureEmission} or {@code textureRoughness} are {@code null}
	 */
	public PlasticPBRTMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission, final Texture textureRoughness, final boolean isRemappingRoughness) {
		this(textureKD, textureKS, textureEmission, textureRoughness, isRemappingRoughness, new NoOpModifier());
	}
	
	/**
	 * Constructs a new {@code PlasticPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKS}, {@code textureEmission}, {@code textureRoughness} or {@code modifier} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureRoughness a {@code Texture} instance for the roughness
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @param modifier a {@link Modifier} instance
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKS}, {@code textureEmission}, {@code textureRoughness} or {@code modifier} are {@code null}
	 */
	public PlasticPBRTMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission, final Texture textureRoughness, final boolean isRemappingRoughness, final Modifier modifier) {
		this.textureKD = Objects.requireNonNull(textureKD, "textureKD == null");
		this.textureKS = Objects.requireNonNull(textureKS, "textureKS == null");
		this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
		this.textureRoughness = Objects.requireNonNull(textureRoughness, "textureRoughness == null");
		this.isRemappingRoughness = isRemappingRoughness;
		this.modifier = Objects.requireNonNull(modifier, "modifier == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code PlasticPBRTMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code PlasticPBRTMaterial} instance at {@code intersection}
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
		
		final List<BXDF> bXDFs = new ArrayList<>();
		
		final Color3F colorKD = Color3F.saturate(this.textureKD.getColor(intersection), 0.0F, Float.MAX_VALUE);
		final Color3F colorKS = Color3F.saturate(this.textureKS.getColor(intersection), 0.0F, Float.MAX_VALUE);
		
		if(!colorKD.isBlack()) {
			bXDFs.add(new LambertianBRDF(colorKD));
		}
		
		if(!colorKS.isBlack()) {
			final Fresnel fresnel = new DielectricFresnel(1.5F, 1.0F);
			
			final float roughness = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(this.textureRoughness.getFloat(intersection)) : this.textureRoughness.getFloat(intersection);
			
			final MicrofacetDistribution microfacetDistribution = new TrowbridgeReitzMicrofacetDistribution(true, false, roughness, roughness);
			
			bXDFs.add(new TorranceSparrowPBRTBRDF(colorKS, fresnel, microfacetDistribution));
		}
		
		if(bXDFs.size() > 0) {
			return Optional.of(new BSDF(intersection, bXDFs, false));
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
	 * Returns a {@code String} with the name of this {@code PlasticPBRTMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code PlasticPBRTMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code PlasticPBRTMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code PlasticPBRTMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new PlasticPBRTMaterial(%s, %s, %s, %s, %s, %s)", this.textureKD, this.textureKS, this.textureEmission, this.textureRoughness, Boolean.toString(this.isRemappingRoughness), this.modifier);
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
	 * Returns the {@link Texture} instance for the roughness.
	 * 
	 * @return the {@code Texture} instance for the roughness
	 */
	public Texture getTextureRoughness() {
		return this.textureRoughness;
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
				
				if(!this.textureRoughness.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code PlasticPBRTMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code PlasticPBRTMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code PlasticPBRTMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code PlasticPBRTMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof PlasticPBRTMaterial)) {
			return false;
		} else if(!Objects.equals(this.modifier, PlasticPBRTMaterial.class.cast(object).modifier)) {
			return false;
		} else if(!Objects.equals(this.textureEmission, PlasticPBRTMaterial.class.cast(object).textureEmission)) {
			return false;
		} else if(!Objects.equals(this.textureKD, PlasticPBRTMaterial.class.cast(object).textureKD)) {
			return false;
		} else if(!Objects.equals(this.textureKS, PlasticPBRTMaterial.class.cast(object).textureKS)) {
			return false;
		} else if(!Objects.equals(this.textureRoughness, PlasticPBRTMaterial.class.cast(object).textureRoughness)) {
			return false;
		} else if(this.isRemappingRoughness != PlasticPBRTMaterial.class.cast(object).isRemappingRoughness) {
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
	 * Returns an {@code int} with the ID of this {@code PlasticPBRTMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code PlasticPBRTMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code PlasticPBRTMaterial} instance.
	 * 
	 * @return a hash code for this {@code PlasticPBRTMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.modifier, this.textureEmission, this.textureKD, this.textureKS, this.textureRoughness, Boolean.valueOf(this.isRemappingRoughness));
	}
}