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
package org.dayflower.display;

import java.io.File;
import java.util.Objects;

import org.dayflower.image.Image;

/**
 * A {@code FileDisplay} is a {@link Display} implementation that displays an {@link Image} instance by writing it to a file.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class FileDisplay implements Display {
	private final File file;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code FileDisplay} instance.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param file a {@code File} instance
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 */
	public FileDisplay(final File file) {
		this.file = Objects.requireNonNull(file, "file == null");
	}
	
	/**
	 * Constructs a new {@code FileDisplay} instance.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new FileDisplay(new File(pathname));
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} instance with a pathname
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 */
	public FileDisplay(final String pathname) {
		this(new File(pathname));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code FileDisplay} instance.
	 * 
	 * @return a {@code String} representation of this {@code FileDisplay} instance
	 */
	@Override
	public String toString() {
		return String.format("new FileDisplay(new File(\"%s\"))", this.file.getPath());
	}
	
	/**
	 * Compares {@code object} to this {@code FileDisplay} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code FileDisplay}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code FileDisplay} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code FileDisplay}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof FileDisplay)) {
			return false;
		} else if(!Objects.equals(this.file, FileDisplay.class.cast(object).file)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code FileDisplay} instance.
	 * 
	 * @return a hash code for this {@code FileDisplay} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.file);
	}
	
	/**
	 * Updates this {@code FileDisplay} instance with {@code image}.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param image the {@link Image} instance to display
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	@Override
	public void update(final Image image) {
		image.save(this.file); 
	}
}