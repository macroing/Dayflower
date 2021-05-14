/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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
package org.dayflower.geometry;

import java.io.DataInput;

/**
 * A {@code BoundingVolume3DReader} reads {@link BoundingVolume3D} instances from a {@code DataInput} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface BoundingVolume3DReader {
	/**
	 * Reads a {@link BoundingVolume3D} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code BoundingVolume3D} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * The ID of the {@code BoundingVolume3D} instance to read has already been read from {@code dataInput} when this method is called. It is passed to this method as a parameter argument.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @param id the ID of the {@code BoundingVolume3D} type to read
	 * @return the {@code BoundingVolume3D} instance that was read
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	BoundingVolume3D read(final DataInput dataInput, final int id);
	
	/**
	 * Returns {@code true} if, and only if, this {@code BoundingVolume3DReader} instance supports reading {@link BoundingVolume3D} instances with an ID of {@code id}, {@code false} otherwise.
	 * 
	 * @param id the ID of the {@code BoundingVolume3D} type to check
	 * @return {@code true} if, and only if, this {@code BoundingVolume3DReader} instance supports reading {@code BoundingVolume3D} instances with an ID of {@code id}, {@code false} otherwise
	 */
	boolean isSupported(final int id);
}