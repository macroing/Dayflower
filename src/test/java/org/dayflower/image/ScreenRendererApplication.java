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
package org.dayflower.image;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.util.Floats;

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

public final class ScreenRendererApplication extends Application {
	private final AtomicReference<Function<PixelImageF, PixelImageF>> function;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public ScreenRendererApplication() {
		this.function = new AtomicReference<>(pixelImage -> pixelImage);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void start(final Stage stage) {
		final AtomicReference<Function<PixelImageF, PixelImageF>> function = this.function;
		
		final
		ComboBox<String> comboBox = new ComboBox<>();
		comboBox.getItems().add("Blend Blue");
		comboBox.getItems().add("Blend Green");
		comboBox.getItems().add("Blend Red");
		comboBox.getItems().add("Box Blur");
		comboBox.getItems().add("Edge Detection");
		comboBox.getItems().add("Emboss");
		comboBox.getItems().add("Fractional Brownian Motion");
		comboBox.getItems().add("None");
		comboBox.getItems().add("Random");
		comboBox.getItems().add("Sharpen");
		comboBox.getItems().add("Threshold");
		comboBox.getSelectionModel().select("None");
		
		final Button button = doCreateButton("Apply", () -> {
			final String selectedItem = comboBox.getSelectionModel().getSelectedItem();
			
			if(selectedItem != null) {
				switch(selectedItem) {
					case "Blend Blue":
						final Color3F colorBlue = Color3F.randomComponent3();
						
						function.set(pixelImage -> {
							pixelImage.update(color -> Color3F.blend(color, colorBlue, 0.5F));
							
							return pixelImage;
						});
						
						break;
					case "Blend Green":
						final Color3F colorGreen = Color3F.randomComponent2();
						
						function.set(pixelImage -> {
							pixelImage.update(color -> Color3F.blend(color, colorGreen, 0.5F));
							
							return pixelImage;
						});
						
						break;
					case "Blend Red":
						final Color3F colorRed = Color3F.randomComponent1();
						
						function.set(pixelImage -> {
							pixelImage.update(color -> Color3F.blend(color, colorRed, 0.5F));
							
							return pixelImage;
						});
						
						break;
					case "Box Blur":
						function.set(pixelImage -> {
							pixelImage.multiply(ConvolutionKernel33F.BOX_BLUR);
							
							return pixelImage;
						});
						
						break;
					case "Edge Detection":
						function.set(pixelImage -> {
							pixelImage.multiply(ConvolutionKernel33F.EDGE_DETECTION);
							
							return pixelImage;
						});
						
						break;
					case "Emboss":
						function.set(pixelImage -> {
							pixelImage.multiply(ConvolutionKernel33F.EMBOSS);
							
							return pixelImage;
						});
						
						break;
					case "Fractional Brownian Motion":
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
					case "None":
						function.set(pixelImage -> pixelImage);
						
						break;
					case "Random":
						final ConvolutionKernel33F random = ConvolutionKernel33F.random();
						
						function.set(pixelImage -> {
							pixelImage.multiply(random);
							
							return pixelImage;
						});
						
						break;
					case "Sharpen":
						function.set(pixelImage -> {
							pixelImage.multiply(ConvolutionKernel33F.SHARPEN);
							
							return pixelImage;
						});
						
						break;
					case "Threshold":
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
		});
		
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
}