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

import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.shape.Rectangle3F;

import org.macroing.javafx.scene.control.TextFields;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * A {@code RectangleGridPane} is a {@link ShapeGridPane} for {@link Rectangle3F} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class RectangleGridPane extends ShapeGridPane {
	private final TextField textFieldAX;
	private final TextField textFieldAY;
	private final TextField textFieldAZ;
	private final TextField textFieldBX;
	private final TextField textFieldBY;
	private final TextField textFieldBZ;
	private final TextField textFieldCX;
	private final TextField textFieldCY;
	private final TextField textFieldCZ;
	private final TextField textFieldDX;
	private final TextField textFieldDY;
	private final TextField textFieldDZ;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code RectangleGridPane} instance.
	 */
	public RectangleGridPane() {
		this.textFieldAX = TextFields.createTextField(-2.0F);
		this.textFieldAY = TextFields.createTextField(+2.0F);
		this.textFieldAZ = TextFields.createTextField(+0.0F);
		this.textFieldBX = TextFields.createTextField(+2.0F);
		this.textFieldBY = TextFields.createTextField(+2.0F);
		this.textFieldBZ = TextFields.createTextField(+0.0F);
		this.textFieldCX = TextFields.createTextField(+2.0F);
		this.textFieldCY = TextFields.createTextField(-2.0F);
		this.textFieldCZ = TextFields.createTextField(+0.0F);
		this.textFieldDX = TextFields.createTextField(-2.0F);
		this.textFieldDY = TextFields.createTextField(-2.0F);
		this.textFieldDZ = TextFields.createTextField(+0.0F);
		
		add(new Text("A X"), 0, 0);
		add(this.textFieldAX, 1, 0);
		add(new Text("A Y"), 0, 1);
		add(this.textFieldAY, 1, 1);
		add(new Text("A Z"), 0, 2);
		add(this.textFieldAZ, 1, 2);
		add(new Text("B X"), 0, 3);
		add(this.textFieldBX, 1, 3);
		add(new Text("B Y"), 0, 4);
		add(this.textFieldBY, 1, 4);
		add(new Text("B Z"), 0, 5);
		add(this.textFieldBZ, 1, 5);
		add(new Text("C X"), 0, 6);
		add(this.textFieldCX, 1, 6);
		add(new Text("C Y"), 0, 7);
		add(this.textFieldCY, 1, 7);
		add(new Text("C Z"), 0, 8);
		add(this.textFieldCZ, 1, 8);
		add(new Text("D X"), 0, 9);
		add(this.textFieldDX, 1, 9);
		add(new Text("D Y"), 0, 10);
		add(this.textFieldDY, 1, 10);
		add(new Text("D Z"), 0, 11);
		add(this.textFieldDZ, 1, 11);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Rectangle3F} instance.
	 * 
	 * @return a {@code Rectangle3F} instance
	 */
	@Override
	public Shape3F createShape() {
		try {
			return new Rectangle3F(doGetA(), doGetB(), doGetC(), doGetD());
		} catch(@SuppressWarnings("unused") final IllegalArgumentException e) {
			System.err.println("Unable to create Rectangle3F. The parameters are invalid.");
			
			return null;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	
	private Point3F doGetC() {
		final float x = Float.parseFloat(this.textFieldCX.getText());
		final float y = Float.parseFloat(this.textFieldCY.getText());
		final float z = Float.parseFloat(this.textFieldCZ.getText());
		
		return new Point3F(x, y, z);
	}
	
	private Point3F doGetD() {
		final float x = Float.parseFloat(this.textFieldDX.getText());
		final float y = Float.parseFloat(this.textFieldDY.getText());
		final float z = Float.parseFloat(this.textFieldDZ.getText());
		
		return new Point3F(x, y, z);
	}
}