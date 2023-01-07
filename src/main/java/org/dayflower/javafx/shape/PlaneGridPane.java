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
import org.dayflower.geometry.shape.Plane3F;

/**
 * A {@code PlaneGridPane} is a {@link ShapeGridPane} for {@link Plane3F} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PlaneGridPane extends ShapeGridPane {
	/**
	 * Constructs a new {@code PlaneGridPane} instance.
	 */
	public PlaneGridPane() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Plane3F} instance.
	 * 
	 * @return a {@code Plane3F} instance
	 */
	@Override
	public Shape3F createShape() {
		return new Plane3F();
	}
}