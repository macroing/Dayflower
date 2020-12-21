/**
 * Copyright 2020 J&#246;rgen Lundgren
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

import org.dayflower.image.Color3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.scene.BSSRDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.pbrt.PBRTBSDF;
import org.dayflower.scene.bxdf.pbrt.PBRTBXDF;
import org.dayflower.scene.bxdf.pbrt.LambertianPBRTBRDF;
import org.dayflower.scene.bxdf.pbrt.SpecularPBRTBRDF;
import org.dayflower.scene.bxdf.pbrt.SpecularPBRTBTDF;
import org.dayflower.scene.bxdf.pbrt.TorranceSparrowPBRTBRDF;
import org.dayflower.scene.fresnel.DielectricFresnel;
import org.dayflower.scene.microfacet.MicrofacetDistribution;
import org.dayflower.scene.microfacet.TrowbridgeReitzMicrofacetDistribution;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

/**
 * An {@code UberPBRTMaterial} is an implementation of {@link PBRTMaterial} that can represent a wide variety of materials.
 * <p>
 * This class is immutable and thread-safe as long as all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class UberPBRTMaterial implements PBRTMaterial {
	/**
	 * The name of this {@code UberPBRTMaterial} class.
	 */
	public static final String NAME = "PBRT - Uber";
	
	/**
	 * The ID of this {@code UberPBRTMaterial} class.
	 */
	public static final int ID = 107;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	 * new UberPBRTMaterial(new Color3F(1.5F)), ConstantTexture.GRAY_0_25, ConstantTexture.BLACK, ConstantTexture.GRAY_0_25, ConstantTexture.BLACK, ConstantTexture.WHITE, ConstantTexture.GRAY_0_10, ConstantTexture.GRAY_0_10, true);
	 * }
	 * </pre>
	 */
	public UberPBRTMaterial() {
		this(new ConstantTexture(new Color3F(1.5F)), ConstantTexture.GRAY_0_25, ConstantTexture.BLACK, ConstantTexture.GRAY_0_25, ConstantTexture.BLACK, ConstantTexture.WHITE, ConstantTexture.GRAY_0_10, ConstantTexture.GRAY_0_10, true);
	}
	
	/**
	 * Constructs a new {@code UberPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureEta}, {@code textureKD}, {@code textureKR}, {@code textureKS}, {@code textureKT}, {@code textureOpacity}, {@code textureRoughnessU} or {@code textureRoughnessV} are {@code null}, a {@code NullPointerException} will be
	 * thrown.
	 * 
	 * @param textureEta a {@link Texture} instance used for eta, also called index of refraction (IOR)
	 * @param textureKD a {@code Texture} instance used for the diffuse component
	 * @param textureKR a {@code Texture} instance used for the reflection coefficient
	 * @param textureKS a {@code Texture} instance used for the specular component
	 * @param textureKT a {@code Texture} instance used for the transmission coefficient
	 * @param textureOpacity a {@code Texture} instance used for opacity
	 * @param textureRoughnessU a {@code Texture} instance used for the roughness along the U-axis
	 * @param textureRoughnessV a {@code Texture} instance used for the roughness along the V-axis
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code textureEta}, {@code textureKD}, {@code textureKR}, {@code textureKS}, {@code textureKT}, {@code textureOpacity}, {@code textureRoughnessU} or {@code textureRoughnessV} are
	 *                              {@code null}
	 */
	public UberPBRTMaterial(final Texture textureEta, final Texture textureKD, final Texture textureKR, final Texture textureKS, final Texture textureKT, final Texture textureOpacity, final Texture textureRoughnessU, final Texture textureRoughnessV, final boolean isRemappingRoughness) {
		this.textureEta = Objects.requireNonNull(textureEta, "textureEta == null");
		this.textureKD = Objects.requireNonNull(textureKD, "textureKD == null");
		this.textureKR = Objects.requireNonNull(textureKR, "textureKR == null");
		this.textureKS = Objects.requireNonNull(textureKS, "textureKS == null");
		this.textureKT = Objects.requireNonNull(textureKT, "textureKT == null");
		this.textureOpacity = Objects.requireNonNull(textureOpacity, "textureOpacity == null");
		this.textureRoughnessU = Objects.requireNonNull(textureRoughnessU, "textureRoughnessU == null");
		this.textureRoughnessV = Objects.requireNonNull(textureRoughnessV, "textureRoughnessV == null");
		this.isRemappingRoughness = isRemappingRoughness;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
		
		final Color3F colorEta = this.textureEta.getColor(intersection);
		final Color3F colorOpacity = Color3F.saturate(this.textureOpacity.getColor(intersection), 0.0F, Float.MAX_VALUE);
		final Color3F colorKD = Color3F.multiply(colorOpacity, Color3F.saturate(this.textureKD.getColor(intersection), 0.0F, Float.MAX_VALUE));
		final Color3F colorKR = Color3F.multiply(colorOpacity, Color3F.saturate(this.textureKR.getColor(intersection), 0.0F, Float.MAX_VALUE));
		final Color3F colorKS = Color3F.multiply(colorOpacity, Color3F.saturate(this.textureKS.getColor(intersection), 0.0F, Float.MAX_VALUE));
		final Color3F colorKT = Color3F.multiply(colorOpacity, Color3F.saturate(this.textureKT.getColor(intersection), 0.0F, Float.MAX_VALUE));
		final Color3F colorRoughnessU = this.textureRoughnessU.getColor(intersection);
		final Color3F colorRoughnessV = this.textureRoughnessV.getColor(intersection);
		final Color3F colorTransmittanceScale = Color3F.saturate(Color3F.subtract(Color3F.WHITE, colorOpacity), 0.0F, Float.MAX_VALUE);
		
		final float eta = colorEta.average();
		final float roughnessU = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(colorRoughnessU.average()) : colorRoughnessU.average();
		final float roughnessV = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(colorRoughnessV.average()) : colorRoughnessV.average();
		
		final List<PBRTBXDF> pBRTBXDFs = new ArrayList<>();
		
		if(!colorTransmittanceScale.isBlack()) {
			pBRTBXDFs.add(new SpecularPBRTBTDF(colorTransmittanceScale, transportMode, 1.0F, 1.0F));
		}
		
		if(!colorKD.isBlack()) {
			pBRTBXDFs.add(new LambertianPBRTBRDF(colorKD));
		}
		
		if(!colorKS.isBlack()) {
			pBRTBXDFs.add(new TorranceSparrowPBRTBRDF(colorKS, new DielectricFresnel(1.0F, eta), new TrowbridgeReitzMicrofacetDistribution(true, roughnessU, roughnessV)));
		}
		
		if(!colorKR.isBlack()) {
			pBRTBXDFs.add(new SpecularPBRTBRDF(colorKR, new DielectricFresnel(1.0F, eta)));
		}
		
		if(!colorKT.isBlack()) {
			pBRTBXDFs.add(new SpecularPBRTBTDF(colorKT, transportMode, 1.0F, eta));
		}
		
		return Optional.of(new PBRTBSDF(intersection, pBRTBXDFs, colorTransmittanceScale.isBlack() ? eta : 1.0F));
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
		return String.format("new UberPBRTMaterial(%s, %s, %s, %s, %s, %s, %s, %s, %s)", this.textureEta, this.textureKD, this.textureKR, this.textureKS, this.textureKT, this.textureOpacity, this.textureRoughnessU, this.textureRoughnessV, Boolean.toString(this.isRemappingRoughness));
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
		return Objects.hash(this.textureEta, this.textureKD, this.textureKR, this.textureKS, this.textureKT, this.textureOpacity, this.textureRoughnessU, this.textureRoughnessV, Boolean.valueOf(this.isRemappingRoughness));
	}
}