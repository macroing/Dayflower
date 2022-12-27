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

import org.dayflower.javafx.scene.control.TextFields;
import org.dayflower.scene.texture.SimplexFractionalBrownianMotionTexture;
import org.dayflower.scene.texture.Texture;

import org.macroing.art4j.color.Color3F;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * A {@code SimplexFractionalBrownianMotionTextureGridPane} is a {@link TextureGridPane} for {@link SimplexFractionalBrownianMotionTexture} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SimplexFractionalBrownianMotionTextureGridPane extends TextureGridPane {
	private final ColorPicker colorPickerColor;
	private final TextField textFieldFrequency;
	private final TextField textFieldGain;
	private final TextField textFieldOctaves;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SimplexFractionalBrownianMotionTextureGridPane} instance.
	 */
	public SimplexFractionalBrownianMotionTextureGridPane() {
		this.colorPickerColor = new ColorPicker(Color.rgb(191, 128, 191));
		this.textFieldFrequency = TextFields.createTextField(5.0F);
		this.textFieldGain = TextFields.createTextField(0.5F);
		this.textFieldOctaves = TextFields.createTextField(16);
		
		add(new Text("Color"), 0, 0);
		add(this.colorPickerColor, 1, 0);
		add(new Text("Frequency"), 0, 1);
		add(this.textFieldFrequency, 1, 1);
		add(new Text("Gain"), 0, 2);
		add(this.textFieldGain, 1, 2);
		add(new Text("Octaves"), 0, 3);
		add(this.textFieldOctaves, 1, 3);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link SimplexFractionalBrownianMotionTexture} instance.
	 * 
	 * @return a {@code SimplexFractionalBrownianMotionTexture} instance
	 */
	@Override
	public Texture createTexture() {
		return new SimplexFractionalBrownianMotionTexture(doGetColor(), doGetFrequency(), doGetGain(), doGetOctaves());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Color3F doGetColor() {
		final Color color = this.colorPickerColor.getValue();
		
		final float colorR = (float)(color.getRed());
		final float colorG = (float)(color.getGreen());
		final float colorB = (float)(color.getBlue());
		
		return new Color3F(colorR, colorG, colorB);
	}
	
	private float doGetFrequency() {
		return Float.parseFloat(this.textFieldFrequency.getText());
	}
	
	private float doGetGain() {
		return Float.parseFloat(this.textFieldGain.getText());
	}
	
	private int doGetOctaves() {
		return Integer.parseInt(this.textFieldOctaves.getText());
	}
}