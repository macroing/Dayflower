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
package org.dayflower.geometry.shape;

import static org.dayflower.utility.Doubles.abs;
import static org.dayflower.utility.Doubles.isZero;

import java.lang.reflect.Field;
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
import org.dayflower.geometry.boundingvolume.InfiniteBoundingVolume3D;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code Plane3D} denotes a 3-dimensional plane that uses the data type {@code double}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Plane3D implements Shape3D {
	/**
	 * The name of this {@code Plane3D} class.
	 */
	public static final String NAME = "Plane";
	
	/**
	 * The length of the {@code double[]}.
	 */
	public static final int ARRAY_LENGTH = 16;
	
	/**
	 * The offset for the {@link Point3D} instance denoted by {@code A} in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_A = 0;
	
	/**
	 * The offset for the {@link Point3D} instance denoted by {@code B} in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_B = 3;
	
	/**
	 * The offset for the {@link Point3D} instance denoted by {@code C} in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_C = 6;
	
	/**
	 * The offset for the {@link Vector3D} instance representing the surface normal in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_SURFACE_NORMAL = 9;
	
	/**
	 * The ID of this {@code Plane3D} class.
	 */
	public static final int ID = 4;
	
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
	@Override
	public Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Vector3D direction = ray.getDirection();
		final Vector3D surfaceNormal = this.surfaceNormal;
		
		final double nDotD = Vector3D.dotProduct(surfaceNormal, direction);
		
		if(isZero(nDotD)) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final Point3D origin = ray.getOrigin();
		final Point3D a = this.a;
		final Point3D b = this.b;
		final Point3D c = this.c;
		
		final Vector3D originToA = Vector3D.direction(origin, a);
		
		final double t = Vector3D.dotProduct(originToA, surfaceNormal) / nDotD;
		
		if(t <= tMinimum || t >= tMaximum) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final Point3D surfaceIntersectionPoint = Point3D.add(origin, direction, t);
		
		final OrthonormalBasis33D orthonormalBasisG = new OrthonormalBasis33D(surfaceNormal);
		final OrthonormalBasis33D orthonormalBasisS = orthonormalBasisG;
		
		final double x = abs(surfaceNormal.getX());
		final double y = abs(surfaceNormal.getY());
		final double z = abs(surfaceNormal.getZ());
		
		final boolean isX = x > y && x > z;
		final boolean isY = y > z;
		
		final double aX = isX ? a.getY()      : isY ? a.getZ()      : a.getX();
		final double aY = isX ? a.getZ()      : isY ? a.getX()      : a.getY();
		final double bX = isX ? c.getY() - aX : isY ? c.getZ() - aX : c.getX() - aX;
		final double bY = isX ? c.getZ() - aY : isY ? c.getX() - aY : c.getY() - aY;
		final double cX = isX ? b.getY() - aX : isY ? b.getZ() - aX : b.getX() - aX;
		final double cY = isX ? b.getZ() - aY : isY ? b.getX() - aY : b.getY() - aY;
		
		final double determinant = bX * cY - bY * cX;
		final double determinantReciprocal = 1.0D / determinant;
		
		final double hU = isX ? surfaceIntersectionPoint.getY() : isY ? surfaceIntersectionPoint.getZ() : surfaceIntersectionPoint.getX();
		final double hV = isX ? surfaceIntersectionPoint.getZ() : isY ? surfaceIntersectionPoint.getX() : surfaceIntersectionPoint.getY();
		
		final double u = hU * (-bY * determinantReciprocal) + hV * (+bX * determinantReciprocal) + (bY * aX - bX * aY) * determinantReciprocal;
		final double v = hU * (+cY * determinantReciprocal) + hV * (-cX * determinantReciprocal) + (cX * aY - cY * aX) * determinantReciprocal;
		
		final Point2D textureCoordinates = new Point2D(u, v);
		
//		TODO: Implement!
		final Vector3D surfaceIntersectionPointError = new Vector3D();
		
		return Optional.of(new SurfaceIntersection3D(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
	}
	
	/**
	 * Returns the {@link Point3D} instance denoted by {@code A}.
	 * 
	 * @return the {@code Point3D} instance denoted by {@code A}
	 */
	public Point3D getA() {
		return this.a;
	}
	
	/**
	 * Returns the {@link Point3D} instance denoted by {@code B}.
	 * 
	 * @return the {@code Point3D} instance denoted by {@code B}
	 */
	public Point3D getB() {
		return this.b;
	}
	
	/**
	 * Returns the {@link Point3D} instance denoted by {@code C}.
	 * 
	 * @return the {@code Point3D} instance denoted by {@code C}
	 */
	public Point3D getC() {
		return this.c;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Plane3D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Plane3D} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Plane3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Plane3D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Plane3D(%s, %s, %s)", this.a, this.b, this.c);
	}
	
	/**
	 * Returns the {@link Vector3D} instance used as surface normal.
	 * 
	 * @return the {@code Vector3D} instance used as surface normal
	 */
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
	 * Compares {@code object} to this {@code Plane3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Plane3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Plane3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Plane3D}, and their respective values are equal, {@code false} otherwise
	 */
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
	@Override
	public double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Vector3D direction = ray.getDirection();
		final Vector3D surfaceNormal = this.surfaceNormal;
		
		final double nDotD = Vector3D.dotProduct(surfaceNormal, direction);
		
		if(isZero(nDotD)) {
			return Double.NaN;
		}
		
		final Point3D origin = ray.getOrigin();
		final Point3D a = this.a;
		
		final Vector3D originToA = Vector3D.direction(origin, a);
		
		final double t = Vector3D.dotProduct(originToA, surfaceNormal) / nDotD;
		
		if(t <= tMinimum || t >= tMaximum) {
			return Double.NaN;
		}
		
		return t;
	}
	
	/**
	 * Returns a {@code double[]} representation of this {@code Plane3D} instance.
	 * 
	 * @return a {@code double[]} representation of this {@code Plane3D} instance
	 */
	public double[] toArray() {
		final double[] array = new double[ARRAY_LENGTH];
		
		array[ARRAY_OFFSET_A + 0] = this.a.getX();							//Block #1
		array[ARRAY_OFFSET_A + 1] = this.a.getY();							//Block #1
		array[ARRAY_OFFSET_A + 2] = this.a.getZ();							//Block #1
		array[ARRAY_OFFSET_B + 0] = this.b.getX();							//Block #1
		array[ARRAY_OFFSET_B + 1] = this.b.getY();							//Block #1
		array[ARRAY_OFFSET_B + 2] = this.b.getZ();							//Block #1
		array[ARRAY_OFFSET_C + 0] = this.c.getX();							//Block #1
		array[ARRAY_OFFSET_C + 1] = this.c.getY();							//Block #1
		array[ARRAY_OFFSET_C + 2] = this.c.getZ();							//Block #2
		array[ARRAY_OFFSET_SURFACE_NORMAL + 0] = this.surfaceNormal.getX();	//Block #2
		array[ARRAY_OFFSET_SURFACE_NORMAL + 1] = this.surfaceNormal.getY();	//Block #2
		array[ARRAY_OFFSET_SURFACE_NORMAL + 2] = this.surfaceNormal.getZ();	//Block #2
		array[12] = 0.0D;													//Block #2
		array[13] = 0.0D;													//Block #2
		array[14] = 0.0D;													//Block #2
		array[15] = 0.0D;													//Block #2
		
		return array;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Plane3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Plane3D} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Plane3D} instance.
	 * 
	 * @return a hash code for this {@code Plane3D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.a, this.b, this.c, this.surfaceNormal);
	}
}