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

import static org.dayflower.utility.Doubles.minOrNaN;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.Shape3D;
import org.dayflower.geometry.SurfaceIntersection3D;
import org.dayflower.geometry.SurfaceIntersector3D;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3D;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.utility.ParameterArguments;

import org.macroing.java.lang.Doubles;

/**
 * A {@code CompoundShape3D} is an implementation of {@link Shape3D} that contains a list of {@code Shape3D} instances.
 * <p>
 * This class is immutable and thread-safe if its {@code Shape3D} instances are.
 * <p>
 * This {@code Shape3D} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompoundShape3D implements Shape3D {
	/**
	 * The name of this {@code CompoundShape3D} class.
	 */
//	TODO: Add Unit Tests!
	public static final String NAME = "Compound Shape";
	
	/**
	 * The ID of this {@code CompoundShape3D} class.
	 */
//	TODO: Add Unit Tests!
	public static final int ID = 1;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final List<? extends Shape3D> shapes;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompoundShape3D} instance.
	 * <p>
	 * If either {@code shapes} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Modifying {@code shapes} will not affect this {@code CompoundShape3D} instance.
	 * 
	 * @param shapes a {@code List} of {@link Shape3D} instances
	 * @throws NullPointerException thrown if, and only if, either {@code shapes} or at least one of its elements are {@code null}
	 */
//	TODO: Add Unit Tests!
	public CompoundShape3D(final List<? extends Shape3D> shapes) {
		this.shapes = new ArrayList<>(ParameterArguments.requireNonNullList(shapes, "shapes"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3D} instance that contains this {@code CompoundShape3D} instance.
	 * 
	 * @return a {@code BoundingVolume3D} instance that contains this {@code CompoundShape3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public BoundingVolume3D getBoundingVolume() {
		return doGetAxisAlignedBoundingBoxes().stream().reduce((a, b) -> AxisAlignedBoundingBox3D.union(a, b)).orElse(new AxisAlignedBoundingBox3D(new Point3D(), new Point3D()));
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code CompoundShape3D} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3D} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code CompoundShape3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3D} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final SurfaceIntersector3D surfaceIntersector = new SurfaceIntersector3D(ray, tMinimum, tMaximum);
		
		for(final Shape3D shape : this.shapes) {
			surfaceIntersector.intersection(shape);
		}
		
		return surfaceIntersector.computeSurfaceIntersection();
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code CompoundShape3D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code CompoundShape3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code CompoundShape3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code CompoundShape3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return "new CompoundShape3D(...)";
	}
	
	/**
	 * Accepts a {@link NodeHierarchicalVisitor}.
	 * <p>
	 * Returns the result of {@code nodeHierarchicalVisitor.visitLeave(this)}.
	 * <p>
	 * If {@code nodeHierarchicalVisitor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}, a {@code NodeTraversalException} will be thrown with the {@code RuntimeException} wrapped.
	 * <p>
	 * This implementation will:
	 * <ul>
	 * <li>throw a {@code NullPointerException} if {@code nodeHierarchicalVisitor} is {@code null}.</li>
	 * <li>throw a {@code NodeTraversalException} if {@code nodeHierarchicalVisitor} throws a {@code RuntimeException}.</li>
	 * <li>traverse its child {@code Node} instances.</li>
	 * </ul>
	 * 
	 * @param nodeHierarchicalVisitor the {@code NodeHierarchicalVisitor} to accept
	 * @return the result of {@code nodeHierarchicalVisitor.visitLeave(this)}
	 * @throws NodeTraversalException thrown if, and only if, a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}
	 * @throws NullPointerException thrown if, and only if, {@code nodeHierarchicalVisitor} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
		Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
		
		try {
			if(nodeHierarchicalVisitor.visitEnter(this)) {
				for(final Shape3D shape : this.shapes) {
					if(!shape.accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code CompoundShape3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3D} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code CompoundShape3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean contains(final Point3D point) {
		Objects.requireNonNull(point, "point == null");
		
		for(final Shape3D shape : this.shapes) {
			if(shape.contains(point)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Compares {@code object} to this {@code CompoundShape3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code CompoundShape3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code CompoundShape3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code CompoundShape3D}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof CompoundShape3D)) {
			return false;
		} else if(!Objects.equals(this.shapes, CompoundShape3D.class.cast(object).shapes)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code CompoundShape3D} instance.
	 * 
	 * @return the surface area of this {@code CompoundShape3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public double getSurfaceArea() {
		double surfaceArea = 0.0D;
		
		for(final Shape3D shape : this.shapes) {
			surfaceArea += shape.getSurfaceArea();
		}
		
		return surfaceArea;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code CompoundShape3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code CompoundShape3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		double t = Double.NaN;
		double tMax = tMaximum;
		double tMin = tMinimum;
		
		for(final Shape3D shape : this.shapes) {
			t = minOrNaN(t, shape.intersectionT(ray, tMin, tMax));
			
			if(!Doubles.isNaN(t)) {
				tMax = t;
			}
		}
		
		return t;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code CompoundShape3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code CompoundShape3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code CompoundShape3D} instance.
	 * 
	 * @return a hash code for this {@code CompoundShape3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.shapes);
	}
	
	/**
	 * Writes this {@code CompoundShape3D} instance to {@code dataOutput}.
	 * <p>
	 * If {@code dataOutput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataOutput the {@code DataOutput} instance to write to
	 * @throws NullPointerException thrown if, and only if, {@code dataOutput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	@Override
	public void write(final DataOutput dataOutput) {
		try {
			dataOutput.writeInt(ID);
			dataOutput.writeInt(this.shapes.size());
			
			for(final Shape3D shape : this.shapes) {
				shape.write(dataOutput);
			}
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private List<AxisAlignedBoundingBox3D> doGetAxisAlignedBoundingBoxes() {
		return doGetBoundingVolumes().stream().filter(boundingVolume -> boundingVolume instanceof AxisAlignedBoundingBox3D).map(boundingVolume -> AxisAlignedBoundingBox3D.class.cast(boundingVolume)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}
	
	private List<BoundingVolume3D> doGetBoundingVolumes() {
		return this.shapes.stream().map(shape -> shape.getBoundingVolume()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}
}