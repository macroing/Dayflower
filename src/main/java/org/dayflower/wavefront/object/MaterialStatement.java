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
package org.dayflower.wavefront.object;

import java.util.Objects;

/**
 * A {@code MaterialStatement} represents a material statement ({@code "usemtl"}) of a Wavefront Object file.
 * <p>
 * This class is currently thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MaterialStatement implements ObjectFileStatement {
	/**
	 * The name of this {@code MaterialStatement} which is {@code "usemtl"}.
	 */
	public static final String NAME = "usemtl";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final String materialName;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MaterialStatement} instance.
	 * <p>
	 * If {@code materialName} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialName the material name to use
	 * @throws NullPointerException thrown if, and only if, {@code materialName} is {@code null}
	 */
	public MaterialStatement(final String materialName) {
		this.materialName = Objects.requireNonNull(materialName, "materialName == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the material name.
	 * 
	 * @return the material name
	 */
	public String getMaterialName() {
		return this.materialName;
	}
	
	/**
	 * Returns the name of this {@code MaterialStatement} instance.
	 * 
	 * @return the name of this {@code MaterialStatement} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code MaterialStatement} instance.
	 * 
	 * @return a {@code String} representation of this {@code MaterialStatement} instance
	 */
	@Override
	public String toString() {
		return String.format("%s %s", getName(), getMaterialName());
	}
	
	/**
	 * Compares {@code object} to this {@code MaterialStatement} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code MaterialStatement}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code MaterialStatement} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code MaterialStatement}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof MaterialStatement)) {
			return false;
		} else if(!Objects.equals(getName(), MaterialStatement.class.cast(object).getName())) {
			return false;
		} else if(!Objects.equals(getMaterialName(), MaterialStatement.class.cast(object).getMaterialName())) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code MaterialStatement} instance.
	 * 
	 * @return a hash code for this {@code MaterialStatement} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), getMaterialName());
	}
}