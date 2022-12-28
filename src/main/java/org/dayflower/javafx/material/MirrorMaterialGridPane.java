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
import org.dayflower.scene.material.MirrorMaterial;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

import javafx.scene.text.Text;

/**
 * A {@code MirrorMaterialGridPane} is a {@link MaterialGridPane} for {@link MirrorMaterial} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MirrorMaterialGridPane extends MaterialGridPane {
	private final TexturePicker texturePickerEmission;
	private final TexturePicker texturePickerKR;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MirrorMaterialGridPane} instance.
	 */
	public MirrorMaterialGridPane() {
		this.texturePickerEmission = new TexturePicker(ConstantTexture.BLACK);
		this.texturePickerKR = new TexturePicker(ConstantTexture.WHITE);
		
		add(new Text("Texture KR"), 0, 0);
		add(this.texturePickerKR, 1, 0);
		add(new Text("Texture Emission"), 0, 1);
		add(this.texturePickerEmission, 1, 1);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link MirrorMaterial} instance.
	 * 
	 * @return a {@code MirrorMaterial} instance
	 */
	@Override
	public Material createMaterial() {
		return new MirrorMaterial(doGetTextureKR(), doGetTextureEmission());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Texture doGetTextureEmission() {
		return this.texturePickerEmission.getTexture();
	}
	
	private Texture doGetTextureKR() {
		return this.texturePickerKR.getTexture();
	}
}