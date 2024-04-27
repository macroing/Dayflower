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
package org.dayflower.javafx.scene.control;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on {@code TextField} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class TextFields {
	private TextFields() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code TextField} instance that is restricted to {@code float}-based values.
	 * 
	 * @param value the initial {@code float} value
	 * @return a new {@code TextField} instance that is restricted to {@code float}-based values
	 */
	public static TextField createTextField(final float value) {
		final
		TextField textField = new TextField();
		textField.setTextFormatter(new TextFormatter<>(TextFields::doHandleChangeFloat));
		textField.setText(Float.toString(value));
		
		return textField;
	}
	
	/**
	 * Returns a new {@code TextField} instance that is restricted to {@code int}-based values.
	 * 
	 * @param value the initial {@code int} value
	 * @return a new {@code TextField} instance that is restricted to {@code int}-based values
	 */
	public static TextField createTextField(final int value) {
		final
		TextField textField = new TextField();
		textField.setTextFormatter(new TextFormatter<>(TextFields::doHandleChangeInt));
		textField.setText(Integer.toString(value));
		
		return textField;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("unused")
	private static Change doHandleChangeFloat(final Change change) {
		try {
			Float.parseFloat(change.getControlNewText());
			
			return change;
		} catch(final NumberFormatException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unused")
	private static Change doHandleChangeInt(final Change change) {
		try {
			Integer.parseInt(change.getControlNewText());
			
			return change;
		} catch(final NumberFormatException e) {
			return null;
		}
	}
}