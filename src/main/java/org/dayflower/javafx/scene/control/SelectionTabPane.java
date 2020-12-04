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
package org.dayflower.javafx.scene.control;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

//TODO: Add Javadocs!
public final class SelectionTabPane<T extends Node, U> extends TabPane {
	private final BiPredicate<U, U> comparator;
	private final Class<T> clazz;
	private final Function<T, U> mapperTToU;
	private final Function<U, T> mapperUToT;
	private final Function<U, String> mapperUToText;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public SelectionTabPane(final Class<T> clazz, final BiPredicate<U, U> comparator, final Function<T, U> mapperTToU, final Function<U, T> mapperUToT, final Function<U, String> mapperUToText) {
		this.clazz = Objects.requireNonNull(clazz, "clazz == null");
		this.comparator = Objects.requireNonNull(comparator, "comparator == null");
		this.mapperTToU = Objects.requireNonNull(mapperTToU, "mapperTToU == null");
		this.mapperUToT = Objects.requireNonNull(mapperUToT, "mapperUToT == null");
		this.mapperUToText = Objects.requireNonNull(mapperUToText, "mapperUToText == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
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
	
//	TODO: Add Javadocs!
	public void add(final U object) {
		Objects.requireNonNull(object, "object == null");
		
		for(final Tab tab : getTabs()) {
			final Node node = tab.getContent();
			
			final Class<?> clazz = node.getClass();
			
			if(clazz.isAssignableFrom(this.clazz)) {
				final U object0 = object;
				final U object1 = this.mapperTToU.apply(this.clazz.cast(node));
				
				if(this.comparator.test(object0, object1)) {
					getSelectionModel().select(tab);
					
					return;
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
	}
}