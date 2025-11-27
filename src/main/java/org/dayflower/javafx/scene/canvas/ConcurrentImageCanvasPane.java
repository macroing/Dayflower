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
package org.dayflower.javafx.scene.canvas;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.dayflower.image.Image;
import org.dayflower.javafx.scene.canvas.ConcurrentImageCanvas.ImageUpdater;

import org.macroing.java.util.function.TriFunction;

import javafx.scene.layout.Pane;

/**
 * A {@code ConcurrentImageCanvasPane} is a {@code Pane} that can resize a {@link ConcurrentImageCanvas} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ConcurrentImageCanvasPane<T extends Image> extends Pane {
	/**
	 * Constructs a new {@code ConcurrentImageCanvasPane} instance.
	 * <p>
	 * If either {@code concurrentImageCanvas} or {@code imageConstructionFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param concurrentImageCanvas a {@link ConcurrentImageCanvas} instance
	 * @param imageConstructionFunction a {@code TriFunction} instance that can construct a new {@link Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code concurrentImageCanvas} or {@code imageConstructionFunction} are {@code null}
	 */
	public ConcurrentImageCanvasPane(final ConcurrentImageCanvas<T> concurrentImageCanvas, final TriFunction<T, Number, Number, T> imageConstructionFunction) {
		Objects.requireNonNull(concurrentImageCanvas, "concurrentImageCanvas == null");
		Objects.requireNonNull(imageConstructionFunction, "imageConstructionFunction == null");
		
		getChildren().add(concurrentImageCanvas);
		
		setHeight(concurrentImageCanvas.getHeight());
		setWidth(concurrentImageCanvas.getWidth());
		
		final AtomicInteger imageResolutionX = new AtomicInteger(-1);
		final AtomicInteger imageResolutionY = new AtomicInteger(-1);
		
		heightProperty().addListener((observable, oldValue, newValue) -> concurrentImageCanvas.setImageUpdater(new ImageUpdater<>(imageConstructionFunction, imageResolutionX.get(), imageResolutionY.updateAndGet(currentValue -> newValue.intValue()))));
		widthProperty().addListener((observable, oldValue, newValue) -> concurrentImageCanvas.setImageUpdater(new ImageUpdater<>(imageConstructionFunction, imageResolutionX.updateAndGet(currentValue -> newValue.intValue()), imageResolutionY.get())));
	}
}