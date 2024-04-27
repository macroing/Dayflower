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

import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.shape.RectangularCuboid3F;
import org.dayflower.javafx.scene.control.TextFields;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * A {@code RectangularCuboidGridPane} is a {@link ShapeGridPane} for {@link RectangularCuboid3F} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class RectangularCuboidGridPane extends ShapeGridPane {
	private final TextField textFieldAX;
	private final TextField textFieldAY;
	private final TextField textFieldAZ;
	private final TextField textFieldBX;
	private final TextField textFieldBY;
	private final TextField textFieldBZ;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code RectangularCuboidGridPane} instance.
	 */
	public RectangularCuboidGridPane() {
		this.textFieldAX = TextFields.createTextField(-0.5F);
		this.textFieldAY = TextFields.createTextField(-0.5F);
		this.textFieldAZ = TextFields.createTextField(-0.5F);
		this.textFieldBX = TextFields.createTextField(+0.5F);
		this.textFieldBY = TextFields.createTextField(+0.5F);
		this.textFieldBZ = TextFields.createTextField(+0.5F);
		
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
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link RectangularCuboid3F} instance.
	 * 
	 * @return a {@code RectangularCuboid3F} instance
	 */
	@Override
	public Shape3F createShape() {
		return new RectangularCuboid3F(doGetA(), doGetB());
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
}