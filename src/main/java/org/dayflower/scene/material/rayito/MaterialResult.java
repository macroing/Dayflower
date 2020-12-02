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
package org.dayflower.scene.material.rayito;

import static org.dayflower.util.Floats.equal;

import java.util.Objects;

import org.dayflower.image.Color3F;
import org.dayflower.scene.Material;
import org.dayflower.scene.bxdf.rayito.BXDF;

/**
 * A {@code MaterialResult} represents a result produced by a {@link Material} instance.
 * <p>
 * This class can be considered immutable and thread-safe if, and only if, its associated {@link BXDF} instance is.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MaterialResult {
	private final BXDF selectedBXDF;
	private final Color3F color;
	private final float selectedBXDFWeight;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MaterialResult} instance.
	 * <p>
	 * If either {@code color} or {@code selectedBXDF} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MaterialResult(color, selectedBXDF, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param color the {@link Color3F} instance associated with this {@code MaterialResult} instance
	 * @param selectedBXDF the selected {@link BXDF} instance associated with this {@code MaterialResult} instance
	 * @throws NullPointerException thrown if, and only if, either {@code color} or {@code selectedBXDF} are {@code null}
	 */
	public MaterialResult(final Color3F color, final BXDF selectedBXDF) {
		this(color, selectedBXDF, 1.0F);
	}
	
	/**
	 * Constructs a new {@code MaterialResult} instance.
	 * <p>
	 * If either {@code color} or {@code selectedBXDF} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color the {@link Color3F} instance associated with this {@code MaterialResult} instance
	 * @param selectedBXDF the selected {@link BXDF} instance associated with this {@code MaterialResult} instance
	 * @param selectedBXDFWeight the weight of the selected {@code BXDF} instance associated with this {@code MaterialResult} instance
	 * @throws NullPointerException thrown if, and only if, either {@code color} or {@code selectedBXDF} are {@code null}
	 */
	public MaterialResult(final Color3F color, final BXDF selectedBXDF, final float selectedBXDFWeight) {
		this.color = Objects.requireNonNull(color, "color == null");
		this.selectedBXDF = Objects.requireNonNull(selectedBXDF, "selectedBXDF == null");
		this.selectedBXDFWeight = selectedBXDFWeight;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the selected {@link BXDF} instance associated with this {@code MaterialResult} instance.
	 * 
	 * @return the selected {@code BXDF} instance associated with this {@code MaterialResult} instance
	 */
	public BXDF getSelectedBXDF() {
		return this.selectedBXDF;
	}
	
	/**
	 * Returns the {@link Color3F} instance associated with this {@code MaterialResult} instance.
	 * 
	 * @return the {@code Color3F} instance associated with this {@code MaterialResult} instance
	 */
	public Color3F getColor() {
		return this.color;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code MaterialResult} instance.
	 * 
	 * @return a {@code String} representation of this {@code MaterialResult} instance
	 */
	@Override
	public String toString() {
		return String.format("new MaterialResult(%s, %s, %+.10f)", this.color, this.selectedBXDF, Float.valueOf(this.selectedBXDFWeight));
	}
	
	/**
	 * Compares {@code object} to this {@code MaterialResult} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code MaterialResult}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code MaterialResult} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code MaterialResult}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof MaterialResult)) {
			return false;
		} else if(!Objects.equals(this.selectedBXDF, MaterialResult.class.cast(object).selectedBXDF)) {
			return false;
		} else if(!Objects.equals(this.color, MaterialResult.class.cast(object).color)) {
			return false;
		} else if(!equal(this.selectedBXDFWeight, MaterialResult.class.cast(object).selectedBXDFWeight)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the weight of the selected {@link BXDF} instance associated with this {@code MaterialResult} instance.
	 * 
	 * @return the weight of the selected {@code BXDF} instance associated with this {@code MaterialResult} instance
	 */
	public float getSelectedBXDFWeight() {
		return this.selectedBXDFWeight;
	}
	
	/**
	 * Returns a hash code for this {@code MaterialResult} instance.
	 * 
	 * @return a hash code for this {@code MaterialResult} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.selectedBXDF, this.color, Float.valueOf(this.selectedBXDFWeight));
	}
}