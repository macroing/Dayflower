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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.scene.BSDF;
import org.dayflower.scene.BSSRDF;
import org.dayflower.scene.BXDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.LambertianBRDF;
import org.dayflower.scene.bxdf.LambertianBTDF;
import org.dayflower.scene.bxdf.TorranceSparrowBRDF;
import org.dayflower.scene.bxdf.TorranceSparrowBTDF;
import org.dayflower.scene.fresnel.DielectricFresnel;
import org.dayflower.scene.microfacet.MicrofacetDistribution;
import org.dayflower.scene.microfacet.TrowbridgeReitzMicrofacetDistribution;
import org.dayflower.scene.modifier.Modifier;
import org.dayflower.scene.modifier.NoOpModifier;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

import org.macroing.art4j.color.Color3F;

/**
 * A {@code TranslucentMaterial} is an implementation of {@link Material} that represents a translucent material.
 * <p>
 * This class is immutable and thread-safe as long as its two associated {@link Material} instances are.
 * <p>
 * This {@code Material} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class TranslucentMaterial implements Material {
	/**
	 * The name of this {@code TranslucentMaterial} class.
	 */
	public static final String NAME = "Translucent";
	
	/**
	 * The ID of this {@code TranslucentMaterial} class.
	 */
	public static final int ID = 15;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Modifier modifier;
	private final Texture textureEmission;
	private final Texture textureKD;
	private final Texture textureKS;
	private final Texture textureReflectance;
	private final Texture textureRoughness;
	private final Texture textureTransmittance;
	private final boolean isRemappingRoughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code TranslucentMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new TranslucentMaterial(new ConstantTexture(new Color3F(0.25F)));
	 * }
	 * </pre>
	 */
	public TranslucentMaterial() {
		this(new ConstantTexture(new Color3F(0.25F)));
	}
	
	/**
	 * Constructs a new {@code TranslucentMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKS}, {@code colorEmission}, {@code colorReflectance}, {@code colorTransmittance} or {@code modifier} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param floatRoughness a {@code float} for the roughness
	 * @param colorReflectance a {@code Color3F} instance for reflectance
	 * @param colorTransmittance a {@code Color3F} instance for transmittance
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @param modifier a {@link Modifier} instance
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKS}, {@code colorEmission}, {@code colorReflectance}, {@code colorTransmittance} or {@code modifier} are {@code null}
	 */
	public TranslucentMaterial(final Color3F colorKD, final Color3F colorKS, final Color3F colorEmission, final float floatRoughness, final Color3F colorReflectance, final Color3F colorTransmittance, final boolean isRemappingRoughness, final Modifier modifier) {
		this.textureKD = new ConstantTexture(Objects.requireNonNull(colorKD, "colorKD == null"));
		this.textureKS = new ConstantTexture(Objects.requireNonNull(colorKS, "colorKS == null"));
		this.textureEmission = new ConstantTexture(Objects.requireNonNull(colorEmission, "colorEmission == null"));
		this.textureReflectance = new ConstantTexture(Objects.requireNonNull(colorReflectance, "colorReflectance == null"));
		this.textureRoughness = new ConstantTexture(floatRoughness);
		this.textureTransmittance = new ConstantTexture(Objects.requireNonNull(colorTransmittance, "colorTransmittance == null"));
		this.isRemappingRoughness = isRemappingRoughness;
		this.modifier = Objects.requireNonNull(modifier, "modifier == null");
	}
	
	/**
	 * Constructs a new {@code TranslucentMaterial} instance.
	 * <p>
	 * If {@code textureKD} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new TranslucentMaterial(textureKD, new ConstantTexture(new Color3F(0.25F)));
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @throws NullPointerException thrown if, and only if, {@code textureKD} is {@code null}
	 */
	public TranslucentMaterial(final Texture textureKD) {
		this(textureKD, new ConstantTexture(new Color3F(0.25F)));
	}
	
	/**
	 * Constructs a new {@code TranslucentMaterial} instance.
	 * <p>
	 * If either {@code textureKD} or {@code textureKS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new TranslucentMaterial(textureKD, textureKS, new ConstantTexture(Color3F.BLACK));
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD} or {@code textureKS} are {@code null}
	 */
	public TranslucentMaterial(final Texture textureKD, final Texture textureKS) {
		this(textureKD, textureKS, new ConstantTexture(Color3F.BLACK));
	}
	
	/**
	 * Constructs a new {@code TranslucentMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKS} or {@code textureEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new TranslucentMaterial(textureKD, textureKS, textureEmission, new ConstantTexture(new Color3F(0.1F)));
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKS} or {@code textureEmission} are {@code null}
	 */
	public TranslucentMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission) {
		this(textureKD, textureKS, textureEmission, new ConstantTexture(new Color3F(0.1F)));
	}
	
	/**
	 * Constructs a new {@code TranslucentMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKS}, {@code textureEmission} or {@code textureRoughness} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new TranslucentMaterial(textureKD, textureKS, textureEmission, textureRoughness, new ConstantTexture(Color3F.GRAY));
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureRoughness a {@code Texture} instance for the roughness
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKS}, {@code textureEmission} or {@code textureRoughness} are {@code null}
	 */
	public TranslucentMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission, final Texture textureRoughness) {
		this(textureKD, textureKS, textureEmission, textureRoughness, new ConstantTexture(Color3F.GRAY));
	}
	
	/**
	 * Constructs a new {@code TranslucentMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKS}, {@code textureEmission}, {@code textureRoughness} or {@code textureReflectance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new TranslucentMaterial(textureKD, textureKS, textureEmission, textureRoughness, textureReflectance, new ConstantTexture(Color3F.GRAY));
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureRoughness a {@code Texture} instance for the roughness
	 * @param textureReflectance a {@code Texture} instance for the reflectance
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKS}, {@code textureEmission}, {@code textureRoughness} or {@code textureReflectance} are {@code null}
	 */
	public TranslucentMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission, final Texture textureRoughness, final Texture textureReflectance) {
		this(textureKD, textureKS, textureEmission, textureRoughness, textureReflectance, new ConstantTexture(Color3F.GRAY));
	}
	
	/**
	 * Constructs a new {@code TranslucentMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKS}, {@code textureEmission}, {@code textureRoughness}, {@code textureReflectance} or {@code textureTransmittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new TranslucentMaterial(textureKD, textureKS, textureEmission, textureRoughness, textureReflectance, textureTransmittance, true);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureRoughness a {@code Texture} instance for the roughness
	 * @param textureReflectance a {@code Texture} instance for the reflectance
	 * @param textureTransmittance a {@code Texture} instance for the transmittance
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKS}, {@code textureEmission}, {@code textureRoughness}, {@code textureReflectance} or {@code textureTransmittance} are {@code null}
	 */
	public TranslucentMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission, final Texture textureRoughness, final Texture textureReflectance, final Texture textureTransmittance) {
		this(textureKD, textureKS, textureEmission, textureRoughness, textureReflectance, textureTransmittance, true);
	}
	
	/**
	 * Constructs a new {@code TranslucentMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKS}, {@code textureEmission}, {@code textureRoughness}, {@code textureReflectance} or {@code textureTransmittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new TranslucentMaterial(textureKD, textureKS, textureEmission, textureRoughness, textureReflectance, textureTransmittance, isRemappingRoughness, new NoOpModifier());
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureRoughness a {@code Texture} instance for the roughness
	 * @param textureReflectance a {@code Texture} instance for the reflectance
	 * @param textureTransmittance a {@code Texture} instance for the transmittance
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKS}, {@code textureEmission}, {@code textureRoughness}, {@code textureReflectance} or {@code textureTransmittance} are {@code null}
	 */
	public TranslucentMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission, final Texture textureRoughness, final Texture textureReflectance, final Texture textureTransmittance, final boolean isRemappingRoughness) {
		this(textureKD, textureKS, textureEmission, textureRoughness, textureReflectance, textureTransmittance, isRemappingRoughness, new NoOpModifier());
	}
	
	/**
	 * Constructs a new {@code TranslucentMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKS}, {@code textureEmission}, {@code textureRoughness}, {@code textureReflectance}, {@code textureTransmittance} or {@code modifier} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureRoughness a {@code Texture} instance for the roughness
	 * @param textureReflectance a {@code Texture} instance for the reflectance
	 * @param textureTransmittance a {@code Texture} instance for the transmittance
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @param modifier a {@link Modifier} instance
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKS}, {@code textureEmission}, {@code textureRoughness}, {@code textureReflectance}, {@code textureTransmittance} or {@code modifier} are {@code null}
	 */
	public TranslucentMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission, final Texture textureRoughness, final Texture textureReflectance, final Texture textureTransmittance, final boolean isRemappingRoughness, final Modifier modifier) {
		this.textureKD = Objects.requireNonNull(textureKD, "textureKD == null");
		this.textureKS = Objects.requireNonNull(textureKS, "textureKS == null");
		this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
		this.textureRoughness = Objects.requireNonNull(textureRoughness, "textureRoughness == null");
		this.textureReflectance = Objects.requireNonNull(textureReflectance, "textureReflectance == null");
		this.textureTransmittance = Objects.requireNonNull(textureTransmittance, "textureTransmittance == null");
		this.isRemappingRoughness = isRemappingRoughness;
		this.modifier = Objects.requireNonNull(modifier, "modifier == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code TranslucentMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code TranslucentMaterial} instance at {@code intersection}
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
		
		final List<BXDF> bXDFs = new ArrayList<>();
		
		final Color3F colorReflectance = Color3F.saturate(this.textureReflectance.getColor(intersection), 0.0F, MAX_VALUE);
		final Color3F colorTransmittance = Color3F.saturate(this.textureTransmittance.getColor(intersection), 0.0F, MAX_VALUE);
		
		if(colorReflectance.isBlack() && colorTransmittance.isBlack()) {
			return Optional.empty();
		}
		
		final Color3F colorKD = Color3F.saturate(this.textureKD.getColor(intersection), 0.0F, MAX_VALUE);
		final Color3F colorKS = Color3F.saturate(this.textureKS.getColor(intersection), 0.0F, MAX_VALUE);
		
		if(!colorKD.isBlack()) {
			if(!colorReflectance.isBlack()) {
				bXDFs.add(new LambertianBRDF(Color3F.multiply(colorKD, colorReflectance)));
			}
			
			if(!colorTransmittance.isBlack()) {
				bXDFs.add(new LambertianBTDF(Color3F.multiply(colorKD, colorTransmittance)));
			}
		}
		
		if(!colorKS.isBlack()) {
			final float roughness = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(this.textureRoughness.getFloat(intersection)) : this.textureRoughness.getFloat(intersection);
			
			final MicrofacetDistribution microfacetDistribution = new TrowbridgeReitzMicrofacetDistribution(true, false, roughness, roughness);
			
			if(!colorReflectance.isBlack()) {
				bXDFs.add(new TorranceSparrowBRDF(Color3F.multiply(colorKS, colorReflectance), new DielectricFresnel(1.0F, 1.5F), microfacetDistribution));
			}
			
			if(!colorTransmittance.isBlack()) {
				bXDFs.add(new TorranceSparrowBTDF(Color3F.multiply(colorKS, colorTransmittance), microfacetDistribution, transportMode, 1.0F, 1.5F));
			}
		}
		
		if(bXDFs.size() > 0) {
			return Optional.of(new BSDF(intersection, bXDFs));
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
	 * Returns a {@code String} with the name of this {@code TranslucentMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code TranslucentMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code TranslucentMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code TranslucentMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new TranslucentMaterial(%s, %s, %s, %s, %s, %s, %s, %s)", this.textureKD, this.textureKS, this.textureEmission, this.textureRoughness, this.textureReflectance, this.textureTransmittance, Boolean.toString(this.isRemappingRoughness), this.modifier);
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
	 * Returns the {@link Texture} instance for the reflectance.
	 * 
	 * @return the {@code Texture} instance for the reflectance
	 */
	public Texture getTextureReflectance() {
		return this.textureReflectance;
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
	 * Returns the {@link Texture} instance for the transmittance.
	 * 
	 * @return the {@code Texture} instance for the transmittance
	 */
	public Texture getTextureTransmittance() {
		return this.textureTransmittance;
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
				
				if(!this.textureReflectance.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureRoughness.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureTransmittance.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code TranslucentMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code TranslucentMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code TranslucentMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code TranslucentMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof TranslucentMaterial)) {
			return false;
		} else if(!Objects.equals(this.modifier, TranslucentMaterial.class.cast(object).modifier)) {
			return false;
		} else if(!Objects.equals(this.textureEmission, TranslucentMaterial.class.cast(object).textureEmission)) {
			return false;
		} else if(!Objects.equals(this.textureKD, TranslucentMaterial.class.cast(object).textureKD)) {
			return false;
		} else if(!Objects.equals(this.textureKS, TranslucentMaterial.class.cast(object).textureKS)) {
			return false;
		} else if(!Objects.equals(this.textureReflectance, TranslucentMaterial.class.cast(object).textureReflectance)) {
			return false;
		} else if(!Objects.equals(this.textureRoughness, TranslucentMaterial.class.cast(object).textureRoughness)) {
			return false;
		} else if(!Objects.equals(this.textureTransmittance, TranslucentMaterial.class.cast(object).textureTransmittance)) {
			return false;
		} else if(this.isRemappingRoughness != TranslucentMaterial.class.cast(object).isRemappingRoughness) {
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
	 * Returns an {@code int} with the ID of this {@code TranslucentMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code TranslucentMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code TranslucentMaterial} instance.
	 * 
	 * @return a hash code for this {@code TranslucentMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.modifier, this.textureEmission, this.textureKD, this.textureKS, this.textureReflectance, this.textureRoughness, this.textureTransmittance, Boolean.valueOf(this.isRemappingRoughness));
	}
}