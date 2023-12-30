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
package org.dayflower.javafx.application;

import java.util.Objects;

import org.dayflower.renderer.CombinedProgressiveImageOrderRenderer;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;

final class TabOnClosedEventHandler implements EventHandler<Event> {
	private final CombinedProgressiveImageOrderRenderer combinedProgressiveImageOrderRenderer;
	private final Tab tab;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public TabOnClosedEventHandler(final CombinedProgressiveImageOrderRenderer combinedProgressiveImageOrderRenderer, final Tab tab) {
		this.combinedProgressiveImageOrderRenderer = Objects.requireNonNull(combinedProgressiveImageOrderRenderer, "combinedProgressiveImageOrderRenderer == null");
		this.tab = Objects.requireNonNull(tab, "tab == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void handle(final Event event) {
		final Object source = event.getSource();
		
		if(source == this.tab) {
			this.combinedProgressiveImageOrderRenderer.renderShutdown();
			this.combinedProgressiveImageOrderRenderer.dispose();
		}
	}
}