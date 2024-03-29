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
import org.dayflower.scene.material.ClearCoatMaterial;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

import javafx.scene.text.Text;

/**
 * A {@code ClearCoatMaterialGridPane} is a {@link MaterialGridPane} for {@link ClearCoatMaterial} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ClearCoatMaterialGridPane extends MaterialGridPane {
	private final TexturePicker texturePickerEmission;
	private final TexturePicker texturePickerKD;
	private final TexturePicker texturePickerKS;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ClearCoatMaterialGridPane} instance.
	 */
	public ClearCoatMaterialGridPane() {
		this.texturePickerEmission = new TexturePicker(ConstantTexture.BLACK);
		this.texturePickerKD = new TexturePicker(ConstantTexture.GRAY_0_50);
		this.texturePickerKS = new TexturePicker(ConstantTexture.WHITE);
		
		add(new Text("Texture KD"), 0, 0);
		add(this.texturePickerKD, 1, 0);
		add(new Text("Texture KS"), 0, 1);
		add(this.texturePickerKS, 1, 1);
		add(new Text("Texture Emission"), 0, 2);
		add(this.texturePickerEmission, 1, 2);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link ClearCoatMaterial} instance.
	 * 
	 * @return a {@code ClearCoatMaterial} instance
	 */
	@Override
	public Material createMaterial() {
		return new ClearCoatMaterial(doGetTextureKD(), doGetTextureKS(), doGetTextureEmission());
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
}