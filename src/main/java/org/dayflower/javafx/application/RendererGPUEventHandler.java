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
package org.dayflower.javafx.application;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;

final class RendererGPUEventHandler implements EventHandler<ActionEvent> {
	private final AtomicBoolean isUsingGPU;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public RendererGPUEventHandler(final AtomicBoolean isUsingGPU) {
		this.isUsingGPU = Objects.requireNonNull(isUsingGPU, "isUsingGPU == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void handle(final ActionEvent actionEvent) {
		final Object source = actionEvent.getSource();
		
		if(source instanceof CheckMenuItem) {
			this.isUsingGPU.set(CheckMenuItem.class.cast(source).isSelected());
		}
	}
}