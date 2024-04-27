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
package org.dayflower.javafx.shape;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.shape.Hyperboloid3F;
import org.dayflower.javafx.scene.control.TextFields;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * A {@code HyperboloidGridPane} is a {@link ShapeGridPane} for {@link Hyperboloid3F} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class HyperboloidGridPane extends ShapeGridPane {
	private final TextField textFieldAX;
	private final TextField textFieldAY;
	private final TextField textFieldAZ;
	private final TextField textFieldBX;
	private final TextField textFieldBY;
	private final TextField textFieldBZ;
	private final TextField textFieldPhiMax;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code HyperboloidGridPane} instance.
	 */
	public HyperboloidGridPane() {
		this.textFieldAX = TextFields.createTextField(0.0001F);
		this.textFieldAY = TextFields.createTextField(0.0001F);
		this.textFieldAZ = TextFields.createTextField(0.0000F);
		this.textFieldBX = TextFields.createTextField(1.0000F);
		this.textFieldBY = TextFields.createTextField(1.0000F);
		this.textFieldBZ = TextFields.createTextField(1.0000F);
		this.textFieldPhiMax = TextFields.createTextField(360.0F);
		
		add(new Text("Phi Max"), 0, 0);
		add(this.textFieldPhiMax, 1, 0);
		add(new Text("A X"), 0, 1);
		add(this.textFieldAX, 1, 1);
		add(new Text("A Y"), 0, 2);
		add(this.textFieldAY, 1, 2);
		add(new Text("A Z"), 0, 3);
		add(this.textFieldAZ, 1, 3);
		add(new Text("B X"), 0, 4);
		add(this.textFieldBX, 1, 4);
		add(new Text("B Y"), 0, 5);
		add(this.textFieldBY, 1, 5);
		add(new Text("B Z"), 0, 6);
		add(this.textFieldBZ, 1, 6);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Hyperboloid3F} instance.
	 * 
	 * @return a {@code Hyperboloid3F} instance
	 */
	@Override
	public Shape3F createShape() {
		try {
			return new Hyperboloid3F(doGetPhiMax(), doGetA(), doGetB());
		} catch(@SuppressWarnings("unused") final IllegalArgumentException e) {
			System.err.println("Unable to create Hyperboloid3F. The parameters are invalid.");
			
			return null;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private AngleF doGetPhiMax() {
		return AngleF.degrees(Float.parseFloat(this.textFieldPhiMax.getText()));
	}
	
	private Point3F doGetA() {
		final float x = Float.parseFloat(this.textFieldAX.getText());
		final float y = Float.parseFloat(this.textFieldAY.getText());
		final float z = Float.parseFloat(this.textFieldAZ.getText());
		
		return new Point3F(x, y, z);
	}
	
	private Point3F doGetB() {
		final float x = Float.parseFloat(this.textFieldBX.getText());
		final float y = Float.parseFloat(this.textFieldBY.getText());
		final float z = Float.parseFloat(this.textFieldBZ.getText());
		
		return new Point3F(x, y, z);
	}
}