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
package org.dayflower.javafx.application;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.image.Image;
import org.dayflower.javafx.scene.layout.Regions;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RendererConfiguration;

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TreeView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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
	private final RendererBox rendererBox;
	private final SceneBox sceneBox;
	private final TreeView<String> treeView;
	private final VBox vBoxL;
	private final VBox vBoxR;
	private final WritableImage writableImage;
	private final boolean[] isKeyPressed;
	private final boolean[] isKeyPressedOnce;
	private final boolean[] isMouseButtonPressed;
	private final boolean[] isMouseButtonPressedOnce;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code RendererViewPane} instance.
	 * <p>
	 * If either {@code renderer} or {@code executorService} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param renderer the {@link Renderer} instance associated with this {@code RendererViewPane} instance
	 * @param executorService the {@code ExecutorService} associated with this {@code RendererViewPane} instance
	 * @throws NullPointerException thrown if, and only if, either {@code renderer} or {@code executorService} are {@code null}
	 */
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
		this.rendererBox = new RendererBox(renderer, executorService);
		this.sceneBox = new SceneBox(renderer, executorService);
		this.treeView = new SceneTreeView(renderer.getRendererConfiguration().getScene());
		this.vBoxL = new VBox();
		this.vBoxR = new VBox();
		this.writableImage = new WritableImage(renderer.getRendererConfiguration().getImage().getResolutionX(), renderer.getRendererConfiguration().getImage().getResolutionY());
		this.isKeyPressed = new boolean[KeyCode.values().length];
		this.isKeyPressedOnce = new boolean[KeyCode.values().length];
		this.isMouseButtonPressed = new boolean[MouseButton.values().length];
		this.isMouseButtonPressedOnce = new boolean[MouseButton.values().length];
		
		doConfigure();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@code ExecutorService} instance associated with this {@code RendererViewPane} instance.
	 * 
	 * @return the {@code ExecutorService} instance associated with this {@code RendererViewPane} instance
	 */
	public ExecutorService getExecutorService() {
		return this.executorService;
	}
	
	/**
	 * Returns the optional {@code File} for saving.
	 * 
	 * @return the optional {@code File} for saving
	 */
	public Optional<File> getFile() {
		return Optional.ofNullable(this.file.get());
	}
	
	/**
	 * Returns the {@link Renderer} instance associated with this {@code RendererViewPane} instance.
	 * 
	 * @return the {@code Renderer} instance associated with this {@code RendererViewPane} instance
	 */
	public Renderer getRenderer() {
		return this.renderer;
	}
	
	/**
	 * Returns {@code true} if, and only if, at least one key is being pressed, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, at least one key is being pressed, {@code false} otherwise
	 */
	public boolean isKeyPressed() {
		return this.keysPressed.get() > 0;
	}
	
	/**
	 * Returns {@code true} if, and only if, the key denoted by {@code keyCode} is being pressed, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to calling {@code isKeyPressed(keyCode, false)}.
	 * <p>
	 * If {@code keyCode} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param keyCode a {@code KeyCode}
	 * @return {@code true} if, and only if, the key denoted by {@code keyCode} is being pressed, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code keyCode} is {@code null}
	 */
	public boolean isKeyPressed(final KeyCode keyCode) {
		return isKeyPressed(keyCode, false);
	}
	
	/**
	 * Returns {@code true} if, and only if, the key denoted by {@code keyCode} is being pressed, {@code false} otherwise.
	 * <p>
	 * If {@code isKeyPressedOnce} is {@code true}, only the first call to this method will return {@code true} per press-release cycle given a specific key.
	 * <p>
	 * If {@code keyCode} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param keyCode a {@code KeyCode}
	 * @param isKeyPressedOnce {@code true} if, and only if, a key press should occur at most one time per press-release cycle, {@code false} otherwise
	 * @return {@code true} if, and only if, the key denoted by {@code keyCode} is being pressed, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code keyCode} is {@code null}
	 */
	public boolean isKeyPressed(final KeyCode keyCode, final boolean isKeyPressedOnce) {
		final boolean isKeyPressed = this.isKeyPressed[keyCode.ordinal()];
		
		if(isKeyPressedOnce) {
			final boolean isKeyPressedOnce0 = this.isKeyPressedOnce[keyCode.ordinal()];
			
			if(isKeyPressed && !isKeyPressedOnce0) {
				this.isKeyPressedOnce[keyCode.ordinal()] = true;
				
				return true;
			}
			
			return false;
		}
		
		return isKeyPressed;
	}
	
	/**
	 * Returns {@code true} if, and only if, at least one mouse button is being pressed, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, at least one mouse button is being pressed, {@code false} otherwise
	 */
	public boolean isMouseButtonPressed() {
		return this.mouseButtonsPressed.get() > 0;
	}
	
	/**
	 * Returns {@code true} if, and only if, the mouse button denoted by {@code mouseButton} is being pressed, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to calling {@code isMouseButtonPressed(mouseButton, false)}.
	 * <p>
	 * If {@code mouseButton} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mouseButton a {@code MouseButton}
	 * @return {@code true} if, and only if, the mouse button denoted by {@code mouseButton} is being pressed, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code mouseButton} is {@code null}
	 */
	public boolean isMouseButtonPressed(final MouseButton mouseButton) {
		return isMouseButtonPressed(mouseButton, false);
	}
	
	/**
	 * Returns {@code true} if, and only if, the mouse button denoted by {@code mouseButton} is being pressed, {@code false} otherwise.
	 * <p>
	 * If {@code isMouseButtonPressedOnce} is {@code true}, only the first call to this method will return {@code true} per press-release cycle given a specific mouse button.
	 * <p>
	 * If {@code mouseButton} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mouseButton a {@code MouseButton}
	 * @param isMouseButtonPressedOnce {@code true} if, and only if, a mouse button press should occur at most one time per press-release cycle, {@code false} otherwise
	 * @return {@code true} if, and only if, the mouse button denoted by {@code mouseButton} is being pressed, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code mouseButton} is {@code null}
	 */
	public boolean isMouseButtonPressed(final MouseButton mouseButton, final boolean isMouseButtonPressedOnce) {
		final boolean isMouseButtonPressed = this.isMouseButtonPressed[mouseButton.ordinal()];
		
		if(isMouseButtonPressedOnce) {
			final boolean isMouseButtonPressedOnce0 = this.isMouseButtonPressedOnce[mouseButton.ordinal()];
			
			if(isMouseButtonPressed && !isMouseButtonPressedOnce0) {
				this.isMouseButtonPressedOnce[mouseButton.ordinal()] = true;
				
				return true;
			}
			
			return false;
		}
		
		return isMouseButtonPressed;
	}
	
	/**
	 * Returns the X-coordinate of the mouse.
	 * 
	 * @return the X-coordinate of the mouse
	 */
	public double getMouseX() {
		return Double.longBitsToDouble(this.mouseX.get());
	}
	
	/**
	 * Returns the Y-coordinate of the mouse.
	 * 
	 * @return the Y-coordinate of the mouse
	 */
	public double getMouseY() {
		return Double.longBitsToDouble(this.mouseY.get());
	}
	
	/**
	 * This method is called when it's time to render.
	 */
	public void render() {
		final ExecutorService executorService = this.executorService;
		
		if(!executorService.isShutdown()) {
			final AtomicReference<RendererTask> rendererTask = this.rendererTask;
			
			final RendererTask oldRendererTask = rendererTask.get();
			
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
				
				rendererTask.set(newRendererTask);
				
				try {
					executorService.execute(newRendererTask);
				} catch(final RejectedExecutionException e) {
//					One of the methods shutdown() and shutdownNow() of the ExecutorService has been called.
//					The next time this render() method is called, nothing will happen.
				}
			}
		}
	}
	
	/**
	 * This method is called when it's time to save the {@link Image}.
	 */
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
	
	/**
	 * Sets the {@code File} for saving to {@code file}.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param file the {@code File} for saving
	 * @throws NullPointerException} thrown if, and only if, {@code file} is {@code null}
	 */
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
		this.hBox.getChildren().add(Regions.createRegionHBoxHorizontalGrowAlways());
		this.hBox.getChildren().add(this.progressBar);
		this.hBox.setBorder(new Border(new BorderStroke(Color.rgb(181, 181, 181), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1.0D, 0.0D, 0.0D, 0.0D))));
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
		
//		Configure the TreeView:
		
		
//		Configure the VBox for L:
		this.vBoxL.getChildren().add(this.rendererBox);
		this.vBoxL.getChildren().add(this.sceneBox);
		this.vBoxL.setBorder(new Border(new BorderStroke(Color.rgb(181, 181, 181), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.0D, 1.0D, 0.0D, 0.0D))));
		this.vBoxL.setFillWidth(true);
		this.vBoxL.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		this.vBoxL.setSpacing(20.0D);
		
//		Configure the VBox for R:
		this.vBoxR.getChildren().add(this.treeView);
		this.vBoxR.setBorder(new Border(new BorderStroke(Color.rgb(181, 181, 181), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.0D, 0.0D, 0.0D, 1.0D))));
		this.vBoxR.setFillWidth(true);
		this.vBoxR.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		this.vBoxR.setSpacing(20.0D);
		
//		Configure the RendererViewPane:
		setBottom(this.hBox);
		setCenter(this.canvas);
		setLeft(this.vBoxL);
		setRight(this.vBoxR);
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