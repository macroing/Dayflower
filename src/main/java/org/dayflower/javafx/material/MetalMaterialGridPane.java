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
import org.dayflower.scene.material.MetalMaterial;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

import org.macroing.art4j.color.Color3F;
import org.macroing.java.lang.Doubles;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;

/**
 * A {@code MetalMaterialGridPane} is a {@link MaterialGridPane} for {@link MetalMaterial} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MetalMaterialGridPane extends MaterialGridPane {
	private static final String NAME_AG = "AG";
	private static final String NAME_AL = "AL";
	private static final String NAME_AU = "AU";
	private static final String NAME_BE = "BE";
	private static final String NAME_CR = "CR";
	private static final String NAME_CU = "CU";
	private static final String NAME_HG = "HG";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final CheckBox checkBoxIsRemappingRoughness;
	private final TexturePicker texturePickerEmission;
	private final TexturePicker texturePickerEta;
	private final TexturePicker texturePickerK;
	private final TexturePicker texturePickerRoughnessU;
	private final TexturePicker texturePickerRoughnessV;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MetalMaterialGridPane} instance.
	 */
	public MetalMaterialGridPane() {
		this.checkBoxIsRemappingRoughness = new CheckBox();
		this.checkBoxIsRemappingRoughness.setSelected(true);
		this.texturePickerEmission = new TexturePicker(ConstantTexture.BLACK);
		this.texturePickerEta = new TexturePicker(new ConstantTexture(Color3F.AU_ETA));
		this.texturePickerK = new TexturePicker(new ConstantTexture(Color3F.AU_K));
		this.texturePickerRoughnessU = new TexturePicker(new ConstantTexture(0.01F));
		this.texturePickerRoughnessV = new TexturePicker(new ConstantTexture(0.01F));
		
		add(new Text("Texture K"), 0, 0);
		add(this.texturePickerK, 1, 0);
		add(new Text("Texture Eta"), 0, 1);
		add(this.texturePickerEta, 1, 1);
		add(new Text("Texture Emission"), 0, 2);
		add(this.texturePickerEmission, 1, 2);
		add(new Text("Texture Roughness U"), 0, 3);
		add(this.texturePickerRoughnessU, 1, 3);
		add(new Text("Texture Roughness V"), 0, 4);
		add(this.texturePickerRoughnessV, 1, 4);
		add(new Text("Is Remapping Roughness"), 0, 5);
		add(this.checkBoxIsRemappingRoughness, 1, 5);
		
		final
		ComboBox<String> comboBox = new ComboBox<>();
		comboBox.getItems().addAll(NAME_AG, NAME_AL, NAME_AU, NAME_BE, NAME_CR, NAME_CU, NAME_HG);
		comboBox.setMaxWidth(Doubles.MAX_VALUE);
		comboBox.setValue(NAME_AU);
		
		final
		Button button = new Button("Select");
		button.setMaxWidth(Doubles.MAX_VALUE);
		button.setOnAction(e -> {
			switch(comboBox.getValue()) {
				case NAME_AG:
					this.texturePickerEta.setTexture(new ConstantTexture(Color3F.AG_ETA));
					this.texturePickerK.setTexture(new ConstantTexture(Color3F.AG_K));
					
					break;
				case NAME_AL:
					this.texturePickerEta.setTexture(new ConstantTexture(Color3F.AL_ETA));
					this.texturePickerK.setTexture(new ConstantTexture(Color3F.AL_K));
					
					break;
				case NAME_AU:
					this.texturePickerEta.setTexture(new ConstantTexture(Color3F.AU_ETA));
					this.texturePickerK.setTexture(new ConstantTexture(Color3F.AU_K));
					
					break;
				case NAME_BE:
					this.texturePickerEta.setTexture(new ConstantTexture(Color3F.BE_ETA));
					this.texturePickerK.setTexture(new ConstantTexture(Color3F.BE_K));
					
					break;
				case NAME_CR:
					this.texturePickerEta.setTexture(new ConstantTexture(Color3F.CR_ETA));
					this.texturePickerK.setTexture(new ConstantTexture(Color3F.CR_K));
					
					break;
				case NAME_CU:
					this.texturePickerEta.setTexture(new ConstantTexture(Color3F.CU_ETA));
					this.texturePickerK.setTexture(new ConstantTexture(Color3F.CU_K));
					
					break;
				case NAME_HG:
					this.texturePickerEta.setTexture(new ConstantTexture(Color3F.HG_ETA));
					this.texturePickerK.setTexture(new ConstantTexture(Color3F.HG_K));
					
					break;
				default:
					break;
			}
		});
		
		add(comboBox, 0, 6);
		add(button, 1, 6);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link MetalMaterial} instance.
	 * 
	 * @return a {@code MetalMaterial} instance
	 */
	@Override
	public Material createMaterial() {
		return new MetalMaterial(doGetTextureK(), doGetTextureEta(), doGetTextureEmission(), doGetTextureRoughnessU(), doGetTextureRoughnessV(), doGetIsRemappingRoughness());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Texture doGetTextureEmission() {
		return this.texturePickerEmission.getTexture();
	}
	
	private Texture doGetTextureEta() {
		return this.texturePickerEta.getTexture();
	}
	
	private Texture doGetTextureK() {
		return this.texturePickerK.getTexture();
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