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
package org.dayflower.geometry.shape;

import java.io.DataInput;
import java.io.UncheckedIOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dayflower.geometry.Shape3D;
import org.dayflower.geometry.Shape3DReader;

/**
 * A {@code DefaultShape3DReader} is a {@link Shape3DReader} implementation that reads all official {@link Shape3D} instances from a {@code DataInput} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DefaultShape3DReader implements Shape3DReader {
	private final Map<Integer, Shape3DReader> shape3DReaders;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DefaultShape3DReader} instance.
	 */
	public DefaultShape3DReader() {
		this.shape3DReaders = new LinkedHashMap<>();
		this.shape3DReaders.put(Integer.valueOf(Cone3D.ID), new Cone3DReader());
		this.shape3DReaders.put(Integer.valueOf(Cylinder3D.ID), new Cylinder3DReader());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Reads a {@link Shape3D} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code Shape3D} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code id} is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * The ID of the {@code Shape3D} instance to read has already been read from {@code dataInput} when this method is called. It is passed to this method as a parameter argument.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @param id the ID of the {@code Shape3D} type to read
	 * @return the {@code Shape3D} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, {@code id} is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public Shape3D read(final DataInput dataInput, final int id) {
		switch(id) {
			case Cone3D.ID:
			case Cylinder3D.ID:
				return this.shape3DReaders.get(Integer.valueOf(id)).read(dataInput, id);
			default:
				throw new IllegalArgumentException(String.format("The ID %d is invalid.", Integer.valueOf(id)));
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code DefaultShape3DReader} instance supports reading {@link Shape3D} instances with an ID of {@code id}, {@code false} otherwise.
	 * 
	 * @param id the ID of the {@code Shape3D} type to check
	 * @return {@code true} if, and only if, this {@code DefaultShape3DReader} instance supports reading {@code Shape3D} instances with an ID of {@code id}, {@code false} otherwise
	 */
	@Override
	public boolean isSupported(final int id) {
		return this.shape3DReaders.containsKey(Integer.valueOf(id));
	}
}