/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.image.IntImageF;
import org.dayflower.javafx.scene.control.TextFields;
import org.dayflower.scene.texture.BullseyeTexture;
import org.dayflower.scene.texture.CheckerboardTexture;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.DotProductTexture;
import org.dayflower.scene.texture.LDRImageTexture;
import org.dayflower.scene.texture.MarbleTexture;
import org.dayflower.scene.texture.PolkaDotTexture;
import org.dayflower.scene.texture.SimplexFractionalBrownianMotionTexture;
import org.dayflower.scene.texture.SurfaceNormalTexture;
import org.dayflower.scene.texture.Texture;
import org.dayflower.scene.texture.UVTexture;

import org.macroing.art4j.color.Color3F;
import org.macroing.java.lang.Doubles;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

final class TextureDialog extends Dialog<Texture> {
	private static final String NAME_BULLSEYE_TEXTURE = "BullseyeTexture";
	private static final String NAME_CHECKERBOARD_TEXTURE = "CheckerboardTexture";
	private static final String NAME_CONSTANT_TEXTURE = "ConstantTexture";
	private static final String NAME_DOT_PRODUCT_TEXTURE = "DotProductTexture";
	private static final String NAME_L_D_R_IMAGE_TEXTURE = "LDRImageTexture";
	private static final String NAME_MARBLE_TEXTURE = "MarbleTexture";
	private static final String NAME_POLKA_DOT_TEXTURE = "PolkaDotTexture";
	private static final String NAME_SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE = "SimplexFractionalBrownianMotionTexture";
	private static final String NAME_SURFACE_NORMAL_TEXTURE = "SurfaceNormalTexture";
	private static final String NAME_U_V_TEXTURE = "UVTexture";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public TextureDialog(final Stage stage, final Consumer<Texture> consumer) {
		Objects.requireNonNull(stage, "stage == null");
		
		final AtomicReference<Class<? extends Texture>> clazz = new AtomicReference<>(ConstantTexture.class);
		
		final
		DialogPane dialogPane = getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.NEXT, ButtonType.CANCEL);
		dialogPane.setContent(doCreateGridPane((options, oldValue, newValue) -> {
			clazz.set(newValue.getClazz());
		}));
		
		final
		Button button = Button.class.cast(dialogPane.lookupButton(ButtonType.NEXT));
		button.setOnAction(e -> {
			show();
			
			final GridPane gridPane = doCreateGridPane(clazz.get());
			
			dialogPane.getButtonTypes().clear();
			dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
			dialogPane.setContent(gridPane);
			dialogPane.getScene().getWindow().sizeToScene();
			
			setResultConverter(buttonType -> buttonType.getButtonData() == ButtonType.OK.getButtonData() ? doCreateTexture(gridPane, clazz.get()) : null);
			setTitle(doCreateTitle(clazz.get()));
		});
		
		initOwner(stage);
		
		setResultConverter(buttonType -> buttonType.getButtonData() == ButtonType.OK.getButtonData() ? new ConstantTexture() : null);
		setTitle("New Texture");
		
		resultProperty().addListener((observableList, oldValue, newValue) -> {
			if(newValue != null) {
				consumer.accept(Texture.class.cast(newValue));
			}
		});
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static GridPane doCreateGridPane(final ChangeListener<? super TextureInfo> changeListener) {
		final
		ComboBox<TextureInfo> comboBox = new ComboBox<>();
		comboBox.getItems().addAll(new TextureInfo(BullseyeTexture.class), new TextureInfo(CheckerboardTexture.class), new TextureInfo(ConstantTexture.class), new TextureInfo(DotProductTexture.class), new TextureInfo(LDRImageTexture.class), new TextureInfo(MarbleTexture.class), new TextureInfo(PolkaDotTexture.class), new TextureInfo(SimplexFractionalBrownianMotionTexture.class), new TextureInfo(SurfaceNormalTexture.class), new TextureInfo(UVTexture.class));
		comboBox.setMaxWidth(Doubles.MAX_VALUE);
		comboBox.setValue(new TextureInfo(ConstantTexture.class));
		comboBox.getSelectionModel().selectedItemProperty().addListener(changeListener);
		
		final
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10.0D);
		gridPane.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		gridPane.setVgap(10.0D);
		gridPane.add(new Text("Texture"), 0, 0);
		gridPane.add(comboBox, 1, 0);
		
