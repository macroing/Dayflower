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
package org.dayflower.javafx.shape;

import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.shape.Torus3F;

import org.macroing.javafx.scene.control.TextFields;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * A {@code TorusGridPane} is a {@link ShapeGridPane} for {@link Torus3F} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class TorusGridPane extends ShapeGridPane {
	private final TextField textFieldRadiusInner;
	private final TextField textFieldRadiusOuter;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code TorusGridPane} instance.
	 */
	public TorusGridPane() {
		this.textFieldRadiusInner = TextFields.createTextField(0.25F);
		this.textFieldRadiusOuter = TextFields.createTextField(1.0F);
		
		add(new Text("Radius Inner"), 0, 0);
		add(this.textFieldRadiusInner, 1, 0);
		add(new Text("Radius Outer"), 0, 1);
		add(this.textFieldRadiusOuter, 1, 1);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Torus3F} instance.
	 * 
	 * @return a {@code Torus3F} instance
	 */
	@Override
	public Shape3F createShape() {
		return new Torus3F(doGetRadiusInner(), doGetRadiusOuter());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private float doGetRadiusInner() {
		return Float.parseFloat(this.textFieldRadiusInner.getText());
	}
	
	private float doGetRadiusOuter() {
		return Float.parseFloat(this.textFieldRadiusOuter.getText());
	}
}