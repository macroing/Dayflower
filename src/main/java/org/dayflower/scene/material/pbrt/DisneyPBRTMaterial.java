/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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

import java.lang.reflect.Field;
import java.util.Objects;

import org.dayflower.image.Color3F;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

//TODO: Add Javadocs!
public final class DisneyPBRTMaterial {
	private final Texture textureAnisotropic;
	private final Texture textureClearCoat;
	private final Texture textureClearCoatGloss;
	private final Texture textureColor;
	private final Texture textureDiffuseTransmission;
	private final Texture textureEmission;
	private final Texture textureEta;
	private final Texture textureFlatness;
	private final Texture textureMetallic;
	private final Texture textureRoughness;
	private final Texture textureScatterDistance;
	private final Texture textureSheen;
	private final Texture textureSheenTint;
	private final Texture textureSpecularTint;
	private final Texture textureSpecularTransmission;
	private final boolean isThin;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial() {
		this(Color3F.GRAY_0_50);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Color3F colorColor) {
		this(colorColor, Color3F.BLACK);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission) {
		this(colorColor, colorEmission, Color3F.BLACK);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance) {
		this(colorColor, colorEmission, colorScatterDistance, 0.0F);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, 0.0F);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, 1.0F);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, 1.0F);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, 1.5F);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, 0.0F);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, 0.0F);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, 0.5F);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic, final float floatRoughness) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, 0.0F);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic, final float floatRoughness, final float floatSheen) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, 0.5F);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic, final float floatRoughness, final float floatSheen, final float floatSheenTint) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, floatSheenTint, 0.0F);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic, final float floatRoughness, final float floatSheen, final float floatSheenTint, final float floatSpecularTint) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, floatSheenTint, floatSpecularTint, 0.0F);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic, final float floatRoughness, final float floatSheen, final float floatSheenTint, final float floatSpecularTint, final float floatSpecularTransmission) {
		this(colorColor, colorEmission, colorScatterDistance, floatAnisotropic, floatClearCoat, floatClearCoatGloss, floatDiffuseTransmission, floatEta, floatFlatness, floatMetallic, floatRoughness, floatSheen, floatSheenTint, floatSpecularTint, floatSpecularTransmission, false);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Color3F colorColor, final Color3F colorEmission, final Color3F colorScatterDistance, final float floatAnisotropic, final float floatClearCoat, final float floatClearCoatGloss, final float floatDiffuseTransmission, final float floatEta, final float floatFlatness, final float floatMetallic, final float floatRoughness, final float floatSheen, final float floatSheenTint, final float floatSpecularTint, final float floatSpecularTransmission, final boolean isThin) {
		this.textureColor = new ConstantTexture(Objects.requireNonNull(colorColor, "colorColor == null"));
		this.textureEmission = new ConstantTexture(Objects.requireNonNull(colorEmission, "colorEmission == null"));
		this.textureScatterDistance = new ConstantTexture(Objects.requireNonNull(colorScatterDistance, "colorScatterDistance == null"));
		this.textureAnisotropic = new ConstantTexture(floatAnisotropic);
		this.textureClearCoat = new ConstantTexture(floatClearCoat);
		this.textureClearCoatGloss = new ConstantTexture(floatClearCoatGloss);
		this.textureDiffuseTransmission = new ConstantTexture(floatDiffuseTransmission);
		this.textureEta = new ConstantTexture(floatEta);
		this.textureFlatness = new ConstantTexture(floatFlatness);
		this.textureMetallic = new ConstantTexture(floatMetallic);
		this.textureRoughness = new ConstantTexture(floatRoughness);
		this.textureSheen = new ConstantTexture(floatSheen);
		this.textureSheenTint = new ConstantTexture(floatSheenTint);
		this.textureSpecularTint = new ConstantTexture(floatSpecularTint);
		this.textureSpecularTransmission = new ConstantTexture(floatSpecularTransmission);
		this.isThin = isThin;
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Texture textureColor) {
		this(textureColor, ConstantTexture.BLACK);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission) {
		this(textureColor, textureEmission, ConstantTexture.BLACK);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance) {
		this(textureColor, textureEmission, textureScatterDistance, ConstantTexture.BLACK);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, ConstantTexture.BLACK);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, ConstantTexture.WHITE);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, ConstantTexture.WHITE);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, ConstantTexture.GRAY_1_50);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, ConstantTexture.BLACK);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, ConstantTexture.BLACK);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, ConstantTexture.GRAY_0_50);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic, final Texture textureRoughness) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, ConstantTexture.BLACK);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic, final Texture textureRoughness, final Texture textureSheen) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, ConstantTexture.GRAY_0_50);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic, final Texture textureRoughness, final Texture textureSheen, final Texture textureSheenTint) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, textureSheenTint, ConstantTexture.BLACK);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic, final Texture textureRoughness, final Texture textureSheen, final Texture textureSheenTint, final Texture textureSpecularTint) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, textureSheenTint, textureSpecularTint, ConstantTexture.BLACK);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic, final Texture textureRoughness, final Texture textureSheen, final Texture textureSheenTint, final Texture textureSpecularTint, final Texture textureSpecularTransmission) {
		this(textureColor, textureEmission, textureScatterDistance, textureAnisotropic, textureClearCoat, textureClearCoatGloss, textureDiffuseTransmission, textureEta, textureFlatness, textureMetallic, textureRoughness, textureSheen, textureSheenTint, textureSpecularTint, textureSpecularTransmission, false);
	}
	
//	TODO: Add Javadocs!
	public DisneyPBRTMaterial(final Texture textureColor, final Texture textureEmission, final Texture textureScatterDistance, final Texture textureAnisotropic, final Texture textureClearCoat, final Texture textureClearCoatGloss, final Texture textureDiffuseTransmission, final Texture textureEta, final Texture textureFlatness, final Texture textureMetallic, final Texture textureRoughness, final Texture textureSheen, final Texture textureSheenTint, final Texture textureSpecularTint, final Texture textureSpecularTransmission, final boolean isThin) {
		this.textureColor = Objects.requireNonNull(textureColor, "textureColor == null");
		this.textureEmission = Objects.requireNonNull(textureEmission, "textureEmission == null");
		this.textureScatterDistance = Objects.requireNonNull(textureScatterDistance, "textureScatterDistance == null");
		this.textureAnisotropic = Objects.requireNonNull(textureAnisotropic, "textureAnisotropic == null");
		this.textureClearCoat = Objects.requireNonNull(textureClearCoat, "textureClearCoat == null");
		this.textureClearCoatGloss = Objects.requireNonNull(textureClearCoatGloss, "textureClearCoatGloss == null");
		this.textureDiffuseTransmission = Objects.requireNonNull(textureDiffuseTransmission, "textureDiffuseTransmission == null");
		this.textureEta = Objects.requireNonNull(textureEta, "textureEta == null");
		this.textureFlatness = Objects.requireNonNull(textureFlatness, "textureFlatness == null");
		this.textureMetallic = Objects.requireNonNull(textureMetallic, "textureMetallic == null");
		this.textureRoughness = Objects.requireNonNull(textureRoughness, "textureRoughness == null");
		this.textureSheen = Objects.requireNonNull(textureSheen, "textureSheen == null");
		this.textureSheenTint = Objects.requireNonNull(textureSheenTint, "textureSheenTint == null");
		this.textureSpecularTint = Objects.requireNonNull(textureSpecularTint, "textureSpecularTint == null");
		this.textureSpecularTransmission = Objects.requireNonNull(textureSpecularTransmission, "textureSpecularTransmission == null");
		this.isThin = isThin;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
}