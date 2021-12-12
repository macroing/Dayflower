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
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

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
		this.shape3FReaders.put(Integer.valueOf(CompoundShape3F.ID), new CompoundShape3FReader(this));
		this.shape3FReaders.put(Integer.valueOf(Cone3F.ID), new Cone3FReader());
		this.shape3FReaders.put(Integer.valueOf(ConstructiveSolidGeometry3F.ID), new ConstructiveSolidGeometry3FReader(this));
		this.shape3FReaders.put(Integer.valueOf(Curve3F.ID), new Curve3FReader());
		this.shape3FReaders.put(Integer.valueOf(Cylinder3F.ID), new Cylinder3FReader());
		this.shape3FReaders.put(Integer.valueOf(Disk3F.ID), new Disk3FReader());
		this.shape3FReaders.put(Integer.valueOf(Hyperboloid3F.ID), new Hyperboloid3FReader());
		this.shape3FReaders.put(Integer.valueOf(LineSegment3F.ID), new LineSegment3FReader());
		this.shape3FReaders.put(Integer.valueOf(Paraboloid3F.ID), new Paraboloid3FReader());
		this.shape3FReaders.put(Integer.valueOf(Plane3F.ID), new Plane3FReader());
		this.shape3FReaders.put(Integer.valueOf(Polygon3F.ID), new Polygon3FReader());
		this.shape3FReaders.put(Integer.valueOf(ProceduralTerrain3F.ID), new ProceduralTerrain3FReader());
		this.shape3FReaders.put(Integer.valueOf(Rectangle3F.ID), new Rectangle3FReader());
		this.shape3FReaders.put(Integer.valueOf(RectangularCuboid3F.ID), new RectangularCuboid3FReader());
		this.shape3FReaders.put(Integer.valueOf(Sphere3F.ID), new Sphere3FReader());
		this.shape3FReaders.put(Integer.valueOf(Torus3F.ID), new Torus3FReader());
		this.shape3FReaders.put(Integer.valueOf(Triangle3F.ID), new Triangle3FReader());
		this.shape3FReaders.put(Integer.valueOf(TriangleMesh3F.ID), new TriangleMesh3FReader());
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
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
	@Override
	public Shape3F read(final DataInput dataInput, final int id) {
		Objects.requireNonNull(dataInput, "dataInput == null");
		
		switch(id) {
			case Cone3F.ID:
			case ConstructiveSolidGeometry3F.ID:
			case Curve3F.ID:
			case CompoundShape3F.ID:
			case Cylinder3F.ID:
			case Disk3F.ID:
			case Hyperboloid3F.ID:
			case LineSegment3F.ID:
			case Paraboloid3F.ID:
			case Plane3F.ID:
			case Polygon3F.ID:
			case ProceduralTerrain3F.ID:
			case Rectangle3F.ID:
			case RectangularCuboid3F.ID:
			case Sphere3F.ID:
			case Torus3F.ID:
			case Triangle3F.ID:
			case TriangleMesh3F.ID:
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
//	TODO: Add Unit Tests!
	@Override
	public boolean isSupported(final int id) {
		return this.shape3FReaders.containsKey(Integer.valueOf(id));
	}
}