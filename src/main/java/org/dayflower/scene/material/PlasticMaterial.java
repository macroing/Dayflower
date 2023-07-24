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

import org.dayflower.scene.BSDF;
import org.dayflower.scene.BXDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.ScatteringFunctions;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.LambertianBRDF;
import org.dayflower.scene.bxdf.TorranceSparrowBRDF;
import org.dayflower.scene.fresnel.DielectricFresnel;
import org.dayflower.scene.fresnel.Fresnel;
import org.dayflower.scene.microfacet.MicrofacetDistribution;
import org.dayflower.scene.microfacet.TrowbridgeReitzMicrofacetDistribution;
import org.dayflower.scene.modifier.Modifier;
import org.dayflower.scene.modifier.NoOpModifier;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

import org.macroing.art4j.color.Color3F;
import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;

/**
 * A {@code PlasticMaterial} is an implementation of {@link Material} that represents plastic.
 * <p>
 * This class is immutable and thread-safe as long as the {@link Modifier} instance and all {@link Texture} instances are.
 * <p>
 * This {@code Material} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PlasticMaterial implements Material {
	/**
	 * The name of this {@code PlasticMaterial} class.
	 */
	public static final String NAME = "Plastic";
	
	/**
	 * The ID of this {@code PlasticMaterial} class.
	 */
	public static final int ID = 13;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Modifier modifier;
	private final Texture textureEmission;
	private final Texture textureKD;
	private final Texture textureKS;
	private final Texture textureRoughness;
	private final boolean isRemappingRoughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PlasticMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticMaterial(new Color3F(0.2F, 0.2F, 0.5F));
	 * }
	 * </pre>
	 */
	public PlasticMaterial() {
		this(new Color3F(0.2F, 0.2F, 0.5F));
	}
	
	/**
	 * Constructs a new {@code PlasticMaterial} instance.
	 * <p>
	 * If {@code colorKD} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticMaterial(colorKD, Color3F.GRAY);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @throws NullPointerException thrown if, and only if, {@code colorKD} is {@code null}
	 */
	public PlasticMaterial(final Color3F colorKD) {
		this(colorKD, Color3F.GRAY);
	}
	
	/**
	 * Constructs a new {@code PlasticMaterial} instance.
	 * <p>
	 * If either {@code colorKD} or {@code colorKS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticMaterial(colorKD, colorKS, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD} or {@code colorKS} are {@code null}
	 */
	public PlasticMaterial(final Color3F colorKD, final Color3F colorKS) {
		this(colorKD, colorKS, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code PlasticMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticMaterial(colorKD, colorKS, colorEmission, 0.025F);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}
	 */
	public PlasticMaterial(final Color3F colorKD, final Color3F colorKS, final Color3F colorEmission) {
		this(colorKD, colorKS, colorEmission, 0.025F);
	}
	
	/**
	 * Constructs a new {@code PlasticMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticMaterial(colorKD, colorKS, colorEmission, floatRoughness, true);
	 * }
	 * </pre>
	 * 
	 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
	 * @param colorKS a {@code Color3F} instance for the specular coefficient
	 * @param colorEmission a {@code Color3F} instance for emission
	 * @param floatRoughness a {@code float} for the roughness
	 * @throws NullPointerException thrown if, and only if, either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}
	 */
	public PlasticMaterial(final Color3F colorKD, final Color3F colorKS, final Color3F colorEmission, final float floatRoughness) {
		this(colorKD, colorKS, colorEmission, floatRoughness, true);
	}
	
	/**
	 * Constructs a new {@code PlasticMaterial} instance.
	 * <p>
	 * If either {@code colorKD}, {@code colorKS} or {@code colorEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticMaterial(colorKD, colorKS, colorEmission, floatRoughness, isRemappingRoughness, new NoOpModifier());
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
	public PlasticMaterial(final Color3F colorKD, final Color3F colorKS, final Color3F colorEmission, final float floatRoughness, final boolean isRemappingRoughness) {
		this(colorKD, colorKS, colorEmission, floatRoughness, isRemappingRoughness, new NoOpModifier());
	}
	
	/**
	 * Constructs a new {@code PlasticMaterial} instance.
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
	public PlasticMaterial(final Color3F colorKD, final Color3F colorKS, final Color3F colorEmission, final float floatRoughness, final boolean isRemappingRoughness, final Modifier modifier) {
		this.textureKD = new ConstantTexture(Objects.requireNonNull(colorKD, "colorKD == null"));
		this.textureKS = new ConstantTexture(Objects.requireNonNull(colorKS, "colorKS == null"));
		this.textureEmission = new ConstantTexture(Objects.requireNonNull(colorEmission, "colorEmission == null"));
		this.textureRoughness = new ConstantTexture(floatRoughness);
		this.isRemappingRoughness = isRemappingRoughness;
		this.modifier = Objects.requireNonNull(modifier, "modifier == null");
	}
	
	/**
	 * Constructs a new {@code PlasticMaterial} instance.
	 * <p>
	 * If {@code textureKD} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticMaterial(textureKD, ConstantTexture.GRAY_0_50);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @throws NullPointerException thrown if, and only if, {@code textureKD} is {@code null}
	 */
	public PlasticMaterial(final Texture textureKD) {
		this(textureKD, ConstantTexture.GRAY_0_50);
	}
	
	/**
	 * Constructs a new {@code PlasticMaterial} instance.
	 * <p>
	 * If either {@code textureKD} or {@code textureKS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticMaterial(textureKD, textureKS, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD} or {@code textureKS} are {@code null}
	 */
	public PlasticMaterial(final Texture textureKD, final Texture textureKS) {
		this(textureKD, textureKS, ConstantTexture.BLACK);
	}
	
	/**
	 * Constructs a new {@code PlasticMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKS} or {@code textureEmission} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticMaterial(textureKD, textureKS, textureEmission, new ConstantTexture(new Color3F(0.025F)));
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKS} or {@code textureEmission} are {@code null}
	 */
	public PlasticMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission) {
		this(textureKD, textureKS, textureEmission, new ConstantTexture(new Color3F(0.025F)));
	}
	
	/**
	 * Constructs a new {@code PlasticMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKS}, {@code textureEmission} or {@code textureRoughness} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticMaterial(textureKD, textureKS, textureEmission, textureRoughness, true);
	 * }
	 * </pre>
	 * 
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKS a {@code Texture} instance for the specular coefficient
	 * @param textureEmission a {@code Texture} instance for emission
	 * @param textureRoughness a {@code Texture} instance for the roughness
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKS}, {@code textureEmission} or {@code textureRoughness} are {@code null}
	 */
	public PlasticMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission, final Texture textureRoughness) {
		this(textureKD, textureKS, textureEmission, textureRoughness, true);
	}
	
	/**
	 * Constructs a new {@code PlasticMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKS}, {@code textureEmission} or {@code textureRoughness} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticMaterial(textureKD, textureKS, textureEmission, textureRoughness, isRemappingRoughness, new NoOpModifier());
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
	public PlasticMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission, final Texture textureRoughness, final boolean isRemappingRoughness) {
		this(textureKD, textureKS, textureEmission, textureRoughness, isRemappingRoughness, new NoOpModifier());
	}
	
	/**
	 * Constructs a new {@code PlasticMaterial} instance.
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
	public PlasticMaterial(final Texture textureKD, final Texture textureKS, final Texture textureEmission, final Texture textureRoughness, final boolean isRemappingRoughness, final Modifier modifier) {
		this.textureKD = Objects.requireNonNull(textureKD, "textureKD == null");
		this.textureKS = Objects.requireNonNull(textureKS, "textureKS == null");
		this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
		this.textureRoughness = Objects.requireNonNull(textureRoughness, "textureRoughness == null");
		this.isRemappingRoughness = isRemappingRoughness;
		this.modifier = Objects.requireNonNull(modifier, "modifier == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code PlasticMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code PlasticMaterial} instance at {@code intersection}
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
	 * Computes the {@link ScatteringFunctions} at {@code intersection}.
	 * <p>
	 * Returns a {@code ScatteringFunctions} instance.
	 * <p>
	 * If either {@code intersection} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection the {@link Intersection} to compute the {@code ScatteringFunctions} for
	 * @param transportMode the {@link TransportMode} to use
	 * @param isAllowingMultipleLobes {@code true} if, and only if, multiple lobes are allowed, {@code false} otherwise
	 * @return a {@code ScatteringFunctions} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code transportMode} are {@code null}
	 */
	@Override
	public ScatteringFunctions computeScatteringFunctions(final Intersection intersection, final TransportMode transportMode, final boolean isAllowingMultipleLobes) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(transportMode, "transportMode == null");
		
		this.modifier.modify(intersection);
		
		final List<BXDF> bXDFs = new ArrayList<>();
		
		final Color3F colorKD = Color3F.saturate(this.textureKD.getColor(intersection), 0.0F, MAX_VALUE);
		final Color3F colorKS = Color3F.saturate(this.textureKS.getColor(intersection), 0.0F, MAX_VALUE);
		
		if(!colorKD.isBlack()) {
			bXDFs.add(new LambertianBRDF(colorKD));
		}
		
		if(!colorKS.isBlack()) {
			final Fresnel fresnel = new DielectricFresnel(1.5F, 1.0F);
			
			final float roughness = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(this.textureRoughness.getFloat(intersection)) : this.textureRoughness.getFloat(intersection);
			
			final MicrofacetDistribution microfacetDistribution = new TrowbridgeReitzMicrofacetDistribution(true, false, roughness, roughness);
			
			bXDFs.add(new TorranceSparrowBRDF(colorKS, fresnel, microfacetDistribution));
		}
		
		if(bXDFs.size() > 0) {
			return new ScatteringFunctions(new BSDF(intersection, bXDFs));
		}
		
		return new ScatteringFunctions();
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code PlasticMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code PlasticMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code PlasticMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code PlasticMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new PlasticMaterial(%s, %s, %s, %s, %s, %s)", this.textureKD, this.textureKS, this.textureEmission, this.textureRoughness, Boolean.toString(this.isRemappingRoughness), this.modifier);
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
	 * Compares {@code object} to this {@code PlasticMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code PlasticMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code PlasticMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code PlasticMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof PlasticMaterial)) {
			return false;
		} else if(!Objects.equals(this.modifier, PlasticMaterial.class.cast(object).modifier)) {
			return false;
		} else if(!Objects.equals(this.textureEmission, PlasticMaterial.class.cast(object).textureEmission)) {
			return false;
		} else if(!Objects.equals(this.textureKD, PlasticMaterial.class.cast(object).textureKD)) {
			return false;
		} else if(!Objects.equals(this.textureKS, PlasticMaterial.class.cast(object).textureKS)) {
			return false;
		} else if(!Objects.equals(this.textureRoughness, PlasticMaterial.class.cast(object).textureRoughness)) {
			return false;
		} else if(this.isRemappingRoughness != PlasticMaterial.class.cast(object).isRemappingRoughness) {
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
	 * Returns an {@code int} with the ID of this {@code PlasticMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code PlasticMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code PlasticMaterial} instance.
	 * 
	 * @return a hash code for this {@code PlasticMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.modifier, this.textureEmission, this.textureKD, this.textureKS, this.textureRoughness, Boolean.valueOf(this.isRemappingRoughness));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * A {@code Builder} is used to build {@link PlasticMaterial} instances.
	 * <p>
	 * This class is mutable and not thread-safe.
	 * 
	 * @since 1.0.0
	 * @author J&#246;rgen Lundgren
	 */
	public static final class Builder {
		private Modifier modifier;
		private Texture textureEmission;
		private Texture textureKD;
		private Texture textureKS;
		private Texture textureRoughness;
		private boolean isRemappingRoughness;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Constructs a new {@code Builder} instance.
		 */
		public Builder() {
			this(new PlasticMaterial());
		}
		
		/**
		 * Constructs a new {@code Builder} instance given {@code plasticMaterial}.
		 * <p>
		 * If {@code plasticMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param plasticMaterial a {@link PlasticMaterial} instance
		 * @throws NullPointerException thrown if, and only if, {@code plasticMaterial} is {@code null}
		 */
		public Builder(final PlasticMaterial plasticMaterial) {
			this.modifier = plasticMaterial.getModifier();
			this.textureEmission = plasticMaterial.getTextureEmission();
			this.textureKD = plasticMaterial.getTextureKD();
			this.textureKS = plasticMaterial.getTextureKS();
			this.textureRoughness = plasticMaterial.getTextureRoughness();
			this.isRemappingRoughness = plasticMaterial.isRemappingRoughness();
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Sets the {@link Modifier} instance.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code modifier} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param modifier the {@code Modifier} instance
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code modifier} is {@code null}
		 */
		public Builder setModifier(final Modifier modifier) {
			this.modifier = Objects.requireNonNull(modifier, "modifier == null");
			
			return this;
		}
		
		/**
		 * Sets the roughness remapping state.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * 
		 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
		 * @return this {@code Builder} instance
		 */
		public Builder setRemappingRoughness(final boolean isRemappingRoughness) {
			this.isRemappingRoughness = isRemappingRoughness;
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for emission.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code colorEmission} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param colorEmission a {@link Color3F} instance for emission
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code colorEmission} is {@code null}
		 */
		public Builder setTextureEmission(final Color3F colorEmission) {
			return setTextureEmission(new ConstantTexture(Objects.requireNonNull(colorEmission, "colorEmission == null")));
		}
		
		/**
		 * Sets the {@link Texture} instance for emission.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureEmission} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureEmission the {@code Texture} instance for emission
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureEmission} is {@code null}
		 */
		public Builder setTextureEmission(final Texture textureEmission) {
			this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the diffuse coefficient.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code colorKD} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param colorKD a {@link Color3F} instance for the diffuse coefficient
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code colorKD} is {@code null}
		 */
		public Builder setTextureKD(final Color3F colorKD) {
			return setTextureKD(new ConstantTexture(Objects.requireNonNull(colorKD, "colorKD == null")));
		}
		
		/**
		 * Sets the {@link Texture} instance for the diffuse coefficient.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureKD} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureKD the {@code Texture} instance for the diffuse coefficient
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureKD} is {@code null}
		 */
		public Builder setTextureKD(final Texture textureKD) {
			this.textureKD = Objects.requireNonNull(textureKD, "textureKD == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the specular coefficient.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code colorKS} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param colorKS a {@link Color3F} instance for the specular coefficient
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code colorKS} is {@code null}
		 */
		public Builder setTextureKS(final Color3F colorKS) {
			return setTextureKS(new ConstantTexture(Objects.requireNonNull(colorKS, "colorKS == null")));
		}
		
		/**
		 * Sets the {@link Texture} instance for the specular coefficient.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureKS} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureKS the {@code Texture} instance for the specular coefficient
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureKS} is {@code null}
		 */
		public Builder setTextureKS(final Texture textureKS) {
			this.textureKS = Objects.requireNonNull(textureKS, "textureKS == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the roughness.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * <p>
		 * If {@code textureRoughness} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param textureRoughness the {@code Texture} instance for the roughness
		 * @return this {@code Builder} instance
		 * @throws NullPointerException thrown if, and only if, {@code textureRoughness} is {@code null}
		 */
		public Builder setTextureRoughness(final Texture textureRoughness) {
			this.textureRoughness = Objects.requireNonNull(textureRoughness, "textureRoughness == null");
			
			return this;
		}
		
		/**
		 * Sets the {@link Texture} instance for the roughness.
		 * <p>
		 * Returns this {@code Builder} instance.
		 * 
		 * @param floatRoughness a {@code float} for the roughness
		 * @return this {@code Builder} instance
		 */
		public Builder setTextureRoughness(final float floatRoughness) {
			return setTextureRoughness(new ConstantTexture(floatRoughness));
		}
		
		/**
		 * Returns a new {@link PlasticMaterial} instance.
		 * 
		 * @return a new {@code PlasticMaterial} instance
		 */
		public PlasticMaterial build() {
			return new PlasticMaterial(this.textureKD, this.textureKS, this.textureEmission, this.textureRoughness, this.isRemappingRoughness, this.modifier);
		}
	}
}