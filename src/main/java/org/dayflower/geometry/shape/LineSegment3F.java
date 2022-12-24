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

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
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

import org.macroing.java.lang.Floats;

/**
 * A {@code LineSegment3F} is an implementation of {@link Shape3F} that represents a line segment.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3F} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class LineSegment3F implements Shape3F {
	/**
	 * The name of this {@code LineSegment3F} class.
	 */
//	TODO: Add Unit Tests!
	public static final String NAME = "Line Segment";
	
	/**
	 * The ID of this {@code LineSegment3F} class.
	 */
//	TODO: Add Unit Tests!
	public static final int ID = 8;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final float LINE_WIDTH = Floats.PI * 0.5F / 4096.0F;
	private static final float LINE_WIDTH_COS = Floats.cos(LINE_WIDTH);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point3F a;
	private final Point3F b;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code LineSegment3F} instance given two {@link Point3F} instances, {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3F} instance
	 * @param b a {@code Point3F} instance
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public LineSegment3F(final Point3F a, final Point3F b) {
		this.a = Objects.requireNonNull(a, "a == null");
		this.b = Objects.requireNonNull(b, "b == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code LineSegment3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code LineSegment3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public BoundingVolume3F getBoundingVolume() {
		return new AxisAlignedBoundingBox3F(this.a, this.b);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code LineSegment3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code LineSegment3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		Objects.requireNonNull(ray, "ray == null");
		
//		TODO: Implement!
		return SurfaceIntersection3F.EMPTY;
	}
	
	/**
	 * Returns the {@link Point3F} instance denoted by {@code A}.
	 * 
	 * @return the {@code Point3F} instance denoted by {@code A}
	 */
//	TODO: Add Unit Tests!
	public Point3F getA() {
		return this.a;
	}
	
	/**
	 * Returns the {@link Point3F} instance denoted by {@code B}.
	 * 
	 * @return the {@code Point3F} instance denoted by {@code B}
	 */
//	TODO: Add Unit Tests!
	public Point3F getB() {
		return this.b;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code LineSegment3F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code LineSegment3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code LineSegment3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code LineSegment3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new LineSegment3F(%s, %s)", this.a, this.b);
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
				if(!this.a.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.b.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code LineSegment3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3F} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code LineSegment3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean contains(final Point3F point) {
		final Point3F p = Objects.requireNonNull(point, "point == null");
		final Point3F a = this.a;
		final Point3F b = this.b;
		
		if(p.equals(a) || p.equals(b)) {
			return true;
		}
		
		final Vector3F vP = new Vector3F(p);
		final Vector3F vA = new Vector3F(a);
		final Vector3F vB = new Vector3F(b);
		
		final Vector3F vBP = Vector3F.direction(b, p);
		final Vector3F vBA = Vector3F.direction(b, a);
		
		final float t = Vector3F.dotProduct(vBP, vBA) / Point3F.distanceSquared(a, b);
		
		final Vector3F projection = Vector3F.lerp(vB, vA, t);
		
		final boolean contains = Vector3F.dotProduct(projection, vP) / projection.length() / vP.length() >= LINE_WIDTH_COS;
		
		return contains;
	}
	
	/**
	 * Compares {@code object} to this {@code LineSegment3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code LineSegment3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code LineSegment3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code LineSegment3F}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof LineSegment3F)) {
			return false;
		} else if(!Objects.equals(this.a, LineSegment3F.class.cast(object).a)) {
			return false;
		} else if(!Objects.equals(this.b, LineSegment3F.class.cast(object).b)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code LineSegment3F} instance.
	 * <p>
	 * This method returns {@code 0.0F}.
	 * 
	 * @return the surface area of this {@code LineSegment3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public float getSurfaceArea() {
		return 0.0F;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code LineSegment3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code LineSegment3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		Objects.requireNonNull(ray, "ray == null");
		
//		TODO: Implement!
		return Float.NaN;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code LineSegment3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code LineSegment3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code LineSegment3F} instance.
	 * 
	 * @return a hash code for this {@code LineSegment3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.a, this.b);
	}
	
	/**
	 * Writes this {@code LineSegment3F} instance to {@code dataOutput}.
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
			
			this.a.write(dataOutput);
			this.b.write(dataOutput);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}