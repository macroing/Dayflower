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
package org.dayflower.geometry;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;

import org.dayflower.node.Node;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code Ray3D} represents a 3-dimensional ray with a point of type {@link Point3D} and a vector of type {@link Vector3D}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Ray3D implements Node {
	private final Point3D origin;
	private final Vector3D direction;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Ray3D} instance given {@code origin} and {@code direction}.
	 * <p>
	 * If either {@code origin} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param origin a {@link Point3D} instance to represent the origin
	 * @param direction a {@link Vector3D} instance to represent the direction
	 * @throws NullPointerException thrown if, and only if, either {@code origin} or {@code direction} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Ray3D(final Point3D origin, final Vector3D direction) {
		this.origin = Objects.requireNonNull(origin, "origin == null");
		this.direction = Vector3D.normalize(Objects.requireNonNull(direction, "direction == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link Point3D} instance used by this {@code Ray3D} instance to represent its origin.
	 * 
	 * @return the {@code Point3D} instance used by this {@code Ray3D} instance to represent its origin
	 */
//	TODO: Add Unit Tests!
	public Point3D getOrigin() {
		return this.origin;
	}
	
	/**
	 * Returns a {@link Point3D} instance given the parametric distance {@code t}.
	 * 
	 * @param t the parametric distance
	 * @return a {@code Point3D} instance given the parametric distance {@code t}
	 */
//	TODO: Add Unit Tests!
	public Point3D getPointAt(final double t) {
		return Point3D.add(this.origin, this.direction, t);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Ray3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Ray3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new Ray3D(%s, %s)", this.origin, this.direction);
	}
	
	/**
	 * Returns the {@link Vector3D} instance used by this {@code Ray3D} instance to represent its direction.
	 * 
	 * @return the {@code Vector3D} instance used by this {@code Ray3D} instance to represent its direction
	 */
//	TODO: Add Unit Tests!
	public Vector3D getDirection() {
		return this.direction;
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
				if(!this.origin.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.direction.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code Ray3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Ray3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Ray3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Ray3D}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Ray3D)) {
			return false;
		} else if(!Objects.equals(this.origin, Ray3D.class.cast(object).origin)) {
			return false;
		} else if(!Objects.equals(this.direction, Ray3D.class.cast(object).direction)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code Ray3D} instance.
	 * 
	 * @return a hash code for this {@code Ray3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.origin, this.direction);
	}
	
	/**
	 * Writes this {@code Ray3D} instance to {@code dataOutput}.
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
	public void write(final DataOutput dataOutput) {
		this.origin.write(dataOutput);
		this.direction.write(dataOutput);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code Ray3D} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Ray3D} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	public static Ray3D read(final DataInput dataInput) {
		return new Ray3D(Point3D.read(dataInput), Vector3D.read(dataInput));
	}
	
	/**
	 * Transforms the {@code Ray3D} {@code rayRHS} with the {@link Matrix44D} {@code matrixLHS}.
	 * <p>
	 * Returns a new {@code Ray3D} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code rayRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS a {@code Matrix44D} instance
	 * @param rayRHS a {@code Ray3D} instance
	 * @return a new {@code Ray3D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code rayRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Ray3D transform(final Matrix44D matrixLHS, final Ray3D rayRHS) {
		final Point3D originOld = rayRHS.origin;
		final Point3D originNew = Point3D.transformAndDivide(matrixLHS, originOld);
		
		final Vector3D directionOld = rayRHS.direction;
		final Vector3D directionNew = Vector3D.transform(matrixLHS, directionOld);
		
		final Vector3D originError = Vector3D.transformError(matrixLHS, originOld);
		
		final double lengthSquared = directionNew.lengthSquared();
		
		if(lengthSquared > 0.0D) {
			final double t = Vector3D.dotProduct(Vector3D.absolute(directionNew), originError);
			
			return new Ray3D(Point3D.add(originNew, directionNew, t), directionNew);
		}
		
		return new Ray3D(originNew, directionNew);
	}
}