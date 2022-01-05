/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;

/**
 * A {@code PathMenuBar} is a {@code MenuBar} that allows you to add {@code CheckMenuItem}, {@code MenuItem} and {@code SeparatorMenuItem} instances in a straight forward way.
 * <p>
 * To add a {@code CheckMenuItem}, a {@code MenuItem} or a {@code SeparatorMenuItem}, you need to specify a "path". A path is used to construct {@code Menu} instances in a hierarchical fashion. Each path is separated into "path elements". Each path
 * element corresponds to a single {@code Menu}. The default path delimiter that is used to separate a path into path elements is {@code "."} (a dot). But this may be changed.
 * <p>
 * To use this class, consider the following example:
 * <pre>
 * {@code
 * //The following example uses internationalization and translates the texts of Menus and MenuItems to Swedish.
 * PathMenuBar pathMenuBar = new PathMenuBar("\\.");
 * pathMenuBar.setPathElementText("File", "Arkiv");
 * pathMenuBar.setPathElementText("New", "Nytt");
 * pathMenuBar.addMenuItem("File.New", "Dokument", null, null, true);
 * pathMenuBar.addSeparatorMenuItem("File");
 * pathMenuBar.addMenuItem("File", "Avsluta", null, null, true);
 * }
 * </pre>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PathMenuBar extends MenuBar {
	/**
	 * The default path delimiter, which is {@code "\\."}.
	 * <p>
	 * A path delimiter is specified in Regex. But when using it, you only have to specify {@code "."} (a dot).
	 */
	public static final String DEFAULT_PATH_DELIMITER = "\\.";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Map<String, String> pathElementTexts;
	private final String pathDelimiter;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new empty {@code PathMenuBar} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PathMenuBar(PathMenuBar.DEFAULT_PATH_DELIMITER);
	 * }
	 * </pre>
	 */
	public PathMenuBar() {
		this(DEFAULT_PATH_DELIMITER);
	}
	
	/**
	 * Constructs a new empty {@code PathMenuBar} instance.
	 * <p>
	 * If {@code pathDelimiter} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pathDelimiter the path delimiter to use, in Regex
	 * @throws NullPointerException thrown if, and only if, {@code pathDelimiter} is {@code null}
	 */
	public PathMenuBar(final String pathDelimiter) {
		this.pathElementTexts = new HashMap<>();
		this.pathDelimiter = Objects.requireNonNull(pathDelimiter, "pathDelimiter == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds a new {@code CheckMenuItem} to this {@code PathMenuBar} instance.
	 * <p>
	 * Returns the new {@code CheckMenuItem} instance.
	 * <p>
	 * If either {@code path} or {@code text} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param path the path for the new {@code CheckMenuItem}
	 * @param text the text for the new {@code CheckMenuItem}
	 * @param eventHandler the {@code EventHandler} for the new {@code CheckMenuItem}, which may be {@code null}
	 * @param isEnabled {@code true} if, and only if, the new {@code CheckMenuItem} should be enabled, {@code false} otherwise
	 * @param isSelected {@code true} if, and only if, the new {@code CheckMenuItem} should be selected, {@code false} otherwise
	 * @return the new {@code CheckMenuItem} instance
	 * @throws NullPointerException thrown if, and only if, either {@code path} or {@code text} are {@code null}
	 */
	public CheckMenuItem addCheckMenuItem(final String path, final String text, final EventHandler<ActionEvent> eventHandler, final boolean isEnabled, final boolean isSelected) {
		final
		CheckMenuItem checkMenuItem = new CheckMenuItem();
		checkMenuItem.setDisable(!isEnabled);
		checkMenuItem.setSelected(isSelected);
		checkMenuItem.setText(Objects.requireNonNull(text, "text == null"));
		
		if(eventHandler != null) {
			checkMenuItem.setOnAction(eventHandler);
		}
		
		final
		Menu menu = getMenu(path);
		menu.getItems().add(checkMenuItem);
		
		return checkMenuItem;
	}
	
	/**
	 * Returns a {@code Menu} given its path {@code path}.
	 * <p>
	 * If {@code path} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param path the path for the {@code Menu}
	 * @return a {@code Menu} given its path {@code path}
	 * @throws NullPointerException thrown if, and only if, {@code path} is {@code null}
	 */
	public Menu getMenu(final String path) {
		Objects.requireNonNull(path, "path == null");
		
		final String[] pathElements = path.split(this.pathDelimiter);
		
		final String pathElement = this.pathElementTexts.getOrDefault(pathElements[0], pathElements[0]);
		
		for(final Menu menu : getMenus()) {
			if(menu.getText().equals(pathElement)) {
				return pathElements.length == 1 ? menu : doGetMenu(path, pathElements, 1, menu);
			}
		}
		
		final Menu menu = new Menu(pathElement);
		
		getMenus().add(menu);
		
		return pathElements.length == 1 ? menu : doGetMenu(path, pathElements, 1, menu);
	}
	
	/**
	 * Returns a {@code MenuItem} given its path {@code path} and text {@code text}.
	 * <p>
	 * If either {@code path} or {@code text} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If no matching {@code MenuItem} exists, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param path the path for the {@code Menu}
	 * @param text the text for the {@code MenuItem}
	 * @return a {@code MenuItem} given its path {@code path} and text {@code text}
	 * @throws IllegalArgumentException thrown if, and only if, no matching {@code MenuItem} exists
	 * @throws NullPointerException thrown if, and only if, either {@code path} or {@code text} are {@code null}
	 */
	public MenuItem getMenuItem(final String path, final String text) {
		Objects.requireNonNull(path, "path == null");
		Objects.requireNonNull(text, "text == null");
		
		final Menu menu = getMenu(path);
		
		for(final MenuItem menuItem : menu.getItems()) {
			if(menuItem.getText() != null && menuItem.getText().equals(text)) {
				return menuItem;
			}
		}
		
		throw new IllegalArgumentException(String.format("Invalid MenuItem: path=\"%s\", text=\"%s\"", path, text));
	}
	
	/**
	 * Adds a new {@code MenuItem} to this {@code PathMenuBar} instance.
	 * <p>
	 * Returns the new {@code MenuItem} instance.
	 * <p>
	 * If either {@code path} or {@code text} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param path the path for the new {@code MenuItem}
	 * @param text the text for the new {@code MenuItem}
	 * @param eventHandler the {@code EventHandler} for the new {@code MenuItem}, which may be {@code null}
	 * @param keyCombination the {@code KeyCombination} for the new {@code MenuItem}, which may be {@code null}
	 * @param isEnabled {@code true} if, and only if, the new {@code MenuItem} should be enabled, {@code false} otherwise
	 * @return the new {@code MenuItem} instance
	 * @throws NullPointerException thrown if, and only if, either {@code path} or {@code text} are {@code null}
	 */
	public MenuItem addMenuItem(final String path, final String text, final EventHandler<ActionEvent> eventHandler, final KeyCombination keyCombination, final boolean isEnabled) {
		final
		MenuItem menuItem = new MenuItem();
		menuItem.setDisable(!isEnabled);
		menuItem.setText(Objects.requireNonNull(text, "text == null"));
		
		if(eventHandler != null) {
			menuItem.setOnAction(eventHandler);
		}
		
		if(keyCombination != null) {
			menuItem.setAccelerator(keyCombination);
		}
		
		final
		Menu menu = getMenu(path);
		menu.getItems().add(menuItem);
		
		return menuItem;
	}
	
	/**
	 * Adds a new {@code SeparatorMenuItem} to this {@code PathMenuBar} instance.
	 * <p>
	 * Returns the new {@code SeparatorMenuItem} instance.
	 * <p>
	 * If {@code path} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param path the path for the new {@code SeparatorMenuItem}
	 * @return the new {@code SeparatorMenuItem} instance
	 * @throws NullPointerException thrown if, and only if, {@code path} is {@code null}
	 */
	public SeparatorMenuItem addSeparatorMenuItem(final String path) {
		final SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
		
		final
		Menu menu = getMenu(path);
		menu.getItems().add(separatorMenuItem);
		
		return separatorMenuItem;
	}
	
	/**
	 * Sets the text to be displayed by the {@code Menu} that is referred to by the path element {@code pathElement}.
	 * <p>
	 * If either {@code pathElement} or {@code text} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pathElement the path element of the {@code Menu} to set the text for
	 * @param text the text for the {@code Menu} that is referred to by the path element {@code pathElement}
	 * @throws NullPointerException thrown if, and only if, either {@code pathElement} or {@code text} are {@code null}
	 */
	public void setPathElementText(final String pathElement, final String text) {
		this.pathElementTexts.put(Objects.requireNonNull(pathElement, "pathElement == null"), Objects.requireNonNull(text, "text == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Menu doGetMenu(final String path, final String[] pathElements, final int index, final Menu menu) {
		final String pathElement = this.pathElementTexts.getOrDefault(pathElements[index], pathElements[index]);
		
		for(final MenuItem menuItem : menu.getItems()) {
			if(menuItem instanceof Menu && menuItem.getText().equals(pathElement)) {
				return index + 1 == pathElements.length ? Menu.class.cast(menuItem) : doGetMenu(path, pathElements, index + 1, Menu.class.cast(menuItem));
			}
		}
		
		final Menu menuForPathElement = new Menu(pathElement);
		
		menu.getItems().add(menuForPathElement);
		
		if(index + 1 == pathElements.length) {
			return menuForPathElement;
		}
		
		return doGetMenu(path, pathElements, index + 1, menuForPathElement);
	}
}