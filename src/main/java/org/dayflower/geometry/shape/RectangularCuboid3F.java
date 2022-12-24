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
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
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
 * A {@code RectangularCuboid3F} is an implementation of {@link Shape3F} that represents a rectangular cuboid.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3F} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class RectangularCuboid3F implements Shape3F {
	/**
	 * The name of this {@code RectangularCuboid3F} class.
	 */
//	TODO: Add Unit Tests!
	public static final String NAME = "Rectangular Cuboid";
	
	/**
	 * The ID of this {@code RectangularCuboid3F} class.
	 */
//	TODO: Add Unit Tests!
	public static final int ID = 14;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point3F maximum;
	private final Point3F minimum;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code RectangularCuboid3F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new RectangularCuboid3F(new Point3F(-0.5F, -0.5F, -0.5F), new Point3F(0.5F, 0.5F, 0.5F));
	 * }
	 * </pre>
	 */
//	TODO: Add Unit Tests!
	public RectangularCuboid3F() {
		this(new Point3F(-0.5F, -0.5F, -0.5F), new Point3F(0.5F, 0.5F, 0.5F));
	}
	
	/**
	 * Constructs a new {@code RectangularCuboid3F} instance.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a reference {@link Point3F}
	 * @param b a reference {@code Point3F}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public RectangularCuboid3F(final Point3F a, final Point3F b) {
		this.maximum = Point3F.getCached(Point3F.maximum(a, b));
		this.minimum = Point3F.getCached(Point3F.minimum(a, b));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code RectangularCuboid3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code RectangularCuboid3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public BoundingVolume3F getBoundingVolume() {
		return new AxisAlignedBoundingBox3F(this.maximum, this.minimum);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code RectangularCuboid3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code RectangularCuboid3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final float t = intersectionT(ray, tMinimum, tMaximum);
		
		if(Floats.isNaN(t)) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		return Optional.of(doCreateSurfaceIntersection(ray, t));
	}
	
	/**
	 * Returns a {@link Point3F} with the largest component values that are contained in this {@code RectangularCuboid3F} instance.
	 * 
	 * @return a {@code Point3F} with the largest component values that are contained in this {@code RectangularCuboid3F} instance
	 */
//	TODO: Add Unit Tests!
	public Point3F getMaximum() {
		return this.maximum;
	}
	
	/**
	 * Returns a {@link Point3F} with the smallest component values that are contained in this {@code RectangularCuboid3F} instance.
	 * 
	 * @return a {@code Point3F} with the smallest component values that are contained in this {@code RectangularCuboid3F} instance
	 */
//	TODO: Add Unit Tests!
	public Point3F getMinimum() {
		return this.minimum;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code RectangularCuboid3F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code RectangularCuboid3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code RectangularCuboid3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code RectangularCuboid3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new RectangularCuboid3F(%s, %s)", this.maximum, this.minimum);
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
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code RectangularCuboid3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3F} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code RectangularCuboid3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean contains(final Point3F point) {
		return point.x >= this.minimum.x && point.x <= this.maximum.x && point.y >= this.minimum.y && point.y <= this.maximum.y && point.z >= this.minimum.z && point.z <= this.maximum.z;
	}
	
	/**
	 * Compares {@code object} to this {@code RectangularCuboid3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code RectangularCuboid3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code RectangularCuboid3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code RectangularCuboid3F}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof RectangularCuboid3F)) {
			return false;
		} else if(!Objects.equals(this.maximum, RectangularCuboid3F.class.cast(object).maximum)) {
			return false;
		} else if(!Objects.equals(this.minimum, RectangularCuboid3F.class.cast(object).minimum)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code RectangularCuboid3F} instance.
	 * 
	 * @return the surface area of this {@code RectangularCuboid3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public float getSurfaceArea() {
		final float x = this.maximum.x - this.minimum.x;
		final float y = this.maximum.y - this.minimum.y;
		final float z = this.maximum.z - this.minimum.z;
		final float surfaceArea = 2.0F * (x * y + y * z + z * x);
		
		return surfaceArea;
	}
	
	/**
	 * Returns the volume of this {@code RectangularCuboid3F} instance.
	 * 
	 * @return the volume of this {@code RectangularCuboid3F} instance
	 */
