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
package org.dayflower.javafx.application;

import java.util.Objects;

import org.dayflower.scene.Primitive;

import org.macroing.javafx.scene.layout.CenteredVBox;

import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

final class PrimitiveConfigurationView extends Accordion {
	public PrimitiveConfigurationView() {
		getPanes().add(new TitledPane("Material", doCreateCenteredVBoxMaterial()));
		getPanes().add(new TitledPane("Shape", doCreateCenteredVBoxShape()));
		getPanes().add(new TitledPane("Transform", doCreateCenteredVBoxTransform()));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	public void setPrimitive(final Primitive primitive) {
		Objects.requireNonNull(primitive, "primitive == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static CenteredVBox doCreateCenteredVBoxMaterial() {
		final
		CenteredVBox centeredVBox = new CenteredVBox();
		centeredVBox.addLabel("Coming soon...", 12.0D);
		
		return centeredVBox;
	}
	
	private static CenteredVBox doCreateCenteredVBoxShape() {
		final
		CenteredVBox centeredVBox = new CenteredVBox();
		centeredVBox.addLabel("Coming soon...", 12.0D);
		
		return centeredVBox;
	}
	
	private static CenteredVBox doCreateCenteredVBoxTransform() {
		final
		CenteredVBox centeredVBox = new CenteredVBox();
		centeredVBox.addLabel("Coming soon...", 12.0D);
		
		return centeredVBox;
	}
}