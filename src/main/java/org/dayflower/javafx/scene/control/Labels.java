/**
 * Copyright 2020 - 2021 J&#246;rgen Lundgren
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

import javafx.scene.control.Label;
import javafx.scene.text.Font;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on {@code Label} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Labels {
	private Labels() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code Label} instance with a text of {@code text}.
	 * 
	 * @param text a {@code String} with the text to use
	 * @return a new {@code Label} instance with a text of {@code text}
	 */
	public static Label createLabel(final String text) {
		final
		Label label = new Label();
		label.setText(text);
		
		return label;
	}
	
	/**
	 * Returns a new {@code Label} instance with a text of {@code text} and a font size of {@code fontSize}.
	 * 
	 * @param text a {@code String} with the text to use
	 * @param fontSize a {@code double} with the font size to use
	 * @return a new {@code Label} instance with a text of {@code text} and a font size of {@code fontSize}
	 */
	public static Label createLabel(final String text, final double fontSize) {
		final
		Label label = new Label();
		label.setFont(new Font(fontSize));
		label.setText(text);
		
		return label;
	}
}