		return gridPane;
	}
	
	private static GridPane doCreateGridPane(final Class<? extends Texture> clazz) {
		final
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10.0D);
		gridPane.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		gridPane.setVgap(10.0D);
		
		switch(clazz.getSimpleName()) {
			case NAME_BULLSEYE_TEXTURE:
				doConfigureTextureBullseyeTexture(gridPane);
				
				break;
			case NAME_CHECKERBOARD_TEXTURE:
				doConfigureTextureCheckerboardTexture(gridPane);
				
				break;
			case NAME_CONSTANT_TEXTURE:
				doConfigureTextureConstantTexture(gridPane);
				
				break;
			case NAME_DOT_PRODUCT_TEXTURE:
				break;
			case NAME_L_D_R_IMAGE_TEXTURE:
				doConfigureTextureLDRImageTexture(gridPane);
				
				break;
			case NAME_MARBLE_TEXTURE:
				doConfigureTextureMarbleTexture(gridPane);
				
				break;
			case NAME_POLKA_DOT_TEXTURE:
				doConfigureTexturePolkaDotTexture(gridPane);
				
				break;
			case NAME_SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE:
				doConfigureTextureSimplexFractionalBrownianMotionTexture(gridPane);
				
				break;
			case NAME_SURFACE_NORMAL_TEXTURE:
				break;
			case NAME_U_V_TEXTURE:
				break;
			default:
				break;
		}
		
		return gridPane;
	}
	
	private static String doCreateTitle(final Class<? extends Texture> clazz) {
		switch(clazz.getSimpleName()) {
			case NAME_BULLSEYE_TEXTURE:
				return "New Bullseye Texture";
			case NAME_CHECKERBOARD_TEXTURE:
				return "New Checkerboard Texture";
			case NAME_CONSTANT_TEXTURE:
				return "New Constant Texture";
			case NAME_DOT_PRODUCT_TEXTURE:
				return "New Dot Product Texture";
			case NAME_L_D_R_IMAGE_TEXTURE:
				return "New LDR Image Texture";
			case NAME_MARBLE_TEXTURE:
				return "New Marble Texture";
			case NAME_POLKA_DOT_TEXTURE:
				return "New Polka Dot Texture";
			case NAME_SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE:
				return "New Simplex Fractional Brownian Motion Texture";
			case NAME_SURFACE_NORMAL_TEXTURE:
				return "New Surface Normal Texture";
			case NAME_U_V_TEXTURE:
				return "New UV Texture";
			default:
				return "New Texture";
		}
	}
	
	private static Texture doCreateTexture(final GridPane gridPane, final Class<? extends Texture> clazz) {
		switch(clazz.getSimpleName()) {
			case NAME_BULLSEYE_TEXTURE:
				return doCreateTextureBullseyeTexture(gridPane);
			case NAME_CHECKERBOARD_TEXTURE:
				return doCreateTextureCheckerboardTexture(gridPane);
			case NAME_CONSTANT_TEXTURE:
				return doCreateTextureConstantTexture(gridPane);
			case NAME_DOT_PRODUCT_TEXTURE:
				return doCreateTextureDotProductTexture();
			case NAME_L_D_R_IMAGE_TEXTURE:
				return doCreateTextureLDRImageTexture(gridPane);
			case NAME_MARBLE_TEXTURE:
				return doCreateTextureMarbleTexture(gridPane);
			case NAME_POLKA_DOT_TEXTURE:
				return doCreateTexturePolkaDotTexture(gridPane);
			case NAME_SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE:
				return doCreateTextureSimplexFractionalBrownianMotionTexture(gridPane);
			case NAME_SURFACE_NORMAL_TEXTURE:
				return doCreateTextureSurfaceNormalTexture();
			case NAME_U_V_TEXTURE:
				return doCreateTextureUVTexture();
			default:
				return doCreateTextureDefault();
		}
	}
	
	private static Texture doCreateTextureBullseyeTexture(final GridPane gridPane) {
		final Node nodeColorPickerColorA = gridPane.getChildren().get(1);
		final Node nodeColorPickerColorB = gridPane.getChildren().get(3);
		final Node nodeTextFieldOriginX = gridPane.getChildren().get(5);
		final Node nodeTextFieldOriginY = gridPane.getChildren().get(7);
		final Node nodeTextFieldOriginZ = gridPane.getChildren().get(9);
		final Node nodeTextFieldScale = gridPane.getChildren().get(11);
		
		if(nodeColorPickerColorA instanceof ColorPicker && nodeColorPickerColorB instanceof ColorPicker && nodeTextFieldOriginX instanceof TextField && nodeTextFieldOriginY instanceof TextField && nodeTextFieldOriginZ instanceof TextField && nodeTextFieldScale instanceof TextField) {
			final ColorPicker colorPickerColorA = ColorPicker.class.cast(nodeColorPickerColorA);
			final ColorPicker colorPickerColorB = ColorPicker.class.cast(nodeColorPickerColorB);
			
			final TextField textFieldOriginX = TextField.class.cast(nodeTextFieldOriginX);
			final TextField textFieldOriginY = TextField.class.cast(nodeTextFieldOriginY);
			final TextField textFieldOriginZ = TextField.class.cast(nodeTextFieldOriginZ);
			
			final TextField textFieldScale = TextField.class.cast(nodeTextFieldScale);
			
			final Color colorA = colorPickerColorA.getValue();
			final Color colorB = colorPickerColorB.getValue();
			
			final float colorAR = (float)(colorA.getRed());
			final float colorAG = (float)(colorA.getGreen());
			final float colorAB = (float)(colorA.getBlue());
			
			final float colorBR = (float)(colorB.getRed());
			final float colorBG = (float)(colorB.getGreen());
			final float colorBB = (float)(colorB.getBlue());
			
			final float originX = Float.parseFloat(textFieldOriginX.getText());
			final float originY = Float.parseFloat(textFieldOriginY.getText());
			final float originZ = Float.parseFloat(textFieldOriginZ.getText());
			
			final float scale = Float.parseFloat(textFieldScale.getText());
			
			return new BullseyeTexture(new Color3F(colorAR, colorAG, colorAB), new Color3F(colorBR, colorBG, colorBB), new Point3F(originX, originY, originZ), scale);
		}
		
		return new BullseyeTexture();
	}
	
	private static Texture doCreateTextureCheckerboardTexture(final GridPane gridPane) {
		final Node nodeColorPickerColorA = gridPane.getChildren().get(1);
		final Node nodeColorPickerColorB = gridPane.getChildren().get(3);
		final Node nodeTextFieldAngle = gridPane.getChildren().get(5);
		final Node nodeTextFieldScaleU = gridPane.getChildren().get(7);
		final Node nodeTextFieldScaleV = gridPane.getChildren().get(9);
		
		if(nodeColorPickerColorA instanceof ColorPicker && nodeColorPickerColorB instanceof ColorPicker && nodeTextFieldAngle instanceof TextField && nodeTextFieldScaleU instanceof TextField && nodeTextFieldScaleV instanceof TextField) {
			final ColorPicker colorPickerColorA = ColorPicker.class.cast(nodeColorPickerColorA);
			final ColorPicker colorPickerColorB = ColorPicker.class.cast(nodeColorPickerColorB);
			
			final TextField textFieldAngle = TextField.class.cast(nodeTextFieldAngle);
			final TextField textFieldScaleU = TextField.class.cast(nodeTextFieldScaleU);
			final TextField textFieldScaleV = TextField.class.cast(nodeTextFieldScaleV);
			
			final Color colorA = colorPickerColorA.getValue();
			final Color colorB = colorPickerColorB.getValue();
			
			final float colorAR = (float)(colorA.getRed());
			final float colorAG = (float)(colorA.getGreen());
			final float colorAB = (float)(colorA.getBlue());
			
			final float colorBR = (float)(colorB.getRed());
			final float colorBG = (float)(colorB.getGreen());
			final float colorBB = (float)(colorB.getBlue());
			
			final AngleF angle = AngleF.degrees(Float.parseFloat(textFieldAngle.getText()));
			
			final Vector2F scale = new Vector2F(Float.parseFloat(textFieldScaleU.getText()), Float.parseFloat(textFieldScaleV.getText()));
			
			return new CheckerboardTexture(new Color3F(colorAR, colorAG, colorAB), new Color3F(colorBR, colorBG, colorBB), angle, scale);
		}
		
		return new CheckerboardTexture();
	}
	
	private static Texture doCreateTextureConstantTexture(final GridPane gridPane) {
		final Node node = gridPane.getChildren().get(1);
		
		if(node instanceof ColorPicker) {
			final ColorPicker colorPicker = ColorPicker.class.cast(node);
			
			final Color color = colorPicker.getValue();
			
			final float r = (float)(color.getRed());
			final float g = (float)(color.getGreen());
			final float b = (float)(color.getBlue());
			
			return new ConstantTexture(new Color3F(r, g, b));
		}
		
		return new ConstantTexture();
	}
	
	private static Texture doCreateTextureDefault() {
		return new ConstantTexture();
	}
	
	private static Texture doCreateTextureDotProductTexture() {
		return new DotProductTexture();
	}
	
	@SuppressWarnings("unused")
	private static Texture doCreateTextureLDRImageTexture(final GridPane gridPane) {
		final Node nodeTextFieldFilename = gridPane.getChildren().get(1);
		final Node nodeTextFieldAngle = gridPane.getChildren().get(3);
		final Node nodeTextFieldScaleU = gridPane.getChildren().get(5);
		final Node nodeTextFieldScaleV = gridPane.getChildren().get(7);
		
		if(nodeTextFieldFilename instanceof TextField && nodeTextFieldAngle instanceof TextField && nodeTextFieldScaleU instanceof TextField && nodeTextFieldScaleV instanceof TextField) {
			final TextField textFieldFilename = TextField.class.cast(nodeTextFieldFilename);
			final TextField textFieldAngle = TextField.class.cast(nodeTextFieldAngle);
			final TextField textFieldScaleU = TextField.class.cast(nodeTextFieldScaleU);
			final TextField textFieldScaleV = TextField.class.cast(nodeTextFieldScaleV);
			
			final String filename = textFieldFilename.getText();
			
			final File file = new File(filename);
			
			if(file.isFile()) {
				final AngleF angle = AngleF.degrees(Float.parseFloat(textFieldAngle.getText()));
				
				final Vector2F scale = new Vector2F(Float.parseFloat(textFieldScaleU.getText()), Float.parseFloat(textFieldScaleV.getText()));
				
				try {
					return new LDRImageTexture(IntImageF.load(file), angle, scale);
				} catch(final UncheckedIOException e) {
					return null;
				}
			}
		}
		
		return null;
	}
	
	private static Texture doCreateTextureMarbleTexture(final GridPane gridPane) {
		final Node nodeColorPickerColorA = gridPane.getChildren().get(1);
		final Node nodeColorPickerColorB = gridPane.getChildren().get(3);
		final Node nodeColorPickerColorC = gridPane.getChildren().get(5);
		final Node nodeTextFieldScale = gridPane.getChildren().get(7);
		final Node nodeTextFieldStripes = gridPane.getChildren().get(9);
		final Node nodeTextFieldOctaves = gridPane.getChildren().get(11);
		
		if(nodeColorPickerColorA instanceof ColorPicker && nodeColorPickerColorB instanceof ColorPicker && nodeColorPickerColorC instanceof ColorPicker && nodeTextFieldScale instanceof TextField && nodeTextFieldStripes instanceof TextField && nodeTextFieldOctaves instanceof TextField) {
			final ColorPicker colorPickerColorA = ColorPicker.class.cast(nodeColorPickerColorA);
			final ColorPicker colorPickerColorB = ColorPicker.class.cast(nodeColorPickerColorB);
			final ColorPicker colorPickerColorC = ColorPicker.class.cast(nodeColorPickerColorC);
			
			final TextField textFieldScale = TextField.class.cast(nodeTextFieldScale);
			final TextField textFieldStripes = TextField.class.cast(nodeTextFieldStripes);
			final TextField textFieldOctaves = TextField.class.cast(nodeTextFieldOctaves);
			
			final Color colorA = colorPickerColorA.getValue();
			final Color colorB = colorPickerColorB.getValue();
			final Color colorC = colorPickerColorC.getValue();
			
			final float colorAR = (float)(colorA.getRed());
			final float colorAG = (float)(colorA.getGreen());
			final float colorAB = (float)(colorA.getBlue());
			
			final float colorBR = (float)(colorB.getRed());
			final float colorBG = (float)(colorB.getGreen());
			final float colorBB = (float)(colorB.getBlue());
			
			final float colorCR = (float)(colorC.getRed());
			final float colorCG = (float)(colorC.getGreen());
			final float colorCB = (float)(colorC.getBlue());
			
			final float scale = Float.parseFloat(textFieldScale.getText());
			final float stripes = Float.parseFloat(textFieldStripes.getText());
			
			final int octaves = Integer.parseInt(textFieldOctaves.getText());
			
			return new MarbleTexture(new Color3F(colorAR, colorAG, colorAB), new Color3F(colorBR, colorBG, colorBB), new Color3F(colorCR, colorCG, colorCB), scale, stripes, octaves);
		}
		
		return new MarbleTexture();
	}
	
	private static Texture doCreateTexturePolkaDotTexture(final GridPane gridPane) {
		final Node nodeColorPickerColorA = gridPane.getChildren().get(1);
		final Node nodeColorPickerColorB = gridPane.getChildren().get(3);
		final Node nodeTextFieldAngle = gridPane.getChildren().get(5);
		final Node nodeTextFieldCellResolution = gridPane.getChildren().get(7);
		final Node nodeTextFieldPolkaDotRadius = gridPane.getChildren().get(9);
		
		if(nodeColorPickerColorA instanceof ColorPicker && nodeColorPickerColorB instanceof ColorPicker && nodeTextFieldAngle instanceof TextField && nodeTextFieldCellResolution instanceof TextField && nodeTextFieldPolkaDotRadius instanceof TextField) {
			final ColorPicker colorPickerColorA = ColorPicker.class.cast(nodeColorPickerColorA);
			final ColorPicker colorPickerColorB = ColorPicker.class.cast(nodeColorPickerColorB);
			
			final TextField textFieldAngle = TextField.class.cast(nodeTextFieldAngle);
			final TextField textFieldCellResolution = TextField.class.cast(nodeTextFieldCellResolution);
			final TextField textFieldPolkaDotRadius = TextField.class.cast(nodeTextFieldPolkaDotRadius);
			
			final Color colorA = colorPickerColorA.getValue();
			final Color colorB = colorPickerColorB.getValue();
			
			final float colorAR = (float)(colorA.getRed());
			final float colorAG = (float)(colorA.getGreen());
			final float colorAB = (float)(colorA.getBlue());
			
			final float colorBR = (float)(colorB.getRed());
			final float colorBG = (float)(colorB.getGreen());
			final float colorBB = (float)(colorB.getBlue());
			
			final AngleF angle = AngleF.degrees(Float.parseFloat(textFieldAngle.getText()));
			
			final float cellResolution = Float.parseFloat(textFieldCellResolution.getText());
			final float polkaDotRadius = Float.parseFloat(textFieldPolkaDotRadius.getText());
			
			return new PolkaDotTexture(new Color3F(colorAR, colorAG, colorAB), new Color3F(colorBR, colorBG, colorBB), angle, cellResolution, polkaDotRadius);
		}
		
		return new PolkaDotTexture();
	}
	
	private static Texture doCreateTextureSimplexFractionalBrownianMotionTexture(final GridPane gridPane) {
		final Node nodeColorPicker = gridPane.getChildren().get(1);
		final Node nodeTextFieldFrequency = gridPane.getChildren().get(3);
		final Node nodeTextFieldGain = gridPane.getChildren().get(5);
		final Node nodeTextFieldOctaves = gridPane.getChildren().get(7);
		
		if(nodeColorPicker instanceof ColorPicker && nodeTextFieldFrequency instanceof TextField && nodeTextFieldGain instanceof TextField && nodeTextFieldOctaves instanceof TextField) {
			final ColorPicker colorPicker = ColorPicker.class.cast(nodeColorPicker);
			
			final TextField textFieldFrequency = TextField.class.cast(nodeTextFieldFrequency);
			final TextField textFieldGain = TextField.class.cast(nodeTextFieldGain);
			final TextField textFieldOctaves = TextField.class.cast(nodeTextFieldOctaves);
			
			final Color color = colorPicker.getValue();
			
			final float r = (float)(color.getRed());
			final float g = (float)(color.getGreen());
			final float b = (float)(color.getBlue());
			
			final float frequency = Float.parseFloat(textFieldFrequency.getText());
			final float gain = Float.parseFloat(textFieldGain.getText());
			
			final int octaves = Integer.parseInt(textFieldOctaves.getText());
			
			return new SimplexFractionalBrownianMotionTexture(new Color3F(r, g, b), frequency, gain, octaves);
		}
		
		return new SimplexFractionalBrownianMotionTexture();
	}
	
	private static Texture doCreateTextureSurfaceNormalTexture() {
		return new SurfaceNormalTexture();
	}
	
	private static Texture doCreateTextureUVTexture() {
		return new UVTexture();
	}
	
	private static void doConfigureTextureBullseyeTexture(final GridPane gridPane) {
		final ColorPicker colorPickerColorA = new ColorPicker(Color.rgb(128, 128, 128));
		final ColorPicker colorPickerColorB = new ColorPicker(Color.rgb(255, 255, 255));
		
		final TextField textFieldOriginX = TextFields.createTextField( 0.0F);
		final TextField textFieldOriginY = TextFields.createTextField(10.0F);
		final TextField textFieldOriginZ = TextFields.createTextField( 0.0F);
		
		final TextField textFieldScale = TextFields.createTextField(1.0F);
		
		gridPane.add(new Text("Color A"), 0, 0);
		gridPane.add(colorPickerColorA, 1, 0);
		gridPane.add(new Text("Color B"), 0, 1);
		gridPane.add(colorPickerColorB, 1, 1);
		gridPane.add(new Text("Origin X"), 0, 2);
		gridPane.add(textFieldOriginX, 1, 2);
		gridPane.add(new Text("Origin Y"), 0, 3);
		gridPane.add(textFieldOriginY, 1, 3);
		gridPane.add(new Text("Origin Z"), 0, 4);
		gridPane.add(textFieldOriginZ, 1, 4);
		gridPane.add(new Text("Scale"), 0, 5);
		gridPane.add(textFieldScale, 1, 5);
	}
	
	private static void doConfigureTextureCheckerboardTexture(final GridPane gridPane) {
		final ColorPicker colorPickerColorA = new ColorPicker(Color.rgb(128, 128, 128));
		final ColorPicker colorPickerColorB = new ColorPicker(Color.rgb(255, 255, 255));
		
		final TextField textFieldAngle = TextFields.createTextField(0.0F);
		final TextField textFieldScaleU = TextFields.createTextField(1.0F);
		final TextField textFieldScaleV = TextFields.createTextField(1.0F);
		
		gridPane.add(new Text("Color A"), 0, 0);
		gridPane.add(colorPickerColorA, 1, 0);
		gridPane.add(new Text("Color B"), 0, 1);
		gridPane.add(colorPickerColorB, 1, 1);
		gridPane.add(new Text("Angle"), 0, 2);
		gridPane.add(textFieldAngle, 1, 2);
		gridPane.add(new Text("Scale U"), 0, 3);
		gridPane.add(textFieldScaleU, 1, 3);
		gridPane.add(new Text("Scale V"), 0, 4);
		gridPane.add(textFieldScaleV, 1, 4);
	}
	
	private static void doConfigureTextureConstantTexture(final GridPane gridPane) {
		final ColorPicker colorPicker = new ColorPicker();
		
		gridPane.add(new Text("Color"), 0, 0);
		gridPane.add(colorPicker, 1, 0);
	}
	
	private static void doConfigureTextureLDRImageTexture(final GridPane gridPane) {
		final TextField textFieldFilename = new TextField();
		final TextField textFieldAngle = TextFields.createTextField(0.0F);
		final TextField textFieldScaleU = TextFields.createTextField(1.0F);
		final TextField textFieldScaleV = TextFields.createTextField(1.0F);
		
		gridPane.add(new Text("Filename"), 0, 0);
		gridPane.add(textFieldFilename, 1, 0);
		gridPane.add(new Text("Angle"), 0, 1);
		gridPane.add(textFieldAngle, 1, 1);
		gridPane.add(new Text("Scale U"), 0, 2);
		gridPane.add(textFieldScaleU, 1, 2);
		gridPane.add(new Text("Scale V"), 0, 3);
		gridPane.add(textFieldScaleV, 1, 3);
	}
	
	private static void doConfigureTextureMarbleTexture(final GridPane gridPane) {
		final ColorPicker colorPickerColorA = new ColorPicker(Color.rgb(204, 204, 204));
		final ColorPicker colorPickerColorB = new ColorPicker(Color.rgb(102,  51,  26));
		final ColorPicker colorPickerColorC = new ColorPicker(Color.rgb( 15,  10,   5));
		
		final TextField textFieldScale = TextFields.createTextField(5.0F);
		final TextField textFieldStripes = TextFields.createTextField(0.15F);
		final TextField textFieldOctaves = TextFields.createTextField(8);
		
		gridPane.add(new Text("Color A"), 0, 0);
		gridPane.add(colorPickerColorA, 1, 0);
		gridPane.add(new Text("Color B"), 0, 1);
		gridPane.add(colorPickerColorB, 1, 1);
		gridPane.add(new Text("Color C"), 0, 2);
		gridPane.add(colorPickerColorC, 1, 2);
		gridPane.add(new Text("Scale"), 0, 3);
		gridPane.add(textFieldScale, 1, 3);
		gridPane.add(new Text("Stripes"), 0, 4);
		gridPane.add(textFieldStripes, 1, 4);
		gridPane.add(new Text("Octaves"), 0, 5);
		gridPane.add(textFieldOctaves, 1, 5);
	}
	
	private static void doConfigureTexturePolkaDotTexture(final GridPane gridPane) {
		final ColorPicker colorPickerColorA = new ColorPicker(Color.rgb(128, 128, 128));
		final ColorPicker colorPickerColorB = new ColorPicker(Color.rgb(255, 255, 255));
		
		final TextField textFieldAngle = TextFields.createTextField(0.0F);
		final TextField textFieldCellResolution = TextFields.createTextField(10.0F);
		final TextField textFieldPolkaDotRadius = TextFields.createTextField(0.25F);
		
		gridPane.add(new Text("Color A"), 0, 0);
		gridPane.add(colorPickerColorA, 1, 0);
		gridPane.add(new Text("Color B"), 0, 1);
		gridPane.add(colorPickerColorB, 1, 1);
		gridPane.add(new Text("Angle"), 0, 2);
		gridPane.add(textFieldAngle, 1, 2);
		gridPane.add(new Text("Cell Resolution"), 0, 3);
		gridPane.add(textFieldCellResolution, 1, 3);
		gridPane.add(new Text("Polka Dot Radius"), 0, 4);
		gridPane.add(textFieldPolkaDotRadius, 1, 4);
	}
	
	private static void doConfigureTextureSimplexFractionalBrownianMotionTexture(final GridPane gridPane) {
		final ColorPicker colorPicker = new ColorPicker(Color.rgb(191, 128, 191));
		
		final TextField textFieldFrequency = TextFields.createTextField(5.0F);
		final TextField textFieldGain = TextFields.createTextField(0.5F);
		final TextField textFieldOctaves = TextFields.createTextField(16);
		
		gridPane.add(new Text("Color"), 0, 0);
		gridPane.add(colorPicker, 1, 0);
		gridPane.add(new Text("Frequency"), 0, 1);
		gridPane.add(textFieldFrequency, 1, 1);
		gridPane.add(new Text("Gain"), 0, 2);
		gridPane.add(textFieldGain, 1, 2);
		gridPane.add(new Text("Octaves"), 0, 3);
		gridPane.add(textFieldOctaves, 1, 3);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class TextureInfo {
		private final Class<? extends Texture> clazz;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public TextureInfo(final Class<? extends Texture> clazz) {
			this.clazz = Objects.requireNonNull(clazz, "clazz == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Class<? extends Texture> getClazz() {
			return this.clazz;
		}
		
		@Override
		public String toString() {
			switch(this.clazz.getSimpleName()) {
			case NAME_BULLSEYE_TEXTURE:
				return "New Bullseye Texture";
			case NAME_CHECKERBOARD_TEXTURE:
				return "New Checkerboard Texture";
			case NAME_CONSTANT_TEXTURE:
				return "New Constant Texture";
			case NAME_DOT_PRODUCT_TEXTURE:
				return "New Dot Product Texture";
			case NAME_L_D_R_IMAGE_TEXTURE:
				return "New LDR Image Texture";
			case NAME_MARBLE_TEXTURE:
				return "New Marble Texture";
			case NAME_POLKA_DOT_TEXTURE:
				return "New Polka Dot Texture";
			case NAME_SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE:
				return "New Simplex Fractional Brownian Motion Texture";
			case NAME_SURFACE_NORMAL_TEXTURE:
				return "New Surface Normal Texture";
			case NAME_U_V_TEXTURE:
				return "New UV Texture";
			default:
				return "New Texture";
		}
		}
		
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof TextureInfo)) {
				return false;
			} else if(!Objects.equals(this.clazz, TextureInfo.class.cast(object).clazz)) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(this.clazz);
		}
	}
}