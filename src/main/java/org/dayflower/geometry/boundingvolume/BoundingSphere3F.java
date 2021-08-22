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

import static org.dayflower.utility.Floats.PI;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_4;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.isNaN;
import static org.dayflower.utility.Floats.max;
import static org.dayflower.utility.Floats.pow;
import static org.dayflower.utility.Floats.solveQuadraticSystem;
import static org.dayflower.utility.Floats.sqrt;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code BoundingSphere3F} is an implementation of {@link BoundingVolume3F} that represents a bounding sphere.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BoundingSphere3F implements BoundingVolume3F {
	/**
	 * The ID of this {@code BoundingSphere3F} class.
	 */
	public static final int ID = 2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point3F center;
	private final float radius;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code BoundingSphere3F} instance with a radius of {@code 1.0F} and a center of {@code new Point3F()}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BoundingSphere3F(1.0F);
	 * }
	 * </pre>
	 */
	public BoundingSphere3F() {
		this(1.0F);
	}
	
	/**
	 * Constructs a new {@code BoundingSphere3F} instance with a radius of {@code radius} and a center of {@code new Point3F()}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BoundingSphere3F(radius, new Point3F());
	 * }
	 * </pre>
	 * 
	 * @param radius the radius of this {@code BoundingSphere3F} instance
	 */
	public BoundingSphere3F(final float radius) {
		this(radius, new Point3F());
	}
	
	/**
	 * Constructs a new {@code BoundingSphere3F} instance with a radius of {@code radius} and a center of {@code center}.
	 * <p>
	 * If {@code center} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param radius the radius of this {@code BoundingSphere3F} instance
	 * @param center the center of this {@code BoundingSphere3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code center} is {@code null}
	 */
	public BoundingSphere3F(final float radius, final Point3F center) {
		this.radius = radius;
		this.center = Point3F.getCached(Objects.requireNonNull(center, "center == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Performs a transformation.
	 * <p>
	 * Returns a new {@code BoundingSphere3F} instance with the result of the transformation.
	 * <p>
	 * If {@code matrix} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrix the {@link Matrix44F} instance to perform the transformation with
	 * @return a new {@code BoundingSphere3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, {@code matrix} is {@code null}
	 */
	@Override
	public BoundingSphere3F transform(final Matrix44F matrix) {
		final Point3F center = Point3F.transformAndDivide(matrix, this.center);
		final Point3F x = Point3F.transformAndDivide(matrix, new Point3F(this.center.getX() + this.radius, this.center.getY(), this.center.getZ()));
		final Point3F y = Point3F.transformAndDivide(matrix, new Point3F(this.center.getX(), this.center.getY() + this.radius, this.center.getZ()));
		final Point3F z = Point3F.transformAndDivide(matrix, new Point3F(this.center.getX(), this.center.getY(), this.center.getZ() + this.radius));
		
		final float distanceSquaredFromCenterToX = Point3F.distanceSquared(center, x);
		final float distanceSquaredFromCenterToY = Point3F.distanceSquared(center, y);
		final float distanceSquaredFromCenterToZ = Point3F.distanceSquared(center, z);
		final float distanceSquared = max(distanceSquaredFromCenterToX, distanceSquaredFromCenterToY, distanceSquaredFromCenterToZ);
		
		final float radius = sqrt(distanceSquared);
		
		return new BoundingSphere3F(radius, center);
	}
	
	/**
	 * Returns the center of this {@code BoundingSphere3F} instance.
	 * 
	 * @return the center of this {@code BoundingSphere3F} instance
	 */
	public Point3F getCenter() {
		return this.center;
	}
	
	/**
	 * Returns a {@link Point3F} instance that represents the closest point to {@code point} and is contained in this {@code BoundingSphere3F} instance.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@code Point3F} instance
	 * @return a {@code Point3F} instance that represents the closest point to {@code point} and is contained in this {@code BoundingSphere3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public Point3F getClosestPointTo(final Point3F point) {
		final Point3F surfaceIntersectionPoint = Point3F.add(this.center, Vector3F.directionNormalized(this.center, point), this.radius);
		
		final float distanceToSurfaceIntersectionPointSquared = Point3F.distanceSquared(this.center, surfaceIntersectionPoint);
		final float distanceToPSquared = Point3F.distanceSquared(this.center, point);
		
		return distanceToSurfaceIntersectionPointSquared <= distanceToPSquared ? surfaceIntersectionPoint : point;
	}
	
	/**
	 * Returns a {@link Point3F} with the largest component values that are contained in this {@code BoundingSphere3F} instance.
	 * 
	 * @return a {@code Point3F} with the largest component values that are contained in this {@code BoundingSphere3F} instance
	 */
	@Override
	public Point3F getMaximum() {
		return Point3F.add(this.center, this.radius);
	}
	
	/**
	 * Returns a {@link Point3F} with the smallest component values that are contained in this {@code BoundingSphere3F} instance.
	 * 
	 * @return a {@code Point3F} with the smallest component values that are contained in this {@code BoundingSphere3F} instance
	 */
	@Override
	public Point3F getMinimum() {
		return Point3F.subtract(this.center, this.radius);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code BoundingSphere3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code BoundingSphere3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new BoundingSphere3F(%+.10f, %s)", Float.valueOf(this.radius), this.center);
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
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code BoundingSphere3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3F} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code BoundingSphere3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public boolean contains(final Point3F point) {
		return Point3F.distanceSquared(this.center, point) <= this.radius * this.radius;
	}
	
	/**
	 * Compares {@code object} to this {@code BoundingSphere3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code BoundingSphere3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code BoundingSphere3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code BoundingSphere3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof BoundingSphere3F)) {
			return false;
		} else if(!Objects.equals(this.center, BoundingSphere3F.class.cast(object).center)) {
			return false;
		} else if(!equal(this.radius, BoundingSphere3F.class.cast(object).radius)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the radius of this {@code BoundingSphere3F} instance.
	 * 
	 * @return the radius of this {@code BoundingSphere3F} instance
	 */
	public float getRadius() {
		return this.radius;
	}
	
	/**
	 * Returns the squared radius of this {@code BoundingSphere3F} instance.
	 * 
	 * @return the squared radius of this {@code BoundingSphere3F} instance
	 */
	public float getRadiusSquared() {
		return this.radius * this.radius;
	}
	
	/**
	 * Returns the surface area of this {@code BoundingSphere3F} instance.
	 * 
	 * @return the surface area of this {@code BoundingSphere3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		return PI_MULTIPLIED_BY_4 * getRadiusSquared();
	}
	
	/**
	 * Returns the volume of this {@code BoundingSphere3F} instance.
	 * 
	 * @return the volume of this {@code BoundingSphere3F} instance
	 */
	@Override
	public float getVolume() {
		return 4.0F / 3.0F * PI * pow(this.radius, 3.0F);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code BoundingSphere3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance from {@code ray} to this {@code BoundingSphere3F} instance, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code BoundingSphere3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance from {@code ray} to this {@code BoundingSphere3F} instance, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public float intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Point3F origin = ray.getOrigin();
		final Point3F center = getCenter();
		
		final Vector3F direction = ray.getDirection();
		final Vector3F centerToOrigin = Vector3F.direction(center, origin);
		
		final float radiusSquared = getRadiusSquared();
		
		final float a = direction.lengthSquared();
		final float b = 2.0F * Vector3F.dotProduct(centerToOrigin, direction);
		final float c = centerToOrigin.lengthSquared() - radiusSquared;
		
		final float[] ts = solveQuadraticSystem(a, b, c);
		
		final float t0 = ts[0];
		final float t1 = ts[1];
		
		final float t = !isNaN(t0) && t0 > tMinimum && t0 < tMaximum ? t0 : !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Float.NaN;
		
		return t;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code BoundingSphere3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code BoundingSphere3F} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code BoundingSphere3F} instance.
	 * 
	 * @return a hash code for this {@code BoundingSphere3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.center, Float.valueOf(this.radius));
	}
	
	/**
	 * Writes this {@code BoundingSphere3F} instance to {@code dataOutput}.
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
			dataOutput.writeFloat(this.radius);
			
			this.center.write(dataOutput);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}