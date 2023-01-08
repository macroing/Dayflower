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

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.shape.Cone3F;
import org.dayflower.geometry.shape.Cylinder3F;
import org.dayflower.geometry.shape.Disk3F;
import org.dayflower.geometry.shape.Hyperboloid3F;
import org.dayflower.geometry.shape.Paraboloid3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.geometry.shape.Rectangle3F;
import org.dayflower.geometry.shape.RectangularCuboid3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Torus3F;
import org.dayflower.geometry.shape.Triangle3F;

import org.macroing.java.lang.Doubles;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Window;

/**
 * A {@code ShapeWizard} is a wizard for {@link Shape3F} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ShapeWizard {
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
	
	private final Window owner;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ShapeWizard} instance.
	 * <p>
	 * If {@code owner} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param owner the owner
	 * @throws NullPointerException thrown if, and only if, {@code owner} is {@code null}
	 */
	public ShapeWizard(final Window owner) {
		this.owner = Objects.requireNonNull(owner, "owner == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns an optional {@link Shape3F} instance.
	 * 
	 * @return an optional {@code Shape3F} instance
	 */
	public Optional<Shape3F> showAndWait() {
		final Dialog<ShapeInfo> dialogShapeInfo = doCreateDialogShapeInfo();
		
		final Optional<ShapeInfo> optionalShapeInfo = dialogShapeInfo.showAndWait();
		
		if(optionalShapeInfo.isPresent()) {
			final ShapeInfo shapeInfo = optionalShapeInfo.get();
			
			final Dialog<Shape3F> dialogShape = doCreateDialogShape(shapeInfo);
			
			return dialogShape.showAndWait();
		}
		
		return Optional.empty();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Dialog<Shape3F> doCreateDialogShape(final ShapeInfo shapeInfo) {
		final ShapeGridPane shapeGridPane = shapeInfo.createShapeGridPane();
		
		final
		Dialog<Shape3F> dialog = new Dialog<>();
		dialog.initOwner(this.owner);
		dialog.setResultConverter(buttonType -> buttonType.getButtonData() == ButtonType.OK.getButtonData() ? shapeGridPane.createShape() : null);
		dialog.setTitle(shapeInfo.getTitle());
		
		final
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		dialogPane.setContent(shapeGridPane);
		
		return dialog;
	}
	
	private Dialog<ShapeInfo> doCreateDialogShapeInfo() {
		final AtomicReference<ShapeInfo> shapeInfo = new AtomicReference<>(new ShapeInfo(Plane3F.class));
		
		final
		ComboBox<ShapeInfo> comboBox = new ComboBox<>();
		comboBox.getItems().addAll(new ShapeInfo(Cone3F.class), new ShapeInfo(Cylinder3F.class), new ShapeInfo(Disk3F.class), new ShapeInfo(Hyperboloid3F.class), new ShapeInfo(Paraboloid3F.class), new ShapeInfo(Plane3F.class), new ShapeInfo(Rectangle3F.class), new ShapeInfo(RectangularCuboid3F.class), new ShapeInfo(Sphere3F.class), new ShapeInfo(Torus3F.class), new ShapeInfo(Triangle3F.class));
		comboBox.setMaxWidth(Doubles.MAX_VALUE);
		comboBox.setValue(new ShapeInfo(Plane3F.class));
		comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> shapeInfo.set(newValue));
		
		final
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10.0D);
		gridPane.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		gridPane.setVgap(10.0D);
		gridPane.add(new Text("Shape"), 0, 0);
		gridPane.add(comboBox, 1, 0);
		
		final
		Dialog<ShapeInfo> dialog = new Dialog<>();
		dialog.initOwner(this.owner);
		dialog.setResultConverter(buttonType -> buttonType.getButtonData() == ButtonType.NEXT.getButtonData() ? shapeInfo.get() : null);
		dialog.setTitle("New Shape");
		
		final
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.NEXT, ButtonType.CANCEL);
		dialogPane.setContent(gridPane);
		
		return dialog;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class ShapeInfo {
		private final Class<? extends Shape3F> clazz;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public ShapeInfo(final Class<? extends Shape3F> clazz) {
			this.clazz = Objects.requireNonNull(clazz, "clazz == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public String getTitle() {
			switch(this.clazz.getSimpleName()) {
				case NAME_CONE:
					return "New Cone";
				case NAME_CYLINDER:
					return "New Cylinder";
				case NAME_DISK:
					return "New Disk";
				case NAME_HYPERBOLOID:
					return "New Hyperboloid";
				case NAME_PARABOLOID:
					return "New Paraboloid";
				case NAME_PLANE:
					return "New Plane";
				case NAME_RECTANGLE:
					return "New Rectangle";
				case NAME_RECTANGULAR_CUBOID:
					return "New Rectangular Cuboid";
				case NAME_SPHERE:
					return "New Sphere";
				case NAME_TORUS:
					return "New Torus";
				case NAME_TRIANGLE:
					return "New Triangle";
				default:
					return "New Plane";
			}
		}
		
		@Override
		public String toString() {
			return getTitle();
		}
		
		public ShapeGridPane createShapeGridPane() {
			switch(this.clazz.getSimpleName()) {
				case NAME_CONE:
					return new ConeGridPane();
				case NAME_CYLINDER:
					return new CylinderGridPane();
				case NAME_DISK:
					return new DiskGridPane();
				case NAME_HYPERBOLOID:
					return new HyperboloidGridPane();
				case NAME_PARABOLOID:
					return new ParaboloidGridPane();
				case NAME_PLANE:
					return new PlaneGridPane();
				case NAME_RECTANGLE:
					return new RectangleGridPane();
				case NAME_RECTANGULAR_CUBOID:
					return new RectangularCuboidGridPane();
				case NAME_SPHERE:
					return new SphereGridPane();
				case NAME_TORUS:
					return new TorusGridPane();
				case NAME_TRIANGLE:
					return new TriangleGridPane();
				default:
					return new PlaneGridPane();
			}
		}
		
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof ShapeInfo)) {
				return false;
			} else if(!Objects.equals(this.clazz, ShapeInfo.class.cast(object).clazz)) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(this.clazz);
		}
	}
}