/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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
package org.dayflower.javafx.material;

import org.dayflower.javafx.texture.TexturePicker;
import org.dayflower.scene.Material;
import org.dayflower.scene.material.DisneyMaterial;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;

/**
 * A {@code DisneyMaterialGridPane} is a {@link MaterialGridPane} for {@link DisneyMaterial} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DisneyMaterialGridPane extends MaterialGridPane {
	private final CheckBox checkBoxIsThin;
	private final TexturePicker texturePickerAnisotropic;
	private final TexturePicker texturePickerClearCoat;
	private final TexturePicker texturePickerClearCoatGloss;
	private final TexturePicker texturePickerColor;
	private final TexturePicker texturePickerDiffuseTransmission;
	private final TexturePicker texturePickerEmission;
	private final TexturePicker texturePickerEta;
	private final TexturePicker texturePickerFlatness;
	private final TexturePicker texturePickerMetallic;
	private final TexturePicker texturePickerRoughness;
	private final TexturePicker texturePickerScatterDistance;
	private final TexturePicker texturePickerSheen;
	private final TexturePicker texturePickerSheenTint;
	private final TexturePicker texturePickerSpecularTint;
	private final TexturePicker texturePickerSpecularTransmission;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DisneyMaterialGridPane} instance.
	 */
	public DisneyMaterialGridPane() {
		this.checkBoxIsThin = new CheckBox();
		this.checkBoxIsThin.setSelected(false);
		this.texturePickerAnisotropic = new TexturePicker(ConstantTexture.BLACK);
		this.texturePickerClearCoat = new TexturePicker(ConstantTexture.BLACK);
		this.texturePickerClearCoatGloss = new TexturePicker(ConstantTexture.WHITE);
		this.texturePickerColor = new TexturePicker(ConstantTexture.GRAY_0_50);
		this.texturePickerDiffuseTransmission = new TexturePicker(ConstantTexture.WHITE);
		this.texturePickerEmission = new TexturePicker(ConstantTexture.BLACK);
		this.texturePickerEta = new TexturePicker(ConstantTexture.GRAY_1_50);
		this.texturePickerFlatness = new TexturePicker(ConstantTexture.BLACK);
		this.texturePickerMetallic = new TexturePicker(ConstantTexture.BLACK);
		this.texturePickerRoughness = new TexturePicker(ConstantTexture.GRAY_0_50);
		this.texturePickerScatterDistance = new TexturePicker(ConstantTexture.BLACK);
		this.texturePickerSheen = new TexturePicker(ConstantTexture.BLACK);
		this.texturePickerSheenTint = new TexturePicker(ConstantTexture.GRAY_0_50);
		this.texturePickerSpecularTint = new TexturePicker(ConstantTexture.BLACK);
		this.texturePickerSpecularTransmission = new TexturePicker(ConstantTexture.BLACK);
		
		add(new Text("Texture Color"), 0, 0);
		add(this.texturePickerColor, 1, 0);
		add(new Text("Texture Emission"), 0, 1);
		add(this.texturePickerEmission, 1, 1);
		add(new Text("Texture Scatter Distance"), 0, 2);
		add(this.texturePickerScatterDistance, 1, 2);
		add(new Text("Texture Anisotropic"), 0, 3);
		add(this.texturePickerAnisotropic, 1, 3);
		add(new Text("Texture Clear Coat"), 0, 4);
		add(this.texturePickerClearCoat, 1, 4);
		add(new Text("Texture Clear Coat Gloss"), 0, 5);
		add(this.texturePickerClearCoatGloss, 1, 5);
		add(new Text("Texture Diffuse Transmission"), 0, 6);
		add(this.texturePickerDiffuseTransmission, 1, 6);
		add(new Text("Texture Eta"), 0, 7);
		add(this.texturePickerEta, 1, 7);
		add(new Text("Texture Flatness"), 0, 8);
		add(this.texturePickerFlatness, 1, 8);
		add(new Text("Texture Metallic"), 0, 9);
		add(this.texturePickerMetallic, 1, 9);
		add(new Text("Texture Roughness"), 0, 10);
		add(this.texturePickerRoughness, 1, 10);
		add(new Text("Texture Sheen"), 0, 11);
		add(this.texturePickerSheen, 1, 11);
		add(new Text("Texture Sheen Tint"), 0, 12);
		add(this.texturePickerSheenTint, 1, 12);
		add(new Text("Texture Specular Tint"), 0, 13);
		add(this.texturePickerSpecularTint, 1, 13);
		add(new Text("Texture Specular Transmission"), 0, 14);
		add(this.texturePickerSpecularTransmission, 1, 14);
		add(new Text("Is Thin"), 0, 15);
		add(this.checkBoxIsThin, 1, 15);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link DisneyMaterial} instance.
	 * 
	 * @return a {@code DisneyMaterial} instance
	 */
	@Override
	public Material createMaterial() {
		return new DisneyMaterial(doGetTextureColor(), doGetTextureEmission(), doGetTextureScatterDistance(), doGetTextureAnisotropic(), doGetTextureClearCoat(), doGetTextureClearCoatGloss(), doGetTextureDiffuseTransmission(), doGetTextureEta(), doGetTextureFlatness(), doGetTextureMetallic(), doGetTextureRoughness(), doGetTextureSheen(), doGetTextureSheenTint(), doGetTextureSpecularTint(), doGetTextureSpecularTransmission(), doGetIsThin());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Texture doGetTextureAnisotropic() {
		return this.texturePickerAnisotropic.getTexture();
	}
	
	private Texture doGetTextureColor() {
		return this.texturePickerColor.getTexture();
	}
	
	private Texture doGetTextureClearCoat() {
		return this.texturePickerClearCoat.getTexture();
	}
	
	private Texture doGetTextureClearCoatGloss() {
		return this.texturePickerClearCoatGloss.getTexture();
	}
	
	private Texture doGetTextureDiffuseTransmission() {
		return this.texturePickerDiffuseTransmission.getTexture();
	}
	
	private Texture doGetTextureEmission() {
		return this.texturePickerEmission.getTexture();
	}
	
	private Texture doGetTextureEta() {
		return this.texturePickerEta.getTexture();
	}
	
	private Texture doGetTextureFlatness() {
		return this.texturePickerFlatness.getTexture();
	}
	
	private Texture doGetTextureMetallic() {
		return this.texturePickerMetallic.getTexture();
	}
	
	private Texture doGetTextureRoughness() {
		return this.texturePickerRoughness.getTexture();
	}
	
	private Texture doGetTextureScatterDistance() {
		return this.texturePickerScatterDistance.getTexture();
	}
	
	private Texture doGetTextureSheen() {
		return this.texturePickerSheen.getTexture();
	}
	
	private Texture doGetTextureSheenTint() {
		return this.texturePickerSheenTint.getTexture();
	}
	
	private Texture doGetTextureSpecularTint() {
		return this.texturePickerSpecularTint.getTexture();
	}
	
	private Texture doGetTextureSpecularTransmission() {
		return this.texturePickerSpecularTransmission.getTexture();
	}
	
	private boolean doGetIsThin() {
		return this.checkBoxIsThin.isSelected();
	}
}