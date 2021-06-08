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

import static org.dayflower.utility.Doubles.PI;
import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_4;
import static org.dayflower.utility.Doubles.equal;
import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.max;
import static org.dayflower.utility.Doubles.pow;
import static org.dayflower.utility.Doubles.solveQuadraticSystem;
import static org.dayflower.utility.Doubles.sqrt;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

import org.dayflower.geometry.BoundingVolume3D;
import org.dayflower.geometry.Matrix44D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.Vector3D;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code BoundingSphere3D} denotes a 3-dimensional bounding sphere that uses the data type {@code double}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BoundingSphere3D implements BoundingVolume3D {
	/**
	 * The length of the {@code double[]}.
	 */
	public static final int ARRAY_LENGTH = 4;
	
	/**
	 * The offset for the {@link Point3D} instance representing the center in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_CENTER = 0;
	
	/**
	 * The offset for the radius in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_RADIUS = 3;
	
	/**
	 * The ID of this {@code BoundingSphere3D} class.
	 */
	public static final int ID = 2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point3D center;
	private final double radius;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code BoundingSphere3D} instance with a radius of {@code 1.0D} and a center of {@code new Point3D()}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BoundingSphere3D(1.0D);
	 * }
	 * </pre>
	 */
	public BoundingSphere3D() {
		this(1.0D);
	}
	
	/**
	 * Constructs a new {@code BoundingSphere3D} instance with a radius of {@code radius} and a center of {@code new Point3D()}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BoundingSphere3D(radius, new Point3D());
	 * }
	 * </pre>
	 * 
	 * @param radius the radius of this {@code BoundingSphere3D} instance
	 */
	public BoundingSphere3D(final double radius) {
		this(radius, new Point3D());
	}
	
	/**
	 * Constructs a new {@code BoundingSphere3D} instance with a radius of {@code radius} and a center of {@code center}.
	 * <p>
	 * If {@code center} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param radius the radius of this {@code BoundingSphere3D} instance
	 * @param center the center of this {@code BoundingSphere3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code center} is {@code null}
	 */
	public BoundingSphere3D(final double radius, final Point3D center) {
		this.radius = radius;
		this.center = Point3D.getCached(Objects.requireNonNull(center, "center == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Performs a transformation.
	 * <p>
	 * Returns a new {@code BoundingSphere3D} instance with the result of the transformation.
	 * <p>
	 * If {@code matrix} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrix the {@link Matrix44D} instance to perform the transformation with
	 * @return a new {@code BoundingSphere3D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, {@code matrix} is {@code null}
	 */
	@Override
	public BoundingSphere3D transform(final Matrix44D matrix) {
		final Point3D center = Point3D.transformAndDivide(matrix, this.center);
		final Point3D x = Point3D.transformAndDivide(matrix, new Point3D(this.center.getX() + this.radius, this.center.getY(), this.center.getZ()));
		final Point3D y = Point3D.transformAndDivide(matrix, new Point3D(this.center.getX(), this.center.getY() + this.radius, this.center.getZ()));
		final Point3D z = Point3D.transformAndDivide(matrix, new Point3D(this.center.getX(), this.center.getY(), this.center.getZ() + this.radius));
		
		final double distanceSquaredFromCenterToX = Point3D.distanceSquared(center, x);
		final double distanceSquaredFromCenterToY = Point3D.distanceSquared(center, y);
		final double distanceSquaredFromCenterToZ = Point3D.distanceSquared(center, z);
		final double distanceSquared = max(distanceSquaredFromCenterToX, distanceSquaredFromCenterToY, distanceSquaredFromCenterToZ);
		
		final double radius = sqrt(distanceSquared);
		
		return new BoundingSphere3D(radius, center);
	}
	
	/**
	 * Returns the center of this {@code BoundingSphere3D} instance.
	 * 
	 * @return the center of this {@code BoundingSphere3D} instance
	 */
	public Point3D getCenter() {
		return this.center;
	}
	
	/**
	 * Returns a {@link Point3D} instance that represents the closest point to {@code point} and is contained in this {@code BoundingSphere3D} instance.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@code Point3D} instance
	 * @return a {@code Point3D} instance that represents the closest point to {@code point} and is contained in this {@code BoundingSphere3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public Point3D getClosestPointTo(final Point3D point) {
		final Point3D surfaceIntersectionPoint = Point3D.add(this.center, Vector3D.directionNormalized(this.center, point), this.radius);
		
		final double distanceToSurfaceIntersectionPointSquared = Point3D.distanceSquared(this.center, surfaceIntersectionPoint);
		final double distanceToPSquared = Point3D.distanceSquared(this.center, point);
		
		return distanceToSurfaceIntersectionPointSquared <= distanceToPSquared ? surfaceIntersectionPoint : point;
	}
	
	/**
	 * Returns a {@link Point3D} with the largest component values that are contained in this {@code BoundingSphere3D} instance.
	 * 
	 * @return a {@code Point3D} with the largest component values that are contained in this {@code BoundingSphere3D} instance
	 */
	@Override
	public Point3D getMaximum() {
		return Point3D.add(this.center, this.radius);
	}
	
	/**
	 * Returns a {@link Point3D} with the smallest component values that are contained in this {@code BoundingSphere3D} instance.
	 * 
	 * @return a {@code Point3D} with the smallest component values that are contained in this {@code BoundingSphere3D} instance
	 */
	@Override
	public Point3D getMinimum() {
		return Point3D.subtract(this.center, this.radius);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code BoundingSphere3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code BoundingSphere3D} instance
	 */
	@Override
	public String toString() {
		return String.format("new BoundingSphere3D(%+.10f, %s)", Double.valueOf(this.radius), this.center);
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
				if(!this.center.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code BoundingSphere3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3D} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code BoundingSphere3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public boolean contains(final Point3D point) {
		return Point3D.distanceSquared(this.center, point) < this.radius * this.radius;
	}
	
	/**
	 * Compares {@code object} to this {@code BoundingSphere3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code BoundingSphere3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code BoundingSphere3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code BoundingSphere3D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof BoundingSphere3D)) {
			return false;
		} else if(!Objects.equals(this.center, BoundingSphere3D.class.cast(object).center)) {
			return false;
		} else if(!equal(this.radius, BoundingSphere3D.class.cast(object).radius)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the radius of this {@code BoundingSphere3D} instance.
	 * 
	 * @return the radius of this {@code BoundingSphere3D} instance
	 */
	public double getRadius() {
		return this.radius;
	}
	
	/**
	 * Returns the squared radius of this {@code BoundingSphere3D} instance.
	 * 
	 * @return the squared radius of this {@code BoundingSphere3D} instance
	 */
	public double getRadiusSquared() {
		return this.radius * this.radius;
	}
	
	/**
	 * Returns the surface area of this {@code BoundingSphere3D} instance.
	 * 
	 * @return the surface area of this {@code BoundingSphere3D} instance
	 */
	@Override
	public double getSurfaceArea() {
		return PI_MULTIPLIED_BY_4 * getRadiusSquared();
	}
	
	/**
	 * Returns the volume of this {@code BoundingSphere3D} instance.
	 * 
	 * @return the volume of this {@code BoundingSphere3D} instance
	 */
	@Override
	public double getVolume() {
		return 4.0D / 3.0D * PI * pow(this.radius, 3.0D);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code BoundingSphere3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance from {@code ray} to this {@code BoundingSphere3D} instance, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code BoundingSphere3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance from {@code ray} to this {@code BoundingSphere3D} instance, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public double intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Point3D origin = ray.getOrigin();
		final Point3D center = getCenter();
		
		final Vector3D direction = ray.getDirection();
		final Vector3D centerToOrigin = Vector3D.direction(center, origin);
		
		final double radiusSquared = getRadiusSquared();
		
		final double a = direction.lengthSquared();
		final double b = 2.0D * Vector3D.dotProduct(centerToOrigin, direction);
		final double c = centerToOrigin.lengthSquared() - radiusSquared;
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		final double t0 = ts[0];
		final double t1 = ts[1];
		
		final double t = !isNaN(t0) && t0 > tMinimum && t0 < tMaximum ? t0 : !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Double.NaN;
		
		return t;
	}
	
	/**
	 * Returns a {@code double[]} representation of this {@code BoundingSphere3D} instance.
	 * 
	 * @return a {@code double[]} representation of this {@code BoundingSphere3D} instance
	 */
	public double[] toArray() {
		final double[] array = new double[ARRAY_LENGTH];
		
		array[ARRAY_OFFSET_CENTER + 0] = this.center.getX();
		array[ARRAY_OFFSET_CENTER + 1] = this.center.getY();
		array[ARRAY_OFFSET_CENTER + 2] = this.center.getZ();
		array[ARRAY_OFFSET_RADIUS] = this.radius;
		
		return array;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code BoundingSphere3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code BoundingSphere3D} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code BoundingSphere3D} instance.
	 * 
	 * @return a hash code for this {@code BoundingSphere3D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.center, Double.valueOf(this.radius));
	}
	
	/**
	 * Writes this {@code BoundingSphere3D} instance to {@code dataOutput}.
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
			dataOutput.writeDouble(this.radius);
			
			this.center.write(dataOutput);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}