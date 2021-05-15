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
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.Shape3FReader;

/**
 * A {@code DefaultShape3FReader} is a {@link Shape3FReader} implementation that reads all official {@link Shape3F} instances from a {@code DataInput} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DefaultShape3FReader implements Shape3FReader {
	private final Map<Integer, Shape3FReader> shape3FReaders;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DefaultShape3FReader} instance.
	 */
	public DefaultShape3FReader() {
		this.shape3FReaders = new LinkedHashMap<>();
		this.shape3FReaders.put(Integer.valueOf(Cone3F.ID), new Cone3FReader());
		this.shape3FReaders.put(Integer.valueOf(Curve3F.ID), new Curve3FReader());
		this.shape3FReaders.put(Integer.valueOf(Curves3F.ID), new Curves3FReader());
		this.shape3FReaders.put(Integer.valueOf(Cylinder3F.ID), new Cylinder3FReader());
		this.shape3FReaders.put(Integer.valueOf(Disk3F.ID), new Disk3FReader());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Reads a {@link Shape3F} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code Shape3F} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the ID is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return the {@code Shape3F} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, the ID is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public Shape3F read(final DataInput dataInput) {
		try {
			return read(dataInput, dataInput.readInt());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Reads a {@link Shape3F} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code Shape3F} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code id} is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * The ID of the {@code Shape3F} instance to read has already been read from {@code dataInput} when this method is called. It is passed to this method as a parameter argument.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @param id the ID of the {@code Shape3F} type to read
	 * @return the {@code Shape3F} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, {@code id} is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public Shape3F read(final DataInput dataInput, final int id) {
		switch(id) {
			case Cone3F.ID:
			case Curve3F.ID:
			case Curves3F.ID:
			case Cylinder3F.ID:
			case Disk3F.ID:
				return this.shape3FReaders.get(Integer.valueOf(id)).read(dataInput, id);
			default:
				throw new IllegalArgumentException(String.format("The ID %d is invalid.", Integer.valueOf(id)));
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code DefaultShape3FReader} instance supports reading {@link Shape3F} instances with an ID of {@code id}, {@code false} otherwise.
	 * 
	 * @param id the ID of the {@code Shape3F} type to check
	 * @return {@code true} if, and only if, this {@code DefaultShape3FReader} instance supports reading {@code Shape3F} instances with an ID of {@code id}, {@code false} otherwise
	 */
	@Override
	public boolean isSupported(final int id) {
		return this.shape3FReaders.containsKey(Integer.valueOf(id));
	}
}