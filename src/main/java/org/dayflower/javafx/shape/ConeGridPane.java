/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.shape.Cone3F;
import org.dayflower.javafx.scene.control.TextFields;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * A {@code ConeGridPane} is a {@link ShapeGridPane} for {@link Cone3F} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ConeGridPane extends ShapeGridPane {
	private final TextField textFieldPhiMax;
	private final TextField textFieldRadius;
	private final TextField textFieldZMax;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ConeGridPane} instance.
	 */
	public ConeGridPane() {
		this.textFieldPhiMax = TextFields.createTextField(360.0F);
		this.textFieldRadius = TextFields.createTextField(1.0F);
		this.textFieldZMax = TextFields.createTextField(1.0F);
		
		add(new Text("Phi Max"), 0, 0);
		add(this.textFieldPhiMax, 1, 0);
		add(new Text("Radius"), 0, 1);
		add(this.textFieldRadius, 1, 1);
		add(new Text("Z Max"), 0, 2);
		add(this.textFieldZMax, 1, 2);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Cone3F} instance.
	 * 
	 * @return a {@code Cone3F} instance
	 */
	@Override
	public Shape3F createShape() {
		return new Cone3F(doGetPhiMax(), doGetRadius(), doGetZMax());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private AngleF doGetPhiMax() {
		return AngleF.degrees(Float.parseFloat(this.textFieldPhiMax.getText()));
	}
	
	private float doGetRadius() {
		return Float.parseFloat(this.textFieldRadius.getText());
	}
	
	private float doGetZMax() {
		return Float.parseFloat(this.textFieldZMax.getText());
	}
}