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

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on {@code ColumnConstraints} and {@code GridPane} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class GridPanes {
	private GridPanes() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code ColumnConstraints} instance with a {@code Priority} of {@code priority}.
	 * 
	 * @param priority a {@code Priority} instance
	 * @return a new {@code ColumnConstraints} instance with a {@code Priority} of {@code priority}
	 */
	public static ColumnConstraints createHorizontalColumnConstraints(final Priority priority) {
		final
		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setHgrow(priority);
		
		return columnConstraints;
	}
	
	/**
	 * Returns a new {@code ColumnConstraints} instance with a {@code Priority} of {@code priority} and a preferred width of {@code preferredWidth}.
	 * 
	 * @param priority a {@code Priority} instance
	 * @param preferredWidth a {@code double} with the preferred width
	 * @return a new {@code ColumnConstraints} instance with a {@code Priority} of {@code priority} and a preferred width of {@code preferredWidth}
	 */
	public static ColumnConstraints createHorizontalColumnConstraints(final Priority priority, final double preferredWidth) {
		final
		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setHgrow(priority);
		columnConstraints.setPrefWidth(preferredWidth);
		
		return columnConstraints;
	}
	
	/**
	 * Returns a new {@code GridPane} instance with a horizontal gap of {@code horizontalGap} and a vertical gap of {@code verticalGap}.
	 * 
	 * @param horizontalGap a {@code double} with the horizontal gap
	 * @param verticalGap a {@code double} with the vertical gap
	 * @return a new {@code GridPane} instance with a horizontal gap of {@code horizontalGap} and a vertical gap of {@code verticalGap}
	 */
	public static GridPane createGridPane(final double horizontalGap, final double verticalGap) {
		final
		GridPane gridPane = new GridPane();
		gridPane.setHgap(horizontalGap);
		gridPane.setVgap(verticalGap);
		
		return gridPane;
	}
}