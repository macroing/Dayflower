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

import static org.dayflower.utility.Doubles.solveQuartic;

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
import org.dayflower.geometry.boundingvolume.BoundingSphere3D;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.macroing.java.lang.Doubles;

/**
 * A {@code Torus3D} is an implementation of {@link Shape3D} that represents a torus.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3D} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Torus3D implements Shape3D {
	/**
	 * The name of this {@code Torus3D} class.
	 */
//	TODO: Add Unit Tests!
	public static final String NAME = "Torus";
	
	/**
	 * The ID of this {@code Torus3D} class.
	 */
//	TODO: Add Unit Tests!
	public static final int ID = 16;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final BoundingVolume3D boundingVolume;
	private final double radiusInner;
	private final double radiusInnerSquared;
	private final double radiusOuter;
	private final double radiusOuterSquared;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Torus3D} instance with an inner radius of {@code 0.25D} and an outer radius of {@code 1.0D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Torus3D(0.25D, 1.0D);
	 * }
	 * </pre>
	 */
//	TODO: Add Unit Tests!
	public Torus3D() {
		this(0.25D, 1.0D);
	}
	
	/**
	 * Constructs a new {@code Torus3D} instance with an inner radius of {@code radiusInner} and an outer radius of {@code radiusOuter}.
	 * 
	 * @param radiusInner the inner radius of this {@code Torus3D} instance
	 * @param radiusOuter the outer radius of this {@code Torus3D} instance
	 */
