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

import java.lang.reflect.Field;//TODO: Add Javadoc!
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
import org.dayflower.scene.texture.Texture;

import org.macroing.art4j.color.Color3F;
import org.macroing.java.lang.Floats;
import org.macroing.java.lang.Strings;
import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;

/**
 * A {@code SubsurfaceMaterial} is an implementation of {@link Material} and is used for subsurface scattering.
 * <p>
 * This class is immutable and thread-safe as long as the {@link Modifier} instance and all {@link Texture} instances are.
 * <p>
 * This {@code Material} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SubsurfaceMaterial implements Material {
	/**
	 * The name of this {@code SubsurfaceMaterial} class.
	 */
	public static final String NAME = "Subsurface";
	
	/**
	 * The ID of this {@code SubsurfaceMaterial} class.
	 */
	public static final int ID = 17;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final BSSRDFTable bSSRDFTable;
	private final Modifier modifier;
	private final Texture textureEmission;
	private final Texture textureKR;
	private final Texture textureKT;
	private final Texture textureRoughnessU;
	private final Texture textureRoughnessV;
	private final Texture textureSigmaA;
	private final Texture textureSigmaS;
	private final boolean isRemappingRoughness;
	private final float eta;
	private final float g;
	private final float scale;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadoc!
	public SubsurfaceMaterial(final float scale, final Texture textureKR, final Texture textureKT, final Texture textureEmission, final Texture textureSigmaA, final Texture textureSigmaS, final float g, final float eta, final Texture textureRoughnessU, final Texture textureRoughnessV, final boolean isRemappingRoughness, final Modifier modifier) {
		this.scale = scale;
		this.textureKR = Objects.requireNonNull(textureKR, "textureKR == null");
		this.textureKT = Objects.requireNonNull(textureKT, "textureKT == null");
		this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
		this.textureSigmaA = Objects.requireNonNull(textureSigmaA, "textureSigmaA == null");
		this.textureSigmaS = Objects.requireNonNull(textureSigmaS, "textureSigmaS == null");
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
	 * Returns a {@link Color3F} instance with the emittance of this {@code SubsurfaceMaterial} instance at {@code intersection}.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the emittance of this {@code SubsurfaceMaterial} instance at {@code intersection}
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
		
		final Color3F sigmaA = Color3F.multiply(Color3F.saturate(this.textureSigmaA.getColor(intersection), 0.0F, MAX_VALUE), this.scale);
		final Color3F sigmaS = Color3F.multiply(Color3F.saturate(this.textureSigmaS.getColor(intersection), 0.0F, MAX_VALUE), this.scale);
		
		final BSDF bSDF = new BSDF(intersection, bXDFs, false, this.eta);
		
		final BSSRDF bSSRDF = new TabulatedBSSRDF(intersection, this.eta, this, transportMode, sigmaA, sigmaS, this.bSSRDFTable);
		
		return new ScatteringFunctions(bSDF, bSSRDF);
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code SubsurfaceMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code SubsurfaceMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SubsurfaceMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code SubsurfaceMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new SubsurfaceMaterial(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", Strings.toNonScientificNotationJava(this.scale), this.textureKR, this.textureKT, this.textureEmission, this.textureSigmaA, this.textureSigmaS, Strings.toNonScientificNotationJava(this.g), Strings.toNonScientificNotationJava(this.eta), this.textureRoughnessU, this.textureRoughnessV, Boolean.toString(this.isRemappingRoughness), this.modifier);
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
				
				if(!this.textureSigmaA.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureSigmaS.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code SubsurfaceMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SubsurfaceMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SubsurfaceMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SubsurfaceMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SubsurfaceMaterial)) {
			return false;
		} else if(!Objects.equals(this.bSSRDFTable, SubsurfaceMaterial.class.cast(object).bSSRDFTable)) {
			return false;
		} else if(!Objects.equals(this.modifier, SubsurfaceMaterial.class.cast(object).modifier)) {
			return false;
		} else if(!Objects.equals(this.textureEmission, SubsurfaceMaterial.class.cast(object).textureEmission)) {
			return false;
		} else if(!Objects.equals(this.textureKR, SubsurfaceMaterial.class.cast(object).textureKR)) {
			return false;
		} else if(!Objects.equals(this.textureKT, SubsurfaceMaterial.class.cast(object).textureKT)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessU, SubsurfaceMaterial.class.cast(object).textureRoughnessU)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessV, SubsurfaceMaterial.class.cast(object).textureRoughnessV)) {
			return false;
		} else if(!Objects.equals(this.textureSigmaA, SubsurfaceMaterial.class.cast(object).textureSigmaA)) {
			return false;
		} else if(!Objects.equals(this.textureSigmaS, SubsurfaceMaterial.class.cast(object).textureSigmaS)) {
			return false;
		} else if(this.isRemappingRoughness != SubsurfaceMaterial.class.cast(object).isRemappingRoughness) {
			return false;
		} else if(!Floats.equals(this.eta, SubsurfaceMaterial.class.cast(object).eta)) {
			return false;
		} else if(!Floats.equals(this.g, SubsurfaceMaterial.class.cast(object).g)) {
			return false;
		} else if(!Floats.equals(this.scale, SubsurfaceMaterial.class.cast(object).scale)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code SubsurfaceMaterial} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code SubsurfaceMaterial} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code SubsurfaceMaterial} instance.
	 * 
	 * @return a hash code for this {@code SubsurfaceMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.bSSRDFTable, this.modifier, this.textureEmission, this.textureKR, this.textureKT, this.textureRoughnessU, this.textureRoughnessV, this.textureSigmaA, this.textureSigmaS, Boolean.valueOf(this.isRemappingRoughness), Float.valueOf(this.eta), Float.valueOf(this.g), Float.valueOf(this.scale));
	}
}