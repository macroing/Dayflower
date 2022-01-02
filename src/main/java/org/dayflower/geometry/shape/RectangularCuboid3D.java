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

import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.max;
import static org.dayflower.utility.Doubles.min;
import static org.dayflower.utility.Doubles.normalize;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3D;
import org.dayflower.geometry.OrthonormalBasis33D;
import org.dayflower.geometry.Point2D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.Shape3D;
import org.dayflower.geometry.SurfaceIntersection3D;
import org.dayflower.geometry.Vector3D;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3D;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code RectangularCuboid3D} is an implementation of {@link Shape3D} that represents a rectangular cuboid.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3D} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class RectangularCuboid3D implements Shape3D {
	/**
	 * The name of this {@code RectangularCuboid3D} class.
	 */
//	TODO: Add Unit Tests!
	public static final String NAME = "Rectangular Cuboid";
	
	/**
	 * The ID of this {@code RectangularCuboid3D} class.
	 */
//	TODO: Add Unit Tests!
	public static final int ID = 14;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point3D maximum;
	private final Point3D minimum;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code RectangularCuboid3D} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new RectangularCuboid3D(new Point3D(-0.5D, -0.5D, -0.5D), new Point3D(0.5D, 0.5D, 0.5D));
	 * }
	 * </pre>
	 */
//	TODO: Add Unit Tests!
	public RectangularCuboid3D() {
		this(new Point3D(-0.5D, -0.5D, -0.5D), new Point3D(0.5D, 0.5D, 0.5D));
	}
	
	/**
	 * Constructs a new {@code RectangularCuboid3D} instance.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a reference {@link Point3D}
	 * @param b a reference {@code Point3D}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public RectangularCuboid3D(final Point3D a, final Point3D b) {
		this.maximum = Point3D.getCached(Point3D.maximum(a, b));
		this.minimum = Point3D.getCached(Point3D.minimum(a, b));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3D} instance that contains this {@code RectangularCuboid3D} instance.
	 * 
	 * @return a {@code BoundingVolume3D} instance that contains this {@code RectangularCuboid3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public BoundingVolume3D getBoundingVolume() {
		return new AxisAlignedBoundingBox3D(this.maximum, this.minimum);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code RectangularCuboid3D} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3D} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code RectangularCuboid3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3D} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final double t = intersectionT(ray, tMinimum, tMaximum);
		
		if(isNaN(t)) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		return Optional.of(doCreateSurfaceIntersection(ray, t));
	}
	
	/**
	 * Returns a {@link Point3D} with the largest component values that are contained in this {@code RectangularCuboid3D} instance.
	 * 
	 * @return a {@code Point3D} with the largest component values that are contained in this {@code RectangularCuboid3D} instance
	 */
//	TODO: Add Unit Tests!
	public Point3D getMaximum() {
		return this.maximum;
	}
	
	/**
	 * Returns a {@link Point3D} with the smallest component values that are contained in this {@code RectangularCuboid3D} instance.
	 * 
	 * @return a {@code Point3D} with the smallest component values that are contained in this {@code RectangularCuboid3D} instance
	 */
//	TODO: Add Unit Tests!
	public Point3D getMinimum() {
		return this.minimum;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code RectangularCuboid3D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code RectangularCuboid3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code RectangularCuboid3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code RectangularCuboid3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new RectangularCuboid3D(%s, %s)", this.maximum, this.minimum);
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
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code RectangularCuboid3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3D} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code RectangularCuboid3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public boolean contains(final Point3D point) {
		return point.getX() >= this.minimum.getX() && point.getX() <= this.maximum.getX() && point.getY() >= this.minimum.getY() && point.getY() <= this.maximum.getY() && point.getZ() >= this.minimum.getZ() && point.getZ() <= this.maximum.getZ();
	}
	
	/**
	 * Compares {@code object} to this {@code RectangularCuboid3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code RectangularCuboid3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code RectangularCuboid3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code RectangularCuboid3D}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof RectangularCuboid3D)) {
			return false;
		} else if(!Objects.equals(this.maximum, RectangularCuboid3D.class.cast(object).maximum)) {
			return false;
		} else if(!Objects.equals(this.minimum, RectangularCuboid3D.class.cast(object).minimum)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code RectangularCuboid3D} instance.
	 * 
	 * @return the surface area of this {@code RectangularCuboid3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public double getSurfaceArea() {
		final double x = this.maximum.getX() - this.minimum.getX();
		final double y = this.maximum.getY() - this.minimum.getY();
		final double z = this.maximum.getZ() - this.minimum.getZ();
		final double surfaceArea = 2.0D * (x * y + y * z + z * x);
		
		return surfaceArea;
	}
	
	/**
	 * Returns the volume of this {@code RectangularCuboid3D} instance.
	 * 
	 * @return the volume of this {@code RectangularCuboid3D} instance
	 */
