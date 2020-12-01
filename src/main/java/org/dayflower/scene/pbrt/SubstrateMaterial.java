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
package org.dayflower.scene.pbrt;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Texture;
import org.dayflower.scene.texture.ConstantTexture;

/**
 * A {@code SubstrateMaterial} is an implementation of {@link PBRTMaterial} that represents a substrate material.
 * <p>
 * This class is immutable and thread-safe as long as all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SubstrateMaterial implements PBRTMaterial {
	/**
	 * The name of this {@code SubstrateMaterial} class.
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
	 * Constructs a new {@code SubstrateMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SubstrateMaterial(ConstantTexture.GRAY_0_50, ConstantTexture.GRAY_0_10, ConstantTexture.GRAY_0_10, ConstantTexture.GRAY_0_50, true);
	 * }
	 * </pre>
	 */
	public SubstrateMaterial() {
		this(ConstantTexture.GRAY_0_50, ConstantTexture.GRAY_0_10, ConstantTexture.GRAY_0_10, ConstantTexture.GRAY_0_50, true);
	}
	
	/**
	 * Constructs a new {@code SubstrateMaterial} instance.
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
	public SubstrateMaterial(final Texture textureDiffuse, final Texture textureRoughnessU, final Texture textureRoughnessV, final Texture textureSpecular, final boolean isRemappingRoughness) {
		this.textureDiffuse = Objects.requireNonNull(textureDiffuse, "textureDiffuse == null");
		this.textureRoughnessU = Objects.requireNonNull(textureRoughnessU, "textureRoughnessU == null");
		this.textureRoughnessV = Objects.requireNonNull(textureRoughnessV, "textureRoughnessV == null");
		this.textureSpecular = Objects.requireNonNull(textureSpecular, "textureSpecular == null");
		this.isRemappingRoughness = isRemappingRoughness;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
		
		final Color3F colorDiffuse = Color3F.saturate(this.textureDiffuse.getColorRGB(intersection), 0.0F, Float.MAX_VALUE);
		final Color3F colorSpecular = Color3F.saturate(this.textureSpecular.getColorRGB(intersection), 0.0F, Float.MAX_VALUE);
		
		if(!colorDiffuse.isBlack() || !colorSpecular.isBlack()) {
			final Color3F colorRoughnessU = this.textureRoughnessU.getColorRGB(intersection);
			final Color3F colorRoughnessV = this.textureRoughnessV.getColorRGB(intersection);
			
			final float roughnessU = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(colorRoughnessU.average()) : colorRoughnessU.average();
			final float roughnessV = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(colorRoughnessV.average()) : colorRoughnessV.average();
			
			final MicrofacetDistribution microfacetDistribution = new TrowbridgeReitzMicrofacetDistribution(true, roughnessU, roughnessV);
			
			return Optional.of(new BSDF(intersection, Arrays.asList(new FresnelBlendBRDF(colorDiffuse, colorSpecular, microfacetDistribution))));
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
		return String.format("new SubstrateMaterial(%s, %s, %s, %s, %s)", this.textureDiffuse, this.textureRoughnessU, this.textureRoughnessV, this.textureSpecular, Boolean.toString(this.isRemappingRoughness));
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
		} else if(!Objects.equals(this.textureDiffuse, SubstrateMaterial.class.cast(object).textureDiffuse)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessU, SubstrateMaterial.class.cast(object).textureRoughnessU)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessV, SubstrateMaterial.class.cast(object).textureRoughnessV)) {
			return false;
		} else if(!Objects.equals(this.textureSpecular, SubstrateMaterial.class.cast(object).textureSpecular)) {
			return false;
		} else if(this.isRemappingRoughness != SubstrateMaterial.class.cast(object).isRemappingRoughness) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code SubstrateMaterial} instance.
	 * 
	 * @return a hash code for this {@code SubstrateMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureDiffuse, this.textureRoughnessU, this.textureRoughnessV, this.textureSpecular, Boolean.valueOf(this.isRemappingRoughness));
	}
}