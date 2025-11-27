/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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
package org.dayflower.javafx.material;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.scene.Material;
import org.dayflower.scene.material.MatteMaterial;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * A {@code MaterialPicker} is a control for picking a {@link Material} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MaterialPicker extends BorderPane {
	private static final String NAME_BULLSEYE_MATERIAL = "BullseyeMaterial";
	private static final String NAME_CHECKERBOARD_MATERIAL = "CheckerboardMaterial";
	private static final String NAME_CLEAR_COAT_MATERIAL = "ClearCoatMaterial";
	private static final String NAME_DISNEY_MATERIAL = "DisneyMaterial";
	private static final String NAME_GLASS_MATERIAL = "GlassMaterial";
	private static final String NAME_GLOSSY_MATERIAL = "GlossyMaterial";
	private static final String NAME_MATTE_MATERIAL = "MatteMaterial";
	private static final String NAME_METAL_MATERIAL = "MetalMaterial";
	private static final String NAME_MIRROR_MATERIAL = "MirrorMaterial";
	private static final String NAME_PLASTIC_MATERIAL = "PlasticMaterial";
	private static final String NAME_POLKA_DOT_MATERIAL = "PolkaDotMaterial";
	private static final String NAME_SUBSTRATE_MATERIAL = "SubstrateMaterial";
	private static final String NAME_TRANSLUCENT_MATERIAL = "TranslucentMaterial";
	private static final String NAME_UBER_MATERIAL = "UberMaterial";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AtomicReference<Material> material;
	private final Button button;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MaterialPicker} instance with {@code new MatteMaterial()} as the current {@link Material} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MaterialPicker(new MatteMaterial());
	 * }
	 * </pre>
	 */
	public MaterialPicker() {
		this(new MatteMaterial());
	}
	
	/**
	 * Constructs a new {@code MaterialPicker} instance with {@code material} as the current {@link Material} instance.
	 * <p>
	 * If {@code material} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param material the current {@code Material} instance
	 * @throws NullPointerException thrown if, and only if, {@code material} is {@code null}
	 */
	public MaterialPicker(final Material material) {
		this.material = new AtomicReference<>(Objects.requireNonNull(material, "material == null"));
		this.button = new Button(doGetName());
		this.button.setGraphic(new ImageView(WritableImageCaches.get(material)));
		this.button.setMaxWidth(Double.MAX_VALUE);
		this.button.setOnAction(this::doOnAction);
		
		setCenter(this.button);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the current {@link Material} instance.
	 * 
	 * @return the current {@code Material} instance
	 */
	public Material getMaterial() {
		return this.material.get();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private String doGetName() {
		switch(getMaterial().getClass().getSimpleName()) {
			case NAME_BULLSEYE_MATERIAL:
				return "Bullseye Material";
			case NAME_CHECKERBOARD_MATERIAL:
				return "Checkerboard Material";
			case NAME_CLEAR_COAT_MATERIAL:
				return "Clear Coat Material";
			case NAME_DISNEY_MATERIAL:
				return "Disney Material";
			case NAME_GLASS_MATERIAL:
				return "Glass Material";
			case NAME_GLOSSY_MATERIAL:
				return "Glossy Material";
			case NAME_MATTE_MATERIAL:
				return "Matte Material";
			case NAME_METAL_MATERIAL:
				return "Metal Material";
			case NAME_MIRROR_MATERIAL:
				return "Mirror Material";
			case NAME_PLASTIC_MATERIAL:
				return "Plastic Material";
			case NAME_POLKA_DOT_MATERIAL:
				return "Polka Dot Material";
			case NAME_SUBSTRATE_MATERIAL:
				return "Substrate Material";
			case NAME_TRANSLUCENT_MATERIAL:
				return "Translucent Material";
			case NAME_UBER_MATERIAL:
				return "Uber Material";
			default:
				return "Material";
		}
	}
	
	@SuppressWarnings("unused")
	private void doOnAction(final ActionEvent e) {
		final
		MaterialWizard materialWizard = new MaterialWizard(getScene().getWindow());
		materialWizard.showAndWait().ifPresent(material -> this.material.set(material));
		
		this.button.setGraphic(new ImageView(WritableImageCaches.get(this.material.get())));
		this.button.setText(doGetName());
	}
}