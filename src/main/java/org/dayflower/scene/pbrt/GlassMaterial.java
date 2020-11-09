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

import static org.dayflower.util.Floats.equal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Texture;

/**
 * A {@code GlassMaterial} is an implementation of {@link PBRTMaterial} that represents glass.
 * <p>
 * This class is immutable and thread-safe as long as all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class GlassMaterial implements PBRTMaterial {
	private final Texture textureEta;
	private final Texture textureKReflection;
	private final Texture textureKTransmission;
	private final Texture textureRoughnessU;
	private final Texture textureRoughnessV;
	private final boolean isRemappingRoughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code GlassMaterial} instance.
	 * <p>
	 * If either {@code textureEta}, {@code textureKReflection}, {@code textureKTransmission}, {@code textureRoughnessU} or {@code textureRoughnessV} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureEta a {@link Texture} instance used for eta, also called index of refraction (IOR)
	 * @param textureKReflection a {@code Texture} instance used for the reflection coefficient
	 * @param textureKTransmission a {@code Texture} instance used for the transmission coefficient
	 * @param textureRoughnessU a {@code Texture} instance used for the roughness along the U-axis
	 * @param textureRoughnessV a {@code Texture} instance used for the roughness along the V-axis
	 * @param isRemappingRoughness {@code true} if, and only if, the roughness values should be remapped, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code textureEta}, {@code textureKReflection}, {@code textureKTransmission}, {@code textureRoughnessU} or {@code textureRoughnessV} are {@code null}
	 */
	public GlassMaterial(final Texture textureEta, final Texture textureKReflection, final Texture textureKTransmission, final Texture textureRoughnessU, final Texture textureRoughnessV, final boolean isRemappingRoughness) {
		this.textureEta = Objects.requireNonNull(textureEta, "textureEta == null");
		this.textureKReflection = Objects.requireNonNull(textureKReflection, "textureKReflection == null");
		this.textureKTransmission = Objects.requireNonNull(textureKTransmission, "textureKTransmission == null");
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
		
		final Color3F colorEta = this.textureEta.getColorRGB(intersection);
		final Color3F colorKReflection = Color3F.saturate(this.textureKReflection.getColorRGB(intersection), 0.0F, Float.MAX_VALUE);
		final Color3F colorKTransmission = Color3F.saturate(this.textureKTransmission.getColorRGB(intersection), 0.0F, Float.MAX_VALUE);
		final Color3F colorRoughnessU = this.textureRoughnessU.getColorRGB(intersection);
		final Color3F colorRoughnessV = this.textureRoughnessV.getColorRGB(intersection);
		
		final float eta = colorEta.average();
		final float roughnessU = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(colorRoughnessU.average()) : colorRoughnessU.average();
		final float roughnessV = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(colorRoughnessV.average()) : colorRoughnessV.average();
		
		if(colorKReflection.isBlack() && colorKTransmission.isBlack()) {
			return Optional.empty();
		}
		
		final boolean isSpecular = equal(roughnessU, 0.0F) && equal(roughnessV, 0.0F);
		
		if(isSpecular && isAllowingMultipleLobes) {
			return Optional.of(new BSDF(intersection, Arrays.asList(new FresnelSpecularBXDF(colorKReflection, colorKTransmission, transportMode, 1.0F, eta)), eta));
		}
		
		if(isSpecular) {
			final List<BXDF> bXDFs = new ArrayList<>();
			
			if(!colorKReflection.isBlack()) {
				final Fresnel fresnel = new DielectricFresnel(1.0F, eta);
				
				bXDFs.add(new SpecularBRDF(colorKReflection, fresnel));
			}
			
			if(!colorKTransmission.isBlack()) {
				bXDFs.add(new SpecularBTDF(colorKTransmission, transportMode, 1.0F, eta));
			}
			
			return Optional.of(new BSDF(intersection, bXDFs, eta));
		}
		
		final List<BXDF> bXDFs = new ArrayList<>();
		
		final MicrofacetDistribution microfacetDistribution = new TrowbridgeReitzMicrofacetDistribution(true, roughnessU, roughnessV);
		
		if(!colorKReflection.isBlack()) {
			final Fresnel fresnel = new DielectricFresnel(1.0F, eta);
			
			bXDFs.add(new TorranceSparrowBRDF(colorKReflection, fresnel, microfacetDistribution));
		}
		
		if(!colorKTransmission.isBlack()) {
			bXDFs.add(new TorranceSparrowBTDF(colorKTransmission, microfacetDistribution, transportMode, 1.0F, eta));
		}
		
		return Optional.of(new BSDF(intersection, bXDFs, eta));
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
	 * Returns a {@code String} representation of this {@code GlassMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code GlassMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new GlassMaterial(%s, %s, %s, %s, %s, %s)", this.textureEta, this.textureKReflection, this.textureKTransmission, this.textureRoughnessU, this.textureRoughnessV, Boolean.toString(this.isRemappingRoughness));
	}
	
	/**
	 * Compares {@code object} to this {@code GlassMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code GlassMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code GlassMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code GlassMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof GlassMaterial)) {
			return false;
		} else if(!Objects.equals(this.textureEta, GlassMaterial.class.cast(object).textureEta)) {
			return false;
		} else if(!Objects.equals(this.textureKReflection, GlassMaterial.class.cast(object).textureKReflection)) {
			return false;
		} else if(!Objects.equals(this.textureKTransmission, GlassMaterial.class.cast(object).textureKTransmission)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessU, GlassMaterial.class.cast(object).textureRoughnessU)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessV, GlassMaterial.class.cast(object).textureRoughnessV)) {
			return false;
		} else if(this.isRemappingRoughness != GlassMaterial.class.cast(object).isRemappingRoughness) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code GlassMaterial} instance.
	 * 
	 * @return a hash code for this {@code GlassMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureEta, this.textureKReflection, this.textureKTransmission, this.textureRoughnessU, this.textureRoughnessV, Boolean.valueOf(this.isRemappingRoughness));
	}
}