/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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
import org.dayflower.scene.material.TranslucentMaterial;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;

/**
 * A {@code TranslucentMaterialGridPane} is a {@link MaterialGridPane} for {@link TranslucentMaterial} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class TranslucentMaterialGridPane extends MaterialGridPane {
	private final CheckBox checkBoxIsRemappingRoughness;
	private final TexturePicker texturePickerEmission;
	private final TexturePicker texturePickerKD;
	private final TexturePicker texturePickerKS;
	private final TexturePicker texturePickerReflectance;
	private final TexturePicker texturePickerRoughness;
	private final TexturePicker texturePickerTransmittance;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code TranslucentMaterialGridPane} instance.
	 */
	public TranslucentMaterialGridPane() {
		this.checkBoxIsRemappingRoughness = new CheckBox();
		this.checkBoxIsRemappingRoughness.setSelected(true);
		this.texturePickerEmission = new TexturePicker(ConstantTexture.BLACK);
		this.texturePickerKD = new TexturePicker(ConstantTexture.GRAY_0_25);
		this.texturePickerKS = new TexturePicker(ConstantTexture.GRAY_0_25);
		this.texturePickerReflectance = new TexturePicker(ConstantTexture.GRAY_0_50);
		this.texturePickerRoughness = new TexturePicker(new ConstantTexture(0.1F));
		this.texturePickerTransmittance = new TexturePicker(ConstantTexture.GRAY_0_50);
		
		add(new Text("Texture KD"), 0, 0);
		add(this.texturePickerKD, 1, 0);
		add(new Text("Texture KS"), 0, 1);
		add(this.texturePickerKS, 1, 1);
		add(new Text("Texture Emission"), 0, 2);
		add(this.texturePickerEmission, 1, 2);
		add(new Text("Texture Roughness"), 0, 3);
		add(this.texturePickerRoughness, 1, 3);
		add(new Text("Texture Reflectance"), 0, 4);
		add(this.texturePickerReflectance, 1, 4);
		add(new Text("Texture Transmittance"), 0, 5);
		add(this.texturePickerTransmittance, 1, 5);
		add(new Text("Is Remapping Roughness"), 0, 6);
		add(this.checkBoxIsRemappingRoughness, 1, 6);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link TranslucentMaterial} instance.
	 * 
	 * @return a {@code TranslucentMaterial} instance
	 */
	@Override
	public Material createMaterial() {
		return new TranslucentMaterial(doGetTextureKD(), doGetTextureKS(), doGetTextureEmission(), doGetTextureRoughness(), doGetTextureReflectance(), doGetTextureTransmittance(), doGetIsRemappingRoughness());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Texture doGetTextureEmission() {
		return this.texturePickerEmission.getTexture();
	}
	
	private Texture doGetTextureKD() {
		return this.texturePickerKD.getTexture();
	}
	
	private Texture doGetTextureKS() {
		return this.texturePickerKS.getTexture();
	}
	
	private Texture doGetTextureReflectance() {
		return this.texturePickerReflectance.getTexture();
	}
	
	private Texture doGetTextureRoughness() {
		return this.texturePickerRoughness.getTexture();
	}
	
	private Texture doGetTextureTransmittance() {
		return this.texturePickerTransmittance.getTexture();
	}
	
	private boolean doGetIsRemappingRoughness() {
		return this.checkBoxIsRemappingRoughness.isSelected();
	}
}