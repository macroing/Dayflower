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
package org.dayflower.geometry.boundingvolume;

import java.io.DataInput;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.BoundingVolume3FReader;
import org.dayflower.utility.ParameterArguments;

/**
 * An {@code AxisAlignedBoundingBox3FReader} is a {@link BoundingVolume3FReader} implementation that reads {@link AxisAlignedBoundingBox3F} instances from a {@code DataInput} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class AxisAlignedBoundingBox3FReader implements BoundingVolume3FReader {
	/**
	 * Constructs a new {@code AxisAlignedBoundingBox3FReader} instance.
	 */
	public AxisAlignedBoundingBox3FReader() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Reads an {@link AxisAlignedBoundingBox3F} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code AxisAlignedBoundingBox3F} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the ID is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return the {@code AxisAlignedBoundingBox3F} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, the ID is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public AxisAlignedBoundingBox3F read(final DataInput dataInput) {
		try {
			return read(dataInput, dataInput.readInt());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Reads an {@link AxisAlignedBoundingBox3F} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code AxisAlignedBoundingBox3F} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code id} is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * The ID of the {@code AxisAlignedBoundingBox3F} instance to read has already been read from {@code dataInput} when this method is called. It is passed to this method as a parameter argument.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @param id the ID of the {@code AxisAlignedBoundingBox3F} to read
	 * @return the {@code AxisAlignedBoundingBox3F} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, {@code id} is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public AxisAlignedBoundingBox3F read(final DataInput dataInput, final int id) {
		ParameterArguments.requireExact(id, AxisAlignedBoundingBox3F.ID, "id");
		
		return new AxisAlignedBoundingBox3F(Point3F.read(dataInput), Point3F.read(dataInput));
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code id == AxisAlignedBoundingBox3F.ID}, {@code false} otherwise.
	 * 
	 * @param id the ID to check
	 * @return {@code true} if, and only if, {@code id == AxisAlignedBoundingBox3F.ID}, {@code false} otherwise
	 */
	@Override
	public boolean isSupported(final int id) {
		return id == AxisAlignedBoundingBox3F.ID;
	}
}