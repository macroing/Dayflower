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

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.shape.Plane3F;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

/**
 * A {@code ShapePicker} is a control for picking a {@link Shape3F} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ShapePicker extends BorderPane {
	private static final String NAME_CONE = "Cone3F";
	private static final String NAME_CYLINDER = "Cylinder3F";
	private static final String NAME_DISK = "Disk3F";
	private static final String NAME_HYPERBOLOID = "Hyperboloid3F";
	private static final String NAME_PARABOLOID = "Paraboloid3F";
	private static final String NAME_PLANE = "Plane3F";
	private static final String NAME_RECTANGLE = "Rectangle3F";
	private static final String NAME_RECTANGULAR_CUBOID = "RectangularCuboid3F";
	private static final String NAME_SPHERE = "Sphere3F";
	private static final String NAME_TORUS = "Torus3F";
	private static final String NAME_TRIANGLE = "Triangle3F";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AtomicReference<Shape3F> pickedShape;
	private final Button button;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ShapePicker} instance with {@code new Plane3F()} as the picked {@link Shape3F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ShapePicker(new Plane3F());
	 * }
	 * </pre>
	 */
	public ShapePicker() {
		this(new Plane3F());
	}
	
	/**
	 * Constructs a new {@code ShapePicker} instance with {@code pickedShape} as the picked {@link Shape3F} instance.
	 * <p>
	 * If {@code pickedShape} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pickedShape the picked {@code Shape3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code pickedShape} is {@code null}
	 */
	public ShapePicker(final Shape3F pickedShape) {
		this.pickedShape = new AtomicReference<>(Objects.requireNonNull(pickedShape, "pickedShape == null"));
		this.button = new Button(doGetName());
		this.button.setMaxWidth(Double.MAX_VALUE);
		this.button.setOnAction(this::doOnAction);
		
		setCenter(this.button);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the picked {@link Shape3F} instance.
	 * 
	 * @return the picked {@code Shape3F} instance
	 */
	public Shape3F getPickedShape() {
		return this.pickedShape.get();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private String doGetName() {
		switch(getPickedShape().getClass().getSimpleName()) {
			case NAME_CONE:
				return "Cone";
			case NAME_CYLINDER:
				return "Cylinder";
			case NAME_DISK:
				return "Disk";
			case NAME_HYPERBOLOID:
				return "Hyperboloid";
			case NAME_PARABOLOID:
				return "Paraboloid";
			case NAME_PLANE:
				return "Plane";
			case NAME_RECTANGLE:
				return "Rectangle";
			case NAME_RECTANGULAR_CUBOID:
				return "Rectangular Cuboid";
			case NAME_SPHERE:
				return "Sphere";
			case NAME_TORUS:
				return "Torus";
			case NAME_TRIANGLE:
				return "Triangle";
			default:
				return "Shape";
		}
	}
	
	@SuppressWarnings("unused")
	private void doOnAction(final ActionEvent e) {
		final
		ShapeWizard shapeWizard = new ShapeWizard(getScene().getWindow());
		shapeWizard.showAndWait().ifPresent(pickedShape -> this.pickedShape.set(pickedShape));
		
		this.button.setText(doGetName());
	}
}