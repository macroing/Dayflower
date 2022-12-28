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

import org.dayflower.geometry.AngleF;
import org.dayflower.javafx.scene.control.TextFields;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.PolkaDotTexture;
import org.dayflower.scene.texture.Texture;

import org.macroing.art4j.color.Color3F;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * A {@code PolkaDotTextureGridPane} is a {@link TextureGridPane} for {@link PolkaDotTexture} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PolkaDotTextureGridPane extends TextureGridPane {
	private final TextField textFieldAngle;
	private final TextField textFieldCellResolution;
	private final TextField textFieldPolkaDotRadius;
	private final TexturePicker texturePickerA;
	private final TexturePicker texturePickerB;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PolkaDotTextureGridPane} instance.
	 */
	public PolkaDotTextureGridPane() {
		this.textFieldAngle = TextFields.createTextField(0.0F);
		this.textFieldCellResolution = TextFields.createTextField(10.0F);
		this.textFieldPolkaDotRadius = TextFields.createTextField(0.25F);
		this.texturePickerA = new TexturePicker(new ConstantTexture(Color3F.GRAY));
		this.texturePickerB = new TexturePicker(new ConstantTexture(Color3F.WHITE));
		
		add(new Text("Texture A"), 0, 0);
		add(this.texturePickerA, 1, 0);
		add(new Text("Texture B"), 0, 1);
		add(this.texturePickerB, 1, 1);
		add(new Text("Angle"), 0, 2);
		add(this.textFieldAngle, 1, 2);
		add(new Text("Cell Resolution"), 0, 3);
		add(this.textFieldCellResolution, 1, 3);
		add(new Text("Polka Dot Radius"), 0, 4);
		add(this.textFieldPolkaDotRadius, 1, 4);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link PolkaDotTexture} instance.
	 * 
	 * @return a {@code PolkaDotTexture} instance
	 */
	@Override
	public Texture createTexture() {
		return new PolkaDotTexture(doGetTextureA(), doGetTextureB(), doGetAngle(), doGetCellResolution(), doGetPolkaDotRadius());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private AngleF doGetAngle() {
		return AngleF.degrees(Float.parseFloat(this.textFieldAngle.getText()));
	}
	
	private Texture doGetTextureA() {
		return this.texturePickerA.getTexture();
	}
	
	private Texture doGetTextureB() {
		return this.texturePickerB.getTexture();
	}
	
	private float doGetCellResolution() {
		return Float.parseFloat(this.textFieldCellResolution.getText());
	}
	
	private float doGetPolkaDotRadius() {
		return Float.parseFloat(this.textFieldPolkaDotRadius.getText());
	}
}