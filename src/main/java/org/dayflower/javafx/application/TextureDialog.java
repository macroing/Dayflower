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

import java.util.Objects;

import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.DotProductTexture;
import org.dayflower.scene.texture.SimplexFractionalBrownianMotionTexture;
import org.dayflower.scene.texture.SurfaceNormalTexture;
import org.dayflower.scene.texture.Texture;
import org.dayflower.scene.texture.UVTexture;
import org.macroing.art4j.color.Color3F;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

final class TextureDialog extends Dialog<Texture> {
	private static final String NAME_CONSTANT_TEXTURE = "ConstantTexture";
	private static final String NAME_DOT_PRODUCT_TEXTURE = "DotProductTexture";
	private static final String NAME_SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE = "SimplexFractionalBrownianMotionTexture";
	private static final String NAME_SURFACE_NORMAL_TEXTURE = "SurfaceNormalTexture";
	private static final String NAME_U_V_TEXTURE = "UVTexture";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public TextureDialog(final Class<? extends Texture> clazz, final Stage stage) {
		Objects.requireNonNull(clazz, "clazz == null");
		Objects.requireNonNull(stage, "stage == null");
		
		final GridPane gridPane = doCreateGridPane(clazz);
		
		final
		DialogPane dialogPane = getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		dialogPane.setContent(gridPane);
		
		initOwner(stage);
		
		setResultConverter(buttonType -> buttonType.getButtonData() == ButtonType.OK.getButtonData() ? doCreateTexture(clazz, gridPane) : null);
		setTitle(doCreateTitle(clazz));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static GridPane doCreateGridPane(final Class<? extends Texture> clazz) {
		final
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10.0D);
		gridPane.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		gridPane.setVgap(10.0D);
		
		switch(clazz.getSimpleName()) {
			case NAME_CONSTANT_TEXTURE: {
				final ColorPicker colorPicker = new ColorPicker();
				
				gridPane.add(new Text("Color"), 0, 0);
				gridPane.add(colorPicker, 1, 0);
				
				break;
			}
			case NAME_DOT_PRODUCT_TEXTURE:
				break;
			case NAME_SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE: {
				final ColorPicker colorPicker = new ColorPicker();
				
				gridPane.add(new Text("Color"), 0, 0);
				gridPane.add(colorPicker, 1, 0);
				
				break;
			}
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
			case NAME_CONSTANT_TEXTURE:
				return "New Constant Texture";
			case NAME_DOT_PRODUCT_TEXTURE:
				return "New Dot Product Texture";
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
	
	private static Texture doCreateTexture(final Class<? extends Texture> clazz, final GridPane gridPane) {
		switch(clazz.getSimpleName()) {
			case NAME_CONSTANT_TEXTURE: {
				final Node node = gridPane.getChildren().get(1);
				
				if(node instanceof ColorPicker) {
					final ColorPicker colorPicker = ColorPicker.class.cast(node);
					
					final Color color = colorPicker.getValue();
					
					final float r = (float)(color.getRed());
					final float g = (float)(color.getGreen());
					final float b = (float)(color.getBlue());
					
					return new ConstantTexture(new Color3F(r, g, b));
				}
				
				return null;
			}
			case NAME_DOT_PRODUCT_TEXTURE:
				return new DotProductTexture();
			case NAME_SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE: {
				final Node node = gridPane.getChildren().get(1);
				
				if(node instanceof ColorPicker) {
					final ColorPicker colorPicker = ColorPicker.class.cast(node);
					
					final Color color = colorPicker.getValue();
					
					final float r = (float)(color.getRed());
					final float g = (float)(color.getGreen());
					final float b = (float)(color.getBlue());
					
					return new SimplexFractionalBrownianMotionTexture(new Color3F(r, g, b));
				}
				
				return null;
			}
			case NAME_SURFACE_NORMAL_TEXTURE:
				return new SurfaceNormalTexture();
			case NAME_U_V_TEXTURE:
				return new UVTexture();
			default:
				return null;
		}
	}
}