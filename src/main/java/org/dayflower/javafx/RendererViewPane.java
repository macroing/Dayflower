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
package org.dayflower.javafx;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.image.Image;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RendererConfiguration;

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

final class RendererViewPane extends BorderPane {
	private static final PixelFormat<ByteBuffer> PIXEL_FORMAT = PixelFormat.getByteBgraPreInstance();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AtomicInteger keysPressed;
	private final AtomicInteger mouseButtonsPressed;
	private final AtomicLong mouseX;
	private final AtomicLong mouseY;
	private final AtomicReference<File> file;
	private final AtomicReference<RendererTask> rendererTask;
	private final ByteBuffer byteBuffer;
	private final Canvas canvas;
	private final ExecutorService executorService;
	private final HBox hBox;
	private final Label labelRenderPass;
	private final Label labelRenderTime;
	private final Label labelRenderTimePerPass;
	private final ProgressBar progressBar;
	private final Renderer renderer;
	private final WritableImage writableImage;
	private final boolean[] isKeyPressed;
	private final boolean[] isKeyPressedOnce;
	private final boolean[] isMouseButtonPressed;
	private final boolean[] isMouseButtonPressedOnce;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public RendererViewPane(final Renderer renderer, final ExecutorService executorService) {
		this.keysPressed = new AtomicInteger();
		this.mouseButtonsPressed = new AtomicInteger();
		this.mouseX = new AtomicLong(Double.doubleToLongBits(0.0D));
		this.mouseY = new AtomicLong(Double.doubleToLongBits(0.0D));
		this.file = new AtomicReference<>();
		this.rendererTask = new AtomicReference<>();
		this.byteBuffer = ByteBuffer.allocate(renderer.getRendererConfiguration().getImage().getResolutionX() * renderer.getRendererConfiguration().getImage().getResolutionY() * 4);
		this.canvas = new Canvas();
		this.executorService = Objects.requireNonNull(executorService, "executorService == null");
		this.hBox = new HBox();
		this.labelRenderPass = new Label();
		this.labelRenderTime = new Label();
		this.labelRenderTimePerPass = new Label();
		this.progressBar = new ProgressBar();
		this.renderer = renderer;
		this.writableImage = new WritableImage(renderer.getRendererConfiguration().getImage().getResolutionX(), renderer.getRendererConfiguration().getImage().getResolutionY());
		this.isKeyPressed = new boolean[KeyCode.values().length];
		this.isKeyPressedOnce = new boolean[KeyCode.values().length];
		this.isMouseButtonPressed = new boolean[MouseButton.values().length];
		this.isMouseButtonPressedOnce = new boolean[MouseButton.values().length];
		
		doConfigure();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Optional<File> getFile() {
		return Optional.ofNullable(this.file.get());
	}
	
	public Renderer getRenderer() {
		return this.renderer;
	}
	
	public void render() {
		final RendererTask oldRendererTask = this.rendererTask.get();
		
		if(oldRendererTask == null || oldRendererTask.isCancelled() || oldRendererTask.isDone()) {
			final ByteBuffer byteBuffer = this.byteBuffer;
			
			final Canvas canvas = this.canvas;
			
			final Renderer renderer = this.renderer;
			
			final RendererConfiguration rendererConfiguration = renderer.getRendererConfiguration();
			
			final Image image = rendererConfiguration.getImage();
			
			final double resolutionX = image.getResolutionX();
			final double resolutionY = image.getResolutionY();
			
			final WritableImage writableImage = this.writableImage;
			
			final RendererTask newRendererTask = new RendererTask(() -> Boolean.valueOf(renderer.render()), () -> {
				image.copyTo(byteBuffer.array());
				
				final
				PixelWriter pixelWriter = writableImage.getPixelWriter();
				pixelWriter.setPixels(0, 0, image.getResolutionX(), image.getResolutionY(), PIXEL_FORMAT, byteBuffer, image.getResolutionX() * 4);
				
				final
				GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
				graphicsContext.drawImage(writableImage, 0.0D, 0.0D, writableImage.getWidth(), writableImage.getHeight(), 0.0D, 0.0D, resolutionX, resolutionY);
			});
			
			this.rendererTask.set(newRendererTask);
			
			this.executorService.execute(newRendererTask);
		}
	}
	
	public void save() {
		final Optional<File> optionalFile = getFile();
		
		if(optionalFile.isPresent()) {
			final File file = optionalFile.get();
			
			final Renderer renderer = this.renderer;
			
			final RendererConfiguration rendererConfiguration = renderer.getRendererConfiguration();
			
			final
			Image image = rendererConfiguration.getImage();
			image.save(file);
		}
	}
	
	public void setFile(final File file) {
		this.file.set(Objects.requireNonNull(file, "file == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doConfigure() {
//		Retrieve the Image and its resolution:
		final Image image = this.renderer.getRendererConfiguration().getImage();
		
		final double resolutionX = image.getResolutionX();
		final double resolutionY = image.getResolutionY();
		
//		Configure the Canvas:
		this.canvas.addEventFilter(MouseEvent.ANY, e -> this.canvas.requestFocus());
		this.canvas.addEventFilter(KeyEvent.ANY, e -> this.canvas.requestFocus());
		this.canvas.setFocusTraversable(true);
		this.canvas.setHeight(resolutionY);
		this.canvas.setOnKeyPressed(this::doOnKeyPressed);
		this.canvas.setOnKeyReleased(this::doOnKeyReleased);
		this.canvas.setOnMouseDragged(this::doOnMouseDragged);
		this.canvas.setOnMouseMoved(this::doOnMouseMoved);
		this.canvas.setOnMousePressed(this::doOnMousePressed);
		this.canvas.setOnMouseReleased(this::doOnMouseReleased);
		this.canvas.setWidth(resolutionX);
		
//		Configure the HBox:
		this.hBox.getChildren().add(this.labelRenderPass);
		this.hBox.getChildren().add(this.labelRenderTime);
		this.hBox.getChildren().add(this.labelRenderTimePerPass);
		this.hBox.getChildren().add(JavaFX.createRegionHBoxGrowAlways());
		this.hBox.getChildren().add(this.progressBar);
		this.hBox.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		this.hBox.setSpacing(20.0D);
		
//		Configure the Label for Render Pass:
		this.labelRenderPass.setText("Render Pass: 0");
		
//		Configure the Label for Render Time:
		this.labelRenderTime.setText("Render Time: 00:00:00");
		
//		Configure the Label for Render Time Per Pass:
		this.labelRenderTimePerPass.setText("Render Time Per Pass: 0");
		
//		Configure the ProgressBar:
		this.progressBar.setProgress(0.0D);
		
//		Configure the Renderer:
		this.renderer.setRendererObserver(new RendererObserverImpl(this.labelRenderPass, this.labelRenderTime, this.labelRenderTimePerPass, this.progressBar));
		
//		Configure the RendererViewPane:
		setBottom(this.hBox);
		setCenter(this.canvas);
	}
	
	private void doOnKeyPressed(final KeyEvent keyEvent) {
		if(!this.isKeyPressed[keyEvent.getCode().ordinal()]) {
			this.keysPressed.incrementAndGet();
		}
		
		this.isKeyPressed[keyEvent.getCode().ordinal()] = true;
	}
	
	private void doOnKeyReleased(final KeyEvent keyEvent) {
		if(this.isKeyPressed[keyEvent.getCode().ordinal()]) {
			this.keysPressed.decrementAndGet();
		}
		
		this.isKeyPressed[keyEvent.getCode().ordinal()] = false;
		this.isKeyPressedOnce[keyEvent.getCode().ordinal()] = false;
	}
	
	private void doOnMouseDragged(final MouseEvent mouseEvent) {
		this.mouseX.set(Double.doubleToLongBits(mouseEvent.getX()));
		this.mouseY.set(Double.doubleToLongBits(mouseEvent.getY()));
	}
	
	private void doOnMouseMoved(final MouseEvent mouseEvent) {
		this.mouseX.set(Double.doubleToLongBits(mouseEvent.getX()));
		this.mouseY.set(Double.doubleToLongBits(mouseEvent.getY()));
	}
	
	private void doOnMousePressed(final MouseEvent mouseEvent) {
		if(!this.isMouseButtonPressed[mouseEvent.getButton().ordinal()]) {
			this.mouseButtonsPressed.incrementAndGet();
		}
		
		this.isMouseButtonPressed[mouseEvent.getButton().ordinal()] = true;
	}
	
	private void doOnMouseReleased(final MouseEvent mouseEvent) {
		if(this.isMouseButtonPressed[mouseEvent.getButton().ordinal()]) {
			this.mouseButtonsPressed.decrementAndGet();
		}
		
		this.isMouseButtonPressed[mouseEvent.getButton().ordinal()] = false;
		this.isMouseButtonPressedOnce[mouseEvent.getButton().ordinal()] = false;
	}
}