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
package org.dayflower.javafx.scene.layout;

import java.util.Collection;

import org.dayflower.javafx.scene.control.Labels;
import org.macroing.java.lang.Doubles;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

/**
 * A {@code CenteredVBox} is a {@code VBox} that centers the content horizontally, fills its width and provides padding and spacing.
 * <p>
 * In addition to the above, a few useful methods are provided to add different {@code Node} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CenteredVBox extends VBox {
	/**
	 * Constructs a new {@code CenteredVBox} instance.
	 */
	public CenteredVBox() {
		setAlignment(Pos.TOP_CENTER);
		setFillWidth(true);
		setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		setSpacing(10.0D);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds a {@code Button} instance with a text of {@code text} and an action of {@code eventHandler}.
	 * <p>
	 * Returns the {@code Button} instance.
	 * 
	 * @param text a {@code String} with the text to use
	 * @param eventHandler an {@code EventHandler} with the action to handle
	 * @param disable {@code true} if, and only if, the {@code Button} should be disabled, {@code false} otherwise
	 * @return the {@code Button} instance
	 */
	public Button addButton(final String text, final EventHandler<ActionEvent> eventHandler, final boolean disable) {
		final
		Button button = new Button();
		button.setDisable(disable);
		button.setMaxWidth(Doubles.MAX_VALUE);
		button.setOnAction(eventHandler);
		button.setText(text);
		
		getChildren().add(button);
		
		return button;
	}
	
	/**
	 * Adds a {@code ComboBox} instance with the items in {@code items} and a value of {@code value}.
	 * <p>
	 * Returns the {@code ComboBox} instance.
	 * 
	 * @param <T> the generic type of the items
	 * @param items a {@code Collection} of items
	 * @param value the value to use
	 * @return the {@code ComboBox} instance
	 */
	public <T> ComboBox<T> addComboBox(final Collection<? extends T> items, final T value) {
		final
		ComboBox<T> comboBox = new ComboBox<>();
		comboBox.getItems().addAll(items);
		comboBox.setMaxWidth(Doubles.MAX_VALUE);
		comboBox.setValue(value);
		
		getChildren().add(comboBox);
		
		return comboBox;
	}
	
	/**
	 * Adds a {@code Label} instance with a text of {@code text} and a font size of {@code fontSize}.
	 * <p>
	 * Returns the {@code Label} instance.
	 * 
	 * @param text a {@code String} with the text to use
	 * @param fontSize a {@code double} with the font size to use
	 * @return the {@code Label} instance
	 */
	public Label addLabel(final String text, final double fontSize) {
		final Label label = Labels.createLabel(text, fontSize);
		
		getChildren().add(label);
		
		return label;
	}
	
	/**
	 * Adds a {@code Separator} instance.
	 * <p>
	 * Returns the {@code Separator} instance.
	 * 
	 * @return the {@code Separator} instance
	 */
	public Separator addSeparator() {
		final Separator separator = new Separator();
		
		getChildren().add(separator);
		
		return separator;
	}
	
	/**
	 * Adds a {@code Slider} instance.
	 * <p>
	 * Returns the {@code Slider} instance.
	 * <p>
	 * If {@code changeListener} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param min the value of the property {@code min}
	 * @param max the value of the property {@code max}
	 * @param value the value of the property {@code value}
	 * @param blockIncrement the value of the property {@code blockIncrement}
	 * @param majorTickUnit the value of the property {@code majorTickUnit}
	 * @param showTickLabels the value of the property {@code showTickLabels}
	 * @param showTickMarks the value of the property {@code showTickMarks}
	 * @param snapToTicks the value of the property {@code snapToTicks}
	 * @param changeListener a {@code ChangeListener}
	 * @return the {@code Slider} instance
	 * @throws NullPointerException thrown if, and only if, {@code changeListener} is {@code null}
	 */
	public Slider addSlider(final double min, final double max, final double value, final double blockIncrement, final double majorTickUnit, final boolean showTickLabels, final boolean showTickMarks, final boolean snapToTicks, final ChangeListener<? super Number> changeListener) {
		final
		Slider slider = new Slider();
		slider.setBlockIncrement(blockIncrement);
		slider.setMajorTickUnit(majorTickUnit);
		slider.setMax(max);
		slider.setMin(min);
		slider.setShowTickLabels(showTickLabels);
		slider.setShowTickMarks(showTickMarks);
		slider.setSnapToTicks(snapToTicks);
		slider.setValue(value);
		slider.valueProperty().addListener(changeListener);
		
		getChildren().add(slider);
		
		return slider;
	}
}