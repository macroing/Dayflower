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
import static org.dayflower.utility.Doubles.isZero;

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
import org.dayflower.geometry.Vector2D;
import org.dayflower.geometry.Vector3D;
import org.dayflower.geometry.boundingvolume.InfiniteBoundingVolume3D;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code Plane3D} is an implementation of {@link Shape3D} that represents a plane.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3D} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Plane3D implements Shape3D {
	/**
	 * The name of this {@code Plane3D} class.
	 */
//	TODO: Add Unit Tests!
	public static final String NAME = "Plane";
	
	/**
	 * The ID of this {@code Plane3D} class.
	 */
//	TODO: Add Unit Tests!
	public static final int ID = 10;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point3D a;
	private final Point3D b;
	private final Point3D c;
	private final Vector3D surfaceNormal;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Plane3D} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Plane3D(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 0.0D, 0.0D));
	 * }
	 * </pre>
	 */
//	TODO: Add Unit Tests!
	public Plane3D() {
		this(new Point3D(0.0D, 0.0D, 0.0D), new Point3D(0.0D, 0.0D, 1.0D), new Point3D(1.0D, 0.0D, 0.0D));
	}
	
	/**
	 * Constructs a new {@code Plane3D} instance given the three {@link Point3D} instances {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a the {@code Point3D} instance denoted by {@code A}
	 * @param b the {@code Point3D} instance denoted by {@code B}
	 * @param c the {@code Point3D} instance denoted by {@code C}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Plane3D(final Point3D a, final Point3D b, final Point3D c) {
		this.a = Point3D.getCached(Objects.requireNonNull(a, "a == null"));
		this.b = Point3D.getCached(Objects.requireNonNull(b, "b == null"));
		this.c = Point3D.getCached(Objects.requireNonNull(c, "c == null"));
		this.surfaceNormal = Vector3D.getCached(Vector3D.normalNormalized(a, b, c));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3D} instance that contains this {@code Plane3D} instance.
	 * 
	 * @return a {@code BoundingVolume3D} instance that contains this {@code Plane3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public BoundingVolume3D getBoundingVolume() {
		return new InfiniteBoundingVolume3D();
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Plane3D} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3D} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Plane3D} instance
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
	 * Returns the {@link Point3D} instance denoted by {@code A}.
	 * 
	 * @return the {@code Point3D} instance denoted by {@code A}
	 */
//	TODO: Add Unit Tests!
	public Point3D getA() {
		return this.a;
	}
	
	/**
	 * Returns the {@link Point3D} instance denoted by {@code B}.
	 * 
	 * @return the {@code Point3D} instance denoted by {@code B}
	 */
//	TODO: Add Unit Tests!
	public Point3D getB() {
		return this.b;
	}
	
	/**
	 * Returns the {@link Point3D} instance denoted by {@code C}.
	 * 
	 * @return the {@code Point3D} instance denoted by {@code C}
	 */
//	TODO: Add Unit Tests!
	public Point3D getC() {
		return this.c;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Plane3D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Plane3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Plane3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Plane3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new Plane3D(%s, %s, %s)", this.a, this.b, this.c);
	}
	
	/**
	 * Returns the {@link Vector3D} instance used as surface normal.
	 * 
	 * @return the {@code Vector3D} instance used as surface normal
	 */
