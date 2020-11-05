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
 * A {@code GlassMaterial} is an implementation of {@link PBRTMaterial} that can represent a wide variety of materials.
 * <p>
 * This class is immutable and thread-safe as long as all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class UberMaterial implements PBRTMaterial {
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
	 * Constructs a new {@code UberMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new UberMaterial(new Color3F(1.5F)), ConstantTexture.GRAY_0_25, ConstantTexture.BLACK, ConstantTexture.GRAY_0_25, ConstantTexture.BLACK, ConstantTexture.WHITE, ConstantTexture.GRAY_0_10, ConstantTexture.GRAY_0_10, true);
	 * }
	 * </pre>
	 */
	public UberMaterial() {
		this(new ConstantTexture(new Color3F(1.5F)), ConstantTexture.GRAY_0_25, ConstantTexture.BLACK, ConstantTexture.GRAY_0_25, ConstantTexture.BLACK, ConstantTexture.WHITE, ConstantTexture.GRAY_0_10, ConstantTexture.GRAY_0_10, true);
	}
	
	/**
	 * Constructs a new {@code UberMaterial} instance.
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
	public UberMaterial(final Texture textureEta, final Texture textureKD, final Texture textureKR, final Texture textureKS, final Texture textureKT, final Texture textureOpacity, final Texture textureRoughnessU, final Texture textureRoughnessV, final boolean isRemappingRoughness) {
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
		
		final Color3F colorEta = this.textureEta.getColorXYZ(intersection);
		final Color3F colorOpacity = Color3F.saturate(this.textureOpacity.getColorXYZ(intersection), 0.0F, Float.MAX_VALUE);
		final Color3F colorKD = Color3F.multiply(colorOpacity, Color3F.saturate(this.textureKD.getColorXYZ(intersection), 0.0F, Float.MAX_VALUE));
		final Color3F colorKR = Color3F.multiply(colorOpacity, Color3F.saturate(this.textureKR.getColorXYZ(intersection), 0.0F, Float.MAX_VALUE));
		final Color3F colorKS = Color3F.multiply(colorOpacity, Color3F.saturate(this.textureKS.getColorXYZ(intersection), 0.0F, Float.MAX_VALUE));
		final Color3F colorKT = Color3F.multiply(colorOpacity, Color3F.saturate(this.textureKT.getColorXYZ(intersection), 0.0F, Float.MAX_VALUE));
		final Color3F colorRoughnessU = this.textureRoughnessU.getColorXYZ(intersection);
		final Color3F colorRoughnessV = this.textureRoughnessV.getColorXYZ(intersection);
		final Color3F colorTransmittanceScale = Color3F.saturate(Color3F.subtract(Color3F.WHITE, colorOpacity), 0.0F, Float.MAX_VALUE);
		
		final float eta = colorEta.average();
		final float roughnessU = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(colorRoughnessU.average()) : colorRoughnessU.average();
		final float roughnessV = this.isRemappingRoughness ? MicrofacetDistribution.convertRoughnessToAlpha(colorRoughnessV.average()) : colorRoughnessV.average();
		
		final List<BXDF> bXDFs = new ArrayList<>();
		
		if(!colorTransmittanceScale.isBlack()) {
			bXDFs.add(new SpecularBTDF(colorTransmittanceScale, transportMode, 1.0F, 1.0F));
		}
		
		if(!colorKD.isBlack()) {
			bXDFs.add(new LambertianBRDF(colorKD));
		}
		
		if(!colorKS.isBlack()) {
			bXDFs.add(new TorranceSparrowBRDF(colorKS, new DielectricFresnel(1.0F, eta), new TrowbridgeReitzMicrofacetDistribution(true, roughnessU, roughnessV)));
		}
		
		if(!colorKR.isBlack()) {
			bXDFs.add(new SpecularBRDF(colorKR, new DielectricFresnel(1.0F, eta)));
		}
		
		if(!colorKT.isBlack()) {
			bXDFs.add(new SpecularBTDF(colorKT, transportMode, 1.0F, eta));
		}
		
		return Optional.of(new BSDF(intersection, bXDFs, colorTransmittanceScale.isBlack() ? eta : 1.0F));
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
	 * Returns a {@code String} representation of this {@code UberMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code UberMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new UberMaterial(%s, %s, %s, %s, %s, %s, %s, %s, %s)", this.textureEta, this.textureKD, this.textureKR, this.textureKS, this.textureKT, this.textureOpacity, this.textureRoughnessU, this.textureRoughnessV, Boolean.toString(this.isRemappingRoughness));
	}
	
	/**
	 * Compares {@code object} to this {@code UberMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code UberMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code UberMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code UberMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof UberMaterial)) {
			return false;
		} else if(!Objects.equals(this.textureEta, UberMaterial.class.cast(object).textureEta)) {
			return false;
		} else if(!Objects.equals(this.textureKD, UberMaterial.class.cast(object).textureKD)) {
			return false;
		} else if(!Objects.equals(this.textureKR, UberMaterial.class.cast(object).textureKR)) {
			return false;
		} else if(!Objects.equals(this.textureKS, UberMaterial.class.cast(object).textureKS)) {
			return false;
		} else if(!Objects.equals(this.textureKT, UberMaterial.class.cast(object).textureKT)) {
			return false;
		} else if(!Objects.equals(this.textureOpacity, UberMaterial.class.cast(object).textureOpacity)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessU, UberMaterial.class.cast(object).textureRoughnessU)) {
			return false;
		} else if(!Objects.equals(this.textureRoughnessV, UberMaterial.class.cast(object).textureRoughnessV)) {
			return false;
		} else if(this.isRemappingRoughness != UberMaterial.class.cast(object).isRemappingRoughness) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code UberMaterial} instance.
	 * 
	 * @return a hash code for this {@code UberMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureEta, this.textureKD, this.textureKR, this.textureKS, this.textureKT, this.textureOpacity, this.textureRoughnessU, this.textureRoughnessV, Boolean.valueOf(this.isRemappingRoughness));
	}
}