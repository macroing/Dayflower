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

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.image.ConvolutionKernel33F;
import org.dayflower.image.PixelImageF;
import org.dayflower.utility.Floats;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * This {@code ScreenRendererApplication} class displays the contents of the screen to the left of the window in a {@code Canvas} instance.
 * <p>
 * The contents shown in the {@code Canvas} instance can be processed using various image processing algorithms.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ScreenRendererApplication extends Application {
	private static final String OPERATION_BLEND_BLUE = "Blend Blue";
	private static final String OPERATION_BLEND_GREEN = "Blend Green";
	private static final String OPERATION_BLEND_RED = "Blend Red";
	private static final String OPERATION_BOX_BLUR = "Box Blur";
	private static final String OPERATION_EDGE_DETECTION = "Edge Detection";
	private static final String OPERATION_EMBOSS = "Emboss";
	private static final String OPERATION_FRACTIONAL_BROWNIAN_MOTION = "Fractional Brownian Motion";
	private static final String OPERATION_NONE = "None";
	private static final String OPERATION_RANDOM = "Random";
	private static final String OPERATION_SHARPEN = "Sharpen";
	private static final String OPERATION_THRESHOLD = "Threshold";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AtomicReference<Function<PixelImageF, PixelImageF>> function;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ScreenRendererApplication} instance.
	 */
	public ScreenRendererApplication() {
		this.function = new AtomicReference<>(pixelImage -> pixelImage);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Starts this {@code ScreenRendererApplication} instance.
	 * <p>
	 * If {@code stage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param stage the main {@code Stage}
	 * @throws NullPointerException thrown if, and only if, {@code stage} is {@code null}
	 */
	@Override
	public void start(final Stage stage) {
		final AtomicReference<Function<PixelImageF, PixelImageF>> function = this.function;
		
		final ComboBox<String> comboBox = doCreateComboBox();
		
		final Button button = doCreateButton("Apply", doCreateRunnable(function, comboBox));
		
		final
		HBox hBox = new HBox();
		hBox.getChildren().addAll(comboBox, button);
		hBox.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		hBox.setSpacing(10.0D);
		
		final Canvas canvas = new Canvas();
		
		final
		BorderPane borderPaneCanvas = new BorderPane();
		borderPaneCanvas.setCenter(canvas);
		
		final
		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(borderPaneCanvas);
		borderPane.setBottom(hBox);
		
		canvas.heightProperty().bind(borderPaneCanvas.heightProperty());
		canvas.widthProperty().bind(borderPaneCanvas.widthProperty());
		
		final Scene scene = new Scene(borderPane, 400.0D, 400.0D);
		
		stage.setAlwaysOnTop(true);
		stage.setScene(scene);
		stage.setTitle("Screen Renderer");
		stage.show();
		
		new AnimationTimer() {
			@Override
			public void handle(final long now) {
				final Bounds boundsInLocal = canvas.getBoundsInLocal();
				final Bounds boundsInScreen = canvas.localToScreen(boundsInLocal);
				
				final int x = (int)(boundsInScreen.getMinX());
				final int y = (int)(boundsInScreen.getMinY());
				final int width = (int)(boundsInScreen.getWidth());
				final int height = (int)(boundsInScreen.getHeight());
				
				final PixelImageF pixelImage = function.get().apply(PixelImageF.createScreenCapture(new Rectangle2I(new Point2I(x - width, y), new Point2I(x, y + height))));
				
				final WritableImage writableImage = pixelImage.toWritableImage();
				
				final
				GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
				graphicsContext.drawImage(writableImage, 0.0D, 0.0D);
			}
		}.start();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Starts this {@code ScreenRendererApplication} instance.
	 * 
	 * @param args the parameter arguments supplied
	 */
	public static void main(final String[] args) {
		launch(args);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Button doCreateButton(final String text, final Runnable runnable) {
		final
		Button button = new Button();
		button.setOnAction(actionEvent -> runnable.run());
		button.setText(text);
		
		return button;
	}
	
	private static ComboBox<String> doCreateComboBox() {
		final
		ComboBox<String> comboBox = new ComboBox<>();
		comboBox.getItems().add(OPERATION_BLEND_BLUE);
		comboBox.getItems().add(OPERATION_BLEND_GREEN);
		comboBox.getItems().add(OPERATION_BLEND_RED);
		comboBox.getItems().add(OPERATION_BOX_BLUR);
		comboBox.getItems().add(OPERATION_EDGE_DETECTION);
		comboBox.getItems().add(OPERATION_EMBOSS);
		comboBox.getItems().add(OPERATION_FRACTIONAL_BROWNIAN_MOTION);
		comboBox.getItems().add(OPERATION_NONE);
		comboBox.getItems().add(OPERATION_RANDOM);
		comboBox.getItems().add(OPERATION_SHARPEN);
		comboBox.getItems().add(OPERATION_THRESHOLD);
		comboBox.getSelectionModel().select(OPERATION_NONE);
		
		return comboBox;
	}
	
	private static Runnable doCreateRunnable(final AtomicReference<Function<PixelImageF, PixelImageF>> function, final ComboBox<String> comboBox) {
		return () -> {
			final String selectedItem = comboBox.getSelectionModel().getSelectedItem();
			
			if(selectedItem != null) {
				switch(selectedItem) {
					case OPERATION_BLEND_BLUE:
						doSetOperationBlend(function, Color3F.randomComponent3());
						
						break;
					case OPERATION_BLEND_GREEN:
						doSetOperationBlend(function, Color3F.randomComponent2());
						
						break;
					case OPERATION_BLEND_RED:
						doSetOperationBlend(function, Color3F.randomComponent1());
						
						break;
					case OPERATION_BOX_BLUR:
						doSetOperationConvolutionKernel(function, ConvolutionKernel33F.BOX_BLUR);
						
						break;
					case OPERATION_EDGE_DETECTION:
						doSetOperationConvolutionKernel(function, ConvolutionKernel33F.EDGE_DETECTION);
						
						break;
					case OPERATION_EMBOSS:
						doSetOperationConvolutionKernel(function, ConvolutionKernel33F.EMBOSS);
						
						break;
					case OPERATION_FRACTIONAL_BROWNIAN_MOTION:
						Color3F base = Color3F.random();
				        
						function.set(pixelImage -> {
							for(int index = 0; index < pixelImage.getResolution(); index++) {
								final float x = (index % pixelImage.getResolutionX()) / (float)(pixelImage.getResolutionX());
								final float y = (index / pixelImage.getResolutionX()) / (float)(pixelImage.getResolutionY());
								
								final float noise = Floats.simplexFractionalBrownianMotionXY(x, y, 5.0F, 0.5F, 0.0F, 1.0F, 8);
								
								final Color3F color = Color3F.redoGammaCorrectionSRGB(Color3F.maximumTo1(Color3F.minimumTo0(Color3F.multiply(base, noise))));
								
								pixelImage.setColorRGB(Color3F.blend(pixelImage.getColorRGB(index), color, 0.5F), index);
							}
							
							return pixelImage;
						});
						
						break;
					case OPERATION_NONE:
						function.set(pixelImage -> pixelImage);
						
						break;
					case OPERATION_RANDOM:
						doSetOperationConvolutionKernel(function, ConvolutionKernel33F.random());
						
						break;
					case OPERATION_SHARPEN:
						doSetOperationConvolutionKernel(function, ConvolutionKernel33F.SHARPEN);
						
						break;
					case OPERATION_THRESHOLD:
						function.set(pixelImage -> {
							pixelImage.update(color -> {
								if(color.isCyan()) {
									return Color3F.CYAN;
								} else if(color.isMagenta()) {
									return Color3F.MAGENTA;
								} else if(color.isYellow()) {
									return Color3F.YELLOW;
								} else if(color.isRed(0.2F, 0.2F)) {
									return Color3F.RED;
								} else if(color.isGreen(0.2F, 0.2F)) {
									return Color3F.GREEN;
								} else if(color.isBlue(0.2F, 0.2F)) {
									return Color3F.BLUE;
								} else {
									return Color3F.BLACK;
								}
							});
							
							return pixelImage;
						});
						
						break;
					default:
						break;
				}
			}
		};
	}
	
	private static void doSetOperationBlend(final AtomicReference<Function<PixelImageF, PixelImageF>> function, final Color3F colorNew) {
		function.set(pixelImage -> {
			pixelImage.update(colorOld -> Color3F.blend(colorOld, colorNew, 0.5F));
			
			return pixelImage;
		});
	}
	
	private static void doSetOperationConvolutionKernel(final AtomicReference<Function<PixelImageF, PixelImageF>> function, final ConvolutionKernel33F convolutionKernel) {
		function.set(pixelImage -> {
			pixelImage.multiply(convolutionKernel);
			
			return pixelImage;
		});
	}
}