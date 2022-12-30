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
package org.dayflower.javafx.texture;

import org.dayflower.scene.texture.BlendTexture;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

import org.macroing.art4j.color.Color3F;
import org.macroing.javafx.scene.control.TextFields;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * A {@code BlendTextureGridPane} is a {@link TextureGridPane} for {@link BlendTexture} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BlendTextureGridPane extends TextureGridPane {
	private final TextField textFieldBlendFactorB;
	private final TextField textFieldBlendFactorG;
	private final TextField textFieldBlendFactorR;
	private final TexturePicker texturePickerA;
	private final TexturePicker texturePickerB;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code BlendTextureGridPane} instance.
	 */
	public BlendTextureGridPane() {
		this.textFieldBlendFactorB = TextFields.createTextField(0.5F);
		this.textFieldBlendFactorG = TextFields.createTextField(0.5F);
		this.textFieldBlendFactorR = TextFields.createTextField(0.5F);
		this.texturePickerA = new TexturePicker(new ConstantTexture(Color3F.GRAY));
		this.texturePickerB = new TexturePicker(new ConstantTexture(Color3F.GRAY));
		
		add(new Text("Texture A"), 0, 0);
		add(this.texturePickerA, 1, 0);
		add(new Text("Texture B"), 0, 1);
		add(this.texturePickerB, 1, 1);
		add(new Text("Blend Factor R"), 0, 2);
		add(this.textFieldBlendFactorR, 1, 2);
		add(new Text("Blend Factor G"), 0, 3);
		add(this.textFieldBlendFactorG, 1, 3);
		add(new Text("Blend Factor B"), 0, 4);
		add(this.textFieldBlendFactorB, 1, 4);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BlendTexture} instance.
	 * 
	 * @return a {@code BlendTexture} instance
	 */
	@Override
	public Texture createTexture() {
		return new BlendTexture(doGetTextureA(), doGetTextureB(), doGetBlendFactorR(), doGetBlendFactorG(), doGetBlendFactorB());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Texture doGetTextureA() {
		return this.texturePickerA.getTexture();
	}
	
	private Texture doGetTextureB() {
		return this.texturePickerB.getTexture();
	}
	
	private float doGetBlendFactorB() {
		return Float.parseFloat(this.textFieldBlendFactorB.getText());
	}
	
	private float doGetBlendFactorG() {
		return Float.parseFloat(this.textFieldBlendFactorG.getText());
	}
	
	private float doGetBlendFactorR() {
		return Float.parseFloat(this.textFieldBlendFactorR.getText());
	}
}