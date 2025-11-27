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
import org.dayflower.scene.material.GlossyMaterial;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

import javafx.scene.text.Text;

/**
 * A {@code GlossyMaterialGridPane} is a {@link MaterialGridPane} for {@link GlossyMaterial} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class GlossyMaterialGridPane extends MaterialGridPane {
	private final TexturePicker texturePickerEmission;
	private final TexturePicker texturePickerKR;
	private final TexturePicker texturePickerRoughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code GlossyMaterialGridPane} instance.
	 */
	public GlossyMaterialGridPane() {
		this.texturePickerEmission = new TexturePicker(ConstantTexture.BLACK);
		this.texturePickerKR = new TexturePicker(ConstantTexture.GRAY_0_50);
		this.texturePickerRoughness = new TexturePicker(ConstantTexture.GRAY_0_20);
		
		add(new Text("Texture KR"), 0, 0);
		add(this.texturePickerKR, 1, 0);
		add(new Text("Texture Emission"), 0, 1);
		add(this.texturePickerEmission, 1, 1);
		add(new Text("Texture Roughness"), 0, 2);
		add(this.texturePickerRoughness, 1, 2);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link GlossyMaterial} instance.
	 * 
	 * @return a {@code GlossyMaterial} instance
	 */
	@Override
	public Material createMaterial() {
		return new GlossyMaterial(doGetTextureKR(), doGetTextureEmission(), doGetTextureRoughness());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Texture doGetTextureEmission() {
		return this.texturePickerEmission.getTexture();
	}
	
	private Texture doGetTextureKR() {
		return this.texturePickerKR.getTexture();
	}
	
	private Texture doGetTextureRoughness() {
		return this.texturePickerRoughness.getTexture();
	}
}