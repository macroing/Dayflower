/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on {@code ContextMenu} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ContextMenus {
	private ContextMenus() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code ContextMenu} instance with the {@code MenuItem} instances of {@code menuItems}.
	 * <p>
	 * If either {@code menuItems} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param menuItems the {@code MenuItem} instances to add
	 * @return a new {@code ContextMenu} instance with the {@code MenuItem} instances of {@code menuItems}
	 * @throws NullPointerException thrown if, and only if, either {@code menuItems} or at least one of its elements are {@code null}
	 */
	public static ContextMenu createContextMenu(final MenuItem... menuItems) {
		Objects.requireNonNull(menuItems, "menuItems == null");
		
		final ContextMenu contextMenu = new ContextMenu();
		
		for(int i = 0; i < menuItems.length; i++) {
			contextMenu.getItems().add(Objects.requireNonNull(menuItems[i], String.format("menuItems[%d] == null", Integer.valueOf(i))));
		}
		
		return contextMenu;
	}
}