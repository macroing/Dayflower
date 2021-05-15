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

import org.dayflower.geometry.Shape2D;
import org.dayflower.geometry.Shape2DReader;

/**
 * A {@code DefaultShape2DReader} is a {@link Shape2DReader} implementation that reads all official {@link Shape2D} instances from a {@code DataInput} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DefaultShape2DReader implements Shape2DReader {
	private final Map<Integer, Shape2DReader> shape2DReaders;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DefaultShape2DReader} instance.
	 */
	public DefaultShape2DReader() {
		this.shape2DReaders = new LinkedHashMap<>();
		this.shape2DReaders.put(Integer.valueOf(Circle2D.ID), new Circle2DReader());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Reads a {@link Shape2D} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code Shape2D} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code id} is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * The ID of the {@code Shape2D} instance to read has already been read from {@code dataInput} when this method is called. It is passed to this method as a parameter argument.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @param id the ID of the {@code Shape2D} type to read
	 * @return the {@code Shape2D} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, {@code id} is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public Shape2D read(final DataInput dataInput, final int id) {
		switch(id) {
			case Circle2D.ID:
				return this.shape2DReaders.get(Integer.valueOf(id)).read(dataInput, id);
			default:
				throw new IllegalArgumentException(String.format("The ID %d is invalid.", Integer.valueOf(id)));
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code DefaultShape2DReader} instance supports reading {@link Shape2D} instances with an ID of {@code id}, {@code false} otherwise.
	 * 
	 * @param id the ID of the {@code Shape2D} type to check
	 * @return {@code true} if, and only if, this {@code DefaultShape2DReader} instance supports reading {@code Shape2D} instances with an ID of {@code id}, {@code false} otherwise
	 */
	@Override
	public boolean isSupported(final int id) {
		return this.shape2DReaders.containsKey(Integer.valueOf(id));
	}
}