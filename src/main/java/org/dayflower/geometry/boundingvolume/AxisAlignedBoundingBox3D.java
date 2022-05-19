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

import static org.dayflower.utility.Doubles.max;
import static org.dayflower.utility.Doubles.min;

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
import org.dayflower.utility.ParameterArguments;

/**
 * An {@code AxisAlignedBoundingBox3D} is an implementation of {@link BoundingVolume3D} that represents an axis-aligned bounding box (AABB).
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class AxisAlignedBoundingBox3D implements BoundingVolume3D {
	/**
	 * The ID of this {@code AxisAlignedBoundingBox3D} class.
	 */
	public static final int ID = 1;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point3D maximum;
	private final Point3D minimum;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AxisAlignedBoundingBox3D} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new AxisAlignedBoundingBox3D(new Point3D(-0.5D, -0.5D, -0.5D), new Point3D(0.5D, 0.5D, 0.5D));
	 * }
	 * </pre>
	 */
	public AxisAlignedBoundingBox3D() {
		this(new Point3D(-0.5D, -0.5D, -0.5D), new Point3D(0.5D, 0.5D, 0.5D));
	}
	
	/**
	 * Constructs a new {@code AxisAlignedBoundingBox3D} instance.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a reference {@link Point3D}
	 * @param b a reference {@code Point3D}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public AxisAlignedBoundingBox3D(final Point3D a, final Point3D b) {
		this.maximum = Point3D.getCached(Point3D.maximum(a, b));
		this.minimum = Point3D.getCached(Point3D.minimum(a, b));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Performs a transformation.
	 * <p>
	 * Returns a new {@code AxisAlignedBoundingBox3D} instance with the result of the transformation.
	 * <p>
	 * If {@code matrix} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrix the {@link Matrix44D} instance to perform the transformation with
	 * @return a new {@code AxisAlignedBoundingBox3D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, {@code matrix} is {@code null}
	 */
	@Override
	public AxisAlignedBoundingBox3D transform(final Matrix44D matrix) {
		final double maximumX = this.maximum.x;
		final double maximumY = this.maximum.y;
		final double maximumZ = this.maximum.z;
		final double minimumX = this.minimum.x;
		final double minimumY = this.minimum.y;
		final double minimumZ = this.minimum.z;
		
		final Point3D[] points = new Point3D[] {
			Point3D.transformAndDivide(matrix, new Point3D(minimumX, minimumY, minimumZ)),
			Point3D.transformAndDivide(matrix, new Point3D(maximumX, minimumY, minimumZ)),
			Point3D.transformAndDivide(matrix, new Point3D(minimumX, maximumY, minimumZ)),
			Point3D.transformAndDivide(matrix, new Point3D(minimumX, minimumY, maximumZ)),
			Point3D.transformAndDivide(matrix, new Point3D(minimumX, maximumY, maximumZ)),
			Point3D.transformAndDivide(matrix, new Point3D(maximumX, maximumY, minimumZ)),
			Point3D.transformAndDivide(matrix, new Point3D(maximumX, minimumY, maximumZ)),
			Point3D.transformAndDivide(matrix, new Point3D(maximumX, maximumY, maximumZ))
		};
		
		Point3D maximum = Point3D.MINIMUM;
		Point3D minimum = Point3D.MAXIMUM;
		
		for(final Point3D point : points) {
			maximum = Point3D.maximum(maximum, point);
			minimum = Point3D.minimum(minimum, point);
		}
		
		return new AxisAlignedBoundingBox3D(maximum, minimum);
	}
	
	/**
	 * Returns a {@link Point3D} instance that represents the closest point to {@code point} and is contained in this {@code AxisAlignedBoundingBox3D} instance.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@code Point3D} instance
	 * @return a {@code Point3D} instance that represents the closest point to {@code point} and is contained in this {@code AxisAlignedBoundingBox3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public Point3D getClosestPointTo(final Point3D point) {
		final double maximumX = this.maximum.x;
		final double maximumY = this.maximum.y;
		final double maximumZ = this.maximum.z;
		
		final double minimumX = this.minimum.x;
		final double minimumY = this.minimum.y;
		final double minimumZ = this.minimum.z;
		
		final double x = point.x < minimumX ? minimumX : point.x > maximumX ? maximumX : point.x;
		final double y = point.y < minimumY ? minimumY : point.y > maximumY ? maximumY : point.y;
		final double z = point.z < minimumZ ? minimumZ : point.z > maximumZ ? maximumZ : point.z;
		
		return new Point3D(x, y, z);
	}
	
	/**
	 * Returns a {@link Point3D} with the largest component values needed to contain this {@code AxisAlignedBoundingBox3D} instance.
	 * 
	 * @return a {@code Point3D} with the largest component values needed to contain this {@code AxisAlignedBoundingBox3D} instance
	 */
	@Override
	public Point3D getMaximum() {
		return this.maximum;
	}
	
	/**
	 * Returns a {@link Point3D} with the smallest component values needed to contain this {@code AxisAlignedBoundingBox3D} instance.
	 * 
	 * @return a {@code Point3D} with the smallest component values needed to contain this {@code AxisAlignedBoundingBox3D} instance
	 */
	@Override
	public Point3D getMinimum() {
		return this.minimum;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code AxisAlignedBoundingBox3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code AxisAlignedBoundingBox3D} instance
	 */
	@Override
	public String toString() {
		return String.format("new AxisAlignedBoundingBox3D(%s, %s)", this.maximum, this.minimum);
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
				if(!this.maximum.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.minimum.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code AxisAlignedBoundingBox3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3D} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code AxisAlignedBoundingBox3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public boolean contains(final Point3D point) {
		return point.x >= this.minimum.x && point.x <= this.maximum.x && point.y >= this.minimum.y && point.y <= this.maximum.y && point.z >= this.minimum.z && point.z <= this.maximum.z;
	}
	
	/**
	 * Compares {@code object} to this {@code AxisAlignedBoundingBox3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code AxisAlignedBoundingBox3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code AxisAlignedBoundingBox3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code AxisAlignedBoundingBox3D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof AxisAlignedBoundingBox3D)) {
			return false;
		} else if(!Objects.equals(this.maximum, AxisAlignedBoundingBox3D.class.cast(object).maximum)) {
			return false;
		} else if(!Objects.equals(this.minimum, AxisAlignedBoundingBox3D.class.cast(object).minimum)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code AxisAlignedBoundingBox3D} instance.
	 * 
	 * @return the surface area of this {@code AxisAlignedBoundingBox3D} instance
	 */
	@Override
	public double getSurfaceArea() {
		final double x = this.maximum.x - this.minimum.x;
		final double y = this.maximum.y - this.minimum.y;
		final double z = this.maximum.z - this.minimum.z;
		final double surfaceArea = 2.0D * (x * y + y * z + z * x);
		
		return surfaceArea;
	}
	
	/**
	 * Returns the volume of this {@code AxisAlignedBoundingBox3D} instance.
	 * 
	 * @return the volume of this {@code AxisAlignedBoundingBox3D} instance
	 */
	@Override
	public double getVolume() {
		final double x = this.maximum.x - this.minimum.x;
		final double y = this.maximum.y - this.minimum.y;
		final double z = this.maximum.z - this.minimum.z;
		final double volume = x * y * z;
		
		return volume;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code AxisAlignedBoundingBox3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance from {@code ray} to this {@code AxisAlignedBoundingBox3D} instance, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code AxisAlignedBoundingBox3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance from {@code ray} to this {@code AxisAlignedBoundingBox3D} instance, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public double intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Point3D maximum = getMaximum();
		final Point3D minimum = getMinimum();
		final Point3D origin = ray.getOrigin();
		
		final Vector3D direction = ray.getDirection();
		final Vector3D directionReciprocal = Vector3D.reciprocal(direction);
		final Vector3D directionA = Vector3D.hadamardProduct(Vector3D.direction(origin, maximum), directionReciprocal);
		final Vector3D directionB = Vector3D.hadamardProduct(Vector3D.direction(origin, minimum), directionReciprocal);
		
		final double t0 = max(min(directionA.x, directionB.x), min(directionA.y, directionB.y), min(directionA.z, directionB.z));
		final double t1 = min(max(directionA.x, directionB.x), max(directionA.y, directionB.y), max(directionA.z, directionB.z));
		
		return t0 > t1 ? Double.NaN : t0 > tMinimum && t0 < tMaximum ? t0 : t1 > tMinimum && t1 < tMaximum ? t1 : Double.NaN;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code AxisAlignedBoundingBox3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code AxisAlignedBoundingBox3D} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code AxisAlignedBoundingBox3D} instance.
	 * 
	 * @return a hash code for this {@code AxisAlignedBoundingBox3D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.maximum, this.minimum);
	}
	
	/**
	 * Writes this {@code AxisAlignedBoundingBox3D} instance to {@code dataOutput}.
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
			
			this.maximum.write(dataOutput);
			this.minimum.write(dataOutput);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns an {@code AxisAlignedBoundingBox3D} instance that is an expanded version of {@code axisAlignedBoundingBox}.
	 * <p>
	 * If {@code axisAlignedBoundingBox} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param axisAlignedBoundingBox an {@code AxisAlignedBoundingBox3D} instance
	 * @param delta the delta to expand with
	 * @return an {@code AxisAlignedBoundingBox3D} instance that is an expanded version of {@code axisAlignedBoundingBox}
	 * @throws NullPointerException thrown if, and only if, {@code axisAlignedBoundingBox} is {@code null}
	 */
	public static AxisAlignedBoundingBox3D expand(final AxisAlignedBoundingBox3D axisAlignedBoundingBox, final double delta) {
		final Point3D maximum = Point3D.add(axisAlignedBoundingBox.maximum, delta);
		final Point3D minimum = Point3D.subtract(axisAlignedBoundingBox.minimum, delta);
		
		return new AxisAlignedBoundingBox3D(maximum, minimum);
	}
	
	/**
	 * Returns an {@code AxisAlignedBoundingBox3D} instance that contains all {@link Point3D} instances in {@code points}.
	 * <p>
	 * If either {@code points} or an element in {@code points} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code points.length} is less than {@code 1}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param points a {@code Point3D[]} instance
	 * @return an {@code AxisAlignedBoundingBox3D} instance that contains all {@code Point3D} instances in {@code points}
	 * @throws IllegalArgumentException thrown if, and only if, {@code points.length} is less than {@code 1}
	 * @throws NullPointerException thrown if, and only if, either {@code points} or an element in {@code points} are {@code null}
	 */
	public static AxisAlignedBoundingBox3D fromPoints(final Point3D... points) {
		ParameterArguments.requireNonNullArray(points, "points");
		ParameterArguments.requireRange(points.length, 1, Integer.MAX_VALUE, "points.length");
		
		Point3D maximum = Point3D.MINIMUM;
		Point3D minimum = Point3D.MAXIMUM;
		
		for(final Point3D point : points) {
			maximum = Point3D.maximum(maximum, point);
			minimum = Point3D.minimum(minimum, point);
		}
		
		return new AxisAlignedBoundingBox3D(maximum, minimum);
	}
	
	/**
	 * Returns an {@code AxisAlignedBoundingBox3D} instance that is the union of {@code boundingVolumeLHS} and {@code boundingVolumeRHS}.
	 * <p>
	 * If either {@code boundingVolumeLHS} or {@code boundingVolumeRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param boundingVolumeLHS a {@link BoundingVolume3D} instance
	 * @param boundingVolumeRHS a {@code BoundingVolume3D} instance
	 * @return an {@code AxisAlignedBoundingBox3D} instance that is the union of {@code boundingVolumeLHS} and {@code boundingVolumeRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code boundingVolumeLHS} or {@code boundingVolumeRHS} are {@code null}
	 */
	public static AxisAlignedBoundingBox3D union(final BoundingVolume3D boundingVolumeLHS, final BoundingVolume3D boundingVolumeRHS) {
		final Point3D maximum = Point3D.maximum(boundingVolumeLHS.getMaximum(), boundingVolumeRHS.getMaximum());
		final Point3D minimum = Point3D.minimum(boundingVolumeLHS.getMinimum(), boundingVolumeRHS.getMinimum());
		
		return new AxisAlignedBoundingBox3D(maximum, minimum);
	}
	
	/**
	 * Returns an {@code AxisAlignedBoundingBox3D} instance that is the union of {@code boundingVolumeLHS} and {@code pointRHS}.
	 * <p>
	 * If either {@code boundingVolumeLHS} or {@code pointRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param boundingVolumeLHS a {@link BoundingVolume3D} instance
	 * @param pointRHS a {@code Point3D} instance
	 * @return an {@code AxisAlignedBoundingBox3D} instance that is the union of {@code boundingVolumeLHS} and {@code pointRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code boundingVolumeLHS} or {@code pointRHS} are {@code null}
	 */
	public static AxisAlignedBoundingBox3D union(final BoundingVolume3D boundingVolumeLHS, final Point3D pointRHS) {
		final Point3D maximum = Point3D.maximum(boundingVolumeLHS.getMaximum(), pointRHS);
		final Point3D minimum = Point3D.minimum(boundingVolumeLHS.getMinimum(), pointRHS);
		
		return new AxisAlignedBoundingBox3D(maximum, minimum);
	}
}