//	TODO: Add Unit Tests!
	public Vector3D getSurfaceNormal() {
		return this.surfaceNormal;
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
				
				if(!this.c.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
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
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Plane3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3D} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Plane3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean contains(final Point3D point) {
		return Point3D.coplanar(this.a, this.b, this.c, point);
	}
	
	/**
	 * Compares {@code object} to this {@code Plane3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Plane3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Plane3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Plane3D}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Plane3D)) {
			return false;
		} else if(!Objects.equals(this.a, Plane3D.class.cast(object).a)) {
			return false;
		} else if(!Objects.equals(this.b, Plane3D.class.cast(object).b)) {
			return false;
		} else if(!Objects.equals(this.c, Plane3D.class.cast(object).c)) {
			return false;
		} else if(!Objects.equals(this.surfaceNormal, Plane3D.class.cast(object).surfaceNormal)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code Plane3D} instance.
	 * <p>
	 * This method returns {@code Double.POSITIVE_INFINITY}.
	 * 
	 * @return the surface area of this {@code Plane3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public double getSurfaceArea() {
		return Double.POSITIVE_INFINITY;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Plane3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Plane3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final double dotProduct = Vector3D.dotProduct(this.surfaceNormal, ray.getDirection());
		
		if(isZero(dotProduct)) {
			return Double.NaN;
		}
		
		final double t = Vector3D.dotProduct(Vector3D.direction(ray.getOrigin(), this.a), this.surfaceNormal) / dotProduct;
		
		if(t > tMinimum && t < tMaximum) {
			return t;
		}
		
		return Double.NaN;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Plane3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Plane3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Plane3D} instance.
	 * 
	 * @return a hash code for this {@code Plane3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.a, this.b, this.c, this.surfaceNormal);
	}
	
	/**
	 * Writes this {@code Plane3D} instance to {@code dataOutput}.
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
			this.c.write(dataOutput);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private OrthonormalBasis33D doCreateOrthonormalBasisG() {
		return new OrthonormalBasis33D(this.surfaceNormal);
	}
	
	private Point2D doCreateTextureCoordinates(final Point3D surfaceIntersectionPoint) {
		final Vector3D surfaceNormalAbs = Vector3D.absolute(this.surfaceNormal);
		
		final boolean isX = surfaceNormalAbs.getX() > surfaceNormalAbs.getY() && surfaceNormalAbs.getX() > surfaceNormalAbs.getZ();
		final boolean isY = surfaceNormalAbs.getY() > surfaceNormalAbs.getZ();
		
		final Vector2D vA = isX ? Vector2D.directionYZ(this.a) : isY ? Vector2D.directionZX(this.a) : Vector2D.directionXY(this.a);
		final Vector2D vB = isX ? Vector2D.directionYZ(this.b) : isY ? Vector2D.directionZX(this.b) : Vector2D.directionXY(this.b);
		final Vector2D vC = isX ? Vector2D.directionYZ(this.c) : isY ? Vector2D.directionZX(this.c) : Vector2D.directionXY(this.c);
		final Vector2D vAB = Vector2D.subtract(vB, vA);
		final Vector2D vAC = Vector2D.subtract(vC, vA);
		
		final double determinant = Vector2D.crossProduct(vAC, vAB);
		final double determinantReciprocal = 1.0D / determinant;
		
		final double hU = isX ? surfaceIntersectionPoint.getY() : isY ? surfaceIntersectionPoint.getZ() : surfaceIntersectionPoint.getX();
		final double hV = isX ? surfaceIntersectionPoint.getZ() : isY ? surfaceIntersectionPoint.getX() : surfaceIntersectionPoint.getY();
		
		final double u = hU * (-vAC.getY() * determinantReciprocal) + hV * (+vAC.getX() * determinantReciprocal) + Vector2D.crossProduct(vAC, vA) * determinantReciprocal;
		final double v = hU * (+vAB.getY() * determinantReciprocal) + hV * (-vAB.getX() * determinantReciprocal) + Vector2D.crossProduct(vAB, vA) * determinantReciprocal;
		
		return new Point2D(u, v);
	}
	
	private SurfaceIntersection3D doCreateSurfaceIntersection(final Ray3D ray, final double t) {
		final Point3D surfaceIntersectionPoint = doCreateSurfaceIntersectionPoint(ray, t);
		
		final OrthonormalBasis33D orthonormalBasisG = doCreateOrthonormalBasisG();
		final OrthonormalBasis33D orthonormalBasisS = orthonormalBasisG;
		
		final Point2D textureCoordinates = doCreateTextureCoordinates(surfaceIntersectionPoint);
		
		final Vector3D surfaceIntersectionPointError = new Vector3D();
		
		return new SurfaceIntersection3D(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point3D doCreateSurfaceIntersectionPoint(final Ray3D ray, final double t) {
		return Point3D.add(ray.getOrigin(), ray.getDirection(), t);
	}
}