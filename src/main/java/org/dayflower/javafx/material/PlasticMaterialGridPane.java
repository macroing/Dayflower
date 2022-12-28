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
import org.dayflower.scene.material.PlasticMaterial;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

import org.macroing.art4j.color.Color3F;

import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;

/**
 * A {@code PlasticMaterialGridPane} is a {@link MaterialGridPane} for {@link PlasticMaterial} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PlasticMaterialGridPane extends MaterialGridPane {
	private final CheckBox checkBoxIsRemappingRoughness;
	private final TexturePicker texturePickerEmission;
	private final TexturePicker texturePickerKD;
	private final TexturePicker texturePickerKS;
	private final TexturePicker texturePickerRoughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PlasticMaterialGridPane} instance.
	 */
	public PlasticMaterialGridPane() {
		this.checkBoxIsRemappingRoughness = new CheckBox();
		this.checkBoxIsRemappingRoughness.setSelected(true);
		this.texturePickerEmission = new TexturePicker(ConstantTexture.BLACK);
		this.texturePickerKD = new TexturePicker(new ConstantTexture(new Color3F(0.2F, 0.2F, 0.5F)));
		this.texturePickerKS = new TexturePicker(ConstantTexture.GRAY_0_50);
		this.texturePickerRoughness = new TexturePicker(new ConstantTexture(0.025F));
		
		add(new Text("Texture KD"), 0, 0);
		add(this.texturePickerKD, 1, 0);
		add(new Text("Texture KS"), 0, 1);
		add(this.texturePickerKS, 1, 1);
		add(new Text("Texture Emission"), 0, 2);
		add(this.texturePickerEmission, 1, 2);
		add(new Text("Texture Roughness"), 0, 3);
		add(this.texturePickerRoughness, 1, 3);
		add(new Text("Is Remapping Roughness"), 0, 4);
		add(this.checkBoxIsRemappingRoughness, 1, 4);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link PlasticMaterial} instance.
	 * 
	 * @return a {@code PlasticMaterial} instance
	 */
	@Override
	public Material createMaterial() {
		return new PlasticMaterial(doGetTextureKD(), doGetTextureKS(), doGetTextureEmission(), doGetTextureRoughness(), doGetIsRemappingRoughness());
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
	
	private Texture doGetTextureRoughness() {
		return this.texturePickerRoughness.getTexture();
	}
	
	private boolean doGetIsRemappingRoughness() {
		return this.checkBoxIsRemappingRoughness.isSelected();
	}
}