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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.Shape3FReader;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code CompoundShape3FReader} is a {@link Shape3FReader} implementation that reads {@link CompoundShape3F} instances from a {@code DataInput} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompoundShape3FReader implements Shape3FReader {
	private final Shape3FReader shape3FReader;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompoundShape3FReader} instance.
	 * <p>
	 * If {@code shape3FReader} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FReader a {@link Shape3FReader} instance
	 * @throws NullPointerException thrown if, and only if, {@code shape3FReader} is {@code null}
	 */
	public CompoundShape3FReader(final Shape3FReader shape3FReader) {
		this.shape3FReader = Objects.requireNonNull(shape3FReader, "shape3FReader == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Reads a {@link CompoundShape3F} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code CompoundShape3F} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the ID is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return the {@code CompoundShape3F} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, the ID is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public CompoundShape3F read(final DataInput dataInput) {
		try {
			return read(dataInput, dataInput.readInt());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Reads a {@link CompoundShape3F} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code CompoundShape3F} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code id} is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * The ID of the {@code CompoundShape3F} instance to read has already been read from {@code dataInput} when this method is called. It is passed to this method as a parameter argument.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @param id the ID of the {@code CompoundShape3F} to read
	 * @return the {@code CompoundShape3F} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, {@code id} is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public CompoundShape3F read(final DataInput dataInput, final int id) {
		ParameterArguments.requireExact(id, CompoundShape3F.ID, "id");
		
		try {
			final int size = dataInput.readInt();
			
			final List<Shape3F> shapes = new ArrayList<>(size);
			
			for(int i = 0; i < size; i++) {
				shapes.add(this.shape3FReader.read(dataInput, dataInput.readInt()));
			}
			
			return new CompoundShape3F(shapes);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code id == CompoundShape3F.ID}, {@code false} otherwise.
	 * 
	 * @param id the ID to check
	 * @return {@code true} if, and only if, {@code id == CompoundShape3F.ID}, {@code false} otherwise
	 */
	@Override
	public boolean isSupported(final int id) {
		return id == CompoundShape3F.ID;
	}
}