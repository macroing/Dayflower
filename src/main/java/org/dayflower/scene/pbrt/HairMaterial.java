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

import static org.dayflower.util.Floats.max;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Texture;
import org.dayflower.scene.texture.ConstantTexture;

/**
 * A {@code HairMaterial} is an implementation of {@link PBRTMaterial} that represents hair.
 * <p>
 * This class is immutable and thread-safe as long as all {@link Texture} instances are.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class HairMaterial implements PBRTMaterial {
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
	 * Constructs a new {@code HairMaterial} instance.
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
	 * Texture textureSigmaA = new ConstantTexture(HairBXDF.computeSigmaAFromConcentration(1.3F, 0.0F));
	 * 
	 * new HairMaterial(textureAlpha, textureBetaM, textureBetaN, textureColor, textureEta, textureEumelanin, texturePheomelanin, textureSigmaA);
	 * }
	 * </pre>
	 */
	public HairMaterial() {
		this(ConstantTexture.GRAY_2_00, ConstantTexture.GRAY_0_30, ConstantTexture.GRAY_0_30, ConstantTexture.BLACK, ConstantTexture.GRAY_1_55, ConstantTexture.BLACK, ConstantTexture.BLACK, new ConstantTexture(HairBXDF.computeSigmaAFromConcentration(1.3F, 0.0F)));
	}
	
	/**
	 * Constructs a new {@code HairMaterial} instance.
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
	public HairMaterial(final Texture textureAlpha, final Texture textureBetaM, final Texture textureBetaN, final Texture textureColor, final Texture textureEta, final Texture textureEumelanin, final Texture texturePheomelanin, final Texture textureSigmaA) {
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
		
		final float alpha = this.textureAlpha.getColorXYZ(intersection).average();
		final float betaM = this.textureBetaM.getColorXYZ(intersection).average();
		final float betaN = this.textureBetaN.getColorXYZ(intersection).average();
		final float eta = this.textureEta.getColorXYZ(intersection).average();
		
		final Color3F sigmaA = doComputeSigmaA(intersection, betaN);
		
		final float h = -1.0F + 2.0F * intersection.getSurfaceIntersectionWorldSpace().getTextureCoordinates().getV();
		
		return Optional.of(new BSDF(intersection, Arrays.asList(new HairBXDF(sigmaA, alpha, betaM, betaN, eta, h)), eta));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code HairMaterial} instance.
	 * 
	 * @return a {@code String} representation of this {@code HairMaterial} instance
	 */
	@Override
	public String toString() {
		return String.format("new HairMaterial(%s, %s, %s, %s, %s, %s, %s, %s)", this.textureAlpha, this.textureBetaM, this.textureBetaN, this.textureColor, this.textureEta, this.textureEumelanin, this.texturePheomelanin, this.textureSigmaA);
	}
	
	/**
	 * Compares {@code object} to this {@code HairMaterial} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code HairMaterial}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code HairMaterial} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code HairMaterial}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof HairMaterial)) {
			return false;
		} else if(!Objects.equals(this.textureAlpha, HairMaterial.class.cast(object).textureAlpha)) {
			return false;
		} else if(!Objects.equals(this.textureBetaM, HairMaterial.class.cast(object).textureBetaM)) {
			return false;
		} else if(!Objects.equals(this.textureBetaN, HairMaterial.class.cast(object).textureBetaN)) {
			return false;
		} else if(!Objects.equals(this.textureColor, HairMaterial.class.cast(object).textureColor)) {
			return false;
		} else if(!Objects.equals(this.textureEta, HairMaterial.class.cast(object).textureEta)) {
			return false;
		} else if(!Objects.equals(this.textureEumelanin, HairMaterial.class.cast(object).textureEumelanin)) {
			return false;
		} else if(!Objects.equals(this.texturePheomelanin, HairMaterial.class.cast(object).texturePheomelanin)) {
			return false;
		} else if(!Objects.equals(this.textureSigmaA, HairMaterial.class.cast(object).textureSigmaA)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code HairMaterial} instance.
	 * 
	 * @return a hash code for this {@code HairMaterial} instance
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
			return HairBXDF.computeSigmaAFromReflectance(colorColor, betaN);
		}
		
		final Color3F colorEumelanin = this.textureEumelanin.getColorXYZ(intersection);
		final Color3F colorPheomelanin = this.texturePheomelanin.getColorXYZ(intersection);
		
		final float eumelanin = max(0.0F, colorEumelanin.average());
		final float pheomelanin = max(0.0F, colorPheomelanin.average());
		
		return HairBXDF.computeSigmaAFromConcentration(eumelanin, pheomelanin);
	}
}