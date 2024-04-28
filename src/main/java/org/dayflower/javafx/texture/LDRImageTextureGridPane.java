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

import java.io.File;
import java.io.UncheckedIOException;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Vector2F;
import org.dayflower.image.ImageF;
import org.dayflower.image.IntImageF;
import org.dayflower.javafx.scene.control.TextFields;
import org.dayflower.scene.texture.LDRImageTexture;
import org.dayflower.scene.texture.Texture;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * A {@code LDRImageTextureGridPane} is a {@link TextureGridPane} for {@link LDRImageTexture} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class LDRImageTextureGridPane extends TextureGridPane {
	private final TextField textFieldFilename;
	private final TextField textFieldAngle;
	private final TextField textFieldScaleU;
	private final TextField textFieldScaleV;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code LDRImageTextureGridPane} instance.
	 */
	public LDRImageTextureGridPane() {
		this.textFieldFilename = new TextField();
		this.textFieldAngle = TextFields.createTextField(0.0F);
		this.textFieldScaleU = TextFields.createTextField(1.0F);
		this.textFieldScaleV = TextFields.createTextField(1.0F);
		
		add(new Text("Filename"), 0, 0);
		add(this.textFieldFilename, 1, 0);
		add(new Text("Angle"), 0, 1);
		add(this.textFieldAngle, 1, 1);
		add(new Text("Scale U"), 0, 2);
		add(this.textFieldScaleU, 1, 2);
		add(new Text("Scale V"), 0, 3);
		add(this.textFieldScaleV, 1, 3);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns an {@link LDRImageTexture} instance.
	 * 
	 * @return an {@code LDRImageTexture} instance
	 */
	@Override
	public Texture createTexture() {
		return new LDRImageTexture(doGetImage(), doGetAngle(), doGetScale());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private AngleF doGetAngle() {
		return AngleF.degrees(Float.parseFloat(this.textFieldAngle.getText()));
	}
	
	private File doGetFile() {
		return new File(doGetFilename());
	}
	
	@SuppressWarnings("unused")
	private ImageF doGetImage() {
		final File file = doGetFile();
		
		if(file.isFile()) {
			try {
				return IntImageF.load(file);
			} catch(final UncheckedIOException e) {
//				Do nothing.
			}
		}
		
		return new IntImageF(800, 800).clear(Color3F.GREEN);
	}
	
	private String doGetFilename() {
		return this.textFieldFilename.getText();
	}
	
	private Vector2F doGetScale() {
		final float scaleU = Float.parseFloat(this.textFieldScaleU.getText());
		final float scaleV = Float.parseFloat(this.textFieldScaleV.getText());
		
		return new Vector2F(scaleU, scaleV);
	}
}