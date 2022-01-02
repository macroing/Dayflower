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
import java.util.Objects;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.Shape3FReader;
import org.dayflower.geometry.shape.ConstructiveSolidGeometry3F.Operation;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code ConstructiveSolidGeometry3FReader} is a {@link Shape3FReader} implementation that reads {@link ConstructiveSolidGeometry3F} instances from a {@code DataInput} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ConstructiveSolidGeometry3FReader implements Shape3FReader {
	private final Shape3FReader shape3FReader;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ConstructiveSolidGeometry3FReader} instance.
	 * <p>
	 * If {@code shape3FReader} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FReader a {@link Shape3FReader} instance
	 * @throws NullPointerException thrown if, and only if, {@code shape3FReader} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public ConstructiveSolidGeometry3FReader(final Shape3FReader shape3FReader) {
		this.shape3FReader = Objects.requireNonNull(shape3FReader, "shape3FReader == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Reads a {@link ConstructiveSolidGeometry3F} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code ConstructiveSolidGeometry3F} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the ID is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return the {@code ConstructiveSolidGeometry3F} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, the ID is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	@Override
	public ConstructiveSolidGeometry3F read(final DataInput dataInput) {
		try {
			return read(dataInput, dataInput.readInt());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Reads a {@link ConstructiveSolidGeometry3F} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code ConstructiveSolidGeometry3F} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code id} is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * The ID of the {@code ConstructiveSolidGeometry3F} instance to read has already been read from {@code dataInput} when this method is called. It is passed to this method as a parameter argument.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @param id the ID of the {@code ConstructiveSolidGeometry3F} to read
	 * @return the {@code ConstructiveSolidGeometry3F} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, {@code id} is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	@Override
	public ConstructiveSolidGeometry3F read(final DataInput dataInput, final int id) {
		Objects.requireNonNull(dataInput, "dataInput == null");
		
		ParameterArguments.requireExact(id, ConstructiveSolidGeometry3F.ID, "id");
		
		try {
			final Operation operation = Operation.values()[dataInput.readInt()];
			
			final Shape3F shapeL = this.shape3FReader.read(dataInput);
			final Shape3F shapeR = this.shape3FReader.read(dataInput);
			
			final Matrix44F shapeLToObject = Matrix44F.read(dataInput);
			final Matrix44F shapeRToObject = Matrix44F.read(dataInput);
			
			return new ConstructiveSolidGeometry3F(operation, shapeL, shapeR, shapeLToObject, shapeRToObject);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code id == ConstructiveSolidGeometry3F.ID}, {@code false} otherwise.
	 * 
	 * @param id the ID to check
	 * @return {@code true} if, and only if, {@code id == ConstructiveSolidGeometry3F.ID}, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean isSupported(final int id) {
		return id == ConstructiveSolidGeometry3F.ID;
	}
}