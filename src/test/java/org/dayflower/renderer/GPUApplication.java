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
package org.dayflower.renderer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.dayflower.javafx.canvas.ConcurrentByteArrayCanvas;

import javafx.application.Application;
import javafx.stage.Stage;

public final class GPUApplication extends Application {
	private final ExecutorService executorService;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public GPUApplication() {
		this.executorService = Executors.newFixedThreadPool(1);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void start(final Stage stage) {
		
		
//		final
//		ConcurrentByteArrayCanvas concurrentByteArrayCanvas = new ConcurrentByteArrayCanvas();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
		launch(args);
	}
}