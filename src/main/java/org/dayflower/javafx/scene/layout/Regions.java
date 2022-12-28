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

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on {@code Region} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Regions {
	private Regions() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code Region} instance with padding.
	 * 
	 * @param top the top offset
	 * @param right the right offset
	 * @param bottom the bottom offset
	 * @param left the left offset
	 * @return a new {@code Region} instance with padding
	 */
	public static Region createRegion(final double top, final double right, final double bottom, final double left) {
		final
		Region region = new Region();
		region.setPadding(new Insets(top, right, bottom, left));
		
		return region;
	}
	
	/**
	 * Returns a new {@code Region} instance with {@code HBox} horizontal grow set to {@code Priority.ALWAYS}.
	 * 
	 * @return a new {@code Region} instance with {@code HBox} horizontal grow set to {@code Priority.ALWAYS}
	 */
	public static Region createRegionHBoxHorizontalGrowAlways() {
		final Region region = new Region();
		
		HBox.setHgrow(region, Priority.ALWAYS);
		
		return region;
	}
}