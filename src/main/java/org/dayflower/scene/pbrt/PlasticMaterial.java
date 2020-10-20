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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Texture;
import org.dayflower.scene.texture.ConstantTexture;

/**
 * A {@code PlasticMaterial} is an implementation of {@link PBRTMaterial} that represents plastic.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PlasticMaterial implements PBRTMaterial {
	private final Texture textureDiffuse;
	private final Texture textureRoughness;
	private final Texture textureSpecular;
	private final boolean isRemappingRoughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PlasticMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PlasticMaterial(ConstantTexture.GRAY_0_25, ConstantTexture.GRAY_0_10, ConstantTexture.GRAY_0_25, true);
	 * }
	 * </pre>
	 */
	public PlasticMaterial() {
		this(ConstantTexture.GRAY_0_25, ConstantTexture.GRAY_0_10, ConstantTexture.GRAY_0_25, true);
	}
	
	/**
	 * Constructs a new {@code PlasticMaterial} instance.
	 * <p>
	 * If either {@code textureDiffuse}, {@code textureRoughness} or {@code textureSpecular} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureDiffuse a {@link Texture} instance used for the diffuse component
	 * @param textureRoughness a {@code Texture} instance used for the roughness
	 * @param textureSpecular a {@code Texture} instance used for the specular component
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code textureDiffuse}, {@code textureRoughness} or {@code textureSpecular} are {@code null}
	 */
	public PlasticMaterial(final Texture textureDiffuse, final Texture textureRoughness, final Texture textureSpecular, final boolean isRemappingRoughness) {
		this.textureDiffuse = Objects.requireNonNull(textureDiffuse, "textureDiffuse == null");
		this.textureRoughness = Objects.requireNonNull(textureRoughness, "textureRoughness == null");
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
		
		final List<BXDF> bXDFs = new ArrayList<>();
		
		final Color3F colorDiffuse = Color3F.saturate(this.textureDiffuse.getColor(intersection), 0.0F, Float.MAX_VALUE);
		final Color3F colorSpecular = Color3F.saturate(this.textureSpecular.getColor(intersection), 0.0F, Float.MAX_VALUE);
		
		if(!colorDiffuse.isBlack()) {
			bXDFs.add(new LambertianBRDF(colorDiffuse));
		}
		
		if(!colorSpecular.isBlack()) {
			final Fresnel fresnel = new DielectricFresnel(1.5F, 1.0F);
			
			final Color3F colorRoughness = this.textureRoughness.getColor(intersection);
			
			final float roughness = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(colorRoughness.average()) : colorRoughness.average();
			
			final MicrofacetDistribution microfacetDistribution = new TrowbridgeReitzMicrofacetDistribution(true, roughness, roughness);
			
			bXDFs.add(new TorranceSparrowBRDF(colorSpecular, fresnel, microfacetDistribution));
		}
		
		if(bXDFs.size() > 0) {
			return Optional.of(new BSDF(intersection, bXDFs));
		}
		
		return Optional.empty();
	}
	
	/**
	 * Returns a {@code String} representation of this {@code PlasticMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code PlasticMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new PlasticMaterial(%s, %s, %s, %s)", this.textureDiffuse, this.textureRoughness, this.textureSpecular, Boolean.toString(this.isRemappingRoughness));
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
		} else if(!Objects.equals(this.textureDiffuse, PlasticMaterial.class.cast(object).textureDiffuse)) {
			return false;
		} else if(!Objects.equals(this.textureRoughness, PlasticMaterial.class.cast(object).textureRoughness)) {
			return false;
		} else if(!Objects.equals(this.textureSpecular, PlasticMaterial.class.cast(object).textureSpecular)) {
			return false;
		} else if(this.isRemappingRoughness != PlasticMaterial.class.cast(object).isRemappingRoughness) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code PlasticMaterial} instance.
	 * 
	 * @return a hash code for this {@code PlasticMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureDiffuse, this.textureRoughness, this.textureSpecular, Boolean.valueOf(this.isRemappingRoughness));
	}
}