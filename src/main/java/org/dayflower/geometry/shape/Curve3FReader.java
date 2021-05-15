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

import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Shape3FReader;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.shape.Curve3F.Data;
import org.dayflower.geometry.shape.Curve3F.Type;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code Curve3FReader} is a {@link Shape3FReader} implementation that reads {@link Curve3F} instances from a {@code DataInput} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Curve3FReader implements Shape3FReader {
	/**
	 * Constructs a new {@code Curve3FReader} instance.
	 */
	public Curve3FReader() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Reads a {@link Curve3F} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code Curve3F} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the ID is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return the {@code Curve3F} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, the ID is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public Curve3F read(final DataInput dataInput) {
		try {
			return read(dataInput, dataInput.readInt());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Reads a {@link Curve3F} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code Curve3F} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code id} is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * The ID of the {@code Curve3F} instance to read has already been read from {@code dataInput} when this method is called. It is passed to this method as a parameter argument.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @param id the ID of the {@code Curve3F} to read
	 * @return the {@code Curve3F} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, {@code id} is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public Curve3F read(final DataInput dataInput, final int id) {
		ParameterArguments.requireExact(id, Curve3F.ID, "id");
		
		try {
			final Point3F pointA = Point3F.read(dataInput);
			final Point3F pointB = Point3F.read(dataInput);
			final Point3F pointC = Point3F.read(dataInput);
			final Point3F pointD = Point3F.read(dataInput);
			
			final Type type = Type.values()[dataInput.readInt()];
			
			final Vector3F normalA = Vector3F.read(dataInput);
			final Vector3F normalB = Vector3F.read(dataInput);
			
			final float widthA = dataInput.readFloat();
			final float widthB = dataInput.readFloat();
			
			final Data data = new Data(pointA, pointB, pointC, pointD, type, normalA, normalB, widthA, widthB);
			
			final float uMinimum = dataInput.readFloat();
			final float uMaximum = dataInput.readFloat();
			
			return new Curve3F(data, uMinimum, uMaximum);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code id == Curve3F.ID}, {@code false} otherwise.
	 * 
	 * @param id the ID to check
	 * @return {@code true} if, and only if, {@code id == Curve3F.ID}, {@code false} otherwise
	 */
	@Override
	public boolean isSupported(final int id) {
		return id == Curve3F.ID;
	}
}