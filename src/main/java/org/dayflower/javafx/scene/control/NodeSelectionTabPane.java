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
package org.dayflower.javafx.scene.control;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * A {@code NodeSelectionTabPane} is a {@code TabPane} that provides a method to retrieve the selected {@code Node} instance and simplifies {@code Tab} creation and selection.
 * <p>
 * To use this class, consider the following example:
 * <pre>
 * {@code
 * NodeSelectionTabPane<Button, String> nodeSelectionTabPane = new NodeSelectionTabPane<>(Button.class, button -> button.getText(), text -> new Button(text));
 * nodeSelectionTabPane.add("Button");
 * }
 * </pre>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class NodeSelectionTabPane<T extends Node, U> extends TabPane {
	private final BiPredicate<U, U> comparator;
	private final Class<T> clazz;
	private final Function<T, U> mapperTToU;
	private final Function<U, T> mapperUToT;
	private final Function<U, String> mapperUToText;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code NodeSelectionTabPane} instance.
	 * <p>
	 * If either {@code clazz}, {@code mapperTToU} or {@code mapperUToT} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new NodeSelectionTabPane(clazz, mapperTToU, mapperUToT, (a, b) -> a.equals(b));
	 * }
	 * </pre>
	 * 
	 * @param clazz a {@code Class} that is assignment compatible to {@code Node.class}
	 * @param mapperTToU a {@code Function} that maps a {@code Node} instance, represented by {@code T}, to an {@code Object} instance, represented by {@code U}
	 * @param mapperUToT a {@code Function} that maps an {@code Object} instance, represented by {@code U}, to a {@code Node} instance, represented by {@code T}
	 * @throws NullPointerException thrown if, and only if, either {@code clazz}, {@code mapperTToU} or {@code mapperUToT} are {@code null}
	 */
	public NodeSelectionTabPane(final Class<T> clazz, final Function<T, U> mapperTToU, final Function<U, T> mapperUToT) {
		this(clazz, mapperTToU, mapperUToT, (a, b) -> a.equals(b));
	}
	
	/**
	 * Constructs a new {@code NodeSelectionTabPane} instance.
	 * <p>
	 * If either {@code clazz}, {@code mapperTToU}, {@code mapperUToT} or {@code comparator} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new NodeSelectionTabPane(clazz, mapperTToU, mapperUToT, comparator, object -> object.toString());
	 * }
	 * </pre>
	 * 
	 * @param clazz a {@code Class} that is assignment compatible to {@code Node.class}
	 * @param mapperTToU a {@code Function} that maps a {@code Node} instance, represented by {@code T}, to an {@code Object} instance, represented by {@code U}
	 * @param mapperUToT a {@code Function} that maps an {@code Object} instance, represented by {@code U}, to a {@code Node} instance, represented by {@code T}
	 * @param comparator a {@code BiPredicate} that acts as a comparator for two {@code Object} instances, represented by {@code U}
	 * @throws NullPointerException thrown if, and only if, either {@code clazz}, {@code mapperTToU}, {@code mapperUToT} or {@code comparator} are {@code null}
	 */
	public NodeSelectionTabPane(final Class<T> clazz, final Function<T, U> mapperTToU, final Function<U, T> mapperUToT, final BiPredicate<U, U> comparator) {
		this(clazz, mapperTToU, mapperUToT, comparator, object -> object.toString());
	}
	
	/**
	 * Constructs a new {@code NodeSelectionTabPane} instance.
	 * <p>
	 * If either {@code clazz}, {@code mapperTToU}, {@code mapperUToT}, {@code comparator} or {@code mapperUToText} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param clazz a {@code Class} that is assignment compatible to {@code Node.class}
	 * @param mapperTToU a {@code Function} that maps a {@code Node} instance, represented by {@code T}, to an {@code Object} instance, represented by {@code U}
	 * @param mapperUToT a {@code Function} that maps an {@code Object} instance, represented by {@code U}, to a {@code Node} instance, represented by {@code T}
	 * @param comparator a {@code BiPredicate} that acts as a comparator for two {@code Object} instances, represented by {@code U}
	 * @param mapperUToText a {@code Function} that maps an {@code Object} instance, represented by {@code U}, to a {@code String} representation
	 * @throws NullPointerException thrown if, and only if, either {@code clazz}, {@code mapperTToU}, {@code mapperUToT}, {@code comparator} or {@code mapperUToText} are {@code null}
	 */
	public NodeSelectionTabPane(final Class<T> clazz, final Function<T, U> mapperTToU, final Function<U, T> mapperUToT, final BiPredicate<U, U> comparator, final Function<U, String> mapperUToText) {
		this.comparator = Objects.requireNonNull(comparator, "comparator == null");
		this.clazz = Objects.requireNonNull(clazz, "clazz == null");
		this.mapperTToU = Objects.requireNonNull(mapperTToU, "mapperTToU == null");
		this.mapperUToT = Objects.requireNonNull(mapperUToT, "mapperUToT == null");
		this.mapperUToText = Objects.requireNonNull(mapperUToText, "mapperUToText == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns an {@code Optional} with the currently selected {@code Node} instance that has a type of {@code T}.
	 * 
	 * @return an {@code Optional} with the currently selected {@code Node} instance that has a type of {@code T}
	 */
	public Optional<T> getSelectedNode() {
		final Tab tab = getSelectionModel().getSelectedItem();
		
		if(tab != null) {
			final Node node = tab.getContent();
			
			final Class<?> clazz = node.getClass();
			
			if(clazz.isAssignableFrom(this.clazz)) {
				return Optional.of(this.clazz.cast(node));
			}
		}
		
		return Optional.empty();
	}
	
	/**
	 * Adds {@code object} of type {@code U} to this {@code NodeSelectionTabPane} instance.
	 * <p>
	 * Returns the {@code Tab} instance associated with {@code object}.
	 * <p>
	 * If {@code object} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method first attempts to find an existing {@code Tab} instance that corresponds to {@code object}. It does this using {@code mapperTToU} and {@code comparator}. If it finds it, it will be selected. Otherwise a new {@code Tab} instance will
	 * be created. The {@code Node} of type {@code T} will be created using {@code mapperUToT} and the text for the {@code Tab} instance will be created using {@code mapperUToText}. Once the {@code Tab} has been created, it will be added and selected.
	 * 
	 * @param object an {@code Object} instance of type {@code U}
	 * @return the {@code Tab} instance associated with {@code object}
	 * @throws NullPointerException thrown if, and only if, {@code object} is {@code null}
	 */
	public Tab add(final U object) {
		Objects.requireNonNull(object, "object == null");
		
		for(final Tab tab : getTabs()) {
			final Node node = tab.getContent();
			
			final Class<?> clazz = node.getClass();
			
			if(clazz.isAssignableFrom(this.clazz)) {
				final U object0 = object;
				final U object1 = this.mapperTToU.apply(this.clazz.cast(node));
				
				if(this.comparator.test(object0, object1)) {
					getSelectionModel().select(tab);
					
					return tab;
				}
			}
		}
		
		final T node = this.mapperUToT.apply(object);
		
		final
		Tab tab = new Tab();
		tab.setContent(node);
		tab.setText(this.mapperUToText.apply(object));
		
		getTabs().add(tab);
		
		getSelectionModel().select(tab);
		
		return tab;
	}
	
	/**
	 * Adds {@code object} of type {@code U} to this {@code NodeSelectionTabPane} instance.
	 * <p>
	 * If {@code object} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method will call {@link #add(Object)} on the {@code JavaFX Application Thread}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * <code>
	 * nodeSelectionTabPane.addLater(object, tab -&gt; {});
	 * </code>
	 * </pre>
	 * 
	 * @param object an {@code Object} instance of type {@code U}
	 * @throws NullPointerException thrown if, and only if, {@code object} is {@code null}
	 */
	public void addLater(final U object) {
		addLater(object, tab -> {/* Do nothing. */});
	}
	
	/**
	 * Adds {@code object} of type {@code U} to this {@code NodeSelectionTabPane} instance.
	 * <p>
	 * If either {@code object} or {@code tabConsumer} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method will call {@link #add(Object)} and the {@code accept(Tab)} method of {@code tabConsumer} on the {@code JavaFX Application Thread}.
	 * 
	 * @param object an {@code Object} instance of type {@code U}
	 * @param tabConsumer a {@code Consumer} that accepts a {@code Tab} instance
	 * @throws NullPointerException thrown if, and only if, either {@code object} or {@code tabConsumer} are {@code null}
	 */
	public void addLater(final U object, final Consumer<Tab> tabConsumer) {
		Objects.requireNonNull(object, "object == null");
		Objects.requireNonNull(tabConsumer, "tabConsumer == null");
		
		if(Platform.isFxApplicationThread()) {
			tabConsumer.accept(add(object));
		} else {
			Platform.runLater(() -> tabConsumer.accept(add(object)));
		}
	}
}