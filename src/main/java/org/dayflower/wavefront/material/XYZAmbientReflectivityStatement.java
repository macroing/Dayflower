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
package org.dayflower.wavefront.material;

import java.util.Objects;

import org.macroing.java.lang.Strings;

/**
 * An {@code XYZAmbientReflectivityStatement} represents an ambient reflectivity statement using XYZ ({@code "Ka xyz"}) of a Wavefront Material file.
 * <p>
 * This class is currently thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class XYZAmbientReflectivityStatement implements AmbientReflectivityStatement {
	/**
	 * The name of the second part of this {@code XYZAmbientReflectivityStatement}, which is {@code "xyz"}.
	 * <p>
	 * The full name of this {@code XYZAmbientReflectivityStatement} would be {@code "Ka xyz"}.
	 */
	public static final String NAME = "xyz";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final float x;
	private final float y;
	private final float z;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code XYZAmbientReflectivityStatement} instance.
	 * 
	 * @param x the X-component
	 * @param y the Y-component
	 * @param z the Z-component
	 */
	public XYZAmbientReflectivityStatement(final float x, final float y, final float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the name of this {@code XYZAmbientReflectivityStatement} instance.
	 * 
	 * @return the name of this {@code XYZAmbientReflectivityStatement} instance
	 */
	@Override
	public String getName() {
		return String.format("%s %s", AmbientReflectivityStatement.NAME, NAME);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code XYZAmbientReflectivityStatement} instance.
	 * 
	 * @return a {@code String} representation of this {@code XYZAmbientReflectivityStatement} instance
	 */
	@Override
	public String toString() {
		return String.format("%s %s %s %s", getName(), Strings.toNonScientificNotation(getX()), Strings.toNonScientificNotation(getY()), Strings.toNonScientificNotation(getZ()));
	}
	
	/**
	 * Compares {@code object} to this {@code XYZAmbientReflectivityStatement} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code XYZAmbientReflectivityStatement}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code XYZAmbientReflectivityStatement} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code XYZAmbientReflectivityStatement}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof XYZAmbientReflectivityStatement)) {
			return false;
		} else if(!Objects.equals(getName(), XYZAmbientReflectivityStatement.class.cast(object).getName())) {
			return false;
		} else if(Float.compare(getX(), XYZAmbientReflectivityStatement.class.cast(object).getX()) != 0) {
			return false;
		} else if(Float.compare(getY(), XYZAmbientReflectivityStatement.class.cast(object).getY()) != 0) {
			return false;
		} else if(Float.compare(getZ(), XYZAmbientReflectivityStatement.class.cast(object).getZ()) != 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the X-component.
	 * 
	 * @return the X-component
	 */
	public float getX() {
		return this.x;
	}
	
	/**
	 * Returns the Y-component.
	 * 
	 * @return the Y-component
	 */
	public float getY() {
		return this.y;
	}
	
	/**
	 * Returns the Z-component.
	 * 
	 * @return the Z-component
	 */
	public float getZ() {
		return this.z;
	}
	
	/**
	 * Returns a hash code for this {@code XYZAmbientReflectivityStatement} instance.
	 * 
	 * @return a hash code for this {@code XYZAmbientReflectivityStatement} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), Float.valueOf(getX()), Float.valueOf(getY()), Float.valueOf(getZ()));
	}
}