//	TODO: Add Unit Tests!
	public double getVolume() {
		final double x = this.maximum.getX() - this.minimum.getX();
		final double y = this.maximum.getY() - this.minimum.getY();
		final double z = this.maximum.getZ() - this.minimum.getZ();
		final double volume = x * y * z;
		
		return volume;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code RectangularCuboid3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code RectangularCuboid3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Vector3D directionReciprocal = Vector3D.reciprocal(ray.getDirection());
		final Vector3D directionA = Vector3D.hadamardProduct(Vector3D.direction(ray.getOrigin(), getMinimum()), directionReciprocal);
		final Vector3D directionB = Vector3D.hadamardProduct(Vector3D.direction(ray.getOrigin(), getMaximum()), directionReciprocal);
		
		final double t0 = max(min(directionA.getX(), directionB.getX()), min(directionA.getY(), directionB.getY()), min(directionA.getZ(), directionB.getZ()));
		final double t1 = min(max(directionA.getX(), directionB.getX()), max(directionA.getY(), directionB.getY()), max(directionA.getZ(), directionB.getZ()));
		
		if(t0 > t1) {
			return Double.NaN;
		}
		
		if(t0 > tMinimum && t0 < tMaximum) {
			return t0;
		}
		
		if(t1 > tMinimum && t1 < tMaximum) {
			return t1;
		}
		
		return Double.NaN;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code RectangularCuboid3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code RectangularCuboid3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code RectangularCuboid3D} instance.
	 * 
	 * @return a hash code for this {@code RectangularCuboid3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.maximum, this.minimum);
	}
	
	/**
	 * Writes this {@code RectangularCuboid3D} instance to {@code dataOutput}.
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
	
	private OrthonormalBasis33D doCreateOrthonormalBasisG(final Point3D surfaceIntersectionPoint) {
		final Point3D midpoint = Point3D.midpoint(this.maximum, this.minimum);
		
		final Vector3D halfDirection = Vector3D.multiply(Vector3D.direction(this.minimum, this.maximum), 0.5D);
		
		if(surfaceIntersectionPoint.getX() + halfDirection.getX() - 0.0001D < midpoint.getX()) {
			return new OrthonormalBasis33D(Vector3D.x(-1.0D));
		}
		
		if(surfaceIntersectionPoint.getX() - halfDirection.getX() + 0.0001D > midpoint.getX()) {
			return new OrthonormalBasis33D(Vector3D.x(+1.0D));
		}
		
		if(surfaceIntersectionPoint.getY() + halfDirection.getY() - 0.0001D < midpoint.getY()) {
			return new OrthonormalBasis33D(Vector3D.y(-1.0D));
		}
		
		if(surfaceIntersectionPoint.getY() - halfDirection.getY() + 0.0001D > midpoint.getY()) {
			return new OrthonormalBasis33D(Vector3D.y(+1.0D));
		}
		
		if(surfaceIntersectionPoint.getZ() + halfDirection.getZ() - 0.0001D < midpoint.getZ()) {
			return new OrthonormalBasis33D(Vector3D.z(-1.0D));
		}
		
		if(surfaceIntersectionPoint.getZ() - halfDirection.getZ() + 0.0001D > midpoint.getZ()) {
			return new OrthonormalBasis33D(Vector3D.z(+1.0D));
		}
		
		return new OrthonormalBasis33D();
	}
	
	private Point2D doCreateTextureCoordinates(final Point3D surfaceIntersectionPoint) {
		final Point3D midpoint = Point3D.midpoint(this.maximum, this.minimum);
		
		final Vector3D halfDirection = Vector3D.multiply(Vector3D.direction(this.minimum, this.maximum), 0.5D);
		
		if(surfaceIntersectionPoint.getX() + halfDirection.getX() - 0.0001D < midpoint.getX() || surfaceIntersectionPoint.getX() - halfDirection.getX() + 0.0001D > midpoint.getX()) {
			final double u = normalize(surfaceIntersectionPoint.getZ(), this.minimum.getZ(), this.maximum.getZ());
			final double v = normalize(surfaceIntersectionPoint.getY(), this.minimum.getY(), this.maximum.getY());
			
			return new Point2D(u, v);
		}
		
		if(surfaceIntersectionPoint.getY() + halfDirection.getY() - 0.0001D < midpoint.getY() || surfaceIntersectionPoint.getY() - halfDirection.getY() + 0.0001D > midpoint.getY()) {
			final double u = normalize(surfaceIntersectionPoint.getX(), this.minimum.getX(), this.maximum.getX());
			final double v = normalize(surfaceIntersectionPoint.getZ(), this.minimum.getZ(), this.maximum.getZ());
			
			return new Point2D(u, v);
		}
		
		if(surfaceIntersectionPoint.getZ() + halfDirection.getZ() - 0.0001D < midpoint.getZ() || surfaceIntersectionPoint.getZ() - halfDirection.getZ() + 0.0001D > midpoint.getZ()) {
			final double u = normalize(surfaceIntersectionPoint.getX(), this.minimum.getX(), this.maximum.getX());
			final double v = normalize(surfaceIntersectionPoint.getY(), this.minimum.getY(), this.maximum.getY());
			
			return new Point2D(u, v);
		}
		
		return new Point2D();
	}
	
	private SurfaceIntersection3D doCreateSurfaceIntersection(final Ray3D ray, final double t) {
		final Point3D surfaceIntersectionPoint = doCreateSurfaceIntersectionPoint(ray, t);
		
		final Point2D textureCoordinates = doCreateTextureCoordinates(surfaceIntersectionPoint);
		
		final OrthonormalBasis33D orthonormalBasisG = doCreateOrthonormalBasisG(surfaceIntersectionPoint);
		final OrthonormalBasis33D orthonormalBasisS = orthonormalBasisG;
		
		final Vector3D surfaceIntersectionPointError = new Vector3D();
		
		return new SurfaceIntersection3D(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point3D doCreateSurfaceIntersectionPoint(final Ray3D ray, final double t) {
		return Point3D.add(ray.getOrigin(), ray.getDirection(), t);
	}
}