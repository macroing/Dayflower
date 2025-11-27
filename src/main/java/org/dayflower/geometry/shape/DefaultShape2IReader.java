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
package org.dayflower.geometry.shape;

import java.io.DataInput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.dayflower.geometry.Shape2I;
import org.dayflower.geometry.Shape2IReader;

/**
 * A {@code DefaultShape2IReader} is a {@link Shape2IReader} implementation that reads all official {@link Shape2I} instances from a {@code DataInput} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DefaultShape2IReader implements Shape2IReader {
	private final Map<Integer, Shape2IReader> shape2IReaders;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DefaultShape2IReader} instance.
	 */
	public DefaultShape2IReader() {
		this.shape2IReaders = new LinkedHashMap<>();
		this.shape2IReaders.put(Integer.valueOf(Circle2I.ID), new Circle2IReader());
		this.shape2IReaders.put(Integer.valueOf(LineSegment2I.ID), new LineSegment2IReader());
		this.shape2IReaders.put(Integer.valueOf(Polygon2I.ID), new Polygon2IReader());
		this.shape2IReaders.put(Integer.valueOf(Rectangle2I.ID), new Rectangle2IReader());
		this.shape2IReaders.put(Integer.valueOf(Triangle2I.ID), new Triangle2IReader());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Reads a {@link Shape2I} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code Shape2I} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the ID is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return the {@code Shape2I} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, the ID is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	@Override
	public Shape2I read(final DataInput dataInput) {
		try {
			return read(dataInput, dataInput.readInt());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Reads a {@link Shape2I} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code Shape2I} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code id} is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * The ID of the {@code Shape2I} instance to read has already been read from {@code dataInput} when this method is called. It is passed to this method as a parameter argument.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @param id the ID of the {@code Shape2I} type to read
	 * @return the {@code Shape2I} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, {@code id} is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	@Override
	public Shape2I read(final DataInput dataInput, final int id) {
		Objects.requireNonNull(dataInput, "dataInput == null");
		
		switch(id) {
			case Circle2I.ID:
			case LineSegment2I.ID:
			case Polygon2I.ID:
			case Rectangle2I.ID:
			case Triangle2I.ID:
				return this.shape2IReaders.get(Integer.valueOf(id)).read(dataInput, id);
			default:
				throw new IllegalArgumentException(String.format("The ID %d is invalid.", Integer.valueOf(id)));
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code DefaultShape2IReader} instance supports reading {@link Shape2I} instances with an ID of {@code id}, {@code false} otherwise.
	 * 
	 * @param id the ID of the {@code Shape2I} type to check
	 * @return {@code true} if, and only if, this {@code DefaultShape2IReader} instance supports reading {@code Shape2I} instances with an ID of {@code id}, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean isSupported(final int id) {
		return this.shape2IReaders.containsKey(Integer.valueOf(id));
	}
}