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

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.image.Image;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * An {@code AbstractCanvasApplication} is an abstract extension of {@code Application} that contains a {@code Canvas} and handles input.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractCanvasApplication extends Application {
	private final AtomicBoolean hasRequestedToExit;
	private final AtomicInteger keysPressed;
	private final AtomicInteger mouseButtonsPressed;
	private final AtomicLong mouseX;
	private final AtomicLong mouseY;
	private final AtomicLong resolutionX;
	private final AtomicLong resolutionY;
	private final AtomicReference<RendererTask> rendererTask;
	private final Canvas canvas;
	private final ExecutorService executorService;
	private final String title;
	private final boolean[] isKeyPressed;
	private final boolean[] isKeyPressedOnce;
	private final boolean[] isMouseButtonPressed;
	private final boolean[] isMouseButtonPressedOnce;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractCanvasApplication} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new AbstractCanvasApplication("");
	 * }
	 * </pre>
	 */
	protected AbstractCanvasApplication() {
		this("");
	}
	
	/**
	 * Constructs a new {@code AbstractCanvasApplication} instance.
	 * <p>
	 * If {@code title} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new AbstractCanvasApplication(title, 800.0D, 800.0D);
	 * }
	 * </pre>
	 * 
	 * @param title the title to show
	 * @throws NullPointerException thrown if, and only if, {@code title} is {@code null}
	 */
	protected AbstractCanvasApplication(final String title) {
		this(title, 800.0D, 800.0D);
	}
	
	/**
	 * Constructs a new {@code AbstractCanvasApplication} instance.
	 * <p>
	 * If {@code title} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param title the title to show
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @throws NullPointerException thrown if, and only if, {@code title} is {@code null}
	 */
	protected AbstractCanvasApplication(final String title, final double resolutionX, final double resolutionY) {
		this.title = Objects.requireNonNull(title, "title == null");
		this.hasRequestedToExit = new AtomicBoolean();
		this.keysPressed = new AtomicInteger();
		this.mouseButtonsPressed = new AtomicInteger();
		this.resolutionX = new AtomicLong(Double.doubleToLongBits(resolutionX));
		this.resolutionY = new AtomicLong(Double.doubleToLongBits(resolutionY));
		this.mouseX = new AtomicLong(Double.doubleToLongBits(0.0D));
		this.mouseY = new AtomicLong(Double.doubleToLongBits(0.0D));
		this.rendererTask = new AtomicReference<>();
		this.canvas = new Canvas();
		this.executorService = Executors.newFixedThreadPool(1);
		this.isKeyPressed = new boolean[KeyCode.values().length];
		this.isKeyPressedOnce = new boolean[KeyCode.values().length];
		this.isMouseButtonPressed = new boolean[MouseButton.values().length];
		this.isMouseButtonPressedOnce = new boolean[MouseButton.values().length];
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Starts this {@code AbstractCanvasApplication} instance.
	 * 
	 * @param stage a {@code Stage}
	 */
	@Override
	public final void start(final Stage stage) {
		final
		Canvas canvas = this.canvas;
		canvas.addEventFilter(MouseEvent.ANY, e -> canvas.requestFocus());
		canvas.addEventFilter(KeyEvent.ANY, e -> canvas.requestFocus());
		canvas.setFocusTraversable(true);
		canvas.setHeight(getResolutionY());
		canvas.setOnKeyPressed(this::doOnKeyPressed);
		canvas.setOnKeyReleased(this::doOnKeyReleased);
		canvas.setOnMouseDragged(this::doOnMouseDragged);
		canvas.setOnMouseMoved(this::doOnMouseMoved);
		canvas.setOnMousePressed(this::doOnMousePressed);
		canvas.setOnMouseReleased(this::doOnMouseReleased);
		canvas.setWidth(getResolutionX());
		
		final int resolutionX = (int)(getResolutionX());
		final int resolutionY = (int)(getResolutionY());
		
		final AtomicReference<RendererTask> rendererTask = this.rendererTask;
		
		final BorderPane borderPane = new BorderPane(this.canvas);
		
		final ByteBuffer byteBuffer = ByteBuffer.allocate(resolutionX * resolutionY * 4);
		
		final ExecutorService executorService = this.executorService;
		
		final Image image = new Image(resolutionX, resolutionY);
		
		final PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteBgraPreInstance();
		
		initializeGUI(borderPane);
		
		final Scene scene = new Scene(borderPane);
		
		final WritableImage writableImage = new WritableImage(resolutionX, resolutionY);
		
		stage.setResizable(false);
		stage.setScene(scene);
		stage.setTitle(this.title);
		stage.sizeToScene();
		stage.show();
		
		new AnimationTimer() {
			@Override
			public void handle(final long now) {
				if(hasRequestedToExit()) {
					Platform.exit();
				} else {
					update();
					
					final RendererTask oldRendererTask = rendererTask.get();
					
					if(oldRendererTask == null || oldRendererTask.isCancelled() || oldRendererTask.isDone()) {
						final RendererTask newRendererTask = new RendererTask(() -> {
							return Boolean.valueOf(render(image));
						}, () -> {
							image.copyTo(byteBuffer.array());
							
							final
							PixelWriter pixelWriter = writableImage.getPixelWriter();
							pixelWriter.setPixels(0, 0, image.getResolutionX(), image.getResolutionY(), pixelFormat, byteBuffer, image.getResolutionX() * 4);
							
							final
							GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
							graphicsContext.drawImage(writableImage, 0.0D, 0.0D, writableImage.getWidth(), writableImage.getHeight(), 0.0D, 0.0D, getResolutionX(), getResolutionY());
						});
						
						rendererTask.set(newRendererTask);
						
						executorService.execute(newRendererTask);
					}
				}
			}
		}.start();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, exit has been requested, {@code false} otherwise.
	 * <p>
	 * To request this {@code AbstractCanvasApplication} to exit, call {@link #exit()}.
	 * 
	 * @return {@code true} if, and only if, exit has been requested, {@code false} otherwise
	 */
	protected final boolean hasRequestedToExit() {
		return this.hasRequestedToExit.get();
	}
	
	/**
	 * Returns {@code true} if, and only if, at least one key is being pressed, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, at least one key is being pressed, {@code false} otherwise
	 */
	protected final boolean isKeyPressed() {
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
	protected final boolean isKeyPressed(final KeyCode keyCode) {
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
	protected final boolean isKeyPressed(final KeyCode keyCode, final boolean isKeyPressedOnce) {
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
	protected final boolean isMouseButtonPressed() {
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
	protected final boolean isMouseButtonPressed(final MouseButton mouseButton) {
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
	protected final boolean isMouseButtonPressed(final MouseButton mouseButton, final boolean isMouseButtonPressedOnce) {
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
	 * This method is called when it's time to render.
	 * <p>
	 * Returns {@code true} if, and only if, something was rendered, {@code false} otherwise.
	 * <p>
	 * This method is not called every frame. It's also not called from the JavaFX Application Thread.
	 * 
	 * @param image the {@code Image} to render to
	 * @return {@code true} if, and only if, something was rendered, {@code false} otherwise
	 */
	protected abstract boolean render(final Image image);
	
	/**
	 * Returns the resolution of the X-axis.
	 * 
	 * @return the resolution of the X-axis
	 */
	protected final double getResolutionX() {
		return Double.longBitsToDouble(this.resolutionX.get());
	}
	
	/**
	 * Returns the resolution of the Y-axis.
	 * 
	 * @return the resolution of the Y-axis
	 */
	protected final double getResolutionY() {
		return Double.longBitsToDouble(this.resolutionY.get());
	}
	
	/**
	 * Returns the X-coordinate of the mouse.
	 * 
	 * @return the X-coordinate of the mouse
	 */
	protected final double getMouseX() {
		return Double.longBitsToDouble(this.mouseX.get());
	}
	
	/**
	 * Returns the Y-coordinate of the mouse.
	 * 
	 * @return the Y-coordinate of the mouse
	 */
	protected final double getMouseY() {
		return Double.longBitsToDouble(this.mouseY.get());
	}
	
	/**
	 * Call this method when it's time to exit.
	 */
	protected final void exit() {
		this.hasRequestedToExit.set(true);
	}
	
	/**
	 * This method is called to initialize the GUI.
	 * 
	 * @param borderPane the main {@code BorderPane}
	 */
	protected abstract void initializeGUI(final BorderPane borderPane);
	
	/**
	 * Sets the resolution of the X- and Y-axes.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 */
	protected final void setResolution(final double resolutionX, final double resolutionY) {
		this.resolutionX.set(Double.doubleToLongBits(resolutionX));
		this.resolutionY.set(Double.doubleToLongBits(resolutionY));
	}
	
	/**
	 * Sets the resolution of the X-axis.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 */
	protected final void setResolutionX(final double resolutionX) {
		this.resolutionX.set(Double.doubleToLongBits(resolutionX));
	}
	
	/**
	 * Sets the resolution of the Y-axis.
	 * 
	 * @param resolutionY the resolution of the Y-axis
	 */
	protected final void setResolutionY(final double resolutionY) {
		this.resolutionY.set(Double.doubleToLongBits(resolutionY));
	}
	
	/**
	 * This method is called every frame when it's time to update.
	 */
	protected abstract void update();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doOnKeyPressed(final KeyEvent e) {
		if(!this.isKeyPressed[e.getCode().ordinal()]) {
			this.keysPressed.incrementAndGet();
		}
		
		this.isKeyPressed[e.getCode().ordinal()] = true;
	}
	
	private void doOnKeyReleased(final KeyEvent e) {
		if(this.isKeyPressed[e.getCode().ordinal()]) {
			this.keysPressed.decrementAndGet();
		}
		
		this.isKeyPressed[e.getCode().ordinal()] = false;
		this.isKeyPressedOnce[e.getCode().ordinal()] = false;
	}
	
	private void doOnMouseDragged(final MouseEvent e) {
		this.mouseX.set(Double.doubleToLongBits(e.getX()));
		this.mouseY.set(Double.doubleToLongBits(e.getY()));
	}
	
	private void doOnMouseMoved(final MouseEvent e) {
		this.mouseX.set(Double.doubleToLongBits(e.getX()));
		this.mouseY.set(Double.doubleToLongBits(e.getY()));
	}
	
	private void doOnMousePressed(final MouseEvent e) {
		if(!this.isMouseButtonPressed[e.getButton().ordinal()]) {
			this.mouseButtonsPressed.incrementAndGet();
		}
		
		this.isMouseButtonPressed[e.getButton().ordinal()] = true;
	}
	
	private void doOnMouseReleased(final MouseEvent e) {
		if(this.isMouseButtonPressed[e.getButton().ordinal()]) {
			this.mouseButtonsPressed.decrementAndGet();
		}
		
		this.isMouseButtonPressed[e.getButton().ordinal()] = false;
		this.isMouseButtonPressedOnce[e.getButton().ordinal()] = false;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class RendererTask extends Task<Boolean> {
		private final Callable<Boolean> callableCall;
		private final Runnable runnableSucceeded;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public RendererTask(final Callable<Boolean> callableCall, final Runnable runnableSucceeded) {
			this.callableCall = Objects.requireNonNull(callableCall, "callableCall == null");
			this.runnableSucceeded = Objects.requireNonNull(runnableSucceeded, "runnableSucceeded == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		@Override
		protected Boolean call() {
			try {
				return this.callableCall.call();
			} catch(final Exception e) {
				doReportThrowable(e);
				
				return Boolean.FALSE;
			}
		}
		
		@Override
		protected void succeeded() {
			try {
				if(get().booleanValue()) {
					this.runnableSucceeded.run();
				}
			} catch(final InterruptedException e) {
				doReportThrowable(e);
			} catch(final ExecutionException e) {
				doReportThrowable(e);
			}
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private static void doReportThrowable(final Throwable throwable) {
			throwable.printStackTrace();
		}
	}
}