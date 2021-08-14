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

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code Polygon3F} is an implementation of {@link Shape3F} that represents a polygon.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3F} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Polygon3F implements Shape3F {
	/**
	 * The name of this {@code Polygon3F} class.
	 */
	public static final String NAME = "Polygon";
	
	/**
	 * The ID of this {@code Polygon3F} class.
	 */
	public static final int ID = 11;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point3F[] points;
	private final Vector3F surfaceNormal;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Polygon3F} instance.
	 * <p>
	 * If either {@code points} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code points.length} is less than {@code 3} or the provided {@link Point3F} instances are not coplanar, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param points the {@code Point3F} instances of this {@code Polygon3F} instance
	 * @throws IllegalArgumentException thrown if, and only if, {@code points.length} is less than {@code 3} or the provided {@code Point3F} instances are not coplanar
	 * @throws NullPointerException thrown if, and only if, either {@code points} or at least one of its elements are {@code null}
	 */
	public Polygon3F(final Point3F... points) {
		this.points = doRequireValidPoints(points);
		this.surfaceNormal = Vector3F.normalNormalized(this.points[0], this.points[1], this.points[2]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code Polygon3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code Polygon3F} instance
	 */
	@Override
	public BoundingVolume3F getBoundingVolume() {
		return AxisAlignedBoundingBox3F.fromPoints(this.points);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Polygon3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Polygon3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		Objects.requireNonNull(ray, "ray == null");
		
//		TODO: Implement!
		return SurfaceIntersection3F.EMPTY;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Polygon3F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Polygon3F} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Polygon3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Polygon3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Polygon3F(%s)", Point3F.toString(this.points));
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
	@Override
	public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
		Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
		
		try {
			if(nodeHierarchicalVisitor.visitEnter(this)) {
				for(final Point3F point : this.points) {
					if(!point.accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
				}
				
				if(!this.surfaceNormal.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code Polygon3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Polygon3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Polygon3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Polygon3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Polygon3F)) {
			return false;
		} else if(!Arrays.equals(this.points, Polygon3F.class.cast(object).points)) {
			return false;
		} else if(!Objects.equals(this.surfaceNormal, Polygon3F.class.cast(object).surfaceNormal)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code Polygon3F} instance.
	 * 
	 * @return the surface area of this {@code Polygon3F} instance
	 */
	@Override
	public float getSurfaceArea() {
//		TODO: Implement!
		return 0.0F;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Polygon3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Polygon3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		Objects.requireNonNull(ray, "ray == null");
		
//		TODO: Implement!
		return Float.NaN;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Polygon3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Polygon3F} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Polygon3F} instance.
	 * 
	 * @return a hash code for this {@code Polygon3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(Arrays.hashCode(this.points)), this.surfaceNormal);
	}
	
	/**
	 * Writes this {@code Polygon3F} instance to {@code dataOutput}.
	 * <p>
	 * If {@code dataOutput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataOutput the {@code DataOutput} instance to write to
	 * @throws NullPointerException thrown if, and only if, {@code dataOutput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public void write(final DataOutput dataOutput) {
		try {
			dataOutput.writeInt(ID);
			dataOutput.writeInt(this.points.length);
			
			for(final Point3F point : this.points) {
				point.write(dataOutput);
			}
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point3F[] doRequireValidPoints(final Point3F[] points) {
		ParameterArguments.requireNonNullArray(points, "points");
		ParameterArguments.requireRange(points.length, 3, Integer.MAX_VALUE, "points.length");
		
		if(Point3F.coplanar(points)) {
			return points.clone();
		}
		
		throw new IllegalArgumentException("The provided Point3F instances are not coplanar.");
	}
}