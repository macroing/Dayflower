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

import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.isZero;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
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

/**
 * A {@code Rectangle3F} is an implementation of {@link Shape3F} that represents a rectangle.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3F} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Rectangle3F implements Shape3F {
	/**
	 * The name of this {@code Rectangle3F} class.
	 */
	public static final String NAME = "Rectangle";
	
	/**
	 * The ID of this {@code Rectangle3F} class.
	 */
	public static final int ID = 11;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point3F position;
	private final Vector3F sideA;
	private final Vector3F sideB;
	private final Vector3F surfaceNormal;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Rectangle3F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Rectangle3F(new Point3F());
	 * }
	 * </pre>
	 */
	public Rectangle3F() {
		this(new Point3F());
	}
	
	/**
	 * Constructs a new {@code Rectangle3F} instance.
	 * <p>
	 * If {@code position} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Rectangle3F(position, Vector3F.x(2.0F), Vector3F.y(2.0F));
	 * }
	 * </pre>
	 * 
	 * @param position the position to use
	 * @throws NullPointerException thrown if, and only if, {@code position} is {@code null}
	 */
	public Rectangle3F(final Point3F position) {
		this(position, Vector3F.x(2.0F), Vector3F.y(2.0F));
	}
	
	/**
	 * Constructs a new {@code Rectangle3F} instance.
	 * <p>
	 * If either {@code position}, {@code sideA} or {@code sideB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param position the position to use
	 * @param sideA the direction and length of the side denoted by {@code A}
	 * @param sideB the direction and length of the side denoted by {@code B}
	 * @throws NullPointerException thrown if, and only if, either {@code position}, {@code sideA} or {@code sideB} are {@code null}
	 */
	public Rectangle3F(final Point3F position, final Vector3F sideA, final Vector3F sideB) {
		this.position = Objects.requireNonNull(position, "position == null");
		this.sideA = Objects.requireNonNull(sideA, "sideA == null");
		this.sideB = Objects.requireNonNull(sideB, "sideB == null");
		this.surfaceNormal = Vector3F.normalize(Vector3F.crossProduct(sideA, sideB));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code Rectangle3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code Rectangle3F} instance
	 */
	@Override
	public BoundingVolume3F getBoundingVolume() {
		return AxisAlignedBoundingBox3F.union(new AxisAlignedBoundingBox3F(this.position, Point3F.add(this.position, this.sideA)), Point3F.add(this.position, this.sideB));
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Rectangle3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Rectangle3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Vector3F direction = ray.getDirection();
		final Vector3F surfaceNormal = this.surfaceNormal;
		
		final float nDotD = Vector3F.dotProduct(surfaceNormal, direction);
		
		if(isZero(nDotD)) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final Point3F origin = ray.getOrigin();
		final Point3F position = this.position;
		
		final Vector3F originToPosition = Vector3F.direction(origin, position);
		
		final float t = Vector3F.dotProduct(originToPosition, surfaceNormal) / nDotD;
		
		if(t <= tMinimum || t >= tMaximum) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final Vector3F sideA = this.sideA;
		final Vector3F sideB = this.sideB;
		
		final float sideALength = sideA.length();
		final float sideBLength = sideB.length();
		
		final Vector3F sideANormalized = Vector3F.normalize(sideA);
		final Vector3F sideBNormalized = Vector3F.normalize(sideB);
		
		final Point3F surfaceIntersectionPoint = Point3F.add(origin, direction, t);
		
		final Vector3F positionToSurfaceIntersectionPoint = Vector3F.direction(position, surfaceIntersectionPoint);
		
		final float sideX = Vector3F.dotProduct(positionToSurfaceIntersectionPoint, sideANormalized);
		final float sideY = Vector3F.dotProduct(positionToSurfaceIntersectionPoint, sideBNormalized);
		
		if(sideX < 0.0F || sideX > sideALength || sideY < 0.0F || sideY > sideBLength) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final OrthonormalBasis33F orthonormalBasisG = new OrthonormalBasis33F(surfaceNormal);
		final OrthonormalBasis33F orthonormalBasisS = orthonormalBasisG;
		
		final float x = abs(surfaceNormal.getX());
		final float y = abs(surfaceNormal.getY());
		final float z = abs(surfaceNormal.getZ());
		
		final boolean isX = x > y && x > z;
		final boolean isY = y > z;
		
		final Point3F a = position;
		final Point3F b = Point3F.add(a, sideA);
		final Point3F c = Point3F.add(a, sideB);
		
		final float aX = isX ? a.getY()      : isY ? a.getZ()      : a.getX();
		final float aY = isX ? a.getZ()      : isY ? a.getX()      : a.getY();
		final float bX = isX ? c.getY() - aX : isY ? c.getZ() - aX : c.getX() - aX;
		final float bY = isX ? c.getZ() - aY : isY ? c.getX() - aY : c.getY() - aY;
		final float cX = isX ? b.getY() - aX : isY ? b.getZ() - aX : b.getX() - aX;
		final float cY = isX ? b.getZ() - aY : isY ? b.getX() - aY : b.getY() - aY;
		
		final float determinant = bX * cY - bY * cX;
		final float determinantReciprocal = 1.0F / determinant;
		
		final float hU = isX ? surfaceIntersectionPoint.getY() : isY ? surfaceIntersectionPoint.getZ() : surfaceIntersectionPoint.getX();
		final float hV = isX ? surfaceIntersectionPoint.getZ() : isY ? surfaceIntersectionPoint.getX() : surfaceIntersectionPoint.getY();
		
		final float u = hU * (-bY * determinantReciprocal) + hV * (+bX * determinantReciprocal) + (bY * aX - bX * aY) * determinantReciprocal;
		final float v = hU * (+cY * determinantReciprocal) + hV * (-cX * determinantReciprocal) + (cX * aY - cY * aX) * determinantReciprocal;
		
		final Point2F textureCoordinates = new Point2F(u, v);
		
		final Vector3F surfaceIntersectionPointError = new Vector3F();
		
		return Optional.of(new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
	}
	
	/**
	 * Returns the position of this {@code Rectangle3F} instance.
	 * 
	 * @return the position of this {@code Rectangle3F} instance
	 */
	public Point3F getPosition() {
		return this.position;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Rectangle3F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Rectangle3F} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Rectangle3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Rectangle3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Rectangle3F(%s, %s, %s)", this.position, this.sideA, this.sideB);
	}
	
	/**
	 * Returns the direction and length of the side denoted by {@code A}.
	 * 
	 * @return the direction and length of the side denoted by {@code A}
	 */
	public Vector3F getSideA() {
		return this.sideA;
	}
	
	/**
	 * Returns the direction and length of the side denoted by {@code B}.
	 * 
	 * @return the direction and length of the side denoted by {@code B}
	 */
	public Vector3F getSideB() {
		return this.sideB;
	}
	
	/**
	 * Returns the surface normal of this {@code Rectangle3F} instance.
	 * 
	 * @return the surface normal of this {@code Rectangle3F} instance
	 */
	public Vector3F getSurfaceNormal() {
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
	 * Compares {@code object} to this {@code Rectangle3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Rectangle3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Rectangle3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Rectangle3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Rectangle3F)) {
			return false;
		} else if(!Objects.equals(this.position, Rectangle3F.class.cast(object).position)) {
			return false;
		} else if(!Objects.equals(this.sideA, Rectangle3F.class.cast(object).sideA)) {
			return false;
		} else if(!Objects.equals(this.sideB, Rectangle3F.class.cast(object).sideB)) {
			return false;
		} else if(!Objects.equals(this.surfaceNormal, Rectangle3F.class.cast(object).surfaceNormal)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code Rectangle3F} instance.
	 * 
	 * @return the surface area of this {@code Rectangle3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		return Vector3F.crossProduct(this.sideA, this.sideB).length();
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Rectangle3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Rectangle3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Vector3F direction = ray.getDirection();
		final Vector3F surfaceNormal = this.surfaceNormal;
		
		final float nDotD = Vector3F.dotProduct(surfaceNormal, direction);
		
		if(isZero(nDotD)) {
			return Float.NaN;
		}
		
		final Point3F origin = ray.getOrigin();
		final Point3F position = this.position;
		
		final Vector3F originToPosition = Vector3F.direction(origin, position);
		
		final float t = Vector3F.dotProduct(originToPosition, surfaceNormal) / nDotD;
		
		if(t <= tMinimum || t >= tMaximum) {
			return Float.NaN;
		}
		
		final Vector3F sideA = this.sideA;
		final Vector3F sideB = this.sideB;
		
		final float sideALength = sideA.length();
		final float sideBLength = sideB.length();
		
		final Vector3F sideANormalized = Vector3F.normalize(sideA);
		final Vector3F sideBNormalized = Vector3F.normalize(sideB);
		
		final Point3F surfaceIntersectionPoint = Point3F.add(origin, direction, t);
		
		final Vector3F positionToSurfaceIntersectionPoint = Vector3F.direction(position, surfaceIntersectionPoint);
		
		final float sideX = Vector3F.dotProduct(positionToSurfaceIntersectionPoint, sideANormalized);
		final float sideY = Vector3F.dotProduct(positionToSurfaceIntersectionPoint, sideBNormalized);
		
		if(sideX < 0.0F || sideX > sideALength || sideY < 0.0F || sideY > sideBLength) {
			return Float.NaN;
		}
		
		return t;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Rectangle3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Rectangle3F} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Rectangle3F} instance.
	 * 
	 * @return a hash code for this {@code Rectangle3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.position, this.sideA, this.sideB, this.surfaceNormal);
	}
	
	/**
	 * Writes this {@code Rectangle3F} instance to {@code dataOutput}.
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