//	TODO: Add Unit Tests!
	public Torus3D(final double radiusInner, final double radiusOuter) {
		this.boundingVolume = new BoundingSphere3D(radiusInner + radiusOuter, new Point3D());
		this.radiusInner = radiusInner;
		this.radiusInnerSquared = radiusInner * radiusInner;
		this.radiusOuter = radiusOuter;
		this.radiusOuterSquared = radiusOuter * radiusOuter;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3D} instance that contains this {@code Torus3D} instance.
	 * 
	 * @return a {@code BoundingVolume3D} instance that contains this {@code Torus3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public BoundingVolume3D getBoundingVolume() {
		return this.boundingVolume;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Torus3D} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3D} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Torus3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3D} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final double t = intersectionT(ray, tMinimum, tMaximum);
		
		if(Doubles.isNaN(t)) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		return Optional.of(doCreateSurfaceIntersection(ray, t));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Torus3D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Torus3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Torus3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Torus3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new Torus3D(%+.10f, %+.10f)", Double.valueOf(this.radiusInner), Double.valueOf(this.radiusOuter));
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
				if(!this.boundingVolume.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Torus3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method currently returns {@code false}.
	 * 
	 * @param point a {@link Point3D} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Torus3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean contains(final Point3D point) {
		Objects.requireNonNull(point, "point == null");
		
		return false;
	}
	
	/**
	 * Compares {@code object} to this {@code Torus3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Torus3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Torus3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Torus3D}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Torus3D)) {
			return false;
		} else if(!Objects.equals(this.boundingVolume, Torus3D.class.cast(object).boundingVolume)) {
			return false;
		} else if(!Doubles.equals(this.radiusInner, Torus3D.class.cast(object).radiusInner)) {
			return false;
		} else if(!Doubles.equals(this.radiusInnerSquared, Torus3D.class.cast(object).radiusInnerSquared)) {
			return false;
		} else if(!Doubles.equals(this.radiusOuter, Torus3D.class.cast(object).radiusOuter)) {
			return false;
		} else if(!Doubles.equals(this.radiusOuterSquared, Torus3D.class.cast(object).radiusOuterSquared)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the inner radius of this {@code Torus3D} instance.
	 * 
	 * @return the inner radius of this {@code Torus3D} instance
	 */
//	TODO: Add Unit Tests!
	public double getRadiusInner() {
		return this.radiusInner;
	}
	
	/**
	 * Returns the squared inner radius of this {@code Torus3D} instance.
	 * 
	 * @return the squared inner radius of this {@code Torus3D} instance
	 */
//	TODO: Add Unit Tests!
	public double getRadiusInnerSquared() {
		return this.radiusInnerSquared;
	}
	
	/**
	 * Returns the outer radius of this {@code Torus3D} instance.
	 * 
	 * @return the outer radius of this {@code Torus3D} instance
	 */
//	TODO: Add Unit Tests!
	public double getRadiusOuter() {
		return this.radiusOuter;
	}
	
	/**
	 * Returns the squared outer radius of this {@code Torus3D} instance.
	 * 
	 * @return the squared outer radius of this {@code Torus3D} instance
	 */
//	TODO: Add Unit Tests!
	public double getRadiusOuterSquared() {
		return this.radiusOuterSquared;
	}
	
	/**
	 * Returns the surface area of this {@code Torus3D} instance.
	 * 
	 * @return the surface area of this {@code Torus3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public double getSurfaceArea() {
		return 4.0D * Doubles.PI * Doubles.PI * this.radiusOuter * this.radiusInner;
	}
	
	/**
	 * Returns the volume of this {@code Torus3D} instance.
	 * 
	 * @return the volume of this {@code Torus3D} instance
	 */
//	TODO: Add Unit Tests!
	public double getVolume() {
		return 2.0D * Doubles.PI * Doubles.PI * this.radiusOuter * this.radiusInnerSquared;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Torus3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Torus3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Point3D origin = ray.getOrigin();
		
		final Vector3D direction = ray.getDirection();
		final Vector3D directionToOrigin = new Vector3D(origin);
		
		final double f0 = direction.lengthSquared();
		final double f1 = Vector3D.dotProduct(directionToOrigin, direction) * 2.0D;
		final double f2 = this.radiusInnerSquared;
		final double f3 = this.radiusOuterSquared;
		final double f4 = directionToOrigin.lengthSquared() - f2 - f3;
		final double f5 = direction.z;
		final double f6 = directionToOrigin.z;
		
		final double a = f0 * f0;
		final double b = f0 * 2.0D * f1;
		final double c = f1 * f1 + 2.0D * f0 * f4 + 4.0D * f3 * f5 * f5;
		final double d = f1 * 2.0D * f4 + 8.0D * f3 * f6 * f5;
		final double e = f4 * f4 + 4.0D * f3 * f6 * f6 - 4.0D * f3 * f2;
		
		final double[] ts = solveQuartic(a, b, c, d, e);
		
		if(ts.length == 0) {
			return Double.NaN;
		}
		
		if(ts[0] >= tMaximum || ts[ts.length - 1] <= tMinimum) {
			return Double.NaN;
		}
		
		for(int i = 0; i < ts.length; i++) {
			if(ts[i] > tMinimum) {
				return ts[i];
			}
		}
		
		return Double.NaN;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Torus3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Torus3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Torus3D} instance.
	 * 
	 * @return a hash code for this {@code Torus3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.boundingVolume, Double.valueOf(this.radiusInner), Double.valueOf(this.radiusInnerSquared), Double.valueOf(this.radiusOuter), Double.valueOf(this.radiusOuterSquared));
	}
	
	/**
	 * Writes this {@code Torus3D} instance to {@code dataOutput}.
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
			dataOutput.writeDouble(this.radiusInner);
			dataOutput.writeDouble(this.radiusOuter);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private OrthonormalBasis33D doCreateOrthonormalBasisG(final Point3D surfaceIntersectionPoint) {
		final Vector3D direction = new Vector3D(surfaceIntersectionPoint);
		
		final double derivative = direction.lengthSquared() - this.radiusInnerSquared - this.radiusOuterSquared;
		
		final double x = surfaceIntersectionPoint.x * derivative;
		final double y = surfaceIntersectionPoint.y * derivative;
		final double z = surfaceIntersectionPoint.z * derivative + 2.0D * this.radiusOuterSquared * surfaceIntersectionPoint.z;
		
		final Vector3D w = Vector3D.normalize(new Vector3D(x, y, z));
		
		return new OrthonormalBasis33D(w);
	}
	
	private Point2D doCreateTextureCoordinates(final Point3D surfaceIntersectionPoint) {
		final double phi = Doubles.asin(Doubles.saturate(surfaceIntersectionPoint.z / this.radiusInner, -1.0D, 1.0D));
		final double theta = Doubles.addLessThan(Doubles.atan2(surfaceIntersectionPoint.y, surfaceIntersectionPoint.x), 0.0D, Doubles.PI_MULTIPLIED_BY_2);
		
		final double u = theta * Doubles.PI_MULTIPLIED_BY_2_RECIPROCAL;
		final double v = (phi + Doubles.PI_DIVIDED_BY_2) * Doubles.PI_RECIPROCAL;
		
		return new Point2D(u, v);
	}
	
	private SurfaceIntersection3D doCreateSurfaceIntersection(final Ray3D ray, final double t) {
		final Point3D surfaceIntersectionPoint = doCreateSurfaceIntersectionPoint(ray, t);
		
		final OrthonormalBasis33D orthonormalBasisG = doCreateOrthonormalBasisG(surfaceIntersectionPoint);
		final OrthonormalBasis33D orthonormalBasisS = orthonormalBasisG;
		
		final Point2D textureCoordinates = doCreateTextureCoordinates(surfaceIntersectionPoint);
		
		return new SurfaceIntersection3D(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, t);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point3D doCreateSurfaceIntersectionPoint(final Ray3D ray, final double t) {
		return Point3D.add(ray.getOrigin(), ray.getDirection(), t);
	}
}