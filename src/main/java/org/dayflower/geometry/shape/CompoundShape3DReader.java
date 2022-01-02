/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dayflower.geometry.Shape3D;
import org.dayflower.geometry.Shape3DReader;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code CompoundShape3DReader} is a {@link Shape3DReader} implementation that reads {@link CompoundShape3D} instances from a {@code DataInput} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompoundShape3DReader implements Shape3DReader {
	private final Shape3DReader shape3DReader;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompoundShape3DReader} instance.
	 * <p>
	 * If {@code shape3DReader} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3DReader a {@link Shape3DReader} instance
	 * @throws NullPointerException thrown if, and only if, {@code shape3DReader} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public CompoundShape3DReader(final Shape3DReader shape3DReader) {
		this.shape3DReader = Objects.requireNonNull(shape3DReader, "shape3DReader == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Reads a {@link CompoundShape3D} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code CompoundShape3D} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the ID is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return the {@code CompoundShape3D} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, the ID is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	@Override
	public CompoundShape3D read(final DataInput dataInput) {
		try {
			return read(dataInput, dataInput.readInt());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Reads a {@link CompoundShape3D} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code CompoundShape3D} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code id} is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * The ID of the {@code CompoundShape3D} instance to read has already been read from {@code dataInput} when this method is called. It is passed to this method as a parameter argument.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @param id the ID of the {@code CompoundShape3D} to read
	 * @return the {@code CompoundShape3D} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, {@code id} is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	@Override
	public CompoundShape3D read(final DataInput dataInput, final int id) {
		Objects.requireNonNull(dataInput, "dataInput == null");
		
		ParameterArguments.requireExact(id, CompoundShape3D.ID, "id");
		
		try {
			final int size = dataInput.readInt();
			
			final List<Shape3D> shapes = new ArrayList<>(size);
			
			for(int i = 0; i < size; i++) {
				shapes.add(this.shape3DReader.read(dataInput, dataInput.readInt()));
			}
			
			return new CompoundShape3D(shapes);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code id == CompoundShape3D.ID}, {@code false} otherwise.
	 * 
	 * @param id the ID to check
	 * @return {@code true} if, and only if, {@code id == CompoundShape3D.ID}, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean isSupported(final int id) {
		return id == CompoundShape3D.ID;
	}
}