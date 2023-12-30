/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.scene.Material;
import org.dayflower.scene.material.BullseyeMaterial;
import org.dayflower.scene.material.CheckerboardMaterial;
import org.dayflower.scene.material.ClearCoatMaterial;
import org.dayflower.scene.material.DisneyMaterial;
import org.dayflower.scene.material.GlassMaterial;
import org.dayflower.scene.material.GlossyMaterial;
import org.dayflower.scene.material.MatteMaterial;
import org.dayflower.scene.material.MetalMaterial;
import org.dayflower.scene.material.MirrorMaterial;
import org.dayflower.scene.material.PlasticMaterial;
import org.dayflower.scene.material.PolkaDotMaterial;
import org.dayflower.scene.material.SubstrateMaterial;
import org.dayflower.scene.material.TranslucentMaterial;
import org.dayflower.scene.material.UberMaterial;
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
 * A {@code MaterialWizard} is a wizard for {@link Material} creation.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MaterialWizard {
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
	
	private final Window owner;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MaterialWizard} instance.
	 * <p>
	 * If {@code owner} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param owner the owner
	 * @throws NullPointerException thrown if, and only if, {@code owner} is {@code null}
	 */
	public MaterialWizard(final Window owner) {
		this.owner = Objects.requireNonNull(owner, "owner == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns an optional {@link Material} instance.
	 * 
	 * @return an optional {@code Material} instance
	 */
	public Optional<Material> showAndWait() {
		final Dialog<MaterialInfo> dialogMaterialInfo = doCreateDialogMaterialInfo();
		
		final Optional<MaterialInfo> optionalMaterialInfo = dialogMaterialInfo.showAndWait();
		
		if(optionalMaterialInfo.isPresent()) {
			final MaterialInfo materialInfo = optionalMaterialInfo.get();
			
			final Dialog<Material> dialogMaterial = doCreateDialogMaterial(materialInfo);
			
			return dialogMaterial.showAndWait();
		}
		
		return Optional.empty();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Dialog<Material> doCreateDialogMaterial(final MaterialInfo materialInfo) {
		final MaterialGridPane materialGridPane = materialInfo.createMaterialGridPane();
		
		final
		Dialog<Material> dialog = new Dialog<>();
		dialog.initOwner(this.owner);
		dialog.setResultConverter(buttonType -> buttonType.getButtonData() == ButtonType.OK.getButtonData() ? materialGridPane.createMaterial() : null);
		dialog.setTitle(materialInfo.getTitle());
		
		final
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		dialogPane.setContent(materialGridPane);
		
		return dialog;
	}
	
	private Dialog<MaterialInfo> doCreateDialogMaterialInfo() {
		final AtomicReference<MaterialInfo> materialInfo = new AtomicReference<>(new MaterialInfo(MatteMaterial.class));
		
		final
		ComboBox<MaterialInfo> comboBox = new ComboBox<>();
		comboBox.getItems().addAll(new MaterialInfo(BullseyeMaterial.class), new MaterialInfo(CheckerboardMaterial.class), new MaterialInfo(ClearCoatMaterial.class), new MaterialInfo(DisneyMaterial.class), new MaterialInfo(GlassMaterial.class), new MaterialInfo(GlossyMaterial.class), new MaterialInfo(MatteMaterial.class), new MaterialInfo(MetalMaterial.class), new MaterialInfo(MirrorMaterial.class), new MaterialInfo(PlasticMaterial.class), new MaterialInfo(PolkaDotMaterial.class), new MaterialInfo(SubstrateMaterial.class), new MaterialInfo(TranslucentMaterial.class), new MaterialInfo(UberMaterial.class));
		comboBox.setMaxWidth(Doubles.MAX_VALUE);
		comboBox.setValue(new MaterialInfo(MatteMaterial.class));
		comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> materialInfo.set(newValue));
		
		final
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10.0D);
		gridPane.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		gridPane.setVgap(10.0D);
		gridPane.add(new Text("Material"), 0, 0);
		gridPane.add(comboBox, 1, 0);
		
		final
		Dialog<MaterialInfo> dialog = new Dialog<>();
		dialog.initOwner(this.owner);
		dialog.setResultConverter(buttonType -> buttonType.getButtonData() == ButtonType.NEXT.getButtonData() ? materialInfo.get() : null);
		dialog.setTitle("New Material");
		
		final
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.NEXT, ButtonType.CANCEL);
		dialogPane.setContent(gridPane);
		
		return dialog;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class MaterialInfo {
		private final Class<? extends Material> clazz;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public MaterialInfo(final Class<? extends Material> clazz) {
			this.clazz = Objects.requireNonNull(clazz, "clazz == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public String getTitle() {
			switch(this.clazz.getSimpleName()) {
				case NAME_BULLSEYE_MATERIAL:
					return "New Bullseye Material";
				case NAME_CHECKERBOARD_MATERIAL:
					return "New Checkerboard Material";
				case NAME_CLEAR_COAT_MATERIAL:
					return "New Clear Coat Material";
				case NAME_DISNEY_MATERIAL:
					return "New Disney Material";
				case NAME_GLASS_MATERIAL:
					return "New Glass Material";
				case NAME_GLOSSY_MATERIAL:
					return "New Glossy Material";
				case NAME_MATTE_MATERIAL:
					return "New Matte Material";
				case NAME_METAL_MATERIAL:
					return "New Metal Material";
				case NAME_MIRROR_MATERIAL:
					return "New Mirror Material";
				case NAME_PLASTIC_MATERIAL:
					return "New Plastic Material";
				case NAME_POLKA_DOT_MATERIAL:
					return "New Polka Dot Material";
				case NAME_SUBSTRATE_MATERIAL:
					return "New Substrate Material";
				case NAME_TRANSLUCENT_MATERIAL:
					return "New Translucent Material";
				case NAME_UBER_MATERIAL:
					return "New Uber Material";
				default:
					return "New Material";
			}
		}
		
		@Override
		public String toString() {
			return getTitle();
		}
		
		public MaterialGridPane createMaterialGridPane() {
			switch(this.clazz.getSimpleName()) {
				case NAME_BULLSEYE_MATERIAL:
					return new BullseyeMaterialGridPane();
				case NAME_CHECKERBOARD_MATERIAL:
					return new CheckerboardMaterialGridPane();
				case NAME_CLEAR_COAT_MATERIAL:
					return new ClearCoatMaterialGridPane();
				case NAME_DISNEY_MATERIAL:
					return new DisneyMaterialGridPane();
				case NAME_GLASS_MATERIAL:
					return new GlassMaterialGridPane();
				case NAME_GLOSSY_MATERIAL:
					return new GlossyMaterialGridPane();
				case NAME_MATTE_MATERIAL:
					return new MatteMaterialGridPane();
				case NAME_METAL_MATERIAL:
					return new MetalMaterialGridPane();
				case NAME_MIRROR_MATERIAL:
					return new MirrorMaterialGridPane();
				case NAME_PLASTIC_MATERIAL:
					return new PlasticMaterialGridPane();
				case NAME_POLKA_DOT_MATERIAL:
					return new PolkaDotMaterialGridPane();
				case NAME_SUBSTRATE_MATERIAL:
					return new SubstrateMaterialGridPane();
				case NAME_TRANSLUCENT_MATERIAL:
					return new TranslucentMaterialGridPane();
				case NAME_UBER_MATERIAL:
					return new UberMaterialGridPane();
				default:
					return new MatteMaterialGridPane();
			}
		}
		
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof MaterialInfo)) {
				return false;
			} else if(!Objects.equals(this.clazz, MaterialInfo.class.cast(object).clazz)) {
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