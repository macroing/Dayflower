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
package org.dayflower.test;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.concurrent.atomic.AtomicLong;

import org.dayflower.display.Display;
import org.dayflower.image.Image;
import org.dayflower.javafx.AbstractCanvasApplication;
import org.dayflower.renderer.PathTracer;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RendererConfiguration;
import org.dayflower.scene.Camera;
import org.dayflower.util.Timer;

/**
 * This {@code DayflowerApplication} class is used to launch Dayflower.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DayflowerApplication extends AbstractCanvasApplication implements Display {
	private static final int RESOLUTION_X = 800;
	private static final int RESOLUTION_Y = 800;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AtomicLong maximumRenderTime;
	private final AtomicLong minimumRenderTime;
	private final Label labelRenderPass;
	private final Label labelRenderTime;
	private final Label labelRenderTimePerPass;
	private final Renderer renderer;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DayflowerApplication} instance.
	 */
	public DayflowerApplication() {
		super("Dayflower", RESOLUTION_X, RESOLUTION_Y);
		
		this.maximumRenderTime = new AtomicLong(Long.MIN_VALUE);
		this.minimumRenderTime = new AtomicLong(Long.MAX_VALUE);
		this.labelRenderPass = new Label("Render Pass: 0");
		this.labelRenderTime = new Label("Render Time: 00:00:00");
		this.labelRenderTimePerPass = new Label("Render Time Per Pass: 0");
		this.renderer = new PathTracer(PathTracer.TYPE_P_B_R_T);
		this.renderer.setDisplay(this);
		this.renderer.setRendererConfiguration(new RendererConfiguration(20, 5, 1, 1, 1));
		this.renderer.setScene(Scenes.newPBRTScene());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Updates this {@code DayflowerApplication} instance with {@code image}.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param image the {@link Image} instance to display
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	@Override
	public void update(final Image image) {
//		Do nothing!
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The entry-point for this {@code DayflowerApplication} class.
	 * 
	 * @param args the command line arguments supplied
	 */
	public static void main(final String[] args) {
		launch(args);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	@Override
	protected boolean render(final Image image) {
		this.renderer.setImage(image);
		this.renderer.render();
		
		return true;
	}
	
	/**
	 * This method is called to initialize the GUI.
	 * 
	 * @param borderPane the main {@code BorderPane}
	 */
	@Override
	protected void initializeGUI(final BorderPane borderPane) {
		final
		HBox hBox = new HBox();
		hBox.getChildren().add(this.labelRenderPass);
		hBox.getChildren().add(this.labelRenderTime);
		hBox.getChildren().add(this.labelRenderTimePerPass);
		hBox.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		hBox.setSpacing(20.0D);
		
		borderPane.setBottom(hBox);
	}
	
	/**
	 * This method is called every frame when it's time to update.
	 */
	@Override
	protected void update() {
		final Camera camera = this.renderer.getScene().getCamera();
		
		final Label labelRenderPass = this.labelRenderPass;
		final Label labelRenderTime = this.labelRenderTime;
		final Label labelRenderTimePerPass = this.labelRenderTimePerPass;
		
		final Timer timer = this.renderer.getTimer();
		
		final long currentRenderTime = this.renderer.getRenderTime();
		final long maximumRenderTime = currentRenderTime == 0L ? this.maximumRenderTime.get() : Math.max(this.maximumRenderTime.get(), currentRenderTime);
		final long minimumRenderTime = currentRenderTime == 0L ? this.minimumRenderTime.get() : Math.min(this.minimumRenderTime.get(), currentRenderTime);
		
		this.maximumRenderTime.set(maximumRenderTime);
		this.minimumRenderTime.set(minimumRenderTime);
		
		labelRenderPass.setText("Render Pass: " + this.renderer.getRenderPass());
		labelRenderTime.setText("Render Time: " + timer.getTime());
		labelRenderTimePerPass.setText("Render Time Per Pass: " + (minimumRenderTime == Long.MAX_VALUE ? "?" : Long.toString(minimumRenderTime)) + " / " + (currentRenderTime == 0L ? "?" : Long.toString(currentRenderTime)) + " / " + (maximumRenderTime == Long.MIN_VALUE ? "?" : Long.toString(maximumRenderTime)));
		
		synchronized(camera) {
			if(isKeyPressed(KeyCode.A, true)) {
				camera.moveLeft(1.0F);
				
				this.renderer.clear();
			}
			
			if(isKeyPressed(KeyCode.D, true)) {
				camera.moveRight(1.0F);
				
				this.renderer.clear();
			}
			
			if(isKeyPressed(KeyCode.S, true)) {
				camera.moveBackward(1.0F);
				
				this.renderer.clear();
			}
			
			if(isKeyPressed(KeyCode.W, true)) {
				camera.moveForward(1.0F);
				
				this.renderer.clear();
			}
		}
	}
}