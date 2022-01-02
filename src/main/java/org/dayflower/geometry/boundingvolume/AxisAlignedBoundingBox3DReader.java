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
package org.dayflower.geometry.boundingvolume;

import java.io.DataInput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.BoundingVolume3DReader;
import org.dayflower.utility.ParameterArguments;

/**
 * An {@code AxisAlignedBoundingBox3DReader} is a {@link BoundingVolume3DReader} implementation that reads {@link AxisAlignedBoundingBox3D} instances from a {@code DataInput} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class AxisAlignedBoundingBox3DReader implements BoundingVolume3DReader {
	/**
	 * Constructs a new {@code AxisAlignedBoundingBox3DReader} instance.
	 */
	public AxisAlignedBoundingBox3DReader() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Reads an {@link AxisAlignedBoundingBox3D} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code AxisAlignedBoundingBox3D} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the ID is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return the {@code AxisAlignedBoundingBox3D} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, the ID is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public AxisAlignedBoundingBox3D read(final DataInput dataInput) {
		try {
			return read(dataInput, dataInput.readInt());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Reads an {@link AxisAlignedBoundingBox3D} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code AxisAlignedBoundingBox3D} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code id} is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * The ID of the {@code AxisAlignedBoundingBox3D} instance to read has already been read from {@code dataInput} when this method is called. It is passed to this method as a parameter argument.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @param id the ID of the {@code AxisAlignedBoundingBox3D} to read
	 * @return the {@code AxisAlignedBoundingBox3D} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, {@code id} is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public AxisAlignedBoundingBox3D read(final DataInput dataInput, final int id) {
		Objects.requireNonNull(dataInput, "dataInput == null");
		
		ParameterArguments.requireExact(id, AxisAlignedBoundingBox3D.ID, "id");
		
		return new AxisAlignedBoundingBox3D(Point3D.read(dataInput), Point3D.read(dataInput));
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code id == AxisAlignedBoundingBox3D.ID}, {@code false} otherwise.
	 * 
	 * @param id the ID to check
	 * @return {@code true} if, and only if, {@code id == AxisAlignedBoundingBox3D.ID}, {@code false} otherwise
	 */
	@Override
	public boolean isSupported(final int id) {
		return id == AxisAlignedBoundingBox3D.ID;
	}
}