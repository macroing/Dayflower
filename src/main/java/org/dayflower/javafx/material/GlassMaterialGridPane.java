/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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
import org.dayflower.scene.material.GlassMaterial;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;

/**
 * A {@code GlassMaterialGridPane} is a {@link MaterialGridPane} for {@link GlassMaterial} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class GlassMaterialGridPane extends MaterialGridPane {
	private final CheckBox checkBoxIsRemappingRoughness;
	private final TexturePicker texturePickerEmission;
	private final TexturePicker texturePickerEta;
	private final TexturePicker texturePickerKR;
	private final TexturePicker texturePickerKT;
	private final TexturePicker texturePickerRoughnessU;
	private final TexturePicker texturePickerRoughnessV;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code GlassMaterialGridPane} instance.
	 */
	public GlassMaterialGridPane() {
		this.checkBoxIsRemappingRoughness = new CheckBox();
		this.checkBoxIsRemappingRoughness.setSelected(true);
		this.texturePickerEmission = new TexturePicker(ConstantTexture.BLACK);
		this.texturePickerEta = new TexturePicker(ConstantTexture.GRAY_1_50);
		this.texturePickerKR = new TexturePicker(ConstantTexture.WHITE);
		this.texturePickerKT = new TexturePicker(ConstantTexture.WHITE);
		this.texturePickerRoughnessU = new TexturePicker(ConstantTexture.BLACK);
		this.texturePickerRoughnessV = new TexturePicker(ConstantTexture.BLACK);
		
		add(new Text("Texture KR"), 0, 0);
		add(this.texturePickerKR, 1, 0);
		add(new Text("Texture KT"), 0, 1);
		add(this.texturePickerKT, 1, 1);
		add(new Text("Texture Emission"), 0, 2);
		add(this.texturePickerEmission, 1, 2);
		add(new Text("Texture Eta"), 0, 3);
		add(this.texturePickerEta, 1, 3);
		add(new Text("Texture Roughness U"), 0, 4);
		add(this.texturePickerRoughnessU, 1, 4);
		add(new Text("Texture Roughness V"), 0, 5);
		add(this.texturePickerRoughnessV, 1, 5);
		add(new Text("Is Remapping Roughness"), 0, 6);
		add(this.checkBoxIsRemappingRoughness, 1, 6);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link PlasticMaterial} instance.
	 * 
	 * @return a {@code PlasticMaterial} instance
	 */
	@Override
	public Material createMaterial() {
		return new GlassMaterial(doGetTextureKR(), doGetTextureKT(), doGetTextureEmission(), doGetTextureEta(), doGetTextureRoughnessU(), doGetTextureRoughnessV(), doGetIsRemappingRoughness());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Texture doGetTextureEmission() {
		return this.texturePickerEmission.getTexture();
	}
	
	private Texture doGetTextureEta() {
		return this.texturePickerEta.getTexture();
	}
	
	private Texture doGetTextureKR() {
		return this.texturePickerKR.getTexture();
	}
	
	private Texture doGetTextureKT() {
		return this.texturePickerKT.getTexture();
	}
	
	private Texture doGetTextureRoughnessU() {
		return this.texturePickerRoughnessU.getTexture();
	}
	
	private Texture doGetTextureRoughnessV() {
		return this.texturePickerRoughnessV.getTexture();
	}
	
	private boolean doGetIsRemappingRoughness() {
		return this.checkBoxIsRemappingRoughness.isSelected();
	}
}