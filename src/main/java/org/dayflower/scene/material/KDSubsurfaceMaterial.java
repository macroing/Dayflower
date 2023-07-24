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
import org.macroing.java.lang.Floats;
import org.macroing.java.lang.Strings;
import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;

/**
 * A {@code KDSubsurfaceMaterial} is an implementation of {@link Material} and is used for subsurface scattering.
 * <p>
 * This class is immutable and thread-safe as long as the {@link Modifier} instance and all {@link Texture} instances are.
 * <p>
 * This {@code Material} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
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
	
	/**
	 * Constructs a new {@code KDSubsurfaceMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new KDSubsurfaceMaterial(1.0F);
	 * }
	 * </pre>
	 */
	public KDSubsurfaceMaterial() {
		this(1.0F);
	}
	
	/**
	 * Constructs a new {@code KDSubsurfaceMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new KDSubsurfaceMaterial(scale, ConstantTexture.GRAY_0_50);
	 * }
	 * </pre>
	 * 
	 * @param scale the scale to use
	 */
	public KDSubsurfaceMaterial(final float scale) {
		this(scale, ConstantTexture.GRAY_0_50);
	}
	
	/**
	 * Constructs a new {@code KDSubsurfaceMaterial} instance.
	 * <p>
	 * If {@code textureKD} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new KDSubsurfaceMaterial(scale, textureKD, ConstantTexture.WHITE);
	 * }
	 * </pre>
	 * 
	 * @param scale the scale to use
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @throws NullPointerException thrown if, and only if, {@code textureKD} is {@code null}
	 */
	public KDSubsurfaceMaterial(final float scale, final Texture textureKD) {
		this(scale, textureKD, ConstantTexture.WHITE);
	}
	
	/**
	 * Constructs a new {@code KDSubsurfaceMaterial} instance.
	 * <p>
	 * If either {@code textureKD} or {@code textureKR} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new KDSubsurfaceMaterial(scale, textureKD, textureKR, ConstantTexture.WHITE);
	 * }
	 * </pre>
	 * 
	 * @param scale the scale to use
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKR a {@code Texture} instance for the reflection coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD} or {@code textureKR} are {@code null}
	 */
	public KDSubsurfaceMaterial(final float scale, final Texture textureKD, final Texture textureKR) {
		this(scale, textureKD, textureKR, ConstantTexture.WHITE);
	}
	
	/**
	 * Constructs a new {@code KDSubsurfaceMaterial} instance.
	 * <p>
	 * If either {@code textureKD}, {@code textureKR} or {@code textureKT} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new KDSubsurfaceMaterial(scale, textureKD, textureKR, textureKT, ConstantTexture.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param scale the scale to use
	 * @param textureKD a {@link Texture} instance for the diffuse coefficient
	 * @param textureKR a {@code Texture} instance for the reflection coefficient
	 * @param textureKT a {@code Texture} instance for the transmission coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code textureKD}, {@code textureKR} or {@code textureKT} are {@code null}
	 */
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
	
	/**
	 * Returns a {@link Color3F} instance with the emittance of this {@code KDSubsurfaceMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code KDSubsurfaceMaterial} instance at {@code intersection}
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public Color3F emittance(final Intersection intersection) {
		Objects.requireNonNull(intersection, "intersection == null");
		
		return this.textureEmission.getColor(intersection);
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
	
	/**
	 * Returns a {@code String} with the name of this {@code KDSubsurfaceMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code KDSubsurfaceMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code KDSubsurfaceMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code KDSubsurfaceMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new KDSubsurfaceMaterial(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", Strings.toNonScientificNotationJava(this.scale), this.textureKD, this.textureKR, this.textureKT, this.textureEmission, this.textureMFP, Strings.toNonScientificNotationJava(this.g), Strings.toNonScientificNotationJava(this.eta), this.textureRoughnessU, this.textureRoughnessV, Boolean.toString(this.isRemappingRoughness), this.modifier);
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
				
				if(!this.textureKR.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureKT.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureMFP.accept(nodeHierarchicalVisitor)) {
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
	 * Compares {@code object} to this {@code KDSubsurfaceMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code KDSubsurfaceMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code KDSubsurfaceMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code KDSubsurfaceMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof KDSubsurfaceMaterial)) {
			return false;
		} else if(!Objects.equals(this.bSSRDFTable, KDSubsurfaceMaterial.class.cast(object).bSSRDFTable)) {
			return false;
		} else if(!Objects.equals(this.modifier, KDSubsurfaceMaterial.class.cast(object).modifier)) {
			return false;
		} else if(!Objects.equals(this.textureEmission, KDSubsurfaceMaterial.class.cast(object).textureEmission)) {
			return false;
		} else if(!Objects.equals(this.textureKD, KDSubsurfaceMaterial.class.cast(object).textureKD)) {
			return false;
		} else if(!Objects.equals(this.textureKR, KDSubsurfaceMaterial.class.cast(object).textureKR)) {
			return false;
		} else if(!Objects.equals(this.textureKT, KDSubsurfaceMaterial.class.cast(object).textureKT)) {
			return false;
		} else if(!Objects.equals(this.textureMFP, KDSubsurfaceMaterial.class.cast(object).textureMFP)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessU, KDSubsurfaceMaterial.class.cast(object).textureRoughnessU)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessV, KDSubsurfaceMaterial.class.cast(object).textureRoughnessV)) {
			return false;
		} else if(this.isRemappingRoughness != KDSubsurfaceMaterial.class.cast(object).isRemappingRoughness) {
			return false;
		} else if(!Floats.equals(this.eta, KDSubsurfaceMaterial.class.cast(object).eta)) {
			return false;
		} else if(!Floats.equals(this.g, KDSubsurfaceMaterial.class.cast(object).g)) {
			return false;
		} else if(!Floats.equals(this.scale, KDSubsurfaceMaterial.class.cast(object).scale)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code KDSubsurfaceMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code KDSubsurfaceMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code KDSubsurfaceMaterial} instance.
	 * 
	 * @return a hash code for this {@code KDSubsurfaceMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.bSSRDFTable, this.modifier, this.textureEmission, this.textureKD, this.textureKR, this.textureKT, this.textureMFP, this.textureRoughnessU, this.textureRoughnessV, Boolean.valueOf(this.isRemappingRoughness), Float.valueOf(this.eta), Float.valueOf(this.g), Float.valueOf(this.scale));
	}
}