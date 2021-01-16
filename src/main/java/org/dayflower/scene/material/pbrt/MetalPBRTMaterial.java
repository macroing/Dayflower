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

import java.util.Objects;
import java.util.Optional;

import org.dayflower.image.Color3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.scene.BSSRDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.pbrt.PBRTBSDF;
import org.dayflower.scene.bxdf.pbrt.TorranceSparrowPBRTBRDF;
import org.dayflower.scene.fresnel.ConductorFresnel;
import org.dayflower.scene.fresnel.Fresnel;
import org.dayflower.scene.microfacet.MicrofacetDistribution;
import org.dayflower.scene.microfacet.TrowbridgeReitzMicrofacetDistribution;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

/**
 * A {@code MetalPBRTMaterial} is an implementation of {@link PBRTMaterial} that represents metal.
 * <p>
 * This class is immutable and thread-safe as long as all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MetalPBRTMaterial implements PBRTMaterial {
	/**
	 * The name of this {@code MetalPBRTMaterial} class.
	 */
	public static final String NAME = "PBRT - Metal";
	
	/**
	 * The ID of this {@code MetalPBRTMaterial} class.
	 */
	public static final int ID = 104;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Texture textureEmission;
	private final Texture textureEta;
	private final Texture textureK;
	private final Texture textureRoughnessU;
	private final Texture textureRoughnessV;
	private final boolean isRemappingRoughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MetalPBRTMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MetalPBRTMaterial(Color3F.GOLD_K_MAXIMUM_TO_1);
	 * }
	 * </pre>
	 */
	public MetalPBRTMaterial() {
		this(Color3F.GOLD_K_MAXIMUM_TO_1);
	}
	
	/**
	 * Constructs a new {@code MetalPBRTMaterial} instance.
	 * <p>
	 * If {@code colorK} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MetalPBRTMaterial(colorK, Color3F.GOLD_ETA_MAXIMUM_TO_1);
	 * }
	 * </pre>
	 * 
	 * @param colorK a {@link Color3F} instance for the absorption coefficient
	 * @throws NullPointerException thrown if, and only if, {@code colorK} is {@code null}
	 */
	public MetalPBRTMaterial(final Color3F colorK) {
		this(colorK, Color3F.GOLD_ETA_MAXIMUM_TO_1);
	}
	
	/**
	 * Constructs a new {@code MetalPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorK} or {@code colorEta} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MetalPBRTMaterial(colorK, colorEta, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorK a {@link Color3F} instance for the absorption coefficient
	 * @param colorEta a {@code Color3F} instance for the index of refraction (IOR)
	 * @throws NullPointerException thrown if, and only if, either {@code colorK} or {@code colorEta} are {@code null}
	 */
	public MetalPBRTMaterial(final Color3F colorK, final Color3F colorEta) {
		this(colorK, colorEta, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code MetalPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorK}, {@code colorEta} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MetalPBRTMaterial(colorK, colorEta, colorEmission, 0.01F);
	 * }
	 * </pre>
	 * 
	 * @param colorK a {@link Color3F} instance for the absorption coefficient
	 * @param colorEta a {@code Color3F} instance for the index of refraction (IOR)
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code colorK}, {@code colorEta} or {@code colorEmission} are {@code null}
	 */
	public MetalPBRTMaterial(final Color3F colorK, final Color3F colorEta, final Color3F colorEmission) {
		this(colorK, colorEta, colorEmission, 0.01F);
	}
	
	/**
	 * Constructs a new {@code MetalPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorK}, {@code colorEta} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MetalPBRTMaterial(colorK, colorEta, colorEmission, floatRoughness, floatRoughness);
	 * }
	 * </pre>
	 * 
	 * @param colorK a {@link Color3F} instance for the absorption coefficient
	 * @param colorEta a {@code Color3F} instance for the index of refraction (IOR)
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param floatRoughness a {@code float} for the roughness along the U-axis and the V-axis
	 * @throws NullPointerException thrown if, and only if, either {@code colorK}, {@code colorEta} or {@code colorEmission} are {@code null}
	 */
	public MetalPBRTMaterial(final Color3F colorK, final Color3F colorEta, final Color3F colorEmission, final float floatRoughness) {
		this(colorK, colorEta, colorEmission, floatRoughness, floatRoughness);
	}
	
	/**
	 * Constructs a new {@code MetalPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorK}, {@code colorEta} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MetalPBRTMaterial(colorK, colorEta, colorEmission, floatRoughnessU, floatRoughnessV, true);
	 * }
	 * </pre>
	 * 
	 * @param colorK a {@link Color3F} instance for the absorption coefficient
	 * @param colorEta a {@code Color3F} instance for the index of refraction (IOR)
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param floatRoughnessU a {@code float} for the roughness along the U-axis
	 * @param floatRoughnessV a {@code float} for the roughness along the V-axis
	 * @throws NullPointerException thrown if, and only if, either {@code colorK}, {@code colorEta} or {@code colorEmission} are {@code null}
	 */
	public MetalPBRTMaterial(final Color3F colorK, final Color3F colorEta, final Color3F colorEmission, final float floatRoughnessU, final float floatRoughnessV) {
		this(colorK, colorEta, colorEmission, floatRoughnessU, floatRoughnessV, true);
	}
	
	/**
	 * Constructs a new {@code MetalPBRTMaterial} instance.
	 * <p>
	 * If either {@code colorK}, {@code colorEta} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorK a {@link Color3F} instance for the absorption coefficient
	 * @param colorEta a {@code Color3F} instance for the index of refraction (IOR)
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param floatRoughnessU a {@code float} for the roughness along the U-axis
	 * @param floatRoughnessV a {@code float} for the roughness along the V-axis
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code colorK}, {@code colorEta} or {@code colorEmission} are {@code null}
	 */
	public MetalPBRTMaterial(final Color3F colorK, final Color3F colorEta, final Color3F colorEmission, final float floatRoughnessU, final float floatRoughnessV, final boolean isRemappingRoughness) {
		this.textureK = new ConstantTexture(Objects.requireNonNull(colorK, "colorK == null"));
		this.textureEta = new ConstantTexture(Objects.requireNonNull(colorEta, "colorEta == null"));
		this.textureEmission = new ConstantTexture(Objects.requireNonNull(colorEmission, "colorEmission == null"));
		this.textureRoughnessU = new ConstantTexture(floatRoughnessU);
		this.textureRoughnessV = new ConstantTexture(floatRoughnessV);
		this.isRemappingRoughness = isRemappingRoughness;
	}
	
	/**
	 * Constructs a new {@code MetalPBRTMaterial} instance.
	 * <p>
	 * If {@code textureK} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MetalPBRTMaterial(textureK, ConstantTexture.GOLD_ETA_MAXIMUM_TO_1);
	 * }
	 * </pre>
	 * 
	 * @param textureK a {@link Texture} instance for the absorption coefficient
	 * @throws NullPointerException thrown if, and only if, {@code textureK} is {@code null}
	 */
	public MetalPBRTMaterial(final Texture textureK) {
		this(textureK, ConstantTexture.GOLD_ETA_MAXIMUM_TO_1);
	}
	
	/**
	 * Constructs a new {@code MetalPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureK} or {@code textureEta} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MetalPBRTMaterial(textureK, textureEta, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureK a {@link Texture} instance for the absorption coefficient
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @throws NullPointerException thrown if, and only if, either {@code textureK} or {@code textureEta} are {@code null}
	 */
	public MetalPBRTMaterial(final Texture textureK, final Texture textureEta) {
		this(textureK, textureEta, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code MetalPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureK}, {@code textureEta} or {@code textureEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MetalPBRTMaterial(textureK, textureEta, textureEmission, ConstantTexture.GRAY_0_01);
	 * }
	 * </pre>
	 * 
	 * @param textureK a {@link Texture} instance for the absorption coefficient
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureEmission a {@code Texture} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code textureK}, {@code textureEta} or {@code textureEmission} are {@code null}
	 */
	public MetalPBRTMaterial(final Texture textureK, final Texture textureEta, final Texture textureEmission) {
		this(textureK, textureEta, textureEmission, ConstantTexture.GRAY_0_01);
	}
	
	/**
	 * Constructs a new {@code MetalPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureK}, {@code textureEta}, {@code textureEmission} or {@code textureRoughness} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MetalPBRTMaterial(textureK, textureEta, textureEmission, textureRoughness, textureRoughness);
	 * }
	 * </pre>
	 * 
	 * @param textureK a {@link Texture} instance for the absorption coefficient
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureRoughness a {@code Texture} instance for the roughness along the U-axis and the V-axis
	 * @throws NullPointerException thrown if, and only if, either {@code textureK}, {@code textureEta}, {@code textureEmission} or {@code textureRoughness} are {@code null}
	 */
	public MetalPBRTMaterial(final Texture textureK, final Texture textureEta, final Texture textureEmission, final Texture textureRoughness) {
		this(textureK, textureEta, textureEmission, textureRoughness, textureRoughness);
	}
	
	/**
	 * Constructs a new {@code MetalPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureK}, {@code textureEta}, {@code textureEmission}, {@code textureRoughnessU} or {@code textureRoughnessV} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MetalPBRTMaterial(textureK, textureEta, textureEmission, textureRoughnessU, textureRoughnessV, true);
	 * }
	 * </pre>
	 * 
	 * @param textureK a {@link Texture} instance for the absorption coefficient
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureRoughnessU a {@code Texture} instance for the roughness along the U-axis
	 * @param textureRoughnessV a {@code Texture} instance for the roughness along the V-axis
	 * @throws NullPointerException thrown if, and only if, either {@code textureK}, {@code textureEta}, {@code textureEmission}, {@code textureRoughnessU} or {@code textureRoughnessV} are {@code null}
	 */
	public MetalPBRTMaterial(final Texture textureK, final Texture textureEta, final Texture textureEmission, final Texture textureRoughnessU, final Texture textureRoughnessV) {
		this(textureK, textureEta, textureEmission, textureRoughnessU, textureRoughnessV, true);
	}
	
	/**
	 * Constructs a new {@code MetalPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureK}, {@code textureEta}, {@code textureEmission}, {@code textureRoughnessU} or {@code textureRoughnessV} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureK a {@link Texture} instance for the absorption coefficient
	 * @param textureEta a {@code Texture} instance for the index of refraction (IOR)
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureRoughnessU a {@code Texture} instance for the roughness along the U-axis
	 * @param textureRoughnessV a {@code Texture} instance for the roughness along the V-axis
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code textureK}, {@code textureEta}, {@code textureEmission}, {@code textureRoughnessU} or {@code textureRoughnessV} are {@code null}
	 */
	public MetalPBRTMaterial(final Texture textureK, final Texture textureEta, final Texture textureEmission, final Texture textureRoughnessU, final Texture textureRoughnessV, final boolean isRemappingRoughness) {
		this.textureK = Objects.requireNonNull(textureK, "textureK == null");
		this.textureEta = Objects.requireNonNull(textureEta, "textureEta == null");
		this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
		this.textureRoughnessU = Objects.requireNonNull(textureRoughnessU, "textureRoughnessU == null");
		this.textureRoughnessV = Objects.requireNonNull(textureRoughnessV, "textureRoughnessV == null");
		this.isRemappingRoughness = isRemappingRoughness;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code MetalPBRTMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code MetalPBRTMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F emittance(final Intersection intersection) {
		return this.textureEmission.getColor(intersection);
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
	 * Computes the {@link PBRTBSDF} at {@code intersection}.
	 * <p>
	 * Returns an optional {@code PBRTBSDF} instance.
	 * <p>
	 * If either {@code intersection} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection the {@link Intersection} to compute the {@code PBRTBSDF} for
	 * @param transportMode the {@link TransportMode} to use
	 * @param isAllowingMultipleLobes {@code true} if, and only if, multiple lobes are allowed, {@code false} otherwise
	 * @return an optional {@code PBRTBSDF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code transportMode} are {@code null}
	 */
	@Override
	public Optional<PBRTBSDF> computeBSDF(final Intersection intersection, final TransportMode transportMode, final boolean isAllowingMultipleLobes) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(transportMode, "transportMode == null");
		
		final Color3F colorEtaI = Color3F.WHITE;
		final Color3F colorEtaT = this.textureEta.getColor(intersection);
		final Color3F colorK = this.textureK.getColor(intersection);
		
		final float roughnessU = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(this.textureRoughnessU.getFloat(intersection)) : this.textureRoughnessU.getFloat(intersection);
		final float roughnessV = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(this.textureRoughnessV.getFloat(intersection)) : this.textureRoughnessV.getFloat(intersection);
		
		final Fresnel fresnel = new ConductorFresnel(colorEtaI, colorEtaT, colorK);
		
		final MicrofacetDistribution microfacetDistribution = new TrowbridgeReitzMicrofacetDistribution(true, false, roughnessU, roughnessV);
		
		return Optional.of(new PBRTBSDF(intersection, new TorranceSparrowPBRTBRDF(colorEtaI, fresnel, microfacetDistribution)));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code MetalPBRTMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code MetalPBRTMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code MetalPBRTMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code MetalPBRTMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new MetalPBRTMaterial(%s, %s, %s, %s, %s, %s)", this.textureK, this.textureEta, this.textureEmission, this.textureRoughnessU, this.textureRoughnessV, Boolean.toString(this.isRemappingRoughness));
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
	 * Returns the {@link Texture} instance for the absorption coefficient.
	 * 
	 * @return the {@code Texture} instance for the absorption coefficient
	 */
	public Texture getTextureK() {
		return this.textureK;
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
				
				if(!this.textureK.accept(nodeHierarchicalVisitor)) {
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
	 * Compares {@code object} to this {@code MetalPBRTMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code MetalPBRTMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code MetalPBRTMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code MetalPBRTMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof MetalPBRTMaterial)) {
			return false;
		} else if(!Objects.equals(this.textureEmission, MetalPBRTMaterial.class.cast(object).textureEmission)) {
			return false;
		} else if(!Objects.equals(this.textureEta, MetalPBRTMaterial.class.cast(object).textureEta)) {
			return false;
		} else if(!Objects.equals(this.textureK, MetalPBRTMaterial.class.cast(object).textureK)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessU, MetalPBRTMaterial.class.cast(object).textureRoughnessU)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessV, MetalPBRTMaterial.class.cast(object).textureRoughnessV)) {
			return false;
		} else if(this.isRemappingRoughness != MetalPBRTMaterial.class.cast(object).isRemappingRoughness) {
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
	 * Returns an {@code int} with the ID of this {@code MetalPBRTMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code MetalPBRTMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code MetalPBRTMaterial} instance.
	 * 
	 * @return a hash code for this {@code MetalPBRTMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureEmission, this.textureEta, this.textureK, this.textureRoughnessU, this.textureRoughnessV, Boolean.valueOf(this.isRemappingRoughness));
	}
}