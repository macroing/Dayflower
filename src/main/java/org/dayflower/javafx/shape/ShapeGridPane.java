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

import org.dayflower.geometry.Shape3F;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

/**
 * A {@code ShapeGridPane} is a {@code GridPane} for {@link Shape3F} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class ShapeGridPane extends GridPane {
	/**
	 * Constructs a new {@code ShapeGridPane} instance.
	 */
	protected ShapeGridPane() {
		setAlignment(Pos.CENTER);
		setHgap(10.0D);
		setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		setVgap(10.0D);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Shape3F} instance.
	 * 
	 * @return a {@code Shape3F} instance
	 */
	public abstract Shape3F createShape();
}