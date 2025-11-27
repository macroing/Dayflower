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
import org.dayflower.scene.material.SubstrateMaterial;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;

/**
 * A {@code SubstrateMaterialGridPane} is a {@link MaterialGridPane} for {@link SubstrateMaterial} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SubstrateMaterialGridPane extends MaterialGridPane {
	private final CheckBox checkBoxIsRemappingRoughness;
	private final TexturePicker texturePickerEmission;
	private final TexturePicker texturePickerKD;
	private final TexturePicker texturePickerKS;
	private final TexturePicker texturePickerRoughnessU;
	private final TexturePicker texturePickerRoughnessV;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SubstrateMaterialGridPane} instance.
	 */
	public SubstrateMaterialGridPane() {
		this.checkBoxIsRemappingRoughness = new CheckBox();
		this.checkBoxIsRemappingRoughness.setSelected(true);
		this.texturePickerEmission = new TexturePicker(ConstantTexture.BLACK);
		this.texturePickerKD = new TexturePicker(ConstantTexture.GRAY_0_50);
		this.texturePickerKS = new TexturePicker(ConstantTexture.GRAY_0_50);
		this.texturePickerRoughnessU = new TexturePicker(new ConstantTexture(0.1F));
		this.texturePickerRoughnessV = new TexturePicker(new ConstantTexture(0.1F));
		
		add(new Text("Texture KD"), 0, 0);
		add(this.texturePickerKD, 1, 0);
		add(new Text("Texture KS"), 0, 1);
		add(this.texturePickerKS, 1, 1);
		add(new Text("Texture Emission"), 0, 2);
		add(this.texturePickerEmission, 1, 2);
		add(new Text("Texture Roughness U"), 0, 3);
		add(this.texturePickerRoughnessU, 1, 3);
		add(new Text("Texture Roughness V"), 0, 4);
		add(this.texturePickerRoughnessV, 1, 4);
		add(new Text("Is Remapping Roughness"), 0, 5);
		add(this.checkBoxIsRemappingRoughness, 1, 5);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link SubstrateMaterial} instance.
	 * 
	 * @return a {@code SubstrateMaterial} instance
	 */
	@Override
	public Material createMaterial() {
		return new SubstrateMaterial(doGetTextureKD(), doGetTextureKS(), doGetTextureEmission(), doGetTextureRoughnessU(), doGetTextureRoughnessV(), doGetIsRemappingRoughness());
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