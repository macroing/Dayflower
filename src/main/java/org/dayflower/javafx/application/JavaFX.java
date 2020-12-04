/**
 * Copyright 2020 J&#246;rgen Lundgren
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
package org.dayflower.javafx.application;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

final class JavaFX {
	private JavaFX() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Button createButton(final String text) {
		final
		Button button = new Button();
		button.setText(text);
		
		return button;
	}
	
	public static ColumnConstraints createHorizontalColumnConstraints(final Priority priority) {
		final
		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setHgrow(priority);
		
		return columnConstraints;
	}
	
	public static ColumnConstraints createHorizontalColumnConstraints(final Priority priority, final double preferredWidth) {
		final
		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setHgrow(priority);
		columnConstraints.setPrefWidth(preferredWidth);
		
		return columnConstraints;
	}
	
	public static GridPane createGridPane(final double horizontalGal, final double verticalGap) {
		final
		GridPane gridPane = new GridPane();
		gridPane.setHgap(horizontalGal);
		gridPane.setVgap(verticalGap);
		
		return gridPane;
	}
	
	public static HBox createHBox(final double spacing, final double top, final double right, final double bottom, final double left) {
		final
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(top, right, bottom, left));
		hBox.setSpacing(spacing);
		
		return hBox;
	}
	
	public static Label createLabel(final String text) {
		final
		Label label = new Label();
		label.setText(text);
		
		return label;
	}
	
	public static Label createLabel(final String text, final double fontSize) {
		final
		Label label = new Label();
		label.setFont(new Font(fontSize));
		label.setText(text);
		
		return label;
	}
	
	public static Region createRegion(final double top, final double right, final double bottom, final double left) {
		final
		Region region = new Region();
		region.setPadding(new Insets(top, right, bottom, left));
		
		return region;
	}
	
	public static Region createRegionHBoxGrowAlways() {
		final Region region = new Region();
		
		HBox.setHgrow(region, Priority.ALWAYS);
		
		return region;
	}
	
	public static Separator createSeparator() {
		return new Separator();
	}
	
	public static TextField createTextField(final String text) {
		final
		TextField textField = new TextField();
		textField.setText(text);
		
		return textField;
	}
	
	public static TextField createTextField(final String text, final ChangeListener<String> changeListener) {
		final
		TextField textField = new TextField();
		textField.setText(text);
		textField.textProperty().addListener(changeListener);
		
		return textField;
	}
	
	public static VBox createVBox(final double top, final double right, final double bottom, final double left) {
		final
		VBox vBox = new VBox();
		vBox.setPadding(new Insets(top, right, bottom, left));
		
		return vBox;
	}
}