/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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
package org.dayflower.javafx.texture;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.scene.texture.BlendTexture;
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

import org.macroing.java.lang.Doubles;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Window;

/**
 * A {@code TextureWizard} is a wizard for {@link Texture} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class TextureWizard {
	private static final String NAME_BLEND_TEXTURE = "BlendTexture";
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
	
	private final Window owner;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code TextureWizard} instance.
	 * <p>
	 * If {@code owner} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param owner the owner
	 * @throws NullPointerException thrown if, and only if, {@code owner} is {@code null}
	 */
	public TextureWizard(final Window owner) {
		this.owner = Objects.requireNonNull(owner, "owner == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns an optional {@link Texture} instance.
	 * 
	 * @return an optional {@code Texture} instance
	 */
	public Optional<Texture> showAndWait() {
		final Dialog<TextureInfo> dialogTextureInfo = doCreateDialogTextureInfo();
		
		final Optional<TextureInfo> optionalTextureInfo = dialogTextureInfo.showAndWait();
		
		if(optionalTextureInfo.isPresent()) {
			final TextureInfo textureInfo = optionalTextureInfo.get();
			
			final Dialog<Texture> dialogTexture = doCreateDialogTexture(textureInfo);
			
			return dialogTexture.showAndWait();
		}
		
		return Optional.empty();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Dialog<Texture> doCreateDialogTexture(final TextureInfo textureInfo) {
		final TextureGridPane textureGridPane = textureInfo.createTextureGridPane();
		
		final
		Dialog<Texture> dialog = new Dialog<>();
		dialog.initOwner(this.owner);
		dialog.setResultConverter(buttonType -> buttonType.getButtonData() == ButtonType.OK.getButtonData() ? textureGridPane.createTexture() : null);
		dialog.setTitle(textureInfo.getTitle());
		
		final
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		dialogPane.setContent(textureGridPane);
		
		return dialog;
	}
	
	private Dialog<TextureInfo> doCreateDialogTextureInfo() {
		final AtomicReference<TextureInfo> textureInfo = new AtomicReference<>(new TextureInfo(ConstantTexture.class));
		
		final
		ComboBox<TextureInfo> comboBox = new ComboBox<>();
		comboBox.getItems().addAll(new TextureInfo(BlendTexture.class), new TextureInfo(BullseyeTexture.class), new TextureInfo(CheckerboardTexture.class), new TextureInfo(ConstantTexture.class), new TextureInfo(DotProductTexture.class), new TextureInfo(LDRImageTexture.class), new TextureInfo(MarbleTexture.class), new TextureInfo(PolkaDotTexture.class), new TextureInfo(SimplexFractionalBrownianMotionTexture.class), new TextureInfo(SurfaceNormalTexture.class), new TextureInfo(UVTexture.class));
		comboBox.setMaxWidth(Doubles.MAX_VALUE);
		comboBox.setValue(new TextureInfo(ConstantTexture.class));
		comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> textureInfo.set(newValue));
		
		final
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10.0D);
		gridPane.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		gridPane.setVgap(10.0D);
		gridPane.add(new Text("Texture"), 0, 0);
		gridPane.add(comboBox, 1, 0);
		
		final
		Dialog<TextureInfo> dialog = new Dialog<>();
		dialog.initOwner(this.owner);
		dialog.setResultConverter(buttonType -> buttonType.getButtonData() == ButtonType.NEXT.getButtonData() ? textureInfo.get() : null);
		dialog.setTitle("New Texture");
		
		final
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.NEXT, ButtonType.CANCEL);
		dialogPane.setContent(gridPane);
		
		return dialog;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class TextureInfo {
		private final Class<? extends Texture> clazz;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public TextureInfo(final Class<? extends Texture> clazz) {
			this.clazz = Objects.requireNonNull(clazz, "clazz == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public String getTitle() {
			switch(this.clazz.getSimpleName()) {
				case NAME_BLEND_TEXTURE:
					return "New Blend Texture";
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
		public String toString() {
			return getTitle();
		}
		
		public TextureGridPane createTextureGridPane() {
			switch(this.clazz.getSimpleName()) {
				case NAME_BLEND_TEXTURE:
					return new BlendTextureGridPane();
				case NAME_BULLSEYE_TEXTURE:
					return new BullseyeTextureGridPane();
				case NAME_CHECKERBOARD_TEXTURE:
					return new CheckerboardTextureGridPane();
				case NAME_CONSTANT_TEXTURE:
					return new ConstantTextureGridPane();
				case NAME_DOT_PRODUCT_TEXTURE:
					return new DotProductTextureGridPane();
				case NAME_L_D_R_IMAGE_TEXTURE:
					return new LDRImageTextureGridPane();
				case NAME_MARBLE_TEXTURE:
					return new MarbleTextureGridPane();
				case NAME_POLKA_DOT_TEXTURE:
					return new PolkaDotTextureGridPane();
				case NAME_SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE:
					return new SimplexFractionalBrownianMotionTextureGridPane();
				case NAME_SURFACE_NORMAL_TEXTURE:
					return new SurfaceNormalTextureGridPane();
				case NAME_U_V_TEXTURE:
					return new UVTextureGridPane();
				default:
					return new ConstantTextureGridPane();
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