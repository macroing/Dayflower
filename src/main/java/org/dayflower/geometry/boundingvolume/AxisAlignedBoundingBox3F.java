/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.utility.ParameterArguments;

import org.macroing.java.lang.Floats;
import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;

/**
 * An {@code AxisAlignedBoundingBox3F} is an implementation of {@link BoundingVolume3F} that represents an axis-aligned bounding box (AABB).
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class AxisAlignedBoundingBox3F implements BoundingVolume3F {
	/**
	 * The ID of this {@code AxisAlignedBoundingBox3F} class.
	 */
	public static final int ID = 1;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point3F maximum;
	private final Point3F minimum;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AxisAlignedBoundingBox3F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new AxisAlignedBoundingBox3F(new Point3F(-0.5F, -0.5F, -0.5F), new Point3F(0.5F, 0.5F, 0.5F));
	 * }
	 * </pre>
	 */
	public AxisAlignedBoundingBox3F() {
		this(new Point3F(-0.5F, -0.5F, -0.5F), new Point3F(0.5F, 0.5F, 0.5F));
	}
	
	/**
	 * Constructs a new {@code AxisAlignedBoundingBox3F} instance.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a reference {@link Point3F}
	 * @param b a reference {@code Point3F}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public AxisAlignedBoundingBox3F(final Point3F a, final Point3F b) {
		this.maximum = Point3F.getCached(Point3F.maximum(a, b));
		this.minimum = Point3F.getCached(Point3F.minimum(a, b));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Performs a transformation.
	 * <p>
	 * Returns a new {@code AxisAlignedBoundingBox3F} instance with the result of the transformation.
	 * <p>
	 * If {@code matrix} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrix the {@link Matrix44F} instance to perform the transformation with
	 * @return a new {@code AxisAlignedBoundingBox3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, {@code matrix} is {@code null}
	 */
	@Override
	public AxisAlignedBoundingBox3F transform(final Matrix44F matrix) {
		final float maximumX = this.maximum.x;
		final float maximumY = this.maximum.y;
		final float maximumZ = this.maximum.z;
		final float minimumX = this.minimum.x;
		final float minimumY = this.minimum.y;
		final float minimumZ = this.minimum.z;
		
		final Point3F[] points = new Point3F[] {
			Point3F.transformAndDivide(matrix, new Point3F(minimumX, minimumY, minimumZ)),
			Point3F.transformAndDivide(matrix, new Point3F(maximumX, minimumY, minimumZ)),
			Point3F.transformAndDivide(matrix, new Point3F(minimumX, maximumY, minimumZ)),
			Point3F.transformAndDivide(matrix, new Point3F(minimumX, minimumY, maximumZ)),
			Point3F.transformAndDivide(matrix, new Point3F(minimumX, maximumY, maximumZ)),
			Point3F.transformAndDivide(matrix, new Point3F(maximumX, maximumY, minimumZ)),
			Point3F.transformAndDivide(matrix, new Point3F(maximumX, minimumY, maximumZ)),
			Point3F.transformAndDivide(matrix, new Point3F(maximumX, maximumY, maximumZ))
		};
		
		Point3F maximum = Point3F.MINIMUM;
		Point3F minimum = Point3F.MAXIMUM;
		
		for(final Point3F point : points) {
			maximum = Point3F.maximum(maximum, point);
			minimum = Point3F.minimum(minimum, point);
		}
		
		return new AxisAlignedBoundingBox3F(maximum, minimum);
	}
	
	/**
	 * Returns a {@link Point3F} instance that represents the closest point to {@code point} and is contained in this {@code AxisAlignedBoundingBox3F} instance.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@code Point3F} instance
	 * @return a {@code Point3F} instance that represents the closest point to {@code point} and is contained in this {@code AxisAlignedBoundingBox3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public Point3F getClosestPointTo(final Point3F point) {
		final float maximumX = this.maximum.x;
		final float maximumY = this.maximum.y;
		final float maximumZ = this.maximum.z;
		
		final float minimumX = this.minimum.x;
		final float minimumY = this.minimum.y;
		final float minimumZ = this.minimum.z;
		
		final float x = point.x < minimumX ? minimumX : point.x > maximumX ? maximumX : point.x;
		final float y = point.y < minimumY ? minimumY : point.y > maximumY ? maximumY : point.y;
		final float z = point.z < minimumZ ? minimumZ : point.z > maximumZ ? maximumZ : point.z;
		
		return new Point3F(x, y, z);
	}
	
	/**
	 * Returns a {@link Point3F} with the largest component values needed to contain this {@code AxisAlignedBoundingBox3F} instance.
	 * 
	 * @return a {@code Point3F} with the largest component values needed to contain this {@code AxisAlignedBoundingBox3F} instance
	 */
	@Override
	public Point3F getMaximum() {
		return this.maximum;
	}
	
	/**
	 * Returns a {@link Point3F} with the smallest component values needed to contain this {@code AxisAlignedBoundingBox3F} instance.
	 * 
	 * @return a {@code Point3F} with the smallest component values needed to contain this {@code AxisAlignedBoundingBox3F} instance
	 */
	@Override
	public Point3F getMinimum() {
		return this.minimum;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code AxisAlignedBoundingBox3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code AxisAlignedBoundingBox3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new AxisAlignedBoundingBox3F(%s, %s)", this.maximum, this.minimum);
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
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code AxisAlignedBoundingBox3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3F} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code AxisAlignedBoundingBox3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public boolean contains(final Point3F point) {
		return point.x >= this.minimum.x && point.x <= this.maximum.x && point.y >= this.minimum.y && point.y <= this.maximum.y && point.z >= this.minimum.z && point.z <= this.maximum.z;
	}
	
	/**
	 * Compares {@code object} to this {@code AxisAlignedBoundingBox3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code AxisAlignedBoundingBox3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code AxisAlignedBoundingBox3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code AxisAlignedBoundingBox3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof AxisAlignedBoundingBox3F)) {
			return false;
		} else if(!Objects.equals(this.maximum, AxisAlignedBoundingBox3F.class.cast(object).maximum)) {
			return false;
		} else if(!Objects.equals(this.minimum, AxisAlignedBoundingBox3F.class.cast(object).minimum)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code AxisAlignedBoundingBox3F} instance.
	 * 
	 * @return the surface area of this {@code AxisAlignedBoundingBox3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		final float x = this.maximum.x - this.minimum.x;
		final float y = this.maximum.y - this.minimum.y;
		final float z = this.maximum.z - this.minimum.z;
		final float surfaceArea = 2.0F * (x * y + y * z + z * x);
		
		return surfaceArea;
	}
	
	/**
	 * Returns the volume of this {@code AxisAlignedBoundingBox3F} instance.
	 * 
	 * @return the volume of this {@code AxisAlignedBoundingBox3F} instance
	 */
	@Override
	public float getVolume() {
		final float x = this.maximum.x - this.minimum.x;
		final float y = this.maximum.y - this.minimum.y;
		final float z = this.maximum.z - this.minimum.z;
		final float volume = x * y * z;
		
		return volume;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code AxisAlignedBoundingBox3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance from {@code ray} to this {@code AxisAlignedBoundingBox3F} instance, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code AxisAlignedBoundingBox3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance from {@code ray} to this {@code AxisAlignedBoundingBox3F} instance, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public float intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Point3F maximum = getMaximum();
		final Point3F minimum = getMinimum();
		final Point3F origin = ray.getOrigin();
		
		final Vector3F direction = ray.getDirection();
		final Vector3F directionReciprocal = Vector3F.reciprocal(direction);
		final Vector3F directionA = Vector3F.hadamardProduct(Vector3F.direction(origin, maximum), directionReciprocal);
		final Vector3F directionB = Vector3F.hadamardProduct(Vector3F.direction(origin, minimum), directionReciprocal);
		
		final float t0 = Floats.max(Floats.min(directionA.x, directionB.x), Floats.min(directionA.y, directionB.y), Floats.min(directionA.z, directionB.z));
		final float t1 = Floats.min(Floats.max(directionA.x, directionB.x), Floats.max(directionA.y, directionB.y), Floats.max(directionA.z, directionB.z));
		
		return t0 > t1 ? Float.NaN : t0 > tMinimum && t0 < tMaximum ? t0 : t1 > tMinimum && t1 < tMaximum ? t1 : Float.NaN;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code AxisAlignedBoundingBox3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code AxisAlignedBoundingBox3F} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code AxisAlignedBoundingBox3F} instance.
	 * 
	 * @return a hash code for this {@code AxisAlignedBoundingBox3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.maximum, this.minimum);
	}
	
	/**
	 * Writes this {@code AxisAlignedBoundingBox3F} instance to {@code dataOutput}.
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
	 * Returns an {@code AxisAlignedBoundingBox3F} instance that is an expanded version of {@code axisAlignedBoundingBox}.
	 * <p>
	 * If {@code axisAlignedBoundingBox} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param axisAlignedBoundingBox an {@code AxisAlignedBoundingBox3F} instance
	 * @param delta the delta to expand with
	 * @return an {@code AxisAlignedBoundingBox3F} instance that is an expanded version of {@code axisAlignedBoundingBox}
	 * @throws NullPointerException thrown if, and only if, {@code axisAlignedBoundingBox} is {@code null}
	 */
	public static AxisAlignedBoundingBox3F expand(final AxisAlignedBoundingBox3F axisAlignedBoundingBox, final float delta) {
		final Point3F maximum = Point3F.add(axisAlignedBoundingBox.maximum, delta);
		final Point3F minimum = Point3F.subtract(axisAlignedBoundingBox.minimum, delta);
		
		return new AxisAlignedBoundingBox3F(maximum, minimum);
	}
	
	/**
	 * Returns an {@code AxisAlignedBoundingBox3F} instance that contains all {@link Point3F} instances in {@code points}.
	 * <p>
	 * If either {@code points} or an element in {@code points} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code points.length} is less than {@code 1}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param points a {@code Point3F[]} instance
	 * @return an {@code AxisAlignedBoundingBox3F} instance that contains all {@code Point3F} instances in {@code points}
	 * @throws IllegalArgumentException thrown if, and only if, {@code points.length} is less than {@code 1}
	 * @throws NullPointerException thrown if, and only if, either {@code points} or an element in {@code points} are {@code null}
	 */
	public static AxisAlignedBoundingBox3F fromPoints(final Point3F... points) {
		ParameterArguments.requireNonNullArray(points, "points");
		ParameterArguments.requireRange(points.length, 1, Integer.MAX_VALUE, "points.length");
		
		Point3F maximum = Point3F.MINIMUM;
		Point3F minimum = Point3F.MAXIMUM;
		
		for(final Point3F point : points) {
			maximum = Point3F.maximum(maximum, point);
			minimum = Point3F.minimum(minimum, point);
		}
		
		return new AxisAlignedBoundingBox3F(maximum, minimum);
	}
	
	/**
	 * Returns an {@code AxisAlignedBoundingBox3F} instance that is the union of {@code boundingVolumeLHS} and {@code boundingVolumeRHS}.
	 * <p>
	 * If either {@code boundingVolumeLHS} or {@code boundingVolumeRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param boundingVolumeLHS a {@link BoundingVolume3F} instance
	 * @param boundingVolumeRHS a {@code BoundingVolume3F} instance
	 * @return an {@code AxisAlignedBoundingBox3F} instance that is the union of {@code boundingVolumeLHS} and {@code boundingVolumeRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code boundingVolumeLHS} or {@code boundingVolumeRHS} are {@code null}
	 */
	public static AxisAlignedBoundingBox3F union(final BoundingVolume3F boundingVolumeLHS, final BoundingVolume3F boundingVolumeRHS) {
		final Point3F maximum = Point3F.maximum(boundingVolumeLHS.getMaximum(), boundingVolumeRHS.getMaximum());
		final Point3F minimum = Point3F.minimum(boundingVolumeLHS.getMinimum(), boundingVolumeRHS.getMinimum());
		
		return new AxisAlignedBoundingBox3F(maximum, minimum);
	}
	
	/**
	 * Returns an {@code AxisAlignedBoundingBox3F} instance that is the union of {@code boundingVolumeLHS} and {@code pointRHS}.
	 * <p>
	 * If either {@code boundingVolumeLHS} or {@code pointRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param boundingVolumeLHS a {@link BoundingVolume3F} instance
	 * @param pointRHS a {@code Point3F} instance
	 * @return an {@code AxisAlignedBoundingBox3F} instance that is the union of {@code boundingVolumeLHS} and {@code pointRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code boundingVolumeLHS} or {@code pointRHS} are {@code null}
	 */
	public static AxisAlignedBoundingBox3F union(final BoundingVolume3F boundingVolumeLHS, final Point3F pointRHS) {
		final Point3F maximum = Point3F.maximum(boundingVolumeLHS.getMaximum(), pointRHS);
		final Point3F minimum = Point3F.minimum(boundingVolumeLHS.getMinimum(), pointRHS);
		
		return new AxisAlignedBoundingBox3F(maximum, minimum);
	}
}