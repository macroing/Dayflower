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
package org.dayflower.javafx.scene.canvas;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import org.dayflower.color.ArrayComponentOrder;
import org.dayflower.image.Image;
import org.dayflower.javafx.concurrent.PredicateTask;

import org.macroing.java.util.function.TriFunction;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * A {@code ConcurrentImageCanvas} is a {@code Canvas} that performs rendering to an {@link Image} using an {@code ExecutorService} and updates the {@code Canvas} on the {@code FX Application Thread}.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ConcurrentImageCanvas<T extends Image> extends Canvas {
	private static final PixelFormat<ByteBuffer> PIXEL_FORMAT = PixelFormat.getByteBgraPreInstance();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AtomicInteger keysPressed;
	private final AtomicInteger mouseButtonsPressed;
	private final AtomicInteger mouseDraggedDeltaX;
	private final AtomicInteger mouseDraggedDeltaY;
	private final AtomicInteger mouseDraggedX;
	private final AtomicInteger mouseDraggedY;
	private final AtomicInteger mouseMovedDeltaX;
	private final AtomicInteger mouseMovedDeltaY;
	private final AtomicInteger mouseMovedX;
	private final AtomicInteger mouseMovedY;
	private final AtomicLong mouseX;
	private final AtomicLong mouseY;
	private final AtomicReference<ByteBuffer> byteBuffer;
	private final AtomicReference<ImageUpdater<T>> imageUpdater;
	private final AtomicReference<PredicateTask> predicateTask;
	private final AtomicReference<T> image;
	private final AtomicReference<WritableImage> writableImage;
	private final ExecutorService executorService;
	private final Observer observer;
	private final Predicate<T> renderPredicate;
	private final boolean[] isKeyPressed;
	private final boolean[] isKeyPressedOnce;
	private final boolean[] isMouseButtonPressed;
	private final boolean[] isMouseButtonPressedOnce;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ConcurrentImageCanvas} instance.
	 * <p>
	 * If either {@code executorService}, {@code image} or {@code renderPredicate} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ConcurrentImageCanvas(executorService, image, renderPredicate, (concurrentImageCanvas, x, y) -> {});
	 * }
	 * </pre>
	 * 
	 * @param executorService the {@code ExecutorService} instance to use
	 * @param image the {@link Image} instance to use
	 * @param renderPredicate the {@code Predicate} of {@code Image} that performs the rendering itself
	 * @throws NullPointerException thrown if, and only if, either {@code executorService}, {@code image} or {@code renderPredicate} are {@code null}
	 */
	public ConcurrentImageCanvas(final ExecutorService executorService, final T image, final Predicate<T> renderPredicate) {
		this(executorService, image, renderPredicate, (concurrentImageCanvas, x, y) -> {});
	}
	
	/**
	 * Constructs a new {@code ConcurrentImageCanvas} instance.
	 * <p>
	 * If either {@code executorService}, {@code image}, {@code renderPredicate} or {@code observer} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param executorService the {@code ExecutorService} instance to use
	 * @param image the {@link Image} instance to use
	 * @param renderPredicate the {@code Predicate} of {@code Image} that performs the rendering itself
	 * @param observer the {@link Observer} instance to use
	 * @throws NullPointerException thrown if, and only if, either {@code executorService}, {@code image}, {@code renderPredicate} or {@code observer} are {@code null}
	 */
	public ConcurrentImageCanvas(final ExecutorService executorService, final T image, final Predicate<T> renderPredicate, final Observer observer) {
		this.keysPressed = new AtomicInteger();
		this.mouseButtonsPressed = new AtomicInteger();
		this.mouseDraggedDeltaX = new AtomicInteger();
		this.mouseDraggedDeltaY = new AtomicInteger();
		this.mouseDraggedX = new AtomicInteger();
		this.mouseDraggedY = new AtomicInteger();
		this.mouseMovedDeltaX = new AtomicInteger();
		this.mouseMovedDeltaY = new AtomicInteger();
		this.mouseMovedX = new AtomicInteger();
		this.mouseMovedY = new AtomicInteger();
		this.mouseX = new AtomicLong(Double.doubleToLongBits(0.0D));
		this.mouseY = new AtomicLong(Double.doubleToLongBits(0.0D));
		this.byteBuffer = new AtomicReference<>(ByteBuffer.allocate(image.getResolutionX() * image.getResolutionY() * 4));
		this.imageUpdater = new AtomicReference<>();
		this.predicateTask = new AtomicReference<>();
		this.image = new AtomicReference<>(image);
		this.writableImage = new AtomicReference<>(new WritableImage(image.getResolutionX(), image.getResolutionY()));
		this.executorService = Objects.requireNonNull(executorService, "executorService == null");
		this.observer = Objects.requireNonNull(observer, "observer == null");
		this.renderPredicate = Objects.requireNonNull(renderPredicate, "renderPredicate == null");
		this.isKeyPressed = new boolean[KeyCode.values().length];
		this.isKeyPressedOnce = new boolean[KeyCode.values().length];
		this.isMouseButtonPressed = new boolean[MouseButton.values().length];
		this.isMouseButtonPressedOnce = new boolean[MouseButton.values().length];
		
		addEventFilter(MouseEvent.ANY, e -> requestFocus());
		addEventFilter(KeyEvent.ANY, e -> requestFocus());
		setFocusTraversable(true);
		setHeight(image.getResolutionY());
		setOnKeyPressed(this::doOnKeyPressed);
		setOnKeyReleased(this::doOnKeyReleased);
		setOnMouseDragged(this::doOnMouseDragged);
		setOnMouseMoved(this::doOnMouseMoved);
		setOnMousePressed(this::doOnMousePressed);
		setOnMouseReleased(this::doOnMouseReleased);
		setWidth(image.getResolutionX());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@code ExecutorService} instance associated with this {@code ConcurrentImageCanvas} instance.
	 * 
	 * @return the {@code ExecutorService} instance associated with this {@code ConcurrentImageCanvas} instance
	 */
	public ExecutorService getExecutorService() {
		return this.executorService;
	}
	
	/**
	 * Returns the {@link Image} instance that is associated with this {@code ConcurrentImageCanvas} instance.
	 * 
	 * @return the {@code Image} instance that is associated with this {@code ConcurrentImageCanvas} instance
	 */
	public T getImage() {
		return this.image.get();
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
	 * Performs the rendering operation.
	 * <p>
	 * This method should be called each frame or render pass. Preferably in an {@code AnimationTimer} instance.
	 * <p>
	 * If the {@code ExecutorService} has been shutdown, nothing will happen. Otherwise, this method will create and execute a new {@link PredicateTask} using the {@code ExecutorService} if, and only if, there are no previous {@code PredicateTask} or
	 * the previous {@code PredicateTask} is cancelled or done.
	 */
	public void render() {
		final ExecutorService executorService = this.executorService;
		
		if(!executorService.isShutdown()) {
			final AtomicReference<PredicateTask> predicateTask = this.predicateTask;
			
			final PredicateTask oldPredicateTask = predicateTask.get();
			
			if(oldPredicateTask == null || oldPredicateTask.isCancelled() || (oldPredicateTask.isDone() && oldPredicateTask.hasFinishedSucceeded())) {
				final ImageUpdater<T> imageUpdater = this.imageUpdater.getAndSet(null);
				
				if(imageUpdater != null) {
					final T image = imageUpdater.update(getImage());
					
					doSetImage(image);
					
					setHeight(image.getResolutionY());
					setWidth(image.getResolutionX());
				}
				
				final ByteBuffer byteBuffer = this.byteBuffer.get();
				
				final T image = getImage();
				
				final double resolutionX = image.getResolutionX();
				final double resolutionY = image.getResolutionY();
				
				final Predicate<T> renderPredicate = this.renderPredicate;
				
				final WritableImage writableImage = this.writableImage.get();
				
				final PredicateTask newPredicateTask = new PredicateTask(() -> Boolean.valueOf(renderPredicate.test(image)), () -> {
					image.copyTo(byteBuffer.array(), ArrayComponentOrder.BGRA);
					
					final
					PixelWriter pixelWriter = writableImage.getPixelWriter();
					pixelWriter.setPixels(0, 0, image.getResolutionX(), image.getResolutionY(), PIXEL_FORMAT, byteBuffer, image.getResolutionX() * 4);
					
					final
					GraphicsContext graphicsContext = getGraphicsContext2D();
					graphicsContext.drawImage(writableImage, 0.0D, 0.0D, writableImage.getWidth(), writableImage.getHeight(), 0.0D, 0.0D, resolutionX, resolutionY);
				});
				
				predicateTask.set(newPredicateTask);
				
				try {
					executorService.execute(newPredicateTask);
				} catch(final RejectedExecutionException e) {
//					One of the methods shutdown() and shutdownNow() of the ExecutorService has been called.
//					The next time this render() method is called, nothing will happen.
				}
			}
		}
	}
	
	/**
	 * Sets the {@link ImageUpdater} instance that will update the {@link Image} instance in the next {@link #render()} call.
	 * <p>
	 * If {@code imageUpdater} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param imageUpdater the {@code ImageUpdater} instance that will update the {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code imageUpdater} is {@code null}
	 */
	public void setImageUpdater(final ImageUpdater<T> imageUpdater) {
		this.imageUpdater.set(Objects.requireNonNull(imageUpdater, "imageUpdater == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * An {@code ImageUpdater} is used to update the {@link Image} in a {@link ConcurrentImageCanvas} instance.
	 * 
	 * @since 1.0.0
	 * @author J&#246;rgen Lundgren
	 */
	public static final class ImageUpdater<T extends Image> {
		private final TriFunction<T, Number, Number, T> imageConstructionFunction;
		private final int imageResolutionX;
		private final int imageResolutionY;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Constructs a new {@code ImageUpdater} instance.
		 * <p>
		 * If {@code imageConstructionFunction} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param imageConstructionFunction a {@code TriFunction} that constructs a new {@link Image} instance given the old {@code Image} instance and the new resolution of the X- and Y-axes
		 * @param imageResolutionX the resolution of the X-axis or a negative value to use the current resolution
		 * @param imageResolutionY the resolution of the Y-axis or a negative value to use the current resolution
		 * @throws NullPointerException thrown if, and only if, {@code imageConstructionFunction} is {@code null}
		 */
		public ImageUpdater(final TriFunction<T, Number, Number, T> imageConstructionFunction, final int imageResolutionX, final int imageResolutionY) {
			this.imageConstructionFunction = Objects.requireNonNull(imageConstructionFunction, "imageConstructionFunction == null");
			this.imageResolutionX = imageResolutionX;
			this.imageResolutionY = imageResolutionY;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Returns the updated {@link Image} instance.
		 * <p>
		 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param image the current {@code Image} instance
		 * @return the updated {@code Image} instance
		 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
		 */
		public T update(final T image) {
			final int imageResolutionX = this.imageResolutionX >= 0 ? this.imageResolutionX : image.getResolutionX();
			final int imageResolutionY = this.imageResolutionY >= 0 ? this.imageResolutionY : image.getResolutionY();
			
			return this.imageConstructionFunction.apply(image, Integer.valueOf(imageResolutionX), Integer.valueOf(imageResolutionY));
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * An {@code Observer} is an observer of a {@code ConcurrentImageCanvas} instance.
	 * 
	 * @since 1.0.0
	 * @author J&#246;rgen Lundgren
	 */
	public static interface Observer {
		/**
		 * This method is called by a {@code ConcurrentImageCanvas} instance when the mouse is dragged.
		 * 
		 * @param concurrentImageCanvas the {@code ConcurrentImageCanvas} instance that called this method
		 * @param x the dragged amount on the X-axis
		 * @param y the dragged amount on the Y-axis
		 */
		void onMouseDragged(final ConcurrentImageCanvas<? extends Image> concurrentImageCanvas, final float x, final float y);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
		this.mouseDraggedX.set(this.mouseDraggedDeltaX.get() - (int)(mouseEvent.getScreenX()));
		this.mouseDraggedY.set(this.mouseDraggedDeltaY.get() - (int)(mouseEvent.getScreenY()));
		this.mouseDraggedDeltaX.set((int)(mouseEvent.getScreenX()));
		this.mouseDraggedDeltaY.set((int)(mouseEvent.getScreenY()));
		this.mouseMovedDeltaX.set(0);
		this.mouseMovedDeltaY.set(0);
		this.mouseMovedX.set(0);
		this.mouseMovedY.set(0);
		this.mouseX.set(Double.doubleToLongBits(mouseEvent.getX()));
		this.mouseY.set(Double.doubleToLongBits(mouseEvent.getY()));
		this.observer.onMouseDragged(this, this.mouseDraggedX.getAndSet(0), this.mouseDraggedY.getAndSet(0));
	}
	
	private void doOnMouseMoved(final MouseEvent mouseEvent) {
		this.mouseDraggedX.set(0);
		this.mouseDraggedY.set(0);
		this.mouseX.set(Double.doubleToLongBits(mouseEvent.getX()));
		this.mouseY.set(Double.doubleToLongBits(mouseEvent.getY()));
	}
	
	private void doOnMousePressed(final MouseEvent mouseEvent) {
		if(!this.isMouseButtonPressed[mouseEvent.getButton().ordinal()]) {
			this.mouseButtonsPressed.incrementAndGet();
		}
		
		this.isMouseButtonPressed[mouseEvent.getButton().ordinal()] = true;
		
		this.mouseDraggedDeltaX.set((int)(mouseEvent.getScreenX()));
		this.mouseDraggedDeltaY.set((int)(mouseEvent.getScreenY()));
	}
	
	private void doOnMouseReleased(final MouseEvent mouseEvent) {
		if(this.isMouseButtonPressed[mouseEvent.getButton().ordinal()]) {
			this.mouseButtonsPressed.decrementAndGet();
		}
		
		this.isMouseButtonPressed[mouseEvent.getButton().ordinal()] = false;
		this.isMouseButtonPressedOnce[mouseEvent.getButton().ordinal()] = false;
	}
	
	private void doSetImage(final T image) {
		this.byteBuffer.set(ByteBuffer.allocate(image.getResolutionX() * image.getResolutionY() * 4));
		this.image.set(image);
		this.writableImage.set(new WritableImage(image.getResolutionX(), image.getResolutionY()));
	}
}