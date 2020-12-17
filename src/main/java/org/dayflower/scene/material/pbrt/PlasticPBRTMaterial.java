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
import org.dayflower.scene.bxdf.pbrt.TorranceSparrowPBRTBRDF;
import org.dayflower.scene.fresnel.DielectricFresnel;
import org.dayflower.scene.fresnel.Fresnel;
import org.dayflower.scene.microfacet.MicrofacetDistribution;
import org.dayflower.scene.microfacet.TrowbridgeReitzMicrofacetDistribution;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

/**
 * A {@code PlasticPBRTMaterial} is an implementation of {@link PBRTMaterial} that represents plastic.
 * <p>
 * This class is immutable and thread-safe as long as all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PlasticPBRTMaterial implements PBRTMaterial {
	/**
	 * The name of this {@code PlasticPBRTMaterial} class.
	 */
	public static final String NAME = "PBRT - Plastic";
	
	/**
	 * The ID of this {@code PlasticPBRTMaterial} class.
	 */
	public static final int ID = 105;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Texture textureDiffuse;
	private final Texture textureRoughness;
	private final Texture textureSpecular;
	private final boolean isRemappingRoughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PlasticPBRTMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticPBRTMaterial(ConstantTexture.GRAY_0_25, ConstantTexture.GRAY_0_10, ConstantTexture.GRAY_0_25, true);
	 * }
	 * </pre>
	 */
	public PlasticPBRTMaterial() {
		this(ConstantTexture.GRAY_0_25, ConstantTexture.GRAY_0_10, ConstantTexture.GRAY_0_25, true);
	}
	
	/**
	 * Constructs a new {@code PlasticPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureDiffuse}, {@code textureRoughness} or {@code textureSpecular} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureDiffuse a {@link Texture} instance used for the diffuse component
	 * @param textureRoughness a {@code Texture} instance used for the roughness
	 * @param textureSpecular a {@code Texture} instance used for the specular component
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code textureDiffuse}, {@code textureRoughness} or {@code textureSpecular} are {@code null}
	 */
	public PlasticPBRTMaterial(final Texture textureDiffuse, final Texture textureRoughness, final Texture textureSpecular, final boolean isRemappingRoughness) {
		this.textureDiffuse = Objects.requireNonNull(textureDiffuse, "textureDiffuse == null");
		this.textureRoughness = Objects.requireNonNull(textureRoughness, "textureRoughness == null");
		this.textureSpecular = Objects.requireNonNull(textureSpecular, "textureSpecular == null");
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
		
		final List<PBRTBXDF> pBRTBXDFs = new ArrayList<>();
		
		final Color3F colorDiffuse = Color3F.saturate(this.textureDiffuse.getColor(intersection), 0.0F, Float.MAX_VALUE);
		final Color3F colorSpecular = Color3F.saturate(this.textureSpecular.getColor(intersection), 0.0F, Float.MAX_VALUE);
		
		if(!colorDiffuse.isBlack()) {
			pBRTBXDFs.add(new LambertianPBRTBRDF(colorDiffuse));
		}
		
		if(!colorSpecular.isBlack()) {
			final Fresnel fresnel = new DielectricFresnel(1.5F, 1.0F);
			
			final Color3F colorRoughness = this.textureRoughness.getColor(intersection);
			
			final float roughness = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(colorRoughness.average()) : colorRoughness.average();
			
			final MicrofacetDistribution microfacetDistribution = new TrowbridgeReitzMicrofacetDistribution(true, roughness, roughness);
			
			pBRTBXDFs.add(new TorranceSparrowPBRTBRDF(colorSpecular, fresnel, microfacetDistribution));
		}
		
		if(pBRTBXDFs.size() > 0) {
			return Optional.of(new PBRTBSDF(intersection, pBRTBXDFs));
		}
		
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
		return String.format("new PlasticPBRTMaterial(%s, %s, %s, %s)", this.textureDiffuse, this.textureRoughness, this.textureSpecular, Boolean.toString(this.isRemappingRoughness));
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
				if(!this.textureDiffuse.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureRoughness.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.textureSpecular.accept(nodeHierarchicalVisitor)) {
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
		} else if(!Objects.equals(this.textureDiffuse, PlasticPBRTMaterial.class.cast(object).textureDiffuse)) {
			return false;
		} else if(!Objects.equals(this.textureRoughness, PlasticPBRTMaterial.class.cast(object).textureRoughness)) {
			return false;
		} else if(!Objects.equals(this.textureSpecular, PlasticPBRTMaterial.class.cast(object).textureSpecular)) {
			return false;
		} else if(this.isRemappingRoughness != PlasticPBRTMaterial.class.cast(object).isRemappingRoughness) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code PlasticPBRTMaterial} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code PlasticPBRTMaterial} instance
	 */
	@Override
	public float[] toArray() {
		return new float[0];//TODO: Implement!
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
		return Objects.hash(this.textureDiffuse, this.textureRoughness, this.textureSpecular, Boolean.valueOf(this.isRemappingRoughness));
	}
}