//	TODO: Add Unit Tests!
	public float getVolume() {
		final float x = this.maximum.x - this.minimum.x;
		final float y = this.maximum.y - this.minimum.y;
		final float z = this.maximum.z - this.minimum.z;
		final float volume = x * y * z;
		
		return volume;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code RectangularCuboid3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code RectangularCuboid3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Vector3F directionReciprocal = Vector3F.reciprocal(ray.getDirection());
		final Vector3F directionA = Vector3F.hadamardProduct(Vector3F.direction(ray.getOrigin(), getMinimum()), directionReciprocal);
		final Vector3F directionB = Vector3F.hadamardProduct(Vector3F.direction(ray.getOrigin(), getMaximum()), directionReciprocal);
		
		final float t0 = Floats.max(Floats.min(directionA.x, directionB.x), Floats.min(directionA.y, directionB.y), Floats.min(directionA.z, directionB.z));
		final float t1 = Floats.min(Floats.max(directionA.x, directionB.x), Floats.max(directionA.y, directionB.y), Floats.max(directionA.z, directionB.z));
		
		if(t0 > t1) {
			return Float.NaN;
		}
		
		if(t0 > tMinimum && t0 < tMaximum) {
			return t0;
		}
		
		if(t1 > tMinimum && t1 < tMaximum) {
			return t1;
		}
		
		return Float.NaN;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code RectangularCuboid3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code RectangularCuboid3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code RectangularCuboid3F} instance.
	 * 
	 * @return a hash code for this {@code RectangularCuboid3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.maximum, this.minimum);
	}
	
	/**
	 * Writes this {@code RectangularCuboid3F} instance to {@code dataOutput}.
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
			
			this.maximum.write(dataOutput);
			this.minimum.write(dataOutput);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private OrthonormalBasis33F doCreateOrthonormalBasisG(final Point3F surfaceIntersectionPoint) {
		final Point3F midpoint = Point3F.midpoint(this.maximum, this.minimum);
		
		final Vector3F halfDirection = Vector3F.multiply(Vector3F.direction(this.minimum, this.maximum), 0.5F);
		
		if(surfaceIntersectionPoint.x + halfDirection.x - 0.0001F < midpoint.x) {
			return new OrthonormalBasis33F(Vector3F.x(-1.0F));
		}
		
		if(surfaceIntersectionPoint.x - halfDirection.x + 0.0001F > midpoint.x) {
			return new OrthonormalBasis33F(Vector3F.x(+1.0F));
		}
		
		if(surfaceIntersectionPoint.y + halfDirection.y - 0.0001F < midpoint.y) {
			return new OrthonormalBasis33F(Vector3F.y(-1.0F));
		}
		
		if(surfaceIntersectionPoint.y - halfDirection.y + 0.0001F > midpoint.y) {
			return new OrthonormalBasis33F(Vector3F.y(+1.0F));
		}
		
		if(surfaceIntersectionPoint.z + halfDirection.z - 0.0001F < midpoint.z) {
			return new OrthonormalBasis33F(Vector3F.z(-1.0F));
		}
		
		if(surfaceIntersectionPoint.z - halfDirection.z + 0.0001F > midpoint.z) {
			return new OrthonormalBasis33F(Vector3F.z(+1.0F));
		}
		
		return new OrthonormalBasis33F();
	}
	
	private Point2F doCreateTextureCoordinates(final Point3F surfaceIntersectionPoint) {
		final Point3F midpoint = Point3F.midpoint(this.maximum, this.minimum);
		
		final Vector3F halfDirection = Vector3F.multiply(Vector3F.direction(this.minimum, this.maximum), 0.5F);
		
		if(surfaceIntersectionPoint.x + halfDirection.x - 0.0001F < midpoint.x || surfaceIntersectionPoint.x - halfDirection.x + 0.0001F > midpoint.x) {
			final float u = Floats.normalize(surfaceIntersectionPoint.z, this.minimum.z, this.maximum.z);
			final float v = Floats.normalize(surfaceIntersectionPoint.y, this.minimum.y, this.maximum.y);
			
			return new Point2F(u, v);
		}
		
		if(surfaceIntersectionPoint.y + halfDirection.y - 0.0001F < midpoint.y || surfaceIntersectionPoint.y - halfDirection.y + 0.0001F > midpoint.y) {
			final float u = Floats.normalize(surfaceIntersectionPoint.x, this.minimum.x, this.maximum.x);
			final float v = Floats.normalize(surfaceIntersectionPoint.z, this.minimum.z, this.maximum.z);
			
			return new Point2F(u, v);
		}
		
		if(surfaceIntersectionPoint.z + halfDirection.z - 0.0001F < midpoint.z || surfaceIntersectionPoint.z - halfDirection.z + 0.0001F > midpoint.z) {
			final float u = Floats.normalize(surfaceIntersectionPoint.x, this.minimum.x, this.maximum.x);
			final float v = Floats.normalize(surfaceIntersectionPoint.y, this.minimum.y, this.maximum.y);
			
			return new Point2F(u, v);
		}
		
		return new Point2F();
	}
	
	private SurfaceIntersection3F doCreateSurfaceIntersection(final Ray3F ray, final float t) {
		final Point3F surfaceIntersectionPoint = doCreateSurfaceIntersectionPoint(ray, t);
		
		final Point2F textureCoordinates = doCreateTextureCoordinates(surfaceIntersectionPoint);
		
		final OrthonormalBasis33F orthonormalBasisG = doCreateOrthonormalBasisG(surfaceIntersectionPoint);
		final OrthonormalBasis33F orthonormalBasisS = orthonormalBasisG;
		
		return new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, t);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point3F doCreateSurfaceIntersectionPoint(final Ray3F ray, final float t) {
		return Point3F.add(ray.getOrigin(), ray.getDirection(), t);
	}
}