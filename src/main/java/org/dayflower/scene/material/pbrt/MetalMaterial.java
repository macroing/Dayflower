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
import org.dayflower.scene.Fresnel;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.MicrofacetDistribution;
import org.dayflower.scene.Texture;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.pbrt.BSDF;
import org.dayflower.scene.bxdf.pbrt.TorranceSparrowBRDF;
import org.dayflower.scene.fresnel.ConductorFresnel;
import org.dayflower.scene.microfacet.TrowbridgeReitzMicrofacetDistribution;
import org.dayflower.scene.texture.ConstantTexture;

/**
 * A {@code MetalMaterial} is an implementation of {@link PBRTMaterial} that represents metal.
 * <p>
 * This class is immutable and thread-safe as long as all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MetalMaterial implements PBRTMaterial {
	/**
	 * The name of this {@code MetalMaterial} class.
	 */
	public static final String NAME = "PBRT - Metal";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Texture textureEta;
	private final Texture textureK;
	private final Texture textureRoughnessU;
	private final Texture textureRoughnessV;
	private final boolean isRemappingRoughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MetalMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MetalTexture(new ConstantTexture(Color3F.GOLD_ETA_MAXIMUM_TO_1), new ConstantTexture(Color3F.GOLD_K_MAXIMUM_TO_1));
	 * }
	 * </pre>
	 */
	public MetalMaterial() {
		this(new ConstantTexture(Color3F.GOLD_ETA_MAXIMUM_TO_1), new ConstantTexture(Color3F.GOLD_K_MAXIMUM_TO_1));
	}
	
	/**
	 * Constructs a new {@code MetalMaterial} instance.
	 * <p>
	 * If either {@code textureEta} or {@code textureK} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MetalTexture(textureEta, textureK, new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.01F)), true);
	 * }
	 * </pre>
	 * 
	 * @param textureEta a {@link Texture} instance used for eta, also called index of refraction (IOR)
	 * @param textureK a {@code Texture} instance used for the absorption coefficient
	 * @throws NullPointerException thrown if, and only if, either {@code textureEta} or {@code textureK} are {@code null}
	 */
	public MetalMaterial(final Texture textureEta, final Texture textureK) {
		this(textureEta, textureK, new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.01F)), true);
	}
	
	/**
	 * Constructs a new {@code MetalMaterial} instance.
	 * <p>
	 * If either {@code textureEta}, {@code textureK}, {@code textureRoughnessU} or {@code textureRoughnessV} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureEta a {@link Texture} instance used for eta, also called index of refraction (IOR)
	 * @param textureK a {@code Texture} instance used for the absorption coefficient
	 * @param textureRoughnessU a {@code Texture} instance used for the roughness along the U-axis
	 * @param textureRoughnessV a {@code Texture} instance used for the roughness along the V-axis
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code textureEta}, {@code textureK}, {@code textureRoughnessU} or {@code textureRoughnessV} are {@code null}
	 */
	public MetalMaterial(final Texture textureEta, final Texture textureK, final Texture textureRoughnessU, final Texture textureRoughnessV, final boolean isRemappingRoughness) {
		this.textureEta = Objects.requireNonNull(textureEta, "textureEta == null");
		this.textureK = Objects.requireNonNull(textureK, "textureK == null");
		this.textureRoughnessU = Objects.requireNonNull(textureRoughnessU, "textureRoughnessU == null");
		this.textureRoughnessV = Objects.requireNonNull(textureRoughnessV, "textureRoughnessV == null");
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
		
		final Color3F colorEtaI = Color3F.WHITE;
		final Color3F colorEtaT = this.textureEta.getColorRGB(intersection);
		final Color3F colorK = this.textureK.getColorRGB(intersection);
		final Color3F colorRoughnessU = this.textureRoughnessU.getColorRGB(intersection);
		final Color3F colorRoughnessV = this.textureRoughnessV.getColorRGB(intersection);
		
		final float roughnessU = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(colorRoughnessU.average()) : colorRoughnessU.average();
		final float roughnessV = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(colorRoughnessV.average()) : colorRoughnessV.average();
		
		final Fresnel fresnel = new ConductorFresnel(colorEtaI, colorEtaT, colorK);
		
		final MicrofacetDistribution microfacetDistribution = new TrowbridgeReitzMicrofacetDistribution(true, roughnessU, roughnessV);
		
		return Optional.of(new BSDF(intersection, Arrays.asList(new TorranceSparrowBRDF(Color3F.WHITE, fresnel, microfacetDistribution))));
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
	 * Returns a {@code String} with the name of this {@code MetalMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code MetalMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code MetalMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code MetalMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new MetalMaterial(%s, %s, %s, %s, %s)", this.textureEta, this.textureK, this.textureRoughnessU, this.textureRoughnessV, Boolean.toString(this.isRemappingRoughness));
	}
	
	/**
	 * Compares {@code object} to this {@code MetalMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code MetalMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code MetalMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code MetalMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof MetalMaterial)) {
			return false;
		} else if(!Objects.equals(this.textureEta, MetalMaterial.class.cast(object).textureEta)) {
			return false;
		} else if(!Objects.equals(this.textureK, MetalMaterial.class.cast(object).textureK)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessU, MetalMaterial.class.cast(object).textureRoughnessU)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessV, MetalMaterial.class.cast(object).textureRoughnessV)) {
			return false;
		} else if(this.isRemappingRoughness != MetalMaterial.class.cast(object).isRemappingRoughness) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code MetalMaterial} instance.
	 * 
	 * @return a hash code for this {@code MetalMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureEta, this.textureK, this.textureRoughnessU, this.textureRoughnessV, Boolean.valueOf(this.isRemappingRoughness));
	}
}