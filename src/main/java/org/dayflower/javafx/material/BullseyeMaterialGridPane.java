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

import org.dayflower.geometry.Point3F;
import org.dayflower.javafx.scene.control.TextFields;
import org.dayflower.scene.Material;
import org.dayflower.scene.material.BullseyeMaterial;
import org.dayflower.scene.material.MatteMaterial;

import org.macroing.art4j.color.Color3F;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * A {@code BullseyeMaterialGridPane} is a {@link MaterialGridPane} for {@link BullseyeMaterial} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BullseyeMaterialGridPane extends MaterialGridPane {
	private final MaterialPicker materialPickerA;
	private final MaterialPicker materialPickerB;
	private final TextField textFieldOriginX;
	private final TextField textFieldOriginY;
	private final TextField textFieldOriginZ;
	private final TextField textFieldScale;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code BullseyeMaterialGridPane} instance.
	 */
	public BullseyeMaterialGridPane() {
		this.materialPickerA = new MaterialPicker(new MatteMaterial(Color3F.GRAY));
		this.materialPickerB = new MaterialPicker(new MatteMaterial(Color3F.WHITE));
		this.textFieldOriginX = TextFields.createTextField( 0.0F);
		this.textFieldOriginY = TextFields.createTextField(10.0F);
		this.textFieldOriginZ = TextFields.createTextField( 0.0F);
		this.textFieldScale = TextFields.createTextField(1.0F);
		
		add(new Text("Material A"), 0, 0);
		add(this.materialPickerA, 1, 0);
		add(new Text("Material B"), 0, 1);
		add(this.materialPickerB, 1, 1);
		add(new Text("Origin X"), 0, 2);
		add(this.textFieldOriginX, 1, 2);
		add(new Text("Origin Y"), 0, 3);
		add(this.textFieldOriginY, 1, 3);
		add(new Text("Origin Z"), 0, 4);
		add(this.textFieldOriginZ, 1, 4);
		add(new Text("Scale"), 0, 5);
		add(this.textFieldScale, 1, 5);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BullseyeMaterial} instance.
	 * 
	 * @return a {@code BullseyeMaterial} instance
	 */
	@Override
	public Material createMaterial() {
		return new BullseyeMaterial(doGetMaterialA(), doGetMaterialB(), doGetOrigin(), doGetScale());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Material doGetMaterialA() {
		return this.materialPickerA.getMaterial();
	}
	
	private Material doGetMaterialB() {
		return this.materialPickerB.getMaterial();
	}
	
	private Point3F doGetOrigin() {
		final float originX = Float.parseFloat(this.textFieldOriginX.getText());
		final float originY = Float.parseFloat(this.textFieldOriginY.getText());
		final float originZ = Float.parseFloat(this.textFieldOriginZ.getText());
		
		return new Point3F(originX, originY, originZ);
	}
	
	private float doGetScale() {
		return Float.parseFloat(this.textFieldScale.getText());
	}
}