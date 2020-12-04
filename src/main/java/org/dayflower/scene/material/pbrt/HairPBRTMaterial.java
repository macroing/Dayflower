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

import static org.dayflower.util.Floats.max;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.image.Color3F;
import org.dayflower.scene.BSSRDF;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.pbrt.PBRTBSDF;
import org.dayflower.scene.bxdf.pbrt.HairPBRTBXDF;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

/**
 * A {@code HairPBRTMaterial} is an implementation of {@link PBRTMaterial} that represents hair.
 * <p>
 * This class is immutable and thread-safe as long as all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class HairPBRTMaterial implements PBRTMaterial {
	/**
	 * The name of this {@code HairPBRTMaterial} class.
	 */
	public static final String NAME = "PBRT - Hair";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Texture textureAlpha;
	private final Texture textureBetaM;
	private final Texture textureBetaN;
	private final Texture textureColor;
	private final Texture textureEta;
	private final Texture textureEumelanin;
	private final Texture texturePheomelanin;
	private final Texture textureSigmaA;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code HairPBRTMaterial} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * Texture textureAlpha = ConstantTexture.GRAY_2_00;
	 * Texture textureBetaM = ConstantTexture.GRAY_0_30;
	 * Texture textureBetaN = ConstantTexture.GRAY_0_30;
	 * Texture textureColor = ConstantTexture.BLACK;
	 * Texture textureEta = ConstantTexture.GRAY_1_55;
	 * Texture textureEumelanin = ConstantTexture.BLACK;
	 * Texture texturePheomelanin = ConstantTexture.BLACK;
	 * Texture textureSigmaA = new ConstantTexture(HairPBRTBXDF.computeSigmaAFromConcentration(1.3F, 0.0F));
	 * 
	 * new HairPBRTMaterial(textureAlpha, textureBetaM, textureBetaN, textureColor, textureEta, textureEumelanin, texturePheomelanin, textureSigmaA);
	 * }
	 * </pre>
	 */
	public HairPBRTMaterial() {
		this(ConstantTexture.GRAY_2_00, ConstantTexture.GRAY_0_30, ConstantTexture.GRAY_0_30, ConstantTexture.BLACK, ConstantTexture.GRAY_1_55, ConstantTexture.BLACK, ConstantTexture.BLACK, new ConstantTexture(HairPBRTBXDF.computeSigmaAFromConcentration(1.3F, 0.0F)));
	}
	
	/**
	 * Constructs a new {@code HairPBRTMaterial} instance.
	 * <p>
	 * If either {@code textureAlpha}, {@code textureBetaM}, {@code textureBetaN}, {@code textureColor}, {@code textureEta}, {@code textureEumelanin}, {@code texturePheomelanin} or {@code textureSigmaA} are {@code null}, a {@code NullPointerException}
	 * will be thrown.
	 * 
	 * @param textureAlpha a {@link Texture} instance
	 * @param textureBetaM a {@code Texture} instance
	 * @param textureBetaN a {@code Texture} instance
	 * @param textureColor a {@code Texture} instance
	 * @param textureEta a {@code Texture} instance with the index of refraction (IOR)
	 * @param textureEumelanin a {@code Texture} instance
	 * @param texturePheomelanin a {@code Texture} instance
	 * @param textureSigmaA a {@code Texture} instance
	 * @throws NullPointerException thrown if, and only if, either {@code textureAlpha}, {@code textureBetaM}, {@code textureBetaN}, {@code textureColor}, {@code textureEta}, {@code textureEumelanin}, {@code texturePheomelanin} or {@code textureSigmaA}
	 *                              are {@code null}
	 */
	public HairPBRTMaterial(final Texture textureAlpha, final Texture textureBetaM, final Texture textureBetaN, final Texture textureColor, final Texture textureEta, final Texture textureEumelanin, final Texture texturePheomelanin, final Texture textureSigmaA) {
		this.textureAlpha = Objects.requireNonNull(textureAlpha, "textureAlpha == null");
		this.textureBetaM = Objects.requireNonNull(textureBetaM, "textureBetaM == null");
		this.textureBetaN = Objects.requireNonNull(textureBetaN, "textureBetaN == null");
		this.textureColor = Objects.requireNonNull(textureColor, "textureColor == null");
		this.textureEta = Objects.requireNonNull(textureEta, "textureEta == null");
		this.textureEumelanin = Objects.requireNonNull(textureEumelanin, "textureEumelanin == null");
		this.texturePheomelanin = Objects.requireNonNull(texturePheomelanin, "texturePheomelanin == null");
		this.textureSigmaA = Objects.requireNonNull(textureSigmaA, "textureSigmaA == null");
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
		
		final float alpha = this.textureAlpha.getColorRGB(intersection).average();
		final float betaM = this.textureBetaM.getColorRGB(intersection).average();
		final float betaN = this.textureBetaN.getColorRGB(intersection).average();
		final float eta = this.textureEta.getColorRGB(intersection).average();
		
		final Color3F sigmaA = doComputeSigmaA(intersection, betaN);
		
		final float h = -1.0F + 2.0F * intersection.getSurfaceIntersectionWorldSpace().getTextureCoordinates().getV();
		
		return Optional.of(new PBRTBSDF(intersection, Arrays.asList(new HairPBRTBXDF(sigmaA, alpha, betaM, betaN, eta, h)), eta));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code HairPBRTMaterial} instance.
	 * 
	 * @return a {@code String} with the name of this {@code HairPBRTMaterial} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code HairPBRTMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code HairPBRTMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new HairPBRTMaterial(%s, %s, %s, %s, %s, %s, %s, %s)", this.textureAlpha, this.textureBetaM, this.textureBetaN, this.textureColor, this.textureEta, this.textureEumelanin, this.texturePheomelanin, this.textureSigmaA);
	}
	
	/**
	 * Compares {@code object} to this {@code HairPBRTMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code HairPBRTMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code HairPBRTMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code HairPBRTMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof HairPBRTMaterial)) {
			return false;
		} else if(!Objects.equals(this.textureAlpha, HairPBRTMaterial.class.cast(object).textureAlpha)) {
			return false;
		} else if(!Objects.equals(this.textureBetaM, HairPBRTMaterial.class.cast(object).textureBetaM)) {
			return false;
		} else if(!Objects.equals(this.textureBetaN, HairPBRTMaterial.class.cast(object).textureBetaN)) {
			return false;
		} else if(!Objects.equals(this.textureColor, HairPBRTMaterial.class.cast(object).textureColor)) {
			return false;
		} else if(!Objects.equals(this.textureEta, HairPBRTMaterial.class.cast(object).textureEta)) {
			return false;
		} else if(!Objects.equals(this.textureEumelanin, HairPBRTMaterial.class.cast(object).textureEumelanin)) {
			return false;
		} else if(!Objects.equals(this.texturePheomelanin, HairPBRTMaterial.class.cast(object).texturePheomelanin)) {
			return false;
		} else if(!Objects.equals(this.textureSigmaA, HairPBRTMaterial.class.cast(object).textureSigmaA)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code HairPBRTMaterial} instance.
	 * 
	 * @return a hash code for this {@code HairPBRTMaterial} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.textureAlpha, this.textureBetaM, this.textureBetaN, this.textureColor, this.textureEta, this.textureEumelanin, this.texturePheomelanin, this.textureSigmaA);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Color3F doComputeSigmaA(final Intersection intersection, final float betaN) {
		final Color3F colorSigmaA = Color3F.saturate(this.textureSigmaA.getColorXYZ(intersection), 0.0F, Float.MAX_VALUE);
		
		if(!colorSigmaA.isBlack()) {
			return colorSigmaA;
		}
		
		final Color3F colorColor = Color3F.saturate(this.textureColor.getColorXYZ(intersection), 0.0F, Float.MAX_VALUE);
		
		if(!colorColor.isBlack()) {
			return HairPBRTBXDF.computeSigmaAFromReflectance(colorColor, betaN);
		}
		
		final Color3F colorEumelanin = this.textureEumelanin.getColorXYZ(intersection);
		final Color3F colorPheomelanin = this.texturePheomelanin.getColorXYZ(intersection);
		
		final float eumelanin = max(0.0F, colorEumelanin.average());
		final float pheomelanin = max(0.0F, colorPheomelanin.average());
		
		return HairPBRTBXDF.computeSigmaAFromConcentration(eumelanin, pheomelanin);
	}
}