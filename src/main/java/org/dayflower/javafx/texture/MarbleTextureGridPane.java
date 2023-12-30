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
package org.dayflower.javafx.texture;

import org.dayflower.scene.texture.MarbleTexture;
import org.dayflower.scene.texture.Texture;

import org.macroing.art4j.color.Color3F;
import org.macroing.javafx.scene.control.TextFields;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * A {@code MarbleTextureGridPane} is a {@link TextureGridPane} for {@link MarbleTexture} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MarbleTextureGridPane extends TextureGridPane {
	private final ColorPicker colorPickerColorA;
	private final ColorPicker colorPickerColorB;
	private final ColorPicker colorPickerColorC;
	private final TextField textFieldScale;
	private final TextField textFieldStripes;
	private final TextField textFieldOctaves;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MarbleTextureGridPane} instance.
	 */
	public MarbleTextureGridPane() {
		this.colorPickerColorA = new ColorPicker(Color.rgb(204, 204, 204));
		this.colorPickerColorB = new ColorPicker(Color.rgb(102,  51,  26));
		this.colorPickerColorC = new ColorPicker(Color.rgb( 15,  10,   5));
		this.textFieldScale = TextFields.createTextField(5.0F);
		this.textFieldStripes = TextFields.createTextField(0.15F);
		this.textFieldOctaves = TextFields.createTextField(8);
		
		add(new Text("Color A"), 0, 0);
		add(this.colorPickerColorA, 1, 0);
		add(new Text("Color B"), 0, 1);
		add(this.colorPickerColorB, 1, 1);
		add(new Text("Color C"), 0, 2);
		add(this.colorPickerColorC, 1, 2);
		add(new Text("Scale"), 0, 3);
		add(this.textFieldScale, 1, 3);
		add(new Text("Stripes"), 0, 4);
		add(this.textFieldStripes, 1, 4);
		add(new Text("Octaves"), 0, 5);
		add(this.textFieldOctaves, 1, 5);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link MarbleTexture} instance.
	 * 
	 * @return a {@code MarbleTexture} instance
	 */
	@Override
	public Texture createTexture() {
		return new MarbleTexture(doGetColorA(), doGetColorB(), doGetColorC(), doGetScale(), doGetStripes(), doGetOctaves());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Color3F doGetColorA() {
		final Color colorA = this.colorPickerColorA.getValue();
		
		final float colorAR = (float)(colorA.getRed());
		final float colorAG = (float)(colorA.getGreen());
		final float colorAB = (float)(colorA.getBlue());
		
		return new Color3F(colorAR, colorAG, colorAB);
	}
	
	private Color3F doGetColorB() {
		final Color colorB = this.colorPickerColorB.getValue();
		
		final float colorBR = (float)(colorB.getRed());
		final float colorBG = (float)(colorB.getGreen());
		final float colorBB = (float)(colorB.getBlue());
		
		return new Color3F(colorBR, colorBG, colorBB);
	}
	
	private Color3F doGetColorC() {
		final Color colorC = this.colorPickerColorC.getValue();
		
		final float colorCR = (float)(colorC.getRed());
		final float colorCG = (float)(colorC.getGreen());
		final float colorCB = (float)(colorC.getBlue());
		
		return new Color3F(colorCR, colorCG, colorCB);
	}
	
	private float doGetScale() {
		return Float.parseFloat(this.textFieldScale.getText());
	}
	
	private float doGetStripes() {
		return Float.parseFloat(this.textFieldStripes.getText());
	}
	
	private int doGetOctaves() {
		return Integer.parseInt(this.textFieldOctaves.getText());
	}
}