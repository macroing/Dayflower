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
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.Texture;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * A {@code TexturePicker} is a control for picking a {@link Texture} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class TexturePicker extends BorderPane {
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
	
	private final AtomicReference<Texture> texture;
	private final Button button;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code TexturePicker} instance with {@code new ConstantTexture()} as the current {@link Texture} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new TexturePicker(new ConstantTexture());
	 * }
	 * </pre>
	 */
	public TexturePicker() {
		this(new ConstantTexture());
	}
	
	/**
	 * Constructs a new {@code TexturePicker} instance with {@code texture} as the current {@link Texture} instance.
	 * <p>
	 * If {@code texture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param texture the current {@code Texture} instance
	 * @throws NullPointerException thrown if, and only if, {@code texture} is {@code null}
	 */
	public TexturePicker(final Texture texture) {
		this.texture = new AtomicReference<>(Objects.requireNonNull(texture, "texture == null"));
		this.button = new Button(doGetName());
		this.button.setGraphic(new ImageView(WritableImageCaches.get(texture)));
		this.button.setMaxWidth(Double.MAX_VALUE);
		this.button.setOnAction(this::doOnAction);
		
		setCenter(this.button);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the current {@link Texture} instance.
	 * 
	 * @return the current {@code Texture} instance
	 */
	public Texture getTexture() {
		return this.texture.get();
	}
	
	/**
	 * Sets the current {@link Texture} instance to {@code texture}.
	 * <p>
	 * If {@code texture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param texture the current {@code Texture} instance
	 * @throws NullPointerException thrown if, and only if, {@code texture} is {@code null}
	 */
	public void setTexture(final Texture texture) {
		this.texture.set(Objects.requireNonNull(texture, "texture == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private String doGetName() {
		switch(getTexture().getClass().getSimpleName()) {
			case NAME_BLEND_TEXTURE:
				return "Blend Texture";
			case NAME_BULLSEYE_TEXTURE:
				return "Bullseye Texture";
			case NAME_CHECKERBOARD_TEXTURE:
				return "Checkerboard Texture";
			case NAME_CONSTANT_TEXTURE:
				return "Constant Texture";
			case NAME_DOT_PRODUCT_TEXTURE:
				return "Dot Product Texture";
			case NAME_L_D_R_IMAGE_TEXTURE:
				return "LDR Image Texture";
			case NAME_MARBLE_TEXTURE:
				return "Marble Texture";
			case NAME_POLKA_DOT_TEXTURE:
				return "Polka Dot Texture";
			case NAME_SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE:
				return "Simplex Fractional Brownian Motion Texture";
			case NAME_SURFACE_NORMAL_TEXTURE:
				return "Surface Normal Texture";
			case NAME_U_V_TEXTURE:
				return "UV Texture";
			default:
				return "Texture";
		}
	}
	
	@SuppressWarnings("unused")
	private void doOnAction(final ActionEvent e) {
		final
		TextureWizard textureWizard = new TextureWizard(getScene().getWindow());
		textureWizard.showAndWait().ifPresent(texture -> this.texture.set(texture));
		
		this.button.setGraphic(new ImageView(WritableImageCaches.get(this.texture.get())));
		this.button.setText(doGetName());
	}
}