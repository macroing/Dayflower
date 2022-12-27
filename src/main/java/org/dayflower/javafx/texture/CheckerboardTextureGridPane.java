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

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Vector2F;
import org.dayflower.javafx.scene.control.TextFields;
import org.dayflower.scene.texture.CheckerboardTexture;
import org.dayflower.scene.texture.Texture;

import org.macroing.art4j.color.Color3F;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * A {@code CheckerboardTextureGridPane} is a {@link TextureGridPane} for {@link CheckerboardTexture} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CheckerboardTextureGridPane extends TextureGridPane {
	private final ColorPicker colorPickerColorA;
	private final ColorPicker colorPickerColorB;
	private final TextField textFieldAngle;
	private final TextField textFieldScaleU;
	private final TextField textFieldScaleV;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CheckerboardTextureGridPane} instance.
	 */
	public CheckerboardTextureGridPane() {
		this.colorPickerColorA = new ColorPicker(Color.rgb(128, 128, 128));
		this.colorPickerColorB = new ColorPicker(Color.rgb(255, 255, 255));
		this.textFieldAngle = TextFields.createTextField(0.0F);
		this.textFieldScaleU = TextFields.createTextField(1.0F);
		this.textFieldScaleV = TextFields.createTextField(1.0F);
		
		add(new Text("Color A"), 0, 0);
		add(this.colorPickerColorA, 1, 0);
		add(new Text("Color B"), 0, 1);
		add(this.colorPickerColorB, 1, 1);
		add(new Text("Angle"), 0, 2);
		add(this.textFieldAngle, 1, 2);
		add(new Text("Scale U"), 0, 3);
		add(this.textFieldScaleU, 1, 3);
		add(new Text("Scale V"), 0, 4);
		add(this.textFieldScaleV, 1, 4);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link CheckerboardTexture} instance.
	 * 
	 * @return a {@code CheckerboardTexture} instance
	 */
	@Override
	public Texture createTexture() {
		return new CheckerboardTexture(doGetColorA(), doGetColorB(), doGetAngle(), doGetScale());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private AngleF doGetAngle() {
		return AngleF.degrees(Float.parseFloat(this.textFieldAngle.getText()));
	}
	
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
	
	private Vector2F doGetScale() {
		final float scaleU = Float.parseFloat(this.textFieldScaleU.getText());
		final float scaleV = Float.parseFloat(this.textFieldScaleV.getText());
		
		return new Vector2F(scaleU, scaleV);
	}
}