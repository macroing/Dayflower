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

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;

/**
 * An {@code ObjectTreeView} is a {@code TreeView} that simplifies {@code TreeItem} creation with or without {@code ContextMenu} instances.
 * <p>
 * To use this class, consider the following example:
 * <pre>
 * {@code
 * Function<Object, ContextMenu> mapperUToContextMenu = object -> object instanceof Integer ? ContextMenus.createContextMenu(MenuItems.createMenuItem("Print", actionEvent -> System.out.println(object))) : null;
 * Function<Object, List<Object>> mapperUToListU = object -> object instanceof Integer ? Arrays.asList("A", "B", "C") : Arrays.asList();
 * Function<Object, Node> mapperUToGraphic = object -> null;
 * Function<Object, String> mapperUToT = object -> object.toString();
 * 
 * ObjectTreeView<String, Object> objectTreeView = new ObjectTreeView<>(mapperUToContextMenu, mapperUToListU, mapperUToGraphic, mapperUToT, "Root");
 * objectTreeView.add(Integer.valueOf(1));
 * objectTreeView.add(Integer.valueOf(2));
 * objectTreeView.add(Integer.valueOf(3));
 * }
 * </pre>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ObjectTreeView<T, U> extends TreeView<T> {
	/**
	 * Constructs a new {@code ObjectTreeView} instance.
	 * <p>
	 * If either {@code mapperUToContextMenu}, {@code mapperUToListU}, {@code mapperUToGraphic}, {@code mapperUToT} or {@code object} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mapperUToContextMenu a {@code Function} that maps an {@code Object} instance, represented by {@code U}, to a {@code ContextMenu} instance
	 * @param mapperUToListU a {@code Function} that maps an {@code Object} instance, represented by {@code U}, to child {@code Object} instances, also represented by {@code U}
	 * @param mapperUToGraphic a {@code Function} that maps an {@code Object} instance, represented by {@code U}, to a graphic {@code Node}
	 * @param mapperUToT a {@code Function} that maps an {@code Object} instance, represented by {@code U}, to an {@code Object} instance, represented by {@code T}, that acts as the value
	 * @param object an {@code Object} instance, represented by {@code U}, that acts as the root
	 * @throws NullPointerException thrown if, and only if, either {@code mapperUToContextMenu}, {@code mapperUToListU}, {@code mapperUToGraphic}, {@code mapperUToT} or {@code object} are {@code null}
	 */
	public ObjectTreeView(final Function<U, ContextMenu> mapperUToContextMenu, final Function<U, List<U>> mapperUToListU, final Function<U, Node> mapperUToGraphic, final Function<U, T> mapperUToT, final U object) {
		setCellFactory(treeView -> new ObjectTextFieldTreeCell<>(mapperUToContextMenu, mapperUToGraphic));
		setRoot(new ObjectTreeItem<>(mapperUToT.apply(Objects.requireNonNull(object, "object == null")), mapperUToContextMenu, mapperUToListU, mapperUToT, object));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds {@code object} to the root of this {@code ObjectTreeView} instance.
	 * <p>
	 * If {@code object} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method will create all child {@code Object} instances using the mappers.
	 * 
	 * @param object the {@code Object} instance to add
	 * @throws NullPointerException thrown if, and only if, {@code object} is {@code null}
	 */
	public void add(final U object) {
		Objects.requireNonNull(object, "object == null");
		
		final TreeItem<T> treeItem = getRoot();
		
		if(treeItem instanceof ObjectTreeItem<?, ?>) {
			@SuppressWarnings("unchecked")
			final
			ObjectTreeItem<T, U> objectTreeItem = ObjectTreeItem.class.cast(treeItem);
			objectTreeItem.add(object);
		}
	}
	
	/**
	 * Removes {@code object} from the root of this {@code ObjectTreeView} instance.
	 * <p>
	 * If {@code object} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param object the {@code Object} instance to remove
	 * @throws NullPointerException thrown if, and only if, {@code object} is {@code null}
	 */
	public void remove(final U object) {
		Objects.requireNonNull(object, "object == null");
		
		final TreeItem<T> treeItem = getRoot();
		
		if(treeItem instanceof ObjectTreeItem<?, ?>) {
			@SuppressWarnings("unchecked")
			final
			ObjectTreeItem<T, U> objectTreeItem = ObjectTreeItem.class.cast(treeItem);
			objectTreeItem.remove(object);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class ObjectTextFieldTreeCell<T, U> extends TextFieldTreeCell<T> {
		private final Function<U, ContextMenu> mapperUToContextMenu;
		private final Function<U, Node> mapperUToGraphic;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public ObjectTextFieldTreeCell(final Function<U, ContextMenu> mapperUToContextMenu, final Function<U, Node> mapperUToGraphic) {
			this.mapperUToContextMenu = Objects.requireNonNull(mapperUToContextMenu, "mapperUToContextMenu == null");
			this.mapperUToGraphic = Objects.requireNonNull(mapperUToGraphic, "mapperUToGraphic == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		@Override
		public void updateItem(final T item, final boolean isEmpty) {
			super.updateItem(item, isEmpty);
			
			final TreeItem<T> treeItem = getTreeItem();
			
			if(isEmpty) {
				setContextMenu(null);
				setGraphic(null);
			} else if(treeItem instanceof ObjectTreeItem<?, ?>) {
				@SuppressWarnings("unchecked")
				final ObjectTreeItem<T, U> objectTreeItem = ObjectTreeItem.class.cast(treeItem);
				
				final U object = objectTreeItem.getObject();
				
				final ContextMenu contextMenu = this.mapperUToContextMenu.apply(object);
				
				final Node graphic = this.mapperUToGraphic.apply(object);
				
				setContextMenu(contextMenu);
				setGraphic(graphic);
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class ObjectTreeItem<T, U> extends TreeItem<T> {
		private final Function<U, ContextMenu> mapperUToContextMenu;
		private final Function<U, List<U>> mapperUToListU;
		private final Function<U, T> mapperUToT;
		private final U object;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public ObjectTreeItem(final T value, final Function<U, ContextMenu> mapperUToContextMenu, final Function<U, List<U>> mapperUToListU, final Function<U, T> mapperUToT, final U object) {
			super(value);
			
			this.mapperUToContextMenu = Objects.requireNonNull(mapperUToContextMenu, "mapperUToContextMenu");
			this.mapperUToListU = Objects.requireNonNull(mapperUToListU, "mapperUToListU");
			this.mapperUToT = Objects.requireNonNull(mapperUToT, "mapperUToT == null");
			this.object = Objects.requireNonNull(object, "object == null");
			
			setExpanded(true);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public U getObject() {
			return this.object;
		}
		
		public void add(final List<U> objects) {
			Objects.requireNonNull(objects, "objects == null");
			
			for(final U object : objects) {
				add(object);
			}
		}
		
		public void add(final U object) {
			Objects.requireNonNull(object, "object == null");
			
			final T value = this.mapperUToT.apply(object);
			
			final
			ObjectTreeItem<T, U> objectTreeItem = new ObjectTreeItem<>(value, this.mapperUToContextMenu, this.mapperUToListU, this.mapperUToT, object);
			objectTreeItem.add(this.mapperUToListU.apply(object));
			
			getChildren().add(objectTreeItem);
		}
		
		public void remove(final U object) {
			Objects.requireNonNull(object, "object == null");
			
			for(final TreeItem<T> treeItem : getChildren()) {
				if(treeItem instanceof ObjectTreeItem<?, ?>) {
					@SuppressWarnings("unchecked")
					final ObjectTreeItem<T, U> objectTreeItem = ObjectTreeItem.class.cast(treeItem);
					
					if(objectTreeItem.getObject().equals(object)) {
						getChildren().remove(treeItem);
						
						break;
					}
					
//					Should this behaviour be supported?
//					objectTreeItem.remove(object);
				}
			}
		}
	}
}