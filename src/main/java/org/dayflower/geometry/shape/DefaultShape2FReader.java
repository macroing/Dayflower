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

import org.dayflower.geometry.Shape2F;
import org.dayflower.geometry.Shape2FReader;

/**
 * A {@code DefaultShape2FReader} is a {@link Shape2FReader} implementation that reads all official {@link Shape2F} instances from a {@code DataInput} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DefaultShape2FReader implements Shape2FReader {
	private final Map<Integer, Shape2FReader> shape2FReaders;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DefaultShape2FReader} instance.
	 */
	public DefaultShape2FReader() {
		this.shape2FReaders = new LinkedHashMap<>();
		this.shape2FReaders.put(Integer.valueOf(Circle2F.ID), new Circle2FReader());
		this.shape2FReaders.put(Integer.valueOf(LineSegment2F.ID), new LineSegment2FReader());
		this.shape2FReaders.put(Integer.valueOf(Polygon2F.ID), new Polygon2FReader());
		this.shape2FReaders.put(Integer.valueOf(Rectangle2F.ID), new Rectangle2FReader());
		this.shape2FReaders.put(Integer.valueOf(Triangle2F.ID), new Triangle2FReader());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Reads a {@link Shape2F} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code Shape2F} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the ID is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return the {@code Shape2F} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, the ID is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	@Override
	public Shape2F read(final DataInput dataInput) {
		try {
			return read(dataInput, dataInput.readInt());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Reads a {@link Shape2F} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code Shape2F} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code id} is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * The ID of the {@code Shape2F} instance to read has already been read from {@code dataInput} when this method is called. It is passed to this method as a parameter argument.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @param id the ID of the {@code Shape2F} type to read
	 * @return the {@code Shape2F} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, {@code id} is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	@Override
	public Shape2F read(final DataInput dataInput, final int id) {
		Objects.requireNonNull(dataInput, "dataInput == null");
		
		switch(id) {
			case Circle2F.ID:
			case LineSegment2F.ID:
			case Polygon2F.ID:
			case Rectangle2F.ID:
			case Triangle2F.ID:
				return this.shape2FReaders.get(Integer.valueOf(id)).read(dataInput, id);
			default:
				throw new IllegalArgumentException(String.format("The ID %d is invalid.", Integer.valueOf(id)));
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code DefaultShape2FReader} instance supports reading {@link Shape2F} instances with an ID of {@code id}, {@code false} otherwise.
	 * 
	 * @param id the ID of the {@code Shape2F} type to check
	 * @return {@code true} if, and only if, this {@code DefaultShape2FReader} instance supports reading {@code Shape2F} instances with an ID of {@code id}, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean isSupported(final int id) {
		return this.shape2FReaders.containsKey(Integer.valueOf(id));
	}
}