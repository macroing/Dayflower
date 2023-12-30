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

import org.dayflower.geometry.AngleF;
import org.dayflower.scene.Material;
import org.dayflower.scene.material.MatteMaterial;
import org.dayflower.scene.material.PolkaDotMaterial;

import org.macroing.art4j.color.Color3F;
import org.macroing.javafx.scene.control.TextFields;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * A {@code PolkaDotMaterialGridPane} is a {@link MaterialGridPane} for {@link PolkaDotMaterial} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PolkaDotMaterialGridPane extends MaterialGridPane {
	private final MaterialPicker materialPickerA;
	private final MaterialPicker materialPickerB;
	private final TextField textFieldAngle;
	private final TextField textFieldCellResolution;
	private final TextField textFieldPolkaDotRadius;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PolkaDotMaterialGridPane} instance.
	 */
	public PolkaDotMaterialGridPane() {
		this.materialPickerA = new MaterialPicker(new MatteMaterial(Color3F.GRAY));
		this.materialPickerB = new MaterialPicker(new MatteMaterial(Color3F.WHITE));
		this.textFieldAngle = TextFields.createTextField(0.0F);
		this.textFieldCellResolution = TextFields.createTextField(10.0F);
		this.textFieldPolkaDotRadius = TextFields.createTextField(0.25F);
		
		add(new Text("Material A"), 0, 0);
		add(this.materialPickerA, 1, 0);
		add(new Text("Material B"), 0, 1);
		add(this.materialPickerB, 1, 1);
		add(new Text("Angle"), 0, 2);
		add(this.textFieldAngle, 1, 2);
		add(new Text("Cell Resolution"), 0, 3);
		add(this.textFieldCellResolution, 1, 3);
		add(new Text("Polka Dot Radius"), 0, 4);
		add(this.textFieldPolkaDotRadius, 1, 4);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link PolkaDotMaterial} instance.
	 * 
	 * @return a {@code PolkaDotMaterial} instance
	 */
	@Override
	public Material createMaterial() {
		return new PolkaDotMaterial(doGetMaterialA(), doGetMaterialB(), doGetAngle(), doGetCellResolution(), doGetPolkaDotRadius());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private AngleF doGetAngle() {
		return AngleF.degrees(Float.parseFloat(this.textFieldAngle.getText()));
	}
	
	private Material doGetMaterialA() {
		return this.materialPickerA.getMaterial();
	}
	
	private Material doGetMaterialB() {
		return this.materialPickerB.getMaterial();
	}
	
	private float doGetCellResolution() {
		return Float.parseFloat(this.textFieldCellResolution.getText());
	}
	
	private float doGetPolkaDotRadius() {
		return Float.parseFloat(this.textFieldPolkaDotRadius.getText());
	}
}