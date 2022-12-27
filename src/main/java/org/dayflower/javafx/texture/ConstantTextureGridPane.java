/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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

import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

import org.macroing.art4j.color.Color3F;

import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * A {@code ConstantTextureGridPane} is a {@link TextureGridPane} for {@link ConstantTexture} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ConstantTextureGridPane extends TextureGridPane {
	private final ColorPicker colorPickerColor;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ConstantTextureGridPane} instance.
	 */
	public ConstantTextureGridPane() {
		this.colorPickerColor = new ColorPicker(Color.rgb(128, 128, 128));
		
		add(new Text("Color"), 0, 0);
		add(this.colorPickerColor, 1, 0);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link ConstantTexture} instance.
	 * 
	 * @return a {@code ConstantTexture} instance
	 */
	@Override
	public Texture createTexture() {
		return new ConstantTexture(doGetColor());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Color3F doGetColor() {
		final Color color = this.colorPickerColor.getValue();
		
		final float r = (float)(color.getRed());
		final float g = (float)(color.getGreen());
		final float b = (float)(color.getBlue());
		
		return new Color3F(r, g, b);
	}
}