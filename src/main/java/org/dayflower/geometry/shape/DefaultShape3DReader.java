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

import org.dayflower.geometry.Shape3D;
import org.dayflower.geometry.Shape3DReader;

/**
 * A {@code DefaultShape3DReader} is a {@link Shape3DReader} implementation that reads all official {@link Shape3D} instances from a {@code DataInput} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DefaultShape3DReader implements Shape3DReader {
	private final Map<Integer, Shape3DReader> shape3DReaders;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DefaultShape3DReader} instance.
	 */
	public DefaultShape3DReader() {
		this.shape3DReaders = new LinkedHashMap<>();
		this.shape3DReaders.put(Integer.valueOf(Cone3D.ID), new Cone3DReader());
		this.shape3DReaders.put(Integer.valueOf(ConstructiveSolidGeometry3D.ID), new ConstructiveSolidGeometry3DReader(this));
		this.shape3DReaders.put(Integer.valueOf(Curve3D.ID), new Curve3DReader());
		this.shape3DReaders.put(Integer.valueOf(Curves3D.ID), new Curves3DReader());
		this.shape3DReaders.put(Integer.valueOf(Cylinder3D.ID), new Cylinder3DReader());
		this.shape3DReaders.put(Integer.valueOf(Disk3D.ID), new Disk3DReader());
		this.shape3DReaders.put(Integer.valueOf(Hyperboloid3D.ID), new Hyperboloid3DReader());
		this.shape3DReaders.put(Integer.valueOf(Paraboloid3D.ID), new Paraboloid3DReader());
		this.shape3DReaders.put(Integer.valueOf(Plane3D.ID), new Plane3DReader());
		this.shape3DReaders.put(Integer.valueOf(ProceduralTerrain3D.ID), new ProceduralTerrain3DReader());
		this.shape3DReaders.put(Integer.valueOf(Rectangle3D.ID), new Rectangle3DReader());
		this.shape3DReaders.put(Integer.valueOf(RectangularCuboid3D.ID), new RectangularCuboid3DReader());
		this.shape3DReaders.put(Integer.valueOf(Sphere3D.ID), new Sphere3DReader());
		this.shape3DReaders.put(Integer.valueOf(Torus3D.ID), new Torus3DReader());
		this.shape3DReaders.put(Integer.valueOf(Triangle3D.ID), new Triangle3DReader());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Reads a {@link Shape3D} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code Shape3D} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If the ID is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return the {@code Shape3D} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, the ID is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public Shape3D read(final DataInput dataInput) {
		try {
			return read(dataInput, dataInput.readInt());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Reads a {@link Shape3D} instance from {@code dataInput}.
	 * <p>
	 * Returns the {@code Shape3D} instance that was read.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code id} is invalid, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * The ID of the {@code Shape3D} instance to read has already been read from {@code dataInput} when this method is called. It is passed to this method as a parameter argument.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @param id the ID of the {@code Shape3D} type to read
	 * @return the {@code Shape3D} instance that was read
	 * @throws IllegalArgumentException thrown if, and only if, {@code id} is invalid
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public Shape3D read(final DataInput dataInput, final int id) {
		switch(id) {
			case Cone3D.ID:
			case ConstructiveSolidGeometry3D.ID:
			case Curve3D.ID:
			case Curves3D.ID:
			case Cylinder3D.ID:
			case Disk3D.ID:
			case Hyperboloid3D.ID:
			case Paraboloid3D.ID:
			case Plane3D.ID:
			case ProceduralTerrain3D.ID:
			case Rectangle3D.ID:
			case RectangularCuboid3D.ID:
			case Sphere3D.ID:
			case Torus3D.ID:
			case Triangle3D.ID:
				return this.shape3DReaders.get(Integer.valueOf(id)).read(dataInput, id);
			default:
				throw new IllegalArgumentException(String.format("The ID %d is invalid.", Integer.valueOf(id)));
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code DefaultShape3DReader} instance supports reading {@link Shape3D} instances with an ID of {@code id}, {@code false} otherwise.
	 * 
	 * @param id the ID of the {@code Shape3D} type to check
	 * @return {@code true} if, and only if, this {@code DefaultShape3DReader} instance supports reading {@code Shape3D} instances with an ID of {@code id}, {@code false} otherwise
	 */
	@Override
	public boolean isSupported(final int id) {
		return this.shape3DReaders.containsKey(Integer.valueOf(id));
	}
}