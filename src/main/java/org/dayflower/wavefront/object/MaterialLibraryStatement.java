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
package org.dayflower.wavefront.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A {@code MaterialLibraryStatement} represents a material library statement ({@code "mtllib"}) of a Wavefront Object file.
 * <p>
 * This class is not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MaterialLibraryStatement implements ObjectFileStatement {
	/**
	 * The name of this {@code MaterialLibraryStatement} which is {@code "mtllib"}.
	 */
	public static final String NAME = "mtllib";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final List<String> filenames;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new empty {@code MaterialLibraryStatement} instance.
	 */
	public MaterialLibraryStatement() {
		this.filenames = new ArrayList<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} of {@code String}s with all filenames currently added to this {@code MaterialLibraryStatement} instance.
	 * <p>
	 * Modifying the returned {@code List} will not affect this {@code MaterialLibraryStatement} instance.
	 * 
	 * @return a {@code List} of {@code String}s with all filenames currently added to this {@code MaterialLibraryStatement} instance
	 */
	public List<String> getFilenames() {
		return new ArrayList<>(this.filenames);
	}
	
	/**
	 * Returns the name of this {@code MaterialLibraryStatement} instance.
	 * 
	 * @return the name of this {@code MaterialLibraryStatement} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code MaterialLibraryStatement} instance.
	 * 
	 * @return a {@code String} representation of this {@code MaterialLibraryStatement} instance
	 */
	@Override
	public String toString() {
		return String.format("%s %s", getName(), this.filenames.stream().collect(Collectors.joining(" ")));
	}
	
	/**
	 * Compares {@code object} to this {@code MaterialLibraryStatement} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code MaterialLibraryStatement}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code MaterialLibraryStatement} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code MaterialLibraryStatement}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof MaterialLibraryStatement)) {
			return false;
		} else if(!Objects.equals(getName(), MaterialLibraryStatement.class.cast(object).getName())) {
			return false;
		} else if(!Objects.equals(getFilenames(), MaterialLibraryStatement.class.cast(object).getFilenames())) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code MaterialLibraryStatement} instance.
	 * 
	 * @return a hash code for this {@code MaterialLibraryStatement} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), getFilenames());
	}
	
	/**
	 * Adds {@code filename} to this {@code MaterialLibraryStatement} instance.
	 * <p>
	 * If {@code filename} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param filename the filename to add
	 * @throws NullPointerException thrown if, and only if, {@code filename} is {@code null}
	 */
	public void addFilename(final String filename) {
		this.filenames.add(Objects.requireNonNull(filename, "filename == null"));
	}
	
	/**
	 * Removes {@code filename} from this {@code MaterialLibraryStatement} instance, if present.
	 * <p>
	 * If {@code filename} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param filename the filename to remove
	 * @throws NullPointerException thrown if, and only if, {@code filename} is {@code null}
	 */
	public void removeFilename(final String filename) {
		this.filenames.remove(Objects.requireNonNull(filename, "filename == null"));
	}
}