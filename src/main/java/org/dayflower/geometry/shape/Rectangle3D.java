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

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
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
 * A {@code Rectangle3D} denotes a 3-dimensional rectangle that uses the data type {@code double}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@link Shape3D} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Rectangle3D implements Shape3D {
	/**
	 * The name of this {@code Rectangle3D} class.
	 */
	public static final String NAME = "Rectangle";
	
	/**
	 * The length of the {@code double[]}.
	 */
	public static final int ARRAY_LENGTH = 12;
	
	/**
	 * The offset for the {@link Point3D} instance denoted by {@code Position} in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_POSITION = 0;
	
	/**
	 * The offset for the {@link Vector3D} instance denoted by {@code Side A} in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_SIDE_A = 3;
	
	/**
	 * The offset for the {@link Vector3D} instance denoted by {@code Side B} in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_SIDE_B = 6;
	
	/**
	 * The offset for the {@link Vector3D} instance denoted by {@code Surface Normal} in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_SURFACE_NORMAL = 9;
	
	/**
	 * The ID of this {@code Rectangle3D} class.
	 */
	public static final int ID = 11;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point3D position;
	private final Vector3D sideA;
	private final Vector3D sideB;
	private final Vector3D surfaceNormal;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Rectangle3D} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Rectangle3D(new Point3D());
	 * }
	 * </pre>
	 */
	public Rectangle3D() {
		this(new Point3D());
	}
	
	/**
	 * Constructs a new {@code Rectangle3D} instance.
	 * <p>
	 * If {@code position} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Rectangle3D(position, Vector3D.x(2.0D), Vector3D.y(2.0D));
	 * }
	 * </pre>
	 * 
	 * @param position the position to use
	 * @throws NullPointerException thrown if, and only if, {@code position} is {@code null}
	 */
	public Rectangle3D(final Point3D position) {
		this(position, Vector3D.x(2.0D), Vector3D.y(2.0D));
	}
	
	/**
	 * Constructs a new {@code Rectangle3D} instance.
	 * <p>
	 * If either {@code position}, {@code sideA} or {@code sideB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param position the position to use
	 * @param sideA the direction and length of the side denoted by {@code A}
	 * @param sideB the direction and length of the side denoted by {@code B}
	 * @throws NullPointerException thrown if, and only if, either {@code position}, {@code sideA} or {@code sideB} are {@code null}
	 */
	public Rectangle3D(final Point3D position, final Vector3D sideA, final Vector3D sideB) {
		this.position = Objects.requireNonNull(position, "position == null");
		this.sideA = Objects.requireNonNull(sideA, "sideA == null");
		this.sideB = Objects.requireNonNull(sideB, "sideB == null");
		this.surfaceNormal = Vector3D.normalize(Vector3D.crossProduct(sideA, sideB));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3D} instance that contains this {@code Rectangle3D} instance.
	 * 
	 * @return a {@code BoundingVolume3D} instance that contains this {@code Rectangle3D} instance
	 */
	@Override
	public BoundingVolume3D getBoundingVolume() {
		return AxisAlignedBoundingBox3D.union(new AxisAlignedBoundingBox3D(this.position, Point3D.add(this.position, this.sideA)), Point3D.add(this.position, this.sideB));
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Rectangle3D} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3D} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Rectangle3D} instance
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
		final Point3D position = this.position;
		
		final Vector3D originToPosition = Vector3D.direction(origin, position);
		
		final double t = Vector3D.dotProduct(originToPosition, surfaceNormal) / nDotD;
		
		if(t <= tMinimum || t >= tMaximum) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final Vector3D sideA = this.sideA;
		final Vector3D sideB = this.sideB;
		
		final double sideALength = sideA.length();
		final double sideBLength = sideB.length();
		
		final Vector3D sideANormalized = Vector3D.normalize(sideA);
		final Vector3D sideBNormalized = Vector3D.normalize(sideB);
		
		final Point3D surfaceIntersectionPoint = Point3D.add(origin, direction, t);
		
		final Vector3D positionToSurfaceIntersectionPoint = Vector3D.direction(position, surfaceIntersectionPoint);
		
		final double sideX = Vector3D.dotProduct(positionToSurfaceIntersectionPoint, sideANormalized);
		final double sideY = Vector3D.dotProduct(positionToSurfaceIntersectionPoint, sideBNormalized);
		
		if(sideX < 0.0D || sideX > sideALength || sideY < 0.0D || sideY > sideBLength) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final OrthonormalBasis33D orthonormalBasisG = new OrthonormalBasis33D(surfaceNormal);
		final OrthonormalBasis33D orthonormalBasisS = orthonormalBasisG;
		
		final double x = abs(surfaceNormal.getX());
		final double y = abs(surfaceNormal.getY());
		final double z = abs(surfaceNormal.getZ());
		
		final boolean isX = x > y && x > z;
		final boolean isY = y > z;
		
		final Point3D a = position;
		final Point3D b = Point3D.add(a, sideA);
		final Point3D c = Point3D.add(a, sideB);
		
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
		
		final Vector3D surfaceIntersectionPointError = new Vector3D();
		
		return Optional.of(new SurfaceIntersection3D(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
	}
	
	/**
	 * Returns the position of this {@code Rectangle3D} instance.
	 * 
	 * @return the position of this {@code Rectangle3D} instance
	 */
	public Point3D getPosition() {
		return this.position;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Rectangle3D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Rectangle3D} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Rectangle3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Rectangle3D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Rectangle3D(%s, %s, %s)", this.position, this.sideA, this.sideB);
	}
	
	/**
	 * Returns the direction and length of the side denoted by {@code A}.
	 * 
	 * @return the direction and length of the side denoted by {@code A}
	 */
	public Vector3D getSideA() {
		return this.sideA;
	}
	
	/**
	 * Returns the direction and length of the side denoted by {@code B}.
	 * 
	 * @return the direction and length of the side denoted by {@code B}
	 */
	public Vector3D getSideB() {
		return this.sideB;
	}
	
	/**
	 * Returns the surface normal of this {@code Rectangle3D} instance.
	 * 
	 * @return the surface normal of this {@code Rectangle3D} instance
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
				if(!this.position.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.sideA.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.sideB.accept(nodeHierarchicalVisitor)) {
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
	 * Compares {@code object} to this {@code Rectangle3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Rectangle3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Rectangle3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Rectangle3D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Rectangle3D)) {
			return false;
		} else if(!Objects.equals(this.position, Rectangle3D.class.cast(object).position)) {
			return false;
		} else if(!Objects.equals(this.sideA, Rectangle3D.class.cast(object).sideA)) {
			return false;
		} else if(!Objects.equals(this.sideB, Rectangle3D.class.cast(object).sideB)) {
			return false;
		} else if(!Objects.equals(this.surfaceNormal, Rectangle3D.class.cast(object).surfaceNormal)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code Rectangle3D} instance.
	 * 
	 * @return the surface area of this {@code Rectangle3D} instance
	 */
	@Override
	public double getSurfaceArea() {
		return Vector3D.crossProduct(this.sideA, this.sideB).length();
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Rectangle3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Rectangle3D} instance
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
		final Point3D position = this.position;
		
		final Vector3D originToPosition = Vector3D.direction(origin, position);
		
		final double t = Vector3D.dotProduct(originToPosition, surfaceNormal) / nDotD;
		
		if(t <= tMinimum || t >= tMaximum) {
			return Double.NaN;
		}
		
		final Vector3D sideA = this.sideA;
		final Vector3D sideB = this.sideB;
		
		final double sideALength = sideA.length();
		final double sideBLength = sideB.length();
		
		final Vector3D sideANormalized = Vector3D.normalize(sideA);
		final Vector3D sideBNormalized = Vector3D.normalize(sideB);
		
		final Point3D surfaceIntersectionPoint = Point3D.add(origin, direction, t);
		
		final Vector3D positionToSurfaceIntersectionPoint = Vector3D.direction(position, surfaceIntersectionPoint);
		
		final double sideX = Vector3D.dotProduct(positionToSurfaceIntersectionPoint, sideANormalized);
		final double sideY = Vector3D.dotProduct(positionToSurfaceIntersectionPoint, sideBNormalized);
		
		if(sideX < 0.0D || sideX > sideALength || sideY < 0.0D || sideY > sideBLength) {
			return Double.NaN;
		}
		
		return t;
	}
	
	/**
	 * Returns a {@code double[]} representation of this {@code Rectangle3D} instance.
	 * 
	 * @return a {@code double[]} representation of this {@code Rectangle3D} instance
	 */
	public double[] toArray() {
		final double[] array = new double[ARRAY_LENGTH];
		
		array[ARRAY_OFFSET_POSITION + 0] = this.position.getX();			//Block #1
		array[ARRAY_OFFSET_POSITION + 1] = this.position.getY();			//Block #1
		array[ARRAY_OFFSET_POSITION + 2] = this.position.getZ();			//Block #1
		array[ARRAY_OFFSET_SIDE_A + 0] = this.sideA.getX();					//Block #1
		array[ARRAY_OFFSET_SIDE_A + 1] = this.sideA.getY();					//Block #1
		array[ARRAY_OFFSET_SIDE_A + 2] = this.sideA.getZ();					//Block #1
		array[ARRAY_OFFSET_SIDE_B + 0] = this.sideB.getX();					//Block #1
		array[ARRAY_OFFSET_SIDE_B + 1] = this.sideB.getY();					//Block #1
		array[ARRAY_OFFSET_SIDE_B + 2] = this.sideB.getZ();					//Block #2
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
	 * Returns an {@code int} with the ID of this {@code Rectangle3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Rectangle3D} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Rectangle3D} instance.
	 * 
	 * @return a hash code for this {@code Rectangle3D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.position, this.sideA, this.sideB, this.surfaceNormal);
	}
	
	/**
	 * Writes this {@code Rectangle3D} instance to {@code dataOutput}.
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
			
			this.position.write(dataOutput);
			this.sideA.write(dataOutput);
			this.sideB.write(dataOutput);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}