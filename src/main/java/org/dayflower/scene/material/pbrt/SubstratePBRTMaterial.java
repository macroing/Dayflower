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

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.image.Color3F;
import org.dayflower.scene.BSSRDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.pbrt.PBRTBSDF;
import org.dayflower.scene.bxdf.pbrt.FresnelBlendPBRTBRDF;
import org.dayflower.scene.microfacet.MicrofacetDistribution;
import org.dayflower.scene.microfacet.TrowbridgeReitzMicrofacetDistribution;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

/**
 * A {@code SubstratePBRTMaterial} is an implementation of {@link PBRTMaterial} that represents a substrate material.
 * <p>
 * This class is immutable and thread-safe as long as all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SubstratePBRTMaterial implements PBRTMaterial {
	/**
	 * The name of this {@code SubstratePBRTMaterial} class.
	 */
	public static final String NAME = "PBRT - Substrate";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Texture textureDiffuse;
	private final Texture textureRoughnessU;
	private final Texture textureRoughnessV;
	private final Texture textureSpecular;
	private final boolean isRemappingRoughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SubstratePBRTMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SubstratePBRTMaterial(ConstantTexture.GRAY_0_50, ConstantTexture.GRAY_0_10, ConstantTexture.GRAY_0_10, ConstantTexture.GRAY_0_50, true);
	 * }
	 * </pre>
	 */
	public SubstratePBRTMaterial() {
		this(ConstantTexture.GRAY_0_50, ConstantTexture.GRAY_0_10, ConstantTexture.GRAY_0_10, ConstantTexture.GRAY_0_50, true);
	}
	
	/**
	 * Constructs a new {@code SubstratePBRTMaterial} instance.
	 * <p>
	 * If either {@code textureDiffuse}, {@code textureRoughnessU}, {@code textureRoughnessV} or {@code textureSpecular} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureDiffuse a {@link Texture} instance used for the diffuse component
	 * @param textureRoughnessU a {@code Texture} instance used for the roughness along the U-direction
	 * @param textureRoughnessV a {@code Texture} instance used for the roughness along the V-direction
	 * @param textureSpecular a {@code Texture} instance used for the specular component
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code textureDiffuse}, {@code textureRoughnessU}, {@code textureRoughnessV} or {@code textureSpecular} are {@code null}
	 */
	public SubstratePBRTMaterial(final Texture textureDiffuse, final Texture textureRoughnessU, final Texture textureRoughnessV, final Texture textureSpecular, final boolean isRemappingRoughness) {
		this.textureDiffuse = Objects.requireNonNull(textureDiffuse, "textureDiffuse == null");
		this.textureRoughnessU = Objects.requireNonNull(textureRoughnessU, "textureRoughnessU == null");
		this.textureRoughnessV = Objects.requireNonNull(textureRoughnessV, "textureRoughnessV == null");
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
		
		final Color3F colorDiffuse = Color3F.saturate(this.textureDiffuse.getColorRGB(intersection), 0.0F, Float.MAX_VALUE);
		final Color3F colorSpecular = Color3F.saturate(this.textureSpecular.getColorRGB(intersection), 0.0F, Float.MAX_VALUE);
		
		if(!colorDiffuse.isBlack() || !colorSpecular.isBlack()) {
			final Color3F colorRoughnessU = this.textureRoughnessU.getColorRGB(intersection);
			final Color3F colorRoughnessV = this.textureRoughnessV.getColorRGB(intersection);
			
			final float roughnessU = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(colorRoughnessU.average()) : colorRoughnessU.average();
			final float roughnessV = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(colorRoughnessV.average()) : colorRoughnessV.average();
			
			final MicrofacetDistribution microfacetDistribution = new TrowbridgeReitzMicrofacetDistribution(true, roughnessU, roughnessV);
			
			return Optional.of(new PBRTBSDF(intersection, Arrays.asList(new FresnelBlendPBRTBRDF(colorDiffuse, colorSpecular, microfacetDistribution))));
		}
		
		return Optional.empty();
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code SubstratePBRTMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code SubstratePBRTMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SubstratePBRTMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code SubstratePBRTMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new SubstratePBRTMaterial(%s, %s, %s, %s, %s)", this.textureDiffuse, this.textureRoughnessU, this.textureRoughnessV, this.textureSpecular, Boolean.toString(this.isRemappingRoughness));
	}
	
	/**
	 * Compares {@code object} to this {@code SubstratePBRTMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SubstratePBRTMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SubstratePBRTMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SubstratePBRTMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SubstratePBRTMaterial)) {
			return false;
		} else if(!Objects.equals(this.textureDiffuse, SubstratePBRTMaterial.class.cast(object).textureDiffuse)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessU, SubstratePBRTMaterial.class.cast(object).textureRoughnessU)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessV, SubstratePBRTMaterial.class.cast(object).textureRoughnessV)) {
			return false;
		} else if(!Objects.equals(this.textureSpecular, SubstratePBRTMaterial.class.cast(object).textureSpecular)) {
			return false;
		} else if(this.isRemappingRoughness != SubstratePBRTMaterial.class.cast(object).isRemappingRoughness) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code SubstratePBRTMaterial} instance.
	 * 
	 * @return a hash code for this {@code SubstratePBRTMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureDiffuse, this.textureRoughnessU, this.textureRoughnessV, this.textureSpecular, Boolean.valueOf(this.isRemappingRoughness));
	}
}