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
package org.dayflower.javafx.scene.control;

import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on {@code MenuItem} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MenuItems {
	private MenuItems() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code MenuItem} instance with the text {@code text} and the {@code onAction} property set to {@code eventHandler}.
	 * <p>
	 * If either {@code text} or {@code eventHandler} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param text a {@code String} with the text
	 * @param eventHandler an {@code EventHandler} for the property {@code onAction}
	 * @return a new {@code MenuItem} instance with the text {@code text} and the {@code onAction} property set to {@code eventHandler}
	 * @throws NullPointerException thrown if, and only if, either {@code text} or {@code eventHandler} are {@code null}
	 */
	public static MenuItem createMenuItem(final String text, final EventHandler<ActionEvent> eventHandler) {
		final
		MenuItem menuItem = new MenuItem();
		menuItem.setOnAction(Objects.requireNonNull(eventHandler, "eventHandler == null"));
		menuItem.setText(Objects.requireNonNull(text, "text == null"));
		
		return menuItem;
	}
}